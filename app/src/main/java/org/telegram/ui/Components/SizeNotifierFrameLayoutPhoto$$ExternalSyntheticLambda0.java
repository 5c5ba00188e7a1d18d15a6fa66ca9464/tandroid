package org.telegram.ui.Components;
/* loaded from: classes3.dex */
public final /* synthetic */ class SizeNotifierFrameLayoutPhoto$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ SizeNotifierFrameLayoutPhoto f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ SizeNotifierFrameLayoutPhoto$$ExternalSyntheticLambda0(SizeNotifierFrameLayoutPhoto sizeNotifierFrameLayoutPhoto, boolean z) {
        this.f$0 = sizeNotifierFrameLayoutPhoto;
        this.f$1 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$notifyHeightChanged$0(this.f$1);
    }
}
