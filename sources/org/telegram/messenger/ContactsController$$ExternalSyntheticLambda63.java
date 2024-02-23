package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class ContactsController$$ExternalSyntheticLambda63 implements RequestDelegate {
    public static final /* synthetic */ ContactsController$$ExternalSyntheticLambda63 INSTANCE = new ContactsController$$ExternalSyntheticLambda63();

    private /* synthetic */ ContactsController$$ExternalSyntheticLambda63() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        ContactsController.lambda$resetImportedContacts$9(tLObject, tLRPC$TL_error);
    }
}
