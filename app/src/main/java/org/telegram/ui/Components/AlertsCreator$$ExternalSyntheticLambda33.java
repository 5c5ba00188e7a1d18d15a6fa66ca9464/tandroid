package org.telegram.ui.Components;

import android.content.DialogInterface;
import org.telegram.ui.Components.AlertsCreator;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda33 implements DialogInterface.OnClickListener {
    public final /* synthetic */ boolean f$0;
    public final /* synthetic */ NumberPicker f$1;
    public final /* synthetic */ NumberPicker f$2;
    public final /* synthetic */ NumberPicker f$3;
    public final /* synthetic */ AlertsCreator.DatePickerDelegate f$4;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda33(boolean z, NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3, AlertsCreator.DatePickerDelegate datePickerDelegate) {
        this.f$0 = z;
        this.f$1 = numberPicker;
        this.f$2 = numberPicker2;
        this.f$3 = numberPicker3;
        this.f$4 = datePickerDelegate;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        AlertsCreator.lambda$createDatePickerDialog$46(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, dialogInterface, i);
    }
}
