package org.telegram.ui.Components;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.Components.InviteLinkBottomSheet;
/* loaded from: classes3.dex */
public final /* synthetic */ class InviteLinkBottomSheet$Adapter$1$$ExternalSyntheticLambda2 implements RequestDelegate {
    public final /* synthetic */ InviteLinkBottomSheet.Adapter.AnonymousClass1 f$0;

    public /* synthetic */ InviteLinkBottomSheet$Adapter$1$$ExternalSyntheticLambda2(InviteLinkBottomSheet.Adapter.AnonymousClass1 anonymousClass1) {
        this.f$0 = anonymousClass1;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$revokeLink$1(tLObject, tLRPC$TL_error);
    }
}
