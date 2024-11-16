package com.google.android.gms.internal.mlkit_vision_label;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Set;

/* loaded from: classes.dex */
abstract class zzbp extends AbstractMap {
    private transient Set zza;
    private transient Collection zzc;

    zzbp() {
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final Set entrySet() {
        Set set = this.zza;
        if (set != null) {
            return set;
        }
        Set zza = zza();
        this.zza = zza;
        return zza;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final Collection values() {
        Collection collection = this.zzc;
        if (collection != null) {
            return collection;
        }
        zzbo zzboVar = new zzbo(this);
        this.zzc = zzboVar;
        return zzboVar;
    }

    abstract Set zza();
}
