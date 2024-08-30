package com.google.android.gms.internal.mlkit_language_id;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class zzgq extends AbstractMap {
    private final int zza;
    private List zzb;
    private Map zzc;
    private boolean zzd;
    private volatile zzhb zze;
    private Map zzf;
    private volatile zzgv zzg;

    private zzgq(int i) {
        this.zza = i;
        this.zzb = Collections.emptyList();
        this.zzc = Collections.emptyMap();
        this.zzf = Collections.emptyMap();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzgq(int i, zzgt zzgtVar) {
        this(i);
    }

    private final int zza(Comparable comparable) {
        int i;
        int size = this.zzb.size();
        int i2 = size - 1;
        if (i2 >= 0) {
            int compareTo = comparable.compareTo((Comparable) ((zzgz) this.zzb.get(i2)).getKey());
            if (compareTo > 0) {
                i = size + 1;
                return -i;
            } else if (compareTo == 0) {
                return i2;
            }
        }
        int i3 = 0;
        while (i3 <= i2) {
            int i4 = (i3 + i2) / 2;
            int compareTo2 = comparable.compareTo((Comparable) ((zzgz) this.zzb.get(i4)).getKey());
            if (compareTo2 < 0) {
                i2 = i4 - 1;
            } else if (compareTo2 <= 0) {
                return i4;
            } else {
                i3 = i4 + 1;
            }
        }
        i = i3 + 1;
        return -i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzgq zza(int i) {
        return new zzgt(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Object zzc(int i) {
        zzf();
        Object value = ((zzgz) this.zzb.remove(i)).getValue();
        if (!this.zzc.isEmpty()) {
            Iterator it = zzg().entrySet().iterator();
            this.zzb.add(new zzgz(this, (Map.Entry) it.next()));
            it.remove();
        }
        return value;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zzf() {
        if (this.zzd) {
            throw new UnsupportedOperationException();
        }
    }

    private final SortedMap zzg() {
        zzf();
        if (this.zzc.isEmpty() && !(this.zzc instanceof TreeMap)) {
            TreeMap treeMap = new TreeMap();
            this.zzc = treeMap;
            this.zzf = treeMap.descendingMap();
        }
        return (SortedMap) this.zzc;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        zzf();
        if (!this.zzb.isEmpty()) {
            this.zzb.clear();
        }
        if (this.zzc.isEmpty()) {
            return;
        }
        this.zzc.clear();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object obj) {
        Comparable comparable = (Comparable) obj;
        return zza(comparable) >= 0 || this.zzc.containsKey(comparable);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set entrySet() {
        if (this.zze == null) {
            this.zze = new zzhb(this, null);
        }
        return this.zze;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof zzgq) {
            zzgq zzgqVar = (zzgq) obj;
            int size = size();
            if (size != zzgqVar.size()) {
                return false;
            }
            int zzc = zzc();
            if (zzc != zzgqVar.zzc()) {
                return entrySet().equals(zzgqVar.entrySet());
            }
            for (int i = 0; i < zzc; i++) {
                if (!zzb(i).equals(zzgqVar.zzb(i))) {
                    return false;
                }
            }
            if (zzc != size) {
                return this.zzc.equals(zzgqVar.zzc);
            }
            return true;
        }
        return super.equals(obj);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object get(Object obj) {
        Comparable comparable = (Comparable) obj;
        int zza = zza(comparable);
        return zza >= 0 ? ((zzgz) this.zzb.get(zza)).getValue() : this.zzc.get(comparable);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int hashCode() {
        int zzc = zzc();
        int i = 0;
        for (int i2 = 0; i2 < zzc; i2++) {
            i += ((zzgz) this.zzb.get(i2)).hashCode();
        }
        return this.zzc.size() > 0 ? i + this.zzc.hashCode() : i;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object remove(Object obj) {
        zzf();
        Comparable comparable = (Comparable) obj;
        int zza = zza(comparable);
        if (zza >= 0) {
            return zzc(zza);
        }
        if (this.zzc.isEmpty()) {
            return null;
        }
        return this.zzc.remove(comparable);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return this.zzb.size() + this.zzc.size();
    }

    @Override // java.util.AbstractMap, java.util.Map
    /* renamed from: zza */
    public final Object put(Comparable comparable, Object obj) {
        zzf();
        int zza = zza(comparable);
        if (zza >= 0) {
            return ((zzgz) this.zzb.get(zza)).setValue(obj);
        }
        zzf();
        if (this.zzb.isEmpty() && !(this.zzb instanceof ArrayList)) {
            this.zzb = new ArrayList(this.zza);
        }
        int i = -(zza + 1);
        if (i >= this.zza) {
            return zzg().put(comparable, obj);
        }
        int size = this.zzb.size();
        int i2 = this.zza;
        if (size == i2) {
            zzgz zzgzVar = (zzgz) this.zzb.remove(i2 - 1);
            zzg().put((Comparable) zzgzVar.getKey(), zzgzVar.getValue());
        }
        this.zzb.add(i, new zzgz(this, comparable, obj));
        return null;
    }

    public void zza() {
        if (this.zzd) {
            return;
        }
        this.zzc = this.zzc.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(this.zzc);
        this.zzf = this.zzf.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(this.zzf);
        this.zzd = true;
    }

    public final Map.Entry zzb(int i) {
        return (Map.Entry) this.zzb.get(i);
    }

    public final boolean zzb() {
        return this.zzd;
    }

    public final int zzc() {
        return this.zzb.size();
    }

    public final Iterable zzd() {
        return this.zzc.isEmpty() ? zzgu.zza() : this.zzc.entrySet();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Set zze() {
        if (this.zzg == null) {
            this.zzg = new zzgv(this, null);
        }
        return this.zzg;
    }
}
