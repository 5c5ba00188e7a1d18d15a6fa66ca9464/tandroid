package org.telegram.ui.Business;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$TL_businessWeeklyOpen;
/* loaded from: classes.dex */
public final /* synthetic */ class OpeningHoursActivity$$ExternalSyntheticLambda3 implements Comparator {
    public static final /* synthetic */ OpeningHoursActivity$$ExternalSyntheticLambda3 INSTANCE = new OpeningHoursActivity$$ExternalSyntheticLambda3();

    private /* synthetic */ OpeningHoursActivity$$ExternalSyntheticLambda3() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$adaptWeeklyOpen$0;
        lambda$adaptWeeklyOpen$0 = OpeningHoursActivity.lambda$adaptWeeklyOpen$0((TLRPC$TL_businessWeeklyOpen) obj, (TLRPC$TL_businessWeeklyOpen) obj2);
        return lambda$adaptWeeklyOpen$0;
    }
}
