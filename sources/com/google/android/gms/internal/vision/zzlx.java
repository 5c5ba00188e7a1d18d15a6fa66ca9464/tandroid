package com.google.android.gms.internal.vision;

import java.util.Arrays;

/* loaded from: classes.dex */
public final class zzlx {
    private static final zzlx zza = new zzlx(0, new int[0], new Object[0], false);
    private int zzb;
    private int[] zzc;
    private Object[] zzd;
    private int zze;
    private boolean zzf;

    private zzlx() {
        this(0, new int[8], new Object[8], true);
    }

    private zzlx(int i, int[] iArr, Object[] objArr, boolean z) {
        this.zze = -1;
        this.zzb = i;
        this.zzc = iArr;
        this.zzd = objArr;
        this.zzf = z;
    }

    public static zzlx zza() {
        return zza;
    }

    static zzlx zza(zzlx zzlxVar, zzlx zzlxVar2) {
        int i = zzlxVar.zzb + zzlxVar2.zzb;
        int[] copyOf = Arrays.copyOf(zzlxVar.zzc, i);
        System.arraycopy(zzlxVar2.zzc, 0, copyOf, zzlxVar.zzb, zzlxVar2.zzb);
        Object[] copyOf2 = Arrays.copyOf(zzlxVar.zzd, i);
        System.arraycopy(zzlxVar2.zzd, 0, copyOf2, zzlxVar.zzb, zzlxVar2.zzb);
        return new zzlx(i, copyOf, copyOf2, true);
    }

    private static void zza(int i, Object obj, zzmr zzmrVar) {
        int i2 = i >>> 3;
        int i3 = i & 7;
        if (i3 == 0) {
            zzmrVar.zza(i2, ((Long) obj).longValue());
            return;
        }
        if (i3 == 1) {
            zzmrVar.zzd(i2, ((Long) obj).longValue());
            return;
        }
        if (i3 == 2) {
            zzmrVar.zza(i2, (zzht) obj);
            return;
        }
        if (i3 != 3) {
            if (i3 != 5) {
                throw new RuntimeException(zzjk.zzf());
            }
            zzmrVar.zzd(i2, ((Integer) obj).intValue());
        } else if (zzmrVar.zza() == zzmq.zza) {
            zzmrVar.zza(i2);
            ((zzlx) obj).zzb(zzmrVar);
            zzmrVar.zzb(i2);
        } else {
            zzmrVar.zzb(i2);
            ((zzlx) obj).zzb(zzmrVar);
            zzmrVar.zza(i2);
        }
    }

    static zzlx zzb() {
        return new zzlx();
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof zzlx)) {
            return false;
        }
        zzlx zzlxVar = (zzlx) obj;
        int i = this.zzb;
        if (i == zzlxVar.zzb) {
            int[] iArr = this.zzc;
            int[] iArr2 = zzlxVar.zzc;
            int i2 = 0;
            while (true) {
                if (i2 >= i) {
                    Object[] objArr = this.zzd;
                    Object[] objArr2 = zzlxVar.zzd;
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

    final void zza(int i, Object obj) {
        if (!this.zzf) {
            throw new UnsupportedOperationException();
        }
        int i2 = this.zzb;
        int[] iArr = this.zzc;
        if (i2 == iArr.length) {
            int i3 = i2 + (i2 < 4 ? 8 : i2 >> 1);
            this.zzc = Arrays.copyOf(iArr, i3);
            this.zzd = Arrays.copyOf(this.zzd, i3);
        }
        int[] iArr2 = this.zzc;
        int i4 = this.zzb;
        iArr2[i4] = i;
        this.zzd[i4] = obj;
        this.zzb = i4 + 1;
    }

    final void zza(zzmr zzmrVar) {
        if (zzmrVar.zza() == zzmq.zzb) {
            for (int i = this.zzb - 1; i >= 0; i--) {
                zzmrVar.zza(this.zzc[i] >>> 3, this.zzd[i]);
            }
            return;
        }
        for (int i2 = 0; i2 < this.zzb; i2++) {
            zzmrVar.zza(this.zzc[i2] >>> 3, this.zzd[i2]);
        }
    }

    final void zza(StringBuilder sb, int i) {
        for (int i2 = 0; i2 < this.zzb; i2++) {
            zzkp.zza(sb, i, String.valueOf(this.zzc[i2] >>> 3), this.zzd[i2]);
        }
    }

    public final void zzb(zzmr zzmrVar) {
        if (this.zzb == 0) {
            return;
        }
        if (zzmrVar.zza() == zzmq.zza) {
            for (int i = 0; i < this.zzb; i++) {
                zza(this.zzc[i], this.zzd[i], zzmrVar);
            }
            return;
        }
        for (int i2 = this.zzb - 1; i2 >= 0; i2--) {
            zza(this.zzc[i2], this.zzd[i2], zzmrVar);
        }
    }

    public final void zzc() {
        this.zzf = false;
    }

    public final int zzd() {
        int i = this.zze;
        if (i != -1) {
            return i;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < this.zzb; i3++) {
            i2 += zzii.zzd(this.zzc[i3] >>> 3, (zzht) this.zzd[i3]);
        }
        this.zze = i2;
        return i2;
    }

    public final int zze() {
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
                zze = zzii.zze(i5, ((Long) this.zzd[i3]).longValue());
            } else if (i6 == 1) {
                zze = zzii.zzg(i5, ((Long) this.zzd[i3]).longValue());
            } else if (i6 == 2) {
                zze = zzii.zzc(i5, (zzht) this.zzd[i3]);
            } else if (i6 == 3) {
                zze = (zzii.zze(i5) << 1) + ((zzlx) this.zzd[i3]).zze();
            } else {
                if (i6 != 5) {
                    throw new IllegalStateException(zzjk.zzf());
                }
                zze = zzii.zzi(i5, ((Integer) this.zzd[i3]).intValue());
            }
            i2 += zze;
        }
        this.zze = i2;
        return i2;
    }
}
