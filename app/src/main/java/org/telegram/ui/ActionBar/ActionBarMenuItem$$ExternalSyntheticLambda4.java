package org.telegram.ui.ActionBar;

import android.view.View;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
/* loaded from: classes3.dex */
public final /* synthetic */ class ActionBarMenuItem$$ExternalSyntheticLambda4 implements View.OnClickListener {
    public final /* synthetic */ ActionBarMenuItem f$0;
    public final /* synthetic */ ActionBarMenuItem.SearchFilterView f$1;

    public /* synthetic */ ActionBarMenuItem$$ExternalSyntheticLambda4(ActionBarMenuItem actionBarMenuItem, ActionBarMenuItem.SearchFilterView searchFilterView) {
        this.f$0 = actionBarMenuItem;
        this.f$1 = searchFilterView;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.lambda$onFiltersChanged$11(this.f$1, view);
    }
}
