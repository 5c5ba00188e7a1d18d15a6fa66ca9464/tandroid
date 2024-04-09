package com.google.android.gms.internal.mlkit_vision_label;

import org.telegram.tgnet.ConnectionsManager;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
public final class zzde {
    private zzdh zza;
    private Integer zzb;
    private zzjn zzc;

    public final zzde zza(Integer num) {
        this.zzb = Integer.valueOf(num.intValue() & ConnectionsManager.DEFAULT_DATACENTER_ID);
        return this;
    }

    public final zzde zzb(zzjn zzjnVar) {
        this.zzc = zzjnVar;
        return this;
    }

    public final zzde zzc(zzdh zzdhVar) {
        this.zza = zzdhVar;
        return this;
    }

    public final zzdj zze() {
        return new zzdj(this, null);
    }
}
