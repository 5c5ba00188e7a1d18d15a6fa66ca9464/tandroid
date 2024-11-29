package com.google.android.gms.internal.vision;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.telegram.messenger.CharacterCompat;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.NotificationCenter;
import sun.misc.Unsafe;

/* loaded from: classes.dex */
final class zzko implements zzlc {
    private static final int[] zza = new int[0];
    private static final Unsafe zzb = zzma.zzc();
    private final int[] zzc;
    private final Object[] zzd;
    private final int zze;
    private final int zzf;
    private final zzkk zzg;
    private final boolean zzh;
    private final boolean zzi;
    private final boolean zzj;
    private final boolean zzk;
    private final int[] zzl;
    private final int zzm;
    private final int zzn;
    private final zzks zzo;
    private final zzju zzp;
    private final zzlu zzq;
    private final zziq zzr;
    private final zzkh zzs;

    private zzko(int[] iArr, Object[] objArr, int i, int i2, zzkk zzkkVar, boolean z, boolean z2, int[] iArr2, int i3, int i4, zzks zzksVar, zzju zzjuVar, zzlu zzluVar, zziq zziqVar, zzkh zzkhVar) {
        this.zzc = iArr;
        this.zzd = objArr;
        this.zze = i;
        this.zzf = i2;
        this.zzi = zzkkVar instanceof zzjb;
        this.zzj = z;
        this.zzh = zziqVar != null && zziqVar.zza(zzkkVar);
        this.zzk = false;
        this.zzl = iArr2;
        this.zzm = i3;
        this.zzn = i4;
        this.zzo = zzksVar;
        this.zzp = zzjuVar;
        this.zzq = zzluVar;
        this.zzr = zziqVar;
        this.zzg = zzkkVar;
        this.zzs = zzkhVar;
    }

    private final int zza(int i, int i2) {
        if (i < this.zze || i > this.zzf) {
            return -1;
        }
        return zzb(i, i2);
    }

