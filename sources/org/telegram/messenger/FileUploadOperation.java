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
import org.telegram.tgnet.TLRPC$InputEncryptedFile;
import org.telegram.tgnet.TLRPC$InputFile;
import org.telegram.tgnet.TLRPC$TL_boolTrue;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputFile;
import org.telegram.tgnet.TLRPC$TL_inputFileBig;
import org.telegram.tgnet.TLRPC$TL_upload_saveBigFilePart;
import org.telegram.tgnet.TLRPC$TL_upload_saveFilePart;
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
    private SparseArray<UploadCachedResult> cachedResults = new SparseArray<>();
    private boolean[] recalculatedEstimatedSize = {false, false};

    /* loaded from: classes3.dex */
    public interface FileUploadOperationDelegate {
        void didChangedUploadProgress(FileUploadOperation fileUploadOperation, long j, long j2);

        void didFailedUploadingFile(FileUploadOperation fileUploadOperation);

        void didFinishUploadingFile(FileUploadOperation fileUploadOperation, TLRPC$InputFile tLRPC$InputFile, TLRPC$InputEncryptedFile tLRPC$InputEncryptedFile, byte[] bArr, byte[] bArr2);
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

    public long getTotalFileSize() {
        return this.totalFileSize;
    }

    public void setDelegate(FileUploadOperationDelegate fileUploadOperationDelegate) {
        this.delegate = fileUploadOperationDelegate;
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

    /* JADX INFO: Access modifiers changed from: protected */
    public void onNetworkChanged(final boolean z) {
        if (this.state != 1) {
            return;
        }
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileUploadOperation$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                FileUploadOperation.this.lambda$onNetworkChanged$1(z);
            }
        });
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

    public void cancel() {
        if (this.state == 3) {
            return;
        }
        this.state = 2;
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileUploadOperation$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                FileUploadOperation.this.lambda$cancel$2();
            }
        });
        AutoDeleteMediaTask.unlockFile(this.uploadingFilePath);
        this.delegate.didFailedUploadingFile(this);
        cleanup();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancel$2() {
        for (int i = 0; i < this.requestTokens.size(); i++) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.requestTokens.valueAt(i), true);
        }
    }

    private void cleanup() {
        if (this.preferences == null) {
            this.preferences = ApplicationLoader.applicationContext.getSharedPreferences("uploadinfo", 0);
        }
        SharedPreferences.Editor edit = this.preferences.edit();
        SharedPreferences.Editor remove = edit.remove(this.fileKey + "_time");
        SharedPreferences.Editor remove2 = remove.remove(this.fileKey + "_size");
        SharedPreferences.Editor remove3 = remove2.remove(this.fileKey + "_uploaded");
        SharedPreferences.Editor remove4 = remove3.remove(this.fileKey + "_id");
        SharedPreferences.Editor remove5 = remove4.remove(this.fileKey + "_iv");
        SharedPreferences.Editor remove6 = remove5.remove(this.fileKey + "_key");
        remove6.remove(this.fileKey + "_ivc").commit();
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

    /* JADX INFO: Access modifiers changed from: protected */
    public void checkNewDataAvailable(final long j, final long j2, final Float f) {
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileUploadOperation$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                FileUploadOperation.this.lambda$checkNewDataAvailable$3(f, j2, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:20:0x003a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$checkNewDataAvailable$3(Float f, long j, long j2) {
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

    private void calcTotalPartsCount() {
        if (this.uploadFirstPartLater) {
            if (this.isBigFile) {
                long j = this.totalFileSize;
                long j2 = this.uploadChunkSize;
                this.totalPartsCount = ((int) ((((j - j2) + j2) - 1) / j2)) + 1;
                return;
            }
            long j3 = this.uploadChunkSize;
            this.totalPartsCount = ((int) ((((this.totalFileSize - 1024) + j3) - 1) / j3)) + 1;
            return;
        }
        long j4 = this.totalFileSize;
        long j5 = this.uploadChunkSize;
        this.totalPartsCount = (int) (((j4 + j5) - 1) / j5);
    }

    public void setForceSmallFile() {
        this.forceSmallFile = true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:86:0x022a, code lost:
        if (r0 < (r29.uploadStartTime - 5400.0f)) goto L77;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:128:0x02f3 A[Catch: Exception -> 0x0052, TryCatch #2 {Exception -> 0x0052, blocks: (B:5:0x0008, B:7:0x0015, B:13:0x0049, B:15:0x004f, B:19:0x005b, B:21:0x005f, B:23:0x0068, B:24:0x006a, B:26:0x0083, B:28:0x008c, B:29:0x0095, B:33:0x009e, B:36:0x00b9, B:38:0x00bd, B:39:0x00c0, B:40:0x00c2, B:44:0x00cb, B:46:0x00d8, B:47:0x00e2, B:49:0x00e6, B:50:0x00f0, B:54:0x0112, B:56:0x0148, B:58:0x014c, B:60:0x0154, B:62:0x015a, B:64:0x01b0, B:67:0x01e8, B:70:0x01fa, B:72:0x01fd, B:74:0x0200, B:79:0x0210, B:81:0x0214, B:91:0x0235, B:94:0x0242, B:96:0x024d, B:98:0x0259, B:100:0x025d, B:102:0x0263, B:104:0x026e, B:107:0x0277, B:111:0x0284, B:112:0x028b, B:113:0x02a2, B:106:0x0275, B:115:0x02ae, B:117:0x02b7, B:119:0x02d3, B:121:0x02db, B:128:0x02f3, B:130:0x02f7, B:131:0x0317, B:133:0x0323, B:135:0x0327, B:137:0x032f, B:138:0x0332, B:147:0x0369, B:149:0x0375, B:151:0x0379, B:153:0x0390, B:152:0x0387, B:146:0x0366, B:123:0x02de, B:125:0x02e7, B:85:0x021f, B:18:0x0055, B:154:0x0393, B:155:0x039b, B:156:0x039c, B:158:0x03a4, B:161:0x03b1, B:163:0x03b5, B:165:0x03c0, B:167:0x03d5, B:173:0x03e6, B:175:0x03ea, B:177:0x03ee, B:179:0x03f4, B:181:0x03ff, B:183:0x0403, B:185:0x040b, B:191:0x041e, B:195:0x042b, B:196:0x0432, B:198:0x045f, B:200:0x0463, B:202:0x0478, B:204:0x047f, B:207:0x0495, B:209:0x0499, B:211:0x049d, B:212:0x04ac, B:203:0x047b, B:206:0x0485, B:187:0x0412, B:189:0x0416, B:190:0x041c, B:166:0x03ca, B:169:0x03d9, B:8:0x0025, B:140:0x0336, B:143:0x0351), top: B:230:0x0008, inners: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:149:0x0375 A[Catch: Exception -> 0x0052, TryCatch #2 {Exception -> 0x0052, blocks: (B:5:0x0008, B:7:0x0015, B:13:0x0049, B:15:0x004f, B:19:0x005b, B:21:0x005f, B:23:0x0068, B:24:0x006a, B:26:0x0083, B:28:0x008c, B:29:0x0095, B:33:0x009e, B:36:0x00b9, B:38:0x00bd, B:39:0x00c0, B:40:0x00c2, B:44:0x00cb, B:46:0x00d8, B:47:0x00e2, B:49:0x00e6, B:50:0x00f0, B:54:0x0112, B:56:0x0148, B:58:0x014c, B:60:0x0154, B:62:0x015a, B:64:0x01b0, B:67:0x01e8, B:70:0x01fa, B:72:0x01fd, B:74:0x0200, B:79:0x0210, B:81:0x0214, B:91:0x0235, B:94:0x0242, B:96:0x024d, B:98:0x0259, B:100:0x025d, B:102:0x0263, B:104:0x026e, B:107:0x0277, B:111:0x0284, B:112:0x028b, B:113:0x02a2, B:106:0x0275, B:115:0x02ae, B:117:0x02b7, B:119:0x02d3, B:121:0x02db, B:128:0x02f3, B:130:0x02f7, B:131:0x0317, B:133:0x0323, B:135:0x0327, B:137:0x032f, B:138:0x0332, B:147:0x0369, B:149:0x0375, B:151:0x0379, B:153:0x0390, B:152:0x0387, B:146:0x0366, B:123:0x02de, B:125:0x02e7, B:85:0x021f, B:18:0x0055, B:154:0x0393, B:155:0x039b, B:156:0x039c, B:158:0x03a4, B:161:0x03b1, B:163:0x03b5, B:165:0x03c0, B:167:0x03d5, B:173:0x03e6, B:175:0x03ea, B:177:0x03ee, B:179:0x03f4, B:181:0x03ff, B:183:0x0403, B:185:0x040b, B:191:0x041e, B:195:0x042b, B:196:0x0432, B:198:0x045f, B:200:0x0463, B:202:0x0478, B:204:0x047f, B:207:0x0495, B:209:0x0499, B:211:0x049d, B:212:0x04ac, B:203:0x047b, B:206:0x0485, B:187:0x0412, B:189:0x0416, B:190:0x041c, B:166:0x03ca, B:169:0x03d9, B:8:0x0025, B:140:0x0336, B:143:0x0351), top: B:230:0x0008, inners: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:228:0x0336 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0214 A[Catch: Exception -> 0x0052, TryCatch #2 {Exception -> 0x0052, blocks: (B:5:0x0008, B:7:0x0015, B:13:0x0049, B:15:0x004f, B:19:0x005b, B:21:0x005f, B:23:0x0068, B:24:0x006a, B:26:0x0083, B:28:0x008c, B:29:0x0095, B:33:0x009e, B:36:0x00b9, B:38:0x00bd, B:39:0x00c0, B:40:0x00c2, B:44:0x00cb, B:46:0x00d8, B:47:0x00e2, B:49:0x00e6, B:50:0x00f0, B:54:0x0112, B:56:0x0148, B:58:0x014c, B:60:0x0154, B:62:0x015a, B:64:0x01b0, B:67:0x01e8, B:70:0x01fa, B:72:0x01fd, B:74:0x0200, B:79:0x0210, B:81:0x0214, B:91:0x0235, B:94:0x0242, B:96:0x024d, B:98:0x0259, B:100:0x025d, B:102:0x0263, B:104:0x026e, B:107:0x0277, B:111:0x0284, B:112:0x028b, B:113:0x02a2, B:106:0x0275, B:115:0x02ae, B:117:0x02b7, B:119:0x02d3, B:121:0x02db, B:128:0x02f3, B:130:0x02f7, B:131:0x0317, B:133:0x0323, B:135:0x0327, B:137:0x032f, B:138:0x0332, B:147:0x0369, B:149:0x0375, B:151:0x0379, B:153:0x0390, B:152:0x0387, B:146:0x0366, B:123:0x02de, B:125:0x02e7, B:85:0x021f, B:18:0x0055, B:154:0x0393, B:155:0x039b, B:156:0x039c, B:158:0x03a4, B:161:0x03b1, B:163:0x03b5, B:165:0x03c0, B:167:0x03d5, B:173:0x03e6, B:175:0x03ea, B:177:0x03ee, B:179:0x03f4, B:181:0x03ff, B:183:0x0403, B:185:0x040b, B:191:0x041e, B:195:0x042b, B:196:0x0432, B:198:0x045f, B:200:0x0463, B:202:0x0478, B:204:0x047f, B:207:0x0495, B:209:0x0499, B:211:0x049d, B:212:0x04ac, B:203:0x047b, B:206:0x0485, B:187:0x0412, B:189:0x0416, B:190:0x041c, B:166:0x03ca, B:169:0x03d9, B:8:0x0025, B:140:0x0336, B:143:0x0351), top: B:230:0x0008, inners: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x021f A[Catch: Exception -> 0x0052, TryCatch #2 {Exception -> 0x0052, blocks: (B:5:0x0008, B:7:0x0015, B:13:0x0049, B:15:0x004f, B:19:0x005b, B:21:0x005f, B:23:0x0068, B:24:0x006a, B:26:0x0083, B:28:0x008c, B:29:0x0095, B:33:0x009e, B:36:0x00b9, B:38:0x00bd, B:39:0x00c0, B:40:0x00c2, B:44:0x00cb, B:46:0x00d8, B:47:0x00e2, B:49:0x00e6, B:50:0x00f0, B:54:0x0112, B:56:0x0148, B:58:0x014c, B:60:0x0154, B:62:0x015a, B:64:0x01b0, B:67:0x01e8, B:70:0x01fa, B:72:0x01fd, B:74:0x0200, B:79:0x0210, B:81:0x0214, B:91:0x0235, B:94:0x0242, B:96:0x024d, B:98:0x0259, B:100:0x025d, B:102:0x0263, B:104:0x026e, B:107:0x0277, B:111:0x0284, B:112:0x028b, B:113:0x02a2, B:106:0x0275, B:115:0x02ae, B:117:0x02b7, B:119:0x02d3, B:121:0x02db, B:128:0x02f3, B:130:0x02f7, B:131:0x0317, B:133:0x0323, B:135:0x0327, B:137:0x032f, B:138:0x0332, B:147:0x0369, B:149:0x0375, B:151:0x0379, B:153:0x0390, B:152:0x0387, B:146:0x0366, B:123:0x02de, B:125:0x02e7, B:85:0x021f, B:18:0x0055, B:154:0x0393, B:155:0x039b, B:156:0x039c, B:158:0x03a4, B:161:0x03b1, B:163:0x03b5, B:165:0x03c0, B:167:0x03d5, B:173:0x03e6, B:175:0x03ea, B:177:0x03ee, B:179:0x03f4, B:181:0x03ff, B:183:0x0403, B:185:0x040b, B:191:0x041e, B:195:0x042b, B:196:0x0432, B:198:0x045f, B:200:0x0463, B:202:0x0478, B:204:0x047f, B:207:0x0495, B:209:0x0499, B:211:0x049d, B:212:0x04ac, B:203:0x047b, B:206:0x0485, B:187:0x0412, B:189:0x0416, B:190:0x041c, B:166:0x03ca, B:169:0x03d9, B:8:0x0025, B:140:0x0336, B:143:0x0351), top: B:230:0x0008, inners: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x022f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void startUploadRequest() {
        int read;
        final byte[] bArr;
        int i;
        TLRPC$TL_upload_saveFilePart tLRPC$TL_upload_saveFilePart;
        int i2;
        boolean z;
        boolean z2;
        if (this.state != 1) {
            return;
        }
        try {
            this.started = true;
            byte[] bArr2 = null;
            if (this.stream == null) {
                File file = new File(this.uploadingFilePath);
                this.stream = new RandomAccessFile(file, "r");
                if (AndroidUtilities.isInternalUri(((Integer) FileDescriptor.class.getDeclaredMethod("getInt$", null).invoke(this.stream.getFD(), null)).intValue())) {
                    throw new Exception("trying to upload internal file");
                }
                long j = this.estimatedSize;
                if (j != 0) {
                    this.totalFileSize = j;
                } else {
                    this.totalFileSize = file.length();
                }
                if (!this.forceSmallFile && this.totalFileSize > 10485760) {
                    this.isBigFile = true;
                }
                long j2 = MessagesController.getInstance(this.currentAccount).uploadMaxFileParts;
                if (AccountInstance.getInstance(this.currentAccount).getUserConfig().isPremium() && this.totalFileSize > FileLoader.DEFAULT_MAX_FILE_SIZE) {
                    j2 = MessagesController.getInstance(this.currentAccount).uploadMaxFilePartsPremium;
                }
                long j3 = j2 * 1024;
                int max = (int) Math.max(this.slowNetwork ? 32L : 128L, ((this.totalFileSize + j3) - 1) / j3);
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
                long j4 = this.preferences.getLong(this.fileKey + "_size", 0L);
                this.uploadStartTime = (int) (System.currentTimeMillis() / 1000);
                if (!this.uploadFirstPartLater && !this.nextPartFirst && this.estimatedSize == 0 && j4 == this.totalFileSize) {
                    this.currentFileId = this.preferences.getLong(this.fileKey + "_id", 0L);
                    int i5 = this.preferences.getInt(this.fileKey + "_time", 0);
                    long j5 = this.preferences.getLong(this.fileKey + "_uploaded", 0L);
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
                        z = true;
                        if (!z && i5 != 0) {
                            z2 = this.isBigFile;
                            if (z2) {
                                if (i5 < this.uploadStartTime - 86400) {
                                    i5 = 0;
                                    if (i5 != 0) {
                                        if (j5 > 0) {
                                            this.readBytesCount = j5;
                                            this.currentPartNum = (int) (j5 / this.uploadChunkSize);
                                            if (!z2) {
                                                for (int i6 = 0; i6 < this.readBytesCount / this.uploadChunkSize; i6++) {
                                                    int read2 = this.stream.read(this.readBuffer);
                                                    int i7 = (!this.isEncrypted || read2 % 16 == 0) ? 0 : 16 - (read2 % 16);
                                                    int i8 = read2 + i7;
                                                    NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(i8);
                                                    if (read2 != this.uploadChunkSize || this.totalPartsCount == this.currentPartNum + 1) {
                                                        this.isLastPart = true;
                                                    }
                                                    int i9 = 0;
                                                    nativeByteBuffer.writeBytes(this.readBuffer, 0, read2);
                                                    if (this.isEncrypted) {
                                                        int i10 = 0;
                                                        while (i10 < i7) {
                                                            nativeByteBuffer.writeByte(i9);
                                                            i10++;
                                                            i9 = 0;
                                                        }
                                                        Utilities.aesIgeEncryption(nativeByteBuffer.buffer, this.key, this.ivChange, true, true, 0, i8);
                                                    }
                                                    nativeByteBuffer.reuse();
                                                }
                                            } else {
                                                this.stream.seek(j5);
                                                if (this.isEncrypted) {
                                                    bArr2 = null;
                                                    String string3 = this.preferences.getString(this.fileKey + "_ivc", null);
                                                    if (string3 != null) {
                                                        byte[] hexToBytes2 = Utilities.hexToBytes(string3);
                                                        this.ivChange = hexToBytes2;
                                                        if (hexToBytes2 == null || hexToBytes2.length != 32) {
                                                            this.readBytesCount = 0L;
                                                            this.currentPartNum = 0;
                                                        }
                                                        if (z) {
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
                                                                for (int i11 = 0; i11 < 4; i11++) {
                                                                    this.fingerprint |= ((digest[i11] ^ digest[i11 + 4]) & NotificationCenter.voipServiceCreated) << (i11 * 8);
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
                                                                this.readBytesCount = this.uploadChunkSize;
                                                            } else {
                                                                this.stream.seek(1024L);
                                                                this.readBytesCount = 1024L;
                                                            }
                                                            this.currentPartNum = 1;
                                                        }
                                                    } else {
                                                        this.readBytesCount = 0L;
                                                        this.currentPartNum = 0;
                                                    }
                                                    z = true;
                                                    if (z) {
                                                    }
                                                    if (this.isEncrypted) {
                                                    }
                                                    this.uploadedBytesCount = this.readBytesCount;
                                                    this.lastSavedPartNum = this.currentPartNum;
                                                    if (this.uploadFirstPartLater) {
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    bArr2 = null;
                                    if (z) {
                                    }
                                    if (this.isEncrypted) {
                                    }
                                    this.uploadedBytesCount = this.readBytesCount;
                                    this.lastSavedPartNum = this.currentPartNum;
                                    if (this.uploadFirstPartLater) {
                                    }
                                }
                            }
                            if (!z2) {
                            }
                            if (i5 != 0) {
                            }
                            bArr2 = null;
                            if (z) {
                            }
                            if (this.isEncrypted) {
                            }
                            this.uploadedBytesCount = this.readBytesCount;
                            this.lastSavedPartNum = this.currentPartNum;
                            if (this.uploadFirstPartLater) {
                            }
                        }
                    }
                    z = false;
                    if (!z) {
                        z2 = this.isBigFile;
                        if (z2) {
                        }
                        if (!z2) {
                        }
                        if (i5 != 0) {
                        }
                        bArr2 = null;
                        if (z) {
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
                z = true;
                if (z) {
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
                    TLRPC$TL_upload_saveBigFilePart tLRPC$TL_upload_saveBigFilePart = new TLRPC$TL_upload_saveBigFilePart();
                    i = this.currentPartNum;
                    tLRPC$TL_upload_saveBigFilePart.file_part = i;
                    tLRPC$TL_upload_saveBigFilePart.file_id = this.currentFileId;
                    if (this.estimatedSize != 0) {
                        tLRPC$TL_upload_saveBigFilePart.file_total_parts = -1;
                    } else {
                        tLRPC$TL_upload_saveBigFilePart.file_total_parts = this.totalPartsCount;
                    }
                    tLRPC$TL_upload_saveBigFilePart.bytes = nativeByteBuffer2;
                    tLRPC$TL_upload_saveFilePart = tLRPC$TL_upload_saveBigFilePart;
                } else {
                    TLRPC$TL_upload_saveFilePart tLRPC$TL_upload_saveFilePart2 = new TLRPC$TL_upload_saveFilePart();
                    i = this.currentPartNum;
                    tLRPC$TL_upload_saveFilePart2.file_part = i;
                    tLRPC$TL_upload_saveFilePart2.file_id = this.currentFileId;
                    tLRPC$TL_upload_saveFilePart2.bytes = nativeByteBuffer2;
                    tLRPC$TL_upload_saveFilePart = tLRPC$TL_upload_saveFilePart2;
                }
                TLRPC$TL_upload_saveFilePart tLRPC$TL_upload_saveFilePart3 = tLRPC$TL_upload_saveFilePart;
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
                final long j6 = i17 + i12;
                final int objectSize = tLRPC$TL_upload_saveFilePart3.getObjectSize() + 4;
                final int i19 = this.operationGuid;
                int i20 = this.slowNetwork ? 4 : ((i18 % 4) << 16) | 4;
                System.currentTimeMillis();
                final int[] iArr = new int[1];
                iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_upload_saveFilePart3, new RequestDelegate() { // from class: org.telegram.messenger.FileUploadOperation$$ExternalSyntheticLambda3
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        FileUploadOperation.this.lambda$startUploadRequest$4(i19, iArr, objectSize, bArr, i18, i12, i17, j6, tLObject, tLRPC$TL_error);
                    }
                }, null, new WriteToSocketDelegate() { // from class: org.telegram.messenger.FileUploadOperation$$ExternalSyntheticLambda4
                    @Override // org.telegram.tgnet.WriteToSocketDelegate
                    public final void run() {
                        FileUploadOperation.this.lambda$startUploadRequest$6();
                    }
                }, this.forceSmallFile ? 4 : 0, ConnectionsManager.DEFAULT_DATACENTER_ID, i20, true);
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("debug_uploading:  send reqId " + iArr[0] + " " + this.uploadingFilePath + " file_part=" + i17 + " isBig=" + this.isBigFile + " file_id=" + this.currentFileId);
                }
                this.requestTokens.put(i18, iArr[0]);
            }
        } catch (Exception e2) {
            FileLog.e(e2);
            this.state = 4;
            this.delegate.didFailedUploadingFile(this);
            cleanup();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0099, code lost:
        if (r21.uploadingFilePath.toLowerCase().endsWith(r11) != false) goto L119;
     */
    /* JADX WARN: Removed duplicated region for block: B:113:0x02bc  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00b9  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00c9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$startUploadRequest$4(int i, int[] iArr, int i2, byte[] bArr, int i3, int i4, int i5, long j, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        String str;
        String str2;
        long j2;
        TLRPC$InputEncryptedFile tLRPC$InputEncryptedFile;
        TLRPC$InputFile tLRPC$TL_inputFile;
        byte[] bArr2 = bArr;
        if (i != this.operationGuid) {
            return;
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("debug_uploading:  response reqId " + iArr[0] + " time" + this.uploadingFilePath);
        }
        int currentNetworkType = tLObject != null ? tLObject.networkType : ApplicationLoader.getCurrentNetworkType();
        int i6 = this.currentType;
        if (i6 == 50331648) {
            str = "m4a";
            StatsController.getInstance(this.currentAccount).incrementSentBytesCount(currentNetworkType, 3, i2);
        } else {
            str = "m4a";
            if (i6 == 33554432) {
                StatsController.getInstance(this.currentAccount).incrementSentBytesCount(currentNetworkType, 2, i2);
            } else if (i6 == 16777216) {
                StatsController.getInstance(this.currentAccount).incrementSentBytesCount(currentNetworkType, 4, i2);
            } else if (i6 == 67108864) {
                String str3 = this.uploadingFilePath;
                if (str3 != null) {
                    if (str3.toLowerCase().endsWith("mp3")) {
                        str2 = str;
                    } else {
                        str2 = str;
                    }
                    StatsController.getInstance(this.currentAccount).incrementSentBytesCount(currentNetworkType, 7, i2);
                    if (bArr2 != null) {
                        this.freeRequestIvs.add(bArr2);
                    }
                    this.requestTokens.delete(i3);
                    if (tLObject instanceof TLRPC$TL_boolTrue) {
                        if (this.state != 1) {
                            return;
                        }
                        this.uploadedBytesCount += i4;
                        long j3 = this.estimatedSize;
                        if (j3 != 0) {
                            j2 = Math.max(this.availableSize, j3);
                        } else {
                            j2 = this.totalFileSize;
                        }
                        this.delegate.didChangedUploadProgress(this, this.uploadedBytesCount, j2);
                        int i7 = this.currentUploadRequetsCount - 1;
                        this.currentUploadRequetsCount = i7;
                        if (this.isLastPart && i7 == 0 && this.state == 1) {
                            this.state = 3;
                            if (this.key == null) {
                                if (this.isBigFile) {
                                    tLRPC$TL_inputFile = new TLRPC$TL_inputFileBig();
                                } else {
                                    tLRPC$TL_inputFile = new TLRPC$TL_inputFile();
                                    tLRPC$TL_inputFile.md5_checksum = "";
                                }
                                tLRPC$TL_inputFile.parts = this.currentPartNum;
                                tLRPC$TL_inputFile.id = this.currentFileId;
                                String str4 = this.uploadingFilePath;
                                tLRPC$TL_inputFile.name = str4.substring(str4.lastIndexOf("/") + 1);
                                this.delegate.didFinishUploadingFile(this, tLRPC$TL_inputFile, null, null, null);
                                cleanup();
                            } else {
                                if (this.isBigFile) {
                                    tLRPC$InputEncryptedFile = new TLRPC$InputEncryptedFile() { // from class: org.telegram.tgnet.TLRPC$TL_inputEncryptedFileBigUploaded
                                        @Override // org.telegram.tgnet.TLObject
                                        public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
                                            this.id = abstractSerializedData.readInt64(z);
                                            this.parts = abstractSerializedData.readInt32(z);
                                            this.key_fingerprint = abstractSerializedData.readInt32(z);
                                        }

                                        @Override // org.telegram.tgnet.TLObject
                                        public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                                            abstractSerializedData.writeInt32(767652808);
                                            abstractSerializedData.writeInt64(this.id);
                                            abstractSerializedData.writeInt32(this.parts);
                                            abstractSerializedData.writeInt32(this.key_fingerprint);
                                        }
                                    };
                                } else {
                                    tLRPC$InputEncryptedFile = new TLRPC$InputEncryptedFile() { // from class: org.telegram.tgnet.TLRPC$TL_inputEncryptedFileUploaded
                                        @Override // org.telegram.tgnet.TLObject
                                        public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
                                            this.id = abstractSerializedData.readInt64(z);
                                            this.parts = abstractSerializedData.readInt32(z);
                                            this.md5_checksum = abstractSerializedData.readString(z);
                                            this.key_fingerprint = abstractSerializedData.readInt32(z);
                                        }

                                        @Override // org.telegram.tgnet.TLObject
                                        public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                                            abstractSerializedData.writeInt32(1690108678);
                                            abstractSerializedData.writeInt64(this.id);
                                            abstractSerializedData.writeInt32(this.parts);
                                            abstractSerializedData.writeString(this.md5_checksum);
                                            abstractSerializedData.writeInt32(this.key_fingerprint);
                                        }
                                    };
                                    tLRPC$InputEncryptedFile.md5_checksum = "";
                                }
                                tLRPC$InputEncryptedFile.parts = this.currentPartNum;
                                tLRPC$InputEncryptedFile.id = this.currentFileId;
                                tLRPC$InputEncryptedFile.key_fingerprint = this.fingerprint;
                                this.delegate.didFinishUploadingFile(this, null, tLRPC$InputEncryptedFile, this.key, this.iv);
                                cleanup();
                            }
                            int i8 = this.currentType;
                            if (i8 == 50331648) {
                                StatsController.getInstance(this.currentAccount).incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), 3, 1);
                                return;
                            } else if (i8 == 33554432) {
                                StatsController.getInstance(this.currentAccount).incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), 2, 1);
                                return;
                            } else if (i8 == 16777216) {
                                StatsController.getInstance(this.currentAccount).incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), 4, 1);
                                return;
                            } else if (i8 == 67108864) {
                                String str5 = this.uploadingFilePath;
                                if (str5 != null && (str5.toLowerCase().endsWith("mp3") || this.uploadingFilePath.toLowerCase().endsWith(str2))) {
                                    StatsController.getInstance(this.currentAccount).incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), 7, 1);
                                    return;
                                } else {
                                    StatsController.getInstance(this.currentAccount).incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), 5, 1);
                                    return;
                                }
                            } else {
                                return;
                            }
                        } else if (i7 < this.maxRequestsCount) {
                            if (this.estimatedSize == 0 && !this.uploadFirstPartLater && !this.nextPartFirst) {
                                if (this.saveInfoTimes >= 4) {
                                    this.saveInfoTimes = 0;
                                }
                                int i9 = this.lastSavedPartNum;
                                if (i5 == i9) {
                                    this.lastSavedPartNum = i9 + 1;
                                    long j4 = j;
                                    while (true) {
                                        UploadCachedResult uploadCachedResult = this.cachedResults.get(this.lastSavedPartNum);
                                        if (uploadCachedResult == null) {
                                            break;
                                        }
                                        j4 = uploadCachedResult.bytesOffset;
                                        bArr2 = uploadCachedResult.iv;
                                        this.cachedResults.remove(this.lastSavedPartNum);
                                        this.lastSavedPartNum++;
                                    }
                                    boolean z = this.isBigFile;
                                    if ((z && j4 % 1048576 == 0) || (!z && this.saveInfoTimes == 0)) {
                                        SharedPreferences.Editor edit = this.preferences.edit();
                                        edit.putLong(this.fileKey + "_uploaded", j4);
                                        if (this.isEncrypted) {
                                            edit.putString(this.fileKey + "_ivc", Utilities.bytesToHex(bArr2));
                                        }
                                        edit.commit();
                                    }
                                } else {
                                    UploadCachedResult uploadCachedResult2 = new UploadCachedResult();
                                    uploadCachedResult2.bytesOffset = j;
                                    if (bArr2 != null) {
                                        uploadCachedResult2.iv = new byte[32];
                                        System.arraycopy(bArr2, 0, uploadCachedResult2.iv, 0, 32);
                                    }
                                    this.cachedResults.put(i5, uploadCachedResult2);
                                }
                                this.saveInfoTimes++;
                            }
                            startUploadRequest();
                            return;
                        } else {
                            return;
                        }
                    }
                    this.state = 4;
                    this.delegate.didFailedUploadingFile(this);
                    cleanup();
                    return;
                }
                str2 = str;
                StatsController.getInstance(this.currentAccount).incrementSentBytesCount(currentNetworkType, 5, i2);
                if (bArr2 != null) {
                }
                this.requestTokens.delete(i3);
                if (tLObject instanceof TLRPC$TL_boolTrue) {
                }
            }
        }
        str2 = str;
        if (bArr2 != null) {
        }
        this.requestTokens.delete(i3);
        if (tLObject instanceof TLRPC$TL_boolTrue) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startUploadRequest$6() {
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileUploadOperation$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                FileUploadOperation.this.lambda$startUploadRequest$5();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startUploadRequest$5() {
        if (this.currentUploadRequetsCount < this.maxRequestsCount) {
            startUploadRequest();
        }
    }
}
