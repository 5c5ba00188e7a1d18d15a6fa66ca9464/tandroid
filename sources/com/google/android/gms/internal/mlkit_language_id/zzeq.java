package com.google.android.gms.internal.mlkit_language_id;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/* loaded from: classes.dex */
public abstract class zzeq {
    public static final byte[] zzb;
    private static final ByteBuffer zzd;
    private static final zzdz zze;
    static final Charset zza = Charset.forName("UTF-8");
    private static final Charset zzc = Charset.forName("ISO-8859-1");

    static {
        byte[] bArr = new byte[0];
        zzb = bArr;
        zzd = ByteBuffer.wrap(bArr);
        zze = zzdz.zza(bArr, 0, bArr.length, false);
    }

    static int zza(int i, byte[] bArr, int i2, int i3) {
        for (int i4 = i2; i4 < i2 + i3; i4++) {
            i = (i * 31) + bArr[i4];
        }
        return i;
    }

    public static int zza(long j) {
        return (int) (j ^ (j >>> 32));
    }

    public static int zza(boolean z) {
        return z ? 1231 : 1237;
    }

    static Object zza(Object obj) {
        obj.getClass();
        return obj;
    }

    static Object zza(Object obj, Object obj2) {
        return ((zzfz) obj).zzm().zza((zzfz) obj2).zzf();
    }

    static Object zza(Object obj, String str) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(str);
    }

    static boolean zza(zzfz zzfzVar) {
        return false;
    }

    public static boolean zza(byte[] bArr) {
        return zzhp.zza(bArr);
    }

    public static String zzb(byte[] bArr) {
        return new String(bArr, zza);
    }
}
