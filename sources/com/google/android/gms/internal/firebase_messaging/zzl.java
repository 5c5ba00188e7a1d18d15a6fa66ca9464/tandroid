package com.google.android.gms.internal.firebase_messaging;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.Queue;
import org.telegram.messenger.LiteMode;
import org.telegram.tgnet.ConnectionsManager;

/* loaded from: classes.dex */
public abstract class zzl {
    private static final OutputStream zza = new zzj();

    public static byte[] zza(InputStream inputStream) {
        ArrayDeque arrayDeque = new ArrayDeque(20);
        int i = LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM;
        int i2 = 0;
        while (i2 < 2147483639) {
            int min = Math.min(i, 2147483639 - i2);
            byte[] bArr = new byte[min];
            arrayDeque.add(bArr);
            int i3 = 0;
            while (i3 < min) {
                int read = inputStream.read(bArr, i3, min - i3);
                if (read == -1) {
                    return zzc(arrayDeque, i2);
                }
                i3 += read;
                i2 += read;
            }
            long j = i;
            long j2 = j + j;
            i = j2 > 2147483647L ? ConnectionsManager.DEFAULT_DATACENTER_ID : j2 < -2147483648L ? Integer.MIN_VALUE : (int) j2;
        }
        if (inputStream.read() == -1) {
            return zzc(arrayDeque, 2147483639);
        }
        throw new OutOfMemoryError("input is too large to fit in a byte array");
    }

    public static InputStream zzb(InputStream inputStream, long j) {
        return new zzk(inputStream, 1048577L);
    }

    private static byte[] zzc(Queue queue, int i) {
        byte[] bArr = new byte[i];
        int i2 = i;
        while (i2 > 0) {
            byte[] bArr2 = (byte[]) queue.remove();
            int min = Math.min(i2, bArr2.length);
            System.arraycopy(bArr2, 0, bArr, i - i2, min);
            i2 -= min;
        }
        return bArr;
    }
}
