package org.telegram.ui;

import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.ui.LocationActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCreateFinalActivity$$ExternalSyntheticLambda9 implements LocationActivity.LocationActivityDelegate {
    public final /* synthetic */ GroupCreateFinalActivity f$0;

    public /* synthetic */ GroupCreateFinalActivity$$ExternalSyntheticLambda9(GroupCreateFinalActivity groupCreateFinalActivity) {
        this.f$0 = groupCreateFinalActivity;
    }

    @Override // org.telegram.ui.LocationActivity.LocationActivityDelegate
    public final void didSelectLocation(TLRPC$MessageMedia tLRPC$MessageMedia, int i, boolean z, int i2) {
        this.f$0.lambda$createView$5(tLRPC$MessageMedia, i, z, i2);
    }
}
