package com.google.android.recaptcha.internal;

import android.util.Base64;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.telegram.messenger.NotificationCenter;

/* loaded from: classes.dex */
public final class zzmh {
    protected static final Charset zza = StandardCharsets.UTF_16;

    protected static int zza(int i, int i2) {
        if (i % 2 != 0) {
            return (i | i2) - (i & i2);
        }
        return ((i ^ (-1)) & i2) | ((i2 ^ (-1)) & i);
    }

    public static String zzb(String str, byte[] bArr, zzmi zzmiVar) {
        int i = 0;
        byte[] decode = Base64.decode(str, 0);
        char c = '\f';
        byte[] bArr2 = new byte[12];
        int length = decode.length - 12;
        byte[] bArr3 = new byte[length];
        System.arraycopy(decode, 0, bArr2, 0, 12);
        System.arraycopy(decode, 12, bArr3, 0, length);
        int[] iArr = {511133343, 1277647508, 107287496, 338123662};
        if (bArr.length != 32) {
            throw new IllegalArgumentException();
        }
        int i2 = 16;
        int[] iArr2 = new int[16];
        for (int i3 = 0; i3 < 4; i3++) {
            iArr2[i3] = zza(iArr[i3], 2131181306);
        }
        for (int i4 = 4; i4 < 12; i4++) {
            iArr2[i4] = zze(bArr, (i4 - 4) * 4);
        }
        iArr2[12] = 1;
        for (int i5 = 13; i5 < 16; i5++) {
            iArr2[i5] = zze(bArr2, (i5 - 13) * 4);
        }
        int[] iArr3 = new int[16];
        System.arraycopy(iArr2, 0, iArr3, 0, 16);
        byte[] bArr4 = new byte[length];
        int i6 = length;
        int i7 = 1;
        int i8 = 0;
        while (i6 > 0) {
            System.arraycopy(iArr3, i, iArr2, i, i2);
            iArr2[c] = i7;
            int i9 = 0;
            while (i9 < 10) {
                int[] iArr4 = iArr3;
                int i10 = i7;
                zzc(0, 4, 8, 12, iArr, bArr, bArr2, i10, iArr2, iArr4);
                zzc(1, 5, 9, 13, iArr, bArr, bArr2, i10, iArr2, iArr4);
                zzc(2, 6, 10, 14, iArr, bArr, bArr2, i10, iArr2, iArr4);
                zzc(3, 7, 11, 15, iArr, bArr, bArr2, i10, iArr2, iArr4);
                zzc(0, 5, 10, 15, iArr, bArr, bArr2, i10, iArr2, iArr4);
                zzc(1, 6, 11, 12, iArr, bArr, bArr2, i10, iArr2, iArr4);
                zzc(2, 7, 8, 13, iArr, bArr, bArr2, i10, iArr2, iArr4);
                zzc(3, 4, 9, 14, iArr, bArr, bArr2, i10, iArr2, iArr4);
                i9++;
                i6 = i6;
                bArr4 = bArr4;
                iArr3 = iArr4;
            }
            int i11 = i6;
            byte[] bArr5 = bArr4;
            int[] iArr5 = iArr3;
            int[] iArr6 = iArr2;
            byte[] bArr6 = new byte[64];
            for (int i12 = 0; i12 < 16; i12++) {
                int i13 = iArr6[i12];
                int i14 = i12 * 4;
                bArr6[i14] = (byte) (i13 & NotificationCenter.liveLocationsChanged);
                bArr6[i14 + 1] = (byte) ((i13 >> 8) & NotificationCenter.liveLocationsChanged);
                bArr6[i14 + 2] = (byte) ((i13 >> 16) & NotificationCenter.liveLocationsChanged);
                bArr6[i14 + 3] = (byte) ((i13 >> 24) & NotificationCenter.liveLocationsChanged);
            }
            for (int i15 = 0; i15 < Math.min(64, i11); i15++) {
                int i16 = i8 + i15;
                bArr5[i16] = (byte) zza(bArr6[i15], bArr3[i16]);
            }
            i7++;
            i6 = i11 - 64;
            i8 += 64;
            bArr4 = bArr5;
            iArr3 = iArr5;
            iArr2 = iArr6;
            i2 = 16;
            i = 0;
            c = '\f';
        }
        return new String(bArr4, zza);
    }

    protected static final void zzc(int i, int i2, int i3, int i4, int[] iArr, byte[] bArr, byte[] bArr2, int i5, int[] iArr2, int[] iArr3) {
        zzd(i, i2, i4, 16, iArr, bArr, bArr2, i5, iArr2, iArr3);
        zzd(i3, i4, i2, 12, iArr, bArr, bArr2, i5, iArr2, iArr3);
        zzd(i, i2, i4, 8, iArr, bArr, bArr2, i5, iArr2, iArr3);
        zzd(i3, i4, i2, 7, iArr, bArr, bArr2, i5, iArr2, iArr3);
    }

    protected static final void zzd(int i, int i2, int i3, int i4, int[] iArr, byte[] bArr, byte[] bArr2, int i5, int[] iArr2, int[] iArr3) {
        int i6 = iArr2[i] + iArr2[i2];
        iArr2[i] = i6;
        int zza2 = zza(iArr2[i3], i6);
        iArr2[i3] = (zza2 << i4) | (zza2 >>> (32 - i4));
    }

    private static final int zze(byte[] bArr, int i) {
        int i2 = bArr[i] & 255;
        int i3 = bArr[i + 1] & 255;
        int i4 = bArr[i + 2] & 255;
        return ((bArr[i + 3] & 255) << 24) | (i3 << 8) | i2 | (i4 << 16);
    }
}
