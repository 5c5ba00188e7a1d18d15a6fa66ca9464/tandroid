package com.google.android.gms.internal.safetynet;

import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafeBrowsingData;
/* compiled from: com.google.android.gms:play-services-safetynet@@17.0.1 */
/* loaded from: classes.dex */
public interface zzg extends IInterface {
    void zzb(Status status, boolean z) throws RemoteException;

    void zzc(Status status) throws RemoteException;

    void zzd(Status status, com.google.android.gms.safetynet.zza zzaVar) throws RemoteException;

    void zze(String str) throws RemoteException;

    void zzf(Status status, boolean z) throws RemoteException;

    void zzg(Status status, com.google.android.gms.safetynet.zzd zzdVar) throws RemoteException;

    void zzh(Status status, com.google.android.gms.safetynet.zzf zzfVar) throws RemoteException;

    void zzi(Status status, com.google.android.gms.safetynet.zzh zzhVar) throws RemoteException;

    void zzj(Status status, SafeBrowsingData safeBrowsingData) throws RemoteException;

    void zzk(Status status, String str, int i) throws RemoteException;
}
