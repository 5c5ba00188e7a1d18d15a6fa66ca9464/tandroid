package com.google.android.gms.internal.clearcut;

import com.google.android.gms.internal.clearcut.zzcg;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import sun.misc.Unsafe;
/* loaded from: classes.dex */
final class zzds<T> implements zzef<T> {
    private static final Unsafe zzmh = zzfd.zzef();
    private final int[] zzmi;
    private final Object[] zzmj;
    private final int zzmk;
    private final int zzml;
    private final int zzmm;
    private final zzdo zzmn;
    private final boolean zzmo;
    private final boolean zzmq;
    private final boolean zzmr;
    private final int[] zzms;
    private final int[] zzmt;
    private final int[] zzmu;
    private final zzdw zzmv;
    private final zzcy zzmw;
    private final zzex<?, ?> zzmx;
    private final zzbu<?> zzmy;
    private final zzdj zzmz;

    private zzds(int[] iArr, Object[] objArr, int i, int i2, int i3, zzdo zzdoVar, boolean z, boolean z2, int[] iArr2, int[] iArr3, int[] iArr4, zzdw zzdwVar, zzcy zzcyVar, zzex<?, ?> zzexVar, zzbu<?> zzbuVar, zzdj zzdjVar) {
        this.zzmi = iArr;
        this.zzmj = objArr;
        this.zzmk = i;
        this.zzml = i2;
        this.zzmm = i3;
        boolean z3 = zzdoVar instanceof zzcg;
        this.zzmq = z;
        this.zzmo = zzbuVar != null && zzbuVar.zze(zzdoVar);
        this.zzmr = false;
        this.zzms = iArr2;
        this.zzmt = iArr3;
        this.zzmu = iArr4;
        this.zzmv = zzdwVar;
        this.zzmw = zzcyVar;
        this.zzmx = zzexVar;
        this.zzmy = zzbuVar;
        this.zzmn = zzdoVar;
        this.zzmz = zzdjVar;
    }

    private static int zza(int i, byte[] bArr, int i2, int i3, Object obj, zzay zzayVar) throws IOException {
        return zzax.zza(i, bArr, i2, i3, zzn(obj), zzayVar);
    }

