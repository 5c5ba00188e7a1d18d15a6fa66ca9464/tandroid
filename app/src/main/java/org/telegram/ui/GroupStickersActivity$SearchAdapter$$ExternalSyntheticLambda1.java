package org.telegram.ui;

import java.util.List;
import org.telegram.ui.GroupStickersActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupStickersActivity$SearchAdapter$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ GroupStickersActivity.SearchAdapter f$0;
    public final /* synthetic */ List f$1;
    public final /* synthetic */ List f$2;
    public final /* synthetic */ String f$3;

    public /* synthetic */ GroupStickersActivity$SearchAdapter$$ExternalSyntheticLambda1(GroupStickersActivity.SearchAdapter searchAdapter, List list, List list2, String str) {
        this.f$0 = searchAdapter;
        this.f$1 = list;
        this.f$2 = list2;
        this.f$3 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onSearchStickers$0(this.f$1, this.f$2, this.f$3);
    }
}
