package com.google.android.gms.internal.play_billing;

import j$.util.Iterator;
import j$.util.function.Consumer;
import java.util.Iterator;
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
final class zzek implements Iterator, j$.util.Iterator {
    final Iterator zza;
    final /* synthetic */ zzel zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzek(zzel zzelVar) {
        zzcn zzcnVar;
        this.zzb = zzelVar;
        zzcnVar = zzelVar.zza;
        this.zza = zzcnVar.iterator();
    }

    @Override // j$.util.Iterator
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        Iterator.-CC.$default$forEachRemaining(this, consumer);
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public final boolean hasNext() {
        return this.zza.hasNext();
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public final /* bridge */ /* synthetic */ Object next() {
        return (String) this.zza.next();
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
