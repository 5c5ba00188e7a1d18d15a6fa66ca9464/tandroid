package com.google.android.gms.internal.vision;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public final class zzke extends LinkedHashMap {
    private static final zzke zzb;
    private boolean zza;

    static {
        zzke zzkeVar = new zzke();
        zzb = zzkeVar;
        zzkeVar.zza = false;
    }

    private zzke() {
        this.zza = true;
    }

    private zzke(Map map) {
        super(map);
        this.zza = true;
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

    public static zzke zza() {
        return zzb;
    }

    private final void zze() {
        if (!this.zza) {
            throw new UnsupportedOperationException();
        }
    }

    @Override // java.util.LinkedHashMap, java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final void clear() {
        zze();
        super.clear();
    }

    @Override // java.util.LinkedHashMap, java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final Set entrySet() {
        return isEmpty() ? Collections.emptySet() : super.entrySet();
    }

    /* JADX WARN: Removed duplicated region for block: B:9:0x005c A[RETURN] */
    @Override // java.util.AbstractMap, java.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final boolean equals(Object obj) {
        boolean z;
        if (obj instanceof Map) {
            Map map = (Map) obj;
            if (this != map) {
                if (size() == map.size()) {
                    Iterator it = entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry entry = (Map.Entry) it.next();
                        if (map.containsKey(entry.getKey())) {
                            Object value = entry.getValue();
                            Object obj2 = map.get(entry.getKey());
                            if (!(((value instanceof byte[]) && (obj2 instanceof byte[])) ? Arrays.equals((byte[]) value, (byte[]) obj2) : value.equals(obj2))) {
                            }
                        }
                    }
                }
                z = false;
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

    @Override // java.util.AbstractMap, java.util.Map
    public final int hashCode() {
        Iterator it = entrySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            i += zza(entry.getValue()) ^ zza(entry.getKey());
        }
        return i;
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final Object put(Object obj, Object obj2) {
        zze();
        zzjf.zza(obj);
        zzjf.zza(obj2);
        return super.put(obj, obj2);
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final void putAll(Map map) {
        zze();
        for (Object obj : map.keySet()) {
            zzjf.zza(obj);
            zzjf.zza(map.get(obj));
        }
        super.putAll(map);
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final Object remove(Object obj) {
        zze();
        return super.remove(obj);
    }

    public final void zza(zzke zzkeVar) {
        zze();
        if (zzkeVar.isEmpty()) {
            return;
        }
        putAll(zzkeVar);
    }

    public final zzke zzb() {
        return isEmpty() ? new zzke() : new zzke(this);
    }

    public final void zzc() {
        this.zza = false;
    }

    public final boolean zzd() {
        return this.zza;
    }
}
