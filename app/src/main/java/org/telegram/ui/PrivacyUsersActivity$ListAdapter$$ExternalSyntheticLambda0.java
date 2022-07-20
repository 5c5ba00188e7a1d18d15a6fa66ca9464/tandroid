package org.telegram.ui;

import org.telegram.ui.Cells.ManageChatUserCell;
import org.telegram.ui.PrivacyUsersActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class PrivacyUsersActivity$ListAdapter$$ExternalSyntheticLambda0 implements ManageChatUserCell.ManageChatUserCellDelegate {
    public final /* synthetic */ PrivacyUsersActivity.ListAdapter f$0;

    public /* synthetic */ PrivacyUsersActivity$ListAdapter$$ExternalSyntheticLambda0(PrivacyUsersActivity.ListAdapter listAdapter) {
        this.f$0 = listAdapter;
    }

    @Override // org.telegram.ui.Cells.ManageChatUserCell.ManageChatUserCellDelegate
    public final boolean onOptionsButtonCheck(ManageChatUserCell manageChatUserCell, boolean z) {
        boolean lambda$onCreateViewHolder$0;
        lambda$onCreateViewHolder$0 = this.f$0.lambda$onCreateViewHolder$0(manageChatUserCell, z);
        return lambda$onCreateViewHolder$0;
    }
}
