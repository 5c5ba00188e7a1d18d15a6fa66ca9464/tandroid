package org.telegram.ui.Components;

import java.util.ArrayList;
import org.telegram.ui.Components.SharedMediaLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class SharedMediaLayout$GroupUsersSearchAdapter$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ SharedMediaLayout.GroupUsersSearchAdapter f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ ArrayList f$2;

    public /* synthetic */ SharedMediaLayout$GroupUsersSearchAdapter$$ExternalSyntheticLambda2(SharedMediaLayout.GroupUsersSearchAdapter groupUsersSearchAdapter, String str, ArrayList arrayList) {
        this.f$0 = groupUsersSearchAdapter;
        this.f$1 = str;
        this.f$2 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processSearch$2(this.f$1, this.f$2);
    }
}
