package org.telegram.ui;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class PaymentFormActivity$$ExternalSyntheticLambda4 implements DialogInterface.OnClickListener {
    public final /* synthetic */ PaymentFormActivity f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ PaymentFormActivity$$ExternalSyntheticLambda4(PaymentFormActivity paymentFormActivity, String str) {
        this.f$0 = paymentFormActivity;
        this.f$1 = str;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$sendSavePassword$44(this.f$1, dialogInterface, i);
    }
}
