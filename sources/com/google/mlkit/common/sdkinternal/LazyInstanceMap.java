package com.google.mlkit.common.sdkinternal;

import java.util.HashMap;
import java.util.Map;
/* compiled from: com.google.mlkit:common@@18.10.0 */
/* loaded from: classes.dex */
public abstract class LazyInstanceMap<K, V> {
    private final Map zza = new HashMap();

    protected abstract V create(K k);

    public V get(K k) {
        synchronized (this.zza) {
            if (this.zza.containsKey(k)) {
                return (V) this.zza.get(k);
            }
            V create = create(k);
            this.zza.put(k, create);
            return create;
        }
    }
}
