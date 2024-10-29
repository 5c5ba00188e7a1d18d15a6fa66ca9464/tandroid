package com.google.android.gms.internal.clearcut;

import java.nio.ByteBuffer;
import org.telegram.messenger.NotificationCenter;

/* loaded from: classes.dex */
abstract class zzfg {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void zzc(CharSequence charSequence, ByteBuffer byteBuffer) {
        int i;
        int length = charSequence.length();
        int position = byteBuffer.position();
        int i2 = 0;
        while (i2 < length) {
            try {
                char charAt = charSequence.charAt(i2);
                if (charAt >= 128) {
                    break;
                }
                byteBuffer.put(position + i2, (byte) charAt);
                i2++;
            } catch (IndexOutOfBoundsException unused) {
                int position2 = byteBuffer.position() + Math.max(i2, (position - byteBuffer.position()) + 1);
                char charAt2 = charSequence.charAt(i2);
                StringBuilder sb = new StringBuilder(37);
                sb.append("Failed writing ");
                sb.append(charAt2);
                sb.append(" at index ");
                sb.append(position2);
                throw new ArrayIndexOutOfBoundsException(sb.toString());
            }
        }
        if (i2 == length) {
            byteBuffer.position(position + i2);
            return;
        }
        position += i2;
        while (i2 < length) {
            char charAt3 = charSequence.charAt(i2);
            if (charAt3 < 128) {
                byteBuffer.put(position, (byte) charAt3);
            } else if (charAt3 < 2048) {
                int i3 = position + 1;
                try {
                    byteBuffer.put(position, (byte) ((charAt3 >>> 6) | 192));
                    byteBuffer.put(i3, (byte) ((charAt3 & '?') | 128));
                    position = i3;
                } catch (IndexOutOfBoundsException unused2) {
                    position = i3;
                    int position22 = byteBuffer.position() + Math.max(i2, (position - byteBuffer.position()) + 1);
                    char charAt22 = charSequence.charAt(i2);
                    StringBuilder sb2 = new StringBuilder(37);
                    sb2.append("Failed writing ");
                    sb2.append(charAt22);
                    sb2.append(" at index ");
                    sb2.append(position22);
                    throw new ArrayIndexOutOfBoundsException(sb2.toString());
                }
            } else {
                if (charAt3 >= 55296 && 57343 >= charAt3) {
                    int i4 = i2 + 1;
                    if (i4 != length) {
                        try {
                            char charAt4 = charSequence.charAt(i4);
                            if (Character.isSurrogatePair(charAt3, charAt4)) {
                                int codePoint = Character.toCodePoint(charAt3, charAt4);
                                int i5 = position + 1;
                                try {
                                    byteBuffer.put(position, (byte) ((codePoint >>> 18) | NotificationCenter.needShareTheme));
                                    i = position + 2;
                                } catch (IndexOutOfBoundsException unused3) {
                                    position = i5;
                                    i2 = i4;
                                    int position222 = byteBuffer.position() + Math.max(i2, (position - byteBuffer.position()) + 1);
                                    char charAt222 = charSequence.charAt(i2);
                                    StringBuilder sb22 = new StringBuilder(37);
                                    sb22.append("Failed writing ");
                                    sb22.append(charAt222);
                                    sb22.append(" at index ");
                                    sb22.append(position222);
                                    throw new ArrayIndexOutOfBoundsException(sb22.toString());
                                }
                                try {
                                    byteBuffer.put(i5, (byte) (((codePoint >>> 12) & 63) | 128));
                                    position += 3;
                                    byteBuffer.put(i, (byte) (((codePoint >>> 6) & 63) | 128));
                                    byteBuffer.put(position, (byte) ((codePoint & 63) | 128));
                                    i2 = i4;
                                } catch (IndexOutOfBoundsException unused4) {
                                    i2 = i4;
                                    position = i;
                                    int position2222 = byteBuffer.position() + Math.max(i2, (position - byteBuffer.position()) + 1);
                                    char charAt2222 = charSequence.charAt(i2);
                                    StringBuilder sb222 = new StringBuilder(37);
                                    sb222.append("Failed writing ");
                                    sb222.append(charAt2222);
                                    sb222.append(" at index ");
                                    sb222.append(position2222);
                                    throw new ArrayIndexOutOfBoundsException(sb222.toString());
                                }
                            } else {
                                i2 = i4;
                            }
                        } catch (IndexOutOfBoundsException unused5) {
                        }
                    }
                    throw new zzfi(i2, length);
                }
                int i6 = position + 1;
                byteBuffer.put(position, (byte) ((charAt3 >>> '\f') | NotificationCenter.updateStories));
                position += 2;
                byteBuffer.put(i6, (byte) (((charAt3 >>> 6) & 63) | 128));
                byteBuffer.put(position, (byte) ((charAt3 & '?') | 128));
            }
            i2++;
            position++;
        }
        byteBuffer.position(position);
    }

    abstract int zzb(int i, byte[] bArr, int i2, int i3);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int zzb(CharSequence charSequence, byte[] bArr, int i, int i2);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void zzb(CharSequence charSequence, ByteBuffer byteBuffer);

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean zze(byte[] bArr, int i, int i2) {
        return zzb(0, bArr, i, i2) == 0;
    }
}
