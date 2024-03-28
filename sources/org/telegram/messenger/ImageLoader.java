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
import j$.wrappers.$r8$wrapper$java$util$stream$Stream$-V-WRP;
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
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import org.telegram.tgnet.TLRPC$TL_document;
import org.telegram.tgnet.TLRPC$TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC$TL_documentEncrypted;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_fileLocationToBeDeprecated;
import org.telegram.tgnet.TLRPC$TL_fileLocationUnavailable;
import org.telegram.tgnet.TLRPC$TL_messageExtendedMediaPreview;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC$TL_messageMediaInvoice;
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
    private HashMap<String, CacheImage> imageLoadingByKeys = new HashMap<>();
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
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpFileTask$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpFileTask.this.lambda$reportProgress$1(j, j2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$reportProgress$1(final long j, final long j2) {
            ImageLoader.this.fileProgresses.put(this.url, new long[]{j, j2});
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpFileTask$$ExternalSyntheticLambda1
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
        /* JADX WARN: Code restructure failed: missing block: B:81:0x0120, code lost:
            if (r5 != (-1)) goto L53;
         */
        /* JADX WARN: Code restructure failed: missing block: B:82:0x0122, code lost:
            r0 = r11.fileSize;
         */
        /* JADX WARN: Code restructure failed: missing block: B:83:0x0124, code lost:
            if (r0 == 0) goto L54;
         */
        /* JADX WARN: Code restructure failed: missing block: B:84:0x0126, code lost:
            reportProgress(r0, r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:86:0x012c, code lost:
            r0 = e;
         */
        /* JADX WARN: Code restructure failed: missing block: B:88:0x012e, code lost:
            r1 = false;
         */
        /* JADX WARN: Code restructure failed: missing block: B:91:0x0132, code lost:
            org.telegram.messenger.FileLog.e(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:93:0x0136, code lost:
            r0 = th;
         */
        /* JADX WARN: Code restructure failed: missing block: B:97:0x013a, code lost:
            org.telegram.messenger.FileLog.e(r0);
         */
        /* JADX WARN: Removed duplicated region for block: B:101:0x0142 A[Catch: all -> 0x0148, TRY_LEAVE, TryCatch #5 {all -> 0x0148, blocks: (B:99:0x013e, B:101:0x0142), top: B:121:0x013e }] */
        /* JADX WARN: Removed duplicated region for block: B:114:0x014e A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:125:0x00ad A[EXC_TOP_SPLITTER, SYNTHETIC] */
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
                    try {
                        this.fileOutputStream = new RandomAccessFile(this.tempFile, "rws");
                    } catch (Throwable th) {
                        inputStream = inputStream2;
                        th = th;
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
                } catch (Throwable th2) {
                    th = th2;
                    inputStream = null;
                }
            } catch (Throwable th3) {
                th = th3;
                inputStream = null;
                httpURLConnection = null;
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
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        z = false;
                    }
                    z2 = z;
                }
                try {
                    RandomAccessFile randomAccessFile = this.fileOutputStream;
                    if (randomAccessFile != null) {
                        randomAccessFile.close();
                        this.fileOutputStream = null;
                    }
                } catch (Throwable th5) {
                    FileLog.e(th5);
                }
                if (inputStream2 != null) {
                    try {
                        inputStream2.close();
                    } catch (Throwable th6) {
                        FileLog.e(th6);
                    }
                }
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
        @Override // android.os.AsyncTask
        public String doInBackground(Void... voidArr) {
            InputStream inputStream;
            ByteArrayOutputStream byteArrayOutputStream;
            int read;
            int responseCode;
            try {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(this.cacheImage.imageLocation.path.replace("athumb://", "https://")).openConnection();
                    this.httpConnection = httpURLConnection;
                    httpURLConnection.setConnectTimeout(5000);
                    this.httpConnection.setReadTimeout(5000);
                    this.httpConnection.connect();
                    try {
                        HttpURLConnection httpURLConnection2 = this.httpConnection;
                        if (httpURLConnection2 != null && (responseCode = httpURLConnection2.getResponseCode()) != 200 && responseCode != 202 && responseCode != 304) {
                            this.canRetry = false;
                        }
                    } catch (Exception e) {
                        FileLog.e((Throwable) e, false);
                    }
                    InputStream inputStream2 = this.httpConnection.getInputStream();
                    try {
                        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                        try {
                            byte[] bArr = new byte[LiteMode.FLAG_CHAT_SCALE];
                            while (!isCancelled() && (read = inputStream2.read(bArr)) > 0) {
                                byteArrayOutputStream2.write(bArr, 0, read);
                            }
                            this.canRetry = false;
                            JSONArray jSONArray = new JSONObject(new String(byteArrayOutputStream2.toByteArray())).getJSONArray("results");
                            if (jSONArray.length() <= 0) {
                                try {
                                    HttpURLConnection httpURLConnection3 = this.httpConnection;
                                    if (httpURLConnection3 != null) {
                                        httpURLConnection3.disconnect();
                                    }
                                } catch (Throwable unused) {
                                }
                                if (inputStream2 != null) {
                                    try {
                                        inputStream2.close();
                                    } catch (Throwable th) {
                                        FileLog.e(th);
                                    }
                                }
                                byteArrayOutputStream2.close();
                            }
                            String string = jSONArray.getJSONObject(0).getString("artworkUrl100");
                            if (this.small) {
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
                                    } catch (Throwable th2) {
                                        FileLog.e(th2);
                                    }
                                }
                                try {
                                    byteArrayOutputStream2.close();
                                } catch (Exception unused3) {
                                }
                                return string;
                            }
                            String replace = string.replace("100x100", "600x600");
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
                                } catch (Throwable th3) {
                                    FileLog.e(th3);
                                }
                            }
                            try {
                                byteArrayOutputStream2.close();
                            } catch (Exception unused5) {
                            }
                            return replace;
                        } catch (Throwable th4) {
                            inputStream = inputStream2;
                            th = th4;
                            byteArrayOutputStream = byteArrayOutputStream2;
                            try {
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
                                FileLog.e(th, false);
                                try {
                                    HttpURLConnection httpURLConnection6 = this.httpConnection;
                                    if (httpURLConnection6 != null) {
                                        httpURLConnection6.disconnect();
                                    }
                                } catch (Throwable unused6) {
                                }
                                if (inputStream != null) {
                                    try {
                                        inputStream.close();
                                    } catch (Throwable th5) {
                                        FileLog.e(th5);
                                    }
                                }
                                if (byteArrayOutputStream != null) {
                                    byteArrayOutputStream.close();
                                }
                                return null;
                            } catch (Throwable th6) {
                                try {
                                    HttpURLConnection httpURLConnection7 = this.httpConnection;
                                    if (httpURLConnection7 != null) {
                                        httpURLConnection7.disconnect();
                                    }
                                } catch (Throwable unused7) {
                                }
                                if (inputStream != null) {
                                    try {
                                        inputStream.close();
                                    } catch (Throwable th7) {
                                        FileLog.e(th7);
                                    }
                                }
                                if (byteArrayOutputStream != null) {
                                    try {
                                        byteArrayOutputStream.close();
                                    } catch (Exception unused8) {
                                    }
                                }
                                throw th6;
                            }
                        }
                    } catch (Throwable th8) {
                        byteArrayOutputStream = null;
                        inputStream = inputStream2;
                        th = th8;
                    }
                } catch (Throwable th9) {
                    th = th9;
                    inputStream = null;
                    byteArrayOutputStream = null;
                }
            } catch (Exception unused9) {
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(final String str) {
            if (str != null) {
                ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$ArtworkLoadTask$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        ImageLoader.ArtworkLoadTask.this.lambda$onPostExecute$0(str);
                    }
                });
            } else if (this.canRetry) {
                ImageLoader.this.artworkLoadError(this.cacheImage.url);
            }
            ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$ArtworkLoadTask$$ExternalSyntheticLambda0
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
            ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$ArtworkLoadTask$$ExternalSyntheticLambda1
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
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpImageTask.this.lambda$reportProgress$1(j, j2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$reportProgress$1(final long j, final long j2) {
            ImageLoader.this.fileProgresses.put(this.cacheImage.url, new long[]{j, j2});
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda5
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
        /* JADX WARN: Can't wrap try/catch for region: R(16:1|(7:102|103|(1:148)|107|(1:109)|110|(15:112|113|114|4|(6:34|35|(1:43)|45|(3:49|50|(1:58))|(6:63|64|65|(2:66|(1:98)(3:68|69|(3:71|(3:73|74|75)(1:77)|76)(1:78)))|81|82))|6|7|(1:9)|11|12|(1:14)|(2:26|27)|(1:22)|23|24))|3|4|(0)|6|7|(0)|11|12|(0)|(0)|(3:18|20|22)|23|24|(1:(0))) */
        /* JADX WARN: Code restructure failed: missing block: B:100:0x0175, code lost:
            r2 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:101:0x0176, code lost:
            r1 = r2;
            r2 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:102:0x0179, code lost:
            r2 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:103:0x017a, code lost:
            r1 = r2;
            r2 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:106:0x0180, code lost:
            org.telegram.messenger.FileLog.e(r1);
         */
        /* JADX WARN: Code restructure failed: missing block: B:107:0x0183, code lost:
            r1 = r2;
         */
        /* JADX WARN: Code restructure failed: missing block: B:110:0x0187, code lost:
            org.telegram.messenger.FileLog.e(r1);
         */
        /* JADX WARN: Code restructure failed: missing block: B:115:0x0194, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:116:0x0195, code lost:
            org.telegram.messenger.FileLog.e(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:95:0x0169, code lost:
            if (r7 != (-1)) goto L81;
         */
        /* JADX WARN: Code restructure failed: missing block: B:96:0x016b, code lost:
            r2 = r12.imageSize;
         */
        /* JADX WARN: Code restructure failed: missing block: B:97:0x016f, code lost:
            if (r2 == 0) goto L82;
         */
        /* JADX WARN: Code restructure failed: missing block: B:98:0x0171, code lost:
            reportProgress(r2, r2);
         */
        /* JADX WARN: Removed duplicated region for block: B:113:0x018e A[Catch: all -> 0x0194, TRY_LEAVE, TryCatch #4 {all -> 0x0194, blocks: (B:111:0x018a, B:113:0x018e), top: B:143:0x018a }] */
        /* JADX WARN: Removed duplicated region for block: B:119:0x019c A[Catch: all -> 0x01a0, TRY_LEAVE, TryCatch #0 {all -> 0x01a0, blocks: (B:117:0x0198, B:119:0x019c), top: B:135:0x0198 }] */
        /* JADX WARN: Removed duplicated region for block: B:128:0x01ad  */
        /* JADX WARN: Removed duplicated region for block: B:149:0x01a3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:151:0x00ee A[EXC_TOP_SPLITTER, SYNTHETIC] */
        @Override // android.os.AsyncTask
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public Boolean doInBackground(Void... voidArr) {
            InputStream inputStream;
            boolean z;
            InputStream inputStream2;
            int i;
            WebFile webFile;
            HttpURLConnection httpURLConnection;
            List<String> list;
            String str;
            int responseCode;
            CacheImage cacheImage;
            File file;
            HttpURLConnection httpURLConnection2;
            RandomAccessFile randomAccessFile;
            boolean z2 = true;
            boolean z3 = false;
            if (!isCancelled()) {
                try {
                    String str2 = this.cacheImage.imageLocation.path;
                    if ((str2.startsWith("https://static-maps") || str2.startsWith("https://maps.googleapis")) && (((i = MessagesController.getInstance(this.cacheImage.currentAccount).mapProvider) == 3 || i == 4) && (webFile = (WebFile) ImageLoader.this.testWebFile.get(str2)) != null)) {
                        TLRPC$TL_upload_getWebFile tLRPC$TL_upload_getWebFile = new TLRPC$TL_upload_getWebFile();
                        tLRPC$TL_upload_getWebFile.location = webFile.location;
                        tLRPC$TL_upload_getWebFile.offset = 0;
                        tLRPC$TL_upload_getWebFile.limit = 0;
                        ConnectionsManager.getInstance(this.cacheImage.currentAccount).sendRequest(tLRPC$TL_upload_getWebFile, new RequestDelegate() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda8
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                ImageLoader.HttpImageTask.lambda$doInBackground$2(tLObject, tLRPC$TL_error);
                            }
                        });
                    }
                    String str3 = this.overrideUrl;
                    if (str3 != null) {
                        str2 = str3;
                    }
                    HttpURLConnection httpURLConnection3 = (HttpURLConnection) new URL(str2).openConnection();
                    this.httpConnection = httpURLConnection3;
                    httpURLConnection3.addRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1");
                    this.httpConnection.setConnectTimeout(5000);
                    this.httpConnection.setReadTimeout(5000);
                    this.httpConnection.setInstanceFollowRedirects(true);
                } catch (Throwable th) {
                    th = th;
                    inputStream = null;
                }
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
                            randomAccessFile = this.fileOutputStream;
                            if (randomAccessFile != null) {
                            }
                            httpURLConnection2 = this.httpConnection;
                            if (httpURLConnection2 != null) {
                            }
                            if (inputStream2 != null) {
                            }
                            if (z3) {
                            }
                            return Boolean.valueOf(z3);
                        }
                        z = false;
                        FileLog.e(th, z);
                        inputStream2 = inputStream;
                        if (!isCancelled()) {
                        }
                        randomAccessFile = this.fileOutputStream;
                        if (randomAccessFile != null) {
                        }
                        httpURLConnection2 = this.httpConnection;
                        if (httpURLConnection2 != null) {
                        }
                        if (inputStream2 != null) {
                        }
                        if (z3) {
                        }
                        return Boolean.valueOf(z3);
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
                                if (headerFields != null && (list = headerFields.get("content-Length")) != null && !list.isEmpty() && (str = list.get(0)) != null) {
                                    this.imageSize = Utilities.parseInt((CharSequence) str).intValue();
                                }
                            } catch (Exception e2) {
                                FileLog.e(e2);
                            }
                        }
                        if (inputStream2 != null) {
                            try {
                                byte[] bArr = new byte[LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM];
                                int i2 = 0;
                                while (true) {
                                    if (isCancelled()) {
                                        break;
                                    }
                                    try {
                                        int read = inputStream2.read(bArr);
                                        if (read <= 0) {
                                            break;
                                        }
                                        i2 += read;
                                        this.fileOutputStream.write(bArr, 0, read);
                                        long j = this.imageSize;
                                        if (j != 0) {
                                            reportProgress(i2, j);
                                        }
                                    } catch (Exception e3) {
                                        e = e3;
                                    }
                                }
                                z2 = false;
                                z3 = z2;
                            } catch (Throwable th3) {
                                th = th3;
                            }
                        }
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
                    if (z3 && (file = (cacheImage = this.cacheImage).tempFilePath) != null && !file.renameTo(cacheImage.finalFilePath)) {
                        CacheImage cacheImage2 = this.cacheImage;
                        cacheImage2.finalFilePath = cacheImage2.tempFilePath;
                    }
                    return Boolean.valueOf(z3);
                }
            }
            inputStream2 = null;
            if (!isCancelled()) {
            }
            randomAccessFile = this.fileOutputStream;
            if (randomAccessFile != null) {
            }
            httpURLConnection2 = this.httpConnection;
            if (httpURLConnection2 != null) {
            }
            if (inputStream2 != null) {
            }
            if (z3) {
                CacheImage cacheImage22 = this.cacheImage;
                cacheImage22.finalFilePath = cacheImage22.tempFilePath;
            }
            return Boolean.valueOf(z3);
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
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpImageTask.this.lambda$onPostExecute$4(bool);
                }
            });
            ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpImageTask.this.lambda$onPostExecute$5();
                }
            }, this.cacheImage.priority);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPostExecute$4(final Boolean bool) {
            ImageLoader.this.fileProgresses.remove(this.cacheImage.url);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda7
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
            ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpImageTask.this.lambda$onCancelled$6();
                }
            }, this.cacheImage.priority);
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpImageTask.this.lambda$onCancelled$8();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCancelled$8() {
            ImageLoader.this.fileProgresses.remove(this.cacheImage.url);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda1
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
            ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$ThumbGenerateTask$$ExternalSyntheticLambda0
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

        @Override // java.lang.Runnable
        public void run() {
            int min;
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
                        min = Math.min(180, Math.min(point2.x, point2.y) / 4);
                    }
                    int i = this.mediaType;
                    Bitmap bitmap = null;
                    if (i == 0) {
                        float f = min;
                        bitmap = ImageLoader.loadBitmap(this.originalPath.toString(), null, f, f, false);
                    } else {
                        int i2 = 2;
                        if (i == 2) {
                            String file2 = this.originalPath.toString();
                            if (!this.info.big) {
                                i2 = 1;
                            }
                            bitmap = SendMessagesHelper.createVideoThumbnail(file2, i2);
                        } else if (i == 3) {
                            String lowerCase = this.originalPath.toString().toLowerCase();
                            if (lowerCase.endsWith("mp4")) {
                                String file3 = this.originalPath.toString();
                                if (!this.info.big) {
                                    i2 = 1;
                                }
                                bitmap = SendMessagesHelper.createVideoThumbnail(file3, i2);
                            } else if (lowerCase.endsWith(".jpg") || lowerCase.endsWith(".jpeg") || lowerCase.endsWith(".png") || lowerCase.endsWith(".gif")) {
                                float f2 = min;
                                bitmap = ImageLoader.loadBitmap(lowerCase, null, f2, f2, false);
                            }
                        }
                    }
                    if (bitmap == null) {
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
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$ThumbGenerateTask$$ExternalSyntheticLambda1
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

        /* JADX WARN: Can't wrap try/catch for region: R(11:1009|(2:1011|(9:1013|1014|1015|(1:1017)|1018|(2:1024|(1:1026))|(1:1028)(1:1031)|1029|1030))|1035|1014|1015|(0)|1018|(4:1020|1022|1024|(0))|(0)(0)|1029|1030) */
        /* JADX WARN: Can't wrap try/catch for region: R(14:70|(6:71|72|73|74|(1:76)(1:116)|77)|(3:79|80|(7:82|83|84|(1:110)(1:87)|(2:96|(1:109)(4:99|(1:103)|104|(1:108)))(1:91)|(1:93)(1:95)|94))|115|83|84|(0)|110|(0)|96|(0)|109|(0)(0)|94) */
        /* JADX WARN: Can't wrap try/catch for region: R(25:(6:964|965|966|967|(1:969)(1:982)|970)|(2:972|(23:974|975|976|333|(21:335|(3:337|(1:339)(1:952)|340)(2:953|(18:955|(1:957)(1:959)|958|342|343|(1:345)|346|347|348|(15:350|(5:352|353|354|355|356)(1:919)|357|358|(1:360)(2:900|(1:902)(2:903|(1:905)(2:906|(1:908)(1:909))))|361|362|363|364|365|366|367|(1:369)(2:888|(1:890))|(1:887)(11:375|376|377|378|(2:853|(10:855|(1:874)(1:859)|(1:861)|862|863|864|(3:869|870|(1:872))|873|870|(0))(3:875|(1:877)(1:879)|878))(2:(6:382|383|384|385|386|387)(1:852)|388)|389|(1:844)(1:393)|394|(1:396)|397|(1:843)(4:403|(1:404)|406|407))|408)(3:920|(11:922|923|924|(1:926)(1:946)|927|928|929|(1:931)|932|(4:934|(1:935)|937|938)(1:941)|939)(1:949)|940)|409|410|(3:715|716|8a0)(7:412|(1:414)|(3:699|700|(6:702|703|(3:706|707|(1:709))(1:705)|417|418|b68))|416|417|418|b68)|456|(3:459|(1:461)(1:463)|462)|(2:469|(1:471))|472|(3:(1:487)(1:490)|488|489)(3:(1:479)(1:482)|480|481))(2:960|(1:962)))|341|342|343|(0)|346|347|348|(0)(0)|409|410|(0)(0)|456|(3:459|(0)(0)|462)|(4:465|467|469|(0))|472|(1:474)|(0)(0)|488|489)|963|343|(0)|346|347|348|(0)(0)|409|410|(0)(0)|456|(0)|(0)|472|(0)|(0)(0)|488|489))|981|975|976|333|(0)|963|343|(0)|346|347|348|(0)(0)|409|410|(0)(0)|456|(0)|(0)|472|(0)|(0)(0)|488|489) */
        /* JADX WARN: Can't wrap try/catch for region: R(27:316|(1:1008)(1:323)|324|(2:326|(1:1006))(1:1007)|330|(30:964|965|966|967|(1:969)(1:982)|970|(2:972|(23:974|975|976|333|(21:335|(3:337|(1:339)(1:952)|340)(2:953|(18:955|(1:957)(1:959)|958|342|343|(1:345)|346|347|348|(15:350|(5:352|353|354|355|356)(1:919)|357|358|(1:360)(2:900|(1:902)(2:903|(1:905)(2:906|(1:908)(1:909))))|361|362|363|364|365|366|367|(1:369)(2:888|(1:890))|(1:887)(11:375|376|377|378|(2:853|(10:855|(1:874)(1:859)|(1:861)|862|863|864|(3:869|870|(1:872))|873|870|(0))(3:875|(1:877)(1:879)|878))(2:(6:382|383|384|385|386|387)(1:852)|388)|389|(1:844)(1:393)|394|(1:396)|397|(1:843)(4:403|(1:404)|406|407))|408)(3:920|(11:922|923|924|(1:926)(1:946)|927|928|929|(1:931)|932|(4:934|(1:935)|937|938)(1:941)|939)(1:949)|940)|409|410|(3:715|716|8a0)(7:412|(1:414)|(3:699|700|(6:702|703|(3:706|707|(1:709))(1:705)|417|418|b68))|416|417|418|b68)|456|(3:459|(1:461)(1:463)|462)|(2:469|(1:471))|472|(3:(1:487)(1:490)|488|489)(3:(1:479)(1:482)|480|481))(2:960|(1:962)))|341|342|343|(0)|346|347|348|(0)(0)|409|410|(0)(0)|456|(3:459|(0)(0)|462)|(4:465|467|469|(0))|472|(1:474)|(0)(0)|488|489)|963|343|(0)|346|347|348|(0)(0)|409|410|(0)(0)|456|(0)|(0)|472|(0)|(0)(0)|488|489))|981|975|976|333|(0)|963|343|(0)|346|347|348|(0)(0)|409|410|(0)(0)|456|(0)|(0)|472|(0)|(0)(0)|488|489)|332|333|(0)|963|343|(0)|346|347|348|(0)(0)|409|410|(0)(0)|456|(0)|(0)|472|(0)|(0)(0)|488|489) */
        /* JADX WARN: Can't wrap try/catch for region: R(7:412|(1:414)|(3:699|700|(6:702|703|(3:706|707|(1:709))(1:705)|417|418|b68))|416|417|418|b68) */
        /* JADX WARN: Code restructure failed: missing block: B:115:0x0208, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:116:0x0209, code lost:
            org.telegram.messenger.FileLog.e(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:344:0x057d, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:345:0x057e, code lost:
            org.telegram.messenger.FileLog.e(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:540:0x0875, code lost:
            r0 = th;
         */
        /* JADX WARN: Code restructure failed: missing block: B:541:0x0876, code lost:
            r26 = r14;
            r35 = r15;
         */
        /* JADX WARN: Code restructure failed: missing block: B:916:0x0e65, code lost:
            r0 = th;
         */
        /* JADX WARN: Code restructure failed: missing block: B:969:0x0f3b, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:970:0x0f3c, code lost:
            org.telegram.messenger.FileLog.e(r0);
            r2 = null;
         */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Not initialized variable reg: 25, insn: 0x0760: MOVE  (r4 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r25 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:469:0x0760 */
        /* JADX WARN: Removed duplicated region for block: B:1005:0x0bee A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:1041:0x0b69 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:1064:0x059f A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:1069:0x0895 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:1070:0x02ba A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:131:0x022a A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:136:0x0232 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:142:0x0240 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:155:0x0269  */
        /* JADX WARN: Removed duplicated region for block: B:156:0x0287  */
        /* JADX WARN: Removed duplicated region for block: B:191:0x030a  */
        /* JADX WARN: Removed duplicated region for block: B:205:0x033b  */
        /* JADX WARN: Removed duplicated region for block: B:218:0x036a  */
        /* JADX WARN: Removed duplicated region for block: B:219:0x036f  */
        /* JADX WARN: Removed duplicated region for block: B:282:0x0471  */
        /* JADX WARN: Removed duplicated region for block: B:283:0x0473  */
        /* JADX WARN: Removed duplicated region for block: B:285:0x047b  */
        /* JADX WARN: Removed duplicated region for block: B:286:0x047d  */
        /* JADX WARN: Removed duplicated region for block: B:289:0x0484  */
        /* JADX WARN: Removed duplicated region for block: B:290:0x0487  */
        /* JADX WARN: Removed duplicated region for block: B:293:0x0495  */
        /* JADX WARN: Removed duplicated region for block: B:294:0x0498  */
        /* JADX WARN: Removed duplicated region for block: B:305:0x04dc  */
        /* JADX WARN: Removed duplicated region for block: B:309:0x04f9  */
        /* JADX WARN: Removed duplicated region for block: B:369:0x05b2  */
        /* JADX WARN: Removed duplicated region for block: B:391:0x061f  */
        /* JADX WARN: Removed duplicated region for block: B:395:0x062f A[Catch: all -> 0x0875, TryCatch #43 {all -> 0x0875, blocks: (B:393:0x0629, B:395:0x062f, B:397:0x0638), top: B:1062:0x0629 }] */
        /* JADX WARN: Removed duplicated region for block: B:466:0x0757 A[Catch: all -> 0x07c9, TryCatch #15 {all -> 0x07c9, blocks: (B:475:0x0780, B:479:0x078e, B:484:0x07a8, B:491:0x07b8, B:493:0x07c2, B:494:0x07c5, B:480:0x0797, B:457:0x073f, B:459:0x0745, B:464:0x074f, B:466:0x0757, B:472:0x076a, B:474:0x0779, B:473:0x0774), top: B:1015:0x06cc }] */
        /* JADX WARN: Removed duplicated region for block: B:514:0x07fc  */
        /* JADX WARN: Removed duplicated region for block: B:679:0x0b16  */
        /* JADX WARN: Removed duplicated region for block: B:77:0x0155  */
        /* JADX WARN: Removed duplicated region for block: B:827:0x0d4b  */
        /* JADX WARN: Removed duplicated region for block: B:830:0x0d51  */
        /* JADX WARN: Removed duplicated region for block: B:837:0x0d69 A[Catch: all -> 0x0e5a, TryCatch #28 {all -> 0x0e5a, blocks: (B:831:0x0d53, B:833:0x0d5d, B:835:0x0d63, B:837:0x0d69, B:839:0x0d6f, B:845:0x0d86, B:851:0x0d95, B:853:0x0d9b, B:859:0x0dba, B:854:0x0da5, B:856:0x0dab, B:862:0x0dc2, B:864:0x0dd0, B:866:0x0ddb, B:870:0x0de2, B:825:0x0d44), top: B:1038:0x0d44 }] */
        /* JADX WARN: Removed duplicated region for block: B:859:0x0dba A[Catch: all -> 0x0e5a, TryCatch #28 {all -> 0x0e5a, blocks: (B:831:0x0d53, B:833:0x0d5d, B:835:0x0d63, B:837:0x0d69, B:839:0x0d6f, B:845:0x0d86, B:851:0x0d95, B:853:0x0d9b, B:859:0x0dba, B:854:0x0da5, B:856:0x0dab, B:862:0x0dc2, B:864:0x0dd0, B:866:0x0ddb, B:870:0x0de2, B:825:0x0d44), top: B:1038:0x0d44 }] */
        /* JADX WARN: Removed duplicated region for block: B:926:0x0e7e A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:929:0x0e8c  */
        /* JADX WARN: Removed duplicated region for block: B:930:0x0e8e  */
        /* JADX WARN: Removed duplicated region for block: B:933:0x0ea3  */
        /* JADX WARN: Removed duplicated region for block: B:939:0x0ec2  */
        /* JADX WARN: Removed duplicated region for block: B:942:0x0ecc  */
        /* JADX WARN: Removed duplicated region for block: B:955:0x0eef  */
        /* JADX WARN: Removed duplicated region for block: B:956:0x0ef6  */
        /* JADX WARN: Removed duplicated region for block: B:966:0x0f35  */
        /* JADX WARN: Removed duplicated region for block: B:978:0x0f61  */
        /* JADX WARN: Removed duplicated region for block: B:980:0x0f69  */
        /* JADX WARN: Removed duplicated region for block: B:981:0x0f6f  */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void run() {
            Drawable drawable;
            boolean z;
            Bitmap bitmap;
            Object obj;
            CacheImage cacheImage;
            SecureDocumentKey secureDocumentKey;
            byte[] bArr;
            Throwable th;
            RandomAccessFile randomAccessFile;
            RandomAccessFile randomAccessFile2;
            String lowerCase;
            boolean z2;
            String str;
            String str2;
            Long l;
            boolean z3;
            boolean z4;
            boolean z5;
            boolean z6;
            float f;
            Bitmap bitmap2;
            float f2;
            char c;
            boolean z7;
            int i;
            float f3;
            char c2;
            boolean z8;
            boolean z9;
            int i2;
            int i3;
            Drawable drawable2;
            boolean z10;
            int i4;
            FileInputStream fileInputStream;
            Bitmap createScaledBitmap;
            CacheImage cacheImage2;
            String str3;
            Object obj2;
            Bitmap bitmap3;
            float f4;
            float f5;
            boolean z11;
            int i5;
            int i6;
            boolean z12;
            int i7;
            FileInputStream fileInputStream2;
            String str4;
            boolean z13;
            Bitmap createScaledBitmap2;
            Bitmap thumbnail;
            String str5;
            char c3;
            boolean z14;
            boolean z15;
            FileInputStream fileInputStream3;
            boolean z16;
            float min;
            Long l2;
            boolean z17;
            String str6;
            boolean z18;
            boolean z19;
            boolean z20;
            boolean z21;
            boolean z22;
            Bitmap bitmap4;
            MediaMetadataRetriever mediaMetadataRetriever;
            BitmapsCache.CacheOptions cacheOptions;
            AnimatedFileDrawable animatedFileDrawable;
            int i8;
            int i9;
            String str7;
            int i10;
            int i11;
            int i12;
            int i13;
            boolean z23;
            boolean z24;
            boolean z25;
            String str8;
            boolean z26;
            boolean z27;
            int i14;
            Throwable th2;
            RandomAccessFile randomAccessFile3;
            boolean z28;
            RandomAccessFile randomAccessFile4;
            boolean z29;
            BitmapsCache.CacheOptions cacheOptions2;
            RLottieDrawable rLottieDrawable;
            RLottieDrawable rLottieDrawable2;
            byte[] bArr2;
            RLottieDrawable rLottieDrawable3;
            boolean z30;
            boolean z31;
            synchronized (this.sync) {
                this.runningThread = Thread.currentThread();
                Thread.interrupted();
                if (this.isCancelled) {
                    return;
                }
                CacheImage cacheImage3 = this.cacheImage;
                ImageLocation imageLocation = cacheImage3.imageLocation;
                TLRPC$PhotoSize tLRPC$PhotoSize = imageLocation.photoSize;
                if (tLRPC$PhotoSize instanceof TLRPC$TL_photoStrippedSize) {
                    Bitmap strippedPhotoBitmap = ImageLoader.getStrippedPhotoBitmap(((TLRPC$TL_photoStrippedSize) tLRPC$PhotoSize).bytes, "b");
                    onPostExecute(strippedPhotoBitmap != null ? new BitmapDrawable(strippedPhotoBitmap) : null);
                    return;
                }
                int i15 = cacheImage3.imageType;
                if (i15 == 5) {
                    try {
                        CacheImage cacheImage4 = this.cacheImage;
                        drawable = new ThemePreviewDrawable(cacheImage4.finalFilePath, (DocumentObject.ThemeDocument) cacheImage4.imageLocation.document);
                    } catch (Throwable th3) {
                        FileLog.e(th3);
                        drawable = null;
                    }
                    onPostExecute(drawable);
                    return;
                }
                boolean z32 = true;
                if (i15 == 3 || i15 == 4) {
                    Point point = AndroidUtilities.displaySize;
                    int i16 = point.x;
                    int i17 = point.y;
                    String str9 = cacheImage3.filter;
                    if (str9 != null) {
                        String[] split = str9.split("_");
                        if (split.length >= 2) {
                            z = false;
                            float parseFloat = Float.parseFloat(split[0]);
                            float parseFloat2 = Float.parseFloat(split[1]);
                            float f6 = AndroidUtilities.density;
                            i16 = (int) (parseFloat * f6);
                            i17 = (int) (parseFloat2 * f6);
                            cacheImage = this.cacheImage;
                            File file = cacheImage.finalFilePath;
                            if (cacheImage.imageType == 4) {
                                z = true;
                            }
                            bitmap = SvgHelper.getBitmap(file, i16, i17, z);
                            if (bitmap != null && !TextUtils.isEmpty(this.cacheImage.filter) && this.cacheImage.filter.contains("wallpaper")) {
                                obj = this.cacheImage.parentObject;
                                if (obj instanceof TLRPC$WallPaper) {
                                    bitmap = applyWallpaperSetting(bitmap, (TLRPC$WallPaper) obj);
                                }
                            }
                            onPostExecute(bitmap == null ? new BitmapDrawable(bitmap) : null);
                        }
                    }
                    z = false;
                    cacheImage = this.cacheImage;
                    File file2 = cacheImage.finalFilePath;
                    if (cacheImage.imageType == 4) {
                    }
                    bitmap = SvgHelper.getBitmap(file2, i16, i17, z);
                    if (bitmap != null) {
                        obj = this.cacheImage.parentObject;
                        if (obj instanceof TLRPC$WallPaper) {
                        }
                    }
                    onPostExecute(bitmap == null ? new BitmapDrawable(bitmap) : null);
                } else if (i15 == 1) {
                    int min2 = Math.min((int) LiteMode.FLAG_CALLS_ANIMATIONS, AndroidUtilities.dp(170.6f));
                    int min3 = Math.min((int) LiteMode.FLAG_CALLS_ANIMATIONS, AndroidUtilities.dp(170.6f));
                    String str10 = this.cacheImage.filter;
                    if (str10 != null) {
                        String[] split2 = str10.split("_");
                        if (split2.length >= 2) {
                            float parseFloat3 = Float.parseFloat(split2[0]);
                            float parseFloat4 = Float.parseFloat(split2[1]);
                            int min4 = Math.min((int) LiteMode.FLAG_CALLS_ANIMATIONS, (int) (AndroidUtilities.density * parseFloat3));
                            int min5 = Math.min((int) LiteMode.FLAG_CALLS_ANIMATIONS, (int) (AndroidUtilities.density * parseFloat4));
                            if (parseFloat3 > 90.0f || parseFloat4 > 90.0f || this.cacheImage.filter.contains("nolimit")) {
                                min3 = min5;
                                min2 = min4;
                                z23 = false;
                            } else {
                                min2 = Math.min(min4, 160);
                                min3 = Math.min(min5, 160);
                                z23 = true;
                            }
                            z27 = (split2.length >= 3 && "pcache".equals(split2[2])) || this.cacheImage.filter.contains("pcache") || !(this.cacheImage.filter.contains("nolimit") || SharedConfig.getDevicePerformanceClass() == 2);
                            z31 = this.cacheImage.filter.contains("lastframe");
                            z30 = this.cacheImage.filter.contains("lastreactframe");
                            if (z30) {
                                z31 = true;
                            }
                            z26 = this.cacheImage.filter.contains("firstframe");
                        } else {
                            z23 = false;
                            z30 = false;
                            z26 = false;
                            z27 = false;
                            z31 = false;
                        }
                        if (split2.length >= 3) {
                            if ("nr".equals(split2[2])) {
                                str8 = null;
                            } else if (!"nrs".equals(split2[2])) {
                                if ("dice".equals(split2[2])) {
                                    str8 = split2[3];
                                }
                            } else {
                                str8 = null;
                                i14 = 3;
                                if (split2.length >= 5) {
                                    if ("c1".equals(split2[4])) {
                                        i11 = min3;
                                        z25 = z30;
                                        z24 = z31;
                                        i12 = min2;
                                        i13 = 12;
                                    } else if ("c2".equals(split2[4])) {
                                        i11 = min3;
                                        z25 = z30;
                                        z24 = z31;
                                        i12 = min2;
                                        i13 = 3;
                                    } else if ("c3".equals(split2[4])) {
                                        i11 = min3;
                                        z25 = z30;
                                        z24 = z31;
                                        i12 = min2;
                                        i13 = 4;
                                    } else if ("c4".equals(split2[4])) {
                                        i11 = min3;
                                        z25 = z30;
                                        z24 = z31;
                                        i12 = min2;
                                        i13 = 5;
                                    } else if ("c5".equals(split2[4])) {
                                        i11 = min3;
                                        z25 = z30;
                                        z24 = z31;
                                        i12 = min2;
                                        i13 = 6;
                                    }
                                }
                                i11 = min3;
                                z25 = z30;
                                z24 = z31;
                                i12 = min2;
                                i13 = 0;
                            }
                            i14 = 2;
                            if (split2.length >= 5) {
                            }
                            i11 = min3;
                            z25 = z30;
                            z24 = z31;
                            i12 = min2;
                            i13 = 0;
                        }
                        str8 = null;
                        i14 = 1;
                        if (split2.length >= 5) {
                        }
                        i11 = min3;
                        z25 = z30;
                        z24 = z31;
                        i12 = min2;
                        i13 = 0;
                    } else {
                        i11 = min3;
                        i12 = min2;
                        i13 = 0;
                        z23 = false;
                        z24 = false;
                        z25 = false;
                        str8 = null;
                        z26 = false;
                        z27 = false;
                        i14 = 1;
                    }
                    if (str8 != null) {
                        if ("🎰".equals(str8)) {
                            rLottieDrawable3 = new SlotsDrawable(str8, i12, i11);
                        } else {
                            rLottieDrawable3 = new RLottieDrawable(str8, i12, i11);
                        }
                        rLottieDrawable2 = rLottieDrawable3;
                    } else {
                        File file3 = this.cacheImage.finalFilePath;
                        try {
                            try {
                                randomAccessFile4 = new RandomAccessFile(this.cacheImage.finalFilePath, "r");
                                try {
                                    bArr2 = this.cacheImage.type == 1 ? ImageLoader.headerThumb : ImageLoader.header;
                                    randomAccessFile4.readFully(bArr2, 0, 2);
                                } catch (Exception e) {
                                    e = e;
                                    z28 = false;
                                    FileLog.e(e, z28);
                                    if (randomAccessFile4 != null) {
                                        try {
                                            randomAccessFile4.close();
                                        } catch (Exception e2) {
                                            FileLog.e(e2);
                                        }
                                    }
                                    z29 = false;
                                    if ((!z24 || z26) ? false : z27) {
                                    }
                                    cacheOptions2 = new BitmapsCache.CacheOptions();
                                    if (z24) {
                                    }
                                    cacheOptions2.firstFrame = true;
                                    if (z29) {
                                    }
                                    rLottieDrawable2 = rLottieDrawable;
                                    if (!z24) {
                                    }
                                    loadLastFrame(rLottieDrawable2, i11, i12, z24, z25);
                                    return;
                                }
                            } catch (Throwable th4) {
                                th2 = th4;
                                randomAccessFile3 = str8;
                                if (randomAccessFile3 != 0) {
                                    try {
                                        randomAccessFile3.close();
                                    } catch (Exception e3) {
                                        FileLog.e(e3);
                                    }
                                }
                                throw th2;
                            }
                        } catch (Exception e4) {
                            e = e4;
                            z28 = false;
                            randomAccessFile4 = null;
                        } catch (Throwable th5) {
                            th2 = th5;
                            randomAccessFile3 = 0;
                            if (randomAccessFile3 != 0) {
                            }
                            throw th2;
                        }
                        if (bArr2[0] == 31) {
                            if (bArr2[1] == -117) {
                                z29 = true;
                                randomAccessFile4.close();
                                if (!((!z24 || z26) ? false : z27) || z24 || z26) {
                                    cacheOptions2 = new BitmapsCache.CacheOptions();
                                    if (z24 && !z26) {
                                        String str11 = this.cacheImage.filter;
                                        if (str11 != null && str11.contains("compress")) {
                                            cacheOptions2.compressQuality = 60;
                                        }
                                        String str12 = this.cacheImage.filter;
                                        if (str12 != null && str12.contains("flbk")) {
                                            cacheOptions2.fallback = true;
                                        }
                                    } else {
                                        cacheOptions2.firstFrame = true;
                                    }
                                } else {
                                    cacheOptions2 = null;
                                }
                                if (z29) {
                                    File file4 = this.cacheImage.finalFilePath;
                                    rLottieDrawable = new RLottieDrawable(file4, ImageLoader.decompressGzip(file4), i12, i11, cacheOptions2, z23, null, i13);
                                } else {
                                    rLottieDrawable = new RLottieDrawable(this.cacheImage.finalFilePath, i12, i11, cacheOptions2, z23, null, i13);
                                }
                                rLottieDrawable2 = rLottieDrawable;
                            }
                        }
                        z29 = false;
                        randomAccessFile4.close();
                        if ((!z24 || z26) ? false : z27) {
                        }
                        cacheOptions2 = new BitmapsCache.CacheOptions();
                        if (z24) {
                        }
                        cacheOptions2.firstFrame = true;
                        if (z29) {
                        }
                        rLottieDrawable2 = rLottieDrawable;
                    }
                    if (!z24 || z26) {
                        loadLastFrame(rLottieDrawable2, i11, i12, z24, z25);
                        return;
                    }
                    rLottieDrawable2.setAutoRepeat(i14);
                    onPostExecute(rLottieDrawable2);
                } else if (i15 == 2) {
                    long j = imageLocation != null ? imageLocation.videoSeekTo : 0L;
                    String str13 = cacheImage3.filter;
                    if (str13 != null) {
                        String[] split3 = str13.split("_");
                        if (split3.length >= 2) {
                            float parseFloat5 = Float.parseFloat(split3[0]);
                            float parseFloat6 = Float.parseFloat(split3[1]);
                            if (parseFloat5 <= 90.0f && parseFloat6 <= 90.0f && !this.cacheImage.filter.contains("nolimit")) {
                                z18 = true;
                                z19 = false;
                                z20 = false;
                                z21 = false;
                                z22 = false;
                                for (i10 = 0; i10 < split3.length; i10++) {
                                    if ("pcache".equals(split3[i10])) {
                                        z20 = true;
                                    }
                                    if ("firstframe".equals(split3[i10])) {
                                        z19 = true;
                                    }
                                    if ("nostream".equals(split3[i10])) {
                                        z22 = true;
                                    }
                                    if ("pframe".equals(split3[i10])) {
                                        z21 = true;
                                    }
                                }
                                if (z19) {
                                    z22 = true;
                                }
                            }
                        }
                        z18 = false;
                        z19 = false;
                        z20 = false;
                        z21 = false;
                        z22 = false;
                        while (i10 < split3.length) {
                        }
                        if (z19) {
                        }
                    } else {
                        z18 = false;
                        z19 = false;
                        z20 = false;
                        z21 = false;
                        z22 = false;
                    }
                    if (!z21) {
                        if (!z20 || z19) {
                            cacheOptions = null;
                        } else {
                            BitmapsCache.CacheOptions cacheOptions3 = new BitmapsCache.CacheOptions();
                            String str14 = this.cacheImage.filter;
                            if (str14 != null && str14.contains("compress")) {
                                cacheOptions3.compressQuality = 60;
                            }
                            cacheOptions = cacheOptions3;
                        }
                        if (ImageLoader.this.isAnimatedAvatar(this.cacheImage.filter) || ImageLoader.AUTOPLAY_FILTER.equals(this.cacheImage.filter)) {
                            CacheImage cacheImage5 = this.cacheImage;
                            ImageLocation imageLocation2 = cacheImage5.imageLocation;
                            TLRPC$Document tLRPC$Document = imageLocation2.document;
                            if (!(tLRPC$Document instanceof TLRPC$TL_documentEncrypted) && !z20) {
                                if (!(tLRPC$Document instanceof TLRPC$Document)) {
                                    tLRPC$Document = null;
                                }
                                long j2 = tLRPC$Document != null ? cacheImage5.size : imageLocation2.currentSize;
                                CacheImage cacheImage6 = this.cacheImage;
                                animatedFileDrawable = new AnimatedFileDrawable(cacheImage6.finalFilePath, z19, z22 ? 0L : j2, cacheImage6.priority, z22 ? null : tLRPC$Document, (tLRPC$Document != null || z22) ? null : cacheImage6.imageLocation, cacheImage6.parentObject, j, cacheImage6.currentAccount, false, cacheOptions);
                                if (!MessageObject.isWebM(tLRPC$Document) && !MessageObject.isVideoSticker(tLRPC$Document) && !ImageLoader.this.isAnimatedAvatar(this.cacheImage.filter)) {
                                    z32 = false;
                                }
                                animatedFileDrawable.setIsWebmSticker(z32);
                                if (z19) {
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
                                animatedFileDrawable.setLimitFps(z18);
                                Thread.interrupted();
                                onPostExecute(animatedFileDrawable);
                                return;
                            }
                        }
                        String str15 = this.cacheImage.filter;
                        if (str15 != null) {
                            String[] split4 = str15.split("_");
                            if (split4.length >= 2) {
                                float parseFloat7 = Float.parseFloat(split4[0]);
                                float parseFloat8 = Float.parseFloat(split4[1]);
                                float f7 = AndroidUtilities.density;
                                i9 = (int) (parseFloat8 * f7);
                                i8 = (int) (parseFloat7 * f7);
                                boolean z33 = !z19 || ((str7 = this.cacheImage.filter) != null && ("d".equals(str7) || this.cacheImage.filter.contains("_d")));
                                int i18 = (!z22 ? null : this.cacheImage.imageLocation.document) == null ? 1 : 0;
                                int i19 = this.cacheImage.cacheType;
                                int i20 = i19 <= 1 ? i19 : i18;
                                CacheImage cacheImage7 = this.cacheImage;
                                animatedFileDrawable = new AnimatedFileDrawable(cacheImage7.finalFilePath, z33, 0L, cacheImage7.priority, !z22 ? null : cacheImage7.imageLocation.document, null, null, j, cacheImage7.currentAccount, false, i8, i9, cacheOptions, i20);
                                if (!MessageObject.isWebM(this.cacheImage.imageLocation.document) && !MessageObject.isVideoSticker(this.cacheImage.imageLocation.document) && !ImageLoader.this.isAnimatedAvatar(this.cacheImage.filter)) {
                                    z32 = false;
                                }
                                animatedFileDrawable.setIsWebmSticker(z32);
                                if (z19) {
                                }
                            }
                        }
                        i8 = 0;
                        i9 = 0;
                        if (z19) {
                        }
                        if ((!z22 ? null : this.cacheImage.imageLocation.document) == null) {
                        }
                        int i192 = this.cacheImage.cacheType;
                        if (i192 <= 1) {
                        }
                        CacheImage cacheImage72 = this.cacheImage;
                        animatedFileDrawable = new AnimatedFileDrawable(cacheImage72.finalFilePath, z33, 0L, cacheImage72.priority, !z22 ? null : cacheImage72.imageLocation.document, null, null, j, cacheImage72.currentAccount, false, i8, i9, cacheOptions, i20);
                        if (!MessageObject.isWebM(this.cacheImage.imageLocation.document)) {
                            z32 = false;
                        }
                        animatedFileDrawable.setIsWebmSticker(z32);
                        if (z19) {
                        }
                    } else {
                        try {
                            mediaMetadataRetriever = new MediaMetadataRetriever();
                            mediaMetadataRetriever.setDataSource(this.cacheImage.finalFilePath.getAbsolutePath());
                            bitmap4 = mediaMetadataRetriever.getFrameAtTime(2L);
                        } catch (Exception e5) {
                            e = e5;
                            bitmap4 = null;
                        }
                        try {
                            mediaMetadataRetriever.release();
                        } catch (Exception e6) {
                            e = e6;
                            e.printStackTrace();
                            Thread.interrupted();
                            if (bitmap4 != null) {
                            }
                        }
                        Thread.interrupted();
                        if (bitmap4 != null) {
                            onPostExecute(null);
                        } else {
                            onPostExecute(new BitmapDrawable(bitmap4));
                        }
                    }
                } else {
                    File file5 = cacheImage3.finalFilePath;
                    boolean z34 = (cacheImage3.secureDocument == null && (cacheImage3.encryptionKeyPath == null || file5 == null || !file5.getAbsolutePath().endsWith(".enc"))) ? false : true;
                    SecureDocument secureDocument = this.cacheImage.secureDocument;
                    if (secureDocument != null) {
                        secureDocumentKey = secureDocument.secureDocumentKey;
                        TLRPC$TL_secureFile tLRPC$TL_secureFile = secureDocument.secureFile;
                        if (tLRPC$TL_secureFile == null || (bArr = tLRPC$TL_secureFile.file_hash) == null) {
                            bArr = secureDocument.fileHash;
                        }
                    } else {
                        secureDocumentKey = null;
                        bArr = null;
                    }
                    if (Build.VERSION.SDK_INT < 19) {
                        try {
                            randomAccessFile2 = new RandomAccessFile(file5, "r");
                            try {
                                try {
                                    byte[] bArr3 = this.cacheImage.type == 1 ? ImageLoader.headerThumb : ImageLoader.header;
                                    randomAccessFile2.readFully(bArr3, 0, bArr3.length);
                                    lowerCase = new String(bArr3).toLowerCase().toLowerCase();
                                } catch (Exception e7) {
                                    e = e7;
                                    FileLog.e(e);
                                    if (randomAccessFile2 != null) {
                                        try {
                                            randomAccessFile2.close();
                                        } catch (Exception e8) {
                                            FileLog.e(e8);
                                        }
                                    }
                                    z2 = false;
                                    str = this.cacheImage.imageLocation.path;
                                    if (str != null) {
                                    }
                                    str2 = null;
                                    l = null;
                                    z3 = false;
                                    z4 = true;
                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    options.inSampleSize = 1;
                                    if (Build.VERSION.SDK_INT < 21) {
                                    }
                                    boolean z35 = ImageLoader.this.canForce8888;
                                    str5 = this.cacheImage.filter;
                                    if (str5 == null) {
                                    }
                                    f3 = f2;
                                    c2 = c;
                                    z8 = z7;
                                    i = 1;
                                    if (this.cacheImage.type == i) {
                                    }
                                    Thread.interrupted();
                                    if (BuildVars.LOGS_ENABLED) {
                                    }
                                    if (bitmap2 != null) {
                                    }
                                    cacheImage2 = this.cacheImage;
                                    if (cacheImage2 != null) {
                                    }
                                    onPostExecute(bitmap2 == null ? new ExtendedBitmapDrawable(bitmap2, i3, i2) : drawable2);
                                }
                            } catch (Throwable th6) {
                                th = th6;
                                randomAccessFile = randomAccessFile2;
                                if (randomAccessFile != null) {
                                    try {
                                        randomAccessFile.close();
                                    } catch (Exception e9) {
                                        FileLog.e(e9);
                                    }
                                }
                                throw th;
                            }
                        } catch (Exception e10) {
                            e = e10;
                            randomAccessFile2 = null;
                        } catch (Throwable th7) {
                            th = th7;
                            randomAccessFile = null;
                            if (randomAccessFile != null) {
                            }
                            throw th;
                        }
                        if (lowerCase.startsWith("riff")) {
                            if (lowerCase.endsWith("webp")) {
                                z2 = true;
                                randomAccessFile2.close();
                                str = this.cacheImage.imageLocation.path;
                                if (str != null) {
                                    if (str.startsWith("thumb://")) {
                                        int indexOf = str.indexOf(":", 8);
                                        if (indexOf >= 0) {
                                            l = Long.valueOf(Long.parseLong(str.substring(8, indexOf)));
                                            str6 = str.substring(indexOf + 1);
                                        } else {
                                            str6 = null;
                                            l = null;
                                        }
                                        str2 = str6;
                                    } else if (str.startsWith("vthumb://")) {
                                        int indexOf2 = str.indexOf(":", 9);
                                        if (indexOf2 >= 0) {
                                            l2 = Long.valueOf(Long.parseLong(str.substring(9, indexOf2)));
                                            z17 = true;
                                        } else {
                                            l2 = null;
                                            z17 = false;
                                        }
                                        l = l2;
                                        z3 = z17;
                                        str2 = null;
                                        z4 = false;
                                        BitmapFactory.Options options2 = new BitmapFactory.Options();
                                        options2.inSampleSize = 1;
                                        if (Build.VERSION.SDK_INT < 21) {
                                            options2.inPurgeable = true;
                                        }
                                        boolean z352 = ImageLoader.this.canForce8888;
                                        str5 = this.cacheImage.filter;
                                        if (str5 == null) {
                                            String[] split5 = str5.split("_");
                                            if (split5.length >= 2) {
                                                float parseFloat9 = Float.parseFloat(split5[0]) * AndroidUtilities.density;
                                                try {
                                                    f2 = parseFloat9;
                                                    f = Float.parseFloat(split5[1]) * AndroidUtilities.density;
                                                } catch (Throwable th8) {
                                                    th = th8;
                                                    f2 = parseFloat9;
                                                    z5 = z3;
                                                    z6 = z4;
                                                    f = 0.0f;
                                                    bitmap2 = null;
                                                    c = 0;
                                                    z7 = false;
                                                    i = 1;
                                                    FileLog.e(th, !(th instanceof FileNotFoundException));
                                                    f3 = f2;
                                                    c2 = c;
                                                    z8 = z7;
                                                    if (this.cacheImage.type == i) {
                                                    }
                                                    Thread.interrupted();
                                                    if (BuildVars.LOGS_ENABLED) {
                                                    }
                                                    if (bitmap2 != null) {
                                                    }
                                                    cacheImage2 = this.cacheImage;
                                                    if (cacheImage2 != null) {
                                                    }
                                                    onPostExecute(bitmap2 == null ? new ExtendedBitmapDrawable(bitmap2, i3, i2) : drawable2);
                                                }
                                            } else {
                                                f = 0.0f;
                                                f2 = 0.0f;
                                            }
                                            try {
                                                if (this.cacheImage.filter.contains("b2r")) {
                                                    c3 = 4;
                                                } else if (this.cacheImage.filter.contains("b2")) {
                                                    c3 = 3;
                                                } else if (this.cacheImage.filter.contains("b1")) {
                                                    c3 = 2;
                                                } else {
                                                    c3 = this.cacheImage.filter.contains("b") ? (char) 1 : (char) 0;
                                                }
                                                try {
                                                    boolean contains = this.cacheImage.filter.contains("i");
                                                    try {
                                                        c = c3;
                                                        try {
                                                            if (this.cacheImage.filter.contains("f")) {
                                                                z352 = true;
                                                            } else if (this.cacheImage.filter.contains("F")) {
                                                                z352 = false;
                                                            }
                                                            if (z2 || f2 == 0.0f || f == 0.0f) {
                                                                z14 = z352;
                                                                z7 = contains;
                                                                z5 = z3;
                                                                z6 = z4;
                                                            } else {
                                                                try {
                                                                    options2.inJustDecodeBounds = true;
                                                                } catch (Throwable th9) {
                                                                    th = th9;
                                                                    z7 = contains;
                                                                    z5 = z3;
                                                                    z6 = z4;
                                                                    bitmap2 = null;
                                                                    i = 1;
                                                                    FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                    f3 = f2;
                                                                    c2 = c;
                                                                    z8 = z7;
                                                                    if (this.cacheImage.type == i) {
                                                                    }
                                                                    Thread.interrupted();
                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                    }
                                                                    if (bitmap2 != null) {
                                                                    }
                                                                    cacheImage2 = this.cacheImage;
                                                                    if (cacheImage2 != null) {
                                                                    }
                                                                    onPostExecute(bitmap2 == null ? new ExtendedBitmapDrawable(bitmap2, i3, i2) : drawable2);
                                                                }
                                                                try {
                                                                    try {
                                                                        if (l == null || str2 != null) {
                                                                            z14 = z352;
                                                                            z7 = contains;
                                                                            if (secureDocumentKey != null) {
                                                                                RandomAccessFile randomAccessFile5 = new RandomAccessFile(file5, "r");
                                                                                int length = (int) randomAccessFile5.length();
                                                                                byte[] bArr4 = (byte[]) ImageLoader.bytesLocal.get();
                                                                                if (bArr4 == null || bArr4.length < length) {
                                                                                    bArr4 = null;
                                                                                }
                                                                                if (bArr4 == null) {
                                                                                    bArr4 = new byte[length];
                                                                                    ImageLoader.bytesLocal.set(bArr4);
                                                                                }
                                                                                randomAccessFile5.readFully(bArr4, 0, length);
                                                                                randomAccessFile5.close();
                                                                                EncryptedFileInputStream.decryptBytesWithKeyFile(bArr4, 0, length, secureDocumentKey);
                                                                                z5 = z3;
                                                                                z6 = z4;
                                                                                byte[] computeSHA256 = Utilities.computeSHA256(bArr4, 0, length);
                                                                                if (bArr != null && Arrays.equals(computeSHA256, bArr)) {
                                                                                    z16 = false;
                                                                                    int i21 = bArr4[0] & 255;
                                                                                    int i22 = length - i21;
                                                                                    if (!z16) {
                                                                                        BitmapFactory.decodeByteArray(bArr4, i21, i22, options2);
                                                                                    }
                                                                                }
                                                                                z16 = true;
                                                                                int i212 = bArr4[0] & 255;
                                                                                int i222 = length - i212;
                                                                                if (!z16) {
                                                                                }
                                                                            } else {
                                                                                z5 = z3;
                                                                                z6 = z4;
                                                                                if (z34) {
                                                                                    fileInputStream3 = new EncryptedFileInputStream(file5, this.cacheImage.encryptionKeyPath);
                                                                                } else {
                                                                                    fileInputStream3 = new FileInputStream(file5);
                                                                                }
                                                                                BitmapFactory.decodeStream(fileInputStream3, null, options2);
                                                                                fileInputStream3.close();
                                                                            }
                                                                        } else {
                                                                            if (z3) {
                                                                                z7 = contains;
                                                                                try {
                                                                                    z14 = z352;
                                                                                    MediaStore.Video.Thumbnails.getThumbnail(ApplicationLoader.applicationContext.getContentResolver(), l.longValue(), 1, options2);
                                                                                } catch (Throwable th10) {
                                                                                    th = th10;
                                                                                    z5 = z3;
                                                                                    z6 = z4;
                                                                                    bitmap2 = null;
                                                                                    i = 1;
                                                                                    FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                                    f3 = f2;
                                                                                    c2 = c;
                                                                                    z8 = z7;
                                                                                    if (this.cacheImage.type == i) {
                                                                                    }
                                                                                    Thread.interrupted();
                                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                                    }
                                                                                    if (bitmap2 != null) {
                                                                                    }
                                                                                    cacheImage2 = this.cacheImage;
                                                                                    if (cacheImage2 != null) {
                                                                                    }
                                                                                    onPostExecute(bitmap2 == null ? new ExtendedBitmapDrawable(bitmap2, i3, i2) : drawable2);
                                                                                }
                                                                            } else {
                                                                                z14 = z352;
                                                                                z7 = contains;
                                                                                MediaStore.Images.Thumbnails.getThumbnail(ApplicationLoader.applicationContext.getContentResolver(), l.longValue(), 1, options2);
                                                                            }
                                                                            z5 = z3;
                                                                            z6 = z4;
                                                                        }
                                                                        float f8 = options2.outWidth;
                                                                        float f9 = options2.outHeight;
                                                                        if (f2 >= f && f8 > f9) {
                                                                            min = Math.max(f8 / f2, f9 / f);
                                                                        } else {
                                                                            min = Math.min(f8 / f2, f9 / f);
                                                                        }
                                                                        if (min < 1.2f) {
                                                                            min = 1.0f;
                                                                        }
                                                                        options2.inJustDecodeBounds = false;
                                                                        if (min > 1.0f && (f8 > f2 || f9 > f)) {
                                                                            int i23 = 1;
                                                                            do {
                                                                                i23 *= 2;
                                                                            } while (i23 * 2 < min);
                                                                            options2.inSampleSize = i23;
                                                                        } else {
                                                                            options2.inSampleSize = (int) min;
                                                                        }
                                                                    } catch (Throwable th11) {
                                                                        th = th11;
                                                                        z352 = z15;
                                                                        bitmap2 = null;
                                                                        i = 1;
                                                                        FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                        f3 = f2;
                                                                        c2 = c;
                                                                        z8 = z7;
                                                                        if (this.cacheImage.type == i) {
                                                                        }
                                                                        Thread.interrupted();
                                                                        if (BuildVars.LOGS_ENABLED) {
                                                                        }
                                                                        if (bitmap2 != null) {
                                                                        }
                                                                        cacheImage2 = this.cacheImage;
                                                                        if (cacheImage2 != null) {
                                                                        }
                                                                        onPostExecute(bitmap2 == null ? new ExtendedBitmapDrawable(bitmap2, i3, i2) : drawable2);
                                                                    }
                                                                } catch (Throwable th12) {
                                                                    th = th12;
                                                                    z5 = z3;
                                                                    z6 = z4;
                                                                    z352 = z15;
                                                                    bitmap2 = null;
                                                                    i = 1;
                                                                    FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                    f3 = f2;
                                                                    c2 = c;
                                                                    z8 = z7;
                                                                    if (this.cacheImage.type == i) {
                                                                    }
                                                                    Thread.interrupted();
                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                    }
                                                                    if (bitmap2 != null) {
                                                                    }
                                                                    cacheImage2 = this.cacheImage;
                                                                    if (cacheImage2 != null) {
                                                                    }
                                                                    onPostExecute(bitmap2 == null ? new ExtendedBitmapDrawable(bitmap2, i3, i2) : drawable2);
                                                                }
                                                            }
                                                            z352 = z14;
                                                            bitmap2 = null;
                                                        } catch (Throwable th13) {
                                                            th = th13;
                                                        }
                                                    } catch (Throwable th14) {
                                                        th = th14;
                                                        c = c3;
                                                    }
                                                } catch (Throwable th15) {
                                                    th = th15;
                                                    c = c3;
                                                    z5 = z3;
                                                    z6 = z4;
                                                    bitmap2 = null;
                                                    z7 = false;
                                                    i = 1;
                                                    FileLog.e(th, !(th instanceof FileNotFoundException));
                                                    f3 = f2;
                                                    c2 = c;
                                                    z8 = z7;
                                                    if (this.cacheImage.type == i) {
                                                    }
                                                    Thread.interrupted();
                                                    if (BuildVars.LOGS_ENABLED) {
                                                    }
                                                    if (bitmap2 != null) {
                                                    }
                                                    cacheImage2 = this.cacheImage;
                                                    if (cacheImage2 != null) {
                                                    }
                                                    onPostExecute(bitmap2 == null ? new ExtendedBitmapDrawable(bitmap2, i3, i2) : drawable2);
                                                }
                                            } catch (Throwable th16) {
                                                th = th16;
                                                z5 = z3;
                                                z6 = z4;
                                                bitmap2 = null;
                                                c = 0;
                                                z7 = false;
                                                i = 1;
                                                FileLog.e(th, !(th instanceof FileNotFoundException));
                                                f3 = f2;
                                                c2 = c;
                                                z8 = z7;
                                                if (this.cacheImage.type == i) {
                                                }
                                                Thread.interrupted();
                                                if (BuildVars.LOGS_ENABLED) {
                                                }
                                                if (bitmap2 != null) {
                                                }
                                                cacheImage2 = this.cacheImage;
                                                if (cacheImage2 != null) {
                                                }
                                                onPostExecute(bitmap2 == null ? new ExtendedBitmapDrawable(bitmap2, i3, i2) : drawable2);
                                            }
                                        } else {
                                            z5 = z3;
                                            z6 = z4;
                                            if (str2 != null) {
                                                try {
                                                    options2.inJustDecodeBounds = true;
                                                    options2.inPreferredConfig = z352 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
                                                    FileInputStream fileInputStream4 = new FileInputStream(file5);
                                                    bitmap2 = BitmapFactory.decodeStream(fileInputStream4, null, options2);
                                                    try {
                                                        fileInputStream4.close();
                                                        int i24 = options2.outWidth;
                                                        int i25 = options2.outHeight;
                                                        options2.inJustDecodeBounds = false;
                                                        float min6 = (Math.min(i25, i24) / Math.max(66, Math.min(AndroidUtilities.getRealScreenSize().x, AndroidUtilities.getRealScreenSize().y))) * 6.0f;
                                                        if (min6 < 1.0f) {
                                                            min6 = 1.0f;
                                                        }
                                                        if (min6 > 1.0f) {
                                                            int i26 = 1;
                                                            do {
                                                                i26 *= 2;
                                                            } while (i26 * 2 <= min6);
                                                            options2.inSampleSize = i26;
                                                        } else {
                                                            options2.inSampleSize = (int) min6;
                                                        }
                                                        f = 0.0f;
                                                    } catch (Throwable th17) {
                                                        th = th17;
                                                        f = 0.0f;
                                                        f2 = 0.0f;
                                                        c = 0;
                                                        z7 = false;
                                                        i = 1;
                                                        FileLog.e(th, !(th instanceof FileNotFoundException));
                                                        f3 = f2;
                                                        c2 = c;
                                                        z8 = z7;
                                                        if (this.cacheImage.type == i) {
                                                        }
                                                        Thread.interrupted();
                                                        if (BuildVars.LOGS_ENABLED) {
                                                        }
                                                        if (bitmap2 != null) {
                                                        }
                                                        cacheImage2 = this.cacheImage;
                                                        if (cacheImage2 != null) {
                                                        }
                                                        onPostExecute(bitmap2 == null ? new ExtendedBitmapDrawable(bitmap2, i3, i2) : drawable2);
                                                    }
                                                } catch (Throwable th18) {
                                                    th = th18;
                                                    f = 0.0f;
                                                    bitmap2 = null;
                                                    f2 = 0.0f;
                                                    c = 0;
                                                    z7 = false;
                                                    i = 1;
                                                    FileLog.e(th, !(th instanceof FileNotFoundException));
                                                    f3 = f2;
                                                    c2 = c;
                                                    z8 = z7;
                                                    if (this.cacheImage.type == i) {
                                                    }
                                                    Thread.interrupted();
                                                    if (BuildVars.LOGS_ENABLED) {
                                                    }
                                                    if (bitmap2 != null) {
                                                    }
                                                    cacheImage2 = this.cacheImage;
                                                    if (cacheImage2 != null) {
                                                    }
                                                    onPostExecute(bitmap2 == null ? new ExtendedBitmapDrawable(bitmap2, i3, i2) : drawable2);
                                                }
                                            } else {
                                                f = 0.0f;
                                                bitmap2 = null;
                                            }
                                            f2 = 0.0f;
                                            c = 0;
                                            z7 = false;
                                        }
                                        f3 = f2;
                                        c2 = c;
                                        z8 = z7;
                                        i = 1;
                                        if (this.cacheImage.type == i) {
                                            try {
                                                ImageLoader.this.lastCacheOutTime = SystemClock.elapsedRealtime();
                                            } catch (Throwable th19) {
                                                th = th19;
                                                z9 = false;
                                            }
                                            synchronized (this.sync) {
                                                if (this.isCancelled) {
                                                    return;
                                                }
                                                if (z2) {
                                                    RandomAccessFile randomAccessFile6 = new RandomAccessFile(file5, "r");
                                                    MappedByteBuffer map = randomAccessFile6.getChannel().map(FileChannel.MapMode.READ_ONLY, 0L, file5.length());
                                                    BitmapFactory.Options options3 = new BitmapFactory.Options();
                                                    options3.inJustDecodeBounds = true;
                                                    Utilities.loadWebpImage(null, map, map.limit(), options3, true);
                                                    bitmap2 = Bitmaps.createBitmap(options3.outWidth, options3.outHeight, Bitmap.Config.ARGB_8888);
                                                    Utilities.loadWebpImage(bitmap2, map, map.limit(), null, !options2.inPurgeable);
                                                    randomAccessFile6.close();
                                                } else {
                                                    if (!options2.inPurgeable && secureDocumentKey == null) {
                                                        if (z34) {
                                                            fileInputStream = new EncryptedFileInputStream(file5, this.cacheImage.encryptionKeyPath);
                                                        } else {
                                                            fileInputStream = new FileInputStream(file5);
                                                        }
                                                        bitmap2 = BitmapFactory.decodeStream(fileInputStream, null, options2);
                                                        fileInputStream.close();
                                                    }
                                                    RandomAccessFile randomAccessFile7 = new RandomAccessFile(file5, "r");
                                                    int length2 = (int) randomAccessFile7.length();
                                                    byte[] bArr5 = (byte[]) ImageLoader.bytesThumbLocal.get();
                                                    if (bArr5 == null || bArr5.length < length2) {
                                                        bArr5 = null;
                                                    }
                                                    if (bArr5 == null) {
                                                        bArr5 = new byte[length2];
                                                        ImageLoader.bytesThumbLocal.set(bArr5);
                                                    }
                                                    randomAccessFile7.readFully(bArr5, 0, length2);
                                                    randomAccessFile7.close();
                                                    if (secureDocumentKey != null) {
                                                        EncryptedFileInputStream.decryptBytesWithKeyFile(bArr5, 0, length2, secureDocumentKey);
                                                        byte[] computeSHA2562 = Utilities.computeSHA256(bArr5, 0, length2);
                                                        if (bArr != null && Arrays.equals(computeSHA2562, bArr)) {
                                                            z10 = false;
                                                            i4 = bArr5[0] & 255;
                                                            length2 -= i4;
                                                        }
                                                        z10 = true;
                                                        i4 = bArr5[0] & 255;
                                                        length2 -= i4;
                                                    } else {
                                                        if (z34) {
                                                            EncryptedFileInputStream.decryptBytesWithKeyFile(bArr5, 0, length2, this.cacheImage.encryptionKeyPath);
                                                        }
                                                        z10 = false;
                                                        i4 = 0;
                                                    }
                                                    if (!z10) {
                                                        bitmap2 = BitmapFactory.decodeByteArray(bArr5, i4, length2, options2);
                                                    }
                                                }
                                                if (bitmap2 == null) {
                                                    if (file5.length() == 0 || this.cacheImage.filter == null) {
                                                        file5.delete();
                                                    }
                                                    z9 = false;
                                                } else {
                                                    if (this.cacheImage.filter != null) {
                                                        float width = bitmap2.getWidth();
                                                        float height = bitmap2.getHeight();
                                                        if (!options2.inPurgeable && f3 != 0.0f && width != f3 && width > 20.0f + f3 && bitmap2 != (createScaledBitmap = Bitmaps.createScaledBitmap(bitmap2, (int) f3, (int) (height / (width / f3)), true))) {
                                                            bitmap2.recycle();
                                                            bitmap2 = createScaledBitmap;
                                                        }
                                                    }
                                                    z9 = z8 ? Utilities.needInvert(bitmap2, options2.inPurgeable ? 0 : 1, bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getRowBytes()) != 0 : false;
                                                    try {
                                                        if (c2 == 1) {
                                                            if (bitmap2.getConfig() == Bitmap.Config.ARGB_8888) {
                                                                Utilities.blurBitmap(bitmap2, 3, options2.inPurgeable ? 0 : 1, bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getRowBytes());
                                                            }
                                                        } else if (c2 == 2) {
                                                            if (bitmap2.getConfig() == Bitmap.Config.ARGB_8888) {
                                                                Utilities.blurBitmap(bitmap2, 1, options2.inPurgeable ? 0 : 1, bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getRowBytes());
                                                            }
                                                        } else {
                                                            if (c2 != 3 && c2 != 4) {
                                                                if (c2 == 0 && options2.inPurgeable) {
                                                                    Utilities.pinBitmap(bitmap2);
                                                                }
                                                            }
                                                            if (bitmap2.getConfig() == Bitmap.Config.ARGB_8888) {
                                                                if (c2 == 4) {
                                                                    Bitmap createBitmap = Bitmap.createBitmap(bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getConfig());
                                                                    Canvas canvas = new Canvas(createBitmap);
                                                                    canvas.save();
                                                                    canvas.scale(1.2f, 1.2f, bitmap2.getWidth() / 2.0f, bitmap2.getHeight() / 2.0f);
                                                                    canvas.drawBitmap(bitmap2, 0.0f, 0.0f, (Paint) null);
                                                                    canvas.restore();
                                                                    Path path = new Path();
                                                                    path.addCircle(bitmap2.getWidth() / 2.0f, bitmap2.getHeight() / 2.0f, Math.min(bitmap2.getWidth(), bitmap2.getHeight()) / 2.0f, Path.Direction.CW);
                                                                    canvas.clipPath(path);
                                                                    canvas.drawBitmap(bitmap2, 0.0f, 0.0f, (Paint) null);
                                                                    bitmap2.recycle();
                                                                    bitmap2 = createBitmap;
                                                                }
                                                                Utilities.blurBitmap(bitmap2, 7, options2.inPurgeable ? 0 : 1, bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getRowBytes());
                                                                Utilities.blurBitmap(bitmap2, 7, options2.inPurgeable ? 0 : 1, bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getRowBytes());
                                                                Utilities.blurBitmap(bitmap2, 7, options2.inPurgeable ? 0 : 1, bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getRowBytes());
                                                            }
                                                        }
                                                    } catch (Throwable th20) {
                                                        th = th20;
                                                        FileLog.e(th, !(th instanceof FileNotFoundException));
                                                        i2 = 0;
                                                        i3 = 0;
                                                        drawable2 = null;
                                                        Thread.interrupted();
                                                        if (BuildVars.LOGS_ENABLED) {
                                                        }
                                                        if (bitmap2 != null) {
                                                        }
                                                        cacheImage2 = this.cacheImage;
                                                        if (cacheImage2 != null) {
                                                        }
                                                        onPostExecute(bitmap2 == null ? new ExtendedBitmapDrawable(bitmap2, i3, i2) : drawable2);
                                                    }
                                                }
                                                i2 = 0;
                                                i3 = 0;
                                                drawable2 = null;
                                            }
                                        } else {
                                            int i27 = l != null ? 0 : 20;
                                            if (i27 != 0) {
                                                try {
                                                } catch (Throwable th21) {
                                                    th = th21;
                                                }
                                                if (ImageLoader.this.lastCacheOutTime != 0) {
                                                    bitmap3 = bitmap2;
                                                    f4 = f3;
                                                    long j3 = i27;
                                                    if (ImageLoader.this.lastCacheOutTime > SystemClock.elapsedRealtime() - j3) {
                                                        try {
                                                            f5 = f;
                                                            if (Build.VERSION.SDK_INT < 21) {
                                                                Thread.sleep(j3);
                                                            }
                                                        } catch (Throwable th22) {
                                                            th = th22;
                                                            bitmap2 = bitmap3;
                                                            i3 = 0;
                                                            z11 = false;
                                                            drawable2 = null;
                                                            i5 = 0;
                                                            FileLog.e(th, !(th instanceof FileNotFoundException));
                                                            z9 = z11;
                                                            i2 = i5;
                                                            Thread.interrupted();
                                                            if (BuildVars.LOGS_ENABLED) {
                                                            }
                                                            if (bitmap2 != null) {
                                                            }
                                                            cacheImage2 = this.cacheImage;
                                                            if (cacheImage2 != null) {
                                                            }
                                                            onPostExecute(bitmap2 == null ? new ExtendedBitmapDrawable(bitmap2, i3, i2) : drawable2);
                                                        }
                                                    } else {
                                                        f5 = f;
                                                    }
                                                    ImageLoader.this.lastCacheOutTime = SystemClock.elapsedRealtime();
                                                    synchronized (this.sync) {
                                                        try {
                                                            if (this.isCancelled) {
                                                                return;
                                                            }
                                                            if (!z352) {
                                                                CacheImage cacheImage8 = this.cacheImage;
                                                                if (cacheImage8.filter != null && c2 == 0 && cacheImage8.imageLocation.path == null) {
                                                                    options2.inPreferredConfig = Bitmap.Config.RGB_565;
                                                                    options2.inDither = false;
                                                                    if (l == null && str2 == null) {
                                                                        if (z5) {
                                                                            if (l.longValue() == 0) {
                                                                                AnimatedFileDrawable animatedFileDrawable2 = new AnimatedFileDrawable(file5, true, 0L, 0, null, null, null, 0L, 0, true, null);
                                                                                Bitmap frameAtTime2 = animatedFileDrawable2.getFrameAtTime(0L, true);
                                                                                try {
                                                                                    animatedFileDrawable2.recycle();
                                                                                    bitmap2 = frameAtTime2;
                                                                                } catch (Throwable th23) {
                                                                                    th = th23;
                                                                                    bitmap2 = frameAtTime2;
                                                                                    i3 = 0;
                                                                                    z11 = false;
                                                                                    drawable2 = null;
                                                                                    i5 = 0;
                                                                                    FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                                    z9 = z11;
                                                                                    i2 = i5;
                                                                                    Thread.interrupted();
                                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                                    }
                                                                                    if (bitmap2 != null) {
                                                                                    }
                                                                                    cacheImage2 = this.cacheImage;
                                                                                    if (cacheImage2 != null) {
                                                                                    }
                                                                                    onPostExecute(bitmap2 == null ? new ExtendedBitmapDrawable(bitmap2, i3, i2) : drawable2);
                                                                                }
                                                                            } else {
                                                                                thumbnail = MediaStore.Video.Thumbnails.getThumbnail(ApplicationLoader.applicationContext.getContentResolver(), l.longValue(), 1, options2);
                                                                            }
                                                                        } else {
                                                                            thumbnail = MediaStore.Images.Thumbnails.getThumbnail(ApplicationLoader.applicationContext.getContentResolver(), l.longValue(), 1, options2);
                                                                        }
                                                                        bitmap2 = thumbnail;
                                                                    } else {
                                                                        bitmap2 = bitmap3;
                                                                    }
                                                                    if (bitmap2 != null) {
                                                                        try {
                                                                        } catch (Throwable th24) {
                                                                            th = th24;
                                                                            i3 = 0;
                                                                            z11 = false;
                                                                            drawable2 = null;
                                                                            i5 = 0;
                                                                            FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                            z9 = z11;
                                                                            i2 = i5;
                                                                            Thread.interrupted();
                                                                            if (BuildVars.LOGS_ENABLED) {
                                                                            }
                                                                            if (bitmap2 != null) {
                                                                            }
                                                                            cacheImage2 = this.cacheImage;
                                                                            if (cacheImage2 != null) {
                                                                            }
                                                                            onPostExecute(bitmap2 == null ? new ExtendedBitmapDrawable(bitmap2, i3, i2) : drawable2);
                                                                        }
                                                                        if (z2 && secureDocumentKey == null) {
                                                                            RandomAccessFile randomAccessFile8 = new RandomAccessFile(file5, "r");
                                                                            MappedByteBuffer map2 = randomAccessFile8.getChannel().map(FileChannel.MapMode.READ_ONLY, 0L, file5.length());
                                                                            BitmapFactory.Options options4 = new BitmapFactory.Options();
                                                                            options4.inJustDecodeBounds = true;
                                                                            drawable2 = null;
                                                                            try {
                                                                                Utilities.loadWebpImage(null, map2, map2.limit(), options4, true);
                                                                                bitmap2 = Bitmaps.createBitmap(options4.outWidth, options4.outHeight, Bitmap.Config.ARGB_8888);
                                                                                try {
                                                                                    Utilities.loadWebpImage(bitmap2, map2, map2.limit(), null, !options2.inPurgeable);
                                                                                    randomAccessFile8.close();
                                                                                    i3 = 0;
                                                                                    drawable2 = null;
                                                                                } catch (Throwable th25) {
                                                                                    th = th25;
                                                                                    drawable2 = null;
                                                                                }
                                                                            } catch (Throwable th26) {
                                                                                th = th26;
                                                                            }
                                                                        } else {
                                                                            if (bitmap2 == null) {
                                                                                if (secureDocumentKey != null) {
                                                                                    fileInputStream2 = new EncryptedFileInputStream(file5, secureDocumentKey);
                                                                                } else if (z34) {
                                                                                    fileInputStream2 = new EncryptedFileInputStream(file5, this.cacheImage.encryptionKeyPath);
                                                                                } else {
                                                                                    try {
                                                                                        fileInputStream2 = new FileInputStream(file5);
                                                                                    } catch (Throwable th27) {
                                                                                        th = th27;
                                                                                        drawable2 = null;
                                                                                    }
                                                                                }
                                                                                CacheImage cacheImage9 = this.cacheImage;
                                                                                if ((cacheImage9.imageLocation.document instanceof TLRPC$TL_document) || ((str4 = cacheImage9.filter) != null && str4.contains("exif"))) {
                                                                                    Pair<Integer, Integer> imageOrientation = AndroidUtilities.getImageOrientation(fileInputStream2);
                                                                                    i3 = ((Integer) imageOrientation.first).intValue();
                                                                                    try {
                                                                                        i6 = ((Integer) imageOrientation.second).intValue();
                                                                                        try {
                                                                                            if (secureDocumentKey == null) {
                                                                                                try {
                                                                                                    if (this.cacheImage.encryptionKeyPath == null) {
                                                                                                        fileInputStream2.getChannel().position(0L);
                                                                                                    }
                                                                                                } catch (Throwable th28) {
                                                                                                    th = th28;
                                                                                                    i5 = i6;
                                                                                                    z11 = false;
                                                                                                    drawable2 = null;
                                                                                                }
                                                                                            }
                                                                                            fileInputStream2.close();
                                                                                            if (secureDocumentKey != null) {
                                                                                                fileInputStream2 = new EncryptedFileInputStream(file5, secureDocumentKey);
                                                                                            } else if (z34) {
                                                                                                fileInputStream2 = new EncryptedFileInputStream(file5, this.cacheImage.encryptionKeyPath);
                                                                                            }
                                                                                        } catch (Throwable th29) {
                                                                                            th = th29;
                                                                                            drawable2 = null;
                                                                                            i5 = i6;
                                                                                            z11 = false;
                                                                                            FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                                            z9 = z11;
                                                                                            i2 = i5;
                                                                                            Thread.interrupted();
                                                                                            if (BuildVars.LOGS_ENABLED) {
                                                                                            }
                                                                                            if (bitmap2 != null) {
                                                                                            }
                                                                                            cacheImage2 = this.cacheImage;
                                                                                            if (cacheImage2 != null) {
                                                                                            }
                                                                                            onPostExecute(bitmap2 == null ? new ExtendedBitmapDrawable(bitmap2, i3, i2) : drawable2);
                                                                                        }
                                                                                    } catch (Throwable th30) {
                                                                                        th = th30;
                                                                                        drawable2 = null;
                                                                                    }
                                                                                } else {
                                                                                    i6 = 0;
                                                                                    i3 = 0;
                                                                                }
                                                                                drawable2 = null;
                                                                                try {
                                                                                    bitmap2 = BitmapFactory.decodeStream(fileInputStream2, null, options2);
                                                                                    fileInputStream2.close();
                                                                                } catch (Throwable th31) {
                                                                                    th = th31;
                                                                                    i5 = i6;
                                                                                    z11 = false;
                                                                                    FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                                    z9 = z11;
                                                                                    i2 = i5;
                                                                                    Thread.interrupted();
                                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                                    }
                                                                                    if (bitmap2 != null) {
                                                                                    }
                                                                                    cacheImage2 = this.cacheImage;
                                                                                    if (cacheImage2 != null) {
                                                                                    }
                                                                                    onPostExecute(bitmap2 == null ? new ExtendedBitmapDrawable(bitmap2, i3, i2) : drawable2);
                                                                                }
                                                                            } else {
                                                                                drawable2 = null;
                                                                                i6 = 0;
                                                                                i3 = 0;
                                                                            }
                                                                            if (bitmap2 == null) {
                                                                                try {
                                                                                    RandomAccessFile randomAccessFile9 = new RandomAccessFile(file5, "r");
                                                                                    int length3 = (int) randomAccessFile9.length();
                                                                                    byte[] bArr6 = (byte[]) ImageLoader.bytesLocal.get();
                                                                                    if (bArr6 == null || bArr6.length < length3) {
                                                                                        bArr6 = drawable2;
                                                                                    }
                                                                                    if (bArr6 == null) {
                                                                                        bArr6 = new byte[length3];
                                                                                        ImageLoader.bytesLocal.set(bArr6);
                                                                                    }
                                                                                    randomAccessFile9.readFully(bArr6, 0, length3);
                                                                                    randomAccessFile9.close();
                                                                                    try {
                                                                                        if (secureDocumentKey != null) {
                                                                                            EncryptedFileInputStream.decryptBytesWithKeyFile(bArr6, 0, length3, secureDocumentKey);
                                                                                            i5 = i6;
                                                                                            byte[] computeSHA2563 = Utilities.computeSHA256(bArr6, 0, length3);
                                                                                            if (bArr != null && Arrays.equals(computeSHA2563, bArr)) {
                                                                                                z12 = false;
                                                                                                i7 = bArr6[0] & 255;
                                                                                                length3 -= i7;
                                                                                            }
                                                                                            z12 = true;
                                                                                            i7 = bArr6[0] & 255;
                                                                                            length3 -= i7;
                                                                                        } else {
                                                                                            i5 = i6;
                                                                                            if (z34) {
                                                                                                EncryptedFileInputStream.decryptBytesWithKeyFile(bArr6, 0, length3, this.cacheImage.encryptionKeyPath);
                                                                                            }
                                                                                            z12 = false;
                                                                                            i7 = 0;
                                                                                        }
                                                                                        if (!z12) {
                                                                                            bitmap2 = BitmapFactory.decodeByteArray(bArr6, i7, length3, options2);
                                                                                        }
                                                                                    } catch (Throwable th32) {
                                                                                        th = th32;
                                                                                        try {
                                                                                            FileLog.e(th);
                                                                                            if (bitmap2 != null) {
                                                                                            }
                                                                                        } catch (Throwable th33) {
                                                                                            th = th33;
                                                                                            z11 = false;
                                                                                            FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                                            z9 = z11;
                                                                                            i2 = i5;
                                                                                            Thread.interrupted();
                                                                                            if (BuildVars.LOGS_ENABLED) {
                                                                                            }
                                                                                            if (bitmap2 != null) {
                                                                                            }
                                                                                            cacheImage2 = this.cacheImage;
                                                                                            if (cacheImage2 != null) {
                                                                                            }
                                                                                            onPostExecute(bitmap2 == null ? new ExtendedBitmapDrawable(bitmap2, i3, i2) : drawable2);
                                                                                        }
                                                                                        z9 = z11;
                                                                                        i2 = i5;
                                                                                        Thread.interrupted();
                                                                                        if (BuildVars.LOGS_ENABLED) {
                                                                                        }
                                                                                        if (bitmap2 != null) {
                                                                                        }
                                                                                        cacheImage2 = this.cacheImage;
                                                                                        if (cacheImage2 != null) {
                                                                                        }
                                                                                        onPostExecute(bitmap2 == null ? new ExtendedBitmapDrawable(bitmap2, i3, i2) : drawable2);
                                                                                    }
                                                                                } catch (Throwable th34) {
                                                                                    th = th34;
                                                                                    i5 = i6;
                                                                                }
                                                                            } else {
                                                                                i5 = i6;
                                                                            }
                                                                            if (bitmap2 != null) {
                                                                                if (z6 && (file5.length() == 0 || this.cacheImage.filter == null)) {
                                                                                    file5.delete();
                                                                                }
                                                                                z11 = false;
                                                                            } else {
                                                                                if (this.cacheImage.filter != null) {
                                                                                    float width2 = bitmap2.getWidth();
                                                                                    float height2 = bitmap2.getHeight();
                                                                                    if (!options2.inPurgeable && f4 != 0.0f && width2 != f4 && width2 > 20.0f + f4) {
                                                                                        if (width2 <= height2 || f4 <= f5) {
                                                                                            float f10 = height2 / f5;
                                                                                            if (f10 > 1.0f) {
                                                                                                createScaledBitmap2 = Bitmaps.createScaledBitmap(bitmap2, (int) (width2 / f10), (int) f5, true);
                                                                                                if (bitmap2 != createScaledBitmap2) {
                                                                                                    bitmap2.recycle();
                                                                                                    bitmap2 = createScaledBitmap2;
                                                                                                }
                                                                                            }
                                                                                            createScaledBitmap2 = bitmap2;
                                                                                            if (bitmap2 != createScaledBitmap2) {
                                                                                            }
                                                                                        } else {
                                                                                            float f11 = width2 / f4;
                                                                                            if (f11 > 1.0f) {
                                                                                                createScaledBitmap2 = Bitmaps.createScaledBitmap(bitmap2, (int) f4, (int) (height2 / f11), true);
                                                                                                if (bitmap2 != createScaledBitmap2) {
                                                                                                }
                                                                                            }
                                                                                            createScaledBitmap2 = bitmap2;
                                                                                            if (bitmap2 != createScaledBitmap2) {
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if (bitmap2 != null) {
                                                                                        if (z8) {
                                                                                            Bitmap createScaledBitmap3 = bitmap2.getWidth() * bitmap2.getHeight() > 22500 ? Bitmaps.createScaledBitmap(bitmap2, 100, 100, false) : bitmap2;
                                                                                            z11 = Utilities.needInvert(createScaledBitmap3, options2.inPurgeable ? 0 : 1, createScaledBitmap3.getWidth(), createScaledBitmap3.getHeight(), createScaledBitmap3.getRowBytes()) != 0;
                                                                                            if (createScaledBitmap3 != bitmap2) {
                                                                                                try {
                                                                                                    createScaledBitmap3.recycle();
                                                                                                } catch (Throwable th35) {
                                                                                                    th = th35;
                                                                                                }
                                                                                            }
                                                                                        } else {
                                                                                            z11 = false;
                                                                                        }
                                                                                        if (c2 != 0 && (height2 > 100.0f || width2 > 100.0f)) {
                                                                                            height2 = 80.0f;
                                                                                            bitmap2 = Bitmaps.createScaledBitmap(bitmap2, 80, 80, false);
                                                                                            width2 = 80.0f;
                                                                                        }
                                                                                        if (c2 == 0 || height2 >= 100.0f || width2 >= 100.0f) {
                                                                                            z13 = false;
                                                                                        } else {
                                                                                            if (bitmap2.getConfig() == Bitmap.Config.ARGB_8888) {
                                                                                                Utilities.blurBitmap(bitmap2, 3, options2.inPurgeable ? 0 : 1, bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getRowBytes());
                                                                                            }
                                                                                            z13 = true;
                                                                                        }
                                                                                        if (!z13 && options2.inPurgeable) {
                                                                                            Utilities.pinBitmap(bitmap2);
                                                                                        }
                                                                                    }
                                                                                }
                                                                                z13 = false;
                                                                                z11 = false;
                                                                                if (!z13) {
                                                                                    Utilities.pinBitmap(bitmap2);
                                                                                }
                                                                            }
                                                                            z9 = z11;
                                                                            i2 = i5;
                                                                        }
                                                                        i3 = 0;
                                                                        z11 = false;
                                                                        i5 = 0;
                                                                        FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                        z9 = z11;
                                                                        i2 = i5;
                                                                    } else {
                                                                        drawable2 = null;
                                                                        i3 = 0;
                                                                    }
                                                                    i5 = 0;
                                                                    if (bitmap2 != null) {
                                                                    }
                                                                    z9 = z11;
                                                                    i2 = i5;
                                                                }
                                                            }
                                                            options2.inPreferredConfig = Bitmap.Config.ARGB_8888;
                                                            options2.inDither = false;
                                                            if (l == null) {
                                                            }
                                                            bitmap2 = bitmap3;
                                                            if (bitmap2 != null) {
                                                            }
                                                            i5 = 0;
                                                            if (bitmap2 != null) {
                                                            }
                                                            z9 = z11;
                                                            i2 = i5;
                                                        } finally {
                                                            th = th;
                                                            drawable2 = null;
                                                            while (true) {
                                                                try {
                                                                    try {
                                                                        break;
                                                                    } catch (Throwable th36) {
                                                                        th = th36;
                                                                        bitmap2 = bitmap3;
                                                                        i3 = 0;
                                                                        z11 = false;
                                                                        i5 = 0;
                                                                        FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                        z9 = z11;
                                                                        i2 = i5;
                                                                        Thread.interrupted();
                                                                        if (BuildVars.LOGS_ENABLED) {
                                                                        }
                                                                        if (bitmap2 != null) {
                                                                        }
                                                                        cacheImage2 = this.cacheImage;
                                                                        if (cacheImage2 != null) {
                                                                        }
                                                                        onPostExecute(bitmap2 == null ? new ExtendedBitmapDrawable(bitmap2, i3, i2) : drawable2);
                                                                    }
                                                                } catch (Throwable th37) {
                                                                    th = th37;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            f5 = f;
                                            bitmap3 = bitmap2;
                                            f4 = f3;
                                            ImageLoader.this.lastCacheOutTime = SystemClock.elapsedRealtime();
                                            synchronized (this.sync) {
                                            }
                                        }
                                        Thread.interrupted();
                                        if (BuildVars.LOGS_ENABLED && z34) {
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("Image Loader image is empty = ");
                                            sb.append(bitmap2 != null);
                                            sb.append(" ");
                                            sb.append(file5);
                                            FileLog.e(sb.toString());
                                        }
                                        if (bitmap2 != null && !TextUtils.isEmpty(this.cacheImage.filter) && this.cacheImage.filter.contains("wallpaper")) {
                                            obj2 = this.cacheImage.parentObject;
                                            if (obj2 instanceof TLRPC$WallPaper) {
                                                bitmap2 = applyWallpaperSetting(bitmap2, (TLRPC$WallPaper) obj2);
                                            }
                                        }
                                        cacheImage2 = this.cacheImage;
                                        if ((cacheImage2 != null || (str3 = cacheImage2.filter) == null || !str3.contains("ignoreOrientation")) && (z9 || i3 != 0 || i2 != 0)) {
                                            onPostExecute(bitmap2 == null ? new ExtendedBitmapDrawable(bitmap2, i3, i2) : drawable2);
                                        } else {
                                            onPostExecute(bitmap2 != null ? new BitmapDrawable(bitmap2) : drawable2);
                                            return;
                                        }
                                    } else if (!str.startsWith("http")) {
                                        str2 = null;
                                        l = null;
                                    }
                                    z3 = false;
                                    z4 = false;
                                    BitmapFactory.Options options22 = new BitmapFactory.Options();
                                    options22.inSampleSize = 1;
                                    if (Build.VERSION.SDK_INT < 21) {
                                    }
                                    boolean z3522 = ImageLoader.this.canForce8888;
                                    str5 = this.cacheImage.filter;
                                    if (str5 == null) {
                                    }
                                    f3 = f2;
                                    c2 = c;
                                    z8 = z7;
                                    i = 1;
                                    if (this.cacheImage.type == i) {
                                    }
                                    Thread.interrupted();
                                    if (BuildVars.LOGS_ENABLED) {
                                        StringBuilder sb2 = new StringBuilder();
                                        sb2.append("Image Loader image is empty = ");
                                        sb2.append(bitmap2 != null);
                                        sb2.append(" ");
                                        sb2.append(file5);
                                        FileLog.e(sb2.toString());
                                    }
                                    if (bitmap2 != null) {
                                        obj2 = this.cacheImage.parentObject;
                                        if (obj2 instanceof TLRPC$WallPaper) {
                                        }
                                    }
                                    cacheImage2 = this.cacheImage;
                                    if (cacheImage2 != null) {
                                    }
                                    onPostExecute(bitmap2 == null ? new ExtendedBitmapDrawable(bitmap2, i3, i2) : drawable2);
                                }
                                str2 = null;
                                l = null;
                                z3 = false;
                                z4 = true;
                                BitmapFactory.Options options222 = new BitmapFactory.Options();
                                options222.inSampleSize = 1;
                                if (Build.VERSION.SDK_INT < 21) {
                                }
                                boolean z35222 = ImageLoader.this.canForce8888;
                                str5 = this.cacheImage.filter;
                                if (str5 == null) {
                                }
                                f3 = f2;
                                c2 = c;
                                z8 = z7;
                                i = 1;
                                if (this.cacheImage.type == i) {
                                }
                                Thread.interrupted();
                                if (BuildVars.LOGS_ENABLED) {
                                }
                                if (bitmap2 != null) {
                                }
                                cacheImage2 = this.cacheImage;
                                if (cacheImage2 != null) {
                                }
                                onPostExecute(bitmap2 == null ? new ExtendedBitmapDrawable(bitmap2, i3, i2) : drawable2);
                            }
                        }
                        z2 = false;
                        randomAccessFile2.close();
                        str = this.cacheImage.imageLocation.path;
                        if (str != null) {
                        }
                        str2 = null;
                        l = null;
                        z3 = false;
                        z4 = true;
                        BitmapFactory.Options options2222 = new BitmapFactory.Options();
                        options2222.inSampleSize = 1;
                        if (Build.VERSION.SDK_INT < 21) {
                        }
                        boolean z352222 = ImageLoader.this.canForce8888;
                        str5 = this.cacheImage.filter;
                        if (str5 == null) {
                        }
                        f3 = f2;
                        c2 = c;
                        z8 = z7;
                        i = 1;
                        if (this.cacheImage.type == i) {
                        }
                        Thread.interrupted();
                        if (BuildVars.LOGS_ENABLED) {
                        }
                        if (bitmap2 != null) {
                        }
                        cacheImage2 = this.cacheImage;
                        if (cacheImage2 != null) {
                        }
                        onPostExecute(bitmap2 == null ? new ExtendedBitmapDrawable(bitmap2, i3, i2) : drawable2);
                    }
                    z2 = false;
                    str = this.cacheImage.imageLocation.path;
                    if (str != null) {
                    }
                    str2 = null;
                    l = null;
                    z3 = false;
                    z4 = true;
                    BitmapFactory.Options options22222 = new BitmapFactory.Options();
                    options22222.inSampleSize = 1;
                    if (Build.VERSION.SDK_INT < 21) {
                    }
                    boolean z3522222 = ImageLoader.this.canForce8888;
                    str5 = this.cacheImage.filter;
                    if (str5 == null) {
                    }
                    f3 = f2;
                    c2 = c;
                    z8 = z7;
                    i = 1;
                    if (this.cacheImage.type == i) {
                    }
                    Thread.interrupted();
                    if (BuildVars.LOGS_ENABLED) {
                    }
                    if (bitmap2 != null) {
                    }
                    cacheImage2 = this.cacheImage;
                    if (cacheImage2 != null) {
                    }
                    onPostExecute(bitmap2 == null ? new ExtendedBitmapDrawable(bitmap2, i3, i2) : drawable2);
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
                canvas.drawColor(ColorUtils.setAlphaComponent(tLRPC$WallPaper.settings.background_color, 255));
            } else if (tLRPC$WallPaperSettings2.third_background_color == 0) {
                int alphaComponent = ColorUtils.setAlphaComponent(tLRPC$WallPaperSettings2.background_color, 255);
                int alphaComponent2 = ColorUtils.setAlphaComponent(tLRPC$WallPaper.settings.second_background_color, 255);
                int averageColor = AndroidUtilities.getAverageColor(alphaComponent, alphaComponent2);
                GradientDrawable gradientDrawable = new GradientDrawable(BackgroundGradientDrawable.getGradientOrientation(tLRPC$WallPaper.settings.rotation), new int[]{alphaComponent, alphaComponent2});
                gradientDrawable.setBounds(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
                gradientDrawable.draw(canvas);
                i = averageColor;
            } else {
                int alphaComponent3 = ColorUtils.setAlphaComponent(tLRPC$WallPaperSettings2.background_color, 255);
                int alphaComponent4 = ColorUtils.setAlphaComponent(tLRPC$WallPaper.settings.second_background_color, 255);
                int alphaComponent5 = ColorUtils.setAlphaComponent(tLRPC$WallPaper.settings.third_background_color, 255);
                int i2 = tLRPC$WallPaper.settings.fourth_background_color;
                int alphaComponent6 = i2 == 0 ? 0 : ColorUtils.setAlphaComponent(i2, 255);
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$CacheOutTask$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.CacheOutTask.this.lambda$onPostExecute$1(drawable);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPostExecute$1(Drawable drawable) {
            final String str;
            boolean z = false;
            final Drawable drawable2 = null;
            r2 = null;
            r2 = null;
            r2 = null;
            String str2 = null;
            if (drawable instanceof RLottieDrawable) {
                RLottieDrawable rLottieDrawable = (RLottieDrawable) drawable;
                Drawable drawable3 = (Drawable) ImageLoader.this.lottieMemCache.get(this.cacheImage.key);
                if (drawable3 == null) {
                    ImageLoader.this.lottieMemCache.put(this.cacheImage.key, rLottieDrawable);
                    drawable = rLottieDrawable;
                } else {
                    rLottieDrawable.recycle(false);
                    drawable = drawable3;
                }
                if (drawable != null) {
                    ImageLoader.this.incrementUseCount(this.cacheImage.key);
                    str2 = this.cacheImage.key;
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
                    str2 = this.cacheImage.key;
                }
            } else if (!(drawable instanceof BitmapDrawable)) {
                str = null;
                ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$CacheOutTask$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        ImageLoader.CacheOutTask.this.lambda$onPostExecute$0(drawable2, str);
                    }
                }, this.cacheImage.priority);
            } else {
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
                    str2 = this.cacheImage.key;
                }
            }
            String str3 = str2;
            drawable2 = drawable;
            str = str3;
            ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$CacheOutTask$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.CacheOutTask.this.lambda$onPostExecute$0(drawable2, str);
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
                if (this.key != null) {
                    ImageLoader.this.imageLoadingByKeys.remove(this.key);
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
            if (this.key != null) {
                ImageLoader.this.imageLoadingByKeys.remove(this.key);
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
                imageLoader = Instance;
                if (imageLoader == null) {
                    imageLoader = new ImageLoader();
                    Instance = imageLoader;
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
        int min = Math.min(z ? 30 : 15, memoryClass / 7) * 1024 * 1024;
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
            ApplicationLoader.applicationContext.registerReceiver(r0, intentFilter);
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
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda1
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
            dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda4
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda2
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
            dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda5
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda3
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda6
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda7
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
                ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda8
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
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda0
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
                CacheImage cacheImage2 = (CacheImage) ImageLoader.this.imageLoadingByKeys.get(str2);
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
            return Math.max(animatedFileDrawable.getIntrinsicHeight() * bitmapDrawable.getIntrinsicWidth() * 4 * 3, animatedFileDrawable.getRenderingHeight() * animatedFileDrawable.getRenderingWidth() * 4 * 3);
        } else if (bitmapDrawable instanceof RLottieDrawable) {
            return bitmapDrawable.getIntrinsicWidth() * bitmapDrawable.getIntrinsicHeight() * 4 * 2;
        } else {
            return bitmapDrawable.getBitmap().getByteCount();
        }
    }

    public void checkMediaPaths() {
        checkMediaPaths(null);
    }

    public void checkMediaPaths(final Runnable runnable) {
        this.cacheOutQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.lambda$checkMediaPaths$1(runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkMediaPaths$1(final Runnable runnable) {
        final SparseArray<File> createMediaPaths = createMediaPaths();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda0
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
        if (file.exists()) {
            if (file2.exists() || file2.mkdir()) {
                try {
                    Stream convert = $r8$wrapper$java$util$stream$Stream$-V-WRP.convert(Files.list(file.toPath()));
                    convert.forEach(new Consumer() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda14
                        @Override // j$.util.function.Consumer
                        public final void accept(Object obj) {
                            ImageLoader.lambda$moveDirectory$2(file2, (java.nio.file.Path) obj);
                        }

                        @Override // j$.util.function.Consumer
                        public /* synthetic */ Consumer andThen(Consumer consumer) {
                            return Consumer.-CC.$default$andThen(this, consumer);
                        }
                    });
                    convert.close();
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$moveDirectory$2(File file, java.nio.file.Path path) {
        File file2 = new File(file, path.getFileName().toString());
        if (Files.isDirectory(path, new LinkOption[0])) {
            moveDirectory(path.toFile(), file2);
            return;
        }
        try {
            Files.move(path, file2.toPath(), new CopyOption[0]);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:68:0x0163, code lost:
        if (r2.mkdirs() != false) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0169, code lost:
        if (r2.canWrite() == false) goto L54;
     */
    /* JADX WARN: Removed duplicated region for block: B:159:0x037b A[Catch: Exception -> 0x038e, TRY_LEAVE, TryCatch #3 {Exception -> 0x038e, blocks: (B:153:0x035e, B:155:0x036c, B:157:0x0372, B:159:0x037b), top: B:187:0x035e, outer: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:169:0x03af A[Catch: Exception -> 0x03c2, TRY_LEAVE, TryCatch #12 {Exception -> 0x03c2, blocks: (B:163:0x0392, B:165:0x03a0, B:167:0x03a6, B:169:0x03af), top: B:205:0x0392, outer: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:201:0x01eb A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:213:0x01d9 A[EDGE_INSN: B:213:0x01d9->B:87:0x01d9 ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x01af A[Catch: Exception -> 0x03d4, TryCatch #4 {Exception -> 0x03d4, blocks: (B:12:0x0053, B:14:0x005f, B:16:0x006d, B:19:0x0075, B:21:0x007c, B:24:0x00aa, B:25:0x00ad, B:27:0x00b9, B:30:0x00c2, B:32:0x00c5, B:36:0x00e6, B:35:0x00ca, B:37:0x00e9, B:54:0x012a, B:73:0x0191, B:75:0x019c, B:77:0x01a4, B:79:0x01af, B:81:0x01b7, B:83:0x01bf, B:85:0x01cb, B:86:0x01d6, B:87:0x01d9, B:149:0x0353, B:139:0x0312, B:129:0x02d1, B:119:0x0290, B:109:0x0250, B:151:0x0358, B:172:0x03c3, B:162:0x038f, B:176:0x03d0, B:99:0x021c, B:53:0x0127, B:55:0x0139, B:57:0x0141, B:62:0x0152, B:64:0x0158, B:69:0x0165, B:71:0x016b, B:67:0x015f, B:72:0x018a, B:173:0x03c7, B:175:0x03cb, B:120:0x0293, B:122:0x02a5, B:124:0x02ac, B:126:0x02bb, B:110:0x0253, B:112:0x0265, B:114:0x026b, B:116:0x027a, B:100:0x021f, B:102:0x022f, B:104:0x0235, B:106:0x023c, B:153:0x035e, B:155:0x036c, B:157:0x0372, B:159:0x037b, B:140:0x0315, B:142:0x0327, B:144:0x032e, B:146:0x033d, B:130:0x02d4, B:132:0x02e6, B:134:0x02ed, B:136:0x02fc, B:90:0x01eb, B:92:0x01fb, B:94:0x0201, B:96:0x0208, B:163:0x0392, B:165:0x03a0, B:167:0x03a6, B:169:0x03af), top: B:189:0x0053, inners: #0, #1, #2, #3, #6, #7, #10, #12 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public SparseArray<File> createMediaPaths() {
        File file;
        File file2;
        File file3;
        int size;
        int i;
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
                        int size2 = rootDirs.size();
                        int i2 = 0;
                        while (true) {
                            if (i2 >= size2) {
                                break;
                            }
                            File file5 = rootDirs.get(i2);
                            FileLog.d("root dir " + i2 + " " + file5);
                            if (file5.getAbsolutePath().startsWith(SharedConfig.storageCacheDir)) {
                                externalStorageDirectory = file5;
                                break;
                            }
                            i2++;
                        }
                    }
                    if (!externalStorageDirectory.getAbsolutePath().startsWith(SharedConfig.storageCacheDir) && (externalFilesDirs = ApplicationLoader.applicationContext.getExternalFilesDirs(null)) != null) {
                        for (int i3 = 0; i3 < externalFilesDirs.length; i3++) {
                            if (externalFilesDirs[i3] != null) {
                                FileLog.d("dirsDebug " + i3 + " " + externalFilesDirs[i3]);
                            }
                        }
                    }
                }
                FileLog.d("external storage = " + externalStorageDirectory);
                if (Build.VERSION.SDK_INT >= 30) {
                    try {
                        if (ApplicationLoader.applicationContext.getExternalMediaDirs().length > 0) {
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
                                if (Build.VERSION.SDK_INT >= 19) {
                                    ArrayList<File> dataDirs = AndroidUtilities.getDataDirs();
                                    size = dataDirs.size();
                                    i = 0;
                                    while (true) {
                                        if (i >= size) {
                                        }
                                        i++;
                                    }
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
                    if (!(!TextUtils.isEmpty(SharedConfig.storageCacheDir) && externalStorageDirectory.getAbsolutePath().startsWith(SharedConfig.storageCacheDir))) {
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
                if (Build.VERSION.SDK_INT >= 19 && !this.telegramPath.isDirectory()) {
                    ArrayList<File> dataDirs2 = AndroidUtilities.getDataDirs();
                    size = dataDirs2.size();
                    i = 0;
                    while (true) {
                        if (i >= size) {
                            break;
                        }
                        File file6 = dataDirs2.get(i);
                        if (file6 != null && !TextUtils.isEmpty(SharedConfig.storageCacheDir) && file6.getAbsolutePath().startsWith(SharedConfig.storageCacheDir)) {
                            File file7 = new File(file6, "Telegram");
                            this.telegramPath = file7;
                            file7.mkdirs();
                            break;
                        }
                        i++;
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

    private File getPublicStorageDir() {
        File file = ApplicationLoader.applicationContext.getExternalMediaDirs()[0];
        if (!TextUtils.isEmpty(SharedConfig.storageCacheDir)) {
            for (int i = 0; i < ApplicationLoader.applicationContext.getExternalMediaDirs().length; i++) {
                File file2 = ApplicationLoader.applicationContext.getExternalMediaDirs()[i];
                if (file2 != null && file2.getPath().startsWith(SharedConfig.storageCacheDir)) {
                    file = ApplicationLoader.applicationContext.getExternalMediaDirs()[i];
                }
            }
        }
        return file;
    }

    private boolean canMoveFiles(File file, File file2, int i) {
        File file3;
        File file4;
        byte[] bArr;
        RandomAccessFile randomAccessFile;
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
            randomAccessFile2 = randomAccessFile;
            if (randomAccessFile2 != null) {
                try {
                    randomAccessFile2.close();
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
        if (jArr[1] == 0) {
            return Float.valueOf(0.0f);
        }
        return Float.valueOf(Math.min(1.0f, ((float) jArr[0]) / ((float) jArr[1])));
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
            boolean z = false;
            if (bitmapDrawable2 != null && bitmapDrawable2.getBitmap() != null && bitmapDrawable.getBitmap() != null) {
                Bitmap bitmap = bitmapDrawable2.getBitmap();
                Bitmap bitmap2 = bitmapDrawable.getBitmap();
                if (bitmap.getWidth() > bitmap2.getWidth() || bitmap.getHeight() > bitmap2.getHeight()) {
                    z = true;
                }
            }
            if (!z) {
                this.ignoreRemoval = str;
                lruCache.remove(str);
                lruCache.put(str2, bitmapDrawable);
                this.ignoreRemoval = null;
            } else {
                lruCache.remove(str);
            }
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
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda12
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
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda13
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

    public void replaceImageInCache(final String str, final String str2, final ImageLocation imageLocation, boolean z) {
        if (z) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda9
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
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda7
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
        Runnable runnable = new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.lambda$createLoadOperationForImageReceiver$7(i3, str2, str, i6, imageReceiver, i4, str4, i2, imageLocation, z, parentObject, currentAccount, qualityThumbDocument, isNeedsQualityThumb, isShouldGenerateQualityThumb, str3, i, j);
            }
        };
        this.imageLoadQueue.postRunnable(runnable, imageReceiver.getFileLoadingPriority() == 0 ? 0L : 1L);
        imageReceiver.addLoadingImageRunnable(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:153:0x0316, code lost:
        if (r7 == false) goto L153;
     */
    /* JADX WARN: Code restructure failed: missing block: B:197:0x03ee, code lost:
        if (r7.equals(r8) != false) goto L184;
     */
    /* JADX WARN: Code restructure failed: missing block: B:240:0x04c8, code lost:
        if (r10.exists() == false) goto L207;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x01b9, code lost:
        if (r8.exists() == false) goto L287;
     */
    /* JADX WARN: Removed duplicated region for block: B:206:0x0426  */
    /* JADX WARN: Removed duplicated region for block: B:211:0x0437  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x01ae  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x01be  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x01c1  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x01c4  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0215  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$createLoadOperationForImageReceiver$7(int i, String str, String str2, int i2, ImageReceiver imageReceiver, int i3, String str3, int i4, ImageLocation imageLocation, boolean z, Object obj, int i5, TLRPC$Document tLRPC$Document, boolean z2, boolean z3, String str4, int i6, long j) {
        String str5;
        int i7;
        boolean z4;
        String str6;
        String str7;
        boolean z5;
        boolean z6;
        int i8;
        File file;
        String str8;
        boolean z7;
        int i9;
        File file2;
        File file3;
        File file4;
        File file5;
        int i10;
        String str9;
        boolean z8;
        int i11;
        boolean z9;
        String str10;
        String str11;
        long j2;
        String str12;
        String str13;
        File file6;
        File file7;
        String str14;
        String str15;
        int i12;
        boolean z10;
        int i13;
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
                    z4 = true;
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
                    z4 = true;
                } else {
                    cacheImage = cacheImage3;
                    cacheImage2 = cacheImage4;
                    str5 = str4;
                    i7 = 0;
                    cacheImage5.removeImageReceiver(imageReceiver);
                }
                if (z4 && cacheImage2 != null) {
                    cacheImage2.addImageReceiver(imageReceiver, str2, str3, i4, i3);
                    z4 = true;
                }
                if (!z4 && cacheImage != null) {
                    cacheImage.addImageReceiver(imageReceiver, str2, str3, i4, i3);
                    z4 = true;
                }
            } else {
                cacheImage = cacheImage3;
                cacheImage2 = cacheImage4;
                str5 = str4;
                i7 = 0;
            }
            z4 = false;
            if (z4) {
            }
            if (!z4) {
                cacheImage.addImageReceiver(imageReceiver, str2, str3, i4, i3);
                z4 = true;
            }
        } else {
            str5 = str4;
            i7 = 0;
            z4 = false;
        }
        if (z4) {
            return;
        }
        String str16 = imageLocation.path;
        if (str16 != null) {
            if (str16.startsWith("http") || str16.startsWith("athumb")) {
                z5 = false;
                file = null;
            } else if (str16.startsWith("thumb://")) {
                int indexOf = str16.indexOf(":", 8);
                if (indexOf >= 0) {
                    file = new File(str16.substring(indexOf + 1));
                    z5 = true;
                }
                file = null;
                z5 = true;
            } else {
                if (str16.startsWith("vthumb://")) {
                    int indexOf2 = str16.indexOf(":", 9);
                    if (indexOf2 >= 0) {
                        file = new File(str16.substring(indexOf2 + 1));
                    }
                    file = null;
                } else {
                    file = new File(str16);
                }
                z5 = true;
            }
            str6 = "athumb";
            z6 = false;
            i8 = 2;
            str7 = str3;
        } else {
            if (i == 0 && z) {
                if (obj instanceof MessageObject) {
                    MessageObject messageObject = (MessageObject) obj;
                    TLRPC$Document document = messageObject.getDocument();
                    String str17 = messageObject.messageOwner.attachPath;
                    file2 = FileLoader.getInstance(i5).getPathToMessage(messageObject.messageOwner);
                    str8 = str17;
                    i9 = messageObject.getMediaType();
                    tLRPC$Document2 = document;
                    z7 = false;
                } else if (tLRPC$Document2 != null) {
                    File pathToAttach = FileLoader.getInstance(i5).getPathToAttach(tLRPC$Document2, true);
                    int i14 = MessageObject.isVideoDocument(tLRPC$Document) ? 2 : 3;
                    file2 = pathToAttach;
                    str8 = null;
                    i9 = i14;
                    z7 = true;
                } else {
                    str8 = null;
                    z7 = false;
                    i9 = 0;
                    file2 = null;
                    tLRPC$Document2 = null;
                }
                if (tLRPC$Document2 != null) {
                    if (z2) {
                        File directory = FileLoader.getDirectory(4);
                        str6 = "athumb";
                        StringBuilder sb = new StringBuilder();
                        file3 = file2;
                        sb.append("q_");
                        sb.append(tLRPC$Document2.dc_id);
                        sb.append("_");
                        sb.append(tLRPC$Document2.id);
                        sb.append(".jpg");
                        file4 = new File(directory, sb.toString());
                        if (file4.exists()) {
                            z6 = true;
                            if (!TextUtils.isEmpty(str8)) {
                                file5 = new File(str8);
                            }
                            file5 = null;
                            File file8 = file5 != null ? file3 : file5;
                            if (file4 != null) {
                                String attachFileName = FileLoader.getAttachFileName(tLRPC$Document2);
                                ThumbGenerateInfo thumbGenerateInfo = this.waitingForQualityThumb.get(attachFileName);
                                if (thumbGenerateInfo == null) {
                                    thumbGenerateInfo = new ThumbGenerateInfo();
                                    thumbGenerateInfo.parentDocument = tLRPC$Document2;
                                    thumbGenerateInfo.filter = str3;
                                    thumbGenerateInfo.big = z7;
                                    this.waitingForQualityThumb.put(attachFileName, thumbGenerateInfo);
                                }
                                if (!thumbGenerateInfo.imageReceiverArray.contains(imageReceiver)) {
                                    thumbGenerateInfo.imageReceiverArray.add(imageReceiver);
                                    thumbGenerateInfo.imageReceiverGuidsArray.add(Integer.valueOf(i3));
                                }
                                this.waitingForQualityThumbByTag.put(i2, attachFileName);
                                if (file8.exists() && z3) {
                                    generateThumb(i9, file8, thumbGenerateInfo);
                                    return;
                                }
                                return;
                            }
                            str7 = str3;
                            file = file4;
                            z5 = true;
                            i8 = 2;
                        }
                    } else {
                        str6 = "athumb";
                        file3 = file2;
                    }
                    z6 = false;
                    file4 = null;
                    if (!TextUtils.isEmpty(str8)) {
                    }
                    file5 = null;
                    if (file5 != null) {
                    }
                    if (file4 != null) {
                    }
                } else {
                    str6 = "athumb";
                    str7 = str3;
                    z5 = true;
                }
            } else {
                str6 = "athumb";
                str7 = str3;
                z5 = false;
            }
            z6 = false;
            i8 = 2;
            file = null;
        }
        if (i != i8) {
            boolean isEncrypted = imageLocation.isEncrypted();
            CacheImage cacheImage6 = new CacheImage();
            cacheImage6.priority = imageReceiver.getFileLoadingPriority() == 0 ? 0 : 1;
            if (!z) {
                if (imageLocation.imageType != i8) {
                    if (MessageObject.isGifDocument(imageLocation.webFile) || MessageObject.isGifDocument(imageLocation.document) || MessageObject.isRoundVideoDocument(imageLocation.document) || MessageObject.isVideoSticker(imageLocation.document)) {
                        i8 = 2;
                    } else {
                        String str18 = imageLocation.path;
                        if (str18 != null && !str18.startsWith("vthumb") && !str18.startsWith("thumb")) {
                            String httpUrlExtension = getHttpUrlExtension(str18, "jpg");
                            if (httpUrlExtension.equals("webm") || httpUrlExtension.equals("mp4") || httpUrlExtension.equals("gif")) {
                                cacheImage6.imageType = 2;
                            } else if ("tgs".equals(str5)) {
                                cacheImage6.imageType = 1;
                            }
                        }
                    }
                }
                cacheImage6.imageType = i8;
            }
            if (file == null) {
                TLRPC$PhotoSize tLRPC$PhotoSize = imageLocation.photoSize;
                boolean z11 = z5;
                if ((tLRPC$PhotoSize instanceof TLRPC$TL_photoStrippedSize) || (tLRPC$PhotoSize instanceof TLRPC$TL_photoPathSize)) {
                    i10 = i6;
                    str11 = AUTOPLAY_FILTER;
                    str9 = str7;
                    z6 = z6;
                    j2 = 0;
                } else {
                    SecureDocument secureDocument = imageLocation.secureDocument;
                    if (secureDocument != null) {
                        cacheImage6.secureDocument = secureDocument;
                        boolean z12 = secureDocument.secureFile.dc_id == Integer.MIN_VALUE;
                        file = new File(FileLoader.getDirectory(4), str);
                        i10 = i6;
                        z8 = z12;
                        str9 = str7;
                        j2 = 0;
                        str11 = AUTOPLAY_FILTER;
                    } else {
                        boolean z13 = z6;
                        if (AUTOPLAY_FILTER.equals(str7) || isAnimatedAvatar(str7)) {
                            i10 = i6;
                            str12 = AUTOPLAY_FILTER;
                            str13 = "_";
                        } else {
                            i10 = i6;
                            str12 = AUTOPLAY_FILTER;
                            if (i10 == 0) {
                                str13 = "_";
                                if (j > 0) {
                                    if (imageLocation.path == null) {
                                    }
                                }
                            }
                            File file9 = new File(FileLoader.getDirectory(4), str);
                            if (file9.exists()) {
                                z10 = true;
                            } else {
                                if (i10 == 2) {
                                    file9 = new File(FileLoader.getDirectory(4), str + ".enc");
                                }
                                z10 = z13;
                            }
                            TLRPC$Document tLRPC$Document3 = imageLocation.document;
                            if (tLRPC$Document3 != null) {
                                if (tLRPC$Document3 instanceof DocumentObject.ThemeDocument) {
                                    if (((DocumentObject.ThemeDocument) tLRPC$Document3).wallpaper == null) {
                                        i13 = 5;
                                        z8 = true;
                                    } else {
                                        z8 = z11;
                                        i13 = 5;
                                    }
                                    cacheImage6.imageType = i13;
                                    str11 = str12;
                                    z6 = z10;
                                    file = file9;
                                    str9 = str7;
                                    j2 = 0;
                                } else if ("application/x-tgsdice".equals(tLRPC$Document3.mime_type)) {
                                    cacheImage6.imageType = 1;
                                    str11 = str12;
                                    z6 = z10;
                                    file = file9;
                                    str9 = str7;
                                    j2 = 0;
                                } else if ("application/x-tgsticker".equals(imageLocation.document.mime_type)) {
                                    cacheImage6.imageType = 1;
                                } else if ("application/x-tgwallpattern".equals(imageLocation.document.mime_type)) {
                                    cacheImage6.imageType = 3;
                                } else if (FileLoader.getDocumentFileName(imageLocation.document).endsWith(".svg")) {
                                    cacheImage6.imageType = 3;
                                }
                            }
                            z8 = z11;
                            str11 = str12;
                            z6 = z10;
                            file = file9;
                            str9 = str7;
                            j2 = 0;
                        }
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
                                str14 = str12;
                            } else {
                                str14 = str12;
                            }
                            if (!file7.exists()) {
                                File directory2 = FileLoader.getDirectory(4);
                                StringBuilder sb2 = new StringBuilder();
                                str15 = str14;
                                sb2.append(tLRPC$Document4.dc_id);
                                sb2.append(str13);
                                sb2.append(tLRPC$Document4.id);
                                sb2.append(".temp");
                                file7 = new File(directory2, sb2.toString());
                                if (!(tLRPC$Document4 instanceof DocumentObject.ThemeDocument)) {
                                    if (((DocumentObject.ThemeDocument) tLRPC$Document4).wallpaper == null) {
                                        i12 = 5;
                                        z8 = true;
                                    } else {
                                        z8 = z11;
                                        i12 = 5;
                                    }
                                    cacheImage6.imageType = i12;
                                } else if ("application/x-tgsdice".equals(imageLocation.document.mime_type)) {
                                    cacheImage6.imageType = 1;
                                    z8 = true;
                                } else {
                                    if ("application/x-tgsticker".equals(tLRPC$Document4.mime_type)) {
                                        cacheImage6.imageType = 1;
                                    } else if ("application/x-tgwallpattern".equals(tLRPC$Document4.mime_type)) {
                                        cacheImage6.imageType = 3;
                                    } else if (FileLoader.getDocumentFileName(imageLocation.document).endsWith(".svg")) {
                                        cacheImage6.imageType = 3;
                                    }
                                    z8 = z11;
                                }
                                file = file7;
                                j2 = tLRPC$Document4.size;
                                str9 = str3;
                                z6 = z13;
                                str11 = str15;
                            }
                            str15 = str14;
                            if (!(tLRPC$Document4 instanceof DocumentObject.ThemeDocument)) {
                            }
                            file = file7;
                            j2 = tLRPC$Document4.size;
                            str9 = str3;
                            z6 = z13;
                            str11 = str15;
                        } else {
                            String str19 = str13;
                            if (imageLocation.webFile != null) {
                                file = new File(FileLoader.getDirectory(3), str);
                                z8 = z11;
                                str9 = str3;
                                z6 = z13;
                                str11 = str12;
                            } else {
                                if (i10 == 1) {
                                    file6 = new File(FileLoader.getDirectory(4), str);
                                } else {
                                    file6 = new File(FileLoader.getDirectory(i7), str);
                                }
                                str9 = str3;
                                file = file6;
                                if (isAnimatedAvatar(str9)) {
                                    str11 = str12;
                                } else {
                                    str11 = str12;
                                    if (str11.equals(str9)) {
                                        if (imageLocation.location != null) {
                                        }
                                    }
                                    z8 = z11;
                                    z6 = z13;
                                }
                                file = new File(FileLoader.getDirectory(4), imageLocation.location.volume_id + str19 + imageLocation.location.local_id + ".temp");
                                z8 = z11;
                                z6 = z13;
                            }
                            j2 = 0;
                        }
                    }
                    if (!hasAutoplayFilter(str3) || isAnimatedAvatar(str9)) {
                        cacheImage6.imageType = 2;
                        cacheImage6.size = j2;
                        cacheImage6.isPFrame = isPFrame(str9);
                        if (!str11.equals(str9) || isAnimatedAvatar(str9)) {
                            i11 = i4;
                            z9 = z6;
                            z8 = true;
                        }
                    }
                    i11 = i4;
                    z9 = z6;
                }
                z8 = true;
                if (!hasAutoplayFilter(str3)) {
                }
                cacheImage6.imageType = 2;
                cacheImage6.size = j2;
                cacheImage6.isPFrame = isPFrame(str9);
                if (!str11.equals(str9)) {
                }
                i11 = i4;
                z9 = z6;
                z8 = true;
            } else {
                i10 = i6;
                str9 = str7;
                z8 = z5;
                i11 = i4;
                z9 = z6;
            }
            cacheImage6.type = i11;
            cacheImage6.key = str2;
            cacheImage6.cacheType = i10;
            cacheImage6.filter = str9;
            cacheImage6.imageLocation = imageLocation;
            cacheImage6.ext = str4;
            cacheImage6.currentAccount = i5;
            cacheImage6.parentObject = obj;
            int i15 = imageLocation.imageType;
            if (i15 != 0) {
                cacheImage6.imageType = i15;
            }
            if (i10 == 2) {
                File internalCacheDir = FileLoader.getInternalCacheDir();
                StringBuilder sb3 = new StringBuilder();
                str10 = str;
                sb3.append(str10);
                sb3.append(".enc.key");
                cacheImage6.encryptionKeyPath = new File(internalCacheDir, sb3.toString());
            } else {
                str10 = str;
            }
            String str20 = str6;
            cacheImage6.addImageReceiver(imageReceiver, str2, str3, i4, i3);
            if (z8 || z9 || file.exists()) {
                cacheImage6.finalFilePath = file;
                cacheImage6.imageLocation = imageLocation;
                cacheImage6.cacheTask = new CacheOutTask(cacheImage6);
                this.imageLoadingByKeys.put(str2, cacheImage6);
                if (i != 0) {
                    this.cacheThumbOutQueue.postRunnable(cacheImage6.cacheTask);
                    return;
                } else {
                    cacheImage6.runningTask = this.cacheOutQueue.postRunnable(cacheImage6.cacheTask, cacheImage6.priority);
                    return;
                }
            }
            cacheImage6.url = str10;
            this.imageLoadingByUrl.put(str10, cacheImage6);
            if (cacheImage6.isPFrame) {
                this.imageLoadingByUrlPframe.put(str10, cacheImage6);
            }
            String str21 = imageLocation.path;
            if (str21 != null) {
                String MD5 = Utilities.MD5(str21);
                cacheImage6.tempFilePath = new File(FileLoader.getDirectory(4), MD5 + "_temp.jpg");
                cacheImage6.finalFilePath = file;
                if (imageLocation.path.startsWith(str20)) {
                    ArtworkLoadTask artworkLoadTask = new ArtworkLoadTask(cacheImage6);
                    cacheImage6.artworkTask = artworkLoadTask;
                    this.artworkTasks.add(artworkLoadTask);
                    runArtworkTasks(false);
                    return;
                }
                HttpImageTask httpImageTask = new HttpImageTask(cacheImage6, j);
                cacheImage6.httpTask = httpImageTask;
                this.httpTasks.add(httpImageTask);
                runHttpTasks(false);
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
                this.forceLoadingImages.put(cacheImage6.key, 0);
            }
        }
    }

    public void preloadArtwork(final String str) {
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda6
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

    /* JADX WARN: Removed duplicated region for block: B:103:0x01a2  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x01af  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x01bf  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x01cb  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x01da  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x01dd  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x01e0  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x01e4  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x01e7  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x01fe  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x03b6  */
    /* JADX WARN: Removed duplicated region for block: B:235:0x0425  */
    /* JADX WARN: Removed duplicated region for block: B:238:0x042c A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:241:0x0442 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:245:0x045d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:250:0x047d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:255:0x049b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:259:0x04b5  */
    /* JADX WARN: Removed duplicated region for block: B:267:0x04f5  */
    /* JADX WARN: Removed duplicated region for block: B:269:0x04fc  */
    /* JADX WARN: Removed duplicated region for block: B:284:0x0566  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00a7 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00bf  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x00f1  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x00f4  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0115  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0192  */
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
        ImageLocation imageLocation5;
        String str13;
        int i2;
        String str14;
        ImageLocation imageLocation6;
        boolean z6;
        ImageLocation imageLocation7;
        ImageLocation imageLocation8;
        String str15;
        String str16;
        String str17;
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
                    ImageLocation imageLocation9 = imageReceiver.getImageLocation();
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
                String str18 = null;
                if (imageLocation2 == null) {
                }
                ImageLocation imageLocation10 = imageLocation2;
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
                ImageLocation imageLocation11 = imageLocation10;
                imageLocation3 = mediaLocation;
                str2 = null;
                str3 = null;
                String str19 = null;
                String str20 = null;
                i = 0;
                boolean z7 = false;
                while (i < 2) {
                }
                boolean z8 = z4;
                str4 = thumbFilter;
                imageLocation4 = imageLocation11;
                str5 = imageFilter;
                int i3 = newGuid;
                if (thumbLocation != null) {
                }
                if (str3 != null) {
                }
                if (str2 != null) {
                }
                str7 = str5;
                if (str6 != null) {
                }
                str8 = str4;
                String str21 = str6;
                if (imageReceiver.getUniqKeyPrefix() != null) {
                }
                str9 = str2;
                if (imageReceiver.getUniqKeyPrefix() != null) {
                }
                str10 = str3;
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
                    ImageLocation imageLocation92 = imageReceiver.getImageLocation();
                    findInPreloadImageReceivers = findInPreloadImageReceivers(imageKey, list);
                    if (findInPreloadImageReceivers == null && useLottieMemCache(imageLocation92, imageKey)) {
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
                                    String str182 = null;
                                    String str22 = (imageLocation2 == null && imageLocation2.imageType == 2) ? "mp4" : null;
                                    ImageLocation imageLocation102 = imageLocation2;
                                    if (mediaLocation != null || mediaLocation.imageType != 2) {
                                        str = null;
                                    }
                                    ext = imageReceiver.getExt();
                                    if (ext == null) {
                                        ext = "jpg";
                                    }
                                    String str23 = str22 == null ? ext : str22;
                                    String str24 = str == null ? ext : str;
                                    ImageLocation imageLocation112 = imageLocation102;
                                    imageLocation3 = mediaLocation;
                                    str2 = null;
                                    str3 = null;
                                    String str192 = null;
                                    String str202 = null;
                                    i = 0;
                                    boolean z72 = false;
                                    while (i < 2) {
                                        if (i == 0) {
                                            imageLocation6 = imageLocation112;
                                            i2 = newGuid;
                                            str14 = str23;
                                        } else {
                                            i2 = newGuid;
                                            str14 = str24;
                                            imageLocation6 = imageLocation3;
                                        }
                                        if (imageLocation6 == null) {
                                            z6 = z4;
                                            imageLocation8 = imageLocation112;
                                        } else {
                                            z6 = z4;
                                            if (imageLocation3 != null) {
                                                imageLocation8 = imageLocation112;
                                                imageLocation7 = imageLocation3;
                                            } else {
                                                imageLocation7 = imageLocation112;
                                                imageLocation8 = imageLocation7;
                                            }
                                            String key = imageLocation6.getKey(parentObject, imageLocation7, false);
                                            if (key != null) {
                                                str15 = thumbFilter2;
                                                String key2 = imageLocation6.getKey(parentObject, imageLocation3 != null ? imageLocation3 : imageLocation8, true);
                                                if (imageLocation6.path != null) {
                                                    key2 = key2 + "." + getHttpUrlExtension(imageLocation6.path, "jpg");
                                                    str16 = imageFilter2;
                                                } else {
                                                    TLRPC$PhotoSize tLRPC$PhotoSize = imageLocation6.photoSize;
                                                    str16 = imageFilter2;
                                                    if ((tLRPC$PhotoSize instanceof TLRPC$TL_photoStrippedSize) || (tLRPC$PhotoSize instanceof TLRPC$TL_photoPathSize)) {
                                                        key2 = key2 + "." + str14;
                                                    } else if (imageLocation6.location != null) {
                                                        String str25 = key2 + "." + str14;
                                                        if (imageReceiver.getExt() == null) {
                                                            TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated = imageLocation6.location;
                                                            if (tLRPC$TL_fileLocationToBeDeprecated.key == null) {
                                                                str17 = str25;
                                                                if (tLRPC$TL_fileLocationToBeDeprecated.volume_id != -2147483648L || tLRPC$TL_fileLocationToBeDeprecated.local_id >= 0) {
                                                                    key2 = str17;
                                                                }
                                                                key2 = str17;
                                                                z72 = true;
                                                            }
                                                        }
                                                        str17 = str25;
                                                        key2 = str17;
                                                        z72 = true;
                                                    } else {
                                                        WebFile webFile = imageLocation6.webFile;
                                                        if (webFile != null) {
                                                            String mimeTypePart = FileLoader.getMimeTypePart(webFile.mime_type);
                                                            key2 = key2 + "." + getHttpUrlExtension(imageLocation6.webFile.url, mimeTypePart);
                                                        } else if (imageLocation6.secureDocument != null) {
                                                            key2 = key2 + "." + str14;
                                                        } else if (imageLocation6.document != null) {
                                                            if (i == 0 && z5) {
                                                                key = "q_" + key;
                                                            }
                                                            String documentFileName = FileLoader.getDocumentFileName(imageLocation6.document);
                                                            int lastIndexOf = documentFileName.lastIndexOf(46);
                                                            String str26 = "";
                                                            String substring = lastIndexOf == -1 ? "" : documentFileName.substring(lastIndexOf);
                                                            if (substring.length() > 1) {
                                                                str26 = substring;
                                                            } else if ("video/mp4".equals(imageLocation6.document.mime_type)) {
                                                                str26 = ".mp4";
                                                            } else if ("video/x-matroska".equals(imageLocation6.document.mime_type)) {
                                                                str26 = ".mkv";
                                                            }
                                                            key2 = key2 + str26;
                                                            z72 = (MessageObject.isVideoDocument(imageLocation6.document) || MessageObject.isGifDocument(imageLocation6.document) || MessageObject.isRoundVideoDocument(imageLocation6.document) || MessageObject.canPreviewDocument(imageLocation6.document)) ? false : true;
                                                        }
                                                    }
                                                }
                                                if (i == 0) {
                                                    str2 = key;
                                                    str192 = key2;
                                                } else {
                                                    str3 = key;
                                                    str202 = key2;
                                                }
                                                if (imageLocation6 == thumbLocation) {
                                                    if (i == 0) {
                                                        str2 = null;
                                                        imageLocation112 = null;
                                                        str192 = null;
                                                        i++;
                                                        newGuid = i2;
                                                        z4 = z6;
                                                        thumbFilter2 = str15;
                                                        imageFilter2 = str16;
                                                    } else {
                                                        str3 = null;
                                                        str202 = null;
                                                        imageLocation3 = null;
                                                    }
                                                }
                                                imageLocation112 = imageLocation8;
                                                i++;
                                                newGuid = i2;
                                                z4 = z6;
                                                thumbFilter2 = str15;
                                                imageFilter2 = str16;
                                            }
                                        }
                                        str15 = thumbFilter2;
                                        str16 = imageFilter2;
                                        imageLocation112 = imageLocation8;
                                        i++;
                                        newGuid = i2;
                                        z4 = z6;
                                        thumbFilter2 = str15;
                                        imageFilter2 = str16;
                                    }
                                    boolean z82 = z4;
                                    str4 = thumbFilter2;
                                    imageLocation4 = imageLocation112;
                                    str5 = imageFilter2;
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
                                        str182 = key4;
                                        str6 = key3;
                                    } else {
                                        str6 = null;
                                    }
                                    if (str3 != null && mediaFilter2 != null) {
                                        str3 = str3 + "@" + mediaFilter2;
                                    }
                                    if (str2 != null || str5 == null) {
                                        str7 = str5;
                                    } else {
                                        StringBuilder sb = new StringBuilder();
                                        sb.append(str2);
                                        sb.append("@");
                                        str7 = str5;
                                        sb.append(str7);
                                        str2 = sb.toString();
                                    }
                                    if (str6 != null || str4 == null) {
                                        str8 = str4;
                                    } else {
                                        StringBuilder sb2 = new StringBuilder();
                                        sb2.append(str6);
                                        sb2.append("@");
                                        str8 = str4;
                                        sb2.append(str8);
                                        str6 = sb2.toString();
                                    }
                                    String str212 = str6;
                                    if (imageReceiver.getUniqKeyPrefix() != null || str2 == null) {
                                        str9 = str2;
                                    } else {
                                        str9 = imageReceiver.getUniqKeyPrefix() + str2;
                                    }
                                    if (imageReceiver.getUniqKeyPrefix() != null || str3 == null) {
                                        str10 = str3;
                                    } else {
                                        str10 = imageReceiver.getUniqKeyPrefix() + str3;
                                    }
                                    if (imageLocation4 == null) {
                                        str11 = ext;
                                        str12 = str7;
                                        imageLocation5 = imageLocation4;
                                    } else if (imageLocation4.path != null) {
                                        createLoadOperationForImageReceiver(imageReceiver, str212, str182, ext, thumbLocation, str8, 0L, 1, 1, z82 ? 2 : 1, i32);
                                        createLoadOperationForImageReceiver(imageReceiver, str9, str192, str23, imageLocation4, str7, imageReceiver.getSize(), 1, 0, 0, i32);
                                        return;
                                    } else {
                                        str11 = ext;
                                        imageLocation5 = imageLocation4;
                                        str12 = str7;
                                    }
                                    if (imageLocation3 != null) {
                                        int cacheType = imageReceiver.getCacheType();
                                        int i4 = (cacheType == 0 && z72) ? 1 : cacheType;
                                        int i5 = i4 == 0 ? 1 : i4;
                                        if (z82) {
                                            str13 = mediaFilter2;
                                        } else {
                                            str13 = mediaFilter2;
                                            createLoadOperationForImageReceiver(imageReceiver, str212, str182, str11, thumbLocation, str8, 0L, i5, 1, 1, i32);
                                        }
                                        if (!z3) {
                                            createLoadOperationForImageReceiver(imageReceiver, str9, str192, str23, imageLocation5, str12, 0L, 1, 0, 0, i32);
                                        }
                                        if (z2) {
                                            return;
                                        }
                                        createLoadOperationForImageReceiver(imageReceiver, str10, str202, str24, imageLocation3, str13, imageReceiver.getSize(), i4, 3, 0, i32);
                                        return;
                                    }
                                    int cacheType2 = imageReceiver.getCacheType();
                                    int i6 = (cacheType2 == 0 && z72) ? 1 : cacheType2;
                                    createLoadOperationForImageReceiver(imageReceiver, str212, str182, str11, thumbLocation, str8, 0L, i6 == 0 ? 1 : i6, 1, z82 ? 2 : 1, i32);
                                    createLoadOperationForImageReceiver(imageReceiver, str9, str192, str23, imageLocation5, str12, imageReceiver.getSize(), i6, 0, 0, i32);
                                    return;
                                }
                                imageLocation2 = imageLocation;
                                z5 = false;
                                str = "mp4";
                                String str1822 = null;
                                if (imageLocation2 == null) {
                                }
                                ImageLocation imageLocation1022 = imageLocation2;
                                if (mediaLocation != null) {
                                }
                                str = null;
                                ext = imageReceiver.getExt();
                                if (ext == null) {
                                }
                                if (str22 == null) {
                                }
                                if (str == null) {
                                }
                                ImageLocation imageLocation1122 = imageLocation1022;
                                imageLocation3 = mediaLocation;
                                str2 = null;
                                str3 = null;
                                String str1922 = null;
                                String str2022 = null;
                                i = 0;
                                boolean z722 = false;
                                while (i < 2) {
                                }
                                boolean z822 = z4;
                                str4 = thumbFilter2;
                                imageLocation4 = imageLocation1122;
                                str5 = imageFilter2;
                                int i322 = newGuid;
                                if (thumbLocation != null) {
                                }
                                if (str3 != null) {
                                    str3 = str3 + "@" + mediaFilter2;
                                }
                                if (str2 != null) {
                                }
                                str7 = str5;
                                if (str6 != null) {
                                }
                                str8 = str4;
                                String str2122 = str6;
                                if (imageReceiver.getUniqKeyPrefix() != null) {
                                }
                                str9 = str2;
                                if (imageReceiver.getUniqKeyPrefix() != null) {
                                }
                                str10 = str3;
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
                            String str18222 = null;
                            if (imageLocation2 == null) {
                            }
                            ImageLocation imageLocation10222 = imageLocation2;
                            if (mediaLocation != null) {
                            }
                            str = null;
                            ext = imageReceiver.getExt();
                            if (ext == null) {
                            }
                            if (str22 == null) {
                            }
                            if (str == null) {
                            }
                            ImageLocation imageLocation11222 = imageLocation10222;
                            imageLocation3 = mediaLocation;
                            str2 = null;
                            str3 = null;
                            String str19222 = null;
                            String str20222 = null;
                            i = 0;
                            boolean z7222 = false;
                            while (i < 2) {
                            }
                            boolean z8222 = z4;
                            str4 = thumbFilter22;
                            imageLocation4 = imageLocation11222;
                            str5 = imageFilter22;
                            int i3222 = newGuid;
                            if (thumbLocation != null) {
                            }
                            if (str3 != null) {
                            }
                            if (str2 != null) {
                            }
                            str7 = str5;
                            if (str6 != null) {
                            }
                            str8 = str4;
                            String str21222 = str6;
                            if (imageReceiver.getUniqKeyPrefix() != null) {
                            }
                            str9 = str2;
                            if (imageReceiver.getUniqKeyPrefix() != null) {
                            }
                            str10 = str3;
                            if (imageLocation4 == null) {
                            }
                            if (imageLocation3 != null) {
                            }
                        }
                        imageLocation2 = imageLocation;
                        z5 = false;
                        str = "mp4";
                        String str182222 = null;
                        if (imageLocation2 == null) {
                        }
                        ImageLocation imageLocation102222 = imageLocation2;
                        if (mediaLocation != null) {
                        }
                        str = null;
                        ext = imageReceiver.getExt();
                        if (ext == null) {
                        }
                        if (str22 == null) {
                        }
                        if (str == null) {
                        }
                        ImageLocation imageLocation112222 = imageLocation102222;
                        imageLocation3 = mediaLocation;
                        str2 = null;
                        str3 = null;
                        String str192222 = null;
                        String str202222 = null;
                        i = 0;
                        boolean z72222 = false;
                        while (i < 2) {
                        }
                        boolean z82222 = z4;
                        str4 = thumbFilter22;
                        imageLocation4 = imageLocation112222;
                        str5 = imageFilter22;
                        int i32222 = newGuid;
                        if (thumbLocation != null) {
                        }
                        if (str3 != null) {
                        }
                        if (str2 != null) {
                        }
                        str7 = str5;
                        if (str6 != null) {
                        }
                        str8 = str4;
                        String str212222 = str6;
                        if (imageReceiver.getUniqKeyPrefix() != null) {
                        }
                        str9 = str2;
                        if (imageReceiver.getUniqKeyPrefix() != null) {
                        }
                        str10 = str3;
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
                String str1822222 = null;
                if (imageLocation2 == null) {
                }
                ImageLocation imageLocation1022222 = imageLocation2;
                if (mediaLocation != null) {
                }
                str = null;
                ext = imageReceiver.getExt();
                if (ext == null) {
                }
                if (str22 == null) {
                }
                if (str == null) {
                }
                ImageLocation imageLocation1122222 = imageLocation1022222;
                imageLocation3 = mediaLocation;
                str2 = null;
                str3 = null;
                String str1922222 = null;
                String str2022222 = null;
                i = 0;
                boolean z722222 = false;
                while (i < 2) {
                }
                boolean z822222 = z4;
                str4 = thumbFilter222;
                imageLocation4 = imageLocation1122222;
                str5 = imageFilter222;
                int i322222 = newGuid;
                if (thumbLocation != null) {
                }
                if (str3 != null) {
                }
                if (str2 != null) {
                }
                str7 = str5;
                if (str6 != null) {
                }
                str8 = str4;
                String str2122222 = str6;
                if (imageReceiver.getUniqKeyPrefix() != null) {
                }
                str9 = str2;
                if (imageReceiver.getUniqKeyPrefix() != null) {
                }
                str10 = str3;
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
        String str18222222 = null;
        if (imageLocation2 == null) {
        }
        ImageLocation imageLocation10222222 = imageLocation2;
        if (mediaLocation != null) {
        }
        str = null;
        ext = imageReceiver.getExt();
        if (ext == null) {
        }
        if (str22 == null) {
        }
        if (str == null) {
        }
        ImageLocation imageLocation11222222 = imageLocation10222222;
        imageLocation3 = mediaLocation;
        str2 = null;
        str3 = null;
        String str19222222 = null;
        String str20222222 = null;
        i = 0;
        boolean z7222222 = false;
        while (i < 2) {
        }
        boolean z8222222 = z4;
        str4 = thumbFilter2222;
        imageLocation4 = imageLocation11222222;
        str5 = imageFilter2222;
        int i3222222 = newGuid;
        if (thumbLocation != null) {
        }
        if (str3 != null) {
        }
        if (str2 != null) {
        }
        str7 = str5;
        if (str6 != null) {
        }
        str8 = str4;
        String str21222222 = str6;
        if (imageReceiver.getUniqKeyPrefix() != null) {
        }
        str9 = str2;
        if (imageReceiver.getUniqKeyPrefix() != null) {
        }
        str10 = str3;
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
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda4
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
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda8
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
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda3
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
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda11
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
                    Runnable runnable = new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda10
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

    /* JADX WARN: Can't wrap try/catch for region: R(30:1|(29:6|(1:8)(2:163|(2:167|168))|9|10|(1:12)(1:(26:156|157|158|14|(1:16)(1:154)|17|(1:19)|20|(3:22|(2:23|(1:25)(1:26))|27)|28|(1:30)|31|32|33|(4:37|38|40|41)|49|(6:51|(1:53)|54|55|(2:(1:58)|59)|(3:100|101|(4:103|(1:105)|106|(3:108|109|110)(1:112))(1:113))(2:61|(7:63|64|(5:72|73|(1:75)|76|(5:78|79|67|68|69))|66|67|68|69)(1:97)))|134|135|136|(5:138|(1:140)(1:146)|141|(1:143)(1:145)|144)|147|(1:149)|55|(0)|(0)(0)))|13|14|(0)(0)|17|(0)|20|(0)|28|(0)|31|32|33|(5:35|37|38|40|41)|49|(0)|134|135|136|(0)|147|(0)|55|(0)|(0)(0))|172|10|(0)(0)|13|14|(0)(0)|17|(0)|20|(0)|28|(0)|31|32|33|(0)|49|(0)|134|135|136|(0)|147|(0)|55|(0)|(0)(0)|(1:(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00d0, code lost:
        if (r9 == null) goto L49;
     */
    /* JADX WARN: Removed duplicated region for block: B:147:0x01b1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:151:0x014f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0047  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x004b  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0080  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x008a  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0096  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00a7  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00b8 A[Catch: all -> 0x00ef, TRY_LEAVE, TryCatch #12 {all -> 0x00ef, blocks: (B:48:0x00aa, B:50:0x00b8, B:55:0x00d2, B:59:0x00da, B:61:0x00e4, B:65:0x00f1), top: B:169:0x00aa }] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x00e4 A[Catch: all -> 0x00ef, TryCatch #12 {all -> 0x00ef, blocks: (B:48:0x00aa, B:50:0x00b8, B:55:0x00d2, B:59:0x00da, B:61:0x00e4, B:65:0x00f1), top: B:169:0x00aa }] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0100 A[Catch: all -> 0x0138, TryCatch #7 {all -> 0x0138, blocks: (B:66:0x00f6, B:68:0x0100, B:72:0x0111, B:76:0x011e, B:77:0x0121, B:79:0x012b), top: B:159:0x00f6 }] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x012b A[Catch: all -> 0x0138, TRY_LEAVE, TryCatch #7 {all -> 0x0138, blocks: (B:66:0x00f6, B:68:0x0100, B:72:0x0111, B:76:0x011e, B:77:0x0121, B:79:0x012b), top: B:159:0x00f6 }] */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0141  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:129:0x01e2 -> B:146:0x01fb). Please submit an issue!!! */
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
        Bitmap decodeStream;
        Bitmap createBitmap2;
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
                    float f4 = options.outWidth / f;
                    float f5 = options.outHeight / f2;
                    max = z ? Math.max(f4, f5) : Math.min(f4, f5);
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
                        } catch (Throwable unused) {
                            inputStream = null;
                        }
                        try {
                            imageOrientation = AndroidUtilities.getImageOrientation(inputStream);
                        } catch (Throwable unused2) {
                            if (inputStream != null) {
                                inputStream.close();
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
                    }
                    if (((Integer) imageOrientation.first).intValue() == 0) {
                        if (((Integer) imageOrientation.second).intValue() == 0) {
                        }
                        matrix = null;
                        f3 = max / options.inSampleSize;
                        if (f3 > 1.0f) {
                            if (matrix == null) {
                                matrix = new Matrix();
                            }
                            float f6 = 1.0f / f3;
                            matrix.postScale(f6, f6);
                        }
                        if (str2 != null) {
                            try {
                                bitmap = BitmapFactory.decodeFile(str2, options);
                            } catch (Throwable th2) {
                                FileLog.e(th2);
                                getInstance().clearMemory();
                                if (bitmap == null) {
                                    try {
                                        bitmap = BitmapFactory.decodeFile(str2, options);
                                        if (bitmap != null && options.inPurgeable) {
                                            Utilities.pinBitmap(bitmap);
                                        }
                                    } catch (Throwable th3) {
                                        FileLog.e(th3);
                                        return bitmap;
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
                        try {
                        } catch (Throwable th4) {
                            FileLog.e(th4);
                        }
                        if (uri != null) {
                            try {
                                decodeStream = BitmapFactory.decodeStream(openInputStream, null, options);
                            } catch (Throwable th5) {
                                th = th5;
                            }
                            if (decodeStream != null) {
                                try {
                                    if (options.inPurgeable) {
                                        Utilities.pinBitmap(decodeStream);
                                    }
                                    createBitmap2 = Bitmaps.createBitmap(decodeStream, 0, 0, decodeStream.getWidth(), decodeStream.getHeight(), matrix, true);
                                } catch (Throwable th6) {
                                    th = th6;
                                    bitmap = decodeStream;
                                    try {
                                        FileLog.e(th);
                                        openInputStream.close();
                                        return bitmap;
                                    } catch (Throwable th7) {
                                        try {
                                            openInputStream.close();
                                        } catch (Throwable th8) {
                                            FileLog.e(th8);
                                        }
                                        throw th7;
                                    }
                                }
                                if (createBitmap2 != decodeStream) {
                                    decodeStream.recycle();
                                    bitmap = createBitmap2;
                                    openInputStream.close();
                                    return bitmap;
                                }
                            }
                            bitmap = decodeStream;
                            openInputStream.close();
                            return bitmap;
                        }
                        return null;
                    }
                    matrix = new Matrix();
                    if (((Integer) imageOrientation.second).intValue() != 0) {
                        float f7 = -1.0f;
                        float f8 = ((Integer) imageOrientation.second).intValue() == 1 ? -1.0f : 1.0f;
                        if (((Integer) imageOrientation.second).intValue() != 2) {
                            f7 = 1.0f;
                        }
                        matrix.postScale(f8, f7);
                    }
                    if (((Integer) imageOrientation.first).intValue() != 0) {
                        matrix.postRotate(((Integer) imageOrientation.first).intValue());
                    }
                    f3 = max / options.inSampleSize;
                    if (f3 > 1.0f) {
                    }
                    if (str2 != null) {
                    }
                } catch (Throwable th9) {
                    FileLog.e(th9);
                    return null;
                }
            }
            openInputStream = null;
            float f42 = options.outWidth / f;
            float f52 = options.outHeight / f2;
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
        float f422 = options.outWidth / f;
        float f522 = options.outHeight / f2;
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
                        byte[] bArr2 = new byte[(int) randomAccessFile.length()];
                        tLRPC$PhotoSize.bytes = bArr2;
                        randomAccessFile.readFully(bArr2, 0, bArr2.length);
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
            int[] iArr = new int[Bitmap.CompressFormat.values().length];
            $SwitchMap$android$graphics$Bitmap$CompressFormat = iArr;
            try {
                iArr[Bitmap.CompressFormat.WEBP.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$CompressFormat[Bitmap.CompressFormat.WEBP_LOSSY.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$CompressFormat[Bitmap.CompressFormat.WEBP_LOSSLESS.ordinal()] = 3;
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
        TLRPC$PhotoSize findPhotoCachedSize;
        byte[] bArr;
        TLRPC$PhotoSize tLRPC$TL_photoSize_layer127;
        if (tLRPC$Message.media == null || (findPhotoCachedSize = findPhotoCachedSize(tLRPC$Message)) == null || (bArr = findPhotoCachedSize.bytes) == null || bArr.length == 0) {
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
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) {
            int size = tLRPC$MessageMedia.photo.sizes.size();
            while (i < size) {
                if (tLRPC$Message.media.photo.sizes.get(i) instanceof TLRPC$TL_photoCachedSize) {
                    tLRPC$Message.media.photo.sizes.set(i, tLRPC$TL_photoSize_layer127);
                    return;
                }
                i++;
            }
        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
            int size2 = tLRPC$MessageMedia.document.thumbs.size();
            while (i < size2) {
                if (tLRPC$Message.media.document.thumbs.get(i) instanceof TLRPC$TL_photoCachedSize) {
                    tLRPC$Message.media.document.thumbs.set(i, tLRPC$TL_photoSize_layer127);
                    return;
                }
                i++;
            }
        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) {
            int size3 = tLRPC$MessageMedia.webpage.photo.sizes.size();
            while (i < size3) {
                if (tLRPC$Message.media.webpage.photo.sizes.get(i) instanceof TLRPC$TL_photoCachedSize) {
                    tLRPC$Message.media.webpage.photo.sizes.set(i, tLRPC$TL_photoSize_layer127);
                    return;
                }
                i++;
            }
        }
    }

    private static TLRPC$PhotoSize findPhotoCachedSize(TLRPC$Message tLRPC$Message) {
        TLRPC$PhotoSize tLRPC$PhotoSize;
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
        int i = 0;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) {
            int size = tLRPC$MessageMedia.photo.sizes.size();
            while (i < size) {
                tLRPC$PhotoSize = tLRPC$Message.media.photo.sizes.get(i);
                if (!(tLRPC$PhotoSize instanceof TLRPC$TL_photoCachedSize)) {
                    i++;
                }
            }
            return null;
        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
            TLRPC$Document tLRPC$Document = tLRPC$MessageMedia.document;
            if (tLRPC$Document != null) {
                int size2 = tLRPC$Document.thumbs.size();
                while (i < size2) {
                    tLRPC$PhotoSize = tLRPC$Message.media.document.thumbs.get(i);
                    if (!(tLRPC$PhotoSize instanceof TLRPC$TL_photoCachedSize)) {
                        i++;
                    }
                }
                return null;
            }
            return null;
        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) {
            TLRPC$Photo tLRPC$Photo = tLRPC$MessageMedia.webpage.photo;
            if (tLRPC$Photo != null) {
                int size3 = tLRPC$Photo.sizes.size();
                while (i < size3) {
                    tLRPC$PhotoSize = tLRPC$Message.media.webpage.photo.sizes.get(i);
                    if (!(tLRPC$PhotoSize instanceof TLRPC$TL_photoCachedSize)) {
                        i++;
                    }
                }
                return null;
            }
            return null;
        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaInvoice) {
            TLRPC$MessageExtendedMedia tLRPC$MessageExtendedMedia = tLRPC$MessageMedia.extended_media;
            if (tLRPC$MessageExtendedMedia instanceof TLRPC$TL_messageExtendedMediaPreview) {
                return ((TLRPC$TL_messageExtendedMediaPreview) tLRPC$MessageExtendedMedia).thumb;
            }
            return null;
        } else {
            return null;
        }
        return tLRPC$PhotoSize;
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
