package org.telegram.messenger;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;
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
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.Storage.CacheModel;
/* loaded from: classes.dex */
public class FileLoadOperation {
    private static final int FINISH_CODE_DEFAULT = 0;
    private static final int FINISH_CODE_FILE_ALREADY_EXIST = 1;
    public static ImmutableByteArrayOutputStream filesQueueByteBuffer = null;
    private static final int preloadMaxBytes = 2097152;
    private static final int stateCanceled = 4;
    private static final int stateDownloading = 1;
    private static final int stateFailed = 2;
    private static final int stateFinished = 3;
    private static final int stateIdle = 0;
    private boolean allowDisordererFileSave;
    private int bigFileSizeFrom;
    private long bytesCountPadding;
    private File cacheFileFinal;
    private File cacheFileGzipTemp;
    private File cacheFileParts;
    private File cacheFilePreload;
    private File cacheFileTemp;
    private File cacheIvTemp;
    private byte[] cdnCheckBytes;
    private int cdnChunkCheckSize;
    private int cdnDatacenterId;
    private HashMap<Long, TLRPC$TL_fileHash> cdnHashes;
    private byte[] cdnIv;
    private byte[] cdnKey;
    private byte[] cdnToken;
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
    private RandomAccessFile preloadStream;
    private int preloadStreamFileOffset;
    private byte[] preloadTempBuffer;
    private int preloadTempBufferCount;
    private HashMap<Long, PreloadRange> preloadedBytesRanges;
    private int priority;
    private FileLoaderPriorityQueue priorityQueue;
    private RequestInfo priorityRequestInfo;
    private int renameRetryCount;
    private ArrayList<RequestInfo> requestInfos;
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
    public static volatile DispatchQueue filesQueue = new DispatchQueue("writeFileQueue");
    private static final Object lockObject = new Object();

    /* loaded from: classes.dex */
    public interface FileLoadOperationDelegate {
        void didChangedLoadProgress(FileLoadOperation fileLoadOperation, long j, long j2);

        void didFailedLoadingFile(FileLoadOperation fileLoadOperation, int i);

        void didFinishLoadingFile(FileLoadOperation fileLoadOperation, File file);

        void didPreFinishLoading(FileLoadOperation fileLoadOperation, File file);

        boolean hasAnotherRefOnFile(String str);

        void saveFilePath(FilePathDatabase.PathData pathData, File file);
    }

    public void setStream(FileLoadOperationStream fileLoadOperationStream, boolean z, long j) {
        this.stream = fileLoadOperationStream;
        this.streamOffset = j;
        this.streamPriority = z;
    }

