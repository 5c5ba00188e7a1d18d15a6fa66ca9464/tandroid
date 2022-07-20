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
import org.telegram.messenger.FilePathDatabase;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
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
import org.telegram.tgnet.TLRPC$TL_inputEncryptedFileLocation;
import org.telegram.tgnet.TLRPC$TL_inputFileLocation;
import org.telegram.tgnet.TLRPC$TL_inputPeerPhotoFileLocation;
import org.telegram.tgnet.TLRPC$TL_inputPhotoFileLocation;
import org.telegram.tgnet.TLRPC$TL_inputSecureFileLocation;
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
/* loaded from: classes.dex */
public class FileLoadOperation {
    private static final Object lockObject = new Object();
    private static final int preloadMaxBytes = 2097152;
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
    private int currentAccount;
    private int currentDownloadChunkSize;
    private int currentMaxDownloadRequests;
    private int currentQueueType;
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
    private String fileName;
    private RandomAccessFile fileOutputStream;
    private RandomAccessFile filePartsStream;
    private RandomAccessFile fileReadStream;
    private RandomAccessFile fiv;
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
    private boolean preloadFinished;
    private long preloadNotRequestedBytesCount;
    private RandomAccessFile preloadStream;
    private int preloadStreamFileOffset;
    private byte[] preloadTempBuffer;
    private int preloadTempBufferCount;
    private HashMap<Long, PreloadRange> preloadedBytesRanges;
    private int priority;
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
    private ArrayList<FileLoadOperationStream> streamListeners;
    private long streamPriorityStartOffset;
    private long streamStartOffset;
    private boolean supportsPreloading;
    private File tempPath;
    private long totalBytesCount;
    private int totalPreloadedBytes;
    private boolean ungzip;
    private WebFile webFile;
    private TLRPC$InputWebFileLocation webLocation;

    /* loaded from: classes.dex */
    public interface FileLoadOperationDelegate {
        void didChangedLoadProgress(FileLoadOperation fileLoadOperation, long j, long j2);

        void didFailedLoadingFile(FileLoadOperation fileLoadOperation, int i);

        void didFinishLoadingFile(FileLoadOperation fileLoadOperation, File file);

        boolean hasAnotherRefOnFile(String str);

        void saveFilePath(FilePathDatabase.PathData pathData, File file);
    }

    /* loaded from: classes.dex */
    public static class RequestInfo {
        private long offset;
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
        if (MessagesController.getInstance(this.currentAccount).getfileExperimentalParams) {
            this.downloadChunkSizeBig = 524288;
            this.maxDownloadRequests = 8;
            this.maxDownloadRequestsBig = 8;
        } else {
            this.downloadChunkSizeBig = 131072;
            this.maxDownloadRequests = 4;
            this.maxDownloadRequestsBig = 4;
        }
        this.maxCdnParts = (int) (2097152000 / this.downloadChunkSizeBig);
    }

    public FileLoadOperation(ImageLocation imageLocation, Object obj, String str, long j) {
        this.downloadChunkSize = 32768;
        this.downloadChunkSizeBig = 131072;
        this.cdnChunkCheckSize = 131072;
        this.maxDownloadRequests = 4;
        this.maxDownloadRequestsBig = 4;
        this.bigFileSizeFrom = 10485760;
        this.maxCdnParts = (int) (2097152000 / 131072);
        this.downloadChunkSizeAnimation = 131072;
        this.maxDownloadRequestsAnimation = 4;
        this.preloadTempBuffer = new byte[24];
        boolean z = false;
        this.state = 0;
        updateParams();
        this.parentObject = obj;
        this.isStream = imageLocation.imageType == 2;
        if (imageLocation.isEncrypted()) {
            TLRPC$TL_inputEncryptedFileLocation tLRPC$TL_inputEncryptedFileLocation = new TLRPC$TL_inputEncryptedFileLocation();
            this.location = tLRPC$TL_inputEncryptedFileLocation;
            TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated = imageLocation.location;
            long j2 = tLRPC$TL_fileLocationToBeDeprecated.volume_id;
            tLRPC$TL_inputEncryptedFileLocation.id = j2;
            tLRPC$TL_inputEncryptedFileLocation.volume_id = j2;
            tLRPC$TL_inputEncryptedFileLocation.local_id = tLRPC$TL_fileLocationToBeDeprecated.local_id;
            tLRPC$TL_inputEncryptedFileLocation.access_hash = imageLocation.access_hash;
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
            TLRPC$InputFileLocation tLRPC$InputFileLocation = this.location;
            if (tLRPC$InputFileLocation.file_reference == null) {
                tLRPC$InputFileLocation.file_reference = new byte[0];
            }
        } else {
            TLRPC$TL_inputFileLocation tLRPC$TL_inputFileLocation = new TLRPC$TL_inputFileLocation();
            this.location = tLRPC$TL_inputFileLocation;
            TLRPC$TL_fileLocationToBeDeprecated tLRPC$TL_fileLocationToBeDeprecated6 = imageLocation.location;
            tLRPC$TL_inputFileLocation.volume_id = tLRPC$TL_fileLocationToBeDeprecated6.volume_id;
            tLRPC$TL_inputFileLocation.local_id = tLRPC$TL_fileLocationToBeDeprecated6.local_id;
            tLRPC$TL_inputFileLocation.secret = imageLocation.access_hash;
            byte[] bArr2 = imageLocation.file_reference;
            tLRPC$TL_inputFileLocation.file_reference = bArr2;
            if (bArr2 == null) {
                tLRPC$TL_inputFileLocation.file_reference = new byte[0];
            }
            this.allowDisordererFileSave = true;
        }
        int i = imageLocation.imageType;
        this.ungzip = (i == 1 || i == 3) ? true : z;
        int i2 = imageLocation.dc_id;
        this.datacenterId = i2;
        this.initialDatacenterId = i2;
        this.currentType = 16777216;
        this.totalBytesCount = j;
        this.ext = str == null ? "jpg" : str;
    }

    public FileLoadOperation(SecureDocument secureDocument) {
        this.downloadChunkSize = 32768;
        this.downloadChunkSizeBig = 131072;
        this.cdnChunkCheckSize = 131072;
        this.maxDownloadRequests = 4;
        this.maxDownloadRequestsBig = 4;
        this.bigFileSizeFrom = 10485760;
        this.maxCdnParts = (int) (2097152000 / 131072);
        this.downloadChunkSizeAnimation = 131072;
        this.maxDownloadRequestsAnimation = 4;
        this.preloadTempBuffer = new byte[24];
        this.state = 0;
        updateParams();
        TLRPC$TL_inputSecureFileLocation tLRPC$TL_inputSecureFileLocation = new TLRPC$TL_inputSecureFileLocation();
        this.location = tLRPC$TL_inputSecureFileLocation;
        TLRPC$TL_secureFile tLRPC$TL_secureFile = secureDocument.secureFile;
        tLRPC$TL_inputSecureFileLocation.id = tLRPC$TL_secureFile.id;
        tLRPC$TL_inputSecureFileLocation.access_hash = tLRPC$TL_secureFile.access_hash;
        this.datacenterId = tLRPC$TL_secureFile.dc_id;
        this.totalBytesCount = tLRPC$TL_secureFile.size;
        this.allowDisordererFileSave = true;
        this.currentType = 67108864;
        this.ext = ".jpg";
    }

