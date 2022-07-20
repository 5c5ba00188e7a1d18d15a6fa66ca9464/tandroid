package org.telegram.ui;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class ContactsActivity$$ExternalSyntheticLambda1 implements DialogInterface.OnClickListener {
    public final /* synthetic */ ContactsActivity f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ ContactsActivity$$ExternalSyntheticLambda1(ContactsActivity contactsActivity, String str) {
        this.f$0 = contactsActivity;
        this.f$1 = str;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$createView$0(this.f$1, dialogInterface, i);
    }
}
