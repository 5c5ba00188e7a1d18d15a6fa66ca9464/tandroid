package com.google.android.gms.internal.mlkit_language_id;

import java.util.ListIterator;

/* loaded from: classes.dex */
final class zzhl implements ListIterator {
    private ListIterator zza;
    private final /* synthetic */ int zzb;
    private final /* synthetic */ zzhi zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhl(zzhi zzhiVar, int i) {
        zzfg zzfgVar;
        this.zzc = zzhiVar;
        this.zzb = i;
        zzfgVar = zzhiVar.zza;
        this.zza = zzfgVar.listIterator(i);
    }

    @Override // java.util.ListIterator
    public final /* synthetic */ void add(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.ListIterator, java.util.Iterator
    public final boolean hasNext() {
        return this.zza.hasNext();
    }

    @Override // java.util.ListIterator
    public final boolean hasPrevious() {
        return this.zza.hasPrevious();
    }

    @Override // java.util.ListIterator, java.util.Iterator
    public final /* synthetic */ Object next() {
        return (String) this.zza.next();
    }

    @Override // java.util.ListIterator
    public final int nextIndex() {
        return this.zza.nextIndex();
    }

    @Override // java.util.ListIterator
    public final /* synthetic */ Object previous() {
        return (String) this.zza.previous();
    }

    @Override // java.util.ListIterator
    public final int previousIndex() {
        return this.zza.previousIndex();
    }

    @Override // java.util.ListIterator, java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.ListIterator
    public final /* synthetic */ void set(Object obj) {
        throw new UnsupportedOperationException();
    }
}
