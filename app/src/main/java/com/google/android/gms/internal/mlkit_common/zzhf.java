package com.google.android.gms.internal.mlkit_common;

import androidx.core.text.HtmlCompat;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.googlecode.mp4parser.authoring.tracks.h265.NalUnitTypes;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.telegram.messenger.CharacterCompat;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.ui.Components.UndoView;
import sun.misc.Unsafe;
/* compiled from: com.google.mlkit:common@@17.0.0 */
/* loaded from: classes3.dex */
final class zzhf<T> implements zzhr<T> {
    private static final int[] zza = new int[0];
    private static final Unsafe zzb = zzip.zzc();
    private final int[] zzc;
    private final Object[] zzd;
    private final int zze;
    private final int zzf;
    private final zzhb zzg;
    private final boolean zzh;
    private final boolean zzi;
    private final boolean zzj;
    private final boolean zzk;
    private final int[] zzl;
    private final int zzm;
    private final int zzn;
    private final zzhg zzo;
    private final zzgl zzp;
    private final zzij<?, ?> zzq;
    private final zzfg<?> zzr;
    private final zzgu zzs;

    private zzhf(int[] iArr, Object[] objArr, int i, int i2, zzhb zzhbVar, boolean z, boolean z2, int[] iArr2, int i3, int i4, zzhg zzhgVar, zzgl zzglVar, zzij<?, ?> zzijVar, zzfg<?> zzfgVar, zzgu zzguVar) {
        this.zzc = iArr;
        this.zzd = objArr;
        this.zze = i;
        this.zzf = i2;
        this.zzi = zzhbVar instanceof zzfq;
        this.zzj = z;
        this.zzh = zzfgVar != null && zzfgVar.zza(zzhbVar);
        this.zzk = false;
        this.zzl = iArr2;
        this.zzm = i3;
        this.zzn = i4;
        this.zzo = zzhgVar;
        this.zzp = zzglVar;
        this.zzq = zzijVar;
        this.zzr = zzfgVar;
        this.zzg = zzhbVar;
        this.zzs = zzguVar;
    }

