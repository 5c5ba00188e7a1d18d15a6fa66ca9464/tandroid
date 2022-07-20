package org.telegram.ui;

import org.telegram.ui.Components.AlertsCreator;
/* loaded from: classes3.dex */
public final /* synthetic */ class LinkEditActivity$$ExternalSyntheticLambda10 implements AlertsCreator.ScheduleDatePickerDelegate {
    public final /* synthetic */ LinkEditActivity f$0;

    public /* synthetic */ LinkEditActivity$$ExternalSyntheticLambda10(LinkEditActivity linkEditActivity) {
        this.f$0 = linkEditActivity;
    }

    @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
    public final void didSelectDate(boolean z, int i) {
        this.f$0.lambda$createView$1(z, i);
    }
}
