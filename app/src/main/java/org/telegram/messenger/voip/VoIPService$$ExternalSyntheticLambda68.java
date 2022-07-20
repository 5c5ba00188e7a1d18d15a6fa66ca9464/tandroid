package org.telegram.messenger.voip;

import org.telegram.messenger.voip.Instance;
/* loaded from: classes.dex */
public final /* synthetic */ class VoIPService$$ExternalSyntheticLambda68 implements Instance.OnStateUpdatedListener {
    public final /* synthetic */ VoIPService f$0;

    public /* synthetic */ VoIPService$$ExternalSyntheticLambda68(VoIPService voIPService) {
        this.f$0 = voIPService;
    }

    @Override // org.telegram.messenger.voip.Instance.OnStateUpdatedListener
    public final void onStateUpdated(int i, boolean z) {
        this.f$0.onConnectionStateChanged(i, z);
    }
}
