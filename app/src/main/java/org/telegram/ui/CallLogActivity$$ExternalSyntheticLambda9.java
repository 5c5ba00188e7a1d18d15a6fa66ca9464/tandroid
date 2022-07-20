package org.telegram.ui;

import android.view.View;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class CallLogActivity$$ExternalSyntheticLambda9 implements RecyclerListView.OnItemLongClickListener {
    public final /* synthetic */ CallLogActivity f$0;

    public /* synthetic */ CallLogActivity$$ExternalSyntheticLambda9(CallLogActivity callLogActivity) {
        this.f$0 = callLogActivity;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
    public final boolean onItemClick(View view, int i) {
        boolean lambda$createView$1;
        lambda$createView$1 = this.f$0.lambda$createView$1(view, i);
        return lambda$createView$1;
    }
}
