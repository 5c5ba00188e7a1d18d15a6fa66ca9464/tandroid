package com.google.android.gms.internal.clearcut;

import java.util.Iterator;

/* loaded from: classes.dex */
final class zzfc implements Iterator {
    private final /* synthetic */ zzfa zzpe;
    private Iterator zzpf;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzfc(zzfa zzfaVar) {
        zzcx zzcxVar;
        this.zzpe = zzfaVar;
        zzcxVar = zzfaVar.zzpb;
        this.zzpf = zzcxVar.iterator();
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.zzpf.hasNext();
    }

    @Override // java.util.Iterator
    public final /* synthetic */ Object next() {
        return (String) this.zzpf.next();
    }

    @Override // java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
