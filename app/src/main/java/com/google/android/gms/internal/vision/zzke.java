package com.google.android.gms.internal.vision;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
/* compiled from: com.google.android.gms:play-services-vision-common@@19.1.3 */
/* loaded from: classes3.dex */
public final class zzke<K, V> extends LinkedHashMap<K, V> {
    private static final zzke zzb;
    private boolean zza = true;

    private zzke() {
    }

    private zzke(Map<K, V> map) {
        super(map);
    }

    public static <K, V> zzke<K, V> zza() {
        return zzb;
    }

    public final void zza(zzke<K, V> zzkeVar) {
        zze();
        if (!zzkeVar.isEmpty()) {
            putAll(zzkeVar);
        }
    }

    @Override // java.util.LinkedHashMap, java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final Set<Map.Entry<K, V>> entrySet() {
        return isEmpty() ? Collections.emptySet() : super.entrySet();
    }

    @Override // java.util.LinkedHashMap, java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final void clear() {
        zze();
        super.clear();
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final V put(K k, V v) {
        zze();
        zzjf.zza(k);
        zzjf.zza(v);
        return (V) super.put(k, v);
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final void putAll(Map<? extends K, ? extends V> map) {
        zze();
        for (K k : map.keySet()) {
            zzjf.zza(k);
            zzjf.zza(map.get(k));
        }
        super.putAll(map);
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final V remove(Object obj) {
        zze();
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
            return zzjf.zzc((byte[]) obj);
        }
        if (obj instanceof zzje) {
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

    public final zzke<K, V> zzb() {
        return isEmpty() ? new zzke<>() : new zzke<>(this);
    }

    public final void zzc() {
        this.zza = false;
    }

    public final boolean zzd() {
        return this.zza;
    }

    private final void zze() {
        if (!this.zza) {
            throw new UnsupportedOperationException();
        }
    }

    static {
        zzke zzkeVar = new zzke();
        zzb = zzkeVar;
        zzkeVar.zza = false;
    }
}
