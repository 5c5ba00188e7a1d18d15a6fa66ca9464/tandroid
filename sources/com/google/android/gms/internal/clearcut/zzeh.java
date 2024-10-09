package com.google.android.gms.internal.clearcut;

import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;

/* loaded from: classes.dex */
abstract class zzeh {
    private static final Class zzoh = zzdp();
    private static final zzex zzoi = zzd(false);
    private static final zzex zzoj = zzd(true);
    private static final zzex zzok = new zzez();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(List list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzdc) {
            if (size <= 0) {
                return 0;
            }
            throw null;
        }
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            i += zzbn.zze(((Long) list.get(i2)).longValue());
        }
        return i;
    }

    private static Object zza(int i, int i2, Object obj, zzex zzexVar) {
        if (obj == null) {
            obj = zzexVar.zzdz();
        }
        zzexVar.zza(obj, i, i2);
        return obj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object zza(int i, List list, zzck zzckVar, Object obj, zzex zzexVar) {
        if (zzckVar == null) {
            return obj;
        }
        if (list instanceof RandomAccess) {
            int size = list.size();
            int i2 = 0;
            for (int i3 = 0; i3 < size; i3++) {
                Integer num = (Integer) list.get(i3);
                int intValue = num.intValue();
                if (zzckVar.zzb(intValue) != null) {
                    if (i3 != i2) {
                        list.set(i2, num);
                    }
                    i2++;
                } else {
                    obj = zza(i, intValue, obj, zzexVar);
                }
            }
            if (i2 != size) {
                list.subList(i2, size).clear();
            }
        } else {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                int intValue2 = ((Integer) it.next()).intValue();
                if (zzckVar.zzb(intValue2) == null) {
                    obj = zza(i, intValue2, obj, zzexVar);
                    it.remove();
                }
            }
        }
        return obj;
    }

    public static void zza(int i, List list, zzfr zzfrVar) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzfrVar.zza(i, list);
    }

    public static void zza(int i, List list, zzfr zzfrVar, zzef zzefVar) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzfrVar.zza(i, list, zzefVar);
    }

    public static void zza(int i, List list, zzfr zzfrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzfrVar.zzg(i, list, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void zza(zzbu zzbuVar, Object obj, Object obj2) {
        zzby zza = zzbuVar.zza(obj2);
        if (zza.isEmpty()) {
            return;
        }
        zzbuVar.zzb(obj).zza(zza);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void zza(zzdj zzdjVar, Object obj, Object obj2, long j) {
        zzfd.zza(obj, j, zzdjVar.zzb(zzfd.zzo(obj, j), zzfd.zzo(obj2, j)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void zza(zzex zzexVar, Object obj, Object obj2) {
        zzexVar.zze(obj, zzexVar.zzg(zzexVar.zzq(obj), zzexVar.zzq(obj2)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzb(List list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzdc) {
            if (size <= 0) {
                return 0;
            }
            throw null;
        }
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            i += zzbn.zzf(((Long) list.get(i2)).longValue());
        }
        return i;
    }

    public static void zzb(int i, List list, zzfr zzfrVar) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzfrVar.zzb(i, list);
    }

    public static void zzb(int i, List list, zzfr zzfrVar, zzef zzefVar) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzfrVar.zzb(i, list, zzefVar);
    }

    public static void zzb(int i, List list, zzfr zzfrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzfrVar.zzf(i, list, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzc(int i, Object obj, zzef zzefVar) {
        return zzbn.zzb(i, (zzdo) obj, zzefVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzc(int i, List list) {
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        int zzr = zzbn.zzr(i) * size;
        if (list instanceof zzcx) {
            zzcx zzcxVar = (zzcx) list;
            while (i2 < size) {
                Object raw = zzcxVar.getRaw(i2);
                zzr += raw instanceof zzbb ? zzbn.zzb((zzbb) raw) : zzbn.zzh((String) raw);
                i2++;
            }
        } else {
            while (i2 < size) {
                Object obj = list.get(i2);
                zzr += obj instanceof zzbb ? zzbn.zzb((zzbb) obj) : zzbn.zzh((String) obj);
                i2++;
            }
        }
        return zzr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzc(int i, List list, zzef zzefVar) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int zzr = zzbn.zzr(i) * size;
        for (int i2 = 0; i2 < size; i2++) {
            zzr += zzbn.zzb((zzdo) list.get(i2), zzefVar);
        }
        return zzr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzc(List list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzdc) {
            if (size <= 0) {
                return 0;
            }
            throw null;
        }
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            i += zzbn.zzg(((Long) list.get(i2)).longValue());
        }
        return i;
    }

    public static void zzc(int i, List list, zzfr zzfrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzfrVar.zzc(i, list, z);
    }

    public static boolean zzc(int i, int i2, int i3) {
        if (i2 < 40) {
            return true;
        }
        long j = i2 - i;
        long j2 = i3;
        return j + 10 <= ((2 * j2) + 3) + ((j2 + 3) * 3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzd(int i, List list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int zzr = size * zzbn.zzr(i);
        for (int i2 = 0; i2 < list.size(); i2++) {
            zzr += zzbn.zzb((zzbb) list.get(i2));
        }
        return zzr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzd(int i, List list, zzef zzefVar) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < size; i3++) {
            i2 += zzbn.zzc(i, (zzdo) list.get(i3), zzefVar);
        }
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzd(List list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzch) {
            if (size <= 0) {
                return 0;
            }
            throw null;
        }
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            i += zzbn.zzx(((Integer) list.get(i2)).intValue());
        }
        return i;
    }

    private static zzex zzd(boolean z) {
        try {
            Class zzdq = zzdq();
            if (zzdq == null) {
                return null;
            }
            return (zzex) zzdq.getConstructor(Boolean.TYPE).newInstance(Boolean.valueOf(z));
        } catch (Throwable unused) {
            return null;
        }
    }

    public static void zzd(int i, List list, zzfr zzfrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzfrVar.zzd(i, list, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean zzd(Object obj, Object obj2) {
        if (obj != obj2) {
            return obj != null && obj.equals(obj2);
        }
        return true;
    }

    public static zzex zzdm() {
        return zzoi;
    }

    public static zzex zzdn() {
        return zzoj;
    }

    public static zzex zzdo() {
        return zzok;
    }

    private static Class zzdp() {
        try {
            return Class.forName("com.google.protobuf.GeneratedMessage");
        } catch (Throwable unused) {
            return null;
        }
    }

    private static Class zzdq() {
        try {
            return Class.forName("com.google.protobuf.UnknownFieldSetSchema");
        } catch (Throwable unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zze(List list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzch) {
            if (size <= 0) {
                return 0;
            }
            throw null;
        }
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            i += zzbn.zzs(((Integer) list.get(i2)).intValue());
        }
        return i;
    }

    public static void zze(int i, List list, zzfr zzfrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzfrVar.zzn(i, list, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzf(List list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzch) {
            if (size <= 0) {
                return 0;
            }
            throw null;
        }
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            i += zzbn.zzt(((Integer) list.get(i2)).intValue());
        }
        return i;
    }

    public static void zzf(int i, List list, zzfr zzfrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzfrVar.zze(i, list, z);
    }

    public static void zzf(Class cls) {
        Class cls2;
        if (!zzcg.class.isAssignableFrom(cls) && (cls2 = zzoh) != null && !cls2.isAssignableFrom(cls)) {
            throw new IllegalArgumentException("Message classes must extend GeneratedMessage or GeneratedMessageLite");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzg(List list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzch) {
            if (size <= 0) {
                return 0;
            }
            throw null;
        }
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            i += zzbn.zzu(((Integer) list.get(i2)).intValue());
        }
        return i;
    }

    public static void zzg(int i, List list, zzfr zzfrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzfrVar.zzl(i, list, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzh(List list) {
        return list.size() << 2;
    }

    public static void zzh(int i, List list, zzfr zzfrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzfrVar.zza(i, list, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzi(List list) {
        return list.size() << 3;
    }

    public static void zzi(int i, List list, zzfr zzfrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzfrVar.zzj(i, list, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzj(List list) {
        return list.size();
    }

    public static void zzj(int i, List list, zzfr zzfrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzfrVar.zzm(i, list, z);
    }

    public static void zzk(int i, List list, zzfr zzfrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzfrVar.zzb(i, list, z);
    }

    public static void zzl(int i, List list, zzfr zzfrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzfrVar.zzk(i, list, z);
    }

    public static void zzm(int i, List list, zzfr zzfrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzfrVar.zzh(i, list, z);
    }

    public static void zzn(int i, List list, zzfr zzfrVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzfrVar.zzi(i, list, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzo(int i, List list, boolean z) {
        if (list.size() == 0) {
            return 0;
        }
        return zza(list) + (list.size() * zzbn.zzr(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzp(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzb(list) + (size * zzbn.zzr(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzq(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzc(list) + (size * zzbn.zzr(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzr(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzd(list) + (size * zzbn.zzr(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzs(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zze(list) + (size * zzbn.zzr(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzt(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzf(list) + (size * zzbn.zzr(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzu(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzg(list) + (size * zzbn.zzr(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzv(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * zzbn.zzj(i, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzw(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * zzbn.zzg(i, 0L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzx(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * zzbn.zzc(i, true);
    }
}
