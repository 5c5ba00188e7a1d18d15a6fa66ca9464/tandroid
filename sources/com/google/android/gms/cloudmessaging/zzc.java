package com.google.android.gms.cloudmessaging;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
final class zzc implements Parcelable.Creator {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        return new zza(parcel.readStrongBinder());
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Object[] newArray(int i) {
        return new zza[i];
    }
}
