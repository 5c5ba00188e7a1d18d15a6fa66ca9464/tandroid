package com.google.android.gms.internal.vision;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/* loaded from: classes.dex */
abstract class zzlh extends AbstractMap {
    private final int zza;
    private List zzb;
    private Map zzc;
    private boolean zzd;
    private volatile zzlo zze;
    private Map zzf;
    private volatile zzli zzg;

    private zzlh(int i) {
        this.zza = i;
        this.zzb = Collections.emptyList();
        this.zzc = Collections.emptyMap();
        this.zzf = Collections.emptyMap();
    }

    /* synthetic */ zzlh(int i, zzlg zzlgVar) {
        this(i);
    }

    private final int zza(Comparable comparable) {
        int i;
        int size = this.zzb.size();
        int i2 = size - 1;
        if (i2 >= 0) {
            int compareTo = comparable.compareTo((Comparable) ((zzlm) this.zzb.get(i2)).getKey());
            if (compareTo > 0) {
                i = size + 1;
                return -i;
            }
            if (compareTo == 0) {
                return i2;
            }
        }
        int i3 = 0;
        while (i3 <= i2) {
            int i4 = (i3 + i2) / 2;
            int compareTo2 = comparable.compareTo((Comparable) ((zzlm) this.zzb.get(i4)).getKey());
            if (compareTo2 < 0) {
                i2 = i4 - 1;
            } else {
                if (compareTo2 <= 0) {
                    return i4;
                }
                i3 = i4 + 1;
            }
        }
        i = i3 + 1;
        return -i;
    }

    static zzlh zza(int i) {
        return new zzlg(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Object zzc(int i) {
        zzf();
        Object value = ((zzlm) this.zzb.remove(i)).getValue();
        if (!this.zzc.isEmpty()) {
            Iterator it = zzg().entrySet().iterator();
            this.zzb.add(new zzlm(this, (Map.Entry) it.next()));
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
            this.zze = new zzlo(this, null);
        }
        return this.zze;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzlh)) {
            return super.equals(obj);
        }
        zzlh zzlhVar = (zzlh) obj;
        int size = size();
        if (size != zzlhVar.size()) {
            return false;
        }
        int zzc = zzc();
        if (zzc != zzlhVar.zzc()) {
            return entrySet().equals(zzlhVar.entrySet());
        }
        for (int i = 0; i < zzc; i++) {
            if (!zzb(i).equals(zzlhVar.zzb(i))) {
                return false;
            }
        }
        if (zzc != size) {
            return this.zzc.equals(zzlhVar.zzc);
        }
        return true;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object get(Object obj) {
        Comparable comparable = (Comparable) obj;
        int zza = zza(comparable);
        return zza >= 0 ? ((zzlm) this.zzb.get(zza)).getValue() : this.zzc.get(comparable);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int hashCode() {
        int zzc = zzc();
        int i = 0;
        for (int i2 = 0; i2 < zzc; i2++) {
            i += ((zzlm) this.zzb.get(i2)).hashCode();
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
    /* renamed from: zza, reason: merged with bridge method [inline-methods] */
    public final Object put(Comparable comparable, Object obj) {
        zzf();
        int zza = zza(comparable);
        if (zza >= 0) {
            return ((zzlm) this.zzb.get(zza)).setValue(obj);
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
            zzlm zzlmVar = (zzlm) this.zzb.remove(i2 - 1);
            zzg().put((Comparable) zzlmVar.getKey(), zzlmVar.getValue());
        }
        this.zzb.add(i, new zzlm(this, comparable, obj));
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
        return this.zzc.isEmpty() ? zzll.zza() : this.zzc.entrySet();
    }

    final Set zze() {
        if (this.zzg == null) {
            this.zzg = new zzli(this, null);
        }
        return this.zzg;
    }
}
