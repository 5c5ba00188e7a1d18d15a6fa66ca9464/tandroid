package com.google.android.gms.internal.clearcut;

import com.google.android.gms.internal.clearcut.zzcg;
import com.huawei.hms.adapter.internal.AvailableCode;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.telegram.messenger.R;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.ConnectionsManager;
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
                    unsafe.putObject(t, j, valueOf);
                    zzb = i + 8;
                    unsafe.putInt(t, j3, i4);
                    return zzb;
                }
                return i;
            case 52:
                if (i5 == 5) {
                    valueOf2 = Float.valueOf(zzax.zzf(bArr, i));
                    unsafe.putObject(t, j, valueOf2);
                    zzb = i + 4;
                    unsafe.putInt(t, j3, i4);
                    return zzb;
                }
                return i;
            case 53:
            case 54:
                if (i5 == 0) {
                    zzb = zzax.zzb(bArr, i, zzayVar);
                    j2 = zzayVar.zzfe;
                    valueOf3 = Long.valueOf(j2);
                    unsafe.putObject(t, j, valueOf3);
                    unsafe.putInt(t, j3, i4);
                    return zzb;
                }
                return i;
            case 55:
            case 62:
                if (i5 == 0) {
                    zzb = zzax.zza(bArr, i, zzayVar);
                    i9 = zzayVar.zzfd;
                    valueOf3 = Integer.valueOf(i9);
                    unsafe.putObject(t, j, valueOf3);
                    unsafe.putInt(t, j3, i4);
                    return zzb;
                }
                return i;
            case 56:
            case VoIPService.CALL_MIN_LAYER /* 65 */:
                if (i5 == 1) {
                    valueOf = Long.valueOf(zzax.zzd(bArr, i));
                    unsafe.putObject(t, j, valueOf);
                    zzb = i + 8;
                    unsafe.putInt(t, j3, i4);
                    return zzb;
                }
                return i;
            case 57:
            case 64:
                if (i5 == 5) {
                    valueOf2 = Integer.valueOf(zzax.zzc(bArr, i));
                    unsafe.putObject(t, j, valueOf2);
                    zzb = i + 4;
                    unsafe.putInt(t, j3, i4);
                    return zzb;
                }
                return i;
            case 58:
                if (i5 == 0) {
                    zzb = zzax.zzb(bArr, i, zzayVar);
                    valueOf3 = Boolean.valueOf(zzayVar.zzfe != 0);
                    unsafe.putObject(t, j, valueOf3);
                    unsafe.putInt(t, j3, i4);
                    return zzb;
                }
                return i;
            case 59:
                if (i5 == 2) {
                    zzb = zzax.zza(bArr, i, zzayVar);
                    i10 = zzayVar.zzfd;
                    if (i10 == 0) {
                        valueOf3 = "";
                        unsafe.putObject(t, j, valueOf3);
                        unsafe.putInt(t, j3, i4);
                        return zzb;
                    } else if ((i6 & 536870912) != 0 && !zzff.zze(bArr, zzb, zzb + i10)) {
                        throw zzco.zzbp();
                    } else {
                        unsafe.putObject(t, j, new String(bArr, zzb, i10, zzci.UTF_8));
                        zzb += i10;
                        unsafe.putInt(t, j3, i4);
                        return zzb;
                    }
                }
                return i;
            case 60:
                if (i5 == 2) {
                    zzb = zza(zzad(i8), bArr, i, i2, zzayVar);
                    Object object = unsafe.getInt(t, j3) == i4 ? unsafe.getObject(t, j) : null;
                    valueOf3 = zzayVar.zzff;
                    if (object != null) {
                        valueOf3 = zzci.zza(object, valueOf3);
                    }
                    unsafe.putObject(t, j, valueOf3);
                    unsafe.putInt(t, j3, i4);
                    return zzb;
                }
                return i;
            case 61:
                if (i5 == 2) {
                    zzb = zzax.zza(bArr, i, zzayVar);
                    i10 = zzayVar.zzfd;
                    if (i10 == 0) {
                        valueOf3 = zzbb.zzfi;
                        unsafe.putObject(t, j, valueOf3);
                        unsafe.putInt(t, j3, i4);
                        return zzb;
                    }
                    unsafe.putObject(t, j, zzbb.zzb(bArr, zzb, i10));
                    zzb += i10;
                    unsafe.putInt(t, j3, i4);
                    return zzb;
                }
                return i;
            case 63:
                if (i5 == 0) {
                    int zza = zzax.zza(bArr, i, zzayVar);
                    int i11 = zzayVar.zzfd;
                    zzck<?> zzaf = zzaf(i8);
                    if (zzaf != null && zzaf.zzb(i11) == null) {
                        zzn(t).zzb(i3, Long.valueOf(i11));
                        return zza;
                    }
                    unsafe.putObject(t, j, Integer.valueOf(i11));
                    zzb = zza;
                    unsafe.putInt(t, j3, i4);
                    return zzb;
                }
                return i;
            case 66:
                if (i5 == 0) {
                    zzb = zzax.zza(bArr, i, zzayVar);
                    i9 = zzbk.zzm(zzayVar.zzfd);
                    valueOf3 = Integer.valueOf(i9);
                    unsafe.putObject(t, j, valueOf3);
                    unsafe.putInt(t, j3, i4);
                    return zzb;
                }
                return i;
            case 67:
                if (i5 == 0) {
                    zzb = zzax.zzb(bArr, i, zzayVar);
                    j2 = zzbk.zza(zzayVar.zzfe);
                    valueOf3 = Long.valueOf(j2);
                    unsafe.putObject(t, j, valueOf3);
                    unsafe.putInt(t, j3, i4);
                    return zzb;
                }
                return i;
            case 68:
                if (i5 == 3) {
                    zzb = zza(zzad(i8), bArr, i, i2, (i3 & (-8)) | 4, zzayVar);
                    Object object2 = unsafe.getInt(t, j3) == i4 ? unsafe.getObject(t, j) : null;
                    valueOf3 = zzayVar.zzff;
                    if (object2 != null) {
                        valueOf3 = zzci.zza(object2, valueOf3);
                    }
                    unsafe.putObject(t, j, valueOf3);
                    unsafe.putInt(t, j3, i4);
                    return zzb;
                }
                return i;
            default:
                return i;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:136:0x022f, code lost:
        if (r29.zzfe != 0) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x0231, code lost:
        r6 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:138:0x0234, code lost:
        r12.addBoolean(r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:139:0x0237, code lost:
        if (r4 >= r19) goto L250;
     */
    /* JADX WARN: Code restructure failed: missing block: B:140:0x0239, code lost:
        r6 = com.google.android.gms.internal.clearcut.zzax.zza(r17, r4, r29);
     */
    /* JADX WARN: Code restructure failed: missing block: B:141:0x023f, code lost:
        if (r20 != r29.zzfd) goto L250;
     */
    /* JADX WARN: Code restructure failed: missing block: B:142:0x0241, code lost:
        r4 = com.google.android.gms.internal.clearcut.zzax.zzb(r17, r6, r29);
     */
    /* JADX WARN: Code restructure failed: missing block: B:143:0x0249, code lost:
        if (r29.zzfe == 0) goto L144;
     */
    /* JADX WARN: Code restructure failed: missing block: B:144:0x0233, code lost:
        r6 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0137, code lost:
        if (r4 == 0) goto L74;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0139, code lost:
        r12.add(com.google.android.gms.internal.clearcut.zzbb.zzfi);
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0147, code lost:
        if (r1 >= r19) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0149, code lost:
        r4 = com.google.android.gms.internal.clearcut.zzax.zza(r17, r1, r29);
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x014f, code lost:
        if (r20 != r29.zzfd) goto L80;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x0151, code lost:
        r1 = com.google.android.gms.internal.clearcut.zzax.zza(r17, r4, r29);
        r4 = r29.zzfd;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0157, code lost:
        if (r4 != 0) goto L82;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:?, code lost:
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:?, code lost:
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x013f, code lost:
        r12.add(com.google.android.gms.internal.clearcut.zzbb.zzb(r17, r1, r4));
        r1 = r1 + r4;
     */
    /* JADX WARN: Removed duplicated region for block: B:104:0x01d0  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x019a  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:123:0x0249 -> B:117:0x0231). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:124:0x0233 -> B:118:0x0234). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:65:0x0157 -> B:60:0x0139). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:66:0x013f -> B:61:0x0147). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:79:0x01a8 -> B:74:0x0189). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:81:0x0194 -> B:75:0x0198). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:90:0x01de -> B:85:0x01b7). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:94:0x01ca -> B:86:0x01ce). Please submit an issue!!! */
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
            case R.styleable.MapAttrs_uiZoomControls /* 20 */:
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
            case R.styleable.MapAttrs_useViewLifecycle /* 22 */:
            case AvailableCode.HMS_IS_SPOOF /* 29 */:
            case 39:
            case 43:
                if (i5 == 2) {
                    return zzax.zza(bArr, i8, zzcnVar, zzayVar);
                }
                if (i5 == 0) {
                    return zzax.zza(i3, bArr, i, i2, zzcnVar, zzayVar);
                }
                return i8;
            case R.styleable.MapAttrs_zOrderOnTop /* 23 */:
            case ConnectionsManager.RequestFlagForceDownload /* 32 */:
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
            case AvailableCode.CURRENT_SHOWING_SERVICE_UNAVAILABLE /* 31 */:
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
            case AvailableCode.ERROR_ON_ACTIVITY_RESULT /* 25 */:
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
            case AvailableCode.ERROR_NO_ACTIVITY /* 26 */:
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
            case AvailableCode.USER_IGNORE_PREVIOUS_POPUP /* 27 */:
                if (i5 == 2) {
                    return zza(zzad(i6), i3, bArr, i, i2, zzcnVar, zzayVar);
                }
                return i8;
            case AvailableCode.APP_IS_BACKGROUND_OR_LOCKED /* 28 */:
                if (i5 == 2) {
                    int zza10 = zzax.zza(bArr, i8, zzayVar);
                    int i24 = zzayVar.zzfd;
                    break;
                }
                return i8;
            case AvailableCode.USER_ALREADY_KNOWS_SERVICE_UNAVAILABLE /* 30 */:
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

    /* JADX WARN: Removed duplicated region for block: B:26:0x0370 A[ADDED_TO_REGION] */
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
        zzay zzayVar2;
        int i15;
        int i16;
        int i17;
        long j;
        Object obj;
        zzay zzayVar3;
        int zze;
        zzds<T> zzdsVar = this;
        T t3 = t;
        byte[] bArr2 = bArr;
        int i18 = i2;
        int i19 = i3;
        zzay zzayVar4 = zzayVar;
        Unsafe unsafe2 = zzmh;
        int i20 = -1;
        int i21 = i;
        int i22 = 0;
        int i23 = 0;
        int i24 = -1;
        while (true) {
            if (i21 < i18) {
                int i25 = i21 + 1;
                byte b2 = bArr2[i21];
                if (b2 < 0) {
                    i9 = zzax.zza(b2, bArr2, i25, zzayVar4);
                    b = zzayVar4.zzfd;
                } else {
                    b = b2;
                    i9 = i25;
                }
                int i26 = b >>> 3;
                int i27 = b & 7;
                int zzai = zzdsVar.zzai(i26);
                if (zzai != i20) {
                    int[] iArr = zzdsVar.zzmi;
                    int i28 = iArr[zzai + 1];
                    int i29 = (i28 & 267386880) >>> 20;
                    int i30 = b;
                    long j2 = i28 & 1048575;
                    if (i29 <= 17) {
                        int i31 = iArr[zzai + 2];
                        int i32 = 1 << (i31 >>> 20);
                        int i33 = i31 & 1048575;
                        if (i33 != i24) {
                            if (i24 != -1) {
                                unsafe2.putInt(t3, i24, i23);
                            }
                            i23 = unsafe2.getInt(t3, i33);
                            i24 = i33;
                        }
                        switch (i29) {
                            case 0:
                                i6 = i30;
                                zzayVar2 = zzayVar;
                                i15 = i9;
                                i16 = i24;
                                bArr2 = bArr;
                                if (i27 == 1) {
                                    zzfd.zza(t3, j2, zzax.zze(bArr2, i15));
                                    i21 = i15 + 8;
                                    i23 |= i32;
                                    i24 = i16;
                                    i18 = i2;
                                    i22 = i6;
                                    zzayVar4 = zzayVar2;
                                    i20 = -1;
                                    i19 = i3;
                                    break;
                                } else {
                                    i24 = i16;
                                    i4 = i3;
                                    i14 = i15;
                                    unsafe = unsafe2;
                                    if (i6 != i4 && i4 != 0) {
                                        i7 = i24;
                                        i8 = -1;
                                        i5 = i14;
                                        break;
                                    } else {
                                        i21 = zza(i6, bArr, i14, i2, t, zzayVar);
                                        zzdsVar = this;
                                        t3 = t;
                                        bArr2 = bArr;
                                        i18 = i2;
                                        i19 = i4;
                                        i22 = i6;
                                        unsafe2 = unsafe;
                                        i20 = -1;
                                        zzayVar4 = zzayVar;
                                        break;
                                    }
                                }
                            case 1:
                                i6 = i30;
                                zzayVar2 = zzayVar;
                                i15 = i9;
                                i16 = i24;
                                bArr2 = bArr;
                                if (i27 == 5) {
                                    zzfd.zza((Object) t3, j2, zzax.zzf(bArr2, i15));
                                    i21 = i15 + 4;
                                    i23 |= i32;
                                    i24 = i16;
                                    i18 = i2;
                                    i22 = i6;
                                    zzayVar4 = zzayVar2;
                                    i20 = -1;
                                    i19 = i3;
                                    break;
                                } else {
                                    i24 = i16;
                                    i4 = i3;
                                    i14 = i15;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i21 = zza(i6, bArr, i14, i2, t, zzayVar);
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i18 = i2;
                                    i19 = i4;
                                    i22 = i6;
                                    unsafe2 = unsafe;
                                    i20 = -1;
                                    zzayVar4 = zzayVar;
                                    break;
                                }
                                break;
                            case 2:
                            case 3:
                                i6 = i30;
                                i15 = i9;
                                i16 = i24;
                                bArr2 = bArr;
                                if (i27 == 0) {
                                    int zzb = zzax.zzb(bArr2, i15, zzayVar);
                                    unsafe2.putLong(t, j2, zzayVar.zzfe);
                                    i23 |= i32;
                                    i24 = i16;
                                    i18 = i2;
                                    i22 = i6;
                                    zzayVar4 = zzayVar;
                                    i21 = zzb;
                                    i20 = -1;
                                    i19 = i3;
                                    break;
                                } else {
                                    i24 = i16;
                                    i4 = i3;
                                    i14 = i15;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i21 = zza(i6, bArr, i14, i2, t, zzayVar);
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i18 = i2;
                                    i19 = i4;
                                    i22 = i6;
                                    unsafe2 = unsafe;
                                    i20 = -1;
                                    zzayVar4 = zzayVar;
                                    break;
                                }
                                break;
                            case 4:
                            case 11:
                                i6 = i30;
                                zzayVar2 = zzayVar;
                                i15 = i9;
                                i16 = i24;
                                bArr2 = bArr;
                                if (i27 == 0) {
                                    i21 = zzax.zza(bArr2, i15, zzayVar2);
                                    unsafe2.putInt(t3, j2, zzayVar2.zzfd);
                                    i23 |= i32;
                                    i24 = i16;
                                    i18 = i2;
                                    i22 = i6;
                                    zzayVar4 = zzayVar2;
                                    i20 = -1;
                                    i19 = i3;
                                    break;
                                } else {
                                    i24 = i16;
                                    i4 = i3;
                                    i14 = i15;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i21 = zza(i6, bArr, i14, i2, t, zzayVar);
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i18 = i2;
                                    i19 = i4;
                                    i22 = i6;
                                    unsafe2 = unsafe;
                                    i20 = -1;
                                    zzayVar4 = zzayVar;
                                    break;
                                }
                                break;
                            case 5:
                            case 14:
                                i6 = i30;
                                zzayVar2 = zzayVar;
                                i16 = i24;
                                bArr2 = bArr;
                                if (i27 == 1) {
                                    unsafe2.putLong(t, j2, zzax.zzd(bArr2, i9));
                                    i21 = i9 + 8;
                                    i23 |= i32;
                                    i24 = i16;
                                    i18 = i2;
                                    i22 = i6;
                                    zzayVar4 = zzayVar2;
                                    i20 = -1;
                                    i19 = i3;
                                    break;
                                } else {
                                    i15 = i9;
                                    i24 = i16;
                                    i4 = i3;
                                    i14 = i15;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i21 = zza(i6, bArr, i14, i2, t, zzayVar);
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i18 = i2;
                                    i19 = i4;
                                    i22 = i6;
                                    unsafe2 = unsafe;
                                    i20 = -1;
                                    zzayVar4 = zzayVar;
                                    break;
                                }
                                break;
                            case 6:
                            case 13:
                                i6 = i30;
                                i17 = i2;
                                zzayVar2 = zzayVar;
                                i16 = i24;
                                bArr2 = bArr;
                                if (i27 == 5) {
                                    unsafe2.putInt(t3, j2, zzax.zzc(bArr2, i9));
                                    i21 = i9 + 4;
                                    i23 |= i32;
                                    i24 = i16;
                                    i18 = i17;
                                    i22 = i6;
                                    zzayVar4 = zzayVar2;
                                    i20 = -1;
                                    i19 = i3;
                                    break;
                                } else {
                                    i15 = i9;
                                    i24 = i16;
                                    i4 = i3;
                                    i14 = i15;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i21 = zza(i6, bArr, i14, i2, t, zzayVar);
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i18 = i2;
                                    i19 = i4;
                                    i22 = i6;
                                    unsafe2 = unsafe;
                                    i20 = -1;
                                    zzayVar4 = zzayVar;
                                    break;
                                }
                                break;
                            case 7:
                                i6 = i30;
                                i17 = i2;
                                zzayVar2 = zzayVar;
                                i16 = i24;
                                bArr2 = bArr;
                                if (i27 == 0) {
                                    i21 = zzax.zzb(bArr2, i9, zzayVar2);
                                    zzfd.zza(t3, j2, zzayVar2.zzfe != 0);
                                    i23 |= i32;
                                    i24 = i16;
                                    i18 = i17;
                                    i22 = i6;
                                    zzayVar4 = zzayVar2;
                                    i20 = -1;
                                    i19 = i3;
                                    break;
                                } else {
                                    i15 = i9;
                                    i24 = i16;
                                    i4 = i3;
                                    i14 = i15;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i21 = zza(i6, bArr, i14, i2, t, zzayVar);
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i18 = i2;
                                    i19 = i4;
                                    i22 = i6;
                                    unsafe2 = unsafe;
                                    i20 = -1;
                                    zzayVar4 = zzayVar;
                                    break;
                                }
                                break;
                            case 8:
                                i6 = i30;
                                i17 = i2;
                                zzayVar2 = zzayVar;
                                i16 = i24;
                                j = j2;
                                bArr2 = bArr;
                                if (i27 == 2) {
                                    i21 = (i28 & 536870912) == 0 ? zzax.zzc(bArr2, i9, zzayVar2) : zzax.zzd(bArr2, i9, zzayVar2);
                                    obj = zzayVar2.zzff;
                                    unsafe2.putObject(t3, j, obj);
                                    i23 |= i32;
                                    i24 = i16;
                                    i18 = i17;
                                    i22 = i6;
                                    zzayVar4 = zzayVar2;
                                    i20 = -1;
                                    i19 = i3;
                                    break;
                                } else {
                                    i15 = i9;
                                    i24 = i16;
                                    i4 = i3;
                                    i14 = i15;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i21 = zza(i6, bArr, i14, i2, t, zzayVar);
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i18 = i2;
                                    i19 = i4;
                                    i22 = i6;
                                    unsafe2 = unsafe;
                                    i20 = -1;
                                    zzayVar4 = zzayVar;
                                    break;
                                }
                                break;
                            case 9:
                                i6 = i30;
                                zzayVar2 = zzayVar;
                                i16 = i24;
                                j = j2;
                                bArr2 = bArr;
                                if (i27 == 2) {
                                    i17 = i2;
                                    i21 = zza(zzdsVar.zzad(zzai), bArr2, i9, i17, zzayVar2);
                                    obj = (i23 & i32) == 0 ? zzayVar2.zzff : zzci.zza(unsafe2.getObject(t3, j), zzayVar2.zzff);
                                    unsafe2.putObject(t3, j, obj);
                                    i23 |= i32;
                                    i24 = i16;
                                    i18 = i17;
                                    i22 = i6;
                                    zzayVar4 = zzayVar2;
                                    i20 = -1;
                                    i19 = i3;
                                    break;
                                } else {
                                    i15 = i9;
                                    i24 = i16;
                                    i4 = i3;
                                    i14 = i15;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i21 = zza(i6, bArr, i14, i2, t, zzayVar);
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i18 = i2;
                                    i19 = i4;
                                    i22 = i6;
                                    unsafe2 = unsafe;
                                    i20 = -1;
                                    zzayVar4 = zzayVar;
                                    break;
                                }
                                break;
                            case 10:
                                i6 = i30;
                                zzayVar3 = zzayVar;
                                i20 = -1;
                                bArr2 = bArr;
                                if (i27 == 2) {
                                    zze = zzax.zze(bArr2, i9, zzayVar3);
                                    unsafe2.putObject(t3, j2, zzayVar3.zzff);
                                    i23 |= i32;
                                    i18 = i2;
                                    i21 = zze;
                                    i22 = i6;
                                    zzayVar4 = zzayVar3;
                                    i19 = i3;
                                    break;
                                } else {
                                    i15 = i9;
                                    i16 = i24;
                                    i24 = i16;
                                    i4 = i3;
                                    i14 = i15;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i21 = zza(i6, bArr, i14, i2, t, zzayVar);
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i18 = i2;
                                    i19 = i4;
                                    i22 = i6;
                                    unsafe2 = unsafe;
                                    i20 = -1;
                                    zzayVar4 = zzayVar;
                                    break;
                                }
                                break;
                            case 12:
                                i6 = i30;
                                zzayVar3 = zzayVar;
                                i20 = -1;
                                bArr2 = bArr;
                                if (i27 == 0) {
                                    i21 = zzax.zza(bArr2, i9, zzayVar3);
                                    int i34 = zzayVar3.zzfd;
                                    zzck<?> zzaf2 = zzdsVar.zzaf(zzai);
                                    if (zzaf2 == null || zzaf2.zzb(i34) != null) {
                                        unsafe2.putInt(t3, j2, i34);
                                        i23 |= i32;
                                    } else {
                                        zzn(t).zzb(i6, Long.valueOf(i34));
                                    }
                                    i18 = i2;
                                    i22 = i6;
                                    zzayVar4 = zzayVar3;
                                    i19 = i3;
                                    break;
                                } else {
                                    i15 = i9;
                                    i16 = i24;
                                    i24 = i16;
                                    i4 = i3;
                                    i14 = i15;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i21 = zza(i6, bArr, i14, i2, t, zzayVar);
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i18 = i2;
                                    i19 = i4;
                                    i22 = i6;
                                    unsafe2 = unsafe;
                                    i20 = -1;
                                    zzayVar4 = zzayVar;
                                    break;
                                }
                                break;
                            case 15:
                                i6 = i30;
                                zzayVar3 = zzayVar;
                                i20 = -1;
                                bArr2 = bArr;
                                if (i27 == 0) {
                                    zze = zzax.zza(bArr2, i9, zzayVar3);
                                    unsafe2.putInt(t3, j2, zzbk.zzm(zzayVar3.zzfd));
                                    i23 |= i32;
                                    i18 = i2;
                                    i21 = zze;
                                    i22 = i6;
                                    zzayVar4 = zzayVar3;
                                    i19 = i3;
                                    break;
                                } else {
                                    i15 = i9;
                                    i16 = i24;
                                    i24 = i16;
                                    i4 = i3;
                                    i14 = i15;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i21 = zza(i6, bArr, i14, i2, t, zzayVar);
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i18 = i2;
                                    i19 = i4;
                                    i22 = i6;
                                    unsafe2 = unsafe;
                                    i20 = -1;
                                    zzayVar4 = zzayVar;
                                    break;
                                }
                                break;
                            case 16:
                                i6 = i30;
                                i20 = -1;
                                if (i27 == 0) {
                                    bArr2 = bArr;
                                    int zzb2 = zzax.zzb(bArr2, i9, zzayVar);
                                    unsafe2.putLong(t, j2, zzbk.zza(zzayVar.zzfe));
                                    i23 |= i32;
                                    i22 = i6;
                                    zzayVar4 = zzayVar;
                                    i21 = zzb2;
                                    i18 = i2;
                                    i19 = i3;
                                    break;
                                } else {
                                    i15 = i9;
                                    i16 = i24;
                                    i24 = i16;
                                    i4 = i3;
                                    i14 = i15;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i21 = zza(i6, bArr, i14, i2, t, zzayVar);
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i18 = i2;
                                    i19 = i4;
                                    i22 = i6;
                                    unsafe2 = unsafe;
                                    i20 = -1;
                                    zzayVar4 = zzayVar;
                                    break;
                                }
                                break;
                            case 17:
                                if (i27 == 3) {
                                    i6 = i30;
                                    i20 = -1;
                                    i21 = zza(zzdsVar.zzad(zzai), bArr, i9, i2, (i26 << 3) | 4, zzayVar);
                                    zzayVar3 = zzayVar;
                                    unsafe2.putObject(t3, j2, (i23 & i32) == 0 ? zzayVar3.zzff : zzci.zza(unsafe2.getObject(t3, j2), zzayVar3.zzff));
                                    i23 |= i32;
                                    bArr2 = bArr;
                                    i18 = i2;
                                    i22 = i6;
                                    zzayVar4 = zzayVar3;
                                    i19 = i3;
                                    break;
                                } else {
                                    i6 = i30;
                                    i15 = i9;
                                    i16 = i24;
                                    i24 = i16;
                                    i4 = i3;
                                    i14 = i15;
                                    unsafe = unsafe2;
                                    if (i6 != i4) {
                                    }
                                    i21 = zza(i6, bArr, i14, i2, t, zzayVar);
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i18 = i2;
                                    i19 = i4;
                                    i22 = i6;
                                    unsafe2 = unsafe;
                                    i20 = -1;
                                    zzayVar4 = zzayVar;
                                    break;
                                }
                                break;
                            default:
                                i6 = i30;
                                i15 = i9;
                                i16 = i24;
                                i24 = i16;
                                i4 = i3;
                                i14 = i15;
                                unsafe = unsafe2;
                                if (i6 != i4) {
                                }
                                i21 = zza(i6, bArr, i14, i2, t, zzayVar);
                                zzdsVar = this;
                                t3 = t;
                                bArr2 = bArr;
                                i18 = i2;
                                i19 = i4;
                                i22 = i6;
                                unsafe2 = unsafe;
                                i20 = -1;
                                zzayVar4 = zzayVar;
                                break;
                        }
                    } else {
                        int i35 = i9;
                        i13 = i24;
                        bArr2 = bArr;
                        if (i29 != 27) {
                            i12 = i23;
                            if (i29 <= 49) {
                                i11 = i30;
                                unsafe = unsafe2;
                                i21 = zza((zzds<T>) t, bArr, i35, i2, i30, i26, i27, zzai, i28, i29, j2, zzayVar);
                                if (i21 == i35) {
                                    i6 = i11;
                                    i4 = i3;
                                    i14 = i21;
                                    i24 = i13;
                                    i23 = i12;
                                } else {
                                    zzdsVar = this;
                                    t3 = t;
                                    bArr2 = bArr;
                                    i22 = i11;
                                    i18 = i2;
                                    i19 = i3;
                                    zzayVar4 = zzayVar;
                                    i24 = i13;
                                    i23 = i12;
                                    unsafe2 = unsafe;
                                    i20 = -1;
                                }
                            } else {
                                i10 = i35;
                                i11 = i30;
                                unsafe = unsafe2;
                                if (i29 != 50) {
                                    i21 = zza((zzds<T>) t, bArr, i10, i2, i11, i26, i27, i28, i29, j2, zzai, zzayVar);
                                    if (i21 == i10) {
                                        i6 = i11;
                                        i4 = i3;
                                        i14 = i21;
                                        i24 = i13;
                                        i23 = i12;
                                    } else {
                                        zzdsVar = this;
                                        t3 = t;
                                        bArr2 = bArr;
                                        i22 = i11;
                                        i18 = i2;
                                        i19 = i3;
                                        zzayVar4 = zzayVar;
                                        i24 = i13;
                                        i23 = i12;
                                        unsafe2 = unsafe;
                                        i20 = -1;
                                    }
                                } else if (i27 == 2) {
                                    i21 = zza(t, bArr, i10, i2, zzai, i26, j2, zzayVar);
                                    if (i21 == i10) {
                                        i6 = i11;
                                        i4 = i3;
                                        i14 = i21;
                                        i24 = i13;
                                        i23 = i12;
                                    } else {
                                        zzdsVar = this;
                                        t3 = t;
                                        bArr2 = bArr;
                                        i22 = i11;
                                        i18 = i2;
                                        i19 = i3;
                                        zzayVar4 = zzayVar;
                                        i24 = i13;
                                        i23 = i12;
                                        unsafe2 = unsafe;
                                        i20 = -1;
                                    }
                                } else {
                                    i6 = i11;
                                    i4 = i3;
                                    i14 = i10;
                                    i24 = i13;
                                    i23 = i12;
                                }
                            }
                            if (i6 != i4) {
                            }
                            i21 = zza(i6, bArr, i14, i2, t, zzayVar);
                            zzdsVar = this;
                            t3 = t;
                            bArr2 = bArr;
                            i18 = i2;
                            i19 = i4;
                            i22 = i6;
                            unsafe2 = unsafe;
                            i20 = -1;
                            zzayVar4 = zzayVar;
                        } else if (i27 == 2) {
                            zzcn zzcnVar = (zzcn) unsafe2.getObject(t3, j2);
                            if (!zzcnVar.zzu()) {
                                int size = zzcnVar.size();
                                zzcnVar = zzcnVar.zzi(size == 0 ? 10 : size << 1);
                                unsafe2.putObject(t3, j2, zzcnVar);
                            }
                            zzcn zzcnVar2 = zzcnVar;
                            zzef zzad = zzdsVar.zzad(zzai);
                            i22 = i30;
                            i21 = zza(zzad, i22, bArr, i35, i2, zzcnVar2, zzayVar);
                            i18 = i2;
                            i19 = i3;
                            i24 = i13;
                            i23 = i23;
                            i20 = -1;
                            zzayVar4 = zzayVar;
                        } else {
                            i12 = i23;
                            i10 = i35;
                            i11 = i30;
                        }
                    }
                } else {
                    i10 = i9;
                    i11 = b;
                    i12 = i23;
                    i13 = i24;
                }
                unsafe = unsafe2;
                i6 = i11;
                i4 = i3;
                i14 = i10;
                i24 = i13;
                i23 = i12;
                if (i6 != i4) {
                }
                i21 = zza(i6, bArr, i14, i2, t, zzayVar);
                zzdsVar = this;
                t3 = t;
                bArr2 = bArr;
                i18 = i2;
                i19 = i4;
                i22 = i6;
                unsafe2 = unsafe;
                i20 = -1;
                zzayVar4 = zzayVar;
            } else {
                int i36 = i24;
                unsafe = unsafe2;
                i4 = i19;
                i5 = i21;
                i6 = i22;
                i7 = i36;
                i8 = -1;
            }
        }
        if (i7 != i8) {
            t2 = t;
            unsafe.putInt(t2, i7, i23);
        } else {
            t2 = t;
        }
        int[] iArr2 = this.zzmt;
        if (iArr2 != null) {
            Object obj2 = null;
            for (int i37 : iArr2) {
                zzex zzexVar = this.zzmx;
                int i38 = this.zzmi[i37];
                Object zzo = zzfd.zzo(t2, zzag(i37) & 1048575);
                if (zzo != null && (zzaf = zzaf(i37)) != null) {
                    obj2 = zza(i37, i38, this.zzmz.zzg(zzo), zzaf, (zzck<?>) obj2, (zzex<UT, zzck<?>>) zzexVar);
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

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> zzds<T> zza(Class<T> cls, zzdm zzdmVar, zzdw zzdwVar, zzcy zzcyVar, zzex<?, ?> zzexVar, zzbu<?> zzbuVar, zzdj zzdjVar) {
        int zzcu;
        int i;
        int i2;
        int zza;
        int i3;
        int i4;
        if (!(zzdmVar instanceof zzec)) {
            ((zzes) zzdmVar).zzcf();
            throw new NoSuchMethodError();
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
            int i5 = 0;
            int i6 = 0;
            int i7 = 0;
            while (true) {
                if (zzcx >= zzecVar.zzcv() || i5 >= ((zzcx - i) << 2)) {
                    if (zzco.zzda()) {
                        zza = (int) zzfd.zza(zzco.zzdb());
                        i4 = (int) zzfd.zza(zzco.zzdc());
                        i3 = 0;
                    } else {
                        zza = (int) zzfd.zza(zzco.zzdd());
                        if (zzco.zzde()) {
                            i4 = (int) zzfd.zza(zzco.zzdf());
                            i3 = zzco.zzdg();
                        } else {
                            i3 = 0;
                            i4 = 0;
                        }
                    }
                    iArr[i5] = zzco.zzcx();
                    int i8 = i5 + 1;
                    iArr[i8] = (zzco.zzdi() ? 536870912 : 0) | (zzco.zzdh() ? 268435456 : 0) | (zzco.zzcy() << 20) | zza;
                    iArr[i5 + 2] = (i3 << 20) | i4;
                    if (zzco.zzdl() != null) {
                        int i9 = (i5 / 4) << 1;
                        objArr[i9] = zzco.zzdl();
                        if (zzco.zzdj() != null) {
                            objArr[i9 + 1] = zzco.zzdj();
                        } else if (zzco.zzdk() != null) {
                            objArr[i9 + 1] = zzco.zzdk();
                        }
                    } else if (zzco.zzdj() != null) {
                        objArr[((i5 / 4) << 1) + 1] = zzco.zzdj();
                    } else if (zzco.zzdk() != null) {
                        objArr[((i5 / 4) << 1) + 1] = zzco.zzdk();
                    }
                    int zzcy = zzco.zzcy();
                    if (zzcy == zzcb.zziw.ordinal()) {
                        iArr2[i6] = i5;
                        i6++;
                    } else if (zzcy >= 18 && zzcy <= 49) {
                        iArr3[i7] = iArr[i8] & 1048575;
                        i7++;
                    }
                    if (!zzco.next()) {
                        break;
                    }
                    zzcx = zzco.zzcx();
                } else {
                    for (int i10 = 0; i10 < 4; i10++) {
                        iArr[i5 + i10] = -1;
                    }
                }
                i5 += 4;
            }
        }
        return new zzds<>(iArr, objArr, i, i2, zzecVar.zzcv(), zzecVar.zzch(), z, false, zzecVar.zzct(), iArr2, iArr3, zzdwVar, zzcyVar, zzexVar, zzbuVar, zzdjVar);
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

    /* JADX WARN: Removed duplicated region for block: B:230:0x0494  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x002d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void zzb(T t, zzfr zzfrVar) throws IOException {
        Iterator<Map.Entry<?, Object>> it;
        Map.Entry<?, Object> entry;
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
                        case R.styleable.MapAttrs_uiZoomControls /* 20 */:
                            zzeh.zzc(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, false);
                            continue;
                            i = i10 + 4;
                        case 21:
                            zzeh.zzd(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, false);
                            continue;
                            i = i10 + 4;
                        case R.styleable.MapAttrs_useViewLifecycle /* 22 */:
                            zzeh.zzh(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, false);
                            continue;
                            i = i10 + 4;
                        case R.styleable.MapAttrs_zOrderOnTop /* 23 */:
                            zzeh.zzf(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, false);
                            continue;
                            i = i10 + 4;
                        case 24:
                            zzeh.zzk(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, false);
                            continue;
                            i = i10 + 4;
                        case AvailableCode.ERROR_ON_ACTIVITY_RESULT /* 25 */:
                            zzeh.zzn(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, false);
                            continue;
                            i = i10 + 4;
                        case AvailableCode.ERROR_NO_ACTIVITY /* 26 */:
                            zzeh.zza(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar);
                            break;
                        case AvailableCode.USER_IGNORE_PREVIOUS_POPUP /* 27 */:
                            zzeh.zza(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, zzad(i10));
                            break;
                        case AvailableCode.APP_IS_BACKGROUND_OR_LOCKED /* 28 */:
                            zzeh.zzb(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar);
                            break;
                        case AvailableCode.HMS_IS_SPOOF /* 29 */:
                            zzeh.zzi(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, false);
                            continue;
                            i = i10 + 4;
                        case AvailableCode.USER_ALREADY_KNOWS_SERVICE_UNAVAILABLE /* 30 */:
                            zzeh.zzm(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, false);
                            continue;
                            i = i10 + 4;
                        case AvailableCode.CURRENT_SHOWING_SERVICE_UNAVAILABLE /* 31 */:
                            zzeh.zzl(this.zzmi[i10], (List) unsafe.getObject(t, j), zzfrVar, false);
                            continue;
                            i = i10 + 4;
                        case ConnectionsManager.RequestFlagForceDownload /* 32 */:
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
                        case VoIPService.CALL_MIN_LAYER /* 65 */:
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

    /* JADX WARN: Code restructure failed: missing block: B:15:0x005c, code lost:
        if (com.google.android.gms.internal.clearcut.zzeh.zzd(com.google.android.gms.internal.clearcut.zzfd.zzo(r10, r6), com.google.android.gms.internal.clearcut.zzfd.zzo(r11, r6)) != false) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0070, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzk(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzk(r11, r6)) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0082, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzj(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzj(r11, r6)) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0096, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzk(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzk(r11, r6)) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00a8, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzj(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzj(r11, r6)) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00ba, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzj(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzj(r11, r6)) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00cc, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzj(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzj(r11, r6)) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00e2, code lost:
        if (com.google.android.gms.internal.clearcut.zzeh.zzd(com.google.android.gms.internal.clearcut.zzfd.zzo(r10, r6), com.google.android.gms.internal.clearcut.zzfd.zzo(r11, r6)) != false) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00f8, code lost:
        if (com.google.android.gms.internal.clearcut.zzeh.zzd(com.google.android.gms.internal.clearcut.zzfd.zzo(r10, r6), com.google.android.gms.internal.clearcut.zzfd.zzo(r11, r6)) != false) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x010e, code lost:
        if (com.google.android.gms.internal.clearcut.zzeh.zzd(com.google.android.gms.internal.clearcut.zzfd.zzo(r10, r6), com.google.android.gms.internal.clearcut.zzfd.zzo(r11, r6)) != false) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0120, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzl(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzl(r11, r6)) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0132, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzj(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzj(r11, r6)) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0145, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzk(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzk(r11, r6)) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0156, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzj(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzj(r11, r6)) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0169, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzk(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzk(r11, r6)) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x017c, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzk(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzk(r11, r6)) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x018d, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzj(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzj(r11, r6)) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x01a0, code lost:
        if (com.google.android.gms.internal.clearcut.zzfd.zzk(r10, r6) == com.google.android.gms.internal.clearcut.zzfd.zzk(r11, r6)) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0038, code lost:
        if (com.google.android.gms.internal.clearcut.zzeh.zzd(com.google.android.gms.internal.clearcut.zzfd.zzo(r10, r6), com.google.android.gms.internal.clearcut.zzfd.zzo(r11, r6)) != false) goto L84;
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
                case R.styleable.MapAttrs_uiZoomControls /* 20 */:
                case 21:
                case R.styleable.MapAttrs_useViewLifecycle /* 22 */:
                case R.styleable.MapAttrs_zOrderOnTop /* 23 */:
                case 24:
                case AvailableCode.ERROR_ON_ACTIVITY_RESULT /* 25 */:
                case AvailableCode.ERROR_NO_ACTIVITY /* 26 */:
                case AvailableCode.USER_IGNORE_PREVIOUS_POPUP /* 27 */:
                case AvailableCode.APP_IS_BACKGROUND_OR_LOCKED /* 28 */:
                case AvailableCode.HMS_IS_SPOOF /* 29 */:
                case AvailableCode.USER_ALREADY_KNOWS_SERVICE_UNAVAILABLE /* 30 */:
                case AvailableCode.CURRENT_SHOWING_SERVICE_UNAVAILABLE /* 31 */:
                case ConnectionsManager.RequestFlagForceDownload /* 32 */:
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
                case VoIPService.CALL_MIN_LAYER /* 65 */:
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
    /* JADX WARN: Code restructure failed: missing block: B:74:0x00ce, code lost:
        if (r3 != null) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x00e6, code lost:
        r2 = (r2 * 53) + r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x00e2, code lost:
        r7 = r3.hashCode();
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x00e0, code lost:
        if (r3 != null) goto L77;
     */
    @Override // com.google.android.gms.internal.clearcut.zzef
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final int hashCode(T t) {
        int i;
        double zzn;
        float zzm;
        long zzk;
        int zzj;
        boolean zzl;
        Object zzo;
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
                    zzn = zzfd.zzn(t, j);
                    zzk = Double.doubleToLongBits(zzn);
                    zzj = zzci.zzl(zzk);
                    i2 = i + zzj;
                    break;
                case 1:
                    i = i2 * 53;
                    zzm = zzfd.zzm(t, j);
                    zzj = Float.floatToIntBits(zzm);
                    i2 = i + zzj;
                    break;
                case 2:
                case 3:
                case 5:
                case 14:
                case 16:
                    i = i2 * 53;
                    zzk = zzfd.zzk(t, j);
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
                    zzj = zzfd.zzj(t, j);
                    i2 = i + zzj;
                    break;
                case 7:
                    i = i2 * 53;
                    zzl = zzfd.zzl(t, j);
                    zzj = zzci.zzc(zzl);
                    i2 = i + zzj;
                    break;
                case 8:
                    i = i2 * 53;
                    zzj = ((String) zzfd.zzo(t, j)).hashCode();
                    i2 = i + zzj;
                    break;
                case 9:
                    zzo = zzfd.zzo(t, j);
                    break;
                case 10:
                case 18:
                case 19:
                case R.styleable.MapAttrs_uiZoomControls /* 20 */:
                case 21:
                case R.styleable.MapAttrs_useViewLifecycle /* 22 */:
                case R.styleable.MapAttrs_zOrderOnTop /* 23 */:
                case 24:
                case AvailableCode.ERROR_ON_ACTIVITY_RESULT /* 25 */:
                case AvailableCode.ERROR_NO_ACTIVITY /* 26 */:
                case AvailableCode.USER_IGNORE_PREVIOUS_POPUP /* 27 */:
                case AvailableCode.APP_IS_BACKGROUND_OR_LOCKED /* 28 */:
                case AvailableCode.HMS_IS_SPOOF /* 29 */:
                case AvailableCode.USER_ALREADY_KNOWS_SERVICE_UNAVAILABLE /* 30 */:
                case AvailableCode.CURRENT_SHOWING_SERVICE_UNAVAILABLE /* 31 */:
                case ConnectionsManager.RequestFlagForceDownload /* 32 */:
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
                    zzo2 = zzfd.zzo(t, j);
                    zzj = zzo2.hashCode();
                    i2 = i + zzj;
                    break;
                case 17:
                    zzo = zzfd.zzo(t, j);
                    break;
                case 51:
                    if (zza((zzds<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zzn = zze(t, j);
                        zzk = Double.doubleToLongBits(zzn);
                        zzj = zzci.zzl(zzk);
                        i2 = i + zzj;
                        break;
                    } else {
                        break;
                    }
                case 52:
                    if (zza((zzds<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zzm = zzf(t, j);
                        zzj = Float.floatToIntBits(zzm);
                        i2 = i + zzj;
                        break;
                    } else {
                        break;
                    }
                case 53:
                    if (!zza((zzds<T>) t, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzk = zzh(t, j);
                    zzj = zzci.zzl(zzk);
                    i2 = i + zzj;
                    break;
                case 54:
                    if (!zza((zzds<T>) t, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzk = zzh(t, j);
                    zzj = zzci.zzl(zzk);
                    i2 = i + zzj;
                    break;
                case 55:
                    if (!zza((zzds<T>) t, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzj = zzg(t, j);
                    i2 = i + zzj;
                    break;
                case 56:
                    if (!zza((zzds<T>) t, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzk = zzh(t, j);
                    zzj = zzci.zzl(zzk);
                    i2 = i + zzj;
                    break;
                case 57:
                    if (!zza((zzds<T>) t, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzj = zzg(t, j);
                    i2 = i + zzj;
                    break;
                case 58:
                    if (zza((zzds<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zzl = zzi(t, j);
                        zzj = zzci.zzc(zzl);
                        i2 = i + zzj;
                        break;
                    } else {
                        break;
                    }
                case 59:
                    if (!zza((zzds<T>) t, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzj = ((String) zzfd.zzo(t, j)).hashCode();
                    i2 = i + zzj;
                    break;
                case 60:
                    if (!zza((zzds<T>) t, i4, i3)) {
                        break;
                    }
                    zzo2 = zzfd.zzo(t, j);
                    i = i2 * 53;
                    zzj = zzo2.hashCode();
                    i2 = i + zzj;
                    break;
                case 61:
                    if (!zza((zzds<T>) t, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzo2 = zzfd.zzo(t, j);
                    zzj = zzo2.hashCode();
                    i2 = i + zzj;
                    break;
                case 62:
                    if (!zza((zzds<T>) t, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzj = zzg(t, j);
                    i2 = i + zzj;
                    break;
                case 63:
                    if (!zza((zzds<T>) t, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzj = zzg(t, j);
                    i2 = i + zzj;
                    break;
                case 64:
                    if (!zza((zzds<T>) t, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzj = zzg(t, j);
                    i2 = i + zzj;
                    break;
                case VoIPService.CALL_MIN_LAYER /* 65 */:
                    if (!zza((zzds<T>) t, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzk = zzh(t, j);
                    zzj = zzci.zzl(zzk);
                    i2 = i + zzj;
                    break;
                case 66:
                    if (!zza((zzds<T>) t, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzj = zzg(t, j);
                    i2 = i + zzj;
                    break;
                case 67:
                    if (!zza((zzds<T>) t, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzk = zzh(t, j);
                    zzj = zzci.zzl(zzk);
                    i2 = i + zzj;
                    break;
                case 68:
                    if (!zza((zzds<T>) t, i4, i3)) {
                        break;
                    }
                    zzo2 = zzfd.zzo(t, j);
                    i = i2 * 53;
                    zzj = zzo2.hashCode();
                    i2 = i + zzj;
                    break;
            }
        }
        int hashCode = (i2 * 53) + this.zzmx.zzq(t).hashCode();
        return this.zzmo ? (hashCode * 53) + this.zzmy.zza(t).hashCode() : hashCode;
    }

    @Override // com.google.android.gms.internal.clearcut.zzef
    public final T newInstance() {
        return (T) this.zzmv.newInstance(this.zzmn);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0039  */
    /* JADX WARN: Removed duplicated region for block: B:269:0x04b9  */
    /* JADX WARN: Removed duplicated region for block: B:291:0x04f6  */
    /* JADX WARN: Removed duplicated region for block: B:550:0x0976  */
    @Override // com.google.android.gms.internal.clearcut.zzef
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void zza(T t, zzfr zzfrVar) throws IOException {
        Iterator<Map.Entry<?, Object>> it;
        Map.Entry<?, Object> entry;
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
        Iterator<Map.Entry<?, Object>> it2;
        Map.Entry<?, Object> entry2;
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
            zza(this.zzmx, t, zzfrVar);
            if (this.zzmo) {
                zzby<?> zza = this.zzmy.zza(t);
                if (!zza.isEmpty()) {
                    it2 = zza.descendingIterator();
                    entry2 = it2.next();
                    for (length2 = this.zzmi.length - 4; length2 >= 0; length2 -= 4) {
                        int zzag = zzag(length2);
                        int i2 = this.zzmi[length2];
                        while (entry2 != null && this.zzmy.zza(entry2) > i2) {
                            this.zzmy.zza(zzfrVar, entry2);
                            entry2 = it2.hasNext() ? it2.next() : null;
                        }
                        switch ((zzag & 267386880) >>> 20) {
                            case 0:
                                if (zza((zzds<T>) t, length2)) {
                                    zzn2 = zzfd.zzn(t, zzag & 1048575);
                                    zzfrVar.zza(i2, zzn2);
                                    break;
                                } else {
                                    break;
                                }
                            case 1:
                                if (zza((zzds<T>) t, length2)) {
                                    zzm2 = zzfd.zzm(t, zzag & 1048575);
                                    zzfrVar.zza(i2, zzm2);
                                    break;
                                } else {
                                    break;
                                }
                            case 2:
                                if (zza((zzds<T>) t, length2)) {
                                    zzk6 = zzfd.zzk(t, zzag & 1048575);
                                    zzfrVar.zzi(i2, zzk6);
                                    break;
                                } else {
                                    break;
                                }
                            case 3:
                                if (zza((zzds<T>) t, length2)) {
                                    zzk7 = zzfd.zzk(t, zzag & 1048575);
                                    zzfrVar.zza(i2, zzk7);
                                    break;
                                } else {
                                    break;
                                }
                            case 4:
                                if (zza((zzds<T>) t, length2)) {
                                    zzj7 = zzfd.zzj(t, zzag & 1048575);
                                    zzfrVar.zzc(i2, zzj7);
                                    break;
                                } else {
                                    break;
                                }
                            case 5:
                                if (zza((zzds<T>) t, length2)) {
                                    zzk8 = zzfd.zzk(t, zzag & 1048575);
                                    zzfrVar.zzc(i2, zzk8);
                                    break;
                                } else {
                                    break;
                                }
                            case 6:
                                if (zza((zzds<T>) t, length2)) {
                                    zzj8 = zzfd.zzj(t, zzag & 1048575);
                                    zzfrVar.zzf(i2, zzj8);
                                    break;
                                } else {
                                    break;
                                }
                            case 7:
                                if (zza((zzds<T>) t, length2)) {
                                    zzl2 = zzfd.zzl(t, zzag & 1048575);
                                    zzfrVar.zzb(i2, zzl2);
                                    break;
                                } else {
                                    break;
                                }
                            case 8:
                                if (!zza((zzds<T>) t, length2)) {
                                    break;
                                }
                                zza(i2, zzfd.zzo(t, zzag & 1048575), zzfrVar);
                                break;
                            case 9:
                                if (!zza((zzds<T>) t, length2)) {
                                    break;
                                }
                                zzfrVar.zza(i2, zzfd.zzo(t, zzag & 1048575), zzad(length2));
                                break;
                            case 10:
                                if (!zza((zzds<T>) t, length2)) {
                                    break;
                                }
                                zzfrVar.zza(i2, (zzbb) zzfd.zzo(t, zzag & 1048575));
                                break;
                            case 11:
                                if (zza((zzds<T>) t, length2)) {
                                    zzj9 = zzfd.zzj(t, zzag & 1048575);
                                    zzfrVar.zzd(i2, zzj9);
                                    break;
                                } else {
                                    break;
                                }
                            case 12:
                                if (zza((zzds<T>) t, length2)) {
                                    zzj10 = zzfd.zzj(t, zzag & 1048575);
                                    zzfrVar.zzn(i2, zzj10);
                                    break;
                                } else {
                                    break;
                                }
                            case 13:
                                if (zza((zzds<T>) t, length2)) {
                                    zzj11 = zzfd.zzj(t, zzag & 1048575);
                                    zzfrVar.zzm(i2, zzj11);
                                    break;
                                } else {
                                    break;
                                }
                            case 14:
                                if (zza((zzds<T>) t, length2)) {
                                    zzk9 = zzfd.zzk(t, zzag & 1048575);
                                    zzfrVar.zzj(i2, zzk9);
                                    break;
                                } else {
                                    break;
                                }
                            case 15:
                                if (zza((zzds<T>) t, length2)) {
                                    zzj12 = zzfd.zzj(t, zzag & 1048575);
                                    zzfrVar.zze(i2, zzj12);
                                    break;
                                } else {
                                    break;
                                }
                            case 16:
                                if (zza((zzds<T>) t, length2)) {
                                    zzk10 = zzfd.zzk(t, zzag & 1048575);
                                    zzfrVar.zzb(i2, zzk10);
                                    break;
                                } else {
                                    break;
                                }
                            case 17:
                                if (!zza((zzds<T>) t, length2)) {
                                    break;
                                }
                                zzfrVar.zzb(i2, zzfd.zzo(t, zzag & 1048575), zzad(length2));
                                break;
                            case 18:
                                zzeh.zza(this.zzmi[length2], (List<Double>) zzfd.zzo(t, zzag & 1048575), zzfrVar, false);
                                break;
                            case 19:
                                zzeh.zzb(this.zzmi[length2], (List<Float>) zzfd.zzo(t, zzag & 1048575), zzfrVar, false);
                                break;
                            case R.styleable.MapAttrs_uiZoomControls /* 20 */:
                                zzeh.zzc(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, false);
                                break;
                            case 21:
                                zzeh.zzd(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, false);
                                break;
                            case R.styleable.MapAttrs_useViewLifecycle /* 22 */:
                                zzeh.zzh(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, false);
                                break;
                            case R.styleable.MapAttrs_zOrderOnTop /* 23 */:
                                zzeh.zzf(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, false);
                                break;
                            case 24:
                                zzeh.zzk(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, false);
                                break;
                            case AvailableCode.ERROR_ON_ACTIVITY_RESULT /* 25 */:
                                zzeh.zzn(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, false);
                                break;
                            case AvailableCode.ERROR_NO_ACTIVITY /* 26 */:
                                zzeh.zza(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar);
                                break;
                            case AvailableCode.USER_IGNORE_PREVIOUS_POPUP /* 27 */:
                                zzeh.zza(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, zzad(length2));
                                break;
                            case AvailableCode.APP_IS_BACKGROUND_OR_LOCKED /* 28 */:
                                zzeh.zzb(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar);
                                break;
                            case AvailableCode.HMS_IS_SPOOF /* 29 */:
                                zzeh.zzi(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, false);
                                break;
                            case AvailableCode.USER_ALREADY_KNOWS_SERVICE_UNAVAILABLE /* 30 */:
                                zzeh.zzm(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, false);
                                break;
                            case AvailableCode.CURRENT_SHOWING_SERVICE_UNAVAILABLE /* 31 */:
                                zzeh.zzl(this.zzmi[length2], (List) zzfd.zzo(t, zzag & 1048575), zzfrVar, false);
                                break;
                            case ConnectionsManager.RequestFlagForceDownload /* 32 */:
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
                                zza(zzfrVar, i2, zzfd.zzo(t, zzag & 1048575), length2);
                                break;
                            case 51:
                                if (zza((zzds<T>) t, i2, length2)) {
                                    zzn2 = zze(t, zzag & 1048575);
                                    zzfrVar.zza(i2, zzn2);
                                    break;
                                } else {
                                    break;
                                }
                            case 52:
                                if (zza((zzds<T>) t, i2, length2)) {
                                    zzm2 = zzf(t, zzag & 1048575);
                                    zzfrVar.zza(i2, zzm2);
                                    break;
                                } else {
                                    break;
                                }
                            case 53:
                                if (zza((zzds<T>) t, i2, length2)) {
                                    zzk6 = zzh(t, zzag & 1048575);
                                    zzfrVar.zzi(i2, zzk6);
                                    break;
                                } else {
                                    break;
                                }
                            case 54:
                                if (zza((zzds<T>) t, i2, length2)) {
                                    zzk7 = zzh(t, zzag & 1048575);
                                    zzfrVar.zza(i2, zzk7);
                                    break;
                                } else {
                                    break;
                                }
                            case 55:
                                if (zza((zzds<T>) t, i2, length2)) {
                                    zzj7 = zzg(t, zzag & 1048575);
                                    zzfrVar.zzc(i2, zzj7);
                                    break;
                                } else {
                                    break;
                                }
                            case 56:
                                if (zza((zzds<T>) t, i2, length2)) {
                                    zzk8 = zzh(t, zzag & 1048575);
                                    zzfrVar.zzc(i2, zzk8);
                                    break;
                                } else {
                                    break;
                                }
                            case 57:
                                if (zza((zzds<T>) t, i2, length2)) {
                                    zzj8 = zzg(t, zzag & 1048575);
                                    zzfrVar.zzf(i2, zzj8);
                                    break;
                                } else {
                                    break;
                                }
                            case 58:
                                if (zza((zzds<T>) t, i2, length2)) {
                                    zzl2 = zzi(t, zzag & 1048575);
                                    zzfrVar.zzb(i2, zzl2);
                                    break;
                                } else {
                                    break;
                                }
                            case 59:
                                if (!zza((zzds<T>) t, i2, length2)) {
                                    break;
                                }
                                zza(i2, zzfd.zzo(t, zzag & 1048575), zzfrVar);
                                break;
                            case 60:
                                if (!zza((zzds<T>) t, i2, length2)) {
                                    break;
                                }
                                zzfrVar.zza(i2, zzfd.zzo(t, zzag & 1048575), zzad(length2));
                                break;
                            case 61:
                                if (!zza((zzds<T>) t, i2, length2)) {
                                    break;
                                }
                                zzfrVar.zza(i2, (zzbb) zzfd.zzo(t, zzag & 1048575));
                                break;
                            case 62:
                                if (zza((zzds<T>) t, i2, length2)) {
                                    zzj9 = zzg(t, zzag & 1048575);
                                    zzfrVar.zzd(i2, zzj9);
                                    break;
                                } else {
                                    break;
                                }
                            case 63:
                                if (zza((zzds<T>) t, i2, length2)) {
                                    zzj10 = zzg(t, zzag & 1048575);
                                    zzfrVar.zzn(i2, zzj10);
                                    break;
                                } else {
                                    break;
                                }
                            case 64:
                                if (zza((zzds<T>) t, i2, length2)) {
                                    zzj11 = zzg(t, zzag & 1048575);
                                    zzfrVar.zzm(i2, zzj11);
                                    break;
                                } else {
                                    break;
                                }
                            case VoIPService.CALL_MIN_LAYER /* 65 */:
                                if (zza((zzds<T>) t, i2, length2)) {
                                    zzk9 = zzh(t, zzag & 1048575);
                                    zzfrVar.zzj(i2, zzk9);
                                    break;
                                } else {
                                    break;
                                }
                            case 66:
                                if (zza((zzds<T>) t, i2, length2)) {
                                    zzj12 = zzg(t, zzag & 1048575);
                                    zzfrVar.zze(i2, zzj12);
                                    break;
                                } else {
                                    break;
                                }
                            case 67:
                                if (zza((zzds<T>) t, i2, length2)) {
                                    zzk10 = zzh(t, zzag & 1048575);
                                    zzfrVar.zzb(i2, zzk10);
                                    break;
                                } else {
                                    break;
                                }
                            case 68:
                                if (!zza((zzds<T>) t, i2, length2)) {
                                    break;
                                }
                                zzfrVar.zzb(i2, zzfd.zzo(t, zzag & 1048575), zzad(length2));
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
                        int i3 = this.zzmi[i];
                        while (entry != null && this.zzmy.zza(entry) <= i3) {
                            this.zzmy.zza(zzfrVar, entry);
                            entry = it.hasNext() ? it.next() : null;
                        }
                        switch ((zzag2 & 267386880) >>> 20) {
                            case 0:
                                if (zza((zzds<T>) t, i)) {
                                    zzn = zzfd.zzn(t, zzag2 & 1048575);
                                    zzfrVar.zza(i3, zzn);
                                    break;
                                } else {
                                    break;
                                }
                            case 1:
                                if (zza((zzds<T>) t, i)) {
                                    zzm = zzfd.zzm(t, zzag2 & 1048575);
                                    zzfrVar.zza(i3, zzm);
                                    break;
                                } else {
                                    break;
                                }
                            case 2:
                                if (zza((zzds<T>) t, i)) {
                                    zzk = zzfd.zzk(t, zzag2 & 1048575);
                                    zzfrVar.zzi(i3, zzk);
                                    break;
                                } else {
                                    break;
                                }
                            case 3:
                                if (zza((zzds<T>) t, i)) {
                                    zzk2 = zzfd.zzk(t, zzag2 & 1048575);
                                    zzfrVar.zza(i3, zzk2);
                                    break;
                                } else {
                                    break;
                                }
                            case 4:
                                if (zza((zzds<T>) t, i)) {
                                    zzj = zzfd.zzj(t, zzag2 & 1048575);
                                    zzfrVar.zzc(i3, zzj);
                                    break;
                                } else {
                                    break;
                                }
                            case 5:
                                if (zza((zzds<T>) t, i)) {
                                    zzk3 = zzfd.zzk(t, zzag2 & 1048575);
                                    zzfrVar.zzc(i3, zzk3);
                                    break;
                                } else {
                                    break;
                                }
                            case 6:
                                if (zza((zzds<T>) t, i)) {
                                    zzj2 = zzfd.zzj(t, zzag2 & 1048575);
                                    zzfrVar.zzf(i3, zzj2);
                                    break;
                                } else {
                                    break;
                                }
                            case 7:
                                if (zza((zzds<T>) t, i)) {
                                    zzl = zzfd.zzl(t, zzag2 & 1048575);
                                    zzfrVar.zzb(i3, zzl);
                                    break;
                                } else {
                                    break;
                                }
                            case 8:
                                if (!zza((zzds<T>) t, i)) {
                                    break;
                                }
                                zza(i3, zzfd.zzo(t, zzag2 & 1048575), zzfrVar);
                                break;
                            case 9:
                                if (!zza((zzds<T>) t, i)) {
                                    break;
                                }
                                zzfrVar.zza(i3, zzfd.zzo(t, zzag2 & 1048575), zzad(i));
                                break;
                            case 10:
                                if (!zza((zzds<T>) t, i)) {
                                    break;
                                }
                                zzfrVar.zza(i3, (zzbb) zzfd.zzo(t, zzag2 & 1048575));
                                break;
                            case 11:
                                if (zza((zzds<T>) t, i)) {
                                    zzj3 = zzfd.zzj(t, zzag2 & 1048575);
                                    zzfrVar.zzd(i3, zzj3);
                                    break;
                                } else {
                                    break;
                                }
                            case 12:
                                if (zza((zzds<T>) t, i)) {
                                    zzj4 = zzfd.zzj(t, zzag2 & 1048575);
                                    zzfrVar.zzn(i3, zzj4);
                                    break;
                                } else {
                                    break;
                                }
                            case 13:
                                if (zza((zzds<T>) t, i)) {
                                    zzj5 = zzfd.zzj(t, zzag2 & 1048575);
                                    zzfrVar.zzm(i3, zzj5);
                                    break;
                                } else {
                                    break;
                                }
                            case 14:
                                if (zza((zzds<T>) t, i)) {
                                    zzk4 = zzfd.zzk(t, zzag2 & 1048575);
                                    zzfrVar.zzj(i3, zzk4);
                                    break;
                                } else {
                                    break;
                                }
                            case 15:
                                if (zza((zzds<T>) t, i)) {
                                    zzj6 = zzfd.zzj(t, zzag2 & 1048575);
                                    zzfrVar.zze(i3, zzj6);
                                    break;
                                } else {
                                    break;
                                }
                            case 16:
                                if (zza((zzds<T>) t, i)) {
                                    zzk5 = zzfd.zzk(t, zzag2 & 1048575);
                                    zzfrVar.zzb(i3, zzk5);
                                    break;
                                } else {
                                    break;
                                }
                            case 17:
                                if (!zza((zzds<T>) t, i)) {
                                    break;
                                }
                                zzfrVar.zzb(i3, zzfd.zzo(t, zzag2 & 1048575), zzad(i));
                                break;
                            case 18:
                                zzeh.zza(this.zzmi[i], (List<Double>) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, false);
                                break;
                            case 19:
                                zzeh.zzb(this.zzmi[i], (List<Float>) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, false);
                                break;
                            case R.styleable.MapAttrs_uiZoomControls /* 20 */:
                                zzeh.zzc(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, false);
                                break;
                            case 21:
                                zzeh.zzd(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, false);
                                break;
                            case R.styleable.MapAttrs_useViewLifecycle /* 22 */:
                                zzeh.zzh(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, false);
                                break;
                            case R.styleable.MapAttrs_zOrderOnTop /* 23 */:
                                zzeh.zzf(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, false);
                                break;
                            case 24:
                                zzeh.zzk(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, false);
                                break;
                            case AvailableCode.ERROR_ON_ACTIVITY_RESULT /* 25 */:
                                zzeh.zzn(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, false);
                                break;
                            case AvailableCode.ERROR_NO_ACTIVITY /* 26 */:
                                zzeh.zza(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar);
                                break;
                            case AvailableCode.USER_IGNORE_PREVIOUS_POPUP /* 27 */:
                                zzeh.zza(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, zzad(i));
                                break;
                            case AvailableCode.APP_IS_BACKGROUND_OR_LOCKED /* 28 */:
                                zzeh.zzb(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar);
                                break;
                            case AvailableCode.HMS_IS_SPOOF /* 29 */:
                                zzeh.zzi(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, false);
                                break;
                            case AvailableCode.USER_ALREADY_KNOWS_SERVICE_UNAVAILABLE /* 30 */:
                                zzeh.zzm(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, false);
                                break;
                            case AvailableCode.CURRENT_SHOWING_SERVICE_UNAVAILABLE /* 31 */:
                                zzeh.zzl(this.zzmi[i], (List) zzfd.zzo(t, zzag2 & 1048575), zzfrVar, false);
                                break;
                            case ConnectionsManager.RequestFlagForceDownload /* 32 */:
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
                                zza(zzfrVar, i3, zzfd.zzo(t, zzag2 & 1048575), i);
                                break;
                            case 51:
                                if (zza((zzds<T>) t, i3, i)) {
                                    zzn = zze(t, zzag2 & 1048575);
                                    zzfrVar.zza(i3, zzn);
                                    break;
                                } else {
                                    break;
                                }
                            case 52:
                                if (zza((zzds<T>) t, i3, i)) {
                                    zzm = zzf(t, zzag2 & 1048575);
                                    zzfrVar.zza(i3, zzm);
                                    break;
                                } else {
                                    break;
                                }
                            case 53:
                                if (zza((zzds<T>) t, i3, i)) {
                                    zzk = zzh(t, zzag2 & 1048575);
                                    zzfrVar.zzi(i3, zzk);
                                    break;
                                } else {
                                    break;
                                }
                            case 54:
                                if (zza((zzds<T>) t, i3, i)) {
                                    zzk2 = zzh(t, zzag2 & 1048575);
                                    zzfrVar.zza(i3, zzk2);
                                    break;
                                } else {
                                    break;
                                }
                            case 55:
                                if (zza((zzds<T>) t, i3, i)) {
                                    zzj = zzg(t, zzag2 & 1048575);
                                    zzfrVar.zzc(i3, zzj);
                                    break;
                                } else {
                                    break;
                                }
                            case 56:
                                if (zza((zzds<T>) t, i3, i)) {
                                    zzk3 = zzh(t, zzag2 & 1048575);
                                    zzfrVar.zzc(i3, zzk3);
                                    break;
                                } else {
                                    break;
                                }
                            case 57:
                                if (zza((zzds<T>) t, i3, i)) {
                                    zzj2 = zzg(t, zzag2 & 1048575);
                                    zzfrVar.zzf(i3, zzj2);
                                    break;
                                } else {
                                    break;
                                }
                            case 58:
                                if (zza((zzds<T>) t, i3, i)) {
                                    zzl = zzi(t, zzag2 & 1048575);
                                    zzfrVar.zzb(i3, zzl);
                                    break;
                                } else {
                                    break;
                                }
                            case 59:
                                if (!zza((zzds<T>) t, i3, i)) {
                                    break;
                                }
                                zza(i3, zzfd.zzo(t, zzag2 & 1048575), zzfrVar);
                                break;
                            case 60:
                                if (!zza((zzds<T>) t, i3, i)) {
                                    break;
                                }
                                zzfrVar.zza(i3, zzfd.zzo(t, zzag2 & 1048575), zzad(i));
                                break;
                            case 61:
                                if (!zza((zzds<T>) t, i3, i)) {
                                    break;
                                }
                                zzfrVar.zza(i3, (zzbb) zzfd.zzo(t, zzag2 & 1048575));
                                break;
                            case 62:
                                if (zza((zzds<T>) t, i3, i)) {
                                    zzj3 = zzg(t, zzag2 & 1048575);
                                    zzfrVar.zzd(i3, zzj3);
                                    break;
                                } else {
                                    break;
                                }
                            case 63:
                                if (zza((zzds<T>) t, i3, i)) {
                                    zzj4 = zzg(t, zzag2 & 1048575);
                                    zzfrVar.zzn(i3, zzj4);
                                    break;
                                } else {
                                    break;
                                }
                            case 64:
                                if (zza((zzds<T>) t, i3, i)) {
                                    zzj5 = zzg(t, zzag2 & 1048575);
                                    zzfrVar.zzm(i3, zzj5);
                                    break;
                                } else {
                                    break;
                                }
                            case VoIPService.CALL_MIN_LAYER /* 65 */:
                                if (zza((zzds<T>) t, i3, i)) {
                                    zzk4 = zzh(t, zzag2 & 1048575);
                                    zzfrVar.zzj(i3, zzk4);
                                    break;
                                } else {
                                    break;
                                }
                            case 66:
                                if (zza((zzds<T>) t, i3, i)) {
                                    zzj6 = zzg(t, zzag2 & 1048575);
                                    zzfrVar.zze(i3, zzj6);
                                    break;
                                } else {
                                    break;
                                }
                            case 67:
                                if (zza((zzds<T>) t, i3, i)) {
                                    zzk5 = zzh(t, zzag2 & 1048575);
                                    zzfrVar.zzb(i3, zzk5);
                                    break;
                                } else {
                                    break;
                                }
                            case 68:
                                if (!zza((zzds<T>) t, i3, i)) {
                                    break;
                                }
                                zzfrVar.zzb(i3, zzfd.zzo(t, zzag2 & 1048575), zzad(i));
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

    /* JADX WARN: Code restructure failed: missing block: B:37:0x0164, code lost:
        if (r0 == r15) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x01a3, code lost:
        r2 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0188, code lost:
        if (r0 == r15) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x01a1, code lost:
        if (r0 == r15) goto L38;
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
        int zzb;
        long j;
        Object zza;
        int i5;
        zzds<T> zzdsVar = this;
        T t2 = t;
        byte[] bArr2 = bArr;
        int i6 = i2;
        zzay zzayVar2 = zzayVar;
        if (!zzdsVar.zzmq) {
            zza((zzds<T>) t, bArr, i, i2, 0, zzayVar);
            return;
        }
        Unsafe unsafe2 = zzmh;
        int i7 = i;
        while (i7 < i6) {
            int i8 = i7 + 1;
            byte b2 = bArr2[i7];
            if (b2 < 0) {
                i3 = zzax.zza(b2, bArr2, i8, zzayVar2);
                b = zzayVar2.zzfd;
            } else {
                b = b2;
                i3 = i8;
            }
            int i9 = b >>> 3;
            int i10 = b & 7;
            int zzai = zzdsVar.zzai(i9);
            if (zzai >= 0) {
                int i11 = zzdsVar.zzmi[zzai + 1];
                int i12 = (267386880 & i11) >>> 20;
                long j2 = 1048575 & i11;
                if (i12 <= 17) {
                    boolean z = true;
                    switch (i12) {
                        case 0:
                            if (i10 != 1) {
                                break;
                            } else {
                                zzfd.zza(t2, j2, zzax.zze(bArr2, i3));
                                i7 = i3 + 8;
                                break;
                            }
                        case 1:
                            if (i10 != 5) {
                                break;
                            } else {
                                zzfd.zza((Object) t2, j2, zzax.zzf(bArr2, i3));
                                i7 = i3 + 4;
                                break;
                            }
                        case 2:
                        case 3:
                            if (i10 != 0) {
                                break;
                            } else {
                                zzb = zzax.zzb(bArr2, i3, zzayVar2);
                                j = zzayVar2.zzfe;
                                unsafe2.putLong(t, j2, j);
                                i7 = zzb;
                                break;
                            }
                        case 4:
                        case 11:
                            if (i10 != 0) {
                                break;
                            } else {
                                i7 = zzax.zza(bArr2, i3, zzayVar2);
                                i5 = zzayVar2.zzfd;
                                unsafe2.putInt(t2, j2, i5);
                                break;
                            }
                        case 5:
                        case 14:
                            if (i10 != 1) {
                                break;
                            } else {
                                unsafe2.putLong(t, j2, zzax.zzd(bArr2, i3));
                                i7 = i3 + 8;
                                break;
                            }
                        case 6:
                        case 13:
                            if (i10 != 5) {
                                break;
                            } else {
                                unsafe2.putInt(t2, j2, zzax.zzc(bArr2, i3));
                                i7 = i3 + 4;
                                break;
                            }
                        case 7:
                            if (i10 != 0) {
                                break;
                            } else {
                                i7 = zzax.zzb(bArr2, i3, zzayVar2);
                                if (zzayVar2.zzfe == 0) {
                                    z = false;
                                }
                                zzfd.zza(t2, j2, z);
                                break;
                            }
                        case 8:
                            if (i10 != 2) {
                                break;
                            } else {
                                i7 = (536870912 & i11) == 0 ? zzax.zzc(bArr2, i3, zzayVar2) : zzax.zzd(bArr2, i3, zzayVar2);
                                zza = zzayVar2.zzff;
                                unsafe2.putObject(t2, j2, zza);
                                break;
                            }
                        case 9:
                            if (i10 != 2) {
                                break;
                            } else {
                                i7 = zza(zzdsVar.zzad(zzai), bArr2, i3, i6, zzayVar2);
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
                            if (i10 != 2) {
                                break;
                            } else {
                                i7 = zzax.zze(bArr2, i3, zzayVar2);
                                zza = zzayVar2.zzff;
                                unsafe2.putObject(t2, j2, zza);
                                break;
                            }
                        case 12:
                            if (i10 != 0) {
                                break;
                            } else {
                                i7 = zzax.zza(bArr2, i3, zzayVar2);
                                i5 = zzayVar2.zzfd;
                                unsafe2.putInt(t2, j2, i5);
                                break;
                            }
                        case 15:
                            if (i10 != 0) {
                                break;
                            } else {
                                i7 = zzax.zza(bArr2, i3, zzayVar2);
                                i5 = zzbk.zzm(zzayVar2.zzfd);
                                unsafe2.putInt(t2, j2, i5);
                                break;
                            }
                        case 16:
                            if (i10 != 0) {
                                break;
                            } else {
                                zzb = zzax.zzb(bArr2, i3, zzayVar2);
                                j = zzbk.zza(zzayVar2.zzfe);
                                unsafe2.putLong(t, j2, j);
                                i7 = zzb;
                                break;
                            }
                    }
                } else if (i12 != 27) {
                    if (i12 <= 49) {
                        unsafe = unsafe2;
                        int i13 = i3;
                        i7 = zza((zzds<T>) t, bArr, i3, i2, b, i9, i10, zzai, i11, i12, j2, zzayVar);
                    } else {
                        unsafe = unsafe2;
                        i4 = i3;
                        if (i12 != 50) {
                            i7 = zza((zzds<T>) t, bArr, i4, i2, b, i9, i10, i11, i12, j2, zzai, zzayVar);
                        } else if (i10 == 2) {
                            i7 = zza(t, bArr, i4, i2, zzai, i9, j2, zzayVar);
                        }
                    }
                    zzdsVar = this;
                    t2 = t;
                    bArr2 = bArr;
                    i6 = i2;
                    zzayVar2 = zzayVar;
                    unsafe2 = unsafe;
                } else if (i10 == 2) {
                    zzcn zzcnVar = (zzcn) unsafe2.getObject(t2, j2);
                    if (!zzcnVar.zzu()) {
                        int size = zzcnVar.size();
                        zzcnVar = zzcnVar.zzi(size == 0 ? 10 : size << 1);
                        unsafe2.putObject(t2, j2, zzcnVar);
                    }
                    i7 = zza(zzdsVar.zzad(zzai), b, bArr, i3, i2, zzcnVar, zzayVar);
                }
                int i14 = i4;
                i7 = zza(b, bArr, i14, i2, t, zzayVar);
                zzdsVar = this;
                t2 = t;
                bArr2 = bArr;
                i6 = i2;
                zzayVar2 = zzayVar;
                unsafe2 = unsafe;
            }
            unsafe = unsafe2;
            i4 = i3;
            int i142 = i4;
            i7 = zza(b, bArr, i142, i2, t, zzayVar);
            zzdsVar = this;
            t2 = t;
            bArr2 = bArr;
            i6 = i2;
            zzayVar2 = zzayVar;
            unsafe2 = unsafe;
        }
        if (i7 != i6) {
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
                case R.styleable.MapAttrs_uiZoomControls /* 20 */:
                case 21:
                case R.styleable.MapAttrs_useViewLifecycle /* 22 */:
                case R.styleable.MapAttrs_zOrderOnTop /* 23 */:
                case 24:
                case AvailableCode.ERROR_ON_ACTIVITY_RESULT /* 25 */:
                case AvailableCode.ERROR_NO_ACTIVITY /* 26 */:
                case AvailableCode.USER_IGNORE_PREVIOUS_POPUP /* 27 */:
                case AvailableCode.APP_IS_BACKGROUND_OR_LOCKED /* 28 */:
                case AvailableCode.HMS_IS_SPOOF /* 29 */:
                case AvailableCode.USER_ALREADY_KNOWS_SERVICE_UNAVAILABLE /* 30 */:
                case AvailableCode.CURRENT_SHOWING_SERVICE_UNAVAILABLE /* 31 */:
                case ConnectionsManager.RequestFlagForceDownload /* 32 */:
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
                case VoIPService.CALL_MIN_LAYER /* 65 */:
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
    /* JADX WARN: Code restructure failed: missing block: B:102:0x0127, code lost:
        if (r19.zzmr != false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x0211, code lost:
        r3 = (com.google.android.gms.internal.clearcut.zzbn.zzr(r3) + com.google.android.gms.internal.clearcut.zzbn.zzt(r5)) + r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x020d, code lost:
        r2.putInt(r20, r14, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x0139, code lost:
        if (r19.zzmr != false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x014b, code lost:
        if (r19.zzmr != false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x015d, code lost:
        if (r19.zzmr != false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:124:0x016f, code lost:
        if (r19.zzmr != false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:129:0x0181, code lost:
        if (r19.zzmr != false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:134:0x0193, code lost:
        if (r19.zzmr != false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:139:0x01a5, code lost:
        if (r19.zzmr != false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:144:0x01b6, code lost:
        if (r19.zzmr != false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:149:0x01c7, code lost:
        if (r19.zzmr != false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:154:0x01d8, code lost:
        if (r19.zzmr != false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:159:0x01e9, code lost:
        if (r19.zzmr != false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:164:0x01fa, code lost:
        if (r19.zzmr != false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:169:0x020b, code lost:
        if (r19.zzmr != false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:218:0x0331, code lost:
        if ((r5 instanceof com.google.android.gms.internal.clearcut.zzbb) != false) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:265:0x0414, code lost:
        if (zza((com.google.android.gms.internal.clearcut.zzds<T>) r20, r15, r5) != false) goto L266;
     */
    /* JADX WARN: Code restructure failed: missing block: B:266:0x06b6, code lost:
        r4 = com.google.android.gms.internal.clearcut.zzbn.zzc(r15, (com.google.android.gms.internal.clearcut.zzdo) r2.getObject(r20, r10), zzad(r5));
     */
    /* JADX WARN: Code restructure failed: missing block: B:277:0x0434, code lost:
        if (zza((com.google.android.gms.internal.clearcut.zzds<T>) r20, r15, r5) != false) goto L278;
     */
    /* JADX WARN: Code restructure failed: missing block: B:278:0x06e3, code lost:
        r4 = com.google.android.gms.internal.clearcut.zzbn.zzh(r15, 0L);
     */
    /* JADX WARN: Code restructure failed: missing block: B:280:0x043c, code lost:
        if (zza((com.google.android.gms.internal.clearcut.zzds<T>) r20, r15, r5) != false) goto L281;
     */
    /* JADX WARN: Code restructure failed: missing block: B:281:0x06ee, code lost:
        r9 = com.google.android.gms.internal.clearcut.zzbn.zzk(r15, 0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x045c, code lost:
        if (zza((com.google.android.gms.internal.clearcut.zzds<T>) r20, r15, r5) != false) goto L293;
     */
    /* JADX WARN: Code restructure failed: missing block: B:293:0x0713, code lost:
        r4 = r2.getObject(r20, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:294:0x0717, code lost:
        r4 = com.google.android.gms.internal.clearcut.zzbn.zzc(r15, (com.google.android.gms.internal.clearcut.zzbb) r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:296:0x0464, code lost:
        if (zza((com.google.android.gms.internal.clearcut.zzds<T>) r20, r15, r5) != false) goto L297;
     */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x0722, code lost:
        r4 = com.google.android.gms.internal.clearcut.zzeh.zzc(r15, r2.getObject(r20, r10), zzad(r5));
     */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x0474, code lost:
        if ((r4 instanceof com.google.android.gms.internal.clearcut.zzbb) != false) goto L294;
     */
    /* JADX WARN: Code restructure failed: missing block: B:302:0x073d, code lost:
        r4 = com.google.android.gms.internal.clearcut.zzbn.zzb(r15, (java.lang.String) r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:304:0x047c, code lost:
        if (zza((com.google.android.gms.internal.clearcut.zzds<T>) r20, r15, r5) != false) goto L305;
     */
    /* JADX WARN: Code restructure failed: missing block: B:305:0x0749, code lost:
        r4 = com.google.android.gms.internal.clearcut.zzbn.zzc(r15, true);
     */
    /* JADX WARN: Code restructure failed: missing block: B:332:0x0514, code lost:
        if (r19.zzmr != false) goto L334;
     */
    /* JADX WARN: Code restructure failed: missing block: B:333:0x05fe, code lost:
        r9 = (com.google.android.gms.internal.clearcut.zzbn.zzr(r15) + com.google.android.gms.internal.clearcut.zzbn.zzt(r4)) + r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:334:0x05fa, code lost:
        r2.putInt(r20, r9, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:338:0x0526, code lost:
        if (r19.zzmr != false) goto L334;
     */
    /* JADX WARN: Code restructure failed: missing block: B:342:0x0538, code lost:
        if (r19.zzmr != false) goto L334;
     */
    /* JADX WARN: Code restructure failed: missing block: B:346:0x054a, code lost:
        if (r19.zzmr != false) goto L334;
     */
    /* JADX WARN: Code restructure failed: missing block: B:350:0x055c, code lost:
        if (r19.zzmr != false) goto L334;
     */
    /* JADX WARN: Code restructure failed: missing block: B:354:0x056e, code lost:
        if (r19.zzmr != false) goto L334;
     */
    /* JADX WARN: Code restructure failed: missing block: B:358:0x0580, code lost:
        if (r19.zzmr != false) goto L334;
     */
    /* JADX WARN: Code restructure failed: missing block: B:362:0x0592, code lost:
        if (r19.zzmr != false) goto L334;
     */
    /* JADX WARN: Code restructure failed: missing block: B:366:0x05a3, code lost:
        if (r19.zzmr != false) goto L334;
     */
    /* JADX WARN: Code restructure failed: missing block: B:370:0x05b4, code lost:
        if (r19.zzmr != false) goto L334;
     */
    /* JADX WARN: Code restructure failed: missing block: B:374:0x05c5, code lost:
        if (r19.zzmr != false) goto L334;
     */
    /* JADX WARN: Code restructure failed: missing block: B:378:0x05d6, code lost:
        if (r19.zzmr != false) goto L334;
     */
    /* JADX WARN: Code restructure failed: missing block: B:382:0x05e7, code lost:
        if (r19.zzmr != false) goto L334;
     */
    /* JADX WARN: Code restructure failed: missing block: B:386:0x05f8, code lost:
        if (r19.zzmr != false) goto L334;
     */
    /* JADX WARN: Code restructure failed: missing block: B:401:0x06b4, code lost:
        if ((r12 & r18) != 0) goto L266;
     */
    /* JADX WARN: Code restructure failed: missing block: B:409:0x06e1, code lost:
        if ((r12 & r18) != 0) goto L278;
     */
    /* JADX WARN: Code restructure failed: missing block: B:411:0x06ec, code lost:
        if ((r12 & r18) != 0) goto L281;
     */
    /* JADX WARN: Code restructure failed: missing block: B:419:0x0711, code lost:
        if ((r12 & r18) != 0) goto L293;
     */
    /* JADX WARN: Code restructure failed: missing block: B:421:0x0720, code lost:
        if ((r12 & r18) != 0) goto L297;
     */
    /* JADX WARN: Code restructure failed: missing block: B:425:0x073a, code lost:
        if ((r4 instanceof com.google.android.gms.internal.clearcut.zzbb) != false) goto L294;
     */
    /* JADX WARN: Code restructure failed: missing block: B:427:0x0747, code lost:
        if ((r12 & r18) != 0) goto L305;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00ab, code lost:
        if ((r5 instanceof com.google.android.gms.internal.clearcut.zzbb) != false) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0334, code lost:
        r3 = com.google.android.gms.internal.clearcut.zzbn.zzb(r3, (java.lang.String) r5);
     */
    @Override // com.google.android.gms.internal.clearcut.zzef
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final int zzm(T t) {
        int i;
        int i2;
        long j;
        int zzd;
        Object object;
        int i3;
        int i4;
        int i5;
        long j2;
        int zzw;
        int zzi;
        int zzr;
        long zzk;
        long zzk2;
        int zzj;
        Object zzo;
        int zzj2;
        int zzj3;
        int zzj4;
        long zzk3;
        int zzw2;
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
                        i10 = unsafe.getInt(t, i14);
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
                            zzd = zzbn.zzd(i11, unsafe.getLong(t, j3));
                            i9 += zzd;
                        }
                        break;
                    case 3:
                        j = 0;
                        if ((i10 & i2) != 0) {
                            zzd = zzbn.zze(i11, unsafe.getLong(t, j3));
                            i9 += zzd;
                        }
                        break;
                    case 4:
                        j = 0;
                        if ((i10 & i2) != 0) {
                            zzd = zzbn.zzg(i11, unsafe.getInt(t, j3));
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
                            object = unsafe.getObject(t, j3);
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
                            i3 = unsafe.getInt(t, j3);
                            zzw = zzbn.zzh(i11, i3);
                            i9 += zzw;
                        }
                        j = 0;
                        break;
                    case 12:
                        if ((i10 & i2) != 0) {
                            i4 = unsafe.getInt(t, j3);
                            zzw = zzbn.zzl(i11, i4);
                            i9 += zzw;
                        }
                        j = 0;
                        break;
                    case 13:
                        break;
                    case 14:
                        break;
                    case 15:
                        if ((i10 & i2) != 0) {
                            i5 = unsafe.getInt(t, j3);
                            zzw = zzbn.zzi(i11, i5);
                            i9 += zzw;
                        }
                        j = 0;
                        break;
                    case 16:
                        if ((i10 & i2) != 0) {
                            j2 = unsafe.getLong(t, j3);
                            zzw = zzbn.zzf(i11, j2);
                            i9 += zzw;
                        }
                        j = 0;
                        break;
                    case 17:
                        break;
                    case 18:
                    case R.styleable.MapAttrs_zOrderOnTop /* 23 */:
                    case ConnectionsManager.RequestFlagForceDownload /* 32 */:
                        zzw = zzeh.zzw(i11, (List) unsafe.getObject(t, j3), false);
                        i9 += zzw;
                        j = 0;
                        break;
                    case 19:
                    case 24:
                    case AvailableCode.CURRENT_SHOWING_SERVICE_UNAVAILABLE /* 31 */:
                        zzw = zzeh.zzv(i11, (List) unsafe.getObject(t, j3), false);
                        i9 += zzw;
                        j = 0;
                        break;
                    case R.styleable.MapAttrs_uiZoomControls /* 20 */:
                        zzw = zzeh.zzo(i11, (List) unsafe.getObject(t, j3), false);
                        i9 += zzw;
                        j = 0;
                        break;
                    case 21:
                        zzw = zzeh.zzp(i11, (List) unsafe.getObject(t, j3), false);
                        i9 += zzw;
                        j = 0;
                        break;
                    case R.styleable.MapAttrs_useViewLifecycle /* 22 */:
                        zzw = zzeh.zzs(i11, (List) unsafe.getObject(t, j3), false);
                        i9 += zzw;
                        j = 0;
                        break;
                    case AvailableCode.ERROR_ON_ACTIVITY_RESULT /* 25 */:
                        zzw = zzeh.zzx(i11, (List) unsafe.getObject(t, j3), false);
                        i9 += zzw;
                        j = 0;
                        break;
                    case AvailableCode.ERROR_NO_ACTIVITY /* 26 */:
                        zzw = zzeh.zzc(i11, (List) unsafe.getObject(t, j3));
                        i9 += zzw;
                        j = 0;
                        break;
                    case AvailableCode.USER_IGNORE_PREVIOUS_POPUP /* 27 */:
                        zzw = zzeh.zzc(i11, (List<?>) unsafe.getObject(t, j3), zzad(i8));
                        i9 += zzw;
                        j = 0;
                        break;
                    case AvailableCode.APP_IS_BACKGROUND_OR_LOCKED /* 28 */:
                        zzw = zzeh.zzd(i11, (List) unsafe.getObject(t, j3));
                        i9 += zzw;
                        j = 0;
                        break;
                    case AvailableCode.HMS_IS_SPOOF /* 29 */:
                        zzw = zzeh.zzt(i11, (List) unsafe.getObject(t, j3), false);
                        i9 += zzw;
                        j = 0;
                        break;
                    case AvailableCode.USER_ALREADY_KNOWS_SERVICE_UNAVAILABLE /* 30 */:
                        zzw = zzeh.zzr(i11, (List) unsafe.getObject(t, j3), false);
                        i9 += zzw;
                        j = 0;
                        break;
                    case 33:
                        zzw = zzeh.zzu(i11, (List) unsafe.getObject(t, j3), false);
                        i9 += zzw;
                        j = 0;
                        break;
                    case 34:
                        zzw = zzeh.zzq(i11, (List) unsafe.getObject(t, j3), false);
                        i9 += zzw;
                        j = 0;
                        break;
                    case 35:
                        zzi = zzeh.zzi((List) unsafe.getObject(t, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 36:
                        zzi = zzeh.zzh((List) unsafe.getObject(t, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 37:
                        zzi = zzeh.zza((List) unsafe.getObject(t, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 38:
                        zzi = zzeh.zzb((List) unsafe.getObject(t, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 39:
                        zzi = zzeh.zze((List) unsafe.getObject(t, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 40:
                        zzi = zzeh.zzi((List) unsafe.getObject(t, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 41:
                        zzi = zzeh.zzh((List) unsafe.getObject(t, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 42:
                        zzi = zzeh.zzj((List) unsafe.getObject(t, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 43:
                        zzi = zzeh.zzf((List) unsafe.getObject(t, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 44:
                        zzi = zzeh.zzd((List) unsafe.getObject(t, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 45:
                        zzi = zzeh.zzh((List) unsafe.getObject(t, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 46:
                        zzi = zzeh.zzi((List) unsafe.getObject(t, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 47:
                        zzi = zzeh.zzg((List) unsafe.getObject(t, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 48:
                        zzi = zzeh.zzc((List) unsafe.getObject(t, j3));
                        if (zzi > 0) {
                            break;
                        }
                        j = 0;
                        break;
                    case 49:
                        zzw = zzeh.zzd(i11, (List) unsafe.getObject(t, j3), zzad(i8));
                        i9 += zzw;
                        j = 0;
                        break;
                    case 50:
                        zzw = this.zzmz.zzb(i11, unsafe.getObject(t, j3), zzae(i8));
                        i9 += zzw;
                        j = 0;
                        break;
                    case 51:
                        if (zza((zzds<T>) t, i11, i8)) {
                            zzw = zzbn.zzb(i11, 0.0d);
                            i9 += zzw;
                        }
                        j = 0;
                        break;
                    case 52:
                        if (zza((zzds<T>) t, i11, i8)) {
                            zzr = zzbn.zzb(i11, 0.0f);
                            i9 += zzr;
                        }
                        j = 0;
                        break;
                    case 53:
                        if (zza((zzds<T>) t, i11, i8)) {
                            zzw = zzbn.zzd(i11, zzh(t, j3));
                            i9 += zzw;
                        }
                        j = 0;
                        break;
                    case 54:
                        if (zza((zzds<T>) t, i11, i8)) {
                            zzw = zzbn.zze(i11, zzh(t, j3));
                            i9 += zzw;
                        }
                        j = 0;
                        break;
                    case 55:
                        if (zza((zzds<T>) t, i11, i8)) {
                            zzw = zzbn.zzg(i11, zzg(t, j3));
                            i9 += zzw;
                        }
                        j = 0;
                        break;
                    case 56:
                        if (zza((zzds<T>) t, i11, i8)) {
                            zzw = zzbn.zzg(i11, 0L);
                            i9 += zzw;
                        }
                        j = 0;
                        break;
                    case 57:
                        if (zza((zzds<T>) t, i11, i8)) {
                            zzr = zzbn.zzj(i11, 0);
                            i9 += zzr;
                        }
                        j = 0;
                        break;
                    case 58:
                        break;
                    case 59:
                        if (zza((zzds<T>) t, i11, i8)) {
                            object = unsafe.getObject(t, j3);
                            break;
                        }
                        j = 0;
                        break;
                    case 60:
                        break;
                    case 61:
                        break;
                    case 62:
                        if (zza((zzds<T>) t, i11, i8)) {
                            i3 = zzg(t, j3);
                            zzw = zzbn.zzh(i11, i3);
                            i9 += zzw;
                        }
                        j = 0;
                        break;
                    case 63:
                        if (zza((zzds<T>) t, i11, i8)) {
                            i4 = zzg(t, j3);
                            zzw = zzbn.zzl(i11, i4);
                            i9 += zzw;
                        }
                        j = 0;
                        break;
                    case 64:
                        break;
                    case VoIPService.CALL_MIN_LAYER /* 65 */:
                        break;
                    case 66:
                        if (zza((zzds<T>) t, i11, i8)) {
                            i5 = zzg(t, j3);
                            zzw = zzbn.zzi(i11, i5);
                            i9 += zzw;
                        }
                        j = 0;
                        break;
                    case 67:
                        if (zza((zzds<T>) t, i11, i8)) {
                            j2 = zzh(t, j3);
                            zzw = zzbn.zzf(i11, j2);
                            i9 += zzw;
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
            int zza = i9 + zza(this.zzmx, t);
            return this.zzmo ? zza + this.zzmy.zza(t).zzas() : zza;
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
                    if (!zza((zzds<T>) t, i15)) {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                    zzw2 = zzbn.zzb(i18, 0.0d);
                    break;
                case 1:
                    if (!zza((zzds<T>) t, i15)) {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                    zzw2 = zzbn.zzb(i18, 0.0f);
                    break;
                case 2:
                    if (zza((zzds<T>) t, i15)) {
                        zzk = zzfd.zzk(t, j4);
                        zzw2 = zzbn.zzd(i18, zzk);
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 3:
                    if (zza((zzds<T>) t, i15)) {
                        zzk2 = zzfd.zzk(t, j4);
                        zzw2 = zzbn.zze(i18, zzk2);
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 4:
                    if (zza((zzds<T>) t, i15)) {
                        zzj = zzfd.zzj(t, j4);
                        zzw2 = zzbn.zzg(i18, zzj);
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 5:
                    if (!zza((zzds<T>) t, i15)) {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                    zzw2 = zzbn.zzg(i18, 0L);
                    break;
                case 6:
                    if (!zza((zzds<T>) t, i15)) {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                    zzw2 = zzbn.zzj(i18, 0);
                    break;
                case 7:
                    if (!zza((zzds<T>) t, i15)) {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                    zzw2 = zzbn.zzc(i18, true);
                    break;
                case 8:
                    if (zza((zzds<T>) t, i15)) {
                        zzo = zzfd.zzo(t, j4);
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 9:
                    if (!zza((zzds<T>) t, i15)) {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                    zzw2 = zzeh.zzc(i18, zzfd.zzo(t, j4), zzad(i15));
                    break;
                case 10:
                    if (!zza((zzds<T>) t, i15)) {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                    zzo = zzfd.zzo(t, j4);
                    zzw2 = zzbn.zzc(i18, (zzbb) zzo);
                    break;
                case 11:
                    if (zza((zzds<T>) t, i15)) {
                        zzj2 = zzfd.zzj(t, j4);
                        zzw2 = zzbn.zzh(i18, zzj2);
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 12:
                    if (zza((zzds<T>) t, i15)) {
                        zzj3 = zzfd.zzj(t, j4);
                        zzw2 = zzbn.zzl(i18, zzj3);
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 13:
                    if (!zza((zzds<T>) t, i15)) {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                    zzw2 = zzbn.zzk(i18, 0);
                    break;
                case 14:
                    if (!zza((zzds<T>) t, i15)) {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                    zzw2 = zzbn.zzh(i18, 0L);
                    break;
                case 15:
                    if (zza((zzds<T>) t, i15)) {
                        zzj4 = zzfd.zzj(t, j4);
                        zzw2 = zzbn.zzi(i18, zzj4);
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 16:
                    if (zza((zzds<T>) t, i15)) {
                        zzk3 = zzfd.zzk(t, j4);
                        zzw2 = zzbn.zzf(i18, zzk3);
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 17:
                    if (!zza((zzds<T>) t, i15)) {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                    zzw2 = zzbn.zzc(i18, (zzdo) zzfd.zzo(t, j4), zzad(i15));
                    break;
                case 18:
                case R.styleable.MapAttrs_zOrderOnTop /* 23 */:
                case ConnectionsManager.RequestFlagForceDownload /* 32 */:
                    zzw2 = zzeh.zzw(i18, zzd(t, j4), false);
                    break;
                case 19:
                case 24:
                case AvailableCode.CURRENT_SHOWING_SERVICE_UNAVAILABLE /* 31 */:
                    zzw2 = zzeh.zzv(i18, zzd(t, j4), false);
                    break;
                case R.styleable.MapAttrs_uiZoomControls /* 20 */:
                    zzw2 = zzeh.zzo(i18, zzd(t, j4), false);
                    break;
                case 21:
                    zzw2 = zzeh.zzp(i18, zzd(t, j4), false);
                    break;
                case R.styleable.MapAttrs_useViewLifecycle /* 22 */:
                    zzw2 = zzeh.zzs(i18, zzd(t, j4), false);
                    break;
                case AvailableCode.ERROR_ON_ACTIVITY_RESULT /* 25 */:
                    zzw2 = zzeh.zzx(i18, zzd(t, j4), false);
                    break;
                case AvailableCode.ERROR_NO_ACTIVITY /* 26 */:
                    zzw2 = zzeh.zzc(i18, zzd(t, j4));
                    break;
                case AvailableCode.USER_IGNORE_PREVIOUS_POPUP /* 27 */:
                    zzw2 = zzeh.zzc(i18, (List<?>) zzd(t, j4), zzad(i15));
                    break;
                case AvailableCode.APP_IS_BACKGROUND_OR_LOCKED /* 28 */:
                    zzw2 = zzeh.zzd(i18, zzd(t, j4));
                    break;
                case AvailableCode.HMS_IS_SPOOF /* 29 */:
                    zzw2 = zzeh.zzt(i18, zzd(t, j4), false);
                    break;
                case AvailableCode.USER_ALREADY_KNOWS_SERVICE_UNAVAILABLE /* 30 */:
                    zzw2 = zzeh.zzr(i18, zzd(t, j4), false);
                    break;
                case 33:
                    zzw2 = zzeh.zzu(i18, zzd(t, j4), false);
                    break;
                case 34:
                    zzw2 = zzeh.zzq(i18, zzd(t, j4), false);
                    break;
                case 35:
                    zzi2 = zzeh.zzi((List) unsafe2.getObject(t, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 36:
                    zzi2 = zzeh.zzh((List) unsafe2.getObject(t, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 37:
                    zzi2 = zzeh.zza((List) unsafe2.getObject(t, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 38:
                    zzi2 = zzeh.zzb((List) unsafe2.getObject(t, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 39:
                    zzi2 = zzeh.zze((List) unsafe2.getObject(t, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 40:
                    zzi2 = zzeh.zzi((List) unsafe2.getObject(t, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 41:
                    zzi2 = zzeh.zzh((List) unsafe2.getObject(t, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 42:
                    zzi2 = zzeh.zzj((List) unsafe2.getObject(t, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 43:
                    zzi2 = zzeh.zzf((List) unsafe2.getObject(t, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 44:
                    zzi2 = zzeh.zzd((List) unsafe2.getObject(t, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 45:
                    zzi2 = zzeh.zzh((List) unsafe2.getObject(t, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 46:
                    zzi2 = zzeh.zzi((List) unsafe2.getObject(t, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 47:
                    zzi2 = zzeh.zzg((List) unsafe2.getObject(t, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 48:
                    zzi2 = zzeh.zzc((List) unsafe2.getObject(t, j4));
                    if (zzi2 > 0) {
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 49:
                    zzw2 = zzeh.zzd(i18, zzd(t, j4), zzad(i15));
                    break;
                case 50:
                    zzw2 = this.zzmz.zzb(i18, zzfd.zzo(t, j4), zzae(i15));
                    break;
                case 51:
                    if (!zza((zzds<T>) t, i18, i15)) {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                    zzw2 = zzbn.zzb(i18, 0.0d);
                    break;
                case 52:
                    if (!zza((zzds<T>) t, i18, i15)) {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                    zzw2 = zzbn.zzb(i18, 0.0f);
                    break;
                case 53:
                    if (zza((zzds<T>) t, i18, i15)) {
                        zzk = zzh(t, j4);
                        zzw2 = zzbn.zzd(i18, zzk);
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 54:
                    if (zza((zzds<T>) t, i18, i15)) {
                        zzk2 = zzh(t, j4);
                        zzw2 = zzbn.zze(i18, zzk2);
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 55:
                    if (zza((zzds<T>) t, i18, i15)) {
                        zzj = zzg(t, j4);
                        zzw2 = zzbn.zzg(i18, zzj);
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 56:
                    if (!zza((zzds<T>) t, i18, i15)) {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                    zzw2 = zzbn.zzg(i18, 0L);
                    break;
                case 57:
                    if (!zza((zzds<T>) t, i18, i15)) {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                    zzw2 = zzbn.zzj(i18, 0);
                    break;
                case 58:
                    if (!zza((zzds<T>) t, i18, i15)) {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                    zzw2 = zzbn.zzc(i18, true);
                    break;
                case 59:
                    if (zza((zzds<T>) t, i18, i15)) {
                        zzo = zzfd.zzo(t, j4);
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 60:
                    if (!zza((zzds<T>) t, i18, i15)) {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                    zzw2 = zzeh.zzc(i18, zzfd.zzo(t, j4), zzad(i15));
                    break;
                case 61:
                    if (!zza((zzds<T>) t, i18, i15)) {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                    zzo = zzfd.zzo(t, j4);
                    zzw2 = zzbn.zzc(i18, (zzbb) zzo);
                    break;
                case 62:
                    if (zza((zzds<T>) t, i18, i15)) {
                        zzj2 = zzg(t, j4);
                        zzw2 = zzbn.zzh(i18, zzj2);
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 63:
                    if (zza((zzds<T>) t, i18, i15)) {
                        zzj3 = zzg(t, j4);
                        zzw2 = zzbn.zzl(i18, zzj3);
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 64:
                    if (!zza((zzds<T>) t, i18, i15)) {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                    zzw2 = zzbn.zzk(i18, 0);
                    break;
                case VoIPService.CALL_MIN_LAYER /* 65 */:
                    if (!zza((zzds<T>) t, i18, i15)) {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                    zzw2 = zzbn.zzh(i18, 0L);
                    break;
                case 66:
                    if (zza((zzds<T>) t, i18, i15)) {
                        zzj4 = zzg(t, j4);
                        zzw2 = zzbn.zzi(i18, zzj4);
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 67:
                    if (zza((zzds<T>) t, i18, i15)) {
                        zzk3 = zzh(t, j4);
                        zzw2 = zzbn.zzf(i18, zzk3);
                        break;
                    } else {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                case 68:
                    if (!zza((zzds<T>) t, i18, i15)) {
                        continue;
                        i15 += 4;
                        i6 = 267386880;
                    }
                    zzw2 = zzbn.zzc(i18, (zzdo) zzfd.zzo(t, j4), zzad(i15));
                    break;
                default:
                    i15 += 4;
                    i6 = 267386880;
            }
            i16 += zzw2;
            i15 += 4;
            i6 = 267386880;
        }
        return i16 + zza(this.zzmx, t);
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
