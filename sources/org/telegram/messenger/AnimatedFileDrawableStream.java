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
    private int loadingPriority;
    private ImageLocation location;
    private Object parentObject;
    private boolean preview;
    private final Object sync = new Object();
    private boolean waitingForLoad;

    public AnimatedFileDrawableStream(TLRPC$Document tLRPC$Document, ImageLocation imageLocation, Object obj, int i, boolean z, int i2) {
        this.document = tLRPC$Document;
        this.location = imageLocation;
        this.parentObject = obj;
        this.currentAccount = i;
        this.preview = z;
        this.loadingPriority = i2;
        this.loadOperation = FileLoader.getInstance(i).loadStreamFile(this, this.document, this.location, this.parentObject, 0L, this.preview, i2);
    }

    public boolean isFinishedLoadingFile() {
        return this.finishedLoadingFile;
    }

    public String getFinishedFilePath() {
        return this.finishedFilePath;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0081 A[Catch: all -> 0x00ab, TRY_ENTER, TryCatch #2 {Exception -> 0x00b6, blocks: (B:15:0x0025, B:17:0x002a, B:19:0x0030, B:22:0x0042, B:23:0x0044, B:30:0x004f, B:32:0x0057, B:34:0x005d, B:39:0x007e, B:40:0x0080, B:48:0x0092, B:50:0x0096, B:51:0x00a1, B:38:0x0064, B:41:0x0081, B:43:0x0085, B:44:0x0088, B:46:0x008a, B:47:0x0091, B:24:0x0045, B:26:0x0049, B:27:0x004c, B:29:0x004e), top: B:74:0x0025 }] */
    /* JADX WARN: Type inference failed for: r13v3 */
    /* JADX WARN: Type inference failed for: r13v4, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r13v5 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int read(int i, int i2) {
        ?? r13;
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
                            synchronized (this.sync) {
                                if (this.canceled) {
                                    cancelLoadingInternal();
                                    return 0;
                                }
                                if (!this.loadOperation.isPaused() && this.lastOffset == j3 && !this.preview) {
                                    r13 = 1;
                                    synchronized (this.sync) {
                                        if (this.canceled) {
                                            cancelLoadingInternal();
                                            return 0;
                                        }
                                        this.countDownLatch = new CountDownLatch(r13);
                                    }
                                    if (!this.preview) {
                                        FileLoader.getInstance(this.currentAccount).setLoadingVideo(this.document, false, r13);
                                    }
                                    this.waitingForLoad = r13;
                                    this.countDownLatch.await();
                                    this.waitingForLoad = false;
                                }
                                r13 = 1;
                                FileLoader.getInstance(this.currentAccount).loadStreamFile(this, this.document, this.location, this.parentObject, j3, this.preview, this.loadingPriority);
                                synchronized (this.sync) {
                                }
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
        if (this.canceled) {
            return;
        }
        synchronized (this.sync) {
            CountDownLatch countDownLatch = this.countDownLatch;
            if (countDownLatch != null) {
                countDownLatch.countDown();
                if (z && !this.canceled && !this.preview) {
                    FileLoader.getInstance(this.currentAccount).removeLoadingVideo(this.document, false, true);
                }
            }
            if (z) {
                cancelLoadingInternal();
            }
            this.canceled = true;
        }
    }

    private void cancelLoadingInternal() {
        FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.document);
        if (this.location != null) {
            FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.location.location, "mp4");
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
