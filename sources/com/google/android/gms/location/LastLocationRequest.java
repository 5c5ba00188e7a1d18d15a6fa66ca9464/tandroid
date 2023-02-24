package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.internal.location.zzd;
import com.google.android.gms.internal.location.zzdj;
/* compiled from: com.google.android.gms:play-services-location@@21.0.1 */
/* loaded from: classes.dex */
public final class LastLocationRequest extends AbstractSafeParcelable {
    public static final Parcelable.Creator<LastLocationRequest> CREATOR = new zzv();
    private final long zza;
    private final int zzb;
    private final boolean zzc;
    private final String zzd;
    private final zzd zze;

    /* compiled from: com.google.android.gms:play-services-location@@21.0.1 */
    /* loaded from: classes.dex */
    public static final class Builder {
        private long zza = Long.MAX_VALUE;
        private int zzb = 0;
        private boolean zzc = false;
        private String zzd = null;
        private zzd zze = null;

        public LastLocationRequest build() {
            return new LastLocationRequest(this.zza, this.zzb, this.zzc, this.zzd, this.zze);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LastLocationRequest(long j, int i, boolean z, String str, zzd zzdVar) {
        this.zza = j;
        this.zzb = i;
        this.zzc = z;
        this.zzd = str;
        this.zze = zzdVar;
    }

    public boolean equals(Object obj) {
        if (obj instanceof LastLocationRequest) {
            LastLocationRequest lastLocationRequest = (LastLocationRequest) obj;
            return this.zza == lastLocationRequest.zza && this.zzb == lastLocationRequest.zzb && this.zzc == lastLocationRequest.zzc && Objects.equal(this.zzd, lastLocationRequest.zzd) && Objects.equal(this.zze, lastLocationRequest.zze);
        }
        return false;
    }

    public int getGranularity() {
        return this.zzb;
    }

    public long getMaxUpdateAgeMillis() {
        return this.zza;
    }

    public int hashCode() {
        return Objects.hashCode(Long.valueOf(this.zza), Integer.valueOf(this.zzb), Boolean.valueOf(this.zzc));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LastLocationRequest[");
        if (this.zza != Long.MAX_VALUE) {
            sb.append("maxAge=");
            zzdj.zzb(this.zza, sb);
        }
        if (this.zzb != 0) {
            sb.append(", ");
            sb.append(zzo.zzb(this.zzb));
        }
        if (this.zzc) {
            sb.append(", bypass");
        }
        if (this.zzd != null) {
            sb.append(", moduleId=");
            sb.append(this.zzd);
        }
        if (this.zze != null) {
            sb.append(", impersonation=");
            sb.append(this.zze);
        }
        sb.append(']');
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeLong(parcel, 1, getMaxUpdateAgeMillis());
        SafeParcelWriter.writeInt(parcel, 2, getGranularity());
        SafeParcelWriter.writeBoolean(parcel, 3, this.zzc);
        SafeParcelWriter.writeString(parcel, 4, this.zzd, false);
        SafeParcelWriter.writeParcelable(parcel, 5, this.zze, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
