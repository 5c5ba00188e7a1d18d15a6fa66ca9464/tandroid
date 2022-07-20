package org.telegram.ui;

import android.view.View;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ProxyListActivity$$ExternalSyntheticLambda4 implements RecyclerListView.OnItemLongClickListener {
    public final /* synthetic */ ProxyListActivity f$0;

    public /* synthetic */ ProxyListActivity$$ExternalSyntheticLambda4(ProxyListActivity proxyListActivity) {
        this.f$0 = proxyListActivity;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
    public final boolean onItemClick(View view, int i) {
        boolean lambda$createView$2;
        lambda$createView$2 = this.f$0.lambda$createView$2(view, i);
        return lambda$createView$2;
    }
}
