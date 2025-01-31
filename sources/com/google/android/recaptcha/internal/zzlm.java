package com.google.android.recaptcha.internal;

import java.util.Arrays;

/* loaded from: classes.dex */
public final class zzlm {
    private static final zzlm zza = new zzlm(0, new int[0], new Object[0], false);
    private int zzb;
    private int[] zzc;
    private Object[] zzd;
    private int zze;
    private boolean zzf;

    private zzlm() {
        this(0, new int[8], new Object[8], true);
    }

    private zzlm(int i, int[] iArr, Object[] objArr, boolean z) {
        this.zze = -1;
        this.zzb = i;
        this.zzc = iArr;
        this.zzd = objArr;
        this.zzf = z;
    }

    public static zzlm zzc() {
        return zza;
    }

    static zzlm zze(zzlm zzlmVar, zzlm zzlmVar2) {
        int i = zzlmVar.zzb + zzlmVar2.zzb;
        int[] copyOf = Arrays.copyOf(zzlmVar.zzc, i);
        System.arraycopy(zzlmVar2.zzc, 0, copyOf, zzlmVar.zzb, zzlmVar2.zzb);
        Object[] copyOf2 = Arrays.copyOf(zzlmVar.zzd, i);
        System.arraycopy(zzlmVar2.zzd, 0, copyOf2, zzlmVar.zzb, zzlmVar2.zzb);
        return new zzlm(i, copyOf, copyOf2, true);
    }

    static zzlm zzf() {
        return new zzlm(0, new int[8], new Object[8], true);
    }

