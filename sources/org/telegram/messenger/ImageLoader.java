package org.telegram.messenger;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Pair;
import android.util.SparseArray;
import androidx.core.graphics.ColorUtils;
import j$.util.concurrent.ConcurrentHashMap;
import j$.util.function.Consumer;
import j$.util.stream.Stream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.zip.GZIPInputStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.DispatchQueuePriority;
import org.telegram.messenger.DocumentObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FilePathDatabase;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.secretmedia.EncryptedFileInputStream;
import org.telegram.messenger.utils.BitmapsCache;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$InputEncryptedFile;
import org.telegram.tgnet.TLRPC$InputFile;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageExtendedMedia;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$StickerSet;
import org.telegram.tgnet.TLRPC$TL_document;
import org.telegram.tgnet.TLRPC$TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC$TL_documentEncrypted;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_fileLocationToBeDeprecated;
import org.telegram.tgnet.TLRPC$TL_fileLocationUnavailable;
import org.telegram.tgnet.TLRPC$TL_messageExtendedMedia;
import org.telegram.tgnet.TLRPC$TL_messageExtendedMediaPreview;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC$TL_messageMediaInvoice;
import org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC$TL_messageMediaWebPage;
import org.telegram.tgnet.TLRPC$TL_photoCachedSize;
import org.telegram.tgnet.TLRPC$TL_photoPathSize;
import org.telegram.tgnet.TLRPC$TL_photoSize_layer127;
import org.telegram.tgnet.TLRPC$TL_photoStrippedSize;
import org.telegram.tgnet.TLRPC$TL_secureFile;
import org.telegram.tgnet.TLRPC$TL_upload_getWebFile;
import org.telegram.tgnet.TLRPC$WallPaper;
import org.telegram.tgnet.TLRPC$WallPaperSettings;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.AnimatedFileDrawable;
import org.telegram.ui.Components.BackgroundGradientDrawable;
import org.telegram.ui.Components.MotionBackgroundDrawable;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.SlotsDrawable;
import org.telegram.ui.Components.ThemePreviewDrawable;
/* loaded from: classes3.dex */
public class ImageLoader {
    public static final String AUTOPLAY_FILTER = "g";
    public static final int CACHE_TYPE_CACHE = 1;
    public static final int CACHE_TYPE_ENCRYPTED = 2;
    public static final int CACHE_TYPE_NONE = 0;
    private static final boolean DEBUG_MODE = false;
    private boolean canForce8888;
    private LruCache<BitmapDrawable> lottieMemCache;
    private LruCache<BitmapDrawable> memCache;
    private LruCache<BitmapDrawable> smallImagesMemCache;
    private LruCache<BitmapDrawable> wallpaperMemCache;
    private static ThreadLocal<byte[]> bytesLocal = new ThreadLocal<>();
    private static ThreadLocal<byte[]> bytesThumbLocal = new ThreadLocal<>();
    private static byte[] header = new byte[12];
    private static byte[] headerThumb = new byte[12];
    private static volatile ImageLoader Instance = null;
    private HashMap<String, Integer> bitmapUseCounts = new HashMap<>();
    ArrayList<AnimatedFileDrawable> cachedAnimatedFileDrawables = new ArrayList<>();
    private HashMap<String, CacheImage> imageLoadingByUrl = new HashMap<>();
    private HashMap<String, CacheImage> imageLoadingByUrlPframe = new HashMap<>();
    public ConcurrentHashMap<String, CacheImage> imageLoadingByKeys = new ConcurrentHashMap<>();
    public HashSet<String> imageLoadingKeys = new HashSet<>();
    private SparseArray<CacheImage> imageLoadingByTag = new SparseArray<>();
    private HashMap<String, ThumbGenerateInfo> waitingForQualityThumb = new HashMap<>();
    private SparseArray<String> waitingForQualityThumbByTag = new SparseArray<>();
    private LinkedList<HttpImageTask> httpTasks = new LinkedList<>();
    private LinkedList<ArtworkLoadTask> artworkTasks = new LinkedList<>();
    private DispatchQueuePriority cacheOutQueue = new DispatchQueuePriority("cacheOutQueue");
    private DispatchQueue cacheThumbOutQueue = new DispatchQueue("cacheThumbOutQueue");
    private DispatchQueue thumbGeneratingQueue = new DispatchQueue("thumbGeneratingQueue");
    private DispatchQueue imageLoadQueue = new DispatchQueue("imageLoadQueue");
    private HashMap<String, String> replacedBitmaps = new HashMap<>();
    private ConcurrentHashMap<String, long[]> fileProgresses = new ConcurrentHashMap<>();
    private HashMap<String, ThumbGenerateTask> thumbGenerateTasks = new HashMap<>();
    private HashMap<String, Integer> forceLoadingImages = new HashMap<>();
    private int currentHttpTasksCount = 0;
    private int currentArtworkTasksCount = 0;
    private ConcurrentHashMap<String, WebFile> testWebFile = new ConcurrentHashMap<>();
    private LinkedList<HttpFileTask> httpFileLoadTasks = new LinkedList<>();
    private HashMap<String, HttpFileTask> httpFileLoadTasksByKeys = new HashMap<>();
    private HashMap<String, Runnable> retryHttpsTasks = new HashMap<>();
    private int currentHttpFileLoadTasksCount = 0;
    private String ignoreRemoval = null;
    private volatile long lastCacheOutTime = 0;
    private int lastImageNum = 0;
    private File telegramPath = null;

    public static boolean hasAutoplayFilter(String str) {
        if (str == null) {
            return false;
        }
        String[] split = str.split("_");
        for (int i = 0; i < split.length; i++) {
            if (AUTOPLAY_FILTER.equals(split[i]) || "pframe".equals(split[i])) {
                return true;
            }
        }
        return false;
    }

