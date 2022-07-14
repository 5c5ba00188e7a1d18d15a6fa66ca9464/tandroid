package org.telegram.messenger;

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
import com.google.android.exoplayer2.util.MimeTypes;
import com.microsoft.appcenter.Constants;
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
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
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
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.AnimatedFileDrawable;
import org.telegram.ui.Components.Point;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.SlotsDrawable;
import org.telegram.ui.Components.ThemePreviewDrawable;
import org.telegram.ui.GroupCallActivity;
/* loaded from: classes4.dex */
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

    public void moveToFront(String key) {
        if (key == null) {
            return;
        }
        BitmapDrawable drawable = this.memCache.get(key);
        if (drawable != null) {
            this.memCache.moveToFront(key);
        }
        BitmapDrawable drawable2 = this.smallImagesMemCache.get(key);
        if (drawable2 != null) {
            this.smallImagesMemCache.moveToFront(key);
        }
    }

    public void putThumbsToCache(ArrayList<MessageThumb> updateMessageThumbs) {
        for (int i = 0; i < updateMessageThumbs.size(); i++) {
            putImageToCache(updateMessageThumbs.get(i).drawable, updateMessageThumbs.get(i).key, true);
        }
    }

    /* loaded from: classes4.dex */
    public static class ThumbGenerateInfo {
        private boolean big;
        private String filter;
        private ArrayList<ImageReceiver> imageReceiverArray;
        private ArrayList<Integer> imageReceiverGuidsArray;
        private TLRPC.Document parentDocument;

        private ThumbGenerateInfo() {
            this.imageReceiverArray = new ArrayList<>();
            this.imageReceiverGuidsArray = new ArrayList<>();
        }
    }

    /* loaded from: classes4.dex */
    public class HttpFileTask extends AsyncTask<Void, Void, Boolean> {
        private int currentAccount;
        private String ext;
        private int fileSize;
        private long lastProgressTime;
        private File tempFile;
        private String url;
        private RandomAccessFile fileOutputStream = null;
        private boolean canRetry = true;

        public HttpFileTask(String url, File tempFile, String ext, int currentAccount) {
            ImageLoader.this = r1;
            this.url = url;
            this.tempFile = tempFile;
            this.ext = ext;
            this.currentAccount = currentAccount;
        }

        private void reportProgress(final long uploadedSize, final long totalSize) {
            long currentTime = SystemClock.elapsedRealtime();
            if (uploadedSize != totalSize) {
                long j = this.lastProgressTime;
                if (j != 0 && j >= currentTime - 100) {
                    return;
                }
            }
            this.lastProgressTime = currentTime;
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpFileTask$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpFileTask.this.m308x99a68a63(uploadedSize, totalSize);
                }
            });
        }

        /* renamed from: lambda$reportProgress$1$org-telegram-messenger-ImageLoader$HttpFileTask */
        public /* synthetic */ void m308x99a68a63(final long uploadedSize, final long totalSize) {
            ImageLoader.this.fileProgresses.put(this.url, new long[]{uploadedSize, totalSize});
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpFileTask$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpFileTask.this.m307x9a1cf062(uploadedSize, totalSize);
                }
            });
        }

        /* renamed from: lambda$reportProgress$0$org-telegram-messenger-ImageLoader$HttpFileTask */
        public /* synthetic */ void m307x9a1cf062(long uploadedSize, long totalSize) {
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.fileLoadProgressChanged, this.url, Long.valueOf(uploadedSize), Long.valueOf(totalSize));
        }

        public Boolean doInBackground(Void... voids) {
            List values;
            String length;
            int code;
            InputStream httpConnectionStream = null;
            boolean done = false;
            URLConnection httpConnection = null;
            try {
                URL downloadUrl = new URL(this.url);
                httpConnection = downloadUrl.openConnection();
                httpConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1");
                httpConnection.setConnectTimeout(5000);
                httpConnection.setReadTimeout(5000);
                if (httpConnection instanceof HttpURLConnection) {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) httpConnection;
                    httpURLConnection.setInstanceFollowRedirects(true);
                    int status = httpURLConnection.getResponseCode();
                    if (status == 302 || status == 301 || status == 303) {
                        String newUrl = httpURLConnection.getHeaderField("Location");
                        String cookies = httpURLConnection.getHeaderField("Set-Cookie");
                        URL downloadUrl2 = new URL(newUrl);
                        httpConnection = downloadUrl2.openConnection();
                        httpConnection.setRequestProperty("Cookie", cookies);
                        httpConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1");
                    }
                }
                httpConnection.connect();
                httpConnectionStream = httpConnection.getInputStream();
                this.fileOutputStream = new RandomAccessFile(this.tempFile, "rws");
            } catch (Throwable e) {
                if (e instanceof SocketTimeoutException) {
                    if (ApplicationLoader.isNetworkOnline()) {
                        this.canRetry = false;
                    }
                } else if (e instanceof UnknownHostException) {
                    this.canRetry = false;
                } else if (e instanceof SocketException) {
                    if (e.getMessage() != null && e.getMessage().contains("ECONNRESET")) {
                        this.canRetry = false;
                    }
                } else if (e instanceof FileNotFoundException) {
                    this.canRetry = false;
                }
                FileLog.e(e);
            }
            if (this.canRetry) {
                try {
                    if ((httpConnection instanceof HttpURLConnection) && (code = ((HttpURLConnection) httpConnection).getResponseCode()) != 200 && code != 202 && code != 304) {
                        this.canRetry = false;
                    }
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
                if (httpConnection != null) {
                    try {
                        Map<String, List<String>> headerFields = httpConnection.getHeaderFields();
                        if (headerFields != null && (values = headerFields.get("content-Length")) != null && !values.isEmpty() && (length = values.get(0)) != null) {
                            this.fileSize = Utilities.parseInt((CharSequence) length).intValue();
                        }
                    } catch (Exception e3) {
                        FileLog.e(e3);
                    }
                }
                if (httpConnectionStream != null) {
                    try {
                        byte[] data = new byte[32768];
                        int totalLoaded = 0;
                        while (!isCancelled()) {
                            try {
                                int read = httpConnectionStream.read(data);
                                if (read > 0) {
                                    this.fileOutputStream.write(data, 0, read);
                                    totalLoaded += read;
                                    int i = this.fileSize;
                                    if (i > 0) {
                                        reportProgress(totalLoaded, i);
                                    }
                                } else if (read == -1) {
                                    done = true;
                                    int i2 = this.fileSize;
                                    if (i2 != 0) {
                                        reportProgress(i2, i2);
                                    }
                                }
                            } catch (Exception e4) {
                                FileLog.e(e4);
                            }
                        }
                    } catch (Throwable e5) {
                        FileLog.e(e5);
                    }
                }
                try {
                    RandomAccessFile randomAccessFile = this.fileOutputStream;
                    if (randomAccessFile != null) {
                        randomAccessFile.close();
                        this.fileOutputStream = null;
                    }
                } catch (Throwable e6) {
                    FileLog.e(e6);
                }
                if (httpConnectionStream != null) {
                    try {
                        httpConnectionStream.close();
                    } catch (Throwable e7) {
                        FileLog.e(e7);
                    }
                }
            }
            return Boolean.valueOf(done);
        }

        public void onPostExecute(Boolean result) {
            ImageLoader.this.runHttpFileLoadTasks(this, result.booleanValue() ? 2 : 1);
        }

        @Override // android.os.AsyncTask
        protected void onCancelled() {
            ImageLoader.this.runHttpFileLoadTasks(this, 2);
        }
    }

    /* loaded from: classes4.dex */
    public class ArtworkLoadTask extends AsyncTask<Void, Void, String> {
        private CacheImage cacheImage;
        private boolean canRetry = true;
        private HttpURLConnection httpConnection;
        private boolean small;

        public ArtworkLoadTask(CacheImage cacheImage) {
            ImageLoader.this = r3;
            boolean z = true;
            this.cacheImage = cacheImage;
            Uri uri = Uri.parse(cacheImage.imageLocation.path);
            this.small = uri.getQueryParameter("s") == null ? false : z;
        }

        public String doInBackground(Void... voids) {
            int read;
            int code;
            try {
                String location = this.cacheImage.imageLocation.path;
                URL downloadUrl = new URL(location.replace("athumb://", "https://"));
                HttpURLConnection httpURLConnection = (HttpURLConnection) downloadUrl.openConnection();
                this.httpConnection = httpURLConnection;
                httpURLConnection.setConnectTimeout(5000);
                this.httpConnection.setReadTimeout(5000);
                this.httpConnection.connect();
                try {
                    HttpURLConnection httpURLConnection2 = this.httpConnection;
                    if (httpURLConnection2 != null && (code = httpURLConnection2.getResponseCode()) != 200 && code != 202 && code != 304) {
                        this.canRetry = false;
                    }
                } catch (Exception e) {
                    FileLog.e((Throwable) e, false);
                }
                InputStream httpConnectionStream = this.httpConnection.getInputStream();
                ByteArrayOutputStream outbuf = new ByteArrayOutputStream();
                byte[] data = new byte[32768];
                while (!isCancelled() && (read = httpConnectionStream.read(data)) > 0) {
                    outbuf.write(data, 0, read);
                }
                this.canRetry = false;
                JSONObject object = new JSONObject(new String(outbuf.toByteArray()));
                JSONArray array = object.getJSONArray("results");
                if (array.length() <= 0) {
                    try {
                        HttpURLConnection httpURLConnection3 = this.httpConnection;
                        if (httpURLConnection3 != null) {
                            httpURLConnection3.disconnect();
                        }
                    } catch (Throwable th) {
                    }
                    if (httpConnectionStream != null) {
                        try {
                            httpConnectionStream.close();
                        } catch (Throwable e2) {
                            FileLog.e(e2);
                        }
                    }
                    outbuf.close();
                    return null;
                }
                JSONObject media = array.getJSONObject(0);
                String artworkUrl100 = media.getString("artworkUrl100");
                if (this.small) {
                    try {
                        HttpURLConnection httpURLConnection4 = this.httpConnection;
                        if (httpURLConnection4 != null) {
                            httpURLConnection4.disconnect();
                        }
                    } catch (Throwable th2) {
                    }
                    if (httpConnectionStream != null) {
                        try {
                            httpConnectionStream.close();
                        } catch (Throwable e3) {
                            FileLog.e(e3);
                        }
                    }
                    try {
                        outbuf.close();
                    } catch (Exception e4) {
                    }
                    return artworkUrl100;
                }
                String replace = artworkUrl100.replace("100x100", "600x600");
                try {
                    HttpURLConnection httpURLConnection5 = this.httpConnection;
                    if (httpURLConnection5 != null) {
                        httpURLConnection5.disconnect();
                    }
                } catch (Throwable th3) {
                }
                if (httpConnectionStream != null) {
                    try {
                        httpConnectionStream.close();
                    } catch (Throwable e5) {
                        FileLog.e(e5);
                    }
                }
                try {
                    outbuf.close();
                } catch (Exception e6) {
                }
                return replace;
            } catch (Exception e7) {
                return null;
            }
        }

        public void onPostExecute(final String result) {
            if (result != null) {
                ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$ArtworkLoadTask$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        ImageLoader.ArtworkLoadTask.this.m300x647e920b(result);
                    }
                });
            } else if (this.canRetry) {
                ImageLoader.this.artworkLoadError(this.cacheImage.url);
            }
            ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$ArtworkLoadTask$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.ArtworkLoadTask.this.m301x92572c6a();
                }
            });
        }

        /* renamed from: lambda$onPostExecute$0$org-telegram-messenger-ImageLoader$ArtworkLoadTask */
        public /* synthetic */ void m300x647e920b(String result) {
            this.cacheImage.httpTask = new HttpImageTask(this.cacheImage, 0, result);
            ImageLoader.this.httpTasks.add(this.cacheImage.httpTask);
            ImageLoader.this.runHttpTasks(false);
        }

        /* renamed from: lambda$onPostExecute$1$org-telegram-messenger-ImageLoader$ArtworkLoadTask */
        public /* synthetic */ void m301x92572c6a() {
            ImageLoader.this.runArtworkTasks(true);
        }

        /* renamed from: lambda$onCancelled$2$org-telegram-messenger-ImageLoader$ArtworkLoadTask */
        public /* synthetic */ void m299x1e3958ad() {
            ImageLoader.this.runArtworkTasks(true);
        }

        @Override // android.os.AsyncTask
        protected void onCancelled() {
            ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$ArtworkLoadTask$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.ArtworkLoadTask.this.m299x1e3958ad();
                }
            });
        }
    }

    /* loaded from: classes4.dex */
    public class HttpImageTask extends AsyncTask<Void, Void, Boolean> {
        private CacheImage cacheImage;
        private boolean canRetry = true;
        private RandomAccessFile fileOutputStream;
        private HttpURLConnection httpConnection;
        private long imageSize;
        private long lastProgressTime;
        private String overrideUrl;

        public HttpImageTask(CacheImage cacheImage, long size) {
            ImageLoader.this = r1;
            this.cacheImage = cacheImage;
            this.imageSize = size;
        }

        public HttpImageTask(CacheImage cacheImage, int size, String url) {
            ImageLoader.this = r3;
            this.cacheImage = cacheImage;
            this.imageSize = size;
            this.overrideUrl = url;
        }

        private void reportProgress(final long uploadedSize, final long totalSize) {
            long currentTime = SystemClock.elapsedRealtime();
            if (uploadedSize != totalSize) {
                long j = this.lastProgressTime;
                if (j != 0 && j >= currentTime - 100) {
                    return;
                }
            }
            this.lastProgressTime = currentTime;
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpImageTask.this.m316xb5eb4c7e(uploadedSize, totalSize);
                }
            });
        }

        /* renamed from: lambda$reportProgress$1$org-telegram-messenger-ImageLoader$HttpImageTask */
        public /* synthetic */ void m316xb5eb4c7e(final long uploadedSize, final long totalSize) {
            ImageLoader.this.fileProgresses.put(this.cacheImage.url, new long[]{uploadedSize, totalSize});
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpImageTask.this.m315xc441a65f(uploadedSize, totalSize);
                }
            });
        }

        /* renamed from: lambda$reportProgress$0$org-telegram-messenger-ImageLoader$HttpImageTask */
        public /* synthetic */ void m315xc441a65f(long uploadedSize, long totalSize) {
            NotificationCenter.getInstance(this.cacheImage.currentAccount).postNotificationName(NotificationCenter.fileLoadProgressChanged, this.cacheImage.url, Long.valueOf(uploadedSize), Long.valueOf(totalSize));
        }

        public Boolean doInBackground(Void... voids) {
            int provider;
            WebFile webFile;
            HttpURLConnection httpURLConnection;
            List values;
            String length;
            int code;
            InputStream httpConnectionStream = null;
            boolean done = false;
            if (!isCancelled()) {
                try {
                    String location = this.cacheImage.imageLocation.path;
                    if ((location.startsWith("https://static-maps") || location.startsWith("https://maps.googleapis")) && (((provider = MessagesController.getInstance(this.cacheImage.currentAccount).mapProvider) == 3 || provider == 4) && (webFile = (WebFile) ImageLoader.this.testWebFile.get(location)) != null)) {
                        TLRPC.TL_upload_getWebFile req = new TLRPC.TL_upload_getWebFile();
                        req.location = webFile.location;
                        req.offset = 0;
                        req.limit = 0;
                        ConnectionsManager.getInstance(this.cacheImage.currentAccount).sendRequest(req, ImageLoader$HttpImageTask$$ExternalSyntheticLambda8.INSTANCE);
                    }
                    String str = this.overrideUrl;
                    if (str == null) {
                        str = location;
                    }
                    URL downloadUrl = new URL(str);
                    HttpURLConnection httpURLConnection2 = (HttpURLConnection) downloadUrl.openConnection();
                    this.httpConnection = httpURLConnection2;
                    httpURLConnection2.addRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1");
                    this.httpConnection.setConnectTimeout(5000);
                    this.httpConnection.setReadTimeout(5000);
                    this.httpConnection.setInstanceFollowRedirects(true);
                    if (!isCancelled()) {
                        this.httpConnection.connect();
                        httpConnectionStream = this.httpConnection.getInputStream();
                        this.fileOutputStream = new RandomAccessFile(this.cacheImage.tempFilePath, "rws");
                    }
                } catch (Throwable e) {
                    boolean sentLogs = true;
                    if (e instanceof SocketTimeoutException) {
                        if (ApplicationLoader.isNetworkOnline()) {
                            this.canRetry = false;
                        }
                        sentLogs = false;
                    } else if (e instanceof UnknownHostException) {
                        this.canRetry = false;
                        sentLogs = false;
                    } else if (e instanceof SocketException) {
                        if (e.getMessage() != null && e.getMessage().contains("ECONNRESET")) {
                            this.canRetry = false;
                        }
                        sentLogs = false;
                    } else if (e instanceof FileNotFoundException) {
                        this.canRetry = false;
                        sentLogs = false;
                    } else if (e instanceof InterruptedIOException) {
                        sentLogs = false;
                    }
                    FileLog.e(e, sentLogs);
                }
            }
            if (!isCancelled()) {
                try {
                    HttpURLConnection httpURLConnection3 = this.httpConnection;
                    if (httpURLConnection3 != null && (code = httpURLConnection3.getResponseCode()) != 200 && code != 202 && code != 304) {
                        this.canRetry = false;
                    }
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
                if (this.imageSize == 0 && (httpURLConnection = this.httpConnection) != null) {
                    try {
                        Map<String, List<String>> headerFields = httpURLConnection.getHeaderFields();
                        if (headerFields != null && (values = headerFields.get("content-Length")) != null && !values.isEmpty() && (length = values.get(0)) != null) {
                            this.imageSize = Utilities.parseInt((CharSequence) length).intValue();
                        }
                    } catch (Exception e3) {
                        FileLog.e(e3);
                    }
                }
                if (httpConnectionStream != null) {
                    try {
                        byte[] data = new byte[8192];
                        int totalLoaded = 0;
                        while (!isCancelled()) {
                            try {
                                int read = httpConnectionStream.read(data);
                                if (read > 0) {
                                    totalLoaded += read;
                                    this.fileOutputStream.write(data, 0, read);
                                    long j = this.imageSize;
                                    if (j != 0) {
                                        reportProgress(totalLoaded, j);
                                    }
                                } else if (read == -1) {
                                    done = true;
                                    long j2 = this.imageSize;
                                    if (j2 != 0) {
                                        reportProgress(j2, j2);
                                    }
                                }
                            } catch (Exception e4) {
                                FileLog.e(e4);
                            }
                        }
                    } catch (Throwable e5) {
                        FileLog.e(e5);
                    }
                }
            }
            try {
                RandomAccessFile randomAccessFile = this.fileOutputStream;
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                    this.fileOutputStream = null;
                }
            } catch (Throwable e6) {
                FileLog.e(e6);
            }
            try {
                HttpURLConnection httpURLConnection4 = this.httpConnection;
                if (httpURLConnection4 != null) {
                    httpURLConnection4.disconnect();
                }
            } catch (Throwable th) {
            }
            if (httpConnectionStream != null) {
                try {
                    httpConnectionStream.close();
                } catch (Throwable e7) {
                    FileLog.e(e7);
                }
            }
            if (done && this.cacheImage.tempFilePath != null && !this.cacheImage.tempFilePath.renameTo(this.cacheImage.finalFilePath)) {
                CacheImage cacheImage = this.cacheImage;
                cacheImage.finalFilePath = cacheImage.tempFilePath;
            }
            return Boolean.valueOf(done);
        }

        public static /* synthetic */ void lambda$doInBackground$2(TLObject response, TLRPC.TL_error error) {
        }

        public void onPostExecute(final Boolean result) {
            if (result.booleanValue() || !this.canRetry) {
                ImageLoader.this.fileDidLoaded(this.cacheImage.url, this.cacheImage.finalFilePath, 0);
            } else {
                ImageLoader.this.httpFileLoadError(this.cacheImage.url);
            }
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpImageTask.this.m313x6321fc0(result);
                }
            });
            ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpImageTask.this.m314xf7dbc5df();
                }
            });
        }

        /* renamed from: lambda$onPostExecute$4$org-telegram-messenger-ImageLoader$HttpImageTask */
        public /* synthetic */ void m313x6321fc0(final Boolean result) {
            ImageLoader.this.fileProgresses.remove(this.cacheImage.url);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpImageTask.this.m312x148879a1(result);
                }
            });
        }

        /* renamed from: lambda$onPostExecute$3$org-telegram-messenger-ImageLoader$HttpImageTask */
        public /* synthetic */ void m312x148879a1(Boolean result) {
            if (result.booleanValue()) {
                NotificationCenter.getInstance(this.cacheImage.currentAccount).postNotificationName(NotificationCenter.fileLoaded, this.cacheImage.url, this.cacheImage.finalFilePath);
            } else {
                NotificationCenter.getInstance(this.cacheImage.currentAccount).postNotificationName(NotificationCenter.fileLoadFailed, this.cacheImage.url, 2);
            }
        }

        /* renamed from: lambda$onPostExecute$5$org-telegram-messenger-ImageLoader$HttpImageTask */
        public /* synthetic */ void m314xf7dbc5df() {
            ImageLoader.this.runHttpTasks(true);
        }

        /* renamed from: lambda$onCancelled$6$org-telegram-messenger-ImageLoader$HttpImageTask */
        public /* synthetic */ void m309xa7d226e2() {
            ImageLoader.this.runHttpTasks(true);
        }

        @Override // android.os.AsyncTask
        protected void onCancelled() {
            ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpImageTask.this.m309xa7d226e2();
                }
            });
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpImageTask.this.m311x8b257320();
                }
            });
        }

        /* renamed from: lambda$onCancelled$8$org-telegram-messenger-ImageLoader$HttpImageTask */
        public /* synthetic */ void m311x8b257320() {
            ImageLoader.this.fileProgresses.remove(this.cacheImage.url);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$HttpImageTask$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.HttpImageTask.this.m310x997bcd01();
                }
            });
        }

        /* renamed from: lambda$onCancelled$7$org-telegram-messenger-ImageLoader$HttpImageTask */
        public /* synthetic */ void m310x997bcd01() {
            NotificationCenter.getInstance(this.cacheImage.currentAccount).postNotificationName(NotificationCenter.fileLoadFailed, this.cacheImage.url, 1);
        }
    }

    /* loaded from: classes4.dex */
    public class ThumbGenerateTask implements Runnable {
        private ThumbGenerateInfo info;
        private int mediaType;
        private File originalPath;

        public ThumbGenerateTask(int type, File path, ThumbGenerateInfo i) {
            ImageLoader.this = r1;
            this.mediaType = type;
            this.originalPath = path;
            this.info = i;
        }

        private void removeTask() {
            ThumbGenerateInfo thumbGenerateInfo = this.info;
            if (thumbGenerateInfo != null) {
                final String name = FileLoader.getAttachFileName(thumbGenerateInfo.parentDocument);
                ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$ThumbGenerateTask$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ImageLoader.ThumbGenerateTask.this.m317x21f4326f(name);
                    }
                });
            }
        }

        /* renamed from: lambda$removeTask$0$org-telegram-messenger-ImageLoader$ThumbGenerateTask */
        public /* synthetic */ void m317x21f4326f(String name) {
            ImageLoader.this.thumbGenerateTasks.remove(name);
        }

        @Override // java.lang.Runnable
        public void run() {
            Bitmap originalBitmap;
            Bitmap scaledBitmap;
            try {
                if (this.info == null) {
                    removeTask();
                    return;
                }
                final String key = "q_" + this.info.parentDocument.dc_id + "_" + this.info.parentDocument.id;
                File thumbFile = new File(FileLoader.getDirectory(4), key + ".jpg");
                if (!thumbFile.exists() && this.originalPath.exists()) {
                    int size = this.info.big ? Math.max(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y) : Math.min(180, Math.min(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y) / 4);
                    Bitmap originalBitmap2 = null;
                    int i = this.mediaType;
                    if (i == 0) {
                        originalBitmap2 = ImageLoader.loadBitmap(this.originalPath.toString(), null, size, size, false);
                    } else {
                        int i2 = 2;
                        if (i == 2) {
                            String file = this.originalPath.toString();
                            if (!this.info.big) {
                                i2 = 1;
                            }
                            originalBitmap2 = SendMessagesHelper.createVideoThumbnail(file, i2);
                        } else if (i == 3) {
                            String path = this.originalPath.toString().toLowerCase();
                            if (path.endsWith("mp4")) {
                                String file2 = this.originalPath.toString();
                                if (!this.info.big) {
                                    i2 = 1;
                                }
                                originalBitmap2 = SendMessagesHelper.createVideoThumbnail(file2, i2);
                            } else if (path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png") || path.endsWith(".gif")) {
                                originalBitmap2 = ImageLoader.loadBitmap(path, null, size, size, false);
                            }
                        }
                    }
                    if (originalBitmap2 == null) {
                        removeTask();
                        return;
                    }
                    int w = originalBitmap2.getWidth();
                    int h = originalBitmap2.getHeight();
                    if (w != 0 && h != 0) {
                        float scaleFactor = Math.min(w / size, h / size);
                        if (scaleFactor > 1.0f && (scaledBitmap = Bitmaps.createScaledBitmap(originalBitmap2, (int) (w / scaleFactor), (int) (h / scaleFactor), true)) != originalBitmap2) {
                            originalBitmap2.recycle();
                            originalBitmap = scaledBitmap;
                        } else {
                            originalBitmap = originalBitmap2;
                        }
                        FileOutputStream stream = new FileOutputStream(thumbFile);
                        originalBitmap.compress(Bitmap.CompressFormat.JPEG, this.info.big ? 83 : 60, stream);
                        try {
                            stream.close();
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                        final BitmapDrawable bitmapDrawable = new BitmapDrawable(originalBitmap);
                        final ArrayList<ImageReceiver> finalImageReceiverArray = new ArrayList<>(this.info.imageReceiverArray);
                        final ArrayList<Integer> finalImageReceiverGuidsArray = new ArrayList<>(this.info.imageReceiverGuidsArray);
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$ThumbGenerateTask$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                ImageLoader.ThumbGenerateTask.this.m318x4b36f506(key, finalImageReceiverArray, bitmapDrawable, finalImageReceiverGuidsArray);
                            }
                        });
                        return;
                    }
                    removeTask();
                    return;
                }
                removeTask();
            } catch (Throwable e2) {
                FileLog.e(e2);
                removeTask();
            }
        }

        /* renamed from: lambda$run$1$org-telegram-messenger-ImageLoader$ThumbGenerateTask */
        public /* synthetic */ void m318x4b36f506(String key, ArrayList finalImageReceiverArray, BitmapDrawable bitmapDrawable, ArrayList finalImageReceiverGuidsArray) {
            removeTask();
            String kf = key;
            if (this.info.filter != null) {
                kf = kf + "@" + this.info.filter;
            }
            for (int a = 0; a < finalImageReceiverArray.size(); a++) {
                ImageReceiver imgView = (ImageReceiver) finalImageReceiverArray.get(a);
                imgView.setImageBitmapByKey(bitmapDrawable, kf, 0, false, ((Integer) finalImageReceiverGuidsArray.get(a)).intValue());
            }
            ImageLoader.this.memCache.put(kf, bitmapDrawable);
        }
    }

    public static String decompressGzip(File file) {
        StringBuilder outStr = new StringBuilder();
        if (file == null) {
            return "";
        }
        try {
            GZIPInputStream gis = new GZIPInputStream(new FileInputStream(file));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
            while (true) {
                try {
                    String line = bufferedReader.readLine();
                    if (line != null) {
                        outStr.append(line);
                    } else {
                        String sb = outStr.toString();
                        bufferedReader.close();
                        gis.close();
                        return sb;
                    }
                } catch (Throwable th) {
                    try {
                        bufferedReader.close();
                    } catch (Throwable th2) {
                    }
                    throw th;
                }
            }
        } catch (Exception e) {
            return "";
        }
    }

    /* loaded from: classes4.dex */
    public class CacheOutTask implements Runnable {
        private CacheImage cacheImage;
        private boolean isCancelled;
        private Thread runningThread;
        private final Object sync = new Object();

        public CacheOutTask(CacheImage image) {
            ImageLoader.this = r1;
            this.cacheImage = image;
        }

        /* JADX WARN: Can't wrap try/catch for region: R(12:774|(2:776|(10:778|782|876|783|(1:785)(1:786)|787|788|(1:792)(1:793)|794|795)(1:779))(1:780)|781|782|876|783|(0)(0)|787|788|(0)(0)|794|795) */
        /* JADX WARN: Can't wrap try/catch for region: R(25:184|(1:192)(1:191)|193|(2:195|(1:200)(1:199))(1:201)|202|(7:204|823|205|(1:207)(1:208)|209|(2:211|(1:213))|214)|228|(18:230|(3:232|(1:234)|235)(2:236|(3:238|(1:240)|241)(2:242|(1:244)))|246|(1:248)|249|882|250|875|(14:252|253|863|254|255|(4:257|839|258|259)|854|262|(1:264)(2:265|(1:267)(2:268|(1:270)))|271|(1:273)|274|(1:276)|(1:349)(9:282|884|(2:299|(12:861|301|(1:306)(1:305)|(1:308)(1:309)|310|311|312|313|(1:317)|318|(1:320)|321)(3:324|(1:326)(1:327)|328))(1:(7:847|286|287|842|288|289|290)(2:295|296))|329|(1:334)(1:333)|335|(1:337)|338|(1:348)(4:344|(1:345)|890|347)))(2:354|(14:356|(1:358)(1:359)|360|869|361|(1:363)|364|(3:366|(2:367|(1:891)(1:370))|369)(1:371)|868|376|383|(4:821|385|810|7b9)(7:539|(1:541)(1:542)|(3:803|544|(9:546|547|(3:885|549|(1:551))|873|557|ac1|756|858|757))|556|873|557|ac1)|763|(3:(1:771)(1:772)|773|899)(3:(1:767)(1:768)|769|898)))|375|376|383|(0)(0)|763|(0)|(0)(0)|773|899)|245|246|(0)|249|882|250|875|(0)(0)|375|376|383|(0)(0)|763|(0)|(0)(0)|773|899) */
        /* JADX WARN: Can't wrap try/catch for region: R(7:393|850|(12:808|395|396|801|397|398|399|400|401|(1:403)(1:404)|405|406)(4:411|799|412|(11:425|(1:430)(1:429)|(1:432)|433|434|(5:436|437|438|(1:442)|443)(2:445|(5:447|(2:449|450)(1:451)|889|(3:453|(1:457)|458)(7:459|(2:461|(1:471))|(5:473|(1:475)(1:476)|477|(1:479)(1:480)|481)(1:482)|483|879|(2:485|(3:487|(1:489)(1:490)|491))(2:494|(2:496|(3:498|(1:500)(1:501)|502))(2:503|(2:505|(7:507|(1:509)(1:510)|511|(1:513)(1:514)|515|(1:517)(1:518)|519))(1:(2:521|(1:523)))))|878)|538))|444|(0)(0)|889|(0)(0)|538)(4:(1:416)(1:417)|418|419|420))|849|889|(0)(0)|538) */
        /* JADX WARN: Code restructure failed: missing block: B:373:0x0783, code lost:
            r0 = th;
         */
        /* JADX WARN: Code restructure failed: missing block: B:374:0x0784, code lost:
            r6 = r26;
         */
        /* JADX WARN: Code restructure failed: missing block: B:377:0x078e, code lost:
            r0 = th;
         */
        /* JADX WARN: Code restructure failed: missing block: B:378:0x078f, code lost:
            r31 = r5;
            r26 = null;
            r27 = false;
            r29 = 0;
            r28 = r14;
            r15 = r15 == 1 ? 1 : 0;
            r15 = r15 == 1 ? 1 : 0;
            r7 = r15;
         */
        /* JADX WARN: Code restructure failed: missing block: B:524:0x0a1e, code lost:
            r0 = th;
         */
        /* JADX WARN: Code restructure failed: missing block: B:525:0x0a1f, code lost:
            r7 = r27;
            r26 = r26;
         */
        /* JADX WARN: Code restructure failed: missing block: B:566:0x0ad4, code lost:
            if (r23 != false) goto L574;
         */
        /* JADX WARN: Code restructure failed: missing block: B:568:0x0ada, code lost:
            if (r42.cacheImage.filter == null) goto L574;
         */
        /* JADX WARN: Code restructure failed: missing block: B:569:0x0adc, code lost:
            if (r9 != null) goto L574;
         */
        /* JADX WARN: Code restructure failed: missing block: B:571:0x0ae4, code lost:
            if (r42.cacheImage.imageLocation.path == null) goto L573;
         */
        /* JADX WARN: Code restructure failed: missing block: B:573:0x0ae7, code lost:
            r0.inPreferredConfig = android.graphics.Bitmap.Config.RGB_565;
         */
        /* JADX WARN: Code restructure failed: missing block: B:574:0x0aec, code lost:
            r0.inPreferredConfig = android.graphics.Bitmap.Config.ARGB_8888;
         */
        /* JADX WARN: Code restructure failed: missing block: B:575:0x0af0, code lost:
            r0.inDither = false;
         */
        /* JADX WARN: Code restructure failed: missing block: B:576:0x0af3, code lost:
            if (r35 == null) goto L582;
         */
        /* JADX WARN: Code restructure failed: missing block: B:577:0x0af5, code lost:
            if (r34 != null) goto L582;
         */
        /* JADX WARN: Code restructure failed: missing block: B:578:0x0af7, code lost:
            if (r31 == false) goto L580;
         */
        /* JADX WARN: Code restructure failed: missing block: B:579:0x0af9, code lost:
            r6 = android.provider.MediaStore.Video.Thumbnails.getThumbnail(org.telegram.messenger.ApplicationLoader.applicationContext.getContentResolver(), r35.longValue(), 1, r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:581:0x0b19, code lost:
            r6 = android.provider.MediaStore.Images.Thumbnails.getThumbnail(org.telegram.messenger.ApplicationLoader.applicationContext.getContentResolver(), r35.longValue(), 1, r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:582:0x0b1b, code lost:
            r6 = r21;
         */
        /* JADX WARN: Code restructure failed: missing block: B:583:0x0b1d, code lost:
            if (r6 != null) goto L645;
         */
        /* JADX WARN: Code restructure failed: missing block: B:584:0x0b1f, code lost:
            if (r30 == null) goto L829;
         */
        /* JADX WARN: Code restructure failed: missing block: B:585:0x0b21, code lost:
            r0 = new java.io.RandomAccessFile(r10, "r");
            r5 = r0.getChannel().map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, r10.length());
            r7 = new android.graphics.BitmapFactory.Options();
            r7.inJustDecodeBounds = true;
            org.telegram.messenger.Utilities.loadWebpImage(null, r5, r5.limit(), r7, true);
            r6 = org.telegram.messenger.Bitmaps.createBitmap(r7.outWidth, r7.outHeight, android.graphics.Bitmap.Config.ARGB_8888);
            r8 = r5.limit();
         */
        /* JADX WARN: Code restructure failed: missing block: B:586:0x0b59, code lost:
            if (r0.inPurgeable != false) goto L588;
         */
        /* JADX WARN: Code restructure failed: missing block: B:587:0x0b5b, code lost:
            r14 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:588:0x0b5d, code lost:
            r14 = false;
         */
        /* JADX WARN: Code restructure failed: missing block: B:589:0x0b5e, code lost:
            org.telegram.messenger.Utilities.loadWebpImage(r6, r5, r8, null, r14);
            r0.close();
         */
        /* JADX WARN: Code restructure failed: missing block: B:590:0x0b65, code lost:
            r17 = r9;
            r9 = r29;
         */
        /* JADX WARN: Code restructure failed: missing block: B:592:0x0b6c, code lost:
            r7 = r27;
            r9 = r29;
         */
        /* JADX WARN: Code restructure failed: missing block: B:594:0x0b7a, code lost:
            if (r0.inPurgeable != false) goto L613;
         */
        /* JADX WARN: Code restructure failed: missing block: B:595:0x0b7c, code lost:
            if (r12 == null) goto L597;
         */
        /* JADX WARN: Code restructure failed: missing block: B:596:0x0b7e, code lost:
            r7 = null;
         */
        /* JADX WARN: Code restructure failed: missing block: B:597:0x0b80, code lost:
            if (r11 == false) goto L599;
         */
        /* JADX WARN: Code restructure failed: missing block: B:598:0x0b82, code lost:
            r5 = new org.telegram.messenger.secretmedia.EncryptedFileInputStream(r10, r42.cacheImage.encryptionKeyPath);
         */
        /* JADX WARN: Code restructure failed: missing block: B:599:0x0b8d, code lost:
            r5 = new java.io.FileInputStream(r10);
         */
        /* JADX WARN: Code restructure failed: missing block: B:601:0x0b9b, code lost:
            if ((r42.cacheImage.imageLocation.document instanceof org.telegram.tgnet.TLRPC.TL_document) == false) goto L611;
         */
        /* JADX WARN: Code restructure failed: missing block: B:602:0x0b9d, code lost:
            r0 = new androidx.exifinterface.media.ExifInterface(r5);
            r7 = r0.getAttributeInt(androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION, 1);
         */
        /* JADX WARN: Code restructure failed: missing block: B:603:0x0ba9, code lost:
            switch(r7) {
                case 3: goto L607;
                case 6: goto L606;
                case 8: goto L605;
                default: goto L604;
            };
         */
        /* JADX WARN: Code restructure failed: missing block: B:604:0x0bac, code lost:
            r8 = r29;
         */
        /* JADX WARN: Code restructure failed: missing block: B:605:0x0baf, code lost:
            r8 = 270;
         */
        /* JADX WARN: Code restructure failed: missing block: B:606:0x0bb2, code lost:
            r8 = 90;
         */
        /* JADX WARN: Code restructure failed: missing block: B:607:0x0bb5, code lost:
            r8 = 180;
         */
        /* JADX WARN: Code restructure failed: missing block: B:608:0x0bb8, code lost:
            r29 = r8;
         */
        /* JADX WARN: Code restructure failed: missing block: B:613:0x0bd4, code lost:
            r7 = null;
         */
        /* JADX WARN: Code restructure failed: missing block: B:614:0x0bd5, code lost:
            r0 = new java.io.RandomAccessFile(r10, "r");
            r5 = (int) r0.length();
            r14 = (byte[]) org.telegram.messenger.ImageLoader.bytesLocal.get();
         */
        /* JADX WARN: Code restructure failed: missing block: B:615:0x0bec, code lost:
            if (r14 == null) goto L619;
         */
        /* JADX WARN: Code restructure failed: missing block: B:617:0x0bef, code lost:
            if (r14.length < r5) goto L619;
         */
        /* JADX WARN: Code restructure failed: missing block: B:618:0x0bf1, code lost:
            r15 = r14;
         */
        /* JADX WARN: Code restructure failed: missing block: B:619:0x0bf3, code lost:
            r15 = r7;
         */
        /* JADX WARN: Code restructure failed: missing block: B:620:0x0bf4, code lost:
            if (r15 != null) goto L622;
         */
        /* JADX WARN: Code restructure failed: missing block: B:621:0x0bf6, code lost:
            r7 = new byte[r5];
            r15 = r7;
            org.telegram.messenger.ImageLoader.bytesLocal.set(r7);
         */
        /* JADX WARN: Code restructure failed: missing block: B:623:0x0c02, code lost:
            r0.readFully(r15, 0, r5);
            r0.close();
            r16 = false;
         */
        /* JADX WARN: Code restructure failed: missing block: B:624:0x0c0a, code lost:
            if (r12 == null) goto L633;
         */
        /* JADX WARN: Code restructure failed: missing block: B:625:0x0c0c, code lost:
            org.telegram.messenger.secretmedia.EncryptedFileInputStream.decryptBytesWithKeyFile(r15, 0, r5, r12);
         */
        /* JADX WARN: Code restructure failed: missing block: B:626:0x0c0f, code lost:
            r17 = r9;
         */
        /* JADX WARN: Code restructure failed: missing block: B:627:0x0c14, code lost:
            r8 = org.telegram.messenger.Utilities.computeSHA256(r15, 0, r5);
         */
        /* JADX WARN: Code restructure failed: missing block: B:628:0x0c19, code lost:
            if (r13 == null) goto L631;
         */
        /* JADX WARN: Code restructure failed: missing block: B:630:0x0c1f, code lost:
            if (java.util.Arrays.equals(r8, r13) != false) goto L632;
         */
        /* JADX WARN: Code restructure failed: missing block: B:631:0x0c21, code lost:
            r16 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:632:0x0c24, code lost:
            r8 = r15[0] & 255;
            r5 = r5 - r8;
         */
        /* JADX WARN: Code restructure failed: missing block: B:633:0x0c2b, code lost:
            r17 = r9;
         */
        /* JADX WARN: Code restructure failed: missing block: B:634:0x0c2f, code lost:
            if (r11 == false) goto L638;
         */
        /* JADX WARN: Code restructure failed: missing block: B:635:0x0c31, code lost:
            org.telegram.messenger.secretmedia.EncryptedFileInputStream.decryptBytesWithKeyFile(r15, 0, r5, r42.cacheImage.encryptionKeyPath);
         */
        /* JADX WARN: Code restructure failed: missing block: B:637:0x0c3b, code lost:
            r7 = r27;
            r9 = r29;
         */
        /* JADX WARN: Code restructure failed: missing block: B:638:0x0c45, code lost:
            r8 = 0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:639:0x0c47, code lost:
            if (r16 != false) goto L642;
         */
        /* JADX WARN: Code restructure failed: missing block: B:641:0x0c4d, code lost:
            r6 = android.graphics.BitmapFactory.decodeByteArray(r15, r8, r5, r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:642:0x0c4e, code lost:
            r9 = r29;
         */
        /* JADX WARN: Code restructure failed: missing block: B:644:0x0c52, code lost:
            r7 = r27;
            r9 = r29;
         */
        /* JADX WARN: Code restructure failed: missing block: B:645:0x0c5e, code lost:
            r17 = r9;
            r9 = r29;
         */
        /* JADX WARN: Code restructure failed: missing block: B:789:0x0e86, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:790:0x0e87, code lost:
            org.telegram.messenger.FileLog.e(r0);
         */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Not initialized variable reg: 26, insn: 0x0784: MOVE  (r6 I:??[OBJECT, ARRAY]) = (r26 I:??[OBJECT, ARRAY] A[D('image' android.graphics.Bitmap)]), block:B:374:0x0784 */
        /* JADX WARN: Removed duplicated region for block: B:248:0x0518  */
        /* JADX WARN: Removed duplicated region for block: B:252:0x0532 A[Catch: all -> 0x078e, TRY_LEAVE, TryCatch #46 {all -> 0x078e, blocks: (B:250:0x052c, B:252:0x0532), top: B:882:0x052c }] */
        /* JADX WARN: Removed duplicated region for block: B:354:0x070d  */
        /* JADX WARN: Removed duplicated region for block: B:381:0x079f  */
        /* JADX WARN: Removed duplicated region for block: B:449:0x08e6 A[Catch: all -> 0x0a39, TRY_LEAVE, TryCatch #6 {all -> 0x0a39, blocks: (B:438:0x08c0, B:440:0x08c6, B:443:0x08cf, B:447:0x08dc, B:449:0x08e6, B:531:0x0a38), top: B:810:0x07b9 }] */
        /* JADX WARN: Removed duplicated region for block: B:451:0x08ec  */
        /* JADX WARN: Removed duplicated region for block: B:453:0x08f1 A[Catch: all -> 0x0a1e, TRY_ENTER, TryCatch #51 {all -> 0x0a1e, blocks: (B:453:0x08f1, B:455:0x08fb, B:457:0x0901, B:459:0x0908, B:461:0x090e, B:467:0x0924, B:469:0x092c, B:471:0x0939, B:473:0x093f, B:477:0x0946), top: B:889:0x08ef }] */
        /* JADX WARN: Removed duplicated region for block: B:459:0x0908 A[Catch: all -> 0x0a1e, TryCatch #51 {all -> 0x0a1e, blocks: (B:453:0x08f1, B:455:0x08fb, B:457:0x0901, B:459:0x0908, B:461:0x090e, B:467:0x0924, B:469:0x092c, B:471:0x0939, B:473:0x093f, B:477:0x0946), top: B:889:0x08ef }] */
        /* JADX WARN: Removed duplicated region for block: B:539:0x0a5b  */
        /* JADX WARN: Removed duplicated region for block: B:765:0x0e15 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:771:0x0e28  */
        /* JADX WARN: Removed duplicated region for block: B:772:0x0e2e  */
        /* JADX WARN: Removed duplicated region for block: B:785:0x0e7d  */
        /* JADX WARN: Removed duplicated region for block: B:786:0x0e7f  */
        /* JADX WARN: Removed duplicated region for block: B:792:0x0e8c  */
        /* JADX WARN: Removed duplicated region for block: B:793:0x0e92  */
        /* JADX WARN: Removed duplicated region for block: B:821:0x07ae A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:852:0x0ac2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Type inference failed for: r15v36 */
        /* JADX WARN: Type inference failed for: r15v37 */
        /* JADX WARN: Type inference failed for: r15v41 */
        /* JADX WARN: Type inference failed for: r15v42 */
        /* JADX WARN: Type inference failed for: r42v0, types: [org.telegram.messenger.ImageLoader$CacheOutTask] */
        /* JADX WARN: Type inference failed for: r5v59, types: [int] */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void run() {
            int h;
            int h2;
            Bitmap image;
            File cacheFileFinal;
            byte[] secureDocumentHash;
            SecureDocumentKey secureDocumentKey;
            String location;
            BitmapFactory.Options opts;
            boolean checkInversion;
            boolean mediaIsVideo;
            int orientation;
            boolean canDeleteFile;
            boolean needInvert;
            float image2;
            Bitmap bitmap;
            float h_filter;
            boolean needInvert2;
            int blurType;
            Throwable e;
            Throwable th;
            Bitmap scaledBitmap;
            float h_filter2;
            float w_filter;
            Throwable th2;
            int orientation2;
            boolean needInvert3;
            boolean blured;
            Bitmap scaledBitmap2;
            FileInputStream is;
            Throwable e2;
            AnimatedFileDrawable fileDrawable;
            String diceEmoji;
            int w;
            int w2;
            RLottieDrawable lottieDrawable;
            synchronized (this.sync) {
                this.runningThread = Thread.currentThread();
                Thread.interrupted();
                if (this.isCancelled) {
                    return;
                }
                if (this.cacheImage.imageLocation.photoSize instanceof TLRPC.TL_photoStrippedSize) {
                    TLRPC.TL_photoStrippedSize photoSize = (TLRPC.TL_photoStrippedSize) this.cacheImage.imageLocation.photoSize;
                    Bitmap bitmap2 = ImageLoader.getStrippedPhotoBitmap(photoSize.bytes, "b");
                    onPostExecute(bitmap2 != null ? new BitmapDrawable(bitmap2) : null);
                    return;
                } else if (this.cacheImage.imageType == 5) {
                    BitmapDrawable bitmapDrawable = null;
                    try {
                        bitmapDrawable = new ThemePreviewDrawable(this.cacheImage.finalFilePath, (DocumentObject.ThemeDocument) this.cacheImage.imageLocation.document);
                    } catch (Throwable e3) {
                        FileLog.e(e3);
                    }
                    onPostExecute(bitmapDrawable);
                    return;
                } else if (this.cacheImage.imageType == 3 || this.cacheImage.imageType == 4) {
                    int w3 = AndroidUtilities.displaySize.x;
                    int h3 = AndroidUtilities.displaySize.y;
                    if (this.cacheImage.filter != null) {
                        String[] args = this.cacheImage.filter.split("_");
                        if (args.length >= 2) {
                            float w_filter2 = Float.parseFloat(args[0]);
                            float h_filter3 = Float.parseFloat(args[1]);
                            int w4 = (int) (AndroidUtilities.density * w_filter2);
                            int h4 = (int) (AndroidUtilities.density * h_filter3);
                            h = h4;
                            h2 = w4;
                            Bitmap bitmap3 = null;
                            bitmap3 = SvgHelper.getBitmap(this.cacheImage.finalFilePath, h2, h, this.cacheImage.imageType != 4);
                            onPostExecute(bitmap3 == null ? new BitmapDrawable(bitmap3) : null);
                            return;
                        }
                    }
                    h = h3;
                    h2 = w3;
                    Bitmap bitmap32 = null;
                    bitmap32 = SvgHelper.getBitmap(this.cacheImage.finalFilePath, h2, h, this.cacheImage.imageType != 4);
                    onPostExecute(bitmap32 == null ? new BitmapDrawable(bitmap32) : null);
                    return;
                } else if (this.cacheImage.imageType == 1) {
                    int w5 = Math.min(512, AndroidUtilities.dp(170.6f));
                    int h5 = Math.min(512, AndroidUtilities.dp(170.6f));
                    boolean precache = false;
                    boolean limitFps = false;
                    boolean lastFrameBitmap = false;
                    int autoRepeat = 1;
                    String diceEmoji2 = null;
                    int fitzModifier = 0;
                    if (this.cacheImage.filter != null) {
                        String[] args2 = this.cacheImage.filter.split("_");
                        if (args2.length >= 2) {
                            float w_filter3 = Float.parseFloat(args2[0]);
                            float h_filter4 = Float.parseFloat(args2[1]);
                            w5 = Math.min(512, (int) (w_filter3 * AndroidUtilities.density));
                            h5 = Math.min(512, (int) (AndroidUtilities.density * h_filter4));
                            if (w_filter3 <= 90.0f && h_filter4 <= 90.0f && !this.cacheImage.filter.contains("nolimit")) {
                                w5 = Math.min(w5, 160);
                                h5 = Math.min(h5, 160);
                                limitFps = true;
                            }
                            if (args2.length < 3 || !"pcache".equals(args2[2])) {
                                precache = !this.cacheImage.filter.contains("nolimit") && SharedConfig.getDevicePerformanceClass() != 2;
                            } else {
                                precache = true;
                            }
                            if (this.cacheImage.filter.contains("lastframe")) {
                                lastFrameBitmap = true;
                            }
                        }
                        if (args2.length >= 3) {
                            if ("nr".equals(args2[2])) {
                                autoRepeat = 2;
                            } else if ("nrs".equals(args2[2])) {
                                autoRepeat = 3;
                            } else if ("dice".equals(args2[2])) {
                                diceEmoji2 = args2[3];
                                autoRepeat = 2;
                            }
                        }
                        if (args2.length >= 5) {
                            if ("c1".equals(args2[4])) {
                                fitzModifier = 12;
                                w = w5;
                                diceEmoji = diceEmoji2;
                                w2 = h5;
                            } else if ("c2".equals(args2[4])) {
                                fitzModifier = 3;
                                w = w5;
                                diceEmoji = diceEmoji2;
                                w2 = h5;
                            } else if ("c3".equals(args2[4])) {
                                fitzModifier = 4;
                                w = w5;
                                diceEmoji = diceEmoji2;
                                w2 = h5;
                            } else if ("c4".equals(args2[4])) {
                                fitzModifier = 5;
                                w = w5;
                                diceEmoji = diceEmoji2;
                                w2 = h5;
                            } else if ("c5".equals(args2[4])) {
                                fitzModifier = 6;
                                w = w5;
                                diceEmoji = diceEmoji2;
                                w2 = h5;
                            }
                        }
                        w = w5;
                        diceEmoji = diceEmoji2;
                        w2 = h5;
                    } else {
                        w = w5;
                        diceEmoji = null;
                        w2 = h5;
                    }
                    if (diceEmoji != null) {
                        lottieDrawable = "🎰".equals(diceEmoji) ? new SlotsDrawable(diceEmoji, w, w2) : new RLottieDrawable(diceEmoji, w, w2);
                    } else {
                        File file = this.cacheImage.finalFilePath;
                        RandomAccessFile randomAccessFile = null;
                        boolean compressed = false;
                        try {
                            try {
                                try {
                                    randomAccessFile = new RandomAccessFile(this.cacheImage.finalFilePath, "r");
                                    byte[] bytes = this.cacheImage.type == 1 ? ImageLoader.headerThumb : ImageLoader.header;
                                    randomAccessFile.readFully(bytes, 0, 2);
                                    if (bytes[0] == 31) {
                                        if (bytes[1] == -117) {
                                            compressed = true;
                                        }
                                    }
                                    randomAccessFile.close();
                                } catch (Exception e4) {
                                    FileLog.e(e4);
                                }
                            } catch (Exception e5) {
                                FileLog.e((Throwable) e5, false);
                                if (randomAccessFile != null) {
                                    randomAccessFile.close();
                                }
                            }
                            if (lastFrameBitmap) {
                                precache = false;
                            }
                            lottieDrawable = compressed ? new RLottieDrawable(this.cacheImage.finalFilePath, ImageLoader.decompressGzip(this.cacheImage.finalFilePath), w, w2, precache, limitFps, null, fitzModifier) : new RLottieDrawable(this.cacheImage.finalFilePath, w, w2, precache, limitFps, null, fitzModifier);
                        } finally {
                        }
                    }
                    if (lastFrameBitmap) {
                        loadLastFrame(lottieDrawable, w2, w);
                        return;
                    }
                    lottieDrawable.setAutoRepeat(autoRepeat);
                    onPostExecute(lottieDrawable);
                    return;
                } else if (this.cacheImage.imageType == 2) {
                    long seekTo = this.cacheImage.imageLocation != null ? this.cacheImage.imageLocation.videoSeekTo : 0L;
                    boolean limitFps2 = false;
                    if (this.cacheImage.filter != null) {
                        String[] args3 = this.cacheImage.filter.split("_");
                        if (args3.length >= 2) {
                            float w_filter4 = Float.parseFloat(args3[0]);
                            float h_filter5 = Float.parseFloat(args3[1]);
                            if (w_filter4 <= 90.0f && h_filter5 <= 90.0f && !this.cacheImage.filter.contains("nolimit")) {
                                limitFps2 = true;
                            }
                        }
                    }
                    if ((ImageLoader.this.isAnimatedAvatar(this.cacheImage.filter) || ImageLoader.AUTOPLAY_FILTER.equals(this.cacheImage.filter)) && !(this.cacheImage.imageLocation.document instanceof TLRPC.TL_documentEncrypted)) {
                        TLRPC.Document document = this.cacheImage.imageLocation.document instanceof TLRPC.Document ? this.cacheImage.imageLocation.document : null;
                        CacheImage cacheImage = this.cacheImage;
                        long size = document != null ? cacheImage.size : cacheImage.imageLocation.currentSize;
                        fileDrawable = new AnimatedFileDrawable(this.cacheImage.finalFilePath, false, size, document, document == null ? this.cacheImage.imageLocation : null, this.cacheImage.parentObject, seekTo, this.cacheImage.currentAccount, false);
                        fileDrawable.setIsWebmSticker(MessageObject.isWebM(document) || MessageObject.isVideoSticker(document) || ImageLoader.this.isAnimatedAvatar(this.cacheImage.filter));
                    } else {
                        int w6 = 0;
                        int h6 = 0;
                        if (this.cacheImage.filter != null) {
                            String[] args4 = this.cacheImage.filter.split("_");
                            if (args4.length >= 2) {
                                float w_filter5 = Float.parseFloat(args4[0]);
                                float h_filter6 = Float.parseFloat(args4[1]);
                                w6 = (int) (AndroidUtilities.density * w_filter5);
                                h6 = (int) (AndroidUtilities.density * h_filter6);
                            }
                        }
                        AnimatedFileDrawable fileDrawable2 = new AnimatedFileDrawable(this.cacheImage.finalFilePath, Theme.DEFAULT_BACKGROUND_SLUG.equals(this.cacheImage.filter), 0L, this.cacheImage.imageLocation.document, null, null, seekTo, this.cacheImage.currentAccount, false, w6, h6);
                        fileDrawable2.setIsWebmSticker(MessageObject.isWebM(this.cacheImage.imageLocation.document) || MessageObject.isVideoSticker(this.cacheImage.imageLocation.document) || ImageLoader.this.isAnimatedAvatar(this.cacheImage.filter));
                        fileDrawable = fileDrawable2;
                    }
                    fileDrawable.setLimitFps(limitFps2);
                    Thread.interrupted();
                    onPostExecute(fileDrawable);
                    return;
                } else {
                    Long mediaId = null;
                    boolean mediaIsVideo2 = false;
                    image = null;
                    String str = null;
                    cacheFileFinal = this.cacheImage.finalFilePath;
                    boolean inEncryptedFile = (this.cacheImage.secureDocument == null && (this.cacheImage.encryptionKeyPath == null || cacheFileFinal == null || !cacheFileFinal.getAbsolutePath().endsWith(".enc"))) ? false : true;
                    if (this.cacheImage.secureDocument != null) {
                        SecureDocumentKey secureDocumentKey2 = this.cacheImage.secureDocument.secureDocumentKey;
                        if (this.cacheImage.secureDocument.secureFile == null || this.cacheImage.secureDocument.secureFile.file_hash == null) {
                            secureDocumentHash = this.cacheImage.secureDocument.fileHash;
                            secureDocumentKey = secureDocumentKey2;
                        } else {
                            secureDocumentHash = this.cacheImage.secureDocument.secureFile.file_hash;
                            secureDocumentKey = secureDocumentKey2;
                        }
                    } else {
                        secureDocumentHash = null;
                        secureDocumentKey = null;
                    }
                    boolean canDeleteFile2 = true;
                    String str2 = null;
                    r15 = 0;
                    ?? r15 = 0;
                    str2 = null;
                    str2 = null;
                    str2 = null;
                    if (Build.VERSION.SDK_INT < 19) {
                        RandomAccessFile randomAccessFile2 = null;
                        try {
                            try {
                                try {
                                    randomAccessFile2 = new RandomAccessFile(cacheFileFinal, "r");
                                    byte[] bytes2 = this.cacheImage.type == 1 ? ImageLoader.headerThumb : ImageLoader.header;
                                    randomAccessFile2.readFully(bytes2, 0, bytes2.length);
                                    String str3 = new String(bytes2).toLowerCase().toLowerCase();
                                    if (str3.startsWith("riff")) {
                                        if (str3.endsWith("webp")) {
                                            r15 = 1;
                                        }
                                    }
                                    randomAccessFile2.close();
                                    str2 = r15;
                                } catch (Exception e6) {
                                    FileLog.e(e6);
                                    if (randomAccessFile2 != null) {
                                        randomAccessFile2.close();
                                    }
                                }
                            } catch (Exception e7) {
                                FileLog.e(e7);
                            }
                        } finally {
                        }
                    }
                    String mediaThumbPath = null;
                    if (this.cacheImage.imageLocation.path != null) {
                        String location2 = this.cacheImage.imageLocation.path;
                        if (location2.startsWith("thumb://")) {
                            int idx = location2.indexOf(Constants.COMMON_SCHEMA_PREFIX_SEPARATOR, 8);
                            if (idx >= 0) {
                                mediaId = Long.valueOf(Long.parseLong(location2.substring(8, idx)));
                                mediaIsVideo2 = false;
                                mediaThumbPath = location2.substring(idx + 1);
                            }
                            canDeleteFile2 = false;
                            location = mediaThumbPath;
                        } else if (location2.startsWith("vthumb://")) {
                            int idx2 = location2.indexOf(Constants.COMMON_SCHEMA_PREFIX_SEPARATOR, 9);
                            if (idx2 >= 0) {
                                mediaId = Long.valueOf(Long.parseLong(location2.substring(9, idx2)));
                                mediaIsVideo2 = true;
                            }
                            canDeleteFile2 = false;
                            location = null;
                        } else if (!location2.startsWith("http")) {
                            canDeleteFile2 = false;
                            location = null;
                        }
                        opts = new BitmapFactory.Options();
                        opts.inSampleSize = 1;
                        if (Build.VERSION.SDK_INT < 21) {
                            opts.inPurgeable = true;
                        }
                        float w_filter6 = 0.0f;
                        float h_filter7 = 0.0f;
                        Bitmap image3 = null;
                        checkInversion = false;
                        boolean force8888 = ImageLoader.this.canForce8888;
                        if (this.cacheImage.filter == null) {
                            image2 = null;
                            try {
                                String[] args5 = this.cacheImage.filter.split("_");
                                needInvert = false;
                                if (args5.length >= 2) {
                                    try {
                                        w_filter6 = Float.parseFloat(args5[0]) * AndroidUtilities.density;
                                        h_filter7 = Float.parseFloat(args5[1]) * AndroidUtilities.density;
                                    } catch (Throwable th3) {
                                        e2 = th3;
                                        mediaIsVideo = mediaIsVideo2;
                                        orientation = 0;
                                        canDeleteFile = canDeleteFile2;
                                        str = str2;
                                        image = null;
                                        boolean sentLog = true;
                                        if (e2 instanceof FileNotFoundException) {
                                            sentLog = false;
                                        }
                                        FileLog.e(e2, sentLog);
                                        h_filter = h_filter7;
                                        bitmap = image3;
                                        if (this.cacheImage.type == 1) {
                                        }
                                        Thread.interrupted();
                                        if (!needInvert2) {
                                        }
                                        onPostExecute(image != null ? new ExtendedBitmapDrawable(image, needInvert2, blurType) : null);
                                    }
                                }
                                try {
                                    if (this.cacheImage.filter.contains("b2")) {
                                        image3 = 3;
                                    } else if (this.cacheImage.filter.contains("b1")) {
                                        image3 = 2;
                                    } else if (this.cacheImage.filter.contains("b")) {
                                        image3 = 1;
                                    }
                                    if (this.cacheImage.filter.contains("i")) {
                                        checkInversion = true;
                                    }
                                    str = "f";
                                    if (this.cacheImage.filter.contains(str)) {
                                        force8888 = true;
                                    }
                                    if (str2 != null || w_filter6 == 0.0f || h_filter7 == 0.0f) {
                                        mediaIsVideo = mediaIsVideo2;
                                        orientation = 0;
                                        canDeleteFile = canDeleteFile2;
                                        str = str2;
                                    } else {
                                        opts.inJustDecodeBounds = true;
                                        try {
                                            if (mediaId == null || location != null) {
                                                orientation = 0;
                                                canDeleteFile = canDeleteFile2;
                                                str = str2;
                                                if (secureDocumentKey != null) {
                                                    try {
                                                        RandomAccessFile f = new RandomAccessFile(cacheFileFinal, "r");
                                                        int len = (int) f.length();
                                                        byte[] bytes3 = (byte[]) ImageLoader.bytesLocal.get();
                                                        byte[] data = (bytes3 == null || bytes3.length < len) ? null : bytes3;
                                                        if (data == null) {
                                                            byte[] bArr = new byte[len];
                                                            data = bArr;
                                                            bytes3 = bArr;
                                                            ImageLoader.bytesLocal.set(bytes3);
                                                        }
                                                        f.readFully(data, 0, len);
                                                        f.close();
                                                        EncryptedFileInputStream.decryptBytesWithKeyFile(data, 0, len, secureDocumentKey);
                                                        mediaIsVideo = mediaIsVideo2;
                                                        byte[] hash = Utilities.computeSHA256(data, 0, len);
                                                        boolean error = false;
                                                        if (secureDocumentHash == null || !Arrays.equals(hash, secureDocumentHash)) {
                                                            error = true;
                                                        }
                                                        int offset = data[0] & 255;
                                                        int len2 = len - offset;
                                                        if (!error) {
                                                            BitmapFactory.decodeByteArray(data, offset, len2, opts);
                                                        }
                                                    } catch (Throwable th4) {
                                                        e2 = th4;
                                                        mediaIsVideo = mediaIsVideo2;
                                                        image = null;
                                                        boolean sentLog2 = true;
                                                        if (e2 instanceof FileNotFoundException) {
                                                        }
                                                        FileLog.e(e2, sentLog2);
                                                        h_filter = h_filter7;
                                                        bitmap = image3;
                                                        if (this.cacheImage.type == 1) {
                                                        }
                                                        Thread.interrupted();
                                                        if (!needInvert2) {
                                                        }
                                                        onPostExecute(image != null ? new ExtendedBitmapDrawable(image, needInvert2, blurType) : null);
                                                    }
                                                } else {
                                                    mediaIsVideo = mediaIsVideo2;
                                                    FileInputStream is2 = inEncryptedFile ? new EncryptedFileInputStream(cacheFileFinal, this.cacheImage.encryptionKeyPath) : new FileInputStream(cacheFileFinal);
                                                    BitmapFactory.decodeStream(is2, null, opts);
                                                    is2.close();
                                                }
                                            } else if (mediaIsVideo2) {
                                                try {
                                                    canDeleteFile = canDeleteFile2;
                                                    str = str2;
                                                    try {
                                                        orientation = 0;
                                                        MediaStore.Video.Thumbnails.getThumbnail(ApplicationLoader.applicationContext.getContentResolver(), mediaId.longValue(), 1, opts);
                                                        mediaIsVideo = mediaIsVideo2;
                                                    } catch (Throwable th5) {
                                                        e2 = th5;
                                                        orientation = 0;
                                                        mediaIsVideo = mediaIsVideo2;
                                                        image = null;
                                                        boolean sentLog22 = true;
                                                        if (e2 instanceof FileNotFoundException) {
                                                        }
                                                        FileLog.e(e2, sentLog22);
                                                        h_filter = h_filter7;
                                                        bitmap = image3;
                                                        if (this.cacheImage.type == 1) {
                                                        }
                                                        Thread.interrupted();
                                                        if (!needInvert2) {
                                                        }
                                                        onPostExecute(image != null ? new ExtendedBitmapDrawable(image, needInvert2, blurType) : null);
                                                    }
                                                } catch (Throwable th6) {
                                                    e2 = th6;
                                                    orientation = 0;
                                                    canDeleteFile = canDeleteFile2;
                                                    str = str2;
                                                    mediaIsVideo = mediaIsVideo2;
                                                    image = null;
                                                }
                                            } else {
                                                orientation = 0;
                                                canDeleteFile = canDeleteFile2;
                                                str = str2;
                                                MediaStore.Images.Thumbnails.getThumbnail(ApplicationLoader.applicationContext.getContentResolver(), mediaId.longValue(), 1, opts);
                                                mediaIsVideo = mediaIsVideo2;
                                            }
                                            float photoW = opts.outWidth;
                                            float photoH = opts.outHeight;
                                            float scaleFactor = (w_filter6 < h_filter7 || photoW <= photoH) ? Math.min(photoW / w_filter6, photoH / h_filter7) : Math.max(photoW / w_filter6, photoH / h_filter7);
                                            if (scaleFactor < 1.2f) {
                                                scaleFactor = 1.0f;
                                            }
                                            opts.inJustDecodeBounds = false;
                                            if (scaleFactor <= 1.0f || (photoW <= w_filter6 && photoH <= h_filter7)) {
                                                opts.inSampleSize = (int) scaleFactor;
                                            } else {
                                                int sample = 1;
                                                do {
                                                    sample *= 2;
                                                } while (sample * 2 < scaleFactor);
                                                opts.inSampleSize = sample;
                                            }
                                        } catch (Throwable th7) {
                                            e2 = th7;
                                            mediaIsVideo = mediaIsVideo2;
                                            image = null;
                                        }
                                    }
                                } catch (Throwable th8) {
                                    e2 = th8;
                                    mediaIsVideo = mediaIsVideo2;
                                    orientation = 0;
                                    canDeleteFile = canDeleteFile2;
                                    str = str2;
                                    image = null;
                                }
                            } catch (Throwable th9) {
                                e2 = th9;
                                mediaIsVideo = mediaIsVideo2;
                                needInvert = false;
                                orientation = 0;
                                canDeleteFile = canDeleteFile2;
                                str = str2;
                                image = null;
                            }
                        } else {
                            mediaIsVideo = mediaIsVideo2;
                            image2 = null;
                            needInvert = false;
                            orientation = 0;
                            canDeleteFile = canDeleteFile2;
                            str = str2;
                            if (location != null) {
                                opts.inJustDecodeBounds = true;
                                opts.inPreferredConfig = force8888 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
                                FileInputStream is3 = new FileInputStream(cacheFileFinal);
                                image = BitmapFactory.decodeStream(is3, null, opts);
                                try {
                                    is3.close();
                                    ?? r5 = opts.outWidth;
                                    int photoH2 = opts.outHeight;
                                    opts.inJustDecodeBounds = false;
                                    int screenSize = Math.max(66, Math.min(AndroidUtilities.getRealScreenSize().x, AndroidUtilities.getRealScreenSize().y));
                                    float scaleFactor2 = (Math.min(photoH2, (int) r5) / screenSize) * 6.0f;
                                    if (scaleFactor2 < 1.0f) {
                                        scaleFactor2 = 1.0f;
                                    }
                                    if (scaleFactor2 > 1.0f) {
                                        int sample2 = 1;
                                        Bitmap bitmap4 = r5;
                                        while (true) {
                                            sample2 *= 2;
                                            image2 = bitmap4;
                                            int photoW2 = sample2 * 2;
                                            if (photoW2 > scaleFactor2) {
                                                break;
                                            }
                                            bitmap4 = image2;
                                        }
                                        opts.inSampleSize = sample2;
                                    } else {
                                        image2 = r5;
                                        opts.inSampleSize = (int) scaleFactor2;
                                    }
                                    h_filter = h_filter7;
                                    bitmap = image3;
                                } catch (Throwable th10) {
                                    e2 = th10;
                                    boolean sentLog222 = true;
                                    if (e2 instanceof FileNotFoundException) {
                                    }
                                    FileLog.e(e2, sentLog222);
                                    h_filter = h_filter7;
                                    bitmap = image3;
                                    if (this.cacheImage.type == 1) {
                                    }
                                    Thread.interrupted();
                                    if (!needInvert2) {
                                    }
                                    onPostExecute(image != null ? new ExtendedBitmapDrawable(image, needInvert2, blurType) : null);
                                }
                                if (this.cacheImage.type == 1) {
                                    try {
                                        ImageLoader.this.lastCacheOutTime = SystemClock.elapsedRealtime();
                                        try {
                                        } catch (Throwable th11) {
                                            e = th11;
                                            image = image3;
                                            needInvert2 = needInvert;
                                            image2 = image2;
                                        }
                                    } catch (Throwable th12) {
                                        e = th12;
                                        image2 = h_filter;
                                        needInvert2 = needInvert;
                                    }
                                    synchronized (this.sync) {
                                        try {
                                            if (this.isCancelled) {
                                                try {
                                                    return;
                                                } catch (Throwable th13) {
                                                    th = th13;
                                                    while (true) {
                                                        try {
                                                            break;
                                                        } catch (Throwable th14) {
                                                            th = th14;
                                                        }
                                                    }
                                                    throw th;
                                                }
                                            }
                                            try {
                                                try {
                                                } catch (Throwable th15) {
                                                    e = th15;
                                                    needInvert2 = needInvert;
                                                    image2 = image2;
                                                }
                                            } catch (Throwable th16) {
                                                e = th16;
                                                image = image3;
                                                needInvert2 = needInvert;
                                                image2 = image2;
                                            }
                                            if (str != null) {
                                                try {
                                                    RandomAccessFile file2 = new RandomAccessFile(cacheFileFinal, "r");
                                                    ByteBuffer buffer = file2.getChannel().map(FileChannel.MapMode.READ_ONLY, 0L, cacheFileFinal.length());
                                                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                                    Bitmap image4 = image;
                                                    try {
                                                        bmOptions.inJustDecodeBounds = true;
                                                        image2 = h_filter;
                                                        Utilities.loadWebpImage(null, buffer, buffer.limit(), bmOptions, true);
                                                        image = Bitmaps.createBitmap(bmOptions.outWidth, bmOptions.outHeight, Bitmap.Config.ARGB_8888);
                                                        Utilities.loadWebpImage(image, buffer, buffer.limit(), null, !opts.inPurgeable);
                                                        file2.close();
                                                    } catch (Throwable th17) {
                                                        e = th17;
                                                        image2 = h_filter;
                                                        image = image4;
                                                        needInvert2 = needInvert;
                                                        FileLog.e(e);
                                                        blurType = orientation;
                                                        Thread.interrupted();
                                                        if (!needInvert2) {
                                                        }
                                                        onPostExecute(image != null ? new ExtendedBitmapDrawable(image, needInvert2, blurType) : null);
                                                    }
                                                } catch (Throwable th18) {
                                                    e = th18;
                                                    image2 = h_filter;
                                                    needInvert2 = needInvert;
                                                }
                                            } else {
                                                image2 = h_filter;
                                                Bitmap image5 = image;
                                                try {
                                                } catch (Throwable th19) {
                                                    e = th19;
                                                    image = image5;
                                                    needInvert2 = needInvert;
                                                    image2 = image2;
                                                    FileLog.e(e);
                                                    blurType = orientation;
                                                    Thread.interrupted();
                                                    if (!needInvert2) {
                                                    }
                                                    onPostExecute(image != null ? new ExtendedBitmapDrawable(image, needInvert2, blurType) : null);
                                                }
                                                if (opts.inPurgeable || secureDocumentKey != null) {
                                                    RandomAccessFile f2 = new RandomAccessFile(cacheFileFinal, "r");
                                                    int len3 = (int) f2.length();
                                                    int offset2 = 0;
                                                    byte[] bytesThumb = (byte[]) ImageLoader.bytesThumbLocal.get();
                                                    byte[] data2 = (bytesThumb == null || bytesThumb.length < len3) ? null : bytesThumb;
                                                    if (data2 == null) {
                                                        byte[] bytesThumb2 = new byte[len3];
                                                        data2 = bytesThumb2;
                                                        ImageLoader.bytesThumbLocal.set(bytesThumb2);
                                                    }
                                                    f2.readFully(data2, 0, len3);
                                                    f2.close();
                                                    boolean error2 = false;
                                                    if (secureDocumentKey != null) {
                                                        EncryptedFileInputStream.decryptBytesWithKeyFile(data2, 0, len3, secureDocumentKey);
                                                        byte[] hash2 = Utilities.computeSHA256(data2, 0, len3);
                                                        if (secureDocumentHash == null || !Arrays.equals(hash2, secureDocumentHash)) {
                                                            error2 = true;
                                                        }
                                                        offset2 = data2[0] & 255;
                                                        len3 -= offset2;
                                                    } else if (inEncryptedFile) {
                                                        EncryptedFileInputStream.decryptBytesWithKeyFile(data2, 0, len3, this.cacheImage.encryptionKeyPath);
                                                        image = error2 ? BitmapFactory.decodeByteArray(data2, offset2, len3, opts) : image5;
                                                        if (image == null) {
                                                            if (cacheFileFinal.length() == 0 || this.cacheImage.filter == null) {
                                                                cacheFileFinal.delete();
                                                            }
                                                            needInvert2 = needInvert;
                                                        } else {
                                                            if (this.cacheImage.filter != null) {
                                                                float bitmapW = image.getWidth();
                                                                float bitmapH = image.getHeight();
                                                                if (!opts.inPurgeable && w_filter6 != 0.0f && bitmapW != w_filter6 && bitmapW > w_filter6 + 20.0f && image != (scaledBitmap = Bitmaps.createScaledBitmap(image, (int) w_filter6, (int) (bitmapH / (bitmapW / w_filter6)), true))) {
                                                                    image.recycle();
                                                                    image = scaledBitmap;
                                                                }
                                                            }
                                                            if (checkInversion) {
                                                                needInvert2 = Utilities.needInvert(image, opts.inPurgeable ? 0 : 1, image.getWidth(), image.getHeight(), image.getRowBytes()) != 0;
                                                            } else {
                                                                needInvert2 = needInvert;
                                                            }
                                                            try {
                                                                if (bitmap == 1) {
                                                                    if (image.getConfig() == Bitmap.Config.ARGB_8888) {
                                                                        Utilities.blurBitmap(image, 3, opts.inPurgeable ? 0 : 1, image.getWidth(), image.getHeight(), image.getRowBytes());
                                                                    }
                                                                } else if (bitmap == 2) {
                                                                    if (image.getConfig() == Bitmap.Config.ARGB_8888) {
                                                                        Utilities.blurBitmap(image, 1, opts.inPurgeable ? 0 : 1, image.getWidth(), image.getHeight(), image.getRowBytes());
                                                                    }
                                                                } else if (bitmap == 3) {
                                                                    if (image.getConfig() == Bitmap.Config.ARGB_8888) {
                                                                        Utilities.blurBitmap(image, 7, opts.inPurgeable ? 0 : 1, image.getWidth(), image.getHeight(), image.getRowBytes());
                                                                        Utilities.blurBitmap(image, 7, opts.inPurgeable ? 0 : 1, image.getWidth(), image.getHeight(), image.getRowBytes());
                                                                        Utilities.blurBitmap(image, 7, opts.inPurgeable ? 0 : 1, image.getWidth(), image.getHeight(), image.getRowBytes());
                                                                    }
                                                                } else if (bitmap == null) {
                                                                    if (opts.inPurgeable) {
                                                                        Utilities.pinBitmap(image);
                                                                    }
                                                                }
                                                            } catch (Throwable th20) {
                                                                e = th20;
                                                                image2 = image2;
                                                                FileLog.e(e);
                                                                blurType = orientation;
                                                                Thread.interrupted();
                                                                if (!needInvert2) {
                                                                }
                                                                onPostExecute(image != null ? new ExtendedBitmapDrawable(image, needInvert2, blurType) : null);
                                                            }
                                                        }
                                                        blurType = orientation;
                                                    }
                                                    if (error2) {
                                                    }
                                                    if (image == null) {
                                                    }
                                                    blurType = orientation;
                                                } else {
                                                    FileInputStream is4 = inEncryptedFile ? new EncryptedFileInputStream(cacheFileFinal, this.cacheImage.encryptionKeyPath) : new FileInputStream(cacheFileFinal);
                                                    image = BitmapFactory.decodeStream(is4, null, opts);
                                                    is4.close();
                                                    image2 = image2;
                                                }
                                            }
                                            if (image == null) {
                                            }
                                            blurType = orientation;
                                        } catch (Throwable th21) {
                                            th = th21;
                                        }
                                    }
                                } else {
                                    String mediaThumbPath2 = location;
                                    Long mediaId2 = mediaId;
                                    h_filter2 = h_filter;
                                    Bitmap image6 = image;
                                    String str4 = str;
                                    int delay = mediaId2 != null ? 0 : 20;
                                    try {
                                        if (delay != 0) {
                                            try {
                                                if (ImageLoader.this.lastCacheOutTime != 0) {
                                                    w_filter = w_filter6;
                                                    if (ImageLoader.this.lastCacheOutTime > SystemClock.elapsedRealtime() - delay) {
                                                        try {
                                                            if (Build.VERSION.SDK_INT < 21) {
                                                                Thread.sleep(delay);
                                                            }
                                                        } catch (Throwable th22) {
                                                            image = image6;
                                                            needInvert2 = needInvert;
                                                            blurType = orientation;
                                                        }
                                                    }
                                                    ImageLoader.this.lastCacheOutTime = SystemClock.elapsedRealtime();
                                                    synchronized (this.sync) {
                                                        try {
                                                            if (this.isCancelled) {
                                                                try {
                                                                    return;
                                                                } catch (Throwable th23) {
                                                                    th2 = th23;
                                                                    while (true) {
                                                                        try {
                                                                            break;
                                                                        } catch (Throwable th24) {
                                                                            th2 = th24;
                                                                        }
                                                                    }
                                                                    throw th2;
                                                                }
                                                            }
                                                        } catch (Throwable th25) {
                                                            th2 = th25;
                                                        }
                                                    }
                                                    try {
                                                        break;
                                                        throw th2;
                                                    } catch (Throwable th26) {
                                                        image = image6;
                                                        needInvert2 = needInvert;
                                                        blurType = orientation;
                                                    }
                                                }
                                            } catch (Throwable th27) {
                                                image = image6;
                                                needInvert2 = needInvert;
                                                blurType = orientation;
                                            }
                                        }
                                        ImageLoader.this.lastCacheOutTime = SystemClock.elapsedRealtime();
                                        synchronized (this.sync) {
                                        }
                                    } catch (Throwable th28) {
                                        image = image6;
                                        needInvert2 = needInvert;
                                        blurType = orientation;
                                    }
                                    w_filter = w_filter6;
                                }
                                Thread.interrupted();
                                if (!needInvert2 || blurType != 0) {
                                    onPostExecute(image != null ? new ExtendedBitmapDrawable(image, needInvert2, blurType) : null);
                                } else {
                                    onPostExecute(image != null ? new BitmapDrawable(image) : null);
                                    return;
                                }
                            }
                        }
                        image = image2;
                        h_filter = h_filter7;
                        bitmap = image3;
                        if (this.cacheImage.type == 1) {
                        }
                        Thread.interrupted();
                        if (!needInvert2) {
                        }
                        onPostExecute(image != null ? new ExtendedBitmapDrawable(image, needInvert2, blurType) : null);
                    }
                    location = null;
                    opts = new BitmapFactory.Options();
                    opts.inSampleSize = 1;
                    if (Build.VERSION.SDK_INT < 21) {
                    }
                    float w_filter62 = 0.0f;
                    float h_filter72 = 0.0f;
                    Bitmap image32 = null;
                    checkInversion = false;
                    boolean force88882 = ImageLoader.this.canForce8888;
                    if (this.cacheImage.filter == null) {
                    }
                    image = image2;
                    h_filter = h_filter72;
                    bitmap = image32;
                    if (this.cacheImage.type == 1) {
                    }
                    Thread.interrupted();
                    if (!needInvert2) {
                    }
                    onPostExecute(image != null ? new ExtendedBitmapDrawable(image, needInvert2, blurType) : null);
                }
            }
            is.getChannel().position(0L);
            image = BitmapFactory.decodeStream(is, null, opts);
            is.close();
            Bitmap bitmap5 = bitmap;
            blurType = orientation;
            try {
                if (image == null) {
                    if (canDeleteFile && (cacheFileFinal.length() == 0 || this.cacheImage.filter == null)) {
                        cacheFileFinal.delete();
                    }
                    orientation2 = blurType;
                    needInvert2 = needInvert;
                } else {
                    boolean blured2 = false;
                    try {
                        if (this.cacheImage.filter != null) {
                            float bitmapW2 = image.getWidth();
                            float bitmapH2 = image.getHeight();
                            if (!opts.inPurgeable && w_filter != 0.0f && bitmapW2 != w_filter && bitmapW2 > 20.0f + w_filter) {
                                if (bitmapW2 <= bitmapH2 || w_filter <= h_filter2) {
                                    float scaleFactor3 = bitmapH2 / h_filter2;
                                    if (scaleFactor3 > 1.0f) {
                                        try {
                                            scaledBitmap2 = Bitmaps.createScaledBitmap(image, (int) (bitmapW2 / scaleFactor3), (int) h_filter2, true);
                                        } catch (Throwable th29) {
                                            needInvert2 = needInvert;
                                        }
                                    } else {
                                        scaledBitmap2 = image;
                                    }
                                } else {
                                    float scaleFactor4 = bitmapW2 / w_filter;
                                    scaledBitmap2 = scaleFactor4 > 1.0f ? Bitmaps.createScaledBitmap(image, (int) w_filter, (int) (bitmapH2 / scaleFactor4), true) : image;
                                }
                                if (image != scaledBitmap2) {
                                    image.recycle();
                                    image = scaledBitmap2;
                                }
                            }
                            if (image != null) {
                                if (checkInversion) {
                                    Bitmap b = image;
                                    try {
                                        int w7 = image.getWidth();
                                        int h7 = image.getHeight();
                                        blured = false;
                                        Bitmap b2 = w7 * h7 > 22500 ? Bitmaps.createScaledBitmap(image, 100, 100, false) : b;
                                        int i = opts.inPurgeable ? 0 : 1;
                                        try {
                                            int width = b2.getWidth();
                                            int w8 = b2.getHeight();
                                            orientation2 = blurType;
                                            try {
                                                needInvert3 = Utilities.needInvert(b2, i, width, w8, b2.getRowBytes()) != 0;
                                                if (b2 != image) {
                                                    try {
                                                        b2.recycle();
                                                    } catch (Throwable th30) {
                                                        needInvert2 = needInvert3;
                                                        blurType = orientation2;
                                                    }
                                                }
                                            } catch (Throwable th31) {
                                                blurType = orientation2;
                                                needInvert2 = needInvert;
                                            }
                                        } catch (Throwable th32) {
                                            needInvert2 = needInvert;
                                        }
                                    } catch (Throwable th33) {
                                        needInvert2 = needInvert;
                                    }
                                } else {
                                    blured = false;
                                    orientation2 = blurType;
                                    needInvert3 = needInvert;
                                }
                                if (bitmap5 != null && (bitmapH2 > 100.0f || bitmapW2 > 100.0f)) {
                                    bitmapH2 = 80.0f;
                                    bitmapW2 = 80.0f;
                                    image = Bitmaps.createScaledBitmap(image, 80, 80, false);
                                }
                                if (bitmap5 == null || bitmapH2 >= 100.0f || bitmapW2 >= 100.0f) {
                                    needInvert2 = needInvert3;
                                    blured2 = blured;
                                } else {
                                    if (image.getConfig() == Bitmap.Config.ARGB_8888) {
                                        Utilities.blurBitmap(image, 3, opts.inPurgeable ? 0 : 1, image.getWidth(), image.getHeight(), image.getRowBytes());
                                    }
                                    blured2 = true;
                                    needInvert2 = needInvert3;
                                }
                            } else {
                                orientation2 = blurType;
                                needInvert2 = needInvert;
                            }
                        } else {
                            orientation2 = blurType;
                            needInvert2 = needInvert;
                        }
                        if (!blured2) {
                            try {
                                if (opts.inPurgeable) {
                                    Utilities.pinBitmap(image);
                                }
                            } catch (Throwable th34) {
                                blurType = orientation2;
                            }
                        }
                    } catch (Throwable th35) {
                        needInvert2 = needInvert;
                    }
                }
                blurType = orientation2;
            } catch (Throwable th36) {
                needInvert2 = needInvert;
            }
            Thread.interrupted();
            if (!needInvert2) {
            }
            onPostExecute(image != null ? new ExtendedBitmapDrawable(image, needInvert2, blurType) : null);
        }

        private void loadLastFrame(final RLottieDrawable lottieDrawable, int w, int h) {
            final Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(bitmap);
            canvas.scale(2.0f, 2.0f, w / 2.0f, h / 2.0f);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$CacheOutTask$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.CacheOutTask.this.m304x8c05a141(lottieDrawable, canvas, bitmap);
                }
            });
        }

        /* renamed from: lambda$loadLastFrame$1$org-telegram-messenger-ImageLoader$CacheOutTask */
        public /* synthetic */ void m304x8c05a141(final RLottieDrawable lottieDrawable, final Canvas canvas, final Bitmap bitmap) {
            lottieDrawable.setOnFrameReadyRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$CacheOutTask$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.CacheOutTask.this.m303x8c7c0740(lottieDrawable, canvas, bitmap);
                }
            });
            lottieDrawable.setCurrentFrame(lottieDrawable.getFramesCount() - 1, true, true);
        }

        /* renamed from: lambda$loadLastFrame$0$org-telegram-messenger-ImageLoader$CacheOutTask */
        public /* synthetic */ void m303x8c7c0740(RLottieDrawable lottieDrawable, Canvas canvas, Bitmap bitmap) {
            lottieDrawable.setOnFrameReadyRunnable(null);
            BitmapDrawable bitmapDrawable = null;
            if (lottieDrawable.getBackgroundBitmap() != null || lottieDrawable.getRenderingBitmap() != null) {
                Bitmap currentBitmap = lottieDrawable.getBackgroundBitmap() != null ? lottieDrawable.getBackgroundBitmap() : lottieDrawable.getRenderingBitmap();
                canvas.drawBitmap(currentBitmap, 0.0f, 0.0f, (Paint) null);
                bitmapDrawable = new BitmapDrawable(bitmap);
            }
            onPostExecute(bitmapDrawable);
            lottieDrawable.recycle();
        }

        private void onPostExecute(final Drawable drawable) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$CacheOutTask$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.CacheOutTask.this.m306xba899b28(drawable);
                }
            });
        }

        /* renamed from: lambda$onPostExecute$3$org-telegram-messenger-ImageLoader$CacheOutTask */
        public /* synthetic */ void m306xba899b28(Drawable drawable) {
            Drawable toSet = null;
            String decrementKey = null;
            if (drawable instanceof RLottieDrawable) {
                RLottieDrawable lottieDrawable = (RLottieDrawable) drawable;
                toSet = (Drawable) ImageLoader.this.lottieMemCache.get(this.cacheImage.key);
                if (toSet == null) {
                    ImageLoader.this.lottieMemCache.put(this.cacheImage.key, lottieDrawable);
                    toSet = lottieDrawable;
                } else {
                    lottieDrawable.recycle();
                }
                if (toSet != null) {
                    ImageLoader.this.incrementUseCount(this.cacheImage.key);
                    decrementKey = this.cacheImage.key;
                }
            } else if (drawable instanceof AnimatedFileDrawable) {
                AnimatedFileDrawable animatedFileDrawable = (AnimatedFileDrawable) drawable;
                if (animatedFileDrawable.isWebmSticker) {
                    toSet = ImageLoader.this.getFromLottieCache(this.cacheImage.key);
                    if (toSet == null) {
                        ImageLoader.this.lottieMemCache.put(this.cacheImage.key, animatedFileDrawable);
                        toSet = animatedFileDrawable;
                    } else {
                        animatedFileDrawable.recycle();
                    }
                    ImageLoader.this.incrementUseCount(this.cacheImage.key);
                    decrementKey = this.cacheImage.key;
                } else {
                    toSet = drawable;
                }
            } else if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                toSet = ImageLoader.this.getFromMemCache(this.cacheImage.key);
                boolean incrementUseCount = true;
                if (toSet == null) {
                    if (this.cacheImage.key.endsWith("_f")) {
                        ImageLoader.this.wallpaperMemCache.put(this.cacheImage.key, bitmapDrawable);
                        incrementUseCount = false;
                    } else if (this.cacheImage.key.endsWith("_isc") || bitmapDrawable.getBitmap().getWidth() > AndroidUtilities.density * 80.0f || bitmapDrawable.getBitmap().getHeight() > AndroidUtilities.density * 80.0f) {
                        ImageLoader.this.memCache.put(this.cacheImage.key, bitmapDrawable);
                    } else {
                        ImageLoader.this.smallImagesMemCache.put(this.cacheImage.key, bitmapDrawable);
                    }
                    toSet = bitmapDrawable;
                } else {
                    Bitmap image = bitmapDrawable.getBitmap();
                    image.recycle();
                }
                if (toSet != null && incrementUseCount) {
                    ImageLoader.this.incrementUseCount(this.cacheImage.key);
                    decrementKey = this.cacheImage.key;
                }
            }
            final Drawable toSetFinal = toSet;
            final String decrementKetFinal = decrementKey;
            ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$CacheOutTask$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.CacheOutTask.this.m305xbb000127(toSetFinal, decrementKetFinal);
                }
            });
        }

        /* renamed from: lambda$onPostExecute$2$org-telegram-messenger-ImageLoader$CacheOutTask */
        public /* synthetic */ void m305xbb000127(Drawable toSetFinal, String decrementKetFinal) {
            this.cacheImage.setImageAndClear(toSetFinal, decrementKetFinal);
        }

        public void cancel() {
            synchronized (this.sync) {
                try {
                    this.isCancelled = true;
                    Thread thread = this.runningThread;
                    if (thread != null) {
                        thread.interrupt();
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public boolean isAnimatedAvatar(String filter) {
        return filter != null && filter.endsWith("avatar");
    }

    public BitmapDrawable getFromMemCache(String key) {
        BitmapDrawable drawable = this.memCache.get(key);
        if (drawable == null) {
            drawable = this.smallImagesMemCache.get(key);
        }
        if (drawable == null) {
            drawable = this.wallpaperMemCache.get(key);
        }
        if (drawable == null) {
            return getFromLottieCache(key);
        }
        return drawable;
    }

    public static Bitmap getStrippedPhotoBitmap(byte[] photoBytes, String filter) {
        int len = (photoBytes.length - 3) + Bitmaps.header.length + Bitmaps.footer.length;
        byte[] bytes = bytesLocal.get();
        byte[] data = (bytes == null || bytes.length < len) ? null : bytes;
        if (data == null) {
            byte[] bytes2 = new byte[len];
            data = bytes2;
            bytesLocal.set(bytes2);
        }
        System.arraycopy(Bitmaps.header, 0, data, 0, Bitmaps.header.length);
        System.arraycopy(photoBytes, 3, data, Bitmaps.header.length, photoBytes.length - 3);
        System.arraycopy(Bitmaps.footer, 0, data, (Bitmaps.header.length + photoBytes.length) - 3, Bitmaps.footer.length);
        data[164] = photoBytes[1];
        data[166] = photoBytes[2];
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, len);
        if (bitmap != null && !TextUtils.isEmpty(filter) && filter.contains("b")) {
            Utilities.blurBitmap(bitmap, 3, 1, bitmap.getWidth(), bitmap.getHeight(), bitmap.getRowBytes());
        }
        return bitmap;
    }

    /* loaded from: classes4.dex */
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

        public void addImageReceiver(ImageReceiver imageReceiver, String key, String filter, int type, int guid) {
            int index = this.imageReceiverArray.indexOf(imageReceiver);
            if (index >= 0) {
                this.imageReceiverGuidsArray.set(index, Integer.valueOf(guid));
                return;
            }
            this.imageReceiverArray.add(imageReceiver);
            this.imageReceiverGuidsArray.add(Integer.valueOf(guid));
            this.keys.add(key);
            this.filters.add(filter);
            this.types.add(Integer.valueOf(type));
            ImageLoader.this.imageLoadingByTag.put(imageReceiver.getTag(type), this);
        }

        public void replaceImageReceiver(ImageReceiver imageReceiver, String key, String filter, int type, int guid) {
            int index = this.imageReceiverArray.indexOf(imageReceiver);
            if (index == -1) {
                return;
            }
            if (this.types.get(index).intValue() != type) {
                ArrayList<ImageReceiver> arrayList = this.imageReceiverArray;
                index = arrayList.subList(index + 1, arrayList.size()).indexOf(imageReceiver);
                if (index == -1) {
                    return;
                }
            }
            this.imageReceiverGuidsArray.set(index, Integer.valueOf(guid));
            this.keys.set(index, key);
            this.filters.set(index, filter);
        }

        public void setImageReceiverGuid(ImageReceiver imageReceiver, int guid) {
            int index = this.imageReceiverArray.indexOf(imageReceiver);
            if (index == -1) {
                return;
            }
            this.imageReceiverGuidsArray.set(index, Integer.valueOf(guid));
        }

        public void removeImageReceiver(ImageReceiver imageReceiver) {
            int currentMediaType = this.type;
            int a = 0;
            while (a < this.imageReceiverArray.size()) {
                ImageReceiver obj = this.imageReceiverArray.get(a);
                if (obj == null || obj == imageReceiver) {
                    this.imageReceiverArray.remove(a);
                    this.imageReceiverGuidsArray.remove(a);
                    this.keys.remove(a);
                    this.filters.remove(a);
                    currentMediaType = this.types.remove(a).intValue();
                    if (obj != null) {
                        ImageLoader.this.imageLoadingByTag.remove(obj.getTag(currentMediaType));
                    }
                    a--;
                }
                a++;
            }
            if (this.imageReceiverArray.isEmpty()) {
                if (this.imageLocation != null && !ImageLoader.this.forceLoadingImages.containsKey(this.key)) {
                    if (this.imageLocation.location != null) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.imageLocation.location, this.ext);
                    } else if (this.imageLocation.document != null) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.imageLocation.document);
                    } else if (this.imageLocation.secureDocument != null) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.imageLocation.secureDocument);
                    } else if (this.imageLocation.webFile != null) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.imageLocation.webFile);
                    }
                }
                if (this.cacheTask != null) {
                    if (currentMediaType == 1) {
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
                if (this.key != null) {
                    ImageLoader.this.imageLoadingByKeys.remove(this.key);
                }
            }
        }

        public void setImageAndClear(final Drawable image, final String decrementKey) {
            if (image != null) {
                final ArrayList<ImageReceiver> finalImageReceiverArray = new ArrayList<>(this.imageReceiverArray);
                final ArrayList<Integer> finalImageReceiverGuidsArray = new ArrayList<>(this.imageReceiverGuidsArray);
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$CacheImage$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ImageLoader.CacheImage.this.m302x9483c100(image, finalImageReceiverArray, finalImageReceiverGuidsArray, decrementKey);
                    }
                });
            }
            for (int a = 0; a < this.imageReceiverArray.size(); a++) {
                ImageReceiver imageReceiver = this.imageReceiverArray.get(a);
                ImageLoader.this.imageLoadingByTag.remove(imageReceiver.getTag(this.type));
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

        /* renamed from: lambda$setImageAndClear$0$org-telegram-messenger-ImageLoader$CacheImage */
        public /* synthetic */ void m302x9483c100(Drawable image, ArrayList finalImageReceiverArray, ArrayList finalImageReceiverGuidsArray, String decrementKey) {
            if (image instanceof AnimatedFileDrawable) {
                boolean imageSet = false;
                AnimatedFileDrawable fileDrawable = (AnimatedFileDrawable) image;
                for (int a = 0; a < finalImageReceiverArray.size(); a++) {
                    ImageReceiver imgView = (ImageReceiver) finalImageReceiverArray.get(a);
                    if (imgView.setImageBitmapByKey(fileDrawable, this.key, this.type, false, ((Integer) finalImageReceiverGuidsArray.get(a)).intValue())) {
                        if (fileDrawable == fileDrawable) {
                            imageSet = true;
                        }
                    } else if (fileDrawable != fileDrawable) {
                        fileDrawable.recycle();
                    }
                }
                if (!imageSet) {
                    fileDrawable.recycle();
                }
            } else {
                for (int a2 = 0; a2 < finalImageReceiverArray.size(); a2++) {
                    ImageReceiver imgView2 = (ImageReceiver) finalImageReceiverArray.get(a2);
                    imgView2.setImageBitmapByKey(image, this.key, this.types.get(a2).intValue(), false, ((Integer) finalImageReceiverGuidsArray.get(a2)).intValue());
                }
            }
            if (decrementKey != null) {
                ImageLoader.this.decrementUseCount(decrementKey);
            }
        }
    }

    public static ImageLoader getInstance() {
        ImageLoader localInstance = Instance;
        if (localInstance == null) {
            synchronized (ImageLoader.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    ImageLoader imageLoader = new ImageLoader();
                    localInstance = imageLoader;
                    Instance = imageLoader;
                }
            }
        }
        return localInstance;
    }

    public ImageLoader() {
        int maxSize;
        boolean z = false;
        this.thumbGeneratingQueue.setPriority(1);
        int memoryClass = ((ActivityManager) ApplicationLoader.applicationContext.getSystemService("activity")).getMemoryClass();
        z = memoryClass >= 192 ? true : z;
        this.canForce8888 = z;
        if (z) {
            maxSize = 30;
        } else {
            maxSize = 15;
        }
        int cacheSize = Math.min(maxSize, memoryClass / 7) * 1024 * 1024;
        int commonCacheSize = (int) (cacheSize * 0.8f);
        int smallImagesCacheSize = (int) (cacheSize * 0.2f);
        this.memCache = new LruCache<BitmapDrawable>(commonCacheSize) { // from class: org.telegram.messenger.ImageLoader.1
            public int sizeOf(String key, BitmapDrawable value) {
                return value.getBitmap().getByteCount();
            }

            public void entryRemoved(boolean evicted, String key, BitmapDrawable oldValue, BitmapDrawable newValue) {
                if (ImageLoader.this.ignoreRemoval == null || !ImageLoader.this.ignoreRemoval.equals(key)) {
                    Integer count = (Integer) ImageLoader.this.bitmapUseCounts.get(key);
                    if (count == null || count.intValue() == 0) {
                        Bitmap b = oldValue.getBitmap();
                        if (!b.isRecycled()) {
                            ArrayList<Bitmap> bitmapToRecycle = new ArrayList<>();
                            bitmapToRecycle.add(b);
                            AndroidUtilities.recycleBitmaps(bitmapToRecycle);
                        }
                    }
                }
            }
        };
        this.smallImagesMemCache = new LruCache<BitmapDrawable>(smallImagesCacheSize) { // from class: org.telegram.messenger.ImageLoader.2
            public int sizeOf(String key, BitmapDrawable value) {
                return value.getBitmap().getByteCount();
            }

            public void entryRemoved(boolean evicted, String key, BitmapDrawable oldValue, BitmapDrawable newValue) {
                if (ImageLoader.this.ignoreRemoval == null || !ImageLoader.this.ignoreRemoval.equals(key)) {
                    Integer count = (Integer) ImageLoader.this.bitmapUseCounts.get(key);
                    if (count == null || count.intValue() == 0) {
                        Bitmap b = oldValue.getBitmap();
                        if (!b.isRecycled()) {
                            ArrayList<Bitmap> bitmapToRecycle = new ArrayList<>();
                            bitmapToRecycle.add(b);
                            AndroidUtilities.recycleBitmaps(bitmapToRecycle);
                        }
                    }
                }
            }
        };
        this.wallpaperMemCache = new LruCache<BitmapDrawable>(cacheSize / 4) { // from class: org.telegram.messenger.ImageLoader.3
            public int sizeOf(String key, BitmapDrawable value) {
                return value.getBitmap().getByteCount();
            }
        };
        this.lottieMemCache = new LruCache<BitmapDrawable>(10485760) { // from class: org.telegram.messenger.ImageLoader.4
            public int sizeOf(String key, BitmapDrawable value) {
                return value.getIntrinsicWidth() * value.getIntrinsicHeight() * 4 * 2;
            }

            public BitmapDrawable put(String key, BitmapDrawable value) {
                if (value instanceof AnimatedFileDrawable) {
                    ImageLoader.this.cachedAnimatedFileDrawables.add((AnimatedFileDrawable) value);
                }
                return (BitmapDrawable) super.put(key, (String) value);
            }

            public void entryRemoved(boolean evicted, String key, BitmapDrawable oldValue, BitmapDrawable newValue) {
                Integer count = (Integer) ImageLoader.this.bitmapUseCounts.get(key);
                if (oldValue instanceof AnimatedFileDrawable) {
                    ImageLoader.this.cachedAnimatedFileDrawables.remove((AnimatedFileDrawable) oldValue);
                }
                if (count == null || count.intValue() == 0) {
                    if (oldValue instanceof AnimatedFileDrawable) {
                        ((AnimatedFileDrawable) oldValue).recycle();
                    }
                    if (oldValue instanceof RLottieDrawable) {
                        ((RLottieDrawable) oldValue).recycle();
                    }
                }
            }
        };
        SparseArray<File> mediaDirs = new SparseArray<>();
        File cachePath = AndroidUtilities.getCacheDir();
        if (!cachePath.isDirectory()) {
            try {
                cachePath.mkdirs();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        AndroidUtilities.createEmptyFile(new File(cachePath, ".nomedia"));
        mediaDirs.put(4, cachePath);
        for (int a = 0; a < 4; a++) {
            int currentAccount = a;
            FileLoader.getInstance(a).setDelegate(new AnonymousClass5(currentAccount));
        }
        FileLoader.setMediaDirs(mediaDirs);
        BroadcastReceiver receiver = new AnonymousClass6();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.MEDIA_BAD_REMOVAL");
        filter.addAction("android.intent.action.MEDIA_CHECKING");
        filter.addAction("android.intent.action.MEDIA_EJECT");
        filter.addAction("android.intent.action.MEDIA_MOUNTED");
        filter.addAction("android.intent.action.MEDIA_NOFS");
        filter.addAction("android.intent.action.MEDIA_REMOVED");
        filter.addAction("android.intent.action.MEDIA_SHARED");
        filter.addAction("android.intent.action.MEDIA_UNMOUNTABLE");
        filter.addAction("android.intent.action.MEDIA_UNMOUNTED");
        filter.addDataScheme("file");
        try {
            ApplicationLoader.applicationContext.registerReceiver(receiver, filter);
        } catch (Throwable th) {
        }
        checkMediaPaths();
    }

    /* renamed from: org.telegram.messenger.ImageLoader$5 */
    /* loaded from: classes4.dex */
    public class AnonymousClass5 implements FileLoader.FileLoaderDelegate {
        final /* synthetic */ int val$currentAccount;

        AnonymousClass5(int i) {
            ImageLoader.this = this$0;
            this.val$currentAccount = i;
        }

        @Override // org.telegram.messenger.FileLoader.FileLoaderDelegate
        public void fileUploadProgressChanged(FileUploadOperation operation, final String location, final long uploadedSize, final long totalSize, final boolean isEncrypted) {
            ImageLoader.this.fileProgresses.put(location, new long[]{uploadedSize, totalSize});
            long currentTime = SystemClock.elapsedRealtime();
            if (operation.lastProgressUpdateTime == 0 || operation.lastProgressUpdateTime < currentTime - 100 || uploadedSize == totalSize) {
                operation.lastProgressUpdateTime = currentTime;
                final int i = this.val$currentAccount;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.fileUploadProgressChanged, location, Long.valueOf(uploadedSize), Long.valueOf(totalSize), Boolean.valueOf(isEncrypted));
                    }
                });
            }
        }

        @Override // org.telegram.messenger.FileLoader.FileLoaderDelegate
        public void fileDidUploaded(final String location, final TLRPC.InputFile inputFile, final TLRPC.InputEncryptedFile inputEncryptedFile, final byte[] key, final byte[] iv, final long totalFileSize) {
            DispatchQueue dispatchQueue = Utilities.stageQueue;
            final int i = this.val$currentAccount;
            dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.AnonymousClass5.this.m297lambda$fileDidUploaded$2$orgtelegrammessengerImageLoader$5(i, location, inputFile, inputEncryptedFile, key, iv, totalFileSize);
                }
            });
        }

        /* renamed from: lambda$fileDidUploaded$2$org-telegram-messenger-ImageLoader$5 */
        public /* synthetic */ void m297lambda$fileDidUploaded$2$orgtelegrammessengerImageLoader$5(final int currentAccount, final String location, final TLRPC.InputFile inputFile, final TLRPC.InputEncryptedFile inputEncryptedFile, final byte[] key, final byte[] iv, final long totalFileSize) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.fileUploaded, location, inputFile, inputEncryptedFile, key, iv, Long.valueOf(totalFileSize));
                }
            });
            ImageLoader.this.fileProgresses.remove(location);
        }

        @Override // org.telegram.messenger.FileLoader.FileLoaderDelegate
        public void fileDidFailedUpload(final String location, final boolean isEncrypted) {
            DispatchQueue dispatchQueue = Utilities.stageQueue;
            final int i = this.val$currentAccount;
            dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.AnonymousClass5.this.m295xac281fd2(i, location, isEncrypted);
                }
            });
        }

        /* renamed from: lambda$fileDidFailedUpload$4$org-telegram-messenger-ImageLoader$5 */
        public /* synthetic */ void m295xac281fd2(final int currentAccount, final String location, final boolean isEncrypted) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.fileUploadFailed, location, Boolean.valueOf(isEncrypted));
                }
            });
            ImageLoader.this.fileProgresses.remove(location);
        }

        @Override // org.telegram.messenger.FileLoader.FileLoaderDelegate
        public void fileDidLoaded(final String location, final File finalFile, final Object parentObject, final int type) {
            ImageLoader.this.fileProgresses.remove(location);
            final int i = this.val$currentAccount;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.AnonymousClass5.this.m296lambda$fileDidLoaded$5$orgtelegrammessengerImageLoader$5(finalFile, location, parentObject, i, type);
                }
            });
        }

        /* renamed from: lambda$fileDidLoaded$5$org-telegram-messenger-ImageLoader$5 */
        public /* synthetic */ void m296lambda$fileDidLoaded$5$orgtelegrammessengerImageLoader$5(File finalFile, String location, Object parentObject, int currentAccount, int type) {
            int flag;
            if (SharedConfig.saveToGalleryFlags != 0 && finalFile != null && ((location.endsWith(".mp4") || location.endsWith(".jpg")) && (parentObject instanceof MessageObject))) {
                MessageObject messageObject = (MessageObject) parentObject;
                long dialogId = messageObject.getDialogId();
                if (dialogId >= 0) {
                    flag = 1;
                } else if (ChatObject.isChannelAndNotMegaGroup(MessagesController.getInstance(currentAccount).getChat(Long.valueOf(-dialogId)))) {
                    flag = 4;
                } else {
                    flag = 2;
                }
                if ((SharedConfig.saveToGalleryFlags & flag) != 0) {
                    AndroidUtilities.addMediaToGallery(finalFile.toString());
                }
            }
            NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.fileLoaded, location, finalFile);
            ImageLoader.this.fileDidLoaded(location, finalFile, type);
        }

        @Override // org.telegram.messenger.FileLoader.FileLoaderDelegate
        public void fileDidFailedLoad(final String location, final int canceled) {
            ImageLoader.this.fileProgresses.remove(location);
            final int i = this.val$currentAccount;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.AnonymousClass5.this.m294lambda$fileDidFailedLoad$6$orgtelegrammessengerImageLoader$5(location, canceled, i);
                }
            });
        }

        /* renamed from: lambda$fileDidFailedLoad$6$org-telegram-messenger-ImageLoader$5 */
        public /* synthetic */ void m294lambda$fileDidFailedLoad$6$orgtelegrammessengerImageLoader$5(String location, int canceled, int currentAccount) {
            ImageLoader.this.fileDidFailedLoad(location, canceled);
            NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.fileLoadFailed, location, Integer.valueOf(canceled));
        }

        @Override // org.telegram.messenger.FileLoader.FileLoaderDelegate
        public void fileLoadProgressChanged(FileLoadOperation operation, final String location, final long uploadedSize, final long totalSize) {
            ImageLoader.this.fileProgresses.put(location, new long[]{uploadedSize, totalSize});
            long currentTime = SystemClock.elapsedRealtime();
            if (operation.lastProgressUpdateTime == 0 || operation.lastProgressUpdateTime < currentTime - 500 || uploadedSize == 0) {
                operation.lastProgressUpdateTime = currentTime;
                final int i = this.val$currentAccount;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$5$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.fileLoadProgressChanged, location, Long.valueOf(uploadedSize), Long.valueOf(totalSize));
                    }
                });
            }
        }
    }

    /* renamed from: org.telegram.messenger.ImageLoader$6 */
    /* loaded from: classes4.dex */
    public class AnonymousClass6 extends BroadcastReceiver {
        AnonymousClass6() {
            ImageLoader.this = this$0;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context arg0, Intent intent) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("file system changed");
            }
            Runnable r = new Runnable() { // from class: org.telegram.messenger.ImageLoader$6$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.AnonymousClass6.this.m298lambda$onReceive$0$orgtelegrammessengerImageLoader$6();
                }
            };
            if ("android.intent.action.MEDIA_UNMOUNTED".equals(intent.getAction())) {
                AndroidUtilities.runOnUIThread(r, 1000L);
            } else {
                r.run();
            }
        }

        /* renamed from: lambda$onReceive$0$org-telegram-messenger-ImageLoader$6 */
        public /* synthetic */ void m298lambda$onReceive$0$orgtelegrammessengerImageLoader$6() {
            ImageLoader.this.checkMediaPaths();
        }
    }

    public void checkMediaPaths() {
        this.cacheOutQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.m285lambda$checkMediaPaths$1$orgtelegrammessengerImageLoader();
            }
        });
    }

    /* renamed from: lambda$checkMediaPaths$1$org-telegram-messenger-ImageLoader */
    public /* synthetic */ void m285lambda$checkMediaPaths$1$orgtelegrammessengerImageLoader() {
        final SparseArray<File> paths = createMediaPaths();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                FileLoader.setMediaDirs(paths);
            }
        });
    }

    public void addTestWebFile(String url, WebFile webFile) {
        if (url == null || webFile == null) {
            return;
        }
        this.testWebFile.put(url, webFile);
    }

    public void removeTestWebFile(String url) {
        if (url == null) {
            return;
        }
        this.testWebFile.remove(url);
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.stream.Stream != java.util.stream.Stream<java.nio.file.Path> */
    private static void moveDirectory(File source, final File target) {
        if (source.exists()) {
            if (!target.exists() && !target.mkdir()) {
                return;
            }
            try {
                Stream convert = C$r8$wrapper$java$util$stream$Stream$VWRP.convert(Files.list(source.toPath()));
                convert.forEach(new Consumer() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda4
                    @Override // j$.util.function.Consumer
                    public final void accept(Object obj) {
                        ImageLoader.lambda$moveDirectory$2(target, (Path) obj);
                    }

                    @Override // j$.util.function.Consumer
                    public /* synthetic */ Consumer andThen(Consumer consumer) {
                        return consumer.getClass();
                    }
                });
                if (convert != null) {
                    convert.close();
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public static /* synthetic */ void lambda$moveDirectory$2(File target, Path path) {
        File dest = new File(target, path.getFileName().toString());
        if (!Files.isDirectory(path, new LinkOption[0])) {
            try {
                Files.move(path, dest.toPath(), new CopyOption[0]);
                return;
            } catch (Exception e) {
                FileLog.e(e);
                return;
            }
        }
        moveDirectory(path.toFile(), dest);
    }

    public SparseArray<File> createMediaPaths() {
        ArrayList<File> dirs;
        SparseArray<File> mediaDirs = new SparseArray<>();
        File cachePath = AndroidUtilities.getCacheDir();
        if (!cachePath.isDirectory()) {
            try {
                cachePath.mkdirs();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        AndroidUtilities.createEmptyFile(new File(cachePath, ".nomedia"));
        mediaDirs.put(4, cachePath);
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("cache path = " + cachePath);
        }
        try {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                File path = Environment.getExternalStorageDirectory();
                if (Build.VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(SharedConfig.storageCacheDir) && (dirs = AndroidUtilities.getRootDirs()) != null) {
                    int a = 0;
                    int N = dirs.size();
                    while (true) {
                        if (a >= N) {
                            break;
                        }
                        File dir = dirs.get(a);
                        if (!dir.getAbsolutePath().startsWith(SharedConfig.storageCacheDir)) {
                            a++;
                        } else {
                            path = dir;
                            break;
                        }
                    }
                }
                File publicMediaDir = null;
                if (Build.VERSION.SDK_INT >= 30) {
                    try {
                        if (ApplicationLoader.applicationContext.getExternalMediaDirs().length > 0) {
                            publicMediaDir = new File(ApplicationLoader.applicationContext.getExternalMediaDirs()[0], "Telegram");
                            publicMediaDir.mkdirs();
                        }
                    } catch (Exception e2) {
                        FileLog.e(e2);
                    }
                    File newPath = ApplicationLoader.applicationContext.getExternalFilesDir(null);
                    this.telegramPath = new File(newPath, "Telegram");
                } else {
                    this.telegramPath = new File(path, "Telegram");
                }
                this.telegramPath.mkdirs();
                if (Build.VERSION.SDK_INT >= 19 && !this.telegramPath.isDirectory()) {
                    ArrayList<File> dirs2 = AndroidUtilities.getDataDirs();
                    int a2 = 0;
                    int N2 = dirs2.size();
                    while (true) {
                        if (a2 >= N2) {
                            break;
                        }
                        File dir2 = dirs2.get(a2);
                        if (!dir2.getAbsolutePath().startsWith(SharedConfig.storageCacheDir)) {
                            a2++;
                        } else {
                            File file = new File(dir2, "Telegram");
                            this.telegramPath = file;
                            file.mkdirs();
                            break;
                        }
                    }
                }
                if (this.telegramPath.isDirectory()) {
                    try {
                        File imagePath = new File(this.telegramPath, "Telegram Images");
                        imagePath.mkdir();
                        if (imagePath.isDirectory() && canMoveFiles(cachePath, imagePath, 0)) {
                            mediaDirs.put(0, imagePath);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("image path = " + imagePath);
                            }
                        }
                    } catch (Exception e3) {
                        FileLog.e(e3);
                    }
                    try {
                        File videoPath = new File(this.telegramPath, "Telegram Video");
                        videoPath.mkdir();
                        if (videoPath.isDirectory() && canMoveFiles(cachePath, videoPath, 2)) {
                            mediaDirs.put(2, videoPath);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("video path = " + videoPath);
                            }
                        }
                    } catch (Exception e4) {
                        FileLog.e(e4);
                    }
                    try {
                        File audioPath = new File(this.telegramPath, "Telegram Audio");
                        audioPath.mkdir();
                        if (audioPath.isDirectory() && canMoveFiles(cachePath, audioPath, 1)) {
                            AndroidUtilities.createEmptyFile(new File(audioPath, ".nomedia"));
                            mediaDirs.put(1, audioPath);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("audio path = " + audioPath);
                            }
                        }
                    } catch (Exception e5) {
                        FileLog.e(e5);
                    }
                    try {
                        File documentPath = new File(this.telegramPath, "Telegram Documents");
                        documentPath.mkdir();
                        if (documentPath.isDirectory() && canMoveFiles(cachePath, documentPath, 3)) {
                            AndroidUtilities.createEmptyFile(new File(documentPath, ".nomedia"));
                            mediaDirs.put(3, documentPath);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("documents path = " + documentPath);
                            }
                        }
                    } catch (Exception e6) {
                        FileLog.e(e6);
                    }
                    try {
                        File normalNamesPath = new File(this.telegramPath, "Telegram Files");
                        normalNamesPath.mkdir();
                        if (normalNamesPath.isDirectory() && canMoveFiles(cachePath, normalNamesPath, 5)) {
                            AndroidUtilities.createEmptyFile(new File(normalNamesPath, ".nomedia"));
                            mediaDirs.put(5, normalNamesPath);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("files path = " + normalNamesPath);
                            }
                        }
                    } catch (Exception e7) {
                        FileLog.e(e7);
                    }
                }
                if (publicMediaDir != null && publicMediaDir.isDirectory()) {
                    try {
                        File imagePath2 = new File(publicMediaDir, "Telegram Images");
                        imagePath2.mkdir();
                        if (imagePath2.isDirectory() && canMoveFiles(cachePath, imagePath2, 0)) {
                            mediaDirs.put(100, imagePath2);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("image path = " + imagePath2);
                            }
                        }
                    } catch (Exception e8) {
                        FileLog.e(e8);
                    }
                    try {
                        File videoPath2 = new File(publicMediaDir, "Telegram Video");
                        videoPath2.mkdir();
                        if (videoPath2.isDirectory() && canMoveFiles(cachePath, videoPath2, 2)) {
                            mediaDirs.put(101, videoPath2);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("video path = " + videoPath2);
                            }
                        }
                    } catch (Exception e9) {
                        FileLog.e(e9);
                    }
                }
            } else if (BuildVars.LOGS_ENABLED) {
                FileLog.d("this Android can't rename files");
            }
            SharedConfig.checkSaveToGalleryFiles();
        } catch (Exception e10) {
            FileLog.e(e10);
        }
        return mediaDirs;
    }

    private boolean canMoveFiles(File from, File to, int type) {
        boolean canRename;
        RandomAccessFile file = null;
        File srcFile = null;
        File dstFile = null;
        try {
            try {
                try {
                    if (type == 0) {
                        srcFile = new File(from, "000000000_999999_temp.f");
                        dstFile = new File(to, "000000000_999999.f");
                    } else {
                        if (type != 3 && type != 5) {
                            if (type == 1) {
                                srcFile = new File(from, "000000000_999999_temp.f");
                                dstFile = new File(to, "000000000_999999.f");
                            } else if (type == 2) {
                                srcFile = new File(from, "000000000_999999_temp.f");
                                dstFile = new File(to, "000000000_999999.f");
                            }
                        }
                        srcFile = new File(from, "000000000_999999_temp.f");
                        dstFile = new File(to, "000000000_999999.f");
                    }
                    byte[] buffer = new byte[1024];
                    srcFile.createNewFile();
                    RandomAccessFile file2 = new RandomAccessFile(srcFile, "rws");
                    file2.write(buffer);
                    file2.close();
                    file = null;
                    canRename = srcFile.renameTo(dstFile);
                    srcFile.delete();
                    dstFile.delete();
                } catch (Exception e) {
                    FileLog.e(e);
                    if (file == null) {
                        return false;
                    }
                    file.close();
                }
                if (!canRename) {
                    if (0 == 0) {
                        return false;
                    }
                    file.close();
                    return false;
                }
                if (0 != 0) {
                    try {
                        file.close();
                    } catch (Exception e2) {
                        FileLog.e(e2);
                    }
                }
                return true;
            } catch (Throwable th) {
                if (file != null) {
                    try {
                        file.close();
                    } catch (Exception e3) {
                        FileLog.e(e3);
                    }
                }
                throw th;
            }
        } catch (Exception e4) {
            FileLog.e(e4);
            return false;
        }
    }

    public Float getFileProgress(String location) {
        long[] progress;
        if (location == null || (progress = this.fileProgresses.get(location)) == null) {
            return null;
        }
        if (progress[1] == 0) {
            return Float.valueOf(0.0f);
        }
        return Float.valueOf(Math.min(1.0f, ((float) progress[0]) / ((float) progress[1])));
    }

    public long[] getFileProgressSizes(String location) {
        if (location == null) {
            return null;
        }
        return this.fileProgresses.get(location);
    }

    public String getReplacedKey(String oldKey) {
        if (oldKey == null) {
            return null;
        }
        return this.replacedBitmaps.get(oldKey);
    }

    private void performReplace(String oldKey, String newKey) {
        LruCache<BitmapDrawable> currentCache = this.memCache;
        BitmapDrawable b = currentCache.get(oldKey);
        if (b == null) {
            currentCache = this.smallImagesMemCache;
            b = currentCache.get(oldKey);
        }
        this.replacedBitmaps.put(oldKey, newKey);
        if (b != null) {
            BitmapDrawable oldBitmap = currentCache.get(newKey);
            boolean dontChange = false;
            if (oldBitmap != null && oldBitmap.getBitmap() != null && b.getBitmap() != null) {
                Bitmap oldBitmapObject = oldBitmap.getBitmap();
                Bitmap newBitmapObject = b.getBitmap();
                if (oldBitmapObject.getWidth() > newBitmapObject.getWidth() || oldBitmapObject.getHeight() > newBitmapObject.getHeight()) {
                    dontChange = true;
                }
            }
            if (!dontChange) {
                this.ignoreRemoval = oldKey;
                currentCache.remove(oldKey);
                currentCache.put(newKey, b);
                this.ignoreRemoval = null;
            } else {
                currentCache.remove(oldKey);
            }
        }
        Integer val = this.bitmapUseCounts.get(oldKey);
        if (val != null) {
            this.bitmapUseCounts.put(newKey, val);
            this.bitmapUseCounts.remove(oldKey);
        }
    }

    public void incrementUseCount(String key) {
        Integer count = this.bitmapUseCounts.get(key);
        if (count == null) {
            this.bitmapUseCounts.put(key, 1);
        } else {
            this.bitmapUseCounts.put(key, Integer.valueOf(count.intValue() + 1));
        }
    }

    public boolean decrementUseCount(String key) {
        Integer count = this.bitmapUseCounts.get(key);
        if (count == null) {
            return true;
        }
        if (count.intValue() != 1) {
            this.bitmapUseCounts.put(key, Integer.valueOf(count.intValue() - 1));
            return false;
        }
        this.bitmapUseCounts.remove(key);
        return true;
    }

    public void removeImage(String key) {
        this.bitmapUseCounts.remove(key);
        this.memCache.remove(key);
        this.smallImagesMemCache.remove(key);
    }

    public boolean isInMemCache(String key, boolean animated) {
        return animated ? getFromLottieCache(key) != null : getFromMemCache(key) != null;
    }

    public void clearMemory() {
        this.smallImagesMemCache.evictAll();
        this.memCache.evictAll();
        this.lottieMemCache.evictAll();
    }

    private void removeFromWaitingForThumb(int TAG, ImageReceiver imageReceiver) {
        String location = this.waitingForQualityThumbByTag.get(TAG);
        if (location != null) {
            ThumbGenerateInfo info = this.waitingForQualityThumb.get(location);
            if (info != null) {
                int index = info.imageReceiverArray.indexOf(imageReceiver);
                if (index >= 0) {
                    info.imageReceiverArray.remove(index);
                    info.imageReceiverGuidsArray.remove(index);
                }
                if (info.imageReceiverArray.isEmpty()) {
                    this.waitingForQualityThumb.remove(location);
                }
            }
            this.waitingForQualityThumbByTag.remove(TAG);
        }
    }

    public void cancelLoadingForImageReceiver(final ImageReceiver imageReceiver, final boolean cancelAll) {
        if (imageReceiver == null) {
            return;
        }
        ArrayList<Runnable> runnables = imageReceiver.getLoadingOperations();
        if (!runnables.isEmpty()) {
            for (int i = 0; i < runnables.size(); i++) {
                this.imageLoadQueue.cancelRunnable(runnables.get(i));
            }
            runnables.clear();
        }
        imageReceiver.addLoadingImageRunnable(null);
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.m284xd4a49e20(cancelAll, imageReceiver);
            }
        });
    }

    /* renamed from: lambda$cancelLoadingForImageReceiver$3$org-telegram-messenger-ImageLoader */
    public /* synthetic */ void m284xd4a49e20(boolean cancelAll, ImageReceiver imageReceiver) {
        int type;
        for (int a = 0; a < 3; a++) {
            if (a > 0 && !cancelAll) {
                return;
            }
            if (a == 0) {
                type = 1;
            } else if (a == 1) {
                type = 0;
            } else {
                type = 3;
            }
            int TAG = imageReceiver.getTag(type);
            if (TAG != 0) {
                if (a == 0) {
                    removeFromWaitingForThumb(TAG, imageReceiver);
                }
                CacheImage ei = this.imageLoadingByTag.get(TAG);
                if (ei != null) {
                    ei.removeImageReceiver(imageReceiver);
                }
            }
        }
    }

    public BitmapDrawable getImageFromMemory(TLObject fileLocation, String httpUrl, String filter) {
        if (fileLocation == null && httpUrl == null) {
            return null;
        }
        String key = null;
        if (httpUrl != null) {
            key = Utilities.MD5(httpUrl);
        } else if (fileLocation instanceof TLRPC.FileLocation) {
            TLRPC.FileLocation location = (TLRPC.FileLocation) fileLocation;
            key = location.volume_id + "_" + location.local_id;
        } else if (fileLocation instanceof TLRPC.Document) {
            TLRPC.Document location2 = (TLRPC.Document) fileLocation;
            key = location2.dc_id + "_" + location2.id;
        } else if (fileLocation instanceof SecureDocument) {
            SecureDocument location3 = (SecureDocument) fileLocation;
            key = location3.secureFile.dc_id + "_" + location3.secureFile.id;
        } else if (fileLocation instanceof WebFile) {
            key = Utilities.MD5(((WebFile) fileLocation).url);
        }
        if (filter != null) {
            key = key + "@" + filter;
        }
        return getFromMemCache(key);
    }

    /* renamed from: replaceImageInCacheInternal */
    public void m291lambda$replaceImageInCache$4$orgtelegrammessengerImageLoader(String oldKey, String newKey, ImageLocation newLocation) {
        ArrayList<String> arr;
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                arr = this.memCache.getFilterKeys(oldKey);
            } else {
                arr = this.smallImagesMemCache.getFilterKeys(oldKey);
            }
            if (arr != null) {
                for (int a = 0; a < arr.size(); a++) {
                    String filter = arr.get(a);
                    String oldK = oldKey + "@" + filter;
                    String newK = newKey + "@" + filter;
                    performReplace(oldK, newK);
                    NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didReplacedPhotoInMemCache, oldK, newK, newLocation);
                }
            } else {
                performReplace(oldKey, newKey);
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didReplacedPhotoInMemCache, oldKey, newKey, newLocation);
            }
        }
    }

    public void replaceImageInCache(final String oldKey, final String newKey, final ImageLocation newLocation, boolean post) {
        if (post) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.this.m291lambda$replaceImageInCache$4$orgtelegrammessengerImageLoader(oldKey, newKey, newLocation);
                }
            });
        } else {
            m291lambda$replaceImageInCache$4$orgtelegrammessengerImageLoader(oldKey, newKey, newLocation);
        }
    }

    public void putImageToCache(BitmapDrawable bitmap, String key, boolean smallImage) {
        if (smallImage) {
            this.smallImagesMemCache.put(key, bitmap);
        } else {
            this.memCache.put(key, bitmap);
        }
    }

    private void generateThumb(int mediaType, File originalPath, ThumbGenerateInfo info) {
        if ((mediaType == 0 || mediaType == 2 || mediaType == 3) && originalPath != null && info != null) {
            String name = FileLoader.getAttachFileName(info.parentDocument);
            ThumbGenerateTask task = this.thumbGenerateTasks.get(name);
            if (task == null) {
                ThumbGenerateTask task2 = new ThumbGenerateTask(mediaType, originalPath, info);
                this.thumbGeneratingQueue.postRunnable(task2);
            }
        }
    }

    public void cancelForceLoadingForImageReceiver(ImageReceiver imageReceiver) {
        final String key;
        if (imageReceiver == null || (key = imageReceiver.getImageKey()) == null) {
            return;
        }
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.m283xa371a69b(key);
            }
        });
    }

    /* renamed from: lambda$cancelForceLoadingForImageReceiver$5$org-telegram-messenger-ImageLoader */
    public /* synthetic */ void m283xa371a69b(String key) {
        this.forceLoadingImages.remove(key);
    }

    private void createLoadOperationForImageReceiver(final ImageReceiver imageReceiver, final String key, final String url, final String ext, final ImageLocation imageLocation, final String filter, final long size, final int cacheType, final int type, final int thumb, final int guid) {
        int TAG;
        if (imageReceiver == null || url == null || key == null) {
            return;
        }
        if (imageLocation == null) {
            return;
        }
        int TAG2 = imageReceiver.getTag(type);
        if (TAG2 != 0) {
            TAG = TAG2;
        } else {
            int TAG3 = this.lastImageNum;
            imageReceiver.setTag(TAG3, type);
            int i = this.lastImageNum + 1;
            this.lastImageNum = i;
            if (i == Integer.MAX_VALUE) {
                this.lastImageNum = 0;
            }
            TAG = TAG3;
        }
        final int finalTag = TAG;
        final boolean finalIsNeedsQualityThumb = imageReceiver.isNeedsQualityThumb();
        final Object parentObject = imageReceiver.getParentObject();
        final TLRPC.Document qualityDocument = imageReceiver.getQualityThumbDocument();
        final boolean shouldGenerateQualityThumb = imageReceiver.isShouldGenerateQualityThumb();
        final int currentAccount = imageReceiver.getCurrentAccount();
        final boolean currentKeyQuality = type == 0 && imageReceiver.isCurrentKeyQuality();
        Runnable loadOperationRunnable = new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.m286xaef5b73a(thumb, url, key, finalTag, imageReceiver, guid, filter, type, imageLocation, currentKeyQuality, parentObject, currentAccount, qualityDocument, finalIsNeedsQualityThumb, shouldGenerateQualityThumb, ext, cacheType, size);
            }
        };
        this.imageLoadQueue.postRunnable(loadOperationRunnable);
        imageReceiver.addLoadingImageRunnable(loadOperationRunnable);
    }

    /* JADX WARN: Removed duplicated region for block: B:206:0x0493  */
    /* JADX WARN: Removed duplicated region for block: B:211:0x04a2  */
    /* renamed from: lambda$createLoadOperationForImageReceiver$6$org-telegram-messenger-ImageLoader */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void m286xaef5b73a(int thumb, String url, String key, int finalTag, ImageReceiver imageReceiver, int guid, String filter, int type, ImageLocation imageLocation, boolean currentKeyQuality, Object parentObject, int currentAccount, TLRPC.Document qualityDocument, boolean finalIsNeedsQualityThumb, boolean shouldGenerateQualityThumb, String ext, int cacheType, long size) {
        String str;
        boolean cacheFileExists;
        boolean onlyCache;
        ImageLoader imageLoader;
        int i;
        String str2;
        File cacheFile;
        int localCacheType;
        long fileSize;
        File cacheFile2;
        String str3;
        boolean onlyCache2;
        boolean cacheFileExists2;
        File cacheFile3;
        String str4;
        String str5;
        File cacheFile4;
        File cacheFile5;
        String str6;
        File cacheFile6;
        File cacheFile7;
        String str7;
        boolean onlyCache3;
        boolean cacheFileExists3;
        boolean onlyCache4;
        String localPath;
        TLRPC.Document parentDocument;
        int mediaType;
        boolean bigThumb;
        File cachePath;
        File cacheFile8;
        CacheImage alreadyLoadingUrl;
        CacheImage alreadyLoadingCache;
        boolean added = false;
        if (thumb != 2) {
            CacheImage alreadyLoadingUrl2 = this.imageLoadingByUrl.get(url);
            CacheImage alreadyLoadingCache2 = this.imageLoadingByKeys.get(key);
            CacheImage alreadyLoadingImage = this.imageLoadingByTag.get(finalTag);
            if (alreadyLoadingImage == null) {
                alreadyLoadingCache = alreadyLoadingCache2;
                alreadyLoadingUrl = alreadyLoadingUrl2;
            } else if (alreadyLoadingImage == alreadyLoadingCache2) {
                alreadyLoadingImage.setImageReceiverGuid(imageReceiver, guid);
                added = true;
                alreadyLoadingCache = alreadyLoadingCache2;
                alreadyLoadingUrl = alreadyLoadingUrl2;
            } else if (alreadyLoadingImage == alreadyLoadingUrl2) {
                if (alreadyLoadingCache2 != null) {
                    alreadyLoadingCache = alreadyLoadingCache2;
                    alreadyLoadingUrl = alreadyLoadingUrl2;
                } else {
                    alreadyLoadingCache = alreadyLoadingCache2;
                    alreadyLoadingUrl = alreadyLoadingUrl2;
                    alreadyLoadingImage.replaceImageReceiver(imageReceiver, key, filter, type, guid);
                }
                added = true;
            } else {
                alreadyLoadingCache = alreadyLoadingCache2;
                alreadyLoadingUrl = alreadyLoadingUrl2;
                alreadyLoadingImage.removeImageReceiver(imageReceiver);
            }
            if (!added && alreadyLoadingCache != null) {
                alreadyLoadingCache.addImageReceiver(imageReceiver, key, filter, type, guid);
                added = true;
            }
            if (!added && alreadyLoadingUrl != null) {
                alreadyLoadingUrl.addImageReceiver(imageReceiver, key, filter, type, guid);
                added = true;
            }
        }
        if (!added) {
            boolean onlyCache5 = false;
            File cacheFile9 = null;
            boolean cacheFileExists4 = false;
            if (imageLocation.path != null) {
                String location = imageLocation.path;
                if (!location.startsWith("http") && !location.startsWith("athumb")) {
                    if (location.startsWith("thumb://")) {
                        int idx = location.indexOf(Constants.COMMON_SCHEMA_PREFIX_SEPARATOR, 8);
                        if (idx >= 0) {
                            cacheFile9 = new File(location.substring(idx + 1));
                        }
                        onlyCache5 = true;
                    } else if (location.startsWith("vthumb://")) {
                        int idx2 = location.indexOf(Constants.COMMON_SCHEMA_PREFIX_SEPARATOR, 9);
                        if (idx2 < 0) {
                            cacheFile9 = null;
                        } else {
                            cacheFile9 = new File(location.substring(idx2 + 1));
                        }
                        onlyCache5 = true;
                    } else {
                        cacheFile9 = new File(location);
                        onlyCache5 = true;
                    }
                } else {
                    cacheFile9 = null;
                }
                str = "athumb";
            } else if (thumb == 0 && currentKeyQuality) {
                if (!(parentObject instanceof MessageObject)) {
                    onlyCache4 = true;
                    cacheFileExists3 = false;
                    if (qualityDocument != null) {
                        parentDocument = qualityDocument;
                        cachePath = FileLoader.getInstance(currentAccount).getPathToAttach(parentDocument, true);
                        if (MessageObject.isVideoDocument(parentDocument)) {
                            mediaType = 2;
                        } else {
                            mediaType = 3;
                        }
                        localPath = null;
                        bigThumb = true;
                    } else {
                        parentDocument = null;
                        localPath = null;
                        cachePath = null;
                        mediaType = 0;
                        bigThumb = false;
                    }
                } else {
                    MessageObject parentMessageObject = (MessageObject) parentObject;
                    parentDocument = parentMessageObject.getDocument();
                    localPath = parentMessageObject.messageOwner.attachPath;
                    onlyCache4 = true;
                    cacheFileExists3 = false;
                    cachePath = FileLoader.getInstance(currentAccount).getPathToMessage(parentMessageObject.messageOwner);
                    mediaType = parentMessageObject.getMediaType();
                    bigThumb = false;
                }
                if (parentDocument == null) {
                    str = "athumb";
                    onlyCache5 = onlyCache4;
                    cacheFile9 = null;
                    cacheFileExists4 = cacheFileExists3;
                } else {
                    if (!finalIsNeedsQualityThumb) {
                        str = "athumb";
                        cacheFile8 = null;
                    } else {
                        str = "athumb";
                        cacheFile8 = new File(FileLoader.getDirectory(4), "q_" + parentDocument.dc_id + "_" + parentDocument.id + ".jpg");
                        if (!cacheFile8.exists()) {
                            cacheFile8 = null;
                        } else {
                            cacheFileExists3 = true;
                        }
                    }
                    File attachPath = null;
                    if (!TextUtils.isEmpty(localPath)) {
                        attachPath = new File(localPath);
                        if (!attachPath.exists()) {
                            attachPath = null;
                        }
                    }
                    if (attachPath == null) {
                        attachPath = cachePath;
                    }
                    if (cacheFile8 == null) {
                        String location2 = FileLoader.getAttachFileName(parentDocument);
                        ThumbGenerateInfo info = this.waitingForQualityThumb.get(location2);
                        if (info == null) {
                            info = new ThumbGenerateInfo();
                            info.parentDocument = parentDocument;
                            info.filter = filter;
                            info.big = bigThumb;
                            this.waitingForQualityThumb.put(location2, info);
                        }
                        if (!info.imageReceiverArray.contains(imageReceiver)) {
                            info.imageReceiverArray.add(imageReceiver);
                            info.imageReceiverGuidsArray.add(Integer.valueOf(guid));
                        }
                        this.waitingForQualityThumbByTag.put(finalTag, location2);
                        if (attachPath.exists() && shouldGenerateQualityThumb) {
                            generateThumb(mediaType, attachPath, info);
                            return;
                        }
                        return;
                    }
                    cacheFile9 = cacheFile8;
                    onlyCache5 = onlyCache4;
                    cacheFileExists4 = cacheFileExists3;
                }
            } else {
                str = "athumb";
                cacheFile9 = null;
                cacheFileExists4 = false;
            }
            if (thumb != 2) {
                boolean isEncrypted = imageLocation.isEncrypted();
                CacheImage img = new CacheImage();
                if (!currentKeyQuality) {
                    if (imageLocation.imageType != 2 && !MessageObject.isGifDocument(imageLocation.webFile) && !MessageObject.isGifDocument(imageLocation.document) && !MessageObject.isRoundVideoDocument(imageLocation.document)) {
                        if (!MessageObject.isVideoSticker(imageLocation.document)) {
                            if (imageLocation.path != null) {
                                String location3 = imageLocation.path;
                                if (!location3.startsWith("vthumb") && !location3.startsWith("thumb")) {
                                    String trueExt = getHttpUrlExtension(location3, "jpg");
                                    if (!trueExt.equals("webm") && !trueExt.equals("mp4")) {
                                        if (!trueExt.equals("gif")) {
                                            if ("tgs".equals(ext)) {
                                                img.imageType = 1;
                                            }
                                        }
                                    }
                                    img.imageType = 2;
                                }
                            }
                        }
                    }
                    img.imageType = 2;
                }
                if (cacheFile9 != null) {
                    str2 = url;
                    i = cacheType;
                    onlyCache = onlyCache5;
                    cacheFileExists = cacheFileExists4;
                    imageLoader = this;
                    cacheFile = cacheFile9;
                } else {
                    if (imageLocation.photoSize instanceof TLRPC.TL_photoStrippedSize) {
                        str2 = url;
                        i = cacheType;
                        cacheFile3 = cacheFile9;
                        cacheFileExists2 = cacheFileExists4;
                        str3 = AUTOPLAY_FILTER;
                        imageLoader = this;
                    } else if (imageLocation.photoSize instanceof TLRPC.TL_photoPathSize) {
                        str2 = url;
                        i = cacheType;
                        cacheFile3 = cacheFile9;
                        cacheFileExists2 = cacheFileExists4;
                        str3 = AUTOPLAY_FILTER;
                        imageLoader = this;
                    } else {
                        if (imageLocation.secureDocument != null) {
                            img.secureDocument = imageLocation.secureDocument;
                            onlyCache2 = img.secureDocument.secureFile.dc_id == Integer.MIN_VALUE;
                            str2 = url;
                            File cacheFile10 = new File(FileLoader.getDirectory(4), str2);
                            i = cacheType;
                            cacheFile2 = cacheFile10;
                            str3 = AUTOPLAY_FILTER;
                            fileSize = 0;
                            imageLoader = this;
                        } else {
                            str2 = url;
                            boolean onlyCache6 = onlyCache5;
                            boolean cacheFileExists5 = cacheFileExists4;
                            if (AUTOPLAY_FILTER.equals(filter) || isAnimatedAvatar(filter)) {
                                i = cacheType;
                                str4 = "_";
                                str5 = AUTOPLAY_FILTER;
                            } else {
                                i = cacheType;
                                if (i == 0) {
                                    str4 = "_";
                                    str5 = AUTOPLAY_FILTER;
                                    if (size > 0 && imageLocation.path == null && !isEncrypted) {
                                    }
                                } else {
                                    str5 = AUTOPLAY_FILTER;
                                }
                                cacheFile2 = new File(FileLoader.getDirectory(4), str2);
                                if (cacheFile2.exists()) {
                                    cacheFileExists5 = true;
                                } else if (i == 2) {
                                    cacheFile2 = new File(FileLoader.getDirectory(4), str2 + ".enc");
                                }
                                if (imageLocation.document != null) {
                                    if (imageLocation.document instanceof DocumentObject.ThemeDocument) {
                                        DocumentObject.ThemeDocument themeDocument = (DocumentObject.ThemeDocument) imageLocation.document;
                                        if (themeDocument.wallpaper != null) {
                                            onlyCache3 = onlyCache6;
                                        } else {
                                            onlyCache3 = true;
                                        }
                                        img.imageType = 5;
                                        imageLoader = this;
                                        onlyCache2 = onlyCache3;
                                        fileSize = 0;
                                        cacheFileExists4 = cacheFileExists5;
                                        str3 = str5;
                                    } else if ("application/x-tgsdice".equals(imageLocation.document.mime_type)) {
                                        img.imageType = 1;
                                        onlyCache2 = true;
                                        imageLoader = this;
                                        fileSize = 0;
                                        cacheFileExists4 = cacheFileExists5;
                                        str3 = str5;
                                    } else if ("application/x-tgsticker".equals(imageLocation.document.mime_type)) {
                                        img.imageType = 1;
                                    } else if ("application/x-tgwallpattern".equals(imageLocation.document.mime_type)) {
                                        img.imageType = 3;
                                    } else {
                                        String name = FileLoader.getDocumentFileName(imageLocation.document);
                                        if (name.endsWith(".svg")) {
                                            img.imageType = 3;
                                        }
                                    }
                                }
                                imageLoader = this;
                                onlyCache2 = onlyCache6;
                                fileSize = 0;
                                cacheFileExists4 = cacheFileExists5;
                                str3 = str5;
                            }
                            if (imageLocation.document != null) {
                                TLRPC.Document document = imageLocation.document;
                                if (document instanceof TLRPC.TL_documentEncrypted) {
                                    cacheFile5 = new File(FileLoader.getDirectory(4), str2);
                                } else if (MessageObject.isVideoDocument(document)) {
                                    cacheFile5 = new File(FileLoader.getDirectory(2), str2);
                                } else {
                                    cacheFile5 = new File(FileLoader.getDirectory(3), str2);
                                }
                                if (!isAnimatedAvatar(filter)) {
                                    str7 = str5;
                                    if (!str7.equals(filter)) {
                                        str6 = str7;
                                        cacheFile7 = cacheFile5;
                                        cacheFile6 = cacheFile7;
                                        if (document instanceof DocumentObject.ThemeDocument) {
                                            DocumentObject.ThemeDocument themeDocument2 = (DocumentObject.ThemeDocument) document;
                                            if (themeDocument2.wallpaper != null) {
                                                onlyCache2 = onlyCache6;
                                            } else {
                                                onlyCache2 = true;
                                            }
                                            img.imageType = 5;
                                        } else if ("application/x-tgsdice".equals(imageLocation.document.mime_type)) {
                                            img.imageType = 1;
                                            onlyCache2 = true;
                                        } else {
                                            if ("application/x-tgsticker".equals(document.mime_type)) {
                                                img.imageType = 1;
                                            } else if ("application/x-tgwallpattern".equals(document.mime_type)) {
                                                img.imageType = 3;
                                            } else {
                                                String name2 = FileLoader.getDocumentFileName(imageLocation.document);
                                                if (name2.endsWith(".svg")) {
                                                    img.imageType = 3;
                                                }
                                            }
                                            onlyCache2 = onlyCache6;
                                        }
                                        long fileSize2 = document.size;
                                        fileSize = fileSize2;
                                        cacheFile2 = cacheFile6;
                                        cacheFileExists4 = cacheFileExists5;
                                        str3 = str6;
                                        imageLoader = this;
                                    }
                                } else {
                                    str7 = str5;
                                }
                                if (cacheFile5.exists()) {
                                    str6 = str7;
                                    cacheFile7 = cacheFile5;
                                    cacheFile6 = cacheFile7;
                                    if (document instanceof DocumentObject.ThemeDocument) {
                                    }
                                    long fileSize22 = document.size;
                                    fileSize = fileSize22;
                                    cacheFile2 = cacheFile6;
                                    cacheFileExists4 = cacheFileExists5;
                                    str3 = str6;
                                    imageLoader = this;
                                } else {
                                    File cacheFile11 = FileLoader.getDirectory(4);
                                    StringBuilder sb = new StringBuilder();
                                    str6 = str7;
                                    sb.append(document.dc_id);
                                    sb.append(str4);
                                    sb.append(document.id);
                                    sb.append(".temp");
                                    cacheFile6 = new File(cacheFile11, sb.toString());
                                    if (document instanceof DocumentObject.ThemeDocument) {
                                    }
                                    long fileSize222 = document.size;
                                    fileSize = fileSize222;
                                    cacheFile2 = cacheFile6;
                                    cacheFileExists4 = cacheFileExists5;
                                    str3 = str6;
                                    imageLoader = this;
                                }
                            } else {
                                String str8 = str4;
                                if (imageLocation.webFile != null) {
                                    imageLoader = this;
                                    cacheFile2 = new File(FileLoader.getDirectory(3), str2);
                                    onlyCache2 = onlyCache6;
                                    fileSize = 0;
                                    cacheFileExists4 = cacheFileExists5;
                                    str3 = str5;
                                } else {
                                    if (i == 1) {
                                        cacheFile4 = new File(FileLoader.getDirectory(4), str2);
                                    } else {
                                        cacheFile4 = new File(FileLoader.getDirectory(0), str2);
                                    }
                                    imageLoader = this;
                                    if (!imageLoader.isAnimatedAvatar(filter)) {
                                        str3 = str5;
                                        if (!str3.equals(filter) || imageLocation.location == null || cacheFile4.exists()) {
                                            cacheFile2 = cacheFile4;
                                            onlyCache2 = onlyCache6;
                                            fileSize = 0;
                                            cacheFileExists4 = cacheFileExists5;
                                        }
                                    } else {
                                        str3 = str5;
                                    }
                                    cacheFile2 = new File(FileLoader.getDirectory(4), imageLocation.location.volume_id + str8 + imageLocation.location.local_id + ".temp");
                                    onlyCache2 = onlyCache6;
                                    fileSize = 0L;
                                    cacheFileExists4 = cacheFileExists5;
                                }
                            }
                        }
                        if (!str3.equals(filter) || imageLoader.isAnimatedAvatar(filter)) {
                            img.imageType = 2;
                            img.size = fileSize;
                            onlyCache = true;
                            cacheFileExists = cacheFileExists4;
                            cacheFile = cacheFile2;
                        } else {
                            onlyCache = onlyCache2;
                            cacheFileExists = cacheFileExists4;
                            cacheFile = cacheFile2;
                        }
                    }
                    onlyCache2 = true;
                    fileSize = 0;
                    cacheFile2 = cacheFile3;
                    cacheFileExists4 = cacheFileExists2;
                    if (!str3.equals(filter)) {
                    }
                    img.imageType = 2;
                    img.size = fileSize;
                    onlyCache = true;
                    cacheFileExists = cacheFileExists4;
                    cacheFile = cacheFile2;
                }
                img.type = type;
                img.key = key;
                img.filter = filter;
                img.imageLocation = imageLocation;
                img.ext = ext;
                img.currentAccount = currentAccount;
                img.parentObject = parentObject;
                if (imageLocation.imageType != 0) {
                    img.imageType = imageLocation.imageType;
                }
                if (i == 2) {
                    img.encryptionKeyPath = new File(FileLoader.getInternalCacheDir(), str2 + ".enc.key");
                }
                int i2 = i;
                String str9 = str;
                img.addImageReceiver(imageReceiver, key, filter, type, guid);
                if (onlyCache || cacheFileExists || cacheFile.exists()) {
                    img.finalFilePath = cacheFile;
                    img.imageLocation = imageLocation;
                    img.cacheTask = new CacheOutTask(img);
                    imageLoader.imageLoadingByKeys.put(key, img);
                    if (thumb != 0) {
                        imageLoader.cacheThumbOutQueue.postRunnable(img.cacheTask);
                        return;
                    } else {
                        imageLoader.cacheOutQueue.postRunnable(img.cacheTask);
                        return;
                    }
                }
                img.url = str2;
                imageLoader.imageLoadingByUrl.put(str2, img);
                if (imageLocation.path != null) {
                    String file = Utilities.MD5(imageLocation.path);
                    File cacheDir = FileLoader.getDirectory(4);
                    img.tempFilePath = new File(cacheDir, file + "_temp.jpg");
                    img.finalFilePath = cacheFile;
                    if (imageLocation.path.startsWith(str9)) {
                        img.artworkTask = new ArtworkLoadTask(img);
                        imageLoader.artworkTasks.add(img.artworkTask);
                        imageLoader.runArtworkTasks(false);
                    } else {
                        img.httpTask = new HttpImageTask(img, size);
                        imageLoader.httpTasks.add(img.httpTask);
                        imageLoader.runHttpTasks(false);
                    }
                    return;
                }
                if (imageLocation.location != null) {
                    if (cacheType == 0 && (size <= 0 || imageLocation.key != null)) {
                        localCacheType = 1;
                    } else {
                        localCacheType = cacheType;
                    }
                    FileLoader.getInstance(currentAccount).loadFile(imageLocation, parentObject, ext, thumb != 0 ? 2 : 1, localCacheType);
                } else if (imageLocation.document != null) {
                    FileLoader.getInstance(currentAccount).loadFile(imageLocation.document, parentObject, thumb != 0 ? 2 : 1, i2);
                } else if (imageLocation.secureDocument != null) {
                    FileLoader.getInstance(currentAccount).loadFile(imageLocation.secureDocument, thumb != 0 ? 2 : 1);
                } else if (imageLocation.webFile != null) {
                    FileLoader.getInstance(currentAccount).loadFile(imageLocation.webFile, thumb != 0 ? 2 : 1, i2);
                }
                if (imageReceiver.isForceLoding()) {
                    imageLoader.forceLoadingImages.put(img.key, 0);
                }
            }
        }
    }

    public void preloadArtwork(final String athumbUrl) {
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.m290lambda$preloadArtwork$7$orgtelegrammessengerImageLoader(athumbUrl);
            }
        });
    }

    /* renamed from: lambda$preloadArtwork$7$org-telegram-messenger-ImageLoader */
    public /* synthetic */ void m290lambda$preloadArtwork$7$orgtelegrammessengerImageLoader(String athumbUrl) {
        String ext = getHttpUrlExtension(athumbUrl, "jpg");
        String url = Utilities.MD5(athumbUrl) + "." + ext;
        File cacheFile = new File(FileLoader.getDirectory(4), url);
        if (cacheFile.exists()) {
            return;
        }
        ImageLocation imageLocation = ImageLocation.getForPath(athumbUrl);
        CacheImage img = new CacheImage();
        img.type = 1;
        img.key = Utilities.MD5(athumbUrl);
        img.filter = null;
        img.imageLocation = imageLocation;
        img.ext = ext;
        img.parentObject = null;
        if (imageLocation.imageType != 0) {
            img.imageType = imageLocation.imageType;
        }
        img.url = url;
        this.imageLoadingByUrl.put(url, img);
        String file = Utilities.MD5(imageLocation.path);
        File cacheDir = FileLoader.getDirectory(4);
        img.tempFilePath = new File(cacheDir, file + "_temp.jpg");
        img.finalFilePath = cacheFile;
        img.artworkTask = new ArtworkLoadTask(img);
        this.artworkTasks.add(img.artworkTask);
        runArtworkTasks(false);
    }

    /* JADX WARN: Removed duplicated region for block: B:101:0x01bb  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x01c4  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x01c9  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x01cd  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x01d2  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x01d6  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x01db  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x01ed  */
    /* JADX WARN: Removed duplicated region for block: B:194:0x038f  */
    /* JADX WARN: Removed duplicated region for block: B:214:0x040e  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x0419 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:221:0x0434 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:224:0x044a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:229:0x0469  */
    /* JADX WARN: Removed duplicated region for block: B:230:0x047f  */
    /* JADX WARN: Removed duplicated region for block: B:232:0x0483  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x04d3  */
    /* JADX WARN: Removed duplicated region for block: B:255:0x0545  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0181  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0190  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x01a8  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x01b2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void loadImageForImageReceiver(ImageReceiver imageReceiver) {
        boolean thumbSet;
        Object parentObject;
        ImageLocation thumbLocation;
        ImageLocation mediaLocation;
        ImageLocation imageLocation;
        boolean qualityThumb;
        String imageKey;
        String mediaKey;
        int i;
        String imageExt;
        boolean saveImageToCache;
        String mediaExt;
        String thumbExt;
        String thumbExt2;
        String imageExt2;
        String mediaExt2;
        int a;
        ImageLocation mediaLocation2;
        ImageLocation imageLocation2;
        String thumbExt3;
        String thumbExt4;
        String thumbKey;
        String mediaKey2;
        String thumbKey2;
        String imageKey2;
        int imageCacheType;
        int mediaCacheType;
        String ext;
        ImageLocation object;
        String thumbKey3;
        String docExt;
        Drawable drawable;
        Drawable drawable2;
        Drawable drawable3;
        if (imageReceiver == null) {
            return;
        }
        boolean imageSet = false;
        String mediaKey3 = imageReceiver.getMediaKey();
        int guid = imageReceiver.getNewGuid();
        if (mediaKey3 != null) {
            if (useLottieMemCache(imageReceiver.getMediaLocation(), mediaKey3)) {
                drawable3 = getFromLottieCache(mediaKey3);
            } else {
                Drawable drawable4 = this.memCache.get(mediaKey3);
                if (drawable4 != null) {
                    this.memCache.moveToFront(mediaKey3);
                }
                if (drawable4 == null && (drawable4 = this.smallImagesMemCache.get(mediaKey3)) != null) {
                    this.smallImagesMemCache.moveToFront(mediaKey3);
                }
                if (drawable4 != null) {
                    drawable3 = drawable4;
                } else {
                    Drawable drawable5 = this.wallpaperMemCache.get(mediaKey3);
                    if (drawable5 != null) {
                        this.wallpaperMemCache.moveToFront(mediaKey3);
                    }
                    drawable3 = drawable5;
                }
            }
            if (drawable3 != null) {
                cancelLoadingForImageReceiver(imageReceiver, true);
                imageReceiver.setImageBitmapByKey(drawable3, mediaKey3, 3, true, guid);
                imageSet = true;
                if (!imageReceiver.isForcePreview()) {
                    return;
                }
            }
        }
        String imageKey3 = imageReceiver.getImageKey();
        if (!imageSet && imageKey3 != null) {
            Drawable drawable6 = null;
            if (useLottieMemCache(imageReceiver.getImageLocation(), imageKey3)) {
                drawable6 = getFromLottieCache(imageKey3);
            }
            if (drawable6 != null) {
                drawable2 = drawable6;
            } else {
                Drawable drawable7 = this.memCache.get(imageKey3);
                if (drawable7 != null) {
                    this.memCache.moveToFront(imageKey3);
                }
                if (drawable7 == null && (drawable7 = this.smallImagesMemCache.get(imageKey3)) != null) {
                    this.smallImagesMemCache.moveToFront(imageKey3);
                }
                if (drawable7 != null) {
                    drawable2 = drawable7;
                } else {
                    Drawable drawable8 = this.wallpaperMemCache.get(imageKey3);
                    if (drawable8 != null) {
                        this.wallpaperMemCache.moveToFront(imageKey3);
                    }
                    drawable2 = drawable8;
                }
            }
            if (drawable2 != null) {
                cancelLoadingForImageReceiver(imageReceiver, true);
                imageReceiver.setImageBitmapByKey(drawable2, imageKey3, 0, true, guid);
                imageSet = true;
                if (!imageReceiver.isForcePreview() && mediaKey3 == null) {
                    return;
                }
            }
        }
        boolean imageSet2 = imageSet;
        String thumbKey4 = imageReceiver.getThumbKey();
        if (thumbKey4 != null) {
            if (useLottieMemCache(imageReceiver.getThumbLocation(), thumbKey4)) {
                drawable = getFromLottieCache(thumbKey4);
            } else {
                Drawable drawable9 = this.memCache.get(thumbKey4);
                if (drawable9 != null) {
                    this.memCache.moveToFront(thumbKey4);
                }
                if (drawable9 == null && (drawable9 = this.smallImagesMemCache.get(thumbKey4)) != null) {
                    this.smallImagesMemCache.moveToFront(thumbKey4);
                }
                if (drawable9 != null) {
                    drawable = drawable9;
                } else {
                    Drawable drawable10 = this.wallpaperMemCache.get(thumbKey4);
                    if (drawable10 != null) {
                        this.wallpaperMemCache.moveToFront(thumbKey4);
                    }
                    drawable = drawable10;
                }
            }
            if (drawable != null) {
                imageReceiver.setImageBitmapByKey(drawable, thumbKey4, 1, true, guid);
                cancelLoadingForImageReceiver(imageReceiver, false);
                if (imageSet2 && imageReceiver.isForcePreview()) {
                    return;
                }
                thumbSet = true;
                parentObject = imageReceiver.getParentObject();
                TLRPC.Document qualityDocument = imageReceiver.getQualityThumbDocument();
                thumbLocation = imageReceiver.getThumbLocation();
                String thumbFilter = imageReceiver.getThumbFilter();
                mediaLocation = imageReceiver.getMediaLocation();
                String mediaFilter = imageReceiver.getMediaFilter();
                ImageLocation originalImageLocation = imageReceiver.getImageLocation();
                String imageFilter = imageReceiver.getImageFilter();
                imageLocation = originalImageLocation;
                if (imageLocation == null && imageReceiver.isNeedsQualityThumb() && imageReceiver.isCurrentKeyQuality()) {
                    if (!(parentObject instanceof MessageObject)) {
                        MessageObject messageObject = (MessageObject) parentObject;
                        imageLocation = ImageLocation.getForDocument(messageObject.getDocument());
                        qualityThumb = true;
                    } else if (qualityDocument != null) {
                        imageLocation = ImageLocation.getForDocument(qualityDocument);
                        qualityThumb = true;
                    }
                    imageKey = null;
                    String thumbKey5 = null;
                    mediaKey = null;
                    i = 2;
                    if (imageLocation == null && imageLocation.imageType == 2) {
                        imageExt = "mp4";
                    } else {
                        imageExt = null;
                    }
                    if (mediaLocation == null) {
                        saveImageToCache = false;
                        if (mediaLocation.imageType == 2) {
                            mediaExt = "mp4";
                            thumbExt = imageReceiver.getExt();
                            if (thumbExt != null) {
                                thumbExt2 = thumbExt;
                            } else {
                                thumbExt2 = "jpg";
                            }
                            if (imageExt != null) {
                                imageExt2 = imageExt;
                            } else {
                                imageExt2 = thumbExt2;
                            }
                            if (mediaExt != null) {
                                mediaExt2 = mediaExt;
                            } else {
                                mediaExt2 = thumbExt2;
                            }
                            a = 0;
                            mediaLocation2 = mediaLocation;
                            imageLocation2 = imageLocation;
                            String imageUrl = null;
                            String mediaUrl = null;
                            boolean saveImageToCache2 = saveImageToCache;
                            while (a < i) {
                                if (a == 0) {
                                    object = imageLocation2;
                                    ext = imageExt2;
                                } else {
                                    object = mediaLocation2;
                                    ext = mediaExt2;
                                }
                                if (object == null) {
                                    thumbKey3 = thumbKey5;
                                } else {
                                    thumbKey3 = thumbKey5;
                                    String key = object.getKey(parentObject, mediaLocation2 != null ? mediaLocation2 : imageLocation2, false);
                                    if (key != null) {
                                        String url = object.getKey(parentObject, mediaLocation2 != null ? mediaLocation2 : imageLocation2, true);
                                        if (object.path != null) {
                                            url = url + "." + getHttpUrlExtension(object.path, "jpg");
                                        } else if ((object.photoSize instanceof TLRPC.TL_photoStrippedSize) || (object.photoSize instanceof TLRPC.TL_photoPathSize)) {
                                            url = url + "." + ext;
                                        } else if (object.location != null) {
                                            url = url + "." + ext;
                                            if (imageReceiver.getExt() != null || object.location.key != null || (object.location.volume_id == -2147483648L && object.location.local_id < 0)) {
                                                saveImageToCache2 = true;
                                            }
                                        } else if (object.webFile != null) {
                                            String defaultExt = FileLoader.getMimeTypePart(object.webFile.mime_type);
                                            url = url + "." + getHttpUrlExtension(object.webFile.url, defaultExt);
                                        } else if (object.secureDocument != null) {
                                            url = url + "." + ext;
                                        } else if (object.document != null) {
                                            if (a == 0 && qualityThumb) {
                                                key = "q_" + key;
                                            }
                                            String docExt2 = FileLoader.getDocumentFileName(object.document);
                                            int idx = docExt2.lastIndexOf(46);
                                            String key2 = key;
                                            if (idx == -1) {
                                                docExt = "";
                                            } else {
                                                docExt = docExt2.substring(idx);
                                            }
                                            if (docExt.length() <= 1) {
                                                if (MimeTypes.VIDEO_MP4.equals(object.document.mime_type)) {
                                                    docExt = ".mp4";
                                                } else if ("video/x-matroska".equals(object.document.mime_type)) {
                                                    docExt = ".mkv";
                                                } else {
                                                    docExt = "";
                                                }
                                            }
                                            url = url + docExt;
                                            saveImageToCache2 = !MessageObject.isVideoDocument(object.document) && !MessageObject.isGifDocument(object.document) && !MessageObject.isRoundVideoDocument(object.document) && !MessageObject.canPreviewDocument(object.document);
                                            key = key2;
                                        }
                                        if (a == 0) {
                                            imageKey = key;
                                            imageUrl = url;
                                        } else {
                                            mediaKey = key;
                                            mediaUrl = url;
                                        }
                                        if (object == thumbLocation) {
                                            if (a == 0) {
                                                imageLocation2 = null;
                                                imageUrl = null;
                                                imageKey = null;
                                            } else {
                                                mediaLocation2 = null;
                                                mediaUrl = null;
                                                mediaKey = null;
                                            }
                                        }
                                    }
                                }
                                a++;
                                i = 2;
                                thumbKey5 = thumbKey3;
                            }
                            String thumbKey6 = thumbKey5;
                            if (thumbLocation == null) {
                                thumbExt4 = thumbExt2;
                                thumbExt3 = null;
                                thumbKey = thumbKey6;
                            } else {
                                ImageLocation strippedLoc = imageReceiver.getStrippedLocation();
                                if (strippedLoc == null) {
                                    strippedLoc = mediaLocation2 != null ? mediaLocation2 : originalImageLocation;
                                }
                                thumbKey = thumbLocation.getKey(parentObject, strippedLoc, false);
                                String thumbUrl = thumbLocation.getKey(parentObject, strippedLoc, true);
                                if (thumbLocation.path != null) {
                                    thumbExt4 = thumbExt2;
                                    thumbExt3 = thumbUrl + "." + getHttpUrlExtension(thumbLocation.path, "jpg");
                                } else {
                                    if (thumbLocation.photoSize instanceof TLRPC.TL_photoStrippedSize) {
                                        thumbExt4 = thumbExt2;
                                    } else if (thumbLocation.photoSize instanceof TLRPC.TL_photoPathSize) {
                                        thumbExt4 = thumbExt2;
                                    } else if (thumbLocation.location == null) {
                                        thumbExt4 = thumbExt2;
                                        thumbExt3 = thumbUrl;
                                    } else {
                                        StringBuilder sb = new StringBuilder();
                                        sb.append(thumbUrl);
                                        sb.append(".");
                                        thumbExt4 = thumbExt2;
                                        sb.append(thumbExt4);
                                        thumbExt3 = sb.toString();
                                    }
                                    thumbExt3 = thumbUrl + "." + thumbExt4;
                                }
                            }
                            if (mediaKey == null && mediaFilter != null) {
                                mediaKey2 = mediaKey + "@" + mediaFilter;
                            } else {
                                mediaKey2 = mediaKey;
                            }
                            if (imageKey != null && imageFilter != null) {
                                imageKey = imageKey + "@" + imageFilter;
                            }
                            if (thumbKey == null && thumbFilter != null) {
                                thumbKey2 = thumbKey + "@" + thumbFilter;
                            } else {
                                thumbKey2 = thumbKey;
                            }
                            if (imageReceiver.getUniqKeyPrefix() == null) {
                                imageKey2 = imageKey;
                            } else {
                                imageKey2 = imageReceiver.getUniqKeyPrefix() + imageKey;
                            }
                            if (imageLocation2 == null && imageLocation2.path != null) {
                                createLoadOperationForImageReceiver(imageReceiver, thumbKey2, thumbExt3, thumbExt4, thumbLocation, thumbFilter, 0L, 1, 1, thumbSet ? 2 : 1, guid);
                                createLoadOperationForImageReceiver(imageReceiver, imageKey2, imageUrl, imageExt2, imageLocation2, imageFilter, imageReceiver.getSize(), 1, 0, 0, guid);
                                return;
                            }
                            ImageLocation imageLocation3 = imageLocation2;
                            if (mediaLocation2 != null) {
                                int mediaCacheType2 = imageReceiver.getCacheType();
                                if (mediaCacheType2 == 0 && saveImageToCache2) {
                                    mediaCacheType = 1;
                                } else {
                                    mediaCacheType = mediaCacheType2;
                                }
                                int thumbCacheType = mediaCacheType == 0 ? 1 : mediaCacheType;
                                if (!thumbSet) {
                                    createLoadOperationForImageReceiver(imageReceiver, thumbKey2, thumbExt3, thumbExt4, thumbLocation, thumbFilter, 0L, thumbCacheType, 1, 1, guid);
                                }
                                if (!imageSet2) {
                                    createLoadOperationForImageReceiver(imageReceiver, imageKey2, imageUrl, imageExt2, imageLocation3, imageFilter, 0L, 1, 0, 0, guid);
                                }
                                createLoadOperationForImageReceiver(imageReceiver, mediaKey2, mediaUrl, mediaExt2, mediaLocation2, mediaFilter, imageReceiver.getSize(), mediaCacheType, 3, 0, guid);
                                return;
                            }
                            String thumbExt5 = thumbExt4;
                            int imageCacheType2 = imageReceiver.getCacheType();
                            if (imageCacheType2 == 0 && saveImageToCache2) {
                                imageCacheType = 1;
                            } else {
                                imageCacheType = imageCacheType2;
                            }
                            int thumbCacheType2 = imageCacheType == 0 ? 1 : imageCacheType;
                            createLoadOperationForImageReceiver(imageReceiver, thumbKey2, thumbExt3, thumbExt5, thumbLocation, thumbFilter, 0L, thumbCacheType2, 1, thumbSet ? 2 : 1, guid);
                            createLoadOperationForImageReceiver(imageReceiver, imageKey2, imageUrl, imageExt2, imageLocation3, imageFilter, imageReceiver.getSize(), imageCacheType, 0, 0, guid);
                            return;
                        }
                    } else {
                        saveImageToCache = false;
                    }
                    mediaExt = null;
                    thumbExt = imageReceiver.getExt();
                    if (thumbExt != null) {
                    }
                    if (imageExt != null) {
                    }
                    if (mediaExt != null) {
                    }
                    a = 0;
                    mediaLocation2 = mediaLocation;
                    imageLocation2 = imageLocation;
                    String imageUrl2 = null;
                    String mediaUrl2 = null;
                    boolean saveImageToCache22 = saveImageToCache;
                    while (a < i) {
                    }
                    String thumbKey62 = thumbKey5;
                    if (thumbLocation == null) {
                    }
                    if (mediaKey == null) {
                    }
                    mediaKey2 = mediaKey;
                    if (imageKey != null) {
                        imageKey = imageKey + "@" + imageFilter;
                    }
                    if (thumbKey == null) {
                    }
                    thumbKey2 = thumbKey;
                    if (imageReceiver.getUniqKeyPrefix() == null) {
                    }
                    if (imageLocation2 == null) {
                    }
                    ImageLocation imageLocation32 = imageLocation2;
                    if (mediaLocation2 != null) {
                    }
                }
                qualityThumb = false;
                imageKey = null;
                String thumbKey52 = null;
                mediaKey = null;
                i = 2;
                if (imageLocation == null) {
                }
                imageExt = null;
                if (mediaLocation == null) {
                }
                mediaExt = null;
                thumbExt = imageReceiver.getExt();
                if (thumbExt != null) {
                }
                if (imageExt != null) {
                }
                if (mediaExt != null) {
                }
                a = 0;
                mediaLocation2 = mediaLocation;
                imageLocation2 = imageLocation;
                String imageUrl22 = null;
                String mediaUrl22 = null;
                boolean saveImageToCache222 = saveImageToCache;
                while (a < i) {
                }
                String thumbKey622 = thumbKey52;
                if (thumbLocation == null) {
                }
                if (mediaKey == null) {
                }
                mediaKey2 = mediaKey;
                if (imageKey != null) {
                }
                if (thumbKey == null) {
                }
                thumbKey2 = thumbKey;
                if (imageReceiver.getUniqKeyPrefix() == null) {
                }
                if (imageLocation2 == null) {
                }
                ImageLocation imageLocation322 = imageLocation2;
                if (mediaLocation2 != null) {
                }
            }
        }
        thumbSet = false;
        parentObject = imageReceiver.getParentObject();
        TLRPC.Document qualityDocument2 = imageReceiver.getQualityThumbDocument();
        thumbLocation = imageReceiver.getThumbLocation();
        String thumbFilter2 = imageReceiver.getThumbFilter();
        mediaLocation = imageReceiver.getMediaLocation();
        String mediaFilter2 = imageReceiver.getMediaFilter();
        ImageLocation originalImageLocation2 = imageReceiver.getImageLocation();
        String imageFilter2 = imageReceiver.getImageFilter();
        imageLocation = originalImageLocation2;
        if (imageLocation == null) {
            if (!(parentObject instanceof MessageObject)) {
            }
            imageKey = null;
            String thumbKey522 = null;
            mediaKey = null;
            i = 2;
            if (imageLocation == null) {
            }
            imageExt = null;
            if (mediaLocation == null) {
            }
            mediaExt = null;
            thumbExt = imageReceiver.getExt();
            if (thumbExt != null) {
            }
            if (imageExt != null) {
            }
            if (mediaExt != null) {
            }
            a = 0;
            mediaLocation2 = mediaLocation;
            imageLocation2 = imageLocation;
            String imageUrl222 = null;
            String mediaUrl222 = null;
            boolean saveImageToCache2222 = saveImageToCache;
            while (a < i) {
            }
            String thumbKey6222 = thumbKey522;
            if (thumbLocation == null) {
            }
            if (mediaKey == null) {
            }
            mediaKey2 = mediaKey;
            if (imageKey != null) {
            }
            if (thumbKey == null) {
            }
            thumbKey2 = thumbKey;
            if (imageReceiver.getUniqKeyPrefix() == null) {
            }
            if (imageLocation2 == null) {
            }
            ImageLocation imageLocation3222 = imageLocation2;
            if (mediaLocation2 != null) {
            }
        }
        qualityThumb = false;
        imageKey = null;
        String thumbKey5222 = null;
        mediaKey = null;
        i = 2;
        if (imageLocation == null) {
        }
        imageExt = null;
        if (mediaLocation == null) {
        }
        mediaExt = null;
        thumbExt = imageReceiver.getExt();
        if (thumbExt != null) {
        }
        if (imageExt != null) {
        }
        if (mediaExt != null) {
        }
        a = 0;
        mediaLocation2 = mediaLocation;
        imageLocation2 = imageLocation;
        String imageUrl2222 = null;
        String mediaUrl2222 = null;
        boolean saveImageToCache22222 = saveImageToCache;
        while (a < i) {
        }
        String thumbKey62222 = thumbKey5222;
        if (thumbLocation == null) {
        }
        if (mediaKey == null) {
        }
        mediaKey2 = mediaKey;
        if (imageKey != null) {
        }
        if (thumbKey == null) {
        }
        thumbKey2 = thumbKey;
        if (imageReceiver.getUniqKeyPrefix() == null) {
        }
        if (imageLocation2 == null) {
        }
        ImageLocation imageLocation32222 = imageLocation2;
        if (mediaLocation2 != null) {
        }
    }

    public BitmapDrawable getFromLottieCache(String imageKey) {
        BitmapDrawable drawable = this.lottieMemCache.get(imageKey);
        if ((drawable instanceof AnimatedFileDrawable) && ((AnimatedFileDrawable) drawable).isRecycled()) {
            this.lottieMemCache.remove(imageKey);
            return null;
        }
        return drawable;
    }

    private boolean useLottieMemCache(ImageLocation imageLocation, String key) {
        return (imageLocation != null && (MessageObject.isAnimatedStickerDocument(imageLocation.document, true) || imageLocation.imageType == 1 || MessageObject.isVideoSticker(imageLocation.document))) || isAnimatedAvatar(key);
    }

    public void httpFileLoadError(final String location) {
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.m289lambda$httpFileLoadError$8$orgtelegrammessengerImageLoader(location);
            }
        });
    }

    /* renamed from: lambda$httpFileLoadError$8$org-telegram-messenger-ImageLoader */
    public /* synthetic */ void m289lambda$httpFileLoadError$8$orgtelegrammessengerImageLoader(String location) {
        CacheImage img = this.imageLoadingByUrl.get(location);
        if (img == null) {
            return;
        }
        HttpImageTask oldTask = img.httpTask;
        if (oldTask != null) {
            img.httpTask = new HttpImageTask(oldTask.cacheImage, oldTask.imageSize);
            this.httpTasks.add(img.httpTask);
        }
        runHttpTasks(false);
    }

    public void artworkLoadError(final String location) {
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.m282lambda$artworkLoadError$9$orgtelegrammessengerImageLoader(location);
            }
        });
    }

    /* renamed from: lambda$artworkLoadError$9$org-telegram-messenger-ImageLoader */
    public /* synthetic */ void m282lambda$artworkLoadError$9$orgtelegrammessengerImageLoader(String location) {
        CacheImage img = this.imageLoadingByUrl.get(location);
        if (img == null) {
            return;
        }
        ArtworkLoadTask oldTask = img.artworkTask;
        if (oldTask != null) {
            img.artworkTask = new ArtworkLoadTask(oldTask.cacheImage);
            this.artworkTasks.add(img.artworkTask);
        }
        runArtworkTasks(false);
    }

    public void fileDidLoaded(final String location, final File finalFile, final int mediaType) {
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.m288lambda$fileDidLoaded$10$orgtelegrammessengerImageLoader(location, mediaType, finalFile);
            }
        });
    }

    /* renamed from: lambda$fileDidLoaded$10$org-telegram-messenger-ImageLoader */
    public /* synthetic */ void m288lambda$fileDidLoaded$10$orgtelegrammessengerImageLoader(String location, int mediaType, File finalFile) {
        ThumbGenerateInfo info = this.waitingForQualityThumb.get(location);
        if (info != null && info.parentDocument != null) {
            generateThumb(mediaType, finalFile, info);
            this.waitingForQualityThumb.remove(location);
        }
        CacheImage img = this.imageLoadingByUrl.get(location);
        if (img == null) {
            return;
        }
        this.imageLoadingByUrl.remove(location);
        ArrayList<CacheOutTask> tasks = new ArrayList<>();
        for (int a = 0; a < img.imageReceiverArray.size(); a++) {
            String key = img.keys.get(a);
            String filter = img.filters.get(a);
            int type = img.types.get(a).intValue();
            ImageReceiver imageReceiver = img.imageReceiverArray.get(a);
            int guid = img.imageReceiverGuidsArray.get(a).intValue();
            CacheImage cacheImage = this.imageLoadingByKeys.get(key);
            if (cacheImage == null) {
                cacheImage = new CacheImage();
                cacheImage.secureDocument = img.secureDocument;
                cacheImage.currentAccount = img.currentAccount;
                cacheImage.finalFilePath = finalFile;
                cacheImage.parentObject = img.parentObject;
                cacheImage.key = key;
                cacheImage.imageLocation = img.imageLocation;
                cacheImage.type = type;
                cacheImage.ext = img.ext;
                cacheImage.encryptionKeyPath = img.encryptionKeyPath;
                cacheImage.cacheTask = new CacheOutTask(cacheImage);
                cacheImage.filter = filter;
                cacheImage.imageType = img.imageType;
                this.imageLoadingByKeys.put(key, cacheImage);
                tasks.add(cacheImage.cacheTask);
            }
            cacheImage.addImageReceiver(imageReceiver, key, filter, type, guid);
        }
        for (int a2 = 0; a2 < tasks.size(); a2++) {
            CacheOutTask task = tasks.get(a2);
            if (task.cacheImage.type == 1) {
                this.cacheThumbOutQueue.postRunnable(task);
            } else {
                this.cacheOutQueue.postRunnable(task);
            }
        }
    }

    public void fileDidFailedLoad(final String location, int canceled) {
        if (canceled == 1) {
            return;
        }
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.m287lambda$fileDidFailedLoad$11$orgtelegrammessengerImageLoader(location);
            }
        });
    }

    /* renamed from: lambda$fileDidFailedLoad$11$org-telegram-messenger-ImageLoader */
    public /* synthetic */ void m287lambda$fileDidFailedLoad$11$orgtelegrammessengerImageLoader(String location) {
        CacheImage img = this.imageLoadingByUrl.get(location);
        if (img != null) {
            img.setImageAndClear(null, null);
        }
    }

    public void runHttpTasks(boolean complete) {
        if (complete) {
            this.currentHttpTasksCount--;
        }
        while (this.currentHttpTasksCount < 4 && !this.httpTasks.isEmpty()) {
            HttpImageTask task = this.httpTasks.poll();
            if (task != null) {
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
                this.currentHttpTasksCount++;
            }
        }
    }

    public void runArtworkTasks(boolean complete) {
        if (complete) {
            this.currentArtworkTasksCount--;
        }
        while (this.currentArtworkTasksCount < 4 && !this.artworkTasks.isEmpty()) {
            try {
                ArtworkLoadTask task = this.artworkTasks.poll();
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
                this.currentArtworkTasksCount++;
            } catch (Throwable th) {
                runArtworkTasks(false);
            }
        }
    }

    public boolean isLoadingHttpFile(String url) {
        return this.httpFileLoadTasksByKeys.containsKey(url);
    }

    public static String getHttpFileName(String url) {
        return Utilities.MD5(url);
    }

    public static File getHttpFilePath(String url, String defaultExt) {
        String ext = getHttpUrlExtension(url, defaultExt);
        File directory = FileLoader.getDirectory(4);
        return new File(directory, Utilities.MD5(url) + "." + ext);
    }

    public void loadHttpFile(String url, String defaultExt, int currentAccount) {
        if (url == null || url.length() == 0 || this.httpFileLoadTasksByKeys.containsKey(url)) {
            return;
        }
        String ext = getHttpUrlExtension(url, defaultExt);
        File directory = FileLoader.getDirectory(4);
        File file = new File(directory, Utilities.MD5(url) + "_temp." + ext);
        file.delete();
        HttpFileTask task = new HttpFileTask(url, file, ext, currentAccount);
        this.httpFileLoadTasks.add(task);
        this.httpFileLoadTasksByKeys.put(url, task);
        runHttpFileLoadTasks(null, 0);
    }

    public void cancelLoadHttpFile(String url) {
        HttpFileTask task = this.httpFileLoadTasksByKeys.get(url);
        if (task != null) {
            task.cancel(true);
            this.httpFileLoadTasksByKeys.remove(url);
            this.httpFileLoadTasks.remove(task);
        }
        Runnable runnable = this.retryHttpsTasks.get(url);
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
        }
        runHttpFileLoadTasks(null, 0);
    }

    public void runHttpFileLoadTasks(final HttpFileTask oldTask, final int reason) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.m293xc74fbb45(oldTask, reason);
            }
        });
    }

    /* renamed from: lambda$runHttpFileLoadTasks$13$org-telegram-messenger-ImageLoader */
    public /* synthetic */ void m293xc74fbb45(HttpFileTask oldTask, int reason) {
        if (oldTask != null) {
            this.currentHttpFileLoadTasksCount--;
        }
        if (oldTask != null) {
            if (reason == 1) {
                if (!oldTask.canRetry) {
                    this.httpFileLoadTasksByKeys.remove(oldTask.url);
                    NotificationCenter.getInstance(oldTask.currentAccount).postNotificationName(NotificationCenter.httpFileDidFailedLoad, oldTask.url, 0);
                } else {
                    final HttpFileTask newTask = new HttpFileTask(oldTask.url, oldTask.tempFile, oldTask.ext, oldTask.currentAccount);
                    Runnable runnable = new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            ImageLoader.this.m292x531082e6(newTask);
                        }
                    };
                    this.retryHttpsTasks.put(oldTask.url, runnable);
                    AndroidUtilities.runOnUIThread(runnable, 1000L);
                }
            } else if (reason == 2) {
                this.httpFileLoadTasksByKeys.remove(oldTask.url);
                File file = new File(FileLoader.getDirectory(4), Utilities.MD5(oldTask.url) + "." + oldTask.ext);
                String result = oldTask.tempFile.renameTo(file) ? file.toString() : oldTask.tempFile.toString();
                NotificationCenter.getInstance(oldTask.currentAccount).postNotificationName(NotificationCenter.httpFileDidLoad, oldTask.url, result);
            }
        }
        while (this.currentHttpFileLoadTasksCount < 2 && !this.httpFileLoadTasks.isEmpty()) {
            HttpFileTask task = this.httpFileLoadTasks.poll();
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
            this.currentHttpFileLoadTasksCount++;
        }
    }

    /* renamed from: lambda$runHttpFileLoadTasks$12$org-telegram-messenger-ImageLoader */
    public /* synthetic */ void m292x531082e6(HttpFileTask newTask) {
        this.httpFileLoadTasks.add(newTask);
        runHttpFileLoadTasks(null, 0);
    }

    public static boolean shouldSendImageAsDocument(String path, Uri uri) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        if (path == null && uri != null && uri.getScheme() != null) {
            if (uri.getScheme().contains("file")) {
                path = uri.getPath();
            } else {
                try {
                    path = AndroidUtilities.getPath(uri);
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        }
        if (path != null) {
            BitmapFactory.decodeFile(path, bmOptions);
        } else if (uri != null) {
            try {
                InputStream inputStream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                BitmapFactory.decodeStream(inputStream, null, bmOptions);
                inputStream.close();
            } catch (Throwable e2) {
                FileLog.e(e2);
                return false;
            }
        }
        float photoW = bmOptions.outWidth;
        float photoH = bmOptions.outHeight;
        return photoW / photoH > 10.0f || photoH / photoW > 10.0f;
    }

    /* JADX WARN: Removed duplicated region for block: B:113:0x00bc A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0133 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x004c  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0050  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0081  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0086  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0090  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0094  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00a1  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00b3  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00c9  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x00f6 A[Catch: all -> 0x00c7, TRY_ENTER, TryCatch #4 {all -> 0x00c7, blocks: (B:49:0x00bc, B:60:0x00f6, B:61:0x0102, B:62:0x010e), top: B:113:0x00bc }] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0102 A[Catch: all -> 0x00c7, TryCatch #4 {all -> 0x00c7, blocks: (B:49:0x00bc, B:60:0x00f6, B:61:0x0102, B:62:0x010e), top: B:113:0x00bc }] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x010e A[Catch: all -> 0x00c7, TRY_LEAVE, TryCatch #4 {all -> 0x00c7, blocks: (B:49:0x00bc, B:60:0x00f6, B:61:0x0102, B:62:0x010e), top: B:113:0x00bc }] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0122  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x019c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Bitmap loadBitmap(String path, Uri uri, float maxWidth, float maxHeight, boolean useMaxScale) {
        String imageFilePath;
        float scaleFactor;
        float scaleFactor2;
        int sample;
        int orientation;
        float scaleFactor3;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        InputStream inputStream = null;
        if (path == null && uri != null && uri.getScheme() != null) {
            if (uri.getScheme().contains("file")) {
                imageFilePath = uri.getPath();
            } else if (Build.VERSION.SDK_INT < 30 || !"content".equals(uri.getScheme())) {
                try {
                    imageFilePath = AndroidUtilities.getPath(uri);
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
            if (imageFilePath == null) {
                BitmapFactory.decodeFile(imageFilePath, bmOptions);
            } else if (uri != null) {
                try {
                    InputStream inputStream2 = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                    BitmapFactory.decodeStream(inputStream2, null, bmOptions);
                    inputStream2.close();
                    inputStream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                } catch (Throwable e2) {
                    FileLog.e(e2);
                    return null;
                }
            }
            float photoW = bmOptions.outWidth;
            float photoH = bmOptions.outHeight;
            float f = photoW / maxWidth;
            float f2 = photoH / maxHeight;
            scaleFactor = !useMaxScale ? Math.max(f, f2) : Math.min(f, f2);
            if (scaleFactor < 1.0f) {
                scaleFactor2 = scaleFactor;
            } else {
                scaleFactor2 = 1.0f;
            }
            boolean z = false;
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = (int) scaleFactor2;
            if (bmOptions.inSampleSize % 2 != 0) {
                int sample2 = 1;
                while (sample2 * 2 < bmOptions.inSampleSize) {
                    sample2 *= 2;
                }
                bmOptions.inSampleSize = sample2;
            }
            sample = Build.VERSION.SDK_INT;
            if (sample < 21) {
                z = true;
            }
            bmOptions.inPurgeable = z;
            Matrix matrix = null;
            orientation = 0;
            if (imageFilePath == null) {
                try {
                    ExifInterface exif = new ExifInterface(imageFilePath);
                    orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                } catch (Throwable th) {
                }
            } else if (uri != null) {
                try {
                    InputStream stream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                    ExifInterface exif2 = new ExifInterface(stream);
                    orientation = exif2.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                    if (stream != null) {
                        stream.close();
                    }
                } catch (Throwable th2) {
                }
            }
            switch (orientation) {
                case 3:
                    matrix = new Matrix();
                    matrix.postRotate(180.0f);
                    break;
                case 6:
                    matrix = new Matrix();
                    matrix.postRotate(90.0f);
                    break;
                case 8:
                    matrix = new Matrix();
                    matrix.postRotate(270.0f);
                    break;
            }
            scaleFactor3 = scaleFactor2 / bmOptions.inSampleSize;
            if (scaleFactor3 > 1.0f) {
                if (matrix == null) {
                    matrix = new Matrix();
                }
                matrix.postScale(1.0f / scaleFactor3, 1.0f / scaleFactor3);
            }
            Bitmap b = null;
            if (imageFilePath == null) {
                try {
                    b = BitmapFactory.decodeFile(imageFilePath, bmOptions);
                    if (b != null) {
                        if (bmOptions.inPurgeable) {
                            Utilities.pinBitmap(b);
                        }
                        Bitmap newBitmap = Bitmaps.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
                        if (newBitmap != b) {
                            b.recycle();
                            return newBitmap;
                        }
                        return b;
                    }
                    return b;
                } catch (Throwable e3) {
                    FileLog.e(e3);
                    getInstance().clearMemory();
                    if (b == null) {
                        try {
                            b = BitmapFactory.decodeFile(imageFilePath, bmOptions);
                            if (b != null && bmOptions.inPurgeable) {
                                Utilities.pinBitmap(b);
                            }
                        } catch (Throwable e22) {
                            FileLog.e(e22);
                            return b;
                        }
                    }
                    if (b != null) {
                        Bitmap newBitmap2 = Bitmaps.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
                        if (newBitmap2 != b) {
                            b.recycle();
                            return newBitmap2;
                        }
                        return b;
                    }
                    return b;
                }
            } else if (uri == null) {
                return null;
            } else {
                try {
                    b = BitmapFactory.decodeStream(inputStream, null, bmOptions);
                    if (b != null) {
                        if (bmOptions.inPurgeable) {
                            Utilities.pinBitmap(b);
                        }
                        Bitmap newBitmap3 = Bitmaps.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
                        if (newBitmap3 != b) {
                            b.recycle();
                            b = newBitmap3;
                        }
                    }
                    inputStream.close();
                    return b;
                } catch (Throwable e4) {
                    FileLog.e(e4);
                    return b;
                }
            }
        }
        imageFilePath = path;
        if (imageFilePath == null) {
        }
        float photoW2 = bmOptions.outWidth;
        float photoH2 = bmOptions.outHeight;
        float f3 = photoW2 / maxWidth;
        float f22 = photoH2 / maxHeight;
        if (!useMaxScale) {
        }
        if (scaleFactor < 1.0f) {
        }
        boolean z2 = false;
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = (int) scaleFactor2;
        if (bmOptions.inSampleSize % 2 != 0) {
        }
        sample = Build.VERSION.SDK_INT;
        if (sample < 21) {
        }
        bmOptions.inPurgeable = z2;
        Matrix matrix2 = null;
        orientation = 0;
        if (imageFilePath == null) {
        }
        switch (orientation) {
            case 3:
                break;
            case 6:
                break;
            case 8:
                break;
        }
        scaleFactor3 = scaleFactor2 / bmOptions.inSampleSize;
        if (scaleFactor3 > 1.0f) {
        }
        Bitmap b2 = null;
        if (imageFilePath == null) {
        }
    }

    public static void fillPhotoSizeWithBytes(TLRPC.PhotoSize photoSize) {
        if (photoSize != null) {
            if (photoSize.bytes != null && photoSize.bytes.length != 0) {
                return;
            }
            File file = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(photoSize, true);
            try {
                RandomAccessFile f = new RandomAccessFile(file, "r");
                int len = (int) f.length();
                if (len < 20000) {
                    photoSize.bytes = new byte[(int) f.length()];
                    f.readFully(photoSize.bytes, 0, photoSize.bytes.length);
                }
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
    }

    private static TLRPC.PhotoSize scaleAndSaveImageInternal(TLRPC.PhotoSize photoSize, Bitmap bitmap, Bitmap.CompressFormat compressFormat, boolean progressive, int w, int h, float photoW, float photoH, float scaleFactor, int quality, boolean cache, boolean scaleAnyway, boolean forceCacheDir) throws Exception {
        Bitmap scaledBitmap;
        TLRPC.TL_fileLocationToBeDeprecated location;
        File fileDir;
        TLRPC.PhotoSize photoSize2 = photoSize;
        if (scaleFactor > 1.0f || scaleAnyway) {
            scaledBitmap = Bitmaps.createScaledBitmap(bitmap, w, h, true);
        } else {
            scaledBitmap = bitmap;
        }
        if (photoSize2 == null) {
        }
        if (photoSize2 == null || !(photoSize2.location instanceof TLRPC.TL_fileLocationToBeDeprecated)) {
            location = new TLRPC.TL_fileLocationToBeDeprecated();
            location.volume_id = -2147483648L;
            location.dc_id = Integer.MIN_VALUE;
            location.local_id = SharedConfig.getLastLocalId();
            location.file_reference = new byte[0];
            photoSize2 = new TLRPC.TL_photoSize_layer127();
            photoSize2.location = location;
            photoSize2.w = scaledBitmap.getWidth();
            photoSize2.h = scaledBitmap.getHeight();
            if (photoSize2.w <= 100 && photoSize2.h <= 100) {
                photoSize2.type = "s";
            } else if (photoSize2.w <= 320 && photoSize2.h <= 320) {
                photoSize2.type = "m";
            } else if (photoSize2.w <= 800 && photoSize2.h <= 800) {
                photoSize2.type = "x";
            } else if (photoSize2.w <= 1280 && photoSize2.h <= 1280) {
                photoSize2.type = "y";
            } else {
                photoSize2.type = "w";
            }
        } else {
            location = (TLRPC.TL_fileLocationToBeDeprecated) photoSize2.location;
        }
        String fileName = location.volume_id + "_" + location.local_id + ".jpg";
        if (!forceCacheDir) {
            if (location.volume_id != -2147483648L) {
                fileDir = FileLoader.getDirectory(0);
            } else {
                fileDir = FileLoader.getDirectory(4);
            }
        } else {
            fileDir = FileLoader.getDirectory(4);
        }
        File cacheFile = new File(fileDir, fileName);
        FileOutputStream stream = new FileOutputStream(cacheFile);
        scaledBitmap.compress(compressFormat, quality, stream);
        if (!cache) {
            photoSize2.size = (int) stream.getChannel().size();
        }
        stream.close();
        if (cache) {
            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
            scaledBitmap.compress(compressFormat, quality, stream2);
            photoSize2.bytes = stream2.toByteArray();
            photoSize2.size = photoSize2.bytes.length;
            stream2.close();
        }
        if (scaledBitmap != bitmap) {
            scaledBitmap.recycle();
        }
        return photoSize2;
    }

    public static TLRPC.PhotoSize scaleAndSaveImage(Bitmap bitmap, float maxWidth, float maxHeight, int quality, boolean cache) {
        return scaleAndSaveImage(null, bitmap, Bitmap.CompressFormat.JPEG, false, maxWidth, maxHeight, quality, cache, 0, 0, false);
    }

    public static TLRPC.PhotoSize scaleAndSaveImage(TLRPC.PhotoSize photoSize, Bitmap bitmap, float maxWidth, float maxHeight, int quality, boolean cache, boolean forceCacheDir) {
        return scaleAndSaveImage(photoSize, bitmap, Bitmap.CompressFormat.JPEG, false, maxWidth, maxHeight, quality, cache, 0, 0, forceCacheDir);
    }

    public static TLRPC.PhotoSize scaleAndSaveImage(Bitmap bitmap, float maxWidth, float maxHeight, int quality, boolean cache, int minWidth, int minHeight) {
        return scaleAndSaveImage(null, bitmap, Bitmap.CompressFormat.JPEG, false, maxWidth, maxHeight, quality, cache, minWidth, minHeight, false);
    }

    public static TLRPC.PhotoSize scaleAndSaveImage(Bitmap bitmap, float maxWidth, float maxHeight, boolean progressive, int quality, boolean cache, int minWidth, int minHeight) {
        return scaleAndSaveImage(null, bitmap, Bitmap.CompressFormat.JPEG, progressive, maxWidth, maxHeight, quality, cache, minWidth, minHeight, false);
    }

    public static TLRPC.PhotoSize scaleAndSaveImage(Bitmap bitmap, Bitmap.CompressFormat compressFormat, float maxWidth, float maxHeight, int quality, boolean cache, int minWidth, int minHeight) {
        return scaleAndSaveImage(null, bitmap, compressFormat, false, maxWidth, maxHeight, quality, cache, minWidth, minHeight, false);
    }

    public static TLRPC.PhotoSize scaleAndSaveImage(TLRPC.PhotoSize photoSize, Bitmap bitmap, Bitmap.CompressFormat compressFormat, boolean progressive, float maxWidth, float maxHeight, int quality, boolean cache, int minWidth, int minHeight, boolean forceCacheDir) {
        float scaleFactor;
        boolean scaleAnyway;
        float scaleFactor2;
        if (bitmap == null) {
            return null;
        }
        float photoW = bitmap.getWidth();
        float photoH = bitmap.getHeight();
        if (photoW != 0.0f && photoH != 0.0f) {
            float scaleFactor3 = Math.max(photoW / maxWidth, photoH / maxHeight);
            if (minWidth != 0 && minHeight != 0 && (photoW < minWidth || photoH < minHeight)) {
                if (photoW < minWidth && photoH > minHeight) {
                    scaleFactor2 = photoW / minWidth;
                } else if (photoW > minWidth && photoH < minHeight) {
                    scaleFactor2 = photoH / minHeight;
                } else {
                    scaleFactor2 = Math.max(photoW / minWidth, photoH / minHeight);
                }
                scaleAnyway = true;
                scaleFactor = scaleFactor2;
            } else {
                scaleAnyway = false;
                scaleFactor = scaleFactor3;
            }
            int w = (int) (photoW / scaleFactor);
            int h = (int) (photoH / scaleFactor);
            if (h != 0 && w != 0) {
                try {
                    return scaleAndSaveImageInternal(photoSize, bitmap, compressFormat, progressive, w, h, photoW, photoH, scaleFactor, quality, cache, scaleAnyway, forceCacheDir);
                } catch (Throwable e) {
                    FileLog.e(e);
                    getInstance().clearMemory();
                    System.gc();
                    try {
                        return scaleAndSaveImageInternal(photoSize, bitmap, compressFormat, progressive, w, h, photoW, photoH, scaleFactor, quality, cache, scaleAnyway, forceCacheDir);
                    } catch (Throwable e2) {
                        FileLog.e(e2);
                        return null;
                    }
                }
            }
            return null;
        }
        return null;
    }

    public static String getHttpUrlExtension(String url, String defaultExt) {
        String ext = null;
        String last = Uri.parse(url).getLastPathSegment();
        if (!TextUtils.isEmpty(last) && last.length() > 1) {
            url = last;
        }
        int idx = url.lastIndexOf(46);
        if (idx != -1) {
            ext = url.substring(idx + 1);
        }
        if (ext == null || ext.length() == 0 || ext.length() > 4) {
            return defaultExt;
        }
        return ext;
    }

    public static void saveMessageThumbs(TLRPC.Message message) {
        TLRPC.PhotoSize photoSize;
        boolean isEncrypted;
        File file;
        if (message.media != null && (photoSize = findPhotoCachedSize(message)) != null && photoSize.bytes != null && photoSize.bytes.length != 0) {
            if (photoSize.location == null || (photoSize.location instanceof TLRPC.TL_fileLocationUnavailable)) {
                photoSize.location = new TLRPC.TL_fileLocationToBeDeprecated();
                photoSize.location.volume_id = -2147483648L;
                photoSize.location.local_id = SharedConfig.getLastLocalId();
            }
            File file2 = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(photoSize, true);
            if (!MessageObject.shouldEncryptPhotoOrVideo(message)) {
                isEncrypted = false;
                file = file2;
            } else {
                isEncrypted = true;
                file = new File(file2.getAbsolutePath() + ".enc");
            }
            if (!file.exists()) {
                if (isEncrypted) {
                    try {
                        File keyPath = new File(FileLoader.getInternalCacheDir(), file.getName() + ".key");
                        RandomAccessFile keyFile = new RandomAccessFile(keyPath, "rws");
                        long len = keyFile.length();
                        byte[] encryptKey = new byte[32];
                        byte[] encryptIv = new byte[16];
                        if (len > 0 && len % 48 == 0) {
                            keyFile.read(encryptKey, 0, 32);
                            keyFile.read(encryptIv, 0, 16);
                        } else {
                            Utilities.random.nextBytes(encryptKey);
                            Utilities.random.nextBytes(encryptIv);
                            keyFile.write(encryptKey);
                            keyFile.write(encryptIv);
                        }
                        keyFile.close();
                        Utilities.aesCtrDecryptionByteArray(photoSize.bytes, encryptKey, encryptIv, 0, photoSize.bytes.length, 0);
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
                RandomAccessFile writeFile = new RandomAccessFile(file, "rws");
                writeFile.write(photoSize.bytes);
                writeFile.close();
            }
            TLRPC.TL_photoSize newPhotoSize = new TLRPC.TL_photoSize_layer127();
            newPhotoSize.w = photoSize.w;
            newPhotoSize.h = photoSize.h;
            newPhotoSize.location = photoSize.location;
            newPhotoSize.size = photoSize.size;
            newPhotoSize.type = photoSize.type;
            if (message.media instanceof TLRPC.TL_messageMediaPhoto) {
                int count = message.media.photo.sizes.size();
                for (int a = 0; a < count; a++) {
                    TLRPC.PhotoSize size = message.media.photo.sizes.get(a);
                    if (size instanceof TLRPC.TL_photoCachedSize) {
                        message.media.photo.sizes.set(a, newPhotoSize);
                        return;
                    }
                }
            } else if (message.media instanceof TLRPC.TL_messageMediaDocument) {
                int count2 = message.media.document.thumbs.size();
                for (int a2 = 0; a2 < count2; a2++) {
                    TLRPC.PhotoSize size2 = message.media.document.thumbs.get(a2);
                    if (size2 instanceof TLRPC.TL_photoCachedSize) {
                        message.media.document.thumbs.set(a2, newPhotoSize);
                        return;
                    }
                }
            } else if (message.media instanceof TLRPC.TL_messageMediaWebPage) {
                int count3 = message.media.webpage.photo.sizes.size();
                for (int a3 = 0; a3 < count3; a3++) {
                    TLRPC.PhotoSize size3 = message.media.webpage.photo.sizes.get(a3);
                    if (size3 instanceof TLRPC.TL_photoCachedSize) {
                        message.media.webpage.photo.sizes.set(a3, newPhotoSize);
                        return;
                    }
                }
            }
        }
    }

    private static TLRPC.PhotoSize findPhotoCachedSize(TLRPC.Message message) {
        if (message.media instanceof TLRPC.TL_messageMediaPhoto) {
            int count = message.media.photo.sizes.size();
            for (int a = 0; a < count; a++) {
                TLRPC.PhotoSize size = message.media.photo.sizes.get(a);
                if (size instanceof TLRPC.TL_photoCachedSize) {
                    return size;
                }
            }
            return null;
        } else if (message.media instanceof TLRPC.TL_messageMediaDocument) {
            int count2 = message.media.document.thumbs.size();
            for (int a2 = 0; a2 < count2; a2++) {
                TLRPC.PhotoSize size2 = message.media.document.thumbs.get(a2);
                if (size2 instanceof TLRPC.TL_photoCachedSize) {
                    return size2;
                }
            }
            return null;
        } else if (!(message.media instanceof TLRPC.TL_messageMediaWebPage) || message.media.webpage.photo == null) {
            return null;
        } else {
            int count3 = message.media.webpage.photo.sizes.size();
            for (int a3 = 0; a3 < count3; a3++) {
                TLRPC.PhotoSize size3 = message.media.webpage.photo.sizes.get(a3);
                if (size3 instanceof TLRPC.TL_photoCachedSize) {
                    return size3;
                }
            }
            return null;
        }
    }

    public static void saveMessagesThumbs(ArrayList<TLRPC.Message> messages) {
        if (messages == null || messages.isEmpty()) {
            return;
        }
        for (int a = 0; a < messages.size(); a++) {
            TLRPC.Message message = messages.get(a);
            saveMessageThumbs(message);
        }
    }

    public static MessageThumb generateMessageThumb(TLRPC.Message message) {
        Bitmap b;
        Bitmap bitmap;
        TLRPC.PhotoSize photoSize = findPhotoCachedSize(message);
        int i = 3;
        if (photoSize != null && photoSize.bytes != null && photoSize.bytes.length != 0) {
            File file = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(photoSize, true);
            TLRPC.TL_photoSize newPhotoSize = new TLRPC.TL_photoSize_layer127();
            newPhotoSize.w = photoSize.w;
            newPhotoSize.h = photoSize.h;
            newPhotoSize.location = photoSize.location;
            newPhotoSize.size = photoSize.size;
            newPhotoSize.type = photoSize.type;
            if (file.exists() && message.grouped_id == 0) {
                int h = photoSize.h;
                int w = photoSize.w;
                Point point = ChatMessageCell.getMessageSize(w, h);
                String key = String.format(Locale.US, "%d_%d@%d_%d_b", Long.valueOf(photoSize.location.volume_id), Integer.valueOf(photoSize.location.local_id), Integer.valueOf((int) (point.x / AndroidUtilities.density)), Integer.valueOf((int) (point.y / AndroidUtilities.density)));
                if (!getInstance().isInMemCache(key, false) && (bitmap = loadBitmap(file.getPath(), null, (int) (point.x / AndroidUtilities.density), (int) (point.y / AndroidUtilities.density), false)) != null) {
                    Utilities.blurBitmap(bitmap, 3, 1, bitmap.getWidth(), bitmap.getHeight(), bitmap.getRowBytes());
                    Bitmap scaledBitmap = Bitmaps.createScaledBitmap(bitmap, (int) (point.x / AndroidUtilities.density), (int) (point.y / AndroidUtilities.density), true);
                    if (scaledBitmap != bitmap) {
                        bitmap.recycle();
                        bitmap = scaledBitmap;
                    }
                    return new MessageThumb(key, new BitmapDrawable(bitmap));
                }
            }
        } else if (message.media instanceof TLRPC.TL_messageMediaDocument) {
            int a = 0;
            int count = message.media.document.thumbs.size();
            while (a < count) {
                TLRPC.PhotoSize size = message.media.document.thumbs.get(a);
                if (size instanceof TLRPC.TL_photoStrippedSize) {
                    TLRPC.PhotoSize thumbSize = FileLoader.getClosestPhotoSizeWithSize(message.media.document.thumbs, GroupCallActivity.TABLET_LIST_SIZE);
                    int h2 = 0;
                    int w2 = 0;
                    if (thumbSize != null) {
                        h2 = thumbSize.h;
                        w2 = thumbSize.w;
                    } else {
                        int k = 0;
                        while (true) {
                            if (k >= message.media.document.attributes.size()) {
                                break;
                            } else if (!(message.media.document.attributes.get(k) instanceof TLRPC.TL_documentAttributeVideo)) {
                                k++;
                            } else {
                                TLRPC.TL_documentAttributeVideo videoAttribute = (TLRPC.TL_documentAttributeVideo) message.media.document.attributes.get(k);
                                h2 = videoAttribute.h;
                                w2 = videoAttribute.w;
                                break;
                            }
                        }
                    }
                    Point point2 = ChatMessageCell.getMessageSize(w2, h2);
                    Locale locale = Locale.US;
                    Object[] objArr = new Object[i];
                    objArr[0] = ImageLocation.getStrippedKey(message, message, size);
                    objArr[1] = Integer.valueOf((int) (point2.x / AndroidUtilities.density));
                    objArr[2] = Integer.valueOf((int) (point2.y / AndroidUtilities.density));
                    String key2 = String.format(locale, "%s_false@%d_%d_b", objArr);
                    if (!getInstance().isInMemCache(key2, false) && (b = getStrippedPhotoBitmap(size.bytes, null)) != null) {
                        Utilities.blurBitmap(b, 3, 1, b.getWidth(), b.getHeight(), b.getRowBytes());
                        Bitmap scaledBitmap2 = Bitmaps.createScaledBitmap(b, (int) (point2.x / AndroidUtilities.density), (int) (point2.y / AndroidUtilities.density), true);
                        if (scaledBitmap2 != b) {
                            b.recycle();
                            b = scaledBitmap2;
                        }
                        return new MessageThumb(key2, new BitmapDrawable(b));
                    }
                }
                a++;
                i = 3;
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

    /* loaded from: classes4.dex */
    public static class MessageThumb {
        BitmapDrawable drawable;
        String key;

        public MessageThumb(String key, BitmapDrawable bitmapDrawable) {
            this.key = key;
            this.drawable = bitmapDrawable;
        }
    }
}
