package com.google.android.gms.internal.play_billing;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Objects;
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
public final class zzcg {
    static final Charset zzb;
    public static final byte[] zzd;

    static {
        Charset.forName("US-ASCII");
        zzb = Charset.forName("UTF-8");
        Charset.forName("ISO-8859-1");
        byte[] bArr = new byte[0];
        zzd = bArr;
        ByteBuffer.wrap(bArr);
        int i = zzbe.zza;
        try {
            new zzbc(bArr, 0, 0, false, null).zza(0);
        } catch (zzci e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static int zza(boolean z) {
        return z ? 1231 : 1237;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzb(int i, byte[] bArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            i = (i * 31) + bArr[i4];
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object zzc(Object obj, String str) {
        Objects.requireNonNull(obj, "messageType");
        return obj;
    }

    public static String zzd(byte[] bArr) {
        return new String(bArr, zzb);
    }
}
