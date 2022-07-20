package org.telegram.ui;

import org.telegram.ui.Components.voip.VoIPFloatingLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class VoIPFragment$$ExternalSyntheticLambda30 implements VoIPFloatingLayout.VoIPFloatingLayoutDelegate {
    public final /* synthetic */ VoIPFragment f$0;

    public /* synthetic */ VoIPFragment$$ExternalSyntheticLambda30(VoIPFragment voIPFragment) {
        this.f$0 = voIPFragment;
    }

    @Override // org.telegram.ui.Components.voip.VoIPFloatingLayout.VoIPFloatingLayoutDelegate
    public final void onChange(float f, boolean z) {
        this.f$0.lambda$createView$5(f, z);
    }
}
