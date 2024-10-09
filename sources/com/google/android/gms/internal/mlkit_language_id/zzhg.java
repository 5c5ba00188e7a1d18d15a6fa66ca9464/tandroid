package com.google.android.gms.internal.mlkit_language_id;

import java.util.Arrays;

/* loaded from: classes.dex */
public final class zzhg {
    private static final zzhg zza = new zzhg(0, new int[0], new Object[0], false);
    private int zzb;
    private int[] zzc;
    private Object[] zzd;
    private int zze = -1;
    private boolean zzf;

    private zzhg(int i, int[] iArr, Object[] objArr, boolean z) {
        this.zzb = i;
        this.zzc = iArr;
        this.zzd = objArr;
        this.zzf = z;
    }

    public static zzhg zza() {
        return zza;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzhg zza(zzhg zzhgVar, zzhg zzhgVar2) {
        int i = zzhgVar.zzb + zzhgVar2.zzb;
        int[] copyOf = Arrays.copyOf(zzhgVar.zzc, i);
        System.arraycopy(zzhgVar2.zzc, 0, copyOf, zzhgVar.zzb, zzhgVar2.zzb);
        Object[] copyOf2 = Arrays.copyOf(zzhgVar.zzd, i);
        System.arraycopy(zzhgVar2.zzd, 0, copyOf2, zzhgVar.zzb, zzhgVar2.zzb);
        return new zzhg(i, copyOf, copyOf2, true);
    }

    private static void zza(int i, Object obj, zzib zzibVar) {
        int i2 = i >>> 3;
        int i3 = i & 7;
        if (i3 == 0) {
            zzibVar.zza(i2, ((Long) obj).longValue());
            return;
        }
        if (i3 == 1) {
            zzibVar.zzd(i2, ((Long) obj).longValue());
            return;
        }
        if (i3 == 2) {
            zzibVar.zza(i2, (zzdn) obj);
            return;
        }
        if (i3 != 3) {
            if (i3 != 5) {
                throw new RuntimeException(zzez.zza());
            }
            zzibVar.zzd(i2, ((Integer) obj).intValue());
        } else if (zzibVar.zza() == zzia.zza) {
            zzibVar.zza(i2);
            ((zzhg) obj).zzb(zzibVar);
            zzibVar.zzb(i2);
        } else {
            zzibVar.zzb(i2);
            ((zzhg) obj).zzb(zzibVar);
            zzibVar.zza(i2);
        }
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof zzhg)) {
            return false;
        }
        zzhg zzhgVar = (zzhg) obj;
        int i = this.zzb;
        if (i == zzhgVar.zzb) {
            int[] iArr = this.zzc;
            int[] iArr2 = zzhgVar.zzc;
            int i2 = 0;
            while (true) {
                if (i2 >= i) {
                    Object[] objArr = this.zzd;
                    Object[] objArr2 = zzhgVar.zzd;
                    int i3 = this.zzb;
                    for (int i4 = 0; i4 < i3; i4++) {
                        if (objArr[i4].equals(objArr2[i4])) {
                        }
                    }
                    return true;
                }
                if (iArr[i2] != iArr2[i2]) {
                    break;
                }
                i2++;
            }
        }
        return false;
    }

    public final int hashCode() {
        int i = this.zzb;
        int i2 = (i + 527) * 31;
        int[] iArr = this.zzc;
        int i3 = 17;
        int i4 = 17;
        for (int i5 = 0; i5 < i; i5++) {
            i4 = (i4 * 31) + iArr[i5];
        }
        int i6 = (i2 + i4) * 31;
        Object[] objArr = this.zzd;
        int i7 = this.zzb;
        for (int i8 = 0; i8 < i7; i8++) {
            i3 = (i3 * 31) + objArr[i8].hashCode();
        }
        return i6 + i3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zza(zzib zzibVar) {
        if (zzibVar.zza() == zzia.zzb) {
            for (int i = this.zzb - 1; i >= 0; i--) {
                zzibVar.zza(this.zzc[i] >>> 3, this.zzd[i]);
            }
            return;
        }
        for (int i2 = 0; i2 < this.zzb; i2++) {
            zzibVar.zza(this.zzc[i2] >>> 3, this.zzd[i2]);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zza(StringBuilder sb, int i) {
        for (int i2 = 0; i2 < this.zzb; i2++) {
            zzga.zza(sb, i, String.valueOf(this.zzc[i2] >>> 3), this.zzd[i2]);
        }
    }

    public final void zzb() {
        this.zzf = false;
    }

    public final void zzb(zzib zzibVar) {
        if (this.zzb == 0) {
            return;
        }
        if (zzibVar.zza() == zzia.zza) {
            for (int i = 0; i < this.zzb; i++) {
                zza(this.zzc[i], this.zzd[i], zzibVar);
            }
            return;
        }
        for (int i2 = this.zzb - 1; i2 >= 0; i2--) {
            zza(this.zzc[i2], this.zzd[i2], zzibVar);
        }
    }

    public final int zzc() {
        int i = this.zze;
        if (i != -1) {
            return i;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < this.zzb; i3++) {
            i2 += zzea.zzd(this.zzc[i3] >>> 3, (zzdn) this.zzd[i3]);
        }
        this.zze = i2;
        return i2;
    }

    public final int zzd() {
        int zze;
        int i = this.zze;
        if (i != -1) {
            return i;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < this.zzb; i3++) {
            int i4 = this.zzc[i3];
            int i5 = i4 >>> 3;
            int i6 = i4 & 7;
            if (i6 == 0) {
                zze = zzea.zze(i5, ((Long) this.zzd[i3]).longValue());
            } else if (i6 == 1) {
                zze = zzea.zzg(i5, ((Long) this.zzd[i3]).longValue());
            } else if (i6 == 2) {
                zze = zzea.zzc(i5, (zzdn) this.zzd[i3]);
            } else if (i6 == 3) {
                zze = (zzea.zze(i5) << 1) + ((zzhg) this.zzd[i3]).zzd();
            } else {
                if (i6 != 5) {
                    throw new IllegalStateException(zzez.zza());
                }
                zze = zzea.zzi(i5, ((Integer) this.zzd[i3]).intValue());
            }
            i2 += zze;
        }
        this.zze = i2;
        return i2;
    }
}
