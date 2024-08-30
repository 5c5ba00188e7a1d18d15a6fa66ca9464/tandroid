package com.google.android.gms.internal.vision;

import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class zzle {
    private static final Class zza = zzd();
    private static final zzlu zzb = zza(false);
    private static final zzlu zzc = zza(true);
    private static final zzlu zzd = new zzlw();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(int i, Object obj, zzlc zzlcVar) {
        return zzii.zzb(i, (zzkk) obj, zzlcVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(int i, List list) {
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        int zze = zzii.zze(i) * size;
        if (list instanceof zzjv) {
            zzjv zzjvVar = (zzjv) list;
            while (i2 < size) {
                Object zzb2 = zzjvVar.zzb(i2);
                zze += zzb2 instanceof zzht ? zzii.zzb((zzht) zzb2) : zzii.zzb((String) zzb2);
                i2++;
            }
        } else {
            while (i2 < size) {
                Object obj = list.get(i2);
                zze += obj instanceof zzht ? zzii.zzb((zzht) obj) : zzii.zzb((String) obj);
                i2++;
            }
        }
        return zze;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(int i, List list, zzlc zzlcVar) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int zze = zzii.zze(i) * size;
        for (int i2 = 0; i2 < size; i2++) {
            zze += zzii.zza((zzkk) list.get(i2), zzlcVar);
        }
        return zze;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(int i, List list, boolean z) {
        if (list.size() == 0) {
            return 0;
        }
        return zza(list) + (list.size() * zzii.zze(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(List list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzjy) {
            zzjy zzjyVar = (zzjy) list;
            if (size <= 0) {
                return 0;
            }
            throw null;
        }
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            i += zzii.zzd(((Long) list.get(i2)).longValue());
        }
        return i;
    }

    public static zzlu zza() {
        return zzb;
    }

    private static zzlu zza(boolean z) {
        try {
            Class zze = zze();
            if (zze == null) {
                return null;
            }
            return (zzlu) zze.getConstructor(Boolean.TYPE).newInstance(Boolean.valueOf(z));
        } catch (Throwable unused) {
            return null;
        }
    }

    static Object zza(int i, int i2, Object obj, zzlu zzluVar) {
        if (obj == null) {
            obj = zzluVar.zza();
        }
        zzluVar.zza(obj, i, i2);
        return obj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object zza(int i, List list, zzjg zzjgVar, Object obj, zzlu zzluVar) {
        if (zzjgVar == null) {
            return obj;
        }
        if (list instanceof RandomAccess) {
            int size = list.size();
            int i2 = 0;
            for (int i3 = 0; i3 < size; i3++) {
                Integer num = (Integer) list.get(i3);
                int intValue = num.intValue();
                if (zzjgVar.zza(intValue)) {
                    if (i3 != i2) {
                        list.set(i2, num);
                    }
                    i2++;
                } else {
                    obj = zza(i, intValue, obj, zzluVar);
                }
            }
            if (i2 != size) {
                list.subList(i2, size).clear();
            }
        } else {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                int intValue2 = ((Integer) it.next()).intValue();
                if (!zzjgVar.zza(intValue2)) {
                    obj = zza(i, intValue2, obj, zzluVar);
                    it.remove();
                }
            }
        }
        return obj;
    }

    public static void zza(int i, List list, zzmr zzmrVar) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmrVar.zza(i, list);
    }

    public static void zza(int i, List list, zzmr zzmrVar, zzlc zzlcVar) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmrVar.zza(i, list, zzlcVar);
    }

    public static void zza(int i, List list, zzmr zzmrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmrVar.zzg(i, list, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void zza(zziq zziqVar, Object obj, Object obj2) {
        zziu zza2 = zziqVar.zza(obj2);
        if (zza2.zza.isEmpty()) {
            return;
        }
        zziqVar.zzb(obj).zza(zza2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void zza(zzkh zzkhVar, Object obj, Object obj2, long j) {
        zzma.zza(obj, j, zzkhVar.zza(zzma.zzf(obj, j), zzma.zzf(obj2, j)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void zza(zzlu zzluVar, Object obj, Object obj2) {
        zzluVar.zza(obj, zzluVar.zzc(zzluVar.zzb(obj), zzluVar.zzb(obj2)));
    }

    public static void zza(Class cls) {
        Class cls2;
        if (!zzjb.class.isAssignableFrom(cls) && (cls2 = zza) != null && !cls2.isAssignableFrom(cls)) {
            throw new IllegalArgumentException("Message classes must extend GeneratedMessage or GeneratedMessageLite");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean zza(Object obj, Object obj2) {
        if (obj != obj2) {
            return obj != null && obj.equals(obj2);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzb(int i, List list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int zze = size * zzii.zze(i);
        for (int i2 = 0; i2 < list.size(); i2++) {
            zze += zzii.zzb((zzht) list.get(i2));
        }
        return zze;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzb(int i, List list, zzlc zzlcVar) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < size; i3++) {
            i2 += zzii.zzc(i, (zzkk) list.get(i3), zzlcVar);
        }
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzb(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzb(list) + (size * zzii.zze(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzb(List list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzjy) {
            zzjy zzjyVar = (zzjy) list;
            if (size <= 0) {
                return 0;
            }
            throw null;
        }
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            i += zzii.zze(((Long) list.get(i2)).longValue());
        }
        return i;
    }

    public static zzlu zzb() {
        return zzc;
    }

    public static void zzb(int i, List list, zzmr zzmrVar) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmrVar.zzb(i, list);
    }

    public static void zzb(int i, List list, zzmr zzmrVar, zzlc zzlcVar) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmrVar.zzb(i, list, zzlcVar);
    }

    public static void zzb(int i, List list, zzmr zzmrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmrVar.zzf(i, list, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzc(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzc(list) + (size * zzii.zze(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzc(List list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzjy) {
            zzjy zzjyVar = (zzjy) list;
            if (size <= 0) {
                return 0;
            }
            throw null;
        }
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            i += zzii.zzf(((Long) list.get(i2)).longValue());
        }
        return i;
    }

    public static zzlu zzc() {
        return zzd;
    }

    public static void zzc(int i, List list, zzmr zzmrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmrVar.zzc(i, list, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzd(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzd(list) + (size * zzii.zze(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzd(List list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzjd) {
            zzjd zzjdVar = (zzjd) list;
            i = 0;
            while (i2 < size) {
                i += zzii.zzk(zzjdVar.zzb(i2));
                i2++;
            }
        } else {
            i = 0;
            while (i2 < size) {
                i += zzii.zzk(((Integer) list.get(i2)).intValue());
                i2++;
            }
        }
        return i;
    }

    private static Class zzd() {
        try {
            return Class.forName("com.google.protobuf.GeneratedMessage");
        } catch (Throwable unused) {
            return null;
        }
    }

    public static void zzd(int i, List list, zzmr zzmrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmrVar.zzd(i, list, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zze(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zze(list) + (size * zzii.zze(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zze(List list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzjd) {
            zzjd zzjdVar = (zzjd) list;
            i = 0;
            while (i2 < size) {
                i += zzii.zzf(zzjdVar.zzb(i2));
                i2++;
            }
        } else {
            i = 0;
            while (i2 < size) {
                i += zzii.zzf(((Integer) list.get(i2)).intValue());
                i2++;
            }
        }
        return i;
    }

    private static Class zze() {
        try {
            return Class.forName("com.google.protobuf.UnknownFieldSetSchema");
        } catch (Throwable unused) {
            return null;
        }
    }

    public static void zze(int i, List list, zzmr zzmrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmrVar.zzn(i, list, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzf(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzf(list) + (size * zzii.zze(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzf(List list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzjd) {
            zzjd zzjdVar = (zzjd) list;
            i = 0;
            while (i2 < size) {
                i += zzii.zzg(zzjdVar.zzb(i2));
                i2++;
            }
        } else {
            i = 0;
            while (i2 < size) {
                i += zzii.zzg(((Integer) list.get(i2)).intValue());
                i2++;
            }
        }
        return i;
    }

    public static void zzf(int i, List list, zzmr zzmrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmrVar.zze(i, list, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzg(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzg(list) + (size * zzii.zze(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzg(List list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzjd) {
            zzjd zzjdVar = (zzjd) list;
            i = 0;
            while (i2 < size) {
                i += zzii.zzh(zzjdVar.zzb(i2));
                i2++;
            }
        } else {
            i = 0;
            while (i2 < size) {
                i += zzii.zzh(((Integer) list.get(i2)).intValue());
                i2++;
            }
        }
        return i;
    }

    public static void zzg(int i, List list, zzmr zzmrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmrVar.zzl(i, list, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzh(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * zzii.zzi(i, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzh(List list) {
        return list.size() << 2;
    }

    public static void zzh(int i, List list, zzmr zzmrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmrVar.zza(i, list, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzi(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * zzii.zzg(i, 0L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzi(List list) {
        return list.size() << 3;
    }

    public static void zzi(int i, List list, zzmr zzmrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmrVar.zzj(i, list, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzj(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * zzii.zzb(i, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzj(List list) {
        return list.size();
    }

    public static void zzj(int i, List list, zzmr zzmrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmrVar.zzm(i, list, z);
    }

    public static void zzk(int i, List list, zzmr zzmrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmrVar.zzb(i, list, z);
    }

    public static void zzl(int i, List list, zzmr zzmrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmrVar.zzk(i, list, z);
    }

    public static void zzm(int i, List list, zzmr zzmrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmrVar.zzh(i, list, z);
    }

    public static void zzn(int i, List list, zzmr zzmrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmrVar.zzi(i, list, z);
    }
}
