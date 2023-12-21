package org.telegram.ui.Components.Premium.boosts;

import j$.util.function.ToLongFunction;
import org.telegram.tgnet.TLRPC$TL_premiumGiftCodeOption;
/* loaded from: classes4.dex */
public final /* synthetic */ class PremiumPreviewGiftToUsersBottomSheet$$ExternalSyntheticLambda2 implements ToLongFunction {
    public static final /* synthetic */ PremiumPreviewGiftToUsersBottomSheet$$ExternalSyntheticLambda2 INSTANCE = new PremiumPreviewGiftToUsersBottomSheet$$ExternalSyntheticLambda2();

    private /* synthetic */ PremiumPreviewGiftToUsersBottomSheet$$ExternalSyntheticLambda2() {
    }

    @Override // j$.util.function.ToLongFunction
    public final long applyAsLong(Object obj) {
        long j;
        j = ((TLRPC$TL_premiumGiftCodeOption) obj).amount;
        return j;
    }
}
