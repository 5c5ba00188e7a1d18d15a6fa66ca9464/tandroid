package com.google.android.recaptcha.internal;

import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;

/* loaded from: classes.dex */
final class zzkt {
    public static final /* synthetic */ int zza = 0;
    private static final Class zzb;
    private static final zzll zzc;
    private static final zzll zzd;

    static {
        Class<?> cls;
        Class<?> cls2;
        zzll zzllVar = null;
        try {
            cls = Class.forName("com.google.protobuf.GeneratedMessage");
        } catch (Throwable unused) {
            cls = null;
        }
        zzb = cls;
        try {
            cls2 = Class.forName("com.google.protobuf.UnknownFieldSetSchema");
        } catch (Throwable unused2) {
            cls2 = null;
        }
        if (cls2 != null) {
            try {
                zzllVar = (zzll) cls2.getConstructor(null).newInstance(null);
            } catch (Throwable unused3) {
            }
        }
        zzc = zzllVar;
        zzd = new zzln();
    }

    public static void zzA(int i, List list, zzmd zzmdVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmdVar.zzu(i, list, z);
    }

    public static void zzB(int i, List list, zzmd zzmdVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmdVar.zzy(i, list, z);
    }

    public static void zzC(int i, List list, zzmd zzmdVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmdVar.zzA(i, list, z);
    }

    public static void zzD(int i, List list, zzmd zzmdVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmdVar.zzC(i, list, z);
    }

    public static void zzE(int i, List list, zzmd zzmdVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmdVar.zzE(i, list, z);
    }

    public static void zzF(int i, List list, zzmd zzmdVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmdVar.zzJ(i, list, z);
    }

    public static void zzG(int i, List list, zzmd zzmdVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmdVar.zzL(i, list, z);
    }

    static boolean zzH(Object obj, Object obj2) {
        if (obj != obj2) {
            return obj != null && obj.equals(obj2);
        }
        return true;
    }

