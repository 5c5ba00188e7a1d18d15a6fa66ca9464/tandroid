package com.google.android.gms.internal.clearcut;

import java.nio.ByteBuffer;
import org.telegram.tgnet.ConnectionsManager;
/* loaded from: classes.dex */
final class zzfh extends zzfg {
    @Override // com.google.android.gms.internal.clearcut.zzfg
    final int zzb(int i, byte[] bArr, int i2, int i3) {
        int zzf;
        int zzf2;
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
                    if (i4 >= i3 - 2) {
                        zzf2 = zzff.zzf(bArr, i4, i3);
                        return zzf2;
                    }
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
                } else if (i4 >= i3 - 1) {
                    zzf = zzff.zzf(bArr, i4, i3);
                    return zzf;
                } else {
                    int i7 = i4 + 1;
                    byte b3 = bArr[i4];
                    if (b3 <= -65 && ((b != -32 || b3 >= -96) && (b != -19 || b3 < -96))) {
                        i2 = i7 + 1;
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

    /* JADX WARN: Code restructure failed: missing block: B:12:0x001d, code lost:
        return r10 + r0;
     */
    @Override // com.google.android.gms.internal.clearcut.zzfg
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final int zzb(CharSequence charSequence, byte[] bArr, int i, int i2) {
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
                    bArr[i9] = (byte) ((charAt2 & '?') | ConnectionsManager.RequestFlagNeedQuickAck);
                } else if ((charAt2 >= 55296 && 57343 >= charAt2) || i8 > i6 - 3) {
                    if (i8 > i6 - 4) {
                        if (55296 <= charAt2 && charAt2 <= 57343 && ((i3 = i7 + 1) == charSequence.length() || !Character.isSurrogatePair(charAt2, charSequence.charAt(i3)))) {
                            throw new zzfi(i7, length);
                        }
                        StringBuilder sb = new StringBuilder(37);
                        sb.append("Failed writing ");
                        sb.append(charAt2);
                        sb.append(" at index ");
                        sb.append(i8);
                        throw new ArrayIndexOutOfBoundsException(sb.toString());
                    }
                    int i10 = i7 + 1;
                    if (i10 != charSequence.length()) {
                        char charAt3 = charSequence.charAt(i10);
                        if (Character.isSurrogatePair(charAt2, charAt3)) {
                            int codePoint = Character.toCodePoint(charAt2, charAt3);
                            int i11 = i8 + 1;
                            bArr[i8] = (byte) ((codePoint >>> 18) | 240);
                            int i12 = i11 + 1;
                            bArr[i11] = (byte) (((codePoint >>> 12) & 63) | ConnectionsManager.RequestFlagNeedQuickAck);
                            int i13 = i12 + 1;
                            bArr[i12] = (byte) (((codePoint >>> 6) & 63) | ConnectionsManager.RequestFlagNeedQuickAck);
                            i8 = i13 + 1;
                            bArr[i13] = (byte) ((codePoint & 63) | ConnectionsManager.RequestFlagNeedQuickAck);
                            i7 = i10;
                        } else {
                            i7 = i10;
                        }
                    }
                    throw new zzfi(i7 - 1, length);
                } else {
                    int i14 = i8 + 1;
                    bArr[i8] = (byte) ((charAt2 >>> '\f') | 480);
                    int i15 = i14 + 1;
                    bArr[i14] = (byte) (((charAt2 >>> 6) & 63) | ConnectionsManager.RequestFlagNeedQuickAck);
                    i4 = i15 + 1;
                    bArr[i15] = (byte) ((charAt2 & '?') | ConnectionsManager.RequestFlagNeedQuickAck);
                }
                i7++;
            } else {
                i4 = i8 + 1;
                bArr[i8] = (byte) charAt2;
            }
            i8 = i4;
            i7++;
        }
        return i8;
    }

    @Override // com.google.android.gms.internal.clearcut.zzfg
    public final void zzb(CharSequence charSequence, ByteBuffer byteBuffer) {
        zzfg.zzc(charSequence, byteBuffer);
    }
}
