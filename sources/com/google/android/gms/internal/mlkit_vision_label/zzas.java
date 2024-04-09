package com.google.android.gms.internal.mlkit_vision_label;

import java.util.Map;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
final class zzas extends zzah {
    final /* synthetic */ zzau zza;
    private final Object zzb;
    private int zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzas(zzau zzauVar, int i) {
        this.zza = zzauVar;
        this.zzb = zzau.zzg(zzauVar, i);
        this.zzc = i;
    }

    private final void zza() {
        int zzv;
        int i = this.zzc;
        if (i == -1 || i >= this.zza.size() || !zzo.zza(this.zzb, zzau.zzg(this.zza, this.zzc))) {
            zzv = this.zza.zzv(this.zzb);
            this.zzc = zzv;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzah, java.util.Map.Entry
    public final Object getKey() {
        return this.zzb;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzah, java.util.Map.Entry
    public final Object getValue() {
        Map zzl = this.zza.zzl();
        if (zzl != null) {
            return zzl.get(this.zzb);
        }
        zza();
        int i = this.zzc;
        if (i == -1) {
            return null;
        }
        return zzau.zzj(this.zza, i);
    }

    @Override // java.util.Map.Entry
    public final Object setValue(Object obj) {
        Map zzl = this.zza.zzl();
        if (zzl != null) {
            return zzl.put(this.zzb, obj);
        }
        zza();
        int i = this.zzc;
        if (i == -1) {
            this.zza.put(this.zzb, obj);
            return null;
        }
        Object zzj = zzau.zzj(this.zza, i);
        zzau.zzm(this.zza, this.zzc, obj);
        return zzj;
    }
}
