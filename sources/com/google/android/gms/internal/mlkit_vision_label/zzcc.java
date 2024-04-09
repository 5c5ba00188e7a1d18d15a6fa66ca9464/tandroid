package com.google.android.gms.internal.mlkit_vision_label;

import j$.util.Iterator;
import j$.util.function.Consumer;
import java.util.Iterator;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
public abstract class zzcc implements Iterator, j$.util.Iterator {
    final Iterator zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzcc(Iterator it) {
        Objects.requireNonNull(it);
        this.zza = it;
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
    public final Object next() {
        return zza(this.zza.next());
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public final void remove() {
        this.zza.remove();
    }

    abstract Object zza(Object obj);
}
