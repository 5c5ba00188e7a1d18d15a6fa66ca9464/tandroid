package org.telegram.ui.Components;

import org.telegram.ui.Components.SharedMediaLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class SharedMediaLayout$GroupUsersSearchAdapter$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ SharedMediaLayout.GroupUsersSearchAdapter f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ SharedMediaLayout$GroupUsersSearchAdapter$$ExternalSyntheticLambda1(SharedMediaLayout.GroupUsersSearchAdapter groupUsersSearchAdapter, String str) {
        this.f$0 = groupUsersSearchAdapter;
        this.f$1 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processSearch$3(this.f$1);
    }
}
