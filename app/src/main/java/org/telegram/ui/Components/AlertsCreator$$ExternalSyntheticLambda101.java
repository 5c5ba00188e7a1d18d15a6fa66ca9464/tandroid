package org.telegram.ui.Components;

import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.Components.AlertsCreator;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda101 implements ActionBarMenuItem.ActionBarMenuItemDelegate {
    public final /* synthetic */ AlertsCreator.ScheduleDatePickerDelegate f$0;
    public final /* synthetic */ BottomSheet.Builder f$1;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda101(AlertsCreator.ScheduleDatePickerDelegate scheduleDatePickerDelegate, BottomSheet.Builder builder) {
        this.f$0 = scheduleDatePickerDelegate;
        this.f$1 = builder;
    }

    @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemDelegate
    public final void onItemClick(int i) {
        AlertsCreator.lambda$createScheduleDatePickerDialog$49(this.f$0, this.f$1, i);
    }
}
