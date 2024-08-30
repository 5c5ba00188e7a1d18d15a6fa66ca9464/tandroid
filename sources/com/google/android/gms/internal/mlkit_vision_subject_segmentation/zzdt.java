package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import org.telegram.tgnet.ConnectionsManager;
/* loaded from: classes.dex */
public final class zzdt {
    private zzdw zza;
    private Integer zzb;
    private zznc zzc;

    public final zzdt zza(Integer num) {
        this.zzb = Integer.valueOf(num.intValue() & ConnectionsManager.DEFAULT_DATACENTER_ID);
        return this;
    }

    public final zzdt zzb(zznc zzncVar) {
        this.zzc = zzncVar;
        return this;
    }

    public final zzdt zzc(zzdw zzdwVar) {
        this.zza = zzdwVar;
        return this;
    }

    public final zzdy zze() {
        return new zzdy(this, null);
    }
}
