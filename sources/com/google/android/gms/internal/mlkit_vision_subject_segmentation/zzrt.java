package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import org.telegram.tgnet.ConnectionsManager;

/* loaded from: classes.dex */
public final class zzrt {
    private Integer zza;
    private Integer zzb;
    private Integer zzc;
    private Integer zzd;

    public final zzrt zza(Integer num) {
        this.zzb = Integer.valueOf(num.intValue() & ConnectionsManager.DEFAULT_DATACENTER_ID);
        return this;
    }

    public final zzrt zzb(Integer num) {
        this.zzc = Integer.valueOf(num.intValue() & ConnectionsManager.DEFAULT_DATACENTER_ID);
        return this;
    }

    public final zzrt zzc(Integer num) {
        this.zzd = Integer.valueOf(num.intValue() & ConnectionsManager.DEFAULT_DATACENTER_ID);
        return this;
    }

    public final zzrt zzd(Integer num) {
        this.zza = Integer.valueOf(num.intValue() & ConnectionsManager.DEFAULT_DATACENTER_ID);
        return this;
    }

    public final zzrv zze() {
        return new zzrv(this, null);
    }
}
