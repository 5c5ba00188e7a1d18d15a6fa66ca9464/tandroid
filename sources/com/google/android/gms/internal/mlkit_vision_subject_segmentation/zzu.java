package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import java.util.List;
import java.util.ListIterator;

/* loaded from: classes.dex */
final class zzu extends zzs implements ListIterator {
    final /* synthetic */ zzv zzd;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzu(zzv zzvVar) {
        super(zzvVar);
        this.zzd = zzvVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzu(zzv zzvVar, int i) {
        super(zzvVar, ((List) zzvVar.zzb).listIterator(i));
        this.zzd = zzvVar;
    }

    @Override // java.util.ListIterator
    public final void add(Object obj) {
        int i;
        boolean isEmpty = this.zzd.isEmpty();
        zza();
        ((ListIterator) this.zza).add(obj);
        zzw zzwVar = this.zzd.zzf;
        i = zzwVar.zzb;
        zzwVar.zzb = i + 1;
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
