package org.telegram.ui.Components;

import org.telegram.ui.Components.ChatAttachAlert;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertPhotoLayoutPreview$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ ChatAttachAlertPhotoLayoutPreview f$0;
    public final /* synthetic */ ChatAttachAlert.AttachAlertLayout f$1;

    public /* synthetic */ ChatAttachAlertPhotoLayoutPreview$$ExternalSyntheticLambda1(ChatAttachAlertPhotoLayoutPreview chatAttachAlertPhotoLayoutPreview, ChatAttachAlert.AttachAlertLayout attachAlertLayout) {
        this.f$0 = chatAttachAlertPhotoLayoutPreview;
        this.f$1 = attachAlertLayout;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onShow$0(this.f$1);
    }
}
