package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import java.util.ArrayList;

/* loaded from: classes.dex */
public final class CardRequirements extends AbstractSafeParcelable {
    public static final Parcelable.Creator<CardRequirements> CREATOR = new zzg();
    ArrayList zza;
    boolean zzb;
    boolean zzc;
    int zzd;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CardRequirements(ArrayList arrayList, boolean z, boolean z2, int i) {
        this.zza = arrayList;
        this.zzb = z;
        this.zzc = z2;
        this.zzd = i;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeIntegerList(parcel, 1, this.zza, false);
        SafeParcelWriter.writeBoolean(parcel, 2, this.zzb);
        SafeParcelWriter.writeBoolean(parcel, 3, this.zzc);
        SafeParcelWriter.writeInt(parcel, 4, this.zzd);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
