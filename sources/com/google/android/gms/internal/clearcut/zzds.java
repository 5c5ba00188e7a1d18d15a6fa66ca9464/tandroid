package com.google.android.gms.internal.clearcut;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import com.google.android.gms.internal.clearcut.zzcg;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import sun.misc.Unsafe;

/* loaded from: classes.dex */
final class zzds implements zzef {
    private static final Unsafe zzmh = zzfd.zzef();
    private final int[] zzmi;
    private final Object[] zzmj;
    private final int zzmk;
    private final int zzml;
    private final int zzmm;
    private final zzdo zzmn;
    private final boolean zzmo;
    private final boolean zzmp;
    private final boolean zzmq;
    private final boolean zzmr;
    private final int[] zzms;
    private final int[] zzmt;
    private final int[] zzmu;
    private final zzdw zzmv;
    private final zzcy zzmw;
    private final zzex zzmx;
    private final zzbu zzmy;
    private final zzdj zzmz;

    private zzds(int[] iArr, Object[] objArr, int i, int i2, int i3, zzdo zzdoVar, boolean z, boolean z2, int[] iArr2, int[] iArr3, int[] iArr4, zzdw zzdwVar, zzcy zzcyVar, zzex zzexVar, zzbu zzbuVar, zzdj zzdjVar) {
        this.zzmi = iArr;
        this.zzmj = objArr;
        this.zzmk = i;
        this.zzml = i2;
        this.zzmm = i3;
        this.zzmp = zzdoVar instanceof zzcg;
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

    private static int zza(int i, byte[] bArr, int i2, int i3, Object obj, zzay zzayVar) {
        return zzax.zza(i, bArr, i2, i3, zzn(obj), zzayVar);
    }

    private static int zza(zzef zzefVar, int i, byte[] bArr, int i2, int i3, zzcn zzcnVar, zzay zzayVar) {
        int zza = zza(zzefVar, bArr, i2, i3, zzayVar);
        while (true) {
            zzcnVar.add(zzayVar.zzff);
            if (zza >= i3) {
                break;
            }
            int zza2 = zzax.zza(bArr, zza, zzayVar);
            if (i != zzayVar.zzfd) {
                break;
            }
            zza = zza(zzefVar, bArr, zza2, i3, zzayVar);
        }
        return zza;
    }

    private static int zza(zzef zzefVar, byte[] bArr, int i, int i2, int i3, zzay zzayVar) {
        zzds zzdsVar = (zzds) zzefVar;
        Object newInstance = zzdsVar.newInstance();
        int zza = zzdsVar.zza(newInstance, bArr, i, i2, i3, zzayVar);
        zzdsVar.zzc(newInstance);
        zzayVar.zzff = newInstance;
        return zza;
    }

    private static int zza(zzef zzefVar, byte[] bArr, int i, int i2, zzay zzayVar) {
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

    private static int zza(zzex zzexVar, Object obj) {
        return zzexVar.zzm(zzexVar.zzq(obj));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0023. Please report as an issue. */
    private final int zza(Object obj, byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6, int i7, long j, int i8, zzay zzayVar) {
        Object valueOf;
        Object valueOf2;
        int zzb;
        long j2;
        int i9;
        Object valueOf3;
        int i10;
        Unsafe unsafe = zzmh;
        long j3 = this.zzmi[i8 + 2] & 1048575;
        switch (i7) {
            case 51:
                if (i5 == 1) {
                    valueOf = Double.valueOf(zzax.zze(bArr, i));
                    unsafe.putObject(obj, j, valueOf);
                    zzb = i + 8;
                    unsafe.putInt(obj, j3, i4);
                    return zzb;
                }
                return i;
            case 52:
                if (i5 == 5) {
                    valueOf2 = Float.valueOf(zzax.zzf(bArr, i));
                    unsafe.putObject(obj, j, valueOf2);
                    zzb = i + 4;
                    unsafe.putInt(obj, j3, i4);
                    return zzb;
                }
                return i;
            case 53:
            case 54:
                if (i5 == 0) {
                    zzb = zzax.zzb(bArr, i, zzayVar);
                    j2 = zzayVar.zzfe;
                    valueOf3 = Long.valueOf(j2);
                    unsafe.putObject(obj, j, valueOf3);
                    unsafe.putInt(obj, j3, i4);
                    return zzb;
                }
                return i;
            case 55:
            case 62:
                if (i5 == 0) {
                    zzb = zzax.zza(bArr, i, zzayVar);
                    i9 = zzayVar.zzfd;
                    valueOf3 = Integer.valueOf(i9);
                    unsafe.putObject(obj, j, valueOf3);
                    unsafe.putInt(obj, j3, i4);
                    return zzb;
                }
                return i;
            case 56:
            case 65:
                if (i5 == 1) {
                    valueOf = Long.valueOf(zzax.zzd(bArr, i));
                    unsafe.putObject(obj, j, valueOf);
                    zzb = i + 8;
                    unsafe.putInt(obj, j3, i4);
                    return zzb;
                }
                return i;
            case 57:
            case 64:
                if (i5 == 5) {
                    valueOf2 = Integer.valueOf(zzax.zzc(bArr, i));
                    unsafe.putObject(obj, j, valueOf2);
                    zzb = i + 4;
                    unsafe.putInt(obj, j3, i4);
                    return zzb;
                }
                return i;
            case 58:
                if (i5 == 0) {
                    zzb = zzax.zzb(bArr, i, zzayVar);
                    valueOf3 = Boolean.valueOf(zzayVar.zzfe != 0);
                    unsafe.putObject(obj, j, valueOf3);
                    unsafe.putInt(obj, j3, i4);
                    return zzb;
                }
                return i;
            case 59:
                if (i5 == 2) {
                    zzb = zzax.zza(bArr, i, zzayVar);
                    i10 = zzayVar.zzfd;
                    if (i10 == 0) {
                        valueOf3 = "";
                        unsafe.putObject(obj, j, valueOf3);
                        unsafe.putInt(obj, j3, i4);
                        return zzb;
                    }
                    if ((i6 & 536870912) != 0 && !zzff.zze(bArr, zzb, zzb + i10)) {
                        throw zzco.zzbp();
                    }
                    unsafe.putObject(obj, j, new String(bArr, zzb, i10, zzci.UTF_8));
                    zzb += i10;
                    unsafe.putInt(obj, j3, i4);
                    return zzb;
                }
                return i;
            case 60:
                if (i5 == 2) {
                    zzb = zza(zzad(i8), bArr, i, i2, zzayVar);
                    Object object = unsafe.getInt(obj, j3) == i4 ? unsafe.getObject(obj, j) : null;
                    valueOf3 = zzayVar.zzff;
                    if (object != null) {
                        valueOf3 = zzci.zza(object, valueOf3);
                    }
                    unsafe.putObject(obj, j, valueOf3);
                    unsafe.putInt(obj, j3, i4);
                    return zzb;
                }
                return i;
            case 61:
                if (i5 == 2) {
                    zzb = zzax.zza(bArr, i, zzayVar);
                    i10 = zzayVar.zzfd;
                    if (i10 == 0) {
                        valueOf3 = zzbb.zzfi;
                        unsafe.putObject(obj, j, valueOf3);
                        unsafe.putInt(obj, j3, i4);
                        return zzb;
                    }
                    unsafe.putObject(obj, j, zzbb.zzb(bArr, zzb, i10));
                    zzb += i10;
                    unsafe.putInt(obj, j3, i4);
                    return zzb;
                }
                return i;
            case 63:
                if (i5 == 0) {
                    int zza = zzax.zza(bArr, i, zzayVar);
                    int i11 = zzayVar.zzfd;
                    zzck zzaf = zzaf(i8);
                    if (zzaf != null && zzaf.zzb(i11) == null) {
                        zzn(obj).zzb(i3, Long.valueOf(i11));
                        return zza;
                    }
                    unsafe.putObject(obj, j, Integer.valueOf(i11));
                    zzb = zza;
                    unsafe.putInt(obj, j3, i4);
                    return zzb;
                }
                return i;
            case 66:
                if (i5 == 0) {
                    zzb = zzax.zza(bArr, i, zzayVar);
                    i9 = zzbk.zzm(zzayVar.zzfd);
                    valueOf3 = Integer.valueOf(i9);
                    unsafe.putObject(obj, j, valueOf3);
                    unsafe.putInt(obj, j3, i4);
                    return zzb;
                }
                return i;
            case 67:
                if (i5 == 0) {
                    zzb = zzax.zzb(bArr, i, zzayVar);
                    j2 = zzbk.zza(zzayVar.zzfe);
                    valueOf3 = Long.valueOf(j2);
                    unsafe.putObject(obj, j, valueOf3);
                    unsafe.putInt(obj, j3, i4);
                    return zzb;
                }
                return i;
            case 68:
                if (i5 == 3) {
                    zzb = zza(zzad(i8), bArr, i, i2, (i3 & (-8)) | 4, zzayVar);
                    Object object2 = unsafe.getInt(obj, j3) == i4 ? unsafe.getObject(obj, j) : null;
                    valueOf3 = zzayVar.zzff;
                    if (object2 != null) {
                        valueOf3 = zzci.zza(object2, valueOf3);
                    }
                    unsafe.putObject(obj, j, valueOf3);
                    unsafe.putInt(obj, j3, i4);
                    return zzb;
                }
                return i;
            default:
                return i;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:59:0x011a, code lost:
    
        if (r4 == 0) goto L62;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x011c, code lost:
    
        r12.add(com.google.android.gms.internal.clearcut.zzbb.zzfi);
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x012a, code lost:
    
        if (r1 >= r19) goto L197;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x012c, code lost:
    
        r4 = com.google.android.gms.internal.clearcut.zzax.zza(r17, r1, r29);
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0132, code lost:
    
        if (r20 != r29.zzfd) goto L196;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0134, code lost:
    
        r1 = com.google.android.gms.internal.clearcut.zzax.zza(r17, r4, r29);
        r4 = r29.zzfd;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x013a, code lost:
    
        if (r4 != 0) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0122, code lost:
    
        r12.add(com.google.android.gms.internal.clearcut.zzbb.zzb(r17, r1, r4));
        r1 = r1 + r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:?, code lost:
    
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:?, code lost:
    
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x012a, code lost:
    
        r12.add(com.google.android.gms.internal.clearcut.zzbb.zzb(r17, r1, r4));
        r1 = r1 + r4;
     */
    /* JADX WARN: Removed duplicated region for block: B:101:0x01b5  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x017f  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:61:0x013a -> B:56:0x011c). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:75:0x018d -> B:70:0x016e). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:86:0x01c3 -> B:81:0x019c). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final int zza(Object obj, byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6, long j, int i7, long j2, zzay zzayVar) {
        int zza;
        int i8 = i;
        Unsafe unsafe = zzmh;
        zzcn zzcnVar = (zzcn) unsafe.getObject(obj, j2);
        if (!zzcnVar.zzu()) {
            int size = zzcnVar.size();
            zzcnVar = zzcnVar.zzi(size == 0 ? 10 : size << 1);
            unsafe.putObject(obj, j2, zzcnVar);
        }
        switch (i7) {
            case 18:
            case 35:
                if (i5 == 2) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcnVar);
                    int zza2 = zzax.zza(bArr, i8, zzayVar);
                    int i9 = zzayVar.zzfd + zza2;
                    if (zza2 < i9) {
                        zzax.zze(bArr, zza2);
                        throw null;
                    }
                    if (zza2 == i9) {
                        return zza2;
                    }
                    throw zzco.zzbl();
                }
                if (i5 == 1) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcnVar);
                    zzax.zze(bArr, i);
                    throw null;
                }
                break;
            case 19:
            case 36:
                if (i5 == 2) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcnVar);
                    int zza3 = zzax.zza(bArr, i8, zzayVar);
                    int i10 = zzayVar.zzfd + zza3;
                    if (zza3 < i10) {
                        zzax.zzf(bArr, zza3);
                        throw null;
                    }
                    if (zza3 == i10) {
                        return zza3;
                    }
                    throw zzco.zzbl();
                }
                if (i5 == 5) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcnVar);
                    zzax.zzf(bArr, i);
                    throw null;
                }
                break;
            case 20:
            case 21:
            case 37:
            case 38:
                if (i5 == 2) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcnVar);
                    int zza4 = zzax.zza(bArr, i8, zzayVar);
                    int i11 = zzayVar.zzfd + zza4;
                    if (zza4 < i11) {
                        zzax.zzb(bArr, zza4, zzayVar);
                        throw null;
                    }
                    if (zza4 == i11) {
                        return zza4;
                    }
                    throw zzco.zzbl();
                }
                if (i5 == 0) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcnVar);
                    zzax.zzb(bArr, i8, zzayVar);
                    long j3 = zzayVar.zzfe;
                    throw null;
                }
                break;
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
                break;
            case 23:
            case 32:
            case 40:
            case 46:
                if (i5 == 2) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcnVar);
                    int zza5 = zzax.zza(bArr, i8, zzayVar);
                    int i12 = zzayVar.zzfd + zza5;
                    if (zza5 < i12) {
                        zzax.zzd(bArr, zza5);
                        throw null;
                    }
                    if (zza5 == i12) {
                        return zza5;
                    }
                    throw zzco.zzbl();
                }
                if (i5 == 1) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcnVar);
                    zzax.zzd(bArr, i);
                    throw null;
                }
                break;
            case 24:
            case 31:
            case 41:
            case 45:
                if (i5 == 2) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcnVar);
                    int zza6 = zzax.zza(bArr, i8, zzayVar);
                    int i13 = zzayVar.zzfd + zza6;
                    if (zza6 < i13) {
                        zzax.zzc(bArr, zza6);
                        throw null;
                    }
                    if (zza6 == i13) {
                        return zza6;
                    }
                    throw zzco.zzbl();
                }
                if (i5 == 5) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcnVar);
                    zzax.zzc(bArr, i);
                    throw null;
                }
                break;
            case 25:
            case 42:
                if (i5 == 2) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcnVar);
                    int zza7 = zzax.zza(bArr, i8, zzayVar);
                    int i14 = zzayVar.zzfd + zza7;
                    if (zza7 < i14) {
                        zzax.zzb(bArr, zza7, zzayVar);
                        throw null;
                    }
                    if (zza7 == i14) {
                        return zza7;
                    }
                    throw zzco.zzbl();
                }
                if (i5 == 0) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcnVar);
                    zzax.zzb(bArr, i8, zzayVar);
                    long j4 = zzayVar.zzfe;
                    throw null;
                }
                break;
            case 26:
                if (i5 == 2) {
                    long j5 = j & 536870912;
                    i8 = zzax.zza(bArr, i8, zzayVar);
                    if (j5 == 0) {
                        int i15 = zzayVar.zzfd;
                        if (i15 != 0) {
                            zzcnVar.add(new String(bArr, i8, i15, zzci.UTF_8));
                            i8 += i15;
                            while (i8 < i2) {
                                int zza8 = zzax.zza(bArr, i8, zzayVar);
                                if (i3 != zzayVar.zzfd) {
                                    break;
                                } else {
                                    i8 = zzax.zza(bArr, zza8, zzayVar);
                                    int i16 = zzayVar.zzfd;
                                    if (i16 != 0) {
                                        zzcnVar.add(new String(bArr, i8, i16, zzci.UTF_8));
                                        i8 += i16;
                                    }
                                }
                            }
                            break;
                        }
                        zzcnVar.add("");
                        while (i8 < i2) {
                        }
                    } else {
                        int i17 = zzayVar.zzfd;
                        if (i17 != 0) {
                            int i18 = i8 + i17;
                            if (!zzff.zze(bArr, i8, i18)) {
                                throw zzco.zzbp();
                            }
                            zzcnVar.add(new String(bArr, i8, i17, zzci.UTF_8));
                            i8 = i18;
                            while (i8 < i2) {
                                int zza9 = zzax.zza(bArr, i8, zzayVar);
                                if (i3 != zzayVar.zzfd) {
                                    break;
                                } else {
                                    i8 = zzax.zza(bArr, zza9, zzayVar);
                                    int i19 = zzayVar.zzfd;
                                    if (i19 != 0) {
                                        int i20 = i8 + i19;
                                        if (!zzff.zze(bArr, i8, i20)) {
                                            throw zzco.zzbp();
                                        }
                                        zzcnVar.add(new String(bArr, i8, i19, zzci.UTF_8));
                                        i8 = i20;
                                    }
                                }
                            }
                            break;
                        }
                        zzcnVar.add("");
                        while (i8 < i2) {
                        }
                    }
                }
                break;
            case 27:
                if (i5 == 2) {
                    return zza(zzad(i6), i3, bArr, i, i2, zzcnVar, zzayVar);
                }
                break;
            case 28:
                if (i5 == 2) {
                    int zza10 = zzax.zza(bArr, i8, zzayVar);
                    int i21 = zzayVar.zzfd;
                    break;
                }
                break;
            case 30:
            case 44:
                if (i5 == 2) {
                    zza = zzax.zza(bArr, i8, zzcnVar, zzayVar);
                } else if (i5 == 0) {
                    zza = zzax.zza(i3, bArr, i, i2, zzcnVar, zzayVar);
                }
                zzcg zzcgVar = (zzcg) obj;
                zzey zzeyVar = zzcgVar.zzjp;
                zzey zzeyVar2 = (zzey) zzeh.zza(i4, zzcnVar, zzaf(i6), zzeyVar != zzey.zzea() ? zzeyVar : null, this.zzmx);
                if (zzeyVar2 != null) {
                    zzcgVar.zzjp = zzeyVar2;
                }
                return zza;
            case 33:
            case 47:
                if (i5 == 2) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcnVar);
                    int zza11 = zzax.zza(bArr, i8, zzayVar);
                    int i22 = zzayVar.zzfd + zza11;
                    if (zza11 >= i22) {
                        if (zza11 == i22) {
                            return zza11;
                        }
                        throw zzco.zzbl();
                    }
                    zzax.zza(bArr, zza11, zzayVar);
                    zzbk.zzm(zzayVar.zzfd);
                    throw null;
                }
                if (i5 == 0) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcnVar);
                    zzax.zza(bArr, i8, zzayVar);
                    zzbk.zzm(zzayVar.zzfd);
                    throw null;
                }
                break;
            case 34:
            case 48:
                if (i5 == 2) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcnVar);
                    int zza12 = zzax.zza(bArr, i8, zzayVar);
                    int i23 = zzayVar.zzfd + zza12;
                    if (zza12 >= i23) {
                        if (zza12 == i23) {
                            return zza12;
                        }
                        throw zzco.zzbl();
                    }
                    zzax.zzb(bArr, zza12, zzayVar);
                    zzbk.zza(zzayVar.zzfe);
                    throw null;
                }
                if (i5 == 0) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcnVar);
                    zzax.zzb(bArr, i8, zzayVar);
                    zzbk.zza(zzayVar.zzfe);
                    throw null;
                }
                break;
            case 49:
                if (i5 == 3) {
                    zzef zzad = zzad(i6);
                    int i24 = (i3 & (-8)) | 4;
                    i8 = zza(zzad, bArr, i, i2, i24, zzayVar);
                    while (true) {
                        zzcnVar.add(zzayVar.zzff);
                        if (i8 >= i2) {
                            break;
                        } else {
                            int zza13 = zzax.zza(bArr, i8, zzayVar);
                            if (i3 != zzayVar.zzfd) {
                                break;
                            } else {
                                i8 = zza(zzad, bArr, zza13, i2, i24, zzayVar);
                            }
                        }
                    }
                }
                break;
        }
        return i8;
    }

    private final int zza(Object obj, byte[] bArr, int i, int i2, int i3, int i4, long j, zzay zzayVar) {
        Unsafe unsafe = zzmh;
        Object zzae = zzae(i3);
        Object object = unsafe.getObject(obj, j);
        if (this.zzmz.zzi(object)) {
            Object zzk = this.zzmz.zzk(zzae);
            this.zzmz.zzb(zzk, object);
            unsafe.putObject(obj, j, zzk);
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

    /* JADX WARN: Failed to find 'out' block for switch in B:97:0x0068. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0374 A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final int zza(Object obj, byte[] bArr, int i, int i2, int i3, zzay zzayVar) {
        Unsafe unsafe;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        Object obj2;
        zzck zzaf;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        zzay zzayVar2;
        int i16;
        int i17;
        int i18;
        long j;
        Object obj3;
        zzay zzayVar3;
        int zze;
        zzds zzdsVar = this;
        Object obj4 = obj;
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
                byte b = bArr2[i22];
                if (b < 0) {
                    i10 = zzax.zza(b, bArr2, i26, zzayVar4);
                    i9 = zzayVar4.zzfd;
                } else {
                    i9 = b;
                    i10 = i26;
                }
                int i27 = i9 >>> 3;
                int i28 = i9 & 7;
                int zzai = zzdsVar.zzai(i27);
                if (zzai != i21) {
                    int[] iArr = zzdsVar.zzmi;
                    int i29 = iArr[zzai + 1];
                    int i30 = (i29 & 267386880) >>> 20;
                    int i31 = i9;
                    long j2 = i29 & 1048575;
                    if (i30 <= 17) {
                        int i32 = iArr[zzai + 2];
                        int i33 = 1 << (i32 >>> 20);
                        int i34 = i32 & 1048575;
                        if (i34 != i25) {
                            if (i25 != -1) {
                                unsafe2.putInt(obj4, i25, i24);
                            }
                            i24 = unsafe2.getInt(obj4, i34);
                            i25 = i34;
                        }
                        switch (i30) {
                            case 0:
                                i6 = i31;
                                zzayVar2 = zzayVar;
                                i16 = i10;
                                i17 = i25;
                                bArr2 = bArr;
                                if (i28 == 1) {
                                    zzfd.zza(obj4, j2, zzax.zze(bArr2, i16));
                                    i22 = i16 + 8;
                                    i24 |= i33;
                                    i25 = i17;
                                    i19 = i2;
                                    i23 = i6;
                                    zzayVar4 = zzayVar2;
                                    i21 = -1;
                                    i20 = i3;
                                    break;
                                } else {
                                    i25 = i17;
                                    i4 = i3;
                                    i15 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4 && i4 != 0) {
                                        i7 = i25;
                                        i8 = -1;
                                        i5 = i15;
                                        break;
                                    } else {
                                        i22 = zza(i6, bArr, i15, i2, obj, zzayVar);
                                        zzdsVar = this;
                                        obj4 = obj;
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
                                i16 = i10;
                                i17 = i25;
                                bArr2 = bArr;
                                if (i28 == 5) {
                                    zzfd.zza(obj4, j2, zzax.zzf(bArr2, i16));
                                    i22 = i16 + 4;
                                    i24 |= i33;
                                    i25 = i17;
                                    i19 = i2;
                                    i23 = i6;
                                    zzayVar4 = zzayVar2;
                                    i21 = -1;
                                    i20 = i3;
                                    break;
                                } else {
                                    i25 = i17;
                                    i4 = i3;
                                    i15 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i22 = zza(i6, bArr, i15, i2, obj, zzayVar);
                                    zzdsVar = this;
                                    obj4 = obj;
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
                                i16 = i10;
                                i17 = i25;
                                bArr2 = bArr;
                                if (i28 == 0) {
                                    int zzb = zzax.zzb(bArr2, i16, zzayVar);
                                    unsafe2.putLong(obj, j2, zzayVar.zzfe);
                                    i24 |= i33;
                                    i25 = i17;
                                    i19 = i2;
                                    i23 = i6;
                                    zzayVar4 = zzayVar;
                                    i22 = zzb;
                                    i21 = -1;
                                    i20 = i3;
                                    break;
                                } else {
                                    i25 = i17;
                                    i4 = i3;
                                    i15 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i22 = zza(i6, bArr, i15, i2, obj, zzayVar);
                                    zzdsVar = this;
                                    obj4 = obj;
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
                                i16 = i10;
                                i17 = i25;
                                bArr2 = bArr;
                                if (i28 == 0) {
                                    i22 = zzax.zza(bArr2, i16, zzayVar2);
                                    unsafe2.putInt(obj4, j2, zzayVar2.zzfd);
                                    i24 |= i33;
                                    i25 = i17;
                                    i19 = i2;
                                    i23 = i6;
                                    zzayVar4 = zzayVar2;
                                    i21 = -1;
                                    i20 = i3;
                                    break;
                                } else {
                                    i25 = i17;
                                    i4 = i3;
                                    i15 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i22 = zza(i6, bArr, i15, i2, obj, zzayVar);
                                    zzdsVar = this;
                                    obj4 = obj;
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
                                i17 = i25;
                                bArr2 = bArr;
                                if (i28 == 1) {
                                    unsafe2.putLong(obj, j2, zzax.zzd(bArr2, i10));
                                    i22 = i10 + 8;
                                    i24 |= i33;
                                    i25 = i17;
                                    i19 = i2;
                                    i23 = i6;
                                    zzayVar4 = zzayVar2;
                                    i21 = -1;
                                    i20 = i3;
                                    break;
                                } else {
                                    i16 = i10;
                                    i25 = i17;
                                    i4 = i3;
                                    i15 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i22 = zza(i6, bArr, i15, i2, obj, zzayVar);
                                    zzdsVar = this;
                                    obj4 = obj;
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
                                i18 = i2;
                                zzayVar2 = zzayVar;
                                i17 = i25;
                                bArr2 = bArr;
                                if (i28 == 5) {
                                    unsafe2.putInt(obj4, j2, zzax.zzc(bArr2, i10));
                                    i22 = i10 + 4;
                                    i24 |= i33;
                                    i25 = i17;
                                    i19 = i18;
                                    i23 = i6;
                                    zzayVar4 = zzayVar2;
                                    i21 = -1;
                                    i20 = i3;
                                    break;
                                } else {
                                    i16 = i10;
                                    i25 = i17;
                                    i4 = i3;
                                    i15 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i22 = zza(i6, bArr, i15, i2, obj, zzayVar);
                                    zzdsVar = this;
                                    obj4 = obj;
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
                                i18 = i2;
                                zzayVar2 = zzayVar;
                                i17 = i25;
                                bArr2 = bArr;
                                if (i28 == 0) {
                                    i22 = zzax.zzb(bArr2, i10, zzayVar2);
                                    zzfd.zza(obj4, j2, zzayVar2.zzfe != 0);
                                    i24 |= i33;
                                    i25 = i17;
                                    i19 = i18;
                                    i23 = i6;
                                    zzayVar4 = zzayVar2;
                                    i21 = -1;
                                    i20 = i3;
                                    break;
                                } else {
                                    i16 = i10;
                                    i25 = i17;
                                    i4 = i3;
                                    i15 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i22 = zza(i6, bArr, i15, i2, obj, zzayVar);
                                    zzdsVar = this;
                                    obj4 = obj;
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
                                i18 = i2;
                                zzayVar2 = zzayVar;
                                i17 = i25;
                                j = j2;
                                bArr2 = bArr;
                                if (i28 == 2) {
                                    i22 = (i29 & 536870912) == 0 ? zzax.zzc(bArr2, i10, zzayVar2) : zzax.zzd(bArr2, i10, zzayVar2);
                                    obj3 = zzayVar2.zzff;
                                    unsafe2.putObject(obj4, j, obj3);
                                    i24 |= i33;
                                    i25 = i17;
                                    i19 = i18;
                                    i23 = i6;
                                    zzayVar4 = zzayVar2;
                                    i21 = -1;
                                    i20 = i3;
                                    break;
                                } else {
                                    i16 = i10;
                                    i25 = i17;
                                    i4 = i3;
                                    i15 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i22 = zza(i6, bArr, i15, i2, obj, zzayVar);
                                    zzdsVar = this;
                                    obj4 = obj;
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
                                i17 = i25;
                                j = j2;
                                bArr2 = bArr;
                                if (i28 == 2) {
                                    i18 = i2;
                                    i22 = zza(zzdsVar.zzad(zzai), bArr2, i10, i18, zzayVar2);
                                    obj3 = (i24 & i33) == 0 ? zzayVar2.zzff : zzci.zza(unsafe2.getObject(obj4, j), zzayVar2.zzff);
                                    unsafe2.putObject(obj4, j, obj3);
                                    i24 |= i33;
                                    i25 = i17;
                                    i19 = i18;
                                    i23 = i6;
                                    zzayVar4 = zzayVar2;
                                    i21 = -1;
                                    i20 = i3;
                                    break;
                                } else {
                                    i16 = i10;
                                    i25 = i17;
                                    i4 = i3;
                                    i15 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i22 = zza(i6, bArr, i15, i2, obj, zzayVar);
                                    zzdsVar = this;
                                    obj4 = obj;
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
                                    zze = zzax.zze(bArr2, i10, zzayVar3);
                                    unsafe2.putObject(obj4, j2, zzayVar3.zzff);
                                    i24 |= i33;
                                    i19 = i2;
                                    i22 = zze;
                                    i23 = i6;
                                    zzayVar4 = zzayVar3;
                                    i20 = i3;
                                    break;
                                } else {
                                    i16 = i10;
                                    i17 = i25;
                                    i25 = i17;
                                    i4 = i3;
                                    i15 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i22 = zza(i6, bArr, i15, i2, obj, zzayVar);
                                    zzdsVar = this;
                                    obj4 = obj;
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
                                    i22 = zzax.zza(bArr2, i10, zzayVar3);
                                    int i35 = zzayVar3.zzfd;
                                    zzck zzaf2 = zzdsVar.zzaf(zzai);
                                    if (zzaf2 == null || zzaf2.zzb(i35) != null) {
                                        unsafe2.putInt(obj4, j2, i35);
                                        i24 |= i33;
                                    } else {
                                        zzn(obj).zzb(i6, Long.valueOf(i35));
                                    }
                                    i19 = i2;
                                    i23 = i6;
                                    zzayVar4 = zzayVar3;
                                    i20 = i3;
                                    break;
                                } else {
                                    i16 = i10;
                                    i17 = i25;
                                    i25 = i17;
                                    i4 = i3;
                                    i15 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i22 = zza(i6, bArr, i15, i2, obj, zzayVar);
                                    zzdsVar = this;
                                    obj4 = obj;
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
                                    zze = zzax.zza(bArr2, i10, zzayVar3);
                                    unsafe2.putInt(obj4, j2, zzbk.zzm(zzayVar3.zzfd));
                                    i24 |= i33;
                                    i19 = i2;
                                    i22 = zze;
                                    i23 = i6;
                                    zzayVar4 = zzayVar3;
                                    i20 = i3;
                                    break;
                                } else {
                                    i16 = i10;
                                    i17 = i25;
                                    i25 = i17;
                                    i4 = i3;
                                    i15 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i22 = zza(i6, bArr, i15, i2, obj, zzayVar);
                                    zzdsVar = this;
                                    obj4 = obj;
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
                                    int zzb2 = zzax.zzb(bArr2, i10, zzayVar);
                                    unsafe2.putLong(obj, j2, zzbk.zza(zzayVar.zzfe));
                                    i24 |= i33;
                                    i23 = i6;
                                    zzayVar4 = zzayVar;
                                    i22 = zzb2;
                                    i19 = i2;
                                    i20 = i3;
                                    break;
                                } else {
                                    i16 = i10;
                                    i17 = i25;
                                    i25 = i17;
                                    i4 = i3;
                                    i15 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i22 = zza(i6, bArr, i15, i2, obj, zzayVar);
                                    zzdsVar = this;
                                    obj4 = obj;
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
                                    i22 = zza(zzdsVar.zzad(zzai), bArr, i10, i2, (i27 << 3) | 4, zzayVar);
                                    zzayVar3 = zzayVar;
                                    unsafe2.putObject(obj4, j2, (i24 & i33) == 0 ? zzayVar3.zzff : zzci.zza(unsafe2.getObject(obj4, j2), zzayVar3.zzff));
                                    i24 |= i33;
                                    bArr2 = bArr;
                                    i19 = i2;
                                    i23 = i6;
                                    zzayVar4 = zzayVar3;
                                    i20 = i3;
                                    break;
                                } else {
                                    i6 = i31;
                                    i16 = i10;
                                    i17 = i25;
                                    i25 = i17;
                                    i4 = i3;
                                    i15 = i16;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i22 = zza(i6, bArr, i15, i2, obj, zzayVar);
                                    zzdsVar = this;
                                    obj4 = obj;
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
                                i16 = i10;
                                i17 = i25;
                                i25 = i17;
                                i4 = i3;
                                i15 = i16;
                                unsafe = unsafe2;
                                if (i6 != i4) {
                                }
                                i22 = zza(i6, bArr, i15, i2, obj, zzayVar);
                                zzdsVar = this;
                                obj4 = obj;
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
                        int i36 = i10;
                        i14 = i25;
                        bArr2 = bArr;
                        if (i30 != 27) {
                            i13 = i24;
                            if (i30 <= 49) {
                                i12 = i31;
                                unsafe = unsafe2;
                                i22 = zza(obj, bArr, i36, i2, i31, i27, i28, zzai, i29, i30, j2, zzayVar);
                                if (i22 == i36) {
                                    i6 = i12;
                                    i4 = i3;
                                    i15 = i22;
                                    i25 = i14;
                                    i24 = i13;
                                } else {
                                    zzdsVar = this;
                                    obj4 = obj;
                                    bArr2 = bArr;
                                    i23 = i12;
                                    i19 = i2;
                                    i20 = i3;
                                    zzayVar4 = zzayVar;
                                    i25 = i14;
                                    i24 = i13;
                                    unsafe2 = unsafe;
                                    i21 = -1;
                                }
                            } else {
                                i11 = i36;
                                i12 = i31;
                                unsafe = unsafe2;
                                if (i30 != 50) {
                                    i22 = zza(obj, bArr, i11, i2, i12, i27, i28, i29, i30, j2, zzai, zzayVar);
                                    if (i22 == i11) {
                                        i6 = i12;
                                        i4 = i3;
                                        i15 = i22;
                                        i25 = i14;
                                        i24 = i13;
                                    } else {
                                        zzdsVar = this;
                                        obj4 = obj;
                                        bArr2 = bArr;
                                        i23 = i12;
                                        i19 = i2;
                                        i20 = i3;
                                        zzayVar4 = zzayVar;
                                        i25 = i14;
                                        i24 = i13;
                                        unsafe2 = unsafe;
                                        i21 = -1;
                                    }
                                } else if (i28 == 2) {
                                    i22 = zza(obj, bArr, i11, i2, zzai, i27, j2, zzayVar);
                                    if (i22 == i11) {
                                        i6 = i12;
                                        i4 = i3;
                                        i15 = i22;
                                        i25 = i14;
                                        i24 = i13;
                                    } else {
                                        zzdsVar = this;
                                        obj4 = obj;
                                        bArr2 = bArr;
                                        i23 = i12;
                                        i19 = i2;
                                        i20 = i3;
                                        zzayVar4 = zzayVar;
                                        i25 = i14;
                                        i24 = i13;
                                        unsafe2 = unsafe;
                                        i21 = -1;
                                    }
                                } else {
                                    i6 = i12;
                                    i4 = i3;
                                    i15 = i11;
                                    i25 = i14;
                                    i24 = i13;
                                }
                            }
                            if (i6 != i4) {
                            }
                            i22 = zza(i6, bArr, i15, i2, obj, zzayVar);
                            zzdsVar = this;
                            obj4 = obj;
                            bArr2 = bArr;
                            i19 = i2;
                            i20 = i4;
                            i23 = i6;
                            unsafe2 = unsafe;
                            i21 = -1;
                            zzayVar4 = zzayVar;
                        } else if (i28 == 2) {
                            zzcn zzcnVar = (zzcn) unsafe2.getObject(obj4, j2);
                            if (!zzcnVar.zzu()) {
                                int size = zzcnVar.size();
                                zzcnVar = zzcnVar.zzi(size == 0 ? 10 : size << 1);
                                unsafe2.putObject(obj4, j2, zzcnVar);
                            }
                            zzcn zzcnVar2 = zzcnVar;
                            zzef zzad = zzdsVar.zzad(zzai);
                            i23 = i31;
                            i22 = zza(zzad, i23, bArr, i36, i2, zzcnVar2, zzayVar);
                            i19 = i2;
                            i20 = i3;
                            i25 = i14;
                            i24 = i24;
                            i21 = -1;
                            zzayVar4 = zzayVar;
                        } else {
                            i13 = i24;
                            i11 = i36;
                            i12 = i31;
                        }
                    }
                } else {
                    i11 = i10;
                    i12 = i9;
                    i13 = i24;
                    i14 = i25;
                }
                unsafe = unsafe2;
                i6 = i12;
                i4 = i3;
                i15 = i11;
                i25 = i14;
                i24 = i13;
                if (i6 != i4) {
                }
                i22 = zza(i6, bArr, i15, i2, obj, zzayVar);
                zzdsVar = this;
                obj4 = obj;
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
            obj2 = obj;
            unsafe.putInt(obj2, i7, i24);
        } else {
            obj2 = obj;
        }
        int[] iArr2 = this.zzmt;
        if (iArr2 != null) {
            Object obj5 = null;
            for (int i38 : iArr2) {
                zzex zzexVar = this.zzmx;
                int i39 = this.zzmi[i38];
                Object zzo = zzfd.zzo(obj2, zzag(i38) & 1048575);
                if (zzo != null && (zzaf = zzaf(i38)) != null) {
                    obj5 = zza(i38, i39, this.zzmz.zzg(zzo), zzaf, obj5, zzexVar);
                }
                obj5 = (zzey) obj5;
            }
            if (obj5 != null) {
                this.zzmx.zzf(obj2, obj5);
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

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00c5  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00d0  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00f0  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0144  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0163  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x016b A[EDGE_INSN: B:50:0x016b->B:71:0x016b BREAK  A[LOOP:0: B:18:0x0066->B:27:0x0167], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x014a  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0117  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x00d3  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x00c8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static zzds zza(Class cls, zzdm zzdmVar, zzdw zzdwVar, zzcy zzcyVar, zzex zzexVar, zzbu zzbuVar, zzdj zzdjVar) {
        int zzcu;
        int i;
        int i2;
        int zza;
        int i3;
        int zzdg;
        int zzcy;
        if (!(zzdmVar instanceof zzec)) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzdmVar);
            throw null;
        }
        zzec zzecVar = (zzec) zzdmVar;
        boolean z = zzecVar.zzcf() == zzcg.zzg.zzkm;
        if (zzecVar.getFieldCount() == 0) {
            zzcu = 0;
            i = 0;
            i2 = 0;
        } else {
            int zzcp = zzecVar.zzcp();
            int zzcq = zzecVar.zzcq();
            zzcu = zzecVar.zzcu();
            i = zzcp;
            i2 = zzcq;
        }
        int[] iArr = new int[zzcu << 2];
        Object[] objArr = new Object[zzcu << 1];
        int[] iArr2 = zzecVar.zzcr() > 0 ? new int[zzecVar.zzcr()] : null;
        int[] iArr3 = zzecVar.zzcs() > 0 ? new int[zzecVar.zzcs()] : null;
        zzed zzco = zzecVar.zzco();
        if (zzco.next()) {
            int zzcx = zzco.zzcx();
            int i4 = 0;
            int i5 = 0;
            int i6 = 0;
            while (true) {
                if (zzcx >= zzecVar.zzcv() || i4 >= ((zzcx - i) << 2)) {
                    if (zzco.zzda()) {
                        zza = (int) zzfd.zza(zzco.zzdb());
                        i3 = (int) zzfd.zza(zzco.zzdc());
                    } else {
                        zza = (int) zzfd.zza(zzco.zzdd());
                        if (zzco.zzde()) {
                            i3 = (int) zzfd.zza(zzco.zzdf());
                            zzdg = zzco.zzdg();
                            iArr[i4] = zzco.zzcx();
                            int i7 = i4 + 1;
                            iArr[i7] = (!zzco.zzdi() ? 536870912 : 0) | (!zzco.zzdh() ? 268435456 : 0) | (zzco.zzcy() << 20) | zza;
                            iArr[i4 + 2] = i3 | (zzdg << 20);
                            if (zzco.zzdl() == null) {
                                int i8 = (i4 / 4) << 1;
                                objArr[i8] = zzco.zzdl();
                                if (zzco.zzdj() != null) {
                                    objArr[i8 + 1] = zzco.zzdj();
                                } else if (zzco.zzdk() != null) {
                                    objArr[i8 + 1] = zzco.zzdk();
                                }
                            } else if (zzco.zzdj() != null) {
                                objArr[((i4 / 4) << 1) + 1] = zzco.zzdj();
                            } else if (zzco.zzdk() != null) {
                                objArr[((i4 / 4) << 1) + 1] = zzco.zzdk();
                            }
                            zzcy = zzco.zzcy();
                            if (zzcy != zzcb.zziw.ordinal()) {
                                iArr2[i5] = i4;
                                i5++;
                            } else if (zzcy >= 18 && zzcy <= 49) {
                                iArr3[i6] = iArr[i7] & 1048575;
                                i6++;
                            }
                            if (zzco.next()) {
                                break;
                            }
                            zzcx = zzco.zzcx();
                        } else {
                            i3 = 0;
                        }
                    }
                    zzdg = 0;
                    iArr[i4] = zzco.zzcx();
                    int i72 = i4 + 1;
                    iArr[i72] = (!zzco.zzdi() ? 536870912 : 0) | (!zzco.zzdh() ? 268435456 : 0) | (zzco.zzcy() << 20) | zza;
                    iArr[i4 + 2] = i3 | (zzdg << 20);
                    if (zzco.zzdl() == null) {
                    }
                    zzcy = zzco.zzcy();
                    if (zzcy != zzcb.zziw.ordinal()) {
                    }
                    if (zzco.next()) {
                    }
                } else {
                    for (int i9 = 0; i9 < 4; i9++) {
                        iArr[i4 + i9] = -1;
                    }
                }
                i4 += 4;
            }
        }
        return new zzds(iArr, objArr, i, i2, zzecVar.zzcv(), zzecVar.zzch(), z, false, zzecVar.zzct(), iArr2, iArr3, zzdwVar, zzcyVar, zzexVar, zzbuVar, zzdjVar);
    }

    private final Object zza(int i, int i2, Map map, zzck zzckVar, Object obj, zzex zzexVar) {
        this.zzmz.zzl(zzae(i));
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (zzckVar.zzb(((Integer) entry.getValue()).intValue()) == null) {
                if (obj == null) {
                    obj = zzexVar.zzdz();
                }
                zzbg zzk = zzbb.zzk(zzdg.zza(null, entry.getKey(), entry.getValue()));
                try {
                    zzdg.zza(zzk.zzae(), null, entry.getKey(), entry.getValue());
                    zzexVar.zza(obj, i2, zzk.zzad());
                    it.remove();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return obj;
    }

    private static void zza(int i, Object obj, zzfr zzfrVar) {
        if (obj instanceof String) {
            zzfrVar.zza(i, (String) obj);
        } else {
            zzfrVar.zza(i, (zzbb) obj);
        }
    }

    private static void zza(zzex zzexVar, Object obj, zzfr zzfrVar) {
        zzexVar.zza(zzexVar.zzq(obj), zzfrVar);
    }

    private final void zza(zzfr zzfrVar, int i, Object obj, int i2) {
        if (obj != null) {
            this.zzmz.zzl(zzae(i2));
            zzfrVar.zza(i, (zzdh) null, this.zzmz.zzh(obj));
        }
    }

    private final void zza(Object obj, Object obj2, int i) {
        long zzag = zzag(i) & 1048575;
        if (zza(obj2, i)) {
            Object zzo = zzfd.zzo(obj, zzag);
            Object zzo2 = zzfd.zzo(obj2, zzag);
            if (zzo != null && zzo2 != null) {
                zzfd.zza(obj, zzag, zzci.zza(zzo, zzo2));
                zzb(obj, i);
            } else if (zzo2 != null) {
                zzfd.zza(obj, zzag, zzo2);
                zzb(obj, i);
            }
        }
    }

    private final boolean zza(Object obj, int i) {
        if (!this.zzmq) {
            int zzah = zzah(i);
            return (zzfd.zzj(obj, (long) (zzah & 1048575)) & (1 << (zzah >>> 20))) != 0;
        }
        int zzag = zzag(i);
        long j = zzag & 1048575;
        switch ((zzag & 267386880) >>> 20) {
            case 0:
                return zzfd.zzn(obj, j) != 0.0d;
            case 1:
                return zzfd.zzm(obj, j) != 0.0f;
            case 2:
                return zzfd.zzk(obj, j) != 0;
            case 3:
                return zzfd.zzk(obj, j) != 0;
            case 4:
                return zzfd.zzj(obj, j) != 0;
            case 5:
                return zzfd.zzk(obj, j) != 0;
            case 6:
                return zzfd.zzj(obj, j) != 0;
            case 7:
                return zzfd.zzl(obj, j);
            case 8:
                Object zzo = zzfd.zzo(obj, j);
                if (zzo instanceof String) {
                    return !((String) zzo).isEmpty();
                }
                if (zzo instanceof zzbb) {
                    return !zzbb.zzfi.equals(zzo);
                }
                throw new IllegalArgumentException();
            case 9:
                return zzfd.zzo(obj, j) != null;
            case 10:
                return !zzbb.zzfi.equals(zzfd.zzo(obj, j));
            case 11:
                return zzfd.zzj(obj, j) != 0;
            case 12:
                return zzfd.zzj(obj, j) != 0;
            case 13:
                return zzfd.zzj(obj, j) != 0;
            case 14:
                return zzfd.zzk(obj, j) != 0;
            case 15:
                return zzfd.zzj(obj, j) != 0;
            case 16:
                return zzfd.zzk(obj, j) != 0;
            case 17:
                return zzfd.zzo(obj, j) != null;
            default:
                throw new IllegalArgumentException();
        }
    }

    private final boolean zza(Object obj, int i, int i2) {
        return zzfd.zzj(obj, (long) (zzah(i2) & 1048575)) == i;
    }

    private final boolean zza(Object obj, int i, int i2, int i3) {
        return this.zzmq ? zza(obj, i) : (i2 & i3) != 0;
    }

    private static boolean zza(Object obj, int i, zzef zzefVar) {
        return zzefVar.zzo(zzfd.zzo(obj, i & 1048575));
    }

    private final zzef zzad(int i) {
        int i2 = (i / 4) << 1;
        zzef zzefVar = (zzef) this.zzmj[i2];
        if (zzefVar != null) {
            return zzefVar;
        }
        zzef zze = zzea.zzcm().zze((Class) this.zzmj[i2 + 1]);
        this.zzmj[i2] = zze;
        return zze;
    }

    private final Object zzae(int i) {
        return this.zzmj[(i / 4) << 1];
    }

    private final zzck zzaf(int i) {
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
                if (this.zzmi[i4] == i) {
                    return i4;
                }
                return -1;
            }
            if (i <= this.zzml) {
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

    private final void zzb(Object obj, int i) {
        if (this.zzmq) {
            return;
        }
        int zzah = zzah(i);
        long j = zzah & 1048575;
        zzfd.zza(obj, j, zzfd.zzj(obj, j) | (1 << (zzah >>> 20)));
    }

    private final void zzb(Object obj, int i, int i2) {
        zzfd.zza(obj, zzah(i2) & 1048575, i);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:31:0x007b. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:230:0x045e  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x002d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void zzb(Object obj, zzfr zzfrVar) {
        Iterator it;
        Map.Entry entry;
        int length;
        int i;
        int i2;
        boolean z;
        int i3;
        boolean z2;
        int i4;
        boolean z3;
        int i5;
        boolean z4;
        int i6;
        boolean z5;
        int i7;
        int i8;
        List list;
        boolean z6;
        if (this.zzmo) {
            zzby zza = this.zzmy.zza(obj);
            if (!zza.isEmpty()) {
                it = zza.iterator();
                entry = (Map.Entry) it.next();
                length = this.zzmi.length;
                Unsafe unsafe = zzmh;
                int i9 = -1;
                int i10 = 0;
                for (i = 0; i < length; i += 4) {
                    int zzag = zzag(i);
                    int[] iArr = this.zzmi;
                    int i11 = iArr[i];
                    int i12 = (267386880 & zzag) >>> 20;
                    if (this.zzmq || i12 > 17) {
                        i2 = 0;
                    } else {
                        int i13 = iArr[i + 2];
                        int i14 = i13 & 1048575;
                        if (i14 != i9) {
                            i10 = unsafe.getInt(obj, i14);
                            i9 = i14;
                        }
                        i2 = 1 << (i13 >>> 20);
                    }
                    while (entry != null && this.zzmy.zza(entry) <= i11) {
                        this.zzmy.zza(zzfrVar, entry);
                        entry = it.hasNext() ? (Map.Entry) it.next() : null;
                    }
                    long j = zzag & 1048575;
                    switch (i12) {
                        case 0:
                            if ((i2 & i10) != 0) {
                                zzfrVar.zza(i11, zzfd.zzn(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 1:
                            if ((i2 & i10) != 0) {
                                zzfrVar.zza(i11, zzfd.zzm(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 2:
                            if ((i2 & i10) != 0) {
                                zzfrVar.zzi(i11, unsafe.getLong(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 3:
                            if ((i2 & i10) != 0) {
                                zzfrVar.zza(i11, unsafe.getLong(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 4:
                            if ((i2 & i10) != 0) {
                                zzfrVar.zzc(i11, unsafe.getInt(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 5:
                            if ((i2 & i10) != 0) {
                                zzfrVar.zzc(i11, unsafe.getLong(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 6:
                            if ((i2 & i10) != 0) {
                                zzfrVar.zzf(i11, unsafe.getInt(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 7:
                            if ((i2 & i10) != 0) {
                                zzfrVar.zzb(i11, zzfd.zzl(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 8:
                            if ((i2 & i10) != 0) {
                                zza(i11, unsafe.getObject(obj, j), zzfrVar);
                                break;
                            } else {
                                break;
                            }
                        case 9:
                            if ((i2 & i10) != 0) {
                                zzfrVar.zza(i11, unsafe.getObject(obj, j), zzad(i));
                                break;
                            } else {
                                break;
                            }
                        case 10:
                            if ((i2 & i10) != 0) {
                                zzfrVar.zza(i11, (zzbb) unsafe.getObject(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 11:
                            if ((i2 & i10) != 0) {
                                zzfrVar.zzd(i11, unsafe.getInt(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 12:
                            if ((i2 & i10) != 0) {
                                zzfrVar.zzn(i11, unsafe.getInt(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 13:
                            if ((i2 & i10) != 0) {
                                zzfrVar.zzm(i11, unsafe.getInt(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 14:
                            if ((i2 & i10) != 0) {
                                zzfrVar.zzj(i11, unsafe.getLong(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 15:
                            if ((i2 & i10) != 0) {
                                zzfrVar.zze(i11, unsafe.getInt(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 16:
                            if ((i2 & i10) != 0) {
                                zzfrVar.zzb(i11, unsafe.getLong(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 17:
                            if ((i2 & i10) != 0) {
                                zzfrVar.zzb(i11, unsafe.getObject(obj, j), zzad(i));
                                break;
                            } else {
                                break;
                            }
                        case 18:
                            zzeh.zza(this.zzmi[i], (List) unsafe.getObject(obj, j), zzfrVar, false);
                            break;
                        case 19:
                            zzeh.zzb(this.zzmi[i], (List) unsafe.getObject(obj, j), zzfrVar, false);
                            break;
                        case 20:
                            zzeh.zzc(this.zzmi[i], (List) unsafe.getObject(obj, j), zzfrVar, false);
                            break;
                        case 21:
                            zzeh.zzd(this.zzmi[i], (List) unsafe.getObject(obj, j), zzfrVar, false);
                            break;
                        case 22:
                            zzeh.zzh(this.zzmi[i], (List) unsafe.getObject(obj, j), zzfrVar, false);
                            break;
                        case 23:
                            zzeh.zzf(this.zzmi[i], (List) unsafe.getObject(obj, j), zzfrVar, false);
                            break;
                        case 24:
                            zzeh.zzk(this.zzmi[i], (List) unsafe.getObject(obj, j), zzfrVar, false);
                            break;
                        case 25:
                            zzeh.zzn(this.zzmi[i], (List) unsafe.getObject(obj, j), zzfrVar, false);
                            break;
                        case 26:
                            zzeh.zza(this.zzmi[i], (List) unsafe.getObject(obj, j), zzfrVar);
                            break;
                        case 27:
                            zzeh.zza(this.zzmi[i], (List) unsafe.getObject(obj, j), zzfrVar, zzad(i));
                            break;
                        case 28:
                            zzeh.zzb(this.zzmi[i], (List) unsafe.getObject(obj, j), zzfrVar);
                            break;
                        case 29:
                            z = false;
                            i3 = this.zzmi[i];
                            zzeh.zzi(i3, (List) unsafe.getObject(obj, j), zzfrVar, z);
                            break;
                        case 30:
                            z2 = false;
                            i4 = this.zzmi[i];
                            zzeh.zzm(i4, (List) unsafe.getObject(obj, j), zzfrVar, z2);
                            break;
                        case 31:
                            z3 = false;
                            i5 = this.zzmi[i];
                            zzeh.zzl(i5, (List) unsafe.getObject(obj, j), zzfrVar, z3);
                            break;
                        case 32:
                            z4 = false;
                            i6 = this.zzmi[i];
                            zzeh.zzg(i6, (List) unsafe.getObject(obj, j), zzfrVar, z4);
                            break;
                        case 33:
                            z5 = false;
                            i7 = this.zzmi[i];
                            zzeh.zzj(i7, (List) unsafe.getObject(obj, j), zzfrVar, z5);
                            break;
                        case 34:
                            i8 = this.zzmi[i];
                            list = (List) unsafe.getObject(obj, j);
                            z6 = false;
                            zzeh.zze(i8, list, zzfrVar, z6);
                            break;
                        case 35:
                            zzeh.zza(this.zzmi[i], (List) unsafe.getObject(obj, j), zzfrVar, true);
                            break;
                        case 36:
                            zzeh.zzb(this.zzmi[i], (List) unsafe.getObject(obj, j), zzfrVar, true);
                            break;
                        case 37:
                            zzeh.zzc(this.zzmi[i], (List) unsafe.getObject(obj, j), zzfrVar, true);
                            break;
                        case 38:
                            zzeh.zzd(this.zzmi[i], (List) unsafe.getObject(obj, j), zzfrVar, true);
                            break;
                        case 39:
                            zzeh.zzh(this.zzmi[i], (List) unsafe.getObject(obj, j), zzfrVar, true);
                            break;
                        case 40:
                            zzeh.zzf(this.zzmi[i], (List) unsafe.getObject(obj, j), zzfrVar, true);
                            break;
                        case 41:
                            zzeh.zzk(this.zzmi[i], (List) unsafe.getObject(obj, j), zzfrVar, true);
                            break;
                        case 42:
                            zzeh.zzn(this.zzmi[i], (List) unsafe.getObject(obj, j), zzfrVar, true);
                            break;
                        case 43:
                            z = true;
                            i3 = this.zzmi[i];
                            zzeh.zzi(i3, (List) unsafe.getObject(obj, j), zzfrVar, z);
                            break;
                        case 44:
                            z2 = true;
                            i4 = this.zzmi[i];
                            zzeh.zzm(i4, (List) unsafe.getObject(obj, j), zzfrVar, z2);
                            break;
                        case 45:
                            z3 = true;
                            i5 = this.zzmi[i];
                            zzeh.zzl(i5, (List) unsafe.getObject(obj, j), zzfrVar, z3);
                            break;
                        case 46:
                            z4 = true;
                            i6 = this.zzmi[i];
                            zzeh.zzg(i6, (List) unsafe.getObject(obj, j), zzfrVar, z4);
                            break;
                        case 47:
                            z5 = true;
                            i7 = this.zzmi[i];
                            zzeh.zzj(i7, (List) unsafe.getObject(obj, j), zzfrVar, z5);
                            break;
                        case 48:
                            i8 = this.zzmi[i];
                            list = (List) unsafe.getObject(obj, j);
                            z6 = true;
                            zzeh.zze(i8, list, zzfrVar, z6);
                            break;
                        case 49:
                            zzeh.zzb(this.zzmi[i], (List) unsafe.getObject(obj, j), zzfrVar, zzad(i));
                            break;
                        case 50:
                            zza(zzfrVar, i11, unsafe.getObject(obj, j), i);
                            break;
                        case 51:
                            if (zza(obj, i11, i)) {
                                zzfrVar.zza(i11, zze(obj, j));
                            }
                            break;
                        case 52:
                            if (zza(obj, i11, i)) {
                                zzfrVar.zza(i11, zzf(obj, j));
                            }
                            break;
                        case 53:
                            if (zza(obj, i11, i)) {
                                zzfrVar.zzi(i11, zzh(obj, j));
                            }
                            break;
                        case 54:
                            if (zza(obj, i11, i)) {
                                zzfrVar.zza(i11, zzh(obj, j));
                            }
                            break;
                        case 55:
                            if (zza(obj, i11, i)) {
                                zzfrVar.zzc(i11, zzg(obj, j));
                            }
                            break;
                        case 56:
                            if (zza(obj, i11, i)) {
                                zzfrVar.zzc(i11, zzh(obj, j));
                            }
                            break;
                        case 57:
                            if (zza(obj, i11, i)) {
                                zzfrVar.zzf(i11, zzg(obj, j));
                            }
                            break;
                        case 58:
                            if (zza(obj, i11, i)) {
                                zzfrVar.zzb(i11, zzi(obj, j));
                            }
                            break;
                        case 59:
                            if (zza(obj, i11, i)) {
                                zza(i11, unsafe.getObject(obj, j), zzfrVar);
                            }
                            break;
                        case 60:
                            if (zza(obj, i11, i)) {
                                zzfrVar.zza(i11, unsafe.getObject(obj, j), zzad(i));
                            }
                            break;
                        case 61:
                            if (zza(obj, i11, i)) {
                                zzfrVar.zza(i11, (zzbb) unsafe.getObject(obj, j));
                            }
                            break;
                        case 62:
                            if (zza(obj, i11, i)) {
                                zzfrVar.zzd(i11, zzg(obj, j));
                            }
                            break;
                        case 63:
                            if (zza(obj, i11, i)) {
                                zzfrVar.zzn(i11, zzg(obj, j));
                            }
                            break;
                        case 64:
                            if (zza(obj, i11, i)) {
                                zzfrVar.zzm(i11, zzg(obj, j));
                            }
                            break;
                        case 65:
                            if (zza(obj, i11, i)) {
                                zzfrVar.zzj(i11, zzh(obj, j));
                            }
                            break;
                        case 66:
                            if (zza(obj, i11, i)) {
                                zzfrVar.zze(i11, zzg(obj, j));
                            }
                            break;
                        case 67:
                            if (zza(obj, i11, i)) {
                                zzfrVar.zzb(i11, zzh(obj, j));
                            }
                            break;
                        case 68:
                            if (zza(obj, i11, i)) {
                                zzfrVar.zzb(i11, unsafe.getObject(obj, j), zzad(i));
                            }
                            break;
                    }
                }
                while (entry != null) {
                    this.zzmy.zza(zzfrVar, entry);
                    entry = it.hasNext() ? (Map.Entry) it.next() : null;
                }
                zza(this.zzmx, obj, zzfrVar);
            }
        }
        it = null;
        entry = null;
        length = this.zzmi.length;
        Unsafe unsafe2 = zzmh;
        int i92 = -1;
        int i102 = 0;
        while (i < length) {
        }
        while (entry != null) {
        }
        zza(this.zzmx, obj, zzfrVar);
    }

    private final void zzb(Object obj, Object obj2, int i) {
        int zzag = zzag(i);
        int i2 = this.zzmi[i];
        long j = zzag & 1048575;
        if (zza(obj2, i2, i)) {
            Object zzo = zzfd.zzo(obj, j);
            Object zzo2 = zzfd.zzo(obj2, j);
            if (zzo != null && zzo2 != null) {
                zzfd.zza(obj, j, zzci.zza(zzo, zzo2));
                zzb(obj, i2, i);
            } else if (zzo2 != null) {
                zzfd.zza(obj, j, zzo2);
                zzb(obj, i2, i);
            }
        }
    }

    private final boolean zzc(Object obj, Object obj2, int i) {
        return zza(obj, i) == zza(obj2, i);
    }

    private static List zzd(Object obj, long j) {
        return (List) zzfd.zzo(obj, j);
    }

    private static double zze(Object obj, long j) {
        return ((Double) zzfd.zzo(obj, j)).doubleValue();
    }

    private static float zzf(Object obj, long j) {
        return ((Float) zzfd.zzo(obj, j)).floatValue();
    }

    private static int zzg(Object obj, long j) {
        return ((Integer) zzfd.zzo(obj, j)).intValue();
    }

    private static long zzh(Object obj, long j) {
        return ((Long) zzfd.zzo(obj, j)).longValue();
    }

    private static boolean zzi(Object obj, long j) {
        return ((Boolean) zzfd.zzo(obj, j)).booleanValue();
    }

    private static zzey zzn(Object obj) {
        zzcg zzcgVar = (zzcg) obj;
        zzey zzeyVar = zzcgVar.zzjp;
        if (zzeyVar != zzey.zzea()) {
            return zzeyVar;
        }
        zzey zzeb = zzey.zzeb();
        zzcgVar.zzjp = zzeb;
        return zzeb;
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x005d, code lost:
    
        if (com.google.android.gms.internal.clearcut.zzeh.zzd(com.google.android.gms.internal.clearcut.zzfd.zzo(r10, r6), com.google.android.gms.internal.clearcut.zzfd.zzo(r11, r6)) != false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0070, code lost:
    
        if (com.google.android.gms.internal.clearcut.zzfd.zzk(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzk(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0081, code lost:
    
        if (com.google.android.gms.internal.clearcut.zzfd.zzj(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzj(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0094, code lost:
    
        if (com.google.android.gms.internal.clearcut.zzfd.zzk(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzk(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00a5, code lost:
    
        if (com.google.android.gms.internal.clearcut.zzfd.zzj(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzj(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00b6, code lost:
    
        if (com.google.android.gms.internal.clearcut.zzfd.zzj(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzj(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00c7, code lost:
    
        if (com.google.android.gms.internal.clearcut.zzfd.zzj(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzj(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00dd, code lost:
    
        if (com.google.android.gms.internal.clearcut.zzeh.zzd(com.google.android.gms.internal.clearcut.zzfd.zzo(r10, r6), com.google.android.gms.internal.clearcut.zzfd.zzo(r11, r6)) != false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00f3, code lost:
    
        if (com.google.android.gms.internal.clearcut.zzeh.zzd(com.google.android.gms.internal.clearcut.zzfd.zzo(r10, r6), com.google.android.gms.internal.clearcut.zzfd.zzo(r11, r6)) != false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0109, code lost:
    
        if (com.google.android.gms.internal.clearcut.zzeh.zzd(com.google.android.gms.internal.clearcut.zzfd.zzo(r10, r6), com.google.android.gms.internal.clearcut.zzfd.zzo(r11, r6)) != false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x011b, code lost:
    
        if (com.google.android.gms.internal.clearcut.zzfd.zzl(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzl(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x012d, code lost:
    
        if (com.google.android.gms.internal.clearcut.zzfd.zzj(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzj(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0141, code lost:
    
        if (com.google.android.gms.internal.clearcut.zzfd.zzk(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzk(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0153, code lost:
    
        if (com.google.android.gms.internal.clearcut.zzfd.zzj(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzj(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0167, code lost:
    
        if (com.google.android.gms.internal.clearcut.zzfd.zzk(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzk(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x017b, code lost:
    
        if (com.google.android.gms.internal.clearcut.zzfd.zzk(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzk(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x018d, code lost:
    
        if (com.google.android.gms.internal.clearcut.zzfd.zzj(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzj(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x01a1, code lost:
    
        if (com.google.android.gms.internal.clearcut.zzfd.zzk(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzk(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0038, code lost:
    
        if (com.google.android.gms.internal.clearcut.zzeh.zzd(com.google.android.gms.internal.clearcut.zzfd.zzo(r10, r6), com.google.android.gms.internal.clearcut.zzfd.zzo(r11, r6)) != false) goto L104;
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:5:0x0017. Please report as an issue. */
    @Override // com.google.android.gms.internal.clearcut.zzef
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final boolean equals(Object obj, Object obj2) {
        int length = this.zzmi.length;
        int i = 0;
        while (true) {
            boolean z = true;
            if (i >= length) {
                if (!this.zzmx.zzq(obj).equals(this.zzmx.zzq(obj2))) {
                    return false;
                }
                if (this.zzmo) {
                    return this.zzmy.zza(obj).equals(this.zzmy.zza(obj2));
                }
                return true;
            }
            int zzag = zzag(i);
            long j = zzag & 1048575;
            switch ((zzag & 267386880) >>> 20) {
                case 0:
                    if (zzc(obj, obj2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 1:
                    if (zzc(obj, obj2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 2:
                    if (zzc(obj, obj2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 3:
                    if (zzc(obj, obj2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 4:
                    if (zzc(obj, obj2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 5:
                    if (zzc(obj, obj2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 6:
                    if (zzc(obj, obj2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 7:
                    if (zzc(obj, obj2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 8:
                    if (zzc(obj, obj2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 9:
                    if (zzc(obj, obj2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 10:
                    if (zzc(obj, obj2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 11:
                    if (zzc(obj, obj2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 12:
                    if (zzc(obj, obj2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 13:
                    if (zzc(obj, obj2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 14:
                    if (zzc(obj, obj2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 15:
                    if (zzc(obj, obj2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 16:
                    if (zzc(obj, obj2, i)) {
                        break;
                    }
                    z = false;
                    break;
                case 17:
                    if (zzc(obj, obj2, i)) {
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
                    z = zzeh.zzd(zzfd.zzo(obj, j), zzfd.zzo(obj2, j));
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
                    if (zzfd.zzj(obj, zzah) == zzfd.zzj(obj2, zzah)) {
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
    /* JADX WARN: Code restructure failed: missing block: B:76:0x00f2, code lost:
    
        if (r3 != null) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x00f4, code lost:
    
        r7 = r3.hashCode();
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x00f8, code lost:
    
        r2 = (r2 * 53) + r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x0110, code lost:
    
        if (r3 != null) goto L69;
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x001b. Please report as an issue. */
    @Override // com.google.android.gms.internal.clearcut.zzef
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final int hashCode(Object obj) {
        int i;
        double zzn;
        float zzm;
        boolean zzl;
        Object zzo;
        int zzj;
        long zzk;
        Object zzo2;
        int length = this.zzmi.length;
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3 += 4) {
            int zzag = zzag(i3);
            int i4 = this.zzmi[i3];
            long j = 1048575 & zzag;
            int i5 = 37;
            switch ((zzag & 267386880) >>> 20) {
                case 0:
                    i = i2 * 53;
                    zzn = zzfd.zzn(obj, j);
                    zzk = Double.doubleToLongBits(zzn);
                    zzj = zzci.zzl(zzk);
                    i2 = i + zzj;
                    break;
                case 1:
                    i = i2 * 53;
                    zzm = zzfd.zzm(obj, j);
                    zzj = Float.floatToIntBits(zzm);
                    i2 = i + zzj;
                    break;
                case 2:
                case 3:
                case 5:
                case 14:
                case 16:
                    i = i2 * 53;
                    zzk = zzfd.zzk(obj, j);
                    zzj = zzci.zzl(zzk);
                    i2 = i + zzj;
                    break;
                case 4:
                case 6:
                case 11:
                case 12:
                case 13:
                case 15:
                    i = i2 * 53;
                    zzj = zzfd.zzj(obj, j);
                    i2 = i + zzj;
                    break;
                case 7:
                    i = i2 * 53;
                    zzl = zzfd.zzl(obj, j);
                    zzj = zzci.zzc(zzl);
                    i2 = i + zzj;
                    break;
                case 8:
                    i = i2 * 53;
                    zzj = ((String) zzfd.zzo(obj, j)).hashCode();
                    i2 = i + zzj;
                    break;
                case 9:
                    zzo = zzfd.zzo(obj, j);
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
                    i = i2 * 53;
                    zzo2 = zzfd.zzo(obj, j);
                    zzj = zzo2.hashCode();
                    i2 = i + zzj;
                    break;
                case 17:
                    zzo = zzfd.zzo(obj, j);
                    break;
                case 51:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        zzn = zze(obj, j);
                        zzk = Double.doubleToLongBits(zzn);
                        zzj = zzci.zzl(zzk);
                        i2 = i + zzj;
                        break;
                    } else {
                        break;
                    }
                case 52:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        zzm = zzf(obj, j);
                        zzj = Float.floatToIntBits(zzm);
                        i2 = i + zzj;
                        break;
                    } else {
                        break;
                    }
                case 53:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzk = zzh(obj, j);
                    zzj = zzci.zzl(zzk);
                    i2 = i + zzj;
                    break;
                case 54:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzk = zzh(obj, j);
                    zzj = zzci.zzl(zzk);
                    i2 = i + zzj;
                    break;
                case 55:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzj = zzg(obj, j);
                    i2 = i + zzj;
                    break;
                case 56:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzk = zzh(obj, j);
                    zzj = zzci.zzl(zzk);
                    i2 = i + zzj;
                    break;
                case 57:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzj = zzg(obj, j);
                    i2 = i + zzj;
                    break;
                case 58:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        zzl = zzi(obj, j);
                        zzj = zzci.zzc(zzl);
                        i2 = i + zzj;
                        break;
                    } else {
                        break;
                    }
                case 59:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzj = ((String) zzfd.zzo(obj, j)).hashCode();
                    i2 = i + zzj;
                    break;
                case 60:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    zzo2 = zzfd.zzo(obj, j);
                    i = i2 * 53;
                    zzj = zzo2.hashCode();
                    i2 = i + zzj;
                    break;
                case 61:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzo2 = zzfd.zzo(obj, j);
                    zzj = zzo2.hashCode();
                    i2 = i + zzj;
                    break;
                case 62:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzj = zzg(obj, j);
                    i2 = i + zzj;
                    break;
                case 63:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzj = zzg(obj, j);
                    i2 = i + zzj;
                    break;
                case 64:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzj = zzg(obj, j);
                    i2 = i + zzj;
                    break;
                case 65:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzk = zzh(obj, j);
                    zzj = zzci.zzl(zzk);
                    i2 = i + zzj;
                    break;
                case 66:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzj = zzg(obj, j);
                    i2 = i + zzj;
                    break;
                case 67:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzk = zzh(obj, j);
                    zzj = zzci.zzl(zzk);
                    i2 = i + zzj;
                    break;
                case 68:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    zzo2 = zzfd.zzo(obj, j);
                    i = i2 * 53;
                    zzj = zzo2.hashCode();
                    i2 = i + zzj;
                    break;
            }
        }
        int hashCode = (i2 * 53) + this.zzmx.zzq(obj).hashCode();
        return this.zzmo ? (hashCode * 53) + this.zzmy.zza(obj).hashCode() : hashCode;
    }

    @Override // com.google.android.gms.internal.clearcut.zzef
    public final Object newInstance() {
        return this.zzmv.newInstance(this.zzmn);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:25:0x0063. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:306:0x0528. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0039  */
    /* JADX WARN: Removed duplicated region for block: B:269:0x04c1  */
    /* JADX WARN: Removed duplicated region for block: B:291:0x04fe  */
    /* JADX WARN: Removed duplicated region for block: B:550:0x0986  */
    @Override // com.google.android.gms.internal.clearcut.zzef
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void zza(Object obj, zzfr zzfrVar) {
        Iterator it;
        Map.Entry entry;
        int length;
        int i;
        double zzn;
        float zzm;
        long zzk;
        long zzk2;
        int zzj;
        long zzk3;
        int zzj2;
        boolean zzl;
        int zzj3;
        int zzj4;
        int zzj5;
        long zzk4;
        int zzj6;
        long zzk5;
        Iterator it2;
        Map.Entry entry2;
        int length2;
        double zzn2;
        float zzm2;
        long zzk6;
        long zzk7;
        int zzj7;
        long zzk8;
        int zzj8;
        boolean zzl2;
        int zzj9;
        int zzj10;
        int zzj11;
        long zzk9;
        int zzj12;
        long zzk10;
        if (zzfrVar.zzaj() == zzcg.zzg.zzkp) {
            zza(this.zzmx, obj, zzfrVar);
            if (this.zzmo) {
                zzby zza = this.zzmy.zza(obj);
                if (!zza.isEmpty()) {
                    it2 = zza.descendingIterator();
                    entry2 = (Map.Entry) it2.next();
                    for (length2 = this.zzmi.length - 4; length2 >= 0; length2 -= 4) {
                        int zzag = zzag(length2);
                        int i2 = this.zzmi[length2];
                        while (entry2 != null && this.zzmy.zza(entry2) > i2) {
                            this.zzmy.zza(zzfrVar, entry2);
                            entry2 = it2.hasNext() ? (Map.Entry) it2.next() : null;
                        }
                        switch ((zzag & 267386880) >>> 20) {
                            case 0:
                                if (zza(obj, length2)) {
                                    zzn2 = zzfd.zzn(obj, zzag & 1048575);
                                    zzfrVar.zza(i2, zzn2);
                                    break;
                                } else {
                                    break;
                                }
                            case 1:
                                if (zza(obj, length2)) {
                                    zzm2 = zzfd.zzm(obj, zzag & 1048575);
                                    zzfrVar.zza(i2, zzm2);
                                    break;
                                } else {
                                    break;
                                }
                            case 2:
                                if (zza(obj, length2)) {
                                    zzk6 = zzfd.zzk(obj, zzag & 1048575);
                                    zzfrVar.zzi(i2, zzk6);
                                    break;
                                } else {
                                    break;
                                }
                            case 3:
                                if (zza(obj, length2)) {
                                    zzk7 = zzfd.zzk(obj, zzag & 1048575);
                                    zzfrVar.zza(i2, zzk7);
                                    break;
                                } else {
                                    break;
                                }
                            case 4:
                                if (zza(obj, length2)) {
                                    zzj7 = zzfd.zzj(obj, zzag & 1048575);
                                    zzfrVar.zzc(i2, zzj7);
                                    break;
                                } else {
                                    break;
                                }
                            case 5:
                                if (zza(obj, length2)) {
                                    zzk8 = zzfd.zzk(obj, zzag & 1048575);
                                    zzfrVar.zzc(i2, zzk8);
                                    break;
                                } else {
                                    break;
                                }
                            case 6:
                                if (zza(obj, length2)) {
                                    zzj8 = zzfd.zzj(obj, zzag & 1048575);
                                    zzfrVar.zzf(i2, zzj8);
                                    break;
                                } else {
                                    break;
                                }
                            case 7:
                                if (zza(obj, length2)) {
                                    zzl2 = zzfd.zzl(obj, zzag & 1048575);
                                    zzfrVar.zzb(i2, zzl2);
                                    break;
                                } else {
                                    break;
                                }
                            case 8:
                                if (!zza(obj, length2)) {
                                    break;
                                }
                                zza(i2, zzfd.zzo(obj, zzag & 1048575), zzfrVar);
                                break;
                            case 9:
                                if (!zza(obj, length2)) {
                                    break;
                                }
                                zzfrVar.zza(i2, zzfd.zzo(obj, zzag & 1048575), zzad(length2));
                                break;
                            case 10:
                                if (!zza(obj, length2)) {
                                    break;
                                }
                                zzfrVar.zza(i2, (zzbb) zzfd.zzo(obj, zzag & 1048575));
                                break;
                            case 11:
                                if (zza(obj, length2)) {
                                    zzj9 = zzfd.zzj(obj, zzag & 1048575);
                                    zzfrVar.zzd(i2, zzj9);
                                    break;
                                } else {
                                    break;
                                }
                            case 12:
                                if (zza(obj, length2)) {
                                    zzj10 = zzfd.zzj(obj, zzag & 1048575);
                                    zzfrVar.zzn(i2, zzj10);
                                    break;
                                } else {
                                    break;
                                }
                            case 13:
                                if (zza(obj, length2)) {
                                    zzj11 = zzfd.zzj(obj, zzag & 1048575);
                                    zzfrVar.zzm(i2, zzj11);
                                    break;
                                } else {
                                    break;
                                }
                            case 14:
                                if (zza(obj, length2)) {
                                    zzk9 = zzfd.zzk(obj, zzag & 1048575);
                                    zzfrVar.zzj(i2, zzk9);
                                    break;
                                } else {
                                    break;
                                }
                            case 15:
                                if (zza(obj, length2)) {
                                    zzj12 = zzfd.zzj(obj, zzag & 1048575);
                                    zzfrVar.zze(i2, zzj12);
                                    break;
                                } else {
                                    break;
                                }
                            case 16:
                                if (zza(obj, length2)) {
                                    zzk10 = zzfd.zzk(obj, zzag & 1048575);
                                    zzfrVar.zzb(i2, zzk10);
                                    break;
                                } else {
                                    break;
                                }
                            case 17:
                                if (!zza(obj, length2)) {
                                    break;
                                }
                                zzfrVar.zzb(i2, zzfd.zzo(obj, zzag & 1048575), zzad(length2));
                                break;
                            case 18:
                                zzeh.zza(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, false);
                                break;
                            case 19:
                                zzeh.zzb(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, false);
                                break;
                            case 20:
                                zzeh.zzc(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, false);
                                break;
                            case 21:
                                zzeh.zzd(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, false);
                                break;
                            case 22:
                                zzeh.zzh(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, false);
                                break;
                            case 23:
                                zzeh.zzf(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, false);
                                break;
                            case 24:
                                zzeh.zzk(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, false);
                                break;
                            case 25:
                                zzeh.zzn(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, false);
                                break;
                            case 26:
                                zzeh.zza(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar);
                                break;
                            case 27:
                                zzeh.zza(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, zzad(length2));
                                break;
                            case 28:
                                zzeh.zzb(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar);
                                break;
                            case 29:
                                zzeh.zzi(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, false);
                                break;
                            case 30:
                                zzeh.zzm(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, false);
                                break;
                            case 31:
                                zzeh.zzl(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, false);
                                break;
                            case 32:
                                zzeh.zzg(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, false);
                                break;
                            case 33:
                                zzeh.zzj(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, false);
                                break;
                            case 34:
                                zzeh.zze(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, false);
                                break;
                            case 35:
                                zzeh.zza(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, true);
                                break;
                            case 36:
                                zzeh.zzb(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, true);
                                break;
                            case 37:
                                zzeh.zzc(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, true);
                                break;
                            case 38:
                                zzeh.zzd(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, true);
                                break;
                            case 39:
                                zzeh.zzh(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, true);
                                break;
                            case 40:
                                zzeh.zzf(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, true);
                                break;
                            case 41:
                                zzeh.zzk(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, true);
                                break;
                            case 42:
                                zzeh.zzn(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, true);
                                break;
                            case 43:
                                zzeh.zzi(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, true);
                                break;
                            case 44:
                                zzeh.zzm(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, true);
                                break;
                            case 45:
                                zzeh.zzl(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, true);
                                break;
                            case 46:
                                zzeh.zzg(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, true);
                                break;
                            case 47:
                                zzeh.zzj(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, true);
                                break;
                            case 48:
                                zzeh.zze(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, true);
                                break;
                            case 49:
                                zzeh.zzb(this.zzmi[length2], (List) zzfd.zzo(obj, zzag & 1048575), zzfrVar, zzad(length2));
                                break;
                            case 50:
                                zza(zzfrVar, i2, zzfd.zzo(obj, zzag & 1048575), length2);
                                break;
                            case 51:
                                if (zza(obj, i2, length2)) {
                                    zzn2 = zze(obj, zzag & 1048575);
                                    zzfrVar.zza(i2, zzn2);
                                    break;
                                } else {
                                    break;
                                }
                            case 52:
                                if (zza(obj, i2, length2)) {
                                    zzm2 = zzf(obj, zzag & 1048575);
                                    zzfrVar.zza(i2, zzm2);
                                    break;
                                } else {
                                    break;
                                }
                            case 53:
                                if (zza(obj, i2, length2)) {
                                    zzk6 = zzh(obj, zzag & 1048575);
                                    zzfrVar.zzi(i2, zzk6);
                                    break;
                                } else {
                                    break;
                                }
                            case 54:
                                if (zza(obj, i2, length2)) {
                                    zzk7 = zzh(obj, zzag & 1048575);
                                    zzfrVar.zza(i2, zzk7);
                                    break;
                                } else {
                                    break;
                                }
                            case 55:
                                if (zza(obj, i2, length2)) {
                                    zzj7 = zzg(obj, zzag & 1048575);
                                    zzfrVar.zzc(i2, zzj7);
                                    break;
                                } else {
                                    break;
                                }
                            case 56:
                                if (zza(obj, i2, length2)) {
                                    zzk8 = zzh(obj, zzag & 1048575);
                                    zzfrVar.zzc(i2, zzk8);
                                    break;
                                } else {
                                    break;
                                }
                            case 57:
                                if (zza(obj, i2, length2)) {
                                    zzj8 = zzg(obj, zzag & 1048575);
                                    zzfrVar.zzf(i2, zzj8);
                                    break;
                                } else {
                                    break;
                                }
                            case 58:
                                if (zza(obj, i2, length2)) {
                                    zzl2 = zzi(obj, zzag & 1048575);
                                    zzfrVar.zzb(i2, zzl2);
                                    break;
                                } else {
                                    break;
                                }
                            case 59:
                                if (!zza(obj, i2, length2)) {
                                    break;
                                }
                                zza(i2, zzfd.zzo(obj, zzag & 1048575), zzfrVar);
                                break;
                            case 60:
                                if (!zza(obj, i2, length2)) {
                                    break;
                                }
                                zzfrVar.zza(i2, zzfd.zzo(obj, zzag & 1048575), zzad(length2));
                                break;
                            case 61:
                                if (!zza(obj, i2, length2)) {
                                    break;
                                }
                                zzfrVar.zza(i2, (zzbb) zzfd.zzo(obj, zzag & 1048575));
                                break;
                            case 62:
                                if (zza(obj, i2, length2)) {
                                    zzj9 = zzg(obj, zzag & 1048575);
                                    zzfrVar.zzd(i2, zzj9);
                                    break;
                                } else {
                                    break;
                                }
                            case 63:
                                if (zza(obj, i2, length2)) {
                                    zzj10 = zzg(obj, zzag & 1048575);
                                    zzfrVar.zzn(i2, zzj10);
                                    break;
                                } else {
                                    break;
                                }
                            case 64:
                                if (zza(obj, i2, length2)) {
                                    zzj11 = zzg(obj, zzag & 1048575);
                                    zzfrVar.zzm(i2, zzj11);
                                    break;
                                } else {
                                    break;
                                }
                            case 65:
                                if (zza(obj, i2, length2)) {
                                    zzk9 = zzh(obj, zzag & 1048575);
                                    zzfrVar.zzj(i2, zzk9);
                                    break;
                                } else {
                                    break;
                                }
                            case 66:
                                if (zza(obj, i2, length2)) {
                                    zzj12 = zzg(obj, zzag & 1048575);
                                    zzfrVar.zze(i2, zzj12);
                                    break;
                                } else {
                                    break;
                                }
                            case 67:
                                if (zza(obj, i2, length2)) {
                                    zzk10 = zzh(obj, zzag & 1048575);
                                    zzfrVar.zzb(i2, zzk10);
                                    break;
                                } else {
                                    break;
                                }
                            case 68:
                                if (!zza(obj, i2, length2)) {
                                    break;
                                }
                                zzfrVar.zzb(i2, zzfd.zzo(obj, zzag & 1048575), zzad(length2));
                                break;
                        }
                    }
                    while (entry2 != null) {
                        this.zzmy.zza(zzfrVar, entry2);
                        entry2 = it2.hasNext() ? (Map.Entry) it2.next() : null;
                    }
                    return;
                }
            }
            it2 = null;
            entry2 = null;
            while (length2 >= 0) {
            }
            while (entry2 != null) {
            }
            return;
        }
        if (!this.zzmq) {
            zzb(obj, zzfrVar);
            return;
        }
        if (this.zzmo) {
            zzby zza2 = this.zzmy.zza(obj);
            if (!zza2.isEmpty()) {
                it = zza2.iterator();
                entry = (Map.Entry) it.next();
                length = this.zzmi.length;
                for (i = 0; i < length; i += 4) {
                    int zzag2 = zzag(i);
                    int i3 = this.zzmi[i];
                    while (entry != null && this.zzmy.zza(entry) <= i3) {
                        this.zzmy.zza(zzfrVar, entry);
                        entry = it.hasNext() ? (Map.Entry) it.next() : null;
                    }
                    switch ((zzag2 & 267386880) >>> 20) {
                        case 0:
                            if (zza(obj, i)) {
                                zzn = zzfd.zzn(obj, zzag2 & 1048575);
                                zzfrVar.zza(i3, zzn);
                                break;
                            } else {
                                break;
                            }
                        case 1:
                            if (zza(obj, i)) {
                                zzm = zzfd.zzm(obj, zzag2 & 1048575);
                                zzfrVar.zza(i3, zzm);
                                break;
                            } else {
                                break;
                            }
                        case 2:
                            if (zza(obj, i)) {
                                zzk = zzfd.zzk(obj, zzag2 & 1048575);
                                zzfrVar.zzi(i3, zzk);
                                break;
                            } else {
                                break;
                            }
                        case 3:
                            if (zza(obj, i)) {
                                zzk2 = zzfd.zzk(obj, zzag2 & 1048575);
                                zzfrVar.zza(i3, zzk2);
                                break;
                            } else {
                                break;
                            }
                        case 4:
                            if (zza(obj, i)) {
                                zzj = zzfd.zzj(obj, zzag2 & 1048575);
                                zzfrVar.zzc(i3, zzj);
                                break;
                            } else {
                                break;
                            }
                        case 5:
                            if (zza(obj, i)) {
                                zzk3 = zzfd.zzk(obj, zzag2 & 1048575);
                                zzfrVar.zzc(i3, zzk3);
                                break;
                            } else {
                                break;
                            }
                        case 6:
                            if (zza(obj, i)) {
                                zzj2 = zzfd.zzj(obj, zzag2 & 1048575);
                                zzfrVar.zzf(i3, zzj2);
                                break;
                            } else {
                                break;
                            }
                        case 7:
                            if (zza(obj, i)) {
                                zzl = zzfd.zzl(obj, zzag2 & 1048575);
                                zzfrVar.zzb(i3, zzl);
                                break;
                            } else {
                                break;
                            }
                        case 8:
                            if (!zza(obj, i)) {
                                break;
                            }
                            zza(i3, zzfd.zzo(obj, zzag2 & 1048575), zzfrVar);
                            break;
                        case 9:
                            if (!zza(obj, i)) {
                                break;
                            }
                            zzfrVar.zza(i3, zzfd.zzo(obj, zzag2 & 1048575), zzad(i));
                            break;
                        case 10:
                            if (!zza(obj, i)) {
                                break;
                            }
                            zzfrVar.zza(i3, (zzbb) zzfd.zzo(obj, zzag2 & 1048575));
                            break;
                        case 11:
                            if (zza(obj, i)) {
                                zzj3 = zzfd.zzj(obj, zzag2 & 1048575);
                                zzfrVar.zzd(i3, zzj3);
                                break;
                            } else {
                                break;
                            }
                        case 12:
                            if (zza(obj, i)) {
                                zzj4 = zzfd.zzj(obj, zzag2 & 1048575);
                                zzfrVar.zzn(i3, zzj4);
                                break;
                            } else {
                                break;
                            }
                        case 13:
                            if (zza(obj, i)) {
                                zzj5 = zzfd.zzj(obj, zzag2 & 1048575);
                                zzfrVar.zzm(i3, zzj5);
                                break;
                            } else {
                                break;
                            }
                        case 14:
                            if (zza(obj, i)) {
                                zzk4 = zzfd.zzk(obj, zzag2 & 1048575);
                                zzfrVar.zzj(i3, zzk4);
                                break;
                            } else {
                                break;
                            }
                        case 15:
                            if (zza(obj, i)) {
                                zzj6 = zzfd.zzj(obj, zzag2 & 1048575);
                                zzfrVar.zze(i3, zzj6);
                                break;
                            } else {
                                break;
                            }
                        case 16:
                            if (zza(obj, i)) {
                                zzk5 = zzfd.zzk(obj, zzag2 & 1048575);
                                zzfrVar.zzb(i3, zzk5);
                                break;
                            } else {
                                break;
                            }
                        case 17:
                            if (!zza(obj, i)) {
                                break;
                            }
                            zzfrVar.zzb(i3, zzfd.zzo(obj, zzag2 & 1048575), zzad(i));
                            break;
                        case 18:
                            zzeh.zza(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, false);
                            break;
                        case 19:
                            zzeh.zzb(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, false);
                            break;
                        case 20:
                            zzeh.zzc(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, false);
                            break;
                        case 21:
                            zzeh.zzd(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, false);
                            break;
                        case 22:
                            zzeh.zzh(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, false);
                            break;
                        case 23:
                            zzeh.zzf(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, false);
                            break;
                        case 24:
                            zzeh.zzk(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, false);
                            break;
                        case 25:
                            zzeh.zzn(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, false);
                            break;
                        case 26:
                            zzeh.zza(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar);
                            break;
                        case 27:
                            zzeh.zza(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, zzad(i));
                            break;
                        case 28:
                            zzeh.zzb(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar);
                            break;
                        case 29:
                            zzeh.zzi(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, false);
                            break;
                        case 30:
                            zzeh.zzm(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, false);
                            break;
                        case 31:
                            zzeh.zzl(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, false);
                            break;
                        case 32:
                            zzeh.zzg(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, false);
                            break;
                        case 33:
                            zzeh.zzj(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, false);
                            break;
                        case 34:
                            zzeh.zze(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, false);
                            break;
                        case 35:
                            zzeh.zza(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, true);
                            break;
                        case 36:
                            zzeh.zzb(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, true);
                            break;
                        case 37:
                            zzeh.zzc(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, true);
                            break;
                        case 38:
                            zzeh.zzd(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, true);
                            break;
                        case 39:
                            zzeh.zzh(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, true);
                            break;
                        case 40:
                            zzeh.zzf(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, true);
                            break;
                        case 41:
                            zzeh.zzk(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, true);
                            break;
                        case 42:
                            zzeh.zzn(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, true);
                            break;
                        case 43:
                            zzeh.zzi(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, true);
                            break;
                        case 44:
                            zzeh.zzm(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, true);
                            break;
                        case 45:
                            zzeh.zzl(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, true);
                            break;
                        case 46:
                            zzeh.zzg(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, true);
                            break;
                        case 47:
                            zzeh.zzj(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, true);
                            break;
                        case 48:
                            zzeh.zze(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, true);
                            break;
                        case 49:
                            zzeh.zzb(this.zzmi[i], (List) zzfd.zzo(obj, zzag2 & 1048575), zzfrVar, zzad(i));
                            break;
                        case 50:
                            zza(zzfrVar, i3, zzfd.zzo(obj, zzag2 & 1048575), i);
                            break;
                        case 51:
                            if (zza(obj, i3, i)) {
                                zzn = zze(obj, zzag2 & 1048575);
                                zzfrVar.zza(i3, zzn);
                                break;
                            } else {
                                break;
                            }
                        case 52:
                            if (zza(obj, i3, i)) {
                                zzm = zzf(obj, zzag2 & 1048575);
                                zzfrVar.zza(i3, zzm);
                                break;
                            } else {
                                break;
                            }
                        case 53:
                            if (zza(obj, i3, i)) {
                                zzk = zzh(obj, zzag2 & 1048575);
                                zzfrVar.zzi(i3, zzk);
                                break;
                            } else {
                                break;
                            }
                        case 54:
                            if (zza(obj, i3, i)) {
                                zzk2 = zzh(obj, zzag2 & 1048575);
                                zzfrVar.zza(i3, zzk2);
                                break;
                            } else {
                                break;
                            }
                        case 55:
                            if (zza(obj, i3, i)) {
                                zzj = zzg(obj, zzag2 & 1048575);
                                zzfrVar.zzc(i3, zzj);
                                break;
                            } else {
                                break;
                            }
                        case 56:
                            if (zza(obj, i3, i)) {
                                zzk3 = zzh(obj, zzag2 & 1048575);
                                zzfrVar.zzc(i3, zzk3);
                                break;
                            } else {
                                break;
                            }
                        case 57:
                            if (zza(obj, i3, i)) {
                                zzj2 = zzg(obj, zzag2 & 1048575);
                                zzfrVar.zzf(i3, zzj2);
                                break;
                            } else {
                                break;
                            }
                        case 58:
                            if (zza(obj, i3, i)) {
                                zzl = zzi(obj, zzag2 & 1048575);
                                zzfrVar.zzb(i3, zzl);
                                break;
                            } else {
                                break;
                            }
                        case 59:
                            if (!zza(obj, i3, i)) {
                                break;
                            }
                            zza(i3, zzfd.zzo(obj, zzag2 & 1048575), zzfrVar);
                            break;
                        case 60:
                            if (!zza(obj, i3, i)) {
                                break;
                            }
                            zzfrVar.zza(i3, zzfd.zzo(obj, zzag2 & 1048575), zzad(i));
                            break;
                        case 61:
                            if (!zza(obj, i3, i)) {
                                break;
                            }
                            zzfrVar.zza(i3, (zzbb) zzfd.zzo(obj, zzag2 & 1048575));
                            break;
                        case 62:
                            if (zza(obj, i3, i)) {
                                zzj3 = zzg(obj, zzag2 & 1048575);
                                zzfrVar.zzd(i3, zzj3);
                                break;
                            } else {
                                break;
                            }
                        case 63:
                            if (zza(obj, i3, i)) {
                                zzj4 = zzg(obj, zzag2 & 1048575);
                                zzfrVar.zzn(i3, zzj4);
                                break;
                            } else {
                                break;
                            }
                        case 64:
                            if (zza(obj, i3, i)) {
                                zzj5 = zzg(obj, zzag2 & 1048575);
                                zzfrVar.zzm(i3, zzj5);
                                break;
                            } else {
                                break;
                            }
                        case 65:
                            if (zza(obj, i3, i)) {
                                zzk4 = zzh(obj, zzag2 & 1048575);
                                zzfrVar.zzj(i3, zzk4);
                                break;
                            } else {
                                break;
                            }
                        case 66:
                            if (zza(obj, i3, i)) {
                                zzj6 = zzg(obj, zzag2 & 1048575);
                                zzfrVar.zze(i3, zzj6);
                                break;
                            } else {
                                break;
                            }
                        case 67:
                            if (zza(obj, i3, i)) {
                                zzk5 = zzh(obj, zzag2 & 1048575);
                                zzfrVar.zzb(i3, zzk5);
                                break;
                            } else {
                                break;
                            }
                        case 68:
                            if (!zza(obj, i3, i)) {
                                break;
                            }
                            zzfrVar.zzb(i3, zzfd.zzo(obj, zzag2 & 1048575), zzad(i));
                            break;
                    }
                }
                while (entry != null) {
                    this.zzmy.zza(zzfrVar, entry);
                    entry = it.hasNext() ? (Map.Entry) it.next() : null;
                }
                zza(this.zzmx, obj, zzfrVar);
            }
        }
        it = null;
        entry = null;
        length = this.zzmi.length;
        while (i < length) {
        }
        while (entry != null) {
        }
        zza(this.zzmx, obj, zzfrVar);
    }

    /* JADX WARN: Code restructure failed: missing block: B:37:0x0163, code lost:
    
        if (r0 == r15) goto L74;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0165, code lost:
    
        r2 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0196, code lost:
    
        if (r0 == r15) goto L74;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x01af, code lost:
    
        if (r0 == r15) goto L74;
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:48:0x0048. Please report as an issue. */
    @Override // com.google.android.gms.internal.clearcut.zzef
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void zza(Object obj, byte[] bArr, int i, int i2, zzay zzayVar) {
        int i3;
        int i4;
        Unsafe unsafe;
        int i5;
        int zzb;
        long j;
        Object zza;
        int i6;
        zzds zzdsVar = this;
        Object obj2 = obj;
        byte[] bArr2 = bArr;
        int i7 = i2;
        zzay zzayVar2 = zzayVar;
        if (!zzdsVar.zzmq) {
            zza(obj, bArr, i, i2, 0, zzayVar);
            return;
        }
        Unsafe unsafe2 = zzmh;
        int i8 = i;
        while (i8 < i7) {
            int i9 = i8 + 1;
            byte b = bArr2[i8];
            if (b < 0) {
                i4 = zzax.zza(b, bArr2, i9, zzayVar2);
                i3 = zzayVar2.zzfd;
            } else {
                i3 = b;
                i4 = i9;
            }
            int i10 = i3 >>> 3;
            int i11 = i3 & 7;
            int zzai = zzdsVar.zzai(i10);
            if (zzai >= 0) {
                int i12 = zzdsVar.zzmi[zzai + 1];
                int i13 = (267386880 & i12) >>> 20;
                long j2 = 1048575 & i12;
                if (i13 <= 17) {
                    switch (i13) {
                        case 0:
                            if (i11 != 1) {
                                break;
                            } else {
                                zzfd.zza(obj2, j2, zzax.zze(bArr2, i4));
                                i8 = i4 + 8;
                                break;
                            }
                        case 1:
                            if (i11 != 5) {
                                break;
                            } else {
                                zzfd.zza(obj2, j2, zzax.zzf(bArr2, i4));
                                i8 = i4 + 4;
                                break;
                            }
                        case 2:
                        case 3:
                            if (i11 != 0) {
                                break;
                            } else {
                                zzb = zzax.zzb(bArr2, i4, zzayVar2);
                                j = zzayVar2.zzfe;
                                unsafe2.putLong(obj, j2, j);
                                i8 = zzb;
                                break;
                            }
                        case 4:
                        case 11:
                            if (i11 != 0) {
                                break;
                            } else {
                                i8 = zzax.zza(bArr2, i4, zzayVar2);
                                i6 = zzayVar2.zzfd;
                                unsafe2.putInt(obj2, j2, i6);
                                break;
                            }
                        case 5:
                        case 14:
                            if (i11 != 1) {
                                break;
                            } else {
                                unsafe2.putLong(obj, j2, zzax.zzd(bArr2, i4));
                                i8 = i4 + 8;
                                break;
                            }
                        case 6:
                        case 13:
                            if (i11 != 5) {
                                break;
                            } else {
                                unsafe2.putInt(obj2, j2, zzax.zzc(bArr2, i4));
                                i8 = i4 + 4;
                                break;
                            }
                        case 7:
                            if (i11 != 0) {
                                break;
                            } else {
                                i8 = zzax.zzb(bArr2, i4, zzayVar2);
                                zzfd.zza(obj2, j2, zzayVar2.zzfe != 0);
                                break;
                            }
                        case 8:
                            if (i11 != 2) {
                                break;
                            } else {
                                i8 = (536870912 & i12) == 0 ? zzax.zzc(bArr2, i4, zzayVar2) : zzax.zzd(bArr2, i4, zzayVar2);
                                zza = zzayVar2.zzff;
                                unsafe2.putObject(obj2, j2, zza);
                                break;
                            }
                        case 9:
                            if (i11 != 2) {
                                break;
                            } else {
                                i8 = zza(zzdsVar.zzad(zzai), bArr2, i4, i7, zzayVar2);
                                Object object = unsafe2.getObject(obj2, j2);
                                if (object != null) {
                                    zza = zzci.zza(object, zzayVar2.zzff);
                                    unsafe2.putObject(obj2, j2, zza);
                                    break;
                                }
                                zza = zzayVar2.zzff;
                                unsafe2.putObject(obj2, j2, zza);
                            }
                        case 10:
                            if (i11 != 2) {
                                break;
                            } else {
                                i8 = zzax.zze(bArr2, i4, zzayVar2);
                                zza = zzayVar2.zzff;
                                unsafe2.putObject(obj2, j2, zza);
                                break;
                            }
                        case 12:
                            if (i11 != 0) {
                                break;
                            } else {
                                i8 = zzax.zza(bArr2, i4, zzayVar2);
                                i6 = zzayVar2.zzfd;
                                unsafe2.putInt(obj2, j2, i6);
                                break;
                            }
                        case 15:
                            if (i11 != 0) {
                                break;
                            } else {
                                i8 = zzax.zza(bArr2, i4, zzayVar2);
                                i6 = zzbk.zzm(zzayVar2.zzfd);
                                unsafe2.putInt(obj2, j2, i6);
                                break;
                            }
                        case 16:
                            if (i11 != 0) {
                                break;
                            } else {
                                zzb = zzax.zzb(bArr2, i4, zzayVar2);
                                j = zzbk.zza(zzayVar2.zzfe);
                                unsafe2.putLong(obj, j2, j);
                                i8 = zzb;
                                break;
                            }
                    }
                } else if (i13 != 27) {
                    if (i13 <= 49) {
                        unsafe = unsafe2;
                        int i14 = i4;
                        i8 = zza(obj, bArr, i4, i2, i3, i10, i11, zzai, i12, i13, j2, zzayVar);
                    } else {
                        unsafe = unsafe2;
                        i5 = i4;
                        if (i13 != 50) {
                            i8 = zza(obj, bArr, i5, i2, i3, i10, i11, i12, i13, j2, zzai, zzayVar);
                        } else if (i11 == 2) {
                            i8 = zza(obj, bArr, i5, i2, zzai, i10, j2, zzayVar);
                        }
                    }
                    zzdsVar = this;
                    obj2 = obj;
                    bArr2 = bArr;
                    i7 = i2;
                    zzayVar2 = zzayVar;
                    unsafe2 = unsafe;
                } else if (i11 == 2) {
                    zzcn zzcnVar = (zzcn) unsafe2.getObject(obj2, j2);
                    if (!zzcnVar.zzu()) {
                        int size = zzcnVar.size();
                        zzcnVar = zzcnVar.zzi(size == 0 ? 10 : size << 1);
                        unsafe2.putObject(obj2, j2, zzcnVar);
                    }
                    i8 = zza(zzdsVar.zzad(zzai), i3, bArr, i4, i2, zzcnVar, zzayVar);
                }
                int i15 = i5;
                i8 = zza(i3, bArr, i15, i2, obj, zzayVar);
                zzdsVar = this;
                obj2 = obj;
                bArr2 = bArr;
                i7 = i2;
                zzayVar2 = zzayVar;
                unsafe2 = unsafe;
            }
            unsafe = unsafe2;
            i5 = i4;
            int i152 = i5;
            i8 = zza(i3, bArr, i152, i2, obj, zzayVar);
            zzdsVar = this;
            obj2 = obj;
            bArr2 = bArr;
            i7 = i2;
            zzayVar2 = zzayVar;
            unsafe2 = unsafe;
        }
        if (i8 != i7) {
            throw zzco.zzbo();
        }
    }

    @Override // com.google.android.gms.internal.clearcut.zzef
    public final void zzc(Object obj) {
        int[] iArr = this.zzmt;
        if (iArr != null) {
            for (int i : iArr) {
                long zzag = zzag(i) & 1048575;
                Object zzo = zzfd.zzo(obj, zzag);
                if (zzo != null) {
                    zzfd.zza(obj, zzag, this.zzmz.zzj(zzo));
                }
            }
        }
        int[] iArr2 = this.zzmu;
        if (iArr2 != null) {
            for (int i2 : iArr2) {
                this.zzmw.zza(obj, i2);
            }
        }
        this.zzmx.zzc(obj);
        if (this.zzmo) {
            this.zzmy.zzc(obj);
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:5:0x001b. Please report as an issue. */
    @Override // com.google.android.gms.internal.clearcut.zzef
    public final void zzc(Object obj, Object obj2) {
        obj2.getClass();
        for (int i = 0; i < this.zzmi.length; i += 4) {
            int zzag = zzag(i);
            long j = 1048575 & zzag;
            int i2 = this.zzmi[i];
            switch ((zzag & 267386880) >>> 20) {
                case 0:
                    if (zza(obj2, i)) {
                        zzfd.zza(obj, j, zzfd.zzn(obj2, j));
                        zzb(obj, i);
                        break;
                    } else {
                        break;
                    }
                case 1:
                    if (zza(obj2, i)) {
                        zzfd.zza(obj, j, zzfd.zzm(obj2, j));
                        zzb(obj, i);
                        break;
                    } else {
                        break;
                    }
                case 2:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzfd.zza(obj, j, zzfd.zzk(obj2, j));
                    zzb(obj, i);
                    break;
                case 3:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzfd.zza(obj, j, zzfd.zzk(obj2, j));
                    zzb(obj, i);
                    break;
                case 4:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzfd.zza(obj, j, zzfd.zzj(obj2, j));
                    zzb(obj, i);
                    break;
                case 5:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzfd.zza(obj, j, zzfd.zzk(obj2, j));
                    zzb(obj, i);
                    break;
                case 6:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzfd.zza(obj, j, zzfd.zzj(obj2, j));
                    zzb(obj, i);
                    break;
                case 7:
                    if (zza(obj2, i)) {
                        zzfd.zza(obj, j, zzfd.zzl(obj2, j));
                        zzb(obj, i);
                        break;
                    } else {
                        break;
                    }
                case 8:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzfd.zza(obj, j, zzfd.zzo(obj2, j));
                    zzb(obj, i);
                    break;
                case 9:
                case 17:
                    zza(obj, obj2, i);
                    break;
                case 10:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzfd.zza(obj, j, zzfd.zzo(obj2, j));
                    zzb(obj, i);
                    break;
                case 11:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzfd.zza(obj, j, zzfd.zzj(obj2, j));
                    zzb(obj, i);
                    break;
                case 12:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzfd.zza(obj, j, zzfd.zzj(obj2, j));
                    zzb(obj, i);
                    break;
                case 13:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzfd.zza(obj, j, zzfd.zzj(obj2, j));
                    zzb(obj, i);
                    break;
                case 14:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzfd.zza(obj, j, zzfd.zzk(obj2, j));
                    zzb(obj, i);
                    break;
                case 15:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzfd.zza(obj, j, zzfd.zzj(obj2, j));
                    zzb(obj, i);
                    break;
                case 16:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzfd.zza(obj, j, zzfd.zzk(obj2, j));
                    zzb(obj, i);
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
                    this.zzmw.zza(obj, obj2, j);
                    break;
                case 50:
                    zzeh.zza(this.zzmz, obj, obj2, j);
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
                    if (!zza(obj2, i2, i)) {
                        break;
                    }
                    zzfd.zza(obj, j, zzfd.zzo(obj2, j));
                    zzb(obj, i2, i);
                    break;
                case 60:
                case 68:
                    zzb(obj, obj2, i);
                    break;
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                    if (!zza(obj2, i2, i)) {
                        break;
                    }
                    zzfd.zza(obj, j, zzfd.zzo(obj2, j));
                    zzb(obj, i2, i);
                    break;
            }
        }
        if (this.zzmq) {
            return;
        }
        zzeh.zza(this.zzmx, obj, obj2);
        if (this.zzmo) {
            zzeh.zza(this.zzmy, obj, obj2);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x0185, code lost:
    
        if (r19.zzmr != false) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x0187, code lost:
    
        r2.putInt(r20, r14, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x018b, code lost:
    
        r3 = (com.google.android.gms.internal.clearcut.zzbn.zzr(r3) + com.google.android.gms.internal.clearcut.zzbn.zzt(r4)) + r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x01a5, code lost:
    
        if (r19.zzmr != false) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x01b6, code lost:
    
        if (r19.zzmr != false) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x01c7, code lost:
    
        if (r19.zzmr != false) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:124:0x01d8, code lost:
    
        if (r19.zzmr != false) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:129:0x01e9, code lost:
    
        if (r19.zzmr != false) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:134:0x01fa, code lost:
    
        if (r19.zzmr != false) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:139:0x020b, code lost:
    
        if (r19.zzmr != false) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:144:0x021d, code lost:
    
        if (r19.zzmr != false) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:149:0x022f, code lost:
    
        if (r19.zzmr != false) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:154:0x0241, code lost:
    
        if (r19.zzmr != false) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:159:0x0253, code lost:
    
        if (r19.zzmr != false) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:164:0x0265, code lost:
    
        if (r19.zzmr != false) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:169:0x0277, code lost:
    
        if (r19.zzmr != false) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:218:0x0365, code lost:
    
        if ((r4 instanceof com.google.android.gms.internal.clearcut.zzbb) != false) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:265:0x041f, code lost:
    
        if (zza(r20, r15, r4) != false) goto L249;
     */
    /* JADX WARN: Code restructure failed: missing block: B:266:0x0421, code lost:
    
        r6 = com.google.android.gms.internal.clearcut.zzbn.zzc(r15, (com.google.android.gms.internal.clearcut.zzdo) r2.getObject(r20, r10), zzad(r4));
     */
    /* JADX WARN: Code restructure failed: missing block: B:277:0x045a, code lost:
    
        if (zza(r20, r15, r4) != false) goto L263;
     */
    /* JADX WARN: Code restructure failed: missing block: B:278:0x045c, code lost:
    
        r6 = com.google.android.gms.internal.clearcut.zzbn.zzh(r15, 0L);
     */
    /* JADX WARN: Code restructure failed: missing block: B:280:0x0467, code lost:
    
        if (zza(r20, r15, r4) != false) goto L266;
     */
    /* JADX WARN: Code restructure failed: missing block: B:281:0x0469, code lost:
    
        r9 = com.google.android.gms.internal.clearcut.zzbn.zzk(r15, 0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x0492, code lost:
    
        if (zza(r20, r15, r4) != false) goto L278;
     */
    /* JADX WARN: Code restructure failed: missing block: B:293:0x0494, code lost:
    
        r6 = r2.getObject(r20, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:294:0x0498, code lost:
    
        r6 = com.google.android.gms.internal.clearcut.zzbn.zzc(r15, (com.google.android.gms.internal.clearcut.zzbb) r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:296:0x04a3, code lost:
    
        if (zza(r20, r15, r4) != false) goto L282;
     */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x04a5, code lost:
    
        r6 = com.google.android.gms.internal.clearcut.zzeh.zzc(r15, r2.getObject(r20, r10), zzad(r4));
     */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x04bf, code lost:
    
        if ((r6 instanceof com.google.android.gms.internal.clearcut.zzbb) != false) goto L279;
     */
    /* JADX WARN: Code restructure failed: missing block: B:302:0x04c2, code lost:
    
        r6 = com.google.android.gms.internal.clearcut.zzbn.zzb(r15, (java.lang.String) r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:304:0x04ce, code lost:
    
        if (zza(r20, r15, r4) != false) goto L291;
     */
    /* JADX WARN: Code restructure failed: missing block: B:305:0x04d0, code lost:
    
        r6 = com.google.android.gms.internal.clearcut.zzbn.zzc(r15, true);
     */
    /* JADX WARN: Code restructure failed: missing block: B:332:0x0569, code lost:
    
        if (r19.zzmr != false) goto L319;
     */
    /* JADX WARN: Code restructure failed: missing block: B:333:0x056b, code lost:
    
        r2.putInt(r20, r9, r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:334:0x056f, code lost:
    
        r9 = (com.google.android.gms.internal.clearcut.zzbn.zzr(r15) + com.google.android.gms.internal.clearcut.zzbn.zzt(r6)) + r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:338:0x0589, code lost:
    
        if (r19.zzmr != false) goto L319;
     */
    /* JADX WARN: Code restructure failed: missing block: B:342:0x059a, code lost:
    
        if (r19.zzmr != false) goto L319;
     */
    /* JADX WARN: Code restructure failed: missing block: B:346:0x05ab, code lost:
    
        if (r19.zzmr != false) goto L319;
     */
    /* JADX WARN: Code restructure failed: missing block: B:350:0x05bc, code lost:
    
        if (r19.zzmr != false) goto L319;
     */
    /* JADX WARN: Code restructure failed: missing block: B:354:0x05cd, code lost:
    
        if (r19.zzmr != false) goto L319;
     */
    /* JADX WARN: Code restructure failed: missing block: B:358:0x05de, code lost:
    
        if (r19.zzmr != false) goto L319;
     */
    /* JADX WARN: Code restructure failed: missing block: B:362:0x05ef, code lost:
    
        if (r19.zzmr != false) goto L319;
     */
    /* JADX WARN: Code restructure failed: missing block: B:366:0x0601, code lost:
    
        if (r19.zzmr != false) goto L319;
     */
    /* JADX WARN: Code restructure failed: missing block: B:370:0x0613, code lost:
    
        if (r19.zzmr != false) goto L319;
     */
    /* JADX WARN: Code restructure failed: missing block: B:374:0x0625, code lost:
    
        if (r19.zzmr != false) goto L319;
     */
    /* JADX WARN: Code restructure failed: missing block: B:378:0x0637, code lost:
    
        if (r19.zzmr != false) goto L319;
     */
    /* JADX WARN: Code restructure failed: missing block: B:382:0x0649, code lost:
    
        if (r19.zzmr != false) goto L319;
     */
    /* JADX WARN: Code restructure failed: missing block: B:386:0x065b, code lost:
    
        if (r19.zzmr != false) goto L319;
     */
    /* JADX WARN: Code restructure failed: missing block: B:401:0x070b, code lost:
    
        if ((r12 & r18) != 0) goto L249;
     */
    /* JADX WARN: Code restructure failed: missing block: B:409:0x0725, code lost:
    
        if ((r12 & r18) != 0) goto L263;
     */
    /* JADX WARN: Code restructure failed: missing block: B:411:0x072b, code lost:
    
        if ((r12 & r18) != 0) goto L266;
     */
    /* JADX WARN: Code restructure failed: missing block: B:419:0x0745, code lost:
    
        if ((r12 & r18) != 0) goto L278;
     */
    /* JADX WARN: Code restructure failed: missing block: B:421:0x074b, code lost:
    
        if ((r12 & r18) != 0) goto L282;
     */
    /* JADX WARN: Code restructure failed: missing block: B:425:0x0759, code lost:
    
        if ((r6 instanceof com.google.android.gms.internal.clearcut.zzbb) != false) goto L279;
     */
    /* JADX WARN: Code restructure failed: missing block: B:427:0x075f, code lost:
    
        if ((r12 & r18) != 0) goto L291;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00e0, code lost:
    
        if ((r4 instanceof com.google.android.gms.internal.clearcut.zzbb) != false) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00e3, code lost:
    
        r3 = com.google.android.gms.internal.clearcut.zzbn.zzb(r3, (java.lang.String) r4);
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:11:0x0042. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:263:0x0417. Please report as an issue. */
    @Override // com.google.android.gms.internal.clearcut.zzef
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final int zzm(Object obj) {
        int i;
        int i2;
        long j;
        int zzd;
        Object object;
        int i3;
        int i4;
        int i5;
        long j2;
        int zzo;
        int zzi;
        int zzr;
        long zzk;
        long zzk2;
        int zzj;
        Object zzo2;
        int zzj2;
        int zzj3;
        int zzj4;
        long zzk3;
        int zzo3;
        int zzi2;
        int i6 = 267386880;
        if (!this.zzmq) {
            Unsafe unsafe = zzmh;
            int i7 = -1;
            int i8 = 0;
            int i9 = 0;
            int i10 = 0;
            while (i8 < this.zzmi.length) {
                int zzag = zzag(i8);
                int[] iArr = this.zzmi;
                int i11 = iArr[i8];
                int i12 = (zzag & 267386880) >>> 20;
                if (i12 <= 17) {
                    int i13 = iArr[i8 + 2];
                    int i14 = i13 & 1048575;
                    i2 = 1 << (i13 >>> 20);
                    if (i14 != i7) {
                        i10 = unsafe.getInt(obj, i14);
                        i7 = i14;
                    }
                    i = i13;
                } else {
                    i = (!this.zzmr || i12 < zzcb.zzih.id() || i12 > zzcb.zziu.id()) ? 0 : this.zzmi[i8 + 2] & 1048575;
                    i2 = 0;
                }
                long j3 = zzag & 1048575;
                switch (i12) {
                    case 0:
                        j = 0;
                        if ((i10 & i2) != 0) {
                            i9 += zzbn.zzb(i11, 0.0d);
                            break;
                        }
                        break;
                    case 1:
                        j = 0;
                        if ((i10 & i2) != 0) {
                            i9 += zzbn.zzb(i11, 0.0f);
                            break;
                        }
                    case 2:
                        j = 0;
                        if ((i10 & i2) != 0) {
                            zzd = zzbn.zzd(i11, unsafe.getLong(obj, j3));
                            i9 += zzd;
                        }
                        break;
                    case 3:
                        j = 0;
                        if ((i10 & i2) != 0) {
                            zzd = zzbn.zze(i11, unsafe.getLong(obj, j3));
                            i9 += zzd;
                        }
                        break;
                    case 4:
                        j = 0;
                        if ((i10 & i2) != 0) {
                            zzd = zzbn.zzg(i11, unsafe.getInt(obj, j3));
                            i9 += zzd;
                        }
                        break;
                    case 5:
                        j = 0;
                        if ((i10 & i2) != 0) {
                            zzd = zzbn.zzg(i11, 0L);
                            i9 += zzd;
                        }
                        break;
                    case 6:
                        if ((i10 & i2) != 0) {
                            i9 += zzbn.zzj(i11, 0);
                            j = 0;
                            break;
                        }
                        j = 0;
                    case 7:
                        break;
                    case 8:
                        if ((i10 & i2) != 0) {
                            object = unsafe.getObject(obj, j3);
                            break;
                        }
                        j = 0;
                        break;
                    case 9:
                        break;
                    case 10:
                        break;
                    case 11:
                        if ((i10 & i2) != 0) {
                            i3 = unsafe.getInt(obj, j3);
                            zzo = zzbn.zzh(i11, i3);
                            i9 += zzo;
                        }
                        j = 0;
                        break;
                    case 12:
                        if ((i10 & i2) != 0) {
                            i4 = unsafe.getInt(obj, j3);
                            zzo = zzbn.zzl(i11, i4);
                            i9 += zzo;
                        }
                        j = 0;
                        break;
                    case 13:
                        break;
                    case 14:
                        break;
                    case 15:
                        if ((i10 & i2) != 0) {
                            i5 = unsafe.getInt(obj, j3);
                            zzo = zzbn.zzi(i11, i5);
                            i9 += zzo;
                        }
                        j = 0;
                        break;
                    case 16:
                        if ((i10 & i2) != 0) {
                            j2 = unsafe.getLong(obj, j3);
                            zzo = zzbn.zzf(i11, j2);
                            i9 += zzo;
                        }
                        j = 0;
                        break;
                    case 17:
                        break;
                    case 18:
                    case 23:
                    case 32:
                        zzo = zzeh.zzw(i11, (List) unsafe.getObject(obj, j3), false);
                        i9 += zzo;
                        j = 0;
                        break;
                    case 19:
                    case 24:
                    case 31:
                        zzo = zzeh.zzv(i11, (List) unsafe.getObject(obj, j3), false);
                        i9 += zzo;
                        j = 0;
                        break;
                    case 20:
                        zzo = zzeh.zzo(i11, (List) unsafe.getObject(obj, j3), false);
                        i9 += zzo;
                        j = 0;
                        break;
                    case 21:
                        zzo = zzeh.zzp(i11, (List) unsafe.getObject(obj, j3), false);
                        i9 += zzo;
                        j = 0;
                        break;
                    case 22:
                        zzo = zzeh.zzs(i11, (List) unsafe.getObject(obj, j3), false);
                        i9 += zzo;
                        j = 0;
                        break;
                    case 25:
                        zzo = zzeh.zzx(i11, (List) unsafe.getObject(obj, j3), false);
                        i9 += zzo;
                        j = 0;
                        break;
                    case 26:
                        zzo = zzeh.zzc(i11, (List) unsafe.getObject(obj, j3));
                        i9 += zzo;
                        j = 0;
                        break;
                    case 27:
                        zzo = zzeh.zzc(i11, (List) unsafe.getObject(obj, j3), zzad(i8));
                        i9 += zzo;
                        j = 0;
                        break;
                    case 28:
                        zzo = zzeh.zzd(i11, (List) unsafe.getObject(obj, j3));
                        i9 += zzo;
                        j = 0;
                        break;
                    case 29:
                        zzo = zzeh.zzt(i11, (List) unsafe.getObject(obj, j3), false);
                        i9 += zzo;
                        j = 0;
                        break;
                    case 30:
                        zzo = zzeh.zzr(i11, (List) unsafe.getObject(obj, j3), false);
                        i9 += zzo;
                        j = 0;
                        break;
                    case 33:
                        zzo = zzeh.zzu(i11, (List) unsafe.getObject(obj, j3), false);
                        i9 += zzo;
                        j = 0;
                        break;
                    case 34:
                        zzo = zzeh.zzq(i11, (List) unsafe.getObject(obj, j3), false);
                        i9 += zzo;
                        j = 0;
                        break;
                    case 35:
                        zzi = zzeh.zzi((List) unsafe.getObject(obj, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 36:
                        zzi = zzeh.zzh((List) unsafe.getObject(obj, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 37:
                        zzi = zzeh.zza((List) unsafe.getObject(obj, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 38:
                        zzi = zzeh.zzb((List) unsafe.getObject(obj, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 39:
                        zzi = zzeh.zze((List) unsafe.getObject(obj, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 40:
                        zzi = zzeh.zzi((List) unsafe.getObject(obj, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 41:
                        zzi = zzeh.zzh((List) unsafe.getObject(obj, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 42:
                        zzi = zzeh.zzj((List) unsafe.getObject(obj, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 43:
                        zzi = zzeh.zzf((List) unsafe.getObject(obj, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 44:
                        zzi = zzeh.zzd((List) unsafe.getObject(obj, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 45:
                        zzi = zzeh.zzh((List) unsafe.getObject(obj, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 46:
                        zzi = zzeh.zzi((List) unsafe.getObject(obj, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 47:
                        zzi = zzeh.zzg((List) unsafe.getObject(obj, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 48:
                        zzi = zzeh.zzc((List) unsafe.getObject(obj, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 49:
                        zzo = zzeh.zzd(i11, (List) unsafe.getObject(obj, j3), zzad(i8));
                        i9 += zzo;
                        j = 0;
                        break;
                    case 50:
                        zzo = this.zzmz.zzb(i11, unsafe.getObject(obj, j3), zzae(i8));
                        i9 += zzo;
                        j = 0;
                        break;
                    case 51:
                        if (zza(obj, i11, i8)) {
                            zzo = zzbn.zzb(i11, 0.0d);
                            i9 += zzo;
                        }
                        j = 0;
                        break;
                    case 52:
                        if (zza(obj, i11, i8)) {
                            zzr = zzbn.zzb(i11, 0.0f);
                            i9 += zzr;
                        }
                        j = 0;
                        break;
                    case 53:
                        if (zza(obj, i11, i8)) {
                            zzo = zzbn.zzd(i11, zzh(obj, j3));
                            i9 += zzo;
                        }
                        j = 0;
                        break;
                    case 54:
                        if (zza(obj, i11, i8)) {
                            zzo = zzbn.zze(i11, zzh(obj, j3));
                            i9 += zzo;
                        }
                        j = 0;
                        break;
                    case 55:
                        if (zza(obj, i11, i8)) {
                            zzo = zzbn.zzg(i11, zzg(obj, j3));
                            i9 += zzo;
                        }
                        j = 0;
                        break;
                    case 56:
                        if (zza(obj, i11, i8)) {
                            zzo = zzbn.zzg(i11, 0L);
                            i9 += zzo;
                        }
                        j = 0;
                        break;
                    case 57:
                        if (zza(obj, i11, i8)) {
                            zzr = zzbn.zzj(i11, 0);
                            i9 += zzr;
                        }
                        j = 0;
                        break;
                    case 58:
                        break;
                    case 59:
                        if (zza(obj, i11, i8)) {
                            object = unsafe.getObject(obj, j3);
                            break;
                        }
                        j = 0;
                        break;
                    case 60:
                        break;
                    case 61:
                        break;
                    case 62:
                        if (zza(obj, i11, i8)) {
                            i3 = zzg(obj, j3);
                            zzo = zzbn.zzh(i11, i3);
                            i9 += zzo;
                        }
                        j = 0;
                        break;
                    case 63:
                        if (zza(obj, i11, i8)) {
                            i4 = zzg(obj, j3);
                            zzo = zzbn.zzl(i11, i4);
                            i9 += zzo;
                        }
                        j = 0;
                        break;
                    case 64:
                        break;
                    case 65:
                        break;
                    case 66:
                        if (zza(obj, i11, i8)) {
                            i5 = zzg(obj, j3);
                            zzo = zzbn.zzi(i11, i5);
                            i9 += zzo;
                        }
                        j = 0;
                        break;
                    case 67:
                        if (zza(obj, i11, i8)) {
                            j2 = zzh(obj, j3);
                            zzo = zzbn.zzf(i11, j2);
                            i9 += zzo;
                        }
                        j = 0;
                        break;
                    case 68:
                        break;
                    default:
                        j = 0;
                        break;
                }
                i8 += 4;
            }
            int zza = i9 + zza(this.zzmx, obj);
            return this.zzmo ? zza + this.zzmy.zza(obj).zzas() : zza;
        }
        Unsafe unsafe2 = zzmh;
        int i15 = 0;
        int i16 = 0;
        while (i15 < this.zzmi.length) {
            int zzag2 = zzag(i15);
            int i17 = (zzag2 & i6) >>> 20;
            int i18 = this.zzmi[i15];
            long j4 = zzag2 & 1048575;
            int i19 = (i17 < zzcb.zzih.id() || i17 > zzcb.zziu.id()) ? 0 : this.zzmi[i15 + 2] & 1048575;
            switch (i17) {
                case 0:
                    if (!zza(obj, i15)) {
                        break;
                    }
                    zzo3 = zzbn.zzb(i18, 0.0d);
                    i16 += zzo3;
                    break;
                case 1:
                    if (!zza(obj, i15)) {
                        break;
                    }
                    zzo3 = zzbn.zzb(i18, 0.0f);
                    i16 += zzo3;
                    break;
                case 2:
                    if (!zza(obj, i15)) {
                        break;
                    } else {
                        zzk = zzfd.zzk(obj, j4);
                        zzo3 = zzbn.zzd(i18, zzk);
                        i16 += zzo3;
                        break;
                    }
                case 3:
                    if (!zza(obj, i15)) {
                        break;
                    } else {
                        zzk2 = zzfd.zzk(obj, j4);
                        zzo3 = zzbn.zze(i18, zzk2);
                        i16 += zzo3;
                        break;
                    }
                case 4:
                    if (!zza(obj, i15)) {
                        break;
                    } else {
                        zzj = zzfd.zzj(obj, j4);
                        zzo3 = zzbn.zzg(i18, zzj);
                        i16 += zzo3;
                        break;
                    }
                case 5:
                    if (!zza(obj, i15)) {
                        break;
                    }
                    zzo3 = zzbn.zzg(i18, 0L);
                    i16 += zzo3;
                    break;
                case 6:
                    if (!zza(obj, i15)) {
                        break;
                    }
                    zzo3 = zzbn.zzj(i18, 0);
                    i16 += zzo3;
                    break;
                case 7:
                    if (!zza(obj, i15)) {
                        break;
                    }
                    zzo3 = zzbn.zzc(i18, true);
                    i16 += zzo3;
                    break;
                case 8:
                    if (!zza(obj, i15)) {
                        break;
                    } else {
                        zzo2 = zzfd.zzo(obj, j4);
                        break;
                    }
                case 9:
                    if (!zza(obj, i15)) {
                        break;
                    }
                    zzo3 = zzeh.zzc(i18, zzfd.zzo(obj, j4), zzad(i15));
                    i16 += zzo3;
                    break;
                case 10:
                    if (!zza(obj, i15)) {
                        break;
                    }
                    zzo2 = zzfd.zzo(obj, j4);
                    zzo3 = zzbn.zzc(i18, (zzbb) zzo2);
                    i16 += zzo3;
                    break;
                case 11:
                    if (!zza(obj, i15)) {
                        break;
                    } else {
                        zzj2 = zzfd.zzj(obj, j4);
                        zzo3 = zzbn.zzh(i18, zzj2);
                        i16 += zzo3;
                        break;
                    }
                case 12:
                    if (!zza(obj, i15)) {
                        break;
                    } else {
                        zzj3 = zzfd.zzj(obj, j4);
                        zzo3 = zzbn.zzl(i18, zzj3);
                        i16 += zzo3;
                        break;
                    }
                case 13:
                    if (!zza(obj, i15)) {
                        break;
                    }
                    zzo3 = zzbn.zzk(i18, 0);
                    i16 += zzo3;
                    break;
                case 14:
                    if (!zza(obj, i15)) {
                        break;
                    }
                    zzo3 = zzbn.zzh(i18, 0L);
                    i16 += zzo3;
                    break;
                case 15:
                    if (!zza(obj, i15)) {
                        break;
                    } else {
                        zzj4 = zzfd.zzj(obj, j4);
                        zzo3 = zzbn.zzi(i18, zzj4);
                        i16 += zzo3;
                        break;
                    }
                case 16:
                    if (!zza(obj, i15)) {
                        break;
                    } else {
                        zzk3 = zzfd.zzk(obj, j4);
                        zzo3 = zzbn.zzf(i18, zzk3);
                        i16 += zzo3;
                        break;
                    }
                case 17:
                    if (!zza(obj, i15)) {
                        break;
                    }
                    zzo3 = zzbn.zzc(i18, (zzdo) zzfd.zzo(obj, j4), zzad(i15));
                    i16 += zzo3;
                    break;
                case 18:
                case 23:
                case 32:
                    zzo3 = zzeh.zzw(i18, zzd(obj, j4), false);
                    i16 += zzo3;
                    break;
                case 19:
                case 24:
                case 31:
                    zzo3 = zzeh.zzv(i18, zzd(obj, j4), false);
                    i16 += zzo3;
                    break;
                case 20:
                    zzo3 = zzeh.zzo(i18, zzd(obj, j4), false);
                    i16 += zzo3;
                    break;
                case 21:
                    zzo3 = zzeh.zzp(i18, zzd(obj, j4), false);
                    i16 += zzo3;
                    break;
                case 22:
                    zzo3 = zzeh.zzs(i18, zzd(obj, j4), false);
                    i16 += zzo3;
                    break;
                case 25:
                    zzo3 = zzeh.zzx(i18, zzd(obj, j4), false);
                    i16 += zzo3;
                    break;
                case 26:
                    zzo3 = zzeh.zzc(i18, zzd(obj, j4));
                    i16 += zzo3;
                    break;
                case 27:
                    zzo3 = zzeh.zzc(i18, zzd(obj, j4), zzad(i15));
                    i16 += zzo3;
                    break;
                case 28:
                    zzo3 = zzeh.zzd(i18, zzd(obj, j4));
                    i16 += zzo3;
                    break;
                case 29:
                    zzo3 = zzeh.zzt(i18, zzd(obj, j4), false);
                    i16 += zzo3;
                    break;
                case 30:
                    zzo3 = zzeh.zzr(i18, zzd(obj, j4), false);
                    i16 += zzo3;
                    break;
                case 33:
                    zzo3 = zzeh.zzu(i18, zzd(obj, j4), false);
                    i16 += zzo3;
                    break;
                case 34:
                    zzo3 = zzeh.zzq(i18, zzd(obj, j4), false);
                    i16 += zzo3;
                    break;
                case 35:
                    zzi2 = zzeh.zzi((List) unsafe2.getObject(obj, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        break;
                    }
                case 36:
                    zzi2 = zzeh.zzh((List) unsafe2.getObject(obj, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        break;
                    }
                case 37:
                    zzi2 = zzeh.zza((List) unsafe2.getObject(obj, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        break;
                    }
                case 38:
                    zzi2 = zzeh.zzb((List) unsafe2.getObject(obj, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        break;
                    }
                case 39:
                    zzi2 = zzeh.zze((List) unsafe2.getObject(obj, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        break;
                    }
                case 40:
                    zzi2 = zzeh.zzi((List) unsafe2.getObject(obj, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        break;
                    }
                case 41:
                    zzi2 = zzeh.zzh((List) unsafe2.getObject(obj, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        break;
                    }
                case 42:
                    zzi2 = zzeh.zzj((List) unsafe2.getObject(obj, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        break;
                    }
                case 43:
                    zzi2 = zzeh.zzf((List) unsafe2.getObject(obj, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        break;
                    }
                case 44:
                    zzi2 = zzeh.zzd((List) unsafe2.getObject(obj, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        break;
                    }
                case 45:
                    zzi2 = zzeh.zzh((List) unsafe2.getObject(obj, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        break;
                    }
                case 46:
                    zzi2 = zzeh.zzi((List) unsafe2.getObject(obj, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        break;
                    }
                case 47:
                    zzi2 = zzeh.zzg((List) unsafe2.getObject(obj, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        break;
                    }
                case 48:
                    zzi2 = zzeh.zzc((List) unsafe2.getObject(obj, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        break;
                    }
                case 49:
                    zzo3 = zzeh.zzd(i18, zzd(obj, j4), zzad(i15));
                    i16 += zzo3;
                    break;
                case 50:
                    zzo3 = this.zzmz.zzb(i18, zzfd.zzo(obj, j4), zzae(i15));
                    i16 += zzo3;
                    break;
                case 51:
                    if (!zza(obj, i18, i15)) {
                        break;
                    }
                    zzo3 = zzbn.zzb(i18, 0.0d);
                    i16 += zzo3;
                    break;
                case 52:
                    if (!zza(obj, i18, i15)) {
                        break;
                    }
                    zzo3 = zzbn.zzb(i18, 0.0f);
                    i16 += zzo3;
                    break;
                case 53:
                    if (!zza(obj, i18, i15)) {
                        break;
                    } else {
                        zzk = zzh(obj, j4);
                        zzo3 = zzbn.zzd(i18, zzk);
                        i16 += zzo3;
                        break;
                    }
                case 54:
                    if (!zza(obj, i18, i15)) {
                        break;
                    } else {
                        zzk2 = zzh(obj, j4);
                        zzo3 = zzbn.zze(i18, zzk2);
                        i16 += zzo3;
                        break;
                    }
                case 55:
                    if (!zza(obj, i18, i15)) {
                        break;
                    } else {
                        zzj = zzg(obj, j4);
                        zzo3 = zzbn.zzg(i18, zzj);
                        i16 += zzo3;
                        break;
                    }
                case 56:
                    if (!zza(obj, i18, i15)) {
                        break;
                    }
                    zzo3 = zzbn.zzg(i18, 0L);
                    i16 += zzo3;
                    break;
                case 57:
                    if (!zza(obj, i18, i15)) {
                        break;
                    }
                    zzo3 = zzbn.zzj(i18, 0);
                    i16 += zzo3;
                    break;
                case 58:
                    if (!zza(obj, i18, i15)) {
                        break;
                    }
                    zzo3 = zzbn.zzc(i18, true);
                    i16 += zzo3;
                    break;
                case 59:
                    if (!zza(obj, i18, i15)) {
                        break;
                    } else {
                        zzo2 = zzfd.zzo(obj, j4);
                        break;
                    }
                case 60:
                    if (!zza(obj, i18, i15)) {
                        break;
                    }
                    zzo3 = zzeh.zzc(i18, zzfd.zzo(obj, j4), zzad(i15));
                    i16 += zzo3;
                    break;
                case 61:
                    if (!zza(obj, i18, i15)) {
                        break;
                    }
                    zzo2 = zzfd.zzo(obj, j4);
                    zzo3 = zzbn.zzc(i18, (zzbb) zzo2);
                    i16 += zzo3;
                    break;
                case 62:
                    if (!zza(obj, i18, i15)) {
                        break;
                    } else {
                        zzj2 = zzg(obj, j4);
                        zzo3 = zzbn.zzh(i18, zzj2);
                        i16 += zzo3;
                        break;
                    }
                case 63:
                    if (!zza(obj, i18, i15)) {
                        break;
                    } else {
                        zzj3 = zzg(obj, j4);
                        zzo3 = zzbn.zzl(i18, zzj3);
                        i16 += zzo3;
                        break;
                    }
                case 64:
                    if (!zza(obj, i18, i15)) {
                        break;
                    }
                    zzo3 = zzbn.zzk(i18, 0);
                    i16 += zzo3;
                    break;
                case 65:
                    if (!zza(obj, i18, i15)) {
                        break;
                    }
                    zzo3 = zzbn.zzh(i18, 0L);
                    i16 += zzo3;
                    break;
                case 66:
                    if (!zza(obj, i18, i15)) {
                        break;
                    } else {
                        zzj4 = zzg(obj, j4);
                        zzo3 = zzbn.zzi(i18, zzj4);
                        i16 += zzo3;
                        break;
                    }
                case 67:
                    if (!zza(obj, i18, i15)) {
                        break;
                    } else {
                        zzk3 = zzh(obj, j4);
                        zzo3 = zzbn.zzf(i18, zzk3);
                        i16 += zzo3;
                        break;
                    }
                case 68:
                    if (!zza(obj, i18, i15)) {
                        break;
                    }
                    zzo3 = zzbn.zzc(i18, (zzdo) zzfd.zzo(obj, j4), zzad(i15));
                    i16 += zzo3;
                    break;
            }
            i15 += 4;
            i6 = 267386880;
        }
        return i16 + zza(this.zzmx, obj);
    }

    @Override // com.google.android.gms.internal.clearcut.zzef
    public final boolean zzo(Object obj) {
        int i;
        int i2;
        int[] iArr = this.zzms;
        int i3 = 1;
        if (iArr == null || iArr.length == 0) {
            return true;
        }
        int length = iArr.length;
        int i4 = -1;
        int i5 = 0;
        int i6 = 0;
        while (i5 < length) {
            int i7 = iArr[i5];
            int zzai = zzai(i7);
            int zzag = zzag(zzai);
            if (this.zzmq) {
                i = length;
                i2 = 0;
            } else {
                int i8 = this.zzmi[zzai + 2];
                int i9 = i8 & 1048575;
                i2 = i3 << (i8 >>> 20);
                if (i9 != i4) {
                    i = length;
                    i6 = zzmh.getInt(obj, i9);
                    i4 = i9;
                } else {
                    i = length;
                }
            }
            if ((268435456 & zzag) != 0 && !zza(obj, zzai, i6, i2)) {
                return false;
            }
            int i10 = (267386880 & zzag) >>> 20;
            if (i10 != 9 && i10 != 17) {
                if (i10 != 27) {
                    if (i10 == 60 || i10 == 68) {
                        if (zza(obj, i7, zzai) && !zza(obj, zzag, zzad(zzai))) {
                            return false;
                        }
                    } else if (i10 != 49) {
                        if (i10 == 50 && !this.zzmz.zzh(zzfd.zzo(obj, zzag & 1048575)).isEmpty()) {
                            this.zzmz.zzl(zzae(zzai));
                            throw null;
                        }
                    }
                }
                List list = (List) zzfd.zzo(obj, zzag & 1048575);
                if (list.isEmpty()) {
                    continue;
                } else {
                    zzef zzad = zzad(zzai);
                    for (int i11 = 0; i11 < list.size(); i11++) {
                        if (!zzad.zzo(list.get(i11))) {
                            return false;
                        }
                    }
                }
            } else if (zza(obj, zzai, i6, i2) && !zza(obj, zzag, zzad(zzai))) {
                return false;
            }
            i5++;
            length = i;
            i3 = 1;
        }
        return !this.zzmo || this.zzmy.zza(obj).isInitialized();
    }
}
