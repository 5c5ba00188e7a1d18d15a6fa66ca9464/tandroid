package com.google.android.gms.internal.mlkit_vision_label;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
final class zzw extends zzbm {
    final /* synthetic */ zzy zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzw(zzy zzyVar) {
        this.zza = zzyVar;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzbm, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean contains(Object obj) {
        Set entrySet = this.zza.zza.entrySet();
        Objects.requireNonNull(entrySet);
        try {
            return entrySet.contains(obj);
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public final Iterator iterator() {
        return new zzx(this.zza);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean remove(Object obj) {
        if (contains(obj)) {
            Map.Entry entry = (Map.Entry) obj;
            entry.getClass();
            zzag.zzm(this.zza.zzb, entry.getKey());
            return true;
        }
        return false;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzbm
    final Map zza() {
        return this.zza;
    }
}
