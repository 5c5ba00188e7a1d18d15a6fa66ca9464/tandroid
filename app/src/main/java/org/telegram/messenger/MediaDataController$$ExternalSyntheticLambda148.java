package org.telegram.messenger;

import org.telegram.messenger.ImageReceiver;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda148 implements ImageReceiver.ImageReceiverDelegate {
    public static final /* synthetic */ MediaDataController$$ExternalSyntheticLambda148 INSTANCE = new MediaDataController$$ExternalSyntheticLambda148();

    private /* synthetic */ MediaDataController$$ExternalSyntheticLambda148() {
    }

    @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
    public final void didSetImage(ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3) {
        imageReceiver.clearImage();
    }

    @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
    public /* synthetic */ void onAnimationReady(ImageReceiver imageReceiver) {
        ImageReceiver.ImageReceiverDelegate.-CC.$default$onAnimationReady(this, imageReceiver);
    }
}
