package org.telegram.ui.Components;

import android.content.DialogInterface;
import java.util.ArrayList;
/* loaded from: classes3.dex */
public final /* synthetic */ class SearchViewPager$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ SearchViewPager f$0;
    public final /* synthetic */ ArrayList f$1;

    public /* synthetic */ SearchViewPager$$ExternalSyntheticLambda0(SearchViewPager searchViewPager, ArrayList arrayList) {
        this.f$0 = searchViewPager;
        this.f$1 = arrayList;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onActionBarItemClick$2(this.f$1, dialogInterface, i);
    }
}