    public int getPositionInQueue() {
        return getQueue().getPosition(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static class RequestInfo {
        private boolean forceSmallChunk;
        private long offset;
        public long requestStartTime;
        private int requestToken;
        private TLRPC$TL_upload_file response;
        private TLRPC$TL_upload_cdnFile responseCdn;
        private TLRPC$TL_upload_webFile responseWeb;

        protected RequestInfo() {
        }
    }

    /* loaded from: classes.dex */
    public static class Range {
        private long end;
        private long start;

        private Range(long j, long j2) {
            this.start = j;
            this.end = j2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class PreloadRange {
        private long fileOffset;
        private long length;

        private PreloadRange(long j, long j2) {
            this.fileOffset = j;
            this.length = j2;
        }
    }

    private void updateParams() {
        if (MessagesController.getInstance(this.currentAccount).getfileExperimentalParams && !this.forceSmallChunk) {
            this.downloadChunkSizeBig = 524288;
            this.maxDownloadRequests = 8;
            this.maxDownloadRequestsBig = 8;
        } else {
            this.downloadChunkSizeBig = 131072;
            this.maxDownloadRequests = 4;
            this.maxDownloadRequestsBig = 4;
        }
        this.maxCdnParts = (int) (FileLoader.DEFAULT_MAX_FILE_SIZE / this.downloadChunkSizeBig);
    }

    public FileLoadOperation(ImageLocation imageLocation, Object obj, String str, long j) {
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
        boolean z = false;
        this.state = 0;
        updateParams();
        this.parentObject = obj;
        this.fileMetadata = FileLoader.getFileMetadataFromParent(this.currentAccount, obj);
        this.isStream = imageLocation.imageType == 2;
        if (imageLocation.isEncrypted()) {
            TLRPC$InputFileLocation tLRPC$InputFileLocation = new TLRPC$InputFileLocation() { // from class: org.telegram.tgnet.TLRPC$TL_inputEncryptedFileLocation
                public static int constructor = -182231723;

                @Override // org.telegram.tgnet.TLObject
                public void readParams(AbstractSerializedData abstractSerializedData, boolean z2) {
                    this.id = abstractSerializedData.readInt64(z2);
                    this.access_hash = abstractSerializedData.readInt64(z2);
                }

                @Override // org.telegram.tgnet.TLObject
                public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                    abstractSerializedData.writeInt32(constructor);
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
            System.arraycopy(imageLocation.iv, 0, bArr, 0, bArr.length);
            this.key = imageLocation.key;
        } else if (imageLocation.photoPeer != null) {
            TLRPC$TL_inputPeerPhotoFileLocation tLRPC$TL_inputPeerPhotoFileLocation = new TLRPC$TL_inputPeerPhotoFileLocation();
            TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated2 = imageLocation.location;
            long j3 = tLRPC$TL_fileLocationToBeDeprecated2.volume_id;
            tLRPC$TL_inputPeerPhotoFileLocation.id = j3;
            tLRPC$TL_inputPeerPhotoFileLocation.volume_id = j3;
            tLRPC$TL_inputPeerPhotoFileLocation.local_id = tLRPC$TL_fileLocationToBeDeprecated2.local_id;
            tLRPC$TL_inputPeerPhotoFileLocation.photo_id = imageLocation.photoId;
            tLRPC$TL_inputPeerPhotoFileLocation.big = imageLocation.photoPeerType == 0;
            tLRPC$TL_inputPeerPhotoFileLocation.peer = imageLocation.photoPeer;
            this.location = tLRPC$TL_inputPeerPhotoFileLocation;
        } else if (imageLocation.stickerSet != null) {
            TLRPC$TL_inputStickerSetThumb tLRPC$TL_inputStickerSetThumb = new TLRPC$TL_inputStickerSetThumb();
            TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated3 = imageLocation.location;
            long j4 = tLRPC$TL_fileLocationToBeDeprecated3.volume_id;
            tLRPC$TL_inputStickerSetThumb.id = j4;
            tLRPC$TL_inputStickerSetThumb.volume_id = j4;
            tLRPC$TL_inputStickerSetThumb.local_id = tLRPC$TL_fileLocationToBeDeprecated3.local_id;
            tLRPC$TL_inputStickerSetThumb.thumb_version = imageLocation.thumbVersion;
            tLRPC$TL_inputStickerSetThumb.stickerset = imageLocation.stickerSet;
            this.location = tLRPC$TL_inputStickerSetThumb;
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
                public static int constructor = -539317279;

                @Override // org.telegram.tgnet.TLObject
                public void readParams(AbstractSerializedData abstractSerializedData, boolean z2) {
                    this.volume_id = abstractSerializedData.readInt64(z2);
                    this.local_id = abstractSerializedData.readInt32(z2);
                    this.secret = abstractSerializedData.readInt64(z2);
                    this.file_reference = abstractSerializedData.readByteArray(z2);
                }

                @Override // org.telegram.tgnet.TLObject
                public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                    abstractSerializedData.writeInt32(constructor);
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
            public static int constructor = -876089816;

            @Override // org.telegram.tgnet.TLObject
            public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
                this.id = abstractSerializedData.readInt64(z);
                this.access_hash = abstractSerializedData.readInt64(z);
            }

            @Override // org.telegram.tgnet.TLObject
            public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                abstractSerializedData.writeInt32(constructor);
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

    public FileLoadOperation(int i, WebFile webFile) {
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
        if (webFile.mime_type.startsWith("image/")) {
            this.currentType = ConnectionsManager.FileTypePhoto;
        } else if (webFile.mime_type.equals("audio/ogg")) {
            this.currentType = ConnectionsManager.FileTypeAudio;
        } else if (webFile.mime_type.startsWith("video/")) {
            this.currentType = ConnectionsManager.FileTypeVideo;
        } else {
            this.currentType = ConnectionsManager.FileTypeFile;
        }
        this.allowDisordererFileSave = true;
        this.ext = ImageLoader.getHttpUrlExtension(webFile.url, mimeTypePart);
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x0101 A[Catch: Exception -> 0x0128, TryCatch #0 {Exception -> 0x0128, blocks: (B:3:0x0030, B:6:0x0040, B:18:0x00a6, B:20:0x00b0, B:25:0x00be, B:27:0x00c8, B:29:0x00d2, B:30:0x00da, B:32:0x00e2, B:35:0x00ec, B:37:0x00f7, B:39:0x0101, B:44:0x0117, B:46:0x011f, B:40:0x0106, B:42:0x010e, B:43:0x0113, B:36:0x00f5, B:7:0x0066, B:9:0x006a, B:11:0x0081, B:12:0x0085, B:14:0x0096, B:16:0x00a0, B:17:0x00a3), top: B:51:0x0030 }] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0106 A[Catch: Exception -> 0x0128, TryCatch #0 {Exception -> 0x0128, blocks: (B:3:0x0030, B:6:0x0040, B:18:0x00a6, B:20:0x00b0, B:25:0x00be, B:27:0x00c8, B:29:0x00d2, B:30:0x00da, B:32:0x00e2, B:35:0x00ec, B:37:0x00f7, B:39:0x0101, B:44:0x0117, B:46:0x011f, B:40:0x0106, B:42:0x010e, B:43:0x0113, B:36:0x00f5, B:7:0x0066, B:9:0x006a, B:11:0x0081, B:12:0x0085, B:14:0x0096, B:16:0x00a0, B:17:0x00a3), top: B:51:0x0030 }] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x011f A[Catch: Exception -> 0x0128, TRY_LEAVE, TryCatch #0 {Exception -> 0x0128, blocks: (B:3:0x0030, B:6:0x0040, B:18:0x00a6, B:20:0x00b0, B:25:0x00be, B:27:0x00c8, B:29:0x00d2, B:30:0x00da, B:32:0x00e2, B:35:0x00ec, B:37:0x00f7, B:39:0x0101, B:44:0x0117, B:46:0x011f, B:40:0x0106, B:42:0x010e, B:43:0x0113, B:36:0x00f5, B:7:0x0066, B:9:0x006a, B:11:0x0081, B:12:0x0085, B:14:0x0096, B:16:0x00a0, B:17:0x00a3), top: B:51:0x0030 }] */
    /* JADX WARN: Removed duplicated region for block: B:55:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public FileLoadOperation(TLRPC$Document tLRPC$Document, Object obj) {
        boolean z;
        long j;
        String documentFileName;
        int lastIndexOf;
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
            this.fileMetadata = FileLoader.getFileMetadataFromParent(this.currentAccount, obj);
            if (tLRPC$Document instanceof TLRPC$TL_documentEncrypted) {
                TLRPC$InputFileLocation tLRPC$InputFileLocation = new TLRPC$InputFileLocation() { // from class: org.telegram.tgnet.TLRPC$TL_inputEncryptedFileLocation
                    public static int constructor = -182231723;

                    @Override // org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData, boolean z2) {
                        this.id = abstractSerializedData.readInt64(z2);
                        this.access_hash = abstractSerializedData.readInt64(z2);
                    }

                    @Override // org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                        abstractSerializedData.writeInt32(constructor);
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
                System.arraycopy(tLRPC$Document.iv, 0, bArr, 0, bArr.length);
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
                    this.ext = this.ext.substring(lastIndexOf);
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
                this.ext = "";
                if (!"audio/ogg".equals(tLRPC$Document.mime_type)) {
                }
                if (this.ext.length() > 1) {
                }
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
                this.ext = this.ext.substring(lastIndexOf);
                if (!"audio/ogg".equals(tLRPC$Document.mime_type)) {
                }
                if (this.ext.length() > 1) {
                }
            }
            this.ext = "";
            if (!"audio/ogg".equals(tLRPC$Document.mime_type)) {
            }
            if (this.ext.length() > 1) {
            }
        } catch (Exception e) {
            FileLog.e(e);
            onFail(true, 0);
        }
    }

    public void setEncryptFile(boolean z) {
        this.encryptFile = z;
        if (z) {
            this.allowDisordererFileSave = false;
        }
    }

    public int getDatacenterId() {
        return this.initialDatacenterId;
    }

    public void setForceRequest(boolean z) {
        this.isForceRequest = z;
    }

    public boolean isForceRequest() {
        return this.isForceRequest;
    }

    public void setPriority(int i) {
        this.priority = i;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPaths(int i, String str, FileLoaderPriorityQueue fileLoaderPriorityQueue, File file, File file2, String str2) {
        this.storePath = file;
        this.tempPath = file2;
        this.currentAccount = i;
        this.fileName = str;
        this.storeFileName = str2;
        this.priorityQueue = fileLoaderPriorityQueue;
    }

    public FileLoaderPriorityQueue getQueue() {
        return this.priorityQueue;
    }

    public boolean wasStarted() {
        return this.started && !this.paused;
    }

    public int getCurrentType() {
        return this.currentType;
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
        Collections.sort(arrayList, FileLoadOperation$$ExternalSyntheticLambda15.INSTANCE);
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

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$removePart$0(Range range, Range range2) {
        if (range.start > range2.start) {
            return 1;
        }
        return range.start < range2.start ? -1 : 0;
    }

    private void addPart(ArrayList<Range> arrayList, long j, long j2, boolean z) {
        if (arrayList == null || j2 < j) {
            return;
        }
        int size = arrayList.size();
        boolean z2 = false;
        for (int i = 0; i < size; i++) {
            Range range = arrayList.get(i);
            if (j <= range.start) {
                if (j2 < range.end) {
                    if (j2 > range.start) {
                        range.start = j2;
                    }
                } else {
                    arrayList.remove(i);
                }
                z2 = true;
                break;
            }
            if (j2 >= range.end) {
                if (j < range.end) {
                    range.end = j;
                }
            } else {
                arrayList.add(0, new Range(range.start, j));
                range.start = j2;
            }
            z2 = true;
            break;
        }
        if (z) {
            if (z2) {
                final ArrayList arrayList2 = new ArrayList(arrayList);
                if (this.fileWriteRunnable != null) {
                    filesQueue.cancelRunnable(this.fileWriteRunnable);
                }
                DispatchQueue dispatchQueue = filesQueue;
                Runnable runnable = new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        FileLoadOperation.this.lambda$addPart$1(arrayList2);
                    }
                };
                this.fileWriteRunnable = runnable;
                dispatchQueue.postRunnable(runnable);
                notifyStreamListeners();
            } else if (BuildVars.LOGS_ENABLED) {
                FileLog.e(this.cacheFileFinal + " downloaded duplicate file part " + j + " - " + j2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addPart$1(ArrayList arrayList) {
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
            this.totalTime += System.currentTimeMillis() - currentTimeMillis;
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

    /* JADX INFO: Access modifiers changed from: protected */
    public File getCacheFileFinal() {
        return this.cacheFileFinal;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public File getCurrentFile() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final File[] fileArr = new File[1];
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$getCurrentFile$2(fileArr, countDownLatch);
            }
        });
        try {
            countDownLatch.await();
        } catch (Exception e) {
            FileLog.e(e);
        }
        return fileArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getCurrentFile$2(File[] fileArr, CountDownLatch countDownLatch) {
        if (this.state == 3) {
            fileArr[0] = this.cacheFileFinal;
        } else {
            fileArr[0] = this.cacheFileTemp;
        }
        countDownLatch.countDown();
    }

    private long getDownloadedLengthFromOffsetInternal(ArrayList<Range> arrayList, long j, long j2) {
        long j3;
        if (arrayList == null || this.state == 3 || arrayList.isEmpty()) {
            if (this.state == 3) {
                return j2;
            }
            long j4 = this.downloadedBytes;
            if (j4 == 0) {
                return 0L;
            }
            return Math.min(j2, Math.max(j4 - j, 0L));
        }
        int size = arrayList.size();
        Range range = null;
        int i = 0;
        while (true) {
            if (i >= size) {
                j3 = j2;
                break;
            }
            Range range2 = arrayList.get(i);
            if (j <= range2.start && (range == null || range2.start < range.start)) {
                range = range2;
            }
            if (range2.start <= j && range2.end > j) {
                j3 = 0;
                break;
            }
            i++;
        }
        if (j3 == 0) {
            return 0L;
        }
        if (range != null) {
            return Math.min(j2, range.start - j);
        }
        return Math.min(j2, Math.max(this.totalBytesCount - j, 0L));
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
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$getDownloadedLengthFromOffset$3(jArr, j, j2, countDownLatch);
            }
        });
        try {
            countDownLatch.await();
        } catch (Exception unused) {
        }
        return jArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getDownloadedLengthFromOffset$3(long[] jArr, long j, long j2, CountDownLatch countDownLatch) {
        jArr[0] = getDownloadedLengthFromOffsetInternal(this.notLoadedBytesRanges, j, j2);
        if (this.state == 3) {
            jArr[1] = 1;
        }
        countDownLatch.countDown();
    }

    public String getFileName() {
        return this.fileName;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void removeStreamListener(final FileLoadOperationStream fileLoadOperationStream) {
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$removeStreamListener$4(fileLoadOperationStream);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeStreamListener$4(FileLoadOperationStream fileLoadOperationStream) {
        ArrayList<FileLoadOperationStream> arrayList = this.streamListeners;
        if (arrayList == null) {
            return;
        }
        arrayList.remove(fileLoadOperationStream);
    }

    private void copyNotLoadedRanges() {
        if (this.notLoadedBytesRanges == null) {
            return;
        }
        this.notLoadedBytesRangesCopy = new ArrayList<>(this.notLoadedBytesRanges);
    }

    public void pause() {
        if (this.state != 1) {
            return;
        }
        this.paused = true;
    }

    public boolean start() {
        return start(this.stream, this.streamOffset, this.streamPriority);
    }

    /* JADX WARN: Code restructure failed: missing block: B:116:0x03ed, code lost:
        if (r6 != r28.cacheFileFinal.length()) goto L57;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:104:0x038f  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x03b6  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x03d7  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0405  */
    /* JADX WARN: Removed duplicated region for block: B:221:0x061f  */
    /* JADX WARN: Removed duplicated region for block: B:236:0x064c  */
    /* JADX WARN: Removed duplicated region for block: B:249:0x06c1  */
    /* JADX WARN: Removed duplicated region for block: B:252:0x06eb  */
    /* JADX WARN: Removed duplicated region for block: B:265:0x0744  */
    /* JADX WARN: Removed duplicated region for block: B:272:0x076f  */
    /* JADX WARN: Removed duplicated region for block: B:278:0x079b  */
    /* JADX WARN: Removed duplicated region for block: B:283:0x07e4  */
    /* JADX WARN: Removed duplicated region for block: B:314:0x0859  */
    /* JADX WARN: Removed duplicated region for block: B:322:0x087e A[Catch: Exception -> 0x0884, TRY_LEAVE, TryCatch #13 {Exception -> 0x0884, blocks: (B:320:0x086d, B:322:0x087e), top: B:385:0x086d }] */
    /* JADX WARN: Removed duplicated region for block: B:335:0x08b0  */
    /* JADX WARN: Removed duplicated region for block: B:337:0x08b4  */
    /* JADX WARN: Removed duplicated region for block: B:338:0x08c1  */
    /* JADX WARN: Removed duplicated region for block: B:377:0x062a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r1v41 */
    /* JADX WARN: Type inference failed for: r1v42, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r1v46 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean start(final FileLoadOperationStream fileLoadOperationStream, final long j, final boolean z) {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        Object obj;
        boolean exists;
        boolean z2;
        int i;
        int i2;
        boolean z3;
        String str8;
        String str9;
        boolean z4;
        String str10;
        ArrayList<Range> arrayList;
        ?? r1;
        boolean z5;
        long j2;
        long j3;
        RandomAccessFile randomAccessFile;
        RandomAccessFile randomAccessFile2;
        this.startTime = System.currentTimeMillis();
        updateParams();
        if (this.currentDownloadChunkSize == 0) {
            if (this.isStream) {
                this.currentDownloadChunkSize = this.downloadChunkSizeAnimation;
                this.currentMaxDownloadRequests = this.maxDownloadRequestsAnimation;
            } else {
                long j4 = this.totalBytesCount;
                int i3 = this.bigFileSizeFrom;
                this.currentDownloadChunkSize = j4 >= ((long) i3) ? this.downloadChunkSizeBig : this.downloadChunkSize;
                this.currentMaxDownloadRequests = j4 >= ((long) i3) ? this.maxDownloadRequestsBig : this.maxDownloadRequests;
            }
        }
        boolean z6 = this.state != 0;
        boolean z7 = this.paused;
        this.paused = false;
        if (fileLoadOperationStream != null) {
            final boolean z8 = z6;
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    FileLoadOperation.this.lambda$start$5(z, j, fileLoadOperationStream, z8);
                }
            });
        } else if (z7 && z6) {
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    FileLoadOperation.this.startDownloadRequest();
                }
            });
        }
        if (z6) {
            return z7;
        }
        if (this.location == null && this.webLocation == null) {
            onFail(true, 0);
            return false;
        }
        int i4 = this.currentDownloadChunkSize;
        this.streamStartOffset = (j / i4) * i4;
        if (this.allowDisordererFileSave) {
            long j5 = this.totalBytesCount;
            if (j5 > 0 && j5 > i4) {
                this.notLoadedBytesRanges = new ArrayList<>();
                this.notRequestedBytesRanges = new ArrayList<>();
            }
        }
        if (this.webLocation != null) {
            String MD5 = Utilities.MD5(this.webFile.url);
            if (this.encryptFile) {
                str6 = MD5 + ".temp.enc";
                str2 = MD5 + "." + this.ext + ".enc";
                if (this.key != null) {
                    str7 = MD5 + "_64.iv.enc";
                }
                str7 = null;
            } else {
                String str11 = MD5 + ".temp";
                str2 = MD5 + "." + this.ext;
                if (this.key != null) {
                    str7 = MD5 + "_64.iv";
                    str6 = str11;
                } else {
                    str6 = str11;
                    str7 = null;
                }
            }
            this.requestInfos = new ArrayList<>(this.currentMaxDownloadRequests);
            this.delayedRequestInfos = new ArrayList<>(this.currentMaxDownloadRequests - 1);
            this.state = 1;
            if (!(this.parentObject instanceof TLRPC$TL_theme)) {
                this.cacheFileFinal = new File(ApplicationLoader.getFilesDirFixed(), "remote" + ((TLRPC$TL_theme) obj).id + ".attheme");
            } else if (!this.encryptFile) {
                this.cacheFileFinal = new File(this.storePath, this.storeFileName);
            } else {
                this.cacheFileFinal = new File(this.storePath, str2);
            }
            exists = this.cacheFileFinal.exists();
            if (exists) {
                if (!(this.parentObject instanceof TLRPC$TL_theme)) {
                    long j6 = this.totalBytesCount;
                    if (j6 != 0) {
                    }
                }
                if (!this.delegate.hasAnotherRefOnFile(this.cacheFileFinal.toString())) {
                    this.cacheFileFinal.delete();
                }
                exists = false;
            }
            if (exists) {
                this.cacheFileTemp = new File(this.tempPath, str6);
                if (this.ungzip) {
                    this.cacheFileGzipTemp = new File(this.tempPath, str6 + ".gz");
                }
                String str12 = "rws";
                if (this.encryptFile) {
                    File file = new File(FileLoader.getInternalCacheDir(), str2 + ".key");
                    try {
                        randomAccessFile2 = new RandomAccessFile(file, "rws");
                        long length = file.length();
                        byte[] bArr = new byte[32];
                        this.encryptKey = bArr;
                        this.encryptIv = new byte[16];
                        if (length > 0 && length % 48 == 0) {
                            randomAccessFile2.read(bArr, 0, 32);
                            randomAccessFile2.read(this.encryptIv, 0, 16);
                            z3 = false;
                        } else {
                            Utilities.random.nextBytes(bArr);
                            Utilities.random.nextBytes(this.encryptIv);
                            randomAccessFile2.write(this.encryptKey);
                            randomAccessFile2.write(this.encryptIv);
                            z3 = true;
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
                            FileLog.e((Throwable) e, false);
                        } else if (AndroidUtilities.isEROFS(e)) {
                            SharedConfig.checkSdCard(this.cacheFileFinal);
                            FileLog.e((Throwable) e, false);
                        } else {
                            FileLog.e(e);
                        }
                        i2 = 1;
                        final boolean[] zArr = new boolean[i2];
                        zArr[0] = false;
                        long j7 = 8;
                        if (this.supportsPreloading) {
                        }
                        str8 = str3;
                        str9 = str4;
                        z4 = z3;
                        str10 = "rws";
                        if (str9 != null) {
                        }
                        if (this.fileMetadata != null) {
                        }
                        if (!this.cacheFileTemp.exists()) {
                        }
                        arrayList = this.notLoadedBytesRanges;
                        if (arrayList != null) {
                        }
                        if (BuildVars.LOGS_ENABLED) {
                        }
                        if (str8 != null) {
                        }
                        if (!this.isPreloadVideoOperation) {
                        }
                        updateProgress();
                        RandomAccessFile randomAccessFile3 = new RandomAccessFile(this.cacheFileTemp, str10);
                        this.fileOutputStream = randomAccessFile3;
                        j2 = this.downloadedBytes;
                        if (j2 != 0) {
                        }
                        r1 = 0;
                        z5 = true;
                        if (this.fileOutputStream != null) {
                        }
                    }
                    i2 = 1;
                } else {
                    i2 = 1;
                    z3 = false;
                }
                final boolean[] zArr2 = new boolean[i2];
                zArr2[0] = false;
                long j72 = 8;
                if (this.supportsPreloading || str5 == null) {
                    str8 = str3;
                    str9 = str4;
                    z4 = z3;
                    str10 = "rws";
                } else {
                    this.cacheFilePreload = new File(this.tempPath, str5);
                    try {
                        RandomAccessFile randomAccessFile4 = new RandomAccessFile(this.cacheFilePreload, "rws");
                        this.preloadStream = randomAccessFile4;
                        long length2 = randomAccessFile4.length();
                        this.preloadStreamFileOffset = 1;
                        long j8 = 1;
                        if (length2 - 0 > 1) {
                            zArr2[0] = this.preloadStream.readByte() != 0;
                            while (j8 < length2) {
                                if (length2 - j8 < j72) {
                                    break;
                                }
                                long readLong = this.preloadStream.readLong();
                                long j9 = j8 + j72;
                                if (length2 - j9 < j72 || readLong < 0) {
                                    break;
                                }
                                boolean z9 = z3;
                                try {
                                    if (readLong <= this.totalBytesCount) {
                                        long readLong2 = this.preloadStream.readLong();
                                        long j10 = j9 + j72;
                                        if (length2 - j10 >= readLong2 && readLong2 <= this.currentDownloadChunkSize) {
                                            PreloadRange preloadRange = new PreloadRange(j10, readLong2);
                                            long j11 = j10 + readLong2;
                                            this.preloadStream.seek(j11);
                                            if (length2 - j11 >= 24) {
                                                long readLong3 = this.preloadStream.readLong();
                                                this.foundMoovSize = readLong3;
                                                if (readLong3 != 0) {
                                                    z4 = z9;
                                                    str10 = str12;
                                                    try {
                                                        str8 = str3;
                                                        str9 = str4;
                                                    } catch (Exception e4) {
                                                        e = e4;
                                                        str8 = str3;
                                                        str9 = str4;
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
                                                        if (str9 != null) {
                                                        }
                                                        if (this.fileMetadata != null) {
                                                        }
                                                        if (!this.cacheFileTemp.exists()) {
                                                        }
                                                        arrayList = this.notLoadedBytesRanges;
                                                        if (arrayList != null) {
                                                        }
                                                        if (BuildVars.LOGS_ENABLED) {
                                                        }
                                                        if (str8 != null) {
                                                        }
                                                        if (!this.isPreloadVideoOperation) {
                                                            copyNotLoadedRanges();
                                                        }
                                                        updateProgress();
                                                        RandomAccessFile randomAccessFile32 = new RandomAccessFile(this.cacheFileTemp, str10);
                                                        this.fileOutputStream = randomAccessFile32;
                                                        j2 = this.downloadedBytes;
                                                        if (j2 != 0) {
                                                        }
                                                        r1 = 0;
                                                        z5 = true;
                                                        if (this.fileOutputStream != null) {
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
                                                        if (str9 != null) {
                                                        }
                                                        if (this.fileMetadata != null) {
                                                        }
                                                        if (!this.cacheFileTemp.exists()) {
                                                        }
                                                        arrayList = this.notLoadedBytesRanges;
                                                        if (arrayList != null) {
                                                        }
                                                        if (BuildVars.LOGS_ENABLED) {
                                                        }
                                                        if (str8 != null) {
                                                        }
                                                        if (!this.isPreloadVideoOperation) {
                                                        }
                                                        updateProgress();
                                                        RandomAccessFile randomAccessFile322 = new RandomAccessFile(this.cacheFileTemp, str10);
                                                        this.fileOutputStream = randomAccessFile322;
                                                        j2 = this.downloadedBytes;
                                                        if (j2 != 0) {
                                                        }
                                                        r1 = 0;
                                                        z5 = true;
                                                        if (this.fileOutputStream != null) {
                                                        }
                                                    }
                                                } else {
                                                    str8 = str3;
                                                    str9 = str4;
                                                    z4 = z9;
                                                    str10 = str12;
                                                }
                                                this.nextPreloadDownloadOffset = this.preloadStream.readLong();
                                                this.nextAtomOffset = this.preloadStream.readLong();
                                                long j12 = j11 + 24;
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
                                                j8 = j12;
                                                str12 = str10;
                                                z3 = z4;
                                                str3 = str8;
                                                str4 = str9;
                                                j72 = 8;
                                            }
                                        }
                                    }
                                    str8 = str3;
                                    str9 = str4;
                                    z4 = z9;
                                    break;
                                } catch (Exception e7) {
                                    e = e7;
                                    str8 = str3;
                                    str9 = str4;
                                    z4 = z9;
                                    str10 = str12;
                                    FileLog.e((Throwable) e, false);
                                    if (!this.isPreloadVideoOperation) {
                                    }
                                    if (str9 != null) {
                                    }
                                    if (this.fileMetadata != null) {
                                    }
                                    if (!this.cacheFileTemp.exists()) {
                                    }
                                    arrayList = this.notLoadedBytesRanges;
                                    if (arrayList != null) {
                                    }
                                    if (BuildVars.LOGS_ENABLED) {
                                    }
                                    if (str8 != null) {
                                    }
                                    if (!this.isPreloadVideoOperation) {
                                    }
                                    updateProgress();
                                    RandomAccessFile randomAccessFile3222 = new RandomAccessFile(this.cacheFileTemp, str10);
                                    this.fileOutputStream = randomAccessFile3222;
                                    j2 = this.downloadedBytes;
                                    if (j2 != 0) {
                                    }
                                    r1 = 0;
                                    z5 = true;
                                    if (this.fileOutputStream != null) {
                                    }
                                }
                            }
                        }
                        str8 = str3;
                        str9 = str4;
                        z4 = z3;
                        str10 = str12;
                        this.preloadStream.seek(this.preloadStreamFileOffset);
                    } catch (Exception e8) {
                        e = e8;
                        str8 = str3;
                        str9 = str4;
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
                if (str9 != null) {
                    this.cacheFileParts = new File(this.tempPath, str9);
                    try {
                        RandomAccessFile randomAccessFile5 = new RandomAccessFile(this.cacheFileParts, str10);
                        this.filePartsStream = randomAccessFile5;
                        long length3 = randomAccessFile5.length();
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
                    } catch (Exception e10) {
                        FileLog.e(e10, !AndroidUtilities.isFilNotFoundException(e10));
                    }
                }
                if (this.fileMetadata != null) {
                    FileLoader.getInstance(this.currentAccount).getFileDatabase().saveFileDialogId(this.cacheFileParts, this.fileMetadata);
                    FileLoader.getInstance(this.currentAccount).getFileDatabase().saveFileDialogId(this.cacheFileTemp, this.fileMetadata);
                }
                if (!this.cacheFileTemp.exists()) {
                    ArrayList<Range> arrayList2 = this.notLoadedBytesRanges;
                    if (arrayList2 != null && arrayList2.isEmpty()) {
                        this.notLoadedBytesRanges.add(new Range(0L, this.totalBytesCount));
                        this.notRequestedBytesRanges.add(new Range(0L, this.totalBytesCount));
                    }
                } else if (z4) {
                    this.cacheFileTemp.delete();
                } else {
                    long length4 = this.cacheFileTemp.length();
                    if (str8 != null && length4 % this.currentDownloadChunkSize != 0) {
                        this.requestedBytesCount = 0L;
                    } else {
                        long length5 = this.cacheFileTemp.length();
                        int i6 = this.currentDownloadChunkSize;
                        long j13 = (length5 / i6) * i6;
                        this.downloadedBytes = j13;
                        this.requestedBytesCount = j13;
                    }
                    ArrayList<Range> arrayList3 = this.notLoadedBytesRanges;
                    if (arrayList3 != null && arrayList3.isEmpty()) {
                        this.notLoadedBytesRanges.add(new Range(this.downloadedBytes, this.totalBytesCount));
                        this.notRequestedBytesRanges.add(new Range(this.downloadedBytes, this.totalBytesCount));
                    }
                }
                arrayList = this.notLoadedBytesRanges;
                if (arrayList != null) {
                    this.downloadedBytes = this.totalBytesCount;
                    int size = arrayList.size();
                    for (int i7 = 0; i7 < size; i7++) {
                        Range range = this.notLoadedBytesRanges.get(i7);
                        this.downloadedBytes -= range.end - range.start;
                    }
                    this.requestedBytesCount = this.downloadedBytes;
                }
                if (BuildVars.LOGS_ENABLED) {
                    if (this.isPreloadVideoOperation) {
                        FileLog.d("start preloading file to temp = " + this.cacheFileTemp);
                    } else {
                        FileLog.d("start loading file to temp = " + this.cacheFileTemp + " final = " + this.cacheFileFinal + " priority" + this.priority);
                    }
                }
                if (str8 != null) {
                    this.cacheIvTemp = new File(this.tempPath, str8);
                    try {
                        this.fiv = new RandomAccessFile(this.cacheIvTemp, str10);
                        if (this.downloadedBytes != 0 && !z4) {
                            try {
                                long length6 = this.cacheIvTemp.length();
                                if (length6 <= 0) {
                                    j3 = 0;
                                } else if (length6 % 64 == 0) {
                                    this.fiv.read(this.iv, 0, 64);
                                } else {
                                    j3 = 0;
                                }
                            } catch (Exception e11) {
                                e = e11;
                                j3 = 0;
                            }
                            try {
                                this.downloadedBytes = j3;
                                this.requestedBytesCount = j3;
                            } catch (Exception e12) {
                                e = e12;
                                this.downloadedBytes = j3;
                                this.requestedBytesCount = j3;
                                if (AndroidUtilities.isENOSPC(e)) {
                                    LaunchActivity.checkFreeDiscSpaceStatic(1);
                                    FileLog.e((Throwable) e, false);
                                } else if (AndroidUtilities.isEROFS(e)) {
                                    SharedConfig.checkSdCard(this.cacheFileFinal);
                                    FileLog.e((Throwable) e, false);
                                } else {
                                    FileLog.e(e);
                                }
                                if (!this.isPreloadVideoOperation) {
                                }
                                updateProgress();
                                RandomAccessFile randomAccessFile32222 = new RandomAccessFile(this.cacheFileTemp, str10);
                                this.fileOutputStream = randomAccessFile32222;
                                j2 = this.downloadedBytes;
                                if (j2 != 0) {
                                }
                                r1 = 0;
                                z5 = true;
                                if (this.fileOutputStream != null) {
                                }
                            }
                        }
                    } catch (Exception e13) {
                        e = e13;
                        j3 = 0;
                    }
                }
                if (!this.isPreloadVideoOperation && this.downloadedBytes != 0 && this.totalBytesCount > 0) {
                    copyNotLoadedRanges();
                }
                updateProgress();
                try {
                    RandomAccessFile randomAccessFile322222 = new RandomAccessFile(this.cacheFileTemp, str10);
                    this.fileOutputStream = randomAccessFile322222;
                    j2 = this.downloadedBytes;
                    if (j2 != 0) {
                        randomAccessFile322222.seek(j2);
                    }
                    r1 = 0;
                    z5 = true;
                } catch (Exception e14) {
                    r1 = 0;
                    FileLog.e((Throwable) e14, false);
                    if (AndroidUtilities.isENOSPC(e14)) {
                        LaunchActivity.checkFreeDiscSpaceStatic(1);
                        onFail(true, -1);
                        return false;
                    }
                    z5 = true;
                    if (AndroidUtilities.isEROFS(e14)) {
                        SharedConfig.checkSdCard(this.cacheFileFinal);
                        FileLog.e((Throwable) e14, false);
                        onFail(true, -1);
                        return false;
                    }
                }
                if (this.fileOutputStream != null) {
                    onFail(z5, r1);
                    return r1;
                }
                this.started = z5;
                Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda14
                    @Override // java.lang.Runnable
                    public final void run() {
                        FileLoadOperation.this.lambda$start$6(zArr2);
                    }
                });
            } else {
                boolean z10 = false;
                this.started = true;
                try {
                    onFinishLoadingFile(false, 1);
                } catch (Exception e15) {
                    e = e15;
                }
                try {
                    FilePathDatabase.PathData pathData = this.pathSaveData;
                    if (pathData != null) {
                        this.delegate.saveFilePath(pathData, this.cacheFileFinal);
                    }
                } catch (Exception e16) {
                    e = e16;
                    z10 = false;
                    FileLog.e(e, z10);
                    if (AndroidUtilities.isENOSPC(e)) {
                        z2 = true;
                        LaunchActivity.checkFreeDiscSpaceStatic(1);
                        i = -1;
                        onFail(true, -1);
                    } else {
                        z2 = true;
                        i = -1;
                    }
                    if (AndroidUtilities.isEROFS(e)) {
                        SharedConfig.checkSdCard(this.cacheFileFinal);
                        onFail(z2, i);
                        return false;
                    }
                    onFail(z2, 0);
                    return z2;
                }
            }
            return true;
        }
        TLRPC$InputFileLocation tLRPC$InputFileLocation = this.location;
        long j14 = tLRPC$InputFileLocation.volume_id;
        if (j14 != 0 && tLRPC$InputFileLocation.local_id != 0) {
            int i8 = this.datacenterId;
            if (i8 == Integer.MIN_VALUE || j14 == -2147483648L || i8 == 0) {
                onFail(true, 0);
                return false;
            } else if (this.encryptFile) {
                str6 = this.location.volume_id + "_" + this.location.local_id + ".temp.enc";
                str2 = this.location.volume_id + "_" + this.location.local_id + "." + this.ext + ".enc";
                if (this.key != null) {
                    str7 = this.location.volume_id + "_" + this.location.local_id + "_64.iv.enc";
                }
                str3 = null;
                str4 = null;
                str5 = null;
                this.requestInfos = new ArrayList<>(this.currentMaxDownloadRequests);
                this.delayedRequestInfos = new ArrayList<>(this.currentMaxDownloadRequests - 1);
                this.state = 1;
                if (!(this.parentObject instanceof TLRPC$TL_theme)) {
                }
                exists = this.cacheFileFinal.exists();
                if (exists) {
                }
                if (exists) {
                }
                return true;
            } else {
                str = this.location.volume_id + "_" + this.location.local_id + ".temp";
                str2 = this.location.volume_id + "_" + this.location.local_id + "." + this.ext;
                str3 = this.key != null ? this.location.volume_id + "_" + this.location.local_id + "_64.iv" : null;
                str4 = this.notLoadedBytesRanges != null ? this.location.volume_id + "_" + this.location.local_id + "_64.pt" : null;
                str5 = this.location.volume_id + "_" + this.location.local_id + "_64.preload";
                str6 = str;
                this.requestInfos = new ArrayList<>(this.currentMaxDownloadRequests);
                this.delayedRequestInfos = new ArrayList<>(this.currentMaxDownloadRequests - 1);
                this.state = 1;
                if (!(this.parentObject instanceof TLRPC$TL_theme)) {
                }
                exists = this.cacheFileFinal.exists();
                if (exists) {
                }
                if (exists) {
                }
                return true;
            }
        } else if (this.datacenterId == 0 || tLRPC$InputFileLocation.id == 0) {
            onFail(true, 0);
            return false;
        } else if (this.encryptFile) {
            str6 = this.datacenterId + "_" + this.location.id + ".temp.enc";
            str2 = this.datacenterId + "_" + this.location.id + this.ext + ".enc";
            if (this.key != null) {
                str7 = this.datacenterId + "_" + this.location.id + "_64.iv.enc";
            }
            str3 = null;
            str4 = null;
            str5 = null;
            this.requestInfos = new ArrayList<>(this.currentMaxDownloadRequests);
            this.delayedRequestInfos = new ArrayList<>(this.currentMaxDownloadRequests - 1);
            this.state = 1;
            if (!(this.parentObject instanceof TLRPC$TL_theme)) {
            }
            exists = this.cacheFileFinal.exists();
            if (exists) {
            }
            if (exists) {
            }
            return true;
        } else {
            str = this.datacenterId + "_" + this.location.id + ".temp";
            str2 = this.datacenterId + "_" + this.location.id + this.ext;
            str3 = this.key != null ? this.datacenterId + "_" + this.location.id + "_64.iv" : null;
            str4 = this.notLoadedBytesRanges != null ? this.datacenterId + "_" + this.location.id + "_64.pt" : null;
            str5 = this.datacenterId + "_" + this.location.id + "_64.preload";
            str6 = str;
            this.requestInfos = new ArrayList<>(this.currentMaxDownloadRequests);
            this.delayedRequestInfos = new ArrayList<>(this.currentMaxDownloadRequests - 1);
            this.state = 1;
            if (!(this.parentObject instanceof TLRPC$TL_theme)) {
            }
            exists = this.cacheFileFinal.exists();
            if (exists) {
            }
            if (exists) {
            }
            return true;
        }
        str3 = str7;
        str4 = null;
        str5 = null;
        this.requestInfos = new ArrayList<>(this.currentMaxDownloadRequests);
        this.delayedRequestInfos = new ArrayList<>(this.currentMaxDownloadRequests - 1);
        this.state = 1;
        if (!(this.parentObject instanceof TLRPC$TL_theme)) {
        }
        exists = this.cacheFileFinal.exists();
        if (exists) {
        }
        if (exists) {
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$5(boolean z, long j, FileLoadOperationStream fileLoadOperationStream, boolean z2) {
        if (this.streamListeners == null) {
            this.streamListeners = new ArrayList<>();
        }
        if (z) {
            int i = this.currentDownloadChunkSize;
            long j2 = (j / i) * i;
            RequestInfo requestInfo = this.priorityRequestInfo;
            if (requestInfo != null && requestInfo.offset != j2) {
                this.requestInfos.remove(this.priorityRequestInfo);
                this.requestedBytesCount -= this.currentDownloadChunkSize;
                removePart(this.notRequestedBytesRanges, this.priorityRequestInfo.offset, this.currentDownloadChunkSize + this.priorityRequestInfo.offset);
                if (this.priorityRequestInfo.requestToken != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.priorityRequestInfo.requestToken, false);
                    this.requestsCount--;
                }
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d("frame get cancel request at offset " + this.priorityRequestInfo.offset);
                }
                this.priorityRequestInfo = null;
            }
            if (this.priorityRequestInfo == null) {
                this.streamPriorityStartOffset = j2;
            }
        } else {
            int i2 = this.currentDownloadChunkSize;
            this.streamStartOffset = (j / i2) * i2;
        }
        this.streamListeners.add(fileLoadOperationStream);
        if (z2) {
            if (this.preloadedBytesRanges != null && getDownloadedLengthFromOffsetInternal(this.notLoadedBytesRanges, this.streamStartOffset, 1L) == 0 && this.preloadedBytesRanges.get(Long.valueOf(this.streamStartOffset)) != null) {
                this.nextPartWasPreloaded = true;
            }
            startDownloadRequest();
            this.nextPartWasPreloaded = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$6(boolean[] zArr) {
        long j = this.totalBytesCount;
        if (j != 0 && ((this.isPreloadVideoOperation && zArr[0]) || this.downloadedBytes == j)) {
            try {
                onFinishLoadingFile(false, 1);
                return;
            } catch (Exception unused) {
                onFail(true, 0);
                return;
            }
        }
        startDownloadRequest();
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

    public boolean isPaused() {
        return this.paused;
    }

    public void setIsPreloadVideoOperation(final boolean z) {
        boolean z2 = this.isPreloadVideoOperation;
        if (z2 != z) {
            if (!z || this.totalBytesCount > 2097152) {
                if (!z && z2) {
                    if (this.state == 3) {
                        this.isPreloadVideoOperation = z;
                        this.state = 0;
                        this.preloadFinished = false;
                        start();
                        return;
                    } else if (this.state == 1) {
                        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda8
                            @Override // java.lang.Runnable
                            public final void run() {
                                FileLoadOperation.this.lambda$setIsPreloadVideoOperation$7(z);
                            }
                        });
                        return;
                    } else {
                        this.isPreloadVideoOperation = z;
                        return;
                    }
                }
                this.isPreloadVideoOperation = z;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setIsPreloadVideoOperation$7(boolean z) {
        this.requestedBytesCount = 0L;
        clearOperaion(null, true);
        this.isPreloadVideoOperation = z;
        startDownloadRequest();
    }

    public boolean isPreloadVideoOperation() {
        return this.isPreloadVideoOperation;
    }

    public boolean isPreloadFinished() {
        return this.preloadFinished;
    }

    public void cancel() {
        cancel(false);
    }

    private void cancel(final boolean z) {
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$cancel$8(z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancel$8(boolean z) {
        if (this.state != 3 && this.state != 2) {
            cancelRequests();
            onFail(false, 1);
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

    private void cancelRequests() {
        if (this.requestInfos != null) {
            for (int i = 0; i < this.requestInfos.size(); i++) {
                RequestInfo requestInfo = this.requestInfos.get(i);
                if (requestInfo.requestToken != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(requestInfo.requestToken, false);
                }
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
                    try {
                        this.filePartsStream.getChannel().close();
                    } catch (Exception e7) {
                        FileLog.e(e7);
                    }
                    this.filePartsStream.close();
                    this.filePartsStream = null;
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

    private void onFinishLoadingFile(final boolean z, int i) {
        if (this.state != 1) {
            return;
        }
        this.state = 3;
        notifyStreamListeners();
        cleanup();
        if (this.isPreloadVideoOperation) {
            this.preloadFinished = true;
            if (BuildVars.DEBUG_VERSION) {
                if (i == 1) {
                    FileLog.d("file already exist " + this.cacheFileTemp);
                } else {
                    FileLog.d("finished preloading file to " + this.cacheFileTemp + " loaded " + this.totalPreloadedBytes + " of " + this.totalBytesCount);
                }
            }
            if (this.fileMetadata != null) {
                if (this.cacheFileTemp != null) {
                    FileLoader.getInstance(this.currentAccount).getFileDatabase().removeFiles(Collections.singletonList(new CacheModel.FileInfo(this.cacheFileTemp)));
                }
                if (this.cacheFileParts != null) {
                    FileLoader.getInstance(this.currentAccount).getFileDatabase().removeFiles(Collections.singletonList(new CacheModel.FileInfo(this.cacheFileParts)));
                }
            }
            this.delegate.didFinishLoadingFile(this, this.cacheFileFinal);
            return;
        }
        final File file = this.cacheIvTemp;
        final File file2 = this.cacheFileParts;
        final File file3 = this.cacheFilePreload;
        final File file4 = this.cacheFileTemp;
        filesQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$onFinishLoadingFile$12(file, file2, file3, file4, z);
            }
        });
        this.cacheIvTemp = null;
        this.cacheFileParts = null;
        this.cacheFilePreload = null;
        this.delegate.didPreFinishLoading(this, this.cacheFileFinal);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:26:0x006c  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0164  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$onFinishLoadingFile$12(File file, File file2, File file3, File file4, final boolean z) {
        File file5;
        int lastIndexOf;
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
                        if (!this.ungzip) {
                        }
                    } catch (Throwable th) {
                        th = th;
                        FileLog.e(th, !AndroidUtilities.isFilNotFoundException(th));
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.e("unable to ungzip temp = " + file4 + " to final = " + this.cacheFileFinal);
                        }
                        file4 = file5;
                        if (!this.ungzip) {
                        }
                    }
                } catch (ZipException unused2) {
                } catch (Throwable th2) {
                    th = th2;
                    file5 = file4;
                }
                file4 = file5;
            }
            if (!this.ungzip) {
                if (this.parentObject instanceof TLRPC$TL_theme) {
                    try {
                        z2 = AndroidUtilities.copyFile(file4, this.cacheFileFinal);
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                } else {
                    try {
                        if (this.pathSaveData != null) {
                            synchronized (lockObject) {
                                this.cacheFileFinal = new File(this.storePath, this.storeFileName);
                                int i = 1;
                                while (this.cacheFileFinal.exists()) {
                                    if (this.storeFileName.lastIndexOf(46) > 0) {
                                        str = this.storeFileName.substring(0, lastIndexOf) + " (" + i + ")" + this.storeFileName.substring(lastIndexOf);
                                    } else {
                                        str = this.storeFileName + " (" + i + ")";
                                    }
                                    this.cacheFileFinal = new File(this.storePath, str);
                                    i++;
                                }
                            }
                        }
                        z2 = file4.renameTo(this.cacheFileFinal);
                    } catch (Exception e2) {
                        FileLog.e(e2);
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
                        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda9
                            @Override // java.lang.Runnable
                            public final void run() {
                                FileLoadOperation.this.lambda$onFinishLoadingFile$9(z);
                            }
                        }, 200L);
                        return;
                    }
                    this.cacheFileFinal = file4;
                } else if (this.pathSaveData != null && this.cacheFileFinal.exists()) {
                    this.delegate.saveFilePath(this.pathSaveData, this.cacheFileFinal);
                }
            } else {
                Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        FileLoadOperation.this.lambda$onFinishLoadingFile$10();
                    }
                });
                return;
            }
        }
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                FileLoadOperation.this.lambda$onFinishLoadingFile$11(z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishLoadingFile$9(boolean z) {
        try {
            onFinishLoadingFile(z, 0);
        } catch (Exception unused) {
            onFail(false, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishLoadingFile$10() {
        onFail(false, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishLoadingFile$11(boolean z) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("finished downloading file to " + this.cacheFileFinal + " time = " + (System.currentTimeMillis() - this.startTime) + " dc = " + this.datacenterId + " size = " + AndroidUtilities.formatFileSize(this.totalBytesCount));
        }
        if (z) {
            int i = this.currentType;
            if (i == 50331648) {
                StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ApplicationLoader.getCurrentNetworkType(), 3, 1);
            } else if (i == 33554432) {
                StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ApplicationLoader.getCurrentNetworkType(), 2, 1);
            } else if (i == 16777216) {
                StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ApplicationLoader.getCurrentNetworkType(), 4, 1);
            } else if (i == 67108864) {
                String str = this.ext;
                if (str != null && (str.toLowerCase().endsWith("mp3") || this.ext.toLowerCase().endsWith("m4a"))) {
                    StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ApplicationLoader.getCurrentNetworkType(), 7, 1);
                } else {
                    StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ApplicationLoader.getCurrentNetworkType(), 5, 1);
                }
            }
        }
        this.delegate.didFinishLoadingFile(this, this.cacheFileFinal);
    }

    private void delayRequestInfo(RequestInfo requestInfo) {
        this.delayedRequestInfos.add(requestInfo);
        if (requestInfo.response != null) {
            requestInfo.response.disableFree = true;
        } else if (requestInfo.responseWeb != null) {
            requestInfo.responseWeb.disableFree = true;
        } else if (requestInfo.responseCdn != null) {
            requestInfo.responseCdn.disableFree = true;
        }
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

    private void requestFileOffsets(long j) {
        if (this.requestingCdnOffsets) {
            return;
        }
        this.requestingCdnOffsets = true;
        TLRPC$TL_upload_getCdnFileHashes tLRPC$TL_upload_getCdnFileHashes = new TLRPC$TL_upload_getCdnFileHashes();
        tLRPC$TL_upload_getCdnFileHashes.file_token = this.cdnToken;
        tLRPC$TL_upload_getCdnFileHashes.offset = j;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_upload_getCdnFileHashes, new RequestDelegate() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda16
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                FileLoadOperation.this.lambda$requestFileOffsets$13(tLObject, tLRPC$TL_error);
            }
        }, null, null, 0, this.datacenterId, 1, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestFileOffsets$13(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
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

    /* JADX WARN: Code restructure failed: missing block: B:143:0x03c1, code lost:
        if (r1 == (r5 - r11)) goto L196;
     */
    /* JADX WARN: Code restructure failed: missing block: B:146:0x03c7, code lost:
        if (r0 != false) goto L196;
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x0252, code lost:
        if (r1 >= r11) goto L151;
     */
    /* JADX WARN: Removed duplicated region for block: B:185:0x0529 A[Catch: Exception -> 0x05a3, TryCatch #1 {Exception -> 0x05a3, blocks: (B:12:0x0057, B:14:0x005b, B:16:0x0065, B:18:0x0069, B:20:0x006f, B:30:0x0094, B:33:0x009c, B:35:0x00a4, B:37:0x00b4, B:40:0x00c2, B:42:0x00c9, B:44:0x00e1, B:45:0x0113, B:47:0x0117, B:49:0x013b, B:50:0x0164, B:52:0x0168, B:53:0x016f, B:55:0x019a, B:57:0x01ac, B:59:0x01c1, B:61:0x01d6, B:63:0x01e3, B:65:0x01e8, B:67:0x0209, B:69:0x020d, B:71:0x0213, B:73:0x0219, B:79:0x0225, B:183:0x0521, B:185:0x0529, B:187:0x0535, B:190:0x0540, B:191:0x0543, B:193:0x054e, B:195:0x0554, B:196:0x0563, B:198:0x0569, B:199:0x0578, B:201:0x057e, B:203:0x058e, B:204:0x0594, B:206:0x0599, B:80:0x0232, B:82:0x0236, B:60:0x01cd, B:62:0x01db, B:83:0x0240, B:102:0x0275, B:104:0x0279, B:106:0x0292, B:108:0x029a, B:113:0x02ae, B:114:0x02c4, B:115:0x02c5, B:116:0x02c9, B:118:0x02cd, B:119:0x02ff, B:121:0x0303, B:123:0x0310, B:124:0x0347, B:126:0x0369, B:128:0x037b, B:130:0x038b, B:136:0x039b, B:138:0x03b0, B:140:0x03b7, B:142:0x03bd, B:147:0x03c9, B:149:0x03d9, B:150:0x03ea, B:155:0x03fb, B:156:0x0402, B:157:0x0403, B:159:0x0410, B:161:0x044a, B:163:0x0459, B:165:0x045d, B:167:0x0461, B:168:0x04b0, B:170:0x04b4, B:171:0x04d2, B:173:0x04dc, B:133:0x0395, B:174:0x04f5, B:176:0x04f9, B:177:0x0505, B:179:0x050d, B:181:0x0512, B:89:0x0258, B:93:0x0260, B:207:0x059d, B:22:0x0077, B:24:0x007d, B:25:0x0084, B:27:0x008a), top: B:265:0x0057 }] */
    /* JADX WARN: Removed duplicated region for block: B:203:0x058e A[Catch: Exception -> 0x05a3, TryCatch #1 {Exception -> 0x05a3, blocks: (B:12:0x0057, B:14:0x005b, B:16:0x0065, B:18:0x0069, B:20:0x006f, B:30:0x0094, B:33:0x009c, B:35:0x00a4, B:37:0x00b4, B:40:0x00c2, B:42:0x00c9, B:44:0x00e1, B:45:0x0113, B:47:0x0117, B:49:0x013b, B:50:0x0164, B:52:0x0168, B:53:0x016f, B:55:0x019a, B:57:0x01ac, B:59:0x01c1, B:61:0x01d6, B:63:0x01e3, B:65:0x01e8, B:67:0x0209, B:69:0x020d, B:71:0x0213, B:73:0x0219, B:79:0x0225, B:183:0x0521, B:185:0x0529, B:187:0x0535, B:190:0x0540, B:191:0x0543, B:193:0x054e, B:195:0x0554, B:196:0x0563, B:198:0x0569, B:199:0x0578, B:201:0x057e, B:203:0x058e, B:204:0x0594, B:206:0x0599, B:80:0x0232, B:82:0x0236, B:60:0x01cd, B:62:0x01db, B:83:0x0240, B:102:0x0275, B:104:0x0279, B:106:0x0292, B:108:0x029a, B:113:0x02ae, B:114:0x02c4, B:115:0x02c5, B:116:0x02c9, B:118:0x02cd, B:119:0x02ff, B:121:0x0303, B:123:0x0310, B:124:0x0347, B:126:0x0369, B:128:0x037b, B:130:0x038b, B:136:0x039b, B:138:0x03b0, B:140:0x03b7, B:142:0x03bd, B:147:0x03c9, B:149:0x03d9, B:150:0x03ea, B:155:0x03fb, B:156:0x0402, B:157:0x0403, B:159:0x0410, B:161:0x044a, B:163:0x0459, B:165:0x045d, B:167:0x0461, B:168:0x04b0, B:170:0x04b4, B:171:0x04d2, B:173:0x04dc, B:133:0x0395, B:174:0x04f5, B:176:0x04f9, B:177:0x0505, B:179:0x050d, B:181:0x0512, B:89:0x0258, B:93:0x0260, B:207:0x059d, B:22:0x0077, B:24:0x007d, B:25:0x0084, B:27:0x008a), top: B:265:0x0057 }] */
    /* JADX WARN: Removed duplicated region for block: B:204:0x0594 A[Catch: Exception -> 0x05a3, TryCatch #1 {Exception -> 0x05a3, blocks: (B:12:0x0057, B:14:0x005b, B:16:0x0065, B:18:0x0069, B:20:0x006f, B:30:0x0094, B:33:0x009c, B:35:0x00a4, B:37:0x00b4, B:40:0x00c2, B:42:0x00c9, B:44:0x00e1, B:45:0x0113, B:47:0x0117, B:49:0x013b, B:50:0x0164, B:52:0x0168, B:53:0x016f, B:55:0x019a, B:57:0x01ac, B:59:0x01c1, B:61:0x01d6, B:63:0x01e3, B:65:0x01e8, B:67:0x0209, B:69:0x020d, B:71:0x0213, B:73:0x0219, B:79:0x0225, B:183:0x0521, B:185:0x0529, B:187:0x0535, B:190:0x0540, B:191:0x0543, B:193:0x054e, B:195:0x0554, B:196:0x0563, B:198:0x0569, B:199:0x0578, B:201:0x057e, B:203:0x058e, B:204:0x0594, B:206:0x0599, B:80:0x0232, B:82:0x0236, B:60:0x01cd, B:62:0x01db, B:83:0x0240, B:102:0x0275, B:104:0x0279, B:106:0x0292, B:108:0x029a, B:113:0x02ae, B:114:0x02c4, B:115:0x02c5, B:116:0x02c9, B:118:0x02cd, B:119:0x02ff, B:121:0x0303, B:123:0x0310, B:124:0x0347, B:126:0x0369, B:128:0x037b, B:130:0x038b, B:136:0x039b, B:138:0x03b0, B:140:0x03b7, B:142:0x03bd, B:147:0x03c9, B:149:0x03d9, B:150:0x03ea, B:155:0x03fb, B:156:0x0402, B:157:0x0403, B:159:0x0410, B:161:0x044a, B:163:0x0459, B:165:0x045d, B:167:0x0461, B:168:0x04b0, B:170:0x04b4, B:171:0x04d2, B:173:0x04dc, B:133:0x0395, B:174:0x04f5, B:176:0x04f9, B:177:0x0505, B:179:0x050d, B:181:0x0512, B:89:0x0258, B:93:0x0260, B:207:0x059d, B:22:0x0077, B:24:0x007d, B:25:0x0084, B:27:0x008a), top: B:265:0x0057 }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00a4 A[Catch: Exception -> 0x05a3, TryCatch #1 {Exception -> 0x05a3, blocks: (B:12:0x0057, B:14:0x005b, B:16:0x0065, B:18:0x0069, B:20:0x006f, B:30:0x0094, B:33:0x009c, B:35:0x00a4, B:37:0x00b4, B:40:0x00c2, B:42:0x00c9, B:44:0x00e1, B:45:0x0113, B:47:0x0117, B:49:0x013b, B:50:0x0164, B:52:0x0168, B:53:0x016f, B:55:0x019a, B:57:0x01ac, B:59:0x01c1, B:61:0x01d6, B:63:0x01e3, B:65:0x01e8, B:67:0x0209, B:69:0x020d, B:71:0x0213, B:73:0x0219, B:79:0x0225, B:183:0x0521, B:185:0x0529, B:187:0x0535, B:190:0x0540, B:191:0x0543, B:193:0x054e, B:195:0x0554, B:196:0x0563, B:198:0x0569, B:199:0x0578, B:201:0x057e, B:203:0x058e, B:204:0x0594, B:206:0x0599, B:80:0x0232, B:82:0x0236, B:60:0x01cd, B:62:0x01db, B:83:0x0240, B:102:0x0275, B:104:0x0279, B:106:0x0292, B:108:0x029a, B:113:0x02ae, B:114:0x02c4, B:115:0x02c5, B:116:0x02c9, B:118:0x02cd, B:119:0x02ff, B:121:0x0303, B:123:0x0310, B:124:0x0347, B:126:0x0369, B:128:0x037b, B:130:0x038b, B:136:0x039b, B:138:0x03b0, B:140:0x03b7, B:142:0x03bd, B:147:0x03c9, B:149:0x03d9, B:150:0x03ea, B:155:0x03fb, B:156:0x0402, B:157:0x0403, B:159:0x0410, B:161:0x044a, B:163:0x0459, B:165:0x045d, B:167:0x0461, B:168:0x04b0, B:170:0x04b4, B:171:0x04d2, B:173:0x04dc, B:133:0x0395, B:174:0x04f5, B:176:0x04f9, B:177:0x0505, B:179:0x050d, B:181:0x0512, B:89:0x0258, B:93:0x0260, B:207:0x059d, B:22:0x0077, B:24:0x007d, B:25:0x0084, B:27:0x008a), top: B:265:0x0057 }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00e1 A[Catch: Exception -> 0x05a3, TryCatch #1 {Exception -> 0x05a3, blocks: (B:12:0x0057, B:14:0x005b, B:16:0x0065, B:18:0x0069, B:20:0x006f, B:30:0x0094, B:33:0x009c, B:35:0x00a4, B:37:0x00b4, B:40:0x00c2, B:42:0x00c9, B:44:0x00e1, B:45:0x0113, B:47:0x0117, B:49:0x013b, B:50:0x0164, B:52:0x0168, B:53:0x016f, B:55:0x019a, B:57:0x01ac, B:59:0x01c1, B:61:0x01d6, B:63:0x01e3, B:65:0x01e8, B:67:0x0209, B:69:0x020d, B:71:0x0213, B:73:0x0219, B:79:0x0225, B:183:0x0521, B:185:0x0529, B:187:0x0535, B:190:0x0540, B:191:0x0543, B:193:0x054e, B:195:0x0554, B:196:0x0563, B:198:0x0569, B:199:0x0578, B:201:0x057e, B:203:0x058e, B:204:0x0594, B:206:0x0599, B:80:0x0232, B:82:0x0236, B:60:0x01cd, B:62:0x01db, B:83:0x0240, B:102:0x0275, B:104:0x0279, B:106:0x0292, B:108:0x029a, B:113:0x02ae, B:114:0x02c4, B:115:0x02c5, B:116:0x02c9, B:118:0x02cd, B:119:0x02ff, B:121:0x0303, B:123:0x0310, B:124:0x0347, B:126:0x0369, B:128:0x037b, B:130:0x038b, B:136:0x039b, B:138:0x03b0, B:140:0x03b7, B:142:0x03bd, B:147:0x03c9, B:149:0x03d9, B:150:0x03ea, B:155:0x03fb, B:156:0x0402, B:157:0x0403, B:159:0x0410, B:161:0x044a, B:163:0x0459, B:165:0x045d, B:167:0x0461, B:168:0x04b0, B:170:0x04b4, B:171:0x04d2, B:173:0x04dc, B:133:0x0395, B:174:0x04f5, B:176:0x04f9, B:177:0x0505, B:179:0x050d, B:181:0x0512, B:89:0x0258, B:93:0x0260, B:207:0x059d, B:22:0x0077, B:24:0x007d, B:25:0x0084, B:27:0x008a), top: B:265:0x0057 }] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0117 A[Catch: Exception -> 0x05a3, TryCatch #1 {Exception -> 0x05a3, blocks: (B:12:0x0057, B:14:0x005b, B:16:0x0065, B:18:0x0069, B:20:0x006f, B:30:0x0094, B:33:0x009c, B:35:0x00a4, B:37:0x00b4, B:40:0x00c2, B:42:0x00c9, B:44:0x00e1, B:45:0x0113, B:47:0x0117, B:49:0x013b, B:50:0x0164, B:52:0x0168, B:53:0x016f, B:55:0x019a, B:57:0x01ac, B:59:0x01c1, B:61:0x01d6, B:63:0x01e3, B:65:0x01e8, B:67:0x0209, B:69:0x020d, B:71:0x0213, B:73:0x0219, B:79:0x0225, B:183:0x0521, B:185:0x0529, B:187:0x0535, B:190:0x0540, B:191:0x0543, B:193:0x054e, B:195:0x0554, B:196:0x0563, B:198:0x0569, B:199:0x0578, B:201:0x057e, B:203:0x058e, B:204:0x0594, B:206:0x0599, B:80:0x0232, B:82:0x0236, B:60:0x01cd, B:62:0x01db, B:83:0x0240, B:102:0x0275, B:104:0x0279, B:106:0x0292, B:108:0x029a, B:113:0x02ae, B:114:0x02c4, B:115:0x02c5, B:116:0x02c9, B:118:0x02cd, B:119:0x02ff, B:121:0x0303, B:123:0x0310, B:124:0x0347, B:126:0x0369, B:128:0x037b, B:130:0x038b, B:136:0x039b, B:138:0x03b0, B:140:0x03b7, B:142:0x03bd, B:147:0x03c9, B:149:0x03d9, B:150:0x03ea, B:155:0x03fb, B:156:0x0402, B:157:0x0403, B:159:0x0410, B:161:0x044a, B:163:0x0459, B:165:0x045d, B:167:0x0461, B:168:0x04b0, B:170:0x04b4, B:171:0x04d2, B:173:0x04dc, B:133:0x0395, B:174:0x04f5, B:176:0x04f9, B:177:0x0505, B:179:0x050d, B:181:0x0512, B:89:0x0258, B:93:0x0260, B:207:0x059d, B:22:0x0077, B:24:0x007d, B:25:0x0084, B:27:0x008a), top: B:265:0x0057 }] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0225 A[Catch: Exception -> 0x05a3, TryCatch #1 {Exception -> 0x05a3, blocks: (B:12:0x0057, B:14:0x005b, B:16:0x0065, B:18:0x0069, B:20:0x006f, B:30:0x0094, B:33:0x009c, B:35:0x00a4, B:37:0x00b4, B:40:0x00c2, B:42:0x00c9, B:44:0x00e1, B:45:0x0113, B:47:0x0117, B:49:0x013b, B:50:0x0164, B:52:0x0168, B:53:0x016f, B:55:0x019a, B:57:0x01ac, B:59:0x01c1, B:61:0x01d6, B:63:0x01e3, B:65:0x01e8, B:67:0x0209, B:69:0x020d, B:71:0x0213, B:73:0x0219, B:79:0x0225, B:183:0x0521, B:185:0x0529, B:187:0x0535, B:190:0x0540, B:191:0x0543, B:193:0x054e, B:195:0x0554, B:196:0x0563, B:198:0x0569, B:199:0x0578, B:201:0x057e, B:203:0x058e, B:204:0x0594, B:206:0x0599, B:80:0x0232, B:82:0x0236, B:60:0x01cd, B:62:0x01db, B:83:0x0240, B:102:0x0275, B:104:0x0279, B:106:0x0292, B:108:0x029a, B:113:0x02ae, B:114:0x02c4, B:115:0x02c5, B:116:0x02c9, B:118:0x02cd, B:119:0x02ff, B:121:0x0303, B:123:0x0310, B:124:0x0347, B:126:0x0369, B:128:0x037b, B:130:0x038b, B:136:0x039b, B:138:0x03b0, B:140:0x03b7, B:142:0x03bd, B:147:0x03c9, B:149:0x03d9, B:150:0x03ea, B:155:0x03fb, B:156:0x0402, B:157:0x0403, B:159:0x0410, B:161:0x044a, B:163:0x0459, B:165:0x045d, B:167:0x0461, B:168:0x04b0, B:170:0x04b4, B:171:0x04d2, B:173:0x04dc, B:133:0x0395, B:174:0x04f5, B:176:0x04f9, B:177:0x0505, B:179:0x050d, B:181:0x0512, B:89:0x0258, B:93:0x0260, B:207:0x059d, B:22:0x0077, B:24:0x007d, B:25:0x0084, B:27:0x008a), top: B:265:0x0057 }] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0232 A[Catch: Exception -> 0x05a3, TryCatch #1 {Exception -> 0x05a3, blocks: (B:12:0x0057, B:14:0x005b, B:16:0x0065, B:18:0x0069, B:20:0x006f, B:30:0x0094, B:33:0x009c, B:35:0x00a4, B:37:0x00b4, B:40:0x00c2, B:42:0x00c9, B:44:0x00e1, B:45:0x0113, B:47:0x0117, B:49:0x013b, B:50:0x0164, B:52:0x0168, B:53:0x016f, B:55:0x019a, B:57:0x01ac, B:59:0x01c1, B:61:0x01d6, B:63:0x01e3, B:65:0x01e8, B:67:0x0209, B:69:0x020d, B:71:0x0213, B:73:0x0219, B:79:0x0225, B:183:0x0521, B:185:0x0529, B:187:0x0535, B:190:0x0540, B:191:0x0543, B:193:0x054e, B:195:0x0554, B:196:0x0563, B:198:0x0569, B:199:0x0578, B:201:0x057e, B:203:0x058e, B:204:0x0594, B:206:0x0599, B:80:0x0232, B:82:0x0236, B:60:0x01cd, B:62:0x01db, B:83:0x0240, B:102:0x0275, B:104:0x0279, B:106:0x0292, B:108:0x029a, B:113:0x02ae, B:114:0x02c4, B:115:0x02c5, B:116:0x02c9, B:118:0x02cd, B:119:0x02ff, B:121:0x0303, B:123:0x0310, B:124:0x0347, B:126:0x0369, B:128:0x037b, B:130:0x038b, B:136:0x039b, B:138:0x03b0, B:140:0x03b7, B:142:0x03bd, B:147:0x03c9, B:149:0x03d9, B:150:0x03ea, B:155:0x03fb, B:156:0x0402, B:157:0x0403, B:159:0x0410, B:161:0x044a, B:163:0x0459, B:165:0x045d, B:167:0x0461, B:168:0x04b0, B:170:0x04b4, B:171:0x04d2, B:173:0x04dc, B:133:0x0395, B:174:0x04f5, B:176:0x04f9, B:177:0x0505, B:179:0x050d, B:181:0x0512, B:89:0x0258, B:93:0x0260, B:207:0x059d, B:22:0x0077, B:24:0x007d, B:25:0x0084, B:27:0x008a), top: B:265:0x0057 }] */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0240 A[Catch: Exception -> 0x05a3, TryCatch #1 {Exception -> 0x05a3, blocks: (B:12:0x0057, B:14:0x005b, B:16:0x0065, B:18:0x0069, B:20:0x006f, B:30:0x0094, B:33:0x009c, B:35:0x00a4, B:37:0x00b4, B:40:0x00c2, B:42:0x00c9, B:44:0x00e1, B:45:0x0113, B:47:0x0117, B:49:0x013b, B:50:0x0164, B:52:0x0168, B:53:0x016f, B:55:0x019a, B:57:0x01ac, B:59:0x01c1, B:61:0x01d6, B:63:0x01e3, B:65:0x01e8, B:67:0x0209, B:69:0x020d, B:71:0x0213, B:73:0x0219, B:79:0x0225, B:183:0x0521, B:185:0x0529, B:187:0x0535, B:190:0x0540, B:191:0x0543, B:193:0x054e, B:195:0x0554, B:196:0x0563, B:198:0x0569, B:199:0x0578, B:201:0x057e, B:203:0x058e, B:204:0x0594, B:206:0x0599, B:80:0x0232, B:82:0x0236, B:60:0x01cd, B:62:0x01db, B:83:0x0240, B:102:0x0275, B:104:0x0279, B:106:0x0292, B:108:0x029a, B:113:0x02ae, B:114:0x02c4, B:115:0x02c5, B:116:0x02c9, B:118:0x02cd, B:119:0x02ff, B:121:0x0303, B:123:0x0310, B:124:0x0347, B:126:0x0369, B:128:0x037b, B:130:0x038b, B:136:0x039b, B:138:0x03b0, B:140:0x03b7, B:142:0x03bd, B:147:0x03c9, B:149:0x03d9, B:150:0x03ea, B:155:0x03fb, B:156:0x0402, B:157:0x0403, B:159:0x0410, B:161:0x044a, B:163:0x0459, B:165:0x045d, B:167:0x0461, B:168:0x04b0, B:170:0x04b4, B:171:0x04d2, B:173:0x04dc, B:133:0x0395, B:174:0x04f5, B:176:0x04f9, B:177:0x0505, B:179:0x050d, B:181:0x0512, B:89:0x0258, B:93:0x0260, B:207:0x059d, B:22:0x0077, B:24:0x007d, B:25:0x0084, B:27:0x008a), top: B:265:0x0057 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected boolean processRequestResult(RequestInfo requestInfo, TLRPC$TL_error tLRPC$TL_error) {
        NativeByteBuffer nativeByteBuffer;
        NativeByteBuffer nativeByteBuffer2;
        long j;
        boolean z;
        boolean z2;
        long j2;
        int i;
        long j3;
        if (this.state != 1) {
            if (BuildVars.DEBUG_VERSION && this.state == 3) {
                FileLog.e(new FileLog.IgnoreSentException("trying to write to finished file " + this.fileName + " offset " + requestInfo.offset + " " + this.totalBytesCount));
            }
            return false;
        }
        this.requestInfos.remove(requestInfo);
        Integer num = null;
        if (tLRPC$TL_error == null) {
            try {
                if (this.notLoadedBytesRanges != null || this.downloadedBytes == requestInfo.offset) {
                    if (requestInfo.response != null) {
                        nativeByteBuffer2 = requestInfo.response.bytes;
                    } else if (requestInfo.responseWeb != null) {
                        nativeByteBuffer2 = requestInfo.responseWeb.bytes;
                    } else if (requestInfo.responseCdn != null) {
                        nativeByteBuffer2 = requestInfo.responseCdn.bytes;
                    } else {
                        nativeByteBuffer = null;
                        if (nativeByteBuffer != null && nativeByteBuffer.limit() != 0) {
                            int limit = nativeByteBuffer.limit();
                            if (this.isCdn) {
                                long j4 = requestInfo.offset;
                                int i2 = this.cdnChunkCheckSize;
                                long j5 = i2 * (j4 / i2);
                                HashMap<Long, TLRPC$TL_fileHash> hashMap = this.cdnHashes;
                                if ((hashMap != null ? hashMap.get(Long.valueOf(j5)) : null) == null) {
                                    delayRequestInfo(requestInfo);
                                    requestFileOffsets(j5);
                                    return true;
                                }
                            }
                            if (requestInfo.responseCdn != null) {
                                long j6 = requestInfo.offset / 16;
                                byte[] bArr = this.cdnIv;
                                bArr[15] = (byte) (j6 & 255);
                                bArr[14] = (byte) ((j6 >> 8) & 255);
                                bArr[13] = (byte) ((j6 >> 16) & 255);
                                bArr[12] = (byte) ((j6 >> 24) & 255);
                                Utilities.aesCtrDecryption(nativeByteBuffer.buffer, this.cdnKey, bArr, 0, nativeByteBuffer.limit());
                            }
                            if (!this.isPreloadVideoOperation) {
                                this.preloadStream.writeLong(requestInfo.offset);
                                long j7 = limit;
                                this.preloadStream.writeLong(j7);
                                this.preloadStreamFileOffset += 16;
                                this.preloadStream.getChannel().write(nativeByteBuffer.buffer);
                                if (BuildVars.DEBUG_VERSION) {
                                    FileLog.d("save preload file part " + this.cacheFilePreload + " offset " + requestInfo.offset + " size " + limit);
                                }
                                if (this.preloadedBytesRanges == null) {
                                    this.preloadedBytesRanges = new HashMap<>();
                                }
                                this.preloadedBytesRanges.put(Long.valueOf(requestInfo.offset), new PreloadRange(this.preloadStreamFileOffset, j7));
                                this.totalPreloadedBytes += limit;
                                this.preloadStreamFileOffset += limit;
                                if (this.moovFound == 0) {
                                    j3 = 0;
                                    long findNextPreloadDownloadOffset = findNextPreloadDownloadOffset(this.nextAtomOffset, requestInfo.offset, nativeByteBuffer);
                                    if (findNextPreloadDownloadOffset < 0) {
                                        findNextPreloadDownloadOffset *= -1;
                                        long j8 = this.nextPreloadDownloadOffset + this.currentDownloadChunkSize;
                                        this.nextPreloadDownloadOffset = j8;
                                        if (j8 < this.totalBytesCount / 2) {
                                            long j9 = 1048576 + findNextPreloadDownloadOffset;
                                            this.foundMoovSize = j9;
                                            this.preloadNotRequestedBytesCount = j9;
                                            this.moovFound = 1;
                                        } else {
                                            this.foundMoovSize = 2097152L;
                                            this.preloadNotRequestedBytesCount = 2097152L;
                                            this.moovFound = 2;
                                        }
                                        this.nextPreloadDownloadOffset = -1L;
                                    } else {
                                        this.nextPreloadDownloadOffset += this.currentDownloadChunkSize;
                                    }
                                    this.nextAtomOffset = findNextPreloadDownloadOffset;
                                } else {
                                    j3 = 0;
                                }
                                this.preloadStream.writeLong(this.foundMoovSize);
                                this.preloadStream.writeLong(this.nextPreloadDownloadOffset);
                                this.preloadStream.writeLong(this.nextAtomOffset);
                                this.preloadStreamFileOffset += 24;
                                long j10 = this.nextPreloadDownloadOffset;
                                if (j10 != j3 && ((this.moovFound == 0 || this.foundMoovSize >= j3) && this.totalPreloadedBytes <= preloadMaxBytes && j10 < this.totalBytesCount)) {
                                    z = false;
                                    if (!z) {
                                        this.preloadStream.seek(j3);
                                        this.preloadStream.write(1);
                                    } else if (this.moovFound != 0) {
                                        this.foundMoovSize -= this.currentDownloadChunkSize;
                                    }
                                }
                                z = true;
                                if (!z) {
                                }
                            } else {
                                long j11 = limit;
                                long j12 = this.downloadedBytes + j11;
                                this.downloadedBytes = j12;
                                long j13 = this.totalBytesCount;
                                if (j13 <= 0) {
                                    int i3 = this.currentDownloadChunkSize;
                                    if (limit == i3) {
                                        if (j13 != j12) {
                                            j = 0;
                                            if (j12 % i3 != 0) {
                                            }
                                            z = false;
                                        } else {
                                            j = 0;
                                        }
                                        if (j13 > j) {
                                            if (j13 <= j12) {
                                            }
                                            z = false;
                                        }
                                    }
                                    z = true;
                                }
                                byte[] bArr2 = this.key;
                                if (bArr2 != null) {
                                    Utilities.aesIgeEncryption(nativeByteBuffer.buffer, bArr2, this.iv, false, true, 0, nativeByteBuffer.limit());
                                    if (z && this.bytesCountPadding != 0) {
                                        long limit2 = nativeByteBuffer.limit() - this.bytesCountPadding;
                                        if (BuildVars.DEBUG_VERSION && limit2 > 2147483647L) {
                                            throw new RuntimeException("Out of limit" + limit2);
                                        }
                                        nativeByteBuffer.limit((int) limit2);
                                    }
                                }
                                if (this.encryptFile) {
                                    long j14 = requestInfo.offset / 16;
                                    byte[] bArr3 = this.encryptIv;
                                    bArr3[15] = (byte) (j14 & 255);
                                    bArr3[14] = (byte) ((j14 >> 8) & 255);
                                    bArr3[13] = (byte) ((j14 >> 16) & 255);
                                    bArr3[12] = (byte) ((j14 >> 24) & 255);
                                    Utilities.aesCtrDecryption(nativeByteBuffer.buffer, this.encryptKey, bArr3, 0, nativeByteBuffer.limit());
                                }
                                if (this.notLoadedBytesRanges != null) {
                                    this.fileOutputStream.seek(requestInfo.offset);
                                    if (BuildVars.DEBUG_VERSION) {
                                        FileLog.d("save file part " + this.fileName + " offset=" + requestInfo.offset + " chunk_size=" + this.currentDownloadChunkSize + " isCdn=" + this.isCdn);
                                    }
                                }
                                this.fileOutputStream.getChannel().write(nativeByteBuffer.buffer);
                                addPart(this.notLoadedBytesRanges, requestInfo.offset, requestInfo.offset + j11, true);
                                if (this.isCdn) {
                                    long j15 = requestInfo.offset / this.cdnChunkCheckSize;
                                    int size = this.notCheckedCdnRanges.size();
                                    int i4 = 0;
                                    while (true) {
                                        if (i4 >= size) {
                                            z2 = true;
                                            break;
                                        }
                                        Range range = this.notCheckedCdnRanges.get(i4);
                                        if (range.start <= j15 && j15 <= range.end) {
                                            z2 = false;
                                            break;
                                        }
                                        i4++;
                                    }
                                    if (!z2) {
                                        int i5 = this.cdnChunkCheckSize;
                                        long j16 = j15 * i5;
                                        long downloadedLengthFromOffsetInternal = getDownloadedLengthFromOffsetInternal(this.notLoadedBytesRanges, j16, i5);
                                        if (downloadedLengthFromOffsetInternal != 0) {
                                            if (downloadedLengthFromOffsetInternal != this.cdnChunkCheckSize) {
                                                long j17 = this.totalBytesCount;
                                                if (j17 > 0) {
                                                }
                                                if (j17 <= 0) {
                                                }
                                            }
                                            TLRPC$TL_fileHash tLRPC$TL_fileHash = this.cdnHashes.get(Long.valueOf(j16));
                                            if (this.fileReadStream == null) {
                                                this.cdnCheckBytes = new byte[this.cdnChunkCheckSize];
                                                this.fileReadStream = new RandomAccessFile(this.cacheFileTemp, "r");
                                            }
                                            this.fileReadStream.seek(j16);
                                            if (BuildVars.DEBUG_VERSION && downloadedLengthFromOffsetInternal > 2147483647L) {
                                                throw new RuntimeException("!!!");
                                            }
                                            this.fileReadStream.readFully(this.cdnCheckBytes, 0, (int) downloadedLengthFromOffsetInternal);
                                            if (this.encryptFile) {
                                                long j18 = j16 / 16;
                                                byte[] bArr4 = this.encryptIv;
                                                j2 = j15;
                                                bArr4[15] = (byte) (j18 & 255);
                                                bArr4[14] = (byte) ((j18 >> 8) & 255);
                                                bArr4[13] = (byte) ((j18 >> 16) & 255);
                                                bArr4[12] = (byte) ((j18 >> 24) & 255);
                                                Utilities.aesCtrDecryptionByteArray(this.cdnCheckBytes, this.encryptKey, bArr4, 0, downloadedLengthFromOffsetInternal, 0);
                                            } else {
                                                j2 = j15;
                                            }
                                            if (!Arrays.equals(Utilities.computeSHA256(this.cdnCheckBytes, 0, downloadedLengthFromOffsetInternal), tLRPC$TL_fileHash.hash)) {
                                                if (BuildVars.LOGS_ENABLED) {
                                                    if (this.location != null) {
                                                        FileLog.e("invalid cdn hash " + this.location + " id = " + this.location.id + " local_id = " + this.location.local_id + " access_hash = " + this.location.access_hash + " volume_id = " + this.location.volume_id + " secret = " + this.location.secret);
                                                    } else if (this.webLocation != null) {
                                                        FileLog.e("invalid cdn hash  " + this.webLocation + " id = " + this.fileName);
                                                    }
                                                }
                                                onFail(false, 0);
                                                this.cacheFileTemp.delete();
                                                return false;
                                            }
                                            this.cdnHashes.remove(Long.valueOf(j16));
                                            long j19 = j2;
                                            addPart(this.notCheckedCdnRanges, j19, j19 + 1, false);
                                        }
                                    }
                                }
                                RandomAccessFile randomAccessFile = this.fiv;
                                if (randomAccessFile != null) {
                                    randomAccessFile.seek(0L);
                                    this.fiv.write(this.iv);
                                }
                                if (this.totalBytesCount > 0 && this.state == 1) {
                                    copyNotLoadedRanges();
                                    this.delegate.didChangedLoadProgress(this, this.downloadedBytes, this.totalBytesCount);
                                }
                            }
                            while (i < this.delayedRequestInfos.size()) {
                                RequestInfo requestInfo2 = this.delayedRequestInfos.get(i);
                                i = (this.notLoadedBytesRanges == null && this.downloadedBytes != requestInfo2.offset) ? i + 1 : 0;
                                this.delayedRequestInfos.remove(i);
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
                                if (z) {
                                    onFinishLoadingFile(true, 0);
                                } else if (this.state != 4) {
                                    startDownloadRequest();
                                }
                            }
                            if (z) {
                            }
                        }
                        onFinishLoadingFile(true, 0);
                        return false;
                    }
                    nativeByteBuffer = nativeByteBuffer2;
                    if (nativeByteBuffer != null) {
                        int limit3 = nativeByteBuffer.limit();
                        if (this.isCdn) {
                        }
                        if (requestInfo.responseCdn != null) {
                        }
                        if (!this.isPreloadVideoOperation) {
                        }
                        while (i < this.delayedRequestInfos.size()) {
                        }
                        if (z) {
                        }
                    }
                    onFinishLoadingFile(true, 0);
                    return false;
                }
                delayRequestInfo(requestInfo);
                return false;
            } catch (Exception e) {
                FileLog.e(e, (AndroidUtilities.isFilNotFoundException(e) || AndroidUtilities.isENOSPC(e)) ? false : true);
                if (AndroidUtilities.isENOSPC(e)) {
                    onFail(false, -1);
                } else if (AndroidUtilities.isEROFS(e)) {
                    SharedConfig.checkSdCard(this.cacheFileFinal);
                    onFail(true, -1);
                } else {
                    onFail(false, 0);
                }
            }
        } else if (tLRPC$TL_error.text.contains("LIMIT_INVALID") && !requestInfo.forceSmallChunk) {
            if (!this.forceSmallChunk) {
                this.forceSmallChunk = true;
                this.currentDownloadChunkSize = 0;
                pause();
                start();
            }
        } else if (tLRPC$TL_error.text.contains("FILE_MIGRATE_")) {
            Scanner scanner = new Scanner(tLRPC$TL_error.text.replace("FILE_MIGRATE_", ""));
            scanner.useDelimiter("");
            try {
                num = Integer.valueOf(scanner.nextInt());
            } catch (Exception unused) {
            }
            if (num == null) {
                onFail(false, 0);
            } else {
                this.datacenterId = num.intValue();
                this.downloadedBytes = 0L;
                this.requestedBytesCount = 0L;
                startDownloadRequest();
            }
        } else if (tLRPC$TL_error.text.contains("OFFSET_INVALID")) {
            if (this.downloadedBytes % this.currentDownloadChunkSize == 0) {
                try {
                    onFinishLoadingFile(true, 0);
                } catch (Exception e2) {
                    FileLog.e(e2);
                    onFail(false, 0);
                }
            } else {
                onFail(false, 0);
            }
        } else if (tLRPC$TL_error.text.contains("RETRY_LIMIT")) {
            onFail(false, 2);
        } else {
            if (BuildVars.LOGS_ENABLED) {
                if (this.location != null) {
                    FileLog.e(tLRPC$TL_error.text + " " + this.location + " id = " + this.location.id + " local_id = " + this.location.local_id + " access_hash = " + this.location.access_hash + " volume_id = " + this.location.volume_id + " secret = " + this.location.secret);
                } else if (this.webLocation != null) {
                    FileLog.e(tLRPC$TL_error.text + " " + this.webLocation + " id = " + this.fileName);
                }
            }
            onFail(false, 0);
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onFail(boolean z, final int i) {
        cleanup();
        this.state = i == 1 ? 4 : 2;
        if (this.delegate != null) {
            if (BuildVars.LOGS_ENABLED) {
                long currentTimeMillis = this.startTime != 0 ? System.currentTimeMillis() - this.startTime : 0L;
                if (i == 1) {
                    FileLog.d("cancel downloading file to " + this.cacheFileFinal + " time = " + currentTimeMillis + " dc = " + this.datacenterId + " size = " + AndroidUtilities.formatFileSize(this.totalBytesCount));
                } else {
                    FileLog.d("failed downloading file to " + this.cacheFileFinal + " reason = " + i + " time = " + currentTimeMillis + " dc = " + this.datacenterId + " size = " + AndroidUtilities.formatFileSize(this.totalBytesCount));
                }
            }
            if (z) {
                Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        FileLoadOperation.this.lambda$onFail$14(i);
                    }
                });
            } else {
                this.delegate.didFailedLoadingFile(this, i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFail$14(int i) {
        this.delegate.didFailedLoadingFile(this, i);
    }

    private void clearOperaion(RequestInfo requestInfo, boolean z) {
        long j = Long.MAX_VALUE;
        for (int i = 0; i < this.requestInfos.size(); i++) {
            RequestInfo requestInfo2 = this.requestInfos.get(i);
            j = Math.min(requestInfo2.offset, j);
            if (this.isPreloadVideoOperation) {
                this.requestedPreloadedBytesRanges.remove(Long.valueOf(requestInfo2.offset));
            } else {
                removePart(this.notRequestedBytesRanges, requestInfo2.offset, this.currentDownloadChunkSize + requestInfo2.offset);
            }
            if (requestInfo != requestInfo2 && requestInfo2.requestToken != 0) {
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(requestInfo2.requestToken, false);
            }
        }
        this.requestInfos.clear();
        for (int i2 = 0; i2 < this.delayedRequestInfos.size(); i2++) {
            RequestInfo requestInfo3 = this.delayedRequestInfos.get(i2);
            if (this.isPreloadVideoOperation) {
                this.requestedPreloadedBytesRanges.remove(Long.valueOf(requestInfo3.offset));
            } else {
                removePart(this.notRequestedBytesRanges, requestInfo3.offset, this.currentDownloadChunkSize + requestInfo3.offset);
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
            j = Math.min(requestInfo3.offset, j);
        }
        this.delayedRequestInfos.clear();
        this.requestsCount = 0;
        if (!z && this.isPreloadVideoOperation) {
            this.requestedBytesCount = this.totalPreloadedBytes;
        } else if (this.notLoadedBytesRanges == null) {
            this.downloadedBytes = j;
            this.requestedBytesCount = j;
        }
    }

    private void requestReference(RequestInfo requestInfo) {
        TLRPC$WebPage tLRPC$WebPage;
        if (this.requestingReference) {
            return;
        }
        clearOperaion(null, false);
        this.requestingReference = true;
        Object obj = this.parentObject;
        if (obj instanceof MessageObject) {
            MessageObject messageObject = (MessageObject) obj;
            if (messageObject.getId() < 0 && (tLRPC$WebPage = messageObject.messageOwner.media.webpage) != null) {
                this.parentObject = tLRPC$WebPage;
            }
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("debug_loading: " + this.cacheFileFinal.getName() + " file reference expired ");
        }
        FileRefController.getInstance(this.currentAccount).requestReference(this.parentObject, this.location, this, requestInfo);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r11v0 */
    /* JADX WARN: Type inference failed for: r11v1, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r11v7 */
    public void startDownloadRequest() {
        int i;
        long j;
        long j2;
        TLRPC$TL_upload_getFile tLRPC$TL_upload_getFile;
        HashMap<Long, PreloadRange> hashMap;
        PreloadRange preloadRange;
        ArrayList<Range> arrayList;
        boolean z;
        if (BuildVars.DEBUG_PRIVATE_VERSION && Utilities.stageQueue != null && Utilities.stageQueue.getHandler() != null && Thread.currentThread() != Utilities.stageQueue.getHandler().getLooper().getThread()) {
            throw new RuntimeException("Wrong thread!!!");
        }
        if (this.paused || this.reuploadingCdn) {
            return;
        }
        int i2 = 1;
        if (this.state != 1 || this.requestingReference) {
            return;
        }
        long j3 = 0;
        if (this.streamPriorityStartOffset == 0) {
            if (!this.nextPartWasPreloaded && this.requestInfos.size() + this.delayedRequestInfos.size() >= this.currentMaxDownloadRequests) {
                return;
            }
            if (this.isPreloadVideoOperation) {
                if (this.requestedBytesCount > 2097152) {
                    return;
                }
                if (this.moovFound != 0 && this.requestInfos.size() > 0) {
                    return;
                }
            }
        }
        ?? r11 = 0;
        int max = (this.streamPriorityStartOffset != 0 || this.nextPartWasPreloaded || (this.isPreloadVideoOperation && this.moovFound == 0) || this.totalBytesCount <= 0) ? 1 : Math.max(0, this.currentMaxDownloadRequests - this.requestInfos.size());
        int i3 = 0;
        while (i3 < max) {
            int i4 = 2;
            if (this.isPreloadVideoOperation) {
                if (this.moovFound != 0 && this.preloadNotRequestedBytesCount <= j3) {
                    return;
                }
                long j4 = this.nextPreloadDownloadOffset;
                if (j4 == -1) {
                    int i5 = (preloadMaxBytes / this.currentDownloadChunkSize) + 2;
                    long j5 = j3;
                    while (i5 != 0) {
                        if (!this.requestedPreloadedBytesRanges.containsKey(Long.valueOf(j5))) {
                            j4 = j5;
                            z = true;
                            break;
                        }
                        int i6 = this.currentDownloadChunkSize;
                        j5 += i6;
                        long j6 = this.totalBytesCount;
                        if (j5 > j6) {
                            break;
                        }
                        if (this.moovFound == i4 && j5 == i6 * 8) {
                            j5 = ((j6 - 1048576) / i6) * i6;
                        }
                        i5--;
                        i4 = 2;
                    }
                    j4 = j5;
                    z = false;
                    if (!z && this.requestInfos.isEmpty()) {
                        onFinishLoadingFile(r11, r11);
                    }
                }
                if (this.requestedPreloadedBytesRanges == null) {
                    this.requestedPreloadedBytesRanges = new HashMap<>();
                }
                this.requestedPreloadedBytesRanges.put(Long.valueOf(j4), Integer.valueOf(i2));
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d("start next preload from " + j4 + " size " + this.totalBytesCount + " for " + this.cacheFilePreload);
                }
                this.preloadNotRequestedBytesCount -= this.currentDownloadChunkSize;
                j2 = j4;
                i = max;
            } else {
                ArrayList<Range> arrayList2 = this.notRequestedBytesRanges;
                if (arrayList2 != null) {
                    long j7 = this.streamPriorityStartOffset;
                    if (j7 == j3) {
                        j7 = this.streamStartOffset;
                    }
                    int size = arrayList2.size();
                    long j8 = Long.MAX_VALUE;
                    i = max;
                    int i7 = 0;
                    long j9 = Long.MAX_VALUE;
                    while (true) {
                        if (i7 >= size) {
                            j7 = j8;
                            break;
                        }
                        Range range = this.notRequestedBytesRanges.get(i7);
                        if (j7 != j3) {
                            if (range.start <= j7 && range.end > j7) {
                                j9 = Long.MAX_VALUE;
                                break;
                            } else if (j7 < range.start && range.start < j8) {
                                j8 = range.start;
                            }
                        }
                        j9 = Math.min(j9, range.start);
                        i7++;
                        j3 = 0;
                    }
                    if (j7 != Long.MAX_VALUE) {
                        j = j7;
                    } else if (j9 == Long.MAX_VALUE) {
                        return;
                    } else {
                        j = j9;
                    }
                } else {
                    i = max;
                    j = this.requestedBytesCount;
                }
                j2 = j;
            }
            if (!this.isPreloadVideoOperation && (arrayList = this.notRequestedBytesRanges) != null) {
                addPart(arrayList, j2, j2 + this.currentDownloadChunkSize, false);
            }
            long j10 = this.totalBytesCount;
            if (j10 > 0 && j2 >= j10) {
                return;
            }
            boolean z2 = j10 <= 0 || i3 == i + (-1) || (j10 > 0 && ((long) this.currentDownloadChunkSize) + j2 >= j10);
            int i8 = this.requestsCount % 2 == 0 ? 2 : ConnectionsManager.ConnectionTypeDownload2;
            int i9 = this.isForceRequest ? 32 : 0;
            if (this.isCdn) {
                TLRPC$TL_upload_getCdnFile tLRPC$TL_upload_getCdnFile = new TLRPC$TL_upload_getCdnFile();
                tLRPC$TL_upload_getCdnFile.file_token = this.cdnToken;
                tLRPC$TL_upload_getCdnFile.offset = j2;
                tLRPC$TL_upload_getCdnFile.limit = this.currentDownloadChunkSize;
                i9 |= 1;
                tLRPC$TL_upload_getFile = tLRPC$TL_upload_getCdnFile;
            } else if (this.webLocation != null) {
                TLRPC$TL_upload_getWebFile tLRPC$TL_upload_getWebFile = new TLRPC$TL_upload_getWebFile();
                tLRPC$TL_upload_getWebFile.location = this.webLocation;
                tLRPC$TL_upload_getWebFile.offset = (int) j2;
                tLRPC$TL_upload_getWebFile.limit = this.currentDownloadChunkSize;
                tLRPC$TL_upload_getFile = tLRPC$TL_upload_getWebFile;
            } else {
                TLRPC$TL_upload_getFile tLRPC$TL_upload_getFile2 = new TLRPC$TL_upload_getFile();
                tLRPC$TL_upload_getFile2.location = this.location;
                tLRPC$TL_upload_getFile2.offset = j2;
                tLRPC$TL_upload_getFile2.limit = this.currentDownloadChunkSize;
                tLRPC$TL_upload_getFile2.cdn_supported = true;
                tLRPC$TL_upload_getFile = tLRPC$TL_upload_getFile2;
            }
            int i10 = i9;
            final TLRPC$TL_upload_getFile tLRPC$TL_upload_getFile3 = tLRPC$TL_upload_getFile;
            this.requestedBytesCount += this.currentDownloadChunkSize;
            final RequestInfo requestInfo = new RequestInfo();
            this.requestInfos.add(requestInfo);
            requestInfo.offset = j2;
            requestInfo.forceSmallChunk = this.forceSmallChunk;
            if (!this.isPreloadVideoOperation && this.supportsPreloading && this.preloadStream != null && (hashMap = this.preloadedBytesRanges) != null && (preloadRange = hashMap.get(Long.valueOf(requestInfo.offset))) != null) {
                requestInfo.response = new TLRPC$TL_upload_file();
                try {
                    if (BuildVars.DEBUG_VERSION && preloadRange.length > 2147483647L) {
                        throw new RuntimeException("cast long to integer");
                        break;
                    }
                    NativeByteBuffer nativeByteBuffer = new NativeByteBuffer((int) preloadRange.length);
                    this.preloadStream.seek(preloadRange.fileOffset);
                    this.preloadStream.getChannel().read(nativeByteBuffer.buffer);
                    try {
                        nativeByteBuffer.buffer.position(0);
                        requestInfo.response.bytes = nativeByteBuffer;
                        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda5
                            @Override // java.lang.Runnable
                            public final void run() {
                                FileLoadOperation.this.lambda$startDownloadRequest$15(requestInfo);
                            }
                        });
                        j3 = 0;
                    } catch (Exception unused) {
                    }
                } catch (Exception unused2) {
                }
                i3++;
                max = i;
                i2 = 1;
                r11 = 0;
            }
            if (this.streamPriorityStartOffset != 0) {
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d("frame get offset = " + this.streamPriorityStartOffset);
                }
                j3 = 0;
                this.streamPriorityStartOffset = 0L;
                this.priorityRequestInfo = requestInfo;
            } else {
                j3 = 0;
            }
            TLRPC$InputFileLocation tLRPC$InputFileLocation = this.location;
            if (!(tLRPC$InputFileLocation instanceof TLRPC$TL_inputPeerPhotoFileLocation) || ((TLRPC$TL_inputPeerPhotoFileLocation) tLRPC$InputFileLocation).photo_id != j3) {
                requestInfo.forceSmallChunk = this.forceSmallChunk;
                if (BuildVars.LOGS_ENABLED) {
                    requestInfo.requestStartTime = System.currentTimeMillis();
                }
                final int i11 = this.isCdn ? this.cdnDatacenterId : this.datacenterId;
                final int i12 = i8;
                requestInfo.requestToken = ConnectionsManager.getInstance(this.currentAccount).sendRequestSync(tLRPC$TL_upload_getFile3, new RequestDelegate() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda18
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        FileLoadOperation.this.lambda$startDownloadRequest$17(requestInfo, i11, i12, tLRPC$TL_upload_getFile3, tLObject, tLRPC$TL_error);
                    }
                }, null, null, i10, i11, i8, z2);
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("debug_loading: " + this.cacheFileFinal.getName() + " send reqId " + requestInfo.requestToken);
                }
                this.requestsCount++;
            } else {
                requestReference(requestInfo);
            }
            i3++;
            max = i;
            i2 = 1;
            r11 = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startDownloadRequest$15(RequestInfo requestInfo) {
        processRequestResult(requestInfo, null);
        requestInfo.response.freeResources();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startDownloadRequest$17(final RequestInfo requestInfo, int i, int i2, TLObject tLObject, TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error) {
        byte[] bArr;
        if (this.requestInfos.contains(requestInfo)) {
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
                if (FileRefController.isFileRefError(tLRPC$TL_error.text)) {
                    requestReference(requestInfo);
                    return;
                } else if ((tLObject instanceof TLRPC$TL_upload_getCdnFile) && tLRPC$TL_error.text.equals("FILE_TOKEN_INVALID")) {
                    this.isCdn = false;
                    clearOperaion(requestInfo, false);
                    startDownloadRequest();
                    return;
                }
            }
            if (tLObject2 instanceof TLRPC$TL_upload_fileCdnRedirect) {
                TLRPC$TL_upload_fileCdnRedirect tLRPC$TL_upload_fileCdnRedirect = (TLRPC$TL_upload_fileCdnRedirect) tLObject2;
                if (!tLRPC$TL_upload_fileCdnRedirect.file_hashes.isEmpty()) {
                    if (this.cdnHashes == null) {
                        this.cdnHashes = new HashMap<>();
                    }
                    for (int i3 = 0; i3 < tLRPC$TL_upload_fileCdnRedirect.file_hashes.size(); i3++) {
                        TLRPC$TL_fileHash tLRPC$TL_fileHash = tLRPC$TL_upload_fileCdnRedirect.file_hashes.get(i3);
                        this.cdnHashes.put(Long.valueOf(tLRPC$TL_fileHash.offset), tLRPC$TL_fileHash);
                    }
                }
                byte[] bArr2 = tLRPC$TL_upload_fileCdnRedirect.encryption_iv;
                if (bArr2 == null || (bArr = tLRPC$TL_upload_fileCdnRedirect.encryption_key) == null || bArr2.length != 16 || bArr.length != 32) {
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
                clearOperaion(requestInfo, false);
                startDownloadRequest();
            } else if (tLObject2 instanceof TLRPC$TL_upload_cdnFileReuploadNeeded) {
                if (this.reuploadingCdn) {
                    return;
                }
                clearOperaion(requestInfo, false);
                this.reuploadingCdn = true;
                TLRPC$TL_upload_reuploadCdnFile tLRPC$TL_upload_reuploadCdnFile = new TLRPC$TL_upload_reuploadCdnFile();
                tLRPC$TL_upload_reuploadCdnFile.file_token = this.cdnToken;
                tLRPC$TL_upload_reuploadCdnFile.request_token = ((TLRPC$TL_upload_cdnFileReuploadNeeded) tLObject2).request_token;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_upload_reuploadCdnFile, new RequestDelegate() { // from class: org.telegram.messenger.FileLoadOperation$$ExternalSyntheticLambda17
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject3, TLRPC$TL_error tLRPC$TL_error3) {
                        FileLoadOperation.this.lambda$startDownloadRequest$16(requestInfo, tLObject3, tLRPC$TL_error3);
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
                    int i4 = this.currentType;
                    if (i4 == 50331648) {
                        StatsController.getInstance(this.currentAccount).incrementReceivedBytesCount(tLObject2.networkType, 3, tLObject2.getObjectSize() + 4);
                    } else if (i4 == 33554432) {
                        StatsController.getInstance(this.currentAccount).incrementReceivedBytesCount(tLObject2.networkType, 2, tLObject2.getObjectSize() + 4);
                    } else if (i4 == 16777216) {
                        StatsController.getInstance(this.currentAccount).incrementReceivedBytesCount(tLObject2.networkType, 4, tLObject2.getObjectSize() + 4);
                    } else if (i4 == 67108864) {
                        String str = this.ext;
                        if (str != null && (str.toLowerCase().endsWith("mp3") || this.ext.toLowerCase().endsWith("m4a"))) {
                            StatsController.getInstance(this.currentAccount).incrementReceivedBytesCount(tLObject2.networkType, 7, tLObject2.getObjectSize() + 4);
                        } else {
                            StatsController.getInstance(this.currentAccount).incrementReceivedBytesCount(tLObject2.networkType, 5, tLObject2.getObjectSize() + 4);
                        }
                    }
                }
                processRequestResult(requestInfo, tLRPC$TL_error);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startDownloadRequest$16(RequestInfo requestInfo, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.reuploadingCdn = false;
        if (tLRPC$TL_error == null) {
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
            startDownloadRequest();
        } else if (tLRPC$TL_error.text.equals("FILE_TOKEN_INVALID") || tLRPC$TL_error.text.equals("REQUEST_TOKEN_INVALID")) {
            this.isCdn = false;
            clearOperaion(requestInfo, false);
            startDownloadRequest();
        } else {
            onFail(false, 0);
        }
    }

    public void setDelegate(FileLoadOperationDelegate fileLoadOperationDelegate) {
        this.delegate = fileLoadOperationDelegate;
    }
}
