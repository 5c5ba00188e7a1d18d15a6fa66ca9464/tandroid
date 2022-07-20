package org.telegram.ui.Components.Premium;

import androidx.core.util.Consumer;
import com.android.billingclient.api.BillingResult;
/* loaded from: classes3.dex */
public final /* synthetic */ class GiftPremiumBottomSheet$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ GiftPremiumBottomSheet f$0;

    public /* synthetic */ GiftPremiumBottomSheet$$ExternalSyntheticLambda1(GiftPremiumBottomSheet giftPremiumBottomSheet) {
        this.f$0 = giftPremiumBottomSheet;
    }

    @Override // androidx.core.util.Consumer
    public final void accept(Object obj) {
        this.f$0.lambda$onGiftPremium$6((BillingResult) obj);
    }
}
