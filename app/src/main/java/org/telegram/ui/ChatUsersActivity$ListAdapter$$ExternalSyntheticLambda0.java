package org.telegram.ui;

import org.telegram.ui.Cells.ManageChatUserCell;
import org.telegram.ui.ChatUsersActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatUsersActivity$ListAdapter$$ExternalSyntheticLambda0 implements ManageChatUserCell.ManageChatUserCellDelegate {
    public final /* synthetic */ ChatUsersActivity.ListAdapter f$0;

    public /* synthetic */ ChatUsersActivity$ListAdapter$$ExternalSyntheticLambda0(ChatUsersActivity.ListAdapter listAdapter) {
        this.f$0 = listAdapter;
    }

    @Override // org.telegram.ui.Cells.ManageChatUserCell.ManageChatUserCellDelegate
    public final boolean onOptionsButtonCheck(ManageChatUserCell manageChatUserCell, boolean z) {
        boolean lambda$onCreateViewHolder$0;
        lambda$onCreateViewHolder$0 = this.f$0.lambda$onCreateViewHolder$0(manageChatUserCell, z);
        return lambda$onCreateViewHolder$0;
    }
}
