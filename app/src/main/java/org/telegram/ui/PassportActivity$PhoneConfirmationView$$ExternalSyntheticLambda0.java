package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.ui.PassportActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class PassportActivity$PhoneConfirmationView$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ PassportActivity.PhoneConfirmationView f$0;

    public /* synthetic */ PassportActivity$PhoneConfirmationView$$ExternalSyntheticLambda0(PassportActivity.PhoneConfirmationView phoneConfirmationView) {
        this.f$0 = phoneConfirmationView;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$resendCode$1(dialogInterface, i);
    }
}
