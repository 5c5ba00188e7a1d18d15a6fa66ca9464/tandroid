package org.telegram.ui.Components.Premium;

import android.graphics.Paint;
import org.telegram.messenger.GenericProvider;
import org.telegram.ui.Components.Premium.GiftPremiumBottomSheet;
/* loaded from: classes3.dex */
public final /* synthetic */ class GiftPremiumBottomSheet$1$$ExternalSyntheticLambda0 implements GenericProvider {
    public final /* synthetic */ GiftPremiumBottomSheet.AnonymousClass1 f$0;
    public final /* synthetic */ PremiumGiftTierCell f$1;

    public /* synthetic */ GiftPremiumBottomSheet$1$$ExternalSyntheticLambda0(GiftPremiumBottomSheet.AnonymousClass1 anonymousClass1, PremiumGiftTierCell premiumGiftTierCell) {
        this.f$0 = anonymousClass1;
        this.f$1 = premiumGiftTierCell;
    }

    @Override // org.telegram.messenger.GenericProvider
    public final Object provide(Object obj) {
        Paint lambda$onCreateViewHolder$0;
        lambda$onCreateViewHolder$0 = this.f$0.lambda$onCreateViewHolder$0(this.f$1, (Void) obj);
        return lambda$onCreateViewHolder$0;
    }
}
