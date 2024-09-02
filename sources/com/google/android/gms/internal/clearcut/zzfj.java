package com.google.android.gms.internal.clearcut;

import java.nio.ByteBuffer;
import org.telegram.messenger.NotificationCenter;
/* loaded from: classes.dex */
final class zzfj extends zzfg {
    private static int zza(byte[] bArr, int i, long j, int i2) {
        int zzam;
        int zzp;
        int zzd;
        if (i2 == 0) {
            zzam = zzff.zzam(i);
            return zzam;
        } else if (i2 == 1) {
            zzp = zzff.zzp(i, zzfd.zza(bArr, j));
            return zzp;
        } else if (i2 == 2) {
            zzd = zzff.zzd(i, zzfd.zza(bArr, j), zzfd.zza(bArr, j + 1));
            return zzd;
        } else {
            throw new AssertionError();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x0060, code lost:
        return -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00c2, code lost:
        return -1;
     */
    @Override // com.google.android.gms.internal.clearcut.zzfg
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    final int zzb(int i, byte[] bArr, int i2, int i3) {
        int i4;
        int i5 = 2;
        int i6 = 0;
        if ((i2 | i3 | (bArr.length - i3)) >= 0) {
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
                    if (zzfd.zza(bArr, j3) < 0) {
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
                    b = zzfd.zza(bArr, j5);
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
                            byte zza = zzfd.zza(bArr, j5);
                            if (zza > -65 || (((b << 28) + (zza + 112)) >> 30) != 0) {
                                break;
                            }
                            long j8 = j5 + 2;
                            if (zzfd.zza(bArr, j7) > -65) {
                                break;
                            }
                            j5 += 3;
                            if (zzfd.zza(bArr, j8) > -65) {
                                break;
                            }
                        } else {
                            return zza(bArr, b, j5, i9);
                        }
                    } else if (i9 >= i5) {
                        i8 -= 3;
                        long j9 = j5 + j2;
                        byte zza2 = zzfd.zza(bArr, j5);
                        if (zza2 > -65 || ((b == -32 && zza2 < -96) || (b == -19 && zza2 >= -96))) {
                            break;
                        }
                        j5 += 2;
                        if (zzfd.zza(bArr, j9) > -65) {
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
                    if (zzfd.zza(bArr, j5) > -65) {
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
        throw new ArrayIndexOutOfBoundsException(String.format("Array length=%d, index=%d, limit=%d", Integer.valueOf(bArr.length), Integer.valueOf(i2), Integer.valueOf(i3)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.clearcut.zzfg
    public final int zzb(CharSequence charSequence, byte[] bArr, int i, int i2) {
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
            zzfd.zza(bArr, j4, (byte) charAt);
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
                            if (55296 > charAt3 || charAt3 > 57343 || ((i3 = i4 + 1) != length && Character.isSurrogatePair(charAt3, charSequence.charAt(i3)))) {
                                StringBuilder sb2 = new StringBuilder(46);
                                sb2.append(str2);
                                sb2.append(charAt3);
                                sb2.append(str);
                                sb2.append(j4);
                                throw new ArrayIndexOutOfBoundsException(sb2.toString());
                            }
                            throw new zzfi(i4, length);
                        }
                        int i5 = i4 + 1;
                        if (i5 != length) {
                            char charAt4 = charSequence.charAt(i5);
                            if (Character.isSurrogatePair(charAt3, charAt4)) {
                                int codePoint = Character.toCodePoint(charAt3, charAt4);
                                j2 = 1;
                                zzfd.zza(bArr, j4, (byte) ((codePoint >>> 18) | NotificationCenter.locationPermissionDenied));
                                j3 = j5;
                                zzfd.zza(bArr, j4 + 1, (byte) (((codePoint >>> 12) & 63) | 128));
                                long j6 = 3 + j4;
                                zzfd.zza(bArr, j4 + 2, (byte) (((codePoint >>> 6) & 63) | 128));
                                j4 += 4;
                                zzfd.zza(bArr, j6, (byte) ((codePoint & 63) | 128));
                                i4 = i5;
                            } else {
                                i4 = i5;
                            }
                        }
                        throw new zzfi(i4 - 1, length);
                    }
                    zzfd.zza(bArr, j4, (byte) ((charAt3 >>> '\f') | 480));
                    long j7 = j4 + 2;
                    zzfd.zza(bArr, j4 + 1, (byte) (((charAt3 >>> 6) & 63) | 128));
                    j4 += 3;
                    zzfd.zza(bArr, j7, (byte) ((charAt3 & '?') | 128));
                } else {
                    str = str3;
                    str2 = str4;
                    long j8 = j4 + j;
                    zzfd.zza(bArr, j4, (byte) ((charAt3 >>> 6) | 960));
                    j4 += 2;
                    zzfd.zza(bArr, j8, (byte) ((charAt3 & '?') | 128));
                }
                j3 = j5;
                j2 = 1;
            } else {
                zzfd.zza(bArr, j4, (byte) charAt3);
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

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.clearcut.zzfg
    public final void zzb(CharSequence charSequence, ByteBuffer byteBuffer) {
        long j;
        char c;
        int i;
        long j2;
        long j3;
        int i2;
        long j4;
        int i3;
        char charAt;
        ByteBuffer byteBuffer2 = byteBuffer;
        long zzb = zzfd.zzb(byteBuffer);
        long position = byteBuffer.position() + zzb;
        long limit = byteBuffer.limit() + zzb;
        int length = charSequence.length();
        if (length > limit - position) {
            char charAt2 = charSequence.charAt(length - 1);
            int limit2 = byteBuffer.limit();
            StringBuilder sb = new StringBuilder(37);
            sb.append("Failed writing ");
            sb.append(charAt2);
            sb.append(" at index ");
            sb.append(limit2);
            throw new ArrayIndexOutOfBoundsException(sb.toString());
        }
        int i4 = 0;
        while (true) {
            j = 1;
            c = 128;
            if (i4 >= length || (charAt = charSequence.charAt(i4)) >= 128) {
                break;
            }
            zzfd.zza(position, (byte) charAt);
            i4++;
            position = 1 + position;
        }
        if (i4 == length) {
            i = (int) (position - zzb);
        } else {
            while (i4 < length) {
                char charAt3 = charSequence.charAt(i4);
                if (charAt3 < c && position < limit) {
                    zzfd.zza(position, (byte) charAt3);
                    j2 = zzb;
                    j3 = limit;
                    i2 = i4;
                    j4 = j;
                    position += j;
                } else if (charAt3 >= 2048 || position > limit - 2) {
                    j2 = zzb;
                    if ((charAt3 >= 55296 && 57343 >= charAt3) || position > limit - 3) {
                        if (position > limit - 4) {
                            if (55296 <= charAt3 && charAt3 <= 57343 && ((i3 = i4 + 1) == length || !Character.isSurrogatePair(charAt3, charSequence.charAt(i3)))) {
                                throw new zzfi(i4, length);
                            }
                            StringBuilder sb2 = new StringBuilder(46);
                            sb2.append("Failed writing ");
                            sb2.append(charAt3);
                            sb2.append(" at index ");
                            sb2.append(position);
                            throw new ArrayIndexOutOfBoundsException(sb2.toString());
                        }
                        i2 = i4 + 1;
                        if (i2 != length) {
                            char charAt4 = charSequence.charAt(i2);
                            if (Character.isSurrogatePair(charAt3, charAt4)) {
                                int codePoint = Character.toCodePoint(charAt3, charAt4);
                                j4 = 1;
                                zzfd.zza(position, (byte) ((codePoint >>> 18) | NotificationCenter.locationPermissionDenied));
                                j3 = limit;
                                zzfd.zza(position + 1, (byte) (((codePoint >>> 12) & 63) | 128));
                                long j5 = position + 3;
                                zzfd.zza(position + 2, (byte) (((codePoint >>> 6) & 63) | 128));
                                position += 4;
                                zzfd.zza(j5, (byte) ((codePoint & 63) | 128));
                            } else {
                                i4 = i2;
                            }
                        }
                        throw new zzfi(i4 - 1, length);
                    }
                    long j6 = position + j;
                    zzfd.zza(position, (byte) ((charAt3 >>> '\f') | 480));
                    long j7 = position + 2;
                    zzfd.zza(j6, (byte) (((charAt3 >>> 6) & 63) | 128));
                    position += 3;
                    zzfd.zza(j7, (byte) ((charAt3 & '?') | 128));
                    j3 = limit;
                    i2 = i4;
                    j4 = 1;
                    i4 = i2 + 1;
                    zzb = j2;
                    j = j4;
                    limit = j3;
                    c = 128;
                } else {
                    j2 = zzb;
                    long j8 = position + j;
                    zzfd.zza(position, (byte) ((charAt3 >>> 6) | 960));
                    position += 2;
                    zzfd.zza(j8, (byte) ((charAt3 & '?') | 128));
                    j3 = limit;
                    i2 = i4;
                    j4 = j;
                }
                i4 = i2 + 1;
                zzb = j2;
                j = j4;
                limit = j3;
                c = 128;
            }
            i = (int) (position - zzb);
            byteBuffer2 = byteBuffer;
        }
        byteBuffer2.position(i);
    }
}
