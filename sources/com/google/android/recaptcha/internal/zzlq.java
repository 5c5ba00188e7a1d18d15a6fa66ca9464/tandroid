package com.google.android.recaptcha.internal;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

@Deprecated
/* loaded from: classes.dex */
public final class zzlq extends AbstractList implements RandomAccess, zzjm {
    private final zzjm zza;

    public zzlq(zzjm zzjmVar) {
        this.zza = zzjmVar;
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* bridge */ /* synthetic */ Object get(int i) {
        return ((zzjl) this.zza).get(i);
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public final Iterator iterator() {
        return new zzlp(this);
    }

    @Override // java.util.AbstractList, java.util.List
    public final ListIterator listIterator(int i) {
        return new zzlo(this, i);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.zza.size();
    }

    @Override // com.google.android.recaptcha.internal.zzjm
    public final zzjm zze() {
        return this;
    }

    @Override // com.google.android.recaptcha.internal.zzjm
    public final Object zzf(int i) {
        return this.zza.zzf(i);
    }

    @Override // com.google.android.recaptcha.internal.zzjm
    public final List zzh() {
        return this.zza.zzh();
    }

    @Override // com.google.android.recaptcha.internal.zzjm
    public final void zzi(zzgw zzgwVar) {
        throw new UnsupportedOperationException();
    }
}
