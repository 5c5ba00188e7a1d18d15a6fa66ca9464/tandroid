package org.telegram.messenger;

import java.util.concurrent.CountDownLatch;
import org.telegram.tgnet.TLRPC$Document;
/* loaded from: classes.dex */
public class AnimatedFileDrawableStream implements FileLoadOperationStream {
    private volatile boolean canceled;
    private CountDownLatch countDownLatch;
    private int currentAccount;
    private TLRPC$Document document;
    private String finishedFilePath;
    private boolean finishedLoadingFile;
    private long lastOffset;
    private final FileLoadOperation loadOperation;
    private ImageLocation location;
    private Object parentObject;
    private boolean preview;
    private final Object sync = new Object();
    private boolean waitingForLoad;

    public AnimatedFileDrawableStream(TLRPC$Document tLRPC$Document, ImageLocation imageLocation, Object obj, int i, boolean z) {
        this.document = tLRPC$Document;
        this.location = imageLocation;
        this.parentObject = obj;
        this.currentAccount = i;
        this.preview = z;
        this.loadOperation = FileLoader.getInstance(i).loadStreamFile(this, this.document, this.location, this.parentObject, 0L, this.preview);
    }

    public boolean isFinishedLoadingFile() {
        return this.finishedLoadingFile;
    }

    public String getFinishedFilePath() {
        return this.finishedFilePath;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0072 A[Catch: all -> 0x00a4, TRY_ENTER, TryCatch #1 {Exception -> 0x00ac, blocks: (B:15:0x0025, B:17:0x002a, B:19:0x0030, B:22:0x0042, B:24:0x004a, B:26:0x0050, B:31:0x006f, B:32:0x0071, B:40:0x008b, B:42:0x008f, B:43:0x009a, B:30:0x0057, B:33:0x0072, B:35:0x0076, B:36:0x0081, B:38:0x0083, B:39:0x008a), top: B:63:0x0025 }] */
    /* JADX WARN: Type inference failed for: r12v3 */
    /* JADX WARN: Type inference failed for: r12v4, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r12v5 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int read(int i, int i2) {
        ?? r12;
        synchronized (this.sync) {
            if (this.canceled) {
                return 0;
            }
            if (i2 == 0) {
                return 0;
            }
            long j = 0;
            for (long j2 = 0; j == j2; j2 = 0) {
                try {
                    long j3 = i;
                    long[] downloadedLengthFromOffset = this.loadOperation.getDownloadedLengthFromOffset(j3, i2);
                    long j4 = downloadedLengthFromOffset[0];
                    try {
                        if (!this.finishedLoadingFile && downloadedLengthFromOffset[1] != j2) {
                            this.finishedLoadingFile = true;
                            this.finishedFilePath = this.loadOperation.getCacheFileFinal().getAbsolutePath();
                        }
                        if (j4 == j2) {
                            if (!this.loadOperation.isPaused() && this.lastOffset == j3 && !this.preview) {
                                r12 = 1;
                                synchronized (this.sync) {
                                    if (this.canceled) {
                                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.document);
                                        return 0;
                                    }
                                    this.countDownLatch = new CountDownLatch(r12);
                                }
                                if (!this.preview) {
                                    FileLoader.getInstance(this.currentAccount).setLoadingVideo(this.document, false, r12);
                                }
                                this.waitingForLoad = r12;
                                this.countDownLatch.await();
                                this.waitingForLoad = false;
                            }
                            r12 = 1;
                            FileLoader.getInstance(this.currentAccount).loadStreamFile(this, this.document, this.location, this.parentObject, j3, this.preview);
                            synchronized (this.sync) {
                            }
                        }
                        j = j4;
                    } catch (Exception e) {
                        e = e;
                        j = j4;
                        FileLog.e((Throwable) e, false);
                        return (int) j;
                    }
                } catch (Exception e2) {
                    e = e2;
                }
            }
            this.lastOffset = i + j;
            return (int) j;
        }
    }

    public void cancel() {
        cancel(true);
    }

    public void cancel(boolean z) {
        synchronized (this.sync) {
            CountDownLatch countDownLatch = this.countDownLatch;
            if (countDownLatch != null) {
                countDownLatch.countDown();
                if (z && !this.canceled && !this.preview) {
                    FileLoader.getInstance(this.currentAccount).removeLoadingVideo(this.document, false, true);
                }
            }
            this.canceled = true;
        }
    }

    public void reset() {
        synchronized (this.sync) {
            this.canceled = false;
        }
    }

    public TLRPC$Document getDocument() {
        return this.document;
    }

    public ImageLocation getLocation() {
        return this.location;
    }

    public Object getParentObject() {
        return this.document;
    }

    public boolean isPreview() {
        return this.preview;
    }

    public int getCurrentAccount() {
        return this.currentAccount;
    }

    public boolean isWaitingForLoad() {
        return this.waitingForLoad;
    }

    @Override // org.telegram.messenger.FileLoadOperationStream
    public void newDataAvailable() {
        CountDownLatch countDownLatch = this.countDownLatch;
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }
}
