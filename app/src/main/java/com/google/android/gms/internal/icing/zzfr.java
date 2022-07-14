package com.google.android.gms.internal.icing;

import com.google.android.exoplayer2.extractor.ts.PsExtractor;
/* compiled from: com.google.firebase:firebase-appindexing@@20.0.0 */
/* loaded from: classes3.dex */
public final class zzfr {
    private static final zzfo zza;

    static {
        if (zzfn.zza() && zzfn.zzb()) {
            int i = zzbu.zza;
        }
        zza = new zzfp();
    }

    public static boolean zza(byte[] bArr) {
        return zza.zza(bArr, 0, bArr.length);
    }

    public static boolean zzb(byte[] bArr, int i, int i2) {
        return zza.zza(bArr, 0, i2);
    }

    public static int zzc(CharSequence charSequence) {
        int length = charSequence.length();
        int i = 0;
        int i2 = 0;
        while (i2 < length && charSequence.charAt(i2) < 128) {
            i2++;
        }
        int i3 = length;
        while (true) {
            if (i2 >= length) {
                break;
            }
            char charAt = charSequence.charAt(i2);
            if (charAt < 2048) {
                i3 += (127 - charAt) >>> 31;
                i2++;
            } else {
                int length2 = charSequence.length();
                while (i2 < length2) {
                    char charAt2 = charSequence.charAt(i2);
                    if (charAt2 < 2048) {
                        i += (127 - charAt2) >>> 31;
                    } else {
                        i += 2;
                        if (charAt2 >= 55296 && charAt2 <= 57343) {
                            if (Character.codePointAt(charSequence, i2) < 65536) {
                                throw new zzfq(i2, length2);
                            }
                            i2++;
                        }
                    }
                    i2++;
                }
                i3 += i;
            }
        }
        if (i3 >= length) {
            return i3;
        }
        StringBuilder sb = new StringBuilder(54);
        sb.append("UTF-8 length does not fit in int: ");
        sb.append(i3 + 4294967296L);
        throw new IllegalArgumentException(sb.toString());
    }

    /* JADX WARN: Code restructure failed: missing block: B:50:0x0104, code lost:
        return r9 + r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static int zzd(CharSequence charSequence, byte[] bArr, int i, int i2) {
        int i3;
        int i4;
        char charAt;
        int length = charSequence.length();
        int i5 = i2 + i;
        int i6 = 0;
        while (i6 < length && (i4 = i6 + i) < i5 && (charAt = charSequence.charAt(i6)) < 128) {
            bArr[i4] = (byte) charAt;
            i6++;
        }
        int i7 = i + i6;
        while (i6 < length) {
            char charAt2 = charSequence.charAt(i6);
            if (charAt2 < 128 && i7 < i5) {
                bArr[i7] = (byte) charAt2;
                i7++;
            } else if (charAt2 < 2048 && i7 <= i5 - 2) {
                int i8 = i7 + 1;
                bArr[i7] = (byte) ((charAt2 >>> 6) | 960);
                i7 = i8 + 1;
                bArr[i8] = (byte) ((charAt2 & '?') | 128);
            } else if ((charAt2 >= 55296 && charAt2 <= 57343) || i7 > i5 - 3) {
                if (i7 <= i5 - 4) {
                    int i9 = i6 + 1;
                    if (i9 != charSequence.length()) {
                        char charAt3 = charSequence.charAt(i9);
                        if (!Character.isSurrogatePair(charAt2, charAt3)) {
                            i6 = i9;
                        } else {
                            int codePoint = Character.toCodePoint(charAt2, charAt3);
                            int i10 = i7 + 1;
                            bArr[i7] = (byte) ((codePoint >>> 18) | PsExtractor.VIDEO_STREAM_MASK);
                            int i11 = i10 + 1;
                            bArr[i10] = (byte) (((codePoint >>> 12) & 63) | 128);
                            int i12 = i11 + 1;
                            bArr[i11] = (byte) (((codePoint >>> 6) & 63) | 128);
                            i7 = i12 + 1;
                            bArr[i12] = (byte) ((codePoint & 63) | 128);
                            i6 = i9;
                        }
                    }
                    throw new zzfq(i6 - 1, length);
                } else if (charAt2 < 55296 || charAt2 > 57343 || ((i3 = i6 + 1) != charSequence.length() && Character.isSurrogatePair(charAt2, charSequence.charAt(i3)))) {
                    StringBuilder sb = new StringBuilder(37);
                    sb.append("Failed writing ");
                    sb.append(charAt2);
                    sb.append(" at index ");
                    sb.append(i7);
                    throw new ArrayIndexOutOfBoundsException(sb.toString());
                } else {
                    throw new zzfq(i6, length);
                }
            } else {
                int i13 = i7 + 1;
                bArr[i7] = (byte) ((charAt2 >>> '\f') | 480);
                int i14 = i13 + 1;
                bArr[i13] = (byte) (((charAt2 >>> 6) & 63) | 128);
                bArr[i14] = (byte) ((charAt2 & '?') | 128);
                i7 = i14 + 1;
            }
            i6++;
        }
        return i7;
    }

    public static /* synthetic */ int zze(byte[] bArr, int i, int i2) {
        byte b = bArr[i - 1];
        switch (i2 - i) {
            case 0:
                if (b <= -12) {
                    return b;
                }
                break;
            case 1:
                byte b2 = bArr[i];
                if (b <= -12 && b2 <= -65) {
                    return b ^ (b2 << 8);
                }
                return -1;
            case 2:
                byte b3 = bArr[i];
                byte b4 = bArr[i + 1];
                if (b <= -12 && b3 <= -65 && b4 <= -65) {
                    return ((b3 << 8) ^ b) ^ (b4 << 16);
                }
                break;
            default:
                throw new AssertionError();
        }
        return -1;
    }
}
