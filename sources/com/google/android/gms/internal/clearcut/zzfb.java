package com.google.android.gms.internal.clearcut;

import java.util.ListIterator;
/* loaded from: classes.dex */
final class zzfb implements ListIterator<String> {
    private ListIterator<String> zzpc;
    private final /* synthetic */ int zzpd;
    private final /* synthetic */ zzfa zzpe;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzfb(zzfa zzfaVar, int i) {
        zzcx zzcxVar;
        this.zzpe = zzfaVar;
        this.zzpd = i;
        zzcxVar = zzfaVar.zzpb;
        this.zzpc = zzcxVar.listIterator(i);
    }

    @Override // java.util.ListIterator
    public final /* synthetic */ void add(String str) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.ListIterator, java.util.Iterator
    public final boolean hasNext() {
        return this.zzpc.hasNext();
    }

    @Override // java.util.ListIterator
    public final boolean hasPrevious() {
        return this.zzpc.hasPrevious();
    }

    @Override // java.util.ListIterator, java.util.Iterator
    public final /* synthetic */ Object next() {
        return this.zzpc.next();
    }

    @Override // java.util.ListIterator
    public final int nextIndex() {
        return this.zzpc.nextIndex();
    }

    @Override // java.util.ListIterator
    public final /* synthetic */ String previous() {
        return this.zzpc.previous();
    }

    @Override // java.util.ListIterator
    public final int previousIndex() {
        return this.zzpc.previousIndex();
    }

    @Override // java.util.ListIterator, java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.ListIterator
    public final /* synthetic */ void set(String str) {
        throw new UnsupportedOperationException();
    }
}
