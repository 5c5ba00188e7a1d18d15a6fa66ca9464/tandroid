package org.telegram.ui.Components;

import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.Components.ChatAttachAlertContactsLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertContactsLayout$ShareAdapter$$ExternalSyntheticLambda1 implements ChatAttachAlertContactsLayout.UserCell.CharSequenceCallback {
    public final /* synthetic */ TLRPC$User f$0;

    public /* synthetic */ ChatAttachAlertContactsLayout$ShareAdapter$$ExternalSyntheticLambda1(TLRPC$User tLRPC$User) {
        this.f$0 = tLRPC$User;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlertContactsLayout.UserCell.CharSequenceCallback
    public final CharSequence run() {
        CharSequence lambda$onBindViewHolder$1;
        lambda$onBindViewHolder$1 = ChatAttachAlertContactsLayout.ShareAdapter.lambda$onBindViewHolder$1(this.f$0);
        return lambda$onBindViewHolder$1;
    }
}
