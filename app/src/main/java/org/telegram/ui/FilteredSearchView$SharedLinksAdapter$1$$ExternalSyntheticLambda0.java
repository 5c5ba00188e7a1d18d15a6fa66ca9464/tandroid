package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.ui.FilteredSearchView;
/* loaded from: classes3.dex */
public final /* synthetic */ class FilteredSearchView$SharedLinksAdapter$1$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ FilteredSearchView.SharedLinksAdapter.AnonymousClass1 f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ FilteredSearchView$SharedLinksAdapter$1$$ExternalSyntheticLambda0(FilteredSearchView.SharedLinksAdapter.AnonymousClass1 anonymousClass1, String str) {
        this.f$0 = anonymousClass1;
        this.f$1 = str;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onLinkPress$0(this.f$1, dialogInterface, i);
    }
}
