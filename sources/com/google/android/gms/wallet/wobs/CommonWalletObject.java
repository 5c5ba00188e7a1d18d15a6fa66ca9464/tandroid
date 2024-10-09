package com.google.android.gms.wallet.wobs;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.util.ArrayUtils;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class CommonWalletObject extends AbstractSafeParcelable {
    public static final Parcelable.Creator<CommonWalletObject> CREATOR = new zzc();
    String zza;
    String zzb;
    String zzc;
    String zzd;
    String zze;
    String zzf;
    String zzg;
    String zzh;
    int zzi;
    ArrayList zzj;
    TimeInterval zzk;
    ArrayList zzl;
    String zzm;
    String zzn;
    ArrayList zzo;
    boolean zzp;
    ArrayList zzq;
    ArrayList zzr;
    ArrayList zzs;

    CommonWalletObject() {
        this.zzj = ArrayUtils.newArrayList();
        this.zzl = ArrayUtils.newArrayList();
        this.zzo = ArrayUtils.newArrayList();
        this.zzq = ArrayUtils.newArrayList();
        this.zzr = ArrayUtils.newArrayList();
        this.zzs = ArrayUtils.newArrayList();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CommonWalletObject(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, int i, ArrayList arrayList, TimeInterval timeInterval, ArrayList arrayList2, String str9, String str10, ArrayList arrayList3, boolean z, ArrayList arrayList4, ArrayList arrayList5, ArrayList arrayList6) {
        this.zza = str;
        this.zzb = str2;
        this.zzc = str3;
        this.zzd = str4;
        this.zze = str5;
        this.zzf = str6;
        this.zzg = str7;
        this.zzh = str8;
        this.zzi = i;
        this.zzj = arrayList;
        this.zzk = timeInterval;
        this.zzl = arrayList2;
        this.zzm = str9;
        this.zzn = str10;
        this.zzo = arrayList3;
        this.zzp = z;
        this.zzq = arrayList4;
        this.zzr = arrayList5;
        this.zzs = arrayList6;
    }

    public static zzb zzb() {
        return new zzb(new CommonWalletObject(), null);
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zza, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzb, false);
        SafeParcelWriter.writeString(parcel, 4, this.zzc, false);
        SafeParcelWriter.writeString(parcel, 5, this.zzd, false);
        SafeParcelWriter.writeString(parcel, 6, this.zze, false);
        SafeParcelWriter.writeString(parcel, 7, this.zzf, false);
        SafeParcelWriter.writeString(parcel, 8, this.zzg, false);
        SafeParcelWriter.writeString(parcel, 9, this.zzh, false);
        SafeParcelWriter.writeInt(parcel, 10, this.zzi);
        SafeParcelWriter.writeTypedList(parcel, 11, this.zzj, false);
        SafeParcelWriter.writeParcelable(parcel, 12, this.zzk, i, false);
        SafeParcelWriter.writeTypedList(parcel, 13, this.zzl, false);
        SafeParcelWriter.writeString(parcel, 14, this.zzm, false);
        SafeParcelWriter.writeString(parcel, 15, this.zzn, false);
        SafeParcelWriter.writeTypedList(parcel, 16, this.zzo, false);
        SafeParcelWriter.writeBoolean(parcel, 17, this.zzp);
        SafeParcelWriter.writeTypedList(parcel, 18, this.zzq, false);
        SafeParcelWriter.writeTypedList(parcel, 19, this.zzr, false);
        SafeParcelWriter.writeTypedList(parcel, 20, this.zzs, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
