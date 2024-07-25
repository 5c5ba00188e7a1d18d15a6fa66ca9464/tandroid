package com.google.android.gms.internal.play_billing;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
import org.telegram.messenger.CharacterCompat;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.R;
import org.telegram.tgnet.ConnectionsManager;
import sun.misc.Unsafe;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
public final class zzdi<T> implements zzdp<T> {
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
        this.zzl = zzctVar;
        this.zzm = zzegVar;
        this.zzn = zzboVar;
        this.zzg = zzdfVar;
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
            Unsafe unsafe = zzb;
            long zzy = zzy(i) & 1048575;
            Object object = unsafe.getObject(obj2, zzy);
            if (object == null) {
                throw new IllegalStateException("Source subfield " + this.zzc[i] + " is present but null: " + obj2.toString());
            }
            zzdp zzB = zzB(i);
            if (!zzP(obj, i)) {
                if (!zzS(object)) {
                    unsafe.putObject(obj, zzy, object);
                } else {
                    Object zze = zzB.zze();
                    zzB.zzg(zze, object);
                    unsafe.putObject(obj, zzy, zze);
                }
                zzJ(obj, i);
                return;
            }
            Object object2 = unsafe.getObject(obj, zzy);
            if (!zzS(object2)) {
                Object zze2 = zzB.zze();
                zzB.zzg(zze2, object2);
                unsafe.putObject(obj, zzy, zze2);
                object2 = zze2;
            }
            zzB.zzg(object2, object);
        }
    }

    private final void zzI(Object obj, Object obj2, int i) {
        int i2 = this.zzc[i];
        if (zzT(obj2, i2, i)) {
            Unsafe unsafe = zzb;
            long zzy = zzy(i) & 1048575;
            Object object = unsafe.getObject(obj2, zzy);
            if (object == null) {
                throw new IllegalStateException("Source subfield " + this.zzc[i] + " is present but null: " + obj2.toString());
            }
            zzdp zzB = zzB(i);
            if (!zzT(obj, i2, i)) {
                if (!zzS(object)) {
                    unsafe.putObject(obj, zzy, object);
                } else {
                    Object zze = zzB.zze();
                    zzB.zzg(zze, object);
                    unsafe.putObject(obj, zzy, zze);
                }
                zzK(obj, i2, i);
                return;
            }
            Object object2 = unsafe.getObject(obj, zzy);
            if (!zzS(object2)) {
                Object zze2 = zzB.zze();
                zzB.zzg(zze2, object2);
                unsafe.putObject(obj, zzy, zze2);
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

    private final void zzN(zzey zzeyVar, int i, Object obj, int i2) throws IOException {
        if (obj == null) {
            return;
        }
        zzcy zzcyVar = (zzcy) zzC(i2);
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
                } else if (zzf instanceof zzba) {
                    return !zzba.zzb.equals(zzf);
                } else {
                    throw new IllegalArgumentException();
                }
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
        if (i2 == 1048575) {
            return zzP(obj, i);
        }
        return (i3 & i4) != 0;
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

    private static final void zzV(int i, Object obj, zzey zzeyVar) throws IOException {
        if (obj instanceof String) {
            zzeyVar.zzF(i, (String) obj);
        } else {
            zzeyVar.zzd(i, (zzba) obj);
        }
    }

    static zzeh zzd(Object obj) {
        zzcb zzcbVar = (zzcb) obj;
        zzeh zzehVar = zzcbVar.zzc;
        if (zzehVar == zzeh.zzc()) {
            zzeh zzf = zzeh.zzf();
            zzcbVar.zzc = zzf;
            return zzf;
        }
        return zzehVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:123:0x024e  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0251  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x0269  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x026c  */
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
        if (zzdcVar instanceof zzdo) {
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
                int i73 = charAt25 & 255;
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
                        if (!(obj instanceof Field)) {
                            zzF2 = (Field) obj;
                        } else {
                            zzF2 = zzF(cls2, (String) obj);
                            zze[i81] = zzF2;
                        }
                        int i82 = i3;
                        i18 = i6;
                        i24 = (int) unsafe.objectFieldOffset(zzF2);
                        int i83 = i81 + 1;
                        obj2 = zze[i83];
                        if (!(obj2 instanceof Field)) {
                            zzF3 = (Field) obj2;
                        } else {
                            zzF3 = zzF(cls2, (String) obj2);
                            zze[i83] = zzF3;
                        }
                        i19 = i82;
                        i20 = (int) unsafe.objectFieldOffset(zzF3);
                        i22 = i78;
                        i23 = 0;
                        str = zzd;
                    }
                    i2 = i26;
                    int i812 = charAt26 + charAt26;
                    obj = zze[i812];
                    if (!(obj instanceof Field)) {
                    }
                    int i822 = i3;
                    i18 = i6;
                    i24 = (int) unsafe.objectFieldOffset(zzF2);
                    int i832 = i812 + 1;
                    obj2 = zze[i832];
                    if (!(obj2 instanceof Field)) {
                    }
                    i19 = i822;
                    i20 = (int) unsafe.objectFieldOffset(zzF3);
                    i22 = i78;
                    i23 = 0;
                    str = zzd;
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
                            i25 = i85 + 1;
                            objArr[i87 + i87 + 1] = zze[i85];
                        } else if (i73 == 12 || i73 == 30 || i73 == 44) {
                            i19 = i84;
                            if (zzdoVar.zzc() == 1 || (charAt25 & 2048) != 0) {
                                int i88 = i64 / 3;
                                i25 = i85 + 1;
                                objArr[i88 + i88 + 1] = zze[i85];
                            }
                        } else {
                            if (i73 == 50) {
                                int i89 = i61 + 1;
                                iArr[i61] = i64;
                                int i90 = i64 / 3;
                                int i91 = i85 + 1;
                                int i92 = i90 + i90;
                                objArr[i92] = zze[i85];
                                if ((charAt25 & 2048) != 0) {
                                    i85 = i91 + 1;
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
                    int objectFieldOffset = (int) unsafe.objectFieldOffset(zzF4);
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
                        iArr[i62] = objectFieldOffset;
                        i62++;
                    }
                    i2 = i21;
                    i24 = objectFieldOffset;
                }
                int i97 = i64 + 1;
                iArr2[i64] = charAt24;
                int i98 = i97 + 1;
                iArr2[i97] = i24 | ((charAt25 & LiteMode.FLAG_CHAT_BLUR) != 0 ? 268435456 : 0) | ((charAt25 & LiteMode.FLAG_CALLS_ANIMATIONS) != 0 ? 536870912 : 0) | (i73 << 20);
                i64 = i98 + 1;
                iArr2[i98] = (i23 << 20) | i20;
                i32 = i22;
                length = i17;
                i3 = i19;
                zzd = str;
                i6 = i18;
                c = CharacterCompat.MIN_HIGH_SURROGATE;
            }
            return new zzdi(iArr2, objArr, i3, i6, zzdoVar.zza(), zzdoVar.zzc(), false, iArr, i4, i59, zzdkVar, zzctVar, zzegVar, zzboVar, zzdaVar);
        }
        zzed zzedVar = (zzed) zzdcVar;
        throw null;
    }

    private static double zzm(Object obj, long j) {
        return ((Double) zzeq.zzf(obj, j)).doubleValue();
    }

    private static float zzn(Object obj, long j) {
        return ((Float) zzeq.zzf(obj, j)).floatValue();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private final int zzo(Object obj) {
        int i;
        int zzx;
        int zzx2;
        int zzy;
        int zzx3;
        int zzx4;
        int zzx5;
        int zzx6;
        int zzt;
        int zzh;
        int zzx7;
        int zzx8;
        int i2;
        int zzx9;
        int zzx10;
        int zzx11;
        int zzx12;
        Unsafe unsafe = zzb;
        int i3 = 1048575;
        int i4 = 0;
        int i5 = 0;
        int i6 = 1048575;
        int i7 = 0;
        while (i4 < this.zzc.length) {
            int zzy2 = zzy(i4);
            int[] iArr = this.zzc;
            int i8 = iArr[i4];
            int zzx13 = zzx(zzy2);
            if (zzx13 <= 17) {
                int i9 = iArr[i4 + 2];
                int i10 = i9 & i3;
                int i11 = i9 >>> 20;
                if (i10 != i6) {
                    i7 = unsafe.getInt(obj, i10);
                    i6 = i10;
                }
                i = 1 << i11;
            } else {
                i = 0;
            }
            long j = zzy2 & i3;
            switch (zzx13) {
                case 0:
                    if ((i7 & i) == 0) {
                        break;
                    } else {
                        zzx = zzbi.zzx(i8 << 3);
                        zzx4 = zzx + 8;
                        i5 += zzx4;
                        break;
                    }
                case 1:
                    if ((i7 & i) == 0) {
                        break;
                    } else {
                        zzx2 = zzbi.zzx(i8 << 3);
                        zzx4 = zzx2 + 4;
                        i5 += zzx4;
                        break;
                    }
                case 2:
                    if ((i7 & i) == 0) {
                        break;
                    } else {
                        zzy = zzbi.zzy(unsafe.getLong(obj, j));
                        zzx3 = zzbi.zzx(i8 << 3);
                        i5 += zzx3 + zzy;
                        break;
                    }
                case 3:
                    if ((i7 & i) == 0) {
                        break;
                    } else {
                        zzy = zzbi.zzy(unsafe.getLong(obj, j));
                        zzx3 = zzbi.zzx(i8 << 3);
                        i5 += zzx3 + zzy;
                        break;
                    }
                case 4:
                    if ((i7 & i) == 0) {
                        break;
                    } else {
                        zzy = zzbi.zzu(unsafe.getInt(obj, j));
                        zzx3 = zzbi.zzx(i8 << 3);
                        i5 += zzx3 + zzy;
                        break;
                    }
                case 5:
                    if ((i7 & i) == 0) {
                        break;
                    } else {
                        zzx = zzbi.zzx(i8 << 3);
                        zzx4 = zzx + 8;
                        i5 += zzx4;
                        break;
                    }
                case 6:
                    if ((i7 & i) == 0) {
                        break;
                    } else {
                        zzx2 = zzbi.zzx(i8 << 3);
                        zzx4 = zzx2 + 4;
                        i5 += zzx4;
                        break;
                    }
                case 7:
                    if ((i7 & i) == 0) {
                        break;
                    } else {
                        zzx4 = zzbi.zzx(i8 << 3) + 1;
                        i5 += zzx4;
                        break;
                    }
                case 8:
                    if ((i7 & i) == 0) {
                        break;
                    } else {
                        Object object = unsafe.getObject(obj, j);
                        if (object instanceof zzba) {
                            int i12 = zzbi.zzb;
                            int zzd = ((zzba) object).zzd();
                            zzx5 = zzbi.zzx(zzd) + zzd;
                            zzx6 = zzbi.zzx(i8 << 3);
                            zzx4 = zzx6 + zzx5;
                            i5 += zzx4;
                            break;
                        } else {
                            zzy = zzbi.zzw((String) object);
                            zzx3 = zzbi.zzx(i8 << 3);
                            i5 += zzx3 + zzy;
                            break;
                        }
                    }
                case 9:
                    if ((i7 & i) == 0) {
                        break;
                    } else {
                        zzx4 = zzdr.zzn(i8, unsafe.getObject(obj, j), zzB(i4));
                        i5 += zzx4;
                        break;
                    }
                case 10:
                    if ((i7 & i) == 0) {
                        break;
                    } else {
                        int i13 = zzbi.zzb;
                        int zzd2 = ((zzba) unsafe.getObject(obj, j)).zzd();
                        zzx5 = zzbi.zzx(zzd2) + zzd2;
                        zzx6 = zzbi.zzx(i8 << 3);
                        zzx4 = zzx6 + zzx5;
                        i5 += zzx4;
                        break;
                    }
                case 11:
                    if ((i7 & i) == 0) {
                        break;
                    } else {
                        zzy = zzbi.zzx(unsafe.getInt(obj, j));
                        zzx3 = zzbi.zzx(i8 << 3);
                        i5 += zzx3 + zzy;
                        break;
                    }
                case 12:
                    if ((i7 & i) == 0) {
                        break;
                    } else {
                        zzy = zzbi.zzu(unsafe.getInt(obj, j));
                        zzx3 = zzbi.zzx(i8 << 3);
                        i5 += zzx3 + zzy;
                        break;
                    }
                case 13:
                    if ((i7 & i) == 0) {
                        break;
                    } else {
                        zzx2 = zzbi.zzx(i8 << 3);
                        zzx4 = zzx2 + 4;
                        i5 += zzx4;
                        break;
                    }
                case 14:
                    if ((i7 & i) == 0) {
                        break;
                    } else {
                        zzx = zzbi.zzx(i8 << 3);
                        zzx4 = zzx + 8;
                        i5 += zzx4;
                        break;
                    }
                case 15:
                    if ((i7 & i) == 0) {
                        break;
                    } else {
                        int i14 = unsafe.getInt(obj, j);
                        zzx3 = zzbi.zzx(i8 << 3);
                        zzy = zzbi.zzx((i14 >> 31) ^ (i14 + i14));
                        i5 += zzx3 + zzy;
                        break;
                    }
                case 16:
                    if ((i & i7) == 0) {
                        break;
                    } else {
                        long j2 = unsafe.getLong(obj, j);
                        i5 += zzbi.zzx(i8 << 3) + zzbi.zzy((j2 >> 63) ^ (j2 + j2));
                        break;
                    }
                case 17:
                    if ((i7 & i) == 0) {
                        break;
                    } else {
                        zzx4 = zzbi.zzt(i8, (zzdf) unsafe.getObject(obj, j), zzB(i4));
                        i5 += zzx4;
                        break;
                    }
                case 18:
                    zzx4 = zzdr.zzg(i8, (List) unsafe.getObject(obj, j), false);
                    i5 += zzx4;
                    break;
                case 19:
                    zzx4 = zzdr.zze(i8, (List) unsafe.getObject(obj, j), false);
                    i5 += zzx4;
                    break;
                case 20:
                    zzx4 = zzdr.zzl(i8, (List) unsafe.getObject(obj, j), false);
                    i5 += zzx4;
                    break;
                case 21:
                    zzx4 = zzdr.zzw(i8, (List) unsafe.getObject(obj, j), false);
                    i5 += zzx4;
                    break;
                case 22:
                    zzx4 = zzdr.zzj(i8, (List) unsafe.getObject(obj, j), false);
                    i5 += zzx4;
                    break;
                case 23:
                    zzx4 = zzdr.zzg(i8, (List) unsafe.getObject(obj, j), false);
                    i5 += zzx4;
                    break;
                case 24:
                    zzx4 = zzdr.zze(i8, (List) unsafe.getObject(obj, j), false);
                    i5 += zzx4;
                    break;
                case 25:
                    zzx4 = zzdr.zza(i8, (List) unsafe.getObject(obj, j), false);
                    i5 += zzx4;
                    break;
                case 26:
                    zzt = zzdr.zzt(i8, (List) unsafe.getObject(obj, j));
                    i5 += zzt;
                    break;
                case 27:
                    zzt = zzdr.zzo(i8, (List) unsafe.getObject(obj, j), zzB(i4));
                    i5 += zzt;
                    break;
                case 28:
                    zzt = zzdr.zzb(i8, (List) unsafe.getObject(obj, j));
                    i5 += zzt;
                    break;
                case 29:
                    zzt = zzdr.zzu(i8, (List) unsafe.getObject(obj, j), false);
                    i5 += zzt;
                    break;
                case 30:
                    zzt = zzdr.zzc(i8, (List) unsafe.getObject(obj, j), false);
                    i5 += zzt;
                    break;
                case R.styleable.AppCompatTheme_actionModeWebSearchDrawable /* 31 */:
                    zzt = zzdr.zze(i8, (List) unsafe.getObject(obj, j), false);
                    i5 += zzt;
                    break;
                case 32:
                    zzt = zzdr.zzg(i8, (List) unsafe.getObject(obj, j), false);
                    i5 += zzt;
                    break;
                case R.styleable.AppCompatTheme_actionOverflowMenuStyle /* 33 */:
                    zzt = zzdr.zzp(i8, (List) unsafe.getObject(obj, j), false);
                    i5 += zzt;
                    break;
                case R.styleable.AppCompatTheme_activityChooserViewStyle /* 34 */:
                    zzt = zzdr.zzr(i8, (List) unsafe.getObject(obj, j), false);
                    i5 += zzt;
                    break;
                case R.styleable.AppCompatTheme_alertDialogButtonGroupStyle /* 35 */:
                    zzh = zzdr.zzh((List) unsafe.getObject(obj, j));
                    if (zzh > 0) {
                        zzx7 = zzbi.zzx(zzh);
                        zzx8 = zzbi.zzx(i8 << 3);
                        i2 = zzx8 + zzx7;
                        i5 += i2 + zzh;
                    }
                    break;
                case R.styleable.AppCompatTheme_alertDialogCenterButtons /* 36 */:
                    zzh = zzdr.zzf((List) unsafe.getObject(obj, j));
                    if (zzh > 0) {
                        zzx7 = zzbi.zzx(zzh);
                        zzx8 = zzbi.zzx(i8 << 3);
                        i2 = zzx8 + zzx7;
                        i5 += i2 + zzh;
                    }
                    break;
                case R.styleable.AppCompatTheme_alertDialogStyle /* 37 */:
                    zzh = zzdr.zzm((List) unsafe.getObject(obj, j));
                    if (zzh > 0) {
                        zzx7 = zzbi.zzx(zzh);
                        zzx8 = zzbi.zzx(i8 << 3);
                        i2 = zzx8 + zzx7;
                        i5 += i2 + zzh;
                    }
                    break;
                case R.styleable.AppCompatTheme_alertDialogTheme /* 38 */:
                    zzh = zzdr.zzx((List) unsafe.getObject(obj, j));
                    if (zzh > 0) {
                        zzx7 = zzbi.zzx(zzh);
                        zzx8 = zzbi.zzx(i8 << 3);
                        i2 = zzx8 + zzx7;
                        i5 += i2 + zzh;
                    }
                    break;
                case R.styleable.AppCompatTheme_autoCompleteTextViewStyle /* 39 */:
                    zzh = zzdr.zzk((List) unsafe.getObject(obj, j));
                    if (zzh > 0) {
                        zzx7 = zzbi.zzx(zzh);
                        zzx8 = zzbi.zzx(i8 << 3);
                        i2 = zzx8 + zzx7;
                        i5 += i2 + zzh;
                    }
                    break;
                case R.styleable.AppCompatTheme_borderlessButtonStyle /* 40 */:
                    zzh = zzdr.zzh((List) unsafe.getObject(obj, j));
                    if (zzh > 0) {
                        zzx7 = zzbi.zzx(zzh);
                        zzx8 = zzbi.zzx(i8 << 3);
                        i2 = zzx8 + zzx7;
                        i5 += i2 + zzh;
                    }
                    break;
                case R.styleable.AppCompatTheme_buttonBarButtonStyle /* 41 */:
                    zzh = zzdr.zzf((List) unsafe.getObject(obj, j));
                    if (zzh > 0) {
                        zzx7 = zzbi.zzx(zzh);
                        zzx8 = zzbi.zzx(i8 << 3);
                        i2 = zzx8 + zzx7;
                        i5 += i2 + zzh;
                    }
                    break;
                case R.styleable.AppCompatTheme_buttonBarNegativeButtonStyle /* 42 */:
                    int i15 = zzdr.zza;
                    zzh = ((List) unsafe.getObject(obj, j)).size();
                    if (zzh > 0) {
                        zzx7 = zzbi.zzx(zzh);
                        zzx8 = zzbi.zzx(i8 << 3);
                        i2 = zzx8 + zzx7;
                        i5 += i2 + zzh;
                    }
                    break;
                case R.styleable.AppCompatTheme_buttonBarNeutralButtonStyle /* 43 */:
                    zzh = zzdr.zzv((List) unsafe.getObject(obj, j));
                    if (zzh > 0) {
                        zzx7 = zzbi.zzx(zzh);
                        zzx8 = zzbi.zzx(i8 << 3);
                        i2 = zzx8 + zzx7;
                        i5 += i2 + zzh;
                    }
                    break;
                case R.styleable.AppCompatTheme_buttonBarPositiveButtonStyle /* 44 */:
                    zzh = zzdr.zzd((List) unsafe.getObject(obj, j));
                    if (zzh > 0) {
                        zzx7 = zzbi.zzx(zzh);
                        zzx8 = zzbi.zzx(i8 << 3);
                        i2 = zzx8 + zzx7;
                        i5 += i2 + zzh;
                    }
                    break;
                case R.styleable.AppCompatTheme_buttonBarStyle /* 45 */:
                    zzh = zzdr.zzf((List) unsafe.getObject(obj, j));
                    if (zzh > 0) {
                        zzx7 = zzbi.zzx(zzh);
                        zzx8 = zzbi.zzx(i8 << 3);
                        i2 = zzx8 + zzx7;
                        i5 += i2 + zzh;
                    }
                    break;
                case R.styleable.AppCompatTheme_buttonStyle /* 46 */:
                    zzh = zzdr.zzh((List) unsafe.getObject(obj, j));
                    if (zzh > 0) {
                        zzx7 = zzbi.zzx(zzh);
                        zzx8 = zzbi.zzx(i8 << 3);
                        i2 = zzx8 + zzx7;
                        i5 += i2 + zzh;
                    }
                    break;
                case R.styleable.AppCompatTheme_buttonStyleSmall /* 47 */:
                    zzh = zzdr.zzq((List) unsafe.getObject(obj, j));
                    if (zzh > 0) {
                        zzx7 = zzbi.zzx(zzh);
                        zzx8 = zzbi.zzx(i8 << 3);
                        i2 = zzx8 + zzx7;
                        i5 += i2 + zzh;
                    }
                    break;
                case R.styleable.AppCompatTheme_checkboxStyle /* 48 */:
                    zzh = zzdr.zzs((List) unsafe.getObject(obj, j));
                    if (zzh > 0) {
                        zzx7 = zzbi.zzx(zzh);
                        zzx8 = zzbi.zzx(i8 << 3);
                        i2 = zzx8 + zzx7;
                        i5 += i2 + zzh;
                    }
                    break;
                case R.styleable.AppCompatTheme_checkedTextViewStyle /* 49 */:
                    zzt = zzdr.zzi(i8, (List) unsafe.getObject(obj, j), zzB(i4));
                    i5 += zzt;
                    break;
                case R.styleable.AppCompatTheme_colorAccent /* 50 */:
                    zzda.zza(i8, unsafe.getObject(obj, j), zzC(i4));
                    break;
                case R.styleable.AppCompatTheme_colorBackgroundFloating /* 51 */:
                    if (zzT(obj, i8, i4)) {
                        zzx9 = zzbi.zzx(i8 << 3);
                        zzt = zzx9 + 8;
                        i5 += zzt;
                    }
                    break;
                case R.styleable.AppCompatTheme_colorButtonNormal /* 52 */:
                    if (zzT(obj, i8, i4)) {
                        zzx10 = zzbi.zzx(i8 << 3);
                        zzt = zzx10 + 4;
                        i5 += zzt;
                    }
                    break;
                case R.styleable.AppCompatTheme_colorControlActivated /* 53 */:
                    if (zzT(obj, i8, i4)) {
                        zzh = zzbi.zzy(zzz(obj, j));
                        i2 = zzbi.zzx(i8 << 3);
                        i5 += i2 + zzh;
                    }
                    break;
                case R.styleable.AppCompatTheme_colorControlHighlight /* 54 */:
                    if (zzT(obj, i8, i4)) {
                        zzh = zzbi.zzy(zzz(obj, j));
                        i2 = zzbi.zzx(i8 << 3);
                        i5 += i2 + zzh;
                    }
                    break;
                case R.styleable.AppCompatTheme_colorControlNormal /* 55 */:
                    if (zzT(obj, i8, i4)) {
                        zzh = zzbi.zzu(zzp(obj, j));
                        i2 = zzbi.zzx(i8 << 3);
                        i5 += i2 + zzh;
                    }
                    break;
                case R.styleable.AppCompatTheme_colorError /* 56 */:
                    if (zzT(obj, i8, i4)) {
                        zzx9 = zzbi.zzx(i8 << 3);
                        zzt = zzx9 + 8;
                        i5 += zzt;
                    }
                    break;
                case R.styleable.AppCompatTheme_colorPrimary /* 57 */:
                    if (zzT(obj, i8, i4)) {
                        zzx10 = zzbi.zzx(i8 << 3);
                        zzt = zzx10 + 4;
                        i5 += zzt;
                    }
                    break;
                case R.styleable.AppCompatTheme_colorPrimaryDark /* 58 */:
                    if (zzT(obj, i8, i4)) {
                        zzt = zzbi.zzx(i8 << 3) + 1;
                        i5 += zzt;
                    }
                    break;
                case R.styleable.AppCompatTheme_colorSwitchThumbNormal /* 59 */:
                    if (zzT(obj, i8, i4)) {
                        Object object2 = unsafe.getObject(obj, j);
                        if (object2 instanceof zzba) {
                            int i16 = zzbi.zzb;
                            int zzd3 = ((zzba) object2).zzd();
                            zzx11 = zzbi.zzx(zzd3) + zzd3;
                            zzx12 = zzbi.zzx(i8 << 3);
                            zzt = zzx12 + zzx11;
                            i5 += zzt;
                        } else {
                            zzh = zzbi.zzw((String) object2);
                            i2 = zzbi.zzx(i8 << 3);
                            i5 += i2 + zzh;
                        }
                    }
                    break;
                case R.styleable.AppCompatTheme_controlBackground /* 60 */:
                    if (zzT(obj, i8, i4)) {
                        zzt = zzdr.zzn(i8, unsafe.getObject(obj, j), zzB(i4));
                        i5 += zzt;
                    }
                    break;
                case R.styleable.AppCompatTheme_dialogCornerRadius /* 61 */:
                    if (zzT(obj, i8, i4)) {
                        int i17 = zzbi.zzb;
                        int zzd4 = ((zzba) unsafe.getObject(obj, j)).zzd();
                        zzx11 = zzbi.zzx(zzd4) + zzd4;
                        zzx12 = zzbi.zzx(i8 << 3);
                        zzt = zzx12 + zzx11;
                        i5 += zzt;
                    }
                    break;
                case R.styleable.AppCompatTheme_dialogPreferredPadding /* 62 */:
                    if (zzT(obj, i8, i4)) {
                        zzh = zzbi.zzx(zzp(obj, j));
                        i2 = zzbi.zzx(i8 << 3);
                        i5 += i2 + zzh;
                    }
                    break;
                case R.styleable.AppCompatTheme_dialogTheme /* 63 */:
                    if (zzT(obj, i8, i4)) {
                        zzh = zzbi.zzu(zzp(obj, j));
                        i2 = zzbi.zzx(i8 << 3);
                        i5 += i2 + zzh;
                    }
                    break;
                case 64:
                    if (zzT(obj, i8, i4)) {
                        zzx10 = zzbi.zzx(i8 << 3);
                        zzt = zzx10 + 4;
                        i5 += zzt;
                    }
                    break;
                case 65:
                    if (zzT(obj, i8, i4)) {
                        zzx9 = zzbi.zzx(i8 << 3);
                        zzt = zzx9 + 8;
                        i5 += zzt;
                    }
                    break;
                case R.styleable.AppCompatTheme_dropDownListViewStyle /* 66 */:
                    if (zzT(obj, i8, i4)) {
                        int zzp = zzp(obj, j);
                        i2 = zzbi.zzx(i8 << 3);
                        zzh = zzbi.zzx((zzp >> 31) ^ (zzp + zzp));
                        i5 += i2 + zzh;
                    }
                    break;
                case R.styleable.AppCompatTheme_dropdownListPreferredItemHeight /* 67 */:
                    if (zzT(obj, i8, i4)) {
                        long zzz = zzz(obj, j);
                        i5 += zzbi.zzx(i8 << 3) + zzbi.zzy((zzz >> 63) ^ (zzz + zzz));
                    }
                    break;
                case R.styleable.AppCompatTheme_editTextBackground /* 68 */:
                    if (zzT(obj, i8, i4)) {
                        zzt = zzbi.zzt(i8, (zzdf) unsafe.getObject(obj, j), zzB(i4));
                        i5 += zzt;
                    }
                    break;
            }
            i4 += 3;
            i3 = 1048575;
        }
        zzeg zzegVar = this.zzm;
        int zza2 = i5 + zzegVar.zza(zzegVar.zzd(obj));
        if (this.zzh) {
            this.zzn.zza(obj);
            throw null;
        }
        return zza2;
    }

    private static int zzp(Object obj, long j) {
        return ((Integer) zzeq.zzf(obj, j)).intValue();
    }

    private final int zzq(Object obj, byte[] bArr, int i, int i2, int i3, long j, zzan zzanVar) throws IOException {
        Unsafe unsafe = zzb;
        Object zzC = zzC(i3);
        Object object = unsafe.getObject(obj, j);
        if (!((zzcz) object).zze()) {
            zzcz zzb2 = zzcz.zza().zzb();
            zzda.zzb(zzb2, object);
            unsafe.putObject(obj, j, zzb2);
        }
        zzcy zzcyVar = (zzcy) zzC;
        throw null;
    }

    private final int zzr(Object obj, byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6, int i7, long j, int i8, zzan zzanVar) throws IOException {
        Unsafe unsafe = zzb;
        long j2 = this.zzc[i8 + 2] & 1048575;
        switch (i7) {
            case R.styleable.AppCompatTheme_colorBackgroundFloating /* 51 */:
                if (i5 == 1) {
                    unsafe.putObject(obj, j, Double.valueOf(Double.longBitsToDouble(zzao.zzp(bArr, i))));
                    int i9 = i + 8;
                    unsafe.putInt(obj, j2, i4);
                    return i9;
                }
                break;
            case R.styleable.AppCompatTheme_colorButtonNormal /* 52 */:
                if (i5 == 5) {
                    unsafe.putObject(obj, j, Float.valueOf(Float.intBitsToFloat(zzao.zzb(bArr, i))));
                    int i10 = i + 4;
                    unsafe.putInt(obj, j2, i4);
                    return i10;
                }
                break;
            case R.styleable.AppCompatTheme_colorControlActivated /* 53 */:
            case R.styleable.AppCompatTheme_colorControlHighlight /* 54 */:
                if (i5 == 0) {
                    int zzm = zzao.zzm(bArr, i, zzanVar);
                    unsafe.putObject(obj, j, Long.valueOf(zzanVar.zzb));
                    unsafe.putInt(obj, j2, i4);
                    return zzm;
                }
                break;
            case R.styleable.AppCompatTheme_colorControlNormal /* 55 */:
            case R.styleable.AppCompatTheme_dialogPreferredPadding /* 62 */:
                if (i5 == 0) {
                    int zzj = zzao.zzj(bArr, i, zzanVar);
                    unsafe.putObject(obj, j, Integer.valueOf(zzanVar.zza));
                    unsafe.putInt(obj, j2, i4);
                    return zzj;
                }
                break;
            case R.styleable.AppCompatTheme_colorError /* 56 */:
            case 65:
                if (i5 == 1) {
                    unsafe.putObject(obj, j, Long.valueOf(zzao.zzp(bArr, i)));
                    int i11 = i + 8;
                    unsafe.putInt(obj, j2, i4);
                    return i11;
                }
                break;
            case R.styleable.AppCompatTheme_colorPrimary /* 57 */:
            case 64:
                if (i5 == 5) {
                    unsafe.putObject(obj, j, Integer.valueOf(zzao.zzb(bArr, i)));
                    int i12 = i + 4;
                    unsafe.putInt(obj, j2, i4);
                    return i12;
                }
                break;
            case R.styleable.AppCompatTheme_colorPrimaryDark /* 58 */:
                if (i5 == 0) {
                    int zzm2 = zzao.zzm(bArr, i, zzanVar);
                    unsafe.putObject(obj, j, Boolean.valueOf(zzanVar.zzb != 0));
                    unsafe.putInt(obj, j2, i4);
                    return zzm2;
                }
                break;
            case R.styleable.AppCompatTheme_colorSwitchThumbNormal /* 59 */:
                if (i5 == 2) {
                    int zzj2 = zzao.zzj(bArr, i, zzanVar);
                    int i13 = zzanVar.zza;
                    if (i13 == 0) {
                        unsafe.putObject(obj, j, "");
                    } else if ((i6 & 536870912) == 0 || zzev.zze(bArr, zzj2, zzj2 + i13)) {
                        unsafe.putObject(obj, j, new String(bArr, zzj2, i13, zzcg.zzb));
                        zzj2 += i13;
                    } else {
                        throw zzci.zzc();
                    }
                    unsafe.putInt(obj, j2, i4);
                    return zzj2;
                }
                break;
            case R.styleable.AppCompatTheme_controlBackground /* 60 */:
                if (i5 == 2) {
                    Object zzE = zzE(obj, i4, i8);
                    int zzo = zzao.zzo(zzE, zzB(i8), bArr, i, i2, zzanVar);
                    zzM(obj, i4, i8, zzE);
                    return zzo;
                }
                break;
            case R.styleable.AppCompatTheme_dialogCornerRadius /* 61 */:
                if (i5 == 2) {
                    int zza2 = zzao.zza(bArr, i, zzanVar);
                    unsafe.putObject(obj, j, zzanVar.zzc);
                    unsafe.putInt(obj, j2, i4);
                    return zza2;
                }
                break;
            case R.styleable.AppCompatTheme_dialogTheme /* 63 */:
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
            case R.styleable.AppCompatTheme_dropDownListViewStyle /* 66 */:
                if (i5 == 0) {
                    int zzj4 = zzao.zzj(bArr, i, zzanVar);
                    unsafe.putObject(obj, j, Integer.valueOf(zzbe.zzb(zzanVar.zza)));
                    unsafe.putInt(obj, j2, i4);
                    return zzj4;
                }
                break;
            case R.styleable.AppCompatTheme_dropdownListPreferredItemHeight /* 67 */:
                if (i5 == 0) {
                    int zzm3 = zzao.zzm(bArr, i, zzanVar);
                    unsafe.putObject(obj, j, Long.valueOf(zzbe.zzc(zzanVar.zzb)));
                    unsafe.putInt(obj, j2, i4);
                    return zzm3;
                }
                break;
            case R.styleable.AppCompatTheme_editTextBackground /* 68 */:
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

    /* JADX WARN: Removed duplicated region for block: B:115:0x0219  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x0267  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x019e  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:113:0x0216 -> B:114:0x0217). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:133:0x0264 -> B:134:0x0265). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:81:0x019b -> B:82:0x019c). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final int zzs(Object obj, byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6, long j, int i7, long j2, zzan zzanVar) throws IOException {
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int zzl;
        int i13 = i;
        Unsafe unsafe = zzb;
        zzcf zzcfVar = (zzcf) unsafe.getObject(obj, j2);
        if (!zzcfVar.zzc()) {
            int size = zzcfVar.size();
            zzcfVar = zzcfVar.zzd(size == 0 ? 10 : size + size);
            unsafe.putObject(obj, j2, zzcfVar);
        }
        switch (i7) {
            case 18:
            case R.styleable.AppCompatTheme_alertDialogButtonGroupStyle /* 35 */:
                if (i5 == 2) {
                    zzbk zzbkVar = (zzbk) zzcfVar;
                    int zzj = zzao.zzj(bArr, i13, zzanVar);
                    int i14 = zzanVar.zza + zzj;
                    while (zzj < i14) {
                        zzbkVar.zze(Double.longBitsToDouble(zzao.zzp(bArr, zzj)));
                        zzj += 8;
                    }
                    if (zzj == i14) {
                        return zzj;
                    }
                    throw zzci.zzg();
                } else if (i5 == 1) {
                    zzbk zzbkVar2 = (zzbk) zzcfVar;
                    zzbkVar2.zze(Double.longBitsToDouble(zzao.zzp(bArr, i)));
                    while (true) {
                        i8 = i13 + 8;
                        if (i8 < i2) {
                            i13 = zzao.zzj(bArr, i8, zzanVar);
                            if (i3 == zzanVar.zza) {
                                zzbkVar2.zze(Double.longBitsToDouble(zzao.zzp(bArr, i13)));
                            }
                        }
                    }
                    return i8;
                }
                break;
            case 19:
            case R.styleable.AppCompatTheme_alertDialogCenterButtons /* 36 */:
                if (i5 == 2) {
                    zzbu zzbuVar = (zzbu) zzcfVar;
                    int zzj2 = zzao.zzj(bArr, i13, zzanVar);
                    int i15 = zzanVar.zza + zzj2;
                    while (zzj2 < i15) {
                        zzbuVar.zze(Float.intBitsToFloat(zzao.zzb(bArr, zzj2)));
                        zzj2 += 4;
                    }
                    if (zzj2 == i15) {
                        return zzj2;
                    }
                    throw zzci.zzg();
                } else if (i5 == 5) {
                    zzbu zzbuVar2 = (zzbu) zzcfVar;
                    zzbuVar2.zze(Float.intBitsToFloat(zzao.zzb(bArr, i)));
                    while (true) {
                        i9 = i13 + 4;
                        if (i9 < i2) {
                            i13 = zzao.zzj(bArr, i9, zzanVar);
                            if (i3 == zzanVar.zza) {
                                zzbuVar2.zze(Float.intBitsToFloat(zzao.zzb(bArr, i13)));
                            }
                        }
                    }
                    return i9;
                }
                break;
            case 20:
            case 21:
            case R.styleable.AppCompatTheme_alertDialogStyle /* 37 */:
            case R.styleable.AppCompatTheme_alertDialogTheme /* 38 */:
                if (i5 == 2) {
                    zzcu zzcuVar = (zzcu) zzcfVar;
                    int zzj3 = zzao.zzj(bArr, i13, zzanVar);
                    int i16 = zzanVar.zza + zzj3;
                    while (zzj3 < i16) {
                        zzj3 = zzao.zzm(bArr, zzj3, zzanVar);
                        zzcuVar.zzf(zzanVar.zzb);
                    }
                    if (zzj3 == i16) {
                        return zzj3;
                    }
                    throw zzci.zzg();
                } else if (i5 == 0) {
                    zzcu zzcuVar2 = (zzcu) zzcfVar;
                    int zzm = zzao.zzm(bArr, i13, zzanVar);
                    zzcuVar2.zzf(zzanVar.zzb);
                    while (zzm < i2) {
                        int zzj4 = zzao.zzj(bArr, zzm, zzanVar);
                        if (i3 != zzanVar.zza) {
                            return zzm;
                        }
                        zzm = zzao.zzm(bArr, zzj4, zzanVar);
                        zzcuVar2.zzf(zzanVar.zzb);
                    }
                    return zzm;
                }
                break;
            case 22:
            case 29:
            case R.styleable.AppCompatTheme_autoCompleteTextViewStyle /* 39 */:
            case R.styleable.AppCompatTheme_buttonBarNeutralButtonStyle /* 43 */:
                if (i5 == 2) {
                    return zzao.zzf(bArr, i13, zzcfVar, zzanVar);
                }
                if (i5 == 0) {
                    return zzao.zzl(i3, bArr, i, i2, zzcfVar, zzanVar);
                }
                break;
            case 23:
            case 32:
            case R.styleable.AppCompatTheme_borderlessButtonStyle /* 40 */:
            case R.styleable.AppCompatTheme_buttonStyle /* 46 */:
                if (i5 == 2) {
                    zzcu zzcuVar3 = (zzcu) zzcfVar;
                    int zzj5 = zzao.zzj(bArr, i13, zzanVar);
                    int i17 = zzanVar.zza + zzj5;
                    while (zzj5 < i17) {
                        zzcuVar3.zzf(zzao.zzp(bArr, zzj5));
                        zzj5 += 8;
                    }
                    if (zzj5 == i17) {
                        return zzj5;
                    }
                    throw zzci.zzg();
                } else if (i5 == 1) {
                    zzcu zzcuVar4 = (zzcu) zzcfVar;
                    zzcuVar4.zzf(zzao.zzp(bArr, i));
                    while (true) {
                        i10 = i13 + 8;
                        if (i10 < i2) {
                            i13 = zzao.zzj(bArr, i10, zzanVar);
                            if (i3 == zzanVar.zza) {
                                zzcuVar4.zzf(zzao.zzp(bArr, i13));
                            }
                        }
                    }
                    return i10;
                }
                break;
            case 24:
            case R.styleable.AppCompatTheme_actionModeWebSearchDrawable /* 31 */:
            case R.styleable.AppCompatTheme_buttonBarButtonStyle /* 41 */:
            case R.styleable.AppCompatTheme_buttonBarStyle /* 45 */:
                if (i5 == 2) {
                    zzcc zzccVar = (zzcc) zzcfVar;
                    int zzj6 = zzao.zzj(bArr, i13, zzanVar);
                    int i18 = zzanVar.zza + zzj6;
                    while (zzj6 < i18) {
                        zzccVar.zzf(zzao.zzb(bArr, zzj6));
                        zzj6 += 4;
                    }
                    if (zzj6 == i18) {
                        return zzj6;
                    }
                    throw zzci.zzg();
                } else if (i5 == 5) {
                    zzcc zzccVar2 = (zzcc) zzcfVar;
                    zzccVar2.zzf(zzao.zzb(bArr, i));
                    while (true) {
                        i11 = i13 + 4;
                        if (i11 < i2) {
                            i13 = zzao.zzj(bArr, i11, zzanVar);
                            if (i3 == zzanVar.zza) {
                                zzccVar2.zzf(zzao.zzb(bArr, i13));
                            }
                        }
                    }
                    return i11;
                }
                break;
            case 25:
            case R.styleable.AppCompatTheme_buttonBarNegativeButtonStyle /* 42 */:
                if (i5 == 2) {
                    zzap zzapVar = (zzap) zzcfVar;
                    int zzj7 = zzao.zzj(bArr, i13, zzanVar);
                    int i19 = zzanVar.zza + zzj7;
                    while (zzj7 < i19) {
                        zzj7 = zzao.zzm(bArr, zzj7, zzanVar);
                        zzapVar.zze(zzanVar.zzb != 0);
                    }
                    if (zzj7 == i19) {
                        return zzj7;
                    }
                    throw zzci.zzg();
                } else if (i5 == 0) {
                    zzap zzapVar2 = (zzap) zzcfVar;
                    int zzm2 = zzao.zzm(bArr, i13, zzanVar);
                    zzapVar2.zze(zzanVar.zzb != 0);
                    while (zzm2 < i2) {
                        int zzj8 = zzao.zzj(bArr, zzm2, zzanVar);
                        if (i3 != zzanVar.zza) {
                            return zzm2;
                        }
                        zzm2 = zzao.zzm(bArr, zzj8, zzanVar);
                        zzapVar2.zze(zzanVar.zzb != 0);
                    }
                    return zzm2;
                }
                break;
            case 26:
                if (i5 == 2) {
                    if ((j & 536870912) != 0) {
                        i13 = zzao.zzj(bArr, i13, zzanVar);
                        int i20 = zzanVar.zza;
                        if (i20 < 0) {
                            throw zzci.zzd();
                        }
                        if (i20 == 0) {
                            zzcfVar.add("");
                            while (i13 < i2) {
                                int zzj9 = zzao.zzj(bArr, i13, zzanVar);
                                if (i3 != zzanVar.zza) {
                                    break;
                                } else {
                                    i13 = zzao.zzj(bArr, zzj9, zzanVar);
                                    int i21 = zzanVar.zza;
                                    if (i21 < 0) {
                                        throw zzci.zzd();
                                    }
                                    if (i21 == 0) {
                                        zzcfVar.add("");
                                    } else {
                                        i12 = i13 + i21;
                                        if (zzev.zze(bArr, i13, i12)) {
                                            zzcfVar.add(new String(bArr, i13, i21, zzcg.zzb));
                                            i13 = i12;
                                            while (i13 < i2) {
                                            }
                                        } else {
                                            throw zzci.zzc();
                                        }
                                    }
                                }
                            }
                            break;
                        } else {
                            i12 = i13 + i20;
                            if (!zzev.zze(bArr, i13, i12)) {
                                throw zzci.zzc();
                            }
                            zzcfVar.add(new String(bArr, i13, i20, zzcg.zzb));
                            i13 = i12;
                            while (i13 < i2) {
                            }
                        }
                    } else {
                        i13 = zzao.zzj(bArr, i13, zzanVar);
                        int i22 = zzanVar.zza;
                        if (i22 < 0) {
                            throw zzci.zzd();
                        }
                        if (i22 == 0) {
                            zzcfVar.add("");
                            while (i13 < i2) {
                                int zzj10 = zzao.zzj(bArr, i13, zzanVar);
                                if (i3 != zzanVar.zza) {
                                    break;
                                } else {
                                    i13 = zzao.zzj(bArr, zzj10, zzanVar);
                                    i22 = zzanVar.zza;
                                    if (i22 < 0) {
                                        throw zzci.zzd();
                                    }
                                    if (i22 == 0) {
                                        zzcfVar.add("");
                                    } else {
                                        zzcfVar.add(new String(bArr, i13, i22, zzcg.zzb));
                                        i13 += i22;
                                        while (i13 < i2) {
                                        }
                                    }
                                }
                            }
                            break;
                        } else {
                            zzcfVar.add(new String(bArr, i13, i22, zzcg.zzb));
                            i13 += i22;
                            while (i13 < i2) {
                            }
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
                    int zzj11 = zzao.zzj(bArr, i13, zzanVar);
                    int i23 = zzanVar.zza;
                    if (i23 < 0) {
                        throw zzci.zzd();
                    }
                    if (i23 <= bArr.length - zzj11) {
                        if (i23 == 0) {
                            zzcfVar.add(zzba.zzb);
                            while (zzj11 < i2) {
                                int zzj12 = zzao.zzj(bArr, zzj11, zzanVar);
                                if (i3 != zzanVar.zza) {
                                    return zzj11;
                                }
                                zzj11 = zzao.zzj(bArr, zzj12, zzanVar);
                                i23 = zzanVar.zza;
                                if (i23 >= 0) {
                                    if (i23 > bArr.length - zzj11) {
                                        throw zzci.zzg();
                                    }
                                    if (i23 == 0) {
                                        zzcfVar.add(zzba.zzb);
                                    } else {
                                        zzcfVar.add(zzba.zzl(bArr, zzj11, i23));
                                        zzj11 += i23;
                                        while (zzj11 < i2) {
                                        }
                                    }
                                } else {
                                    throw zzci.zzd();
                                }
                            }
                            return zzj11;
                        }
                        zzcfVar.add(zzba.zzl(bArr, zzj11, i23));
                        zzj11 += i23;
                        while (zzj11 < i2) {
                        }
                        return zzj11;
                    }
                    throw zzci.zzg();
                }
                break;
            case 30:
            case R.styleable.AppCompatTheme_buttonBarPositiveButtonStyle /* 44 */:
                if (i5 == 2) {
                    zzl = zzao.zzf(bArr, i13, zzcfVar, zzanVar);
                } else if (i5 == 0) {
                    zzl = zzao.zzl(i3, bArr, i, i2, zzcfVar, zzanVar);
                }
                zzce zzA = zzA(i6);
                zzeg zzegVar = this.zzm;
                int i24 = zzdr.zza;
                if (zzA != null) {
                    Object obj2 = null;
                    if (zzcfVar instanceof RandomAccess) {
                        int size2 = zzcfVar.size();
                        int i25 = 0;
                        for (int i26 = 0; i26 < size2; i26++) {
                            int intValue = ((Integer) zzcfVar.get(i26)).intValue();
                            if (zzA.zza(intValue)) {
                                if (i26 != i25) {
                                    zzcfVar.set(i25, Integer.valueOf(intValue));
                                }
                                i25++;
                            } else {
                                obj2 = zzdr.zzA(obj, i4, intValue, obj2, zzegVar);
                            }
                        }
                        if (i25 != size2) {
                            zzcfVar.subList(i25, size2).clear();
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
            case R.styleable.AppCompatTheme_actionOverflowMenuStyle /* 33 */:
            case R.styleable.AppCompatTheme_buttonStyleSmall /* 47 */:
                if (i5 == 2) {
                    zzcc zzccVar3 = (zzcc) zzcfVar;
                    int zzj13 = zzao.zzj(bArr, i13, zzanVar);
                    int i27 = zzanVar.zza + zzj13;
                    while (zzj13 < i27) {
                        zzj13 = zzao.zzj(bArr, zzj13, zzanVar);
                        zzccVar3.zzf(zzbe.zzb(zzanVar.zza));
                    }
                    if (zzj13 == i27) {
                        return zzj13;
                    }
                    throw zzci.zzg();
                } else if (i5 == 0) {
                    zzcc zzccVar4 = (zzcc) zzcfVar;
                    int zzj14 = zzao.zzj(bArr, i13, zzanVar);
                    zzccVar4.zzf(zzbe.zzb(zzanVar.zza));
                    while (zzj14 < i2) {
                        int zzj15 = zzao.zzj(bArr, zzj14, zzanVar);
                        if (i3 != zzanVar.zza) {
                            return zzj14;
                        }
                        zzj14 = zzao.zzj(bArr, zzj15, zzanVar);
                        zzccVar4.zzf(zzbe.zzb(zzanVar.zza));
                    }
                    return zzj14;
                }
                break;
            case R.styleable.AppCompatTheme_activityChooserViewStyle /* 34 */:
            case R.styleable.AppCompatTheme_checkboxStyle /* 48 */:
                if (i5 == 2) {
                    zzcu zzcuVar5 = (zzcu) zzcfVar;
                    int zzj16 = zzao.zzj(bArr, i13, zzanVar);
                    int i28 = zzanVar.zza + zzj16;
                    while (zzj16 < i28) {
                        zzj16 = zzao.zzm(bArr, zzj16, zzanVar);
                        zzcuVar5.zzf(zzbe.zzc(zzanVar.zzb));
                    }
                    if (zzj16 == i28) {
                        return zzj16;
                    }
                    throw zzci.zzg();
                } else if (i5 == 0) {
                    zzcu zzcuVar6 = (zzcu) zzcfVar;
                    int zzm3 = zzao.zzm(bArr, i13, zzanVar);
                    zzcuVar6.zzf(zzbe.zzc(zzanVar.zzb));
                    while (zzm3 < i2) {
                        int zzj17 = zzao.zzj(bArr, zzm3, zzanVar);
                        if (i3 != zzanVar.zza) {
                            return zzm3;
                        }
                        zzm3 = zzao.zzm(bArr, zzj17, zzanVar);
                        zzcuVar6.zzf(zzbe.zzc(zzanVar.zzb));
                    }
                    return zzm3;
                }
                break;
            default:
                if (i5 == 3) {
                    zzdp zzB = zzB(i6);
                    int i29 = (i3 & (-8)) | 4;
                    int zzc = zzao.zzc(zzB, bArr, i, i2, i29, zzanVar);
                    zzcfVar.add(zzanVar.zzc);
                    while (zzc < i2) {
                        int zzj18 = zzao.zzj(bArr, zzc, zzanVar);
                        if (i3 != zzanVar.zza) {
                            return zzc;
                        }
                        zzc = zzao.zzc(zzB, bArr, zzj18, i2, i29, zzanVar);
                        zzcfVar.add(zzanVar.zzc);
                    }
                    return zzc;
                }
                break;
        }
        return i13;
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
        return (i >>> 20) & 255;
    }

    private final int zzy(int i) {
        return this.zzc[i + 1];
    }

    private static long zzz(Object obj, long j) {
        return ((Long) zzeq.zzf(obj, j)).longValue();
    }

    @Override // com.google.android.gms.internal.play_billing.zzdp
    public final int zza(Object obj) {
        int zzx;
        int zzx2;
        int zzy;
        int zzx3;
        int zzx4;
        int zzx5;
        int zzx6;
        int zzn;
        int zzx7;
        int zzy2;
        int zzx8;
        int zzx9;
        zzew zzewVar = zzew.zza;
        if (this.zzo - 1 != 0) {
            Unsafe unsafe = zzb;
            int i = 0;
            for (int i2 = 0; i2 < this.zzc.length; i2 += 3) {
                int zzy3 = zzy(i2);
                int zzx10 = zzx(zzy3);
                int i3 = this.zzc[i2];
                int i4 = zzy3 & 1048575;
                if (zzx10 >= zzbt.zzJ.zza() && zzx10 <= zzbt.zzW.zza()) {
                    int i5 = this.zzc[i2 + 2];
                }
                long j = i4;
                switch (zzx10) {
                    case 0:
                        if (zzP(obj, i2)) {
                            zzx = zzbi.zzx(i3 << 3);
                            zzn = zzx + 8;
                            i += zzn;
                            break;
                        } else {
                            break;
                        }
                    case 1:
                        if (zzP(obj, i2)) {
                            zzx2 = zzbi.zzx(i3 << 3);
                            zzn = zzx2 + 4;
                            i += zzn;
                            break;
                        } else {
                            break;
                        }
                    case 2:
                        if (zzP(obj, i2)) {
                            zzy = zzbi.zzy(zzeq.zzd(obj, j));
                            zzx3 = zzbi.zzx(i3 << 3);
                            i += zzx3 + zzy;
                            break;
                        } else {
                            break;
                        }
                    case 3:
                        if (zzP(obj, i2)) {
                            zzy = zzbi.zzy(zzeq.zzd(obj, j));
                            zzx3 = zzbi.zzx(i3 << 3);
                            i += zzx3 + zzy;
                            break;
                        } else {
                            break;
                        }
                    case 4:
                        if (zzP(obj, i2)) {
                            zzy = zzbi.zzu(zzeq.zzc(obj, j));
                            zzx3 = zzbi.zzx(i3 << 3);
                            i += zzx3 + zzy;
                            break;
                        } else {
                            break;
                        }
                    case 5:
                        if (zzP(obj, i2)) {
                            zzx = zzbi.zzx(i3 << 3);
                            zzn = zzx + 8;
                            i += zzn;
                            break;
                        } else {
                            break;
                        }
                    case 6:
                        if (zzP(obj, i2)) {
                            zzx2 = zzbi.zzx(i3 << 3);
                            zzn = zzx2 + 4;
                            i += zzn;
                            break;
                        } else {
                            break;
                        }
                    case 7:
                        if (zzP(obj, i2)) {
                            zzx4 = zzbi.zzx(i3 << 3);
                            zzn = zzx4 + 1;
                            i += zzn;
                            break;
                        } else {
                            break;
                        }
                    case 8:
                        if (zzP(obj, i2)) {
                            Object zzf = zzeq.zzf(obj, j);
                            if (zzf instanceof zzba) {
                                int i6 = i3 << 3;
                                int i7 = zzbi.zzb;
                                int zzd = ((zzba) zzf).zzd();
                                zzx5 = zzbi.zzx(zzd) + zzd;
                                zzx6 = zzbi.zzx(i6);
                                zzn = zzx6 + zzx5;
                                i += zzn;
                                break;
                            } else {
                                zzy = zzbi.zzw((String) zzf);
                                zzx3 = zzbi.zzx(i3 << 3);
                                i += zzx3 + zzy;
                                break;
                            }
                        } else {
                            break;
                        }
                    case 9:
                        if (zzP(obj, i2)) {
                            zzn = zzdr.zzn(i3, zzeq.zzf(obj, j), zzB(i2));
                            i += zzn;
                            break;
                        } else {
                            break;
                        }
                    case 10:
                        if (zzP(obj, i2)) {
                            int i8 = i3 << 3;
                            int i9 = zzbi.zzb;
                            int zzd2 = ((zzba) zzeq.zzf(obj, j)).zzd();
                            zzx5 = zzbi.zzx(zzd2) + zzd2;
                            zzx6 = zzbi.zzx(i8);
                            zzn = zzx6 + zzx5;
                            i += zzn;
                            break;
                        } else {
                            break;
                        }
                    case 11:
                        if (zzP(obj, i2)) {
                            zzy = zzbi.zzx(zzeq.zzc(obj, j));
                            zzx3 = zzbi.zzx(i3 << 3);
                            i += zzx3 + zzy;
                            break;
                        } else {
                            break;
                        }
                    case 12:
                        if (zzP(obj, i2)) {
                            zzy = zzbi.zzu(zzeq.zzc(obj, j));
                            zzx3 = zzbi.zzx(i3 << 3);
                            i += zzx3 + zzy;
                            break;
                        } else {
                            break;
                        }
                    case 13:
                        if (zzP(obj, i2)) {
                            zzx2 = zzbi.zzx(i3 << 3);
                            zzn = zzx2 + 4;
                            i += zzn;
                            break;
                        } else {
                            break;
                        }
                    case 14:
                        if (zzP(obj, i2)) {
                            zzx = zzbi.zzx(i3 << 3);
                            zzn = zzx + 8;
                            i += zzn;
                            break;
                        } else {
                            break;
                        }
                    case 15:
                        if (zzP(obj, i2)) {
                            int zzc = zzeq.zzc(obj, j);
                            zzx3 = zzbi.zzx(i3 << 3);
                            zzy = zzbi.zzx((zzc >> 31) ^ (zzc + zzc));
                            i += zzx3 + zzy;
                            break;
                        } else {
                            break;
                        }
                    case 16:
                        if (zzP(obj, i2)) {
                            long zzd3 = zzeq.zzd(obj, j);
                            zzx7 = zzbi.zzx(i3 << 3);
                            zzy2 = zzbi.zzy((zzd3 >> 63) ^ (zzd3 + zzd3));
                            zzn = zzx7 + zzy2;
                            i += zzn;
                            break;
                        } else {
                            break;
                        }
                    case 17:
                        if (zzP(obj, i2)) {
                            zzn = zzbi.zzt(i3, (zzdf) zzeq.zzf(obj, j), zzB(i2));
                            i += zzn;
                            break;
                        } else {
                            break;
                        }
                    case 18:
                        zzn = zzdr.zzg(i3, (List) zzeq.zzf(obj, j), false);
                        i += zzn;
                        break;
                    case 19:
                        zzn = zzdr.zze(i3, (List) zzeq.zzf(obj, j), false);
                        i += zzn;
                        break;
                    case 20:
                        zzn = zzdr.zzl(i3, (List) zzeq.zzf(obj, j), false);
                        i += zzn;
                        break;
                    case 21:
                        zzn = zzdr.zzw(i3, (List) zzeq.zzf(obj, j), false);
                        i += zzn;
                        break;
                    case 22:
                        zzn = zzdr.zzj(i3, (List) zzeq.zzf(obj, j), false);
                        i += zzn;
                        break;
                    case 23:
                        zzn = zzdr.zzg(i3, (List) zzeq.zzf(obj, j), false);
                        i += zzn;
                        break;
                    case 24:
                        zzn = zzdr.zze(i3, (List) zzeq.zzf(obj, j), false);
                        i += zzn;
                        break;
                    case 25:
                        zzn = zzdr.zza(i3, (List) zzeq.zzf(obj, j), false);
                        i += zzn;
                        break;
                    case 26:
                        zzn = zzdr.zzt(i3, (List) zzeq.zzf(obj, j));
                        i += zzn;
                        break;
                    case 27:
                        zzn = zzdr.zzo(i3, (List) zzeq.zzf(obj, j), zzB(i2));
                        i += zzn;
                        break;
                    case 28:
                        zzn = zzdr.zzb(i3, (List) zzeq.zzf(obj, j));
                        i += zzn;
                        break;
                    case 29:
                        zzn = zzdr.zzu(i3, (List) zzeq.zzf(obj, j), false);
                        i += zzn;
                        break;
                    case 30:
                        zzn = zzdr.zzc(i3, (List) zzeq.zzf(obj, j), false);
                        i += zzn;
                        break;
                    case R.styleable.AppCompatTheme_actionModeWebSearchDrawable /* 31 */:
                        zzn = zzdr.zze(i3, (List) zzeq.zzf(obj, j), false);
                        i += zzn;
                        break;
                    case 32:
                        zzn = zzdr.zzg(i3, (List) zzeq.zzf(obj, j), false);
                        i += zzn;
                        break;
                    case R.styleable.AppCompatTheme_actionOverflowMenuStyle /* 33 */:
                        zzn = zzdr.zzp(i3, (List) zzeq.zzf(obj, j), false);
                        i += zzn;
                        break;
                    case R.styleable.AppCompatTheme_activityChooserViewStyle /* 34 */:
                        zzn = zzdr.zzr(i3, (List) zzeq.zzf(obj, j), false);
                        i += zzn;
                        break;
                    case R.styleable.AppCompatTheme_alertDialogButtonGroupStyle /* 35 */:
                        zzy = zzdr.zzh((List) unsafe.getObject(obj, j));
                        if (zzy > 0) {
                            int i10 = i3 << 3;
                            zzx8 = zzbi.zzx(zzy);
                            zzx9 = zzbi.zzx(i10);
                            zzx3 = zzx9 + zzx8;
                            i += zzx3 + zzy;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_alertDialogCenterButtons /* 36 */:
                        zzy = zzdr.zzf((List) unsafe.getObject(obj, j));
                        if (zzy > 0) {
                            int i11 = i3 << 3;
                            zzx8 = zzbi.zzx(zzy);
                            zzx9 = zzbi.zzx(i11);
                            zzx3 = zzx9 + zzx8;
                            i += zzx3 + zzy;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_alertDialogStyle /* 37 */:
                        zzy = zzdr.zzm((List) unsafe.getObject(obj, j));
                        if (zzy > 0) {
                            int i12 = i3 << 3;
                            zzx8 = zzbi.zzx(zzy);
                            zzx9 = zzbi.zzx(i12);
                            zzx3 = zzx9 + zzx8;
                            i += zzx3 + zzy;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_alertDialogTheme /* 38 */:
                        zzy = zzdr.zzx((List) unsafe.getObject(obj, j));
                        if (zzy > 0) {
                            int i13 = i3 << 3;
                            zzx8 = zzbi.zzx(zzy);
                            zzx9 = zzbi.zzx(i13);
                            zzx3 = zzx9 + zzx8;
                            i += zzx3 + zzy;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_autoCompleteTextViewStyle /* 39 */:
                        zzy = zzdr.zzk((List) unsafe.getObject(obj, j));
                        if (zzy > 0) {
                            int i14 = i3 << 3;
                            zzx8 = zzbi.zzx(zzy);
                            zzx9 = zzbi.zzx(i14);
                            zzx3 = zzx9 + zzx8;
                            i += zzx3 + zzy;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_borderlessButtonStyle /* 40 */:
                        zzy = zzdr.zzh((List) unsafe.getObject(obj, j));
                        if (zzy > 0) {
                            int i15 = i3 << 3;
                            zzx8 = zzbi.zzx(zzy);
                            zzx9 = zzbi.zzx(i15);
                            zzx3 = zzx9 + zzx8;
                            i += zzx3 + zzy;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_buttonBarButtonStyle /* 41 */:
                        zzy = zzdr.zzf((List) unsafe.getObject(obj, j));
                        if (zzy > 0) {
                            int i16 = i3 << 3;
                            zzx8 = zzbi.zzx(zzy);
                            zzx9 = zzbi.zzx(i16);
                            zzx3 = zzx9 + zzx8;
                            i += zzx3 + zzy;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_buttonBarNegativeButtonStyle /* 42 */:
                        int i17 = zzdr.zza;
                        zzy = ((List) unsafe.getObject(obj, j)).size();
                        if (zzy > 0) {
                            int i18 = i3 << 3;
                            zzx8 = zzbi.zzx(zzy);
                            zzx9 = zzbi.zzx(i18);
                            zzx3 = zzx9 + zzx8;
                            i += zzx3 + zzy;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_buttonBarNeutralButtonStyle /* 43 */:
                        zzy = zzdr.zzv((List) unsafe.getObject(obj, j));
                        if (zzy > 0) {
                            int i19 = i3 << 3;
                            zzx8 = zzbi.zzx(zzy);
                            zzx9 = zzbi.zzx(i19);
                            zzx3 = zzx9 + zzx8;
                            i += zzx3 + zzy;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_buttonBarPositiveButtonStyle /* 44 */:
                        zzy = zzdr.zzd((List) unsafe.getObject(obj, j));
                        if (zzy > 0) {
                            int i20 = i3 << 3;
                            zzx8 = zzbi.zzx(zzy);
                            zzx9 = zzbi.zzx(i20);
                            zzx3 = zzx9 + zzx8;
                            i += zzx3 + zzy;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_buttonBarStyle /* 45 */:
                        zzy = zzdr.zzf((List) unsafe.getObject(obj, j));
                        if (zzy > 0) {
                            int i21 = i3 << 3;
                            zzx8 = zzbi.zzx(zzy);
                            zzx9 = zzbi.zzx(i21);
                            zzx3 = zzx9 + zzx8;
                            i += zzx3 + zzy;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_buttonStyle /* 46 */:
                        zzy = zzdr.zzh((List) unsafe.getObject(obj, j));
                        if (zzy > 0) {
                            int i22 = i3 << 3;
                            zzx8 = zzbi.zzx(zzy);
                            zzx9 = zzbi.zzx(i22);
                            zzx3 = zzx9 + zzx8;
                            i += zzx3 + zzy;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_buttonStyleSmall /* 47 */:
                        zzy = zzdr.zzq((List) unsafe.getObject(obj, j));
                        if (zzy > 0) {
                            int i23 = i3 << 3;
                            zzx8 = zzbi.zzx(zzy);
                            zzx9 = zzbi.zzx(i23);
                            zzx3 = zzx9 + zzx8;
                            i += zzx3 + zzy;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_checkboxStyle /* 48 */:
                        zzy = zzdr.zzs((List) unsafe.getObject(obj, j));
                        if (zzy > 0) {
                            int i24 = i3 << 3;
                            zzx8 = zzbi.zzx(zzy);
                            zzx9 = zzbi.zzx(i24);
                            zzx3 = zzx9 + zzx8;
                            i += zzx3 + zzy;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_checkedTextViewStyle /* 49 */:
                        zzn = zzdr.zzi(i3, (List) zzeq.zzf(obj, j), zzB(i2));
                        i += zzn;
                        break;
                    case R.styleable.AppCompatTheme_colorAccent /* 50 */:
                        zzda.zza(i3, zzeq.zzf(obj, j), zzC(i2));
                        break;
                    case R.styleable.AppCompatTheme_colorBackgroundFloating /* 51 */:
                        if (zzT(obj, i3, i2)) {
                            zzx = zzbi.zzx(i3 << 3);
                            zzn = zzx + 8;
                            i += zzn;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_colorButtonNormal /* 52 */:
                        if (zzT(obj, i3, i2)) {
                            zzx2 = zzbi.zzx(i3 << 3);
                            zzn = zzx2 + 4;
                            i += zzn;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_colorControlActivated /* 53 */:
                        if (zzT(obj, i3, i2)) {
                            zzy = zzbi.zzy(zzz(obj, j));
                            zzx3 = zzbi.zzx(i3 << 3);
                            i += zzx3 + zzy;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_colorControlHighlight /* 54 */:
                        if (zzT(obj, i3, i2)) {
                            zzy = zzbi.zzy(zzz(obj, j));
                            zzx3 = zzbi.zzx(i3 << 3);
                            i += zzx3 + zzy;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_colorControlNormal /* 55 */:
                        if (zzT(obj, i3, i2)) {
                            zzy = zzbi.zzu(zzp(obj, j));
                            zzx3 = zzbi.zzx(i3 << 3);
                            i += zzx3 + zzy;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_colorError /* 56 */:
                        if (zzT(obj, i3, i2)) {
                            zzx = zzbi.zzx(i3 << 3);
                            zzn = zzx + 8;
                            i += zzn;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_colorPrimary /* 57 */:
                        if (zzT(obj, i3, i2)) {
                            zzx2 = zzbi.zzx(i3 << 3);
                            zzn = zzx2 + 4;
                            i += zzn;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_colorPrimaryDark /* 58 */:
                        if (zzT(obj, i3, i2)) {
                            zzx4 = zzbi.zzx(i3 << 3);
                            zzn = zzx4 + 1;
                            i += zzn;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_colorSwitchThumbNormal /* 59 */:
                        if (zzT(obj, i3, i2)) {
                            Object zzf2 = zzeq.zzf(obj, j);
                            if (zzf2 instanceof zzba) {
                                int i25 = i3 << 3;
                                int i26 = zzbi.zzb;
                                int zzd4 = ((zzba) zzf2).zzd();
                                zzx5 = zzbi.zzx(zzd4) + zzd4;
                                zzx6 = zzbi.zzx(i25);
                                zzn = zzx6 + zzx5;
                                i += zzn;
                                break;
                            } else {
                                zzy = zzbi.zzw((String) zzf2);
                                zzx3 = zzbi.zzx(i3 << 3);
                                i += zzx3 + zzy;
                                break;
                            }
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_controlBackground /* 60 */:
                        if (zzT(obj, i3, i2)) {
                            zzn = zzdr.zzn(i3, zzeq.zzf(obj, j), zzB(i2));
                            i += zzn;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_dialogCornerRadius /* 61 */:
                        if (zzT(obj, i3, i2)) {
                            int i27 = i3 << 3;
                            int i28 = zzbi.zzb;
                            int zzd5 = ((zzba) zzeq.zzf(obj, j)).zzd();
                            zzx5 = zzbi.zzx(zzd5) + zzd5;
                            zzx6 = zzbi.zzx(i27);
                            zzn = zzx6 + zzx5;
                            i += zzn;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_dialogPreferredPadding /* 62 */:
                        if (zzT(obj, i3, i2)) {
                            zzy = zzbi.zzx(zzp(obj, j));
                            zzx3 = zzbi.zzx(i3 << 3);
                            i += zzx3 + zzy;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_dialogTheme /* 63 */:
                        if (zzT(obj, i3, i2)) {
                            zzy = zzbi.zzu(zzp(obj, j));
                            zzx3 = zzbi.zzx(i3 << 3);
                            i += zzx3 + zzy;
                            break;
                        } else {
                            break;
                        }
                    case 64:
                        if (zzT(obj, i3, i2)) {
                            zzx2 = zzbi.zzx(i3 << 3);
                            zzn = zzx2 + 4;
                            i += zzn;
                            break;
                        } else {
                            break;
                        }
                    case 65:
                        if (zzT(obj, i3, i2)) {
                            zzx = zzbi.zzx(i3 << 3);
                            zzn = zzx + 8;
                            i += zzn;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_dropDownListViewStyle /* 66 */:
                        if (zzT(obj, i3, i2)) {
                            int zzp = zzp(obj, j);
                            zzx3 = zzbi.zzx(i3 << 3);
                            zzy = zzbi.zzx((zzp >> 31) ^ (zzp + zzp));
                            i += zzx3 + zzy;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_dropdownListPreferredItemHeight /* 67 */:
                        if (zzT(obj, i3, i2)) {
                            long zzz = zzz(obj, j);
                            zzx7 = zzbi.zzx(i3 << 3);
                            zzy2 = zzbi.zzy((zzz >> 63) ^ (zzz + zzz));
                            zzn = zzx7 + zzy2;
                            i += zzn;
                            break;
                        } else {
                            break;
                        }
                    case R.styleable.AppCompatTheme_editTextBackground /* 68 */:
                        if (zzT(obj, i3, i2)) {
                            zzn = zzbi.zzt(i3, (zzdf) zzeq.zzf(obj, j), zzB(i2));
                            i += zzn;
                            break;
                        } else {
                            break;
                        }
                }
            }
            zzeg zzegVar = this.zzm;
            return i + zzegVar.zza(zzegVar.zzd(obj));
        }
        return zzo(obj);
    }

    @Override // com.google.android.gms.internal.play_billing.zzdp
    public final int zzb(Object obj) {
        int i;
        long doubleToLongBits;
        int i2;
        int floatToIntBits;
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
                    doubleToLongBits = Double.doubleToLongBits(zzeq.zza(obj, j));
                    byte[] bArr = zzcg.zzd;
                    i3 = i + ((int) (doubleToLongBits ^ (doubleToLongBits >>> 32)));
                    break;
                case 1:
                    i2 = i3 * 53;
                    floatToIntBits = Float.floatToIntBits(zzeq.zzb(obj, j));
                    i3 = i2 + floatToIntBits;
                    break;
                case 2:
                    i = i3 * 53;
                    doubleToLongBits = zzeq.zzd(obj, j);
                    byte[] bArr2 = zzcg.zzd;
                    i3 = i + ((int) (doubleToLongBits ^ (doubleToLongBits >>> 32)));
                    break;
                case 3:
                    i = i3 * 53;
                    doubleToLongBits = zzeq.zzd(obj, j);
                    byte[] bArr3 = zzcg.zzd;
                    i3 = i + ((int) (doubleToLongBits ^ (doubleToLongBits >>> 32)));
                    break;
                case 4:
                    i2 = i3 * 53;
                    floatToIntBits = zzeq.zzc(obj, j);
                    i3 = i2 + floatToIntBits;
                    break;
                case 5:
                    i = i3 * 53;
                    doubleToLongBits = zzeq.zzd(obj, j);
                    byte[] bArr4 = zzcg.zzd;
                    i3 = i + ((int) (doubleToLongBits ^ (doubleToLongBits >>> 32)));
                    break;
                case 6:
                    i2 = i3 * 53;
                    floatToIntBits = zzeq.zzc(obj, j);
                    i3 = i2 + floatToIntBits;
                    break;
                case 7:
                    i2 = i3 * 53;
                    floatToIntBits = zzcg.zza(zzeq.zzw(obj, j));
                    i3 = i2 + floatToIntBits;
                    break;
                case 8:
                    i2 = i3 * 53;
                    floatToIntBits = ((String) zzeq.zzf(obj, j)).hashCode();
                    i3 = i2 + floatToIntBits;
                    break;
                case 9:
                    Object zzf = zzeq.zzf(obj, j);
                    if (zzf != null) {
                        i6 = zzf.hashCode();
                    }
                    i3 = (i3 * 53) + i6;
                    break;
                case 10:
                    i2 = i3 * 53;
                    floatToIntBits = zzeq.zzf(obj, j).hashCode();
                    i3 = i2 + floatToIntBits;
                    break;
                case 11:
                    i2 = i3 * 53;
                    floatToIntBits = zzeq.zzc(obj, j);
                    i3 = i2 + floatToIntBits;
                    break;
                case 12:
                    i2 = i3 * 53;
                    floatToIntBits = zzeq.zzc(obj, j);
                    i3 = i2 + floatToIntBits;
                    break;
                case 13:
                    i2 = i3 * 53;
                    floatToIntBits = zzeq.zzc(obj, j);
                    i3 = i2 + floatToIntBits;
                    break;
                case 14:
                    i = i3 * 53;
                    doubleToLongBits = zzeq.zzd(obj, j);
                    byte[] bArr5 = zzcg.zzd;
                    i3 = i + ((int) (doubleToLongBits ^ (doubleToLongBits >>> 32)));
                    break;
                case 15:
                    i2 = i3 * 53;
                    floatToIntBits = zzeq.zzc(obj, j);
                    i3 = i2 + floatToIntBits;
                    break;
                case 16:
                    i = i3 * 53;
                    doubleToLongBits = zzeq.zzd(obj, j);
                    byte[] bArr6 = zzcg.zzd;
                    i3 = i + ((int) (doubleToLongBits ^ (doubleToLongBits >>> 32)));
                    break;
                case 17:
                    Object zzf2 = zzeq.zzf(obj, j);
                    if (zzf2 != null) {
                        i6 = zzf2.hashCode();
                    }
                    i3 = (i3 * 53) + i6;
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
                case R.styleable.AppCompatTheme_actionModeWebSearchDrawable /* 31 */:
                case 32:
                case R.styleable.AppCompatTheme_actionOverflowMenuStyle /* 33 */:
                case R.styleable.AppCompatTheme_activityChooserViewStyle /* 34 */:
                case R.styleable.AppCompatTheme_alertDialogButtonGroupStyle /* 35 */:
                case R.styleable.AppCompatTheme_alertDialogCenterButtons /* 36 */:
                case R.styleable.AppCompatTheme_alertDialogStyle /* 37 */:
                case R.styleable.AppCompatTheme_alertDialogTheme /* 38 */:
                case R.styleable.AppCompatTheme_autoCompleteTextViewStyle /* 39 */:
                case R.styleable.AppCompatTheme_borderlessButtonStyle /* 40 */:
                case R.styleable.AppCompatTheme_buttonBarButtonStyle /* 41 */:
                case R.styleable.AppCompatTheme_buttonBarNegativeButtonStyle /* 42 */:
                case R.styleable.AppCompatTheme_buttonBarNeutralButtonStyle /* 43 */:
                case R.styleable.AppCompatTheme_buttonBarPositiveButtonStyle /* 44 */:
                case R.styleable.AppCompatTheme_buttonBarStyle /* 45 */:
                case R.styleable.AppCompatTheme_buttonStyle /* 46 */:
                case R.styleable.AppCompatTheme_buttonStyleSmall /* 47 */:
                case R.styleable.AppCompatTheme_checkboxStyle /* 48 */:
                case R.styleable.AppCompatTheme_checkedTextViewStyle /* 49 */:
                    i2 = i3 * 53;
                    floatToIntBits = zzeq.zzf(obj, j).hashCode();
                    i3 = i2 + floatToIntBits;
                    break;
                case R.styleable.AppCompatTheme_colorAccent /* 50 */:
                    i2 = i3 * 53;
                    floatToIntBits = zzeq.zzf(obj, j).hashCode();
                    i3 = i2 + floatToIntBits;
                    break;
                case R.styleable.AppCompatTheme_colorBackgroundFloating /* 51 */:
                    if (zzT(obj, i5, i4)) {
                        i = i3 * 53;
                        doubleToLongBits = Double.doubleToLongBits(zzm(obj, j));
                        byte[] bArr7 = zzcg.zzd;
                        i3 = i + ((int) (doubleToLongBits ^ (doubleToLongBits >>> 32)));
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_colorButtonNormal /* 52 */:
                    if (zzT(obj, i5, i4)) {
                        i2 = i3 * 53;
                        floatToIntBits = Float.floatToIntBits(zzn(obj, j));
                        i3 = i2 + floatToIntBits;
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_colorControlActivated /* 53 */:
                    if (zzT(obj, i5, i4)) {
                        i = i3 * 53;
                        doubleToLongBits = zzz(obj, j);
                        byte[] bArr8 = zzcg.zzd;
                        i3 = i + ((int) (doubleToLongBits ^ (doubleToLongBits >>> 32)));
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_colorControlHighlight /* 54 */:
                    if (zzT(obj, i5, i4)) {
                        i = i3 * 53;
                        doubleToLongBits = zzz(obj, j);
                        byte[] bArr9 = zzcg.zzd;
                        i3 = i + ((int) (doubleToLongBits ^ (doubleToLongBits >>> 32)));
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_colorControlNormal /* 55 */:
                    if (zzT(obj, i5, i4)) {
                        i2 = i3 * 53;
                        floatToIntBits = zzp(obj, j);
                        i3 = i2 + floatToIntBits;
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_colorError /* 56 */:
                    if (zzT(obj, i5, i4)) {
                        i = i3 * 53;
                        doubleToLongBits = zzz(obj, j);
                        byte[] bArr10 = zzcg.zzd;
                        i3 = i + ((int) (doubleToLongBits ^ (doubleToLongBits >>> 32)));
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_colorPrimary /* 57 */:
                    if (zzT(obj, i5, i4)) {
                        i2 = i3 * 53;
                        floatToIntBits = zzp(obj, j);
                        i3 = i2 + floatToIntBits;
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_colorPrimaryDark /* 58 */:
                    if (zzT(obj, i5, i4)) {
                        i2 = i3 * 53;
                        floatToIntBits = zzcg.zza(zzU(obj, j));
                        i3 = i2 + floatToIntBits;
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_colorSwitchThumbNormal /* 59 */:
                    if (zzT(obj, i5, i4)) {
                        i2 = i3 * 53;
                        floatToIntBits = ((String) zzeq.zzf(obj, j)).hashCode();
                        i3 = i2 + floatToIntBits;
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_controlBackground /* 60 */:
                    if (zzT(obj, i5, i4)) {
                        i2 = i3 * 53;
                        floatToIntBits = zzeq.zzf(obj, j).hashCode();
                        i3 = i2 + floatToIntBits;
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_dialogCornerRadius /* 61 */:
                    if (zzT(obj, i5, i4)) {
                        i2 = i3 * 53;
                        floatToIntBits = zzeq.zzf(obj, j).hashCode();
                        i3 = i2 + floatToIntBits;
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_dialogPreferredPadding /* 62 */:
                    if (zzT(obj, i5, i4)) {
                        i2 = i3 * 53;
                        floatToIntBits = zzp(obj, j);
                        i3 = i2 + floatToIntBits;
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_dialogTheme /* 63 */:
                    if (zzT(obj, i5, i4)) {
                        i2 = i3 * 53;
                        floatToIntBits = zzp(obj, j);
                        i3 = i2 + floatToIntBits;
                        break;
                    } else {
                        break;
                    }
                case 64:
                    if (zzT(obj, i5, i4)) {
                        i2 = i3 * 53;
                        floatToIntBits = zzp(obj, j);
                        i3 = i2 + floatToIntBits;
                        break;
                    } else {
                        break;
                    }
                case 65:
                    if (zzT(obj, i5, i4)) {
                        i = i3 * 53;
                        doubleToLongBits = zzz(obj, j);
                        byte[] bArr11 = zzcg.zzd;
                        i3 = i + ((int) (doubleToLongBits ^ (doubleToLongBits >>> 32)));
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_dropDownListViewStyle /* 66 */:
                    if (zzT(obj, i5, i4)) {
                        i2 = i3 * 53;
                        floatToIntBits = zzp(obj, j);
                        i3 = i2 + floatToIntBits;
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_dropdownListPreferredItemHeight /* 67 */:
                    if (zzT(obj, i5, i4)) {
                        i = i3 * 53;
                        doubleToLongBits = zzz(obj, j);
                        byte[] bArr12 = zzcg.zzd;
                        i3 = i + ((int) (doubleToLongBits ^ (doubleToLongBits >>> 32)));
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_editTextBackground /* 68 */:
                    if (zzT(obj, i5, i4)) {
                        i2 = i3 * 53;
                        floatToIntBits = zzeq.zzf(obj, j).hashCode();
                        i3 = i2 + floatToIntBits;
                        break;
                    } else {
                        break;
                    }
            }
        }
        int hashCode = (i3 * 53) + this.zzm.zzd(obj).hashCode();
        if (this.zzh) {
            this.zzn.zza(obj);
            throw null;
        }
        return hashCode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:141:0x0401, code lost:
        if (r6 == 1048575) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:142:0x0403, code lost:
        r27.putInt(r12, r6, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:143:0x0409, code lost:
        r2 = r8.zzj;
     */
    /* JADX WARN: Code restructure failed: missing block: B:145:0x040d, code lost:
        if (r2 >= r8.zzk) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:146:0x040f, code lost:
        r4 = r8.zzi[r2];
        r5 = r8.zzc[r4];
        r5 = com.google.android.gms.internal.play_billing.zzeq.zzf(r12, r8.zzy(r4) & 1048575);
     */
    /* JADX WARN: Code restructure failed: missing block: B:147:0x0421, code lost:
        if (r5 != null) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:150:0x0428, code lost:
        if (r8.zzA(r4) != null) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:151:0x042a, code lost:
        r2 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:152:0x042d, code lost:
        r5 = (com.google.android.gms.internal.play_billing.zzcz) r5;
        r0 = (com.google.android.gms.internal.play_billing.zzcy) r8.zzC(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:153:0x0435, code lost:
        throw null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:154:0x0436, code lost:
        if (r9 != 0) goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:156:0x043a, code lost:
        if (r0 != r32) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:159:0x0441, code lost:
        throw com.google.android.gms.internal.play_billing.zzci.zze();
     */
    /* JADX WARN: Code restructure failed: missing block: B:161:0x0444, code lost:
        if (r0 > r32) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:162:0x0446, code lost:
        if (r3 != r9) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:163:0x0448, code lost:
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:165:0x044e, code lost:
        throw com.google.android.gms.internal.play_billing.zzci.zze();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final int zzc(Object obj, byte[] bArr, int i, int i2, int i3, zzan zzanVar) throws IOException {
        Unsafe unsafe;
        int i4;
        Object obj2;
        zzdi<T> zzdiVar;
        byte b;
        int zzt;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        Object obj3;
        int i11;
        zzan zzanVar2;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        int i18;
        int i19;
        int i20;
        int i21;
        int i22;
        int i23;
        zzdi<T> zzdiVar2 = this;
        Object obj4 = obj;
        byte[] bArr2 = bArr;
        int i24 = i2;
        int i25 = i3;
        zzan zzanVar3 = zzanVar;
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
                byte b2 = bArr2[i27];
                if (b2 < 0) {
                    int zzk = zzao.zzk(b2, bArr2, i33, zzanVar3);
                    b = zzanVar3.zza;
                    i33 = zzk;
                } else {
                    b = b2;
                }
                int i34 = b >>> 3;
                if (i34 > i28) {
                    zzt = zzdiVar2.zzu(i34, i29 / 3);
                } else {
                    zzt = zzdiVar2.zzt(i34);
                }
                int i35 = zzt;
                if (i35 == i26) {
                    i5 = i34;
                    i6 = i33;
                    i7 = b;
                    i8 = i31;
                    unsafe = unsafe2;
                    i9 = i25;
                    i10 = 0;
                } else {
                    int i36 = b & 7;
                    int[] iArr = zzdiVar2.zzc;
                    int i37 = iArr[i35 + 1];
                    int zzx = zzx(i37);
                    int i38 = i33;
                    int i39 = b;
                    long j = i37 & 1048575;
                    if (zzx <= 17) {
                        int i40 = iArr[i35 + 2];
                        int i41 = 1 << (i40 >>> 20);
                        int i42 = i40 & 1048575;
                        if (i42 != i32) {
                            if (i32 != 1048575) {
                                unsafe2.putInt(obj4, i32, i31);
                            }
                            i13 = i42;
                            i12 = unsafe2.getInt(obj4, i42);
                        } else {
                            i12 = i31;
                            i13 = i32;
                        }
                        switch (zzx) {
                            case 0:
                                i14 = i35;
                                i15 = i34;
                                i16 = i38;
                                if (i36 != 1) {
                                    i21 = i14;
                                    i9 = i3;
                                    i8 = i12;
                                    unsafe = unsafe2;
                                    i5 = i15;
                                    i10 = i21;
                                    i6 = i16;
                                    i7 = i39;
                                    i32 = i13;
                                    break;
                                } else {
                                    zzeq.zzo(obj4, j, Double.longBitsToDouble(zzao.zzp(bArr2, i16)));
                                    i27 = i16 + 8;
                                    i31 = i12 | i41;
                                    i24 = i2;
                                    i29 = i14;
                                    i28 = i15;
                                    i30 = i39;
                                    i32 = i13;
                                    i26 = -1;
                                    i25 = i3;
                                    break;
                                }
                            case 1:
                                i14 = i35;
                                i15 = i34;
                                i16 = i38;
                                if (i36 != 5) {
                                    i21 = i14;
                                    i9 = i3;
                                    i8 = i12;
                                    unsafe = unsafe2;
                                    i5 = i15;
                                    i10 = i21;
                                    i6 = i16;
                                    i7 = i39;
                                    i32 = i13;
                                    break;
                                } else {
                                    zzeq.zzp(obj4, j, Float.intBitsToFloat(zzao.zzb(bArr2, i16)));
                                    i27 = i16 + 4;
                                    i31 = i12 | i41;
                                    i24 = i2;
                                    i29 = i14;
                                    i28 = i15;
                                    i30 = i39;
                                    i32 = i13;
                                    i26 = -1;
                                    i25 = i3;
                                    break;
                                }
                            case 2:
                            case 3:
                                i14 = i35;
                                i15 = i34;
                                i16 = i38;
                                if (i36 != 0) {
                                    i21 = i14;
                                    i9 = i3;
                                    i8 = i12;
                                    unsafe = unsafe2;
                                    i5 = i15;
                                    i10 = i21;
                                    i6 = i16;
                                    i7 = i39;
                                    i32 = i13;
                                    break;
                                } else {
                                    int zzm = zzao.zzm(bArr2, i16, zzanVar3);
                                    unsafe2.putLong(obj, j, zzanVar3.zzb);
                                    i31 = i12 | i41;
                                    i29 = i14;
                                    i28 = i15;
                                    i27 = zzm;
                                    i30 = i39;
                                    i32 = i13;
                                    i26 = -1;
                                    i24 = i2;
                                    i25 = i3;
                                    break;
                                }
                            case 4:
                            case 11:
                                i14 = i35;
                                i15 = i34;
                                i16 = i38;
                                if (i36 != 0) {
                                    i21 = i14;
                                    i9 = i3;
                                    i8 = i12;
                                    unsafe = unsafe2;
                                    i5 = i15;
                                    i10 = i21;
                                    i6 = i16;
                                    i7 = i39;
                                    i32 = i13;
                                    break;
                                } else {
                                    i27 = zzao.zzj(bArr2, i16, zzanVar3);
                                    unsafe2.putInt(obj4, j, zzanVar3.zza);
                                    i31 = i12 | i41;
                                    i24 = i2;
                                    i29 = i14;
                                    i28 = i15;
                                    i30 = i39;
                                    i32 = i13;
                                    i26 = -1;
                                    i25 = i3;
                                    break;
                                }
                            case 5:
                            case 14:
                                i14 = i35;
                                i15 = i34;
                                i17 = i38;
                                if (i36 != 1) {
                                    i16 = i17;
                                    i21 = i14;
                                    i9 = i3;
                                    i8 = i12;
                                    unsafe = unsafe2;
                                    i5 = i15;
                                    i10 = i21;
                                    i6 = i16;
                                    i7 = i39;
                                    i32 = i13;
                                    break;
                                } else {
                                    i16 = i17;
                                    unsafe2.putLong(obj, j, zzao.zzp(bArr2, i17));
                                    i27 = i16 + 8;
                                    i31 = i12 | i41;
                                    i24 = i2;
                                    i29 = i14;
                                    i28 = i15;
                                    i30 = i39;
                                    i32 = i13;
                                    i26 = -1;
                                    i25 = i3;
                                    break;
                                }
                            case 6:
                            case 13:
                                i14 = i35;
                                i15 = i34;
                                i17 = i38;
                                if (i36 != 5) {
                                    i16 = i17;
                                    i21 = i14;
                                    i9 = i3;
                                    i8 = i12;
                                    unsafe = unsafe2;
                                    i5 = i15;
                                    i10 = i21;
                                    i6 = i16;
                                    i7 = i39;
                                    i32 = i13;
                                    break;
                                } else {
                                    unsafe2.putInt(obj4, j, zzao.zzb(bArr2, i17));
                                    i27 = i17 + 4;
                                    i31 = i12 | i41;
                                    i24 = i2;
                                    i29 = i14;
                                    i28 = i15;
                                    i30 = i39;
                                    i32 = i13;
                                    i26 = -1;
                                    i25 = i3;
                                    break;
                                }
                            case 7:
                                i14 = i35;
                                i15 = i34;
                                i17 = i38;
                                if (i36 != 0) {
                                    i16 = i17;
                                    i21 = i14;
                                    i9 = i3;
                                    i8 = i12;
                                    unsafe = unsafe2;
                                    i5 = i15;
                                    i10 = i21;
                                    i6 = i16;
                                    i7 = i39;
                                    i32 = i13;
                                    break;
                                } else {
                                    i27 = zzao.zzm(bArr2, i17, zzanVar3);
                                    zzeq.zzm(obj4, j, zzanVar3.zzb != 0);
                                    i31 = i12 | i41;
                                    i24 = i2;
                                    i29 = i14;
                                    i28 = i15;
                                    i30 = i39;
                                    i32 = i13;
                                    i26 = -1;
                                    i25 = i3;
                                    break;
                                }
                            case 8:
                                i14 = i35;
                                i15 = i34;
                                i17 = i38;
                                if (i36 != 2) {
                                    i16 = i17;
                                    i21 = i14;
                                    i9 = i3;
                                    i8 = i12;
                                    unsafe = unsafe2;
                                    i5 = i15;
                                    i10 = i21;
                                    i6 = i16;
                                    i7 = i39;
                                    i32 = i13;
                                    break;
                                } else {
                                    if ((i37 & 536870912) == 0) {
                                        i27 = zzao.zzg(bArr2, i17, zzanVar3);
                                    } else {
                                        i27 = zzao.zzh(bArr2, i17, zzanVar3);
                                    }
                                    unsafe2.putObject(obj4, j, zzanVar3.zzc);
                                    i31 = i12 | i41;
                                    i24 = i2;
                                    i29 = i14;
                                    i28 = i15;
                                    i30 = i39;
                                    i32 = i13;
                                    i26 = -1;
                                    i25 = i3;
                                    break;
                                }
                            case 9:
                                i14 = i35;
                                i15 = i34;
                                i17 = i38;
                                if (i36 != 2) {
                                    i39 = i39;
                                    i16 = i17;
                                    i21 = i14;
                                    i9 = i3;
                                    i8 = i12;
                                    unsafe = unsafe2;
                                    i5 = i15;
                                    i10 = i21;
                                    i6 = i16;
                                    i7 = i39;
                                    i32 = i13;
                                    break;
                                } else {
                                    Object zzD = zzdiVar2.zzD(obj4, i14);
                                    i39 = i39;
                                    i27 = zzao.zzo(zzD, zzdiVar2.zzB(i14), bArr, i17, i2, zzanVar);
                                    zzdiVar2.zzL(obj4, i14, zzD);
                                    i31 = i12 | i41;
                                    i24 = i2;
                                    i29 = i14;
                                    i28 = i15;
                                    i30 = i39;
                                    i32 = i13;
                                    i26 = -1;
                                    i25 = i3;
                                    break;
                                }
                            case 10:
                                i18 = i35;
                                i15 = i34;
                                i19 = i39;
                                i20 = i38;
                                if (i36 != 2) {
                                    i21 = i18;
                                    i39 = i19;
                                    i16 = i20;
                                    i9 = i3;
                                    i8 = i12;
                                    unsafe = unsafe2;
                                    i5 = i15;
                                    i10 = i21;
                                    i6 = i16;
                                    i7 = i39;
                                    i32 = i13;
                                    break;
                                } else {
                                    i27 = zzao.zza(bArr2, i20, zzanVar3);
                                    unsafe2.putObject(obj4, j, zzanVar3.zzc);
                                    i31 = i12 | i41;
                                    i29 = i18;
                                    i28 = i15;
                                    i30 = i19;
                                    i32 = i13;
                                    i26 = -1;
                                    i24 = i2;
                                    i25 = i3;
                                    break;
                                }
                            case 12:
                                i18 = i35;
                                i15 = i34;
                                i19 = i39;
                                i20 = i38;
                                if (i36 != 0) {
                                    i21 = i18;
                                    i39 = i19;
                                    i16 = i20;
                                    i9 = i3;
                                    i8 = i12;
                                    unsafe = unsafe2;
                                    i5 = i15;
                                    i10 = i21;
                                    i6 = i16;
                                    i7 = i39;
                                    i32 = i13;
                                    break;
                                } else {
                                    i27 = zzao.zzj(bArr2, i20, zzanVar3);
                                    int i43 = zzanVar3.zza;
                                    zzce zzA = zzdiVar2.zzA(i18);
                                    if (zzA == null || zzA.zza(i43)) {
                                        unsafe2.putInt(obj4, j, i43);
                                        i31 = i12 | i41;
                                        i29 = i18;
                                        i28 = i15;
                                        i30 = i19;
                                        i32 = i13;
                                        i26 = -1;
                                        i24 = i2;
                                        i25 = i3;
                                        break;
                                    } else {
                                        zzd(obj).zzj(i19, Long.valueOf(i43));
                                        i29 = i18;
                                        i31 = i12;
                                        i28 = i15;
                                        i30 = i19;
                                        i32 = i13;
                                        i26 = -1;
                                        i24 = i2;
                                        i25 = i3;
                                    }
                                }
                                break;
                            case 15:
                                i18 = i35;
                                i15 = i34;
                                i19 = i39;
                                i20 = i38;
                                if (i36 != 0) {
                                    i21 = i18;
                                    i39 = i19;
                                    i16 = i20;
                                    i9 = i3;
                                    i8 = i12;
                                    unsafe = unsafe2;
                                    i5 = i15;
                                    i10 = i21;
                                    i6 = i16;
                                    i7 = i39;
                                    i32 = i13;
                                    break;
                                } else {
                                    i27 = zzao.zzj(bArr2, i20, zzanVar3);
                                    unsafe2.putInt(obj4, j, zzbe.zzb(zzanVar3.zza));
                                    i31 = i12 | i41;
                                    i29 = i18;
                                    i28 = i15;
                                    i30 = i19;
                                    i32 = i13;
                                    i26 = -1;
                                    i24 = i2;
                                    i25 = i3;
                                    break;
                                }
                            case 16:
                                if (i36 != 0) {
                                    i15 = i34;
                                    i21 = i35;
                                    i16 = i38;
                                    i9 = i3;
                                    i8 = i12;
                                    unsafe = unsafe2;
                                    i5 = i15;
                                    i10 = i21;
                                    i6 = i16;
                                    i7 = i39;
                                    i32 = i13;
                                    break;
                                } else {
                                    int zzm2 = zzao.zzm(bArr2, i38, zzanVar3);
                                    unsafe2.putLong(obj, j, zzbe.zzc(zzanVar3.zzb));
                                    i31 = i12 | i41;
                                    i29 = i35;
                                    i28 = i34;
                                    i30 = i39;
                                    i27 = zzm2;
                                    i32 = i13;
                                    i26 = -1;
                                    i24 = i2;
                                    i25 = i3;
                                    break;
                                }
                            default:
                                i14 = i35;
                                i15 = i34;
                                i16 = i38;
                                if (i36 != 3) {
                                    i21 = i14;
                                    i9 = i3;
                                    i8 = i12;
                                    unsafe = unsafe2;
                                    i5 = i15;
                                    i10 = i21;
                                    i6 = i16;
                                    i7 = i39;
                                    i32 = i13;
                                    break;
                                } else {
                                    Object zzD2 = zzdiVar2.zzD(obj4, i14);
                                    i27 = zzao.zzn(zzD2, zzdiVar2.zzB(i14), bArr, i16, i2, (i15 << 3) | 4, zzanVar);
                                    zzdiVar2.zzL(obj4, i14, zzD2);
                                    i31 = i12 | i41;
                                    i24 = i2;
                                    i28 = i15;
                                    i29 = i14;
                                    i30 = i39;
                                    i32 = i13;
                                    i26 = -1;
                                    bArr2 = bArr;
                                    i25 = i3;
                                    break;
                                }
                        }
                    } else if (zzx != 27) {
                        i8 = i31;
                        i22 = i32;
                        if (zzx <= 49) {
                            i5 = i34;
                            unsafe = unsafe2;
                            i10 = i35;
                            i27 = zzs(obj, bArr, i38, i2, i39, i34, i36, i35, i37, zzx, j, zzanVar);
                            if (i27 != i38) {
                                zzdiVar2 = this;
                                obj4 = obj;
                                bArr2 = bArr;
                                i28 = i5;
                                i24 = i2;
                                i25 = i3;
                                zzanVar3 = zzanVar;
                                i31 = i8;
                                i30 = i39;
                                i29 = i10;
                                i32 = i22;
                                unsafe2 = unsafe;
                                i26 = -1;
                            } else {
                                i6 = i27;
                                i7 = i39;
                                i32 = i22;
                                i9 = i3;
                            }
                        } else {
                            i5 = i34;
                            unsafe = unsafe2;
                            i10 = i35;
                            i23 = i38;
                            if (zzx != 50) {
                                i27 = zzr(obj, bArr, i23, i2, i39, i5, i36, i37, zzx, j, i10, zzanVar);
                                if (i27 != i23) {
                                    zzdiVar2 = this;
                                    obj4 = obj;
                                    bArr2 = bArr;
                                    i28 = i5;
                                    i24 = i2;
                                    i25 = i3;
                                    zzanVar3 = zzanVar;
                                    i31 = i8;
                                    i30 = i39;
                                    i29 = i10;
                                    i32 = i22;
                                    unsafe2 = unsafe;
                                    i26 = -1;
                                } else {
                                    i6 = i27;
                                    i7 = i39;
                                    i32 = i22;
                                    i9 = i3;
                                }
                            } else if (i36 == 2) {
                                i27 = zzq(obj, bArr, i23, i2, i10, j, zzanVar);
                                if (i27 != i23) {
                                    zzdiVar2 = this;
                                    obj4 = obj;
                                    bArr2 = bArr;
                                    i28 = i5;
                                    i24 = i2;
                                    i25 = i3;
                                    zzanVar3 = zzanVar;
                                    i31 = i8;
                                    i30 = i39;
                                    i29 = i10;
                                    i32 = i22;
                                    unsafe2 = unsafe;
                                    i26 = -1;
                                } else {
                                    i6 = i27;
                                    i7 = i39;
                                    i32 = i22;
                                    i9 = i3;
                                }
                            } else {
                                i9 = i3;
                                i6 = i23;
                                i7 = i39;
                                i32 = i22;
                            }
                        }
                    } else if (i36 == 2) {
                        zzcf zzcfVar = (zzcf) unsafe2.getObject(obj4, j);
                        if (!zzcfVar.zzc()) {
                            int size = zzcfVar.size();
                            zzcfVar = zzcfVar.zzd(size == 0 ? 10 : size + size);
                            unsafe2.putObject(obj4, j, zzcfVar);
                        }
                        i27 = zzao.zze(zzdiVar2.zzB(i35), i39, bArr, i38, i2, zzcfVar, zzanVar);
                        i24 = i2;
                        i25 = i3;
                        i28 = i34;
                        i29 = i35;
                        i31 = i31;
                        i30 = i39;
                        i32 = i32;
                        i26 = -1;
                        bArr2 = bArr;
                    } else {
                        i8 = i31;
                        i22 = i32;
                        i5 = i34;
                        unsafe = unsafe2;
                        i10 = i35;
                        i23 = i38;
                        i9 = i3;
                        i6 = i23;
                        i7 = i39;
                        i32 = i22;
                    }
                }
                if (i7 != i9 || i9 == 0) {
                    int i44 = i9;
                    if (this.zzh) {
                        zzanVar2 = zzanVar;
                        zzbn zzbnVar = zzanVar2.zzd;
                        if (zzbnVar != zzbn.zza) {
                            i11 = i5;
                            if (zzbnVar.zzb(this.zzg, i11) == null) {
                                i27 = zzao.zzi(i7, bArr, i6, i2, zzd(obj), zzanVar);
                                obj3 = obj;
                                i24 = i2;
                                i30 = i7;
                                zzdiVar2 = this;
                                i28 = i11;
                                obj4 = obj3;
                                i31 = i8;
                                i29 = i10;
                                i26 = -1;
                                bArr2 = bArr;
                                i25 = i44;
                                zzanVar3 = zzanVar2;
                                unsafe2 = unsafe;
                            } else {
                                zzby zzbyVar = (zzby) obj;
                                throw null;
                            }
                        } else {
                            obj3 = obj;
                            i11 = i5;
                        }
                    } else {
                        obj3 = obj;
                        i11 = i5;
                        zzanVar2 = zzanVar;
                    }
                    i27 = zzao.zzi(i7, bArr, i6, i2, zzd(obj), zzanVar);
                    i24 = i2;
                    i30 = i7;
                    zzdiVar2 = this;
                    i28 = i11;
                    obj4 = obj3;
                    i31 = i8;
                    i29 = i10;
                    i26 = -1;
                    bArr2 = bArr;
                    i25 = i44;
                    zzanVar3 = zzanVar2;
                    unsafe2 = unsafe;
                } else {
                    zzdiVar = this;
                    obj2 = obj;
                    i4 = i9;
                    i27 = i6;
                    i30 = i7;
                    i31 = i8;
                }
            } else {
                unsafe = unsafe2;
                i4 = i25;
                obj2 = obj4;
                zzdiVar = zzdiVar2;
            }
        }
    }

    @Override // com.google.android.gms.internal.play_billing.zzdp
    public final Object zze() {
        return ((zzcb) this.zzg).zzi();
    }

    @Override // com.google.android.gms.internal.play_billing.zzdp
    public final void zzf(Object obj) {
        if (zzS(obj)) {
            if (obj instanceof zzcb) {
                zzcb zzcbVar = (zzcb) obj;
                zzcbVar.zzq(ConnectionsManager.DEFAULT_DATACENTER_ID);
                zzcbVar.zza = 0;
                zzcbVar.zzo();
            }
            int length = this.zzc.length;
            for (int i = 0; i < length; i += 3) {
                int zzy = zzy(i);
                int i2 = 1048575 & zzy;
                int zzx = zzx(zzy);
                long j = i2;
                if (zzx != 9) {
                    if (zzx == 60 || zzx == 68) {
                        if (zzT(obj, this.zzc[i], i)) {
                            zzB(i).zzf(zzb.getObject(obj, j));
                        }
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
                            case R.styleable.AppCompatTheme_actionModeWebSearchDrawable /* 31 */:
                            case 32:
                            case R.styleable.AppCompatTheme_actionOverflowMenuStyle /* 33 */:
                            case R.styleable.AppCompatTheme_activityChooserViewStyle /* 34 */:
                            case R.styleable.AppCompatTheme_alertDialogButtonGroupStyle /* 35 */:
                            case R.styleable.AppCompatTheme_alertDialogCenterButtons /* 36 */:
                            case R.styleable.AppCompatTheme_alertDialogStyle /* 37 */:
                            case R.styleable.AppCompatTheme_alertDialogTheme /* 38 */:
                            case R.styleable.AppCompatTheme_autoCompleteTextViewStyle /* 39 */:
                            case R.styleable.AppCompatTheme_borderlessButtonStyle /* 40 */:
                            case R.styleable.AppCompatTheme_buttonBarButtonStyle /* 41 */:
                            case R.styleable.AppCompatTheme_buttonBarNegativeButtonStyle /* 42 */:
                            case R.styleable.AppCompatTheme_buttonBarNeutralButtonStyle /* 43 */:
                            case R.styleable.AppCompatTheme_buttonBarPositiveButtonStyle /* 44 */:
                            case R.styleable.AppCompatTheme_buttonBarStyle /* 45 */:
                            case R.styleable.AppCompatTheme_buttonStyle /* 46 */:
                            case R.styleable.AppCompatTheme_buttonStyleSmall /* 47 */:
                            case R.styleable.AppCompatTheme_checkboxStyle /* 48 */:
                            case R.styleable.AppCompatTheme_checkedTextViewStyle /* 49 */:
                                this.zzl.zza(obj, j);
                                break;
                            case R.styleable.AppCompatTheme_colorAccent /* 50 */:
                                Unsafe unsafe = zzb;
                                Object object = unsafe.getObject(obj, j);
                                if (object != null) {
                                    ((zzcz) object).zzc();
                                    unsafe.putObject(obj, j, object);
                                    break;
                                } else {
                                    break;
                                }
                        }
                    }
                }
                if (zzP(obj, i)) {
                    zzB(i).zzf(zzb.getObject(obj, j));
                }
            }
            this.zzm.zzg(obj);
            if (this.zzh) {
                this.zzn.zzb(obj);
            }
        }
    }

    @Override // com.google.android.gms.internal.play_billing.zzdp
    public final void zzg(Object obj, Object obj2) {
        zzG(obj);
        Objects.requireNonNull(obj2);
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
                    if (zzP(obj2, i)) {
                        zzeq.zzr(obj, j, zzeq.zzd(obj2, j));
                        zzJ(obj, i);
                        break;
                    } else {
                        break;
                    }
                case 3:
                    if (zzP(obj2, i)) {
                        zzeq.zzr(obj, j, zzeq.zzd(obj2, j));
                        zzJ(obj, i);
                        break;
                    } else {
                        break;
                    }
                case 4:
                    if (zzP(obj2, i)) {
                        zzeq.zzq(obj, j, zzeq.zzc(obj2, j));
                        zzJ(obj, i);
                        break;
                    } else {
                        break;
                    }
                case 5:
                    if (zzP(obj2, i)) {
                        zzeq.zzr(obj, j, zzeq.zzd(obj2, j));
                        zzJ(obj, i);
                        break;
                    } else {
                        break;
                    }
                case 6:
                    if (zzP(obj2, i)) {
                        zzeq.zzq(obj, j, zzeq.zzc(obj2, j));
                        zzJ(obj, i);
                        break;
                    } else {
                        break;
                    }
                case 7:
                    if (zzP(obj2, i)) {
                        zzeq.zzm(obj, j, zzeq.zzw(obj2, j));
                        zzJ(obj, i);
                        break;
                    } else {
                        break;
                    }
                case 8:
                    if (zzP(obj2, i)) {
                        zzeq.zzs(obj, j, zzeq.zzf(obj2, j));
                        zzJ(obj, i);
                        break;
                    } else {
                        break;
                    }
                case 9:
                    zzH(obj, obj2, i);
                    break;
                case 10:
                    if (zzP(obj2, i)) {
                        zzeq.zzs(obj, j, zzeq.zzf(obj2, j));
                        zzJ(obj, i);
                        break;
                    } else {
                        break;
                    }
                case 11:
                    if (zzP(obj2, i)) {
                        zzeq.zzq(obj, j, zzeq.zzc(obj2, j));
                        zzJ(obj, i);
                        break;
                    } else {
                        break;
                    }
                case 12:
                    if (zzP(obj2, i)) {
                        zzeq.zzq(obj, j, zzeq.zzc(obj2, j));
                        zzJ(obj, i);
                        break;
                    } else {
                        break;
                    }
                case 13:
                    if (zzP(obj2, i)) {
                        zzeq.zzq(obj, j, zzeq.zzc(obj2, j));
                        zzJ(obj, i);
                        break;
                    } else {
                        break;
                    }
                case 14:
                    if (zzP(obj2, i)) {
                        zzeq.zzr(obj, j, zzeq.zzd(obj2, j));
                        zzJ(obj, i);
                        break;
                    } else {
                        break;
                    }
                case 15:
                    if (zzP(obj2, i)) {
                        zzeq.zzq(obj, j, zzeq.zzc(obj2, j));
                        zzJ(obj, i);
                        break;
                    } else {
                        break;
                    }
                case 16:
                    if (zzP(obj2, i)) {
                        zzeq.zzr(obj, j, zzeq.zzd(obj2, j));
                        zzJ(obj, i);
                        break;
                    } else {
                        break;
                    }
                case 17:
                    zzH(obj, obj2, i);
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
                case R.styleable.AppCompatTheme_actionModeWebSearchDrawable /* 31 */:
                case 32:
                case R.styleable.AppCompatTheme_actionOverflowMenuStyle /* 33 */:
                case R.styleable.AppCompatTheme_activityChooserViewStyle /* 34 */:
                case R.styleable.AppCompatTheme_alertDialogButtonGroupStyle /* 35 */:
                case R.styleable.AppCompatTheme_alertDialogCenterButtons /* 36 */:
                case R.styleable.AppCompatTheme_alertDialogStyle /* 37 */:
                case R.styleable.AppCompatTheme_alertDialogTheme /* 38 */:
                case R.styleable.AppCompatTheme_autoCompleteTextViewStyle /* 39 */:
                case R.styleable.AppCompatTheme_borderlessButtonStyle /* 40 */:
                case R.styleable.AppCompatTheme_buttonBarButtonStyle /* 41 */:
                case R.styleable.AppCompatTheme_buttonBarNegativeButtonStyle /* 42 */:
                case R.styleable.AppCompatTheme_buttonBarNeutralButtonStyle /* 43 */:
                case R.styleable.AppCompatTheme_buttonBarPositiveButtonStyle /* 44 */:
                case R.styleable.AppCompatTheme_buttonBarStyle /* 45 */:
                case R.styleable.AppCompatTheme_buttonStyle /* 46 */:
                case R.styleable.AppCompatTheme_buttonStyleSmall /* 47 */:
                case R.styleable.AppCompatTheme_checkboxStyle /* 48 */:
                case R.styleable.AppCompatTheme_checkedTextViewStyle /* 49 */:
                    this.zzl.zzb(obj, obj2, j);
                    break;
                case R.styleable.AppCompatTheme_colorAccent /* 50 */:
                    int i3 = zzdr.zza;
                    zzeq.zzs(obj, j, zzda.zzb(zzeq.zzf(obj, j), zzeq.zzf(obj2, j)));
                    break;
                case R.styleable.AppCompatTheme_colorBackgroundFloating /* 51 */:
                case R.styleable.AppCompatTheme_colorButtonNormal /* 52 */:
                case R.styleable.AppCompatTheme_colorControlActivated /* 53 */:
                case R.styleable.AppCompatTheme_colorControlHighlight /* 54 */:
                case R.styleable.AppCompatTheme_colorControlNormal /* 55 */:
                case R.styleable.AppCompatTheme_colorError /* 56 */:
                case R.styleable.AppCompatTheme_colorPrimary /* 57 */:
                case R.styleable.AppCompatTheme_colorPrimaryDark /* 58 */:
                case R.styleable.AppCompatTheme_colorSwitchThumbNormal /* 59 */:
                    if (zzT(obj2, i2, i)) {
                        zzeq.zzs(obj, j, zzeq.zzf(obj2, j));
                        zzK(obj, i2, i);
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_controlBackground /* 60 */:
                    zzI(obj, obj2, i);
                    break;
                case R.styleable.AppCompatTheme_dialogCornerRadius /* 61 */:
                case R.styleable.AppCompatTheme_dialogPreferredPadding /* 62 */:
                case R.styleable.AppCompatTheme_dialogTheme /* 63 */:
                case 64:
                case 65:
                case R.styleable.AppCompatTheme_dropDownListViewStyle /* 66 */:
                case R.styleable.AppCompatTheme_dropdownListPreferredItemHeight /* 67 */:
                    if (zzT(obj2, i2, i)) {
                        zzeq.zzs(obj, j, zzeq.zzf(obj2, j));
                        zzK(obj, i2, i);
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_editTextBackground /* 68 */:
                    zzI(obj, obj2, i);
                    break;
            }
        }
        zzdr.zzB(this.zzm, obj, obj2);
        if (this.zzh) {
            this.zzn.zza(obj2);
            throw null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:104:0x02e6, code lost:
        if (r0 != r15) goto L129;
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x030a, code lost:
        if (r0 != r15) goto L129;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x02a3, code lost:
        if (r0 != r5) goto L129;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x02a5, code lost:
        r15 = r28;
        r14 = r29;
        r12 = r30;
        r13 = r32;
        r11 = r33;
        r1 = r19;
        r2 = r22;
        r6 = r25;
        r7 = r26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x02b9, code lost:
        r2 = r0;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v12, types: [int] */
    @Override // com.google.android.gms.internal.play_billing.zzdp
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void zzh(Object obj, byte[] bArr, int i, int i2, zzan zzanVar) throws IOException {
        byte b;
        int i3;
        int zzt;
        int i4;
        int i5;
        Unsafe unsafe;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        zzdi<T> zzdiVar = this;
        Object obj2 = obj;
        byte[] bArr2 = bArr;
        int i12 = i2;
        zzan zzanVar2 = zzanVar;
        zzew zzewVar = zzew.zza;
        int i13 = -1;
        if (zzdiVar.zzo - 1 != 0) {
            zzG(obj);
            Unsafe unsafe2 = zzb;
            int i14 = 1048575;
            int i15 = i;
            int i16 = -1;
            int i17 = 0;
            int i18 = 0;
            int i19 = 1048575;
            while (i15 < i12) {
                int i20 = i15 + 1;
                byte b2 = bArr2[i15];
                if (b2 < 0) {
                    i3 = zzao.zzk(b2, bArr2, i20, zzanVar2);
                    b = zzanVar2.zza;
                } else {
                    b = b2;
                    i3 = i20;
                }
                int i21 = b >>> 3;
                if (i21 > i16) {
                    zzt = zzdiVar.zzu(i21, i17 / 3);
                } else {
                    zzt = zzdiVar.zzt(i21);
                }
                int i22 = zzt;
                if (i22 == i13) {
                    i4 = i3;
                    i5 = i21;
                    unsafe = unsafe2;
                    i6 = 0;
                } else {
                    int i23 = b & 7;
                    int[] iArr = zzdiVar.zzc;
                    int i24 = iArr[i22 + 1];
                    int zzx = zzx(i24);
                    long j = i24 & i14;
                    if (zzx <= 17) {
                        int i25 = iArr[i22 + 2];
                        int i26 = 1 << (i25 >>> 20);
                        int i27 = i25 & 1048575;
                        if (i27 != i19) {
                            if (i19 != 1048575) {
                                unsafe2.putInt(obj2, i19, i18);
                            }
                            if (i27 != 1048575) {
                                i18 = unsafe2.getInt(obj2, i27);
                            }
                            i19 = i27;
                        }
                        switch (zzx) {
                            case 0:
                                i10 = i3;
                                i11 = i22;
                                i5 = i21;
                                if (i23 != 1) {
                                    i4 = i10;
                                    unsafe = unsafe2;
                                    i6 = i11;
                                    break;
                                } else {
                                    zzeq.zzo(obj2, j, Double.longBitsToDouble(zzao.zzp(bArr2, i10)));
                                    i15 = i10 + 8;
                                    i18 |= i26;
                                    i17 = i11;
                                    break;
                                }
                            case 1:
                                i10 = i3;
                                i11 = i22;
                                i5 = i21;
                                if (i23 != 5) {
                                    i4 = i10;
                                    unsafe = unsafe2;
                                    i6 = i11;
                                    break;
                                } else {
                                    zzeq.zzp(obj2, j, Float.intBitsToFloat(zzao.zzb(bArr2, i10)));
                                    i15 = i10 + 4;
                                    i18 |= i26;
                                    i17 = i11;
                                    break;
                                }
                            case 2:
                            case 3:
                                i10 = i3;
                                i11 = i22;
                                i5 = i21;
                                if (i23 != 0) {
                                    i4 = i10;
                                    unsafe = unsafe2;
                                    i6 = i11;
                                    break;
                                } else {
                                    int zzm = zzao.zzm(bArr2, i10, zzanVar2);
                                    unsafe2.putLong(obj, j, zzanVar2.zzb);
                                    i18 |= i26;
                                    i17 = i11;
                                    i15 = zzm;
                                    break;
                                }
                            case 4:
                            case 11:
                                i10 = i3;
                                i11 = i22;
                                i5 = i21;
                                if (i23 != 0) {
                                    i4 = i10;
                                    unsafe = unsafe2;
                                    i6 = i11;
                                    break;
                                } else {
                                    i15 = zzao.zzj(bArr2, i10, zzanVar2);
                                    unsafe2.putInt(obj2, j, zzanVar2.zza);
                                    i18 |= i26;
                                    i17 = i11;
                                    break;
                                }
                            case 5:
                            case 14:
                                i11 = i22;
                                i5 = i21;
                                if (i23 != 1) {
                                    i10 = i3;
                                    i4 = i10;
                                    unsafe = unsafe2;
                                    i6 = i11;
                                    break;
                                } else {
                                    unsafe2.putLong(obj, j, zzao.zzp(bArr2, i3));
                                    i15 = i3 + 8;
                                    i18 |= i26;
                                    i17 = i11;
                                    break;
                                }
                            case 6:
                            case 13:
                                i11 = i22;
                                i5 = i21;
                                if (i23 != 5) {
                                    i10 = i3;
                                    i4 = i10;
                                    unsafe = unsafe2;
                                    i6 = i11;
                                    break;
                                } else {
                                    unsafe2.putInt(obj2, j, zzao.zzb(bArr2, i3));
                                    i15 = i3 + 4;
                                    i18 |= i26;
                                    i17 = i11;
                                    break;
                                }
                            case 7:
                                i11 = i22;
                                i5 = i21;
                                if (i23 != 0) {
                                    i10 = i3;
                                    i4 = i10;
                                    unsafe = unsafe2;
                                    i6 = i11;
                                    break;
                                } else {
                                    i15 = zzao.zzm(bArr2, i3, zzanVar2);
                                    zzeq.zzm(obj2, j, zzanVar2.zzb != 0);
                                    i18 |= i26;
                                    i17 = i11;
                                    break;
                                }
                            case 8:
                                i11 = i22;
                                i5 = i21;
                                if (i23 != 2) {
                                    i10 = i3;
                                    i4 = i10;
                                    unsafe = unsafe2;
                                    i6 = i11;
                                    break;
                                } else {
                                    if ((i24 & 536870912) == 0) {
                                        i15 = zzao.zzg(bArr2, i3, zzanVar2);
                                    } else {
                                        i15 = zzao.zzh(bArr2, i3, zzanVar2);
                                    }
                                    unsafe2.putObject(obj2, j, zzanVar2.zzc);
                                    i18 |= i26;
                                    i17 = i11;
                                    break;
                                }
                            case 9:
                                i11 = i22;
                                i5 = i21;
                                if (i23 != 2) {
                                    i10 = i3;
                                    i4 = i10;
                                    unsafe = unsafe2;
                                    i6 = i11;
                                    break;
                                } else {
                                    Object zzD = zzdiVar.zzD(obj2, i11);
                                    i15 = zzao.zzo(zzD, zzdiVar.zzB(i11), bArr, i3, i2, zzanVar);
                                    zzdiVar.zzL(obj2, i11, zzD);
                                    i18 |= i26;
                                    i17 = i11;
                                    break;
                                }
                            case 10:
                                i11 = i22;
                                i5 = i21;
                                if (i23 != 2) {
                                    i10 = i3;
                                    i4 = i10;
                                    unsafe = unsafe2;
                                    i6 = i11;
                                    break;
                                } else {
                                    i15 = zzao.zza(bArr2, i3, zzanVar2);
                                    unsafe2.putObject(obj2, j, zzanVar2.zzc);
                                    i18 |= i26;
                                    i17 = i11;
                                    break;
                                }
                            case 12:
                                i11 = i22;
                                i5 = i21;
                                if (i23 != 0) {
                                    i10 = i3;
                                    i4 = i10;
                                    unsafe = unsafe2;
                                    i6 = i11;
                                    break;
                                } else {
                                    i15 = zzao.zzj(bArr2, i3, zzanVar2);
                                    unsafe2.putInt(obj2, j, zzanVar2.zza);
                                    i18 |= i26;
                                    i17 = i11;
                                    break;
                                }
                            case 15:
                                i11 = i22;
                                i5 = i21;
                                if (i23 != 0) {
                                    i10 = i3;
                                    i4 = i10;
                                    unsafe = unsafe2;
                                    i6 = i11;
                                    break;
                                } else {
                                    i15 = zzao.zzj(bArr2, i3, zzanVar2);
                                    unsafe2.putInt(obj2, j, zzbe.zzb(zzanVar2.zza));
                                    i18 |= i26;
                                    i17 = i11;
                                    break;
                                }
                            case 16:
                                if (i23 != 0) {
                                    i5 = i21;
                                    i10 = i3;
                                    i11 = i22;
                                    i4 = i10;
                                    unsafe = unsafe2;
                                    i6 = i11;
                                    break;
                                } else {
                                    int zzm2 = zzao.zzm(bArr2, i3, zzanVar2);
                                    i5 = i21;
                                    unsafe2.putLong(obj, j, zzbe.zzc(zzanVar2.zzb));
                                    i18 |= i26;
                                    i17 = i22;
                                    i15 = zzm2;
                                    break;
                                }
                            default:
                                i10 = i3;
                                i11 = i22;
                                i5 = i21;
                                i4 = i10;
                                unsafe = unsafe2;
                                i6 = i11;
                                break;
                        }
                        i16 = i5;
                        i14 = 1048575;
                        i13 = -1;
                        i12 = i2;
                    } else {
                        i5 = i21;
                        int i28 = i3;
                        if (zzx != 27) {
                            i6 = i22;
                            if (zzx <= 49) {
                                i8 = i18;
                                i9 = i19;
                                unsafe = unsafe2;
                                i15 = zzs(obj, bArr, i28, i2, b, i5, i23, i6, i24, zzx, j, zzanVar);
                            } else {
                                i7 = i28;
                                i8 = i18;
                                i9 = i19;
                                unsafe = unsafe2;
                                if (zzx != 50) {
                                    i15 = zzr(obj, bArr, i7, i2, b, i5, i23, i24, zzx, j, i6, zzanVar);
                                } else if (i23 == 2) {
                                    i15 = zzq(obj, bArr, i7, i2, i6, j, zzanVar);
                                }
                            }
                            i14 = 1048575;
                            i13 = -1;
                        } else if (i23 == 2) {
                            zzcf zzcfVar = (zzcf) unsafe2.getObject(obj2, j);
                            if (!zzcfVar.zzc()) {
                                int size = zzcfVar.size();
                                zzcfVar = zzcfVar.zzd(size == 0 ? 10 : size + size);
                                unsafe2.putObject(obj2, j, zzcfVar);
                            }
                            i15 = zzao.zze(zzdiVar.zzB(i22), b, bArr, i28, i2, zzcfVar, zzanVar);
                            i12 = i2;
                            i18 = i18;
                            i16 = i5;
                            i17 = i22;
                            i14 = 1048575;
                            i13 = -1;
                        } else {
                            i6 = i22;
                            i7 = i28;
                            i8 = i18;
                            i9 = i19;
                            unsafe = unsafe2;
                        }
                        i4 = i7;
                        i18 = i8;
                        i19 = i9;
                    }
                }
                i15 = zzao.zzi(b, bArr, i4, i2, zzd(obj), zzanVar);
                zzdiVar = this;
                obj2 = obj;
                bArr2 = bArr;
                i12 = i2;
                zzanVar2 = zzanVar;
                i16 = i5;
                i17 = i6;
                unsafe2 = unsafe;
                i14 = 1048575;
                i13 = -1;
            }
            int i29 = i18;
            Unsafe unsafe3 = unsafe2;
            if (i19 != 1048575) {
                unsafe3.putInt(obj, i19, i29);
            }
            if (i15 != i2) {
                throw zzci.zze();
            }
            return;
        }
        zzc(obj, bArr, i, i2, 0, zzanVar);
    }

    @Override // com.google.android.gms.internal.play_billing.zzdp
    public final void zzi(Object obj, zzey zzeyVar) throws IOException {
        int i;
        zzew zzewVar = zzew.zza;
        int i2 = 1048575;
        if (this.zzo - 1 == 0) {
            if (this.zzh) {
                this.zzn.zza(obj);
                throw null;
            } else {
                int length = this.zzc.length;
                Unsafe unsafe = zzb;
                int i3 = 0;
                int i4 = 1048575;
                int i5 = 0;
                while (i3 < length) {
                    int zzy = zzy(i3);
                    int[] iArr = this.zzc;
                    int i6 = iArr[i3];
                    int zzx = zzx(zzy);
                    if (zzx <= 17) {
                        int i7 = iArr[i3 + 2];
                        int i8 = i7 & i2;
                        if (i8 != i4) {
                            i5 = unsafe.getInt(obj, i8);
                            i4 = i8;
                        }
                        i = 1 << (i7 >>> 20);
                    } else {
                        i = 0;
                    }
                    long j = zzy & i2;
                    switch (zzx) {
                        case 0:
                            if ((i5 & i) != 0) {
                                zzeyVar.zzf(i6, zzeq.zza(obj, j));
                                continue;
                            }
                            i3 += 3;
                            i2 = 1048575;
                        case 1:
                            if ((i5 & i) != 0) {
                                zzeyVar.zzo(i6, zzeq.zzb(obj, j));
                            } else {
                                continue;
                            }
                            i3 += 3;
                            i2 = 1048575;
                        case 2:
                            if ((i5 & i) != 0) {
                                zzeyVar.zzt(i6, unsafe.getLong(obj, j));
                            } else {
                                continue;
                            }
                            i3 += 3;
                            i2 = 1048575;
                        case 3:
                            if ((i5 & i) != 0) {
                                zzeyVar.zzJ(i6, unsafe.getLong(obj, j));
                            } else {
                                continue;
                            }
                            i3 += 3;
                            i2 = 1048575;
                        case 4:
                            if ((i5 & i) != 0) {
                                zzeyVar.zzr(i6, unsafe.getInt(obj, j));
                            } else {
                                continue;
                            }
                            i3 += 3;
                            i2 = 1048575;
                        case 5:
                            if ((i5 & i) != 0) {
                                zzeyVar.zzm(i6, unsafe.getLong(obj, j));
                            } else {
                                continue;
                            }
                            i3 += 3;
                            i2 = 1048575;
                        case 6:
                            if ((i5 & i) != 0) {
                                zzeyVar.zzk(i6, unsafe.getInt(obj, j));
                            } else {
                                continue;
                            }
                            i3 += 3;
                            i2 = 1048575;
                        case 7:
                            if ((i5 & i) != 0) {
                                zzeyVar.zzb(i6, zzeq.zzw(obj, j));
                            } else {
                                continue;
                            }
                            i3 += 3;
                            i2 = 1048575;
                        case 8:
                            if ((i5 & i) != 0) {
                                zzV(i6, unsafe.getObject(obj, j), zzeyVar);
                            } else {
                                continue;
                            }
                            i3 += 3;
                            i2 = 1048575;
                        case 9:
                            if ((i5 & i) != 0) {
                                zzeyVar.zzv(i6, unsafe.getObject(obj, j), zzB(i3));
                            } else {
                                continue;
                            }
                            i3 += 3;
                            i2 = 1048575;
                        case 10:
                            if ((i5 & i) != 0) {
                                zzeyVar.zzd(i6, (zzba) unsafe.getObject(obj, j));
                            } else {
                                continue;
                            }
                            i3 += 3;
                            i2 = 1048575;
                        case 11:
                            if ((i5 & i) != 0) {
                                zzeyVar.zzH(i6, unsafe.getInt(obj, j));
                            } else {
                                continue;
                            }
                            i3 += 3;
                            i2 = 1048575;
                        case 12:
                            if ((i5 & i) != 0) {
                                zzeyVar.zzi(i6, unsafe.getInt(obj, j));
                            } else {
                                continue;
                            }
                            i3 += 3;
                            i2 = 1048575;
                        case 13:
                            if ((i5 & i) != 0) {
                                zzeyVar.zzw(i6, unsafe.getInt(obj, j));
                            } else {
                                continue;
                            }
                            i3 += 3;
                            i2 = 1048575;
                        case 14:
                            if ((i5 & i) != 0) {
                                zzeyVar.zzy(i6, unsafe.getLong(obj, j));
                            } else {
                                continue;
                            }
                            i3 += 3;
                            i2 = 1048575;
                        case 15:
                            if ((i5 & i) != 0) {
                                zzeyVar.zzA(i6, unsafe.getInt(obj, j));
                            } else {
                                continue;
                            }
                            i3 += 3;
                            i2 = 1048575;
                        case 16:
                            if ((i5 & i) != 0) {
                                zzeyVar.zzC(i6, unsafe.getLong(obj, j));
                            } else {
                                continue;
                            }
                            i3 += 3;
                            i2 = 1048575;
                        case 17:
                            if ((i5 & i) != 0) {
                                zzeyVar.zzq(i6, unsafe.getObject(obj, j), zzB(i3));
                            } else {
                                continue;
                            }
                            i3 += 3;
                            i2 = 1048575;
                        case 18:
                            zzdr.zzF(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, false);
                            continue;
                            i3 += 3;
                            i2 = 1048575;
                        case 19:
                            zzdr.zzJ(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, false);
                            continue;
                            i3 += 3;
                            i2 = 1048575;
                        case 20:
                            zzdr.zzM(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, false);
                            continue;
                            i3 += 3;
                            i2 = 1048575;
                        case 21:
                            zzdr.zzU(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, false);
                            continue;
                            i3 += 3;
                            i2 = 1048575;
                        case 22:
                            zzdr.zzL(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, false);
                            continue;
                            i3 += 3;
                            i2 = 1048575;
                        case 23:
                            zzdr.zzI(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, false);
                            continue;
                            i3 += 3;
                            i2 = 1048575;
                        case 24:
                            zzdr.zzH(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, false);
                            continue;
                            i3 += 3;
                            i2 = 1048575;
                        case 25:
                            zzdr.zzD(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, false);
                            continue;
                            i3 += 3;
                            i2 = 1048575;
                        case 26:
                            zzdr.zzS(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar);
                            break;
                        case 27:
                            zzdr.zzN(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, zzB(i3));
                            break;
                        case 28:
                            zzdr.zzE(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar);
                            break;
                        case 29:
                            zzdr.zzT(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, false);
                            break;
                        case 30:
                            zzdr.zzG(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, false);
                            break;
                        case R.styleable.AppCompatTheme_actionModeWebSearchDrawable /* 31 */:
                            zzdr.zzO(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, false);
                            break;
                        case 32:
                            zzdr.zzP(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, false);
                            break;
                        case R.styleable.AppCompatTheme_actionOverflowMenuStyle /* 33 */:
                            zzdr.zzQ(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, false);
                            break;
                        case R.styleable.AppCompatTheme_activityChooserViewStyle /* 34 */:
                            zzdr.zzR(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, false);
                            break;
                        case R.styleable.AppCompatTheme_alertDialogButtonGroupStyle /* 35 */:
                            zzdr.zzF(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_alertDialogCenterButtons /* 36 */:
                            zzdr.zzJ(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_alertDialogStyle /* 37 */:
                            zzdr.zzM(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_alertDialogTheme /* 38 */:
                            zzdr.zzU(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_autoCompleteTextViewStyle /* 39 */:
                            zzdr.zzL(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_borderlessButtonStyle /* 40 */:
                            zzdr.zzI(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_buttonBarButtonStyle /* 41 */:
                            zzdr.zzH(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_buttonBarNegativeButtonStyle /* 42 */:
                            zzdr.zzD(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_buttonBarNeutralButtonStyle /* 43 */:
                            zzdr.zzT(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_buttonBarPositiveButtonStyle /* 44 */:
                            zzdr.zzG(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_buttonBarStyle /* 45 */:
                            zzdr.zzO(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_buttonStyle /* 46 */:
                            zzdr.zzP(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_buttonStyleSmall /* 47 */:
                            zzdr.zzQ(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_checkboxStyle /* 48 */:
                            zzdr.zzR(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_checkedTextViewStyle /* 49 */:
                            zzdr.zzK(this.zzc[i3], (List) unsafe.getObject(obj, j), zzeyVar, zzB(i3));
                            break;
                        case R.styleable.AppCompatTheme_colorAccent /* 50 */:
                            zzN(zzeyVar, i6, unsafe.getObject(obj, j), i3);
                            break;
                        case R.styleable.AppCompatTheme_colorBackgroundFloating /* 51 */:
                            if (zzT(obj, i6, i3)) {
                                zzeyVar.zzf(i6, zzm(obj, j));
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_colorButtonNormal /* 52 */:
                            if (zzT(obj, i6, i3)) {
                                zzeyVar.zzo(i6, zzn(obj, j));
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_colorControlActivated /* 53 */:
                            if (zzT(obj, i6, i3)) {
                                zzeyVar.zzt(i6, zzz(obj, j));
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_colorControlHighlight /* 54 */:
                            if (zzT(obj, i6, i3)) {
                                zzeyVar.zzJ(i6, zzz(obj, j));
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_colorControlNormal /* 55 */:
                            if (zzT(obj, i6, i3)) {
                                zzeyVar.zzr(i6, zzp(obj, j));
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_colorError /* 56 */:
                            if (zzT(obj, i6, i3)) {
                                zzeyVar.zzm(i6, zzz(obj, j));
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_colorPrimary /* 57 */:
                            if (zzT(obj, i6, i3)) {
                                zzeyVar.zzk(i6, zzp(obj, j));
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_colorPrimaryDark /* 58 */:
                            if (zzT(obj, i6, i3)) {
                                zzeyVar.zzb(i6, zzU(obj, j));
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_colorSwitchThumbNormal /* 59 */:
                            if (zzT(obj, i6, i3)) {
                                zzV(i6, unsafe.getObject(obj, j), zzeyVar);
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_controlBackground /* 60 */:
                            if (zzT(obj, i6, i3)) {
                                zzeyVar.zzv(i6, unsafe.getObject(obj, j), zzB(i3));
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_dialogCornerRadius /* 61 */:
                            if (zzT(obj, i6, i3)) {
                                zzeyVar.zzd(i6, (zzba) unsafe.getObject(obj, j));
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_dialogPreferredPadding /* 62 */:
                            if (zzT(obj, i6, i3)) {
                                zzeyVar.zzH(i6, zzp(obj, j));
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_dialogTheme /* 63 */:
                            if (zzT(obj, i6, i3)) {
                                zzeyVar.zzi(i6, zzp(obj, j));
                                break;
                            }
                            break;
                        case 64:
                            if (zzT(obj, i6, i3)) {
                                zzeyVar.zzw(i6, zzp(obj, j));
                                break;
                            }
                            break;
                        case 65:
                            if (zzT(obj, i6, i3)) {
                                zzeyVar.zzy(i6, zzz(obj, j));
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_dropDownListViewStyle /* 66 */:
                            if (zzT(obj, i6, i3)) {
                                zzeyVar.zzA(i6, zzp(obj, j));
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_dropdownListPreferredItemHeight /* 67 */:
                            if (zzT(obj, i6, i3)) {
                                zzeyVar.zzC(i6, zzz(obj, j));
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_editTextBackground /* 68 */:
                            if (zzT(obj, i6, i3)) {
                                zzeyVar.zzq(i6, unsafe.getObject(obj, j), zzB(i3));
                                break;
                            }
                            break;
                    }
                    i3 += 3;
                    i2 = 1048575;
                }
                zzeg zzegVar = this.zzm;
                zzegVar.zzi(zzegVar.zzd(obj), zzeyVar);
            }
        } else {
            if (!this.zzh) {
                int length2 = this.zzc.length;
                for (int i9 = 0; i9 < length2; i9 += 3) {
                    int zzy2 = zzy(i9);
                    int i10 = this.zzc[i9];
                    switch (zzx(zzy2)) {
                        case 0:
                            if (zzP(obj, i9)) {
                                zzeyVar.zzf(i10, zzeq.zza(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case 1:
                            if (zzP(obj, i9)) {
                                zzeyVar.zzo(i10, zzeq.zzb(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case 2:
                            if (zzP(obj, i9)) {
                                zzeyVar.zzt(i10, zzeq.zzd(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case 3:
                            if (zzP(obj, i9)) {
                                zzeyVar.zzJ(i10, zzeq.zzd(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case 4:
                            if (zzP(obj, i9)) {
                                zzeyVar.zzr(i10, zzeq.zzc(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case 5:
                            if (zzP(obj, i9)) {
                                zzeyVar.zzm(i10, zzeq.zzd(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case 6:
                            if (zzP(obj, i9)) {
                                zzeyVar.zzk(i10, zzeq.zzc(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case 7:
                            if (zzP(obj, i9)) {
                                zzeyVar.zzb(i10, zzeq.zzw(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case 8:
                            if (zzP(obj, i9)) {
                                zzV(i10, zzeq.zzf(obj, zzy2 & 1048575), zzeyVar);
                                break;
                            } else {
                                break;
                            }
                        case 9:
                            if (zzP(obj, i9)) {
                                zzeyVar.zzv(i10, zzeq.zzf(obj, zzy2 & 1048575), zzB(i9));
                                break;
                            } else {
                                break;
                            }
                        case 10:
                            if (zzP(obj, i9)) {
                                zzeyVar.zzd(i10, (zzba) zzeq.zzf(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case 11:
                            if (zzP(obj, i9)) {
                                zzeyVar.zzH(i10, zzeq.zzc(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case 12:
                            if (zzP(obj, i9)) {
                                zzeyVar.zzi(i10, zzeq.zzc(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case 13:
                            if (zzP(obj, i9)) {
                                zzeyVar.zzw(i10, zzeq.zzc(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case 14:
                            if (zzP(obj, i9)) {
                                zzeyVar.zzy(i10, zzeq.zzd(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case 15:
                            if (zzP(obj, i9)) {
                                zzeyVar.zzA(i10, zzeq.zzc(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case 16:
                            if (zzP(obj, i9)) {
                                zzeyVar.zzC(i10, zzeq.zzd(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case 17:
                            if (zzP(obj, i9)) {
                                zzeyVar.zzq(i10, zzeq.zzf(obj, zzy2 & 1048575), zzB(i9));
                                break;
                            } else {
                                break;
                            }
                        case 18:
                            zzdr.zzF(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, false);
                            break;
                        case 19:
                            zzdr.zzJ(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, false);
                            break;
                        case 20:
                            zzdr.zzM(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, false);
                            break;
                        case 21:
                            zzdr.zzU(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, false);
                            break;
                        case 22:
                            zzdr.zzL(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, false);
                            break;
                        case 23:
                            zzdr.zzI(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, false);
                            break;
                        case 24:
                            zzdr.zzH(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, false);
                            break;
                        case 25:
                            zzdr.zzD(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, false);
                            break;
                        case 26:
                            zzdr.zzS(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar);
                            break;
                        case 27:
                            zzdr.zzN(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, zzB(i9));
                            break;
                        case 28:
                            zzdr.zzE(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar);
                            break;
                        case 29:
                            zzdr.zzT(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, false);
                            break;
                        case 30:
                            zzdr.zzG(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, false);
                            break;
                        case R.styleable.AppCompatTheme_actionModeWebSearchDrawable /* 31 */:
                            zzdr.zzO(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, false);
                            break;
                        case 32:
                            zzdr.zzP(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, false);
                            break;
                        case R.styleable.AppCompatTheme_actionOverflowMenuStyle /* 33 */:
                            zzdr.zzQ(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, false);
                            break;
                        case R.styleable.AppCompatTheme_activityChooserViewStyle /* 34 */:
                            zzdr.zzR(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, false);
                            break;
                        case R.styleable.AppCompatTheme_alertDialogButtonGroupStyle /* 35 */:
                            zzdr.zzF(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_alertDialogCenterButtons /* 36 */:
                            zzdr.zzJ(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_alertDialogStyle /* 37 */:
                            zzdr.zzM(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_alertDialogTheme /* 38 */:
                            zzdr.zzU(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_autoCompleteTextViewStyle /* 39 */:
                            zzdr.zzL(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_borderlessButtonStyle /* 40 */:
                            zzdr.zzI(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_buttonBarButtonStyle /* 41 */:
                            zzdr.zzH(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_buttonBarNegativeButtonStyle /* 42 */:
                            zzdr.zzD(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_buttonBarNeutralButtonStyle /* 43 */:
                            zzdr.zzT(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_buttonBarPositiveButtonStyle /* 44 */:
                            zzdr.zzG(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_buttonBarStyle /* 45 */:
                            zzdr.zzO(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_buttonStyle /* 46 */:
                            zzdr.zzP(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_buttonStyleSmall /* 47 */:
                            zzdr.zzQ(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_checkboxStyle /* 48 */:
                            zzdr.zzR(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, true);
                            break;
                        case R.styleable.AppCompatTheme_checkedTextViewStyle /* 49 */:
                            zzdr.zzK(i10, (List) zzeq.zzf(obj, zzy2 & 1048575), zzeyVar, zzB(i9));
                            break;
                        case R.styleable.AppCompatTheme_colorAccent /* 50 */:
                            zzN(zzeyVar, i10, zzeq.zzf(obj, zzy2 & 1048575), i9);
                            break;
                        case R.styleable.AppCompatTheme_colorBackgroundFloating /* 51 */:
                            if (zzT(obj, i10, i9)) {
                                zzeyVar.zzf(i10, zzm(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case R.styleable.AppCompatTheme_colorButtonNormal /* 52 */:
                            if (zzT(obj, i10, i9)) {
                                zzeyVar.zzo(i10, zzn(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case R.styleable.AppCompatTheme_colorControlActivated /* 53 */:
                            if (zzT(obj, i10, i9)) {
                                zzeyVar.zzt(i10, zzz(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case R.styleable.AppCompatTheme_colorControlHighlight /* 54 */:
                            if (zzT(obj, i10, i9)) {
                                zzeyVar.zzJ(i10, zzz(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case R.styleable.AppCompatTheme_colorControlNormal /* 55 */:
                            if (zzT(obj, i10, i9)) {
                                zzeyVar.zzr(i10, zzp(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case R.styleable.AppCompatTheme_colorError /* 56 */:
                            if (zzT(obj, i10, i9)) {
                                zzeyVar.zzm(i10, zzz(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case R.styleable.AppCompatTheme_colorPrimary /* 57 */:
                            if (zzT(obj, i10, i9)) {
                                zzeyVar.zzk(i10, zzp(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case R.styleable.AppCompatTheme_colorPrimaryDark /* 58 */:
                            if (zzT(obj, i10, i9)) {
                                zzeyVar.zzb(i10, zzU(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case R.styleable.AppCompatTheme_colorSwitchThumbNormal /* 59 */:
                            if (zzT(obj, i10, i9)) {
                                zzV(i10, zzeq.zzf(obj, zzy2 & 1048575), zzeyVar);
                                break;
                            } else {
                                break;
                            }
                        case R.styleable.AppCompatTheme_controlBackground /* 60 */:
                            if (zzT(obj, i10, i9)) {
                                zzeyVar.zzv(i10, zzeq.zzf(obj, zzy2 & 1048575), zzB(i9));
                                break;
                            } else {
                                break;
                            }
                        case R.styleable.AppCompatTheme_dialogCornerRadius /* 61 */:
                            if (zzT(obj, i10, i9)) {
                                zzeyVar.zzd(i10, (zzba) zzeq.zzf(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case R.styleable.AppCompatTheme_dialogPreferredPadding /* 62 */:
                            if (zzT(obj, i10, i9)) {
                                zzeyVar.zzH(i10, zzp(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case R.styleable.AppCompatTheme_dialogTheme /* 63 */:
                            if (zzT(obj, i10, i9)) {
                                zzeyVar.zzi(i10, zzp(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case 64:
                            if (zzT(obj, i10, i9)) {
                                zzeyVar.zzw(i10, zzp(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case 65:
                            if (zzT(obj, i10, i9)) {
                                zzeyVar.zzy(i10, zzz(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case R.styleable.AppCompatTheme_dropDownListViewStyle /* 66 */:
                            if (zzT(obj, i10, i9)) {
                                zzeyVar.zzA(i10, zzp(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case R.styleable.AppCompatTheme_dropdownListPreferredItemHeight /* 67 */:
                            if (zzT(obj, i10, i9)) {
                                zzeyVar.zzC(i10, zzz(obj, zzy2 & 1048575));
                                break;
                            } else {
                                break;
                            }
                        case R.styleable.AppCompatTheme_editTextBackground /* 68 */:
                            if (zzT(obj, i10, i9)) {
                                zzeyVar.zzq(i10, zzeq.zzf(obj, zzy2 & 1048575), zzB(i9));
                                break;
                            } else {
                                break;
                            }
                    }
                }
                zzeg zzegVar2 = this.zzm;
                zzegVar2.zzi(zzegVar2.zzd(obj), zzeyVar);
                return;
            }
            this.zzn.zza(obj);
            throw null;
        }
    }

    @Override // com.google.android.gms.internal.play_billing.zzdp
    public final boolean zzj(Object obj, Object obj2) {
        boolean zzV;
        int length = this.zzc.length;
        for (int i = 0; i < length; i += 3) {
            int zzy = zzy(i);
            long j = zzy & 1048575;
            switch (zzx(zzy)) {
                case 0:
                    if (zzO(obj, obj2, i) && Double.doubleToLongBits(zzeq.zza(obj, j)) == Double.doubleToLongBits(zzeq.zza(obj2, j))) {
                        continue;
                    }
                    return false;
                case 1:
                    if (zzO(obj, obj2, i) && Float.floatToIntBits(zzeq.zzb(obj, j)) == Float.floatToIntBits(zzeq.zzb(obj2, j))) {
                        continue;
                    }
                    return false;
                case 2:
                    if (zzO(obj, obj2, i) && zzeq.zzd(obj, j) == zzeq.zzd(obj2, j)) {
                        continue;
                    }
                    return false;
                case 3:
                    if (zzO(obj, obj2, i) && zzeq.zzd(obj, j) == zzeq.zzd(obj2, j)) {
                        continue;
                    }
                    return false;
                case 4:
                    if (zzO(obj, obj2, i) && zzeq.zzc(obj, j) == zzeq.zzc(obj2, j)) {
                        continue;
                    }
                    return false;
                case 5:
                    if (zzO(obj, obj2, i) && zzeq.zzd(obj, j) == zzeq.zzd(obj2, j)) {
                        continue;
                    }
                    return false;
                case 6:
                    if (zzO(obj, obj2, i) && zzeq.zzc(obj, j) == zzeq.zzc(obj2, j)) {
                        continue;
                    }
                    return false;
                case 7:
                    if (zzO(obj, obj2, i) && zzeq.zzw(obj, j) == zzeq.zzw(obj2, j)) {
                        continue;
                    }
                    return false;
                case 8:
                    if (zzO(obj, obj2, i) && zzdr.zzV(zzeq.zzf(obj, j), zzeq.zzf(obj2, j))) {
                        continue;
                    }
                    return false;
                case 9:
                    if (zzO(obj, obj2, i) && zzdr.zzV(zzeq.zzf(obj, j), zzeq.zzf(obj2, j))) {
                        continue;
                    }
                    return false;
                case 10:
                    if (zzO(obj, obj2, i) && zzdr.zzV(zzeq.zzf(obj, j), zzeq.zzf(obj2, j))) {
                        continue;
                    }
                    return false;
                case 11:
                    if (zzO(obj, obj2, i) && zzeq.zzc(obj, j) == zzeq.zzc(obj2, j)) {
                        continue;
                    }
                    return false;
                case 12:
                    if (zzO(obj, obj2, i) && zzeq.zzc(obj, j) == zzeq.zzc(obj2, j)) {
                        continue;
                    }
                    return false;
                case 13:
                    if (zzO(obj, obj2, i) && zzeq.zzc(obj, j) == zzeq.zzc(obj2, j)) {
                        continue;
                    }
                    return false;
                case 14:
                    if (zzO(obj, obj2, i) && zzeq.zzd(obj, j) == zzeq.zzd(obj2, j)) {
                        continue;
                    }
                    return false;
                case 15:
                    if (zzO(obj, obj2, i) && zzeq.zzc(obj, j) == zzeq.zzc(obj2, j)) {
                        continue;
                    }
                    return false;
                case 16:
                    if (zzO(obj, obj2, i) && zzeq.zzd(obj, j) == zzeq.zzd(obj2, j)) {
                        continue;
                    }
                    return false;
                case 17:
                    if (zzO(obj, obj2, i) && zzdr.zzV(zzeq.zzf(obj, j), zzeq.zzf(obj2, j))) {
                        continue;
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
                case R.styleable.AppCompatTheme_actionModeWebSearchDrawable /* 31 */:
                case 32:
                case R.styleable.AppCompatTheme_actionOverflowMenuStyle /* 33 */:
                case R.styleable.AppCompatTheme_activityChooserViewStyle /* 34 */:
                case R.styleable.AppCompatTheme_alertDialogButtonGroupStyle /* 35 */:
                case R.styleable.AppCompatTheme_alertDialogCenterButtons /* 36 */:
                case R.styleable.AppCompatTheme_alertDialogStyle /* 37 */:
                case R.styleable.AppCompatTheme_alertDialogTheme /* 38 */:
                case R.styleable.AppCompatTheme_autoCompleteTextViewStyle /* 39 */:
                case R.styleable.AppCompatTheme_borderlessButtonStyle /* 40 */:
                case R.styleable.AppCompatTheme_buttonBarButtonStyle /* 41 */:
                case R.styleable.AppCompatTheme_buttonBarNegativeButtonStyle /* 42 */:
                case R.styleable.AppCompatTheme_buttonBarNeutralButtonStyle /* 43 */:
                case R.styleable.AppCompatTheme_buttonBarPositiveButtonStyle /* 44 */:
                case R.styleable.AppCompatTheme_buttonBarStyle /* 45 */:
                case R.styleable.AppCompatTheme_buttonStyle /* 46 */:
                case R.styleable.AppCompatTheme_buttonStyleSmall /* 47 */:
                case R.styleable.AppCompatTheme_checkboxStyle /* 48 */:
                case R.styleable.AppCompatTheme_checkedTextViewStyle /* 49 */:
                    zzV = zzdr.zzV(zzeq.zzf(obj, j), zzeq.zzf(obj2, j));
                    break;
                case R.styleable.AppCompatTheme_colorAccent /* 50 */:
                    zzV = zzdr.zzV(zzeq.zzf(obj, j), zzeq.zzf(obj2, j));
                    break;
                case R.styleable.AppCompatTheme_colorBackgroundFloating /* 51 */:
                case R.styleable.AppCompatTheme_colorButtonNormal /* 52 */:
                case R.styleable.AppCompatTheme_colorControlActivated /* 53 */:
                case R.styleable.AppCompatTheme_colorControlHighlight /* 54 */:
                case R.styleable.AppCompatTheme_colorControlNormal /* 55 */:
                case R.styleable.AppCompatTheme_colorError /* 56 */:
                case R.styleable.AppCompatTheme_colorPrimary /* 57 */:
                case R.styleable.AppCompatTheme_colorPrimaryDark /* 58 */:
                case R.styleable.AppCompatTheme_colorSwitchThumbNormal /* 59 */:
                case R.styleable.AppCompatTheme_controlBackground /* 60 */:
                case R.styleable.AppCompatTheme_dialogCornerRadius /* 61 */:
                case R.styleable.AppCompatTheme_dialogPreferredPadding /* 62 */:
                case R.styleable.AppCompatTheme_dialogTheme /* 63 */:
                case 64:
                case 65:
                case R.styleable.AppCompatTheme_dropDownListViewStyle /* 66 */:
                case R.styleable.AppCompatTheme_dropdownListPreferredItemHeight /* 67 */:
                case R.styleable.AppCompatTheme_editTextBackground /* 68 */:
                    long zzv = zzv(i) & 1048575;
                    if (zzeq.zzc(obj, zzv) == zzeq.zzc(obj2, zzv) && zzdr.zzV(zzeq.zzf(obj, j), zzeq.zzf(obj2, j))) {
                        continue;
                    }
                    return false;
                default:
            }
            if (!zzV) {
                return false;
            }
        }
        if (this.zzm.zzd(obj).equals(this.zzm.zzd(obj2))) {
            if (this.zzh) {
                this.zzn.zza(obj);
                this.zzn.zza(obj2);
                throw null;
            }
            return true;
        }
        return false;
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
                            zzcy zzcyVar = (zzcy) zzC(i6);
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
                    continue;
                }
            } else if (zzQ(obj, i6, i, i2, i10) && !zzR(obj, zzy, zzB(i6))) {
                return false;
            }
            i5++;
            i3 = i;
            i4 = i2;
        }
        if (this.zzh) {
            this.zzn.zza(obj);
            throw null;
        }
        return true;
    }
}
