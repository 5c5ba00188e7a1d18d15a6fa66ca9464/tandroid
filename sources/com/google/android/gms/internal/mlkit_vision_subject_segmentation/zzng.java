package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import org.telegram.tgnet.ConnectionsManager;

/* loaded from: classes.dex */
public final class zzng {
    private zznh zza;
    private Integer zzb;

    public final zzng zza(zznh zznhVar) {
        this.zza = zznhVar;
        return this;
    }

    public final zzng zzb(Integer num) {
        this.zzb = Integer.valueOf(num.intValue() & ConnectionsManager.DEFAULT_DATACENTER_ID);
        return this;
    }

    public final zznj zzd() {
        return new zznj(this, null);
    }
}
