package org.telegram.messenger;

import android.content.SharedPreferences;
import android.util.SparseArray;
import android.util.SparseIntArray;
import java.io.File;
import java.io.FileDescriptor;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.util.ArrayList;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.WriteToSocketDelegate;

/* loaded from: classes3.dex */
public class FileUploadOperation {
    private static final int initialRequestsCount = 8;
    private static final int initialRequestsSlowNetworkCount = 1;
    private static final int maxUploadingKBytes = 2048;
    private static final int maxUploadingSlowNetworkKBytes = 32;
    private static final int minUploadChunkSize = 128;
    private static final int minUploadChunkSlowNetworkSize = 32;
    private long availableSize;
    public volatile boolean caughtPremiumFloodWait;
    private int currentAccount;
    private long currentFileId;
    private int currentPartNum;
    private int currentType;
    private int currentUploadRequetsCount;
    private FileUploadOperationDelegate delegate;
    private long estimatedSize;
    private String fileKey;
    private int fingerprint;
    private boolean forceSmallFile;
    private ArrayList<byte[]> freeRequestIvs;
    private boolean isBigFile;
    private boolean isEncrypted;
    private boolean isLastPart;
    private byte[] iv;
    private byte[] ivChange;
    private byte[] key;
    protected long lastProgressUpdateTime;
    private int lastSavedPartNum;
    private int maxRequestsCount;
    private boolean nextPartFirst;
    private int operationGuid;
    private SharedPreferences preferences;
    private byte[] readBuffer;
    private long readBytesCount;
    private int requestNum;
    private int saveInfoTimes;
    private boolean slowNetwork;
    private boolean started;
    private int state;
    private RandomAccessFile stream;
    private long totalFileSize;
    private int totalPartsCount;
    private boolean uploadFirstPartLater;
    private int uploadStartTime;
    private long uploadedBytesCount;
    private String uploadingFilePath;
    private int uploadChunkSize = 65536;
    public final SparseIntArray requestTokens = new SparseIntArray();
    public final ArrayList<Integer> uiRequestTokens = new ArrayList<>();
    private SparseArray<UploadCachedResult> cachedResults = new SparseArray<>();
    private boolean[] recalculatedEstimatedSize = {false, false};

    /* loaded from: classes3.dex */
    public interface FileUploadOperationDelegate {
        void didChangedUploadProgress(FileUploadOperation fileUploadOperation, long j, long j2);

        void didFailedUploadingFile(FileUploadOperation fileUploadOperation);

