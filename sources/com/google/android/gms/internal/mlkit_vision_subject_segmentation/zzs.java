package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
/* compiled from: com.google.android.gms:play-services-mlkit-subject-segmentation@@16.0.0-beta1 */
/* loaded from: classes.dex */
class zzs implements Iterator {
    final Iterator zza;
    final Collection zzb;
    final /* synthetic */ zzt zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzs(zzt zztVar) {
        Iterator it;
        this.zzc = zztVar;
        Collection collection = zztVar.zzb;
        this.zzb = collection;
        if (collection instanceof List) {
            it = ((List) collection).listIterator();
        } else {
            it = collection.iterator();
        }
        this.zza = it;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzs(zzt zztVar, Iterator it) {
        this.zzc = zztVar;
        this.zzb = zztVar.zzb;
        this.zza = it;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        zza();
        return this.zza.hasNext();
    }

    @Override // java.util.Iterator
    public final Object next() {
        zza();
        return this.zza.next();
    }

    @Override // java.util.Iterator
    public final void remove() {
        int i;
        this.zza.remove();
        zzw zzwVar = this.zzc.zze;
        i = zzwVar.zzb;
        zzwVar.zzb = i - 1;
        this.zzc.zzc();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zza() {
        this.zzc.zzb();
        if (this.zzc.zzb != this.zzb) {
            throw new ConcurrentModificationException();
        }
    }
}
