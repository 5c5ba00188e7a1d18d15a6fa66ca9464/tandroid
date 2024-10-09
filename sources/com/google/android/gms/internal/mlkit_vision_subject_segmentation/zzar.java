package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import java.io.Serializable;

/* loaded from: classes.dex */
final class zzar extends zzx implements Serializable {
    final Object zza;
    final Object zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzar(Object obj, Object obj2) {
        this.zza = obj;
        this.zzb = obj2;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzx, java.util.Map.Entry
    public final Object getKey() {
        return this.zza;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzx, java.util.Map.Entry
    public final Object getValue() {
        return this.zzb;
    }

    @Override // java.util.Map.Entry
    public final Object setValue(Object obj) {
        throw new UnsupportedOperationException();
    }
}
