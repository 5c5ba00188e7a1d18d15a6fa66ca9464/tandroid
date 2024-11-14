package com.google.android.gms.internal.play_billing;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;
import org.telegram.messenger.CharacterCompat;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.ConnectionsManager;
import sun.misc.Unsafe;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzdi implements zzdp {
    private static final int[] zza = new int[0];
    private static final Unsafe zzb = zzeq.zzg();
    private final int[] zzc;
    private final Object[] zzd;
    private final int zze;
    private final int zzf;
    private final zzdf zzg;
    private final boolean zzh;
    private final int[] zzi;
    private final int zzj;
    private final int zzk;
    private final zzct zzl;
    private final zzeg zzm;
    private final zzbo zzn;
    private final int zzo;
    private final zzdk zzp;
    private final zzda zzq;

    private zzdi(int[] iArr, Object[] objArr, int i, int i2, zzdf zzdfVar, int i3, boolean z, int[] iArr2, int i4, int i5, zzdk zzdkVar, zzct zzctVar, zzeg zzegVar, zzbo zzboVar, zzda zzdaVar) {
        this.zzc = iArr;
        this.zzd = objArr;
        this.zze = i;
        this.zzf = i2;
        this.zzo = i3;
        boolean z2 = false;
        if (zzboVar != null && zzboVar.zzc(zzdfVar)) {
            z2 = true;
        }
        this.zzh = z2;
        this.zzi = iArr2;
        this.zzj = i4;
        this.zzk = i5;
        this.zzp = zzdkVar;
        this.zzl = zzctVar;
        this.zzm = zzegVar;
        this.zzn = zzboVar;
        this.zzg = zzdfVar;
        this.zzq = zzdaVar;
    }

    private final zzce zzA(int i) {
        int i2 = i / 3;
        return (zzce) this.zzd[i2 + i2 + 1];
    }

    private final zzdp zzB(int i) {
        int i2 = i / 3;
        int i3 = i2 + i2;
        zzdp zzdpVar = (zzdp) this.zzd[i3];
        if (zzdpVar != null) {
            return zzdpVar;
        }
        zzdp zzb2 = zzdn.zza().zzb((Class) this.zzd[i3 + 1]);
        this.zzd[i3] = zzb2;
        return zzb2;
    }

    private final Object zzC(int i) {
        int i2 = i / 3;
        return this.zzd[i2 + i2];
    }

    private final Object zzD(Object obj, int i) {
        zzdp zzB = zzB(i);
        int zzy = zzy(i) & 1048575;
        if (!zzP(obj, i)) {
            return zzB.zze();
        }
        Object object = zzb.getObject(obj, zzy);
        if (zzS(object)) {
            return object;
        }
        Object zze = zzB.zze();
        if (object != null) {
            zzB.zzg(zze, object);
        }
        return zze;
    }

    private final Object zzE(Object obj, int i, int i2) {
        zzdp zzB = zzB(i2);
        if (!zzT(obj, i, i2)) {
            return zzB.zze();
        }
        Object object = zzb.getObject(obj, zzy(i2) & 1048575);
        if (zzS(object)) {
            return object;
        }
        Object zze = zzB.zze();
        if (object != null) {
            zzB.zzg(zze, object);
        }
        return zze;
    }

    private static Field zzF(Class cls, String str) {
        try {
            return cls.getDeclaredField(str);
        } catch (NoSuchFieldException unused) {
            Field[] declaredFields = cls.getDeclaredFields();
            for (Field field : declaredFields) {
                if (str.equals(field.getName())) {
                    return field;
                }
            }
            throw new RuntimeException("Field " + str + " for " + cls.getName() + " not found. Known fields are " + Arrays.toString(declaredFields));
        }
    }

    private static void zzG(Object obj) {
        if (!zzS(obj)) {
            throw new IllegalArgumentException("Mutating immutable message: ".concat(String.valueOf(obj)));
        }
    }

    private final void zzH(Object obj, Object obj2, int i) {
        if (zzP(obj2, i)) {
            int zzy = zzy(i) & 1048575;
            Unsafe unsafe = zzb;
            long j = zzy;
            Object object = unsafe.getObject(obj2, j);
            if (object == null) {
                throw new IllegalStateException("Source subfield " + this.zzc[i] + " is present but null: " + obj2.toString());
            }
            zzdp zzB = zzB(i);
            if (!zzP(obj, i)) {
                if (zzS(object)) {
                    Object zze = zzB.zze();
                    zzB.zzg(zze, object);
                    unsafe.putObject(obj, j, zze);
                } else {
                    unsafe.putObject(obj, j, object);
                }
                zzJ(obj, i);
                return;
            }
            Object object2 = unsafe.getObject(obj, j);
            if (!zzS(object2)) {
                Object zze2 = zzB.zze();
                zzB.zzg(zze2, object2);
                unsafe.putObject(obj, j, zze2);
                object2 = zze2;
            }
            zzB.zzg(object2, object);
        }
    }

    private final void zzI(Object obj, Object obj2, int i) {
        int i2 = this.zzc[i];
        if (zzT(obj2, i2, i)) {
            int zzy = zzy(i) & 1048575;
            Unsafe unsafe = zzb;
            long j = zzy;
            Object object = unsafe.getObject(obj2, j);
            if (object == null) {
                throw new IllegalStateException("Source subfield " + this.zzc[i] + " is present but null: " + obj2.toString());
            }
            zzdp zzB = zzB(i);
            if (!zzT(obj, i2, i)) {
                if (zzS(object)) {
                    Object zze = zzB.zze();
                    zzB.zzg(zze, object);
                    unsafe.putObject(obj, j, zze);
                } else {
                    unsafe.putObject(obj, j, object);
                }
                zzK(obj, i2, i);
                return;
            }
            Object object2 = unsafe.getObject(obj, j);
            if (!zzS(object2)) {
                Object zze2 = zzB.zze();
                zzB.zzg(zze2, object2);
                unsafe.putObject(obj, j, zze2);
                object2 = zze2;
            }
            zzB.zzg(object2, object);
        }
    }

    private final void zzJ(Object obj, int i) {
        int zzv = zzv(i);
        long j = 1048575 & zzv;
        if (j == 1048575) {
            return;
        }
        zzeq.zzq(obj, j, (1 << (zzv >>> 20)) | zzeq.zzc(obj, j));
    }

    private final void zzK(Object obj, int i, int i2) {
        zzeq.zzq(obj, zzv(i2) & 1048575, i);
    }

    private final void zzL(Object obj, int i, Object obj2) {
        zzb.putObject(obj, zzy(i) & 1048575, obj2);
        zzJ(obj, i);
    }

    private final void zzM(Object obj, int i, int i2, Object obj2) {
        zzb.putObject(obj, zzy(i2) & 1048575, obj2);
        zzK(obj, i, i2);
    }

    private final void zzN(zzey zzeyVar, int i, Object obj, int i2) {
        if (obj == null) {
            return;
        }
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzC(i2));
        throw null;
    }

    private final boolean zzO(Object obj, Object obj2, int i) {
        return zzP(obj, i) == zzP(obj2, i);
    }

    private final boolean zzP(Object obj, int i) {
        int zzv = zzv(i);
        long j = zzv & 1048575;
        if (j != 1048575) {
            return (zzeq.zzc(obj, j) & (1 << (zzv >>> 20))) != 0;
        }
        int zzy = zzy(i);
        long j2 = zzy & 1048575;
        switch (zzx(zzy)) {
            case 0:
                return Double.doubleToRawLongBits(zzeq.zza(obj, j2)) != 0;
            case 1:
                return Float.floatToRawIntBits(zzeq.zzb(obj, j2)) != 0;
            case 2:
                return zzeq.zzd(obj, j2) != 0;
            case 3:
                return zzeq.zzd(obj, j2) != 0;
            case 4:
                return zzeq.zzc(obj, j2) != 0;
            case 5:
                return zzeq.zzd(obj, j2) != 0;
            case 6:
                return zzeq.zzc(obj, j2) != 0;
            case 7:
                return zzeq.zzw(obj, j2);
            case 8:
                Object zzf = zzeq.zzf(obj, j2);
                if (zzf instanceof String) {
                    return !((String) zzf).isEmpty();
                }
                if (zzf instanceof zzba) {
                    return !zzba.zzb.equals(zzf);
                }
                throw new IllegalArgumentException();
            case 9:
                return zzeq.zzf(obj, j2) != null;
            case 10:
                return !zzba.zzb.equals(zzeq.zzf(obj, j2));
            case 11:
                return zzeq.zzc(obj, j2) != 0;
            case 12:
                return zzeq.zzc(obj, j2) != 0;
            case 13:
                return zzeq.zzc(obj, j2) != 0;
            case 14:
                return zzeq.zzd(obj, j2) != 0;
            case 15:
                return zzeq.zzc(obj, j2) != 0;
            case 16:
                return zzeq.zzd(obj, j2) != 0;
            case 17:
                return zzeq.zzf(obj, j2) != null;
            default:
                throw new IllegalArgumentException();
        }
    }

    private final boolean zzQ(Object obj, int i, int i2, int i3, int i4) {
        return i2 == 1048575 ? zzP(obj, i) : (i3 & i4) != 0;
    }

    private static boolean zzR(Object obj, int i, zzdp zzdpVar) {
        return zzdpVar.zzk(zzeq.zzf(obj, i & 1048575));
    }

    private static boolean zzS(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof zzcb) {
            return ((zzcb) obj).zzt();
        }
        return true;
    }

    private final boolean zzT(Object obj, int i, int i2) {
        return zzeq.zzc(obj, (long) (zzv(i2) & 1048575)) == i;
    }

    private static boolean zzU(Object obj, long j) {
        return ((Boolean) zzeq.zzf(obj, j)).booleanValue();
    }

    private static final void zzV(int i, Object obj, zzey zzeyVar) {
        if (obj instanceof String) {
            zzeyVar.zzF(i, (String) obj);
        } else {
            zzeyVar.zzd(i, (zzba) obj);
        }
    }

    static zzeh zzd(Object obj) {
        zzcb zzcbVar = (zzcb) obj;
        zzeh zzehVar = zzcbVar.zzc;
        if (zzehVar != zzeh.zzc()) {
            return zzehVar;
        }
        zzeh zzf = zzeh.zzf();
        zzcbVar.zzc = zzf;
        return zzf;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:65:0x024f  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x026b  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x026e  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0255  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static zzdi zzl(Class cls, zzdc zzdcVar, zzdk zzdkVar, zzct zzctVar, zzeg zzegVar, zzbo zzboVar, zzda zzdaVar) {
        int i;
        int charAt;
        int charAt2;
        int i2;
        int[] iArr;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        char charAt3;
        int i8;
        char charAt4;
        int i9;
        char charAt5;
        int i10;
        char charAt6;
        int i11;
        char charAt7;
        int i12;
        char charAt8;
        int i13;
        char charAt9;
        int i14;
        char charAt10;
        int i15;
        int i16;
        int i17;
        int i18;
        int i19;
        int i20;
        String str;
        int i21;
        int i22;
        int i23;
        int i24;
        Field zzF;
        char charAt11;
        int i25;
        int i26;
        Object obj;
        Field zzF2;
        Object obj2;
        Field zzF3;
        int i27;
        char charAt12;
        int i28;
        char charAt13;
        int i29;
        char charAt14;
        int i30;
        char charAt15;
        if (!(zzdcVar instanceof zzdo)) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzdcVar);
            throw null;
        }
        zzdo zzdoVar = (zzdo) zzdcVar;
        String zzd = zzdoVar.zzd();
        int length = zzd.length();
        char charAt16 = zzd.charAt(0);
        char c = CharacterCompat.MIN_HIGH_SURROGATE;
        if (charAt16 >= 55296) {
            int i31 = 1;
            while (true) {
                i = i31 + 1;
                if (zzd.charAt(i31) < 55296) {
                    break;
                }
                i31 = i;
            }
        } else {
            i = 1;
        }
        int i32 = i + 1;
        int charAt17 = zzd.charAt(i);
        if (charAt17 >= 55296) {
            int i33 = charAt17 & 8191;
            int i34 = 13;
            while (true) {
                i30 = i32 + 1;
                charAt15 = zzd.charAt(i32);
                if (charAt15 < 55296) {
                    break;
                }
                i33 |= (charAt15 & 8191) << i34;
                i34 += 13;
                i32 = i30;
            }
            charAt17 = i33 | (charAt15 << i34);
            i32 = i30;
        }
        if (charAt17 == 0) {
            iArr = zza;
            i5 = 0;
            charAt = 0;
            charAt2 = 0;
            i3 = 0;
            i6 = 0;
            i2 = 0;
            i4 = 0;
        } else {
            int i35 = i32 + 1;
            int charAt18 = zzd.charAt(i32);
            if (charAt18 >= 55296) {
                int i36 = charAt18 & 8191;
                int i37 = 13;
                while (true) {
                    i14 = i35 + 1;
                    charAt10 = zzd.charAt(i35);
                    if (charAt10 < 55296) {
                        break;
                    }
                    i36 |= (charAt10 & 8191) << i37;
                    i37 += 13;
                    i35 = i14;
                }
                charAt18 = i36 | (charAt10 << i37);
                i35 = i14;
            }
            int i38 = i35 + 1;
            int charAt19 = zzd.charAt(i35);
            if (charAt19 >= 55296) {
                int i39 = charAt19 & 8191;
                int i40 = 13;
                while (true) {
                    i13 = i38 + 1;
                    charAt9 = zzd.charAt(i38);
                    if (charAt9 < 55296) {
                        break;
                    }
                    i39 |= (charAt9 & 8191) << i40;
                    i40 += 13;
                    i38 = i13;
                }
                charAt19 = i39 | (charAt9 << i40);
                i38 = i13;
            }
            int i41 = i38 + 1;
            int charAt20 = zzd.charAt(i38);
            if (charAt20 >= 55296) {
                int i42 = charAt20 & 8191;
                int i43 = 13;
                while (true) {
                    i12 = i41 + 1;
                    charAt8 = zzd.charAt(i41);
                    if (charAt8 < 55296) {
                        break;
                    }
                    i42 |= (charAt8 & 8191) << i43;
                    i43 += 13;
                    i41 = i12;
                }
                charAt20 = i42 | (charAt8 << i43);
                i41 = i12;
            }
            int i44 = i41 + 1;
            int charAt21 = zzd.charAt(i41);
            if (charAt21 >= 55296) {
                int i45 = charAt21 & 8191;
                int i46 = 13;
                while (true) {
                    i11 = i44 + 1;
                    charAt7 = zzd.charAt(i44);
                    if (charAt7 < 55296) {
                        break;
                    }
                    i45 |= (charAt7 & 8191) << i46;
                    i46 += 13;
                    i44 = i11;
                }
                charAt21 = i45 | (charAt7 << i46);
                i44 = i11;
            }
            int i47 = i44 + 1;
            charAt = zzd.charAt(i44);
            if (charAt >= 55296) {
                int i48 = charAt & 8191;
                int i49 = 13;
                while (true) {
                    i10 = i47 + 1;
                    charAt6 = zzd.charAt(i47);
                    if (charAt6 < 55296) {
                        break;
                    }
                    i48 |= (charAt6 & 8191) << i49;
                    i49 += 13;
                    i47 = i10;
                }
                charAt = i48 | (charAt6 << i49);
                i47 = i10;
            }
            int i50 = i47 + 1;
            charAt2 = zzd.charAt(i47);
            if (charAt2 >= 55296) {
                int i51 = charAt2 & 8191;
                int i52 = 13;
                while (true) {
                    i9 = i50 + 1;
                    charAt5 = zzd.charAt(i50);
                    if (charAt5 < 55296) {
                        break;
                    }
                    i51 |= (charAt5 & 8191) << i52;
                    i52 += 13;
                    i50 = i9;
                }
                charAt2 = i51 | (charAt5 << i52);
                i50 = i9;
            }
            int i53 = i50 + 1;
            int charAt22 = zzd.charAt(i50);
            if (charAt22 >= 55296) {
                int i54 = charAt22 & 8191;
                int i55 = 13;
                while (true) {
                    i8 = i53 + 1;
                    charAt4 = zzd.charAt(i53);
                    if (charAt4 < 55296) {
                        break;
                    }
                    i54 |= (charAt4 & 8191) << i55;
                    i55 += 13;
                    i53 = i8;
                }
                charAt22 = i54 | (charAt4 << i55);
                i53 = i8;
            }
            int i56 = i53 + 1;
            int charAt23 = zzd.charAt(i53);
            if (charAt23 >= 55296) {
                int i57 = charAt23 & 8191;
                int i58 = 13;
                while (true) {
                    i7 = i56 + 1;
                    charAt3 = zzd.charAt(i56);
                    if (charAt3 < 55296) {
                        break;
                    }
                    i57 |= (charAt3 & 8191) << i58;
                    i58 += 13;
                    i56 = i7;
                }
                charAt23 = i57 | (charAt3 << i58);
                i56 = i7;
            }
            i2 = charAt18 + charAt18 + charAt19;
            iArr = new int[charAt23 + charAt2 + charAt22];
            i3 = charAt20;
            i4 = charAt23;
            i5 = charAt18;
            i6 = charAt21;
            i32 = i56;
        }
        Unsafe unsafe = zzb;
        Object[] zze = zzdoVar.zze();
        Class<?> cls2 = zzdoVar.zza().getClass();
        int i59 = i4 + charAt2;
        int i60 = charAt + charAt;
        int[] iArr2 = new int[charAt * 3];
        Object[] objArr = new Object[i60];
        int i61 = i4;
        int i62 = i59;
        int i63 = 0;
        int i64 = 0;
        while (i32 < length) {
            int i65 = i32 + 1;
            int charAt24 = zzd.charAt(i32);
            if (charAt24 >= c) {
                int i66 = charAt24 & 8191;
                int i67 = i65;
                int i68 = 13;
                while (true) {
                    i29 = i67 + 1;
                    charAt14 = zzd.charAt(i67);
                    if (charAt14 < c) {
                        break;
                    }
                    i66 |= (charAt14 & 8191) << i68;
                    i68 += 13;
                    i67 = i29;
                }
                charAt24 = i66 | (charAt14 << i68);
                i15 = i29;
            } else {
                i15 = i65;
            }
            int i69 = i15 + 1;
            int charAt25 = zzd.charAt(i15);
            if (charAt25 >= c) {
                int i70 = charAt25 & 8191;
                int i71 = i69;
                int i72 = 13;
                while (true) {
                    i28 = i71 + 1;
                    charAt13 = zzd.charAt(i71);
                    if (charAt13 < c) {
                        break;
                    }
                    i70 |= (charAt13 & 8191) << i72;
                    i72 += 13;
                    i71 = i28;
                }
                charAt25 = i70 | (charAt13 << i72);
                i16 = i28;
            } else {
                i16 = i69;
            }
            if ((charAt25 & 1024) != 0) {
                iArr[i63] = i64;
                i63++;
            }
            int i73 = charAt25 & NotificationCenter.notificationsCountUpdated;
            if (i73 >= 51) {
                int i74 = i16 + 1;
                int charAt26 = zzd.charAt(i16);
                i17 = length;
                char c2 = CharacterCompat.MIN_HIGH_SURROGATE;
                if (charAt26 >= 55296) {
                    int i75 = charAt26 & 8191;
                    int i76 = 13;
                    while (true) {
                        i27 = i74 + 1;
                        charAt12 = zzd.charAt(i74);
                        if (charAt12 < c2) {
                            break;
                        }
                        i75 |= (charAt12 & 8191) << i76;
                        i76 += 13;
                        i74 = i27;
                        c2 = CharacterCompat.MIN_HIGH_SURROGATE;
                    }
                    charAt26 = i75 | (charAt12 << i76);
                    i74 = i27;
                }
                int i77 = i73 - 51;
                int i78 = i74;
                if (i77 == 9 || i77 == 17) {
                    int i79 = i64 / 3;
                    i26 = i2 + 1;
                    objArr[i79 + i79 + 1] = zze[i2];
                } else {
                    if (i77 == 12 && (zzdoVar.zzc() == 1 || (charAt25 & 2048) != 0)) {
                        int i80 = i64 / 3;
                        i26 = i2 + 1;
                        objArr[i80 + i80 + 1] = zze[i2];
                    }
                    int i81 = charAt26 + charAt26;
                    obj = zze[i81];
                    if (obj instanceof Field) {
                        zzF2 = zzF(cls2, (String) obj);
                        zze[i81] = zzF2;
                    } else {
                        zzF2 = (Field) obj;
                    }
                    int i82 = i3;
                    i18 = i6;
                    i24 = (int) unsafe.objectFieldOffset(zzF2);
                    int i83 = i81 + 1;
                    obj2 = zze[i83];
                    if (obj2 instanceof Field) {
                        zzF3 = zzF(cls2, (String) obj2);
                        zze[i83] = zzF3;
                    } else {
                        zzF3 = (Field) obj2;
                    }
                    int objectFieldOffset = (int) unsafe.objectFieldOffset(zzF3);
                    str = zzd;
                    i19 = i82;
                    i21 = i2;
                    i22 = i78;
                    i20 = objectFieldOffset;
                    i23 = 0;
                }
                i2 = i26;
                int i812 = charAt26 + charAt26;
                obj = zze[i812];
                if (obj instanceof Field) {
                }
                int i822 = i3;
                i18 = i6;
                i24 = (int) unsafe.objectFieldOffset(zzF2);
                int i832 = i812 + 1;
                obj2 = zze[i832];
                if (obj2 instanceof Field) {
                }
                int objectFieldOffset2 = (int) unsafe.objectFieldOffset(zzF3);
                str = zzd;
                i19 = i822;
                i21 = i2;
                i22 = i78;
                i20 = objectFieldOffset2;
                i23 = 0;
            } else {
                i17 = length;
                int i84 = i3;
                i18 = i6;
                int i85 = i2 + 1;
                Field zzF4 = zzF(cls2, (String) zze[i2]);
                if (i73 == 9 || i73 == 17) {
                    i19 = i84;
                    int i86 = i64 / 3;
                    objArr[i86 + i86 + 1] = zzF4.getType();
                } else {
                    if (i73 == 27 || i73 == 49) {
                        i19 = i84;
                        int i87 = i64 / 3;
                        i25 = i2 + 2;
                        objArr[i87 + i87 + 1] = zze[i85];
                    } else if (i73 == 12 || i73 == 30 || i73 == 44) {
                        i19 = i84;
                        if (zzdoVar.zzc() == 1 || (charAt25 & 2048) != 0) {
                            int i88 = i64 / 3;
                            i25 = i2 + 2;
                            objArr[i88 + i88 + 1] = zze[i85];
                        }
                    } else {
                        if (i73 == 50) {
                            int i89 = i61 + 1;
                            iArr[i61] = i64;
                            int i90 = i64 / 3;
                            int i91 = i2 + 2;
                            int i92 = i90 + i90;
                            objArr[i92] = zze[i85];
                            if ((charAt25 & 2048) != 0) {
                                i85 = i2 + 3;
                                objArr[i92 + 1] = zze[i91];
                                i19 = i84;
                                i61 = i89;
                            } else {
                                i61 = i89;
                                i85 = i91;
                            }
                        }
                        i19 = i84;
                    }
                    i85 = i25;
                }
                int objectFieldOffset3 = (int) unsafe.objectFieldOffset(zzF4);
                i20 = 1048575;
                if ((charAt25 & LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM) == 0 || i73 > 17) {
                    str = zzd;
                    i21 = i85;
                    i22 = i16;
                    i23 = 0;
                } else {
                    int i93 = i16 + 1;
                    int charAt27 = zzd.charAt(i16);
                    if (charAt27 >= 55296) {
                        int i94 = charAt27 & 8191;
                        int i95 = 13;
                        while (true) {
                            i22 = i93 + 1;
                            charAt11 = zzd.charAt(i93);
                            if (charAt11 < 55296) {
                                break;
                            }
                            i94 |= (charAt11 & 8191) << i95;
                            i95 += 13;
                            i93 = i22;
                        }
                        charAt27 = i94 | (charAt11 << i95);
                    } else {
                        i22 = i93;
                    }
                    int i96 = i5 + i5 + (charAt27 / 32);
                    Object obj3 = zze[i96];
                    str = zzd;
                    if (obj3 instanceof Field) {
                        zzF = (Field) obj3;
                    } else {
                        zzF = zzF(cls2, (String) obj3);
                        zze[i96] = zzF;
                    }
                    i21 = i85;
                    i23 = charAt27 % 32;
                    i20 = (int) unsafe.objectFieldOffset(zzF);
                }
                if (i73 >= 18 && i73 <= 49) {
                    iArr[i62] = objectFieldOffset3;
                    i62++;
                }
                i24 = objectFieldOffset3;
            }
            int i97 = i64 + 1;
            iArr2[i64] = charAt24;
            int i98 = i64 + 2;
            iArr2[i97] = i24 | ((charAt25 & 256) != 0 ? 268435456 : 0) | ((charAt25 & 512) != 0 ? 536870912 : 0) | (i73 << 20);
            i64 += 3;
            iArr2[i98] = (i23 << 20) | i20;
            i2 = i21;
            i32 = i22;
            length = i17;
            i3 = i19;
            zzd = str;
            i6 = i18;
            c = CharacterCompat.MIN_HIGH_SURROGATE;
        }
        return new zzdi(iArr2, objArr, i3, i6, zzdoVar.zza(), zzdoVar.zzc(), false, iArr, i4, i59, zzdkVar, zzctVar, zzegVar, zzboVar, zzdaVar);
    }

    private static double zzm(Object obj, long j) {
        return ((Double) zzeq.zzf(obj, j)).doubleValue();
    }

    private static float zzn(Object obj, long j) {
        return ((Float) zzeq.zzf(obj, j)).floatValue();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0093, code lost:
    
        if (zzT(r16, r11, r5) != false) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0095, code lost:
    
        r3 = com.google.android.gms.internal.play_billing.zzbi.zzx(r11 << 3) + 8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x00a2, code lost:
    
        if (zzT(r16, r11, r5) != false) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00a4, code lost:
    
        r3 = com.google.android.gms.internal.play_billing.zzbi.zzx(r11 << 3) + 4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00b1, code lost:
    
        if (zzT(r16, r11, r5) != false) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00b3, code lost:
    
        r4 = r11 << 3;
        r3 = com.google.android.gms.internal.play_billing.zzbi.zzu(zzp(r16, r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0132, code lost:
    
        if (zzT(r16, r11, r5) != false) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x013a, code lost:
    
        if (zzT(r16, r11, r5) != false) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0142, code lost:
    
        if (zzT(r16, r11, r5) != false) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x014a, code lost:
    
        if (zzT(r16, r11, r5) != false) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x014c, code lost:
    
        r3 = com.google.android.gms.internal.play_billing.zzbi.zzy(zzz(r16, r3));
        r4 = com.google.android.gms.internal.play_billing.zzbi.zzx(r11 << 3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0160, code lost:
    
        if (zzT(r16, r11, r5) != false) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0167, code lost:
    
        if (zzT(r16, r11, r5) != false) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x016f, code lost:
    
        if (zzT(r16, r11, r5) != false) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x019a, code lost:
    
        if (r3 > 0) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x019c, code lost:
    
        r4 = com.google.android.gms.internal.play_billing.zzbi.zzx(r11 << 3) + com.google.android.gms.internal.play_billing.zzbi.zzx(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x01b3, code lost:
    
        if (r3 > 0) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x01c0, code lost:
    
        if (r3 > 0) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x01cd, code lost:
    
        if (r3 > 0) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x01da, code lost:
    
        if (r3 > 0) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x01e7, code lost:
    
        if (r3 > 0) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x01f6, code lost:
    
        if (r3 > 0) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x0203, code lost:
    
        if (r3 > 0) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x0210, code lost:
    
        if (r3 > 0) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x021d, code lost:
    
        if (r3 > 0) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x022b, code lost:
    
        if (r3 > 0) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x0239, code lost:
    
        if (r3 > 0) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x0247, code lost:
    
        if (r3 > 0) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0255, code lost:
    
        if (r3 > 0) goto L81;
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:11:0x003c. Please report as an issue. */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final int zzo(Object obj) {
        int i;
        int zzy;
        int zzx;
        int zzx2;
        Object object;
        int i2;
        int zzt;
        int zzh;
        int zzx3;
        Object object2;
        int i3;
        Unsafe unsafe = zzb;
        int i4 = 1048575;
        int i5 = 0;
        int i6 = 0;
        int i7 = 1048575;
        int i8 = 0;
        while (i5 < this.zzc.length) {
            int zzy2 = zzy(i5);
            int[] iArr = this.zzc;
            int i9 = iArr[i5];
            int zzx4 = zzx(zzy2);
            if (zzx4 <= 17) {
                int i10 = iArr[i5 + 2];
                int i11 = i10 & i4;
                int i12 = i10 >>> 20;
                if (i11 != i7) {
                    i8 = unsafe.getInt(obj, i11);
                    i7 = i11;
                }
                i = 1 << i12;
            } else {
                i = 0;
            }
            long j = zzy2 & i4;
            switch (zzx4) {
                case 0:
                    if ((i8 & i) == 0) {
                        break;
                    }
                    zzx2 = zzbi.zzx(i9 << 3) + 8;
                    i6 += zzx2;
                    break;
                case 1:
                    if ((i8 & i) == 0) {
                        break;
                    }
                    zzx2 = zzbi.zzx(i9 << 3) + 4;
                    i6 += zzx2;
                    break;
                case 2:
                    if ((i8 & i) == 0) {
                        break;
                    }
                    zzy = zzbi.zzy(unsafe.getLong(obj, j));
                    zzx = zzbi.zzx(i9 << 3);
                    i6 += zzx + zzy;
                    break;
                case 3:
                    if ((i8 & i) == 0) {
                        break;
                    }
                    zzy = zzbi.zzy(unsafe.getLong(obj, j));
                    zzx = zzbi.zzx(i9 << 3);
                    i6 += zzx + zzy;
                    break;
                case 4:
                    if ((i8 & i) == 0) {
                        break;
                    }
                    i2 = i9 << 3;
                    zzy = zzbi.zzu(unsafe.getInt(obj, j));
                    zzx = zzbi.zzx(i2);
                    i6 += zzx + zzy;
                    break;
                case 5:
                    if ((i8 & i) == 0) {
                        break;
                    }
                    zzx2 = zzbi.zzx(i9 << 3) + 8;
                    i6 += zzx2;
                    break;
                case 6:
                    if ((i8 & i) == 0) {
                        break;
                    }
                    zzx2 = zzbi.zzx(i9 << 3) + 4;
                    i6 += zzx2;
                    break;
                case 7:
                    if ((i8 & i) == 0) {
                        break;
                    } else {
                        zzx2 = zzbi.zzx(i9 << 3) + 1;
                        i6 += zzx2;
                        break;
                    }
                case 8:
                    if ((i8 & i) == 0) {
                        break;
                    } else {
                        object = unsafe.getObject(obj, j);
                        if (!(object instanceof zzba)) {
                            i2 = i9 << 3;
                            zzy = zzbi.zzw((String) object);
                            zzx = zzbi.zzx(i2);
                            i6 += zzx + zzy;
                            break;
                        }
                        int i13 = zzbi.$r8$clinit;
                        int zzd = ((zzba) object).zzd();
                        zzx2 = zzbi.zzx(i9 << 3) + zzbi.zzx(zzd) + zzd;
                        i6 += zzx2;
                        break;
                    }
                case 9:
                    if ((i8 & i) == 0) {
                        break;
                    } else {
                        zzx2 = zzdr.zzn(i9, unsafe.getObject(obj, j), zzB(i5));
                        i6 += zzx2;
                        break;
                    }
                case 10:
                    if ((i8 & i) == 0) {
                        break;
                    } else {
                        object = unsafe.getObject(obj, j);
                        int i132 = zzbi.$r8$clinit;
                        int zzd2 = ((zzba) object).zzd();
                        zzx2 = zzbi.zzx(i9 << 3) + zzbi.zzx(zzd2) + zzd2;
                        i6 += zzx2;
                        break;
                    }
                case 11:
                    if ((i8 & i) == 0) {
                        break;
                    } else {
                        i2 = i9 << 3;
                        zzy = zzbi.zzx(unsafe.getInt(obj, j));
                        zzx = zzbi.zzx(i2);
                        i6 += zzx + zzy;
                        break;
                    }
                case 12:
                    if ((i8 & i) == 0) {
                        break;
                    }
                    i2 = i9 << 3;
                    zzy = zzbi.zzu(unsafe.getInt(obj, j));
                    zzx = zzbi.zzx(i2);
                    i6 += zzx + zzy;
                    break;
                case 13:
                    if ((i8 & i) == 0) {
                        break;
                    }
                    zzx2 = zzbi.zzx(i9 << 3) + 4;
                    i6 += zzx2;
                    break;
                case 14:
                    if ((i8 & i) == 0) {
                        break;
                    }
                    zzx2 = zzbi.zzx(i9 << 3) + 8;
                    i6 += zzx2;
                    break;
                case 15:
                    if ((i8 & i) == 0) {
                        break;
                    } else {
                        int i14 = unsafe.getInt(obj, j);
                        zzx = zzbi.zzx(i9 << 3);
                        zzy = zzbi.zzx((i14 >> 31) ^ (i14 + i14));
                        i6 += zzx + zzy;
                        break;
                    }
                case 16:
                    if ((i & i8) == 0) {
                        break;
                    } else {
                        long j2 = unsafe.getLong(obj, j);
                        i6 += zzbi.zzx(i9 << 3) + zzbi.zzy((j2 >> 63) ^ (j2 + j2));
                        break;
                    }
                case 17:
                    if ((i8 & i) == 0) {
                        break;
                    } else {
                        zzx2 = zzbi.zzt(i9, (zzdf) unsafe.getObject(obj, j), zzB(i5));
                        i6 += zzx2;
                        break;
                    }
                case 18:
                case 23:
                    zzx2 = zzdr.zzg(i9, (List) unsafe.getObject(obj, j), false);
                    i6 += zzx2;
                    break;
                case 19:
                case 24:
                    zzx2 = zzdr.zze(i9, (List) unsafe.getObject(obj, j), false);
                    i6 += zzx2;
                    break;
                case 20:
                    zzx2 = zzdr.zzl(i9, (List) unsafe.getObject(obj, j), false);
                    i6 += zzx2;
                    break;
                case 21:
                    zzx2 = zzdr.zzw(i9, (List) unsafe.getObject(obj, j), false);
                    i6 += zzx2;
                    break;
                case 22:
                    zzx2 = zzdr.zzj(i9, (List) unsafe.getObject(obj, j), false);
                    i6 += zzx2;
                    break;
                case 25:
                    zzx2 = zzdr.zza(i9, (List) unsafe.getObject(obj, j), false);
                    i6 += zzx2;
                    break;
                case 26:
                    zzt = zzdr.zzt(i9, (List) unsafe.getObject(obj, j));
                    i6 += zzt;
                    break;
                case 27:
                    zzt = zzdr.zzo(i9, (List) unsafe.getObject(obj, j), zzB(i5));
                    i6 += zzt;
                    break;
                case 28:
                    zzt = zzdr.zzb(i9, (List) unsafe.getObject(obj, j));
                    i6 += zzt;
                    break;
                case 29:
                    zzt = zzdr.zzu(i9, (List) unsafe.getObject(obj, j), false);
                    i6 += zzt;
                    break;
                case 30:
                    zzt = zzdr.zzc(i9, (List) unsafe.getObject(obj, j), false);
                    i6 += zzt;
                    break;
                case 31:
                    zzt = zzdr.zze(i9, (List) unsafe.getObject(obj, j), false);
                    i6 += zzt;
                    break;
                case 32:
                    zzt = zzdr.zzg(i9, (List) unsafe.getObject(obj, j), false);
                    i6 += zzt;
                    break;
                case 33:
                    zzt = zzdr.zzp(i9, (List) unsafe.getObject(obj, j), false);
                    i6 += zzt;
                    break;
                case 34:
                    zzt = zzdr.zzr(i9, (List) unsafe.getObject(obj, j), false);
                    i6 += zzt;
                    break;
                case 35:
                    zzh = zzdr.zzh((List) unsafe.getObject(obj, j));
                    break;
                case 36:
                    zzh = zzdr.zzf((List) unsafe.getObject(obj, j));
                    break;
                case 37:
                    zzh = zzdr.zzm((List) unsafe.getObject(obj, j));
                    break;
                case 38:
                    zzh = zzdr.zzx((List) unsafe.getObject(obj, j));
                    break;
                case 39:
                    zzh = zzdr.zzk((List) unsafe.getObject(obj, j));
                    break;
                case 40:
                    zzh = zzdr.zzh((List) unsafe.getObject(obj, j));
                    break;
                case 41:
                    zzh = zzdr.zzf((List) unsafe.getObject(obj, j));
                    break;
                case 42:
                    List list = (List) unsafe.getObject(obj, j);
                    int i15 = zzdr.$r8$clinit;
                    zzh = list.size();
                    break;
                case 43:
                    zzh = zzdr.zzv((List) unsafe.getObject(obj, j));
                    break;
                case 44:
                    zzh = zzdr.zzd((List) unsafe.getObject(obj, j));
                    break;
                case 45:
                    zzh = zzdr.zzf((List) unsafe.getObject(obj, j));
                    break;
                case 46:
                    zzh = zzdr.zzh((List) unsafe.getObject(obj, j));
                    break;
                case 47:
                    zzh = zzdr.zzq((List) unsafe.getObject(obj, j));
                    break;
                case 48:
                    zzh = zzdr.zzs((List) unsafe.getObject(obj, j));
                    break;
                case 49:
                    zzt = zzdr.zzi(i9, (List) unsafe.getObject(obj, j), zzB(i5));
                    i6 += zzt;
                    break;
                case 50:
                    zzda.zza(i9, unsafe.getObject(obj, j), zzC(i5));
                    break;
                case 58:
                    if (zzT(obj, i9, i5)) {
                        zzt = zzbi.zzx(i9 << 3) + 1;
                        i6 += zzt;
                    }
                    break;
                case 59:
                    if (zzT(obj, i9, i5)) {
                        object2 = unsafe.getObject(obj, j);
                        if (!(object2 instanceof zzba)) {
                            i3 = i9 << 3;
                            zzh = zzbi.zzw((String) object2);
                            zzx3 = zzbi.zzx(i3);
                            i6 += zzx3 + zzh;
                        }
                        int i16 = zzbi.$r8$clinit;
                        int zzd3 = ((zzba) object2).zzd();
                        zzt = zzbi.zzx(i9 << 3) + zzbi.zzx(zzd3) + zzd3;
                        i6 += zzt;
                    }
                    break;
                case 60:
                    if (zzT(obj, i9, i5)) {
                        zzt = zzdr.zzn(i9, unsafe.getObject(obj, j), zzB(i5));
                        i6 += zzt;
                    }
                    break;
                case 61:
                    if (zzT(obj, i9, i5)) {
                        object2 = unsafe.getObject(obj, j);
                        int i162 = zzbi.$r8$clinit;
                        int zzd32 = ((zzba) object2).zzd();
                        zzt = zzbi.zzx(i9 << 3) + zzbi.zzx(zzd32) + zzd32;
                        i6 += zzt;
                    }
                    break;
                case 62:
                    if (zzT(obj, i9, i5)) {
                        i3 = i9 << 3;
                        zzh = zzbi.zzx(zzp(obj, j));
                        zzx3 = zzbi.zzx(i3);
                        i6 += zzx3 + zzh;
                    }
                    break;
                case 66:
                    if (zzT(obj, i9, i5)) {
                        int zzp = zzp(obj, j);
                        zzx3 = zzbi.zzx(i9 << 3);
                        zzh = zzbi.zzx((zzp >> 31) ^ (zzp + zzp));
                        i6 += zzx3 + zzh;
                    }
                    break;
                case 67:
                    if (zzT(obj, i9, i5)) {
                        long zzz = zzz(obj, j);
                        i6 += zzbi.zzx(i9 << 3) + zzbi.zzy((zzz >> 63) ^ (zzz + zzz));
                    }
                    break;
                case 68:
                    if (zzT(obj, i9, i5)) {
                        zzt = zzbi.zzt(i9, (zzdf) unsafe.getObject(obj, j), zzB(i5));
                        i6 += zzt;
                    }
                    break;
            }
            i5 += 3;
            i4 = 1048575;
        }
        zzeg zzegVar = this.zzm;
        int zza2 = i6 + zzegVar.zza(zzegVar.zzd(obj));
        if (!this.zzh) {
            return zza2;
        }
        this.zzn.zza(obj);
        throw null;
    }

    private static int zzp(Object obj, long j) {
        return ((Integer) zzeq.zzf(obj, j)).intValue();
    }

    private final int zzq(Object obj, byte[] bArr, int i, int i2, int i3, long j, zzan zzanVar) {
        Unsafe unsafe = zzb;
        Object zzC = zzC(i3);
        Object object = unsafe.getObject(obj, j);
        if (!((zzcz) object).zze()) {
            zzcz zzb2 = zzcz.zza().zzb();
            zzda.zzb(zzb2, object);
            unsafe.putObject(obj, j, zzb2);
        }
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzC);
        throw null;
    }

    private final int zzr(Object obj, byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6, int i7, long j, int i8, zzan zzanVar) {
        Unsafe unsafe = zzb;
        long j2 = this.zzc[i8 + 2] & 1048575;
        switch (i7) {
            case 51:
                if (i5 == 1) {
                    unsafe.putObject(obj, j, Double.valueOf(Double.longBitsToDouble(zzao.zzp(bArr, i))));
                    int i9 = i + 8;
                    unsafe.putInt(obj, j2, i4);
                    return i9;
                }
                break;
            case 52:
                if (i5 == 5) {
                    unsafe.putObject(obj, j, Float.valueOf(Float.intBitsToFloat(zzao.zzb(bArr, i))));
                    int i10 = i + 4;
                    unsafe.putInt(obj, j2, i4);
                    return i10;
                }
                break;
            case 53:
            case 54:
                if (i5 == 0) {
                    int zzm = zzao.zzm(bArr, i, zzanVar);
                    unsafe.putObject(obj, j, Long.valueOf(zzanVar.zzb));
                    unsafe.putInt(obj, j2, i4);
                    return zzm;
                }
                break;
            case 55:
            case 62:
                if (i5 == 0) {
                    int zzj = zzao.zzj(bArr, i, zzanVar);
                    unsafe.putObject(obj, j, Integer.valueOf(zzanVar.zza));
                    unsafe.putInt(obj, j2, i4);
                    return zzj;
                }
                break;
            case 56:
            case 65:
                if (i5 == 1) {
                    unsafe.putObject(obj, j, Long.valueOf(zzao.zzp(bArr, i)));
                    int i11 = i + 8;
                    unsafe.putInt(obj, j2, i4);
                    return i11;
                }
                break;
            case 57:
            case 64:
                if (i5 == 5) {
                    unsafe.putObject(obj, j, Integer.valueOf(zzao.zzb(bArr, i)));
                    int i12 = i + 4;
                    unsafe.putInt(obj, j2, i4);
                    return i12;
                }
                break;
            case 58:
                if (i5 == 0) {
                    int zzm2 = zzao.zzm(bArr, i, zzanVar);
                    unsafe.putObject(obj, j, Boolean.valueOf(zzanVar.zzb != 0));
                    unsafe.putInt(obj, j2, i4);
                    return zzm2;
                }
                break;
            case 59:
                if (i5 == 2) {
                    int zzj2 = zzao.zzj(bArr, i, zzanVar);
                    int i13 = zzanVar.zza;
                    if (i13 == 0) {
                        unsafe.putObject(obj, j, "");
                    } else {
                        if ((i6 & 536870912) != 0 && !zzev.zze(bArr, zzj2, zzj2 + i13)) {
                            throw zzci.zzc();
                        }
                        unsafe.putObject(obj, j, new String(bArr, zzj2, i13, zzcg.zzb));
                        zzj2 += i13;
                    }
                    unsafe.putInt(obj, j2, i4);
                    return zzj2;
                }
                break;
            case 60:
                if (i5 == 2) {
                    Object zzE = zzE(obj, i4, i8);
                    int zzo = zzao.zzo(zzE, zzB(i8), bArr, i, i2, zzanVar);
                    zzM(obj, i4, i8, zzE);
                    return zzo;
                }
                break;
            case 61:
                if (i5 == 2) {
                    int zza2 = zzao.zza(bArr, i, zzanVar);
                    unsafe.putObject(obj, j, zzanVar.zzc);
                    unsafe.putInt(obj, j2, i4);
                    return zza2;
                }
                break;
            case 63:
                if (i5 == 0) {
                    int zzj3 = zzao.zzj(bArr, i, zzanVar);
                    int i14 = zzanVar.zza;
                    zzce zzA = zzA(i8);
                    if (zzA == null || zzA.zza(i14)) {
                        unsafe.putObject(obj, j, Integer.valueOf(i14));
                        unsafe.putInt(obj, j2, i4);
                    } else {
                        zzd(obj).zzj(i3, Long.valueOf(i14));
                    }
                    return zzj3;
                }
                break;
            case 66:
                if (i5 == 0) {
                    int zzj4 = zzao.zzj(bArr, i, zzanVar);
                    unsafe.putObject(obj, j, Integer.valueOf(zzbe.zzb(zzanVar.zza)));
                    unsafe.putInt(obj, j2, i4);
                    return zzj4;
                }
                break;
            case 67:
                if (i5 == 0) {
                    int zzm3 = zzao.zzm(bArr, i, zzanVar);
                    unsafe.putObject(obj, j, Long.valueOf(zzbe.zzc(zzanVar.zzb)));
                    unsafe.putInt(obj, j2, i4);
                    return zzm3;
                }
                break;
            case 68:
                if (i5 == 3) {
                    Object zzE2 = zzE(obj, i4, i8);
                    int zzn = zzao.zzn(zzE2, zzB(i8), bArr, i, i2, (i3 & (-8)) | 4, zzanVar);
                    zzM(obj, i4, i8, zzE2);
                    return zzn;
                }
                break;
        }
        return i;
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x016a, code lost:
    
        r14.add(com.google.android.gms.internal.play_billing.zzba.zzl(r18, r1, r4));
        r1 = r1 + r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x0190, code lost:
    
        throw com.google.android.gms.internal.play_billing.zzci.zzg();
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x0195, code lost:
    
        throw com.google.android.gms.internal.play_billing.zzci.zzd();
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x0196, code lost:
    
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x0172, code lost:
    
        r14.add(com.google.android.gms.internal.play_billing.zzba.zzl(r18, r1, r4));
        r1 = r1 + r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x0162, code lost:
    
        if (r4 == 0) goto L80;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x0164, code lost:
    
        r14.add(com.google.android.gms.internal.play_billing.zzba.zzb);
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x0172, code lost:
    
        if (r1 >= r20) goto L250;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x0174, code lost:
    
        r4 = com.google.android.gms.internal.play_billing.zzao.zzj(r18, r1, r30);
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x017a, code lost:
    
        if (r21 == r30.zza) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x017d, code lost:
    
        r1 = com.google.android.gms.internal.play_billing.zzao.zzj(r18, r4, r30);
        r4 = r30.zza;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0183, code lost:
    
        if (r4 < 0) goto L251;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x0187, code lost:
    
        if (r4 > (r18.length - r1)) goto L252;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x0189, code lost:
    
        if (r4 != 0) goto L81;
     */
    /* JADX WARN: Removed duplicated region for block: B:128:0x01e4  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x022b  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:111:0x01f4 -> B:105:0x01d3). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:128:0x023b -> B:122:0x0212). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:85:0x0189 -> B:77:0x0164). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final int zzs(Object obj, byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6, long j, int i7, long j2, zzan zzanVar) {
        int zzl;
        int i8 = i;
        Unsafe unsafe = zzb;
        zzcf zzcfVar = (zzcf) unsafe.getObject(obj, j2);
        if (!zzcfVar.zzc()) {
            int size = zzcfVar.size();
            zzcfVar = zzcfVar.zzd(size == 0 ? 10 : size + size);
            unsafe.putObject(obj, j2, zzcfVar);
        }
        Object obj2 = null;
        switch (i7) {
            case 18:
            case 35:
                if (i5 == 2) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcfVar);
                    int zzj = zzao.zzj(bArr, i8, zzanVar);
                    int i9 = zzanVar.zza + zzj;
                    if (zzj < i9) {
                        Double.longBitsToDouble(zzao.zzp(bArr, zzj));
                        throw null;
                    }
                    if (zzj == i9) {
                        return zzj;
                    }
                    throw zzci.zzg();
                }
                if (i5 == 1) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcfVar);
                    Double.longBitsToDouble(zzao.zzp(bArr, i));
                    throw null;
                }
                break;
            case 19:
            case 36:
                if (i5 == 2) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcfVar);
                    int zzj2 = zzao.zzj(bArr, i8, zzanVar);
                    int i10 = zzanVar.zza + zzj2;
                    if (zzj2 < i10) {
                        Float.intBitsToFloat(zzao.zzb(bArr, zzj2));
                        throw null;
                    }
                    if (zzj2 == i10) {
                        return zzj2;
                    }
                    throw zzci.zzg();
                }
                if (i5 == 5) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcfVar);
                    Float.intBitsToFloat(zzao.zzb(bArr, i));
                    throw null;
                }
                break;
            case 20:
            case 21:
            case 37:
            case 38:
                if (i5 == 2) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcfVar);
                    int zzj3 = zzao.zzj(bArr, i8, zzanVar);
                    int i11 = zzanVar.zza + zzj3;
                    if (zzj3 < i11) {
                        zzao.zzm(bArr, zzj3, zzanVar);
                        throw null;
                    }
                    if (zzj3 == i11) {
                        return zzj3;
                    }
                    throw zzci.zzg();
                }
                if (i5 == 0) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcfVar);
                    zzao.zzm(bArr, i8, zzanVar);
                    long j3 = zzanVar.zzb;
                    throw null;
                }
                break;
            case 22:
            case 29:
            case 39:
            case 43:
                if (i5 == 2) {
                    return zzao.zzf(bArr, i8, zzcfVar, zzanVar);
                }
                if (i5 == 0) {
                    return zzao.zzl(i3, bArr, i, i2, zzcfVar, zzanVar);
                }
                break;
            case 23:
            case 32:
            case 40:
            case 46:
                if (i5 == 2) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcfVar);
                    int zzj4 = zzao.zzj(bArr, i8, zzanVar);
                    int i12 = zzanVar.zza + zzj4;
                    if (zzj4 < i12) {
                        zzao.zzp(bArr, zzj4);
                        throw null;
                    }
                    if (zzj4 == i12) {
                        return zzj4;
                    }
                    throw zzci.zzg();
                }
                if (i5 == 1) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcfVar);
                    zzao.zzp(bArr, i);
                    throw null;
                }
                break;
            case 24:
            case 31:
            case 41:
            case 45:
                if (i5 == 2) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcfVar);
                    int zzj5 = zzao.zzj(bArr, i8, zzanVar);
                    int i13 = zzanVar.zza + zzj5;
                    if (zzj5 < i13) {
                        zzao.zzb(bArr, zzj5);
                        throw null;
                    }
                    if (zzj5 == i13) {
                        return zzj5;
                    }
                    throw zzci.zzg();
                }
                if (i5 == 5) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcfVar);
                    zzao.zzb(bArr, i);
                    throw null;
                }
                break;
            case 25:
            case 42:
                if (i5 == 2) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcfVar);
                    int zzj6 = zzao.zzj(bArr, i8, zzanVar);
                    int i14 = zzanVar.zza + zzj6;
                    if (zzj6 < i14) {
                        zzao.zzm(bArr, zzj6, zzanVar);
                        throw null;
                    }
                    if (zzj6 == i14) {
                        return zzj6;
                    }
                    throw zzci.zzg();
                }
                if (i5 == 0) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcfVar);
                    zzao.zzm(bArr, i8, zzanVar);
                    long j4 = zzanVar.zzb;
                    throw null;
                }
                break;
            case 26:
                if (i5 == 2) {
                    long j5 = j & 536870912;
                    i8 = zzao.zzj(bArr, i8, zzanVar);
                    if (j5 == 0) {
                        int i15 = zzanVar.zza;
                        if (i15 < 0) {
                            throw zzci.zzd();
                        }
                        if (i15 != 0) {
                            zzcfVar.add(new String(bArr, i8, i15, zzcg.zzb));
                            i8 += i15;
                            while (i8 < i2) {
                                int zzj7 = zzao.zzj(bArr, i8, zzanVar);
                                if (i3 != zzanVar.zza) {
                                    break;
                                } else {
                                    i8 = zzao.zzj(bArr, zzj7, zzanVar);
                                    int i16 = zzanVar.zza;
                                    if (i16 < 0) {
                                        throw zzci.zzd();
                                    }
                                    if (i16 != 0) {
                                        zzcfVar.add(new String(bArr, i8, i16, zzcg.zzb));
                                        i8 += i16;
                                    }
                                }
                            }
                            break;
                        }
                        zzcfVar.add("");
                        while (i8 < i2) {
                        }
                    } else {
                        int i17 = zzanVar.zza;
                        if (i17 < 0) {
                            throw zzci.zzd();
                        }
                        if (i17 != 0) {
                            int i18 = i8 + i17;
                            if (!zzev.zze(bArr, i8, i18)) {
                                throw zzci.zzc();
                            }
                            zzcfVar.add(new String(bArr, i8, i17, zzcg.zzb));
                            i8 = i18;
                            while (i8 < i2) {
                                int zzj8 = zzao.zzj(bArr, i8, zzanVar);
                                if (i3 != zzanVar.zza) {
                                    break;
                                } else {
                                    i8 = zzao.zzj(bArr, zzj8, zzanVar);
                                    int i19 = zzanVar.zza;
                                    if (i19 < 0) {
                                        throw zzci.zzd();
                                    }
                                    if (i19 != 0) {
                                        int i20 = i8 + i19;
                                        if (!zzev.zze(bArr, i8, i20)) {
                                            throw zzci.zzc();
                                        }
                                        zzcfVar.add(new String(bArr, i8, i19, zzcg.zzb));
                                        i8 = i20;
                                    }
                                }
                            }
                            break;
                        }
                        zzcfVar.add("");
                        while (i8 < i2) {
                        }
                    }
                }
                break;
            case 27:
                if (i5 == 2) {
                    return zzao.zze(zzB(i6), i3, bArr, i, i2, zzcfVar, zzanVar);
                }
                break;
            case 28:
                if (i5 == 2) {
                    int zzj9 = zzao.zzj(bArr, i8, zzanVar);
                    int i21 = zzanVar.zza;
                    if (i21 < 0) {
                        throw zzci.zzd();
                    }
                    if (i21 > bArr.length - zzj9) {
                        throw zzci.zzg();
                    }
                }
                break;
            case 30:
            case 44:
                if (i5 == 2) {
                    zzl = zzao.zzf(bArr, i8, zzcfVar, zzanVar);
                } else if (i5 == 0) {
                    zzl = zzao.zzl(i3, bArr, i, i2, zzcfVar, zzanVar);
                }
                zzce zzA = zzA(i6);
                zzeg zzegVar = this.zzm;
                int i22 = zzdr.$r8$clinit;
                if (zzA != null) {
                    if (zzcfVar instanceof RandomAccess) {
                        int size2 = zzcfVar.size();
                        int i23 = 0;
                        for (int i24 = 0; i24 < size2; i24++) {
                            Integer num = (Integer) zzcfVar.get(i24);
                            int intValue = num.intValue();
                            if (zzA.zza(intValue)) {
                                if (i24 != i23) {
                                    zzcfVar.set(i23, num);
                                }
                                i23++;
                            } else {
                                obj2 = zzdr.zzA(obj, i4, intValue, obj2, zzegVar);
                            }
                        }
                        if (i23 != size2) {
                            zzcfVar.subList(i23, size2).clear();
                            return zzl;
                        }
                    } else {
                        Iterator it = zzcfVar.iterator();
                        while (it.hasNext()) {
                            int intValue2 = ((Integer) it.next()).intValue();
                            if (!zzA.zza(intValue2)) {
                                obj2 = zzdr.zzA(obj, i4, intValue2, obj2, zzegVar);
                                it.remove();
                            }
                        }
                    }
                }
                return zzl;
            case 33:
            case 47:
                if (i5 == 2) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcfVar);
                    int zzj10 = zzao.zzj(bArr, i8, zzanVar);
                    int i25 = zzanVar.zza + zzj10;
                    if (zzj10 >= i25) {
                        if (zzj10 == i25) {
                            return zzj10;
                        }
                        throw zzci.zzg();
                    }
                    zzao.zzj(bArr, zzj10, zzanVar);
                    zzbe.zzb(zzanVar.zza);
                    throw null;
                }
                if (i5 == 0) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcfVar);
                    zzao.zzj(bArr, i8, zzanVar);
                    zzbe.zzb(zzanVar.zza);
                    throw null;
                }
                break;
            case 34:
            case 48:
                if (i5 == 2) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcfVar);
                    int zzj11 = zzao.zzj(bArr, i8, zzanVar);
                    int i26 = zzanVar.zza + zzj11;
                    if (zzj11 >= i26) {
                        if (zzj11 == i26) {
                            return zzj11;
                        }
                        throw zzci.zzg();
                    }
                    zzao.zzm(bArr, zzj11, zzanVar);
                    zzbe.zzc(zzanVar.zzb);
                    throw null;
                }
                if (i5 == 0) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzcfVar);
                    zzao.zzm(bArr, i8, zzanVar);
                    zzbe.zzc(zzanVar.zzb);
                    throw null;
                }
                break;
            default:
                if (i5 == 3) {
                    zzdp zzB = zzB(i6);
                    int i27 = (i3 & (-8)) | 4;
                    int zzc = zzao.zzc(zzB, bArr, i, i2, i27, zzanVar);
                    while (true) {
                        zzcfVar.add(zzanVar.zzc);
                        if (zzc < i2) {
                            int zzj12 = zzao.zzj(bArr, zzc, zzanVar);
                            if (i3 == zzanVar.zza) {
                                zzc = zzao.zzc(zzB, bArr, zzj12, i2, i27, zzanVar);
                            }
                        }
                    }
                    return zzc;
                }
                break;
        }
        return i8;
    }

    private final int zzt(int i) {
        if (i < this.zze || i > this.zzf) {
            return -1;
        }
        return zzw(i, 0);
    }

    private final int zzu(int i, int i2) {
        if (i < this.zze || i > this.zzf) {
            return -1;
        }
        return zzw(i, i2);
    }

    private final int zzv(int i) {
        return this.zzc[i + 2];
    }

    private final int zzw(int i, int i2) {
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

    private static int zzx(int i) {
        return (i >>> 20) & NotificationCenter.notificationsCountUpdated;
    }

    private final int zzy(int i) {
        return this.zzc[i + 1];
    }

    private static long zzz(Object obj, long j) {
        return ((Long) zzeq.zzf(obj, j)).longValue();
    }

    /* JADX WARN: Code restructure failed: missing block: B:186:0x035e, code lost:
    
        if ((r4 instanceof com.google.android.gms.internal.play_billing.zzba) != false) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0113, code lost:
    
        if ((r4 instanceof com.google.android.gms.internal.play_billing.zzba) != false) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0116, code lost:
    
        r5 = r6 << 3;
        r4 = com.google.android.gms.internal.play_billing.zzbi.zzw((java.lang.String) r4);
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:12:0x003b. Please report as an issue. */
    @Override // com.google.android.gms.internal.play_billing.zzdp
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final int zza(Object obj) {
        long zzd;
        Object zzf;
        int zzc;
        int zzc2;
        int zzc3;
        long zzd2;
        int zzl;
        int zzh;
        int zzx;
        int i;
        zzew zzewVar = zzew.zza;
        if (this.zzo - 1 == 0) {
            return zzo(obj);
        }
        Unsafe unsafe = zzb;
        int i2 = 0;
        for (int i3 = 0; i3 < this.zzc.length; i3 += 3) {
            int zzy = zzy(i3);
            int zzx2 = zzx(zzy);
            int i4 = this.zzc[i3];
            int i5 = zzy & 1048575;
            if (zzx2 >= zzbt.zzJ.zza() && zzx2 <= zzbt.zzW.zza()) {
                int i6 = this.zzc[i3 + 2];
            }
            long j = i5;
            switch (zzx2) {
                case 0:
                    if (!zzP(obj, i3)) {
                        break;
                    }
                    zzl = zzbi.zzx(i4 << 3) + 8;
                    i2 += zzl;
                    break;
                case 1:
                    if (!zzP(obj, i3)) {
                        break;
                    }
                    zzl = zzbi.zzx(i4 << 3) + 4;
                    i2 += zzl;
                    break;
                case 2:
                    if (!zzP(obj, i3)) {
                        break;
                    }
                    zzd = zzeq.zzd(obj, j);
                    zzh = zzbi.zzy(zzd);
                    zzx = zzbi.zzx(i4 << 3);
                    i2 += zzx + zzh;
                    break;
                case 3:
                    if (!zzP(obj, i3)) {
                        break;
                    }
                    zzd = zzeq.zzd(obj, j);
                    zzh = zzbi.zzy(zzd);
                    zzx = zzbi.zzx(i4 << 3);
                    i2 += zzx + zzh;
                    break;
                case 4:
                    if (!zzP(obj, i3)) {
                        break;
                    }
                    zzc2 = zzeq.zzc(obj, j);
                    i = i4 << 3;
                    zzh = zzbi.zzu(zzc2);
                    zzx = zzbi.zzx(i);
                    i2 += zzx + zzh;
                    break;
                case 5:
                    if (!zzP(obj, i3)) {
                        break;
                    }
                    zzl = zzbi.zzx(i4 << 3) + 8;
                    i2 += zzl;
                    break;
                case 6:
                    if (!zzP(obj, i3)) {
                        break;
                    }
                    zzl = zzbi.zzx(i4 << 3) + 4;
                    i2 += zzl;
                    break;
                case 7:
                    if (!zzP(obj, i3)) {
                        break;
                    }
                    zzl = zzbi.zzx(i4 << 3) + 1;
                    i2 += zzl;
                    break;
                case 8:
                    if (zzP(obj, i3)) {
                        zzf = zzeq.zzf(obj, j);
                        break;
                    } else {
                        break;
                    }
                case 9:
                    if (!zzP(obj, i3)) {
                        break;
                    }
                    zzl = zzdr.zzn(i4, zzeq.zzf(obj, j), zzB(i3));
                    i2 += zzl;
                    break;
                case 10:
                    if (!zzP(obj, i3)) {
                        break;
                    }
                    zzf = zzeq.zzf(obj, j);
                    int i7 = i4 << 3;
                    int i8 = zzbi.$r8$clinit;
                    int zzd3 = ((zzba) zzf).zzd();
                    zzl = zzbi.zzx(i7) + zzbi.zzx(zzd3) + zzd3;
                    i2 += zzl;
                    break;
                case 11:
                    if (zzP(obj, i3)) {
                        zzc = zzeq.zzc(obj, j);
                        i = i4 << 3;
                        zzh = zzbi.zzx(zzc);
                        zzx = zzbi.zzx(i);
                        i2 += zzx + zzh;
                        break;
                    } else {
                        break;
                    }
                case 12:
                    if (!zzP(obj, i3)) {
                        break;
                    }
                    zzc2 = zzeq.zzc(obj, j);
                    i = i4 << 3;
                    zzh = zzbi.zzu(zzc2);
                    zzx = zzbi.zzx(i);
                    i2 += zzx + zzh;
                    break;
                case 13:
                    if (!zzP(obj, i3)) {
                        break;
                    }
                    zzl = zzbi.zzx(i4 << 3) + 4;
                    i2 += zzl;
                    break;
                case 14:
                    if (!zzP(obj, i3)) {
                        break;
                    }
                    zzl = zzbi.zzx(i4 << 3) + 8;
                    i2 += zzl;
                    break;
                case 15:
                    if (zzP(obj, i3)) {
                        zzc3 = zzeq.zzc(obj, j);
                        zzx = zzbi.zzx(i4 << 3);
                        zzh = zzbi.zzx((zzc3 >> 31) ^ (zzc3 + zzc3));
                        i2 += zzx + zzh;
                        break;
                    } else {
                        break;
                    }
                case 16:
                    if (zzP(obj, i3)) {
                        zzd2 = zzeq.zzd(obj, j);
                        zzl = zzbi.zzx(i4 << 3) + zzbi.zzy((zzd2 >> 63) ^ (zzd2 + zzd2));
                        i2 += zzl;
                        break;
                    } else {
                        break;
                    }
                case 17:
                    if (!zzP(obj, i3)) {
                        break;
                    }
                    zzl = zzbi.zzt(i4, (zzdf) zzeq.zzf(obj, j), zzB(i3));
                    i2 += zzl;
                    break;
                case 18:
                case 23:
                case 32:
                    zzl = zzdr.zzg(i4, (List) zzeq.zzf(obj, j), false);
                    i2 += zzl;
                    break;
                case 19:
                case 24:
                case 31:
                    zzl = zzdr.zze(i4, (List) zzeq.zzf(obj, j), false);
                    i2 += zzl;
                    break;
                case 20:
                    zzl = zzdr.zzl(i4, (List) zzeq.zzf(obj, j), false);
                    i2 += zzl;
                    break;
                case 21:
                    zzl = zzdr.zzw(i4, (List) zzeq.zzf(obj, j), false);
                    i2 += zzl;
                    break;
                case 22:
                    zzl = zzdr.zzj(i4, (List) zzeq.zzf(obj, j), false);
                    i2 += zzl;
                    break;
                case 25:
                    zzl = zzdr.zza(i4, (List) zzeq.zzf(obj, j), false);
                    i2 += zzl;
                    break;
                case 26:
                    zzl = zzdr.zzt(i4, (List) zzeq.zzf(obj, j));
                    i2 += zzl;
                    break;
                case 27:
                    zzl = zzdr.zzo(i4, (List) zzeq.zzf(obj, j), zzB(i3));
                    i2 += zzl;
                    break;
                case 28:
                    zzl = zzdr.zzb(i4, (List) zzeq.zzf(obj, j));
                    i2 += zzl;
                    break;
                case 29:
                    zzl = zzdr.zzu(i4, (List) zzeq.zzf(obj, j), false);
                    i2 += zzl;
                    break;
                case 30:
                    zzl = zzdr.zzc(i4, (List) zzeq.zzf(obj, j), false);
                    i2 += zzl;
                    break;
                case 33:
                    zzl = zzdr.zzp(i4, (List) zzeq.zzf(obj, j), false);
                    i2 += zzl;
                    break;
                case 34:
                    zzl = zzdr.zzr(i4, (List) zzeq.zzf(obj, j), false);
                    i2 += zzl;
                    break;
                case 35:
                    zzh = zzdr.zzh((List) unsafe.getObject(obj, j));
                    if (zzh <= 0) {
                        break;
                    }
                    zzx = zzbi.zzx(i4 << 3) + zzbi.zzx(zzh);
                    i2 += zzx + zzh;
                    break;
                case 36:
                    zzh = zzdr.zzf((List) unsafe.getObject(obj, j));
                    if (zzh <= 0) {
                        break;
                    }
                    zzx = zzbi.zzx(i4 << 3) + zzbi.zzx(zzh);
                    i2 += zzx + zzh;
                    break;
                case 37:
                    zzh = zzdr.zzm((List) unsafe.getObject(obj, j));
                    if (zzh <= 0) {
                        break;
                    }
                    zzx = zzbi.zzx(i4 << 3) + zzbi.zzx(zzh);
                    i2 += zzx + zzh;
                    break;
                case 38:
                    zzh = zzdr.zzx((List) unsafe.getObject(obj, j));
                    if (zzh <= 0) {
                        break;
                    }
                    zzx = zzbi.zzx(i4 << 3) + zzbi.zzx(zzh);
                    i2 += zzx + zzh;
                    break;
                case 39:
                    zzh = zzdr.zzk((List) unsafe.getObject(obj, j));
                    if (zzh <= 0) {
                        break;
                    }
                    zzx = zzbi.zzx(i4 << 3) + zzbi.zzx(zzh);
                    i2 += zzx + zzh;
                    break;
                case 40:
                    zzh = zzdr.zzh((List) unsafe.getObject(obj, j));
                    if (zzh <= 0) {
                        break;
                    }
                    zzx = zzbi.zzx(i4 << 3) + zzbi.zzx(zzh);
                    i2 += zzx + zzh;
                    break;
                case 41:
                    zzh = zzdr.zzf((List) unsafe.getObject(obj, j));
                    if (zzh <= 0) {
                        break;
                    }
                    zzx = zzbi.zzx(i4 << 3) + zzbi.zzx(zzh);
                    i2 += zzx + zzh;
                    break;
                case 42:
                    List list = (List) unsafe.getObject(obj, j);
                    int i9 = zzdr.$r8$clinit;
                    zzh = list.size();
                    if (zzh <= 0) {
                        break;
                    }
                    zzx = zzbi.zzx(i4 << 3) + zzbi.zzx(zzh);
                    i2 += zzx + zzh;
                    break;
                case 43:
                    zzh = zzdr.zzv((List) unsafe.getObject(obj, j));
                    if (zzh <= 0) {
                        break;
                    }
                    zzx = zzbi.zzx(i4 << 3) + zzbi.zzx(zzh);
                    i2 += zzx + zzh;
                    break;
                case 44:
                    zzh = zzdr.zzd((List) unsafe.getObject(obj, j));
                    if (zzh <= 0) {
                        break;
                    }
                    zzx = zzbi.zzx(i4 << 3) + zzbi.zzx(zzh);
                    i2 += zzx + zzh;
                    break;
                case 45:
                    zzh = zzdr.zzf((List) unsafe.getObject(obj, j));
                    if (zzh <= 0) {
                        break;
                    }
                    zzx = zzbi.zzx(i4 << 3) + zzbi.zzx(zzh);
                    i2 += zzx + zzh;
                    break;
                case 46:
                    zzh = zzdr.zzh((List) unsafe.getObject(obj, j));
                    if (zzh <= 0) {
                        break;
                    }
                    zzx = zzbi.zzx(i4 << 3) + zzbi.zzx(zzh);
                    i2 += zzx + zzh;
                    break;
                case 47:
                    zzh = zzdr.zzq((List) unsafe.getObject(obj, j));
                    if (zzh <= 0) {
                        break;
                    }
                    zzx = zzbi.zzx(i4 << 3) + zzbi.zzx(zzh);
                    i2 += zzx + zzh;
                    break;
                case 48:
                    zzh = zzdr.zzs((List) unsafe.getObject(obj, j));
                    if (zzh <= 0) {
                        break;
                    }
                    zzx = zzbi.zzx(i4 << 3) + zzbi.zzx(zzh);
                    i2 += zzx + zzh;
                    break;
                case 49:
                    zzl = zzdr.zzi(i4, (List) zzeq.zzf(obj, j), zzB(i3));
                    i2 += zzl;
                    break;
                case 50:
                    zzda.zza(i4, zzeq.zzf(obj, j), zzC(i3));
                    break;
                case 51:
                    if (!zzT(obj, i4, i3)) {
                        break;
                    }
                    zzl = zzbi.zzx(i4 << 3) + 8;
                    i2 += zzl;
                    break;
                case 52:
                    if (!zzT(obj, i4, i3)) {
                        break;
                    }
                    zzl = zzbi.zzx(i4 << 3) + 4;
                    i2 += zzl;
                    break;
                case 53:
                    if (!zzT(obj, i4, i3)) {
                        break;
                    }
                    zzd = zzz(obj, j);
                    zzh = zzbi.zzy(zzd);
                    zzx = zzbi.zzx(i4 << 3);
                    i2 += zzx + zzh;
                    break;
                case 54:
                    if (!zzT(obj, i4, i3)) {
                        break;
                    }
                    zzd = zzz(obj, j);
                    zzh = zzbi.zzy(zzd);
                    zzx = zzbi.zzx(i4 << 3);
                    i2 += zzx + zzh;
                    break;
                case 55:
                    if (!zzT(obj, i4, i3)) {
                        break;
                    }
                    zzc2 = zzp(obj, j);
                    i = i4 << 3;
                    zzh = zzbi.zzu(zzc2);
                    zzx = zzbi.zzx(i);
                    i2 += zzx + zzh;
                    break;
                case 56:
                    if (!zzT(obj, i4, i3)) {
                        break;
                    }
                    zzl = zzbi.zzx(i4 << 3) + 8;
                    i2 += zzl;
                    break;
                case 57:
                    if (!zzT(obj, i4, i3)) {
                        break;
                    }
                    zzl = zzbi.zzx(i4 << 3) + 4;
                    i2 += zzl;
                    break;
                case 58:
                    if (!zzT(obj, i4, i3)) {
                        break;
                    }
                    zzl = zzbi.zzx(i4 << 3) + 1;
                    i2 += zzl;
                    break;
                case 59:
                    if (zzT(obj, i4, i3)) {
                        zzf = zzeq.zzf(obj, j);
                        break;
                    } else {
                        break;
                    }
                case 60:
                    if (!zzT(obj, i4, i3)) {
                        break;
                    }
                    zzl = zzdr.zzn(i4, zzeq.zzf(obj, j), zzB(i3));
                    i2 += zzl;
                    break;
                case 61:
                    if (!zzT(obj, i4, i3)) {
                        break;
                    }
                    zzf = zzeq.zzf(obj, j);
                    int i72 = i4 << 3;
                    int i82 = zzbi.$r8$clinit;
                    int zzd32 = ((zzba) zzf).zzd();
                    zzl = zzbi.zzx(i72) + zzbi.zzx(zzd32) + zzd32;
                    i2 += zzl;
                    break;
                case 62:
                    if (zzT(obj, i4, i3)) {
                        zzc = zzp(obj, j);
                        i = i4 << 3;
                        zzh = zzbi.zzx(zzc);
                        zzx = zzbi.zzx(i);
                        i2 += zzx + zzh;
                        break;
                    } else {
                        break;
                    }
                case 63:
                    if (!zzT(obj, i4, i3)) {
                        break;
                    }
                    zzc2 = zzp(obj, j);
                    i = i4 << 3;
                    zzh = zzbi.zzu(zzc2);
                    zzx = zzbi.zzx(i);
                    i2 += zzx + zzh;
                    break;
                case 64:
                    if (!zzT(obj, i4, i3)) {
                        break;
                    }
                    zzl = zzbi.zzx(i4 << 3) + 4;
                    i2 += zzl;
                    break;
                case 65:
                    if (!zzT(obj, i4, i3)) {
                        break;
                    }
                    zzl = zzbi.zzx(i4 << 3) + 8;
                    i2 += zzl;
                    break;
                case 66:
                    if (zzT(obj, i4, i3)) {
                        zzc3 = zzp(obj, j);
                        zzx = zzbi.zzx(i4 << 3);
                        zzh = zzbi.zzx((zzc3 >> 31) ^ (zzc3 + zzc3));
                        i2 += zzx + zzh;
                        break;
                    } else {
                        break;
                    }
                case 67:
                    if (zzT(obj, i4, i3)) {
                        zzd2 = zzz(obj, j);
                        zzl = zzbi.zzx(i4 << 3) + zzbi.zzy((zzd2 >> 63) ^ (zzd2 + zzd2));
                        i2 += zzl;
                        break;
                    } else {
                        break;
                    }
                case 68:
                    if (!zzT(obj, i4, i3)) {
                        break;
                    }
                    zzl = zzbi.zzt(i4, (zzdf) zzeq.zzf(obj, j), zzB(i3));
                    i2 += zzl;
                    break;
            }
        }
        zzeg zzegVar = this.zzm;
        return i2 + zzegVar.zza(zzegVar.zzd(obj));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x00f7, code lost:
    
        if (r3 != null) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x00f9, code lost:
    
        r7 = r3.hashCode();
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x00fd, code lost:
    
        r2 = (r2 * 53) + r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x0115, code lost:
    
        if (r3 != null) goto L69;
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x001c. Please report as an issue. */
    @Override // com.google.android.gms.internal.play_billing.zzdp
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final int zzb(Object obj) {
        int i;
        double zza2;
        int i2;
        float zzb2;
        boolean zzw;
        Object zzf;
        int zzc;
        long zzd;
        Object zzf2;
        int length = this.zzc.length;
        int i3 = 0;
        for (int i4 = 0; i4 < length; i4 += 3) {
            int zzy = zzy(i4);
            int i5 = this.zzc[i4];
            long j = 1048575 & zzy;
            int i6 = 37;
            switch (zzx(zzy)) {
                case 0:
                    i = i3 * 53;
                    zza2 = zzeq.zza(obj, j);
                    zzd = Double.doubleToLongBits(zza2);
                    byte[] bArr = zzcg.zzd;
                    i3 = i + ((int) (zzd ^ (zzd >>> 32)));
                    break;
                case 1:
                    i2 = i3 * 53;
                    zzb2 = zzeq.zzb(obj, j);
                    zzc = Float.floatToIntBits(zzb2);
                    i3 = i2 + zzc;
                    break;
                case 2:
                case 3:
                case 5:
                case 14:
                case 16:
                    i = i3 * 53;
                    zzd = zzeq.zzd(obj, j);
                    byte[] bArr2 = zzcg.zzd;
                    i3 = i + ((int) (zzd ^ (zzd >>> 32)));
                    break;
                case 4:
                case 6:
                case 11:
                case 12:
                case 13:
                case 15:
                    i2 = i3 * 53;
                    zzc = zzeq.zzc(obj, j);
                    i3 = i2 + zzc;
                    break;
                case 7:
                    i2 = i3 * 53;
                    zzw = zzeq.zzw(obj, j);
                    zzc = zzcg.zza(zzw);
                    i3 = i2 + zzc;
                    break;
                case 8:
                    i2 = i3 * 53;
                    zzc = ((String) zzeq.zzf(obj, j)).hashCode();
                    i3 = i2 + zzc;
                    break;
                case 9:
                    zzf = zzeq.zzf(obj, j);
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
                    zzf2 = zzeq.zzf(obj, j);
                    zzc = zzf2.hashCode();
                    i3 = i2 + zzc;
                    break;
                case 17:
                    zzf = zzeq.zzf(obj, j);
                    break;
                case 51:
                    if (zzT(obj, i5, i4)) {
                        i = i3 * 53;
                        zza2 = zzm(obj, j);
                        zzd = Double.doubleToLongBits(zza2);
                        byte[] bArr22 = zzcg.zzd;
                        i3 = i + ((int) (zzd ^ (zzd >>> 32)));
                        break;
                    } else {
                        break;
                    }
                case 52:
                    if (zzT(obj, i5, i4)) {
                        i2 = i3 * 53;
                        zzb2 = zzn(obj, j);
                        zzc = Float.floatToIntBits(zzb2);
                        i3 = i2 + zzc;
                        break;
                    } else {
                        break;
                    }
                case 53:
                    if (!zzT(obj, i5, i4)) {
                        break;
                    }
                    i = i3 * 53;
                    zzd = zzz(obj, j);
                    byte[] bArr222 = zzcg.zzd;
                    i3 = i + ((int) (zzd ^ (zzd >>> 32)));
                    break;
                case 54:
                    if (!zzT(obj, i5, i4)) {
                        break;
                    }
                    i = i3 * 53;
                    zzd = zzz(obj, j);
                    byte[] bArr2222 = zzcg.zzd;
                    i3 = i + ((int) (zzd ^ (zzd >>> 32)));
                    break;
                case 55:
                    if (!zzT(obj, i5, i4)) {
                        break;
                    }
                    i2 = i3 * 53;
                    zzc = zzp(obj, j);
                    i3 = i2 + zzc;
                    break;
                case 56:
                    if (!zzT(obj, i5, i4)) {
                        break;
                    }
                    i = i3 * 53;
                    zzd = zzz(obj, j);
                    byte[] bArr22222 = zzcg.zzd;
                    i3 = i + ((int) (zzd ^ (zzd >>> 32)));
                    break;
                case 57:
                    if (!zzT(obj, i5, i4)) {
                        break;
                    }
                    i2 = i3 * 53;
                    zzc = zzp(obj, j);
                    i3 = i2 + zzc;
                    break;
                case 58:
                    if (zzT(obj, i5, i4)) {
                        i2 = i3 * 53;
                        zzw = zzU(obj, j);
                        zzc = zzcg.zza(zzw);
                        i3 = i2 + zzc;
                        break;
                    } else {
                        break;
                    }
                case 59:
                    if (!zzT(obj, i5, i4)) {
                        break;
                    }
                    i2 = i3 * 53;
                    zzc = ((String) zzeq.zzf(obj, j)).hashCode();
                    i3 = i2 + zzc;
                    break;
                case 60:
                    if (!zzT(obj, i5, i4)) {
                        break;
                    }
                    zzf2 = zzeq.zzf(obj, j);
                    i2 = i3 * 53;
                    zzc = zzf2.hashCode();
                    i3 = i2 + zzc;
                    break;
                case 61:
                    if (!zzT(obj, i5, i4)) {
                        break;
                    }
                    i2 = i3 * 53;
                    zzf2 = zzeq.zzf(obj, j);
                    zzc = zzf2.hashCode();
                    i3 = i2 + zzc;
                    break;
                case 62:
                    if (!zzT(obj, i5, i4)) {
                        break;
                    }
                    i2 = i3 * 53;
                    zzc = zzp(obj, j);
                    i3 = i2 + zzc;
                    break;
                case 63:
                    if (!zzT(obj, i5, i4)) {
                        break;
                    }
                    i2 = i3 * 53;
                    zzc = zzp(obj, j);
                    i3 = i2 + zzc;
                    break;
                case 64:
                    if (!zzT(obj, i5, i4)) {
                        break;
                    }
                    i2 = i3 * 53;
                    zzc = zzp(obj, j);
                    i3 = i2 + zzc;
                    break;
                case 65:
                    if (!zzT(obj, i5, i4)) {
                        break;
                    }
                    i = i3 * 53;
                    zzd = zzz(obj, j);
                    byte[] bArr222222 = zzcg.zzd;
                    i3 = i + ((int) (zzd ^ (zzd >>> 32)));
                    break;
                case 66:
                    if (!zzT(obj, i5, i4)) {
                        break;
                    }
                    i2 = i3 * 53;
                    zzc = zzp(obj, j);
                    i3 = i2 + zzc;
                    break;
                case 67:
                    if (!zzT(obj, i5, i4)) {
                        break;
                    }
                    i = i3 * 53;
                    zzd = zzz(obj, j);
                    byte[] bArr2222222 = zzcg.zzd;
                    i3 = i + ((int) (zzd ^ (zzd >>> 32)));
                    break;
                case 68:
                    if (!zzT(obj, i5, i4)) {
                        break;
                    }
                    zzf2 = zzeq.zzf(obj, j);
                    i2 = i3 * 53;
                    zzc = zzf2.hashCode();
                    i3 = i2 + zzc;
                    break;
            }
        }
        int hashCode = (i3 * 53) + this.zzm.zzd(obj).hashCode();
        if (!this.zzh) {
            return hashCode;
        }
        this.zzn.zza(obj);
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Failed to find 'out' block for switch in B:105:0x008b. Please report as an issue. */
    public final int zzc(Object obj, byte[] bArr, int i, int i2, int i3, zzan zzanVar) {
        Unsafe unsafe;
        int i4;
        zzdi zzdiVar;
        Object obj2;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        zzbn zzbnVar;
        int i13;
        int i14;
        byte[] bArr2;
        int i15;
        int i16;
        int i17;
        int i18;
        int i19;
        int i20;
        int i21;
        byte[] bArr3;
        long j;
        long j2;
        int zzb2;
        int i22;
        int i23;
        zzdi zzdiVar2 = this;
        Object obj3 = obj;
        byte[] bArr4 = bArr;
        int i24 = i2;
        int i25 = i3;
        zzan zzanVar2 = zzanVar;
        zzG(obj);
        Unsafe unsafe2 = zzb;
        int i26 = -1;
        int i27 = i;
        int i28 = -1;
        int i29 = 0;
        int i30 = 0;
        int i31 = 0;
        int i32 = 1048575;
        while (true) {
            if (i27 < i24) {
                int i33 = i27 + 1;
                byte b = bArr4[i27];
                if (b < 0) {
                    int zzk = zzao.zzk(b, bArr4, i33, zzanVar2);
                    i5 = zzanVar2.zza;
                    i33 = zzk;
                } else {
                    i5 = b;
                }
                int i34 = i5 >>> 3;
                int zzu = i34 > i28 ? zzdiVar2.zzu(i34, i29 / 3) : zzdiVar2.zzt(i34);
                if (zzu == i26) {
                    i6 = i34;
                    i7 = i33;
                    i8 = i5;
                    i9 = i31;
                    unsafe = unsafe2;
                    i10 = i25;
                    i11 = 0;
                } else {
                    int i35 = i5 & 7;
                    int[] iArr = zzdiVar2.zzc;
                    int i36 = iArr[zzu + 1];
                    int zzx = zzx(i36);
                    int i37 = i5;
                    long j3 = i36 & 1048575;
                    if (zzx <= 17) {
                        int i38 = iArr[zzu + 2];
                        int i39 = 1 << (i38 >>> 20);
                        int i40 = i38 & 1048575;
                        if (i40 != i32) {
                            if (i32 != 1048575) {
                                unsafe2.putInt(obj3, i32, i31);
                            }
                            i14 = i40;
                            i13 = unsafe2.getInt(obj3, i40);
                        } else {
                            i13 = i31;
                            i14 = i32;
                        }
                        switch (zzx) {
                            case 0:
                                bArr2 = bArr;
                                i15 = i34;
                                i16 = zzu;
                                i17 = i14;
                                i18 = i33;
                                i19 = i39;
                                i20 = i37;
                                if (i35 != 1) {
                                    i32 = i17;
                                    i10 = i3;
                                    unsafe = unsafe2;
                                    i11 = i16;
                                    i9 = i13;
                                    i7 = i18;
                                    i6 = i15;
                                    i8 = i20;
                                    break;
                                } else {
                                    zzeq.zzo(obj3, j3, Double.longBitsToDouble(zzao.zzp(bArr2, i18)));
                                    i27 = i18 + 8;
                                    i31 = i13 | i19;
                                    i24 = i2;
                                    bArr4 = bArr2;
                                    i29 = i16;
                                    i28 = i15;
                                    i30 = i20;
                                    i26 = -1;
                                    i32 = i17;
                                    i25 = i3;
                                }
                            case 1:
                                bArr2 = bArr;
                                i15 = i34;
                                i16 = zzu;
                                i17 = i14;
                                i18 = i33;
                                i19 = i39;
                                i20 = i37;
                                if (i35 != 5) {
                                    i32 = i17;
                                    i10 = i3;
                                    unsafe = unsafe2;
                                    i11 = i16;
                                    i9 = i13;
                                    i7 = i18;
                                    i6 = i15;
                                    i8 = i20;
                                    break;
                                } else {
                                    zzeq.zzp(obj3, j3, Float.intBitsToFloat(zzao.zzb(bArr2, i18)));
                                    i27 = i18 + 4;
                                    i31 = i13 | i19;
                                    i24 = i2;
                                    bArr4 = bArr2;
                                    i29 = i16;
                                    i28 = i15;
                                    i30 = i20;
                                    i26 = -1;
                                    i32 = i17;
                                    i25 = i3;
                                }
                            case 2:
                            case 3:
                                i15 = i34;
                                i16 = zzu;
                                i17 = i14;
                                i18 = i33;
                                i20 = i37;
                                if (i35 != 0) {
                                    i32 = i17;
                                    i10 = i3;
                                    unsafe = unsafe2;
                                    i11 = i16;
                                    i9 = i13;
                                    i7 = i18;
                                    i6 = i15;
                                    i8 = i20;
                                    break;
                                } else {
                                    int zzm = zzao.zzm(bArr, i18, zzanVar2);
                                    unsafe2.putLong(obj, j3, zzanVar2.zzb);
                                    i31 = i13 | i39;
                                    bArr4 = bArr;
                                    i29 = i16;
                                    i27 = zzm;
                                    i28 = i15;
                                    i30 = i20;
                                    i26 = -1;
                                    i32 = i17;
                                    i24 = i2;
                                    i25 = i3;
                                }
                            case 4:
                            case 11:
                                bArr2 = bArr;
                                i15 = i34;
                                i16 = zzu;
                                i17 = i14;
                                i18 = i33;
                                i19 = i39;
                                i20 = i37;
                                if (i35 != 0) {
                                    i32 = i17;
                                    i10 = i3;
                                    unsafe = unsafe2;
                                    i11 = i16;
                                    i9 = i13;
                                    i7 = i18;
                                    i6 = i15;
                                    i8 = i20;
                                    break;
                                } else {
                                    i27 = zzao.zzj(bArr2, i18, zzanVar2);
                                    unsafe2.putInt(obj3, j3, zzanVar2.zza);
                                    i31 = i13 | i19;
                                    i24 = i2;
                                    bArr4 = bArr2;
                                    i29 = i16;
                                    i28 = i15;
                                    i30 = i20;
                                    i26 = -1;
                                    i32 = i17;
                                    i25 = i3;
                                }
                            case 5:
                            case 14:
                                bArr2 = bArr;
                                i15 = i34;
                                i16 = zzu;
                                i17 = i14;
                                i21 = i37;
                                i19 = i39;
                                if (i35 != 1) {
                                    i20 = i21;
                                    i18 = i33;
                                    i32 = i17;
                                    i10 = i3;
                                    unsafe = unsafe2;
                                    i11 = i16;
                                    i9 = i13;
                                    i7 = i18;
                                    i6 = i15;
                                    i8 = i20;
                                    break;
                                } else {
                                    i20 = i21;
                                    i18 = i33;
                                    unsafe2.putLong(obj, j3, zzao.zzp(bArr2, i33));
                                    i27 = i18 + 8;
                                    i31 = i13 | i19;
                                    i24 = i2;
                                    bArr4 = bArr2;
                                    i29 = i16;
                                    i28 = i15;
                                    i30 = i20;
                                    i26 = -1;
                                    i32 = i17;
                                    i25 = i3;
                                }
                            case 6:
                            case 13:
                                bArr3 = bArr;
                                i15 = i34;
                                i16 = zzu;
                                i17 = i14;
                                i21 = i37;
                                if (i35 != 5) {
                                    i20 = i21;
                                    i18 = i33;
                                    i32 = i17;
                                    i10 = i3;
                                    unsafe = unsafe2;
                                    i11 = i16;
                                    i9 = i13;
                                    i7 = i18;
                                    i6 = i15;
                                    i8 = i20;
                                    break;
                                } else {
                                    unsafe2.putInt(obj3, j3, zzao.zzb(bArr3, i33));
                                    i27 = i33 + 4;
                                    i31 = i13 | i39;
                                    bArr4 = bArr3;
                                    i29 = i16;
                                    i30 = i21;
                                    i28 = i15;
                                    i26 = -1;
                                    i32 = i17;
                                    i24 = i2;
                                    i25 = i3;
                                }
                            case 7:
                                bArr3 = bArr;
                                i15 = i34;
                                i16 = zzu;
                                i17 = i14;
                                i21 = i37;
                                if (i35 != 0) {
                                    i20 = i21;
                                    i18 = i33;
                                    i32 = i17;
                                    i10 = i3;
                                    unsafe = unsafe2;
                                    i11 = i16;
                                    i9 = i13;
                                    i7 = i18;
                                    i6 = i15;
                                    i8 = i20;
                                    break;
                                } else {
                                    i27 = zzao.zzm(bArr3, i33, zzanVar2);
                                    zzeq.zzm(obj3, j3, zzanVar2.zzb != 0);
                                    i31 = i13 | i39;
                                    bArr4 = bArr3;
                                    i29 = i16;
                                    i30 = i21;
                                    i28 = i15;
                                    i26 = -1;
                                    i32 = i17;
                                    i24 = i2;
                                    i25 = i3;
                                }
                            case 8:
                                bArr3 = bArr;
                                i15 = i34;
                                i16 = zzu;
                                i17 = i14;
                                i21 = i37;
                                j = j3;
                                if (i35 != 2) {
                                    i20 = i21;
                                    i18 = i33;
                                    i32 = i17;
                                    i10 = i3;
                                    unsafe = unsafe2;
                                    i11 = i16;
                                    i9 = i13;
                                    i7 = i18;
                                    i6 = i15;
                                    i8 = i20;
                                    break;
                                } else {
                                    i27 = (536870912 & i36) == 0 ? zzao.zzg(bArr3, i33, zzanVar2) : zzao.zzh(bArr3, i33, zzanVar2);
                                    unsafe2.putObject(obj3, j, zzanVar2.zzc);
                                    i31 = i13 | i39;
                                    bArr4 = bArr3;
                                    i29 = i16;
                                    i30 = i21;
                                    i28 = i15;
                                    i26 = -1;
                                    i32 = i17;
                                    i24 = i2;
                                    i25 = i3;
                                }
                            case 9:
                                bArr3 = bArr;
                                i15 = i34;
                                i16 = zzu;
                                i17 = i14;
                                i21 = i37;
                                if (i35 != 2) {
                                    i20 = i21;
                                    i18 = i33;
                                    i32 = i17;
                                    i10 = i3;
                                    unsafe = unsafe2;
                                    i11 = i16;
                                    i9 = i13;
                                    i7 = i18;
                                    i6 = i15;
                                    i8 = i20;
                                    break;
                                } else {
                                    Object zzD = zzdiVar2.zzD(obj3, i16);
                                    i27 = zzao.zzo(zzD, zzdiVar2.zzB(i16), bArr, i33, i2, zzanVar);
                                    zzdiVar2.zzL(obj3, i16, zzD);
                                    i31 = i13 | i39;
                                    bArr4 = bArr3;
                                    i29 = i16;
                                    i30 = i21;
                                    i28 = i15;
                                    i26 = -1;
                                    i32 = i17;
                                    i24 = i2;
                                    i25 = i3;
                                }
                            case 10:
                                bArr3 = bArr;
                                i15 = i34;
                                i16 = zzu;
                                i17 = i14;
                                i21 = i37;
                                j = j3;
                                if (i35 != 2) {
                                    i20 = i21;
                                    i18 = i33;
                                    i32 = i17;
                                    i10 = i3;
                                    unsafe = unsafe2;
                                    i11 = i16;
                                    i9 = i13;
                                    i7 = i18;
                                    i6 = i15;
                                    i8 = i20;
                                    break;
                                } else {
                                    i27 = zzao.zza(bArr3, i33, zzanVar2);
                                    unsafe2.putObject(obj3, j, zzanVar2.zzc);
                                    i31 = i13 | i39;
                                    bArr4 = bArr3;
                                    i29 = i16;
                                    i30 = i21;
                                    i28 = i15;
                                    i26 = -1;
                                    i32 = i17;
                                    i24 = i2;
                                    i25 = i3;
                                }
                            case 12:
                                bArr3 = bArr;
                                i15 = i34;
                                i16 = zzu;
                                i17 = i14;
                                i21 = i37;
                                j2 = j3;
                                if (i35 != 0) {
                                    i20 = i21;
                                    i18 = i33;
                                    i32 = i17;
                                    i10 = i3;
                                    unsafe = unsafe2;
                                    i11 = i16;
                                    i9 = i13;
                                    i7 = i18;
                                    i6 = i15;
                                    i8 = i20;
                                    break;
                                } else {
                                    i27 = zzao.zzj(bArr3, i33, zzanVar2);
                                    zzb2 = zzanVar2.zza;
                                    zzce zzA = zzdiVar2.zzA(i16);
                                    if (zzA != null && !zzA.zza(zzb2)) {
                                        zzd(obj).zzj(i21, Long.valueOf(zzb2));
                                        i29 = i16;
                                        i31 = i13;
                                        i30 = i21;
                                        i28 = i15;
                                        i26 = -1;
                                        i24 = i2;
                                        i25 = i3;
                                        bArr4 = bArr3;
                                        i32 = i17;
                                    }
                                    unsafe2.putInt(obj3, j2, zzb2);
                                    i31 = i13 | i39;
                                    bArr4 = bArr3;
                                    i29 = i16;
                                    i30 = i21;
                                    i28 = i15;
                                    i26 = -1;
                                    i32 = i17;
                                    i24 = i2;
                                    i25 = i3;
                                }
                                break;
                            case 15:
                                bArr3 = bArr;
                                i15 = i34;
                                i16 = zzu;
                                i17 = i14;
                                i21 = i37;
                                j2 = j3;
                                if (i35 != 0) {
                                    i20 = i21;
                                    i18 = i33;
                                    i32 = i17;
                                    i10 = i3;
                                    unsafe = unsafe2;
                                    i11 = i16;
                                    i9 = i13;
                                    i7 = i18;
                                    i6 = i15;
                                    i8 = i20;
                                    break;
                                } else {
                                    i27 = zzao.zzj(bArr3, i33, zzanVar2);
                                    zzb2 = zzbe.zzb(zzanVar2.zza);
                                    unsafe2.putInt(obj3, j2, zzb2);
                                    i31 = i13 | i39;
                                    bArr4 = bArr3;
                                    i29 = i16;
                                    i30 = i21;
                                    i28 = i15;
                                    i26 = -1;
                                    i32 = i17;
                                    i24 = i2;
                                    i25 = i3;
                                }
                            case 16:
                                i15 = i34;
                                i16 = zzu;
                                i17 = i14;
                                i21 = i37;
                                if (i35 != 0) {
                                    i20 = i21;
                                    i18 = i33;
                                    i32 = i17;
                                    i10 = i3;
                                    unsafe = unsafe2;
                                    i11 = i16;
                                    i9 = i13;
                                    i7 = i18;
                                    i6 = i15;
                                    i8 = i20;
                                    break;
                                } else {
                                    int zzm2 = zzao.zzm(bArr, i33, zzanVar2);
                                    unsafe2.putLong(obj, j3, zzbe.zzc(zzanVar2.zzb));
                                    i31 = i13 | i39;
                                    bArr4 = bArr;
                                    i27 = zzm2;
                                    i29 = i16;
                                    i30 = i21;
                                    i28 = i15;
                                    i26 = -1;
                                    i32 = i17;
                                    i24 = i2;
                                    i25 = i3;
                                }
                            default:
                                if (i35 != 3) {
                                    i15 = i34;
                                    i16 = zzu;
                                    i17 = i14;
                                    i18 = i33;
                                    i20 = i37;
                                    i32 = i17;
                                    i10 = i3;
                                    unsafe = unsafe2;
                                    i11 = i16;
                                    i9 = i13;
                                    i7 = i18;
                                    i6 = i15;
                                    i8 = i20;
                                    break;
                                } else {
                                    Object zzD2 = zzdiVar2.zzD(obj3, zzu);
                                    i27 = zzao.zzn(zzD2, zzdiVar2.zzB(zzu), bArr, i33, i2, (i34 << 3) | 4, zzanVar);
                                    zzdiVar2.zzL(obj3, zzu, zzD2);
                                    i31 = i13 | i39;
                                    bArr4 = bArr;
                                    i32 = i14;
                                    i29 = zzu;
                                    i30 = i37;
                                    i28 = i34;
                                    i26 = -1;
                                    i24 = i2;
                                    i25 = i3;
                                }
                        }
                    } else {
                        int i41 = i33;
                        if (zzx != 27) {
                            i9 = i31;
                            i22 = i32;
                            if (zzx <= 49) {
                                unsafe = unsafe2;
                                i11 = zzu;
                                i6 = i34;
                                i27 = zzs(obj, bArr, i41, i2, i37, i34, i35, zzu, i36, zzx, j3, zzanVar);
                                if (i27 != i41) {
                                    zzdiVar2 = this;
                                    obj3 = obj;
                                    bArr4 = bArr;
                                    i24 = i2;
                                    i25 = i3;
                                    zzanVar2 = zzanVar;
                                    i31 = i9;
                                    i30 = i37;
                                    i32 = i22;
                                    i29 = i11;
                                    i28 = i6;
                                    unsafe2 = unsafe;
                                    i26 = -1;
                                } else {
                                    i7 = i27;
                                    i8 = i37;
                                    i32 = i22;
                                    i10 = i3;
                                }
                            } else {
                                unsafe = unsafe2;
                                i11 = zzu;
                                i6 = i34;
                                i23 = i41;
                                if (zzx != 50) {
                                    i27 = zzr(obj, bArr, i23, i2, i37, i6, i35, i36, zzx, j3, i11, zzanVar);
                                    if (i27 != i23) {
                                        zzdiVar2 = this;
                                        obj3 = obj;
                                        bArr4 = bArr;
                                        i24 = i2;
                                        i25 = i3;
                                        zzanVar2 = zzanVar;
                                        i31 = i9;
                                        i30 = i37;
                                        i32 = i22;
                                        i29 = i11;
                                        i28 = i6;
                                        unsafe2 = unsafe;
                                        i26 = -1;
                                    } else {
                                        i7 = i27;
                                        i8 = i37;
                                        i32 = i22;
                                        i10 = i3;
                                    }
                                } else if (i35 == 2) {
                                    i27 = zzq(obj, bArr, i23, i2, i11, j3, zzanVar);
                                    if (i27 != i23) {
                                        zzdiVar2 = this;
                                        obj3 = obj;
                                        bArr4 = bArr;
                                        i24 = i2;
                                        i25 = i3;
                                        zzanVar2 = zzanVar;
                                        i31 = i9;
                                        i30 = i37;
                                        i32 = i22;
                                        i29 = i11;
                                        i28 = i6;
                                        unsafe2 = unsafe;
                                        i26 = -1;
                                    } else {
                                        i7 = i27;
                                        i8 = i37;
                                        i32 = i22;
                                        i10 = i3;
                                    }
                                }
                            }
                        } else if (i35 == 2) {
                            zzcf zzcfVar = (zzcf) unsafe2.getObject(obj3, j3);
                            if (!zzcfVar.zzc()) {
                                int size = zzcfVar.size();
                                zzcfVar = zzcfVar.zzd(size == 0 ? 10 : size + size);
                                unsafe2.putObject(obj3, j3, zzcfVar);
                            }
                            i27 = zzao.zze(zzdiVar2.zzB(zzu), i37, bArr, i41, i2, zzcfVar, zzanVar);
                            i24 = i2;
                            i29 = zzu;
                            i28 = i34;
                            i31 = i31;
                            i30 = i37;
                            i32 = i32;
                            i26 = -1;
                            bArr4 = bArr;
                            i25 = i3;
                        } else {
                            i9 = i31;
                            i22 = i32;
                            unsafe = unsafe2;
                            i11 = zzu;
                            i6 = i34;
                            i23 = i41;
                        }
                        i10 = i3;
                        i7 = i23;
                        i8 = i37;
                        i32 = i22;
                    }
                }
                if (i8 != i10 || i10 == 0) {
                    int i42 = i10;
                    if (!this.zzh || (zzbnVar = zzanVar.zzd) == zzbn.zza) {
                        i12 = i6;
                        i27 = zzao.zzi(i8, bArr, i7, i2, zzd(obj), zzanVar);
                    } else {
                        i12 = i6;
                        zzbnVar.zzb(this.zzg, i12);
                        i27 = zzao.zzi(i8, bArr, i7, i2, zzd(obj), zzanVar);
                    }
                    obj3 = obj;
                    bArr4 = bArr;
                    i24 = i2;
                    i30 = i8;
                    zzdiVar2 = this;
                    i28 = i12;
                    i31 = i9;
                    i29 = i11;
                    i26 = -1;
                    i25 = i42;
                    zzanVar2 = zzanVar;
                    unsafe2 = unsafe;
                } else {
                    zzdiVar = this;
                    i4 = i10;
                    i27 = i7;
                    i30 = i8;
                    i31 = i9;
                }
            } else {
                unsafe = unsafe2;
                i4 = i25;
                zzdiVar = zzdiVar2;
            }
        }
        if (i32 != 1048575) {
            obj2 = obj;
            unsafe.putInt(obj2, i32, i31);
        } else {
            obj2 = obj;
        }
        for (int i43 = zzdiVar.zzj; i43 < zzdiVar.zzk; i43++) {
            int i44 = zzdiVar.zzi[i43];
            int i45 = zzdiVar.zzc[i44];
            Object zzf = zzeq.zzf(obj2, zzdiVar.zzy(i44) & 1048575);
            if (zzf != null && zzdiVar.zzA(i44) != null) {
                ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzdiVar.zzC(i44));
                throw null;
            }
        }
        if (i4 == 0) {
            if (i27 != i2) {
                throw zzci.zze();
            }
        } else if (i27 > i2 || i30 != i4) {
            throw zzci.zze();
        }
        return i27;
    }

    @Override // com.google.android.gms.internal.play_billing.zzdp
    public final Object zze() {
        return ((zzcb) this.zzg).zzi();
    }

    @Override // com.google.android.gms.internal.play_billing.zzdp
    public final void zzf(Object obj) {
        int i;
        if (zzS(obj)) {
            if (obj instanceof zzcb) {
                zzcb zzcbVar = (zzcb) obj;
                zzcbVar.zzq(ConnectionsManager.DEFAULT_DATACENTER_ID);
                zzcbVar.zza = 0;
                zzcbVar.zzo();
            }
            int length = this.zzc.length;
            while (i < length) {
                int zzy = zzy(i);
                int i2 = 1048575 & zzy;
                int zzx = zzx(zzy);
                long j = i2;
                if (zzx != 9) {
                    if (zzx == 60 || zzx == 68) {
                        if (!zzT(obj, this.zzc[i], i)) {
                        }
                        zzB(i).zzf(zzb.getObject(obj, j));
                    } else {
                        switch (zzx) {
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
                                this.zzl.zza(obj, j);
                                continue;
                            case 50:
                                Unsafe unsafe = zzb;
                                Object object = unsafe.getObject(obj, j);
                                if (object != null) {
                                    ((zzcz) object).zzc();
                                    unsafe.putObject(obj, j, object);
                                    break;
                                } else {
                                    continue;
                                }
                        }
                        zzB(i).zzf(zzb.getObject(obj, j));
                    }
                }
                i = zzP(obj, i) ? 0 : i + 3;
                zzB(i).zzf(zzb.getObject(obj, j));
            }
            this.zzm.zzg(obj);
            if (this.zzh) {
                this.zzn.zzb(obj);
            }
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:5:0x001d. Please report as an issue. */
    @Override // com.google.android.gms.internal.play_billing.zzdp
    public final void zzg(Object obj, Object obj2) {
        zzG(obj);
        obj2.getClass();
        for (int i = 0; i < this.zzc.length; i += 3) {
            int zzy = zzy(i);
            int i2 = this.zzc[i];
            long j = 1048575 & zzy;
            switch (zzx(zzy)) {
                case 0:
                    if (zzP(obj2, i)) {
                        zzeq.zzo(obj, j, zzeq.zza(obj2, j));
                        zzJ(obj, i);
                        break;
                    } else {
                        break;
                    }
                case 1:
                    if (zzP(obj2, i)) {
                        zzeq.zzp(obj, j, zzeq.zzb(obj2, j));
                        zzJ(obj, i);
                        break;
                    } else {
                        break;
                    }
                case 2:
                    if (!zzP(obj2, i)) {
                        break;
                    }
                    zzeq.zzr(obj, j, zzeq.zzd(obj2, j));
                    zzJ(obj, i);
                    break;
                case 3:
                    if (!zzP(obj2, i)) {
                        break;
                    }
                    zzeq.zzr(obj, j, zzeq.zzd(obj2, j));
                    zzJ(obj, i);
                    break;
                case 4:
                    if (!zzP(obj2, i)) {
                        break;
                    }
                    zzeq.zzq(obj, j, zzeq.zzc(obj2, j));
                    zzJ(obj, i);
                    break;
                case 5:
                    if (!zzP(obj2, i)) {
                        break;
                    }
                    zzeq.zzr(obj, j, zzeq.zzd(obj2, j));
                    zzJ(obj, i);
                    break;
                case 6:
                    if (!zzP(obj2, i)) {
                        break;
                    }
                    zzeq.zzq(obj, j, zzeq.zzc(obj2, j));
                    zzJ(obj, i);
                    break;
                case 7:
                    if (zzP(obj2, i)) {
                        zzeq.zzm(obj, j, zzeq.zzw(obj2, j));
                        zzJ(obj, i);
                        break;
                    } else {
                        break;
                    }
                case 8:
                    if (!zzP(obj2, i)) {
                        break;
                    }
                    zzeq.zzs(obj, j, zzeq.zzf(obj2, j));
                    zzJ(obj, i);
                    break;
                case 9:
                case 17:
                    zzH(obj, obj2, i);
                    break;
                case 10:
                    if (!zzP(obj2, i)) {
                        break;
                    }
                    zzeq.zzs(obj, j, zzeq.zzf(obj2, j));
                    zzJ(obj, i);
                    break;
                case 11:
                    if (!zzP(obj2, i)) {
                        break;
                    }
                    zzeq.zzq(obj, j, zzeq.zzc(obj2, j));
                    zzJ(obj, i);
                    break;
                case 12:
                    if (!zzP(obj2, i)) {
                        break;
                    }
                    zzeq.zzq(obj, j, zzeq.zzc(obj2, j));
                    zzJ(obj, i);
                    break;
                case 13:
                    if (!zzP(obj2, i)) {
                        break;
                    }
                    zzeq.zzq(obj, j, zzeq.zzc(obj2, j));
                    zzJ(obj, i);
                    break;
                case 14:
                    if (!zzP(obj2, i)) {
                        break;
                    }
                    zzeq.zzr(obj, j, zzeq.zzd(obj2, j));
                    zzJ(obj, i);
                    break;
                case 15:
                    if (!zzP(obj2, i)) {
                        break;
                    }
                    zzeq.zzq(obj, j, zzeq.zzc(obj2, j));
                    zzJ(obj, i);
                    break;
                case 16:
                    if (!zzP(obj2, i)) {
                        break;
                    }
                    zzeq.zzr(obj, j, zzeq.zzd(obj2, j));
                    zzJ(obj, i);
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
                    this.zzl.zzb(obj, obj2, j);
                    break;
                case 50:
                    int i3 = zzdr.$r8$clinit;
                    zzeq.zzs(obj, j, zzda.zzb(zzeq.zzf(obj, j), zzeq.zzf(obj2, j)));
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
                    if (!zzT(obj2, i2, i)) {
                        break;
                    }
                    zzeq.zzs(obj, j, zzeq.zzf(obj2, j));
                    zzK(obj, i2, i);
                    break;
                case 60:
                case 68:
                    zzI(obj, obj2, i);
                    break;
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                    if (!zzT(obj2, i2, i)) {
                        break;
                    }
                    zzeq.zzs(obj, j, zzeq.zzf(obj2, j));
                    zzK(obj, i2, i);
                    break;
            }
        }
        zzdr.zzB(this.zzm, obj, obj2);
        if (this.zzh) {
            this.zzn.zza(obj2);
            throw null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:48:0x02f5, code lost:
    
        if (r0 != r15) goto L105;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x02f8, code lost:
    
        r6 = r32;
        r2 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0318, code lost:
    
        if (r0 != r15) goto L105;
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:61:0x00a3. Please report as an issue. */
    @Override // com.google.android.gms.internal.play_billing.zzdp
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void zzh(Object obj, byte[] bArr, int i, int i2, zzan zzanVar) {
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        Unsafe unsafe;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        long j;
        int i13;
        int i14;
        int i15;
        zzdi zzdiVar = this;
        Object obj2 = obj;
        byte[] bArr2 = bArr;
        int i16 = i2;
        zzan zzanVar2 = zzanVar;
        zzew zzewVar = zzew.zza;
        int i17 = -1;
        if (zzdiVar.zzo - 1 == 0) {
            zzc(obj, bArr, i, i2, 0, zzanVar);
            return;
        }
        zzG(obj);
        Unsafe unsafe2 = zzb;
        int i18 = 1048575;
        int i19 = i;
        int i20 = -1;
        int i21 = 0;
        int i22 = 0;
        int i23 = 1048575;
        while (i19 < i16) {
            int i24 = i19 + 1;
            byte b = bArr2[i19];
            if (b < 0) {
                int zzk = zzao.zzk(b, bArr2, i24, zzanVar2);
                i3 = zzanVar2.zza;
                i24 = zzk;
            } else {
                i3 = b;
            }
            int i25 = i3 >>> 3;
            int zzu = i25 > i20 ? zzdiVar.zzu(i25, i21 / 3) : zzdiVar.zzt(i25);
            if (zzu == i17) {
                i4 = i25;
                i5 = i24;
                i6 = i22;
                i7 = i23;
                unsafe = unsafe2;
                i8 = 0;
            } else {
                int i26 = i3 & 7;
                int[] iArr = zzdiVar.zzc;
                int i27 = iArr[zzu + 1];
                int zzx = zzx(i27);
                Unsafe unsafe3 = unsafe2;
                long j2 = i27 & i18;
                if (zzx <= 17) {
                    int i28 = iArr[zzu + 2];
                    int i29 = 1 << (i28 >>> 20);
                    int i30 = i28 & 1048575;
                    if (i30 != i23) {
                        if (i23 != 1048575) {
                            unsafe2 = unsafe3;
                            unsafe2.putInt(obj2, i23, i22);
                        } else {
                            unsafe2 = unsafe3;
                        }
                        if (i30 != 1048575) {
                            i22 = unsafe2.getInt(obj2, i30);
                        }
                        i9 = i30;
                        i6 = i22;
                    } else {
                        unsafe2 = unsafe3;
                        i6 = i22;
                        i9 = i23;
                    }
                    switch (zzx) {
                        case 0:
                            i4 = i25;
                            i10 = zzu;
                            i11 = i24;
                            if (i26 == 1) {
                                zzeq.zzo(obj2, j2, Double.longBitsToDouble(zzao.zzp(bArr2, i11)));
                                i19 = i11 + 8;
                                i22 = i6 | i29;
                                i16 = i2;
                                i21 = i10;
                                i23 = i9;
                                i20 = i4;
                                break;
                            }
                            unsafe = unsafe2;
                            i5 = i11;
                            i7 = i9;
                            i8 = i10;
                            break;
                        case 1:
                            i4 = i25;
                            i10 = zzu;
                            i11 = i24;
                            if (i26 == 5) {
                                zzeq.zzp(obj2, j2, Float.intBitsToFloat(zzao.zzb(bArr2, i11)));
                                i19 = i11 + 4;
                                i22 = i6 | i29;
                                i16 = i2;
                                i21 = i10;
                                i23 = i9;
                                i20 = i4;
                                break;
                            }
                            unsafe = unsafe2;
                            i5 = i11;
                            i7 = i9;
                            i8 = i10;
                            break;
                        case 2:
                        case 3:
                            i4 = i25;
                            i10 = zzu;
                            i11 = i24;
                            if (i26 == 0) {
                                int zzm = zzao.zzm(bArr2, i11, zzanVar2);
                                unsafe2.putLong(obj, j2, zzanVar2.zzb);
                                i22 = i6 | i29;
                                i16 = i2;
                                i21 = i10;
                                i19 = zzm;
                                i23 = i9;
                                i20 = i4;
                                break;
                            }
                            unsafe = unsafe2;
                            i5 = i11;
                            i7 = i9;
                            i8 = i10;
                            break;
                        case 4:
                        case 11:
                            i4 = i25;
                            i10 = zzu;
                            i11 = i24;
                            if (i26 == 0) {
                                i19 = zzao.zzj(bArr2, i11, zzanVar2);
                                unsafe2.putInt(obj2, j2, zzanVar2.zza);
                                i22 = i6 | i29;
                                i16 = i2;
                                i21 = i10;
                                i23 = i9;
                                i20 = i4;
                                break;
                            }
                            unsafe = unsafe2;
                            i5 = i11;
                            i7 = i9;
                            i8 = i10;
                            break;
                        case 5:
                        case 14:
                            i4 = i25;
                            i10 = zzu;
                            if (i26 == 1) {
                                i11 = i24;
                                unsafe2.putLong(obj, j2, zzao.zzp(bArr2, i24));
                                i19 = i11 + 8;
                                i22 = i6 | i29;
                                i16 = i2;
                                i21 = i10;
                                i23 = i9;
                                i20 = i4;
                                break;
                            }
                            i11 = i24;
                            unsafe = unsafe2;
                            i5 = i11;
                            i7 = i9;
                            i8 = i10;
                            break;
                        case 6:
                        case 13:
                            i4 = i25;
                            i10 = zzu;
                            if (i26 == 5) {
                                unsafe2.putInt(obj2, j2, zzao.zzb(bArr2, i24));
                                i19 = i24 + 4;
                                i22 = i6 | i29;
                                i16 = i2;
                                i21 = i10;
                                i23 = i9;
                                i20 = i4;
                                break;
                            }
                            i11 = i24;
                            unsafe = unsafe2;
                            i5 = i11;
                            i7 = i9;
                            i8 = i10;
                            break;
                        case 7:
                            i4 = i25;
                            i10 = zzu;
                            if (i26 == 0) {
                                i19 = zzao.zzm(bArr2, i24, zzanVar2);
                                zzeq.zzm(obj2, j2, zzanVar2.zzb != 0);
                                i22 = i6 | i29;
                                i16 = i2;
                                i21 = i10;
                                i23 = i9;
                                i20 = i4;
                                break;
                            }
                            i11 = i24;
                            unsafe = unsafe2;
                            i5 = i11;
                            i7 = i9;
                            i8 = i10;
                            break;
                        case 8:
                            i4 = i25;
                            i10 = zzu;
                            if (i26 == 2) {
                                i19 = (536870912 & i27) == 0 ? zzao.zzg(bArr2, i24, zzanVar2) : zzao.zzh(bArr2, i24, zzanVar2);
                                unsafe2.putObject(obj2, j2, zzanVar2.zzc);
                                i22 = i6 | i29;
                                i16 = i2;
                                i21 = i10;
                                i23 = i9;
                                i20 = i4;
                                break;
                            }
                            i11 = i24;
                            unsafe = unsafe2;
                            i5 = i11;
                            i7 = i9;
                            i8 = i10;
                            break;
                        case 9:
                            i4 = i25;
                            i12 = zzu;
                            if (i26 == 2) {
                                Object zzD = zzdiVar.zzD(obj2, i12);
                                i19 = zzao.zzo(zzD, zzdiVar.zzB(i12), bArr, i24, i2, zzanVar);
                                zzdiVar.zzL(obj2, i12, zzD);
                                i22 = i6 | i29;
                                i16 = i2;
                                i21 = i12;
                                i23 = i9;
                                i20 = i4;
                                break;
                            }
                            i10 = i12;
                            i11 = i24;
                            unsafe = unsafe2;
                            i5 = i11;
                            i7 = i9;
                            i8 = i10;
                            break;
                        case 10:
                            i4 = i25;
                            i12 = zzu;
                            if (i26 == 2) {
                                int zza2 = zzao.zza(bArr2, i24, zzanVar2);
                                unsafe2.putObject(obj2, j2, zzanVar2.zzc);
                                i22 = i6 | i29;
                                i16 = i2;
                                i19 = zza2;
                                i21 = i12;
                                i23 = i9;
                                i20 = i4;
                                break;
                            }
                            i10 = i12;
                            i11 = i24;
                            unsafe = unsafe2;
                            i5 = i11;
                            i7 = i9;
                            i8 = i10;
                            break;
                        case 12:
                            i4 = i25;
                            i12 = zzu;
                            j = j2;
                            if (i26 == 0) {
                                i19 = zzao.zzj(bArr2, i24, zzanVar2);
                                i13 = zzanVar2.zza;
                                unsafe2.putInt(obj2, j, i13);
                                i22 = i6 | i29;
                                i16 = i2;
                                i21 = i12;
                                i23 = i9;
                                i20 = i4;
                                break;
                            }
                            i10 = i12;
                            i11 = i24;
                            unsafe = unsafe2;
                            i5 = i11;
                            i7 = i9;
                            i8 = i10;
                            break;
                        case 15:
                            i4 = i25;
                            i12 = zzu;
                            if (i26 == 0) {
                                i19 = zzao.zzj(bArr2, i24, zzanVar2);
                                i13 = zzbe.zzb(zzanVar2.zza);
                                j = j2;
                                unsafe2.putInt(obj2, j, i13);
                                i22 = i6 | i29;
                                i16 = i2;
                                i21 = i12;
                                i23 = i9;
                                i20 = i4;
                                break;
                            }
                            i10 = i12;
                            i11 = i24;
                            unsafe = unsafe2;
                            i5 = i11;
                            i7 = i9;
                            i8 = i10;
                            break;
                        case 16:
                            if (i26 == 0) {
                                int zzm2 = zzao.zzm(bArr2, i24, zzanVar2);
                                i4 = i25;
                                i12 = zzu;
                                unsafe2.putLong(obj, j2, zzbe.zzc(zzanVar2.zzb));
                                i22 = i6 | i29;
                                i16 = i2;
                                i19 = zzm2;
                                i21 = i12;
                                i23 = i9;
                                i20 = i4;
                                break;
                            }
                        default:
                            i4 = i25;
                            i10 = zzu;
                            i11 = i24;
                            unsafe = unsafe2;
                            i5 = i11;
                            i7 = i9;
                            i8 = i10;
                            break;
                    }
                    i18 = 1048575;
                    i17 = -1;
                } else {
                    i4 = i25;
                    int i31 = i24;
                    unsafe2 = unsafe3;
                    if (zzx != 27) {
                        i8 = zzu;
                        int i32 = i23;
                        int i33 = i22;
                        if (zzx <= 49) {
                            i14 = i33;
                            unsafe = unsafe2;
                            i7 = i32;
                            i19 = zzs(obj, bArr, i31, i2, i3, i4, i26, i8, i27, zzx, j2, zzanVar);
                            if (i19 == i31) {
                                i6 = i14;
                                i5 = i19;
                            }
                            zzdiVar = this;
                            obj2 = obj;
                            bArr2 = bArr;
                            i22 = i14;
                            i16 = i2;
                            zzanVar2 = zzanVar;
                        } else {
                            i14 = i33;
                            i7 = i32;
                            unsafe = unsafe2;
                            i15 = i31;
                            if (zzx != 50) {
                                i19 = zzr(obj, bArr, i15, i2, i3, i4, i26, i27, zzx, j2, i8, zzanVar);
                            } else if (i26 == 2) {
                                i19 = zzq(obj, bArr, i15, i2, i8, j2, zzanVar);
                            }
                            i18 = 1048575;
                            i17 = -1;
                        }
                        i21 = i8;
                        i20 = i4;
                        unsafe2 = unsafe;
                        i23 = i7;
                        i18 = 1048575;
                        i17 = -1;
                    } else if (i26 == 2) {
                        zzcf zzcfVar = (zzcf) unsafe2.getObject(obj2, j2);
                        if (!zzcfVar.zzc()) {
                            int size = zzcfVar.size();
                            zzcfVar = zzcfVar.zzd(size == 0 ? 10 : size + size);
                            unsafe2.putObject(obj2, j2, zzcfVar);
                        }
                        i19 = zzao.zze(zzdiVar.zzB(zzu), i3, bArr, i31, i2, zzcfVar, zzanVar);
                        bArr2 = bArr;
                        i16 = i2;
                        i22 = i22;
                        i23 = i23;
                        i21 = zzu;
                        i20 = i4;
                        i18 = 1048575;
                        i17 = -1;
                        zzanVar2 = zzanVar;
                    } else {
                        i8 = zzu;
                        i14 = i22;
                        i7 = i23;
                        unsafe = unsafe2;
                        i15 = i31;
                    }
                    i6 = i14;
                    i5 = i15;
                }
            }
            i19 = zzao.zzi(i3, bArr, i5, i2, zzd(obj), zzanVar);
            zzdiVar = this;
            obj2 = obj;
            bArr2 = bArr;
            i16 = i2;
            zzanVar2 = zzanVar;
            i22 = i6;
            i21 = i8;
            i20 = i4;
            unsafe2 = unsafe;
            i23 = i7;
            i18 = 1048575;
            i17 = -1;
        }
        int i34 = i22;
        int i35 = i23;
        Unsafe unsafe4 = unsafe2;
        if (i35 != 1048575) {
            unsafe4.putInt(obj, i35, i34);
        }
        if (i19 != i2) {
            throw zzci.zze();
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:8:0x002a. Please report as an issue. */
    @Override // com.google.android.gms.internal.play_billing.zzdp
    public final void zzi(Object obj, zzey zzeyVar) {
        int i;
        double zza2;
        float zzb2;
        long zzd;
        long zzd2;
        int zzc;
        long zzd3;
        int zzc2;
        boolean zzw;
        int zzc3;
        int zzc4;
        int zzc5;
        long zzd4;
        int zzc6;
        long zzd5;
        zzew zzewVar = zzew.zza;
        int i2 = 1048575;
        if (this.zzo - 1 != 0) {
            if (this.zzh) {
                this.zzn.zza(obj);
                throw null;
            }
            int length = this.zzc.length;
            for (int i3 = 0; i3 < length; i3 += 3) {
                int zzy = zzy(i3);
                int i4 = this.zzc[i3];
                switch (zzx(zzy)) {
                    case 0:
                        if (zzP(obj, i3)) {
                            zza2 = zzeq.zza(obj, zzy & 1048575);
                            zzeyVar.zzf(i4, zza2);
                            break;
                        } else {
                            break;
                        }
                    case 1:
                        if (zzP(obj, i3)) {
                            zzb2 = zzeq.zzb(obj, zzy & 1048575);
                            zzeyVar.zzo(i4, zzb2);
                            break;
                        } else {
                            break;
                        }
                    case 2:
                        if (zzP(obj, i3)) {
                            zzd = zzeq.zzd(obj, zzy & 1048575);
                            zzeyVar.zzt(i4, zzd);
                            break;
                        } else {
                            break;
                        }
                    case 3:
                        if (zzP(obj, i3)) {
                            zzd2 = zzeq.zzd(obj, zzy & 1048575);
                            zzeyVar.zzJ(i4, zzd2);
                            break;
                        } else {
                            break;
                        }
                    case 4:
                        if (zzP(obj, i3)) {
                            zzc = zzeq.zzc(obj, zzy & 1048575);
                            zzeyVar.zzr(i4, zzc);
                            break;
                        } else {
                            break;
                        }
                    case 5:
                        if (zzP(obj, i3)) {
                            zzd3 = zzeq.zzd(obj, zzy & 1048575);
                            zzeyVar.zzm(i4, zzd3);
                            break;
                        } else {
                            break;
                        }
                    case 6:
                        if (zzP(obj, i3)) {
                            zzc2 = zzeq.zzc(obj, zzy & 1048575);
                            zzeyVar.zzk(i4, zzc2);
                            break;
                        } else {
                            break;
                        }
                    case 7:
                        if (zzP(obj, i3)) {
                            zzw = zzeq.zzw(obj, zzy & 1048575);
                            zzeyVar.zzb(i4, zzw);
                            break;
                        } else {
                            break;
                        }
                    case 8:
                        if (!zzP(obj, i3)) {
                            break;
                        }
                        zzV(i4, zzeq.zzf(obj, zzy & 1048575), zzeyVar);
                        break;
                    case 9:
                        if (!zzP(obj, i3)) {
                            break;
                        }
                        zzeyVar.zzv(i4, zzeq.zzf(obj, zzy & 1048575), zzB(i3));
                        break;
                    case 10:
                        if (!zzP(obj, i3)) {
                            break;
                        }
                        zzeyVar.zzd(i4, (zzba) zzeq.zzf(obj, zzy & 1048575));
                        break;
                    case 11:
                        if (zzP(obj, i3)) {
                            zzc3 = zzeq.zzc(obj, zzy & 1048575);
                            zzeyVar.zzH(i4, zzc3);
                            break;
                        } else {
                            break;
                        }
                    case 12:
                        if (zzP(obj, i3)) {
                            zzc4 = zzeq.zzc(obj, zzy & 1048575);
                            zzeyVar.zzi(i4, zzc4);
                            break;
                        } else {
                            break;
                        }
                    case 13:
                        if (zzP(obj, i3)) {
                            zzc5 = zzeq.zzc(obj, zzy & 1048575);
                            zzeyVar.zzw(i4, zzc5);
                            break;
                        } else {
                            break;
                        }
                    case 14:
                        if (zzP(obj, i3)) {
                            zzd4 = zzeq.zzd(obj, zzy & 1048575);
                            zzeyVar.zzy(i4, zzd4);
                            break;
                        } else {
                            break;
                        }
                    case 15:
                        if (zzP(obj, i3)) {
                            zzc6 = zzeq.zzc(obj, zzy & 1048575);
                            zzeyVar.zzA(i4, zzc6);
                            break;
                        } else {
                            break;
                        }
                    case 16:
                        if (zzP(obj, i3)) {
                            zzd5 = zzeq.zzd(obj, zzy & 1048575);
                            zzeyVar.zzC(i4, zzd5);
                            break;
                        } else {
                            break;
                        }
                    case 17:
                        if (!zzP(obj, i3)) {
                            break;
                        }
                        zzeyVar.zzq(i4, zzeq.zzf(obj, zzy & 1048575), zzB(i3));
                        break;
                    case 18:
                        zzdr.zzF(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, false);
                        break;
                    case 19:
                        zzdr.zzJ(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, false);
                        break;
                    case 20:
                        zzdr.zzM(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, false);
                        break;
                    case 21:
                        zzdr.zzU(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, false);
                        break;
                    case 22:
                        zzdr.zzL(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, false);
                        break;
                    case 23:
                        zzdr.zzI(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, false);
                        break;
                    case 24:
                        zzdr.zzH(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, false);
                        break;
                    case 25:
                        zzdr.zzD(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, false);
                        break;
                    case 26:
                        zzdr.zzS(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar);
                        break;
                    case 27:
                        zzdr.zzN(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, zzB(i3));
                        break;
                    case 28:
                        zzdr.zzE(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar);
                        break;
                    case 29:
                        zzdr.zzT(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, false);
                        break;
                    case 30:
                        zzdr.zzG(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, false);
                        break;
                    case 31:
                        zzdr.zzO(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, false);
                        break;
                    case 32:
                        zzdr.zzP(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, false);
                        break;
                    case 33:
                        zzdr.zzQ(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, false);
                        break;
                    case 34:
                        zzdr.zzR(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, false);
                        break;
                    case 35:
                        zzdr.zzF(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, true);
                        break;
                    case 36:
                        zzdr.zzJ(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, true);
                        break;
                    case 37:
                        zzdr.zzM(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, true);
                        break;
                    case 38:
                        zzdr.zzU(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, true);
                        break;
                    case 39:
                        zzdr.zzL(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, true);
                        break;
                    case 40:
                        zzdr.zzI(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, true);
                        break;
                    case 41:
                        zzdr.zzH(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, true);
                        break;
                    case 42:
                        zzdr.zzD(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, true);
                        break;
                    case 43:
                        zzdr.zzT(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, true);
                        break;
                    case 44:
                        zzdr.zzG(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, true);
                        break;
                    case 45:
                        zzdr.zzO(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, true);
                        break;
                    case 46:
                        zzdr.zzP(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, true);
                        break;
                    case 47:
                        zzdr.zzQ(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, true);
                        break;
                    case 48:
                        zzdr.zzR(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, true);
                        break;
                    case 49:
                        zzdr.zzK(i4, (List) zzeq.zzf(obj, zzy & 1048575), zzeyVar, zzB(i3));
                        break;
                    case 50:
                        zzN(zzeyVar, i4, zzeq.zzf(obj, zzy & 1048575), i3);
                        break;
                    case 51:
                        if (zzT(obj, i4, i3)) {
                            zza2 = zzm(obj, zzy & 1048575);
                            zzeyVar.zzf(i4, zza2);
                            break;
                        } else {
                            break;
                        }
                    case 52:
                        if (zzT(obj, i4, i3)) {
                            zzb2 = zzn(obj, zzy & 1048575);
                            zzeyVar.zzo(i4, zzb2);
                            break;
                        } else {
                            break;
                        }
                    case 53:
                        if (zzT(obj, i4, i3)) {
                            zzd = zzz(obj, zzy & 1048575);
                            zzeyVar.zzt(i4, zzd);
                            break;
                        } else {
                            break;
                        }
                    case 54:
                        if (zzT(obj, i4, i3)) {
                            zzd2 = zzz(obj, zzy & 1048575);
                            zzeyVar.zzJ(i4, zzd2);
                            break;
                        } else {
                            break;
                        }
                    case 55:
                        if (zzT(obj, i4, i3)) {
                            zzc = zzp(obj, zzy & 1048575);
                            zzeyVar.zzr(i4, zzc);
                            break;
                        } else {
                            break;
                        }
                    case 56:
                        if (zzT(obj, i4, i3)) {
                            zzd3 = zzz(obj, zzy & 1048575);
                            zzeyVar.zzm(i4, zzd3);
                            break;
                        } else {
                            break;
                        }
                    case 57:
                        if (zzT(obj, i4, i3)) {
                            zzc2 = zzp(obj, zzy & 1048575);
                            zzeyVar.zzk(i4, zzc2);
                            break;
                        } else {
                            break;
                        }
                    case 58:
                        if (zzT(obj, i4, i3)) {
                            zzw = zzU(obj, zzy & 1048575);
                            zzeyVar.zzb(i4, zzw);
                            break;
                        } else {
                            break;
                        }
                    case 59:
                        if (!zzT(obj, i4, i3)) {
                            break;
                        }
                        zzV(i4, zzeq.zzf(obj, zzy & 1048575), zzeyVar);
                        break;
                    case 60:
                        if (!zzT(obj, i4, i3)) {
                            break;
                        }
                        zzeyVar.zzv(i4, zzeq.zzf(obj, zzy & 1048575), zzB(i3));
                        break;
                    case 61:
                        if (!zzT(obj, i4, i3)) {
                            break;
                        }
                        zzeyVar.zzd(i4, (zzba) zzeq.zzf(obj, zzy & 1048575));
                        break;
                    case 62:
                        if (zzT(obj, i4, i3)) {
                            zzc3 = zzp(obj, zzy & 1048575);
                            zzeyVar.zzH(i4, zzc3);
                            break;
                        } else {
                            break;
                        }
                    case 63:
                        if (zzT(obj, i4, i3)) {
                            zzc4 = zzp(obj, zzy & 1048575);
                            zzeyVar.zzi(i4, zzc4);
                            break;
                        } else {
                            break;
                        }
                    case 64:
                        if (zzT(obj, i4, i3)) {
                            zzc5 = zzp(obj, zzy & 1048575);
                            zzeyVar.zzw(i4, zzc5);
                            break;
                        } else {
                            break;
                        }
                    case 65:
                        if (zzT(obj, i4, i3)) {
                            zzd4 = zzz(obj, zzy & 1048575);
                            zzeyVar.zzy(i4, zzd4);
                            break;
                        } else {
                            break;
                        }
                    case 66:
                        if (zzT(obj, i4, i3)) {
                            zzc6 = zzp(obj, zzy & 1048575);
                            zzeyVar.zzA(i4, zzc6);
                            break;
                        } else {
                            break;
                        }
                    case 67:
                        if (zzT(obj, i4, i3)) {
                            zzd5 = zzz(obj, zzy & 1048575);
                            zzeyVar.zzC(i4, zzd5);
                            break;
                        } else {
                            break;
                        }
                    case 68:
                        if (!zzT(obj, i4, i3)) {
                            break;
                        }
                        zzeyVar.zzq(i4, zzeq.zzf(obj, zzy & 1048575), zzB(i3));
                        break;
                }
            }
        } else {
            if (this.zzh) {
                this.zzn.zza(obj);
                throw null;
            }
            int length2 = this.zzc.length;
            Unsafe unsafe = zzb;
            int i5 = 0;
            int i6 = 1048575;
            int i7 = 0;
            while (i5 < length2) {
                int zzy2 = zzy(i5);
                int[] iArr = this.zzc;
                int i8 = iArr[i5];
                int zzx = zzx(zzy2);
                if (zzx <= 17) {
                    int i9 = iArr[i5 + 2];
                    int i10 = i9 & i2;
                    if (i10 != i6) {
                        i7 = unsafe.getInt(obj, i10);
                        i6 = i10;
                    }
                    i = 1 << (i9 >>> 20);
                } else {
                    i = 0;
                }
                long j = zzy2 & i2;
                switch (zzx) {
                    case 0:
                        if ((i7 & i) == 0) {
                            break;
                        } else {
                            zzeyVar.zzf(i8, zzeq.zza(obj, j));
                            continue;
                        }
                    case 1:
                        if ((i7 & i) != 0) {
                            zzeyVar.zzo(i8, zzeq.zzb(obj, j));
                            break;
                        } else {
                            continue;
                        }
                    case 2:
                        if ((i7 & i) != 0) {
                            zzeyVar.zzt(i8, unsafe.getLong(obj, j));
                            break;
                        } else {
                            continue;
                        }
                    case 3:
                        if ((i7 & i) != 0) {
                            zzeyVar.zzJ(i8, unsafe.getLong(obj, j));
                            break;
                        } else {
                            continue;
                        }
                    case 4:
                        if ((i7 & i) != 0) {
                            zzeyVar.zzr(i8, unsafe.getInt(obj, j));
                            break;
                        } else {
                            continue;
                        }
                    case 5:
                        if ((i7 & i) != 0) {
                            zzeyVar.zzm(i8, unsafe.getLong(obj, j));
                            break;
                        } else {
                            continue;
                        }
                    case 6:
                        if ((i7 & i) != 0) {
                            zzeyVar.zzk(i8, unsafe.getInt(obj, j));
                            break;
                        } else {
                            continue;
                        }
                    case 7:
                        if ((i7 & i) != 0) {
                            zzeyVar.zzb(i8, zzeq.zzw(obj, j));
                            break;
                        } else {
                            continue;
                        }
                    case 8:
                        if ((i7 & i) != 0) {
                            zzV(i8, unsafe.getObject(obj, j), zzeyVar);
                            break;
                        } else {
                            continue;
                        }
                    case 9:
                        if ((i7 & i) != 0) {
                            zzeyVar.zzv(i8, unsafe.getObject(obj, j), zzB(i5));
                            break;
                        } else {
                            continue;
                        }
                    case 10:
                        if ((i7 & i) != 0) {
                            zzeyVar.zzd(i8, (zzba) unsafe.getObject(obj, j));
                            break;
                        } else {
                            continue;
                        }
                    case 11:
                        if ((i7 & i) != 0) {
                            zzeyVar.zzH(i8, unsafe.getInt(obj, j));
                            break;
                        } else {
                            continue;
                        }
                    case 12:
                        if ((i7 & i) != 0) {
                            zzeyVar.zzi(i8, unsafe.getInt(obj, j));
                            break;
                        } else {
                            continue;
                        }
                    case 13:
                        if ((i7 & i) != 0) {
                            zzeyVar.zzw(i8, unsafe.getInt(obj, j));
                            break;
                        } else {
                            continue;
                        }
                    case 14:
                        if ((i7 & i) != 0) {
                            zzeyVar.zzy(i8, unsafe.getLong(obj, j));
                            break;
                        } else {
                            continue;
                        }
                    case 15:
                        if ((i7 & i) != 0) {
                            zzeyVar.zzA(i8, unsafe.getInt(obj, j));
                            break;
                        } else {
                            continue;
                        }
                    case 16:
                        if ((i7 & i) != 0) {
                            zzeyVar.zzC(i8, unsafe.getLong(obj, j));
                            break;
                        } else {
                            continue;
                        }
                    case 17:
                        if ((i7 & i) != 0) {
                            zzeyVar.zzq(i8, unsafe.getObject(obj, j), zzB(i5));
                            break;
                        } else {
                            continue;
                        }
                    case 18:
                        zzdr.zzF(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, false);
                        continue;
                    case 19:
                        zzdr.zzJ(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, false);
                        continue;
                    case 20:
                        zzdr.zzM(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, false);
                        continue;
                    case 21:
                        zzdr.zzU(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, false);
                        continue;
                    case 22:
                        zzdr.zzL(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, false);
                        continue;
                    case 23:
                        zzdr.zzI(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, false);
                        continue;
                    case 24:
                        zzdr.zzH(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, false);
                        continue;
                    case 25:
                        zzdr.zzD(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, false);
                        continue;
                    case 26:
                        zzdr.zzS(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar);
                        break;
                    case 27:
                        zzdr.zzN(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, zzB(i5));
                        break;
                    case 28:
                        zzdr.zzE(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar);
                        break;
                    case 29:
                        zzdr.zzT(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, false);
                        break;
                    case 30:
                        zzdr.zzG(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, false);
                        break;
                    case 31:
                        zzdr.zzO(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, false);
                        break;
                    case 32:
                        zzdr.zzP(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, false);
                        break;
                    case 33:
                        zzdr.zzQ(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, false);
                        break;
                    case 34:
                        zzdr.zzR(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, false);
                        break;
                    case 35:
                        zzdr.zzF(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, true);
                        break;
                    case 36:
                        zzdr.zzJ(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, true);
                        break;
                    case 37:
                        zzdr.zzM(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, true);
                        break;
                    case 38:
                        zzdr.zzU(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, true);
                        break;
                    case 39:
                        zzdr.zzL(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, true);
                        break;
                    case 40:
                        zzdr.zzI(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, true);
                        break;
                    case 41:
                        zzdr.zzH(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, true);
                        break;
                    case 42:
                        zzdr.zzD(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, true);
                        break;
                    case 43:
                        zzdr.zzT(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, true);
                        break;
                    case 44:
                        zzdr.zzG(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, true);
                        break;
                    case 45:
                        zzdr.zzO(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, true);
                        break;
                    case 46:
                        zzdr.zzP(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, true);
                        break;
                    case 47:
                        zzdr.zzQ(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, true);
                        break;
                    case 48:
                        zzdr.zzR(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, true);
                        break;
                    case 49:
                        zzdr.zzK(this.zzc[i5], (List) unsafe.getObject(obj, j), zzeyVar, zzB(i5));
                        break;
                    case 50:
                        zzN(zzeyVar, i8, unsafe.getObject(obj, j), i5);
                        break;
                    case 51:
                        if (zzT(obj, i8, i5)) {
                            zzeyVar.zzf(i8, zzm(obj, j));
                            break;
                        }
                        break;
                    case 52:
                        if (zzT(obj, i8, i5)) {
                            zzeyVar.zzo(i8, zzn(obj, j));
                            break;
                        }
                        break;
                    case 53:
                        if (zzT(obj, i8, i5)) {
                            zzeyVar.zzt(i8, zzz(obj, j));
                            break;
                        }
                        break;
                    case 54:
                        if (zzT(obj, i8, i5)) {
                            zzeyVar.zzJ(i8, zzz(obj, j));
                            break;
                        }
                        break;
                    case 55:
                        if (zzT(obj, i8, i5)) {
                            zzeyVar.zzr(i8, zzp(obj, j));
                            break;
                        }
                        break;
                    case 56:
                        if (zzT(obj, i8, i5)) {
                            zzeyVar.zzm(i8, zzz(obj, j));
                            break;
                        }
                        break;
                    case 57:
                        if (zzT(obj, i8, i5)) {
                            zzeyVar.zzk(i8, zzp(obj, j));
                            break;
                        }
                        break;
                    case 58:
                        if (zzT(obj, i8, i5)) {
                            zzeyVar.zzb(i8, zzU(obj, j));
                            break;
                        }
                        break;
                    case 59:
                        if (zzT(obj, i8, i5)) {
                            zzV(i8, unsafe.getObject(obj, j), zzeyVar);
                            break;
                        }
                        break;
                    case 60:
                        if (zzT(obj, i8, i5)) {
                            zzeyVar.zzv(i8, unsafe.getObject(obj, j), zzB(i5));
                            break;
                        }
                        break;
                    case 61:
                        if (zzT(obj, i8, i5)) {
                            zzeyVar.zzd(i8, (zzba) unsafe.getObject(obj, j));
                            break;
                        }
                        break;
                    case 62:
                        if (zzT(obj, i8, i5)) {
                            zzeyVar.zzH(i8, zzp(obj, j));
                            break;
                        }
                        break;
                    case 63:
                        if (zzT(obj, i8, i5)) {
                            zzeyVar.zzi(i8, zzp(obj, j));
                            break;
                        }
                        break;
                    case 64:
                        if (zzT(obj, i8, i5)) {
                            zzeyVar.zzw(i8, zzp(obj, j));
                            break;
                        }
                        break;
                    case 65:
                        if (zzT(obj, i8, i5)) {
                            zzeyVar.zzy(i8, zzz(obj, j));
                            break;
                        }
                        break;
                    case 66:
                        if (zzT(obj, i8, i5)) {
                            zzeyVar.zzA(i8, zzp(obj, j));
                            break;
                        }
                        break;
                    case 67:
                        if (zzT(obj, i8, i5)) {
                            zzeyVar.zzC(i8, zzz(obj, j));
                            break;
                        }
                        break;
                    case 68:
                        if (zzT(obj, i8, i5)) {
                            zzeyVar.zzq(i8, unsafe.getObject(obj, j), zzB(i5));
                            break;
                        }
                        break;
                }
                i5 += 3;
                i2 = 1048575;
            }
        }
        zzeg zzegVar = this.zzm;
        zzegVar.zzi(zzegVar.zzd(obj), zzeyVar);
    }

    @Override // com.google.android.gms.internal.play_billing.zzdp
    public final boolean zzj(Object obj, Object obj2) {
        int length = this.zzc.length;
        for (int i = 0; i < length; i += 3) {
            int zzy = zzy(i);
            long j = zzy & 1048575;
            switch (zzx(zzy)) {
                case 0:
                    if (zzO(obj, obj2, i) && Double.doubleToLongBits(zzeq.zza(obj, j)) == Double.doubleToLongBits(zzeq.zza(obj2, j))) {
                        break;
                    }
                    return false;
                case 1:
                    if (zzO(obj, obj2, i) && Float.floatToIntBits(zzeq.zzb(obj, j)) == Float.floatToIntBits(zzeq.zzb(obj2, j))) {
                        break;
                    }
                    return false;
                case 2:
                    if (zzO(obj, obj2, i) && zzeq.zzd(obj, j) == zzeq.zzd(obj2, j)) {
                        break;
                    }
                    return false;
                case 3:
                    if (zzO(obj, obj2, i) && zzeq.zzd(obj, j) == zzeq.zzd(obj2, j)) {
                        break;
                    }
                    return false;
                case 4:
                    if (zzO(obj, obj2, i) && zzeq.zzc(obj, j) == zzeq.zzc(obj2, j)) {
                        break;
                    }
                    return false;
                case 5:
                    if (zzO(obj, obj2, i) && zzeq.zzd(obj, j) == zzeq.zzd(obj2, j)) {
                        break;
                    }
                    return false;
                case 6:
                    if (zzO(obj, obj2, i) && zzeq.zzc(obj, j) == zzeq.zzc(obj2, j)) {
                        break;
                    }
                    return false;
                case 7:
                    if (zzO(obj, obj2, i) && zzeq.zzw(obj, j) == zzeq.zzw(obj2, j)) {
                        break;
                    }
                    return false;
                case 8:
                    if (zzO(obj, obj2, i) && zzdr.zzV(zzeq.zzf(obj, j), zzeq.zzf(obj2, j))) {
                        break;
                    }
                    return false;
                case 9:
                    if (zzO(obj, obj2, i) && zzdr.zzV(zzeq.zzf(obj, j), zzeq.zzf(obj2, j))) {
                        break;
                    }
                    return false;
                case 10:
                    if (zzO(obj, obj2, i) && zzdr.zzV(zzeq.zzf(obj, j), zzeq.zzf(obj2, j))) {
                        break;
                    }
                    return false;
                case 11:
                    if (zzO(obj, obj2, i) && zzeq.zzc(obj, j) == zzeq.zzc(obj2, j)) {
                        break;
                    }
                    return false;
                case 12:
                    if (zzO(obj, obj2, i) && zzeq.zzc(obj, j) == zzeq.zzc(obj2, j)) {
                        break;
                    }
                    return false;
                case 13:
                    if (zzO(obj, obj2, i) && zzeq.zzc(obj, j) == zzeq.zzc(obj2, j)) {
                        break;
                    }
                    return false;
                case 14:
                    if (zzO(obj, obj2, i) && zzeq.zzd(obj, j) == zzeq.zzd(obj2, j)) {
                        break;
                    }
                    return false;
                case 15:
                    if (zzO(obj, obj2, i) && zzeq.zzc(obj, j) == zzeq.zzc(obj2, j)) {
                        break;
                    }
                    return false;
                case 16:
                    if (zzO(obj, obj2, i) && zzeq.zzd(obj, j) == zzeq.zzd(obj2, j)) {
                        break;
                    }
                    return false;
                case 17:
                    if (zzO(obj, obj2, i) && zzdr.zzV(zzeq.zzf(obj, j), zzeq.zzf(obj2, j))) {
                        break;
                    }
                    return false;
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
                    if (!zzdr.zzV(zzeq.zzf(obj, j), zzeq.zzf(obj2, j))) {
                        return false;
                    }
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
                    long zzv = zzv(i) & 1048575;
                    if (zzeq.zzc(obj, zzv) == zzeq.zzc(obj2, zzv) && zzdr.zzV(zzeq.zzf(obj, j), zzeq.zzf(obj2, j))) {
                        break;
                    }
                    return false;
            }
        }
        if (!this.zzm.zzd(obj).equals(this.zzm.zzd(obj2))) {
            return false;
        }
        if (!this.zzh) {
            return true;
        }
        this.zzn.zza(obj);
        this.zzn.zza(obj2);
        throw null;
    }

    @Override // com.google.android.gms.internal.play_billing.zzdp
    public final boolean zzk(Object obj) {
        int i;
        int i2;
        int i3 = 1048575;
        int i4 = 0;
        int i5 = 0;
        while (i5 < this.zzj) {
            int i6 = this.zzi[i5];
            int i7 = this.zzc[i6];
            int zzy = zzy(i6);
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
            if ((268435456 & zzy) != 0 && !zzQ(obj, i6, i, i2, i10)) {
                return false;
            }
            int zzx = zzx(zzy);
            if (zzx != 9 && zzx != 17) {
                if (zzx != 27) {
                    if (zzx == 60 || zzx == 68) {
                        if (zzT(obj, i7, i6) && !zzR(obj, zzy, zzB(i6))) {
                            return false;
                        }
                    } else if (zzx != 49) {
                        if (zzx == 50 && !((zzcz) zzeq.zzf(obj, zzy & 1048575)).isEmpty()) {
                            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzC(i6));
                            throw null;
                        }
                    }
                }
                List list = (List) zzeq.zzf(obj, zzy & 1048575);
                if (list.isEmpty()) {
                    continue;
                } else {
                    zzdp zzB = zzB(i6);
                    for (int i11 = 0; i11 < list.size(); i11++) {
                        if (!zzB.zzk(list.get(i11))) {
                            return false;
                        }
                    }
                }
            } else if (zzQ(obj, i6, i, i2, i10) && !zzR(obj, zzy, zzB(i6))) {
                return false;
            }
            i5++;
            i3 = i;
            i4 = i2;
        }
        if (!this.zzh) {
            return true;
        }
        this.zzn.zza(obj);
        throw null;
    }
}
