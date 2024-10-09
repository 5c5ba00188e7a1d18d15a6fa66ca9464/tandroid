package com.google.android.gms.internal.mlkit_language_id;

/* loaded from: classes.dex */
public abstract class zzg {
    public static int zza(int i, int i2) {
        String zza;
        if (i >= 0 && i < i2) {
            return i;
        }
        if (i < 0) {
            zza = zzj.zza("%s (%s) must not be negative", "index", Integer.valueOf(i));
        } else {
            if (i2 < 0) {
                StringBuilder sb = new StringBuilder(26);
                sb.append("negative size: ");
                sb.append(i2);
                throw new IllegalArgumentException(sb.toString());
            }
            zza = zzj.zza("%s (%s) must be less than size (%s)", "index", Integer.valueOf(i), Integer.valueOf(i2));
        }
        throw new IndexOutOfBoundsException(zza);
    }

    public static Object zza(Object obj) {
        obj.getClass();
        return obj;
    }

    private static String zza(int i, int i2, String str) {
        if (i < 0) {
            return zzj.zza("%s (%s) must not be negative", str, Integer.valueOf(i));
        }
        if (i2 >= 0) {
            return zzj.zza("%s (%s) must not be greater than size (%s)", str, Integer.valueOf(i), Integer.valueOf(i2));
        }
        StringBuilder sb = new StringBuilder(26);
        sb.append("negative size: ");
        sb.append(i2);
        throw new IllegalArgumentException(sb.toString());
    }

    public static void zza(int i, int i2, int i3) {
        if (i < 0 || i2 < i || i2 > i3) {
            throw new IndexOutOfBoundsException((i < 0 || i > i3) ? zza(i, i3, "start index") : (i2 < 0 || i2 > i3) ? zza(i2, i3, "end index") : zzj.zza("end index (%s) must not be less than start index (%s)", Integer.valueOf(i2), Integer.valueOf(i)));
        }
    }

    public static int zzb(int i, int i2) {
        if (i < 0 || i > i2) {
            throw new IndexOutOfBoundsException(zza(i, i2, "index"));
        }
        return i;
    }
}
