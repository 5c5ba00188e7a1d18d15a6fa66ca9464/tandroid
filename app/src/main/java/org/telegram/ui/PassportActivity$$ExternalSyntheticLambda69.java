package org.telegram.ui;

import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.EditTextBoldCursor;
/* loaded from: classes3.dex */
public final /* synthetic */ class PassportActivity$$ExternalSyntheticLambda69 implements AlertsCreator.DatePickerDelegate {
    public final /* synthetic */ PassportActivity f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ EditTextBoldCursor f$2;

    public /* synthetic */ PassportActivity$$ExternalSyntheticLambda69(PassportActivity passportActivity, int i, EditTextBoldCursor editTextBoldCursor) {
        this.f$0 = passportActivity;
        this.f$1 = i;
        this.f$2 = editTextBoldCursor;
    }

    @Override // org.telegram.ui.Components.AlertsCreator.DatePickerDelegate
    public final void didSelectDate(int i, int i2, int i3) {
        this.f$0.lambda$createIdentityInterface$47(this.f$1, this.f$2, i, i2, i3);
    }
}
