package com.google.android.gms.common.api.internal;

import android.content.Context;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-base@@18.1.0 */
/* loaded from: classes.dex */
public final class zaak implements Runnable {
    final /* synthetic */ zaaw zaa;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zaak(zaaw zaawVar) {
        this.zaa = zaawVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        GoogleApiAvailabilityLight googleApiAvailabilityLight;
        Context context;
        zaaw zaawVar = this.zaa;
        googleApiAvailabilityLight = zaawVar.zad;
        context = zaawVar.zac;
        googleApiAvailabilityLight.cancelAvailabilityErrorNotifications(context);
    }
}
