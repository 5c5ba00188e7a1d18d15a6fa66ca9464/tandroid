package org.telegram.ui.Components;

import java.util.List;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class InviteLinkBottomSheet$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ InviteLinkBottomSheet f$0;
    public final /* synthetic */ TLRPC$TL_error f$1;
    public final /* synthetic */ TLObject f$2;
    public final /* synthetic */ List f$3;
    public final /* synthetic */ boolean f$4;
    public final /* synthetic */ boolean f$5;

    public /* synthetic */ InviteLinkBottomSheet$$ExternalSyntheticLambda0(InviteLinkBottomSheet inviteLinkBottomSheet, TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, List list, boolean z, boolean z2) {
        this.f$0 = inviteLinkBottomSheet;
        this.f$1 = tLRPC$TL_error;
        this.f$2 = tLObject;
        this.f$3 = list;
        this.f$4 = z;
        this.f$5 = z2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadUsers$3(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
