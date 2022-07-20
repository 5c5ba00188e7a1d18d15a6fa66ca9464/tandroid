package org.telegram.ui;

import org.telegram.ui.Components.AlertsCreator;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoAlbumPickerActivity$$ExternalSyntheticLambda7 implements AlertsCreator.ScheduleDatePickerDelegate {
    public final /* synthetic */ PhotoAlbumPickerActivity f$0;

    public /* synthetic */ PhotoAlbumPickerActivity$$ExternalSyntheticLambda7(PhotoAlbumPickerActivity photoAlbumPickerActivity) {
        this.f$0 = photoAlbumPickerActivity;
    }

    @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
    public final void didSelectDate(boolean z, int i) {
        this.f$0.lambda$createView$2(z, i);
    }
}
