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
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.SparseArray;
import androidx.exifinterface.media.ExifInterface;
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
import java.nio.file.Path;
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
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.AnimatedFileDrawable;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.SlotsDrawable;
import org.telegram.ui.Components.ThemePreviewDrawable;
/* loaded from: classes.dex */
public class ImageLoader {
    public static final String AUTOPLAY_FILTER = "g";
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
        for (String str2 : str.split("_")) {
            if (AUTOPLAY_FILTER.equals(str2)) {
                return true;
            }
        }
        return false;
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
    /* loaded from: classes.dex */
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
    /* loaded from: classes.dex */
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
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.fileLoadProgressChanged, this.url, Long.valueOf(j), Long.valueOf(j2));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Code restructure failed: missing block: B:81:0x0121, code lost:
            if (r5 != (-1)) goto L54;
         */
        /* JADX WARN: Code restructure failed: missing block: B:82:0x0123, code lost:
            r0 = r11.fileSize;
         */
        /* JADX WARN: Code restructure failed: missing block: B:83:0x0125, code lost:
            if (r0 == 0) goto L55;
         */
        /* JADX WARN: Code restructure failed: missing block: B:84:0x0127, code lost:
            reportProgress(r0, r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:86:0x012d, code lost:
            r0 = e;
         */
        /* JADX WARN: Code restructure failed: missing block: B:88:0x012f, code lost:
            r1 = false;
         */
        /* JADX WARN: Code restructure failed: missing block: B:91:0x0133, code lost:
            org.telegram.messenger.FileLog.e(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:93:0x0137, code lost:
            r0 = th;
         */
        /* JADX WARN: Code restructure failed: missing block: B:97:0x013b, code lost:
            org.telegram.messenger.FileLog.e(r0);
         */
        /* JADX WARN: Removed duplicated region for block: B:101:0x0143 A[Catch: all -> 0x0149, TRY_LEAVE, TryCatch #6 {all -> 0x0149, blocks: (B:99:0x013f, B:101:0x0143), top: B:123:0x013f }] */
        /* JADX WARN: Removed duplicated region for block: B:114:0x014f A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:119:0x00ae A[EXC_TOP_SPLITTER, SYNTHETIC] */
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
    /* loaded from: classes.dex */
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
    /* loaded from: classes.dex */
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
            NotificationCenter.getInstance(this.cacheImage.currentAccount).postNotificationName(NotificationCenter.fileLoadProgressChanged, this.cacheImage.url, Long.valueOf(j), Long.valueOf(j2));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't wrap try/catch for region: R(16:1|(7:102|103|(1:148)|107|(1:109)|110|(15:112|113|114|4|(6:34|35|(1:43)|45|(3:49|50|(1:58))|(6:63|64|65|(2:66|(1:98)(3:68|69|(3:71|(3:73|74|75)(1:77)|76)(1:78)))|81|82))|6|7|(1:9)|11|12|(1:14)|(2:26|27)|(1:22)|23|24))|3|4|(0)|6|7|(0)|11|12|(0)|(0)|(3:18|20|22)|23|24|(1:(0))) */
        /* JADX WARN: Code restructure failed: missing block: B:100:0x0176, code lost:
            r2 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:101:0x0177, code lost:
            r1 = r2;
            r2 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:102:0x017a, code lost:
            r2 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:103:0x017b, code lost:
            r1 = r2;
            r2 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:106:0x0181, code lost:
            org.telegram.messenger.FileLog.e(r1);
         */
        /* JADX WARN: Code restructure failed: missing block: B:107:0x0184, code lost:
            r1 = r2;
         */
        /* JADX WARN: Code restructure failed: missing block: B:110:0x0188, code lost:
            org.telegram.messenger.FileLog.e(r1);
         */
        /* JADX WARN: Code restructure failed: missing block: B:115:0x0195, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:116:0x0196, code lost:
            org.telegram.messenger.FileLog.e(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:95:0x016a, code lost:
            if (r7 != (-1)) goto L81;
         */
        /* JADX WARN: Code restructure failed: missing block: B:96:0x016c, code lost:
            r2 = r12.imageSize;
         */
        /* JADX WARN: Code restructure failed: missing block: B:97:0x0170, code lost:
            if (r2 == 0) goto L82;
         */
        /* JADX WARN: Code restructure failed: missing block: B:98:0x0172, code lost:
            reportProgress(r2, r2);
         */
        /* JADX WARN: Removed duplicated region for block: B:113:0x018f A[Catch: all -> 0x0195, TRY_LEAVE, TryCatch #4 {all -> 0x0195, blocks: (B:111:0x018b, B:113:0x018f), top: B:143:0x018b }] */
        /* JADX WARN: Removed duplicated region for block: B:119:0x019d A[Catch: all -> 0x01a1, TRY_LEAVE, TryCatch #2 {all -> 0x01a1, blocks: (B:117:0x0199, B:119:0x019d), top: B:139:0x0199 }] */
        /* JADX WARN: Removed duplicated region for block: B:128:0x01ae  */
        /* JADX WARN: Removed duplicated region for block: B:149:0x01a4 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:151:0x00ef A[EXC_TOP_SPLITTER, SYNTHETIC] */
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
                        ConnectionsManager.getInstance(this.cacheImage.currentAccount).sendRequest(tLRPC$TL_upload_getWebFile, ImageLoader$HttpImageTask$$ExternalSyntheticLambda8.INSTANCE);
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
                            FileLog.e(e);
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
                notificationCenter.postNotificationName(i, cacheImage.url, cacheImage.finalFilePath);
                return;
            }
            NotificationCenter.getInstance(this.cacheImage.currentAccount).postNotificationName(NotificationCenter.fileLoadFailed, this.cacheImage.url, 2);
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
            NotificationCenter.getInstance(this.cacheImage.currentAccount).postNotificationName(NotificationCenter.fileLoadFailed, this.cacheImage.url, 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
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
    /* loaded from: classes.dex */
    public class CacheOutTask implements Runnable {
        private CacheImage cacheImage;
        private boolean isCancelled;
        private Thread runningThread;
        private final Object sync = new Object();

        public CacheOutTask(CacheImage cacheImage) {
            this.cacheImage = cacheImage;
        }

        /* JADX WARN: Can't wrap try/catch for region: R(10:932|(2:934|(8:936|937|938|(1:940)(1:947)|941|(1:943)|944|945))|950|937|938|(0)(0)|941|(0)|944|945) */
        /* JADX WARN: Can't wrap try/catch for region: R(14:71|(6:72|73|74|75|(1:77)(1:117)|78)|(3:80|81|(7:83|84|85|(1:111)(1:88)|(2:97|(1:110)(4:100|(1:104)|105|(1:109)))(1:92)|(1:94)(1:96)|95))|116|84|85|(0)|111|(0)|97|(0)|110|(0)(0)|95) */
        /* JADX WARN: Can't wrap try/catch for region: R(21:302|(28:888|889|890|891|(1:893)(1:906)|894|(2:896|(21:898|899|900|305|(19:307|(3:309|(1:311)(1:876)|312)(2:877|(16:879|(1:881)(1:883)|882|314|315|(1:317)|318|319|320|(15:322|(7:324|325|326|327|328|329|330)(1:843)|331|332|(1:334)(2:824|(1:826)(2:827|(1:829)(1:830)))|335|336|337|338|339|340|341|(1:343)|(1:814)(11:349|350|351|352|(2:773|(12:780|781|(1:802)(1:785)|(1:787)|788|789|790|791|(3:796|797|(1:799))|800|797|(0))(3:775|(1:777)(1:779)|778))(2:(7:756|757|758|759|760|761|762)(1:356)|357)|358|(1:755)(1:362)|363|(1:365)|366|(1:754)(4:372|(1:373)|375|376))|377)(3:845|(11:847|848|849|(1:851)(1:870)|852|853|854|(1:856)|857|(4:859|(1:860)|862|863)(1:866)|864)(1:873)|865)|378|379|(3:624|625|85b)(7:381|(1:383)|(3:613|614|(5:616|(2:618|(1:620))(1:621)|386|387|ab5))|385|386|387|ab5)|435|(3:438|(1:440)(1:442)|441)|(3:(1:450)|451|452)(3:(1:446)|447|448))(2:884|(1:886)))|313|314|315|(0)|318|319|320|(0)(0)|378|379|(0)(0)|435|(3:438|(0)(0)|441)|(0)|(0)|451|452)|887|315|(0)|318|319|320|(0)(0)|378|379|(0)(0)|435|(0)|(0)|(0)|451|452))|905|899|900|305|(0)|887|315|(0)|318|319|320|(0)(0)|378|379|(0)(0)|435|(0)|(0)|(0)|451|452)|304|305|(0)|887|315|(0)|318|319|320|(0)(0)|378|379|(0)(0)|435|(0)|(0)|(0)|451|452) */
        /* JADX WARN: Can't wrap try/catch for region: R(23:(6:888|889|890|891|(1:893)(1:906)|894)|(2:896|(21:898|899|900|305|(19:307|(3:309|(1:311)(1:876)|312)(2:877|(16:879|(1:881)(1:883)|882|314|315|(1:317)|318|319|320|(15:322|(7:324|325|326|327|328|329|330)(1:843)|331|332|(1:334)(2:824|(1:826)(2:827|(1:829)(1:830)))|335|336|337|338|339|340|341|(1:343)|(1:814)(11:349|350|351|352|(2:773|(12:780|781|(1:802)(1:785)|(1:787)|788|789|790|791|(3:796|797|(1:799))|800|797|(0))(3:775|(1:777)(1:779)|778))(2:(7:756|757|758|759|760|761|762)(1:356)|357)|358|(1:755)(1:362)|363|(1:365)|366|(1:754)(4:372|(1:373)|375|376))|377)(3:845|(11:847|848|849|(1:851)(1:870)|852|853|854|(1:856)|857|(4:859|(1:860)|862|863)(1:866)|864)(1:873)|865)|378|379|(3:624|625|85b)(7:381|(1:383)|(3:613|614|(5:616|(2:618|(1:620))(1:621)|386|387|ab5))|385|386|387|ab5)|435|(3:438|(1:440)(1:442)|441)|(3:(1:450)|451|452)(3:(1:446)|447|448))(2:884|(1:886)))|313|314|315|(0)|318|319|320|(0)(0)|378|379|(0)(0)|435|(3:438|(0)(0)|441)|(0)|(0)|451|452)|887|315|(0)|318|319|320|(0)(0)|378|379|(0)(0)|435|(0)|(0)|(0)|451|452))|905|899|900|305|(0)|887|315|(0)|318|319|320|(0)(0)|378|379|(0)(0)|435|(0)|(0)|(0)|451|452) */
        /* JADX WARN: Can't wrap try/catch for region: R(7:381|(1:383)|(3:613|614|(5:616|(2:618|(1:620))(1:621)|386|387|ab5))|385|386|387|ab5) */
        /* JADX WARN: Code restructure failed: missing block: B:116:0x0207, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:117:0x0208, code lost:
            org.telegram.messenger.FileLog.e(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:319:0x051f, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:320:0x0520, code lost:
            org.telegram.messenger.FileLog.e(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:518:0x0828, code lost:
            r0 = th;
         */
        /* JADX WARN: Code restructure failed: missing block: B:519:0x0829, code lost:
            r25 = r14;
            r36 = r15;
         */
        /* JADX WARN: Code restructure failed: missing block: B:869:0x0d54, code lost:
            r0 = th;
         */
        /* JADX WARN: Code restructure failed: missing block: B:905:0x0de7, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:906:0x0de8, code lost:
            org.telegram.messenger.FileLog.e(r0);
            r2 = null;
         */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:133:0x0228 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:138:0x0230 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:144:0x023e A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:157:0x0267  */
        /* JADX WARN: Removed duplicated region for block: B:158:0x0285  */
        /* JADX WARN: Removed duplicated region for block: B:193:0x0307  */
        /* JADX WARN: Removed duplicated region for block: B:204:0x032d  */
        /* JADX WARN: Removed duplicated region for block: B:268:0x0436  */
        /* JADX WARN: Removed duplicated region for block: B:269:0x0439  */
        /* JADX WARN: Removed duplicated region for block: B:280:0x047d  */
        /* JADX WARN: Removed duplicated region for block: B:284:0x049a  */
        /* JADX WARN: Removed duplicated region for block: B:344:0x0556  */
        /* JADX WARN: Removed duplicated region for block: B:366:0x05c2  */
        /* JADX WARN: Removed duplicated region for block: B:370:0x05d4 A[Catch: all -> 0x0828, TRY_LEAVE, TryCatch #3 {all -> 0x0828, blocks: (B:368:0x05ce, B:370:0x05d4), top: B:921:0x05ce }] */
        /* JADX WARN: Removed duplicated region for block: B:444:0x06fa A[Catch: all -> 0x0770, TryCatch #26 {all -> 0x0770, blocks: (B:454:0x0727, B:458:0x0735, B:463:0x074f, B:470:0x075f, B:472:0x0769, B:473:0x076c, B:459:0x073e, B:435:0x06e2, B:437:0x06e8, B:442:0x06f2, B:444:0x06fa, B:451:0x0711, B:453:0x0720, B:452:0x071b), top: B:956:0x065b }] */
        /* JADX WARN: Removed duplicated region for block: B:492:0x07ae  */
        /* JADX WARN: Removed duplicated region for block: B:661:0x0a6a  */
        /* JADX WARN: Removed duplicated region for block: B:710:0x0b36  */
        /* JADX WARN: Removed duplicated region for block: B:77:0x0155  */
        /* JADX WARN: Removed duplicated region for block: B:783:0x0c47  */
        /* JADX WARN: Removed duplicated region for block: B:790:0x0c5f A[Catch: all -> 0x0d4a, TryCatch #11 {all -> 0x0d4a, blocks: (B:784:0x0c49, B:786:0x0c53, B:788:0x0c59, B:790:0x0c5f, B:792:0x0c65, B:798:0x0c7b, B:804:0x0c89, B:806:0x0c8f, B:812:0x0cae, B:807:0x0c99, B:809:0x0c9f, B:815:0x0cb6, B:817:0x0cc4, B:819:0x0ccf, B:823:0x0cd6, B:778:0x0c37), top: B:936:0x0c37 }] */
        /* JADX WARN: Removed duplicated region for block: B:812:0x0cae A[Catch: all -> 0x0d4a, TryCatch #11 {all -> 0x0d4a, blocks: (B:784:0x0c49, B:786:0x0c53, B:788:0x0c59, B:790:0x0c5f, B:792:0x0c65, B:798:0x0c7b, B:804:0x0c89, B:806:0x0c8f, B:812:0x0cae, B:807:0x0c99, B:809:0x0c9f, B:815:0x0cb6, B:817:0x0cc4, B:819:0x0ccf, B:823:0x0cd6, B:778:0x0c37), top: B:936:0x0c37 }] */
        /* JADX WARN: Removed duplicated region for block: B:878:0x0d69 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:881:0x0d77  */
        /* JADX WARN: Removed duplicated region for block: B:882:0x0d79  */
        /* JADX WARN: Removed duplicated region for block: B:885:0x0d8e A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:891:0x0d9e  */
        /* JADX WARN: Removed duplicated region for block: B:901:0x0ddf  */
        /* JADX WARN: Removed duplicated region for block: B:902:0x0de1  */
        /* JADX WARN: Removed duplicated region for block: B:908:0x0dee  */
        /* JADX WARN: Removed duplicated region for block: B:938:0x0ab6 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:949:0x0850 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:986:0x02b8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Type inference failed for: r1v36 */
        /* JADX WARN: Type inference failed for: r1v67 */
        /* JADX WARN: Type inference failed for: r1v68 */
        /* JADX WARN: Type inference failed for: r42v0, types: [org.telegram.messenger.ImageLoader$CacheOutTask] */
        /* JADX WARN: Type inference failed for: r8v7 */
        /* JADX WARN: Type inference failed for: r8v8 */
        /* JADX WARN: Type inference failed for: r8v9 */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void run() {
            ThemePreviewDrawable themePreviewDrawable;
            SecureDocumentKey secureDocumentKey;
            byte[] bArr;
            Throwable th;
            RandomAccessFile randomAccessFile;
            RandomAccessFile randomAccessFile2;
            String lowerCase;
            boolean z;
            String str;
            String str2;
            Long l;
            boolean z2;
            boolean z3;
            boolean z4;
            boolean z5;
            Throwable th2;
            float f;
            float f2;
            Bitmap bitmap;
            char c;
            boolean z6;
            char c2;
            boolean z7;
            float f3;
            float f4;
            boolean z8;
            Bitmap bitmap2;
            int i;
            Drawable drawable;
            boolean z9;
            int i2;
            FileInputStream fileInputStream;
            Bitmap bitmap3;
            Bitmap bitmap4;
            boolean z10;
            float f5;
            boolean z11;
            boolean z12;
            Bitmap createScaledBitmap;
            FileInputStream fileInputStream2;
            int attributeInt;
            boolean z13;
            int i3;
            String str3;
            float f6;
            char c3;
            boolean z14;
            boolean z15;
            FileInputStream fileInputStream3;
            float min;
            Long l2;
            boolean z16;
            String str4;
            boolean z17;
            boolean z18;
            boolean z19;
            boolean z20;
            BitmapsCache.CacheOptions cacheOptions;
            AnimatedFileDrawable animatedFileDrawable;
            int i4;
            int i5;
            String str5;
            int i6;
            int i7;
            int i8;
            boolean z21;
            boolean z22;
            boolean z23;
            String str6;
            boolean z24;
            boolean z25;
            int i9;
            int i10;
            ?? r1;
            Throwable th3;
            RandomAccessFile randomAccessFile3;
            RandomAccessFile randomAccessFile4;
            boolean z26;
            BitmapsCache.CacheOptions cacheOptions2;
            RLottieDrawable rLottieDrawable;
            RLottieDrawable rLottieDrawable2;
            byte[] bArr2;
            RLottieDrawable rLottieDrawable3;
            boolean z27;
            boolean z28;
            int i11;
            int i12;
            synchronized (this.sync) {
                this.runningThread = Thread.currentThread();
                Thread.interrupted();
                if (this.isCancelled) {
                    return;
                }
                CacheImage cacheImage = this.cacheImage;
                ImageLocation imageLocation = cacheImage.imageLocation;
                TLRPC$PhotoSize tLRPC$PhotoSize = imageLocation.photoSize;
                if (tLRPC$PhotoSize instanceof TLRPC$TL_photoStrippedSize) {
                    Bitmap strippedPhotoBitmap = ImageLoader.getStrippedPhotoBitmap(((TLRPC$TL_photoStrippedSize) tLRPC$PhotoSize).bytes, "b");
                    onPostExecute(strippedPhotoBitmap != null ? new BitmapDrawable(strippedPhotoBitmap) : null);
                    return;
                }
                int i13 = cacheImage.imageType;
                if (i13 == 5) {
                    try {
                        CacheImage cacheImage2 = this.cacheImage;
                        themePreviewDrawable = new ThemePreviewDrawable(cacheImage2.finalFilePath, (DocumentObject.ThemeDocument) cacheImage2.imageLocation.document);
                    } catch (Throwable th4) {
                        FileLog.e(th4);
                        themePreviewDrawable = null;
                    }
                    onPostExecute(themePreviewDrawable);
                    return;
                }
                boolean z29 = true;
                if (i13 == 3 || i13 == 4) {
                    Point point = AndroidUtilities.displaySize;
                    int i14 = point.x;
                    int i15 = point.y;
                    String str7 = cacheImage.filter;
                    if (str7 != null) {
                        String[] split = str7.split("_");
                        if (split.length >= 2) {
                            float parseFloat = Float.parseFloat(split[0]);
                            float parseFloat2 = Float.parseFloat(split[1]);
                            float f7 = AndroidUtilities.density;
                            i14 = (int) (parseFloat * f7);
                            i15 = (int) (parseFloat2 * f7);
                            CacheImage cacheImage3 = this.cacheImage;
                            Bitmap bitmap5 = SvgHelper.getBitmap(cacheImage3.finalFilePath, i14, i15, cacheImage3.imageType != 4);
                            onPostExecute(bitmap5 != null ? new BitmapDrawable(bitmap5) : null);
                        }
                    }
                    CacheImage cacheImage32 = this.cacheImage;
                    Bitmap bitmap52 = SvgHelper.getBitmap(cacheImage32.finalFilePath, i14, i15, cacheImage32.imageType != 4);
                    onPostExecute(bitmap52 != null ? new BitmapDrawable(bitmap52) : null);
                } else if (i13 == 1) {
                    int min2 = Math.min((int) LiteMode.FLAG_CALLS_ANIMATIONS, AndroidUtilities.dp(170.6f));
                    int min3 = Math.min((int) LiteMode.FLAG_CALLS_ANIMATIONS, AndroidUtilities.dp(170.6f));
                    String str8 = this.cacheImage.filter;
                    if (str8 != null) {
                        String[] split2 = str8.split("_");
                        if (split2.length >= 2) {
                            float parseFloat3 = Float.parseFloat(split2[0]);
                            float parseFloat4 = Float.parseFloat(split2[1]);
                            int min4 = Math.min((int) LiteMode.FLAG_CALLS_ANIMATIONS, (int) (AndroidUtilities.density * parseFloat3));
                            int min5 = Math.min((int) LiteMode.FLAG_CALLS_ANIMATIONS, (int) (parseFloat4 * AndroidUtilities.density));
                            if (parseFloat3 > 90.0f || parseFloat4 > 90.0f || this.cacheImage.filter.contains("nolimit")) {
                                min3 = min5;
                                i12 = min4;
                                z21 = false;
                            } else {
                                int min6 = Math.min(min4, 160);
                                min3 = Math.min(min5, 160);
                                z21 = true;
                                i12 = min6;
                            }
                            z24 = (split2.length >= 3 && "pcache".equals(split2[2])) || this.cacheImage.filter.contains("pcache") || !(this.cacheImage.filter.contains("nolimit") || SharedConfig.getDevicePerformanceClass() == 2);
                            z28 = this.cacheImage.filter.contains("lastframe");
                            z27 = this.cacheImage.filter.contains("lastreactframe");
                            if (z27) {
                                z28 = true;
                            }
                            if (this.cacheImage.filter.contains("firstframe")) {
                                z25 = true;
                                i11 = i12;
                            } else {
                                z25 = false;
                                i11 = i12;
                            }
                        } else {
                            z21 = false;
                            z27 = false;
                            z24 = false;
                            z25 = false;
                            z28 = false;
                            i11 = min2;
                        }
                        if (split2.length >= 3) {
                            if ("nr".equals(split2[2])) {
                                str6 = null;
                            } else if (!"nrs".equals(split2[2])) {
                                if ("dice".equals(split2[2])) {
                                    str6 = split2[3];
                                }
                            } else {
                                str6 = null;
                                i9 = 3;
                                if (split2.length >= 5) {
                                    if ("c1".equals(split2[4])) {
                                        i7 = min3;
                                        z23 = z27;
                                        z22 = z28;
                                        i10 = 12;
                                    } else if ("c2".equals(split2[4])) {
                                        i7 = min3;
                                        z23 = z27;
                                        z22 = z28;
                                        i10 = 3;
                                    } else if ("c3".equals(split2[4])) {
                                        i7 = min3;
                                        z23 = z27;
                                        z22 = z28;
                                        i10 = 4;
                                    } else if ("c4".equals(split2[4])) {
                                        i7 = min3;
                                        z23 = z27;
                                        z22 = z28;
                                        i10 = 5;
                                    } else if ("c5".equals(split2[4])) {
                                        i7 = min3;
                                        z23 = z27;
                                        z22 = z28;
                                        i10 = 6;
                                    }
                                    i8 = i11;
                                    r1 = i11;
                                }
                                i7 = min3;
                                z23 = z27;
                                z22 = z28;
                                i10 = 0;
                                i8 = i11;
                                r1 = i11;
                            }
                            i9 = 2;
                            if (split2.length >= 5) {
                            }
                            i7 = min3;
                            z23 = z27;
                            z22 = z28;
                            i10 = 0;
                            i8 = i11;
                            r1 = i11;
                        }
                        str6 = null;
                        i9 = 1;
                        if (split2.length >= 5) {
                        }
                        i7 = min3;
                        z23 = z27;
                        z22 = z28;
                        i10 = 0;
                        i8 = i11;
                        r1 = i11;
                    } else {
                        i7 = min3;
                        i8 = min2;
                        z21 = false;
                        z22 = false;
                        z23 = false;
                        str6 = null;
                        z24 = false;
                        z25 = false;
                        i9 = 1;
                        i10 = 0;
                        r1 = min2;
                    }
                    if (str6 != null) {
                        if ("🎰".equals(str6)) {
                            rLottieDrawable3 = new SlotsDrawable(str6, i8, i7);
                        } else {
                            rLottieDrawable3 = new RLottieDrawable(str6, i8, i7);
                        }
                        rLottieDrawable2 = rLottieDrawable3;
                    } else {
                        File file = this.cacheImage.finalFilePath;
                        try {
                            try {
                                randomAccessFile4 = new RandomAccessFile(this.cacheImage.finalFilePath, "r");
                                try {
                                    bArr2 = this.cacheImage.type == 1 ? ImageLoader.headerThumb : ImageLoader.header;
                                    randomAccessFile4.readFully(bArr2, 0, 2);
                                } catch (Exception e) {
                                    e = e;
                                    FileLog.e((Throwable) e, false);
                                    if (randomAccessFile4 != null) {
                                        try {
                                            randomAccessFile4.close();
                                        } catch (Exception e2) {
                                            FileLog.e(e2);
                                        }
                                    }
                                    z26 = false;
                                    if ((!z22 || z25) ? false : z24) {
                                    }
                                    cacheOptions2 = new BitmapsCache.CacheOptions();
                                    if (z22) {
                                    }
                                    cacheOptions2.firstFrame = true;
                                    if (z26) {
                                    }
                                    rLottieDrawable2 = rLottieDrawable;
                                    if (!z22) {
                                    }
                                    loadLastFrame(rLottieDrawable2, i7, i8, z22, z23);
                                    return;
                                }
                            } catch (Throwable th5) {
                                randomAccessFile3 = r1;
                                th3 = th5;
                                if (randomAccessFile3 != null) {
                                    try {
                                        randomAccessFile3.close();
                                    } catch (Exception e3) {
                                        FileLog.e(e3);
                                    }
                                }
                                throw th3;
                            }
                        } catch (Exception e4) {
                            e = e4;
                            randomAccessFile4 = null;
                        } catch (Throwable th6) {
                            th3 = th6;
                            randomAccessFile3 = null;
                            if (randomAccessFile3 != null) {
                            }
                            throw th3;
                        }
                        if (bArr2[0] == 31) {
                            if (bArr2[1] == -117) {
                                z26 = true;
                                randomAccessFile4.close();
                                if (!((!z22 || z25) ? false : z24) || z22 || z25) {
                                    cacheOptions2 = new BitmapsCache.CacheOptions();
                                    if (z22 && !z25) {
                                        String str9 = this.cacheImage.filter;
                                        if (str9 != null && str9.contains("compress")) {
                                            cacheOptions2.compressQuality = 60;
                                        }
                                        String str10 = this.cacheImage.filter;
                                        if (str10 != null && str10.contains("flbk")) {
                                            cacheOptions2.fallback = true;
                                        }
                                    } else {
                                        cacheOptions2.firstFrame = true;
                                    }
                                } else {
                                    cacheOptions2 = null;
                                }
                                if (z26) {
                                    File file2 = this.cacheImage.finalFilePath;
                                    rLottieDrawable = new RLottieDrawable(file2, ImageLoader.decompressGzip(file2), i8, i7, cacheOptions2, z21, null, i10);
                                } else {
                                    rLottieDrawable = new RLottieDrawable(this.cacheImage.finalFilePath, i8, i7, cacheOptions2, z21, null, i10);
                                }
                                rLottieDrawable2 = rLottieDrawable;
                            }
                        }
                        z26 = false;
                        randomAccessFile4.close();
                        if ((!z22 || z25) ? false : z24) {
                        }
                        cacheOptions2 = new BitmapsCache.CacheOptions();
                        if (z22) {
                        }
                        cacheOptions2.firstFrame = true;
                        if (z26) {
                        }
                        rLottieDrawable2 = rLottieDrawable;
                    }
                    if (!z22 || z25) {
                        loadLastFrame(rLottieDrawable2, i7, i8, z22, z23);
                        return;
                    }
                    rLottieDrawable2.setAutoRepeat(i9);
                    onPostExecute(rLottieDrawable2);
                } else if (i13 == 2) {
                    long j = imageLocation != null ? imageLocation.videoSeekTo : 0L;
                    String str11 = cacheImage.filter;
                    if (str11 != null) {
                        String[] split3 = str11.split("_");
                        if (split3.length >= 2) {
                            float parseFloat5 = Float.parseFloat(split3[0]);
                            float parseFloat6 = Float.parseFloat(split3[1]);
                            if (parseFloat5 <= 90.0f && parseFloat6 <= 90.0f && !this.cacheImage.filter.contains("nolimit")) {
                                z17 = true;
                                z18 = false;
                                z19 = false;
                                z20 = false;
                                for (i6 = 0; i6 < split3.length; i6++) {
                                    if ("pcache".equals(split3[i6])) {
                                        z19 = true;
                                    }
                                    if ("firstframe".equals(split3[i6])) {
                                        z18 = true;
                                    }
                                    if ("nostream".equals(split3[i6])) {
                                        z20 = true;
                                    }
                                }
                                if (z18) {
                                    z20 = true;
                                }
                            }
                        }
                        z17 = false;
                        z18 = false;
                        z19 = false;
                        z20 = false;
                        while (i6 < split3.length) {
                        }
                        if (z18) {
                        }
                    } else {
                        z17 = false;
                        z18 = false;
                        z19 = false;
                        z20 = false;
                    }
                    if (!z19 || z18) {
                        cacheOptions = null;
                    } else {
                        BitmapsCache.CacheOptions cacheOptions3 = new BitmapsCache.CacheOptions();
                        String str12 = this.cacheImage.filter;
                        if (str12 != null && str12.contains("compress")) {
                            cacheOptions3.compressQuality = 60;
                        }
                        cacheOptions = cacheOptions3;
                    }
                    if (ImageLoader.this.isAnimatedAvatar(this.cacheImage.filter) || ImageLoader.AUTOPLAY_FILTER.equals(this.cacheImage.filter)) {
                        CacheImage cacheImage4 = this.cacheImage;
                        ImageLocation imageLocation2 = cacheImage4.imageLocation;
                        TLRPC$Document tLRPC$Document = imageLocation2.document;
                        if (!(tLRPC$Document instanceof TLRPC$TL_documentEncrypted) && !z19) {
                            if (!(tLRPC$Document instanceof TLRPC$Document)) {
                                tLRPC$Document = null;
                            }
                            long j2 = tLRPC$Document != null ? cacheImage4.size : imageLocation2.currentSize;
                            CacheImage cacheImage5 = this.cacheImage;
                            animatedFileDrawable = new AnimatedFileDrawable(cacheImage5.finalFilePath, z18, z20 ? 0L : j2, cacheImage5.priority, z20 ? null : tLRPC$Document, (tLRPC$Document != null || z20) ? null : cacheImage5.imageLocation, cacheImage5.parentObject, j, cacheImage5.currentAccount, false, cacheOptions);
                            if (!MessageObject.isWebM(tLRPC$Document) && !MessageObject.isVideoSticker(tLRPC$Document) && !ImageLoader.this.isAnimatedAvatar(this.cacheImage.filter)) {
                                z29 = false;
                            }
                            animatedFileDrawable.setIsWebmSticker(z29);
                            if (z18) {
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
                            animatedFileDrawable.setLimitFps(z17);
                            Thread.interrupted();
                            onPostExecute(animatedFileDrawable);
                            return;
                        }
                    }
                    String str13 = this.cacheImage.filter;
                    if (str13 != null) {
                        String[] split4 = str13.split("_");
                        if (split4.length >= 2) {
                            float parseFloat7 = Float.parseFloat(split4[0]);
                            float parseFloat8 = Float.parseFloat(split4[1]);
                            float f8 = AndroidUtilities.density;
                            i5 = (int) (parseFloat8 * f8);
                            i4 = (int) (parseFloat7 * f8);
                            boolean z30 = !z18 || ((str5 = this.cacheImage.filter) != null && ("d".equals(str5) || this.cacheImage.filter.contains("_d")));
                            CacheImage cacheImage6 = this.cacheImage;
                            animatedFileDrawable = new AnimatedFileDrawable(cacheImage6.finalFilePath, z30, 0L, cacheImage6.priority, !z20 ? null : cacheImage6.imageLocation.document, null, null, j, cacheImage6.currentAccount, false, i4, i5, cacheOptions);
                            if (!MessageObject.isWebM(this.cacheImage.imageLocation.document) && !MessageObject.isVideoSticker(this.cacheImage.imageLocation.document) && !ImageLoader.this.isAnimatedAvatar(this.cacheImage.filter)) {
                                z29 = false;
                            }
                            animatedFileDrawable.setIsWebmSticker(z29);
                            if (z18) {
                            }
                        }
                    }
                    i4 = 0;
                    i5 = 0;
                    if (z18) {
                    }
                    CacheImage cacheImage62 = this.cacheImage;
                    animatedFileDrawable = new AnimatedFileDrawable(cacheImage62.finalFilePath, z30, 0L, cacheImage62.priority, !z20 ? null : cacheImage62.imageLocation.document, null, null, j, cacheImage62.currentAccount, false, i4, i5, cacheOptions);
                    if (!MessageObject.isWebM(this.cacheImage.imageLocation.document)) {
                        z29 = false;
                    }
                    animatedFileDrawable.setIsWebmSticker(z29);
                    if (z18) {
                    }
                } else {
                    File file3 = cacheImage.finalFilePath;
                    boolean z31 = (cacheImage.secureDocument == null && (cacheImage.encryptionKeyPath == null || file3 == null || !file3.getAbsolutePath().endsWith(".enc"))) ? false : true;
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
                    try {
                        if (Build.VERSION.SDK_INT < 19) {
                            try {
                                randomAccessFile2 = new RandomAccessFile(file3, "r");
                                try {
                                    byte[] bArr3 = this.cacheImage.type == 1 ? ImageLoader.headerThumb : ImageLoader.header;
                                    randomAccessFile2.readFully(bArr3, 0, bArr3.length);
                                    lowerCase = new String(bArr3).toLowerCase().toLowerCase();
                                } catch (Exception e5) {
                                    e = e5;
                                    FileLog.e(e);
                                    if (randomAccessFile2 != null) {
                                        try {
                                            randomAccessFile2.close();
                                        } catch (Exception e6) {
                                            FileLog.e(e6);
                                        }
                                    }
                                    z = false;
                                    str = this.cacheImage.imageLocation.path;
                                    if (str != null) {
                                    }
                                    str2 = null;
                                    l = null;
                                    z2 = false;
                                    z3 = true;
                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    options.inSampleSize = 1;
                                    if (Build.VERSION.SDK_INT < 21) {
                                    }
                                    boolean z32 = ImageLoader.this.canForce8888;
                                    str3 = this.cacheImage.filter;
                                    if (str3 == null) {
                                    }
                                    f4 = f6;
                                    c2 = c;
                                    z7 = z6;
                                    Bitmap bitmap6 = bitmap;
                                    ?? r8 = 1;
                                    if (this.cacheImage.type != 1) {
                                    }
                                    Thread.interrupted();
                                    if (BuildVars.LOGS_ENABLED) {
                                    }
                                    if (z8) {
                                    }
                                    if (bitmap3 != null) {
                                    }
                                    onPostExecute(drawable);
                                }
                            } catch (Exception e7) {
                                e = e7;
                                randomAccessFile2 = null;
                            } catch (Throwable th7) {
                                th = th7;
                                randomAccessFile = null;
                                if (randomAccessFile != null) {
                                    try {
                                        randomAccessFile.close();
                                    } catch (Exception e8) {
                                        FileLog.e(e8);
                                    }
                                }
                                throw th;
                            }
                            if (lowerCase.startsWith("riff")) {
                                if (lowerCase.endsWith("webp")) {
                                    z = true;
                                    randomAccessFile2.close();
                                    str = this.cacheImage.imageLocation.path;
                                    if (str != null) {
                                        if (str.startsWith("thumb://")) {
                                            int indexOf = str.indexOf(":", 8);
                                            if (indexOf >= 0) {
                                                l = Long.valueOf(Long.parseLong(str.substring(8, indexOf)));
                                                str4 = str.substring(indexOf + 1);
                                            } else {
                                                str4 = null;
                                                l = null;
                                            }
                                            str2 = str4;
                                        } else if (str.startsWith("vthumb://")) {
                                            int indexOf2 = str.indexOf(":", 9);
                                            if (indexOf2 >= 0) {
                                                l2 = Long.valueOf(Long.parseLong(str.substring(9, indexOf2)));
                                                z16 = true;
                                            } else {
                                                l2 = null;
                                                z16 = false;
                                            }
                                            l = l2;
                                            z2 = z16;
                                            str2 = null;
                                            z3 = false;
                                            BitmapFactory.Options options2 = new BitmapFactory.Options();
                                            options2.inSampleSize = 1;
                                            if (Build.VERSION.SDK_INT < 21) {
                                                options2.inPurgeable = true;
                                            }
                                            boolean z322 = ImageLoader.this.canForce8888;
                                            str3 = this.cacheImage.filter;
                                            if (str3 == null) {
                                                String[] split5 = str3.split("_");
                                                if (split5.length >= 2) {
                                                    try {
                                                        f2 = Float.parseFloat(split5[0]) * AndroidUtilities.density;
                                                        try {
                                                            f6 = f2;
                                                            f3 = Float.parseFloat(split5[1]) * AndroidUtilities.density;
                                                        } catch (Throwable th8) {
                                                            th2 = th8;
                                                            z4 = z2;
                                                            z5 = z3;
                                                            f = 0.0f;
                                                            bitmap = null;
                                                            c = 0;
                                                            z6 = false;
                                                            FileLog.e(th2, !(th2 instanceof FileNotFoundException));
                                                            c2 = c;
                                                            z7 = z6;
                                                            float f9 = f2;
                                                            f3 = f;
                                                            f4 = f9;
                                                            Bitmap bitmap62 = bitmap;
                                                            ?? r82 = 1;
                                                            if (this.cacheImage.type != 1) {
                                                            }
                                                            Thread.interrupted();
                                                            if (BuildVars.LOGS_ENABLED) {
                                                            }
                                                            if (z8) {
                                                            }
                                                            if (bitmap3 != null) {
                                                            }
                                                            onPostExecute(drawable);
                                                        }
                                                    } catch (Throwable th9) {
                                                        th2 = th9;
                                                        z4 = z2;
                                                        z5 = z3;
                                                        f = 0.0f;
                                                        f2 = 0.0f;
                                                        bitmap = null;
                                                        c = 0;
                                                        z6 = false;
                                                        FileLog.e(th2, !(th2 instanceof FileNotFoundException));
                                                        c2 = c;
                                                        z7 = z6;
                                                        float f92 = f2;
                                                        f3 = f;
                                                        f4 = f92;
                                                        Bitmap bitmap622 = bitmap;
                                                        ?? r822 = 1;
                                                        if (this.cacheImage.type != 1) {
                                                        }
                                                        Thread.interrupted();
                                                        if (BuildVars.LOGS_ENABLED) {
                                                        }
                                                        if (z8) {
                                                        }
                                                        if (bitmap3 != null) {
                                                        }
                                                        onPostExecute(drawable);
                                                    }
                                                } else {
                                                    f3 = 0.0f;
                                                    f6 = 0.0f;
                                                }
                                                try {
                                                    if (this.cacheImage.filter.contains("b2")) {
                                                        c3 = 3;
                                                    } else if (this.cacheImage.filter.contains("b1")) {
                                                        c3 = 2;
                                                    } else {
                                                        c3 = this.cacheImage.filter.contains("b") ? (char) 1 : (char) 0;
                                                    }
                                                } catch (Throwable th10) {
                                                    z4 = z2;
                                                    z5 = z3;
                                                    th2 = th10;
                                                    f = f3;
                                                    f2 = f6;
                                                    bitmap = null;
                                                    c = 0;
                                                    z6 = false;
                                                    FileLog.e(th2, !(th2 instanceof FileNotFoundException));
                                                    c2 = c;
                                                    z7 = z6;
                                                    float f922 = f2;
                                                    f3 = f;
                                                    f4 = f922;
                                                    Bitmap bitmap6222 = bitmap;
                                                    ?? r8222 = 1;
                                                    if (this.cacheImage.type != 1) {
                                                    }
                                                    Thread.interrupted();
                                                    if (BuildVars.LOGS_ENABLED) {
                                                    }
                                                    if (z8) {
                                                    }
                                                    if (bitmap3 != null) {
                                                    }
                                                    onPostExecute(drawable);
                                                }
                                                try {
                                                    boolean contains = this.cacheImage.filter.contains("i");
                                                    try {
                                                        c = c3;
                                                        try {
                                                            if (this.cacheImage.filter.contains("f")) {
                                                                z322 = true;
                                                            }
                                                            if (z || f6 == 0.0f || f3 == 0.0f) {
                                                                z14 = z322;
                                                                z6 = contains;
                                                                z4 = z2;
                                                                z5 = z3;
                                                            } else {
                                                                try {
                                                                    options2.inJustDecodeBounds = true;
                                                                } catch (Throwable th11) {
                                                                    th = th11;
                                                                    z6 = contains;
                                                                    z4 = z2;
                                                                    z5 = z3;
                                                                    th2 = th;
                                                                    f = f3;
                                                                    f2 = f6;
                                                                    bitmap = null;
                                                                    FileLog.e(th2, !(th2 instanceof FileNotFoundException));
                                                                    c2 = c;
                                                                    z7 = z6;
                                                                    float f9222 = f2;
                                                                    f3 = f;
                                                                    f4 = f9222;
                                                                    Bitmap bitmap62222 = bitmap;
                                                                    ?? r82222 = 1;
                                                                    if (this.cacheImage.type != 1) {
                                                                    }
                                                                    Thread.interrupted();
                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                    }
                                                                    if (z8) {
                                                                    }
                                                                    if (bitmap3 != null) {
                                                                    }
                                                                    onPostExecute(drawable);
                                                                }
                                                                try {
                                                                    try {
                                                                        if (l == null || str2 != null) {
                                                                            z14 = z322;
                                                                            z6 = contains;
                                                                            if (secureDocumentKey != null) {
                                                                                try {
                                                                                    RandomAccessFile randomAccessFile5 = new RandomAccessFile(file3, "r");
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
                                                                                    z4 = z2;
                                                                                    z5 = z3;
                                                                                    byte[] computeSHA256 = Utilities.computeSHA256(bArr4, 0, length);
                                                                                    if (bArr != null && Arrays.equals(computeSHA256, bArr)) {
                                                                                        z15 = false;
                                                                                        int i16 = bArr4[0] & 255;
                                                                                        int i17 = length - i16;
                                                                                        if (!z15) {
                                                                                            BitmapFactory.decodeByteArray(bArr4, i16, i17, options2);
                                                                                        }
                                                                                    }
                                                                                    z15 = true;
                                                                                    int i162 = bArr4[0] & 255;
                                                                                    int i172 = length - i162;
                                                                                    if (!z15) {
                                                                                    }
                                                                                } catch (Throwable th12) {
                                                                                    th = th12;
                                                                                    z4 = z2;
                                                                                    z5 = z3;
                                                                                    th2 = th;
                                                                                    f = f3;
                                                                                    f2 = f6;
                                                                                    z322 = z14;
                                                                                    bitmap = null;
                                                                                    FileLog.e(th2, !(th2 instanceof FileNotFoundException));
                                                                                    c2 = c;
                                                                                    z7 = z6;
                                                                                    float f92222 = f2;
                                                                                    f3 = f;
                                                                                    f4 = f92222;
                                                                                    Bitmap bitmap622222 = bitmap;
                                                                                    ?? r822222 = 1;
                                                                                    if (this.cacheImage.type != 1) {
                                                                                    }
                                                                                    Thread.interrupted();
                                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                                    }
                                                                                    if (z8) {
                                                                                    }
                                                                                    if (bitmap3 != null) {
                                                                                    }
                                                                                    onPostExecute(drawable);
                                                                                }
                                                                            } else {
                                                                                z4 = z2;
                                                                                z5 = z3;
                                                                                if (z31) {
                                                                                    fileInputStream3 = new EncryptedFileInputStream(file3, this.cacheImage.encryptionKeyPath);
                                                                                } else {
                                                                                    fileInputStream3 = new FileInputStream(file3);
                                                                                }
                                                                                BitmapFactory.decodeStream(fileInputStream3, null, options2);
                                                                                fileInputStream3.close();
                                                                            }
                                                                        } else {
                                                                            if (z2) {
                                                                                try {
                                                                                    z6 = contains;
                                                                                    try {
                                                                                        z14 = z322;
                                                                                        MediaStore.Video.Thumbnails.getThumbnail(ApplicationLoader.applicationContext.getContentResolver(), l.longValue(), 1, options2);
                                                                                    } catch (Throwable th13) {
                                                                                        th = th13;
                                                                                        th2 = th;
                                                                                        f = f3;
                                                                                        z4 = z2;
                                                                                        z5 = z3;
                                                                                        f2 = f6;
                                                                                        bitmap = null;
                                                                                        FileLog.e(th2, !(th2 instanceof FileNotFoundException));
                                                                                        c2 = c;
                                                                                        z7 = z6;
                                                                                        float f922222 = f2;
                                                                                        f3 = f;
                                                                                        f4 = f922222;
                                                                                        Bitmap bitmap6222222 = bitmap;
                                                                                        ?? r8222222 = 1;
                                                                                        if (this.cacheImage.type != 1) {
                                                                                        }
                                                                                        Thread.interrupted();
                                                                                        if (BuildVars.LOGS_ENABLED) {
                                                                                        }
                                                                                        if (z8) {
                                                                                        }
                                                                                        if (bitmap3 != null) {
                                                                                        }
                                                                                        onPostExecute(drawable);
                                                                                    }
                                                                                } catch (Throwable th14) {
                                                                                    th = th14;
                                                                                    z6 = contains;
                                                                                }
                                                                            } else {
                                                                                z14 = z322;
                                                                                z6 = contains;
                                                                                MediaStore.Images.Thumbnails.getThumbnail(ApplicationLoader.applicationContext.getContentResolver(), l.longValue(), 1, options2);
                                                                            }
                                                                            z4 = z2;
                                                                            z5 = z3;
                                                                        }
                                                                        float f10 = options2.outWidth;
                                                                        float f11 = options2.outHeight;
                                                                        if (f6 >= f3 && f10 > f11) {
                                                                            min = Math.max(f10 / f6, f11 / f3);
                                                                        } else {
                                                                            min = Math.min(f10 / f6, f11 / f3);
                                                                        }
                                                                        if (min < 1.2f) {
                                                                            min = 1.0f;
                                                                        }
                                                                        options2.inJustDecodeBounds = false;
                                                                        if (min > 1.0f && (f10 > f6 || f11 > f3)) {
                                                                            int i18 = 1;
                                                                            do {
                                                                                i18 *= 2;
                                                                            } while (i18 * 2 < min);
                                                                            options2.inSampleSize = i18;
                                                                        } else {
                                                                            options2.inSampleSize = (int) min;
                                                                        }
                                                                    } catch (Throwable th15) {
                                                                        th = th15;
                                                                    }
                                                                } catch (Throwable th16) {
                                                                    th2 = th16;
                                                                    f = f3;
                                                                    z4 = z2;
                                                                    z5 = z3;
                                                                    f2 = f6;
                                                                    z322 = z14;
                                                                    bitmap = null;
                                                                    FileLog.e(th2, !(th2 instanceof FileNotFoundException));
                                                                    c2 = c;
                                                                    z7 = z6;
                                                                    float f9222222 = f2;
                                                                    f3 = f;
                                                                    f4 = f9222222;
                                                                    Bitmap bitmap62222222 = bitmap;
                                                                    ?? r82222222 = 1;
                                                                    if (this.cacheImage.type != 1) {
                                                                    }
                                                                    Thread.interrupted();
                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                    }
                                                                    if (z8) {
                                                                    }
                                                                    if (bitmap3 != null) {
                                                                    }
                                                                    onPostExecute(drawable);
                                                                }
                                                            }
                                                            z322 = z14;
                                                            bitmap = null;
                                                        } catch (Throwable th17) {
                                                            th = th17;
                                                        }
                                                    } catch (Throwable th18) {
                                                        th = th18;
                                                        c = c3;
                                                    }
                                                } catch (Throwable th19) {
                                                    c = c3;
                                                    z4 = z2;
                                                    z5 = z3;
                                                    th2 = th19;
                                                    f = f3;
                                                    f2 = f6;
                                                    bitmap = null;
                                                    z6 = false;
                                                    FileLog.e(th2, !(th2 instanceof FileNotFoundException));
                                                    c2 = c;
                                                    z7 = z6;
                                                    float f92222222 = f2;
                                                    f3 = f;
                                                    f4 = f92222222;
                                                    Bitmap bitmap622222222 = bitmap;
                                                    ?? r822222222 = 1;
                                                    if (this.cacheImage.type != 1) {
                                                    }
                                                    Thread.interrupted();
                                                    if (BuildVars.LOGS_ENABLED) {
                                                    }
                                                    if (z8) {
                                                    }
                                                    if (bitmap3 != null) {
                                                    }
                                                    onPostExecute(drawable);
                                                }
                                            } else {
                                                z4 = z2;
                                                z5 = z3;
                                                if (str2 != null) {
                                                    try {
                                                        options2.inJustDecodeBounds = true;
                                                        options2.inPreferredConfig = z322 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
                                                        FileInputStream fileInputStream4 = new FileInputStream(file3);
                                                        bitmap = BitmapFactory.decodeStream(fileInputStream4, null, options2);
                                                        try {
                                                            fileInputStream4.close();
                                                            int i19 = options2.outWidth;
                                                            int i20 = options2.outHeight;
                                                            options2.inJustDecodeBounds = false;
                                                            float min7 = (Math.min(i20, i19) / Math.max(66, Math.min(AndroidUtilities.getRealScreenSize().x, AndroidUtilities.getRealScreenSize().y))) * 6.0f;
                                                            if (min7 < 1.0f) {
                                                                min7 = 1.0f;
                                                            }
                                                            if (min7 > 1.0f) {
                                                                int i21 = 1;
                                                                do {
                                                                    i21 *= 2;
                                                                } while (i21 * 2 <= min7);
                                                                options2.inSampleSize = i21;
                                                            } else {
                                                                options2.inSampleSize = (int) min7;
                                                            }
                                                            f3 = 0.0f;
                                                        } catch (Throwable th20) {
                                                            th2 = th20;
                                                            f = 0.0f;
                                                            f2 = 0.0f;
                                                            c = 0;
                                                            z6 = false;
                                                            FileLog.e(th2, !(th2 instanceof FileNotFoundException));
                                                            c2 = c;
                                                            z7 = z6;
                                                            float f922222222 = f2;
                                                            f3 = f;
                                                            f4 = f922222222;
                                                            Bitmap bitmap6222222222 = bitmap;
                                                            ?? r8222222222 = 1;
                                                            if (this.cacheImage.type != 1) {
                                                            }
                                                            Thread.interrupted();
                                                            if (BuildVars.LOGS_ENABLED) {
                                                            }
                                                            if (z8) {
                                                            }
                                                            if (bitmap3 != null) {
                                                            }
                                                            onPostExecute(drawable);
                                                        }
                                                    } catch (Throwable th21) {
                                                        th = th21;
                                                        th2 = th;
                                                        f = 0.0f;
                                                        f2 = 0.0f;
                                                        bitmap = null;
                                                        c = 0;
                                                        z6 = false;
                                                        FileLog.e(th2, !(th2 instanceof FileNotFoundException));
                                                        c2 = c;
                                                        z7 = z6;
                                                        float f9222222222 = f2;
                                                        f3 = f;
                                                        f4 = f9222222222;
                                                        Bitmap bitmap62222222222 = bitmap;
                                                        ?? r82222222222 = 1;
                                                        if (this.cacheImage.type != 1) {
                                                        }
                                                        Thread.interrupted();
                                                        if (BuildVars.LOGS_ENABLED) {
                                                        }
                                                        if (z8) {
                                                        }
                                                        if (bitmap3 != null) {
                                                        }
                                                        onPostExecute(drawable);
                                                    }
                                                } else {
                                                    f3 = 0.0f;
                                                    bitmap = null;
                                                }
                                                f6 = 0.0f;
                                                c = 0;
                                                z6 = false;
                                            }
                                            f4 = f6;
                                            c2 = c;
                                            z7 = z6;
                                            Bitmap bitmap622222222222 = bitmap;
                                            ?? r822222222222 = 1;
                                            if (this.cacheImage.type != 1) {
                                                try {
                                                    ImageLoader.this.lastCacheOutTime = SystemClock.elapsedRealtime();
                                                } catch (Throwable th22) {
                                                    th = th22;
                                                    r822222222222 = bitmap622222222222;
                                                }
                                                synchronized (this.sync) {
                                                    if (this.isCancelled) {
                                                        return;
                                                    }
                                                    try {
                                                        if (z) {
                                                            RandomAccessFile randomAccessFile6 = new RandomAccessFile(file3, "r");
                                                            MappedByteBuffer map = randomAccessFile6.getChannel().map(FileChannel.MapMode.READ_ONLY, 0L, file3.length());
                                                            BitmapFactory.Options options3 = new BitmapFactory.Options();
                                                            options3.inJustDecodeBounds = true;
                                                            Utilities.loadWebpImage(null, map, map.limit(), options3, true);
                                                            bitmap2 = Bitmaps.createBitmap(options3.outWidth, options3.outHeight, Bitmap.Config.ARGB_8888);
                                                            Utilities.loadWebpImage(bitmap2, map, map.limit(), null, !options2.inPurgeable);
                                                            randomAccessFile6.close();
                                                        } else {
                                                            if (!options2.inPurgeable && secureDocumentKey == null) {
                                                                if (z31) {
                                                                    fileInputStream = new EncryptedFileInputStream(file3, this.cacheImage.encryptionKeyPath);
                                                                } else {
                                                                    fileInputStream = new FileInputStream(file3);
                                                                }
                                                                bitmap2 = BitmapFactory.decodeStream(fileInputStream, null, options2);
                                                                fileInputStream.close();
                                                            }
                                                            RandomAccessFile randomAccessFile7 = new RandomAccessFile(file3, "r");
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
                                                                    z9 = false;
                                                                    i2 = bArr5[0] & 255;
                                                                    length2 -= i2;
                                                                }
                                                                z9 = true;
                                                                i2 = bArr5[0] & 255;
                                                                length2 -= i2;
                                                            } else {
                                                                if (z31) {
                                                                    EncryptedFileInputStream.decryptBytesWithKeyFile(bArr5, 0, length2, this.cacheImage.encryptionKeyPath);
                                                                }
                                                                z9 = false;
                                                                i2 = 0;
                                                            }
                                                            bitmap2 = !z9 ? BitmapFactory.decodeByteArray(bArr5, i2, length2, options2) : bitmap622222222222;
                                                        }
                                                        if (bitmap2 == null) {
                                                            if (file3.length() == 0 || this.cacheImage.filter == null) {
                                                                file3.delete();
                                                            }
                                                            z8 = false;
                                                        } else {
                                                            if (this.cacheImage.filter != null) {
                                                                float width = bitmap2.getWidth();
                                                                float height = bitmap2.getHeight();
                                                                if (!options2.inPurgeable && f4 != 0.0f && width != f4 && width > f4 + 20.0f) {
                                                                    Bitmap createScaledBitmap2 = Bitmaps.createScaledBitmap(bitmap2, (int) f4, (int) (height / (width / f4)), true);
                                                                    if (bitmap2 != createScaledBitmap2) {
                                                                        bitmap2.recycle();
                                                                        bitmap2 = createScaledBitmap2;
                                                                    }
                                                                }
                                                            }
                                                            z8 = z7 ? Utilities.needInvert(bitmap2, options2.inPurgeable ? 0 : 1, bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getRowBytes()) != 0 : false;
                                                            try {
                                                                if (c2 == 1) {
                                                                    if (bitmap2.getConfig() == Bitmap.Config.ARGB_8888) {
                                                                        Utilities.blurBitmap(bitmap2, 3, options2.inPurgeable ? 0 : 1, bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getRowBytes());
                                                                    }
                                                                } else if (c2 == 2) {
                                                                    if (bitmap2.getConfig() == Bitmap.Config.ARGB_8888) {
                                                                        Utilities.blurBitmap(bitmap2, 1, options2.inPurgeable ? 0 : 1, bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getRowBytes());
                                                                    }
                                                                } else if (c2 == 3) {
                                                                    if (bitmap2.getConfig() == Bitmap.Config.ARGB_8888) {
                                                                        Utilities.blurBitmap(bitmap2, 7, options2.inPurgeable ? 0 : 1, bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getRowBytes());
                                                                        Utilities.blurBitmap(bitmap2, 7, options2.inPurgeable ? 0 : 1, bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getRowBytes());
                                                                        Utilities.blurBitmap(bitmap2, 7, options2.inPurgeable ? 0 : 1, bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getRowBytes());
                                                                    }
                                                                } else if (c2 == 0) {
                                                                    if (options2.inPurgeable) {
                                                                        Utilities.pinBitmap(bitmap2);
                                                                    }
                                                                }
                                                            } catch (Throwable th23) {
                                                                th = th23;
                                                                bitmap2 = bitmap2;
                                                                FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                i = 0;
                                                                drawable = null;
                                                                bitmap3 = bitmap2;
                                                                Thread.interrupted();
                                                                if (BuildVars.LOGS_ENABLED) {
                                                                }
                                                                if (z8) {
                                                                }
                                                                if (bitmap3 != null) {
                                                                }
                                                                onPostExecute(drawable);
                                                            }
                                                        }
                                                    } catch (Throwable th24) {
                                                        th = th24;
                                                        z8 = false;
                                                        bitmap2 = r822222222222;
                                                        FileLog.e(th, !(th instanceof FileNotFoundException));
                                                        i = 0;
                                                        drawable = null;
                                                        bitmap3 = bitmap2;
                                                        Thread.interrupted();
                                                        if (BuildVars.LOGS_ENABLED) {
                                                        }
                                                        if (z8) {
                                                        }
                                                        if (bitmap3 != null) {
                                                        }
                                                        onPostExecute(drawable);
                                                    }
                                                    i = 0;
                                                    drawable = null;
                                                    bitmap3 = bitmap2;
                                                }
                                            } else {
                                                int i22 = l != null ? 0 : 20;
                                                if (i22 != 0) {
                                                    try {
                                                    } catch (Throwable th25) {
                                                        th = th25;
                                                        bitmap4 = bitmap622222222222;
                                                        i = 0;
                                                        drawable = null;
                                                        z11 = false;
                                                        FileLog.e(th, !(th instanceof FileNotFoundException));
                                                        z8 = z11;
                                                        bitmap3 = bitmap4;
                                                        Thread.interrupted();
                                                        if (BuildVars.LOGS_ENABLED) {
                                                        }
                                                        if (z8) {
                                                        }
                                                        if (bitmap3 != null) {
                                                        }
                                                        onPostExecute(drawable);
                                                    }
                                                    if (ImageLoader.this.lastCacheOutTime != 0) {
                                                        z10 = z7;
                                                        long j3 = i22;
                                                        if (ImageLoader.this.lastCacheOutTime > SystemClock.elapsedRealtime() - j3) {
                                                            f5 = f3;
                                                            if (Build.VERSION.SDK_INT < 21) {
                                                                Thread.sleep(j3);
                                                            }
                                                        } else {
                                                            f5 = f3;
                                                        }
                                                        ImageLoader.this.lastCacheOutTime = SystemClock.elapsedRealtime();
                                                        synchronized (this.sync) {
                                                            try {
                                                                if (this.isCancelled) {
                                                                    return;
                                                                }
                                                                if (!z322) {
                                                                    CacheImage cacheImage7 = this.cacheImage;
                                                                    if (cacheImage7.filter != null && c2 == 0 && cacheImage7.imageLocation.path == null) {
                                                                        options2.inPreferredConfig = Bitmap.Config.RGB_565;
                                                                        options2.inDither = false;
                                                                        if (l != null || str2 != null) {
                                                                            bitmap4 = bitmap622222222222;
                                                                        } else if (z4) {
                                                                            if (l.longValue() == 0) {
                                                                                AnimatedFileDrawable animatedFileDrawable2 = new AnimatedFileDrawable(file3, true, 0L, 0, null, null, null, 0L, 0, true, null);
                                                                                bitmap4 = animatedFileDrawable2.getFrameAtTime(0L, true);
                                                                                try {
                                                                                    animatedFileDrawable2.recycle();
                                                                                } catch (Throwable th26) {
                                                                                    th = th26;
                                                                                    i = 0;
                                                                                    drawable = null;
                                                                                    z11 = false;
                                                                                    FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                                    z8 = z11;
                                                                                    bitmap3 = bitmap4;
                                                                                    Thread.interrupted();
                                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                                    }
                                                                                    if (z8) {
                                                                                    }
                                                                                    if (bitmap3 != null) {
                                                                                    }
                                                                                    onPostExecute(drawable);
                                                                                }
                                                                            } else {
                                                                                bitmap4 = MediaStore.Video.Thumbnails.getThumbnail(ApplicationLoader.applicationContext.getContentResolver(), l.longValue(), 1, options2);
                                                                            }
                                                                        } else {
                                                                            bitmap4 = MediaStore.Images.Thumbnails.getThumbnail(ApplicationLoader.applicationContext.getContentResolver(), l.longValue(), 1, options2);
                                                                        }
                                                                        if (bitmap4 == null) {
                                                                            if (z) {
                                                                                RandomAccessFile randomAccessFile8 = new RandomAccessFile(file3, "r");
                                                                                MappedByteBuffer map2 = randomAccessFile8.getChannel().map(FileChannel.MapMode.READ_ONLY, 0L, file3.length());
                                                                                BitmapFactory.Options options4 = new BitmapFactory.Options();
                                                                                options4.inJustDecodeBounds = true;
                                                                                try {
                                                                                    Utilities.loadWebpImage(null, map2, map2.limit(), options4, true);
                                                                                    Bitmap createBitmap = Bitmaps.createBitmap(options4.outWidth, options4.outHeight, Bitmap.Config.ARGB_8888);
                                                                                    try {
                                                                                        Utilities.loadWebpImage(createBitmap, map2, map2.limit(), null, !options2.inPurgeable);
                                                                                        randomAccessFile8.close();
                                                                                        i = 0;
                                                                                        drawable = null;
                                                                                        bitmap4 = createBitmap;
                                                                                    } catch (Throwable th27) {
                                                                                        th = th27;
                                                                                        drawable = null;
                                                                                        bitmap4 = createBitmap;
                                                                                        i = 0;
                                                                                        z11 = false;
                                                                                        FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                                        z8 = z11;
                                                                                        bitmap3 = bitmap4;
                                                                                        Thread.interrupted();
                                                                                        if (BuildVars.LOGS_ENABLED) {
                                                                                        }
                                                                                        if (z8) {
                                                                                        }
                                                                                        if (bitmap3 != null) {
                                                                                        }
                                                                                        onPostExecute(drawable);
                                                                                    }
                                                                                } catch (Throwable th28) {
                                                                                    th = th28;
                                                                                    drawable = null;
                                                                                    bitmap4 = bitmap4;
                                                                                }
                                                                            } else {
                                                                                try {
                                                                                    RandomAccessFile randomAccessFile9 = new RandomAccessFile(file3, "r");
                                                                                    int length3 = (int) randomAccessFile9.length();
                                                                                    byte[] bArr6 = (byte[]) ImageLoader.bytesLocal.get();
                                                                                    if (bArr6 == null || bArr6.length < length3) {
                                                                                        bArr6 = null;
                                                                                    }
                                                                                    if (bArr6 == null) {
                                                                                        bArr6 = new byte[length3];
                                                                                        ImageLoader.bytesLocal.set(bArr6);
                                                                                    }
                                                                                    randomAccessFile9.readFully(bArr6, 0, length3);
                                                                                    randomAccessFile9.close();
                                                                                    if (secureDocumentKey != null) {
                                                                                        EncryptedFileInputStream.decryptBytesWithKeyFile(bArr6, 0, length3, secureDocumentKey);
                                                                                        byte[] computeSHA2563 = Utilities.computeSHA256(bArr6, 0, length3);
                                                                                        if (bArr != null && Arrays.equals(computeSHA2563, bArr)) {
                                                                                            z13 = false;
                                                                                            i3 = bArr6[0] & 255;
                                                                                            length3 -= i3;
                                                                                        }
                                                                                        z13 = true;
                                                                                        i3 = bArr6[0] & 255;
                                                                                        length3 -= i3;
                                                                                    } else {
                                                                                        if (z31) {
                                                                                            EncryptedFileInputStream.decryptBytesWithKeyFile(bArr6, 0, length3, this.cacheImage.encryptionKeyPath);
                                                                                        }
                                                                                        z13 = false;
                                                                                        i3 = 0;
                                                                                    }
                                                                                    if (!z13) {
                                                                                        bitmap4 = BitmapFactory.decodeByteArray(bArr6, i3, length3, options2);
                                                                                    }
                                                                                } catch (Throwable unused) {
                                                                                }
                                                                                if (bitmap4 == null) {
                                                                                    if (z31) {
                                                                                        fileInputStream2 = new EncryptedFileInputStream(file3, this.cacheImage.encryptionKeyPath);
                                                                                    } else {
                                                                                        try {
                                                                                            fileInputStream2 = new FileInputStream(file3);
                                                                                        } catch (Throwable th29) {
                                                                                            th = th29;
                                                                                            drawable = null;
                                                                                            bitmap4 = bitmap4;
                                                                                            i = 0;
                                                                                            z11 = false;
                                                                                            FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                                            z8 = z11;
                                                                                            bitmap3 = bitmap4;
                                                                                            Thread.interrupted();
                                                                                            if (BuildVars.LOGS_ENABLED) {
                                                                                            }
                                                                                            if (z8) {
                                                                                            }
                                                                                            if (bitmap3 != null) {
                                                                                            }
                                                                                            onPostExecute(drawable);
                                                                                        }
                                                                                    }
                                                                                    if (this.cacheImage.imageLocation.document instanceof TLRPC$TL_document) {
                                                                                        try {
                                                                                            attributeInt = new ExifInterface(fileInputStream2).getAttributeInt("Orientation", 1);
                                                                                        } catch (Throwable unused2) {
                                                                                        }
                                                                                        if (attributeInt == 3) {
                                                                                            i = 180;
                                                                                        } else if (attributeInt != 6) {
                                                                                            if (attributeInt == 8) {
                                                                                                i = 270;
                                                                                            }
                                                                                            i = 0;
                                                                                        } else {
                                                                                            i = 90;
                                                                                        }
                                                                                        try {
                                                                                            fileInputStream2.getChannel().position(0L);
                                                                                        } catch (Throwable th30) {
                                                                                            th = th30;
                                                                                            drawable = null;
                                                                                            z11 = false;
                                                                                            FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                                            z8 = z11;
                                                                                            bitmap3 = bitmap4;
                                                                                            Thread.interrupted();
                                                                                            if (BuildVars.LOGS_ENABLED) {
                                                                                            }
                                                                                            if (z8) {
                                                                                            }
                                                                                            if (bitmap3 != null) {
                                                                                            }
                                                                                            onPostExecute(drawable);
                                                                                        }
                                                                                    } else {
                                                                                        i = 0;
                                                                                    }
                                                                                    drawable = null;
                                                                                    try {
                                                                                        Bitmap decodeStream = BitmapFactory.decodeStream(fileInputStream2, null, options2);
                                                                                        fileInputStream2.close();
                                                                                        bitmap4 = decodeStream;
                                                                                    } catch (Throwable th31) {
                                                                                        th = th31;
                                                                                        z11 = false;
                                                                                        FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                                        z8 = z11;
                                                                                        bitmap3 = bitmap4;
                                                                                        Thread.interrupted();
                                                                                        if (BuildVars.LOGS_ENABLED) {
                                                                                        }
                                                                                        if (z8) {
                                                                                        }
                                                                                        if (bitmap3 != null) {
                                                                                        }
                                                                                        onPostExecute(drawable);
                                                                                    }
                                                                                }
                                                                            }
                                                                            if (bitmap4 == null) {
                                                                                if (z5 && (file3.length() == 0 || this.cacheImage.filter == null)) {
                                                                                    file3.delete();
                                                                                }
                                                                                z11 = false;
                                                                            } else {
                                                                                if (this.cacheImage.filter != null) {
                                                                                    float width2 = bitmap4.getWidth();
                                                                                    float height2 = bitmap4.getHeight();
                                                                                    if (!options2.inPurgeable && f4 != 0.0f && width2 != f4 && width2 > f4 + 20.0f) {
                                                                                        if (width2 <= height2 || f4 <= f5) {
                                                                                            float f12 = height2 / f5;
                                                                                            if (f12 > 1.0f) {
                                                                                                createScaledBitmap = Bitmaps.createScaledBitmap(bitmap4, (int) (width2 / f12), (int) f5, true);
                                                                                                if (bitmap4 != createScaledBitmap) {
                                                                                                    bitmap4.recycle();
                                                                                                    bitmap4 = createScaledBitmap;
                                                                                                }
                                                                                            }
                                                                                            createScaledBitmap = bitmap4;
                                                                                            if (bitmap4 != createScaledBitmap) {
                                                                                            }
                                                                                        } else {
                                                                                            float f13 = width2 / f4;
                                                                                            if (f13 > 1.0f) {
                                                                                                createScaledBitmap = Bitmaps.createScaledBitmap(bitmap4, (int) f4, (int) (height2 / f13), true);
                                                                                                if (bitmap4 != createScaledBitmap) {
                                                                                                }
                                                                                            }
                                                                                            createScaledBitmap = bitmap4;
                                                                                            if (bitmap4 != createScaledBitmap) {
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if (bitmap4 != null) {
                                                                                        if (z10) {
                                                                                            Bitmap createScaledBitmap3 = bitmap4.getWidth() * bitmap4.getHeight() > 22500 ? Bitmaps.createScaledBitmap(bitmap4, 100, 100, false) : bitmap4;
                                                                                            z11 = Utilities.needInvert(createScaledBitmap3, options2.inPurgeable ? 0 : 1, createScaledBitmap3.getWidth(), createScaledBitmap3.getHeight(), createScaledBitmap3.getRowBytes()) != 0;
                                                                                            if (createScaledBitmap3 != bitmap4) {
                                                                                                try {
                                                                                                    createScaledBitmap3.recycle();
                                                                                                } catch (Throwable th32) {
                                                                                                    th = th32;
                                                                                                    FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                                                    z8 = z11;
                                                                                                    bitmap3 = bitmap4;
                                                                                                    Thread.interrupted();
                                                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                                                    }
                                                                                                    if (z8) {
                                                                                                    }
                                                                                                    if (bitmap3 != null) {
                                                                                                    }
                                                                                                    onPostExecute(drawable);
                                                                                                }
                                                                                            }
                                                                                        } else {
                                                                                            z11 = false;
                                                                                        }
                                                                                        if (c2 != 0 && (height2 > 100.0f || width2 > 100.0f)) {
                                                                                            height2 = 80.0f;
                                                                                            bitmap4 = Bitmaps.createScaledBitmap(bitmap4, 80, 80, false);
                                                                                            width2 = 80.0f;
                                                                                        }
                                                                                        if (c2 == 0 || height2 >= 100.0f || width2 >= 100.0f) {
                                                                                            z12 = false;
                                                                                        } else {
                                                                                            if (bitmap4.getConfig() == Bitmap.Config.ARGB_8888) {
                                                                                                Utilities.blurBitmap(bitmap4, 3, options2.inPurgeable ? 0 : 1, bitmap4.getWidth(), bitmap4.getHeight(), bitmap4.getRowBytes());
                                                                                            }
                                                                                            z12 = true;
                                                                                        }
                                                                                        if (!z12 && options2.inPurgeable) {
                                                                                            Utilities.pinBitmap(bitmap4);
                                                                                        }
                                                                                    }
                                                                                }
                                                                                z12 = false;
                                                                                z11 = false;
                                                                                if (!z12) {
                                                                                    Utilities.pinBitmap(bitmap4);
                                                                                }
                                                                            }
                                                                            z8 = z11;
                                                                            bitmap3 = bitmap4;
                                                                        }
                                                                        drawable = null;
                                                                        i = 0;
                                                                        bitmap4 = bitmap4;
                                                                        if (bitmap4 == null) {
                                                                        }
                                                                        z8 = z11;
                                                                        bitmap3 = bitmap4;
                                                                    }
                                                                }
                                                                options2.inPreferredConfig = Bitmap.Config.ARGB_8888;
                                                                options2.inDither = false;
                                                                if (l != null) {
                                                                }
                                                                bitmap4 = bitmap622222222222;
                                                                if (bitmap4 == null) {
                                                                }
                                                                drawable = null;
                                                                i = 0;
                                                                bitmap4 = bitmap4;
                                                                if (bitmap4 == null) {
                                                                }
                                                                z8 = z11;
                                                                bitmap3 = bitmap4;
                                                            } finally {
                                                                th = th;
                                                                drawable = null;
                                                                while (true) {
                                                                    try {
                                                                        try {
                                                                            break;
                                                                        } catch (Throwable th33) {
                                                                            th = th33;
                                                                            bitmap4 = bitmap622222222222;
                                                                            i = 0;
                                                                            z11 = false;
                                                                            FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                            z8 = z11;
                                                                            bitmap3 = bitmap4;
                                                                            Thread.interrupted();
                                                                            if (BuildVars.LOGS_ENABLED) {
                                                                            }
                                                                            if (z8) {
                                                                            }
                                                                            if (bitmap3 != null) {
                                                                            }
                                                                            onPostExecute(drawable);
                                                                        }
                                                                    } catch (Throwable th34) {
                                                                        th = th34;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                f5 = f3;
                                                z10 = z7;
                                                ImageLoader.this.lastCacheOutTime = SystemClock.elapsedRealtime();
                                                synchronized (this.sync) {
                                                }
                                            }
                                            Thread.interrupted();
                                            if (BuildVars.LOGS_ENABLED && z31) {
                                                StringBuilder sb = new StringBuilder();
                                                sb.append("Image Loader image is empty = ");
                                                sb.append(bitmap3 != null);
                                                sb.append(" ");
                                                sb.append(file3);
                                                FileLog.e(sb.toString());
                                            }
                                            if (z8 && i == 0) {
                                                if (bitmap3 != null) {
                                                    drawable = new BitmapDrawable(bitmap3);
                                                }
                                                onPostExecute(drawable);
                                                return;
                                            }
                                            if (bitmap3 != null) {
                                                drawable = new ExtendedBitmapDrawable(bitmap3, z8, i);
                                            }
                                            onPostExecute(drawable);
                                        } else if (!str.startsWith("http")) {
                                            str2 = null;
                                            l = null;
                                        }
                                        z2 = false;
                                        z3 = false;
                                        BitmapFactory.Options options22 = new BitmapFactory.Options();
                                        options22.inSampleSize = 1;
                                        if (Build.VERSION.SDK_INT < 21) {
                                        }
                                        boolean z3222 = ImageLoader.this.canForce8888;
                                        str3 = this.cacheImage.filter;
                                        if (str3 == null) {
                                        }
                                        f4 = f6;
                                        c2 = c;
                                        z7 = z6;
                                        Bitmap bitmap6222222222222 = bitmap;
                                        ?? r8222222222222 = 1;
                                        if (this.cacheImage.type != 1) {
                                        }
                                        Thread.interrupted();
                                        if (BuildVars.LOGS_ENABLED) {
                                            StringBuilder sb2 = new StringBuilder();
                                            sb2.append("Image Loader image is empty = ");
                                            sb2.append(bitmap3 != null);
                                            sb2.append(" ");
                                            sb2.append(file3);
                                            FileLog.e(sb2.toString());
                                        }
                                        if (z8) {
                                        }
                                        if (bitmap3 != null) {
                                        }
                                        onPostExecute(drawable);
                                    }
                                    str2 = null;
                                    l = null;
                                    z2 = false;
                                    z3 = true;
                                    BitmapFactory.Options options222 = new BitmapFactory.Options();
                                    options222.inSampleSize = 1;
                                    if (Build.VERSION.SDK_INT < 21) {
                                    }
                                    boolean z32222 = ImageLoader.this.canForce8888;
                                    str3 = this.cacheImage.filter;
                                    if (str3 == null) {
                                    }
                                    f4 = f6;
                                    c2 = c;
                                    z7 = z6;
                                    Bitmap bitmap62222222222222 = bitmap;
                                    ?? r82222222222222 = 1;
                                    if (this.cacheImage.type != 1) {
                                    }
                                    Thread.interrupted();
                                    if (BuildVars.LOGS_ENABLED) {
                                    }
                                    if (z8) {
                                    }
                                    if (bitmap3 != null) {
                                    }
                                    onPostExecute(drawable);
                                }
                            }
                            z = false;
                            randomAccessFile2.close();
                            str = this.cacheImage.imageLocation.path;
                            if (str != null) {
                            }
                            str2 = null;
                            l = null;
                            z2 = false;
                            z3 = true;
                            BitmapFactory.Options options2222 = new BitmapFactory.Options();
                            options2222.inSampleSize = 1;
                            if (Build.VERSION.SDK_INT < 21) {
                            }
                            boolean z322222 = ImageLoader.this.canForce8888;
                            str3 = this.cacheImage.filter;
                            if (str3 == null) {
                            }
                            f4 = f6;
                            c2 = c;
                            z7 = z6;
                            Bitmap bitmap622222222222222 = bitmap;
                            ?? r822222222222222 = 1;
                            if (this.cacheImage.type != 1) {
                            }
                            Thread.interrupted();
                            if (BuildVars.LOGS_ENABLED) {
                            }
                            if (z8) {
                            }
                            if (bitmap3 != null) {
                            }
                            onPostExecute(drawable);
                        }
                        z = false;
                        str = this.cacheImage.imageLocation.path;
                        if (str != null) {
                        }
                        str2 = null;
                        l = null;
                        z2 = false;
                        z3 = true;
                        BitmapFactory.Options options22222 = new BitmapFactory.Options();
                        options22222.inSampleSize = 1;
                        if (Build.VERSION.SDK_INT < 21) {
                        }
                        boolean z3222222 = ImageLoader.this.canForce8888;
                        str3 = this.cacheImage.filter;
                        if (str3 == null) {
                        }
                        f4 = f6;
                        c2 = c;
                        z7 = z6;
                        Bitmap bitmap6222222222222222 = bitmap;
                        ?? r8222222222222222 = 1;
                        if (this.cacheImage.type != 1) {
                        }
                        Thread.interrupted();
                        if (BuildVars.LOGS_ENABLED) {
                        }
                        if (z8) {
                        }
                        if (bitmap3 != null) {
                        }
                        onPostExecute(drawable);
                    } catch (Throwable th35) {
                        th = th35;
                        randomAccessFile = 19;
                    }
                }
            }
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
        bArr2 = (bArr2 == null || bArr2.length < length) ? null : null;
        if (bArr2 == null) {
            bArr2 = new byte[length];
            bytesLocal.set(bArr2);
        }
        byte[] bArr3 = Bitmaps.header;
        System.arraycopy(bArr3, 0, bArr2, 0, bArr3.length);
        System.arraycopy(bArr, 3, bArr2, Bitmaps.header.length, bArr.length - 3);
        System.arraycopy(Bitmaps.footer, 0, bArr2, (Bitmaps.header.length + bArr.length) - 3, Bitmaps.footer.length);
        bArr2[164] = bArr[1];
        bArr2[166] = bArr[2];
        Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bArr2, 0, length);
        if (decodeByteArray != null && !TextUtils.isEmpty(str) && str.contains("b")) {
            Utilities.blurBitmap(decodeByteArray, 3, 1, decodeByteArray.getWidth(), decodeByteArray.getHeight(), decodeByteArray.getRowBytes());
        }
        return decodeByteArray;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class CacheImage {
        protected ArtworkLoadTask artworkTask;
        protected CacheOutTask cacheTask;
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
                if (this.key != null) {
                    ImageLoader.this.imageLoadingByKeys.remove(this.key);
                }
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
                return bitmapDrawable.getBitmap().getByteCount();
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
                return bitmapDrawable.getBitmap().getByteCount();
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
                return bitmapDrawable.getBitmap().getByteCount();
            }
        };
        this.lottieMemCache = new LruCache<BitmapDrawable>(10485760) { // from class: org.telegram.messenger.ImageLoader.4
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.messenger.LruCache
            public int sizeOf(String str, BitmapDrawable bitmapDrawable) {
                return bitmapDrawable.getIntrinsicWidth() * bitmapDrawable.getIntrinsicHeight() * 4 * 2;
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
    /* loaded from: classes.dex */
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
            NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.fileUploadProgressChanged, str, Long.valueOf(j), Long.valueOf(j2), Boolean.valueOf(z));
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
            NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.fileUploaded, str, tLRPC$InputFile, tLRPC$InputEncryptedFile, bArr, bArr2, Long.valueOf(j));
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
            NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.fileUploadFailed, str, Boolean.valueOf(z));
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
            NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.fileLoaded, str, file);
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
            NotificationCenter.getInstance(i2).postNotificationName(NotificationCenter.fileLoadFailed, str, Integer.valueOf(i));
        }

        @Override // org.telegram.messenger.FileLoader.FileLoaderDelegate
        public void fileLoadProgressChanged(FileLoadOperation fileLoadOperation, final String str, final long j, final long j2) {
            ImageLoader.this.fileProgresses.put(str, new long[]{j, j2});
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long j3 = fileLoadOperation.lastProgressUpdateTime;
            if (j3 == 0 || j3 < elapsedRealtime - 500 || j == 0) {
                fileLoadOperation.lastProgressUpdateTime = elapsedRealtime;
                final int i = this.val$currentAccount;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ImageLoader.5.lambda$fileLoadProgressChanged$7(i, str, j, j2);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$fileLoadProgressChanged$7(int i, String str, long j, long j2) {
            NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.fileLoadProgressChanged, str, Long.valueOf(j), Long.valueOf(j2));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
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

    public void checkMediaPaths() {
        this.cacheOutQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.lambda$checkMediaPaths$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkMediaPaths$1() {
        final SparseArray<File> createMediaPaths = createMediaPaths();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                FileLoader.setMediaDirs(createMediaPaths);
            }
        });
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
                    convert.forEach(new Consumer() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda13
                        @Override // j$.util.function.Consumer
                        public final void accept(Object obj) {
                            ImageLoader.lambda$moveDirectory$2(file2, (Path) obj);
                        }

                        @Override // j$.util.function.Consumer
                        public /* synthetic */ Consumer andThen(Consumer consumer) {
                            return Objects.requireNonNull(consumer);
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
    public static /* synthetic */ void lambda$moveDirectory$2(File file, Path path) {
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

    /* JADX WARN: Code restructure failed: missing block: B:47:0x00cb, code lost:
        if (r2.mkdirs() != false) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00d1, code lost:
        if (r2.canWrite() == false) goto L138;
     */
    /* JADX WARN: Removed duplicated region for block: B:128:0x028a A[Catch: Exception -> 0x029d, TRY_LEAVE, TryCatch #4 {Exception -> 0x029d, blocks: (B:122:0x026d, B:124:0x027b, B:126:0x0281, B:128:0x028a), top: B:158:0x026d, outer: #8 }] */
    /* JADX WARN: Removed duplicated region for block: B:138:0x02be A[Catch: Exception -> 0x02d1, TRY_LEAVE, TryCatch #10 {Exception -> 0x02d1, blocks: (B:132:0x02a1, B:134:0x02af, B:136:0x02b5, B:138:0x02be), top: B:170:0x02a1, outer: #8 }] */
    /* JADX WARN: Removed duplicated region for block: B:162:0x013a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:177:0x0128 A[EDGE_INSN: B:177:0x0128->B:66:0x0128 ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00fe A[Catch: Exception -> 0x02e4, TryCatch #8 {Exception -> 0x02e4, blocks: (B:11:0x003d, B:13:0x0049, B:15:0x0056, B:17:0x005c, B:19:0x0063, B:22:0x0077, B:23:0x007a, B:40:0x00ab, B:52:0x00e0, B:54:0x00eb, B:56:0x00f3, B:58:0x00fe, B:60:0x0106, B:62:0x010e, B:64:0x011a, B:65:0x0125, B:66:0x0128, B:118:0x0262, B:108:0x0221, B:98:0x01e0, B:88:0x019f, B:120:0x0267, B:141:0x02d2, B:131:0x029e, B:145:0x02e0, B:78:0x016b, B:39:0x00a8, B:41:0x00ba, B:43:0x00c0, B:48:0x00cd, B:51:0x00d9, B:50:0x00d3, B:46:0x00c7, B:142:0x02d6, B:144:0x02da, B:99:0x01e3, B:101:0x01f5, B:103:0x01fc, B:105:0x020b, B:89:0x01a2, B:91:0x01b4, B:93:0x01bb, B:95:0x01ca, B:79:0x016e, B:81:0x017e, B:83:0x0184, B:85:0x018b, B:122:0x026d, B:124:0x027b, B:126:0x0281, B:128:0x028a, B:69:0x013a, B:71:0x014a, B:73:0x0150, B:75:0x0157, B:132:0x02a1, B:134:0x02af, B:136:0x02b5, B:138:0x02be, B:109:0x0224, B:111:0x0236, B:113:0x023d, B:115:0x024c), top: B:166:0x003d, inners: #0, #1, #3, #4, #6, #10, #11 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public SparseArray<File> createMediaPaths() {
        File file;
        File file2;
        File file3;
        int size;
        int i;
        ArrayList<File> rootDirs;
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
        try {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                File externalStorageDirectory = Environment.getExternalStorageDirectory();
                if (!TextUtils.isEmpty(SharedConfig.storageCacheDir) && (rootDirs = AndroidUtilities.getRootDirs()) != null) {
                    int size2 = rootDirs.size();
                    int i2 = 0;
                    while (true) {
                        if (i2 >= size2) {
                            break;
                        }
                        File file4 = rootDirs.get(i2);
                        if (file4.getAbsolutePath().startsWith(SharedConfig.storageCacheDir)) {
                            externalStorageDirectory = file4;
                            break;
                        }
                        i2++;
                    }
                }
                File file5 = null;
                if (Build.VERSION.SDK_INT >= 30) {
                    try {
                        if (ApplicationLoader.applicationContext.getExternalMediaDirs().length > 0) {
                            File file6 = ApplicationLoader.applicationContext.getExternalMediaDirs()[0];
                            try {
                                file = new File(file6, "Telegram");
                            } catch (Exception e2) {
                                file = file6;
                                e = e2;
                            }
                            try {
                                file.mkdirs();
                            } catch (Exception e3) {
                                e = e3;
                                FileLog.e(e);
                                this.telegramPath = new File(ApplicationLoader.applicationContext.getExternalFilesDir(null), "Telegram");
                                file5 = file;
                                this.telegramPath.mkdirs();
                                if (Build.VERSION.SDK_INT >= 19) {
                                    ArrayList<File> dataDirs = AndroidUtilities.getDataDirs();
                                    size = dataDirs.size();
                                    i = 0;
                                    while (true) {
                                        if (i < size) {
                                        }
                                        i++;
                                    }
                                }
                                if (this.telegramPath.isDirectory()) {
                                }
                                if (file5 != null) {
                                    try {
                                        file3 = new File(file5, "Telegram Images");
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
                                        file2 = new File(file5, "Telegram Video");
                                        file2.mkdir();
                                        if (file2.isDirectory()) {
                                            sparseArray.put(FileLoader.MEDIA_DIR_VIDEO_PUBLIC, file2);
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
                    file5 = file;
                } else {
                    if (externalStorageDirectory.exists()) {
                        if (externalStorageDirectory.isDirectory()) {
                        }
                        externalStorageDirectory = ApplicationLoader.applicationContext.getExternalFilesDir(null);
                    }
                    this.telegramPath = new File(externalStorageDirectory, "Telegram");
                }
                this.telegramPath.mkdirs();
                if (Build.VERSION.SDK_INT >= 19 && !this.telegramPath.isDirectory()) {
                    ArrayList<File> dataDirs2 = AndroidUtilities.getDataDirs();
                    size = dataDirs2.size();
                    i = 0;
                    while (true) {
                        if (i < size) {
                            File file7 = dataDirs2.get(i);
                            if (file7 != null && !TextUtils.isEmpty(SharedConfig.storageCacheDir) && file7.getAbsolutePath().startsWith(SharedConfig.storageCacheDir)) {
                                File file8 = new File(file7, "Telegram");
                                this.telegramPath = file8;
                                file8.mkdirs();
                                break;
                            }
                            i++;
                        } else {
                            break;
                        }
                    }
                }
                if (this.telegramPath.isDirectory()) {
                    try {
                        File file9 = new File(this.telegramPath, "Telegram Images");
                        file9.mkdir();
                        if (file9.isDirectory() && canMoveFiles(cacheDir, file9, 0)) {
                            sparseArray.put(0, file9);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("image path = " + file9);
                            }
                        }
                    } catch (Exception e7) {
                        FileLog.e(e7);
                    }
                    try {
                        File file10 = new File(this.telegramPath, "Telegram Video");
                        file10.mkdir();
                        if (file10.isDirectory() && canMoveFiles(cacheDir, file10, 2)) {
                            sparseArray.put(2, file10);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("video path = " + file10);
                            }
                        }
                    } catch (Exception e8) {
                        FileLog.e(e8);
                    }
                    try {
                        File file11 = new File(this.telegramPath, "Telegram Audio");
                        file11.mkdir();
                        if (file11.isDirectory() && canMoveFiles(cacheDir, file11, 1)) {
                            AndroidUtilities.createEmptyFile(new File(file11, ".nomedia"));
                            sparseArray.put(1, file11);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("audio path = " + file11);
                            }
                        }
                    } catch (Exception e9) {
                        FileLog.e(e9);
                    }
                    try {
                        File file12 = new File(this.telegramPath, "Telegram Documents");
                        file12.mkdir();
                        if (file12.isDirectory() && canMoveFiles(cacheDir, file12, 3)) {
                            AndroidUtilities.createEmptyFile(new File(file12, ".nomedia"));
                            sparseArray.put(3, file12);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("documents path = " + file12);
                            }
                        }
                    } catch (Exception e10) {
                        FileLog.e(e10);
                    }
                    try {
                        File file13 = new File(this.telegramPath, "Telegram Files");
                        file13.mkdir();
                        if (file13.isDirectory() && canMoveFiles(cacheDir, file13, 5)) {
                            AndroidUtilities.createEmptyFile(new File(file13, ".nomedia"));
                            sparseArray.put(5, file13);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("files path = " + file13);
                            }
                        }
                    } catch (Exception e11) {
                        FileLog.e(e11);
                    }
                }
                if (file5 != null && file5.isDirectory()) {
                    file3 = new File(file5, "Telegram Images");
                    file3.mkdir();
                    if (file3.isDirectory() && canMoveFiles(cacheDir, file3, 0)) {
                        sparseArray.put(100, file3);
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("image path = " + file3);
                        }
                    }
                    file2 = new File(file5, "Telegram Video");
                    file2.mkdir();
                    if (file2.isDirectory() && canMoveFiles(cacheDir, file2, 2)) {
                        sparseArray.put(FileLoader.MEDIA_DIR_VIDEO_PUBLIC, file2);
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("video path = " + file2);
                        }
                    }
                }
            } else if (BuildVars.LOGS_ENABLED) {
                FileLog.d("this Android can't rename files");
            }
            SharedConfig.checkSaveToGalleryFiles();
        } catch (Exception e12) {
            FileLog.e(e12);
        }
        return sparseArray;
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
                    if (i != 3 && i != 5) {
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
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.lambda$cancelLoadingForImageReceiver$3(z, imageReceiver);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelLoadingForImageReceiver$3(boolean z, ImageReceiver imageReceiver) {
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
    public void lambda$replaceImageInCache$4(String str, String str2, ImageLocation imageLocation) {
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
                    NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didReplacedPhotoInMemCache, str4, str5, imageLocation);
                }
            } else {
                performReplace(str, str2);
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didReplacedPhotoInMemCache, str, str2, imageLocation);
            }
        }
    }

    public void replaceImageInCache(final String str, final String str2, final ImageLocation imageLocation, boolean z) {
        if (z) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.this.lambda$replaceImageInCache$4(str, str2, imageLocation);
                }
            });
        } else {
            lambda$replaceImageInCache$4(str, str2, imageLocation);
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
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.lambda$cancelForceLoadingForImageReceiver$5(imageKey);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelForceLoadingForImageReceiver$5(String str) {
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
        Runnable runnable = new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.lambda$createLoadOperationForImageReceiver$6(i3, str2, str, i6, imageReceiver, i4, str4, i2, imageLocation, z, parentObject, currentAccount, qualityThumbDocument, isNeedsQualityThumb, isShouldGenerateQualityThumb, str3, i, j);
            }
        };
        this.imageLoadQueue.postRunnable(runnable, imageReceiver.getFileLoadingPriority() == 0 ? 0L : 1L);
        imageReceiver.addLoadingImageRunnable(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:153:0x0319, code lost:
        if (r7 == false) goto L150;
     */
    /* JADX WARN: Code restructure failed: missing block: B:197:0x03f1, code lost:
        if (r7.equals(r8) != false) goto L181;
     */
    /* JADX WARN: Code restructure failed: missing block: B:240:0x04cb, code lost:
        if (r10.exists() == false) goto L204;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x01ba, code lost:
        if (r8.exists() == false) goto L284;
     */
    /* JADX WARN: Removed duplicated region for block: B:206:0x0429  */
    /* JADX WARN: Removed duplicated region for block: B:211:0x043a  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x01af  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x01bf  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x01c2  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x01c5  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0216  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$createLoadOperationForImageReceiver$6(int i, String str, String str2, int i2, ImageReceiver imageReceiver, int i3, String str3, int i4, ImageLocation imageLocation, boolean z, Object obj, int i5, TLRPC$Document tLRPC$Document, boolean z2, boolean z3, String str4, int i6, long j) {
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
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.lambda$preloadArtwork$7(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$preloadArtwork$7(String str) {
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

    /* JADX WARN: Code restructure failed: missing block: B:160:0x0286, code lost:
        if (r6.local_id < 0) goto L130;
     */
    /* JADX WARN: Removed duplicated region for block: B:101:0x01a1  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x01af  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x01b9  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x01c8  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x01cb  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x01ce  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x01d2  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x01e7  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x03a7  */
    /* JADX WARN: Removed duplicated region for block: B:228:0x0412  */
    /* JADX WARN: Removed duplicated region for block: B:231:0x041a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:235:0x0435 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:239:0x0450 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:244:0x0471 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:249:0x048f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:253:0x04a9  */
    /* JADX WARN: Removed duplicated region for block: B:261:0x04e7  */
    /* JADX WARN: Removed duplicated region for block: B:263:0x04ec  */
    /* JADX WARN: Removed duplicated region for block: B:278:0x0556  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x009f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00ab  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00b0  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00b3  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00e6  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0107  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0184  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0194  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void loadImageForImageReceiver(ImageReceiver imageReceiver) {
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
        int i;
        String str2;
        String str3;
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
        String str15;
        ImageLocation imageLocation5;
        String str16;
        int i2;
        String str17;
        ImageLocation imageLocation6;
        boolean z6;
        ImageLocation imageLocation7;
        ImageLocation imageLocation8;
        String str18;
        String str19;
        String str20;
        BitmapDrawable bitmapDrawable;
        BitmapDrawable fromLottieCache;
        BitmapDrawable bitmapDrawable2;
        boolean hasBitmap;
        if (imageReceiver == null) {
            return;
        }
        String mediaKey = imageReceiver.getMediaKey();
        int newGuid = imageReceiver.getNewGuid();
        if (mediaKey != null) {
            if (useLottieMemCache(imageReceiver.getMediaLocation(), mediaKey)) {
                bitmapDrawable2 = getFromLottieCache(mediaKey);
            } else {
                bitmapDrawable2 = this.memCache.get(mediaKey);
                if (bitmapDrawable2 != null) {
                    this.memCache.moveToFront(mediaKey);
                }
                if (bitmapDrawable2 == null && (bitmapDrawable2 = this.smallImagesMemCache.get(mediaKey)) != null) {
                    this.smallImagesMemCache.moveToFront(mediaKey);
                }
                if (bitmapDrawable2 == null && (bitmapDrawable2 = this.wallpaperMemCache.get(mediaKey)) != null) {
                    this.wallpaperMemCache.moveToFront(mediaKey);
                }
            }
            BitmapDrawable bitmapDrawable3 = bitmapDrawable2;
            if (bitmapDrawable3 instanceof RLottieDrawable) {
                hasBitmap = ((RLottieDrawable) bitmapDrawable3).hasBitmap();
            } else {
                hasBitmap = bitmapDrawable3 instanceof AnimatedFileDrawable ? ((AnimatedFileDrawable) bitmapDrawable3).hasBitmap() : true;
            }
            if (hasBitmap && bitmapDrawable3 != null) {
                cancelLoadingForImageReceiver(imageReceiver, true);
                imageReceiver.setImageBitmapByKey(bitmapDrawable3, mediaKey, 3, true, newGuid);
                if (!imageReceiver.isForcePreview()) {
                    return;
                }
                z = true;
                z2 = false;
                imageKey = imageReceiver.getImageKey();
                if (!z) {
                    if (!useLottieMemCache(imageReceiver.getImageLocation(), imageKey)) {
                    }
                    if (fromLottieCache == null) {
                    }
                    if (fromLottieCache != null) {
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
                if (imageLocation2 == null) {
                }
                if (mediaLocation == null) {
                }
                ext = imageReceiver.getExt();
                if (ext == null) {
                }
                if (r8 == null) {
                }
                if (str == null) {
                }
                ImageLocation imageLocation9 = imageLocation2;
                imageLocation3 = mediaLocation;
                ImageLocation imageLocation10 = imageLocation;
                i = 0;
                str2 = null;
                str3 = null;
                String str21 = null;
                String str22 = null;
                boolean z7 = false;
                while (i < 2) {
                }
                boolean z8 = z4;
                str4 = thumbFilter;
                imageLocation4 = imageLocation9;
                str5 = imageFilter;
                str6 = mediaFilter;
                int i3 = newGuid;
                if (thumbLocation != null) {
                }
                if (str2 != null) {
                }
                str9 = str6;
                if (str3 != null) {
                }
                str10 = str5;
                if (str7 != null) {
                }
                str11 = str4;
                str12 = str7;
                if (imageReceiver.getUniqKeyPrefix() != null) {
                }
                str13 = str3;
                if (imageReceiver.getUniqKeyPrefix() != null) {
                }
                str14 = str2;
                if (imageLocation4 == null) {
                }
                if (imageLocation3 != null) {
                }
            } else if (bitmapDrawable3 != null) {
                imageReceiver.setImageBitmapByKey(bitmapDrawable3, mediaKey, 3, true, newGuid);
                z = false;
                z2 = true;
                imageKey = imageReceiver.getImageKey();
                if (!z && imageKey != null) {
                    fromLottieCache = !useLottieMemCache(imageReceiver.getImageLocation(), imageKey) ? getFromLottieCache(imageKey) : null;
                    if (fromLottieCache == null) {
                        fromLottieCache = this.memCache.get(imageKey);
                        if (fromLottieCache != null) {
                            this.memCache.moveToFront(imageKey);
                        }
                        if (fromLottieCache == null && (fromLottieCache = this.smallImagesMemCache.get(imageKey)) != null) {
                            this.smallImagesMemCache.moveToFront(imageKey);
                        }
                        if (fromLottieCache == null && (fromLottieCache = this.wallpaperMemCache.get(imageKey)) != null) {
                            this.wallpaperMemCache.moveToFront(imageKey);
                        }
                    }
                    if (fromLottieCache != null) {
                        cancelLoadingForImageReceiver(imageReceiver, true);
                        imageReceiver.setImageBitmapByKey(fromLottieCache, imageKey, 0, true, newGuid);
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
                                    String str23 = (imageLocation2 == null && imageLocation2.imageType == 2) ? "mp4" : null;
                                    str = (mediaLocation == null && mediaLocation.imageType == 2) ? null : null;
                                    ext = imageReceiver.getExt();
                                    if (ext == null) {
                                        ext = "jpg";
                                    }
                                    String str24 = str23 == null ? ext : str23;
                                    if (str == null) {
                                        str = ext;
                                    }
                                    ImageLocation imageLocation92 = imageLocation2;
                                    imageLocation3 = mediaLocation;
                                    ImageLocation imageLocation102 = imageLocation;
                                    i = 0;
                                    str2 = null;
                                    str3 = null;
                                    String str212 = null;
                                    String str222 = null;
                                    boolean z72 = false;
                                    while (i < 2) {
                                        if (i == 0) {
                                            imageLocation6 = imageLocation92;
                                            i2 = newGuid;
                                            str17 = str24;
                                        } else {
                                            i2 = newGuid;
                                            str17 = str;
                                            imageLocation6 = imageLocation3;
                                        }
                                        if (imageLocation6 == null) {
                                            z6 = z4;
                                            imageLocation8 = imageLocation92;
                                        } else {
                                            z6 = z4;
                                            if (imageLocation3 != null) {
                                                imageLocation8 = imageLocation92;
                                                imageLocation7 = imageLocation3;
                                            } else {
                                                imageLocation7 = imageLocation92;
                                                imageLocation8 = imageLocation7;
                                            }
                                            String key = imageLocation6.getKey(parentObject, imageLocation7, false);
                                            if (key != null) {
                                                str18 = thumbFilter2;
                                                String key2 = imageLocation6.getKey(parentObject, imageLocation3 != null ? imageLocation3 : imageLocation8, true);
                                                if (imageLocation6.path != null) {
                                                    key2 = key2 + "." + getHttpUrlExtension(imageLocation6.path, "jpg");
                                                    str19 = imageFilter2;
                                                    str20 = mediaFilter2;
                                                } else {
                                                    TLRPC$PhotoSize tLRPC$PhotoSize = imageLocation6.photoSize;
                                                    str19 = imageFilter2;
                                                    if ((tLRPC$PhotoSize instanceof TLRPC$TL_photoStrippedSize) || (tLRPC$PhotoSize instanceof TLRPC$TL_photoPathSize)) {
                                                        str20 = mediaFilter2;
                                                        key2 = key2 + "." + str17;
                                                    } else if (imageLocation6.location != null) {
                                                        key2 = key2 + "." + str17;
                                                        if (imageReceiver.getExt() == null) {
                                                            TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated = imageLocation6.location;
                                                            if (tLRPC$TL_fileLocationToBeDeprecated.key == null) {
                                                                str20 = mediaFilter2;
                                                                if (tLRPC$TL_fileLocationToBeDeprecated.volume_id == -2147483648L) {
                                                                }
                                                            }
                                                        }
                                                        str20 = mediaFilter2;
                                                        z72 = true;
                                                    } else {
                                                        str20 = mediaFilter2;
                                                        WebFile webFile = imageLocation6.webFile;
                                                        if (webFile != null) {
                                                            String mimeTypePart = FileLoader.getMimeTypePart(webFile.mime_type);
                                                            key2 = key2 + "." + getHttpUrlExtension(imageLocation6.webFile.url, mimeTypePart);
                                                        } else if (imageLocation6.secureDocument != null) {
                                                            key2 = key2 + "." + str17;
                                                        } else if (imageLocation6.document != null) {
                                                            if (i == 0 && z5) {
                                                                key = "q_" + key;
                                                            }
                                                            String documentFileName = FileLoader.getDocumentFileName(imageLocation6.document);
                                                            int lastIndexOf = documentFileName.lastIndexOf(46);
                                                            String str25 = "";
                                                            String substring = lastIndexOf == -1 ? "" : documentFileName.substring(lastIndexOf);
                                                            if (substring.length() > 1) {
                                                                str25 = substring;
                                                            } else if ("video/mp4".equals(imageLocation6.document.mime_type)) {
                                                                str25 = ".mp4";
                                                            } else if ("video/x-matroska".equals(imageLocation6.document.mime_type)) {
                                                                str25 = ".mkv";
                                                            }
                                                            key2 = key2 + str25;
                                                            z72 = (MessageObject.isVideoDocument(imageLocation6.document) || MessageObject.isGifDocument(imageLocation6.document) || MessageObject.isRoundVideoDocument(imageLocation6.document) || MessageObject.canPreviewDocument(imageLocation6.document)) ? false : true;
                                                        }
                                                    }
                                                }
                                                if (i == 0) {
                                                    str3 = key;
                                                    str212 = key2;
                                                } else {
                                                    str2 = key;
                                                    str222 = key2;
                                                }
                                                if (imageLocation6 == thumbLocation) {
                                                    if (i == 0) {
                                                        str3 = null;
                                                        imageLocation92 = null;
                                                        str212 = null;
                                                    } else {
                                                        imageLocation92 = imageLocation8;
                                                        str2 = null;
                                                        str222 = null;
                                                        imageLocation3 = null;
                                                    }
                                                    i++;
                                                    newGuid = i2;
                                                    z4 = z6;
                                                    thumbFilter2 = str18;
                                                    imageFilter2 = str19;
                                                    mediaFilter2 = str20;
                                                }
                                                imageLocation92 = imageLocation8;
                                                i++;
                                                newGuid = i2;
                                                z4 = z6;
                                                thumbFilter2 = str18;
                                                imageFilter2 = str19;
                                                mediaFilter2 = str20;
                                            }
                                        }
                                        str18 = thumbFilter2;
                                        str19 = imageFilter2;
                                        str20 = mediaFilter2;
                                        imageLocation92 = imageLocation8;
                                        i++;
                                        newGuid = i2;
                                        z4 = z6;
                                        thumbFilter2 = str18;
                                        imageFilter2 = str19;
                                        mediaFilter2 = str20;
                                    }
                                    boolean z82 = z4;
                                    str4 = thumbFilter2;
                                    imageLocation4 = imageLocation92;
                                    str5 = imageFilter2;
                                    str6 = mediaFilter2;
                                    int i32 = newGuid;
                                    if (thumbLocation != null) {
                                        ImageLocation strippedLocation = imageReceiver.getStrippedLocation();
                                        if (strippedLocation == null) {
                                            if (imageLocation3 != null) {
                                                imageLocation102 = imageLocation3;
                                            }
                                            strippedLocation = imageLocation102;
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
                                        str8 = key4;
                                        str7 = key3;
                                    } else {
                                        str7 = null;
                                        str8 = null;
                                    }
                                    if (str2 != null || str6 == null) {
                                        str9 = str6;
                                    } else {
                                        StringBuilder sb = new StringBuilder();
                                        sb.append(str2);
                                        sb.append("@");
                                        str9 = str6;
                                        sb.append(str9);
                                        str2 = sb.toString();
                                    }
                                    if (str3 != null || str5 == null) {
                                        str10 = str5;
                                    } else {
                                        StringBuilder sb2 = new StringBuilder();
                                        sb2.append(str3);
                                        sb2.append("@");
                                        str10 = str5;
                                        sb2.append(str10);
                                        str3 = sb2.toString();
                                    }
                                    if (str7 != null || str4 == null) {
                                        str11 = str4;
                                        str12 = str7;
                                    } else {
                                        StringBuilder sb3 = new StringBuilder();
                                        sb3.append(str7);
                                        sb3.append("@");
                                        str11 = str4;
                                        sb3.append(str11);
                                        str12 = sb3.toString();
                                    }
                                    if (imageReceiver.getUniqKeyPrefix() != null || str3 == null) {
                                        str13 = str3;
                                    } else {
                                        str13 = imageReceiver.getUniqKeyPrefix() + str3;
                                    }
                                    if (imageReceiver.getUniqKeyPrefix() != null || str2 == null) {
                                        str14 = str2;
                                    } else {
                                        str14 = imageReceiver.getUniqKeyPrefix() + str2;
                                    }
                                    if (imageLocation4 == null) {
                                        str15 = str10;
                                        imageLocation5 = imageLocation4;
                                    } else if (imageLocation4.path != null) {
                                        createLoadOperationForImageReceiver(imageReceiver, str12, str8, ext, thumbLocation, str11, 0L, 1, 1, z82 ? 2 : 1, i32);
                                        createLoadOperationForImageReceiver(imageReceiver, str13, str212, str24, imageLocation4, str10, imageReceiver.getSize(), 1, 0, 0, i32);
                                        return;
                                    } else {
                                        imageLocation5 = imageLocation4;
                                        str15 = str10;
                                    }
                                    if (imageLocation3 != null) {
                                        int cacheType = imageReceiver.getCacheType();
                                        int i4 = (cacheType == 0 && z72) ? 1 : cacheType;
                                        int i5 = i4 == 0 ? 1 : i4;
                                        if (z82) {
                                            str16 = str9;
                                        } else {
                                            str16 = str9;
                                            createLoadOperationForImageReceiver(imageReceiver, str12, str8, ext, thumbLocation, str11, 0L, i5, 1, 1, i32);
                                        }
                                        if (!z3) {
                                            createLoadOperationForImageReceiver(imageReceiver, str13, str212, str24, imageLocation5, str15, 0L, 1, 0, 0, i32);
                                        }
                                        if (z2) {
                                            return;
                                        }
                                        createLoadOperationForImageReceiver(imageReceiver, str14, str222, str, imageLocation3, str16, imageReceiver.getSize(), i4, 3, 0, i32);
                                        return;
                                    }
                                    int cacheType2 = imageReceiver.getCacheType();
                                    int i6 = (cacheType2 == 0 && z72) ? 1 : cacheType2;
                                    createLoadOperationForImageReceiver(imageReceiver, str12, str8, ext, thumbLocation, str11, 0L, i6 == 0 ? 1 : i6, 1, z82 ? 2 : 1, i32);
                                    createLoadOperationForImageReceiver(imageReceiver, str13, str212, str24, imageLocation5, str15, imageReceiver.getSize(), i6, 0, 0, i32);
                                    return;
                                }
                                imageLocation2 = imageLocation;
                                z5 = false;
                                str = "mp4";
                                if (imageLocation2 == null) {
                                }
                                if (mediaLocation == null) {
                                }
                                ext = imageReceiver.getExt();
                                if (ext == null) {
                                }
                                if (str23 == null) {
                                }
                                if (str == null) {
                                }
                                ImageLocation imageLocation922 = imageLocation2;
                                imageLocation3 = mediaLocation;
                                ImageLocation imageLocation1022 = imageLocation;
                                i = 0;
                                str2 = null;
                                str3 = null;
                                String str2122 = null;
                                String str2222 = null;
                                boolean z722 = false;
                                while (i < 2) {
                                }
                                boolean z822 = z4;
                                str4 = thumbFilter2;
                                imageLocation4 = imageLocation922;
                                str5 = imageFilter2;
                                str6 = mediaFilter2;
                                int i322 = newGuid;
                                if (thumbLocation != null) {
                                }
                                if (str2 != null) {
                                }
                                str9 = str6;
                                if (str3 != null) {
                                }
                                str10 = str5;
                                if (str7 != null) {
                                }
                                str11 = str4;
                                str12 = str7;
                                if (imageReceiver.getUniqKeyPrefix() != null) {
                                }
                                str13 = str3;
                                if (imageReceiver.getUniqKeyPrefix() != null) {
                                }
                                str14 = str2;
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
                            if (imageLocation2 == null) {
                            }
                            if (mediaLocation == null) {
                            }
                            ext = imageReceiver.getExt();
                            if (ext == null) {
                            }
                            if (str23 == null) {
                            }
                            if (str == null) {
                            }
                            ImageLocation imageLocation9222 = imageLocation2;
                            imageLocation3 = mediaLocation;
                            ImageLocation imageLocation10222 = imageLocation;
                            i = 0;
                            str2 = null;
                            str3 = null;
                            String str21222 = null;
                            String str22222 = null;
                            boolean z7222 = false;
                            while (i < 2) {
                            }
                            boolean z8222 = z4;
                            str4 = thumbFilter22;
                            imageLocation4 = imageLocation9222;
                            str5 = imageFilter22;
                            str6 = mediaFilter22;
                            int i3222 = newGuid;
                            if (thumbLocation != null) {
                            }
                            if (str2 != null) {
                            }
                            str9 = str6;
                            if (str3 != null) {
                            }
                            str10 = str5;
                            if (str7 != null) {
                            }
                            str11 = str4;
                            str12 = str7;
                            if (imageReceiver.getUniqKeyPrefix() != null) {
                            }
                            str13 = str3;
                            if (imageReceiver.getUniqKeyPrefix() != null) {
                            }
                            str14 = str2;
                            if (imageLocation4 == null) {
                            }
                            if (imageLocation3 != null) {
                            }
                        }
                        imageLocation2 = imageLocation;
                        z5 = false;
                        str = "mp4";
                        if (imageLocation2 == null) {
                        }
                        if (mediaLocation == null) {
                        }
                        ext = imageReceiver.getExt();
                        if (ext == null) {
                        }
                        if (str23 == null) {
                        }
                        if (str == null) {
                        }
                        ImageLocation imageLocation92222 = imageLocation2;
                        imageLocation3 = mediaLocation;
                        ImageLocation imageLocation102222 = imageLocation;
                        i = 0;
                        str2 = null;
                        str3 = null;
                        String str212222 = null;
                        String str222222 = null;
                        boolean z72222 = false;
                        while (i < 2) {
                        }
                        boolean z82222 = z4;
                        str4 = thumbFilter22;
                        imageLocation4 = imageLocation92222;
                        str5 = imageFilter22;
                        str6 = mediaFilter22;
                        int i32222 = newGuid;
                        if (thumbLocation != null) {
                        }
                        if (str2 != null) {
                        }
                        str9 = str6;
                        if (str3 != null) {
                        }
                        str10 = str5;
                        if (str7 != null) {
                        }
                        str11 = str4;
                        str12 = str7;
                        if (imageReceiver.getUniqKeyPrefix() != null) {
                        }
                        str13 = str3;
                        if (imageReceiver.getUniqKeyPrefix() != null) {
                        }
                        str14 = str2;
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
                if (imageLocation2 == null) {
                }
                if (mediaLocation == null) {
                }
                ext = imageReceiver.getExt();
                if (ext == null) {
                }
                if (str23 == null) {
                }
                if (str == null) {
                }
                ImageLocation imageLocation922222 = imageLocation2;
                imageLocation3 = mediaLocation;
                ImageLocation imageLocation1022222 = imageLocation;
                i = 0;
                str2 = null;
                str3 = null;
                String str2122222 = null;
                String str2222222 = null;
                boolean z722222 = false;
                while (i < 2) {
                }
                boolean z822222 = z4;
                str4 = thumbFilter222;
                imageLocation4 = imageLocation922222;
                str5 = imageFilter222;
                str6 = mediaFilter222;
                int i322222 = newGuid;
                if (thumbLocation != null) {
                }
                if (str2 != null) {
                }
                str9 = str6;
                if (str3 != null) {
                }
                str10 = str5;
                if (str7 != null) {
                }
                str11 = str4;
                str12 = str7;
                if (imageReceiver.getUniqKeyPrefix() != null) {
                }
                str13 = str3;
                if (imageReceiver.getUniqKeyPrefix() != null) {
                }
                str14 = str2;
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
        if (imageLocation2 == null) {
        }
        if (mediaLocation == null) {
        }
        ext = imageReceiver.getExt();
        if (ext == null) {
        }
        if (str23 == null) {
        }
        if (str == null) {
        }
        ImageLocation imageLocation9222222 = imageLocation2;
        imageLocation3 = mediaLocation;
        ImageLocation imageLocation10222222 = imageLocation;
        i = 0;
        str2 = null;
        str3 = null;
        String str21222222 = null;
        String str22222222 = null;
        boolean z7222222 = false;
        while (i < 2) {
        }
        boolean z8222222 = z4;
        str4 = thumbFilter2222;
        imageLocation4 = imageLocation9222222;
        str5 = imageFilter2222;
        str6 = mediaFilter2222;
        int i3222222 = newGuid;
        if (thumbLocation != null) {
        }
        if (str2 != null) {
        }
        str9 = str6;
        if (str3 != null) {
        }
        str10 = str5;
        if (str7 != null) {
        }
        str11 = str4;
        str12 = str7;
        if (imageReceiver.getUniqKeyPrefix() != null) {
        }
        str13 = str3;
        if (imageReceiver.getUniqKeyPrefix() != null) {
        }
        str14 = str2;
        if (imageLocation4 == null) {
        }
        if (imageLocation3 != null) {
        }
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
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.lambda$httpFileLoadError$8(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$httpFileLoadError$8(String str) {
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
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.lambda$artworkLoadError$9(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$artworkLoadError$9(String str) {
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
                ImageLoader.this.lambda$fileDidLoaded$10(str, i, file);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fileDidLoaded$10(String str, int i, File file) {
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
                cacheImage2.key = str2;
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
                ImageLoader.this.lambda$fileDidFailedLoad$11(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fileDidFailedLoad$11(String str) {
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
                ImageLoader.this.lambda$runHttpFileLoadTasks$13(httpFileTask, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runHttpFileLoadTasks$13(HttpFileTask httpFileTask, int i) {
        if (httpFileTask != null) {
            this.currentHttpFileLoadTasksCount--;
        }
        if (httpFileTask != null) {
            if (i == 1) {
                if (!httpFileTask.canRetry) {
                    this.httpFileLoadTasksByKeys.remove(httpFileTask.url);
                    NotificationCenter.getInstance(httpFileTask.currentAccount).postNotificationName(NotificationCenter.httpFileDidFailedLoad, httpFileTask.url, 0);
                } else {
                    final HttpFileTask httpFileTask2 = new HttpFileTask(httpFileTask.url, httpFileTask.tempFile, httpFileTask.ext, httpFileTask.currentAccount);
                    Runnable runnable = new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda10
                        @Override // java.lang.Runnable
                        public final void run() {
                            ImageLoader.this.lambda$runHttpFileLoadTasks$12(httpFileTask2);
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
                NotificationCenter.getInstance(httpFileTask.currentAccount).postNotificationName(NotificationCenter.httpFileDidLoad, httpFileTask.url, file.toString());
            }
        }
        while (this.currentHttpFileLoadTasksCount < 2 && !this.httpFileLoadTasks.isEmpty()) {
            this.httpFileLoadTasks.poll().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
            this.currentHttpFileLoadTasksCount++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runHttpFileLoadTasks$12(HttpFileTask httpFileTask) {
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

    /* JADX WARN: Can't wrap try/catch for region: R(23:1|(2:6|(1:8)(2:9|(2:13|14)))|18|(1:20)(1:(19:155|156|22|(1:24)(1:153)|25|(1:27)|28|(3:30|(2:31|(1:33)(1:34))|35)|36|(1:38)(1:152)|39|(2:149|150)(1:(4:142|143|144|(1:146)))|42|43|(2:45|(6:47|(3:130|131|132)|49|50|(2:(1:53)|54)|(3:56|57|(4:59|(1:61)|62|(3:64|65|66)(1:69))(1:70))(2:93|(7:95|96|(5:103|104|(1:106)|107|(5:109|110|99|100|101))|98|99|100|101)(1:127)))(3:135|136|137))(3:138|139|140)|133|50|(0)|(0)(0)))|21|22|(0)(0)|25|(0)|28|(0)|36|(0)(0)|39|(0)(0)|42|43|(0)(0)|133|50|(0)|(0)(0)|(1:(0))) */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:144:0x011b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:148:0x00a7 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:155:0x017c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0072  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0077  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0081  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x008d  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x009e  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00a0  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00b1  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x00db  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x00fa A[Catch: all -> 0x00e2, TRY_ENTER, TRY_LEAVE, TryCatch #2 {all -> 0x00e2, blocks: (B:48:0x00a7, B:70:0x00e4, B:74:0x00ef, B:78:0x00fa), top: B:148:0x00a7 }] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x010e  */
    /* JADX WARN: Type inference failed for: r15v10 */
    /* JADX WARN: Type inference failed for: r15v11 */
    /* JADX WARN: Type inference failed for: r15v18 */
    /* JADX WARN: Type inference failed for: r15v2 */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:130:0x01ac -> B:154:0x01c2). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Bitmap loadBitmap(String str, Uri uri, float f, float f2, boolean z) {
        InputStream openInputStream;
        float max;
        int i;
        int i2;
        float f3;
        Bitmap decodeStream;
        Bitmap createBitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        if (str == null && uri != null && uri.getScheme() != null) {
            if (uri.getScheme().contains("file")) {
                str = uri.getPath();
            } else if (Build.VERSION.SDK_INT < 30 || !"content".equals(uri.getScheme())) {
                try {
                    str = AndroidUtilities.getPath(uri);
                } catch (Throwable th) {
                    FileLog.e(th);
                }
            }
        }
        Bitmap bitmap = null;
        if (str != null) {
            BitmapFactory.decodeFile(str, options);
        } else if (uri != null) {
            try {
                InputStream openInputStream2 = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                BitmapFactory.decodeStream(openInputStream2, null, options);
                openInputStream2.close();
                openInputStream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                float f4 = options.outWidth / f;
                float f5 = options.outHeight / f2;
                max = !z ? Math.max(f4, f5) : Math.min(f4, f5);
                if (max < 1.0f) {
                    max = 1.0f;
                }
                i = 0;
                i = 0;
                options.inJustDecodeBounds = false;
                i2 = (int) max;
                options.inSampleSize = i2;
                if (i2 % 2 != 0) {
                    int i3 = 1;
                    while (true) {
                        int i4 = i3 * 2;
                        if (i4 >= options.inSampleSize) {
                            break;
                        }
                        i3 = i4;
                    }
                    options.inSampleSize = i3;
                }
                options.inPurgeable = Build.VERSION.SDK_INT >= 21;
                if (str == null) {
                    try {
                        i = new ExifInterface(str).getAttributeInt("Orientation", 1);
                    } catch (Throwable unused) {
                    }
                } else if (uri != null) {
                    try {
                        InputStream openInputStream3 = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                        i = new ExifInterface(openInputStream3).getAttributeInt("Orientation", 1);
                        if (openInputStream3 != null) {
                            openInputStream3.close();
                        }
                    } catch (Throwable unused2) {
                    }
                }
                if (i != 3) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(180.0f);
                    i = matrix;
                } else if (i == 6) {
                    Matrix matrix2 = new Matrix();
                    matrix2.postRotate(90.0f);
                    i = matrix2;
                } else {
                    if (i == 8) {
                        Matrix matrix3 = new Matrix();
                        matrix3.postRotate(270.0f);
                        i = matrix3;
                    }
                    i = null;
                    f3 = max / options.inSampleSize;
                    if (f3 > 1.0f) {
                        if (i == null) {
                            i = new Matrix();
                        }
                        float f6 = 1.0f / f3;
                        i.postScale(f6, f6);
                    }
                    if (str != null) {
                        try {
                            bitmap = BitmapFactory.decodeFile(str, options);
                            if (bitmap != null) {
                                if (options.inPurgeable) {
                                    Utilities.pinBitmap(bitmap);
                                }
                                Bitmap createBitmap2 = Bitmaps.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), i, true);
                                if (createBitmap2 != bitmap) {
                                    bitmap.recycle();
                                    return createBitmap2;
                                }
                                return bitmap;
                            }
                            return bitmap;
                        } catch (Throwable th2) {
                            FileLog.e(th2);
                            getInstance().clearMemory();
                            if (bitmap == null) {
                                try {
                                    bitmap = BitmapFactory.decodeFile(str, options);
                                    if (bitmap != null && options.inPurgeable) {
                                        Utilities.pinBitmap(bitmap);
                                    }
                                } catch (Throwable th3) {
                                    FileLog.e(th3);
                                    return bitmap;
                                }
                            }
                            if (bitmap != null) {
                                Bitmap createBitmap3 = Bitmaps.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), i, true);
                                if (createBitmap3 != bitmap) {
                                    bitmap.recycle();
                                    return createBitmap3;
                                }
                                return bitmap;
                            }
                            return bitmap;
                        }
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
                                createBitmap = Bitmaps.createBitmap(decodeStream, 0, 0, decodeStream.getWidth(), decodeStream.getHeight(), i, true);
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
                            if (createBitmap != decodeStream) {
                                decodeStream.recycle();
                                bitmap = createBitmap;
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
                f3 = max / options.inSampleSize;
                if (f3 > 1.0f) {
                }
                if (str != null) {
                }
            } catch (Throwable th9) {
                FileLog.e(th9);
                return null;
            }
        }
        openInputStream = null;
        float f42 = options.outWidth / f;
        float f52 = options.outHeight / f2;
        if (!z) {
        }
        if (max < 1.0f) {
        }
        i = 0;
        i = 0;
        options.inJustDecodeBounds = false;
        i2 = (int) max;
        options.inSampleSize = i2;
        if (i2 % 2 != 0) {
        }
        options.inPurgeable = Build.VERSION.SDK_INT >= 21;
        if (str == null) {
        }
        if (i != 3) {
        }
        f3 = max / options.inSampleSize;
        if (f3 > 1.0f) {
        }
        if (str != null) {
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

    /* JADX WARN: Removed duplicated region for block: B:33:0x009e  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00a3  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00c1  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00d1  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00e7  */
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
                String str = tLRPC$TL_fileLocationToBeDeprecated.volume_id + "_" + tLRPC$TL_fileLocationToBeDeprecated.local_id + ".jpg";
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
        int i4 = tLRPC$PhotoSize.w;
        if (i4 <= 100 && height <= 100) {
            tLRPC$PhotoSize.type = "s";
        } else if (i4 <= 320 && height <= 320) {
            tLRPC$PhotoSize.type = "m";
        } else if (i4 <= 800 && height <= 800) {
            tLRPC$PhotoSize.type = "x";
        } else if (i4 <= 1280 && height <= 1280) {
            tLRPC$PhotoSize.type = "y";
        } else {
            tLRPC$PhotoSize.type = "w";
        }
        String str2 = tLRPC$TL_fileLocationToBeDeprecated.volume_id + "_" + tLRPC$TL_fileLocationToBeDeprecated.local_id + ".jpg";
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
        boolean z = true;
        File pathToAttach = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(findPhotoCachedSize, true);
        int i = 0;
        if (MessageObject.shouldEncryptPhotoOrVideo(tLRPC$Message)) {
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
        TLRPC$TL_photoSize_layer127 tLRPC$TL_photoSize_layer127 = new TLRPC$TL_photoSize_layer127();
        tLRPC$TL_photoSize_layer127.w = findPhotoCachedSize.w;
        tLRPC$TL_photoSize_layer127.h = findPhotoCachedSize.h;
        tLRPC$TL_photoSize_layer127.location = findPhotoCachedSize.location;
        tLRPC$TL_photoSize_layer127.size = findPhotoCachedSize.size;
        tLRPC$TL_photoSize_layer127.type = findPhotoCachedSize.type;
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
            int size2 = tLRPC$MessageMedia.document.thumbs.size();
            while (i < size2) {
                tLRPC$PhotoSize = tLRPC$Message.media.document.thumbs.get(i);
                if (!(tLRPC$PhotoSize instanceof TLRPC$TL_photoCachedSize)) {
                    i++;
                }
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

    /* loaded from: classes.dex */
    public static class MessageThumb {
        BitmapDrawable drawable;
        String key;

        public MessageThumb(String str, BitmapDrawable bitmapDrawable) {
            this.key = str;
            this.drawable = bitmapDrawable;
        }
    }
}
