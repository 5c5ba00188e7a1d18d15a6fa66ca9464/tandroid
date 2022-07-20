package org.telegram.ui;

import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ContactsActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class DialogOrContactPickerActivity$$ExternalSyntheticLambda2 implements ContactsActivity.ContactsActivityDelegate {
    public final /* synthetic */ DialogOrContactPickerActivity f$0;

    public /* synthetic */ DialogOrContactPickerActivity$$ExternalSyntheticLambda2(DialogOrContactPickerActivity dialogOrContactPickerActivity) {
        this.f$0 = dialogOrContactPickerActivity;
    }

    @Override // org.telegram.ui.ContactsActivity.ContactsActivityDelegate
    public final void didSelectContact(TLRPC$User tLRPC$User, String str, ContactsActivity contactsActivity) {
        this.f$0.lambda$new$2(tLRPC$User, str, contactsActivity);
    }
}
