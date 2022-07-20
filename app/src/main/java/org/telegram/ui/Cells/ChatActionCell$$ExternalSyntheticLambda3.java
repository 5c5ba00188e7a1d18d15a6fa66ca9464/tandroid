package org.telegram.ui.Cells;

import org.telegram.messenger.ImageReceiver;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActionCell$$ExternalSyntheticLambda3 implements ImageReceiver.ImageReceiverDelegate {
    public final /* synthetic */ ChatActionCell f$0;

    public /* synthetic */ ChatActionCell$$ExternalSyntheticLambda3(ChatActionCell chatActionCell) {
        this.f$0 = chatActionCell;
    }

    @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
    public final void didSetImage(ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3) {
        this.f$0.lambda$new$0(imageReceiver, z, z2, z3);
    }

    @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
    public /* synthetic */ void onAnimationReady(ImageReceiver imageReceiver) {
        ImageReceiver.ImageReceiverDelegate.CC.$default$onAnimationReady(this, imageReceiver);
    }
}
