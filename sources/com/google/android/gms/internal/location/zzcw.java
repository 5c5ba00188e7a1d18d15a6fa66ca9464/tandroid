package com.google.android.gms.internal.location;

import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationResult;

/* loaded from: classes.dex */
final class zzcw extends com.google.android.gms.location.zzq {
    private final zzcs zza;

    zzcw(zzcs zzcsVar) {
        this.zza = zzcsVar;
    }

    @Override // com.google.android.gms.location.zzr
    public final void zzd(LocationAvailability locationAvailability) {
        this.zza.zza().notifyListener(new zzcu(this, locationAvailability));
    }

    @Override // com.google.android.gms.location.zzr
    public final void zze(LocationResult locationResult) {
        this.zza.zza().notifyListener(new zzct(this, locationResult));
    }

    @Override // com.google.android.gms.location.zzr
    public final void zzf() {
        this.zza.zza().notifyListener(new zzcv(this));
    }

    final zzcw zzg(ListenerHolder listenerHolder) {
        this.zza.zzc(listenerHolder);
        return this;
    }

    final void zzh() {
        this.zza.zza().clear();
    }
}
