package org.telegram.ui.Components.voip;

import org.telegram.ui.Components.voip.VoIPPiPView;
/* loaded from: classes3.dex */
public final /* synthetic */ class VoIPPiPView$FloatingView$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ VoIPPiPView.FloatingView f$0;
    public final /* synthetic */ float f$1;
    public final /* synthetic */ VoIPPiPView f$2;

    public /* synthetic */ VoIPPiPView$FloatingView$$ExternalSyntheticLambda2(VoIPPiPView.FloatingView floatingView, float f, VoIPPiPView voIPPiPView) {
        this.f$0 = floatingView;
        this.f$1 = f;
        this.f$2 = voIPPiPView;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$expand$1(this.f$1, this.f$2);
    }
}
