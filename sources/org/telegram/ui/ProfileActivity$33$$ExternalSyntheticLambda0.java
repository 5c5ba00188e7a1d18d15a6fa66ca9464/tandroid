package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.ProfileActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ProfileActivity$33$$ExternalSyntheticLambda0 implements RequestDelegate {
    public static final /* synthetic */ ProfileActivity$33$$ExternalSyntheticLambda0 INSTANCE = new ProfileActivity$33$$ExternalSyntheticLambda0();

    private /* synthetic */ ProfileActivity$33$$ExternalSyntheticLambda0() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        ProfileActivity.33.lambda$onEmojiSelected$0(tLObject, tLRPC$TL_error);
    }
}
