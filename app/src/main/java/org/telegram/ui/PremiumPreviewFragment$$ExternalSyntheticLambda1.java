package org.telegram.ui;

import androidx.core.util.Consumer;
import com.android.billingclient.api.BillingResult;
/* loaded from: classes3.dex */
public final /* synthetic */ class PremiumPreviewFragment$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ Runnable f$0;

    public /* synthetic */ PremiumPreviewFragment$$ExternalSyntheticLambda1(Runnable runnable) {
        this.f$0 = runnable;
    }

    @Override // androidx.core.util.Consumer
    public final void accept(Object obj) {
        PremiumPreviewFragment.lambda$buyPremium$7(this.f$0, (BillingResult) obj);
    }
}
