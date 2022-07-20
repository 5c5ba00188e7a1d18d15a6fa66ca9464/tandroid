package org.telegram.ui;

import org.telegram.ui.Components.AlertsCreator;
/* loaded from: classes3.dex */
public final /* synthetic */ class LocationActivity$$ExternalSyntheticLambda32 implements AlertsCreator.ScheduleDatePickerDelegate {
    public final /* synthetic */ LocationActivity f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ LocationActivity$$ExternalSyntheticLambda32(LocationActivity locationActivity, Object obj) {
        this.f$0 = locationActivity;
        this.f$1 = obj;
    }

    @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
    public final void didSelectDate(boolean z, int i) {
        this.f$0.lambda$createView$13(this.f$1, z, i);
    }
}
