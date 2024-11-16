package org.telegram.ui.Components;

import android.graphics.Bitmap;
import android.text.TextUtils;
import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Cells.ChatMessageCell;

/* loaded from: classes3.dex */
public class SlotsDrawable extends RLottieDrawable {
    private ReelValue center;
    private int[] frameCounts;
    private int[] frameNums;
    private ReelValue left;
    private long[] nativePtrs;
    private boolean playWinAnimation;
    private ReelValue right;
    private int[] secondFrameCounts;
    private int[] secondFrameNums;
    private long[] secondNativePtrs;

    enum ReelValue {
        bar,
        berries,
        lemon,
        seven,
        sevenWin
    }

    public SlotsDrawable(String str, int i, int i2) {
        super(str, i, i2);
        this.nativePtrs = new long[5];
        this.frameCounts = new int[5];
        this.frameNums = new int[5];
        this.secondNativePtrs = new long[3];
        this.secondFrameCounts = new int[3];
        this.secondFrameNums = new int[3];
        this.loadFrameRunnable = new Runnable() { // from class: org.telegram.ui.Components.SlotsDrawable$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SlotsDrawable.this.lambda$new$0();
            }
        };
    }

    private void init(int i) {
        int i2 = i - 1;
        ReelValue reelValue = reelValue(i2 & 3);
        ReelValue reelValue2 = reelValue((i2 >> 2) & 3);
        ReelValue reelValue3 = reelValue(i2 >> 4);
        ReelValue reelValue4 = ReelValue.seven;
        if (reelValue == reelValue4 && reelValue2 == reelValue4 && reelValue3 == reelValue4) {
            reelValue = ReelValue.sevenWin;
            reelValue3 = reelValue;
            reelValue2 = reelValue3;
        }
        this.left = reelValue;
        this.center = reelValue2;
        this.right = reelValue3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        int frame;
        if (this.isRecycled) {
            return;
        }
        if (this.nativePtr == 0 || (this.isDice == 2 && this.secondNativePtr == 0)) {
            CountDownLatch countDownLatch = this.frameWaitSync;
            if (countDownLatch != null) {
                countDownLatch.countDown();
            }
            RLottieDrawable.uiHandler.post(this.uiRunnableNoFrame);
            return;
        }
        if (this.backgroundBitmap == null) {
            try {
                this.backgroundBitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888);
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }
        if (this.backgroundBitmap != null) {
            try {
                if (this.isDice == 1) {
                    int i = 0;
                    frame = -1;
                    while (true) {
                        long[] jArr = this.nativePtrs;
                        if (i >= jArr.length) {
                            break;
                        }
                        frame = RLottieDrawable.getFrame(jArr[i], this.frameNums[i], this.backgroundBitmap, this.width, this.height, this.backgroundBitmap.getRowBytes(), i == 0);
                        if (i != 0) {
                            int[] iArr = this.frameNums;
                            int i2 = iArr[i] + 1;
                            if (i2 < this.frameCounts[i]) {
                                iArr[i] = i2;
                            } else if (i != 4) {
                                iArr[i] = 0;
                                this.nextFrameIsLast = false;
                                if (this.secondNativePtr != 0) {
                                    this.isDice = 2;
                                }
                            }
                        }
                        i++;
                    }
                } else {
                    if (this.setLastFrame) {
                        int i3 = 0;
                        while (true) {
                            int[] iArr2 = this.secondFrameNums;
                            if (i3 >= iArr2.length) {
                                break;
                            }
                            iArr2[i3] = this.secondFrameCounts[i3] - 1;
                            i3++;
                        }
                    }
                    if (this.playWinAnimation) {
                        int[] iArr3 = this.frameNums;
                        int i4 = iArr3[0] + 1;
                        if (i4 < this.frameCounts[0]) {
                            iArr3[0] = i4;
                        } else {
                            iArr3[0] = -1;
                        }
                    }
                    RLottieDrawable.getFrame(this.nativePtrs[0], Math.max(this.frameNums[0], 0), this.backgroundBitmap, this.width, this.height, this.backgroundBitmap.getRowBytes(), true);
                    int i5 = 0;
                    while (true) {
                        long[] jArr2 = this.secondNativePtrs;
                        if (i5 >= jArr2.length) {
                            break;
                        }
                        long j = jArr2[i5];
                        int i6 = this.secondFrameNums[i5];
                        if (i6 < 0) {
                            i6 = this.secondFrameCounts[i5] - 1;
                        }
                        RLottieDrawable.getFrame(j, i6, this.backgroundBitmap, this.width, this.height, this.backgroundBitmap.getRowBytes(), false);
                        if (!this.nextFrameIsLast) {
                            int[] iArr4 = this.secondFrameNums;
                            int i7 = iArr4[i5] + 1;
                            if (i7 < this.secondFrameCounts[i5]) {
                                iArr4[i5] = i7;
                            } else {
                                iArr4[i5] = -1;
                            }
                        }
                        i5++;
                    }
                    frame = RLottieDrawable.getFrame(this.nativePtrs[4], this.frameNums[4], this.backgroundBitmap, this.width, this.height, this.backgroundBitmap.getRowBytes(), false);
                    int[] iArr5 = this.frameNums;
                    int i8 = iArr5[4] + 1;
                    if (i8 < this.frameCounts[4]) {
                        iArr5[4] = i8;
                    }
                    int[] iArr6 = this.secondFrameNums;
                    if (iArr6[0] == -1 && iArr6[1] == -1 && iArr6[2] == -1) {
                        this.nextFrameIsLast = true;
                        this.autoRepeatPlayCount++;
                    }
                    ReelValue reelValue = this.left;
                    ReelValue reelValue2 = this.right;
                    if (reelValue != reelValue2 || reelValue2 != this.center) {
                        this.frameNums[0] = -1;
                    } else if (this.secondFrameNums[0] == this.secondFrameCounts[0] - 100) {
                        this.playWinAnimation = true;
                        if (reelValue == ReelValue.sevenWin) {
                            WeakReference weakReference = this.onFinishCallback;
                            Runnable runnable = weakReference == null ? null : (Runnable) weakReference.get();
                            if (runnable != null) {
                                AndroidUtilities.runOnUIThread(runnable);
                            }
                        }
                    }
                }
                if (frame == -1) {
                    RLottieDrawable.uiHandler.post(this.uiRunnableNoFrame);
                    CountDownLatch countDownLatch2 = this.frameWaitSync;
                    if (countDownLatch2 != null) {
                        countDownLatch2.countDown();
                        return;
                    }
                    return;
                }
                this.nextRenderingBitmap = this.backgroundBitmap;
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        RLottieDrawable.uiHandler.post(this.uiRunnable);
        CountDownLatch countDownLatch3 = this.frameWaitSync;
        if (countDownLatch3 != null) {
            countDownLatch3.countDown();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setBaseDice$1() {
        this.loadingInBackground = false;
        if (this.secondLoadingInBackground || !this.destroyAfterLoading) {
            return;
        }
        recycle(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$setBaseDice$2(TLRPC.Document document, int i, MessageObject messageObject, ChatMessageCell chatMessageCell, TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
        DownloadController.getInstance(i).addLoadingFileObserver(FileLoader.getAttachFileName(document), messageObject, chatMessageCell);
        FileLoader.getInstance(i).loadFile(document, tL_messages_stickerSet, 1, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setBaseDice$3() {
        this.loadingInBackground = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setBaseDice$4(int i, ChatMessageCell chatMessageCell) {
        this.loadingInBackground = false;
        if (!this.secondLoadingInBackground && this.destroyAfterLoading) {
            recycle(true);
            return;
        }
        this.nativePtr = this.nativePtrs[0];
        DownloadController.getInstance(i).removeLoadingFileObserver(chatMessageCell);
        this.timeBetweenFrames = Math.max(16, (int) (1000.0f / this.metaData[1]));
        scheduleNextGetFrame();
        invalidateInternal();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setBaseDice$5(final TLRPC.TL_messages_stickerSet tL_messages_stickerSet, final int i, final MessageObject messageObject, final ChatMessageCell chatMessageCell) {
        Runnable runnable;
        int i2;
        if (this.destroyAfterLoading) {
            runnable = new Runnable() { // from class: org.telegram.ui.Components.SlotsDrawable$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    SlotsDrawable.this.lambda$setBaseDice$1();
                }
            };
        } else {
            int i3 = 0;
            boolean z = false;
            while (true) {
                long[] jArr = this.nativePtrs;
                if (i3 >= jArr.length) {
                    break;
                }
                if (jArr[i3] == 0) {
                    if (i3 == 0) {
                        i2 = 1;
                    } else if (i3 == 1) {
                        i2 = 8;
                    } else {
                        i2 = 2;
                        if (i3 == 2) {
                            i2 = 14;
                        } else if (i3 == 3) {
                            i2 = 20;
                        }
                    }
                    final TLRPC.Document document = tL_messages_stickerSet.documents.get(i2);
                    String readRes = AndroidUtilities.readRes(FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(document, true), 0);
                    if (TextUtils.isEmpty(readRes)) {
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.SlotsDrawable$$ExternalSyntheticLambda8
                            @Override // java.lang.Runnable
                            public final void run() {
                                SlotsDrawable.lambda$setBaseDice$2(TLRPC.Document.this, i, messageObject, chatMessageCell, tL_messages_stickerSet);
                            }
                        });
                        z = true;
                    } else {
                        this.nativePtrs[i3] = RLottieDrawable.createWithJson(readRes, "dice", this.metaData, null);
                        this.frameCounts[i3] = this.metaData[0];
                    }
                }
                i3++;
            }
            runnable = z ? new Runnable() { // from class: org.telegram.ui.Components.SlotsDrawable$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    SlotsDrawable.this.lambda$setBaseDice$3();
                }
            } : new Runnable() { // from class: org.telegram.ui.Components.SlotsDrawable$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    SlotsDrawable.this.lambda$setBaseDice$4(i, chatMessageCell);
                }
            };
        }
        AndroidUtilities.runOnUIThread(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:24:0x00c1  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x00d4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$setDiceNumber$10(final TLRPC.TL_messages_stickerSet tL_messages_stickerSet, final int i, final MessageObject messageObject, final ChatMessageCell chatMessageCell, final boolean z) {
        Runnable runnable;
        TLRPC.TL_messages_stickerSet tL_messages_stickerSet2;
        int i2;
        String readRes;
        if (this.destroyAfterLoading) {
            runnable = new Runnable() { // from class: org.telegram.ui.Components.SlotsDrawable$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    SlotsDrawable.this.lambda$setDiceNumber$6();
                }
            };
        } else {
            int i3 = 0;
            boolean z2 = false;
            while (true) {
                long[] jArr = this.secondNativePtrs;
                if (i3 >= jArr.length + 2) {
                    break;
                }
                if (i3 <= 2) {
                    if (jArr[i3] == 0) {
                        if (i3 == 0) {
                            ReelValue reelValue = this.left;
                            if (reelValue == ReelValue.bar) {
                                i2 = 5;
                            } else if (reelValue == ReelValue.berries) {
                                i2 = 6;
                            } else if (reelValue == ReelValue.lemon) {
                                i2 = 7;
                            } else {
                                if (reelValue == ReelValue.seven) {
                                    tL_messages_stickerSet2 = tL_messages_stickerSet;
                                    i2 = 4;
                                } else {
                                    tL_messages_stickerSet2 = tL_messages_stickerSet;
                                    i2 = 3;
                                }
                                final TLRPC.Document document = tL_messages_stickerSet2.documents.get(i2);
                                readRes = AndroidUtilities.readRes(FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(document, true), 0);
                                if (TextUtils.isEmpty(readRes)) {
                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.SlotsDrawable$$ExternalSyntheticLambda4
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            SlotsDrawable.lambda$setDiceNumber$7(TLRPC.Document.this, i, messageObject, chatMessageCell, tL_messages_stickerSet);
                                        }
                                    });
                                    z2 = true;
                                } else if (i3 <= 2) {
                                    this.secondNativePtrs[i3] = RLottieDrawable.createWithJson(readRes, "dice", this.metaData, null);
                                    this.secondFrameCounts[i3] = this.metaData[0];
                                } else {
                                    this.nativePtrs[i3 == 3 ? (char) 0 : (char) 4] = RLottieDrawable.createWithJson(readRes, "dice", this.metaData, null);
                                    this.frameCounts[i3 == 3 ? (char) 0 : (char) 4] = this.metaData[0];
                                }
                            }
                        } else if (i3 == 1) {
                            ReelValue reelValue2 = this.center;
                            i2 = reelValue2 == ReelValue.bar ? 11 : reelValue2 == ReelValue.berries ? 12 : reelValue2 == ReelValue.lemon ? 13 : reelValue2 == ReelValue.seven ? 10 : 9;
                        } else {
                            ReelValue reelValue3 = this.right;
                            i2 = reelValue3 == ReelValue.bar ? 17 : reelValue3 == ReelValue.berries ? 18 : reelValue3 == ReelValue.lemon ? 19 : reelValue3 == ReelValue.seven ? 16 : 15;
                        }
                        tL_messages_stickerSet2 = tL_messages_stickerSet;
                        final TLRPC.Document document2 = tL_messages_stickerSet2.documents.get(i2);
                        readRes = AndroidUtilities.readRes(FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(document2, true), 0);
                        if (TextUtils.isEmpty(readRes)) {
                        }
                    }
                } else if (this.nativePtrs[i3] == 0) {
                    tL_messages_stickerSet2 = tL_messages_stickerSet;
                    i2 = i3 == 3 ? 1 : 2;
                    final TLRPC.Document document22 = tL_messages_stickerSet2.documents.get(i2);
                    readRes = AndroidUtilities.readRes(FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(document22, true), 0);
                    if (TextUtils.isEmpty(readRes)) {
                    }
                }
                i3++;
            }
            runnable = z2 ? new Runnable() { // from class: org.telegram.ui.Components.SlotsDrawable$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    SlotsDrawable.this.lambda$setDiceNumber$8();
                }
            } : new Runnable() { // from class: org.telegram.ui.Components.SlotsDrawable$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    SlotsDrawable.this.lambda$setDiceNumber$9(z, i, chatMessageCell);
                }
            };
        }
        AndroidUtilities.runOnUIThread(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setDiceNumber$6() {
        this.secondLoadingInBackground = false;
        if (this.loadingInBackground || !this.destroyAfterLoading) {
            return;
        }
        recycle(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$setDiceNumber$7(TLRPC.Document document, int i, MessageObject messageObject, ChatMessageCell chatMessageCell, TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
        DownloadController.getInstance(i).addLoadingFileObserver(FileLoader.getAttachFileName(document), messageObject, chatMessageCell);
        FileLoader.getInstance(i).loadFile(document, tL_messages_stickerSet, 1, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setDiceNumber$8() {
        this.secondLoadingInBackground = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setDiceNumber$9(boolean z, int i, ChatMessageCell chatMessageCell) {
        if (z && this.nextRenderingBitmap == null && this.renderingBitmap == null && this.loadFrameTask == null) {
            this.isDice = 2;
            this.setLastFrame = true;
        }
        this.secondLoadingInBackground = false;
        if (!this.loadingInBackground && this.destroyAfterLoading) {
            recycle(true);
            return;
        }
        this.secondNativePtr = this.secondNativePtrs[0];
        DownloadController.getInstance(i).removeLoadingFileObserver(chatMessageCell);
        this.timeBetweenFrames = Math.max(16, (int) (1000.0f / this.metaData[1]));
        scheduleNextGetFrame();
        invalidateInternal();
    }

    private ReelValue reelValue(int i) {
        return i != 0 ? i != 1 ? i != 2 ? ReelValue.seven : ReelValue.lemon : ReelValue.berries : ReelValue.bar;
    }

    @Override // org.telegram.ui.Components.RLottieDrawable
    protected void decodeFrameFinishedInternal() {
        if (this.destroyWhenDone) {
            checkRunningTasks();
            if (this.loadFrameTask == null && this.cacheGenerateTask == null) {
                int i = 0;
                int i2 = 0;
                while (true) {
                    long[] jArr = this.nativePtrs;
                    if (i2 >= jArr.length) {
                        break;
                    }
                    long j = jArr[i2];
                    if (j != 0) {
                        RLottieDrawable.destroy(j);
                        this.nativePtrs[i2] = 0;
                    }
                    i2++;
                }
                while (true) {
                    long[] jArr2 = this.secondNativePtrs;
                    if (i >= jArr2.length) {
                        break;
                    }
                    long j2 = jArr2[i];
                    if (j2 != 0) {
                        RLottieDrawable.destroy(j2);
                        this.secondNativePtrs[i] = 0;
                    }
                    i++;
                }
            }
        }
        if (this.nativePtr == 0 && this.secondNativePtr == 0) {
            recycleResources();
            return;
        }
        this.waitingForNextTask = true;
        if (!hasParentView()) {
            stop();
        }
        scheduleNextGetFrame();
    }

    @Override // org.telegram.ui.Components.RLottieDrawable
    public void recycle(boolean z) {
        int i = 0;
        this.isRunning = false;
        this.isRecycled = true;
        checkRunningTasks();
        if (this.loadingInBackground || this.secondLoadingInBackground) {
            this.destroyAfterLoading = true;
            return;
        }
        if (this.loadFrameTask != null || this.cacheGenerateTask != null) {
            this.destroyWhenDone = true;
            return;
        }
        int i2 = 0;
        while (true) {
            long[] jArr = this.nativePtrs;
            if (i2 >= jArr.length) {
                break;
            }
            long j = jArr[i2];
            if (j != 0) {
                if (j == this.nativePtr) {
                    this.nativePtr = 0L;
                }
                RLottieDrawable.destroy(this.nativePtrs[i2]);
                this.nativePtrs[i2] = 0;
            }
            i2++;
        }
        while (true) {
            long[] jArr2 = this.secondNativePtrs;
            if (i >= jArr2.length) {
                recycleResources();
                return;
            }
            long j2 = jArr2[i];
            if (j2 != 0) {
                if (j2 == this.secondNativePtr) {
                    this.secondNativePtr = 0L;
                }
                RLottieDrawable.destroy(this.secondNativePtrs[i]);
                this.secondNativePtrs[i] = 0;
            }
            i++;
        }
    }

    public boolean setBaseDice(final ChatMessageCell chatMessageCell, final TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
        if (this.nativePtr == 0 && !this.loadingInBackground) {
            this.loadingInBackground = true;
            final MessageObject messageObject = chatMessageCell.getMessageObject();
            final int i = chatMessageCell.getMessageObject().currentAccount;
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Components.SlotsDrawable$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    SlotsDrawable.this.lambda$setBaseDice$5(tL_messages_stickerSet, i, messageObject, chatMessageCell);
                }
            });
        }
        return true;
    }

    public boolean setDiceNumber(final ChatMessageCell chatMessageCell, int i, final TLRPC.TL_messages_stickerSet tL_messages_stickerSet, final boolean z) {
        if (this.secondNativePtr == 0 && !this.secondLoadingInBackground) {
            init(i);
            final MessageObject messageObject = chatMessageCell.getMessageObject();
            final int i2 = chatMessageCell.getMessageObject().currentAccount;
            this.secondLoadingInBackground = true;
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Components.SlotsDrawable$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    SlotsDrawable.this.lambda$setDiceNumber$10(tL_messages_stickerSet, i2, messageObject, chatMessageCell, z);
                }
            });
        }
        return true;
    }
}
