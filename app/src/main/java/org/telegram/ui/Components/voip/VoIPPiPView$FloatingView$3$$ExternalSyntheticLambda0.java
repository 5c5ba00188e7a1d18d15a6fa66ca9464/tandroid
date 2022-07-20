package org.telegram.ui.Components.voip;

import org.telegram.ui.Components.voip.VoIPPiPView;
/* loaded from: classes3.dex */
public final /* synthetic */ class VoIPPiPView$FloatingView$3$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ VoIPPiPView.FloatingView.AnonymousClass3 f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ VoIPPiPView$FloatingView$3$$ExternalSyntheticLambda0(VoIPPiPView.FloatingView.AnonymousClass3 anonymousClass3, boolean z) {
        this.f$0 = anonymousClass3;
        this.f$1 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onAnimationEnd$0(this.f$1);
    }
}