    public FileLoadOperation(int i, WebFile webFile) {
        this.downloadChunkSize = 32768;
        this.downloadChunkSizeBig = 131072;
        this.cdnChunkCheckSize = 131072;
        this.maxDownloadRequests = 4;
        this.maxDownloadRequestsBig = 4;
        this.bigFileSizeFrom = 10485760;
        this.maxCdnParts = (int) (2097152000 / 131072);
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
            this.currentType = 16777216;
        } else if (webFile.mime_type.equals("audio/ogg")) {
            this.currentType = 50331648;
        } else if (webFile.mime_type.startsWith("video/")) {
            this.currentType = 33554432;
        } else {
            this.currentType = 67108864;
        }
        this.allowDisordererFileSave = true;
        this.ext = ImageLoader.getHttpUrlExtension(webFile.url, mimeTypePart);
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x00f9 A[Catch: Exception -> 0x0120, TryCatch #0 {Exception -> 0x0120, blocks: (B:3:0x0030, B:6:0x0038, B:7:0x005e, B:9:0x0062, B:11:0x0079, B:12:0x007d, B:14:0x008e, B:16:0x0098, B:17:0x009b, B:18:0x009e, B:20:0x00a8, B:25:0x00b6, B:27:0x00c0, B:29:0x00ca, B:30:0x00d2, B:32:0x00da, B:35:0x00e4, B:36:0x00ed, B:37:0x00ef, B:39:0x00f9, B:40:0x00fe, B:42:0x0106, B:43:0x010b, B:44:0x010f, B:46:0x0117), top: B:50:0x0030 }] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00fe A[Catch: Exception -> 0x0120, TryCatch #0 {Exception -> 0x0120, blocks: (B:3:0x0030, B:6:0x0038, B:7:0x005e, B:9:0x0062, B:11:0x0079, B:12:0x007d, B:14:0x008e, B:16:0x0098, B:17:0x009b, B:18:0x009e, B:20:0x00a8, B:25:0x00b6, B:27:0x00c0, B:29:0x00ca, B:30:0x00d2, B:32:0x00da, B:35:0x00e4, B:36:0x00ed, B:37:0x00ef, B:39:0x00f9, B:40:0x00fe, B:42:0x0106, B:43:0x010b, B:44:0x010f, B:46:0x0117), top: B:50:0x0030 }] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0117 A[Catch: Exception -> 0x0120, TRY_LEAVE, TryCatch #0 {Exception -> 0x0120, blocks: (B:3:0x0030, B:6:0x0038, B:7:0x005e, B:9:0x0062, B:11:0x0079, B:12:0x007d, B:14:0x008e, B:16:0x0098, B:17:0x009b, B:18:0x009e, B:20:0x00a8, B:25:0x00b6, B:27:0x00c0, B:29:0x00ca, B:30:0x00d2, B:32:0x00da, B:35:0x00e4, B:36:0x00ed, B:37:0x00ef, B:39:0x00f9, B:40:0x00fe, B:42:0x0106, B:43:0x010b, B:44:0x010f, B:46:0x0117), top: B:50:0x0030 }] */
    /* JADX WARN: Removed duplicated region for block: B:54:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public FileLoadOperation(TLRPC$Document tLRPC$Document, Object obj) {
        boolean z;
        long j;
        String documentFileName;
        int lastIndexOf;
        this.downloadChunkSize = 32768;
        this.downloadChunkSizeBig = 131072;
        this.cdnChunkCheckSize = 131072;
        this.maxDownloadRequests = 4;
        this.maxDownloadRequestsBig = 4;
        this.bigFileSizeFrom = 10485760;
        this.maxCdnParts = (int) (2097152000 / 131072);
        this.downloadChunkSizeAnimation = 131072;
        this.maxDownloadRequestsAnimation = 4;
        this.preloadTempBuffer = new byte[24];
        this.state = 0;
        updateParams();
        try {
            this.parentObject = obj;
            if (tLRPC$Document instanceof TLRPC$TL_documentEncrypted) {
                TLRPC$TL_inputEncryptedFileLocation tLRPC$TL_inputEncryptedFileLocation = new TLRPC$TL_inputEncryptedFileLocation();
                this.location = tLRPC$TL_inputEncryptedFileLocation;
                tLRPC$TL_inputEncryptedFileLocation.id = tLRPC$Document.id;
                tLRPC$TL_inputEncryptedFileLocation.access_hash = tLRPC$Document.access_hash;
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
                        this.currentType = 50331648;
                    } else if (FileLoader.isVideoMimeType(tLRPC$Document.mime_type)) {
                        this.currentType = 33554432;
                    } else {
                        this.currentType = 67108864;
                    }
                    if (this.ext.length() <= 1) {
                        return;
                    }
                    this.ext = FileLoader.getExtensionByMimeType(tLRPC$Document.mime_type);
                    return;
                }
                this.ext = "";
                if (!"audio/ogg".equals(tLRPC$Document.mime_type)) {
                }
                if (this.ext.length() <= 1) {
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
                if (this.ext.length() <= 1) {
                }
            }
            this.ext = "";
            if (!"audio/ogg".equals(tLRPC$Document.mime_type)) {
            }
            if (this.ext.length() <= 1) {
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

    public void setPaths(int i, String str, int i2, File file, File file2, String str2) {
        this.storePath = file;
        this.tempPath = file2;
        this.currentAccount = i;
        this.fileName = str;
        this.storeFileName = str2;
        this.currentQueueType = i2;
    }

    public int getQueueType() {
        return this.currentQueueType;
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
        Collections.sort(arrayList, FileLoadOperation$$ExternalSyntheticLambda11.INSTANCE);
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

    public static /* synthetic */ int lambda$removePart$0(Range range, Range range2) {
        if (range.start > range2.start) {
            return 1;
        }
        return range.start < range2.start ? -1 : 0;
    }

    private void addPart(ArrayList<Range> arrayList, long j, long j2, boolean z) {
        boolean z2;
        if (arrayList == null || j2 < j) {
            return;
        }
        int size = arrayList.size();
        int i = 0;
        while (true) {
            z2 = true;
            if (i >= size) {
                z2 = false;
                break;
            }
            Range range = arrayList.get(i);
            if (j <= range.start) {
                if (j2 < range.end) {
                    if (j2 > range.start) {
                        range.start = j2;
                        break;
                    }
                    i++;
                } else {
                    arrayList.remove(i);
                    break;
                }
            } else if (j2 >= range.end) {
                if (j < range.end) {
                    range.end = j;
                    break;
                }
                i++;
            } else {
                arrayList.add(0, new Range(range.start, j));
                range.start = j2;
                break;
            }
        }
        if (!z) {
            return;
        }
        if (z2) {
            try {
                this.filePartsStream.seek(0L);
                int size2 = arrayList.size();
                this.filePartsStream.writeInt(size2);
                for (int i2 = 0; i2 < size2; i2++) {
                    Range range2 = arrayList.get(i2);
                    this.filePartsStream.writeLong(range2.start);
                    this.filePartsStream.writeLong(range2.end);
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
            notifyStreamListeners();
        } else if (BuildVars.LOGS_ENABLED) {
            FileLog.e(this.cacheFileFinal + " downloaded duplicate file part " + j + " - " + j2);
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

    public File getCacheFileFinal() {
        return this.cacheFileFinal;
    }

    public File getCurrentFile() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        File[] fileArr = new File[1];
        Utilities.stageQueue.postRunnable(new FileLoadOperation$$ExternalSyntheticLambda9(this, fileArr, countDownLatch));
        try {
            countDownLatch.await();
        } catch (Exception e) {
            FileLog.e(e);
        }
        return fileArr[0];
    }

    public /* synthetic */ void lambda$getCurrentFile$1(File[] fileArr, CountDownLatch countDownLatch) {
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
            if (j4 != 0) {
                return Math.min(j2, Math.max(j4 - j, 0L));
            }
            return 0L;
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

    public float getDownloadedLengthFromOffset(float f) {
        ArrayList<Range> arrayList = this.notLoadedBytesRangesCopy;
        long j = this.totalBytesCount;
        if (j == 0 || arrayList == null) {
            return 0.0f;
        }
        return f + (((float) getDownloadedLengthFromOffsetInternal(arrayList, (int) (((float) j) * f), j)) / ((float) this.totalBytesCount));
    }

    public long[] getDownloadedLengthFromOffset(int i, long j) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        long[] jArr = new long[2];
        Utilities.stageQueue.postRunnable(new FileLoadOperation$$ExternalSyntheticLambda8(this, jArr, i, j, countDownLatch));
        try {
            countDownLatch.await();
        } catch (Exception unused) {
        }
        return jArr;
    }

    public /* synthetic */ void lambda$getDownloadedLengthFromOffset$2(long[] jArr, int i, long j, CountDownLatch countDownLatch) {
        jArr[0] = getDownloadedLengthFromOffsetInternal(this.notLoadedBytesRanges, i, j);
        if (this.state == 3) {
            jArr[1] = 1;
        }
        countDownLatch.countDown();
    }

    public String getFileName() {
        return this.fileName;
    }

    public void removeStreamListener(FileLoadOperationStream fileLoadOperationStream) {
        Utilities.stageQueue.postRunnable(new FileLoadOperation$$ExternalSyntheticLambda3(this, fileLoadOperationStream));
    }

    public /* synthetic */ void lambda$removeStreamListener$3(FileLoadOperationStream fileLoadOperationStream) {
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
        return start(null, 0L, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:116:0x03f1, code lost:
        if (r6 != r30.cacheFileFinal.length()) goto L117;
     */
    /* JADX WARN: Removed duplicated region for block: B:104:0x0394  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x03ba  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x03db  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0409  */
    /* JADX WARN: Removed duplicated region for block: B:205:0x05e5  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x060f  */
    /* JADX WARN: Removed duplicated region for block: B:233:0x0681  */
    /* JADX WARN: Removed duplicated region for block: B:236:0x068b  */
    /* JADX WARN: Removed duplicated region for block: B:249:0x06e4  */
    /* JADX WARN: Removed duplicated region for block: B:256:0x070f  */
    /* JADX WARN: Removed duplicated region for block: B:262:0x073b  */
    /* JADX WARN: Removed duplicated region for block: B:267:0x0778  */
    /* JADX WARN: Removed duplicated region for block: B:282:0x07c8  */
    /* JADX WARN: Removed duplicated region for block: B:290:0x07eb A[Catch: Exception -> 0x07f0, TRY_LEAVE, TryCatch #0 {Exception -> 0x07f0, blocks: (B:288:0x07da, B:290:0x07eb), top: B:308:0x07da }] */
    /* JADX WARN: Removed duplicated region for block: B:296:0x07f9  */
    /* JADX WARN: Removed duplicated region for block: B:298:0x07fe  */
    /* JADX WARN: Removed duplicated region for block: B:299:0x080c  */
    /* JADX WARN: Removed duplicated region for block: B:314:0x05f0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean start(FileLoadOperationStream fileLoadOperationStream, long j, boolean z) {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        Object obj;
        boolean exists;
        boolean z2;
        int i;
        String str6;
        boolean z3;
        String str7;
        ArrayList<Range> arrayList;
        long j2;
        boolean z4;
        long j3;
        Exception e;
        RandomAccessFile randomAccessFile;
        Exception e2;
        Exception e3;
        RandomAccessFile randomAccessFile2;
        String str8;
        String str9;
        this.startTime = System.currentTimeMillis();
        updateParams();
        if (this.currentDownloadChunkSize == 0) {
            boolean z5 = this.isStream;
            if (z5) {
                this.currentDownloadChunkSize = this.downloadChunkSizeAnimation;
                this.currentMaxDownloadRequests = this.maxDownloadRequestsAnimation;
            }
            long j4 = this.totalBytesCount;
            int i2 = this.bigFileSizeFrom;
            this.currentDownloadChunkSize = (j4 >= ((long) i2) || z5) ? this.downloadChunkSizeBig : this.downloadChunkSize;
            this.currentMaxDownloadRequests = (j4 >= ((long) i2) || z5) ? this.maxDownloadRequestsBig : this.maxDownloadRequests;
        }
        boolean z6 = this.state != 0;
        boolean z7 = this.paused;
        this.paused = false;
        if (fileLoadOperationStream != null) {
            Utilities.stageQueue.postRunnable(new FileLoadOperation$$ExternalSyntheticLambda7(this, z, j, fileLoadOperationStream, z6));
        } else if (z7 && z6) {
            Utilities.stageQueue.postRunnable(new FileLoadOperation$$ExternalSyntheticLambda0(this));
        }
        if (z6) {
            return z7;
        }
        if (this.location == null && this.webLocation == null) {
            onFail(true, 0);
            return false;
        }
        int i3 = this.currentDownloadChunkSize;
        this.streamStartOffset = (j / i3) * i3;
        if (this.allowDisordererFileSave) {
            long j5 = this.totalBytesCount;
            if (j5 > 0 && j5 > i3) {
                this.notLoadedBytesRanges = new ArrayList<>();
                this.notRequestedBytesRanges = new ArrayList<>();
            }
        }
        if (this.webLocation != null) {
            String MD5 = Utilities.MD5(this.webFile.url);
            if (this.encryptFile) {
                str2 = MD5 + ".temp.enc";
                str = MD5 + "." + this.ext + ".enc";
                if (this.key != null) {
                    str9 = MD5 + "_64.iv.enc";
                }
                str9 = null;
            } else {
                String str10 = MD5 + ".temp";
                str = MD5 + "." + this.ext;
                if (this.key != null) {
                    str9 = MD5 + "_64.iv";
                    str2 = str10;
                } else {
                    str2 = str10;
                    str9 = null;
                }
            }
            str4 = null;
            str3 = null;
            this.requestInfos = new ArrayList<>(this.currentMaxDownloadRequests);
            this.delayedRequestInfos = new ArrayList<>(this.currentMaxDownloadRequests - 1);
            this.state = 1;
            if (!(this.parentObject instanceof TLRPC$TL_theme)) {
                this.cacheFileFinal = new File(ApplicationLoader.getFilesDirFixed(), "remote" + ((TLRPC$TL_theme) obj).id + ".attheme");
            } else if (!this.encryptFile) {
                this.cacheFileFinal = new File(this.storePath, this.storeFileName);
            } else {
                this.cacheFileFinal = new File(this.storePath, str);
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
                this.cacheFileTemp = new File(this.tempPath, str2);
                if (this.ungzip) {
                    this.cacheFileGzipTemp = new File(this.tempPath, str2 + ".gz");
                }
                String str11 = "rws";
                if (this.encryptFile) {
                    File file = new File(FileLoader.getInternalCacheDir(), str + ".key");
                    try {
                        randomAccessFile2 = new RandomAccessFile(file, str11);
                        long length = file.length();
                        byte[] bArr = new byte[32];
                        this.encryptKey = bArr;
                        this.encryptIv = new byte[16];
                        if (length > 0 && length % 48 == 0) {
                            randomAccessFile2.read(bArr, 0, 32);
                            randomAccessFile2.read(this.encryptIv, 0, 16);
                            z2 = false;
                        } else {
                            Utilities.random.nextBytes(bArr);
                            Utilities.random.nextBytes(this.encryptIv);
                            randomAccessFile2.write(this.encryptKey);
                            randomAccessFile2.write(this.encryptIv);
                            z2 = true;
                        }
                    } catch (Exception e4) {
                        e3 = e4;
                        z2 = false;
                    }
                    try {
                        try {
                            randomAccessFile2.getChannel().close();
                        } catch (Exception e5) {
                            FileLog.e(e5);
                        }
                        randomAccessFile2.close();
                    } catch (Exception e6) {
                        e3 = e6;
                        FileLog.e(e3);
                        i = 1;
                        boolean[] zArr = new boolean[i];
                        zArr[0] = false;
                        long j7 = 8;
                        if (this.supportsPreloading) {
                        }
                        str6 = str11;
                        z3 = z2;
                        if (str4 == null) {
                        }
                        if (!this.cacheFileTemp.exists()) {
                        }
                        arrayList = this.notLoadedBytesRanges;
                        if (arrayList != null) {
                        }
                        if (BuildVars.LOGS_ENABLED) {
                        }
                        if (str5 != null) {
                        }
                        j2 = 0;
                        if (!this.isPreloadVideoOperation) {
                        }
                        updateProgress();
                        RandomAccessFile randomAccessFile3 = new RandomAccessFile(this.cacheFileTemp, str7);
                        this.fileOutputStream = randomAccessFile3;
                        j3 = this.downloadedBytes;
                        if (j3 != 0) {
                        }
                        z4 = false;
                        if (this.fileOutputStream == null) {
                        }
                    }
                    i = 1;
                } else {
                    i = 1;
                    z2 = false;
                }
                boolean[] zArr2 = new boolean[i];
                zArr2[0] = false;
                long j72 = 8;
                if (this.supportsPreloading || str3 == null) {
                    str6 = str11;
                    z3 = z2;
                } else {
                    this.cacheFilePreload = new File(this.tempPath, str3);
                    try {
                        RandomAccessFile randomAccessFile4 = new RandomAccessFile(this.cacheFilePreload, str11);
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
                                if (length2 - j9 < j72 || readLong < 0 || readLong > this.totalBytesCount) {
                                    break;
                                }
                                long readLong2 = this.preloadStream.readLong();
                                long j10 = j9 + j72;
                                if (length2 - j10 < readLong2 || readLong2 > this.currentDownloadChunkSize) {
                                    break;
                                }
                                PreloadRange preloadRange = new PreloadRange(j10, readLong2);
                                long j11 = j10 + readLong2;
                                this.preloadStream.seek(j11);
                                if (length2 - j11 < 24) {
                                    break;
                                }
                                long readLong3 = this.preloadStream.readLong();
                                this.foundMoovSize = readLong3;
                                if (readLong3 != 0) {
                                    str6 = str11;
                                    try {
                                        z3 = z2;
                                    } catch (Exception e7) {
                                        e2 = e7;
                                        z3 = z2;
                                        FileLog.e(e2);
                                        if (!this.isPreloadVideoOperation) {
                                            this.cacheFilePreload = null;
                                            try {
                                                randomAccessFile = this.preloadStream;
                                                if (randomAccessFile != null) {
                                                }
                                            } catch (Exception e8) {
                                                FileLog.e(e8);
                                            }
                                        }
                                        if (str4 == null) {
                                        }
                                        if (!this.cacheFileTemp.exists()) {
                                        }
                                        arrayList = this.notLoadedBytesRanges;
                                        if (arrayList != null) {
                                        }
                                        if (BuildVars.LOGS_ENABLED) {
                                        }
                                        if (str5 != null) {
                                        }
                                        j2 = 0;
                                        if (!this.isPreloadVideoOperation) {
                                            copyNotLoadedRanges();
                                        }
                                        updateProgress();
                                        RandomAccessFile randomAccessFile32 = new RandomAccessFile(this.cacheFileTemp, str7);
                                        this.fileOutputStream = randomAccessFile32;
                                        j3 = this.downloadedBytes;
                                        if (j3 != 0) {
                                        }
                                        z4 = false;
                                        if (this.fileOutputStream == null) {
                                        }
                                    }
                                    try {
                                        this.moovFound = this.nextPreloadDownloadOffset > this.totalBytesCount / 2 ? 2 : 1;
                                        this.preloadNotRequestedBytesCount = readLong3;
                                    } catch (Exception e9) {
                                        e2 = e9;
                                        FileLog.e(e2);
                                        if (!this.isPreloadVideoOperation) {
                                        }
                                        if (str4 == null) {
                                        }
                                        if (!this.cacheFileTemp.exists()) {
                                        }
                                        arrayList = this.notLoadedBytesRanges;
                                        if (arrayList != null) {
                                        }
                                        if (BuildVars.LOGS_ENABLED) {
                                        }
                                        if (str5 != null) {
                                        }
                                        j2 = 0;
                                        if (!this.isPreloadVideoOperation) {
                                        }
                                        updateProgress();
                                        RandomAccessFile randomAccessFile322 = new RandomAccessFile(this.cacheFileTemp, str7);
                                        this.fileOutputStream = randomAccessFile322;
                                        j3 = this.downloadedBytes;
                                        if (j3 != 0) {
                                        }
                                        z4 = false;
                                        if (this.fileOutputStream == null) {
                                        }
                                    }
                                } else {
                                    str6 = str11;
                                    z3 = z2;
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
                                z2 = z3;
                                j72 = 8;
                                str11 = str6;
                            }
                        }
                        str6 = str11;
                        z3 = z2;
                        this.preloadStream.seek(this.preloadStreamFileOffset);
                    } catch (Exception e10) {
                        e2 = e10;
                        str6 = str11;
                    }
                    if (!this.isPreloadVideoOperation && this.preloadedBytesRanges == null) {
                        this.cacheFilePreload = null;
                        randomAccessFile = this.preloadStream;
                        if (randomAccessFile != null) {
                            try {
                                randomAccessFile.getChannel().close();
                            } catch (Exception e11) {
                                FileLog.e(e11);
                            }
                            this.preloadStream.close();
                            this.preloadStream = null;
                        }
                    }
                }
                if (str4 == null) {
                    this.cacheFileParts = new File(this.tempPath, str4);
                    try {
                        str7 = str6;
                        try {
                            RandomAccessFile randomAccessFile5 = new RandomAccessFile(this.cacheFileParts, str7);
                            this.filePartsStream = randomAccessFile5;
                            long length3 = randomAccessFile5.length();
                            if (length3 % 8 == 4) {
                                int readInt = this.filePartsStream.readInt();
                                if (readInt <= (length3 - 4) / 2) {
                                    int i4 = 0;
                                    while (i4 < readInt) {
                                        long readLong4 = this.filePartsStream.readLong();
                                        long readLong5 = this.filePartsStream.readLong();
                                        int i5 = readInt;
                                        this.notLoadedBytesRanges.add(new Range(readLong4, readLong5));
                                        this.notRequestedBytesRanges.add(new Range(readLong4, readLong5));
                                        i4++;
                                        readInt = i5;
                                    }
                                }
                            }
                        } catch (Exception e12) {
                            e = e12;
                            FileLog.e(e);
                            if (!this.cacheFileTemp.exists()) {
                            }
                            arrayList = this.notLoadedBytesRanges;
                            if (arrayList != null) {
                            }
                            if (BuildVars.LOGS_ENABLED) {
                            }
                            if (str5 != null) {
                            }
                            j2 = 0;
                            if (!this.isPreloadVideoOperation) {
                            }
                            updateProgress();
                            RandomAccessFile randomAccessFile3222 = new RandomAccessFile(this.cacheFileTemp, str7);
                            this.fileOutputStream = randomAccessFile3222;
                            j3 = this.downloadedBytes;
                            if (j3 != 0) {
                            }
                            z4 = false;
                            if (this.fileOutputStream == null) {
                            }
                        }
                    } catch (Exception e13) {
                        e = e13;
                        str7 = str6;
                    }
                } else {
                    str7 = str6;
                }
                if (!this.cacheFileTemp.exists()) {
                    ArrayList<Range> arrayList2 = this.notLoadedBytesRanges;
                    if (arrayList2 != null && arrayList2.isEmpty()) {
                        this.notLoadedBytesRanges.add(new Range(0L, this.totalBytesCount));
                        this.notRequestedBytesRanges.add(new Range(0L, this.totalBytesCount));
                    }
                } else if (z3) {
                    this.cacheFileTemp.delete();
                } else {
                    long length4 = this.cacheFileTemp.length();
                    if (str5 != null && length4 % this.currentDownloadChunkSize != 0) {
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
                        FileLog.d("start loading file to temp = " + this.cacheFileTemp + " final = " + this.cacheFileFinal);
                    }
                }
                if (str5 != null) {
                    this.cacheIvTemp = new File(this.tempPath, str5);
                    try {
                        this.fiv = new RandomAccessFile(this.cacheIvTemp, str7);
                        if (this.downloadedBytes != 0 && !z3) {
                            long length6 = this.cacheIvTemp.length();
                            if (length6 > 0 && length6 % 64 == 0) {
                                this.fiv.read(this.iv, 0, 64);
                            } else {
                                this.downloadedBytes = 0L;
                                this.requestedBytesCount = 0L;
                            }
                        }
                    } catch (Exception e14) {
                        FileLog.e(e14);
                        j2 = 0;
                        this.downloadedBytes = 0L;
                        this.requestedBytesCount = 0L;
                    }
                }
                j2 = 0;
                if (!this.isPreloadVideoOperation && this.downloadedBytes != j2 && this.totalBytesCount > j2) {
                    copyNotLoadedRanges();
                }
                updateProgress();
                try {
                    RandomAccessFile randomAccessFile32222 = new RandomAccessFile(this.cacheFileTemp, str7);
                    this.fileOutputStream = randomAccessFile32222;
                    j3 = this.downloadedBytes;
                    if (j3 != 0) {
                        randomAccessFile32222.seek(j3);
                    }
                    z4 = false;
                } catch (Exception e15) {
                    z4 = false;
                    FileLog.e((Throwable) e15, false);
                }
                if (this.fileOutputStream == null) {
                    int i8 = z4 ? 1 : 0;
                    int i9 = z4 ? 1 : 0;
                    onFail(true, i8);
                    return z4;
                }
                this.started = true;
                Utilities.stageQueue.postRunnable(new FileLoadOperation$$ExternalSyntheticLambda10(this, zArr2));
            } else {
                this.started = true;
                try {
                    onFinishLoadingFile(false);
                    FilePathDatabase.PathData pathData = this.pathSaveData;
                    if (pathData != null) {
                        this.delegate.saveFilePath(pathData, null);
                    }
                } catch (Exception unused) {
                    onFail(true, 0);
                    return true;
                }
            }
            return true;
        }
        TLRPC$InputFileLocation tLRPC$InputFileLocation = this.location;
        long j14 = tLRPC$InputFileLocation.volume_id;
        if (j14 != 0 && tLRPC$InputFileLocation.local_id != 0) {
            int i10 = this.datacenterId;
            if (i10 == Integer.MIN_VALUE || j14 == -2147483648L || i10 == 0) {
                onFail(true, 0);
                return false;
            } else if (this.encryptFile) {
                str2 = this.location.volume_id + "_" + this.location.local_id + ".temp.enc";
                str = this.location.volume_id + "_" + this.location.local_id + "." + this.ext + ".enc";
                if (this.key != null) {
                    str9 = this.location.volume_id + "_" + this.location.local_id + "_64.iv.enc";
                }
                str5 = null;
                str4 = null;
                str3 = null;
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
                str8 = this.location.volume_id + "_" + this.location.local_id + ".temp";
                str = this.location.volume_id + "_" + this.location.local_id + "." + this.ext;
                str5 = this.key != null ? this.location.volume_id + "_" + this.location.local_id + "_64.iv" : null;
                str4 = this.notLoadedBytesRanges != null ? this.location.volume_id + "_" + this.location.local_id + "_64.pt" : null;
                str3 = this.location.volume_id + "_" + this.location.local_id + "_64.preload";
                str2 = str8;
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
            str2 = this.datacenterId + "_" + this.location.id + ".temp.enc";
            str = this.datacenterId + "_" + this.location.id + this.ext + ".enc";
            if (this.key != null) {
                str9 = this.datacenterId + "_" + this.location.id + "_64.iv.enc";
            }
            str5 = null;
            str4 = null;
            str3 = null;
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
            str8 = this.datacenterId + "_" + this.location.id + ".temp";
            str = this.datacenterId + "_" + this.location.id + this.ext;
            str5 = this.key != null ? this.datacenterId + "_" + this.location.id + "_64.iv" : null;
            str4 = this.notLoadedBytesRanges != null ? this.datacenterId + "_" + this.location.id + "_64.pt" : null;
            str3 = this.datacenterId + "_" + this.location.id + "_64.preload";
            str2 = str8;
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
        str5 = str9;
        str4 = null;
        str3 = null;
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

    public /* synthetic */ void lambda$start$4(boolean z, long j, FileLoadOperationStream fileLoadOperationStream, boolean z2) {
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
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.priorityRequestInfo.requestToken, true);
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

    public /* synthetic */ void lambda$start$5(boolean[] zArr) {
        long j = this.totalBytesCount;
        if (j != 0 && ((this.isPreloadVideoOperation && zArr[0]) || this.downloadedBytes == j)) {
            try {
                onFinishLoadingFile(false);
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

    public void setIsPreloadVideoOperation(boolean z) {
        boolean z2 = this.isPreloadVideoOperation;
        if (z2 != z) {
            if (z && this.totalBytesCount <= 2097152) {
                return;
            }
            if (!z && z2) {
                if (this.state == 3) {
                    this.isPreloadVideoOperation = z;
                    this.state = 0;
                    this.preloadFinished = false;
                    start();
                    return;
                } else if (this.state == 1) {
                    Utilities.stageQueue.postRunnable(new FileLoadOperation$$ExternalSyntheticLambda6(this, z));
                    return;
                } else {
                    this.isPreloadVideoOperation = z;
                    return;
                }
            }
            this.isPreloadVideoOperation = z;
        }
    }

    public /* synthetic */ void lambda$setIsPreloadVideoOperation$6(boolean z) {
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

    public void cancel(boolean z) {
        Utilities.stageQueue.postRunnable(new FileLoadOperation$$ExternalSyntheticLambda5(this, z));
    }

    public /* synthetic */ void lambda$cancel$7(boolean z) {
        if (this.state != 3 && this.state != 2) {
            if (this.requestInfos != null) {
                for (int i = 0; i < this.requestInfos.size(); i++) {
                    RequestInfo requestInfo = this.requestInfos.get(i);
                    if (requestInfo.requestToken != 0) {
                        ConnectionsManager.getInstance(this.currentAccount).cancelRequest(requestInfo.requestToken, true);
                    }
                }
            }
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
            if (file5 == null) {
                return;
            }
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
            RandomAccessFile randomAccessFile4 = this.filePartsStream;
            if (randomAccessFile4 != null) {
                try {
                    randomAccessFile4.getChannel().close();
                } catch (Exception e7) {
                    FileLog.e(e7);
                }
                this.filePartsStream.close();
                this.filePartsStream = null;
            }
        } catch (Exception e8) {
            FileLog.e(e8);
        }
        try {
            RandomAccessFile randomAccessFile5 = this.fiv;
            if (randomAccessFile5 != null) {
                randomAccessFile5.close();
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

    private void onFinishLoadingFile(boolean z) {
        int lastIndexOf;
        String str;
        if (this.state != 1) {
            return;
        }
        this.state = 3;
        notifyStreamListeners();
        cleanup();
        if (this.isPreloadVideoOperation) {
            this.preloadFinished = true;
            if (BuildVars.DEBUG_VERSION) {
                FileLog.d("finished preloading file to " + this.cacheFileTemp + " loaded " + this.totalPreloadedBytes + " of " + this.totalBytesCount);
            }
        } else {
            File file = this.cacheIvTemp;
            if (file != null) {
                file.delete();
                this.cacheIvTemp = null;
            }
            File file2 = this.cacheFileParts;
            if (file2 != null) {
                file2.delete();
                this.cacheFileParts = null;
            }
            File file3 = this.cacheFilePreload;
            if (file3 != null) {
                file3.delete();
                this.cacheFilePreload = null;
            }
            if (this.cacheFileTemp != null) {
                boolean z2 = false;
                if (this.ungzip) {
                    try {
                        GZIPInputStream gZIPInputStream = new GZIPInputStream(new FileInputStream(this.cacheFileTemp));
                        FileLoader.copyFile(gZIPInputStream, this.cacheFileGzipTemp, 2097152);
                        gZIPInputStream.close();
                        this.cacheFileTemp.delete();
                        this.cacheFileTemp = this.cacheFileGzipTemp;
                        this.ungzip = false;
                    } catch (ZipException unused) {
                        this.ungzip = false;
                    } catch (Throwable th) {
                        FileLog.e(th);
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.e("unable to ungzip temp = " + this.cacheFileTemp + " to final = " + this.cacheFileFinal);
                        }
                    }
                }
                if (!this.ungzip) {
                    if (this.parentObject instanceof TLRPC$TL_theme) {
                        try {
                            z2 = AndroidUtilities.copyFile(this.cacheFileTemp, this.cacheFileFinal);
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
                            z2 = this.cacheFileTemp.renameTo(this.cacheFileFinal);
                        } catch (Exception e2) {
                            FileLog.e(e2);
                        }
                    }
                    if (!z2) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.e("unable to rename temp = " + this.cacheFileTemp + " to final = " + this.cacheFileFinal + " retry = " + this.renameRetryCount);
                        }
                        int i2 = this.renameRetryCount + 1;
                        this.renameRetryCount = i2;
                        if (i2 < 3) {
                            this.state = 1;
                            Utilities.stageQueue.postRunnable(new FileLoadOperation$$ExternalSyntheticLambda4(this, z), 200L);
                            return;
                        }
                        this.cacheFileFinal = this.cacheFileTemp;
                    } else if (this.pathSaveData != null && this.cacheFileFinal.exists()) {
                        this.delegate.saveFilePath(this.pathSaveData, this.cacheFileFinal);
                    }
                } else {
                    onFail(false, 0);
                    return;
                }
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("finished downloading file to " + this.cacheFileFinal + " time = " + (System.currentTimeMillis() - this.startTime));
            }
            if (z) {
                int i3 = this.currentType;
                if (i3 == 50331648) {
                    StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ApplicationLoader.getCurrentNetworkType(), 3, 1);
                } else if (i3 == 33554432) {
                    StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ApplicationLoader.getCurrentNetworkType(), 2, 1);
                } else if (i3 == 16777216) {
                    StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ApplicationLoader.getCurrentNetworkType(), 4, 1);
                } else if (i3 == 67108864) {
                    StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ApplicationLoader.getCurrentNetworkType(), 5, 1);
                }
            }
        }
        this.delegate.didFinishLoadingFile(this, this.cacheFileFinal);
    }

    public /* synthetic */ void lambda$onFinishLoadingFile$8(boolean z) {
        try {
            onFinishLoadingFile(z);
        } catch (Exception unused) {
            onFail(false, 0);
        }
    }

    private void delayRequestInfo(RequestInfo requestInfo) {
        this.delayedRequestInfos.add(requestInfo);
        if (requestInfo.response != null) {
            requestInfo.response.disableFree = true;
        } else if (requestInfo.responseWeb != null) {
            requestInfo.responseWeb.disableFree = true;
        } else if (requestInfo.responseCdn == null) {
        } else {
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
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_upload_getCdnFileHashes, new FileLoadOperation$$ExternalSyntheticLambda12(this), null, null, 0, this.datacenterId, 1, true);
    }

    public /* synthetic */ void lambda$requestFileOffsets$9(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
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
                } else if (requestInfo.responseCdn == null) {
                    return;
                } else {
                    requestInfo.responseCdn.disableFree = false;
                    requestInfo.responseCdn.freeResources();
                    return;
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:141:0x03a4, code lost:
        if (r0 == (r4 - r5)) goto L145;
     */
    /* JADX WARN: Code restructure failed: missing block: B:144:0x03aa, code lost:
        if (r9 != false) goto L145;
     */
    /* JADX WARN: Removed duplicated region for block: B:177:0x04df A[Catch: Exception -> 0x0585, TryCatch #3 {Exception -> 0x0585, blocks: (B:10:0x0048, B:12:0x004c, B:14:0x0056, B:16:0x005a, B:18:0x0060, B:19:0x0067, B:21:0x006d, B:22:0x0074, B:24:0x007a, B:27:0x0084, B:30:0x008c, B:32:0x0094, B:34:0x00a4, B:37:0x00b2, B:39:0x00b9, B:41:0x00d1, B:42:0x0103, B:44:0x0107, B:46:0x012b, B:47:0x0153, B:49:0x0157, B:50:0x015e, B:52:0x0189, B:54:0x019d, B:56:0x01b2, B:57:0x01be, B:58:0x01c7, B:59:0x01cc, B:60:0x01d4, B:62:0x01da, B:64:0x01fb, B:66:0x01ff, B:68:0x0205, B:70:0x020b, B:76:0x0217, B:77:0x0224, B:79:0x0228, B:80:0x0232, B:85:0x0248, B:89:0x0250, B:100:0x0268, B:102:0x026d, B:104:0x0286, B:106:0x028e, B:111:0x02a2, B:112:0x02b8, B:113:0x02b9, B:114:0x02bd, B:116:0x02c1, B:117:0x02f3, B:119:0x02f7, B:121:0x0304, B:122:0x0324, B:124:0x0345, B:126:0x0357, B:128:0x0367, B:131:0x0371, B:134:0x0377, B:136:0x0393, B:138:0x039a, B:140:0x03a0, B:145:0x03ac, B:147:0x03bc, B:148:0x03cd, B:153:0x03de, B:154:0x03e5, B:155:0x03e6, B:157:0x03f3, B:159:0x042f, B:161:0x043e, B:163:0x0442, B:165:0x0446, B:166:0x0493, B:168:0x0497, B:173:0x04c2, B:175:0x04db, B:177:0x04df, B:178:0x04eb, B:180:0x04f3, B:182:0x04f8, B:185:0x0508, B:187:0x0510, B:189:0x051c, B:192:0x0527, B:193:0x052a, B:195:0x0536, B:197:0x053c, B:198:0x054b, B:200:0x0551, B:201:0x0560, B:203:0x0566, B:205:0x0576, B:206:0x057b, B:207:0x057f), top: B:249:0x0048 }] */
    /* JADX WARN: Removed duplicated region for block: B:205:0x0576 A[Catch: Exception -> 0x0585, TryCatch #3 {Exception -> 0x0585, blocks: (B:10:0x0048, B:12:0x004c, B:14:0x0056, B:16:0x005a, B:18:0x0060, B:19:0x0067, B:21:0x006d, B:22:0x0074, B:24:0x007a, B:27:0x0084, B:30:0x008c, B:32:0x0094, B:34:0x00a4, B:37:0x00b2, B:39:0x00b9, B:41:0x00d1, B:42:0x0103, B:44:0x0107, B:46:0x012b, B:47:0x0153, B:49:0x0157, B:50:0x015e, B:52:0x0189, B:54:0x019d, B:56:0x01b2, B:57:0x01be, B:58:0x01c7, B:59:0x01cc, B:60:0x01d4, B:62:0x01da, B:64:0x01fb, B:66:0x01ff, B:68:0x0205, B:70:0x020b, B:76:0x0217, B:77:0x0224, B:79:0x0228, B:80:0x0232, B:85:0x0248, B:89:0x0250, B:100:0x0268, B:102:0x026d, B:104:0x0286, B:106:0x028e, B:111:0x02a2, B:112:0x02b8, B:113:0x02b9, B:114:0x02bd, B:116:0x02c1, B:117:0x02f3, B:119:0x02f7, B:121:0x0304, B:122:0x0324, B:124:0x0345, B:126:0x0357, B:128:0x0367, B:131:0x0371, B:134:0x0377, B:136:0x0393, B:138:0x039a, B:140:0x03a0, B:145:0x03ac, B:147:0x03bc, B:148:0x03cd, B:153:0x03de, B:154:0x03e5, B:155:0x03e6, B:157:0x03f3, B:159:0x042f, B:161:0x043e, B:163:0x0442, B:165:0x0446, B:166:0x0493, B:168:0x0497, B:173:0x04c2, B:175:0x04db, B:177:0x04df, B:178:0x04eb, B:180:0x04f3, B:182:0x04f8, B:185:0x0508, B:187:0x0510, B:189:0x051c, B:192:0x0527, B:193:0x052a, B:195:0x0536, B:197:0x053c, B:198:0x054b, B:200:0x0551, B:201:0x0560, B:203:0x0566, B:205:0x0576, B:206:0x057b, B:207:0x057f), top: B:249:0x0048 }] */
    /* JADX WARN: Removed duplicated region for block: B:206:0x057b A[Catch: Exception -> 0x0585, TryCatch #3 {Exception -> 0x0585, blocks: (B:10:0x0048, B:12:0x004c, B:14:0x0056, B:16:0x005a, B:18:0x0060, B:19:0x0067, B:21:0x006d, B:22:0x0074, B:24:0x007a, B:27:0x0084, B:30:0x008c, B:32:0x0094, B:34:0x00a4, B:37:0x00b2, B:39:0x00b9, B:41:0x00d1, B:42:0x0103, B:44:0x0107, B:46:0x012b, B:47:0x0153, B:49:0x0157, B:50:0x015e, B:52:0x0189, B:54:0x019d, B:56:0x01b2, B:57:0x01be, B:58:0x01c7, B:59:0x01cc, B:60:0x01d4, B:62:0x01da, B:64:0x01fb, B:66:0x01ff, B:68:0x0205, B:70:0x020b, B:76:0x0217, B:77:0x0224, B:79:0x0228, B:80:0x0232, B:85:0x0248, B:89:0x0250, B:100:0x0268, B:102:0x026d, B:104:0x0286, B:106:0x028e, B:111:0x02a2, B:112:0x02b8, B:113:0x02b9, B:114:0x02bd, B:116:0x02c1, B:117:0x02f3, B:119:0x02f7, B:121:0x0304, B:122:0x0324, B:124:0x0345, B:126:0x0357, B:128:0x0367, B:131:0x0371, B:134:0x0377, B:136:0x0393, B:138:0x039a, B:140:0x03a0, B:145:0x03ac, B:147:0x03bc, B:148:0x03cd, B:153:0x03de, B:154:0x03e5, B:155:0x03e6, B:157:0x03f3, B:159:0x042f, B:161:0x043e, B:163:0x0442, B:165:0x0446, B:166:0x0493, B:168:0x0497, B:173:0x04c2, B:175:0x04db, B:177:0x04df, B:178:0x04eb, B:180:0x04f3, B:182:0x04f8, B:185:0x0508, B:187:0x0510, B:189:0x051c, B:192:0x0527, B:193:0x052a, B:195:0x0536, B:197:0x053c, B:198:0x054b, B:200:0x0551, B:201:0x0560, B:203:0x0566, B:205:0x0576, B:206:0x057b, B:207:0x057f), top: B:249:0x0048 }] */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0217 A[Catch: Exception -> 0x0585, TryCatch #3 {Exception -> 0x0585, blocks: (B:10:0x0048, B:12:0x004c, B:14:0x0056, B:16:0x005a, B:18:0x0060, B:19:0x0067, B:21:0x006d, B:22:0x0074, B:24:0x007a, B:27:0x0084, B:30:0x008c, B:32:0x0094, B:34:0x00a4, B:37:0x00b2, B:39:0x00b9, B:41:0x00d1, B:42:0x0103, B:44:0x0107, B:46:0x012b, B:47:0x0153, B:49:0x0157, B:50:0x015e, B:52:0x0189, B:54:0x019d, B:56:0x01b2, B:57:0x01be, B:58:0x01c7, B:59:0x01cc, B:60:0x01d4, B:62:0x01da, B:64:0x01fb, B:66:0x01ff, B:68:0x0205, B:70:0x020b, B:76:0x0217, B:77:0x0224, B:79:0x0228, B:80:0x0232, B:85:0x0248, B:89:0x0250, B:100:0x0268, B:102:0x026d, B:104:0x0286, B:106:0x028e, B:111:0x02a2, B:112:0x02b8, B:113:0x02b9, B:114:0x02bd, B:116:0x02c1, B:117:0x02f3, B:119:0x02f7, B:121:0x0304, B:122:0x0324, B:124:0x0345, B:126:0x0357, B:128:0x0367, B:131:0x0371, B:134:0x0377, B:136:0x0393, B:138:0x039a, B:140:0x03a0, B:145:0x03ac, B:147:0x03bc, B:148:0x03cd, B:153:0x03de, B:154:0x03e5, B:155:0x03e6, B:157:0x03f3, B:159:0x042f, B:161:0x043e, B:163:0x0442, B:165:0x0446, B:166:0x0493, B:168:0x0497, B:173:0x04c2, B:175:0x04db, B:177:0x04df, B:178:0x04eb, B:180:0x04f3, B:182:0x04f8, B:185:0x0508, B:187:0x0510, B:189:0x051c, B:192:0x0527, B:193:0x052a, B:195:0x0536, B:197:0x053c, B:198:0x054b, B:200:0x0551, B:201:0x0560, B:203:0x0566, B:205:0x0576, B:206:0x057b, B:207:0x057f), top: B:249:0x0048 }] */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0224 A[Catch: Exception -> 0x0585, TryCatch #3 {Exception -> 0x0585, blocks: (B:10:0x0048, B:12:0x004c, B:14:0x0056, B:16:0x005a, B:18:0x0060, B:19:0x0067, B:21:0x006d, B:22:0x0074, B:24:0x007a, B:27:0x0084, B:30:0x008c, B:32:0x0094, B:34:0x00a4, B:37:0x00b2, B:39:0x00b9, B:41:0x00d1, B:42:0x0103, B:44:0x0107, B:46:0x012b, B:47:0x0153, B:49:0x0157, B:50:0x015e, B:52:0x0189, B:54:0x019d, B:56:0x01b2, B:57:0x01be, B:58:0x01c7, B:59:0x01cc, B:60:0x01d4, B:62:0x01da, B:64:0x01fb, B:66:0x01ff, B:68:0x0205, B:70:0x020b, B:76:0x0217, B:77:0x0224, B:79:0x0228, B:80:0x0232, B:85:0x0248, B:89:0x0250, B:100:0x0268, B:102:0x026d, B:104:0x0286, B:106:0x028e, B:111:0x02a2, B:112:0x02b8, B:113:0x02b9, B:114:0x02bd, B:116:0x02c1, B:117:0x02f3, B:119:0x02f7, B:121:0x0304, B:122:0x0324, B:124:0x0345, B:126:0x0357, B:128:0x0367, B:131:0x0371, B:134:0x0377, B:136:0x0393, B:138:0x039a, B:140:0x03a0, B:145:0x03ac, B:147:0x03bc, B:148:0x03cd, B:153:0x03de, B:154:0x03e5, B:155:0x03e6, B:157:0x03f3, B:159:0x042f, B:161:0x043e, B:163:0x0442, B:165:0x0446, B:166:0x0493, B:168:0x0497, B:173:0x04c2, B:175:0x04db, B:177:0x04df, B:178:0x04eb, B:180:0x04f3, B:182:0x04f8, B:185:0x0508, B:187:0x0510, B:189:0x051c, B:192:0x0527, B:193:0x052a, B:195:0x0536, B:197:0x053c, B:198:0x054b, B:200:0x0551, B:201:0x0560, B:203:0x0566, B:205:0x0576, B:206:0x057b, B:207:0x057f), top: B:249:0x0048 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean processRequestResult(RequestInfo requestInfo, TLRPC$TL_error tLRPC$TL_error) {
        boolean z;
        Exception e;
        boolean z2;
        boolean z3;
        boolean z4;
        RandomAccessFile randomAccessFile;
        boolean z5;
        long j;
        long j2;
        long j3;
        Integer num;
        if (this.state != 1) {
            if (BuildVars.DEBUG_VERSION) {
                FileLog.e(new Exception("trying to write to finished file " + this.cacheFileFinal + " offset " + requestInfo.offset));
            }
            return false;
        }
        this.requestInfos.remove(requestInfo);
        if (tLRPC$TL_error == null) {
            try {
                if (this.notLoadedBytesRanges != null || this.downloadedBytes == requestInfo.offset) {
                    NativeByteBuffer nativeByteBuffer = requestInfo.response != null ? requestInfo.response.bytes : requestInfo.responseWeb != null ? requestInfo.responseWeb.bytes : requestInfo.responseCdn != null ? requestInfo.responseCdn.bytes : null;
                    if (nativeByteBuffer != null && nativeByteBuffer.limit() != 0) {
                        int limit = nativeByteBuffer.limit();
                        if (this.isCdn) {
                            long j4 = requestInfo.offset;
                            int i = this.cdnChunkCheckSize;
                            long j5 = i * (j4 / i);
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
                        if (this.isPreloadVideoOperation) {
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
                            if (j10 != j3 && ((this.moovFound == 0 || this.foundMoovSize >= j3) && this.totalPreloadedBytes <= 2097152 && j10 < this.totalBytesCount)) {
                                z2 = false;
                                if (!z2) {
                                    this.preloadStream.seek(j3);
                                    this.preloadStream.write(1);
                                } else if (this.moovFound != 0) {
                                    this.foundMoovSize -= this.currentDownloadChunkSize;
                                }
                            }
                            z2 = true;
                            if (!z2) {
                            }
                        } else {
                            long j11 = limit;
                            long j12 = this.downloadedBytes + j11;
                            this.downloadedBytes = j12;
                            long j13 = this.totalBytesCount;
                            if (j13 > 0) {
                                if (j12 >= j13) {
                                    z3 = true;
                                }
                                z3 = false;
                            } else {
                                int i2 = this.currentDownloadChunkSize;
                                if (limit == i2) {
                                    if (j13 != j12) {
                                        j2 = 0;
                                        if (j12 % i2 != 0) {
                                        }
                                        z3 = false;
                                    } else {
                                        j2 = 0;
                                    }
                                    if (j13 > j2) {
                                        if (j13 <= j12) {
                                        }
                                        z3 = false;
                                    }
                                }
                                z3 = true;
                            }
                            boolean z6 = z3;
                            byte[] bArr2 = this.key;
                            if (bArr2 != null) {
                                Utilities.aesIgeEncryption(nativeByteBuffer.buffer, bArr2, this.iv, false, true, 0, nativeByteBuffer.limit());
                                if (z6 && this.bytesCountPadding != 0) {
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
                                    FileLog.d("save file part " + this.cacheFileFinal + " offset " + requestInfo.offset);
                                }
                            }
                            this.fileOutputStream.getChannel().write(nativeByteBuffer.buffer);
                            addPart(this.notLoadedBytesRanges, requestInfo.offset, j11 + requestInfo.offset, true);
                            if (this.isCdn) {
                                long j15 = requestInfo.offset / this.cdnChunkCheckSize;
                                int size = this.notCheckedCdnRanges.size();
                                int i3 = 0;
                                while (true) {
                                    if (i3 >= size) {
                                        z5 = true;
                                        break;
                                    }
                                    Range range = this.notCheckedCdnRanges.get(i3);
                                    if (range.start <= j15 && j15 <= range.end) {
                                        z5 = false;
                                        break;
                                    }
                                    i3++;
                                }
                                if (!z5) {
                                    int i4 = this.cdnChunkCheckSize;
                                    long j16 = j15 * i4;
                                    long downloadedLengthFromOffsetInternal = getDownloadedLengthFromOffsetInternal(this.notLoadedBytesRanges, j16, i4);
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
                                            z4 = z6;
                                            j = j16;
                                            bArr4[15] = (byte) (j18 & 255);
                                            bArr4[14] = (byte) ((j18 >> 8) & 255);
                                            bArr4[13] = (byte) ((j18 >> 16) & 255);
                                            bArr4[12] = (byte) ((j18 >> 24) & 255);
                                            Utilities.aesCtrDecryptionByteArray(this.cdnCheckBytes, this.encryptKey, bArr4, 0, downloadedLengthFromOffsetInternal, 0);
                                        } else {
                                            z4 = z6;
                                            j = j16;
                                        }
                                        if (!Arrays.equals(Utilities.computeSHA256(this.cdnCheckBytes, 0, downloadedLengthFromOffsetInternal), tLRPC$TL_fileHash.hash)) {
                                            if (BuildVars.LOGS_ENABLED) {
                                                if (this.location != null) {
                                                    FileLog.e("invalid cdn hash " + this.location + " id = " + this.location.id + " local_id = " + this.location.local_id + " access_hash = " + this.location.access_hash + " volume_id = " + this.location.volume_id + " secret = " + this.location.secret);
                                                } else if (this.webLocation != null) {
                                                    FileLog.e("invalid cdn hash  " + this.webLocation + " id = " + this.fileName);
                                                }
                                            }
                                            z = false;
                                            try {
                                                onFail(false, 0);
                                                this.cacheFileTemp.delete();
                                                return false;
                                            } catch (Exception e2) {
                                                e = e2;
                                                int i5 = z ? 1 : 0;
                                                int i6 = z ? 1 : 0;
                                                onFail(z, i5);
                                                FileLog.e(e);
                                                return false;
                                            }
                                        }
                                        this.cdnHashes.remove(Long.valueOf(j));
                                        addPart(this.notCheckedCdnRanges, j15, j15 + 1, false);
                                        randomAccessFile = this.fiv;
                                        if (randomAccessFile != null) {
                                            randomAccessFile.seek(0L);
                                            this.fiv.write(this.iv);
                                        }
                                        if (this.totalBytesCount > 0 && this.state == 1) {
                                            copyNotLoadedRanges();
                                            this.delegate.didChangedLoadProgress(this, this.downloadedBytes, this.totalBytesCount);
                                        }
                                        z2 = z4;
                                    }
                                }
                            }
                            z4 = z6;
                            randomAccessFile = this.fiv;
                            if (randomAccessFile != null) {
                            }
                            if (this.totalBytesCount > 0) {
                                copyNotLoadedRanges();
                                this.delegate.didChangedLoadProgress(this, this.downloadedBytes, this.totalBytesCount);
                            }
                            z2 = z4;
                        }
                        for (int i7 = 0; i7 < this.delayedRequestInfos.size(); i7++) {
                            RequestInfo requestInfo2 = this.delayedRequestInfos.get(i7);
                            if (this.notLoadedBytesRanges == null && this.downloadedBytes != requestInfo2.offset) {
                            }
                            this.delayedRequestInfos.remove(i7);
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
                            if (!z2) {
                                onFinishLoadingFile(true);
                            } else {
                                startDownloadRequest();
                            }
                        }
                        if (!z2) {
                        }
                    }
                    onFinishLoadingFile(true);
                    return false;
                }
                delayRequestInfo(requestInfo);
                return false;
            } catch (Exception e3) {
                e = e3;
                z = false;
            }
        } else if (tLRPC$TL_error.text.contains("FILE_MIGRATE_")) {
            Scanner scanner = new Scanner(tLRPC$TL_error.text.replace("FILE_MIGRATE_", ""));
            scanner.useDelimiter("");
            try {
                num = Integer.valueOf(scanner.nextInt());
            } catch (Exception unused) {
                num = null;
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
                    onFinishLoadingFile(true);
                } catch (Exception e4) {
                    FileLog.e(e4);
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

    public void onFail(boolean z, int i) {
        cleanup();
        this.state = 2;
        FileLoadOperationDelegate fileLoadOperationDelegate = this.delegate;
        if (fileLoadOperationDelegate != null) {
            if (z) {
                Utilities.stageQueue.postRunnable(new FileLoadOperation$$ExternalSyntheticLambda1(this, i));
            } else {
                fileLoadOperationDelegate.didFailedLoadingFile(this, i);
            }
        }
    }

    public /* synthetic */ void lambda$onFail$10(int i) {
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
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(requestInfo2.requestToken, true);
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
        clearOperaion(requestInfo, false);
        this.requestingReference = true;
        Object obj = this.parentObject;
        if (obj instanceof MessageObject) {
            MessageObject messageObject = (MessageObject) obj;
            if (messageObject.getId() < 0 && (tLRPC$WebPage = messageObject.messageOwner.media.webpage) != null) {
                this.parentObject = tLRPC$WebPage;
            }
        }
        FileRefController.getInstance(this.currentAccount).requestReference(this.parentObject, this.location, this, requestInfo);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void startDownloadRequest() {
        int i;
        long j;
        TLRPC$TL_upload_getFile tLRPC$TL_upload_getFile;
        long j2;
        HashMap<Long, PreloadRange> hashMap;
        PreloadRange preloadRange;
        ArrayList<Range> arrayList;
        long j3;
        boolean z;
        if (this.paused || this.reuploadingCdn) {
            return;
        }
        int i2 = 1;
        if (this.state != 1) {
            return;
        }
        long j4 = 0;
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
        boolean z2 = false;
        int max = (this.streamPriorityStartOffset != 0 || this.nextPartWasPreloaded || (this.isPreloadVideoOperation && this.moovFound == 0) || this.totalBytesCount <= 0) ? 1 : Math.max(0, this.currentMaxDownloadRequests - this.requestInfos.size());
        int i3 = 0;
        while (i3 < max) {
            int i4 = 2;
            if (this.isPreloadVideoOperation) {
                if (this.moovFound != 0 && this.preloadNotRequestedBytesCount <= j4) {
                    return;
                }
                long j5 = this.nextPreloadDownloadOffset;
                if (j5 == -1) {
                    int i5 = (2097152 / this.currentDownloadChunkSize) + 2;
                    long j6 = j4;
                    while (i5 != 0) {
                        if (!this.requestedPreloadedBytesRanges.containsKey(Long.valueOf(j6))) {
                            j5 = j6;
                            z = true;
                            break;
                        }
                        int i6 = this.currentDownloadChunkSize;
                        j6 += i6;
                        long j7 = this.totalBytesCount;
                        if (j6 > j7) {
                            break;
                        }
                        if (this.moovFound == i4 && j6 == i6 * 8) {
                            j6 = ((j7 - 1048576) / i6) * i6;
                        }
                        i5--;
                        i4 = 2;
                    }
                    j5 = j6;
                    z = false;
                    if (!z && this.requestInfos.isEmpty()) {
                        onFinishLoadingFile(z2);
                    }
                }
                if (this.requestedPreloadedBytesRanges == null) {
                    this.requestedPreloadedBytesRanges = new HashMap<>();
                }
                this.requestedPreloadedBytesRanges.put(Long.valueOf(j5), Integer.valueOf(i2));
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d("start next preload from " + j5 + " size " + this.totalBytesCount + " for " + this.cacheFilePreload);
                }
                this.preloadNotRequestedBytesCount -= this.currentDownloadChunkSize;
                j = j5;
                i = max;
            } else {
                ArrayList<Range> arrayList2 = this.notRequestedBytesRanges;
                if (arrayList2 != null) {
                    long j8 = this.streamPriorityStartOffset;
                    if (j8 == j4) {
                        j8 = this.streamStartOffset;
                    }
                    int size = arrayList2.size();
                    long j9 = Long.MAX_VALUE;
                    i = max;
                    int i7 = 0;
                    long j10 = Long.MAX_VALUE;
                    while (true) {
                        if (i7 >= size) {
                            j8 = j9;
                            break;
                        }
                        Range range = this.notRequestedBytesRanges.get(i7);
                        if (j8 != j4) {
                            if (range.start <= j8 && range.end > j8) {
                                j10 = Long.MAX_VALUE;
                                break;
                            } else if (j8 < range.start && range.start < j9) {
                                j9 = range.start;
                            }
                        }
                        j10 = Math.min(j10, range.start);
                        i7++;
                        j4 = 0;
                    }
                    if (j8 != Long.MAX_VALUE) {
                        j3 = j8;
                    } else if (j10 == Long.MAX_VALUE) {
                        return;
                    } else {
                        j3 = j10;
                    }
                } else {
                    i = max;
                    j3 = this.requestedBytesCount;
                }
                j = j3;
            }
            if (!this.isPreloadVideoOperation && (arrayList = this.notRequestedBytesRanges) != null) {
                addPart(arrayList, j, j + this.currentDownloadChunkSize, false);
            }
            long j11 = this.totalBytesCount;
            if (j11 > 0 && j >= j11) {
                return;
            }
            boolean z3 = j11 <= 0 || i3 == i + (-1) || (j11 > 0 && ((long) this.currentDownloadChunkSize) + j >= j11);
            int i8 = this.requestsCount % 2 == 0 ? 2 : 65538;
            int i9 = this.isForceRequest ? 32 : 0;
            if (this.isCdn) {
                TLRPC$TL_upload_getCdnFile tLRPC$TL_upload_getCdnFile = new TLRPC$TL_upload_getCdnFile();
                tLRPC$TL_upload_getCdnFile.file_token = this.cdnToken;
                tLRPC$TL_upload_getCdnFile.offset = j;
                tLRPC$TL_upload_getCdnFile.limit = this.currentDownloadChunkSize;
                i9 |= 1;
                tLRPC$TL_upload_getFile = tLRPC$TL_upload_getCdnFile;
            } else if (this.webLocation != null) {
                TLRPC$TL_upload_getWebFile tLRPC$TL_upload_getWebFile = new TLRPC$TL_upload_getWebFile();
                tLRPC$TL_upload_getWebFile.location = this.webLocation;
                tLRPC$TL_upload_getWebFile.offset = (int) j;
                tLRPC$TL_upload_getWebFile.limit = this.currentDownloadChunkSize;
                tLRPC$TL_upload_getFile = tLRPC$TL_upload_getWebFile;
            } else {
                TLRPC$TL_upload_getFile tLRPC$TL_upload_getFile2 = new TLRPC$TL_upload_getFile();
                tLRPC$TL_upload_getFile2.location = this.location;
                tLRPC$TL_upload_getFile2.offset = j;
                tLRPC$TL_upload_getFile2.limit = this.currentDownloadChunkSize;
                tLRPC$TL_upload_getFile2.cdn_supported = true;
                tLRPC$TL_upload_getFile = tLRPC$TL_upload_getFile2;
            }
            int i10 = i9;
            this.requestedBytesCount += this.currentDownloadChunkSize;
            RequestInfo requestInfo = new RequestInfo();
            this.requestInfos.add(requestInfo);
            requestInfo.offset = j;
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
                        Utilities.stageQueue.postRunnable(new FileLoadOperation$$ExternalSyntheticLambda2(this, requestInfo));
                        j2 = 0;
                    } catch (Exception unused) {
                    }
                } catch (Exception unused2) {
                }
                i3++;
                j4 = j2;
                max = i;
                i2 = 1;
                z2 = false;
            }
            if (this.streamPriorityStartOffset != 0) {
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d("frame get offset = " + this.streamPriorityStartOffset);
                }
                j2 = 0;
                this.streamPriorityStartOffset = 0L;
                this.priorityRequestInfo = requestInfo;
            } else {
                j2 = 0;
            }
            TLRPC$InputFileLocation tLRPC$InputFileLocation = this.location;
            if (!(tLRPC$InputFileLocation instanceof TLRPC$TL_inputPeerPhotoFileLocation) || ((TLRPC$TL_inputPeerPhotoFileLocation) tLRPC$InputFileLocation).photo_id != j2) {
                requestInfo.requestToken = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_upload_getFile, new FileLoadOperation$$ExternalSyntheticLambda14(this, requestInfo, tLRPC$TL_upload_getFile), null, null, i10, this.isCdn ? this.cdnDatacenterId : this.datacenterId, i8, z3);
                this.requestsCount++;
            } else {
                requestReference(requestInfo);
            }
            i3++;
            j4 = j2;
            max = i;
            i2 = 1;
            z2 = false;
        }
    }

    public /* synthetic */ void lambda$startDownloadRequest$11(RequestInfo requestInfo) {
        processRequestResult(requestInfo, null);
        requestInfo.response.freeResources();
    }

    public /* synthetic */ void lambda$startDownloadRequest$13(RequestInfo requestInfo, TLObject tLObject, TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error) {
        byte[] bArr;
        if (!this.requestInfos.contains(requestInfo)) {
            return;
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
                for (int i = 0; i < tLRPC$TL_upload_fileCdnRedirect.file_hashes.size(); i++) {
                    TLRPC$TL_fileHash tLRPC$TL_fileHash = tLRPC$TL_upload_fileCdnRedirect.file_hashes.get(i);
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
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_upload_reuploadCdnFile, new FileLoadOperation$$ExternalSyntheticLambda13(this, requestInfo), null, null, 0, this.datacenterId, 1, true);
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
                int i2 = this.currentType;
                if (i2 == 50331648) {
                    StatsController.getInstance(this.currentAccount).incrementReceivedBytesCount(tLObject2.networkType, 3, tLObject2.getObjectSize() + 4);
                } else if (i2 == 33554432) {
                    StatsController.getInstance(this.currentAccount).incrementReceivedBytesCount(tLObject2.networkType, 2, tLObject2.getObjectSize() + 4);
                } else if (i2 == 16777216) {
                    StatsController.getInstance(this.currentAccount).incrementReceivedBytesCount(tLObject2.networkType, 4, tLObject2.getObjectSize() + 4);
                } else if (i2 == 67108864) {
                    StatsController.getInstance(this.currentAccount).incrementReceivedBytesCount(tLObject2.networkType, 5, tLObject2.getObjectSize() + 4);
                }
            }
            processRequestResult(requestInfo, tLRPC$TL_error);
        }
    }

    public /* synthetic */ void lambda$startDownloadRequest$12(RequestInfo requestInfo, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
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
