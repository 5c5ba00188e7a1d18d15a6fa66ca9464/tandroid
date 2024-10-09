package com.google.android.gms.common.internal;

import android.os.Parcel;
import com.google.android.gms.dynamic.IObjectWrapper;

/* loaded from: classes.dex */
public abstract class zzy extends com.google.android.gms.internal.common.zzb implements zzz {
    public zzy() {
        super("com.google.android.gms.common.internal.ICertData");
    }

    @Override // com.google.android.gms.internal.common.zzb
    protected final boolean zza(int i, Parcel parcel, Parcel parcel2, int i2) {
        if (i == 1) {
            IObjectWrapper zzd = zzd();
            parcel2.writeNoException();
            com.google.android.gms.internal.common.zzc.zzf(parcel2, zzd);
        } else {
            if (i != 2) {
                return false;
            }
            int zzc = zzc();
            parcel2.writeNoException();
            parcel2.writeInt(zzc);
        }
        return true;
    }
}
