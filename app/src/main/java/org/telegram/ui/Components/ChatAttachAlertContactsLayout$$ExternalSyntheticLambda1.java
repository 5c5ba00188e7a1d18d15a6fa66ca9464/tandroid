package org.telegram.ui.Components;

import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.Components.ChatAttachAlertContactsLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertContactsLayout$$ExternalSyntheticLambda1 implements ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate {
    public final /* synthetic */ ChatAttachAlertContactsLayout f$0;

    public /* synthetic */ ChatAttachAlertContactsLayout$$ExternalSyntheticLambda1(ChatAttachAlertContactsLayout chatAttachAlertContactsLayout) {
        this.f$0 = chatAttachAlertContactsLayout;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate
    public final void didSelectContact(TLRPC$User tLRPC$User, boolean z, int i) {
        this.f$0.lambda$new$0(tLRPC$User, z, i);
    }
}
