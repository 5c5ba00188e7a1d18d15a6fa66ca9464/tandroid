package org.telegram.ui.Components;

import android.view.View;
import org.telegram.ui.Components.EmojiView;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class EmojiView$StickersGridAdapter$$ExternalSyntheticLambda4 implements RecyclerListView.OnItemClickListener {
    public final /* synthetic */ EmojiView.StickersGridAdapter f$0;

    public /* synthetic */ EmojiView$StickersGridAdapter$$ExternalSyntheticLambda4(EmojiView.StickersGridAdapter stickersGridAdapter) {
        this.f$0 = stickersGridAdapter;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
    public final void onItemClick(View view, int i) {
        this.f$0.lambda$onCreateViewHolder$4(view, i);
    }
}
