package org.telegram.ui.Components;

import org.telegram.ui.Components.AlertsCreator;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertPhotoLayout$$ExternalSyntheticLambda14 implements AlertsCreator.ScheduleDatePickerDelegate {
    public final /* synthetic */ ChatAttachAlertPhotoLayout f$0;

    public /* synthetic */ ChatAttachAlertPhotoLayout$$ExternalSyntheticLambda14(ChatAttachAlertPhotoLayout chatAttachAlertPhotoLayout) {
        this.f$0 = chatAttachAlertPhotoLayout;
    }

    @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
    public final void didSelectDate(boolean z, int i) {
        this.f$0.lambda$onMenuItemClick$15(z, i);
    }
}
