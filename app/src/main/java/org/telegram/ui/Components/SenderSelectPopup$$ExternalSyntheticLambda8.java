package org.telegram.ui.Components;

import android.view.View;
import java.util.List;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SenderSelectPopup;
/* loaded from: classes3.dex */
public final /* synthetic */ class SenderSelectPopup$$ExternalSyntheticLambda8 implements RecyclerListView.OnItemClickListener {
    public final /* synthetic */ SenderSelectPopup f$0;
    public final /* synthetic */ SenderSelectPopup.OnSelectCallback f$1;
    public final /* synthetic */ List f$2;

    public /* synthetic */ SenderSelectPopup$$ExternalSyntheticLambda8(SenderSelectPopup senderSelectPopup, SenderSelectPopup.OnSelectCallback onSelectCallback, List list) {
        this.f$0 = senderSelectPopup;
        this.f$1 = onSelectCallback;
        this.f$2 = list;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
    public final void onItemClick(View view, int i) {
        this.f$0.lambda$new$0(this.f$1, this.f$2, view, i);
    }
}
