package org.telegram.ui.Components;

import org.telegram.ui.Cells.PhotoAttachPhotoCell;
import org.telegram.ui.Components.ChatAttachAlertPhotoLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertPhotoLayout$PhotoAttachAdapter$$ExternalSyntheticLambda0 implements PhotoAttachPhotoCell.PhotoAttachPhotoCellDelegate {
    public final /* synthetic */ ChatAttachAlertPhotoLayout.PhotoAttachAdapter f$0;

    public /* synthetic */ ChatAttachAlertPhotoLayout$PhotoAttachAdapter$$ExternalSyntheticLambda0(ChatAttachAlertPhotoLayout.PhotoAttachAdapter photoAttachAdapter) {
        this.f$0 = photoAttachAdapter;
    }

    @Override // org.telegram.ui.Cells.PhotoAttachPhotoCell.PhotoAttachPhotoCellDelegate
    public final void onCheckClick(PhotoAttachPhotoCell photoAttachPhotoCell) {
        this.f$0.lambda$createHolder$0(photoAttachPhotoCell);
    }
}