    private static int zza(zzlu zzluVar, Object obj) {
        return zzluVar.zzf(zzluVar.zzb(obj));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private final int zza(Object obj, byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6, int i7, long j, int i8, zzhn zzhnVar) {
        Object valueOf;
        Object valueOf2;
        int zzb2;
        long j2;
        int i9;
        Object valueOf3;
        Unsafe unsafe = zzb;
        long j3 = this.zzc[i8 + 2] & 1048575;
        switch (i7) {
            case 51:
                if (i5 == 1) {
                    valueOf = Double.valueOf(zzhl.zzc(bArr, i));
                    unsafe.putObject(obj, j, valueOf);
                    zzb2 = i + 8;
                    unsafe.putInt(obj, j3, i4);
                    return zzb2;
                }
                return i;
            case 52:
                if (i5 == 5) {
                    valueOf2 = Float.valueOf(zzhl.zzd(bArr, i));
                    unsafe.putObject(obj, j, valueOf2);
                    zzb2 = i + 4;
                    unsafe.putInt(obj, j3, i4);
                    return zzb2;
                }
                return i;
            case 53:
            case 54:
                if (i5 == 0) {
                    zzb2 = zzhl.zzb(bArr, i, zzhnVar);
                    j2 = zzhnVar.zzb;
                    valueOf3 = Long.valueOf(j2);
                    unsafe.putObject(obj, j, valueOf3);
                    unsafe.putInt(obj, j3, i4);
                    return zzb2;
                }
                return i;
            case 55:
            case 62:
                if (i5 == 0) {
                    zzb2 = zzhl.zza(bArr, i, zzhnVar);
                    i9 = zzhnVar.zza;
                    valueOf3 = Integer.valueOf(i9);
                    unsafe.putObject(obj, j, valueOf3);
                    unsafe.putInt(obj, j3, i4);
                    return zzb2;
                }
                return i;
            case 56:
            case 65:
                if (i5 == 1) {
                    valueOf = Long.valueOf(zzhl.zzb(bArr, i));
                    unsafe.putObject(obj, j, valueOf);
                    zzb2 = i + 8;
                    unsafe.putInt(obj, j3, i4);
                    return zzb2;
                }
                return i;
            case 57:
            case 64:
                if (i5 == 5) {
                    valueOf2 = Integer.valueOf(zzhl.zza(bArr, i));
                    unsafe.putObject(obj, j, valueOf2);
                    zzb2 = i + 4;
                    unsafe.putInt(obj, j3, i4);
                    return zzb2;
                }
                return i;
            case 58:
                if (i5 == 0) {
                    zzb2 = zzhl.zzb(bArr, i, zzhnVar);
                    valueOf3 = Boolean.valueOf(zzhnVar.zzb != 0);
                    unsafe.putObject(obj, j, valueOf3);
                    unsafe.putInt(obj, j3, i4);
                    return zzb2;
                }
                return i;
            case 59:
                if (i5 == 2) {
                    zzb2 = zzhl.zza(bArr, i, zzhnVar);
                    int i10 = zzhnVar.zza;
                    if (i10 == 0) {
                        valueOf3 = "";
                        unsafe.putObject(obj, j, valueOf3);
                        unsafe.putInt(obj, j3, i4);
                        return zzb2;
                    }
                    if ((i6 & 536870912) != 0 && !zzmd.zza(bArr, zzb2, zzb2 + i10)) {
                        throw zzjk.zzh();
                    }
                    unsafe.putObject(obj, j, new String(bArr, zzb2, i10, zzjf.zza));
                    zzb2 += i10;
                    unsafe.putInt(obj, j3, i4);
                    return zzb2;
                }
                return i;
            case 60:
                if (i5 == 2) {
                    zzb2 = zzhl.zza(zza(i8), bArr, i, i2, zzhnVar);
                    Object object = unsafe.getInt(obj, j3) == i4 ? unsafe.getObject(obj, j) : null;
                    valueOf3 = zzhnVar.zzc;
                    if (object != null) {
                        valueOf3 = zzjf.zza(object, valueOf3);
                    }
                    unsafe.putObject(obj, j, valueOf3);
                    unsafe.putInt(obj, j3, i4);
                    return zzb2;
                }
                return i;
            case 61:
                if (i5 == 2) {
                    zzb2 = zzhl.zze(bArr, i, zzhnVar);
                    valueOf3 = zzhnVar.zzc;
                    unsafe.putObject(obj, j, valueOf3);
                    unsafe.putInt(obj, j3, i4);
                    return zzb2;
                }
                return i;
            case 63:
                if (i5 == 0) {
                    int zza2 = zzhl.zza(bArr, i, zzhnVar);
                    int i11 = zzhnVar.zza;
                    zzjg zzc = zzc(i8);
                    if (zzc != null && !zzc.zza(i11)) {
                        zze(obj).zza(i3, Long.valueOf(i11));
                        return zza2;
                    }
                    unsafe.putObject(obj, j, Integer.valueOf(i11));
                    zzb2 = zza2;
                    unsafe.putInt(obj, j3, i4);
                    return zzb2;
                }
                return i;
            case 66:
                if (i5 == 0) {
                    zzb2 = zzhl.zza(bArr, i, zzhnVar);
                    i9 = zzif.zze(zzhnVar.zza);
                    valueOf3 = Integer.valueOf(i9);
                    unsafe.putObject(obj, j, valueOf3);
                    unsafe.putInt(obj, j3, i4);
                    return zzb2;
                }
                return i;
            case 67:
                if (i5 == 0) {
                    zzb2 = zzhl.zzb(bArr, i, zzhnVar);
                    j2 = zzif.zza(zzhnVar.zzb);
                    valueOf3 = Long.valueOf(j2);
                    unsafe.putObject(obj, j, valueOf3);
                    unsafe.putInt(obj, j3, i4);
                    return zzb2;
                }
                return i;
            case 68:
                if (i5 == 3) {
                    zzb2 = zzhl.zza(zza(i8), bArr, i, i2, (i3 & (-8)) | 4, zzhnVar);
                    Object object2 = unsafe.getInt(obj, j3) == i4 ? unsafe.getObject(obj, j) : null;
                    valueOf3 = zzhnVar.zzc;
                    if (object2 != null) {
                        valueOf3 = zzjf.zza(object2, valueOf3);
                    }
                    unsafe.putObject(obj, j, valueOf3);
                    unsafe.putInt(obj, j3, i4);
                    return zzb2;
                }
                return i;
            default:
                return i;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:70:0x012e, code lost:
    
        if (r4 == 0) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0130, code lost:
    
        r12.add(com.google.android.gms.internal.vision.zzht.zza);
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x013e, code lost:
    
        if (r1 >= r19) goto L233;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0140, code lost:
    
        r4 = com.google.android.gms.internal.vision.zzhl.zza(r17, r1, r29);
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0146, code lost:
    
        if (r20 != r29.zza) goto L232;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0148, code lost:
    
        r1 = com.google.android.gms.internal.vision.zzhl.zza(r17, r4, r29);
        r4 = r29.zza;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x014e, code lost:
    
        if (r4 < 0) goto L234;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0152, code lost:
    
        if (r4 > (r17.length - r1)) goto L230;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x0154, code lost:
    
        if (r4 != 0) goto L68;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x0136, code lost:
    
        r12.add(com.google.android.gms.internal.vision.zzht.zza(r17, r1, r4));
        r1 = r1 + r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x015b, code lost:
    
        throw com.google.android.gms.internal.vision.zzjk.zza();
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x0160, code lost:
    
        throw com.google.android.gms.internal.vision.zzjk.zzb();
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:?, code lost:
    
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:?, code lost:
    
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x013e, code lost:
    
        r12.add(com.google.android.gms.internal.vision.zzht.zza(r17, r1, r4));
        r1 = r1 + r4;
     */
    /* JADX WARN: Removed duplicated region for block: B:110:0x01af  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x01f3  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:110:0x0203 -> B:104:0x01da). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:69:0x0154 -> B:61:0x0130). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:93:0x01bf -> B:87:0x019e). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final int zza(Object obj, byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6, long j, int i7, long j2, zzhn zzhnVar) {
        int zza2;
        int zza3;
        int i8 = i;
        Unsafe unsafe = zzb;
        zzjl zzjlVar = (zzjl) unsafe.getObject(obj, j2);
        if (!zzjlVar.zza()) {
            int size = zzjlVar.size();
            zzjlVar = zzjlVar.zza(size == 0 ? 10 : size << 1);
            unsafe.putObject(obj, j2, zzjlVar);
        }
        switch (i7) {
            case 18:
            case 35:
                if (i5 == 2) {
                    int zza4 = zzhl.zza(bArr, i8, zzhnVar);
                    int i9 = zzhnVar.zza + zza4;
                    if (zza4 < i9) {
                        zzhl.zzc(bArr, zza4);
                        throw null;
                    }
                    if (zza4 == i9) {
                        return zza4;
                    }
                    throw zzjk.zza();
                }
                if (i5 == 1) {
                    zzhl.zzc(bArr, i);
                    throw null;
                }
                break;
            case 19:
            case 36:
                if (i5 == 2) {
                    int zza5 = zzhl.zza(bArr, i8, zzhnVar);
                    int i10 = zzhnVar.zza + zza5;
                    if (zza5 < i10) {
                        zzhl.zzd(bArr, zza5);
                        throw null;
                    }
                    if (zza5 == i10) {
                        return zza5;
                    }
                    throw zzjk.zza();
                }
                if (i5 == 5) {
                    zzhl.zzd(bArr, i);
                    throw null;
                }
                break;
            case 20:
            case 21:
            case 37:
            case 38:
                if (i5 == 2) {
                    int zza6 = zzhl.zza(bArr, i8, zzhnVar);
                    int i11 = zzhnVar.zza + zza6;
                    if (zza6 < i11) {
                        zzhl.zzb(bArr, zza6, zzhnVar);
                        throw null;
                    }
                    if (zza6 == i11) {
                        return zza6;
                    }
                    throw zzjk.zza();
                }
                if (i5 == 0) {
                    zzhl.zzb(bArr, i8, zzhnVar);
                    long j3 = zzhnVar.zzb;
                    throw null;
                }
                break;
            case 22:
            case 29:
            case 39:
            case 43:
                if (i5 == 2) {
                    return zzhl.zza(bArr, i8, zzjlVar, zzhnVar);
                }
                if (i5 == 0) {
                    return zzhl.zza(i3, bArr, i, i2, zzjlVar, zzhnVar);
                }
                break;
            case 23:
            case 32:
            case 40:
            case 46:
                if (i5 == 2) {
                    int zza7 = zzhl.zza(bArr, i8, zzhnVar);
                    int i12 = zzhnVar.zza + zza7;
                    if (zza7 < i12) {
                        zzhl.zzb(bArr, zza7);
                        throw null;
                    }
                    if (zza7 == i12) {
                        return zza7;
                    }
                    throw zzjk.zza();
                }
                if (i5 == 1) {
                    zzhl.zzb(bArr, i);
                    throw null;
                }
                break;
            case 24:
            case 31:
            case 41:
            case 45:
                if (i5 == 2) {
                    zzjd zzjdVar = (zzjd) zzjlVar;
                    int zza8 = zzhl.zza(bArr, i8, zzhnVar);
                    int i13 = zzhnVar.zza + zza8;
                    while (zza8 < i13) {
                        zzjdVar.zzc(zzhl.zza(bArr, zza8));
                        zza8 += 4;
                    }
                    if (zza8 == i13) {
                        return zza8;
                    }
                    throw zzjk.zza();
                }
                if (i5 == 5) {
                    zzjd zzjdVar2 = (zzjd) zzjlVar;
                    zzjdVar2.zzc(zzhl.zza(bArr, i));
                    while (true) {
                        int i14 = i8 + 4;
                        if (i14 >= i2) {
                            return i14;
                        }
                        i8 = zzhl.zza(bArr, i14, zzhnVar);
                        if (i3 != zzhnVar.zza) {
                            return i14;
                        }
                        zzjdVar2.zzc(zzhl.zza(bArr, i8));
                    }
                }
                break;
            case 25:
            case 42:
                if (i5 == 2) {
                    int zza9 = zzhl.zza(bArr, i8, zzhnVar);
                    int i15 = zzhnVar.zza + zza9;
                    if (zza9 < i15) {
                        zzhl.zzb(bArr, zza9, zzhnVar);
                        throw null;
                    }
                    if (zza9 == i15) {
                        return zza9;
                    }
                    throw zzjk.zza();
                }
                if (i5 == 0) {
                    zzhl.zzb(bArr, i8, zzhnVar);
                    long j4 = zzhnVar.zzb;
                    throw null;
                }
                break;
            case 26:
                if (i5 == 2) {
                    long j5 = j & 536870912;
                    i8 = zzhl.zza(bArr, i8, zzhnVar);
                    if (j5 == 0) {
                        int i16 = zzhnVar.zza;
                        if (i16 < 0) {
                            throw zzjk.zzb();
                        }
                        if (i16 != 0) {
                            zzjlVar.add(new String(bArr, i8, i16, zzjf.zza));
                            i8 += i16;
                            while (i8 < i2) {
                                int zza10 = zzhl.zza(bArr, i8, zzhnVar);
                                if (i3 != zzhnVar.zza) {
                                    break;
                                } else {
                                    i8 = zzhl.zza(bArr, zza10, zzhnVar);
                                    int i17 = zzhnVar.zza;
                                    if (i17 < 0) {
                                        throw zzjk.zzb();
                                    }
                                    if (i17 != 0) {
                                        zzjlVar.add(new String(bArr, i8, i17, zzjf.zza));
                                        i8 += i17;
                                    }
                                }
                            }
                            break;
                        }
                        zzjlVar.add("");
                        while (i8 < i2) {
                        }
                    } else {
                        int i18 = zzhnVar.zza;
                        if (i18 < 0) {
                            throw zzjk.zzb();
                        }
                        if (i18 != 0) {
                            int i19 = i8 + i18;
                            if (!zzmd.zza(bArr, i8, i19)) {
                                throw zzjk.zzh();
                            }
                            zzjlVar.add(new String(bArr, i8, i18, zzjf.zza));
                            i8 = i19;
                            while (i8 < i2) {
                                int zza11 = zzhl.zza(bArr, i8, zzhnVar);
                                if (i3 != zzhnVar.zza) {
                                    break;
                                } else {
                                    i8 = zzhl.zza(bArr, zza11, zzhnVar);
                                    int i20 = zzhnVar.zza;
                                    if (i20 < 0) {
                                        throw zzjk.zzb();
                                    }
                                    if (i20 != 0) {
                                        int i21 = i8 + i20;
                                        if (!zzmd.zza(bArr, i8, i21)) {
                                            throw zzjk.zzh();
                                        }
                                        zzjlVar.add(new String(bArr, i8, i20, zzjf.zza));
                                        i8 = i21;
                                    }
                                }
                            }
                            break;
                        }
                        zzjlVar.add("");
                        while (i8 < i2) {
                        }
                    }
                }
                break;
            case 27:
                if (i5 == 2) {
                    return zzhl.zza(zza(i6), i3, bArr, i, i2, zzjlVar, zzhnVar);
                }
                break;
            case 28:
                if (i5 == 2) {
                    int zza12 = zzhl.zza(bArr, i8, zzhnVar);
                    int i22 = zzhnVar.zza;
                    if (i22 < 0) {
                        throw zzjk.zzb();
                    }
                    if (i22 > bArr.length - zza12) {
                        throw zzjk.zza();
                    }
                }
                break;
            case 30:
            case 44:
                if (i5 == 2) {
                    zza2 = zzhl.zza(bArr, i8, zzjlVar, zzhnVar);
                } else if (i5 == 0) {
                    zza2 = zzhl.zza(i3, bArr, i, i2, zzjlVar, zzhnVar);
                }
                zzjb zzjbVar = (zzjb) obj;
                zzlx zzlxVar = zzjbVar.zzb;
                zzlx zzlxVar2 = (zzlx) zzle.zza(i4, zzjlVar, zzc(i6), zzlxVar != zzlx.zza() ? zzlxVar : null, this.zzq);
                if (zzlxVar2 != null) {
                    zzjbVar.zzb = zzlxVar2;
                }
                return zza2;
            case 33:
            case 47:
                if (i5 == 2) {
                    zzjd zzjdVar3 = (zzjd) zzjlVar;
                    int zza13 = zzhl.zza(bArr, i8, zzhnVar);
                    int i23 = zzhnVar.zza + zza13;
                    while (zza13 < i23) {
                        zza13 = zzhl.zza(bArr, zza13, zzhnVar);
                        zzjdVar3.zzc(zzif.zze(zzhnVar.zza));
                    }
                    if (zza13 == i23) {
                        return zza13;
                    }
                    throw zzjk.zza();
                }
                if (i5 == 0) {
                    zzjd zzjdVar4 = (zzjd) zzjlVar;
                    do {
                        zza3 = zzhl.zza(bArr, i8, zzhnVar);
                        zzjdVar4.zzc(zzif.zze(zzhnVar.zza));
                        if (zza3 >= i2) {
                            return zza3;
                        }
                        i8 = zzhl.zza(bArr, zza3, zzhnVar);
                    } while (i3 == zzhnVar.zza);
                    return zza3;
                }
                break;
            case 34:
            case 48:
                if (i5 == 2) {
                    int zza14 = zzhl.zza(bArr, i8, zzhnVar);
                    int i24 = zzhnVar.zza + zza14;
                    if (zza14 >= i24) {
                        if (zza14 == i24) {
                            return zza14;
                        }
                        throw zzjk.zza();
                    }
                    zzhl.zzb(bArr, zza14, zzhnVar);
                    zzif.zza(zzhnVar.zzb);
                    throw null;
                }
                if (i5 == 0) {
                    zzhl.zzb(bArr, i8, zzhnVar);
                    zzif.zza(zzhnVar.zzb);
                    throw null;
                }
                break;
            case 49:
                if (i5 == 3) {
                    zzlc zza15 = zza(i6);
                    int i25 = (i3 & (-8)) | 4;
                    i8 = zzhl.zza(zza15, bArr, i, i2, i25, zzhnVar);
                    while (true) {
                        zzjlVar.add(zzhnVar.zzc);
                        if (i8 >= i2) {
                            break;
                        } else {
                            int zza16 = zzhl.zza(bArr, i8, zzhnVar);
                            if (i3 != zzhnVar.zza) {
                                break;
                            } else {
                                i8 = zzhl.zza(zza15, bArr, zza16, i2, i25, zzhnVar);
                            }
                        }
                    }
                }
                break;
        }
        return i8;
    }

    private final int zza(Object obj, byte[] bArr, int i, int i2, int i3, long j, zzhn zzhnVar) {
        Unsafe unsafe = zzb;
        Object zzb2 = zzb(i3);
        Object object = unsafe.getObject(obj, j);
        if (this.zzs.zzd(object)) {
            Object zzf = this.zzs.zzf(zzb2);
            this.zzs.zza(zzf, object);
            unsafe.putObject(obj, j, zzf);
            object = zzf;
        }
        this.zzs.zzb(zzb2);
        this.zzs.zza(object);
        int zza2 = zzhl.zza(bArr, i, zzhnVar);
        int i4 = zzhnVar.zza;
        if (i4 < 0 || i4 > i2 - zza2) {
            throw zzjk.zza();
        }
        throw null;
    }

    /* JADX WARN: Removed duplicated region for block: B:106:0x033b  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0399  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    static zzko zza(Class cls, zzki zzkiVar, zzks zzksVar, zzju zzjuVar, zzlu zzluVar, zziq zziqVar, zzkh zzkhVar) {
        int i;
        int charAt;
        int charAt2;
        int charAt3;
        int charAt4;
        int charAt5;
        int i2;
        int[] iArr;
        int i3;
        char charAt6;
        int i4;
        char charAt7;
        int i5;
        char charAt8;
        int i6;
        char charAt9;
        int i7;
        char charAt10;
        int i8;
        char charAt11;
        int i9;
        char charAt12;
        int i10;
        char charAt13;
        int i11;
        int i12;
        int i13;
        int i14;
        zzla zzlaVar;
        int i15;
        int objectFieldOffset;
        int i16;
        String str;
        int i17;
        int i18;
        int i19;
        Field zza2;
        char charAt14;
        int i20;
        int i21;
        Field zza3;
        Field zza4;
        int i22;
        char charAt15;
        int i23;
        char charAt16;
        int i24;
        char charAt17;
        int i25;
        char charAt18;
        if (!(zzkiVar instanceof zzla)) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzkiVar);
            throw null;
        }
        zzla zzlaVar2 = (zzla) zzkiVar;
        int i26 = 0;
        boolean z = zzlaVar2.zza() == zzkz.zzb;
        String zzd = zzlaVar2.zzd();
        int length = zzd.length();
        if (zzd.charAt(0) >= 55296) {
            int i27 = 1;
            while (true) {
                i = i27 + 1;
                if (zzd.charAt(i27) < 55296) {
                    break;
                }
                i27 = i;
            }
        } else {
            i = 1;
        }
        int i28 = i + 1;
        int charAt19 = zzd.charAt(i);
        if (charAt19 >= 55296) {
            int i29 = charAt19 & 8191;
            int i30 = 13;
            while (true) {
                i25 = i28 + 1;
                charAt18 = zzd.charAt(i28);
                if (charAt18 < 55296) {
                    break;
                }
                i29 |= (charAt18 & 8191) << i30;
                i30 += 13;
                i28 = i25;
            }
            charAt19 = i29 | (charAt18 << i30);
            i28 = i25;
        }
        if (charAt19 == 0) {
            iArr = zza;
            i2 = 0;
            charAt = 0;
            charAt2 = 0;
            charAt3 = 0;
            charAt4 = 0;
            charAt5 = 0;
        } else {
            int i31 = i28 + 1;
            int charAt20 = zzd.charAt(i28);
            if (charAt20 >= 55296) {
                int i32 = charAt20 & 8191;
                int i33 = 13;
                while (true) {
                    i10 = i31 + 1;
                    charAt13 = zzd.charAt(i31);
                    if (charAt13 < 55296) {
                        break;
                    }
                    i32 |= (charAt13 & 8191) << i33;
                    i33 += 13;
                    i31 = i10;
                }
                charAt20 = i32 | (charAt13 << i33);
                i31 = i10;
            }
            int i34 = i31 + 1;
            int charAt21 = zzd.charAt(i31);
            if (charAt21 >= 55296) {
                int i35 = charAt21 & 8191;
                int i36 = 13;
                while (true) {
                    i9 = i34 + 1;
                    charAt12 = zzd.charAt(i34);
                    if (charAt12 < 55296) {
                        break;
                    }
                    i35 |= (charAt12 & 8191) << i36;
                    i36 += 13;
                    i34 = i9;
                }
                charAt21 = i35 | (charAt12 << i36);
                i34 = i9;
            }
            int i37 = i34 + 1;
            charAt = zzd.charAt(i34);
            if (charAt >= 55296) {
                int i38 = charAt & 8191;
                int i39 = 13;
                while (true) {
                    i8 = i37 + 1;
                    charAt11 = zzd.charAt(i37);
                    if (charAt11 < 55296) {
                        break;
                    }
                    i38 |= (charAt11 & 8191) << i39;
                    i39 += 13;
                    i37 = i8;
                }
                charAt = i38 | (charAt11 << i39);
                i37 = i8;
            }
            int i40 = i37 + 1;
            charAt2 = zzd.charAt(i37);
            if (charAt2 >= 55296) {
                int i41 = charAt2 & 8191;
                int i42 = 13;
                while (true) {
                    i7 = i40 + 1;
                    charAt10 = zzd.charAt(i40);
                    if (charAt10 < 55296) {
                        break;
                    }
                    i41 |= (charAt10 & 8191) << i42;
                    i42 += 13;
                    i40 = i7;
                }
                charAt2 = i41 | (charAt10 << i42);
                i40 = i7;
            }
            int i43 = i40 + 1;
            charAt3 = zzd.charAt(i40);
            if (charAt3 >= 55296) {
                int i44 = charAt3 & 8191;
                int i45 = 13;
                while (true) {
                    i6 = i43 + 1;
                    charAt9 = zzd.charAt(i43);
                    if (charAt9 < 55296) {
                        break;
                    }
                    i44 |= (charAt9 & 8191) << i45;
                    i45 += 13;
                    i43 = i6;
                }
                charAt3 = i44 | (charAt9 << i45);
                i43 = i6;
            }
            int i46 = i43 + 1;
            charAt4 = zzd.charAt(i43);
            if (charAt4 >= 55296) {
                int i47 = charAt4 & 8191;
                int i48 = 13;
                while (true) {
                    i5 = i46 + 1;
                    charAt8 = zzd.charAt(i46);
                    if (charAt8 < 55296) {
                        break;
                    }
                    i47 |= (charAt8 & 8191) << i48;
                    i48 += 13;
                    i46 = i5;
                }
                charAt4 = i47 | (charAt8 << i48);
                i46 = i5;
            }
            int i49 = i46 + 1;
            int charAt22 = zzd.charAt(i46);
            if (charAt22 >= 55296) {
                int i50 = charAt22 & 8191;
                int i51 = 13;
                while (true) {
                    i4 = i49 + 1;
                    charAt7 = zzd.charAt(i49);
                    if (charAt7 < 55296) {
                        break;
                    }
                    i50 |= (charAt7 & 8191) << i51;
                    i51 += 13;
                    i49 = i4;
                }
                charAt22 = i50 | (charAt7 << i51);
                i49 = i4;
            }
            int i52 = i49 + 1;
            charAt5 = zzd.charAt(i49);
            if (charAt5 >= 55296) {
                int i53 = charAt5 & 8191;
                int i54 = i52;
                int i55 = 13;
                while (true) {
                    i3 = i54 + 1;
                    charAt6 = zzd.charAt(i54);
                    if (charAt6 < 55296) {
                        break;
                    }
                    i53 |= (charAt6 & 8191) << i55;
                    i55 += 13;
                    i54 = i3;
                }
                charAt5 = i53 | (charAt6 << i55);
                i52 = i3;
            }
            i2 = (charAt20 << 1) + charAt21;
            iArr = new int[charAt5 + charAt4 + charAt22];
            i26 = charAt20;
            i28 = i52;
        }
        Unsafe unsafe = zzb;
        Object[] zze = zzlaVar2.zze();
        Class<?> cls2 = zzlaVar2.zzc().getClass();
        int i56 = i28;
        int[] iArr2 = new int[charAt3 * 3];
        Object[] objArr = new Object[charAt3 << 1];
        int i57 = charAt5 + charAt4;
        int i58 = i2;
        int i59 = charAt5;
        int i60 = i56;
        int i61 = i57;
        int i62 = 0;
        int i63 = 0;
        while (i60 < length) {
            int i64 = i60 + 1;
            int charAt23 = zzd.charAt(i60);
            if (charAt23 >= 55296) {
                int i65 = charAt23 & 8191;
                int i66 = i64;
                int i67 = 13;
                while (true) {
                    i24 = i66 + 1;
                    charAt17 = zzd.charAt(i66);
                    i11 = length;
                    if (charAt17 < 55296) {
                        break;
                    }
                    i65 |= (charAt17 & 8191) << i67;
                    i67 += 13;
                    i66 = i24;
                    length = i11;
                }
                charAt23 = i65 | (charAt17 << i67);
                i12 = i24;
            } else {
                i11 = length;
                i12 = i64;
            }
            int i68 = i12 + 1;
            int charAt24 = zzd.charAt(i12);
            if (charAt24 >= 55296) {
                int i69 = charAt24 & 8191;
                int i70 = i68;
                int i71 = 13;
                while (true) {
                    i23 = i70 + 1;
                    charAt16 = zzd.charAt(i70);
                    i13 = charAt5;
                    if (charAt16 < 55296) {
                        break;
                    }
                    i69 |= (charAt16 & 8191) << i71;
                    i71 += 13;
                    i70 = i23;
                    charAt5 = i13;
                }
                charAt24 = i69 | (charAt16 << i71);
                i14 = i23;
            } else {
                i13 = charAt5;
                i14 = i68;
            }
            int i72 = charAt24 & NotificationCenter.newLocationAvailable;
            int i73 = charAt2;
            if ((charAt24 & 1024) != 0) {
                iArr[i62] = i63;
                i62++;
            }
            int i74 = charAt;
            if (i72 >= 51) {
                int i75 = i14 + 1;
                int charAt25 = zzd.charAt(i14);
                char c = CharacterCompat.MIN_HIGH_SURROGATE;
                if (charAt25 >= 55296) {
                    int i76 = charAt25 & 8191;
                    int i77 = 13;
                    while (true) {
                        i22 = i75 + 1;
                        charAt15 = zzd.charAt(i75);
                        if (charAt15 < c) {
                            break;
                        }
                        i76 |= (charAt15 & 8191) << i77;
                        i77 += 13;
                        i75 = i22;
                        c = CharacterCompat.MIN_HIGH_SURROGATE;
                    }
                    charAt25 = i76 | (charAt15 << i77);
                    i75 = i22;
                }
                int i78 = i72 - 51;
                int i79 = i75;
                if (i78 == 9 || i78 == 17) {
                    i21 = 1;
                    objArr[((i63 / 3) << 1) + 1] = zze[i58];
                    i58++;
                } else {
                    if (i78 == 12 && !z) {
                        objArr[((i63 / 3) << 1) + 1] = zze[i58];
                        i58++;
                    }
                    i21 = 1;
                }
                int i80 = charAt25 << i21;
                Object obj = zze[i80];
                if (obj instanceof Field) {
                    zza3 = (Field) obj;
                } else {
                    zza3 = zza((Class) cls2, (String) obj);
                    zze[i80] = zza3;
                }
                int objectFieldOffset2 = (int) unsafe.objectFieldOffset(zza3);
                int i81 = i80 + 1;
                Object obj2 = zze[i81];
                if (obj2 instanceof Field) {
                    zza4 = (Field) obj2;
                } else {
                    zza4 = zza((Class) cls2, (String) obj2);
                    zze[i81] = zza4;
                }
                i16 = i58;
                objectFieldOffset = objectFieldOffset2;
                i18 = i79;
                zzlaVar = zzlaVar2;
                str = zzd;
                i17 = (int) unsafe.objectFieldOffset(zza4);
                i19 = 0;
            } else {
                int i82 = i58 + 1;
                Field zza5 = zza((Class) cls2, (String) zze[i58]);
                zzlaVar = zzlaVar2;
                if (i72 == 9 || i72 == 17) {
                    objArr[((i63 / 3) << 1) + 1] = zza5.getType();
                } else {
                    if (i72 == 27 || i72 == 49) {
                        i20 = i58 + 2;
                        objArr[((i63 / 3) << 1) + 1] = zze[i82];
                    } else if (i72 == 12 || i72 == 30 || i72 == 44) {
                        if (!z) {
                            i20 = i58 + 2;
                            objArr[((i63 / 3) << 1) + 1] = zze[i82];
                        }
                    } else if (i72 == 50) {
                        int i83 = i59 + 1;
                        iArr[i59] = i63;
                        int i84 = (i63 / 3) << 1;
                        int i85 = i58 + 2;
                        objArr[i84] = zze[i82];
                        if ((charAt24 & 2048) != 0) {
                            i82 = i58 + 3;
                            objArr[i84 + 1] = zze[i85];
                            i59 = i83;
                        } else {
                            i59 = i83;
                            i15 = i85;
                            objectFieldOffset = (int) unsafe.objectFieldOffset(zza5);
                            if ((charAt24 & LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM) == 4096 || i72 > 17) {
                                i16 = i15;
                                str = zzd;
                                i17 = 1048575;
                                i18 = i14;
                                i19 = 0;
                            } else {
                                int i86 = i14 + 1;
                                int charAt26 = zzd.charAt(i14);
                                if (charAt26 >= 55296) {
                                    int i87 = charAt26 & 8191;
                                    int i88 = 13;
                                    while (true) {
                                        i18 = i86 + 1;
                                        charAt14 = zzd.charAt(i86);
                                        if (charAt14 < 55296) {
                                            break;
                                        }
                                        i87 |= (charAt14 & 8191) << i88;
                                        i88 += 13;
                                        i86 = i18;
                                    }
                                    charAt26 = i87 | (charAt14 << i88);
                                } else {
                                    i18 = i86;
                                }
                                int i89 = (i26 << 1) + (charAt26 / 32);
                                Object obj3 = zze[i89];
                                if (obj3 instanceof Field) {
                                    zza2 = (Field) obj3;
                                } else {
                                    zza2 = zza((Class) cls2, (String) obj3);
                                    zze[i89] = zza2;
                                }
                                i16 = i15;
                                str = zzd;
                                i19 = charAt26 % 32;
                                i17 = (int) unsafe.objectFieldOffset(zza2);
                            }
                            if (i72 >= 18 && i72 <= 49) {
                                iArr[i61] = objectFieldOffset;
                                i61++;
                            }
                        }
                    }
                    i15 = i20;
                    objectFieldOffset = (int) unsafe.objectFieldOffset(zza5);
                    if ((charAt24 & LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM) == 4096) {
                    }
                    i16 = i15;
                    str = zzd;
                    i17 = 1048575;
                    i18 = i14;
                    i19 = 0;
                    if (i72 >= 18) {
                        iArr[i61] = objectFieldOffset;
                        i61++;
                    }
                }
                i15 = i82;
                objectFieldOffset = (int) unsafe.objectFieldOffset(zza5);
                if ((charAt24 & LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM) == 4096) {
                }
                i16 = i15;
                str = zzd;
                i17 = 1048575;
                i18 = i14;
                i19 = 0;
                if (i72 >= 18) {
                }
            }
            int i90 = i63 + 1;
            iArr2[i63] = charAt23;
            int i91 = i63 + 2;
            iArr2[i90] = ((charAt24 & 256) != 0 ? 268435456 : 0) | ((charAt24 & 512) != 0 ? 536870912 : 0) | (i72 << 20) | objectFieldOffset;
            i63 += 3;
            iArr2[i91] = i17 | (i19 << 20);
            zzd = str;
            i58 = i16;
            charAt2 = i73;
            i60 = i18;
            length = i11;
            charAt5 = i13;
            charAt = i74;
            zzlaVar2 = zzlaVar;
        }
        return new zzko(iArr2, objArr, charAt, charAt2, zzlaVar2.zzc(), z, false, iArr, charAt5, i57, zzksVar, zzjuVar, zzluVar, zziqVar, zzkhVar);
    }

    private final zzlc zza(int i) {
        int i2 = (i / 3) << 1;
        zzlc zzlcVar = (zzlc) this.zzd[i2];
        if (zzlcVar != null) {
            return zzlcVar;
        }
        zzlc zza2 = zzky.zza().zza((Class) this.zzd[i2 + 1]);
        this.zzd[i2] = zza2;
        return zza2;
    }

    private final Object zza(int i, int i2, Map map, zzjg zzjgVar, Object obj, zzlu zzluVar) {
        this.zzs.zzb(zzb(i));
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (!zzjgVar.zza(((Integer) entry.getValue()).intValue())) {
                if (obj == null) {
                    obj = zzluVar.zza();
                }
                zzib zzc = zzht.zzc(zzkc.zza(null, entry.getKey(), entry.getValue()));
                try {
                    zzkc.zza(zzc.zzb(), null, entry.getKey(), entry.getValue());
                    zzluVar.zza(obj, i2, zzc.zza());
                    it.remove();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return obj;
    }

    private final Object zza(Object obj, int i, Object obj2, zzlu zzluVar) {
        zzjg zzc;
        int i2 = this.zzc[i];
        Object zzf = zzma.zzf(obj, zzd(i) & 1048575);
        return (zzf == null || (zzc = zzc(i)) == null) ? obj2 : zza(i, i2, this.zzs.zza(zzf), zzc, obj2, zzluVar);
    }

    private static Field zza(Class cls, String str) {
        try {
            return cls.getDeclaredField(str);
        } catch (NoSuchFieldException unused) {
            Field[] declaredFields = cls.getDeclaredFields();
            for (Field field : declaredFields) {
                if (str.equals(field.getName())) {
                    return field;
                }
            }
            String name = cls.getName();
            String arrays = Arrays.toString(declaredFields);
            StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 40 + name.length() + String.valueOf(arrays).length());
            sb.append("Field ");
            sb.append(str);
            sb.append(" for ");
            sb.append(name);
            sb.append(" not found. Known fields are ");
            sb.append(arrays);
            throw new RuntimeException(sb.toString());
        }
    }

    private static List zza(Object obj, long j) {
        return (List) zzma.zzf(obj, j);
    }

    private static void zza(int i, Object obj, zzmr zzmrVar) {
        if (obj instanceof String) {
            zzmrVar.zza(i, (String) obj);
        } else {
            zzmrVar.zza(i, (zzht) obj);
        }
    }

    private static void zza(zzlu zzluVar, Object obj, zzmr zzmrVar) {
        zzluVar.zza(zzluVar.zzb(obj), zzmrVar);
    }

    private final void zza(zzmr zzmrVar, int i, Object obj, int i2) {
        if (obj != null) {
            this.zzs.zzb(zzb(i2));
            zzmrVar.zza(i, (zzkf) null, this.zzs.zzc(obj));
        }
    }

    private final void zza(Object obj, Object obj2, int i) {
        long zzd = zzd(i) & 1048575;
        if (zza(obj2, i)) {
            Object zzf = zzma.zzf(obj, zzd);
            Object zzf2 = zzma.zzf(obj2, zzd);
            if (zzf != null && zzf2 != null) {
                zzma.zza(obj, zzd, zzjf.zza(zzf, zzf2));
                zzb(obj, i);
            } else if (zzf2 != null) {
                zzma.zza(obj, zzd, zzf2);
                zzb(obj, i);
            }
        }
    }

    private final boolean zza(Object obj, int i) {
        int zze = zze(i);
        long j = zze & 1048575;
        if (j != 1048575) {
            return (zzma.zza(obj, j) & (1 << (zze >>> 20))) != 0;
        }
        int zzd = zzd(i);
        long j2 = zzd & 1048575;
        switch ((zzd & 267386880) >>> 20) {
            case 0:
                return zzma.zze(obj, j2) != 0.0d;
            case 1:
                return zzma.zzd(obj, j2) != 0.0f;
            case 2:
                return zzma.zzb(obj, j2) != 0;
            case 3:
                return zzma.zzb(obj, j2) != 0;
            case 4:
                return zzma.zza(obj, j2) != 0;
            case 5:
                return zzma.zzb(obj, j2) != 0;
            case 6:
                return zzma.zza(obj, j2) != 0;
            case 7:
                return zzma.zzc(obj, j2);
            case 8:
                Object zzf = zzma.zzf(obj, j2);
                if (zzf instanceof String) {
                    return !((String) zzf).isEmpty();
                }
                if (zzf instanceof zzht) {
                    return !zzht.zza.equals(zzf);
                }
                throw new IllegalArgumentException();
            case 9:
                return zzma.zzf(obj, j2) != null;
            case 10:
                return !zzht.zza.equals(zzma.zzf(obj, j2));
            case 11:
                return zzma.zza(obj, j2) != 0;
            case 12:
                return zzma.zza(obj, j2) != 0;
            case 13:
                return zzma.zza(obj, j2) != 0;
            case 14:
                return zzma.zzb(obj, j2) != 0;
            case 15:
                return zzma.zza(obj, j2) != 0;
            case 16:
                return zzma.zzb(obj, j2) != 0;
            case 17:
                return zzma.zzf(obj, j2) != null;
            default:
                throw new IllegalArgumentException();
        }
    }

    private final boolean zza(Object obj, int i, int i2) {
        return zzma.zza(obj, (long) (zze(i2) & 1048575)) == i;
    }

    private final boolean zza(Object obj, int i, int i2, int i3, int i4) {
        return i2 == 1048575 ? zza(obj, i) : (i3 & i4) != 0;
    }

    private static boolean zza(Object obj, int i, zzlc zzlcVar) {
        return zzlcVar.zzd(zzma.zzf(obj, i & 1048575));
    }

    private static double zzb(Object obj, long j) {
        return ((Double) zzma.zzf(obj, j)).doubleValue();
    }

    private final int zzb(int i, int i2) {
        int length = (this.zzc.length / 3) - 1;
        while (i2 <= length) {
            int i3 = (length + i2) >>> 1;
            int i4 = i3 * 3;
            int i5 = this.zzc[i4];
            if (i == i5) {
                return i4;
            }
            if (i < i5) {
                length = i3 - 1;
            } else {
                i2 = i3 + 1;
            }
        }
        return -1;
    }

    private final Object zzb(int i) {
        return this.zzd[(i / 3) << 1];
    }

    private final void zzb(Object obj, int i) {
        int zze = zze(i);
        long j = 1048575 & zze;
        if (j == 1048575) {
            return;
        }
        zzma.zza(obj, j, (1 << (zze >>> 20)) | zzma.zza(obj, j));
    }

    private final void zzb(Object obj, int i, int i2) {
        zzma.zza(obj, zze(i2) & 1048575, i);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:228:0x041f  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0031  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void zzb(Object obj, zzmr zzmrVar) {
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
        boolean z6;
        int i8;
        boolean z7;
        int i9;
        int i10;
        List list;
        boolean z8;
        boolean z9;
        int i11;
        boolean z10;
        int i12;
        boolean z11;
        int i13;
        boolean z12;
        int i14;
        boolean z13;
        int i15;
        int i16;
        List list2;
        boolean z14;
        if (this.zzh) {
            zziu zza2 = this.zzr.zza(obj);
            if (!zza2.zza.isEmpty()) {
                it = zza2.zzd();
                entry = (Map.Entry) it.next();
                length = this.zzc.length;
                Unsafe unsafe = zzb;
                int i17 = 1048575;
                int i18 = 0;
                for (i = 0; i < length; i += 3) {
                    int zzd = zzd(i);
                    int[] iArr = this.zzc;
                    int i19 = iArr[i];
                    int i20 = (zzd & 267386880) >>> 20;
                    if (i20 <= 17) {
                        int i21 = iArr[i + 2];
                        int i22 = i21 & 1048575;
                        if (i22 != i17) {
                            i18 = unsafe.getInt(obj, i22);
                            i17 = i22;
                        }
                        i2 = 1 << (i21 >>> 20);
                    } else {
                        i2 = 0;
                    }
                    while (entry != null && this.zzr.zza(entry) <= i19) {
                        this.zzr.zza(zzmrVar, entry);
                        entry = it.hasNext() ? (Map.Entry) it.next() : null;
                    }
                    long j = zzd & 1048575;
                    switch (i20) {
                        case 0:
                            if ((i2 & i18) != 0) {
                                zzmrVar.zza(i19, zzma.zze(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 1:
                            if ((i2 & i18) != 0) {
                                zzmrVar.zza(i19, zzma.zzd(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 2:
                            if ((i2 & i18) != 0) {
                                zzmrVar.zza(i19, unsafe.getLong(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 3:
                            if ((i2 & i18) != 0) {
                                zzmrVar.zzc(i19, unsafe.getLong(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 4:
                            if ((i2 & i18) != 0) {
                                zzmrVar.zzc(i19, unsafe.getInt(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 5:
                            if ((i2 & i18) != 0) {
                                zzmrVar.zzd(i19, unsafe.getLong(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 6:
                            if ((i2 & i18) != 0) {
                                zzmrVar.zzd(i19, unsafe.getInt(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 7:
                            if ((i2 & i18) != 0) {
                                zzmrVar.zza(i19, zzma.zzc(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 8:
                            if ((i2 & i18) != 0) {
                                zza(i19, unsafe.getObject(obj, j), zzmrVar);
                                break;
                            } else {
                                break;
                            }
                        case 9:
                            if ((i2 & i18) != 0) {
                                zzmrVar.zza(i19, unsafe.getObject(obj, j), zza(i));
                                break;
                            } else {
                                break;
                            }
                        case 10:
                            if ((i2 & i18) != 0) {
                                zzmrVar.zza(i19, (zzht) unsafe.getObject(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 11:
                            if ((i2 & i18) != 0) {
                                zzmrVar.zze(i19, unsafe.getInt(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 12:
                            if ((i2 & i18) != 0) {
                                zzmrVar.zzb(i19, unsafe.getInt(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 13:
                            if ((i2 & i18) != 0) {
                                zzmrVar.zza(i19, unsafe.getInt(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 14:
                            if ((i2 & i18) != 0) {
                                zzmrVar.zzb(i19, unsafe.getLong(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 15:
                            if ((i2 & i18) != 0) {
                                zzmrVar.zzf(i19, unsafe.getInt(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 16:
                            if ((i2 & i18) != 0) {
                                zzmrVar.zze(i19, unsafe.getLong(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 17:
                            if ((i2 & i18) != 0) {
                                zzmrVar.zzb(i19, unsafe.getObject(obj, j), zza(i));
                                break;
                            } else {
                                break;
                            }
                        case 18:
                            z = false;
                            i3 = this.zzc[i];
                            zzle.zza(i3, (List) unsafe.getObject(obj, j), zzmrVar, z);
                            break;
                        case 19:
                            z2 = false;
                            i4 = this.zzc[i];
                            zzle.zzb(i4, (List) unsafe.getObject(obj, j), zzmrVar, z2);
                            break;
                        case 20:
                            z3 = false;
                            i5 = this.zzc[i];
                            zzle.zzc(i5, (List) unsafe.getObject(obj, j), zzmrVar, z3);
                            break;
                        case 21:
                            z4 = false;
                            i6 = this.zzc[i];
                            zzle.zzd(i6, (List) unsafe.getObject(obj, j), zzmrVar, z4);
                            break;
                        case 22:
                            z5 = false;
                            i7 = this.zzc[i];
                            zzle.zzh(i7, (List) unsafe.getObject(obj, j), zzmrVar, z5);
                            break;
                        case 23:
                            z6 = false;
                            i8 = this.zzc[i];
                            zzle.zzf(i8, (List) unsafe.getObject(obj, j), zzmrVar, z6);
                            break;
                        case 24:
                            z7 = false;
                            i9 = this.zzc[i];
                            zzle.zzk(i9, (List) unsafe.getObject(obj, j), zzmrVar, z7);
                            break;
                        case 25:
                            i10 = this.zzc[i];
                            list = (List) unsafe.getObject(obj, j);
                            z8 = false;
                            zzle.zzn(i10, list, zzmrVar, z8);
                            break;
                        case 26:
                            zzle.zza(this.zzc[i], (List) unsafe.getObject(obj, j), zzmrVar);
                            break;
                        case 27:
                            zzle.zza(this.zzc[i], (List) unsafe.getObject(obj, j), zzmrVar, zza(i));
                            break;
                        case 28:
                            zzle.zzb(this.zzc[i], (List) unsafe.getObject(obj, j), zzmrVar);
                            break;
                        case 29:
                            z9 = false;
                            i11 = this.zzc[i];
                            zzle.zzi(i11, (List) unsafe.getObject(obj, j), zzmrVar, z9);
                            break;
                        case 30:
                            z10 = false;
                            i12 = this.zzc[i];
                            zzle.zzm(i12, (List) unsafe.getObject(obj, j), zzmrVar, z10);
                            break;
                        case 31:
                            z11 = false;
                            i13 = this.zzc[i];
                            zzle.zzl(i13, (List) unsafe.getObject(obj, j), zzmrVar, z11);
                            break;
                        case 32:
                            z12 = false;
                            i14 = this.zzc[i];
                            zzle.zzg(i14, (List) unsafe.getObject(obj, j), zzmrVar, z12);
                            break;
                        case 33:
                            z13 = false;
                            i15 = this.zzc[i];
                            zzle.zzj(i15, (List) unsafe.getObject(obj, j), zzmrVar, z13);
                            break;
                        case 34:
                            i16 = this.zzc[i];
                            list2 = (List) unsafe.getObject(obj, j);
                            z14 = false;
                            zzle.zze(i16, list2, zzmrVar, z14);
                            break;
                        case 35:
                            z = true;
                            i3 = this.zzc[i];
                            zzle.zza(i3, (List) unsafe.getObject(obj, j), zzmrVar, z);
                            break;
                        case 36:
                            z2 = true;
                            i4 = this.zzc[i];
                            zzle.zzb(i4, (List) unsafe.getObject(obj, j), zzmrVar, z2);
                            break;
                        case 37:
                            z3 = true;
                            i5 = this.zzc[i];
                            zzle.zzc(i5, (List) unsafe.getObject(obj, j), zzmrVar, z3);
                            break;
                        case 38:
                            z4 = true;
                            i6 = this.zzc[i];
                            zzle.zzd(i6, (List) unsafe.getObject(obj, j), zzmrVar, z4);
                            break;
                        case 39:
                            z5 = true;
                            i7 = this.zzc[i];
                            zzle.zzh(i7, (List) unsafe.getObject(obj, j), zzmrVar, z5);
                            break;
                        case 40:
                            z6 = true;
                            i8 = this.zzc[i];
                            zzle.zzf(i8, (List) unsafe.getObject(obj, j), zzmrVar, z6);
                            break;
                        case 41:
                            z7 = true;
                            i9 = this.zzc[i];
                            zzle.zzk(i9, (List) unsafe.getObject(obj, j), zzmrVar, z7);
                            break;
                        case 42:
                            z8 = true;
                            i10 = this.zzc[i];
                            list = (List) unsafe.getObject(obj, j);
                            zzle.zzn(i10, list, zzmrVar, z8);
                            break;
                        case 43:
                            z9 = true;
                            i11 = this.zzc[i];
                            zzle.zzi(i11, (List) unsafe.getObject(obj, j), zzmrVar, z9);
                            break;
                        case 44:
                            z10 = true;
                            i12 = this.zzc[i];
                            zzle.zzm(i12, (List) unsafe.getObject(obj, j), zzmrVar, z10);
                            break;
                        case 45:
                            z11 = true;
                            i13 = this.zzc[i];
                            zzle.zzl(i13, (List) unsafe.getObject(obj, j), zzmrVar, z11);
                            break;
                        case 46:
                            z12 = true;
                            i14 = this.zzc[i];
                            zzle.zzg(i14, (List) unsafe.getObject(obj, j), zzmrVar, z12);
                            break;
                        case 47:
                            z13 = true;
                            i15 = this.zzc[i];
                            zzle.zzj(i15, (List) unsafe.getObject(obj, j), zzmrVar, z13);
                            break;
                        case 48:
                            i16 = this.zzc[i];
                            list2 = (List) unsafe.getObject(obj, j);
                            z14 = true;
                            zzle.zze(i16, list2, zzmrVar, z14);
                            break;
                        case 49:
                            zzle.zzb(this.zzc[i], (List) unsafe.getObject(obj, j), zzmrVar, zza(i));
                            break;
                        case 50:
                            zza(zzmrVar, i19, unsafe.getObject(obj, j), i);
                            break;
                        case 51:
                            if (zza(obj, i19, i)) {
                                zzmrVar.zza(i19, zzb(obj, j));
                            }
                            break;
                        case 52:
                            if (zza(obj, i19, i)) {
                                zzmrVar.zza(i19, zzc(obj, j));
                            }
                            break;
                        case 53:
                            if (zza(obj, i19, i)) {
                                zzmrVar.zza(i19, zze(obj, j));
                            }
                            break;
                        case 54:
                            if (zza(obj, i19, i)) {
                                zzmrVar.zzc(i19, zze(obj, j));
                            }
                            break;
                        case 55:
                            if (zza(obj, i19, i)) {
                                zzmrVar.zzc(i19, zzd(obj, j));
                            }
                            break;
                        case 56:
                            if (zza(obj, i19, i)) {
                                zzmrVar.zzd(i19, zze(obj, j));
                            }
                            break;
                        case 57:
                            if (zza(obj, i19, i)) {
                                zzmrVar.zzd(i19, zzd(obj, j));
                            }
                            break;
                        case 58:
                            if (zza(obj, i19, i)) {
                                zzmrVar.zza(i19, zzf(obj, j));
                            }
                            break;
                        case 59:
                            if (zza(obj, i19, i)) {
                                zza(i19, unsafe.getObject(obj, j), zzmrVar);
                            }
                            break;
                        case 60:
                            if (zza(obj, i19, i)) {
                                zzmrVar.zza(i19, unsafe.getObject(obj, j), zza(i));
                            }
                            break;
                        case 61:
                            if (zza(obj, i19, i)) {
                                zzmrVar.zza(i19, (zzht) unsafe.getObject(obj, j));
                            }
                            break;
                        case 62:
                            if (zza(obj, i19, i)) {
                                zzmrVar.zze(i19, zzd(obj, j));
                            }
                            break;
                        case 63:
                            if (zza(obj, i19, i)) {
                                zzmrVar.zzb(i19, zzd(obj, j));
                            }
                            break;
                        case 64:
                            if (zza(obj, i19, i)) {
                                zzmrVar.zza(i19, zzd(obj, j));
                            }
                            break;
                        case 65:
                            if (zza(obj, i19, i)) {
                                zzmrVar.zzb(i19, zze(obj, j));
                            }
                            break;
                        case 66:
                            if (zza(obj, i19, i)) {
                                zzmrVar.zzf(i19, zzd(obj, j));
                            }
                            break;
                        case 67:
                            if (zza(obj, i19, i)) {
                                zzmrVar.zze(i19, zze(obj, j));
                            }
                            break;
                        case 68:
                            if (zza(obj, i19, i)) {
                                zzmrVar.zzb(i19, unsafe.getObject(obj, j), zza(i));
                            }
                            break;
                    }
                }
                while (entry != null) {
                    this.zzr.zza(zzmrVar, entry);
                    entry = it.hasNext() ? (Map.Entry) it.next() : null;
                }
                zza(this.zzq, obj, zzmrVar);
            }
        }
        it = null;
        entry = null;
        length = this.zzc.length;
        Unsafe unsafe2 = zzb;
        int i172 = 1048575;
        int i182 = 0;
        while (i < length) {
        }
        while (entry != null) {
        }
        zza(this.zzq, obj, zzmrVar);
    }

    private final void zzb(Object obj, Object obj2, int i) {
        int zzd = zzd(i);
        int i2 = this.zzc[i];
        long j = zzd & 1048575;
        if (zza(obj2, i2, i)) {
            Object zzf = zza(obj, i2, i) ? zzma.zzf(obj, j) : null;
            Object zzf2 = zzma.zzf(obj2, j);
            if (zzf != null && zzf2 != null) {
                zzma.zza(obj, j, zzjf.zza(zzf, zzf2));
                zzb(obj, i2, i);
            } else if (zzf2 != null) {
                zzma.zza(obj, j, zzf2);
                zzb(obj, i2, i);
            }
        }
    }

    private static float zzc(Object obj, long j) {
        return ((Float) zzma.zzf(obj, j)).floatValue();
    }

    private final zzjg zzc(int i) {
        return (zzjg) this.zzd[((i / 3) << 1) + 1];
    }

    private final boolean zzc(Object obj, Object obj2, int i) {
        return zza(obj, i) == zza(obj2, i);
    }

    private final int zzd(int i) {
        return this.zzc[i + 1];
    }

    private static int zzd(Object obj, long j) {
        return ((Integer) zzma.zzf(obj, j)).intValue();
    }

    private final int zze(int i) {
        return this.zzc[i + 2];
    }

    private static long zze(Object obj, long j) {
        return ((Long) zzma.zzf(obj, j)).longValue();
    }

    private static zzlx zze(Object obj) {
        zzjb zzjbVar = (zzjb) obj;
        zzlx zzlxVar = zzjbVar.zzb;
        if (zzlxVar != zzlx.zza()) {
            return zzlxVar;
        }
        zzlx zzb2 = zzlx.zzb();
        zzjbVar.zzb = zzb2;
        return zzb2;
    }

    private static boolean zzf(Object obj, long j) {
        return ((Boolean) zzma.zzf(obj, j)).booleanValue();
    }

    private final int zzg(int i) {
        if (i < this.zze || i > this.zzf) {
            return -1;
        }
        return zzb(i, 0);
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
    @Override // com.google.android.gms.internal.vision.zzlc
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final int zza(Object obj) {
        int i;
        double zze;
        float zzd;
        boolean zzc;
        Object zzf;
        int zza2;
        long zzb2;
        Object zzf2;
        int length = this.zzc.length;
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3 += 3) {
            int zzd2 = zzd(i3);
            int i4 = this.zzc[i3];
            long j = 1048575 & zzd2;
            int i5 = 37;
            switch ((zzd2 & 267386880) >>> 20) {
                case 0:
                    i = i2 * 53;
                    zze = zzma.zze(obj, j);
                    zzb2 = Double.doubleToLongBits(zze);
                    zza2 = zzjf.zza(zzb2);
                    i2 = i + zza2;
                    break;
                case 1:
                    i = i2 * 53;
                    zzd = zzma.zzd(obj, j);
                    zza2 = Float.floatToIntBits(zzd);
                    i2 = i + zza2;
                    break;
                case 2:
                case 3:
                case 5:
                case 14:
                case 16:
                    i = i2 * 53;
                    zzb2 = zzma.zzb(obj, j);
                    zza2 = zzjf.zza(zzb2);
                    i2 = i + zza2;
                    break;
                case 4:
                case 6:
                case 11:
                case 12:
                case 13:
                case 15:
                    i = i2 * 53;
                    zza2 = zzma.zza(obj, j);
                    i2 = i + zza2;
                    break;
                case 7:
                    i = i2 * 53;
                    zzc = zzma.zzc(obj, j);
                    zza2 = zzjf.zza(zzc);
                    i2 = i + zza2;
                    break;
                case 8:
                    i = i2 * 53;
                    zza2 = ((String) zzma.zzf(obj, j)).hashCode();
                    i2 = i + zza2;
                    break;
                case 9:
                    zzf = zzma.zzf(obj, j);
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
                    zzf2 = zzma.zzf(obj, j);
                    zza2 = zzf2.hashCode();
                    i2 = i + zza2;
                    break;
                case 17:
                    zzf = zzma.zzf(obj, j);
                    break;
                case 51:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        zze = zzb(obj, j);
                        zzb2 = Double.doubleToLongBits(zze);
                        zza2 = zzjf.zza(zzb2);
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case 52:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        zzd = zzc(obj, j);
                        zza2 = Float.floatToIntBits(zzd);
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case 53:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzb2 = zze(obj, j);
                    zza2 = zzjf.zza(zzb2);
                    i2 = i + zza2;
                    break;
                case 54:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzb2 = zze(obj, j);
                    zza2 = zzjf.zza(zzb2);
                    i2 = i + zza2;
                    break;
                case 55:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zza2 = zzd(obj, j);
                    i2 = i + zza2;
                    break;
                case 56:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzb2 = zze(obj, j);
                    zza2 = zzjf.zza(zzb2);
                    i2 = i + zza2;
                    break;
                case 57:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zza2 = zzd(obj, j);
                    i2 = i + zza2;
                    break;
                case 58:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        zzc = zzf(obj, j);
                        zza2 = zzjf.zza(zzc);
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case 59:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zza2 = ((String) zzma.zzf(obj, j)).hashCode();
                    i2 = i + zza2;
                    break;
                case 60:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    zzf2 = zzma.zzf(obj, j);
                    i = i2 * 53;
                    zza2 = zzf2.hashCode();
                    i2 = i + zza2;
                    break;
                case 61:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzf2 = zzma.zzf(obj, j);
                    zza2 = zzf2.hashCode();
                    i2 = i + zza2;
                    break;
                case 62:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zza2 = zzd(obj, j);
                    i2 = i + zza2;
                    break;
                case 63:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zza2 = zzd(obj, j);
                    i2 = i + zza2;
                    break;
                case 64:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zza2 = zzd(obj, j);
                    i2 = i + zza2;
                    break;
                case 65:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzb2 = zze(obj, j);
                    zza2 = zzjf.zza(zzb2);
                    i2 = i + zza2;
                    break;
                case 66:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zza2 = zzd(obj, j);
                    i2 = i + zza2;
                    break;
                case 67:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzb2 = zze(obj, j);
                    zza2 = zzjf.zza(zzb2);
                    i2 = i + zza2;
                    break;
                case 68:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    zzf2 = zzma.zzf(obj, j);
                    i = i2 * 53;
                    zza2 = zzf2.hashCode();
                    i2 = i + zza2;
                    break;
            }
        }
        int hashCode = (i2 * 53) + this.zzq.zzb(obj).hashCode();
        return this.zzh ? (hashCode * 53) + this.zzr.zza(obj).hashCode() : hashCode;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:106:0x008c. Please report as an issue. */
    final int zza(Object obj, byte[] bArr, int i, int i2, int i3, zzhn zzhnVar) {
        Unsafe unsafe;
        int i4;
        zzko zzkoVar;
        int i5;
        Object obj2;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        zzhn zzhnVar2;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        int zzb2;
        Unsafe unsafe2;
        Object obj3;
        long j;
        long j2;
        long j3;
        int i18;
        int i19;
        long j4;
        Object obj4;
        int i20;
        int i21;
        int i22;
        zzko zzkoVar2 = this;
        Object obj5 = obj;
        byte[] bArr2 = bArr;
        int i23 = i2;
        int i24 = i3;
        zzhn zzhnVar3 = zzhnVar;
        Unsafe unsafe3 = zzb;
        int i25 = i;
        int i26 = -1;
        int i27 = 0;
        int i28 = 0;
        int i29 = 0;
        int i30 = 1048575;
        while (true) {
            if (i25 < i23) {
                int i31 = i25 + 1;
                byte b = bArr2[i25];
                if (b < 0) {
                    int zza2 = zzhl.zza(b, bArr2, i31, zzhnVar3);
                    i6 = zzhnVar3.zza;
                    i31 = zza2;
                } else {
                    i6 = b;
                }
                int i32 = i6 >>> 3;
                int i33 = i6 & 7;
                int zza3 = i32 > i26 ? zzkoVar2.zza(i32, i27 / 3) : zzkoVar2.zzg(i32);
                if (zza3 == -1) {
                    i7 = i32;
                    i8 = i31;
                    i9 = i6;
                    i10 = i29;
                    unsafe = unsafe3;
                    i4 = i24;
                    i11 = 0;
                } else {
                    int[] iArr = zzkoVar2.zzc;
                    int i34 = iArr[zza3 + 1];
                    int i35 = (i34 & 267386880) >>> 20;
                    int i36 = i6;
                    long j5 = i34 & 1048575;
                    if (i35 <= 17) {
                        int i37 = iArr[zza3 + 2];
                        int i38 = 1 << (i37 >>> 20);
                        int i39 = i37 & 1048575;
                        if (i39 != i30) {
                            if (i30 != 1048575) {
                                unsafe3.putInt(obj5, i30, i29);
                            }
                            i29 = unsafe3.getInt(obj5, i39);
                            i13 = i39;
                        } else {
                            i13 = i30;
                        }
                        int i40 = i29;
                        switch (i35) {
                            case 0:
                                i14 = i32;
                                i15 = i13;
                                i16 = i36;
                                bArr2 = bArr;
                                i17 = zza3;
                                if (i33 == 1) {
                                    zzma.zza(obj5, j5, zzhl.zzc(bArr2, i31));
                                    i25 = i31 + 8;
                                    i29 = i40 | i38;
                                    i30 = i15;
                                    i28 = i16;
                                    i26 = i14;
                                    i27 = i17;
                                    i23 = i2;
                                    i24 = i3;
                                    break;
                                } else {
                                    i8 = i31;
                                    i10 = i40;
                                    i9 = i16;
                                    unsafe = unsafe3;
                                    i11 = i17;
                                    i30 = i15;
                                    i4 = i3;
                                    i7 = i14;
                                    break;
                                }
                            case 1:
                                i14 = i32;
                                i15 = i13;
                                i16 = i36;
                                bArr2 = bArr;
                                i17 = zza3;
                                if (i33 == 5) {
                                    zzma.zza(obj5, j5, zzhl.zzd(bArr2, i31));
                                    i25 = i31 + 4;
                                    i29 = i40 | i38;
                                    i30 = i15;
                                    i28 = i16;
                                    i26 = i14;
                                    i27 = i17;
                                    i23 = i2;
                                    i24 = i3;
                                    break;
                                } else {
                                    i8 = i31;
                                    i10 = i40;
                                    i9 = i16;
                                    unsafe = unsafe3;
                                    i11 = i17;
                                    i30 = i15;
                                    i4 = i3;
                                    i7 = i14;
                                    break;
                                }
                            case 2:
                            case 3:
                                i14 = i32;
                                i15 = i13;
                                i16 = i36;
                                bArr2 = bArr;
                                i17 = zza3;
                                if (i33 == 0) {
                                    zzb2 = zzhl.zzb(bArr2, i31, zzhnVar3);
                                    unsafe2 = unsafe3;
                                    obj3 = obj;
                                    j = j5;
                                    j2 = zzhnVar3.zzb;
                                    unsafe2.putLong(obj3, j, j2);
                                    i29 = i40 | i38;
                                    i30 = i15;
                                    i28 = i16;
                                    i25 = zzb2;
                                    i26 = i14;
                                    i27 = i17;
                                    i23 = i2;
                                    i24 = i3;
                                    break;
                                } else {
                                    i8 = i31;
                                    i10 = i40;
                                    i9 = i16;
                                    unsafe = unsafe3;
                                    i11 = i17;
                                    i30 = i15;
                                    i4 = i3;
                                    i7 = i14;
                                    break;
                                }
                            case 4:
                            case 11:
                                i14 = i32;
                                i15 = i13;
                                j3 = j5;
                                i16 = i36;
                                bArr2 = bArr;
                                i17 = zza3;
                                if (i33 == 0) {
                                    i25 = zzhl.zza(bArr2, i31, zzhnVar3);
                                    i18 = zzhnVar3.zza;
                                    unsafe3.putInt(obj5, j3, i18);
                                    i29 = i40 | i38;
                                    i30 = i15;
                                    i28 = i16;
                                    i26 = i14;
                                    i27 = i17;
                                    i23 = i2;
                                    i24 = i3;
                                    break;
                                } else {
                                    i8 = i31;
                                    i10 = i40;
                                    i9 = i16;
                                    unsafe = unsafe3;
                                    i11 = i17;
                                    i30 = i15;
                                    i4 = i3;
                                    i7 = i14;
                                    break;
                                }
                            case 5:
                            case 14:
                                i14 = i32;
                                i15 = i13;
                                i16 = i36;
                                bArr2 = bArr;
                                i17 = zza3;
                                if (i33 == 1) {
                                    unsafe3.putLong(obj, j5, zzhl.zzb(bArr2, i31));
                                    i25 = i31 + 8;
                                    i29 = i40 | i38;
                                    i30 = i15;
                                    i28 = i16;
                                    i26 = i14;
                                    i27 = i17;
                                    i23 = i2;
                                    i24 = i3;
                                    break;
                                } else {
                                    i8 = i31;
                                    i10 = i40;
                                    i9 = i16;
                                    unsafe = unsafe3;
                                    i11 = i17;
                                    i30 = i15;
                                    i4 = i3;
                                    i7 = i14;
                                    break;
                                }
                            case 6:
                            case 13:
                                i14 = i32;
                                i15 = i13;
                                i16 = i36;
                                bArr2 = bArr;
                                i17 = zza3;
                                i19 = i2;
                                if (i33 == 5) {
                                    unsafe3.putInt(obj5, j5, zzhl.zza(bArr2, i31));
                                    i25 = i31 + 4;
                                    i29 = i40 | i38;
                                    i30 = i15;
                                    i28 = i16;
                                    i26 = i14;
                                    i24 = i3;
                                    int i41 = i17;
                                    i23 = i19;
                                    i27 = i41;
                                    break;
                                } else {
                                    i8 = i31;
                                    i10 = i40;
                                    i9 = i16;
                                    unsafe = unsafe3;
                                    i11 = i17;
                                    i30 = i15;
                                    i4 = i3;
                                    i7 = i14;
                                    break;
                                }
                            case 7:
                                i14 = i32;
                                i15 = i13;
                                i16 = i36;
                                bArr2 = bArr;
                                i17 = zza3;
                                i19 = i2;
                                if (i33 == 0) {
                                    int zzb3 = zzhl.zzb(bArr2, i31, zzhnVar3);
                                    zzma.zza(obj5, j5, zzhnVar3.zzb != 0);
                                    i29 = i40 | i38;
                                    i30 = i15;
                                    i25 = zzb3;
                                    i28 = i16;
                                    i26 = i14;
                                    i24 = i3;
                                    int i412 = i17;
                                    i23 = i19;
                                    i27 = i412;
                                    break;
                                } else {
                                    i8 = i31;
                                    i10 = i40;
                                    i9 = i16;
                                    unsafe = unsafe3;
                                    i11 = i17;
                                    i30 = i15;
                                    i4 = i3;
                                    i7 = i14;
                                    break;
                                }
                            case 8:
                                i14 = i32;
                                i15 = i13;
                                j4 = j5;
                                i16 = i36;
                                bArr2 = bArr;
                                i17 = zza3;
                                i19 = i2;
                                if (i33 == 2) {
                                    i25 = (i34 & 536870912) == 0 ? zzhl.zzc(bArr2, i31, zzhnVar3) : zzhl.zzd(bArr2, i31, zzhnVar3);
                                    obj4 = zzhnVar3.zzc;
                                    unsafe3.putObject(obj5, j4, obj4);
                                    i29 = i40 | i38;
                                    i30 = i15;
                                    i28 = i16;
                                    i26 = i14;
                                    i24 = i3;
                                    int i4122 = i17;
                                    i23 = i19;
                                    i27 = i4122;
                                    break;
                                } else {
                                    i8 = i31;
                                    i10 = i40;
                                    i9 = i16;
                                    unsafe = unsafe3;
                                    i11 = i17;
                                    i30 = i15;
                                    i4 = i3;
                                    i7 = i14;
                                    break;
                                }
                            case 9:
                                i14 = i32;
                                i15 = i13;
                                j4 = j5;
                                i16 = i36;
                                bArr2 = bArr;
                                i17 = zza3;
                                if (i33 == 2) {
                                    i19 = i2;
                                    i25 = zzhl.zza(zzkoVar2.zza(i17), bArr2, i31, i19, zzhnVar3);
                                    obj4 = (i40 & i38) == 0 ? zzhnVar3.zzc : zzjf.zza(unsafe3.getObject(obj5, j4), zzhnVar3.zzc);
                                    unsafe3.putObject(obj5, j4, obj4);
                                    i29 = i40 | i38;
                                    i30 = i15;
                                    i28 = i16;
                                    i26 = i14;
                                    i24 = i3;
                                    int i41222 = i17;
                                    i23 = i19;
                                    i27 = i41222;
                                    break;
                                } else {
                                    i8 = i31;
                                    i10 = i40;
                                    i9 = i16;
                                    unsafe = unsafe3;
                                    i11 = i17;
                                    i30 = i15;
                                    i4 = i3;
                                    i7 = i14;
                                    break;
                                }
                            case 10:
                                i14 = i32;
                                i15 = i13;
                                i16 = i36;
                                bArr2 = bArr;
                                i17 = zza3;
                                if (i33 == 2) {
                                    i25 = zzhl.zze(bArr2, i31, zzhnVar3);
                                    unsafe3.putObject(obj5, j5, zzhnVar3.zzc);
                                    i29 = i40 | i38;
                                    i30 = i15;
                                    i28 = i16;
                                    i26 = i14;
                                    i27 = i17;
                                    i23 = i2;
                                    i24 = i3;
                                    break;
                                } else {
                                    i8 = i31;
                                    i10 = i40;
                                    i9 = i16;
                                    unsafe = unsafe3;
                                    i11 = i17;
                                    i30 = i15;
                                    i4 = i3;
                                    i7 = i14;
                                    break;
                                }
                            case 12:
                                i14 = i32;
                                i15 = i13;
                                j3 = j5;
                                i16 = i36;
                                bArr2 = bArr;
                                i17 = zza3;
                                if (i33 != 0) {
                                    i8 = i31;
                                    i10 = i40;
                                    i9 = i16;
                                    unsafe = unsafe3;
                                    i11 = i17;
                                    i30 = i15;
                                    i4 = i3;
                                    i7 = i14;
                                    break;
                                } else {
                                    i25 = zzhl.zza(bArr2, i31, zzhnVar3);
                                    i18 = zzhnVar3.zza;
                                    zzjg zzc = zzkoVar2.zzc(i17);
                                    if (zzc != null && !zzc.zza(i18)) {
                                        zze(obj).zza(i16, Long.valueOf(i18));
                                        i29 = i40;
                                        i28 = i16;
                                        i26 = i14;
                                        i27 = i17;
                                        i30 = i15;
                                        i23 = i2;
                                        i24 = i3;
                                        break;
                                    }
                                    unsafe3.putInt(obj5, j3, i18);
                                    i29 = i40 | i38;
                                    i30 = i15;
                                    i28 = i16;
                                    i26 = i14;
                                    i27 = i17;
                                    i23 = i2;
                                    i24 = i3;
                                }
                                break;
                            case 15:
                                i14 = i32;
                                i15 = i13;
                                j3 = j5;
                                i16 = i36;
                                bArr2 = bArr;
                                i17 = zza3;
                                if (i33 == 0) {
                                    i25 = zzhl.zza(bArr2, i31, zzhnVar3);
                                    i18 = zzif.zze(zzhnVar3.zza);
                                    unsafe3.putInt(obj5, j3, i18);
                                    i29 = i40 | i38;
                                    i30 = i15;
                                    i28 = i16;
                                    i26 = i14;
                                    i27 = i17;
                                    i23 = i2;
                                    i24 = i3;
                                    break;
                                } else {
                                    i8 = i31;
                                    i10 = i40;
                                    i9 = i16;
                                    unsafe = unsafe3;
                                    i11 = i17;
                                    i30 = i15;
                                    i4 = i3;
                                    i7 = i14;
                                    break;
                                }
                            case 16:
                                i14 = i32;
                                i15 = i13;
                                i16 = i36;
                                if (i33 == 0) {
                                    bArr2 = bArr;
                                    zzb2 = zzhl.zzb(bArr2, i31, zzhnVar3);
                                    unsafe2 = unsafe3;
                                    j = j5;
                                    obj3 = obj;
                                    i17 = zza3;
                                    j2 = zzif.zza(zzhnVar3.zzb);
                                    unsafe2.putLong(obj3, j, j2);
                                    i29 = i40 | i38;
                                    i30 = i15;
                                    i28 = i16;
                                    i25 = zzb2;
                                    i26 = i14;
                                    i27 = i17;
                                    i23 = i2;
                                    i24 = i3;
                                    break;
                                } else {
                                    i17 = zza3;
                                    i8 = i31;
                                    i10 = i40;
                                    i9 = i16;
                                    unsafe = unsafe3;
                                    i11 = i17;
                                    i30 = i15;
                                    i4 = i3;
                                    i7 = i14;
                                    break;
                                }
                            case 17:
                                if (i33 == 3) {
                                    int i42 = i13;
                                    i25 = zzhl.zza(zzkoVar2.zza(zza3), bArr, i31, i2, (i32 << 3) | 4, zzhnVar);
                                    unsafe3.putObject(obj5, j5, (i40 & i38) == 0 ? zzhnVar3.zzc : zzjf.zza(unsafe3.getObject(obj5, j5), zzhnVar3.zzc));
                                    i29 = i40 | i38;
                                    bArr2 = bArr;
                                    i30 = i42;
                                    i23 = i2;
                                    i28 = i36;
                                    i27 = zza3;
                                    i26 = i32;
                                    i24 = i3;
                                    break;
                                } else {
                                    i14 = i32;
                                    i15 = i13;
                                    i16 = i36;
                                    i17 = zza3;
                                    i8 = i31;
                                    i10 = i40;
                                    i9 = i16;
                                    unsafe = unsafe3;
                                    i11 = i17;
                                    i30 = i15;
                                    i4 = i3;
                                    i7 = i14;
                                    break;
                                }
                            default:
                                i14 = i32;
                                i17 = zza3;
                                i15 = i13;
                                i16 = i36;
                                i8 = i31;
                                i10 = i40;
                                i9 = i16;
                                unsafe = unsafe3;
                                i11 = i17;
                                i30 = i15;
                                i4 = i3;
                                i7 = i14;
                                break;
                        }
                    } else {
                        bArr2 = bArr;
                        if (i35 != 27) {
                            i10 = i29;
                            i20 = i30;
                            if (i35 <= 49) {
                                int i43 = i31;
                                i7 = i32;
                                i22 = i36;
                                unsafe = unsafe3;
                                i11 = zza3;
                                i25 = zza(obj, bArr, i31, i2, i36, i7, i33, zza3, i34, i35, j5, zzhnVar);
                                if (i25 == i43) {
                                    i4 = i3;
                                    i8 = i25;
                                    i30 = i20;
                                    i9 = i22;
                                } else {
                                    zzkoVar2 = this;
                                    obj5 = obj;
                                    bArr2 = bArr;
                                    i26 = i7;
                                    i23 = i2;
                                    i24 = i3;
                                    zzhnVar3 = zzhnVar;
                                    i27 = i11;
                                    i29 = i10;
                                    i30 = i20;
                                    i28 = i22;
                                    unsafe3 = unsafe;
                                }
                            } else {
                                i21 = i31;
                                i7 = i32;
                                i22 = i36;
                                unsafe = unsafe3;
                                i11 = zza3;
                                if (i35 != 50) {
                                    i25 = zza(obj, bArr, i21, i2, i22, i7, i33, i34, i35, j5, i11, zzhnVar);
                                    if (i25 != i21) {
                                        zzkoVar2 = this;
                                        obj5 = obj;
                                        bArr2 = bArr;
                                        i23 = i2;
                                        zzhnVar3 = zzhnVar;
                                        i28 = i22;
                                        i26 = i7;
                                        i27 = i11;
                                        i29 = i10;
                                        i30 = i20;
                                        unsafe3 = unsafe;
                                        i24 = i3;
                                    }
                                } else if (i33 == 2) {
                                    i25 = zza(obj, bArr, i21, i2, i11, j5, zzhnVar);
                                    if (i25 != i21) {
                                        zzkoVar2 = this;
                                        obj5 = obj;
                                        bArr2 = bArr;
                                        i26 = i7;
                                        i23 = i2;
                                        i24 = i3;
                                        zzhnVar3 = zzhnVar;
                                        i27 = i11;
                                        i29 = i10;
                                        i30 = i20;
                                        i28 = i22;
                                        unsafe3 = unsafe;
                                    }
                                }
                                i4 = i3;
                                i8 = i25;
                                i30 = i20;
                                i9 = i22;
                            }
                        } else if (i33 == 2) {
                            zzjl zzjlVar = (zzjl) unsafe3.getObject(obj5, j5);
                            if (!zzjlVar.zza()) {
                                int size = zzjlVar.size();
                                zzjlVar = zzjlVar.zza(size == 0 ? 10 : size << 1);
                                unsafe3.putObject(obj5, j5, zzjlVar);
                            }
                            i25 = zzhl.zza(zzkoVar2.zza(zza3), i36, bArr, i31, i2, zzjlVar, zzhnVar);
                            i28 = i36;
                            i26 = i32;
                            i27 = zza3;
                            i29 = i29;
                            i30 = i30;
                            i23 = i2;
                            i24 = i3;
                        } else {
                            i10 = i29;
                            i20 = i30;
                            i21 = i31;
                            i7 = i32;
                            i22 = i36;
                            unsafe = unsafe3;
                            i11 = zza3;
                        }
                        i4 = i3;
                        i8 = i21;
                        i30 = i20;
                        i9 = i22;
                    }
                }
                if (i9 != i4 || i4 == 0) {
                    if (this.zzh) {
                        zzhnVar2 = zzhnVar;
                        if (zzhnVar2.zzd != zzio.zzb()) {
                            i12 = i7;
                            zzhnVar2.zzd.zza(this.zzg, i12);
                            i25 = zzhl.zza(i9, bArr, i8, i2, zze(obj), zzhnVar);
                            obj5 = obj;
                            bArr2 = bArr;
                            i23 = i2;
                            i28 = i9;
                            zzkoVar2 = this;
                            zzhnVar3 = zzhnVar2;
                            i26 = i12;
                            i27 = i11;
                            i29 = i10;
                            unsafe3 = unsafe;
                            i24 = i4;
                        } else {
                            i12 = i7;
                        }
                    } else {
                        i12 = i7;
                        zzhnVar2 = zzhnVar;
                    }
                    i25 = zzhl.zza(i9, bArr, i8, i2, zze(obj), zzhnVar);
                    obj5 = obj;
                    bArr2 = bArr;
                    i23 = i2;
                    i28 = i9;
                    zzkoVar2 = this;
                    zzhnVar3 = zzhnVar2;
                    i26 = i12;
                    i27 = i11;
                    i29 = i10;
                    unsafe3 = unsafe;
                    i24 = i4;
                } else {
                    i5 = 1048575;
                    zzkoVar = this;
                    i25 = i8;
                    i28 = i9;
                    i29 = i10;
                }
            } else {
                unsafe = unsafe3;
                i4 = i24;
                zzkoVar = zzkoVar2;
                i5 = 1048575;
            }
        }
        if (i30 != i5) {
            obj2 = obj;
            unsafe.putInt(obj2, i30, i29);
        } else {
            obj2 = obj;
        }
        zzlx zzlxVar = null;
        for (int i44 = zzkoVar.zzm; i44 < zzkoVar.zzn; i44++) {
            zzlxVar = (zzlx) zzkoVar.zza(obj2, zzkoVar.zzl[i44], zzlxVar, zzkoVar.zzq);
        }
        if (zzlxVar != null) {
            zzkoVar.zzq.zzb(obj2, zzlxVar);
        }
        if (i4 == 0) {
            if (i25 != i2) {
                throw zzjk.zzg();
            }
        } else if (i25 > i2 || i28 != i4) {
            throw zzjk.zzg();
        }
        return i25;
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final Object zza() {
        return this.zzo.zza(this.zzg);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x003b  */
    /* JADX WARN: Removed duplicated region for block: B:269:0x04c3  */
    /* JADX WARN: Removed duplicated region for block: B:291:0x0502  */
    /* JADX WARN: Removed duplicated region for block: B:550:0x098a  */
    @Override // com.google.android.gms.internal.vision.zzlc
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void zza(Object obj, zzmr zzmrVar) {
        Iterator it;
        Map.Entry entry;
        int length;
        int i;
        double zze;
        float zzd;
        long zzb2;
        long zzb3;
        int zza2;
        long zzb4;
        int zza3;
        boolean zzc;
        int zza4;
        int zza5;
        int zza6;
        long zzb5;
        int zza7;
        long zzb6;
        Iterator it2;
        Map.Entry entry2;
        int length2;
        double zze2;
        float zzd2;
        long zzb7;
        long zzb8;
        int zza8;
        long zzb9;
        int zza9;
        boolean zzc2;
        int zza10;
        int zza11;
        int zza12;
        long zzb10;
        int zza13;
        long zzb11;
        if (zzmrVar.zza() == zzmq.zzb) {
            zza(this.zzq, obj, zzmrVar);
            if (this.zzh) {
                zziu zza14 = this.zzr.zza(obj);
                if (!zza14.zza.isEmpty()) {
                    it2 = zza14.zze();
                    entry2 = (Map.Entry) it2.next();
                    for (length2 = this.zzc.length - 3; length2 >= 0; length2 -= 3) {
                        int zzd3 = zzd(length2);
                        int i2 = this.zzc[length2];
                        while (entry2 != null && this.zzr.zza(entry2) > i2) {
                            this.zzr.zza(zzmrVar, entry2);
                            entry2 = it2.hasNext() ? (Map.Entry) it2.next() : null;
                        }
                        switch ((zzd3 & 267386880) >>> 20) {
                            case 0:
                                if (zza(obj, length2)) {
                                    zze2 = zzma.zze(obj, zzd3 & 1048575);
                                    zzmrVar.zza(i2, zze2);
                                    break;
                                } else {
                                    break;
                                }
                            case 1:
                                if (zza(obj, length2)) {
                                    zzd2 = zzma.zzd(obj, zzd3 & 1048575);
                                    zzmrVar.zza(i2, zzd2);
                                    break;
                                } else {
                                    break;
                                }
                            case 2:
                                if (zza(obj, length2)) {
                                    zzb7 = zzma.zzb(obj, zzd3 & 1048575);
                                    zzmrVar.zza(i2, zzb7);
                                    break;
                                } else {
                                    break;
                                }
                            case 3:
                                if (zza(obj, length2)) {
                                    zzb8 = zzma.zzb(obj, zzd3 & 1048575);
                                    zzmrVar.zzc(i2, zzb8);
                                    break;
                                } else {
                                    break;
                                }
                            case 4:
                                if (zza(obj, length2)) {
                                    zza8 = zzma.zza(obj, zzd3 & 1048575);
                                    zzmrVar.zzc(i2, zza8);
                                    break;
                                } else {
                                    break;
                                }
                            case 5:
                                if (zza(obj, length2)) {
                                    zzb9 = zzma.zzb(obj, zzd3 & 1048575);
                                    zzmrVar.zzd(i2, zzb9);
                                    break;
                                } else {
                                    break;
                                }
                            case 6:
                                if (zza(obj, length2)) {
                                    zza9 = zzma.zza(obj, zzd3 & 1048575);
                                    zzmrVar.zzd(i2, zza9);
                                    break;
                                } else {
                                    break;
                                }
                            case 7:
                                if (zza(obj, length2)) {
                                    zzc2 = zzma.zzc(obj, zzd3 & 1048575);
                                    zzmrVar.zza(i2, zzc2);
                                    break;
                                } else {
                                    break;
                                }
                            case 8:
                                if (!zza(obj, length2)) {
                                    break;
                                }
                                zza(i2, zzma.zzf(obj, zzd3 & 1048575), zzmrVar);
                                break;
                            case 9:
                                if (!zza(obj, length2)) {
                                    break;
                                }
                                zzmrVar.zza(i2, zzma.zzf(obj, zzd3 & 1048575), zza(length2));
                                break;
                            case 10:
                                if (!zza(obj, length2)) {
                                    break;
                                }
                                zzmrVar.zza(i2, (zzht) zzma.zzf(obj, zzd3 & 1048575));
                                break;
                            case 11:
                                if (zza(obj, length2)) {
                                    zza10 = zzma.zza(obj, zzd3 & 1048575);
                                    zzmrVar.zze(i2, zza10);
                                    break;
                                } else {
                                    break;
                                }
                            case 12:
                                if (zza(obj, length2)) {
                                    zza11 = zzma.zza(obj, zzd3 & 1048575);
                                    zzmrVar.zzb(i2, zza11);
                                    break;
                                } else {
                                    break;
                                }
                            case 13:
                                if (zza(obj, length2)) {
                                    zza12 = zzma.zza(obj, zzd3 & 1048575);
                                    zzmrVar.zza(i2, zza12);
                                    break;
                                } else {
                                    break;
                                }
                            case 14:
                                if (zza(obj, length2)) {
                                    zzb10 = zzma.zzb(obj, zzd3 & 1048575);
                                    zzmrVar.zzb(i2, zzb10);
                                    break;
                                } else {
                                    break;
                                }
                            case 15:
                                if (zza(obj, length2)) {
                                    zza13 = zzma.zza(obj, zzd3 & 1048575);
                                    zzmrVar.zzf(i2, zza13);
                                    break;
                                } else {
                                    break;
                                }
                            case 16:
                                if (zza(obj, length2)) {
                                    zzb11 = zzma.zzb(obj, zzd3 & 1048575);
                                    zzmrVar.zze(i2, zzb11);
                                    break;
                                } else {
                                    break;
                                }
                            case 17:
                                if (!zza(obj, length2)) {
                                    break;
                                }
                                zzmrVar.zzb(i2, zzma.zzf(obj, zzd3 & 1048575), zza(length2));
                                break;
                            case 18:
                                zzle.zza(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, false);
                                break;
                            case 19:
                                zzle.zzb(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, false);
                                break;
                            case 20:
                                zzle.zzc(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, false);
                                break;
                            case 21:
                                zzle.zzd(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, false);
                                break;
                            case 22:
                                zzle.zzh(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, false);
                                break;
                            case 23:
                                zzle.zzf(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, false);
                                break;
                            case 24:
                                zzle.zzk(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, false);
                                break;
                            case 25:
                                zzle.zzn(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, false);
                                break;
                            case 26:
                                zzle.zza(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar);
                                break;
                            case 27:
                                zzle.zza(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, zza(length2));
                                break;
                            case 28:
                                zzle.zzb(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar);
                                break;
                            case 29:
                                zzle.zzi(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, false);
                                break;
                            case 30:
                                zzle.zzm(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, false);
                                break;
                            case 31:
                                zzle.zzl(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, false);
                                break;
                            case 32:
                                zzle.zzg(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, false);
                                break;
                            case 33:
                                zzle.zzj(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, false);
                                break;
                            case 34:
                                zzle.zze(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, false);
                                break;
                            case 35:
                                zzle.zza(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, true);
                                break;
                            case 36:
                                zzle.zzb(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, true);
                                break;
                            case 37:
                                zzle.zzc(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, true);
                                break;
                            case 38:
                                zzle.zzd(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, true);
                                break;
                            case 39:
                                zzle.zzh(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, true);
                                break;
                            case 40:
                                zzle.zzf(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, true);
                                break;
                            case 41:
                                zzle.zzk(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, true);
                                break;
                            case 42:
                                zzle.zzn(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, true);
                                break;
                            case 43:
                                zzle.zzi(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, true);
                                break;
                            case 44:
                                zzle.zzm(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, true);
                                break;
                            case 45:
                                zzle.zzl(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, true);
                                break;
                            case 46:
                                zzle.zzg(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, true);
                                break;
                            case 47:
                                zzle.zzj(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, true);
                                break;
                            case 48:
                                zzle.zze(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, true);
                                break;
                            case 49:
                                zzle.zzb(this.zzc[length2], (List) zzma.zzf(obj, zzd3 & 1048575), zzmrVar, zza(length2));
                                break;
                            case 50:
                                zza(zzmrVar, i2, zzma.zzf(obj, zzd3 & 1048575), length2);
                                break;
                            case 51:
                                if (zza(obj, i2, length2)) {
                                    zze2 = zzb(obj, zzd3 & 1048575);
                                    zzmrVar.zza(i2, zze2);
                                    break;
                                } else {
                                    break;
                                }
                            case 52:
                                if (zza(obj, i2, length2)) {
                                    zzd2 = zzc(obj, zzd3 & 1048575);
                                    zzmrVar.zza(i2, zzd2);
                                    break;
                                } else {
                                    break;
                                }
                            case 53:
                                if (zza(obj, i2, length2)) {
                                    zzb7 = zze(obj, zzd3 & 1048575);
                                    zzmrVar.zza(i2, zzb7);
                                    break;
                                } else {
                                    break;
                                }
                            case 54:
                                if (zza(obj, i2, length2)) {
                                    zzb8 = zze(obj, zzd3 & 1048575);
                                    zzmrVar.zzc(i2, zzb8);
                                    break;
                                } else {
                                    break;
                                }
                            case 55:
                                if (zza(obj, i2, length2)) {
                                    zza8 = zzd(obj, zzd3 & 1048575);
                                    zzmrVar.zzc(i2, zza8);
                                    break;
                                } else {
                                    break;
                                }
                            case 56:
                                if (zza(obj, i2, length2)) {
                                    zzb9 = zze(obj, zzd3 & 1048575);
                                    zzmrVar.zzd(i2, zzb9);
                                    break;
                                } else {
                                    break;
                                }
                            case 57:
                                if (zza(obj, i2, length2)) {
                                    zza9 = zzd(obj, zzd3 & 1048575);
                                    zzmrVar.zzd(i2, zza9);
                                    break;
                                } else {
                                    break;
                                }
                            case 58:
                                if (zza(obj, i2, length2)) {
                                    zzc2 = zzf(obj, zzd3 & 1048575);
                                    zzmrVar.zza(i2, zzc2);
                                    break;
                                } else {
                                    break;
                                }
                            case 59:
                                if (!zza(obj, i2, length2)) {
                                    break;
                                }
                                zza(i2, zzma.zzf(obj, zzd3 & 1048575), zzmrVar);
                                break;
                            case 60:
                                if (!zza(obj, i2, length2)) {
                                    break;
                                }
                                zzmrVar.zza(i2, zzma.zzf(obj, zzd3 & 1048575), zza(length2));
                                break;
                            case 61:
                                if (!zza(obj, i2, length2)) {
                                    break;
                                }
                                zzmrVar.zza(i2, (zzht) zzma.zzf(obj, zzd3 & 1048575));
                                break;
                            case 62:
                                if (zza(obj, i2, length2)) {
                                    zza10 = zzd(obj, zzd3 & 1048575);
                                    zzmrVar.zze(i2, zza10);
                                    break;
                                } else {
                                    break;
                                }
                            case 63:
                                if (zza(obj, i2, length2)) {
                                    zza11 = zzd(obj, zzd3 & 1048575);
                                    zzmrVar.zzb(i2, zza11);
                                    break;
                                } else {
                                    break;
                                }
                            case 64:
                                if (zza(obj, i2, length2)) {
                                    zza12 = zzd(obj, zzd3 & 1048575);
                                    zzmrVar.zza(i2, zza12);
                                    break;
                                } else {
                                    break;
                                }
                            case 65:
                                if (zza(obj, i2, length2)) {
                                    zzb10 = zze(obj, zzd3 & 1048575);
                                    zzmrVar.zzb(i2, zzb10);
                                    break;
                                } else {
                                    break;
                                }
                            case 66:
                                if (zza(obj, i2, length2)) {
                                    zza13 = zzd(obj, zzd3 & 1048575);
                                    zzmrVar.zzf(i2, zza13);
                                    break;
                                } else {
                                    break;
                                }
                            case 67:
                                if (zza(obj, i2, length2)) {
                                    zzb11 = zze(obj, zzd3 & 1048575);
                                    zzmrVar.zze(i2, zzb11);
                                    break;
                                } else {
                                    break;
                                }
                            case 68:
                                if (!zza(obj, i2, length2)) {
                                    break;
                                }
                                zzmrVar.zzb(i2, zzma.zzf(obj, zzd3 & 1048575), zza(length2));
                                break;
                        }
                    }
                    while (entry2 != null) {
                        this.zzr.zza(zzmrVar, entry2);
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
        if (!this.zzj) {
            zzb(obj, zzmrVar);
            return;
        }
        if (this.zzh) {
            zziu zza15 = this.zzr.zza(obj);
            if (!zza15.zza.isEmpty()) {
                it = zza15.zzd();
                entry = (Map.Entry) it.next();
                length = this.zzc.length;
                for (i = 0; i < length; i += 3) {
                    int zzd4 = zzd(i);
                    int i3 = this.zzc[i];
                    while (entry != null && this.zzr.zza(entry) <= i3) {
                        this.zzr.zza(zzmrVar, entry);
                        entry = it.hasNext() ? (Map.Entry) it.next() : null;
                    }
                    switch ((zzd4 & 267386880) >>> 20) {
                        case 0:
                            if (zza(obj, i)) {
                                zze = zzma.zze(obj, zzd4 & 1048575);
                                zzmrVar.zza(i3, zze);
                                break;
                            } else {
                                break;
                            }
                        case 1:
                            if (zza(obj, i)) {
                                zzd = zzma.zzd(obj, zzd4 & 1048575);
                                zzmrVar.zza(i3, zzd);
                                break;
                            } else {
                                break;
                            }
                        case 2:
                            if (zza(obj, i)) {
                                zzb2 = zzma.zzb(obj, zzd4 & 1048575);
                                zzmrVar.zza(i3, zzb2);
                                break;
                            } else {
                                break;
                            }
                        case 3:
                            if (zza(obj, i)) {
                                zzb3 = zzma.zzb(obj, zzd4 & 1048575);
                                zzmrVar.zzc(i3, zzb3);
                                break;
                            } else {
                                break;
                            }
                        case 4:
                            if (zza(obj, i)) {
                                zza2 = zzma.zza(obj, zzd4 & 1048575);
                                zzmrVar.zzc(i3, zza2);
                                break;
                            } else {
                                break;
                            }
                        case 5:
                            if (zza(obj, i)) {
                                zzb4 = zzma.zzb(obj, zzd4 & 1048575);
                                zzmrVar.zzd(i3, zzb4);
                                break;
                            } else {
                                break;
                            }
                        case 6:
                            if (zza(obj, i)) {
                                zza3 = zzma.zza(obj, zzd4 & 1048575);
                                zzmrVar.zzd(i3, zza3);
                                break;
                            } else {
                                break;
                            }
                        case 7:
                            if (zza(obj, i)) {
                                zzc = zzma.zzc(obj, zzd4 & 1048575);
                                zzmrVar.zza(i3, zzc);
                                break;
                            } else {
                                break;
                            }
                        case 8:
                            if (!zza(obj, i)) {
                                break;
                            }
                            zza(i3, zzma.zzf(obj, zzd4 & 1048575), zzmrVar);
                            break;
                        case 9:
                            if (!zza(obj, i)) {
                                break;
                            }
                            zzmrVar.zza(i3, zzma.zzf(obj, zzd4 & 1048575), zza(i));
                            break;
                        case 10:
                            if (!zza(obj, i)) {
                                break;
                            }
                            zzmrVar.zza(i3, (zzht) zzma.zzf(obj, zzd4 & 1048575));
                            break;
                        case 11:
                            if (zza(obj, i)) {
                                zza4 = zzma.zza(obj, zzd4 & 1048575);
                                zzmrVar.zze(i3, zza4);
                                break;
                            } else {
                                break;
                            }
                        case 12:
                            if (zza(obj, i)) {
                                zza5 = zzma.zza(obj, zzd4 & 1048575);
                                zzmrVar.zzb(i3, zza5);
                                break;
                            } else {
                                break;
                            }
                        case 13:
                            if (zza(obj, i)) {
                                zza6 = zzma.zza(obj, zzd4 & 1048575);
                                zzmrVar.zza(i3, zza6);
                                break;
                            } else {
                                break;
                            }
                        case 14:
                            if (zza(obj, i)) {
                                zzb5 = zzma.zzb(obj, zzd4 & 1048575);
                                zzmrVar.zzb(i3, zzb5);
                                break;
                            } else {
                                break;
                            }
                        case 15:
                            if (zza(obj, i)) {
                                zza7 = zzma.zza(obj, zzd4 & 1048575);
                                zzmrVar.zzf(i3, zza7);
                                break;
                            } else {
                                break;
                            }
                        case 16:
                            if (zza(obj, i)) {
                                zzb6 = zzma.zzb(obj, zzd4 & 1048575);
                                zzmrVar.zze(i3, zzb6);
                                break;
                            } else {
                                break;
                            }
                        case 17:
                            if (!zza(obj, i)) {
                                break;
                            }
                            zzmrVar.zzb(i3, zzma.zzf(obj, zzd4 & 1048575), zza(i));
                            break;
                        case 18:
                            zzle.zza(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, false);
                            break;
                        case 19:
                            zzle.zzb(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, false);
                            break;
                        case 20:
                            zzle.zzc(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, false);
                            break;
                        case 21:
                            zzle.zzd(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, false);
                            break;
                        case 22:
                            zzle.zzh(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, false);
                            break;
                        case 23:
                            zzle.zzf(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, false);
                            break;
                        case 24:
                            zzle.zzk(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, false);
                            break;
                        case 25:
                            zzle.zzn(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, false);
                            break;
                        case 26:
                            zzle.zza(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar);
                            break;
                        case 27:
                            zzle.zza(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, zza(i));
                            break;
                        case 28:
                            zzle.zzb(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar);
                            break;
                        case 29:
                            zzle.zzi(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, false);
                            break;
                        case 30:
                            zzle.zzm(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, false);
                            break;
                        case 31:
                            zzle.zzl(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, false);
                            break;
                        case 32:
                            zzle.zzg(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, false);
                            break;
                        case 33:
                            zzle.zzj(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, false);
                            break;
                        case 34:
                            zzle.zze(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, false);
                            break;
                        case 35:
                            zzle.zza(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, true);
                            break;
                        case 36:
                            zzle.zzb(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, true);
                            break;
                        case 37:
                            zzle.zzc(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, true);
                            break;
                        case 38:
                            zzle.zzd(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, true);
                            break;
                        case 39:
                            zzle.zzh(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, true);
                            break;
                        case 40:
                            zzle.zzf(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, true);
                            break;
                        case 41:
                            zzle.zzk(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, true);
                            break;
                        case 42:
                            zzle.zzn(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, true);
                            break;
                        case 43:
                            zzle.zzi(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, true);
                            break;
                        case 44:
                            zzle.zzm(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, true);
                            break;
                        case 45:
                            zzle.zzl(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, true);
                            break;
                        case 46:
                            zzle.zzg(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, true);
                            break;
                        case 47:
                            zzle.zzj(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, true);
                            break;
                        case 48:
                            zzle.zze(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, true);
                            break;
                        case 49:
                            zzle.zzb(this.zzc[i], (List) zzma.zzf(obj, zzd4 & 1048575), zzmrVar, zza(i));
                            break;
                        case 50:
                            zza(zzmrVar, i3, zzma.zzf(obj, zzd4 & 1048575), i);
                            break;
                        case 51:
                            if (zza(obj, i3, i)) {
                                zze = zzb(obj, zzd4 & 1048575);
                                zzmrVar.zza(i3, zze);
                                break;
                            } else {
                                break;
                            }
                        case 52:
                            if (zza(obj, i3, i)) {
                                zzd = zzc(obj, zzd4 & 1048575);
                                zzmrVar.zza(i3, zzd);
                                break;
                            } else {
                                break;
                            }
                        case 53:
                            if (zza(obj, i3, i)) {
                                zzb2 = zze(obj, zzd4 & 1048575);
                                zzmrVar.zza(i3, zzb2);
                                break;
                            } else {
                                break;
                            }
                        case 54:
                            if (zza(obj, i3, i)) {
                                zzb3 = zze(obj, zzd4 & 1048575);
                                zzmrVar.zzc(i3, zzb3);
                                break;
                            } else {
                                break;
                            }
                        case 55:
                            if (zza(obj, i3, i)) {
                                zza2 = zzd(obj, zzd4 & 1048575);
                                zzmrVar.zzc(i3, zza2);
                                break;
                            } else {
                                break;
                            }
                        case 56:
                            if (zza(obj, i3, i)) {
                                zzb4 = zze(obj, zzd4 & 1048575);
                                zzmrVar.zzd(i3, zzb4);
                                break;
                            } else {
                                break;
                            }
                        case 57:
                            if (zza(obj, i3, i)) {
                                zza3 = zzd(obj, zzd4 & 1048575);
                                zzmrVar.zzd(i3, zza3);
                                break;
                            } else {
                                break;
                            }
                        case 58:
                            if (zza(obj, i3, i)) {
                                zzc = zzf(obj, zzd4 & 1048575);
                                zzmrVar.zza(i3, zzc);
                                break;
                            } else {
                                break;
                            }
                        case 59:
                            if (!zza(obj, i3, i)) {
                                break;
                            }
                            zza(i3, zzma.zzf(obj, zzd4 & 1048575), zzmrVar);
                            break;
                        case 60:
                            if (!zza(obj, i3, i)) {
                                break;
                            }
                            zzmrVar.zza(i3, zzma.zzf(obj, zzd4 & 1048575), zza(i));
                            break;
                        case 61:
                            if (!zza(obj, i3, i)) {
                                break;
                            }
                            zzmrVar.zza(i3, (zzht) zzma.zzf(obj, zzd4 & 1048575));
                            break;
                        case 62:
                            if (zza(obj, i3, i)) {
                                zza4 = zzd(obj, zzd4 & 1048575);
                                zzmrVar.zze(i3, zza4);
                                break;
                            } else {
                                break;
                            }
                        case 63:
                            if (zza(obj, i3, i)) {
                                zza5 = zzd(obj, zzd4 & 1048575);
                                zzmrVar.zzb(i3, zza5);
                                break;
                            } else {
                                break;
                            }
                        case 64:
                            if (zza(obj, i3, i)) {
                                zza6 = zzd(obj, zzd4 & 1048575);
                                zzmrVar.zza(i3, zza6);
                                break;
                            } else {
                                break;
                            }
                        case 65:
                            if (zza(obj, i3, i)) {
                                zzb5 = zze(obj, zzd4 & 1048575);
                                zzmrVar.zzb(i3, zzb5);
                                break;
                            } else {
                                break;
                            }
                        case 66:
                            if (zza(obj, i3, i)) {
                                zza7 = zzd(obj, zzd4 & 1048575);
                                zzmrVar.zzf(i3, zza7);
                                break;
                            } else {
                                break;
                            }
                        case 67:
                            if (zza(obj, i3, i)) {
                                zzb6 = zze(obj, zzd4 & 1048575);
                                zzmrVar.zze(i3, zzb6);
                                break;
                            } else {
                                break;
                            }
                        case 68:
                            if (!zza(obj, i3, i)) {
                                break;
                            }
                            zzmrVar.zzb(i3, zzma.zzf(obj, zzd4 & 1048575), zza(i));
                            break;
                    }
                }
                while (entry != null) {
                    this.zzr.zza(zzmrVar, entry);
                    entry = it.hasNext() ? (Map.Entry) it.next() : null;
                }
                zza(this.zzq, obj, zzmrVar);
            }
        }
        it = null;
        entry = null;
        length = this.zzc.length;
        while (i < length) {
        }
        while (entry != null) {
        }
        zza(this.zzq, obj, zzmrVar);
    }

    /* JADX WARN: Code restructure failed: missing block: B:109:0x02ca, code lost:
    
        if (r0 == r5) goto L107;
     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x02cc, code lost:
    
        r2 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x02d3, code lost:
    
        r15 = r30;
        r14 = r31;
        r12 = r32;
        r13 = r34;
        r11 = r35;
        r2 = r18;
        r1 = r25;
        r6 = r27;
        r7 = r28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x0318, code lost:
    
        if (r0 == r15) goto L107;
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x0335, code lost:
    
        if (r0 == r15) goto L107;
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:29:0x0095. Please report as an issue. */
    @Override // com.google.android.gms.internal.vision.zzlc
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void zza(Object obj, byte[] bArr, int i, int i2, zzhn zzhnVar) {
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        Unsafe unsafe;
        int i10;
        int i11;
        Unsafe unsafe2;
        int i12;
        int i13;
        Unsafe unsafe3;
        Object zza2;
        int i14;
        Unsafe unsafe4;
        zzko zzkoVar = this;
        Object obj2 = obj;
        byte[] bArr2 = bArr;
        int i15 = i2;
        zzhn zzhnVar2 = zzhnVar;
        if (!zzkoVar.zzj) {
            zza(obj, bArr, i, i2, 0, zzhnVar);
            return;
        }
        Unsafe unsafe5 = zzb;
        int i16 = -1;
        int i17 = 1048575;
        int i18 = i;
        int i19 = -1;
        int i20 = 0;
        int i21 = 0;
        int i22 = 1048575;
        while (i18 < i15) {
            int i23 = i18 + 1;
            byte b = bArr2[i18];
            if (b < 0) {
                i4 = zzhl.zza(b, bArr2, i23, zzhnVar2);
                i3 = zzhnVar2.zza;
            } else {
                i3 = b;
                i4 = i23;
            }
            int i24 = i3 >>> 3;
            int i25 = i3 & 7;
            int zza3 = i24 > i19 ? zzkoVar.zza(i24, i20 / 3) : zzkoVar.zzg(i24);
            if (zza3 == i16) {
                i11 = i4;
                i5 = i24;
                unsafe = unsafe5;
                i7 = 0;
            } else {
                int[] iArr = zzkoVar.zzc;
                int i26 = iArr[zza3 + 1];
                int i27 = (i26 & 267386880) >>> 20;
                Unsafe unsafe6 = unsafe5;
                long j = i26 & i17;
                if (i27 <= 17) {
                    int i28 = iArr[zza3 + 2];
                    int i29 = 1 << (i28 >>> 20);
                    int i30 = i28 & 1048575;
                    if (i30 != i22) {
                        if (i22 != 1048575) {
                            long j2 = i22;
                            unsafe4 = unsafe6;
                            unsafe4.putInt(obj2, j2, i21);
                        } else {
                            unsafe4 = unsafe6;
                        }
                        if (i30 != 1048575) {
                            i21 = unsafe4.getInt(obj2, i30);
                        }
                        unsafe2 = unsafe4;
                        i22 = i30;
                    } else {
                        unsafe2 = unsafe6;
                    }
                    switch (i27) {
                        case 0:
                            i5 = i24;
                            i12 = i4;
                            i13 = zza3;
                            i6 = i22;
                            unsafe3 = unsafe2;
                            if (i25 == 1) {
                                zzma.zza(obj2, j, zzhl.zzc(bArr2, i12));
                                i18 = i12 + 8;
                                i21 |= i29;
                                unsafe5 = unsafe3;
                                i20 = i13;
                                break;
                            }
                            i11 = i12;
                            unsafe = unsafe3;
                            i7 = i13;
                            i22 = i6;
                            break;
                        case 1:
                            i5 = i24;
                            i12 = i4;
                            i13 = zza3;
                            i6 = i22;
                            unsafe3 = unsafe2;
                            if (i25 == 5) {
                                zzma.zza(obj2, j, zzhl.zzd(bArr2, i12));
                                i18 = i12 + 4;
                                i21 |= i29;
                                unsafe5 = unsafe3;
                                i20 = i13;
                                break;
                            }
                            i11 = i12;
                            unsafe = unsafe3;
                            i7 = i13;
                            i22 = i6;
                            break;
                        case 2:
                        case 3:
                            i5 = i24;
                            i12 = i4;
                            i13 = zza3;
                            i6 = i22;
                            unsafe3 = unsafe2;
                            if (i25 == 0) {
                                int zzb2 = zzhl.zzb(bArr2, i12, zzhnVar2);
                                unsafe3.putLong(obj, j, zzhnVar2.zzb);
                                i21 |= i29;
                                unsafe5 = unsafe3;
                                i20 = i13;
                                i18 = zzb2;
                                break;
                            }
                            i11 = i12;
                            unsafe = unsafe3;
                            i7 = i13;
                            i22 = i6;
                            break;
                        case 4:
                        case 11:
                            i5 = i24;
                            i12 = i4;
                            i13 = zza3;
                            i6 = i22;
                            unsafe3 = unsafe2;
                            if (i25 == 0) {
                                i18 = zzhl.zza(bArr2, i12, zzhnVar2);
                                i14 = zzhnVar2.zza;
                                unsafe3.putInt(obj2, j, i14);
                                i21 |= i29;
                                unsafe5 = unsafe3;
                                i20 = i13;
                                break;
                            }
                            i11 = i12;
                            unsafe = unsafe3;
                            i7 = i13;
                            i22 = i6;
                            break;
                        case 5:
                        case 14:
                            i5 = i24;
                            i13 = zza3;
                            i6 = i22;
                            unsafe3 = unsafe2;
                            if (i25 == 1) {
                                unsafe3.putLong(obj, j, zzhl.zzb(bArr2, i4));
                                i18 = i4 + 8;
                                i21 |= i29;
                                unsafe5 = unsafe3;
                                i20 = i13;
                                break;
                            }
                            i12 = i4;
                            i11 = i12;
                            unsafe = unsafe3;
                            i7 = i13;
                            i22 = i6;
                            break;
                        case 6:
                        case 13:
                            i5 = i24;
                            i13 = zza3;
                            i6 = i22;
                            unsafe3 = unsafe2;
                            if (i25 == 5) {
                                unsafe3.putInt(obj2, j, zzhl.zza(bArr2, i4));
                                i18 = i4 + 4;
                                i21 |= i29;
                                unsafe5 = unsafe3;
                                i20 = i13;
                                break;
                            }
                            i12 = i4;
                            i11 = i12;
                            unsafe = unsafe3;
                            i7 = i13;
                            i22 = i6;
                            break;
                        case 7:
                            i5 = i24;
                            i13 = zza3;
                            i6 = i22;
                            unsafe3 = unsafe2;
                            if (i25 == 0) {
                                i18 = zzhl.zzb(bArr2, i4, zzhnVar2);
                                zzma.zza(obj2, j, zzhnVar2.zzb != 0);
                                i21 |= i29;
                                unsafe5 = unsafe3;
                                i20 = i13;
                                break;
                            }
                            i12 = i4;
                            i11 = i12;
                            unsafe = unsafe3;
                            i7 = i13;
                            i22 = i6;
                            break;
                        case 8:
                            i5 = i24;
                            i13 = zza3;
                            i6 = i22;
                            unsafe3 = unsafe2;
                            if (i25 == 2) {
                                i18 = (i26 & 536870912) == 0 ? zzhl.zzc(bArr2, i4, zzhnVar2) : zzhl.zzd(bArr2, i4, zzhnVar2);
                                zza2 = zzhnVar2.zzc;
                                unsafe3.putObject(obj2, j, zza2);
                                i21 |= i29;
                                unsafe5 = unsafe3;
                                i20 = i13;
                                break;
                            }
                            i12 = i4;
                            i11 = i12;
                            unsafe = unsafe3;
                            i7 = i13;
                            i22 = i6;
                            break;
                        case 9:
                            i5 = i24;
                            i13 = zza3;
                            i6 = i22;
                            unsafe3 = unsafe2;
                            if (i25 == 2) {
                                i18 = zzhl.zza(zzkoVar.zza(i13), bArr2, i4, i15, zzhnVar2);
                                Object object = unsafe3.getObject(obj2, j);
                                zza2 = object == null ? zzhnVar2.zzc : zzjf.zza(object, zzhnVar2.zzc);
                                unsafe3.putObject(obj2, j, zza2);
                                i21 |= i29;
                                unsafe5 = unsafe3;
                                i20 = i13;
                                break;
                            }
                            i12 = i4;
                            i11 = i12;
                            unsafe = unsafe3;
                            i7 = i13;
                            i22 = i6;
                            break;
                        case 10:
                            i5 = i24;
                            i13 = zza3;
                            i6 = i22;
                            unsafe3 = unsafe2;
                            if (i25 == 2) {
                                i18 = zzhl.zze(bArr2, i4, zzhnVar2);
                                zza2 = zzhnVar2.zzc;
                                unsafe3.putObject(obj2, j, zza2);
                                i21 |= i29;
                                unsafe5 = unsafe3;
                                i20 = i13;
                                break;
                            }
                            i12 = i4;
                            i11 = i12;
                            unsafe = unsafe3;
                            i7 = i13;
                            i22 = i6;
                            break;
                        case 12:
                            i5 = i24;
                            i13 = zza3;
                            i6 = i22;
                            unsafe3 = unsafe2;
                            if (i25 == 0) {
                                i18 = zzhl.zza(bArr2, i4, zzhnVar2);
                                i14 = zzhnVar2.zza;
                                unsafe3.putInt(obj2, j, i14);
                                i21 |= i29;
                                unsafe5 = unsafe3;
                                i20 = i13;
                                break;
                            }
                            i12 = i4;
                            i11 = i12;
                            unsafe = unsafe3;
                            i7 = i13;
                            i22 = i6;
                            break;
                        case 15:
                            i5 = i24;
                            i13 = zza3;
                            i6 = i22;
                            unsafe3 = unsafe2;
                            if (i25 == 0) {
                                i18 = zzhl.zza(bArr2, i4, zzhnVar2);
                                i14 = zzif.zze(zzhnVar2.zza);
                                unsafe3.putInt(obj2, j, i14);
                                i21 |= i29;
                                unsafe5 = unsafe3;
                                i20 = i13;
                                break;
                            }
                            i12 = i4;
                            i11 = i12;
                            unsafe = unsafe3;
                            i7 = i13;
                            i22 = i6;
                            break;
                        case 16:
                            if (i25 != 0) {
                                i5 = i24;
                                i6 = i22;
                                unsafe3 = unsafe2;
                                i12 = i4;
                                i13 = zza3;
                                i11 = i12;
                                unsafe = unsafe3;
                                i7 = i13;
                                i22 = i6;
                                break;
                            } else {
                                int zzb3 = zzhl.zzb(bArr2, i4, zzhnVar2);
                                i6 = i22;
                                i5 = i24;
                                unsafe2.putLong(obj, j, zzif.zza(zzhnVar2.zzb));
                                i21 |= i29;
                                unsafe5 = unsafe2;
                                i20 = zza3;
                                i18 = zzb3;
                                break;
                            }
                        default:
                            i5 = i24;
                            i12 = i4;
                            i13 = zza3;
                            i6 = i22;
                            unsafe3 = unsafe2;
                            i11 = i12;
                            unsafe = unsafe3;
                            i7 = i13;
                            i22 = i6;
                            break;
                    }
                    i17 = 1048575;
                    i16 = -1;
                } else {
                    i5 = i24;
                    int i31 = i4;
                    i6 = i22;
                    if (i27 != 27) {
                        i7 = zza3;
                        if (i27 <= 49) {
                            i9 = i21;
                            i10 = i6;
                            unsafe = unsafe6;
                            i18 = zza(obj, bArr, i31, i2, i3, i5, i25, i7, i26, i27, j, zzhnVar);
                        } else {
                            i8 = i31;
                            i9 = i21;
                            unsafe = unsafe6;
                            i10 = i6;
                            if (i27 != 50) {
                                i18 = zza(obj, bArr, i8, i2, i3, i5, i25, i26, i27, j, i7, zzhnVar);
                            } else if (i25 == 2) {
                                i18 = zza(obj, bArr, i8, i2, i7, j, zzhnVar);
                            }
                        }
                        unsafe5 = unsafe;
                        i17 = 1048575;
                        i16 = -1;
                    } else if (i25 == 2) {
                        zzjl zzjlVar = (zzjl) unsafe6.getObject(obj2, j);
                        if (!zzjlVar.zza()) {
                            int size = zzjlVar.size();
                            zzjlVar = zzjlVar.zza(size == 0 ? 10 : size << 1);
                            unsafe6.putObject(obj2, j, zzjlVar);
                        }
                        i18 = zzhl.zza(zzkoVar.zza(zza3), i3, bArr, i31, i2, zzjlVar, zzhnVar);
                        unsafe5 = unsafe6;
                        i21 = i21;
                        i20 = zza3;
                    } else {
                        i7 = zza3;
                        i8 = i31;
                        i9 = i21;
                        unsafe = unsafe6;
                        i10 = i6;
                    }
                    i11 = i8;
                    i21 = i9;
                    i22 = i10;
                    i18 = zzhl.zza(i3, bArr, i11, i2, zze(obj), zzhnVar);
                    zzkoVar = this;
                    obj2 = obj;
                    bArr2 = bArr;
                    i15 = i2;
                    zzhnVar2 = zzhnVar;
                    i20 = i7;
                    i19 = i5;
                    unsafe5 = unsafe;
                    i17 = 1048575;
                    i16 = -1;
                }
                i22 = i6;
                i19 = i5;
                i17 = 1048575;
                i16 = -1;
            }
            i18 = zzhl.zza(i3, bArr, i11, i2, zze(obj), zzhnVar);
            zzkoVar = this;
            obj2 = obj;
            bArr2 = bArr;
            i15 = i2;
            zzhnVar2 = zzhnVar;
            i20 = i7;
            i19 = i5;
            unsafe5 = unsafe;
            i17 = 1048575;
            i16 = -1;
        }
        int i32 = i21;
        Unsafe unsafe7 = unsafe5;
        if (i22 != 1048575) {
            unsafe7.putInt(obj, i22, i32);
        }
        if (i18 != i2) {
            throw zzjk.zzg();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x005d, code lost:
    
        if (com.google.android.gms.internal.vision.zzle.zza(com.google.android.gms.internal.vision.zzma.zzf(r10, r6), com.google.android.gms.internal.vision.zzma.zzf(r11, r6)) != false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0070, code lost:
    
        if (com.google.android.gms.internal.vision.zzma.zzb(r10, r6) == com.google.android.gms.internal.vision.zzma.zzb(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0081, code lost:
    
        if (com.google.android.gms.internal.vision.zzma.zza(r10, r6) == com.google.android.gms.internal.vision.zzma.zza(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0094, code lost:
    
        if (com.google.android.gms.internal.vision.zzma.zzb(r10, r6) == com.google.android.gms.internal.vision.zzma.zzb(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00a5, code lost:
    
        if (com.google.android.gms.internal.vision.zzma.zza(r10, r6) == com.google.android.gms.internal.vision.zzma.zza(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00b6, code lost:
    
        if (com.google.android.gms.internal.vision.zzma.zza(r10, r6) == com.google.android.gms.internal.vision.zzma.zza(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00c7, code lost:
    
        if (com.google.android.gms.internal.vision.zzma.zza(r10, r6) == com.google.android.gms.internal.vision.zzma.zza(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00dd, code lost:
    
        if (com.google.android.gms.internal.vision.zzle.zza(com.google.android.gms.internal.vision.zzma.zzf(r10, r6), com.google.android.gms.internal.vision.zzma.zzf(r11, r6)) != false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00f3, code lost:
    
        if (com.google.android.gms.internal.vision.zzle.zza(com.google.android.gms.internal.vision.zzma.zzf(r10, r6), com.google.android.gms.internal.vision.zzma.zzf(r11, r6)) != false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0109, code lost:
    
        if (com.google.android.gms.internal.vision.zzle.zza(com.google.android.gms.internal.vision.zzma.zzf(r10, r6), com.google.android.gms.internal.vision.zzma.zzf(r11, r6)) != false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x011b, code lost:
    
        if (com.google.android.gms.internal.vision.zzma.zzc(r10, r6) == com.google.android.gms.internal.vision.zzma.zzc(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x012d, code lost:
    
        if (com.google.android.gms.internal.vision.zzma.zza(r10, r6) == com.google.android.gms.internal.vision.zzma.zza(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0141, code lost:
    
        if (com.google.android.gms.internal.vision.zzma.zzb(r10, r6) == com.google.android.gms.internal.vision.zzma.zzb(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0153, code lost:
    
        if (com.google.android.gms.internal.vision.zzma.zza(r10, r6) == com.google.android.gms.internal.vision.zzma.zza(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0167, code lost:
    
        if (com.google.android.gms.internal.vision.zzma.zzb(r10, r6) == com.google.android.gms.internal.vision.zzma.zzb(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x017b, code lost:
    
        if (com.google.android.gms.internal.vision.zzma.zzb(r10, r6) == com.google.android.gms.internal.vision.zzma.zzb(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0195, code lost:
    
        if (java.lang.Float.floatToIntBits(com.google.android.gms.internal.vision.zzma.zzd(r10, r6)) == java.lang.Float.floatToIntBits(com.google.android.gms.internal.vision.zzma.zzd(r11, r6))) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x01b1, code lost:
    
        if (java.lang.Double.doubleToLongBits(com.google.android.gms.internal.vision.zzma.zze(r10, r6)) == java.lang.Double.doubleToLongBits(com.google.android.gms.internal.vision.zzma.zze(r11, r6))) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0038, code lost:
    
        if (com.google.android.gms.internal.vision.zzle.zza(com.google.android.gms.internal.vision.zzma.zzf(r10, r6), com.google.android.gms.internal.vision.zzma.zzf(r11, r6)) != false) goto L104;
     */
    @Override // com.google.android.gms.internal.vision.zzlc
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final boolean zza(Object obj, Object obj2) {
        int length = this.zzc.length;
        int i = 0;
        while (true) {
            boolean z = true;
            if (i >= length) {
                if (!this.zzq.zzb(obj).equals(this.zzq.zzb(obj2))) {
                    return false;
                }
                if (this.zzh) {
                    return this.zzr.zza(obj).equals(this.zzr.zza(obj2));
                }
                return true;
            }
            int zzd = zzd(i);
            long j = zzd & 1048575;
            switch ((zzd & 267386880) >>> 20) {
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
                    z = zzle.zza(zzma.zzf(obj, j), zzma.zzf(obj2, j));
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
                    long zze = zze(i) & 1048575;
                    if (zzma.zza(obj, zze) == zzma.zza(obj2, zze)) {
                        break;
                    }
                    z = false;
                    break;
            }
            if (!z) {
                return false;
            }
            i += 3;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:189:0x0324, code lost:
    
        if ((r4 instanceof com.google.android.gms.internal.vision.zzht) != false) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:234:0x03c0, code lost:
    
        if (zza(r19, r15, r3) != false) goto L210;
     */
    /* JADX WARN: Code restructure failed: missing block: B:235:0x03c2, code lost:
    
        r6 = com.google.android.gms.internal.vision.zzii.zzc(r15, (com.google.android.gms.internal.vision.zzkk) r2.getObject(r19, r8), zza(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:246:0x03fb, code lost:
    
        if (zza(r19, r15, r3) != false) goto L224;
     */
    /* JADX WARN: Code restructure failed: missing block: B:247:0x03fd, code lost:
    
        r6 = com.google.android.gms.internal.vision.zzii.zzh(r15, 0L);
     */
    /* JADX WARN: Code restructure failed: missing block: B:249:0x0408, code lost:
    
        if (zza(r19, r15, r3) != false) goto L227;
     */
    /* JADX WARN: Code restructure failed: missing block: B:250:0x040a, code lost:
    
        r8 = com.google.android.gms.internal.vision.zzii.zzj(r15, 0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:261:0x0433, code lost:
    
        if (zza(r19, r15, r3) != false) goto L239;
     */
    /* JADX WARN: Code restructure failed: missing block: B:262:0x0435, code lost:
    
        r6 = r2.getObject(r19, r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:263:0x0439, code lost:
    
        r6 = com.google.android.gms.internal.vision.zzii.zzc(r15, (com.google.android.gms.internal.vision.zzht) r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:265:0x0444, code lost:
    
        if (zza(r19, r15, r3) != false) goto L243;
     */
    /* JADX WARN: Code restructure failed: missing block: B:266:0x0446, code lost:
    
        r6 = com.google.android.gms.internal.vision.zzle.zza(r15, r2.getObject(r19, r8), zza(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:270:0x0460, code lost:
    
        if ((r6 instanceof com.google.android.gms.internal.vision.zzht) != false) goto L240;
     */
    /* JADX WARN: Code restructure failed: missing block: B:271:0x0463, code lost:
    
        r6 = com.google.android.gms.internal.vision.zzii.zzb(r15, (java.lang.String) r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:273:0x046f, code lost:
    
        if (zza(r19, r15, r3) != false) goto L252;
     */
    /* JADX WARN: Code restructure failed: missing block: B:274:0x0471, code lost:
    
        r8 = com.google.android.gms.internal.vision.zzii.zzb(r15, true);
     */
    /* JADX WARN: Code restructure failed: missing block: B:299:0x0506, code lost:
    
        if (r6 > 0) goto L278;
     */
    /* JADX WARN: Code restructure failed: missing block: B:300:0x0508, code lost:
    
        r8 = (com.google.android.gms.internal.vision.zzii.zze(r15) + com.google.android.gms.internal.vision.zzii.zzg(r6)) + r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:302:0x051e, code lost:
    
        if (r6 > 0) goto L278;
     */
    /* JADX WARN: Code restructure failed: missing block: B:304:0x052b, code lost:
    
        if (r6 > 0) goto L278;
     */
    /* JADX WARN: Code restructure failed: missing block: B:306:0x0538, code lost:
    
        if (r6 > 0) goto L278;
     */
    /* JADX WARN: Code restructure failed: missing block: B:308:0x0545, code lost:
    
        if (r6 > 0) goto L278;
     */
    /* JADX WARN: Code restructure failed: missing block: B:310:0x0552, code lost:
    
        if (r6 > 0) goto L278;
     */
    /* JADX WARN: Code restructure failed: missing block: B:312:0x055f, code lost:
    
        if (r6 > 0) goto L278;
     */
    /* JADX WARN: Code restructure failed: missing block: B:314:0x056c, code lost:
    
        if (r6 > 0) goto L278;
     */
    /* JADX WARN: Code restructure failed: missing block: B:316:0x0579, code lost:
    
        if (r6 > 0) goto L278;
     */
    /* JADX WARN: Code restructure failed: missing block: B:318:0x0586, code lost:
    
        if (r6 > 0) goto L278;
     */
    /* JADX WARN: Code restructure failed: missing block: B:320:0x0594, code lost:
    
        if (r6 > 0) goto L278;
     */
    /* JADX WARN: Code restructure failed: missing block: B:322:0x05a2, code lost:
    
        if (r6 > 0) goto L278;
     */
    /* JADX WARN: Code restructure failed: missing block: B:324:0x05b0, code lost:
    
        if (r6 > 0) goto L278;
     */
    /* JADX WARN: Code restructure failed: missing block: B:326:0x05be, code lost:
    
        if (r6 > 0) goto L278;
     */
    /* JADX WARN: Code restructure failed: missing block: B:347:0x067a, code lost:
    
        if ((r12 & r11) != 0) goto L210;
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x0694, code lost:
    
        if ((r12 & r11) != 0) goto L224;
     */
    /* JADX WARN: Code restructure failed: missing block: B:357:0x069a, code lost:
    
        if ((r12 & r11) != 0) goto L227;
     */
    /* JADX WARN: Code restructure failed: missing block: B:365:0x06b4, code lost:
    
        if ((r12 & r11) != 0) goto L239;
     */
    /* JADX WARN: Code restructure failed: missing block: B:367:0x06ba, code lost:
    
        if ((r12 & r11) != 0) goto L243;
     */
    /* JADX WARN: Code restructure failed: missing block: B:371:0x06c8, code lost:
    
        if ((r6 instanceof com.google.android.gms.internal.vision.zzht) != false) goto L240;
     */
    /* JADX WARN: Code restructure failed: missing block: B:373:0x06ce, code lost:
    
        if ((r12 & r11) != 0) goto L252;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00dd, code lost:
    
        if ((r4 instanceof com.google.android.gms.internal.vision.zzht) != false) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00e0, code lost:
    
        r3 = com.google.android.gms.internal.vision.zzii.zzb(r3, (java.lang.String) r4);
     */
    @Override // com.google.android.gms.internal.vision.zzlc
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final int zzb(Object obj) {
        int i;
        long j;
        int zzd;
        Object object;
        int i2;
        int i3;
        int i4;
        long j2;
        int zzi;
        int zza2;
        int zzi2;
        int zze;
        long zzb2;
        long zzb3;
        int zza3;
        Object zzf;
        int zza4;
        int zza5;
        int zza6;
        long zzb4;
        int zza7;
        int zzi3;
        int i5 = 267386880;
        int i6 = 1048575;
        int i7 = 1;
        if (!this.zzj) {
            Unsafe unsafe = zzb;
            int i8 = 0;
            int i9 = 0;
            int i10 = 1048575;
            int i11 = 0;
            while (i8 < this.zzc.length) {
                int zzd2 = zzd(i8);
                int[] iArr = this.zzc;
                int i12 = iArr[i8];
                int i13 = (zzd2 & 267386880) >>> 20;
                if (i13 <= 17) {
                    int i14 = iArr[i8 + 2];
                    int i15 = i14 & i6;
                    i = i7 << (i14 >>> 20);
                    if (i15 != i10) {
                        i11 = unsafe.getInt(obj, i15);
                        i10 = i15;
                    }
                } else {
                    i = 0;
                }
                long j3 = zzd2 & i6;
                switch (i13) {
                    case 0:
                        j = 0;
                        if ((i11 & i) != 0) {
                            i9 += zzii.zzb(i12, 0.0d);
                            break;
                        } else {
                            break;
                        }
                    case 1:
                        j = 0;
                        if ((i11 & i) != 0) {
                            i9 += zzii.zzb(i12, 0.0f);
                        }
                        break;
                    case 2:
                        j = 0;
                        if ((i & i11) != 0) {
                            zzd = zzii.zzd(i12, unsafe.getLong(obj, j3));
                            i9 += zzd;
                        }
                        break;
                    case 3:
                        j = 0;
                        if ((i & i11) != 0) {
                            zzd = zzii.zze(i12, unsafe.getLong(obj, j3));
                            i9 += zzd;
                        }
                        break;
                    case 4:
                        j = 0;
                        if ((i & i11) != 0) {
                            zzd = zzii.zzf(i12, unsafe.getInt(obj, j3));
                            i9 += zzd;
                        }
                        break;
                    case 5:
                        j = 0;
                        if ((i11 & i) != 0) {
                            zzd = zzii.zzg(i12, 0L);
                            i9 += zzd;
                        }
                        break;
                    case 6:
                        if ((i11 & i) != 0) {
                            i9 += zzii.zzi(i12, 0);
                        }
                        j = 0;
                        break;
                    case 7:
                        break;
                    case 8:
                        if ((i11 & i) != 0) {
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
                        if ((i11 & i) != 0) {
                            i2 = unsafe.getInt(obj, j3);
                            zzi = zzii.zzg(i12, i2);
                            i9 += zzi;
                        }
                        j = 0;
                        break;
                    case 12:
                        if ((i11 & i) != 0) {
                            i3 = unsafe.getInt(obj, j3);
                            zzi = zzii.zzk(i12, i3);
                            i9 += zzi;
                        }
                        j = 0;
                        break;
                    case 13:
                        break;
                    case 14:
                        break;
                    case 15:
                        if ((i11 & i) != 0) {
                            i4 = unsafe.getInt(obj, j3);
                            zzi = zzii.zzh(i12, i4);
                            i9 += zzi;
                        }
                        j = 0;
                        break;
                    case 16:
                        if ((i11 & i) != 0) {
                            j2 = unsafe.getLong(obj, j3);
                            zzi = zzii.zzf(i12, j2);
                            i9 += zzi;
                        }
                        j = 0;
                        break;
                    case 17:
                        break;
                    case 18:
                        zzi = zzle.zzi(i12, (List) unsafe.getObject(obj, j3), false);
                        i9 += zzi;
                        j = 0;
                        break;
                    case 19:
                    case 24:
                    case 31:
                        zza2 = zzle.zzh(i12, (List) unsafe.getObject(obj, j3), false);
                        i9 += zza2;
                        j = 0;
                        break;
                    case 20:
                        zza2 = zzle.zza(i12, (List) unsafe.getObject(obj, j3), false);
                        i9 += zza2;
                        j = 0;
                        break;
                    case 21:
                        zza2 = zzle.zzb(i12, (List) unsafe.getObject(obj, j3), false);
                        i9 += zza2;
                        j = 0;
                        break;
                    case 22:
                        zza2 = zzle.zze(i12, (List) unsafe.getObject(obj, j3), false);
                        i9 += zza2;
                        j = 0;
                        break;
                    case 23:
                    case 32:
                        zza2 = zzle.zzi(i12, (List) unsafe.getObject(obj, j3), false);
                        i9 += zza2;
                        j = 0;
                        break;
                    case 25:
                        zza2 = zzle.zzj(i12, (List) unsafe.getObject(obj, j3), false);
                        i9 += zza2;
                        j = 0;
                        break;
                    case 26:
                        zzi = zzle.zza(i12, (List) unsafe.getObject(obj, j3));
                        i9 += zzi;
                        j = 0;
                        break;
                    case 27:
                        zzi = zzle.zza(i12, (List) unsafe.getObject(obj, j3), zza(i8));
                        i9 += zzi;
                        j = 0;
                        break;
                    case 28:
                        zzi = zzle.zzb(i12, (List) unsafe.getObject(obj, j3));
                        i9 += zzi;
                        j = 0;
                        break;
                    case 29:
                        zzi = zzle.zzf(i12, (List) unsafe.getObject(obj, j3), false);
                        i9 += zzi;
                        j = 0;
                        break;
                    case 30:
                        zza2 = zzle.zzd(i12, (List) unsafe.getObject(obj, j3), false);
                        i9 += zza2;
                        j = 0;
                        break;
                    case 33:
                        zza2 = zzle.zzg(i12, (List) unsafe.getObject(obj, j3), false);
                        i9 += zza2;
                        j = 0;
                        break;
                    case 34:
                        zza2 = zzle.zzc(i12, (List) unsafe.getObject(obj, j3), false);
                        i9 += zza2;
                        j = 0;
                        break;
                    case 35:
                        zzi2 = zzle.zzi((List) unsafe.getObject(obj, j3));
                        break;
                    case 36:
                        zzi2 = zzle.zzh((List) unsafe.getObject(obj, j3));
                        break;
                    case 37:
                        zzi2 = zzle.zza((List) unsafe.getObject(obj, j3));
                        break;
                    case 38:
                        zzi2 = zzle.zzb((List) unsafe.getObject(obj, j3));
                        break;
                    case 39:
                        zzi2 = zzle.zze((List) unsafe.getObject(obj, j3));
                        break;
                    case 40:
                        zzi2 = zzle.zzi((List) unsafe.getObject(obj, j3));
                        break;
                    case 41:
                        zzi2 = zzle.zzh((List) unsafe.getObject(obj, j3));
                        break;
                    case 42:
                        zzi2 = zzle.zzj((List) unsafe.getObject(obj, j3));
                        break;
                    case 43:
                        zzi2 = zzle.zzf((List) unsafe.getObject(obj, j3));
                        break;
                    case 44:
                        zzi2 = zzle.zzd((List) unsafe.getObject(obj, j3));
                        break;
                    case 45:
                        zzi2 = zzle.zzh((List) unsafe.getObject(obj, j3));
                        break;
                    case 46:
                        zzi2 = zzle.zzi((List) unsafe.getObject(obj, j3));
                        break;
                    case 47:
                        zzi2 = zzle.zzg((List) unsafe.getObject(obj, j3));
                        break;
                    case 48:
                        zzi2 = zzle.zzc((List) unsafe.getObject(obj, j3));
                        break;
                    case 49:
                        zzi = zzle.zzb(i12, (List) unsafe.getObject(obj, j3), zza(i8));
                        i9 += zzi;
                        j = 0;
                        break;
                    case 50:
                        zzi = this.zzs.zza(i12, unsafe.getObject(obj, j3), zzb(i8));
                        i9 += zzi;
                        j = 0;
                        break;
                    case 51:
                        if (zza(obj, i12, i8)) {
                            zzi = zzii.zzb(i12, 0.0d);
                            i9 += zzi;
                        }
                        j = 0;
                        break;
                    case 52:
                        if (zza(obj, i12, i8)) {
                            zze = zzii.zzb(i12, 0.0f);
                            i9 += zze;
                        }
                        j = 0;
                        break;
                    case 53:
                        if (zza(obj, i12, i8)) {
                            zzi = zzii.zzd(i12, zze(obj, j3));
                            i9 += zzi;
                        }
                        j = 0;
                        break;
                    case 54:
                        if (zza(obj, i12, i8)) {
                            zzi = zzii.zze(i12, zze(obj, j3));
                            i9 += zzi;
                        }
                        j = 0;
                        break;
                    case 55:
                        if (zza(obj, i12, i8)) {
                            zzi = zzii.zzf(i12, zzd(obj, j3));
                            i9 += zzi;
                        }
                        j = 0;
                        break;
                    case 56:
                        if (zza(obj, i12, i8)) {
                            zzi = zzii.zzg(i12, 0L);
                            i9 += zzi;
                        }
                        j = 0;
                        break;
                    case 57:
                        if (zza(obj, i12, i8)) {
                            zze = zzii.zzi(i12, 0);
                            i9 += zze;
                        }
                        j = 0;
                        break;
                    case 58:
                        break;
                    case 59:
                        if (zza(obj, i12, i8)) {
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
                        if (zza(obj, i12, i8)) {
                            i2 = zzd(obj, j3);
                            zzi = zzii.zzg(i12, i2);
                            i9 += zzi;
                        }
                        j = 0;
                        break;
                    case 63:
                        if (zza(obj, i12, i8)) {
                            i3 = zzd(obj, j3);
                            zzi = zzii.zzk(i12, i3);
                            i9 += zzi;
                        }
                        j = 0;
                        break;
                    case 64:
                        break;
                    case 65:
                        break;
                    case 66:
                        if (zza(obj, i12, i8)) {
                            i4 = zzd(obj, j3);
                            zzi = zzii.zzh(i12, i4);
                            i9 += zzi;
                        }
                        j = 0;
                        break;
                    case 67:
                        if (zza(obj, i12, i8)) {
                            j2 = zze(obj, j3);
                            zzi = zzii.zzf(i12, j2);
                            i9 += zzi;
                        }
                        j = 0;
                        break;
                    case 68:
                        break;
                    default:
                        j = 0;
                        break;
                }
                i8 += 3;
                i6 = 1048575;
                i7 = 1;
            }
            int i16 = 0;
            int zza8 = i9 + zza(this.zzq, obj);
            if (!this.zzh) {
                return zza8;
            }
            zziu zza9 = this.zzr.zza(obj);
            for (int i17 = 0; i17 < zza9.zza.zzc(); i17++) {
                Map.Entry zzb5 = zza9.zza.zzb(i17);
                ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzb5.getKey());
                i16 += zziu.zzc(null, zzb5.getValue());
            }
            for (Map.Entry entry : zza9.zza.zzd()) {
                ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(entry.getKey());
                i16 += zziu.zzc(null, entry.getValue());
            }
            return zza8 + i16;
        }
        Unsafe unsafe2 = zzb;
        int i18 = 0;
        int i19 = 0;
        while (i18 < this.zzc.length) {
            int zzd3 = zzd(i18);
            int i20 = (zzd3 & i5) >>> 20;
            int i21 = this.zzc[i18];
            long j4 = zzd3 & 1048575;
            if (i20 >= zziv.zza.zza() && i20 <= zziv.zzb.zza()) {
                int i22 = this.zzc[i18 + 2];
            }
            switch (i20) {
                case 0:
                    if (!zza(obj, i18)) {
                        break;
                    }
                    zza7 = zzii.zzb(i21, 0.0d);
                    i19 += zza7;
                    break;
                case 1:
                    if (!zza(obj, i18)) {
                        break;
                    }
                    zza7 = zzii.zzb(i21, 0.0f);
                    i19 += zza7;
                    break;
                case 2:
                    if (zza(obj, i18)) {
                        zzb2 = zzma.zzb(obj, j4);
                        zza7 = zzii.zzd(i21, zzb2);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 3:
                    if (zza(obj, i18)) {
                        zzb3 = zzma.zzb(obj, j4);
                        zza7 = zzii.zze(i21, zzb3);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 4:
                    if (zza(obj, i18)) {
                        zza3 = zzma.zza(obj, j4);
                        zza7 = zzii.zzf(i21, zza3);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 5:
                    if (!zza(obj, i18)) {
                        break;
                    }
                    zza7 = zzii.zzg(i21, 0L);
                    i19 += zza7;
                    break;
                case 6:
                    if (!zza(obj, i18)) {
                        break;
                    }
                    zza7 = zzii.zzi(i21, 0);
                    i19 += zza7;
                    break;
                case 7:
                    if (!zza(obj, i18)) {
                        break;
                    }
                    zza7 = zzii.zzb(i21, true);
                    i19 += zza7;
                    break;
                case 8:
                    if (zza(obj, i18)) {
                        zzf = zzma.zzf(obj, j4);
                        break;
                    } else {
                        break;
                    }
                case 9:
                    if (!zza(obj, i18)) {
                        break;
                    }
                    zza7 = zzle.zza(i21, zzma.zzf(obj, j4), zza(i18));
                    i19 += zza7;
                    break;
                case 10:
                    if (!zza(obj, i18)) {
                        break;
                    }
                    zzf = zzma.zzf(obj, j4);
                    zza7 = zzii.zzc(i21, (zzht) zzf);
                    i19 += zza7;
                    break;
                case 11:
                    if (zza(obj, i18)) {
                        zza4 = zzma.zza(obj, j4);
                        zza7 = zzii.zzg(i21, zza4);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 12:
                    if (zza(obj, i18)) {
                        zza5 = zzma.zza(obj, j4);
                        zza7 = zzii.zzk(i21, zza5);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 13:
                    if (!zza(obj, i18)) {
                        break;
                    }
                    zza7 = zzii.zzj(i21, 0);
                    i19 += zza7;
                    break;
                case 14:
                    if (!zza(obj, i18)) {
                        break;
                    }
                    zza7 = zzii.zzh(i21, 0L);
                    i19 += zza7;
                    break;
                case 15:
                    if (zza(obj, i18)) {
                        zza6 = zzma.zza(obj, j4);
                        zza7 = zzii.zzh(i21, zza6);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 16:
                    if (zza(obj, i18)) {
                        zzb4 = zzma.zzb(obj, j4);
                        zza7 = zzii.zzf(i21, zzb4);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 17:
                    if (!zza(obj, i18)) {
                        break;
                    }
                    zza7 = zzii.zzc(i21, (zzkk) zzma.zzf(obj, j4), zza(i18));
                    i19 += zza7;
                    break;
                case 18:
                case 23:
                case 32:
                    zza7 = zzle.zzi(i21, zza(obj, j4), false);
                    i19 += zza7;
                    break;
                case 19:
                case 24:
                case 31:
                    zza7 = zzle.zzh(i21, zza(obj, j4), false);
                    i19 += zza7;
                    break;
                case 20:
                    zza7 = zzle.zza(i21, zza(obj, j4), false);
                    i19 += zza7;
                    break;
                case 21:
                    zza7 = zzle.zzb(i21, zza(obj, j4), false);
                    i19 += zza7;
                    break;
                case 22:
                    zza7 = zzle.zze(i21, zza(obj, j4), false);
                    i19 += zza7;
                    break;
                case 25:
                    zza7 = zzle.zzj(i21, zza(obj, j4), false);
                    i19 += zza7;
                    break;
                case 26:
                    zza7 = zzle.zza(i21, zza(obj, j4));
                    i19 += zza7;
                    break;
                case 27:
                    zza7 = zzle.zza(i21, zza(obj, j4), zza(i18));
                    i19 += zza7;
                    break;
                case 28:
                    zza7 = zzle.zzb(i21, zza(obj, j4));
                    i19 += zza7;
                    break;
                case 29:
                    zza7 = zzle.zzf(i21, zza(obj, j4), false);
                    i19 += zza7;
                    break;
                case 30:
                    zza7 = zzle.zzd(i21, zza(obj, j4), false);
                    i19 += zza7;
                    break;
                case 33:
                    zza7 = zzle.zzg(i21, zza(obj, j4), false);
                    i19 += zza7;
                    break;
                case 34:
                    zza7 = zzle.zzc(i21, zza(obj, j4), false);
                    i19 += zza7;
                    break;
                case 35:
                    zzi3 = zzle.zzi((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzii.zze(i21) + zzii.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 36:
                    zzi3 = zzle.zzh((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzii.zze(i21) + zzii.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 37:
                    zzi3 = zzle.zza((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzii.zze(i21) + zzii.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 38:
                    zzi3 = zzle.zzb((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzii.zze(i21) + zzii.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 39:
                    zzi3 = zzle.zze((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzii.zze(i21) + zzii.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 40:
                    zzi3 = zzle.zzi((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzii.zze(i21) + zzii.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 41:
                    zzi3 = zzle.zzh((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzii.zze(i21) + zzii.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 42:
                    zzi3 = zzle.zzj((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzii.zze(i21) + zzii.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 43:
                    zzi3 = zzle.zzf((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzii.zze(i21) + zzii.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 44:
                    zzi3 = zzle.zzd((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzii.zze(i21) + zzii.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 45:
                    zzi3 = zzle.zzh((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzii.zze(i21) + zzii.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 46:
                    zzi3 = zzle.zzi((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzii.zze(i21) + zzii.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 47:
                    zzi3 = zzle.zzg((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzii.zze(i21) + zzii.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 48:
                    zzi3 = zzle.zzc((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzii.zze(i21) + zzii.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 49:
                    zza7 = zzle.zzb(i21, zza(obj, j4), zza(i18));
                    i19 += zza7;
                    break;
                case 50:
                    zza7 = this.zzs.zza(i21, zzma.zzf(obj, j4), zzb(i18));
                    i19 += zza7;
                    break;
                case 51:
                    if (!zza(obj, i21, i18)) {
                        break;
                    }
                    zza7 = zzii.zzb(i21, 0.0d);
                    i19 += zza7;
                    break;
                case 52:
                    if (!zza(obj, i21, i18)) {
                        break;
                    }
                    zza7 = zzii.zzb(i21, 0.0f);
                    i19 += zza7;
                    break;
                case 53:
                    if (zza(obj, i21, i18)) {
                        zzb2 = zze(obj, j4);
                        zza7 = zzii.zzd(i21, zzb2);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 54:
                    if (zza(obj, i21, i18)) {
                        zzb3 = zze(obj, j4);
                        zza7 = zzii.zze(i21, zzb3);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 55:
                    if (zza(obj, i21, i18)) {
                        zza3 = zzd(obj, j4);
                        zza7 = zzii.zzf(i21, zza3);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 56:
                    if (!zza(obj, i21, i18)) {
                        break;
                    }
                    zza7 = zzii.zzg(i21, 0L);
                    i19 += zza7;
                    break;
                case 57:
                    if (!zza(obj, i21, i18)) {
                        break;
                    }
                    zza7 = zzii.zzi(i21, 0);
                    i19 += zza7;
                    break;
                case 58:
                    if (!zza(obj, i21, i18)) {
                        break;
                    }
                    zza7 = zzii.zzb(i21, true);
                    i19 += zza7;
                    break;
                case 59:
                    if (zza(obj, i21, i18)) {
                        zzf = zzma.zzf(obj, j4);
                        break;
                    } else {
                        break;
                    }
                case 60:
                    if (!zza(obj, i21, i18)) {
                        break;
                    }
                    zza7 = zzle.zza(i21, zzma.zzf(obj, j4), zza(i18));
                    i19 += zza7;
                    break;
                case 61:
                    if (!zza(obj, i21, i18)) {
                        break;
                    }
                    zzf = zzma.zzf(obj, j4);
                    zza7 = zzii.zzc(i21, (zzht) zzf);
                    i19 += zza7;
                    break;
                case 62:
                    if (zza(obj, i21, i18)) {
                        zza4 = zzd(obj, j4);
                        zza7 = zzii.zzg(i21, zza4);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 63:
                    if (zza(obj, i21, i18)) {
                        zza5 = zzd(obj, j4);
                        zza7 = zzii.zzk(i21, zza5);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 64:
                    if (!zza(obj, i21, i18)) {
                        break;
                    }
                    zza7 = zzii.zzj(i21, 0);
                    i19 += zza7;
                    break;
                case 65:
                    if (!zza(obj, i21, i18)) {
                        break;
                    }
                    zza7 = zzii.zzh(i21, 0L);
                    i19 += zza7;
                    break;
                case 66:
                    if (zza(obj, i21, i18)) {
                        zza6 = zzd(obj, j4);
                        zza7 = zzii.zzh(i21, zza6);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 67:
                    if (zza(obj, i21, i18)) {
                        zzb4 = zze(obj, j4);
                        zza7 = zzii.zzf(i21, zzb4);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 68:
                    if (!zza(obj, i21, i18)) {
                        break;
                    }
                    zza7 = zzii.zzc(i21, (zzkk) zzma.zzf(obj, j4), zza(i18));
                    i19 += zza7;
                    break;
            }
            i18 += 3;
            i5 = 267386880;
        }
        return i19 + zza(this.zzq, obj);
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final void zzb(Object obj, Object obj2) {
        obj2.getClass();
        for (int i = 0; i < this.zzc.length; i += 3) {
            int zzd = zzd(i);
            long j = 1048575 & zzd;
            int i2 = this.zzc[i];
            switch ((zzd & 267386880) >>> 20) {
                case 0:
                    if (zza(obj2, i)) {
                        zzma.zza(obj, j, zzma.zze(obj2, j));
                        zzb(obj, i);
                        break;
                    } else {
                        break;
                    }
                case 1:
                    if (zza(obj2, i)) {
                        zzma.zza(obj, j, zzma.zzd(obj2, j));
                        zzb(obj, i);
                        break;
                    } else {
                        break;
                    }
                case 2:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzma.zza(obj, j, zzma.zzb(obj2, j));
                    zzb(obj, i);
                    break;
                case 3:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzma.zza(obj, j, zzma.zzb(obj2, j));
                    zzb(obj, i);
                    break;
                case 4:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzma.zza(obj, j, zzma.zza(obj2, j));
                    zzb(obj, i);
                    break;
                case 5:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzma.zza(obj, j, zzma.zzb(obj2, j));
                    zzb(obj, i);
                    break;
                case 6:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzma.zza(obj, j, zzma.zza(obj2, j));
                    zzb(obj, i);
                    break;
                case 7:
                    if (zza(obj2, i)) {
                        zzma.zza(obj, j, zzma.zzc(obj2, j));
                        zzb(obj, i);
                        break;
                    } else {
                        break;
                    }
                case 8:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzma.zza(obj, j, zzma.zzf(obj2, j));
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
                    zzma.zza(obj, j, zzma.zzf(obj2, j));
                    zzb(obj, i);
                    break;
                case 11:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzma.zza(obj, j, zzma.zza(obj2, j));
                    zzb(obj, i);
                    break;
                case 12:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzma.zza(obj, j, zzma.zza(obj2, j));
                    zzb(obj, i);
                    break;
                case 13:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzma.zza(obj, j, zzma.zza(obj2, j));
                    zzb(obj, i);
                    break;
                case 14:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzma.zza(obj, j, zzma.zzb(obj2, j));
                    zzb(obj, i);
                    break;
                case 15:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzma.zza(obj, j, zzma.zza(obj2, j));
                    zzb(obj, i);
                    break;
                case 16:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzma.zza(obj, j, zzma.zzb(obj2, j));
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
                    this.zzp.zza(obj, obj2, j);
                    break;
                case 50:
                    zzle.zza(this.zzs, obj, obj2, j);
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
                    zzma.zza(obj, j, zzma.zzf(obj2, j));
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
                    zzma.zza(obj, j, zzma.zzf(obj2, j));
                    zzb(obj, i2, i);
                    break;
            }
        }
        zzle.zza(this.zzq, obj, obj2);
        if (this.zzh) {
            zzle.zza(this.zzr, obj, obj2);
        }
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final void zzc(Object obj) {
        int i;
        int i2 = this.zzm;
        while (true) {
            i = this.zzn;
            if (i2 >= i) {
                break;
            }
            long zzd = zzd(this.zzl[i2]) & 1048575;
            Object zzf = zzma.zzf(obj, zzd);
            if (zzf != null) {
                zzma.zza(obj, zzd, this.zzs.zze(zzf));
            }
            i2++;
        }
        int length = this.zzl.length;
        while (i < length) {
            this.zzp.zzb(obj, this.zzl[i]);
            i++;
        }
        this.zzq.zzd(obj);
        if (this.zzh) {
            this.zzr.zzc(obj);
        }
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final boolean zzd(Object obj) {
        int i;
        int i2;
        int i3 = 1048575;
        int i4 = 0;
        int i5 = 0;
        while (i5 < this.zzm) {
            int i6 = this.zzl[i5];
            int i7 = this.zzc[i6];
            int zzd = zzd(i6);
            int i8 = this.zzc[i6 + 2];
            int i9 = i8 & 1048575;
            int i10 = 1 << (i8 >>> 20);
            if (i9 != i3) {
                if (i9 != 1048575) {
                    i4 = zzb.getInt(obj, i9);
                }
                i2 = i4;
                i = i9;
            } else {
                i = i3;
                i2 = i4;
            }
            if ((268435456 & zzd) != 0 && !zza(obj, i6, i, i2, i10)) {
                return false;
            }
            int i11 = (267386880 & zzd) >>> 20;
            if (i11 != 9 && i11 != 17) {
                if (i11 != 27) {
                    if (i11 == 60 || i11 == 68) {
                        if (zza(obj, i7, i6) && !zza(obj, zzd, zza(i6))) {
                            return false;
                        }
                    } else if (i11 != 49) {
                        if (i11 == 50 && !this.zzs.zzc(zzma.zzf(obj, zzd & 1048575)).isEmpty()) {
                            this.zzs.zzb(zzb(i6));
                            throw null;
                        }
                    }
                }
                List list = (List) zzma.zzf(obj, zzd & 1048575);
                if (list.isEmpty()) {
                    continue;
                } else {
                    zzlc zza2 = zza(i6);
                    for (int i12 = 0; i12 < list.size(); i12++) {
                        if (!zza2.zzd(list.get(i12))) {
                            return false;
                        }
                    }
                }
            } else if (zza(obj, i6, i, i2, i10) && !zza(obj, zzd, zza(i6))) {
                return false;
            }
            i5++;
            i3 = i;
            i4 = i2;
        }
        return !this.zzh || this.zzr.zza(obj).zzf();
    }
}
