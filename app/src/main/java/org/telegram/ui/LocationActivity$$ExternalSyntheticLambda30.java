package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.ui.Adapters.BaseLocationAdapter;
/* loaded from: classes3.dex */
public final /* synthetic */ class LocationActivity$$ExternalSyntheticLambda30 implements BaseLocationAdapter.BaseLocationAdapterDelegate {
    public final /* synthetic */ LocationActivity f$0;

    public /* synthetic */ LocationActivity$$ExternalSyntheticLambda30(LocationActivity locationActivity) {
        this.f$0 = locationActivity;
    }

    @Override // org.telegram.ui.Adapters.BaseLocationAdapter.BaseLocationAdapterDelegate
    public final void didLoadSearchResult(ArrayList arrayList) {
        this.f$0.updatePlacesMarkers(arrayList);
    }
}
