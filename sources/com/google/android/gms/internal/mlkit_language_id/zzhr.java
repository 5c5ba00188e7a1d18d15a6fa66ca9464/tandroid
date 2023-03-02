package com.google.android.gms.internal.mlkit_language_id;
/* compiled from: com.google.mlkit:language-id@@16.1.1 */
/* loaded from: classes.dex */
final class zzhr extends zzho {
    @Override // com.google.android.gms.internal.mlkit_language_id.zzho
    final int zza(int i, byte[] bArr, int i2, int i3) {
        int zzc;
        int zzc2;
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
                        i2 = i4 + 1;
                        if (bArr[i4] > -65) {
                        }
                    }
                    return -1;
                } else if (b >= -16) {
                    if (i4 < i3 - 2) {
                        int i5 = i4 + 1;
                        byte b2 = bArr[i4];
                        if (b2 <= -65 && (((b << 28) + (b2 + 112)) >> 30) == 0) {
                            int i6 = i5 + 1;
                            if (bArr[i5] <= -65) {
                                i4 = i6 + 1;
                                if (bArr[i6] > -65) {
                                }
                            }
                        }
                        return -1;
                    }
                    zzc2 = zzhp.zzc(bArr, i4, i3);
                    return zzc2;
                } else if (i4 < i3 - 1) {
                    int i7 = i4 + 1;
                    byte b3 = bArr[i4];
                    if (b3 <= -65 && ((b != -32 || b3 >= -96) && (b != -19 || b3 < -96))) {
                        i2 = i7 + 1;
                        if (bArr[i7] > -65) {
                        }
                    }
                    return -1;
                } else {
                    zzc = zzhp.zzc(bArr, i4, i3);
                    return zzc;
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
    @Override // com.google.android.gms.internal.mlkit_language_id.zzho
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final int zza(CharSequence charSequence, byte[] bArr, int i, int i2) {
        int i3;
        int i4;
        int i5;
        char charAt;
        int length = charSequence.length();
        int i6 = i2 + i;
        int i7 = 0;
        while (i7 < length && (i5 = i7 + i) < i6 && (charAt = charSequence.charAt(i7)) < 128) {
            bArr[i5] = (byte) charAt;
            i7++;
        }
        int i8 = i + i7;
        while (i7 < length) {
            char charAt2 = charSequence.charAt(i7);
            if (charAt2 >= 128 || i8 >= i6) {
                if (charAt2 < 2048 && i8 <= i6 - 2) {
                    int i9 = i8 + 1;
                    bArr[i8] = (byte) ((charAt2 >>> 6) | 960);
                    i8 = i9 + 1;
                    bArr[i9] = (byte) ((charAt2 & '?') | 128);
                } else if ((charAt2 >= 55296 && 57343 >= charAt2) || i8 > i6 - 3) {
                    if (i8 <= i6 - 4) {
                        int i10 = i7 + 1;
                        if (i10 != charSequence.length()) {
                            char charAt3 = charSequence.charAt(i10);
                            if (Character.isSurrogatePair(charAt2, charAt3)) {
                                int codePoint = Character.toCodePoint(charAt2, charAt3);
                                int i11 = i8 + 1;
                                bArr[i8] = (byte) ((codePoint >>> 18) | 240);
                                int i12 = i11 + 1;
                                bArr[i11] = (byte) (((codePoint >>> 12) & 63) | 128);
                                int i13 = i12 + 1;
                                bArr[i12] = (byte) (((codePoint >>> 6) & 63) | 128);
                                i8 = i13 + 1;
                                bArr[i13] = (byte) ((codePoint & 63) | 128);
                                i7 = i10;
                            } else {
                                i7 = i10;
                            }
                        }
                        throw new zzhq(i7 - 1, length);
                    } else if (55296 <= charAt2 && charAt2 <= 57343 && ((i4 = i7 + 1) == charSequence.length() || !Character.isSurrogatePair(charAt2, charSequence.charAt(i4)))) {
                        throw new zzhq(i7, length);
                    } else {
                        StringBuilder sb = new StringBuilder(37);
                        sb.append("Failed writing ");
                        sb.append(charAt2);
                        sb.append(" at index ");
                        sb.append(i8);
                        throw new ArrayIndexOutOfBoundsException(sb.toString());
                    }
                } else {
                    int i14 = i8 + 1;
                    bArr[i8] = (byte) ((charAt2 >>> '\f') | 480);
                    int i15 = i14 + 1;
                    bArr[i14] = (byte) (((charAt2 >>> 6) & 63) | 128);
                    i3 = i15 + 1;
                    bArr[i15] = (byte) ((charAt2 & '?') | 128);
                }
                i7++;
            } else {
                i3 = i8 + 1;
                bArr[i8] = (byte) charAt2;
            }
            i8 = i3;
            i7++;
        }
        return i8;
    }
}
