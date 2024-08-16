package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
/* compiled from: com.google.android.gms:play-services-mlkit-subject-segmentation@@16.0.0-beta1 */
/* loaded from: classes.dex */
final class zzn implements Iterator {
    final Iterator zza;
    Collection zzb;
    final /* synthetic */ zzo zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzn(zzo zzoVar) {
        this.zzc = zzoVar;
        this.zza = zzoVar.zza.entrySet().iterator();
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.zza.hasNext();
    }

    @Override // java.util.Iterator
    public final /* bridge */ /* synthetic */ Object next() {
        Map.Entry entry = (Map.Entry) this.zza.next();
        this.zzb = (Collection) entry.getValue();
        Object key = entry.getKey();
        return new zzar(key, this.zzc.zzb.zzb(key, (Collection) entry.getValue()));
    }

    @Override // java.util.Iterator
    public final void remove() {
        int i;
        zzi.zzd(this.zzb != null, "no calls to next() since the last call to remove()");
        this.zza.remove();
        zzw zzwVar = this.zzc.zzb;
        i = zzwVar.zzb;
        zzwVar.zzb = i - this.zzb.size();
        this.zzb.clear();
        this.zzb = null;
    }
}