    /* JADX WARN: Removed duplicated region for block: B:157:0x0349  */
    /* JADX WARN: Removed duplicated region for block: B:173:0x03a2  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x03af A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static <T> zzhf<T> zza(Class<T> cls, zzgz zzgzVar, zzhg zzhgVar, zzgl zzglVar, zzij<?, ?> zzijVar, zzfg<?> zzfgVar, zzgu zzguVar) {
        int i;
        int i2;
        int[] iArr;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int[] iArr2;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        Field field;
        char charAt;
        int i18;
        Field field2;
        Field field3;
        int i19;
        char charAt2;
        int i20;
        char charAt3;
        int i21;
        char charAt4;
        int i22;
        char charAt5;
        int i23;
        char charAt6;
        int i24;
        char charAt7;
        int i25;
        char charAt8;
        int i26;
        char charAt9;
        int i27;
        char charAt10;
        int i28;
        char charAt11;
        int i29;
        char charAt12;
        int i30;
        char charAt13;
        if (zzgzVar instanceof zzho) {
            zzho zzhoVar = (zzho) zzgzVar;
            int i31 = 0;
            boolean z = zzhoVar.zza() == zzhn.zzb;
            String zzd = zzhoVar.zzd();
            int length = zzd.length();
            if (zzd.charAt(0) < 55296) {
                i = 1;
            } else {
                int i32 = 1;
                while (true) {
                    i = i32 + 1;
                    if (zzd.charAt(i32) < 55296) {
                        break;
                    }
                    i32 = i;
                }
            }
            int i33 = i + 1;
            int charAt14 = zzd.charAt(i);
            if (charAt14 >= 55296) {
                int i34 = charAt14 & 8191;
                int i35 = 13;
                while (true) {
                    i30 = i33 + 1;
                    charAt13 = zzd.charAt(i33);
                    if (charAt13 < 55296) {
                        break;
                    }
                    i34 |= (charAt13 & 8191) << i35;
                    i35 += 13;
                    i33 = i30;
                }
                charAt14 = i34 | (charAt13 << i35);
                i33 = i30;
            }
            if (charAt14 == 0) {
                iArr = zza;
                i7 = 0;
                i6 = 0;
                i5 = 0;
                i4 = 0;
                i3 = 0;
                i2 = 0;
            } else {
                int i36 = i33 + 1;
                int charAt15 = zzd.charAt(i33);
                if (charAt15 >= 55296) {
                    int i37 = charAt15 & 8191;
                    int i38 = 13;
                    while (true) {
                        i29 = i36 + 1;
                        charAt12 = zzd.charAt(i36);
                        if (charAt12 < 55296) {
                            break;
                        }
                        i37 |= (charAt12 & 8191) << i38;
                        i38 += 13;
                        i36 = i29;
                    }
                    charAt15 = i37 | (charAt12 << i38);
                    i36 = i29;
                }
                int i39 = i36 + 1;
                int charAt16 = zzd.charAt(i36);
                if (charAt16 >= 55296) {
                    int i40 = charAt16 & 8191;
                    int i41 = 13;
                    while (true) {
                        i28 = i39 + 1;
                        charAt11 = zzd.charAt(i39);
                        if (charAt11 < 55296) {
                            break;
                        }
                        i40 |= (charAt11 & 8191) << i41;
                        i41 += 13;
                        i39 = i28;
                    }
                    charAt16 = i40 | (charAt11 << i41);
                    i39 = i28;
                }
                int i42 = i39 + 1;
                i6 = zzd.charAt(i39);
                if (i6 >= 55296) {
                    int i43 = i6 & 8191;
                    int i44 = 13;
                    while (true) {
                        i27 = i42 + 1;
                        charAt10 = zzd.charAt(i42);
                        if (charAt10 < 55296) {
                            break;
                        }
                        i43 |= (charAt10 & 8191) << i44;
                        i44 += 13;
                        i42 = i27;
                    }
                    i6 = i43 | (charAt10 << i44);
                    i42 = i27;
                }
                int i45 = i42 + 1;
                i5 = zzd.charAt(i42);
                if (i5 >= 55296) {
                    int i46 = i5 & 8191;
                    int i47 = 13;
                    while (true) {
                        i26 = i45 + 1;
                        charAt9 = zzd.charAt(i45);
                        if (charAt9 < 55296) {
                            break;
                        }
                        i46 |= (charAt9 & 8191) << i47;
                        i47 += 13;
                        i45 = i26;
                    }
                    i5 = i46 | (charAt9 << i47);
                    i45 = i26;
                }
                int i48 = i45 + 1;
                i4 = zzd.charAt(i45);
                if (i4 >= 55296) {
                    int i49 = i4 & 8191;
                    int i50 = 13;
                    while (true) {
                        i25 = i48 + 1;
                        charAt8 = zzd.charAt(i48);
                        if (charAt8 < 55296) {
                            break;
                        }
                        i49 |= (charAt8 & 8191) << i50;
                        i50 += 13;
                        i48 = i25;
                    }
                    i4 = i49 | (charAt8 << i50);
                    i48 = i25;
                }
                int i51 = i48 + 1;
                i3 = zzd.charAt(i48);
                if (i3 >= 55296) {
                    int i52 = i3 & 8191;
                    int i53 = 13;
                    while (true) {
                        i24 = i51 + 1;
                        charAt7 = zzd.charAt(i51);
                        if (charAt7 < 55296) {
                            break;
                        }
                        i52 |= (charAt7 & 8191) << i53;
                        i53 += 13;
                        i51 = i24;
                    }
                    i3 = i52 | (charAt7 << i53);
                    i51 = i24;
                }
                int i54 = i51 + 1;
                int charAt17 = zzd.charAt(i51);
                if (charAt17 >= 55296) {
                    int i55 = charAt17 & 8191;
                    int i56 = 13;
                    while (true) {
                        i23 = i54 + 1;
                        charAt6 = zzd.charAt(i54);
                        if (charAt6 < 55296) {
                            break;
                        }
                        i55 |= (charAt6 & 8191) << i56;
                        i56 += 13;
                        i54 = i23;
                    }
                    charAt17 = i55 | (charAt6 << i56);
                    i54 = i23;
                }
                int i57 = i54 + 1;
                i2 = zzd.charAt(i54);
                if (i2 >= 55296) {
                    int i58 = i2 & 8191;
                    int i59 = i57;
                    int i60 = 13;
                    while (true) {
                        i22 = i59 + 1;
                        charAt5 = zzd.charAt(i59);
                        if (charAt5 < 55296) {
                            break;
                        }
                        i58 |= (charAt5 & 8191) << i60;
                        i60 += 13;
                        i59 = i22;
                    }
                    i2 = i58 | (charAt5 << i60);
                    i57 = i22;
                }
                i7 = (charAt15 << 1) + charAt16;
                iArr = new int[i2 + i3 + charAt17];
                i31 = charAt15;
                i33 = i57;
            }
            Unsafe unsafe = zzb;
            Object[] zze = zzhoVar.zze();
            Class<?> cls2 = zzhoVar.zzc().getClass();
            int[] iArr3 = new int[i4 * 3];
            Object[] objArr = new Object[i4 << 1];
            int i61 = i2 + i3;
            int i62 = i2;
            int i63 = i61;
            int i64 = 0;
            int i65 = 0;
            while (i33 < length) {
                int i66 = i33 + 1;
                int charAt18 = zzd.charAt(i33);
                int i67 = length;
                if (charAt18 < 55296) {
                    i8 = i2;
                    i9 = i66;
                } else {
                    int i68 = charAt18 & 8191;
                    int i69 = i66;
                    int i70 = 13;
                    while (true) {
                        i21 = i69 + 1;
                        charAt4 = zzd.charAt(i69);
                        i8 = i2;
                        if (charAt4 < 55296) {
                            break;
                        }
                        i68 |= (charAt4 & 8191) << i70;
                        i70 += 13;
                        i69 = i21;
                        i2 = i8;
                    }
                    charAt18 = i68 | (charAt4 << i70);
                    i9 = i21;
                }
                int i71 = i9 + 1;
                int charAt19 = zzd.charAt(i9);
                if (charAt19 < 55296) {
                    i10 = i5;
                    i11 = i71;
                } else {
                    int i72 = charAt19 & 8191;
                    int i73 = i71;
                    int i74 = 13;
                    while (true) {
                        i20 = i73 + 1;
                        charAt3 = zzd.charAt(i73);
                        i10 = i5;
                        if (charAt3 < 55296) {
                            break;
                        }
                        i72 |= (charAt3 & 8191) << i74;
                        i74 += 13;
                        i73 = i20;
                        i5 = i10;
                    }
                    charAt19 = i72 | (charAt3 << i74);
                    i11 = i20;
                }
                int i75 = charAt19 & 255;
                int i76 = i6;
                if ((charAt19 & 1024) != 0) {
                    iArr[i64] = i65;
                    i64++;
                }
                int i77 = i64;
                if (i75 >= 51) {
                    int i78 = i11 + 1;
                    int charAt20 = zzd.charAt(i11);
                    char c = CharacterCompat.MIN_HIGH_SURROGATE;
                    if (charAt20 >= 55296) {
                        int i79 = charAt20 & 8191;
                        int i80 = 13;
                        while (true) {
                            i19 = i78 + 1;
                            charAt2 = zzd.charAt(i78);
                            if (charAt2 < c) {
                                break;
                            }
                            i79 |= (charAt2 & 8191) << i80;
                            i80 += 13;
                            i78 = i19;
                            c = CharacterCompat.MIN_HIGH_SURROGATE;
                        }
                        charAt20 = i79 | (charAt2 << i80);
                        i78 = i19;
                    }
                    int i81 = i75 - 51;
                    int i82 = i78;
                    if (i81 == 9 || i81 == 17) {
                        objArr[((i65 / 3) << 1) + 1] = zze[i7];
                        i7++;
                    } else if (i81 == 12 && !z) {
                        objArr[((i65 / 3) << 1) + 1] = zze[i7];
                        i7++;
                    }
                    int i83 = charAt20 << 1;
                    Object obj = zze[i83];
                    if (obj instanceof Field) {
                        field2 = (Field) obj;
                    } else {
                        field2 = zza(cls2, (String) obj);
                        zze[i83] = field2;
                    }
                    iArr2 = iArr3;
                    i12 = charAt18;
                    int objectFieldOffset = (int) unsafe.objectFieldOffset(field2);
                    int i84 = i83 + 1;
                    Object obj2 = zze[i84];
                    if (obj2 instanceof Field) {
                        field3 = (Field) obj2;
                    } else {
                        field3 = zza(cls2, (String) obj2);
                        zze[i84] = field3;
                    }
                    i13 = i31;
                    i16 = (int) unsafe.objectFieldOffset(field3);
                    i15 = objectFieldOffset;
                    i11 = i82;
                    i14 = 0;
                } else {
                    iArr2 = iArr3;
                    i12 = charAt18;
                    int i85 = i7 + 1;
                    Field zza2 = zza(cls2, (String) zze[i7]);
                    if (i75 == 9) {
                        i18 = 1;
                    } else if (i75 == 17) {
                        i18 = 1;
                    } else {
                        if (i75 == 27 || i75 == 49) {
                            objArr[((i65 / 3) << 1) + 1] = zze[i85];
                            i85++;
                        } else if (i75 == 12 || i75 == 30 || i75 == 44) {
                            if (!z) {
                                objArr[((i65 / 3) << 1) + 1] = zze[i85];
                                i85++;
                            }
                        } else if (i75 == 50) {
                            int i86 = i62 + 1;
                            iArr[i62] = i65;
                            int i87 = (i65 / 3) << 1;
                            int i88 = i85 + 1;
                            objArr[i87] = zze[i85];
                            if ((charAt19 & 2048) == 0) {
                                i85 = i88;
                                i62 = i86;
                            } else {
                                i85 = i88 + 1;
                                objArr[i87 + 1] = zze[i88];
                                i62 = i86;
                            }
                        }
                        int i89 = i85;
                        i15 = (int) unsafe.objectFieldOffset(zza2);
                        if ((charAt19 & 4096) == 4096) {
                            i13 = i31;
                        } else if (i75 > 17) {
                            i13 = i31;
                        } else {
                            int i90 = i11 + 1;
                            int charAt21 = zzd.charAt(i11);
                            if (charAt21 < 55296) {
                                i17 = i90;
                            } else {
                                int i91 = charAt21 & 8191;
                                int i92 = 13;
                                while (true) {
                                    i17 = i90 + 1;
                                    charAt = zzd.charAt(i90);
                                    if (charAt < 55296) {
                                        break;
                                    }
                                    i91 |= (charAt & 8191) << i92;
                                    i92 += 13;
                                    i90 = i17;
                                }
                                charAt21 = i91 | (charAt << i92);
                            }
                            int i93 = (i31 << 1) + (charAt21 / 32);
                            Object obj3 = zze[i93];
                            if (obj3 instanceof Field) {
                                field = (Field) obj3;
                            } else {
                                field = zza(cls2, (String) obj3);
                                zze[i93] = field;
                            }
                            i13 = i31;
                            i16 = (int) unsafe.objectFieldOffset(field);
                            i14 = charAt21 % 32;
                            i11 = i17;
                            if (i75 < 18 && i75 <= 49) {
                                iArr[i63] = i15;
                                i63++;
                                i7 = i89;
                            } else {
                                i7 = i89;
                            }
                        }
                        i16 = 1048575;
                        i14 = 0;
                        if (i75 < 18) {
                        }
                        i7 = i89;
                    }
                    objArr[((i65 / 3) << i18) + i18] = zza2.getType();
                    int i892 = i85;
                    i15 = (int) unsafe.objectFieldOffset(zza2);
                    if ((charAt19 & 4096) == 4096) {
                    }
                    i16 = 1048575;
                    i14 = 0;
                    if (i75 < 18) {
                    }
                    i7 = i892;
                }
                int i94 = i65 + 1;
                iArr2[i65] = i12;
                int i95 = i94 + 1;
                String str = zzd;
                iArr2[i94] = ((charAt19 & 512) != 0 ? 536870912 : 0) | ((charAt19 & 256) != 0 ? 268435456 : 0) | (i75 << 20) | i15;
                int i96 = i95 + 1;
                iArr2[i95] = (i14 << 20) | i16;
                i33 = i11;
                i31 = i13;
                i6 = i76;
                length = i67;
                i2 = i8;
                i5 = i10;
                i64 = i77;
                iArr3 = iArr2;
                i65 = i96;
                zzd = str;
            }
            return new zzhf<>(iArr3, objArr, i6, i5, zzhoVar.zzc(), z, false, iArr, i2, i61, zzhgVar, zzglVar, zzijVar, zzfgVar, zzguVar);
        }
        ((zzic) zzgzVar).zza();
        int i97 = zzhn.zzb;
        throw new NoSuchMethodError();
    }

    private static Field zza(Class<?> cls, String str) {
        try {
            return cls.getDeclaredField(str);
        } catch (NoSuchFieldException e) {
            Field[] declaredFields = cls.getDeclaredFields();
            for (Field field : declaredFields) {
                if (str.equals(field.getName())) {
                    return field;
                }
            }
            String name = cls.getName();
            String arrays = Arrays.toString(declaredFields);
            StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 40 + String.valueOf(name).length() + String.valueOf(arrays).length());
            sb.append("Field ");
            sb.append(str);
            sb.append(" for ");
            sb.append(name);
            sb.append(" not found. Known fields are ");
            sb.append(arrays);
            throw new RuntimeException(sb.toString());
        }
    }

    @Override // com.google.android.gms.internal.mlkit_common.zzhr
    public final boolean zza(T t, T t2) {
        int length = this.zzc.length;
        int i = 0;
        while (true) {
            boolean z = true;
            if (i < length) {
                int zzc = zzc(i);
                long j = zzc & 1048575;
                switch ((zzc & 267386880) >>> 20) {
                    case 0:
                        if (!zzc(t, t2, i) || Double.doubleToLongBits(zzip.zze(t, j)) != Double.doubleToLongBits(zzip.zze(t2, j))) {
                            z = false;
                            break;
                        }
                        break;
                    case 1:
                        if (!zzc(t, t2, i) || Float.floatToIntBits(zzip.zzd(t, j)) != Float.floatToIntBits(zzip.zzd(t2, j))) {
                            z = false;
                            break;
                        }
                        break;
                    case 2:
                        if (!zzc(t, t2, i) || zzip.zzb(t, j) != zzip.zzb(t2, j)) {
                            z = false;
                            break;
                        }
                        break;
                    case 3:
                        if (!zzc(t, t2, i) || zzip.zzb(t, j) != zzip.zzb(t2, j)) {
                            z = false;
                            break;
                        }
                        break;
                    case 4:
                        if (!zzc(t, t2, i) || zzip.zza(t, j) != zzip.zza(t2, j)) {
                            z = false;
                            break;
                        }
                        break;
                    case 5:
                        if (!zzc(t, t2, i) || zzip.zzb(t, j) != zzip.zzb(t2, j)) {
                            z = false;
                            break;
                        }
                        break;
                    case 6:
                        if (!zzc(t, t2, i) || zzip.zza(t, j) != zzip.zza(t2, j)) {
                            z = false;
                            break;
                        }
                        break;
                    case 7:
                        if (!zzc(t, t2, i) || zzip.zzc(t, j) != zzip.zzc(t2, j)) {
                            z = false;
                            break;
                        }
                        break;
                    case 8:
                        if (!zzc(t, t2, i) || !zzht.zza(zzip.zzf(t, j), zzip.zzf(t2, j))) {
                            z = false;
                            break;
                        }
                        break;
                    case 9:
                        if (!zzc(t, t2, i) || !zzht.zza(zzip.zzf(t, j), zzip.zzf(t2, j))) {
                            z = false;
                            break;
                        }
                        break;
                    case 10:
                        if (!zzc(t, t2, i) || !zzht.zza(zzip.zzf(t, j), zzip.zzf(t2, j))) {
                            z = false;
                            break;
                        }
                        break;
                    case 11:
                        if (!zzc(t, t2, i) || zzip.zza(t, j) != zzip.zza(t2, j)) {
                            z = false;
                            break;
                        }
                        break;
                    case 12:
                        if (!zzc(t, t2, i) || zzip.zza(t, j) != zzip.zza(t2, j)) {
                            z = false;
                            break;
                        }
                        break;
                    case 13:
                        if (!zzc(t, t2, i) || zzip.zza(t, j) != zzip.zza(t2, j)) {
                            z = false;
                            break;
                        }
                        break;
                    case 14:
                        if (!zzc(t, t2, i) || zzip.zzb(t, j) != zzip.zzb(t2, j)) {
                            z = false;
                            break;
                        }
                        break;
                    case 15:
                        if (!zzc(t, t2, i) || zzip.zza(t, j) != zzip.zza(t2, j)) {
                            z = false;
                            break;
                        }
                        break;
                    case 16:
                        if (!zzc(t, t2, i) || zzip.zzb(t, j) != zzip.zzb(t2, j)) {
                            z = false;
                            break;
                        }
                        break;
                    case 17:
                        if (!zzc(t, t2, i) || !zzht.zza(zzip.zzf(t, j), zzip.zzf(t2, j))) {
                            z = false;
                            break;
                        }
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
                    case NalUnitTypes.NAL_TYPE_RSV_VCL29 /* 29 */:
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
                        z = zzht.zza(zzip.zzf(t, j), zzip.zzf(t2, j));
                        break;
                    case 50:
                        z = zzht.zza(zzip.zzf(t, j), zzip.zzf(t2, j));
                        break;
                    case 51:
                    case 52:
                    case 53:
                    case 54:
                    case 55:
                    case UndoView.ACTION_USERNAME_COPIED /* 56 */:
                    case UndoView.ACTION_HASHTAG_COPIED /* 57 */:
                    case UndoView.ACTION_TEXT_COPIED /* 58 */:
                    case 59:
                    case UndoView.ACTION_PHONE_COPIED /* 60 */:
                    case UndoView.ACTION_SHARE_BACKGROUND /* 61 */:
                    case 62:
                    case HtmlCompat.FROM_HTML_MODE_COMPACT /* 63 */:
                    case 64:
                    case VoIPService.CALL_MIN_LAYER /* 65 */:
                    case 66:
                    case 67:
                    case 68:
                        long zzd = zzd(i) & 1048575;
                        if (zzip.zza(t, zzd) != zzip.zza(t2, zzd) || !zzht.zza(zzip.zzf(t, j), zzip.zzf(t2, j))) {
                            z = false;
                            break;
                        }
                        break;
                }
                if (!z) {
                    return false;
                }
                i += 3;
            } else if (!this.zzq.zza(t).equals(this.zzq.zza(t2))) {
                return false;
            } else {
                if (!this.zzh) {
                    return true;
                }
                return this.zzr.zza(t).equals(this.zzr.zza(t2));
            }
        }
    }

    @Override // com.google.android.gms.internal.mlkit_common.zzhr
    public final int zza(T t) {
        int length = this.zzc.length;
        int i = 0;
        for (int i2 = 0; i2 < length; i2 += 3) {
            int zzc = zzc(i2);
            int i3 = this.zzc[i2];
            long j = 1048575 & zzc;
            int i4 = 37;
            switch ((zzc & 267386880) >>> 20) {
                case 0:
                    i = (i * 53) + zzfs.zza(Double.doubleToLongBits(zzip.zze(t, j)));
                    break;
                case 1:
                    i = (i * 53) + Float.floatToIntBits(zzip.zzd(t, j));
                    break;
                case 2:
                    i = (i * 53) + zzfs.zza(zzip.zzb(t, j));
                    break;
                case 3:
                    i = (i * 53) + zzfs.zza(zzip.zzb(t, j));
                    break;
                case 4:
                    i = (i * 53) + zzip.zza(t, j);
                    break;
                case 5:
                    i = (i * 53) + zzfs.zza(zzip.zzb(t, j));
                    break;
                case 6:
                    i = (i * 53) + zzip.zza(t, j);
                    break;
                case 7:
                    i = (i * 53) + zzfs.zza(zzip.zzc(t, j));
                    break;
                case 8:
                    i = (i * 53) + ((String) zzip.zzf(t, j)).hashCode();
                    break;
                case 9:
                    Object zzf = zzip.zzf(t, j);
                    if (zzf != null) {
                        i4 = zzf.hashCode();
                    }
                    i = (i * 53) + i4;
                    break;
                case 10:
                    i = (i * 53) + zzip.zzf(t, j).hashCode();
                    break;
                case 11:
                    i = (i * 53) + zzip.zza(t, j);
                    break;
                case 12:
                    i = (i * 53) + zzip.zza(t, j);
                    break;
                case 13:
                    i = (i * 53) + zzip.zza(t, j);
                    break;
                case 14:
                    i = (i * 53) + zzfs.zza(zzip.zzb(t, j));
                    break;
                case 15:
                    i = (i * 53) + zzip.zza(t, j);
                    break;
                case 16:
                    i = (i * 53) + zzfs.zza(zzip.zzb(t, j));
                    break;
                case 17:
                    Object zzf2 = zzip.zzf(t, j);
                    if (zzf2 != null) {
                        i4 = zzf2.hashCode();
                    }
                    i = (i * 53) + i4;
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
                case NalUnitTypes.NAL_TYPE_RSV_VCL29 /* 29 */:
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
                    i = (i * 53) + zzip.zzf(t, j).hashCode();
                    break;
                case 50:
                    i = (i * 53) + zzip.zzf(t, j).hashCode();
                    break;
                case 51:
                    if (zza((zzhf<T>) t, i3, i2)) {
                        i = (i * 53) + zzfs.zza(Double.doubleToLongBits(zzb(t, j)));
                        break;
                    } else {
                        break;
                    }
                case 52:
                    if (zza((zzhf<T>) t, i3, i2)) {
                        i = (i * 53) + Float.floatToIntBits(zzc(t, j));
                        break;
                    } else {
                        break;
                    }
                case 53:
                    if (zza((zzhf<T>) t, i3, i2)) {
                        i = (i * 53) + zzfs.zza(zze(t, j));
                        break;
                    } else {
                        break;
                    }
                case 54:
                    if (zza((zzhf<T>) t, i3, i2)) {
                        i = (i * 53) + zzfs.zza(zze(t, j));
                        break;
                    } else {
                        break;
                    }
                case 55:
                    if (zza((zzhf<T>) t, i3, i2)) {
                        i = (i * 53) + zzd(t, j);
                        break;
                    } else {
                        break;
                    }
                case UndoView.ACTION_USERNAME_COPIED /* 56 */:
                    if (zza((zzhf<T>) t, i3, i2)) {
                        i = (i * 53) + zzfs.zza(zze(t, j));
                        break;
                    } else {
                        break;
                    }
                case UndoView.ACTION_HASHTAG_COPIED /* 57 */:
                    if (zza((zzhf<T>) t, i3, i2)) {
                        i = (i * 53) + zzd(t, j);
                        break;
                    } else {
                        break;
                    }
                case UndoView.ACTION_TEXT_COPIED /* 58 */:
                    if (zza((zzhf<T>) t, i3, i2)) {
                        i = (i * 53) + zzfs.zza(zzf(t, j));
                        break;
                    } else {
                        break;
                    }
                case 59:
                    if (zza((zzhf<T>) t, i3, i2)) {
                        i = (i * 53) + ((String) zzip.zzf(t, j)).hashCode();
                        break;
                    } else {
                        break;
                    }
                case UndoView.ACTION_PHONE_COPIED /* 60 */:
                    if (zza((zzhf<T>) t, i3, i2)) {
                        i = (i * 53) + zzip.zzf(t, j).hashCode();
                        break;
                    } else {
                        break;
                    }
                case UndoView.ACTION_SHARE_BACKGROUND /* 61 */:
                    if (zza((zzhf<T>) t, i3, i2)) {
                        i = (i * 53) + zzip.zzf(t, j).hashCode();
                        break;
                    } else {
                        break;
                    }
                case 62:
                    if (zza((zzhf<T>) t, i3, i2)) {
                        i = (i * 53) + zzd(t, j);
                        break;
                    } else {
                        break;
                    }
                case HtmlCompat.FROM_HTML_MODE_COMPACT /* 63 */:
                    if (zza((zzhf<T>) t, i3, i2)) {
                        i = (i * 53) + zzd(t, j);
                        break;
                    } else {
                        break;
                    }
                case 64:
                    if (zza((zzhf<T>) t, i3, i2)) {
                        i = (i * 53) + zzd(t, j);
                        break;
                    } else {
                        break;
                    }
                case VoIPService.CALL_MIN_LAYER /* 65 */:
                    if (zza((zzhf<T>) t, i3, i2)) {
                        i = (i * 53) + zzfs.zza(zze(t, j));
                        break;
                    } else {
                        break;
                    }
                case 66:
                    if (zza((zzhf<T>) t, i3, i2)) {
                        i = (i * 53) + zzd(t, j);
                        break;
                    } else {
                        break;
                    }
                case 67:
                    if (zza((zzhf<T>) t, i3, i2)) {
                        i = (i * 53) + zzfs.zza(zze(t, j));
                        break;
                    } else {
                        break;
                    }
                case 68:
                    if (zza((zzhf<T>) t, i3, i2)) {
                        i = (i * 53) + zzip.zzf(t, j).hashCode();
                        break;
                    } else {
                        break;
                    }
            }
        }
        int hashCode = (i * 53) + this.zzq.zza(t).hashCode();
        if (this.zzh) {
            return (hashCode * 53) + this.zzr.zza(t).hashCode();
        }
        return hashCode;
    }

    @Override // com.google.android.gms.internal.mlkit_common.zzhr
    public final void zzb(T t, T t2) {
        if (t2 == null) {
            throw new NullPointerException();
        }
        for (int i = 0; i < this.zzc.length; i += 3) {
            int zzc = zzc(i);
            long j = 1048575 & zzc;
            int i2 = this.zzc[i];
            switch ((zzc & 267386880) >>> 20) {
                case 0:
                    if (zza((zzhf<T>) t2, i)) {
                        zzip.zza(t, j, zzip.zze(t2, j));
                        zzb((zzhf<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 1:
                    if (zza((zzhf<T>) t2, i)) {
                        zzip.zza((Object) t, j, zzip.zzd(t2, j));
                        zzb((zzhf<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 2:
                    if (zza((zzhf<T>) t2, i)) {
                        zzip.zza((Object) t, j, zzip.zzb(t2, j));
                        zzb((zzhf<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 3:
                    if (zza((zzhf<T>) t2, i)) {
                        zzip.zza((Object) t, j, zzip.zzb(t2, j));
                        zzb((zzhf<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 4:
                    if (zza((zzhf<T>) t2, i)) {
                        zzip.zza((Object) t, j, zzip.zza(t2, j));
                        zzb((zzhf<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 5:
                    if (zza((zzhf<T>) t2, i)) {
                        zzip.zza((Object) t, j, zzip.zzb(t2, j));
                        zzb((zzhf<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 6:
                    if (zza((zzhf<T>) t2, i)) {
                        zzip.zza((Object) t, j, zzip.zza(t2, j));
                        zzb((zzhf<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 7:
                    if (zza((zzhf<T>) t2, i)) {
                        zzip.zza(t, j, zzip.zzc(t2, j));
                        zzb((zzhf<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 8:
                    if (zza((zzhf<T>) t2, i)) {
                        zzip.zza(t, j, zzip.zzf(t2, j));
                        zzb((zzhf<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 9:
                    zza(t, t2, i);
                    break;
                case 10:
                    if (zza((zzhf<T>) t2, i)) {
                        zzip.zza(t, j, zzip.zzf(t2, j));
                        zzb((zzhf<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 11:
                    if (zza((zzhf<T>) t2, i)) {
                        zzip.zza((Object) t, j, zzip.zza(t2, j));
                        zzb((zzhf<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 12:
                    if (zza((zzhf<T>) t2, i)) {
                        zzip.zza((Object) t, j, zzip.zza(t2, j));
                        zzb((zzhf<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 13:
                    if (zza((zzhf<T>) t2, i)) {
                        zzip.zza((Object) t, j, zzip.zza(t2, j));
                        zzb((zzhf<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 14:
                    if (zza((zzhf<T>) t2, i)) {
                        zzip.zza((Object) t, j, zzip.zzb(t2, j));
                        zzb((zzhf<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 15:
                    if (zza((zzhf<T>) t2, i)) {
                        zzip.zza((Object) t, j, zzip.zza(t2, j));
                        zzb((zzhf<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 16:
                    if (zza((zzhf<T>) t2, i)) {
                        zzip.zza((Object) t, j, zzip.zzb(t2, j));
                        zzb((zzhf<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 17:
                    zza(t, t2, i);
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
                case NalUnitTypes.NAL_TYPE_RSV_VCL29 /* 29 */:
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
                    this.zzp.zza(t, t2, j);
                    break;
                case 50:
                    zzht.zza(this.zzs, t, t2, j);
                    break;
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                case UndoView.ACTION_USERNAME_COPIED /* 56 */:
                case UndoView.ACTION_HASHTAG_COPIED /* 57 */:
                case UndoView.ACTION_TEXT_COPIED /* 58 */:
                case 59:
                    if (zza((zzhf<T>) t2, i2, i)) {
                        zzip.zza(t, j, zzip.zzf(t2, j));
                        zzb((zzhf<T>) t, i2, i);
                        break;
                    } else {
                        break;
                    }
                case UndoView.ACTION_PHONE_COPIED /* 60 */:
                    zzb(t, t2, i);
                    break;
                case UndoView.ACTION_SHARE_BACKGROUND /* 61 */:
                case 62:
                case HtmlCompat.FROM_HTML_MODE_COMPACT /* 63 */:
                case 64:
                case VoIPService.CALL_MIN_LAYER /* 65 */:
                case 66:
                case 67:
                    if (zza((zzhf<T>) t2, i2, i)) {
                        zzip.zza(t, j, zzip.zzf(t2, j));
                        zzb((zzhf<T>) t, i2, i);
                        break;
                    } else {
                        break;
                    }
                case 68:
                    zzb(t, t2, i);
                    break;
            }
        }
        zzht.zza(this.zzq, t, t2);
        if (this.zzh) {
            zzht.zza(this.zzr, t, t2);
        }
    }

    private final void zza(T t, T t2, int i) {
        long zzc = zzc(i) & 1048575;
        if (!zza((zzhf<T>) t2, i)) {
            return;
        }
        Object zzf = zzip.zzf(t, zzc);
        Object zzf2 = zzip.zzf(t2, zzc);
        if (zzf != null && zzf2 != null) {
            zzip.zza(t, zzc, zzfs.zza(zzf, zzf2));
            zzb((zzhf<T>) t, i);
        } else if (zzf2 != null) {
            zzip.zza(t, zzc, zzf2);
            zzb((zzhf<T>) t, i);
        }
    }

    private final void zzb(T t, T t2, int i) {
        int zzc = zzc(i);
        int i2 = this.zzc[i];
        long j = zzc & 1048575;
        if (!zza((zzhf<T>) t2, i2, i)) {
            return;
        }
        Object zzf = zzip.zzf(t, j);
        Object zzf2 = zzip.zzf(t2, j);
        if (zzf != null && zzf2 != null) {
            zzip.zza(t, j, zzfs.zza(zzf, zzf2));
            zzb((zzhf<T>) t, i2, i);
        } else if (zzf2 != null) {
            zzip.zza(t, j, zzf2);
            zzb((zzhf<T>) t, i2, i);
        }
    }

    @Override // com.google.android.gms.internal.mlkit_common.zzhr
    public final int zzd(T t) {
        int i;
        long j;
        int i2 = 267386880;
        int i3 = 1048575;
        int i4 = 1;
        if (this.zzj) {
            Unsafe unsafe = zzb;
            int i5 = 0;
            int i6 = 0;
            while (i5 < this.zzc.length) {
                int zzc = zzc(i5);
                int i7 = (zzc & i2) >>> 20;
                int i8 = this.zzc[i5];
                long j2 = zzc & 1048575;
                if (i7 >= zzfm.DOUBLE_LIST_PACKED.zza() && i7 <= zzfm.SINT64_LIST_PACKED.zza()) {
                    int i9 = this.zzc[i5 + 2];
                }
                switch (i7) {
                    case 0:
                        if (zza((zzhf<T>) t, i5)) {
                            i6 += zzfc.zzb(i8, (double) FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE);
                            break;
                        } else {
                            break;
                        }
                    case 1:
                        if (zza((zzhf<T>) t, i5)) {
                            i6 += zzfc.zzb(i8, 0.0f);
                            break;
                        } else {
                            break;
                        }
                    case 2:
                        if (zza((zzhf<T>) t, i5)) {
                            i6 += zzfc.zzd(i8, zzip.zzb(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 3:
                        if (zza((zzhf<T>) t, i5)) {
                            i6 += zzfc.zze(i8, zzip.zzb(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 4:
                        if (zza((zzhf<T>) t, i5)) {
                            i6 += zzfc.zzf(i8, zzip.zza(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 5:
                        if (zza((zzhf<T>) t, i5)) {
                            i6 += zzfc.zzg(i8, 0L);
                            break;
                        } else {
                            break;
                        }
                    case 6:
                        if (zza((zzhf<T>) t, i5)) {
                            i6 += zzfc.zzi(i8, 0);
                            break;
                        } else {
                            break;
                        }
                    case 7:
                        if (zza((zzhf<T>) t, i5)) {
                            i6 += zzfc.zzb(i8, true);
                            break;
                        } else {
                            break;
                        }
                    case 8:
                        if (zza((zzhf<T>) t, i5)) {
                            Object zzf = zzip.zzf(t, j2);
                            if (zzf instanceof zzep) {
                                i6 += zzfc.zzc(i8, (zzep) zzf);
                                break;
                            } else {
                                i6 += zzfc.zzb(i8, (String) zzf);
                                break;
                            }
                        } else {
                            break;
                        }
                    case 9:
                        if (zza((zzhf<T>) t, i5)) {
                            i6 += zzht.zza(i8, zzip.zzf(t, j2), zza(i5));
                            break;
                        } else {
                            break;
                        }
                    case 10:
                        if (zza((zzhf<T>) t, i5)) {
                            i6 += zzfc.zzc(i8, (zzep) zzip.zzf(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 11:
                        if (zza((zzhf<T>) t, i5)) {
                            i6 += zzfc.zzg(i8, zzip.zza(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 12:
                        if (zza((zzhf<T>) t, i5)) {
                            i6 += zzfc.zzk(i8, zzip.zza(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 13:
                        if (zza((zzhf<T>) t, i5)) {
                            i6 += zzfc.zzj(i8, 0);
                            break;
                        } else {
                            break;
                        }
                    case 14:
                        if (zza((zzhf<T>) t, i5)) {
                            i6 += zzfc.zzh(i8, 0L);
                            break;
                        } else {
                            break;
                        }
                    case 15:
                        if (zza((zzhf<T>) t, i5)) {
                            i6 += zzfc.zzh(i8, zzip.zza(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 16:
                        if (zza((zzhf<T>) t, i5)) {
                            i6 += zzfc.zzf(i8, zzip.zzb(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 17:
                        if (zza((zzhf<T>) t, i5)) {
                            i6 += zzfc.zzc(i8, (zzhb) zzip.zzf(t, j2), zza(i5));
                            break;
                        } else {
                            break;
                        }
                    case 18:
                        i6 += zzht.zzi(i8, zza(t, j2), false);
                        break;
                    case 19:
                        i6 += zzht.zzh(i8, zza(t, j2), false);
                        break;
                    case 20:
                        i6 += zzht.zza(i8, (List<Long>) zza(t, j2), false);
                        break;
                    case 21:
                        i6 += zzht.zzb(i8, (List<Long>) zza(t, j2), false);
                        break;
                    case 22:
                        i6 += zzht.zze(i8, zza(t, j2), false);
                        break;
                    case 23:
                        i6 += zzht.zzi(i8, zza(t, j2), false);
                        break;
                    case 24:
                        i6 += zzht.zzh(i8, zza(t, j2), false);
                        break;
                    case 25:
                        i6 += zzht.zzj(i8, zza(t, j2), false);
                        break;
                    case 26:
                        i6 += zzht.zza(i8, zza(t, j2));
                        break;
                    case 27:
                        i6 += zzht.zza(i8, zza(t, j2), zza(i5));
                        break;
                    case 28:
                        i6 += zzht.zzb(i8, zza(t, j2));
                        break;
                    case NalUnitTypes.NAL_TYPE_RSV_VCL29 /* 29 */:
                        i6 += zzht.zzf(i8, zza(t, j2), false);
                        break;
                    case 30:
                        i6 += zzht.zzd(i8, zza(t, j2), false);
                        break;
                    case 31:
                        i6 += zzht.zzh(i8, zza(t, j2), false);
                        break;
                    case 32:
                        i6 += zzht.zzi(i8, zza(t, j2), false);
                        break;
                    case 33:
                        i6 += zzht.zzg(i8, zza(t, j2), false);
                        break;
                    case 34:
                        i6 += zzht.zzc(i8, zza(t, j2), false);
                        break;
                    case 35:
                        int zzi = zzht.zzi((List) unsafe.getObject(t, j2));
                        if (zzi > 0) {
                            i6 += zzfc.zze(i8) + zzfc.zzg(zzi) + zzi;
                            break;
                        } else {
                            break;
                        }
                    case 36:
                        int zzh = zzht.zzh((List) unsafe.getObject(t, j2));
                        if (zzh > 0) {
                            i6 += zzfc.zze(i8) + zzfc.zzg(zzh) + zzh;
                            break;
                        } else {
                            break;
                        }
                    case 37:
                        int zza2 = zzht.zza((List) unsafe.getObject(t, j2));
                        if (zza2 > 0) {
                            i6 += zzfc.zze(i8) + zzfc.zzg(zza2) + zza2;
                            break;
                        } else {
                            break;
                        }
                    case 38:
                        int zzb2 = zzht.zzb((List) unsafe.getObject(t, j2));
                        if (zzb2 > 0) {
                            i6 += zzfc.zze(i8) + zzfc.zzg(zzb2) + zzb2;
                            break;
                        } else {
                            break;
                        }
                    case 39:
                        int zze = zzht.zze((List) unsafe.getObject(t, j2));
                        if (zze > 0) {
                            i6 += zzfc.zze(i8) + zzfc.zzg(zze) + zze;
                            break;
                        } else {
                            break;
                        }
                    case 40:
                        int zzi2 = zzht.zzi((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            i6 += zzfc.zze(i8) + zzfc.zzg(zzi2) + zzi2;
                            break;
                        } else {
                            break;
                        }
                    case 41:
                        int zzh2 = zzht.zzh((List) unsafe.getObject(t, j2));
                        if (zzh2 > 0) {
                            i6 += zzfc.zze(i8) + zzfc.zzg(zzh2) + zzh2;
                            break;
                        } else {
                            break;
                        }
                    case 42:
                        int zzj = zzht.zzj((List) unsafe.getObject(t, j2));
                        if (zzj > 0) {
                            i6 += zzfc.zze(i8) + zzfc.zzg(zzj) + zzj;
                            break;
                        } else {
                            break;
                        }
                    case 43:
                        int zzf2 = zzht.zzf((List) unsafe.getObject(t, j2));
                        if (zzf2 > 0) {
                            i6 += zzfc.zze(i8) + zzfc.zzg(zzf2) + zzf2;
                            break;
                        } else {
                            break;
                        }
                    case 44:
                        int zzd = zzht.zzd((List) unsafe.getObject(t, j2));
                        if (zzd > 0) {
                            i6 += zzfc.zze(i8) + zzfc.zzg(zzd) + zzd;
                            break;
                        } else {
                            break;
                        }
                    case 45:
                        int zzh3 = zzht.zzh((List) unsafe.getObject(t, j2));
                        if (zzh3 > 0) {
                            i6 += zzfc.zze(i8) + zzfc.zzg(zzh3) + zzh3;
                            break;
                        } else {
                            break;
                        }
                    case 46:
                        int zzi3 = zzht.zzi((List) unsafe.getObject(t, j2));
                        if (zzi3 > 0) {
                            i6 += zzfc.zze(i8) + zzfc.zzg(zzi3) + zzi3;
                            break;
                        } else {
                            break;
                        }
                    case 47:
                        int zzg = zzht.zzg((List) unsafe.getObject(t, j2));
                        if (zzg > 0) {
                            i6 += zzfc.zze(i8) + zzfc.zzg(zzg) + zzg;
                            break;
                        } else {
                            break;
                        }
                    case 48:
                        int zzc2 = zzht.zzc((List) unsafe.getObject(t, j2));
                        if (zzc2 > 0) {
                            i6 += zzfc.zze(i8) + zzfc.zzg(zzc2) + zzc2;
                            break;
                        } else {
                            break;
                        }
                    case 49:
                        i6 += zzht.zzb(i8, (List<zzhb>) zza(t, j2), zza(i5));
                        break;
                    case 50:
                        i6 += this.zzs.zza(i8, zzip.zzf(t, j2), zzb(i5));
                        break;
                    case 51:
                        if (zza((zzhf<T>) t, i8, i5)) {
                            i6 += zzfc.zzb(i8, (double) FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE);
                            break;
                        } else {
                            break;
                        }
                    case 52:
                        if (zza((zzhf<T>) t, i8, i5)) {
                            i6 += zzfc.zzb(i8, 0.0f);
                            break;
                        } else {
                            break;
                        }
                    case 53:
                        if (zza((zzhf<T>) t, i8, i5)) {
                            i6 += zzfc.zzd(i8, zze(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 54:
                        if (zza((zzhf<T>) t, i8, i5)) {
                            i6 += zzfc.zze(i8, zze(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 55:
                        if (zza((zzhf<T>) t, i8, i5)) {
                            i6 += zzfc.zzf(i8, zzd(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case UndoView.ACTION_USERNAME_COPIED /* 56 */:
                        if (zza((zzhf<T>) t, i8, i5)) {
                            i6 += zzfc.zzg(i8, 0L);
                            break;
                        } else {
                            break;
                        }
                    case UndoView.ACTION_HASHTAG_COPIED /* 57 */:
                        if (zza((zzhf<T>) t, i8, i5)) {
                            i6 += zzfc.zzi(i8, 0);
                            break;
                        } else {
                            break;
                        }
                    case UndoView.ACTION_TEXT_COPIED /* 58 */:
                        if (zza((zzhf<T>) t, i8, i5)) {
                            i6 += zzfc.zzb(i8, true);
                            break;
                        } else {
                            break;
                        }
                    case 59:
                        if (zza((zzhf<T>) t, i8, i5)) {
                            Object zzf3 = zzip.zzf(t, j2);
                            if (zzf3 instanceof zzep) {
                                i6 += zzfc.zzc(i8, (zzep) zzf3);
                                break;
                            } else {
                                i6 += zzfc.zzb(i8, (String) zzf3);
                                break;
                            }
                        } else {
                            break;
                        }
                    case UndoView.ACTION_PHONE_COPIED /* 60 */:
                        if (zza((zzhf<T>) t, i8, i5)) {
                            i6 += zzht.zza(i8, zzip.zzf(t, j2), zza(i5));
                            break;
                        } else {
                            break;
                        }
                    case UndoView.ACTION_SHARE_BACKGROUND /* 61 */:
                        if (zza((zzhf<T>) t, i8, i5)) {
                            i6 += zzfc.zzc(i8, (zzep) zzip.zzf(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 62:
                        if (zza((zzhf<T>) t, i8, i5)) {
                            i6 += zzfc.zzg(i8, zzd(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case HtmlCompat.FROM_HTML_MODE_COMPACT /* 63 */:
                        if (zza((zzhf<T>) t, i8, i5)) {
                            i6 += zzfc.zzk(i8, zzd(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 64:
                        if (zza((zzhf<T>) t, i8, i5)) {
                            i6 += zzfc.zzj(i8, 0);
                            break;
                        } else {
                            break;
                        }
                    case VoIPService.CALL_MIN_LAYER /* 65 */:
                        if (zza((zzhf<T>) t, i8, i5)) {
                            i6 += zzfc.zzh(i8, 0L);
                            break;
                        } else {
                            break;
                        }
                    case 66:
                        if (zza((zzhf<T>) t, i8, i5)) {
                            i6 += zzfc.zzh(i8, zzd(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 67:
                        if (zza((zzhf<T>) t, i8, i5)) {
                            i6 += zzfc.zzf(i8, zze(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 68:
                        if (zza((zzhf<T>) t, i8, i5)) {
                            i6 += zzfc.zzc(i8, (zzhb) zzip.zzf(t, j2), zza(i5));
                            break;
                        } else {
                            break;
                        }
                }
                i5 += 3;
                i2 = 267386880;
            }
            return i6 + zza((zzij) this.zzq, (Object) t);
        }
        Unsafe unsafe2 = zzb;
        int i10 = 0;
        int i11 = 0;
        int i12 = 1048575;
        int i13 = 0;
        while (i10 < this.zzc.length) {
            int zzc3 = zzc(i10);
            int[] iArr = this.zzc;
            int i14 = iArr[i10];
            int i15 = (zzc3 & 267386880) >>> 20;
            if (i15 > 17) {
                i = 0;
            } else {
                int i16 = iArr[i10 + 2];
                int i17 = i16 & i3;
                i = i4 << (i16 >>> 20);
                if (i17 != i12) {
                    i13 = unsafe2.getInt(t, i17);
                    i12 = i17;
                }
            }
            long j3 = zzc3 & i3;
            switch (i15) {
                case 0:
                    j = 0;
                    if ((i13 & i) != 0) {
                        i11 += zzfc.zzb(i14, (double) FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE);
                        break;
                    } else {
                        break;
                    }
                case 1:
                    j = 0;
                    if ((i13 & i) != 0) {
                        i11 += zzfc.zzb(i14, 0.0f);
                        break;
                    } else {
                        break;
                    }
                case 2:
                    j = 0;
                    if ((i & i13) != 0) {
                        i11 += zzfc.zzd(i14, unsafe2.getLong(t, j3));
                        break;
                    } else {
                        break;
                    }
                case 3:
                    j = 0;
                    if ((i & i13) != 0) {
                        i11 += zzfc.zze(i14, unsafe2.getLong(t, j3));
                        break;
                    } else {
                        break;
                    }
                case 4:
                    j = 0;
                    if ((i & i13) != 0) {
                        i11 += zzfc.zzf(i14, unsafe2.getInt(t, j3));
                        break;
                    } else {
                        break;
                    }
                case 5:
                    if ((i13 & i) == 0) {
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        i11 += zzfc.zzg(i14, 0L);
                        break;
                    }
                case 6:
                    if ((i13 & i) != 0) {
                        i11 += zzfc.zzi(i14, 0);
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 7:
                    if ((i13 & i) != 0) {
                        i11 += zzfc.zzb(i14, true);
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 8:
                    if ((i13 & i) != 0) {
                        Object object = unsafe2.getObject(t, j3);
                        if (object instanceof zzep) {
                            i11 += zzfc.zzc(i14, (zzep) object);
                            j = 0;
                            break;
                        } else {
                            i11 += zzfc.zzb(i14, (String) object);
                            j = 0;
                            break;
                        }
                    } else {
                        j = 0;
                        break;
                    }
                case 9:
                    if ((i13 & i) != 0) {
                        i11 += zzht.zza(i14, unsafe2.getObject(t, j3), zza(i10));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 10:
                    if ((i13 & i) != 0) {
                        i11 += zzfc.zzc(i14, (zzep) unsafe2.getObject(t, j3));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 11:
                    if ((i13 & i) != 0) {
                        i11 += zzfc.zzg(i14, unsafe2.getInt(t, j3));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 12:
                    if ((i13 & i) != 0) {
                        i11 += zzfc.zzk(i14, unsafe2.getInt(t, j3));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 13:
                    if ((i13 & i) != 0) {
                        i11 += zzfc.zzj(i14, 0);
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 14:
                    if ((i13 & i) != 0) {
                        i11 += zzfc.zzh(i14, 0L);
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 15:
                    if ((i13 & i) != 0) {
                        i11 += zzfc.zzh(i14, unsafe2.getInt(t, j3));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 16:
                    if ((i13 & i) != 0) {
                        i11 += zzfc.zzf(i14, unsafe2.getLong(t, j3));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 17:
                    if ((i13 & i) != 0) {
                        i11 += zzfc.zzc(i14, (zzhb) unsafe2.getObject(t, j3), zza(i10));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 18:
                    i11 += zzht.zzi(i14, (List) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 19:
                    i11 += zzht.zzh(i14, (List) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 20:
                    i11 += zzht.zza(i14, (List<Long>) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 21:
                    i11 += zzht.zzb(i14, (List<Long>) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 22:
                    i11 += zzht.zze(i14, (List) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 23:
                    i11 += zzht.zzi(i14, (List) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 24:
                    i11 += zzht.zzh(i14, (List) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 25:
                    i11 += zzht.zzj(i14, (List) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 26:
                    i11 += zzht.zza(i14, (List) unsafe2.getObject(t, j3));
                    j = 0;
                    break;
                case 27:
                    i11 += zzht.zza(i14, (List<?>) unsafe2.getObject(t, j3), zza(i10));
                    j = 0;
                    break;
                case 28:
                    i11 += zzht.zzb(i14, (List) unsafe2.getObject(t, j3));
                    j = 0;
                    break;
                case NalUnitTypes.NAL_TYPE_RSV_VCL29 /* 29 */:
                    i11 += zzht.zzf(i14, (List) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 30:
                    i11 += zzht.zzd(i14, (List) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 31:
                    i11 += zzht.zzh(i14, (List) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 32:
                    i11 += zzht.zzi(i14, (List) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 33:
                    i11 += zzht.zzg(i14, (List) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 34:
                    i11 += zzht.zzc(i14, (List) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 35:
                    int zzi4 = zzht.zzi((List) unsafe2.getObject(t, j3));
                    if (zzi4 > 0) {
                        i11 += zzfc.zze(i14) + zzfc.zzg(zzi4) + zzi4;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 36:
                    int zzh4 = zzht.zzh((List) unsafe2.getObject(t, j3));
                    if (zzh4 > 0) {
                        i11 += zzfc.zze(i14) + zzfc.zzg(zzh4) + zzh4;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 37:
                    int zza3 = zzht.zza((List) unsafe2.getObject(t, j3));
                    if (zza3 > 0) {
                        i11 += zzfc.zze(i14) + zzfc.zzg(zza3) + zza3;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 38:
                    int zzb3 = zzht.zzb((List) unsafe2.getObject(t, j3));
                    if (zzb3 > 0) {
                        i11 += zzfc.zze(i14) + zzfc.zzg(zzb3) + zzb3;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 39:
                    int zze2 = zzht.zze((List) unsafe2.getObject(t, j3));
                    if (zze2 > 0) {
                        i11 += zzfc.zze(i14) + zzfc.zzg(zze2) + zze2;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 40:
                    int zzi5 = zzht.zzi((List) unsafe2.getObject(t, j3));
                    if (zzi5 > 0) {
                        i11 += zzfc.zze(i14) + zzfc.zzg(zzi5) + zzi5;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 41:
                    int zzh5 = zzht.zzh((List) unsafe2.getObject(t, j3));
                    if (zzh5 > 0) {
                        i11 += zzfc.zze(i14) + zzfc.zzg(zzh5) + zzh5;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 42:
                    int zzj2 = zzht.zzj((List) unsafe2.getObject(t, j3));
                    if (zzj2 > 0) {
                        i11 += zzfc.zze(i14) + zzfc.zzg(zzj2) + zzj2;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 43:
                    int zzf4 = zzht.zzf((List) unsafe2.getObject(t, j3));
                    if (zzf4 > 0) {
                        i11 += zzfc.zze(i14) + zzfc.zzg(zzf4) + zzf4;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 44:
                    int zzd2 = zzht.zzd((List) unsafe2.getObject(t, j3));
                    if (zzd2 > 0) {
                        i11 += zzfc.zze(i14) + zzfc.zzg(zzd2) + zzd2;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 45:
                    int zzh6 = zzht.zzh((List) unsafe2.getObject(t, j3));
                    if (zzh6 > 0) {
                        i11 += zzfc.zze(i14) + zzfc.zzg(zzh6) + zzh6;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 46:
                    int zzi6 = zzht.zzi((List) unsafe2.getObject(t, j3));
                    if (zzi6 > 0) {
                        i11 += zzfc.zze(i14) + zzfc.zzg(zzi6) + zzi6;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 47:
                    int zzg2 = zzht.zzg((List) unsafe2.getObject(t, j3));
                    if (zzg2 > 0) {
                        i11 += zzfc.zze(i14) + zzfc.zzg(zzg2) + zzg2;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 48:
                    int zzc4 = zzht.zzc((List) unsafe2.getObject(t, j3));
                    if (zzc4 > 0) {
                        i11 += zzfc.zze(i14) + zzfc.zzg(zzc4) + zzc4;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 49:
                    i11 += zzht.zzb(i14, (List) unsafe2.getObject(t, j3), zza(i10));
                    j = 0;
                    break;
                case 50:
                    i11 += this.zzs.zza(i14, unsafe2.getObject(t, j3), zzb(i10));
                    j = 0;
                    break;
                case 51:
                    if (zza((zzhf<T>) t, i14, i10)) {
                        i11 += zzfc.zzb(i14, (double) FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE);
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 52:
                    if (zza((zzhf<T>) t, i14, i10)) {
                        i11 += zzfc.zzb(i14, 0.0f);
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 53:
                    if (zza((zzhf<T>) t, i14, i10)) {
                        i11 += zzfc.zzd(i14, zze(t, j3));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 54:
                    if (zza((zzhf<T>) t, i14, i10)) {
                        i11 += zzfc.zze(i14, zze(t, j3));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 55:
                    if (zza((zzhf<T>) t, i14, i10)) {
                        i11 += zzfc.zzf(i14, zzd(t, j3));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case UndoView.ACTION_USERNAME_COPIED /* 56 */:
                    if (zza((zzhf<T>) t, i14, i10)) {
                        i11 += zzfc.zzg(i14, 0L);
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case UndoView.ACTION_HASHTAG_COPIED /* 57 */:
                    if (zza((zzhf<T>) t, i14, i10)) {
                        i11 += zzfc.zzi(i14, 0);
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case UndoView.ACTION_TEXT_COPIED /* 58 */:
                    if (zza((zzhf<T>) t, i14, i10)) {
                        i11 += zzfc.zzb(i14, true);
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 59:
                    if (zza((zzhf<T>) t, i14, i10)) {
                        Object object2 = unsafe2.getObject(t, j3);
                        if (object2 instanceof zzep) {
                            i11 += zzfc.zzc(i14, (zzep) object2);
                            j = 0;
                            break;
                        } else {
                            i11 += zzfc.zzb(i14, (String) object2);
                            j = 0;
                            break;
                        }
                    } else {
                        j = 0;
                        break;
                    }
                case UndoView.ACTION_PHONE_COPIED /* 60 */:
                    if (zza((zzhf<T>) t, i14, i10)) {
                        i11 += zzht.zza(i14, unsafe2.getObject(t, j3), zza(i10));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case UndoView.ACTION_SHARE_BACKGROUND /* 61 */:
                    if (zza((zzhf<T>) t, i14, i10)) {
                        i11 += zzfc.zzc(i14, (zzep) unsafe2.getObject(t, j3));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 62:
                    if (zza((zzhf<T>) t, i14, i10)) {
                        i11 += zzfc.zzg(i14, zzd(t, j3));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case HtmlCompat.FROM_HTML_MODE_COMPACT /* 63 */:
                    if (zza((zzhf<T>) t, i14, i10)) {
                        i11 += zzfc.zzk(i14, zzd(t, j3));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 64:
                    if (zza((zzhf<T>) t, i14, i10)) {
                        i11 += zzfc.zzj(i14, 0);
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case VoIPService.CALL_MIN_LAYER /* 65 */:
                    if (zza((zzhf<T>) t, i14, i10)) {
                        i11 += zzfc.zzh(i14, 0L);
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 66:
                    if (zza((zzhf<T>) t, i14, i10)) {
                        i11 += zzfc.zzh(i14, zzd(t, j3));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 67:
                    if (zza((zzhf<T>) t, i14, i10)) {
                        i11 += zzfc.zzf(i14, zze(t, j3));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 68:
                    if (zza((zzhf<T>) t, i14, i10)) {
                        i11 += zzfc.zzc(i14, (zzhb) unsafe2.getObject(t, j3), zza(i10));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                default:
                    j = 0;
                    break;
            }
            i10 += 3;
            i3 = 1048575;
            i4 = 1;
        }
        int i18 = 0;
        int zza4 = i11 + zza((zzij) this.zzq, (Object) t);
        if (this.zzh) {
            zzfl<?> zza5 = this.zzr.zza(t);
            for (int i19 = 0; i19 < zza5.zza.zzc(); i19++) {
                Map.Entry<?, Object> zzb4 = zza5.zza.zzb(i19);
                i18 += zzfl.zza((zzfn) zzb4.getKey(), zzb4.getValue());
            }
            for (Map.Entry<?, Object> entry : zza5.zza.zzd()) {
                i18 += zzfl.zza((zzfn) entry.getKey(), entry.getValue());
            }
            return zza4 + i18;
        }
        return zza4;
    }

    private static <UT, UB> int zza(zzij<UT, UB> zzijVar, T t) {
        return zzijVar.zzd(zzijVar.zza(t));
    }

    private static List<?> zza(Object obj, long j) {
        return (List) zzip.zzf(obj, j);
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x003f  */
    /* JADX WARN: Removed duplicated region for block: B:165:0x059f  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x05e2  */
    /* JADX WARN: Removed duplicated region for block: B:333:0x0b42  */
    @Override // com.google.android.gms.internal.mlkit_common.zzhr
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void zza(T t, zzjd zzjdVar) throws IOException {
        Map.Entry<?, Object> entry;
        Iterator<Map.Entry<?, Object>> it;
        int length;
        int i;
        Map.Entry<?, Object> entry2;
        Iterator<Map.Entry<?, Object>> it2;
        int length2;
        if (zzjdVar.zza() == zzjc.zzb) {
            zza(this.zzq, t, zzjdVar);
            if (this.zzh) {
                zzfl<?> zza2 = this.zzr.zza(t);
                if (!zza2.zza.isEmpty()) {
                    it2 = zza2.zze();
                    entry2 = it2.next();
                    for (length2 = this.zzc.length - 3; length2 >= 0; length2 -= 3) {
                        int zzc = zzc(length2);
                        int i2 = this.zzc[length2];
                        while (entry2 != null && this.zzr.zza(entry2) > i2) {
                            this.zzr.zza(zzjdVar, entry2);
                            entry2 = it2.hasNext() ? it2.next() : null;
                        }
                        switch ((zzc & 267386880) >>> 20) {
                            case 0:
                                if (zza((zzhf<T>) t, length2)) {
                                    zzjdVar.zza(i2, zzip.zze(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 1:
                                if (zza((zzhf<T>) t, length2)) {
                                    zzjdVar.zza(i2, zzip.zzd(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 2:
                                if (zza((zzhf<T>) t, length2)) {
                                    zzjdVar.zza(i2, zzip.zzb(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 3:
                                if (zza((zzhf<T>) t, length2)) {
                                    zzjdVar.zzc(i2, zzip.zzb(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 4:
                                if (zza((zzhf<T>) t, length2)) {
                                    zzjdVar.zzc(i2, zzip.zza(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 5:
                                if (zza((zzhf<T>) t, length2)) {
                                    zzjdVar.zzd(i2, zzip.zzb(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 6:
                                if (zza((zzhf<T>) t, length2)) {
                                    zzjdVar.zzd(i2, zzip.zza(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 7:
                                if (zza((zzhf<T>) t, length2)) {
                                    zzjdVar.zza(i2, zzip.zzc(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 8:
                                if (zza((zzhf<T>) t, length2)) {
                                    zza(i2, zzip.zzf(t, zzc & 1048575), zzjdVar);
                                    break;
                                } else {
                                    break;
                                }
                            case 9:
                                if (zza((zzhf<T>) t, length2)) {
                                    zzjdVar.zza(i2, zzip.zzf(t, zzc & 1048575), zza(length2));
                                    break;
                                } else {
                                    break;
                                }
                            case 10:
                                if (zza((zzhf<T>) t, length2)) {
                                    zzjdVar.zza(i2, (zzep) zzip.zzf(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 11:
                                if (zza((zzhf<T>) t, length2)) {
                                    zzjdVar.zze(i2, zzip.zza(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 12:
                                if (zza((zzhf<T>) t, length2)) {
                                    zzjdVar.zzb(i2, zzip.zza(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 13:
                                if (zza((zzhf<T>) t, length2)) {
                                    zzjdVar.zza(i2, zzip.zza(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 14:
                                if (zza((zzhf<T>) t, length2)) {
                                    zzjdVar.zzb(i2, zzip.zzb(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 15:
                                if (zza((zzhf<T>) t, length2)) {
                                    zzjdVar.zzf(i2, zzip.zza(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 16:
                                if (zza((zzhf<T>) t, length2)) {
                                    zzjdVar.zze(i2, zzip.zzb(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 17:
                                if (zza((zzhf<T>) t, length2)) {
                                    zzjdVar.zzb(i2, zzip.zzf(t, zzc & 1048575), zza(length2));
                                    break;
                                } else {
                                    break;
                                }
                            case 18:
                                zzht.zza(this.zzc[length2], (List<Double>) zzip.zzf(t, zzc & 1048575), zzjdVar, false);
                                break;
                            case 19:
                                zzht.zzb(this.zzc[length2], (List<Float>) zzip.zzf(t, zzc & 1048575), zzjdVar, false);
                                break;
                            case 20:
                                zzht.zzc(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar, false);
                                break;
                            case 21:
                                zzht.zzd(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar, false);
                                break;
                            case 22:
                                zzht.zzh(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar, false);
                                break;
                            case 23:
                                zzht.zzf(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar, false);
                                break;
                            case 24:
                                zzht.zzk(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar, false);
                                break;
                            case 25:
                                zzht.zzn(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar, false);
                                break;
                            case 26:
                                zzht.zza(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar);
                                break;
                            case 27:
                                zzht.zza(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar, zza(length2));
                                break;
                            case 28:
                                zzht.zzb(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar);
                                break;
                            case NalUnitTypes.NAL_TYPE_RSV_VCL29 /* 29 */:
                                zzht.zzi(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar, false);
                                break;
                            case 30:
                                zzht.zzm(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar, false);
                                break;
                            case 31:
                                zzht.zzl(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar, false);
                                break;
                            case 32:
                                zzht.zzg(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar, false);
                                break;
                            case 33:
                                zzht.zzj(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar, false);
                                break;
                            case 34:
                                zzht.zze(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar, false);
                                break;
                            case 35:
                                zzht.zza(this.zzc[length2], (List<Double>) zzip.zzf(t, zzc & 1048575), zzjdVar, true);
                                break;
                            case 36:
                                zzht.zzb(this.zzc[length2], (List<Float>) zzip.zzf(t, zzc & 1048575), zzjdVar, true);
                                break;
                            case 37:
                                zzht.zzc(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar, true);
                                break;
                            case 38:
                                zzht.zzd(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar, true);
                                break;
                            case 39:
                                zzht.zzh(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar, true);
                                break;
                            case 40:
                                zzht.zzf(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar, true);
                                break;
                            case 41:
                                zzht.zzk(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar, true);
                                break;
                            case 42:
                                zzht.zzn(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar, true);
                                break;
                            case 43:
                                zzht.zzi(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar, true);
                                break;
                            case 44:
                                zzht.zzm(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar, true);
                                break;
                            case 45:
                                zzht.zzl(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar, true);
                                break;
                            case 46:
                                zzht.zzg(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar, true);
                                break;
                            case 47:
                                zzht.zzj(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar, true);
                                break;
                            case 48:
                                zzht.zze(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar, true);
                                break;
                            case 49:
                                zzht.zzb(this.zzc[length2], (List) zzip.zzf(t, zzc & 1048575), zzjdVar, zza(length2));
                                break;
                            case 50:
                                zza(zzjdVar, i2, zzip.zzf(t, zzc & 1048575), length2);
                                break;
                            case 51:
                                if (zza((zzhf<T>) t, i2, length2)) {
                                    zzjdVar.zza(i2, zzb(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 52:
                                if (zza((zzhf<T>) t, i2, length2)) {
                                    zzjdVar.zza(i2, zzc(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 53:
                                if (zza((zzhf<T>) t, i2, length2)) {
                                    zzjdVar.zza(i2, zze(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 54:
                                if (zza((zzhf<T>) t, i2, length2)) {
                                    zzjdVar.zzc(i2, zze(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 55:
                                if (zza((zzhf<T>) t, i2, length2)) {
                                    zzjdVar.zzc(i2, zzd(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case UndoView.ACTION_USERNAME_COPIED /* 56 */:
                                if (zza((zzhf<T>) t, i2, length2)) {
                                    zzjdVar.zzd(i2, zze(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case UndoView.ACTION_HASHTAG_COPIED /* 57 */:
                                if (zza((zzhf<T>) t, i2, length2)) {
                                    zzjdVar.zzd(i2, zzd(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case UndoView.ACTION_TEXT_COPIED /* 58 */:
                                if (zza((zzhf<T>) t, i2, length2)) {
                                    zzjdVar.zza(i2, zzf(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 59:
                                if (zza((zzhf<T>) t, i2, length2)) {
                                    zza(i2, zzip.zzf(t, zzc & 1048575), zzjdVar);
                                    break;
                                } else {
                                    break;
                                }
                            case UndoView.ACTION_PHONE_COPIED /* 60 */:
                                if (zza((zzhf<T>) t, i2, length2)) {
                                    zzjdVar.zza(i2, zzip.zzf(t, zzc & 1048575), zza(length2));
                                    break;
                                } else {
                                    break;
                                }
                            case UndoView.ACTION_SHARE_BACKGROUND /* 61 */:
                                if (zza((zzhf<T>) t, i2, length2)) {
                                    zzjdVar.zza(i2, (zzep) zzip.zzf(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 62:
                                if (zza((zzhf<T>) t, i2, length2)) {
                                    zzjdVar.zze(i2, zzd(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case HtmlCompat.FROM_HTML_MODE_COMPACT /* 63 */:
                                if (zza((zzhf<T>) t, i2, length2)) {
                                    zzjdVar.zzb(i2, zzd(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 64:
                                if (zza((zzhf<T>) t, i2, length2)) {
                                    zzjdVar.zza(i2, zzd(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case VoIPService.CALL_MIN_LAYER /* 65 */:
                                if (zza((zzhf<T>) t, i2, length2)) {
                                    zzjdVar.zzb(i2, zze(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 66:
                                if (zza((zzhf<T>) t, i2, length2)) {
                                    zzjdVar.zzf(i2, zzd(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 67:
                                if (zza((zzhf<T>) t, i2, length2)) {
                                    zzjdVar.zze(i2, zze(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 68:
                                if (zza((zzhf<T>) t, i2, length2)) {
                                    zzjdVar.zzb(i2, zzip.zzf(t, zzc & 1048575), zza(length2));
                                    break;
                                } else {
                                    break;
                                }
                        }
                    }
                    while (entry2 != null) {
                        this.zzr.zza(zzjdVar, entry2);
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
        } else if (this.zzj) {
            if (this.zzh) {
                zzfl<?> zza3 = this.zzr.zza(t);
                if (!zza3.zza.isEmpty()) {
                    it = zza3.zzd();
                    entry = it.next();
                    length = this.zzc.length;
                    for (i = 0; i < length; i += 3) {
                        int zzc2 = zzc(i);
                        int i3 = this.zzc[i];
                        while (entry != null && this.zzr.zza(entry) <= i3) {
                            this.zzr.zza(zzjdVar, entry);
                            entry = it.hasNext() ? it.next() : null;
                        }
                        switch ((zzc2 & 267386880) >>> 20) {
                            case 0:
                                if (zza((zzhf<T>) t, i)) {
                                    zzjdVar.zza(i3, zzip.zze(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 1:
                                if (zza((zzhf<T>) t, i)) {
                                    zzjdVar.zza(i3, zzip.zzd(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 2:
                                if (zza((zzhf<T>) t, i)) {
                                    zzjdVar.zza(i3, zzip.zzb(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 3:
                                if (zza((zzhf<T>) t, i)) {
                                    zzjdVar.zzc(i3, zzip.zzb(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 4:
                                if (zza((zzhf<T>) t, i)) {
                                    zzjdVar.zzc(i3, zzip.zza(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 5:
                                if (zza((zzhf<T>) t, i)) {
                                    zzjdVar.zzd(i3, zzip.zzb(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 6:
                                if (zza((zzhf<T>) t, i)) {
                                    zzjdVar.zzd(i3, zzip.zza(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 7:
                                if (zza((zzhf<T>) t, i)) {
                                    zzjdVar.zza(i3, zzip.zzc(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 8:
                                if (zza((zzhf<T>) t, i)) {
                                    zza(i3, zzip.zzf(t, zzc2 & 1048575), zzjdVar);
                                    break;
                                } else {
                                    break;
                                }
                            case 9:
                                if (zza((zzhf<T>) t, i)) {
                                    zzjdVar.zza(i3, zzip.zzf(t, zzc2 & 1048575), zza(i));
                                    break;
                                } else {
                                    break;
                                }
                            case 10:
                                if (zza((zzhf<T>) t, i)) {
                                    zzjdVar.zza(i3, (zzep) zzip.zzf(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 11:
                                if (zza((zzhf<T>) t, i)) {
                                    zzjdVar.zze(i3, zzip.zza(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 12:
                                if (zza((zzhf<T>) t, i)) {
                                    zzjdVar.zzb(i3, zzip.zza(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 13:
                                if (zza((zzhf<T>) t, i)) {
                                    zzjdVar.zza(i3, zzip.zza(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 14:
                                if (zza((zzhf<T>) t, i)) {
                                    zzjdVar.zzb(i3, zzip.zzb(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 15:
                                if (zza((zzhf<T>) t, i)) {
                                    zzjdVar.zzf(i3, zzip.zza(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 16:
                                if (zza((zzhf<T>) t, i)) {
                                    zzjdVar.zze(i3, zzip.zzb(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 17:
                                if (zza((zzhf<T>) t, i)) {
                                    zzjdVar.zzb(i3, zzip.zzf(t, zzc2 & 1048575), zza(i));
                                    break;
                                } else {
                                    break;
                                }
                            case 18:
                                zzht.zza(this.zzc[i], (List<Double>) zzip.zzf(t, zzc2 & 1048575), zzjdVar, false);
                                break;
                            case 19:
                                zzht.zzb(this.zzc[i], (List<Float>) zzip.zzf(t, zzc2 & 1048575), zzjdVar, false);
                                break;
                            case 20:
                                zzht.zzc(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar, false);
                                break;
                            case 21:
                                zzht.zzd(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar, false);
                                break;
                            case 22:
                                zzht.zzh(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar, false);
                                break;
                            case 23:
                                zzht.zzf(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar, false);
                                break;
                            case 24:
                                zzht.zzk(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar, false);
                                break;
                            case 25:
                                zzht.zzn(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar, false);
                                break;
                            case 26:
                                zzht.zza(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar);
                                break;
                            case 27:
                                zzht.zza(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar, zza(i));
                                break;
                            case 28:
                                zzht.zzb(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar);
                                break;
                            case NalUnitTypes.NAL_TYPE_RSV_VCL29 /* 29 */:
                                zzht.zzi(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar, false);
                                break;
                            case 30:
                                zzht.zzm(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar, false);
                                break;
                            case 31:
                                zzht.zzl(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar, false);
                                break;
                            case 32:
                                zzht.zzg(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar, false);
                                break;
                            case 33:
                                zzht.zzj(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar, false);
                                break;
                            case 34:
                                zzht.zze(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar, false);
                                break;
                            case 35:
                                zzht.zza(this.zzc[i], (List<Double>) zzip.zzf(t, zzc2 & 1048575), zzjdVar, true);
                                break;
                            case 36:
                                zzht.zzb(this.zzc[i], (List<Float>) zzip.zzf(t, zzc2 & 1048575), zzjdVar, true);
                                break;
                            case 37:
                                zzht.zzc(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar, true);
                                break;
                            case 38:
                                zzht.zzd(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar, true);
                                break;
                            case 39:
                                zzht.zzh(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar, true);
                                break;
                            case 40:
                                zzht.zzf(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar, true);
                                break;
                            case 41:
                                zzht.zzk(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar, true);
                                break;
                            case 42:
                                zzht.zzn(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar, true);
                                break;
                            case 43:
                                zzht.zzi(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar, true);
                                break;
                            case 44:
                                zzht.zzm(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar, true);
                                break;
                            case 45:
                                zzht.zzl(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar, true);
                                break;
                            case 46:
                                zzht.zzg(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar, true);
                                break;
                            case 47:
                                zzht.zzj(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar, true);
                                break;
                            case 48:
                                zzht.zze(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar, true);
                                break;
                            case 49:
                                zzht.zzb(this.zzc[i], (List) zzip.zzf(t, zzc2 & 1048575), zzjdVar, zza(i));
                                break;
                            case 50:
                                zza(zzjdVar, i3, zzip.zzf(t, zzc2 & 1048575), i);
                                break;
                            case 51:
                                if (zza((zzhf<T>) t, i3, i)) {
                                    zzjdVar.zza(i3, zzb(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 52:
                                if (zza((zzhf<T>) t, i3, i)) {
                                    zzjdVar.zza(i3, zzc(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 53:
                                if (zza((zzhf<T>) t, i3, i)) {
                                    zzjdVar.zza(i3, zze(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 54:
                                if (zza((zzhf<T>) t, i3, i)) {
                                    zzjdVar.zzc(i3, zze(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 55:
                                if (zza((zzhf<T>) t, i3, i)) {
                                    zzjdVar.zzc(i3, zzd(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case UndoView.ACTION_USERNAME_COPIED /* 56 */:
                                if (zza((zzhf<T>) t, i3, i)) {
                                    zzjdVar.zzd(i3, zze(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case UndoView.ACTION_HASHTAG_COPIED /* 57 */:
                                if (zza((zzhf<T>) t, i3, i)) {
                                    zzjdVar.zzd(i3, zzd(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case UndoView.ACTION_TEXT_COPIED /* 58 */:
                                if (zza((zzhf<T>) t, i3, i)) {
                                    zzjdVar.zza(i3, zzf(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 59:
                                if (zza((zzhf<T>) t, i3, i)) {
                                    zza(i3, zzip.zzf(t, zzc2 & 1048575), zzjdVar);
                                    break;
                                } else {
                                    break;
                                }
                            case UndoView.ACTION_PHONE_COPIED /* 60 */:
                                if (zza((zzhf<T>) t, i3, i)) {
                                    zzjdVar.zza(i3, zzip.zzf(t, zzc2 & 1048575), zza(i));
                                    break;
                                } else {
                                    break;
                                }
                            case UndoView.ACTION_SHARE_BACKGROUND /* 61 */:
                                if (zza((zzhf<T>) t, i3, i)) {
                                    zzjdVar.zza(i3, (zzep) zzip.zzf(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 62:
                                if (zza((zzhf<T>) t, i3, i)) {
                                    zzjdVar.zze(i3, zzd(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case HtmlCompat.FROM_HTML_MODE_COMPACT /* 63 */:
                                if (zza((zzhf<T>) t, i3, i)) {
                                    zzjdVar.zzb(i3, zzd(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 64:
                                if (zza((zzhf<T>) t, i3, i)) {
                                    zzjdVar.zza(i3, zzd(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case VoIPService.CALL_MIN_LAYER /* 65 */:
                                if (zza((zzhf<T>) t, i3, i)) {
                                    zzjdVar.zzb(i3, zze(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 66:
                                if (zza((zzhf<T>) t, i3, i)) {
                                    zzjdVar.zzf(i3, zzd(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 67:
                                if (zza((zzhf<T>) t, i3, i)) {
                                    zzjdVar.zze(i3, zze(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 68:
                                if (zza((zzhf<T>) t, i3, i)) {
                                    zzjdVar.zzb(i3, zzip.zzf(t, zzc2 & 1048575), zza(i));
                                    break;
                                } else {
                                    break;
                                }
                        }
                    }
                    while (entry != null) {
                        this.zzr.zza(zzjdVar, entry);
                        entry = it.hasNext() ? it.next() : null;
                    }
                    zza(this.zzq, t, zzjdVar);
                }
            }
            it = null;
            entry = null;
            length = this.zzc.length;
            while (i < length) {
            }
            while (entry != null) {
            }
            zza(this.zzq, t, zzjdVar);
        } else {
            zzb((zzhf<T>) t, zzjdVar);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0035  */
    /* JADX WARN: Removed duplicated region for block: B:190:0x0559  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void zzb(T t, zzjd zzjdVar) throws IOException {
        Map.Entry<?, Object> entry;
        Iterator<Map.Entry<?, Object>> it;
        int length;
        int i;
        int i2;
        if (this.zzh) {
            zzfl<?> zza2 = this.zzr.zza(t);
            if (!zza2.zza.isEmpty()) {
                it = zza2.zzd();
                entry = it.next();
                length = this.zzc.length;
                Unsafe unsafe = zzb;
                int i3 = 1048575;
                int i4 = 0;
                for (i = 0; i < length; i += 3) {
                    int zzc = zzc(i);
                    int[] iArr = this.zzc;
                    int i5 = iArr[i];
                    int i6 = (zzc & 267386880) >>> 20;
                    if (!this.zzj && i6 <= 17) {
                        int i7 = iArr[i + 2];
                        int i8 = i7 & 1048575;
                        if (i8 != i3) {
                            i4 = unsafe.getInt(t, i8);
                            i3 = i8;
                        }
                        i2 = 1 << (i7 >>> 20);
                    } else {
                        i2 = 0;
                    }
                    while (entry != null && this.zzr.zza(entry) <= i5) {
                        this.zzr.zza(zzjdVar, entry);
                        entry = it.hasNext() ? it.next() : null;
                    }
                    long j = zzc & 1048575;
                    switch (i6) {
                        case 0:
                            if ((i2 & i4) != 0) {
                                zzjdVar.zza(i5, zzip.zze(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 1:
                            if ((i2 & i4) != 0) {
                                zzjdVar.zza(i5, zzip.zzd(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 2:
                            if ((i2 & i4) != 0) {
                                zzjdVar.zza(i5, unsafe.getLong(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 3:
                            if ((i2 & i4) != 0) {
                                zzjdVar.zzc(i5, unsafe.getLong(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 4:
                            if ((i2 & i4) != 0) {
                                zzjdVar.zzc(i5, unsafe.getInt(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 5:
                            if ((i2 & i4) != 0) {
                                zzjdVar.zzd(i5, unsafe.getLong(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 6:
                            if ((i2 & i4) != 0) {
                                zzjdVar.zzd(i5, unsafe.getInt(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 7:
                            if ((i2 & i4) != 0) {
                                zzjdVar.zza(i5, zzip.zzc(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 8:
                            if ((i2 & i4) != 0) {
                                zza(i5, unsafe.getObject(t, j), zzjdVar);
                                break;
                            } else {
                                break;
                            }
                        case 9:
                            if ((i2 & i4) != 0) {
                                zzjdVar.zza(i5, unsafe.getObject(t, j), zza(i));
                                break;
                            } else {
                                break;
                            }
                        case 10:
                            if ((i2 & i4) != 0) {
                                zzjdVar.zza(i5, (zzep) unsafe.getObject(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 11:
                            if ((i2 & i4) != 0) {
                                zzjdVar.zze(i5, unsafe.getInt(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 12:
                            if ((i2 & i4) != 0) {
                                zzjdVar.zzb(i5, unsafe.getInt(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 13:
                            if ((i2 & i4) != 0) {
                                zzjdVar.zza(i5, unsafe.getInt(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 14:
                            if ((i2 & i4) != 0) {
                                zzjdVar.zzb(i5, unsafe.getLong(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 15:
                            if ((i2 & i4) != 0) {
                                zzjdVar.zzf(i5, unsafe.getInt(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 16:
                            if ((i2 & i4) != 0) {
                                zzjdVar.zze(i5, unsafe.getLong(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 17:
                            if ((i2 & i4) != 0) {
                                zzjdVar.zzb(i5, unsafe.getObject(t, j), zza(i));
                                break;
                            } else {
                                break;
                            }
                        case 18:
                            zzht.zza(this.zzc[i], (List<Double>) unsafe.getObject(t, j), zzjdVar, false);
                            break;
                        case 19:
                            zzht.zzb(this.zzc[i], (List<Float>) unsafe.getObject(t, j), zzjdVar, false);
                            break;
                        case 20:
                            zzht.zzc(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar, false);
                            break;
                        case 21:
                            zzht.zzd(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar, false);
                            break;
                        case 22:
                            zzht.zzh(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar, false);
                            break;
                        case 23:
                            zzht.zzf(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar, false);
                            break;
                        case 24:
                            zzht.zzk(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar, false);
                            break;
                        case 25:
                            zzht.zzn(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar, false);
                            break;
                        case 26:
                            zzht.zza(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar);
                            break;
                        case 27:
                            zzht.zza(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar, zza(i));
                            break;
                        case 28:
                            zzht.zzb(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar);
                            break;
                        case NalUnitTypes.NAL_TYPE_RSV_VCL29 /* 29 */:
                            zzht.zzi(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar, false);
                            break;
                        case 30:
                            zzht.zzm(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar, false);
                            break;
                        case 31:
                            zzht.zzl(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar, false);
                            break;
                        case 32:
                            zzht.zzg(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar, false);
                            break;
                        case 33:
                            zzht.zzj(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar, false);
                            break;
                        case 34:
                            zzht.zze(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar, false);
                            break;
                        case 35:
                            zzht.zza(this.zzc[i], (List<Double>) unsafe.getObject(t, j), zzjdVar, true);
                            break;
                        case 36:
                            zzht.zzb(this.zzc[i], (List<Float>) unsafe.getObject(t, j), zzjdVar, true);
                            break;
                        case 37:
                            zzht.zzc(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar, true);
                            break;
                        case 38:
                            zzht.zzd(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar, true);
                            break;
                        case 39:
                            zzht.zzh(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar, true);
                            break;
                        case 40:
                            zzht.zzf(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar, true);
                            break;
                        case 41:
                            zzht.zzk(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar, true);
                            break;
                        case 42:
                            zzht.zzn(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar, true);
                            break;
                        case 43:
                            zzht.zzi(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar, true);
                            break;
                        case 44:
                            zzht.zzm(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar, true);
                            break;
                        case 45:
                            zzht.zzl(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar, true);
                            break;
                        case 46:
                            zzht.zzg(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar, true);
                            break;
                        case 47:
                            zzht.zzj(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar, true);
                            break;
                        case 48:
                            zzht.zze(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar, true);
                            break;
                        case 49:
                            zzht.zzb(this.zzc[i], (List) unsafe.getObject(t, j), zzjdVar, zza(i));
                            break;
                        case 50:
                            zza(zzjdVar, i5, unsafe.getObject(t, j), i);
                            break;
                        case 51:
                            if (zza((zzhf<T>) t, i5, i)) {
                                zzjdVar.zza(i5, zzb(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 52:
                            if (zza((zzhf<T>) t, i5, i)) {
                                zzjdVar.zza(i5, zzc(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 53:
                            if (zza((zzhf<T>) t, i5, i)) {
                                zzjdVar.zza(i5, zze(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 54:
                            if (zza((zzhf<T>) t, i5, i)) {
                                zzjdVar.zzc(i5, zze(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 55:
                            if (zza((zzhf<T>) t, i5, i)) {
                                zzjdVar.zzc(i5, zzd(t, j));
                                break;
                            } else {
                                break;
                            }
                        case UndoView.ACTION_USERNAME_COPIED /* 56 */:
                            if (zza((zzhf<T>) t, i5, i)) {
                                zzjdVar.zzd(i5, zze(t, j));
                                break;
                            } else {
                                break;
                            }
                        case UndoView.ACTION_HASHTAG_COPIED /* 57 */:
                            if (zza((zzhf<T>) t, i5, i)) {
                                zzjdVar.zzd(i5, zzd(t, j));
                                break;
                            } else {
                                break;
                            }
                        case UndoView.ACTION_TEXT_COPIED /* 58 */:
                            if (zza((zzhf<T>) t, i5, i)) {
                                zzjdVar.zza(i5, zzf(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 59:
                            if (zza((zzhf<T>) t, i5, i)) {
                                zza(i5, unsafe.getObject(t, j), zzjdVar);
                                break;
                            } else {
                                break;
                            }
                        case UndoView.ACTION_PHONE_COPIED /* 60 */:
                            if (zza((zzhf<T>) t, i5, i)) {
                                zzjdVar.zza(i5, unsafe.getObject(t, j), zza(i));
                                break;
                            } else {
                                break;
                            }
                        case UndoView.ACTION_SHARE_BACKGROUND /* 61 */:
                            if (zza((zzhf<T>) t, i5, i)) {
                                zzjdVar.zza(i5, (zzep) unsafe.getObject(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 62:
                            if (zza((zzhf<T>) t, i5, i)) {
                                zzjdVar.zze(i5, zzd(t, j));
                                break;
                            } else {
                                break;
                            }
                        case HtmlCompat.FROM_HTML_MODE_COMPACT /* 63 */:
                            if (zza((zzhf<T>) t, i5, i)) {
                                zzjdVar.zzb(i5, zzd(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 64:
                            if (zza((zzhf<T>) t, i5, i)) {
                                zzjdVar.zza(i5, zzd(t, j));
                                break;
                            } else {
                                break;
                            }
                        case VoIPService.CALL_MIN_LAYER /* 65 */:
                            if (zza((zzhf<T>) t, i5, i)) {
                                zzjdVar.zzb(i5, zze(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 66:
                            if (zza((zzhf<T>) t, i5, i)) {
                                zzjdVar.zzf(i5, zzd(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 67:
                            if (zza((zzhf<T>) t, i5, i)) {
                                zzjdVar.zze(i5, zze(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 68:
                            if (zza((zzhf<T>) t, i5, i)) {
                                zzjdVar.zzb(i5, unsafe.getObject(t, j), zza(i));
                                break;
                            } else {
                                break;
                            }
                    }
                }
                while (entry != null) {
                    this.zzr.zza(zzjdVar, entry);
                    entry = it.hasNext() ? it.next() : null;
                }
                zza(this.zzq, t, zzjdVar);
            }
        }
        it = null;
        entry = null;
        length = this.zzc.length;
        Unsafe unsafe2 = zzb;
        int i32 = 1048575;
        int i42 = 0;
        while (i < length) {
        }
        while (entry != null) {
        }
        zza(this.zzq, t, zzjdVar);
    }

    private final <K, V> void zza(zzjd zzjdVar, int i, Object obj, int i2) throws IOException {
        if (obj != null) {
            zzjdVar.zza(i, this.zzs.zzc(zzb(i2)), this.zzs.zza(obj));
        }
    }

    private static <UT, UB> void zza(zzij<UT, UB> zzijVar, T t, zzjd zzjdVar) throws IOException {
        zzijVar.zza((zzij<UT, UB>) zzijVar.zza(t), zzjdVar);
    }

    private final zzhr zza(int i) {
        int i2 = (i / 3) << 1;
        zzhr zzhrVar = (zzhr) this.zzd[i2];
        if (zzhrVar != null) {
            return zzhrVar;
        }
        zzhr<T> zza2 = zzhm.zza().zza((Class) ((Class) this.zzd[i2 + 1]));
        this.zzd[i2] = zza2;
        return zza2;
    }

    private final Object zzb(int i) {
        return this.zzd[(i / 3) << 1];
    }

    @Override // com.google.android.gms.internal.mlkit_common.zzhr
    public final void zzb(T t) {
        int i;
        int i2 = this.zzm;
        while (true) {
            i = this.zzn;
            if (i2 >= i) {
                break;
            }
            long zzc = zzc(this.zzl[i2]) & 1048575;
            Object zzf = zzip.zzf(t, zzc);
            if (zzf != null) {
                zzip.zza(t, zzc, this.zzs.zzb(zzf));
            }
            i2++;
        }
        int length = this.zzl.length;
        while (i < length) {
            this.zzp.zza(t, this.zzl[i]);
            i++;
        }
        this.zzq.zzb(t);
        if (this.zzh) {
            this.zzr.zzc(t);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v14 */
    /* JADX WARN: Type inference failed for: r1v16, types: [com.google.android.gms.internal.mlkit_common.zzhr] */
    /* JADX WARN: Type inference failed for: r1v22 */
    /* JADX WARN: Type inference failed for: r1v5, types: [com.google.android.gms.internal.mlkit_common.zzhr] */
    @Override // com.google.android.gms.internal.mlkit_common.zzhr
    public final boolean zzc(T t) {
        int i;
        int i2;
        int i3 = 1048575;
        int i4 = 0;
        int i5 = 0;
        while (true) {
            boolean z = true;
            if (i5 >= this.zzm) {
                return !this.zzh || this.zzr.zza(t).zzf();
            }
            int i6 = this.zzl[i5];
            int i7 = this.zzc[i6];
            int zzc = zzc(i6);
            int i8 = this.zzc[i6 + 2];
            int i9 = i8 & 1048575;
            int i10 = 1 << (i8 >>> 20);
            if (i9 == i3) {
                i2 = i3;
                i = i4;
            } else if (i9 == 1048575) {
                i = i4;
                i2 = i9;
            } else {
                i = zzb.getInt(t, i9);
                i2 = i9;
            }
            if (((268435456 & zzc) != 0) && !zza(t, i6, i2, i, i10)) {
                return false;
            }
            switch ((267386880 & zzc) >>> 20) {
                case 9:
                case 17:
                    if (zza(t, i6, i2, i, i10) && !zza(t, zzc, zza(i6))) {
                        return false;
                    }
                    break;
                case 27:
                case 49:
                    List list = (List) zzip.zzf(t, zzc & 1048575);
                    if (!list.isEmpty()) {
                        ?? zza2 = zza(i6);
                        int i11 = 0;
                        while (true) {
                            if (i11 < list.size()) {
                                if (!zza2.zzc(list.get(i11))) {
                                    z = false;
                                } else {
                                    i11++;
                                }
                            }
                        }
                    }
                    if (z) {
                        break;
                    } else {
                        return false;
                    }
                case 50:
                    Map<?, ?> zza3 = this.zzs.zza(zzip.zzf(t, zzc & 1048575));
                    if (!zza3.isEmpty()) {
                        if (this.zzs.zzc(zzb(i6)).zzb.zza() == zzja.MESSAGE) {
                            zzhr<T> zzhrVar = 0;
                            Iterator<?> it = zza3.values().iterator();
                            while (true) {
                                if (it.hasNext()) {
                                    Object next = it.next();
                                    if (zzhrVar == null) {
                                        zzhrVar = zzhm.zza().zza((Class) next.getClass());
                                    }
                                    boolean zzc2 = zzhrVar.zzc(next);
                                    zzhrVar = zzhrVar;
                                    if (!zzc2) {
                                        z = false;
                                    }
                                }
                            }
                        }
                    }
                    if (z) {
                        break;
                    } else {
                        return false;
                    }
                case UndoView.ACTION_PHONE_COPIED /* 60 */:
                case 68:
                    if (zza((zzhf<T>) t, i7, i6) && !zza(t, zzc, zza(i6))) {
                        return false;
                    }
                    break;
            }
            i5++;
            i3 = i2;
            i4 = i;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static boolean zza(Object obj, int i, zzhr zzhrVar) {
        return zzhrVar.zzc(zzip.zzf(obj, i & 1048575));
    }

    private static void zza(int i, Object obj, zzjd zzjdVar) throws IOException {
        if (obj instanceof String) {
            zzjdVar.zza(i, (String) obj);
        } else {
            zzjdVar.zza(i, (zzep) obj);
        }
    }

    private final int zzc(int i) {
        return this.zzc[i + 1];
    }

    private final int zzd(int i) {
        return this.zzc[i + 2];
    }

    private static <T> double zzb(T t, long j) {
        return ((Double) zzip.zzf(t, j)).doubleValue();
    }

    private static <T> float zzc(T t, long j) {
        return ((Float) zzip.zzf(t, j)).floatValue();
    }

    private static <T> int zzd(T t, long j) {
        return ((Integer) zzip.zzf(t, j)).intValue();
    }

    private static <T> long zze(T t, long j) {
        return ((Long) zzip.zzf(t, j)).longValue();
    }

    private static <T> boolean zzf(T t, long j) {
        return ((Boolean) zzip.zzf(t, j)).booleanValue();
    }

    private final boolean zzc(T t, T t2, int i) {
        return zza((zzhf<T>) t, i) == zza((zzhf<T>) t2, i);
    }

    private final boolean zza(T t, int i, int i2, int i3, int i4) {
        if (i2 == 1048575) {
            return zza((zzhf<T>) t, i);
        }
        return (i3 & i4) != 0;
    }

    private final boolean zza(T t, int i) {
        int zzd = zzd(i);
        long j = zzd & 1048575;
        if (j != 1048575) {
            return (zzip.zza(t, j) & (1 << (zzd >>> 20))) != 0;
        }
        int zzc = zzc(i);
        long j2 = zzc & 1048575;
        switch ((zzc & 267386880) >>> 20) {
            case 0:
                return zzip.zze(t, j2) != FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE;
            case 1:
                return zzip.zzd(t, j2) != 0.0f;
            case 2:
                return zzip.zzb(t, j2) != 0;
            case 3:
                return zzip.zzb(t, j2) != 0;
            case 4:
                return zzip.zza(t, j2) != 0;
            case 5:
                return zzip.zzb(t, j2) != 0;
            case 6:
                return zzip.zza(t, j2) != 0;
            case 7:
                return zzip.zzc(t, j2);
            case 8:
                Object zzf = zzip.zzf(t, j2);
                if (zzf instanceof String) {
                    return !((String) zzf).isEmpty();
                } else if (!(zzf instanceof zzep)) {
                    throw new IllegalArgumentException();
                } else {
                    return !zzep.zza.equals(zzf);
                }
            case 9:
                return zzip.zzf(t, j2) != null;
            case 10:
                return !zzep.zza.equals(zzip.zzf(t, j2));
            case 11:
                return zzip.zza(t, j2) != 0;
            case 12:
                return zzip.zza(t, j2) != 0;
            case 13:
                return zzip.zza(t, j2) != 0;
            case 14:
                return zzip.zzb(t, j2) != 0;
            case 15:
                return zzip.zza(t, j2) != 0;
            case 16:
                return zzip.zzb(t, j2) != 0;
            case 17:
                return zzip.zzf(t, j2) != null;
            default:
                throw new IllegalArgumentException();
        }
    }

    private final void zzb(T t, int i) {
        int zzd = zzd(i);
        long j = 1048575 & zzd;
        if (j == 1048575) {
            return;
        }
        zzip.zza((Object) t, j, (1 << (zzd >>> 20)) | zzip.zza(t, j));
    }

    private final boolean zza(T t, int i, int i2) {
        return zzip.zza(t, (long) (zzd(i2) & 1048575)) == i;
    }

    private final void zzb(T t, int i, int i2) {
        zzip.zza((Object) t, zzd(i2) & 1048575, i);
    }
}
