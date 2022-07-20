package org.telegram.ui.Components;

import org.telegram.messenger.ContactsController;
import org.telegram.ui.Components.ChatAttachAlertContactsLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertContactsLayout$ShareAdapter$$ExternalSyntheticLambda0 implements ChatAttachAlertContactsLayout.UserCell.CharSequenceCallback {
    public final /* synthetic */ ContactsController.Contact f$0;

    public /* synthetic */ ChatAttachAlertContactsLayout$ShareAdapter$$ExternalSyntheticLambda0(ContactsController.Contact contact) {
        this.f$0 = contact;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlertContactsLayout.UserCell.CharSequenceCallback
    public final CharSequence run() {
        CharSequence lambda$onBindViewHolder$0;
        lambda$onBindViewHolder$0 = ChatAttachAlertContactsLayout.ShareAdapter.lambda$onBindViewHolder$0(this.f$0);
        return lambda$onBindViewHolder$0;
    }
}
