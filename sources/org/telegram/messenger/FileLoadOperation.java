package org.telegram.messenger;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;
import org.telegram.messenger.FileLoadOperation;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.FilePathDatabase;
import org.telegram.messenger.utils.ImmutableByteArrayOutputStream;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.Vector;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.Storage.CacheModel;

/* loaded from: classes3.dex */
public class FileLoadOperation {
    private static final int FINISH_CODE_DEFAULT = 0;
    private static final int FINISH_CODE_FILE_ALREADY_EXIST = 1;
    public static ImmutableByteArrayOutputStream filesQueueByteBuffer = null;
    private static int globalRequestPointer = 0;
    private static final int preloadMaxBytes = 2097152;
    private static final int stateCanceled = 4;
    private static final int stateCancelling = 5;
    private static final int stateDownloading = 1;
    private static final int stateFailed = 2;
    private static final int stateFinished = 3;
    private static final int stateIdle = 0;
    private final boolean FULL_LOGS;
    private boolean allowDisordererFileSave;
    private int bigFileSizeFrom;
    private long bytesCountPadding;
    private File cacheFileFinal;
    private boolean cacheFileFinalReady;
    private File cacheFileGzipTemp;
    private File cacheFileParts;
    private File cacheFilePreload;
    private File cacheFileTemp;
    private File cacheIvTemp;
    private final Runnable cancelAfterNoStreamListeners;
    private ArrayList<RequestInfo> cancelledRequestInfos;
    public volatile boolean caughtPremiumFloodWait;
    private byte[] cdnCheckBytes;
    private int cdnChunkCheckSize;
    private int cdnDatacenterId;
    private HashMap<Long, TLRPC.TL_fileHash> cdnHashes;
    private byte[] cdnIv;
    private byte[] cdnKey;
    private byte[] cdnToken;
    private volatile boolean closeFilePartsStreamOnWriteEnd;
    public int currentAccount;
    private int currentDownloadChunkSize;
    private int currentMaxDownloadRequests;
    private int currentType;
    private int datacenterId;
    private ArrayList<RequestInfo> delayedRequestInfos;
    private FileLoadOperationDelegate delegate;
    private long documentId;
    private int downloadChunkSize;
    private int downloadChunkSizeAnimation;
    private int downloadChunkSizeBig;
    private long downloadedBytes;
    private boolean encryptFile;
    private byte[] encryptIv;
    private byte[] encryptKey;
    private String ext;
    private FilePathDatabase.FileMeta fileMetadata;
    private String fileName;
    private RandomAccessFile fileOutputStream;
    private RandomAccessFile filePartsStream;
    private RandomAccessFile fileReadStream;
    private Runnable fileWriteRunnable;
    private RandomAccessFile fiv;
    private boolean forceSmallChunk;
    private long foundMoovSize;
    private int initialDatacenterId;
    private boolean isCdn;
    private boolean isForceRequest;
    private boolean isPreloadVideoOperation;
    public boolean isStory;
    private boolean isStream;
    private byte[] iv;
    private byte[] key;
    protected long lastProgressUpdateTime;
    protected TLRPC.InputFileLocation location;
    private int maxCdnParts;
    private int maxDownloadRequests;
    private int maxDownloadRequestsAnimation;
    private int maxDownloadRequestsBig;
    private int moovFound;
    private long nextAtomOffset;
    private boolean nextPartWasPreloaded;
    private long nextPreloadDownloadOffset;
    private ArrayList<Range> notCheckedCdnRanges;
    private ArrayList<Range> notLoadedBytesRanges;
    private volatile ArrayList<Range> notLoadedBytesRangesCopy;
    private ArrayList<Range> notRequestedBytesRanges;
    public Object parentObject;
    public FilePathDatabase.PathData pathSaveData;
    private volatile boolean paused;
    public boolean preFinished;
    private boolean preloadFinished;
    private long preloadNotRequestedBytesCount;
    private int preloadPrefixSize;
    private RandomAccessFile preloadStream;
    private int preloadStreamFileOffset;
    private byte[] preloadTempBuffer;
    private int preloadTempBufferCount;
    private HashMap<Long, PreloadRange> preloadedBytesRanges;
    private int priority;
    private FileLoaderPriorityQueue priorityQueue;
    private RequestInfo priorityRequestInfo;
    private int renameRetryCount;
    public ArrayList<RequestInfo> requestInfos;
    private long requestedBytesCount;
    private HashMap<Long, Integer> requestedPreloadedBytesRanges;
    private boolean requestingCdnOffsets;
    protected boolean requestingReference;
    private int requestsCount;
    private boolean reuploadingCdn;
    private long startTime;
    private boolean started;
    private volatile int state;
    private String storeFileName;
    private File storePath;
    FileLoadOperationStream stream;
    private ArrayList<FileLoadOperationStream> streamListeners;
    long streamOffset;
    boolean streamPriority;
    private long streamPriorityStartOffset;
    private long streamStartOffset;
    private boolean supportsPreloading;
    private File tempPath;
    public long totalBytesCount;
    private int totalPreloadedBytes;
    long totalTime;
    public final ArrayList<Integer> uiRequestTokens;
    private boolean ungzip;
    private WebFile webFile;
    private TLRPC.InputWebFileLocation webLocation;
    private volatile boolean writingToFilePartsStream;
    public static volatile DispatchQueue filesQueue = new DispatchQueue("writeFileQueue");
    private static final Object lockObject = new Object();

    public interface FileLoadOperationDelegate {
        void didChangedLoadProgress(FileLoadOperation fileLoadOperation, long j, long j2);

        void didFailedLoadingFile(FileLoadOperation fileLoadOperation, int i);

        void didFinishLoadingFile(FileLoadOperation fileLoadOperation, File file);

        void didPreFinishLoading(FileLoadOperation fileLoadOperation, File file);

        boolean hasAnotherRefOnFile(String str);

        boolean isLocallyCreatedFile(String str);

        void saveFilePath(FilePathDatabase.PathData pathData, File file);
    }

    private static class PreloadRange {
        private long fileOffset;
        private long length;

        private PreloadRange(long j, long j2) {
            this.fileOffset = j;
            this.length = j2;
        }
    }

    public static class Range {
        private long end;
        private long start;

        private Range(long j, long j2) {
            this.start = j;
            this.end = j2;
        }

        public String toString() {
            return "Range{start=" + this.start + ", end=" + this.end + '}';
        }
    }

    protected static class RequestInfo {
        public boolean cancelled;
        public boolean cancelling;
        public int chunkSize;
        public int connectionType;
        private boolean forceSmallChunk;
        private long offset;
        public long requestStartTime;
        public int requestToken;
        private TLRPC.TL_upload_file response;
        private TLRPC.TL_upload_cdnFile responseCdn;
        private TLRPC.TL_upload_webFile responseWeb;
        public Runnable whenCancelled;

        protected RequestInfo() {
        }
    }

