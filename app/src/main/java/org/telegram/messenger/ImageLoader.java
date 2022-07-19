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
import android.graphics.Rect;
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
import com.huawei.hms.adapter.internal.AvailableCode;
import com.huawei.hms.opendevice.i;
import com.huawei.hms.push.constant.RemoteMessageConst;
import j$.util.concurrent.ConcurrentHashMap;
import j$.util.function.Consumer;
import j$.util.stream.Stream;
import j$.wrappers.C$r8$wrapper$java$util$stream$Stream$VWRP;
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
import java.util.zip.GZIPInputStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.messenger.DocumentObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.secretmedia.EncryptedFileInputStream;
import org.telegram.messenger.utils.BitmapsCache;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$InputEncryptedFile;
import org.telegram.tgnet.TLRPC$InputFile;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_document;
import org.telegram.tgnet.TLRPC$TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC$TL_documentEncrypted;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_fileLocationToBeDeprecated;
import org.telegram.tgnet.TLRPC$TL_fileLocationUnavailable;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument;
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
    private DispatchQueue cacheOutQueue = new DispatchQueue("cacheOutQueue");
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
        if (this.memCache.get(str) != null) {
            this.memCache.moveToFront(str);
        }
        if (this.smallImagesMemCache.get(str) == null) {
            return;
        }
        this.smallImagesMemCache.moveToFront(str);
    }

    public void putThumbsToCache(ArrayList<MessageThumb> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            putImageToCache(arrayList.get(i).drawable, arrayList.get(i).key, true);
        }
    }

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
            ImageLoader.this = r1;
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

        public /* synthetic */ void lambda$reportProgress$1(final long j, final long j2) {
            ImageLoader.this.fileProgresses.put(this.url, new long[]{j, j2});
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpFileTask$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpFileTask.this.lambda$reportProgress$0(j, j2);
                }
            });
        }

        public /* synthetic */ void lambda$reportProgress$0(long j, long j2) {
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.fileLoadProgressChanged, this.url, Long.valueOf(j), Long.valueOf(j2));
        }

        /* JADX WARN: Code restructure failed: missing block: B:76:0x0120, code lost:
            if (r5 != (-1)) goto L81;
         */
        /* JADX WARN: Code restructure failed: missing block: B:77:0x0122, code lost:
            r0 = r11.fileSize;
         */
        /* JADX WARN: Code restructure failed: missing block: B:78:0x0124, code lost:
            if (r0 == 0) goto L89;
         */
        /* JADX WARN: Code restructure failed: missing block: B:79:0x0126, code lost:
            reportProgress(r0, r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:80:0x012c, code lost:
            r0 = e;
         */
        /* JADX WARN: Code restructure failed: missing block: B:81:0x012e, code lost:
            r1 = false;
         */
        /* JADX WARN: Code restructure failed: missing block: B:84:0x0132, code lost:
            org.telegram.messenger.FileLog.e(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:85:0x0136, code lost:
            r0 = th;
         */
        /* JADX WARN: Code restructure failed: missing block: B:88:0x013a, code lost:
            org.telegram.messenger.FileLog.e(r0);
         */
        /* JADX WARN: Removed duplicated region for block: B:103:0x014e A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:114:0x00ad A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:92:0x0142 A[Catch: all -> 0x0148, TRY_LEAVE, TryCatch #5 {all -> 0x0148, blocks: (B:90:0x013e, B:92:0x0142), top: B:110:0x013e }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public Boolean doInBackground(Void... voidArr) {
            InputStream inputStream;
            HttpURLConnection httpURLConnection;
            List<String> list;
            String str;
            int responseCode;
            Throwable th;
            InputStream inputStream2;
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
                    inputStream = httpURLConnection.getInputStream();
                    try {
                        this.fileOutputStream = new RandomAccessFile(this.tempFile, "rws");
                    } catch (Throwable th2) {
                        inputStream2 = inputStream;
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
                        }
                        FileLog.e(th);
                        inputStream = inputStream2;
                        if (this.canRetry) {
                        }
                        return Boolean.valueOf(z2);
                    }
                } catch (Throwable th3) {
                    th = th3;
                    inputStream2 = null;
                }
            } catch (Throwable th4) {
                th = th4;
                inputStream2 = null;
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
                if (inputStream != null) {
                    try {
                        byte[] bArr = new byte[32768];
                        int i = 0;
                        while (true) {
                            if (isCancelled()) {
                                break;
                            }
                            try {
                                int read = inputStream.read(bArr);
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
                                Exception e4 = e3;
                                z = false;
                            }
                        }
                    } catch (Throwable th5) {
                        Throwable th6 = th5;
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
                } catch (Throwable th7) {
                    FileLog.e(th7);
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable th8) {
                        FileLog.e(th8);
                    }
                }
            }
            return Boolean.valueOf(z2);
        }

        public void onPostExecute(Boolean bool) {
            ImageLoader.this.runHttpFileLoadTasks(this, bool.booleanValue() ? 2 : 1);
        }

        @Override // android.os.AsyncTask
        protected void onCancelled() {
            ImageLoader.this.runHttpFileLoadTasks(this, 2);
        }
    }

    /* loaded from: classes.dex */
    public class ArtworkLoadTask extends AsyncTask<Void, Void, String> {
        private CacheImage cacheImage;
        private boolean canRetry = true;
        private HttpURLConnection httpConnection;
        private boolean small;

        public ArtworkLoadTask(CacheImage cacheImage) {
            ImageLoader.this = r2;
            boolean z = true;
            this.cacheImage = cacheImage;
            this.small = Uri.parse(cacheImage.imageLocation.path).getQueryParameter("s") == null ? false : z;
        }

        public String doInBackground(Void... voidArr) {
            ByteArrayOutputStream byteArrayOutputStream;
            InputStream inputStream;
            Throwable th;
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
                            byte[] bArr = new byte[32768];
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
                                    } catch (Throwable th2) {
                                        FileLog.e(th2);
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
                                    } catch (Throwable th3) {
                                        FileLog.e(th3);
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
                                } catch (Throwable th4) {
                                    FileLog.e(th4);
                                }
                            }
                            try {
                                byteArrayOutputStream2.close();
                            } catch (Exception unused5) {
                            }
                            return replace;
                        } catch (Throwable th5) {
                            inputStream = inputStream2;
                            th = th5;
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
                                    } catch (Throwable th6) {
                                        FileLog.e(th6);
                                    }
                                }
                                if (byteArrayOutputStream != null) {
                                    byteArrayOutputStream.close();
                                }
                                return null;
                            } catch (Throwable th7) {
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
                                    } catch (Throwable th8) {
                                        FileLog.e(th8);
                                    }
                                }
                                if (byteArrayOutputStream != null) {
                                    try {
                                        byteArrayOutputStream.close();
                                    } catch (Exception unused8) {
                                    }
                                }
                                throw th7;
                            }
                        }
                    } catch (Throwable th9) {
                        byteArrayOutputStream = null;
                        inputStream = inputStream2;
                        th = th9;
                    }
                } catch (Throwable th10) {
                    th = th10;
                    inputStream = null;
                    byteArrayOutputStream = null;
                }
            } catch (Exception unused9) {
            }
        }

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

        public /* synthetic */ void lambda$onPostExecute$0(String str) {
            CacheImage cacheImage = this.cacheImage;
            cacheImage.httpTask = new HttpImageTask(cacheImage, 0, str);
            ImageLoader.this.httpTasks.add(this.cacheImage.httpTask);
            ImageLoader.this.runHttpTasks(false);
        }

        public /* synthetic */ void lambda$onPostExecute$1() {
            ImageLoader.this.runArtworkTasks(true);
        }

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

    /* loaded from: classes.dex */
    public class HttpImageTask extends AsyncTask<Void, Void, Boolean> {
        private CacheImage cacheImage;
        private boolean canRetry = true;
        private RandomAccessFile fileOutputStream;
        private HttpURLConnection httpConnection;
        private long imageSize;
        private long lastProgressTime;
        private String overrideUrl;

        public static /* synthetic */ void lambda$doInBackground$2(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        }

        public HttpImageTask(CacheImage cacheImage, long j) {
            ImageLoader.this = r1;
            this.cacheImage = cacheImage;
            this.imageSize = j;
        }

        public HttpImageTask(CacheImage cacheImage, int i, String str) {
            ImageLoader.this = r1;
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

        public /* synthetic */ void lambda$reportProgress$1(final long j, final long j2) {
            ImageLoader.this.fileProgresses.put(this.cacheImage.url, new long[]{j, j2});
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpImageTask.this.lambda$reportProgress$0(j, j2);
                }
            });
        }

        public /* synthetic */ void lambda$reportProgress$0(long j, long j2) {
            NotificationCenter.getInstance(this.cacheImage.currentAccount).postNotificationName(NotificationCenter.fileLoadProgressChanged, this.cacheImage.url, Long.valueOf(j), Long.valueOf(j2));
        }

        /* JADX WARN: Can't wrap try/catch for region: R(16:2|(7:135|4|(1:14)|15|(1:17)|18|(15:20|127|21|50|(6:141|52|(1:60)|63|(3:131|67|(1:75))|(6:79|137|80|(2:81|(1:147)(3:129|83|(3:85|(3:145|87|150)(1:149)|148)(1:146)))|97|101))|133|104|(1:106)|125|109|(1:111)|(2:139|114)|(1:122)|123|124))|49|50|(0)|133|104|(0)|125|109|(0)|(0)|(3:118|120|122)|123|124|(1:(0))) */
        /* JADX WARN: Code restructure failed: missing block: B:100:0x0183, code lost:
            r1 = r2;
         */
        /* JADX WARN: Code restructure failed: missing block: B:103:0x0187, code lost:
            org.telegram.messenger.FileLog.e(r1);
         */
        /* JADX WARN: Code restructure failed: missing block: B:107:0x0194, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:108:0x0195, code lost:
            org.telegram.messenger.FileLog.e(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:89:0x0169, code lost:
            if (r7 != (-1)) goto L97;
         */
        /* JADX WARN: Code restructure failed: missing block: B:90:0x016b, code lost:
            r2 = r12.imageSize;
         */
        /* JADX WARN: Code restructure failed: missing block: B:91:0x016f, code lost:
            if (r2 == 0) goto L101;
         */
        /* JADX WARN: Code restructure failed: missing block: B:92:0x0171, code lost:
            reportProgress(r2, r2);
         */
        /* JADX WARN: Code restructure failed: missing block: B:93:0x0175, code lost:
            r2 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:94:0x0176, code lost:
            r1 = r2;
            r2 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:95:0x0179, code lost:
            r2 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:96:0x017a, code lost:
            r1 = r2;
            r2 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:99:0x0180, code lost:
            org.telegram.messenger.FileLog.e(r1);
         */
        /* JADX WARN: Removed duplicated region for block: B:106:0x018e A[Catch: all -> 0x0194, TRY_LEAVE, TryCatch #4 {all -> 0x0194, blocks: (B:104:0x018a, B:106:0x018e), top: B:133:0x018a }] */
        /* JADX WARN: Removed duplicated region for block: B:111:0x019c A[Catch: all -> 0x01a0, TRY_LEAVE, TryCatch #0 {all -> 0x01a0, blocks: (B:109:0x0198, B:111:0x019c), top: B:125:0x0198 }] */
        /* JADX WARN: Removed duplicated region for block: B:118:0x01ad  */
        /* JADX WARN: Removed duplicated region for block: B:139:0x01a3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:141:0x00ee A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public Boolean doInBackground(Void... voidArr) {
            Throwable th;
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
                } catch (Throwable th2) {
                    th = th2;
                    inputStream = null;
                }
                if (!isCancelled()) {
                    this.httpConnection.connect();
                    inputStream2 = this.httpConnection.getInputStream();
                    try {
                        this.fileOutputStream = new RandomAccessFile(this.cacheImage.tempFilePath, "rws");
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
                                byte[] bArr = new byte[8192];
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
                                        Exception e4 = e3;
                                    }
                                }
                                z2 = false;
                                z3 = z2;
                            } catch (Throwable th4) {
                                Throwable th5 = th4;
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
                        } catch (Throwable th6) {
                            FileLog.e(th6);
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
            });
        }

        public /* synthetic */ void lambda$onPostExecute$4(final Boolean bool) {
            ImageLoader.this.fileProgresses.remove(this.cacheImage.url);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpImageTask.this.lambda$onPostExecute$3(bool);
                }
            });
        }

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

        public /* synthetic */ void lambda$onPostExecute$5() {
            ImageLoader.this.runHttpTasks(true);
        }

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
            });
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpImageTask.this.lambda$onCancelled$8();
                }
            });
        }

        public /* synthetic */ void lambda$onCancelled$8() {
            ImageLoader.this.fileProgresses.remove(this.cacheImage.url);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpImageTask.this.lambda$onCancelled$7();
                }
            });
        }

        public /* synthetic */ void lambda$onCancelled$7() {
            NotificationCenter.getInstance(this.cacheImage.currentAccount).postNotificationName(NotificationCenter.fileLoadFailed, this.cacheImage.url, 1);
        }
    }

    /* loaded from: classes.dex */
    public class ThumbGenerateTask implements Runnable {
        private ThumbGenerateInfo info;
        private int mediaType;
        private File originalPath;

        public ThumbGenerateTask(int i, File file, ThumbGenerateInfo thumbGenerateInfo) {
            ImageLoader.this = r1;
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

        public /* synthetic */ void lambda$removeTask$0(String str) {
            ImageLoader.this.thumbGenerateTasks.remove(str);
        }

        @Override // java.lang.Runnable
        public void run() {
            int i;
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
                        i = Math.max(point.x, point.y);
                    } else {
                        Point point2 = AndroidUtilities.displaySize;
                        i = Math.min(180, Math.min(point2.x, point2.y) / 4);
                    }
                    int i2 = this.mediaType;
                    Bitmap bitmap = null;
                    if (i2 == 0) {
                        float f = i;
                        bitmap = ImageLoader.loadBitmap(this.originalPath.toString(), null, f, f, false);
                    } else {
                        int i3 = 2;
                        if (i2 == 2) {
                            String file2 = this.originalPath.toString();
                            if (!this.info.big) {
                                i3 = 1;
                            }
                            bitmap = SendMessagesHelper.createVideoThumbnail(file2, i3);
                        } else if (i2 == 3) {
                            String lowerCase = this.originalPath.toString().toLowerCase();
                            if (lowerCase.endsWith("mp4")) {
                                String file3 = this.originalPath.toString();
                                if (!this.info.big) {
                                    i3 = 1;
                                }
                                bitmap = SendMessagesHelper.createVideoThumbnail(file3, i3);
                            } else if (lowerCase.endsWith(".jpg") || lowerCase.endsWith(".jpeg") || lowerCase.endsWith(".png") || lowerCase.endsWith(".gif")) {
                                float f2 = i;
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
                        float f4 = i;
                        float f5 = height;
                        float min = Math.min(f3 / f4, f5 / f4);
                        if (min > 1.0f && (createScaledBitmap = Bitmaps.createScaledBitmap(bitmap, (int) (f3 / min), (int) (f5 / min), true)) != bitmap) {
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
                    } catch (Throwable unused) {
                    }
                    throw th;
                }
            }
        } catch (Exception unused2) {
            return "";
        }
    }

    /* loaded from: classes.dex */
    public class CacheOutTask implements Runnable {
        private CacheImage cacheImage;
        private boolean isCancelled;
        private Thread runningThread;
        private final Object sync = new Object();

        public CacheOutTask(CacheImage cacheImage) {
            ImageLoader.this = r1;
            this.cacheImage = cacheImage;
        }

        /* JADX WARN: Can't wrap try/catch for region: R(10:774|(2:776|(8:778|806|780|(1:782)(1:783)|784|(1:788)(1:789)|790|791))|779|806|780|(0)(0)|784|(0)(0)|790|791) */
        /* JADX WARN: Can't wrap try/catch for region: R(23:(6:860|243|800|244|(1:246)(1:247)|248)|(2:250|(21:252|816|254|275|(14:277|(3:279|(1:281)(1:282)|283)(3:284|(3:286|(1:288)(1:289)|290)(2:291|(1:293))|294)|296|(1:298)|299|812|300|(12:302|(6:825|304|305|818|306|307)(1:313)|810|314|(1:316)(2:317|(1:319)(2:320|(1:322)(1:323)))|798|324|855|325|(1:327)|(1:406)(11:333|830|334|797|(2:352|(12:842|354|(1:359)(1:358)|(1:361)|362|363|364|365|(3:370|372|(1:374))|371|372|(0))(3:378|(1:380)(1:381)|382))(2:(7:827|338|339|814|340|341|342)(1:347)|348)|383|(1:388)(1:387)|389|(1:391)|392|(1:402)(4:398|(1:399)|867|401))|407)(3:415|(11:417|804|418|(1:420)(1:421)|422|796|423|(1:425)|426|(4:428|(1:429)|868|431)(1:432)|433)(1:437)|438)|439|447|(3:858|449|7a0)(6:569|(1:571)|(3:853|573|(5:575|576|(3:859|578|(1:580))(1:581)|585|9ec))|584|585|9ec)|756|(3:759|(1:761)(1:762)|763)|(3:(1:771)(1:772)|773|875)(3:(1:767)(1:768)|769|874))|295|296|(0)|299|812|300|(0)(0)|439|447|(0)(0)|756|(3:759|(0)(0)|763)|(0)|(0)(0)|773|875))|253|816|254|275|(0)|295|296|(0)|299|812|300|(0)(0)|439|447|(0)(0)|756|(0)|(0)|(0)(0)|773|875) */
        /* JADX WARN: Can't wrap try/catch for region: R(25:224|(1:232)(1:231)|233|(2:235|(1:239))(1:240)|241|(28:860|243|800|244|(1:246)(1:247)|248|(2:250|(21:252|816|254|275|(14:277|(3:279|(1:281)(1:282)|283)(3:284|(3:286|(1:288)(1:289)|290)(2:291|(1:293))|294)|296|(1:298)|299|812|300|(12:302|(6:825|304|305|818|306|307)(1:313)|810|314|(1:316)(2:317|(1:319)(2:320|(1:322)(1:323)))|798|324|855|325|(1:327)|(1:406)(11:333|830|334|797|(2:352|(12:842|354|(1:359)(1:358)|(1:361)|362|363|364|365|(3:370|372|(1:374))|371|372|(0))(3:378|(1:380)(1:381)|382))(2:(7:827|338|339|814|340|341|342)(1:347)|348)|383|(1:388)(1:387)|389|(1:391)|392|(1:402)(4:398|(1:399)|867|401))|407)(3:415|(11:417|804|418|(1:420)(1:421)|422|796|423|(1:425)|426|(4:428|(1:429)|868|431)(1:432)|433)(1:437)|438)|439|447|(3:858|449|7a0)(6:569|(1:571)|(3:853|573|(5:575|576|(3:859|578|(1:580))(1:581)|585|9ec))|584|585|9ec)|756|(3:759|(1:761)(1:762)|763)|(3:(1:771)(1:772)|773|875)(3:(1:767)(1:768)|769|874))|295|296|(0)|299|812|300|(0)(0)|439|447|(0)(0)|756|(3:759|(0)(0)|763)|(0)|(0)(0)|773|875))|253|816|254|275|(0)|295|296|(0)|299|812|300|(0)(0)|439|447|(0)(0)|756|(0)|(0)|(0)(0)|773|875)|274|275|(0)|295|296|(0)|299|812|300|(0)(0)|439|447|(0)(0)|756|(0)|(0)|(0)(0)|773|875) */
        /* JADX WARN: Code restructure failed: missing block: B:104:0x01ca, code lost:
            if (r0[1] == (-117)) goto L840;
         */
        /* JADX WARN: Code restructure failed: missing block: B:255:0x0464, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:256:0x0465, code lost:
            org.telegram.messenger.FileLog.e(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:440:0x0777, code lost:
            r0 = th;
         */
        /* JADX WARN: Code restructure failed: missing block: B:441:0x0778, code lost:
            r32 = r12;
            r33 = r13;
         */
        /* JADX WARN: Code restructure failed: missing block: B:785:0x0cee, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:786:0x0cef, code lost:
            org.telegram.messenger.FileLog.e(r0);
            r5 = null;
         */
        /* JADX WARN: Not initialized variable reg: 30, insn: 0x05eb: MOVE  (r8 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r30 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:350:0x05e7 */
        /* JADX WARN: Not initialized variable reg: 31, insn: 0x05ed: MOVE  (r5 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r31 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:350:0x05e7 */
        /* JADX WARN: Removed duplicated region for block: B:121:0x01f3  */
        /* JADX WARN: Removed duplicated region for block: B:123:0x01f6  */
        /* JADX WARN: Removed duplicated region for block: B:129:0x020e  */
        /* JADX WARN: Removed duplicated region for block: B:131:0x0212  */
        /* JADX WARN: Removed duplicated region for block: B:132:0x0230  */
        /* JADX WARN: Removed duplicated region for block: B:134:0x024b  */
        /* JADX WARN: Removed duplicated region for block: B:135:0x0250  */
        /* JADX WARN: Removed duplicated region for block: B:162:0x02a6  */
        /* JADX WARN: Removed duplicated region for block: B:208:0x0376  */
        /* JADX WARN: Removed duplicated region for block: B:214:0x038e  */
        /* JADX WARN: Removed duplicated region for block: B:277:0x049b  */
        /* JADX WARN: Removed duplicated region for block: B:298:0x050a  */
        /* JADX WARN: Removed duplicated region for block: B:302:0x051c A[Catch: all -> 0x0777, TRY_LEAVE, TryCatch #12 {all -> 0x0777, blocks: (B:300:0x0516, B:302:0x051c), top: B:812:0x0516 }] */
        /* JADX WARN: Removed duplicated region for block: B:374:0x064b A[Catch: all -> 0x06c3, TryCatch #19 {all -> 0x06c3, blocks: (B:365:0x0633, B:367:0x0639, B:372:0x0643, B:374:0x064b, B:380:0x0664, B:381:0x066e, B:382:0x0673, B:383:0x067a, B:387:0x0688, B:388:0x0691, B:392:0x06a2, B:399:0x06b2, B:401:0x06bc, B:402:0x06bf), top: B:797:0x05a1 }] */
        /* JADX WARN: Removed duplicated region for block: B:415:0x06fe  */
        /* JADX WARN: Removed duplicated region for block: B:569:0x09a2  */
        /* JADX WARN: Removed duplicated region for block: B:57:0x0104  */
        /* JADX WARN: Removed duplicated region for block: B:587:0x09ed A[Catch: all -> 0x0c5a, TRY_ENTER, TryCatch #42 {all -> 0x0c5d, blocks: (B:578:0x09c9, B:580:0x09d1, B:585:0x09e1, B:586:0x09ec, B:593:0x09f6, B:596:0x09fe, B:599:0x0a05, B:600:0x0a0a, B:601:0x0a0e, B:605:0x0a17, B:606:0x0a27, B:587:0x09ed, B:589:0x09f1, B:591:0x09f3), top: B:859:0x09c9 }] */
        /* JADX WARN: Removed duplicated region for block: B:670:0x0b4b  */
        /* JADX WARN: Removed duplicated region for block: B:677:0x0b63 A[Catch: all -> 0x0c58, TryCatch #35 {all -> 0x0c58, blocks: (B:642:0x0add, B:671:0x0b4d, B:673:0x0b57, B:675:0x0b5d, B:677:0x0b63, B:679:0x0b69, B:685:0x0b7f, B:691:0x0b8d, B:693:0x0b93, B:694:0x0b9d, B:696:0x0ba3, B:699:0x0bb0, B:702:0x0bb8, B:704:0x0bc6, B:706:0x0bd1, B:710:0x0bd8), top: B:852:0x0add }] */
        /* JADX WARN: Removed duplicated region for block: B:699:0x0bb0 A[Catch: all -> 0x0c58, TryCatch #35 {all -> 0x0c58, blocks: (B:642:0x0add, B:671:0x0b4d, B:673:0x0b57, B:675:0x0b5d, B:677:0x0b63, B:679:0x0b69, B:685:0x0b7f, B:691:0x0b8d, B:693:0x0b93, B:694:0x0b9d, B:696:0x0ba3, B:699:0x0bb0, B:702:0x0bb8, B:704:0x0bc6, B:706:0x0bd1, B:710:0x0bd8), top: B:852:0x0add }] */
        /* JADX WARN: Removed duplicated region for block: B:70:0x0130  */
        /* JADX WARN: Removed duplicated region for block: B:758:0x0c6d A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:761:0x0c7b  */
        /* JADX WARN: Removed duplicated region for block: B:762:0x0c7d  */
        /* JADX WARN: Removed duplicated region for block: B:765:0x0c92 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:771:0x0ca4  */
        /* JADX WARN: Removed duplicated region for block: B:772:0x0caa  */
        /* JADX WARN: Removed duplicated region for block: B:782:0x0ce6  */
        /* JADX WARN: Removed duplicated region for block: B:783:0x0ce8  */
        /* JADX WARN: Removed duplicated region for block: B:788:0x0cf5  */
        /* JADX WARN: Removed duplicated region for block: B:789:0x0cfc  */
        /* JADX WARN: Removed duplicated region for block: B:808:0x025c A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:829:0x0a3b A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:832:0x0486 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:846:0x0c4b A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:858:0x0795 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void run() {
            Drawable drawable;
            byte[] bArr;
            SecureDocumentKey secureDocumentKey;
            RandomAccessFile randomAccessFile;
            Throwable th;
            RandomAccessFile randomAccessFile2;
            Exception e;
            String lowerCase;
            boolean z;
            String str;
            boolean z2;
            Long l;
            String str2;
            boolean z3;
            boolean z4;
            boolean z5;
            float f;
            Bitmap bitmap;
            char c;
            int i;
            float f2;
            boolean z6;
            int i2;
            Throwable th2;
            Bitmap createScaledBitmap;
            int i3;
            boolean z7;
            FileInputStream fileInputStream;
            Bitmap bitmap2;
            boolean z8;
            boolean z9;
            int i4;
            boolean z10;
            FileInputStream fileInputStream2;
            Rect rect;
            int i5;
            int attributeInt;
            boolean z11;
            Bitmap bitmap3;
            Bitmap bitmap4;
            Throwable th3;
            float f3;
            String str3;
            float f4;
            float f5;
            Bitmap bitmap5;
            Throwable th4;
            Throwable th5;
            boolean z12;
            char c2;
            Throwable th6;
            char c3;
            boolean z13;
            float f6;
            boolean z14;
            FileInputStream fileInputStream3;
            Long l2;
            Long l3;
            String str4;
            boolean z15;
            boolean z16;
            AnimatedFileDrawable animatedFileDrawable;
            int i6;
            int i7;
            BitmapsCache.CacheOptions cacheOptions;
            BitmapsCache.CacheOptions cacheOptions2;
            int i8;
            int i9;
            boolean z17;
            boolean z18;
            String str5;
            boolean z19;
            int i10;
            RLottieDrawable rLottieDrawable;
            RandomAccessFile randomAccessFile3;
            Throwable th7;
            BitmapsCache.CacheOptions cacheOptions3;
            Exception e2;
            boolean z20;
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
                int i11 = cacheImage.imageType;
                if (i11 == 5) {
                    try {
                        CacheImage cacheImage2 = this.cacheImage;
                        drawable = new ThemePreviewDrawable(cacheImage2.finalFilePath, (DocumentObject.ThemeDocument) cacheImage2.imageLocation.document);
                    } catch (Throwable th8) {
                        FileLog.e(th8);
                        drawable = null;
                    }
                    onPostExecute(drawable);
                    return;
                }
                boolean z21 = true;
                boolean z22 = false;
                if (i11 == 3 || i11 == 4) {
                    Point point = AndroidUtilities.displaySize;
                    int i12 = point.x;
                    int i13 = point.y;
                    String str6 = cacheImage.filter;
                    if (str6 != null) {
                        String[] split = str6.split("_");
                        if (split.length >= 2) {
                            float parseFloat = Float.parseFloat(split[0]);
                            float parseFloat2 = Float.parseFloat(split[1]);
                            float f7 = AndroidUtilities.density;
                            i12 = (int) (parseFloat * f7);
                            i13 = (int) (parseFloat2 * f7);
                            CacheImage cacheImage3 = this.cacheImage;
                            Bitmap bitmap6 = SvgHelper.getBitmap(cacheImage3.finalFilePath, i12, i13, cacheImage3.imageType != 4);
                            onPostExecute(bitmap6 == null ? new BitmapDrawable(bitmap6) : null);
                        }
                    }
                    CacheImage cacheImage32 = this.cacheImage;
                    Bitmap bitmap62 = SvgHelper.getBitmap(cacheImage32.finalFilePath, i12, i13, cacheImage32.imageType != 4);
                    onPostExecute(bitmap62 == null ? new BitmapDrawable(bitmap62) : null);
                } else if (i11 == 1) {
                    int min = Math.min(512, AndroidUtilities.dp(170.6f));
                    int min2 = Math.min(512, AndroidUtilities.dp(170.6f));
                    String str7 = this.cacheImage.filter;
                    if (str7 != null) {
                        String[] split2 = str7.split("_");
                        if (split2.length >= 2) {
                            float parseFloat3 = Float.parseFloat(split2[0]);
                            float parseFloat4 = Float.parseFloat(split2[1]);
                            int min3 = Math.min(512, (int) (AndroidUtilities.density * parseFloat3));
                            int min4 = Math.min(512, (int) (parseFloat4 * AndroidUtilities.density));
                            if (parseFloat3 > 90.0f || parseFloat4 > 90.0f || this.cacheImage.filter.contains("nolimit")) {
                                min2 = min4;
                                min = min3;
                                z20 = false;
                            } else {
                                min = Math.min(min3, 160);
                                min2 = Math.min(min4, 160);
                                z20 = true;
                            }
                            z18 = (split2.length >= 3 && "pcache".equals(split2[2])) || (!this.cacheImage.filter.contains("nolimit") && SharedConfig.getDevicePerformanceClass() != 2);
                            if (this.cacheImage.filter.contains("lastframe")) {
                                z17 = true;
                                if (split2.length >= 3) {
                                    if ("nr".equals(split2[2])) {
                                        str5 = null;
                                    } else if (!"nrs".equals(split2[2])) {
                                        if ("dice".equals(split2[2])) {
                                            str5 = split2[3];
                                        }
                                    } else {
                                        str5 = null;
                                        i9 = 3;
                                        if (split2.length >= 5) {
                                            if ("c1".equals(split2[4])) {
                                                z19 = z20;
                                                i8 = 12;
                                            } else if ("c2".equals(split2[4])) {
                                                z19 = z20;
                                                i8 = 3;
                                            } else if ("c3".equals(split2[4])) {
                                                z19 = z20;
                                                i8 = 4;
                                            } else if ("c4".equals(split2[4])) {
                                                z19 = z20;
                                                i8 = 5;
                                            } else if ("c5".equals(split2[4])) {
                                                z19 = z20;
                                                i8 = 6;
                                            }
                                            i10 = min;
                                        }
                                        z19 = z20;
                                        i8 = 0;
                                        i10 = min;
                                    }
                                    i9 = 2;
                                    if (split2.length >= 5) {
                                    }
                                    z19 = z20;
                                    i8 = 0;
                                    i10 = min;
                                }
                                str5 = null;
                                i9 = 1;
                                if (split2.length >= 5) {
                                }
                                z19 = z20;
                                i8 = 0;
                                i10 = min;
                            }
                        } else {
                            z20 = false;
                            z18 = false;
                        }
                        z17 = false;
                        if (split2.length >= 3) {
                        }
                        str5 = null;
                        i9 = 1;
                        if (split2.length >= 5) {
                        }
                        z19 = z20;
                        i8 = 0;
                        i10 = min;
                    } else {
                        i10 = min;
                        z19 = false;
                        str5 = null;
                        z18 = false;
                        z17 = false;
                        i9 = 1;
                        i8 = 0;
                    }
                    int i14 = min2;
                    if (str5 != null) {
                        if ("🎰".equals(str5)) {
                            rLottieDrawable = new SlotsDrawable(str5, i10, i14);
                        } else {
                            rLottieDrawable = new RLottieDrawable(str5, i10, i14);
                        }
                    } else {
                        File file = this.cacheImage.finalFilePath;
                        try {
                            randomAccessFile3 = new RandomAccessFile(this.cacheImage.finalFilePath, "r");
                            try {
                                try {
                                    byte[] bArr2 = this.cacheImage.type == 1 ? ImageLoader.headerThumb : ImageLoader.header;
                                    randomAccessFile3.readFully(bArr2, 0, 2);
                                    if (bArr2[0] == 31) {
                                    }
                                    z21 = false;
                                    try {
                                        randomAccessFile3.close();
                                    } catch (Exception e3) {
                                        FileLog.e(e3);
                                    }
                                } catch (Exception e4) {
                                    e2 = e4;
                                    FileLog.e((Throwable) e2, false);
                                    if (randomAccessFile3 != null) {
                                        try {
                                            randomAccessFile3.close();
                                        } catch (Exception e5) {
                                            FileLog.e(e5);
                                        }
                                    }
                                    z21 = false;
                                    if (!z17) {
                                    }
                                    if (!z22) {
                                    }
                                    if (!z21) {
                                    }
                                    if (!z17) {
                                    }
                                }
                            } catch (Throwable th9) {
                                th7 = th9;
                                if (randomAccessFile3 != null) {
                                    try {
                                        randomAccessFile3.close();
                                    } catch (Exception e6) {
                                        FileLog.e(e6);
                                    }
                                }
                                throw th7;
                            }
                        } catch (Exception e7) {
                            e2 = e7;
                            randomAccessFile3 = null;
                        } catch (Throwable th10) {
                            th7 = th10;
                            randomAccessFile3 = null;
                            if (randomAccessFile3 != null) {
                            }
                            throw th7;
                        }
                        if (!z17) {
                            z22 = z18;
                        }
                        if (!z22) {
                            BitmapsCache.CacheOptions cacheOptions4 = new BitmapsCache.CacheOptions();
                            String str8 = this.cacheImage.filter;
                            if (str8 != null && str8.contains("compress")) {
                                cacheOptions4.compressQuality = 60;
                            }
                            cacheOptions3 = cacheOptions4;
                        } else {
                            cacheOptions3 = null;
                        }
                        if (!z21) {
                            File file2 = this.cacheImage.finalFilePath;
                            rLottieDrawable = new RLottieDrawable(file2, ImageLoader.decompressGzip(file2), i10, i14, cacheOptions3, z19, null, i8);
                        } else {
                            rLottieDrawable = new RLottieDrawable(this.cacheImage.finalFilePath, i10, i14, cacheOptions3, z19, null, i8);
                        }
                    }
                    if (!z17) {
                        loadLastFrame(rLottieDrawable, i14, i10);
                        return;
                    }
                    rLottieDrawable.setAutoRepeat(i9);
                    onPostExecute(rLottieDrawable);
                } else if (i11 == 2) {
                    long j = imageLocation != null ? imageLocation.videoSeekTo : 0L;
                    String str9 = cacheImage.filter;
                    if (str9 != null) {
                        String[] split3 = str9.split("_");
                        if (split3.length >= 2) {
                            float parseFloat5 = Float.parseFloat(split3[0]);
                            float parseFloat6 = Float.parseFloat(split3[1]);
                            if (parseFloat5 <= 90.0f && parseFloat6 <= 90.0f && !this.cacheImage.filter.contains("nolimit")) {
                                z16 = true;
                                z15 = false;
                                for (String str10 : split3) {
                                    if ("pcache".equals(str10)) {
                                        z15 = true;
                                    }
                                }
                            }
                        }
                        z16 = false;
                        z15 = false;
                        while (r3 < split3.length) {
                        }
                    } else {
                        z16 = false;
                        z15 = false;
                    }
                    if (ImageLoader.this.isAnimatedAvatar(this.cacheImage.filter) || ImageLoader.AUTOPLAY_FILTER.equals(this.cacheImage.filter)) {
                        CacheImage cacheImage4 = this.cacheImage;
                        ImageLocation imageLocation2 = cacheImage4.imageLocation;
                        TLRPC$Document tLRPC$Document = imageLocation2.document;
                        if (!(tLRPC$Document instanceof TLRPC$TL_documentEncrypted) && !z15) {
                            if (!(tLRPC$Document instanceof TLRPC$Document)) {
                                tLRPC$Document = null;
                            }
                            long j2 = tLRPC$Document != null ? cacheImage4.size : imageLocation2.currentSize;
                            if (z15) {
                                BitmapsCache.CacheOptions cacheOptions5 = new BitmapsCache.CacheOptions();
                                String str11 = this.cacheImage.filter;
                                if (str11 != null && str11.contains("compress")) {
                                    cacheOptions5.compressQuality = 60;
                                }
                                cacheOptions2 = cacheOptions5;
                            } else {
                                cacheOptions2 = null;
                            }
                            CacheImage cacheImage5 = this.cacheImage;
                            animatedFileDrawable = new AnimatedFileDrawable(cacheImage5.finalFilePath, false, j2, tLRPC$Document, tLRPC$Document == null ? cacheImage5.imageLocation : null, cacheImage5.parentObject, j, cacheImage5.currentAccount, false, cacheOptions2);
                            if (!MessageObject.isWebM(tLRPC$Document) && !MessageObject.isVideoSticker(tLRPC$Document) && !ImageLoader.this.isAnimatedAvatar(this.cacheImage.filter)) {
                                z21 = false;
                            }
                            animatedFileDrawable.setIsWebmSticker(z21);
                            animatedFileDrawable.setLimitFps(z16);
                            Thread.interrupted();
                            onPostExecute(animatedFileDrawable);
                        }
                    }
                    String str12 = this.cacheImage.filter;
                    if (str12 != null) {
                        String[] split4 = str12.split("_");
                        if (split4.length >= 2) {
                            float parseFloat7 = Float.parseFloat(split4[0]);
                            float parseFloat8 = Float.parseFloat(split4[1]);
                            float f8 = AndroidUtilities.density;
                            i6 = (int) (parseFloat8 * f8);
                            i7 = (int) (parseFloat7 * f8);
                            if (!z15) {
                                BitmapsCache.CacheOptions cacheOptions6 = new BitmapsCache.CacheOptions();
                                String str13 = this.cacheImage.filter;
                                if (str13 != null && str13.contains("compress")) {
                                    cacheOptions6.compressQuality = 60;
                                }
                                cacheOptions = cacheOptions6;
                            } else {
                                cacheOptions = null;
                            }
                            CacheImage cacheImage6 = this.cacheImage;
                            File file3 = cacheImage6.finalFilePath;
                            boolean equals = "d".equals(cacheImage6.filter);
                            CacheImage cacheImage7 = this.cacheImage;
                            animatedFileDrawable = new AnimatedFileDrawable(file3, equals, 0L, cacheImage7.imageLocation.document, null, null, j, cacheImage7.currentAccount, false, i7, i6, cacheOptions);
                            if (!MessageObject.isWebM(this.cacheImage.imageLocation.document) && !MessageObject.isVideoSticker(this.cacheImage.imageLocation.document) && !ImageLoader.this.isAnimatedAvatar(this.cacheImage.filter)) {
                                z21 = false;
                            }
                            animatedFileDrawable.setIsWebmSticker(z21);
                            animatedFileDrawable.setLimitFps(z16);
                            Thread.interrupted();
                            onPostExecute(animatedFileDrawable);
                        }
                    }
                    i7 = 0;
                    i6 = 0;
                    if (!z15) {
                    }
                    CacheImage cacheImage62 = this.cacheImage;
                    File file32 = cacheImage62.finalFilePath;
                    boolean equals2 = "d".equals(cacheImage62.filter);
                    CacheImage cacheImage72 = this.cacheImage;
                    animatedFileDrawable = new AnimatedFileDrawable(file32, equals2, 0L, cacheImage72.imageLocation.document, null, null, j, cacheImage72.currentAccount, false, i7, i6, cacheOptions);
                    if (!MessageObject.isWebM(this.cacheImage.imageLocation.document)) {
                        z21 = false;
                    }
                    animatedFileDrawable.setIsWebmSticker(z21);
                    animatedFileDrawable.setLimitFps(z16);
                    Thread.interrupted();
                    onPostExecute(animatedFileDrawable);
                } else {
                    File file4 = cacheImage.finalFilePath;
                    boolean z23 = (cacheImage.secureDocument == null && (cacheImage.encryptionKeyPath == null || file4 == null || !file4.getAbsolutePath().endsWith(".enc"))) ? false : true;
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
                            randomAccessFile2 = new RandomAccessFile(file4, "r");
                            try {
                                try {
                                    byte[] bArr3 = this.cacheImage.type == 1 ? ImageLoader.headerThumb : ImageLoader.header;
                                    randomAccessFile2.readFully(bArr3, 0, bArr3.length);
                                    lowerCase = new String(bArr3).toLowerCase().toLowerCase();
                                } catch (Exception e8) {
                                    e = e8;
                                    FileLog.e(e);
                                    if (randomAccessFile2 != null) {
                                        try {
                                            randomAccessFile2.close();
                                        } catch (Exception e9) {
                                            FileLog.e(e9);
                                        }
                                    }
                                    z = false;
                                    str = this.cacheImage.imageLocation.path;
                                    if (str != null) {
                                    }
                                    z3 = false;
                                    str2 = null;
                                    l = null;
                                    z2 = true;
                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    options.inSampleSize = 1;
                                    if (Build.VERSION.SDK_INT < 21) {
                                    }
                                    boolean z24 = ImageLoader.this.canForce8888;
                                    str3 = this.cacheImage.filter;
                                    if (str3 != null) {
                                    }
                                    bitmap = bitmap5;
                                    f2 = f5;
                                    f = f4;
                                    i = 1;
                                    if (this.cacheImage.type == i) {
                                    }
                                    Thread.interrupted();
                                    if (BuildVars.LOGS_ENABLED) {
                                    }
                                    if (!z6) {
                                    }
                                    onPostExecute(bitmap == null ? new ExtendedBitmapDrawable(bitmap, z6, i2) : null);
                                }
                            } catch (Throwable th11) {
                                th = th11;
                                randomAccessFile = randomAccessFile2;
                                if (randomAccessFile != null) {
                                    try {
                                        randomAccessFile.close();
                                    } catch (Exception e10) {
                                        FileLog.e(e10);
                                    }
                                }
                                throw th;
                            }
                        } catch (Exception e11) {
                            e = e11;
                            randomAccessFile2 = null;
                        } catch (Throwable th12) {
                            th = th12;
                            randomAccessFile = null;
                            if (randomAccessFile != null) {
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
                                            l3 = Long.valueOf(Long.parseLong(str.substring(8, indexOf)));
                                            str4 = str.substring(indexOf + 1);
                                        } else {
                                            str4 = null;
                                            l3 = null;
                                        }
                                        l = l3;
                                        z3 = false;
                                        z2 = false;
                                        str2 = str4;
                                    } else {
                                        if (str.startsWith("vthumb://")) {
                                            int indexOf2 = str.indexOf(":", 9);
                                            if (indexOf2 >= 0) {
                                                l2 = Long.valueOf(Long.parseLong(str.substring(9, indexOf2)));
                                                z3 = true;
                                            } else {
                                                l2 = null;
                                                z3 = false;
                                            }
                                            l = l2;
                                            str2 = null;
                                        } else if (!str.startsWith("http")) {
                                            z3 = false;
                                            str2 = null;
                                            l = null;
                                        }
                                        z2 = false;
                                    }
                                    BitmapFactory.Options options2 = new BitmapFactory.Options();
                                    options2.inSampleSize = 1;
                                    if (Build.VERSION.SDK_INT < 21) {
                                        options2.inPurgeable = true;
                                    }
                                    boolean z242 = ImageLoader.this.canForce8888;
                                    str3 = this.cacheImage.filter;
                                    if (str3 != null) {
                                        String[] split5 = str3.split("_");
                                        if (split5.length >= 2) {
                                            try {
                                                f3 = Float.parseFloat(split5[0]) * AndroidUtilities.density;
                                                try {
                                                    f4 = f3;
                                                    f5 = Float.parseFloat(split5[1]) * AndroidUtilities.density;
                                                } catch (Throwable th13) {
                                                    th4 = th13;
                                                    z5 = z;
                                                    z4 = z3;
                                                    c = 0;
                                                    bitmap = null;
                                                    th3 = th4;
                                                    f2 = 0.0f;
                                                    float f9 = f3;
                                                    i = 1;
                                                    FileLog.e(th3, !(th3 instanceof FileNotFoundException));
                                                    f = f9;
                                                    if (this.cacheImage.type == i) {
                                                    }
                                                    Thread.interrupted();
                                                    if (BuildVars.LOGS_ENABLED) {
                                                    }
                                                    if (!z6) {
                                                    }
                                                    onPostExecute(bitmap == null ? new ExtendedBitmapDrawable(bitmap, z6, i2) : null);
                                                }
                                            } catch (Throwable th14) {
                                                th4 = th14;
                                                z5 = z;
                                                z4 = z3;
                                                f3 = 0.0f;
                                            }
                                        } else {
                                            f5 = 0.0f;
                                            f4 = 0.0f;
                                        }
                                        try {
                                            if (this.cacheImage.filter.contains("b2")) {
                                                c = 3;
                                            } else if (this.cacheImage.filter.contains("b1")) {
                                                c = 2;
                                            } else {
                                                c = this.cacheImage.filter.contains("b") ? (char) 1 : (char) 0;
                                            }
                                            try {
                                                z22 = this.cacheImage.filter.contains(i.TAG);
                                                try {
                                                    if (this.cacheImage.filter.contains("f")) {
                                                        z242 = true;
                                                    }
                                                    if (z || f4 == 0.0f || f5 == 0.0f) {
                                                        z12 = z242;
                                                        c2 = c;
                                                        z5 = z;
                                                        z4 = z3;
                                                    } else {
                                                        try {
                                                            options2.inJustDecodeBounds = true;
                                                            try {
                                                                try {
                                                                    if (l == null || str2 != null) {
                                                                        z12 = z242;
                                                                        c2 = c;
                                                                        if (secureDocumentKey != null) {
                                                                            try {
                                                                                RandomAccessFile randomAccessFile4 = new RandomAccessFile(file4, "r");
                                                                                int length = (int) randomAccessFile4.length();
                                                                                byte[] bArr4 = (byte[]) ImageLoader.bytesLocal.get();
                                                                                if (bArr4 == null || bArr4.length < length) {
                                                                                    bArr4 = null;
                                                                                }
                                                                                if (bArr4 == null) {
                                                                                    bArr4 = new byte[length];
                                                                                    ImageLoader.bytesLocal.set(bArr4);
                                                                                }
                                                                                randomAccessFile4.readFully(bArr4, 0, length);
                                                                                randomAccessFile4.close();
                                                                                EncryptedFileInputStream.decryptBytesWithKeyFile(bArr4, 0, length, secureDocumentKey);
                                                                                z5 = z;
                                                                                z4 = z3;
                                                                                byte[] computeSHA256 = Utilities.computeSHA256(bArr4, 0, length);
                                                                                if (bArr != null && Arrays.equals(computeSHA256, bArr)) {
                                                                                    z14 = false;
                                                                                    int i15 = bArr4[0] & 255;
                                                                                    int i16 = length - i15;
                                                                                    if (!z14) {
                                                                                        BitmapFactory.decodeByteArray(bArr4, i15, i16, options2);
                                                                                    }
                                                                                }
                                                                                z14 = true;
                                                                                int i152 = bArr4[0] & 255;
                                                                                int i162 = length - i152;
                                                                                if (!z14) {
                                                                                }
                                                                            } catch (Throwable th15) {
                                                                                th = th15;
                                                                                z5 = z;
                                                                                z4 = z3;
                                                                                th3 = th;
                                                                                f2 = f5;
                                                                                f3 = f4;
                                                                                c = c2;
                                                                                z242 = z12;
                                                                                bitmap = null;
                                                                                float f92 = f3;
                                                                                i = 1;
                                                                                FileLog.e(th3, !(th3 instanceof FileNotFoundException));
                                                                                f = f92;
                                                                                if (this.cacheImage.type == i) {
                                                                                }
                                                                                Thread.interrupted();
                                                                                if (BuildVars.LOGS_ENABLED) {
                                                                                }
                                                                                if (!z6) {
                                                                                }
                                                                                onPostExecute(bitmap == null ? new ExtendedBitmapDrawable(bitmap, z6, i2) : null);
                                                                            }
                                                                        } else {
                                                                            z5 = z;
                                                                            z4 = z3;
                                                                            if (z23) {
                                                                                fileInputStream3 = new EncryptedFileInputStream(file4, this.cacheImage.encryptionKeyPath);
                                                                            } else {
                                                                                fileInputStream3 = new FileInputStream(file4);
                                                                            }
                                                                            BitmapFactory.decodeStream(fileInputStream3, null, options2);
                                                                            fileInputStream3.close();
                                                                        }
                                                                    } else {
                                                                        if (z3) {
                                                                            try {
                                                                                c2 = c;
                                                                                try {
                                                                                    z12 = z242;
                                                                                    MediaStore.Video.Thumbnails.getThumbnail(ApplicationLoader.applicationContext.getContentResolver(), l.longValue(), 1, options2);
                                                                                } catch (Throwable th16) {
                                                                                    th6 = th16;
                                                                                    z5 = z;
                                                                                    z4 = z3;
                                                                                    c = c2;
                                                                                    bitmap = null;
                                                                                    th3 = th6;
                                                                                    f2 = f5;
                                                                                    f3 = f4;
                                                                                    float f922 = f3;
                                                                                    i = 1;
                                                                                    FileLog.e(th3, !(th3 instanceof FileNotFoundException));
                                                                                    f = f922;
                                                                                    if (this.cacheImage.type == i) {
                                                                                    }
                                                                                    Thread.interrupted();
                                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                                    }
                                                                                    if (!z6) {
                                                                                    }
                                                                                    onPostExecute(bitmap == null ? new ExtendedBitmapDrawable(bitmap, z6, i2) : null);
                                                                                }
                                                                            } catch (Throwable th17) {
                                                                                th6 = th17;
                                                                                z5 = z;
                                                                                z4 = z3;
                                                                            }
                                                                        } else {
                                                                            z12 = z242;
                                                                            c2 = c;
                                                                            MediaStore.Images.Thumbnails.getThumbnail(ApplicationLoader.applicationContext.getContentResolver(), l.longValue(), 1, options2);
                                                                        }
                                                                        z5 = z;
                                                                        z4 = z3;
                                                                    }
                                                                    float f10 = options2.outWidth;
                                                                    float f11 = options2.outHeight;
                                                                    if (f4 >= f5 && f10 > f11) {
                                                                        f6 = Math.max(f10 / f4, f11 / f5);
                                                                    } else {
                                                                        f6 = Math.min(f10 / f4, f11 / f5);
                                                                    }
                                                                    if (f6 < 1.2f) {
                                                                        f6 = 1.0f;
                                                                    }
                                                                    options2.inJustDecodeBounds = false;
                                                                    if (f6 > 1.0f && (f10 > f4 || f11 > f5)) {
                                                                        int i17 = 1;
                                                                        do {
                                                                            i17 *= 2;
                                                                        } while (i17 * 2 < f6);
                                                                        options2.inSampleSize = i17;
                                                                    } else {
                                                                        options2.inSampleSize = (int) f6;
                                                                    }
                                                                } catch (Throwable th18) {
                                                                    th6 = th18;
                                                                    z5 = z;
                                                                    z4 = z3;
                                                                    c = c3;
                                                                    z242 = z13;
                                                                }
                                                            } catch (Throwable th19) {
                                                                th = th19;
                                                            }
                                                        } catch (Throwable th20) {
                                                            th5 = th20;
                                                            z5 = z;
                                                            z4 = z3;
                                                            th3 = th5;
                                                            f2 = f5;
                                                            f3 = f4;
                                                            bitmap = null;
                                                            float f9222 = f3;
                                                            i = 1;
                                                            FileLog.e(th3, !(th3 instanceof FileNotFoundException));
                                                            f = f9222;
                                                            if (this.cacheImage.type == i) {
                                                            }
                                                            Thread.interrupted();
                                                            if (BuildVars.LOGS_ENABLED) {
                                                            }
                                                            if (!z6) {
                                                            }
                                                            onPostExecute(bitmap == null ? new ExtendedBitmapDrawable(bitmap, z6, i2) : null);
                                                        }
                                                    }
                                                    c = c2;
                                                    z242 = z12;
                                                    bitmap5 = null;
                                                } catch (Throwable th21) {
                                                    th5 = th21;
                                                }
                                            } catch (Throwable th22) {
                                                z5 = z;
                                                z4 = z3;
                                                th3 = th22;
                                                f2 = f5;
                                                f3 = f4;
                                                bitmap = null;
                                                z22 = false;
                                                float f92222 = f3;
                                                i = 1;
                                                FileLog.e(th3, !(th3 instanceof FileNotFoundException));
                                                f = f92222;
                                                if (this.cacheImage.type == i) {
                                                }
                                                Thread.interrupted();
                                                if (BuildVars.LOGS_ENABLED) {
                                                }
                                                if (!z6) {
                                                }
                                                onPostExecute(bitmap == null ? new ExtendedBitmapDrawable(bitmap, z6, i2) : null);
                                            }
                                        } catch (Throwable th23) {
                                            z5 = z;
                                            z4 = z3;
                                            th3 = th23;
                                            f2 = f5;
                                            f3 = f4;
                                            c = 0;
                                            bitmap = null;
                                            z22 = false;
                                            float f922222 = f3;
                                            i = 1;
                                            FileLog.e(th3, !(th3 instanceof FileNotFoundException));
                                            f = f922222;
                                            if (this.cacheImage.type == i) {
                                            }
                                            Thread.interrupted();
                                            if (BuildVars.LOGS_ENABLED) {
                                            }
                                            if (!z6) {
                                            }
                                            onPostExecute(bitmap == null ? new ExtendedBitmapDrawable(bitmap, z6, i2) : null);
                                        }
                                    } else {
                                        z5 = z;
                                        z4 = z3;
                                        if (str2 != null) {
                                            try {
                                                options2.inJustDecodeBounds = true;
                                                options2.inPreferredConfig = z242 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
                                                FileInputStream fileInputStream4 = new FileInputStream(file4);
                                                Bitmap decodeStream = BitmapFactory.decodeStream(fileInputStream4, null, options2);
                                                try {
                                                    fileInputStream4.close();
                                                    int i18 = options2.outWidth;
                                                    int i19 = options2.outHeight;
                                                    options2.inJustDecodeBounds = false;
                                                    float min5 = (Math.min(i19, i18) / Math.max(66, Math.min(AndroidUtilities.getRealScreenSize().x, AndroidUtilities.getRealScreenSize().y))) * 6.0f;
                                                    if (min5 < 1.0f) {
                                                        min5 = 1.0f;
                                                    }
                                                    if (min5 > 1.0f) {
                                                        int i20 = 1;
                                                        do {
                                                            i20 *= 2;
                                                        } while (i20 * 2 <= min5);
                                                        options2.inSampleSize = i20;
                                                    } else {
                                                        options2.inSampleSize = (int) min5;
                                                    }
                                                    bitmap5 = decodeStream;
                                                } catch (Throwable th24) {
                                                    th3 = th24;
                                                    bitmap = decodeStream;
                                                    f2 = 0.0f;
                                                    f3 = 0.0f;
                                                    c = 0;
                                                    z22 = false;
                                                    float f9222222 = f3;
                                                    i = 1;
                                                    FileLog.e(th3, !(th3 instanceof FileNotFoundException));
                                                    f = f9222222;
                                                    if (this.cacheImage.type == i) {
                                                    }
                                                    Thread.interrupted();
                                                    if (BuildVars.LOGS_ENABLED) {
                                                    }
                                                    if (!z6) {
                                                    }
                                                    onPostExecute(bitmap == null ? new ExtendedBitmapDrawable(bitmap, z6, i2) : null);
                                                }
                                            } catch (Throwable th25) {
                                                Throwable th26 = th25;
                                                th3 = th26;
                                                f2 = 0.0f;
                                                f3 = 0.0f;
                                                c = 0;
                                                bitmap = null;
                                                z22 = false;
                                                float f92222222 = f3;
                                                i = 1;
                                                FileLog.e(th3, !(th3 instanceof FileNotFoundException));
                                                f = f92222222;
                                                if (this.cacheImage.type == i) {
                                                }
                                                Thread.interrupted();
                                                if (BuildVars.LOGS_ENABLED) {
                                                }
                                                if (!z6) {
                                                }
                                                onPostExecute(bitmap == null ? new ExtendedBitmapDrawable(bitmap, z6, i2) : null);
                                            }
                                        } else {
                                            bitmap5 = null;
                                        }
                                        f5 = 0.0f;
                                        c = 0;
                                        z22 = false;
                                        f4 = 0.0f;
                                    }
                                    bitmap = bitmap5;
                                    f2 = f5;
                                    f = f4;
                                    i = 1;
                                    if (this.cacheImage.type == i) {
                                        try {
                                            ImageLoader.this.lastCacheOutTime = SystemClock.elapsedRealtime();
                                        } catch (Throwable th27) {
                                            th2 = th27;
                                            z6 = false;
                                        }
                                        synchronized (this.sync) {
                                            if (this.isCancelled) {
                                                return;
                                            }
                                            if (z5) {
                                                RandomAccessFile randomAccessFile5 = new RandomAccessFile(file4, "r");
                                                MappedByteBuffer map = randomAccessFile5.getChannel().map(FileChannel.MapMode.READ_ONLY, 0L, file4.length());
                                                BitmapFactory.Options options3 = new BitmapFactory.Options();
                                                options3.inJustDecodeBounds = true;
                                                Utilities.loadWebpImage(null, map, map.limit(), options3, true);
                                                bitmap = Bitmaps.createBitmap(options3.outWidth, options3.outHeight, Bitmap.Config.ARGB_8888);
                                                Utilities.loadWebpImage(bitmap, map, map.limit(), null, !options2.inPurgeable);
                                                randomAccessFile5.close();
                                            } else {
                                                if (!options2.inPurgeable && secureDocumentKey == null) {
                                                    if (z23) {
                                                        fileInputStream = new EncryptedFileInputStream(file4, this.cacheImage.encryptionKeyPath);
                                                    } else {
                                                        fileInputStream = new FileInputStream(file4);
                                                    }
                                                    bitmap = BitmapFactory.decodeStream(fileInputStream, null, options2);
                                                    fileInputStream.close();
                                                }
                                                RandomAccessFile randomAccessFile6 = new RandomAccessFile(file4, "r");
                                                int length2 = (int) randomAccessFile6.length();
                                                byte[] bArr5 = (byte[]) ImageLoader.bytesThumbLocal.get();
                                                if (bArr5 == null || bArr5.length < length2) {
                                                    bArr5 = null;
                                                }
                                                if (bArr5 == null) {
                                                    bArr5 = new byte[length2];
                                                    ImageLoader.bytesThumbLocal.set(bArr5);
                                                }
                                                randomAccessFile6.readFully(bArr5, 0, length2);
                                                randomAccessFile6.close();
                                                if (secureDocumentKey != null) {
                                                    EncryptedFileInputStream.decryptBytesWithKeyFile(bArr5, 0, length2, secureDocumentKey);
                                                    byte[] computeSHA2562 = Utilities.computeSHA256(bArr5, 0, length2);
                                                    if (bArr != null && Arrays.equals(computeSHA2562, bArr)) {
                                                        z7 = false;
                                                        i3 = bArr5[0] & 255;
                                                        length2 -= i3;
                                                    }
                                                    z7 = true;
                                                    i3 = bArr5[0] & 255;
                                                    length2 -= i3;
                                                } else {
                                                    if (z23) {
                                                        EncryptedFileInputStream.decryptBytesWithKeyFile(bArr5, 0, length2, this.cacheImage.encryptionKeyPath);
                                                    }
                                                    z7 = false;
                                                    i3 = 0;
                                                }
                                                if (!z7) {
                                                    bitmap = BitmapFactory.decodeByteArray(bArr5, i3, length2, options2);
                                                }
                                            }
                                            if (bitmap == null) {
                                                if (file4.length() == 0 || this.cacheImage.filter == null) {
                                                    file4.delete();
                                                }
                                                z6 = false;
                                            } else {
                                                if (this.cacheImage.filter != null) {
                                                    float width = bitmap.getWidth();
                                                    float height = bitmap.getHeight();
                                                    if (!options2.inPurgeable && f != 0.0f && width != f && width > f + 20.0f && bitmap != (createScaledBitmap = Bitmaps.createScaledBitmap(bitmap, (int) f, (int) (height / (width / f)), true))) {
                                                        bitmap.recycle();
                                                        bitmap = createScaledBitmap;
                                                    }
                                                }
                                                z6 = z22 ? Utilities.needInvert(bitmap, options2.inPurgeable ? 0 : 1, bitmap.getWidth(), bitmap.getHeight(), bitmap.getRowBytes()) != 0 : false;
                                                try {
                                                    if (c == 1) {
                                                        if (bitmap.getConfig() == Bitmap.Config.ARGB_8888) {
                                                            Utilities.blurBitmap(bitmap, 3, options2.inPurgeable ? 0 : 1, bitmap.getWidth(), bitmap.getHeight(), bitmap.getRowBytes());
                                                        }
                                                    } else if (c == 2) {
                                                        if (bitmap.getConfig() == Bitmap.Config.ARGB_8888) {
                                                            Utilities.blurBitmap(bitmap, 1, options2.inPurgeable ? 0 : 1, bitmap.getWidth(), bitmap.getHeight(), bitmap.getRowBytes());
                                                        }
                                                    } else if (c == 3) {
                                                        if (bitmap.getConfig() == Bitmap.Config.ARGB_8888) {
                                                            Utilities.blurBitmap(bitmap, 7, options2.inPurgeable ? 0 : 1, bitmap.getWidth(), bitmap.getHeight(), bitmap.getRowBytes());
                                                            Utilities.blurBitmap(bitmap, 7, options2.inPurgeable ? 0 : 1, bitmap.getWidth(), bitmap.getHeight(), bitmap.getRowBytes());
                                                            Utilities.blurBitmap(bitmap, 7, options2.inPurgeable ? 0 : 1, bitmap.getWidth(), bitmap.getHeight(), bitmap.getRowBytes());
                                                        }
                                                    } else if (c == 0) {
                                                        if (options2.inPurgeable) {
                                                            Utilities.pinBitmap(bitmap);
                                                        }
                                                    }
                                                } catch (Throwable th28) {
                                                    th2 = th28;
                                                    FileLog.e(th2);
                                                    i2 = 0;
                                                    Thread.interrupted();
                                                    if (BuildVars.LOGS_ENABLED) {
                                                    }
                                                    if (!z6) {
                                                    }
                                                    onPostExecute(bitmap == null ? new ExtendedBitmapDrawable(bitmap, z6, i2) : null);
                                                }
                                            }
                                            i2 = 0;
                                        }
                                    } else {
                                        int i21 = 20;
                                        if (l != null) {
                                            i21 = 0;
                                        }
                                        if (i21 != 0) {
                                            try {
                                            } catch (Throwable th29) {
                                                th = th29;
                                            }
                                            if (ImageLoader.this.lastCacheOutTime != 0) {
                                                bitmap2 = bitmap;
                                                boolean z25 = z22;
                                                long j3 = i21;
                                                if (ImageLoader.this.lastCacheOutTime > SystemClock.elapsedRealtime() - j3) {
                                                    try {
                                                        z8 = z25;
                                                        if (Build.VERSION.SDK_INT < 21) {
                                                            Thread.sleep(j3);
                                                        }
                                                    } catch (Throwable th30) {
                                                        th = th30;
                                                        bitmap = bitmap2;
                                                        i2 = 0;
                                                        z9 = false;
                                                        FileLog.e(th);
                                                        z6 = z9;
                                                        Thread.interrupted();
                                                        if (BuildVars.LOGS_ENABLED) {
                                                        }
                                                        if (!z6) {
                                                        }
                                                        onPostExecute(bitmap == null ? new ExtendedBitmapDrawable(bitmap, z6, i2) : null);
                                                    }
                                                } else {
                                                    z8 = z25;
                                                }
                                                ImageLoader.this.lastCacheOutTime = SystemClock.elapsedRealtime();
                                                synchronized (this.sync) {
                                                    if (this.isCancelled) {
                                                        return;
                                                    }
                                                    if (!z242) {
                                                        CacheImage cacheImage8 = this.cacheImage;
                                                        if (cacheImage8.filter != null && c == 0 && cacheImage8.imageLocation.path == null) {
                                                            options2.inPreferredConfig = Bitmap.Config.RGB_565;
                                                            options2.inDither = false;
                                                            if (l != null || str2 != null) {
                                                                bitmap = bitmap2;
                                                            } else if (z4) {
                                                                bitmap = MediaStore.Video.Thumbnails.getThumbnail(ApplicationLoader.applicationContext.getContentResolver(), l.longValue(), 1, options2);
                                                            } else {
                                                                bitmap = MediaStore.Images.Thumbnails.getThumbnail(ApplicationLoader.applicationContext.getContentResolver(), l.longValue(), 1, options2);
                                                            }
                                                            if (bitmap == null) {
                                                                try {
                                                                } catch (Throwable th31) {
                                                                    th = th31;
                                                                    i2 = 0;
                                                                    z9 = false;
                                                                    FileLog.e(th);
                                                                    z6 = z9;
                                                                    Thread.interrupted();
                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                    }
                                                                    if (!z6) {
                                                                    }
                                                                    onPostExecute(bitmap == null ? new ExtendedBitmapDrawable(bitmap, z6, i2) : null);
                                                                }
                                                                if (z5) {
                                                                    RandomAccessFile randomAccessFile7 = new RandomAccessFile(file4, "r");
                                                                    MappedByteBuffer map2 = randomAccessFile7.getChannel().map(FileChannel.MapMode.READ_ONLY, 0L, file4.length());
                                                                    BitmapFactory.Options options4 = new BitmapFactory.Options();
                                                                    options4.inJustDecodeBounds = true;
                                                                    Utilities.loadWebpImage(null, map2, map2.limit(), options4, true);
                                                                    bitmap = Bitmaps.createBitmap(options4.outWidth, options4.outHeight, Bitmap.Config.ARGB_8888);
                                                                    Utilities.loadWebpImage(bitmap, map2, map2.limit(), null, !options2.inPurgeable);
                                                                    randomAccessFile7.close();
                                                                } else {
                                                                    if (!options2.inPurgeable && secureDocumentKey == null && Build.VERSION.SDK_INT > 29) {
                                                                        if (z23) {
                                                                            fileInputStream2 = new EncryptedFileInputStream(file4, this.cacheImage.encryptionKeyPath);
                                                                        } else {
                                                                            fileInputStream2 = new FileInputStream(file4);
                                                                        }
                                                                        if (this.cacheImage.imageLocation.document instanceof TLRPC$TL_document) {
                                                                            try {
                                                                                attributeInt = new ExifInterface(fileInputStream2).getAttributeInt("Orientation", 1);
                                                                            } catch (Throwable unused) {
                                                                            }
                                                                            if (attributeInt == 3) {
                                                                                i5 = 180;
                                                                            } else if (attributeInt != 6) {
                                                                                if (attributeInt == 8) {
                                                                                    i5 = 270;
                                                                                }
                                                                                i5 = 0;
                                                                            } else {
                                                                                i5 = 90;
                                                                            }
                                                                            try {
                                                                                fileInputStream2.getChannel().position(0L);
                                                                                i2 = i5;
                                                                                rect = null;
                                                                            } catch (Throwable th32) {
                                                                                th = th32;
                                                                                i2 = i5;
                                                                                z9 = false;
                                                                                FileLog.e(th);
                                                                                z6 = z9;
                                                                                Thread.interrupted();
                                                                                if (BuildVars.LOGS_ENABLED) {
                                                                                }
                                                                                if (!z6) {
                                                                                }
                                                                                onPostExecute(bitmap == null ? new ExtendedBitmapDrawable(bitmap, z6, i2) : null);
                                                                            }
                                                                        } else {
                                                                            rect = null;
                                                                            i2 = 0;
                                                                        }
                                                                        try {
                                                                            bitmap = BitmapFactory.decodeStream(fileInputStream2, rect, options2);
                                                                            fileInputStream2.close();
                                                                            if (bitmap == null) {
                                                                                if (z2 && (file4.length() == 0 || this.cacheImage.filter == null)) {
                                                                                    file4.delete();
                                                                                }
                                                                                z9 = false;
                                                                            } else {
                                                                                if (this.cacheImage.filter != null) {
                                                                                    float width2 = bitmap.getWidth();
                                                                                    float height2 = bitmap.getHeight();
                                                                                    if (!options2.inPurgeable && f != 0.0f && width2 != f && width2 > f + 20.0f) {
                                                                                        if (width2 <= height2 || f <= f2) {
                                                                                            float f12 = height2 / f2;
                                                                                            if (f12 > 1.0f) {
                                                                                                bitmap4 = Bitmaps.createScaledBitmap(bitmap, (int) (width2 / f12), (int) f2, true);
                                                                                                if (bitmap != bitmap4) {
                                                                                                    bitmap.recycle();
                                                                                                    bitmap = bitmap4;
                                                                                                }
                                                                                            }
                                                                                            bitmap4 = bitmap;
                                                                                            if (bitmap != bitmap4) {
                                                                                            }
                                                                                        } else {
                                                                                            float f13 = width2 / f;
                                                                                            if (f13 > 1.0f) {
                                                                                                bitmap4 = Bitmaps.createScaledBitmap(bitmap, (int) f, (int) (height2 / f13), true);
                                                                                                if (bitmap != bitmap4) {
                                                                                                }
                                                                                            }
                                                                                            bitmap4 = bitmap;
                                                                                            if (bitmap != bitmap4) {
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if (bitmap != null) {
                                                                                        if (z8) {
                                                                                            Bitmap createScaledBitmap2 = bitmap.getWidth() * bitmap.getHeight() > 22500 ? Bitmaps.createScaledBitmap(bitmap, 100, 100, false) : bitmap;
                                                                                            z9 = Utilities.needInvert(createScaledBitmap2, options2.inPurgeable ? 0 : 1, createScaledBitmap2.getWidth(), createScaledBitmap2.getHeight(), createScaledBitmap2.getRowBytes()) != 0;
                                                                                            if (createScaledBitmap2 != bitmap) {
                                                                                                try {
                                                                                                    createScaledBitmap2.recycle();
                                                                                                } catch (Throwable th33) {
                                                                                                    th = th33;
                                                                                                    FileLog.e(th);
                                                                                                    z6 = z9;
                                                                                                    Thread.interrupted();
                                                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                                                    }
                                                                                                    if (!z6) {
                                                                                                    }
                                                                                                    onPostExecute(bitmap == null ? new ExtendedBitmapDrawable(bitmap, z6, i2) : null);
                                                                                                }
                                                                                            }
                                                                                        } else {
                                                                                            z9 = false;
                                                                                        }
                                                                                        if (c != 0 && (height2 > 100.0f || width2 > 100.0f)) {
                                                                                            height2 = 80.0f;
                                                                                            bitmap = Bitmaps.createScaledBitmap(bitmap, 80, 80, false);
                                                                                            width2 = 80.0f;
                                                                                        }
                                                                                        if (c == 0 || height2 >= 100.0f || width2 >= 100.0f) {
                                                                                            bitmap3 = bitmap;
                                                                                            z11 = false;
                                                                                            if (!z11) {
                                                                                                try {
                                                                                                    if (options2.inPurgeable) {
                                                                                                        Utilities.pinBitmap(bitmap3);
                                                                                                    }
                                                                                                } catch (Throwable th34) {
                                                                                                    th = th34;
                                                                                                    bitmap = bitmap3;
                                                                                                    FileLog.e(th);
                                                                                                    z6 = z9;
                                                                                                    Thread.interrupted();
                                                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                                                    }
                                                                                                    if (!z6) {
                                                                                                    }
                                                                                                    onPostExecute(bitmap == null ? new ExtendedBitmapDrawable(bitmap, z6, i2) : null);
                                                                                                }
                                                                                            }
                                                                                            bitmap = bitmap3;
                                                                                        } else {
                                                                                            if (bitmap.getConfig() == Bitmap.Config.ARGB_8888) {
                                                                                                Utilities.blurBitmap(bitmap, 3, options2.inPurgeable ? 0 : 1, bitmap.getWidth(), bitmap.getHeight(), bitmap.getRowBytes());
                                                                                            }
                                                                                            bitmap3 = bitmap;
                                                                                            z11 = true;
                                                                                            if (!z11) {
                                                                                            }
                                                                                            bitmap = bitmap3;
                                                                                        }
                                                                                    }
                                                                                }
                                                                                bitmap3 = bitmap;
                                                                                z9 = false;
                                                                                z11 = false;
                                                                                if (!z11) {
                                                                                }
                                                                                bitmap = bitmap3;
                                                                            }
                                                                        } catch (Throwable th35) {
                                                                            th = th35;
                                                                            z9 = false;
                                                                            FileLog.e(th);
                                                                            z6 = z9;
                                                                            Thread.interrupted();
                                                                            if (BuildVars.LOGS_ENABLED) {
                                                                            }
                                                                            if (!z6) {
                                                                            }
                                                                            onPostExecute(bitmap == null ? new ExtendedBitmapDrawable(bitmap, z6, i2) : null);
                                                                        }
                                                                        z6 = z9;
                                                                    }
                                                                    RandomAccessFile randomAccessFile8 = new RandomAccessFile(file4, "r");
                                                                    int length3 = (int) randomAccessFile8.length();
                                                                    byte[] bArr6 = (byte[]) ImageLoader.bytesLocal.get();
                                                                    if (bArr6 == null || bArr6.length < length3) {
                                                                        bArr6 = null;
                                                                    }
                                                                    if (bArr6 == null) {
                                                                        bArr6 = new byte[length3];
                                                                        ImageLoader.bytesLocal.set(bArr6);
                                                                    }
                                                                    randomAccessFile8.readFully(bArr6, 0, length3);
                                                                    randomAccessFile8.close();
                                                                    if (secureDocumentKey != null) {
                                                                        EncryptedFileInputStream.decryptBytesWithKeyFile(bArr6, 0, length3, secureDocumentKey);
                                                                        byte[] computeSHA2563 = Utilities.computeSHA256(bArr6, 0, length3);
                                                                        if (bArr != null && Arrays.equals(computeSHA2563, bArr)) {
                                                                            z10 = false;
                                                                            i4 = bArr6[0] & 255;
                                                                            length3 -= i4;
                                                                        }
                                                                        z10 = true;
                                                                        i4 = bArr6[0] & 255;
                                                                        length3 -= i4;
                                                                    } else {
                                                                        if (z23) {
                                                                            EncryptedFileInputStream.decryptBytesWithKeyFile(bArr6, 0, length3, this.cacheImage.encryptionKeyPath);
                                                                        }
                                                                        z10 = false;
                                                                        i4 = 0;
                                                                    }
                                                                    if (!z10) {
                                                                        bitmap = BitmapFactory.decodeByteArray(bArr6, i4, length3, options2);
                                                                    }
                                                                }
                                                            }
                                                            i2 = 0;
                                                            if (bitmap == null) {
                                                            }
                                                            z6 = z9;
                                                        }
                                                    }
                                                    options2.inPreferredConfig = Bitmap.Config.ARGB_8888;
                                                    options2.inDither = false;
                                                    if (l != null) {
                                                    }
                                                    bitmap = bitmap2;
                                                    if (bitmap == null) {
                                                    }
                                                    i2 = 0;
                                                    if (bitmap == null) {
                                                    }
                                                    z6 = z9;
                                                }
                                            }
                                        }
                                        bitmap2 = bitmap;
                                        z8 = z22;
                                        ImageLoader.this.lastCacheOutTime = SystemClock.elapsedRealtime();
                                        synchronized (this.sync) {
                                        }
                                    }
                                    Thread.interrupted();
                                    if (BuildVars.LOGS_ENABLED && z23) {
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("Image Loader image is empty = ");
                                        sb.append(bitmap != null);
                                        sb.append(" ");
                                        sb.append(file4);
                                        FileLog.e(sb.toString());
                                    }
                                    if (!z6 || i2 != 0) {
                                        onPostExecute(bitmap == null ? new ExtendedBitmapDrawable(bitmap, z6, i2) : null);
                                    } else {
                                        onPostExecute(bitmap != null ? new BitmapDrawable(bitmap) : null);
                                        return;
                                    }
                                }
                                z3 = false;
                                str2 = null;
                                l = null;
                                z2 = true;
                                BitmapFactory.Options options22 = new BitmapFactory.Options();
                                options22.inSampleSize = 1;
                                if (Build.VERSION.SDK_INT < 21) {
                                }
                                boolean z2422 = ImageLoader.this.canForce8888;
                                str3 = this.cacheImage.filter;
                                if (str3 != null) {
                                }
                                bitmap = bitmap5;
                                f2 = f5;
                                f = f4;
                                i = 1;
                                if (this.cacheImage.type == i) {
                                }
                                Thread.interrupted();
                                if (BuildVars.LOGS_ENABLED) {
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("Image Loader image is empty = ");
                                    sb2.append(bitmap != null);
                                    sb2.append(" ");
                                    sb2.append(file4);
                                    FileLog.e(sb2.toString());
                                }
                                if (!z6) {
                                }
                                onPostExecute(bitmap == null ? new ExtendedBitmapDrawable(bitmap, z6, i2) : null);
                            }
                        }
                        z = false;
                        randomAccessFile2.close();
                        str = this.cacheImage.imageLocation.path;
                        if (str != null) {
                        }
                        z3 = false;
                        str2 = null;
                        l = null;
                        z2 = true;
                        BitmapFactory.Options options222 = new BitmapFactory.Options();
                        options222.inSampleSize = 1;
                        if (Build.VERSION.SDK_INT < 21) {
                        }
                        boolean z24222 = ImageLoader.this.canForce8888;
                        str3 = this.cacheImage.filter;
                        if (str3 != null) {
                        }
                        bitmap = bitmap5;
                        f2 = f5;
                        f = f4;
                        i = 1;
                        if (this.cacheImage.type == i) {
                        }
                        Thread.interrupted();
                        if (BuildVars.LOGS_ENABLED) {
                        }
                        if (!z6) {
                        }
                        onPostExecute(bitmap == null ? new ExtendedBitmapDrawable(bitmap, z6, i2) : null);
                    }
                    z = false;
                    str = this.cacheImage.imageLocation.path;
                    if (str != null) {
                    }
                    z3 = false;
                    str2 = null;
                    l = null;
                    z2 = true;
                    BitmapFactory.Options options2222 = new BitmapFactory.Options();
                    options2222.inSampleSize = 1;
                    if (Build.VERSION.SDK_INT < 21) {
                    }
                    boolean z242222 = ImageLoader.this.canForce8888;
                    str3 = this.cacheImage.filter;
                    if (str3 != null) {
                    }
                    bitmap = bitmap5;
                    f2 = f5;
                    f = f4;
                    i = 1;
                    if (this.cacheImage.type == i) {
                    }
                    Thread.interrupted();
                    if (BuildVars.LOGS_ENABLED) {
                    }
                    if (!z6) {
                    }
                    onPostExecute(bitmap == null ? new ExtendedBitmapDrawable(bitmap, z6, i2) : null);
                }
            }
        }

        private void loadLastFrame(final RLottieDrawable rLottieDrawable, int i, int i2) {
            final Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(createBitmap);
            canvas.scale(2.0f, 2.0f, i / 2.0f, i2 / 2.0f);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$CacheOutTask$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.CacheOutTask.this.lambda$loadLastFrame$1(rLottieDrawable, canvas, createBitmap);
                }
            });
        }

        public /* synthetic */ void lambda$loadLastFrame$1(final RLottieDrawable rLottieDrawable, final Canvas canvas, final Bitmap bitmap) {
            rLottieDrawable.setOnFrameReadyRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$CacheOutTask$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.CacheOutTask.this.lambda$loadLastFrame$0(rLottieDrawable, canvas, bitmap);
                }
            });
            rLottieDrawable.setCurrentFrame(rLottieDrawable.getFramesCount() - 1, true, true);
        }

        public /* synthetic */ void lambda$loadLastFrame$0(RLottieDrawable rLottieDrawable, Canvas canvas, Bitmap bitmap) {
            BitmapDrawable bitmapDrawable = null;
            rLottieDrawable.setOnFrameReadyRunnable(null);
            if (rLottieDrawable.getBackgroundBitmap() != null || rLottieDrawable.getRenderingBitmap() != null) {
                canvas.drawBitmap(rLottieDrawable.getBackgroundBitmap() != null ? rLottieDrawable.getBackgroundBitmap() : rLottieDrawable.getRenderingBitmap(), 0.0f, 0.0f, (Paint) null);
                bitmapDrawable = new BitmapDrawable(bitmap);
            }
            onPostExecute(bitmapDrawable);
            rLottieDrawable.recycle();
        }

        private void onPostExecute(final Drawable drawable) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$CacheOutTask$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.CacheOutTask.this.lambda$onPostExecute$3(drawable);
                }
            });
        }

        public /* synthetic */ void lambda$onPostExecute$3(Drawable drawable) {
            final String str;
            final Drawable drawable2 = null;
            r1 = null;
            r1 = null;
            r1 = null;
            String str2 = null;
            if (drawable instanceof RLottieDrawable) {
                RLottieDrawable rLottieDrawable = (RLottieDrawable) drawable;
                Drawable drawable3 = (Drawable) ImageLoader.this.lottieMemCache.get(this.cacheImage.key);
                if (drawable3 == null) {
                    ImageLoader.this.lottieMemCache.put(this.cacheImage.key, rLottieDrawable);
                    drawable = rLottieDrawable;
                } else {
                    rLottieDrawable.recycle();
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
                        ImageLoader.CacheOutTask.this.lambda$onPostExecute$2(drawable2, str);
                    }
                });
            } else {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                BitmapDrawable fromMemCache = ImageLoader.this.getFromMemCache(this.cacheImage.key);
                boolean z = true;
                if (fromMemCache == null) {
                    if (this.cacheImage.key.endsWith("_f")) {
                        ImageLoader.this.wallpaperMemCache.put(this.cacheImage.key, bitmapDrawable);
                        z = false;
                        drawable = bitmapDrawable;
                    } else if (this.cacheImage.key.endsWith("_isc") || bitmapDrawable.getBitmap().getWidth() > AndroidUtilities.density * 80.0f || bitmapDrawable.getBitmap().getHeight() > AndroidUtilities.density * 80.0f) {
                        ImageLoader.this.memCache.put(this.cacheImage.key, bitmapDrawable);
                        drawable = bitmapDrawable;
                    } else {
                        ImageLoader.this.smallImagesMemCache.put(this.cacheImage.key, bitmapDrawable);
                        drawable = bitmapDrawable;
                    }
                } else {
                    bitmapDrawable.getBitmap().recycle();
                    drawable = fromMemCache;
                }
                if (drawable != null && z) {
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
                    ImageLoader.CacheOutTask.this.lambda$onPostExecute$2(drawable2, str);
                }
            });
        }

        public /* synthetic */ void lambda$onPostExecute$2(Drawable drawable, String str) {
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
        bArr2[164] = bArr[1];
        bArr2[166] = bArr[2];
        Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bArr2, 0, length);
        if (decodeByteArray != null && !TextUtils.isEmpty(str) && str.contains("b")) {
            Utilities.blurBitmap(decodeByteArray, 3, 1, decodeByteArray.getWidth(), decodeByteArray.getHeight(), decodeByteArray.getRowBytes());
        }
        return decodeByteArray;
    }

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
        protected SecureDocument secureDocument;
        protected long size;
        protected File tempFilePath;
        protected int type;
        protected ArrayList<Integer> types;
        protected String url;

        private CacheImage() {
            ImageLoader.this = r1;
            this.imageReceiverArray = new ArrayList<>();
            this.imageReceiverGuidsArray = new ArrayList<>();
            this.keys = new ArrayList<>();
            this.filters = new ArrayList<>();
            this.types = new ArrayList<>();
        }

        public void addImageReceiver(ImageReceiver imageReceiver, String str, String str2, int i, int i2) {
            int indexOf = this.imageReceiverArray.indexOf(imageReceiver);
            if (indexOf >= 0) {
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
                if (this.key == null) {
                    return;
                }
                ImageLoader.this.imageLoadingByKeys.remove(this.key);
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
                    if (str != null) {
                        return;
                    }
                    ImageLoader.this.decrementUseCount(str);
                    return;
                }
            }
            while (i < arrayList.size()) {
                ((ImageReceiver) arrayList.get(i)).setImageBitmapByKey(drawable, this.key, this.types.get(i).intValue(), false, ((Integer) arrayList2.get(i)).intValue());
                i++;
            }
            if (str != null) {
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
        boolean z = true;
        this.thumbGeneratingQueue.setPriority(1);
        int memoryClass = ((ActivityManager) ApplicationLoader.applicationContext.getSystemService("activity")).getMemoryClass();
        z = memoryClass < 192 ? false : z;
        this.canForce8888 = z;
        int min = Math.min(z ? 30 : 15, memoryClass / 7) * 1024 * 1024;
        float f = min;
        this.memCache = new LruCache<BitmapDrawable>((int) (0.8f * f)) { // from class: org.telegram.messenger.ImageLoader.1
            public int sizeOf(String str, BitmapDrawable bitmapDrawable) {
                return bitmapDrawable.getBitmap().getByteCount();
            }

            public void entryRemoved(boolean z2, String str, BitmapDrawable bitmapDrawable, BitmapDrawable bitmapDrawable2) {
                if (ImageLoader.this.ignoreRemoval == null || !ImageLoader.this.ignoreRemoval.equals(str)) {
                    Integer num = (Integer) ImageLoader.this.bitmapUseCounts.get(str);
                    if (num != null && num.intValue() != 0) {
                        return;
                    }
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    if (bitmap.isRecycled()) {
                        return;
                    }
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(bitmap);
                    AndroidUtilities.recycleBitmaps(arrayList);
                }
            }
        };
        this.smallImagesMemCache = new LruCache<BitmapDrawable>((int) (f * 0.2f)) { // from class: org.telegram.messenger.ImageLoader.2
            public int sizeOf(String str, BitmapDrawable bitmapDrawable) {
                return bitmapDrawable.getBitmap().getByteCount();
            }

            public void entryRemoved(boolean z2, String str, BitmapDrawable bitmapDrawable, BitmapDrawable bitmapDrawable2) {
                if (ImageLoader.this.ignoreRemoval == null || !ImageLoader.this.ignoreRemoval.equals(str)) {
                    Integer num = (Integer) ImageLoader.this.bitmapUseCounts.get(str);
                    if (num != null && num.intValue() != 0) {
                        return;
                    }
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    if (bitmap.isRecycled()) {
                        return;
                    }
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(bitmap);
                    AndroidUtilities.recycleBitmaps(arrayList);
                }
            }
        };
        this.wallpaperMemCache = new LruCache<BitmapDrawable>(min / 4) { // from class: org.telegram.messenger.ImageLoader.3
            public int sizeOf(String str, BitmapDrawable bitmapDrawable) {
                return bitmapDrawable.getBitmap().getByteCount();
            }
        };
        this.lottieMemCache = new LruCache<BitmapDrawable>(10485760) { // from class: org.telegram.messenger.ImageLoader.4
            public int sizeOf(String str, BitmapDrawable bitmapDrawable) {
                return bitmapDrawable.getIntrinsicWidth() * bitmapDrawable.getIntrinsicHeight() * 4 * 2;
            }

            public BitmapDrawable put(String str, BitmapDrawable bitmapDrawable) {
                if (bitmapDrawable instanceof AnimatedFileDrawable) {
                    ImageLoader.this.cachedAnimatedFileDrawables.add((AnimatedFileDrawable) bitmapDrawable);
                }
                return (BitmapDrawable) super.put(str, (String) bitmapDrawable);
            }

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
                    if (!(bitmapDrawable instanceof RLottieDrawable)) {
                        return;
                    }
                    ((RLottieDrawable) bitmapDrawable).recycle();
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
            FileLoader.getInstance(i).setDelegate(new AnonymousClass5(i));
        }
        FileLoader.setMediaDirs(sparseArray);
        AnonymousClass6 anonymousClass6 = new AnonymousClass6();
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
            ApplicationLoader.applicationContext.registerReceiver(anonymousClass6, intentFilter);
        } catch (Throwable unused) {
        }
        checkMediaPaths();
    }

    /* renamed from: org.telegram.messenger.ImageLoader$5 */
    /* loaded from: classes.dex */
    public class AnonymousClass5 implements FileLoader.FileLoaderDelegate {
        final /* synthetic */ int val$currentAccount;

        AnonymousClass5(int i) {
            ImageLoader.this = r1;
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
                        ImageLoader.AnonymousClass5.lambda$fileUploadProgressChanged$0(i, str, j, j2, z);
                    }
                });
            }
        }

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
                    ImageLoader.AnonymousClass5.this.lambda$fileDidUploaded$2(i, str, tLRPC$InputFile, tLRPC$InputEncryptedFile, bArr, bArr2, j);
                }
            });
        }

        public static /* synthetic */ void lambda$fileDidUploaded$1(int i, String str, TLRPC$InputFile tLRPC$InputFile, TLRPC$InputEncryptedFile tLRPC$InputEncryptedFile, byte[] bArr, byte[] bArr2, long j) {
            NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.fileUploaded, str, tLRPC$InputFile, tLRPC$InputEncryptedFile, bArr, bArr2, Long.valueOf(j));
        }

        public /* synthetic */ void lambda$fileDidUploaded$2(final int i, final String str, final TLRPC$InputFile tLRPC$InputFile, final TLRPC$InputEncryptedFile tLRPC$InputEncryptedFile, final byte[] bArr, final byte[] bArr2, final long j) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.AnonymousClass5.lambda$fileDidUploaded$1(i, str, tLRPC$InputFile, tLRPC$InputEncryptedFile, bArr, bArr2, j);
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
                    ImageLoader.AnonymousClass5.this.lambda$fileDidFailedUpload$4(i, str, z);
                }
            });
        }

        public static /* synthetic */ void lambda$fileDidFailedUpload$3(int i, String str, boolean z) {
            NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.fileUploadFailed, str, Boolean.valueOf(z));
        }

        public /* synthetic */ void lambda$fileDidFailedUpload$4(final int i, final String str, final boolean z) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.AnonymousClass5.lambda$fileDidFailedUpload$3(i, str, z);
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
                    ImageLoader.AnonymousClass5.this.lambda$fileDidLoaded$5(file, str, obj, i2, i);
                }
            });
        }

        public /* synthetic */ void lambda$fileDidLoaded$5(File file, String str, Object obj, int i, int i2) {
            int i3;
            if (SharedConfig.saveToGalleryFlags != 0 && file != null && ((str.endsWith(".mp4") || str.endsWith(".jpg")) && (obj instanceof MessageObject))) {
                long dialogId = ((MessageObject) obj).getDialogId();
                if (dialogId >= 0) {
                    i3 = 1;
                } else {
                    i3 = ChatObject.isChannelAndNotMegaGroup(MessagesController.getInstance(i).getChat(Long.valueOf(-dialogId))) ? 4 : 2;
                }
                if ((i3 & SharedConfig.saveToGalleryFlags) != 0) {
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
                    ImageLoader.AnonymousClass5.this.lambda$fileDidFailedLoad$6(str, i, i2);
                }
            });
        }

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
                        ImageLoader.AnonymousClass5.lambda$fileLoadProgressChanged$7(i, str, j, j2);
                    }
                });
            }
        }

        public static /* synthetic */ void lambda$fileLoadProgressChanged$7(int i, String str, long j, long j2) {
            NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.fileLoadProgressChanged, str, Long.valueOf(j), Long.valueOf(j2));
        }
    }

    /* renamed from: org.telegram.messenger.ImageLoader$6 */
    /* loaded from: classes.dex */
    public class AnonymousClass6 extends BroadcastReceiver {
        AnonymousClass6() {
            ImageLoader.this = r1;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("file system changed");
            }
            Runnable runnable = new Runnable() { // from class: org.telegram.messenger.ImageLoader$6$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.AnonymousClass6.this.lambda$onReceive$0();
                }
            };
            if ("android.intent.action.MEDIA_UNMOUNTED".equals(intent.getAction())) {
                AndroidUtilities.runOnUIThread(runnable, 1000L);
            } else {
                runnable.run();
            }
        }

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

    @TargetApi(AvailableCode.ERROR_NO_ACTIVITY)
    private static void moveDirectory(File file, final File file2) {
        if (file.exists()) {
            if (!file2.exists() && !file2.mkdir()) {
                return;
            }
            try {
                Stream convert = C$r8$wrapper$java$util$stream$Stream$VWRP.convert(Files.list(file.toPath()));
                convert.forEach(new Consumer() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda13
                    @Override // j$.util.function.Consumer
                    public final void accept(Object obj) {
                        ImageLoader.lambda$moveDirectory$2(file2, (Path) obj);
                    }

                    @Override // j$.util.function.Consumer
                    public /* synthetic */ Consumer andThen(Consumer consumer) {
                        return consumer.getClass();
                    }
                });
                convert.close();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

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

    /* JADX WARN: Removed duplicated region for block: B:108:0x0264 A[Catch: Exception -> 0x0277, TRY_LEAVE, TryCatch #7 {Exception -> 0x0277, blocks: (B:102:0x0247, B:104:0x0255, B:106:0x025b, B:108:0x0264), top: B:141:0x0247, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0298 A[Catch: Exception -> 0x02ab, TRY_LEAVE, TryCatch #2 {Exception -> 0x02ab, blocks: (B:111:0x027b, B:113:0x0289, B:115:0x028f, B:117:0x0298), top: B:131:0x027b, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:145:0x0114 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:153:0x0103 A[EDGE_INSN: B:153:0x0103->B:51:0x0103 ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00e3 A[Catch: Exception -> 0x02bd, TryCatch #0 {Exception -> 0x02bd, blocks: (B:10:0x003d, B:12:0x0049, B:14:0x0054, B:16:0x005c, B:18:0x0062, B:20:0x0069, B:23:0x007d, B:24:0x0080, B:38:0x00ae, B:39:0x00b1, B:40:0x00c0, B:41:0x00c7, B:43:0x00d0, B:45:0x00d8, B:47:0x00e3, B:49:0x00f5, B:50:0x0100, B:51:0x0103, B:62:0x0145, B:71:0x0179, B:80:0x01ba, B:89:0x01fb, B:98:0x023c, B:100:0x0241, B:110:0x0278, B:119:0x02ac, B:120:0x02b0, B:122:0x02b4, B:123:0x02b9, B:111:0x027b, B:113:0x0289, B:115:0x028f, B:117:0x0298, B:90:0x01fe, B:92:0x0210, B:94:0x0217, B:96:0x0226, B:81:0x01bd, B:83:0x01cf, B:85:0x01d6, B:87:0x01e5, B:72:0x017c, B:74:0x018e, B:76:0x0195, B:78:0x01a4, B:63:0x0148, B:65:0x0158, B:67:0x015e, B:69:0x0165, B:102:0x0247, B:104:0x0255, B:106:0x025b, B:108:0x0264, B:54:0x0114, B:56:0x0124, B:58:0x012a, B:60:0x0131), top: B:127:0x003d, inners: #2, #3, #4, #5, #6, #7, #9 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public SparseArray<File> createMediaPaths() {
        File file;
        Exception e;
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
            } catch (Exception e2) {
                FileLog.e(e2);
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
                if (Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(SharedConfig.storageCacheDir) && (rootDirs = AndroidUtilities.getRootDirs()) != null) {
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
                            } catch (Exception e3) {
                                file = file6;
                                e = e3;
                            }
                            try {
                                file.mkdirs();
                            } catch (Exception e4) {
                                e = e4;
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
                                    } catch (Exception e5) {
                                        FileLog.e(e5);
                                    }
                                    try {
                                        file2 = new File(file5, "Telegram Video");
                                        file2.mkdir();
                                        if (file2.isDirectory()) {
                                            sparseArray.put(FileLoader.MEDIA_DIR_VIDEO_PUBLIC, file2);
                                            if (BuildVars.LOGS_ENABLED) {
                                            }
                                        }
                                    } catch (Exception e6) {
                                        FileLog.e(e6);
                                    }
                                }
                                SharedConfig.checkSaveToGalleryFiles();
                                return sparseArray;
                            }
                        } else {
                            file = null;
                        }
                    } catch (Exception e7) {
                        e = e7;
                        file = null;
                    }
                    this.telegramPath = new File(ApplicationLoader.applicationContext.getExternalFilesDir(null), "Telegram");
                    file5 = file;
                } else {
                    this.telegramPath = new File(externalStorageDirectory, "Telegram");
                }
                this.telegramPath.mkdirs();
                if (Build.VERSION.SDK_INT >= 19 && !this.telegramPath.isDirectory()) {
                    ArrayList<File> dataDirs2 = AndroidUtilities.getDataDirs();
                    size = dataDirs2.size();
                    i = 0;
                    while (true) {
                        if (i < size) {
                            break;
                        }
                        File file7 = dataDirs2.get(i);
                        if (file7.getAbsolutePath().startsWith(SharedConfig.storageCacheDir)) {
                            File file8 = new File(file7, "Telegram");
                            this.telegramPath = file8;
                            file8.mkdirs();
                            break;
                        }
                        i++;
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
                    } catch (Exception e8) {
                        FileLog.e(e8);
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
                    } catch (Exception e9) {
                        FileLog.e(e9);
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
                    } catch (Exception e10) {
                        FileLog.e(e10);
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
                    } catch (Exception e11) {
                        FileLog.e(e11);
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
                    } catch (Exception e12) {
                        FileLog.e(e12);
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
        } catch (Exception e13) {
            FileLog.e(e13);
        }
        return sparseArray;
    }

    private boolean canMoveFiles(File file, File file2, int i) {
        Throwable th;
        Exception e;
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
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e2) {
            e = e2;
        }
        try {
            randomAccessFile.write(bArr);
            randomAccessFile.close();
            boolean renameTo = file3.renameTo(file4);
            file3.delete();
            file4.delete();
            return renameTo;
        } catch (Exception e3) {
            e = e3;
            randomAccessFile2 = randomAccessFile;
            FileLog.e(e);
            if (randomAccessFile2 == null) {
                return false;
            }
            try {
                randomAccessFile2.close();
                return false;
            } catch (Exception e4) {
                FileLog.e(e4);
                return false;
            }
        } catch (Throwable th3) {
            th = th3;
            randomAccessFile2 = randomAccessFile;
            if (randomAccessFile2 != null) {
                try {
                    randomAccessFile2.close();
                } catch (Exception e5) {
                    FileLog.e(e5);
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

    public /* synthetic */ void lambda$cancelLoadingForImageReceiver$3(boolean z, ImageReceiver imageReceiver) {
        int i = 0;
        while (true) {
            int i2 = 3;
            if (i < 3) {
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
            } else {
                return;
            }
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

    /* renamed from: replaceImageInCacheInternal */
    public void lambda$replaceImageInCache$4(String str, String str2, ImageLocation imageLocation) {
        ArrayList<String> arrayList;
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                arrayList = this.memCache.getFilterKeys(str);
            } else {
                arrayList = this.smallImagesMemCache.getFilterKeys(str);
            }
            if (arrayList != null) {
                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                    String str3 = arrayList.get(i2);
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
        if (this.thumbGenerateTasks.get(FileLoader.getAttachFileName(thumbGenerateInfo.parentDocument)) != null) {
            return;
        }
        this.thumbGeneratingQueue.postRunnable(new ThumbGenerateTask(i, file, thumbGenerateInfo));
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
        this.imageLoadQueue.postRunnable(runnable);
        imageReceiver.addLoadingImageRunnable(runnable);
    }

    /* JADX WARN: Code restructure failed: missing block: B:70:0x01a9, code lost:
        if (r9.exists() == false) goto L71;
     */
    /* JADX WARN: Removed duplicated region for block: B:126:0x02a5  */
    /* JADX WARN: Removed duplicated region for block: B:238:0x04e0  */
    /* JADX WARN: Removed duplicated region for block: B:242:0x04f6  */
    /* JADX WARN: Removed duplicated region for block: B:245:0x0521  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x0526  */
    /* JADX WARN: Removed duplicated region for block: B:251:0x055b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:300:0x0648  */
    /* JADX WARN: Removed duplicated region for block: B:301:0x0650  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x019e  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x01ae  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x01b0  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x01b3  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0204  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$createLoadOperationForImageReceiver$6(int i, String str, String str2, int i2, ImageReceiver imageReceiver, int i3, String str3, int i4, ImageLocation imageLocation, boolean z, Object obj, int i5, TLRPC$Document tLRPC$Document, boolean z2, boolean z3, String str4, int i6, long j) {
        boolean z4;
        String str5;
        int i7;
        File file;
        String str6;
        int i8;
        boolean z5;
        boolean z6;
        ImageLoader imageLoader;
        boolean z7;
        int i9;
        int i10;
        boolean z8;
        String str7;
        int i11;
        boolean z9;
        long j2;
        File file2;
        File file3;
        String str8;
        boolean z10;
        int i12;
        boolean z11;
        boolean z12;
        int i13;
        String str9;
        File file4;
        int i14;
        boolean z13;
        File file5;
        File file6;
        File file7;
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
                    cacheImage2 = cacheImage3;
                    cacheImage = cacheImage4;
                    z4 = true;
                } else if (cacheImage5 == cacheImage3) {
                    cacheImage2 = cacheImage3;
                    if (cacheImage4 == null) {
                        cacheImage = cacheImage4;
                        cacheImage5.replaceImageReceiver(imageReceiver, str2, str3, i4, i3);
                    } else {
                        cacheImage = cacheImage4;
                    }
                    z4 = true;
                } else {
                    cacheImage2 = cacheImage3;
                    cacheImage = cacheImage4;
                    cacheImage5.removeImageReceiver(imageReceiver);
                }
                if (z4 && cacheImage != null) {
                    cacheImage.addImageReceiver(imageReceiver, str2, str3, i4, i3);
                    z4 = true;
                }
                if (!z4 && cacheImage2 != null) {
                    cacheImage2.addImageReceiver(imageReceiver, str2, str3, i4, i3);
                    z4 = true;
                }
            } else {
                cacheImage2 = cacheImage3;
                cacheImage = cacheImage4;
            }
            z4 = false;
            if (z4) {
            }
            if (!z4) {
                cacheImage2.addImageReceiver(imageReceiver, str2, str3, i4, i3);
                z4 = true;
            }
        } else {
            z4 = false;
        }
        if (!z4) {
            String str10 = imageLocation.path;
            if (str10 != null) {
                if (str10.startsWith("http") || str10.startsWith("athumb")) {
                    z6 = false;
                    file = null;
                } else if (str10.startsWith("thumb://")) {
                    int indexOf = str10.indexOf(":", 8);
                    if (indexOf >= 0) {
                        file = new File(str10.substring(indexOf + 1));
                        z6 = true;
                    }
                    file = null;
                    z6 = true;
                } else {
                    if (str10.startsWith("vthumb://")) {
                        int indexOf2 = str10.indexOf(":", 9);
                        if (indexOf2 >= 0) {
                            file = new File(str10.substring(indexOf2 + 1));
                        }
                        file = null;
                    } else {
                        file = new File(str10);
                    }
                    z6 = true;
                }
                str5 = "athumb";
                z5 = false;
                i8 = 2;
                i7 = 1;
                str6 = str3;
            } else {
                if (i != 0 || !z) {
                    str5 = "athumb";
                    i7 = 1;
                    str6 = str3;
                    z6 = false;
                } else {
                    if (obj instanceof MessageObject) {
                        MessageObject messageObject = (MessageObject) obj;
                        TLRPC$Document document = messageObject.getDocument();
                        String str11 = messageObject.messageOwner.attachPath;
                        file4 = FileLoader.getInstance(i5).getPathToMessage(messageObject.messageOwner);
                        str9 = str11;
                        tLRPC$Document2 = document;
                        i14 = messageObject.getMediaType();
                        z13 = false;
                    } else if (tLRPC$Document2 != null) {
                        File pathToAttach = FileLoader.getInstance(i5).getPathToAttach(tLRPC$Document2, true);
                        i14 = MessageObject.isVideoDocument(tLRPC$Document) ? 2 : 3;
                        file4 = pathToAttach;
                        str9 = null;
                        z13 = true;
                    } else {
                        str9 = null;
                        z13 = false;
                        i14 = 0;
                        file4 = null;
                        tLRPC$Document2 = null;
                    }
                    if (tLRPC$Document2 != null) {
                        if (z2) {
                            File directory = FileLoader.getDirectory(4);
                            StringBuilder sb = new StringBuilder();
                            str5 = "athumb";
                            sb.append("q_");
                            sb.append(tLRPC$Document2.dc_id);
                            sb.append("_");
                            file5 = file4;
                            sb.append(tLRPC$Document2.id);
                            sb.append(".jpg");
                            File file8 = new File(directory, sb.toString());
                            if (file8.exists()) {
                                file6 = file8;
                                z5 = true;
                                if (!TextUtils.isEmpty(str9)) {
                                    file7 = new File(str9);
                                }
                                file7 = null;
                                File file9 = file7 != null ? file5 : file7;
                                if (file6 != null) {
                                    String attachFileName = FileLoader.getAttachFileName(tLRPC$Document2);
                                    ThumbGenerateInfo thumbGenerateInfo = this.waitingForQualityThumb.get(attachFileName);
                                    if (thumbGenerateInfo == null) {
                                        thumbGenerateInfo = new ThumbGenerateInfo();
                                        thumbGenerateInfo.parentDocument = tLRPC$Document2;
                                        thumbGenerateInfo.filter = str3;
                                        thumbGenerateInfo.big = z13;
                                        this.waitingForQualityThumb.put(attachFileName, thumbGenerateInfo);
                                    }
                                    if (!thumbGenerateInfo.imageReceiverArray.contains(imageReceiver)) {
                                        thumbGenerateInfo.imageReceiverArray.add(imageReceiver);
                                        thumbGenerateInfo.imageReceiverGuidsArray.add(Integer.valueOf(i3));
                                    }
                                    this.waitingForQualityThumbByTag.put(i2, attachFileName);
                                    if (!file9.exists() || !z3) {
                                        return;
                                    }
                                    generateThumb(i14, file9, thumbGenerateInfo);
                                    return;
                                }
                                str6 = str3;
                                i7 = 1;
                                file = file6;
                                z6 = true;
                                i8 = 2;
                            }
                        } else {
                            str5 = "athumb";
                            file5 = file4;
                        }
                        z5 = false;
                        file6 = null;
                        if (!TextUtils.isEmpty(str9)) {
                        }
                        file7 = null;
                        if (file7 != null) {
                        }
                        if (file6 != null) {
                        }
                    } else {
                        str5 = "athumb";
                        i7 = 1;
                        str6 = str3;
                        z6 = true;
                    }
                }
                z5 = false;
                i8 = 2;
                file = null;
            }
            if (i != i8) {
                boolean isEncrypted = imageLocation.isEncrypted();
                CacheImage cacheImage6 = new CacheImage();
                if (!z) {
                    if (imageLocation.imageType != i8) {
                        if (MessageObject.isGifDocument(imageLocation.webFile) || MessageObject.isGifDocument(imageLocation.document) || MessageObject.isRoundVideoDocument(imageLocation.document) || MessageObject.isVideoSticker(imageLocation.document)) {
                            i8 = 2;
                        } else {
                            String str12 = imageLocation.path;
                            if (str12 != null && !str12.startsWith("vthumb") && !str12.startsWith("thumb")) {
                                String httpUrlExtension = getHttpUrlExtension(str12, "jpg");
                                if (httpUrlExtension.equals("webm") || httpUrlExtension.equals("mp4") || httpUrlExtension.equals("gif")) {
                                    cacheImage6.imageType = 2;
                                } else if ("tgs".equals(str4)) {
                                    cacheImage6.imageType = i7;
                                }
                                if (file == null) {
                                    TLRPC$PhotoSize tLRPC$PhotoSize = imageLocation.photoSize;
                                    if ((tLRPC$PhotoSize instanceof TLRPC$TL_photoStrippedSize) || (tLRPC$PhotoSize instanceof TLRPC$TL_photoPathSize)) {
                                        i10 = i6;
                                        imageLoader = this;
                                        str7 = str;
                                        z5 = z5;
                                        j2 = 0;
                                    } else {
                                        SecureDocument secureDocument = imageLocation.secureDocument;
                                        if (secureDocument != null) {
                                            cacheImage6.secureDocument = secureDocument;
                                            z9 = secureDocument.secureFile.dc_id == Integer.MIN_VALUE;
                                            file = new File(FileLoader.getDirectory(4), str);
                                            i10 = i6;
                                            imageLoader = this;
                                            str7 = str;
                                            j2 = 0;
                                        } else {
                                            boolean z14 = z6;
                                            boolean z15 = z5;
                                            if (hasAutoplayFilter(str3) || isAnimatedAvatar(str6)) {
                                                i10 = i6;
                                            } else {
                                                i10 = i6;
                                                if (i10 != 0 || j <= 0 || imageLocation.path != null || isEncrypted) {
                                                    File file10 = new File(FileLoader.getDirectory(4), str);
                                                    if (file10.exists()) {
                                                        z11 = true;
                                                    } else {
                                                        if (i10 == 2) {
                                                            file10 = new File(FileLoader.getDirectory(4), str + ".enc");
                                                        }
                                                        z11 = z15;
                                                    }
                                                    TLRPC$Document tLRPC$Document3 = imageLocation.document;
                                                    if (tLRPC$Document3 != null) {
                                                        if (tLRPC$Document3 instanceof DocumentObject.ThemeDocument) {
                                                            if (((DocumentObject.ThemeDocument) tLRPC$Document3).wallpaper == null) {
                                                                i13 = 5;
                                                                z12 = true;
                                                            } else {
                                                                z12 = z14;
                                                                i13 = 5;
                                                            }
                                                            cacheImage6.imageType = i13;
                                                            str7 = str;
                                                            file = file10;
                                                            z5 = z11;
                                                            z9 = z12;
                                                            j2 = 0;
                                                            imageLoader = this;
                                                        } else if ("application/x-tgsdice".equals(tLRPC$Document3.mime_type)) {
                                                            cacheImage6.imageType = 1;
                                                            imageLoader = this;
                                                            str7 = str;
                                                            file = file10;
                                                            z5 = z11;
                                                            j2 = 0;
                                                        } else if ("application/x-tgsticker".equals(imageLocation.document.mime_type)) {
                                                            cacheImage6.imageType = 1;
                                                        } else if ("application/x-tgwallpattern".equals(imageLocation.document.mime_type)) {
                                                            cacheImage6.imageType = 3;
                                                        } else if (FileLoader.getDocumentFileName(imageLocation.document).endsWith(".svg")) {
                                                            cacheImage6.imageType = 3;
                                                        }
                                                    }
                                                    imageLoader = this;
                                                    str7 = str;
                                                    file = file10;
                                                    z5 = z11;
                                                    j2 = 0;
                                                    z9 = z14;
                                                    if (!hasAutoplayFilter(str3) || imageLoader.isAnimatedAvatar(str6)) {
                                                        cacheImage6.imageType = 2;
                                                        cacheImage6.size = j2;
                                                        i9 = i4;
                                                        z8 = z5;
                                                        z7 = true;
                                                    } else {
                                                        i9 = i4;
                                                        z7 = z9;
                                                        z8 = z5;
                                                    }
                                                }
                                            }
                                            TLRPC$Document tLRPC$Document4 = imageLocation.document;
                                            if (tLRPC$Document4 != null) {
                                                if (tLRPC$Document4 instanceof TLRPC$TL_documentEncrypted) {
                                                    file3 = new File(FileLoader.getDirectory(4), str);
                                                } else if (MessageObject.isVideoDocument(tLRPC$Document4)) {
                                                    file3 = new File(FileLoader.getDirectory(2), str);
                                                } else {
                                                    file3 = new File(FileLoader.getDirectory(3), str);
                                                }
                                                if ((isAnimatedAvatar(str6) || hasAutoplayFilter(str3)) && !file3.exists()) {
                                                    File directory2 = FileLoader.getDirectory(4);
                                                    StringBuilder sb2 = new StringBuilder();
                                                    sb2.append(tLRPC$Document4.dc_id);
                                                    sb2.append("_");
                                                    str8 = "application/x-tgsticker";
                                                    sb2.append(tLRPC$Document4.id);
                                                    sb2.append(".temp");
                                                    file3 = new File(directory2, sb2.toString());
                                                } else {
                                                    str8 = "application/x-tgsticker";
                                                }
                                                if (tLRPC$Document4 instanceof DocumentObject.ThemeDocument) {
                                                    if (((DocumentObject.ThemeDocument) tLRPC$Document4).wallpaper == null) {
                                                        i12 = 5;
                                                        z10 = true;
                                                    } else {
                                                        z10 = z14;
                                                        i12 = 5;
                                                    }
                                                    cacheImage6.imageType = i12;
                                                } else if ("application/x-tgsdice".equals(imageLocation.document.mime_type)) {
                                                    cacheImage6.imageType = 1;
                                                    z10 = true;
                                                } else {
                                                    if (str8.equals(tLRPC$Document4.mime_type)) {
                                                        cacheImage6.imageType = 1;
                                                    } else if ("application/x-tgwallpattern".equals(tLRPC$Document4.mime_type)) {
                                                        cacheImage6.imageType = 3;
                                                    } else if (FileLoader.getDocumentFileName(imageLocation.document).endsWith(".svg")) {
                                                        cacheImage6.imageType = 3;
                                                    }
                                                    z10 = z14;
                                                }
                                                z5 = z15;
                                                j2 = tLRPC$Document4.size;
                                                z9 = z10;
                                                file = file3;
                                                imageLoader = this;
                                                str7 = str;
                                            } else {
                                                if (imageLocation.webFile != null) {
                                                    str7 = str;
                                                    file = new File(FileLoader.getDirectory(3), str7);
                                                    imageLoader = this;
                                                } else {
                                                    str7 = str;
                                                    if (i10 == 1) {
                                                        file2 = new File(FileLoader.getDirectory(4), str7);
                                                    } else {
                                                        file2 = new File(FileLoader.getDirectory(0), str7);
                                                    }
                                                    imageLoader = this;
                                                    file = file2;
                                                    if (imageLoader.isAnimatedAvatar(str6) || (hasAutoplayFilter(str3) && imageLocation.location != null && !file.exists())) {
                                                        file = new File(FileLoader.getDirectory(4), imageLocation.location.volume_id + "_" + imageLocation.location.local_id + ".temp");
                                                    }
                                                }
                                                z9 = z14;
                                                z5 = z15;
                                                j2 = 0;
                                            }
                                            if (!hasAutoplayFilter(str3)) {
                                            }
                                            cacheImage6.imageType = 2;
                                            cacheImage6.size = j2;
                                            i9 = i4;
                                            z8 = z5;
                                            z7 = true;
                                        }
                                        if (!hasAutoplayFilter(str3)) {
                                        }
                                        cacheImage6.imageType = 2;
                                        cacheImage6.size = j2;
                                        i9 = i4;
                                        z8 = z5;
                                        z7 = true;
                                    }
                                    z9 = true;
                                    if (!hasAutoplayFilter(str3)) {
                                    }
                                    cacheImage6.imageType = 2;
                                    cacheImage6.size = j2;
                                    i9 = i4;
                                    z8 = z5;
                                    z7 = true;
                                } else {
                                    i10 = i6;
                                    imageLoader = this;
                                    str7 = str;
                                    z7 = z6;
                                    i9 = i4;
                                    z8 = z5;
                                }
                                cacheImage6.type = i9;
                                cacheImage6.key = str2;
                                cacheImage6.filter = str6;
                                cacheImage6.imageLocation = imageLocation;
                                cacheImage6.ext = str4;
                                cacheImage6.currentAccount = i5;
                                cacheImage6.parentObject = obj;
                                i11 = imageLocation.imageType;
                                if (i11 != 0) {
                                    cacheImage6.imageType = i11;
                                }
                                if (i10 == 2) {
                                    cacheImage6.encryptionKeyPath = new File(FileLoader.getInternalCacheDir(), str7 + ".enc.key");
                                }
                                String str13 = str5;
                                cacheImage6.addImageReceiver(imageReceiver, str2, str3, i4, i3);
                                if (!z7 || z8 || file.exists()) {
                                    cacheImage6.finalFilePath = file;
                                    cacheImage6.imageLocation = imageLocation;
                                    cacheImage6.cacheTask = new CacheOutTask(cacheImage6);
                                    imageLoader.imageLoadingByKeys.put(str2, cacheImage6);
                                    if (i != 0) {
                                        imageLoader.cacheThumbOutQueue.postRunnable(cacheImage6.cacheTask);
                                        return;
                                    } else {
                                        imageLoader.cacheOutQueue.postRunnable(cacheImage6.cacheTask);
                                        return;
                                    }
                                }
                                cacheImage6.url = str7;
                                imageLoader.imageLoadingByUrl.put(str7, cacheImage6);
                                String str14 = imageLocation.path;
                                if (str14 != null) {
                                    String MD5 = Utilities.MD5(str14);
                                    cacheImage6.tempFilePath = new File(FileLoader.getDirectory(4), MD5 + "_temp.jpg");
                                    cacheImage6.finalFilePath = file;
                                    if (imageLocation.path.startsWith(str13)) {
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
                                if (imageLocation.location != null) {
                                    FileLoader.getInstance(i5).loadFile(imageLocation, obj, str4, i != 0 ? 2 : 1, (i6 != 0 || (j > 0 && imageLocation.key == null)) ? i6 : 1);
                                } else if (imageLocation.document != null) {
                                    FileLoader.getInstance(i5).loadFile(imageLocation.document, obj, i != 0 ? 2 : 1, i6);
                                } else if (imageLocation.secureDocument != null) {
                                    FileLoader.getInstance(i5).loadFile(imageLocation.secureDocument, i != 0 ? 2 : 1);
                                } else if (imageLocation.webFile != null) {
                                    FileLoader.getInstance(i5).loadFile(imageLocation.webFile, i != 0 ? 2 : 1, i6);
                                }
                                if (!imageReceiver.isForceLoding()) {
                                    return;
                                }
                                imageLoader.forceLoadingImages.put(cacheImage6.key, 0);
                                return;
                            }
                        }
                    }
                    cacheImage6.imageType = i8;
                    if (file == null) {
                    }
                    cacheImage6.type = i9;
                    cacheImage6.key = str2;
                    cacheImage6.filter = str6;
                    cacheImage6.imageLocation = imageLocation;
                    cacheImage6.ext = str4;
                    cacheImage6.currentAccount = i5;
                    cacheImage6.parentObject = obj;
                    i11 = imageLocation.imageType;
                    if (i11 != 0) {
                    }
                    if (i10 == 2) {
                    }
                    String str132 = str5;
                    cacheImage6.addImageReceiver(imageReceiver, str2, str3, i4, i3);
                    if (!z7) {
                    }
                    cacheImage6.finalFilePath = file;
                    cacheImage6.imageLocation = imageLocation;
                    cacheImage6.cacheTask = new CacheOutTask(cacheImage6);
                    imageLoader.imageLoadingByKeys.put(str2, cacheImage6);
                    if (i != 0) {
                    }
                }
                if (file == null) {
                }
                cacheImage6.type = i9;
                cacheImage6.key = str2;
                cacheImage6.filter = str6;
                cacheImage6.imageLocation = imageLocation;
                cacheImage6.ext = str4;
                cacheImage6.currentAccount = i5;
                cacheImage6.parentObject = obj;
                i11 = imageLocation.imageType;
                if (i11 != 0) {
                }
                if (i10 == 2) {
                }
                String str1322 = str5;
                cacheImage6.addImageReceiver(imageReceiver, str2, str3, i4, i3);
                if (!z7) {
                }
                cacheImage6.finalFilePath = file;
                cacheImage6.imageLocation = imageLocation;
                cacheImage6.cacheTask = new CacheOutTask(cacheImage6);
                imageLoader.imageLoadingByKeys.put(str2, cacheImage6);
                if (i != 0) {
                }
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

    /* JADX WARN: Code restructure failed: missing block: B:146:0x0258, code lost:
        if (r6.local_id < 0) goto L148;
     */
    /* JADX WARN: Removed duplicated region for block: B:104:0x019a  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x019d  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x01a0  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x01a4  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x01b9  */
    /* JADX WARN: Removed duplicated region for block: B:195:0x0377  */
    /* JADX WARN: Removed duplicated region for block: B:212:0x03e2  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x03ea A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:219:0x0405 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:223:0x0420 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:228:0x0441 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:233:0x045f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:237:0x0479  */
    /* JADX WARN: Removed duplicated region for block: B:245:0x04b7  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x04bc  */
    /* JADX WARN: Removed duplicated region for block: B:261:0x0524  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x007f  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0084  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0087  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00ba  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00d9  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0156  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0166  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0173  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0181  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x018b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void loadImageForImageReceiver(ImageReceiver imageReceiver) {
        boolean z;
        String imageKey;
        boolean z2;
        String thumbKey;
        boolean z3;
        Object parentObject;
        ImageLocation thumbLocation;
        ImageLocation mediaLocation;
        ImageLocation imageLocation;
        boolean z4;
        ImageLocation imageLocation2;
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
        ImageLocation imageLocation5;
        String str15;
        String str16;
        int i2;
        String str17;
        ImageLocation imageLocation6;
        String str18;
        String str19;
        String str20;
        boolean z5;
        Object obj;
        Object obj2;
        BitmapDrawable bitmapDrawable;
        BitmapDrawable fromLottieCache;
        BitmapDrawable bitmapDrawable2;
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
            if (bitmapDrawable3 != null) {
                cancelLoadingForImageReceiver(imageReceiver, true);
                imageReceiver.setImageBitmapByKey(bitmapDrawable3, mediaKey, 3, true, newGuid);
                if (!imageReceiver.isForcePreview()) {
                    return;
                }
                z = true;
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
                        if (!imageReceiver.isForcePreview() && mediaKey == null) {
                            return;
                        }
                        z2 = true;
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
                                if (z2 && imageReceiver.isForcePreview()) {
                                    return;
                                }
                                z3 = true;
                                parentObject = imageReceiver.getParentObject();
                                TLRPC$Document qualityThumbDocument = imageReceiver.getQualityThumbDocument();
                                thumbLocation = imageReceiver.getThumbLocation();
                                String thumbFilter = imageReceiver.getThumbFilter();
                                mediaLocation = imageReceiver.getMediaLocation();
                                String mediaFilter = imageReceiver.getMediaFilter();
                                imageLocation = imageReceiver.getImageLocation();
                                String imageFilter = imageReceiver.getImageFilter();
                                if (imageLocation == null && imageReceiver.isNeedsQualityThumb() && imageReceiver.isCurrentKeyQuality()) {
                                    if (!(parentObject instanceof MessageObject)) {
                                        imageLocation2 = ImageLocation.getForDocument(((MessageObject) parentObject).getDocument());
                                    } else if (qualityThumbDocument != null) {
                                        imageLocation2 = ImageLocation.getForDocument(qualityThumbDocument);
                                    }
                                    z4 = true;
                                    str = "mp4";
                                    String str21 = (imageLocation2 == null && imageLocation2.imageType == 2) ? str : null;
                                    if (mediaLocation != null || mediaLocation.imageType != 2) {
                                        str = null;
                                    }
                                    ext = imageReceiver.getExt();
                                    if (ext == null) {
                                        ext = "jpg";
                                    }
                                    String str22 = str21 != null ? ext : str21;
                                    if (str == null) {
                                        str = ext;
                                    }
                                    ImageLocation imageLocation7 = imageLocation2;
                                    imageLocation3 = mediaLocation;
                                    ImageLocation imageLocation8 = imageLocation;
                                    i = 0;
                                    str2 = null;
                                    str3 = null;
                                    String str23 = null;
                                    String str24 = null;
                                    boolean z6 = false;
                                    while (i < 2) {
                                        if (i == 0) {
                                            imageLocation6 = imageLocation7;
                                            i2 = newGuid;
                                            str17 = str22;
                                        } else {
                                            i2 = newGuid;
                                            str17 = str;
                                            imageLocation6 = imageLocation3;
                                        }
                                        if (imageLocation6 == null) {
                                            z5 = z3;
                                            obj = imageLocation7;
                                        } else {
                                            z5 = z3;
                                            if (imageLocation3 != null) {
                                                obj = imageLocation7;
                                                obj2 = imageLocation3;
                                            } else {
                                                obj2 = imageLocation7;
                                                obj = obj2;
                                            }
                                            String key = imageLocation6.getKey(parentObject, obj2, false);
                                            if (key != null) {
                                                str20 = thumbFilter;
                                                String key2 = imageLocation6.getKey(parentObject, imageLocation3 != null ? imageLocation3 : obj, true);
                                                if (imageLocation6.path != null) {
                                                    key2 = key2 + "." + getHttpUrlExtension(imageLocation6.path, "jpg");
                                                    str19 = imageFilter;
                                                    str18 = mediaFilter;
                                                } else {
                                                    TLRPC$PhotoSize tLRPC$PhotoSize = imageLocation6.photoSize;
                                                    str19 = imageFilter;
                                                    if ((tLRPC$PhotoSize instanceof TLRPC$TL_photoStrippedSize) || (tLRPC$PhotoSize instanceof TLRPC$TL_photoPathSize)) {
                                                        str18 = mediaFilter;
                                                        key2 = key2 + "." + str17;
                                                    } else if (imageLocation6.location != null) {
                                                        key2 = key2 + "." + str17;
                                                        if (imageReceiver.getExt() == null) {
                                                            TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated = imageLocation6.location;
                                                            if (tLRPC$TL_fileLocationToBeDeprecated.key == null) {
                                                                str18 = mediaFilter;
                                                                if (tLRPC$TL_fileLocationToBeDeprecated.volume_id == -2147483648L) {
                                                                }
                                                            }
                                                        }
                                                        str18 = mediaFilter;
                                                        z6 = true;
                                                    } else {
                                                        str18 = mediaFilter;
                                                        WebFile webFile = imageLocation6.webFile;
                                                        if (webFile != null) {
                                                            String mimeTypePart = FileLoader.getMimeTypePart(webFile.mime_type);
                                                            key2 = key2 + "." + getHttpUrlExtension(imageLocation6.webFile.url, mimeTypePart);
                                                        } else if (imageLocation6.secureDocument != null) {
                                                            key2 = key2 + "." + str17;
                                                        } else if (imageLocation6.document != null) {
                                                            if (i == 0 && z4) {
                                                                key = "q_" + key;
                                                            }
                                                            String documentFileName = FileLoader.getDocumentFileName(imageLocation6.document);
                                                            int lastIndexOf = documentFileName.lastIndexOf(46);
                                                            String str25 = "";
                                                            String substring = lastIndexOf == -1 ? str25 : documentFileName.substring(lastIndexOf);
                                                            if (substring.length() > 1) {
                                                                str25 = substring;
                                                            } else if ("video/mp4".equals(imageLocation6.document.mime_type)) {
                                                                str25 = ".mp4";
                                                            } else if ("video/x-matroska".equals(imageLocation6.document.mime_type)) {
                                                                str25 = ".mkv";
                                                            }
                                                            key2 = key2 + str25;
                                                            z6 = !MessageObject.isVideoDocument(imageLocation6.document) && !MessageObject.isGifDocument(imageLocation6.document) && !MessageObject.isRoundVideoDocument(imageLocation6.document) && !MessageObject.canPreviewDocument(imageLocation6.document);
                                                        }
                                                    }
                                                }
                                                if (i == 0) {
                                                    str3 = key;
                                                    str23 = key2;
                                                } else {
                                                    str2 = key;
                                                    str24 = key2;
                                                }
                                                if (imageLocation6 == thumbLocation) {
                                                    if (i == 0) {
                                                        str3 = null;
                                                        imageLocation7 = null;
                                                        str23 = null;
                                                    } else {
                                                        imageLocation7 = obj;
                                                        str2 = null;
                                                        str24 = null;
                                                        imageLocation3 = null;
                                                    }
                                                    i++;
                                                    newGuid = i2;
                                                    z3 = z5;
                                                    thumbFilter = str20;
                                                    imageFilter = str19;
                                                    mediaFilter = str18;
                                                }
                                                imageLocation7 = obj;
                                                i++;
                                                newGuid = i2;
                                                z3 = z5;
                                                thumbFilter = str20;
                                                imageFilter = str19;
                                                mediaFilter = str18;
                                            }
                                        }
                                        str20 = thumbFilter;
                                        str19 = imageFilter;
                                        str18 = mediaFilter;
                                        imageLocation7 = obj;
                                        i++;
                                        newGuid = i2;
                                        z3 = z5;
                                        thumbFilter = str20;
                                        imageFilter = str19;
                                        mediaFilter = str18;
                                    }
                                    boolean z7 = z3;
                                    str4 = thumbFilter;
                                    imageLocation4 = imageLocation7;
                                    str5 = imageFilter;
                                    str6 = mediaFilter;
                                    int i3 = newGuid;
                                    if (thumbLocation == null) {
                                        ImageLocation strippedLocation = imageReceiver.getStrippedLocation();
                                        if (strippedLocation == null) {
                                            if (imageLocation3 != null) {
                                                imageLocation8 = imageLocation3;
                                            }
                                            strippedLocation = imageLocation8;
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
                                        str7 = key4;
                                        str8 = key3;
                                    } else {
                                        str8 = null;
                                        str7 = null;
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
                                    if (str8 != null || str4 == null) {
                                        str11 = str4;
                                        str12 = str8;
                                    } else {
                                        StringBuilder sb3 = new StringBuilder();
                                        sb3.append(str8);
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
                                    if (imageLocation4 != null) {
                                        str15 = str10;
                                        imageLocation5 = imageLocation4;
                                    } else if (imageLocation4.path != null) {
                                        createLoadOperationForImageReceiver(imageReceiver, str12, str7, ext, thumbLocation, str11, 0L, 1, 1, z7 ? 2 : 1, i3);
                                        createLoadOperationForImageReceiver(imageReceiver, str13, str23, str22, imageLocation4, str10, imageReceiver.getSize(), 1, 0, 0, i3);
                                        return;
                                    } else {
                                        imageLocation5 = imageLocation4;
                                        str15 = str10;
                                    }
                                    if (imageLocation3 == null) {
                                        int cacheType = imageReceiver.getCacheType();
                                        int i4 = (cacheType != 0 || !z6) ? cacheType : 1;
                                        int i5 = i4 == 0 ? 1 : i4;
                                        if (!z7) {
                                            str16 = str9;
                                            createLoadOperationForImageReceiver(imageReceiver, str12, str7, ext, thumbLocation, str11, 0L, i5, 1, 1, i3);
                                        } else {
                                            str16 = str9;
                                        }
                                        if (!z2) {
                                            createLoadOperationForImageReceiver(imageReceiver, str13, str23, str22, imageLocation5, str15, 0L, 1, 0, 0, i3);
                                        }
                                        createLoadOperationForImageReceiver(imageReceiver, str14, str24, str, imageLocation3, str16, imageReceiver.getSize(), i4, 3, 0, i3);
                                        return;
                                    }
                                    int cacheType2 = imageReceiver.getCacheType();
                                    int i6 = (cacheType2 != 0 || !z6) ? cacheType2 : 1;
                                    createLoadOperationForImageReceiver(imageReceiver, str12, str7, ext, thumbLocation, str11, 0L, i6 == 0 ? 1 : i6, 1, z7 ? 2 : 1, i3);
                                    createLoadOperationForImageReceiver(imageReceiver, str13, str23, str22, imageLocation5, str15, imageReceiver.getSize(), i6, 0, 0, i3);
                                    return;
                                }
                                imageLocation2 = imageLocation;
                                z4 = false;
                                str = "mp4";
                                if (imageLocation2 == null) {
                                }
                                if (mediaLocation != null) {
                                }
                                str = null;
                                ext = imageReceiver.getExt();
                                if (ext == null) {
                                }
                                if (str21 != null) {
                                }
                                if (str == null) {
                                }
                                ImageLocation imageLocation72 = imageLocation2;
                                imageLocation3 = mediaLocation;
                                ImageLocation imageLocation82 = imageLocation;
                                i = 0;
                                str2 = null;
                                str3 = null;
                                String str232 = null;
                                String str242 = null;
                                boolean z62 = false;
                                while (i < 2) {
                                }
                                boolean z72 = z3;
                                str4 = thumbFilter;
                                imageLocation4 = imageLocation72;
                                str5 = imageFilter;
                                str6 = mediaFilter;
                                int i32 = newGuid;
                                if (thumbLocation == null) {
                                }
                                if (str2 != null) {
                                }
                                str9 = str6;
                                if (str3 != null) {
                                }
                                str10 = str5;
                                if (str8 != null) {
                                }
                                str11 = str4;
                                str12 = str8;
                                if (imageReceiver.getUniqKeyPrefix() != null) {
                                }
                                str13 = str3;
                                if (imageReceiver.getUniqKeyPrefix() != null) {
                                }
                                str14 = str2;
                                if (imageLocation4 != null) {
                                }
                                if (imageLocation3 == null) {
                                }
                            }
                        }
                        z3 = false;
                        parentObject = imageReceiver.getParentObject();
                        TLRPC$Document qualityThumbDocument2 = imageReceiver.getQualityThumbDocument();
                        thumbLocation = imageReceiver.getThumbLocation();
                        String thumbFilter2 = imageReceiver.getThumbFilter();
                        mediaLocation = imageReceiver.getMediaLocation();
                        String mediaFilter2 = imageReceiver.getMediaFilter();
                        imageLocation = imageReceiver.getImageLocation();
                        String imageFilter2 = imageReceiver.getImageFilter();
                        if (imageLocation == null) {
                            if (!(parentObject instanceof MessageObject)) {
                            }
                            z4 = true;
                            str = "mp4";
                            if (imageLocation2 == null) {
                            }
                            if (mediaLocation != null) {
                            }
                            str = null;
                            ext = imageReceiver.getExt();
                            if (ext == null) {
                            }
                            if (str21 != null) {
                            }
                            if (str == null) {
                            }
                            ImageLocation imageLocation722 = imageLocation2;
                            imageLocation3 = mediaLocation;
                            ImageLocation imageLocation822 = imageLocation;
                            i = 0;
                            str2 = null;
                            str3 = null;
                            String str2322 = null;
                            String str2422 = null;
                            boolean z622 = false;
                            while (i < 2) {
                            }
                            boolean z722 = z3;
                            str4 = thumbFilter2;
                            imageLocation4 = imageLocation722;
                            str5 = imageFilter2;
                            str6 = mediaFilter2;
                            int i322 = newGuid;
                            if (thumbLocation == null) {
                            }
                            if (str2 != null) {
                            }
                            str9 = str6;
                            if (str3 != null) {
                            }
                            str10 = str5;
                            if (str8 != null) {
                            }
                            str11 = str4;
                            str12 = str8;
                            if (imageReceiver.getUniqKeyPrefix() != null) {
                            }
                            str13 = str3;
                            if (imageReceiver.getUniqKeyPrefix() != null) {
                            }
                            str14 = str2;
                            if (imageLocation4 != null) {
                            }
                            if (imageLocation3 == null) {
                            }
                        }
                        imageLocation2 = imageLocation;
                        z4 = false;
                        str = "mp4";
                        if (imageLocation2 == null) {
                        }
                        if (mediaLocation != null) {
                        }
                        str = null;
                        ext = imageReceiver.getExt();
                        if (ext == null) {
                        }
                        if (str21 != null) {
                        }
                        if (str == null) {
                        }
                        ImageLocation imageLocation7222 = imageLocation2;
                        imageLocation3 = mediaLocation;
                        ImageLocation imageLocation8222 = imageLocation;
                        i = 0;
                        str2 = null;
                        str3 = null;
                        String str23222 = null;
                        String str24222 = null;
                        boolean z6222 = false;
                        while (i < 2) {
                        }
                        boolean z7222 = z3;
                        str4 = thumbFilter2;
                        imageLocation4 = imageLocation7222;
                        str5 = imageFilter2;
                        str6 = mediaFilter2;
                        int i3222 = newGuid;
                        if (thumbLocation == null) {
                        }
                        if (str2 != null) {
                        }
                        str9 = str6;
                        if (str3 != null) {
                        }
                        str10 = str5;
                        if (str8 != null) {
                        }
                        str11 = str4;
                        str12 = str8;
                        if (imageReceiver.getUniqKeyPrefix() != null) {
                        }
                        str13 = str3;
                        if (imageReceiver.getUniqKeyPrefix() != null) {
                        }
                        str14 = str2;
                        if (imageLocation4 != null) {
                        }
                        if (imageLocation3 == null) {
                        }
                    }
                }
                z2 = z;
                thumbKey = imageReceiver.getThumbKey();
                if (thumbKey != null) {
                }
                z3 = false;
                parentObject = imageReceiver.getParentObject();
                TLRPC$Document qualityThumbDocument22 = imageReceiver.getQualityThumbDocument();
                thumbLocation = imageReceiver.getThumbLocation();
                String thumbFilter22 = imageReceiver.getThumbFilter();
                mediaLocation = imageReceiver.getMediaLocation();
                String mediaFilter22 = imageReceiver.getMediaFilter();
                imageLocation = imageReceiver.getImageLocation();
                String imageFilter22 = imageReceiver.getImageFilter();
                if (imageLocation == null) {
                }
                imageLocation2 = imageLocation;
                z4 = false;
                str = "mp4";
                if (imageLocation2 == null) {
                }
                if (mediaLocation != null) {
                }
                str = null;
                ext = imageReceiver.getExt();
                if (ext == null) {
                }
                if (str21 != null) {
                }
                if (str == null) {
                }
                ImageLocation imageLocation72222 = imageLocation2;
                imageLocation3 = mediaLocation;
                ImageLocation imageLocation82222 = imageLocation;
                i = 0;
                str2 = null;
                str3 = null;
                String str232222 = null;
                String str242222 = null;
                boolean z62222 = false;
                while (i < 2) {
                }
                boolean z72222 = z3;
                str4 = thumbFilter22;
                imageLocation4 = imageLocation72222;
                str5 = imageFilter22;
                str6 = mediaFilter22;
                int i32222 = newGuid;
                if (thumbLocation == null) {
                }
                if (str2 != null) {
                }
                str9 = str6;
                if (str3 != null) {
                }
                str10 = str5;
                if (str8 != null) {
                }
                str11 = str4;
                str12 = str8;
                if (imageReceiver.getUniqKeyPrefix() != null) {
                }
                str13 = str3;
                if (imageReceiver.getUniqKeyPrefix() != null) {
                }
                str14 = str2;
                if (imageLocation4 != null) {
                }
                if (imageLocation3 == null) {
                }
            }
        }
        z = false;
        imageKey = imageReceiver.getImageKey();
        if (!z) {
            if (!useLottieMemCache(imageReceiver.getImageLocation(), imageKey)) {
            }
            if (fromLottieCache == null) {
            }
            if (fromLottieCache != null) {
            }
        }
        z2 = z;
        thumbKey = imageReceiver.getThumbKey();
        if (thumbKey != null) {
        }
        z3 = false;
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
        z4 = false;
        str = "mp4";
        if (imageLocation2 == null) {
        }
        if (mediaLocation != null) {
        }
        str = null;
        ext = imageReceiver.getExt();
        if (ext == null) {
        }
        if (str21 != null) {
        }
        if (str == null) {
        }
        ImageLocation imageLocation722222 = imageLocation2;
        imageLocation3 = mediaLocation;
        ImageLocation imageLocation822222 = imageLocation;
        i = 0;
        str2 = null;
        str3 = null;
        String str2322222 = null;
        String str2422222 = null;
        boolean z622222 = false;
        while (i < 2) {
        }
        boolean z722222 = z3;
        str4 = thumbFilter222;
        imageLocation4 = imageLocation722222;
        str5 = imageFilter222;
        str6 = mediaFilter222;
        int i322222 = newGuid;
        if (thumbLocation == null) {
        }
        if (str2 != null) {
        }
        str9 = str6;
        if (str3 != null) {
        }
        str10 = str5;
        if (str8 != null) {
        }
        str11 = str4;
        str12 = str8;
        if (imageReceiver.getUniqKeyPrefix() != null) {
        }
        str13 = str3;
        if (imageReceiver.getUniqKeyPrefix() != null) {
        }
        str14 = str2;
        if (imageLocation4 != null) {
        }
        if (imageLocation3 == null) {
        }
    }

    public BitmapDrawable getFromLottieCache(String str) {
        BitmapDrawable bitmapDrawable = this.lottieMemCache.get(str);
        if (!(bitmapDrawable instanceof AnimatedFileDrawable) || !((AnimatedFileDrawable) bitmapDrawable).isRecycled()) {
            return bitmapDrawable;
        }
        this.lottieMemCache.remove(str);
        return null;
    }

    private boolean useLottieMemCache(ImageLocation imageLocation, String str) {
        return (imageLocation != null && (MessageObject.isAnimatedStickerDocument(imageLocation.document, true) || imageLocation.imageType == 1 || MessageObject.isVideoSticker(imageLocation.document))) || isAnimatedAvatar(str);
    }

    public boolean hasLottieMemCache(String str) {
        LruCache<BitmapDrawable> lruCache = this.lottieMemCache;
        return lruCache != null && lruCache.contains(str);
    }

    public void httpFileLoadError(final String str) {
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.lambda$httpFileLoadError$8(str);
            }
        });
    }

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

    public void artworkLoadError(final String str) {
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.lambda$artworkLoadError$9(str);
            }
        });
    }

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

    public void fileDidLoaded(final String str, final File file, final int i) {
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.lambda$fileDidLoaded$10(str, i, file);
            }
        });
    }

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
            if (cacheOutTask.cacheImage.type == 1) {
                this.cacheThumbOutQueue.postRunnable(cacheOutTask);
            } else {
                this.cacheOutQueue.postRunnable(cacheOutTask);
            }
        }
    }

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

    public /* synthetic */ void lambda$fileDidFailedLoad$11(String str) {
        CacheImage cacheImage = this.imageLoadingByUrl.get(str);
        if (cacheImage != null) {
            cacheImage.setImageAndClear(null, null);
        }
    }

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

    public void runHttpFileLoadTasks(final HttpFileTask httpFileTask, final int i) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.lambda$runHttpFileLoadTasks$13(httpFileTask, i);
            }
        });
    }

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

    /* JADX WARN: Can't wrap try/catch for region: R(23:2|(2:7|(1:9)(2:10|(2:145|14)))|17|(1:19)(1:(19:130|21|26|(1:28)(1:29)|30|(1:32)|33|(3:35|(2:36|(1:38)(1:152))|39)|40|(1:42)(1:43)|44|(2:149|46)(1:(4:139|48|49|(1:51)))|52|128|(2:54|(6:56|(3:59|60|61)|58|69|(2:(1:72)|73)|(3:147|75|(4:77|(1:79)|80|(3:82|83|155)(1:154))(1:153))(2:133|(7:135|101|(5:150|103|(1:105)|106|(5:108|109|113|132|124))|112|113|132|124)(1:160)))(3:62|63|64))(3:65|66|67)|127|69|(0)|(0)(0)))|25|26|(0)(0)|30|(0)|33|(0)|40|(0)(0)|44|(0)(0)|52|128|(0)(0)|127|69|(0)|(0)(0)|(1:(0))) */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:133:0x0177 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:147:0x0116 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:149:0x00a7 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0072  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0077  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0081  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x008d  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x009e  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00a0  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00b1  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00d6  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x00f5 A[Catch: all -> 0x00dd, TRY_ENTER, TRY_LEAVE, TryCatch #12 {all -> 0x00dd, blocks: (B:46:0x00a7, B:59:0x00df, B:62:0x00ea, B:65:0x00f5), top: B:149:0x00a7 }] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0109  */
    /* JADX WARN: Type inference failed for: r15v10 */
    /* JADX WARN: Type inference failed for: r15v11 */
    /* JADX WARN: Type inference failed for: r15v18 */
    /* JADX WARN: Type inference failed for: r15v2 */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:115:0x01a7 -> B:132:0x01bd). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Bitmap loadBitmap(String str, Uri uri, float f, float f2, boolean z) {
        InputStream inputStream;
        float max;
        int i;
        int i2;
        float f3;
        Throwable th;
        Bitmap decodeStream;
        Bitmap createBitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        if (str == null && uri != null && uri.getScheme() != null) {
            if (uri.getScheme().contains("file")) {
                str = uri.getPath();
            } else if (Build.VERSION.SDK_INT < 30 || !RemoteMessageConst.Notification.CONTENT.equals(uri.getScheme())) {
                try {
                    str = AndroidUtilities.getPath(uri);
                } catch (Throwable th2) {
                    FileLog.e(th2);
                }
            }
        }
        Bitmap bitmap = null;
        if (str != null) {
            BitmapFactory.decodeFile(str, options);
        } else if (uri != null) {
            try {
                InputStream openInputStream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                BitmapFactory.decodeStream(openInputStream, null, options);
                openInputStream.close();
                inputStream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
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
                        InputStream openInputStream2 = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                        i = new ExifInterface(openInputStream2).getAttributeInt("Orientation", 1);
                        if (openInputStream2 != null) {
                            openInputStream2.close();
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
                            if (bitmap == null) {
                                return bitmap;
                            }
                            if (options.inPurgeable) {
                                Utilities.pinBitmap(bitmap);
                            }
                            Bitmap createBitmap2 = Bitmaps.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), i, true);
                            if (createBitmap2 == bitmap) {
                                return bitmap;
                            }
                            bitmap.recycle();
                            return createBitmap2;
                        } catch (Throwable th3) {
                            FileLog.e(th3);
                            getInstance().clearMemory();
                            if (bitmap == null) {
                                try {
                                    bitmap = BitmapFactory.decodeFile(str, options);
                                    if (bitmap != null && options.inPurgeable) {
                                        Utilities.pinBitmap(bitmap);
                                    }
                                } catch (Throwable th4) {
                                    FileLog.e(th4);
                                    return bitmap;
                                }
                            }
                            if (bitmap == null) {
                                return bitmap;
                            }
                            Bitmap createBitmap3 = Bitmaps.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), i, true);
                            if (createBitmap3 == bitmap) {
                                return bitmap;
                            }
                            bitmap.recycle();
                            return createBitmap3;
                        }
                    }
                    try {
                    } catch (Throwable th5) {
                        FileLog.e(th5);
                    }
                    if (uri == null) {
                        return null;
                    }
                    try {
                        decodeStream = BitmapFactory.decodeStream(inputStream, null, options);
                    } catch (Throwable th6) {
                        th = th6;
                    }
                    if (decodeStream != null) {
                        try {
                            if (options.inPurgeable) {
                                Utilities.pinBitmap(decodeStream);
                            }
                            createBitmap = Bitmaps.createBitmap(decodeStream, 0, 0, decodeStream.getWidth(), decodeStream.getHeight(), i, true);
                        } catch (Throwable th7) {
                            th = th7;
                            bitmap = decodeStream;
                            try {
                                FileLog.e(th);
                                inputStream.close();
                                return bitmap;
                            } catch (Throwable th8) {
                                try {
                                    inputStream.close();
                                } catch (Throwable th9) {
                                    FileLog.e(th9);
                                }
                                throw th8;
                            }
                        }
                        if (createBitmap != decodeStream) {
                            decodeStream.recycle();
                            bitmap = createBitmap;
                            inputStream.close();
                            return bitmap;
                        }
                    }
                    bitmap = decodeStream;
                    inputStream.close();
                    return bitmap;
                }
                f3 = max / options.inSampleSize;
                if (f3 > 1.0f) {
                }
                if (str != null) {
                }
            } catch (Throwable th10) {
                FileLog.e(th10);
                return null;
            }
        }
        inputStream = null;
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
            if (bArr != null && bArr.length != 0) {
                return;
            }
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(tLRPC$PhotoSize, true), "r");
                if (((int) randomAccessFile.length()) >= 20000) {
                    return;
                }
                byte[] bArr2 = new byte[(int) randomAccessFile.length()];
                tLRPC$PhotoSize.bytes = bArr2;
                randomAccessFile.readFully(bArr2, 0, bArr2.length);
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x009c  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00a1  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00bf  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00cf  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00e5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static TLRPC$PhotoSize scaleAndSaveImageInternal(TLRPC$PhotoSize tLRPC$PhotoSize, Bitmap bitmap, Bitmap.CompressFormat compressFormat, boolean z, int i, int i2, float f, float f2, float f3, int i3, boolean z2, boolean z3, boolean z4) throws Exception {
        TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated;
        File file;
        Bitmap createScaledBitmap = (f3 > 1.0f || z3) ? Bitmaps.createScaledBitmap(bitmap, i, i2, true) : bitmap;
        if (tLRPC$PhotoSize != null) {
            TLRPC$FileLocation tLRPC$FileLocation = tLRPC$PhotoSize.location;
            if (tLRPC$FileLocation instanceof TLRPC$TL_fileLocationToBeDeprecated) {
                tLRPC$TL_fileLocationToBeDeprecated = (TLRPC$TL_fileLocationToBeDeprecated) tLRPC$FileLocation;
                String str = tLRPC$TL_fileLocationToBeDeprecated.volume_id + "_" + tLRPC$TL_fileLocationToBeDeprecated.local_id + ".jpg";
                if (!z4) {
                    file = FileLoader.getDirectory(4);
                } else {
                    file = tLRPC$TL_fileLocationToBeDeprecated.volume_id != -2147483648L ? FileLoader.getDirectory(0) : FileLoader.getDirectory(4);
                }
                FileOutputStream fileOutputStream = new FileOutputStream(new File(file, str));
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
        FileOutputStream fileOutputStream2 = new FileOutputStream(new File(file, str2));
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
        boolean z4;
        float f3;
        int i4;
        int i5;
        float f4;
        if (bitmap == null) {
            return null;
        }
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        if (width != 0.0f && height != 0.0f) {
            float max = Math.max(width / f, height / f2);
            if (i2 != 0 && i3 != 0) {
                float f5 = i2;
                if (width < f5 || height < i3) {
                    if (width >= f5 || height <= i3) {
                        if (width > f5) {
                            float f6 = i3;
                            if (height < f6) {
                                f4 = height / f6;
                            }
                        }
                        f4 = Math.max(width / f5, height / i3);
                    } else {
                        f4 = width / f5;
                    }
                    f3 = f4;
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
            f3 = max;
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
        } else if (!(tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage)) {
        } else {
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
        TLRPC$Photo tLRPC$Photo;
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
        } else if (!(tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) || (tLRPC$Photo = tLRPC$MessageMedia.webpage.photo) == null) {
            return null;
        } else {
            int size3 = tLRPC$Photo.sizes.size();
            while (i < size3) {
                tLRPC$PhotoSize = tLRPC$Message.media.webpage.photo.sizes.get(i);
                if (!(tLRPC$PhotoSize instanceof TLRPC$TL_photoCachedSize)) {
                    i++;
                }
            }
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
                                    i2 = 0;
                                    i = 0;
                                    break;
                                } else if (tLRPC$Message.media.document.attributes.get(i4) instanceof TLRPC$TL_documentAttributeVideo) {
                                    TLRPC$TL_documentAttributeVideo tLRPC$TL_documentAttributeVideo = (TLRPC$TL_documentAttributeVideo) tLRPC$Message.media.document.attributes.get(i4);
                                    i = tLRPC$TL_documentAttributeVideo.h;
                                    i2 = tLRPC$TL_documentAttributeVideo.w;
                                    break;
                                } else {
                                    i4++;
                                }
                            }
                        } else {
                            i = closestPhotoSizeWithSize.h;
                            i2 = closestPhotoSizeWithSize.w;
                        }
                        org.telegram.ui.Components.Point messageSize2 = ChatMessageCell.getMessageSize(i2, i);
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

    public DispatchQueue getCacheOutQueue() {
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
