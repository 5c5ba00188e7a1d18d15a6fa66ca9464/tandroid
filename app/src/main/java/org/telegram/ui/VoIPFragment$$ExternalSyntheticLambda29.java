package org.telegram.ui;

import org.telegram.messenger.ImageReceiver;
/* loaded from: classes3.dex */
public final /* synthetic */ class VoIPFragment$$ExternalSyntheticLambda29 implements ImageReceiver.ImageReceiverDelegate {
    public final /* synthetic */ VoIPFragment f$0;

    public /* synthetic */ VoIPFragment$$ExternalSyntheticLambda29(VoIPFragment voIPFragment) {
        this.f$0 = voIPFragment;
    }

    @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
    public final void didSetImage(ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3) {
        this.f$0.lambda$createView$4(imageReceiver, z, z2, z3);
    }

    @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
    public /* synthetic */ void onAnimationReady(ImageReceiver imageReceiver) {
        ImageReceiver.ImageReceiverDelegate.CC.$default$onAnimationReady(this, imageReceiver);
    }
}
