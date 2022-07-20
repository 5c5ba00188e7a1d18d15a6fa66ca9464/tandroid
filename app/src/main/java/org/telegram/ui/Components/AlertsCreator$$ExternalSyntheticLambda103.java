package org.telegram.ui.Components;

import java.util.Calendar;
import org.telegram.ui.Components.NumberPicker;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda103 implements NumberPicker.Formatter {
    public final /* synthetic */ long f$0;
    public final /* synthetic */ Calendar f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda103(long j, Calendar calendar, int i) {
        this.f$0 = j;
        this.f$1 = calendar;
        this.f$2 = i;
    }

    @Override // org.telegram.ui.Components.NumberPicker.Formatter
    public final String format(int i) {
        String lambda$createDatePickerDialog$57;
        lambda$createDatePickerDialog$57 = AlertsCreator.lambda$createDatePickerDialog$57(this.f$0, this.f$1, this.f$2, i);
        return lambda$createDatePickerDialog$57;
    }
}
