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

    /* JADX WARN: Removed duplicated region for block: B:35:0x0072 A[Catch: all -> 0x00a4, TRY_ENTER, TryCatch #1 {Exception -> 0x00ac, blocks: (B:18:0x0025, B:20:0x002a, B:22:0x0030, B:25:0x0042, B:27:0x004a, B:29:0x0050, B:33:0x006f, B:34:0x0071, B:39:0x008b, B:41:0x008f, B:42:0x009a, B:50:0x0057, B:35:0x0072, B:46:0x0076, B:47:0x0081, B:37:0x0083, B:38:0x008a), top: B:17:0x0025 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int read(int i, int i2) {
        boolean z;
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
                                z = true;
                                synchronized (this.sync) {
                                    if (this.canceled) {
                                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.document);
                                        return 0;
                                    }
                                    int i3 = z ? 1 : 0;
                                    int i4 = z ? 1 : 0;
                                    this.countDownLatch = new CountDownLatch(i3);
                                }
                                if (!this.preview) {
                                    FileLoader.getInstance(this.currentAccount).setLoadingVideo(this.document, false, z);
                                }
                                this.waitingForLoad = z;
                                this.countDownLatch.await();
                                this.waitingForLoad = false;
                            }
                            z = true;
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
