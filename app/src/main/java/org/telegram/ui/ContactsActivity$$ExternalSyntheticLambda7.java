package org.telegram.ui;

import org.telegram.messenger.MessagesStorage;
/* loaded from: classes3.dex */
public final /* synthetic */ class ContactsActivity$$ExternalSyntheticLambda7 implements MessagesStorage.IntCallback {
    public final /* synthetic */ ContactsActivity f$0;

    public /* synthetic */ ContactsActivity$$ExternalSyntheticLambda7(ContactsActivity contactsActivity) {
        this.f$0 = contactsActivity;
    }

    @Override // org.telegram.messenger.MessagesStorage.IntCallback
    public final void run(int i) {
        this.f$0.lambda$onResume$5(i);
    }
}
