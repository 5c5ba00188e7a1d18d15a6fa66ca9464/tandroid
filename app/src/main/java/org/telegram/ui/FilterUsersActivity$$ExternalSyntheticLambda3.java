package org.telegram.ui;

import android.content.Context;
import android.view.View;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class FilterUsersActivity$$ExternalSyntheticLambda3 implements RecyclerListView.OnItemClickListener {
    public final /* synthetic */ FilterUsersActivity f$0;
    public final /* synthetic */ Context f$1;

    public /* synthetic */ FilterUsersActivity$$ExternalSyntheticLambda3(FilterUsersActivity filterUsersActivity, Context context) {
        this.f$0 = filterUsersActivity;
        this.f$1 = context;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
    public final void onItemClick(View view, int i) {
        this.f$0.lambda$createView$1(this.f$1, view, i);
    }
}
