package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.ui.FilterUsersActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class FilterUsersActivity$GroupCreateAdapter$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ FilterUsersActivity.GroupCreateAdapter f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ ArrayList f$2;

    public /* synthetic */ FilterUsersActivity$GroupCreateAdapter$$ExternalSyntheticLambda3(FilterUsersActivity.GroupCreateAdapter groupCreateAdapter, ArrayList arrayList, ArrayList arrayList2) {
        this.f$0 = groupCreateAdapter;
        this.f$1 = arrayList;
        this.f$2 = arrayList2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$updateSearchResults$4(this.f$1, this.f$2);
    }
}
