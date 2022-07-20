package org.telegram.ui;

import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ContactsActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class CallLogActivity$$ExternalSyntheticLambda10 implements ContactsActivity.ContactsActivityDelegate {
    public final /* synthetic */ CallLogActivity f$0;

    public /* synthetic */ CallLogActivity$$ExternalSyntheticLambda10(CallLogActivity callLogActivity) {
        this.f$0 = callLogActivity;
    }

    @Override // org.telegram.ui.ContactsActivity.ContactsActivityDelegate
    public final void didSelectContact(TLRPC$User tLRPC$User, String str, ContactsActivity contactsActivity) {
        this.f$0.lambda$createView$2(tLRPC$User, str, contactsActivity);
    }
}
