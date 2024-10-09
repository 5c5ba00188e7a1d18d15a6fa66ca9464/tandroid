package com.google.android.gms.internal.mlkit_vision_label;

import java.util.List;
import java.util.ListIterator;

/* loaded from: classes.dex */
final class zzae extends zzac implements ListIterator {
    final /* synthetic */ zzaf zzd;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzae(zzaf zzafVar) {
        super(zzafVar);
        this.zzd = zzafVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzae(zzaf zzafVar, int i) {
        super(zzafVar, ((List) zzafVar.zzb).listIterator(i));
        this.zzd = zzafVar;
    }

    @Override // java.util.ListIterator
    public final void add(Object obj) {
        boolean isEmpty = this.zzd.isEmpty();
        zza();
        ((ListIterator) this.zza).add(obj);
        zzag.zzd(this.zzd.zzf);
        if (isEmpty) {
            this.zzd.zza();
        }
    }

    @Override // java.util.ListIterator
    public final boolean hasPrevious() {
        zza();
        return ((ListIterator) this.zza).hasPrevious();
    }

    @Override // java.util.ListIterator
    public final int nextIndex() {
        zza();
        return ((ListIterator) this.zza).nextIndex();
    }

    @Override // java.util.ListIterator
    public final Object previous() {
        zza();
        return ((ListIterator) this.zza).previous();
    }

    @Override // java.util.ListIterator
    public final int previousIndex() {
        zza();
        return ((ListIterator) this.zza).previousIndex();
    }

    @Override // java.util.ListIterator
    public final void set(Object obj) {
        zza();
        ((ListIterator) this.zza).set(obj);
    }
}
