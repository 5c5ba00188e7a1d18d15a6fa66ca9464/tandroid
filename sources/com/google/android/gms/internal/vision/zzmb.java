package com.google.android.gms.internal.vision;

import java.util.Iterator;
/* compiled from: com.google.android.gms:play-services-vision-common@@19.1.3 */
/* loaded from: classes.dex */
final class zzmb implements Iterator<String> {
    private Iterator<String> zza;
    private final /* synthetic */ zzlz zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzmb(zzlz zzlzVar) {
        zzjv zzjvVar;
        this.zzb = zzlzVar;
        zzjvVar = zzlzVar.zza;
        this.zza = zzjvVar.iterator();
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
