package org.telegram.ui.Components;

import android.content.DialogInterface;
import android.widget.EditText;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda1 implements DialogInterface.OnClickListener {
    public final /* synthetic */ long f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ EditText f$2;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda1(long j, int i, EditText editText) {
        this.f$0 = j;
        this.f$1 = i;
        this.f$2 = editText;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        AlertsCreator.lambda$createChangeBioAlert$30(this.f$0, this.f$1, this.f$2, dialogInterface, i);
    }
}
