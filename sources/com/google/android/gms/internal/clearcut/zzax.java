package com.google.android.gms.internal.clearcut;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import org.telegram.messenger.NotificationCenter;

/* loaded from: classes.dex */
abstract class zzax {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(int i, byte[] bArr, int i2, int i3, zzay zzayVar) {
        if ((i >>> 3) == 0) {
            throw zzco.zzbm();
        }
        int i4 = i & 7;
        if (i4 == 0) {
            return zzb(bArr, i2, zzayVar);
        }
        if (i4 == 1) {
            return i2 + 8;
        }
        if (i4 == 2) {
            return zza(bArr, i2, zzayVar) + zzayVar.zzfd;
        }
        if (i4 != 3) {
            if (i4 == 5) {
                return i2 + 4;
            }
            throw zzco.zzbm();
        }
        int i5 = (i & (-8)) | 4;
        int i6 = 0;
        while (i2 < i3) {
            i2 = zza(bArr, i2, zzayVar);
            i6 = zzayVar.zzfd;
            if (i6 == i5) {
                break;
            }
            i2 = zza(i6, bArr, i2, i3, zzayVar);
        }
        if (i2 > i3 || i6 != i5) {
            throw zzco.zzbo();
        }
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(int i, byte[] bArr, int i2, int i3, zzcn zzcnVar, zzay zzayVar) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcnVar);
        zza(bArr, i2, zzayVar);
        int i4 = zzayVar.zzfd;
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(int i, byte[] bArr, int i2, int i3, zzey zzeyVar, zzay zzayVar) {
        if ((i >>> 3) == 0) {
            throw zzco.zzbm();
        }
        int i4 = i & 7;
        if (i4 == 0) {
            int zzb = zzb(bArr, i2, zzayVar);
            zzeyVar.zzb(i, Long.valueOf(zzayVar.zzfe));
            return zzb;
        }
        if (i4 == 1) {
            zzeyVar.zzb(i, Long.valueOf(zzd(bArr, i2)));
            return i2 + 8;
        }
        if (i4 == 2) {
            int zza = zza(bArr, i2, zzayVar);
            int i5 = zzayVar.zzfd;
            zzeyVar.zzb(i, i5 == 0 ? zzbb.zzfi : zzbb.zzb(bArr, zza, i5));
            return zza + i5;
        }
        if (i4 != 3) {
            if (i4 != 5) {
                throw zzco.zzbm();
            }
            zzeyVar.zzb(i, Integer.valueOf(zzc(bArr, i2)));
            return i2 + 4;
        }
        zzey zzeb = zzey.zzeb();
        int i6 = (i & (-8)) | 4;
        int i7 = 0;
        while (true) {
            if (i2 >= i3) {
                break;
            }
            int zza2 = zza(bArr, i2, zzayVar);
            int i8 = zzayVar.zzfd;
            i7 = i8;
            if (i8 == i6) {
                i2 = zza2;
                break;
            }
            int zza3 = zza(i7, bArr, zza2, i3, zzeb, zzayVar);
            i7 = i8;
            i2 = zza3;
        }
        if (i2 > i3 || i7 != i6) {
            throw zzco.zzbo();
        }
        zzeyVar.zzb(i, zzeb);
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(int i, byte[] bArr, int i2, zzay zzayVar) {
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
                zzayVar.zzfd = i6 | (b2 << 14);
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
                    zzayVar.zzfd = i8 | (b4 << 28);
                    return i9;
                }
                int i10 = i8 | ((b4 & Byte.MAX_VALUE) << 28);
                while (true) {
                    int i11 = i9 + 1;
                    if (bArr[i9] >= 0) {
                        zzayVar.zzfd = i10;
                        return i11;
                    }
                    i9 = i11;
                }
            }
        }
        zzayVar.zzfd = i4 | i3;
        return i5;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(byte[] bArr, int i, zzay zzayVar) {
        int i2 = i + 1;
        byte b = bArr[i];
        if (b < 0) {
            return zza(b, bArr, i2, zzayVar);
        }
        zzayVar.zzfd = b;
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(byte[] bArr, int i, zzcn zzcnVar, zzay zzayVar) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcnVar);
        int zza = zza(bArr, i, zzayVar);
        int i2 = zzayVar.zzfd + zza;
        if (zza < i2) {
            zza(bArr, zza, zzayVar);
            throw null;
        }
        if (zza == i2) {
            return zza;
        }
        throw zzco.zzbl();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzb(byte[] bArr, int i, zzay zzayVar) {
        int i2 = i + 1;
        long j = bArr[i];
        if (j >= 0) {
            zzayVar.zzfe = j;
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
        zzayVar.zzfe = j2;
        return i3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzc(byte[] bArr, int i) {
        return ((bArr[i + 3] & 255) << 24) | (bArr[i] & 255) | ((bArr[i + 1] & 255) << 8) | ((bArr[i + 2] & 255) << 16);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzc(byte[] bArr, int i, zzay zzayVar) {
        int zza = zza(bArr, i, zzayVar);
        int i2 = zzayVar.zzfd;
        if (i2 == 0) {
            zzayVar.zzff = "";
            return zza;
        }
        zzayVar.zzff = new String(bArr, zza, i2, zzci.UTF_8);
        return zza + i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzd(byte[] bArr, int i, zzay zzayVar) {
        int zza = zza(bArr, i, zzayVar);
        int i2 = zzayVar.zzfd;
        if (i2 == 0) {
            zzayVar.zzff = "";
            return zza;
        }
        int i3 = zza + i2;
        if (!zzff.zze(bArr, zza, i3)) {
            throw zzco.zzbp();
        }
        zzayVar.zzff = new String(bArr, zza, i2, zzci.UTF_8);
        return i3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long zzd(byte[] bArr, int i) {
        return ((bArr[i + 7] & 255) << 56) | (bArr[i] & 255) | ((bArr[i + 1] & 255) << 8) | ((bArr[i + 2] & 255) << 16) | ((bArr[i + 3] & 255) << 24) | ((bArr[i + 4] & 255) << 32) | ((bArr[i + 5] & 255) << 40) | ((bArr[i + 6] & 255) << 48);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static double zze(byte[] bArr, int i) {
        return Double.longBitsToDouble(zzd(bArr, i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zze(byte[] bArr, int i, zzay zzayVar) {
        int zza = zza(bArr, i, zzayVar);
        int i2 = zzayVar.zzfd;
        if (i2 == 0) {
            zzayVar.zzff = zzbb.zzfi;
            return zza;
        }
        zzayVar.zzff = zzbb.zzb(bArr, zza, i2);
        return zza + i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float zzf(byte[] bArr, int i) {
        return Float.intBitsToFloat(zzc(bArr, i));
    }
}
