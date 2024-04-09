package com.google.android.gms.internal.mlkit_vision_label;

import j$.util.Iterator;
import j$.util.function.Consumer;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
final class zzx implements Iterator, j$.util.Iterator {
    final Iterator zza;
    Collection zzb;
    final /* synthetic */ zzy zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzx(zzy zzyVar) {
        this.zzc = zzyVar;
        this.zza = zzyVar.zza.entrySet().iterator();
    }

    @Override // j$.util.Iterator
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        Iterator.-CC.$default$forEachRemaining(this, consumer);
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public final boolean hasNext() {
        return this.zza.hasNext();
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public final /* bridge */ /* synthetic */ Object next() {
        Map.Entry entry = (Map.Entry) this.zza.next();
        this.zzb = (Collection) entry.getValue();
        zzy zzyVar = this.zzc;
        Object key = entry.getKey();
        return new zzba(key, zzyVar.zzb.zzb(key, (Collection) entry.getValue()));
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public final void remove() {
        zzs.zzd(this.zzb != null, "no calls to next() since the last call to remove()");
        this.zza.remove();
        zzag.zzg(this.zzc.zzb, this.zzb.size());
        this.zzb.clear();
        this.zzb = null;
    }
}
