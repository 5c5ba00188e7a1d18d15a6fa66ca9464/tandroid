package com.google.android.gms.internal.mlkit_language_id;

import java.util.Iterator;
/* compiled from: com.google.mlkit:language-id@@16.1.1 */
/* loaded from: classes.dex */
final class zzhk implements Iterator<String> {
    private Iterator<String> zza;
    private final /* synthetic */ zzhi zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhk(zzhi zzhiVar) {
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
    public final void remove() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Iterator
    public final /* synthetic */ String next() {
        return this.zza.next();
    }
}
