package org.telegram.ui.Components;

import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.Components.ChatAttachAlertContactsLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertContactsLayout$ShareSearchAdapter$$ExternalSyntheticLambda5 implements ChatAttachAlertContactsLayout.UserCell.CharSequenceCallback {
    public final /* synthetic */ TLRPC$User f$0;

    public /* synthetic */ ChatAttachAlertContactsLayout$ShareSearchAdapter$$ExternalSyntheticLambda5(TLRPC$User tLRPC$User) {
        this.f$0 = tLRPC$User;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlertContactsLayout.UserCell.CharSequenceCallback
    public final CharSequence run() {
        CharSequence lambda$onBindViewHolder$5;
        lambda$onBindViewHolder$5 = ChatAttachAlertContactsLayout.ShareSearchAdapter.lambda$onBindViewHolder$5(this.f$0);
        return lambda$onBindViewHolder$5;
    }
}