    public FileLoadOperation(int i, WebFile webFile) {
        this.FULL_LOGS = false;
        this.downloadChunkSize = 32768;
        this.downloadChunkSizeBig = 131072;
        this.cdnChunkCheckSize = 131072;
        this.maxDownloadRequests = 4;
        this.maxDownloadRequestsBig = 4;
        this.bigFileSizeFrom = 10485760;
        this.maxCdnParts = (int) (FileLoader.DEFAULT_MAX_FILE_SIZE / 131072);
        this.downloadChunkSizeAnimation = 131072;
        this.maxDownloadRequestsAnimation = 4;
        this.preloadTempBuffer = new byte[24];
        this.state = 0;
        this.uiRequestTokens = new ArrayList<>();
        this.cancelAfterNoStreamListeners = new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$new$6();
            }
        };
        updateParams();
        this.currentAccount = i;
        this.webFile = webFile;
        this.webLocation = webFile.location;
        this.totalBytesCount = webFile.size;
        int i2 = MessagesController.getInstance(i).webFileDatacenterId;
        this.datacenterId = i2;
        this.initialDatacenterId = i2;
        String mimeTypePart = FileLoader.getMimeTypePart(webFile.mime_type);
        this.currentType = webFile.mime_type.startsWith("image/") ? ConnectionsManager.FileTypePhoto : webFile.mime_type.equals("audio/ogg") ? ConnectionsManager.FileTypeAudio : webFile.mime_type.startsWith("video/") ? ConnectionsManager.FileTypeVideo : ConnectionsManager.FileTypeFile;
        this.allowDisordererFileSave = true;
        this.ext = ImageLoader.getHttpUrlExtension(webFile.url, mimeTypePart);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public FileLoadOperation(ImageLocation imageLocation, Object obj, String str, long j) {
        TLRPC.TL_inputStickerSetThumb tL_inputStickerSetThumb;
        this.FULL_LOGS = false;
        this.downloadChunkSize = 32768;
        this.downloadChunkSizeBig = 131072;
        this.cdnChunkCheckSize = 131072;
        this.maxDownloadRequests = 4;
        this.maxDownloadRequestsBig = 4;
        this.bigFileSizeFrom = 10485760;
        this.maxCdnParts = (int) (FileLoader.DEFAULT_MAX_FILE_SIZE / 131072);
        this.downloadChunkSizeAnimation = 131072;
        this.maxDownloadRequestsAnimation = 4;
        this.preloadTempBuffer = new byte[24];
        this.state = 0;
        this.uiRequestTokens = new ArrayList<>();
        this.cancelAfterNoStreamListeners = new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$new$6();
            }
        };
        updateParams();
        this.parentObject = obj;
        this.isStory = obj instanceof TL_stories.TL_storyItem;
        this.fileMetadata = FileLoader.getFileMetadataFromParent(this.currentAccount, obj);
        this.isStream = imageLocation.imageType == 2;
        if (imageLocation.isEncrypted()) {
            TLRPC.TL_inputEncryptedFileLocation tL_inputEncryptedFileLocation = new TLRPC.TL_inputEncryptedFileLocation();
            this.location = tL_inputEncryptedFileLocation;
            TLRPC.TL_fileLocationToBeDeprecated tL_fileLocationToBeDeprecated = imageLocation.location;
            long j2 = tL_fileLocationToBeDeprecated.volume_id;
            tL_inputEncryptedFileLocation.id = j2;
            tL_inputEncryptedFileLocation.volume_id = j2;
            tL_inputEncryptedFileLocation.local_id = tL_fileLocationToBeDeprecated.local_id;
            tL_inputEncryptedFileLocation.access_hash = imageLocation.access_hash;
            byte[] bArr = new byte[32];
            this.iv = bArr;
            System.arraycopy(imageLocation.iv, 0, bArr, 0, 32);
            this.key = imageLocation.key;
        } else {
            if (imageLocation.photoPeer != null) {
                TLRPC.TL_inputPeerPhotoFileLocation tL_inputPeerPhotoFileLocation = new TLRPC.TL_inputPeerPhotoFileLocation();
                TLRPC.TL_fileLocationToBeDeprecated tL_fileLocationToBeDeprecated2 = imageLocation.location;
                long j3 = tL_fileLocationToBeDeprecated2.volume_id;
                tL_inputPeerPhotoFileLocation.id = j3;
                tL_inputPeerPhotoFileLocation.volume_id = j3;
                tL_inputPeerPhotoFileLocation.local_id = tL_fileLocationToBeDeprecated2.local_id;
                tL_inputPeerPhotoFileLocation.photo_id = imageLocation.photoId;
                tL_inputPeerPhotoFileLocation.big = imageLocation.photoPeerType == 0;
                tL_inputPeerPhotoFileLocation.peer = imageLocation.photoPeer;
                tL_inputStickerSetThumb = tL_inputPeerPhotoFileLocation;
            } else if (imageLocation.stickerSet != null) {
                TLRPC.TL_inputStickerSetThumb tL_inputStickerSetThumb2 = new TLRPC.TL_inputStickerSetThumb();
                TLRPC.TL_fileLocationToBeDeprecated tL_fileLocationToBeDeprecated3 = imageLocation.location;
                long j4 = tL_fileLocationToBeDeprecated3.volume_id;
                tL_inputStickerSetThumb2.id = j4;
                tL_inputStickerSetThumb2.volume_id = j4;
                tL_inputStickerSetThumb2.local_id = tL_fileLocationToBeDeprecated3.local_id;
                tL_inputStickerSetThumb2.thumb_version = imageLocation.thumbVersion;
                tL_inputStickerSetThumb2.stickerset = imageLocation.stickerSet;
                tL_inputStickerSetThumb = tL_inputStickerSetThumb2;
            } else if (imageLocation.thumbSize != null) {
                if (imageLocation.photoId != 0) {
                    TLRPC.TL_inputPhotoFileLocation tL_inputPhotoFileLocation = new TLRPC.TL_inputPhotoFileLocation();
                    this.location = tL_inputPhotoFileLocation;
                    tL_inputPhotoFileLocation.id = imageLocation.photoId;
                    TLRPC.TL_fileLocationToBeDeprecated tL_fileLocationToBeDeprecated4 = imageLocation.location;
                    tL_inputPhotoFileLocation.volume_id = tL_fileLocationToBeDeprecated4.volume_id;
                    tL_inputPhotoFileLocation.local_id = tL_fileLocationToBeDeprecated4.local_id;
                    tL_inputPhotoFileLocation.access_hash = imageLocation.access_hash;
                    tL_inputPhotoFileLocation.file_reference = imageLocation.file_reference;
                    tL_inputPhotoFileLocation.thumb_size = imageLocation.thumbSize;
                    if (imageLocation.imageType == 2) {
                        this.allowDisordererFileSave = true;
                    }
                } else {
                    TLRPC.TL_inputDocumentFileLocation tL_inputDocumentFileLocation = new TLRPC.TL_inputDocumentFileLocation();
                    this.location = tL_inputDocumentFileLocation;
                    long j5 = imageLocation.documentId;
                    tL_inputDocumentFileLocation.id = j5;
                    this.documentId = j5;
                    TLRPC.TL_fileLocationToBeDeprecated tL_fileLocationToBeDeprecated5 = imageLocation.location;
                    tL_inputDocumentFileLocation.volume_id = tL_fileLocationToBeDeprecated5.volume_id;
                    tL_inputDocumentFileLocation.local_id = tL_fileLocationToBeDeprecated5.local_id;
                    tL_inputDocumentFileLocation.access_hash = imageLocation.access_hash;
                    tL_inputDocumentFileLocation.file_reference = imageLocation.file_reference;
                    tL_inputDocumentFileLocation.thumb_size = imageLocation.thumbSize;
                }
                TLRPC.InputFileLocation inputFileLocation = this.location;
                if (inputFileLocation.file_reference == null) {
                    inputFileLocation.file_reference = new byte[0];
                }
            } else {
                TLRPC.TL_inputFileLocation tL_inputFileLocation = new TLRPC.TL_inputFileLocation();
                this.location = tL_inputFileLocation;
                TLRPC.TL_fileLocationToBeDeprecated tL_fileLocationToBeDeprecated6 = imageLocation.location;
                tL_inputFileLocation.volume_id = tL_fileLocationToBeDeprecated6.volume_id;
                tL_inputFileLocation.local_id = tL_fileLocationToBeDeprecated6.local_id;
                tL_inputFileLocation.secret = imageLocation.access_hash;
                byte[] bArr2 = imageLocation.file_reference;
                tL_inputFileLocation.file_reference = bArr2;
                if (bArr2 == null) {
                    tL_inputFileLocation.file_reference = new byte[0];
                }
                this.allowDisordererFileSave = true;
            }
            this.location = tL_inputStickerSetThumb;
        }
        int i = imageLocation.imageType;
        this.ungzip = i == 1 || i == 3;
        int i2 = imageLocation.dc_id;
        this.datacenterId = i2;
        this.initialDatacenterId = i2;
        this.currentType = ConnectionsManager.FileTypePhoto;
        this.totalBytesCount = j;
        this.ext = str == null ? "jpg" : str;
    }

    public FileLoadOperation(SecureDocument secureDocument) {
        this.FULL_LOGS = false;
        this.downloadChunkSize = 32768;
        this.downloadChunkSizeBig = 131072;
        this.cdnChunkCheckSize = 131072;
        this.maxDownloadRequests = 4;
        this.maxDownloadRequestsBig = 4;
        this.bigFileSizeFrom = 10485760;
        this.maxCdnParts = (int) (FileLoader.DEFAULT_MAX_FILE_SIZE / 131072);
        this.downloadChunkSizeAnimation = 131072;
        this.maxDownloadRequestsAnimation = 4;
        this.preloadTempBuffer = new byte[24];
        this.state = 0;
        this.uiRequestTokens = new ArrayList<>();
        this.cancelAfterNoStreamListeners = new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$new$6();
            }
        };
        updateParams();
        TLRPC.TL_inputSecureFileLocation tL_inputSecureFileLocation = new TLRPC.TL_inputSecureFileLocation();
        this.location = tL_inputSecureFileLocation;
        TLRPC.TL_secureFile tL_secureFile = secureDocument.secureFile;
        tL_inputSecureFileLocation.id = tL_secureFile.id;
        tL_inputSecureFileLocation.access_hash = tL_secureFile.access_hash;
        this.datacenterId = tL_secureFile.dc_id;
        this.totalBytesCount = tL_secureFile.size;
        this.allowDisordererFileSave = true;
        this.currentType = ConnectionsManager.FileTypeFile;
        this.ext = ".jpg";
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x0122 A[Catch: Exception -> 0x0079, TryCatch #0 {Exception -> 0x0079, blocks: (B:3:0x0040, B:6:0x0054, B:7:0x00ca, B:9:0x00d4, B:13:0x00e2, B:15:0x00ec, B:17:0x00f6, B:18:0x00fe, B:20:0x0106, B:23:0x0110, B:24:0x0116, B:26:0x0122, B:27:0x0138, B:29:0x0140, B:34:0x0127, B:36:0x012f, B:37:0x0134, B:39:0x007c, B:41:0x0080, B:43:0x0099, B:44:0x009d, B:46:0x00ae, B:50:0x00b8, B:48:0x00c7), top: B:2:0x0040 }] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0140 A[Catch: Exception -> 0x0079, TRY_LEAVE, TryCatch #0 {Exception -> 0x0079, blocks: (B:3:0x0040, B:6:0x0054, B:7:0x00ca, B:9:0x00d4, B:13:0x00e2, B:15:0x00ec, B:17:0x00f6, B:18:0x00fe, B:20:0x0106, B:23:0x0110, B:24:0x0116, B:26:0x0122, B:27:0x0138, B:29:0x0140, B:34:0x0127, B:36:0x012f, B:37:0x0134, B:39:0x007c, B:41:0x0080, B:43:0x0099, B:44:0x009d, B:46:0x00ae, B:50:0x00b8, B:48:0x00c7), top: B:2:0x0040 }] */
    /* JADX WARN: Removed duplicated region for block: B:33:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0127 A[Catch: Exception -> 0x0079, TryCatch #0 {Exception -> 0x0079, blocks: (B:3:0x0040, B:6:0x0054, B:7:0x00ca, B:9:0x00d4, B:13:0x00e2, B:15:0x00ec, B:17:0x00f6, B:18:0x00fe, B:20:0x0106, B:23:0x0110, B:24:0x0116, B:26:0x0122, B:27:0x0138, B:29:0x0140, B:34:0x0127, B:36:0x012f, B:37:0x0134, B:39:0x007c, B:41:0x0080, B:43:0x0099, B:44:0x009d, B:46:0x00ae, B:50:0x00b8, B:48:0x00c7), top: B:2:0x0040 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public FileLoadOperation(TLRPC.Document document, Object obj) {
        boolean z;
        long j;
        String documentFileName;
        int lastIndexOf;
        this.FULL_LOGS = false;
        this.downloadChunkSize = 32768;
        this.downloadChunkSizeBig = 131072;
        this.cdnChunkCheckSize = 131072;
        this.maxDownloadRequests = 4;
        this.maxDownloadRequestsBig = 4;
        this.bigFileSizeFrom = 10485760;
        this.maxCdnParts = (int) (FileLoader.DEFAULT_MAX_FILE_SIZE / 131072);
        this.downloadChunkSizeAnimation = 131072;
        this.maxDownloadRequestsAnimation = 4;
        this.preloadTempBuffer = new byte[24];
        this.state = 0;
        this.uiRequestTokens = new ArrayList<>();
        this.cancelAfterNoStreamListeners = new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$new$6();
            }
        };
        updateParams();
        try {
            this.parentObject = obj;
            this.isStory = obj instanceof TL_stories.TL_storyItem;
            this.fileMetadata = FileLoader.getFileMetadataFromParent(this.currentAccount, obj);
            String str = "";
            if (document instanceof TLRPC.TL_documentEncrypted) {
                TLRPC.TL_inputEncryptedFileLocation tL_inputEncryptedFileLocation = new TLRPC.TL_inputEncryptedFileLocation();
                this.location = tL_inputEncryptedFileLocation;
                tL_inputEncryptedFileLocation.id = document.id;
                tL_inputEncryptedFileLocation.access_hash = document.access_hash;
                int i = document.dc_id;
                this.datacenterId = i;
                this.initialDatacenterId = i;
                byte[] bArr = new byte[32];
                this.iv = bArr;
                System.arraycopy(document.iv, 0, bArr, 0, 32);
                this.key = document.key;
            } else if (document instanceof TLRPC.TL_document) {
                TLRPC.TL_inputDocumentFileLocation tL_inputDocumentFileLocation = new TLRPC.TL_inputDocumentFileLocation();
                this.location = tL_inputDocumentFileLocation;
                long j2 = document.id;
                tL_inputDocumentFileLocation.id = j2;
                this.documentId = j2;
                tL_inputDocumentFileLocation.access_hash = document.access_hash;
                byte[] bArr2 = document.file_reference;
                tL_inputDocumentFileLocation.file_reference = bArr2;
                tL_inputDocumentFileLocation.thumb_size = "";
                if (bArr2 == null) {
                    tL_inputDocumentFileLocation.file_reference = new byte[0];
                }
                int i2 = document.dc_id;
                this.datacenterId = i2;
                this.initialDatacenterId = i2;
                this.allowDisordererFileSave = true;
                int size = document.attributes.size();
                int i3 = 0;
                while (true) {
                    if (i3 >= size) {
                        break;
                    }
                    if (document.attributes.get(i3) instanceof TLRPC.TL_documentAttributeVideo) {
                        this.supportsPreloading = true;
                        this.preloadPrefixSize = document.attributes.get(i3).preload_prefix_size;
                        break;
                    }
                    i3++;
                }
            }
            if (!"application/x-tgsticker".equals(document.mime_type) && !"application/x-tgwallpattern".equals(document.mime_type)) {
                z = false;
                this.ungzip = z;
                j = document.size;
                this.totalBytesCount = j;
                if (this.key != null && j % 16 != 0) {
                    long j3 = 16 - (j % 16);
                    this.bytesCountPadding = j3;
                    this.totalBytesCount = j + j3;
                }
                documentFileName = FileLoader.getDocumentFileName(document);
                this.ext = documentFileName;
                if (documentFileName != null && (lastIndexOf = documentFileName.lastIndexOf(46)) != -1) {
                    str = this.ext.substring(lastIndexOf);
                }
                this.ext = str;
                if (!"audio/ogg".equals(document.mime_type)) {
                    this.currentType = ConnectionsManager.FileTypeAudio;
                } else if (FileLoader.isVideoMimeType(document.mime_type)) {
                    this.currentType = ConnectionsManager.FileTypeVideo;
                } else {
                    this.currentType = ConnectionsManager.FileTypeFile;
                }
                if (this.ext.length() > 1) {
                    this.ext = FileLoader.getExtensionByMimeType(document.mime_type);
                    return;
                }
                return;
            }
            z = true;
            this.ungzip = z;
            j = document.size;
            this.totalBytesCount = j;
            if (this.key != null) {
                long j32 = 16 - (j % 16);
                this.bytesCountPadding = j32;
                this.totalBytesCount = j + j32;
            }
            documentFileName = FileLoader.getDocumentFileName(document);
            this.ext = documentFileName;
            if (documentFileName != null) {
                str = this.ext.substring(lastIndexOf);
            }
            this.ext = str;
            if (!"audio/ogg".equals(document.mime_type)) {
            }
            if (this.ext.length() > 1) {
            }
        } catch (Exception e) {
            FileLog.e(e);
            onFail(true, 0);
        }
    }

    private void addPart(ArrayList<Range> arrayList, long j, long j2, boolean z) {
        if (arrayList == null || j2 < j) {
            return;
        }
        int size = arrayList.size();
        boolean z2 = false;
        for (int i = 0; i < size; i++) {
            Range range = arrayList.get(i);
            long j3 = range.start;
            long j4 = range.end;
            if (j <= j3) {
                if (j2 >= j4) {
                    arrayList.remove(i);
                    z2 = true;
                    break;
                } else {
                    if (j2 > range.start) {
                        range.start = j2;
                        z2 = true;
                        break;
                    }
                }
            } else if (j2 < j4) {
                arrayList.add(0, new Range(range.start, j));
                range.start = j2;
                z2 = true;
                break;
            } else {
                if (j < range.end) {
                    range.end = j;
                    z2 = true;
                    break;
                }
            }
        }
        if (z) {
            if (!z2) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e(this.cacheFileFinal + " downloaded duplicate file part " + j + " - " + j2);
                    return;
                }
                return;
            }
            final ArrayList arrayList2 = new ArrayList(arrayList);
            if (this.fileWriteRunnable != null) {
                filesQueue.cancelRunnable(this.fileWriteRunnable);
            }
            synchronized (this) {
                this.writingToFilePartsStream = true;
            }
            DispatchQueue dispatchQueue = filesQueue;
            Runnable runnable = new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda25
                @Override // java.lang.Runnable
                public final void run() {
                    FileLoadOperation.this.lambda$addPart$2(arrayList2);
                }
            };
            this.fileWriteRunnable = runnable;
            dispatchQueue.postRunnable(runnable);
            notifyStreamListeners();
        }
    }

    private boolean canFinishPreload() {
        return this.isStory && this.priority < 3;
    }

    private void cancel(final boolean z) {
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$cancel$13(z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: cancelOnStage, reason: merged with bridge method [inline-methods] */
    public void lambda$cancel$13(boolean z) {
        if (this.state != 3 && this.state != 2) {
            this.state = 5;
            cancelRequests(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    FileLoadOperation.this.lambda$cancelOnStage$14();
                }
            });
        }
        if (z) {
            File file = this.cacheFileFinal;
            if (file != null) {
                try {
                    if (!file.delete()) {
                        this.cacheFileFinal.deleteOnExit();
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            File file2 = this.cacheFileTemp;
            if (file2 != null) {
                try {
                    if (!file2.delete()) {
                        this.cacheFileTemp.deleteOnExit();
                    }
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            }
            File file3 = this.cacheFileParts;
            if (file3 != null) {
                try {
                    if (!file3.delete()) {
                        this.cacheFileParts.deleteOnExit();
                    }
                } catch (Exception e3) {
                    FileLog.e(e3);
                }
            }
            File file4 = this.cacheIvTemp;
            if (file4 != null) {
                try {
                    if (!file4.delete()) {
                        this.cacheIvTemp.deleteOnExit();
                    }
                } catch (Exception e4) {
                    FileLog.e(e4);
                }
            }
            File file5 = this.cacheFilePreload;
            if (file5 != null) {
                try {
                    if (file5.delete()) {
                        return;
                    }
                    this.cacheFilePreload.deleteOnExit();
                } catch (Exception e5) {
                    FileLog.e(e5);
                }
            }
        }
    }

    private void cancelRequests(final Runnable runnable) {
        StringBuilder sb = new StringBuilder();
        sb.append("cancelRequests");
        sb.append(runnable != null ? " with callback" : "");
        FileLog.d(sb.toString());
        if (this.requestInfos != null) {
            final int[] iArr = new int[1];
            int[] iArr2 = new int[2];
            int i = 0;
            for (int i2 = 0; i2 < this.requestInfos.size(); i2++) {
                final RequestInfo requestInfo = this.requestInfos.get(i2);
                if (requestInfo.requestToken != 0) {
                    requestInfo.cancelling = true;
                    if (runnable == null) {
                        requestInfo.cancelled = true;
                        FileLog.d("cancelRequests cancel " + requestInfo.requestToken);
                        ConnectionsManager.getInstance(this.currentAccount).cancelRequest(requestInfo.requestToken, true);
                    } else {
                        requestInfo.whenCancelled = new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda7
                            @Override // java.lang.Runnable
                            public final void run() {
                                FileLoadOperation.lambda$cancelRequests$15(FileLoadOperation.RequestInfo.this, iArr, runnable);
                            }
                        };
                        iArr[0] = iArr[0] + 1;
                        FileLog.d("cancelRequests cancel " + requestInfo.requestToken + " with callback");
                        ConnectionsManager.getInstance(this.currentAccount).cancelRequest(requestInfo.requestToken, true, new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda8
                            @Override // java.lang.Runnable
                            public final void run() {
                                FileLoadOperation.lambda$cancelRequests$16(FileLoadOperation.RequestInfo.this);
                            }
                        });
                    }
                    char c = requestInfo.connectionType == 2 ? (char) 0 : (char) 1;
                    iArr2[c] = iArr2[c] + requestInfo.chunkSize;
                }
            }
            while (i < 2) {
                int i3 = i == 0 ? 2 : ConnectionsManager.ConnectionTypeDownload2;
                if (iArr2[i] > 1048576) {
                    ConnectionsManager.getInstance(this.currentAccount).discardConnection(this.isCdn ? this.cdnDatacenterId : this.datacenterId, i3);
                }
                i++;
            }
        }
    }

    private void cleanup() {
        try {
            RandomAccessFile randomAccessFile = this.fileOutputStream;
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.getChannel().close();
                } catch (Exception e) {
                    FileLog.e(e);
                }
                this.fileOutputStream.close();
                this.fileOutputStream = null;
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
        try {
            RandomAccessFile randomAccessFile2 = this.preloadStream;
            if (randomAccessFile2 != null) {
                try {
                    randomAccessFile2.getChannel().close();
                } catch (Exception e3) {
                    FileLog.e(e3);
                }
                this.preloadStream.close();
                this.preloadStream = null;
            }
        } catch (Exception e4) {
            FileLog.e(e4);
        }
        try {
            RandomAccessFile randomAccessFile3 = this.fileReadStream;
            if (randomAccessFile3 != null) {
                try {
                    randomAccessFile3.getChannel().close();
                } catch (Exception e5) {
                    FileLog.e(e5);
                }
                this.fileReadStream.close();
                this.fileReadStream = null;
            }
        } catch (Exception e6) {
            FileLog.e(e6);
        }
        try {
            if (this.filePartsStream != null) {
                synchronized (this) {
                    if (this.writingToFilePartsStream) {
                        this.closeFilePartsStreamOnWriteEnd = true;
                    } else {
                        try {
                            this.filePartsStream.getChannel().close();
                        } catch (Exception e7) {
                            FileLog.e(e7);
                        }
                        this.filePartsStream.close();
                        this.filePartsStream = null;
                    }
                }
            }
        } catch (Exception e8) {
            FileLog.e(e8);
        }
        try {
            RandomAccessFile randomAccessFile4 = this.fiv;
            if (randomAccessFile4 != null) {
                randomAccessFile4.close();
                this.fiv = null;
            }
        } catch (Exception e9) {
            FileLog.e(e9);
        }
        if (this.delayedRequestInfos != null) {
            for (int i = 0; i < this.delayedRequestInfos.size(); i++) {
                RequestInfo requestInfo = this.delayedRequestInfos.get(i);
                if (requestInfo.response != null) {
                    requestInfo.response.disableFree = false;
                    requestInfo.response.freeResources();
                } else if (requestInfo.responseWeb != null) {
                    requestInfo.responseWeb.disableFree = false;
                    requestInfo.responseWeb.freeResources();
                } else if (requestInfo.responseCdn != null) {
                    requestInfo.responseCdn.disableFree = false;
                    requestInfo.responseCdn.freeResources();
                }
            }
            this.delayedRequestInfos.clear();
        }
    }

    private void clearOperation(RequestInfo requestInfo, boolean z, boolean z2) {
        int[] iArr = new int[2];
        long j = Long.MAX_VALUE;
        int i = 0;
        while (i < this.requestInfos.size()) {
            final RequestInfo requestInfo2 = this.requestInfos.get(i);
            long min = Math.min(requestInfo2.offset, j);
            if (this.isPreloadVideoOperation) {
                this.requestedPreloadedBytesRanges.remove(Long.valueOf(requestInfo2.offset));
            } else {
                removePart(this.notRequestedBytesRanges, requestInfo2.offset, requestInfo2.offset + requestInfo2.chunkSize);
            }
            if (requestInfo != requestInfo2 && requestInfo2.requestToken != 0) {
                requestInfo2.cancelling = true;
                if (z2) {
                    this.cancelledRequestInfos.add(requestInfo2);
                    requestInfo2.whenCancelled = new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda9
                        @Override // java.lang.Runnable
                        public final void run() {
                            FileLoadOperation.this.lambda$clearOperation$24(requestInfo2);
                        }
                    };
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(requestInfo2.requestToken, true, new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda10
                        @Override // java.lang.Runnable
                        public final void run() {
                            FileLoadOperation.lambda$clearOperation$25(FileLoadOperation.RequestInfo.this);
                        }
                    });
                } else {
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(requestInfo2.requestToken, true);
                    requestInfo2.cancelled = true;
                }
            }
            i++;
            j = min;
        }
        int i2 = 0;
        while (i2 < 2) {
            int i3 = i2 == 0 ? 2 : ConnectionsManager.ConnectionTypeDownload2;
            if (iArr[i2] > 1048576) {
                ConnectionsManager.getInstance(this.currentAccount).discardConnection(this.isCdn ? this.cdnDatacenterId : this.datacenterId, i3);
            }
            i2++;
        }
        this.requestInfos.clear();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$clearOperation$26();
            }
        });
        long j2 = j;
        for (int i4 = 0; i4 < this.delayedRequestInfos.size(); i4++) {
            RequestInfo requestInfo3 = this.delayedRequestInfos.get(i4);
            if (this.isPreloadVideoOperation) {
                this.requestedPreloadedBytesRanges.remove(Long.valueOf(requestInfo3.offset));
            } else {
                removePart(this.notRequestedBytesRanges, requestInfo3.offset, requestInfo3.offset + requestInfo3.chunkSize);
            }
            if (requestInfo3.response != null) {
                requestInfo3.response.disableFree = false;
                requestInfo3.response.freeResources();
            } else if (requestInfo3.responseWeb != null) {
                requestInfo3.responseWeb.disableFree = false;
                requestInfo3.responseWeb.freeResources();
            } else if (requestInfo3.responseCdn != null) {
                requestInfo3.responseCdn.disableFree = false;
                requestInfo3.responseCdn.freeResources();
            }
            j2 = Math.min(requestInfo3.offset, j2);
        }
        this.delayedRequestInfos.clear();
        this.requestsCount = 0;
        if (!z && this.isPreloadVideoOperation) {
            this.requestedBytesCount = this.totalPreloadedBytes;
        } else if (this.notLoadedBytesRanges == null) {
            this.downloadedBytes = j2;
            this.requestedBytesCount = j2;
        }
    }

    private void copyNotLoadedRanges() {
        if (this.notLoadedBytesRanges == null) {
            return;
        }
        this.notLoadedBytesRangesCopy = new ArrayList<>(this.notLoadedBytesRanges);
    }

    private void delayRequestInfo(RequestInfo requestInfo) {
        TLObject tLObject;
        this.delayedRequestInfos.add(requestInfo);
        if (requestInfo.response != null) {
            tLObject = requestInfo.response;
        } else if (requestInfo.responseWeb != null) {
            tLObject = requestInfo.responseWeb;
        } else if (requestInfo.responseCdn == null) {
            return;
        } else {
            tLObject = requestInfo.responseCdn;
        }
        tLObject.disableFree = true;
    }

    private long findNextPreloadDownloadOffset(long j, long j2, NativeByteBuffer nativeByteBuffer) {
        long j3;
        int limit = nativeByteBuffer.limit();
        long j4 = j;
        do {
            if (j4 >= j2 - (this.preloadTempBuffer != null ? 16 : 0)) {
                j3 = j2 + limit;
                if (j4 < j3) {
                    if (j4 >= j3 - 16) {
                        long j5 = j3 - j4;
                        if (j5 > 2147483647L) {
                            throw new RuntimeException("!!!");
                        }
                        this.preloadTempBufferCount = (int) j5;
                        nativeByteBuffer.position(nativeByteBuffer.limit() - this.preloadTempBufferCount);
                        nativeByteBuffer.readBytes(this.preloadTempBuffer, 0, this.preloadTempBufferCount, false);
                        return j3;
                    }
                    if (this.preloadTempBufferCount != 0) {
                        nativeByteBuffer.position(0);
                        byte[] bArr = this.preloadTempBuffer;
                        int i = this.preloadTempBufferCount;
                        nativeByteBuffer.readBytes(bArr, i, 16 - i, false);
                        this.preloadTempBufferCount = 0;
                    } else {
                        long j6 = j4 - j2;
                        if (j6 > 2147483647L) {
                            throw new RuntimeException("!!!");
                        }
                        nativeByteBuffer.position((int) j6);
                        nativeByteBuffer.readBytes(this.preloadTempBuffer, 0, 16, false);
                    }
                    byte[] bArr2 = this.preloadTempBuffer;
                    int i2 = ((bArr2[0] & 255) << 24) + ((bArr2[1] & 255) << 16) + ((bArr2[2] & 255) << 8) + (bArr2[3] & 255);
                    if (i2 == 0) {
                        return 0L;
                    }
                    if (i2 == 1) {
                        i2 = ((bArr2[12] & 255) << 24) + ((bArr2[13] & 255) << 16) + ((bArr2[14] & 255) << 8) + (bArr2[15] & 255);
                    }
                    if (bArr2[4] == 109 && bArr2[5] == 111 && bArr2[6] == 111 && bArr2[7] == 118) {
                        return -i2;
                    }
                    j4 += i2;
                }
            }
            return 0L;
        } while (j4 < j3);
        return j4;
    }

    public static long floorDiv(long j, long j2) {
        long j3 = j / j2;
        return ((j ^ j2) >= 0 || j2 * j3 == j) ? j3 : j3 - 1;
    }

    private long getDownloadedLengthFromOffsetInternal(ArrayList<Range> arrayList, long j, long j2) {
        long j3;
        long max;
        long j4;
        if (arrayList != null && this.state != 3 && !arrayList.isEmpty()) {
            int size = arrayList.size();
            Range range = null;
            int i = 0;
            while (true) {
                if (i >= size) {
                    j4 = j2;
                    break;
                }
                Range range2 = arrayList.get(i);
                if (j <= range2.start && (range == null || range2.start < range.start)) {
                    range = range2;
                }
                if (range2.start <= j && range2.end > j) {
                    j4 = 0;
                    break;
                }
                i++;
            }
            if (j4 == 0) {
                return 0L;
            }
            if (range != null) {
                max = range.start - j;
                return Math.min(j2, max);
            }
            j3 = this.totalBytesCount;
        } else {
            if (this.state == 3) {
                return j2;
            }
            j3 = this.downloadedBytes;
            if (j3 == 0) {
                return 0L;
            }
        }
        max = Math.max(j3 - j, 0L);
        return Math.min(j2, max);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addPart$2(ArrayList arrayList) {
        long currentTimeMillis = System.currentTimeMillis();
        try {
        } catch (Exception e) {
            FileLog.e((Throwable) e, false);
            if (AndroidUtilities.isENOSPC(e)) {
                LaunchActivity.checkFreeDiscSpaceStatic(1);
            } else if (AndroidUtilities.isEROFS(e)) {
                SharedConfig.checkSdCard(this.cacheFileFinal);
            }
        }
        if (this.filePartsStream == null) {
            return;
        }
        int size = arrayList.size();
        int i = (size * 16) + 4;
        ImmutableByteArrayOutputStream immutableByteArrayOutputStream = filesQueueByteBuffer;
        if (immutableByteArrayOutputStream == null) {
            filesQueueByteBuffer = new ImmutableByteArrayOutputStream(i);
        } else {
            immutableByteArrayOutputStream.reset();
        }
        filesQueueByteBuffer.writeInt(size);
        for (int i2 = 0; i2 < size; i2++) {
            Range range = (Range) arrayList.get(i2);
            filesQueueByteBuffer.writeLong(range.start);
            filesQueueByteBuffer.writeLong(range.end);
        }
        synchronized (this) {
            try {
                RandomAccessFile randomAccessFile = this.filePartsStream;
                if (randomAccessFile == null) {
                    return;
                }
                randomAccessFile.seek(0L);
                this.filePartsStream.write(filesQueueByteBuffer.buf, 0, i);
                this.writingToFilePartsStream = false;
                if (this.closeFilePartsStreamOnWriteEnd) {
                    try {
                        this.filePartsStream.getChannel().close();
                    } catch (Exception e2) {
                        FileLog.e(e2);
                    }
                    this.filePartsStream.close();
                    this.filePartsStream = null;
                }
                this.totalTime += System.currentTimeMillis() - currentTimeMillis;
            } finally {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelOnStage$14() {
        if (this.state == 5) {
            onFail(false, 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$cancelRequests$15(RequestInfo requestInfo, int[] iArr, Runnable runnable) {
        requestInfo.whenCancelled = null;
        requestInfo.cancelled = true;
        int i = iArr[0] - 1;
        iArr[0] = i;
        if (i == 0) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$cancelRequests$16(RequestInfo requestInfo) {
        Runnable runnable = requestInfo.whenCancelled;
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearOperation$24(RequestInfo requestInfo) {
        requestInfo.whenCancelled = null;
        this.cancelledRequestInfos.remove(requestInfo);
        requestInfo.cancelled = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$clearOperation$25(RequestInfo requestInfo) {
        Runnable runnable = requestInfo.whenCancelled;
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearOperation$26() {
        this.uiRequestTokens.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getCurrentFile$3(File[] fileArr, CountDownLatch countDownLatch) {
        if (this.state != 3 || this.preloadFinished) {
            fileArr[0] = this.cacheFileTemp;
        } else {
            fileArr[0] = this.cacheFileFinal;
        }
        countDownLatch.countDown();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getDownloadedLengthFromOffset$4(long[] jArr, long j, long j2, CountDownLatch countDownLatch) {
        try {
            jArr[0] = getDownloadedLengthFromOffsetInternal(this.notLoadedBytesRanges, j, j2);
        } catch (Throwable th) {
            FileLog.e(th);
            jArr[0] = 0;
        }
        if (this.state == 3) {
            jArr[1] = 1;
        }
        countDownLatch.countDown();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$6() {
        pause();
        FileLoader.getInstance(this.currentAccount).cancelLoadFile(getFileName());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFail$23(int i) {
        FileLoadOperationDelegate fileLoadOperationDelegate = this.delegate;
        if (fileLoadOperationDelegate != null) {
            fileLoadOperationDelegate.didFailedLoadingFile(this, i);
        }
        notifyStreamListeners();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishLoadingFile$17(boolean z) {
        try {
            onFinishLoadingFile(z, 0, false);
        } catch (Exception unused) {
            onFail(false, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishLoadingFile$18() {
        onFail(false, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishLoadingFile$19(boolean z) {
        StatsController statsController;
        int currentNetworkType;
        int i;
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("finished downloading file to " + this.cacheFileFinal + " time = " + (System.currentTimeMillis() - this.startTime) + " dc = " + this.datacenterId + " size = " + AndroidUtilities.formatFileSize(this.totalBytesCount));
        }
        if (z) {
            int i2 = this.currentType;
            if (i2 == 50331648) {
                statsController = StatsController.getInstance(this.currentAccount);
                currentNetworkType = ApplicationLoader.getCurrentNetworkType();
                i = 3;
            } else if (i2 == 33554432) {
                statsController = StatsController.getInstance(this.currentAccount);
                currentNetworkType = ApplicationLoader.getCurrentNetworkType();
                i = 2;
            } else if (i2 == 16777216) {
                statsController = StatsController.getInstance(this.currentAccount);
                currentNetworkType = ApplicationLoader.getCurrentNetworkType();
                i = 4;
            } else if (i2 == 67108864) {
                String str = this.ext;
                if (str == null || !(str.toLowerCase().endsWith("mp3") || this.ext.toLowerCase().endsWith("m4a"))) {
                    statsController = StatsController.getInstance(this.currentAccount);
                    currentNetworkType = ApplicationLoader.getCurrentNetworkType();
                    i = 5;
                } else {
                    statsController = StatsController.getInstance(this.currentAccount);
                    currentNetworkType = ApplicationLoader.getCurrentNetworkType();
                    i = 7;
                }
            }
            statsController.incrementReceivedItemsCount(currentNetworkType, i, 1);
        }
        this.delegate.didFinishLoadingFile(this, this.cacheFileFinal);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:12:0x006d  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0178  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$onFinishLoadingFile$20(File file, File file2, File file3, File file4, final boolean z) {
        Throwable th;
        File file5;
        boolean z2;
        StringBuilder sb;
        String str;
        if (file != null) {
            file.delete();
        }
        if (file2 != null) {
            file2.delete();
        }
        if (file3 != null) {
            file3.delete();
        }
        if (file4 != null) {
            if (this.ungzip) {
                try {
                    GZIPInputStream gZIPInputStream = new GZIPInputStream(new FileInputStream(file4));
                    FileLoader.copyFile(gZIPInputStream, this.cacheFileGzipTemp, preloadMaxBytes);
                    gZIPInputStream.close();
                    file4.delete();
                    file5 = this.cacheFileGzipTemp;
                    try {
                        this.ungzip = false;
                    } catch (ZipException unused) {
                        file4 = file5;
                        this.ungzip = false;
                        if (this.ungzip) {
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        FileLog.e(th, !AndroidUtilities.isFilNotFoundException(th));
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.e("unable to ungzip temp = " + file4 + " to final = " + this.cacheFileFinal);
                        }
                        file4 = file5;
                        if (this.ungzip) {
                        }
                    }
                } catch (ZipException unused2) {
                } catch (Throwable th3) {
                    th = th3;
                    file5 = file4;
                }
                file4 = file5;
            }
            if (this.ungzip) {
                Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda29
                    @Override // java.lang.Runnable
                    public final void run() {
                        FileLoadOperation.this.lambda$onFinishLoadingFile$18();
                    }
                });
                return;
            }
            try {
                if (this.parentObject instanceof TLRPC.TL_theme) {
                    z2 = AndroidUtilities.copyFile(file4, this.cacheFileFinal);
                } else {
                    if (this.pathSaveData != null) {
                        synchronized (lockObject) {
                            try {
                                this.cacheFileFinal = new File(this.storePath, this.storeFileName);
                                int i = 1;
                                while (this.cacheFileFinal.exists()) {
                                    int lastIndexOf = this.storeFileName.lastIndexOf(46);
                                    if (lastIndexOf > 0) {
                                        sb = new StringBuilder();
                                        sb.append(this.storeFileName.substring(0, lastIndexOf));
                                        sb.append(" (");
                                        sb.append(i);
                                        sb.append(")");
                                        str = this.storeFileName.substring(lastIndexOf);
                                    } else {
                                        sb = new StringBuilder();
                                        sb.append(this.storeFileName);
                                        sb.append(" (");
                                        sb.append(i);
                                        str = ")";
                                    }
                                    sb.append(str);
                                    this.cacheFileFinal = new File(this.storePath, sb.toString());
                                    i++;
                                }
                            } finally {
                            }
                        }
                    }
                    z2 = file4.renameTo(this.cacheFileFinal);
                }
            } catch (Exception e) {
                FileLog.e(e);
                z2 = false;
            }
            if (!z2 && this.renameRetryCount == 3) {
                try {
                    z2 = AndroidUtilities.copyFile(file4, this.cacheFileFinal);
                    if (z2) {
                        this.cacheFileFinal.delete();
                    }
                } catch (Throwable th4) {
                    FileLog.e(th4);
                }
            }
            if (z2) {
                this.cacheFileFinalReady = true;
                if (this.pathSaveData != null && this.cacheFileFinal.exists()) {
                    this.delegate.saveFilePath(this.pathSaveData, this.cacheFileFinal);
                }
            } else {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("unable to rename temp = " + file4 + " to final = " + this.cacheFileFinal + " retry = " + this.renameRetryCount);
                }
                int i2 = this.renameRetryCount + 1;
                this.renameRetryCount = i2;
                if (i2 < 3) {
                    this.state = 1;
                    Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda28
                        @Override // java.lang.Runnable
                        public final void run() {
                            FileLoadOperation.this.lambda$onFinishLoadingFile$17(z);
                        }
                    }, 200L);
                    return;
                } else {
                    this.cacheFileFinal = file4;
                    this.cacheFileFinalReady = false;
                }
            }
        }
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda30
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$onFinishLoadingFile$19(z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$pause$7() {
        if (!this.isStory) {
            for (int i = 0; i < this.requestInfos.size(); i++) {
                ConnectionsManager.getInstance(this.currentAccount).failNotRunningRequest(this.requestInfos.get(i).requestToken);
            }
            return;
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("debug_loading: " + this.cacheFileFinal.getName() + " pause operation, clear requests");
        }
        clearOperation(null, false, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processRequestResult$22(int i) {
        this.uiRequestTokens.remove(Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$removePart$1(Range range, Range range2) {
        if (range.start > range2.start) {
            return 1;
        }
        return range.start < range2.start ? -1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeStreamListener$5(FileLoadOperationStream fileLoadOperationStream) {
        if (this.streamListeners == null) {
            return;
        }
        FileLog.e("FileLoadOperation " + getFileName() + " removing stream listener " + fileLoadOperationStream);
        this.streamListeners.remove(fileLoadOperationStream);
        if (!this.isStory && this.streamListeners.isEmpty()) {
            Utilities.stageQueue.cancelRunnable(this.cancelAfterNoStreamListeners);
            Utilities.stageQueue.postRunnable(this.cancelAfterNoStreamListeners, 1200L);
        } else {
            if (this.streamListeners.isEmpty()) {
                return;
            }
            Utilities.stageQueue.cancelRunnable(this.cancelAfterNoStreamListeners);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestFileOffsets$21(TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error != null) {
            onFail(false, 0);
            return;
        }
        if (tLObject instanceof Vector) {
            this.requestingCdnOffsets = false;
            Vector vector = (Vector) tLObject;
            if (!vector.objects.isEmpty()) {
                if (this.cdnHashes == null) {
                    this.cdnHashes = new HashMap<>();
                }
                for (int i = 0; i < vector.objects.size(); i++) {
                    TLRPC.TL_fileHash tL_fileHash = (TLRPC.TL_fileHash) vector.objects.get(i);
                    this.cdnHashes.put(Long.valueOf(tL_fileHash.offset), tL_fileHash);
                }
            }
            for (int i2 = 0; i2 < this.delayedRequestInfos.size(); i2++) {
                RequestInfo requestInfo = this.delayedRequestInfos.get(i2);
                if (this.notLoadedBytesRanges != null || this.downloadedBytes == requestInfo.offset) {
                    this.delayedRequestInfos.remove(i2);
                    if (processRequestResult(requestInfo, null)) {
                        return;
                    }
                    if (requestInfo.response != null) {
                        requestInfo.response.disableFree = false;
                        requestInfo.response.freeResources();
                        return;
                    } else if (requestInfo.responseWeb != null) {
                        requestInfo.responseWeb.disableFree = false;
                        requestInfo.responseWeb.freeResources();
                        return;
                    } else {
                        if (requestInfo.responseCdn != null) {
                            requestInfo.responseCdn.disableFree = false;
                            requestInfo.responseCdn.freeResources();
                            return;
                        }
                        return;
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setIsPreloadVideoOperation$12(boolean z) {
        this.requestedBytesCount = 0L;
        clearOperation(null, true, true);
        this.isPreloadVideoOperation = z;
        startDownloadRequest(-1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setStream$0(FileLoadOperationStream fileLoadOperationStream) {
        if (this.streamListeners == null) {
            this.streamListeners = new ArrayList<>();
        }
        if (fileLoadOperationStream != null && !this.streamListeners.contains(fileLoadOperationStream)) {
            this.streamListeners.add(fileLoadOperationStream);
        }
        if (!this.streamListeners.isEmpty()) {
            Utilities.stageQueue.cancelRunnable(this.cancelAfterNoStreamListeners);
        }
        if (fileLoadOperationStream == null || this.state == 1 || this.state == 0) {
            return;
        }
        fileLoadOperationStream.newDataAvailable();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$10() {
        startDownloadRequest(-1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$11(boolean[] zArr) {
        boolean z = this.isPreloadVideoOperation && zArr[0];
        int i = this.preloadPrefixSize;
        boolean z2 = i > 0 && this.downloadedBytes >= ((long) i) && canFinishPreload();
        long j = this.totalBytesCount;
        if (j == 0 || !(z || this.downloadedBytes == j || z2)) {
            startDownloadRequest(-1);
            return;
        }
        try {
            onFinishLoadingFile(false, 1, true);
        } catch (Exception unused) {
            onFail(true, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$8(int i) {
        this.uiRequestTokens.remove(Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$9(boolean z, long j, FileLoadOperationStream fileLoadOperationStream, boolean z2) {
        if (this.streamListeners == null) {
            this.streamListeners = new ArrayList<>();
        }
        if (z) {
            long j2 = this.currentDownloadChunkSize;
            long j3 = (j / j2) * j2;
            RequestInfo requestInfo = this.priorityRequestInfo;
            if (requestInfo != null && requestInfo.offset != j3) {
                RequestInfo requestInfo2 = this.priorityRequestInfo;
                final int i = requestInfo2.requestToken;
                this.requestInfos.remove(requestInfo2);
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda20
                    @Override // java.lang.Runnable
                    public final void run() {
                        FileLoadOperation.this.lambda$start$8(i);
                    }
                });
                this.requestedBytesCount -= this.currentDownloadChunkSize;
                removePart(this.notRequestedBytesRanges, this.priorityRequestInfo.offset, this.currentDownloadChunkSize + this.priorityRequestInfo.offset);
                if (this.priorityRequestInfo.requestToken != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.priorityRequestInfo.requestToken, true);
                    this.requestsCount--;
                }
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d("frame get cancel request at offset " + this.priorityRequestInfo.offset);
                }
                this.priorityRequestInfo = null;
            }
            if (this.priorityRequestInfo == null) {
                this.streamPriorityStartOffset = j3;
            }
        } else {
            long j4 = this.currentDownloadChunkSize;
            this.streamStartOffset = (j / j4) * j4;
        }
        if (!this.streamListeners.contains(fileLoadOperationStream)) {
            this.streamListeners.add(fileLoadOperationStream);
            FileLog.e("FileLoadOperation " + getFileName() + " start, adding stream " + fileLoadOperationStream);
        }
        if (!this.streamListeners.isEmpty()) {
            Utilities.stageQueue.cancelRunnable(this.cancelAfterNoStreamListeners);
        }
        if (z2) {
            if (this.preloadedBytesRanges != null && getDownloadedLengthFromOffsetInternal(this.notLoadedBytesRanges, this.streamStartOffset, 1L) == 0 && this.preloadedBytesRanges.get(Long.valueOf(this.streamStartOffset)) != null) {
                this.nextPartWasPreloaded = true;
            }
            startDownloadRequest(-1);
            this.nextPartWasPreloaded = false;
        }
        if (this.notLoadedBytesRanges != null) {
            notifyStreamListeners();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startDownloadRequest$27(RequestInfo requestInfo) {
        processRequestResult(requestInfo, null);
        requestInfo.response.freeResources();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startDownloadRequest$28(int i, RequestInfo requestInfo, TLObject tLObject, TLRPC.TL_error tL_error) {
        this.reuploadingCdn = false;
        if (tLObject instanceof Vector) {
            Vector vector = (Vector) tLObject;
            if (!vector.objects.isEmpty()) {
                if (this.cdnHashes == null) {
                    this.cdnHashes = new HashMap<>();
                }
                for (int i2 = 0; i2 < vector.objects.size(); i2++) {
                    TLRPC.TL_fileHash tL_fileHash = (TLRPC.TL_fileHash) vector.objects.get(i2);
                    this.cdnHashes.put(Long.valueOf(tL_fileHash.offset), tL_fileHash);
                }
            }
        } else {
            if (tL_error == null) {
                return;
            }
            if (!tL_error.text.equals("FILE_TOKEN_INVALID") && !tL_error.text.equals("REQUEST_TOKEN_INVALID")) {
                onFail(false, 0);
                return;
            } else {
                this.isCdn = false;
                clearOperation(requestInfo, false, false);
            }
        }
        startDownloadRequest(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startDownloadRequest$29(final RequestInfo requestInfo, int i, final int i2, TLObject tLObject, TLObject tLObject2, TLRPC.TL_error tL_error) {
        StatsController statsController;
        int i3;
        long objectSize;
        int i4;
        byte[] bArr;
        if (requestInfo.cancelled) {
            FileLog.e("received chunk but definitely cancelled offset=" + requestInfo.offset + " size=" + requestInfo.chunkSize + " token=" + requestInfo.requestToken);
            return;
        }
        if (requestInfo.cancelling) {
            FileLog.e("received cancelled chunk after cancelRequests! offset=" + requestInfo.offset + " size=" + requestInfo.chunkSize + " token=" + requestInfo.requestToken);
        }
        if (!this.requestInfos.contains(requestInfo)) {
            if (!this.cancelledRequestInfos.contains(requestInfo)) {
                return;
            }
            int i5 = 0;
            boolean z = false;
            while (i5 < this.requestInfos.size()) {
                RequestInfo requestInfo2 = this.requestInfos.get(i5);
                if (requestInfo2 != null && requestInfo2 != requestInfo && requestInfo2.offset == requestInfo.offset && requestInfo2.chunkSize == requestInfo.chunkSize) {
                    FileLog.e("received cancelled chunk faster than new one! received=" + requestInfo.requestToken + " new=" + requestInfo2.requestToken);
                    if (z) {
                        this.requestInfos.remove(i5);
                        i5--;
                    } else {
                        this.requestInfos.set(i5, requestInfo);
                        z = true;
                    }
                }
                i5++;
            }
        }
        int i6 = 0;
        while (i6 < this.cancelledRequestInfos.size()) {
            RequestInfo requestInfo3 = this.cancelledRequestInfos.get(i6);
            if (requestInfo3 != null && requestInfo3 != requestInfo && requestInfo3.offset == requestInfo.offset && requestInfo3.chunkSize == requestInfo.chunkSize) {
                FileLog.e("received new chunk faster than cancelled one! received=" + requestInfo.requestToken + " cancelled=" + requestInfo3.requestToken);
                this.cancelledRequestInfos.remove(i6);
                i6 += -1;
            }
            i6++;
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("debug_loading: " + this.cacheFileFinal.getName() + " time=" + (System.currentTimeMillis() - requestInfo.requestStartTime) + " dcId=" + i + " cdn=" + this.isCdn + " conType=" + i2 + " reqId" + requestInfo.requestToken);
        }
        if (requestInfo == this.priorityRequestInfo) {
            if (BuildVars.DEBUG_VERSION) {
                FileLog.d("frame get request completed " + this.priorityRequestInfo.offset);
            }
            this.priorityRequestInfo = null;
        }
        if (tL_error != null) {
            Runnable runnable = requestInfo.whenCancelled;
            if (runnable != null) {
                runnable.run();
            }
            if (tL_error.code == -2000) {
                this.requestInfos.remove(requestInfo);
                this.requestedBytesCount -= requestInfo.chunkSize;
                removePart(this.notRequestedBytesRanges, requestInfo.offset, requestInfo.offset + requestInfo.chunkSize);
                return;
            } else if (FileRefController.isFileRefError(tL_error.text)) {
                requestReference(requestInfo);
                return;
            } else if ((tLObject instanceof TLRPC.TL_upload_getCdnFile) && tL_error.text.equals("FILE_TOKEN_INVALID")) {
                this.isCdn = false;
                clearOperation(requestInfo, false, false);
                startDownloadRequest(i2);
                return;
            }
        }
        if (tLObject2 instanceof TLRPC.TL_upload_fileCdnRedirect) {
            TLRPC.TL_upload_fileCdnRedirect tL_upload_fileCdnRedirect = (TLRPC.TL_upload_fileCdnRedirect) tLObject2;
            if (!tL_upload_fileCdnRedirect.file_hashes.isEmpty()) {
                if (this.cdnHashes == null) {
                    this.cdnHashes = new HashMap<>();
                }
                for (int i7 = 0; i7 < tL_upload_fileCdnRedirect.file_hashes.size(); i7++) {
                    TLRPC.TL_fileHash tL_fileHash = tL_upload_fileCdnRedirect.file_hashes.get(i7);
                    this.cdnHashes.put(Long.valueOf(tL_fileHash.offset), tL_fileHash);
                }
            }
            byte[] bArr2 = tL_upload_fileCdnRedirect.encryption_iv;
            if (bArr2 == null || (bArr = tL_upload_fileCdnRedirect.encryption_key) == null || bArr2.length != 16 || bArr.length != 32) {
                Runnable runnable2 = requestInfo.whenCancelled;
                if (runnable2 != null) {
                    runnable2.run();
                }
                TLRPC.TL_error tL_error2 = new TLRPC.TL_error();
                tL_error2.text = "bad redirect response";
                tL_error2.code = 400;
                processRequestResult(requestInfo, tL_error2);
                return;
            }
            this.isCdn = true;
            if (this.notCheckedCdnRanges == null) {
                ArrayList<Range> arrayList = new ArrayList<>();
                this.notCheckedCdnRanges = arrayList;
                arrayList.add(new Range(0L, this.maxCdnParts));
            }
            this.cdnDatacenterId = tL_upload_fileCdnRedirect.dc_id;
            this.cdnIv = tL_upload_fileCdnRedirect.encryption_iv;
            this.cdnKey = tL_upload_fileCdnRedirect.encryption_key;
            this.cdnToken = tL_upload_fileCdnRedirect.file_token;
            clearOperation(requestInfo, false, false);
            startDownloadRequest(i2);
            return;
        }
        if (tLObject2 instanceof TLRPC.TL_upload_cdnFileReuploadNeeded) {
            if (this.reuploadingCdn) {
                return;
            }
            clearOperation(requestInfo, false, false);
            this.reuploadingCdn = true;
            TLRPC.TL_upload_reuploadCdnFile tL_upload_reuploadCdnFile = new TLRPC.TL_upload_reuploadCdnFile();
            tL_upload_reuploadCdnFile.file_token = this.cdnToken;
            tL_upload_reuploadCdnFile.request_token = ((TLRPC.TL_upload_cdnFileReuploadNeeded) tLObject2).request_token;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_upload_reuploadCdnFile, new RequestDelegate() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda5
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject3, TLRPC.TL_error tL_error3) {
                    FileLoadOperation.this.lambda$startDownloadRequest$28(i2, requestInfo, tLObject3, tL_error3);
                }
            }, null, null, 0, this.datacenterId, 1, true);
            return;
        }
        if (tLObject2 instanceof TLRPC.TL_upload_file) {
            requestInfo.response = (TLRPC.TL_upload_file) tLObject2;
        } else if (tLObject2 instanceof TLRPC.TL_upload_webFile) {
            requestInfo.responseWeb = (TLRPC.TL_upload_webFile) tLObject2;
            if (this.totalBytesCount == 0 && requestInfo.responseWeb.size != 0) {
                this.totalBytesCount = requestInfo.responseWeb.size;
            }
        } else {
            requestInfo.responseCdn = (TLRPC.TL_upload_cdnFile) tLObject2;
        }
        if (tLObject2 != null) {
            int i8 = this.currentType;
            if (i8 == 50331648) {
                statsController = StatsController.getInstance(this.currentAccount);
                i3 = tLObject2.networkType;
                objectSize = tLObject2.getObjectSize() + 4;
                i4 = 3;
            } else if (i8 == 33554432) {
                statsController = StatsController.getInstance(this.currentAccount);
                i3 = tLObject2.networkType;
                objectSize = tLObject2.getObjectSize() + 4;
                i4 = 2;
            } else if (i8 == 16777216) {
                StatsController.getInstance(this.currentAccount).incrementReceivedBytesCount(tLObject2.networkType, 4, tLObject2.getObjectSize() + 4);
            } else if (i8 == 67108864) {
                String str = this.ext;
                if (str == null || !(str.toLowerCase().endsWith("mp3") || this.ext.toLowerCase().endsWith("m4a"))) {
                    statsController = StatsController.getInstance(this.currentAccount);
                    i3 = tLObject2.networkType;
                    objectSize = tLObject2.getObjectSize() + 4;
                    i4 = 5;
                } else {
                    statsController = StatsController.getInstance(this.currentAccount);
                    i3 = tLObject2.networkType;
                    objectSize = tLObject2.getObjectSize() + 4;
                    i4 = 7;
                }
            }
            statsController.incrementReceivedBytesCount(i3, i4, objectSize);
        }
        processRequestResult(requestInfo, tL_error);
        Runnable runnable3 = requestInfo.whenCancelled;
        if (runnable3 != null) {
            runnable3.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startDownloadRequest$30(int i) {
        this.uiRequestTokens.add(Integer.valueOf(i));
    }

    private void notifyStreamListeners() {
        ArrayList<FileLoadOperationStream> arrayList = this.streamListeners;
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                this.streamListeners.get(i).newDataAvailable();
            }
        }
    }

    private void onFinishLoadingFile(final boolean z, int i, boolean z2) {
        StringBuilder sb;
        if (this.state == 1 || this.state == 5) {
            this.state = 3;
            notifyStreamListeners();
            cleanup();
            if (!this.isPreloadVideoOperation && !z2) {
                final File file = this.cacheIvTemp;
                final File file2 = this.cacheFileParts;
                final File file3 = this.cacheFilePreload;
                final File file4 = this.cacheFileTemp;
                filesQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda15
                    @Override // java.lang.Runnable
                    public final void run() {
                        FileLoadOperation.this.lambda$onFinishLoadingFile$20(file, file2, file3, file4, z);
                    }
                });
                this.cacheIvTemp = null;
                this.cacheFileParts = null;
                this.cacheFilePreload = null;
                this.delegate.didPreFinishLoading(this, this.cacheFileFinal);
                return;
            }
            this.preloadFinished = true;
            if (BuildVars.DEBUG_VERSION) {
                if (i == 1) {
                    sb = new StringBuilder();
                    sb.append("file already exist ");
                    sb.append(this.cacheFileTemp);
                } else {
                    sb = new StringBuilder();
                    sb.append("finished preloading file to ");
                    sb.append(this.cacheFileTemp);
                    sb.append(" loaded ");
                    sb.append(this.downloadedBytes);
                    sb.append(" of ");
                    sb.append(this.totalBytesCount);
                    sb.append(" prefSize=");
                    sb.append(this.preloadPrefixSize);
                }
                FileLog.d(sb.toString());
            }
            if (this.fileMetadata != null) {
                if (this.cacheFileTemp != null) {
                    FileLoader.getInstance(this.currentAccount).getFileDatabase().removeFiles(Collections.singletonList(new CacheModel.FileInfo(this.cacheFileTemp)));
                }
                if (this.cacheFileParts != null) {
                    FileLoader.getInstance(this.currentAccount).getFileDatabase().removeFiles(Collections.singletonList(new CacheModel.FileInfo(this.cacheFileParts)));
                }
            }
            this.delegate.didPreFinishLoading(this, this.cacheFileFinal);
            this.delegate.didFinishLoadingFile(this, this.cacheFileFinal);
        }
    }

    private void removePart(ArrayList<Range> arrayList, long j, long j2) {
        boolean z;
        if (arrayList == null || j2 < j) {
            return;
        }
        int size = arrayList.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            Range range = arrayList.get(i2);
            if (j == range.end) {
                range.end = j2;
            } else if (j2 == range.start) {
                range.start = j;
            }
            z = true;
        }
        z = false;
        Collections.sort(arrayList, new Comparator() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda21
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$removePart$1;
                lambda$removePart$1 = FileLoadOperation.lambda$removePart$1((FileLoadOperation.Range) obj, (FileLoadOperation.Range) obj2);
                return lambda$removePart$1;
            }
        });
        while (i < arrayList.size() - 1) {
            Range range2 = arrayList.get(i);
            int i3 = i + 1;
            Range range3 = arrayList.get(i3);
            if (range2.end == range3.start) {
                range2.end = range3.end;
                arrayList.remove(i3);
                i--;
            }
            i++;
        }
        if (z) {
            return;
        }
        arrayList.add(new Range(j, j2));
    }

    private void requestFileOffsets(long j) {
        if (this.requestingCdnOffsets) {
            return;
        }
        this.requestingCdnOffsets = true;
        TLRPC.TL_upload_getCdnFileHashes tL_upload_getCdnFileHashes = new TLRPC.TL_upload_getCdnFileHashes();
        tL_upload_getCdnFileHashes.file_token = this.cdnToken;
        tL_upload_getCdnFileHashes.offset = j;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_upload_getCdnFileHashes, new RequestDelegate() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda22
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                FileLoadOperation.this.lambda$requestFileOffsets$21(tLObject, tL_error);
            }
        }, null, null, 0, this.datacenterId, 1, true);
    }

    private void requestReference(RequestInfo requestInfo) {
        TLRPC.Message message;
        TLRPC.MessageMedia messageMedia;
        TLRPC.WebPage webPage;
        if (this.requestingReference) {
            return;
        }
        clearOperation(null, false, false);
        this.requestingReference = true;
        Object obj = this.parentObject;
        if (obj instanceof MessageObject) {
            MessageObject messageObject = (MessageObject) obj;
            if (messageObject.getId() < 0 && (message = messageObject.messageOwner) != null && (messageMedia = message.media) != null && (webPage = messageMedia.webpage) != null) {
                this.parentObject = webPage;
                this.isStory = false;
            }
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("debug_loading: " + this.cacheFileFinal.getName() + " file reference expired ");
        }
        FileRefController.getInstance(this.currentAccount).requestReference(this.parentObject, this.location, this, requestInfo);
    }

    private void updateParams() {
        int i;
        if ((this.preloadPrefixSize > 0 || MessagesController.getInstance(this.currentAccount).getfileExperimentalParams) && !this.forceSmallChunk) {
            this.downloadChunkSizeBig = 524288;
            i = 8;
        } else {
            this.downloadChunkSizeBig = 131072;
            i = 4;
        }
        this.maxDownloadRequests = i;
        this.maxDownloadRequestsBig = i;
        this.maxCdnParts = (int) (FileLoader.DEFAULT_MAX_FILE_SIZE / this.downloadChunkSizeBig);
    }

    public void cancel() {
        cancel(false);
    }

    public boolean checkPrefixPreloadFinished() {
        int i = this.preloadPrefixSize;
        if (i > 0 && this.downloadedBytes > i) {
            ArrayList<Range> arrayList = this.notLoadedBytesRanges;
            if (arrayList == null) {
                return true;
            }
            long j = Long.MAX_VALUE;
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                try {
                    j = Math.min(j, arrayList.get(i2).start);
                } catch (Throwable th) {
                    FileLog.e(th);
                    return true;
                }
            }
            if (j > this.preloadPrefixSize) {
                return true;
            }
        }
        return false;
    }

    protected File getCacheFileFinal() {
        return this.cacheFileFinal;
    }

    protected File getCurrentFile() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final File[] fileArr = new File[1];
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$getCurrentFile$3(fileArr, countDownLatch);
            }
        });
        try {
            countDownLatch.await();
        } catch (Exception e) {
            FileLog.e(e);
        }
        return fileArr[0];
    }

    protected File getCurrentFileFast() {
        return (this.state == 3 && !this.preloadFinished && this.cacheFileFinalReady) ? this.cacheFileFinal : this.cacheFileTemp;
    }

    public int getCurrentType() {
        return this.currentType;
    }

    public int getDatacenterId() {
        return this.initialDatacenterId;
    }

    public long getDocumentId() {
        return this.documentId;
    }

    protected float getDownloadedLengthFromOffset(float f) {
        ArrayList<Range> arrayList = this.notLoadedBytesRangesCopy;
        if (this.totalBytesCount == 0 || arrayList == null) {
            return 0.0f;
        }
        return f + (getDownloadedLengthFromOffsetInternal(arrayList, (int) (r4 * f), r4) / this.totalBytesCount);
    }

    protected long[] getDownloadedLengthFromOffset(final long j, final long j2) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final long[] jArr = new long[2];
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$getDownloadedLengthFromOffset$4(jArr, j, j2, countDownLatch);
            }
        });
        try {
            countDownLatch.await();
        } catch (Exception unused) {
        }
        return jArr;
    }

    public String getFileName() {
        return this.fileName;
    }

    public int getPositionInQueue() {
        return getQueue().getPosition(this);
    }

    public int getPriority() {
        return this.priority;
    }

    public FileLoaderPriorityQueue getQueue() {
        return this.priorityQueue;
    }

    public boolean isFinished() {
        return this.state == 3;
    }

    public boolean isForceRequest() {
        return this.isForceRequest;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public boolean isPreloadFinished() {
        return this.preloadFinished;
    }

    public boolean isPreloadVideoOperation() {
        return this.isPreloadVideoOperation;
    }

    protected void onFail(boolean z, final int i) {
        StringBuilder sb;
        cleanup();
        this.state = i == 1 ? 4 : 2;
        if (this.delegate != null && BuildVars.LOGS_ENABLED) {
            long currentTimeMillis = this.startTime != 0 ? System.currentTimeMillis() - this.startTime : 0L;
            if (i == 1) {
                sb = new StringBuilder();
                sb.append("cancel downloading file to ");
                sb.append(this.cacheFileFinal);
            } else {
                sb = new StringBuilder();
                sb.append("failed downloading file to ");
                sb.append(this.cacheFileFinal);
                sb.append(" reason = ");
                sb.append(i);
            }
            sb.append(" time = ");
            sb.append(currentTimeMillis);
            sb.append(" dc = ");
            sb.append(this.datacenterId);
            sb.append(" size = ");
            sb.append(AndroidUtilities.formatFileSize(this.totalBytesCount));
            FileLog.d(sb.toString());
        }
        if (z) {
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    FileLoadOperation.this.lambda$onFail$23(i);
                }
            });
            return;
        }
        FileLoadOperationDelegate fileLoadOperationDelegate = this.delegate;
        if (fileLoadOperationDelegate != null) {
            fileLoadOperationDelegate.didFailedLoadingFile(this, i);
        }
        notifyStreamListeners();
    }

    public void pause() {
        if (this.state != 1) {
            return;
        }
        this.paused = true;
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda27
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$pause$7();
            }
        });
    }

    /* JADX WARN: Code restructure failed: missing block: B:213:0x0420, code lost:
    
        if (r1 == (r5 - r3)) goto L166;
     */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0252 A[Catch: Exception -> 0x0099, TryCatch #1 {Exception -> 0x0099, blocks: (B:65:0x0087, B:67:0x008b, B:69:0x0095, B:71:0x009c, B:73:0x00a2, B:76:0x00c7, B:78:0x00d1, B:80:0x00d9, B:82:0x00e7, B:84:0x00f5, B:87:0x00fc, B:89:0x0110, B:90:0x0145, B:92:0x0149, B:94:0x016d, B:95:0x0195, B:97:0x0199, B:98:0x01a0, B:100:0x01cb, B:102:0x01dd, B:104:0x01f2, B:105:0x01fb, B:106:0x0207, B:107:0x0211, B:108:0x01fe, B:109:0x020a, B:110:0x0213, B:112:0x0236, B:114:0x023a, B:116:0x0240, B:118:0x0246, B:123:0x0252, B:125:0x05a7, B:127:0x05af, B:129:0x05bb, B:131:0x05c6, B:134:0x05c9, B:136:0x05d5, B:138:0x05db, B:139:0x05ea, B:141:0x05f0, B:142:0x05ff, B:144:0x0605, B:146:0x0615, B:147:0x061d, B:149:0x0622, B:151:0x0627, B:154:0x0261, B:156:0x0265, B:158:0x026f, B:162:0x0281, B:164:0x0285, B:166:0x028a, B:168:0x0290, B:172:0x029c, B:175:0x02d0, B:177:0x02d6, B:179:0x02ef, B:181:0x02f7, B:186:0x030b, B:187:0x0321, B:188:0x0322, B:189:0x0326, B:191:0x032a, B:192:0x0362, B:194:0x0366, B:196:0x0373, B:197:0x03a9, B:199:0x03ce, B:201:0x03e0, B:203:0x03f0, B:206:0x03f8, B:208:0x040d, B:210:0x0414, B:212:0x041c, B:214:0x0432, B:216:0x0442, B:217:0x0453, B:222:0x0464, B:223:0x046b, B:224:0x046c, B:226:0x0479, B:227:0x04bb, B:229:0x04ca, B:231:0x04ce, B:233:0x04d2, B:234:0x0517, B:235:0x0538, B:236:0x051c, B:238:0x0520, B:239:0x053b, B:241:0x0545, B:242:0x0578, B:244:0x057c, B:245:0x0588, B:247:0x0590, B:249:0x0595, B:258:0x055d, B:264:0x02ab, B:268:0x02b5, B:281:0x062d, B:283:0x00a9, B:285:0x00af, B:286:0x00b6, B:288:0x00bc), top: B:64:0x0087 }] */
    /* JADX WARN: Removed duplicated region for block: B:146:0x0615 A[Catch: Exception -> 0x0099, TryCatch #1 {Exception -> 0x0099, blocks: (B:65:0x0087, B:67:0x008b, B:69:0x0095, B:71:0x009c, B:73:0x00a2, B:76:0x00c7, B:78:0x00d1, B:80:0x00d9, B:82:0x00e7, B:84:0x00f5, B:87:0x00fc, B:89:0x0110, B:90:0x0145, B:92:0x0149, B:94:0x016d, B:95:0x0195, B:97:0x0199, B:98:0x01a0, B:100:0x01cb, B:102:0x01dd, B:104:0x01f2, B:105:0x01fb, B:106:0x0207, B:107:0x0211, B:108:0x01fe, B:109:0x020a, B:110:0x0213, B:112:0x0236, B:114:0x023a, B:116:0x0240, B:118:0x0246, B:123:0x0252, B:125:0x05a7, B:127:0x05af, B:129:0x05bb, B:131:0x05c6, B:134:0x05c9, B:136:0x05d5, B:138:0x05db, B:139:0x05ea, B:141:0x05f0, B:142:0x05ff, B:144:0x0605, B:146:0x0615, B:147:0x061d, B:149:0x0622, B:151:0x0627, B:154:0x0261, B:156:0x0265, B:158:0x026f, B:162:0x0281, B:164:0x0285, B:166:0x028a, B:168:0x0290, B:172:0x029c, B:175:0x02d0, B:177:0x02d6, B:179:0x02ef, B:181:0x02f7, B:186:0x030b, B:187:0x0321, B:188:0x0322, B:189:0x0326, B:191:0x032a, B:192:0x0362, B:194:0x0366, B:196:0x0373, B:197:0x03a9, B:199:0x03ce, B:201:0x03e0, B:203:0x03f0, B:206:0x03f8, B:208:0x040d, B:210:0x0414, B:212:0x041c, B:214:0x0432, B:216:0x0442, B:217:0x0453, B:222:0x0464, B:223:0x046b, B:224:0x046c, B:226:0x0479, B:227:0x04bb, B:229:0x04ca, B:231:0x04ce, B:233:0x04d2, B:234:0x0517, B:235:0x0538, B:236:0x051c, B:238:0x0520, B:239:0x053b, B:241:0x0545, B:242:0x0578, B:244:0x057c, B:245:0x0588, B:247:0x0590, B:249:0x0595, B:258:0x055d, B:264:0x02ab, B:268:0x02b5, B:281:0x062d, B:283:0x00a9, B:285:0x00af, B:286:0x00b6, B:288:0x00bc), top: B:64:0x0087 }] */
    /* JADX WARN: Removed duplicated region for block: B:147:0x061d A[Catch: Exception -> 0x0099, TryCatch #1 {Exception -> 0x0099, blocks: (B:65:0x0087, B:67:0x008b, B:69:0x0095, B:71:0x009c, B:73:0x00a2, B:76:0x00c7, B:78:0x00d1, B:80:0x00d9, B:82:0x00e7, B:84:0x00f5, B:87:0x00fc, B:89:0x0110, B:90:0x0145, B:92:0x0149, B:94:0x016d, B:95:0x0195, B:97:0x0199, B:98:0x01a0, B:100:0x01cb, B:102:0x01dd, B:104:0x01f2, B:105:0x01fb, B:106:0x0207, B:107:0x0211, B:108:0x01fe, B:109:0x020a, B:110:0x0213, B:112:0x0236, B:114:0x023a, B:116:0x0240, B:118:0x0246, B:123:0x0252, B:125:0x05a7, B:127:0x05af, B:129:0x05bb, B:131:0x05c6, B:134:0x05c9, B:136:0x05d5, B:138:0x05db, B:139:0x05ea, B:141:0x05f0, B:142:0x05ff, B:144:0x0605, B:146:0x0615, B:147:0x061d, B:149:0x0622, B:151:0x0627, B:154:0x0261, B:156:0x0265, B:158:0x026f, B:162:0x0281, B:164:0x0285, B:166:0x028a, B:168:0x0290, B:172:0x029c, B:175:0x02d0, B:177:0x02d6, B:179:0x02ef, B:181:0x02f7, B:186:0x030b, B:187:0x0321, B:188:0x0322, B:189:0x0326, B:191:0x032a, B:192:0x0362, B:194:0x0366, B:196:0x0373, B:197:0x03a9, B:199:0x03ce, B:201:0x03e0, B:203:0x03f0, B:206:0x03f8, B:208:0x040d, B:210:0x0414, B:212:0x041c, B:214:0x0432, B:216:0x0442, B:217:0x0453, B:222:0x0464, B:223:0x046b, B:224:0x046c, B:226:0x0479, B:227:0x04bb, B:229:0x04ca, B:231:0x04ce, B:233:0x04d2, B:234:0x0517, B:235:0x0538, B:236:0x051c, B:238:0x0520, B:239:0x053b, B:241:0x0545, B:242:0x0578, B:244:0x057c, B:245:0x0588, B:247:0x0590, B:249:0x0595, B:258:0x055d, B:264:0x02ab, B:268:0x02b5, B:281:0x062d, B:283:0x00a9, B:285:0x00af, B:286:0x00b6, B:288:0x00bc), top: B:64:0x0087 }] */
    /* JADX WARN: Removed duplicated region for block: B:154:0x0261 A[Catch: Exception -> 0x0099, TryCatch #1 {Exception -> 0x0099, blocks: (B:65:0x0087, B:67:0x008b, B:69:0x0095, B:71:0x009c, B:73:0x00a2, B:76:0x00c7, B:78:0x00d1, B:80:0x00d9, B:82:0x00e7, B:84:0x00f5, B:87:0x00fc, B:89:0x0110, B:90:0x0145, B:92:0x0149, B:94:0x016d, B:95:0x0195, B:97:0x0199, B:98:0x01a0, B:100:0x01cb, B:102:0x01dd, B:104:0x01f2, B:105:0x01fb, B:106:0x0207, B:107:0x0211, B:108:0x01fe, B:109:0x020a, B:110:0x0213, B:112:0x0236, B:114:0x023a, B:116:0x0240, B:118:0x0246, B:123:0x0252, B:125:0x05a7, B:127:0x05af, B:129:0x05bb, B:131:0x05c6, B:134:0x05c9, B:136:0x05d5, B:138:0x05db, B:139:0x05ea, B:141:0x05f0, B:142:0x05ff, B:144:0x0605, B:146:0x0615, B:147:0x061d, B:149:0x0622, B:151:0x0627, B:154:0x0261, B:156:0x0265, B:158:0x026f, B:162:0x0281, B:164:0x0285, B:166:0x028a, B:168:0x0290, B:172:0x029c, B:175:0x02d0, B:177:0x02d6, B:179:0x02ef, B:181:0x02f7, B:186:0x030b, B:187:0x0321, B:188:0x0322, B:189:0x0326, B:191:0x032a, B:192:0x0362, B:194:0x0366, B:196:0x0373, B:197:0x03a9, B:199:0x03ce, B:201:0x03e0, B:203:0x03f0, B:206:0x03f8, B:208:0x040d, B:210:0x0414, B:212:0x041c, B:214:0x0432, B:216:0x0442, B:217:0x0453, B:222:0x0464, B:223:0x046b, B:224:0x046c, B:226:0x0479, B:227:0x04bb, B:229:0x04ca, B:231:0x04ce, B:233:0x04d2, B:234:0x0517, B:235:0x0538, B:236:0x051c, B:238:0x0520, B:239:0x053b, B:241:0x0545, B:242:0x0578, B:244:0x057c, B:245:0x0588, B:247:0x0590, B:249:0x0595, B:258:0x055d, B:264:0x02ab, B:268:0x02b5, B:281:0x062d, B:283:0x00a9, B:285:0x00af, B:286:0x00b6, B:288:0x00bc), top: B:64:0x0087 }] */
    /* JADX WARN: Removed duplicated region for block: B:174:0x02a7  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x02d6 A[Catch: Exception -> 0x0099, TryCatch #1 {Exception -> 0x0099, blocks: (B:65:0x0087, B:67:0x008b, B:69:0x0095, B:71:0x009c, B:73:0x00a2, B:76:0x00c7, B:78:0x00d1, B:80:0x00d9, B:82:0x00e7, B:84:0x00f5, B:87:0x00fc, B:89:0x0110, B:90:0x0145, B:92:0x0149, B:94:0x016d, B:95:0x0195, B:97:0x0199, B:98:0x01a0, B:100:0x01cb, B:102:0x01dd, B:104:0x01f2, B:105:0x01fb, B:106:0x0207, B:107:0x0211, B:108:0x01fe, B:109:0x020a, B:110:0x0213, B:112:0x0236, B:114:0x023a, B:116:0x0240, B:118:0x0246, B:123:0x0252, B:125:0x05a7, B:127:0x05af, B:129:0x05bb, B:131:0x05c6, B:134:0x05c9, B:136:0x05d5, B:138:0x05db, B:139:0x05ea, B:141:0x05f0, B:142:0x05ff, B:144:0x0605, B:146:0x0615, B:147:0x061d, B:149:0x0622, B:151:0x0627, B:154:0x0261, B:156:0x0265, B:158:0x026f, B:162:0x0281, B:164:0x0285, B:166:0x028a, B:168:0x0290, B:172:0x029c, B:175:0x02d0, B:177:0x02d6, B:179:0x02ef, B:181:0x02f7, B:186:0x030b, B:187:0x0321, B:188:0x0322, B:189:0x0326, B:191:0x032a, B:192:0x0362, B:194:0x0366, B:196:0x0373, B:197:0x03a9, B:199:0x03ce, B:201:0x03e0, B:203:0x03f0, B:206:0x03f8, B:208:0x040d, B:210:0x0414, B:212:0x041c, B:214:0x0432, B:216:0x0442, B:217:0x0453, B:222:0x0464, B:223:0x046b, B:224:0x046c, B:226:0x0479, B:227:0x04bb, B:229:0x04ca, B:231:0x04ce, B:233:0x04d2, B:234:0x0517, B:235:0x0538, B:236:0x051c, B:238:0x0520, B:239:0x053b, B:241:0x0545, B:242:0x0578, B:244:0x057c, B:245:0x0588, B:247:0x0590, B:249:0x0595, B:258:0x055d, B:264:0x02ab, B:268:0x02b5, B:281:0x062d, B:283:0x00a9, B:285:0x00af, B:286:0x00b6, B:288:0x00bc), top: B:64:0x0087 }] */
    /* JADX WARN: Removed duplicated region for block: B:191:0x032a A[Catch: Exception -> 0x0099, TryCatch #1 {Exception -> 0x0099, blocks: (B:65:0x0087, B:67:0x008b, B:69:0x0095, B:71:0x009c, B:73:0x00a2, B:76:0x00c7, B:78:0x00d1, B:80:0x00d9, B:82:0x00e7, B:84:0x00f5, B:87:0x00fc, B:89:0x0110, B:90:0x0145, B:92:0x0149, B:94:0x016d, B:95:0x0195, B:97:0x0199, B:98:0x01a0, B:100:0x01cb, B:102:0x01dd, B:104:0x01f2, B:105:0x01fb, B:106:0x0207, B:107:0x0211, B:108:0x01fe, B:109:0x020a, B:110:0x0213, B:112:0x0236, B:114:0x023a, B:116:0x0240, B:118:0x0246, B:123:0x0252, B:125:0x05a7, B:127:0x05af, B:129:0x05bb, B:131:0x05c6, B:134:0x05c9, B:136:0x05d5, B:138:0x05db, B:139:0x05ea, B:141:0x05f0, B:142:0x05ff, B:144:0x0605, B:146:0x0615, B:147:0x061d, B:149:0x0622, B:151:0x0627, B:154:0x0261, B:156:0x0265, B:158:0x026f, B:162:0x0281, B:164:0x0285, B:166:0x028a, B:168:0x0290, B:172:0x029c, B:175:0x02d0, B:177:0x02d6, B:179:0x02ef, B:181:0x02f7, B:186:0x030b, B:187:0x0321, B:188:0x0322, B:189:0x0326, B:191:0x032a, B:192:0x0362, B:194:0x0366, B:196:0x0373, B:197:0x03a9, B:199:0x03ce, B:201:0x03e0, B:203:0x03f0, B:206:0x03f8, B:208:0x040d, B:210:0x0414, B:212:0x041c, B:214:0x0432, B:216:0x0442, B:217:0x0453, B:222:0x0464, B:223:0x046b, B:224:0x046c, B:226:0x0479, B:227:0x04bb, B:229:0x04ca, B:231:0x04ce, B:233:0x04d2, B:234:0x0517, B:235:0x0538, B:236:0x051c, B:238:0x0520, B:239:0x053b, B:241:0x0545, B:242:0x0578, B:244:0x057c, B:245:0x0588, B:247:0x0590, B:249:0x0595, B:258:0x055d, B:264:0x02ab, B:268:0x02b5, B:281:0x062d, B:283:0x00a9, B:285:0x00af, B:286:0x00b6, B:288:0x00bc), top: B:64:0x0087 }] */
    /* JADX WARN: Removed duplicated region for block: B:194:0x0366 A[Catch: Exception -> 0x0099, TryCatch #1 {Exception -> 0x0099, blocks: (B:65:0x0087, B:67:0x008b, B:69:0x0095, B:71:0x009c, B:73:0x00a2, B:76:0x00c7, B:78:0x00d1, B:80:0x00d9, B:82:0x00e7, B:84:0x00f5, B:87:0x00fc, B:89:0x0110, B:90:0x0145, B:92:0x0149, B:94:0x016d, B:95:0x0195, B:97:0x0199, B:98:0x01a0, B:100:0x01cb, B:102:0x01dd, B:104:0x01f2, B:105:0x01fb, B:106:0x0207, B:107:0x0211, B:108:0x01fe, B:109:0x020a, B:110:0x0213, B:112:0x0236, B:114:0x023a, B:116:0x0240, B:118:0x0246, B:123:0x0252, B:125:0x05a7, B:127:0x05af, B:129:0x05bb, B:131:0x05c6, B:134:0x05c9, B:136:0x05d5, B:138:0x05db, B:139:0x05ea, B:141:0x05f0, B:142:0x05ff, B:144:0x0605, B:146:0x0615, B:147:0x061d, B:149:0x0622, B:151:0x0627, B:154:0x0261, B:156:0x0265, B:158:0x026f, B:162:0x0281, B:164:0x0285, B:166:0x028a, B:168:0x0290, B:172:0x029c, B:175:0x02d0, B:177:0x02d6, B:179:0x02ef, B:181:0x02f7, B:186:0x030b, B:187:0x0321, B:188:0x0322, B:189:0x0326, B:191:0x032a, B:192:0x0362, B:194:0x0366, B:196:0x0373, B:197:0x03a9, B:199:0x03ce, B:201:0x03e0, B:203:0x03f0, B:206:0x03f8, B:208:0x040d, B:210:0x0414, B:212:0x041c, B:214:0x0432, B:216:0x0442, B:217:0x0453, B:222:0x0464, B:223:0x046b, B:224:0x046c, B:226:0x0479, B:227:0x04bb, B:229:0x04ca, B:231:0x04ce, B:233:0x04d2, B:234:0x0517, B:235:0x0538, B:236:0x051c, B:238:0x0520, B:239:0x053b, B:241:0x0545, B:242:0x0578, B:244:0x057c, B:245:0x0588, B:247:0x0590, B:249:0x0595, B:258:0x055d, B:264:0x02ab, B:268:0x02b5, B:281:0x062d, B:283:0x00a9, B:285:0x00af, B:286:0x00b6, B:288:0x00bc), top: B:64:0x0087 }] */
    /* JADX WARN: Removed duplicated region for block: B:199:0x03ce A[Catch: Exception -> 0x0099, TryCatch #1 {Exception -> 0x0099, blocks: (B:65:0x0087, B:67:0x008b, B:69:0x0095, B:71:0x009c, B:73:0x00a2, B:76:0x00c7, B:78:0x00d1, B:80:0x00d9, B:82:0x00e7, B:84:0x00f5, B:87:0x00fc, B:89:0x0110, B:90:0x0145, B:92:0x0149, B:94:0x016d, B:95:0x0195, B:97:0x0199, B:98:0x01a0, B:100:0x01cb, B:102:0x01dd, B:104:0x01f2, B:105:0x01fb, B:106:0x0207, B:107:0x0211, B:108:0x01fe, B:109:0x020a, B:110:0x0213, B:112:0x0236, B:114:0x023a, B:116:0x0240, B:118:0x0246, B:123:0x0252, B:125:0x05a7, B:127:0x05af, B:129:0x05bb, B:131:0x05c6, B:134:0x05c9, B:136:0x05d5, B:138:0x05db, B:139:0x05ea, B:141:0x05f0, B:142:0x05ff, B:144:0x0605, B:146:0x0615, B:147:0x061d, B:149:0x0622, B:151:0x0627, B:154:0x0261, B:156:0x0265, B:158:0x026f, B:162:0x0281, B:164:0x0285, B:166:0x028a, B:168:0x0290, B:172:0x029c, B:175:0x02d0, B:177:0x02d6, B:179:0x02ef, B:181:0x02f7, B:186:0x030b, B:187:0x0321, B:188:0x0322, B:189:0x0326, B:191:0x032a, B:192:0x0362, B:194:0x0366, B:196:0x0373, B:197:0x03a9, B:199:0x03ce, B:201:0x03e0, B:203:0x03f0, B:206:0x03f8, B:208:0x040d, B:210:0x0414, B:212:0x041c, B:214:0x0432, B:216:0x0442, B:217:0x0453, B:222:0x0464, B:223:0x046b, B:224:0x046c, B:226:0x0479, B:227:0x04bb, B:229:0x04ca, B:231:0x04ce, B:233:0x04d2, B:234:0x0517, B:235:0x0538, B:236:0x051c, B:238:0x0520, B:239:0x053b, B:241:0x0545, B:242:0x0578, B:244:0x057c, B:245:0x0588, B:247:0x0590, B:249:0x0595, B:258:0x055d, B:264:0x02ab, B:268:0x02b5, B:281:0x062d, B:283:0x00a9, B:285:0x00af, B:286:0x00b6, B:288:0x00bc), top: B:64:0x0087 }] */
    /* JADX WARN: Removed duplicated region for block: B:244:0x057c A[Catch: Exception -> 0x0099, TryCatch #1 {Exception -> 0x0099, blocks: (B:65:0x0087, B:67:0x008b, B:69:0x0095, B:71:0x009c, B:73:0x00a2, B:76:0x00c7, B:78:0x00d1, B:80:0x00d9, B:82:0x00e7, B:84:0x00f5, B:87:0x00fc, B:89:0x0110, B:90:0x0145, B:92:0x0149, B:94:0x016d, B:95:0x0195, B:97:0x0199, B:98:0x01a0, B:100:0x01cb, B:102:0x01dd, B:104:0x01f2, B:105:0x01fb, B:106:0x0207, B:107:0x0211, B:108:0x01fe, B:109:0x020a, B:110:0x0213, B:112:0x0236, B:114:0x023a, B:116:0x0240, B:118:0x0246, B:123:0x0252, B:125:0x05a7, B:127:0x05af, B:129:0x05bb, B:131:0x05c6, B:134:0x05c9, B:136:0x05d5, B:138:0x05db, B:139:0x05ea, B:141:0x05f0, B:142:0x05ff, B:144:0x0605, B:146:0x0615, B:147:0x061d, B:149:0x0622, B:151:0x0627, B:154:0x0261, B:156:0x0265, B:158:0x026f, B:162:0x0281, B:164:0x0285, B:166:0x028a, B:168:0x0290, B:172:0x029c, B:175:0x02d0, B:177:0x02d6, B:179:0x02ef, B:181:0x02f7, B:186:0x030b, B:187:0x0321, B:188:0x0322, B:189:0x0326, B:191:0x032a, B:192:0x0362, B:194:0x0366, B:196:0x0373, B:197:0x03a9, B:199:0x03ce, B:201:0x03e0, B:203:0x03f0, B:206:0x03f8, B:208:0x040d, B:210:0x0414, B:212:0x041c, B:214:0x0432, B:216:0x0442, B:217:0x0453, B:222:0x0464, B:223:0x046b, B:224:0x046c, B:226:0x0479, B:227:0x04bb, B:229:0x04ca, B:231:0x04ce, B:233:0x04d2, B:234:0x0517, B:235:0x0538, B:236:0x051c, B:238:0x0520, B:239:0x053b, B:241:0x0545, B:242:0x0578, B:244:0x057c, B:245:0x0588, B:247:0x0590, B:249:0x0595, B:258:0x055d, B:264:0x02ab, B:268:0x02b5, B:281:0x062d, B:283:0x00a9, B:285:0x00af, B:286:0x00b6, B:288:0x00bc), top: B:64:0x0087 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected boolean processRequestResult(RequestInfo requestInfo, TLRPC.TL_error tL_error) {
        NativeByteBuffer nativeByteBuffer;
        NativeByteBuffer nativeByteBuffer2;
        String str;
        boolean z;
        long j;
        boolean z2;
        byte[] bArr;
        boolean z3;
        boolean z4;
        RandomAccessFile randomAccessFile;
        boolean z5;
        long j2;
        StringBuilder sb;
        boolean z6;
        int i;
        int i2;
        long j3;
        int i3;
        StringBuilder sb2;
        Integer num;
        boolean z7 = false;
        if (this.state != 1 && this.state != 5) {
            if (BuildVars.DEBUG_VERSION && this.state == 3) {
                FileLog.e(new FileLog.IgnoreSentException("trying to write to finished file " + this.fileName + " offset " + requestInfo.offset + " " + this.totalBytesCount + " reqToken=" + requestInfo.requestToken + " (state=" + this.state + ")"));
            }
            return false;
        }
        final int i4 = requestInfo.requestToken;
        this.requestInfos.remove(requestInfo);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$processRequestResult$22(i4);
            }
        });
        String str2 = " secret = ";
        if (tL_error != null) {
            if (!tL_error.text.contains("LIMIT_INVALID") || requestInfo.forceSmallChunk) {
                if (tL_error.text.contains("FILE_MIGRATE_")) {
                    Scanner scanner = new Scanner(tL_error.text.replace("FILE_MIGRATE_", ""));
                    scanner.useDelimiter("");
                    try {
                        num = Integer.valueOf(scanner.nextInt());
                    } catch (Exception unused) {
                        num = null;
                    }
                    if (num != null) {
                        this.datacenterId = num.intValue();
                        this.downloadedBytes = 0L;
                        this.requestedBytesCount = 0L;
                    }
                } else if (tL_error.text.contains("OFFSET_INVALID")) {
                    if (this.downloadedBytes % this.currentDownloadChunkSize == 0) {
                        try {
                            onFinishLoadingFile(true, 0, false);
                        } catch (Exception e) {
                            FileLog.e(e);
                            onFail(false, 0);
                        }
                        return false;
                    }
                } else {
                    if (tL_error.text.contains("RETRY_LIMIT")) {
                        onFail(false, 2);
                        return false;
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        TLRPC.InputFileLocation inputFileLocation = this.location;
                        if (inputFileLocation != null) {
                            if (inputFileLocation instanceof TLRPC.TL_inputPeerPhotoFileLocation) {
                                sb2 = new StringBuilder();
                                sb2.append(tL_error.text);
                                sb2.append(" ");
                                sb2.append(this.location);
                                sb2.append(" peer_did = ");
                                sb2.append(DialogObject.getPeerDialogId(((TLRPC.TL_inputPeerPhotoFileLocation) this.location).peer));
                                sb2.append(" peer_access_hash=");
                                sb2.append(((TLRPC.TL_inputPeerPhotoFileLocation) this.location).peer.access_hash);
                                sb2.append(" photo_id=");
                                sb2.append(((TLRPC.TL_inputPeerPhotoFileLocation) this.location).photo_id);
                                sb2.append(" big=");
                                sb2.append(((TLRPC.TL_inputPeerPhotoFileLocation) this.location).big);
                            } else {
                                sb2 = new StringBuilder();
                                sb2.append(tL_error.text);
                                sb2.append(" ");
                                sb2.append(this.location);
                                sb2.append(" id = ");
                                sb2.append(this.location.id);
                                sb2.append(" local_id = ");
                                sb2.append(this.location.local_id);
                                sb2.append(" access_hash = ");
                                sb2.append(this.location.access_hash);
                                sb2.append(" volume_id = ");
                                sb2.append(this.location.volume_id);
                                sb2.append(str2);
                                sb2.append(this.location.secret);
                            }
                        } else if (this.webLocation != null) {
                            sb2 = new StringBuilder();
                            sb2.append(tL_error.text);
                            sb2.append(" ");
                            sb2.append(this.webLocation);
                            sb2.append(" id = ");
                            sb2.append(this.fileName);
                        }
                        FileLog.e(sb2.toString());
                    }
                }
                onFail(false, 0);
                return false;
            }
            Runnable runnable = requestInfo.whenCancelled;
            if (runnable != null) {
                runnable.run();
            }
            removePart(this.notRequestedBytesRanges, requestInfo.offset, requestInfo.offset + requestInfo.chunkSize);
            if (!this.forceSmallChunk) {
                this.forceSmallChunk = true;
                this.currentDownloadChunkSize = 32768;
                this.currentMaxDownloadRequests = 4;
            }
            startDownloadRequest(requestInfo.connectionType);
            return false;
        }
        try {
        } catch (Exception e2) {
            FileLog.e(e2, (AndroidUtilities.isFilNotFoundException(e2) || AndroidUtilities.isENOSPC(e2)) ? false : true);
            if (AndroidUtilities.isENOSPC(e2)) {
                onFail(false, -1);
                return false;
            }
            if (AndroidUtilities.isEROFS(e2)) {
                SharedConfig.checkSdCard(this.cacheFileFinal);
                onFail(true, -1);
            }
        }
        if (this.notLoadedBytesRanges == null && this.downloadedBytes != requestInfo.offset) {
            delayRequestInfo(requestInfo);
            return false;
        }
        if (requestInfo.response != null) {
            nativeByteBuffer2 = requestInfo.response.bytes;
        } else if (requestInfo.responseWeb != null) {
            nativeByteBuffer2 = requestInfo.responseWeb.bytes;
        } else {
            if (requestInfo.responseCdn == null) {
                nativeByteBuffer = null;
                if (nativeByteBuffer != null || nativeByteBuffer.limit() == 0) {
                    onFinishLoadingFile(true, 0, false);
                    return false;
                }
                int limit = nativeByteBuffer.limit();
                if (this.isCdn) {
                    long j4 = requestInfo.offset;
                    long j5 = this.cdnChunkCheckSize;
                    long j6 = (j4 / j5) * j5;
                    HashMap<Long, TLRPC.TL_fileHash> hashMap = this.cdnHashes;
                    if ((hashMap != null ? hashMap.get(Long.valueOf(j6)) : null) == null) {
                        delayRequestInfo(requestInfo);
                        requestFileOffsets(j6);
                        return true;
                    }
                }
                if (requestInfo.responseCdn != null) {
                    long j7 = requestInfo.offset / 16;
                    byte[] bArr2 = this.cdnIv;
                    bArr2[15] = (byte) (j7 & 255);
                    bArr2[14] = (byte) ((j7 >> 8) & 255);
                    bArr2[13] = (byte) ((j7 >> 16) & 255);
                    bArr2[12] = (byte) ((j7 >> 24) & 255);
                    Utilities.aesCtrDecryption(nativeByteBuffer.buffer, this.cdnKey, bArr2, 0, nativeByteBuffer.limit());
                }
                if (this.isPreloadVideoOperation) {
                    this.preloadStream.writeLong(requestInfo.offset);
                    long j8 = limit;
                    this.preloadStream.writeLong(j8);
                    this.preloadStreamFileOffset += 16;
                    this.preloadStream.getChannel().write(nativeByteBuffer.buffer);
                    if (BuildVars.DEBUG_VERSION) {
                        FileLog.d("save preload file part " + this.cacheFilePreload + " offset " + requestInfo.offset + " size " + limit);
                    }
                    if (this.preloadedBytesRanges == null) {
                        this.preloadedBytesRanges = new HashMap<>();
                    }
                    this.preloadedBytesRanges.put(Long.valueOf(requestInfo.offset), new PreloadRange(this.preloadStreamFileOffset, j8));
                    this.totalPreloadedBytes += limit;
                    this.preloadStreamFileOffset += limit;
                    if (this.moovFound == 0) {
                        long findNextPreloadDownloadOffset = findNextPreloadDownloadOffset(this.nextAtomOffset, requestInfo.offset, nativeByteBuffer);
                        if (findNextPreloadDownloadOffset < 0) {
                            j3 = -1;
                            findNextPreloadDownloadOffset *= -1;
                            long j9 = this.nextPreloadDownloadOffset + this.currentDownloadChunkSize;
                            this.nextPreloadDownloadOffset = j9;
                            if (j9 < this.totalBytesCount / 2) {
                                long j10 = 1048576 + findNextPreloadDownloadOffset;
                                this.foundMoovSize = j10;
                                this.preloadNotRequestedBytesCount = j10;
                                i3 = 1;
                            } else {
                                this.foundMoovSize = 2097152L;
                                this.preloadNotRequestedBytesCount = 2097152L;
                                i3 = 2;
                            }
                            this.moovFound = i3;
                        } else {
                            j3 = this.nextPreloadDownloadOffset + this.currentDownloadChunkSize;
                        }
                        this.nextPreloadDownloadOffset = j3;
                        this.nextAtomOffset = findNextPreloadDownloadOffset;
                    }
                    this.preloadStream.writeLong(this.foundMoovSize);
                    this.preloadStream.writeLong(this.nextPreloadDownloadOffset);
                    this.preloadStream.writeLong(this.nextAtomOffset);
                    this.preloadStreamFileOffset += 24;
                    long j11 = this.nextPreloadDownloadOffset;
                    if (j11 != 0 && ((this.moovFound == 0 || this.foundMoovSize >= 0) && this.totalPreloadedBytes <= preloadMaxBytes && j11 < this.totalBytesCount)) {
                        z5 = false;
                        if (!z5) {
                            this.preloadStream.seek(0L);
                            this.preloadStream.write(1);
                        } else if (this.moovFound != 0) {
                            this.foundMoovSize -= this.currentDownloadChunkSize;
                        }
                    }
                    z5 = true;
                    if (!z5) {
                    }
                } else {
                    long j12 = limit;
                    long j13 = this.downloadedBytes + j12;
                    this.downloadedBytes = j13;
                    long j14 = this.totalBytesCount;
                    if (j14 > 0) {
                        if (j13 < j14 && ((i = this.preloadPrefixSize) <= 0 || j13 < i || !canFinishPreload() || !this.requestInfos.isEmpty())) {
                            z6 = false;
                            z = z6;
                            str = " volume_id = ";
                            if (this.downloadedBytes < this.totalBytesCount) {
                                z2 = true;
                                boolean z8 = BuildVars.DEBUG_VERSION;
                                bArr = this.key;
                                if (bArr != null) {
                                    Utilities.aesIgeEncryption(nativeByteBuffer.buffer, bArr, this.iv, false, true, 0, nativeByteBuffer.limit());
                                    if (z && this.bytesCountPadding != 0) {
                                        long limit2 = nativeByteBuffer.limit() - this.bytesCountPadding;
                                        if (BuildVars.DEBUG_VERSION && limit2 > 2147483647L) {
                                            throw new RuntimeException("Out of limit" + limit2);
                                        }
                                        nativeByteBuffer.limit((int) limit2);
                                    }
                                }
                                if (this.encryptFile) {
                                    long j15 = requestInfo.offset / 16;
                                    byte[] bArr3 = this.encryptIv;
                                    bArr3[15] = (byte) (j15 & 255);
                                    bArr3[14] = (byte) ((j15 >> 8) & 255);
                                    bArr3[13] = (byte) ((j15 >> 16) & 255);
                                    bArr3[12] = (byte) ((j15 >> 24) & 255);
                                    Utilities.aesCtrDecryption(nativeByteBuffer.buffer, this.encryptKey, bArr3, 0, nativeByteBuffer.limit());
                                }
                                if (this.notLoadedBytesRanges != null) {
                                    this.fileOutputStream.seek(requestInfo.offset);
                                    if (BuildVars.DEBUG_VERSION) {
                                        FileLog.d("save file part " + this.fileName + " offset=" + requestInfo.offset + " chunk_size=" + this.currentDownloadChunkSize + " isCdn=" + this.isCdn);
                                    }
                                }
                                this.fileOutputStream.getChannel().write(nativeByteBuffer.buffer);
                                addPart(this.notLoadedBytesRanges, requestInfo.offset, j12 + requestInfo.offset, true);
                                if (this.isCdn) {
                                    long j16 = requestInfo.offset / this.cdnChunkCheckSize;
                                    int size = this.notCheckedCdnRanges.size();
                                    int i5 = 0;
                                    while (true) {
                                        if (i5 >= size) {
                                            break;
                                        }
                                        Range range = this.notCheckedCdnRanges.get(i5);
                                        if (range.start > j16 || j16 > range.end) {
                                            i5++;
                                            str = str;
                                            str2 = str2;
                                            z = z;
                                            z2 = z2;
                                        } else {
                                            long j17 = this.cdnChunkCheckSize;
                                            long j18 = j16 * j17;
                                            long downloadedLengthFromOffsetInternal = getDownloadedLengthFromOffsetInternal(this.notLoadedBytesRanges, j18, j17);
                                            if (downloadedLengthFromOffsetInternal != 0) {
                                                if (downloadedLengthFromOffsetInternal != this.cdnChunkCheckSize) {
                                                    long j19 = this.totalBytesCount;
                                                    j2 = j18;
                                                    if (j19 > 0) {
                                                    }
                                                    if (j19 <= 0 && z) {
                                                    }
                                                } else {
                                                    j2 = j18;
                                                }
                                                TLRPC.TL_fileHash tL_fileHash = this.cdnHashes.get(Long.valueOf(j2));
                                                if (this.fileReadStream == null) {
                                                    this.cdnCheckBytes = new byte[this.cdnChunkCheckSize];
                                                    this.fileReadStream = new RandomAccessFile(this.cacheFileTemp, "r");
                                                }
                                                this.fileReadStream.seek(j2);
                                                if (BuildVars.DEBUG_VERSION && downloadedLengthFromOffsetInternal > 2147483647L) {
                                                    throw new RuntimeException("!!!");
                                                }
                                                this.fileReadStream.readFully(this.cdnCheckBytes, 0, (int) downloadedLengthFromOffsetInternal);
                                                if (this.encryptFile) {
                                                    long j20 = j2 / 16;
                                                    byte[] bArr4 = this.encryptIv;
                                                    z3 = z;
                                                    z4 = z2;
                                                    bArr4[15] = (byte) (j20 & 255);
                                                    bArr4[14] = (byte) ((j20 >> 8) & 255);
                                                    bArr4[13] = (byte) ((j20 >> 16) & 255);
                                                    bArr4[12] = (byte) ((j20 >> 24) & 255);
                                                    Utilities.aesCtrDecryptionByteArray(this.cdnCheckBytes, this.encryptKey, bArr4, 0, downloadedLengthFromOffsetInternal, 0);
                                                } else {
                                                    z3 = z;
                                                    z4 = z2;
                                                }
                                                if (!Arrays.equals(Utilities.computeSHA256(this.cdnCheckBytes, 0, downloadedLengthFromOffsetInternal), tL_fileHash.hash)) {
                                                    if (BuildVars.LOGS_ENABLED) {
                                                        if (this.location != null) {
                                                            sb = new StringBuilder();
                                                            sb.append("invalid cdn hash ");
                                                            sb.append(this.location);
                                                            sb.append(" id = ");
                                                            sb.append(this.location.id);
                                                            sb.append(" local_id = ");
                                                            sb.append(this.location.local_id);
                                                            sb.append(" access_hash = ");
                                                            sb.append(this.location.access_hash);
                                                            sb.append(str);
                                                            sb.append(this.location.volume_id);
                                                            sb.append(str2);
                                                            sb.append(this.location.secret);
                                                        } else if (this.webLocation != null) {
                                                            sb = new StringBuilder();
                                                            sb.append("invalid cdn hash  ");
                                                            sb.append(this.webLocation);
                                                            sb.append(" id = ");
                                                            sb.append(this.fileName);
                                                        }
                                                        FileLog.e(sb.toString());
                                                    }
                                                    onFail(false, 0);
                                                    this.cacheFileTemp.delete();
                                                    return false;
                                                }
                                                this.cdnHashes.remove(Long.valueOf(j2));
                                                addPart(this.notCheckedCdnRanges, j16, j16 + 1, false);
                                            }
                                        }
                                    }
                                }
                                z3 = z;
                                z4 = z2;
                                randomAccessFile = this.fiv;
                                if (randomAccessFile != null) {
                                    randomAccessFile.seek(0L);
                                    this.fiv.write(this.iv);
                                }
                                if (this.totalBytesCount > 0 && this.state == 1) {
                                    copyNotLoadedRanges();
                                    this.delegate.didChangedLoadProgress(this, this.downloadedBytes, this.totalBytesCount);
                                }
                                z5 = z3;
                                z7 = z4;
                            }
                        }
                        z6 = true;
                        z = z6;
                        str = " volume_id = ";
                        if (this.downloadedBytes < this.totalBytesCount) {
                        }
                    } else {
                        int i6 = this.currentDownloadChunkSize;
                        if (limit == i6) {
                            str = " volume_id = ";
                            if (j14 != j13) {
                                j = 0;
                                if (j13 % i6 != 0) {
                                }
                                z = false;
                            } else {
                                j = 0;
                            }
                            if (j14 > j) {
                                if (j14 <= j13) {
                                }
                                z = false;
                            }
                        } else {
                            str = " volume_id = ";
                        }
                        z = true;
                    }
                    z2 = false;
                    boolean z82 = BuildVars.DEBUG_VERSION;
                    bArr = this.key;
                    if (bArr != null) {
                    }
                    if (this.encryptFile) {
                    }
                    if (this.notLoadedBytesRanges != null) {
                    }
                    this.fileOutputStream.getChannel().write(nativeByteBuffer.buffer);
                    addPart(this.notLoadedBytesRanges, requestInfo.offset, j12 + requestInfo.offset, true);
                    if (this.isCdn) {
                    }
                    z3 = z;
                    z4 = z2;
                    randomAccessFile = this.fiv;
                    if (randomAccessFile != null) {
                    }
                    if (this.totalBytesCount > 0) {
                        copyNotLoadedRanges();
                        this.delegate.didChangedLoadProgress(this, this.downloadedBytes, this.totalBytesCount);
                    }
                    z5 = z3;
                    z7 = z4;
                }
                while (i2 < this.delayedRequestInfos.size()) {
                    RequestInfo requestInfo2 = this.delayedRequestInfos.get(i2);
                    i2 = (this.notLoadedBytesRanges == null && this.downloadedBytes != requestInfo2.offset) ? i2 + 1 : 0;
                    this.delayedRequestInfos.remove(i2);
                    if (!processRequestResult(requestInfo2, null)) {
                        if (requestInfo2.response != null) {
                            requestInfo2.response.disableFree = false;
                            requestInfo2.response.freeResources();
                        } else if (requestInfo2.responseWeb != null) {
                            requestInfo2.responseWeb.disableFree = false;
                            requestInfo2.responseWeb.freeResources();
                        } else if (requestInfo2.responseCdn != null) {
                            requestInfo2.responseCdn.disableFree = false;
                            requestInfo2.responseCdn.freeResources();
                        }
                    }
                    if (!z5) {
                        onFinishLoadingFile(true, 0, z7);
                    } else if (this.state != 4 && this.state != 5) {
                        startDownloadRequest(requestInfo.connectionType);
                    }
                    return false;
                }
                if (!z5) {
                }
                return false;
            }
            nativeByteBuffer2 = requestInfo.responseCdn.bytes;
        }
        nativeByteBuffer = nativeByteBuffer2;
        if (nativeByteBuffer != null) {
        }
        onFinishLoadingFile(true, 0, false);
        return false;
    }

    protected void removeStreamListener(final FileLoadOperationStream fileLoadOperationStream) {
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$removeStreamListener$5(fileLoadOperationStream);
            }
        });
    }

    public void setDelegate(FileLoadOperationDelegate fileLoadOperationDelegate) {
        this.delegate = fileLoadOperationDelegate;
    }

    public void setEncryptFile(boolean z) {
        this.encryptFile = z;
        if (z) {
            this.allowDisordererFileSave = false;
        }
    }

    public void setForceRequest(boolean z) {
        this.isForceRequest = z;
    }

    public void setIsPreloadVideoOperation(final boolean z) {
        if (this.isPreloadVideoOperation != z) {
            if (!z || this.totalBytesCount > 2097152) {
                FileLog.e("setIsPreloadVideoOperation " + z + " file=" + this.fileName);
                if (!z && this.isPreloadVideoOperation) {
                    if (this.state == 3) {
                        this.isPreloadVideoOperation = z;
                        this.state = 0;
                        this.preloadFinished = false;
                        start();
                        return;
                    }
                    if (this.state == 1) {
                        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda13
                            @Override // java.lang.Runnable
                            public final void run() {
                                FileLoadOperation.this.lambda$setIsPreloadVideoOperation$12(z);
                            }
                        });
                        return;
                    }
                }
                this.isPreloadVideoOperation = z;
            }
        }
    }

    public void setPaths(int i, String str, FileLoaderPriorityQueue fileLoaderPriorityQueue, File file, File file2, String str2) {
        this.storePath = file;
        this.tempPath = file2;
        this.currentAccount = i;
        this.fileName = str;
        this.storeFileName = str2;
        this.priorityQueue = fileLoaderPriorityQueue;
    }

    public void setPriority(int i) {
        this.priority = i;
    }

    public void setStream(final FileLoadOperationStream fileLoadOperationStream, boolean z, long j) {
        this.stream = fileLoadOperationStream;
        this.streamOffset = j;
        this.streamPriority = z;
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda26
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$setStream$0(fileLoadOperationStream);
            }
        });
    }

    public boolean start() {
        return start(this.stream, this.streamOffset, this.streamPriority);
    }

    /* JADX WARN: Code restructure failed: missing block: B:72:0x041a, code lost:
    
        if (r5 != r29.cacheFileFinal.length()) goto L136;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:101:0x0540 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:156:0x068b  */
    /* JADX WARN: Removed duplicated region for block: B:162:0x0696 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:172:0x06b8  */
    /* JADX WARN: Removed duplicated region for block: B:191:0x0741  */
    /* JADX WARN: Removed duplicated region for block: B:194:0x076b  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x0802  */
    /* JADX WARN: Removed duplicated region for block: B:206:0x082e  */
    /* JADX WARN: Removed duplicated region for block: B:212:0x0871  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x0900 A[Catch: Exception -> 0x0906, TRY_LEAVE, TryCatch #12 {Exception -> 0x0906, blocks: (B:245:0x08ef, B:247:0x0900), top: B:244:0x08ef }] */
    /* JADX WARN: Removed duplicated region for block: B:252:0x092f  */
    /* JADX WARN: Removed duplicated region for block: B:254:0x0933  */
    /* JADX WARN: Removed duplicated region for block: B:277:0x07d2  */
    /* JADX WARN: Removed duplicated region for block: B:287:0x073b  */
    /* JADX WARN: Removed duplicated region for block: B:332:0x0941  */
    /* JADX WARN: Removed duplicated region for block: B:349:0x03de  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x03b8  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0400  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0476  */
    /* JADX WARN: Type inference failed for: r1v36 */
    /* JADX WARN: Type inference failed for: r1v37, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r1v41 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean start(final FileLoadOperationStream fileLoadOperationStream, final long j, final boolean z) {
        String str;
        String str2;
        String str3;
        String str4;
        StringBuilder sb;
        String str5;
        StringBuilder sb2;
        String sb3;
        String sb4;
        Object obj;
        boolean exists;
        boolean z2;
        int i;
        int i2;
        boolean z3;
        String str6;
        String str7;
        String str8;
        boolean z4;
        String str9;
        ArrayList<Range> arrayList;
        Range range;
        ArrayList<Range> arrayList2;
        ?? r1;
        boolean z5;
        int i3;
        long j2;
        boolean z6;
        StringBuilder sb5;
        RandomAccessFile randomAccessFile;
        RandomAccessFile randomAccessFile2;
        int i4;
        this.startTime = System.currentTimeMillis();
        updateParams();
        if (this.currentDownloadChunkSize == 0) {
            if (this.forceSmallChunk) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("debug_loading: restart with small chunk");
                }
                this.currentDownloadChunkSize = 32768;
                i4 = 4;
            } else {
                if (this.isStory) {
                    this.currentDownloadChunkSize = this.downloadChunkSizeBig;
                } else if (this.isStream) {
                    this.currentDownloadChunkSize = this.downloadChunkSizeAnimation;
                    i4 = this.maxDownloadRequestsAnimation;
                } else {
                    boolean z7 = this.totalBytesCount >= ((long) this.bigFileSizeFrom);
                    this.currentDownloadChunkSize = z7 ? this.downloadChunkSizeBig : this.downloadChunkSize;
                    if (!z7) {
                        i4 = this.maxDownloadRequests;
                    }
                }
                i4 = this.maxDownloadRequestsBig;
            }
            this.currentMaxDownloadRequests = i4;
        }
        boolean z8 = this.state != 0;
        boolean z9 = this.paused;
        this.paused = false;
        if (fileLoadOperationStream != null) {
            final boolean z10 = z8;
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda17
                @Override // java.lang.Runnable
                public final void run() {
                    FileLoadOperation.this.lambda$start$9(z, j, fileLoadOperationStream, z10);
                }
            });
        } else if (z8) {
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda18
                @Override // java.lang.Runnable
                public final void run() {
                    FileLoadOperation.this.lambda$start$10();
                }
            });
        }
        if (z8) {
            return z9;
        }
        if (this.location == null && this.webLocation == null) {
            if (BuildVars.DEBUG_VERSION) {
                FileLog.d("loadOperation: no location, failing");
            }
            onFail(true, 0);
            return false;
        }
        long j3 = this.currentDownloadChunkSize;
        this.streamStartOffset = (j / j3) * j3;
        if (this.allowDisordererFileSave) {
            long j4 = this.totalBytesCount;
            if (j4 > 0 && j4 > j3) {
                this.notLoadedBytesRanges = new ArrayList<>();
                this.notRequestedBytesRanges = new ArrayList<>();
            }
        }
        if (this.webLocation != null) {
            String MD5 = Utilities.MD5(this.webFile.url);
            if (this.encryptFile) {
                str5 = MD5 + ".temp.enc";
                str2 = MD5 + "." + this.ext + ".enc";
                if (this.key != null) {
                    sb4 = MD5 + "_64.iv.enc";
                    str4 = null;
                    sb3 = null;
                    String str10 = str5;
                    str3 = sb4;
                    str = str10;
                    this.requestInfos = new ArrayList<>(this.currentMaxDownloadRequests);
                    this.cancelledRequestInfos = new ArrayList<>();
                    this.delayedRequestInfos = new ArrayList<>(this.currentMaxDownloadRequests - 1);
                    this.state = 1;
                    obj = this.parentObject;
                    if (obj instanceof TLRPC.TL_theme) {
                        this.cacheFileFinal = !this.encryptFile ? new File(this.storePath, this.storeFileName) : new File(this.storePath, str2);
                    } else {
                        this.cacheFileFinal = new File(ApplicationLoader.getFilesDirFixed(), "remote" + ((TLRPC.TL_theme) obj).id + ".attheme");
                    }
                    exists = this.cacheFileFinal.exists();
                    this.cacheFileFinalReady = exists;
                    if (exists) {
                        if (!(this.parentObject instanceof TLRPC.TL_theme)) {
                            long j5 = this.totalBytesCount;
                            if (j5 != 0) {
                                if (!this.ungzip) {
                                }
                            }
                        }
                        if (!this.delegate.isLocallyCreatedFile(this.cacheFileFinal.toString())) {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("debug_loading: delete existing file cause file size mismatch " + this.cacheFileFinal.getName() + " totalSize=" + this.totalBytesCount + " existingFileSize=" + this.cacheFileFinal.length());
                            }
                            if (!this.delegate.hasAnotherRefOnFile(this.cacheFileFinal.toString())) {
                                this.cacheFileFinal.delete();
                            }
                            exists = false;
                        }
                    }
                    if (exists) {
                        this.cacheFileTemp = new File(this.tempPath, str);
                        if (this.ungzip) {
                            this.cacheFileGzipTemp = new File(this.tempPath, str + ".gz");
                        }
                        String str11 = "rws";
                        if (this.encryptFile) {
                            File file = new File(FileLoader.getInternalCacheDir(), str2 + ".key");
                            try {
                                randomAccessFile2 = new RandomAccessFile(file, "rws");
                                long length = file.length();
                                byte[] bArr = new byte[32];
                                this.encryptKey = bArr;
                                this.encryptIv = new byte[16];
                                if (length <= 0 || length % 48 != 0) {
                                    Utilities.random.nextBytes(bArr);
                                    Utilities.random.nextBytes(this.encryptIv);
                                    randomAccessFile2.write(this.encryptKey);
                                    randomAccessFile2.write(this.encryptIv);
                                    z3 = true;
                                } else {
                                    randomAccessFile2.read(bArr, 0, 32);
                                    randomAccessFile2.read(this.encryptIv, 0, 16);
                                    z3 = false;
                                }
                            } catch (Exception e) {
                                e = e;
                                z3 = false;
                            }
                            try {
                                try {
                                    randomAccessFile2.getChannel().close();
                                } catch (Exception e2) {
                                    FileLog.e(e2);
                                }
                                randomAccessFile2.close();
                            } catch (Exception e3) {
                                e = e3;
                                if (AndroidUtilities.isENOSPC(e)) {
                                    LaunchActivity.checkFreeDiscSpaceStatic(1);
                                } else if (AndroidUtilities.isEROFS(e)) {
                                    SharedConfig.checkSdCard(this.cacheFileFinal);
                                } else {
                                    FileLog.e(e);
                                    i2 = 1;
                                    final boolean[] zArr = new boolean[i2];
                                    zArr[0] = false;
                                    if (this.supportsPreloading) {
                                    }
                                    str6 = str3;
                                    str7 = str4;
                                    str8 = "rws";
                                    z4 = z3;
                                    if (str7 != null) {
                                    }
                                    if (this.fileMetadata != null) {
                                    }
                                    if (this.cacheFileTemp.exists()) {
                                    }
                                }
                                FileLog.e((Throwable) e, false);
                                i2 = 1;
                                final boolean[] zArr2 = new boolean[i2];
                                zArr2[0] = false;
                                if (this.supportsPreloading) {
                                }
                                str6 = str3;
                                str7 = str4;
                                str8 = "rws";
                                z4 = z3;
                                if (str7 != null) {
                                }
                                if (this.fileMetadata != null) {
                                }
                                if (this.cacheFileTemp.exists()) {
                                }
                            }
                            i2 = 1;
                        } else {
                            i2 = 1;
                            z3 = false;
                        }
                        final boolean[] zArr22 = new boolean[i2];
                        zArr22[0] = false;
                        if (this.supportsPreloading || sb3 == null) {
                            str6 = str3;
                            str7 = str4;
                            str8 = "rws";
                            z4 = z3;
                        } else {
                            this.cacheFilePreload = new File(this.tempPath, sb3);
                            try {
                                RandomAccessFile randomAccessFile3 = new RandomAccessFile(this.cacheFilePreload, "rws");
                                this.preloadStream = randomAccessFile3;
                                long length2 = randomAccessFile3.length();
                                this.preloadStreamFileOffset = 1;
                                long j6 = 1;
                                if (length2 > 1) {
                                    zArr22[0] = this.preloadStream.readByte() != 0;
                                    while (j6 < length2) {
                                        if (length2 - j6 < 8) {
                                            break;
                                        }
                                        long readLong = this.preloadStream.readLong();
                                        if (length2 - (j6 + 8) < 8 || readLong < 0) {
                                            break;
                                        }
                                        boolean z11 = z3;
                                        try {
                                            if (readLong <= this.totalBytesCount) {
                                                long readLong2 = this.preloadStream.readLong();
                                                long j7 = j6 + 16;
                                                if (length2 - j7 >= readLong2 && readLong2 <= this.currentDownloadChunkSize) {
                                                    PreloadRange preloadRange = new PreloadRange(j7, readLong2);
                                                    long j8 = j7 + readLong2;
                                                    this.preloadStream.seek(j8);
                                                    if (length2 - j8 >= 24) {
                                                        long j9 = length2;
                                                        long readLong3 = this.preloadStream.readLong();
                                                        this.foundMoovSize = readLong3;
                                                        if (readLong3 != 0) {
                                                            str6 = str3;
                                                            z4 = z11;
                                                            try {
                                                                str7 = str4;
                                                                str8 = str11;
                                                            } catch (Exception e4) {
                                                                e = e4;
                                                                str7 = str4;
                                                                str8 = str11;
                                                                FileLog.e((Throwable) e, false);
                                                                if (!this.isPreloadVideoOperation) {
                                                                    this.cacheFilePreload = null;
                                                                    try {
                                                                        randomAccessFile = this.preloadStream;
                                                                        if (randomAccessFile != null) {
                                                                        }
                                                                    } catch (Exception e5) {
                                                                        FileLog.e(e5);
                                                                    }
                                                                }
                                                                if (str7 != null) {
                                                                }
                                                                if (this.fileMetadata != null) {
                                                                }
                                                                if (this.cacheFileTemp.exists()) {
                                                                }
                                                            }
                                                            try {
                                                                this.moovFound = this.nextPreloadDownloadOffset > this.totalBytesCount / 2 ? 2 : 1;
                                                                this.preloadNotRequestedBytesCount = readLong3;
                                                            } catch (Exception e6) {
                                                                e = e6;
                                                                FileLog.e((Throwable) e, false);
                                                                if (!this.isPreloadVideoOperation) {
                                                                }
                                                                if (str7 != null) {
                                                                }
                                                                if (this.fileMetadata != null) {
                                                                }
                                                                if (this.cacheFileTemp.exists()) {
                                                                }
                                                            }
                                                        } else {
                                                            str6 = str3;
                                                            z4 = z11;
                                                            str7 = str4;
                                                            str8 = str11;
                                                        }
                                                        this.nextPreloadDownloadOffset = this.preloadStream.readLong();
                                                        this.nextAtomOffset = this.preloadStream.readLong();
                                                        long j10 = j8 + 24;
                                                        if (this.preloadedBytesRanges == null) {
                                                            this.preloadedBytesRanges = new HashMap<>();
                                                        }
                                                        if (this.requestedPreloadedBytesRanges == null) {
                                                            this.requestedPreloadedBytesRanges = new HashMap<>();
                                                        }
                                                        this.preloadedBytesRanges.put(Long.valueOf(readLong), preloadRange);
                                                        this.requestedPreloadedBytesRanges.put(Long.valueOf(readLong), 1);
                                                        this.totalPreloadedBytes = (int) (this.totalPreloadedBytes + readLong2);
                                                        this.preloadStreamFileOffset = (int) (this.preloadStreamFileOffset + readLong2 + 36);
                                                        z3 = z4;
                                                        length2 = j9;
                                                        str4 = str7;
                                                        str11 = str8;
                                                        j6 = j10;
                                                        str3 = str6;
                                                    }
                                                }
                                            }
                                            str6 = str3;
                                            z4 = z11;
                                            str7 = str4;
                                            str8 = str11;
                                            break;
                                        } catch (Exception e7) {
                                            e = e7;
                                            str6 = str3;
                                            z4 = z11;
                                        }
                                    }
                                }
                                str6 = str3;
                                str7 = str4;
                                str8 = str11;
                                z4 = z3;
                                this.preloadStream.seek(this.preloadStreamFileOffset);
                            } catch (Exception e8) {
                                e = e8;
                                str6 = str3;
                                str7 = str4;
                                str8 = str11;
                                z4 = z3;
                            }
                            if (!this.isPreloadVideoOperation && this.preloadedBytesRanges == null) {
                                this.cacheFilePreload = null;
                                randomAccessFile = this.preloadStream;
                                if (randomAccessFile != null) {
                                    try {
                                        randomAccessFile.getChannel().close();
                                    } catch (Exception e9) {
                                        FileLog.e(e9);
                                    }
                                    this.preloadStream.close();
                                    this.preloadStream = null;
                                }
                            }
                        }
                        if (str7 != null) {
                            this.cacheFileParts = new File(this.tempPath, str7);
                            if (!this.cacheFileTemp.exists()) {
                                this.cacheFileParts.delete();
                            }
                            try {
                                str9 = str8;
                            } catch (Exception e10) {
                                e = e10;
                                str9 = str8;
                            }
                            try {
                                RandomAccessFile randomAccessFile4 = new RandomAccessFile(this.cacheFileParts, str9);
                                this.filePartsStream = randomAccessFile4;
                                long length3 = randomAccessFile4.length();
                                if (length3 % 8 == 4) {
                                    int readInt = this.filePartsStream.readInt();
                                    if (readInt <= (length3 - 4) / 2) {
                                        for (int i5 = 0; i5 < readInt; i5++) {
                                            long readLong4 = this.filePartsStream.readLong();
                                            long readLong5 = this.filePartsStream.readLong();
                                            this.notLoadedBytesRanges.add(new Range(readLong4, readLong5));
                                            this.notRequestedBytesRanges.add(new Range(readLong4, readLong5));
                                        }
                                    }
                                }
                            } catch (Exception e11) {
                                e = e11;
                                FileLog.e(e, !AndroidUtilities.isFilNotFoundException(e));
                                if (this.fileMetadata != null) {
                                }
                                if (this.cacheFileTemp.exists()) {
                                }
                            }
                        } else {
                            str9 = str8;
                        }
                        if (this.fileMetadata != null) {
                            FileLoader.getInstance(this.currentAccount).getFileDatabase().saveFileDialogId(this.cacheFileParts, this.fileMetadata);
                            FileLoader.getInstance(this.currentAccount).getFileDatabase().saveFileDialogId(this.cacheFileTemp, this.fileMetadata);
                        }
                        if (this.cacheFileTemp.exists()) {
                            ArrayList<Range> arrayList3 = this.notLoadedBytesRanges;
                            if (arrayList3 != null && arrayList3.isEmpty()) {
                                long j11 = 0;
                                this.notLoadedBytesRanges.add(new Range(j11, this.totalBytesCount));
                                arrayList = this.notRequestedBytesRanges;
                                range = new Range(j11, this.totalBytesCount);
                                arrayList.add(range);
                            }
                            arrayList2 = this.notLoadedBytesRanges;
                            if (arrayList2 != null) {
                            }
                            if (BuildVars.LOGS_ENABLED) {
                            }
                            if (str6 != null) {
                            }
                            if (!this.isPreloadVideoOperation) {
                                copyNotLoadedRanges();
                            }
                            updateProgress();
                            RandomAccessFile randomAccessFile5 = new RandomAccessFile(this.cacheFileTemp, str9);
                            this.fileOutputStream = randomAccessFile5;
                            j2 = this.downloadedBytes;
                            if (j2 != 0) {
                            }
                            r1 = 0;
                            z5 = true;
                            if (this.fileOutputStream != null) {
                            }
                        } else {
                            File file2 = this.cacheFileTemp;
                            if (z4) {
                                file2.delete();
                            } else {
                                long length4 = file2.length();
                                if (str6 == null || length4 % this.currentDownloadChunkSize == 0) {
                                    long floorDiv = floorDiv(this.cacheFileTemp.length(), this.currentDownloadChunkSize) * this.currentDownloadChunkSize;
                                    this.downloadedBytes = floorDiv;
                                    this.requestedBytesCount = floorDiv;
                                } else {
                                    this.requestedBytesCount = 0L;
                                }
                                ArrayList<Range> arrayList4 = this.notLoadedBytesRanges;
                                if (arrayList4 != null && arrayList4.isEmpty()) {
                                    this.notLoadedBytesRanges.add(new Range(this.downloadedBytes, this.totalBytesCount));
                                    arrayList = this.notRequestedBytesRanges;
                                    range = new Range(this.downloadedBytes, this.totalBytesCount);
                                    arrayList.add(range);
                                }
                            }
                            arrayList2 = this.notLoadedBytesRanges;
                            if (arrayList2 != null) {
                                this.downloadedBytes = this.totalBytesCount;
                                int size = arrayList2.size();
                                for (int i6 = 0; i6 < size; i6++) {
                                    Range range2 = this.notLoadedBytesRanges.get(i6);
                                    this.downloadedBytes -= range2.end - range2.start;
                                }
                                this.requestedBytesCount = this.downloadedBytes;
                            }
                            if (BuildVars.LOGS_ENABLED) {
                                if (this.isPreloadVideoOperation) {
                                    sb5 = new StringBuilder();
                                    sb5.append("start preloading file to temp = ");
                                    sb5.append(this.cacheFileTemp);
                                } else {
                                    sb5 = new StringBuilder();
                                    sb5.append("start loading file to temp = ");
                                    sb5.append(this.cacheFileTemp);
                                    sb5.append(" final = ");
                                    sb5.append(this.cacheFileFinal);
                                    sb5.append(" priority");
                                    sb5.append(this.priority);
                                }
                                FileLog.d(sb5.toString());
                            }
                            if (str6 != null) {
                                this.cacheIvTemp = new File(this.tempPath, str6);
                                try {
                                    this.fiv = new RandomAccessFile(this.cacheIvTemp, str9);
                                    long j12 = 0;
                                    if (this.downloadedBytes != 0 && !z4) {
                                        long length5 = this.cacheIvTemp.length();
                                        if (length5 > 0) {
                                            if (length5 % 64 == 0) {
                                                this.fiv.read(this.iv, 0, 64);
                                            } else {
                                                j12 = 0;
                                            }
                                        }
                                        this.downloadedBytes = j12;
                                        this.requestedBytesCount = j12;
                                    }
                                } catch (Exception e12) {
                                    this.downloadedBytes = 0L;
                                    this.requestedBytesCount = 0L;
                                    if (AndroidUtilities.isENOSPC(e12)) {
                                        LaunchActivity.checkFreeDiscSpaceStatic(1);
                                        z6 = false;
                                    } else {
                                        z6 = false;
                                        if (AndroidUtilities.isEROFS(e12)) {
                                            SharedConfig.checkSdCard(this.cacheFileFinal);
                                        } else {
                                            FileLog.e(e12);
                                        }
                                    }
                                    FileLog.e(e12, z6);
                                }
                            }
                            if (!this.isPreloadVideoOperation && this.downloadedBytes != 0 && this.totalBytesCount > 0) {
                                copyNotLoadedRanges();
                            }
                            updateProgress();
                            try {
                                RandomAccessFile randomAccessFile52 = new RandomAccessFile(this.cacheFileTemp, str9);
                                this.fileOutputStream = randomAccessFile52;
                                j2 = this.downloadedBytes;
                                if (j2 != 0) {
                                    randomAccessFile52.seek(j2);
                                }
                                r1 = 0;
                                z5 = true;
                            } catch (Exception e13) {
                                r1 = 0;
                                FileLog.e((Throwable) e13, false);
                                if (AndroidUtilities.isENOSPC(e13)) {
                                    z5 = true;
                                    LaunchActivity.checkFreeDiscSpaceStatic(1);
                                    i3 = -1;
                                } else {
                                    z5 = true;
                                    i3 = -1;
                                    if (AndroidUtilities.isEROFS(e13)) {
                                        SharedConfig.checkSdCard(this.cacheFileFinal);
                                        FileLog.e((Throwable) e13, false);
                                    }
                                }
                                onFail(z5, i3);
                                return false;
                            }
                            if (this.fileOutputStream != null) {
                                onFail(z5, r1);
                                return r1;
                            }
                            this.started = z5;
                            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda19
                                @Override // java.lang.Runnable
                                public final void run() {
                                    FileLoadOperation.this.lambda$start$11(zArr22);
                                }
                            });
                        }
                    } else {
                        this.started = true;
                        try {
                            onFinishLoadingFile(false, 1, false);
                            FilePathDatabase.PathData pathData = this.pathSaveData;
                            if (pathData != null) {
                                this.delegate.saveFilePath(pathData, this.cacheFileFinal);
                            }
                        } catch (Exception e14) {
                            FileLog.e((Throwable) e14, false);
                            if (AndroidUtilities.isENOSPC(e14)) {
                                z2 = true;
                                LaunchActivity.checkFreeDiscSpaceStatic(1);
                                i = -1;
                                onFail(true, -1);
                            } else {
                                z2 = true;
                                i = -1;
                            }
                            if (!AndroidUtilities.isEROFS(e14)) {
                                onFail(z2, 0);
                                return z2;
                            }
                            SharedConfig.checkSdCard(this.cacheFileFinal);
                            onFail(z2, i);
                            return false;
                        }
                    }
                    return true;
                }
                str = str5;
                str3 = null;
            } else {
                String str12 = MD5 + ".temp";
                String str13 = MD5 + "." + this.ext;
                if (this.key != null) {
                    str3 = MD5 + "_64.iv";
                    str = str12;
                    str2 = str13;
                } else {
                    str = str12;
                    str2 = str13;
                    str3 = null;
                }
            }
        } else {
            TLRPC.InputFileLocation inputFileLocation = this.location;
            long j13 = inputFileLocation.volume_id;
            if (j13 == 0 || inputFileLocation.local_id == 0) {
                if (this.datacenterId == 0 || inputFileLocation.id == 0) {
                    onFail(true, 0);
                    return false;
                }
                if (!this.encryptFile) {
                    str = this.datacenterId + "_" + this.location.id + ".temp";
                    str2 = this.datacenterId + "_" + this.location.id + this.ext;
                    str3 = this.key != null ? this.datacenterId + "_" + this.location.id + "_64.iv" : null;
                    str4 = this.notLoadedBytesRanges != null ? this.datacenterId + "_" + this.location.id + "_64.pt" : null;
                    sb = new StringBuilder();
                    sb.append(this.datacenterId);
                    sb.append("_");
                    sb.append(this.location.id);
                    sb.append("_64.preload");
                    sb3 = sb.toString();
                    this.requestInfos = new ArrayList<>(this.currentMaxDownloadRequests);
                    this.cancelledRequestInfos = new ArrayList<>();
                    this.delayedRequestInfos = new ArrayList<>(this.currentMaxDownloadRequests - 1);
                    this.state = 1;
                    obj = this.parentObject;
                    if (obj instanceof TLRPC.TL_theme) {
                    }
                    exists = this.cacheFileFinal.exists();
                    this.cacheFileFinalReady = exists;
                    if (exists) {
                    }
                    if (exists) {
                    }
                    return true;
                }
                str5 = this.datacenterId + "_" + this.location.id + ".temp.enc";
                str2 = this.datacenterId + "_" + this.location.id + this.ext + ".enc";
                if (this.key != null) {
                    sb2 = new StringBuilder();
                    sb2.append(this.datacenterId);
                    sb2.append("_");
                    sb2.append(this.location.id);
                    sb2.append("_64.iv.enc");
                    sb4 = sb2.toString();
                    str4 = null;
                    sb3 = null;
                    String str102 = str5;
                    str3 = sb4;
                    str = str102;
                    this.requestInfos = new ArrayList<>(this.currentMaxDownloadRequests);
                    this.cancelledRequestInfos = new ArrayList<>();
                    this.delayedRequestInfos = new ArrayList<>(this.currentMaxDownloadRequests - 1);
                    this.state = 1;
                    obj = this.parentObject;
                    if (obj instanceof TLRPC.TL_theme) {
                    }
                    exists = this.cacheFileFinal.exists();
                    this.cacheFileFinalReady = exists;
                    if (exists) {
                    }
                    if (exists) {
                    }
                    return true;
                }
                str = str5;
                str3 = null;
            } else {
                int i7 = this.datacenterId;
                if (i7 == Integer.MIN_VALUE || j13 == -2147483648L || i7 == 0) {
                    onFail(true, 0);
                    return false;
                }
                if (!this.encryptFile) {
                    str = this.location.volume_id + "_" + this.location.local_id + ".temp";
                    str2 = this.location.volume_id + "_" + this.location.local_id + "." + this.ext;
                    str3 = this.key != null ? this.location.volume_id + "_" + this.location.local_id + "_64.iv" : null;
                    str4 = this.notLoadedBytesRanges != null ? this.location.volume_id + "_" + this.location.local_id + "_64.pt" : null;
                    sb = new StringBuilder();
                    sb.append(this.location.volume_id);
                    sb.append("_");
                    sb.append(this.location.local_id);
                    sb.append("_64.preload");
                    sb3 = sb.toString();
                    this.requestInfos = new ArrayList<>(this.currentMaxDownloadRequests);
                    this.cancelledRequestInfos = new ArrayList<>();
                    this.delayedRequestInfos = new ArrayList<>(this.currentMaxDownloadRequests - 1);
                    this.state = 1;
                    obj = this.parentObject;
                    if (obj instanceof TLRPC.TL_theme) {
                    }
                    exists = this.cacheFileFinal.exists();
                    this.cacheFileFinalReady = exists;
                    if (exists) {
                    }
                    if (exists) {
                    }
                    return true;
                }
                str5 = this.location.volume_id + "_" + this.location.local_id + ".temp.enc";
                str2 = this.location.volume_id + "_" + this.location.local_id + "." + this.ext + ".enc";
                if (this.key != null) {
                    sb2 = new StringBuilder();
                    sb2.append(this.location.volume_id);
                    sb2.append("_");
                    sb2.append(this.location.local_id);
                    sb2.append("_64.iv.enc");
                    sb4 = sb2.toString();
                    str4 = null;
                    sb3 = null;
                    String str1022 = str5;
                    str3 = sb4;
                    str = str1022;
                    this.requestInfos = new ArrayList<>(this.currentMaxDownloadRequests);
                    this.cancelledRequestInfos = new ArrayList<>();
                    this.delayedRequestInfos = new ArrayList<>(this.currentMaxDownloadRequests - 1);
                    this.state = 1;
                    obj = this.parentObject;
                    if (obj instanceof TLRPC.TL_theme) {
                    }
                    exists = this.cacheFileFinal.exists();
                    this.cacheFileFinalReady = exists;
                    if (exists) {
                    }
                    if (exists) {
                    }
                    return true;
                }
                str = str5;
                str3 = null;
            }
        }
        str4 = null;
        sb3 = null;
        this.requestInfos = new ArrayList<>(this.currentMaxDownloadRequests);
        this.cancelledRequestInfos = new ArrayList<>();
        this.delayedRequestInfos = new ArrayList<>(this.currentMaxDownloadRequests - 1);
        this.state = 1;
        obj = this.parentObject;
        if (obj instanceof TLRPC.TL_theme) {
        }
        exists = this.cacheFileFinal.exists();
        this.cacheFileFinalReady = exists;
        if (exists) {
        }
        if (exists) {
        }
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:129:0x0254  */
    /* JADX WARN: Removed duplicated region for block: B:134:0x0263  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x026b  */
    /* JADX WARN: Removed duplicated region for block: B:165:0x034c  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x0375  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x038d  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x0399  */
    /* JADX WARN: Removed duplicated region for block: B:184:0x03ca  */
    /* JADX WARN: Removed duplicated region for block: B:187:0x039d  */
    /* JADX WARN: Removed duplicated region for block: B:188:0x036d  */
    /* JADX WARN: Removed duplicated region for block: B:189:0x02f1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:201:0x027e  */
    /* JADX WARN: Removed duplicated region for block: B:205:0x0266  */
    /* JADX WARN: Removed duplicated region for block: B:206:0x025e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void startDownloadRequest(int i) {
        int i2;
        long j;
        long j2;
        long j3;
        int i3;
        long j4;
        long j5;
        int i4;
        boolean z;
        int i5;
        TLRPC.TL_upload_getFile tL_upload_getFile;
        final RequestInfo requestInfo;
        long j6;
        TLRPC.InputFileLocation inputFileLocation;
        long j7;
        HashMap<Long, PreloadRange> hashMap;
        PreloadRange preloadRange;
        ArrayList<Range> arrayList;
        int i6;
        if (BuildVars.DEBUG_PRIVATE_VERSION && Utilities.stageQueue != null && Utilities.stageQueue.getHandler() != null && Thread.currentThread() != Utilities.stageQueue.getHandler().getLooper().getThread()) {
            throw new RuntimeException("Wrong thread!!!");
        }
        if (this.state == 5) {
            this.state = 1;
        }
        if (this.paused || this.reuploadingCdn || this.state != 1 || this.requestingReference) {
            return;
        }
        long j8 = 0;
        if (this.isStory || this.streamPriorityStartOffset != 0 || this.nextPartWasPreloaded || this.requestInfos.size() + this.delayedRequestInfos.size() < this.currentMaxDownloadRequests) {
            if (this.isPreloadVideoOperation) {
                if (this.requestedBytesCount > 2097152) {
                    return;
                }
                if (this.moovFound != 0 && this.requestInfos.size() > 0) {
                    return;
                }
            }
            boolean z2 = false;
            int max = (!this.isStory && (this.streamPriorityStartOffset != 0 || this.nextPartWasPreloaded || ((this.isPreloadVideoOperation && this.moovFound == 0) || this.totalBytesCount <= 0))) ? 1 : Math.max(0, this.currentMaxDownloadRequests - this.requestInfos.size());
            int i7 = 0;
            while (i7 < max) {
                int i8 = 2;
                if (!this.isPreloadVideoOperation) {
                    i2 = max;
                    ArrayList<Range> arrayList2 = this.notRequestedBytesRanges;
                    if (arrayList2 != null) {
                        long j9 = this.streamPriorityStartOffset;
                        if (j9 == 0) {
                            j9 = this.streamStartOffset;
                        }
                        int size = arrayList2.size();
                        int i9 = 0;
                        long j10 = Long.MAX_VALUE;
                        long j11 = Long.MAX_VALUE;
                        while (true) {
                            if (i9 >= size) {
                                j = j10;
                                j2 = j11;
                                break;
                            }
                            Range range = this.notRequestedBytesRanges.get(i9);
                            if (j9 != 0) {
                                if (range.start <= j9 && range.end > j9) {
                                    j = j9;
                                    j2 = Long.MAX_VALUE;
                                    break;
                                } else if (j9 < range.start && range.start < j10) {
                                    j10 = range.start;
                                }
                            }
                            j11 = Math.min(j11, range.start);
                            i9++;
                        }
                        if (j == Long.MAX_VALUE) {
                            if (j2 == Long.MAX_VALUE) {
                                boolean z3 = BuildVars.DEBUG_VERSION;
                                return;
                            }
                            j3 = j2;
                            i3 = this.preloadPrefixSize;
                            if (i3 > 0 || j3 < i3 || !canFinishPreload()) {
                                j4 = this.totalBytesCount;
                                if (j4 > 0 || j3 <= 0 || j3 < j4) {
                                    if (!this.isPreloadVideoOperation && (arrayList = this.notRequestedBytesRanges) != null) {
                                        addPart(arrayList, j3, j3 + this.currentDownloadChunkSize, false);
                                        boolean z4 = BuildVars.DEBUG_VERSION;
                                    }
                                    j5 = this.totalBytesCount;
                                    if (j5 > 0 || i7 == i2 - 1 || (j5 > 0 && this.currentDownloadChunkSize + j3 >= j5)) {
                                        i4 = i;
                                        z = true;
                                    } else {
                                        i4 = i;
                                        z = false;
                                    }
                                    if (i4 == -1) {
                                        i8 = i4;
                                    } else if (this.requestsCount % 2 != 0) {
                                        i8 = ConnectionsManager.ConnectionTypeDownload2;
                                    }
                                    i5 = !this.isForceRequest ? 32 : 0;
                                    if (!this.isCdn) {
                                        TLRPC.TL_upload_getCdnFile tL_upload_getCdnFile = new TLRPC.TL_upload_getCdnFile();
                                        tL_upload_getCdnFile.file_token = this.cdnToken;
                                        tL_upload_getCdnFile.offset = j3;
                                        tL_upload_getCdnFile.limit = this.currentDownloadChunkSize;
                                        i5 |= 1;
                                        tL_upload_getFile = tL_upload_getCdnFile;
                                    } else if (this.webLocation != null) {
                                        TLRPC.TL_upload_getWebFile tL_upload_getWebFile = new TLRPC.TL_upload_getWebFile();
                                        tL_upload_getWebFile.location = this.webLocation;
                                        tL_upload_getWebFile.offset = (int) j3;
                                        tL_upload_getWebFile.limit = this.currentDownloadChunkSize;
                                        tL_upload_getFile = tL_upload_getWebFile;
                                    } else {
                                        TLRPC.TL_upload_getFile tL_upload_getFile2 = new TLRPC.TL_upload_getFile();
                                        tL_upload_getFile2.location = this.location;
                                        tL_upload_getFile2.offset = j3;
                                        tL_upload_getFile2.limit = this.currentDownloadChunkSize;
                                        tL_upload_getFile2.cdn_supported = true;
                                        tL_upload_getFile = tL_upload_getFile2;
                                    }
                                    final TLRPC.TL_upload_getFile tL_upload_getFile3 = tL_upload_getFile;
                                    this.requestedBytesCount += this.currentDownloadChunkSize;
                                    requestInfo = new RequestInfo();
                                    this.requestInfos.add(requestInfo);
                                    requestInfo.offset = j3;
                                    requestInfo.chunkSize = this.currentDownloadChunkSize;
                                    requestInfo.forceSmallChunk = this.forceSmallChunk;
                                    requestInfo.connectionType = i8;
                                    if (!this.isPreloadVideoOperation && this.supportsPreloading && this.preloadStream != null && (hashMap = this.preloadedBytesRanges) != null && (preloadRange = hashMap.get(Long.valueOf(requestInfo.offset))) != null) {
                                        requestInfo.response = new TLRPC.TL_upload_file();
                                        try {
                                            if (BuildVars.DEBUG_VERSION) {
                                                try {
                                                    if (preloadRange.length > 2147483647L) {
                                                        throw new RuntimeException("cast long to integer");
                                                    }
                                                } catch (Exception unused) {
                                                }
                                            }
                                            NativeByteBuffer nativeByteBuffer = new NativeByteBuffer((int) preloadRange.length);
                                            this.preloadStream.seek(preloadRange.fileOffset);
                                            this.preloadStream.getChannel().read(nativeByteBuffer.buffer);
                                            try {
                                                nativeByteBuffer.buffer.position(0);
                                                requestInfo.response.bytes = nativeByteBuffer;
                                                Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda1
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        FileLoadOperation.this.lambda$startDownloadRequest$27(requestInfo);
                                                    }
                                                });
                                                j7 = 0;
                                            } catch (Exception unused2) {
                                                if (this.streamPriorityStartOffset != 0) {
                                                }
                                                inputFileLocation = this.location;
                                                if (inputFileLocation instanceof TLRPC.TL_inputPeerPhotoFileLocation) {
                                                }
                                                requestInfo.forceSmallChunk = this.forceSmallChunk;
                                                if (BuildVars.LOGS_ENABLED) {
                                                }
                                                int i10 = i5 | 2048;
                                                final int i11 = this.isCdn ? this.cdnDatacenterId : this.datacenterId;
                                                j7 = j6;
                                                final int i12 = i8;
                                                final int sendRequestSync = ConnectionsManager.getInstance(this.currentAccount).sendRequestSync(tL_upload_getFile3, new RequestDelegate() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda2
                                                    @Override // org.telegram.tgnet.RequestDelegate
                                                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                                        FileLoadOperation.this.lambda$startDownloadRequest$29(requestInfo, i11, i12, tL_upload_getFile3, tLObject, tL_error);
                                                    }
                                                }, null, null, i10, i11, i8, z);
                                                requestInfo.requestToken = sendRequestSync;
                                                if (BuildVars.LOGS_ENABLED) {
                                                }
                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda3
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        FileLoadOperation.this.lambda$startDownloadRequest$30(sendRequestSync);
                                                    }
                                                });
                                                this.requestsCount++;
                                                i7++;
                                                j8 = j7;
                                                max = i2;
                                                z2 = false;
                                            }
                                        } catch (Exception unused3) {
                                        }
                                        i7++;
                                        j8 = j7;
                                        max = i2;
                                        z2 = false;
                                    }
                                    if (this.streamPriorityStartOffset != 0) {
                                        if (BuildVars.DEBUG_VERSION) {
                                            FileLog.d("frame get offset = " + this.streamPriorityStartOffset);
                                        }
                                        j6 = 0;
                                        this.streamPriorityStartOffset = 0L;
                                        this.priorityRequestInfo = requestInfo;
                                    } else {
                                        j6 = 0;
                                    }
                                    inputFileLocation = this.location;
                                    if ((inputFileLocation instanceof TLRPC.TL_inputPeerPhotoFileLocation) || ((TLRPC.TL_inputPeerPhotoFileLocation) inputFileLocation).photo_id != j6) {
                                        requestInfo.forceSmallChunk = this.forceSmallChunk;
                                        if (BuildVars.LOGS_ENABLED) {
                                            requestInfo.requestStartTime = System.currentTimeMillis();
                                        }
                                        int i102 = i5 | 2048;
                                        final int i112 = this.isCdn ? this.cdnDatacenterId : this.datacenterId;
                                        j7 = j6;
                                        final int i122 = i8;
                                        final int sendRequestSync2 = ConnectionsManager.getInstance(this.currentAccount).sendRequestSync(tL_upload_getFile3, new RequestDelegate() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda2
                                            @Override // org.telegram.tgnet.RequestDelegate
                                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                                FileLoadOperation.this.lambda$startDownloadRequest$29(requestInfo, i112, i122, tL_upload_getFile3, tLObject, tL_error);
                                            }
                                        }, null, null, i102, i112, i8, z);
                                        requestInfo.requestToken = sendRequestSync2;
                                        if (BuildVars.LOGS_ENABLED) {
                                            FileLog.d("debug_loading: " + this.cacheFileFinal.getName() + " dc=" + i112 + " send reqId " + requestInfo.requestToken + " offset=" + requestInfo.offset + " conType=" + i8 + " priority=" + this.priority);
                                        }
                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda3
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                FileLoadOperation.this.lambda$startDownloadRequest$30(sendRequestSync2);
                                            }
                                        });
                                        this.requestsCount++;
                                    } else {
                                        requestReference(requestInfo);
                                        j7 = j6;
                                    }
                                    i7++;
                                    j8 = j7;
                                    max = i2;
                                    z2 = false;
                                }
                            }
                            boolean z32 = BuildVars.DEBUG_VERSION;
                            return;
                        }
                    } else {
                        j = this.requestedBytesCount;
                    }
                } else {
                    if (this.moovFound != 0 && this.preloadNotRequestedBytesCount <= j8) {
                        boolean z5 = BuildVars.DEBUG_VERSION;
                        return;
                    }
                    j = this.nextPreloadDownloadOffset;
                    if (j == -1) {
                        int i13 = (preloadMaxBytes / this.currentDownloadChunkSize) + 2;
                        long j12 = j8;
                        while (true) {
                            if (i13 == 0) {
                                i2 = max;
                                j = j12;
                                z2 = false;
                                break;
                            }
                            if (!this.requestedPreloadedBytesRanges.containsKey(Long.valueOf(j12))) {
                                j = j12;
                                i2 = max;
                                z2 = true;
                                break;
                            }
                            long j13 = this.currentDownloadChunkSize;
                            j12 += j13;
                            long j14 = this.totalBytesCount;
                            if (j12 > j14) {
                                j = j12;
                                i2 = max;
                                break;
                            }
                            if (this.moovFound == 2) {
                                i6 = max;
                                if (j12 == r0 * 8) {
                                    j12 = ((j14 - 1048576) / j13) * j13;
                                }
                            } else {
                                i6 = max;
                            }
                            i13--;
                            max = i6;
                            z2 = false;
                        }
                        if (!z2 && this.requestInfos.isEmpty()) {
                            onFinishLoadingFile(false, 0, false);
                        }
                    } else {
                        i2 = max;
                    }
                    if (this.requestedPreloadedBytesRanges == null) {
                        this.requestedPreloadedBytesRanges = new HashMap<>();
                    }
                    this.requestedPreloadedBytesRanges.put(Long.valueOf(j), 1);
                    if (BuildVars.DEBUG_VERSION) {
                        FileLog.d("start next preload from " + j + " size " + this.totalBytesCount + " for " + this.cacheFilePreload);
                    }
                    this.preloadNotRequestedBytesCount -= this.currentDownloadChunkSize;
                }
                j3 = j;
                i3 = this.preloadPrefixSize;
                if (i3 > 0) {
                }
                j4 = this.totalBytesCount;
                if (j4 > 0) {
                }
                if (!this.isPreloadVideoOperation) {
                    addPart(arrayList, j3, j3 + this.currentDownloadChunkSize, false);
                    boolean z42 = BuildVars.DEBUG_VERSION;
                }
                j5 = this.totalBytesCount;
                if (j5 > 0) {
                }
                i4 = i;
                z = true;
                if (i4 == -1) {
                }
                if (!this.isForceRequest) {
                }
                if (!this.isCdn) {
                }
                final TLObject tL_upload_getFile32 = tL_upload_getFile;
                this.requestedBytesCount += this.currentDownloadChunkSize;
                requestInfo = new RequestInfo();
                this.requestInfos.add(requestInfo);
                requestInfo.offset = j3;
                requestInfo.chunkSize = this.currentDownloadChunkSize;
                requestInfo.forceSmallChunk = this.forceSmallChunk;
                requestInfo.connectionType = i8;
                if (!this.isPreloadVideoOperation) {
                    requestInfo.response = new TLRPC.TL_upload_file();
                    if (BuildVars.DEBUG_VERSION) {
                    }
                    NativeByteBuffer nativeByteBuffer2 = new NativeByteBuffer((int) preloadRange.length);
                    this.preloadStream.seek(preloadRange.fileOffset);
                    this.preloadStream.getChannel().read(nativeByteBuffer2.buffer);
                    nativeByteBuffer2.buffer.position(0);
                    requestInfo.response.bytes = nativeByteBuffer2;
                    Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            FileLoadOperation.this.lambda$startDownloadRequest$27(requestInfo);
                        }
                    });
                    j7 = 0;
                    i7++;
                    j8 = j7;
                    max = i2;
                    z2 = false;
                }
                if (this.streamPriorityStartOffset != 0) {
                }
                inputFileLocation = this.location;
                if (inputFileLocation instanceof TLRPC.TL_inputPeerPhotoFileLocation) {
                }
                requestInfo.forceSmallChunk = this.forceSmallChunk;
                if (BuildVars.LOGS_ENABLED) {
                }
                int i1022 = i5 | 2048;
                final int i1122 = this.isCdn ? this.cdnDatacenterId : this.datacenterId;
                j7 = j6;
                final int i1222 = i8;
                final int sendRequestSync22 = ConnectionsManager.getInstance(this.currentAccount).sendRequestSync(tL_upload_getFile32, new RequestDelegate() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda2
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        FileLoadOperation.this.lambda$startDownloadRequest$29(requestInfo, i1122, i1222, tL_upload_getFile32, tLObject, tL_error);
                    }
                }, null, null, i1022, i1122, i8, z);
                requestInfo.requestToken = sendRequestSync22;
                if (BuildVars.LOGS_ENABLED) {
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        FileLoadOperation.this.lambda$startDownloadRequest$30(sendRequestSync22);
                    }
                });
                this.requestsCount++;
                i7++;
                j8 = j7;
                max = i2;
                z2 = false;
            }
        }
    }

    public void updateProgress() {
        FileLoadOperationDelegate fileLoadOperationDelegate = this.delegate;
        if (fileLoadOperationDelegate != null) {
            long j = this.downloadedBytes;
            long j2 = this.totalBytesCount;
            if (j == j2 || j2 <= 0) {
                return;
            }
            fileLoadOperationDelegate.didChangedLoadProgress(this, j, j2);
        }
    }

    public boolean wasStarted() {
        return this.started && !this.paused;
    }
}
