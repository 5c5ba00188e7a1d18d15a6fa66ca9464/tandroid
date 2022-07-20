package org.telegram.messenger;

import org.telegram.tgnet.TLObject;
/* loaded from: classes.dex */
public final /* synthetic */ class LocationController$$ExternalSyntheticLambda13 implements Runnable {
    public final /* synthetic */ LocationController f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ TLObject f$2;

    public /* synthetic */ LocationController$$ExternalSyntheticLambda13(LocationController locationController, long j, TLObject tLObject) {
        this.f$0 = locationController;
        this.f$1 = j;
        this.f$2 = tLObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadLiveLocations$27(this.f$1, this.f$2);
    }
}
