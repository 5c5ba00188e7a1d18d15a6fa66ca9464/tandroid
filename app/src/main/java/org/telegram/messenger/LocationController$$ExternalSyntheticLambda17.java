package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class LocationController$$ExternalSyntheticLambda17 implements Runnable {
    public final /* synthetic */ LocationController f$0;
    public final /* synthetic */ ArrayList f$1;

    public /* synthetic */ LocationController$$ExternalSyntheticLambda17(LocationController locationController, ArrayList arrayList) {
        this.f$0 = locationController;
        this.f$1 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadSharingLocations$16(this.f$1);
    }
}
