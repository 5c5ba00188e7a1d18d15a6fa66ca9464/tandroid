package org.telegram.ui;

import org.telegram.ui.Cells.ManageChatUserCell;
import org.telegram.ui.ChatUsersActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatUsersActivity$SearchAdapter$$ExternalSyntheticLambda5 implements ManageChatUserCell.ManageChatUserCellDelegate {
    public final /* synthetic */ ChatUsersActivity.SearchAdapter f$0;

    public /* synthetic */ ChatUsersActivity$SearchAdapter$$ExternalSyntheticLambda5(ChatUsersActivity.SearchAdapter searchAdapter) {
        this.f$0 = searchAdapter;
    }

    @Override // org.telegram.ui.Cells.ManageChatUserCell.ManageChatUserCellDelegate
    public final boolean onOptionsButtonCheck(ManageChatUserCell manageChatUserCell, boolean z) {
        boolean lambda$onCreateViewHolder$5;
        lambda$onCreateViewHolder$5 = this.f$0.lambda$onCreateViewHolder$5(manageChatUserCell, z);
        return lambda$onCreateViewHolder$5;
    }
}
