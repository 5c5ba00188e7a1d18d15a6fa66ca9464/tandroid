package org.telegram.ui.Components;

import androidx.collection.LongSparseArray;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.ui.Adapters.SearchAdapterHelper;
import org.telegram.ui.Components.InviteMembersBottomSheet;
/* loaded from: classes3.dex */
public final /* synthetic */ class InviteMembersBottomSheet$SearchAdapter$$ExternalSyntheticLambda4 implements SearchAdapterHelper.SearchAdapterHelperDelegate {
    public final /* synthetic */ InviteMembersBottomSheet.SearchAdapter f$0;

    public /* synthetic */ InviteMembersBottomSheet$SearchAdapter$$ExternalSyntheticLambda4(InviteMembersBottomSheet.SearchAdapter searchAdapter) {
        this.f$0 = searchAdapter;
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
