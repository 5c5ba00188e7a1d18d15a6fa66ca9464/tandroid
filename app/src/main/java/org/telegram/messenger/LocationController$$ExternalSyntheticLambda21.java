package org.telegram.messenger;

import org.telegram.messenger.LocationController;
/* loaded from: classes.dex */
public final /* synthetic */ class LocationController$$ExternalSyntheticLambda21 implements Runnable {
    public final /* synthetic */ LocationController f$0;
    public final /* synthetic */ LocationController.SharingLocationInfo f$1;

    public /* synthetic */ LocationController$$ExternalSyntheticLambda21(LocationController locationController, LocationController.SharingLocationInfo sharingLocationInfo) {
        this.f$0 = locationController;
        this.f$1 = sharingLocationInfo;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$update$9(this.f$1);
    }
}