        void didFinishUploadingFile(FileUploadOperation fileUploadOperation, TLRPC.InputFile inputFile, TLRPC.InputEncryptedFile inputEncryptedFile, byte[] bArr, byte[] bArr2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class UploadCachedResult {
        private long bytesOffset;
        private byte[] iv;

        private UploadCachedResult() {
        }
    }

    public FileUploadOperation(int i, String str, boolean z, long j, int i2) {
        boolean z2 = false;
        this.currentAccount = i;
        this.uploadingFilePath = str;
        this.isEncrypted = z;
        this.estimatedSize = j;
        this.currentType = i2;
        if (j != 0 && !z) {
            z2 = true;
        }
        this.uploadFirstPartLater = z2;
    }

    private void calcTotalPartsCount() {
        int i;
        long j;
        long j2;
        if (this.uploadFirstPartLater) {
            boolean z = this.isBigFile;
            long j3 = this.totalFileSize;
            if (z) {
                j2 = this.uploadChunkSize;
                j = j3 - j2;
            } else {
                j = j3 - 1024;
                j2 = this.uploadChunkSize;
            }
            i = ((int) (((j + j2) - 1) / j2)) + 1;
        } else {
            long j4 = this.totalFileSize;
            long j5 = this.uploadChunkSize;
            i = (int) (((j4 + j5) - 1) / j5);
        }
        this.totalPartsCount = i;
    }

    private void cleanup() {
        if (this.preferences == null) {
            this.preferences = ApplicationLoader.applicationContext.getSharedPreferences("uploadinfo", 0);
        }
        this.preferences.edit().remove(this.fileKey + "_time").remove(this.fileKey + "_size").remove(this.fileKey + "_uploaded").remove(this.fileKey + "_id").remove(this.fileKey + "_iv").remove(this.fileKey + "_key").remove(this.fileKey + "_ivc").commit();
        try {
            RandomAccessFile randomAccessFile = this.stream;
            if (randomAccessFile != null) {
                randomAccessFile.close();
                this.stream = null;
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        AutoDeleteMediaTask.unlockFile(this.uploadingFilePath);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancel$3() {
        for (int i = 0; i < this.requestTokens.size(); i++) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.requestTokens.valueAt(i), true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:18:0x003a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$checkNewDataAvailable$4(Float f, long j, long j2) {
        if (f != null && this.estimatedSize != 0 && j == 0) {
            boolean z = false;
            boolean z2 = true;
            if (f.floatValue() > 0.75f) {
                boolean[] zArr = this.recalculatedEstimatedSize;
                if (!zArr[0]) {
                    zArr[0] = true;
                    z = true;
                }
            }
            if (f.floatValue() > 0.95f) {
                boolean[] zArr2 = this.recalculatedEstimatedSize;
                if (!zArr2[1]) {
                    zArr2[1] = true;
                    if (z2) {
                        this.estimatedSize = ((float) j2) / f.floatValue();
                    }
                }
            }
            z2 = z;
            if (z2) {
            }
        }
        if (this.estimatedSize != 0 && j != 0) {
            this.estimatedSize = 0L;
            this.totalFileSize = j;
            calcTotalPartsCount();
            if (!this.uploadFirstPartLater && this.started) {
                storeFileUploadInfo();
            }
        }
        if (j <= 0) {
            j = j2;
        }
        this.availableSize = j;
        if (this.currentUploadRequetsCount < this.maxRequestsCount) {
            startUploadRequest();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onNetworkChanged$1(boolean z) {
        if (this.slowNetwork != z) {
            this.slowNetwork = z;
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("network changed to slow = " + this.slowNetwork);
            }
            int i = 0;
            while (true) {
                if (i >= this.requestTokens.size()) {
                    break;
                }
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.requestTokens.valueAt(i), true);
                i++;
            }
            this.requestTokens.clear();
            cleanup();
            this.isLastPart = false;
            this.nextPartFirst = false;
            this.requestNum = 0;
            this.currentPartNum = 0;
            this.readBytesCount = 0L;
            this.uploadedBytesCount = 0L;
            this.saveInfoTimes = 0;
            this.key = null;
            this.iv = null;
            this.ivChange = null;
            this.currentUploadRequetsCount = 0;
            this.lastSavedPartNum = 0;
            this.uploadFirstPartLater = false;
            this.cachedResults.clear();
            this.operationGuid++;
            int i2 = this.slowNetwork ? 1 : 8;
            for (int i3 = 0; i3 < i2; i3++) {
                startUploadRequest();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onNetworkChanged$2() {
        this.uiRequestTokens.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$0() {
        this.preferences = ApplicationLoader.applicationContext.getSharedPreferences("uploadinfo", 0);
        this.slowNetwork = ApplicationLoader.isConnectionSlow();
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("start upload on slow network = " + this.slowNetwork);
        }
        int i = this.slowNetwork ? 1 : 8;
        for (int i2 = 0; i2 < i; i2++) {
            startUploadRequest();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startUploadRequest$5(int[] iArr) {
        this.uiRequestTokens.remove(Integer.valueOf(iArr[0]));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startUploadRequest$6(int i, final int[] iArr, int i2, byte[] bArr, int i3, int i4, int i5, long j, TLObject tLObject, TLRPC.TL_error tL_error) {
        StatsController statsController;
        long j2;
        int i6;
        TLRPC.InputEncryptedFile tL_inputEncryptedFileUploaded;
        byte[] bArr2;
        byte[] bArr3;
        FileUploadOperationDelegate fileUploadOperationDelegate;
        FileUploadOperation fileUploadOperation;
        TLRPC.InputFile inputFile;
        TLRPC.InputEncryptedFile inputEncryptedFile;
        StatsController statsController2;
        int currentNetworkType;
        int i7;
        TLRPC.InputFile tL_inputFile;
        byte[] bArr4 = bArr;
        if (i != this.operationGuid) {
            return;
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("debug_uploading:  response reqId " + iArr[0] + " time" + this.uploadingFilePath);
        }
        int currentNetworkType2 = tLObject != null ? tLObject.networkType : ApplicationLoader.getCurrentNetworkType();
        int i8 = this.currentType;
        if (i8 == 50331648) {
            StatsController.getInstance(this.currentAccount).incrementSentBytesCount(currentNetworkType2, 3, i2);
        } else if (i8 == 33554432) {
            StatsController.getInstance(this.currentAccount).incrementSentBytesCount(currentNetworkType2, 2, i2);
        } else {
            if (i8 == 16777216) {
                statsController = StatsController.getInstance(this.currentAccount);
                j2 = i2;
                i6 = 4;
            } else if (i8 == 67108864) {
                String str = this.uploadingFilePath;
                if (str == null || !(str.toLowerCase().endsWith("mp3") || this.uploadingFilePath.toLowerCase().endsWith("m4a"))) {
                    statsController = StatsController.getInstance(this.currentAccount);
                    j2 = i2;
                    i6 = 5;
                } else {
                    statsController = StatsController.getInstance(this.currentAccount);
                    j2 = i2;
                    i6 = 7;
                }
            }
            statsController.incrementSentBytesCount(currentNetworkType2, i6, j2);
        }
        if (bArr4 != null) {
            this.freeRequestIvs.add(bArr4);
        }
        this.requestTokens.delete(i3);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileUploadOperation$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                FileUploadOperation.this.lambda$startUploadRequest$5(iArr);
            }
        });
        if (!(tLObject instanceof TLRPC.TL_boolTrue)) {
            this.state = 4;
            this.delegate.didFailedUploadingFile(this);
            cleanup();
            return;
        }
        if (this.state != 1) {
            return;
        }
        this.uploadedBytesCount += i4;
        long j3 = this.estimatedSize;
        this.delegate.didChangedUploadProgress(this, this.uploadedBytesCount, j3 != 0 ? Math.max(this.availableSize, j3) : this.totalFileSize);
        int i9 = this.currentUploadRequetsCount - 1;
        this.currentUploadRequetsCount = i9;
        if (!this.isLastPart || i9 != 0 || this.state != 1) {
            if (i9 < this.maxRequestsCount) {
                if (this.estimatedSize == 0 && !this.uploadFirstPartLater && !this.nextPartFirst) {
                    if (this.saveInfoTimes >= 4) {
                        this.saveInfoTimes = 0;
                    }
                    int i10 = this.lastSavedPartNum;
                    if (i5 == i10) {
                        this.lastSavedPartNum = i10 + 1;
                        long j4 = j;
                        while (true) {
                            UploadCachedResult uploadCachedResult = this.cachedResults.get(this.lastSavedPartNum);
                            if (uploadCachedResult == null) {
                                break;
                            }
                            j4 = uploadCachedResult.bytesOffset;
                            bArr4 = uploadCachedResult.iv;
                            this.cachedResults.remove(this.lastSavedPartNum);
                            this.lastSavedPartNum++;
                        }
                        boolean z = this.isBigFile;
                        if ((z && j4 % 1048576 == 0) || (!z && this.saveInfoTimes == 0)) {
                            SharedPreferences.Editor edit = this.preferences.edit();
                            edit.putLong(this.fileKey + "_uploaded", j4);
                            if (this.isEncrypted) {
                                edit.putString(this.fileKey + "_ivc", Utilities.bytesToHex(bArr4));
                            }
                            edit.commit();
                        }
                    } else {
                        UploadCachedResult uploadCachedResult2 = new UploadCachedResult();
                        uploadCachedResult2.bytesOffset = j;
                        if (bArr4 != null) {
                            uploadCachedResult2.iv = new byte[32];
                            System.arraycopy(bArr4, 0, uploadCachedResult2.iv, 0, 32);
                        }
                        this.cachedResults.put(i5, uploadCachedResult2);
                    }
                    this.saveInfoTimes++;
                }
                startUploadRequest();
                return;
            }
            return;
        }
        this.state = 3;
        if (this.key == null) {
            if (this.isBigFile) {
                tL_inputFile = new TLRPC.TL_inputFileBig();
            } else {
                tL_inputFile = new TLRPC.TL_inputFile();
                tL_inputFile.md5_checksum = "";
            }
            tL_inputFile.parts = this.currentPartNum;
            tL_inputFile.id = this.currentFileId;
            String str2 = this.uploadingFilePath;
            tL_inputFile.name = str2.substring(str2.lastIndexOf("/") + 1);
            bArr2 = null;
            bArr3 = null;
            fileUploadOperationDelegate = this.delegate;
            fileUploadOperation = this;
            inputFile = tL_inputFile;
            inputEncryptedFile = null;
        } else {
            if (this.isBigFile) {
                tL_inputEncryptedFileUploaded = new TLRPC.TL_inputEncryptedFileBigUploaded();
            } else {
                tL_inputEncryptedFileUploaded = new TLRPC.TL_inputEncryptedFileUploaded();
                tL_inputEncryptedFileUploaded.md5_checksum = "";
            }
            tL_inputEncryptedFileUploaded.parts = this.currentPartNum;
            tL_inputEncryptedFileUploaded.id = this.currentFileId;
            tL_inputEncryptedFileUploaded.key_fingerprint = this.fingerprint;
            FileUploadOperationDelegate fileUploadOperationDelegate2 = this.delegate;
            bArr2 = this.key;
            bArr3 = this.iv;
            fileUploadOperationDelegate = fileUploadOperationDelegate2;
            fileUploadOperation = this;
            inputFile = null;
            inputEncryptedFile = tL_inputEncryptedFileUploaded;
        }
        fileUploadOperationDelegate.didFinishUploadingFile(fileUploadOperation, inputFile, inputEncryptedFile, bArr2, bArr3);
        cleanup();
        int i11 = this.currentType;
        if (i11 == 50331648) {
            StatsController.getInstance(this.currentAccount).incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), 3, 1);
            return;
        }
        if (i11 == 33554432) {
            StatsController.getInstance(this.currentAccount).incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), 2, 1);
            return;
        }
        if (i11 == 16777216) {
            statsController2 = StatsController.getInstance(this.currentAccount);
            currentNetworkType = ApplicationLoader.getCurrentNetworkType();
            i7 = 4;
        } else {
            if (i11 != 67108864) {
                return;
            }
            String str3 = this.uploadingFilePath;
            if (str3 == null || !(str3.toLowerCase().endsWith("mp3") || this.uploadingFilePath.toLowerCase().endsWith("m4a"))) {
                statsController2 = StatsController.getInstance(this.currentAccount);
                currentNetworkType = ApplicationLoader.getCurrentNetworkType();
                i7 = 5;
            } else {
                statsController2 = StatsController.getInstance(this.currentAccount);
                currentNetworkType = ApplicationLoader.getCurrentNetworkType();
                i7 = 7;
            }
        }
        statsController2.incrementSentItemsCount(currentNetworkType, i7, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startUploadRequest$7() {
        if (this.currentUploadRequetsCount < this.maxRequestsCount) {
            startUploadRequest();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startUploadRequest$8() {
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileUploadOperation$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                FileUploadOperation.this.lambda$startUploadRequest$7();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startUploadRequest$9(int[] iArr) {
        this.uiRequestTokens.add(Integer.valueOf(iArr[0]));
    }

    /* JADX WARN: Code restructure failed: missing block: B:156:0x022a, code lost:
    
        if (r0 < (r29.uploadStartTime - 5400.0f)) goto L87;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:118:0x02f1 A[Catch: Exception -> 0x0052, TryCatch #2 {Exception -> 0x0052, blocks: (B:6:0x0008, B:8:0x0015, B:12:0x0049, B:14:0x004f, B:15:0x005b, B:17:0x005f, B:19:0x0068, B:20:0x006a, B:22:0x0083, B:24:0x008c, B:25:0x0095, B:28:0x009e, B:31:0x00b9, B:33:0x00bd, B:35:0x00c0, B:36:0x00c2, B:39:0x00cb, B:41:0x00d8, B:42:0x00e2, B:44:0x00e6, B:46:0x00f0, B:49:0x0112, B:51:0x0148, B:53:0x014c, B:55:0x0154, B:57:0x015a, B:59:0x01b0, B:62:0x01e8, B:65:0x01fa, B:67:0x01fd, B:69:0x0200, B:73:0x0210, B:75:0x0214, B:82:0x0235, B:85:0x0242, B:87:0x024d, B:89:0x0259, B:91:0x025d, B:92:0x0263, B:94:0x026e, B:96:0x0277, B:100:0x0284, B:102:0x028b, B:104:0x02a2, B:106:0x0275, B:109:0x02ae, B:111:0x02b7, B:113:0x02d3, B:115:0x02db, B:118:0x02f1, B:120:0x02f5, B:121:0x0315, B:123:0x0321, B:125:0x0325, B:127:0x032d, B:128:0x0330, B:130:0x0367, B:132:0x0373, B:134:0x0377, B:135:0x0382, B:136:0x038d, B:137:0x0385, B:147:0x0364, B:148:0x02de, B:150:0x02e9, B:152:0x02e4, B:155:0x021f, B:162:0x0055, B:163:0x0390, B:164:0x0398, B:167:0x0043, B:168:0x0399, B:170:0x03a1, B:173:0x03ae, B:175:0x03b2, B:177:0x03bd, B:178:0x03d2, B:183:0x03e3, B:185:0x03e7, B:187:0x03eb, B:188:0x03f1, B:190:0x03fc, B:192:0x0400, B:194:0x0408, B:196:0x041b, B:200:0x0428, B:202:0x042f, B:203:0x045c, B:205:0x0460, B:207:0x0475, B:208:0x047b, B:210:0x0491, B:212:0x0495, B:214:0x0499, B:215:0x04a8, B:229:0x0478, B:230:0x0481, B:232:0x040f, B:234:0x0413, B:235:0x0419, B:237:0x03c7, B:238:0x03d6, B:10:0x0025, B:139:0x0334, B:142:0x034f), top: B:5:0x0008, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0373 A[Catch: Exception -> 0x0052, TryCatch #2 {Exception -> 0x0052, blocks: (B:6:0x0008, B:8:0x0015, B:12:0x0049, B:14:0x004f, B:15:0x005b, B:17:0x005f, B:19:0x0068, B:20:0x006a, B:22:0x0083, B:24:0x008c, B:25:0x0095, B:28:0x009e, B:31:0x00b9, B:33:0x00bd, B:35:0x00c0, B:36:0x00c2, B:39:0x00cb, B:41:0x00d8, B:42:0x00e2, B:44:0x00e6, B:46:0x00f0, B:49:0x0112, B:51:0x0148, B:53:0x014c, B:55:0x0154, B:57:0x015a, B:59:0x01b0, B:62:0x01e8, B:65:0x01fa, B:67:0x01fd, B:69:0x0200, B:73:0x0210, B:75:0x0214, B:82:0x0235, B:85:0x0242, B:87:0x024d, B:89:0x0259, B:91:0x025d, B:92:0x0263, B:94:0x026e, B:96:0x0277, B:100:0x0284, B:102:0x028b, B:104:0x02a2, B:106:0x0275, B:109:0x02ae, B:111:0x02b7, B:113:0x02d3, B:115:0x02db, B:118:0x02f1, B:120:0x02f5, B:121:0x0315, B:123:0x0321, B:125:0x0325, B:127:0x032d, B:128:0x0330, B:130:0x0367, B:132:0x0373, B:134:0x0377, B:135:0x0382, B:136:0x038d, B:137:0x0385, B:147:0x0364, B:148:0x02de, B:150:0x02e9, B:152:0x02e4, B:155:0x021f, B:162:0x0055, B:163:0x0390, B:164:0x0398, B:167:0x0043, B:168:0x0399, B:170:0x03a1, B:173:0x03ae, B:175:0x03b2, B:177:0x03bd, B:178:0x03d2, B:183:0x03e3, B:185:0x03e7, B:187:0x03eb, B:188:0x03f1, B:190:0x03fc, B:192:0x0400, B:194:0x0408, B:196:0x041b, B:200:0x0428, B:202:0x042f, B:203:0x045c, B:205:0x0460, B:207:0x0475, B:208:0x047b, B:210:0x0491, B:212:0x0495, B:214:0x0499, B:215:0x04a8, B:229:0x0478, B:230:0x0481, B:232:0x040f, B:234:0x0413, B:235:0x0419, B:237:0x03c7, B:238:0x03d6, B:10:0x0025, B:139:0x0334, B:142:0x034f), top: B:5:0x0008, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:138:0x0334 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:155:0x021f A[Catch: Exception -> 0x0052, TryCatch #2 {Exception -> 0x0052, blocks: (B:6:0x0008, B:8:0x0015, B:12:0x0049, B:14:0x004f, B:15:0x005b, B:17:0x005f, B:19:0x0068, B:20:0x006a, B:22:0x0083, B:24:0x008c, B:25:0x0095, B:28:0x009e, B:31:0x00b9, B:33:0x00bd, B:35:0x00c0, B:36:0x00c2, B:39:0x00cb, B:41:0x00d8, B:42:0x00e2, B:44:0x00e6, B:46:0x00f0, B:49:0x0112, B:51:0x0148, B:53:0x014c, B:55:0x0154, B:57:0x015a, B:59:0x01b0, B:62:0x01e8, B:65:0x01fa, B:67:0x01fd, B:69:0x0200, B:73:0x0210, B:75:0x0214, B:82:0x0235, B:85:0x0242, B:87:0x024d, B:89:0x0259, B:91:0x025d, B:92:0x0263, B:94:0x026e, B:96:0x0277, B:100:0x0284, B:102:0x028b, B:104:0x02a2, B:106:0x0275, B:109:0x02ae, B:111:0x02b7, B:113:0x02d3, B:115:0x02db, B:118:0x02f1, B:120:0x02f5, B:121:0x0315, B:123:0x0321, B:125:0x0325, B:127:0x032d, B:128:0x0330, B:130:0x0367, B:132:0x0373, B:134:0x0377, B:135:0x0382, B:136:0x038d, B:137:0x0385, B:147:0x0364, B:148:0x02de, B:150:0x02e9, B:152:0x02e4, B:155:0x021f, B:162:0x0055, B:163:0x0390, B:164:0x0398, B:167:0x0043, B:168:0x0399, B:170:0x03a1, B:173:0x03ae, B:175:0x03b2, B:177:0x03bd, B:178:0x03d2, B:183:0x03e3, B:185:0x03e7, B:187:0x03eb, B:188:0x03f1, B:190:0x03fc, B:192:0x0400, B:194:0x0408, B:196:0x041b, B:200:0x0428, B:202:0x042f, B:203:0x045c, B:205:0x0460, B:207:0x0475, B:208:0x047b, B:210:0x0491, B:212:0x0495, B:214:0x0499, B:215:0x04a8, B:229:0x0478, B:230:0x0481, B:232:0x040f, B:234:0x0413, B:235:0x0419, B:237:0x03c7, B:238:0x03d6, B:10:0x0025, B:139:0x0334, B:142:0x034f), top: B:5:0x0008, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0214 A[Catch: Exception -> 0x0052, TryCatch #2 {Exception -> 0x0052, blocks: (B:6:0x0008, B:8:0x0015, B:12:0x0049, B:14:0x004f, B:15:0x005b, B:17:0x005f, B:19:0x0068, B:20:0x006a, B:22:0x0083, B:24:0x008c, B:25:0x0095, B:28:0x009e, B:31:0x00b9, B:33:0x00bd, B:35:0x00c0, B:36:0x00c2, B:39:0x00cb, B:41:0x00d8, B:42:0x00e2, B:44:0x00e6, B:46:0x00f0, B:49:0x0112, B:51:0x0148, B:53:0x014c, B:55:0x0154, B:57:0x015a, B:59:0x01b0, B:62:0x01e8, B:65:0x01fa, B:67:0x01fd, B:69:0x0200, B:73:0x0210, B:75:0x0214, B:82:0x0235, B:85:0x0242, B:87:0x024d, B:89:0x0259, B:91:0x025d, B:92:0x0263, B:94:0x026e, B:96:0x0277, B:100:0x0284, B:102:0x028b, B:104:0x02a2, B:106:0x0275, B:109:0x02ae, B:111:0x02b7, B:113:0x02d3, B:115:0x02db, B:118:0x02f1, B:120:0x02f5, B:121:0x0315, B:123:0x0321, B:125:0x0325, B:127:0x032d, B:128:0x0330, B:130:0x0367, B:132:0x0373, B:134:0x0377, B:135:0x0382, B:136:0x038d, B:137:0x0385, B:147:0x0364, B:148:0x02de, B:150:0x02e9, B:152:0x02e4, B:155:0x021f, B:162:0x0055, B:163:0x0390, B:164:0x0398, B:167:0x0043, B:168:0x0399, B:170:0x03a1, B:173:0x03ae, B:175:0x03b2, B:177:0x03bd, B:178:0x03d2, B:183:0x03e3, B:185:0x03e7, B:187:0x03eb, B:188:0x03f1, B:190:0x03fc, B:192:0x0400, B:194:0x0408, B:196:0x041b, B:200:0x0428, B:202:0x042f, B:203:0x045c, B:205:0x0460, B:207:0x0475, B:208:0x047b, B:210:0x0491, B:212:0x0495, B:214:0x0499, B:215:0x04a8, B:229:0x0478, B:230:0x0481, B:232:0x040f, B:234:0x0413, B:235:0x0419, B:237:0x03c7, B:238:0x03d6, B:10:0x0025, B:139:0x0334, B:142:0x034f), top: B:5:0x0008, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x022f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void startUploadRequest() {
        int read;
        final byte[] bArr;
        int i;
        TLRPC.TL_upload_saveFilePart tL_upload_saveFilePart;
        int i2;
        boolean z;
        boolean z2;
        long j;
        boolean z3;
        if (this.state != 1) {
            return;
        }
        try {
            this.started = true;
            byte[] bArr2 = null;
            if (this.stream == null) {
                File file = new File(this.uploadingFilePath);
                this.stream = new RandomAccessFile(file, "r");
                try {
                    z = AndroidUtilities.isInternalUri(((Integer) FileDescriptor.class.getDeclaredMethod("getInt$", null).invoke(this.stream.getFD(), null)).intValue());
                } catch (Throwable th) {
                    FileLog.e(th);
                    z = false;
                }
                if (z) {
                    throw new Exception("trying to upload internal file");
                }
                long j2 = this.estimatedSize;
                if (j2 != 0) {
                    this.totalFileSize = j2;
                } else {
                    this.totalFileSize = file.length();
                }
                if (!this.forceSmallFile && this.totalFileSize > 10485760) {
                    this.isBigFile = true;
                }
                long j3 = MessagesController.getInstance(this.currentAccount).uploadMaxFileParts;
                if (AccountInstance.getInstance(this.currentAccount).getUserConfig().isPremium() && this.totalFileSize > FileLoader.DEFAULT_MAX_FILE_SIZE) {
                    j3 = MessagesController.getInstance(this.currentAccount).uploadMaxFilePartsPremium;
                }
                long j4 = j3 * 1024;
                int max = (int) Math.max(this.slowNetwork ? 32L : 128L, ((this.totalFileSize + j4) - 1) / j4);
                this.uploadChunkSize = max;
                if (1024 % max != 0) {
                    int i3 = 64;
                    while (this.uploadChunkSize > i3) {
                        i3 *= 2;
                    }
                    this.uploadChunkSize = i3;
                }
                this.maxRequestsCount = Math.max(1, (this.slowNetwork ? 32 : 2048) / this.uploadChunkSize);
                if (this.isEncrypted) {
                    this.freeRequestIvs = new ArrayList<>(this.maxRequestsCount);
                    for (int i4 = 0; i4 < this.maxRequestsCount; i4++) {
                        this.freeRequestIvs.add(new byte[32]);
                    }
                }
                this.uploadChunkSize *= 1024;
                calcTotalPartsCount();
                this.readBuffer = new byte[this.uploadChunkSize];
                StringBuilder sb = new StringBuilder();
                sb.append(this.uploadingFilePath);
                sb.append(this.isEncrypted ? "enc" : "");
                this.fileKey = Utilities.MD5(sb.toString());
                long j5 = this.preferences.getLong(this.fileKey + "_size", 0L);
                this.uploadStartTime = (int) (System.currentTimeMillis() / 1000);
                if (!this.uploadFirstPartLater && !this.nextPartFirst && this.estimatedSize == 0 && j5 == this.totalFileSize) {
                    this.currentFileId = this.preferences.getLong(this.fileKey + "_id", 0L);
                    int i5 = this.preferences.getInt(this.fileKey + "_time", 0);
                    long j6 = this.preferences.getLong(this.fileKey + "_uploaded", 0L);
                    if (this.isEncrypted) {
                        String string = this.preferences.getString(this.fileKey + "_iv", null);
                        String string2 = this.preferences.getString(this.fileKey + "_key", null);
                        if (string != null && string2 != null) {
                            this.key = Utilities.hexToBytes(string2);
                            byte[] hexToBytes = Utilities.hexToBytes(string);
                            this.iv = hexToBytes;
                            byte[] bArr3 = this.key;
                            if (bArr3 != null && hexToBytes != null && bArr3.length == 32 && hexToBytes.length == 32) {
                                byte[] bArr4 = new byte[32];
                                this.ivChange = bArr4;
                                System.arraycopy(hexToBytes, 0, bArr4, 0, 32);
                            }
                        }
                        z2 = true;
                        if (!z2 && i5 != 0) {
                            z3 = this.isBigFile;
                            if (z3) {
                                if (i5 < this.uploadStartTime - 86400) {
                                    i5 = 0;
                                    if (i5 != 0) {
                                        if (j6 > 0) {
                                            this.readBytesCount = j6;
                                            this.currentPartNum = (int) (j6 / this.uploadChunkSize);
                                            if (z3) {
                                                this.stream.seek(j6);
                                                if (this.isEncrypted) {
                                                    bArr2 = null;
                                                    String string3 = this.preferences.getString(this.fileKey + "_ivc", null);
                                                    if (string3 != null) {
                                                        byte[] hexToBytes2 = Utilities.hexToBytes(string3);
                                                        this.ivChange = hexToBytes2;
                                                        if (hexToBytes2 == null || hexToBytes2.length != 32) {
                                                            this.readBytesCount = 0L;
                                                        }
                                                        if (z2) {
                                                            if (this.isEncrypted) {
                                                                byte[] bArr5 = new byte[32];
                                                                this.iv = bArr5;
                                                                this.key = new byte[32];
                                                                this.ivChange = new byte[32];
                                                                Utilities.random.nextBytes(bArr5);
                                                                Utilities.random.nextBytes(this.key);
                                                                System.arraycopy(this.iv, 0, this.ivChange, 0, 32);
                                                            }
                                                            this.currentFileId = Utilities.random.nextLong();
                                                            if (!this.nextPartFirst && !this.uploadFirstPartLater && this.estimatedSize == 0) {
                                                                storeFileUploadInfo();
                                                            }
                                                        }
                                                        if (this.isEncrypted) {
                                                            try {
                                                                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                                                                byte[] bArr6 = new byte[64];
                                                                System.arraycopy(this.key, 0, bArr6, 0, 32);
                                                                System.arraycopy(this.iv, 0, bArr6, 32, 32);
                                                                byte[] digest = messageDigest.digest(bArr6);
                                                                for (int i6 = 0; i6 < 4; i6++) {
                                                                    this.fingerprint |= ((digest[i6] ^ digest[i6 + 4]) & NotificationCenter.closeSearchByActiveAction) << (i6 * 8);
                                                                }
                                                            } catch (Exception e) {
                                                                FileLog.e(e);
                                                            }
                                                        }
                                                        this.uploadedBytesCount = this.readBytesCount;
                                                        this.lastSavedPartNum = this.currentPartNum;
                                                        if (this.uploadFirstPartLater) {
                                                            if (this.isBigFile) {
                                                                this.stream.seek(this.uploadChunkSize);
                                                                j = this.uploadChunkSize;
                                                            } else {
                                                                j = 1024;
                                                                this.stream.seek(1024L);
                                                            }
                                                            this.readBytesCount = j;
                                                            this.currentPartNum = 1;
                                                        }
                                                    } else {
                                                        this.readBytesCount = 0L;
                                                    }
                                                    this.currentPartNum = 0;
                                                    z2 = true;
                                                    if (z2) {
                                                    }
                                                    if (this.isEncrypted) {
                                                    }
                                                    this.uploadedBytesCount = this.readBytesCount;
                                                    this.lastSavedPartNum = this.currentPartNum;
                                                    if (this.uploadFirstPartLater) {
                                                    }
                                                }
                                            } else {
                                                for (int i7 = 0; i7 < this.readBytesCount / this.uploadChunkSize; i7++) {
                                                    int read2 = this.stream.read(this.readBuffer);
                                                    int i8 = (!this.isEncrypted || read2 % 16 == 0) ? 0 : 16 - (read2 % 16);
                                                    int i9 = read2 + i8;
                                                    NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(i9);
                                                    if (read2 != this.uploadChunkSize || this.totalPartsCount == this.currentPartNum + 1) {
                                                        this.isLastPart = true;
                                                    }
                                                    int i10 = 0;
                                                    nativeByteBuffer.writeBytes(this.readBuffer, 0, read2);
                                                    if (this.isEncrypted) {
                                                        int i11 = 0;
                                                        while (i11 < i8) {
                                                            nativeByteBuffer.writeByte(i10);
                                                            i11++;
                                                            i10 = 0;
                                                        }
                                                        Utilities.aesIgeEncryption(nativeByteBuffer.buffer, this.key, this.ivChange, true, true, 0, i9);
                                                    }
                                                    nativeByteBuffer.reuse();
                                                }
                                            }
                                        }
                                    }
                                    bArr2 = null;
                                    if (z2) {
                                    }
                                    if (this.isEncrypted) {
                                    }
                                    this.uploadedBytesCount = this.readBytesCount;
                                    this.lastSavedPartNum = this.currentPartNum;
                                    if (this.uploadFirstPartLater) {
                                    }
                                }
                            }
                            if (!z3) {
                            }
                            if (i5 != 0) {
                            }
                            bArr2 = null;
                            if (z2) {
                            }
                            if (this.isEncrypted) {
                            }
                            this.uploadedBytesCount = this.readBytesCount;
                            this.lastSavedPartNum = this.currentPartNum;
                            if (this.uploadFirstPartLater) {
                            }
                        }
                    }
                    z2 = false;
                    if (!z2) {
                        z3 = this.isBigFile;
                        if (z3) {
                        }
                        if (!z3) {
                        }
                        if (i5 != 0) {
                        }
                        bArr2 = null;
                        if (z2) {
                        }
                        if (this.isEncrypted) {
                        }
                        this.uploadedBytesCount = this.readBytesCount;
                        this.lastSavedPartNum = this.currentPartNum;
                        if (this.uploadFirstPartLater) {
                        }
                    }
                }
                bArr2 = null;
                z2 = true;
                if (z2) {
                }
                if (this.isEncrypted) {
                }
                this.uploadedBytesCount = this.readBytesCount;
                this.lastSavedPartNum = this.currentPartNum;
                if (this.uploadFirstPartLater) {
                }
            }
            if (this.estimatedSize == 0 || this.readBytesCount + this.uploadChunkSize <= this.availableSize) {
                if (this.nextPartFirst) {
                    this.stream.seek(0L);
                    if (this.isBigFile) {
                        read = this.stream.read(this.readBuffer);
                        i2 = 0;
                    } else {
                        i2 = 0;
                        read = this.stream.read(this.readBuffer, 0, 1024);
                    }
                    this.currentPartNum = i2;
                } else {
                    read = this.stream.read(this.readBuffer);
                }
                final int i12 = read;
                if (i12 == -1) {
                    return;
                }
                int i13 = (!this.isEncrypted || i12 % 16 == 0) ? 0 : 16 - (i12 % 16);
                int i14 = i12 + i13;
                NativeByteBuffer nativeByteBuffer2 = new NativeByteBuffer(i14);
                if (this.nextPartFirst || i12 != this.uploadChunkSize || (this.estimatedSize == 0 && this.totalPartsCount == this.currentPartNum + 1)) {
                    if (this.uploadFirstPartLater) {
                        this.nextPartFirst = true;
                        this.uploadFirstPartLater = false;
                    } else {
                        this.isLastPart = true;
                    }
                }
                int i15 = 0;
                nativeByteBuffer2.writeBytes(this.readBuffer, 0, i12);
                if (this.isEncrypted) {
                    int i16 = 0;
                    while (i16 < i13) {
                        nativeByteBuffer2.writeByte(i15);
                        i16++;
                        i15 = 0;
                    }
                    Utilities.aesIgeEncryption(nativeByteBuffer2.buffer, this.key, this.ivChange, true, true, 0, i14);
                    byte[] bArr7 = this.freeRequestIvs.get(0);
                    System.arraycopy(this.ivChange, 0, bArr7, 0, 32);
                    this.freeRequestIvs.remove(0);
                    bArr = bArr7;
                } else {
                    bArr = bArr2;
                }
                if (this.isBigFile) {
                    TLRPC.TL_upload_saveBigFilePart tL_upload_saveBigFilePart = new TLRPC.TL_upload_saveBigFilePart();
                    i = this.currentPartNum;
                    tL_upload_saveBigFilePart.file_part = i;
                    tL_upload_saveBigFilePart.file_id = this.currentFileId;
                    tL_upload_saveBigFilePart.file_total_parts = this.estimatedSize == 0 ? this.totalPartsCount : -1;
                    tL_upload_saveBigFilePart.bytes = nativeByteBuffer2;
                    tL_upload_saveFilePart = tL_upload_saveBigFilePart;
                } else {
                    TLRPC.TL_upload_saveFilePart tL_upload_saveFilePart2 = new TLRPC.TL_upload_saveFilePart();
                    i = this.currentPartNum;
                    tL_upload_saveFilePart2.file_part = i;
                    tL_upload_saveFilePart2.file_id = this.currentFileId;
                    tL_upload_saveFilePart2.bytes = nativeByteBuffer2;
                    tL_upload_saveFilePart = tL_upload_saveFilePart2;
                }
                TLRPC.TL_upload_saveFilePart tL_upload_saveFilePart3 = tL_upload_saveFilePart;
                final int i17 = i;
                if (this.isLastPart && this.nextPartFirst) {
                    this.nextPartFirst = false;
                    this.currentPartNum = this.totalPartsCount - 1;
                    this.stream.seek(this.totalFileSize);
                }
                this.readBytesCount += i12;
                this.currentPartNum++;
                this.currentUploadRequetsCount++;
                final int i18 = this.requestNum;
                this.requestNum = i18 + 1;
                final long j7 = i17 + i12;
                final int objectSize = tL_upload_saveFilePart3.getObjectSize() + 4;
                final int i19 = this.operationGuid;
                int i20 = this.slowNetwork ? 4 : ((i18 % 4) << 16) | 4;
                System.currentTimeMillis();
                final int[] iArr = new int[1];
                iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_upload_saveFilePart3, new RequestDelegate() { // from class: org.telegram.messenger.FileUploadOperation$$ExternalSyntheticLambda5
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        FileUploadOperation.this.lambda$startUploadRequest$6(i19, iArr, objectSize, bArr, i18, i12, i17, j7, tLObject, tL_error);
                    }
                }, null, new WriteToSocketDelegate() { // from class: org.telegram.messenger.FileUploadOperation$$ExternalSyntheticLambda6
                    @Override // org.telegram.tgnet.WriteToSocketDelegate
                    public final void run() {
                        FileUploadOperation.this.lambda$startUploadRequest$8();
                    }
                }, this.forceSmallFile ? 4 : 0, ConnectionsManager.DEFAULT_DATACENTER_ID, i20, true);
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("debug_uploading:  send reqId " + iArr[0] + " " + this.uploadingFilePath + " file_part=" + i17 + " isBig=" + this.isBigFile + " file_id=" + this.currentFileId);
                }
                this.requestTokens.put(i18, iArr[0]);
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileUploadOperation$$ExternalSyntheticLambda7
                    @Override // java.lang.Runnable
                    public final void run() {
                        FileUploadOperation.this.lambda$startUploadRequest$9(iArr);
                    }
                });
            }
        } catch (Exception e2) {
            FileLog.e(e2);
            this.state = 4;
            this.delegate.didFailedUploadingFile(this);
            cleanup();
        }
    }

    private void storeFileUploadInfo() {
        SharedPreferences.Editor edit = this.preferences.edit();
        edit.putInt(this.fileKey + "_time", this.uploadStartTime);
        edit.putLong(this.fileKey + "_size", this.totalFileSize);
        edit.putLong(this.fileKey + "_id", this.currentFileId);
        edit.remove(this.fileKey + "_uploaded");
        if (this.isEncrypted) {
            edit.putString(this.fileKey + "_iv", Utilities.bytesToHex(this.iv));
            edit.putString(this.fileKey + "_ivc", Utilities.bytesToHex(this.ivChange));
            edit.putString(this.fileKey + "_key", Utilities.bytesToHex(this.key));
        }
        edit.commit();
    }

    public void cancel() {
        if (this.state == 3) {
            return;
        }
        this.state = 2;
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileUploadOperation$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                FileUploadOperation.this.lambda$cancel$3();
            }
        });
        AutoDeleteMediaTask.unlockFile(this.uploadingFilePath);
        this.delegate.didFailedUploadingFile(this);
        cleanup();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void checkNewDataAvailable(final long j, final long j2, final Float f) {
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileUploadOperation$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                FileUploadOperation.this.lambda$checkNewDataAvailable$4(f, j2, j);
            }
        });
    }

    public long getTotalFileSize() {
        return this.totalFileSize;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onNetworkChanged(final boolean z) {
        if (this.state != 1) {
            return;
        }
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileUploadOperation$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                FileUploadOperation.this.lambda$onNetworkChanged$1(z);
            }
        });
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileUploadOperation$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                FileUploadOperation.this.lambda$onNetworkChanged$2();
            }
        });
    }

    public void setDelegate(FileUploadOperationDelegate fileUploadOperationDelegate) {
        this.delegate = fileUploadOperationDelegate;
    }

    public void setForceSmallFile() {
        this.forceSmallFile = true;
    }

    public void start() {
        if (this.state != 0) {
            return;
        }
        this.state = 1;
        AutoDeleteMediaTask.lockFile(this.uploadingFilePath);
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileUploadOperation$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                FileUploadOperation.this.lambda$start$0();
            }
        });
    }
}
