package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class ContactsController$$ExternalSyntheticLambda18 implements Runnable {
    public final /* synthetic */ ContactsController f$0;
    public final /* synthetic */ ArrayList f$1;

    public /* synthetic */ ContactsController$$ExternalSyntheticLambda18(ContactsController contactsController, ArrayList arrayList) {
        this.f$0 = contactsController;
        this.f$1 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$performWriteContactsToPhoneBook$45(this.f$1);
    }
}
