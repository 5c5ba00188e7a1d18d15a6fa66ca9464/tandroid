package com.google.android.gms.internal.mlkit_vision_label;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import org.telegram.tgnet.ConnectionsManager;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class zzbm extends zzca {
    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final void clear() {
        zza().clear();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public abstract boolean contains(Object obj);

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean isEmpty() {
        return zza().isEmpty();
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzca, java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean removeAll(Collection collection) {
        collection.getClass();
        try {
            return zzcb.zzb(this, collection);
        } catch (UnsupportedOperationException unused) {
            return zzcb.zzc(this, collection.iterator());
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzca, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean retainAll(Collection collection) {
        int i;
        collection.getClass();
        try {
            return super.retainAll(collection);
        } catch (UnsupportedOperationException unused) {
            int size = collection.size();
            if (size < 3) {
                zzal.zza(size, "expectedSize");
                i = size + 1;
            } else if (size < 1073741824) {
                double d = size;
                Double.isNaN(d);
                i = (int) Math.ceil(d / 0.75d);
            } else {
                i = ConnectionsManager.DEFAULT_DATACENTER_ID;
            }
            HashSet hashSet = new HashSet(i);
            for (Object obj : collection) {
                if (contains(obj) && (obj instanceof Map.Entry)) {
                    hashSet.add(((Map.Entry) obj).getKey());
                }
            }
            return ((zzy) zza()).zzb.zzq().retainAll(hashSet);
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final int size() {
        return zza().size();
    }

    abstract Map zza();
}
