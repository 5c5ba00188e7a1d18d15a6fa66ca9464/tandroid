package org.telegram.ui.Adapters;

import androidx.collection.LongSparseArray;
import java.util.ArrayList;
import org.telegram.ui.Adapters.DialogsSearchAdapter;
/* loaded from: classes3.dex */
public final /* synthetic */ class DialogsSearchAdapter$$ExternalSyntheticLambda22 implements DialogsSearchAdapter.OnRecentSearchLoaded {
    public final /* synthetic */ DialogsSearchAdapter f$0;

    public /* synthetic */ DialogsSearchAdapter$$ExternalSyntheticLambda22(DialogsSearchAdapter dialogsSearchAdapter) {
        this.f$0 = dialogsSearchAdapter;
    }

    @Override // org.telegram.ui.Adapters.DialogsSearchAdapter.OnRecentSearchLoaded
    public final void setRecentSearch(ArrayList arrayList, LongSparseArray longSparseArray) {
        this.f$0.lambda$loadRecentSearch$2(arrayList, longSparseArray);
    }
}
