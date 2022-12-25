package com.google.android.gms.internal.location;

import android.app.PendingIntent;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.location.zzt;
import com.google.android.gms.location.zzu;
/* compiled from: com.google.android.gms:play-services-location@@21.0.1 */
@Deprecated
/* loaded from: classes.dex */
public final class zzdf extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzdf> CREATOR = new zzdg();
    private final int zza;
    private final zzdd zzb;
    private final zzu zzc;
    private final com.google.android.gms.location.zzr zzd;
    private final PendingIntent zze;
    private final zzk zzf;
    private final String zzg;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzdf(int i, zzdd zzddVar, IBinder iBinder, IBinder iBinder2, PendingIntent pendingIntent, IBinder iBinder3, String str) {
        this.zza = i;
        this.zzb = zzddVar;
        zzk zzkVar = null;
        this.zzc = iBinder != null ? zzt.zzb(iBinder) : null;
        this.zze = pendingIntent;
        this.zzd = iBinder2 != null ? com.google.android.gms.location.zzq.zzb(iBinder2) : null;
        if (iBinder3 != null) {
            IInterface queryLocalInterface = iBinder3.queryLocalInterface("com.google.android.gms.location.internal.IFusedLocationProviderCallback");
            zzkVar = queryLocalInterface instanceof zzk ? (zzk) queryLocalInterface : new zzi(iBinder3);
        }
        this.zzf = zzkVar;
        this.zzg = str;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zza);
        SafeParcelWriter.writeParcelable(parcel, 2, this.zzb, i, false);
        zzu zzuVar = this.zzc;
        SafeParcelWriter.writeIBinder(parcel, 3, zzuVar == null ? null : zzuVar.asBinder(), false);
        SafeParcelWriter.writeParcelable(parcel, 4, this.zze, i, false);
        com.google.android.gms.location.zzr zzrVar = this.zzd;
        SafeParcelWriter.writeIBinder(parcel, 5, zzrVar == null ? null : zzrVar.asBinder(), false);
        zzk zzkVar = this.zzf;
        SafeParcelWriter.writeIBinder(parcel, 6, zzkVar != null ? zzkVar.asBinder() : null, false);
        SafeParcelWriter.writeString(parcel, 8, this.zzg, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
