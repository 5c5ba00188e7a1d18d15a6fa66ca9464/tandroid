package com.google.android.gms.internal.vision;

import j$.util.Iterator;
import j$.util.function.Consumer;
/* compiled from: com.google.android.gms:play-services-vision-common@@19.1.3 */
/* loaded from: classes.dex */
abstract class zzhu implements zzhy, Iterator {
    @Override // j$.util.Iterator
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        Iterator.CC.$default$forEachRemaining(this, consumer);
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Iterator, j$.util.Iterator
    /* renamed from: next */
    public /* synthetic */ Object mo335next() {
        return Byte.valueOf(zza());
    }
}
