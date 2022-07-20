package org.telegram.ui.ActionBar;

import org.telegram.messenger.ImageReceiver;
import org.telegram.tgnet.ResultCallback;
/* loaded from: classes3.dex */
public final /* synthetic */ class EmojiThemes$$ExternalSyntheticLambda1 implements ImageReceiver.ImageReceiverDelegate {
    public final /* synthetic */ ResultCallback f$0;
    public final /* synthetic */ long f$1;

    public /* synthetic */ EmojiThemes$$ExternalSyntheticLambda1(ResultCallback resultCallback, long j) {
        this.f$0 = resultCallback;
        this.f$1 = j;
    }

    @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
    public final void didSetImage(ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3) {
        EmojiThemes.lambda$loadWallpaper$0(this.f$0, this.f$1, imageReceiver, z, z2, z3);
    }

    @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
    public /* synthetic */ void onAnimationReady(ImageReceiver imageReceiver) {
        ImageReceiver.ImageReceiverDelegate.CC.$default$onAnimationReady(this, imageReceiver);
    }
}
