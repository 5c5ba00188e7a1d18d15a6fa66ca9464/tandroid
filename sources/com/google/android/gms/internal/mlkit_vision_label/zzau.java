package com.google.android.gms.internal.mlkit_vision_label;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzau extends AbstractMap implements Serializable {
    private static final Object zzd = new Object();
    transient int[] zza;
    transient Object[] zzb;
    transient Object[] zzc;
    private transient Object zze;
    private transient int zzf;
    private transient int zzg;
    private transient Set zzh;
    private transient Set zzi;
    private transient Collection zzj;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzau(int i) {
        zzo(12);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Object[] zzA() {
        Object[] objArr = this.zzb;
        objArr.getClass();
        return objArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Object[] zzB() {
        Object[] objArr = this.zzc;
        objArr.getClass();
        return objArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int zzb(zzau zzauVar) {
        int i = zzauVar.zzg;
        zzauVar.zzg = i - 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Object zzg(zzau zzauVar, int i) {
        return zzauVar.zzA()[i];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Object zzj(zzau zzauVar, int i) {
        return zzauVar.zzB()[i];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Object zzk(zzau zzauVar) {
        Object obj = zzauVar.zze;
        obj.getClass();
        return obj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void zzm(zzau zzauVar, int i, Object obj) {
        zzauVar.zzB()[i] = obj;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int zzu() {
        return (1 << (this.zzf & 31)) - 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int zzv(Object obj) {
        if (zzq()) {
            return -1;
        }
        int zza = zzaw.zza(obj);
        int zzu = zzu();
        Object obj2 = this.zze;
        obj2.getClass();
        int zzc = zzav.zzc(obj2, zza & zzu);
        if (zzc != 0) {
            int i = zzu ^ (-1);
            int i2 = zza & i;
            do {
                int i3 = zzc - 1;
                int i4 = zzz()[i3];
                if ((i4 & i) == i2 && zzo.zza(obj, zzA()[i3])) {
                    return i3;
                }
                zzc = i4 & zzu;
            } while (zzc != 0);
        }
        return -1;
    }

    private final int zzw(int i, int i2, int i3, int i4) {
        Object zzd2 = zzav.zzd(i2);
        int i5 = i2 - 1;
        if (i4 != 0) {
            zzav.zze(zzd2, i3 & i5, i4 + 1);
        }
        Object obj = this.zze;
        obj.getClass();
        int[] zzz = zzz();
        for (int i6 = 0; i6 <= i; i6++) {
            int zzc = zzav.zzc(obj, i6);
            while (zzc != 0) {
                int i7 = zzc - 1;
                int i8 = zzz[i7];
                int i9 = ((i ^ (-1)) & i8) | i6;
                int i10 = i9 & i5;
                int zzc2 = zzav.zzc(zzd2, i10);
                zzav.zze(zzd2, i10, zzc);
                zzz[i7] = ((i5 ^ (-1)) & i9) | (zzc2 & i5);
                zzc = i8 & i;
            }
        }
        this.zze = zzd2;
        zzy(i5);
        return i5;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Object zzx(Object obj) {
        if (zzq()) {
            return zzd;
        }
        int zzu = zzu();
        Object obj2 = this.zze;
        obj2.getClass();
        int zzb = zzav.zzb(obj, null, zzu, obj2, zzz(), zzA(), null);
        if (zzb == -1) {
            return zzd;
        }
        Object obj3 = zzB()[zzb];
        zzp(zzb, zzu);
        this.zzg--;
        zzn();
        return obj3;
    }

    private final void zzy(int i) {
        this.zzf = ((32 - Integer.numberOfLeadingZeros(i)) & 31) | (this.zzf & (-32));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int[] zzz() {
        int[] iArr = this.zza;
        iArr.getClass();
        return iArr;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final void clear() {
        if (zzq()) {
            return;
        }
        zzn();
        Map zzl = zzl();
        if (zzl != null) {
            this.zzf = zzcf.zza(size(), 3, 1073741823);
            zzl.clear();
            this.zze = null;
        } else {
            Arrays.fill(zzA(), 0, this.zzg, (Object) null);
            Arrays.fill(zzB(), 0, this.zzg, (Object) null);
            Object obj = this.zze;
            obj.getClass();
            if (obj instanceof byte[]) {
                Arrays.fill((byte[]) obj, (byte) 0);
            } else if (obj instanceof short[]) {
                Arrays.fill((short[]) obj, (short) 0);
            } else {
                Arrays.fill((int[]) obj, 0);
            }
            Arrays.fill(zzz(), 0, this.zzg, 0);
        }
        this.zzg = 0;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final boolean containsKey(Object obj) {
        Map zzl = zzl();
        return zzl != null ? zzl.containsKey(obj) : zzv(obj) != -1;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final boolean containsValue(Object obj) {
        Map zzl = zzl();
        if (zzl != null) {
            return zzl.containsValue(obj);
        }
        for (int i = 0; i < this.zzg; i++) {
            if (zzo.zza(obj, zzB()[i])) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final Set entrySet() {
        Set set = this.zzi;
        if (set != null) {
            return set;
        }
        zzap zzapVar = new zzap(this);
        this.zzi = zzapVar;
        return zzapVar;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final Object get(Object obj) {
        Map zzl = zzl();
        if (zzl != null) {
            return zzl.get(obj);
        }
        int zzv = zzv(obj);
        if (zzv == -1) {
            return null;
        }
        return zzB()[zzv];
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final boolean isEmpty() {
        return size() == 0;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final Set keySet() {
        Set set = this.zzh;
        if (set != null) {
            return set;
        }
        zzar zzarVar = new zzar(this);
        this.zzh = zzarVar;
        return zzarVar;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final Object put(Object obj, Object obj2) {
        int length;
        int min;
        if (zzq()) {
            zzs.zzd(zzq(), "Arrays already allocated");
            int i = this.zzf;
            int max = Math.max(i + 1, 2);
            int highestOneBit = Integer.highestOneBit(max);
            if (max > highestOneBit && (highestOneBit = highestOneBit + highestOneBit) <= 0) {
                highestOneBit = 1073741824;
            }
            int max2 = Math.max(4, highestOneBit);
            this.zze = zzav.zzd(max2);
            zzy(max2 - 1);
            this.zza = new int[i];
            this.zzb = new Object[i];
            this.zzc = new Object[i];
        }
        Map zzl = zzl();
        if (zzl != null) {
            return zzl.put(obj, obj2);
        }
        int[] zzz = zzz();
        Object[] zzA = zzA();
        Object[] zzB = zzB();
        int i2 = this.zzg;
        int i3 = i2 + 1;
        int zza = zzaw.zza(obj);
        int zzu = zzu();
        int i4 = zza & zzu;
        Object obj3 = this.zze;
        obj3.getClass();
        int zzc = zzav.zzc(obj3, i4);
        if (zzc == 0) {
            if (i3 <= zzu) {
                Object obj4 = this.zze;
                obj4.getClass();
                zzav.zze(obj4, i4, i3);
                length = zzz().length;
                if (i3 > length && (min = Math.min(1073741823, (Math.max(1, length >>> 1) + length) | 1)) != length) {
                    this.zza = Arrays.copyOf(zzz(), min);
                    this.zzb = Arrays.copyOf(zzA(), min);
                    this.zzc = Arrays.copyOf(zzB(), min);
                }
                zzz()[i2] = (zzu ^ (-1)) & zza;
                zzA()[i2] = obj;
                zzB()[i2] = obj2;
                this.zzg = i3;
                zzn();
                return null;
            }
            zzu = zzw(zzu, zzav.zza(zzu), zza, i2);
            length = zzz().length;
            if (i3 > length) {
                this.zza = Arrays.copyOf(zzz(), min);
                this.zzb = Arrays.copyOf(zzA(), min);
                this.zzc = Arrays.copyOf(zzB(), min);
            }
            zzz()[i2] = (zzu ^ (-1)) & zza;
            zzA()[i2] = obj;
            zzB()[i2] = obj2;
            this.zzg = i3;
            zzn();
            return null;
        }
        int i5 = zzu ^ (-1);
        int i6 = zza & i5;
        int i7 = 0;
        while (true) {
            int i8 = zzc - 1;
            int i9 = zzz[i8];
            int i10 = i9 & i5;
            if (i10 == i6 && zzo.zza(obj, zzA[i8])) {
                Object obj5 = zzB[i8];
                zzB[i8] = obj2;
                return obj5;
            }
            int i11 = i9 & zzu;
            i7++;
            if (i11 != 0) {
                zzc = i11;
            } else {
                if (i7 >= 9) {
                    LinkedHashMap linkedHashMap = new LinkedHashMap(zzu() + 1, 1.0f);
                    int zze = zze();
                    while (zze >= 0) {
                        linkedHashMap.put(zzA()[zze], zzB()[zze]);
                        zze = zzf(zze);
                    }
                    this.zze = linkedHashMap;
                    this.zza = null;
                    this.zzb = null;
                    this.zzc = null;
                    zzn();
                    return linkedHashMap.put(obj, obj2);
                }
                if (i3 <= zzu) {
                    zzz[i8] = (i3 & zzu) | i10;
                }
            }
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final Object remove(Object obj) {
        Map zzl = zzl();
        if (zzl != null) {
            return zzl.remove(obj);
        }
        Object zzx = zzx(obj);
        if (zzx == zzd) {
            return null;
        }
        return zzx;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final int size() {
        Map zzl = zzl();
        return zzl != null ? zzl.size() : this.zzg;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final Collection values() {
        Collection collection = this.zzj;
        if (collection != null) {
            return collection;
        }
        zzat zzatVar = new zzat(this);
        this.zzj = zzatVar;
        return zzatVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int zze() {
        return isEmpty() ? -1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int zzf(int i) {
        int i2 = i + 1;
        if (i2 < this.zzg) {
            return i2;
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Map zzl() {
        Object obj = this.zze;
        if (obj instanceof Map) {
            return (Map) obj;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zzn() {
        this.zzf += 32;
    }

    final void zzo(int i) {
        this.zzf = zzcf.zza(12, 1, 1073741823);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zzp(int i, int i2) {
        Object obj = this.zze;
        obj.getClass();
        int[] zzz = zzz();
        Object[] zzA = zzA();
        Object[] zzB = zzB();
        int size = size();
        int i3 = size - 1;
        if (i >= i3) {
            zzA[i] = null;
            zzB[i] = null;
            zzz[i] = 0;
            return;
        }
        Object obj2 = zzA[i3];
        zzA[i] = obj2;
        zzB[i] = zzB[i3];
        zzA[i3] = null;
        zzB[i3] = null;
        zzz[i] = zzz[i3];
        zzz[i3] = 0;
        int zza = zzaw.zza(obj2) & i2;
        int zzc = zzav.zzc(obj, zza);
        if (zzc == size) {
            zzav.zze(obj, zza, i + 1);
            return;
        }
        while (true) {
            int i4 = zzc - 1;
            int i5 = zzz[i4];
            int i6 = i5 & i2;
            if (i6 == size) {
                zzz[i4] = ((i + 1) & i2) | (i5 & (i2 ^ (-1)));
                return;
            }
            zzc = i6;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean zzq() {
        return this.zze == null;
    }
}
