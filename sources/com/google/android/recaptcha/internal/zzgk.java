package com.google.android.recaptcha.internal;

import org.telegram.messenger.NotificationCenter;

/* loaded from: classes.dex */
final class zzgk {
    static int zza(byte[] bArr, int i, zzgj zzgjVar) {
        int zzi = zzi(bArr, i, zzgjVar);
        int i2 = zzgjVar.zza;
        if (i2 < 0) {
            throw zzje.zzf();
        }
        if (i2 > bArr.length - zzi) {
            throw zzje.zzj();
        }
        if (i2 == 0) {
            zzgjVar.zzc = zzgw.zzb;
            return zzi;
        }
        zzgjVar.zzc = zzgw.zzm(bArr, zzi, i2);
        return zzi + i2;
    }

    static int zzb(byte[] bArr, int i) {
        int i2 = bArr[i] & 255;
        int i3 = bArr[i + 1] & 255;
        int i4 = bArr[i + 2] & 255;
        return ((bArr[i + 3] & 255) << 24) | (i3 << 8) | i2 | (i4 << 16);
    }

    static int zzc(zzkr zzkrVar, byte[] bArr, int i, int i2, int i3, zzgj zzgjVar) {
        Object zze = zzkrVar.zze();
        int zzm = zzm(zze, zzkrVar, bArr, i, i2, i3, zzgjVar);
        zzkrVar.zzf(zze);
        zzgjVar.zzc = zze;
        return zzm;
    }

    static int zzd(zzkr zzkrVar, byte[] bArr, int i, int i2, zzgj zzgjVar) {
        Object zze = zzkrVar.zze();
        int zzn = zzn(zze, zzkrVar, bArr, i, i2, zzgjVar);
        zzkrVar.zzf(zze);
        zzgjVar.zzc = zze;
        return zzn;
    }

    static int zze(zzkr zzkrVar, int i, byte[] bArr, int i2, int i3, zzjb zzjbVar, zzgj zzgjVar) {
        int zzd = zzd(zzkrVar, bArr, i2, i3, zzgjVar);
        while (true) {
            zzjbVar.add(zzgjVar.zzc);
            if (zzd >= i3) {
                break;
            }
            int zzi = zzi(bArr, zzd, zzgjVar);
            if (i != zzgjVar.zza) {
                break;
            }
            zzd = zzd(zzkrVar, bArr, zzi, i3, zzgjVar);
        }
        return zzd;
    }

    static int zzf(byte[] bArr, int i, zzjb zzjbVar, zzgj zzgjVar) {
        zziu zziuVar = (zziu) zzjbVar;
        int zzi = zzi(bArr, i, zzgjVar);
        int i2 = zzgjVar.zza + zzi;
        while (zzi < i2) {
            zzi = zzi(bArr, zzi, zzgjVar);
            zziuVar.zzg(zzgjVar.zza);
        }
        if (zzi == i2) {
            return zzi;
        }
        throw zzje.zzj();
    }

    static int zzg(byte[] bArr, int i, zzgj zzgjVar) {
        int zzi = zzi(bArr, i, zzgjVar);
        int i2 = zzgjVar.zza;
        if (i2 < 0) {
            throw zzje.zzf();
        }
        if (i2 == 0) {
            zzgjVar.zzc = "";
            return zzi;
        }
        zzgjVar.zzc = new String(bArr, zzi, i2, zzjc.zzb);
        return zzi + i2;
    }

    static int zzh(int i, byte[] bArr, int i2, int i3, zzlm zzlmVar, zzgj zzgjVar) {
        if ((i >>> 3) == 0) {
            throw zzje.zzc();
        }
        int i4 = i & 7;
        if (i4 == 0) {
            int zzl = zzl(bArr, i2, zzgjVar);
            zzlmVar.zzj(i, Long.valueOf(zzgjVar.zzb));
            return zzl;
        }
        if (i4 == 1) {
            zzlmVar.zzj(i, Long.valueOf(zzp(bArr, i2)));
            return i2 + 8;
        }
        if (i4 == 2) {
            int zzi = zzi(bArr, i2, zzgjVar);
            int i5 = zzgjVar.zza;
            if (i5 < 0) {
                throw zzje.zzf();
            }
            if (i5 > bArr.length - zzi) {
                throw zzje.zzj();
            }
            zzlmVar.zzj(i, i5 == 0 ? zzgw.zzb : zzgw.zzm(bArr, zzi, i5));
            return zzi + i5;
        }
        if (i4 != 3) {
            if (i4 != 5) {
                throw zzje.zzc();
            }
            zzlmVar.zzj(i, Integer.valueOf(zzb(bArr, i2)));
            return i2 + 4;
        }
        int i6 = (i & (-8)) | 4;
        zzlm zzf = zzlm.zzf();
        int i7 = 0;
        while (true) {
            if (i2 >= i3) {
                break;
            }
            int zzi2 = zzi(bArr, i2, zzgjVar);
            int i8 = zzgjVar.zza;
            i7 = i8;
            if (i8 == i6) {
                i2 = zzi2;
                break;
            }
            int zzh = zzh(i7, bArr, zzi2, i3, zzf, zzgjVar);
            i7 = i8;
            i2 = zzh;
        }
        if (i2 > i3 || i7 != i6) {
            throw zzje.zzg();
        }
        zzlmVar.zzj(i, zzf);
        return i2;
    }

