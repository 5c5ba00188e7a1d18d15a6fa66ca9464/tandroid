package org.telegram.ui;

import android.view.View;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ContactsActivity$$ExternalSyntheticLambda9 implements RecyclerListView.OnItemClickListener {
    public final /* synthetic */ ContactsActivity f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ ContactsActivity$$ExternalSyntheticLambda9(ContactsActivity contactsActivity, int i) {
        this.f$0 = contactsActivity;
        this.f$1 = i;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
    public final void onItemClick(View view, int i) {
        this.f$0.lambda$createView$1(this.f$1, view, i);
    }
}
