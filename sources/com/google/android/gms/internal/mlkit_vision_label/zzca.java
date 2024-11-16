package com.google.android.gms.internal.mlkit_vision_label;

import java.util.AbstractSet;
import java.util.Collection;

/* loaded from: classes.dex */
abstract class zzca extends AbstractSet {
    zzca() {
    }

    @Override // java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean removeAll(Collection collection) {
        return zzcb.zzb(this, collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean retainAll(Collection collection) {
        collection.getClass();
        return super.retainAll(collection);
    }
}
