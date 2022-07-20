package org.telegram.ui;

import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.ui.LocationActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LaunchActivity$$ExternalSyntheticLambda103 implements LocationActivity.LocationActivityDelegate {
    public final /* synthetic */ int[] f$0;
    public final /* synthetic */ long f$1;

    public /* synthetic */ LaunchActivity$$ExternalSyntheticLambda103(int[] iArr, long j) {
        this.f$0 = iArr;
        this.f$1 = j;
    }

    @Override // org.telegram.ui.LocationActivity.LocationActivityDelegate
    public final void didSelectLocation(TLRPC$MessageMedia tLRPC$MessageMedia, int i, boolean z, int i2) {
        LaunchActivity.lambda$handleIntent$13(this.f$0, this.f$1, tLRPC$MessageMedia, i, z, i2);
    }
}
