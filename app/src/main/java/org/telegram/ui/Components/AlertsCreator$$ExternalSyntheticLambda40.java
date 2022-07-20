package org.telegram.ui.Components;

import android.content.DialogInterface;
import android.widget.EditText;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda40 implements DialogInterface.OnDismissListener {
    public final /* synthetic */ EditText f$0;
    public final /* synthetic */ EditText f$1;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda40(EditText editText, EditText editText2) {
        this.f$0 = editText;
        this.f$1 = editText2;
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        AlertsCreator.lambda$createChangeNameAlert$35(this.f$0, this.f$1, dialogInterface);
    }
}
