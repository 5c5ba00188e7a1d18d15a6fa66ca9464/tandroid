package com.google.android.gms.internal.play_billing;

import java.io.IOException;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
public final class zzao {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zza(byte[] bArr, int i, zzan zzanVar) throws zzci {
        int zzj = zzj(bArr, i, zzanVar);
        int i2 = zzanVar.zza;
        if (i2 < 0) {
            throw zzci.zzd();
        }
        if (i2 <= bArr.length - zzj) {
            if (i2 == 0) {
                zzanVar.zzc = zzba.zzb;
                return zzj;
            }
            zzanVar.zzc = zzba.zzl(bArr, zzj, i2);
            return zzj + i2;
        }
        throw zzci.zzg();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzb(byte[] bArr, int i) {
        int i2 = (bArr[i + 1] & 255) << 8;
        return ((bArr[i + 3] & 255) << 24) | i2 | (bArr[i] & 255) | ((bArr[i + 2] & 255) << 16);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzc(zzdp zzdpVar, byte[] bArr, int i, int i2, int i3, zzan zzanVar) throws IOException {
        Object zze = zzdpVar.zze();
        int zzn = zzn(zze, zzdpVar, bArr, i, i2, i3, zzanVar);
        zzdpVar.zzf(zze);
        zzanVar.zzc = zze;
        return zzn;
    }

    static int zzd(zzdp zzdpVar, byte[] bArr, int i, int i2, zzan zzanVar) throws IOException {
        Object zze = zzdpVar.zze();
        int zzo = zzo(zze, zzdpVar, bArr, i, i2, zzanVar);
        zzdpVar.zzf(zze);
        zzanVar.zzc = zze;
        return zzo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zze(zzdp zzdpVar, int i, byte[] bArr, int i2, int i3, zzcf zzcfVar, zzan zzanVar) throws IOException {
        int zzd = zzd(zzdpVar, bArr, i2, i3, zzanVar);
        zzcfVar.add(zzanVar.zzc);
        while (zzd < i3) {
            int zzj = zzj(bArr, zzd, zzanVar);
            if (i != zzanVar.zza) {
                break;
            }
            zzd = zzd(zzdpVar, bArr, zzj, i3, zzanVar);
            zzcfVar.add(zzanVar.zzc);
        }
        return zzd;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzf(byte[] bArr, int i, zzcf zzcfVar, zzan zzanVar) throws IOException {
        zzcc zzccVar = (zzcc) zzcfVar;
        int zzj = zzj(bArr, i, zzanVar);
        int i2 = zzanVar.zza + zzj;
        while (zzj < i2) {
            zzj = zzj(bArr, zzj, zzanVar);
            zzccVar.zzf(zzanVar.zza);
        }
        if (zzj == i2) {
            return zzj;
        }
        throw zzci.zzg();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzg(byte[] bArr, int i, zzan zzanVar) throws zzci {
        int zzj = zzj(bArr, i, zzanVar);
        int i2 = zzanVar.zza;
        if (i2 >= 0) {
            if (i2 == 0) {
                zzanVar.zzc = "";
                return zzj;
            }
            zzanVar.zzc = new String(bArr, zzj, i2, zzcg.zzb);
            return zzj + i2;
        }
        throw zzci.zzd();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzh(byte[] bArr, int i, zzan zzanVar) throws zzci {
        int zzj = zzj(bArr, i, zzanVar);
        int i2 = zzanVar.zza;
        if (i2 >= 0) {
            if (i2 == 0) {
                zzanVar.zzc = "";
                return zzj;
            }
            int i3 = zzev.zza;
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
            while (zzj < i4) {
                int i6 = zzj + 1;
                byte b2 = bArr[zzj];
                if (zzer.zzd(b2)) {
                    int i7 = i5 + 1;
                    cArr[i5] = (char) b2;
                    zzj = i6;
                    while (true) {
                        i5 = i7;
                        if (zzj < i4) {
                            byte b3 = bArr[zzj];
                            if (zzer.zzd(b3)) {
                                zzj++;
                                i7 = i5 + 1;
                                cArr[i5] = (char) b3;
                            }
                        }
                    }
                } else if (b2 < -32) {
                    if (i6 < i4) {
                        zzer.zzc(b2, bArr[i6], cArr, i5);
                        zzj = i6 + 1;
                        i5++;
                    } else {
                        throw zzci.zzc();
                    }
                } else if (b2 < -16) {
                    if (i6 < i4 - 1) {
                        int i8 = i6 + 1;
                        zzer.zzb(b2, bArr[i6], bArr[i8], cArr, i5);
                        zzj = i8 + 1;
                        i5++;
                    } else {
                        throw zzci.zzc();
                    }
                } else if (i6 < i4 - 2) {
                    int i9 = i6 + 1;
                    byte b4 = bArr[i6];
                    int i10 = i9 + 1;
                    zzer.zza(b2, b4, bArr[i9], bArr[i10], cArr, i5);
                    i5 += 2;
                    zzj = i10 + 1;
                } else {
                    throw zzci.zzc();
                }
            }
            zzanVar.zzc = new String(cArr, 0, i5);
            return i4;
        }
        throw zzci.zzd();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzi(int i, byte[] bArr, int i2, int i3, zzeh zzehVar, zzan zzanVar) throws zzci {
        if ((i >>> 3) != 0) {
            int i4 = i & 7;
            if (i4 == 0) {
                int zzm = zzm(bArr, i2, zzanVar);
                zzehVar.zzj(i, Long.valueOf(zzanVar.zzb));
                return zzm;
            } else if (i4 == 1) {
                zzehVar.zzj(i, Long.valueOf(zzp(bArr, i2)));
                return i2 + 8;
            } else if (i4 == 2) {
                int zzj = zzj(bArr, i2, zzanVar);
                int i5 = zzanVar.zza;
                if (i5 < 0) {
                    throw zzci.zzd();
                }
                if (i5 <= bArr.length - zzj) {
                    if (i5 == 0) {
                        zzehVar.zzj(i, zzba.zzb);
                    } else {
                        zzehVar.zzj(i, zzba.zzl(bArr, zzj, i5));
                    }
                    return zzj + i5;
                }
                throw zzci.zzg();
            } else if (i4 != 3) {
                if (i4 == 5) {
                    zzehVar.zzj(i, Integer.valueOf(zzb(bArr, i2)));
                    return i2 + 4;
                }
                throw zzci.zzb();
            } else {
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
        }
        throw zzci.zzb();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzj(byte[] bArr, int i, zzan zzanVar) {
        int i2 = i + 1;
        byte b = bArr[i];
        if (b >= 0) {
            zzanVar.zza = b;
            return i2;
        }
        return zzk(b, bArr, i2, zzanVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzk(int i, byte[] bArr, int i2, zzan zzanVar) {
        byte b = bArr[i2];
        int i3 = i2 + 1;
        int i4 = i & 127;
        if (b >= 0) {
            zzanVar.zza = i4 | (b << 7);
            return i3;
        }
        int i5 = i4 | ((b & Byte.MAX_VALUE) << 7);
        int i6 = i3 + 1;
        byte b2 = bArr[i3];
        if (b2 >= 0) {
            zzanVar.zza = i5 | (b2 << 14);
            return i6;
        }
        int i7 = i5 | ((b2 & Byte.MAX_VALUE) << 14);
        int i8 = i6 + 1;
        byte b3 = bArr[i6];
        if (b3 >= 0) {
            zzanVar.zza = i7 | (b3 << 21);
            return i8;
        }
        int i9 = i7 | ((b3 & Byte.MAX_VALUE) << 21);
        int i10 = i8 + 1;
        byte b4 = bArr[i8];
        if (b4 >= 0) {
            zzanVar.zza = i9 | (b4 << 28);
            return i10;
        }
        int i11 = i9 | ((b4 & Byte.MAX_VALUE) << 28);
        while (true) {
            int i12 = i10 + 1;
            if (bArr[i10] >= 0) {
                zzanVar.zza = i11;
                return i12;
            }
            i10 = i12;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzl(int i, byte[] bArr, int i2, int i3, zzcf zzcfVar, zzan zzanVar) {
        zzcc zzccVar = (zzcc) zzcfVar;
        int zzj = zzj(bArr, i2, zzanVar);
        zzccVar.zzf(zzanVar.zza);
        while (zzj < i3) {
            int zzj2 = zzj(bArr, zzj, zzanVar);
            if (i != zzanVar.zza) {
                break;
            }
            zzj = zzj(bArr, zzj2, zzanVar);
            zzccVar.zzf(zzanVar.zza);
        }
        return zzj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzm(byte[] bArr, int i, zzan zzanVar) {
        long j = bArr[i];
        int i2 = i + 1;
        if (j >= 0) {
            zzanVar.zzb = j;
            return i2;
        }
        int i3 = i2 + 1;
        byte b = bArr[i2];
        long j2 = (j & 127) | ((b & Byte.MAX_VALUE) << 7);
        int i4 = 7;
        while (b < 0) {
            int i5 = i3 + 1;
            byte b2 = bArr[i3];
            i4 += 7;
            j2 |= (b2 & Byte.MAX_VALUE) << i4;
            i3 = i5;
            b = b2;
        }
        zzanVar.zzb = j2;
        return i3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzn(Object obj, zzdp zzdpVar, byte[] bArr, int i, int i2, int i3, zzan zzanVar) throws IOException {
        int zzc = ((zzdi) zzdpVar).zzc(obj, bArr, i, i2, i3, zzanVar);
        zzanVar.zzc = obj;
        return zzc;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int zzo(Object obj, zzdp zzdpVar, byte[] bArr, int i, int i2, zzan zzanVar) throws IOException {
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