    static int zza(List list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zziu) {
            zziu zziuVar = (zziu) list;
            i = 0;
            while (i2 < size) {
                i += zzhh.zzu(zziuVar.zze(i2));
                i2++;
            }
        } else {
            i = 0;
            while (i2 < size) {
                i += zzhh.zzu(((Integer) list.get(i2)).intValue());
                i2++;
            }
        }
        return i;
    }

    static int zzb(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * (zzhh.zzy(i << 3) + 4);
    }

    static int zzc(List list) {
        return list.size() * 4;
    }

    static int zzd(int i, List list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * (zzhh.zzy(i << 3) + 8);
    }

    static int zze(List list) {
        return list.size() * 8;
    }

    static int zzf(List list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zziu) {
            zziu zziuVar = (zziu) list;
            i = 0;
            while (i2 < size) {
                i += zzhh.zzu(zziuVar.zze(i2));
                i2++;
            }
        } else {
            i = 0;
            while (i2 < size) {
                i += zzhh.zzu(((Integer) list.get(i2)).intValue());
                i2++;
            }
        }
        return i;
    }

    static int zzg(List list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzjt) {
            zzjt zzjtVar = (zzjt) list;
            i = 0;
            while (i2 < size) {
                i += zzhh.zzz(zzjtVar.zze(i2));
                i2++;
            }
        } else {
            i = 0;
            while (i2 < size) {
                i += zzhh.zzz(((Long) list.get(i2)).longValue());
                i2++;
            }
        }
        return i;
    }

    static int zzh(int i, Object obj, zzkr zzkrVar) {
        int i2 = i << 3;
        if (!(obj instanceof zzjk)) {
            return zzhh.zzy(i2) + zzhh.zzw((zzke) obj, zzkrVar);
        }
        int i3 = zzhh.zzb;
        int zza2 = ((zzjk) obj).zza();
        return zzhh.zzy(i2) + zzhh.zzy(zza2) + zza2;
    }

    static int zzi(List list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zziu) {
            zziu zziuVar = (zziu) list;
            i = 0;
            while (i2 < size) {
                int zze = zziuVar.zze(i2);
                i += zzhh.zzy((zze >> 31) ^ (zze + zze));
                i2++;
            }
        } else {
            i = 0;
            while (i2 < size) {
                int intValue = ((Integer) list.get(i2)).intValue();
                i += zzhh.zzy((intValue >> 31) ^ (intValue + intValue));
                i2++;
            }
        }
        return i;
    }

    static int zzj(List list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzjt) {
            zzjt zzjtVar = (zzjt) list;
            i = 0;
            while (i2 < size) {
                long zze = zzjtVar.zze(i2);
                i += zzhh.zzz((zze >> 63) ^ (zze + zze));
                i2++;
            }
        } else {
            i = 0;
            while (i2 < size) {
                long longValue = ((Long) list.get(i2)).longValue();
                i += zzhh.zzz((longValue >> 63) ^ (longValue + longValue));
                i2++;
            }
        }
        return i;
    }

    static int zzk(List list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zziu) {
            zziu zziuVar = (zziu) list;
            i = 0;
            while (i2 < size) {
                i += zzhh.zzy(zziuVar.zze(i2));
                i2++;
            }
        } else {
            i = 0;
            while (i2 < size) {
                i += zzhh.zzy(((Integer) list.get(i2)).intValue());
                i2++;
            }
        }
        return i;
    }

    static int zzl(List list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzjt) {
            zzjt zzjtVar = (zzjt) list;
            i = 0;
            while (i2 < size) {
                i += zzhh.zzz(zzjtVar.zze(i2));
                i2++;
            }
        } else {
            i = 0;
            while (i2 < size) {
                i += zzhh.zzz(((Long) list.get(i2)).longValue());
                i2++;
            }
        }
        return i;
    }

    public static zzll zzm() {
        return zzc;
    }

    public static zzll zzn() {
        return zzd;
    }

    static Object zzo(Object obj, int i, List list, zzix zzixVar, Object obj2, zzll zzllVar) {
        if (zzixVar == null) {
            return obj2;
        }
        if (list instanceof RandomAccess) {
            int size = list.size();
            int i2 = 0;
            for (int i3 = 0; i3 < size; i3++) {
                Integer num = (Integer) list.get(i3);
                int intValue = num.intValue();
                if (zzixVar.zza(intValue)) {
                    if (i3 != i2) {
                        list.set(i2, num);
                    }
                    i2++;
                } else {
                    obj2 = zzp(obj, i, intValue, obj2, zzllVar);
                }
            }
            if (i2 != size) {
                list.subList(i2, size).clear();
                return obj2;
            }
        } else {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                int intValue2 = ((Integer) it.next()).intValue();
                if (!zzixVar.zza(intValue2)) {
                    obj2 = zzp(obj, i, intValue2, obj2, zzllVar);
                    it.remove();
                }
            }
        }
        return obj2;
    }

    static Object zzp(Object obj, int i, int i2, Object obj2, zzll zzllVar) {
        if (obj2 == null) {
            obj2 = zzllVar.zzc(obj);
        }
        zzllVar.zzl(obj2, i, i2);
        return obj2;
    }

    static void zzq(zzif zzifVar, Object obj, Object obj2) {
        zzij zzb2 = zzifVar.zzb(obj2);
        if (zzb2.zza.isEmpty()) {
            return;
        }
        zzifVar.zzc(obj).zzh(zzb2);
    }

    static void zzr(zzll zzllVar, Object obj, Object obj2) {
        zzllVar.zzo(obj, zzllVar.zze(zzllVar.zzd(obj), zzllVar.zzd(obj2)));
    }

    public static void zzs(Class cls) {
        Class cls2;
        if (!zzit.class.isAssignableFrom(cls) && (cls2 = zzb) != null && !cls2.isAssignableFrom(cls)) {
            throw new IllegalArgumentException("Message classes must extend GeneratedMessage or GeneratedMessageLite");
        }
    }

    public static void zzt(int i, List list, zzmd zzmdVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmdVar.zzc(i, list, z);
    }

    public static void zzu(int i, List list, zzmd zzmdVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmdVar.zzg(i, list, z);
    }

    public static void zzv(int i, List list, zzmd zzmdVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmdVar.zzj(i, list, z);
    }

    public static void zzw(int i, List list, zzmd zzmdVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmdVar.zzl(i, list, z);
    }

    public static void zzx(int i, List list, zzmd zzmdVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmdVar.zzn(i, list, z);
    }

    public static void zzy(int i, List list, zzmd zzmdVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmdVar.zzp(i, list, z);
    }

    public static void zzz(int i, List list, zzmd zzmdVar, boolean z) {
        if (list == null || list.isEmpty()) {
            return;
        }
        zzmdVar.zzs(i, list, z);
    }
}
