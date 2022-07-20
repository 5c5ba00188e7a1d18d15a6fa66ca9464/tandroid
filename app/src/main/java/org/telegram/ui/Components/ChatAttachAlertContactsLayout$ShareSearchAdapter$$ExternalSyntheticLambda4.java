package org.telegram.ui.Components;

import org.telegram.messenger.ContactsController;
import org.telegram.ui.Components.ChatAttachAlertContactsLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertContactsLayout$ShareSearchAdapter$$ExternalSyntheticLambda4 implements ChatAttachAlertContactsLayout.UserCell.CharSequenceCallback {
    public final /* synthetic */ ContactsController.Contact f$0;

    public /* synthetic */ ChatAttachAlertContactsLayout$ShareSearchAdapter$$ExternalSyntheticLambda4(ContactsController.Contact contact) {
        this.f$0 = contact;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlertContactsLayout.UserCell.CharSequenceCallback
    public final CharSequence run() {
        CharSequence lambda$onBindViewHolder$4;
        lambda$onBindViewHolder$4 = ChatAttachAlertContactsLayout.ShareSearchAdapter.lambda$onBindViewHolder$4(this.f$0);
        return lambda$onBindViewHolder$4;
    }
}