    private static int zza(zzef<?> zzefVar, int i, byte[] bArr, int i2, int i3, zzcn<?> zzcnVar, zzay zzayVar) throws IOException {
        int zza = zza((zzef) zzefVar, bArr, i2, i3, zzayVar);
        while (true) {
            zzcnVar.add(zzayVar.zzff);
            if (zza >= i3) {
                break;
            }
            int zza2 = zzax.zza(bArr, zza, zzayVar);
            if (i != zzayVar.zzfd) {
                break;
            }
            zza = zza((zzef) zzefVar, bArr, zza2, i3, zzayVar);
        }
        return zza;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static int zza(zzef zzefVar, byte[] bArr, int i, int i2, int i3, zzay zzayVar) throws IOException {
        zzds zzdsVar = (zzds) zzefVar;
        Object newInstance = zzdsVar.newInstance();
        int zza = zzdsVar.zza((zzds) newInstance, bArr, i, i2, i3, zzayVar);
        zzdsVar.zzc(newInstance);
        zzayVar.zzff = newInstance;
        return zza;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static int zza(zzef zzefVar, byte[] bArr, int i, int i2, zzay zzayVar) throws IOException {
        int i3 = i + 1;
        int i4 = bArr[i];
        if (i4 < 0) {
            i3 = zzax.zza(i4, bArr, i3, zzayVar);
            i4 = zzayVar.zzfd;
        }
        int i5 = i3;
        if (i4 < 0 || i4 > i2 - i5) {
            throw zzco.zzbl();
        }
        Object newInstance = zzefVar.newInstance();
        int i6 = i4 + i5;
        zzefVar.zza(newInstance, bArr, i5, i6, zzayVar);
        zzefVar.zzc(newInstance);
        zzayVar.zzff = newInstance;
        return i6;
    }

    private static <UT, UB> int zza(zzex<UT, UB> zzexVar, T t) {
        return zzexVar.zzm(zzexVar.zzq(t));
    }

    private final int zza(T t, byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6, int i7, long j, int i8, zzay zzayVar) throws IOException {
        int i9;
        Object obj;
        Object obj2;
        Object obj3;
        long j2;
        int i10;
        int i11;
        Unsafe unsafe = zzmh;
        long j3 = this.zzmi[i8 + 2] & 1048575;
        switch (i7) {
            case 51:
                if (i5 == 1) {
                    obj = Double.valueOf(zzax.zze(bArr, i));
                    unsafe.putObject(t, j, obj);
                    i9 = i + 8;
                    unsafe.putInt(t, j3, i4);
                    return i9;
                }
                return i;
            case 52:
                if (i5 == 5) {
                    obj2 = Float.valueOf(zzax.zzf(bArr, i));
                    unsafe.putObject(t, j, obj2);
                    i9 = i + 4;
                    unsafe.putInt(t, j3, i4);
                    return i9;
                }
                return i;
            case 53:
            case 54:
                if (i5 == 0) {
                    i9 = zzax.zzb(bArr, i, zzayVar);
                    j2 = zzayVar.zzfe;
                    obj3 = Long.valueOf(j2);
                    unsafe.putObject(t, j, obj3);
                    unsafe.putInt(t, j3, i4);
                    return i9;
                }
                return i;
            case 55:
            case 62:
                if (i5 == 0) {
                    i9 = zzax.zza(bArr, i, zzayVar);
                    i10 = zzayVar.zzfd;
                    obj3 = Integer.valueOf(i10);
                    unsafe.putObject(t, j, obj3);
                    unsafe.putInt(t, j3, i4);
                    return i9;
                }
                return i;
            case 56:
            case 65:
                if (i5 == 1) {
                    obj = Long.valueOf(zzax.zzd(bArr, i));
                    unsafe.putObject(t, j, obj);
                    i9 = i + 8;
                    unsafe.putInt(t, j3, i4);
                    return i9;
                }
                return i;
            case 57:
            case 64:
                if (i5 == 5) {
                    obj2 = Integer.valueOf(zzax.zzc(bArr, i));
                    unsafe.putObject(t, j, obj2);
                    i9 = i + 4;
                    unsafe.putInt(t, j3, i4);
                    return i9;
                }
                return i;
            case 58:
                if (i5 == 0) {
                    i9 = zzax.zzb(bArr, i, zzayVar);
                    obj3 = Boolean.valueOf(zzayVar.zzfe != 0);
                    unsafe.putObject(t, j, obj3);
                    unsafe.putInt(t, j3, i4);
                    return i9;
                }
                return i;
            case 59:
                if (i5 == 2) {
                    i9 = zzax.zza(bArr, i, zzayVar);
                    i11 = zzayVar.zzfd;
                    if (i11 == 0) {
                        obj3 = "";
                        unsafe.putObject(t, j, obj3);
                        unsafe.putInt(t, j3, i4);
                        return i9;
                    } else if ((i6 & 536870912) != 0 && !zzff.zze(bArr, i9, i9 + i11)) {
                        throw zzco.zzbp();
                    } else {
                        unsafe.putObject(t, j, new String(bArr, i9, i11, zzci.UTF_8));
                        i9 += i11;
                        unsafe.putInt(t, j3, i4);
                        return i9;
                    }
                }
                return i;
            case 60:
                if (i5 == 2) {
                    i9 = zza(zzad(i8), bArr, i, i2, zzayVar);
                    Object object = unsafe.getInt(t, j3) == i4 ? unsafe.getObject(t, j) : null;
                    obj3 = zzayVar.zzff;
                    if (object != null) {
                        obj3 = zzci.zza(object, obj3);
                    }
                    unsafe.putObject(t, j, obj3);
                    unsafe.putInt(t, j3, i4);
                    return i9;
                }
                return i;
            case 61:
                if (i5 == 2) {
                    i9 = zzax.zza(bArr, i, zzayVar);
                    i11 = zzayVar.zzfd;
                    if (i11 == 0) {
                        obj3 = zzbb.zzfi;
                        unsafe.putObject(t, j, obj3);
                        unsafe.putInt(t, j3, i4);
                        return i9;
                    }
                    unsafe.putObject(t, j, zzbb.zzb(bArr, i9, i11));
                    i9 += i11;
                    unsafe.putInt(t, j3, i4);
                    return i9;
                }
                return i;
            case 63:
                if (i5 == 0) {
                    int zza = zzax.zza(bArr, i, zzayVar);
                    int i12 = zzayVar.zzfd;
                    zzck<?> zzaf = zzaf(i8);
                    if (zzaf != null && zzaf.zzb(i12) == null) {
                        zzn(t).zzb(i3, Long.valueOf(i12));
                        return zza;
                    }
                    unsafe.putObject(t, j, Integer.valueOf(i12));
                    i9 = zza;
                    unsafe.putInt(t, j3, i4);
                    return i9;
                }
                return i;
            case 66:
                if (i5 == 0) {
                    i9 = zzax.zza(bArr, i, zzayVar);
                    i10 = zzbk.zzm(zzayVar.zzfd);
                    obj3 = Integer.valueOf(i10);
                    unsafe.putObject(t, j, obj3);
                    unsafe.putInt(t, j3, i4);
                    return i9;
                }
                return i;
            case 67:
                if (i5 == 0) {
                    i9 = zzax.zzb(bArr, i, zzayVar);
                    j2 = zzbk.zza(zzayVar.zzfe);
                    obj3 = Long.valueOf(j2);
                    unsafe.putObject(t, j, obj3);
                    unsafe.putInt(t, j3, i4);
                    return i9;
                }
                return i;
            case 68:
                if (i5 == 3) {
                    i9 = zza(zzad(i8), bArr, i, i2, (i3 & (-8)) | 4, zzayVar);
                    Object object2 = unsafe.getInt(t, j3) == i4 ? unsafe.getObject(t, j) : null;
                    obj3 = zzayVar.zzff;
                    if (object2 != null) {
                        obj3 = zzci.zza(object2, obj3);
                    }
                    unsafe.putObject(t, j, obj3);
                    unsafe.putInt(t, j3, i4);
                    return i9;
                }
                return i;
            default:
                return i;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:115:0x022f, code lost:
        if (r29.zzfe != 0) goto L116;
     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x0231, code lost:
        r6 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x0233, code lost:
        r6 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x0234, code lost:
        r12.addBoolean(r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x0237, code lost:
        if (r4 >= r19) goto L197;
     */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x0239, code lost:
        r6 = com.google.android.gms.internal.clearcut.zzax.zza(r17, r4, r29);
     */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x023f, code lost:
        if (r20 != r29.zzfd) goto L197;
     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x0241, code lost:
        r4 = com.google.android.gms.internal.clearcut.zzax.zzb(r17, r6, r29);
     */
    /* JADX WARN: Code restructure failed: missing block: B:123:0x0249, code lost:
        if (r29.zzfe == 0) goto L117;
     */
    /* JADX WARN: Code restructure failed: missing block: B:226:?, code lost:
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:227:?, code lost:
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0137, code lost:
        if (r4 == 0) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0139, code lost:
        r12.add(com.google.android.gms.internal.clearcut.zzbb.zzfi);
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x013f, code lost:
        r12.add(com.google.android.gms.internal.clearcut.zzbb.zzb(r17, r1, r4));
        r1 = r1 + r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0147, code lost:
        if (r1 >= r19) goto L226;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x0149, code lost:
        r4 = com.google.android.gms.internal.clearcut.zzax.zza(r17, r1, r29);
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x014f, code lost:
        if (r20 != r29.zzfd) goto L227;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0151, code lost:
        r1 = com.google.android.gms.internal.clearcut.zzax.zza(r17, r4, r29);
        r4 = r29.zzfd;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0157, code lost:
        if (r4 != 0) goto L60;
     */
    /* JADX WARN: Removed duplicated region for block: B:77:0x019a  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x01d0  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:117:0x0233 -> B:118:0x0234). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:123:0x0249 -> B:116:0x0231). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:60:0x013f -> B:61:0x0147). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:65:0x0157 -> B:59:0x0139). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:75:0x0194 -> B:76:0x0198). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:80:0x01a8 -> B:73:0x0189). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:88:0x01ca -> B:89:0x01ce). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:93:0x01de -> B:84:0x01b7). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final int zza(T t, byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6, long j, int i7, long j2, zzay zzayVar) throws IOException {
        int zzb;
        int zza;
        int zza2;
        int zzb2;
        int i8 = i;
        Unsafe unsafe = zzmh;
        zzcn zzcnVar = (zzcn) unsafe.getObject(t, j2);
        if (!zzcnVar.zzu()) {
            int size = zzcnVar.size();
            zzcnVar = zzcnVar.zzi(size == 0 ? 10 : size << 1);
            unsafe.putObject(t, j2, zzcnVar);
        }
        switch (i7) {
            case 18:
            case 35:
                if (i5 == 2) {
                    zzbq zzbqVar = (zzbq) zzcnVar;
                    int zza3 = zzax.zza(bArr, i8, zzayVar);
                    int i9 = zzayVar.zzfd + zza3;
                    while (zza3 < i9) {
                        zzbqVar.zzc(zzax.zze(bArr, zza3));
                        zza3 += 8;
                    }
                    if (zza3 != i9) {
                        throw zzco.zzbl();
                    }
                    return zza3;
                }
                if (i5 == 1) {
                    zzbq zzbqVar2 = (zzbq) zzcnVar;
                    zzbqVar2.zzc(zzax.zze(bArr, i));
                    while (true) {
                        int i10 = i8 + 8;
                        if (i10 >= i2) {
                            return i10;
                        }
                        i8 = zzax.zza(bArr, i10, zzayVar);
                        if (i3 != zzayVar.zzfd) {
                            return i10;
                        }
                        zzbqVar2.zzc(zzax.zze(bArr, i8));
                    }
                }
                return i8;
            case 19:
            case 36:
                if (i5 == 2) {
                    zzce zzceVar = (zzce) zzcnVar;
                    int zza4 = zzax.zza(bArr, i8, zzayVar);
                    int i11 = zzayVar.zzfd + zza4;
                    while (zza4 < i11) {
                        zzceVar.zzc(zzax.zzf(bArr, zza4));
                        zza4 += 4;
                    }
                    if (zza4 != i11) {
                        throw zzco.zzbl();
                    }
                    return zza4;
                }
                if (i5 == 5) {
                    zzce zzceVar2 = (zzce) zzcnVar;
                    zzceVar2.zzc(zzax.zzf(bArr, i));
                    while (true) {
                        int i12 = i8 + 4;
                        if (i12 >= i2) {
                            return i12;
                        }
                        i8 = zzax.zza(bArr, i12, zzayVar);
                        if (i3 != zzayVar.zzfd) {
                            return i12;
                        }
                        zzceVar2.zzc(zzax.zzf(bArr, i8));
                    }
                }
                return i8;
            case 20:
            case 21:
            case 37:
            case 38:
                if (i5 == 2) {
                    zzdc zzdcVar = (zzdc) zzcnVar;
                    int zza5 = zzax.zza(bArr, i8, zzayVar);
                    int i13 = zzayVar.zzfd + zza5;
                    while (zza5 < i13) {
                        zza5 = zzax.zzb(bArr, zza5, zzayVar);
                        zzdcVar.zzm(zzayVar.zzfe);
                    }
                    if (zza5 != i13) {
                        throw zzco.zzbl();
                    }
                    return zza5;
                }
                if (i5 == 0) {
                    zzdc zzdcVar2 = (zzdc) zzcnVar;
                    do {
                        zzb = zzax.zzb(bArr, i8, zzayVar);
                        zzdcVar2.zzm(zzayVar.zzfe);
                        if (zzb >= i2) {
                            return zzb;
                        }
                        i8 = zzax.zza(bArr, zzb, zzayVar);
                    } while (i3 == zzayVar.zzfd);
                    return zzb;
                }
                return i8;
            case 22:
            case 29:
            case 39:
            case 43:
                if (i5 == 2) {
                    return zzax.zza(bArr, i8, zzcnVar, zzayVar);
                }
                if (i5 == 0) {
                    return zzax.zza(i3, bArr, i, i2, zzcnVar, zzayVar);
                }
                return i8;
            case 23:
            case 32:
            case 40:
            case 46:
                if (i5 == 2) {
                    zzdc zzdcVar3 = (zzdc) zzcnVar;
                    int zza6 = zzax.zza(bArr, i8, zzayVar);
                    int i14 = zzayVar.zzfd + zza6;
                    while (zza6 < i14) {
                        zzdcVar3.zzm(zzax.zzd(bArr, zza6));
                        zza6 += 8;
                    }
                    if (zza6 != i14) {
                        throw zzco.zzbl();
                    }
                    return zza6;
                }
                if (i5 == 1) {
                    zzdc zzdcVar4 = (zzdc) zzcnVar;
                    zzdcVar4.zzm(zzax.zzd(bArr, i));
                    while (true) {
                        int i15 = i8 + 8;
                        if (i15 >= i2) {
                            return i15;
                        }
                        i8 = zzax.zza(bArr, i15, zzayVar);
                        if (i3 != zzayVar.zzfd) {
                            return i15;
                        }
                        zzdcVar4.zzm(zzax.zzd(bArr, i8));
                    }
                }
                return i8;
            case 24:
            case 31:
            case 41:
            case 45:
                if (i5 == 2) {
                    zzch zzchVar = (zzch) zzcnVar;
                    int zza7 = zzax.zza(bArr, i8, zzayVar);
                    int i16 = zzayVar.zzfd + zza7;
                    while (zza7 < i16) {
                        zzchVar.zzac(zzax.zzc(bArr, zza7));
                        zza7 += 4;
                    }
                    if (zza7 != i16) {
                        throw zzco.zzbl();
                    }
                    return zza7;
                }
                if (i5 == 5) {
                    zzch zzchVar2 = (zzch) zzcnVar;
                    zzchVar2.zzac(zzax.zzc(bArr, i));
                    while (true) {
                        int i17 = i8 + 4;
                        if (i17 >= i2) {
                            return i17;
                        }
                        i8 = zzax.zza(bArr, i17, zzayVar);
                        if (i3 != zzayVar.zzfd) {
                            return i17;
                        }
                        zzchVar2.zzac(zzax.zzc(bArr, i8));
                    }
                }
                return i8;
            case 25:
            case 42:
                if (i5 != 2) {
                    if (i5 == 0) {
                        zzaz zzazVar = (zzaz) zzcnVar;
                        i8 = zzax.zzb(bArr, i8, zzayVar);
                        break;
                    }
                    return i8;
                }
                zzaz zzazVar2 = (zzaz) zzcnVar;
                zza = zzax.zza(bArr, i8, zzayVar);
                int i18 = zzayVar.zzfd + zza;
                while (zza < i18) {
                    zza = zzax.zzb(bArr, zza, zzayVar);
                    zzazVar2.addBoolean(zzayVar.zzfe != 0);
                }
                if (zza != i18) {
                    throw zzco.zzbl();
                }
                return zza;
            case 26:
                if (i5 == 2) {
                    int i19 = ((j & 536870912) > 0L ? 1 : ((j & 536870912) == 0L ? 0 : -1));
                    i8 = zzax.zza(bArr, i8, zzayVar);
                    if (i19 == 0) {
                        int i20 = zzayVar.zzfd;
                        if (i20 != 0) {
                            String str = new String(bArr, i8, i20, zzci.UTF_8);
                            zzcnVar.add(str);
                            i8 += i20;
                            if (i8 < i2) {
                                int zza8 = zzax.zza(bArr, i8, zzayVar);
                                if (i3 == zzayVar.zzfd) {
                                    i8 = zzax.zza(bArr, zza8, zzayVar);
                                    i20 = zzayVar.zzfd;
                                    if (i20 != 0) {
                                        str = new String(bArr, i8, i20, zzci.UTF_8);
                                        zzcnVar.add(str);
                                        i8 += i20;
                                        if (i8 < i2) {
                                        }
                                    }
                                }
                            }
                        }
                        zzcnVar.add("");
                        if (i8 < i2) {
                        }
                    } else {
                        int i21 = zzayVar.zzfd;
                        if (i21 != 0) {
                            int i22 = i8 + i21;
                            if (!zzff.zze(bArr, i8, i22)) {
                                throw zzco.zzbp();
                            }
                            String str2 = new String(bArr, i8, i21, zzci.UTF_8);
                            zzcnVar.add(str2);
                            i8 = i22;
                            if (i8 < i2) {
                                int zza9 = zzax.zza(bArr, i8, zzayVar);
                                if (i3 == zzayVar.zzfd) {
                                    i8 = zzax.zza(bArr, zza9, zzayVar);
                                    int i23 = zzayVar.zzfd;
                                    if (i23 != 0) {
                                        i22 = i8 + i23;
                                        if (!zzff.zze(bArr, i8, i22)) {
                                            throw zzco.zzbp();
                                        }
                                        str2 = new String(bArr, i8, i23, zzci.UTF_8);
                                        zzcnVar.add(str2);
                                        i8 = i22;
                                        if (i8 < i2) {
                                        }
                                    }
                                }
                            }
                        }
                        zzcnVar.add("");
                        if (i8 < i2) {
                        }
                    }
                }
                return i8;
            case 27:
                if (i5 == 2) {
                    return zza(zzad(i6), i3, bArr, i, i2, zzcnVar, zzayVar);
                }
                return i8;
            case 28:
                if (i5 == 2) {
                    int zza10 = zzax.zza(bArr, i8, zzayVar);
                    int i24 = zzayVar.zzfd;
                    break;
                }
                return i8;
            case 30:
            case 44:
                if (i5 != 2) {
                    if (i5 == 0) {
                        zza = zzax.zza(i3, bArr, i, i2, zzcnVar, zzayVar);
                    }
                    return i8;
                }
                zza = zzax.zza(bArr, i8, zzcnVar, zzayVar);
                zzcg zzcgVar = (zzcg) t;
                zzey zzeyVar = zzcgVar.zzjp;
                if (zzeyVar == zzey.zzea()) {
                    zzeyVar = null;
                }
                zzey zzeyVar2 = (zzey) zzeh.zza(i4, zzcnVar, zzaf(i6), zzeyVar, this.zzmx);
                if (zzeyVar2 != null) {
                    zzcgVar.zzjp = zzeyVar2;
                }
                return zza;
            case 33:
            case 47:
                if (i5 == 2) {
                    zzch zzchVar3 = (zzch) zzcnVar;
                    int zza11 = zzax.zza(bArr, i8, zzayVar);
                    int i25 = zzayVar.zzfd + zza11;
                    while (zza11 < i25) {
                        zza11 = zzax.zza(bArr, zza11, zzayVar);
                        zzchVar3.zzac(zzbk.zzm(zzayVar.zzfd));
                    }
                    if (zza11 != i25) {
                        throw zzco.zzbl();
                    }
                    return zza11;
                }
                if (i5 == 0) {
                    zzch zzchVar4 = (zzch) zzcnVar;
                    do {
                        zza2 = zzax.zza(bArr, i8, zzayVar);
                        zzchVar4.zzac(zzbk.zzm(zzayVar.zzfd));
                        if (zza2 >= i2) {
                            return zza2;
                        }
                        i8 = zzax.zza(bArr, zza2, zzayVar);
                    } while (i3 == zzayVar.zzfd);
                    return zza2;
                }
                return i8;
            case 34:
            case 48:
                if (i5 == 2) {
                    zzdc zzdcVar5 = (zzdc) zzcnVar;
                    int zza12 = zzax.zza(bArr, i8, zzayVar);
                    int i26 = zzayVar.zzfd + zza12;
                    while (zza12 < i26) {
                        zza12 = zzax.zzb(bArr, zza12, zzayVar);
                        zzdcVar5.zzm(zzbk.zza(zzayVar.zzfe));
                    }
                    if (zza12 != i26) {
                        throw zzco.zzbl();
                    }
                    return zza12;
                }
                if (i5 == 0) {
                    zzdc zzdcVar6 = (zzdc) zzcnVar;
                    do {
                        zzb2 = zzax.zzb(bArr, i8, zzayVar);
                        zzdcVar6.zzm(zzbk.zza(zzayVar.zzfe));
                        if (zzb2 >= i2) {
                            return zzb2;
                        }
                        i8 = zzax.zza(bArr, zzb2, zzayVar);
                    } while (i3 == zzayVar.zzfd);
                    return zzb2;
                }
                return i8;
            case 49:
                if (i5 == 3) {
                    zzef zzad = zzad(i6);
                    int i27 = (i3 & (-8)) | 4;
                    i8 = zza(zzad, bArr, i, i2, i27, zzayVar);
                    while (true) {
                        zzcnVar.add(zzayVar.zzff);
                        if (i8 < i2) {
                            int zza13 = zzax.zza(bArr, i8, zzayVar);
                            if (i3 == zzayVar.zzfd) {
                                i8 = zza(zzad, bArr, zza13, i2, i27, zzayVar);
                            }
                        }
                    }
                }
                return i8;
            default:
                return i8;
        }
    }

    private final <K, V> int zza(T t, byte[] bArr, int i, int i2, int i3, int i4, long j, zzay zzayVar) throws IOException {
        Unsafe unsafe = zzmh;
        Object zzae = zzae(i3);
        Object object = unsafe.getObject(t, j);
        if (this.zzmz.zzi(object)) {
            Object zzk = this.zzmz.zzk(zzae);
            this.zzmz.zzb(zzk, object);
            unsafe.putObject(t, j, zzk);
            object = zzk;
        }
        this.zzmz.zzl(zzae);
        this.zzmz.zzg(object);
        int zza = zzax.zza(bArr, i, zzayVar);
        int i5 = zzayVar.zzfd;
        if (i5 < 0 || i5 > i2 - zza) {
            throw zzco.zzbl();
        }
        throw null;
    }

    /* JADX WARN: Removed duplicated region for block: B:130:0x0370 A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final int zza(T t, byte[] bArr, int i, int i2, int i3, zzay zzayVar) throws IOException {
        Unsafe unsafe;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        T t2;
        zzck<?> zzaf;
        byte b;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        zzay zzayVar2;
        int i17;
        long j;
        Object obj;
        zzay zzayVar3;
        int i18;
        zzds<T> zzdsVar = this;
        T t3 = t;
        byte[] bArr2 = bArr;
        int i19 = i2;
        int i20 = i3;
        zzay zzayVar4 = zzayVar;
        Unsafe unsafe2 = zzmh;
        int i21 = -1;
        int i22 = i;
        int i23 = 0;
        int i24 = 0;
        int i25 = -1;
        while (true) {
            if (i22 < i19) {
                int i26 = i22 + 1;
                byte b2 = bArr2[i22];
                if (b2 < 0) {
                    i9 = zzax.zza(b2, bArr2, i26, zzayVar4);
                    b = zzayVar4.zzfd;
                } else {
                    b = b2;
                    i9 = i26;
                }
                int i27 = b >>> 3;
                int i28 = b & 7;
                int zzai = zzdsVar.zzai(i27);
                if (zzai != i21) {
                    int[] iArr = zzdsVar.zzmi;
                    int i29 = iArr[zzai + 1];
                    int i30 = (i29 & 267386880) >>> 20;
                    int i31 = b;
                    long j2 = i29 & 1048575;
                    if (i30 <= 17) {
                        int i32 = iArr[zzai + 2];
                        int i33 = 1 << (i32 >>> 20);
                        int i34 = i32 & 1048575;
                        if (i34 != i25) {
                            if (i25 != -1) {
                                unsafe2.putInt(t3, i25, i24);
                            }
                            i24 = unsafe2.getInt(t3, i34);
                            i25 = i34;
                        }
                        switch (i30) {
                            case 0:
                                i6 = i31;
                                zzayVar2 = zzayVar;
                                i16 = i9;
                                i15 = i25;
                                bArr2 = bArr;
                                if (i28 == 1) {
                                    zzfd.zza(t3, j2, zzax.zze(bArr2, i16));
                                    i22 = i16 + 8;
                                    i24 |= i33;
                                    i25 = i15;
                                    i19 = i2;
                                    i23 = i6;
                                    zzayVar4 = zzayVar2;
                                    i21 = -1;
                                    i20 = i3;
                                    break;
                                } else {
                                    i25 = i15;
                                    i4 = i3;
                                    i14 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4 && i4 != 0) {
                                        i7 = i25;
                                        i8 = -1;
                                        i5 = i14;
                                        break;
                                    } else {
                                        i22 = zza(i6, bArr, i14, i2, t, zzayVar);
                                        zzdsVar = this;
                                        t3 = t;
                                        bArr2 = bArr;
                                        i19 = i2;
                                        i20 = i4;
                                        i23 = i6;
                                        unsafe2 = unsafe;
                                        i21 = -1;
                                        zzayVar4 = zzayVar;
                                        break;
                                    }
                                }
                            case 1:
                                i6 = i31;
                                zzayVar2 = zzayVar;
                                i16 = i9;
                                i15 = i25;
                                bArr2 = bArr;
                                if (i28 == 5) {
                                    zzfd.zza((Object) t3, j2, zzax.zzf(bArr2, i16));
                                    i22 = i16 + 4;
                                    i24 |= i33;
                                    i25 = i15;
                                    i19 = i2;
                                    i23 = i6;
                                    zzayVar4 = zzayVar2;
                                    i21 = -1;
                                    i20 = i3;
                                    break;
                                } else {
                                    i25 = i15;
                                    i4 = i3;
                                    i14 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i22 = zza(i6, bArr, i14, i2, t, zzayVar);
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i19 = i2;
                                    i20 = i4;
                                    i23 = i6;
                                    unsafe2 = unsafe;
                                    i21 = -1;
                                    zzayVar4 = zzayVar;
                                    break;
                                }
                                break;
                            case 2:
                            case 3:
                                i6 = i31;
                                i16 = i9;
                                i15 = i25;
                                bArr2 = bArr;
                                if (i28 == 0) {
                                    int zzb = zzax.zzb(bArr2, i16, zzayVar);
                                    unsafe2.putLong(t, j2, zzayVar.zzfe);
                                    i24 |= i33;
                                    i25 = i15;
                                    i19 = i2;
                                    i23 = i6;
                                    zzayVar4 = zzayVar;
                                    i22 = zzb;
                                    i21 = -1;
                                    i20 = i3;
                                    break;
                                } else {
                                    i25 = i15;
                                    i4 = i3;
                                    i14 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i22 = zza(i6, bArr, i14, i2, t, zzayVar);
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i19 = i2;
                                    i20 = i4;
                                    i23 = i6;
                                    unsafe2 = unsafe;
                                    i21 = -1;
                                    zzayVar4 = zzayVar;
                                    break;
                                }
                                break;
                            case 4:
                            case 11:
                                i6 = i31;
                                zzayVar2 = zzayVar;
                                i16 = i9;
                                i15 = i25;
                                bArr2 = bArr;
                                if (i28 == 0) {
                                    i22 = zzax.zza(bArr2, i16, zzayVar2);
                                    unsafe2.putInt(t3, j2, zzayVar2.zzfd);
                                    i24 |= i33;
                                    i25 = i15;
                                    i19 = i2;
                                    i23 = i6;
                                    zzayVar4 = zzayVar2;
                                    i21 = -1;
                                    i20 = i3;
                                    break;
                                } else {
                                    i25 = i15;
                                    i4 = i3;
                                    i14 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i22 = zza(i6, bArr, i14, i2, t, zzayVar);
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i19 = i2;
                                    i20 = i4;
                                    i23 = i6;
                                    unsafe2 = unsafe;
                                    i21 = -1;
                                    zzayVar4 = zzayVar;
                                    break;
                                }
                                break;
                            case 5:
                            case 14:
                                i6 = i31;
                                zzayVar2 = zzayVar;
                                i15 = i25;
                                bArr2 = bArr;
                                if (i28 == 1) {
                                    unsafe2.putLong(t, j2, zzax.zzd(bArr2, i9));
                                    i22 = i9 + 8;
                                    i24 |= i33;
                                    i25 = i15;
                                    i19 = i2;
                                    i23 = i6;
                                    zzayVar4 = zzayVar2;
                                    i21 = -1;
                                    i20 = i3;
                                    break;
                                } else {
                                    i16 = i9;
                                    i25 = i15;
                                    i4 = i3;
                                    i14 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i22 = zza(i6, bArr, i14, i2, t, zzayVar);
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i19 = i2;
                                    i20 = i4;
                                    i23 = i6;
                                    unsafe2 = unsafe;
                                    i21 = -1;
                                    zzayVar4 = zzayVar;
                                    break;
                                }
                                break;
                            case 6:
                            case 13:
                                i6 = i31;
                                i17 = i2;
                                zzayVar2 = zzayVar;
                                i15 = i25;
                                bArr2 = bArr;
                                if (i28 == 5) {
                                    unsafe2.putInt(t3, j2, zzax.zzc(bArr2, i9));
                                    i22 = i9 + 4;
                                    i24 |= i33;
                                    i25 = i15;
                                    i19 = i17;
                                    i23 = i6;
                                    zzayVar4 = zzayVar2;
                                    i21 = -1;
                                    i20 = i3;
                                    break;
                                } else {
                                    i16 = i9;
                                    i25 = i15;
                                    i4 = i3;
                                    i14 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i22 = zza(i6, bArr, i14, i2, t, zzayVar);
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i19 = i2;
                                    i20 = i4;
                                    i23 = i6;
                                    unsafe2 = unsafe;
                                    i21 = -1;
                                    zzayVar4 = zzayVar;
                                    break;
                                }
                                break;
                            case 7:
                                i6 = i31;
                                i17 = i2;
                                zzayVar2 = zzayVar;
                                i15 = i25;
                                bArr2 = bArr;
                                if (i28 == 0) {
                                    i22 = zzax.zzb(bArr2, i9, zzayVar2);
                                    zzfd.zza(t3, j2, zzayVar2.zzfe != 0);
                                    i24 |= i33;
                                    i25 = i15;
                                    i19 = i17;
                                    i23 = i6;
                                    zzayVar4 = zzayVar2;
                                    i21 = -1;
                                    i20 = i3;
                                    break;
                                } else {
                                    i16 = i9;
                                    i25 = i15;
                                    i4 = i3;
                                    i14 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i22 = zza(i6, bArr, i14, i2, t, zzayVar);
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i19 = i2;
                                    i20 = i4;
                                    i23 = i6;
                                    unsafe2 = unsafe;
                                    i21 = -1;
                                    zzayVar4 = zzayVar;
                                    break;
                                }
                                break;
                            case 8:
                                i6 = i31;
                                i17 = i2;
                                zzayVar2 = zzayVar;
                                i15 = i25;
                                j = j2;
                                bArr2 = bArr;
                                if (i28 == 2) {
                                    i22 = (i29 & 536870912) == 0 ? zzax.zzc(bArr2, i9, zzayVar2) : zzax.zzd(bArr2, i9, zzayVar2);
                                    obj = zzayVar2.zzff;
                                    unsafe2.putObject(t3, j, obj);
                                    i24 |= i33;
                                    i25 = i15;
                                    i19 = i17;
                                    i23 = i6;
                                    zzayVar4 = zzayVar2;
                                    i21 = -1;
                                    i20 = i3;
                                    break;
                                } else {
                                    i16 = i9;
                                    i25 = i15;
                                    i4 = i3;
                                    i14 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i22 = zza(i6, bArr, i14, i2, t, zzayVar);
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i19 = i2;
                                    i20 = i4;
                                    i23 = i6;
                                    unsafe2 = unsafe;
                                    i21 = -1;
                                    zzayVar4 = zzayVar;
                                    break;
                                }
                                break;
                            case 9:
                                i6 = i31;
                                zzayVar2 = zzayVar;
                                i15 = i25;
                                j = j2;
                                bArr2 = bArr;
                                if (i28 == 2) {
                                    i17 = i2;
                                    i22 = zza(zzdsVar.zzad(zzai), bArr2, i9, i17, zzayVar2);
                                    obj = (i24 & i33) == 0 ? zzayVar2.zzff : zzci.zza(unsafe2.getObject(t3, j), zzayVar2.zzff);
                                    unsafe2.putObject(t3, j, obj);
                                    i24 |= i33;
                                    i25 = i15;
                                    i19 = i17;
                                    i23 = i6;
                                    zzayVar4 = zzayVar2;
                                    i21 = -1;
                                    i20 = i3;
                                    break;
                                } else {
                                    i16 = i9;
                                    i25 = i15;
                                    i4 = i3;
                                    i14 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i22 = zza(i6, bArr, i14, i2, t, zzayVar);
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i19 = i2;
                                    i20 = i4;
                                    i23 = i6;
                                    unsafe2 = unsafe;
                                    i21 = -1;
                                    zzayVar4 = zzayVar;
                                    break;
                                }
                                break;
                            case 10:
                                i6 = i31;
                                zzayVar3 = zzayVar;
                                i21 = -1;
                                bArr2 = bArr;
                                if (i28 == 2) {
                                    i18 = zzax.zze(bArr2, i9, zzayVar3);
                                    unsafe2.putObject(t3, j2, zzayVar3.zzff);
                                    i24 |= i33;
                                    i19 = i2;
                                    i22 = i18;
                                    i23 = i6;
                                    zzayVar4 = zzayVar3;
                                    i20 = i3;
                                    break;
                                } else {
                                    i16 = i9;
                                    i15 = i25;
                                    i25 = i15;
                                    i4 = i3;
                                    i14 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i22 = zza(i6, bArr, i14, i2, t, zzayVar);
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i19 = i2;
                                    i20 = i4;
                                    i23 = i6;
                                    unsafe2 = unsafe;
                                    i21 = -1;
                                    zzayVar4 = zzayVar;
                                    break;
                                }
                                break;
                            case 12:
                                i6 = i31;
                                zzayVar3 = zzayVar;
                                i21 = -1;
                                bArr2 = bArr;
                                if (i28 == 0) {
                                    i22 = zzax.zza(bArr2, i9, zzayVar3);
                                    int i35 = zzayVar3.zzfd;
                                    zzck<?> zzaf2 = zzdsVar.zzaf(zzai);
                                    if (zzaf2 == null || zzaf2.zzb(i35) != null) {
                                        unsafe2.putInt(t3, j2, i35);
                                        i24 |= i33;
                                    } else {
                                        zzn(t).zzb(i6, Long.valueOf(i35));
                                    }
                                    i19 = i2;
                                    i23 = i6;
                                    zzayVar4 = zzayVar3;
                                    i20 = i3;
                                    break;
                                } else {
                                    i16 = i9;
                                    i15 = i25;
                                    i25 = i15;
                                    i4 = i3;
                                    i14 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i22 = zza(i6, bArr, i14, i2, t, zzayVar);
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i19 = i2;
                                    i20 = i4;
                                    i23 = i6;
                                    unsafe2 = unsafe;
                                    i21 = -1;
                                    zzayVar4 = zzayVar;
                                    break;
                                }
                                break;
                            case 15:
                                i6 = i31;
                                zzayVar3 = zzayVar;
                                i21 = -1;
                                bArr2 = bArr;
                                if (i28 == 0) {
                                    i18 = zzax.zza(bArr2, i9, zzayVar3);
                                    unsafe2.putInt(t3, j2, zzbk.zzm(zzayVar3.zzfd));
                                    i24 |= i33;
                                    i19 = i2;
                                    i22 = i18;
                                    i23 = i6;
                                    zzayVar4 = zzayVar3;
                                    i20 = i3;
                                    break;
                                } else {
                                    i16 = i9;
                                    i15 = i25;
                                    i25 = i15;
                                    i4 = i3;
                                    i14 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i22 = zza(i6, bArr, i14, i2, t, zzayVar);
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i19 = i2;
                                    i20 = i4;
                                    i23 = i6;
                                    unsafe2 = unsafe;
                                    i21 = -1;
                                    zzayVar4 = zzayVar;
                                    break;
                                }
                                break;
                            case 16:
                                i6 = i31;
                                i21 = -1;
                                if (i28 == 0) {
                                    bArr2 = bArr;
                                    int zzb2 = zzax.zzb(bArr2, i9, zzayVar);
                                    unsafe2.putLong(t, j2, zzbk.zza(zzayVar.zzfe));
                                    i24 |= i33;
                                    i23 = i6;
                                    zzayVar4 = zzayVar;
                                    i22 = zzb2;
                                    i19 = i2;
                                    i20 = i3;
                                    break;
                                } else {
                                    i16 = i9;
                                    i15 = i25;
                                    i25 = i15;
                                    i4 = i3;
                                    i14 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i22 = zza(i6, bArr, i14, i2, t, zzayVar);
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i19 = i2;
                                    i20 = i4;
                                    i23 = i6;
                                    unsafe2 = unsafe;
                                    i21 = -1;
                                    zzayVar4 = zzayVar;
                                    break;
                                }
                                break;
                            case 17:
                                if (i28 == 3) {
                                    i6 = i31;
                                    i21 = -1;
                                    i22 = zza(zzdsVar.zzad(zzai), bArr, i9, i2, (i27 << 3) | 4, zzayVar);
                                    zzayVar3 = zzayVar;
                                    unsafe2.putObject(t3, j2, (i24 & i33) == 0 ? zzayVar3.zzff : zzci.zza(unsafe2.getObject(t3, j2), zzayVar3.zzff));
                                    i24 |= i33;
                                    bArr2 = bArr;
                                    i19 = i2;
                                    i23 = i6;
                                    zzayVar4 = zzayVar3;
                                    i20 = i3;
                                    break;
                                } else {
                                    i6 = i31;
                                    i16 = i9;
                                    i15 = i25;
                                    i25 = i15;
                                    i4 = i3;
                                    i14 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i22 = zza(i6, bArr, i14, i2, t, zzayVar);
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i19 = i2;
                                    i20 = i4;
                                    i23 = i6;
                                    unsafe2 = unsafe;
                                    i21 = -1;
                                    zzayVar4 = zzayVar;
                                    break;
                                }
                                break;
                            default:
                                i6 = i31;
                                i16 = i9;
                                i15 = i25;
                                i25 = i15;
                                i4 = i3;
                                i14 = i16;
                                unsafe = unsafe2;
                                if (i6 != i4) {
                                }
                                i22 = zza(i6, bArr, i14, i2, t, zzayVar);
                                zzdsVar = this;
                                t3 = t;
                                bArr2 = bArr;
                                i19 = i2;
                                i20 = i4;
                                i23 = i6;
                                unsafe2 = unsafe;
                                i21 = -1;
                                zzayVar4 = zzayVar;
                                break;
                        }
                    } else {
                        int i36 = i9;
                        i13 = i25;
                        bArr2 = bArr;
                        if (i30 != 27) {
                            i12 = i24;
                            if (i30 <= 49) {
                                i11 = i31;
                                unsafe = unsafe2;
                                i22 = zza((zzds<T>) t, bArr, i36, i2, i31, i27, i28, zzai, i29, i30, j2, zzayVar);
                                if (i22 == i36) {
                                    i6 = i11;
                                    i4 = i3;
                                    i14 = i22;
                                    i25 = i13;
                                    i24 = i12;
                                } else {
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i23 = i11;
                                    i19 = i2;
                                    i20 = i3;
                                    zzayVar4 = zzayVar;
                                    i25 = i13;
                                    i24 = i12;
                                    unsafe2 = unsafe;
                                    i21 = -1;
                                }
                            } else {
                                i10 = i36;
                                i11 = i31;
                                unsafe = unsafe2;
                                if (i30 != 50) {
                                    i22 = zza((zzds<T>) t, bArr, i10, i2, i11, i27, i28, i29, i30, j2, zzai, zzayVar);
                                    if (i22 == i10) {
                                        i6 = i11;
                                        i4 = i3;
                                        i14 = i22;
                                        i25 = i13;
                                        i24 = i12;
                                    } else {
                                        zzdsVar = this;
                                        t3 = t;
                                        bArr2 = bArr;
                                        i23 = i11;
                                        i19 = i2;
                                        i20 = i3;
                                        zzayVar4 = zzayVar;
                                        i25 = i13;
                                        i24 = i12;
                                        unsafe2 = unsafe;
                                        i21 = -1;
                                    }
                                } else if (i28 == 2) {
                                    i22 = zza(t, bArr, i10, i2, zzai, i27, j2, zzayVar);
                                    if (i22 == i10) {
                                        i6 = i11;
                                        i4 = i3;
                                        i14 = i22;
                                        i25 = i13;
                                        i24 = i12;
                                    } else {
                                        zzdsVar = this;
                                        t3 = t;
                                        bArr2 = bArr;
                                        i23 = i11;
                                        i19 = i2;
                                        i20 = i3;
                                        zzayVar4 = zzayVar;
                                        i25 = i13;
                                        i24 = i12;
                                        unsafe2 = unsafe;
                                        i21 = -1;
                                    }
                                } else {
                                    i6 = i11;
                                    i4 = i3;
                                    i14 = i10;
                                    i25 = i13;
                                    i24 = i12;
                                }
                            }
                            if (i6 != i4) {
                            }
                            i22 = zza(i6, bArr, i14, i2, t, zzayVar);
                            zzdsVar = this;
                            t3 = t;
                            bArr2 = bArr;
                            i19 = i2;
                            i20 = i4;
                            i23 = i6;
                            unsafe2 = unsafe;
                            i21 = -1;
                            zzayVar4 = zzayVar;
                        } else if (i28 == 2) {
                            zzcn zzcnVar = (zzcn) unsafe2.getObject(t3, j2);
                            if (!zzcnVar.zzu()) {
                                int size = zzcnVar.size();
                                zzcnVar = zzcnVar.zzi(size == 0 ? 10 : size << 1);
                                unsafe2.putObject(t3, j2, zzcnVar);
                            }
                            zzcn zzcnVar2 = zzcnVar;
                            zzef zzad = zzdsVar.zzad(zzai);
                            i23 = i31;
                            i22 = zza(zzad, i23, bArr, i36, i2, zzcnVar2, zzayVar);
                            i19 = i2;
                            i20 = i3;
                            i25 = i13;
                            i24 = i24;
                            i21 = -1;
                            zzayVar4 = zzayVar;
                        } else {
                            i12 = i24;
                            i10 = i36;
                            i11 = i31;
                        }
                    }
                } else {
                    i10 = i9;
                    i11 = b;
                    i12 = i24;
                    i13 = i25;
                }
                unsafe = unsafe2;
                i6 = i11;
                i4 = i3;
                i14 = i10;
                i25 = i13;
                i24 = i12;
                if (i6 != i4) {
                }
                i22 = zza(i6, bArr, i14, i2, t, zzayVar);
                zzdsVar = this;
                t3 = t;
                bArr2 = bArr;
                i19 = i2;
                i20 = i4;
                i23 = i6;
                unsafe2 = unsafe;
                i21 = -1;
                zzayVar4 = zzayVar;
            } else {
                int i37 = i25;
                unsafe = unsafe2;
                i4 = i20;
                i5 = i22;
                i6 = i23;
                i7 = i37;
                i8 = -1;
            }
        }
        if (i7 != i8) {
            t2 = t;
            unsafe.putInt(t2, i7, i24);
        } else {
            t2 = t;
        }
        int[] iArr2 = this.zzmt;
        if (iArr2 != null) {
            Object obj2 = null;
            for (int i38 : iArr2) {
                zzex zzexVar = this.zzmx;
                int i39 = this.zzmi[i38];
                Object zzo = zzfd.zzo(t2, zzag(i38) & 1048575);
                if (zzo != null && (zzaf = zzaf(i38)) != null) {
                    obj2 = zza(i38, i39, this.zzmz.zzg(zzo), zzaf, (zzck<?>) obj2, (zzex<UT, zzck<?>>) zzexVar);
                }
                obj2 = (zzey) obj2;
            }
            if (obj2 != null) {
                this.zzmx.zzf(t2, obj2);
            }
        }
        if (i4 == 0) {
            if (i5 != i2) {
                throw zzco.zzbo();
            }
        } else if (i5 > i2 || i6 != i4) {
            throw zzco.zzbo();
        }
        return i5;
    }

    public static <T> zzds<T> zza(Class<T> cls, zzdm zzdmVar, zzdw zzdwVar, zzcy zzcyVar, zzex<?, ?> zzexVar, zzbu<?> zzbuVar, zzdj zzdjVar) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        if (!(zzdmVar instanceof zzec)) {
            ((zzes) zzdmVar).zzcf();
            throw new NoSuchMethodError();
        }
        zzec zzecVar = (zzec) zzdmVar;
        boolean z = zzecVar.zzcf() == zzcg.zzg.zzkm;
        if (zzecVar.getFieldCount() == 0) {
            i3 = 0;
            i2 = 0;
            i = 0;
        } else {
            int zzcp = zzecVar.zzcp();
            int zzcq = zzecVar.zzcq();
            i3 = zzecVar.zzcu();
            i2 = zzcp;
            i = zzcq;
        }
        int[] iArr = new int[i3 << 2];
        Object[] objArr = new Object[i3 << 1];
        int[] iArr2 = zzecVar.zzcr() > 0 ? new int[zzecVar.zzcr()] : null;
        int[] iArr3 = zzecVar.zzcs() > 0 ? new int[zzecVar.zzcs()] : null;
        zzed zzco = zzecVar.zzco();
        if (zzco.next()) {
            int zzcx = zzco.zzcx();
            int i7 = 0;
            int i8 = 0;
            int i9 = 0;
            while (true) {
                if (zzcx >= zzecVar.zzcv() || i7 >= ((zzcx - i2) << 2)) {
                    if (zzco.zzda()) {
                        i6 = (int) zzfd.zza(zzco.zzdb());
                        i4 = (int) zzfd.zza(zzco.zzdc());
                        i5 = 0;
                    } else {
                        i6 = (int) zzfd.zza(zzco.zzdd());
                        if (zzco.zzde()) {
                            i4 = (int) zzfd.zza(zzco.zzdf());
                            i5 = zzco.zzdg();
                        } else {
                            i5 = 0;
                            i4 = 0;
                        }
                    }
                    iArr[i7] = zzco.zzcx();
                    int i10 = i7 + 1;
                    iArr[i10] = (zzco.zzdi() ? 536870912 : 0) | (zzco.zzdh() ? 268435456 : 0) | (zzco.zzcy() << 20) | i6;
                    iArr[i7 + 2] = (i5 << 20) | i4;
                    if (zzco.zzdl() != null) {
                        int i11 = (i7 / 4) << 1;
                        objArr[i11] = zzco.zzdl();
                        if (zzco.zzdj() != null) {
                            objArr[i11 + 1] = zzco.zzdj();
                        } else if (zzco.zzdk() != null) {
                            objArr[i11 + 1] = zzco.zzdk();
                        }
                    } else if (zzco.zzdj() != null) {
                        objArr[((i7 / 4) << 1) + 1] = zzco.zzdj();
                    } else if (zzco.zzdk() != null) {
                        objArr[((i7 / 4) << 1) + 1] = zzco.zzdk();
                    }
                    int zzcy = zzco.zzcy();
                    if (zzcy == zzcb.MAP.ordinal()) {
                        iArr2[i8] = i7;
                        i8++;
                    } else if (zzcy >= 18 && zzcy <= 49) {
                        iArr3[i9] = iArr[i10] & 1048575;
                        i9++;
                    }
                    if (!zzco.next()) {
                        break;
                    }
                    zzcx = zzco.zzcx();
                } else {
                    for (int i12 = 0; i12 < 4; i12++) {
                        iArr[i7 + i12] = -1;
                    }
                }
                i7 += 4;
            }
        }
        return new zzds<>(iArr, objArr, i2, i, zzecVar.zzcv(), zzecVar.zzch(), z, false, zzecVar.zzct(), iArr2, iArr3, zzdwVar, zzcyVar, zzexVar, zzbuVar, zzdjVar);
    }

    private final <K, V, UT, UB> UB zza(int i, int i2, Map<K, V> map, zzck<?> zzckVar, UB ub, zzex<UT, UB> zzexVar) {
        zzdh<?, ?> zzl = this.zzmz.zzl(zzae(i));
        Iterator<Map.Entry<K, V>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<K, V> next = it.next();
            if (zzckVar.zzb(((Integer) next.getValue()).intValue()) == null) {
                if (ub == null) {
                    ub = zzexVar.zzdz();
                }
                zzbg zzk = zzbb.zzk(zzdg.zza(zzl, next.getKey(), next.getValue()));
                try {
                    zzdg.zza(zzk.zzae(), zzl, next.getKey(), next.getValue());
                    zzexVar.zza((zzex<UT, UB>) ub, i2, zzk.zzad());
                    it.remove();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return ub;
    }

    private static void zza(int i, Object obj, zzfr zzfrVar) throws IOException {
        if (obj instanceof String) {
            zzfrVar.zza(i, (String) obj);
        } else {
            zzfrVar.zza(i, (zzbb) obj);
        }
    }

    private static <UT, UB> void zza(zzex<UT, UB> zzexVar, T t, zzfr zzfrVar) throws IOException {
        zzexVar.zza(zzexVar.zzq(t), zzfrVar);
    }

    private final <K, V> void zza(zzfr zzfrVar, int i, Object obj, int i2) throws IOException {
        if (obj != null) {
            zzfrVar.zza(i, this.zzmz.zzl(zzae(i2)), this.zzmz.zzh(obj));
        }
    }

    private final void zza(T t, T t2, int i) {
        long zzag = zzag(i) & 1048575;
        if (!zza((zzds<T>) t2, i)) {
            return;
        }
        Object zzo = zzfd.zzo(t, zzag);
        Object zzo2 = zzfd.zzo(t2, zzag);
        if (zzo != null && zzo2 != null) {
            zzfd.zza(t, zzag, zzci.zza(zzo, zzo2));
            zzb((zzds<T>) t, i);
        } else if (zzo2 == null) {
        } else {
            zzfd.zza(t, zzag, zzo2);
            zzb((zzds<T>) t, i);
        }
    }

    private final boolean zza(T t, int i) {
        if (!this.zzmq) {
            int zzah = zzah(i);
            return (zzfd.zzj(t, (long) (zzah & 1048575)) & (1 << (zzah >>> 20))) != 0;
        }
        int zzag = zzag(i);
        long j = zzag & 1048575;
        switch ((zzag & 267386880) >>> 20) {
            case 0:
                return zzfd.zzn(t, j) != 0.0d;
            case 1:
                return zzfd.zzm(t, j) != 0.0f;
            case 2:
                return zzfd.zzk(t, j) != 0;
            case 3:
                return zzfd.zzk(t, j) != 0;
            case 4:
                return zzfd.zzj(t, j) != 0;
            case 5:
                return zzfd.zzk(t, j) != 0;
            case 6:
                return zzfd.zzj(t, j) != 0;
            case 7:
                return zzfd.zzl(t, j);
            case 8:
                Object zzo = zzfd.zzo(t, j);
                if (zzo instanceof String) {
                    return !((String) zzo).isEmpty();
                } else if (!(zzo instanceof zzbb)) {
                    throw new IllegalArgumentException();
                } else {
                    return !zzbb.zzfi.equals(zzo);
                }
            case 9:
                return zzfd.zzo(t, j) != null;
            case 10:
                return !zzbb.zzfi.equals(zzfd.zzo(t, j));
            case 11:
                return zzfd.zzj(t, j) != 0;
            case 12:
                return zzfd.zzj(t, j) != 0;
            case 13:
                return zzfd.zzj(t, j) != 0;
            case 14:
                return zzfd.zzk(t, j) != 0;
            case 15:
                return zzfd.zzj(t, j) != 0;
            case 16:
                return zzfd.zzk(t, j) != 0;
            case 17:
                return zzfd.zzo(t, j) != null;
            default:
                throw new IllegalArgumentException();
        }
    }

    private final boolean zza(T t, int i, int i2) {
        return zzfd.zzj(t, (long) (zzah(i2) & 1048575)) == i;
    }

    private final boolean zza(T t, int i, int i2, int i3) {
        return this.zzmq ? zza((zzds<T>) t, i) : (i2 & i3) != 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static boolean zza(Object obj, int i, zzef zzefVar) {
        return zzefVar.zzo(zzfd.zzo(obj, i & 1048575));
    }

    private final zzef zzad(int i) {
        int i2 = (i / 4) << 1;
        zzef zzefVar = (zzef) this.zzmj[i2];
        if (zzefVar != null) {
            return zzefVar;
        }
        zzef<T> zze = zzea.zzcm().zze((Class) this.zzmj[i2 + 1]);
        this.zzmj[i2] = zze;
        return zze;
    }

    private final Object zzae(int i) {
        return this.zzmj[(i / 4) << 1];
    }

    private final zzck<?> zzaf(int i) {
        return (zzck) this.zzmj[((i / 4) << 1) + 1];
    }

    private final int zzag(int i) {
        return this.zzmi[i + 1];
    }

    private final int zzah(int i) {
        return this.zzmi[i + 2];
    }

    private final int zzai(int i) {
        int i2 = this.zzmk;
        if (i >= i2) {
            int i3 = this.zzmm;
            if (i < i3) {
                int i4 = (i - i2) << 2;
                if (this.zzmi[i4] != i) {
                    return -1;
                }
                return i4;
            } else if (i <= this.zzml) {
                int i5 = i3 - i2;
                int length = (this.zzmi.length / 4) - 1;
                while (i5 <= length) {
                    int i6 = (length + i5) >>> 1;
                    int i7 = i6 << 2;
                    int i8 = this.zzmi[i7];
                    if (i == i8) {
                        return i7;
                    }
                    if (i < i8) {
                        length = i6 - 1;
                    } else {
                        i5 = i6 + 1;
                    }
                }
            }
        }
        return -1;
    }

    private final void zzb(T t, int i) {
        if (this.zzmq) {
            return;
        }
        int zzah = zzah(i);
        long j = zzah & 1048575;
        zzfd.zza((Object) t, j, zzfd.zzj(t, j) | (1 << (zzah >>> 20)));
    }

    private final void zzb(T t, int i, int i2) {
        zzfd.zza((Object) t, zzah(i2) & 1048575, i);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x002d  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x0494  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void zzb(T t, zzfr zzfrVar) throws IOException {
        Map.Entry<?, Object> entry;
        Iterator<Map.Entry<?, Object>> it;
        int length;
        int i;
        int i2;
        int i3;
        if (this.zzmo) {
            zzby<?> zza = this.zzmy.zza(t);
            if (!zza.isEmpty()) {
                it = zza.iterator();
                entry = it.next();
                int i4 = -1;
                length = this.zzmi.length;
                Unsafe unsafe = zzmh;
                i = 0;
                int i5 = 0;
                while (i < length) {
                    int zzag = zzag(i);
                    int[] iArr = this.zzmi;
                    int i6 = iArr[i];
                    int i7 = (267386880 & zzag) >>> 20;
                    if (this.zzmq || i7 > 17) {
                        i2 = i;
                        i3 = 0;
                    } else {
                        int i8 = iArr[i + 2];
                        int i9 = i8 & 1048575;
                        i2 = i;
                        if (i9 != i4) {
                            i5 = unsafe.getInt(t, i9);
                            i4 = i9;
                        }
                        i3 = 1 << (i8 >>> 20);
                    }
                    while (entry != null && this.zzmy.zza(entry) <= i6) {
                        this.zzmy.zza(zzfrVar, entry);
                        entry = it.hasNext() ? it.next() : null;
                    }
                    long j = zzag & 1048575;
                    int i10 = i2;
                    switch (i7) {
                        case 0:
                            if ((i3 & i5) != 0) {
                                zzfrVar.zza(i6, zzfd.zzn(t, j));
                                continue;
                            }
                            i = i10 + 4;
                        case 1:
                            if ((i3 & i5) != 0) {
                                zzfrVar.zza(i6, zzfd.zzm(t, j));
                            } else {
                                continue;
                            }
                            i = i10 + 4;
                        case 2:
                            if ((i3 & i5) != 0) {
                                zzfrVar.zzi(i6, unsafe.getLong(t, j));
                            } else {
                                continue;
                            }
                            i = i10 + 4;
                        case 3:
                            if ((i3 & i5) != 0) {
                                zzfrVar.zza(i6, unsafe.getLong(t, j));
                            } else {
                                continue;
                            }
                            i = i10 + 4;
                        case 4:
                            if ((i3 & i5) != 0) {
                                zzfrVar.zzc(i6, unsafe.getInt(t, j));
                            } else {
                                continue;
                            }
                            i = i10 + 4;
                        case 5:
                            if ((i3 & i5) != 0) {
                                zzfrVar.zzc(i6, unsafe.getLong(t, j));
                            } else {
                                continue;
                            }
                            i = i10 + 4;
                        case 6:
                            if ((i3 & i5) != 0) {
                                zzfrVar.zzf(i6, unsafe.getInt(t, j));
                            } else {
                                continue;
                            }
                            i = i10 + 4;
                        case 7:
                            if ((i3 & i5) != 0) {
                                zzfrVar.zzb(i6, zzfd.zzl(t, j));
                            } else {
                                continue;
                            }
                            i = i10 + 4;
                        case 8:
                            if ((i3 & i5) != 0) {
                                zza(i6, unsafe.getObject(t, j), zzfrVar);
                            } else {
                                continue;
                            }
                            i = i10 + 4;
                        case 9:
                            if ((i3 & i5) != 0) {
                                zzfrVar.zza(i6, unsafe.getObject(t, j), zzad(i10));
                            } else {
                                continue;
                            }
                            i = i10 + 4;
                        case 10:
                            if ((i3 & i5) != 0) {
                                zzfrVar.zza(i6, (zzbb) unsafe.getObject(t, j));
                            } else {
                                continue;
                            }
                            i = i10 + 4;
                        case 11:
                            if ((i3 & i5) != 0) {
                                zzfrVar.zzd(i6, unsafe.getInt(t, j));
                            } else {
                                continue;
                            }
                            i = i10 + 4;
                        case 12:
                            if ((i3 & i5) != 0) {
                                zzfrVar.zzn(i6, unsafe.getInt(t, j));
                            } else {
                                continue;
                            }
                            i = i10 + 4;
                        case 13:
                            if ((i3 & i5) != 0) {
                                zzfrVar.zzm(i6, unsafe.getInt(t, j));
                            } else {
                                continue;
                            }
                            i = i10 + 4;
                        case 14:
                            if ((i3 & i5) != 0) {
                                zzfrVar.zzj(i6, unsafe.getLong(t, j));
                            } else {
                                continue;
                            }
                            i = i10 + 4;
                        case 15:
                            if ((i3 & i5) != 0) {
                                zzfrVar.zze(i6, unsafe.getInt(t, j));
                            } else {
                                continue;
                            }
                            i = i10 + 4;
                        case 16:
                            if ((i3 & i5) != 0) {
                                zzfrVar.zzb(i6, unsafe.getLong(t, j));
                            } else {
                                continue;
                            }
                            i = i10 + 4;
                        case 17:
                            if ((i3 & i5) != 0) {
                                zzfrVar.zzb(i6, unsafe.getObject(t, j), zzad(i10));
                            } else {
                                continue;
                            }
                            i = i10 + 4;
                        case 18:
                            zzeh.zza(this.zzmi[i10], (List<Double>) unsafe.getObject(t, j), zzfrVar, false);
                            continue;
                            i = i10 + 4;
                        case 19:
                            zzeh.zzb(this.zzmi[i10], (List<Float>) unsafe.getObject(t, j), zzfrVar, false);
                            continue;
                            i = i10 + 4;
                        case 20:
                            zzeh.zzc(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, false);
                            continue;
                            i = i10 + 4;
                        case 21:
                            zzeh.zzd(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, false);
                            continue;
                            i = i10 + 4;
                        case 22:
                            zzeh.zzh(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, false);
                            continue;
                            i = i10 + 4;
                        case 23:
                            zzeh.zzf(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, false);
                            continue;
                            i = i10 + 4;
                        case 24:
                            zzeh.zzk(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, false);
                            continue;
                            i = i10 + 4;
                        case 25:
                            zzeh.zzn(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, false);
                            continue;
                            i = i10 + 4;
                        case 26:
                            zzeh.zza(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar);
                            break;
                        case 27:
                            zzeh.zza(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, zzad(i10));
                            break;
                        case 28:
                            zzeh.zzb(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar);
                            break;
                        case 29:
                            zzeh.zzi(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, false);
                            continue;
                            i = i10 + 4;
                        case 30:
                            zzeh.zzm(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, false);
                            continue;
                            i = i10 + 4;
                        case 31:
                            zzeh.zzl(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, false);
                            continue;
                            i = i10 + 4;
                        case 32:
                            zzeh.zzg(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, false);
                            continue;
                            i = i10 + 4;
                        case 33:
                            zzeh.zzj(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, false);
                            continue;
                            i = i10 + 4;
                        case 34:
                            zzeh.zze(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, false);
                            continue;
                            i = i10 + 4;
                        case 35:
                            zzeh.zza(this.zzmi[i10], (List<Double>) unsafe.getObject(t, j), zzfrVar, true);
                            break;
                        case 36:
                            zzeh.zzb(this.zzmi[i10], (List<Float>) unsafe.getObject(t, j), zzfrVar, true);
                            break;
                        case 37:
                            zzeh.zzc(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, true);
                            break;
                        case 38:
                            zzeh.zzd(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, true);
                            break;
                        case 39:
                            zzeh.zzh(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, true);
                            break;
                        case 40:
                            zzeh.zzf(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, true);
                            break;
                        case 41:
                            zzeh.zzk(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, true);
                            break;
                        case 42:
                            zzeh.zzn(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, true);
                            break;
                        case 43:
                            zzeh.zzi(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, true);
                            break;
                        case 44:
                            zzeh.zzm(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, true);
                            break;
                        case 45:
                            zzeh.zzl(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, true);
                            break;
                        case 46:
                            zzeh.zzg(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, true);
                            break;
                        case 47:
                            zzeh.zzj(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, true);
                            break;
                        case 48:
                            zzeh.zze(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, true);
                            break;
                        case 49:
                            zzeh.zzb(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, zzad(i10));
                            break;
                        case 50:
                            zza(zzfrVar, i6, unsafe.getObject(t, j), i10);
                            break;
                        case 51:
                            if (zza((zzds<T>) t, i6, i10)) {
                                zzfrVar.zza(i6, zze(t, j));
                                break;
                            }
                            break;
                        case 52:
                            if (zza((zzds<T>) t, i6, i10)) {
                                zzfrVar.zza(i6, zzf(t, j));
                                break;
                            }
                            break;
                        case 53:
                            if (zza((zzds<T>) t, i6, i10)) {
                                zzfrVar.zzi(i6, zzh(t, j));
                                break;
                            }
                            break;
                        case 54:
                            if (zza((zzds<T>) t, i6, i10)) {
                                zzfrVar.zza(i6, zzh(t, j));
                                break;
                            }
                            break;
                        case 55:
                            if (zza((zzds<T>) t, i6, i10)) {
                                zzfrVar.zzc(i6, zzg(t, j));
                                break;
                            }
                            break;
                        case 56:
                            if (zza((zzds<T>) t, i6, i10)) {
                                zzfrVar.zzc(i6, zzh(t, j));
                                break;
                            }
                            break;
                        case 57:
                            if (zza((zzds<T>) t, i6, i10)) {
                                zzfrVar.zzf(i6, zzg(t, j));
                                break;
                            }
                            break;
                        case 58:
                            if (zza((zzds<T>) t, i6, i10)) {
                                zzfrVar.zzb(i6, zzi(t, j));
                                break;
                            }
                            break;
                        case 59:
                            if (zza((zzds<T>) t, i6, i10)) {
                                zza(i6, unsafe.getObject(t, j), zzfrVar);
                                break;
                            }
                            break;
                        case 60:
                            if (zza((zzds<T>) t, i6, i10)) {
                                zzfrVar.zza(i6, unsafe.getObject(t, j), zzad(i10));
                                break;
                            }
                            break;
                        case 61:
                            if (zza((zzds<T>) t, i6, i10)) {
                                zzfrVar.zza(i6, (zzbb) unsafe.getObject(t, j));
                                break;
                            }
                            break;
                        case 62:
                            if (zza((zzds<T>) t, i6, i10)) {
                                zzfrVar.zzd(i6, zzg(t, j));
                                break;
                            }
                            break;
                        case 63:
                            if (zza((zzds<T>) t, i6, i10)) {
                                zzfrVar.zzn(i6, zzg(t, j));
                                break;
                            }
                            break;
                        case 64:
                            if (zza((zzds<T>) t, i6, i10)) {
                                zzfrVar.zzm(i6, zzg(t, j));
                                break;
                            }
                            break;
                        case 65:
                            if (zza((zzds<T>) t, i6, i10)) {
                                zzfrVar.zzj(i6, zzh(t, j));
                                break;
                            }
                            break;
                        case 66:
                            if (zza((zzds<T>) t, i6, i10)) {
                                zzfrVar.zze(i6, zzg(t, j));
                                break;
                            }
                            break;
                        case 67:
                            if (zza((zzds<T>) t, i6, i10)) {
                                zzfrVar.zzb(i6, zzh(t, j));
                                break;
                            }
                            break;
                        case 68:
                            if (zza((zzds<T>) t, i6, i10)) {
                                zzfrVar.zzb(i6, unsafe.getObject(t, j), zzad(i10));
                                break;
                            }
                            break;
                    }
                    i = i10 + 4;
                }
                while (entry != null) {
                    this.zzmy.zza(zzfrVar, entry);
                    entry = it.hasNext() ? it.next() : null;
                }
                zza(this.zzmx, t, zzfrVar);
            }
        }
        it = null;
        entry = null;
        int i42 = -1;
        length = this.zzmi.length;
        Unsafe unsafe2 = zzmh;
        i = 0;
        int i52 = 0;
        while (i < length) {
        }
        while (entry != null) {
        }
        zza(this.zzmx, t, zzfrVar);
    }

    private final void zzb(T t, T t2, int i) {
        int zzag = zzag(i);
        int i2 = this.zzmi[i];
        long j = zzag & 1048575;
        if (!zza((zzds<T>) t2, i2, i)) {
            return;
        }
        Object zzo = zzfd.zzo(t, j);
        Object zzo2 = zzfd.zzo(t2, j);
        if (zzo != null && zzo2 != null) {
            zzfd.zza(t, j, zzci.zza(zzo, zzo2));
            zzb((zzds<T>) t, i2, i);
        } else if (zzo2 == null) {
        } else {
            zzfd.zza(t, j, zzo2);
            zzb((zzds<T>) t, i2, i);
        }
    }

    private final boolean zzc(T t, T t2, int i) {
        return zza((zzds<T>) t, i) == zza((zzds<T>) t2, i);
    }

    private static <E> List<E> zzd(Object obj, long j) {
        return (List) zzfd.zzo(obj, j);
    }

    private static <T> double zze(T t, long j) {
        return ((Double) zzfd.zzo(t, j)).doubleValue();
    }

    private static <T> float zzf(T t, long j) {
        return ((Float) zzfd.zzo(t, j)).floatValue();
    }

    private static <T> int zzg(T t, long j) {
        return ((Integer) zzfd.zzo(t, j)).intValue();
    }

    private static <T> long zzh(T t, long j) {
        return ((Long) zzfd.zzo(t, j)).longValue();
    }

    private static <T> boolean zzi(T t, long j) {
        return ((Boolean) zzfd.zzo(t, j)).booleanValue();
    }

    private static zzey zzn(Object obj) {
        zzcg zzcgVar = (zzcg) obj;
        zzey zzeyVar = zzcgVar.zzjp;
        if (zzeyVar == zzey.zzea()) {
            zzey zzeb = zzey.zzeb();
            zzcgVar.zzjp = zzeb;
            return zzeb;
        }
        return zzeyVar;
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0038, code lost:
        if (com.google.android.gms.internal.clearcut.zzeh.zzd(com.google.android.gms.internal.clearcut.zzfd.zzo(r10, r6), com.google.android.gms.internal.clearcut.zzfd.zzo(r11, r6)) != false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x005c, code lost:
        if (com.google.android.gms.internal.clearcut.zzeh.zzd(com.google.android.gms.internal.clearcut.zzfd.zzo(r10, r6), com.google.android.gms.internal.clearcut.zzfd.zzo(r11, r6)) != false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0070, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzk(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzk(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0082, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzj(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzj(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0096, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzk(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzk(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00a8, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzj(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzj(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00ba, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzj(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzj(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00cc, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzj(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzj(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00e2, code lost:
        if (com.google.android.gms.internal.clearcut.zzeh.zzd(com.google.android.gms.internal.clearcut.zzfd.zzo(r10, r6), com.google.android.gms.internal.clearcut.zzfd.zzo(r11, r6)) != false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00f8, code lost:
        if (com.google.android.gms.internal.clearcut.zzeh.zzd(com.google.android.gms.internal.clearcut.zzfd.zzo(r10, r6), com.google.android.gms.internal.clearcut.zzfd.zzo(r11, r6)) != false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x010e, code lost:
        if (com.google.android.gms.internal.clearcut.zzeh.zzd(com.google.android.gms.internal.clearcut.zzfd.zzo(r10, r6), com.google.android.gms.internal.clearcut.zzfd.zzo(r11, r6)) != false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0120, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzl(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzl(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0132, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzj(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzj(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0145, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzk(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzk(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0156, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzj(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzj(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0169, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzk(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzk(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x017c, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzk(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzk(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x018d, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzj(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzj(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x01a0, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzk(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzk(r11, r6)) goto L85;
     */
    @Override // com.google.android.gms.internal.clearcut.zzef
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final boolean equals(T t, T t2) {
        int length = this.zzmi.length;
        int i = 0;
        while (true) {
            boolean z = true;
            if (i >= length) {
                if (!this.zzmx.zzq(t).equals(this.zzmx.zzq(t2))) {
                    return false;
                }
                if (!this.zzmo) {
                    return true;
                }
                return this.zzmy.zza(t).equals(this.zzmy.zza(t2));
            }
            int zzag = zzag(i);
            long j = zzag & 1048575;
            switch ((zzag & 267386880) >>> 20) {
                case 0:
                    if (zzc(t, t2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 1:
                    if (zzc(t, t2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 2:
                    if (zzc(t, t2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 3:
                    if (zzc(t, t2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 4:
                    if (zzc(t, t2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 5:
                    if (zzc(t, t2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 6:
                    if (zzc(t, t2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 7:
                    if (zzc(t, t2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 8:
                    if (zzc(t, t2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 9:
                    if (zzc(t, t2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 10:
                    if (zzc(t, t2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 11:
                    if (zzc(t, t2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 12:
                    if (zzc(t, t2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 13:
                    if (zzc(t, t2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 14:
                    if (zzc(t, t2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 15:
                    if (zzc(t, t2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 16:
                    if (zzc(t, t2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 17:
                    if (zzc(t, t2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                case 50:
                    z = zzeh.zzd(zzfd.zzo(t, j), zzfd.zzo(t2, j));
                    break;
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                case 56:
                case 57:
                case 58:
                case 59:
                case 60:
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                case 68:
                    long zzah = zzah(i) & 1048575;
                    if (zzfd.zzj(t, zzah) == zzfd.zzj(t2, zzah)) {
                        break;
                    }
                    z = false;
                    break;
            }
            if (!z) {
                return false;
            }
            i += 4;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00ce, code lost:
        if (r3 != null) goto L54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00e0, code lost:
        if (r3 != null) goto L54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00e2, code lost:
        r7 = r3.hashCode();
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00e6, code lost:
        r2 = (r2 * 53) + r7;
     */
    @Override // com.google.android.gms.internal.clearcut.zzef
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final int hashCode(T t) {
        int i;
        int i2;
        long j;
        double d;
        float f;
        boolean z;
        Object obj;
        Object obj2;
        int length = this.zzmi.length;
        int i3 = 0;
        for (int i4 = 0; i4 < length; i4 += 4) {
            int zzag = zzag(i4);
            int i5 = this.zzmi[i4];
            long j2 = 1048575 & zzag;
            int i6 = 37;
            switch ((zzag & 267386880) >>> 20) {
                case 0:
                    i2 = i3 * 53;
                    d = zzfd.zzn(t, j2);
                    j = Double.doubleToLongBits(d);
                    i = zzci.zzl(j);
                    i3 = i2 + i;
                    break;
                case 1:
                    i2 = i3 * 53;
                    f = zzfd.zzm(t, j2);
                    i = Float.floatToIntBits(f);
                    i3 = i2 + i;
                    break;
                case 2:
                case 3:
                case 5:
                case 14:
                case 16:
                    i2 = i3 * 53;
                    j = zzfd.zzk(t, j2);
                    i = zzci.zzl(j);
                    i3 = i2 + i;
                    break;
                case 4:
                case 6:
                case 11:
                case 12:
                case 13:
                case 15:
                    i2 = i3 * 53;
                    i = zzfd.zzj(t, j2);
                    i3 = i2 + i;
                    break;
                case 7:
                    i2 = i3 * 53;
                    z = zzfd.zzl(t, j2);
                    i = zzci.zzc(z);
                    i3 = i2 + i;
                    break;
                case 8:
                    i2 = i3 * 53;
                    i = ((String) zzfd.zzo(t, j2)).hashCode();
                    i3 = i2 + i;
                    break;
                case 9:
                    obj = zzfd.zzo(t, j2);
                    break;
                case 10:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                case 50:
                    i2 = i3 * 53;
                    obj2 = zzfd.zzo(t, j2);
                    i = obj2.hashCode();
                    i3 = i2 + i;
                    break;
                case 17:
                    obj = zzfd.zzo(t, j2);
                    break;
                case 51:
                    if (zza((zzds<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        d = zze(t, j2);
                        j = Double.doubleToLongBits(d);
                        i = zzci.zzl(j);
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 52:
                    if (zza((zzds<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        f = zzf(t, j2);
                        i = Float.floatToIntBits(f);
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 53:
                    if (!zza((zzds<T>) t, i5, i4)) {
                        break;
                    }
                    i2 = i3 * 53;
                    j = zzh(t, j2);
                    i = zzci.zzl(j);
                    i3 = i2 + i;
                    break;
                case 54:
                    if (!zza((zzds<T>) t, i5, i4)) {
                        break;
                    }
                    i2 = i3 * 53;
                    j = zzh(t, j2);
                    i = zzci.zzl(j);
                    i3 = i2 + i;
                    break;
                case 55:
                    if (!zza((zzds<T>) t, i5, i4)) {
                        break;
                    }
                    i2 = i3 * 53;
                    i = zzg(t, j2);
                    i3 = i2 + i;
                    break;
                case 56:
                    if (!zza((zzds<T>) t, i5, i4)) {
                        break;
                    }
                    i2 = i3 * 53;
                    j = zzh(t, j2);
                    i = zzci.zzl(j);
                    i3 = i2 + i;
                    break;
                case 57:
                    if (!zza((zzds<T>) t, i5, i4)) {
                        break;
                    }
                    i2 = i3 * 53;
                    i = zzg(t, j2);
                    i3 = i2 + i;
                    break;
                case 58:
                    if (zza((zzds<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        z = zzi(t, j2);
                        i = zzci.zzc(z);
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 59:
                    if (!zza((zzds<T>) t, i5, i4)) {
                        break;
                    }
                    i2 = i3 * 53;
                    i = ((String) zzfd.zzo(t, j2)).hashCode();
                    i3 = i2 + i;
                    break;
                case 60:
                    if (!zza((zzds<T>) t, i5, i4)) {
                        break;
                    }
                    obj2 = zzfd.zzo(t, j2);
                    i2 = i3 * 53;
                    i = obj2.hashCode();
                    i3 = i2 + i;
                    break;
                case 61:
                    if (!zza((zzds<T>) t, i5, i4)) {
                        break;
                    }
                    i2 = i3 * 53;
                    obj2 = zzfd.zzo(t, j2);
                    i = obj2.hashCode();
                    i3 = i2 + i;
                    break;
                case 62:
                    if (!zza((zzds<T>) t, i5, i4)) {
                        break;
                    }
                    i2 = i3 * 53;
                    i = zzg(t, j2);
                    i3 = i2 + i;
                    break;
                case 63:
                    if (!zza((zzds<T>) t, i5, i4)) {
                        break;
                    }
                    i2 = i3 * 53;
                    i = zzg(t, j2);
                    i3 = i2 + i;
                    break;
                case 64:
                    if (!zza((zzds<T>) t, i5, i4)) {
                        break;
                    }
                    i2 = i3 * 53;
                    i = zzg(t, j2);
                    i3 = i2 + i;
                    break;
                case 65:
                    if (!zza((zzds<T>) t, i5, i4)) {
                        break;
                    }
                    i2 = i3 * 53;
                    j = zzh(t, j2);
                    i = zzci.zzl(j);
                    i3 = i2 + i;
                    break;
                case 66:
                    if (!zza((zzds<T>) t, i5, i4)) {
                        break;
                    }
                    i2 = i3 * 53;
                    i = zzg(t, j2);
                    i3 = i2 + i;
                    break;
                case 67:
                    if (!zza((zzds<T>) t, i5, i4)) {
                        break;
                    }
                    i2 = i3 * 53;
                    j = zzh(t, j2);
                    i = zzci.zzl(j);
                    i3 = i2 + i;
                    break;
                case 68:
                    if (!zza((zzds<T>) t, i5, i4)) {
                        break;
                    }
                    obj2 = zzfd.zzo(t, j2);
                    i2 = i3 * 53;
                    i = obj2.hashCode();
                    i3 = i2 + i;
                    break;
            }
        }
        int hashCode = (i3 * 53) + this.zzmx.zzq(t).hashCode();
        return this.zzmo ? (hashCode * 53) + this.zzmy.zza(t).hashCode() : hashCode;
    }

    @Override // com.google.android.gms.internal.clearcut.zzef
    public final T newInstance() {
        return (T) this.zzmv.newInstance(this.zzmn);
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0039  */
    /* JADX WARN: Removed duplicated region for block: B:175:0x04b9  */
    /* JADX WARN: Removed duplicated region for block: B:190:0x04f6  */
    /* JADX WARN: Removed duplicated region for block: B:353:0x0976  */
    @Override // com.google.android.gms.internal.clearcut.zzef
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void zza(T t, zzfr zzfrVar) throws IOException {
        Map.Entry<?, Object> entry;
        Iterator<Map.Entry<?, Object>> it;
        int length;
        int i;
        double d;
        float f;
        long j;
        long j2;
        int i2;
        long j3;
        int i3;
        boolean z;
        int i4;
        int i5;
        int i6;
        long j4;
        int i7;
        long j5;
        Map.Entry<?, Object> entry2;
        Iterator<Map.Entry<?, Object>> it2;
        int length2;
        double d2;
        float f2;
        long j6;
        long j7;
        int i8;
        long j8;
        int i9;
        boolean z2;
        int i10;
        int i11;
        int i12;
        long j9;
        int i13;
        long j10;
        if (zzfrVar.zzaj() == zzcg.zzg.zzkp) {
            zza(this.zzmx, t, zzfrVar);
            if (this.zzmo) {
                zzby<?> zza = this.zzmy.zza(t);
                if (!zza.isEmpty()) {
                    it2 = zza.descendingIterator();
                    entry2 = it2.next();
                    for (length2 = this.zzmi.length - 4; length2 >= 0; length2 -= 4) {
                        int zzag = zzag(length2);
                        int i14 = this.zzmi[length2];
                        while (entry2 != null && this.zzmy.zza(entry2) > i14) {
                            this.zzmy.zza(zzfrVar, entry2);
                            entry2 = it2.hasNext() ? it2.next() : null;
                        }
                        switch ((zzag & 267386880) >>> 20) {
                            case 0:
                                if (zza((zzds<T>) t, length2)) {
                                    d2 = zzfd.zzn(t, zzag & 1048575);
                                    zzfrVar.zza(i14, d2);
                                    break;
                                } else {
                                    break;
                                }
                            case 1:
                                if (zza((zzds<T>) t, length2)) {
                                    f2 = zzfd.zzm(t, zzag & 1048575);
                                    zzfrVar.zza(i14, f2);
                                    break;
                                } else {
                                    break;
                                }
                            case 2:
                                if (zza((zzds<T>) t, length2)) {
                                    j6 = zzfd.zzk(t, zzag & 1048575);
                                    zzfrVar.zzi(i14, j6);
                                    break;
                                } else {
                                    break;
                                }
                            case 3:
                                if (zza((zzds<T>) t, length2)) {
                                    j7 = zzfd.zzk(t, zzag & 1048575);
                                    zzfrVar.zza(i14, j7);
                                    break;
                                } else {
                                    break;
                                }
                            case 4:
                                if (zza((zzds<T>) t, length2)) {
                                    i8 = zzfd.zzj(t, zzag & 1048575);
                                    zzfrVar.zzc(i14, i8);
                                    break;
                                } else {
                                    break;
                                }
                            case 5:
                                if (zza((zzds<T>) t, length2)) {
                                    j8 = zzfd.zzk(t, zzag & 1048575);
                                    zzfrVar.zzc(i14, j8);
                                    break;
                                } else {
                                    break;
                                }
                            case 6:
                                if (zza((zzds<T>) t, length2)) {
                                    i9 = zzfd.zzj(t, zzag & 1048575);
                                    zzfrVar.zzf(i14, i9);
                                    break;
                                } else {
                                    break;
                                }
                            case 7:
                                if (zza((zzds<T>) t, length2)) {
                                    z2 = zzfd.zzl(t, zzag & 1048575);
                                    zzfrVar.zzb(i14, z2);
                                    break;
                                } else {
                                    break;
                                }
                            case 8:
                                if (!zza((zzds<T>) t, length2)) {
                                    break;
                                }
                                zza(i14, zzfd.zzo(t, zzag & 1048575), zzfrVar);
                                break;
                            case 9:
                                if (!zza((zzds<T>) t, length2)) {
                                    break;
                                }
                                zzfrVar.zza(i14, zzfd.zzo(t, zzag & 1048575), zzad(length2));
                                break;
                            case 10:
                                if (!zza((zzds<T>) t, length2)) {
                                    break;
                                }
                                zzfrVar.zza(i14, (zzbb) zzfd.zzo(t, zzag & 1048575));
                                break;
                            case 11:
                                if (zza((zzds<T>) t, length2)) {
                                    i10 = zzfd.zzj(t, zzag & 1048575);
                                    zzfrVar.zzd(i14, i10);
                                    break;
                                } else {
                                    break;
                                }
                            case 12:
                                if (zza((zzds<T>) t, length2)) {
                                    i11 = zzfd.zzj(t, zzag & 1048575);
                                    zzfrVar.zzn(i14, i11);
                                    break;
                                } else {
                                    break;
                                }
                            case 13:
                                if (zza((zzds<T>) t, length2)) {
                                    i12 = zzfd.zzj(t, zzag & 1048575);
                                    zzfrVar.zzm(i14, i12);
                                    break;
                                } else {
                                    break;
                                }
                            case 14:
                                if (zza((zzds<T>) t, length2)) {
                                    j9 = zzfd.zzk(t, zzag & 1048575);
                                    zzfrVar.zzj(i14, j9);
                                    break;
                                } else {
                                    break;
                                }
                            case 15:
                                if (zza((zzds<T>) t, length2)) {
                                    i13 = zzfd.zzj(t, zzag & 1048575);
                                    zzfrVar.zze(i14, i13);
                                    break;
                                } else {
                                    break;
                                }
                            case 16:
                                if (zza((zzds<T>) t, length2)) {
                                    j10 = zzfd.zzk(t, zzag & 1048575);
                                    zzfrVar.zzb(i14, j10);
                                    break;
                                } else {
                                    break;
                                }
                            case 17:
                                if (!zza((zzds<T>) t, length2)) {
                                    break;
                                }
                                zzfrVar.zzb(i14, zzfd.zzo(t, zzag & 1048575), zzad(length2));
                                break;
                            case 18:
                                zzeh.zza(this.zzmi[length2], (List<Double>) zzfd.zzo(t, zzag & 1048575), zzfrVar, false);
                                break;
                            case 19:
                                zzeh.zzb(this.zzmi[length2], (List<Float>) zzfd.zzo(t, zzag & 1048575), zzfrVar, false);
                                break;
                            case 20:
                                zzeh.zzc(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, false);
                                break;
                            case 21:
                                zzeh.zzd(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, false);
                                break;
                            case 22:
                                zzeh.zzh(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, false);
                                break;
                            case 23:
                                zzeh.zzf(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, false);
                                break;
                            case 24:
                                zzeh.zzk(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, false);
                                break;
                            case 25:
                                zzeh.zzn(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, false);
                                break;
                            case 26:
                                zzeh.zza(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar);
                                break;
                            case 27:
                                zzeh.zza(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, zzad(length2));
                                break;
                            case 28:
                                zzeh.zzb(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar);
                                break;
                            case 29:
                                zzeh.zzi(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, false);
                                break;
                            case 30:
                                zzeh.zzm(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, false);
                                break;
                            case 31:
                                zzeh.zzl(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, false);
                                break;
                            case 32:
                                zzeh.zzg(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, false);
                                break;
                            case 33:
                                zzeh.zzj(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, false);
                                break;
                            case 34:
                                zzeh.zze(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, false);
                                break;
                            case 35:
                                zzeh.zza(this.zzmi[length2], (List<Double>) zzfd.zzo(t, zzag & 1048575), zzfrVar, true);
                                break;
                            case 36:
                                zzeh.zzb(this.zzmi[length2], (List<Float>) zzfd.zzo(t, zzag & 1048575), zzfrVar, true);
                                break;
                            case 37:
                                zzeh.zzc(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, true);
                                break;
                            case 38:
                                zzeh.zzd(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, true);
                                break;
                            case 39:
                                zzeh.zzh(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, true);
                                break;
                            case 40:
                                zzeh.zzf(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, true);
                                break;
                            case 41:
                                zzeh.zzk(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, true);
                                break;
                            case 42:
                                zzeh.zzn(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, true);
                                break;
                            case 43:
                                zzeh.zzi(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, true);
                                break;
                            case 44:
                                zzeh.zzm(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, true);
                                break;
                            case 45:
                                zzeh.zzl(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, true);
                                break;
                            case 46:
                                zzeh.zzg(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, true);
                                break;
                            case 47:
                                zzeh.zzj(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, true);
                                break;
                            case 48:
                                zzeh.zze(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, true);
                                break;
                            case 49:
                                zzeh.zzb(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, zzad(length2));
                                break;
                            case 50:
                                zza(zzfrVar, i14, zzfd.zzo(t, zzag & 1048575), length2);
                                break;
                            case 51:
                                if (zza((zzds<T>) t, i14, length2)) {
                                    d2 = zze(t, zzag & 1048575);
                                    zzfrVar.zza(i14, d2);
                                    break;
                                } else {
                                    break;
                                }
                            case 52:
                                if (zza((zzds<T>) t, i14, length2)) {
                                    f2 = zzf(t, zzag & 1048575);
                                    zzfrVar.zza(i14, f2);
                                    break;
                                } else {
                                    break;
                                }
                            case 53:
                                if (zza((zzds<T>) t, i14, length2)) {
                                    j6 = zzh(t, zzag & 1048575);
                                    zzfrVar.zzi(i14, j6);
                                    break;
                                } else {
                                    break;
                                }
                            case 54:
                                if (zza((zzds<T>) t, i14, length2)) {
                                    j7 = zzh(t, zzag & 1048575);
                                    zzfrVar.zza(i14, j7);
                                    break;
                                } else {
                                    break;
                                }
                            case 55:
                                if (zza((zzds<T>) t, i14, length2)) {
                                    i8 = zzg(t, zzag & 1048575);
                                    zzfrVar.zzc(i14, i8);
                                    break;
                                } else {
                                    break;
                                }
                            case 56:
                                if (zza((zzds<T>) t, i14, length2)) {
                                    j8 = zzh(t, zzag & 1048575);
                                    zzfrVar.zzc(i14, j8);
                                    break;
                                } else {
                                    break;
                                }
                            case 57:
                                if (zza((zzds<T>) t, i14, length2)) {
                                    i9 = zzg(t, zzag & 1048575);
                                    zzfrVar.zzf(i14, i9);
                                    break;
                                } else {
                                    break;
                                }
                            case 58:
                                if (zza((zzds<T>) t, i14, length2)) {
                                    z2 = zzi(t, zzag & 1048575);
                                    zzfrVar.zzb(i14, z2);
                                    break;
                                } else {
                                    break;
                                }
                            case 59:
                                if (!zza((zzds<T>) t, i14, length2)) {
                                    break;
                                }
                                zza(i14, zzfd.zzo(t, zzag & 1048575), zzfrVar);
                                break;
                            case 60:
                                if (!zza((zzds<T>) t, i14, length2)) {
                                    break;
                                }
                                zzfrVar.zza(i14, zzfd.zzo(t, zzag & 1048575), zzad(length2));
                                break;
                            case 61:
                                if (!zza((zzds<T>) t, i14, length2)) {
                                    break;
                                }
                                zzfrVar.zza(i14, (zzbb) zzfd.zzo(t, zzag & 1048575));
                                break;
                            case 62:
                                if (zza((zzds<T>) t, i14, length2)) {
                                    i10 = zzg(t, zzag & 1048575);
                                    zzfrVar.zzd(i14, i10);
                                    break;
                                } else {
                                    break;
                                }
                            case 63:
                                if (zza((zzds<T>) t, i14, length2)) {
                                    i11 = zzg(t, zzag & 1048575);
                                    zzfrVar.zzn(i14, i11);
                                    break;
                                } else {
                                    break;
                                }
                            case 64:
                                if (zza((zzds<T>) t, i14, length2)) {
                                    i12 = zzg(t, zzag & 1048575);
                                    zzfrVar.zzm(i14, i12);
                                    break;
                                } else {
                                    break;
                                }
                            case 65:
                                if (zza((zzds<T>) t, i14, length2)) {
                                    j9 = zzh(t, zzag & 1048575);
                                    zzfrVar.zzj(i14, j9);
                                    break;
                                } else {
                                    break;
                                }
                            case 66:
                                if (zza((zzds<T>) t, i14, length2)) {
                                    i13 = zzg(t, zzag & 1048575);
                                    zzfrVar.zze(i14, i13);
                                    break;
                                } else {
                                    break;
                                }
                            case 67:
                                if (zza((zzds<T>) t, i14, length2)) {
                                    j10 = zzh(t, zzag & 1048575);
                                    zzfrVar.zzb(i14, j10);
                                    break;
                                } else {
                                    break;
                                }
                            case 68:
                                if (!zza((zzds<T>) t, i14, length2)) {
                                    break;
                                }
                                zzfrVar.zzb(i14, zzfd.zzo(t, zzag & 1048575), zzad(length2));
                                break;
                        }
                    }
                    while (entry2 != null) {
                        this.zzmy.zza(zzfrVar, entry2);
                        entry2 = it2.hasNext() ? it2.next() : null;
                    }
                }
            }
            it2 = null;
            entry2 = null;
            while (length2 >= 0) {
            }
            while (entry2 != null) {
            }
        } else if (!this.zzmq) {
            zzb((zzds<T>) t, zzfrVar);
        } else {
            if (this.zzmo) {
                zzby<?> zza2 = this.zzmy.zza(t);
                if (!zza2.isEmpty()) {
                    it = zza2.iterator();
                    entry = it.next();
                    length = this.zzmi.length;
                    for (i = 0; i < length; i += 4) {
                        int zzag2 = zzag(i);
                        int i15 = this.zzmi[i];
                        while (entry != null && this.zzmy.zza(entry) <= i15) {
                            this.zzmy.zza(zzfrVar, entry);
                            entry = it.hasNext() ? it.next() : null;
                        }
                        switch ((zzag2 & 267386880) >>> 20) {
                            case 0:
                                if (zza((zzds<T>) t, i)) {
                                    d = zzfd.zzn(t, zzag2 & 1048575);
                                    zzfrVar.zza(i15, d);
                                    break;
                                } else {
                                    break;
                                }
                            case 1:
                                if (zza((zzds<T>) t, i)) {
                                    f = zzfd.zzm(t, zzag2 & 1048575);
                                    zzfrVar.zza(i15, f);
                                    break;
                                } else {
                                    break;
                                }
                            case 2:
                                if (zza((zzds<T>) t, i)) {
                                    j = zzfd.zzk(t, zzag2 & 1048575);
                                    zzfrVar.zzi(i15, j);
                                    break;
                                } else {
                                    break;
                                }
                            case 3:
                                if (zza((zzds<T>) t, i)) {
                                    j2 = zzfd.zzk(t, zzag2 & 1048575);
                                    zzfrVar.zza(i15, j2);
                                    break;
                                } else {
                                    break;
                                }
                            case 4:
                                if (zza((zzds<T>) t, i)) {
                                    i2 = zzfd.zzj(t, zzag2 & 1048575);
                                    zzfrVar.zzc(i15, i2);
                                    break;
                                } else {
                                    break;
                                }
                            case 5:
                                if (zza((zzds<T>) t, i)) {
                                    j3 = zzfd.zzk(t, zzag2 & 1048575);
                                    zzfrVar.zzc(i15, j3);
                                    break;
                                } else {
                                    break;
                                }
                            case 6:
                                if (zza((zzds<T>) t, i)) {
                                    i3 = zzfd.zzj(t, zzag2 & 1048575);
                                    zzfrVar.zzf(i15, i3);
                                    break;
                                } else {
                                    break;
                                }
                            case 7:
                                if (zza((zzds<T>) t, i)) {
                                    z = zzfd.zzl(t, zzag2 & 1048575);
                                    zzfrVar.zzb(i15, z);
                                    break;
                                } else {
                                    break;
                                }
                            case 8:
                                if (!zza((zzds<T>) t, i)) {
                                    break;
                                }
                                zza(i15, zzfd.zzo(t, zzag2 & 1048575), zzfrVar);
                                break;
                            case 9:
                                if (!zza((zzds<T>) t, i)) {
                                    break;
                                }
                                zzfrVar.zza(i15, zzfd.zzo(t, zzag2 & 1048575), zzad(i));
                                break;
                            case 10:
                                if (!zza((zzds<T>) t, i)) {
                                    break;
                                }
                                zzfrVar.zza(i15, (zzbb) zzfd.zzo(t, zzag2 & 1048575));
                                break;
                            case 11:
                                if (zza((zzds<T>) t, i)) {
                                    i4 = zzfd.zzj(t, zzag2 & 1048575);
                                    zzfrVar.zzd(i15, i4);
                                    break;
                                } else {
                                    break;
                                }
                            case 12:
                                if (zza((zzds<T>) t, i)) {
                                    i5 = zzfd.zzj(t, zzag2 & 1048575);
                                    zzfrVar.zzn(i15, i5);
                                    break;
                                } else {
                                    break;
                                }
                            case 13:
                                if (zza((zzds<T>) t, i)) {
                                    i6 = zzfd.zzj(t, zzag2 & 1048575);
                                    zzfrVar.zzm(i15, i6);
                                    break;
                                } else {
                                    break;
                                }
                            case 14:
                                if (zza((zzds<T>) t, i)) {
                                    j4 = zzfd.zzk(t, zzag2 & 1048575);
                                    zzfrVar.zzj(i15, j4);
                                    break;
                                } else {
                                    break;
                                }
                            case 15:
                                if (zza((zzds<T>) t, i)) {
                                    i7 = zzfd.zzj(t, zzag2 & 1048575);
                                    zzfrVar.zze(i15, i7);
                                    break;
                                } else {
                                    break;
                                }
                            case 16:
                                if (zza((zzds<T>) t, i)) {
                                    j5 = zzfd.zzk(t, zzag2 & 1048575);
                                    zzfrVar.zzb(i15, j5);
                                    break;
                                } else {
                                    break;
                                }
                            case 17:
                                if (!zza((zzds<T>) t, i)) {
                                    break;
                                }
                                zzfrVar.zzb(i15, zzfd.zzo(t, zzag2 & 1048575), zzad(i));
                                break;
                            case 18:
                                zzeh.zza(this.zzmi[i], (List<Double>) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, false);
                                break;
                            case 19:
                                zzeh.zzb(this.zzmi[i], (List<Float>) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, false);
                                break;
                            case 20:
                                zzeh.zzc(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, false);
                                break;
                            case 21:
                                zzeh.zzd(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, false);
                                break;
                            case 22:
                                zzeh.zzh(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, false);
                                break;
                            case 23:
                                zzeh.zzf(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, false);
                                break;
                            case 24:
                                zzeh.zzk(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, false);
                                break;
                            case 25:
                                zzeh.zzn(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, false);
                                break;
                            case 26:
                                zzeh.zza(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar);
                                break;
                            case 27:
                                zzeh.zza(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, zzad(i));
                                break;
                            case 28:
                                zzeh.zzb(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar);
                                break;
                            case 29:
                                zzeh.zzi(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, false);
                                break;
                            case 30:
                                zzeh.zzm(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, false);
                                break;
                            case 31:
                                zzeh.zzl(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, false);
                                break;
                            case 32:
                                zzeh.zzg(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, false);
                                break;
                            case 33:
                                zzeh.zzj(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, false);
                                break;
                            case 34:
                                zzeh.zze(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, false);
                                break;
                            case 35:
                                zzeh.zza(this.zzmi[i], (List<Double>) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, true);
                                break;
                            case 36:
                                zzeh.zzb(this.zzmi[i], (List<Float>) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, true);
                                break;
                            case 37:
                                zzeh.zzc(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, true);
                                break;
                            case 38:
                                zzeh.zzd(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, true);
                                break;
                            case 39:
                                zzeh.zzh(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, true);
                                break;
                            case 40:
                                zzeh.zzf(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, true);
                                break;
                            case 41:
                                zzeh.zzk(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, true);
                                break;
                            case 42:
                                zzeh.zzn(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, true);
                                break;
                            case 43:
                                zzeh.zzi(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, true);
                                break;
                            case 44:
                                zzeh.zzm(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, true);
                                break;
                            case 45:
                                zzeh.zzl(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, true);
                                break;
                            case 46:
                                zzeh.zzg(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, true);
                                break;
                            case 47:
                                zzeh.zzj(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, true);
                                break;
                            case 48:
                                zzeh.zze(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, true);
                                break;
                            case 49:
                                zzeh.zzb(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, zzad(i));
                                break;
                            case 50:
                                zza(zzfrVar, i15, zzfd.zzo(t, zzag2 & 1048575), i);
                                break;
                            case 51:
                                if (zza((zzds<T>) t, i15, i)) {
                                    d = zze(t, zzag2 & 1048575);
                                    zzfrVar.zza(i15, d);
                                    break;
                                } else {
                                    break;
                                }
                            case 52:
                                if (zza((zzds<T>) t, i15, i)) {
                                    f = zzf(t, zzag2 & 1048575);
                                    zzfrVar.zza(i15, f);
                                    break;
                                } else {
                                    break;
                                }
                            case 53:
                                if (zza((zzds<T>) t, i15, i)) {
                                    j = zzh(t, zzag2 & 1048575);
                                    zzfrVar.zzi(i15, j);
                                    break;
                                } else {
                                    break;
                                }
                            case 54:
                                if (zza((zzds<T>) t, i15, i)) {
                                    j2 = zzh(t, zzag2 & 1048575);
                                    zzfrVar.zza(i15, j2);
                                    break;
                                } else {
                                    break;
                                }
                            case 55:
                                if (zza((zzds<T>) t, i15, i)) {
                                    i2 = zzg(t, zzag2 & 1048575);
                                    zzfrVar.zzc(i15, i2);
                                    break;
                                } else {
                                    break;
                                }
                            case 56:
                                if (zza((zzds<T>) t, i15, i)) {
                                    j3 = zzh(t, zzag2 & 1048575);
                                    zzfrVar.zzc(i15, j3);
                                    break;
                                } else {
                                    break;
                                }
                            case 57:
                                if (zza((zzds<T>) t, i15, i)) {
                                    i3 = zzg(t, zzag2 & 1048575);
                                    zzfrVar.zzf(i15, i3);
                                    break;
                                } else {
                                    break;
                                }
                            case 58:
                                if (zza((zzds<T>) t, i15, i)) {
                                    z = zzi(t, zzag2 & 1048575);
                                    zzfrVar.zzb(i15, z);
                                    break;
                                } else {
                                    break;
                                }
                            case 59:
                                if (!zza((zzds<T>) t, i15, i)) {
                                    break;
                                }
                                zza(i15, zzfd.zzo(t, zzag2 & 1048575), zzfrVar);
                                break;
                            case 60:
                                if (!zza((zzds<T>) t, i15, i)) {
                                    break;
                                }
                                zzfrVar.zza(i15, zzfd.zzo(t, zzag2 & 1048575), zzad(i));
                                break;
                            case 61:
                                if (!zza((zzds<T>) t, i15, i)) {
                                    break;
                                }
                                zzfrVar.zza(i15, (zzbb) zzfd.zzo(t, zzag2 & 1048575));
                                break;
                            case 62:
                                if (zza((zzds<T>) t, i15, i)) {
                                    i4 = zzg(t, zzag2 & 1048575);
                                    zzfrVar.zzd(i15, i4);
                                    break;
                                } else {
                                    break;
                                }
                            case 63:
                                if (zza((zzds<T>) t, i15, i)) {
                                    i5 = zzg(t, zzag2 & 1048575);
                                    zzfrVar.zzn(i15, i5);
                                    break;
                                } else {
                                    break;
                                }
                            case 64:
                                if (zza((zzds<T>) t, i15, i)) {
                                    i6 = zzg(t, zzag2 & 1048575);
                                    zzfrVar.zzm(i15, i6);
                                    break;
                                } else {
                                    break;
                                }
                            case 65:
                                if (zza((zzds<T>) t, i15, i)) {
                                    j4 = zzh(t, zzag2 & 1048575);
                                    zzfrVar.zzj(i15, j4);
                                    break;
                                } else {
                                    break;
                                }
                            case 66:
                                if (zza((zzds<T>) t, i15, i)) {
                                    i7 = zzg(t, zzag2 & 1048575);
                                    zzfrVar.zze(i15, i7);
                                    break;
                                } else {
                                    break;
                                }
                            case 67:
                                if (zza((zzds<T>) t, i15, i)) {
                                    j5 = zzh(t, zzag2 & 1048575);
                                    zzfrVar.zzb(i15, j5);
                                    break;
                                } else {
                                    break;
                                }
                            case 68:
                                if (!zza((zzds<T>) t, i15, i)) {
                                    break;
                                }
                                zzfrVar.zzb(i15, zzfd.zzo(t, zzag2 & 1048575), zzad(i));
                                break;
                        }
                    }
                    while (entry != null) {
                        this.zzmy.zza(zzfrVar, entry);
                        entry = it.hasNext() ? it.next() : null;
                    }
                    zza(this.zzmx, t, zzfrVar);
                }
            }
            it = null;
            entry = null;
            length = this.zzmi.length;
            while (i < length) {
            }
            while (entry != null) {
            }
            zza(this.zzmx, t, zzfrVar);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:69:0x0164, code lost:
        if (r0 == r15) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0188, code lost:
        if (r0 == r15) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x01a1, code lost:
        if (r0 == r15) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x01a3, code lost:
        r2 = r0;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v25, types: [int] */
    @Override // com.google.android.gms.internal.clearcut.zzef
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void zza(T t, byte[] bArr, int i, int i2, zzay zzayVar) throws IOException {
        byte b;
        int i3;
        Unsafe unsafe;
        int i4;
        Object zza;
        int zzb;
        long j;
        int zzm;
        zzds<T> zzdsVar = this;
        T t2 = t;
        byte[] bArr2 = bArr;
        int i5 = i2;
        zzay zzayVar2 = zzayVar;
        if (!zzdsVar.zzmq) {
            zza((zzds<T>) t, bArr, i, i2, 0, zzayVar);
            return;
        }
        Unsafe unsafe2 = zzmh;
        int i6 = i;
        while (i6 < i5) {
            int i7 = i6 + 1;
            byte b2 = bArr2[i6];
            if (b2 < 0) {
                i3 = zzax.zza(b2, bArr2, i7, zzayVar2);
                b = zzayVar2.zzfd;
            } else {
                b = b2;
                i3 = i7;
            }
            int i8 = b >>> 3;
            int i9 = b & 7;
            int zzai = zzdsVar.zzai(i8);
            if (zzai >= 0) {
                int i10 = zzdsVar.zzmi[zzai + 1];
                int i11 = (267386880 & i10) >>> 20;
                long j2 = 1048575 & i10;
                if (i11 <= 17) {
                    boolean z = true;
                    switch (i11) {
                        case 0:
                            if (i9 != 1) {
                                break;
                            } else {
                                zzfd.zza(t2, j2, zzax.zze(bArr2, i3));
                                i6 = i3 + 8;
                                break;
                            }
                        case 1:
                            if (i9 != 5) {
                                break;
                            } else {
                                zzfd.zza((Object) t2, j2, zzax.zzf(bArr2, i3));
                                i6 = i3 + 4;
                                break;
                            }
                        case 2:
                        case 3:
                            if (i9 != 0) {
                                break;
                            } else {
                                zzb = zzax.zzb(bArr2, i3, zzayVar2);
                                j = zzayVar2.zzfe;
                                unsafe2.putLong(t, j2, j);
                                i6 = zzb;
                                break;
                            }
                        case 4:
                        case 11:
                            if (i9 != 0) {
                                break;
                            } else {
                                i6 = zzax.zza(bArr2, i3, zzayVar2);
                                zzm = zzayVar2.zzfd;
                                unsafe2.putInt(t2, j2, zzm);
                                break;
                            }
                        case 5:
                        case 14:
                            if (i9 != 1) {
                                break;
                            } else {
                                unsafe2.putLong(t, j2, zzax.zzd(bArr2, i3));
                                i6 = i3 + 8;
                                break;
                            }
                        case 6:
                        case 13:
                            if (i9 != 5) {
                                break;
                            } else {
                                unsafe2.putInt(t2, j2, zzax.zzc(bArr2, i3));
                                i6 = i3 + 4;
                                break;
                            }
                        case 7:
                            if (i9 != 0) {
                                break;
                            } else {
                                i6 = zzax.zzb(bArr2, i3, zzayVar2);
                                if (zzayVar2.zzfe == 0) {
                                    z = false;
                                }
                                zzfd.zza(t2, j2, z);
                                break;
                            }
                        case 8:
                            if (i9 != 2) {
                                break;
                            } else {
                                i6 = (536870912 & i10) == 0 ? zzax.zzc(bArr2, i3, zzayVar2) : zzax.zzd(bArr2, i3, zzayVar2);
                                zza = zzayVar2.zzff;
                                unsafe2.putObject(t2, j2, zza);
                                break;
                            }
                        case 9:
                            if (i9 != 2) {
                                break;
                            } else {
                                i6 = zza(zzdsVar.zzad(zzai), bArr2, i3, i5, zzayVar2);
                                Object object = unsafe2.getObject(t2, j2);
                                if (object != null) {
                                    zza = zzci.zza(object, zzayVar2.zzff);
                                    unsafe2.putObject(t2, j2, zza);
                                    break;
                                }
                                zza = zzayVar2.zzff;
                                unsafe2.putObject(t2, j2, zza);
                            }
                        case 10:
                            if (i9 != 2) {
                                break;
                            } else {
                                i6 = zzax.zze(bArr2, i3, zzayVar2);
                                zza = zzayVar2.zzff;
                                unsafe2.putObject(t2, j2, zza);
                                break;
                            }
                        case 12:
                            if (i9 != 0) {
                                break;
                            } else {
                                i6 = zzax.zza(bArr2, i3, zzayVar2);
                                zzm = zzayVar2.zzfd;
                                unsafe2.putInt(t2, j2, zzm);
                                break;
                            }
                        case 15:
                            if (i9 != 0) {
                                break;
                            } else {
                                i6 = zzax.zza(bArr2, i3, zzayVar2);
                                zzm = zzbk.zzm(zzayVar2.zzfd);
                                unsafe2.putInt(t2, j2, zzm);
                                break;
                            }
                        case 16:
                            if (i9 != 0) {
                                break;
                            } else {
                                zzb = zzax.zzb(bArr2, i3, zzayVar2);
                                j = zzbk.zza(zzayVar2.zzfe);
                                unsafe2.putLong(t, j2, j);
                                i6 = zzb;
                                break;
                            }
                    }
                } else if (i11 != 27) {
                    if (i11 <= 49) {
                        unsafe = unsafe2;
                        int i12 = i3;
                        i6 = zza((zzds<T>) t, bArr, i3, i2, b, i8, i9, zzai, i10, i11, j2, zzayVar);
                    } else {
                        unsafe = unsafe2;
                        i4 = i3;
                        if (i11 != 50) {
                            i6 = zza((zzds<T>) t, bArr, i4, i2, b, i8, i9, i10, i11, j2, zzai, zzayVar);
                        } else if (i9 == 2) {
                            i6 = zza(t, bArr, i4, i2, zzai, i8, j2, zzayVar);
                        }
                    }
                    zzdsVar = this;
                    t2 = t;
                    bArr2 = bArr;
                    i5 = i2;
                    zzayVar2 = zzayVar;
                    unsafe2 = unsafe;
                } else if (i9 == 2) {
                    zzcn zzcnVar = (zzcn) unsafe2.getObject(t2, j2);
                    if (!zzcnVar.zzu()) {
                        int size = zzcnVar.size();
                        zzcnVar = zzcnVar.zzi(size == 0 ? 10 : size << 1);
                        unsafe2.putObject(t2, j2, zzcnVar);
                    }
                    i6 = zza(zzdsVar.zzad(zzai), b, bArr, i3, i2, zzcnVar, zzayVar);
                }
                int i13 = i4;
                i6 = zza(b, bArr, i13, i2, t, zzayVar);
                zzdsVar = this;
                t2 = t;
                bArr2 = bArr;
                i5 = i2;
                zzayVar2 = zzayVar;
                unsafe2 = unsafe;
            }
            unsafe = unsafe2;
            i4 = i3;
            int i132 = i4;
            i6 = zza(b, bArr, i132, i2, t, zzayVar);
            zzdsVar = this;
            t2 = t;
            bArr2 = bArr;
            i5 = i2;
            zzayVar2 = zzayVar;
            unsafe2 = unsafe;
        }
        if (i6 != i5) {
            throw zzco.zzbo();
        }
    }

    @Override // com.google.android.gms.internal.clearcut.zzef
    public final void zzc(T t) {
        int[] iArr = this.zzmt;
        if (iArr != null) {
            for (int i : iArr) {
                long zzag = zzag(i) & 1048575;
                Object zzo = zzfd.zzo(t, zzag);
                if (zzo != null) {
                    zzfd.zza(t, zzag, this.zzmz.zzj(zzo));
                }
            }
        }
        int[] iArr2 = this.zzmu;
        if (iArr2 != null) {
            for (int i2 : iArr2) {
                this.zzmw.zza(t, i2);
            }
        }
        this.zzmx.zzc(t);
        if (this.zzmo) {
            this.zzmy.zzc(t);
        }
    }

    @Override // com.google.android.gms.internal.clearcut.zzef
    public final void zzc(T t, T t2) {
        t2.getClass();
        for (int i = 0; i < this.zzmi.length; i += 4) {
            int zzag = zzag(i);
            long j = 1048575 & zzag;
            int i2 = this.zzmi[i];
            switch ((zzag & 267386880) >>> 20) {
                case 0:
                    if (zza((zzds<T>) t2, i)) {
                        zzfd.zza(t, j, zzfd.zzn(t2, j));
                        zzb((zzds<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 1:
                    if (zza((zzds<T>) t2, i)) {
                        zzfd.zza((Object) t, j, zzfd.zzm(t2, j));
                        zzb((zzds<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 2:
                    if (!zza((zzds<T>) t2, i)) {
                        break;
                    }
                    zzfd.zza((Object) t, j, zzfd.zzk(t2, j));
                    zzb((zzds<T>) t, i);
                    break;
                case 3:
                    if (!zza((zzds<T>) t2, i)) {
                        break;
                    }
                    zzfd.zza((Object) t, j, zzfd.zzk(t2, j));
                    zzb((zzds<T>) t, i);
                    break;
                case 4:
                    if (!zza((zzds<T>) t2, i)) {
                        break;
                    }
                    zzfd.zza((Object) t, j, zzfd.zzj(t2, j));
                    zzb((zzds<T>) t, i);
                    break;
                case 5:
                    if (!zza((zzds<T>) t2, i)) {
                        break;
                    }
                    zzfd.zza((Object) t, j, zzfd.zzk(t2, j));
                    zzb((zzds<T>) t, i);
                    break;
                case 6:
                    if (!zza((zzds<T>) t2, i)) {
                        break;
                    }
                    zzfd.zza((Object) t, j, zzfd.zzj(t2, j));
                    zzb((zzds<T>) t, i);
                    break;
                case 7:
                    if (zza((zzds<T>) t2, i)) {
                        zzfd.zza(t, j, zzfd.zzl(t2, j));
                        zzb((zzds<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 8:
                    if (!zza((zzds<T>) t2, i)) {
                        break;
                    }
                    zzfd.zza(t, j, zzfd.zzo(t2, j));
                    zzb((zzds<T>) t, i);
                    break;
                case 9:
                case 17:
                    zza(t, t2, i);
                    break;
                case 10:
                    if (!zza((zzds<T>) t2, i)) {
                        break;
                    }
                    zzfd.zza(t, j, zzfd.zzo(t2, j));
                    zzb((zzds<T>) t, i);
                    break;
                case 11:
                    if (!zza((zzds<T>) t2, i)) {
                        break;
                    }
                    zzfd.zza((Object) t, j, zzfd.zzj(t2, j));
                    zzb((zzds<T>) t, i);
                    break;
                case 12:
                    if (!zza((zzds<T>) t2, i)) {
                        break;
                    }
                    zzfd.zza((Object) t, j, zzfd.zzj(t2, j));
                    zzb((zzds<T>) t, i);
                    break;
                case 13:
                    if (!zza((zzds<T>) t2, i)) {
                        break;
                    }
                    zzfd.zza((Object) t, j, zzfd.zzj(t2, j));
                    zzb((zzds<T>) t, i);
                    break;
                case 14:
                    if (!zza((zzds<T>) t2, i)) {
                        break;
                    }
                    zzfd.zza((Object) t, j, zzfd.zzk(t2, j));
                    zzb((zzds<T>) t, i);
                    break;
                case 15:
                    if (!zza((zzds<T>) t2, i)) {
                        break;
                    }
                    zzfd.zza((Object) t, j, zzfd.zzj(t2, j));
                    zzb((zzds<T>) t, i);
                    break;
                case 16:
                    if (!zza((zzds<T>) t2, i)) {
                        break;
                    }
                    zzfd.zza((Object) t, j, zzfd.zzk(t2, j));
                    zzb((zzds<T>) t, i);
                    break;
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                    this.zzmw.zza(t, t2, j);
                    break;
                case 50:
                    zzeh.zza(this.zzmz, t, t2, j);
                    break;
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                case 56:
                case 57:
                case 58:
                case 59:
                    if (!zza((zzds<T>) t2, i2, i)) {
                        break;
                    }
                    zzfd.zza(t, j, zzfd.zzo(t2, j));
                    zzb((zzds<T>) t, i2, i);
                    break;
                case 60:
                case 68:
                    zzb(t, t2, i);
                    break;
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                    if (!zza((zzds<T>) t2, i2, i)) {
                        break;
                    }
                    zzfd.zza(t, j, zzfd.zzo(t2, j));
                    zzb((zzds<T>) t, i2, i);
                    break;
            }
        }
        if (!this.zzmq) {
            zzeh.zza(this.zzmx, t, t2);
            if (!this.zzmo) {
                return;
            }
            zzeh.zza(this.zzmy, t, t2);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:100:0x01c7, code lost:
        if (r19.zzmr != false) goto L117;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x01d8, code lost:
        if (r19.zzmr != false) goto L117;
     */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x01e9, code lost:
        if (r19.zzmr != false) goto L117;
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x01fa, code lost:
        if (r19.zzmr != false) goto L117;
     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x020b, code lost:
        if (r19.zzmr != false) goto L117;
     */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x020d, code lost:
        r2.putInt(r20, r14, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x0211, code lost:
        r3 = (com.google.android.gms.internal.clearcut.zzbn.zzr(r3) + com.google.android.gms.internal.clearcut.zzbn.zzt(r5)) + r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x0331, code lost:
        if ((r5 instanceof com.google.android.gms.internal.clearcut.zzbb) != false) goto L161;
     */
    /* JADX WARN: Code restructure failed: missing block: B:169:0x0334, code lost:
        r3 = com.google.android.gms.internal.clearcut.zzbn.zzb(r3, (java.lang.String) r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:221:0x0414, code lost:
        if (zza((com.google.android.gms.internal.clearcut.zzds<T>) r20, r15, r5) != false) goto L347;
     */
    /* JADX WARN: Code restructure failed: missing block: B:229:0x0434, code lost:
        if (zza((com.google.android.gms.internal.clearcut.zzds<T>) r20, r15, r5) != false) goto L358;
     */
    /* JADX WARN: Code restructure failed: missing block: B:231:0x043c, code lost:
        if (zza((com.google.android.gms.internal.clearcut.zzds<T>) r20, r15, r5) != false) goto L361;
     */
    /* JADX WARN: Code restructure failed: missing block: B:239:0x045c, code lost:
        if (zza((com.google.android.gms.internal.clearcut.zzds<T>) r20, r15, r5) != false) goto L373;
     */
    /* JADX WARN: Code restructure failed: missing block: B:241:0x0464, code lost:
        if (zza((com.google.android.gms.internal.clearcut.zzds<T>) r20, r15, r5) != false) goto L377;
     */
    /* JADX WARN: Code restructure failed: missing block: B:245:0x0474, code lost:
        if ((r4 instanceof com.google.android.gms.internal.clearcut.zzbb) != false) goto L374;
     */
    /* JADX WARN: Code restructure failed: missing block: B:247:0x047c, code lost:
        if (zza((com.google.android.gms.internal.clearcut.zzds<T>) r20, r15, r5) != false) goto L385;
     */
    /* JADX WARN: Code restructure failed: missing block: B:274:0x0514, code lost:
        if (r19.zzmr != false) goto L327;
     */
    /* JADX WARN: Code restructure failed: missing block: B:278:0x0526, code lost:
        if (r19.zzmr != false) goto L327;
     */
    /* JADX WARN: Code restructure failed: missing block: B:282:0x0538, code lost:
        if (r19.zzmr != false) goto L327;
     */
    /* JADX WARN: Code restructure failed: missing block: B:286:0x054a, code lost:
        if (r19.zzmr != false) goto L327;
     */
    /* JADX WARN: Code restructure failed: missing block: B:290:0x055c, code lost:
        if (r19.zzmr != false) goto L327;
     */
    /* JADX WARN: Code restructure failed: missing block: B:294:0x056e, code lost:
        if (r19.zzmr != false) goto L327;
     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x0580, code lost:
        if (r19.zzmr != false) goto L327;
     */
    /* JADX WARN: Code restructure failed: missing block: B:302:0x0592, code lost:
        if (r19.zzmr != false) goto L327;
     */
    /* JADX WARN: Code restructure failed: missing block: B:306:0x05a3, code lost:
        if (r19.zzmr != false) goto L327;
     */
    /* JADX WARN: Code restructure failed: missing block: B:310:0x05b4, code lost:
        if (r19.zzmr != false) goto L327;
     */
    /* JADX WARN: Code restructure failed: missing block: B:314:0x05c5, code lost:
        if (r19.zzmr != false) goto L327;
     */
    /* JADX WARN: Code restructure failed: missing block: B:318:0x05d6, code lost:
        if (r19.zzmr != false) goto L327;
     */
    /* JADX WARN: Code restructure failed: missing block: B:322:0x05e7, code lost:
        if (r19.zzmr != false) goto L327;
     */
    /* JADX WARN: Code restructure failed: missing block: B:326:0x05f8, code lost:
        if (r19.zzmr != false) goto L327;
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x05fa, code lost:
        r2.putInt(r20, r9, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:328:0x05fe, code lost:
        r9 = (com.google.android.gms.internal.clearcut.zzbn.zzr(r15) + com.google.android.gms.internal.clearcut.zzbn.zzt(r4)) + r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:346:0x06b4, code lost:
        if ((r12 & r18) != 0) goto L347;
     */
    /* JADX WARN: Code restructure failed: missing block: B:347:0x06b6, code lost:
        r4 = com.google.android.gms.internal.clearcut.zzbn.zzc(r15, (com.google.android.gms.internal.clearcut.zzdo) r2.getObject(r20, r10), zzad(r5));
     */
    /* JADX WARN: Code restructure failed: missing block: B:357:0x06e1, code lost:
        if ((r12 & r18) != 0) goto L358;
     */
    /* JADX WARN: Code restructure failed: missing block: B:358:0x06e3, code lost:
        r4 = com.google.android.gms.internal.clearcut.zzbn.zzh(r15, 0L);
     */
    /* JADX WARN: Code restructure failed: missing block: B:360:0x06ec, code lost:
        if ((r12 & r18) != 0) goto L361;
     */
    /* JADX WARN: Code restructure failed: missing block: B:361:0x06ee, code lost:
        r9 = com.google.android.gms.internal.clearcut.zzbn.zzk(r15, 0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:372:0x0711, code lost:
        if ((r12 & r18) != 0) goto L373;
     */
    /* JADX WARN: Code restructure failed: missing block: B:373:0x0713, code lost:
        r4 = r2.getObject(r20, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:374:0x0717, code lost:
        r4 = com.google.android.gms.internal.clearcut.zzbn.zzc(r15, (com.google.android.gms.internal.clearcut.zzbb) r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:376:0x0720, code lost:
        if ((r12 & r18) != 0) goto L377;
     */
    /* JADX WARN: Code restructure failed: missing block: B:377:0x0722, code lost:
        r4 = com.google.android.gms.internal.clearcut.zzeh.zzc(r15, r2.getObject(r20, r10), zzad(r5));
     */
    /* JADX WARN: Code restructure failed: missing block: B:381:0x073a, code lost:
        if ((r4 instanceof com.google.android.gms.internal.clearcut.zzbb) != false) goto L374;
     */
    /* JADX WARN: Code restructure failed: missing block: B:382:0x073d, code lost:
        r4 = com.google.android.gms.internal.clearcut.zzbn.zzb(r15, (java.lang.String) r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:384:0x0747, code lost:
        if ((r12 & r18) != 0) goto L385;
     */
    /* JADX WARN: Code restructure failed: missing block: B:385:0x0749, code lost:
        r4 = com.google.android.gms.internal.clearcut.zzbn.zzc(r15, true);
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00ab, code lost:
        if ((r5 instanceof com.google.android.gms.internal.clearcut.zzbb) != false) goto L161;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0127, code lost:
        if (r19.zzmr != false) goto L117;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0139, code lost:
        if (r19.zzmr != false) goto L117;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x014b, code lost:
        if (r19.zzmr != false) goto L117;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x015d, code lost:
        if (r19.zzmr != false) goto L117;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x016f, code lost:
        if (r19.zzmr != false) goto L117;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x0181, code lost:
        if (r19.zzmr != false) goto L117;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x0193, code lost:
        if (r19.zzmr != false) goto L117;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x01a5, code lost:
        if (r19.zzmr != false) goto L117;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x01b6, code lost:
        if (r19.zzmr != false) goto L117;
     */
    @Override // com.google.android.gms.internal.clearcut.zzef
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final int zzm(T t) {
        int i;
        int i2;
        long j;
        int i3;
        int zzc;
        Object obj;
        int i4;
        int i5;
        int i6;
        int i7;
        long j2;
        int i8;
        int zzb;
        long j3;
        long j4;
        int i9;
        Object obj2;
        int i10;
        int i11;
        int i12;
        long j5;
        int i13;
        int i14 = 267386880;
        if (!this.zzmq) {
            Unsafe unsafe = zzmh;
            int i15 = -1;
            int i16 = 0;
            int i17 = 0;
            int i18 = 0;
            while (i16 < this.zzmi.length) {
                int zzag = zzag(i16);
                int[] iArr = this.zzmi;
                int i19 = iArr[i16];
                int i20 = (zzag & 267386880) >>> 20;
                if (i20 <= 17) {
                    int i21 = iArr[i16 + 2];
                    int i22 = i21 & 1048575;
                    i = 1 << (i21 >>> 20);
                    if (i22 != i15) {
                        i18 = unsafe.getInt(t, i22);
                        i15 = i22;
                    }
                    i2 = i21;
                } else {
                    i2 = (!this.zzmr || i20 < zzcb.DOUBLE_LIST_PACKED.id() || i20 > zzcb.SINT64_LIST_PACKED.id()) ? 0 : this.zzmi[i16 + 2] & 1048575;
                    i = 0;
                }
                long j6 = zzag & 1048575;
                switch (i20) {
                    case 0:
                        j = 0;
                        if ((i18 & i) != 0) {
                            i17 += zzbn.zzb(i19, 0.0d);
                            break;
                        }
                        break;
                    case 1:
                        j = 0;
                        if ((i18 & i) != 0) {
                            i17 += zzbn.zzb(i19, 0.0f);
                            break;
                        }
                    case 2:
                        j = 0;
                        if ((i18 & i) != 0) {
                            i3 = zzbn.zzd(i19, unsafe.getLong(t, j6));
                            i17 += i3;
                        }
                        break;
                    case 3:
                        j = 0;
                        if ((i18 & i) != 0) {
                            i3 = zzbn.zze(i19, unsafe.getLong(t, j6));
                            i17 += i3;
                        }
                        break;
                    case 4:
                        j = 0;
                        if ((i18 & i) != 0) {
                            i3 = zzbn.zzg(i19, unsafe.getInt(t, j6));
                            i17 += i3;
                        }
                        break;
                    case 5:
                        j = 0;
                        if ((i18 & i) != 0) {
                            i3 = zzbn.zzg(i19, 0L);
                            i17 += i3;
                        }
                        break;
                    case 6:
                        if ((i18 & i) != 0) {
                            i17 += zzbn.zzj(i19, 0);
                            j = 0;
                            break;
                        }
                        j = 0;
                    case 7:
                        break;
                    case 8:
                        if ((i18 & i) != 0) {
                            obj = unsafe.getObject(t, j6);
                            break;
                        }
                        j = 0;
                        break;
                    case 9:
                        break;
                    case 10:
                        break;
                    case 11:
                        if ((i18 & i) != 0) {
                            i4 = unsafe.getInt(t, j6);
                            zzc = zzbn.zzh(i19, i4);
                            i17 += zzc;
                        }
                        j = 0;
                        break;
                    case 12:
                        if ((i18 & i) != 0) {
                            i5 = unsafe.getInt(t, j6);
                            zzc = zzbn.zzl(i19, i5);
                            i17 += zzc;
                        }
                        j = 0;
                        break;
                    case 13:
                        break;
                    case 14:
                        break;
                    case 15:
                        if ((i18 & i) != 0) {
                            i7 = unsafe.getInt(t, j6);
                            zzc = zzbn.zzi(i19, i7);
                            i17 += zzc;
                        }
                        j = 0;
                        break;
                    case 16:
                        if ((i18 & i) != 0) {
                            j2 = unsafe.getLong(t, j6);
                            zzc = zzbn.zzf(i19, j2);
                            i17 += zzc;
                        }
                        j = 0;
                        break;
                    case 17:
                        break;
                    case 18:
                    case 23:
                    case 32:
                        zzc = zzeh.zzw(i19, (List) unsafe.getObject(t, j6), false);
                        i17 += zzc;
                        j = 0;
                        break;
                    case 19:
                    case 24:
                    case 31:
                        zzc = zzeh.zzv(i19, (List) unsafe.getObject(t, j6), false);
                        i17 += zzc;
                        j = 0;
                        break;
                    case 20:
                        zzc = zzeh.zzo(i19, (List) unsafe.getObject(t, j6), false);
                        i17 += zzc;
                        j = 0;
                        break;
                    case 21:
                        zzc = zzeh.zzp(i19, (List) unsafe.getObject(t, j6), false);
                        i17 += zzc;
                        j = 0;
                        break;
                    case 22:
                        zzc = zzeh.zzs(i19, (List) unsafe.getObject(t, j6), false);
                        i17 += zzc;
                        j = 0;
                        break;
                    case 25:
                        zzc = zzeh.zzx(i19, (List) unsafe.getObject(t, j6), false);
                        i17 += zzc;
                        j = 0;
                        break;
                    case 26:
                        zzc = zzeh.zzc(i19, (List) unsafe.getObject(t, j6));
                        i17 += zzc;
                        j = 0;
                        break;
                    case 27:
                        zzc = zzeh.zzc(i19, (List<?>) unsafe.getObject(t, j6), zzad(i16));
                        i17 += zzc;
                        j = 0;
                        break;
                    case 28:
                        zzc = zzeh.zzd(i19, (List) unsafe.getObject(t, j6));
                        i17 += zzc;
                        j = 0;
                        break;
                    case 29:
                        zzc = zzeh.zzt(i19, (List) unsafe.getObject(t, j6), false);
                        i17 += zzc;
                        j = 0;
                        break;
                    case 30:
                        zzc = zzeh.zzr(i19, (List) unsafe.getObject(t, j6), false);
                        i17 += zzc;
                        j = 0;
                        break;
                    case 33:
                        zzc = zzeh.zzu(i19, (List) unsafe.getObject(t, j6), false);
                        i17 += zzc;
                        j = 0;
                        break;
                    case 34:
                        zzc = zzeh.zzq(i19, (List) unsafe.getObject(t, j6), false);
                        i17 += zzc;
                        j = 0;
                        break;
                    case 35:
                        i8 = zzeh.zzi((List) unsafe.getObject(t, j6));
                        if (i8 > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 36:
                        i8 = zzeh.zzh((List) unsafe.getObject(t, j6));
                        if (i8 > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 37:
                        i8 = zzeh.zza((List) unsafe.getObject(t, j6));
                        if (i8 > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 38:
                        i8 = zzeh.zzb((List) unsafe.getObject(t, j6));
                        if (i8 > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 39:
                        i8 = zzeh.zze((List) unsafe.getObject(t, j6));
                        if (i8 > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 40:
                        i8 = zzeh.zzi((List) unsafe.getObject(t, j6));
                        if (i8 > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 41:
                        i8 = zzeh.zzh((List) unsafe.getObject(t, j6));
                        if (i8 > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 42:
                        i8 = zzeh.zzj((List) unsafe.getObject(t, j6));
                        if (i8 > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 43:
                        i8 = zzeh.zzf((List) unsafe.getObject(t, j6));
                        if (i8 > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 44:
                        i8 = zzeh.zzd((List) unsafe.getObject(t, j6));
                        if (i8 > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 45:
                        i8 = zzeh.zzh((List) unsafe.getObject(t, j6));
                        if (i8 > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 46:
                        i8 = zzeh.zzi((List) unsafe.getObject(t, j6));
                        if (i8 > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 47:
                        i8 = zzeh.zzg((List) unsafe.getObject(t, j6));
                        if (i8 > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 48:
                        i8 = zzeh.zzc((List) unsafe.getObject(t, j6));
                        if (i8 > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 49:
                        zzc = zzeh.zzd(i19, (List) unsafe.getObject(t, j6), zzad(i16));
                        i17 += zzc;
                        j = 0;
                        break;
                    case 50:
                        zzc = this.zzmz.zzb(i19, unsafe.getObject(t, j6), zzae(i16));
                        i17 += zzc;
                        j = 0;
                        break;
                    case 51:
                        if (zza((zzds<T>) t, i19, i16)) {
                            zzc = zzbn.zzb(i19, 0.0d);
                            i17 += zzc;
                        }
                        j = 0;
                        break;
                    case 52:
                        if (zza((zzds<T>) t, i19, i16)) {
                            i6 = zzbn.zzb(i19, 0.0f);
                            i17 += i6;
                        }
                        j = 0;
                        break;
                    case 53:
                        if (zza((zzds<T>) t, i19, i16)) {
                            zzc = zzbn.zzd(i19, zzh(t, j6));
                            i17 += zzc;
                        }
                        j = 0;
                        break;
                    case 54:
                        if (zza((zzds<T>) t, i19, i16)) {
                            zzc = zzbn.zze(i19, zzh(t, j6));
                            i17 += zzc;
                        }
                        j = 0;
                        break;
                    case 55:
                        if (zza((zzds<T>) t, i19, i16)) {
                            zzc = zzbn.zzg(i19, zzg(t, j6));
                            i17 += zzc;
                        }
                        j = 0;
                        break;
                    case 56:
                        if (zza((zzds<T>) t, i19, i16)) {
                            zzc = zzbn.zzg(i19, 0L);
                            i17 += zzc;
                        }
                        j = 0;
                        break;
                    case 57:
                        if (zza((zzds<T>) t, i19, i16)) {
                            i6 = zzbn.zzj(i19, 0);
                            i17 += i6;
                        }
                        j = 0;
                        break;
                    case 58:
                        break;
                    case 59:
                        if (zza((zzds<T>) t, i19, i16)) {
                            obj = unsafe.getObject(t, j6);
                            break;
                        }
                        j = 0;
                        break;
                    case 60:
                        break;
                    case 61:
                        break;
                    case 62:
                        if (zza((zzds<T>) t, i19, i16)) {
                            i4 = zzg(t, j6);
                            zzc = zzbn.zzh(i19, i4);
                            i17 += zzc;
                        }
                        j = 0;
                        break;
                    case 63:
                        if (zza((zzds<T>) t, i19, i16)) {
                            i5 = zzg(t, j6);
                            zzc = zzbn.zzl(i19, i5);
                            i17 += zzc;
                        }
                        j = 0;
                        break;
                    case 64:
                        break;
                    case 65:
                        break;
                    case 66:
                        if (zza((zzds<T>) t, i19, i16)) {
                            i7 = zzg(t, j6);
                            zzc = zzbn.zzi(i19, i7);
                            i17 += zzc;
                        }
                        j = 0;
                        break;
                    case 67:
                        if (zza((zzds<T>) t, i19, i16)) {
                            j2 = zzh(t, j6);
                            zzc = zzbn.zzf(i19, j2);
                            i17 += zzc;
                        }
                        j = 0;
                        break;
                    case 68:
                        break;
                    default:
                        j = 0;
                        break;
                }
                i16 += 4;
            }
            int zza = i17 + zza(this.zzmx, t);
            return this.zzmo ? zza + this.zzmy.zza(t).zzas() : zza;
        }
        Unsafe unsafe2 = zzmh;
        int i23 = 0;
        int i24 = 0;
        while (i23 < this.zzmi.length) {
            int zzag2 = zzag(i23);
            int i25 = (zzag2 & i14) >>> 20;
            int i26 = this.zzmi[i23];
            long j7 = zzag2 & 1048575;
            int i27 = (i25 < zzcb.DOUBLE_LIST_PACKED.id() || i25 > zzcb.SINT64_LIST_PACKED.id()) ? 0 : this.zzmi[i23 + 2] & 1048575;
            switch (i25) {
                case 0:
                    if (!zza((zzds<T>) t, i23)) {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                    zzb = zzbn.zzb(i26, 0.0d);
                    break;
                case 1:
                    if (!zza((zzds<T>) t, i23)) {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                    zzb = zzbn.zzb(i26, 0.0f);
                    break;
                case 2:
                    if (zza((zzds<T>) t, i23)) {
                        j3 = zzfd.zzk(t, j7);
                        zzb = zzbn.zzd(i26, j3);
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 3:
                    if (zza((zzds<T>) t, i23)) {
                        j4 = zzfd.zzk(t, j7);
                        zzb = zzbn.zze(i26, j4);
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 4:
                    if (zza((zzds<T>) t, i23)) {
                        i9 = zzfd.zzj(t, j7);
                        zzb = zzbn.zzg(i26, i9);
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 5:
                    if (!zza((zzds<T>) t, i23)) {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                    zzb = zzbn.zzg(i26, 0L);
                    break;
                case 6:
                    if (!zza((zzds<T>) t, i23)) {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                    zzb = zzbn.zzj(i26, 0);
                    break;
                case 7:
                    if (!zza((zzds<T>) t, i23)) {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                    zzb = zzbn.zzc(i26, true);
                    break;
                case 8:
                    if (zza((zzds<T>) t, i23)) {
                        obj2 = zzfd.zzo(t, j7);
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 9:
                    if (!zza((zzds<T>) t, i23)) {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                    zzb = zzeh.zzc(i26, zzfd.zzo(t, j7), zzad(i23));
                    break;
                case 10:
                    if (!zza((zzds<T>) t, i23)) {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                    obj2 = zzfd.zzo(t, j7);
                    zzb = zzbn.zzc(i26, (zzbb) obj2);
                    break;
                case 11:
                    if (zza((zzds<T>) t, i23)) {
                        i10 = zzfd.zzj(t, j7);
                        zzb = zzbn.zzh(i26, i10);
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 12:
                    if (zza((zzds<T>) t, i23)) {
                        i11 = zzfd.zzj(t, j7);
                        zzb = zzbn.zzl(i26, i11);
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 13:
                    if (!zza((zzds<T>) t, i23)) {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                    zzb = zzbn.zzk(i26, 0);
                    break;
                case 14:
                    if (!zza((zzds<T>) t, i23)) {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                    zzb = zzbn.zzh(i26, 0L);
                    break;
                case 15:
                    if (zza((zzds<T>) t, i23)) {
                        i12 = zzfd.zzj(t, j7);
                        zzb = zzbn.zzi(i26, i12);
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 16:
                    if (zza((zzds<T>) t, i23)) {
                        j5 = zzfd.zzk(t, j7);
                        zzb = zzbn.zzf(i26, j5);
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 17:
                    if (!zza((zzds<T>) t, i23)) {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                    zzb = zzbn.zzc(i26, (zzdo) zzfd.zzo(t, j7), zzad(i23));
                    break;
                case 18:
                case 23:
                case 32:
                    zzb = zzeh.zzw(i26, zzd(t, j7), false);
                    break;
                case 19:
                case 24:
                case 31:
                    zzb = zzeh.zzv(i26, zzd(t, j7), false);
                    break;
                case 20:
                    zzb = zzeh.zzo(i26, zzd(t, j7), false);
                    break;
                case 21:
                    zzb = zzeh.zzp(i26, zzd(t, j7), false);
                    break;
                case 22:
                    zzb = zzeh.zzs(i26, zzd(t, j7), false);
                    break;
                case 25:
                    zzb = zzeh.zzx(i26, zzd(t, j7), false);
                    break;
                case 26:
                    zzb = zzeh.zzc(i26, zzd(t, j7));
                    break;
                case 27:
                    zzb = zzeh.zzc(i26, (List<?>) zzd(t, j7), zzad(i23));
                    break;
                case 28:
                    zzb = zzeh.zzd(i26, zzd(t, j7));
                    break;
                case 29:
                    zzb = zzeh.zzt(i26, zzd(t, j7), false);
                    break;
                case 30:
                    zzb = zzeh.zzr(i26, zzd(t, j7), false);
                    break;
                case 33:
                    zzb = zzeh.zzu(i26, zzd(t, j7), false);
                    break;
                case 34:
                    zzb = zzeh.zzq(i26, zzd(t, j7), false);
                    break;
                case 35:
                    i13 = zzeh.zzi((List) unsafe2.getObject(t, j7));
                    if (i13 > 0) {
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 36:
                    i13 = zzeh.zzh((List) unsafe2.getObject(t, j7));
                    if (i13 > 0) {
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 37:
                    i13 = zzeh.zza((List) unsafe2.getObject(t, j7));
                    if (i13 > 0) {
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 38:
                    i13 = zzeh.zzb((List) unsafe2.getObject(t, j7));
                    if (i13 > 0) {
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 39:
                    i13 = zzeh.zze((List) unsafe2.getObject(t, j7));
                    if (i13 > 0) {
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 40:
                    i13 = zzeh.zzi((List) unsafe2.getObject(t, j7));
                    if (i13 > 0) {
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 41:
                    i13 = zzeh.zzh((List) unsafe2.getObject(t, j7));
                    if (i13 > 0) {
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 42:
                    i13 = zzeh.zzj((List) unsafe2.getObject(t, j7));
                    if (i13 > 0) {
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 43:
                    i13 = zzeh.zzf((List) unsafe2.getObject(t, j7));
                    if (i13 > 0) {
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 44:
                    i13 = zzeh.zzd((List) unsafe2.getObject(t, j7));
                    if (i13 > 0) {
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 45:
                    i13 = zzeh.zzh((List) unsafe2.getObject(t, j7));
                    if (i13 > 0) {
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 46:
                    i13 = zzeh.zzi((List) unsafe2.getObject(t, j7));
                    if (i13 > 0) {
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 47:
                    i13 = zzeh.zzg((List) unsafe2.getObject(t, j7));
                    if (i13 > 0) {
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 48:
                    i13 = zzeh.zzc((List) unsafe2.getObject(t, j7));
                    if (i13 > 0) {
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 49:
                    zzb = zzeh.zzd(i26, zzd(t, j7), zzad(i23));
                    break;
                case 50:
                    zzb = this.zzmz.zzb(i26, zzfd.zzo(t, j7), zzae(i23));
                    break;
                case 51:
                    if (!zza((zzds<T>) t, i26, i23)) {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                    zzb = zzbn.zzb(i26, 0.0d);
                    break;
                case 52:
                    if (!zza((zzds<T>) t, i26, i23)) {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                    zzb = zzbn.zzb(i26, 0.0f);
                    break;
                case 53:
                    if (zza((zzds<T>) t, i26, i23)) {
                        j3 = zzh(t, j7);
                        zzb = zzbn.zzd(i26, j3);
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 54:
                    if (zza((zzds<T>) t, i26, i23)) {
                        j4 = zzh(t, j7);
                        zzb = zzbn.zze(i26, j4);
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 55:
                    if (zza((zzds<T>) t, i26, i23)) {
                        i9 = zzg(t, j7);
                        zzb = zzbn.zzg(i26, i9);
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 56:
                    if (!zza((zzds<T>) t, i26, i23)) {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                    zzb = zzbn.zzg(i26, 0L);
                    break;
                case 57:
                    if (!zza((zzds<T>) t, i26, i23)) {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                    zzb = zzbn.zzj(i26, 0);
                    break;
                case 58:
                    if (!zza((zzds<T>) t, i26, i23)) {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                    zzb = zzbn.zzc(i26, true);
                    break;
                case 59:
                    if (zza((zzds<T>) t, i26, i23)) {
                        obj2 = zzfd.zzo(t, j7);
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 60:
                    if (!zza((zzds<T>) t, i26, i23)) {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                    zzb = zzeh.zzc(i26, zzfd.zzo(t, j7), zzad(i23));
                    break;
                case 61:
                    if (!zza((zzds<T>) t, i26, i23)) {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                    obj2 = zzfd.zzo(t, j7);
                    zzb = zzbn.zzc(i26, (zzbb) obj2);
                    break;
                case 62:
                    if (zza((zzds<T>) t, i26, i23)) {
                        i10 = zzg(t, j7);
                        zzb = zzbn.zzh(i26, i10);
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 63:
                    if (zza((zzds<T>) t, i26, i23)) {
                        i11 = zzg(t, j7);
                        zzb = zzbn.zzl(i26, i11);
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 64:
                    if (!zza((zzds<T>) t, i26, i23)) {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                    zzb = zzbn.zzk(i26, 0);
                    break;
                case 65:
                    if (!zza((zzds<T>) t, i26, i23)) {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                    zzb = zzbn.zzh(i26, 0L);
                    break;
                case 66:
                    if (zza((zzds<T>) t, i26, i23)) {
                        i12 = zzg(t, j7);
                        zzb = zzbn.zzi(i26, i12);
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 67:
                    if (zza((zzds<T>) t, i26, i23)) {
                        j5 = zzh(t, j7);
                        zzb = zzbn.zzf(i26, j5);
                        break;
                    } else {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                case 68:
                    if (!zza((zzds<T>) t, i26, i23)) {
                        continue;
                        i23 += 4;
                        i14 = 267386880;
                    }
                    zzb = zzbn.zzc(i26, (zzdo) zzfd.zzo(t, j7), zzad(i23));
                    break;
                default:
                    i23 += 4;
                    i14 = 267386880;
            }
            i24 += zzb;
            i23 += 4;
            i14 = 267386880;
        }
        return i24 + zza(this.zzmx, t);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.android.gms.internal.clearcut.zzef
    public final boolean zzo(T t) {
        int i;
        int i2;
        boolean z;
        int[] iArr = this.zzms;
        if (iArr != null && iArr.length != 0) {
            int i3 = -1;
            int length = iArr.length;
            int i4 = 0;
            for (int i5 = 0; i5 < length; i5 = i + 1) {
                int i6 = iArr[i5];
                int zzai = zzai(i6);
                int zzag = zzag(zzai);
                if (!this.zzmq) {
                    int i7 = this.zzmi[zzai + 2];
                    int i8 = i7 & 1048575;
                    i2 = 1 << (i7 >>> 20);
                    if (i8 != i3) {
                        i = i5;
                        i4 = zzmh.getInt(t, i8);
                        i3 = i8;
                    } else {
                        i = i5;
                    }
                } else {
                    i = i5;
                    i2 = 0;
                }
                if (((268435456 & zzag) != 0) && !zza((zzds<T>) t, zzai, i4, i2)) {
                    return false;
                }
                int i9 = (267386880 & zzag) >>> 20;
                if (i9 != 9 && i9 != 17) {
                    if (i9 != 27) {
                        if (i9 == 60 || i9 == 68) {
                            if (zza((zzds<T>) t, i6, zzai) && !zza(t, zzag, zzad(zzai))) {
                                return false;
                            }
                        } else if (i9 != 49) {
                            if (i9 == 50 && !this.zzmz.zzh(zzfd.zzo(t, zzag & 1048575)).isEmpty()) {
                                this.zzmz.zzl(zzae(zzai));
                                throw null;
                            }
                        }
                    }
                    List list = (List) zzfd.zzo(t, zzag & 1048575);
                    if (!list.isEmpty()) {
                        zzef zzad = zzad(zzai);
                        for (int i10 = 0; i10 < list.size(); i10++) {
                            if (!zzad.zzo(list.get(i10))) {
                                z = false;
                                break;
                            }
                        }
                    }
                    z = true;
                    if (!z) {
                        return false;
                    }
                } else if (zza((zzds<T>) t, zzai, i4, i2) && !zza(t, zzag, zzad(zzai))) {
                    return false;
                }
            }
            if (this.zzmo && !this.zzmy.zza(t).isInitialized()) {
                return false;
            }
        }
        return true;
    }
}
