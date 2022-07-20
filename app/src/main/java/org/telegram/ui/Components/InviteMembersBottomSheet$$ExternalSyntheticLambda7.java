package org.telegram.ui.Components;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class InviteMembersBottomSheet$$ExternalSyntheticLambda7 implements RequestDelegate {
    public final /* synthetic */ InviteMembersBottomSheet f$0;

    public /* synthetic */ InviteMembersBottomSheet$$ExternalSyntheticLambda7(InviteMembersBottomSheet inviteMembersBottomSheet) {
        this.f$0 = inviteMembersBottomSheet;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$generateLink$8(tLObject, tLRPC$TL_error);
    }
}
