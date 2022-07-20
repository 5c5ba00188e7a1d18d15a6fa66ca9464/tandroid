package org.telegram.ui.Components;

import org.telegram.ui.Cells.ManageChatUserCell;
import org.telegram.ui.Components.SharedMediaLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class SharedMediaLayout$GroupUsersSearchAdapter$$ExternalSyntheticLambda5 implements ManageChatUserCell.ManageChatUserCellDelegate {
    public final /* synthetic */ SharedMediaLayout.GroupUsersSearchAdapter f$0;

    public /* synthetic */ SharedMediaLayout$GroupUsersSearchAdapter$$ExternalSyntheticLambda5(SharedMediaLayout.GroupUsersSearchAdapter groupUsersSearchAdapter) {
        this.f$0 = groupUsersSearchAdapter;
    }

    @Override // org.telegram.ui.Cells.ManageChatUserCell.ManageChatUserCellDelegate
    public final boolean onOptionsButtonCheck(ManageChatUserCell manageChatUserCell, boolean z) {
        boolean lambda$onCreateViewHolder$5;
        lambda$onCreateViewHolder$5 = this.f$0.lambda$onCreateViewHolder$5(manageChatUserCell, z);
        return lambda$onCreateViewHolder$5;
    }
}