    private final void zzm(int i) {
        int[] iArr = this.zzc;
        if (i > iArr.length) {
            int i2 = this.zzb;
            int i3 = i2 + (i2 / 2);
            if (i3 >= i) {
                i = i3;
            }
            if (i < 8) {
                i = 8;
            }
            this.zzc = Arrays.copyOf(iArr, i);
            this.zzd = Arrays.copyOf(this.zzd, i);
        }
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof zzlm)) {
            return false;
        }
        zzlm zzlmVar = (zzlm) obj;
        int i = this.zzb;
        if (i == zzlmVar.zzb) {
            int[] iArr = this.zzc;
            int[] iArr2 = zzlmVar.zzc;
            int i2 = 0;
            while (true) {
                if (i2 >= i) {
                    Object[] objArr = this.zzd;
                    Object[] objArr2 = zzlmVar.zzd;
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
        int i2 = i + 527;
        int[] iArr = this.zzc;
        int i3 = 17;
        int i4 = 17;
        for (int i5 = 0; i5 < i; i5++) {
            i4 = (i4 * 31) + iArr[i5];
        }
        int i6 = ((i2 * 31) + i4) * 31;
        Object[] objArr = this.zzd;
        int i7 = this.zzb;
        for (int i8 = 0; i8 < i7; i8++) {
            i3 = (i3 * 31) + objArr[i8].hashCode();
        }
        return i6 + i3;
    }

    public final int zza() {
        int zzz;
        int zzy;
        int i;
        int i2 = this.zze;
        if (i2 != -1) {
            return i2;
        }
        int i3 = 0;
        for (int i4 = 0; i4 < this.zzb; i4++) {
            int i5 = this.zzc[i4];
            int i6 = i5 >>> 3;
            int i7 = i5 & 7;
            if (i7 != 0) {
                if (i7 == 1) {
                    ((Long) this.zzd[i4]).longValue();
                    i = zzhh.zzy(i6 << 3) + 8;
                } else if (i7 == 2) {
                    int i8 = i6 << 3;
                    zzgw zzgwVar = (zzgw) this.zzd[i4];
                    int i9 = zzhh.zzb;
                    int zzd = zzgwVar.zzd();
                    i = zzhh.zzy(i8) + zzhh.zzy(zzd) + zzd;
                } else if (i7 == 3) {
                    int i10 = i6 << 3;
                    int i11 = zzhh.zzb;
                    zzz = ((zzlm) this.zzd[i4]).zza();
                    int zzy2 = zzhh.zzy(i10);
                    zzy = zzy2 + zzy2;
                } else {
                    if (i7 != 5) {
                        throw new IllegalStateException(zzje.zza());
                    }
                    ((Integer) this.zzd[i4]).intValue();
                    i = zzhh.zzy(i6 << 3) + 4;
                }
                i3 += i;
            } else {
                int i12 = i6 << 3;
                zzz = zzhh.zzz(((Long) this.zzd[i4]).longValue());
                zzy = zzhh.zzy(i12);
            }
            i = zzy + zzz;
            i3 += i;
        }
        this.zze = i3;
        return i3;
    }

    public final int zzb() {
        int i = this.zze;
        if (i != -1) {
            return i;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < this.zzb; i3++) {
            int i4 = this.zzc[i3] >>> 3;
            zzgw zzgwVar = (zzgw) this.zzd[i3];
            int i5 = zzhh.zzb;
            int zzd = zzgwVar.zzd();
            int zzy = zzhh.zzy(zzd) + zzd;
            int zzy2 = zzhh.zzy(16);
            int zzy3 = zzhh.zzy(i4);
            int zzy4 = zzhh.zzy(8);
            i2 += zzy4 + zzy4 + zzy2 + zzy3 + zzhh.zzy(24) + zzy;
        }
        this.zze = i2;
        return i2;
    }

    final zzlm zzd(zzlm zzlmVar) {
        if (zzlmVar.equals(zza)) {
            return this;
        }
        zzg();
        int i = this.zzb + zzlmVar.zzb;
        zzm(i);
        System.arraycopy(zzlmVar.zzc, 0, this.zzc, this.zzb, zzlmVar.zzb);
        System.arraycopy(zzlmVar.zzd, 0, this.zzd, this.zzb, zzlmVar.zzb);
        this.zzb = i;
        return this;
    }

    final void zzg() {
        if (!this.zzf) {
            throw new UnsupportedOperationException();
        }
    }

    public final void zzh() {
        if (this.zzf) {
            this.zzf = false;
        }
    }

    final void zzi(StringBuilder sb, int i) {
        for (int i2 = 0; i2 < this.zzb; i2++) {
            zzkg.zzb(sb, i, String.valueOf(this.zzc[i2] >>> 3), this.zzd[i2]);
        }
    }

    final void zzj(int i, Object obj) {
        zzg();
        zzm(this.zzb + 1);
        int[] iArr = this.zzc;
        int i2 = this.zzb;
        iArr[i2] = i;
        this.zzd[i2] = obj;
        this.zzb = i2 + 1;
    }

    final void zzk(zzmd zzmdVar) {
        for (int i = 0; i < this.zzb; i++) {
            zzmdVar.zzw(this.zzc[i] >>> 3, this.zzd[i]);
        }
    }

    public final void zzl(zzmd zzmdVar) {
        if (this.zzb != 0) {
            for (int i = 0; i < this.zzb; i++) {
                int i2 = this.zzc[i];
                Object obj = this.zzd[i];
                int i3 = i2 & 7;
                int i4 = i2 >>> 3;
                if (i3 == 0) {
                    zzmdVar.zzt(i4, ((Long) obj).longValue());
                } else if (i3 == 1) {
                    zzmdVar.zzm(i4, ((Long) obj).longValue());
                } else if (i3 == 2) {
                    zzmdVar.zzd(i4, (zzgw) obj);
                } else if (i3 == 3) {
                    zzmdVar.zzF(i4);
                    ((zzlm) obj).zzl(zzmdVar);
                    zzmdVar.zzh(i4);
                } else {
                    if (i3 != 5) {
                        throw new RuntimeException(zzje.zza());
                    }
                    zzmdVar.zzk(i4, ((Integer) obj).intValue());
                }
            }
        }
    }
}
