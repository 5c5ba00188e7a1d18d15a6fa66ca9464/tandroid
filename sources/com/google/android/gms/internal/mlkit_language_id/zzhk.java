package com.google.android.gms.internal.mlkit_language_id;

import java.util.Iterator;

/* loaded from: classes.dex */
final class zzhk implements Iterator {
    private Iterator zza;
    private final /* synthetic */ zzhi zzb;

    zzhk(zzhi zzhiVar) {
        zzfg zzfgVar;
        this.zzb = zzhiVar;
        zzfgVar = zzhiVar.zza;
        this.zza = zzfgVar.iterator();
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.zza.hasNext();
    }

    @Override // java.util.Iterator
    public final /* synthetic */ Object next() {
        return (String) this.zza.next();
    }

    @Override // java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
