package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class ContactsController$$ExternalSyntheticLambda54 implements RequestDelegate {
    public final /* synthetic */ ContactsController f$0;

    public /* synthetic */ ContactsController$$ExternalSyntheticLambda54(ContactsController contactsController) {
        this.f$0 = contactsController;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$checkInviteText$3(tLObject, tLRPC$TL_error);
    }
}
