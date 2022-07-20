package org.telegram.ui.Components;

import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.Components.ChatAttachAlertContactsLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlert$$ExternalSyntheticLambda32 implements ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate {
    public final /* synthetic */ ChatAttachAlert f$0;

    public /* synthetic */ ChatAttachAlert$$ExternalSyntheticLambda32(ChatAttachAlert chatAttachAlert) {
        this.f$0 = chatAttachAlert;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate
    public final void didSelectContact(TLRPC$User tLRPC$User, boolean z, int i) {
        this.f$0.lambda$openContactsLayout$24(tLRPC$User, z, i);
    }
}
