package com.google.android.gms.internal.mlkit_vision_label;

import java.util.Iterator;
import java.util.Map;

/* loaded from: classes.dex */
abstract class zzbn extends zzca {
    final Map zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzbn(Map map) {
        map.getClass();
        this.zzb = map;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean contains(Object obj) {
        return this.zzb.containsKey(obj);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean isEmpty() {
        return this.zzb.isEmpty();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public abstract Iterator iterator();

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final int size() {
        return this.zzb.size();
    }
}
