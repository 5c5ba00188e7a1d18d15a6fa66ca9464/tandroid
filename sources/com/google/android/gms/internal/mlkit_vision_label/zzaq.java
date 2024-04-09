package com.google.android.gms.internal.mlkit_vision_label;

import j$.util.Iterator;
import j$.util.function.Consumer;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
abstract class zzaq implements Iterator, j$.util.Iterator {
    int zzb;
    int zzc;
    int zzd;
    final /* synthetic */ zzau zze;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzaq(zzau zzauVar, zzam zzamVar) {
        int i;
        this.zze = zzauVar;
        i = zzauVar.zzf;
        this.zzb = i;
        this.zzc = zzauVar.zze();
        this.zzd = -1;
    }

    private final void zzb() {
        int i;
        i = this.zze.zzf;
        if (i != this.zzb) {
            throw new ConcurrentModificationException();
        }
    }

    @Override // j$.util.Iterator
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        Iterator.-CC.$default$forEachRemaining(this, consumer);
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public final boolean hasNext() {
        return this.zzc >= 0;
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public final Object next() {
        zzb();
        if (hasNext()) {
            int i = this.zzc;
            this.zzd = i;
            Object zza = zza(i);
            this.zzc = this.zze.zzf(this.zzc);
            return zza;
        }
        throw new NoSuchElementException();
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public final void remove() {
        zzb();
        zzs.zzd(this.zzd >= 0, "no calls to next() since the last call to remove()");
        this.zzb += 32;
        zzau zzauVar = this.zze;
        zzauVar.remove(zzau.zzg(zzauVar, this.zzd));
        this.zzc--;
        this.zzd = -1;
    }

    abstract Object zza(int i);
}
