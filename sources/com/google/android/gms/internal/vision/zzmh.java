package com.google.android.gms.internal.vision;

import org.telegram.messenger.NotificationCenter;
/* loaded from: classes.dex */
final class zzmh extends zzme {
    @Override // com.google.android.gms.internal.vision.zzme
    final int zza(int i, byte[] bArr, int i2, int i3) {
        int zzd;
        int zzd2;
        while (i2 < i3 && bArr[i2] >= 0) {
            i2++;
        }
        if (i2 >= i3) {
            return 0;
        }
        while (i2 < i3) {
            int i4 = i2 + 1;
            byte b = bArr[i2];
            if (b < 0) {
                if (b < -32) {
                    if (i4 >= i3) {
                        return b;
                    }
                    if (b >= -62) {
                        i2 += 2;
                        if (bArr[i4] > -65) {
                        }
                    }
                    return -1;
                } else if (b >= -16) {
                    if (i4 >= i3 - 2) {
                        zzd2 = zzmd.zzd(bArr, i4, i3);
                        return zzd2;
                    }
                    int i5 = i2 + 2;
                    byte b2 = bArr[i4];
                    if (b2 <= -65 && (((b << 28) + (b2 + 112)) >> 30) == 0) {
                        int i6 = i2 + 3;
                        if (bArr[i5] <= -65) {
                            i2 += 4;
                            if (bArr[i6] > -65) {
                            }
                        }
                    }
                    return -1;
                } else if (i4 >= i3 - 1) {
                    zzd = zzmd.zzd(bArr, i4, i3);
                    return zzd;
                } else {
                    int i7 = i2 + 2;
                    byte b3 = bArr[i4];
                    if (b3 <= -65 && ((b != -32 || b3 >= -96) && (b != -19 || b3 < -96))) {
                        i2 += 3;
                        if (bArr[i7] > -65) {
                        }
                    }
                    return -1;
                }
            }
            i2 = i4;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x001d, code lost:
        return r10 + r0;
     */
    @Override // com.google.android.gms.internal.vision.zzme
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final int zza(CharSequence charSequence, byte[] bArr, int i, int i2) {
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
                i7 += 2;
                bArr[i8] = (byte) ((charAt2 & '?') | 128);
            } else if ((charAt2 >= 55296 && 57343 >= charAt2) || i7 > i5 - 3) {
                if (i7 > i5 - 4) {
                    if (55296 > charAt2 || charAt2 > 57343 || ((i3 = i6 + 1) != charSequence.length() && Character.isSurrogatePair(charAt2, charSequence.charAt(i3)))) {
                        StringBuilder sb = new StringBuilder(37);
                        sb.append("Failed writing ");
                        sb.append(charAt2);
                        sb.append(" at index ");
                        sb.append(i7);
                        throw new ArrayIndexOutOfBoundsException(sb.toString());
                    }
                    throw new zzmg(i6, length);
                }
                int i9 = i6 + 1;
                if (i9 != charSequence.length()) {
                    char charAt3 = charSequence.charAt(i9);
                    if (Character.isSurrogatePair(charAt2, charAt3)) {
                        int codePoint = Character.toCodePoint(charAt2, charAt3);
                        bArr[i7] = (byte) ((codePoint >>> 18) | NotificationCenter.goingToPreviewTheme);
                        bArr[i7 + 1] = (byte) (((codePoint >>> 12) & 63) | 128);
                        int i10 = i7 + 3;
                        bArr[i7 + 2] = (byte) (((codePoint >>> 6) & 63) | 128);
                        i7 += 4;
                        bArr[i10] = (byte) ((codePoint & 63) | 128);
                        i6 = i9;
                    } else {
                        i6 = i9;
                    }
                }
                throw new zzmg(i6 - 1, length);
            } else {
                bArr[i7] = (byte) ((charAt2 >>> '\f') | 480);
                int i11 = i7 + 2;
                bArr[i7 + 1] = (byte) (((charAt2 >>> 6) & 63) | 128);
                i7 += 3;
                bArr[i11] = (byte) ((charAt2 & '?') | 128);
            }
            i6++;
        }
        return i7;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.vision.zzme
    public final String zzb(byte[] bArr, int i, int i2) {
        boolean zzd;
        boolean zzd2;
        boolean zze;
        boolean zzf;
        boolean zzd3;
        if ((i | i2 | ((bArr.length - i) - i2)) >= 0) {
            int i3 = i + i2;
            char[] cArr = new char[i2];
            int i4 = 0;
            while (i < i3) {
                byte b = bArr[i];
                zzd3 = zzmf.zzd(b);
                if (!zzd3) {
                    break;
                }
                i++;
                zzmf.zzb(b, cArr, i4);
                i4++;
            }
            int i5 = i4;
            while (i < i3) {
                int i6 = i + 1;
                byte b2 = bArr[i];
                zzd = zzmf.zzd(b2);
                if (zzd) {
                    int i7 = i5 + 1;
                    zzmf.zzb(b2, cArr, i5);
                    while (i6 < i3) {
                        byte b3 = bArr[i6];
                        zzd2 = zzmf.zzd(b3);
                        if (!zzd2) {
                            break;
                        }
                        i6++;
                        zzmf.zzb(b3, cArr, i7);
                        i7++;
                    }
                    i5 = i7;
                    i = i6;
                } else {
                    zze = zzmf.zze(b2);
                    if (!zze) {
                        zzf = zzmf.zzf(b2);
                        if (zzf) {
                            if (i6 >= i3 - 1) {
                                throw zzjk.zzh();
                            }
                            int i8 = i + 2;
                            i += 3;
                            zzmf.zzb(b2, bArr[i6], bArr[i8], cArr, i5);
                            i5++;
                        } else if (i6 >= i3 - 2) {
                            throw zzjk.zzh();
                        } else {
                            byte b4 = bArr[i6];
                            int i9 = i + 3;
                            i += 4;
                            zzmf.zzb(b2, b4, bArr[i + 2], bArr[i9], cArr, i5);
                            i5 += 2;
                        }
                    } else if (i6 >= i3) {
                        throw zzjk.zzh();
                    } else {
                        i += 2;
                        zzmf.zzb(b2, bArr[i6], cArr, i5);
                        i5++;
                    }
                }
            }
            return new String(cArr, 0, i5);
        }
        throw new ArrayIndexOutOfBoundsException(String.format("buffer length=%d, index=%d, size=%d", Integer.valueOf(bArr.length), Integer.valueOf(i), Integer.valueOf(i2)));
    }
}
