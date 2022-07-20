package org.telegram.ui;

import android.view.View;
import org.telegram.messenger.voip.VoIPService;
/* loaded from: classes3.dex */
public final /* synthetic */ class VoIPFragment$$ExternalSyntheticLambda18 implements View.OnClickListener {
    public final /* synthetic */ VoIPFragment f$0;
    public final /* synthetic */ VoIPService f$1;

    public /* synthetic */ VoIPFragment$$ExternalSyntheticLambda18(VoIPFragment voIPFragment, VoIPService voIPService) {
        this.f$0 = voIPFragment;
        this.f$1 = voIPService;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.lambda$setFrontalCameraAction$27(this.f$1, view);
    }
}
