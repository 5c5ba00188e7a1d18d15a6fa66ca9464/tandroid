package com.google.android.gms.common.moduleinstall.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* loaded from: classes.dex */
public final class zaf extends com.google.android.gms.internal.base.zaa implements IInterface {
    zaf(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.common.moduleinstall.internal.IModuleInstallService");
    }

    public final void zae(zae zaeVar, ApiFeatureRequest apiFeatureRequest) {
        Parcel zaa = zaa();
        com.google.android.gms.internal.base.zac.zae(zaa, zaeVar);
        com.google.android.gms.internal.base.zac.zad(zaa, apiFeatureRequest);
        zac(1, zaa);
    }

    public final void zag(zae zaeVar, ApiFeatureRequest apiFeatureRequest, zah zahVar) {
        Parcel zaa = zaa();
        com.google.android.gms.internal.base.zac.zae(zaa, zaeVar);
        com.google.android.gms.internal.base.zac.zad(zaa, apiFeatureRequest);
        com.google.android.gms.internal.base.zac.zae(zaa, zahVar);
        zac(2, zaa);
    }
}
