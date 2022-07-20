package org.telegram.ui;

import androidx.collection.LongSparseArray;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.ui.Adapters.SearchAdapterHelper;
import org.telegram.ui.FilterUsersActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class FilterUsersActivity$GroupCreateAdapter$$ExternalSyntheticLambda4 implements SearchAdapterHelper.SearchAdapterHelperDelegate {
    public final /* synthetic */ FilterUsersActivity.GroupCreateAdapter f$0;

    public /* synthetic */ FilterUsersActivity$GroupCreateAdapter$$ExternalSyntheticLambda4(FilterUsersActivity.GroupCreateAdapter groupCreateAdapter) {
        this.f$0 = groupCreateAdapter;
    }

    @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
    public /* synthetic */ boolean canApplySearchResults(int i) {
        return SearchAdapterHelper.SearchAdapterHelperDelegate.CC.$default$canApplySearchResults(this, i);
    }

    @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
    public /* synthetic */ LongSparseArray getExcludeCallParticipants() {
        return SearchAdapterHelper.SearchAdapterHelperDelegate.CC.$default$getExcludeCallParticipants(this);
    }

    @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
    public /* synthetic */ LongSparseArray getExcludeUsers() {
        return SearchAdapterHelper.SearchAdapterHelperDelegate.CC.$default$getExcludeUsers(this);
    }

    @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
    public final void onDataSetChanged(int i) {
        this.f$0.lambda$new$0(i);
    }

    @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
    public /* synthetic */ void onSetHashtags(ArrayList arrayList, HashMap hashMap) {
        SearchAdapterHelper.SearchAdapterHelperDelegate.CC.$default$onSetHashtags(this, arrayList, hashMap);
    }
}
