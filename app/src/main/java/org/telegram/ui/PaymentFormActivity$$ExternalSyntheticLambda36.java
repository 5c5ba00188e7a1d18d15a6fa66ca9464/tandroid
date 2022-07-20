package org.telegram.ui;

import org.telegram.tgnet.TLObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class PaymentFormActivity$$ExternalSyntheticLambda36 implements Runnable {
    public final /* synthetic */ PaymentFormActivity f$0;
    public final /* synthetic */ TLObject f$1;

    public /* synthetic */ PaymentFormActivity$$ExternalSyntheticLambda36(PaymentFormActivity paymentFormActivity, TLObject tLObject) {
        this.f$0 = paymentFormActivity;
        this.f$1 = tLObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$sendData$56(this.f$1);
    }
}
