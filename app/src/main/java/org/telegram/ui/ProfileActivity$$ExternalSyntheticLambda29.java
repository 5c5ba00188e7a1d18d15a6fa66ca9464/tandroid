package org.telegram.ui;

import java.util.Comparator;
/* loaded from: classes3.dex */
public final /* synthetic */ class ProfileActivity$$ExternalSyntheticLambda29 implements Comparator {
    public final /* synthetic */ ProfileActivity f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ ProfileActivity$$ExternalSyntheticLambda29(ProfileActivity profileActivity, int i) {
        this.f$0 = profileActivity;
        this.f$1 = i;
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$updateOnlineCount$31;
        lambda$updateOnlineCount$31 = this.f$0.lambda$updateOnlineCount$31(this.f$1, (Integer) obj, (Integer) obj2);
        return lambda$updateOnlineCount$31;
    }
}
