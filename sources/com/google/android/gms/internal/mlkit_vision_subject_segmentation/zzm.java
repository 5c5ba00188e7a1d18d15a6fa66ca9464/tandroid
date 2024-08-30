package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
/* loaded from: classes.dex */
final class zzm extends zzbd {
    final /* synthetic */ zzo zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzm(zzo zzoVar) {
        this.zza = zzoVar;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzbd, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean contains(Object obj) {
        Set entrySet = this.zza.zza.entrySet();
        entrySet.getClass();
        try {
            return entrySet.contains(obj);
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public final Iterator iterator() {
        return new zzn(this.zza);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean remove(Object obj) {
        if (contains(obj)) {
            Map.Entry entry = (Map.Entry) obj;
            entry.getClass();
            zzo zzoVar = this.zza;
            zzw.zzk(zzoVar.zzb, entry.getKey());
            return true;
        }
        return false;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzbd
    final Map zza() {
        return this.zza;
    }
}
