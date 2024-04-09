package com.google.android.gms.internal.mlkit_vision_label;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Objects;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
abstract class zzca extends AbstractSet {
    @Override // java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean removeAll(Collection collection) {
        return zzcb.zzb(this, collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean retainAll(Collection collection) {
        Objects.requireNonNull(collection);
        return super.retainAll(collection);
    }
}
