package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.ui.TooManyCommunitiesActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class TooManyCommunitiesActivity$SearchAdapter$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ TooManyCommunitiesActivity.SearchAdapter f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ ArrayList f$2;
    public final /* synthetic */ ArrayList f$3;

    public /* synthetic */ TooManyCommunitiesActivity$SearchAdapter$$ExternalSyntheticLambda0(TooManyCommunitiesActivity.SearchAdapter searchAdapter, int i, ArrayList arrayList, ArrayList arrayList2) {
        this.f$0 = searchAdapter;
        this.f$1 = i;
        this.f$2 = arrayList;
        this.f$3 = arrayList2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$updateSearchResults$2(this.f$1, this.f$2, this.f$3);
    }
}
