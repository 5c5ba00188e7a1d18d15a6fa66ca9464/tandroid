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
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$InputFileLocation;
import org.telegram.tgnet.TLRPC$InputWebFileLocation;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$TL_document;
import org.telegram.tgnet.TLRPC$TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC$TL_documentEncrypted;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_fileHash;
import org.telegram.tgnet.TLRPC$TL_fileLocationToBeDeprecated;
import org.telegram.tgnet.TLRPC$TL_inputDocumentFileLocation;
import org.telegram.tgnet.TLRPC$TL_inputPeerPhotoFileLocation;
import org.telegram.tgnet.TLRPC$TL_inputPhotoFileLocation;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetThumb;
import org.telegram.tgnet.TLRPC$TL_secureFile;
import org.telegram.tgnet.TLRPC$TL_theme;
import org.telegram.tgnet.TLRPC$TL_upload_cdnFile;
import org.telegram.tgnet.TLRPC$TL_upload_cdnFileReuploadNeeded;
import org.telegram.tgnet.TLRPC$TL_upload_file;
import org.telegram.tgnet.TLRPC$TL_upload_fileCdnRedirect;
import org.telegram.tgnet.TLRPC$TL_upload_getCdnFile;
import org.telegram.tgnet.TLRPC$TL_upload_getCdnFileHashes;
import org.telegram.tgnet.TLRPC$TL_upload_getFile;
import org.telegram.tgnet.TLRPC$TL_upload_getWebFile;
import org.telegram.tgnet.TLRPC$TL_upload_reuploadCdnFile;
import org.telegram.tgnet.TLRPC$TL_upload_webFile;
import org.telegram.tgnet.TLRPC$Vector;
import org.telegram.tgnet.TLRPC$WebPage;
import org.telegram.tgnet.tl.TL_stories$TL_storyItem;
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
    private File cacheFileGzipTemp;
    private File cacheFileParts;
    private File cacheFilePreload;
    private File cacheFileTemp;
    private File cacheIvTemp;
    private ArrayList<RequestInfo> cancelledRequestInfos;
    public volatile boolean caughtPremiumFloodWait;
    private byte[] cdnCheckBytes;
    private int cdnChunkCheckSize;
    private int cdnDatacenterId;
    private HashMap<Long, TLRPC$TL_fileHash> cdnHashes;
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
    protected TLRPC$InputFileLocation location;
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
    private boolean ungzip;
    private WebFile webFile;
    private TLRPC$InputWebFileLocation webLocation;
    private volatile boolean writingToFilePartsStream;
    public static volatile DispatchQueue filesQueue = new DispatchQueue("writeFileQueue");
    private static final Object lockObject = new Object();

    /* loaded from: classes3.dex */
    public interface FileLoadOperationDelegate {
        void didChangedLoadProgress(FileLoadOperation fileLoadOperation, long j, long j2);

        void didFailedLoadingFile(FileLoadOperation fileLoadOperation, int i);

        void didFinishLoadingFile(FileLoadOperation fileLoadOperation, File file);

        void didPreFinishLoading(FileLoadOperation fileLoadOperation, File file);

        boolean hasAnotherRefOnFile(String str);

        boolean isLocallyCreatedFile(String str);

        void saveFilePath(FilePathDatabase.PathData pathData, File file);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class PreloadRange {
        private long fileOffset;
        private long length;

        private PreloadRange(long j, long j2) {
            this.fileOffset = j;
            this.length = j2;
        }
    }

    /* loaded from: classes3.dex */
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

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes3.dex */
    public static class RequestInfo {
        public boolean cancelled;
        public boolean cancelling;
        public int chunkSize;
        public int connectionType;
        private boolean forceSmallChunk;
        private long offset;
        public long requestStartTime;
        public int requestToken;
        private TLRPC$TL_upload_file response;
        private TLRPC$TL_upload_cdnFile responseCdn;
        private TLRPC$TL_upload_webFile responseWeb;
        public Runnable whenCancelled;

        protected RequestInfo() {
        }
    }

    public FileLoadOperation(int i, WebFile webFile) {
        this.FULL_LOGS = false;
        this.downloadChunkSize = LiteMode.FLAG_CHAT_SCALE;
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
        TLRPC$TL_inputStickerSetThumb tLRPC$TL_inputStickerSetThumb;
        boolean z = false;
        this.FULL_LOGS = false;
        this.downloadChunkSize = LiteMode.FLAG_CHAT_SCALE;
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
        updateParams();
        this.parentObject = obj;
        this.isStory = obj instanceof TL_stories$TL_storyItem;
        this.fileMetadata = FileLoader.getFileMetadataFromParent(this.currentAccount, obj);
        this.isStream = imageLocation.imageType == 2;
        if (imageLocation.isEncrypted()) {
            TLRPC$InputFileLocation tLRPC$InputFileLocation = new TLRPC$InputFileLocation() { // from class: org.telegram.tgnet.TLRPC$TL_inputEncryptedFileLocation
                @Override // org.telegram.tgnet.TLObject
                public void readParams(AbstractSerializedData abstractSerializedData, boolean z2) {
                    this.id = abstractSerializedData.readInt64(z2);
                    this.access_hash = abstractSerializedData.readInt64(z2);
                }

                @Override // org.telegram.tgnet.TLObject
                public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                    abstractSerializedData.writeInt32(-182231723);
                    abstractSerializedData.writeInt64(this.id);
                    abstractSerializedData.writeInt64(this.access_hash);
                }
            };
            this.location = tLRPC$InputFileLocation;
            TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated = imageLocation.location;
            long j2 = tLRPC$TL_fileLocationToBeDeprecated.volume_id;
            tLRPC$InputFileLocation.id = j2;
            tLRPC$InputFileLocation.volume_id = j2;
            tLRPC$InputFileLocation.local_id = tLRPC$TL_fileLocationToBeDeprecated.local_id;
            tLRPC$InputFileLocation.access_hash = imageLocation.access_hash;
            byte[] bArr = new byte[32];
            this.iv = bArr;
            System.arraycopy(imageLocation.iv, 0, bArr, 0, 32);
            this.key = imageLocation.key;
        } else {
            if (imageLocation.photoPeer != null) {
                TLRPC$TL_inputPeerPhotoFileLocation tLRPC$TL_inputPeerPhotoFileLocation = new TLRPC$TL_inputPeerPhotoFileLocation();
                TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated2 = imageLocation.location;
                long j3 = tLRPC$TL_fileLocationToBeDeprecated2.volume_id;
                tLRPC$TL_inputPeerPhotoFileLocation.id = j3;
                tLRPC$TL_inputPeerPhotoFileLocation.volume_id = j3;
                tLRPC$TL_inputPeerPhotoFileLocation.local_id = tLRPC$TL_fileLocationToBeDeprecated2.local_id;
                tLRPC$TL_inputPeerPhotoFileLocation.photo_id = imageLocation.photoId;
                tLRPC$TL_inputPeerPhotoFileLocation.big = imageLocation.photoPeerType == 0;
                tLRPC$TL_inputPeerPhotoFileLocation.peer = imageLocation.photoPeer;
                tLRPC$TL_inputStickerSetThumb = tLRPC$TL_inputPeerPhotoFileLocation;
            } else if (imageLocation.stickerSet != null) {
                TLRPC$TL_inputStickerSetThumb tLRPC$TL_inputStickerSetThumb2 = new TLRPC$TL_inputStickerSetThumb();
                TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated3 = imageLocation.location;
                long j4 = tLRPC$TL_fileLocationToBeDeprecated3.volume_id;
                tLRPC$TL_inputStickerSetThumb2.id = j4;
                tLRPC$TL_inputStickerSetThumb2.volume_id = j4;
                tLRPC$TL_inputStickerSetThumb2.local_id = tLRPC$TL_fileLocationToBeDeprecated3.local_id;
                tLRPC$TL_inputStickerSetThumb2.thumb_version = imageLocation.thumbVersion;
                tLRPC$TL_inputStickerSetThumb2.stickerset = imageLocation.stickerSet;
                tLRPC$TL_inputStickerSetThumb = tLRPC$TL_inputStickerSetThumb2;
            } else if (imageLocation.thumbSize != null) {
                if (imageLocation.photoId != 0) {
                    TLRPC$TL_inputPhotoFileLocation tLRPC$TL_inputPhotoFileLocation = new TLRPC$TL_inputPhotoFileLocation();
                    this.location = tLRPC$TL_inputPhotoFileLocation;
                    tLRPC$TL_inputPhotoFileLocation.id = imageLocation.photoId;
                    TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated4 = imageLocation.location;
                    tLRPC$TL_inputPhotoFileLocation.volume_id = tLRPC$TL_fileLocationToBeDeprecated4.volume_id;
                    tLRPC$TL_inputPhotoFileLocation.local_id = tLRPC$TL_fileLocationToBeDeprecated4.local_id;
                    tLRPC$TL_inputPhotoFileLocation.access_hash = imageLocation.access_hash;
                    tLRPC$TL_inputPhotoFileLocation.file_reference = imageLocation.file_reference;
                    tLRPC$TL_inputPhotoFileLocation.thumb_size = imageLocation.thumbSize;
                    if (imageLocation.imageType == 2) {
                        this.allowDisordererFileSave = true;
                    }
                } else {
                    TLRPC$TL_inputDocumentFileLocation tLRPC$TL_inputDocumentFileLocation = new TLRPC$TL_inputDocumentFileLocation();
                    this.location = tLRPC$TL_inputDocumentFileLocation;
                    tLRPC$TL_inputDocumentFileLocation.id = imageLocation.documentId;
                    TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated5 = imageLocation.location;
                    tLRPC$TL_inputDocumentFileLocation.volume_id = tLRPC$TL_fileLocationToBeDeprecated5.volume_id;
                    tLRPC$TL_inputDocumentFileLocation.local_id = tLRPC$TL_fileLocationToBeDeprecated5.local_id;
                    tLRPC$TL_inputDocumentFileLocation.access_hash = imageLocation.access_hash;
                    tLRPC$TL_inputDocumentFileLocation.file_reference = imageLocation.file_reference;
                    tLRPC$TL_inputDocumentFileLocation.thumb_size = imageLocation.thumbSize;
                }
                TLRPC$InputFileLocation tLRPC$InputFileLocation2 = this.location;
                if (tLRPC$InputFileLocation2.file_reference == null) {
                    tLRPC$InputFileLocation2.file_reference = new byte[0];
                }
            } else {
                TLRPC$InputFileLocation tLRPC$InputFileLocation3 = new TLRPC$InputFileLocation() { // from class: org.telegram.tgnet.TLRPC$TL_inputFileLocation
                    @Override // org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData, boolean z2) {
                        this.volume_id = abstractSerializedData.readInt64(z2);
                        this.local_id = abstractSerializedData.readInt32(z2);
                        this.secret = abstractSerializedData.readInt64(z2);
                        this.file_reference = abstractSerializedData.readByteArray(z2);
                    }

                    @Override // org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                        abstractSerializedData.writeInt32(-539317279);
                        abstractSerializedData.writeInt64(this.volume_id);
                        abstractSerializedData.writeInt32(this.local_id);
                        abstractSerializedData.writeInt64(this.secret);
                        abstractSerializedData.writeByteArray(this.file_reference);
                    }
                };
                this.location = tLRPC$InputFileLocation3;
                TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated6 = imageLocation.location;
                tLRPC$InputFileLocation3.volume_id = tLRPC$TL_fileLocationToBeDeprecated6.volume_id;
                tLRPC$InputFileLocation3.local_id = tLRPC$TL_fileLocationToBeDeprecated6.local_id;
                tLRPC$InputFileLocation3.secret = imageLocation.access_hash;
                byte[] bArr2 = imageLocation.file_reference;
                tLRPC$InputFileLocation3.file_reference = bArr2;
                if (bArr2 == null) {
                    tLRPC$InputFileLocation3.file_reference = new byte[0];
                }
                this.allowDisordererFileSave = true;
            }
            this.location = tLRPC$TL_inputStickerSetThumb;
        }
        int i = imageLocation.imageType;
        this.ungzip = (i == 1 || i == 3) ? true : true;
        int i2 = imageLocation.dc_id;
        this.datacenterId = i2;
        this.initialDatacenterId = i2;
        this.currentType = ConnectionsManager.FileTypePhoto;
        this.totalBytesCount = j;
        this.ext = str == null ? "jpg" : str;
    }

    public FileLoadOperation(SecureDocument secureDocument) {
        this.FULL_LOGS = false;
        this.downloadChunkSize = LiteMode.FLAG_CHAT_SCALE;
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
        updateParams();
        TLRPC$InputFileLocation tLRPC$InputFileLocation = new TLRPC$InputFileLocation() { // from class: org.telegram.tgnet.TLRPC$TL_inputSecureFileLocation
            @Override // org.telegram.tgnet.TLObject
            public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
                this.id = abstractSerializedData.readInt64(z);
                this.access_hash = abstractSerializedData.readInt64(z);
            }

            @Override // org.telegram.tgnet.TLObject
            public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                abstractSerializedData.writeInt32(-876089816);
                abstractSerializedData.writeInt64(this.id);
                abstractSerializedData.writeInt64(this.access_hash);
            }
        };
        this.location = tLRPC$InputFileLocation;
        TLRPC$TL_secureFile tLRPC$TL_secureFile = secureDocument.secureFile;
        tLRPC$InputFileLocation.id = tLRPC$TL_secureFile.id;
        tLRPC$InputFileLocation.access_hash = tLRPC$TL_secureFile.access_hash;
        this.datacenterId = tLRPC$TL_secureFile.dc_id;
        this.totalBytesCount = tLRPC$TL_secureFile.size;
        this.allowDisordererFileSave = true;
        this.currentType = ConnectionsManager.FileTypeFile;
        this.ext = ".jpg";
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x0112 A[Catch: Exception -> 0x006b, TryCatch #0 {Exception -> 0x006b, blocks: (B:3:0x0032, B:6:0x0046, B:20:0x00ba, B:22:0x00c4, B:27:0x00d2, B:29:0x00dc, B:31:0x00e6, B:32:0x00ee, B:34:0x00f6, B:37:0x0100, B:38:0x0106, B:40:0x0112, B:45:0x0128, B:47:0x0130, B:41:0x0117, B:43:0x011f, B:44:0x0124, B:9:0x006e, B:11:0x0072, B:13:0x0089, B:14:0x008d, B:16:0x009e, B:18:0x00a8, B:19:0x00b7), top: B:51:0x0032 }] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0117 A[Catch: Exception -> 0x006b, TryCatch #0 {Exception -> 0x006b, blocks: (B:3:0x0032, B:6:0x0046, B:20:0x00ba, B:22:0x00c4, B:27:0x00d2, B:29:0x00dc, B:31:0x00e6, B:32:0x00ee, B:34:0x00f6, B:37:0x0100, B:38:0x0106, B:40:0x0112, B:45:0x0128, B:47:0x0130, B:41:0x0117, B:43:0x011f, B:44:0x0124, B:9:0x006e, B:11:0x0072, B:13:0x0089, B:14:0x008d, B:16:0x009e, B:18:0x00a8, B:19:0x00b7), top: B:51:0x0032 }] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0130 A[Catch: Exception -> 0x006b, TRY_LEAVE, TryCatch #0 {Exception -> 0x006b, blocks: (B:3:0x0032, B:6:0x0046, B:20:0x00ba, B:22:0x00c4, B:27:0x00d2, B:29:0x00dc, B:31:0x00e6, B:32:0x00ee, B:34:0x00f6, B:37:0x0100, B:38:0x0106, B:40:0x0112, B:45:0x0128, B:47:0x0130, B:41:0x0117, B:43:0x011f, B:44:0x0124, B:9:0x006e, B:11:0x0072, B:13:0x0089, B:14:0x008d, B:16:0x009e, B:18:0x00a8, B:19:0x00b7), top: B:51:0x0032 }] */
    /* JADX WARN: Removed duplicated region for block: B:55:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public FileLoadOperation(TLRPC$Document tLRPC$Document, Object obj) {
        boolean z;
        long j;
        String documentFileName;
        int lastIndexOf;
        this.FULL_LOGS = false;
        this.downloadChunkSize = LiteMode.FLAG_CHAT_SCALE;
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
        updateParams();
        try {
            this.parentObject = obj;
            this.isStory = obj instanceof TL_stories$TL_storyItem;
            this.fileMetadata = FileLoader.getFileMetadataFromParent(this.currentAccount, obj);
            String str = "";
            if (tLRPC$Document instanceof TLRPC$TL_documentEncrypted) {
                TLRPC$InputFileLocation tLRPC$InputFileLocation = new TLRPC$InputFileLocation() { // from class: org.telegram.tgnet.TLRPC$TL_inputEncryptedFileLocation
                    @Override // org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData, boolean z2) {
                        this.id = abstractSerializedData.readInt64(z2);
                        this.access_hash = abstractSerializedData.readInt64(z2);
                    }

                    @Override // org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                        abstractSerializedData.writeInt32(-182231723);
                        abstractSerializedData.writeInt64(this.id);
                        abstractSerializedData.writeInt64(this.access_hash);
                    }
                };
                this.location = tLRPC$InputFileLocation;
                tLRPC$InputFileLocation.id = tLRPC$Document.id;
                tLRPC$InputFileLocation.access_hash = tLRPC$Document.access_hash;
                int i = tLRPC$Document.dc_id;
                this.datacenterId = i;
                this.initialDatacenterId = i;
                byte[] bArr = new byte[32];
                this.iv = bArr;
                System.arraycopy(tLRPC$Document.iv, 0, bArr, 0, 32);
                this.key = tLRPC$Document.key;
            } else if (tLRPC$Document instanceof TLRPC$TL_document) {
                TLRPC$TL_inputDocumentFileLocation tLRPC$TL_inputDocumentFileLocation = new TLRPC$TL_inputDocumentFileLocation();
                this.location = tLRPC$TL_inputDocumentFileLocation;
                tLRPC$TL_inputDocumentFileLocation.id = tLRPC$Document.id;
                tLRPC$TL_inputDocumentFileLocation.access_hash = tLRPC$Document.access_hash;
                byte[] bArr2 = tLRPC$Document.file_reference;
                tLRPC$TL_inputDocumentFileLocation.file_reference = bArr2;
                tLRPC$TL_inputDocumentFileLocation.thumb_size = "";
                if (bArr2 == null) {
                    tLRPC$TL_inputDocumentFileLocation.file_reference = new byte[0];
                }
                int i2 = tLRPC$Document.dc_id;
                this.datacenterId = i2;
                this.initialDatacenterId = i2;
                this.allowDisordererFileSave = true;
                int size = tLRPC$Document.attributes.size();
                int i3 = 0;
                while (true) {
                    if (i3 >= size) {
                        break;
                    } else if (tLRPC$Document.attributes.get(i3) instanceof TLRPC$TL_documentAttributeVideo) {
                        this.supportsPreloading = true;
                        this.preloadPrefixSize = tLRPC$Document.attributes.get(i3).preload_prefix_size;
                        break;
                    } else {
                        i3++;
                    }
                }
            }
            if (!"application/x-tgsticker".equals(tLRPC$Document.mime_type) && !"application/x-tgwallpattern".equals(tLRPC$Document.mime_type)) {
                z = false;
                this.ungzip = z;
                j = tLRPC$Document.size;
                this.totalBytesCount = j;
                if (this.key != null && j % 16 != 0) {
                    long j2 = 16 - (j % 16);
                    this.bytesCountPadding = j2;
                    this.totalBytesCount = j + j2;
                }
                documentFileName = FileLoader.getDocumentFileName(tLRPC$Document);
                this.ext = documentFileName;
                if (documentFileName != null && (lastIndexOf = documentFileName.lastIndexOf(46)) != -1) {
                    str = this.ext.substring(lastIndexOf);
                }
                this.ext = str;
                if (!"audio/ogg".equals(tLRPC$Document.mime_type)) {
                    this.currentType = ConnectionsManager.FileTypeAudio;
                } else if (FileLoader.isVideoMimeType(tLRPC$Document.mime_type)) {
                    this.currentType = ConnectionsManager.FileTypeVideo;
                } else {
                    this.currentType = ConnectionsManager.FileTypeFile;
                }
                if (this.ext.length() > 1) {
                    this.ext = FileLoader.getExtensionByMimeType(tLRPC$Document.mime_type);
                    return;
                }
                return;
            }
            z = true;
            this.ungzip = z;
            j = tLRPC$Document.size;
            this.totalBytesCount = j;
            if (this.key != null) {
                long j22 = 16 - (j % 16);
                this.bytesCountPadding = j22;
                this.totalBytesCount = j + j22;
            }
            documentFileName = FileLoader.getDocumentFileName(tLRPC$Document);
            this.ext = documentFileName;
            if (documentFileName != null) {
                str = this.ext.substring(lastIndexOf);
            }
            this.ext = str;
            if (!"audio/ogg".equals(tLRPC$Document.mime_type)) {
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
            int i2 = (j > range.start ? 1 : (j == range.start ? 0 : -1));
            long j3 = range.end;
            if (i2 <= 0) {
                if (j2 >= j3) {
                    arrayList.remove(i);
                    z2 = true;
                    break;
                } else if (j2 > range.start) {
                    range.start = j2;
                    z2 = true;
                    break;
                }
            } else if (j2 < j3) {
                arrayList.add(0, new Range(range.start, j));
                range.start = j2;
                z2 = true;
                break;
            } else if (j < range.end) {
                range.end = j;
                z2 = true;
                break;
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
            Runnable runnable = new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda23
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
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$cancel$12(z);
            }
        });
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
                                FileLoadOperation.lambda$cancelRequests$13(FileLoadOperation.RequestInfo.this, iArr, runnable);
                            }
                        };
                        iArr[0] = iArr[0] + 1;
                        FileLog.d("cancelRequests cancel " + requestInfo.requestToken + " with callback");
                        ConnectionsManager.getInstance(this.currentAccount).cancelRequest(requestInfo.requestToken, true, new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda8
                            @Override // java.lang.Runnable
                            public final void run() {
                                FileLoadOperation.lambda$cancelRequests$14(FileLoadOperation.RequestInfo.this);
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
                            FileLoadOperation.this.lambda$clearOperation$21(requestInfo2);
                        }
                    };
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(requestInfo2.requestToken, true, new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda10
                        @Override // java.lang.Runnable
                        public final void run() {
                            FileLoadOperation.lambda$clearOperation$22(FileLoadOperation.RequestInfo.this);
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
                        if (j5 <= 2147483647L) {
                            this.preloadTempBufferCount = (int) j5;
                            nativeByteBuffer.position(nativeByteBuffer.limit() - this.preloadTempBufferCount);
                            nativeByteBuffer.readBytes(this.preloadTempBuffer, 0, this.preloadTempBufferCount, false);
                            return j3;
                        }
                        throw new RuntimeException("!!!");
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
        } else if (this.state == 3) {
            return j2;
        } else {
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
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancel$11() {
        if (this.state == 5) {
            onFail(false, 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancel$12(boolean z) {
        if (this.state != 3 && this.state != 2) {
            this.state = 5;
            cancelRequests(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    FileLoadOperation.this.lambda$cancel$11();
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

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$cancelRequests$13(RequestInfo requestInfo, int[] iArr, Runnable runnable) {
        requestInfo.whenCancelled = null;
        requestInfo.cancelled = true;
        int i = iArr[0] - 1;
        iArr[0] = i;
        if (i == 0) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$cancelRequests$14(RequestInfo requestInfo) {
        Runnable runnable = requestInfo.whenCancelled;
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearOperation$21(RequestInfo requestInfo) {
        requestInfo.whenCancelled = null;
        this.cancelledRequestInfos.remove(requestInfo);
        requestInfo.cancelled = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$clearOperation$22(RequestInfo requestInfo) {
        Runnable runnable = requestInfo.whenCancelled;
        if (runnable != null) {
            runnable.run();
        }
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
    public /* synthetic */ void lambda$onFail$20(int i) {
        FileLoadOperationDelegate fileLoadOperationDelegate = this.delegate;
        if (fileLoadOperationDelegate != null) {
            fileLoadOperationDelegate.didFailedLoadingFile(this, i);
        }
        notifyStreamListeners();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishLoadingFile$15(boolean z) {
        try {
            onFinishLoadingFile(z, 0, false);
        } catch (Exception unused) {
            onFail(false, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishLoadingFile$16() {
        onFail(false, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishLoadingFile$17(boolean z) {
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
    /* JADX WARN: Removed duplicated region for block: B:25:0x006d  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0176  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$onFinishLoadingFile$18(File file, File file2, File file3, File file4, final boolean z) {
        Throwable th;
        File file5;
        boolean renameTo;
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
            boolean z2 = false;
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
                Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        FileLoadOperation.this.lambda$onFinishLoadingFile$16();
                    }
                });
                return;
            }
            try {
                if (this.parentObject instanceof TLRPC$TL_theme) {
                    renameTo = AndroidUtilities.copyFile(file4, this.cacheFileFinal);
                } else {
                    if (this.pathSaveData != null) {
                        synchronized (lockObject) {
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
                        }
                    }
                    renameTo = file4.renameTo(this.cacheFileFinal);
                }
                z2 = renameTo;
            } catch (Exception e) {
                FileLog.e(e);
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
            if (!z2) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("unable to rename temp = " + file4 + " to final = " + this.cacheFileFinal + " retry = " + this.renameRetryCount);
                }
                int i2 = this.renameRetryCount + 1;
                this.renameRetryCount = i2;
                if (i2 < 3) {
                    this.state = 1;
                    Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda4
                        @Override // java.lang.Runnable
                        public final void run() {
                            FileLoadOperation.this.lambda$onFinishLoadingFile$15(z);
                        }
                    }, 200L);
                    return;
                }
                this.cacheFileFinal = file4;
            } else if (this.pathSaveData != null && this.cacheFileFinal.exists()) {
                this.delegate.saveFilePath(this.pathSaveData, this.cacheFileFinal);
            }
        }
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$onFinishLoadingFile$17(z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$pause$6() {
        if (!this.isStory) {
            for (int i = 0; i < this.requestInfos.size(); i++) {
                ConnectionsManager.getInstance(this.currentAccount).failNotRunningRequest(this.requestInfos.get(i).requestToken);
            }
            return;
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("debug_loading:" + this.cacheFileFinal.getName() + " pause operation, clear requests");
        }
        clearOperation(null, false, true);
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
        FileLog.e("FileLoadOperation " + getFileName() + " removing stream listener " + this.stream);
        this.streamListeners.remove(fileLoadOperationStream);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestFileOffsets$19(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error != null) {
            onFail(false, 0);
            return;
        }
        this.requestingCdnOffsets = false;
        TLRPC$Vector tLRPC$Vector = (TLRPC$Vector) tLObject;
        if (!tLRPC$Vector.objects.isEmpty()) {
            if (this.cdnHashes == null) {
                this.cdnHashes = new HashMap<>();
            }
            for (int i = 0; i < tLRPC$Vector.objects.size(); i++) {
                TLRPC$TL_fileHash tLRPC$TL_fileHash = (TLRPC$TL_fileHash) tLRPC$Vector.objects.get(i);
                this.cdnHashes.put(Long.valueOf(tLRPC$TL_fileHash.offset), tLRPC$TL_fileHash);
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
                } else if (requestInfo.responseCdn != null) {
                    requestInfo.responseCdn.disableFree = false;
                    requestInfo.responseCdn.freeResources();
                    return;
                } else {
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setIsPreloadVideoOperation$10(boolean z) {
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
        if (fileLoadOperationStream == null || this.state == 1 || this.state == 0) {
            return;
        }
        fileLoadOperationStream.newDataAvailable();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$7(boolean z, long j, FileLoadOperationStream fileLoadOperationStream, boolean z2) {
        if (this.streamListeners == null) {
            this.streamListeners = new ArrayList<>();
        }
        if (z) {
            long j2 = this.currentDownloadChunkSize;
            long j3 = (j / j2) * j2;
            RequestInfo requestInfo = this.priorityRequestInfo;
            if (requestInfo != null && requestInfo.offset != j3) {
                this.requestInfos.remove(this.priorityRequestInfo);
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
    public /* synthetic */ void lambda$start$8() {
        startDownloadRequest(-1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$9(boolean[] zArr) {
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
    public /* synthetic */ void lambda$startDownloadRequest$23(RequestInfo requestInfo) {
        processRequestResult(requestInfo, null);
        requestInfo.response.freeResources();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startDownloadRequest$24(int i, RequestInfo requestInfo, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.reuploadingCdn = false;
        if (tLRPC$TL_error == null) {
            TLRPC$Vector tLRPC$Vector = (TLRPC$Vector) tLObject;
            if (!tLRPC$Vector.objects.isEmpty()) {
                if (this.cdnHashes == null) {
                    this.cdnHashes = new HashMap<>();
                }
                for (int i2 = 0; i2 < tLRPC$Vector.objects.size(); i2++) {
                    TLRPC$TL_fileHash tLRPC$TL_fileHash = (TLRPC$TL_fileHash) tLRPC$Vector.objects.get(i2);
                    this.cdnHashes.put(Long.valueOf(tLRPC$TL_fileHash.offset), tLRPC$TL_fileHash);
                }
            }
        } else if (!tLRPC$TL_error.text.equals("FILE_TOKEN_INVALID") && !tLRPC$TL_error.text.equals("REQUEST_TOKEN_INVALID")) {
            onFail(false, 0);
            return;
        } else {
            this.isCdn = false;
            clearOperation(requestInfo, false, false);
        }
        startDownloadRequest(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startDownloadRequest$25(final RequestInfo requestInfo, int i, final int i2, TLObject tLObject, TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error) {
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
        if (tLRPC$TL_error != null) {
            Runnable runnable = requestInfo.whenCancelled;
            if (runnable != null) {
                runnable.run();
            }
            if (tLRPC$TL_error.code == -2000) {
                this.requestInfos.remove(requestInfo);
                this.requestedBytesCount -= requestInfo.chunkSize;
                removePart(this.notRequestedBytesRanges, requestInfo.offset, requestInfo.offset + requestInfo.chunkSize);
                return;
            } else if (FileRefController.isFileRefError(tLRPC$TL_error.text)) {
                requestReference(requestInfo);
                return;
            } else if ((tLObject instanceof TLRPC$TL_upload_getCdnFile) && tLRPC$TL_error.text.equals("FILE_TOKEN_INVALID")) {
                this.isCdn = false;
                clearOperation(requestInfo, false, false);
                startDownloadRequest(i2);
                return;
            }
        }
        if (tLObject2 instanceof TLRPC$TL_upload_fileCdnRedirect) {
            TLRPC$TL_upload_fileCdnRedirect tLRPC$TL_upload_fileCdnRedirect = (TLRPC$TL_upload_fileCdnRedirect) tLObject2;
            if (!tLRPC$TL_upload_fileCdnRedirect.file_hashes.isEmpty()) {
                if (this.cdnHashes == null) {
                    this.cdnHashes = new HashMap<>();
                }
                for (int i7 = 0; i7 < tLRPC$TL_upload_fileCdnRedirect.file_hashes.size(); i7++) {
                    TLRPC$TL_fileHash tLRPC$TL_fileHash = (TLRPC$TL_fileHash) tLRPC$TL_upload_fileCdnRedirect.file_hashes.get(i7);
                    this.cdnHashes.put(Long.valueOf(tLRPC$TL_fileHash.offset), tLRPC$TL_fileHash);
                }
            }
            byte[] bArr2 = tLRPC$TL_upload_fileCdnRedirect.encryption_iv;
            if (bArr2 == null || (bArr = tLRPC$TL_upload_fileCdnRedirect.encryption_key) == null || bArr2.length != 16 || bArr.length != 32) {
                Runnable runnable2 = requestInfo.whenCancelled;
                if (runnable2 != null) {
                    runnable2.run();
                }
                TLRPC$TL_error tLRPC$TL_error2 = new TLRPC$TL_error();
                tLRPC$TL_error2.text = "bad redirect response";
                tLRPC$TL_error2.code = 400;
                processRequestResult(requestInfo, tLRPC$TL_error2);
                return;
            }
            this.isCdn = true;
            if (this.notCheckedCdnRanges == null) {
                ArrayList<Range> arrayList = new ArrayList<>();
                this.notCheckedCdnRanges = arrayList;
                arrayList.add(new Range(0L, this.maxCdnParts));
            }
            this.cdnDatacenterId = tLRPC$TL_upload_fileCdnRedirect.dc_id;
            this.cdnIv = tLRPC$TL_upload_fileCdnRedirect.encryption_iv;
            this.cdnKey = tLRPC$TL_upload_fileCdnRedirect.encryption_key;
            this.cdnToken = tLRPC$TL_upload_fileCdnRedirect.file_token;
            clearOperation(requestInfo, false, false);
            startDownloadRequest(i2);
        } else if (tLObject2 instanceof TLRPC$TL_upload_cdnFileReuploadNeeded) {
            if (this.reuploadingCdn) {
                return;
            }
            clearOperation(requestInfo, false, false);
            this.reuploadingCdn = true;
            TLRPC$TL_upload_reuploadCdnFile tLRPC$TL_upload_reuploadCdnFile = new TLRPC$TL_upload_reuploadCdnFile();
            tLRPC$TL_upload_reuploadCdnFile.file_token = this.cdnToken;
            tLRPC$TL_upload_reuploadCdnFile.request_token = ((TLRPC$TL_upload_cdnFileReuploadNeeded) tLObject2).request_token;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_upload_reuploadCdnFile, new RequestDelegate() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda0
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject3, TLRPC$TL_error tLRPC$TL_error3) {
                    FileLoadOperation.this.lambda$startDownloadRequest$24(i2, requestInfo, tLObject3, tLRPC$TL_error3);
                }
            }, null, null, 0, this.datacenterId, 1, true);
        } else {
            if (tLObject2 instanceof TLRPC$TL_upload_file) {
                requestInfo.response = (TLRPC$TL_upload_file) tLObject2;
            } else if (tLObject2 instanceof TLRPC$TL_upload_webFile) {
                requestInfo.responseWeb = (TLRPC$TL_upload_webFile) tLObject2;
                if (this.totalBytesCount == 0 && requestInfo.responseWeb.size != 0) {
                    this.totalBytesCount = requestInfo.responseWeb.size;
                }
            } else {
                requestInfo.responseCdn = (TLRPC$TL_upload_cdnFile) tLObject2;
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
            processRequestResult(requestInfo, tLRPC$TL_error);
            Runnable runnable3 = requestInfo.whenCancelled;
            if (runnable3 != null) {
                runnable3.run();
            }
        }
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
                filesQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda14
                    @Override // java.lang.Runnable
                    public final void run() {
                        FileLoadOperation.this.lambda$onFinishLoadingFile$18(file, file2, file3, file4, z);
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
        Collections.sort(arrayList, new Comparator() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda19
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
        TLRPC$TL_upload_getCdnFileHashes tLRPC$TL_upload_getCdnFileHashes = new TLRPC$TL_upload_getCdnFileHashes();
        tLRPC$TL_upload_getCdnFileHashes.file_token = this.cdnToken;
        tLRPC$TL_upload_getCdnFileHashes.offset = j;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_upload_getCdnFileHashes, new RequestDelegate() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda20
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                FileLoadOperation.this.lambda$requestFileOffsets$19(tLObject, tLRPC$TL_error);
            }
        }, null, null, 0, this.datacenterId, 1, true);
    }

    private void requestReference(RequestInfo requestInfo) {
        TLRPC$Message tLRPC$Message;
        TLRPC$MessageMedia tLRPC$MessageMedia;
        TLRPC$WebPage tLRPC$WebPage;
        if (this.requestingReference) {
            return;
        }
        clearOperation(null, false, false);
        this.requestingReference = true;
        Object obj = this.parentObject;
        if (obj instanceof MessageObject) {
            MessageObject messageObject = (MessageObject) obj;
            if (messageObject.getId() < 0 && (tLRPC$Message = messageObject.messageOwner) != null && (tLRPC$MessageMedia = tLRPC$Message.media) != null && (tLRPC$WebPage = tLRPC$MessageMedia.webpage) != null) {
                this.parentObject = tLRPC$WebPage;
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

    /* JADX INFO: Access modifiers changed from: protected */
    public File getCacheFileFinal() {
        return this.cacheFileFinal;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public File getCurrentFile() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final File[] fileArr = new File[1];
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda15
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

    /* JADX INFO: Access modifiers changed from: protected */
    public File getCurrentFileFast() {
        return (this.state != 3 || this.preloadFinished) ? this.cacheFileTemp : this.cacheFileFinal;
    }

    public int getCurrentType() {
        return this.currentType;
    }

    public int getDatacenterId() {
        return this.initialDatacenterId;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public float getDownloadedLengthFromOffset(float f) {
        ArrayList<Range> arrayList = this.notLoadedBytesRangesCopy;
        long j = this.totalBytesCount;
        if (j == 0 || arrayList == null) {
            return 0.0f;
        }
        return f + (((float) getDownloadedLengthFromOffsetInternal(arrayList, (int) (((float) j) * f), j)) / ((float) this.totalBytesCount));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long[] getDownloadedLengthFromOffset(final long j, final long j2) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final long[] jArr = new long[2];
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda21
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

    /* JADX INFO: Access modifiers changed from: protected */
    public void onFail(boolean z, final int i) {
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
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    FileLoadOperation.this.lambda$onFail$20(i);
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
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda25
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$pause$6();
            }
        });
    }

    /* JADX WARN: Code restructure failed: missing block: B:159:0x0416, code lost:
        if (r1 == (r5 - r3)) goto L214;
     */
    /* JADX WARN: Removed duplicated region for block: B:104:0x029d  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x02cc A[Catch: Exception -> 0x008f, TryCatch #1 {Exception -> 0x008f, blocks: (B:14:0x007d, B:16:0x0081, B:18:0x008b, B:22:0x0092, B:24:0x0098, B:34:0x00bd, B:37:0x00c7, B:39:0x00cf, B:41:0x00dd, B:44:0x00eb, B:46:0x00f2, B:48:0x0106, B:49:0x013b, B:51:0x013f, B:53:0x0163, B:54:0x018b, B:56:0x018f, B:57:0x0196, B:59:0x01c1, B:61:0x01d3, B:63:0x01e8, B:64:0x01f1, B:66:0x01fd, B:68:0x0207, B:65:0x01f4, B:67:0x0200, B:69:0x0209, B:71:0x022c, B:73:0x0230, B:75:0x0236, B:77:0x023c, B:83:0x0248, B:206:0x059d, B:208:0x05a5, B:210:0x05b1, B:213:0x05bc, B:214:0x05bf, B:216:0x05cb, B:218:0x05d1, B:219:0x05e0, B:221:0x05e6, B:222:0x05f5, B:224:0x05fb, B:226:0x060b, B:228:0x0613, B:230:0x0618, B:232:0x061d, B:84:0x0257, B:86:0x025b, B:87:0x0265, B:91:0x0277, B:93:0x027b, B:95:0x0280, B:97:0x0286, B:102:0x0292, B:122:0x02c6, B:124:0x02cc, B:126:0x02e5, B:128:0x02ed, B:133:0x0301, B:134:0x0317, B:135:0x0318, B:136:0x031c, B:138:0x0320, B:139:0x0358, B:141:0x035c, B:143:0x0369, B:144:0x039f, B:146:0x03c4, B:148:0x03d6, B:150:0x03e6, B:152:0x03ee, B:154:0x0403, B:156:0x040a, B:158:0x0412, B:166:0x0428, B:168:0x0438, B:169:0x0449, B:174:0x045a, B:175:0x0461, B:176:0x0462, B:178:0x046f, B:180:0x04b1, B:182:0x04c0, B:184:0x04c4, B:186:0x04c8, B:187:0x050d, B:191:0x052e, B:188:0x0512, B:190:0x0516, B:192:0x0531, B:194:0x053b, B:196:0x056e, B:198:0x0572, B:199:0x057e, B:201:0x0586, B:203:0x058b, B:195:0x0553, B:106:0x02a1, B:110:0x02ab, B:233:0x0623, B:25:0x009f, B:27:0x00a5, B:28:0x00ac, B:30:0x00b2), top: B:296:0x007d }] */
    /* JADX WARN: Removed duplicated region for block: B:138:0x0320 A[Catch: Exception -> 0x008f, TryCatch #1 {Exception -> 0x008f, blocks: (B:14:0x007d, B:16:0x0081, B:18:0x008b, B:22:0x0092, B:24:0x0098, B:34:0x00bd, B:37:0x00c7, B:39:0x00cf, B:41:0x00dd, B:44:0x00eb, B:46:0x00f2, B:48:0x0106, B:49:0x013b, B:51:0x013f, B:53:0x0163, B:54:0x018b, B:56:0x018f, B:57:0x0196, B:59:0x01c1, B:61:0x01d3, B:63:0x01e8, B:64:0x01f1, B:66:0x01fd, B:68:0x0207, B:65:0x01f4, B:67:0x0200, B:69:0x0209, B:71:0x022c, B:73:0x0230, B:75:0x0236, B:77:0x023c, B:83:0x0248, B:206:0x059d, B:208:0x05a5, B:210:0x05b1, B:213:0x05bc, B:214:0x05bf, B:216:0x05cb, B:218:0x05d1, B:219:0x05e0, B:221:0x05e6, B:222:0x05f5, B:224:0x05fb, B:226:0x060b, B:228:0x0613, B:230:0x0618, B:232:0x061d, B:84:0x0257, B:86:0x025b, B:87:0x0265, B:91:0x0277, B:93:0x027b, B:95:0x0280, B:97:0x0286, B:102:0x0292, B:122:0x02c6, B:124:0x02cc, B:126:0x02e5, B:128:0x02ed, B:133:0x0301, B:134:0x0317, B:135:0x0318, B:136:0x031c, B:138:0x0320, B:139:0x0358, B:141:0x035c, B:143:0x0369, B:144:0x039f, B:146:0x03c4, B:148:0x03d6, B:150:0x03e6, B:152:0x03ee, B:154:0x0403, B:156:0x040a, B:158:0x0412, B:166:0x0428, B:168:0x0438, B:169:0x0449, B:174:0x045a, B:175:0x0461, B:176:0x0462, B:178:0x046f, B:180:0x04b1, B:182:0x04c0, B:184:0x04c4, B:186:0x04c8, B:187:0x050d, B:191:0x052e, B:188:0x0512, B:190:0x0516, B:192:0x0531, B:194:0x053b, B:196:0x056e, B:198:0x0572, B:199:0x057e, B:201:0x0586, B:203:0x058b, B:195:0x0553, B:106:0x02a1, B:110:0x02ab, B:233:0x0623, B:25:0x009f, B:27:0x00a5, B:28:0x00ac, B:30:0x00b2), top: B:296:0x007d }] */
    /* JADX WARN: Removed duplicated region for block: B:141:0x035c A[Catch: Exception -> 0x008f, TryCatch #1 {Exception -> 0x008f, blocks: (B:14:0x007d, B:16:0x0081, B:18:0x008b, B:22:0x0092, B:24:0x0098, B:34:0x00bd, B:37:0x00c7, B:39:0x00cf, B:41:0x00dd, B:44:0x00eb, B:46:0x00f2, B:48:0x0106, B:49:0x013b, B:51:0x013f, B:53:0x0163, B:54:0x018b, B:56:0x018f, B:57:0x0196, B:59:0x01c1, B:61:0x01d3, B:63:0x01e8, B:64:0x01f1, B:66:0x01fd, B:68:0x0207, B:65:0x01f4, B:67:0x0200, B:69:0x0209, B:71:0x022c, B:73:0x0230, B:75:0x0236, B:77:0x023c, B:83:0x0248, B:206:0x059d, B:208:0x05a5, B:210:0x05b1, B:213:0x05bc, B:214:0x05bf, B:216:0x05cb, B:218:0x05d1, B:219:0x05e0, B:221:0x05e6, B:222:0x05f5, B:224:0x05fb, B:226:0x060b, B:228:0x0613, B:230:0x0618, B:232:0x061d, B:84:0x0257, B:86:0x025b, B:87:0x0265, B:91:0x0277, B:93:0x027b, B:95:0x0280, B:97:0x0286, B:102:0x0292, B:122:0x02c6, B:124:0x02cc, B:126:0x02e5, B:128:0x02ed, B:133:0x0301, B:134:0x0317, B:135:0x0318, B:136:0x031c, B:138:0x0320, B:139:0x0358, B:141:0x035c, B:143:0x0369, B:144:0x039f, B:146:0x03c4, B:148:0x03d6, B:150:0x03e6, B:152:0x03ee, B:154:0x0403, B:156:0x040a, B:158:0x0412, B:166:0x0428, B:168:0x0438, B:169:0x0449, B:174:0x045a, B:175:0x0461, B:176:0x0462, B:178:0x046f, B:180:0x04b1, B:182:0x04c0, B:184:0x04c4, B:186:0x04c8, B:187:0x050d, B:191:0x052e, B:188:0x0512, B:190:0x0516, B:192:0x0531, B:194:0x053b, B:196:0x056e, B:198:0x0572, B:199:0x057e, B:201:0x0586, B:203:0x058b, B:195:0x0553, B:106:0x02a1, B:110:0x02ab, B:233:0x0623, B:25:0x009f, B:27:0x00a5, B:28:0x00ac, B:30:0x00b2), top: B:296:0x007d }] */
    /* JADX WARN: Removed duplicated region for block: B:146:0x03c4 A[Catch: Exception -> 0x008f, TryCatch #1 {Exception -> 0x008f, blocks: (B:14:0x007d, B:16:0x0081, B:18:0x008b, B:22:0x0092, B:24:0x0098, B:34:0x00bd, B:37:0x00c7, B:39:0x00cf, B:41:0x00dd, B:44:0x00eb, B:46:0x00f2, B:48:0x0106, B:49:0x013b, B:51:0x013f, B:53:0x0163, B:54:0x018b, B:56:0x018f, B:57:0x0196, B:59:0x01c1, B:61:0x01d3, B:63:0x01e8, B:64:0x01f1, B:66:0x01fd, B:68:0x0207, B:65:0x01f4, B:67:0x0200, B:69:0x0209, B:71:0x022c, B:73:0x0230, B:75:0x0236, B:77:0x023c, B:83:0x0248, B:206:0x059d, B:208:0x05a5, B:210:0x05b1, B:213:0x05bc, B:214:0x05bf, B:216:0x05cb, B:218:0x05d1, B:219:0x05e0, B:221:0x05e6, B:222:0x05f5, B:224:0x05fb, B:226:0x060b, B:228:0x0613, B:230:0x0618, B:232:0x061d, B:84:0x0257, B:86:0x025b, B:87:0x0265, B:91:0x0277, B:93:0x027b, B:95:0x0280, B:97:0x0286, B:102:0x0292, B:122:0x02c6, B:124:0x02cc, B:126:0x02e5, B:128:0x02ed, B:133:0x0301, B:134:0x0317, B:135:0x0318, B:136:0x031c, B:138:0x0320, B:139:0x0358, B:141:0x035c, B:143:0x0369, B:144:0x039f, B:146:0x03c4, B:148:0x03d6, B:150:0x03e6, B:152:0x03ee, B:154:0x0403, B:156:0x040a, B:158:0x0412, B:166:0x0428, B:168:0x0438, B:169:0x0449, B:174:0x045a, B:175:0x0461, B:176:0x0462, B:178:0x046f, B:180:0x04b1, B:182:0x04c0, B:184:0x04c4, B:186:0x04c8, B:187:0x050d, B:191:0x052e, B:188:0x0512, B:190:0x0516, B:192:0x0531, B:194:0x053b, B:196:0x056e, B:198:0x0572, B:199:0x057e, B:201:0x0586, B:203:0x058b, B:195:0x0553, B:106:0x02a1, B:110:0x02ab, B:233:0x0623, B:25:0x009f, B:27:0x00a5, B:28:0x00ac, B:30:0x00b2), top: B:296:0x007d }] */
    /* JADX WARN: Removed duplicated region for block: B:198:0x0572 A[Catch: Exception -> 0x008f, TryCatch #1 {Exception -> 0x008f, blocks: (B:14:0x007d, B:16:0x0081, B:18:0x008b, B:22:0x0092, B:24:0x0098, B:34:0x00bd, B:37:0x00c7, B:39:0x00cf, B:41:0x00dd, B:44:0x00eb, B:46:0x00f2, B:48:0x0106, B:49:0x013b, B:51:0x013f, B:53:0x0163, B:54:0x018b, B:56:0x018f, B:57:0x0196, B:59:0x01c1, B:61:0x01d3, B:63:0x01e8, B:64:0x01f1, B:66:0x01fd, B:68:0x0207, B:65:0x01f4, B:67:0x0200, B:69:0x0209, B:71:0x022c, B:73:0x0230, B:75:0x0236, B:77:0x023c, B:83:0x0248, B:206:0x059d, B:208:0x05a5, B:210:0x05b1, B:213:0x05bc, B:214:0x05bf, B:216:0x05cb, B:218:0x05d1, B:219:0x05e0, B:221:0x05e6, B:222:0x05f5, B:224:0x05fb, B:226:0x060b, B:228:0x0613, B:230:0x0618, B:232:0x061d, B:84:0x0257, B:86:0x025b, B:87:0x0265, B:91:0x0277, B:93:0x027b, B:95:0x0280, B:97:0x0286, B:102:0x0292, B:122:0x02c6, B:124:0x02cc, B:126:0x02e5, B:128:0x02ed, B:133:0x0301, B:134:0x0317, B:135:0x0318, B:136:0x031c, B:138:0x0320, B:139:0x0358, B:141:0x035c, B:143:0x0369, B:144:0x039f, B:146:0x03c4, B:148:0x03d6, B:150:0x03e6, B:152:0x03ee, B:154:0x0403, B:156:0x040a, B:158:0x0412, B:166:0x0428, B:168:0x0438, B:169:0x0449, B:174:0x045a, B:175:0x0461, B:176:0x0462, B:178:0x046f, B:180:0x04b1, B:182:0x04c0, B:184:0x04c4, B:186:0x04c8, B:187:0x050d, B:191:0x052e, B:188:0x0512, B:190:0x0516, B:192:0x0531, B:194:0x053b, B:196:0x056e, B:198:0x0572, B:199:0x057e, B:201:0x0586, B:203:0x058b, B:195:0x0553, B:106:0x02a1, B:110:0x02ab, B:233:0x0623, B:25:0x009f, B:27:0x00a5, B:28:0x00ac, B:30:0x00b2), top: B:296:0x007d }] */
    /* JADX WARN: Removed duplicated region for block: B:226:0x060b A[Catch: Exception -> 0x008f, TryCatch #1 {Exception -> 0x008f, blocks: (B:14:0x007d, B:16:0x0081, B:18:0x008b, B:22:0x0092, B:24:0x0098, B:34:0x00bd, B:37:0x00c7, B:39:0x00cf, B:41:0x00dd, B:44:0x00eb, B:46:0x00f2, B:48:0x0106, B:49:0x013b, B:51:0x013f, B:53:0x0163, B:54:0x018b, B:56:0x018f, B:57:0x0196, B:59:0x01c1, B:61:0x01d3, B:63:0x01e8, B:64:0x01f1, B:66:0x01fd, B:68:0x0207, B:65:0x01f4, B:67:0x0200, B:69:0x0209, B:71:0x022c, B:73:0x0230, B:75:0x0236, B:77:0x023c, B:83:0x0248, B:206:0x059d, B:208:0x05a5, B:210:0x05b1, B:213:0x05bc, B:214:0x05bf, B:216:0x05cb, B:218:0x05d1, B:219:0x05e0, B:221:0x05e6, B:222:0x05f5, B:224:0x05fb, B:226:0x060b, B:228:0x0613, B:230:0x0618, B:232:0x061d, B:84:0x0257, B:86:0x025b, B:87:0x0265, B:91:0x0277, B:93:0x027b, B:95:0x0280, B:97:0x0286, B:102:0x0292, B:122:0x02c6, B:124:0x02cc, B:126:0x02e5, B:128:0x02ed, B:133:0x0301, B:134:0x0317, B:135:0x0318, B:136:0x031c, B:138:0x0320, B:139:0x0358, B:141:0x035c, B:143:0x0369, B:144:0x039f, B:146:0x03c4, B:148:0x03d6, B:150:0x03e6, B:152:0x03ee, B:154:0x0403, B:156:0x040a, B:158:0x0412, B:166:0x0428, B:168:0x0438, B:169:0x0449, B:174:0x045a, B:175:0x0461, B:176:0x0462, B:178:0x046f, B:180:0x04b1, B:182:0x04c0, B:184:0x04c4, B:186:0x04c8, B:187:0x050d, B:191:0x052e, B:188:0x0512, B:190:0x0516, B:192:0x0531, B:194:0x053b, B:196:0x056e, B:198:0x0572, B:199:0x057e, B:201:0x0586, B:203:0x058b, B:195:0x0553, B:106:0x02a1, B:110:0x02ab, B:233:0x0623, B:25:0x009f, B:27:0x00a5, B:28:0x00ac, B:30:0x00b2), top: B:296:0x007d }] */
    /* JADX WARN: Removed duplicated region for block: B:228:0x0613 A[Catch: Exception -> 0x008f, TryCatch #1 {Exception -> 0x008f, blocks: (B:14:0x007d, B:16:0x0081, B:18:0x008b, B:22:0x0092, B:24:0x0098, B:34:0x00bd, B:37:0x00c7, B:39:0x00cf, B:41:0x00dd, B:44:0x00eb, B:46:0x00f2, B:48:0x0106, B:49:0x013b, B:51:0x013f, B:53:0x0163, B:54:0x018b, B:56:0x018f, B:57:0x0196, B:59:0x01c1, B:61:0x01d3, B:63:0x01e8, B:64:0x01f1, B:66:0x01fd, B:68:0x0207, B:65:0x01f4, B:67:0x0200, B:69:0x0209, B:71:0x022c, B:73:0x0230, B:75:0x0236, B:77:0x023c, B:83:0x0248, B:206:0x059d, B:208:0x05a5, B:210:0x05b1, B:213:0x05bc, B:214:0x05bf, B:216:0x05cb, B:218:0x05d1, B:219:0x05e0, B:221:0x05e6, B:222:0x05f5, B:224:0x05fb, B:226:0x060b, B:228:0x0613, B:230:0x0618, B:232:0x061d, B:84:0x0257, B:86:0x025b, B:87:0x0265, B:91:0x0277, B:93:0x027b, B:95:0x0280, B:97:0x0286, B:102:0x0292, B:122:0x02c6, B:124:0x02cc, B:126:0x02e5, B:128:0x02ed, B:133:0x0301, B:134:0x0317, B:135:0x0318, B:136:0x031c, B:138:0x0320, B:139:0x0358, B:141:0x035c, B:143:0x0369, B:144:0x039f, B:146:0x03c4, B:148:0x03d6, B:150:0x03e6, B:152:0x03ee, B:154:0x0403, B:156:0x040a, B:158:0x0412, B:166:0x0428, B:168:0x0438, B:169:0x0449, B:174:0x045a, B:175:0x0461, B:176:0x0462, B:178:0x046f, B:180:0x04b1, B:182:0x04c0, B:184:0x04c4, B:186:0x04c8, B:187:0x050d, B:191:0x052e, B:188:0x0512, B:190:0x0516, B:192:0x0531, B:194:0x053b, B:196:0x056e, B:198:0x0572, B:199:0x057e, B:201:0x0586, B:203:0x058b, B:195:0x0553, B:106:0x02a1, B:110:0x02ab, B:233:0x0623, B:25:0x009f, B:27:0x00a5, B:28:0x00ac, B:30:0x00b2), top: B:296:0x007d }] */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0248 A[Catch: Exception -> 0x008f, TryCatch #1 {Exception -> 0x008f, blocks: (B:14:0x007d, B:16:0x0081, B:18:0x008b, B:22:0x0092, B:24:0x0098, B:34:0x00bd, B:37:0x00c7, B:39:0x00cf, B:41:0x00dd, B:44:0x00eb, B:46:0x00f2, B:48:0x0106, B:49:0x013b, B:51:0x013f, B:53:0x0163, B:54:0x018b, B:56:0x018f, B:57:0x0196, B:59:0x01c1, B:61:0x01d3, B:63:0x01e8, B:64:0x01f1, B:66:0x01fd, B:68:0x0207, B:65:0x01f4, B:67:0x0200, B:69:0x0209, B:71:0x022c, B:73:0x0230, B:75:0x0236, B:77:0x023c, B:83:0x0248, B:206:0x059d, B:208:0x05a5, B:210:0x05b1, B:213:0x05bc, B:214:0x05bf, B:216:0x05cb, B:218:0x05d1, B:219:0x05e0, B:221:0x05e6, B:222:0x05f5, B:224:0x05fb, B:226:0x060b, B:228:0x0613, B:230:0x0618, B:232:0x061d, B:84:0x0257, B:86:0x025b, B:87:0x0265, B:91:0x0277, B:93:0x027b, B:95:0x0280, B:97:0x0286, B:102:0x0292, B:122:0x02c6, B:124:0x02cc, B:126:0x02e5, B:128:0x02ed, B:133:0x0301, B:134:0x0317, B:135:0x0318, B:136:0x031c, B:138:0x0320, B:139:0x0358, B:141:0x035c, B:143:0x0369, B:144:0x039f, B:146:0x03c4, B:148:0x03d6, B:150:0x03e6, B:152:0x03ee, B:154:0x0403, B:156:0x040a, B:158:0x0412, B:166:0x0428, B:168:0x0438, B:169:0x0449, B:174:0x045a, B:175:0x0461, B:176:0x0462, B:178:0x046f, B:180:0x04b1, B:182:0x04c0, B:184:0x04c4, B:186:0x04c8, B:187:0x050d, B:191:0x052e, B:188:0x0512, B:190:0x0516, B:192:0x0531, B:194:0x053b, B:196:0x056e, B:198:0x0572, B:199:0x057e, B:201:0x0586, B:203:0x058b, B:195:0x0553, B:106:0x02a1, B:110:0x02ab, B:233:0x0623, B:25:0x009f, B:27:0x00a5, B:28:0x00ac, B:30:0x00b2), top: B:296:0x007d }] */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0257 A[Catch: Exception -> 0x008f, TryCatch #1 {Exception -> 0x008f, blocks: (B:14:0x007d, B:16:0x0081, B:18:0x008b, B:22:0x0092, B:24:0x0098, B:34:0x00bd, B:37:0x00c7, B:39:0x00cf, B:41:0x00dd, B:44:0x00eb, B:46:0x00f2, B:48:0x0106, B:49:0x013b, B:51:0x013f, B:53:0x0163, B:54:0x018b, B:56:0x018f, B:57:0x0196, B:59:0x01c1, B:61:0x01d3, B:63:0x01e8, B:64:0x01f1, B:66:0x01fd, B:68:0x0207, B:65:0x01f4, B:67:0x0200, B:69:0x0209, B:71:0x022c, B:73:0x0230, B:75:0x0236, B:77:0x023c, B:83:0x0248, B:206:0x059d, B:208:0x05a5, B:210:0x05b1, B:213:0x05bc, B:214:0x05bf, B:216:0x05cb, B:218:0x05d1, B:219:0x05e0, B:221:0x05e6, B:222:0x05f5, B:224:0x05fb, B:226:0x060b, B:228:0x0613, B:230:0x0618, B:232:0x061d, B:84:0x0257, B:86:0x025b, B:87:0x0265, B:91:0x0277, B:93:0x027b, B:95:0x0280, B:97:0x0286, B:102:0x0292, B:122:0x02c6, B:124:0x02cc, B:126:0x02e5, B:128:0x02ed, B:133:0x0301, B:134:0x0317, B:135:0x0318, B:136:0x031c, B:138:0x0320, B:139:0x0358, B:141:0x035c, B:143:0x0369, B:144:0x039f, B:146:0x03c4, B:148:0x03d6, B:150:0x03e6, B:152:0x03ee, B:154:0x0403, B:156:0x040a, B:158:0x0412, B:166:0x0428, B:168:0x0438, B:169:0x0449, B:174:0x045a, B:175:0x0461, B:176:0x0462, B:178:0x046f, B:180:0x04b1, B:182:0x04c0, B:184:0x04c4, B:186:0x04c8, B:187:0x050d, B:191:0x052e, B:188:0x0512, B:190:0x0516, B:192:0x0531, B:194:0x053b, B:196:0x056e, B:198:0x0572, B:199:0x057e, B:201:0x0586, B:203:0x058b, B:195:0x0553, B:106:0x02a1, B:110:0x02ab, B:233:0x0623, B:25:0x009f, B:27:0x00a5, B:28:0x00ac, B:30:0x00b2), top: B:296:0x007d }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected boolean processRequestResult(RequestInfo requestInfo, TLRPC$TL_error tLRPC$TL_error) {
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
        this.requestInfos.remove(requestInfo);
        String str2 = " secret = ";
        if (tLRPC$TL_error != null) {
            if (!tLRPC$TL_error.text.contains("LIMIT_INVALID") || requestInfo.forceSmallChunk) {
                if (tLRPC$TL_error.text.contains("FILE_MIGRATE_")) {
                    Scanner scanner = new Scanner(tLRPC$TL_error.text.replace("FILE_MIGRATE_", ""));
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
                } else if (tLRPC$TL_error.text.contains("OFFSET_INVALID")) {
                    if (this.downloadedBytes % this.currentDownloadChunkSize == 0) {
                        try {
                            onFinishLoadingFile(true, 0, false);
                        } catch (Exception e) {
                            FileLog.e(e);
                            onFail(false, 0);
                        }
                        return false;
                    }
                } else if (tLRPC$TL_error.text.contains("RETRY_LIMIT")) {
                    onFail(false, 2);
                    return false;
                } else if (BuildVars.LOGS_ENABLED) {
                    TLRPC$InputFileLocation tLRPC$InputFileLocation = this.location;
                    if (tLRPC$InputFileLocation != null) {
                        if (tLRPC$InputFileLocation instanceof TLRPC$TL_inputPeerPhotoFileLocation) {
                            sb2 = new StringBuilder();
                            sb2.append(tLRPC$TL_error.text);
                            sb2.append(" ");
                            sb2.append(this.location);
                            sb2.append(" peer_did = ");
                            sb2.append(DialogObject.getPeerDialogId(((TLRPC$TL_inputPeerPhotoFileLocation) this.location).peer));
                            sb2.append(" peer_access_hash=");
                            sb2.append(((TLRPC$TL_inputPeerPhotoFileLocation) this.location).peer.access_hash);
                            sb2.append(" photo_id=");
                            sb2.append(((TLRPC$TL_inputPeerPhotoFileLocation) this.location).photo_id);
                            sb2.append(" big=");
                            sb2.append(((TLRPC$TL_inputPeerPhotoFileLocation) this.location).big);
                        } else {
                            sb2 = new StringBuilder();
                            sb2.append(tLRPC$TL_error.text);
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
                        sb2.append(tLRPC$TL_error.text);
                        sb2.append(" ");
                        sb2.append(this.webLocation);
                        sb2.append(" id = ");
                        sb2.append(this.fileName);
                    }
                    FileLog.e(sb2.toString());
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
                this.currentDownloadChunkSize = LiteMode.FLAG_CHAT_SCALE;
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
            } else if (AndroidUtilities.isEROFS(e2)) {
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
        } else if (requestInfo.responseCdn == null) {
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
                HashMap<Long, TLRPC$TL_fileHash> hashMap = this.cdnHashes;
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
                                int i4 = 0;
                                while (true) {
                                    if (i4 >= size) {
                                        break;
                                    }
                                    Range range = this.notCheckedCdnRanges.get(i4);
                                    if (range.start > j16 || j16 > range.end) {
                                        i4++;
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
                                            TLRPC$TL_fileHash tLRPC$TL_fileHash = this.cdnHashes.get(Long.valueOf(j2));
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
                                            if (!Arrays.equals(Utilities.computeSHA256(this.cdnCheckBytes, 0, downloadedLengthFromOffsetInternal), tLRPC$TL_fileHash.hash)) {
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
                    int i5 = this.currentDownloadChunkSize;
                    if (limit == i5) {
                        str = " volume_id = ";
                        if (j14 != j13) {
                            j = 0;
                            if (j13 % i5 != 0) {
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
        } else {
            nativeByteBuffer2 = requestInfo.responseCdn.bytes;
        }
        nativeByteBuffer = nativeByteBuffer2;
        if (nativeByteBuffer != null) {
        }
        onFinishLoadingFile(true, 0, false);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void removeStreamListener(final FileLoadOperationStream fileLoadOperationStream) {
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda13
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
                    } else if (this.state == 1) {
                        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda12
                            @Override // java.lang.Runnable
                            public final void run() {
                                FileLoadOperation.this.lambda$setIsPreloadVideoOperation$10(z);
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
        FileLog.e("FileLoadOperation " + getFileName() + " setStream(" + fileLoadOperationStream + ")");
        this.stream = fileLoadOperationStream;
        this.streamOffset = j;
        this.streamPriority = z;
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$setStream$0(fileLoadOperationStream);
            }
        });
    }

    public boolean start() {
        return start(this.stream, this.streamOffset, this.streamPriority);
    }

    /* JADX WARN: Code restructure failed: missing block: B:135:0x0418, code lost:
        if (r5 != r29.cacheFileFinal.length()) goto L73;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:120:0x03b8  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x03de  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x03fe  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x0474  */
    /* JADX WARN: Removed duplicated region for block: B:179:0x053e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:242:0x0689  */
    /* JADX WARN: Removed duplicated region for block: B:257:0x06b6  */
    /* JADX WARN: Removed duplicated region for block: B:275:0x0739  */
    /* JADX WARN: Removed duplicated region for block: B:278:0x073f  */
    /* JADX WARN: Removed duplicated region for block: B:281:0x0769  */
    /* JADX WARN: Removed duplicated region for block: B:296:0x07d0  */
    /* JADX WARN: Removed duplicated region for block: B:303:0x0800  */
    /* JADX WARN: Removed duplicated region for block: B:309:0x082c  */
    /* JADX WARN: Removed duplicated region for block: B:315:0x086f  */
    /* JADX WARN: Removed duplicated region for block: B:347:0x08fe A[Catch: Exception -> 0x0904, TRY_LEAVE, TryCatch #13 {Exception -> 0x0904, blocks: (B:345:0x08ed, B:347:0x08fe), top: B:407:0x08ed }] */
    /* JADX WARN: Removed duplicated region for block: B:360:0x092d  */
    /* JADX WARN: Removed duplicated region for block: B:362:0x0931  */
    /* JADX WARN: Removed duplicated region for block: B:364:0x093f  */
    /* JADX WARN: Removed duplicated region for block: B:399:0x0694 A[EXC_TOP_SPLITTER, SYNTHETIC] */
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
        int i4;
        this.startTime = System.currentTimeMillis();
        updateParams();
        if (this.currentDownloadChunkSize == 0) {
            if (this.forceSmallChunk) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("debug_loading: restart with small chunk");
                }
                this.currentDownloadChunkSize = LiteMode.FLAG_CHAT_SCALE;
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
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda16
                @Override // java.lang.Runnable
                public final void run() {
                    FileLoadOperation.this.lambda$start$7(z, j, fileLoadOperationStream, z10);
                }
            });
        } else if (z8) {
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda17
                @Override // java.lang.Runnable
                public final void run() {
                    FileLoadOperation.this.lambda$start$8();
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
                    if (this.parentObject instanceof TLRPC$TL_theme) {
                        this.cacheFileFinal = !this.encryptFile ? new File(this.storePath, this.storeFileName) : new File(this.storePath, str2);
                    } else {
                        this.cacheFileFinal = new File(ApplicationLoader.getFilesDirFixed(), "remote" + ((TLRPC$TL_theme) obj).id + ".attheme");
                    }
                    exists = this.cacheFileFinal.exists();
                    if (exists) {
                        if (!(this.parentObject instanceof TLRPC$TL_theme)) {
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
                                RandomAccessFile randomAccessFile2 = new RandomAccessFile(file, "rws");
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
                                try {
                                    try {
                                        randomAccessFile2.getChannel().close();
                                    } catch (Exception e) {
                                        FileLog.e(e);
                                    }
                                    randomAccessFile2.close();
                                } catch (Exception e2) {
                                    e = e2;
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
                            } catch (Exception e3) {
                                e = e3;
                                z3 = false;
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
                                                                try {
                                                                    this.moovFound = this.nextPreloadDownloadOffset > this.totalBytesCount / 2 ? 2 : 1;
                                                                    this.preloadNotRequestedBytesCount = readLong3;
                                                                } catch (Exception e4) {
                                                                    e = e4;
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
                                                            } catch (Exception e6) {
                                                                e = e6;
                                                                str7 = str4;
                                                                str8 = str11;
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
                                this.notLoadedBytesRanges.add(new Range(0L, this.totalBytesCount));
                                arrayList = this.notRequestedBytesRanges;
                                range = new Range(0L, this.totalBytesCount);
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
                                    long j11 = 0;
                                    if (this.downloadedBytes != 0 && !z4) {
                                        long length5 = this.cacheIvTemp.length();
                                        if (length5 > 0) {
                                            if (length5 % 64 == 0) {
                                                this.fiv.read(this.iv, 0, 64);
                                            } else {
                                                j11 = 0;
                                            }
                                        }
                                        this.downloadedBytes = j11;
                                        this.requestedBytesCount = j11;
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
                            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda18
                                @Override // java.lang.Runnable
                                public final void run() {
                                    FileLoadOperation.this.lambda$start$9(zArr22);
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
            TLRPC$InputFileLocation tLRPC$InputFileLocation = this.location;
            long j12 = tLRPC$InputFileLocation.volume_id;
            if (j12 != 0 && tLRPC$InputFileLocation.local_id != 0) {
                int i7 = this.datacenterId;
                if (i7 == Integer.MIN_VALUE || j12 == -2147483648L || i7 == 0) {
                    onFail(true, 0);
                    return false;
                } else if (!this.encryptFile) {
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
                    if (this.parentObject instanceof TLRPC$TL_theme) {
                    }
                    exists = this.cacheFileFinal.exists();
                    if (exists) {
                    }
                    if (exists) {
                    }
                    return true;
                } else {
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
                        String str102 = str5;
                        str3 = sb4;
                        str = str102;
                        this.requestInfos = new ArrayList<>(this.currentMaxDownloadRequests);
                        this.cancelledRequestInfos = new ArrayList<>();
                        this.delayedRequestInfos = new ArrayList<>(this.currentMaxDownloadRequests - 1);
                        this.state = 1;
                        if (this.parentObject instanceof TLRPC$TL_theme) {
                        }
                        exists = this.cacheFileFinal.exists();
                        if (exists) {
                        }
                        if (exists) {
                        }
                        return true;
                    }
                    str = str5;
                    str3 = null;
                }
            } else if (this.datacenterId == 0 || tLRPC$InputFileLocation.id == 0) {
                onFail(true, 0);
                return false;
            } else if (!this.encryptFile) {
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
                if (this.parentObject instanceof TLRPC$TL_theme) {
                }
                exists = this.cacheFileFinal.exists();
                if (exists) {
                }
                if (exists) {
                }
                return true;
            } else {
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
                    String str1022 = str5;
                    str3 = sb4;
                    str = str1022;
                    this.requestInfos = new ArrayList<>(this.currentMaxDownloadRequests);
                    this.cancelledRequestInfos = new ArrayList<>();
                    this.delayedRequestInfos = new ArrayList<>(this.currentMaxDownloadRequests - 1);
                    this.state = 1;
                    if (this.parentObject instanceof TLRPC$TL_theme) {
                    }
                    exists = this.cacheFileFinal.exists();
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
        if (this.parentObject instanceof TLRPC$TL_theme) {
        }
        exists = this.cacheFileFinal.exists();
        if (exists) {
        }
        if (exists) {
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:158:0x0254  */
    /* JADX WARN: Removed duplicated region for block: B:162:0x025e  */
    /* JADX WARN: Removed duplicated region for block: B:165:0x0263  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x0266  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x026b  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x027e  */
    /* JADX WARN: Removed duplicated region for block: B:203:0x034c  */
    /* JADX WARN: Removed duplicated region for block: B:207:0x036d  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x0375  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x038d  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x0399  */
    /* JADX WARN: Removed duplicated region for block: B:220:0x039d  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x03ca  */
    /* JADX WARN: Removed duplicated region for block: B:230:0x02f1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void startDownloadRequest(int i) {
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
        TLRPC$TL_upload_getFile tLRPC$TL_upload_getFile;
        final RequestInfo requestInfo;
        long j6;
        TLRPC$InputFileLocation tLRPC$InputFileLocation;
        long j7;
        HashMap<Long, PreloadRange> hashMap;
        PreloadRange preloadRange;
        ArrayList<Range> arrayList;
        int i6;
        int i7;
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
            int i8 = 0;
            while (i8 < max) {
                int i9 = 2;
                if (!this.isPreloadVideoOperation) {
                    i2 = max;
                    ArrayList<Range> arrayList2 = this.notRequestedBytesRanges;
                    if (arrayList2 != null) {
                        long j9 = this.streamPriorityStartOffset;
                        if (j9 == 0) {
                            j9 = this.streamStartOffset;
                        }
                        int size = arrayList2.size();
                        int i10 = 0;
                        long j10 = Long.MAX_VALUE;
                        long j11 = Long.MAX_VALUE;
                        while (true) {
                            if (i10 >= size) {
                                j = j10;
                                j2 = j11;
                                break;
                            }
                            Range range = this.notRequestedBytesRanges.get(i10);
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
                            i10++;
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
                                    if (j5 > 0 || i8 == i2 - 1 || (j5 > 0 && this.currentDownloadChunkSize + j3 >= j5)) {
                                        i4 = i;
                                        z = true;
                                    } else {
                                        i4 = i;
                                        z = false;
                                    }
                                    if (i4 == -1) {
                                        i9 = i4;
                                    } else if (this.requestsCount % 2 != 0) {
                                        i9 = ConnectionsManager.ConnectionTypeDownload2;
                                    }
                                    i5 = !this.isForceRequest ? 32 : 0;
                                    if (!this.isCdn) {
                                        TLRPC$TL_upload_getCdnFile tLRPC$TL_upload_getCdnFile = new TLRPC$TL_upload_getCdnFile();
                                        tLRPC$TL_upload_getCdnFile.file_token = this.cdnToken;
                                        tLRPC$TL_upload_getCdnFile.offset = j3;
                                        tLRPC$TL_upload_getCdnFile.limit = this.currentDownloadChunkSize;
                                        i5 |= 1;
                                        tLRPC$TL_upload_getFile = tLRPC$TL_upload_getCdnFile;
                                    } else if (this.webLocation != null) {
                                        TLRPC$TL_upload_getWebFile tLRPC$TL_upload_getWebFile = new TLRPC$TL_upload_getWebFile();
                                        tLRPC$TL_upload_getWebFile.location = this.webLocation;
                                        tLRPC$TL_upload_getWebFile.offset = (int) j3;
                                        tLRPC$TL_upload_getWebFile.limit = this.currentDownloadChunkSize;
                                        tLRPC$TL_upload_getFile = tLRPC$TL_upload_getWebFile;
                                    } else {
                                        TLRPC$TL_upload_getFile tLRPC$TL_upload_getFile2 = new TLRPC$TL_upload_getFile();
                                        tLRPC$TL_upload_getFile2.location = this.location;
                                        tLRPC$TL_upload_getFile2.offset = j3;
                                        tLRPC$TL_upload_getFile2.limit = this.currentDownloadChunkSize;
                                        tLRPC$TL_upload_getFile2.cdn_supported = true;
                                        tLRPC$TL_upload_getFile = tLRPC$TL_upload_getFile2;
                                    }
                                    final TLRPC$TL_upload_getFile tLRPC$TL_upload_getFile3 = tLRPC$TL_upload_getFile;
                                    this.requestedBytesCount += this.currentDownloadChunkSize;
                                    requestInfo = new RequestInfo();
                                    this.requestInfos.add(requestInfo);
                                    requestInfo.offset = j3;
                                    requestInfo.chunkSize = this.currentDownloadChunkSize;
                                    requestInfo.forceSmallChunk = this.forceSmallChunk;
                                    requestInfo.connectionType = i9;
                                    if (!this.isPreloadVideoOperation && this.supportsPreloading && this.preloadStream != null && (hashMap = this.preloadedBytesRanges) != null && (preloadRange = hashMap.get(Long.valueOf(requestInfo.offset))) != null) {
                                        requestInfo.response = new TLRPC$TL_upload_file();
                                        try {
                                            if (BuildVars.DEBUG_VERSION) {
                                                try {
                                                    if (preloadRange.length > 2147483647L) {
                                                        throw new RuntimeException("cast long to integer");
                                                        break;
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
                                                Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda2
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        FileLoadOperation.this.lambda$startDownloadRequest$23(requestInfo);
                                                    }
                                                });
                                                j7 = 0;
                                            } catch (Exception unused2) {
                                                if (this.streamPriorityStartOffset != 0) {
                                                }
                                                tLRPC$InputFileLocation = this.location;
                                                if (tLRPC$InputFileLocation instanceof TLRPC$TL_inputPeerPhotoFileLocation) {
                                                }
                                                requestInfo.forceSmallChunk = this.forceSmallChunk;
                                                if (BuildVars.LOGS_ENABLED) {
                                                }
                                                int i11 = i5 | 2048;
                                                final int i12 = this.isCdn ? this.cdnDatacenterId : this.datacenterId;
                                                j7 = j6;
                                                final int i13 = i9;
                                                requestInfo.requestToken = ConnectionsManager.getInstance(this.currentAccount).sendRequestSync(tLRPC$TL_upload_getFile3, new RequestDelegate() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda3
                                                    @Override // org.telegram.tgnet.RequestDelegate
                                                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                                        FileLoadOperation.this.lambda$startDownloadRequest$25(requestInfo, i12, i13, tLRPC$TL_upload_getFile3, tLObject, tLRPC$TL_error);
                                                    }
                                                }, null, null, i11, i12, i9, z);
                                                if (BuildVars.LOGS_ENABLED) {
                                                }
                                                this.requestsCount++;
                                                i8++;
                                                j8 = j7;
                                                max = i2;
                                                z2 = false;
                                            }
                                        } catch (Exception unused3) {
                                        }
                                        i8++;
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
                                    tLRPC$InputFileLocation = this.location;
                                    if ((tLRPC$InputFileLocation instanceof TLRPC$TL_inputPeerPhotoFileLocation) || ((TLRPC$TL_inputPeerPhotoFileLocation) tLRPC$InputFileLocation).photo_id != j6) {
                                        requestInfo.forceSmallChunk = this.forceSmallChunk;
                                        if (BuildVars.LOGS_ENABLED) {
                                            requestInfo.requestStartTime = System.currentTimeMillis();
                                        }
                                        int i112 = i5 | 2048;
                                        final int i122 = this.isCdn ? this.cdnDatacenterId : this.datacenterId;
                                        j7 = j6;
                                        final int i132 = i9;
                                        requestInfo.requestToken = ConnectionsManager.getInstance(this.currentAccount).sendRequestSync(tLRPC$TL_upload_getFile3, new RequestDelegate() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda3
                                            @Override // org.telegram.tgnet.RequestDelegate
                                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                                FileLoadOperation.this.lambda$startDownloadRequest$25(requestInfo, i122, i132, tLRPC$TL_upload_getFile3, tLObject, tLRPC$TL_error);
                                            }
                                        }, null, null, i112, i122, i9, z);
                                        if (BuildVars.LOGS_ENABLED) {
                                            FileLog.d("debug_loading: " + this.cacheFileFinal.getName() + " dc=" + i122 + " send reqId " + requestInfo.requestToken + " offset=" + requestInfo.offset + " conType=" + i9 + " priority=" + this.priority);
                                        }
                                        this.requestsCount++;
                                    } else {
                                        requestReference(requestInfo);
                                        j7 = j6;
                                    }
                                    i8++;
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
                } else if (this.moovFound != 0 && this.preloadNotRequestedBytesCount <= j8) {
                    boolean z5 = BuildVars.DEBUG_VERSION;
                    return;
                } else {
                    j = this.nextPreloadDownloadOffset;
                    if (j == -1) {
                        int i14 = (preloadMaxBytes / this.currentDownloadChunkSize) + 2;
                        long j12 = j8;
                        while (true) {
                            if (i14 == 0) {
                                i2 = max;
                                j = j12;
                                z2 = false;
                                break;
                            } else if (!this.requestedPreloadedBytesRanges.containsKey(Long.valueOf(j12))) {
                                j = j12;
                                i2 = max;
                                z2 = true;
                                break;
                            } else {
                                long j13 = this.currentDownloadChunkSize;
                                j12 += j13;
                                long j14 = this.totalBytesCount;
                                if (j12 > j14) {
                                    j = j12;
                                    i2 = max;
                                    break;
                                }
                                if (this.moovFound == 2) {
                                    i7 = max;
                                    if (j12 == i6 * 8) {
                                        j12 = ((j14 - 1048576) / j13) * j13;
                                    }
                                } else {
                                    i7 = max;
                                }
                                i14--;
                                max = i7;
                                z2 = false;
                            }
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
                final TLObject tLRPC$TL_upload_getFile32 = tLRPC$TL_upload_getFile;
                this.requestedBytesCount += this.currentDownloadChunkSize;
                requestInfo = new RequestInfo();
                this.requestInfos.add(requestInfo);
                requestInfo.offset = j3;
                requestInfo.chunkSize = this.currentDownloadChunkSize;
                requestInfo.forceSmallChunk = this.forceSmallChunk;
                requestInfo.connectionType = i9;
                if (!this.isPreloadVideoOperation) {
                    requestInfo.response = new TLRPC$TL_upload_file();
                    if (BuildVars.DEBUG_VERSION) {
                    }
                    NativeByteBuffer nativeByteBuffer2 = new NativeByteBuffer((int) preloadRange.length);
                    this.preloadStream.seek(preloadRange.fileOffset);
                    this.preloadStream.getChannel().read(nativeByteBuffer2.buffer);
                    nativeByteBuffer2.buffer.position(0);
                    requestInfo.response.bytes = nativeByteBuffer2;
                    Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            FileLoadOperation.this.lambda$startDownloadRequest$23(requestInfo);
                        }
                    });
                    j7 = 0;
                    i8++;
                    j8 = j7;
                    max = i2;
                    z2 = false;
                }
                if (this.streamPriorityStartOffset != 0) {
                }
                tLRPC$InputFileLocation = this.location;
                if (tLRPC$InputFileLocation instanceof TLRPC$TL_inputPeerPhotoFileLocation) {
                }
                requestInfo.forceSmallChunk = this.forceSmallChunk;
                if (BuildVars.LOGS_ENABLED) {
                }
                int i1122 = i5 | 2048;
                final int i1222 = this.isCdn ? this.cdnDatacenterId : this.datacenterId;
                j7 = j6;
                final int i1322 = i9;
                requestInfo.requestToken = ConnectionsManager.getInstance(this.currentAccount).sendRequestSync(tLRPC$TL_upload_getFile32, new RequestDelegate() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda3
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        FileLoadOperation.this.lambda$startDownloadRequest$25(requestInfo, i1222, i1322, tLRPC$TL_upload_getFile32, tLObject, tLRPC$TL_error);
                    }
                }, null, null, i1122, i1222, i9, z);
                if (BuildVars.LOGS_ENABLED) {
                }
                this.requestsCount++;
                i8++;
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
