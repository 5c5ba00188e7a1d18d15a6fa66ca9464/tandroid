package com.google.android.gms.internal.wearable;

import com.google.android.exoplayer2.extractor.ts.PsExtractor;
/* compiled from: com.google.android.gms:play-services-wearable@@17.1.0 */
/* loaded from: classes3.dex */
public final class zzel {
    private static final zzei zza;

    static {
        if (zzeg.zza() && zzeg.zzb()) {
            int i = zzah.zza;
        }
        zza = new zzej();
    }

    public static boolean zza(byte[] bArr) {
        return zza.zza(bArr, 0, bArr.length);
    }

    public static boolean zzb(byte[] bArr, int i, int i2) {
        return zza.zza(bArr, i, i2);
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
                                throw new zzek(i2, length2);
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
                    throw new zzek(i6 - 1, length);
                } else if (charAt2 < 55296 || charAt2 > 57343 || ((i3 = i6 + 1) != charSequence.length() && Character.isSurrogatePair(charAt2, charSequence.charAt(i3)))) {
                    StringBuilder sb = new StringBuilder(37);
                    sb.append("Failed writing ");
                    sb.append(charAt2);
                    sb.append(" at index ");
                    sb.append(i7);
                    throw new ArrayIndexOutOfBoundsException(sb.toString());
                } else {
                    throw new zzek(i6, length);
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

    public static String zze(byte[] bArr, int i, int i2) throws zzcc {
        int length = bArr.length;
        if ((i | i2 | ((length - i) - i2)) < 0) {
            throw new ArrayIndexOutOfBoundsException(String.format("buffer length=%d, index=%d, size=%d", Integer.valueOf(length), Integer.valueOf(i), Integer.valueOf(i2)));
        }
        int i3 = i + i2;
        char[] cArr = new char[i2];
        int i4 = 0;
        while (i < i3) {
            byte b = bArr[i];
            if (!zzeh.zza(b)) {
                break;
            }
            i++;
            cArr[i4] = (char) b;
            i4++;
        }
        while (i < i3) {
            int i5 = i + 1;
            byte b2 = bArr[i];
            if (zzeh.zza(b2)) {
                cArr[i4] = (char) b2;
                i = i5;
                i4++;
                while (i < i3) {
                    byte b3 = bArr[i];
                    if (!zzeh.zza(b3)) {
                        break;
                    }
                    i++;
                    cArr[i4] = (char) b3;
                    i4++;
                }
            } else if (b2 < -32) {
                if (i5 < i3) {
                    zzeh.zzb(b2, bArr[i5], cArr, i4);
                    i = i5 + 1;
                    i4++;
                } else {
                    throw zzcc.zzg();
                }
            } else if (b2 < -16) {
                if (i5 < i3 - 1) {
                    int i6 = i5 + 1;
                    zzeh.zzc(b2, bArr[i5], bArr[i6], cArr, i4);
                    i = i6 + 1;
                    i4++;
                } else {
                    throw zzcc.zzg();
                }
            } else if (i5 < i3 - 2) {
                int i7 = i5 + 1;
                int i8 = i7 + 1;
                zzeh.zzd(b2, bArr[i5], bArr[i7], bArr[i8], cArr, i4);
                i4 += 2;
                i = i8 + 1;
            } else {
                throw zzcc.zzg();
            }
        }
        return new String(cArr, 0, i4);
    }

    public static /* synthetic */ int zzf(byte[] bArr, int i, int i2) {
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
