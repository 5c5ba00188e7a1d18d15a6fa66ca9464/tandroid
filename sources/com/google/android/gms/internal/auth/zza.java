package com.google.android.gms.internal.auth;

import android.os.IBinder;
import android.os.IInterface;

/* loaded from: classes.dex */
public abstract class zza implements IInterface {
    private final IBinder zza;
    private final String zzb;

    /* JADX INFO: Access modifiers changed from: protected */
    public zza(IBinder iBinder, String str) {
        this.zza = iBinder;
        this.zzb = str;
    }

    @Override // android.os.IInterface
    public final IBinder asBinder() {
        return this.zza;
    }
}
