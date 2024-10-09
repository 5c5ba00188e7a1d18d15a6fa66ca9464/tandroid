package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import java.util.AbstractSet;
import java.util.Collection;

/* loaded from: classes.dex */
abstract class zzbr extends AbstractSet {
    @Override // java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean removeAll(Collection collection) {
        return zzbs.zzb(this, collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean retainAll(Collection collection) {
        collection.getClass();
        return super.retainAll(collection);
    }
}
