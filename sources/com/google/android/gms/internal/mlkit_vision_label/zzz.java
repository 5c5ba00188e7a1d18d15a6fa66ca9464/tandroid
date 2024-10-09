package com.google.android.gms.internal.mlkit_vision_label;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes.dex */
final class zzz implements Iterator {
    Map.Entry zza;
    final /* synthetic */ Iterator zzb;
    final /* synthetic */ zzaa zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzz(zzaa zzaaVar, Iterator it) {
        this.zzc = zzaaVar;
        this.zzb = it;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.zzb.hasNext();
    }

    @Override // java.util.Iterator
    public final Object next() {
        Map.Entry entry = (Map.Entry) this.zzb.next();
        this.zza = entry;
        return entry.getKey();
    }

    @Override // java.util.Iterator
    public final void remove() {
        zzs.zzd(this.zza != null, "no calls to next() since the last call to remove()");
        Collection collection = (Collection) this.zza.getValue();
        this.zzb.remove();
        zzag.zzg(this.zzc.zza, collection.size());
        collection.clear();
        this.zza = null;
    }
}
