package com.google.android.gms.internal.mlkit_language_id;

import java.util.List;
/* loaded from: classes.dex */
abstract class zzgr {
    private static final Class zza = zzd();
    private static final zzhh zzb = zza(false);
    private static final zzhh zzc = zza(true);
    private static final zzhh zzd = new zzhj();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(int i, Object obj, zzgp zzgpVar) {
        return zzea.zzb(i, (zzfz) obj, zzgpVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(int i, List list) {
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        int zze = zzea.zze(i) * size;
        if (list instanceof zzfg) {
            zzfg zzfgVar = (zzfg) list;
            while (i2 < size) {
                Object zza2 = zzfgVar.zza(i2);
                zze += zza2 instanceof zzdn ? zzea.zzb((zzdn) zza2) : zzea.zzb((String) zza2);
                i2++;
            }
        } else {
            while (i2 < size) {
                Object obj = list.get(i2);
                zze += obj instanceof zzdn ? zzea.zzb((zzdn) obj) : zzea.zzb((String) obj);
                i2++;
            }
        }
        return zze;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(int i, List list, zzgp zzgpVar) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int zze = zzea.zze(i) * size;
        for (int i2 = 0; i2 < size; i2++) {
            zze += zzea.zza((zzfz) list.get(i2), zzgpVar);
        }
        return zze;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(int i, List list, boolean z) {
        if (list.size() == 0) {
            return 0;
        }
        return zza(list) + (list.size() * zzea.zze(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(List list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzfn) {
            zzfn zzfnVar = (zzfn) list;
            if (size <= 0) {
                return 0;
            }
            throw null;
        }
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            i += zzea.zzd(((Long) list.get(i2)).longValue());
        }
        return i;
    }

    public static zzhh zza() {
        return zzb;
    }

    private static zzhh zza(boolean z) {
        try {
            Class zze = zze();
            if (zze == null) {
                return null;
            }
            return (zzhh) zze.getConstructor(Boolean.TYPE).newInstance(Boolean.valueOf(z));
        } catch (Throwable unused) {
            return null;
        }
    }

    public static void zza(int i, List list, zzib zzibVar) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzibVar.zza(i, list);
    }

    public static void zza(int i, List list, zzib zzibVar, zzgp zzgpVar) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzibVar.zza(i, list, zzgpVar);
    }

    public static void zza(int i, List list, zzib zzibVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzibVar.zzg(i, list, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void zza(zzee zzeeVar, Object obj, Object obj2) {
        zzej zza2 = zzeeVar.zza(obj2);
        if (zza2.zza.isEmpty()) {
            return;
        }
        zzeeVar.zzb(obj).zza(zza2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void zza(zzfs zzfsVar, Object obj, Object obj2, long j) {
        zzhn.zza(obj, j, zzfsVar.zza(zzhn.zzf(obj, j), zzhn.zzf(obj2, j)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void zza(zzhh zzhhVar, Object obj, Object obj2) {
        zzhhVar.zza(obj, zzhhVar.zzb(zzhhVar.zza(obj), zzhhVar.zza(obj2)));
    }

    public static void zza(Class cls) {
        Class cls2;
        if (!zzeo.class.isAssignableFrom(cls) && (cls2 = zza) != null && !cls2.isAssignableFrom(cls)) {
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
        int zze = size * zzea.zze(i);
        for (int i2 = 0; i2 < list.size(); i2++) {
            zze += zzea.zzb((zzdn) list.get(i2));
        }
        return zze;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzb(int i, List list, zzgp zzgpVar) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < size; i3++) {
            i2 += zzea.zzc(i, (zzfz) list.get(i3), zzgpVar);
        }
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzb(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzb(list) + (size * zzea.zze(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzb(List list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzfn) {
            zzfn zzfnVar = (zzfn) list;
            if (size <= 0) {
                return 0;
            }
            throw null;
        }
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            i += zzea.zze(((Long) list.get(i2)).longValue());
        }
        return i;
    }

    public static zzhh zzb() {
        return zzc;
    }

    public static void zzb(int i, List list, zzib zzibVar) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzibVar.zzb(i, list);
    }

    public static void zzb(int i, List list, zzib zzibVar, zzgp zzgpVar) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzibVar.zzb(i, list, zzgpVar);
    }

    public static void zzb(int i, List list, zzib zzibVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzibVar.zzf(i, list, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzc(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzc(list) + (size * zzea.zze(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzc(List list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzfn) {
            zzfn zzfnVar = (zzfn) list;
            if (size <= 0) {
                return 0;
            }
            throw null;
        }
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            i += zzea.zzf(((Long) list.get(i2)).longValue());
        }
        return i;
    }

    public static zzhh zzc() {
        return zzd;
    }

    public static void zzc(int i, List list, zzib zzibVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzibVar.zzc(i, list, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzd(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzd(list) + (size * zzea.zze(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzd(List list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzer) {
            zzer zzerVar = (zzer) list;
            i = 0;
            while (i2 < size) {
                i += zzea.zzk(zzerVar.zza(i2));
                i2++;
            }
        } else {
            i = 0;
            while (i2 < size) {
                i += zzea.zzk(((Integer) list.get(i2)).intValue());
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

    public static void zzd(int i, List list, zzib zzibVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzibVar.zzd(i, list, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zze(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zze(list) + (size * zzea.zze(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zze(List list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzer) {
            zzer zzerVar = (zzer) list;
            i = 0;
            while (i2 < size) {
                i += zzea.zzf(zzerVar.zza(i2));
                i2++;
            }
        } else {
            i = 0;
            while (i2 < size) {
                i += zzea.zzf(((Integer) list.get(i2)).intValue());
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

    public static void zze(int i, List list, zzib zzibVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzibVar.zzn(i, list, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzf(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzf(list) + (size * zzea.zze(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzf(List list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzer) {
            zzer zzerVar = (zzer) list;
            i = 0;
            while (i2 < size) {
                i += zzea.zzg(zzerVar.zza(i2));
                i2++;
            }
        } else {
            i = 0;
            while (i2 < size) {
                i += zzea.zzg(((Integer) list.get(i2)).intValue());
                i2++;
            }
        }
        return i;
    }

    public static void zzf(int i, List list, zzib zzibVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzibVar.zze(i, list, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzg(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzg(list) + (size * zzea.zze(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzg(List list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzer) {
            zzer zzerVar = (zzer) list;
            i = 0;
            while (i2 < size) {
                i += zzea.zzh(zzerVar.zza(i2));
                i2++;
            }
        } else {
            i = 0;
            while (i2 < size) {
                i += zzea.zzh(((Integer) list.get(i2)).intValue());
                i2++;
            }
        }
        return i;
    }

    public static void zzg(int i, List list, zzib zzibVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzibVar.zzl(i, list, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzh(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * zzea.zzi(i, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzh(List list) {
        return list.size() << 2;
    }

    public static void zzh(int i, List list, zzib zzibVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzibVar.zza(i, list, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzi(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * zzea.zzg(i, 0L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzi(List list) {
        return list.size() << 3;
    }

    public static void zzi(int i, List list, zzib zzibVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzibVar.zzj(i, list, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzj(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * zzea.zzb(i, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzj(List list) {
        return list.size();
    }

    public static void zzj(int i, List list, zzib zzibVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzibVar.zzm(i, list, z);
    }

    public static void zzk(int i, List list, zzib zzibVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzibVar.zzb(i, list, z);
    }

    public static void zzl(int i, List list, zzib zzibVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzibVar.zzk(i, list, z);
    }

    public static void zzm(int i, List list, zzib zzibVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzibVar.zzh(i, list, z);
    }

    public static void zzn(int i, List list, zzib zzibVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzibVar.zzi(i, list, z);
    }
}
