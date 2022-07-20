package org.telegram.messenger.voip;

import org.telegram.messenger.voip.Instance;
/* loaded from: classes.dex */
public final /* synthetic */ class VoIPService$$ExternalSyntheticLambda67 implements Instance.OnSignalingDataListener {
    public final /* synthetic */ VoIPService f$0;

    public /* synthetic */ VoIPService$$ExternalSyntheticLambda67(VoIPService voIPService) {
        this.f$0 = voIPService;
    }

    @Override // org.telegram.messenger.voip.Instance.OnSignalingDataListener
    public final void onSignalingData(byte[] bArr) {
        this.f$0.onSignalingData(bArr);
    }
}