    static int zzi(byte[] bArr, int i, zzgj zzgjVar) {
        int i2 = i + 1;
        byte b = bArr[i];
        if (b < 0) {
            return zzj(b, bArr, i2, zzgjVar);
        }
        zzgjVar.zza = b;
        return i2;
    }

    static int zzj(int i, byte[] bArr, int i2, zzgj zzgjVar) {
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
                zzgjVar.zza = i6 | (b2 << 14);
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
                    zzgjVar.zza = i8 | (b4 << 28);
                    return i9;
                }
                int i10 = i8 | ((b4 & Byte.MAX_VALUE) << 28);
                while (true) {
                    int i11 = i9 + 1;
                    if (bArr[i9] >= 0) {
                        zzgjVar.zza = i10;
                        return i11;
                    }
                    i9 = i11;
                }
            }
        }
        zzgjVar.zza = i5 | i3;
        return i4;
    }

    static int zzk(int i, byte[] bArr, int i2, int i3, zzjb zzjbVar, zzgj zzgjVar) {
        zziu zziuVar = (zziu) zzjbVar;
        int zzi = zzi(bArr, i2, zzgjVar);
        while (true) {
            zziuVar.zzg(zzgjVar.zza);
            if (zzi >= i3) {
                break;
            }
            int zzi2 = zzi(bArr, zzi, zzgjVar);
            if (i != zzgjVar.zza) {
                break;
            }
            zzi = zzi(bArr, zzi2, zzgjVar);
        }
        return zzi;
    }

    static int zzl(byte[] bArr, int i, zzgj zzgjVar) {
        long j = bArr[i];
        int i2 = i + 1;
        if (j >= 0) {
            zzgjVar.zzb = j;
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
        zzgjVar.zzb = j2;
        return i3;
    }

    static int zzm(Object obj, zzkr zzkrVar, byte[] bArr, int i, int i2, int i3, zzgj zzgjVar) {
        int zzc = ((zzkh) zzkrVar).zzc(obj, bArr, i, i2, i3, zzgjVar);
        zzgjVar.zzc = obj;
        return zzc;
    }

    static int zzn(Object obj, zzkr zzkrVar, byte[] bArr, int i, int i2, zzgj zzgjVar) {
        int i3 = i + 1;
        int i4 = bArr[i];
        if (i4 < 0) {
            i3 = zzj(i4, bArr, i3, zzgjVar);
            i4 = zzgjVar.zza;
        }
        int i5 = i3;
        if (i4 < 0 || i4 > i2 - i5) {
            throw zzje.zzj();
        }
        int i6 = i4 + i5;
        zzkrVar.zzi(obj, bArr, i5, i6, zzgjVar);
        zzgjVar.zzc = obj;
        return i6;
    }

    static int zzo(int i, byte[] bArr, int i2, int i3, zzgj zzgjVar) {
        if ((i >>> 3) == 0) {
            throw zzje.zzc();
        }
        int i4 = i & 7;
        if (i4 == 0) {
            return zzl(bArr, i2, zzgjVar);
        }
        if (i4 == 1) {
            return i2 + 8;
        }
        if (i4 == 2) {
            return zzi(bArr, i2, zzgjVar) + zzgjVar.zza;
        }
        if (i4 != 3) {
            if (i4 == 5) {
                return i2 + 4;
            }
            throw zzje.zzc();
        }
        int i5 = (i & (-8)) | 4;
        int i6 = 0;
        while (i2 < i3) {
            i2 = zzi(bArr, i2, zzgjVar);
            i6 = zzgjVar.zza;
            if (i6 == i5) {
                break;
            }
            i2 = zzo(i6, bArr, i2, i3, zzgjVar);
        }
        if (i2 > i3 || i6 != i5) {
            throw zzje.zzg();
        }
        return i2;
    }

    static long zzp(byte[] bArr, int i) {
        return (bArr[i] & 255) | ((bArr[i + 1] & 255) << 8) | ((bArr[i + 2] & 255) << 16) | ((bArr[i + 3] & 255) << 24) | ((bArr[i + 4] & 255) << 32) | ((bArr[i + 5] & 255) << 40) | ((bArr[i + 6] & 255) << 48) | ((bArr[i + 7] & 255) << 56);
    }
}
