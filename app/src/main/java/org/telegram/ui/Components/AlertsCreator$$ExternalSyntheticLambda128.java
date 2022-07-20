package org.telegram.ui.Components;

import org.telegram.ui.Components.NumberPicker;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda128 implements NumberPicker.OnValueChangeListener {
    public final /* synthetic */ NumberPicker f$0;
    public final /* synthetic */ NumberPicker f$1;
    public final /* synthetic */ NumberPicker f$2;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda128(NumberPicker numberPicker, NumberPicker numberPicker2, NumberPicker numberPicker3) {
        this.f$0 = numberPicker;
        this.f$1 = numberPicker2;
        this.f$2 = numberPicker3;
    }

    @Override // org.telegram.ui.Components.NumberPicker.OnValueChangeListener
    public final void onValueChange(NumberPicker numberPicker, int i, int i2) {
        AlertsCreator.updateDayPicker(this.f$0, this.f$1, this.f$2);
    }
}
