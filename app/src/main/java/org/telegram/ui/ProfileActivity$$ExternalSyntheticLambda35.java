package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_channels_getParticipants;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class ProfileActivity$$ExternalSyntheticLambda35 implements RequestDelegate {
    public final /* synthetic */ ProfileActivity f$0;
    public final /* synthetic */ TLRPC$TL_channels_getParticipants f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ ProfileActivity$$ExternalSyntheticLambda35(ProfileActivity profileActivity, TLRPC$TL_channels_getParticipants tLRPC$TL_channels_getParticipants, int i) {
        this.f$0 = profileActivity;
        this.f$1 = tLRPC$TL_channels_getParticipants;
        this.f$2 = i;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$getChannelParticipants$27(this.f$1, this.f$2, tLObject, tLRPC$TL_error);
    }
}
