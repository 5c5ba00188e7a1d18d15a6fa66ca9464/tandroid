package org.telegram.messenger;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class ContactsController$$ExternalSyntheticLambda37 implements Runnable {
    public final /* synthetic */ ContactsController f$0;
    public final /* synthetic */ TLRPC$TL_error f$1;
    public final /* synthetic */ TLObject f$2;

    public /* synthetic */ ContactsController$$ExternalSyntheticLambda37(ContactsController contactsController, TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        this.f$0 = contactsController;
        this.f$1 = tLRPC$TL_error;
        this.f$2 = tLObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadPrivacySettings$60(this.f$1, this.f$2);
    }
}
