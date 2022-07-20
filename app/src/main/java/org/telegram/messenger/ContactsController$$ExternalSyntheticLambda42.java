package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$User;
/* loaded from: classes.dex */
public final /* synthetic */ class ContactsController$$ExternalSyntheticLambda42 implements Runnable {
    public final /* synthetic */ ContactsController f$0;
    public final /* synthetic */ TLRPC$User f$1;

    public /* synthetic */ ContactsController$$ExternalSyntheticLambda42(ContactsController contactsController, TLRPC$User tLRPC$User) {
        this.f$0 = contactsController;
        this.f$1 = tLRPC$User;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$addContact$50(this.f$1);
    }
}
