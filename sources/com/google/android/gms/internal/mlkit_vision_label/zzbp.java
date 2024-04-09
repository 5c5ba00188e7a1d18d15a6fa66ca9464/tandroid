package com.google.android.gms.internal.mlkit_vision_label;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Set;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
abstract class zzbp extends AbstractMap {
    private transient Set zza;
    private transient Collection zzc;

    @Override // java.util.AbstractMap, java.util.Map
    public final Set entrySet() {
        Set set = this.zza;
        if (set == null) {
            Set zza = zza();
            this.zza = zza;
            return zza;
        }
        return set;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final Collection values() {
        Collection collection = this.zzc;
        if (collection == null) {
            zzbo zzboVar = new zzbo(this);
            this.zzc = zzboVar;
            return zzboVar;
        }
        return collection;
    }

    abstract Set zza();
}
