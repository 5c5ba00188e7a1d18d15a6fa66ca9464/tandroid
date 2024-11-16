package com.google.android.gms.internal.clearcut;

import android.os.IBinder;
import android.os.Parcel;

/* loaded from: classes.dex */
public final class zzo extends zza implements zzn {
    zzo(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.clearcut.internal.IClearcutLoggerService");
    }

    @Override // com.google.android.gms.internal.clearcut.zzn
    public final void zza(zzl zzlVar, com.google.android.gms.clearcut.zze zzeVar) {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, zzlVar);
        zzc.zza(obtainAndWriteInterfaceToken, zzeVar);
        transactOneway(1, obtainAndWriteInterfaceToken);
    }
}
