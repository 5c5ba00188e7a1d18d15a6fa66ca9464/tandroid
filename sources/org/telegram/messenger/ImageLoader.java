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

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 5 implements FileLoader.FileLoaderDelegate {
        final /* synthetic */ int val$currentAccount;

        5(int i) {
            this.val$currentAccount = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$fileDidFailedLoad$6(String str, int i, int i2) {
            ImageLoader.this.fileDidFailedLoad(str, i);
            NotificationCenter.getInstance(i2).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileLoadFailed, str, Integer.valueOf(i));
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

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$fileDidLoaded$5(File file, String str, int i, Object obj, int i2) {
            FilePathDatabase.FileMeta fileMetadataFromParent;
            if (file != null && ((str.endsWith(".mp4") || str.endsWith(".jpg")) && (fileMetadataFromParent = FileLoader.getFileMetadataFromParent(i, obj)) != null)) {
                MessageObject messageObject = obj instanceof MessageObject ? (MessageObject) obj : null;
                long j = fileMetadataFromParent.dialogId;
                if (SaveToGallerySettingsHelper.needSave(j >= 0 ? 1 : ChatObject.isChannelAndNotMegaGroup(MessagesController.getInstance(i).getChat(Long.valueOf(-j))) ? 4 : 2, fileMetadataFromParent, messageObject, i)) {
                    AndroidUtilities.addMediaToGallery(file.toString());
                }
            }
            NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileLoaded, str, file);
            ImageLoader.this.fileDidLoaded(str, file, i2);
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

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$fileUploadProgressChanged$0(int i, String str, long j, long j2, boolean z) {
            NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileUploadProgressChanged, str, Long.valueOf(j), Long.valueOf(j2), Boolean.valueOf(z));
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
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 6 extends BroadcastReceiver {
        6() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0() {
            ImageLoader.this.checkMediaPaths();
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

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCancelled$2() {
            ImageLoader.this.runArtworkTasks(true);
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

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Code restructure failed: missing block: B:105:0x0132, code lost:
            if (r2 == null) goto L79;
         */
        /* JADX WARN: Code restructure failed: missing block: B:92:0x0118, code lost:
            if ((r3 instanceof java.io.FileNotFoundException) != false) goto L96;
         */
        /* JADX WARN: Removed duplicated region for block: B:110:0x013a A[Catch: all -> 0x013e, TRY_LEAVE, TryCatch #17 {all -> 0x013e, blocks: (B:108:0x0136, B:110:0x013a), top: B:161:0x0136 }] */
        /* JADX WARN: Removed duplicated region for block: B:133:0x014b A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:139:0x0141 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:153:0x012a A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:76:0x00f1 A[Catch: all -> 0x00f8, TryCatch #15 {all -> 0x00f8, blocks: (B:74:0x00ed, B:76:0x00f1, B:93:0x011a, B:94:0x011c, B:81:0x00fa, B:84:0x00ff, B:86:0x0103, B:88:0x0109, B:91:0x0116), top: B:157:0x00ed }] */
        /* JADX WARN: Removed duplicated region for block: B:81:0x00fa A[Catch: all -> 0x00f8, TryCatch #15 {all -> 0x00f8, blocks: (B:74:0x00ed, B:76:0x00f1, B:93:0x011a, B:94:0x011c, B:81:0x00fa, B:84:0x00ff, B:86:0x0103, B:88:0x0109, B:91:0x0116), top: B:157:0x00ed }] */
        /* JADX WARN: Removed duplicated region for block: B:97:0x0123 A[Catch: all -> 0x0127, TRY_LEAVE, TryCatch #8 {all -> 0x0127, blocks: (B:95:0x011f, B:97:0x0123), top: B:143:0x011f }] */
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
            HttpURLConnection httpURLConnection2;
            JSONArray jSONArray;
            int read;
            int responseCode;
            try {
                HttpURLConnection httpURLConnection3 = (HttpURLConnection) new URL(this.cacheImage.imageLocation.path.replace("athumb://", "https://")).openConnection();
                this.httpConnection = httpURLConnection3;
                httpURLConnection3.setConnectTimeout(5000);
                this.httpConnection.setReadTimeout(5000);
                this.httpConnection.connect();
                try {
                    HttpURLConnection httpURLConnection4 = this.httpConnection;
                    if (httpURLConnection4 != null && (responseCode = httpURLConnection4.getResponseCode()) != 200 && responseCode != 202 && responseCode != 304) {
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
                        if (!(th instanceof UnknownHostException)) {
                            if (th instanceof SocketException) {
                                if (th.getMessage() != null && th.getMessage().contains("ECONNRESET")) {
                                }
                                FileLog.e(th, false);
                                httpURLConnection2 = this.httpConnection;
                                if (httpURLConnection2 != null) {
                                }
                                if (inputStream2 != null) {
                                }
                            }
                        }
                        this.canRetry = false;
                        FileLog.e(th, false);
                        httpURLConnection2 = this.httpConnection;
                        if (httpURLConnection2 != null) {
                        }
                        if (inputStream2 != null) {
                        }
                    } else {
                        if (ApplicationLoader.isNetworkOnline()) {
                            this.canRetry = false;
                        }
                        FileLog.e(th, false);
                        try {
                            httpURLConnection2 = this.httpConnection;
                            if (httpURLConnection2 != null) {
                                httpURLConnection2.disconnect();
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
                    }
                } finally {
                    httpURLConnection = this.httpConnection;
                    if (httpURLConnection != null) {
                    }
                    if (inputStream2 != null) {
                    }
                    if (byteArrayOutputStream != null) {
                    }
                }
                try {
                    httpURLConnection = this.httpConnection;
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
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
                httpURLConnection = this.httpConnection;
                if (httpURLConnection != null) {
                }
                if (inputStream2 != null) {
                }
                if (byteArrayOutputStream != null) {
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

        @Override // android.os.AsyncTask
        protected void onCancelled() {
            ImageLoader.this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$ArtworkLoadTask$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ImageLoader.ArtworkLoadTask.this.lambda$onCancelled$2();
                }
            });
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
                    ImageLoader imageLoader = ImageLoader.this;
                    if (i == 1) {
                        imageLoader.cacheThumbOutQueue.cancelRunnable(this.cacheTask);
                    } else {
                        imageLoader.cacheOutQueue.cancelRunnable(this.cacheTask);
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

        public void setImageReceiverGuid(ImageReceiver imageReceiver, int i) {
            int indexOf = this.imageReceiverArray.indexOf(imageReceiver);
            if (indexOf == -1) {
                return;
            }
            this.imageReceiverGuidsArray.set(indexOf, Integer.valueOf(i));
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
            } else {
                int i2 = tLRPC$WallPaperSettings2.third_background_color;
                int alphaComponent = ColorUtils.setAlphaComponent(tLRPC$WallPaperSettings2.background_color, NotificationCenter.voipServiceCreated);
                if (i2 == 0) {
                    int alphaComponent2 = ColorUtils.setAlphaComponent(tLRPC$WallPaper.settings.second_background_color, NotificationCenter.voipServiceCreated);
                    int averageColor = AndroidUtilities.getAverageColor(alphaComponent, alphaComponent2);
                    GradientDrawable gradientDrawable = new GradientDrawable(BackgroundGradientDrawable.getGradientOrientation(tLRPC$WallPaper.settings.rotation), new int[]{alphaComponent, alphaComponent2});
                    gradientDrawable.setBounds(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
                    gradientDrawable.draw(canvas);
                    i = averageColor;
                } else {
                    int alphaComponent3 = ColorUtils.setAlphaComponent(tLRPC$WallPaper.settings.second_background_color, NotificationCenter.voipServiceCreated);
                    int alphaComponent4 = ColorUtils.setAlphaComponent(tLRPC$WallPaper.settings.third_background_color, NotificationCenter.voipServiceCreated);
                    int i3 = tLRPC$WallPaper.settings.fourth_background_color;
                    int alphaComponent5 = i3 == 0 ? 0 : ColorUtils.setAlphaComponent(i3, NotificationCenter.voipServiceCreated);
                    int patternColor = MotionBackgroundDrawable.getPatternColor(alphaComponent, alphaComponent3, alphaComponent4, alphaComponent5);
                    MotionBackgroundDrawable motionBackgroundDrawable = new MotionBackgroundDrawable();
                    motionBackgroundDrawable.setColors(alphaComponent, alphaComponent3, alphaComponent4, alphaComponent5);
                    motionBackgroundDrawable.setBounds(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
                    motionBackgroundDrawable.setPatternBitmap(tLRPC$WallPaper.settings.intensity, bitmap);
                    motionBackgroundDrawable.draw(canvas);
                    i = patternColor;
                    z = false;
                }
            }
            if (z) {
                Paint paint = new Paint(2);
                paint.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.SRC_IN));
                paint.setAlpha((int) ((tLRPC$WallPaper.settings.intensity / 100.0f) * 255.0f));
                canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
            }
            return createBitmap;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPostExecute$0(Drawable drawable, String str) {
            this.cacheImage.setImageAndClear(drawable, str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code restructure failed: missing block: B:39:0x00e7, code lost:
            if (r3 != false) goto L10;
         */
        /* JADX WARN: Code restructure failed: missing block: B:8:0x002c, code lost:
            if (r6 != null) goto L10;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
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
            } else if (drawable instanceof AnimatedFileDrawable) {
                AnimatedFileDrawable animatedFileDrawable = (AnimatedFileDrawable) drawable;
                if (animatedFileDrawable.isWebmSticker) {
                    drawable = ImageLoader.this.getFromLottieCache(this.cacheImage.key);
                    if (drawable == null) {
                        ImageLoader.this.lottieMemCache.put(this.cacheImage.key, animatedFileDrawable);
                        drawable = animatedFileDrawable;
                    } else {
                        animatedFileDrawable.recycle();
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
                        ((this.cacheImage.key.endsWith("_isc") || ((float) bitmapDrawable.getBitmap().getWidth()) > AndroidUtilities.density * 80.0f || ((float) bitmapDrawable.getBitmap().getHeight()) > AndroidUtilities.density * 80.0f) ? ImageLoader.this.memCache : ImageLoader.this.smallImagesMemCache).put(this.cacheImage.key, bitmapDrawable);
                        z = true;
                    }
                    z2 = z;
                    drawable = bitmapDrawable;
                } else {
                    AndroidUtilities.recycleBitmap(bitmapDrawable.getBitmap());
                    drawable = fromMemCache;
                }
                if (drawable != null) {
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

        /* JADX WARN: Can't wrap try/catch for region: R(10:910|(2:912|(8:914|915|916|(1:918)(1:931)|919|(2:925|(1:927))|(1:929)|17))|934|915|916|(0)(0)|919|(4:921|923|925|(0))|(0)|17) */
        /* JADX WARN: Can't wrap try/catch for region: R(15:71|(7:72|73|74|75|(1:77)(1:119)|78|79)|(3:81|82|(7:84|85|86|(1:113)(1:89)|(3:98|(1:112)(4:101|(1:105)|106|(1:110))|111)(1:93)|94|(1:96)(1:97)))|118|85|86|(0)|113|(0)|98|(0)|112|111|94|(0)(0)) */
        /* JADX WARN: Can't wrap try/catch for region: R(26:327|(1:909)(1:334)|335|(3:337|(1:907)|341)(1:908)|342|(22:344|(3:346|(1:348)(1:896)|349)(4:897|(2:899|(2:901|902))(1:904)|903|902)|350|351|(1:353)|354|355|356|(22:358|(5:360|361|362|363|364)(1:861)|365|366|(1:368)(2:841|(1:843)(2:844|(1:846)(2:847|(1:849)(1:850))))|369|370|371|372|(1:374)(2:832|(1:834))|375|376|(1:831)(10:380|381|(2:793|(13:801|802|(1:824)(1:806)|(1:808)|809|810|811|812|(4:817|818|819|(1:821))|822|818|819|(0))(4:795|(1:797)(1:800)|798|799))(2:(4:784|785|786|787)(1:385)|386)|387|(1:783)(1:391)|392|393|(1:395)|396|(1:775)(3:402|(2:403|(1:406)(1:405))|407))|408|409|410|(3:660|661|853)(6:412|(1:414)(1:659)|(3:649|650|(4:652|(1:656)|417|ae3))|416|417|ae3)|455|(3:458|(1:460)(1:462)|461)|(2:468|(1:470))|471|(2:(1:483)|17)(2:(1:478)|17))(12:862|(10:864|865|866|(1:868)(1:889)|869|871|872|(1:874)|875|(4:877|(2:878|(1:881)(1:880))|882|883)(1:886))(1:893)|884|410|(0)(0)|455|(3:458|(0)(0)|461)|(4:464|466|468|(0))|471|(1:473)|(0)|17)|776|777|778|409|410|(0)(0)|455|(0)|(0)|471|(0)|(0)|17)|906|351|(0)|354|355|356|(0)(0)|776|777|778|409|410|(0)(0)|455|(0)|(0)|471|(0)|(0)|17) */
        /* JADX WARN: Code restructure failed: missing block: B:122:0x020c, code lost:
            r0 = e;
         */
        /* JADX WARN: Code restructure failed: missing block: B:276:0x0428, code lost:
            if (r40.this$0.isAnimatedAvatar(r40.cacheImage.filter) != false) goto L254;
         */
        /* JADX WARN: Code restructure failed: missing block: B:278:0x042b, code lost:
            r9 = false;
            r0 = r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:315:0x04e6, code lost:
            if (r40.this$0.isAnimatedAvatar(r40.cacheImage.filter) != false) goto L254;
         */
        /* JADX WARN: Code restructure failed: missing block: B:359:0x0599, code lost:
            if (r0.startsWith("http") == false) goto L903;
         */
        /* JADX WARN: Code restructure failed: missing block: B:381:0x05f1, code lost:
            r0 = th;
         */
        /* JADX WARN: Code restructure failed: missing block: B:382:0x05f2, code lost:
            r25 = r11;
            r26 = r12;
         */
        /* JADX WARN: Code restructure failed: missing block: B:518:0x082a, code lost:
            r0 = th;
         */
        /* JADX WARN: Code restructure failed: missing block: B:519:0x082b, code lost:
            r39 = r6;
            r6 = r4;
            r4 = r39;
         */
        /* JADX WARN: Code restructure failed: missing block: B:706:0x0b9c, code lost:
            if (r4.contains("exif") != false) goto L609;
         */
        /* JADX WARN: Code restructure failed: missing block: B:892:0x0e52, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:893:0x0e53, code lost:
            org.telegram.messenger.FileLog.e(r0);
            r2 = null;
         */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Not initialized variable reg: 36, insn: 0x08d1: MOVE  (r8 I:??[OBJECT, ARRAY]) = (r36 I:??[OBJECT, ARRAY]), block:B:566:0x08d1 */
        /* JADX WARN: Removed duplicated region for block: B:139:0x022c A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:144:0x0234 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:150:0x0243 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:165:0x0270  */
        /* JADX WARN: Removed duplicated region for block: B:166:0x028d  */
        /* JADX WARN: Removed duplicated region for block: B:168:0x02a8 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:199:0x0309  */
        /* JADX WARN: Removed duplicated region for block: B:213:0x033a  */
        /* JADX WARN: Removed duplicated region for block: B:226:0x0369  */
        /* JADX WARN: Removed duplicated region for block: B:227:0x036e  */
        /* JADX WARN: Removed duplicated region for block: B:297:0x0482  */
        /* JADX WARN: Removed duplicated region for block: B:298:0x0484  */
        /* JADX WARN: Removed duplicated region for block: B:300:0x048c  */
        /* JADX WARN: Removed duplicated region for block: B:301:0x048e  */
        /* JADX WARN: Removed duplicated region for block: B:304:0x0495  */
        /* JADX WARN: Removed duplicated region for block: B:305:0x0498  */
        /* JADX WARN: Removed duplicated region for block: B:308:0x04a4  */
        /* JADX WARN: Removed duplicated region for block: B:309:0x04a7  */
        /* JADX WARN: Removed duplicated region for block: B:312:0x04d0  */
        /* JADX WARN: Removed duplicated region for block: B:318:0x04ec  */
        /* JADX WARN: Removed duplicated region for block: B:323:0x0506  */
        /* JADX WARN: Removed duplicated region for block: B:365:0x05b2  */
        /* JADX WARN: Removed duplicated region for block: B:369:0x05c2 A[Catch: all -> 0x05f1, TryCatch #7 {all -> 0x05f1, blocks: (B:367:0x05bc, B:369:0x05c2, B:371:0x05cb), top: B:913:0x05bc }] */
        /* JADX WARN: Removed duplicated region for block: B:450:0x0700 A[Catch: all -> 0x06f4, TryCatch #15 {all -> 0x06f4, blocks: (B:459:0x0727, B:463:0x0735, B:469:0x0750, B:476:0x0760, B:478:0x076a, B:465:0x0741, B:438:0x06e4, B:440:0x06ea, B:448:0x06f9, B:450:0x0700, B:455:0x0710, B:458:0x0721, B:457:0x071b), top: B:927:0x0666 }] */
        /* JADX WARN: Removed duplicated region for block: B:491:0x07a6  */
        /* JADX WARN: Removed duplicated region for block: B:641:0x0a95  */
        /* JADX WARN: Removed duplicated region for block: B:659:0x0ae4 A[Catch: all -> 0x0aea, TRY_ENTER, TryCatch #26 {all -> 0x0acb, blocks: (B:646:0x0aa0, B:648:0x0aac, B:650:0x0ac1, B:652:0x0ac7, B:657:0x0ad8, B:658:0x0ae3, B:667:0x0af0, B:670:0x0af8, B:673:0x0aff, B:675:0x0b04, B:679:0x0b0f, B:681:0x0b19, B:686:0x0b45, B:688:0x0b56, B:674:0x0b02, B:659:0x0ae4, B:661:0x0ae8, B:665:0x0aed), top: B:945:0x0aa0 }] */
        /* JADX WARN: Removed duplicated region for block: B:692:0x0b6a  */
        /* JADX WARN: Removed duplicated region for block: B:772:0x0c7b  */
        /* JADX WARN: Removed duplicated region for block: B:774:0x0c80  */
        /* JADX WARN: Removed duplicated region for block: B:781:0x0c9f A[Catch: all -> 0x0c74, TryCatch #12 {all -> 0x0c74, blocks: (B:775:0x0c82, B:777:0x0c8c, B:779:0x0c92, B:781:0x0c9f, B:783:0x0ca5, B:789:0x0cbc, B:795:0x0ccc, B:797:0x0cd2, B:798:0x0cd7, B:802:0x0ce4, B:805:0x0cec, B:799:0x0cda, B:801:0x0ce0, B:808:0x0cf4, B:810:0x0d02, B:812:0x0d0d, B:768:0x0c70), top: B:923:0x0c70 }] */
        /* JADX WARN: Removed duplicated region for block: B:79:0x0154  */
        /* JADX WARN: Removed duplicated region for block: B:852:0x0d9f A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:855:0x0dad  */
        /* JADX WARN: Removed duplicated region for block: B:856:0x0daf  */
        /* JADX WARN: Removed duplicated region for block: B:859:0x0dc4  */
        /* JADX WARN: Removed duplicated region for block: B:865:0x0de3  */
        /* JADX WARN: Removed duplicated region for block: B:868:0x0ded  */
        /* JADX WARN: Removed duplicated region for block: B:879:0x0e0b  */
        /* JADX WARN: Removed duplicated region for block: B:888:0x0e4a  */
        /* JADX WARN: Removed duplicated region for block: B:889:0x0e4c  */
        /* JADX WARN: Removed duplicated region for block: B:901:0x0e78  */
        /* JADX WARN: Removed duplicated region for block: B:903:0x0e80  */
        /* JADX WARN: Removed duplicated region for block: B:961:0x0848 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:965:0x02c0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Type inference failed for: r2v54 */
        /* JADX WARN: Type inference failed for: r2v55 */
        /* JADX WARN: Type inference failed for: r2v56 */
        /* JADX WARN: Type inference failed for: r2v57 */
        /* JADX WARN: Type inference failed for: r2v58 */
        /* JADX WARN: Type inference failed for: r2v59 */
        /* JADX WARN: Type inference failed for: r2v60 */
        /* JADX WARN: Type inference failed for: r2v84 */
        /* JADX WARN: Type inference failed for: r40v0, types: [org.telegram.messenger.ImageLoader$CacheOutTask] */
        /* JADX WARN: Type inference failed for: r4v48 */
        /* JADX WARN: Type inference failed for: r4v50 */
        /* JADX WARN: Type inference failed for: r4v52 */
        /* JADX WARN: Type inference failed for: r4v54 */
        /* JADX WARN: Type inference failed for: r4v61 */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void run() {
            Drawable themePreviewDrawable;
            Bitmap bitmap;
            Object obj;
            SecureDocumentKey secureDocumentKey;
            byte[] bArr;
            String str;
            Long l;
            boolean z;
            boolean z2;
            Long l2;
            boolean z3;
            float f;
            float f2;
            Bitmap bitmap2;
            char c;
            boolean z4;
            int i;
            float f3;
            float f4;
            char c2;
            boolean z5;
            boolean z6;
            boolean z7;
            SecureDocumentKey secureDocumentKey2;
            boolean z8;
            int i2;
            Bitmap bitmap3;
            Bitmap bitmap4;
            boolean z9;
            boolean z10;
            int i3;
            int i4;
            int i5;
            int i6;
            int i7;
            int rowBytes;
            int i8;
            int i9;
            Bitmap createScaledBitmap;
            int i10;
            int i11;
            int i12;
            CacheImage cacheImage;
            String str2;
            Object obj2;
            boolean z11;
            float f5;
            SecureDocumentKey secureDocumentKey3;
            boolean z12;
            Bitmap.Config config;
            boolean z13;
            int i13;
            Bitmap bitmap5;
            FileInputStream fileInputStream;
            Pair<Integer, Integer> imageOrientation;
            int intValue;
            SecureDocumentKey secureDocumentKey4;
            int intValue2;
            boolean z14;
            int i14;
            Bitmap thumbnail;
            String str3;
            FileInputStream fileInputStream2;
            Bitmap decodeStream;
            int i15;
            boolean z15;
            float f6;
            int i16;
            float f7;
            boolean z16;
            int i17;
            Long l3;
            boolean z17;
            String str4;
            boolean z18;
            boolean z19;
            boolean z20;
            boolean z21;
            boolean z22;
            Bitmap bitmap6;
            MediaMetadataRetriever mediaMetadataRetriever;
            BitmapsCache.CacheOptions cacheOptions;
            AnimatedFileDrawable animatedFileDrawable;
            AnimatedFileDrawable animatedFileDrawable2;
            Drawable drawable;
            RLottieDrawable rLottieDrawable;
            int i18;
            int i19;
            String str5;
            int i20;
            int i21;
            int i22;
            int i23;
            boolean z23;
            boolean z24;
            boolean z25;
            String str6;
            boolean z26;
            boolean z27;
            int i24;
            RandomAccessFile randomAccessFile;
            boolean z28;
            RandomAccessFile randomAccessFile2;
            Throwable th;
            boolean z29;
            BitmapsCache.CacheOptions cacheOptions2;
            BitmapsCache.CacheOptions cacheOptions3;
            RLottieDrawable rLottieDrawable2;
            byte[] bArr2;
            boolean z30;
            boolean z31;
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
                        if (strippedPhotoBitmap != null) {
                            themePreviewDrawable = new BitmapDrawable(strippedPhotoBitmap);
                        }
                        themePreviewDrawable = null;
                    } else {
                        int i25 = cacheImage2.imageType;
                        if (i25 == 5) {
                            try {
                                CacheImage cacheImage3 = this.cacheImage;
                                themePreviewDrawable = new ThemePreviewDrawable(cacheImage3.finalFilePath, (DocumentObject.ThemeDocument) cacheImage3.imageLocation.document);
                            } catch (Throwable th2) {
                                FileLog.e(th2);
                            }
                        } else {
                            boolean z32 = true;
                            if (i25 != 3 && i25 != 4) {
                                if (i25 == 1) {
                                    int min = Math.min((int) LiteMode.FLAG_CALLS_ANIMATIONS, AndroidUtilities.dp(170.6f));
                                    int min2 = Math.min((int) LiteMode.FLAG_CALLS_ANIMATIONS, AndroidUtilities.dp(170.6f));
                                    String str7 = this.cacheImage.filter;
                                    if (str7 != null) {
                                        String[] split = str7.split("_");
                                        if (split.length >= 2) {
                                            float parseFloat = Float.parseFloat(split[0]);
                                            float parseFloat2 = Float.parseFloat(split[1]);
                                            int min3 = Math.min((int) LiteMode.FLAG_CALLS_ANIMATIONS, (int) (AndroidUtilities.density * parseFloat));
                                            int min4 = Math.min((int) LiteMode.FLAG_CALLS_ANIMATIONS, (int) (AndroidUtilities.density * parseFloat2));
                                            if (parseFloat > 90.0f || parseFloat2 > 90.0f || this.cacheImage.filter.contains("nolimit")) {
                                                min2 = min4;
                                                min = min3;
                                                z23 = false;
                                            } else {
                                                min = Math.min(min3, (int) NotificationCenter.audioRouteChanged);
                                                min2 = Math.min(min4, (int) NotificationCenter.audioRouteChanged);
                                                z23 = true;
                                            }
                                            z27 = (split.length >= 3 && "pcache".equals(split[2])) || this.cacheImage.filter.contains("pcache") || !(this.cacheImage.filter.contains("nolimit") || SharedConfig.getDevicePerformanceClass() == 2);
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
                                        if (split.length >= 3) {
                                            if ("nr".equals(split[2])) {
                                                str6 = null;
                                            } else if ("nrs".equals(split[2])) {
                                                str6 = null;
                                                i24 = 3;
                                                if (split.length >= 5) {
                                                    if ("c1".equals(split[4])) {
                                                        i21 = min2;
                                                        z25 = z30;
                                                        z24 = z31;
                                                        i22 = min;
                                                        i23 = 12;
                                                    } else if ("c2".equals(split[4])) {
                                                        i21 = min2;
                                                        z25 = z30;
                                                        z24 = z31;
                                                        i22 = min;
                                                        i23 = 3;
                                                    } else if ("c3".equals(split[4])) {
                                                        i21 = min2;
                                                        z25 = z30;
                                                        z24 = z31;
                                                        i22 = min;
                                                        i23 = 4;
                                                    } else if ("c4".equals(split[4])) {
                                                        i21 = min2;
                                                        z25 = z30;
                                                        z24 = z31;
                                                        i22 = min;
                                                        i23 = 5;
                                                    } else if ("c5".equals(split[4])) {
                                                        i21 = min2;
                                                        z25 = z30;
                                                        z24 = z31;
                                                        i22 = min;
                                                        i23 = 6;
                                                    }
                                                }
                                                i21 = min2;
                                                z25 = z30;
                                                z24 = z31;
                                                i22 = min;
                                                i23 = 0;
                                            } else if ("dice".equals(split[2])) {
                                                str6 = split[3];
                                            }
                                            i24 = 2;
                                            if (split.length >= 5) {
                                            }
                                            i21 = min2;
                                            z25 = z30;
                                            z24 = z31;
                                            i22 = min;
                                            i23 = 0;
                                        }
                                        str6 = null;
                                        i24 = 1;
                                        if (split.length >= 5) {
                                        }
                                        i21 = min2;
                                        z25 = z30;
                                        z24 = z31;
                                        i22 = min;
                                        i23 = 0;
                                    } else {
                                        i21 = min2;
                                        i22 = min;
                                        i23 = 0;
                                        z23 = false;
                                        z24 = false;
                                        z25 = false;
                                        str6 = null;
                                        z26 = false;
                                        z27 = false;
                                        i24 = 1;
                                    }
                                    if (str6 != null) {
                                        rLottieDrawable2 = "🎰".equals(str6) ? new SlotsDrawable(str6, i22, i21) : new RLottieDrawable(str6, i22, i21);
                                    } else {
                                        File file = this.cacheImage.finalFilePath;
                                        try {
                                            randomAccessFile2 = new RandomAccessFile(this.cacheImage.finalFilePath, "r");
                                            try {
                                                bArr2 = this.cacheImage.type == 1 ? ImageLoader.headerThumb : ImageLoader.header;
                                                randomAccessFile2.readFully(bArr2, 0, 2);
                                            } catch (Exception e) {
                                                e = e;
                                                z28 = false;
                                                try {
                                                    FileLog.e(e, z28);
                                                    if (randomAccessFile2 != null) {
                                                        try {
                                                            randomAccessFile2.close();
                                                        } catch (Exception e2) {
                                                            e = e2;
                                                            z29 = false;
                                                            FileLog.e(e);
                                                            if ((!z24 || z26) ? false : z27) {
                                                            }
                                                            cacheOptions2 = new BitmapsCache.CacheOptions();
                                                            if (z24) {
                                                            }
                                                            cacheOptions2.firstFrame = true;
                                                            cacheOptions3 = cacheOptions2;
                                                            if (z29) {
                                                            }
                                                            rLottieDrawable = rLottieDrawable2;
                                                            if (z24) {
                                                            }
                                                            loadLastFrame(rLottieDrawable, i21, i22, z24, z25);
                                                            return;
                                                        }
                                                    }
                                                    z29 = false;
                                                    if ((!z24 || z26) ? false : z27) {
                                                    }
                                                    cacheOptions2 = new BitmapsCache.CacheOptions();
                                                    if (z24) {
                                                    }
                                                    cacheOptions2.firstFrame = true;
                                                    cacheOptions3 = cacheOptions2;
                                                    if (z29) {
                                                    }
                                                    rLottieDrawable = rLottieDrawable2;
                                                    if (z24) {
                                                    }
                                                    loadLastFrame(rLottieDrawable, i21, i22, z24, z25);
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
                                            z28 = false;
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
                                                z29 = true;
                                                randomAccessFile2.close();
                                                if (!((!z24 || z26) ? false : z27) || z24 || z26) {
                                                    cacheOptions2 = new BitmapsCache.CacheOptions();
                                                    if (!z24 || z26) {
                                                        cacheOptions2.firstFrame = true;
                                                    } else {
                                                        String str8 = this.cacheImage.filter;
                                                        if (str8 != null && str8.contains("compress")) {
                                                            cacheOptions2.compressQuality = 60;
                                                        }
                                                        String str9 = this.cacheImage.filter;
                                                        if (str9 != null && str9.contains("flbk")) {
                                                            cacheOptions2.fallback = true;
                                                        }
                                                    }
                                                    cacheOptions3 = cacheOptions2;
                                                } else {
                                                    cacheOptions3 = null;
                                                }
                                                if (z29) {
                                                    File file2 = this.cacheImage.finalFilePath;
                                                    rLottieDrawable2 = new RLottieDrawable(file2, ImageLoader.decompressGzip(file2), i22, i21, cacheOptions3, z23, null, i23);
                                                } else {
                                                    rLottieDrawable2 = new RLottieDrawable(this.cacheImage.finalFilePath, i22, i21, cacheOptions3, z23, null, i23);
                                                }
                                            }
                                        }
                                        z29 = false;
                                        randomAccessFile2.close();
                                        if ((!z24 || z26) ? false : z27) {
                                        }
                                        cacheOptions2 = new BitmapsCache.CacheOptions();
                                        if (z24) {
                                        }
                                        cacheOptions2.firstFrame = true;
                                        cacheOptions3 = cacheOptions2;
                                        if (z29) {
                                        }
                                    }
                                    rLottieDrawable = rLottieDrawable2;
                                    if (!z24 || z26) {
                                        loadLastFrame(rLottieDrawable, i21, i22, z24, z25);
                                        return;
                                    }
                                    rLottieDrawable.setAutoRepeat(i24);
                                } else if (i25 == 2) {
                                    long j = imageLocation.videoSeekTo;
                                    String str10 = cacheImage2.filter;
                                    if (str10 != null) {
                                        String[] split2 = str10.split("_");
                                        if (split2.length >= 2) {
                                            float parseFloat3 = Float.parseFloat(split2[0]);
                                            float parseFloat4 = Float.parseFloat(split2[1]);
                                            if (parseFloat3 <= 90.0f && parseFloat4 <= 90.0f && !this.cacheImage.filter.contains("nolimit")) {
                                                z18 = true;
                                                z19 = false;
                                                z20 = false;
                                                z21 = false;
                                                z22 = false;
                                                for (i20 = 0; i20 < split2.length; i20++) {
                                                    if ("pcache".equals(split2[i20])) {
                                                        z20 = true;
                                                    }
                                                    if ("firstframe".equals(split2[i20])) {
                                                        z19 = true;
                                                    }
                                                    if ("nostream".equals(split2[i20])) {
                                                        z22 = true;
                                                    }
                                                    if ("pframe".equals(split2[i20])) {
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
                                        while (i20 < split2.length) {
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
                                    if (z21) {
                                        try {
                                            mediaMetadataRetriever = new MediaMetadataRetriever();
                                            mediaMetadataRetriever.setDataSource(this.cacheImage.finalFilePath.getAbsolutePath());
                                            bitmap6 = mediaMetadataRetriever.getFrameAtTime(2L);
                                        } catch (Exception e5) {
                                            e = e5;
                                            bitmap6 = null;
                                        }
                                        try {
                                            mediaMetadataRetriever.release();
                                        } catch (Exception e6) {
                                            e = e6;
                                            e.printStackTrace();
                                            Thread.interrupted();
                                            if (bitmap6 != null) {
                                            }
                                        }
                                        Thread.interrupted();
                                        if (bitmap6 != null) {
                                            onPostExecute(null);
                                            return;
                                        } else {
                                            onPostExecute(new BitmapDrawable(bitmap6));
                                            return;
                                        }
                                    }
                                    if (!z20 || z19) {
                                        cacheOptions = null;
                                    } else {
                                        BitmapsCache.CacheOptions cacheOptions4 = new BitmapsCache.CacheOptions();
                                        String str11 = this.cacheImage.filter;
                                        if (str11 != null && str11.contains("compress")) {
                                            cacheOptions4.compressQuality = 60;
                                        }
                                        cacheOptions = cacheOptions4;
                                    }
                                    if (ImageLoader.this.isAnimatedAvatar(this.cacheImage.filter) || ImageLoader.AUTOPLAY_FILTER.equals(this.cacheImage.filter)) {
                                        CacheImage cacheImage4 = this.cacheImage;
                                        ImageLocation imageLocation2 = cacheImage4.imageLocation;
                                        TLRPC$Document tLRPC$Document = imageLocation2.document;
                                        if (!(tLRPC$Document instanceof TLRPC$TL_documentEncrypted) && !z20) {
                                            if (!(tLRPC$Document instanceof TLRPC$Document)) {
                                                tLRPC$Document = null;
                                            }
                                            long j2 = tLRPC$Document != null ? cacheImage4.size : imageLocation2.currentSize;
                                            int i26 = tLRPC$Document != null ? 1 : 0;
                                            int i27 = cacheImage4.cacheType;
                                            int i28 = i27 > 1 ? i27 : i26;
                                            CacheImage cacheImage5 = this.cacheImage;
                                            AnimatedFileDrawable animatedFileDrawable3 = new AnimatedFileDrawable(cacheImage5.finalFilePath, z19, z22 ? 0L : j2, cacheImage5.priority, z22 ? null : tLRPC$Document, (tLRPC$Document != null || z22) ? null : cacheImage5.imageLocation, cacheImage5.parentObject, j, cacheImage5.currentAccount, false, 0, 0, cacheOptions, i28);
                                            animatedFileDrawable2 = animatedFileDrawable3;
                                            if (!MessageObject.isWebM(tLRPC$Document)) {
                                                animatedFileDrawable2 = animatedFileDrawable3;
                                                if (!MessageObject.isVideoSticker(tLRPC$Document)) {
                                                    animatedFileDrawable2 = animatedFileDrawable3;
                                                    animatedFileDrawable = animatedFileDrawable3;
                                                }
                                            }
                                            animatedFileDrawable2.setIsWebmSticker(z32);
                                            if (z19) {
                                                Bitmap frameAtTime = animatedFileDrawable2.getFrameAtTime(0L, false);
                                                animatedFileDrawable2.recycle();
                                                Thread.interrupted();
                                                if (frameAtTime == null) {
                                                    rLottieDrawable = null;
                                                } else {
                                                    drawable = new BitmapDrawable(frameAtTime);
                                                }
                                            } else {
                                                animatedFileDrawable2.setLimitFps(z18);
                                                Thread.interrupted();
                                                drawable = animatedFileDrawable2;
                                            }
                                            onPostExecute(drawable);
                                            return;
                                        }
                                    }
                                    String str12 = this.cacheImage.filter;
                                    if (str12 != null) {
                                        String[] split3 = str12.split("_");
                                        if (split3.length >= 2) {
                                            float parseFloat5 = Float.parseFloat(split3[0]);
                                            float parseFloat6 = Float.parseFloat(split3[1]);
                                            float f8 = AndroidUtilities.density;
                                            i19 = (int) (parseFloat6 * f8);
                                            i18 = (int) (parseFloat5 * f8);
                                            boolean z33 = !z19 || ((str5 = this.cacheImage.filter) != null && ("d".equals(str5) || this.cacheImage.filter.contains("_d")));
                                            int i29 = (!z22 ? null : this.cacheImage.imageLocation.document) == null ? 1 : 0;
                                            int i30 = this.cacheImage.cacheType;
                                            int i31 = i30 <= 1 ? i30 : i29;
                                            CacheImage cacheImage6 = this.cacheImage;
                                            AnimatedFileDrawable animatedFileDrawable4 = new AnimatedFileDrawable(cacheImage6.finalFilePath, z33, 0L, cacheImage6.priority, !z22 ? null : cacheImage6.imageLocation.document, null, null, j, cacheImage6.currentAccount, false, i18, i19, cacheOptions, i31);
                                            animatedFileDrawable2 = animatedFileDrawable4;
                                            if (!MessageObject.isWebM(this.cacheImage.imageLocation.document)) {
                                                animatedFileDrawable2 = animatedFileDrawable4;
                                                if (!MessageObject.isVideoSticker(this.cacheImage.imageLocation.document)) {
                                                    animatedFileDrawable2 = animatedFileDrawable4;
                                                    animatedFileDrawable = animatedFileDrawable4;
                                                }
                                            }
                                            animatedFileDrawable2.setIsWebmSticker(z32);
                                            if (z19) {
                                            }
                                            onPostExecute(drawable);
                                            return;
                                        }
                                    }
                                    i18 = 0;
                                    i19 = 0;
                                    if (z19) {
                                    }
                                    if ((!z22 ? null : this.cacheImage.imageLocation.document) == null) {
                                    }
                                    int i302 = this.cacheImage.cacheType;
                                    if (i302 <= 1) {
                                    }
                                    CacheImage cacheImage62 = this.cacheImage;
                                    AnimatedFileDrawable animatedFileDrawable42 = new AnimatedFileDrawable(cacheImage62.finalFilePath, z33, 0L, cacheImage62.priority, !z22 ? null : cacheImage62.imageLocation.document, null, null, j, cacheImage62.currentAccount, false, i18, i19, cacheOptions, i31);
                                    animatedFileDrawable2 = animatedFileDrawable42;
                                    if (!MessageObject.isWebM(this.cacheImage.imageLocation.document)) {
                                    }
                                    animatedFileDrawable2.setIsWebmSticker(z32);
                                    if (z19) {
                                    }
                                    onPostExecute(drawable);
                                    return;
                                } else {
                                    File file3 = cacheImage2.finalFilePath;
                                    boolean z34 = (cacheImage2.secureDocument == null && (cacheImage2.encryptionKeyPath == null || file3 == null || !file3.getAbsolutePath().endsWith(".enc"))) ? false : true;
                                    CacheImage cacheImage7 = this.cacheImage;
                                    SecureDocument secureDocument = cacheImage7.secureDocument;
                                    if (secureDocument != null) {
                                        SecureDocumentKey secureDocumentKey5 = secureDocument.secureDocumentKey;
                                        TLRPC$TL_secureFile tLRPC$TL_secureFile = secureDocument.secureFile;
                                        if (tLRPC$TL_secureFile == null || (bArr = tLRPC$TL_secureFile.file_hash) == null) {
                                            bArr = secureDocument.fileHash;
                                        }
                                        secureDocumentKey = secureDocumentKey5;
                                    } else {
                                        secureDocumentKey = null;
                                        bArr = null;
                                    }
                                    String str13 = cacheImage7.imageLocation.path;
                                    if (str13 != null) {
                                        if (str13.startsWith("thumb://")) {
                                            int indexOf = str13.indexOf(":", 8);
                                            if (indexOf >= 0) {
                                                l = Long.valueOf(Long.parseLong(str13.substring(8, indexOf)));
                                                str4 = str13.substring(indexOf + 1);
                                            } else {
                                                str4 = null;
                                                l = null;
                                            }
                                            str = str4;
                                            z = false;
                                        } else {
                                            if (str13.startsWith("vthumb://")) {
                                                int indexOf2 = str13.indexOf(":", 9);
                                                if (indexOf2 >= 0) {
                                                    l3 = Long.valueOf(Long.parseLong(str13.substring(9, indexOf2)));
                                                    z17 = true;
                                                    l = l3;
                                                    z = z17;
                                                    str = null;
                                                }
                                            }
                                            l3 = null;
                                            z17 = false;
                                            l = l3;
                                            z = z17;
                                            str = null;
                                        }
                                        z2 = false;
                                        BitmapFactory.Options options = new BitmapFactory.Options();
                                        options.inSampleSize = 1;
                                        if (Build.VERSION.SDK_INT < 21) {
                                            options.inPurgeable = true;
                                        }
                                        boolean z35 = ImageLoader.this.canForce8888;
                                        str3 = this.cacheImage.filter;
                                        if (str3 == null) {
                                            String[] split4 = str3.split("_");
                                            if (split4.length >= 2) {
                                                f = Float.parseFloat(split4[0]) * AndroidUtilities.density;
                                                try {
                                                    f7 = f;
                                                    f4 = Float.parseFloat(split4[1]) * AndroidUtilities.density;
                                                } catch (Throwable th6) {
                                                    th = th6;
                                                    l2 = l;
                                                    z3 = z;
                                                    f2 = 0.0f;
                                                    bitmap2 = null;
                                                    c = 0;
                                                    z4 = false;
                                                    i = 1;
                                                    FileLog.e(th, !(th instanceof FileNotFoundException));
                                                    f3 = f;
                                                    f4 = f2;
                                                    c2 = c;
                                                    z5 = z4;
                                                    if (this.cacheImage.type == i) {
                                                    }
                                                    Thread.interrupted();
                                                    if (BuildVars.LOGS_ENABLED) {
                                                    }
                                                    if (bitmap2 != null) {
                                                    }
                                                    cacheImage = this.cacheImage;
                                                    if (cacheImage == null) {
                                                    }
                                                    if (bitmap2 != null) {
                                                    }
                                                    themePreviewDrawable = null;
                                                    onPostExecute(themePreviewDrawable);
                                                }
                                            } else {
                                                f4 = 0.0f;
                                                f7 = 0.0f;
                                            }
                                            try {
                                                char c3 = this.cacheImage.filter.contains("b2r") ? (char) 4 : this.cacheImage.filter.contains("b2") ? (char) 3 : this.cacheImage.filter.contains("b1") ? (char) 2 : this.cacheImage.filter.contains("b") ? (char) 1 : (char) 0;
                                                try {
                                                    boolean contains = this.cacheImage.filter.contains("i");
                                                    try {
                                                        if (this.cacheImage.filter.contains("f")) {
                                                            z35 = true;
                                                        } else if (this.cacheImage.filter.contains("F")) {
                                                            z35 = false;
                                                        }
                                                        if (f7 == 0.0f || f4 == 0.0f) {
                                                            c = c3;
                                                            z4 = contains;
                                                            l2 = l;
                                                            z3 = z;
                                                        } else {
                                                            options.inJustDecodeBounds = true;
                                                            try {
                                                                try {
                                                                    if (l == null || str != null) {
                                                                        c = c3;
                                                                        z4 = contains;
                                                                        if (secureDocumentKey != null) {
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
                                                                                EncryptedFileInputStream.decryptBytesWithKeyFile(bArr3, 0, length, secureDocumentKey);
                                                                                l2 = l;
                                                                                z3 = z;
                                                                                byte[] computeSHA256 = Utilities.computeSHA256(bArr3, 0, length);
                                                                                if (bArr != null && Arrays.equals(computeSHA256, bArr)) {
                                                                                    z16 = false;
                                                                                    int i32 = bArr3[0] & 255;
                                                                                    int i33 = length - i32;
                                                                                    if (!z16) {
                                                                                        BitmapFactory.decodeByteArray(bArr3, i32, i33, options);
                                                                                    }
                                                                                }
                                                                                z16 = true;
                                                                                int i322 = bArr3[0] & 255;
                                                                                int i332 = length - i322;
                                                                                if (!z16) {
                                                                                }
                                                                            } catch (Throwable th7) {
                                                                                th = th7;
                                                                                l2 = l;
                                                                                z3 = z;
                                                                                f2 = f4;
                                                                                f = f7;
                                                                                bitmap2 = null;
                                                                                i = 1;
                                                                                FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                                f3 = f;
                                                                                f4 = f2;
                                                                                c2 = c;
                                                                                z5 = z4;
                                                                                if (this.cacheImage.type == i) {
                                                                                }
                                                                                Thread.interrupted();
                                                                                if (BuildVars.LOGS_ENABLED) {
                                                                                }
                                                                                if (bitmap2 != null) {
                                                                                }
                                                                                cacheImage = this.cacheImage;
                                                                                if (cacheImage == null) {
                                                                                }
                                                                                if (bitmap2 != null) {
                                                                                }
                                                                                themePreviewDrawable = null;
                                                                                onPostExecute(themePreviewDrawable);
                                                                            }
                                                                        } else {
                                                                            l2 = l;
                                                                            z3 = z;
                                                                            FileInputStream encryptedFileInputStream = z34 ? new EncryptedFileInputStream(file3, this.cacheImage.encryptionKeyPath) : new FileInputStream(file3);
                                                                            BitmapFactory.decodeStream(encryptedFileInputStream, null, options);
                                                                            encryptedFileInputStream.close();
                                                                        }
                                                                    } else {
                                                                        if (z) {
                                                                            try {
                                                                                c = c3;
                                                                                z4 = contains;
                                                                                MediaStore.Video.Thumbnails.getThumbnail(ApplicationLoader.applicationContext.getContentResolver(), l.longValue(), 1, options);
                                                                            } catch (Throwable th8) {
                                                                                th = th8;
                                                                                c = c3;
                                                                                z4 = contains;
                                                                                f2 = f4;
                                                                                l2 = l;
                                                                                z3 = z;
                                                                                f = f7;
                                                                                bitmap2 = null;
                                                                                i = 1;
                                                                                FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                                f3 = f;
                                                                                f4 = f2;
                                                                                c2 = c;
                                                                                z5 = z4;
                                                                                if (this.cacheImage.type == i) {
                                                                                }
                                                                                Thread.interrupted();
                                                                                if (BuildVars.LOGS_ENABLED) {
                                                                                }
                                                                                if (bitmap2 != null) {
                                                                                }
                                                                                cacheImage = this.cacheImage;
                                                                                if (cacheImage == null) {
                                                                                }
                                                                                if (bitmap2 != null) {
                                                                                }
                                                                                themePreviewDrawable = null;
                                                                                onPostExecute(themePreviewDrawable);
                                                                            }
                                                                        } else {
                                                                            c = c3;
                                                                            z4 = contains;
                                                                            MediaStore.Images.Thumbnails.getThumbnail(ApplicationLoader.applicationContext.getContentResolver(), l.longValue(), 1, options);
                                                                        }
                                                                        l2 = l;
                                                                        z3 = z;
                                                                    }
                                                                    float f9 = options.outWidth;
                                                                    float f10 = options.outHeight;
                                                                    float min5 = (f7 < f4 || f9 <= f10) ? Math.min(f9 / f7, f10 / f4) : Math.max(f9 / f7, f10 / f4);
                                                                    if (min5 < 1.2f) {
                                                                        min5 = 1.0f;
                                                                    }
                                                                    options.inJustDecodeBounds = false;
                                                                    if (min5 <= 1.0f || (f9 <= f7 && f10 <= f4)) {
                                                                        i15 = (int) min5;
                                                                        z15 = z35;
                                                                        bitmap2 = null;
                                                                        f6 = f4;
                                                                        f = f7;
                                                                    } else {
                                                                        int i34 = 1;
                                                                        while (true) {
                                                                            i17 = i34 * 2;
                                                                            if (i34 * 4 >= min5) {
                                                                                break;
                                                                            }
                                                                            i34 = i17;
                                                                        }
                                                                        options.inSampleSize = i17;
                                                                    }
                                                                } catch (Throwable th9) {
                                                                    th = th9;
                                                                    f2 = f4;
                                                                    f = f7;
                                                                    bitmap2 = null;
                                                                    i = 1;
                                                                    FileLog.e(th, !(th instanceof FileNotFoundException));
                                                                    f3 = f;
                                                                    f4 = f2;
                                                                    c2 = c;
                                                                    z5 = z4;
                                                                    if (this.cacheImage.type == i) {
                                                                    }
                                                                    Thread.interrupted();
                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                    }
                                                                    if (bitmap2 != null) {
                                                                    }
                                                                    cacheImage = this.cacheImage;
                                                                    if (cacheImage == null) {
                                                                    }
                                                                    if (bitmap2 != null) {
                                                                    }
                                                                    themePreviewDrawable = null;
                                                                    onPostExecute(themePreviewDrawable);
                                                                }
                                                            } catch (Throwable th10) {
                                                                th = th10;
                                                            }
                                                        }
                                                        f3 = f7;
                                                        c2 = c;
                                                        z5 = z4;
                                                        bitmap2 = null;
                                                        i = 1;
                                                    } catch (Throwable th11) {
                                                        th = th11;
                                                        c = c3;
                                                        z4 = contains;
                                                    }
                                                } catch (Throwable th12) {
                                                    th = th12;
                                                    c = c3;
                                                    l2 = l;
                                                    z3 = z;
                                                    f2 = f4;
                                                    f = f7;
                                                    bitmap2 = null;
                                                    z4 = false;
                                                    i = 1;
                                                    FileLog.e(th, !(th instanceof FileNotFoundException));
                                                    f3 = f;
                                                    f4 = f2;
                                                    c2 = c;
                                                    z5 = z4;
                                                    if (this.cacheImage.type == i) {
                                                    }
                                                    Thread.interrupted();
                                                    if (BuildVars.LOGS_ENABLED) {
                                                    }
                                                    if (bitmap2 != null) {
                                                    }
                                                    cacheImage = this.cacheImage;
                                                    if (cacheImage == null) {
                                                    }
                                                    if (bitmap2 != null) {
                                                    }
                                                    themePreviewDrawable = null;
                                                    onPostExecute(themePreviewDrawable);
                                                }
                                            } catch (Throwable th13) {
                                                th = th13;
                                                l2 = l;
                                                z3 = z;
                                                f2 = f4;
                                                f = f7;
                                                bitmap2 = null;
                                                c = 0;
                                                z4 = false;
                                                i = 1;
                                                FileLog.e(th, !(th instanceof FileNotFoundException));
                                                f3 = f;
                                                f4 = f2;
                                                c2 = c;
                                                z5 = z4;
                                                if (this.cacheImage.type == i) {
                                                }
                                                Thread.interrupted();
                                                if (BuildVars.LOGS_ENABLED) {
                                                }
                                                if (bitmap2 != null) {
                                                }
                                                cacheImage = this.cacheImage;
                                                if (cacheImage == null) {
                                                }
                                                if (bitmap2 != null) {
                                                }
                                                themePreviewDrawable = null;
                                                onPostExecute(themePreviewDrawable);
                                            }
                                            if (this.cacheImage.type == i) {
                                                try {
                                                    ImageLoader.this.lastCacheOutTime = SystemClock.elapsedRealtime();
                                                } catch (Throwable th14) {
                                                    th = th14;
                                                }
                                                synchronized (this.sync) {
                                                    try {
                                                    } catch (Throwable th15) {
                                                        th = th15;
                                                        bitmap2 = bitmap3;
                                                    }
                                                    if (this.isCancelled) {
                                                        return;
                                                    }
                                                    try {
                                                        if (options.inPurgeable || secureDocumentKey != null) {
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
                                                            if (secureDocumentKey != null) {
                                                                EncryptedFileInputStream.decryptBytesWithKeyFile(bArr4, 0, length2, secureDocumentKey);
                                                                bitmap4 = bitmap2;
                                                                z9 = z5;
                                                                byte[] computeSHA2562 = Utilities.computeSHA256(bArr4, 0, length2);
                                                                if (bArr != null && Arrays.equals(computeSHA2562, bArr)) {
                                                                    z10 = false;
                                                                    i3 = bArr4[0] & 255;
                                                                    length2 -= i3;
                                                                }
                                                                z10 = true;
                                                                i3 = bArr4[0] & 255;
                                                                length2 -= i3;
                                                            } else {
                                                                bitmap4 = bitmap2;
                                                                z9 = z5;
                                                                if (z34) {
                                                                    EncryptedFileInputStream.decryptBytesWithKeyFile(bArr4, 0, length2, this.cacheImage.encryptionKeyPath);
                                                                }
                                                                z10 = false;
                                                                i3 = 0;
                                                            }
                                                            bitmap2 = !z10 ? BitmapFactory.decodeByteArray(bArr4, i3, length2, options) : bitmap4;
                                                        } else {
                                                            FileInputStream encryptedFileInputStream2 = z34 ? new EncryptedFileInputStream(file3, this.cacheImage.encryptionKeyPath) : new FileInputStream(file3);
                                                            bitmap2 = BitmapFactory.decodeStream(encryptedFileInputStream2, null, options);
                                                            encryptedFileInputStream2.close();
                                                            z9 = z5;
                                                        }
                                                    } catch (Throwable th16) {
                                                        th = th16;
                                                        z6 = false;
                                                        z7 = th instanceof FileNotFoundException;
                                                        secureDocumentKey2 = null;
                                                        z8 = true;
                                                        i2 = 0;
                                                        FileLog.e(th, z7 ^ z8);
                                                        i11 = secureDocumentKey2;
                                                        i10 = i2;
                                                        i12 = i11;
                                                        Thread.interrupted();
                                                        if (BuildVars.LOGS_ENABLED) {
                                                        }
                                                        if (bitmap2 != null) {
                                                        }
                                                        cacheImage = this.cacheImage;
                                                        if (cacheImage == null) {
                                                        }
                                                        if (bitmap2 != null) {
                                                        }
                                                        themePreviewDrawable = null;
                                                        onPostExecute(themePreviewDrawable);
                                                    }
                                                    if (bitmap2 == null) {
                                                        if (file3.length() != 0 && this.cacheImage.filter != null) {
                                                            i9 = 0;
                                                            z6 = false;
                                                            i10 = 0;
                                                            i12 = i9;
                                                        }
                                                        file3.delete();
                                                        i4 = 0;
                                                        z6 = false;
                                                        i2 = 0;
                                                        i11 = i4;
                                                        i10 = i2;
                                                        i12 = i11;
                                                    } else {
                                                        if (this.cacheImage.filter != null) {
                                                            float width = bitmap2.getWidth();
                                                            float height = bitmap2.getHeight();
                                                            if (!options.inPurgeable && f3 != 0.0f && width != f3 && width > 20.0f + f3 && bitmap2 != (createScaledBitmap = Bitmaps.createScaledBitmap(bitmap2, (int) f3, (int) (height / (width / f3)), true))) {
                                                                bitmap2.recycle();
                                                                bitmap2 = createScaledBitmap;
                                                            }
                                                        }
                                                        z6 = z9 && Utilities.needInvert(bitmap2, !options.inPurgeable ? 1 : 0, bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getRowBytes()) != 0;
                                                        try {
                                                        } catch (Throwable th17) {
                                                            th = th17;
                                                            z7 = th instanceof FileNotFoundException;
                                                            secureDocumentKey2 = null;
                                                            z8 = true;
                                                            i2 = 0;
                                                            FileLog.e(th, z7 ^ z8);
                                                            i11 = secureDocumentKey2;
                                                            i10 = i2;
                                                            i12 = i11;
                                                            Thread.interrupted();
                                                            if (BuildVars.LOGS_ENABLED) {
                                                            }
                                                            if (bitmap2 != null) {
                                                            }
                                                            cacheImage = this.cacheImage;
                                                            if (cacheImage == null) {
                                                            }
                                                            if (bitmap2 != null) {
                                                            }
                                                            themePreviewDrawable = null;
                                                            onPostExecute(themePreviewDrawable);
                                                        }
                                                        if (c2 == 1) {
                                                            if (bitmap2.getConfig() == Bitmap.Config.ARGB_8888) {
                                                                i5 = !options.inPurgeable ? 1 : 0;
                                                                i6 = bitmap2.getWidth();
                                                                i7 = bitmap2.getHeight();
                                                                rowBytes = bitmap2.getRowBytes();
                                                                i8 = 3;
                                                                Utilities.blurBitmap(bitmap2, i8, i5, i6, i7, rowBytes);
                                                            }
                                                            i9 = 0;
                                                        } else {
                                                            if (c2 != 2) {
                                                                if (c2 != 3 && c2 != 4) {
                                                                    if (c2 == 0 && options.inPurgeable) {
                                                                        Utilities.pinBitmap(bitmap2);
                                                                        i4 = 0;
                                                                        i2 = 0;
                                                                        i11 = i4;
                                                                        i10 = i2;
                                                                        i12 = i11;
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
                                                                    Utilities.blurBitmap(bitmap2, 7, !options.inPurgeable ? 1 : 0, bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getRowBytes());
                                                                    Utilities.blurBitmap(bitmap2, 7, !options.inPurgeable ? 1 : 0, bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getRowBytes());
                                                                    Utilities.blurBitmap(bitmap2, 7, !options.inPurgeable ? 1 : 0, bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getRowBytes());
                                                                }
                                                            } else if (bitmap2.getConfig() == Bitmap.Config.ARGB_8888) {
                                                                int width2 = bitmap2.getWidth();
                                                                int height2 = bitmap2.getHeight();
                                                                i5 = !options.inPurgeable;
                                                                i6 = width2;
                                                                i7 = height2;
                                                                rowBytes = bitmap2.getRowBytes();
                                                                i8 = 1;
                                                                Utilities.blurBitmap(bitmap2, i8, i5, i6, i7, rowBytes);
                                                            }
                                                            i9 = 0;
                                                        }
                                                        i10 = 0;
                                                        i12 = i9;
                                                    }
                                                }
                                            } else {
                                                Bitmap bitmap7 = bitmap2;
                                                boolean z36 = z5;
                                                int i35 = l2 != null ? 0 : 20;
                                                if (i35 != 0) {
                                                    try {
                                                    } catch (Throwable th18) {
                                                        th = th18;
                                                        bitmap2 = bitmap7;
                                                        secureDocumentKey3 = null;
                                                        z12 = false;
                                                        i2 = 0;
                                                        z7 = th instanceof FileNotFoundException;
                                                        secureDocumentKey2 = secureDocumentKey3;
                                                        z6 = z12;
                                                        z8 = true;
                                                        FileLog.e(th, z7 ^ z8);
                                                        i11 = secureDocumentKey2;
                                                        i10 = i2;
                                                        i12 = i11;
                                                        Thread.interrupted();
                                                        if (BuildVars.LOGS_ENABLED) {
                                                        }
                                                        if (bitmap2 != null) {
                                                        }
                                                        cacheImage = this.cacheImage;
                                                        if (cacheImage == null) {
                                                        }
                                                        if (bitmap2 != null) {
                                                        }
                                                        themePreviewDrawable = null;
                                                        onPostExecute(themePreviewDrawable);
                                                    }
                                                    if (ImageLoader.this.lastCacheOutTime != 0) {
                                                        z11 = z36;
                                                        f5 = f3;
                                                        long j3 = i35;
                                                        if (ImageLoader.this.lastCacheOutTime > SystemClock.elapsedRealtime() - j3 && Build.VERSION.SDK_INT < 21) {
                                                            Thread.sleep(j3);
                                                        }
                                                        ImageLoader.this.lastCacheOutTime = SystemClock.elapsedRealtime();
                                                        synchronized (this.sync) {
                                                            if (this.isCancelled) {
                                                                return;
                                                            }
                                                            if (!z35) {
                                                                CacheImage cacheImage8 = this.cacheImage;
                                                                if (cacheImage8.filter != null && c2 == 0 && cacheImage8.imageLocation.path == null) {
                                                                    config = Bitmap.Config.RGB_565;
                                                                    options.inPreferredConfig = config;
                                                                    options.inDither = false;
                                                                    if (l2 == null && str == null) {
                                                                        if (!z3) {
                                                                            thumbnail = MediaStore.Images.Thumbnails.getThumbnail(ApplicationLoader.applicationContext.getContentResolver(), l2.longValue(), 1, options);
                                                                        } else if (l2.longValue() == 0) {
                                                                            AnimatedFileDrawable animatedFileDrawable5 = new AnimatedFileDrawable(file3, true, 0L, 0, null, null, null, 0L, 0, true, null);
                                                                            Bitmap frameAtTime2 = animatedFileDrawable5.getFrameAtTime(0L, true);
                                                                            try {
                                                                                animatedFileDrawable5.recycle();
                                                                                bitmap2 = frameAtTime2;
                                                                            } catch (Throwable th19) {
                                                                                th = th19;
                                                                                bitmap2 = frameAtTime2;
                                                                                secureDocumentKey3 = null;
                                                                                z12 = false;
                                                                                i2 = 0;
                                                                                z7 = th instanceof FileNotFoundException;
                                                                                secureDocumentKey2 = secureDocumentKey3;
                                                                                z6 = z12;
                                                                                z8 = true;
                                                                                FileLog.e(th, z7 ^ z8);
                                                                                i11 = secureDocumentKey2;
                                                                                i10 = i2;
                                                                                i12 = i11;
                                                                                Thread.interrupted();
                                                                                if (BuildVars.LOGS_ENABLED) {
                                                                                }
                                                                                if (bitmap2 != null) {
                                                                                }
                                                                                cacheImage = this.cacheImage;
                                                                                if (cacheImage == null) {
                                                                                }
                                                                                if (bitmap2 != null) {
                                                                                }
                                                                                themePreviewDrawable = null;
                                                                                onPostExecute(themePreviewDrawable);
                                                                            }
                                                                        } else {
                                                                            thumbnail = MediaStore.Video.Thumbnails.getThumbnail(ApplicationLoader.applicationContext.getContentResolver(), l2.longValue(), 1, options);
                                                                        }
                                                                        bitmap2 = thumbnail;
                                                                    } else {
                                                                        bitmap2 = bitmap7;
                                                                    }
                                                                    if (bitmap2 != null) {
                                                                        if (bitmap2 == null) {
                                                                            try {
                                                                                if (secureDocumentKey != null) {
                                                                                    fileInputStream = new EncryptedFileInputStream(file3, secureDocumentKey);
                                                                                } else if (z34) {
                                                                                    fileInputStream = new EncryptedFileInputStream(file3, this.cacheImage.encryptionKeyPath);
                                                                                } else {
                                                                                    try {
                                                                                        fileInputStream = new FileInputStream(file3);
                                                                                    } catch (Throwable th20) {
                                                                                        th = th20;
                                                                                        secureDocumentKey3 = null;
                                                                                        z12 = false;
                                                                                        i2 = 0;
                                                                                        z7 = th instanceof FileNotFoundException;
                                                                                        secureDocumentKey2 = secureDocumentKey3;
                                                                                        z6 = z12;
                                                                                        z8 = true;
                                                                                        FileLog.e(th, z7 ^ z8);
                                                                                        i11 = secureDocumentKey2;
                                                                                        i10 = i2;
                                                                                        i12 = i11;
                                                                                        Thread.interrupted();
                                                                                        if (BuildVars.LOGS_ENABLED) {
                                                                                        }
                                                                                        if (bitmap2 != null) {
                                                                                        }
                                                                                        cacheImage = this.cacheImage;
                                                                                        if (cacheImage == null) {
                                                                                        }
                                                                                        if (bitmap2 != null) {
                                                                                        }
                                                                                        themePreviewDrawable = null;
                                                                                        onPostExecute(themePreviewDrawable);
                                                                                    }
                                                                                }
                                                                                CacheImage cacheImage9 = this.cacheImage;
                                                                                try {
                                                                                    try {
                                                                                        if (!(cacheImage9.imageLocation.document instanceof TLRPC$TL_document)) {
                                                                                            String str14 = cacheImage9.filter;
                                                                                            if (str14 != null) {
                                                                                            }
                                                                                            intValue2 = 0;
                                                                                            intValue = 0;
                                                                                            bitmap2 = BitmapFactory.decodeStream(fileInputStream, null, options);
                                                                                            fileInputStream.close();
                                                                                        }
                                                                                        if (secureDocumentKey == null) {
                                                                                            try {
                                                                                                if (this.cacheImage.encryptionKeyPath == null) {
                                                                                                    fileInputStream.getChannel().position(0L);
                                                                                                    bitmap2 = BitmapFactory.decodeStream(fileInputStream, null, options);
                                                                                                    fileInputStream.close();
                                                                                                }
                                                                                            } catch (Throwable th21) {
                                                                                                th = th21;
                                                                                                secureDocumentKey4 = intValue2;
                                                                                                i2 = intValue;
                                                                                                z12 = false;
                                                                                                secureDocumentKey3 = secureDocumentKey4;
                                                                                                z7 = th instanceof FileNotFoundException;
                                                                                                secureDocumentKey2 = secureDocumentKey3;
                                                                                                z6 = z12;
                                                                                                z8 = true;
                                                                                                FileLog.e(th, z7 ^ z8);
                                                                                                i11 = secureDocumentKey2;
                                                                                                i10 = i2;
                                                                                                i12 = i11;
                                                                                                Thread.interrupted();
                                                                                                if (BuildVars.LOGS_ENABLED) {
                                                                                                }
                                                                                                if (bitmap2 != null) {
                                                                                                }
                                                                                                cacheImage = this.cacheImage;
                                                                                                if (cacheImage == null) {
                                                                                                }
                                                                                                if (bitmap2 != null) {
                                                                                                }
                                                                                                themePreviewDrawable = null;
                                                                                                onPostExecute(themePreviewDrawable);
                                                                                            }
                                                                                        }
                                                                                        fileInputStream.close();
                                                                                        if (secureDocumentKey != null) {
                                                                                            fileInputStream = new EncryptedFileInputStream(file3, secureDocumentKey);
                                                                                        } else if (z34) {
                                                                                            fileInputStream = new EncryptedFileInputStream(file3, this.cacheImage.encryptionKeyPath);
                                                                                        }
                                                                                        bitmap2 = BitmapFactory.decodeStream(fileInputStream, null, options);
                                                                                        fileInputStream.close();
                                                                                    } catch (Throwable th22) {
                                                                                        th = th22;
                                                                                        secureDocumentKey4 = intValue2;
                                                                                        i2 = intValue;
                                                                                        z12 = false;
                                                                                        secureDocumentKey3 = secureDocumentKey4;
                                                                                        z7 = th instanceof FileNotFoundException;
                                                                                        secureDocumentKey2 = secureDocumentKey3;
                                                                                        z6 = z12;
                                                                                        z8 = true;
                                                                                        FileLog.e(th, z7 ^ z8);
                                                                                        i11 = secureDocumentKey2;
                                                                                        i10 = i2;
                                                                                        i12 = i11;
                                                                                        Thread.interrupted();
                                                                                        if (BuildVars.LOGS_ENABLED) {
                                                                                        }
                                                                                        if (bitmap2 != null) {
                                                                                        }
                                                                                        cacheImage = this.cacheImage;
                                                                                        if (cacheImage == null) {
                                                                                        }
                                                                                        if (bitmap2 != null) {
                                                                                        }
                                                                                        themePreviewDrawable = null;
                                                                                        onPostExecute(themePreviewDrawable);
                                                                                    }
                                                                                    intValue2 = ((Integer) imageOrientation.second).intValue();
                                                                                } catch (Throwable th23) {
                                                                                    th = th23;
                                                                                    i2 = intValue;
                                                                                    secureDocumentKey4 = null;
                                                                                    z12 = false;
                                                                                    secureDocumentKey3 = secureDocumentKey4;
                                                                                    z7 = th instanceof FileNotFoundException;
                                                                                    secureDocumentKey2 = secureDocumentKey3;
                                                                                    z6 = z12;
                                                                                    z8 = true;
                                                                                    FileLog.e(th, z7 ^ z8);
                                                                                    i11 = secureDocumentKey2;
                                                                                    i10 = i2;
                                                                                    i12 = i11;
                                                                                    Thread.interrupted();
                                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                                    }
                                                                                    if (bitmap2 != null) {
                                                                                    }
                                                                                    cacheImage = this.cacheImage;
                                                                                    if (cacheImage == null) {
                                                                                    }
                                                                                    if (bitmap2 != null) {
                                                                                    }
                                                                                    themePreviewDrawable = null;
                                                                                    onPostExecute(themePreviewDrawable);
                                                                                }
                                                                                imageOrientation = AndroidUtilities.getImageOrientation(fileInputStream);
                                                                                intValue = ((Integer) imageOrientation.first).intValue();
                                                                            } catch (Throwable th24) {
                                                                                th = th24;
                                                                            }
                                                                        } else {
                                                                            intValue2 = 0;
                                                                            intValue = 0;
                                                                        }
                                                                        if (bitmap2 == null) {
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
                                                                                    if (secureDocumentKey != null) {
                                                                                        EncryptedFileInputStream.decryptBytesWithKeyFile(bArr5, 0, length3, secureDocumentKey);
                                                                                        secureDocumentKey = intValue2;
                                                                                        i2 = intValue;
                                                                                        byte[] computeSHA2563 = Utilities.computeSHA256(bArr5, 0, length3);
                                                                                        if (bArr != null && Arrays.equals(computeSHA2563, bArr)) {
                                                                                            z14 = false;
                                                                                            i14 = bArr5[0] & 255;
                                                                                            length3 -= i14;
                                                                                        }
                                                                                        z14 = true;
                                                                                        i14 = bArr5[0] & 255;
                                                                                        length3 -= i14;
                                                                                    } else {
                                                                                        secureDocumentKey = intValue2;
                                                                                        i2 = intValue;
                                                                                        if (z34) {
                                                                                            EncryptedFileInputStream.decryptBytesWithKeyFile(bArr5, 0, length3, this.cacheImage.encryptionKeyPath);
                                                                                        }
                                                                                        z14 = false;
                                                                                        i14 = 0;
                                                                                    }
                                                                                    if (!z14) {
                                                                                        bitmap2 = BitmapFactory.decodeByteArray(bArr5, i14, length3, options);
                                                                                    }
                                                                                } catch (Throwable th25) {
                                                                                    th = th25;
                                                                                    secureDocumentKey = secureDocumentKey;
                                                                                    try {
                                                                                        FileLog.e(th);
                                                                                        if (bitmap2 != null) {
                                                                                        }
                                                                                    } catch (Throwable th26) {
                                                                                        th = th26;
                                                                                        secureDocumentKey4 = secureDocumentKey;
                                                                                        z12 = false;
                                                                                        secureDocumentKey3 = secureDocumentKey4;
                                                                                        z7 = th instanceof FileNotFoundException;
                                                                                        secureDocumentKey2 = secureDocumentKey3;
                                                                                        z6 = z12;
                                                                                        z8 = true;
                                                                                        FileLog.e(th, z7 ^ z8);
                                                                                        i11 = secureDocumentKey2;
                                                                                        i10 = i2;
                                                                                        i12 = i11;
                                                                                        Thread.interrupted();
                                                                                        if (BuildVars.LOGS_ENABLED) {
                                                                                        }
                                                                                        if (bitmap2 != null) {
                                                                                        }
                                                                                        cacheImage = this.cacheImage;
                                                                                        if (cacheImage == null) {
                                                                                        }
                                                                                        if (bitmap2 != null) {
                                                                                        }
                                                                                        themePreviewDrawable = null;
                                                                                        onPostExecute(themePreviewDrawable);
                                                                                    }
                                                                                    i10 = i2;
                                                                                    i12 = i11;
                                                                                    Thread.interrupted();
                                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                                    }
                                                                                    if (bitmap2 != null) {
                                                                                    }
                                                                                    cacheImage = this.cacheImage;
                                                                                    if (cacheImage == null) {
                                                                                    }
                                                                                    if (bitmap2 != null) {
                                                                                    }
                                                                                    themePreviewDrawable = null;
                                                                                    onPostExecute(themePreviewDrawable);
                                                                                }
                                                                            } catch (Throwable th27) {
                                                                                th = th27;
                                                                                secureDocumentKey = intValue2;
                                                                                i2 = intValue;
                                                                            }
                                                                        } else {
                                                                            secureDocumentKey = intValue2;
                                                                            i2 = intValue;
                                                                        }
                                                                    } else {
                                                                        secureDocumentKey = 0;
                                                                        i2 = 0;
                                                                    }
                                                                    if (bitmap2 != null) {
                                                                        if (this.cacheImage.filter != null) {
                                                                            float width3 = bitmap2.getWidth();
                                                                            float height3 = bitmap2.getHeight();
                                                                            if (!options.inPurgeable && f5 != 0.0f && width3 != f5 && width3 > f5 + 20.0f) {
                                                                                if (width3 <= height3 || f5 <= f4) {
                                                                                    float f11 = height3 / f4;
                                                                                    if (f11 > 1.0f) {
                                                                                        i13 = (int) (width3 / f11);
                                                                                        bitmap5 = Bitmaps.createScaledBitmap(bitmap2, i13, (int) f4, true);
                                                                                    }
                                                                                    bitmap5 = bitmap2;
                                                                                } else {
                                                                                    float f12 = width3 / f5;
                                                                                    if (f12 > 1.0f) {
                                                                                        i13 = (int) f5;
                                                                                        f4 = height3 / f12;
                                                                                        bitmap5 = Bitmaps.createScaledBitmap(bitmap2, i13, (int) f4, true);
                                                                                    }
                                                                                    bitmap5 = bitmap2;
                                                                                }
                                                                                if (bitmap2 != bitmap5) {
                                                                                    bitmap2.recycle();
                                                                                    bitmap2 = bitmap5;
                                                                                }
                                                                            }
                                                                            if (bitmap2 != null) {
                                                                                if (z11) {
                                                                                    Bitmap createScaledBitmap2 = bitmap2.getWidth() * bitmap2.getHeight() > 22500 ? Bitmaps.createScaledBitmap(bitmap2, 100, 100, false) : bitmap2;
                                                                                    z12 = Utilities.needInvert(createScaledBitmap2, !options.inPurgeable ? 1 : 0, createScaledBitmap2.getWidth(), createScaledBitmap2.getHeight(), createScaledBitmap2.getRowBytes()) != 0;
                                                                                    if (createScaledBitmap2 != bitmap2) {
                                                                                        try {
                                                                                            createScaledBitmap2.recycle();
                                                                                        } catch (Throwable th28) {
                                                                                            th = th28;
                                                                                            secureDocumentKey3 = secureDocumentKey;
                                                                                            z7 = th instanceof FileNotFoundException;
                                                                                            secureDocumentKey2 = secureDocumentKey3;
                                                                                            z6 = z12;
                                                                                            z8 = true;
                                                                                            FileLog.e(th, z7 ^ z8);
                                                                                            i11 = secureDocumentKey2;
                                                                                            i10 = i2;
                                                                                            i12 = i11;
                                                                                            Thread.interrupted();
                                                                                            if (BuildVars.LOGS_ENABLED) {
                                                                                            }
                                                                                            if (bitmap2 != null) {
                                                                                            }
                                                                                            cacheImage = this.cacheImage;
                                                                                            if (cacheImage == null) {
                                                                                            }
                                                                                            if (bitmap2 != null) {
                                                                                            }
                                                                                            themePreviewDrawable = null;
                                                                                            onPostExecute(themePreviewDrawable);
                                                                                        }
                                                                                    }
                                                                                } else {
                                                                                    z12 = false;
                                                                                }
                                                                                if (c2 != 0 && (height3 > 100.0f || width3 > 100.0f)) {
                                                                                    height3 = 80.0f;
                                                                                    bitmap2 = Bitmaps.createScaledBitmap(bitmap2, 80, 80, false);
                                                                                    width3 = 80.0f;
                                                                                }
                                                                                if (c2 == 0 || height3 >= 100.0f || width3 >= 100.0f) {
                                                                                    z13 = false;
                                                                                } else {
                                                                                    if (bitmap2.getConfig() == Bitmap.Config.ARGB_8888) {
                                                                                        Utilities.blurBitmap(bitmap2, 3, !options.inPurgeable ? 1 : 0, bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getRowBytes());
                                                                                    }
                                                                                    z13 = true;
                                                                                }
                                                                                if (!z13 && options.inPurgeable) {
                                                                                    Utilities.pinBitmap(bitmap2);
                                                                                }
                                                                                i11 = secureDocumentKey;
                                                                                z6 = z12;
                                                                            }
                                                                        }
                                                                        z13 = false;
                                                                        z12 = false;
                                                                        if (!z13) {
                                                                            Utilities.pinBitmap(bitmap2);
                                                                        }
                                                                        i11 = secureDocumentKey;
                                                                        z6 = z12;
                                                                    } else if (z2 && (file3.length() == 0 || this.cacheImage.filter == null)) {
                                                                        file3.delete();
                                                                        i11 = secureDocumentKey;
                                                                        z6 = false;
                                                                    } else {
                                                                        i12 = secureDocumentKey;
                                                                        i10 = i2;
                                                                        z6 = false;
                                                                    }
                                                                    i10 = i2;
                                                                    i12 = i11;
                                                                }
                                                            }
                                                            config = Bitmap.Config.ARGB_8888;
                                                            options.inPreferredConfig = config;
                                                            options.inDither = false;
                                                            if (l2 == null) {
                                                            }
                                                            bitmap2 = bitmap7;
                                                            if (bitmap2 != null) {
                                                            }
                                                            if (bitmap2 != null) {
                                                            }
                                                            i10 = i2;
                                                            i12 = i11;
                                                        }
                                                    }
                                                }
                                                z11 = z36;
                                                f5 = f3;
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
                                                sb.append(file3);
                                                FileLog.e(sb.toString());
                                            }
                                            if (bitmap2 != null && !TextUtils.isEmpty(this.cacheImage.filter) && this.cacheImage.filter.contains("wallpaper")) {
                                                obj2 = this.cacheImage.parentObject;
                                                if (obj2 instanceof TLRPC$WallPaper) {
                                                    bitmap2 = applyWallpaperSetting(bitmap2, (TLRPC$WallPaper) obj2);
                                                }
                                            }
                                            cacheImage = this.cacheImage;
                                            if ((cacheImage == null && (str2 = cacheImage.filter) != null && str2.contains("ignoreOrientation")) || (!z6 && i10 == 0 && i12 == 0)) {
                                                if (bitmap2 != null) {
                                                    themePreviewDrawable = new BitmapDrawable(bitmap2);
                                                }
                                                themePreviewDrawable = null;
                                            } else {
                                                if (bitmap2 != null) {
                                                    themePreviewDrawable = new ExtendedBitmapDrawable(bitmap2, i10, i12);
                                                }
                                                themePreviewDrawable = null;
                                            }
                                        } else {
                                            l2 = l;
                                            z3 = z;
                                            if (str != null) {
                                                try {
                                                    options.inJustDecodeBounds = true;
                                                    options.inPreferredConfig = z35 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
                                                    fileInputStream2 = new FileInputStream(file3);
                                                    decodeStream = BitmapFactory.decodeStream(fileInputStream2, null, options);
                                                } catch (Throwable th29) {
                                                    th = th29;
                                                    f = 0.0f;
                                                    f2 = 0.0f;
                                                    bitmap2 = null;
                                                    c = 0;
                                                    z4 = false;
                                                    i = 1;
                                                    FileLog.e(th, !(th instanceof FileNotFoundException));
                                                    f3 = f;
                                                    f4 = f2;
                                                    c2 = c;
                                                    z5 = z4;
                                                    if (this.cacheImage.type == i) {
                                                    }
                                                    Thread.interrupted();
                                                    if (BuildVars.LOGS_ENABLED) {
                                                    }
                                                    if (bitmap2 != null) {
                                                    }
                                                    cacheImage = this.cacheImage;
                                                    if (cacheImage == null) {
                                                    }
                                                    if (bitmap2 != null) {
                                                    }
                                                    themePreviewDrawable = null;
                                                    onPostExecute(themePreviewDrawable);
                                                }
                                                try {
                                                    fileInputStream2.close();
                                                    int i36 = options.outWidth;
                                                    int i37 = options.outHeight;
                                                    options.inJustDecodeBounds = false;
                                                    float min6 = (Math.min(i37, i36) / Math.max(66, Math.min(AndroidUtilities.getRealScreenSize().x, AndroidUtilities.getRealScreenSize().y))) * 6.0f;
                                                    if (min6 < 1.0f) {
                                                        min6 = 1.0f;
                                                    }
                                                    if (min6 > 1.0f) {
                                                        int i38 = 1;
                                                        while (true) {
                                                            i16 = i38 * 2;
                                                            if (i38 * 4 > min6) {
                                                                break;
                                                            }
                                                            i38 = i16;
                                                        }
                                                        options.inSampleSize = i16;
                                                        bitmap2 = decodeStream;
                                                        f4 = 0.0f;
                                                        c2 = 0;
                                                    } else {
                                                        i15 = (int) min6;
                                                        bitmap2 = decodeStream;
                                                        f = 0.0f;
                                                        c = 0;
                                                        z4 = false;
                                                        z15 = z35;
                                                        f6 = 0.0f;
                                                    }
                                                } catch (Throwable th30) {
                                                    th = th30;
                                                    bitmap2 = decodeStream;
                                                    f = 0.0f;
                                                    f2 = 0.0f;
                                                    c = 0;
                                                    z4 = false;
                                                    i = 1;
                                                    FileLog.e(th, !(th instanceof FileNotFoundException));
                                                    f3 = f;
                                                    f4 = f2;
                                                    c2 = c;
                                                    z5 = z4;
                                                    if (this.cacheImage.type == i) {
                                                    }
                                                    Thread.interrupted();
                                                    if (BuildVars.LOGS_ENABLED) {
                                                    }
                                                    if (bitmap2 != null) {
                                                    }
                                                    cacheImage = this.cacheImage;
                                                    if (cacheImage == null) {
                                                    }
                                                    if (bitmap2 != null) {
                                                    }
                                                    themePreviewDrawable = null;
                                                    onPostExecute(themePreviewDrawable);
                                                }
                                            } else {
                                                f4 = 0.0f;
                                                c2 = 0;
                                                bitmap2 = null;
                                            }
                                            z5 = false;
                                            i = 1;
                                            f3 = 0.0f;
                                            if (this.cacheImage.type == i) {
                                            }
                                            Thread.interrupted();
                                            if (BuildVars.LOGS_ENABLED) {
                                                StringBuilder sb2 = new StringBuilder();
                                                sb2.append("Image Loader image is empty = ");
                                                sb2.append(bitmap2 != null);
                                                sb2.append(" ");
                                                sb2.append(file3);
                                                FileLog.e(sb2.toString());
                                            }
                                            if (bitmap2 != null) {
                                                obj2 = this.cacheImage.parentObject;
                                                if (obj2 instanceof TLRPC$WallPaper) {
                                                }
                                            }
                                            cacheImage = this.cacheImage;
                                            if (cacheImage == null) {
                                            }
                                            if (bitmap2 != null) {
                                            }
                                            themePreviewDrawable = null;
                                        }
                                        options.inSampleSize = i15;
                                        f3 = f;
                                        f4 = f6;
                                        z35 = z15;
                                        c2 = c;
                                        z5 = z4;
                                        i = 1;
                                        if (this.cacheImage.type == i) {
                                        }
                                        Thread.interrupted();
                                        if (BuildVars.LOGS_ENABLED) {
                                        }
                                        if (bitmap2 != null) {
                                        }
                                        cacheImage = this.cacheImage;
                                        if (cacheImage == null) {
                                        }
                                        if (bitmap2 != null) {
                                        }
                                        themePreviewDrawable = null;
                                    }
                                    str = null;
                                    l = null;
                                    z = false;
                                    z2 = true;
                                    BitmapFactory.Options options2 = new BitmapFactory.Options();
                                    options2.inSampleSize = 1;
                                    if (Build.VERSION.SDK_INT < 21) {
                                    }
                                    boolean z352 = ImageLoader.this.canForce8888;
                                    str3 = this.cacheImage.filter;
                                    if (str3 == null) {
                                    }
                                    options2.inSampleSize = i15;
                                    f3 = f;
                                    f4 = f6;
                                    z352 = z15;
                                    c2 = c;
                                    z5 = z4;
                                    i = 1;
                                    if (this.cacheImage.type == i) {
                                    }
                                    Thread.interrupted();
                                    if (BuildVars.LOGS_ENABLED) {
                                    }
                                    if (bitmap2 != null) {
                                    }
                                    cacheImage = this.cacheImage;
                                    if (cacheImage == null) {
                                    }
                                    if (bitmap2 != null) {
                                    }
                                    themePreviewDrawable = null;
                                }
                                onPostExecute(rLottieDrawable);
                                return;
                            }
                            Point point = AndroidUtilities.displaySize;
                            int i39 = point.x;
                            int i40 = point.y;
                            String str15 = cacheImage2.filter;
                            if (str15 != null) {
                                String[] split5 = str15.split("_");
                                if (split5.length >= 2) {
                                    float parseFloat7 = Float.parseFloat(split5[0]);
                                    float parseFloat8 = Float.parseFloat(split5[1]);
                                    float f13 = AndroidUtilities.density;
                                    i39 = (int) (parseFloat7 * f13);
                                    i40 = (int) (parseFloat8 * f13);
                                    CacheImage cacheImage10 = this.cacheImage;
                                    bitmap = SvgHelper.getBitmap(cacheImage10.finalFilePath, i39, i40, cacheImage10.imageType != 4);
                                    if (bitmap != null && !TextUtils.isEmpty(this.cacheImage.filter) && this.cacheImage.filter.contains("wallpaper")) {
                                        obj = this.cacheImage.parentObject;
                                        if (obj instanceof TLRPC$WallPaper) {
                                            bitmap = applyWallpaperSetting(bitmap, (TLRPC$WallPaper) obj);
                                        }
                                    }
                                    if (bitmap != null) {
                                        themePreviewDrawable = new BitmapDrawable(bitmap);
                                    }
                                    themePreviewDrawable = null;
                                }
                            }
                            CacheImage cacheImage102 = this.cacheImage;
                            bitmap = SvgHelper.getBitmap(cacheImage102.finalFilePath, i39, i40, cacheImage102.imageType != 4);
                            if (bitmap != null) {
                                obj = this.cacheImage.parentObject;
                                if (obj instanceof TLRPC$WallPaper) {
                                }
                            }
                            if (bitmap != null) {
                            }
                            themePreviewDrawable = null;
                        }
                    }
                    onPostExecute(themePreviewDrawable);
                } finally {
                }
            }
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

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$reportProgress$0(long j, long j2) {
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileLoadProgressChanged, this.url, Long.valueOf(j), Long.valueOf(j2));
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

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Code restructure failed: missing block: B:85:0x0121, code lost:
            if (r5 != (-1)) goto L86;
         */
        /* JADX WARN: Code restructure failed: missing block: B:86:0x0123, code lost:
            r0 = r11.fileSize;
         */
        /* JADX WARN: Code restructure failed: missing block: B:87:0x0125, code lost:
            if (r0 == 0) goto L60;
         */
        /* JADX WARN: Code restructure failed: missing block: B:88:0x0127, code lost:
            r3 = r0;
            reportProgress(r3, r3);
         */
        /* JADX WARN: Code restructure failed: missing block: B:90:0x012c, code lost:
            r0 = th;
         */
        /* JADX WARN: Code restructure failed: missing block: B:92:0x012e, code lost:
            r0 = e;
         */
        /* JADX WARN: Code restructure failed: missing block: B:93:0x012f, code lost:
            org.telegram.messenger.FileLog.e(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:95:0x0133, code lost:
            org.telegram.messenger.FileLog.e(r0);
         */
        /* JADX WARN: Removed duplicated region for block: B:125:0x0148 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:128:0x00a8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:99:0x013c A[Catch: all -> 0x0142, TRY_LEAVE, TryCatch #1 {all -> 0x0142, blocks: (B:97:0x0138, B:99:0x013c), top: B:113:0x0138 }] */
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
                if (!(th instanceof SocketTimeoutException) ? !(!(th instanceof UnknownHostException) && (!(th instanceof SocketException) ? !(th instanceof FileNotFoundException) : th.getMessage() == null || !th.getMessage().contains("ECONNRESET"))) : ApplicationLoader.isNetworkOnline()) {
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
                }
                z = false;
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
                z2 = z;
            }
            return Boolean.valueOf(z2);
        }

        @Override // android.os.AsyncTask
        protected void onCancelled() {
            ImageLoader.this.runHttpFileLoadTasks(this, 2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Boolean bool) {
            ImageLoader.this.runHttpFileLoadTasks(this, bool.booleanValue() ? 2 : 1);
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

        public HttpImageTask(CacheImage cacheImage, int i, String str) {
            this.cacheImage = cacheImage;
            this.imageSize = i;
            this.overrideUrl = str;
        }

        public HttpImageTask(CacheImage cacheImage, long j) {
            this.cacheImage = cacheImage;
            this.imageSize = j;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$doInBackground$2(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCancelled$6() {
            ImageLoader.this.runHttpTasks(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCancelled$7() {
            NotificationCenter.getInstance(this.cacheImage.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileLoadFailed, this.cacheImage.url, 1);
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
        public /* synthetic */ void lambda$onPostExecute$3(Boolean bool) {
            if (!bool.booleanValue()) {
                NotificationCenter.getInstance(this.cacheImage.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileLoadFailed, this.cacheImage.url, 2);
                return;
            }
            NotificationCenter notificationCenter = NotificationCenter.getInstance(this.cacheImage.currentAccount);
            int i = NotificationCenter.fileLoaded;
            CacheImage cacheImage = this.cacheImage;
            notificationCenter.lambda$postNotificationNameOnUIThread$1(i, cacheImage.url, cacheImage.finalFilePath);
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
        public /* synthetic */ void lambda$onPostExecute$5() {
            ImageLoader.this.runHttpTasks(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$reportProgress$0(long j, long j2) {
            NotificationCenter.getInstance(this.cacheImage.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.fileLoadProgressChanged, this.cacheImage.url, Long.valueOf(j), Long.valueOf(j2));
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

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't wrap try/catch for region: R(17:1|(9:101|102|(6:104|(1:106)|107|(1:109)|110|(16:112|114|115|4|(6:35|36|(1:44)|46|(3:50|51|(1:59))|(4:64|65|66|(2:67|(1:97)(3:69|70|(3:72|(3:74|75|76)(1:78)|77)(1:79))))(0))(0)|6|7|8|(1:10)|12|13|(1:15)|(2:27|28)|(1:23)|24|25))|140|(1:146)|107|(0)|110|(0))|3|4|(0)(0)|6|7|8|(0)|12|13|(0)|(0)|(3:19|21|23)|24|25|(1:(0))) */
        /* JADX WARN: Code restructure failed: missing block: B:100:0x016e, code lost:
            if (r7 != (-1)) goto L6;
         */
        /* JADX WARN: Code restructure failed: missing block: B:101:0x0170, code lost:
            r2 = r12.imageSize;
         */
        /* JADX WARN: Code restructure failed: missing block: B:102:0x0174, code lost:
            if (r2 == 0) goto L7;
         */
        /* JADX WARN: Code restructure failed: missing block: B:103:0x0176, code lost:
            reportProgress(r2, r2);
         */
        /* JADX WARN: Code restructure failed: missing block: B:105:0x017a, code lost:
            r2 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:106:0x017b, code lost:
            r0 = r2;
            r2 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:107:0x017e, code lost:
            r2 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:108:0x017f, code lost:
            r0 = r2;
            r2 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:109:0x0181, code lost:
            org.telegram.messenger.FileLog.e(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:110:0x0184, code lost:
            r0 = r2;
         */
        /* JADX WARN: Code restructure failed: missing block: B:111:0x0186, code lost:
            org.telegram.messenger.FileLog.e(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:117:0x0195, code lost:
            r1 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:118:0x0196, code lost:
            org.telegram.messenger.FileLog.e(r1);
         */
        /* JADX WARN: Code restructure failed: missing block: B:31:0x00b4, code lost:
            if (org.telegram.messenger.ApplicationLoader.isNetworkOnline() != false) goto L125;
         */
        /* JADX WARN: Code restructure failed: missing block: B:41:0x00d0, code lost:
            if (r13.getMessage().contains("ECONNRESET") != false) goto L125;
         */
        /* JADX WARN: Code restructure failed: missing block: B:42:0x00d2, code lost:
            r12.canRetry = false;
         */
        /* JADX WARN: Removed duplicated region for block: B:115:0x018f A[Catch: all -> 0x0195, TRY_LEAVE, TryCatch #4 {all -> 0x0195, blocks: (B:113:0x018b, B:115:0x018f), top: B:143:0x018b }] */
        /* JADX WARN: Removed duplicated region for block: B:121:0x019d A[Catch: all -> 0x01a1, TRY_LEAVE, TryCatch #2 {all -> 0x01a1, blocks: (B:119:0x0199, B:121:0x019d), top: B:141:0x0199 }] */
        /* JADX WARN: Removed duplicated region for block: B:130:0x01ae  */
        /* JADX WARN: Removed duplicated region for block: B:147:0x01a4 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:153:0x00ef A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:20:0x0065  */
        /* JADX WARN: Removed duplicated region for block: B:23:0x008f A[Catch: all -> 0x0020, TRY_LEAVE, TryCatch #1 {all -> 0x0020, blocks: (B:4:0x0009, B:6:0x0017, B:18:0x005f, B:21:0x0066, B:23:0x008f, B:11:0x0024, B:15:0x0034, B:17:0x0042), top: B:139:0x0009 }] */
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
                            if (!(th instanceof SocketTimeoutException)) {
                                if (!(th instanceof UnknownHostException)) {
                                    if (th instanceof SocketException) {
                                        if (th.getMessage() != null) {
                                        }
                                    } else if (!(th instanceof FileNotFoundException)) {
                                        if (!(th instanceof InterruptedIOException)) {
                                            z = true;
                                            FileLog.e(th, z);
                                            inputStream2 = inputStream;
                                        }
                                    }
                                    z = false;
                                    FileLog.e(th, z);
                                    inputStream2 = inputStream;
                                }
                                this.canRetry = false;
                                z = false;
                                FileLog.e(th, z);
                                inputStream2 = inputStream;
                            }
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
                            }
                        }
                        z2 = false;
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
                CacheImage cacheImage22 = this.cacheImage;
                cacheImage22.finalFilePath = cacheImage22.tempFilePath;
            }
            return Boolean.valueOf(z2);
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

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(final Boolean bool) {
            if (bool.booleanValue() || !this.canRetry) {
                ImageLoader imageLoader = ImageLoader.this;
                CacheImage cacheImage = this.cacheImage;
                imageLoader.fileDidLoaded(cacheImage.url, cacheImage.finalFilePath, 0);
            } else {
                ImageLoader.this.httpFileLoadError(this.cacheImage.url);
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

        static /* synthetic */ TLRPC$Document access$1002(ThumbGenerateInfo thumbGenerateInfo, TLRPC$Document tLRPC$Document) {
            thumbGenerateInfo.parentDocument = tLRPC$Document;
            return tLRPC$Document;
        }

        static /* synthetic */ boolean access$1102(ThumbGenerateInfo thumbGenerateInfo, boolean z) {
            thumbGenerateInfo.big = z;
            return z;
        }

        static /* synthetic */ ArrayList access$1200(ThumbGenerateInfo thumbGenerateInfo) {
            return thumbGenerateInfo.imageReceiverArray;
        }

        static /* synthetic */ ArrayList access$1300(ThumbGenerateInfo thumbGenerateInfo) {
            return thumbGenerateInfo.imageReceiverGuidsArray;
        }

        static /* synthetic */ String access$1402(ThumbGenerateInfo thumbGenerateInfo, String str) {
            thumbGenerateInfo.filter = str;
            return str;
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

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$removeTask$0(String str) {
            ImageLoader.this.thumbGenerateTasks.remove(str);
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

        /* JADX WARN: Code restructure failed: missing block: B:32:0x00c6, code lost:
            if (r11.info.big != false) goto L52;
         */
        /* JADX WARN: Removed duplicated region for block: B:46:0x00f8 A[Catch: all -> 0x000a, TryCatch #0 {all -> 0x000a, blocks: (B:3:0x0002, B:5:0x0006, B:9:0x000d, B:11:0x0055, B:14:0x005f, B:16:0x0067, B:18:0x0083, B:20:0x0089, B:44:0x00f0, B:46:0x00f8, B:48:0x00fc, B:52:0x0109, B:54:0x011a, B:56:0x0124, B:57:0x0128, B:61:0x013c, B:62:0x013f, B:66:0x0147, B:65:0x0144, B:67:0x016d, B:23:0x0094, B:33:0x00c8, B:29:0x00a8, B:31:0x00ba, B:35:0x00ce, B:37:0x00d4, B:39:0x00dc, B:41:0x00e4, B:43:0x00ec, B:17:0x0072, B:69:0x0171), top: B:73:0x0002, inners: #1 }] */
        /* JADX WARN: Removed duplicated region for block: B:48:0x00fc A[Catch: all -> 0x000a, TryCatch #0 {all -> 0x000a, blocks: (B:3:0x0002, B:5:0x0006, B:9:0x000d, B:11:0x0055, B:14:0x005f, B:16:0x0067, B:18:0x0083, B:20:0x0089, B:44:0x00f0, B:46:0x00f8, B:48:0x00fc, B:52:0x0109, B:54:0x011a, B:56:0x0124, B:57:0x0128, B:61:0x013c, B:62:0x013f, B:66:0x0147, B:65:0x0144, B:67:0x016d, B:23:0x0094, B:33:0x00c8, B:29:0x00a8, B:31:0x00ba, B:35:0x00ce, B:37:0x00d4, B:39:0x00dc, B:41:0x00e4, B:43:0x00ec, B:17:0x0072, B:69:0x0171), top: B:73:0x0002, inners: #1 }] */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void run() {
            int min;
            float f;
            String str;
            String file;
            Bitmap createVideoThumbnail;
            Bitmap createScaledBitmap;
            try {
            } catch (Throwable th) {
                FileLog.e(th);
                removeTask();
                return;
            }
            if (this.info == null) {
                removeTask();
                return;
            }
            final String str2 = "q_" + this.info.parentDocument.dc_id + "_" + this.info.parentDocument.id;
            File file2 = new File(FileLoader.getDirectory(4), str2 + ".jpg");
            if (!file2.exists() && this.originalPath.exists()) {
                if (this.info.big) {
                    Point point = AndroidUtilities.displaySize;
                    min = Math.max(point.x, point.y);
                } else {
                    Point point2 = AndroidUtilities.displaySize;
                    min = Math.min((int) NotificationCenter.updateBotMenuButton, Math.min(point2.x, point2.y) / 4);
                }
                int i = this.mediaType;
                Bitmap bitmap = null;
                if (i != 0) {
                    int i2 = 2;
                    if (i == 2) {
                        file = this.originalPath.toString();
                        if (this.info.big) {
                            createVideoThumbnail = SendMessagesHelper.createVideoThumbnail(file, i2);
                            bitmap = createVideoThumbnail;
                            if (bitmap != null) {
                                removeTask();
                                return;
                            }
                            int width = bitmap.getWidth();
                            int height = bitmap.getHeight();
                            if (width != 0 && height != 0) {
                                float f2 = width;
                                float f3 = min;
                                float f4 = height;
                                float min2 = Math.min(f2 / f3, f4 / f3);
                                if (min2 > 1.0f && (createScaledBitmap = Bitmaps.createScaledBitmap(bitmap, (int) (f2 / min2), (int) (f4 / min2), true)) != bitmap) {
                                    bitmap.recycle();
                                    bitmap = createScaledBitmap;
                                }
                                FileOutputStream fileOutputStream = new FileOutputStream(file2);
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
                                        ImageLoader.ThumbGenerateTask.this.lambda$run$1(str2, arrayList, bitmapDrawable, arrayList2);
                                    }
                                });
                                return;
                            }
                            removeTask();
                            return;
                        }
                        i2 = 1;
                        createVideoThumbnail = SendMessagesHelper.createVideoThumbnail(file, i2);
                        bitmap = createVideoThumbnail;
                        if (bitmap != null) {
                        }
                    } else {
                        if (i == 3) {
                            String lowerCase = this.originalPath.toString().toLowerCase();
                            if (lowerCase.endsWith("mp4")) {
                                file = this.originalPath.toString();
                            } else if (lowerCase.endsWith(".jpg") || lowerCase.endsWith(".jpeg") || lowerCase.endsWith(".png") || lowerCase.endsWith(".gif")) {
                                f = min;
                                str = lowerCase;
                            }
                        }
                        if (bitmap != null) {
                        }
                    }
                    FileLog.e(th);
                    removeTask();
                    return;
                }
                str = this.originalPath.toString();
                f = min;
                createVideoThumbnail = ImageLoader.loadBitmap(str, null, f, f, false);
                bitmap = createVideoThumbnail;
                if (bitmap != null) {
                }
            }
            removeTask();
        }
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

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.messenger.LruCache
            public int sizeOf(String str, BitmapDrawable bitmapDrawable) {
                return ImageLoader.this.sizeOfBitmapDrawable(bitmapDrawable);
            }
        };
        this.smallImagesMemCache = new LruCache<BitmapDrawable>((int) (f * 0.2f)) { // from class: org.telegram.messenger.ImageLoader.2
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

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.messenger.LruCache
            public int sizeOf(String str, BitmapDrawable bitmapDrawable) {
                return ImageLoader.this.sizeOfBitmapDrawable(bitmapDrawable);
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

            @Override // org.telegram.messenger.LruCache
            public BitmapDrawable put(String str, BitmapDrawable bitmapDrawable) {
                if (bitmapDrawable instanceof AnimatedFileDrawable) {
                    ImageLoader.this.cachedAnimatedFileDrawables.add((AnimatedFileDrawable) bitmapDrawable);
                }
                return (BitmapDrawable) super.put(str, (String) bitmapDrawable);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.messenger.LruCache
            public int sizeOf(String str, BitmapDrawable bitmapDrawable) {
                return ImageLoader.this.sizeOfBitmapDrawable(bitmapDrawable);
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

    /* JADX INFO: Access modifiers changed from: private */
    public void artworkLoadError(final String str) {
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.lambda$artworkLoadError$10(str);
            }
        });
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

    public static Drawable createStripedBitmap(ArrayList<TLRPC$PhotoSize> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i) instanceof TLRPC$TL_photoStrippedSize) {
                return new BitmapDrawable(ApplicationLoader.applicationContext.getResources(), getStrippedPhotoBitmap(((TLRPC$TL_photoStrippedSize) arrayList.get(i)).bytes, "b"));
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String cutFilter(String str) {
        if (str == null) {
            return null;
        }
        int indexOf = str.indexOf(64);
        return indexOf >= 0 ? str.substring(0, indexOf) : str;
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
                    if (readLine == null) {
                        String sb2 = sb.toString();
                        bufferedReader.close();
                        gZIPInputStream.close();
                        return sb2;
                    }
                    sb.append(readLine);
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
    public void fileDidLoaded(final String str, final File file, final int i) {
        this.imageLoadQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                ImageLoader.this.lambda$fileDidLoaded$11(str, i, file);
            }
        });
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

    private static TLRPC$PhotoSize findPhotoCachedSize(TLRPC$Message tLRPC$Message) {
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
        int i = 0;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) {
            int size = tLRPC$MessageMedia.photo.sizes.size();
            while (i < size) {
                TLRPC$PhotoSize tLRPC$PhotoSize = (TLRPC$PhotoSize) tLRPC$Message.media.photo.sizes.get(i);
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
                    TLRPC$PhotoSize tLRPC$PhotoSize3 = (TLRPC$PhotoSize) tLRPC$Message.media.webpage.photo.sizes.get(i);
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
                TLRPC$PhotoSize tLRPC$PhotoSize = (TLRPC$PhotoSize) tLRPC$MessageMedia.photo.sizes.get(i);
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
                    TLRPC$PhotoSize tLRPC$PhotoSize3 = (TLRPC$PhotoSize) tLRPC$MessageMedia.webpage.photo.sizes.get(i);
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

    public static MessageThumb generateMessageThumb(TLRPC$Message tLRPC$Message) {
        int i;
        int i2;
        Bitmap strippedPhotoBitmap;
        byte[] bArr;
        TLRPC$PhotoSize findPhotoCachedSize = findPhotoCachedSize(tLRPC$Message);
        if (findPhotoCachedSize == null || (bArr = findPhotoCachedSize.bytes) == null || bArr.length == 0) {
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
                        org.telegram.ui.Components.Point messageSize = ChatMessageCell.getMessageSize(i, i2);
                        String format = String.format(Locale.US, "%s_false@%d_%d_b", ImageLocation.getStrippedKey(tLRPC$Message, tLRPC$Message, tLRPC$PhotoSize), Integer.valueOf((int) (messageSize.x / AndroidUtilities.density)), Integer.valueOf((int) (messageSize.y / AndroidUtilities.density)));
                        if (!getInstance().isInMemCache(format, false) && (strippedPhotoBitmap = getStrippedPhotoBitmap(tLRPC$PhotoSize.bytes, null)) != null) {
                            Utilities.blurBitmap(strippedPhotoBitmap, 3, 1, strippedPhotoBitmap.getWidth(), strippedPhotoBitmap.getHeight(), strippedPhotoBitmap.getRowBytes());
                            float f = messageSize.x;
                            float f2 = AndroidUtilities.density;
                            Bitmap createScaledBitmap = Bitmaps.createScaledBitmap(strippedPhotoBitmap, (int) (f / f2), (int) (messageSize.y / f2), true);
                            if (createScaledBitmap != strippedPhotoBitmap) {
                                strippedPhotoBitmap.recycle();
                                strippedPhotoBitmap = createScaledBitmap;
                            }
                            return new MessageThumb(format, new BitmapDrawable(strippedPhotoBitmap));
                        }
                    }
                }
            }
        } else {
            File pathToAttach = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(findPhotoCachedSize, true);
            TLRPC$TL_photoSize_layer127 tLRPC$TL_photoSize_layer127 = new TLRPC$TL_photoSize_layer127();
            tLRPC$TL_photoSize_layer127.w = findPhotoCachedSize.w;
            tLRPC$TL_photoSize_layer127.h = findPhotoCachedSize.h;
            tLRPC$TL_photoSize_layer127.location = findPhotoCachedSize.location;
            tLRPC$TL_photoSize_layer127.size = findPhotoCachedSize.size;
            tLRPC$TL_photoSize_layer127.type = findPhotoCachedSize.type;
            if (pathToAttach.exists() && tLRPC$Message.grouped_id == 0) {
                org.telegram.ui.Components.Point messageSize2 = ChatMessageCell.getMessageSize(findPhotoCachedSize.w, findPhotoCachedSize.h);
                String format2 = String.format(Locale.US, "%d_%d@%d_%d_b", Long.valueOf(findPhotoCachedSize.location.volume_id), Integer.valueOf(findPhotoCachedSize.location.local_id), Integer.valueOf((int) (messageSize2.x / AndroidUtilities.density)), Integer.valueOf((int) (messageSize2.y / AndroidUtilities.density)));
                if (!getInstance().isInMemCache(format2, false)) {
                    String path = pathToAttach.getPath();
                    float f3 = messageSize2.x;
                    float f4 = AndroidUtilities.density;
                    Bitmap loadBitmap = loadBitmap(path, null, (int) (f3 / f4), (int) (messageSize2.y / f4), false);
                    if (loadBitmap != null) {
                        Utilities.blurBitmap(loadBitmap, 3, 1, loadBitmap.getWidth(), loadBitmap.getHeight(), loadBitmap.getRowBytes());
                        float f5 = messageSize2.x;
                        float f6 = AndroidUtilities.density;
                        Bitmap createScaledBitmap2 = Bitmaps.createScaledBitmap(loadBitmap, (int) (f5 / f6), (int) (messageSize2.y / f6), true);
                        if (createScaledBitmap2 != loadBitmap) {
                            loadBitmap.recycle();
                            loadBitmap = createScaledBitmap2;
                        }
                        return new MessageThumb(format2, new BitmapDrawable(loadBitmap));
                    }
                }
            }
        }
        return null;
    }

    private void generateThumb(int i, File file, ThumbGenerateInfo thumbGenerateInfo) {
        if ((i != 0 && i != 2 && i != 3) || file == null || thumbGenerateInfo == null) {
            return;
        }
        if (this.thumbGenerateTasks.get(FileLoader.getAttachFileName(thumbGenerateInfo.parentDocument)) == null) {
            this.thumbGeneratingQueue.postRunnable(new ThumbGenerateTask(i, file, thumbGenerateInfo));
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

    public static String getHttpFileName(String str) {
        return Utilities.MD5(str);
    }

    public static File getHttpFilePath(String str, String str2) {
        String httpUrlExtension = getHttpUrlExtension(str, str2);
        File directory = FileLoader.getDirectory(4);
        return new File(directory, Utilities.MD5(str) + "." + httpUrlExtension);
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
    public boolean isAnimatedAvatar(String str) {
        return str != null && str.endsWith("avatar");
    }

    private boolean isPFrame(String str) {
        return str != null && str.endsWith("pframe");
    }

    public static boolean isSdCardPath(File file) {
        return !TextUtils.isEmpty(SharedConfig.storageCacheDir) && file.getAbsolutePath().startsWith(SharedConfig.storageCacheDir);
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
    public /* synthetic */ void lambda$cancelForceLoadingForImageReceiver$6(String str) {
        this.forceLoadingImages.remove(str);
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

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$checkMediaPaths$0(SparseArray sparseArray, Runnable runnable) {
        FileLoader.setMediaDirs(sparseArray);
        if (runnable != null) {
            runnable.run();
        }
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

    /*  JADX ERROR: IndexOutOfBoundsException in pass: ConstInlineVisitor
        java.lang.IndexOutOfBoundsException: Index -1 out of bounds for length 5
        	at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        	at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        	at java.base/java.util.Objects.checkIndex(Objects.java:374)
        	at java.base/java.util.ArrayList.get(ArrayList.java:459)
        	at jadx.core.dex.visitors.ConstInlineVisitor.needExplicitCast(ConstInlineVisitor.java:282)
        	at jadx.core.dex.visitors.ConstInlineVisitor.replaceArg(ConstInlineVisitor.java:263)
        	at jadx.core.dex.visitors.ConstInlineVisitor.replaceConst(ConstInlineVisitor.java:181)
        	at jadx.core.dex.visitors.ConstInlineVisitor.checkInsn(ConstInlineVisitor.java:109)
        	at jadx.core.dex.visitors.ConstInlineVisitor.process(ConstInlineVisitor.java:53)
        	at jadx.core.dex.visitors.ConstInlineVisitor.visit(ConstInlineVisitor.java:45)
        */
    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createLoadOperationForImageReceiver$7(int r27, java.lang.String r28, java.lang.String r29, int r30, org.telegram.messenger.ImageReceiver r31, int r32, java.lang.String r33, int r34, org.telegram.messenger.ImageLocation r35, boolean r36, java.lang.Object r37, int r38, org.telegram.tgnet.TLRPC$Document r39, boolean r40, boolean r41, java.lang.String r42, int r43, long r44) {
        /*
            Method dump skipped, instructions count: 1701
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ImageLoader.lambda$createLoadOperationForImageReceiver$7(int, java.lang.String, java.lang.String, int, org.telegram.messenger.ImageReceiver, int, java.lang.String, int, org.telegram.messenger.ImageLocation, boolean, java.lang.Object, int, org.telegram.tgnet.TLRPC$Document, boolean, boolean, java.lang.String, int, long):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fileDidFailedLoad$12(String str) {
        CacheImage cacheImage = this.imageLoadingByUrl.get(str);
        if (cacheImage != null) {
            cacheImage.setImageAndClear(null, null);
        }
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
            if (cacheOutTask.cacheImage.type == 1) {
                this.cacheThumbOutQueue.postRunnable(cacheOutTask);
            } else {
                this.cacheOutQueue.postRunnable(cacheOutTask, cacheOutTask.cacheImage.priority);
            }
        }
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runHttpFileLoadTasks$13(HttpFileTask httpFileTask) {
        this.httpFileLoadTasks.add(httpFileTask);
        runHttpFileLoadTasks(null, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runHttpFileLoadTasks$14(HttpFileTask httpFileTask, int i) {
        if (httpFileTask != null) {
            this.currentHttpFileLoadTasksCount--;
        }
        if (httpFileTask != null) {
            if (i == 1) {
                if (httpFileTask.canRetry) {
                    final HttpFileTask httpFileTask2 = new HttpFileTask(httpFileTask.url, httpFileTask.tempFile, httpFileTask.ext, httpFileTask.currentAccount);
                    Runnable runnable = new Runnable() { // from class: org.telegram.messenger.ImageLoader$$ExternalSyntheticLambda6
                        @Override // java.lang.Runnable
                        public final void run() {
                            ImageLoader.this.lambda$runHttpFileLoadTasks$13(httpFileTask2);
                        }
                    };
                    this.retryHttpsTasks.put(httpFileTask.url, runnable);
                    AndroidUtilities.runOnUIThread(runnable, 1000L);
                } else {
                    this.httpFileLoadTasksByKeys.remove(httpFileTask.url);
                    NotificationCenter.getInstance(httpFileTask.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.httpFileDidFailedLoad, httpFileTask.url, 0);
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

    /* JADX WARN: Can't wrap try/catch for region: R(29:1|(3:(30:6|(1:8)(2:131|(2:135|136))|9|10|(1:12)(1:(22:124|125|126|14|(1:16)(1:122)|17|(1:19)|20|(3:22|(2:23|(1:25)(1:26))|27)|28|(1:30)|31|32|33|(4:37|38|39|40)|48|(10:102|103|104|(5:106|(1:108)(1:114)|109|(1:111)(1:113)|112)|115|(1:117)|53|(2:(1:56)|57)|58|(2:60|(4:62|(1:64)|65|(3:67|68|69)(1:71))(1:72))(1:(5:74|75|(4:77|(1:79)|80|(2:82|83))|85|86)(1:100)))|52|53|(0)|58|(0)(0)))|13|14|(0)(0)|17|(0)|20|(0)|28|(0)|31|32|33|(5:35|37|38|39|40)|48|(1:50)|102|103|104|(0)|115|(0)|53|(0)|58|(0)(0))|58|(0)(0))|140|10|(0)(0)|13|14|(0)(0)|17|(0)|20|(0)|28|(0)|31|32|33|(0)|48|(0)|102|103|104|(0)|115|(0)|53|(0)|(2:(0)|(0))) */
    /* JADX WARN: Can't wrap try/catch for region: R(31:1|(30:6|(1:8)(2:131|(2:135|136))|9|10|(1:12)(1:(22:124|125|126|14|(1:16)(1:122)|17|(1:19)|20|(3:22|(2:23|(1:25)(1:26))|27)|28|(1:30)|31|32|33|(4:37|38|39|40)|48|(10:102|103|104|(5:106|(1:108)(1:114)|109|(1:111)(1:113)|112)|115|(1:117)|53|(2:(1:56)|57)|58|(2:60|(4:62|(1:64)|65|(3:67|68|69)(1:71))(1:72))(1:(5:74|75|(4:77|(1:79)|80|(2:82|83))|85|86)(1:100)))|52|53|(0)|58|(0)(0)))|13|14|(0)(0)|17|(0)|20|(0)|28|(0)|31|32|33|(5:35|37|38|39|40)|48|(1:50)|102|103|104|(0)|115|(0)|53|(0)|58|(0)(0))|140|10|(0)(0)|13|14|(0)(0)|17|(0)|20|(0)|28|(0)|31|32|33|(0)|48|(0)|102|103|104|(0)|115|(0)|53|(0)|58|(0)(0)|(2:(0)|(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00d0, code lost:
        if (r9 == null) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00d7, code lost:
        if (r9 == null) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00d9, code lost:
        r9.close();
     */
    /* JADX WARN: Removed duplicated region for block: B:119:0x01bc  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0047  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x004b  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0080  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x008a  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0096  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00a7  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00b8 A[Catch: all -> 0x00dd, TRY_LEAVE, TryCatch #10 {all -> 0x00dd, blocks: (B:48:0x00aa, B:50:0x00b8, B:59:0x00d9, B:61:0x00df, B:63:0x00e9, B:65:0x00f3), top: B:158:0x00aa }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x00e9 A[Catch: all -> 0x00dd, TryCatch #10 {all -> 0x00dd, blocks: (B:48:0x00aa, B:50:0x00b8, B:59:0x00d9, B:61:0x00df, B:63:0x00e9, B:65:0x00f3), top: B:158:0x00aa }] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0102 A[Catch: all -> 0x0124, TryCatch #2 {all -> 0x0124, blocks: (B:66:0x00f8, B:68:0x0102, B:72:0x0113, B:76:0x0120, B:78:0x0126, B:80:0x0130), top: B:145:0x00f8 }] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0130 A[Catch: all -> 0x0124, TRY_LEAVE, TryCatch #2 {all -> 0x0124, blocks: (B:66:0x00f8, B:68:0x0102, B:72:0x0113, B:76:0x0120, B:78:0x0126, B:80:0x0130), top: B:145:0x00f8 }] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0146  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0153 A[Catch: all -> 0x0161, TRY_ENTER, TryCatch #7 {all -> 0x019c, blocks: (B:131:0x01e6, B:89:0x0153, B:91:0x0159, B:93:0x015d, B:96:0x0163, B:98:0x0177), top: B:153:0x0151 }] */
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
        Pair<Integer, Integer> imageOrientation;
        InputStream inputStream;
        String path;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
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
                            if (str2 == null) {
                                Bitmap decodeFile = BitmapFactory.decodeFile(str2, options);
                                if (decodeFile != null) {
                                    if (options.inPurgeable) {
                                        Utilities.pinBitmap(decodeFile);
                                    }
                                    Bitmap createBitmap = Bitmaps.createBitmap(decodeFile, 0, 0, decodeFile.getWidth(), decodeFile.getHeight(), matrix, true);
                                    if (createBitmap != decodeFile) {
                                        decodeFile.recycle();
                                        return createBitmap;
                                    }
                                    return decodeFile;
                                }
                                return decodeFile;
                            } else if (uri != null) {
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
                                openInputStream.close();
                                return bitmap;
                            } else {
                                return null;
                            }
                        }
                        matrix = null;
                        f3 = max / options.inSampleSize;
                        if (f3 > 1.0f) {
                        }
                        if (str2 == null) {
                        }
                    } catch (Throwable th3) {
                        FileLog.e(th3);
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
                if (str2 == null) {
                }
            }
            if (str2 == null) {
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
    }

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

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: replaceImageInCacheInternal */
    public void lambda$replaceImageInCache$5(String str, String str2, ImageLocation imageLocation) {
        int i = 0;
        while (i < 2) {
            ArrayList<String> filterKeys = (i == 0 ? this.memCache : this.smallImagesMemCache).getFilterKeys(str);
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
            i++;
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

    public static void saveMessageThumbs(TLRPC$Message tLRPC$Message) {
        byte[] bArr;
        TLRPC$PhotoSize tLRPC$TL_photoSize_layer127;
        TLRPC$Photo tLRPC$Photo;
        ArrayList<TLRPC$PhotoSize> arrayList;
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
        if (findPhotoCachedSize.h > 50 || findPhotoCachedSize.w > 50) {
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
                        if (length <= 0 || length % 48 != 0) {
                            Utilities.random.nextBytes(bArr2);
                            Utilities.random.nextBytes(bArr3);
                            randomAccessFile.write(bArr2);
                            randomAccessFile.write(bArr3);
                        } else {
                            randomAccessFile.read(bArr2, 0, 32);
                            randomAccessFile.read(bArr3, 0, 16);
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
        } else {
            tLRPC$TL_photoSize_layer127 = new TLRPC$TL_photoStrippedSize();
            tLRPC$TL_photoSize_layer127.location = findPhotoCachedSize.location;
            tLRPC$TL_photoSize_layer127.bytes = findPhotoCachedSize.bytes;
            tLRPC$TL_photoSize_layer127.h = findPhotoCachedSize.h;
            tLRPC$TL_photoSize_layer127.w = findPhotoCachedSize.w;
        }
        TLRPC$MessageMedia tLRPC$MessageMedia2 = tLRPC$Message.media;
        if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPhoto) {
            int size = tLRPC$MessageMedia2.photo.sizes.size();
            while (i < size) {
                if (((TLRPC$PhotoSize) tLRPC$Message.media.photo.sizes.get(i)) instanceof TLRPC$TL_photoCachedSize) {
                    tLRPC$Photo = tLRPC$Message.media.photo;
                } else {
                    i++;
                }
            }
            return;
        } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaDocument) {
            int size2 = tLRPC$MessageMedia2.document.thumbs.size();
            while (i < size2) {
                if (tLRPC$Message.media.document.thumbs.get(i) instanceof TLRPC$TL_photoCachedSize) {
                    arrayList = tLRPC$Message.media.document.thumbs;
                    arrayList.set(i, tLRPC$TL_photoSize_layer127);
                }
                i++;
            }
            return;
        } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaWebPage) {
            int size3 = tLRPC$MessageMedia2.webpage.photo.sizes.size();
            while (i < size3) {
                if (((TLRPC$PhotoSize) tLRPC$Message.media.webpage.photo.sizes.get(i)) instanceof TLRPC$TL_photoCachedSize) {
                    tLRPC$Photo = tLRPC$Message.media.webpage.photo;
                } else {
                    i++;
                }
            }
            return;
        } else {
            return;
        }
        arrayList = tLRPC$Photo.sizes;
        arrayList.set(i, tLRPC$TL_photoSize_layer127);
    }

    public static void saveMessageThumbs(TLRPC$Message tLRPC$Message, TLRPC$MessageMedia tLRPC$MessageMedia) {
        TLRPC$PhotoSize findPhotoCachedSize;
        byte[] bArr;
        TLRPC$PhotoSize tLRPC$TL_photoSize_layer127;
        TLRPC$Photo tLRPC$Photo;
        ArrayList<TLRPC$PhotoSize> arrayList;
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
        if (findPhotoCachedSize.h > 50 || findPhotoCachedSize.w > 50) {
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
                        if (length <= 0 || length % 48 != 0) {
                            Utilities.random.nextBytes(bArr2);
                            Utilities.random.nextBytes(bArr3);
                            randomAccessFile.write(bArr2);
                            randomAccessFile.write(bArr3);
                        } else {
                            randomAccessFile.read(bArr2, 0, 32);
                            randomAccessFile.read(bArr3, 0, 16);
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
        } else {
            tLRPC$TL_photoSize_layer127 = new TLRPC$TL_photoStrippedSize();
            tLRPC$TL_photoSize_layer127.location = findPhotoCachedSize.location;
            tLRPC$TL_photoSize_layer127.bytes = findPhotoCachedSize.bytes;
            tLRPC$TL_photoSize_layer127.h = findPhotoCachedSize.h;
            tLRPC$TL_photoSize_layer127.w = findPhotoCachedSize.w;
        }
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) {
            int size = tLRPC$MessageMedia.photo.sizes.size();
            while (i < size) {
                if (((TLRPC$PhotoSize) tLRPC$MessageMedia.photo.sizes.get(i)) instanceof TLRPC$TL_photoCachedSize) {
                    tLRPC$Photo = tLRPC$MessageMedia.photo;
                } else {
                    i++;
                }
            }
            return;
        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
            int size2 = tLRPC$MessageMedia.document.thumbs.size();
            while (i < size2) {
                if (tLRPC$MessageMedia.document.thumbs.get(i) instanceof TLRPC$TL_photoCachedSize) {
                    arrayList = tLRPC$MessageMedia.document.thumbs;
                    arrayList.set(i, tLRPC$TL_photoSize_layer127);
                }
                i++;
            }
            return;
        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) {
            int size3 = tLRPC$MessageMedia.webpage.photo.sizes.size();
            while (i < size3) {
                if (((TLRPC$PhotoSize) tLRPC$MessageMedia.webpage.photo.sizes.get(i)) instanceof TLRPC$TL_photoCachedSize) {
                    tLRPC$Photo = tLRPC$MessageMedia.webpage.photo;
                } else {
                    i++;
                }
            }
            return;
        } else {
            return;
        }
        arrayList = tLRPC$Photo.sizes;
        arrayList.set(i, tLRPC$TL_photoSize_layer127);
    }

    public static void saveMessagesThumbs(ArrayList<TLRPC$Message> arrayList) {
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            saveMessageThumbs(arrayList.get(i));
        }
    }

    public static TLRPC$PhotoSize scaleAndSaveImage(Bitmap bitmap, float f, float f2, int i, boolean z) {
        return scaleAndSaveImage(null, bitmap, Bitmap.CompressFormat.JPEG, false, f, f2, i, z, 0, 0, false);
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

    public static TLRPC$PhotoSize scaleAndSaveImage(TLRPC$PhotoSize tLRPC$PhotoSize, Bitmap bitmap, float f, float f2, int i, boolean z, boolean z2) {
        return scaleAndSaveImage(tLRPC$PhotoSize, bitmap, Bitmap.CompressFormat.JPEG, false, f, f2, i, z, 0, 0, z2);
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

    /* JADX WARN: Removed duplicated region for block: B:49:0x00c5  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00d5  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00eb  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static TLRPC$PhotoSize scaleAndSaveImageInternal(TLRPC$PhotoSize tLRPC$PhotoSize, Bitmap bitmap, Bitmap.CompressFormat compressFormat, boolean z, int i, int i2, float f, float f2, float f3, int i3, boolean z2, boolean z3, boolean z4) {
        TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated;
        Bitmap createScaledBitmap = (f3 > 1.0f || z3) ? Bitmaps.createScaledBitmap(bitmap, i, i2, true) : bitmap;
        int i4 = 0;
        if (tLRPC$PhotoSize != null) {
            TLRPC$FileLocation tLRPC$FileLocation = tLRPC$PhotoSize.location;
            if (tLRPC$FileLocation instanceof TLRPC$TL_fileLocationToBeDeprecated) {
                tLRPC$TL_fileLocationToBeDeprecated = (TLRPC$TL_fileLocationToBeDeprecated) tLRPC$FileLocation;
                int i5 = 7.$SwitchMap$android$graphics$Bitmap$CompressFormat[compressFormat.ordinal()];
                String str = (i5 != 1 || i5 == 2 || i5 == 3) ? ".webp" : ".jpg";
                FileOutputStream fileOutputStream = new FileOutputStream(new File(FileLoader.getDirectory((!z4 || tLRPC$TL_fileLocationToBeDeprecated.volume_id == -2147483648L) ? 4 : 4), tLRPC$TL_fileLocationToBeDeprecated.volume_id + "_" + tLRPC$TL_fileLocationToBeDeprecated.local_id + str));
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
        int i6 = tLRPC$PhotoSize.w;
        tLRPC$PhotoSize.type = (i6 > 100 || height > 100) ? (i6 > 320 || height > 320) ? (i6 > 800 || height > 800) ? (i6 > 1280 || height > 1280) ? "w" : "y" : "x" : "m" : "s";
        int i52 = 7.$SwitchMap$android$graphics$Bitmap$CompressFormat[compressFormat.ordinal()];
        if (i52 != 1) {
        }
        FileOutputStream fileOutputStream2 = new FileOutputStream(new File(FileLoader.getDirectory((!z4 || tLRPC$TL_fileLocationToBeDeprecated.volume_id == -2147483648L) ? 4 : 4), tLRPC$TL_fileLocationToBeDeprecated.volume_id + "_" + tLRPC$TL_fileLocationToBeDeprecated.local_id + str));
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

    /* JADX INFO: Access modifiers changed from: private */
    public int sizeOfBitmapDrawable(BitmapDrawable bitmapDrawable) {
        if (!(bitmapDrawable instanceof AnimatedFileDrawable)) {
            return bitmapDrawable instanceof RLottieDrawable ? bitmapDrawable.getIntrinsicWidth() * bitmapDrawable.getIntrinsicHeight() * 8 : bitmapDrawable.getBitmap().getByteCount();
        }
        AnimatedFileDrawable animatedFileDrawable = (AnimatedFileDrawable) bitmapDrawable;
        return Math.max(animatedFileDrawable.getIntrinsicHeight() * bitmapDrawable.getIntrinsicWidth() * 12, animatedFileDrawable.getRenderingHeight() * animatedFileDrawable.getRenderingWidth() * 12);
    }

    private boolean useLottieMemCache(ImageLocation imageLocation, String str) {
        if (str.endsWith("_firstframe") || str.endsWith("_lastframe")) {
            return false;
        }
        return (imageLocation != null && (MessageObject.isAnimatedStickerDocument(imageLocation.document, true) || imageLocation.imageType == 1 || MessageObject.isVideoSticker(imageLocation.document))) || isAnimatedAvatar(str);
    }

    public void addTestWebFile(String str, WebFile webFile) {
        if (str == null || webFile == null) {
            return;
        }
        this.testWebFile.put(str, webFile);
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

    public void clearMemory() {
        this.smallImagesMemCache.evictAll();
        this.memCache.evictAll();
        this.lottieMemCache.evictAll();
    }

    /* JADX WARN: Code restructure failed: missing block: B:68:0x015f, code lost:
        if (r2.mkdirs() != false) goto L167;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0165, code lost:
        if (r2.canWrite() == false) goto L169;
     */
    /* JADX WARN: Removed duplicated region for block: B:157:0x0374 A[Catch: Exception -> 0x0387, TRY_LEAVE, TryCatch #9 {Exception -> 0x0387, blocks: (B:151:0x0357, B:153:0x0365, B:155:0x036b, B:157:0x0374), top: B:196:0x0357, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:167:0x03a8 A[Catch: Exception -> 0x03bb, TRY_LEAVE, TryCatch #7 {Exception -> 0x03bb, blocks: (B:161:0x038b, B:163:0x0399, B:165:0x039f, B:167:0x03a8), top: B:192:0x038b, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:186:0x01e2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x019b A[Catch: Exception -> 0x00ad, TryCatch #0 {Exception -> 0x00ad, blocks: (B:12:0x0053, B:14:0x005f, B:16:0x006d, B:19:0x0075, B:21:0x007c, B:24:0x00aa, B:27:0x00b0, B:29:0x00bc, B:32:0x00c5, B:34:0x00c8, B:38:0x00e9, B:37:0x00cd, B:39:0x00ec, B:56:0x012d, B:73:0x018e, B:75:0x019b, B:77:0x01a6, B:79:0x01ae, B:81:0x01b6, B:83:0x01c2, B:84:0x01cd, B:85:0x01d0, B:147:0x034c, B:137:0x030a, B:127:0x02c9, B:117:0x0288, B:107:0x0247, B:149:0x0351, B:170:0x03bc, B:160:0x0388, B:174:0x03ca, B:97:0x0213, B:55:0x012a, B:57:0x0139, B:59:0x0141, B:72:0x0186, B:62:0x014e, B:64:0x0154, B:69:0x0161, B:71:0x0167, B:67:0x015b, B:171:0x03c0, B:173:0x03c4, B:138:0x030d, B:140:0x031f, B:142:0x0326, B:144:0x0335, B:128:0x02cc, B:130:0x02de, B:132:0x02e5, B:134:0x02f4, B:88:0x01e2, B:90:0x01f2, B:92:0x01f8, B:94:0x01ff, B:161:0x038b, B:163:0x0399, B:165:0x039f, B:167:0x03a8, B:118:0x028b, B:120:0x029d, B:122:0x02a4, B:124:0x02b3, B:151:0x0357, B:153:0x0365, B:155:0x036b, B:157:0x0374, B:108:0x024a, B:110:0x025c, B:112:0x0263, B:114:0x0272, B:98:0x0216, B:100:0x0226, B:102:0x022c, B:104:0x0233), top: B:178:0x0053, inners: #1, #2, #4, #7, #8, #9, #10, #11 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public SparseArray<File> createMediaPaths() {
        File file;
        File file2;
        File[] externalMediaDirs;
        File file3;
        File file4;
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
                                file2 = new File(ApplicationLoader.applicationContext.getExternalFilesDir(null), "Telegram");
                                this.telegramPath = file2;
                                file2.mkdirs();
                                if (!this.telegramPath.isDirectory()) {
                                }
                                if (this.telegramPath.isDirectory()) {
                                }
                                if (file != null) {
                                    try {
                                        file4 = new File(file, "Telegram Images");
                                        file4.mkdir();
                                        if (file4.isDirectory()) {
                                            sparseArray.put(100, file4);
                                            if (BuildVars.LOGS_ENABLED) {
                                            }
                                        }
                                    } catch (Exception e4) {
                                        FileLog.e(e4);
                                    }
                                    try {
                                        file3 = new File(file, "Telegram Video");
                                        file3.mkdir();
                                        if (file3.isDirectory()) {
                                            sparseArray.put(101, file3);
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
                    file2 = new File(ApplicationLoader.applicationContext.getExternalFilesDir(null), "Telegram");
                } else {
                    if (TextUtils.isEmpty(SharedConfig.storageCacheDir) || !externalStorageDirectory.getAbsolutePath().startsWith(SharedConfig.storageCacheDir)) {
                        if (externalStorageDirectory.exists()) {
                            if (externalStorageDirectory.isDirectory()) {
                            }
                            FileLog.d("can't write to this directory = " + externalStorageDirectory + " use files dir");
                            externalStorageDirectory = ApplicationLoader.applicationContext.getExternalFilesDir(null);
                        }
                    }
                    file2 = new File(externalStorageDirectory, "Telegram");
                    file = null;
                }
                this.telegramPath = file2;
                file2.mkdirs();
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
                if (file != null && file.isDirectory()) {
                    file4 = new File(file, "Telegram Images");
                    file4.mkdir();
                    if (file4.isDirectory() && canMoveFiles(cacheDir, file4, 0)) {
                        sparseArray.put(100, file4);
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("image path = " + file4);
                        }
                    }
                    file3 = new File(file, "Telegram Video");
                    file3.mkdir();
                    if (file3.isDirectory() && canMoveFiles(cacheDir, file3, 2)) {
                        sparseArray.put(101, file3);
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("video path = " + file3);
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

    public DispatchQueuePriority getCacheOutQueue() {
        return this.cacheOutQueue;
    }

    public Float getFileProgress(String str) {
        long[] jArr;
        if (str == null || (jArr = this.fileProgresses.get(str)) == null) {
            return null;
        }
        long j = jArr[1];
        return Float.valueOf(j == 0 ? 0.0f : Math.min(1.0f, ((float) jArr[0]) / ((float) j)));
    }

    public long[] getFileProgressSizes(String str) {
        if (str == null) {
            return null;
        }
        return this.fileProgresses.get(str);
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

    public BitmapDrawable getImageFromMemory(TLObject tLObject, String str, String str2) {
        StringBuilder sb;
        long j;
        String str3 = null;
        if (tLObject == null && str == null) {
            return null;
        }
        if (str != null) {
            str3 = Utilities.MD5(str);
        } else {
            if (tLObject instanceof TLRPC$FileLocation) {
                TLRPC$FileLocation tLRPC$FileLocation = (TLRPC$FileLocation) tLObject;
                sb = new StringBuilder();
                sb.append(tLRPC$FileLocation.volume_id);
                sb.append("_");
                sb.append(tLRPC$FileLocation.local_id);
            } else {
                if (tLObject instanceof TLRPC$Document) {
                    TLRPC$Document tLRPC$Document = (TLRPC$Document) tLObject;
                    sb = new StringBuilder();
                    sb.append(tLRPC$Document.dc_id);
                    sb.append("_");
                    j = tLRPC$Document.id;
                } else if (tLObject instanceof SecureDocument) {
                    SecureDocument secureDocument = (SecureDocument) tLObject;
                    sb = new StringBuilder();
                    sb.append(secureDocument.secureFile.dc_id);
                    sb.append("_");
                    j = secureDocument.secureFile.id;
                } else if (tLObject instanceof WebFile) {
                    str3 = Utilities.MD5(((WebFile) tLObject).url);
                }
                sb.append(j);
            }
            str3 = sb.toString();
        }
        if (str2 != null) {
            str3 = str3 + "@" + str2;
        }
        return getFromMemCache(str3);
    }

    public LruCache<BitmapDrawable> getLottieMemCahce() {
        return this.lottieMemCache;
    }

    public String getReplacedKey(String str) {
        if (str == null) {
            return null;
        }
        return this.replacedBitmaps.get(str);
    }

    public boolean hasLottieMemCache(String str) {
        LruCache<BitmapDrawable> lruCache = this.lottieMemCache;
        return lruCache != null && lruCache.contains(str);
    }

    public void incrementUseCount(String str) {
        Integer num = this.bitmapUseCounts.get(str);
        if (num == null) {
            this.bitmapUseCounts.put(str, 1);
        } else {
            this.bitmapUseCounts.put(str, Integer.valueOf(num.intValue() + 1));
        }
    }

    public boolean isInMemCache(String str, boolean z) {
        return z ? getFromLottieCache(str) != null : getFromMemCache(str) != null;
    }

    public boolean isLoadingHttpFile(String str) {
        return this.httpFileLoadTasksByKeys.containsKey(str);
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

    public void loadImageForImageReceiver(ImageReceiver imageReceiver) {
        loadImageForImageReceiver(imageReceiver, null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:105:0x01b0, code lost:
        if (r2 != null) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x0295, code lost:
        if (r0.local_id < 0) goto L144;
     */
    /* JADX WARN: Removed duplicated region for block: B:103:0x01a3  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x01b0  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x01bc  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x01c8  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x01d7  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x01da  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x01dd  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x01e1  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x01e4  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x01fb  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x03b5  */
    /* JADX WARN: Removed duplicated region for block: B:239:0x0414  */
    /* JADX WARN: Removed duplicated region for block: B:242:0x041b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:246:0x0436 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:250:0x0451 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:256:0x0472 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:261:0x0490 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:265:0x04aa  */
    /* JADX WARN: Removed duplicated region for block: B:273:0x04ea  */
    /* JADX WARN: Removed duplicated region for block: B:276:0x04f5  */
    /* JADX WARN: Removed duplicated region for block: B:290:0x0550  */
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
        TLRPC$Document qualityThumbDocument;
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
        ImageLoader imageLoader;
        ImageReceiver imageReceiver2;
        int i2;
        long size;
        int i3;
        int i4;
        String str15;
        String str16;
        String str17;
        ImageLocation imageLocation6;
        String str18;
        int i5;
        String str19;
        StringBuilder sb;
        int i6;
        String str20;
        ImageLocation imageLocation7;
        String str21;
        String str22;
        String str23;
        StringBuilder sb2;
        BitmapDrawable bitmapDrawable;
        Drawable findInPreloadImageReceivers;
        BitmapDrawable bitmapDrawable2;
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
            if ((findInPreloadImageReceivers2 instanceof RLottieDrawable ? ((RLottieDrawable) findInPreloadImageReceivers2).hasBitmap() : findInPreloadImageReceivers2 instanceof AnimatedFileDrawable ? ((AnimatedFileDrawable) findInPreloadImageReceivers2).hasBitmap() : true) && findInPreloadImageReceivers2 != null) {
                cancelLoadingForImageReceiver(imageReceiver, true);
                imageReceiver.setImageBitmapByKey(findInPreloadImageReceivers2, mediaKey, 3, true, newGuid);
                if (imageReceiver.isForcePreview()) {
                    z = true;
                    z2 = false;
                    imageKey = imageReceiver.getImageKey();
                    if (!z) {
                        ImageLocation imageLocation8 = imageReceiver.getImageLocation();
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
                    qualityThumbDocument = imageReceiver.getQualityThumbDocument();
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
                    String str24 = null;
                    if (imageLocation2 == null) {
                    }
                    ImageLocation imageLocation9 = imageLocation2;
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
                    ImageLocation imageLocation10 = imageLocation9;
                    imageLocation3 = mediaLocation;
                    str2 = null;
                    str3 = null;
                    String str25 = null;
                    String str26 = null;
                    i = 0;
                    boolean z6 = false;
                    while (i < 2) {
                    }
                    boolean z7 = z4;
                    str4 = thumbFilter;
                    imageLocation4 = imageLocation10;
                    str5 = imageFilter;
                    str6 = mediaFilter;
                    int i7 = newGuid;
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
                    String str27 = str7;
                    if (imageReceiver.getUniqKeyPrefix() != null) {
                    }
                    str11 = str3;
                    if (imageReceiver.getUniqKeyPrefix() != null) {
                    }
                    str12 = str2;
                    if (imageLocation4 == null) {
                    }
                    int cacheType = imageReceiver.getCacheType();
                    if (imageLocation3 != null) {
                    }
                    imageLoader.createLoadOperationForImageReceiver(imageReceiver2, str15, str16, str17, imageLocation6, str18, size, i5, i3, i4, i2);
                }
                return;
            } else if (findInPreloadImageReceivers2 != null) {
                imageReceiver.setImageBitmapByKey(findInPreloadImageReceivers2, mediaKey, 3, true, newGuid);
                z = false;
                z2 = true;
                imageKey = imageReceiver.getImageKey();
                if (!z && imageKey != null) {
                    ImageLocation imageLocation82 = imageReceiver.getImageLocation();
                    findInPreloadImageReceivers = findInPreloadImageReceivers(imageKey, list);
                    if (findInPreloadImageReceivers == null && useLottieMemCache(imageLocation82, imageKey)) {
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
                        if (imageReceiver.isForcePreview() || !(mediaKey == null || z2)) {
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
                                    qualityThumbDocument = imageReceiver.getQualityThumbDocument();
                                    thumbLocation = imageReceiver.getThumbLocation();
                                    String thumbFilter2 = imageReceiver.getThumbFilter();
                                    mediaLocation = imageReceiver.getMediaLocation();
                                    String mediaFilter2 = imageReceiver.getMediaFilter();
                                    imageLocation = imageReceiver.getImageLocation();
                                    String imageFilter2 = imageReceiver.getImageFilter();
                                    if (imageLocation == null && imageReceiver.isNeedsQualityThumb() && imageReceiver.isCurrentKeyQuality()) {
                                        if (!(parentObject instanceof MessageObject)) {
                                            qualityThumbDocument = ((MessageObject) parentObject).getDocument();
                                        }
                                        imageLocation2 = ImageLocation.getForDocument(qualityThumbDocument);
                                        z5 = true;
                                        str = "mp4";
                                        String str242 = null;
                                        String str28 = (imageLocation2 == null && imageLocation2.imageType == 2) ? "mp4" : null;
                                        ImageLocation imageLocation92 = imageLocation2;
                                        if (mediaLocation != null || mediaLocation.imageType != 2) {
                                            str = null;
                                        }
                                        ext = imageReceiver.getExt();
                                        if (ext == null) {
                                            ext = "jpg";
                                        }
                                        String str29 = str28 == null ? ext : str28;
                                        String str30 = str == null ? ext : str;
                                        ImageLocation imageLocation102 = imageLocation92;
                                        imageLocation3 = mediaLocation;
                                        str2 = null;
                                        str3 = null;
                                        String str252 = null;
                                        String str262 = null;
                                        i = 0;
                                        boolean z62 = false;
                                        while (i < 2) {
                                            if (i == 0) {
                                                imageLocation7 = imageLocation102;
                                                i6 = newGuid;
                                                str20 = str29;
                                            } else {
                                                i6 = newGuid;
                                                str20 = str30;
                                                imageLocation7 = imageLocation3;
                                            }
                                            boolean z8 = z4;
                                            ImageLocation imageLocation11 = imageLocation102;
                                            if (imageLocation7 != null) {
                                                if (imageLocation3 != null) {
                                                    imageLocation102 = imageLocation3;
                                                }
                                                String key = imageLocation7.getKey(parentObject, imageLocation102, false);
                                                if (key != null) {
                                                    str21 = thumbFilter2;
                                                    String key2 = imageLocation7.getKey(parentObject, imageLocation3 != null ? imageLocation3 : imageLocation11, true);
                                                    if (imageLocation7.path != null) {
                                                        key2 = key2 + "." + getHttpUrlExtension(imageLocation7.path, "jpg");
                                                        str22 = imageFilter2;
                                                        str23 = mediaFilter2;
                                                    } else {
                                                        TLRPC$PhotoSize tLRPC$PhotoSize = imageLocation7.photoSize;
                                                        str22 = imageFilter2;
                                                        if ((tLRPC$PhotoSize instanceof TLRPC$TL_photoStrippedSize) || (tLRPC$PhotoSize instanceof TLRPC$TL_photoPathSize)) {
                                                            str23 = mediaFilter2;
                                                            sb2 = new StringBuilder();
                                                        } else if (imageLocation7.location != null) {
                                                            key2 = key2 + "." + str20;
                                                            if (imageReceiver.getExt() == null) {
                                                                TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated = imageLocation7.location;
                                                                if (tLRPC$TL_fileLocationToBeDeprecated.key == null) {
                                                                    str23 = mediaFilter2;
                                                                    if (tLRPC$TL_fileLocationToBeDeprecated.volume_id == -2147483648L) {
                                                                    }
                                                                }
                                                            }
                                                            str23 = mediaFilter2;
                                                            z62 = true;
                                                        } else {
                                                            str23 = mediaFilter2;
                                                            WebFile webFile = imageLocation7.webFile;
                                                            if (webFile != null) {
                                                                String mimeTypePart = FileLoader.getMimeTypePart(webFile.mime_type);
                                                                key2 = key2 + "." + getHttpUrlExtension(imageLocation7.webFile.url, mimeTypePart);
                                                            } else if (imageLocation7.secureDocument != null) {
                                                                sb2 = new StringBuilder();
                                                            } else if (imageLocation7.document != null) {
                                                                if (i == 0 && z5) {
                                                                    key = "q_" + key;
                                                                }
                                                                String documentFileName = FileLoader.getDocumentFileName(imageLocation7.document);
                                                                int lastIndexOf = documentFileName.lastIndexOf(46);
                                                                String str31 = "";
                                                                String substring = lastIndexOf == -1 ? "" : documentFileName.substring(lastIndexOf);
                                                                if (substring.length() > 1) {
                                                                    str31 = substring;
                                                                } else if ("video/mp4".equals(imageLocation7.document.mime_type)) {
                                                                    str31 = ".mp4";
                                                                } else if ("video/x-matroska".equals(imageLocation7.document.mime_type)) {
                                                                    str31 = ".mkv";
                                                                }
                                                                key2 = key2 + str31;
                                                                if (MessageObject.isVideoDocument(imageLocation7.document) || MessageObject.isGifDocument(imageLocation7.document) || MessageObject.isRoundVideoDocument(imageLocation7.document) || MessageObject.canPreviewDocument(imageLocation7.document)) {
                                                                    z62 = false;
                                                                }
                                                                z62 = true;
                                                            } else if (parentObject instanceof TLRPC$StickerSet) {
                                                                sb2 = new StringBuilder();
                                                            }
                                                        }
                                                        sb2.append(key2);
                                                        sb2.append(".");
                                                        sb2.append(str20);
                                                        key2 = sb2.toString();
                                                    }
                                                    if (i == 0) {
                                                        str252 = key2;
                                                        str3 = key;
                                                    } else {
                                                        str262 = key2;
                                                        str2 = key;
                                                    }
                                                    if (imageLocation7 == thumbLocation) {
                                                        if (i == 0) {
                                                            str3 = null;
                                                            imageLocation102 = null;
                                                            str252 = null;
                                                            i++;
                                                            newGuid = i6;
                                                            z4 = z8;
                                                            thumbFilter2 = str21;
                                                            imageFilter2 = str22;
                                                            mediaFilter2 = str23;
                                                        } else {
                                                            str2 = null;
                                                            str262 = null;
                                                            imageLocation3 = null;
                                                        }
                                                    }
                                                    imageLocation102 = imageLocation11;
                                                    i++;
                                                    newGuid = i6;
                                                    z4 = z8;
                                                    thumbFilter2 = str21;
                                                    imageFilter2 = str22;
                                                    mediaFilter2 = str23;
                                                }
                                            }
                                            str21 = thumbFilter2;
                                            str22 = imageFilter2;
                                            str23 = mediaFilter2;
                                            imageLocation102 = imageLocation11;
                                            i++;
                                            newGuid = i6;
                                            z4 = z8;
                                            thumbFilter2 = str21;
                                            imageFilter2 = str22;
                                            mediaFilter2 = str23;
                                        }
                                        boolean z72 = z4;
                                        str4 = thumbFilter2;
                                        imageLocation4 = imageLocation102;
                                        str5 = imageFilter2;
                                        str6 = mediaFilter2;
                                        int i72 = newGuid;
                                        if (thumbLocation != null) {
                                            ImageLocation strippedLocation = imageReceiver.getStrippedLocation();
                                            if (strippedLocation == null) {
                                                strippedLocation = imageLocation3 != null ? imageLocation3 : imageLocation;
                                            }
                                            String key3 = thumbLocation.getKey(parentObject, strippedLocation, false);
                                            String key4 = thumbLocation.getKey(parentObject, strippedLocation, true);
                                            if (thumbLocation.path != null) {
                                                sb = new StringBuilder();
                                                sb.append(key4);
                                                sb.append(".");
                                                sb.append(getHttpUrlExtension(thumbLocation.path, "jpg"));
                                            } else {
                                                TLRPC$PhotoSize tLRPC$PhotoSize2 = thumbLocation.photoSize;
                                                if ((tLRPC$PhotoSize2 instanceof TLRPC$TL_photoStrippedSize) || (tLRPC$PhotoSize2 instanceof TLRPC$TL_photoPathSize)) {
                                                    sb = new StringBuilder();
                                                } else {
                                                    if (thumbLocation.location != null) {
                                                        sb = new StringBuilder();
                                                    }
                                                    str242 = key4;
                                                    str7 = key3;
                                                }
                                                sb.append(key4);
                                                sb.append(".");
                                                sb.append(ext);
                                            }
                                            key4 = sb.toString();
                                            str242 = key4;
                                            str7 = key3;
                                        } else {
                                            str7 = null;
                                        }
                                        if (str2 != null || str6 == null) {
                                            str8 = str6;
                                        } else {
                                            StringBuilder sb3 = new StringBuilder();
                                            sb3.append(str2);
                                            sb3.append("@");
                                            str8 = str6;
                                            sb3.append(str8);
                                            str2 = sb3.toString();
                                        }
                                        if (str3 != null || str5 == null) {
                                            str9 = str5;
                                        } else {
                                            StringBuilder sb4 = new StringBuilder();
                                            sb4.append(str3);
                                            sb4.append("@");
                                            str9 = str5;
                                            sb4.append(str9);
                                            str3 = sb4.toString();
                                        }
                                        if (str7 != null || str4 == null) {
                                            str10 = str4;
                                        } else {
                                            StringBuilder sb5 = new StringBuilder();
                                            sb5.append(str7);
                                            sb5.append("@");
                                            str10 = str4;
                                            sb5.append(str10);
                                            str7 = sb5.toString();
                                        }
                                        String str272 = str7;
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
                                            imageLoader = this;
                                            imageReceiver2 = imageReceiver;
                                            String str32 = str9;
                                            i2 = i72;
                                            imageLoader.createLoadOperationForImageReceiver(imageReceiver2, str272, str242, ext, thumbLocation, str10, 0L, 1, 1, z72 ? 2 : 1, i2);
                                            size = imageReceiver.getSize();
                                            i3 = 0;
                                            i4 = 0;
                                            i5 = 1;
                                            str15 = str11;
                                            str16 = str252;
                                            str17 = str29;
                                            imageLocation6 = imageLocation4;
                                            str18 = str32;
                                            imageLoader.createLoadOperationForImageReceiver(imageReceiver2, str15, str16, str17, imageLocation6, str18, size, i5, i3, i4, i2);
                                        } else {
                                            str13 = ext;
                                            imageLocation5 = imageLocation4;
                                            str14 = str9;
                                        }
                                        int cacheType2 = imageReceiver.getCacheType();
                                        if (imageLocation3 != null) {
                                            int i8 = (cacheType2 == 0 && z62) ? 1 : cacheType2;
                                            int i9 = i8 == 0 ? 1 : i8;
                                            if (z72) {
                                                str19 = str8;
                                            } else {
                                                str19 = str8;
                                                createLoadOperationForImageReceiver(imageReceiver, str272, str242, str13, thumbLocation, str10, 0L, i9, 1, 1, i72);
                                            }
                                            if (!z3) {
                                                createLoadOperationForImageReceiver(imageReceiver, str11, str252, str29, imageLocation5, str14, 0L, 1, 0, 0, i72);
                                            }
                                            if (z2) {
                                                return;
                                            }
                                            size = imageReceiver.getSize();
                                            i3 = 3;
                                            i4 = 0;
                                            imageLoader = this;
                                            imageReceiver2 = imageReceiver;
                                            str15 = str12;
                                            str16 = str262;
                                            str17 = str30;
                                            imageLocation6 = imageLocation3;
                                            str18 = str19;
                                            i5 = i8;
                                            i2 = i72;
                                        } else {
                                            int i10 = (cacheType2 == 0 && z62) ? 1 : cacheType2;
                                            imageLoader = this;
                                            imageReceiver2 = imageReceiver;
                                            i2 = i72;
                                            imageLoader.createLoadOperationForImageReceiver(imageReceiver2, str272, str242, str13, thumbLocation, str10, 0L, i10 == 0 ? 1 : i10, 1, z72 ? 2 : 1, i2);
                                            size = imageReceiver.getSize();
                                            i3 = 0;
                                            i4 = 0;
                                            str15 = str11;
                                            str16 = str252;
                                            str17 = str29;
                                            imageLocation6 = imageLocation5;
                                            str18 = str14;
                                            i5 = i10;
                                        }
                                        imageLoader.createLoadOperationForImageReceiver(imageReceiver2, str15, str16, str17, imageLocation6, str18, size, i5, i3, i4, i2);
                                    }
                                    imageLocation2 = imageLocation;
                                    z5 = false;
                                    str = "mp4";
                                    String str2422 = null;
                                    if (imageLocation2 == null) {
                                    }
                                    ImageLocation imageLocation922 = imageLocation2;
                                    if (mediaLocation != null) {
                                    }
                                    str = null;
                                    ext = imageReceiver.getExt();
                                    if (ext == null) {
                                    }
                                    if (str28 == null) {
                                    }
                                    if (str == null) {
                                    }
                                    ImageLocation imageLocation1022 = imageLocation922;
                                    imageLocation3 = mediaLocation;
                                    str2 = null;
                                    str3 = null;
                                    String str2522 = null;
                                    String str2622 = null;
                                    i = 0;
                                    boolean z622 = false;
                                    while (i < 2) {
                                    }
                                    boolean z722 = z4;
                                    str4 = thumbFilter2;
                                    imageLocation4 = imageLocation1022;
                                    str5 = imageFilter2;
                                    str6 = mediaFilter2;
                                    int i722 = newGuid;
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
                                    String str2722 = str7;
                                    if (imageReceiver.getUniqKeyPrefix() != null) {
                                    }
                                    str11 = str3;
                                    if (imageReceiver.getUniqKeyPrefix() != null) {
                                    }
                                    str12 = str2;
                                    if (imageLocation4 == null) {
                                    }
                                    int cacheType22 = imageReceiver.getCacheType();
                                    if (imageLocation3 != null) {
                                    }
                                    imageLoader.createLoadOperationForImageReceiver(imageReceiver2, str15, str16, str17, imageLocation6, str18, size, i5, i3, i4, i2);
                                }
                            }
                            z4 = false;
                            parentObject = imageReceiver.getParentObject();
                            qualityThumbDocument = imageReceiver.getQualityThumbDocument();
                            thumbLocation = imageReceiver.getThumbLocation();
                            String thumbFilter22 = imageReceiver.getThumbFilter();
                            mediaLocation = imageReceiver.getMediaLocation();
                            String mediaFilter22 = imageReceiver.getMediaFilter();
                            imageLocation = imageReceiver.getImageLocation();
                            String imageFilter22 = imageReceiver.getImageFilter();
                            if (imageLocation == null) {
                                if (!(parentObject instanceof MessageObject)) {
                                }
                                imageLocation2 = ImageLocation.getForDocument(qualityThumbDocument);
                                z5 = true;
                                str = "mp4";
                                String str24222 = null;
                                if (imageLocation2 == null) {
                                }
                                ImageLocation imageLocation9222 = imageLocation2;
                                if (mediaLocation != null) {
                                }
                                str = null;
                                ext = imageReceiver.getExt();
                                if (ext == null) {
                                }
                                if (str28 == null) {
                                }
                                if (str == null) {
                                }
                                ImageLocation imageLocation10222 = imageLocation9222;
                                imageLocation3 = mediaLocation;
                                str2 = null;
                                str3 = null;
                                String str25222 = null;
                                String str26222 = null;
                                i = 0;
                                boolean z6222 = false;
                                while (i < 2) {
                                }
                                boolean z7222 = z4;
                                str4 = thumbFilter22;
                                imageLocation4 = imageLocation10222;
                                str5 = imageFilter22;
                                str6 = mediaFilter22;
                                int i7222 = newGuid;
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
                                String str27222 = str7;
                                if (imageReceiver.getUniqKeyPrefix() != null) {
                                }
                                str11 = str3;
                                if (imageReceiver.getUniqKeyPrefix() != null) {
                                }
                                str12 = str2;
                                if (imageLocation4 == null) {
                                }
                                int cacheType222 = imageReceiver.getCacheType();
                                if (imageLocation3 != null) {
                                }
                                imageLoader.createLoadOperationForImageReceiver(imageReceiver2, str15, str16, str17, imageLocation6, str18, size, i5, i3, i4, i2);
                            }
                            imageLocation2 = imageLocation;
                            z5 = false;
                            str = "mp4";
                            String str242222 = null;
                            if (imageLocation2 == null) {
                            }
                            ImageLocation imageLocation92222 = imageLocation2;
                            if (mediaLocation != null) {
                            }
                            str = null;
                            ext = imageReceiver.getExt();
                            if (ext == null) {
                            }
                            if (str28 == null) {
                            }
                            if (str == null) {
                            }
                            ImageLocation imageLocation102222 = imageLocation92222;
                            imageLocation3 = mediaLocation;
                            str2 = null;
                            str3 = null;
                            String str252222 = null;
                            String str262222 = null;
                            i = 0;
                            boolean z62222 = false;
                            while (i < 2) {
                            }
                            boolean z72222 = z4;
                            str4 = thumbFilter22;
                            imageLocation4 = imageLocation102222;
                            str5 = imageFilter22;
                            str6 = mediaFilter22;
                            int i72222 = newGuid;
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
                            String str272222 = str7;
                            if (imageReceiver.getUniqKeyPrefix() != null) {
                            }
                            str11 = str3;
                            if (imageReceiver.getUniqKeyPrefix() != null) {
                            }
                            str12 = str2;
                            if (imageLocation4 == null) {
                            }
                            int cacheType2222 = imageReceiver.getCacheType();
                            if (imageLocation3 != null) {
                            }
                            imageLoader.createLoadOperationForImageReceiver(imageReceiver2, str15, str16, str17, imageLocation6, str18, size, i5, i3, i4, i2);
                        }
                        return;
                    }
                }
                z3 = z;
                thumbKey = imageReceiver.getThumbKey();
                if (thumbKey != null) {
                }
                z4 = false;
                parentObject = imageReceiver.getParentObject();
                qualityThumbDocument = imageReceiver.getQualityThumbDocument();
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
                String str2422222 = null;
                if (imageLocation2 == null) {
                }
                ImageLocation imageLocation922222 = imageLocation2;
                if (mediaLocation != null) {
                }
                str = null;
                ext = imageReceiver.getExt();
                if (ext == null) {
                }
                if (str28 == null) {
                }
                if (str == null) {
                }
                ImageLocation imageLocation1022222 = imageLocation922222;
                imageLocation3 = mediaLocation;
                str2 = null;
                str3 = null;
                String str2522222 = null;
                String str2622222 = null;
                i = 0;
                boolean z622222 = false;
                while (i < 2) {
                }
                boolean z722222 = z4;
                str4 = thumbFilter222;
                imageLocation4 = imageLocation1022222;
                str5 = imageFilter222;
                str6 = mediaFilter222;
                int i722222 = newGuid;
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
                String str2722222 = str7;
                if (imageReceiver.getUniqKeyPrefix() != null) {
                }
                str11 = str3;
                if (imageReceiver.getUniqKeyPrefix() != null) {
                }
                str12 = str2;
                if (imageLocation4 == null) {
                }
                int cacheType22222 = imageReceiver.getCacheType();
                if (imageLocation3 != null) {
                }
                imageLoader.createLoadOperationForImageReceiver(imageReceiver2, str15, str16, str17, imageLocation6, str18, size, i5, i3, i4, i2);
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
        qualityThumbDocument = imageReceiver.getQualityThumbDocument();
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
        String str24222222 = null;
        if (imageLocation2 == null) {
        }
        ImageLocation imageLocation9222222 = imageLocation2;
        if (mediaLocation != null) {
        }
        str = null;
        ext = imageReceiver.getExt();
        if (ext == null) {
        }
        if (str28 == null) {
        }
        if (str == null) {
        }
        ImageLocation imageLocation10222222 = imageLocation9222222;
        imageLocation3 = mediaLocation;
        str2 = null;
        str3 = null;
        String str25222222 = null;
        String str26222222 = null;
        i = 0;
        boolean z6222222 = false;
        while (i < 2) {
        }
        boolean z7222222 = z4;
        str4 = thumbFilter2222;
        imageLocation4 = imageLocation10222222;
        str5 = imageFilter2222;
        str6 = mediaFilter2222;
        int i7222222 = newGuid;
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
        String str27222222 = str7;
        if (imageReceiver.getUniqKeyPrefix() != null) {
        }
        str11 = str3;
        if (imageReceiver.getUniqKeyPrefix() != null) {
        }
        str12 = str2;
        if (imageLocation4 == null) {
        }
        int cacheType222222 = imageReceiver.getCacheType();
        if (imageLocation3 != null) {
        }
        imageLoader.createLoadOperationForImageReceiver(imageReceiver2, str15, str16, str17, imageLocation6, str18, size, i5, i3, i4, i2);
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

    public void onFragmentStackChanged() {
        for (int i = 0; i < this.cachedAnimatedFileDrawables.size(); i++) {
            this.cachedAnimatedFileDrawables.get(i).repeatCount = 0;
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

    public void putImageToCache(BitmapDrawable bitmapDrawable, String str, boolean z) {
        (z ? this.smallImagesMemCache : this.memCache).put(str, bitmapDrawable);
    }

    public void putThumbsToCache(ArrayList<MessageThumb> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            putImageToCache(arrayList.get(i).drawable, arrayList.get(i).key, true);
        }
    }

    public void removeImage(String str) {
        this.bitmapUseCounts.remove(str);
        this.memCache.remove(str);
        this.smallImagesMemCache.remove(str);
    }

    public void removeTestWebFile(String str) {
        if (str == null) {
            return;
        }
        this.testWebFile.remove(str);
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
}
