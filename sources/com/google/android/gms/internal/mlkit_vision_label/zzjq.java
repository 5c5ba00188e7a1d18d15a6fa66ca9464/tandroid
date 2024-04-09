package com.google.android.gms.internal.mlkit_vision_label;

import org.telegram.tgnet.ConnectionsManager;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
public final class zzjq {
    private zzjr zza;
    private Integer zzb;

    public final zzjq zza(zzjr zzjrVar) {
        this.zza = zzjrVar;
        return this;
    }

    public final zzjq zzb(Integer num) {
        this.zzb = Integer.valueOf(num.intValue() & ConnectionsManager.DEFAULT_DATACENTER_ID);
        return this;
    }

    public final zzjt zzd() {
        return new zzjt(this, null);
    }
}
