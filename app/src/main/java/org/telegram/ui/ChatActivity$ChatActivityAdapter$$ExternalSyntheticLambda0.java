package org.telegram.ui;

import org.telegram.ui.Cells.BotHelpCell;
import org.telegram.ui.ChatActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$ChatActivityAdapter$$ExternalSyntheticLambda0 implements BotHelpCell.BotHelpCellDelegate {
    public final /* synthetic */ ChatActivity.ChatActivityAdapter f$0;

    public /* synthetic */ ChatActivity$ChatActivityAdapter$$ExternalSyntheticLambda0(ChatActivity.ChatActivityAdapter chatActivityAdapter) {
        this.f$0 = chatActivityAdapter;
    }

    @Override // org.telegram.ui.Cells.BotHelpCell.BotHelpCellDelegate
    public final void didPressUrl(String str) {
        this.f$0.lambda$onCreateViewHolder$0(str);
    }
}
