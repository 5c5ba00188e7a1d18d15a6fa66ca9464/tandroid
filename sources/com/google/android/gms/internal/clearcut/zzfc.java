package com.google.android.gms.internal.clearcut;

import java.util.Iterator;
/* loaded from: classes.dex */
final class zzfc implements Iterator<String> {
    private final /* synthetic */ zzfa zzpe;
    private Iterator<String> zzpf;

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
    public final /* synthetic */ String next() {
        return this.zzpf.next();
    }

    @Override // java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
