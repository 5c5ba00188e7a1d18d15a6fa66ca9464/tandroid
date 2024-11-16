package com.google.android.gms.internal.phenotype;

import android.os.IBinder;
import android.os.IInterface;

/* loaded from: classes.dex */
public abstract class zza implements IInterface {
    private final IBinder zza;
    private final String zzb;

    protected zza(IBinder iBinder, String str) {
        this.zza = iBinder;
        this.zzb = str;
    }

    @Override // android.os.IInterface
    public IBinder asBinder() {
        return this.zza;
    }
}
