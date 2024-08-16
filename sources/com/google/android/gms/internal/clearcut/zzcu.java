package com.google.android.gms.internal.clearcut;

import java.util.Iterator;
import java.util.Map;
/* loaded from: classes.dex */
final class zzcu<K> implements Iterator<Map.Entry<K, Object>> {
    private Iterator<Map.Entry<K, Object>> zzlm;

    public zzcu(Iterator<Map.Entry<K, Object>> it) {
        this.zzlm = it;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.zzlm.hasNext();
    }

    @Override // java.util.Iterator
    public final /* synthetic */ Object next() {
        Map.Entry<K, Object> next = this.zzlm.next();
        return next.getValue() instanceof zzcr ? new zzct(next) : next;
    }

    @Override // java.util.Iterator
    public final void remove() {
        this.zzlm.remove();
    }
}
