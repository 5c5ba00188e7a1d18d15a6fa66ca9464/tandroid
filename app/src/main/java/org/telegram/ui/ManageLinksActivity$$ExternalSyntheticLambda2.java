package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_chatInviteExported;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class ManageLinksActivity$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ ManageLinksActivity f$0;
    public final /* synthetic */ TLRPC$TL_chatInviteExported f$1;
    public final /* synthetic */ TLRPC$TL_error f$2;
    public final /* synthetic */ TLObject f$3;
    public final /* synthetic */ boolean f$4;

    public /* synthetic */ ManageLinksActivity$$ExternalSyntheticLambda2(ManageLinksActivity manageLinksActivity, TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported, TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, boolean z) {
        this.f$0 = manageLinksActivity;
        this.f$1 = tLRPC$TL_chatInviteExported;
        this.f$2 = tLRPC$TL_error;
        this.f$3 = tLObject;
        this.f$4 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadLinks$3(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
