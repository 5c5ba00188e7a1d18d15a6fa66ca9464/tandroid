package com.google.android.gms.internal.mlkit_language_id;

import org.telegram.messenger.NotificationCenter;

/* loaded from: classes.dex */
final class zzht extends zzho {
    private static int zza(byte[] bArr, int i, long j, int i2) {
        int zzb;
        int zzb2;
        int zzb3;
        if (i2 == 0) {
            zzb = zzhp.zzb(i);
            return zzb;
        }
        if (i2 == 1) {
            zzb2 = zzhp.zzb(i, zzhn.zza(bArr, j));
            return zzb2;
        }
        if (i2 != 2) {
            throw new AssertionError();
        }
        zzb3 = zzhp.zzb(i, zzhn.zza(bArr, j), zzhn.zza(bArr, j + 1));
        return zzb3;
    }

    /* JADX WARN: Code restructure failed: missing block: B:56:0x00c2, code lost:
    
        return -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0060, code lost:
    
        return -1;
     */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzho
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    final int zza(int i, byte[] bArr, int i2, int i3) {
        int i4;
        int i5 = 2;
        int i6 = 0;
        if ((i2 | i3 | (bArr.length - i3)) < 0) {
            throw new ArrayIndexOutOfBoundsException(String.format("Array length=%d, index=%d, limit=%d", Integer.valueOf(bArr.length), Integer.valueOf(i2), Integer.valueOf(i3)));
        }
        long j = i2;
        int i7 = (int) (i3 - j);
        long j2 = 1;
        if (i7 >= 16) {
            long j3 = j;
            i4 = 0;
            while (true) {
                if (i4 >= i7) {
                    i4 = i7;
                    break;
                }
                long j4 = j3 + 1;
                if (zzhn.zza(bArr, j3) < 0) {
                    break;
                }
                i4++;
                j3 = j4;
            }
        } else {
            i4 = 0;
        }
        int i8 = i7 - i4;
        long j5 = j + i4;
        while (true) {
            byte b = 0;
            while (true) {
                if (i8 <= 0) {
                    break;
                }
                long j6 = j5 + j2;
                b = zzhn.zza(bArr, j5);
                if (b < 0) {
                    j5 = j6;
                    break;
                }
                i8--;
                j5 = j6;
            }
            if (i8 == 0) {
                return i6;
            }
            int i9 = i8 - 1;
            if (b >= -32) {
                if (b >= -16) {
                    if (i9 >= 3) {
                        i8 -= 4;
                        long j7 = j5 + j2;
                        byte zza = zzhn.zza(bArr, j5);
                        if (zza > -65 || (((b << 28) + (zza + 112)) >> 30) != 0) {
                            break;
                        }
                        long j8 = j5 + 2;
                        if (zzhn.zza(bArr, j7) > -65) {
                            break;
                        }
                        j5 += 3;
                        if (zzhn.zza(bArr, j8) > -65) {
                            break;
                        }
                    } else {
                        return zza(bArr, b, j5, i9);
                    }
                } else if (i9 >= i5) {
                    i8 -= 3;
                    long j9 = j5 + j2;
                    byte zza2 = zzhn.zza(bArr, j5);
                    if (zza2 > -65 || ((b == -32 && zza2 < -96) || (b == -19 && zza2 >= -96))) {
                        break;
                    }
                    j5 += 2;
                    if (zzhn.zza(bArr, j9) > -65) {
                        break;
                    }
                    i5 = 2;
                    i6 = 0;
                } else {
                    return zza(bArr, b, j5, i9);
                }
            } else if (i9 != 0) {
                i8 -= 2;
                if (b < -62) {
                    break;
                }
                long j10 = j5 + j2;
                if (zzhn.zza(bArr, j5) > -65) {
                    break;
                }
                j5 = j10;
            } else {
                return b;
            }
            i5 = 2;
            i6 = 0;
            j2 = 1;
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzho
    public final int zza(CharSequence charSequence, byte[] bArr, int i, int i2) {
        long j;
        String str;
        String str2;
        int i3;
        long j2;
        long j3;
        char charAt;
        long j4 = i;
        long j5 = i2 + j4;
        int length = charSequence.length();
        String str3 = " at index ";
        String str4 = "Failed writing ";
        if (length > i2 || bArr.length - i2 < i) {
            char charAt2 = charSequence.charAt(length - 1);
            StringBuilder sb = new StringBuilder(37);
            sb.append("Failed writing ");
            sb.append(charAt2);
            sb.append(" at index ");
            sb.append(i + i2);
            throw new ArrayIndexOutOfBoundsException(sb.toString());
        }
        int i4 = 0;
        while (true) {
            j = 1;
            if (i4 >= length || (charAt = charSequence.charAt(i4)) >= 128) {
                break;
            }
            zzhn.zza(bArr, j4, (byte) charAt);
            i4++;
            j4 = 1 + j4;
        }
        if (i4 == length) {
            return (int) j4;
        }
        while (i4 < length) {
            char charAt3 = charSequence.charAt(i4);
            if (charAt3 >= 128 || j4 >= j5) {
                if (charAt3 >= 2048 || j4 > j5 - 2) {
                    str = str3;
                    str2 = str4;
                    if ((charAt3 >= 55296 && 57343 >= charAt3) || j4 > j5 - 3) {
                        if (j4 > j5 - 4) {
                            if (55296 <= charAt3 && charAt3 <= 57343 && ((i3 = i4 + 1) == length || !Character.isSurrogatePair(charAt3, charSequence.charAt(i3)))) {
                                throw new zzhq(i4, length);
                            }
                            StringBuilder sb2 = new StringBuilder(46);
                            sb2.append(str2);
                            sb2.append(charAt3);
                            sb2.append(str);
                            sb2.append(j4);
                            throw new ArrayIndexOutOfBoundsException(sb2.toString());
                        }
                        int i5 = i4 + 1;
                        if (i5 != length) {
                            char charAt4 = charSequence.charAt(i5);
                            if (Character.isSurrogatePair(charAt3, charAt4)) {
                                int codePoint = Character.toCodePoint(charAt3, charAt4);
                                j2 = 1;
                                zzhn.zza(bArr, j4, (byte) ((codePoint >>> 18) | NotificationCenter.needShareTheme));
                                j3 = j5;
                                zzhn.zza(bArr, j4 + 1, (byte) (((codePoint >>> 12) & 63) | 128));
                                long j6 = 3 + j4;
                                zzhn.zza(bArr, j4 + 2, (byte) (((codePoint >>> 6) & 63) | 128));
                                j4 += 4;
                                zzhn.zza(bArr, j6, (byte) ((codePoint & 63) | 128));
                                i4 = i5;
                            } else {
                                i4 = i5;
                            }
                        }
                        throw new zzhq(i4 - 1, length);
                    }
                    zzhn.zza(bArr, j4, (byte) ((charAt3 >>> '\f') | 480));
                    long j7 = j4 + 2;
                    zzhn.zza(bArr, j4 + 1, (byte) (((charAt3 >>> 6) & 63) | 128));
                    j4 += 3;
                    zzhn.zza(bArr, j7, (byte) ((charAt3 & '?') | 128));
                } else {
                    str = str3;
                    str2 = str4;
                    long j8 = j4 + j;
                    zzhn.zza(bArr, j4, (byte) ((charAt3 >>> 6) | 960));
                    j4 += 2;
                    zzhn.zza(bArr, j8, (byte) ((charAt3 & '?') | 128));
                }
                j3 = j5;
                j2 = 1;
            } else {
                zzhn.zza(bArr, j4, (byte) charAt3);
                j3 = j5;
                str2 = str4;
                j2 = j;
                j4 += j;
                str = str3;
            }
            i4++;
            str3 = str;
            str4 = str2;
            j = j2;
            j5 = j3;
        }
        return (int) j4;
    }
}
