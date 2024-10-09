package com.google.android.gms.internal.play_billing;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import org.telegram.messenger.NotificationCenter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class zzao {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(byte[] bArr, int i, zzan zzanVar) {
        int zzj = zzj(bArr, i, zzanVar);
        int i2 = zzanVar.zza;
        if (i2 < 0) {
            throw zzci.zzd();
        }
        if (i2 > bArr.length - zzj) {
            throw zzci.zzg();
        }
        if (i2 == 0) {
            zzanVar.zzc = zzba.zzb;
            return zzj;
        }
        zzanVar.zzc = zzba.zzl(bArr, zzj, i2);
        return zzj + i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzb(byte[] bArr, int i) {
        int i2 = bArr[i] & 255;
        int i3 = bArr[i + 1] & 255;
        int i4 = bArr[i + 2] & 255;
        return ((bArr[i + 3] & 255) << 24) | (i3 << 8) | i2 | (i4 << 16);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzc(zzdp zzdpVar, byte[] bArr, int i, int i2, int i3, zzan zzanVar) {
        Object zze = zzdpVar.zze();
        int zzn = zzn(zze, zzdpVar, bArr, i, i2, i3, zzanVar);
        zzdpVar.zzf(zze);
        zzanVar.zzc = zze;
        return zzn;
    }

    static int zzd(zzdp zzdpVar, byte[] bArr, int i, int i2, zzan zzanVar) {
        Object zze = zzdpVar.zze();
        int zzo = zzo(zze, zzdpVar, bArr, i, i2, zzanVar);
        zzdpVar.zzf(zze);
        zzanVar.zzc = zze;
        return zzo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zze(zzdp zzdpVar, int i, byte[] bArr, int i2, int i3, zzcf zzcfVar, zzan zzanVar) {
        int zzd = zzd(zzdpVar, bArr, i2, i3, zzanVar);
        while (true) {
            zzcfVar.add(zzanVar.zzc);
            if (zzd >= i3) {
                break;
            }
            int zzj = zzj(bArr, zzd, zzanVar);
            if (i != zzanVar.zza) {
                break;
            }
            zzd = zzd(zzdpVar, bArr, zzj, i3, zzanVar);
        }
        return zzd;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzf(byte[] bArr, int i, zzcf zzcfVar, zzan zzanVar) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcfVar);
        int zzj = zzj(bArr, i, zzanVar);
        int i2 = zzanVar.zza + zzj;
        if (zzj < i2) {
            zzj(bArr, zzj, zzanVar);
            throw null;
        }
        if (zzj == i2) {
            return zzj;
        }
        throw zzci.zzg();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzg(byte[] bArr, int i, zzan zzanVar) {
        int zzj = zzj(bArr, i, zzanVar);
        int i2 = zzanVar.zza;
        if (i2 < 0) {
            throw zzci.zzd();
        }
        if (i2 == 0) {
            zzanVar.zzc = "";
            return zzj;
        }
        zzanVar.zzc = new String(bArr, zzj, i2, zzcg.zzb);
        return zzj + i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzh(byte[] bArr, int i, zzan zzanVar) {
        int zzj = zzj(bArr, i, zzanVar);
        int i2 = zzanVar.zza;
        if (i2 < 0) {
            throw zzci.zzd();
        }
        if (i2 == 0) {
            zzanVar.zzc = "";
            return zzj;
        }
        int i3 = zzev.$r8$clinit;
        int length = bArr.length;
        if ((((length - zzj) - i2) | zzj | i2) < 0) {
            throw new ArrayIndexOutOfBoundsException(String.format("buffer length=%d, index=%d, size=%d", Integer.valueOf(length), Integer.valueOf(zzj), Integer.valueOf(i2)));
        }
        int i4 = zzj + i2;
        char[] cArr = new char[i2];
        int i5 = 0;
        while (zzj < i4) {
            byte b = bArr[zzj];
            if (!zzer.zzd(b)) {
                break;
            }
            zzj++;
            cArr[i5] = (char) b;
            i5++;
        }
        int i6 = i5;
        while (zzj < i4) {
            int i7 = zzj + 1;
            byte b2 = bArr[zzj];
            if (zzer.zzd(b2)) {
                cArr[i6] = (char) b2;
                i6++;
                zzj = i7;
                while (zzj < i4) {
                    byte b3 = bArr[zzj];
                    if (zzer.zzd(b3)) {
                        zzj++;
                        cArr[i6] = (char) b3;
                        i6++;
                    }
                }
            } else if (b2 < -32) {
                if (i7 >= i4) {
                    throw zzci.zzc();
                }
                zzj += 2;
                zzer.zzc(b2, bArr[i7], cArr, i6);
                i6++;
            } else if (b2 < -16) {
                if (i7 >= i4 - 1) {
                    throw zzci.zzc();
                }
                int i8 = zzj + 2;
                zzj += 3;
                zzer.zzb(b2, bArr[i7], bArr[i8], cArr, i6);
                i6++;
            } else {
                if (i7 >= i4 - 2) {
                    throw zzci.zzc();
                }
                byte b4 = bArr[i7];
                int i9 = zzj + 3;
                byte b5 = bArr[zzj + 2];
                zzj += 4;
                zzer.zza(b2, b4, b5, bArr[i9], cArr, i6);
                i6 += 2;
            }
        }
        zzanVar.zzc = new String(cArr, 0, i6);
        return i4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzi(int i, byte[] bArr, int i2, int i3, zzeh zzehVar, zzan zzanVar) {
        if ((i >>> 3) == 0) {
            throw zzci.zzb();
        }
        int i4 = i & 7;
        if (i4 == 0) {
            int zzm = zzm(bArr, i2, zzanVar);
            zzehVar.zzj(i, Long.valueOf(zzanVar.zzb));
            return zzm;
        }
        if (i4 == 1) {
            zzehVar.zzj(i, Long.valueOf(zzp(bArr, i2)));
            return i2 + 8;
        }
        if (i4 == 2) {
            int zzj = zzj(bArr, i2, zzanVar);
            int i5 = zzanVar.zza;
            if (i5 < 0) {
                throw zzci.zzd();
            }
            if (i5 > bArr.length - zzj) {
                throw zzci.zzg();
            }
            zzehVar.zzj(i, i5 == 0 ? zzba.zzb : zzba.zzl(bArr, zzj, i5));
            return zzj + i5;
        }
        if (i4 != 3) {
            if (i4 != 5) {
                throw zzci.zzb();
            }
            zzehVar.zzj(i, Integer.valueOf(zzb(bArr, i2)));
            return i2 + 4;
        }
        int i6 = (i & (-8)) | 4;
        zzeh zzf = zzeh.zzf();
        int i7 = 0;
        while (true) {
            if (i2 >= i3) {
                break;
            }
            int zzj2 = zzj(bArr, i2, zzanVar);
            int i8 = zzanVar.zza;
            i7 = i8;
            if (i8 == i6) {
                i2 = zzj2;
                break;
            }
            int zzi = zzi(i7, bArr, zzj2, i3, zzf, zzanVar);
            i7 = i8;
            i2 = zzi;
        }
        if (i2 > i3 || i7 != i6) {
            throw zzci.zze();
        }
        zzehVar.zzj(i, zzf);
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzj(byte[] bArr, int i, zzan zzanVar) {
        int i2 = i + 1;
        byte b = bArr[i];
        if (b < 0) {
            return zzk(b, bArr, i2, zzanVar);
        }
        zzanVar.zza = b;
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzk(int i, byte[] bArr, int i2, zzan zzanVar) {
        int i3;
        byte b = bArr[i2];
        int i4 = i2 + 1;
        int i5 = i & NotificationCenter.dialogTranslate;
        if (b >= 0) {
            i3 = b << 7;
        } else {
            int i6 = i5 | ((b & Byte.MAX_VALUE) << 7);
            int i7 = i2 + 2;
            byte b2 = bArr[i4];
            if (b2 >= 0) {
                zzanVar.zza = i6 | (b2 << 14);
                return i7;
            }
            i5 = i6 | ((b2 & Byte.MAX_VALUE) << 14);
            i4 = i2 + 3;
            byte b3 = bArr[i7];
            if (b3 >= 0) {
                i3 = b3 << 21;
            } else {
                int i8 = i5 | ((b3 & Byte.MAX_VALUE) << 21);
                int i9 = i2 + 4;
                byte b4 = bArr[i4];
                if (b4 >= 0) {
                    zzanVar.zza = i8 | (b4 << 28);
                    return i9;
                }
                int i10 = i8 | ((b4 & Byte.MAX_VALUE) << 28);
                while (true) {
                    int i11 = i9 + 1;
                    if (bArr[i9] >= 0) {
                        zzanVar.zza = i10;
                        return i11;
                    }
                    i9 = i11;
                }
            }
        }
        zzanVar.zza = i5 | i3;
        return i4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzl(int i, byte[] bArr, int i2, int i3, zzcf zzcfVar, zzan zzanVar) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcfVar);
        zzj(bArr, i2, zzanVar);
        int i4 = zzanVar.zza;
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzm(byte[] bArr, int i, zzan zzanVar) {
        long j = bArr[i];
        int i2 = i + 1;
        if (j >= 0) {
            zzanVar.zzb = j;
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
        zzanVar.zzb = j2;
        return i3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzn(Object obj, zzdp zzdpVar, byte[] bArr, int i, int i2, int i3, zzan zzanVar) {
        int zzc = ((zzdi) zzdpVar).zzc(obj, bArr, i, i2, i3, zzanVar);
        zzanVar.zzc = obj;
        return zzc;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzo(Object obj, zzdp zzdpVar, byte[] bArr, int i, int i2, zzan zzanVar) {
        int i3 = i + 1;
        int i4 = bArr[i];
        if (i4 < 0) {
            i3 = zzk(i4, bArr, i3, zzanVar);
            i4 = zzanVar.zza;
        }
        int i5 = i3;
        if (i4 < 0 || i4 > i2 - i5) {
            throw zzci.zzg();
        }
        int i6 = i4 + i5;
        zzdpVar.zzh(obj, bArr, i5, i6, zzanVar);
        zzanVar.zzc = obj;
        return i6;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long zzp(byte[] bArr, int i) {
        return (bArr[i] & 255) | ((bArr[i + 1] & 255) << 8) | ((bArr[i + 2] & 255) << 16) | ((bArr[i + 3] & 255) << 24) | ((bArr[i + 4] & 255) << 32) | ((bArr[i + 5] & 255) << 40) | ((bArr[i + 6] & 255) << 48) | ((bArr[i + 7] & 255) << 56);
    }
}
