package org.telegram.ui.Components.voip;

import org.telegram.messenger.ImageReceiver;
/* loaded from: classes3.dex */
public final /* synthetic */ class VoIPOverlayBackground$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ VoIPOverlayBackground f$0;
    public final /* synthetic */ ImageReceiver.BitmapHolder f$1;

    public /* synthetic */ VoIPOverlayBackground$$ExternalSyntheticLambda2(VoIPOverlayBackground voIPOverlayBackground, ImageReceiver.BitmapHolder bitmapHolder) {
        this.f$0 = voIPOverlayBackground;
        this.f$1 = bitmapHolder;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$setBackground$1(this.f$1);
    }
}
