package com.google.android.gms.internal.safetynet;

import android.os.IInterface;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafeBrowsingData;

/* loaded from: classes.dex */
public interface zzg extends IInterface {
    void zzb(Status status, boolean z);

    void zzc(Status status);

    void zzd(Status status, com.google.android.gms.safetynet.zza zzaVar);

    void zze(String str);

    void zzf(Status status, boolean z);

    void zzg(Status status, com.google.android.gms.safetynet.zzd zzdVar);

    void zzh(Status status, com.google.android.gms.safetynet.zzf zzfVar);

    void zzi(Status status, com.google.android.gms.safetynet.zzh zzhVar);

    void zzj(Status status, SafeBrowsingData safeBrowsingData);

    void zzk(Status status, String str, int i);
}
