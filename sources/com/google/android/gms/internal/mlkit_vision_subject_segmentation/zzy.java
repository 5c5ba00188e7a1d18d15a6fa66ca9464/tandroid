package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
abstract class zzy implements zzbi {
    private transient Set zza;
    private transient Map zzb;

    zzy() {
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof zzbi) {
            return zzn().equals(((zzbi) obj).zzn());
        }
        return false;
    }

    public final int hashCode() {
        return zzn().hashCode();
    }

    public final String toString() {
        return zzn().toString();
    }

    abstract Map zzh();

    abstract Set zzi();

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzbi
    public final Map zzn() {
        Map map = this.zzb;
        if (map != null) {
            return map;
        }
        Map zzh = zzh();
        this.zzb = zzh;
        return zzh;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzbi
    public final Set zzo() {
        Set set = this.zza;
        if (set != null) {
            return set;
        }
        Set zzi = zzi();
        this.zza = zzi;
        return zzi;
    }
}
