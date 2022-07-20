package org.telegram.ui;

import android.view.View;
import androidx.recyclerview.widget.ItemTouchHelper;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class LaunchActivity$$ExternalSyntheticLambda95 implements RecyclerListView.OnItemLongClickListener {
    public final /* synthetic */ LaunchActivity f$0;
    public final /* synthetic */ ItemTouchHelper f$1;

    public /* synthetic */ LaunchActivity$$ExternalSyntheticLambda95(LaunchActivity launchActivity, ItemTouchHelper itemTouchHelper) {
        this.f$0 = launchActivity;
        this.f$1 = itemTouchHelper;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
    public final boolean onItemClick(View view, int i) {
        boolean lambda$onCreate$4;
        lambda$onCreate$4 = this.f$0.lambda$onCreate$4(this.f$1, view, i);
        return lambda$onCreate$4;
    }
}
