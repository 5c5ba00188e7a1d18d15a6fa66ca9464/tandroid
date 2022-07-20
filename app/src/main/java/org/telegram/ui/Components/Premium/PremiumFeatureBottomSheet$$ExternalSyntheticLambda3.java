package org.telegram.ui.Components.Premium;

import java.util.Comparator;
import java.util.HashMap;
/* loaded from: classes3.dex */
public final /* synthetic */ class PremiumFeatureBottomSheet$$ExternalSyntheticLambda3 implements Comparator {
    public final /* synthetic */ HashMap f$0;

    public /* synthetic */ PremiumFeatureBottomSheet$$ExternalSyntheticLambda3(HashMap hashMap) {
        this.f$0 = hashMap;
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$getViewForPosition$3;
        lambda$getViewForPosition$3 = PremiumFeatureBottomSheet.lambda$getViewForPosition$3(this.f$0, (ReactionDrawingObject) obj, (ReactionDrawingObject) obj2);
        return lambda$getViewForPosition$3;
    }
}
