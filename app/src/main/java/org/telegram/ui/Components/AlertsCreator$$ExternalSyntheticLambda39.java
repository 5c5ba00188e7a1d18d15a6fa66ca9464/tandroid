package org.telegram.ui.Components;

import android.content.DialogInterface;
import android.widget.EditText;
import org.telegram.messenger.AndroidUtilities;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda39 implements DialogInterface.OnDismissListener {
    public final /* synthetic */ EditText f$0;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda39(EditText editText) {
        this.f$0 = editText;
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        AndroidUtilities.hideKeyboard(this.f$0);
    }
}
