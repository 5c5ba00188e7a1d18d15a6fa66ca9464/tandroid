package com.google.android.gms.internal.clearcut;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Objects;
/* loaded from: classes.dex */
public final class zzci {
    static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final byte[] zzkt;

    static {
        Charset.forName("ISO-8859-1");
        byte[] bArr = new byte[0];
        zzkt = bArr;
        ByteBuffer.wrap(bArr);
        zzbk.zza(bArr, 0, bArr.length, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> T checkNotNull(T t) {
        Objects.requireNonNull(t);
        return t;
    }

    public static int hashCode(byte[] bArr) {
        int length = bArr.length;
        int zza = zza(length, bArr, 0, length);
        if (zza == 0) {
            return 1;
        }
        return zza;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(int i, byte[] bArr, int i2, int i3) {
        for (int i4 = i2; i4 < i2 + i3; i4++) {
            i = (i * 31) + bArr[i4];
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object zza(Object obj, Object obj2) {
        return ((zzdo) obj).zzbc().zza((zzdo) obj2).zzbi();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> T zza(T t, String str) {
        Objects.requireNonNull(t, str);
        return t;
    }

    public static int zzc(boolean z) {
        return z ? 1231 : 1237;
    }

    public static boolean zze(byte[] bArr) {
        return zzff.zze(bArr);
    }

    public static String zzf(byte[] bArr) {
        return new String(bArr, UTF_8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean zzf(zzdo zzdoVar) {
        return false;
    }

    public static int zzl(long j) {
        return (int) (j ^ (j >>> 32));
    }
}
