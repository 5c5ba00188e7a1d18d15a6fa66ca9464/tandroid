package com.google.android.gms.internal.icing;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* loaded from: classes.dex */
public final class zzaa extends zza implements IInterface {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzaa(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.appdatasearch.internal.ILightweightAppDataSearch");
    }

    public final void zze(zzac zzacVar, com.google.firebase.appindexing.internal.zzc[] zzcVarArr) {
        Parcel zza = zza();
        zzc.zzc(zza, zzacVar);
        zza.writeTypedArray(zzcVarArr, 0);
        zzc(7, zza);
    }
}
