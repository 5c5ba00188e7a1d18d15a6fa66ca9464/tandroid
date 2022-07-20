package org.telegram.ui;

import java.util.Comparator;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.PremiumPreviewFragment;
/* loaded from: classes3.dex */
public final /* synthetic */ class PremiumPreviewFragment$$ExternalSyntheticLambda4 implements Comparator {
    public final /* synthetic */ MessagesController f$0;

    public /* synthetic */ PremiumPreviewFragment$$ExternalSyntheticLambda4(MessagesController messagesController) {
        this.f$0 = messagesController;
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$fillPremiumFeaturesList$3;
        lambda$fillPremiumFeaturesList$3 = PremiumPreviewFragment.lambda$fillPremiumFeaturesList$3(this.f$0, (PremiumPreviewFragment.PremiumFeatureData) obj, (PremiumPreviewFragment.PremiumFeatureData) obj2);
        return lambda$fillPremiumFeaturesList$3;
    }
}
