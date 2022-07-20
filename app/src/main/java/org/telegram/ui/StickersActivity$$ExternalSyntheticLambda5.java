package org.telegram.ui;

import android.view.View;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class StickersActivity$$ExternalSyntheticLambda5 implements RecyclerListView.OnItemLongClickListener {
    public final /* synthetic */ StickersActivity f$0;

    public /* synthetic */ StickersActivity$$ExternalSyntheticLambda5(StickersActivity stickersActivity) {
        this.f$0 = stickersActivity;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
    public final boolean onItemClick(View view, int i) {
        boolean lambda$createView$3;
        lambda$createView$3 = this.f$0.lambda$createView$3(view, i);
        return lambda$createView$3;
    }
}
