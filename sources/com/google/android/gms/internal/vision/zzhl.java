package com.google.android.gms.internal.vision;

import org.telegram.messenger.NotificationCenter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class zzhl {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(int i, byte[] bArr, int i2, int i3, zzjl zzjlVar, zzhn zzhnVar) {
        zzjd zzjdVar = (zzjd) zzjlVar;
        int zza = zza(bArr, i2, zzhnVar);
        while (true) {
            zzjdVar.zzc(zzhnVar.zza);
            if (zza >= i3) {
                break;
            }
            int zza2 = zza(bArr, zza, zzhnVar);
            if (i != zzhnVar.zza) {
                break;
            }
            zza = zza(bArr, zza2, zzhnVar);
        }
        return zza;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(int i, byte[] bArr, int i2, int i3, zzlx zzlxVar, zzhn zzhnVar) {
        if ((i >>> 3) == 0) {
            throw zzjk.zzd();
        }
        int i4 = i & 7;
        if (i4 == 0) {
            int zzb = zzb(bArr, i2, zzhnVar);
            zzlxVar.zza(i, Long.valueOf(zzhnVar.zzb));
            return zzb;
        }
        if (i4 == 1) {
            zzlxVar.zza(i, Long.valueOf(zzb(bArr, i2)));
            return i2 + 8;
        }
        if (i4 == 2) {
            int zza = zza(bArr, i2, zzhnVar);
            int i5 = zzhnVar.zza;
            if (i5 < 0) {
                throw zzjk.zzb();
            }
            if (i5 > bArr.length - zza) {
                throw zzjk.zza();
            }
            zzlxVar.zza(i, i5 == 0 ? zzht.zza : zzht.zza(bArr, zza, i5));
            return zza + i5;
        }
        if (i4 != 3) {
            if (i4 != 5) {
                throw zzjk.zzd();
            }
            zzlxVar.zza(i, Integer.valueOf(zza(bArr, i2)));
            return i2 + 4;
        }
        zzlx zzb2 = zzlx.zzb();
        int i6 = (i & (-8)) | 4;
        int i7 = 0;
        while (true) {
            if (i2 >= i3) {
                break;
            }
            int zza2 = zza(bArr, i2, zzhnVar);
            int i8 = zzhnVar.zza;
            i7 = i8;
            if (i8 == i6) {
                i2 = zza2;
                break;
            }
            int zza3 = zza(i7, bArr, zza2, i3, zzb2, zzhnVar);
            i7 = i8;
            i2 = zza3;
        }
        if (i2 > i3 || i7 != i6) {
            throw zzjk.zzg();
        }
        zzlxVar.zza(i, zzb2);
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(int i, byte[] bArr, int i2, zzhn zzhnVar) {
        int i3;
        int i4 = i & NotificationCenter.dialogTranslate;
        int i5 = i2 + 1;
        byte b = bArr[i2];
        if (b >= 0) {
            i3 = b << 7;
        } else {
            int i6 = i4 | ((b & Byte.MAX_VALUE) << 7);
            int i7 = i2 + 2;
            byte b2 = bArr[i5];
            if (b2 >= 0) {
                zzhnVar.zza = i6 | (b2 << 14);
                return i7;
            }
            i4 = i6 | ((b2 & Byte.MAX_VALUE) << 14);
            i5 = i2 + 3;
            byte b3 = bArr[i7];
            if (b3 >= 0) {
                i3 = b3 << 21;
            } else {
                int i8 = i4 | ((b3 & Byte.MAX_VALUE) << 21);
                int i9 = i2 + 4;
                byte b4 = bArr[i5];
                if (b4 >= 0) {
                    zzhnVar.zza = i8 | (b4 << 28);
                    return i9;
                }
                int i10 = i8 | ((b4 & Byte.MAX_VALUE) << 28);
                while (true) {
                    int i11 = i9 + 1;
                    if (bArr[i9] >= 0) {
                        zzhnVar.zza = i10;
                        return i11;
                    }
                    i9 = i11;
                }
            }
        }
        zzhnVar.zza = i4 | i3;
        return i5;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(zzlc zzlcVar, int i, byte[] bArr, int i2, int i3, zzjl zzjlVar, zzhn zzhnVar) {
        int zza = zza(zzlcVar, bArr, i2, i3, zzhnVar);
        while (true) {
            zzjlVar.add(zzhnVar.zzc);
            if (zza >= i3) {
                break;
            }
            int zza2 = zza(bArr, zza, zzhnVar);
            if (i != zzhnVar.zza) {
                break;
            }
            zza = zza(zzlcVar, bArr, zza2, i3, zzhnVar);
        }
        return zza;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(zzlc zzlcVar, byte[] bArr, int i, int i2, int i3, zzhn zzhnVar) {
        zzko zzkoVar = (zzko) zzlcVar;
        Object zza = zzkoVar.zza();
        int zza2 = zzkoVar.zza(zza, bArr, i, i2, i3, zzhnVar);
        zzkoVar.zzc(zza);
        zzhnVar.zzc = zza;
        return zza2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(zzlc zzlcVar, byte[] bArr, int i, int i2, zzhn zzhnVar) {
        int i3 = i + 1;
        int i4 = bArr[i];
        if (i4 < 0) {
            i3 = zza(i4, bArr, i3, zzhnVar);
            i4 = zzhnVar.zza;
        }
        int i5 = i3;
        if (i4 < 0 || i4 > i2 - i5) {
            throw zzjk.zza();
        }
        Object zza = zzlcVar.zza();
        int i6 = i4 + i5;
        zzlcVar.zza(zza, bArr, i5, i6, zzhnVar);
        zzlcVar.zzc(zza);
        zzhnVar.zzc = zza;
        return i6;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(byte[] bArr, int i) {
        return ((bArr[i + 3] & 255) << 24) | (bArr[i] & 255) | ((bArr[i + 1] & 255) << 8) | ((bArr[i + 2] & 255) << 16);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(byte[] bArr, int i, zzhn zzhnVar) {
        int i2 = i + 1;
        byte b = bArr[i];
        if (b < 0) {
            return zza(b, bArr, i2, zzhnVar);
        }
        zzhnVar.zza = b;
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(byte[] bArr, int i, zzjl zzjlVar, zzhn zzhnVar) {
        zzjd zzjdVar = (zzjd) zzjlVar;
        int zza = zza(bArr, i, zzhnVar);
        int i2 = zzhnVar.zza + zza;
        while (zza < i2) {
            zza = zza(bArr, zza, zzhnVar);
            zzjdVar.zzc(zzhnVar.zza);
        }
        if (zza == i2) {
            return zza;
        }
        throw zzjk.zza();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzb(byte[] bArr, int i, zzhn zzhnVar) {
        int i2 = i + 1;
        long j = bArr[i];
        if (j >= 0) {
            zzhnVar.zzb = j;
            return i2;
        }
        int i3 = i + 2;
        byte b = bArr[i2];
        long j2 = (j & 127) | ((b & Byte.MAX_VALUE) << 7);
        int i4 = 7;
        while (b < 0) {
            int i5 = i3 + 1;
            i4 += 7;
            j2 |= (r10 & Byte.MAX_VALUE) << i4;
            b = bArr[i3];
            i3 = i5;
        }
        zzhnVar.zzb = j2;
        return i3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long zzb(byte[] bArr, int i) {
        return ((bArr[i + 7] & 255) << 56) | (bArr[i] & 255) | ((bArr[i + 1] & 255) << 8) | ((bArr[i + 2] & 255) << 16) | ((bArr[i + 3] & 255) << 24) | ((bArr[i + 4] & 255) << 32) | ((bArr[i + 5] & 255) << 40) | ((bArr[i + 6] & 255) << 48);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static double zzc(byte[] bArr, int i) {
        return Double.longBitsToDouble(zzb(bArr, i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzc(byte[] bArr, int i, zzhn zzhnVar) {
        int zza = zza(bArr, i, zzhnVar);
        int i2 = zzhnVar.zza;
        if (i2 < 0) {
            throw zzjk.zzb();
        }
        if (i2 == 0) {
            zzhnVar.zzc = "";
            return zza;
        }
        zzhnVar.zzc = new String(bArr, zza, i2, zzjf.zza);
        return zza + i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float zzd(byte[] bArr, int i) {
        return Float.intBitsToFloat(zza(bArr, i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzd(byte[] bArr, int i, zzhn zzhnVar) {
        int zza = zza(bArr, i, zzhnVar);
        int i2 = zzhnVar.zza;
        if (i2 < 0) {
            throw zzjk.zzb();
        }
        if (i2 == 0) {
            zzhnVar.zzc = "";
            return zza;
        }
        zzhnVar.zzc = zzmd.zzb(bArr, zza, i2);
        return zza + i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zze(byte[] bArr, int i, zzhn zzhnVar) {
        int zza = zza(bArr, i, zzhnVar);
        int i2 = zzhnVar.zza;
        if (i2 < 0) {
            throw zzjk.zzb();
        }
        if (i2 > bArr.length - zza) {
            throw zzjk.zza();
        }
        if (i2 == 0) {
            zzhnVar.zzc = zzht.zza;
            return zza;
        }
        zzhnVar.zzc = zzht.zza(bArr, zza, i2);
        return zza + i2;
    }
}
