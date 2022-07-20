package org.telegram.ui.Components;

import org.telegram.ui.Components.BotWebViewMenuContainer;
import org.telegram.ui.PaymentFormActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class BotWebViewMenuContainer$1$$ExternalSyntheticLambda3 implements PaymentFormActivity.PaymentFormCallback {
    public final /* synthetic */ BotWebViewMenuContainer.AnonymousClass1 f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ BotWebViewMenuContainer$1$$ExternalSyntheticLambda3(BotWebViewMenuContainer.AnonymousClass1 anonymousClass1, String str) {
        this.f$0 = anonymousClass1;
        this.f$1 = str;
    }

    @Override // org.telegram.ui.PaymentFormActivity.PaymentFormCallback
    public final void onInvoiceStatusChanged(PaymentFormActivity.InvoiceStatus invoiceStatus) {
        this.f$0.lambda$onWebAppOpenInvoice$2(this.f$1, invoiceStatus);
    }
}