    public static Drawable createStripedBitmap(ArrayList<TLRPC$PhotoSize> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i) instanceof TLRPC$TL_photoStrippedSize) {
                return new BitmapDrawable(ApplicationLoader.applicationContext.getResources(), getStrippedPhotoBitmap(((TLRPC$TL_photoStrippedSize) arrayList.get(i)).bytes, "b"));
            }
        }
        return null;
    }

    public static boolean isSdCardPath(File file) {
        return !TextUtils.isEmpty(SharedConfig.storageCacheDir) && file.getAbsolutePath().startsWith(SharedConfig.storageCacheDir);
    }

    public void moveToFront(String str) {
        if (str == null) {
            return;
        }
        if (this.lottieMemCache.get(str) != null) {
            this.lottieMemCache.moveToFront(str);
        }
        if (this.memCache.get(str) != null) {
            this.memCache.moveToFront(str);
        }
        if (this.smallImagesMemCache.get(str) != null) {
            this.smallImagesMemCache.moveToFront(str);
        }
    }

    public void putThumbsToCache(ArrayList<MessageThumb> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            putImageToCache(arrayList.get(i).drawable, arrayList.get(i).key, true);
        }
    }

    public LruCache<BitmapDrawable> getLottieMemCahce() {
        return this.lottieMemCache;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class ThumbGenerateInfo {
        private boolean big;
        private String filter;
        private ArrayList<ImageReceiver> imageReceiverArray;
        private ArrayList<Integer> imageReceiverGuidsArray;
        private TLRPC$Document parentDocument;

        private ThumbGenerateInfo() {
            this.imageReceiverArray = new ArrayList<>();
            this.imageReceiverGuidsArray = new ArrayList<>();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class HttpFileTask extends AsyncTask<Void, Void, Boolean> {
        private int currentAccount;
        private String ext;
        private int fileSize;
        private long lastProgressTime;
        private File tempFile;
        private String url;
        private RandomAccessFile fileOutputStream = null;
        private boolean canRetry = true;

        public HttpFileTask(String str, File file, String str2, int i) {
            this.url = str;
            this.tempFile = file;
            this.ext = str2;
            this.currentAccount = i;
        }

        private void reportProgress(final long j, final long j2) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            if (j != j2) {
                long j3 = this.lastProgressTime;
                if (j3 != 0 && j3 >= elapsedRealtime - 100) {
                    return;
                }
            }
            this.lastProgressTime = elapsedRealtime;
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpFileTask$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpFileTask.this.lambda$reportProgress$1(j, j2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$reportProgress$1(final long j, final long j2) {
            ImageLoader.this.fileProgresses.put(this.url, new long[]{j, j2});
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpFileTask$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpFileTask.this.lambda$reportProgress$0(j, j2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$reportProgress$0(long j, long j2) {
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileLoadProgressChanged, this.url, Long.valueOf(j), Long.valueOf(j2));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Code restructure failed: missing block: B:85:0x0127, code lost:
            if (r5 != (-1)) goto L88;
         */
        /* JADX WARN: Code restructure failed: missing block: B:86:0x0129, code lost:
            r0 = r11.fileSize;
         */
        /* JADX WARN: Code restructure failed: missing block: B:87:0x012b, code lost:
            if (r0 == 0) goto L62;
         */
        /* JADX WARN: Code restructure failed: missing block: B:88:0x012d, code lost:
            r3 = r0;
            reportProgress(r3, r3);
         */
        /* JADX WARN: Code restructure failed: missing block: B:92:0x0134, code lost:
            r0 = e;
         */
        /* JADX WARN: Code restructure failed: missing block: B:93:0x0135, code lost:
            org.telegram.messenger.FileLog.e(r0);
         */
        /* JADX WARN: Removed duplicated region for block: B:115:0x014e A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:124:0x00ae A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:99:0x0142 A[Catch: all -> 0x0148, TRY_LEAVE, TryCatch #5 {all -> 0x0148, blocks: (B:97:0x013e, B:99:0x0142), top: B:122:0x013e }] */
        @Override // android.os.AsyncTask
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public Boolean doInBackground(Void... voidArr) {
            InputStream inputStream;
            HttpURLConnection httpURLConnection;
            InputStream inputStream2;
            List<String> list;
            String str;
            RandomAccessFile randomAccessFile;
            int responseCode;
            boolean z = true;
            boolean z2 = false;
            try {
                httpURLConnection = new URL(this.url).openConnection();
                try {
                    httpURLConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1");
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setReadTimeout(5000);
                    if (httpURLConnection instanceof HttpURLConnection) {
                        HttpURLConnection httpURLConnection2 = (HttpURLConnection) httpURLConnection;
                        httpURLConnection2.setInstanceFollowRedirects(true);
                        int responseCode2 = httpURLConnection2.getResponseCode();
                        if (responseCode2 == 302 || responseCode2 == 301 || responseCode2 == 303) {
                            String headerField = httpURLConnection2.getHeaderField("Location");
                            String headerField2 = httpURLConnection2.getHeaderField("Set-Cookie");
                            httpURLConnection = new URL(headerField).openConnection();
                            httpURLConnection.setRequestProperty("Cookie", headerField2);
                            httpURLConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1");
                        }
                    }
                    httpURLConnection.connect();
                    inputStream2 = httpURLConnection.getInputStream();
                } catch (Throwable th) {
                    th = th;
                    inputStream = null;
                }
            } catch (Throwable th2) {
                th = th2;
                inputStream = null;
                httpURLConnection = null;
            }
            try {
                this.fileOutputStream = new RandomAccessFile(this.tempFile, "rws");
            } catch (Throwable th3) {
                inputStream = inputStream2;
                th = th3;
                if (th instanceof SocketTimeoutException) {
                    if (ApplicationLoader.isNetworkOnline()) {
                        this.canRetry = false;
                    }
                } else if (th instanceof UnknownHostException) {
                    this.canRetry = false;
                } else if (th instanceof SocketException) {
                    if (th.getMessage() != null && th.getMessage().contains("ECONNRESET")) {
                        this.canRetry = false;
                    }
                } else if (th instanceof FileNotFoundException) {
                    this.canRetry = false;
                }
                FileLog.e(th);
                inputStream2 = inputStream;
                if (this.canRetry) {
                }
                return Boolean.valueOf(z2);
            }
            if (this.canRetry) {
                try {
                    if ((httpURLConnection instanceof HttpURLConnection) && (responseCode = httpURLConnection.getResponseCode()) != 200 && responseCode != 202 && responseCode != 304) {
                        this.canRetry = false;
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                }
                if (httpURLConnection != null) {
                    try {
                        Map<String, List<String>> headerFields = httpURLConnection.getHeaderFields();
                        if (headerFields != null && (list = headerFields.get("content-Length")) != null && !list.isEmpty() && (str = list.get(0)) != null) {
                            this.fileSize = Utilities.parseInt((CharSequence) str).intValue();
                        }
                    } catch (Exception e2) {
                        FileLog.e(e2);
                    }
                }
                if (inputStream2 != null) {
                    try {
                        byte[] bArr = new byte[LiteMode.FLAG_CHAT_SCALE];
                        int i = 0;
                        while (true) {
                            try {
                                if (isCancelled()) {
                                    break;
                                }
                                try {
                                    int read = inputStream2.read(bArr);
                                    if (read <= 0) {
                                        break;
                                    }
                                    this.fileOutputStream.write(bArr, 0, read);
                                    i += read;
                                    int i2 = this.fileSize;
                                    if (i2 > 0) {
                                        reportProgress(i, i2);
                                    }
                                } catch (Exception e3) {
                                    e = e3;
                                    z = false;
                                }
                            } catch (Throwable th4) {
                                th = th4;
                                FileLog.e(th);
                                randomAccessFile = this.fileOutputStream;
                                if (randomAccessFile != null) {
                                }
                                if (inputStream2 != null) {
                                }
                                z2 = z;
                                return Boolean.valueOf(z2);
                            }
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        z = false;
                    }
                }
                z = false;
                try {
                    randomAccessFile = this.fileOutputStream;
                    if (randomAccessFile != null) {
                        randomAccessFile.close();
                        this.fileOutputStream = null;
                    }
                } catch (Throwable th6) {
                    FileLog.e(th6);
                }
                if (inputStream2 != null) {
                    try {
                        inputStream2.close();
                    } catch (Throwable th7) {
                        FileLog.e(th7);
                    }
                }
                z2 = z;
            }
            return Boolean.valueOf(z2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Boolean bool) {
            ImageLoader.this.runHttpFileLoadTasks(this, bool.booleanValue() ? 2 : 1);
        }

        @Override // android.os.AsyncTask
        protected void onCancelled() {
            ImageLoader.this.runHttpFileLoadTasks(this, 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class ArtworkLoadTask extends AsyncTask<Void, Void, String> {
        private CacheImage cacheImage;
        private boolean canRetry = true;
        private HttpURLConnection httpConnection;
        private boolean small;

        public ArtworkLoadTask(CacheImage cacheImage) {
            this.cacheImage = cacheImage;
            this.small = Uri.parse(cacheImage.imageLocation.path).getQueryParameter("s") != null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Code restructure failed: missing block: B:105:0x0138, code lost:
            if (r2 != null) goto L75;
         */
        /* JADX WARN: Removed duplicated region for block: B:153:0x0130 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:76:0x00f1 A[Catch: all -> 0x00fa, TryCatch #17 {all -> 0x00fa, blocks: (B:74:0x00ed, B:76:0x00f1, B:78:0x00f7, B:94:0x0122, B:81:0x00fc, B:83:0x0100, B:84:0x0103, B:86:0x0107, B:88:0x010d, B:90:0x0119, B:91:0x011c, B:93:0x0120), top: B:161:0x00ed }] */
        /* JADX WARN: Removed duplicated region for block: B:81:0x00fc A[Catch: all -> 0x00fa, TryCatch #17 {all -> 0x00fa, blocks: (B:74:0x00ed, B:76:0x00f1, B:78:0x00f7, B:94:0x0122, B:81:0x00fc, B:83:0x0100, B:84:0x0103, B:86:0x0107, B:88:0x010d, B:90:0x0119, B:91:0x011c, B:93:0x0120), top: B:161:0x00ed }] */
        /* JADX WARN: Removed duplicated region for block: B:97:0x0129 A[Catch: all -> 0x012d, TRY_LEAVE, TryCatch #12 {all -> 0x012d, blocks: (B:95:0x0125, B:97:0x0129), top: B:151:0x0125 }] */
        @Override // android.os.AsyncTask
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public String doInBackground(Void... voidArr) {
            InputStream inputStream;
            Throwable th;
            InputStream inputStream2;
            ByteArrayOutputStream byteArrayOutputStream;
            HttpURLConnection httpURLConnection;
            JSONArray jSONArray;
            int read;
            int responseCode;
            try {
                HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(this.cacheImage.imageLocation.path.replace("athumb://", "https://")).openConnection();
                this.httpConnection = httpURLConnection2;
                httpURLConnection2.setConnectTimeout(5000);
                this.httpConnection.setReadTimeout(5000);
                this.httpConnection.connect();
                try {
                    HttpURLConnection httpURLConnection3 = this.httpConnection;
                    if (httpURLConnection3 != null && (responseCode = httpURLConnection3.getResponseCode()) != 200 && responseCode != 202 && responseCode != 304) {
                        this.canRetry = false;
                    }
                } catch (Exception e) {
                    FileLog.e((Throwable) e, false);
                }
                inputStream2 = this.httpConnection.getInputStream();
            } catch (Throwable th2) {
                th = th2;
                inputStream = null;
            }
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
            } catch (Throwable th3) {
                inputStream = inputStream2;
                th = th3;
                th = th;
                inputStream2 = inputStream;
                byteArrayOutputStream = null;
                try {
                    if (th instanceof SocketTimeoutException) {
                        if (th instanceof UnknownHostException) {
                            this.canRetry = false;
                        } else if (th instanceof SocketException) {
                            if (th.getMessage() != null && th.getMessage().contains("ECONNRESET")) {
                                this.canRetry = false;
                            }
                        } else if (th instanceof FileNotFoundException) {
                            this.canRetry = false;
                        }
                    } else if (ApplicationLoader.isNetworkOnline()) {
                        this.canRetry = false;
                    }
                    FileLog.e(th, false);
                    try {
                        httpURLConnection = this.httpConnection;
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                    } catch (Throwable unused) {
                    }
                    if (inputStream2 != null) {
                        try {
                            inputStream2.close();
                        } catch (Throwable th4) {
                            FileLog.e(th4);
                        }
                    }
                } finally {
                    try {
                        HttpURLConnection httpURLConnection4 = this.httpConnection;
                        if (httpURLConnection4 != null) {
                            httpURLConnection4.disconnect();
                        }
                    } catch (Throwable unused2) {
                    }
                    if (inputStream2 != null) {
                        try {
                            inputStream2.close();
                        } catch (Throwable th5) {
                            FileLog.e(th5);
                        }
                    }
                    if (byteArrayOutputStream != null) {
                        try {
                            byteArrayOutputStream.close();
                        } catch (Exception unused3) {
                        }
                    }
                }
            }
            try {
                byte[] bArr = new byte[LiteMode.FLAG_CHAT_SCALE];
                while (!isCancelled() && (read = inputStream2.read(bArr)) > 0) {
                    byteArrayOutputStream.write(bArr, 0, read);
                }
                this.canRetry = false;
                jSONArray = new JSONObject(new String(byteArrayOutputStream.toByteArray())).getJSONArray("results");
            } catch (Throwable th6) {
                th = th6;
                if (th instanceof SocketTimeoutException) {
                }
                FileLog.e(th, false);
                httpURLConnection = this.httpConnection;
                if (httpURLConnection != null) {
                }
                if (inputStream2 != null) {
                }
            }
            if (jSONArray.length() > 0) {
                String string = jSONArray.getJSONObject(0).getString("artworkUrl100");
                if (this.small) {
                    try {
                        HttpURLConnection httpURLConnection5 = this.httpConnection;
                        if (httpURLConnection5 != null) {
                            httpURLConnection5.disconnect();
                        }
                    } catch (Throwable unused4) {
                    }
                    if (inputStream2 != null) {
                        try {
                            inputStream2.close();
                        } catch (Throwable th7) {
                            FileLog.e(th7);
                        }
                    }
                    try {
                        byteArrayOutputStream.close();
                    } catch (Exception unused5) {
                    }
                    return string;
                }
                String replace = string.replace("100x100", "600x600");
                try {
                    HttpURLConnection httpURLConnection6 = this.httpConnection;
                    if (httpURLConnection6 != null) {
                        httpURLConnection6.disconnect();
                    }
                } catch (Throwable unused6) {
                }
                if (inputStream2 != null) {
                    try {
                        inputStream2.close();
                    } catch (Throwable th8) {
                        FileLog.e(th8);
                    }
                }
                try {
                    byteArrayOutputStream.close();
                } catch (Exception unused7) {
                }
                return replace;
            }
            try {
                HttpURLConnection httpURLConnection7 = this.httpConnection;
                if (httpURLConnection7 != null) {
                    httpURLConnection7.disconnect();
                }
            } catch (Throwable unused8) {
            }
            if (inputStream2 != null) {
                try {
                    inputStream2.close();
                } catch (Throwable th9) {
                    FileLog.e(th9);
                }
            }
            try {
                byteArrayOutputStream.close();
            } catch (Exception unused9) {
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(final String str) {
            if (str != null) {
                ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$ArtworkLoadTask$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        ImageLoader.ArtworkLoadTask.this.lambda$onPostExecute$0(str);
                    }
                });
            } else if (this.canRetry) {
                ImageLoader.this.artworkLoadError(this.cacheImage.url);
            }
            ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$ArtworkLoadTask$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.ArtworkLoadTask.this.lambda$onPostExecute$1();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPostExecute$0(String str) {
            CacheImage cacheImage = this.cacheImage;
            cacheImage.httpTask = new HttpImageTask(cacheImage, 0, str);
            ImageLoader.this.httpTasks.add(this.cacheImage.httpTask);
            ImageLoader.this.runHttpTasks(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPostExecute$1() {
            ImageLoader.this.runArtworkTasks(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCancelled$2() {
            ImageLoader.this.runArtworkTasks(true);
        }

        @Override // android.os.AsyncTask
        protected void onCancelled() {
            ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$ArtworkLoadTask$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.ArtworkLoadTask.this.lambda$onCancelled$2();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class HttpImageTask extends AsyncTask<Void, Void, Boolean> {
        private CacheImage cacheImage;
        private boolean canRetry = true;
        private RandomAccessFile fileOutputStream;
        private HttpURLConnection httpConnection;
        private long imageSize;
        private long lastProgressTime;
        private String overrideUrl;

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$doInBackground$2(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        }

        public HttpImageTask(CacheImage cacheImage, long j) {
            this.cacheImage = cacheImage;
            this.imageSize = j;
        }

        public HttpImageTask(CacheImage cacheImage, int i, String str) {
            this.cacheImage = cacheImage;
            this.imageSize = i;
            this.overrideUrl = str;
        }

        private void reportProgress(final long j, final long j2) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            if (j != j2) {
                long j3 = this.lastProgressTime;
                if (j3 != 0 && j3 >= elapsedRealtime - 100) {
                    return;
                }
            }
            this.lastProgressTime = elapsedRealtime;
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpImageTask.this.lambda$reportProgress$1(j, j2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$reportProgress$1(final long j, final long j2) {
            ImageLoader.this.fileProgresses.put(this.cacheImage.url, new long[]{j, j2});
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpImageTask.this.lambda$reportProgress$0(j, j2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$reportProgress$0(long j, long j2) {
            NotificationCenter.getInstance(this.cacheImage.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileLoadProgressChanged, this.cacheImage.url, Long.valueOf(j), Long.valueOf(j2));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't wrap try/catch for region: R(17:1|(9:101|102|(6:104|(1:106)|107|(1:109)|110|(16:112|114|115|4|(6:35|36|(1:44)|46|(3:50|51|(1:59))|(14:64|65|66|(2:67|(1:97)(3:69|70|(3:72|(3:74|75|76)(1:78)|77)(1:79)))|7|8|(1:10)|12|13|(1:15)|(2:27|28)|(1:23)|24|25)(0))(0)|6|7|8|(0)|12|13|(0)|(0)|(3:19|21|23)|24|25))|142|(1:148)|107|(0)|110|(0))|3|4|(0)(0)|6|7|8|(0)|12|13|(0)|(0)|(0)|24|25|(1:(0))) */
        /* JADX WARN: Code restructure failed: missing block: B:100:0x0172, code lost:
            if (r7 != (-1)) goto L6;
         */
        /* JADX WARN: Code restructure failed: missing block: B:101:0x0174, code lost:
            r2 = r12.imageSize;
         */
        /* JADX WARN: Code restructure failed: missing block: B:102:0x0178, code lost:
            if (r2 == 0) goto L7;
         */
        /* JADX WARN: Code restructure failed: missing block: B:103:0x017a, code lost:
            reportProgress(r2, r2);
         */
        /* JADX WARN: Code restructure failed: missing block: B:105:0x017e, code lost:
            r2 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:106:0x017f, code lost:
            r0 = r2;
            r2 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:107:0x0182, code lost:
            r2 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:108:0x0183, code lost:
            r0 = r2;
            r2 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:109:0x0185, code lost:
            org.telegram.messenger.FileLog.e(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:110:0x0188, code lost:
            r0 = r2;
         */
        /* JADX WARN: Code restructure failed: missing block: B:111:0x018a, code lost:
            org.telegram.messenger.FileLog.e(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:117:0x0199, code lost:
            r1 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:118:0x019a, code lost:
            org.telegram.messenger.FileLog.e(r1);
         */
        /* JADX WARN: Removed duplicated region for block: B:115:0x0193 A[Catch: all -> 0x0199, TRY_LEAVE, TryCatch #5 {all -> 0x0199, blocks: (B:113:0x018f, B:115:0x0193), top: B:147:0x018f }] */
        /* JADX WARN: Removed duplicated region for block: B:121:0x01a1 A[Catch: all -> 0x01a5, TRY_LEAVE, TryCatch #2 {all -> 0x01a5, blocks: (B:119:0x019d, B:121:0x01a1), top: B:141:0x019d }] */
        /* JADX WARN: Removed duplicated region for block: B:130:0x01b2  */
        /* JADX WARN: Removed duplicated region for block: B:143:0x00f3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:149:0x01a8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:20:0x0065  */
        /* JADX WARN: Removed duplicated region for block: B:23:0x008f A[Catch: all -> 0x0020, TRY_LEAVE, TryCatch #0 {all -> 0x0020, blocks: (B:4:0x0009, B:6:0x0017, B:18:0x005f, B:21:0x0066, B:23:0x008f, B:11:0x0024, B:15:0x0034, B:17:0x0042), top: B:137:0x0009 }] */
        @Override // android.os.AsyncTask
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public Boolean doInBackground(Void... voidArr) {
            InputStream inputStream;
            boolean z;
            InputStream inputStream2;
            String str;
            WebFile webFile;
            String str2;
            HttpURLConnection httpURLConnection;
            List<String> list;
            String str3;
            int responseCode;
            CacheImage cacheImage;
            File file;
            HttpURLConnection httpURLConnection2;
            RandomAccessFile randomAccessFile;
            boolean z2 = true;
            boolean z3 = false;
            if (!isCancelled()) {
                try {
                    str = this.cacheImage.imageLocation.path;
                } catch (Throwable th) {
                    th = th;
                    inputStream = null;
                }
                if (!str.startsWith("https://static-maps")) {
                    if (str.startsWith("https://maps.googleapis")) {
                    }
                    str2 = this.overrideUrl;
                    if (str2 != null) {
                        str = str2;
                    }
                    HttpURLConnection httpURLConnection3 = (HttpURLConnection) new URL(str).openConnection();
                    this.httpConnection = httpURLConnection3;
                    httpURLConnection3.addRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1");
                    this.httpConnection.setConnectTimeout(5000);
                    this.httpConnection.setReadTimeout(5000);
                    this.httpConnection.setInstanceFollowRedirects(true);
                    if (!isCancelled()) {
                        this.httpConnection.connect();
                        inputStream2 = this.httpConnection.getInputStream();
                        try {
                            this.fileOutputStream = new RandomAccessFile(this.cacheImage.tempFilePath, "rws");
                        } catch (Throwable th2) {
                            inputStream = inputStream2;
                            th = th2;
                            if (th instanceof SocketTimeoutException) {
                                if (ApplicationLoader.isNetworkOnline()) {
                                    this.canRetry = false;
                                }
                            } else if (th instanceof UnknownHostException) {
                                this.canRetry = false;
                            } else if (th instanceof SocketException) {
                                if (th.getMessage() != null && th.getMessage().contains("ECONNRESET")) {
                                    this.canRetry = false;
                                }
                            } else if (th instanceof FileNotFoundException) {
                                this.canRetry = false;
                            } else if (!(th instanceof InterruptedIOException)) {
                                z = true;
                                FileLog.e(th, z);
                                inputStream2 = inputStream;
                                if (!isCancelled()) {
                                }
                                z2 = false;
                                randomAccessFile = this.fileOutputStream;
                                if (randomAccessFile != null) {
                                }
                                httpURLConnection2 = this.httpConnection;
                                if (httpURLConnection2 != null) {
                                }
                                if (inputStream2 != null) {
                                }
                                if (z2) {
                                }
                                return Boolean.valueOf(z2);
                            }
                            z = false;
                            FileLog.e(th, z);
                            inputStream2 = inputStream;
                            if (!isCancelled()) {
                            }
                            z2 = false;
                            randomAccessFile = this.fileOutputStream;
                            if (randomAccessFile != null) {
                            }
                            httpURLConnection2 = this.httpConnection;
                            if (httpURLConnection2 != null) {
                            }
                            if (inputStream2 != null) {
                            }
                            if (z2) {
                            }
                            return Boolean.valueOf(z2);
                        }
                        if (!isCancelled()) {
                            try {
                                HttpURLConnection httpURLConnection4 = this.httpConnection;
                                if (httpURLConnection4 != null && (responseCode = httpURLConnection4.getResponseCode()) != 200 && responseCode != 202 && responseCode != 304) {
                                    this.canRetry = false;
                                }
                            } catch (Exception e) {
                                FileLog.e((Throwable) e, false);
                            }
                            if (this.imageSize == 0 && (httpURLConnection = this.httpConnection) != null) {
                                try {
                                    Map<String, List<String>> headerFields = httpURLConnection.getHeaderFields();
                                    if (headerFields != null && (list = headerFields.get("content-Length")) != null && !list.isEmpty() && (str3 = list.get(0)) != null) {
                                        this.imageSize = Utilities.parseInt((CharSequence) str3).intValue();
                                    }
                                } catch (Exception e2) {
                                    FileLog.e(e2);
                                }
                            }
                            if (inputStream2 != null) {
                                try {
                                    byte[] bArr = new byte[LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM];
                                    int i = 0;
                                    while (true) {
                                        if (isCancelled()) {
                                            break;
                                        }
                                        try {
                                            int read = inputStream2.read(bArr);
                                            if (read <= 0) {
                                                break;
                                            }
                                            i += read;
                                            this.fileOutputStream.write(bArr, 0, read);
                                            long j = this.imageSize;
                                            if (j != 0) {
                                                reportProgress(i, j);
                                            }
                                        } catch (Exception e3) {
                                            e = e3;
                                        }
                                    }
                                } catch (Throwable th3) {
                                    th = th3;
                                }
                                randomAccessFile = this.fileOutputStream;
                                if (randomAccessFile != null) {
                                    randomAccessFile.close();
                                    this.fileOutputStream = null;
                                }
                                httpURLConnection2 = this.httpConnection;
                                if (httpURLConnection2 != null) {
                                    httpURLConnection2.disconnect();
                                }
                                if (inputStream2 != null) {
                                    try {
                                        inputStream2.close();
                                    } catch (Throwable th4) {
                                        FileLog.e(th4);
                                    }
                                }
                                if (z2 && (file = (cacheImage = this.cacheImage).tempFilePath) != null && !file.renameTo(cacheImage.finalFilePath)) {
                                    CacheImage cacheImage2 = this.cacheImage;
                                    cacheImage2.finalFilePath = cacheImage2.tempFilePath;
                                }
                                return Boolean.valueOf(z2);
                            }
                        }
                        z2 = false;
                        randomAccessFile = this.fileOutputStream;
                        if (randomAccessFile != null) {
                        }
                        httpURLConnection2 = this.httpConnection;
                        if (httpURLConnection2 != null) {
                        }
                        if (inputStream2 != null) {
                        }
                        if (z2) {
                            CacheImage cacheImage22 = this.cacheImage;
                            cacheImage22.finalFilePath = cacheImage22.tempFilePath;
                        }
                        return Boolean.valueOf(z2);
                    }
                }
                int i2 = MessagesController.getInstance(this.cacheImage.currentAccount).mapProvider;
                if ((i2 == 3 || i2 == 4) && (webFile = (WebFile) ImageLoader.this.testWebFile.get(str)) != null) {
                    TLRPC$TL_upload_getWebFile tLRPC$TL_upload_getWebFile = new TLRPC$TL_upload_getWebFile();
                    tLRPC$TL_upload_getWebFile.location = webFile.location;
                    tLRPC$TL_upload_getWebFile.offset = 0;
                    tLRPC$TL_upload_getWebFile.limit = 0;
                    ConnectionsManager.getInstance(this.cacheImage.currentAccount).sendRequest(tLRPC$TL_upload_getWebFile, new RequestDelegate() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda0
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            ImageLoader.HttpImageTask.lambda$doInBackground$2(tLObject, tLRPC$TL_error);
                        }
                    });
                }
                str2 = this.overrideUrl;
                if (str2 != null) {
                }
                HttpURLConnection httpURLConnection32 = (HttpURLConnection) new URL(str).openConnection();
                this.httpConnection = httpURLConnection32;
                httpURLConnection32.addRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1");
                this.httpConnection.setConnectTimeout(5000);
                this.httpConnection.setReadTimeout(5000);
                this.httpConnection.setInstanceFollowRedirects(true);
                if (!isCancelled()) {
                }
            }
            inputStream2 = null;
            if (!isCancelled()) {
            }
            z2 = false;
            randomAccessFile = this.fileOutputStream;
            if (randomAccessFile != null) {
            }
            httpURLConnection2 = this.httpConnection;
            if (httpURLConnection2 != null) {
            }
            if (inputStream2 != null) {
            }
            if (z2) {
            }
            return Boolean.valueOf(z2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(final Boolean bool) {
            if (!bool.booleanValue() && this.canRetry) {
                ImageLoader.this.httpFileLoadError(this.cacheImage.url);
            } else {
                ImageLoader imageLoader = ImageLoader.this;
                CacheImage cacheImage = this.cacheImage;
                imageLoader.fileDidLoaded(cacheImage.url, cacheImage.finalFilePath, 0);
            }
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpImageTask.this.lambda$onPostExecute$4(bool);
                }
            });
            ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpImageTask.this.lambda$onPostExecute$5();
                }
            }, this.cacheImage.priority);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPostExecute$4(final Boolean bool) {
            ImageLoader.this.fileProgresses.remove(this.cacheImage.url);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpImageTask.this.lambda$onPostExecute$3(bool);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPostExecute$3(Boolean bool) {
            if (bool.booleanValue()) {
                NotificationCenter notificationCenter = NotificationCenter.getInstance(this.cacheImage.currentAccount);
                int i = NotificationCenter.fileLoaded;
                CacheImage cacheImage = this.cacheImage;
                notificationCenter.lambda$postNotificationNameOnUIThread$1(i, cacheImage.url, cacheImage.finalFilePath);
                return;
            }
            NotificationCenter.getInstance(this.cacheImage.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileLoadFailed, this.cacheImage.url, 2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPostExecute$5() {
            ImageLoader.this.runHttpTasks(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCancelled$6() {
            ImageLoader.this.runHttpTasks(true);
        }

        @Override // android.os.AsyncTask
        protected void onCancelled() {
            ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpImageTask.this.lambda$onCancelled$6();
                }
            }, this.cacheImage.priority);
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpImageTask.this.lambda$onCancelled$8();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCancelled$8() {
            ImageLoader.this.fileProgresses.remove(this.cacheImage.url);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpImageTask.this.lambda$onCancelled$7();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCancelled$7() {
            NotificationCenter.getInstance(this.cacheImage.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileLoadFailed, this.cacheImage.url, 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class ThumbGenerateTask implements Runnable {
        private ThumbGenerateInfo info;
        private int mediaType;
        private File originalPath;

        public ThumbGenerateTask(int i, File file, ThumbGenerateInfo thumbGenerateInfo) {
            this.mediaType = i;
            this.originalPath = file;
            this.info = thumbGenerateInfo;
        }

        private void removeTask() {
            ThumbGenerateInfo thumbGenerateInfo = this.info;
            if (thumbGenerateInfo == null) {
                return;
            }
            final String attachFileName = FileLoader.getAttachFileName(thumbGenerateInfo.parentDocument);
            ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$ThumbGenerateTask$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.ThumbGenerateTask.this.lambda$removeTask$0(attachFileName);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$removeTask$0(String str) {
            ImageLoader.this.thumbGenerateTasks.remove(str);
        }

        /* JADX WARN: Removed duplicated region for block: B:48:0x00ff A[Catch: all -> 0x000a, TryCatch #1 {all -> 0x000a, blocks: (B:3:0x0002, B:5:0x0006, B:9:0x000d, B:11:0x0055, B:14:0x005f, B:16:0x0067, B:18:0x0083, B:20:0x008a, B:48:0x00ff, B:50:0x0103, B:54:0x0110, B:56:0x0121, B:58:0x012b, B:59:0x012f, B:63:0x0143, B:64:0x0146, B:68:0x014e, B:67:0x014b, B:69:0x0174, B:24:0x009a, B:28:0x00aa, B:31:0x00b2, B:33:0x00c4, B:37:0x00d4, B:38:0x00d9, B:40:0x00df, B:42:0x00e7, B:44:0x00ef, B:46:0x00f7, B:17:0x0072, B:71:0x0178), top: B:77:0x0002, inners: #0 }] */
        /* JADX WARN: Removed duplicated region for block: B:50:0x0103 A[Catch: all -> 0x000a, TryCatch #1 {all -> 0x000a, blocks: (B:3:0x0002, B:5:0x0006, B:9:0x000d, B:11:0x0055, B:14:0x005f, B:16:0x0067, B:18:0x0083, B:20:0x008a, B:48:0x00ff, B:50:0x0103, B:54:0x0110, B:56:0x0121, B:58:0x012b, B:59:0x012f, B:63:0x0143, B:64:0x0146, B:68:0x014e, B:67:0x014b, B:69:0x0174, B:24:0x009a, B:28:0x00aa, B:31:0x00b2, B:33:0x00c4, B:37:0x00d4, B:38:0x00d9, B:40:0x00df, B:42:0x00e7, B:44:0x00ef, B:46:0x00f7, B:17:0x0072, B:71:0x0178), top: B:77:0x0002, inners: #0 }] */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void run() {
            int min;
            Bitmap loadBitmap;
            Bitmap createScaledBitmap;
            try {
                if (this.info == null) {
                    removeTask();
                    return;
                }
                final String str = "q_" + this.info.parentDocument.dc_id + "_" + this.info.parentDocument.id;
                File file = new File(FileLoader.getDirectory(4), str + ".jpg");
                if (!file.exists() && this.originalPath.exists()) {
                    if (this.info.big) {
                        Point point = AndroidUtilities.displaySize;
                        min = Math.max(point.x, point.y);
                    } else {
                        Point point2 = AndroidUtilities.displaySize;
                        min = Math.min((int) NotificationCenter.updateBotMenuButton, Math.min(point2.x, point2.y) / 4);
                    }
                    int i = this.mediaType;
                    Bitmap bitmap = null;
                    if (i == 0) {
                        float f = min;
                        loadBitmap = ImageLoader.loadBitmap(this.originalPath.toString(), null, f, f, false);
                    } else {
                        int i2 = 2;
                        if (i != 2) {
                            if (i == 3) {
                                String lowerCase = this.originalPath.toString().toLowerCase();
                                if (lowerCase.endsWith("mp4")) {
                                    String file2 = this.originalPath.toString();
                                    if (!this.info.big) {
                                        i2 = 1;
                                    }
                                    loadBitmap = SendMessagesHelper.createVideoThumbnail(file2, i2);
                                } else if (lowerCase.endsWith(".jpg") || lowerCase.endsWith(".jpeg") || lowerCase.endsWith(".png") || lowerCase.endsWith(".gif")) {
                                    float f2 = min;
                                    loadBitmap = ImageLoader.loadBitmap(lowerCase, null, f2, f2, false);
                                }
                            }
                            if (bitmap != null) {
                                removeTask();
                                return;
                            }
                            int width = bitmap.getWidth();
                            int height = bitmap.getHeight();
                            if (width != 0 && height != 0) {
                                float f3 = width;
                                float f4 = min;
                                float f5 = height;
                                float min2 = Math.min(f3 / f4, f5 / f4);
                                if (min2 > 1.0f && (createScaledBitmap = Bitmaps.createScaledBitmap(bitmap, (int) (f3 / min2), (int) (f5 / min2), true)) != bitmap) {
                                    bitmap.recycle();
                                    bitmap = createScaledBitmap;
                                }
                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, this.info.big ? 83 : 60, fileOutputStream);
                                try {
                                    fileOutputStream.close();
                                } catch (Exception e) {
                                    FileLog.e(e);
                                }
                                final BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                                final ArrayList arrayList = new ArrayList(this.info.imageReceiverArray);
                                final ArrayList arrayList2 = new ArrayList(this.info.imageReceiverGuidsArray);
                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$ThumbGenerateTask$$ExternalSyntheticLambda0
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        ImageLoader.ThumbGenerateTask.this.lambda$run$1(str, arrayList, bitmapDrawable, arrayList2);
                                    }
                                });
                                return;
                            }
                            removeTask();
                            return;
                        }
                        String file3 = this.originalPath.toString();
                        if (!this.info.big) {
                            i2 = 1;
                        }
                        loadBitmap = SendMessagesHelper.createVideoThumbnail(file3, i2);
                    }
                    bitmap = loadBitmap;
                    if (bitmap != null) {
                    }
                }
                removeTask();
            } catch (Throwable th) {
                FileLog.e(th);
                removeTask();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$1(String str, ArrayList arrayList, BitmapDrawable bitmapDrawable, ArrayList arrayList2) {
            removeTask();
            if (this.info.filter != null) {
                str = str + "@" + this.info.filter;
            }
            for (int i = 0; i < arrayList.size(); i++) {
                ((ImageReceiver) arrayList.get(i)).setImageBitmapByKey(bitmapDrawable, str, 0, false, ((Integer) arrayList2.get(i)).intValue());
            }
            ImageLoader.this.memCache.put(str, bitmapDrawable);
        }
    }

    public static String decompressGzip(File file) {
        StringBuilder sb = new StringBuilder();
        if (file == null) {
            return "";
        }
        try {
            GZIPInputStream gZIPInputStream = new GZIPInputStream(new FileInputStream(file));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gZIPInputStream, "UTF-8"));
            while (true) {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        sb.append(readLine);
                    } else {
                        String sb2 = sb.toString();
                        bufferedReader.close();
                        gZIPInputStream.close();
                        return sb2;
                    }
                } catch (Throwable th) {
                    try {
                        bufferedReader.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            }
        } catch (Exception unused) {
            return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class CacheOutTask implements Runnable {
        private CacheImage cacheImage;
        private boolean isCancelled;
        private Thread runningThread;
        private final Object sync = new Object();

        public CacheOutTask(CacheImage cacheImage) {
            this.cacheImage = cacheImage;
        }

        /* JADX WARN: Can't wrap try/catch for region: R(10:427|(2:429|(7:435|436|437|(1:662)(2:(2:441|(4:443|444|445|446)(1:659))(1:661)|660)|(3:(13:607|(1:609)(1:(1:650)(2:651|652))|610|(5:612|(1:614)|617|618|619)|623|624|625|(3:636|637|(1:639)(3:640|618|619))|627|628|(1:630)(1:(1:632))|618|619)(1:449)|(8:566|567|(1:604)(1:571)|(1:573)|574|575|(7:577|578|579|(3:584|585|586)|591|585|586)(3:593|(1:595)|596)|(2:588|589))(1:451)|452)(1:655)|453|(11:(9:498|463|(0)|(0)|479|(0)|(0)(0)|495|496)|461|462|463|(0)|(0)|479|(0)|(0)(0)|495|496)(14:499|(3:501|(10:509|(4:519|(2:521|(1:517))|518|(0))(4:513|(2:515|(0))|518|(0))|463|(0)|(0)|479|(0)|(0)(0)|495|496)|(15:(5:524|(1:526)(1:560)|527|(1:529)(1:559)|(2:557|558))(1:561)|531|(1:537)|(1:556)(3:543|(1:545)|546)|(1:550)|555|462|463|(0)|(0)|479|(0)|(0)(0)|495|496))|562|(2:548|550)|555|462|463|(0)|(0)|479|(0)|(0)(0)|495|496)))|663|436|437|(0)|662|(0)(0)|453|(0)(0)) */
        /* JADX WARN: Can't wrap try/catch for region: R(11:920|(2:922|(9:924|925|926|(1:928)(1:944)|929|(2:935|(1:937))|(1:939)(1:942)|940|941))|947|925|926|(0)(0)|929|(4:931|933|935|(0))|(0)(0)|940|941) */
        /* JADX WARN: Can't wrap try/catch for region: R(14:70|(7:71|72|73|74|(1:76)(1:117)|77|78)|(3:80|81|(6:83|84|85|(1:111)(1:88)|(3:96|(1:110)(4:99|(1:103)|104|(1:108))|109)(1:92)|(1:94)(1:95)))|116|84|85|(0)|111|(0)|96|(0)|110|109|(0)(0)) */
        /* JADX WARN: Can't wrap try/catch for region: R(16:328|(1:919)(1:335)|336|(3:338|(1:917)|342)(1:918)|343|(12:345|(3:347|(1:349)(1:906)|350)(4:907|(2:909|(2:911|912))(1:914)|913|912)|351|352|(1:354)|355|356|357|(14:359|(5:361|362|363|364|365)(1:872)|366|367|(1:369)(2:852|(1:854)(2:855|(1:857)(2:858|(1:860)(1:861))))|370|371|372|373|(1:375)(2:843|(1:845))|376|377|(1:842)(12:381|382|383|385|(2:800|(13:808|809|(1:831)(1:813)|(1:815)|816|817|818|819|(4:824|825|826|(1:828))|829|825|826|(0))(4:802|(1:804)(1:807)|805|806))(2:(4:790|791|792|793)(1:389)|390)|391|(1:789)(1:395)|396|397|(1:399)|400|(1:788)(3:406|(2:407|(1:410)(1:409))|411))|412)(3:873|(11:875|876|877|(1:879)(1:899)|880|881|882|(1:884)|885|(3:887|(2:888|(1:891)(1:890))|892)(1:895)|893)(1:903)|894)|413|414|(3:675|676|852)(6:416|(1:418)(1:674)|(3:664|665|(4:667|(1:671)|421|ad9))|420|421|ad9))|916|352|(0)|355|356|357|(0)(0)|413|414|(0)(0)) */
        /* JADX WARN: Code restructure failed: missing block: B:123:0x0211, code lost:
            r0 = e;
         */
        /* JADX WARN: Code restructure failed: missing block: B:359:0x05aa, code lost:
            if (r0.startsWith("http") == false) goto L913;
         */
        /* JADX WARN: Code restructure failed: missing block: B:381:0x0601, code lost:
            r0 = th;
         */
        /* JADX WARN: Code restructure failed: missing block: B:382:0x0602, code lost:
            r23 = r5;
            r25 = r11;
         */
        /* JADX WARN: Code restructure failed: missing block: B:709:0x0b95, code lost:
            if (r4.contains("exif") != false) goto L623;
         */
        /* JADX WARN: Code restructure failed: missing block: B:785:0x0c8c, code lost:
            r0 = th;
         */
        /* JADX WARN: Code restructure failed: missing block: B:786:0x0c8d, code lost:
            r5 = r5;
         */
        /* JADX WARN: Code restructure failed: missing block: B:901:0x0e58, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:902:0x0e59, code lost:
            org.telegram.messenger.FileLog.e(r0);
            r2 = null;
         */
        /* JADX WARN: Not initialized variable reg: 37, insn: 0x08d2: MOVE  (r5 I:??[OBJECT, ARRAY]) = (r37 I:??[OBJECT, ARRAY]), block:B:568:0x08d2 */
        /* JADX WARN: Removed duplicated region for block: B:140:0x0231 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:145:0x0239 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:151:0x0248 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:165:0x0273  */
        /* JADX WARN: Removed duplicated region for block: B:166:0x0292  */
        /* JADX WARN: Removed duplicated region for block: B:168:0x02af A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:198:0x0310  */
        /* JADX WARN: Removed duplicated region for block: B:212:0x0341  */
        /* JADX WARN: Removed duplicated region for block: B:225:0x0370  */
        /* JADX WARN: Removed duplicated region for block: B:226:0x0375  */
        /* JADX WARN: Removed duplicated region for block: B:296:0x0489  */
        /* JADX WARN: Removed duplicated region for block: B:297:0x048b  */
        /* JADX WARN: Removed duplicated region for block: B:299:0x0493  */
        /* JADX WARN: Removed duplicated region for block: B:300:0x0495  */
        /* JADX WARN: Removed duplicated region for block: B:303:0x049c  */
        /* JADX WARN: Removed duplicated region for block: B:304:0x049f  */
        /* JADX WARN: Removed duplicated region for block: B:307:0x04ab  */
        /* JADX WARN: Removed duplicated region for block: B:308:0x04ae  */
        /* JADX WARN: Removed duplicated region for block: B:319:0x04f6  */
        /* JADX WARN: Removed duplicated region for block: B:323:0x0513  */
        /* JADX WARN: Removed duplicated region for block: B:365:0x05c3  */
        /* JADX WARN: Removed duplicated region for block: B:369:0x05d3 A[Catch: all -> 0x0601, TryCatch #21 {all -> 0x0601, blocks: (B:367:0x05cd, B:369:0x05d3, B:371:0x05dc), top: B:950:0x05cd }] */
        /* JADX WARN: Removed duplicated region for block: B:453:0x0719 A[Catch: all -> 0x070d, TryCatch #30 {all -> 0x070d, blocks: (B:462:0x0740, B:466:0x074e, B:472:0x0769, B:479:0x0779, B:481:0x0783, B:483:0x0788, B:468:0x075a, B:441:0x06fd, B:443:0x0703, B:451:0x0712, B:453:0x0719, B:458:0x0729, B:461:0x073a, B:460:0x0734), top: B:964:0x0676 }] */
        /* JADX WARN: Removed duplicated region for block: B:497:0x07c2  */
        /* JADX WARN: Removed duplicated region for block: B:642:0x0a8e  */
        /* JADX WARN: Removed duplicated region for block: B:661:0x0ada A[Catch: all -> 0x0ae0, TRY_ENTER, TryCatch #24 {all -> 0x0ac3, blocks: (B:647:0x0a9a, B:649:0x0aa6, B:651:0x0ab9, B:653:0x0abf, B:659:0x0ace, B:660:0x0ad9, B:669:0x0ae6, B:672:0x0aee, B:675:0x0af5, B:678:0x0b00, B:682:0x0b08, B:684:0x0b12, B:689:0x0b3e, B:691:0x0b4f, B:677:0x0afb, B:661:0x0ada, B:663:0x0ade, B:667:0x0ae3), top: B:956:0x0a9a }] */
        /* JADX WARN: Removed duplicated region for block: B:695:0x0b63  */
        /* JADX WARN: Removed duplicated region for block: B:777:0x0c74  */
        /* JADX WARN: Removed duplicated region for block: B:779:0x0c79  */
        /* JADX WARN: Removed duplicated region for block: B:789:0x0c9a A[Catch: all -> 0x0c8c, TryCatch #28 {all -> 0x0c8c, blocks: (B:780:0x0c7b, B:782:0x0c85, B:787:0x0c8f, B:789:0x0c9a, B:791:0x0ca0, B:797:0x0cb7, B:803:0x0cc7, B:805:0x0ccd, B:811:0x0cec, B:806:0x0cd9, B:808:0x0cdf, B:814:0x0cf4, B:816:0x0d02, B:818:0x0d0d), top: B:961:0x0c77 }] */
        /* JADX WARN: Removed duplicated region for block: B:80:0x0159  */
        /* JADX WARN: Removed duplicated region for block: B:811:0x0cec A[Catch: all -> 0x0c8c, TryCatch #28 {all -> 0x0c8c, blocks: (B:780:0x0c7b, B:782:0x0c85, B:787:0x0c8f, B:789:0x0c9a, B:791:0x0ca0, B:797:0x0cb7, B:803:0x0cc7, B:805:0x0ccd, B:811:0x0cec, B:806:0x0cd9, B:808:0x0cdf, B:814:0x0cf4, B:816:0x0d02, B:818:0x0d0d), top: B:961:0x0c77 }] */
        /* JADX WARN: Removed duplicated region for block: B:857:0x0d9b A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:860:0x0da9  */
        /* JADX WARN: Removed duplicated region for block: B:861:0x0dab  */
        /* JADX WARN: Removed duplicated region for block: B:864:0x0dc0  */
        /* JADX WARN: Removed duplicated region for block: B:870:0x0ddf  */
        /* JADX WARN: Removed duplicated region for block: B:873:0x0de9  */
        /* JADX WARN: Removed duplicated region for block: B:886:0x0e0c  */
        /* JADX WARN: Removed duplicated region for block: B:887:0x0e12  */
        /* JADX WARN: Removed duplicated region for block: B:897:0x0e50  */
        /* JADX WARN: Removed duplicated region for block: B:898:0x0e52  */
        /* JADX WARN: Removed duplicated region for block: B:910:0x0e7e  */
        /* JADX WARN: Removed duplicated region for block: B:912:0x0e86  */
        /* JADX WARN: Removed duplicated region for block: B:913:0x0e8c  */
        /* JADX WARN: Removed duplicated region for block: B:957:0x02c7 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:980:0x0847 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void run() {
            Drawable drawable;
            Bitmap bitmap;
            Object obj;
            Bitmap bitmap2;
            byte[] bArr;
            String str;
            Long l;
            boolean z;
            boolean z2;
            String str2;
            Long l2;
            float f;
            Bitmap bitmap3;
            float f2;
            char c;
            boolean z3;
            int i;
            float f3;
            float f4;
            boolean z4;
            boolean z5;
            Bitmap bitmap4;
            Bitmap bitmap5;
            int i2;
            Bitmap bitmap6;
            int i3;
            Bitmap bitmap7;
            Bitmap bitmap8;
            boolean z6;
            boolean z7;
            int i4;
            Bitmap bitmap9;
            Bitmap createScaledBitmap;
            int i5;
            FileInputStream fileInputStream;
            Bitmap bitmap10;
            CacheImage cacheImage;
            String str3;
            Object obj2;
            float f5;
            boolean z8;
            boolean z9;
            Bitmap createScaledBitmap2;
            FileInputStream fileInputStream2;
            Pair<Integer, Integer> imageOrientation;
            int intValue;
            boolean z10;
            int i6;
            Bitmap thumbnail;
            String str4;
            int i7;
            float f6;
            boolean z11;
            boolean z12;
            boolean z13;
            FileInputStream fileInputStream3;
            float min;
            int i8;
            Long l3;
            boolean z14;
            String str5;
            boolean z15;
            boolean z16;
            boolean z17;
            boolean z18;
            boolean z19;
            Bitmap bitmap11;
            MediaMetadataRetriever mediaMetadataRetriever;
            BitmapsCache.CacheOptions cacheOptions;
            AnimatedFileDrawable animatedFileDrawable;
            int i9;
            int i10;
            String str6;
            int i11;
            int i12;
            int i13;
            int i14;
            boolean z20;
            boolean z21;
            boolean z22;
            String str7;
            boolean z23;
            boolean z24;
            int i15;
            RandomAccessFile randomAccessFile;
            boolean z25;
            RandomAccessFile randomAccessFile2;
            Throwable th;
            boolean z26;
            BitmapsCache.CacheOptions cacheOptions2;
            BitmapsCache.CacheOptions cacheOptions3;
            RLottieDrawable rLottieDrawable;
            byte[] bArr2;
            RLottieDrawable rLottieDrawable2;
            boolean z27;
            boolean z28;
            synchronized (this.sync) {
                try {
                    this.runningThread = Thread.currentThread();
                    Thread.interrupted();
                    if (this.isCancelled) {
                        return;
                    }
                    CacheImage cacheImage2 = this.cacheImage;
                    ImageLocation imageLocation = cacheImage2.imageLocation;
                    TLRPC$PhotoSize tLRPC$PhotoSize = imageLocation.photoSize;
                    if (tLRPC$PhotoSize instanceof TLRPC$TL_photoStrippedSize) {
                        Bitmap strippedPhotoBitmap = ImageLoader.getStrippedPhotoBitmap(((TLRPC$TL_photoStrippedSize) tLRPC$PhotoSize).bytes, "b");
                        onPostExecute(strippedPhotoBitmap != null ? new BitmapDrawable(strippedPhotoBitmap) : null);
                        return;
                    }
                    int i16 = cacheImage2.imageType;
                    if (i16 == 5) {
                        try {
                            CacheImage cacheImage3 = this.cacheImage;
                            drawable = new ThemePreviewDrawable(cacheImage3.finalFilePath, (DocumentObject.ThemeDocument) cacheImage3.imageLocation.document);
                        } catch (Throwable th2) {
                            FileLog.e(th2);
                            drawable = null;
                        }
                        onPostExecute(drawable);
                        return;
                    }
                    boolean z29 = true;
                    if (i16 == 3 || i16 == 4) {
                        Point point = AndroidUtilities.displaySize;
                        int i17 = point.x;
                        int i18 = point.y;
                        String str8 = cacheImage2.filter;
                        if (str8 != null) {
                            String[] split = str8.split("_");
                            if (split.length >= 2) {
                                float parseFloat = Float.parseFloat(split[0]);
                                float parseFloat2 = Float.parseFloat(split[1]);
                                float f7 = AndroidUtilities.density;
                                i17 = (int) (parseFloat * f7);
                                i18 = (int) (parseFloat2 * f7);
                                CacheImage cacheImage4 = this.cacheImage;
                                bitmap = SvgHelper.getBitmap(cacheImage4.finalFilePath, i17, i18, cacheImage4.imageType != 4);
                                if (bitmap != null && !TextUtils.isEmpty(this.cacheImage.filter) && this.cacheImage.filter.contains("wallpaper")) {
                                    obj = this.cacheImage.parentObject;
                                    if (obj instanceof TLRPC$WallPaper) {
                                        bitmap = applyWallpaperSetting(bitmap, (TLRPC$WallPaper) obj);
                                    }
                                }
                                onPostExecute(bitmap == null ? new BitmapDrawable(bitmap) : null);
                            }
                        }
                        CacheImage cacheImage42 = this.cacheImage;
                        bitmap = SvgHelper.getBitmap(cacheImage42.finalFilePath, i17, i18, cacheImage42.imageType != 4);
                        if (bitmap != null) {
                            obj = this.cacheImage.parentObject;
                            if (obj instanceof TLRPC$WallPaper) {
                            }
                        }
                        onPostExecute(bitmap == null ? new BitmapDrawable(bitmap) : null);
                    } else if (i16 == 1) {
                        int min2 = Math.min((int) LiteMode.FLAG_CALLS_ANIMATIONS, AndroidUtilities.dp(170.6f));
                        int min3 = Math.min((int) LiteMode.FLAG_CALLS_ANIMATIONS, AndroidUtilities.dp(170.6f));
                        String str9 = this.cacheImage.filter;
                        if (str9 != null) {
                            String[] split2 = str9.split("_");
                            if (split2.length >= 2) {
                                float parseFloat3 = Float.parseFloat(split2[0]);
                                float parseFloat4 = Float.parseFloat(split2[1]);
                                int min4 = Math.min((int) LiteMode.FLAG_CALLS_ANIMATIONS, (int) (AndroidUtilities.density * parseFloat3));
                                int min5 = Math.min((int) LiteMode.FLAG_CALLS_ANIMATIONS, (int) (AndroidUtilities.density * parseFloat4));
                                if (parseFloat3 > 90.0f || parseFloat4 > 90.0f || this.cacheImage.filter.contains("nolimit")) {
                                    min3 = min5;
                                    min2 = min4;
                                    z20 = false;
                                } else {
                                    min2 = Math.min(min4, (int) NotificationCenter.audioRouteChanged);
                                    min3 = Math.min(min5, (int) NotificationCenter.audioRouteChanged);
                                    z20 = true;
                                }
                                z24 = (split2.length >= 3 && "pcache".equals(split2[2])) || this.cacheImage.filter.contains("pcache") || !(this.cacheImage.filter.contains("nolimit") || SharedConfig.getDevicePerformanceClass() == 2);
                                z28 = this.cacheImage.filter.contains("lastframe");
                                z27 = this.cacheImage.filter.contains("lastreactframe");
                                if (z27) {
                                    z28 = true;
                                }
                                z23 = this.cacheImage.filter.contains("firstframe");
                            } else {
                                z20 = false;
                                z27 = false;
                                z23 = false;
                                z24 = false;
                                z28 = false;
                            }
                            if (split2.length >= 3) {
                                if ("nr".equals(split2[2])) {
                                    str7 = null;
                                } else if (!"nrs".equals(split2[2])) {
                                    if ("dice".equals(split2[2])) {
                                        str7 = split2[3];
                                    }
                                } else {
                                    str7 = null;
                                    i15 = 3;
                                    if (split2.length >= 5) {
                                        if ("c1".equals(split2[4])) {
                                            i12 = min3;
                                            z22 = z27;
                                            z21 = z28;
                                            i13 = min2;
                                            i14 = 12;
                                        } else if ("c2".equals(split2[4])) {
                                            i12 = min3;
                                            z22 = z27;
                                            z21 = z28;
                                            i13 = min2;
                                            i14 = 3;
                                        } else if ("c3".equals(split2[4])) {
                                            i12 = min3;
                                            z22 = z27;
                                            z21 = z28;
                                            i13 = min2;
                                            i14 = 4;
                                        } else if ("c4".equals(split2[4])) {
                                            i12 = min3;
                                            z22 = z27;
                                            z21 = z28;
                                            i13 = min2;
                                            i14 = 5;
                                        } else if ("c5".equals(split2[4])) {
                                            i12 = min3;
                                            z22 = z27;
                                            z21 = z28;
                                            i13 = min2;
                                            i14 = 6;
                                        }
                                    }
                                    i12 = min3;
                                    z22 = z27;
                                    z21 = z28;
                                    i13 = min2;
                                    i14 = 0;
                                }
                                i15 = 2;
                                if (split2.length >= 5) {
                                }
                                i12 = min3;
                                z22 = z27;
                                z21 = z28;
                                i13 = min2;
                                i14 = 0;
                            }
                            str7 = null;
                            i15 = 1;
                            if (split2.length >= 5) {
                            }
                            i12 = min3;
                            z22 = z27;
                            z21 = z28;
                            i13 = min2;
                            i14 = 0;
                        } else {
                            i12 = min3;
                            i13 = min2;
                            i14 = 0;
                            z20 = false;
                            z21 = false;
                            z22 = false;
                            str7 = null;
                            z23 = false;
                            z24 = false;
                            i15 = 1;
                        }
                        if (str7 != null) {
                            if ("🎰".equals(str7)) {
                                rLottieDrawable = new SlotsDrawable(str7, i13, i12);
                            } else {
                                rLottieDrawable = new RLottieDrawable(str7, i13, i12);
                            }
                        } else {
                            File file = this.cacheImage.finalFilePath;
                            try {
                                randomAccessFile2 = new RandomAccessFile(this.cacheImage.finalFilePath, "r");
                                try {
                                    bArr2 = this.cacheImage.type == 1 ? ImageLoader.headerThumb : ImageLoader.header;
                                    randomAccessFile2.readFully(bArr2, 0, 2);
                                } catch (Exception e) {
                                    e = e;
                                    z25 = false;
                                    try {
                                        FileLog.e(e, z25);
                                        if (randomAccessFile2 != null) {
                                            try {
                                                randomAccessFile2.close();
                                            } catch (Exception e2) {
                                                e = e2;
                                                z26 = false;
                                                FileLog.e(e);
                                                if ((!z21 || z23) ? false : z24) {
                                                }
                                                cacheOptions2 = new BitmapsCache.CacheOptions();
                                                if (z21) {
                                                }
                                                cacheOptions2.firstFrame = true;
                                                cacheOptions3 = cacheOptions2;
                                                if (z26) {
                                                }
                                                rLottieDrawable2 = rLottieDrawable;
                                                if (!z21) {
                                                }
                                                loadLastFrame(rLottieDrawable2, i12, i13, z21, z22);
                                                return;
                                            }
                                        }
                                        z26 = false;
                                        if ((!z21 || z23) ? false : z24) {
                                        }
                                        cacheOptions2 = new BitmapsCache.CacheOptions();
                                        if (z21) {
                                        }
                                        cacheOptions2.firstFrame = true;
                                        cacheOptions3 = cacheOptions2;
                                        if (z26) {
                                        }
                                        rLottieDrawable2 = rLottieDrawable;
                                        if (!z21) {
                                        }
                                        loadLastFrame(rLottieDrawable2, i12, i13, z21, z22);
                                        return;
                                    } catch (Throwable th3) {
                                        th = th3;
                                        randomAccessFile = randomAccessFile2;
                                        th = th;
                                        randomAccessFile2 = randomAccessFile;
                                        if (randomAccessFile2 != null) {
                                            try {
                                                randomAccessFile2.close();
                                            } catch (Exception e3) {
                                                FileLog.e(e3);
                                            }
                                        }
                                        throw th;
                                    }
                                } catch (Throwable th4) {
                                    th = th4;
                                    if (randomAccessFile2 != null) {
                                    }
                                    throw th;
                                }
                            } catch (Exception e4) {
                                e = e4;
                                z25 = false;
                                randomAccessFile2 = null;
                            } catch (Throwable th5) {
                                th = th5;
                                randomAccessFile = null;
                                th = th;
                                randomAccessFile2 = randomAccessFile;
                                if (randomAccessFile2 != null) {
                                }
                                throw th;
                            }
                            if (bArr2[0] == 31) {
                                if (bArr2[1] == -117) {
                                    z26 = true;
                                    randomAccessFile2.close();
                                    if (!((!z21 || z23) ? false : z24) || z21 || z23) {
                                        cacheOptions2 = new BitmapsCache.CacheOptions();
                                        if (z21 && !z23) {
                                            String str10 = this.cacheImage.filter;
                                            if (str10 != null && str10.contains("compress")) {
                                                cacheOptions2.compressQuality = 60;
                                            }
                                            String str11 = this.cacheImage.filter;
                                            if (str11 != null && str11.contains("flbk")) {
                                                cacheOptions2.fallback = true;
                                            }
                                        } else {
                                            cacheOptions2.firstFrame = true;
                                        }
                                        cacheOptions3 = cacheOptions2;
                                    } else {
                                        cacheOptions3 = null;
                                    }
                                    if (z26) {
                                        File file2 = this.cacheImage.finalFilePath;
                                        rLottieDrawable = new RLottieDrawable(file2, ImageLoader.decompressGzip(file2), i13, i12, cacheOptions3, z20, null, i14);
                                    } else {
                                        rLottieDrawable = new RLottieDrawable(this.cacheImage.finalFilePath, i13, i12, cacheOptions3, z20, null, i14);
                                    }
                                }
                            }
                            z26 = false;
                            randomAccessFile2.close();
                            if ((!z21 || z23) ? false : z24) {
                            }
                            cacheOptions2 = new BitmapsCache.CacheOptions();
                            if (z21) {
                            }
                            cacheOptions2.firstFrame = true;
                            cacheOptions3 = cacheOptions2;
                            if (z26) {
                            }
                        }
                        rLottieDrawable2 = rLottieDrawable;
                        if (!z21 || z23) {
                            loadLastFrame(rLottieDrawable2, i12, i13, z21, z22);
                            return;
                        }
                        rLottieDrawable2.setAutoRepeat(i15);
                        onPostExecute(rLottieDrawable2);
                    } else if (i16 == 2) {
                        long j = imageLocation.videoSeekTo;
                        String str12 = cacheImage2.filter;
                        if (str12 != null) {
                            String[] split3 = str12.split("_");
                            if (split3.length >= 2) {
                                float parseFloat5 = Float.parseFloat(split3[0]);
                                float parseFloat6 = Float.parseFloat(split3[1]);
                                if (parseFloat5 <= 90.0f && parseFloat6 <= 90.0f && !this.cacheImage.filter.contains("nolimit")) {
                                    z15 = true;
                                    z16 = false;
                                    z17 = false;
                                    z18 = false;
                                    z19 = false;
                                    for (i11 = 0; i11 < split3.length; i11++) {
                                        if ("pcache".equals(split3[i11])) {
                                            z17 = true;
                                        }
                                        if ("firstframe".equals(split3[i11])) {
                                            z16 = true;
                                        }
                                        if ("nostream".equals(split3[i11])) {
                                            z19 = true;
                                        }
                                        if ("pframe".equals(split3[i11])) {
                                            z18 = true;
                                        }
                                    }
                                    if (z16) {
                                        z19 = true;
                                    }
                                }
                            }
                            z15 = false;
                            z16 = false;
                            z17 = false;
                            z18 = false;
                            z19 = false;
                            while (i11 < split3.length) {
                            }
                            if (z16) {
                            }
                        } else {
                            z15 = false;
                            z16 = false;
                            z17 = false;
                            z18 = false;
                            z19 = false;
                        }
                        if (!z18) {
                            if (!z17 || z16) {
                                cacheOptions = null;
                            } else {
                                BitmapsCache.CacheOptions cacheOptions4 = new BitmapsCache.CacheOptions();
                                String str13 = this.cacheImage.filter;
                                if (str13 != null && str13.contains("compress")) {
                                    cacheOptions4.compressQuality = 60;
                                }
                                cacheOptions = cacheOptions4;
                            }
                            if (ImageLoader.this.isAnimatedAvatar(this.cacheImage.filter) || ImageLoader.AUTOPLAY_FILTER.equals(this.cacheImage.filter)) {
                                CacheImage cacheImage5 = this.cacheImage;
                                ImageLocation imageLocation2 = cacheImage5.imageLocation;
                                TLRPC$Document tLRPC$Document = imageLocation2.document;
                                if (!(tLRPC$Document instanceof TLRPC$TL_documentEncrypted) && !z17) {
                                    if (!(tLRPC$Document instanceof TLRPC$Document)) {
                                        tLRPC$Document = null;
                                    }
                                    long j2 = tLRPC$Document != null ? cacheImage5.size : imageLocation2.currentSize;
                                    int i19 = tLRPC$Document != null ? 1 : 0;
                                    int i20 = cacheImage5.cacheType;
                                    int i21 = i20 > 1 ? i20 : i19;
                                    CacheImage cacheImage6 = this.cacheImage;
                                    animatedFileDrawable = new AnimatedFileDrawable(cacheImage6.finalFilePath, z16, z19 ? 0L : j2, cacheImage6.priority, z19 ? null : tLRPC$Document, (tLRPC$Document != null || z19) ? null : cacheImage6.imageLocation, cacheImage6.parentObject, j, cacheImage6.currentAccount, false, 0, 0, cacheOptions, i21);
                                    if (!MessageObject.isWebM(tLRPC$Document) && !MessageObject.isVideoSticker(tLRPC$Document) && !ImageLoader.this.isAnimatedAvatar(this.cacheImage.filter)) {
                                        z29 = false;
                                    }
                                    animatedFileDrawable.setIsWebmSticker(z29);
                                    if (z16) {
                                        Bitmap frameAtTime = animatedFileDrawable.getFrameAtTime(0L, false);
                                        animatedFileDrawable.recycle();
                                        Thread.interrupted();
                                        if (frameAtTime == null) {
                                            onPostExecute(null);
                                            return;
                                        } else {
                                            onPostExecute(new BitmapDrawable(frameAtTime));
                                            return;
                                        }
                                    }
                                    animatedFileDrawable.setLimitFps(z15);
                                    Thread.interrupted();
                                    onPostExecute(animatedFileDrawable);
                                    return;
                                }
                            }
                            String str14 = this.cacheImage.filter;
                            if (str14 != null) {
                                String[] split4 = str14.split("_");
                                if (split4.length >= 2) {
                                    float parseFloat7 = Float.parseFloat(split4[0]);
                                    float parseFloat8 = Float.parseFloat(split4[1]);
                                    float f8 = AndroidUtilities.density;
                                    i10 = (int) (parseFloat8 * f8);
                                    i9 = (int) (parseFloat7 * f8);
                                    boolean z30 = !z16 || ((str6 = this.cacheImage.filter) != null && ("d".equals(str6) || this.cacheImage.filter.contains("_d")));
                                    int i22 = (!z19 ? null : this.cacheImage.imageLocation.document) == null ? 1 : 0;
                                    int i23 = this.cacheImage.cacheType;
                                    int i24 = i23 <= 1 ? i23 : i22;
                                    CacheImage cacheImage7 = this.cacheImage;
                                    animatedFileDrawable = new AnimatedFileDrawable(cacheImage7.finalFilePath, z30, 0L, cacheImage7.priority, !z19 ? null : cacheImage7.imageLocation.document, null, null, j, cacheImage7.currentAccount, false, i9, i10, cacheOptions, i24);
                                    if (!MessageObject.isWebM(this.cacheImage.imageLocation.document) && !MessageObject.isVideoSticker(this.cacheImage.imageLocation.document) && !ImageLoader.this.isAnimatedAvatar(this.cacheImage.filter)) {
                                        z29 = false;
                                    }
                                    animatedFileDrawable.setIsWebmSticker(z29);
                                    if (z16) {
                                    }
                                }
                            }
                            i9 = 0;
                            i10 = 0;
                            if (z16) {
                            }
                            if ((!z19 ? null : this.cacheImage.imageLocation.document) == null) {
                            }
                            int i232 = this.cacheImage.cacheType;
                            if (i232 <= 1) {
                            }
                            CacheImage cacheImage72 = this.cacheImage;
                            animatedFileDrawable = new AnimatedFileDrawable(cacheImage72.finalFilePath, z30, 0L, cacheImage72.priority, !z19 ? null : cacheImage72.imageLocation.document, null, null, j, cacheImage72.currentAccount, false, i9, i10, cacheOptions, i24);
                            if (!MessageObject.isWebM(this.cacheImage.imageLocation.document)) {
                                z29 = false;
                            }
                            animatedFileDrawable.setIsWebmSticker(z29);
                            if (z16) {
                            }
                        } else {
                            try {
                                mediaMetadataRetriever = new MediaMetadataRetriever();
                                mediaMetadataRetriever.setDataSource(this.cacheImage.finalFilePath.getAbsolutePath());
                                bitmap11 = mediaMetadataRetriever.getFrameAtTime(2L);
                            } catch (Exception e5) {
                                e = e5;
                                bitmap11 = null;
                            }
                            try {
                                mediaMetadataRetriever.release();
                            } catch (Exception e6) {
                                e = e6;
                                e.printStackTrace();
                                Thread.interrupted();
                                if (bitmap11 != null) {
                                }
                            }
                            Thread.interrupted();
                            if (bitmap11 != null) {
                                onPostExecute(null);
                            } else {
                                onPostExecute(new BitmapDrawable(bitmap11));
                            }
                        }
                    } else {
                        File file3 = cacheImage2.finalFilePath;
                        boolean z31 = (cacheImage2.secureDocument == null && (cacheImage2.encryptionKeyPath == null || file3 == null || !file3.getAbsolutePath().endsWith(".enc"))) ? false : true;
                        CacheImage cacheImage8 = this.cacheImage;
                        SecureDocument secureDocument = cacheImage8.secureDocument;
                        if (secureDocument != null) {
                            SecureDocumentKey secureDocumentKey = secureDocument.secureDocumentKey;
                            TLRPC$TL_secureFile tLRPC$TL_secureFile = secureDocument.secureFile;
                            if (tLRPC$TL_secureFile == null || (bArr = tLRPC$TL_secureFile.file_hash) == null) {
                                bArr = secureDocument.fileHash;
                            }
                            bitmap2 = secureDocumentKey;
                        } else {
                            bitmap2 = null;
                            bArr = null;
                        }
                        String str15 = cacheImage8.imageLocation.path;
                        if (str15 != null) {
                            if (str15.startsWith("thumb://")) {
                                int indexOf = str15.indexOf(":", 8);
                                if (indexOf >= 0) {
                                    l = Long.valueOf(Long.parseLong(str15.substring(8, indexOf)));
                                    str5 = str15.substring(indexOf + 1);
                                } else {
                                    str5 = null;
                                    l = null;
                                }
                                str = str5;
                                z = false;
                            } else {
                                if (str15.startsWith("vthumb://")) {
                                    int indexOf2 = str15.indexOf(":", 9);
                                    if (indexOf2 >= 0) {
                                        l3 = Long.valueOf(Long.parseLong(str15.substring(9, indexOf2)));
                                        z14 = true;
                                        l = l3;
                                        z = z14;
                                        str = null;
                                    }
                                }
                                l3 = null;
                                z14 = false;
                                l = l3;
                                z = z14;
                                str = null;
                            }
                            z2 = false;
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 1;
                            if (Build.VERSION.SDK_INT < 21) {
                                options.inPurgeable = true;
                            }
                            boolean z32 = ImageLoader.this.canForce8888;
                            str4 = this.cacheImage.filter;
                            if (str4 == null) {
                                String[] split5 = str4.split("_");
                                if (split5.length >= 2) {
                                    f = Float.parseFloat(split5[0]) * AndroidUtilities.density;
                                    try {
                                        f6 = f;
                                        f4 = Float.parseFloat(split5[1]) * AndroidUtilities.density;
                                    } catch (Throwable th6) {
                                        th = th6;
                                        str2 = str;
                                        l2 = l;
                                        bitmap3 = null;
                                        f2 = 0.0f;
                                        c = 0;
                                        z3 = false;
                                        i = 1;
                                        FileLog.e(th, !(th instanceof FileNotFoundException));
                                        f3 = f;
                                        f4 = f2;
                                        z4 = z3;
                                        if (this.cacheImage.type == i) {
                                        }
                                    }
                                } else {
                                    f4 = 0.0f;
                                    f6 = 0.0f;
                                }
                                try {
                                    if (this.cacheImage.filter.contains("b2r")) {
                                        c = 4;
                                    } else if (this.cacheImage.filter.contains("b2")) {
                                        c = 3;
                                    } else if (this.cacheImage.filter.contains("b1")) {
                                        c = 2;
                                    } else {
                                        c = this.cacheImage.filter.contains("b") ? (char) 1 : (char) 0;
                                    }
                                    try {
                                        boolean contains = this.cacheImage.filter.contains("i");
                                        try {
                                            if (this.cacheImage.filter.contains("f")) {
                                                z32 = true;
                                            } else if (this.cacheImage.filter.contains("F")) {
                                                z32 = false;
                                            }
                                            if (f6 == 0.0f || f4 == 0.0f) {
                                                z11 = z32;
                                                str2 = str;
                                                z12 = contains;
                                                l2 = l;
                                            } else {
                                                try {
                                                    options.inJustDecodeBounds = true;
                                                } catch (Throwable th7) {
                                                    th = th7;
                                                    str2 = str;
                                                    z3 = contains;
                                                    l2 = l;
                                                    f2 = f4;
                                                    f = f6;
                                                    bitmap3 = null;
                                                    i = 1;
                                                    FileLog.e(th, !(th instanceof FileNotFoundException));
                                                    f3 = f;
                                                    f4 = f2;
                                                    z4 = z3;
                                                    if (this.cacheImage.type == i) {
                                                    }
                                                }
                                                try {
                                                    try {
                                                        if (l == null || str != null) {
                                                            z11 = z32;
                                                            str2 = str;
                                                            if (bitmap2 != null) {
                                                                try {
                                                                    RandomAccessFile randomAccessFile3 = new RandomAccessFile(file3, "r");
                                                                    int length = (int) randomAccessFile3.length();
                                                                    byte[] bArr3 = (byte[]) ImageLoader.bytesLocal.get();
                                                                    if (bArr3 == null || bArr3.length < length) {
                                                                        bArr3 = null;
                                                                    }
                                                                    if (bArr3 == null) {
                                                                        bArr3 = new byte[length];
                                                                        ImageLoader.bytesLocal.set(bArr3);
                                                                    }
                                                                    randomAccessFile3.readFully(bArr3, 0, length);
                                                                    randomAccessFile3.close();
                                                                    EncryptedFileInputStream.decryptBytesWithKeyFile(bArr3, 0, length, bitmap2);
                                                                    z12 = contains;
                                                                    l2 = l;
                                                                    byte[] computeSHA256 = Utilities.computeSHA256(bArr3, 0, length);
                                                                    if (bArr != null && Arrays.equals(computeSHA256, bArr)) {
                                                                        z13 = false;
                                                                        int i25 = bArr3[0] & 255;
                                                                        int i26 = length - i25;
                                                                        if (!z13) {
                                                                            BitmapFactory.decodeByteArray(bArr3, i25, i26, options);
                                                                        }
                                                                    }
                                                                    z13 = true;
                                                                    int i252 = bArr3[0] & 255;
                                                                    int i262 = length - i252;
                                                                    if (!z13) {
                                                                    }
                                                                } catch (Throwable th8) {
                                                                    th = th8;
                                                                    z3 = contains;
                                                                    l2 = l;
                                                                    f2 = f4;
                                                                    f = f6;
                                                                    z32 = z11;
                                                                    bitmap3 = null;
                                                                    i = 1;
                                                                    FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                    f3 = f;
                                                                    f4 = f2;
                                                                    z4 = z3;
                                                                    if (this.cacheImage.type == i) {
                                                                    }
                                                                }
                                                            } else {
                                                                z12 = contains;
                                                                l2 = l;
                                                                if (z31) {
                                                                    fileInputStream3 = new EncryptedFileInputStream(file3, this.cacheImage.encryptionKeyPath);
                                                                } else {
                                                                    fileInputStream3 = new FileInputStream(file3);
                                                                }
                                                                BitmapFactory.decodeStream(fileInputStream3, null, options);
                                                                fileInputStream3.close();
                                                            }
                                                        } else {
                                                            if (z) {
                                                                try {
                                                                    z11 = z32;
                                                                    str2 = str;
                                                                    MediaStore.Video.Thumbnails.getThumbnail(ApplicationLoader.applicationContext.getContentResolver(), l.longValue(), 1, options);
                                                                } catch (Throwable th9) {
                                                                    th = th9;
                                                                    str2 = str;
                                                                    f2 = f4;
                                                                    z3 = contains;
                                                                    l2 = l;
                                                                    f = f6;
                                                                    bitmap3 = null;
                                                                    i = 1;
                                                                    FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                    f3 = f;
                                                                    f4 = f2;
                                                                    z4 = z3;
                                                                    if (this.cacheImage.type == i) {
                                                                    }
                                                                }
                                                            } else {
                                                                z11 = z32;
                                                                str2 = str;
                                                                MediaStore.Images.Thumbnails.getThumbnail(ApplicationLoader.applicationContext.getContentResolver(), l.longValue(), 1, options);
                                                            }
                                                            z12 = contains;
                                                            l2 = l;
                                                        }
                                                        float f9 = options.outWidth;
                                                        float f10 = options.outHeight;
                                                        if (f6 >= f4 && f9 > f10) {
                                                            min = Math.max(f9 / f6, f10 / f4);
                                                        } else {
                                                            min = Math.min(f9 / f6, f10 / f4);
                                                        }
                                                        if (min < 1.2f) {
                                                            min = 1.0f;
                                                        }
                                                        options.inJustDecodeBounds = false;
                                                        if (min > 1.0f && (f9 > f6 || f10 > f4)) {
                                                            int i27 = 1;
                                                            while (true) {
                                                                i8 = i27 * 2;
                                                                if (i27 * 4 >= min) {
                                                                    break;
                                                                }
                                                                i27 = i8;
                                                            }
                                                            options.inSampleSize = i8;
                                                        } else {
                                                            options.inSampleSize = (int) min;
                                                        }
                                                    } catch (Throwable th10) {
                                                        th = th10;
                                                    }
                                                } catch (Throwable th11) {
                                                    th = th11;
                                                    f2 = f4;
                                                    z3 = contains;
                                                    l2 = l;
                                                    f = f6;
                                                    z32 = z11;
                                                    bitmap3 = null;
                                                    i = 1;
                                                    FileLog.e(th, !(th instanceof FileNotFoundException));
                                                    f3 = f;
                                                    f4 = f2;
                                                    z4 = z3;
                                                    if (this.cacheImage.type == i) {
                                                    }
                                                }
                                            }
                                            f3 = f6;
                                            z32 = z11;
                                            z4 = z12;
                                            bitmap3 = null;
                                        } catch (Throwable th12) {
                                            th = th12;
                                        }
                                    } catch (Throwable th13) {
                                        th = th13;
                                        str2 = str;
                                        l2 = l;
                                        f2 = f4;
                                        f = f6;
                                        bitmap3 = null;
                                        z3 = false;
                                        i = 1;
                                        FileLog.e(th, !(th instanceof FileNotFoundException));
                                        f3 = f;
                                        f4 = f2;
                                        z4 = z3;
                                        if (this.cacheImage.type == i) {
                                        }
                                    }
                                } catch (Throwable th14) {
                                    th = th14;
                                    str2 = str;
                                    l2 = l;
                                    f2 = f4;
                                    f = f6;
                                    bitmap3 = null;
                                    c = 0;
                                    z3 = false;
                                    i = 1;
                                    FileLog.e(th, !(th instanceof FileNotFoundException));
                                    f3 = f;
                                    f4 = f2;
                                    z4 = z3;
                                    if (this.cacheImage.type == i) {
                                    }
                                }
                            } else {
                                str2 = str;
                                l2 = l;
                                if (str2 != null) {
                                    try {
                                        options.inJustDecodeBounds = true;
                                        options.inPreferredConfig = z32 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
                                        FileInputStream fileInputStream4 = new FileInputStream(file3);
                                        bitmap3 = BitmapFactory.decodeStream(fileInputStream4, null, options);
                                        try {
                                            fileInputStream4.close();
                                            int i28 = options.outWidth;
                                            int i29 = options.outHeight;
                                            options.inJustDecodeBounds = false;
                                            float min6 = (Math.min(i29, i28) / Math.max(66, Math.min(AndroidUtilities.getRealScreenSize().x, AndroidUtilities.getRealScreenSize().y))) * 6.0f;
                                            if (min6 < 1.0f) {
                                                min6 = 1.0f;
                                            }
                                            if (min6 > 1.0f) {
                                                int i30 = 1;
                                                while (true) {
                                                    i7 = i30 * 2;
                                                    if (i30 * 4 > min6) {
                                                        break;
                                                    }
                                                    i30 = i7;
                                                }
                                                options.inSampleSize = i7;
                                            } else {
                                                options.inSampleSize = (int) min6;
                                            }
                                            f4 = 0.0f;
                                        } catch (Throwable th15) {
                                            th = th15;
                                            f = 0.0f;
                                            f2 = 0.0f;
                                            c = 0;
                                            z3 = false;
                                            i = 1;
                                            FileLog.e(th, !(th instanceof FileNotFoundException));
                                            f3 = f;
                                            f4 = f2;
                                            z4 = z3;
                                            if (this.cacheImage.type == i) {
                                            }
                                        }
                                    } catch (Throwable th16) {
                                        th = th16;
                                        f = 0.0f;
                                        bitmap3 = null;
                                        f2 = 0.0f;
                                        c = 0;
                                        z3 = false;
                                        i = 1;
                                        FileLog.e(th, !(th instanceof FileNotFoundException));
                                        f3 = f;
                                        f4 = f2;
                                        z4 = z3;
                                        if (this.cacheImage.type == i) {
                                        }
                                    }
                                } else {
                                    f4 = 0.0f;
                                    bitmap3 = null;
                                }
                                z4 = false;
                                c = 0;
                                f3 = 0.0f;
                            }
                            i = 1;
                            if (this.cacheImage.type == i) {
                                try {
                                    ImageLoader.this.lastCacheOutTime = SystemClock.elapsedRealtime();
                                } catch (Throwable th17) {
                                    th = th17;
                                }
                                synchronized (this.sync) {
                                    try {
                                    } catch (Throwable th18) {
                                        th = th18;
                                        bitmap3 = bitmap7;
                                    }
                                    if (this.isCancelled) {
                                        return;
                                    }
                                    try {
                                        if (options.inPurgeable || bitmap2 != null) {
                                            RandomAccessFile randomAccessFile4 = new RandomAccessFile(file3, "r");
                                            int length2 = (int) randomAccessFile4.length();
                                            byte[] bArr4 = (byte[]) ImageLoader.bytesThumbLocal.get();
                                            if (bArr4 == null || bArr4.length < length2) {
                                                bArr4 = null;
                                            }
                                            if (bArr4 == null) {
                                                bArr4 = new byte[length2];
                                                ImageLoader.bytesThumbLocal.set(bArr4);
                                            }
                                            randomAccessFile4.readFully(bArr4, 0, length2);
                                            randomAccessFile4.close();
                                            if (bitmap2 != null) {
                                                EncryptedFileInputStream.decryptBytesWithKeyFile(bArr4, 0, length2, bitmap2);
                                                bitmap8 = bitmap3;
                                                z6 = z4;
                                                byte[] computeSHA2562 = Utilities.computeSHA256(bArr4, 0, length2);
                                                if (bArr != null && Arrays.equals(computeSHA2562, bArr)) {
                                                    z7 = false;
                                                    i4 = bArr4[0] & 255;
                                                    length2 -= i4;
                                                }
                                                z7 = true;
                                                i4 = bArr4[0] & 255;
                                                length2 -= i4;
                                            } else {
                                                bitmap8 = bitmap3;
                                                z6 = z4;
                                                if (z31) {
                                                    EncryptedFileInputStream.decryptBytesWithKeyFile(bArr4, 0, length2, this.cacheImage.encryptionKeyPath);
                                                }
                                                z7 = false;
                                                i4 = 0;
                                            }
                                            bitmap9 = !z7 ? BitmapFactory.decodeByteArray(bArr4, i4, length2, options) : bitmap8;
                                        } else {
                                            if (z31) {
                                                fileInputStream = new EncryptedFileInputStream(file3, this.cacheImage.encryptionKeyPath);
                                            } else {
                                                fileInputStream = new FileInputStream(file3);
                                            }
                                            Bitmap decodeStream = BitmapFactory.decodeStream(fileInputStream, null, options);
                                            fileInputStream.close();
                                            z6 = z4;
                                            bitmap9 = decodeStream;
                                        }
                                    } catch (Throwable th19) {
                                        th = th19;
                                        z5 = false;
                                        bitmap4 = bitmap3;
                                        FileLog.e(th, !(th instanceof FileNotFoundException));
                                        bitmap5 = bitmap4;
                                        i2 = 0;
                                        bitmap6 = bitmap5;
                                        i3 = 0;
                                        bitmap10 = bitmap6;
                                        Thread.interrupted();
                                        if (BuildVars.LOGS_ENABLED) {
                                        }
                                        if (bitmap10 != null) {
                                        }
                                        cacheImage = this.cacheImage;
                                        if (cacheImage != null) {
                                        }
                                        onPostExecute(bitmap10 != null ? new ExtendedBitmapDrawable(bitmap10, i3, i2) : null);
                                        return;
                                    }
                                    if (bitmap9 == null) {
                                        if (file3.length() != 0 && this.cacheImage.filter != null) {
                                            i2 = 0;
                                            z5 = false;
                                            bitmap6 = bitmap9;
                                            i3 = 0;
                                            bitmap10 = bitmap6;
                                            Thread.interrupted();
                                            if (BuildVars.LOGS_ENABLED && z31) {
                                                StringBuilder sb = new StringBuilder();
                                                sb.append("Image Loader image is empty = ");
                                                sb.append(bitmap10 != null);
                                                sb.append(" ");
                                                sb.append(file3);
                                                FileLog.e(sb.toString());
                                            }
                                            if (bitmap10 != null && !TextUtils.isEmpty(this.cacheImage.filter) && this.cacheImage.filter.contains("wallpaper")) {
                                                obj2 = this.cacheImage.parentObject;
                                                if (obj2 instanceof TLRPC$WallPaper) {
                                                    bitmap10 = applyWallpaperSetting(bitmap10, (TLRPC$WallPaper) obj2);
                                                }
                                            }
                                            cacheImage = this.cacheImage;
                                            if ((cacheImage != null || (str3 = cacheImage.filter) == null || !str3.contains("ignoreOrientation")) && (z5 || i3 != 0 || i2 != 0)) {
                                                onPostExecute(bitmap10 != null ? new ExtendedBitmapDrawable(bitmap10, i3, i2) : null);
                                                return;
                                            } else {
                                                onPostExecute(bitmap10 != null ? new BitmapDrawable(bitmap10) : null);
                                                return;
                                            }
                                        }
                                        file3.delete();
                                        z5 = false;
                                    } else {
                                        if (this.cacheImage.filter != null) {
                                            float width = bitmap9.getWidth();
                                            float height = bitmap9.getHeight();
                                            if (!options.inPurgeable && f3 != 0.0f && width != f3 && width > 20.0f + f3 && bitmap9 != (createScaledBitmap = Bitmaps.createScaledBitmap(bitmap9, (int) f3, (int) (height / (width / f3)), true))) {
                                                bitmap9.recycle();
                                                bitmap9 = createScaledBitmap;
                                            }
                                        }
                                        z5 = z6 && Utilities.needInvert(bitmap9, !options.inPurgeable ? 1 : 0, bitmap9.getWidth(), bitmap9.getHeight(), bitmap9.getRowBytes()) != 0;
                                        try {
                                            if (c == 1) {
                                                Bitmap.Config config = bitmap9.getConfig();
                                                bitmap5 = bitmap9;
                                                if (config == Bitmap.Config.ARGB_8888) {
                                                    Utilities.blurBitmap(bitmap9, 3, !options.inPurgeable ? 1 : 0, bitmap9.getWidth(), bitmap9.getHeight(), bitmap9.getRowBytes());
                                                    bitmap5 = bitmap9;
                                                }
                                            } else if (c == 2) {
                                                Bitmap.Config config2 = bitmap9.getConfig();
                                                bitmap5 = bitmap9;
                                                if (config2 == Bitmap.Config.ARGB_8888) {
                                                    Utilities.blurBitmap(bitmap9, 1, !options.inPurgeable ? 1 : 0, bitmap9.getWidth(), bitmap9.getHeight(), bitmap9.getRowBytes());
                                                    bitmap5 = bitmap9;
                                                }
                                            } else {
                                                if (c != 3) {
                                                    bitmap5 = bitmap9;
                                                    if (c != 4) {
                                                        if (c == 0) {
                                                            bitmap5 = bitmap9;
                                                            if (options.inPurgeable) {
                                                                Utilities.pinBitmap(bitmap9);
                                                            }
                                                        }
                                                    }
                                                }
                                                Bitmap.Config config3 = bitmap9.getConfig();
                                                bitmap5 = bitmap9;
                                                if (config3 == Bitmap.Config.ARGB_8888) {
                                                    if (c == 4) {
                                                        Bitmap createBitmap = Bitmap.createBitmap(bitmap9.getWidth(), bitmap9.getHeight(), bitmap9.getConfig());
                                                        Canvas canvas = new Canvas(createBitmap);
                                                        canvas.save();
                                                        canvas.scale(1.2f, 1.2f, bitmap9.getWidth() / 2.0f, bitmap9.getHeight() / 2.0f);
                                                        canvas.drawBitmap(bitmap9, 0.0f, 0.0f, (Paint) null);
                                                        canvas.restore();
                                                        Path path = new Path();
                                                        path.addCircle(bitmap9.getWidth() / 2.0f, bitmap9.getHeight() / 2.0f, Math.min(bitmap9.getWidth(), bitmap9.getHeight()) / 2.0f, Path.Direction.CW);
                                                        canvas.clipPath(path);
                                                        canvas.drawBitmap(bitmap9, 0.0f, 0.0f, (Paint) null);
                                                        bitmap9.recycle();
                                                        bitmap9 = createBitmap;
                                                    }
                                                    Utilities.blurBitmap(bitmap9, 7, !options.inPurgeable ? 1 : 0, bitmap9.getWidth(), bitmap9.getHeight(), bitmap9.getRowBytes());
                                                    Utilities.blurBitmap(bitmap9, 7, !options.inPurgeable ? 1 : 0, bitmap9.getWidth(), bitmap9.getHeight(), bitmap9.getRowBytes());
                                                    Utilities.blurBitmap(bitmap9, 7, !options.inPurgeable ? 1 : 0, bitmap9.getWidth(), bitmap9.getHeight(), bitmap9.getRowBytes());
                                                    bitmap5 = bitmap9;
                                                }
                                            }
                                        } catch (Throwable th20) {
                                            th = th20;
                                            bitmap4 = bitmap9;
                                            FileLog.e(th, !(th instanceof FileNotFoundException));
                                            bitmap5 = bitmap4;
                                            i2 = 0;
                                            bitmap6 = bitmap5;
                                            i3 = 0;
                                            bitmap10 = bitmap6;
                                            Thread.interrupted();
                                            if (BuildVars.LOGS_ENABLED) {
                                            }
                                            if (bitmap10 != null) {
                                            }
                                            cacheImage = this.cacheImage;
                                            if (cacheImage != null) {
                                            }
                                            onPostExecute(bitmap10 != null ? new ExtendedBitmapDrawable(bitmap10, i3, i2) : null);
                                            return;
                                        }
                                        i2 = 0;
                                        bitmap6 = bitmap5;
                                        i3 = 0;
                                        bitmap10 = bitmap6;
                                        Thread.interrupted();
                                        if (BuildVars.LOGS_ENABLED) {
                                            StringBuilder sb2 = new StringBuilder();
                                            sb2.append("Image Loader image is empty = ");
                                            sb2.append(bitmap10 != null);
                                            sb2.append(" ");
                                            sb2.append(file3);
                                            FileLog.e(sb2.toString());
                                        }
                                        if (bitmap10 != null) {
                                            obj2 = this.cacheImage.parentObject;
                                            if (obj2 instanceof TLRPC$WallPaper) {
                                            }
                                        }
                                        cacheImage = this.cacheImage;
                                        if (cacheImage != null) {
                                        }
                                        onPostExecute(bitmap10 != null ? new ExtendedBitmapDrawable(bitmap10, i3, i2) : null);
                                        return;
                                    }
                                    i3 = 0;
                                    i5 = 0;
                                    i2 = i5;
                                    bitmap10 = bitmap9;
                                    Thread.interrupted();
                                    if (BuildVars.LOGS_ENABLED) {
                                    }
                                    if (bitmap10 != null) {
                                    }
                                    cacheImage = this.cacheImage;
                                    if (cacheImage != null) {
                                    }
                                    onPostExecute(bitmap10 != null ? new ExtendedBitmapDrawable(bitmap10, i3, i2) : null);
                                    return;
                                }
                            }
                            Bitmap bitmap12 = bitmap3;
                            boolean z33 = z4;
                            int i31 = l2 != null ? 0 : 20;
                            if (i31 != 0) {
                                try {
                                } catch (Throwable th21) {
                                    th = th21;
                                    bitmap9 = bitmap12;
                                    z8 = false;
                                    i3 = 0;
                                    i5 = 0;
                                    FileLog.e(th, !(th instanceof FileNotFoundException));
                                    z5 = z8;
                                    i2 = i5;
                                    bitmap10 = bitmap9;
                                    Thread.interrupted();
                                    if (BuildVars.LOGS_ENABLED) {
                                    }
                                    if (bitmap10 != null) {
                                    }
                                    cacheImage = this.cacheImage;
                                    if (cacheImage != null) {
                                    }
                                    onPostExecute(bitmap10 != null ? new ExtendedBitmapDrawable(bitmap10, i3, i2) : null);
                                    return;
                                }
                                if (ImageLoader.this.lastCacheOutTime != 0) {
                                    f5 = f3;
                                    long j3 = i31;
                                    if (ImageLoader.this.lastCacheOutTime > SystemClock.elapsedRealtime() - j3 && Build.VERSION.SDK_INT < 21) {
                                        Thread.sleep(j3);
                                    }
                                    ImageLoader.this.lastCacheOutTime = SystemClock.elapsedRealtime();
                                    synchronized (this.sync) {
                                        if (this.isCancelled) {
                                            return;
                                        }
                                        if (!z32) {
                                            CacheImage cacheImage9 = this.cacheImage;
                                            if (cacheImage9.filter != null && c == 0 && cacheImage9.imageLocation.path == null) {
                                                options.inPreferredConfig = Bitmap.Config.RGB_565;
                                                options.inDither = false;
                                                if (l2 == null && str2 == null) {
                                                    if (z) {
                                                        if (l2.longValue() == 0) {
                                                            AnimatedFileDrawable animatedFileDrawable2 = new AnimatedFileDrawable(file3, true, 0L, 0, null, null, null, 0L, 0, true, null);
                                                            Bitmap frameAtTime2 = animatedFileDrawable2.getFrameAtTime(0L, true);
                                                            try {
                                                                animatedFileDrawable2.recycle();
                                                                bitmap9 = frameAtTime2;
                                                            } catch (Throwable th22) {
                                                                th = th22;
                                                                bitmap9 = frameAtTime2;
                                                                z8 = false;
                                                                i3 = 0;
                                                                i5 = 0;
                                                                FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                z5 = z8;
                                                                i2 = i5;
                                                                bitmap10 = bitmap9;
                                                                Thread.interrupted();
                                                                if (BuildVars.LOGS_ENABLED) {
                                                                }
                                                                if (bitmap10 != null) {
                                                                }
                                                                cacheImage = this.cacheImage;
                                                                if (cacheImage != null) {
                                                                }
                                                                onPostExecute(bitmap10 != null ? new ExtendedBitmapDrawable(bitmap10, i3, i2) : null);
                                                                return;
                                                            }
                                                        } else {
                                                            thumbnail = MediaStore.Video.Thumbnails.getThumbnail(ApplicationLoader.applicationContext.getContentResolver(), l2.longValue(), 1, options);
                                                        }
                                                    } else {
                                                        thumbnail = MediaStore.Images.Thumbnails.getThumbnail(ApplicationLoader.applicationContext.getContentResolver(), l2.longValue(), 1, options);
                                                    }
                                                    bitmap9 = thumbnail;
                                                } else {
                                                    bitmap9 = bitmap12;
                                                }
                                                if (bitmap9 != null) {
                                                    if (bitmap9 == null) {
                                                        try {
                                                            if (bitmap2 != null) {
                                                                fileInputStream2 = new EncryptedFileInputStream(file3, bitmap2);
                                                            } else if (z31) {
                                                                fileInputStream2 = new EncryptedFileInputStream(file3, this.cacheImage.encryptionKeyPath);
                                                            } else {
                                                                try {
                                                                    fileInputStream2 = new FileInputStream(file3);
                                                                } catch (Throwable th23) {
                                                                    th = th23;
                                                                    z8 = false;
                                                                    i3 = 0;
                                                                    i5 = 0;
                                                                    FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                    z5 = z8;
                                                                    i2 = i5;
                                                                    bitmap10 = bitmap9;
                                                                    Thread.interrupted();
                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                    }
                                                                    if (bitmap10 != null) {
                                                                    }
                                                                    cacheImage = this.cacheImage;
                                                                    if (cacheImage != null) {
                                                                    }
                                                                    onPostExecute(bitmap10 != null ? new ExtendedBitmapDrawable(bitmap10, i3, i2) : null);
                                                                    return;
                                                                }
                                                            }
                                                            CacheImage cacheImage10 = this.cacheImage;
                                                            try {
                                                                try {
                                                                    if (!(cacheImage10.imageLocation.document instanceof TLRPC$TL_document)) {
                                                                        String str16 = cacheImage10.filter;
                                                                        if (str16 != null) {
                                                                        }
                                                                        intValue = 0;
                                                                        i3 = 0;
                                                                        bitmap9 = BitmapFactory.decodeStream(fileInputStream2, null, options);
                                                                        fileInputStream2.close();
                                                                    }
                                                                    if (bitmap2 == null) {
                                                                        try {
                                                                            if (this.cacheImage.encryptionKeyPath == null) {
                                                                                fileInputStream2.getChannel().position(0L);
                                                                                bitmap9 = BitmapFactory.decodeStream(fileInputStream2, null, options);
                                                                                fileInputStream2.close();
                                                                            }
                                                                        } catch (Throwable th24) {
                                                                            th = th24;
                                                                            i5 = intValue;
                                                                            bitmap9 = bitmap9;
                                                                            z8 = false;
                                                                            FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                            z5 = z8;
                                                                            i2 = i5;
                                                                            bitmap10 = bitmap9;
                                                                            Thread.interrupted();
                                                                            if (BuildVars.LOGS_ENABLED) {
                                                                            }
                                                                            if (bitmap10 != null) {
                                                                            }
                                                                            cacheImage = this.cacheImage;
                                                                            if (cacheImage != null) {
                                                                            }
                                                                            onPostExecute(bitmap10 != null ? new ExtendedBitmapDrawable(bitmap10, i3, i2) : null);
                                                                            return;
                                                                        }
                                                                    }
                                                                    fileInputStream2.close();
                                                                    if (bitmap2 != null) {
                                                                        fileInputStream2 = new EncryptedFileInputStream(file3, bitmap2);
                                                                    } else if (z31) {
                                                                        fileInputStream2 = new EncryptedFileInputStream(file3, this.cacheImage.encryptionKeyPath);
                                                                    }
                                                                    bitmap9 = BitmapFactory.decodeStream(fileInputStream2, null, options);
                                                                    fileInputStream2.close();
                                                                } catch (Throwable th25) {
                                                                    th = th25;
                                                                    i5 = intValue;
                                                                    bitmap9 = bitmap9;
                                                                    z8 = false;
                                                                    FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                    z5 = z8;
                                                                    i2 = i5;
                                                                    bitmap10 = bitmap9;
                                                                    Thread.interrupted();
                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                    }
                                                                    if (bitmap10 != null) {
                                                                    }
                                                                    cacheImage = this.cacheImage;
                                                                    if (cacheImage != null) {
                                                                    }
                                                                    onPostExecute(bitmap10 != null ? new ExtendedBitmapDrawable(bitmap10, i3, i2) : null);
                                                                    return;
                                                                }
                                                                intValue = ((Integer) imageOrientation.second).intValue();
                                                            } catch (Throwable th26) {
                                                                th = th26;
                                                                z8 = false;
                                                                i5 = 0;
                                                                FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                z5 = z8;
                                                                i2 = i5;
                                                                bitmap10 = bitmap9;
                                                                Thread.interrupted();
                                                                if (BuildVars.LOGS_ENABLED) {
                                                                }
                                                                if (bitmap10 != null) {
                                                                }
                                                                cacheImage = this.cacheImage;
                                                                if (cacheImage != null) {
                                                                }
                                                                onPostExecute(bitmap10 != null ? new ExtendedBitmapDrawable(bitmap10, i3, i2) : null);
                                                                return;
                                                            }
                                                            imageOrientation = AndroidUtilities.getImageOrientation(fileInputStream2);
                                                            i3 = ((Integer) imageOrientation.first).intValue();
                                                        } catch (Throwable th27) {
                                                            th = th27;
                                                        }
                                                    } else {
                                                        intValue = 0;
                                                        i3 = 0;
                                                    }
                                                    if (bitmap9 == null) {
                                                        try {
                                                            RandomAccessFile randomAccessFile5 = new RandomAccessFile(file3, "r");
                                                            int length3 = (int) randomAccessFile5.length();
                                                            byte[] bArr5 = (byte[]) ImageLoader.bytesLocal.get();
                                                            if (bArr5 == null || bArr5.length < length3) {
                                                                bArr5 = null;
                                                            }
                                                            if (bArr5 == null) {
                                                                bArr5 = new byte[length3];
                                                                ImageLoader.bytesLocal.set(bArr5);
                                                            }
                                                            randomAccessFile5.readFully(bArr5, 0, length3);
                                                            randomAccessFile5.close();
                                                            try {
                                                                if (bitmap2 != null) {
                                                                    EncryptedFileInputStream.decryptBytesWithKeyFile(bArr5, 0, length3, bitmap2);
                                                                    i5 = intValue;
                                                                    bitmap2 = bitmap9;
                                                                    byte[] computeSHA2563 = Utilities.computeSHA256(bArr5, 0, length3);
                                                                    if (bArr != null && Arrays.equals(computeSHA2563, bArr)) {
                                                                        z10 = false;
                                                                        i6 = bArr5[0] & 255;
                                                                        length3 -= i6;
                                                                    }
                                                                    z10 = true;
                                                                    i6 = bArr5[0] & 255;
                                                                    length3 -= i6;
                                                                } else {
                                                                    i5 = intValue;
                                                                    bitmap2 = bitmap9;
                                                                    if (z31) {
                                                                        EncryptedFileInputStream.decryptBytesWithKeyFile(bArr5, 0, length3, this.cacheImage.encryptionKeyPath);
                                                                    }
                                                                    z10 = false;
                                                                    i6 = 0;
                                                                }
                                                                if (!z10) {
                                                                    bitmap9 = BitmapFactory.decodeByteArray(bArr5, i6, length3, options);
                                                                }
                                                            } catch (Throwable th28) {
                                                                th = th28;
                                                                bitmap2 = bitmap2;
                                                                try {
                                                                    FileLog.e(th);
                                                                    bitmap9 = bitmap2;
                                                                    if (bitmap9 == null) {
                                                                    }
                                                                } catch (Throwable th29) {
                                                                    th = th29;
                                                                    bitmap9 = bitmap2;
                                                                    z8 = false;
                                                                    FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                    z5 = z8;
                                                                    i2 = i5;
                                                                    bitmap10 = bitmap9;
                                                                    Thread.interrupted();
                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                    }
                                                                    if (bitmap10 != null) {
                                                                    }
                                                                    cacheImage = this.cacheImage;
                                                                    if (cacheImage != null) {
                                                                    }
                                                                    onPostExecute(bitmap10 != null ? new ExtendedBitmapDrawable(bitmap10, i3, i2) : null);
                                                                    return;
                                                                }
                                                            }
                                                        } catch (Throwable th30) {
                                                            th = th30;
                                                            i5 = intValue;
                                                            bitmap2 = bitmap9;
                                                        }
                                                    } else {
                                                        i5 = intValue;
                                                        bitmap2 = bitmap9;
                                                    }
                                                    bitmap9 = bitmap2;
                                                } else {
                                                    i3 = 0;
                                                    i5 = 0;
                                                    bitmap9 = bitmap9;
                                                }
                                                if (bitmap9 == null) {
                                                    if (!z2 || (file3.length() != 0 && this.cacheImage.filter != null)) {
                                                        i2 = i5;
                                                        z5 = false;
                                                        bitmap10 = bitmap9;
                                                        Thread.interrupted();
                                                        if (BuildVars.LOGS_ENABLED) {
                                                        }
                                                        if (bitmap10 != null) {
                                                        }
                                                        cacheImage = this.cacheImage;
                                                        if (cacheImage != null) {
                                                        }
                                                        onPostExecute(bitmap10 != null ? new ExtendedBitmapDrawable(bitmap10, i3, i2) : null);
                                                        return;
                                                    }
                                                    file3.delete();
                                                    z5 = false;
                                                    i2 = i5;
                                                    bitmap10 = bitmap9;
                                                    Thread.interrupted();
                                                    if (BuildVars.LOGS_ENABLED) {
                                                    }
                                                    if (bitmap10 != null) {
                                                    }
                                                    cacheImage = this.cacheImage;
                                                    if (cacheImage != null) {
                                                    }
                                                    onPostExecute(bitmap10 != null ? new ExtendedBitmapDrawable(bitmap10, i3, i2) : null);
                                                    return;
                                                }
                                                if (this.cacheImage.filter != null) {
                                                    float width2 = bitmap9.getWidth();
                                                    float height2 = bitmap9.getHeight();
                                                    if (!options.inPurgeable && f5 != 0.0f && width2 != f5 && width2 > f5 + 20.0f) {
                                                        if (width2 <= height2 || f5 <= f4) {
                                                            float f11 = height2 / f4;
                                                            if (f11 > 1.0f) {
                                                                createScaledBitmap2 = Bitmaps.createScaledBitmap(bitmap9, (int) (width2 / f11), (int) f4, true);
                                                                if (bitmap9 != createScaledBitmap2) {
                                                                    bitmap9.recycle();
                                                                    bitmap9 = createScaledBitmap2;
                                                                }
                                                            }
                                                            createScaledBitmap2 = bitmap9;
                                                            if (bitmap9 != createScaledBitmap2) {
                                                            }
                                                        } else {
                                                            float f12 = width2 / f5;
                                                            if (f12 > 1.0f) {
                                                                createScaledBitmap2 = Bitmaps.createScaledBitmap(bitmap9, (int) f5, (int) (height2 / f12), true);
                                                                if (bitmap9 != createScaledBitmap2) {
                                                                }
                                                            }
                                                            createScaledBitmap2 = bitmap9;
                                                            if (bitmap9 != createScaledBitmap2) {
                                                            }
                                                        }
                                                        Thread.interrupted();
                                                        if (BuildVars.LOGS_ENABLED) {
                                                        }
                                                        if (bitmap10 != null) {
                                                        }
                                                        cacheImage = this.cacheImage;
                                                        if (cacheImage != null) {
                                                        }
                                                        onPostExecute(bitmap10 != null ? new ExtendedBitmapDrawable(bitmap10, i3, i2) : null);
                                                        return;
                                                    }
                                                    if (bitmap9 != null) {
                                                        if (z33) {
                                                            Bitmap createScaledBitmap3 = bitmap9.getWidth() * bitmap9.getHeight() > 22500 ? Bitmaps.createScaledBitmap(bitmap9, 100, 100, false) : bitmap9;
                                                            z8 = Utilities.needInvert(createScaledBitmap3, !options.inPurgeable ? 1 : 0, createScaledBitmap3.getWidth(), createScaledBitmap3.getHeight(), createScaledBitmap3.getRowBytes()) != 0;
                                                            if (createScaledBitmap3 != bitmap9) {
                                                                try {
                                                                    createScaledBitmap3.recycle();
                                                                } catch (Throwable th31) {
                                                                    th = th31;
                                                                    FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                    z5 = z8;
                                                                    i2 = i5;
                                                                    bitmap10 = bitmap9;
                                                                    Thread.interrupted();
                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                    }
                                                                    if (bitmap10 != null) {
                                                                    }
                                                                    cacheImage = this.cacheImage;
                                                                    if (cacheImage != null) {
                                                                    }
                                                                    onPostExecute(bitmap10 != null ? new ExtendedBitmapDrawable(bitmap10, i3, i2) : null);
                                                                    return;
                                                                }
                                                            }
                                                        } else {
                                                            z8 = false;
                                                        }
                                                        if (c != 0 && (height2 > 100.0f || width2 > 100.0f)) {
                                                            height2 = 80.0f;
                                                            bitmap9 = Bitmaps.createScaledBitmap(bitmap9, 80, 80, false);
                                                            width2 = 80.0f;
                                                        }
                                                        if (c == 0 || height2 >= 100.0f || width2 >= 100.0f) {
                                                            z9 = false;
                                                        } else {
                                                            if (bitmap9.getConfig() == Bitmap.Config.ARGB_8888) {
                                                                Utilities.blurBitmap(bitmap9, 3, !options.inPurgeable ? 1 : 0, bitmap9.getWidth(), bitmap9.getHeight(), bitmap9.getRowBytes());
                                                            }
                                                            z9 = true;
                                                        }
                                                        if (!z9 && options.inPurgeable) {
                                                            Utilities.pinBitmap(bitmap9);
                                                        }
                                                        z5 = z8;
                                                        i2 = i5;
                                                        bitmap10 = bitmap9;
                                                        Thread.interrupted();
                                                        if (BuildVars.LOGS_ENABLED) {
                                                        }
                                                        if (bitmap10 != null) {
                                                        }
                                                        cacheImage = this.cacheImage;
                                                        if (cacheImage != null) {
                                                        }
                                                        onPostExecute(bitmap10 != null ? new ExtendedBitmapDrawable(bitmap10, i3, i2) : null);
                                                        return;
                                                    }
                                                }
                                                z9 = false;
                                                z8 = false;
                                                if (!z9) {
                                                    Utilities.pinBitmap(bitmap9);
                                                }
                                                z5 = z8;
                                                i2 = i5;
                                                bitmap10 = bitmap9;
                                                Thread.interrupted();
                                                if (BuildVars.LOGS_ENABLED) {
                                                }
                                                if (bitmap10 != null) {
                                                }
                                                cacheImage = this.cacheImage;
                                                if (cacheImage != null) {
                                                }
                                                onPostExecute(bitmap10 != null ? new ExtendedBitmapDrawable(bitmap10, i3, i2) : null);
                                                return;
                                            }
                                        }
                                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                                        options.inDither = false;
                                        if (l2 == null) {
                                        }
                                        bitmap9 = bitmap12;
                                        if (bitmap9 != null) {
                                        }
                                        if (bitmap9 == null) {
                                        }
                                    }
                                }
                            }
                            f5 = f3;
                            ImageLoader.this.lastCacheOutTime = SystemClock.elapsedRealtime();
                            synchronized (this.sync) {
                            }
                        }
                        str = null;
                        l = null;
                        z = false;
                        z2 = true;
                        BitmapFactory.Options options2 = new BitmapFactory.Options();
                        options2.inSampleSize = 1;
                        if (Build.VERSION.SDK_INT < 21) {
                        }
                        boolean z322 = ImageLoader.this.canForce8888;
                        str4 = this.cacheImage.filter;
                        if (str4 == null) {
                        }
                        i = 1;
                        if (this.cacheImage.type == i) {
                        }
                    }
                } finally {
                }
            }
        }

        private Bitmap applyWallpaperSetting(Bitmap bitmap, TLRPC$WallPaper tLRPC$WallPaper) {
            int i;
            if (!tLRPC$WallPaper.pattern || tLRPC$WallPaper.settings == null) {
                TLRPC$WallPaperSettings tLRPC$WallPaperSettings = tLRPC$WallPaper.settings;
                return (tLRPC$WallPaperSettings == null || !tLRPC$WallPaperSettings.blur) ? bitmap : Utilities.blurWallpaper(bitmap);
            }
            Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            TLRPC$WallPaperSettings tLRPC$WallPaperSettings2 = tLRPC$WallPaper.settings;
            boolean z = true;
            if (tLRPC$WallPaperSettings2.second_background_color == 0) {
                i = AndroidUtilities.getPatternColor(tLRPC$WallPaperSettings2.background_color);
                canvas.drawColor(ColorUtils.setAlphaComponent(tLRPC$WallPaper.settings.background_color, NotificationCenter.voipServiceCreated));
            } else if (tLRPC$WallPaperSettings2.third_background_color == 0) {
                int alphaComponent = ColorUtils.setAlphaComponent(tLRPC$WallPaperSettings2.background_color, NotificationCenter.voipServiceCreated);
                int alphaComponent2 = ColorUtils.setAlphaComponent(tLRPC$WallPaper.settings.second_background_color, NotificationCenter.voipServiceCreated);
                int averageColor = AndroidUtilities.getAverageColor(alphaComponent, alphaComponent2);
                GradientDrawable gradientDrawable = new GradientDrawable(BackgroundGradientDrawable.getGradientOrientation(tLRPC$WallPaper.settings.rotation), new int[]{alphaComponent, alphaComponent2});
                gradientDrawable.setBounds(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
                gradientDrawable.draw(canvas);
                i = averageColor;
            } else {
                int alphaComponent3 = ColorUtils.setAlphaComponent(tLRPC$WallPaperSettings2.background_color, NotificationCenter.voipServiceCreated);
                int alphaComponent4 = ColorUtils.setAlphaComponent(tLRPC$WallPaper.settings.second_background_color, NotificationCenter.voipServiceCreated);
                int alphaComponent5 = ColorUtils.setAlphaComponent(tLRPC$WallPaper.settings.third_background_color, NotificationCenter.voipServiceCreated);
                int i2 = tLRPC$WallPaper.settings.fourth_background_color;
                int alphaComponent6 = i2 == 0 ? 0 : ColorUtils.setAlphaComponent(i2, NotificationCenter.voipServiceCreated);
                int patternColor = MotionBackgroundDrawable.getPatternColor(alphaComponent3, alphaComponent4, alphaComponent5, alphaComponent6);
                MotionBackgroundDrawable motionBackgroundDrawable = new MotionBackgroundDrawable();
                motionBackgroundDrawable.setColors(alphaComponent3, alphaComponent4, alphaComponent5, alphaComponent6);
                motionBackgroundDrawable.setBounds(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
                motionBackgroundDrawable.setPatternBitmap(tLRPC$WallPaper.settings.intensity, bitmap);
                motionBackgroundDrawable.draw(canvas);
                i = patternColor;
                z = false;
            }
            if (z) {
                Paint paint = new Paint(2);
                paint.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.SRC_IN));
                paint.setAlpha((int) ((tLRPC$WallPaper.settings.intensity / 100.0f) * 255.0f));
                canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
            }
            return createBitmap;
        }

        private void loadLastFrame(RLottieDrawable rLottieDrawable, int i, int i2, boolean z, boolean z2) {
            Bitmap createBitmap;
            Canvas canvas;
            Drawable bitmapDrawable;
            if (z && z2) {
                float f = i * 1.2f;
                float f2 = i2 * 1.2f;
                createBitmap = Bitmap.createBitmap((int) f, (int) f2, Bitmap.Config.ARGB_8888);
                canvas = new Canvas(createBitmap);
                canvas.scale(2.0f, 2.0f, f / 2.0f, f2 / 2.0f);
            } else {
                createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
                canvas = new Canvas(createBitmap);
            }
            rLottieDrawable.prepareForGenerateCache();
            Bitmap createBitmap2 = Bitmap.createBitmap(rLottieDrawable.getIntrinsicWidth(), rLottieDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            rLottieDrawable.setGeneratingFrame(z ? rLottieDrawable.getFramesCount() - 1 : 0);
            rLottieDrawable.getNextFrame(createBitmap2);
            rLottieDrawable.releaseForGenerateCache();
            canvas.save();
            if (!z || !z2) {
                canvas.scale(createBitmap2.getWidth() / i, createBitmap2.getHeight() / i2, i / 2.0f, i2 / 2.0f);
            }
            Paint paint = new Paint(1);
            paint.setFilterBitmap(true);
            if (z && z2) {
                canvas.drawBitmap(createBitmap2, (createBitmap.getWidth() - createBitmap2.getWidth()) / 2.0f, (createBitmap.getHeight() - createBitmap2.getHeight()) / 2.0f, paint);
                bitmapDrawable = new ImageReceiver.ReactionLastFrame(createBitmap);
            } else {
                canvas.drawBitmap(createBitmap2, 0.0f, 0.0f, paint);
                bitmapDrawable = new BitmapDrawable(createBitmap);
            }
            rLottieDrawable.recycle(false);
            createBitmap2.recycle();
            onPostExecute(bitmapDrawable);
        }

        private void onPostExecute(final Drawable drawable) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$CacheOutTask$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.CacheOutTask.this.lambda$onPostExecute$1(drawable);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPostExecute$1(final Drawable drawable) {
            boolean z = false;
            final String str = null;
            if (drawable instanceof RLottieDrawable) {
                RLottieDrawable rLottieDrawable = (RLottieDrawable) drawable;
                Drawable drawable2 = (Drawable) ImageLoader.this.lottieMemCache.get(this.cacheImage.key);
                if (drawable2 == null) {
                    ImageLoader.this.lottieMemCache.put(this.cacheImage.key, rLottieDrawable);
                    drawable = rLottieDrawable;
                } else {
                    rLottieDrawable.recycle(false);
                    drawable = drawable2;
                }
                if (drawable != null) {
                    ImageLoader.this.incrementUseCount(this.cacheImage.key);
                    str = this.cacheImage.key;
                }
            } else if (drawable instanceof AnimatedFileDrawable) {
                AnimatedFileDrawable animatedFileDrawable = (AnimatedFileDrawable) drawable;
                if (animatedFileDrawable.isWebmSticker) {
                    BitmapDrawable fromLottieCache = ImageLoader.this.getFromLottieCache(this.cacheImage.key);
                    if (fromLottieCache == null) {
                        ImageLoader.this.lottieMemCache.put(this.cacheImage.key, animatedFileDrawable);
                        drawable = animatedFileDrawable;
                    } else {
                        animatedFileDrawable.recycle();
                        drawable = fromLottieCache;
                    }
                    ImageLoader.this.incrementUseCount(this.cacheImage.key);
                    str = this.cacheImage.key;
                }
            } else if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                BitmapDrawable fromMemCache = ImageLoader.this.getFromMemCache(this.cacheImage.key);
                boolean z2 = true;
                if (fromMemCache == null) {
                    if (this.cacheImage.key.endsWith("_f")) {
                        ImageLoader.this.wallpaperMemCache.put(this.cacheImage.key, bitmapDrawable);
                    } else {
                        if (this.cacheImage.key.endsWith("_isc") || bitmapDrawable.getBitmap().getWidth() > AndroidUtilities.density * 80.0f || bitmapDrawable.getBitmap().getHeight() > AndroidUtilities.density * 80.0f) {
                            ImageLoader.this.memCache.put(this.cacheImage.key, bitmapDrawable);
                        } else {
                            ImageLoader.this.smallImagesMemCache.put(this.cacheImage.key, bitmapDrawable);
                        }
                        z = true;
                    }
                    z2 = z;
                    drawable = bitmapDrawable;
                } else {
                    AndroidUtilities.recycleBitmap(bitmapDrawable.getBitmap());
                    drawable = fromMemCache;
                }
                if (drawable != null && z2) {
                    ImageLoader.this.incrementUseCount(this.cacheImage.key);
                    str = this.cacheImage.key;
                }
            } else {
                drawable = null;
            }
            ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$CacheOutTask$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.CacheOutTask.this.lambda$onPostExecute$0(drawable, str);
                }
            }, this.cacheImage.priority);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPostExecute$0(Drawable drawable, String str) {
            this.cacheImage.setImageAndClear(drawable, str);
        }

        public void cancel() {
            synchronized (this.sync) {
                try {
                    this.isCancelled = true;
                    Thread thread = this.runningThread;
                    if (thread != null) {
                        thread.interrupt();
                    }
                } catch (Exception unused) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAnimatedAvatar(String str) {
        return str != null && str.endsWith("avatar");
    }

    private boolean isPFrame(String str) {
        return str != null && str.endsWith("pframe");
    }

    public BitmapDrawable getFromMemCache(String str) {
        BitmapDrawable bitmapDrawable = this.memCache.get(str);
        if (bitmapDrawable == null) {
            bitmapDrawable = this.smallImagesMemCache.get(str);
        }
        if (bitmapDrawable == null) {
            bitmapDrawable = this.wallpaperMemCache.get(str);
        }
        return bitmapDrawable == null ? getFromLottieCache(str) : bitmapDrawable;
    }

    public static Bitmap getStrippedPhotoBitmap(byte[] bArr, String str) {
        int length = (bArr.length - 3) + Bitmaps.header.length + Bitmaps.footer.length;
        byte[] bArr2 = bytesLocal.get();
        if (bArr2 == null || bArr2.length < length) {
            bArr2 = null;
        }
        if (bArr2 == null) {
            bArr2 = new byte[length];
            bytesLocal.set(bArr2);
        }
        byte[] bArr3 = Bitmaps.header;
        System.arraycopy(bArr3, 0, bArr2, 0, bArr3.length);
        System.arraycopy(bArr, 3, bArr2, Bitmaps.header.length, bArr.length - 3);
        System.arraycopy(Bitmaps.footer, 0, bArr2, (Bitmaps.header.length + bArr.length) - 3, Bitmaps.footer.length);
        boolean z = true;
        bArr2[164] = bArr[1];
        bArr2[166] = bArr[2];
        BitmapFactory.Options options = new BitmapFactory.Options();
        z = (TextUtils.isEmpty(str) || !str.contains("r")) ? false : false;
        options.inPreferredConfig = (SharedConfig.deviceIsHigh() || z) ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bArr2, 0, length, options);
        if (z) {
            Bitmap createBitmap = Bitmap.createBitmap(decodeByteArray.getWidth(), decodeByteArray.getHeight(), decodeByteArray.getConfig());
            Canvas canvas = new Canvas(createBitmap);
            canvas.save();
            canvas.scale(1.2f, 1.2f, decodeByteArray.getWidth() / 2.0f, decodeByteArray.getHeight() / 2.0f);
            canvas.drawBitmap(decodeByteArray, 0.0f, 0.0f, (Paint) null);
            canvas.restore();
            Path path = new Path();
            path.addCircle(decodeByteArray.getWidth() / 2.0f, decodeByteArray.getHeight() / 2.0f, Math.min(decodeByteArray.getWidth(), decodeByteArray.getHeight()) / 2.0f, Path.Direction.CW);
            canvas.clipPath(path);
            canvas.drawBitmap(decodeByteArray, 0.0f, 0.0f, (Paint) null);
            decodeByteArray.recycle();
            decodeByteArray = createBitmap;
        }
        if (decodeByteArray != null && !TextUtils.isEmpty(str) && str.contains("b")) {
            Utilities.blurBitmap(decodeByteArray, 3, 1, decodeByteArray.getWidth(), decodeByteArray.getHeight(), decodeByteArray.getRowBytes());
        }
        return decodeByteArray;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class CacheImage {
        protected ArtworkLoadTask artworkTask;
        protected CacheOutTask cacheTask;
        protected int cacheType;
        protected int currentAccount;
        protected File encryptionKeyPath;
        protected String ext;
        protected String filter;
        protected ArrayList<String> filters;
        protected File finalFilePath;
        protected HttpImageTask httpTask;
        protected ImageLocation imageLocation;
        protected ArrayList<ImageReceiver> imageReceiverArray;
        protected ArrayList<Integer> imageReceiverGuidsArray;
        protected int imageType;
        public boolean isPFrame;
        protected String key;
        protected ArrayList<String> keys;
        protected Object parentObject;
        public int priority;
        public Runnable runningTask;
        protected SecureDocument secureDocument;
        protected long size;
        protected File tempFilePath;
        protected int type;
        protected ArrayList<Integer> types;
        protected String url;

        private CacheImage() {
            this.priority = 1;
            this.imageReceiverArray = new ArrayList<>();
            this.imageReceiverGuidsArray = new ArrayList<>();
            this.keys = new ArrayList<>();
            this.filters = new ArrayList<>();
            this.types = new ArrayList<>();
        }

        public void addImageReceiver(ImageReceiver imageReceiver, String str, String str2, int i, int i2) {
            int indexOf = this.imageReceiverArray.indexOf(imageReceiver);
            if (indexOf >= 0 && Objects.equals(this.imageReceiverArray.get(indexOf).getImageKey(), str)) {
                this.imageReceiverGuidsArray.set(indexOf, Integer.valueOf(i2));
                return;
            }
            this.imageReceiverArray.add(imageReceiver);
            this.imageReceiverGuidsArray.add(Integer.valueOf(i2));
            this.keys.add(str);
            this.filters.add(str2);
            this.types.add(Integer.valueOf(i));
            ImageLoader.this.imageLoadingByTag.put(imageReceiver.getTag(i), this);
        }

        public void replaceImageReceiver(ImageReceiver imageReceiver, String str, String str2, int i, int i2) {
            int indexOf = this.imageReceiverArray.indexOf(imageReceiver);
            if (indexOf == -1) {
                return;
            }
            if (this.types.get(indexOf).intValue() != i) {
                ArrayList<ImageReceiver> arrayList = this.imageReceiverArray;
                indexOf = arrayList.subList(indexOf + 1, arrayList.size()).indexOf(imageReceiver);
                if (indexOf == -1) {
                    return;
                }
            }
            this.imageReceiverGuidsArray.set(indexOf, Integer.valueOf(i2));
            this.keys.set(indexOf, str);
            this.filters.set(indexOf, str2);
        }

        public void setImageReceiverGuid(ImageReceiver imageReceiver, int i) {
            int indexOf = this.imageReceiverArray.indexOf(imageReceiver);
            if (indexOf == -1) {
                return;
            }
            this.imageReceiverGuidsArray.set(indexOf, Integer.valueOf(i));
        }

        public void removeImageReceiver(ImageReceiver imageReceiver) {
            int i = this.type;
            int i2 = 0;
            while (i2 < this.imageReceiverArray.size()) {
                ImageReceiver imageReceiver2 = this.imageReceiverArray.get(i2);
                if (imageReceiver2 == null || imageReceiver2 == imageReceiver) {
                    this.imageReceiverArray.remove(i2);
                    this.imageReceiverGuidsArray.remove(i2);
                    this.keys.remove(i2);
                    this.filters.remove(i2);
                    i = this.types.remove(i2).intValue();
                    if (imageReceiver2 != null) {
                        ImageLoader.this.imageLoadingByTag.remove(imageReceiver2.getTag(i));
                    }
                    i2--;
                }
                i2++;
            }
            if (this.imageReceiverArray.isEmpty()) {
                if (this.imageLocation != null && !ImageLoader.this.forceLoadingImages.containsKey(this.key)) {
                    ImageLocation imageLocation = this.imageLocation;
                    if (imageLocation.location != null) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.imageLocation.location, this.ext);
                    } else if (imageLocation.document != null) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.imageLocation.document);
                    } else if (imageLocation.secureDocument != null) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.imageLocation.secureDocument);
                    } else if (imageLocation.webFile != null) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.imageLocation.webFile);
                    }
                }
                if (this.cacheTask != null) {
                    if (i == 1) {
                        ImageLoader.this.cacheThumbOutQueue.cancelRunnable(this.cacheTask);
                    } else {
                        ImageLoader.this.cacheOutQueue.cancelRunnable(this.cacheTask);
                        ImageLoader.this.cacheOutQueue.cancelRunnable(this.runningTask);
                    }
                    this.cacheTask.cancel();
                    this.cacheTask = null;
                }
                if (this.httpTask != null) {
                    ImageLoader.this.httpTasks.remove(this.httpTask);
                    this.httpTask.cancel(true);
                    this.httpTask = null;
                }
                if (this.artworkTask != null) {
                    ImageLoader.this.artworkTasks.remove(this.artworkTask);
                    this.artworkTask.cancel(true);
                    this.artworkTask = null;
                }
                if (this.url != null) {
                    ImageLoader.this.imageLoadingByUrl.remove(this.url);
                }
                if (this.url != null) {
                    ImageLoader.this.imageLoadingByUrlPframe.remove(this.url);
                }
                String str = this.key;
                if (str != null) {
                    ImageLoader.this.imageLoadingByKeys.remove(str);
                    ImageLoader.this.imageLoadingKeys.remove(ImageLoader.cutFilter(this.key));
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        void changePriority(int i) {
            TLRPC$Document tLRPC$Document;
            SecureDocument secureDocument;
            WebFile webFile;
            TLRPC$FileLocation tLRPC$FileLocation;
            SecureDocument secureDocument2;
            String str;
            WebFile webFile2;
            ImageLocation imageLocation = this.imageLocation;
            if (imageLocation != null) {
                TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated = imageLocation.location;
                if (tLRPC$TL_fileLocationToBeDeprecated != null) {
                    str = this.ext;
                    tLRPC$FileLocation = tLRPC$TL_fileLocationToBeDeprecated;
                    tLRPC$Document = null;
                    secureDocument = null;
                    webFile2 = null;
                } else {
                    TLRPC$Document tLRPC$Document2 = imageLocation.document;
                    if (tLRPC$Document2 != null) {
                        tLRPC$Document = tLRPC$Document2;
                        secureDocument = null;
                    } else {
                        SecureDocument secureDocument3 = imageLocation.secureDocument;
                        if (secureDocument3 != null) {
                            secureDocument = secureDocument3;
                            tLRPC$Document = null;
                            secureDocument2 = null;
                            tLRPC$FileLocation = secureDocument2;
                            webFile = secureDocument2;
                            str = tLRPC$FileLocation;
                            webFile2 = webFile;
                        } else {
                            WebFile webFile3 = imageLocation.webFile;
                            if (webFile3 != null) {
                                webFile = webFile3;
                                tLRPC$Document = null;
                                secureDocument = null;
                                tLRPC$FileLocation = null;
                                str = tLRPC$FileLocation;
                                webFile2 = webFile;
                            } else {
                                tLRPC$Document = null;
                                secureDocument = null;
                            }
                        }
                    }
                    secureDocument2 = secureDocument;
                    tLRPC$FileLocation = secureDocument2;
                    webFile = secureDocument2;
                    str = tLRPC$FileLocation;
                    webFile2 = webFile;
                }
                FileLoader.getInstance(this.currentAccount).changePriority(i, tLRPC$Document, secureDocument, webFile2, tLRPC$FileLocation, str, null);
            }
        }

        public void setImageAndClear(final Drawable drawable, final String str) {
            if (drawable != null) {
                final ArrayList arrayList = new ArrayList(this.imageReceiverArray);
                final ArrayList arrayList2 = new ArrayList(this.imageReceiverGuidsArray);
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$CacheImage$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ImageLoader.CacheImage.this.lambda$setImageAndClear$0(drawable, arrayList, arrayList2, str);
                    }
                });
            }
            for (int i = 0; i < this.imageReceiverArray.size(); i++) {
                ImageLoader.this.imageLoadingByTag.remove(this.imageReceiverArray.get(i).getTag(this.type));
            }
            this.imageReceiverArray.clear();
            this.imageReceiverGuidsArray.clear();
            if (this.url != null) {
                ImageLoader.this.imageLoadingByUrl.remove(this.url);
            }
            if (this.url != null) {
                ImageLoader.this.imageLoadingByUrlPframe.remove(this.url);
            }
            String str2 = this.key;
            if (str2 != null) {
                ImageLoader.this.imageLoadingByKeys.remove(str2);
                ImageLoader.this.imageLoadingKeys.remove(ImageLoader.cutFilter(this.key));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:26:0x0079  */
        /* JADX WARN: Removed duplicated region for block: B:35:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$setImageAndClear$0(Drawable drawable, ArrayList arrayList, ArrayList arrayList2, String str) {
            int i = 0;
            if (drawable instanceof AnimatedFileDrawable) {
                AnimatedFileDrawable animatedFileDrawable = (AnimatedFileDrawable) drawable;
                if (!animatedFileDrawable.isWebmSticker) {
                    boolean z = false;
                    while (i < arrayList.size()) {
                        ImageReceiver imageReceiver = (ImageReceiver) arrayList.get(i);
                        AnimatedFileDrawable makeCopy = i == 0 ? animatedFileDrawable : animatedFileDrawable.makeCopy();
                        if (imageReceiver.setImageBitmapByKey(makeCopy, this.key, this.type, false, ((Integer) arrayList2.get(i)).intValue())) {
                            if (makeCopy == animatedFileDrawable) {
                                z = true;
                            }
                        } else if (makeCopy != animatedFileDrawable) {
                            makeCopy.recycle();
                        }
                        i++;
                    }
                    if (!z) {
                        animatedFileDrawable.recycle();
                    }
                    if (str == null) {
                        ImageLoader.this.decrementUseCount(str);
                        return;
                    }
                    return;
                }
            }
            while (i < arrayList.size()) {
                ((ImageReceiver) arrayList.get(i)).setImageBitmapByKey(drawable, this.key, this.types.get(i).intValue(), false, ((Integer) arrayList2.get(i)).intValue());
                i++;
            }
            if (str == null) {
            }
        }
    }

    public static ImageLoader getInstance() {
        ImageLoader imageLoader = Instance;
        if (imageLoader == null) {
            synchronized (ImageLoader.class) {
                try {
                    imageLoader = Instance;
                    if (imageLoader == null) {
                        imageLoader = new ImageLoader();
                        Instance = imageLoader;
                    }
                } finally {
                }
            }
        }
        return imageLoader;
    }

    public ImageLoader() {
        this.thumbGeneratingQueue.setPriority(1);
        int memoryClass = ((ActivityManager) ApplicationLoader.applicationContext.getSystemService("activity")).getMemoryClass();
        boolean z = memoryClass >= 192;
        this.canForce8888 = z;
        int min = Math.min(z ? 30 : 15, memoryClass / 7) * FileLoaderPriorityQueue.PRIORITY_VALUE_MAX;
        float f = min;
        this.memCache = new LruCache<BitmapDrawable>((int) (0.8f * f)) { // from class: org.telegram.messenger.ImageLoader.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.messenger.LruCache
            public int sizeOf(String str, BitmapDrawable bitmapDrawable) {
                return ImageLoader.this.sizeOfBitmapDrawable(bitmapDrawable);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.messenger.LruCache
            public void entryRemoved(boolean z2, String str, BitmapDrawable bitmapDrawable, BitmapDrawable bitmapDrawable2) {
                if (ImageLoader.this.ignoreRemoval == null || !ImageLoader.this.ignoreRemoval.equals(str)) {
                    Integer num = (Integer) ImageLoader.this.bitmapUseCounts.get(str);
                    if (num == null || num.intValue() == 0) {
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        if (bitmap.isRecycled()) {
                            return;
                        }
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(bitmap);
                        AndroidUtilities.recycleBitmaps(arrayList);
                    }
                }
            }
        };
        this.smallImagesMemCache = new LruCache<BitmapDrawable>((int) (f * 0.2f)) { // from class: org.telegram.messenger.ImageLoader.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.messenger.LruCache
            public int sizeOf(String str, BitmapDrawable bitmapDrawable) {
                return ImageLoader.this.sizeOfBitmapDrawable(bitmapDrawable);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.messenger.LruCache
            public void entryRemoved(boolean z2, String str, BitmapDrawable bitmapDrawable, BitmapDrawable bitmapDrawable2) {
                if (ImageLoader.this.ignoreRemoval == null || !ImageLoader.this.ignoreRemoval.equals(str)) {
                    Integer num = (Integer) ImageLoader.this.bitmapUseCounts.get(str);
                    if (num == null || num.intValue() == 0) {
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        if (bitmap.isRecycled()) {
                            return;
                        }
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(bitmap);
                        AndroidUtilities.recycleBitmaps(arrayList);
                    }
                }
            }
        };
        this.wallpaperMemCache = new LruCache<BitmapDrawable>(min / 4) { // from class: org.telegram.messenger.ImageLoader.3
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.messenger.LruCache
            public int sizeOf(String str, BitmapDrawable bitmapDrawable) {
                return ImageLoader.this.sizeOfBitmapDrawable(bitmapDrawable);
            }
        };
        this.lottieMemCache = new LruCache<BitmapDrawable>(10485760) { // from class: org.telegram.messenger.ImageLoader.4
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.messenger.LruCache
            public int sizeOf(String str, BitmapDrawable bitmapDrawable) {
                return ImageLoader.this.sizeOfBitmapDrawable(bitmapDrawable);
            }

            @Override // org.telegram.messenger.LruCache
            public BitmapDrawable put(String str, BitmapDrawable bitmapDrawable) {
                if (bitmapDrawable instanceof AnimatedFileDrawable) {
                    ImageLoader.this.cachedAnimatedFileDrawables.add((AnimatedFileDrawable) bitmapDrawable);
                }
                return (BitmapDrawable) super.put(str, (String) bitmapDrawable);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.messenger.LruCache
            public void entryRemoved(boolean z2, String str, BitmapDrawable bitmapDrawable, BitmapDrawable bitmapDrawable2) {
                Integer num = (Integer) ImageLoader.this.bitmapUseCounts.get(str);
                boolean z3 = bitmapDrawable instanceof AnimatedFileDrawable;
                if (z3) {
                    ImageLoader.this.cachedAnimatedFileDrawables.remove((AnimatedFileDrawable) bitmapDrawable);
                }
                if (num == null || num.intValue() == 0) {
                    if (z3) {
                        ((AnimatedFileDrawable) bitmapDrawable).recycle();
                    }
                    if (bitmapDrawable instanceof RLottieDrawable) {
                        ((RLottieDrawable) bitmapDrawable).recycle(false);
                    }
                }
            }
        };
        SparseArray sparseArray = new SparseArray();
        File cacheDir = AndroidUtilities.getCacheDir();
        if (!cacheDir.isDirectory()) {
            try {
                cacheDir.mkdirs();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        AndroidUtilities.createEmptyFile(new File(cacheDir, ".nomedia"));
        sparseArray.put(4, cacheDir);
        for (int i = 0; i < 4; i++) {
            FileLoader.getInstance(i).setDelegate(new 5(i));
        }
        FileLoader.setMediaDirs(sparseArray);
        6 r0 = new 6();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.MEDIA_BAD_REMOVAL");
        intentFilter.addAction("android.intent.action.MEDIA_CHECKING");
        intentFilter.addAction("android.intent.action.MEDIA_EJECT");
        intentFilter.addAction("android.intent.action.MEDIA_MOUNTED");
        intentFilter.addAction("android.intent.action.MEDIA_NOFS");
        intentFilter.addAction("android.intent.action.MEDIA_REMOVED");
        intentFilter.addAction("android.intent.action.MEDIA_SHARED");
        intentFilter.addAction("android.intent.action.MEDIA_UNMOUNTABLE");
        intentFilter.addAction("android.intent.action.MEDIA_UNMOUNTED");
        intentFilter.addDataScheme("file");
        try {
            if (Build.VERSION.SDK_INT >= 33) {
                ApplicationLoader.applicationContext.registerReceiver(r0, intentFilter, 4);
            } else {
                ApplicationLoader.applicationContext.registerReceiver(r0, intentFilter);
            }
        } catch (Throwable unused) {
        }
        checkMediaPaths();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 5 implements FileLoader.FileLoaderDelegate {
        final /* synthetic */ int val$currentAccount;

        5(int i) {
            this.val$currentAccount = i;
        }

        @Override // org.telegram.messenger.FileLoader.FileLoaderDelegate
        public void fileUploadProgressChanged(FileUploadOperation fileUploadOperation, final String str, final long j, final long j2, final boolean z) {
            ImageLoader.this.fileProgresses.put(str, new long[]{j, j2});
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long j3 = fileUploadOperation.lastProgressUpdateTime;
            if (j3 == 0 || j3 < elapsedRealtime - 100 || j == j2) {
                fileUploadOperation.lastProgressUpdateTime = elapsedRealtime;
                final int i = this.val$currentAccount;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        ImageLoader.5.lambda$fileUploadProgressChanged$0(i, str, j, j2, z);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$fileUploadProgressChanged$0(int i, String str, long j, long j2, boolean z) {
            NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileUploadProgressChanged, str, Long.valueOf(j), Long.valueOf(j2), Boolean.valueOf(z));
        }

        @Override // org.telegram.messenger.FileLoader.FileLoaderDelegate
        public void fileDidUploaded(final String str, final TLRPC$InputFile tLRPC$InputFile, final TLRPC$InputEncryptedFile tLRPC$InputEncryptedFile, final byte[] bArr, final byte[] bArr2, final long j) {
            DispatchQueue dispatchQueue = Utilities.stageQueue;
            final int i = this.val$currentAccount;
            dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.5.this.lambda$fileDidUploaded$2(i, str, tLRPC$InputFile, tLRPC$InputEncryptedFile, bArr, bArr2, j);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$fileDidUploaded$1(int i, String str, TLRPC$InputFile tLRPC$InputFile, TLRPC$InputEncryptedFile tLRPC$InputEncryptedFile, byte[] bArr, byte[] bArr2, long j) {
            NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileUploaded, str, tLRPC$InputFile, tLRPC$InputEncryptedFile, bArr, bArr2, Long.valueOf(j));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$fileDidUploaded$2(final int i, final String str, final TLRPC$InputFile tLRPC$InputFile, final TLRPC$InputEncryptedFile tLRPC$InputEncryptedFile, final byte[] bArr, final byte[] bArr2, final long j) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.5.lambda$fileDidUploaded$1(i, str, tLRPC$InputFile, tLRPC$InputEncryptedFile, bArr, bArr2, j);
                }
            });
            ImageLoader.this.fileProgresses.remove(str);
        }

        @Override // org.telegram.messenger.FileLoader.FileLoaderDelegate
        public void fileDidFailedUpload(final String str, final boolean z) {
            DispatchQueue dispatchQueue = Utilities.stageQueue;
            final int i = this.val$currentAccount;
            dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.5.this.lambda$fileDidFailedUpload$4(i, str, z);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$fileDidFailedUpload$3(int i, String str, boolean z) {
            NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileUploadFailed, str, Boolean.valueOf(z));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$fileDidFailedUpload$4(final int i, final String str, final boolean z) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.5.lambda$fileDidFailedUpload$3(i, str, z);
                }
            });
            ImageLoader.this.fileProgresses.remove(str);
        }

        @Override // org.telegram.messenger.FileLoader.FileLoaderDelegate
        public void fileDidLoaded(final String str, final File file, final Object obj, final int i) {
            ImageLoader.this.fileProgresses.remove(str);
            final int i2 = this.val$currentAccount;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.5.this.lambda$fileDidLoaded$5(file, str, i2, obj, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$fileDidLoaded$5(File file, String str, int i, Object obj, int i2) {
            FilePathDatabase.FileMeta fileMetadataFromParent;
            int i3;
            if (file != null && ((str.endsWith(".mp4") || str.endsWith(".jpg")) && (fileMetadataFromParent = FileLoader.getFileMetadataFromParent(i, obj)) != null)) {
                MessageObject messageObject = obj instanceof MessageObject ? (MessageObject) obj : null;
                long j = fileMetadataFromParent.dialogId;
                if (j >= 0) {
                    i3 = 1;
                } else {
                    i3 = ChatObject.isChannelAndNotMegaGroup(MessagesController.getInstance(i).getChat(Long.valueOf(-j))) ? 4 : 2;
                }
                if (SaveToGallerySettingsHelper.needSave(i3, fileMetadataFromParent, messageObject, i)) {
                    AndroidUtilities.addMediaToGallery(file.toString());
                }
            }
            NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileLoaded, str, file);
            ImageLoader.this.fileDidLoaded(str, file, i2);
        }

        @Override // org.telegram.messenger.FileLoader.FileLoaderDelegate
        public void fileDidFailedLoad(final String str, final int i) {
            ImageLoader.this.fileProgresses.remove(str);
            final int i2 = this.val$currentAccount;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.5.this.lambda$fileDidFailedLoad$6(str, i, i2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$fileDidFailedLoad$6(String str, int i, int i2) {
            ImageLoader.this.fileDidFailedLoad(str, i);
            NotificationCenter.getInstance(i2).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileLoadFailed, str, Integer.valueOf(i));
        }

        @Override // org.telegram.messenger.FileLoader.FileLoaderDelegate
        public void fileLoadProgressChanged(final FileLoadOperation fileLoadOperation, final String str, final long j, final long j2) {
            ImageLoader.this.fileProgresses.put(str, new long[]{j, j2});
            if (!ImageLoader.this.imageLoadingByUrlPframe.isEmpty() && fileLoadOperation.checkPrefixPreloadFinished()) {
                ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        ImageLoader.5.this.lambda$fileLoadProgressChanged$7(str, fileLoadOperation);
                    }
                });
            }
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long j3 = fileLoadOperation.lastProgressUpdateTime;
            if (j3 == 0 || j3 < elapsedRealtime - 500 || j == 0) {
                fileLoadOperation.lastProgressUpdateTime = elapsedRealtime;
                final int i = this.val$currentAccount;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        ImageLoader.5.lambda$fileLoadProgressChanged$8(i, str, j, j2);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$fileLoadProgressChanged$7(String str, FileLoadOperation fileLoadOperation) {
            CacheImage cacheImage = (CacheImage) ImageLoader.this.imageLoadingByUrlPframe.remove(str);
            if (cacheImage == null) {
                return;
            }
            ImageLoader.this.imageLoadingByUrl.remove(str);
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < cacheImage.imageReceiverArray.size(); i++) {
                String str2 = cacheImage.keys.get(i);
                String str3 = cacheImage.filters.get(i);
                int intValue = cacheImage.types.get(i).intValue();
                ImageReceiver imageReceiver = cacheImage.imageReceiverArray.get(i);
                int intValue2 = cacheImage.imageReceiverGuidsArray.get(i).intValue();
                CacheImage cacheImage2 = ImageLoader.this.imageLoadingByKeys.get(str2);
                if (cacheImage2 == null) {
                    cacheImage2 = new CacheImage();
                    cacheImage2.priority = cacheImage.priority;
                    cacheImage2.secureDocument = cacheImage.secureDocument;
                    cacheImage2.currentAccount = cacheImage.currentAccount;
                    cacheImage2.finalFilePath = fileLoadOperation.getCurrentFile();
                    cacheImage2.parentObject = cacheImage.parentObject;
                    cacheImage2.isPFrame = cacheImage.isPFrame;
                    cacheImage2.key = str2;
                    cacheImage2.imageLocation = cacheImage.imageLocation;
                    cacheImage2.type = intValue;
                    cacheImage2.ext = cacheImage.ext;
                    cacheImage2.encryptionKeyPath = cacheImage.encryptionKeyPath;
                    cacheImage2.cacheTask = new CacheOutTask(cacheImage2);
                    cacheImage2.filter = str3;
                    cacheImage2.imageType = cacheImage.imageType;
                    cacheImage2.cacheType = cacheImage.cacheType;
                    ImageLoader.this.imageLoadingByKeys.put(str2, cacheImage2);
                    ImageLoader.this.imageLoadingKeys.add(ImageLoader.cutFilter(str2));
                    arrayList.add(cacheImage2.cacheTask);
                }
                cacheImage2.addImageReceiver(imageReceiver, str2, str3, intValue, intValue2);
            }
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                CacheOutTask cacheOutTask = (CacheOutTask) arrayList.get(i2);
                if (cacheOutTask.cacheImage.type == 1) {
                    ImageLoader.this.cacheThumbOutQueue.postRunnable(cacheOutTask);
                } else {
                    ImageLoader.this.cacheOutQueue.postRunnable(cacheOutTask, cacheOutTask.cacheImage.priority);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$fileLoadProgressChanged$8(int i, String str, long j, long j2) {
            NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileLoadProgressChanged, str, Long.valueOf(j), Long.valueOf(j2));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 6 extends BroadcastReceiver {
        6() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("file system changed");
            }
            Runnable runnable = new Runnable() { // from class: org.telegram.messenger.ImageLoader$6$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.6.this.lambda$onReceive$0();
                }
            };
            if ("android.intent.action.MEDIA_UNMOUNTED".equals(intent.getAction())) {
                AndroidUtilities.runOnUIThread(runnable, 1000L);
            } else {
                runnable.run();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0() {
            ImageLoader.this.checkMediaPaths();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int sizeOfBitmapDrawable(BitmapDrawable bitmapDrawable) {
        if (bitmapDrawable instanceof AnimatedFileDrawable) {
            AnimatedFileDrawable animatedFileDrawable = (AnimatedFileDrawable) bitmapDrawable;
            return Math.max(animatedFileDrawable.getIntrinsicHeight() * bitmapDrawable.getIntrinsicWidth() * 12, animatedFileDrawable.getRenderingHeight() * animatedFileDrawable.getRenderingWidth() * 12);
        } else if (bitmapDrawable instanceof RLottieDrawable) {
            return bitmapDrawable.getIntrinsicWidth() * bitmapDrawable.getIntrinsicHeight() * 8;
        } else {
            return bitmapDrawable.getBitmap().getByteCount();
        }
    }

    public void checkMediaPaths() {
        checkMediaPaths(null);
    }

    public void checkMediaPaths(final Runnable runnable) {
        this.cacheOutQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.lambda$checkMediaPaths$1(runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkMediaPaths$1(final Runnable runnable) {
        final SparseArray<File> createMediaPaths = createMediaPaths();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.lambda$checkMediaPaths$0(createMediaPaths, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$checkMediaPaths$0(SparseArray sparseArray, Runnable runnable) {
        FileLoader.setMediaDirs(sparseArray);
        if (runnable != null) {
            runnable.run();
        }
    }

    public void addTestWebFile(String str, WebFile webFile) {
        if (str == null || webFile == null) {
            return;
        }
        this.testWebFile.put(str, webFile);
    }

    public void removeTestWebFile(String str) {
        if (str == null) {
            return;
        }
        this.testWebFile.remove(str);
    }

    @TargetApi(26)
    private static void moveDirectory(File file, final File file2) {
        java.nio.file.Path path;
        if (file.exists()) {
            if (file2.exists() || file2.mkdir()) {
                try {
                    path = file.toPath();
                    Stream m = FilesMigrationService$$ExternalSyntheticAPIConversion0.m(path);
                    m.forEach(new Consumer() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda12
                        @Override // j$.util.function.Consumer
                        public final void accept(Object obj) {
                            ImageLoader.lambda$moveDirectory$2(file2, (java.nio.file.Path) obj);
                        }

                        @Override // j$.util.function.Consumer
                        public /* synthetic */ Consumer andThen(Consumer consumer) {
                            return Consumer.-CC.$default$andThen(this, consumer);
                        }
                    });
                    m.close();
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$moveDirectory$2(File file, java.nio.file.Path path) {
        java.nio.file.Path fileName;
        String path2;
        boolean isDirectory;
        java.nio.file.Path path3;
        File file2;
        fileName = path.getFileName();
        path2 = fileName.toString();
        File file3 = new File(file, path2);
        isDirectory = Files.isDirectory(path, new LinkOption[0]);
        if (isDirectory) {
            file2 = path.toFile();
            moveDirectory(file2, file3);
            return;
        }
        try {
            path3 = file3.toPath();
            Files.move(path, path3, new CopyOption[0]);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:68:0x0162, code lost:
        if (r2.mkdirs() != false) goto L167;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0168, code lost:
        if (r2.canWrite() == false) goto L169;
     */
    /* JADX WARN: Removed duplicated region for block: B:157:0x0376 A[Catch: Exception -> 0x0389, TRY_LEAVE, TryCatch #4 {Exception -> 0x0389, blocks: (B:151:0x0359, B:153:0x0367, B:155:0x036d, B:157:0x0376), top: B:186:0x0359, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:167:0x03aa A[Catch: Exception -> 0x03bd, TRY_LEAVE, TryCatch #10 {Exception -> 0x03bd, blocks: (B:161:0x038d, B:163:0x039b, B:165:0x03a1, B:167:0x03aa), top: B:198:0x038d, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:190:0x01e4 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x019d A[Catch: Exception -> 0x00ad, TryCatch #1 {Exception -> 0x00ad, blocks: (B:12:0x0053, B:14:0x005f, B:16:0x006d, B:19:0x0075, B:21:0x007c, B:24:0x00aa, B:27:0x00b0, B:29:0x00bc, B:32:0x00c5, B:34:0x00c8, B:38:0x00e9, B:37:0x00cd, B:39:0x00ec, B:56:0x012d, B:73:0x0190, B:75:0x019d, B:77:0x01a8, B:79:0x01b0, B:81:0x01b8, B:83:0x01c4, B:84:0x01cf, B:85:0x01d2, B:147:0x034e, B:137:0x030c, B:127:0x02cb, B:117:0x028a, B:107:0x0249, B:149:0x0353, B:170:0x03be, B:160:0x038a, B:174:0x03cc, B:97:0x0215, B:55:0x012a, B:57:0x013c, B:59:0x0144, B:72:0x0189, B:62:0x0151, B:64:0x0157, B:69:0x0164, B:71:0x016a, B:67:0x015e, B:171:0x03c2, B:173:0x03c6, B:138:0x030f, B:140:0x0321, B:142:0x0328, B:144:0x0337, B:128:0x02ce, B:130:0x02e0, B:132:0x02e7, B:134:0x02f6, B:118:0x028d, B:120:0x029f, B:122:0x02a6, B:124:0x02b5, B:151:0x0359, B:153:0x0367, B:155:0x036d, B:157:0x0376, B:108:0x024c, B:110:0x025e, B:112:0x0265, B:114:0x0274, B:88:0x01e4, B:90:0x01f4, B:92:0x01fa, B:94:0x0201, B:161:0x038d, B:163:0x039b, B:165:0x03a1, B:167:0x03aa, B:98:0x0218, B:100:0x0228, B:102:0x022e, B:104:0x0235), top: B:180:0x0053, inners: #0, #2, #3, #4, #5, #6, #10, #11 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public SparseArray<File> createMediaPaths() {
        File file;
        File[] externalMediaDirs;
        File file2;
        File file3;
        File[] externalFilesDirs;
        SparseArray<File> sparseArray = new SparseArray<>();
        File cacheDir = AndroidUtilities.getCacheDir();
        if (!cacheDir.isDirectory()) {
            try {
                cacheDir.mkdirs();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        AndroidUtilities.createEmptyFile(new File(cacheDir, ".nomedia"));
        sparseArray.put(4, cacheDir);
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("cache path = " + cacheDir);
        }
        FileLog.d("selected SD card = " + SharedConfig.storageCacheDir);
        try {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                File externalStorageDirectory = Environment.getExternalStorageDirectory();
                File file4 = null;
                if (!TextUtils.isEmpty(SharedConfig.storageCacheDir)) {
                    ArrayList<File> rootDirs = AndroidUtilities.getRootDirs();
                    if (rootDirs != null) {
                        int size = rootDirs.size();
                        int i = 0;
                        while (true) {
                            if (i >= size) {
                                break;
                            }
                            File file5 = rootDirs.get(i);
                            FileLog.d("root dir " + i + " " + file5);
                            if (file5.getAbsolutePath().startsWith(SharedConfig.storageCacheDir)) {
                                externalStorageDirectory = file5;
                                break;
                            }
                            i++;
                        }
                    }
                    if (!externalStorageDirectory.getAbsolutePath().startsWith(SharedConfig.storageCacheDir) && (externalFilesDirs = ApplicationLoader.applicationContext.getExternalFilesDirs(null)) != null) {
                        for (int i2 = 0; i2 < externalFilesDirs.length; i2++) {
                            if (externalFilesDirs[i2] != null) {
                                FileLog.d("dirsDebug " + i2 + " " + externalFilesDirs[i2]);
                            }
                        }
                    }
                }
                FileLog.d("external storage = " + externalStorageDirectory);
                if (Build.VERSION.SDK_INT >= 30) {
                    try {
                        externalMediaDirs = ApplicationLoader.applicationContext.getExternalMediaDirs();
                        if (externalMediaDirs.length > 0) {
                            File publicStorageDir = getPublicStorageDir();
                            try {
                                file = new File(publicStorageDir, "Telegram");
                            } catch (Exception e2) {
                                file = publicStorageDir;
                                e = e2;
                            }
                            try {
                                file.mkdirs();
                            } catch (Exception e3) {
                                e = e3;
                                FileLog.e(e);
                                this.telegramPath = new File(ApplicationLoader.applicationContext.getExternalFilesDir(null), "Telegram");
                                file4 = file;
                                this.telegramPath.mkdirs();
                                if (!this.telegramPath.isDirectory()) {
                                }
                                if (this.telegramPath.isDirectory()) {
                                }
                                if (file4 != null) {
                                    try {
                                        file3 = new File(file4, "Telegram Images");
                                        file3.mkdir();
                                        if (file3.isDirectory()) {
                                            sparseArray.put(100, file3);
                                            if (BuildVars.LOGS_ENABLED) {
                                            }
                                        }
                                    } catch (Exception e4) {
                                        FileLog.e(e4);
                                    }
                                    try {
                                        file2 = new File(file4, "Telegram Video");
                                        file2.mkdir();
                                        if (file2.isDirectory()) {
                                            sparseArray.put(101, file2);
                                            if (BuildVars.LOGS_ENABLED) {
                                            }
                                        }
                                    } catch (Exception e5) {
                                        FileLog.e(e5);
                                    }
                                }
                                SharedConfig.checkSaveToGalleryFiles();
                                return sparseArray;
                            }
                        } else {
                            file = null;
                        }
                    } catch (Exception e6) {
                        e = e6;
                        file = null;
                    }
                    this.telegramPath = new File(ApplicationLoader.applicationContext.getExternalFilesDir(null), "Telegram");
                    file4 = file;
                } else {
                    if (TextUtils.isEmpty(SharedConfig.storageCacheDir) || !externalStorageDirectory.getAbsolutePath().startsWith(SharedConfig.storageCacheDir)) {
                        if (externalStorageDirectory.exists()) {
                            if (externalStorageDirectory.isDirectory()) {
                            }
                            FileLog.d("can't write to this directory = " + externalStorageDirectory + " use files dir");
                            externalStorageDirectory = ApplicationLoader.applicationContext.getExternalFilesDir(null);
                        }
                    }
                    this.telegramPath = new File(externalStorageDirectory, "Telegram");
                }
                this.telegramPath.mkdirs();
                if (!this.telegramPath.isDirectory()) {
                    ArrayList<File> dataDirs = AndroidUtilities.getDataDirs();
                    int size2 = dataDirs.size();
                    int i3 = 0;
                    while (true) {
                        if (i3 >= size2) {
                            break;
                        }
                        File file6 = dataDirs.get(i3);
                        if (file6 != null && !TextUtils.isEmpty(SharedConfig.storageCacheDir) && file6.getAbsolutePath().startsWith(SharedConfig.storageCacheDir)) {
                            File file7 = new File(file6, "Telegram");
                            this.telegramPath = file7;
                            file7.mkdirs();
                            break;
                        }
                        i3++;
                    }
                }
                if (this.telegramPath.isDirectory()) {
                    try {
                        File file8 = new File(this.telegramPath, "Telegram Images");
                        file8.mkdir();
                        if (file8.isDirectory() && canMoveFiles(cacheDir, file8, 0)) {
                            sparseArray.put(0, file8);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("image path = " + file8);
                            }
                        }
                    } catch (Exception e7) {
                        FileLog.e(e7);
                    }
                    try {
                        File file9 = new File(this.telegramPath, "Telegram Video");
                        file9.mkdir();
                        if (file9.isDirectory() && canMoveFiles(cacheDir, file9, 2)) {
                            sparseArray.put(2, file9);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("video path = " + file9);
                            }
                        }
                    } catch (Exception e8) {
                        FileLog.e(e8);
                    }
                    try {
                        File file10 = new File(this.telegramPath, "Telegram Audio");
                        file10.mkdir();
                        if (file10.isDirectory() && canMoveFiles(cacheDir, file10, 1)) {
                            AndroidUtilities.createEmptyFile(new File(file10, ".nomedia"));
                            sparseArray.put(1, file10);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("audio path = " + file10);
                            }
                        }
                    } catch (Exception e9) {
                        FileLog.e(e9);
                    }
                    try {
                        File file11 = new File(this.telegramPath, "Telegram Documents");
                        file11.mkdir();
                        if (file11.isDirectory() && canMoveFiles(cacheDir, file11, 3)) {
                            AndroidUtilities.createEmptyFile(new File(file11, ".nomedia"));
                            sparseArray.put(3, file11);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("documents path = " + file11);
                            }
                        }
                    } catch (Exception e10) {
                        FileLog.e(e10);
                    }
                    try {
                        File file12 = new File(this.telegramPath, "Telegram Files");
                        file12.mkdir();
                        if (file12.isDirectory() && canMoveFiles(cacheDir, file12, 5)) {
                            AndroidUtilities.createEmptyFile(new File(file12, ".nomedia"));
                            sparseArray.put(5, file12);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("files path = " + file12);
                            }
                        }
                    } catch (Exception e11) {
                        FileLog.e(e11);
                    }
                    try {
                        File file13 = new File(this.telegramPath, "Telegram Stories");
                        file13.mkdir();
                        if (file13.isDirectory() && canMoveFiles(cacheDir, file13, 6)) {
                            AndroidUtilities.createEmptyFile(new File(file13, ".nomedia"));
                            sparseArray.put(6, file13);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("stories path = " + file13);
                            }
                        }
                    } catch (Exception e12) {
                        FileLog.e(e12);
                    }
                }
                if (file4 != null && file4.isDirectory()) {
                    file3 = new File(file4, "Telegram Images");
                    file3.mkdir();
                    if (file3.isDirectory() && canMoveFiles(cacheDir, file3, 0)) {
                        sparseArray.put(100, file3);
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("image path = " + file3);
                        }
                    }
                    file2 = new File(file4, "Telegram Video");
                    file2.mkdir();
                    if (file2.isDirectory() && canMoveFiles(cacheDir, file2, 2)) {
                        sparseArray.put(101, file2);
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("video path = " + file2);
                        }
                    }
                }
            } else if (BuildVars.LOGS_ENABLED) {
                FileLog.d("this Android can't rename files");
            }
            SharedConfig.checkSaveToGalleryFiles();
        } catch (Exception e13) {
            FileLog.e(e13);
        }
        return sparseArray;
    }

    /* JADX WARN: Incorrect condition in loop: B:5:0x0018 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private File getPublicStorageDir() {
        File[] externalMediaDirs;
        File[] externalMediaDirs2;
        File[] externalMediaDirs3;
        File[] externalMediaDirs4;
        externalMediaDirs = ApplicationLoader.applicationContext.getExternalMediaDirs();
        File file = externalMediaDirs[0];
        if (!TextUtils.isEmpty(SharedConfig.storageCacheDir)) {
            for (int i = 0; i < externalMediaDirs2.length; i++) {
                externalMediaDirs3 = ApplicationLoader.applicationContext.getExternalMediaDirs();
                File file2 = externalMediaDirs3[i];
                if (file2 != null && file2.getPath().startsWith(SharedConfig.storageCacheDir)) {
                    externalMediaDirs4 = ApplicationLoader.applicationContext.getExternalMediaDirs();
                    file = externalMediaDirs4[i];
                }
            }
        }
        return file;
    }

    private boolean canMoveFiles(File file, File file2, int i) {
        RandomAccessFile randomAccessFile;
        File file3;
        File file4;
        byte[] bArr;
        RandomAccessFile randomAccessFile2 = null;
        try {
            try {
                if (i == 0) {
                    file3 = new File(file, "000000000_999999_temp.f");
                    file4 = new File(file2, "000000000_999999.f");
                } else {
                    if (i != 3 && i != 5 && i != 6) {
                        if (i == 1) {
                            file3 = new File(file, "000000000_999999_temp.f");
                            file4 = new File(file2, "000000000_999999.f");
                        } else if (i == 2) {
                            file3 = new File(file, "000000000_999999_temp.f");
                            file4 = new File(file2, "000000000_999999.f");
                        } else {
                            file4 = null;
                            file3 = null;
                        }
                    }
                    file3 = new File(file, "000000000_999999_temp.f");
                    file4 = new File(file2, "000000000_999999.f");
                }
                bArr = new byte[1024];
                file3.createNewFile();
                randomAccessFile = new RandomAccessFile(file3, "rws");
            } catch (Throwable th) {
                th = th;
                randomAccessFile = null;
            }
        } catch (Exception e) {
            e = e;
        }
        try {
            randomAccessFile.write(bArr);
            randomAccessFile.close();
            boolean renameTo = file3.renameTo(file4);
            file3.delete();
            file4.delete();
            return renameTo;
        } catch (Exception e2) {
            e = e2;
            randomAccessFile2 = randomAccessFile;
            FileLog.e(e);
            if (randomAccessFile2 != null) {
                try {
                    randomAccessFile2.close();
                    return false;
                } catch (Exception e3) {
                    FileLog.e(e3);
                    return false;
                }
            }
            return false;
        } catch (Throwable th2) {
            th = th2;
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (Exception e4) {
                    FileLog.e(e4);
                }
            }
            throw th;
        }
    }

    public Float getFileProgress(String str) {
        long[] jArr;
        if (str == null || (jArr = this.fileProgresses.get(str)) == null) {
            return null;
        }
        long j = jArr[1];
        if (j == 0) {
            return Float.valueOf(0.0f);
        }
        return Float.valueOf(Math.min(1.0f, ((float) jArr[0]) / ((float) j)));
    }

    public long[] getFileProgressSizes(String str) {
        if (str == null) {
            return null;
        }
        return this.fileProgresses.get(str);
    }

    public String getReplacedKey(String str) {
        if (str == null) {
            return null;
        }
        return this.replacedBitmaps.get(str);
    }

    private void performReplace(String str, String str2) {
        LruCache<BitmapDrawable> lruCache = this.memCache;
        BitmapDrawable bitmapDrawable = lruCache.get(str);
        if (bitmapDrawable == null) {
            lruCache = this.smallImagesMemCache;
            bitmapDrawable = lruCache.get(str);
        }
        this.replacedBitmaps.put(str, str2);
        if (bitmapDrawable != null) {
            BitmapDrawable bitmapDrawable2 = lruCache.get(str2);
            if (bitmapDrawable2 != null && bitmapDrawable2.getBitmap() != null && bitmapDrawable.getBitmap() != null) {
                Bitmap bitmap = bitmapDrawable2.getBitmap();
                Bitmap bitmap2 = bitmapDrawable.getBitmap();
                if (bitmap.getWidth() > bitmap2.getWidth() || bitmap.getHeight() > bitmap2.getHeight()) {
                    lruCache.remove(str);
                }
            }
            this.ignoreRemoval = str;
            lruCache.remove(str);
            lruCache.put(str2, bitmapDrawable);
            this.ignoreRemoval = null;
        }
        Integer num = this.bitmapUseCounts.get(str);
        if (num != null) {
            this.bitmapUseCounts.put(str2, num);
            this.bitmapUseCounts.remove(str);
        }
    }

    public void incrementUseCount(String str) {
        Integer num = this.bitmapUseCounts.get(str);
        if (num == null) {
            this.bitmapUseCounts.put(str, 1);
        } else {
            this.bitmapUseCounts.put(str, Integer.valueOf(num.intValue() + 1));
        }
    }

    public boolean decrementUseCount(String str) {
        Integer num = this.bitmapUseCounts.get(str);
        if (num == null) {
            return true;
        }
        if (num.intValue() == 1) {
            this.bitmapUseCounts.remove(str);
            return true;
        }
        this.bitmapUseCounts.put(str, Integer.valueOf(num.intValue() - 1));
        return false;
    }

    public void removeImage(String str) {
        this.bitmapUseCounts.remove(str);
        this.memCache.remove(str);
        this.smallImagesMemCache.remove(str);
    }

    public boolean isInMemCache(String str, boolean z) {
        return z ? getFromLottieCache(str) != null : getFromMemCache(str) != null;
    }

    public void clearMemory() {
        this.smallImagesMemCache.evictAll();
        this.memCache.evictAll();
        this.lottieMemCache.evictAll();
    }

    private void removeFromWaitingForThumb(int i, ImageReceiver imageReceiver) {
        String str = this.waitingForQualityThumbByTag.get(i);
        if (str != null) {
            ThumbGenerateInfo thumbGenerateInfo = this.waitingForQualityThumb.get(str);
            if (thumbGenerateInfo != null) {
                int indexOf = thumbGenerateInfo.imageReceiverArray.indexOf(imageReceiver);
                if (indexOf >= 0) {
                    thumbGenerateInfo.imageReceiverArray.remove(indexOf);
                    thumbGenerateInfo.imageReceiverGuidsArray.remove(indexOf);
                }
                if (thumbGenerateInfo.imageReceiverArray.isEmpty()) {
                    this.waitingForQualityThumb.remove(str);
                }
            }
            this.waitingForQualityThumbByTag.remove(i);
        }
    }

    public void changeFileLoadingPriorityForImageReceiver(final ImageReceiver imageReceiver) {
        if (imageReceiver == null) {
            return;
        }
        final int fileLoadingPriority = imageReceiver.getFileLoadingPriority();
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.lambda$changeFileLoadingPriorityForImageReceiver$3(imageReceiver, fileLoadingPriority);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$changeFileLoadingPriorityForImageReceiver$3(ImageReceiver imageReceiver, int i) {
        CacheImage cacheImage;
        int i2 = 0;
        while (true) {
            int i3 = 3;
            if (i2 >= 3) {
                return;
            }
            if (i2 == 0) {
                i3 = 1;
            } else if (i2 == 1) {
                i3 = 0;
            }
            int tag = imageReceiver.getTag(i3);
            if (tag != 0 && (cacheImage = this.imageLoadingByTag.get(tag)) != null) {
                cacheImage.changePriority(i);
            }
            i2++;
        }
    }

    public void cancelLoadingForImageReceiver(final ImageReceiver imageReceiver, final boolean z) {
        if (imageReceiver == null) {
            return;
        }
        ArrayList<Runnable> loadingOperations = imageReceiver.getLoadingOperations();
        if (!loadingOperations.isEmpty()) {
            for (int i = 0; i < loadingOperations.size(); i++) {
                this.imageLoadQueue.cancelRunnable(loadingOperations.get(i));
            }
            loadingOperations.clear();
        }
        imageReceiver.addLoadingImageRunnable(null);
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.lambda$cancelLoadingForImageReceiver$4(z, imageReceiver);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelLoadingForImageReceiver$4(boolean z, ImageReceiver imageReceiver) {
        int i = 0;
        while (true) {
            int i2 = 3;
            if (i >= 3) {
                return;
            }
            if (i > 0 && !z) {
                return;
            }
            if (i == 0) {
                i2 = 1;
            } else if (i == 1) {
                i2 = 0;
            }
            int tag = imageReceiver.getTag(i2);
            if (tag != 0) {
                if (i == 0) {
                    removeFromWaitingForThumb(tag, imageReceiver);
                }
                CacheImage cacheImage = this.imageLoadingByTag.get(tag);
                if (cacheImage != null) {
                    cacheImage.removeImageReceiver(imageReceiver);
                }
            }
            i++;
        }
    }

    public BitmapDrawable getImageFromMemory(TLObject tLObject, String str, String str2) {
        String str3 = null;
        if (tLObject == null && str == null) {
            return null;
        }
        if (str != null) {
            str3 = Utilities.MD5(str);
        } else if (tLObject instanceof TLRPC$FileLocation) {
            TLRPC$FileLocation tLRPC$FileLocation = (TLRPC$FileLocation) tLObject;
            str3 = tLRPC$FileLocation.volume_id + "_" + tLRPC$FileLocation.local_id;
        } else if (tLObject instanceof TLRPC$Document) {
            TLRPC$Document tLRPC$Document = (TLRPC$Document) tLObject;
            str3 = tLRPC$Document.dc_id + "_" + tLRPC$Document.id;
        } else if (tLObject instanceof SecureDocument) {
            SecureDocument secureDocument = (SecureDocument) tLObject;
            str3 = secureDocument.secureFile.dc_id + "_" + secureDocument.secureFile.id;
        } else if (tLObject instanceof WebFile) {
            str3 = Utilities.MD5(((WebFile) tLObject).url);
        }
        if (str2 != null) {
            str3 = str3 + "@" + str2;
        }
        return getFromMemCache(str3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: replaceImageInCacheInternal */
    public void lambda$replaceImageInCache$5(String str, String str2, ImageLocation imageLocation) {
        ArrayList<String> filterKeys;
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                filterKeys = this.memCache.getFilterKeys(str);
            } else {
                filterKeys = this.smallImagesMemCache.getFilterKeys(str);
            }
            if (filterKeys != null) {
                for (int i2 = 0; i2 < filterKeys.size(); i2++) {
                    String str3 = filterKeys.get(i2);
                    String str4 = str + "@" + str3;
                    String str5 = str2 + "@" + str3;
                    performReplace(str4, str5);
                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didReplacedPhotoInMemCache, str4, str5, imageLocation);
                }
            } else {
                performReplace(str, str2);
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didReplacedPhotoInMemCache, str, str2, imageLocation);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String cutFilter(String str) {
        if (str == null) {
            return null;
        }
        int indexOf = str.indexOf(64);
        return indexOf >= 0 ? str.substring(0, indexOf) : str;
    }

    public void replaceImageInCache(final String str, final String str2, final ImageLocation imageLocation, boolean z) {
        if (z) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.this.lambda$replaceImageInCache$5(str, str2, imageLocation);
                }
            });
        } else {
            lambda$replaceImageInCache$5(str, str2, imageLocation);
        }
    }

    public void putImageToCache(BitmapDrawable bitmapDrawable, String str, boolean z) {
        if (z) {
            this.smallImagesMemCache.put(str, bitmapDrawable);
        } else {
            this.memCache.put(str, bitmapDrawable);
        }
    }

    private void generateThumb(int i, File file, ThumbGenerateInfo thumbGenerateInfo) {
        if ((i != 0 && i != 2 && i != 3) || file == null || thumbGenerateInfo == null) {
            return;
        }
        if (this.thumbGenerateTasks.get(FileLoader.getAttachFileName(thumbGenerateInfo.parentDocument)) == null) {
            this.thumbGeneratingQueue.postRunnable(new ThumbGenerateTask(i, file, thumbGenerateInfo));
        }
    }

    public void cancelForceLoadingForImageReceiver(ImageReceiver imageReceiver) {
        final String imageKey;
        if (imageReceiver == null || (imageKey = imageReceiver.getImageKey()) == null) {
            return;
        }
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.lambda$cancelForceLoadingForImageReceiver$6(imageKey);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelForceLoadingForImageReceiver$6(String str) {
        this.forceLoadingImages.remove(str);
    }

    private void createLoadOperationForImageReceiver(final ImageReceiver imageReceiver, final String str, final String str2, final String str3, final ImageLocation imageLocation, final String str4, final long j, final int i, final int i2, final int i3, final int i4) {
        if (imageReceiver == null || str2 == null || str == null || imageLocation == null) {
            return;
        }
        int tag = imageReceiver.getTag(i2);
        if (tag == 0) {
            tag = this.lastImageNum;
            imageReceiver.setTag(tag, i2);
            int i5 = this.lastImageNum + 1;
            this.lastImageNum = i5;
            if (i5 == Integer.MAX_VALUE) {
                this.lastImageNum = 0;
            }
        }
        final int i6 = tag;
        final boolean isNeedsQualityThumb = imageReceiver.isNeedsQualityThumb();
        final Object parentObject = imageReceiver.getParentObject();
        final TLRPC$Document qualityThumbDocument = imageReceiver.getQualityThumbDocument();
        final boolean isShouldGenerateQualityThumb = imageReceiver.isShouldGenerateQualityThumb();
        final int currentAccount = imageReceiver.getCurrentAccount();
        final boolean z = i2 == 0 && imageReceiver.isCurrentKeyQuality();
        Runnable runnable = new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.lambda$createLoadOperationForImageReceiver$7(i3, str2, str, i6, imageReceiver, i4, str4, i2, imageLocation, z, parentObject, currentAccount, qualityThumbDocument, isNeedsQualityThumb, isShouldGenerateQualityThumb, str3, i, j);
            }
        };
        this.imageLoadQueue.postRunnable(runnable, imageReceiver.getFileLoadingPriority() == 0 ? 0L : 1L);
        imageReceiver.addLoadingImageRunnable(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:149:0x0314, code lost:
        if (r5 == false) goto L150;
     */
    /* JADX WARN: Code restructure failed: missing block: B:198:0x0403, code lost:
        if (r1.equals(r6) != false) goto L182;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x01bc, code lost:
        if (r8.exists() == false) goto L289;
     */
    /* JADX WARN: Removed duplicated region for block: B:207:0x043b  */
    /* JADX WARN: Removed duplicated region for block: B:212:0x044c  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x01b3  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x01c1  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x01c5  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0216  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0229  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$createLoadOperationForImageReceiver$7(int i, String str, String str2, int i2, ImageReceiver imageReceiver, int i3, String str3, int i4, ImageLocation imageLocation, boolean z, Object obj, int i5, TLRPC$Document tLRPC$Document, boolean z2, boolean z3, String str4, int i6, long j) {
        String str5;
        boolean z4;
        int i7;
        boolean z5;
        int i8;
        boolean z6;
        String str6;
        File file;
        String str7;
        String str8;
        int i9;
        boolean z7;
        File file2;
        File file3;
        File file4;
        File file5;
        int i10;
        ImageLoader imageLoader;
        boolean z8;
        int i11;
        boolean z9;
        boolean z10;
        long j2;
        boolean z11;
        String str9;
        String str10;
        boolean z12;
        File file6;
        File file7;
        String str11;
        String str12;
        int i12;
        boolean z13;
        CacheImage cacheImage;
        CacheImage cacheImage2;
        TLRPC$Document tLRPC$Document2 = tLRPC$Document;
        if (i != 2) {
            CacheImage cacheImage3 = this.imageLoadingByUrl.get(str);
            CacheImage cacheImage4 = this.imageLoadingByKeys.get(str2);
            CacheImage cacheImage5 = this.imageLoadingByTag.get(i2);
            if (cacheImage5 != null) {
                if (cacheImage5 == cacheImage4) {
                    cacheImage5.setImageReceiverGuid(imageReceiver, i3);
                    cacheImage = cacheImage3;
                    cacheImage2 = cacheImage4;
                    str5 = str4;
                    z5 = true;
                    i7 = 0;
                } else if (cacheImage5 == cacheImage3) {
                    cacheImage = cacheImage3;
                    if (cacheImage4 == null) {
                        i7 = 0;
                        cacheImage2 = cacheImage4;
                        str5 = str4;
                        cacheImage5.replaceImageReceiver(imageReceiver, str2, str3, i4, i3);
                    } else {
                        cacheImage2 = cacheImage4;
                        str5 = str4;
                        i7 = 0;
                    }
                    z5 = true;
                } else {
                    cacheImage = cacheImage3;
                    cacheImage2 = cacheImage4;
                    str5 = str4;
                    i7 = 0;
                    cacheImage5.removeImageReceiver(imageReceiver);
                }
                if (!z5 || cacheImage2 == null) {
                    z4 = true;
                } else {
                    z4 = true;
                    cacheImage2.addImageReceiver(imageReceiver, str2, str3, i4, i3);
                    z5 = true;
                }
                if (!z5 && cacheImage != null) {
                    cacheImage.addImageReceiver(imageReceiver, str2, str3, i4, i3);
                    z5 = true;
                }
            } else {
                cacheImage = cacheImage3;
                cacheImage2 = cacheImage4;
                str5 = str4;
                i7 = 0;
            }
            z5 = false;
            if (z5) {
            }
            z4 = true;
            if (!z5) {
                cacheImage.addImageReceiver(imageReceiver, str2, str3, i4, i3);
                z5 = true;
            }
        } else {
            str5 = str4;
            z4 = true;
            i7 = 0;
            z5 = false;
        }
        if (!z5) {
            String str13 = imageLocation.path;
            if (str13 != null) {
                if (!str13.startsWith("http") && !str13.startsWith("athumb")) {
                    if (str13.startsWith("thumb://")) {
                        int indexOf = str13.indexOf(":", 8);
                        if (indexOf >= 0) {
                            file2 = new File(str13.substring(indexOf + (z4 ? 1 : 0)));
                            str7 = str3;
                            str8 = "athumb";
                            i9 = 2;
                            z7 = false;
                        }
                        str7 = str3;
                        str8 = "athumb";
                        i9 = 2;
                        z7 = false;
                        file2 = null;
                    } else {
                        if (str13.startsWith("vthumb://")) {
                            int indexOf2 = str13.indexOf(":", 9);
                            if (indexOf2 >= 0) {
                                file2 = new File(str13.substring(indexOf2 + (z4 ? 1 : 0)));
                            }
                            str7 = str3;
                            str8 = "athumb";
                            i9 = 2;
                            z7 = false;
                            file2 = null;
                        } else {
                            file2 = new File(str13);
                        }
                        str7 = str3;
                        str8 = "athumb";
                        i9 = 2;
                        z7 = false;
                    }
                    if (i != i9) {
                        boolean isEncrypted = imageLocation.isEncrypted();
                        CacheImage cacheImage6 = new CacheImage();
                        cacheImage6.priority = imageReceiver.getFileLoadingPriority() == 0 ? 0 : 1;
                        if (!z) {
                            if (imageLocation.imageType == i9 || MessageObject.isGifDocument(imageLocation.webFile) || MessageObject.isGifDocument(imageLocation.document) || MessageObject.isRoundVideoDocument(imageLocation.document) || MessageObject.isVideoSticker(imageLocation.document)) {
                                cacheImage6.imageType = i9;
                            } else {
                                String str14 = imageLocation.path;
                                if (str14 != null && !str14.startsWith("vthumb") && !str14.startsWith("thumb")) {
                                    String httpUrlExtension = getHttpUrlExtension(str14, "jpg");
                                    if (httpUrlExtension.equals("webm") || httpUrlExtension.equals("mp4") || httpUrlExtension.equals("gif")) {
                                        cacheImage6.imageType = i9;
                                    } else if ("tgs".equals(str5)) {
                                        cacheImage6.imageType = 1;
                                    }
                                }
                            }
                        }
                        if (file2 == null) {
                            TLRPC$PhotoSize tLRPC$PhotoSize = imageLocation.photoSize;
                            boolean z14 = tLRPC$PhotoSize instanceof TLRPC$TL_photoStrippedSize;
                            String str15 = AUTOPLAY_FILTER;
                            if (z14 || (tLRPC$PhotoSize instanceof TLRPC$TL_photoPathSize)) {
                                i10 = i6;
                                imageLoader = this;
                                z10 = z7;
                                j2 = 0;
                                z11 = true;
                            } else {
                                SecureDocument secureDocument = imageLocation.secureDocument;
                                if (secureDocument != null) {
                                    cacheImage6.secureDocument = secureDocument;
                                    z11 = secureDocument.secureFile.dc_id == Integer.MIN_VALUE;
                                    file2 = new File(FileLoader.getDirectory(4), str);
                                    i10 = i6;
                                    imageLoader = this;
                                    z10 = z7;
                                    j2 = 0;
                                } else {
                                    boolean z15 = z7;
                                    if (AUTOPLAY_FILTER.equals(str7) || isAnimatedAvatar(str7)) {
                                        i10 = i6;
                                        str9 = AUTOPLAY_FILTER;
                                        str10 = "_";
                                    } else {
                                        i10 = i6;
                                        str9 = AUTOPLAY_FILTER;
                                        if (i10 == 0) {
                                            str10 = "_";
                                            if (j > 0) {
                                                if (imageLocation.path == null) {
                                                }
                                            }
                                        }
                                        File file8 = new File(FileLoader.getDirectory(4), str);
                                        if (file8.exists()) {
                                            z12 = z4;
                                            z13 = true;
                                        } else {
                                            if (i10 == 2) {
                                                File directory = FileLoader.getDirectory(4);
                                                StringBuilder sb = new StringBuilder();
                                                sb.append(str);
                                                z12 = z4;
                                                sb.append(".enc");
                                                file8 = new File(directory, sb.toString());
                                            } else {
                                                z12 = z4;
                                            }
                                            z13 = z15;
                                        }
                                        TLRPC$Document tLRPC$Document3 = imageLocation.document;
                                        if (tLRPC$Document3 != null) {
                                            if (tLRPC$Document3 instanceof DocumentObject.ThemeDocument) {
                                                boolean z16 = ((DocumentObject.ThemeDocument) tLRPC$Document3).wallpaper == null ? true : z12;
                                                cacheImage6.imageType = 5;
                                                imageLoader = this;
                                                file2 = file8;
                                                z10 = z13;
                                                z11 = z16;
                                                j2 = 0;
                                            } else if ("application/x-tgsdice".equals(tLRPC$Document3.mime_type)) {
                                                cacheImage6.imageType = 1;
                                                imageLoader = this;
                                                file2 = file8;
                                                z10 = z13;
                                                j2 = 0;
                                                z11 = true;
                                            } else if ("application/x-tgsticker".equals(imageLocation.document.mime_type)) {
                                                cacheImage6.imageType = 1;
                                            } else if ("application/x-tgwallpattern".equals(imageLocation.document.mime_type)) {
                                                cacheImage6.imageType = 3;
                                            } else if (FileLoader.getDocumentFileName(imageLocation.document).endsWith(".svg")) {
                                                cacheImage6.imageType = 3;
                                            }
                                            str15 = str9;
                                        }
                                        imageLoader = this;
                                        file2 = file8;
                                        z10 = z13;
                                        j2 = 0;
                                        str15 = str9;
                                        z11 = z12;
                                    }
                                    z12 = z4;
                                    TLRPC$Document tLRPC$Document4 = imageLocation.document;
                                    if (tLRPC$Document4 != null) {
                                        if (tLRPC$Document4 instanceof TLRPC$TL_documentEncrypted) {
                                            file7 = new File(FileLoader.getDirectory(4), str);
                                        } else if (MessageObject.isVideoDocument(tLRPC$Document4)) {
                                            file7 = new File(FileLoader.getDirectory(2), str);
                                        } else {
                                            file7 = new File(FileLoader.getDirectory(3), str);
                                        }
                                        if (isAnimatedAvatar(str7)) {
                                            str11 = str9;
                                        } else {
                                            str11 = str9;
                                        }
                                        if (!file7.exists()) {
                                            File directory2 = FileLoader.getDirectory(4);
                                            StringBuilder sb2 = new StringBuilder();
                                            str12 = str11;
                                            sb2.append(tLRPC$Document4.dc_id);
                                            sb2.append(str10);
                                            sb2.append(tLRPC$Document4.id);
                                            sb2.append(".temp");
                                            file7 = new File(directory2, sb2.toString());
                                            if (!(tLRPC$Document4 instanceof DocumentObject.ThemeDocument)) {
                                                if (((DocumentObject.ThemeDocument) tLRPC$Document4).wallpaper == null) {
                                                    i12 = 5;
                                                    z11 = true;
                                                } else {
                                                    z11 = z12;
                                                    i12 = 5;
                                                }
                                                cacheImage6.imageType = i12;
                                            } else if ("application/x-tgsdice".equals(imageLocation.document.mime_type)) {
                                                cacheImage6.imageType = 1;
                                                z11 = true;
                                            } else {
                                                if ("application/x-tgsticker".equals(tLRPC$Document4.mime_type)) {
                                                    cacheImage6.imageType = 1;
                                                } else if ("application/x-tgwallpattern".equals(tLRPC$Document4.mime_type)) {
                                                    cacheImage6.imageType = 3;
                                                } else if (FileLoader.getDocumentFileName(imageLocation.document).endsWith(".svg")) {
                                                    cacheImage6.imageType = 3;
                                                }
                                                z11 = z12;
                                            }
                                            j2 = tLRPC$Document4.size;
                                            imageLoader = this;
                                            z10 = z15;
                                            str15 = str12;
                                            file2 = file7;
                                        }
                                        str12 = str11;
                                        if (!(tLRPC$Document4 instanceof DocumentObject.ThemeDocument)) {
                                        }
                                        j2 = tLRPC$Document4.size;
                                        imageLoader = this;
                                        z10 = z15;
                                        str15 = str12;
                                        file2 = file7;
                                    } else {
                                        String str16 = str10;
                                        if (imageLocation.webFile != null) {
                                            file2 = new File(FileLoader.getDirectory(3), str);
                                            imageLoader = this;
                                            z10 = z15;
                                            str15 = str9;
                                        } else {
                                            if (i10 == 1) {
                                                file6 = new File(FileLoader.getDirectory(4), str);
                                            } else {
                                                file6 = new File(FileLoader.getDirectory(i7), str);
                                            }
                                            imageLoader = this;
                                            file2 = file6;
                                            if (imageLoader.isAnimatedAvatar(str7)) {
                                                str15 = str9;
                                            } else {
                                                str15 = str9;
                                                if (!str15.equals(str7) || imageLocation.location == null || file2.exists()) {
                                                    z10 = z15;
                                                }
                                            }
                                            z10 = z15;
                                            file2 = new File(FileLoader.getDirectory(4), imageLocation.location.volume_id + str16 + imageLocation.location.local_id + ".temp");
                                            j2 = 0L;
                                            z11 = z12;
                                        }
                                        z11 = z12;
                                        j2 = 0;
                                    }
                                }
                            }
                            if (hasAutoplayFilter(str3) || imageLoader.isAnimatedAvatar(str7)) {
                                cacheImage6.imageType = 2;
                                cacheImage6.size = j2;
                                cacheImage6.isPFrame = imageLoader.isPFrame(str7);
                                if (str15.equals(str7) || imageLoader.isAnimatedAvatar(str7)) {
                                    i11 = i4;
                                    z8 = z10;
                                    z9 = true;
                                }
                            }
                            i11 = i4;
                            z8 = z10;
                            z9 = z11;
                        } else {
                            i10 = i6;
                            boolean z17 = z4;
                            imageLoader = this;
                            z8 = z7;
                            i11 = i4;
                            z9 = z17;
                        }
                        cacheImage6.type = i11;
                        cacheImage6.key = str2;
                        cacheImage6.cacheType = i10;
                        cacheImage6.filter = str7;
                        cacheImage6.imageLocation = imageLocation;
                        cacheImage6.ext = str4;
                        cacheImage6.currentAccount = i5;
                        cacheImage6.parentObject = obj;
                        int i13 = imageLocation.imageType;
                        if (i13 != 0) {
                            cacheImage6.imageType = i13;
                        }
                        if (i10 == 2) {
                            cacheImage6.encryptionKeyPath = new File(FileLoader.getInternalCacheDir(), str + ".enc.key");
                        }
                        String str17 = str8;
                        cacheImage6.addImageReceiver(imageReceiver, str2, str3, i4, i3);
                        if (z9 || z8 || file2.exists()) {
                            cacheImage6.finalFilePath = file2;
                            cacheImage6.imageLocation = imageLocation;
                            cacheImage6.cacheTask = new CacheOutTask(cacheImage6);
                            imageLoader.imageLoadingByKeys.put(str2, cacheImage6);
                            imageLoader.imageLoadingKeys.add(cutFilter(str2));
                            if (i != 0) {
                                imageLoader.cacheThumbOutQueue.postRunnable(cacheImage6.cacheTask);
                                return;
                            } else {
                                cacheImage6.runningTask = imageLoader.cacheOutQueue.postRunnable(cacheImage6.cacheTask, cacheImage6.priority);
                                return;
                            }
                        }
                        cacheImage6.url = str;
                        imageLoader.imageLoadingByUrl.put(str, cacheImage6);
                        if (cacheImage6.isPFrame) {
                            imageLoader.imageLoadingByUrlPframe.put(str, cacheImage6);
                        }
                        String str18 = imageLocation.path;
                        if (str18 != null) {
                            String MD5 = Utilities.MD5(str18);
                            cacheImage6.tempFilePath = new File(FileLoader.getDirectory(4), MD5 + "_temp.jpg");
                            cacheImage6.finalFilePath = file2;
                            if (imageLocation.path.startsWith(str17)) {
                                ArtworkLoadTask artworkLoadTask = new ArtworkLoadTask(cacheImage6);
                                cacheImage6.artworkTask = artworkLoadTask;
                                imageLoader.artworkTasks.add(artworkLoadTask);
                                imageLoader.runArtworkTasks(false);
                                return;
                            }
                            HttpImageTask httpImageTask = new HttpImageTask(cacheImage6, j);
                            cacheImage6.httpTask = httpImageTask;
                            imageLoader.httpTasks.add(httpImageTask);
                            imageLoader.runHttpTasks(false);
                            return;
                        }
                        int fileLoadingPriority = i != 0 ? 3 : imageReceiver.getFileLoadingPriority();
                        if (imageLocation.location != null) {
                            FileLoader.getInstance(i5).loadFile(imageLocation, obj, str4, fileLoadingPriority, (i10 != 0 || (j > 0 && imageLocation.key == null)) ? i10 : 1);
                        } else if (imageLocation.document != null) {
                            FileLoader.getInstance(i5).loadFile(imageLocation.document, obj, fileLoadingPriority, i10);
                        } else if (imageLocation.secureDocument != null) {
                            FileLoader.getInstance(i5).loadFile(imageLocation.secureDocument, fileLoadingPriority);
                        } else if (imageLocation.webFile != null) {
                            FileLoader.getInstance(i5).loadFile(imageLocation.webFile, fileLoadingPriority, i10);
                        }
                        if (imageReceiver.isForceLoding()) {
                            imageLoader.forceLoadingImages.put(cacheImage6.key, 0);
                            return;
                        }
                        return;
                    }
                }
                str7 = str3;
                str8 = "athumb";
                i9 = 2;
                z7 = false;
                file2 = null;
                z4 = false;
                if (i != i9) {
                }
            } else {
                if (i == 0 && z) {
                    if (obj instanceof MessageObject) {
                        MessageObject messageObject = (MessageObject) obj;
                        TLRPC$Document document = messageObject.getDocument();
                        String str19 = messageObject.messageOwner.attachPath;
                        File pathToMessage = FileLoader.getInstance(i5).getPathToMessage(messageObject.messageOwner);
                        i8 = messageObject.getMediaType();
                        file = pathToMessage;
                        z6 = false;
                        tLRPC$Document2 = document;
                        str6 = str19;
                    } else if (tLRPC$Document2 != null) {
                        file = FileLoader.getInstance(i5).getPathToAttach(tLRPC$Document2, z4);
                        i8 = MessageObject.isVideoDocument(tLRPC$Document) ? 2 : 3;
                        z6 = true;
                        str6 = null;
                    } else {
                        i8 = 0;
                        z6 = false;
                        str6 = null;
                        file = null;
                        tLRPC$Document2 = null;
                    }
                    if (tLRPC$Document2 != null) {
                        if (z2) {
                            file3 = file;
                            str8 = "athumb";
                            file4 = new File(FileLoader.getDirectory(4), "q_" + tLRPC$Document2.dc_id + "_" + tLRPC$Document2.id + ".jpg");
                            if (file4.exists()) {
                                z7 = true;
                                if (!TextUtils.isEmpty(str6)) {
                                    file5 = new File(str6);
                                }
                                file5 = null;
                                if (file5 == null) {
                                    file5 = file3;
                                }
                                if (file4 != null) {
                                    String attachFileName = FileLoader.getAttachFileName(tLRPC$Document2);
                                    ThumbGenerateInfo thumbGenerateInfo = this.waitingForQualityThumb.get(attachFileName);
                                    if (thumbGenerateInfo == null) {
                                        thumbGenerateInfo = new ThumbGenerateInfo();
                                        thumbGenerateInfo.parentDocument = tLRPC$Document2;
                                        thumbGenerateInfo.filter = str3;
                                        thumbGenerateInfo.big = z6;
                                        this.waitingForQualityThumb.put(attachFileName, thumbGenerateInfo);
                                    }
                                    if (!thumbGenerateInfo.imageReceiverArray.contains(imageReceiver)) {
                                        thumbGenerateInfo.imageReceiverArray.add(imageReceiver);
                                        thumbGenerateInfo.imageReceiverGuidsArray.add(Integer.valueOf(i3));
                                    }
                                    this.waitingForQualityThumbByTag.put(i2, attachFileName);
                                    if (file5.exists() && z3) {
                                        generateThumb(i8, file5, thumbGenerateInfo);
                                        return;
                                    }
                                    return;
                                }
                                str7 = str3;
                                i9 = 2;
                                file2 = file4;
                            }
                        } else {
                            file3 = file;
                            str8 = "athumb";
                        }
                        z7 = false;
                        file4 = null;
                        if (!TextUtils.isEmpty(str6)) {
                        }
                        file5 = null;
                        if (file5 == null) {
                        }
                        if (file4 != null) {
                        }
                    } else {
                        str7 = str3;
                        str8 = "athumb";
                        i9 = 2;
                        z7 = false;
                        file2 = null;
                    }
                    z4 = true;
                    if (i != i9) {
                    }
                }
                str7 = str3;
                str8 = "athumb";
                i9 = 2;
                z7 = false;
                file2 = null;
                z4 = false;
                if (i != i9) {
                }
            }
        }
    }

    public void preloadArtwork(final String str) {
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.lambda$preloadArtwork$8(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$preloadArtwork$8(String str) {
        String httpUrlExtension = getHttpUrlExtension(str, "jpg");
        String str2 = Utilities.MD5(str) + "." + httpUrlExtension;
        File file = new File(FileLoader.getDirectory(4), str2);
        if (file.exists()) {
            return;
        }
        ImageLocation forPath = ImageLocation.getForPath(str);
        CacheImage cacheImage = new CacheImage();
        cacheImage.type = 1;
        cacheImage.key = Utilities.MD5(str);
        cacheImage.filter = null;
        cacheImage.imageLocation = forPath;
        cacheImage.ext = httpUrlExtension;
        cacheImage.parentObject = null;
        int i = forPath.imageType;
        if (i != 0) {
            cacheImage.imageType = i;
        }
        cacheImage.url = str2;
        this.imageLoadingByUrl.put(str2, cacheImage);
        String MD5 = Utilities.MD5(forPath.path);
        cacheImage.tempFilePath = new File(FileLoader.getDirectory(4), MD5 + "_temp.jpg");
        cacheImage.finalFilePath = file;
        ArtworkLoadTask artworkLoadTask = new ArtworkLoadTask(cacheImage);
        cacheImage.artworkTask = artworkLoadTask;
        this.artworkTasks.add(artworkLoadTask);
        runArtworkTasks(false);
    }

    public void loadImageForImageReceiver(ImageReceiver imageReceiver) {
        loadImageForImageReceiver(imageReceiver, null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:166:0x0299, code lost:
        if (r0.local_id < 0) goto L144;
     */
    /* JADX WARN: Removed duplicated region for block: B:103:0x01a3  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x01b0  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x01c0  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x01cc  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x01db  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x01de  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x01e1  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x01e5  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x01e8  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x01ff  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x03d0  */
    /* JADX WARN: Removed duplicated region for block: B:236:0x0440  */
    /* JADX WARN: Removed duplicated region for block: B:239:0x0447 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:243:0x0462 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:247:0x047d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:253:0x049e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:258:0x04bc A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:262:0x04d6  */
    /* JADX WARN: Removed duplicated region for block: B:269:0x0516  */
    /* JADX WARN: Removed duplicated region for block: B:271:0x051d  */
    /* JADX WARN: Removed duplicated region for block: B:286:0x057f  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00a8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00c0  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x00f2  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x00f5  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0116  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0193  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void loadImageForImageReceiver(ImageReceiver imageReceiver, List<ImageReceiver> list) {
        boolean z;
        boolean z2;
        String imageKey;
        boolean z3;
        String thumbKey;
        boolean z4;
        Object parentObject;
        ImageLocation thumbLocation;
        ImageLocation mediaLocation;
        ImageLocation imageLocation;
        ImageLocation imageLocation2;
        boolean z5;
        String str;
        String ext;
        ImageLocation imageLocation3;
        String str2;
        String str3;
        int i;
        String str4;
        ImageLocation imageLocation4;
        String str5;
        String str6;
        String str7;
        String str8;
        String str9;
        String str10;
        String str11;
        String str12;
        String str13;
        String str14;
        ImageLocation imageLocation5;
        String str15;
        int i2;
        String str16;
        ImageLocation imageLocation6;
        String str17;
        String str18;
        String str19;
        BitmapDrawable bitmapDrawable;
        Drawable findInPreloadImageReceivers;
        BitmapDrawable bitmapDrawable2;
        boolean hasBitmap;
        BitmapDrawable bitmapDrawable3;
        if (imageReceiver == null) {
            return;
        }
        String mediaKey = imageReceiver.getMediaKey();
        int newGuid = imageReceiver.getNewGuid();
        if (mediaKey != null) {
            ImageLocation mediaLocation2 = imageReceiver.getMediaLocation();
            Drawable findInPreloadImageReceivers2 = findInPreloadImageReceivers(mediaKey, list);
            if (findInPreloadImageReceivers2 == null) {
                if (useLottieMemCache(mediaLocation2, mediaKey)) {
                    bitmapDrawable3 = getFromLottieCache(mediaKey);
                } else {
                    bitmapDrawable3 = this.memCache.get(mediaKey);
                    if (bitmapDrawable3 != null) {
                        this.memCache.moveToFront(mediaKey);
                    }
                    if (bitmapDrawable3 == null && (bitmapDrawable3 = this.smallImagesMemCache.get(mediaKey)) != null) {
                        this.smallImagesMemCache.moveToFront(mediaKey);
                    }
                    if (bitmapDrawable3 == null && (bitmapDrawable3 = this.wallpaperMemCache.get(mediaKey)) != null) {
                        this.wallpaperMemCache.moveToFront(mediaKey);
                    }
                }
                findInPreloadImageReceivers2 = bitmapDrawable3;
            }
            if (findInPreloadImageReceivers2 instanceof RLottieDrawable) {
                hasBitmap = ((RLottieDrawable) findInPreloadImageReceivers2).hasBitmap();
            } else {
                hasBitmap = findInPreloadImageReceivers2 instanceof AnimatedFileDrawable ? ((AnimatedFileDrawable) findInPreloadImageReceivers2).hasBitmap() : true;
            }
            if (hasBitmap && findInPreloadImageReceivers2 != null) {
                cancelLoadingForImageReceiver(imageReceiver, true);
                imageReceiver.setImageBitmapByKey(findInPreloadImageReceivers2, mediaKey, 3, true, newGuid);
                if (!imageReceiver.isForcePreview()) {
                    return;
                }
                z = true;
                z2 = false;
                imageKey = imageReceiver.getImageKey();
                if (!z) {
                    ImageLocation imageLocation7 = imageReceiver.getImageLocation();
                    findInPreloadImageReceivers = findInPreloadImageReceivers(imageKey, list);
                    if (findInPreloadImageReceivers == null) {
                        findInPreloadImageReceivers = getFromLottieCache(imageKey);
                    }
                    if (findInPreloadImageReceivers != null) {
                    }
                    if (bitmapDrawable2 != null) {
                    }
                }
                z3 = z;
                thumbKey = imageReceiver.getThumbKey();
                if (thumbKey != null) {
                }
                z4 = false;
                parentObject = imageReceiver.getParentObject();
                TLRPC$Document qualityThumbDocument = imageReceiver.getQualityThumbDocument();
                thumbLocation = imageReceiver.getThumbLocation();
                String thumbFilter = imageReceiver.getThumbFilter();
                mediaLocation = imageReceiver.getMediaLocation();
                String mediaFilter = imageReceiver.getMediaFilter();
                imageLocation = imageReceiver.getImageLocation();
                String imageFilter = imageReceiver.getImageFilter();
                if (imageLocation == null) {
                }
                imageLocation2 = imageLocation;
                z5 = false;
                str = "mp4";
                String str20 = null;
                if (imageLocation2 == null) {
                }
                ImageLocation imageLocation8 = imageLocation2;
                if (mediaLocation != null) {
                }
                str = null;
                ext = imageReceiver.getExt();
                if (ext == null) {
                }
                if (r8 == null) {
                }
                if (str == null) {
                }
                ImageLocation imageLocation9 = imageLocation8;
                imageLocation3 = mediaLocation;
                str2 = null;
                str3 = null;
                String str21 = null;
                String str22 = null;
                i = 0;
                boolean z6 = false;
                while (i < 2) {
                }
                boolean z7 = z4;
                str4 = thumbFilter;
                imageLocation4 = imageLocation9;
                str5 = imageFilter;
                str6 = mediaFilter;
                int i3 = newGuid;
                if (thumbLocation != null) {
                }
                if (str2 != null) {
                }
                str8 = str6;
                if (str3 != null) {
                }
                str9 = str5;
                if (str7 != null) {
                }
                str10 = str4;
                String str23 = str7;
                if (imageReceiver.getUniqKeyPrefix() != null) {
                }
                str11 = str3;
                if (imageReceiver.getUniqKeyPrefix() != null) {
                }
                str12 = str2;
                if (imageLocation4 == null) {
                }
                if (imageLocation3 != null) {
                }
            } else if (findInPreloadImageReceivers2 != null) {
                imageReceiver.setImageBitmapByKey(findInPreloadImageReceivers2, mediaKey, 3, true, newGuid);
                z = false;
                z2 = true;
                imageKey = imageReceiver.getImageKey();
                if (!z && imageKey != null) {
                    ImageLocation imageLocation72 = imageReceiver.getImageLocation();
                    findInPreloadImageReceivers = findInPreloadImageReceivers(imageKey, list);
                    if (findInPreloadImageReceivers == null && useLottieMemCache(imageLocation72, imageKey)) {
                        findInPreloadImageReceivers = getFromLottieCache(imageKey);
                    }
                    if (findInPreloadImageReceivers != null) {
                        bitmapDrawable2 = this.memCache.get(imageKey);
                        if (bitmapDrawable2 != null) {
                            this.memCache.moveToFront(imageKey);
                        }
                        if (bitmapDrawable2 == null && (bitmapDrawable2 = this.smallImagesMemCache.get(imageKey)) != null) {
                            this.smallImagesMemCache.moveToFront(imageKey);
                        }
                        if (bitmapDrawable2 == null && (bitmapDrawable2 = this.wallpaperMemCache.get(imageKey)) != null) {
                            this.wallpaperMemCache.moveToFront(imageKey);
                        }
                    } else {
                        bitmapDrawable2 = findInPreloadImageReceivers;
                    }
                    if (bitmapDrawable2 != null) {
                        cancelLoadingForImageReceiver(imageReceiver, true);
                        imageReceiver.setImageBitmapByKey(bitmapDrawable2, imageKey, 0, true, newGuid);
                        if (!imageReceiver.isForcePreview() && (mediaKey == null || z2)) {
                            return;
                        }
                        z3 = true;
                        thumbKey = imageReceiver.getThumbKey();
                        if (thumbKey != null) {
                            if (useLottieMemCache(imageReceiver.getThumbLocation(), thumbKey)) {
                                bitmapDrawable = getFromLottieCache(thumbKey);
                            } else {
                                bitmapDrawable = this.memCache.get(thumbKey);
                                if (bitmapDrawable != null) {
                                    this.memCache.moveToFront(thumbKey);
                                }
                                if (bitmapDrawable == null && (bitmapDrawable = this.smallImagesMemCache.get(thumbKey)) != null) {
                                    this.smallImagesMemCache.moveToFront(thumbKey);
                                }
                                if (bitmapDrawable == null && (bitmapDrawable = this.wallpaperMemCache.get(thumbKey)) != null) {
                                    this.wallpaperMemCache.moveToFront(thumbKey);
                                }
                            }
                            BitmapDrawable bitmapDrawable4 = bitmapDrawable;
                            if (bitmapDrawable4 != null) {
                                imageReceiver.setImageBitmapByKey(bitmapDrawable4, thumbKey, 1, true, newGuid);
                                cancelLoadingForImageReceiver(imageReceiver, false);
                                if (z3 && imageReceiver.isForcePreview()) {
                                    return;
                                }
                                z4 = true;
                                parentObject = imageReceiver.getParentObject();
                                TLRPC$Document qualityThumbDocument2 = imageReceiver.getQualityThumbDocument();
                                thumbLocation = imageReceiver.getThumbLocation();
                                String thumbFilter2 = imageReceiver.getThumbFilter();
                                mediaLocation = imageReceiver.getMediaLocation();
                                String mediaFilter2 = imageReceiver.getMediaFilter();
                                imageLocation = imageReceiver.getImageLocation();
                                String imageFilter2 = imageReceiver.getImageFilter();
                                if (imageLocation == null && imageReceiver.isNeedsQualityThumb() && imageReceiver.isCurrentKeyQuality()) {
                                    if (!(parentObject instanceof MessageObject)) {
                                        imageLocation2 = ImageLocation.getForDocument(((MessageObject) parentObject).getDocument());
                                    } else if (qualityThumbDocument2 != null) {
                                        imageLocation2 = ImageLocation.getForDocument(qualityThumbDocument2);
                                    }
                                    z5 = true;
                                    str = "mp4";
                                    String str202 = null;
                                    String str24 = (imageLocation2 == null && imageLocation2.imageType == 2) ? "mp4" : null;
                                    ImageLocation imageLocation82 = imageLocation2;
                                    if (mediaLocation != null || mediaLocation.imageType != 2) {
                                        str = null;
                                    }
                                    ext = imageReceiver.getExt();
                                    if (ext == null) {
                                        ext = "jpg";
                                    }
                                    String str25 = str24 == null ? ext : str24;
                                    String str26 = str == null ? ext : str;
                                    ImageLocation imageLocation92 = imageLocation82;
                                    imageLocation3 = mediaLocation;
                                    str2 = null;
                                    str3 = null;
                                    String str212 = null;
                                    String str222 = null;
                                    i = 0;
                                    boolean z62 = false;
                                    while (i < 2) {
                                        if (i == 0) {
                                            imageLocation6 = imageLocation92;
                                            i2 = newGuid;
                                            str16 = str25;
                                        } else {
                                            i2 = newGuid;
                                            str16 = str26;
                                            imageLocation6 = imageLocation3;
                                        }
                                        boolean z8 = z4;
                                        ImageLocation imageLocation10 = imageLocation92;
                                        if (imageLocation6 != null) {
                                            if (imageLocation3 != null) {
                                                imageLocation92 = imageLocation3;
                                            }
                                            String key = imageLocation6.getKey(parentObject, imageLocation92, false);
                                            if (key != null) {
                                                str17 = thumbFilter2;
                                                String key2 = imageLocation6.getKey(parentObject, imageLocation3 != null ? imageLocation3 : imageLocation10, true);
                                                if (imageLocation6.path != null) {
                                                    key2 = key2 + "." + getHttpUrlExtension(imageLocation6.path, "jpg");
                                                    str18 = imageFilter2;
                                                    str19 = mediaFilter2;
                                                } else {
                                                    TLRPC$PhotoSize tLRPC$PhotoSize = imageLocation6.photoSize;
                                                    str18 = imageFilter2;
                                                    if ((tLRPC$PhotoSize instanceof TLRPC$TL_photoStrippedSize) || (tLRPC$PhotoSize instanceof TLRPC$TL_photoPathSize)) {
                                                        str19 = mediaFilter2;
                                                        key2 = key2 + "." + str16;
                                                    } else if (imageLocation6.location != null) {
                                                        key2 = key2 + "." + str16;
                                                        if (imageReceiver.getExt() == null) {
                                                            TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated = imageLocation6.location;
                                                            if (tLRPC$TL_fileLocationToBeDeprecated.key == null) {
                                                                str19 = mediaFilter2;
                                                                if (tLRPC$TL_fileLocationToBeDeprecated.volume_id == -2147483648L) {
                                                                }
                                                            }
                                                        }
                                                        str19 = mediaFilter2;
                                                        z62 = true;
                                                    } else {
                                                        str19 = mediaFilter2;
                                                        WebFile webFile = imageLocation6.webFile;
                                                        if (webFile != null) {
                                                            String mimeTypePart = FileLoader.getMimeTypePart(webFile.mime_type);
                                                            key2 = key2 + "." + getHttpUrlExtension(imageLocation6.webFile.url, mimeTypePart);
                                                        } else if (imageLocation6.secureDocument != null) {
                                                            key2 = key2 + "." + str16;
                                                        } else if (imageLocation6.document != null) {
                                                            if (i == 0 && z5) {
                                                                key = "q_" + key;
                                                            }
                                                            String documentFileName = FileLoader.getDocumentFileName(imageLocation6.document);
                                                            int lastIndexOf = documentFileName.lastIndexOf(46);
                                                            String str27 = "";
                                                            String substring = lastIndexOf == -1 ? "" : documentFileName.substring(lastIndexOf);
                                                            if (substring.length() > 1) {
                                                                str27 = substring;
                                                            } else if ("video/mp4".equals(imageLocation6.document.mime_type)) {
                                                                str27 = ".mp4";
                                                            } else if ("video/x-matroska".equals(imageLocation6.document.mime_type)) {
                                                                str27 = ".mkv";
                                                            }
                                                            key2 = key2 + str27;
                                                            if (MessageObject.isVideoDocument(imageLocation6.document) || MessageObject.isGifDocument(imageLocation6.document) || MessageObject.isRoundVideoDocument(imageLocation6.document) || MessageObject.canPreviewDocument(imageLocation6.document)) {
                                                                z62 = false;
                                                            }
                                                            z62 = true;
                                                        } else if (parentObject instanceof TLRPC$StickerSet) {
                                                            key2 = key2 + "." + str16;
                                                        }
                                                    }
                                                }
                                                if (i == 0) {
                                                    str212 = key2;
                                                    str3 = key;
                                                } else {
                                                    str222 = key2;
                                                    str2 = key;
                                                }
                                                if (imageLocation6 == thumbLocation) {
                                                    if (i == 0) {
                                                        str3 = null;
                                                        imageLocation92 = null;
                                                        str212 = null;
                                                        i++;
                                                        newGuid = i2;
                                                        z4 = z8;
                                                        thumbFilter2 = str17;
                                                        imageFilter2 = str18;
                                                        mediaFilter2 = str19;
                                                    } else {
                                                        str2 = null;
                                                        str222 = null;
                                                        imageLocation3 = null;
                                                    }
                                                }
                                                imageLocation92 = imageLocation10;
                                                i++;
                                                newGuid = i2;
                                                z4 = z8;
                                                thumbFilter2 = str17;
                                                imageFilter2 = str18;
                                                mediaFilter2 = str19;
                                            }
                                        }
                                        str17 = thumbFilter2;
                                        str18 = imageFilter2;
                                        str19 = mediaFilter2;
                                        imageLocation92 = imageLocation10;
                                        i++;
                                        newGuid = i2;
                                        z4 = z8;
                                        thumbFilter2 = str17;
                                        imageFilter2 = str18;
                                        mediaFilter2 = str19;
                                    }
                                    boolean z72 = z4;
                                    str4 = thumbFilter2;
                                    imageLocation4 = imageLocation92;
                                    str5 = imageFilter2;
                                    str6 = mediaFilter2;
                                    int i32 = newGuid;
                                    if (thumbLocation != null) {
                                        ImageLocation strippedLocation = imageReceiver.getStrippedLocation();
                                        if (strippedLocation == null) {
                                            strippedLocation = imageLocation3 != null ? imageLocation3 : imageLocation;
                                        }
                                        String key3 = thumbLocation.getKey(parentObject, strippedLocation, false);
                                        String key4 = thumbLocation.getKey(parentObject, strippedLocation, true);
                                        if (thumbLocation.path != null) {
                                            key4 = key4 + "." + getHttpUrlExtension(thumbLocation.path, "jpg");
                                        } else {
                                            TLRPC$PhotoSize tLRPC$PhotoSize2 = thumbLocation.photoSize;
                                            if ((tLRPC$PhotoSize2 instanceof TLRPC$TL_photoStrippedSize) || (tLRPC$PhotoSize2 instanceof TLRPC$TL_photoPathSize)) {
                                                key4 = key4 + "." + ext;
                                            } else if (thumbLocation.location != null) {
                                                key4 = key4 + "." + ext;
                                            }
                                        }
                                        str202 = key4;
                                        str7 = key3;
                                    } else {
                                        str7 = null;
                                    }
                                    if (str2 != null || str6 == null) {
                                        str8 = str6;
                                    } else {
                                        StringBuilder sb = new StringBuilder();
                                        sb.append(str2);
                                        sb.append("@");
                                        str8 = str6;
                                        sb.append(str8);
                                        str2 = sb.toString();
                                    }
                                    if (str3 != null || str5 == null) {
                                        str9 = str5;
                                    } else {
                                        StringBuilder sb2 = new StringBuilder();
                                        sb2.append(str3);
                                        sb2.append("@");
                                        str9 = str5;
                                        sb2.append(str9);
                                        str3 = sb2.toString();
                                    }
                                    if (str7 != null || str4 == null) {
                                        str10 = str4;
                                    } else {
                                        StringBuilder sb3 = new StringBuilder();
                                        sb3.append(str7);
                                        sb3.append("@");
                                        str10 = str4;
                                        sb3.append(str10);
                                        str7 = sb3.toString();
                                    }
                                    String str232 = str7;
                                    if (imageReceiver.getUniqKeyPrefix() != null || str3 == null) {
                                        str11 = str3;
                                    } else {
                                        str11 = imageReceiver.getUniqKeyPrefix() + str3;
                                    }
                                    if (imageReceiver.getUniqKeyPrefix() != null || str2 == null) {
                                        str12 = str2;
                                    } else {
                                        str12 = imageReceiver.getUniqKeyPrefix() + str2;
                                    }
                                    if (imageLocation4 == null) {
                                        str13 = ext;
                                        str14 = str9;
                                        imageLocation5 = imageLocation4;
                                    } else if (imageLocation4.path != null) {
                                        createLoadOperationForImageReceiver(imageReceiver, str232, str202, ext, thumbLocation, str10, 0L, 1, 1, z72 ? 2 : 1, i32);
                                        createLoadOperationForImageReceiver(imageReceiver, str11, str212, str25, imageLocation4, str9, imageReceiver.getSize(), 1, 0, 0, i32);
                                        return;
                                    } else {
                                        str13 = ext;
                                        imageLocation5 = imageLocation4;
                                        str14 = str9;
                                    }
                                    if (imageLocation3 != null) {
                                        int cacheType = imageReceiver.getCacheType();
                                        int i4 = (cacheType == 0 && z62) ? 1 : cacheType;
                                        int i5 = i4 == 0 ? 1 : i4;
                                        if (z72) {
                                            str15 = str8;
                                        } else {
                                            str15 = str8;
                                            createLoadOperationForImageReceiver(imageReceiver, str232, str202, str13, thumbLocation, str10, 0L, i5, 1, 1, i32);
                                        }
                                        if (!z3) {
                                            createLoadOperationForImageReceiver(imageReceiver, str11, str212, str25, imageLocation5, str14, 0L, 1, 0, 0, i32);
                                        }
                                        if (z2) {
                                            return;
                                        }
                                        createLoadOperationForImageReceiver(imageReceiver, str12, str222, str26, imageLocation3, str15, imageReceiver.getSize(), i4, 3, 0, i32);
                                        return;
                                    }
                                    int cacheType2 = imageReceiver.getCacheType();
                                    int i6 = (cacheType2 == 0 && z62) ? 1 : cacheType2;
                                    createLoadOperationForImageReceiver(imageReceiver, str232, str202, str13, thumbLocation, str10, 0L, i6 == 0 ? 1 : i6, 1, z72 ? 2 : 1, i32);
                                    createLoadOperationForImageReceiver(imageReceiver, str11, str212, str25, imageLocation5, str14, imageReceiver.getSize(), i6, 0, 0, i32);
                                    return;
                                }
                                imageLocation2 = imageLocation;
                                z5 = false;
                                str = "mp4";
                                String str2022 = null;
                                if (imageLocation2 == null) {
                                }
                                ImageLocation imageLocation822 = imageLocation2;
                                if (mediaLocation != null) {
                                }
                                str = null;
                                ext = imageReceiver.getExt();
                                if (ext == null) {
                                }
                                if (str24 == null) {
                                }
                                if (str == null) {
                                }
                                ImageLocation imageLocation922 = imageLocation822;
                                imageLocation3 = mediaLocation;
                                str2 = null;
                                str3 = null;
                                String str2122 = null;
                                String str2222 = null;
                                i = 0;
                                boolean z622 = false;
                                while (i < 2) {
                                }
                                boolean z722 = z4;
                                str4 = thumbFilter2;
                                imageLocation4 = imageLocation922;
                                str5 = imageFilter2;
                                str6 = mediaFilter2;
                                int i322 = newGuid;
                                if (thumbLocation != null) {
                                }
                                if (str2 != null) {
                                }
                                str8 = str6;
                                if (str3 != null) {
                                }
                                str9 = str5;
                                if (str7 != null) {
                                }
                                str10 = str4;
                                String str2322 = str7;
                                if (imageReceiver.getUniqKeyPrefix() != null) {
                                }
                                str11 = str3;
                                if (imageReceiver.getUniqKeyPrefix() != null) {
                                }
                                str12 = str2;
                                if (imageLocation4 == null) {
                                }
                                if (imageLocation3 != null) {
                                }
                            }
                        }
                        z4 = false;
                        parentObject = imageReceiver.getParentObject();
                        TLRPC$Document qualityThumbDocument22 = imageReceiver.getQualityThumbDocument();
                        thumbLocation = imageReceiver.getThumbLocation();
                        String thumbFilter22 = imageReceiver.getThumbFilter();
                        mediaLocation = imageReceiver.getMediaLocation();
                        String mediaFilter22 = imageReceiver.getMediaFilter();
                        imageLocation = imageReceiver.getImageLocation();
                        String imageFilter22 = imageReceiver.getImageFilter();
                        if (imageLocation == null) {
                            if (!(parentObject instanceof MessageObject)) {
                            }
                            z5 = true;
                            str = "mp4";
                            String str20222 = null;
                            if (imageLocation2 == null) {
                            }
                            ImageLocation imageLocation8222 = imageLocation2;
                            if (mediaLocation != null) {
                            }
                            str = null;
                            ext = imageReceiver.getExt();
                            if (ext == null) {
                            }
                            if (str24 == null) {
                            }
                            if (str == null) {
                            }
                            ImageLocation imageLocation9222 = imageLocation8222;
                            imageLocation3 = mediaLocation;
                            str2 = null;
                            str3 = null;
                            String str21222 = null;
                            String str22222 = null;
                            i = 0;
                            boolean z6222 = false;
                            while (i < 2) {
                            }
                            boolean z7222 = z4;
                            str4 = thumbFilter22;
                            imageLocation4 = imageLocation9222;
                            str5 = imageFilter22;
                            str6 = mediaFilter22;
                            int i3222 = newGuid;
                            if (thumbLocation != null) {
                            }
                            if (str2 != null) {
                            }
                            str8 = str6;
                            if (str3 != null) {
                            }
                            str9 = str5;
                            if (str7 != null) {
                            }
                            str10 = str4;
                            String str23222 = str7;
                            if (imageReceiver.getUniqKeyPrefix() != null) {
                            }
                            str11 = str3;
                            if (imageReceiver.getUniqKeyPrefix() != null) {
                            }
                            str12 = str2;
                            if (imageLocation4 == null) {
                            }
                            if (imageLocation3 != null) {
                            }
                        }
                        imageLocation2 = imageLocation;
                        z5 = false;
                        str = "mp4";
                        String str202222 = null;
                        if (imageLocation2 == null) {
                        }
                        ImageLocation imageLocation82222 = imageLocation2;
                        if (mediaLocation != null) {
                        }
                        str = null;
                        ext = imageReceiver.getExt();
                        if (ext == null) {
                        }
                        if (str24 == null) {
                        }
                        if (str == null) {
                        }
                        ImageLocation imageLocation92222 = imageLocation82222;
                        imageLocation3 = mediaLocation;
                        str2 = null;
                        str3 = null;
                        String str212222 = null;
                        String str222222 = null;
                        i = 0;
                        boolean z62222 = false;
                        while (i < 2) {
                        }
                        boolean z72222 = z4;
                        str4 = thumbFilter22;
                        imageLocation4 = imageLocation92222;
                        str5 = imageFilter22;
                        str6 = mediaFilter22;
                        int i32222 = newGuid;
                        if (thumbLocation != null) {
                        }
                        if (str2 != null) {
                        }
                        str8 = str6;
                        if (str3 != null) {
                        }
                        str9 = str5;
                        if (str7 != null) {
                        }
                        str10 = str4;
                        String str232222 = str7;
                        if (imageReceiver.getUniqKeyPrefix() != null) {
                        }
                        str11 = str3;
                        if (imageReceiver.getUniqKeyPrefix() != null) {
                        }
                        str12 = str2;
                        if (imageLocation4 == null) {
                        }
                        if (imageLocation3 != null) {
                        }
                    }
                }
                z3 = z;
                thumbKey = imageReceiver.getThumbKey();
                if (thumbKey != null) {
                }
                z4 = false;
                parentObject = imageReceiver.getParentObject();
                TLRPC$Document qualityThumbDocument222 = imageReceiver.getQualityThumbDocument();
                thumbLocation = imageReceiver.getThumbLocation();
                String thumbFilter222 = imageReceiver.getThumbFilter();
                mediaLocation = imageReceiver.getMediaLocation();
                String mediaFilter222 = imageReceiver.getMediaFilter();
                imageLocation = imageReceiver.getImageLocation();
                String imageFilter222 = imageReceiver.getImageFilter();
                if (imageLocation == null) {
                }
                imageLocation2 = imageLocation;
                z5 = false;
                str = "mp4";
                String str2022222 = null;
                if (imageLocation2 == null) {
                }
                ImageLocation imageLocation822222 = imageLocation2;
                if (mediaLocation != null) {
                }
                str = null;
                ext = imageReceiver.getExt();
                if (ext == null) {
                }
                if (str24 == null) {
                }
                if (str == null) {
                }
                ImageLocation imageLocation922222 = imageLocation822222;
                imageLocation3 = mediaLocation;
                str2 = null;
                str3 = null;
                String str2122222 = null;
                String str2222222 = null;
                i = 0;
                boolean z622222 = false;
                while (i < 2) {
                }
                boolean z722222 = z4;
                str4 = thumbFilter222;
                imageLocation4 = imageLocation922222;
                str5 = imageFilter222;
                str6 = mediaFilter222;
                int i322222 = newGuid;
                if (thumbLocation != null) {
                }
                if (str2 != null) {
                }
                str8 = str6;
                if (str3 != null) {
                }
                str9 = str5;
                if (str7 != null) {
                }
                str10 = str4;
                String str2322222 = str7;
                if (imageReceiver.getUniqKeyPrefix() != null) {
                }
                str11 = str3;
                if (imageReceiver.getUniqKeyPrefix() != null) {
                }
                str12 = str2;
                if (imageLocation4 == null) {
                }
                if (imageLocation3 != null) {
                }
            }
        }
        z = false;
        z2 = false;
        imageKey = imageReceiver.getImageKey();
        if (!z) {
        }
        z3 = z;
        thumbKey = imageReceiver.getThumbKey();
        if (thumbKey != null) {
        }
        z4 = false;
        parentObject = imageReceiver.getParentObject();
        TLRPC$Document qualityThumbDocument2222 = imageReceiver.getQualityThumbDocument();
        thumbLocation = imageReceiver.getThumbLocation();
        String thumbFilter2222 = imageReceiver.getThumbFilter();
        mediaLocation = imageReceiver.getMediaLocation();
        String mediaFilter2222 = imageReceiver.getMediaFilter();
        imageLocation = imageReceiver.getImageLocation();
        String imageFilter2222 = imageReceiver.getImageFilter();
        if (imageLocation == null) {
        }
        imageLocation2 = imageLocation;
        z5 = false;
        str = "mp4";
        String str20222222 = null;
        if (imageLocation2 == null) {
        }
        ImageLocation imageLocation8222222 = imageLocation2;
        if (mediaLocation != null) {
        }
        str = null;
        ext = imageReceiver.getExt();
        if (ext == null) {
        }
        if (str24 == null) {
        }
        if (str == null) {
        }
        ImageLocation imageLocation9222222 = imageLocation8222222;
        imageLocation3 = mediaLocation;
        str2 = null;
        str3 = null;
        String str21222222 = null;
        String str22222222 = null;
        i = 0;
        boolean z6222222 = false;
        while (i < 2) {
        }
        boolean z7222222 = z4;
        str4 = thumbFilter2222;
        imageLocation4 = imageLocation9222222;
        str5 = imageFilter2222;
        str6 = mediaFilter2222;
        int i3222222 = newGuid;
        if (thumbLocation != null) {
        }
        if (str2 != null) {
        }
        str8 = str6;
        if (str3 != null) {
        }
        str9 = str5;
        if (str7 != null) {
        }
        str10 = str4;
        String str23222222 = str7;
        if (imageReceiver.getUniqKeyPrefix() != null) {
        }
        str11 = str3;
        if (imageReceiver.getUniqKeyPrefix() != null) {
        }
        str12 = str2;
        if (imageLocation4 == null) {
        }
        if (imageLocation3 != null) {
        }
    }

    private Drawable findInPreloadImageReceivers(String str, List<ImageReceiver> list) {
        if (list == null) {
            return null;
        }
        for (int i = 0; i < list.size(); i++) {
            ImageReceiver imageReceiver = list.get(i);
            if (str.equals(imageReceiver.getImageKey())) {
                return imageReceiver.getImageDrawable();
            }
            if (str.equals(imageReceiver.getMediaKey())) {
                return imageReceiver.getMediaDrawable();
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BitmapDrawable getFromLottieCache(String str) {
        BitmapDrawable bitmapDrawable = this.lottieMemCache.get(str);
        if ((bitmapDrawable instanceof AnimatedFileDrawable) && ((AnimatedFileDrawable) bitmapDrawable).isRecycled()) {
            this.lottieMemCache.remove(str);
            return null;
        }
        return bitmapDrawable;
    }

    private boolean useLottieMemCache(ImageLocation imageLocation, String str) {
        if (str.endsWith("_firstframe") || str.endsWith("_lastframe")) {
            return false;
        }
        return (imageLocation != null && (MessageObject.isAnimatedStickerDocument(imageLocation.document, true) || imageLocation.imageType == 1 || MessageObject.isVideoSticker(imageLocation.document))) || isAnimatedAvatar(str);
    }

    public boolean hasLottieMemCache(String str) {
        LruCache<BitmapDrawable> lruCache = this.lottieMemCache;
        return lruCache != null && lruCache.contains(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void httpFileLoadError(final String str) {
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.lambda$httpFileLoadError$9(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$httpFileLoadError$9(String str) {
        CacheImage cacheImage = this.imageLoadingByUrl.get(str);
        if (cacheImage == null) {
            return;
        }
        HttpImageTask httpImageTask = cacheImage.httpTask;
        if (httpImageTask != null) {
            HttpImageTask httpImageTask2 = new HttpImageTask(httpImageTask.cacheImage, httpImageTask.imageSize);
            cacheImage.httpTask = httpImageTask2;
            this.httpTasks.add(httpImageTask2);
        }
        runHttpTasks(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void artworkLoadError(final String str) {
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.lambda$artworkLoadError$10(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$artworkLoadError$10(String str) {
        CacheImage cacheImage = this.imageLoadingByUrl.get(str);
        if (cacheImage == null) {
            return;
        }
        ArtworkLoadTask artworkLoadTask = cacheImage.artworkTask;
        if (artworkLoadTask != null) {
            ArtworkLoadTask artworkLoadTask2 = new ArtworkLoadTask(artworkLoadTask.cacheImage);
            cacheImage.artworkTask = artworkLoadTask2;
            this.artworkTasks.add(artworkLoadTask2);
        }
        runArtworkTasks(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fileDidLoaded(final String str, final File file, final int i) {
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.lambda$fileDidLoaded$11(str, i, file);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fileDidLoaded$11(String str, int i, File file) {
        ThumbGenerateInfo thumbGenerateInfo = this.waitingForQualityThumb.get(str);
        if (thumbGenerateInfo != null && thumbGenerateInfo.parentDocument != null) {
            generateThumb(i, file, thumbGenerateInfo);
            this.waitingForQualityThumb.remove(str);
        }
        CacheImage cacheImage = this.imageLoadingByUrl.get(str);
        if (cacheImage == null) {
            return;
        }
        this.imageLoadingByUrl.remove(str);
        this.imageLoadingByUrlPframe.remove(str);
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < cacheImage.imageReceiverArray.size(); i2++) {
            String str2 = cacheImage.keys.get(i2);
            String str3 = cacheImage.filters.get(i2);
            int intValue = cacheImage.types.get(i2).intValue();
            ImageReceiver imageReceiver = cacheImage.imageReceiverArray.get(i2);
            int intValue2 = cacheImage.imageReceiverGuidsArray.get(i2).intValue();
            CacheImage cacheImage2 = this.imageLoadingByKeys.get(str2);
            if (cacheImage2 == null) {
                cacheImage2 = new CacheImage();
                cacheImage2.priority = cacheImage.priority;
                cacheImage2.secureDocument = cacheImage.secureDocument;
                cacheImage2.currentAccount = cacheImage.currentAccount;
                cacheImage2.finalFilePath = file;
                cacheImage2.parentObject = cacheImage.parentObject;
                cacheImage2.isPFrame = cacheImage.isPFrame;
                cacheImage2.key = str2;
                cacheImage2.cacheType = cacheImage.cacheType;
                cacheImage2.imageLocation = cacheImage.imageLocation;
                cacheImage2.type = intValue;
                cacheImage2.ext = cacheImage.ext;
                cacheImage2.encryptionKeyPath = cacheImage.encryptionKeyPath;
                cacheImage2.cacheTask = new CacheOutTask(cacheImage2);
                cacheImage2.filter = str3;
                cacheImage2.imageType = cacheImage.imageType;
                this.imageLoadingByKeys.put(str2, cacheImage2);
                this.imageLoadingKeys.add(cutFilter(str2));
                arrayList.add(cacheImage2.cacheTask);
            }
            cacheImage2.addImageReceiver(imageReceiver, str2, str3, intValue, intValue2);
        }
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            CacheOutTask cacheOutTask = (CacheOutTask) arrayList.get(i3);
            if (cacheOutTask.cacheImage.type != 1) {
                this.cacheOutQueue.postRunnable(cacheOutTask, cacheOutTask.cacheImage.priority);
            } else {
                this.cacheThumbOutQueue.postRunnable(cacheOutTask);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fileDidFailedLoad(final String str, int i) {
        if (i == 1) {
            return;
        }
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.lambda$fileDidFailedLoad$12(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fileDidFailedLoad$12(String str) {
        CacheImage cacheImage = this.imageLoadingByUrl.get(str);
        if (cacheImage != null) {
            cacheImage.setImageAndClear(null, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runHttpTasks(boolean z) {
        if (z) {
            this.currentHttpTasksCount--;
        }
        while (this.currentHttpTasksCount < 4 && !this.httpTasks.isEmpty()) {
            HttpImageTask poll = this.httpTasks.poll();
            if (poll != null) {
                poll.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
                this.currentHttpTasksCount++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runArtworkTasks(boolean z) {
        if (z) {
            this.currentArtworkTasksCount--;
        }
        while (this.currentArtworkTasksCount < 4 && !this.artworkTasks.isEmpty()) {
            try {
                this.artworkTasks.poll().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
                this.currentArtworkTasksCount++;
            } catch (Throwable unused) {
                runArtworkTasks(false);
            }
        }
    }

    public boolean isLoadingHttpFile(String str) {
        return this.httpFileLoadTasksByKeys.containsKey(str);
    }

    public static String getHttpFileName(String str) {
        return Utilities.MD5(str);
    }

    public static File getHttpFilePath(String str, String str2) {
        String httpUrlExtension = getHttpUrlExtension(str, str2);
        File directory = FileLoader.getDirectory(4);
        return new File(directory, Utilities.MD5(str) + "." + httpUrlExtension);
    }

    public void loadHttpFile(String str, String str2, int i) {
        if (str == null || str.length() == 0 || this.httpFileLoadTasksByKeys.containsKey(str)) {
            return;
        }
        String httpUrlExtension = getHttpUrlExtension(str, str2);
        File directory = FileLoader.getDirectory(4);
        File file = new File(directory, Utilities.MD5(str) + "_temp." + httpUrlExtension);
        file.delete();
        HttpFileTask httpFileTask = new HttpFileTask(str, file, httpUrlExtension, i);
        this.httpFileLoadTasks.add(httpFileTask);
        this.httpFileLoadTasksByKeys.put(str, httpFileTask);
        runHttpFileLoadTasks(null, 0);
    }

    public void cancelLoadHttpFile(String str) {
        HttpFileTask httpFileTask = this.httpFileLoadTasksByKeys.get(str);
        if (httpFileTask != null) {
            httpFileTask.cancel(true);
            this.httpFileLoadTasksByKeys.remove(str);
            this.httpFileLoadTasks.remove(httpFileTask);
        }
        Runnable runnable = this.retryHttpsTasks.get(str);
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
        }
        runHttpFileLoadTasks(null, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runHttpFileLoadTasks(final HttpFileTask httpFileTask, final int i) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.lambda$runHttpFileLoadTasks$14(httpFileTask, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runHttpFileLoadTasks$14(HttpFileTask httpFileTask, int i) {
        if (httpFileTask != null) {
            this.currentHttpFileLoadTasksCount--;
        }
        if (httpFileTask != null) {
            if (i == 1) {
                if (!httpFileTask.canRetry) {
                    this.httpFileLoadTasksByKeys.remove(httpFileTask.url);
                    NotificationCenter.getInstance(httpFileTask.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.httpFileDidFailedLoad, httpFileTask.url, 0);
                } else {
                    final HttpFileTask httpFileTask2 = new HttpFileTask(httpFileTask.url, httpFileTask.tempFile, httpFileTask.ext, httpFileTask.currentAccount);
                    Runnable runnable = new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda6
                        @Override // java.lang.Runnable
                        public final void run() {
                            ImageLoader.this.lambda$runHttpFileLoadTasks$13(httpFileTask2);
                        }
                    };
                    this.retryHttpsTasks.put(httpFileTask.url, runnable);
                    AndroidUtilities.runOnUIThread(runnable, 1000L);
                }
            } else if (i == 2) {
                this.httpFileLoadTasksByKeys.remove(httpFileTask.url);
                File file = new File(FileLoader.getDirectory(4), Utilities.MD5(httpFileTask.url) + "." + httpFileTask.ext);
                if (!httpFileTask.tempFile.renameTo(file)) {
                    file = httpFileTask.tempFile;
                }
                NotificationCenter.getInstance(httpFileTask.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.httpFileDidLoad, httpFileTask.url, file.toString());
            }
        }
        while (this.currentHttpFileLoadTasksCount < 2 && !this.httpFileLoadTasks.isEmpty()) {
            this.httpFileLoadTasks.poll().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
            this.currentHttpFileLoadTasksCount++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runHttpFileLoadTasks$13(HttpFileTask httpFileTask) {
        this.httpFileLoadTasks.add(httpFileTask);
        runHttpFileLoadTasks(null, 0);
    }

    public static boolean shouldSendImageAsDocument(String str, Uri uri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        if (str == null && uri != null && uri.getScheme() != null) {
            if (uri.getScheme().contains("file")) {
                str = uri.getPath();
            } else {
                try {
                    str = AndroidUtilities.getPath(uri);
                } catch (Throwable th) {
                    FileLog.e(th);
                }
            }
        }
        if (str != null) {
            BitmapFactory.decodeFile(str, options);
        } else if (uri != null) {
            try {
                InputStream openInputStream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                BitmapFactory.decodeStream(openInputStream, null, options);
                openInputStream.close();
            } catch (Throwable th2) {
                FileLog.e(th2);
                return false;
            }
        }
        float f = options.outWidth;
        float f2 = options.outHeight;
        return f / f2 > 10.0f || f2 / f > 10.0f;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(30:1|(29:6|(1:8)(2:161|(2:165|166))|9|10|(1:12)(1:(21:154|155|156|14|(1:16)(1:152)|17|(1:19)|20|(3:22|(2:23|(1:25)(1:26))|27)|28|(1:30)|31|32|33|(4:37|38|39|40)|48|(9:132|134|135|(5:137|(1:139)(1:145)|140|(1:142)(1:144)|143)|146|(1:148)|53|(2:(1:56)|57)|(3:93|94|(4:96|(1:98)|99|(3:101|102|103)(1:105))(1:106))(1:(6:60|61|(4:63|(1:65)|66|(2:68|69))|71|72|74)(1:92)))|52|53|(0)|(0)(0)))|13|14|(0)(0)|17|(0)|20|(0)|28|(0)|31|32|33|(5:35|37|38|39|40)|48|(1:50)|132|134|135|(0)|146|(0)|53|(0)|(0)(0))|170|10|(0)(0)|13|14|(0)(0)|17|(0)|20|(0)|28|(0)|31|32|33|(0)|48|(0)|132|134|135|(0)|146|(0)|53|(0)|(0)(0)|(2:(0)|(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00d4, code lost:
        if (r9 == null) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00db, code lost:
        if (r9 == null) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00dd, code lost:
        r9.close();
     */
    /* JADX WARN: Removed duplicated region for block: B:119:0x01c0  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x0157 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0047  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x004b  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0077  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0080  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x008e  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x009a  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00ab  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00bc A[Catch: all -> 0x00e1, TRY_LEAVE, TryCatch #2 {all -> 0x00e1, blocks: (B:48:0x00ae, B:50:0x00bc, B:59:0x00dd, B:61:0x00e3, B:63:0x00ed, B:65:0x00f7), top: B:150:0x00ae }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x00ed A[Catch: all -> 0x00e1, TryCatch #2 {all -> 0x00e1, blocks: (B:48:0x00ae, B:50:0x00bc, B:59:0x00dd, B:61:0x00e3, B:63:0x00ed, B:65:0x00f7), top: B:150:0x00ae }] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0106 A[Catch: all -> 0x0128, TryCatch #5 {all -> 0x0128, blocks: (B:66:0x00fc, B:68:0x0106, B:72:0x0117, B:76:0x0124, B:78:0x012a, B:80:0x0134), top: B:156:0x00fc }] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0134 A[Catch: all -> 0x0128, TRY_LEAVE, TryCatch #5 {all -> 0x0128, blocks: (B:66:0x00fc, B:68:0x0106, B:72:0x0117, B:76:0x0124, B:78:0x012a, B:80:0x0134), top: B:156:0x00fc }] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x014a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Bitmap loadBitmap(String str, Uri uri, float f, float f2, boolean z) {
        String str2;
        InputStream openInputStream;
        float max;
        int i;
        Matrix matrix;
        float f3;
        Bitmap createBitmap;
        Pair<Integer, Integer> imageOrientation;
        InputStream inputStream;
        String path;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        if (str == null && uri != null && uri.getScheme() != null) {
            if (uri.getScheme().contains("file")) {
                path = uri.getPath();
            } else if (Build.VERSION.SDK_INT < 30 || !"content".equals(uri.getScheme())) {
                try {
                    path = AndroidUtilities.getPath(uri);
                } catch (Throwable th) {
                    FileLog.e(th);
                }
            }
            str2 = path;
            Bitmap bitmap = null;
            if (str2 == null) {
                BitmapFactory.decodeFile(str2, options);
            } else if (uri != null) {
                try {
                    InputStream openInputStream2 = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                    BitmapFactory.decodeStream(openInputStream2, null, options);
                    openInputStream2.close();
                    openInputStream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                    float f4 = options.outWidth;
                    float f5 = options.outHeight;
                    max = z ? Math.max(f4 / f, f5 / f2) : Math.min(f4 / f, f5 / f2);
                    if (max < 1.0f) {
                        max = 1.0f;
                    }
                    options.inJustDecodeBounds = false;
                    i = (int) max;
                    options.inSampleSize = i;
                    if (i % 2 != 0) {
                        int i2 = 1;
                        while (true) {
                            int i3 = i2 * 2;
                            if (i3 >= options.inSampleSize) {
                                break;
                            }
                            i2 = i3;
                        }
                        options.inSampleSize = i2;
                    }
                    options.inPurgeable = Build.VERSION.SDK_INT < 21;
                    imageOrientation = AndroidUtilities.getImageOrientation(str2);
                    if (((Integer) imageOrientation.first).intValue() == 0 && ((Integer) imageOrientation.second).intValue() == 0) {
                        try {
                            inputStream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                            try {
                                imageOrientation = AndroidUtilities.getImageOrientation(inputStream);
                            } catch (Throwable unused) {
                            }
                        } catch (Throwable unused2) {
                            inputStream = null;
                        }
                    }
                    if (((Integer) imageOrientation.first).intValue() == 0 || ((Integer) imageOrientation.second).intValue() != 0) {
                        matrix = new Matrix();
                        if (((Integer) imageOrientation.second).intValue() != 0) {
                            float f6 = -1.0f;
                            float f7 = ((Integer) imageOrientation.second).intValue() == 1 ? -1.0f : 1.0f;
                            if (((Integer) imageOrientation.second).intValue() != 2) {
                                f6 = 1.0f;
                            }
                            matrix.postScale(f7, f6);
                        }
                        if (((Integer) imageOrientation.first).intValue() != 0) {
                            matrix.postRotate(((Integer) imageOrientation.first).intValue());
                        }
                        f3 = max / options.inSampleSize;
                        if (f3 > 1.0f) {
                            if (matrix == null) {
                                matrix = new Matrix();
                            }
                            float f8 = 1.0f / f3;
                            matrix.postScale(f8, f8);
                        }
                        if (str2 != null) {
                            if (uri != null) {
                                try {
                                    bitmap = BitmapFactory.decodeStream(openInputStream, null, options);
                                    if (bitmap != null) {
                                        if (options.inPurgeable) {
                                            Utilities.pinBitmap(bitmap);
                                        }
                                        Bitmap createBitmap2 = Bitmaps.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                                        if (createBitmap2 != bitmap) {
                                            bitmap.recycle();
                                            bitmap = createBitmap2;
                                        }
                                    }
                                } finally {
                                    try {
                                        openInputStream.close();
                                        return bitmap;
                                    } catch (Throwable th2) {
                                    }
                                }
                                try {
                                    openInputStream.close();
                                    return bitmap;
                                } catch (Throwable th3) {
                                    FileLog.e(th3);
                                    return bitmap;
                                }
                            }
                            return null;
                        }
                        try {
                            bitmap = BitmapFactory.decodeFile(str2, options);
                        } catch (Throwable th4) {
                            FileLog.e(th4);
                            getInstance().clearMemory();
                            if (bitmap == null) {
                                try {
                                    Bitmap decodeFile = BitmapFactory.decodeFile(str2, options);
                                    if (decodeFile != null) {
                                        try {
                                            if (options.inPurgeable) {
                                                Utilities.pinBitmap(decodeFile);
                                            }
                                        } catch (Throwable th5) {
                                            th = th5;
                                            bitmap = decodeFile;
                                            FileLog.e(th);
                                            return bitmap;
                                        }
                                    }
                                    bitmap = decodeFile;
                                } catch (Throwable th6) {
                                    th = th6;
                                }
                            }
                            if (bitmap == null) {
                                return bitmap;
                            }
                            createBitmap = Bitmaps.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                            if (createBitmap == bitmap) {
                                return bitmap;
                            }
                            bitmap.recycle();
                        }
                        if (bitmap != null) {
                            if (options.inPurgeable) {
                                Utilities.pinBitmap(bitmap);
                            }
                            createBitmap = Bitmaps.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                            if (createBitmap != bitmap) {
                                bitmap.recycle();
                                return createBitmap;
                            }
                            return bitmap;
                        }
                        return bitmap;
                    }
                    matrix = null;
                    f3 = max / options.inSampleSize;
                    if (f3 > 1.0f) {
                    }
                    if (str2 != null) {
                    }
                } catch (Throwable th7) {
                    FileLog.e(th7);
                    return null;
                }
            }
            openInputStream = null;
            float f42 = options.outWidth;
            float f52 = options.outHeight;
            if (z) {
            }
            if (max < 1.0f) {
            }
            options.inJustDecodeBounds = false;
            i = (int) max;
            options.inSampleSize = i;
            if (i % 2 != 0) {
            }
            options.inPurgeable = Build.VERSION.SDK_INT < 21;
            imageOrientation = AndroidUtilities.getImageOrientation(str2);
            if (((Integer) imageOrientation.first).intValue() == 0) {
                inputStream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                imageOrientation = AndroidUtilities.getImageOrientation(inputStream);
            }
            if (((Integer) imageOrientation.first).intValue() == 0) {
            }
            matrix = new Matrix();
            if (((Integer) imageOrientation.second).intValue() != 0) {
            }
            if (((Integer) imageOrientation.first).intValue() != 0) {
            }
            f3 = max / options.inSampleSize;
            if (f3 > 1.0f) {
            }
            if (str2 != null) {
            }
        }
        str2 = str;
        Bitmap bitmap2 = null;
        if (str2 == null) {
        }
        openInputStream = null;
        float f422 = options.outWidth;
        float f522 = options.outHeight;
        if (z) {
        }
        if (max < 1.0f) {
        }
        options.inJustDecodeBounds = false;
        i = (int) max;
        options.inSampleSize = i;
        if (i % 2 != 0) {
        }
        options.inPurgeable = Build.VERSION.SDK_INT < 21;
        imageOrientation = AndroidUtilities.getImageOrientation(str2);
        if (((Integer) imageOrientation.first).intValue() == 0) {
        }
        if (((Integer) imageOrientation.first).intValue() == 0) {
        }
        matrix = new Matrix();
        if (((Integer) imageOrientation.second).intValue() != 0) {
        }
        if (((Integer) imageOrientation.first).intValue() != 0) {
        }
        f3 = max / options.inSampleSize;
        if (f3 > 1.0f) {
        }
        if (str2 != null) {
        }
    }

    public static void fillPhotoSizeWithBytes(TLRPC$PhotoSize tLRPC$PhotoSize) {
        if (tLRPC$PhotoSize != null) {
            byte[] bArr = tLRPC$PhotoSize.bytes;
            if (bArr == null || bArr.length == 0) {
                try {
                    RandomAccessFile randomAccessFile = new RandomAccessFile(FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(tLRPC$PhotoSize, true), "r");
                    if (((int) randomAccessFile.length()) < 20000) {
                        int length = (int) randomAccessFile.length();
                        byte[] bArr2 = new byte[length];
                        tLRPC$PhotoSize.bytes = bArr2;
                        randomAccessFile.readFully(bArr2, 0, length);
                    }
                } catch (Throwable th) {
                    FileLog.e(th);
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x00b0  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00b5  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00d3  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00e3  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00f9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static TLRPC$PhotoSize scaleAndSaveImageInternal(TLRPC$PhotoSize tLRPC$PhotoSize, Bitmap bitmap, Bitmap.CompressFormat compressFormat, boolean z, int i, int i2, float f, float f2, float f3, int i3, boolean z2, boolean z3, boolean z4) throws Exception {
        TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated;
        File directory;
        Bitmap createScaledBitmap = (f3 > 1.0f || z3) ? Bitmaps.createScaledBitmap(bitmap, i, i2, true) : bitmap;
        if (tLRPC$PhotoSize != null) {
            TLRPC$FileLocation tLRPC$FileLocation = tLRPC$PhotoSize.location;
            if (tLRPC$FileLocation instanceof TLRPC$TL_fileLocationToBeDeprecated) {
                tLRPC$TL_fileLocationToBeDeprecated = (TLRPC$TL_fileLocationToBeDeprecated) tLRPC$FileLocation;
                int i4 = 7.$SwitchMap$android$graphics$Bitmap$CompressFormat[compressFormat.ordinal()];
                String str = tLRPC$TL_fileLocationToBeDeprecated.volume_id + "_" + tLRPC$TL_fileLocationToBeDeprecated.local_id + ((i4 != 1 || i4 == 2 || i4 == 3) ? ".webp" : ".jpg");
                if (!z4) {
                    directory = FileLoader.getDirectory(4);
                } else {
                    directory = tLRPC$TL_fileLocationToBeDeprecated.volume_id != -2147483648L ? FileLoader.getDirectory(0) : FileLoader.getDirectory(4);
                }
                FileOutputStream fileOutputStream = new FileOutputStream(new File(directory, str));
                createScaledBitmap.compress(compressFormat, i3, fileOutputStream);
                if (!z2) {
                    tLRPC$PhotoSize.size = (int) fileOutputStream.getChannel().size();
                }
                fileOutputStream.close();
                if (z2) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    createScaledBitmap.compress(compressFormat, i3, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    tLRPC$PhotoSize.bytes = byteArray;
                    tLRPC$PhotoSize.size = byteArray.length;
                    byteArrayOutputStream.close();
                }
                if (createScaledBitmap != bitmap) {
                    createScaledBitmap.recycle();
                }
                return tLRPC$PhotoSize;
            }
        }
        tLRPC$TL_fileLocationToBeDeprecated = new TLRPC$TL_fileLocationToBeDeprecated();
        tLRPC$TL_fileLocationToBeDeprecated.volume_id = -2147483648L;
        tLRPC$TL_fileLocationToBeDeprecated.dc_id = Integer.MIN_VALUE;
        tLRPC$TL_fileLocationToBeDeprecated.local_id = SharedConfig.getLastLocalId();
        tLRPC$TL_fileLocationToBeDeprecated.file_reference = new byte[0];
        tLRPC$PhotoSize = new TLRPC$TL_photoSize_layer127();
        tLRPC$PhotoSize.location = tLRPC$TL_fileLocationToBeDeprecated;
        tLRPC$PhotoSize.w = createScaledBitmap.getWidth();
        int height = createScaledBitmap.getHeight();
        tLRPC$PhotoSize.h = height;
        int i5 = tLRPC$PhotoSize.w;
        if (i5 <= 100 && height <= 100) {
            tLRPC$PhotoSize.type = "s";
        } else if (i5 <= 320 && height <= 320) {
            tLRPC$PhotoSize.type = "m";
        } else if (i5 <= 800 && height <= 800) {
            tLRPC$PhotoSize.type = "x";
        } else if (i5 <= 1280 && height <= 1280) {
            tLRPC$PhotoSize.type = "y";
        } else {
            tLRPC$PhotoSize.type = "w";
        }
        int i42 = 7.$SwitchMap$android$graphics$Bitmap$CompressFormat[compressFormat.ordinal()];
        if (i42 != 1) {
        }
        String str2 = tLRPC$TL_fileLocationToBeDeprecated.volume_id + "_" + tLRPC$TL_fileLocationToBeDeprecated.local_id + ((i42 != 1 || i42 == 2 || i42 == 3) ? ".webp" : ".jpg");
        if (!z4) {
        }
        FileOutputStream fileOutputStream2 = new FileOutputStream(new File(directory, str2));
        createScaledBitmap.compress(compressFormat, i3, fileOutputStream2);
        if (!z2) {
        }
        fileOutputStream2.close();
        if (z2) {
        }
        if (createScaledBitmap != bitmap) {
        }
        return tLRPC$PhotoSize;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static /* synthetic */ class 7 {
        static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$CompressFormat;

        static {
            Bitmap.CompressFormat compressFormat;
            Bitmap.CompressFormat compressFormat2;
            int[] iArr = new int[Bitmap.CompressFormat.values().length];
            $SwitchMap$android$graphics$Bitmap$CompressFormat = iArr;
            try {
                iArr[Bitmap.CompressFormat.WEBP.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                int[] iArr2 = $SwitchMap$android$graphics$Bitmap$CompressFormat;
                compressFormat2 = Bitmap.CompressFormat.WEBP_LOSSY;
                iArr2[compressFormat2.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                int[] iArr3 = $SwitchMap$android$graphics$Bitmap$CompressFormat;
                compressFormat = Bitmap.CompressFormat.WEBP_LOSSLESS;
                iArr3[compressFormat.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public static TLRPC$PhotoSize scaleAndSaveImage(Bitmap bitmap, float f, float f2, int i, boolean z) {
        return scaleAndSaveImage(null, bitmap, Bitmap.CompressFormat.JPEG, false, f, f2, i, z, 0, 0, false);
    }

    public static TLRPC$PhotoSize scaleAndSaveImage(TLRPC$PhotoSize tLRPC$PhotoSize, Bitmap bitmap, float f, float f2, int i, boolean z, boolean z2) {
        return scaleAndSaveImage(tLRPC$PhotoSize, bitmap, Bitmap.CompressFormat.JPEG, false, f, f2, i, z, 0, 0, z2);
    }

    public static TLRPC$PhotoSize scaleAndSaveImage(Bitmap bitmap, float f, float f2, int i, boolean z, int i2, int i3) {
        return scaleAndSaveImage(null, bitmap, Bitmap.CompressFormat.JPEG, false, f, f2, i, z, i2, i3, false);
    }

    public static TLRPC$PhotoSize scaleAndSaveImage(Bitmap bitmap, float f, float f2, boolean z, int i, boolean z2, int i2, int i3) {
        return scaleAndSaveImage(null, bitmap, Bitmap.CompressFormat.JPEG, z, f, f2, i, z2, i2, i3, false);
    }

    public static TLRPC$PhotoSize scaleAndSaveImage(Bitmap bitmap, Bitmap.CompressFormat compressFormat, float f, float f2, int i, boolean z, int i2, int i3) {
        return scaleAndSaveImage(null, bitmap, compressFormat, false, f, f2, i, z, i2, i3, false);
    }

    public static TLRPC$PhotoSize scaleAndSaveImage(TLRPC$PhotoSize tLRPC$PhotoSize, Bitmap bitmap, Bitmap.CompressFormat compressFormat, boolean z, float f, float f2, int i, boolean z2, int i2, int i3, boolean z3) {
        float f3;
        boolean z4;
        int i4;
        int i5;
        float max;
        if (bitmap == null) {
            return null;
        }
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        if (width != 0.0f && height != 0.0f) {
            float max2 = Math.max(width / f, height / f2);
            if (i2 != 0 && i3 != 0) {
                float f4 = i2;
                if (width < f4 || height < i3) {
                    if (width >= f4 || height <= i3) {
                        if (width > f4) {
                            float f5 = i3;
                            if (height < f5) {
                                max = height / f5;
                            }
                        }
                        max = Math.max(width / f4, height / i3);
                    } else {
                        max = width / f4;
                    }
                    f3 = max;
                    z4 = true;
                    i4 = (int) (width / f3);
                    i5 = (int) (height / f3);
                    if (i5 != 0 && i4 != 0) {
                        try {
                            return scaleAndSaveImageInternal(tLRPC$PhotoSize, bitmap, compressFormat, z, i4, i5, width, height, f3, i, z2, z4, z3);
                        } catch (Throwable th) {
                            FileLog.e(th);
                            getInstance().clearMemory();
                            System.gc();
                            try {
                                return scaleAndSaveImageInternal(tLRPC$PhotoSize, bitmap, compressFormat, z, i4, i5, width, height, f3, i, z2, z4, z3);
                            } catch (Throwable th2) {
                                FileLog.e(th2);
                            }
                        }
                    }
                }
            }
            f3 = max2;
            z4 = false;
            i4 = (int) (width / f3);
            i5 = (int) (height / f3);
            if (i5 != 0) {
                return scaleAndSaveImageInternal(tLRPC$PhotoSize, bitmap, compressFormat, z, i4, i5, width, height, f3, i, z2, z4, z3);
            }
        }
        return null;
    }

    public static String getHttpUrlExtension(String str, String str2) {
        String lastPathSegment = Uri.parse(str).getLastPathSegment();
        if (!TextUtils.isEmpty(lastPathSegment) && lastPathSegment.length() > 1) {
            str = lastPathSegment;
        }
        int lastIndexOf = str.lastIndexOf(46);
        String substring = lastIndexOf != -1 ? str.substring(lastIndexOf + 1) : null;
        return (substring == null || substring.length() == 0 || substring.length() > 4) ? str2 : substring;
    }

    public static void saveMessageThumbs(TLRPC$Message tLRPC$Message) {
        byte[] bArr;
        TLRPC$PhotoSize tLRPC$TL_photoSize_layer127;
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
        if (tLRPC$MessageMedia == null) {
            return;
        }
        int i = 0;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPaidMedia) {
            TLRPC$TL_messageMediaPaidMedia tLRPC$TL_messageMediaPaidMedia = (TLRPC$TL_messageMediaPaidMedia) tLRPC$MessageMedia;
            while (i < tLRPC$TL_messageMediaPaidMedia.extended_media.size()) {
                TLRPC$MessageExtendedMedia tLRPC$MessageExtendedMedia = tLRPC$TL_messageMediaPaidMedia.extended_media.get(i);
                if (tLRPC$MessageExtendedMedia instanceof TLRPC$TL_messageExtendedMedia) {
                    saveMessageThumbs(tLRPC$Message, ((TLRPC$TL_messageExtendedMedia) tLRPC$MessageExtendedMedia).media);
                }
                i++;
            }
            return;
        }
        TLRPC$PhotoSize findPhotoCachedSize = findPhotoCachedSize(tLRPC$Message);
        if (findPhotoCachedSize == null || (bArr = findPhotoCachedSize.bytes) == null || bArr.length == 0) {
            return;
        }
        TLRPC$FileLocation tLRPC$FileLocation = findPhotoCachedSize.location;
        if (tLRPC$FileLocation == null || (tLRPC$FileLocation instanceof TLRPC$TL_fileLocationUnavailable)) {
            TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated = new TLRPC$TL_fileLocationToBeDeprecated();
            findPhotoCachedSize.location = tLRPC$TL_fileLocationToBeDeprecated;
            tLRPC$TL_fileLocationToBeDeprecated.volume_id = -2147483648L;
            tLRPC$TL_fileLocationToBeDeprecated.local_id = SharedConfig.getLastLocalId();
        }
        if (findPhotoCachedSize.h <= 50 && findPhotoCachedSize.w <= 50) {
            tLRPC$TL_photoSize_layer127 = new TLRPC$TL_photoStrippedSize();
            tLRPC$TL_photoSize_layer127.location = findPhotoCachedSize.location;
            tLRPC$TL_photoSize_layer127.bytes = findPhotoCachedSize.bytes;
            tLRPC$TL_photoSize_layer127.h = findPhotoCachedSize.h;
            tLRPC$TL_photoSize_layer127.w = findPhotoCachedSize.w;
        } else {
            boolean z = true;
            File pathToAttach = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(findPhotoCachedSize, true);
            if (MessageObject.shouldEncryptPhotoOrVideo(UserConfig.selectedAccount, tLRPC$Message)) {
                pathToAttach = new File(pathToAttach.getAbsolutePath() + ".enc");
            } else {
                z = false;
            }
            if (!pathToAttach.exists()) {
                if (z) {
                    try {
                        File internalCacheDir = FileLoader.getInternalCacheDir();
                        RandomAccessFile randomAccessFile = new RandomAccessFile(new File(internalCacheDir, pathToAttach.getName() + ".key"), "rws");
                        long length = randomAccessFile.length();
                        byte[] bArr2 = new byte[32];
                        byte[] bArr3 = new byte[16];
                        if (length > 0 && length % 48 == 0) {
                            randomAccessFile.read(bArr2, 0, 32);
                            randomAccessFile.read(bArr3, 0, 16);
                        } else {
                            Utilities.random.nextBytes(bArr2);
                            Utilities.random.nextBytes(bArr3);
                            randomAccessFile.write(bArr2);
                            randomAccessFile.write(bArr3);
                        }
                        randomAccessFile.close();
                        byte[] bArr4 = findPhotoCachedSize.bytes;
                        Utilities.aesCtrDecryptionByteArray(bArr4, bArr2, bArr3, 0, bArr4.length, 0);
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
                RandomAccessFile randomAccessFile2 = new RandomAccessFile(pathToAttach, "rws");
                randomAccessFile2.write(findPhotoCachedSize.bytes);
                randomAccessFile2.close();
            }
            tLRPC$TL_photoSize_layer127 = new TLRPC$TL_photoSize_layer127();
            tLRPC$TL_photoSize_layer127.w = findPhotoCachedSize.w;
            tLRPC$TL_photoSize_layer127.h = findPhotoCachedSize.h;
            tLRPC$TL_photoSize_layer127.location = findPhotoCachedSize.location;
            tLRPC$TL_photoSize_layer127.size = findPhotoCachedSize.size;
            tLRPC$TL_photoSize_layer127.type = findPhotoCachedSize.type;
        }
        TLRPC$MessageMedia tLRPC$MessageMedia2 = tLRPC$Message.media;
        if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPhoto) {
            int size = tLRPC$MessageMedia2.photo.sizes.size();
            while (i < size) {
                if (tLRPC$Message.media.photo.sizes.get(i) instanceof TLRPC$TL_photoCachedSize) {
                    tLRPC$Message.media.photo.sizes.set(i, tLRPC$TL_photoSize_layer127);
                    return;
                }
                i++;
            }
        } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaDocument) {
            int size2 = tLRPC$MessageMedia2.document.thumbs.size();
            while (i < size2) {
                if (tLRPC$Message.media.document.thumbs.get(i) instanceof TLRPC$TL_photoCachedSize) {
                    tLRPC$Message.media.document.thumbs.set(i, tLRPC$TL_photoSize_layer127);
                    return;
                }
                i++;
            }
        } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaWebPage) {
            int size3 = tLRPC$MessageMedia2.webpage.photo.sizes.size();
            while (i < size3) {
                if (tLRPC$Message.media.webpage.photo.sizes.get(i) instanceof TLRPC$TL_photoCachedSize) {
                    tLRPC$Message.media.webpage.photo.sizes.set(i, tLRPC$TL_photoSize_layer127);
                    return;
                }
                i++;
            }
        }
    }

    public static void saveMessageThumbs(TLRPC$Message tLRPC$Message, TLRPC$MessageMedia tLRPC$MessageMedia) {
        TLRPC$PhotoSize findPhotoCachedSize;
        byte[] bArr;
        TLRPC$PhotoSize tLRPC$TL_photoSize_layer127;
        if (tLRPC$Message == null || tLRPC$MessageMedia == null || (findPhotoCachedSize = findPhotoCachedSize(tLRPC$MessageMedia)) == null || (bArr = findPhotoCachedSize.bytes) == null || bArr.length == 0) {
            return;
        }
        TLRPC$FileLocation tLRPC$FileLocation = findPhotoCachedSize.location;
        if (tLRPC$FileLocation == null || (tLRPC$FileLocation instanceof TLRPC$TL_fileLocationUnavailable)) {
            TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated = new TLRPC$TL_fileLocationToBeDeprecated();
            findPhotoCachedSize.location = tLRPC$TL_fileLocationToBeDeprecated;
            tLRPC$TL_fileLocationToBeDeprecated.volume_id = -2147483648L;
            tLRPC$TL_fileLocationToBeDeprecated.local_id = SharedConfig.getLastLocalId();
        }
        int i = 0;
        if (findPhotoCachedSize.h <= 50 && findPhotoCachedSize.w <= 50) {
            tLRPC$TL_photoSize_layer127 = new TLRPC$TL_photoStrippedSize();
            tLRPC$TL_photoSize_layer127.location = findPhotoCachedSize.location;
            tLRPC$TL_photoSize_layer127.bytes = findPhotoCachedSize.bytes;
            tLRPC$TL_photoSize_layer127.h = findPhotoCachedSize.h;
            tLRPC$TL_photoSize_layer127.w = findPhotoCachedSize.w;
        } else {
            boolean z = true;
            File pathToAttach = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(findPhotoCachedSize, true);
            if (MessageObject.shouldEncryptPhotoOrVideo(UserConfig.selectedAccount, tLRPC$Message)) {
                pathToAttach = new File(pathToAttach.getAbsolutePath() + ".enc");
            } else {
                z = false;
            }
            if (!pathToAttach.exists()) {
                if (z) {
                    try {
                        File internalCacheDir = FileLoader.getInternalCacheDir();
                        RandomAccessFile randomAccessFile = new RandomAccessFile(new File(internalCacheDir, pathToAttach.getName() + ".key"), "rws");
                        long length = randomAccessFile.length();
                        byte[] bArr2 = new byte[32];
                        byte[] bArr3 = new byte[16];
                        if (length > 0 && length % 48 == 0) {
                            randomAccessFile.read(bArr2, 0, 32);
                            randomAccessFile.read(bArr3, 0, 16);
                        } else {
                            Utilities.random.nextBytes(bArr2);
                            Utilities.random.nextBytes(bArr3);
                            randomAccessFile.write(bArr2);
                            randomAccessFile.write(bArr3);
                        }
                        randomAccessFile.close();
                        byte[] bArr4 = findPhotoCachedSize.bytes;
                        Utilities.aesCtrDecryptionByteArray(bArr4, bArr2, bArr3, 0, bArr4.length, 0);
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
                RandomAccessFile randomAccessFile2 = new RandomAccessFile(pathToAttach, "rws");
                randomAccessFile2.write(findPhotoCachedSize.bytes);
                randomAccessFile2.close();
            }
            tLRPC$TL_photoSize_layer127 = new TLRPC$TL_photoSize_layer127();
            tLRPC$TL_photoSize_layer127.w = findPhotoCachedSize.w;
            tLRPC$TL_photoSize_layer127.h = findPhotoCachedSize.h;
            tLRPC$TL_photoSize_layer127.location = findPhotoCachedSize.location;
            tLRPC$TL_photoSize_layer127.size = findPhotoCachedSize.size;
            tLRPC$TL_photoSize_layer127.type = findPhotoCachedSize.type;
        }
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) {
            int size = tLRPC$MessageMedia.photo.sizes.size();
            while (i < size) {
                if (tLRPC$MessageMedia.photo.sizes.get(i) instanceof TLRPC$TL_photoCachedSize) {
                    tLRPC$MessageMedia.photo.sizes.set(i, tLRPC$TL_photoSize_layer127);
                    return;
                }
                i++;
            }
        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
            int size2 = tLRPC$MessageMedia.document.thumbs.size();
            while (i < size2) {
                if (tLRPC$MessageMedia.document.thumbs.get(i) instanceof TLRPC$TL_photoCachedSize) {
                    tLRPC$MessageMedia.document.thumbs.set(i, tLRPC$TL_photoSize_layer127);
                    return;
                }
                i++;
            }
        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) {
            int size3 = tLRPC$MessageMedia.webpage.photo.sizes.size();
            while (i < size3) {
                if (tLRPC$MessageMedia.webpage.photo.sizes.get(i) instanceof TLRPC$TL_photoCachedSize) {
                    tLRPC$MessageMedia.webpage.photo.sizes.set(i, tLRPC$TL_photoSize_layer127);
                    return;
                }
                i++;
            }
        }
    }

    private static TLRPC$PhotoSize findPhotoCachedSize(TLRPC$Message tLRPC$Message) {
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
        int i = 0;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) {
            int size = tLRPC$MessageMedia.photo.sizes.size();
            while (i < size) {
                TLRPC$PhotoSize tLRPC$PhotoSize = tLRPC$Message.media.photo.sizes.get(i);
                if (tLRPC$PhotoSize instanceof TLRPC$TL_photoCachedSize) {
                    return tLRPC$PhotoSize;
                }
                i++;
            }
        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
            TLRPC$Document tLRPC$Document = tLRPC$MessageMedia.document;
            if (tLRPC$Document != null) {
                int size2 = tLRPC$Document.thumbs.size();
                while (i < size2) {
                    TLRPC$PhotoSize tLRPC$PhotoSize2 = tLRPC$Message.media.document.thumbs.get(i);
                    if (tLRPC$PhotoSize2 instanceof TLRPC$TL_photoCachedSize) {
                        return tLRPC$PhotoSize2;
                    }
                    i++;
                }
            }
        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) {
            TLRPC$Photo tLRPC$Photo = tLRPC$MessageMedia.webpage.photo;
            if (tLRPC$Photo != null) {
                int size3 = tLRPC$Photo.sizes.size();
                while (i < size3) {
                    TLRPC$PhotoSize tLRPC$PhotoSize3 = tLRPC$Message.media.webpage.photo.sizes.get(i);
                    if (tLRPC$PhotoSize3 instanceof TLRPC$TL_photoCachedSize) {
                        return tLRPC$PhotoSize3;
                    }
                    i++;
                }
            }
        } else if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaInvoice) && !tLRPC$MessageMedia.extended_media.isEmpty() && (tLRPC$Message.media.extended_media.get(0) instanceof TLRPC$TL_messageExtendedMediaPreview)) {
            return ((TLRPC$TL_messageExtendedMediaPreview) tLRPC$Message.media.extended_media.get(0)).thumb;
        }
        return null;
    }

    private static TLRPC$PhotoSize findPhotoCachedSize(TLRPC$MessageMedia tLRPC$MessageMedia) {
        int i = 0;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) {
            int size = tLRPC$MessageMedia.photo.sizes.size();
            while (i < size) {
                TLRPC$PhotoSize tLRPC$PhotoSize = tLRPC$MessageMedia.photo.sizes.get(i);
                if (tLRPC$PhotoSize instanceof TLRPC$TL_photoCachedSize) {
                    return tLRPC$PhotoSize;
                }
                i++;
            }
        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
            TLRPC$Document tLRPC$Document = tLRPC$MessageMedia.document;
            if (tLRPC$Document != null) {
                int size2 = tLRPC$Document.thumbs.size();
                while (i < size2) {
                    TLRPC$PhotoSize tLRPC$PhotoSize2 = tLRPC$MessageMedia.document.thumbs.get(i);
                    if (tLRPC$PhotoSize2 instanceof TLRPC$TL_photoCachedSize) {
                        return tLRPC$PhotoSize2;
                    }
                    i++;
                }
            }
        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) {
            TLRPC$Photo tLRPC$Photo = tLRPC$MessageMedia.webpage.photo;
            if (tLRPC$Photo != null) {
                int size3 = tLRPC$Photo.sizes.size();
                while (i < size3) {
                    TLRPC$PhotoSize tLRPC$PhotoSize3 = tLRPC$MessageMedia.webpage.photo.sizes.get(i);
                    if (tLRPC$PhotoSize3 instanceof TLRPC$TL_photoCachedSize) {
                        return tLRPC$PhotoSize3;
                    }
                    i++;
                }
            }
        } else if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaInvoice) && !tLRPC$MessageMedia.extended_media.isEmpty() && (tLRPC$MessageMedia.extended_media.get(0) instanceof TLRPC$TL_messageExtendedMediaPreview)) {
            return ((TLRPC$TL_messageExtendedMediaPreview) tLRPC$MessageMedia.extended_media.get(0)).thumb;
        }
        return null;
    }

    public static void saveMessagesThumbs(ArrayList<TLRPC$Message> arrayList) {
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            saveMessageThumbs(arrayList.get(i));
        }
    }

    public static MessageThumb generateMessageThumb(TLRPC$Message tLRPC$Message) {
        int i;
        int i2;
        Bitmap strippedPhotoBitmap;
        byte[] bArr;
        TLRPC$PhotoSize findPhotoCachedSize = findPhotoCachedSize(tLRPC$Message);
        if (findPhotoCachedSize != null && (bArr = findPhotoCachedSize.bytes) != null && bArr.length != 0) {
            File pathToAttach = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(findPhotoCachedSize, true);
            TLRPC$TL_photoSize_layer127 tLRPC$TL_photoSize_layer127 = new TLRPC$TL_photoSize_layer127();
            tLRPC$TL_photoSize_layer127.w = findPhotoCachedSize.w;
            tLRPC$TL_photoSize_layer127.h = findPhotoCachedSize.h;
            tLRPC$TL_photoSize_layer127.location = findPhotoCachedSize.location;
            tLRPC$TL_photoSize_layer127.size = findPhotoCachedSize.size;
            tLRPC$TL_photoSize_layer127.type = findPhotoCachedSize.type;
            if (pathToAttach.exists() && tLRPC$Message.grouped_id == 0) {
                org.telegram.ui.Components.Point messageSize = ChatMessageCell.getMessageSize(findPhotoCachedSize.w, findPhotoCachedSize.h);
                String format = String.format(Locale.US, "%d_%d@%d_%d_b", Long.valueOf(findPhotoCachedSize.location.volume_id), Integer.valueOf(findPhotoCachedSize.location.local_id), Integer.valueOf((int) (messageSize.x / AndroidUtilities.density)), Integer.valueOf((int) (messageSize.y / AndroidUtilities.density)));
                if (!getInstance().isInMemCache(format, false)) {
                    String path = pathToAttach.getPath();
                    float f = messageSize.x;
                    float f2 = AndroidUtilities.density;
                    Bitmap loadBitmap = loadBitmap(path, null, (int) (f / f2), (int) (messageSize.y / f2), false);
                    if (loadBitmap != null) {
                        Utilities.blurBitmap(loadBitmap, 3, 1, loadBitmap.getWidth(), loadBitmap.getHeight(), loadBitmap.getRowBytes());
                        float f3 = messageSize.x;
                        float f4 = AndroidUtilities.density;
                        Bitmap createScaledBitmap = Bitmaps.createScaledBitmap(loadBitmap, (int) (f3 / f4), (int) (messageSize.y / f4), true);
                        if (createScaledBitmap != loadBitmap) {
                            loadBitmap.recycle();
                            loadBitmap = createScaledBitmap;
                        }
                        return new MessageThumb(format, new BitmapDrawable(loadBitmap));
                    }
                }
            }
        } else {
            TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
            if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
                int size = tLRPC$MessageMedia.document.thumbs.size();
                for (int i3 = 0; i3 < size; i3++) {
                    TLRPC$PhotoSize tLRPC$PhotoSize = tLRPC$Message.media.document.thumbs.get(i3);
                    if (tLRPC$PhotoSize instanceof TLRPC$TL_photoStrippedSize) {
                        TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Message.media.document.thumbs, 320);
                        if (closestPhotoSizeWithSize == null) {
                            int i4 = 0;
                            while (true) {
                                if (i4 >= tLRPC$Message.media.document.attributes.size()) {
                                    i = 0;
                                    i2 = 0;
                                    break;
                                } else if (tLRPC$Message.media.document.attributes.get(i4) instanceof TLRPC$TL_documentAttributeVideo) {
                                    TLRPC$TL_documentAttributeVideo tLRPC$TL_documentAttributeVideo = (TLRPC$TL_documentAttributeVideo) tLRPC$Message.media.document.attributes.get(i4);
                                    i2 = tLRPC$TL_documentAttributeVideo.h;
                                    i = tLRPC$TL_documentAttributeVideo.w;
                                    break;
                                } else {
                                    i4++;
                                }
                            }
                        } else {
                            i2 = closestPhotoSizeWithSize.h;
                            i = closestPhotoSizeWithSize.w;
                        }
                        org.telegram.ui.Components.Point messageSize2 = ChatMessageCell.getMessageSize(i, i2);
                        String format2 = String.format(Locale.US, "%s_false@%d_%d_b", ImageLocation.getStrippedKey(tLRPC$Message, tLRPC$Message, tLRPC$PhotoSize), Integer.valueOf((int) (messageSize2.x / AndroidUtilities.density)), Integer.valueOf((int) (messageSize2.y / AndroidUtilities.density)));
                        if (!getInstance().isInMemCache(format2, false) && (strippedPhotoBitmap = getStrippedPhotoBitmap(tLRPC$PhotoSize.bytes, null)) != null) {
                            Utilities.blurBitmap(strippedPhotoBitmap, 3, 1, strippedPhotoBitmap.getWidth(), strippedPhotoBitmap.getHeight(), strippedPhotoBitmap.getRowBytes());
                            float f5 = messageSize2.x;
                            float f6 = AndroidUtilities.density;
                            Bitmap createScaledBitmap2 = Bitmaps.createScaledBitmap(strippedPhotoBitmap, (int) (f5 / f6), (int) (messageSize2.y / f6), true);
                            if (createScaledBitmap2 != strippedPhotoBitmap) {
                                strippedPhotoBitmap.recycle();
                                strippedPhotoBitmap = createScaledBitmap2;
                            }
                            return new MessageThumb(format2, new BitmapDrawable(strippedPhotoBitmap));
                        }
                    }
                }
            }
        }
        return null;
    }

    public void onFragmentStackChanged() {
        for (int i = 0; i < this.cachedAnimatedFileDrawables.size(); i++) {
            this.cachedAnimatedFileDrawables.get(i).repeatCount = 0;
        }
    }

    public DispatchQueuePriority getCacheOutQueue() {
        return this.cacheOutQueue;
    }

    /* loaded from: classes3.dex */
    public static class MessageThumb {
        BitmapDrawable drawable;
        String key;

        public MessageThumb(String str, BitmapDrawable bitmapDrawable) {
            this.key = str;
            this.drawable = bitmapDrawable;
        }
    }
}
