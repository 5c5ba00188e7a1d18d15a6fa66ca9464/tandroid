package com.google.android.gms.internal.mlkit_common;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
/* compiled from: com.google.mlkit:common@@17.0.0 */
/* loaded from: classes3.dex */
public final class zzgv<K, V> extends LinkedHashMap<K, V> {
    private static final zzgv zzb;
    private boolean zza = true;

    private zzgv() {
    }

    private zzgv(Map<K, V> map) {
        super(map);
    }

    public final void zza(zzgv<K, V> zzgvVar) {
        zzd();
        if (!zzgvVar.isEmpty()) {
            putAll(zzgvVar);
        }
    }

    @Override // java.util.LinkedHashMap, java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final Set<Map.Entry<K, V>> entrySet() {
        return isEmpty() ? Collections.emptySet() : super.entrySet();
    }

    @Override // java.util.LinkedHashMap, java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final void clear() {
        zzd();
        super.clear();
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final V put(K k, V v) {
        zzd();
        zzfs.zza(k);
        zzfs.zza(v);
        return (V) super.put(k, v);
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final void putAll(Map<? extends K, ? extends V> map) {
        zzd();
        for (K k : map.keySet()) {
            zzfs.zza(k);
            zzfs.zza(map.get(k));
        }
        super.putAll(map);
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final V remove(Object obj) {
        zzd();
        return (V) super.remove(obj);
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x005f A[RETURN] */
    @Override // java.util.AbstractMap, java.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final boolean equals(Object obj) {
        boolean z;
        boolean z2;
        if (obj instanceof Map) {
            Map map = (Map) obj;
            if (this != map) {
                if (size() != map.size()) {
                    z = false;
                } else {
                    for (Map.Entry<K, V> entry : entrySet()) {
                        if (!map.containsKey(entry.getKey())) {
                            z = false;
                            break;
                        }
                        V value = entry.getValue();
                        Object obj2 = map.get(entry.getKey());
                        if ((value instanceof byte[]) && (obj2 instanceof byte[])) {
                            z2 = Arrays.equals((byte[]) value, (byte[]) obj2);
                            continue;
                        } else {
                            z2 = value.equals(obj2);
                            continue;
                        }
                        if (!z2) {
                            z = false;
                            break;
                        }
                    }
                }
                if (!z) {
                    return true;
                }
            }
            z = true;
            if (!z) {
            }
        }
        return false;
    }

    private static int zza(Object obj) {
        if (obj instanceof byte[]) {
            return zzfs.zzc((byte[]) obj);
        }
        if (obj instanceof zzfv) {
            throw new UnsupportedOperationException();
        }
        return obj.hashCode();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final int hashCode() {
        int i = 0;
        for (Map.Entry<K, V> entry : entrySet()) {
            i += zza(entry.getValue()) ^ zza(entry.getKey());
        }
        return i;
    }

    public final zzgv<K, V> zza() {
        return isEmpty() ? new zzgv<>() : new zzgv<>(this);
    }

    public final void zzb() {
        this.zza = false;
    }

    public final boolean zzc() {
        return this.zza;
    }

    private final void zzd() {
        if (!this.zza) {
            throw new UnsupportedOperationException();
        }
    }

    static {
        zzgv zzgvVar = new zzgv();
        zzb = zzgvVar;
        zzgvVar.zza = false;
    }
}
