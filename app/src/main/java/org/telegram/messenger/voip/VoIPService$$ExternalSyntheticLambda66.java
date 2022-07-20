package org.telegram.messenger.voip;

import org.telegram.messenger.voip.Instance;
/* loaded from: classes.dex */
public final /* synthetic */ class VoIPService$$ExternalSyntheticLambda66 implements Instance.OnSignalBarsUpdatedListener {
    public final /* synthetic */ VoIPService f$0;

    public /* synthetic */ VoIPService$$ExternalSyntheticLambda66(VoIPService voIPService) {
        this.f$0 = voIPService;
    }

    @Override // org.telegram.messenger.voip.Instance.OnSignalBarsUpdatedListener
    public final void onSignalBarsUpdated(int i) {
        this.f$0.onSignalBarCountChanged(i);
    }
}
