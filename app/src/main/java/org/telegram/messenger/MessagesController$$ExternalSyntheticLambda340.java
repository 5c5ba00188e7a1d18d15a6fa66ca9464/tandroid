package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda340 implements RequestDelegate {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ AlertDialog[] f$1;
    public final /* synthetic */ BaseFragment f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda340(MessagesController messagesController, AlertDialog[] alertDialogArr, BaseFragment baseFragment, int i) {
        this.f$0 = messagesController;
        this.f$1 = alertDialogArr;
        this.f$2 = baseFragment;
        this.f$3 = i;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$openByUserName$348(this.f$1, this.f$2, this.f$3, tLObject, tLRPC$TL_error);
    }
}
