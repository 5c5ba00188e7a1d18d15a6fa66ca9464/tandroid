package org.telegram.ui.Components.Premium;

import android.view.View;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.PremiumPreviewFragment;
/* loaded from: classes3.dex */
public final /* synthetic */ class PremiumFeatureBottomSheet$$ExternalSyntheticLambda2 implements View.OnClickListener {
    public final /* synthetic */ PremiumFeatureBottomSheet f$0;
    public final /* synthetic */ BaseFragment f$1;
    public final /* synthetic */ boolean f$2;
    public final /* synthetic */ PremiumPreviewFragment.PremiumFeatureData f$3;

    public /* synthetic */ PremiumFeatureBottomSheet$$ExternalSyntheticLambda2(PremiumFeatureBottomSheet premiumFeatureBottomSheet, BaseFragment baseFragment, boolean z, PremiumPreviewFragment.PremiumFeatureData premiumFeatureData) {
        this.f$0 = premiumFeatureBottomSheet;
        this.f$1 = baseFragment;
        this.f$2 = z;
        this.f$3 = premiumFeatureData;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.lambda$new$1(this.f$1, this.f$2, this.f$3, view);
    }
}
