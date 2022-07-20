package org.telegram.ui.Components;

import org.telegram.messenger.LocationController;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.ui.LocationActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class FragmentContextView$$ExternalSyntheticLambda13 implements LocationActivity.LocationActivityDelegate {
    public final /* synthetic */ LocationController.SharingLocationInfo f$0;
    public final /* synthetic */ long f$1;

    public /* synthetic */ FragmentContextView$$ExternalSyntheticLambda13(LocationController.SharingLocationInfo sharingLocationInfo, long j) {
        this.f$0 = sharingLocationInfo;
        this.f$1 = j;
    }

    @Override // org.telegram.ui.LocationActivity.LocationActivityDelegate
    public final void didSelectLocation(TLRPC$MessageMedia tLRPC$MessageMedia, int i, boolean z, int i2) {
        FragmentContextView.lambda$openSharingLocation$11(this.f$0, this.f$1, tLRPC$MessageMedia, i, z, i2);
    }
}
