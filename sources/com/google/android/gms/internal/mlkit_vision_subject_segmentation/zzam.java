package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

/* loaded from: classes.dex */
abstract class zzam {
    static int zza(int i) {
        return (i < 32 ? 4 : 2) * (i + 1);
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x002b, code lost:
    
        if (r5 != (-1)) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x002d, code lost:
    
        zze(r11, r1, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0039, code lost:
    
        return r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0031, code lost:
    
        r12[r5] = (r12[r5] & r4) | (r7 & r10);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    static int zzb(Object obj, Object obj2, int i, Object obj3, int[] iArr, Object[] objArr, Object[] objArr2) {
        int zza = zzan.zza(obj);
        int i2 = zza & i;
        int zzc = zzc(obj3, i2);
        if (zzc != 0) {
            int i3 = i ^ (-1);
            int i4 = zza & i3;
            int i5 = -1;
            while (true) {
                int i6 = zzc - 1;
                int i7 = iArr[i6];
                int i8 = i7 & i;
                if ((i7 & i3) != i4 || !zze.zza(obj, objArr[i6]) || (objArr2 != null && !zze.zza(obj2, objArr2[i6]))) {
                    if (i8 == 0) {
                        break;
                    }
                    i5 = i6;
                    zzc = i8;
                }
            }
        }
        return -1;
    }

    static int zzc(Object obj, int i) {
        return obj instanceof byte[] ? ((byte[]) obj)[i] & 255 : obj instanceof short[] ? (char) ((short[]) obj)[i] : ((int[]) obj)[i];
    }

    static Object zzd(int i) {
        if (i >= 2 && i <= 1073741824 && Integer.highestOneBit(i) == i) {
            return i <= 256 ? new byte[i] : i <= 65536 ? new short[i] : new int[i];
        }
        throw new IllegalArgumentException("must be power of 2 between 2^1 and 2^30: " + i);
    }

    static void zze(Object obj, int i, int i2) {
        if (obj instanceof byte[]) {
            ((byte[]) obj)[i] = (byte) i2;
        } else if (obj instanceof short[]) {
            ((short[]) obj)[i] = (short) i2;
        } else {
            ((int[]) obj)[i] = i2;
        }
    }
}
