package org.telegram.ui;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCreateFinalActivity$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ GroupCreateFinalActivity f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ ArrayList f$2;
    public final /* synthetic */ CountDownLatch f$3;

    public /* synthetic */ GroupCreateFinalActivity$$ExternalSyntheticLambda5(GroupCreateFinalActivity groupCreateFinalActivity, ArrayList arrayList, ArrayList arrayList2, CountDownLatch countDownLatch) {
        this.f$0 = groupCreateFinalActivity;
        this.f$1 = arrayList;
        this.f$2 = arrayList2;
        this.f$3 = countDownLatch;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onFragmentCreate$0(this.f$1, this.f$2, this.f$3);
    }
}
