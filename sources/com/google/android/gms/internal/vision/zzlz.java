package com.google.android.gms.internal.vision;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
/* loaded from: classes.dex */
public final class zzlz extends AbstractList implements zzjv, RandomAccess {
    private final zzjv zza;

    public zzlz(zzjv zzjvVar) {
        this.zza = zzjvVar;
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ Object get(int i) {
        return (String) this.zza.get(i);
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public final Iterator iterator() {
        return new zzmb(this);
    }

    @Override // java.util.AbstractList, java.util.List
    public final ListIterator listIterator(int i) {
        return new zzly(this, i);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.zza.size();
    }

    @Override // com.google.android.gms.internal.vision.zzjv
    public final void zza(zzht zzhtVar) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.android.gms.internal.vision.zzjv
    public final Object zzb(int i) {
        return this.zza.zzb(i);
    }

    @Override // com.google.android.gms.internal.vision.zzjv
    public final List zzd() {
        return this.zza.zzd();
    }

    @Override // com.google.android.gms.internal.vision.zzjv
    public final zzjv zze() {
        return this;
    }
}
