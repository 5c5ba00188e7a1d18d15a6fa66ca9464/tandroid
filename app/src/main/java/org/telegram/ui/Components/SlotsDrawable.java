package org.telegram.ui.Components;

import android.graphics.Bitmap;
import android.text.TextUtils;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.ui.Cells.ChatMessageCell;
/* loaded from: classes3.dex */
public class SlotsDrawable extends RLottieDrawable {
    private ReelValue center;
    private ReelValue left;
    private boolean playWinAnimation;
    private ReelValue right;
    private long[] nativePtrs = new long[5];
    private int[] frameCounts = new int[5];
    private int[] frameNums = new int[5];
    private long[] secondNativePtrs = new long[3];
    private int[] secondFrameCounts = new int[3];
    private int[] secondFrameNums = new int[3];

    /* loaded from: classes3.dex */
    public enum ReelValue {
        bar,
        berries,
        lemon,
        seven,
        sevenWin
    }

    public SlotsDrawable(String str, int i, int i2) {
        super(str, i, i2);
        this.loadFrameRunnable = new SlotsDrawable$$ExternalSyntheticLambda6(this);
    }

    public /* synthetic */ void lambda$new$0() {
        int i;
        Runnable runnable;
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
                    int i2 = 0;
                    i = -1;
                    while (true) {
                        long[] jArr = this.nativePtrs;
                        if (i2 >= jArr.length) {
                            break;
                        }
                        i = RLottieDrawable.getFrame(jArr[i2], this.frameNums[i2], this.backgroundBitmap, this.width, this.height, this.backgroundBitmap.getRowBytes(), i2 == 0);
                        if (i2 != 0) {
                            int[] iArr = this.frameNums;
                            if (iArr[i2] + 1 < this.frameCounts[i2]) {
                                iArr[i2] = iArr[i2] + 1;
                            } else if (i2 != 4) {
                                iArr[i2] = 0;
                                this.nextFrameIsLast = false;
                                if (this.secondNativePtr != 0) {
                                    this.isDice = 2;
                                }
                            }
                        }
                        i2++;
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
                        if (iArr3[0] + 1 < this.frameCounts[0]) {
                            iArr3[0] = iArr3[0] + 1;
                        } else {
                            iArr3[0] = -1;
                        }
                    }
                    RLottieDrawable.getFrame(this.nativePtrs[0], Math.max(this.frameNums[0], 0), this.backgroundBitmap, this.width, this.height, this.backgroundBitmap.getRowBytes(), true);
                    int i4 = 0;
                    while (true) {
                        long[] jArr2 = this.secondNativePtrs;
                        if (i4 >= jArr2.length) {
                            break;
                        }
                        long j = jArr2[i4];
                        int[] iArr4 = this.secondFrameNums;
                        RLottieDrawable.getFrame(j, iArr4[i4] >= 0 ? iArr4[i4] : this.secondFrameCounts[i4] - 1, this.backgroundBitmap, this.width, this.height, this.backgroundBitmap.getRowBytes(), false);
                        if (!this.nextFrameIsLast) {
                            int[] iArr5 = this.secondFrameNums;
                            if (iArr5[i4] + 1 < this.secondFrameCounts[i4]) {
                                iArr5[i4] = iArr5[i4] + 1;
                            } else {
                                iArr5[i4] = -1;
                            }
                        }
                        i4++;
                    }
                    i = RLottieDrawable.getFrame(this.nativePtrs[4], this.frameNums[4], this.backgroundBitmap, this.width, this.height, this.backgroundBitmap.getRowBytes(), false);
                    int[] iArr6 = this.frameNums;
                    if (iArr6[4] + 1 < this.frameCounts[4]) {
                        iArr6[4] = iArr6[4] + 1;
                    }
                    int[] iArr7 = this.secondFrameNums;
                    if (iArr7[0] == -1 && iArr7[1] == -1 && iArr7[2] == -1) {
                        this.nextFrameIsLast = true;
                        this.autoRepeatPlayCount++;
                    }
                    ReelValue reelValue = this.left;
                    ReelValue reelValue2 = this.right;
                    if (reelValue == reelValue2 && reelValue2 == this.center) {
                        if (this.secondFrameNums[0] == this.secondFrameCounts[0] - 100) {
                            this.playWinAnimation = true;
                            if (reelValue == ReelValue.sevenWin && (runnable = this.onFinishCallback.get()) != null) {
                                AndroidUtilities.runOnUIThread(runnable);
                            }
                        }
                    } else {
                        this.frameNums[0] = -1;
                    }
                }
                if (i == -1) {
                    RLottieDrawable.uiHandler.post(this.uiRunnableNoFrame);
                    CountDownLatch countDownLatch2 = this.frameWaitSync;
                    if (countDownLatch2 == null) {
                        return;
                    }
                    countDownLatch2.countDown();
                    return;
                }
                this.nextRenderingBitmap = this.backgroundBitmap;
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        RLottieDrawable.uiHandler.post(this.uiRunnable);
        CountDownLatch countDownLatch3 = this.frameWaitSync;
        if (countDownLatch3 == null) {
            return;
        }
        countDownLatch3.countDown();
    }

    private ReelValue reelValue(int i) {
        if (i != 0) {
            if (i == 1) {
                return ReelValue.berries;
            }
            if (i == 2) {
                return ReelValue.lemon;
            }
            return ReelValue.seven;
        }
        return ReelValue.bar;
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

    public boolean setBaseDice(ChatMessageCell chatMessageCell, TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet) {
        if (this.nativePtr == 0 && !this.loadingInBackground) {
            this.loadingInBackground = true;
            MessageObject messageObject = chatMessageCell.getMessageObject();
            Utilities.globalQueue.postRunnable(new SlotsDrawable$$ExternalSyntheticLambda8(this, tLRPC$TL_messages_stickerSet, chatMessageCell.getMessageObject().currentAccount, messageObject, chatMessageCell));
        }
        return true;
    }

    public /* synthetic */ void lambda$setBaseDice$5(TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet, int i, MessageObject messageObject, ChatMessageCell chatMessageCell) {
        if (this.destroyAfterLoading) {
            AndroidUtilities.runOnUIThread(new SlotsDrawable$$ExternalSyntheticLambda5(this));
            return;
        }
        int i2 = 0;
        boolean z = false;
        while (true) {
            long[] jArr = this.nativePtrs;
            if (i2 >= jArr.length) {
                break;
            }
            if (jArr[i2] == 0) {
                int i3 = 2;
                if (i2 == 0) {
                    i3 = 1;
                } else if (i2 == 1) {
                    i3 = 8;
                } else if (i2 == 2) {
                    i3 = 14;
                } else if (i2 == 3) {
                    i3 = 20;
                }
                TLRPC$Document tLRPC$Document = tLRPC$TL_messages_stickerSet.documents.get(i3);
                String readRes = RLottieDrawable.readRes(FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(tLRPC$Document, true), 0);
                if (TextUtils.isEmpty(readRes)) {
                    AndroidUtilities.runOnUIThread(new SlotsDrawable$$ExternalSyntheticLambda0(tLRPC$Document, i, messageObject, chatMessageCell, tLRPC$TL_messages_stickerSet));
                    z = true;
                } else {
                    this.nativePtrs[i2] = RLottieDrawable.createWithJson(readRes, "dice", this.metaData, null);
                    this.frameCounts[i2] = this.metaData[0];
                }
            }
            i2++;
        }
        if (z) {
            AndroidUtilities.runOnUIThread(new SlotsDrawable$$ExternalSyntheticLambda2(this));
        } else {
            AndroidUtilities.runOnUIThread(new SlotsDrawable$$ExternalSyntheticLambda7(this, i, chatMessageCell));
        }
    }

    public /* synthetic */ void lambda$setBaseDice$1() {
        this.loadingInBackground = false;
        if (this.secondLoadingInBackground || !this.destroyAfterLoading) {
            return;
        }
        recycle();
    }

    public static /* synthetic */ void lambda$setBaseDice$2(TLRPC$Document tLRPC$Document, int i, MessageObject messageObject, ChatMessageCell chatMessageCell, TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet) {
        DownloadController.getInstance(i).addLoadingFileObserver(FileLoader.getAttachFileName(tLRPC$Document), messageObject, chatMessageCell);
        FileLoader.getInstance(i).loadFile(tLRPC$Document, tLRPC$TL_messages_stickerSet, 1, 1);
    }

    public /* synthetic */ void lambda$setBaseDice$3() {
        this.loadingInBackground = false;
    }

    public /* synthetic */ void lambda$setBaseDice$4(int i, ChatMessageCell chatMessageCell) {
        this.loadingInBackground = false;
        if (!this.secondLoadingInBackground && this.destroyAfterLoading) {
            recycle();
            return;
        }
        this.nativePtr = this.nativePtrs[0];
        DownloadController.getInstance(i).removeLoadingFileObserver(chatMessageCell);
        this.timeBetweenFrames = Math.max(16, (int) (1000.0f / this.metaData[1]));
        scheduleNextGetFrame();
        invalidateInternal();
    }

    public boolean setDiceNumber(ChatMessageCell chatMessageCell, int i, TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet, boolean z) {
        if (this.secondNativePtr == 0 && !this.secondLoadingInBackground) {
            init(i);
            MessageObject messageObject = chatMessageCell.getMessageObject();
            int i2 = chatMessageCell.getMessageObject().currentAccount;
            this.secondLoadingInBackground = true;
            Utilities.globalQueue.postRunnable(new SlotsDrawable$$ExternalSyntheticLambda9(this, tLRPC$TL_messages_stickerSet, i2, messageObject, chatMessageCell, z));
        }
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:63:0x00c5  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x00d8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$setDiceNumber$10(TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet, int i, MessageObject messageObject, ChatMessageCell chatMessageCell, boolean z) {
        TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet2;
        int i2;
        String readRes;
        if (this.destroyAfterLoading) {
            AndroidUtilities.runOnUIThread(new SlotsDrawable$$ExternalSyntheticLambda3(this));
            return;
        }
        int i3 = 0;
        boolean z2 = false;
        while (true) {
            long[] jArr = this.secondNativePtrs;
            if (i3 >= jArr.length + 2) {
                break;
            }
            char c = 4;
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
                                tLRPC$TL_messages_stickerSet2 = tLRPC$TL_messages_stickerSet;
                                i2 = 4;
                            } else {
                                tLRPC$TL_messages_stickerSet2 = tLRPC$TL_messages_stickerSet;
                                i2 = 3;
                            }
                            TLRPC$Document tLRPC$Document = tLRPC$TL_messages_stickerSet2.documents.get(i2);
                            readRes = RLottieDrawable.readRes(FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(tLRPC$Document, true), 0);
                            if (TextUtils.isEmpty(readRes)) {
                                AndroidUtilities.runOnUIThread(new SlotsDrawable$$ExternalSyntheticLambda1(tLRPC$Document, i, messageObject, chatMessageCell, tLRPC$TL_messages_stickerSet));
                                z2 = true;
                            } else if (i3 <= 2) {
                                this.secondNativePtrs[i3] = RLottieDrawable.createWithJson(readRes, "dice", this.metaData, null);
                                this.secondFrameCounts[i3] = this.metaData[0];
                            } else {
                                this.nativePtrs[i3 == 3 ? (char) 0 : (char) 4] = RLottieDrawable.createWithJson(readRes, "dice", this.metaData, null);
                                int[] iArr = this.frameCounts;
                                if (i3 == 3) {
                                    c = 0;
                                }
                                iArr[c] = this.metaData[0];
                            }
                        }
                    } else if (i3 == 1) {
                        ReelValue reelValue2 = this.center;
                        if (reelValue2 == ReelValue.bar) {
                            i2 = 11;
                        } else if (reelValue2 == ReelValue.berries) {
                            i2 = 12;
                        } else if (reelValue2 == ReelValue.lemon) {
                            i2 = 13;
                        } else {
                            i2 = reelValue2 == ReelValue.seven ? 10 : 9;
                        }
                    } else {
                        ReelValue reelValue3 = this.right;
                        if (reelValue3 == ReelValue.bar) {
                            i2 = 17;
                        } else if (reelValue3 == ReelValue.berries) {
                            i2 = 18;
                        } else if (reelValue3 == ReelValue.lemon) {
                            i2 = 19;
                        } else {
                            i2 = reelValue3 == ReelValue.seven ? 16 : 15;
                        }
                    }
                    tLRPC$TL_messages_stickerSet2 = tLRPC$TL_messages_stickerSet;
                    TLRPC$Document tLRPC$Document2 = tLRPC$TL_messages_stickerSet2.documents.get(i2);
                    readRes = RLottieDrawable.readRes(FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(tLRPC$Document2, true), 0);
                    if (TextUtils.isEmpty(readRes)) {
                    }
                }
            } else if (this.nativePtrs[i3] == 0) {
                if (i3 == 3) {
                    tLRPC$TL_messages_stickerSet2 = tLRPC$TL_messages_stickerSet;
                    i2 = 1;
                } else {
                    tLRPC$TL_messages_stickerSet2 = tLRPC$TL_messages_stickerSet;
                    i2 = 2;
                }
                TLRPC$Document tLRPC$Document22 = tLRPC$TL_messages_stickerSet2.documents.get(i2);
                readRes = RLottieDrawable.readRes(FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(tLRPC$Document22, true), 0);
                if (TextUtils.isEmpty(readRes)) {
                }
            }
            i3++;
        }
        if (z2) {
            AndroidUtilities.runOnUIThread(new SlotsDrawable$$ExternalSyntheticLambda4(this));
        } else {
            AndroidUtilities.runOnUIThread(new SlotsDrawable$$ExternalSyntheticLambda10(this, z, i, chatMessageCell));
        }
    }

    public /* synthetic */ void lambda$setDiceNumber$6() {
        this.secondLoadingInBackground = false;
        if (this.loadingInBackground || !this.destroyAfterLoading) {
            return;
        }
        recycle();
    }

    public static /* synthetic */ void lambda$setDiceNumber$7(TLRPC$Document tLRPC$Document, int i, MessageObject messageObject, ChatMessageCell chatMessageCell, TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet) {
        DownloadController.getInstance(i).addLoadingFileObserver(FileLoader.getAttachFileName(tLRPC$Document), messageObject, chatMessageCell);
        FileLoader.getInstance(i).loadFile(tLRPC$Document, tLRPC$TL_messages_stickerSet, 1, 1);
    }

    public /* synthetic */ void lambda$setDiceNumber$8() {
        this.secondLoadingInBackground = false;
    }

    public /* synthetic */ void lambda$setDiceNumber$9(boolean z, int i, ChatMessageCell chatMessageCell) {
        if (z && this.nextRenderingBitmap == null && this.renderingBitmap == null && this.loadFrameTask == null) {
            this.isDice = 2;
            this.setLastFrame = true;
        }
        this.secondLoadingInBackground = false;
        if (!this.loadingInBackground && this.destroyAfterLoading) {
            recycle();
            return;
        }
        this.secondNativePtr = this.secondNativePtrs[0];
        DownloadController.getInstance(i).removeLoadingFileObserver(chatMessageCell);
        this.timeBetweenFrames = Math.max(16, (int) (1000.0f / this.metaData[1]));
        scheduleNextGetFrame();
        invalidateInternal();
    }

    @Override // org.telegram.ui.Components.RLottieDrawable
    public void recycle() {
        int i = 0;
        this.isRunning = false;
        this.isRecycled = true;
        checkRunningTasks();
        if (this.loadingInBackground || this.secondLoadingInBackground) {
            this.destroyAfterLoading = true;
        } else if (this.loadFrameTask != null || this.cacheGenerateTask != null) {
            this.destroyWhenDone = true;
        } else {
            int i2 = 0;
            while (true) {
                long[] jArr = this.nativePtrs;
                if (i2 < jArr.length) {
                    if (jArr[i2] != 0) {
                        if (jArr[i2] == this.nativePtr) {
                            this.nativePtr = 0L;
                        }
                        RLottieDrawable.destroy(this.nativePtrs[i2]);
                        this.nativePtrs[i2] = 0;
                    }
                    i2++;
                }
            }
            while (true) {
                long[] jArr2 = this.secondNativePtrs;
                if (i < jArr2.length) {
                    if (jArr2[i] != 0) {
                        if (jArr2[i] == this.secondNativePtr) {
                            this.secondNativePtr = 0L;
                        }
                        RLottieDrawable.destroy(this.secondNativePtrs[i]);
                        this.secondNativePtrs[i] = 0;
                    }
                    i++;
                } else {
                    recycleResources();
                    return;
                }
            }
        }
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
                    if (i2 < jArr.length) {
                        if (jArr[i2] != 0) {
                            RLottieDrawable.destroy(jArr[i2]);
                            this.nativePtrs[i2] = 0;
                        }
                        i2++;
                    }
                }
                while (true) {
                    long[] jArr2 = this.secondNativePtrs;
                    if (i >= jArr2.length) {
                        break;
                    }
                    if (jArr2[i] != 0) {
                        RLottieDrawable.destroy(jArr2[i]);
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
}
