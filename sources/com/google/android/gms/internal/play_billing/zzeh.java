package com.google.android.gms.internal.play_billing;

import java.io.IOException;
import java.util.Arrays;
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
public final class zzeh {
    private static final zzeh zza = new zzeh(0, new int[0], new Object[0], false);
    private int zzb;
    private int[] zzc;
    private Object[] zzd;
    private int zze;
    private boolean zzf;

    private zzeh() {
        this(0, new int[8], new Object[8], true);
    }

    private zzeh(int i, int[] iArr, Object[] objArr, boolean z) {
        this.zze = -1;
        this.zzb = i;
        this.zzc = iArr;
        this.zzd = objArr;
        this.zzf = z;
    }

    public static zzeh zzc() {
        return zza;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzeh zze(zzeh zzehVar, zzeh zzehVar2) {
        int i = zzehVar.zzb + zzehVar2.zzb;
        int[] copyOf = Arrays.copyOf(zzehVar.zzc, i);
        System.arraycopy(zzehVar2.zzc, 0, copyOf, zzehVar.zzb, zzehVar2.zzb);
        Object[] copyOf2 = Arrays.copyOf(zzehVar.zzd, i);
        System.arraycopy(zzehVar2.zzd, 0, copyOf2, zzehVar.zzb, zzehVar2.zzb);
        return new zzeh(i, copyOf, copyOf2, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzeh zzf() {
        return new zzeh(0, new int[8], new Object[8], true);
    }

    private final void zzl(int i) {
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
        if (obj != null && (obj instanceof zzeh)) {
            zzeh zzehVar = (zzeh) obj;
            int i = this.zzb;
            if (i == zzehVar.zzb) {
                int[] iArr = this.zzc;
                int[] iArr2 = zzehVar.zzc;
                int i2 = 0;
                while (true) {
                    if (i2 >= i) {
                        Object[] objArr = this.zzd;
                        Object[] objArr2 = zzehVar.zzd;
                        int i3 = this.zzb;
                        for (int i4 = 0; i4 < i3; i4++) {
                            if (objArr[i4].equals(objArr2[i4])) {
                            }
                        }
                        return true;
                    } else if (iArr[i2] != iArr2[i2]) {
                        break;
                    } else {
                        i2++;
                    }
                }
            }
            return false;
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
        int i6 = (i2 * 31) + i4;
        Object[] objArr = this.zzd;
        int i7 = this.zzb;
        for (int i8 = 0; i8 < i7; i8++) {
            i3 = (i3 * 31) + objArr[i8].hashCode();
        }
        return (i6 * 31) + i3;
    }

    public final int zza() {
        int zzy;
        int zzx;
        int i;
        int i2 = this.zze;
        if (i2 == -1) {
            int i3 = 0;
            for (int i4 = 0; i4 < this.zzb; i4++) {
                int i5 = this.zzc[i4];
                int i6 = i5 >>> 3;
                int i7 = i5 & 7;
                if (i7 != 0) {
                    if (i7 == 1) {
                        ((Long) this.zzd[i4]).longValue();
                        i = zzbi.zzx(i6 << 3) + 8;
                    } else if (i7 == 2) {
                        int i8 = zzbi.zzb;
                        int zzd = ((zzba) this.zzd[i4]).zzd();
                        i = zzbi.zzx(i6 << 3) + zzbi.zzx(zzd) + zzd;
                    } else if (i7 == 3) {
                        int i9 = i6 << 3;
                        int i10 = zzbi.zzb;
                        zzy = ((zzeh) this.zzd[i4]).zza();
                        int zzx2 = zzbi.zzx(i9);
                        zzx = zzx2 + zzx2;
                    } else if (i7 == 5) {
                        ((Integer) this.zzd[i4]).intValue();
                        i = zzbi.zzx(i6 << 3) + 4;
                    } else {
                        throw new IllegalStateException(zzci.zza());
                    }
                    i3 += i;
                } else {
                    int i11 = i6 << 3;
                    zzy = zzbi.zzy(((Long) this.zzd[i4]).longValue());
                    zzx = zzbi.zzx(i11);
                }
                i = zzx + zzy;
                i3 += i;
            }
            this.zze = i3;
            return i3;
        }
        return i2;
    }

    public final int zzb() {
        int i = this.zze;
        if (i == -1) {
            int i2 = 0;
            for (int i3 = 0; i3 < this.zzb; i3++) {
                int i4 = zzbi.zzb;
                int zzd = ((zzba) this.zzd[i3]).zzd();
                int zzx = zzbi.zzx(zzd) + zzd;
                int zzx2 = zzbi.zzx(16);
                int zzx3 = zzbi.zzx(this.zzc[i3] >>> 3);
                int zzx4 = zzbi.zzx(8);
                i2 += zzx4 + zzx4 + zzx2 + zzx3 + zzbi.zzx(24) + zzx;
            }
            this.zze = i2;
            return i2;
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final zzeh zzd(zzeh zzehVar) {
        if (zzehVar.equals(zza)) {
            return this;
        }
        zzg();
        int i = this.zzb + zzehVar.zzb;
        zzl(i);
        System.arraycopy(zzehVar.zzc, 0, this.zzc, this.zzb, zzehVar.zzb);
        System.arraycopy(zzehVar.zzd, 0, this.zzd, this.zzb, zzehVar.zzb);
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

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zzi(StringBuilder sb, int i) {
        for (int i2 = 0; i2 < this.zzb; i2++) {
            zzdh.zzb(sb, i, String.valueOf(this.zzc[i2] >>> 3), this.zzd[i2]);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zzj(int i, Object obj) {
        zzg();
        zzl(this.zzb + 1);
        int[] iArr = this.zzc;
        int i2 = this.zzb;
        iArr[i2] = i;
        this.zzd[i2] = obj;
        this.zzb = i2 + 1;
    }

    public final void zzk(zzey zzeyVar) throws IOException {
        if (this.zzb != 0) {
            for (int i = 0; i < this.zzb; i++) {
                int i2 = this.zzc[i];
                Object obj = this.zzd[i];
                int i3 = i2 & 7;
                int i4 = i2 >>> 3;
                if (i3 == 0) {
                    zzeyVar.zzt(i4, ((Long) obj).longValue());
                } else if (i3 == 1) {
                    zzeyVar.zzm(i4, ((Long) obj).longValue());
                } else if (i3 == 2) {
                    zzeyVar.zzd(i4, (zzba) obj);
                } else if (i3 == 3) {
                    zzeyVar.zzE(i4);
                    ((zzeh) obj).zzk(zzeyVar);
                    zzeyVar.zzh(i4);
                } else if (i3 == 5) {
                    zzeyVar.zzk(i4, ((Integer) obj).intValue());
                } else {
                    throw new RuntimeException(zzci.zza());
                }
            }
        }
    }
}
