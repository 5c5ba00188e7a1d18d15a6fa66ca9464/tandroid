package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Set;

/* loaded from: classes.dex */
abstract class zzbg extends AbstractMap {
    private transient Set zza;
    private transient Collection zzc;

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
        zzbf zzbfVar = new zzbf(this);
        this.zzc = zzbfVar;
        return zzbfVar;
    }

    abstract Set zza();
}
