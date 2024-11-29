package com.google.android.gms.internal.mlkit_language_id;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
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
final class zzgd implements zzgp {
    private static final int[] zza = new int[0];
    private static final Unsafe zzb = zzhn.zzc();
    private final int[] zzc;
    private final Object[] zzd;
    private final int zze;
    private final int zzf;
    private final zzfz zzg;
    private final boolean zzh;
    private final boolean zzi;
    private final boolean zzj;
    private final boolean zzk;
    private final int[] zzl;
    private final int zzm;
    private final int zzn;
    private final zzge zzo;
    private final zzfj zzp;
    private final zzhh zzq;
    private final zzee zzr;
    private final zzfs zzs;

    private zzgd(int[] iArr, Object[] objArr, int i, int i2, zzfz zzfzVar, boolean z, boolean z2, int[] iArr2, int i3, int i4, zzge zzgeVar, zzfj zzfjVar, zzhh zzhhVar, zzee zzeeVar, zzfs zzfsVar) {
        this.zzc = iArr;
        this.zzd = objArr;
        this.zze = i;
        this.zzf = i2;
        this.zzi = zzfzVar instanceof zzeo;
        this.zzj = z;
        this.zzh = zzeeVar != null && zzeeVar.zza(zzfzVar);
        this.zzk = false;
        this.zzl = iArr2;
        this.zzm = i3;
        this.zzn = i4;
        this.zzo = zzgeVar;
        this.zzp = zzfjVar;
        this.zzq = zzhhVar;
        this.zzr = zzeeVar;
        this.zzg = zzfzVar;
        this.zzs = zzfsVar;
    }

    private static int zza(zzhh zzhhVar, Object obj) {
        return zzhhVar.zzd(zzhhVar.zza(obj));
    }

    /* JADX WARN: Removed duplicated region for block: B:106:0x033b  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0399  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    static zzgd zza(Class cls, zzfx zzfxVar, zzge zzgeVar, zzfj zzfjVar, zzhh zzhhVar, zzee zzeeVar, zzfs zzfsVar) {
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
        zzgm zzgmVar;
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
        if (!(zzfxVar instanceof zzgm)) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzfxVar);
            throw null;
        }
        zzgm zzgmVar2 = (zzgm) zzfxVar;
        int i26 = 0;
        boolean z = zzgmVar2.zza() == zzgl.zzb;
        String zzd = zzgmVar2.zzd();
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
        Object[] zze = zzgmVar2.zze();
        Class<?> cls2 = zzgmVar2.zzc().getClass();
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
                zzgmVar = zzgmVar2;
                str = zzd;
                i17 = (int) unsafe.objectFieldOffset(zza4);
                i19 = 0;
            } else {
                int i82 = i58 + 1;
                Field zza5 = zza((Class) cls2, (String) zze[i58]);
                zzgmVar = zzgmVar2;
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
            zzgmVar2 = zzgmVar;
        }
        return new zzgd(iArr2, objArr, charAt, charAt2, zzgmVar2.zzc(), z, false, iArr, charAt5, i57, zzgeVar, zzfjVar, zzhhVar, zzeeVar, zzfsVar);
    }

    private final zzgp zza(int i) {
        int i2 = (i / 3) << 1;
        zzgp zzgpVar = (zzgp) this.zzd[i2];
        if (zzgpVar != null) {
            return zzgpVar;
        }
        zzgp zza2 = zzgk.zza().zza((Class) this.zzd[i2 + 1]);
        this.zzd[i2] = zza2;
        return zza2;
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
        return (List) zzhn.zzf(obj, j);
    }

    private static void zza(int i, Object obj, zzib zzibVar) {
        if (obj instanceof String) {
            zzibVar.zza(i, (String) obj);
        } else {
            zzibVar.zza(i, (zzdn) obj);
        }
    }

    private static void zza(zzhh zzhhVar, Object obj, zzib zzibVar) {
        zzhhVar.zza(zzhhVar.zza(obj), zzibVar);
    }

    private final void zza(zzib zzibVar, int i, Object obj, int i2) {
        if (obj != null) {
            this.zzs.zzc(zzb(i2));
            zzibVar.zza(i, (zzfq) null, this.zzs.zza(obj));
        }
    }

    private final void zza(Object obj, Object obj2, int i) {
        long zzc = zzc(i) & 1048575;
        if (zza(obj2, i)) {
            Object zzf = zzhn.zzf(obj, zzc);
            Object zzf2 = zzhn.zzf(obj2, zzc);
            if (zzf != null && zzf2 != null) {
                zzhn.zza(obj, zzc, zzeq.zza(zzf, zzf2));
                zzb(obj, i);
            } else if (zzf2 != null) {
                zzhn.zza(obj, zzc, zzf2);
                zzb(obj, i);
            }
        }
    }

    private final boolean zza(Object obj, int i) {
        int zzd = zzd(i);
        long j = zzd & 1048575;
        if (j != 1048575) {
            return (zzhn.zza(obj, j) & (1 << (zzd >>> 20))) != 0;
        }
        int zzc = zzc(i);
        long j2 = zzc & 1048575;
        switch ((zzc & 267386880) >>> 20) {
            case 0:
                return zzhn.zze(obj, j2) != 0.0d;
            case 1:
                return zzhn.zzd(obj, j2) != 0.0f;
            case 2:
                return zzhn.zzb(obj, j2) != 0;
            case 3:
                return zzhn.zzb(obj, j2) != 0;
            case 4:
                return zzhn.zza(obj, j2) != 0;
            case 5:
                return zzhn.zzb(obj, j2) != 0;
            case 6:
                return zzhn.zza(obj, j2) != 0;
            case 7:
                return zzhn.zzc(obj, j2);
            case 8:
                Object zzf = zzhn.zzf(obj, j2);
                if (zzf instanceof String) {
                    return !((String) zzf).isEmpty();
                }
                if (zzf instanceof zzdn) {
                    return !zzdn.zza.equals(zzf);
                }
                throw new IllegalArgumentException();
            case 9:
                return zzhn.zzf(obj, j2) != null;
            case 10:
                return !zzdn.zza.equals(zzhn.zzf(obj, j2));
            case 11:
                return zzhn.zza(obj, j2) != 0;
            case 12:
                return zzhn.zza(obj, j2) != 0;
            case 13:
                return zzhn.zza(obj, j2) != 0;
            case 14:
                return zzhn.zzb(obj, j2) != 0;
            case 15:
                return zzhn.zza(obj, j2) != 0;
            case 16:
                return zzhn.zzb(obj, j2) != 0;
            case 17:
                return zzhn.zzf(obj, j2) != null;
            default:
                throw new IllegalArgumentException();
        }
    }

    private final boolean zza(Object obj, int i, int i2) {
        return zzhn.zza(obj, (long) (zzd(i2) & 1048575)) == i;
    }

    private final boolean zza(Object obj, int i, int i2, int i3, int i4) {
        return i2 == 1048575 ? zza(obj, i) : (i3 & i4) != 0;
    }

    private static boolean zza(Object obj, int i, zzgp zzgpVar) {
        return zzgpVar.zzc(zzhn.zzf(obj, i & 1048575));
    }

    private static double zzb(Object obj, long j) {
        return ((Double) zzhn.zzf(obj, j)).doubleValue();
    }

    private final Object zzb(int i) {
        return this.zzd[(i / 3) << 1];
    }

    private final void zzb(Object obj, int i) {
        int zzd = zzd(i);
        long j = 1048575 & zzd;
        if (j == 1048575) {
            return;
        }
        zzhn.zza(obj, j, (1 << (zzd >>> 20)) | zzhn.zza(obj, j));
    }

    private final void zzb(Object obj, int i, int i2) {
        zzhn.zza(obj, zzd(i2) & 1048575, i);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:230:0x0423  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0031  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void zzb(Object obj, zzib zzibVar) {
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
            zzej zza2 = this.zzr.zza(obj);
            if (!zza2.zza.isEmpty()) {
                it = zza2.zzd();
                entry = (Map.Entry) it.next();
                length = this.zzc.length;
                Unsafe unsafe = zzb;
                int i17 = 1048575;
                int i18 = 0;
                for (i = 0; i < length; i += 3) {
                    int zzc = zzc(i);
                    int[] iArr = this.zzc;
                    int i19 = iArr[i];
                    int i20 = (zzc & 267386880) >>> 20;
                    if (this.zzj || i20 > 17) {
                        i2 = 0;
                    } else {
                        int i21 = iArr[i + 2];
                        int i22 = i21 & 1048575;
                        if (i22 != i17) {
                            i18 = unsafe.getInt(obj, i22);
                            i17 = i22;
                        }
                        i2 = 1 << (i21 >>> 20);
                    }
                    while (entry != null && this.zzr.zza(entry) <= i19) {
                        this.zzr.zza(zzibVar, entry);
                        entry = it.hasNext() ? (Map.Entry) it.next() : null;
                    }
                    long j = zzc & 1048575;
                    switch (i20) {
                        case 0:
                            if ((i2 & i18) != 0) {
                                zzibVar.zza(i19, zzhn.zze(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 1:
                            if ((i2 & i18) != 0) {
                                zzibVar.zza(i19, zzhn.zzd(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 2:
                            if ((i2 & i18) != 0) {
                                zzibVar.zza(i19, unsafe.getLong(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 3:
                            if ((i2 & i18) != 0) {
                                zzibVar.zzc(i19, unsafe.getLong(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 4:
                            if ((i2 & i18) != 0) {
                                zzibVar.zzc(i19, unsafe.getInt(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 5:
                            if ((i2 & i18) != 0) {
                                zzibVar.zzd(i19, unsafe.getLong(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 6:
                            if ((i2 & i18) != 0) {
                                zzibVar.zzd(i19, unsafe.getInt(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 7:
                            if ((i2 & i18) != 0) {
                                zzibVar.zza(i19, zzhn.zzc(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 8:
                            if ((i2 & i18) != 0) {
                                zza(i19, unsafe.getObject(obj, j), zzibVar);
                                break;
                            } else {
                                break;
                            }
                        case 9:
                            if ((i2 & i18) != 0) {
                                zzibVar.zza(i19, unsafe.getObject(obj, j), zza(i));
                                break;
                            } else {
                                break;
                            }
                        case 10:
                            if ((i2 & i18) != 0) {
                                zzibVar.zza(i19, (zzdn) unsafe.getObject(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 11:
                            if ((i2 & i18) != 0) {
                                zzibVar.zze(i19, unsafe.getInt(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 12:
                            if ((i2 & i18) != 0) {
                                zzibVar.zzb(i19, unsafe.getInt(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 13:
                            if ((i2 & i18) != 0) {
                                zzibVar.zza(i19, unsafe.getInt(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 14:
                            if ((i2 & i18) != 0) {
                                zzibVar.zzb(i19, unsafe.getLong(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 15:
                            if ((i2 & i18) != 0) {
                                zzibVar.zzf(i19, unsafe.getInt(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 16:
                            if ((i2 & i18) != 0) {
                                zzibVar.zze(i19, unsafe.getLong(obj, j));
                                break;
                            } else {
                                break;
                            }
                        case 17:
                            if ((i2 & i18) != 0) {
                                zzibVar.zzb(i19, unsafe.getObject(obj, j), zza(i));
                                break;
                            } else {
                                break;
                            }
                        case 18:
                            z = false;
                            i3 = this.zzc[i];
                            zzgr.zza(i3, (List) unsafe.getObject(obj, j), zzibVar, z);
                            break;
                        case 19:
                            z2 = false;
                            i4 = this.zzc[i];
                            zzgr.zzb(i4, (List) unsafe.getObject(obj, j), zzibVar, z2);
                            break;
                        case 20:
                            z3 = false;
                            i5 = this.zzc[i];
                            zzgr.zzc(i5, (List) unsafe.getObject(obj, j), zzibVar, z3);
                            break;
                        case 21:
                            z4 = false;
                            i6 = this.zzc[i];
                            zzgr.zzd(i6, (List) unsafe.getObject(obj, j), zzibVar, z4);
                            break;
                        case 22:
                            z5 = false;
                            i7 = this.zzc[i];
                            zzgr.zzh(i7, (List) unsafe.getObject(obj, j), zzibVar, z5);
                            break;
                        case 23:
                            z6 = false;
                            i8 = this.zzc[i];
                            zzgr.zzf(i8, (List) unsafe.getObject(obj, j), zzibVar, z6);
                            break;
                        case 24:
                            z7 = false;
                            i9 = this.zzc[i];
                            zzgr.zzk(i9, (List) unsafe.getObject(obj, j), zzibVar, z7);
                            break;
                        case 25:
                            i10 = this.zzc[i];
                            list = (List) unsafe.getObject(obj, j);
                            z8 = false;
                            zzgr.zzn(i10, list, zzibVar, z8);
                            break;
                        case 26:
                            zzgr.zza(this.zzc[i], (List) unsafe.getObject(obj, j), zzibVar);
                            break;
                        case 27:
                            zzgr.zza(this.zzc[i], (List) unsafe.getObject(obj, j), zzibVar, zza(i));
                            break;
                        case 28:
                            zzgr.zzb(this.zzc[i], (List) unsafe.getObject(obj, j), zzibVar);
                            break;
                        case 29:
                            z9 = false;
                            i11 = this.zzc[i];
                            zzgr.zzi(i11, (List) unsafe.getObject(obj, j), zzibVar, z9);
                            break;
                        case 30:
                            z10 = false;
                            i12 = this.zzc[i];
                            zzgr.zzm(i12, (List) unsafe.getObject(obj, j), zzibVar, z10);
                            break;
                        case 31:
                            z11 = false;
                            i13 = this.zzc[i];
                            zzgr.zzl(i13, (List) unsafe.getObject(obj, j), zzibVar, z11);
                            break;
                        case 32:
                            z12 = false;
                            i14 = this.zzc[i];
                            zzgr.zzg(i14, (List) unsafe.getObject(obj, j), zzibVar, z12);
                            break;
                        case 33:
                            z13 = false;
                            i15 = this.zzc[i];
                            zzgr.zzj(i15, (List) unsafe.getObject(obj, j), zzibVar, z13);
                            break;
                        case 34:
                            i16 = this.zzc[i];
                            list2 = (List) unsafe.getObject(obj, j);
                            z14 = false;
                            zzgr.zze(i16, list2, zzibVar, z14);
                            break;
                        case 35:
                            z = true;
                            i3 = this.zzc[i];
                            zzgr.zza(i3, (List) unsafe.getObject(obj, j), zzibVar, z);
                            break;
                        case 36:
                            z2 = true;
                            i4 = this.zzc[i];
                            zzgr.zzb(i4, (List) unsafe.getObject(obj, j), zzibVar, z2);
                            break;
                        case 37:
                            z3 = true;
                            i5 = this.zzc[i];
                            zzgr.zzc(i5, (List) unsafe.getObject(obj, j), zzibVar, z3);
                            break;
                        case 38:
                            z4 = true;
                            i6 = this.zzc[i];
                            zzgr.zzd(i6, (List) unsafe.getObject(obj, j), zzibVar, z4);
                            break;
                        case 39:
                            z5 = true;
                            i7 = this.zzc[i];
                            zzgr.zzh(i7, (List) unsafe.getObject(obj, j), zzibVar, z5);
                            break;
                        case 40:
                            z6 = true;
                            i8 = this.zzc[i];
                            zzgr.zzf(i8, (List) unsafe.getObject(obj, j), zzibVar, z6);
                            break;
                        case 41:
                            z7 = true;
                            i9 = this.zzc[i];
                            zzgr.zzk(i9, (List) unsafe.getObject(obj, j), zzibVar, z7);
                            break;
                        case 42:
                            z8 = true;
                            i10 = this.zzc[i];
                            list = (List) unsafe.getObject(obj, j);
                            zzgr.zzn(i10, list, zzibVar, z8);
                            break;
                        case 43:
                            z9 = true;
                            i11 = this.zzc[i];
                            zzgr.zzi(i11, (List) unsafe.getObject(obj, j), zzibVar, z9);
                            break;
                        case 44:
                            z10 = true;
                            i12 = this.zzc[i];
                            zzgr.zzm(i12, (List) unsafe.getObject(obj, j), zzibVar, z10);
                            break;
                        case 45:
                            z11 = true;
                            i13 = this.zzc[i];
                            zzgr.zzl(i13, (List) unsafe.getObject(obj, j), zzibVar, z11);
                            break;
                        case 46:
                            z12 = true;
                            i14 = this.zzc[i];
                            zzgr.zzg(i14, (List) unsafe.getObject(obj, j), zzibVar, z12);
                            break;
                        case 47:
                            z13 = true;
                            i15 = this.zzc[i];
                            zzgr.zzj(i15, (List) unsafe.getObject(obj, j), zzibVar, z13);
                            break;
                        case 48:
                            i16 = this.zzc[i];
                            list2 = (List) unsafe.getObject(obj, j);
                            z14 = true;
                            zzgr.zze(i16, list2, zzibVar, z14);
                            break;
                        case 49:
                            zzgr.zzb(this.zzc[i], (List) unsafe.getObject(obj, j), zzibVar, zza(i));
                            break;
                        case 50:
                            zza(zzibVar, i19, unsafe.getObject(obj, j), i);
                            break;
                        case 51:
                            if (zza(obj, i19, i)) {
                                zzibVar.zza(i19, zzb(obj, j));
                            }
                            break;
                        case 52:
                            if (zza(obj, i19, i)) {
                                zzibVar.zza(i19, zzc(obj, j));
                            }
                            break;
                        case 53:
                            if (zza(obj, i19, i)) {
                                zzibVar.zza(i19, zze(obj, j));
                            }
                            break;
                        case 54:
                            if (zza(obj, i19, i)) {
                                zzibVar.zzc(i19, zze(obj, j));
                            }
                            break;
                        case 55:
                            if (zza(obj, i19, i)) {
                                zzibVar.zzc(i19, zzd(obj, j));
                            }
                            break;
                        case 56:
                            if (zza(obj, i19, i)) {
                                zzibVar.zzd(i19, zze(obj, j));
                            }
                            break;
                        case 57:
                            if (zza(obj, i19, i)) {
                                zzibVar.zzd(i19, zzd(obj, j));
                            }
                            break;
                        case 58:
                            if (zza(obj, i19, i)) {
                                zzibVar.zza(i19, zzf(obj, j));
                            }
                            break;
                        case 59:
                            if (zza(obj, i19, i)) {
                                zza(i19, unsafe.getObject(obj, j), zzibVar);
                            }
                            break;
                        case 60:
                            if (zza(obj, i19, i)) {
                                zzibVar.zza(i19, unsafe.getObject(obj, j), zza(i));
                            }
                            break;
                        case 61:
                            if (zza(obj, i19, i)) {
                                zzibVar.zza(i19, (zzdn) unsafe.getObject(obj, j));
                            }
                            break;
                        case 62:
                            if (zza(obj, i19, i)) {
                                zzibVar.zze(i19, zzd(obj, j));
                            }
                            break;
                        case 63:
                            if (zza(obj, i19, i)) {
                                zzibVar.zzb(i19, zzd(obj, j));
                            }
                            break;
                        case 64:
                            if (zza(obj, i19, i)) {
                                zzibVar.zza(i19, zzd(obj, j));
                            }
                            break;
                        case 65:
                            if (zza(obj, i19, i)) {
                                zzibVar.zzb(i19, zze(obj, j));
                            }
                            break;
                        case 66:
                            if (zza(obj, i19, i)) {
                                zzibVar.zzf(i19, zzd(obj, j));
                            }
                            break;
                        case 67:
                            if (zza(obj, i19, i)) {
                                zzibVar.zze(i19, zze(obj, j));
                            }
                            break;
                        case 68:
                            if (zza(obj, i19, i)) {
                                zzibVar.zzb(i19, unsafe.getObject(obj, j), zza(i));
                            }
                            break;
                    }
                }
                while (entry != null) {
                    this.zzr.zza(zzibVar, entry);
                    entry = it.hasNext() ? (Map.Entry) it.next() : null;
                }
                zza(this.zzq, obj, zzibVar);
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
        zza(this.zzq, obj, zzibVar);
    }

    private final void zzb(Object obj, Object obj2, int i) {
        int zzc = zzc(i);
        int i2 = this.zzc[i];
        long j = zzc & 1048575;
        if (zza(obj2, i2, i)) {
            Object zzf = zzhn.zzf(obj, j);
            Object zzf2 = zzhn.zzf(obj2, j);
            if (zzf != null && zzf2 != null) {
                zzhn.zza(obj, j, zzeq.zza(zzf, zzf2));
                zzb(obj, i2, i);
            } else if (zzf2 != null) {
                zzhn.zza(obj, j, zzf2);
                zzb(obj, i2, i);
            }
        }
    }

    private static float zzc(Object obj, long j) {
        return ((Float) zzhn.zzf(obj, j)).floatValue();
    }

    private final int zzc(int i) {
        return this.zzc[i + 1];
    }

    private final boolean zzc(Object obj, Object obj2, int i) {
        return zza(obj, i) == zza(obj2, i);
    }

    private final int zzd(int i) {
        return this.zzc[i + 2];
    }

    private static int zzd(Object obj, long j) {
        return ((Integer) zzhn.zzf(obj, j)).intValue();
    }

    private static long zze(Object obj, long j) {
        return ((Long) zzhn.zzf(obj, j)).longValue();
    }

    private static boolean zzf(Object obj, long j) {
        return ((Boolean) zzhn.zzf(obj, j)).booleanValue();
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
    @Override // com.google.android.gms.internal.mlkit_language_id.zzgp
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
            int zzc2 = zzc(i3);
            int i4 = this.zzc[i3];
            long j = 1048575 & zzc2;
            int i5 = 37;
            switch ((zzc2 & 267386880) >>> 20) {
                case 0:
                    i = i2 * 53;
                    zze = zzhn.zze(obj, j);
                    zzb2 = Double.doubleToLongBits(zze);
                    zza2 = zzeq.zza(zzb2);
                    i2 = i + zza2;
                    break;
                case 1:
                    i = i2 * 53;
                    zzd = zzhn.zzd(obj, j);
                    zza2 = Float.floatToIntBits(zzd);
                    i2 = i + zza2;
                    break;
                case 2:
                case 3:
                case 5:
                case 14:
                case 16:
                    i = i2 * 53;
                    zzb2 = zzhn.zzb(obj, j);
                    zza2 = zzeq.zza(zzb2);
                    i2 = i + zza2;
                    break;
                case 4:
                case 6:
                case 11:
                case 12:
                case 13:
                case 15:
                    i = i2 * 53;
                    zza2 = zzhn.zza(obj, j);
                    i2 = i + zza2;
                    break;
                case 7:
                    i = i2 * 53;
                    zzc = zzhn.zzc(obj, j);
                    zza2 = zzeq.zza(zzc);
                    i2 = i + zza2;
                    break;
                case 8:
                    i = i2 * 53;
                    zza2 = ((String) zzhn.zzf(obj, j)).hashCode();
                    i2 = i + zza2;
                    break;
                case 9:
                    zzf = zzhn.zzf(obj, j);
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
                    zzf2 = zzhn.zzf(obj, j);
                    zza2 = zzf2.hashCode();
                    i2 = i + zza2;
                    break;
                case 17:
                    zzf = zzhn.zzf(obj, j);
                    break;
                case 51:
                    if (zza(obj, i4, i3)) {
                        i = i2 * 53;
                        zze = zzb(obj, j);
                        zzb2 = Double.doubleToLongBits(zze);
                        zza2 = zzeq.zza(zzb2);
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
                    zza2 = zzeq.zza(zzb2);
                    i2 = i + zza2;
                    break;
                case 54:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzb2 = zze(obj, j);
                    zza2 = zzeq.zza(zzb2);
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
                    zza2 = zzeq.zza(zzb2);
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
                        zza2 = zzeq.zza(zzc);
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
                    zza2 = ((String) zzhn.zzf(obj, j)).hashCode();
                    i2 = i + zza2;
                    break;
                case 60:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    zzf2 = zzhn.zzf(obj, j);
                    i = i2 * 53;
                    zza2 = zzf2.hashCode();
                    i2 = i + zza2;
                    break;
                case 61:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    i = i2 * 53;
                    zzf2 = zzhn.zzf(obj, j);
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
                    zza2 = zzeq.zza(zzb2);
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
                    zza2 = zzeq.zza(zzb2);
                    i2 = i + zza2;
                    break;
                case 68:
                    if (!zza(obj, i4, i3)) {
                        break;
                    }
                    zzf2 = zzhn.zzf(obj, j);
                    i = i2 * 53;
                    zza2 = zzf2.hashCode();
                    i2 = i + zza2;
                    break;
            }
        }
        int hashCode = (i2 * 53) + this.zzq.zza(obj).hashCode();
        return this.zzh ? (hashCode * 53) + this.zzr.zza(obj).hashCode() : hashCode;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x003b  */
    /* JADX WARN: Removed duplicated region for block: B:269:0x04c3  */
    /* JADX WARN: Removed duplicated region for block: B:291:0x0502  */
    /* JADX WARN: Removed duplicated region for block: B:550:0x098a  */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzgp
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void zza(Object obj, zzib zzibVar) {
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
        if (zzibVar.zza() == zzia.zzb) {
            zza(this.zzq, obj, zzibVar);
            if (this.zzh) {
                zzej zza14 = this.zzr.zza(obj);
                if (!zza14.zza.isEmpty()) {
                    it2 = zza14.zze();
                    entry2 = (Map.Entry) it2.next();
                    for (length2 = this.zzc.length - 3; length2 >= 0; length2 -= 3) {
                        int zzc3 = zzc(length2);
                        int i2 = this.zzc[length2];
                        while (entry2 != null && this.zzr.zza(entry2) > i2) {
                            this.zzr.zza(zzibVar, entry2);
                            entry2 = it2.hasNext() ? (Map.Entry) it2.next() : null;
                        }
                        switch ((zzc3 & 267386880) >>> 20) {
                            case 0:
                                if (zza(obj, length2)) {
                                    zze2 = zzhn.zze(obj, zzc3 & 1048575);
                                    zzibVar.zza(i2, zze2);
                                    break;
                                } else {
                                    break;
                                }
                            case 1:
                                if (zza(obj, length2)) {
                                    zzd2 = zzhn.zzd(obj, zzc3 & 1048575);
                                    zzibVar.zza(i2, zzd2);
                                    break;
                                } else {
                                    break;
                                }
                            case 2:
                                if (zza(obj, length2)) {
                                    zzb7 = zzhn.zzb(obj, zzc3 & 1048575);
                                    zzibVar.zza(i2, zzb7);
                                    break;
                                } else {
                                    break;
                                }
                            case 3:
                                if (zza(obj, length2)) {
                                    zzb8 = zzhn.zzb(obj, zzc3 & 1048575);
                                    zzibVar.zzc(i2, zzb8);
                                    break;
                                } else {
                                    break;
                                }
                            case 4:
                                if (zza(obj, length2)) {
                                    zza8 = zzhn.zza(obj, zzc3 & 1048575);
                                    zzibVar.zzc(i2, zza8);
                                    break;
                                } else {
                                    break;
                                }
                            case 5:
                                if (zza(obj, length2)) {
                                    zzb9 = zzhn.zzb(obj, zzc3 & 1048575);
                                    zzibVar.zzd(i2, zzb9);
                                    break;
                                } else {
                                    break;
                                }
                            case 6:
                                if (zza(obj, length2)) {
                                    zza9 = zzhn.zza(obj, zzc3 & 1048575);
                                    zzibVar.zzd(i2, zza9);
                                    break;
                                } else {
                                    break;
                                }
                            case 7:
                                if (zza(obj, length2)) {
                                    zzc2 = zzhn.zzc(obj, zzc3 & 1048575);
                                    zzibVar.zza(i2, zzc2);
                                    break;
                                } else {
                                    break;
                                }
                            case 8:
                                if (!zza(obj, length2)) {
                                    break;
                                }
                                zza(i2, zzhn.zzf(obj, zzc3 & 1048575), zzibVar);
                                break;
                            case 9:
                                if (!zza(obj, length2)) {
                                    break;
                                }
                                zzibVar.zza(i2, zzhn.zzf(obj, zzc3 & 1048575), zza(length2));
                                break;
                            case 10:
                                if (!zza(obj, length2)) {
                                    break;
                                }
                                zzibVar.zza(i2, (zzdn) zzhn.zzf(obj, zzc3 & 1048575));
                                break;
                            case 11:
                                if (zza(obj, length2)) {
                                    zza10 = zzhn.zza(obj, zzc3 & 1048575);
                                    zzibVar.zze(i2, zza10);
                                    break;
                                } else {
                                    break;
                                }
                            case 12:
                                if (zza(obj, length2)) {
                                    zza11 = zzhn.zza(obj, zzc3 & 1048575);
                                    zzibVar.zzb(i2, zza11);
                                    break;
                                } else {
                                    break;
                                }
                            case 13:
                                if (zza(obj, length2)) {
                                    zza12 = zzhn.zza(obj, zzc3 & 1048575);
                                    zzibVar.zza(i2, zza12);
                                    break;
                                } else {
                                    break;
                                }
                            case 14:
                                if (zza(obj, length2)) {
                                    zzb10 = zzhn.zzb(obj, zzc3 & 1048575);
                                    zzibVar.zzb(i2, zzb10);
                                    break;
                                } else {
                                    break;
                                }
                            case 15:
                                if (zza(obj, length2)) {
                                    zza13 = zzhn.zza(obj, zzc3 & 1048575);
                                    zzibVar.zzf(i2, zza13);
                                    break;
                                } else {
                                    break;
                                }
                            case 16:
                                if (zza(obj, length2)) {
                                    zzb11 = zzhn.zzb(obj, zzc3 & 1048575);
                                    zzibVar.zze(i2, zzb11);
                                    break;
                                } else {
                                    break;
                                }
                            case 17:
                                if (!zza(obj, length2)) {
                                    break;
                                }
                                zzibVar.zzb(i2, zzhn.zzf(obj, zzc3 & 1048575), zza(length2));
                                break;
                            case 18:
                                zzgr.zza(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, false);
                                break;
                            case 19:
                                zzgr.zzb(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, false);
                                break;
                            case 20:
                                zzgr.zzc(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, false);
                                break;
                            case 21:
                                zzgr.zzd(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, false);
                                break;
                            case 22:
                                zzgr.zzh(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, false);
                                break;
                            case 23:
                                zzgr.zzf(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, false);
                                break;
                            case 24:
                                zzgr.zzk(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, false);
                                break;
                            case 25:
                                zzgr.zzn(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, false);
                                break;
                            case 26:
                                zzgr.zza(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar);
                                break;
                            case 27:
                                zzgr.zza(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, zza(length2));
                                break;
                            case 28:
                                zzgr.zzb(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar);
                                break;
                            case 29:
                                zzgr.zzi(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, false);
                                break;
                            case 30:
                                zzgr.zzm(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, false);
                                break;
                            case 31:
                                zzgr.zzl(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, false);
                                break;
                            case 32:
                                zzgr.zzg(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, false);
                                break;
                            case 33:
                                zzgr.zzj(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, false);
                                break;
                            case 34:
                                zzgr.zze(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, false);
                                break;
                            case 35:
                                zzgr.zza(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, true);
                                break;
                            case 36:
                                zzgr.zzb(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, true);
                                break;
                            case 37:
                                zzgr.zzc(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, true);
                                break;
                            case 38:
                                zzgr.zzd(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, true);
                                break;
                            case 39:
                                zzgr.zzh(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, true);
                                break;
                            case 40:
                                zzgr.zzf(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, true);
                                break;
                            case 41:
                                zzgr.zzk(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, true);
                                break;
                            case 42:
                                zzgr.zzn(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, true);
                                break;
                            case 43:
                                zzgr.zzi(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, true);
                                break;
                            case 44:
                                zzgr.zzm(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, true);
                                break;
                            case 45:
                                zzgr.zzl(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, true);
                                break;
                            case 46:
                                zzgr.zzg(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, true);
                                break;
                            case 47:
                                zzgr.zzj(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, true);
                                break;
                            case 48:
                                zzgr.zze(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, true);
                                break;
                            case 49:
                                zzgr.zzb(this.zzc[length2], (List) zzhn.zzf(obj, zzc3 & 1048575), zzibVar, zza(length2));
                                break;
                            case 50:
                                zza(zzibVar, i2, zzhn.zzf(obj, zzc3 & 1048575), length2);
                                break;
                            case 51:
                                if (zza(obj, i2, length2)) {
                                    zze2 = zzb(obj, zzc3 & 1048575);
                                    zzibVar.zza(i2, zze2);
                                    break;
                                } else {
                                    break;
                                }
                            case 52:
                                if (zza(obj, i2, length2)) {
                                    zzd2 = zzc(obj, zzc3 & 1048575);
                                    zzibVar.zza(i2, zzd2);
                                    break;
                                } else {
                                    break;
                                }
                            case 53:
                                if (zza(obj, i2, length2)) {
                                    zzb7 = zze(obj, zzc3 & 1048575);
                                    zzibVar.zza(i2, zzb7);
                                    break;
                                } else {
                                    break;
                                }
                            case 54:
                                if (zza(obj, i2, length2)) {
                                    zzb8 = zze(obj, zzc3 & 1048575);
                                    zzibVar.zzc(i2, zzb8);
                                    break;
                                } else {
                                    break;
                                }
                            case 55:
                                if (zza(obj, i2, length2)) {
                                    zza8 = zzd(obj, zzc3 & 1048575);
                                    zzibVar.zzc(i2, zza8);
                                    break;
                                } else {
                                    break;
                                }
                            case 56:
                                if (zza(obj, i2, length2)) {
                                    zzb9 = zze(obj, zzc3 & 1048575);
                                    zzibVar.zzd(i2, zzb9);
                                    break;
                                } else {
                                    break;
                                }
                            case 57:
                                if (zza(obj, i2, length2)) {
                                    zza9 = zzd(obj, zzc3 & 1048575);
                                    zzibVar.zzd(i2, zza9);
                                    break;
                                } else {
                                    break;
                                }
                            case 58:
                                if (zza(obj, i2, length2)) {
                                    zzc2 = zzf(obj, zzc3 & 1048575);
                                    zzibVar.zza(i2, zzc2);
                                    break;
                                } else {
                                    break;
                                }
                            case 59:
                                if (!zza(obj, i2, length2)) {
                                    break;
                                }
                                zza(i2, zzhn.zzf(obj, zzc3 & 1048575), zzibVar);
                                break;
                            case 60:
                                if (!zza(obj, i2, length2)) {
                                    break;
                                }
                                zzibVar.zza(i2, zzhn.zzf(obj, zzc3 & 1048575), zza(length2));
                                break;
                            case 61:
                                if (!zza(obj, i2, length2)) {
                                    break;
                                }
                                zzibVar.zza(i2, (zzdn) zzhn.zzf(obj, zzc3 & 1048575));
                                break;
                            case 62:
                                if (zza(obj, i2, length2)) {
                                    zza10 = zzd(obj, zzc3 & 1048575);
                                    zzibVar.zze(i2, zza10);
                                    break;
                                } else {
                                    break;
                                }
                            case 63:
                                if (zza(obj, i2, length2)) {
                                    zza11 = zzd(obj, zzc3 & 1048575);
                                    zzibVar.zzb(i2, zza11);
                                    break;
                                } else {
                                    break;
                                }
                            case 64:
                                if (zza(obj, i2, length2)) {
                                    zza12 = zzd(obj, zzc3 & 1048575);
                                    zzibVar.zza(i2, zza12);
                                    break;
                                } else {
                                    break;
                                }
                            case 65:
                                if (zza(obj, i2, length2)) {
                                    zzb10 = zze(obj, zzc3 & 1048575);
                                    zzibVar.zzb(i2, zzb10);
                                    break;
                                } else {
                                    break;
                                }
                            case 66:
                                if (zza(obj, i2, length2)) {
                                    zza13 = zzd(obj, zzc3 & 1048575);
                                    zzibVar.zzf(i2, zza13);
                                    break;
                                } else {
                                    break;
                                }
                            case 67:
                                if (zza(obj, i2, length2)) {
                                    zzb11 = zze(obj, zzc3 & 1048575);
                                    zzibVar.zze(i2, zzb11);
                                    break;
                                } else {
                                    break;
                                }
                            case 68:
                                if (!zza(obj, i2, length2)) {
                                    break;
                                }
                                zzibVar.zzb(i2, zzhn.zzf(obj, zzc3 & 1048575), zza(length2));
                                break;
                        }
                    }
                    while (entry2 != null) {
                        this.zzr.zza(zzibVar, entry2);
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
            zzb(obj, zzibVar);
            return;
        }
        if (this.zzh) {
            zzej zza15 = this.zzr.zza(obj);
            if (!zza15.zza.isEmpty()) {
                it = zza15.zzd();
                entry = (Map.Entry) it.next();
                length = this.zzc.length;
                for (i = 0; i < length; i += 3) {
                    int zzc4 = zzc(i);
                    int i3 = this.zzc[i];
                    while (entry != null && this.zzr.zza(entry) <= i3) {
                        this.zzr.zza(zzibVar, entry);
                        entry = it.hasNext() ? (Map.Entry) it.next() : null;
                    }
                    switch ((zzc4 & 267386880) >>> 20) {
                        case 0:
                            if (zza(obj, i)) {
                                zze = zzhn.zze(obj, zzc4 & 1048575);
                                zzibVar.zza(i3, zze);
                                break;
                            } else {
                                break;
                            }
                        case 1:
                            if (zza(obj, i)) {
                                zzd = zzhn.zzd(obj, zzc4 & 1048575);
                                zzibVar.zza(i3, zzd);
                                break;
                            } else {
                                break;
                            }
                        case 2:
                            if (zza(obj, i)) {
                                zzb2 = zzhn.zzb(obj, zzc4 & 1048575);
                                zzibVar.zza(i3, zzb2);
                                break;
                            } else {
                                break;
                            }
                        case 3:
                            if (zza(obj, i)) {
                                zzb3 = zzhn.zzb(obj, zzc4 & 1048575);
                                zzibVar.zzc(i3, zzb3);
                                break;
                            } else {
                                break;
                            }
                        case 4:
                            if (zza(obj, i)) {
                                zza2 = zzhn.zza(obj, zzc4 & 1048575);
                                zzibVar.zzc(i3, zza2);
                                break;
                            } else {
                                break;
                            }
                        case 5:
                            if (zza(obj, i)) {
                                zzb4 = zzhn.zzb(obj, zzc4 & 1048575);
                                zzibVar.zzd(i3, zzb4);
                                break;
                            } else {
                                break;
                            }
                        case 6:
                            if (zza(obj, i)) {
                                zza3 = zzhn.zza(obj, zzc4 & 1048575);
                                zzibVar.zzd(i3, zza3);
                                break;
                            } else {
                                break;
                            }
                        case 7:
                            if (zza(obj, i)) {
                                zzc = zzhn.zzc(obj, zzc4 & 1048575);
                                zzibVar.zza(i3, zzc);
                                break;
                            } else {
                                break;
                            }
                        case 8:
                            if (!zza(obj, i)) {
                                break;
                            }
                            zza(i3, zzhn.zzf(obj, zzc4 & 1048575), zzibVar);
                            break;
                        case 9:
                            if (!zza(obj, i)) {
                                break;
                            }
                            zzibVar.zza(i3, zzhn.zzf(obj, zzc4 & 1048575), zza(i));
                            break;
                        case 10:
                            if (!zza(obj, i)) {
                                break;
                            }
                            zzibVar.zza(i3, (zzdn) zzhn.zzf(obj, zzc4 & 1048575));
                            break;
                        case 11:
                            if (zza(obj, i)) {
                                zza4 = zzhn.zza(obj, zzc4 & 1048575);
                                zzibVar.zze(i3, zza4);
                                break;
                            } else {
                                break;
                            }
                        case 12:
                            if (zza(obj, i)) {
                                zza5 = zzhn.zza(obj, zzc4 & 1048575);
                                zzibVar.zzb(i3, zza5);
                                break;
                            } else {
                                break;
                            }
                        case 13:
                            if (zza(obj, i)) {
                                zza6 = zzhn.zza(obj, zzc4 & 1048575);
                                zzibVar.zza(i3, zza6);
                                break;
                            } else {
                                break;
                            }
                        case 14:
                            if (zza(obj, i)) {
                                zzb5 = zzhn.zzb(obj, zzc4 & 1048575);
                                zzibVar.zzb(i3, zzb5);
                                break;
                            } else {
                                break;
                            }
                        case 15:
                            if (zza(obj, i)) {
                                zza7 = zzhn.zza(obj, zzc4 & 1048575);
                                zzibVar.zzf(i3, zza7);
                                break;
                            } else {
                                break;
                            }
                        case 16:
                            if (zza(obj, i)) {
                                zzb6 = zzhn.zzb(obj, zzc4 & 1048575);
                                zzibVar.zze(i3, zzb6);
                                break;
                            } else {
                                break;
                            }
                        case 17:
                            if (!zza(obj, i)) {
                                break;
                            }
                            zzibVar.zzb(i3, zzhn.zzf(obj, zzc4 & 1048575), zza(i));
                            break;
                        case 18:
                            zzgr.zza(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, false);
                            break;
                        case 19:
                            zzgr.zzb(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, false);
                            break;
                        case 20:
                            zzgr.zzc(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, false);
                            break;
                        case 21:
                            zzgr.zzd(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, false);
                            break;
                        case 22:
                            zzgr.zzh(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, false);
                            break;
                        case 23:
                            zzgr.zzf(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, false);
                            break;
                        case 24:
                            zzgr.zzk(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, false);
                            break;
                        case 25:
                            zzgr.zzn(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, false);
                            break;
                        case 26:
                            zzgr.zza(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar);
                            break;
                        case 27:
                            zzgr.zza(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, zza(i));
                            break;
                        case 28:
                            zzgr.zzb(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar);
                            break;
                        case 29:
                            zzgr.zzi(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, false);
                            break;
                        case 30:
                            zzgr.zzm(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, false);
                            break;
                        case 31:
                            zzgr.zzl(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, false);
                            break;
                        case 32:
                            zzgr.zzg(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, false);
                            break;
                        case 33:
                            zzgr.zzj(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, false);
                            break;
                        case 34:
                            zzgr.zze(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, false);
                            break;
                        case 35:
                            zzgr.zza(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, true);
                            break;
                        case 36:
                            zzgr.zzb(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, true);
                            break;
                        case 37:
                            zzgr.zzc(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, true);
                            break;
                        case 38:
                            zzgr.zzd(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, true);
                            break;
                        case 39:
                            zzgr.zzh(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, true);
                            break;
                        case 40:
                            zzgr.zzf(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, true);
                            break;
                        case 41:
                            zzgr.zzk(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, true);
                            break;
                        case 42:
                            zzgr.zzn(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, true);
                            break;
                        case 43:
                            zzgr.zzi(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, true);
                            break;
                        case 44:
                            zzgr.zzm(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, true);
                            break;
                        case 45:
                            zzgr.zzl(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, true);
                            break;
                        case 46:
                            zzgr.zzg(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, true);
                            break;
                        case 47:
                            zzgr.zzj(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, true);
                            break;
                        case 48:
                            zzgr.zze(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, true);
                            break;
                        case 49:
                            zzgr.zzb(this.zzc[i], (List) zzhn.zzf(obj, zzc4 & 1048575), zzibVar, zza(i));
                            break;
                        case 50:
                            zza(zzibVar, i3, zzhn.zzf(obj, zzc4 & 1048575), i);
                            break;
                        case 51:
                            if (zza(obj, i3, i)) {
                                zze = zzb(obj, zzc4 & 1048575);
                                zzibVar.zza(i3, zze);
                                break;
                            } else {
                                break;
                            }
                        case 52:
                            if (zza(obj, i3, i)) {
                                zzd = zzc(obj, zzc4 & 1048575);
                                zzibVar.zza(i3, zzd);
                                break;
                            } else {
                                break;
                            }
                        case 53:
                            if (zza(obj, i3, i)) {
                                zzb2 = zze(obj, zzc4 & 1048575);
                                zzibVar.zza(i3, zzb2);
                                break;
                            } else {
                                break;
                            }
                        case 54:
                            if (zza(obj, i3, i)) {
                                zzb3 = zze(obj, zzc4 & 1048575);
                                zzibVar.zzc(i3, zzb3);
                                break;
                            } else {
                                break;
                            }
                        case 55:
                            if (zza(obj, i3, i)) {
                                zza2 = zzd(obj, zzc4 & 1048575);
                                zzibVar.zzc(i3, zza2);
                                break;
                            } else {
                                break;
                            }
                        case 56:
                            if (zza(obj, i3, i)) {
                                zzb4 = zze(obj, zzc4 & 1048575);
                                zzibVar.zzd(i3, zzb4);
                                break;
                            } else {
                                break;
                            }
                        case 57:
                            if (zza(obj, i3, i)) {
                                zza3 = zzd(obj, zzc4 & 1048575);
                                zzibVar.zzd(i3, zza3);
                                break;
                            } else {
                                break;
                            }
                        case 58:
                            if (zza(obj, i3, i)) {
                                zzc = zzf(obj, zzc4 & 1048575);
                                zzibVar.zza(i3, zzc);
                                break;
                            } else {
                                break;
                            }
                        case 59:
                            if (!zza(obj, i3, i)) {
                                break;
                            }
                            zza(i3, zzhn.zzf(obj, zzc4 & 1048575), zzibVar);
                            break;
                        case 60:
                            if (!zza(obj, i3, i)) {
                                break;
                            }
                            zzibVar.zza(i3, zzhn.zzf(obj, zzc4 & 1048575), zza(i));
                            break;
                        case 61:
                            if (!zza(obj, i3, i)) {
                                break;
                            }
                            zzibVar.zza(i3, (zzdn) zzhn.zzf(obj, zzc4 & 1048575));
                            break;
                        case 62:
                            if (zza(obj, i3, i)) {
                                zza4 = zzd(obj, zzc4 & 1048575);
                                zzibVar.zze(i3, zza4);
                                break;
                            } else {
                                break;
                            }
                        case 63:
                            if (zza(obj, i3, i)) {
                                zza5 = zzd(obj, zzc4 & 1048575);
                                zzibVar.zzb(i3, zza5);
                                break;
                            } else {
                                break;
                            }
                        case 64:
                            if (zza(obj, i3, i)) {
                                zza6 = zzd(obj, zzc4 & 1048575);
                                zzibVar.zza(i3, zza6);
                                break;
                            } else {
                                break;
                            }
                        case 65:
                            if (zza(obj, i3, i)) {
                                zzb5 = zze(obj, zzc4 & 1048575);
                                zzibVar.zzb(i3, zzb5);
                                break;
                            } else {
                                break;
                            }
                        case 66:
                            if (zza(obj, i3, i)) {
                                zza7 = zzd(obj, zzc4 & 1048575);
                                zzibVar.zzf(i3, zza7);
                                break;
                            } else {
                                break;
                            }
                        case 67:
                            if (zza(obj, i3, i)) {
                                zzb6 = zze(obj, zzc4 & 1048575);
                                zzibVar.zze(i3, zzb6);
                                break;
                            } else {
                                break;
                            }
                        case 68:
                            if (!zza(obj, i3, i)) {
                                break;
                            }
                            zzibVar.zzb(i3, zzhn.zzf(obj, zzc4 & 1048575), zza(i));
                            break;
                    }
                }
                while (entry != null) {
                    this.zzr.zza(zzibVar, entry);
                    entry = it.hasNext() ? (Map.Entry) it.next() : null;
                }
                zza(this.zzq, obj, zzibVar);
            }
        }
        it = null;
        entry = null;
        length = this.zzc.length;
        while (i < length) {
        }
        while (entry != null) {
        }
        zza(this.zzq, obj, zzibVar);
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x005d, code lost:
    
        if (com.google.android.gms.internal.mlkit_language_id.zzgr.zza(com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r10, r6), com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r11, r6)) != false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0070, code lost:
    
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0081, code lost:
    
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0094, code lost:
    
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00a5, code lost:
    
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00b6, code lost:
    
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00c7, code lost:
    
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00dd, code lost:
    
        if (com.google.android.gms.internal.mlkit_language_id.zzgr.zza(com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r10, r6), com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r11, r6)) != false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00f3, code lost:
    
        if (com.google.android.gms.internal.mlkit_language_id.zzgr.zza(com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r10, r6), com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r11, r6)) != false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0109, code lost:
    
        if (com.google.android.gms.internal.mlkit_language_id.zzgr.zza(com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r10, r6), com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r11, r6)) != false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x011b, code lost:
    
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zzc(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zzc(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x012d, code lost:
    
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0141, code lost:
    
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0153, code lost:
    
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0167, code lost:
    
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x017b, code lost:
    
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r11, r6)) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0195, code lost:
    
        if (java.lang.Float.floatToIntBits(com.google.android.gms.internal.mlkit_language_id.zzhn.zzd(r10, r6)) == java.lang.Float.floatToIntBits(com.google.android.gms.internal.mlkit_language_id.zzhn.zzd(r11, r6))) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x01b1, code lost:
    
        if (java.lang.Double.doubleToLongBits(com.google.android.gms.internal.mlkit_language_id.zzhn.zze(r10, r6)) == java.lang.Double.doubleToLongBits(com.google.android.gms.internal.mlkit_language_id.zzhn.zze(r11, r6))) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0038, code lost:
    
        if (com.google.android.gms.internal.mlkit_language_id.zzgr.zza(com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r10, r6), com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r11, r6)) != false) goto L104;
     */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzgp
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final boolean zza(Object obj, Object obj2) {
        int length = this.zzc.length;
        int i = 0;
        while (true) {
            boolean z = true;
            if (i >= length) {
                if (!this.zzq.zza(obj).equals(this.zzq.zza(obj2))) {
                    return false;
                }
                if (this.zzh) {
                    return this.zzr.zza(obj).equals(this.zzr.zza(obj2));
                }
                return true;
            }
            int zzc = zzc(i);
            long j = zzc & 1048575;
            switch ((zzc & 267386880) >>> 20) {
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
                    z = zzgr.zza(zzhn.zzf(obj, j), zzhn.zzf(obj2, j));
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
                    long zzd = zzd(i) & 1048575;
                    if (zzhn.zza(obj, zzd) == zzhn.zza(obj2, zzd)) {
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

    @Override // com.google.android.gms.internal.mlkit_language_id.zzgp
    public final void zzb(Object obj) {
        int i;
        int i2 = this.zzm;
        while (true) {
            i = this.zzn;
            if (i2 >= i) {
                break;
            }
            long zzc = zzc(this.zzl[i2]) & 1048575;
            Object zzf = zzhn.zzf(obj, zzc);
            if (zzf != null) {
                zzhn.zza(obj, zzc, this.zzs.zzb(zzf));
            }
            i2++;
        }
        int length = this.zzl.length;
        while (i < length) {
            this.zzp.zza(obj, this.zzl[i]);
            i++;
        }
        this.zzq.zzb(obj);
        if (this.zzh) {
            this.zzr.zzc(obj);
        }
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzgp
    public final void zzb(Object obj, Object obj2) {
        obj2.getClass();
        for (int i = 0; i < this.zzc.length; i += 3) {
            int zzc = zzc(i);
            long j = 1048575 & zzc;
            int i2 = this.zzc[i];
            switch ((zzc & 267386880) >>> 20) {
                case 0:
                    if (zza(obj2, i)) {
                        zzhn.zza(obj, j, zzhn.zze(obj2, j));
                        zzb(obj, i);
                        break;
                    } else {
                        break;
                    }
                case 1:
                    if (zza(obj2, i)) {
                        zzhn.zza(obj, j, zzhn.zzd(obj2, j));
                        zzb(obj, i);
                        break;
                    } else {
                        break;
                    }
                case 2:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzhn.zza(obj, j, zzhn.zzb(obj2, j));
                    zzb(obj, i);
                    break;
                case 3:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzhn.zza(obj, j, zzhn.zzb(obj2, j));
                    zzb(obj, i);
                    break;
                case 4:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzhn.zza(obj, j, zzhn.zza(obj2, j));
                    zzb(obj, i);
                    break;
                case 5:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzhn.zza(obj, j, zzhn.zzb(obj2, j));
                    zzb(obj, i);
                    break;
                case 6:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzhn.zza(obj, j, zzhn.zza(obj2, j));
                    zzb(obj, i);
                    break;
                case 7:
                    if (zza(obj2, i)) {
                        zzhn.zza(obj, j, zzhn.zzc(obj2, j));
                        zzb(obj, i);
                        break;
                    } else {
                        break;
                    }
                case 8:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzhn.zza(obj, j, zzhn.zzf(obj2, j));
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
                    zzhn.zza(obj, j, zzhn.zzf(obj2, j));
                    zzb(obj, i);
                    break;
                case 11:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzhn.zza(obj, j, zzhn.zza(obj2, j));
                    zzb(obj, i);
                    break;
                case 12:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzhn.zza(obj, j, zzhn.zza(obj2, j));
                    zzb(obj, i);
                    break;
                case 13:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzhn.zza(obj, j, zzhn.zza(obj2, j));
                    zzb(obj, i);
                    break;
                case 14:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzhn.zza(obj, j, zzhn.zzb(obj2, j));
                    zzb(obj, i);
                    break;
                case 15:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzhn.zza(obj, j, zzhn.zza(obj2, j));
                    zzb(obj, i);
                    break;
                case 16:
                    if (!zza(obj2, i)) {
                        break;
                    }
                    zzhn.zza(obj, j, zzhn.zzb(obj2, j));
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
                    zzgr.zza(this.zzs, obj, obj2, j);
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
                    zzhn.zza(obj, j, zzhn.zzf(obj2, j));
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
                    zzhn.zza(obj, j, zzhn.zzf(obj2, j));
                    zzb(obj, i2, i);
                    break;
            }
        }
        zzgr.zza(this.zzq, obj, obj2);
        if (this.zzh) {
            zzgr.zza(this.zzr, obj, obj2);
        }
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzgp
    public final boolean zzc(Object obj) {
        int i;
        int i2;
        int i3 = 1048575;
        int i4 = 0;
        int i5 = 0;
        while (i5 < this.zzm) {
            int i6 = this.zzl[i5];
            int i7 = this.zzc[i6];
            int zzc = zzc(i6);
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
            if ((268435456 & zzc) != 0 && !zza(obj, i6, i, i2, i10)) {
                return false;
            }
            int i11 = (267386880 & zzc) >>> 20;
            if (i11 != 9 && i11 != 17) {
                if (i11 != 27) {
                    if (i11 == 60 || i11 == 68) {
                        if (zza(obj, i7, i6) && !zza(obj, zzc, zza(i6))) {
                            return false;
                        }
                    } else if (i11 != 49) {
                        if (i11 == 50 && !this.zzs.zza(zzhn.zzf(obj, zzc & 1048575)).isEmpty()) {
                            this.zzs.zzc(zzb(i6));
                            throw null;
                        }
                    }
                }
                List list = (List) zzhn.zzf(obj, zzc & 1048575);
                if (list.isEmpty()) {
                    continue;
                } else {
                    zzgp zza2 = zza(i6);
                    for (int i12 = 0; i12 < list.size(); i12++) {
                        if (!zza2.zzc(list.get(i12))) {
                            return false;
                        }
                    }
                }
            } else if (zza(obj, i6, i, i2, i10) && !zza(obj, zzc, zza(i6))) {
                return false;
            }
            i5++;
            i3 = i;
            i4 = i2;
        }
        return !this.zzh || this.zzr.zza(obj).zzf();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:189:0x0324, code lost:
    
        if ((r4 instanceof com.google.android.gms.internal.mlkit_language_id.zzdn) != false) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:234:0x03c0, code lost:
    
        if (zza(r19, r15, r3) != false) goto L210;
     */
    /* JADX WARN: Code restructure failed: missing block: B:235:0x03c2, code lost:
    
        r6 = com.google.android.gms.internal.mlkit_language_id.zzea.zzc(r15, (com.google.android.gms.internal.mlkit_language_id.zzfz) r2.getObject(r19, r8), zza(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:246:0x03fb, code lost:
    
        if (zza(r19, r15, r3) != false) goto L224;
     */
    /* JADX WARN: Code restructure failed: missing block: B:247:0x03fd, code lost:
    
        r6 = com.google.android.gms.internal.mlkit_language_id.zzea.zzh(r15, 0L);
     */
    /* JADX WARN: Code restructure failed: missing block: B:249:0x0408, code lost:
    
        if (zza(r19, r15, r3) != false) goto L227;
     */
    /* JADX WARN: Code restructure failed: missing block: B:250:0x040a, code lost:
    
        r8 = com.google.android.gms.internal.mlkit_language_id.zzea.zzj(r15, 0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:261:0x0433, code lost:
    
        if (zza(r19, r15, r3) != false) goto L239;
     */
    /* JADX WARN: Code restructure failed: missing block: B:262:0x0435, code lost:
    
        r6 = r2.getObject(r19, r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:263:0x0439, code lost:
    
        r6 = com.google.android.gms.internal.mlkit_language_id.zzea.zzc(r15, (com.google.android.gms.internal.mlkit_language_id.zzdn) r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:265:0x0444, code lost:
    
        if (zza(r19, r15, r3) != false) goto L243;
     */
    /* JADX WARN: Code restructure failed: missing block: B:266:0x0446, code lost:
    
        r6 = com.google.android.gms.internal.mlkit_language_id.zzgr.zza(r15, r2.getObject(r19, r8), zza(r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:270:0x0460, code lost:
    
        if ((r6 instanceof com.google.android.gms.internal.mlkit_language_id.zzdn) != false) goto L240;
     */
    /* JADX WARN: Code restructure failed: missing block: B:271:0x0463, code lost:
    
        r6 = com.google.android.gms.internal.mlkit_language_id.zzea.zzb(r15, (java.lang.String) r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:273:0x046f, code lost:
    
        if (zza(r19, r15, r3) != false) goto L252;
     */
    /* JADX WARN: Code restructure failed: missing block: B:274:0x0471, code lost:
    
        r8 = com.google.android.gms.internal.mlkit_language_id.zzea.zzb(r15, true);
     */
    /* JADX WARN: Code restructure failed: missing block: B:299:0x0506, code lost:
    
        if (r6 > 0) goto L278;
     */
    /* JADX WARN: Code restructure failed: missing block: B:300:0x0508, code lost:
    
        r8 = (com.google.android.gms.internal.mlkit_language_id.zzea.zze(r15) + com.google.android.gms.internal.mlkit_language_id.zzea.zzg(r6)) + r6;
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
    
        if ((r6 instanceof com.google.android.gms.internal.mlkit_language_id.zzdn) != false) goto L240;
     */
    /* JADX WARN: Code restructure failed: missing block: B:373:0x06ce, code lost:
    
        if ((r12 & r11) != 0) goto L252;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00dd, code lost:
    
        if ((r4 instanceof com.google.android.gms.internal.mlkit_language_id.zzdn) != false) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00e0, code lost:
    
        r3 = com.google.android.gms.internal.mlkit_language_id.zzea.zzb(r3, (java.lang.String) r4);
     */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzgp
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final int zzd(Object obj) {
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
                int zzc = zzc(i8);
                int[] iArr = this.zzc;
                int i12 = iArr[i8];
                int i13 = (zzc & 267386880) >>> 20;
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
                long j3 = zzc & i6;
                switch (i13) {
                    case 0:
                        j = 0;
                        if ((i11 & i) != 0) {
                            i9 += zzea.zzb(i12, 0.0d);
                            break;
                        } else {
                            break;
                        }
                    case 1:
                        j = 0;
                        if ((i11 & i) != 0) {
                            i9 += zzea.zzb(i12, 0.0f);
                        }
                        break;
                    case 2:
                        j = 0;
                        if ((i & i11) != 0) {
                            zzd = zzea.zzd(i12, unsafe.getLong(obj, j3));
                            i9 += zzd;
                        }
                        break;
                    case 3:
                        j = 0;
                        if ((i & i11) != 0) {
                            zzd = zzea.zze(i12, unsafe.getLong(obj, j3));
                            i9 += zzd;
                        }
                        break;
                    case 4:
                        j = 0;
                        if ((i & i11) != 0) {
                            zzd = zzea.zzf(i12, unsafe.getInt(obj, j3));
                            i9 += zzd;
                        }
                        break;
                    case 5:
                        j = 0;
                        if ((i11 & i) != 0) {
                            zzd = zzea.zzg(i12, 0L);
                            i9 += zzd;
                        }
                        break;
                    case 6:
                        if ((i11 & i) != 0) {
                            i9 += zzea.zzi(i12, 0);
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
                            zzi = zzea.zzg(i12, i2);
                            i9 += zzi;
                        }
                        j = 0;
                        break;
                    case 12:
                        if ((i11 & i) != 0) {
                            i3 = unsafe.getInt(obj, j3);
                            zzi = zzea.zzk(i12, i3);
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
                            zzi = zzea.zzh(i12, i4);
                            i9 += zzi;
                        }
                        j = 0;
                        break;
                    case 16:
                        if ((i11 & i) != 0) {
                            j2 = unsafe.getLong(obj, j3);
                            zzi = zzea.zzf(i12, j2);
                            i9 += zzi;
                        }
                        j = 0;
                        break;
                    case 17:
                        break;
                    case 18:
                        zzi = zzgr.zzi(i12, (List) unsafe.getObject(obj, j3), false);
                        i9 += zzi;
                        j = 0;
                        break;
                    case 19:
                    case 24:
                    case 31:
                        zza2 = zzgr.zzh(i12, (List) unsafe.getObject(obj, j3), false);
                        i9 += zza2;
                        j = 0;
                        break;
                    case 20:
                        zza2 = zzgr.zza(i12, (List) unsafe.getObject(obj, j3), false);
                        i9 += zza2;
                        j = 0;
                        break;
                    case 21:
                        zza2 = zzgr.zzb(i12, (List) unsafe.getObject(obj, j3), false);
                        i9 += zza2;
                        j = 0;
                        break;
                    case 22:
                        zza2 = zzgr.zze(i12, (List) unsafe.getObject(obj, j3), false);
                        i9 += zza2;
                        j = 0;
                        break;
                    case 23:
                    case 32:
                        zza2 = zzgr.zzi(i12, (List) unsafe.getObject(obj, j3), false);
                        i9 += zza2;
                        j = 0;
                        break;
                    case 25:
                        zza2 = zzgr.zzj(i12, (List) unsafe.getObject(obj, j3), false);
                        i9 += zza2;
                        j = 0;
                        break;
                    case 26:
                        zzi = zzgr.zza(i12, (List) unsafe.getObject(obj, j3));
                        i9 += zzi;
                        j = 0;
                        break;
                    case 27:
                        zzi = zzgr.zza(i12, (List) unsafe.getObject(obj, j3), zza(i8));
                        i9 += zzi;
                        j = 0;
                        break;
                    case 28:
                        zzi = zzgr.zzb(i12, (List) unsafe.getObject(obj, j3));
                        i9 += zzi;
                        j = 0;
                        break;
                    case 29:
                        zzi = zzgr.zzf(i12, (List) unsafe.getObject(obj, j3), false);
                        i9 += zzi;
                        j = 0;
                        break;
                    case 30:
                        zza2 = zzgr.zzd(i12, (List) unsafe.getObject(obj, j3), false);
                        i9 += zza2;
                        j = 0;
                        break;
                    case 33:
                        zza2 = zzgr.zzg(i12, (List) unsafe.getObject(obj, j3), false);
                        i9 += zza2;
                        j = 0;
                        break;
                    case 34:
                        zza2 = zzgr.zzc(i12, (List) unsafe.getObject(obj, j3), false);
                        i9 += zza2;
                        j = 0;
                        break;
                    case 35:
                        zzi2 = zzgr.zzi((List) unsafe.getObject(obj, j3));
                        break;
                    case 36:
                        zzi2 = zzgr.zzh((List) unsafe.getObject(obj, j3));
                        break;
                    case 37:
                        zzi2 = zzgr.zza((List) unsafe.getObject(obj, j3));
                        break;
                    case 38:
                        zzi2 = zzgr.zzb((List) unsafe.getObject(obj, j3));
                        break;
                    case 39:
                        zzi2 = zzgr.zze((List) unsafe.getObject(obj, j3));
                        break;
                    case 40:
                        zzi2 = zzgr.zzi((List) unsafe.getObject(obj, j3));
                        break;
                    case 41:
                        zzi2 = zzgr.zzh((List) unsafe.getObject(obj, j3));
                        break;
                    case 42:
                        zzi2 = zzgr.zzj((List) unsafe.getObject(obj, j3));
                        break;
                    case 43:
                        zzi2 = zzgr.zzf((List) unsafe.getObject(obj, j3));
                        break;
                    case 44:
                        zzi2 = zzgr.zzd((List) unsafe.getObject(obj, j3));
                        break;
                    case 45:
                        zzi2 = zzgr.zzh((List) unsafe.getObject(obj, j3));
                        break;
                    case 46:
                        zzi2 = zzgr.zzi((List) unsafe.getObject(obj, j3));
                        break;
                    case 47:
                        zzi2 = zzgr.zzg((List) unsafe.getObject(obj, j3));
                        break;
                    case 48:
                        zzi2 = zzgr.zzc((List) unsafe.getObject(obj, j3));
                        break;
                    case 49:
                        zzi = zzgr.zzb(i12, (List) unsafe.getObject(obj, j3), zza(i8));
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
                            zzi = zzea.zzb(i12, 0.0d);
                            i9 += zzi;
                        }
                        j = 0;
                        break;
                    case 52:
                        if (zza(obj, i12, i8)) {
                            zze = zzea.zzb(i12, 0.0f);
                            i9 += zze;
                        }
                        j = 0;
                        break;
                    case 53:
                        if (zza(obj, i12, i8)) {
                            zzi = zzea.zzd(i12, zze(obj, j3));
                            i9 += zzi;
                        }
                        j = 0;
                        break;
                    case 54:
                        if (zza(obj, i12, i8)) {
                            zzi = zzea.zze(i12, zze(obj, j3));
                            i9 += zzi;
                        }
                        j = 0;
                        break;
                    case 55:
                        if (zza(obj, i12, i8)) {
                            zzi = zzea.zzf(i12, zzd(obj, j3));
                            i9 += zzi;
                        }
                        j = 0;
                        break;
                    case 56:
                        if (zza(obj, i12, i8)) {
                            zzi = zzea.zzg(i12, 0L);
                            i9 += zzi;
                        }
                        j = 0;
                        break;
                    case 57:
                        if (zza(obj, i12, i8)) {
                            zze = zzea.zzi(i12, 0);
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
                            zzi = zzea.zzg(i12, i2);
                            i9 += zzi;
                        }
                        j = 0;
                        break;
                    case 63:
                        if (zza(obj, i12, i8)) {
                            i3 = zzd(obj, j3);
                            zzi = zzea.zzk(i12, i3);
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
                            zzi = zzea.zzh(i12, i4);
                            i9 += zzi;
                        }
                        j = 0;
                        break;
                    case 67:
                        if (zza(obj, i12, i8)) {
                            j2 = zze(obj, j3);
                            zzi = zzea.zzf(i12, j2);
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
            zzej zza9 = this.zzr.zza(obj);
            for (int i17 = 0; i17 < zza9.zza.zzc(); i17++) {
                Map.Entry zzb5 = zza9.zza.zzb(i17);
                ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(zzb5.getKey());
                i16 += zzej.zza((zzel) null, zzb5.getValue());
            }
            for (Map.Entry entry : zza9.zza.zzd()) {
                ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(entry.getKey());
                i16 += zzej.zza((zzel) null, entry.getValue());
            }
            return zza8 + i16;
        }
        Unsafe unsafe2 = zzb;
        int i18 = 0;
        int i19 = 0;
        while (i18 < this.zzc.length) {
            int zzc2 = zzc(i18);
            int i20 = (zzc2 & i5) >>> 20;
            int i21 = this.zzc[i18];
            long j4 = zzc2 & 1048575;
            if (i20 >= zzek.zza.zza() && i20 <= zzek.zzb.zza()) {
                int i22 = this.zzc[i18 + 2];
            }
            switch (i20) {
                case 0:
                    if (!zza(obj, i18)) {
                        break;
                    }
                    zza7 = zzea.zzb(i21, 0.0d);
                    i19 += zza7;
                    break;
                case 1:
                    if (!zza(obj, i18)) {
                        break;
                    }
                    zza7 = zzea.zzb(i21, 0.0f);
                    i19 += zza7;
                    break;
                case 2:
                    if (zza(obj, i18)) {
                        zzb2 = zzhn.zzb(obj, j4);
                        zza7 = zzea.zzd(i21, zzb2);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 3:
                    if (zza(obj, i18)) {
                        zzb3 = zzhn.zzb(obj, j4);
                        zza7 = zzea.zze(i21, zzb3);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 4:
                    if (zza(obj, i18)) {
                        zza3 = zzhn.zza(obj, j4);
                        zza7 = zzea.zzf(i21, zza3);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 5:
                    if (!zza(obj, i18)) {
                        break;
                    }
                    zza7 = zzea.zzg(i21, 0L);
                    i19 += zza7;
                    break;
                case 6:
                    if (!zza(obj, i18)) {
                        break;
                    }
                    zza7 = zzea.zzi(i21, 0);
                    i19 += zza7;
                    break;
                case 7:
                    if (!zza(obj, i18)) {
                        break;
                    }
                    zza7 = zzea.zzb(i21, true);
                    i19 += zza7;
                    break;
                case 8:
                    if (zza(obj, i18)) {
                        zzf = zzhn.zzf(obj, j4);
                        break;
                    } else {
                        break;
                    }
                case 9:
                    if (!zza(obj, i18)) {
                        break;
                    }
                    zza7 = zzgr.zza(i21, zzhn.zzf(obj, j4), zza(i18));
                    i19 += zza7;
                    break;
                case 10:
                    if (!zza(obj, i18)) {
                        break;
                    }
                    zzf = zzhn.zzf(obj, j4);
                    zza7 = zzea.zzc(i21, (zzdn) zzf);
                    i19 += zza7;
                    break;
                case 11:
                    if (zza(obj, i18)) {
                        zza4 = zzhn.zza(obj, j4);
                        zza7 = zzea.zzg(i21, zza4);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 12:
                    if (zza(obj, i18)) {
                        zza5 = zzhn.zza(obj, j4);
                        zza7 = zzea.zzk(i21, zza5);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 13:
                    if (!zza(obj, i18)) {
                        break;
                    }
                    zza7 = zzea.zzj(i21, 0);
                    i19 += zza7;
                    break;
                case 14:
                    if (!zza(obj, i18)) {
                        break;
                    }
                    zza7 = zzea.zzh(i21, 0L);
                    i19 += zza7;
                    break;
                case 15:
                    if (zza(obj, i18)) {
                        zza6 = zzhn.zza(obj, j4);
                        zza7 = zzea.zzh(i21, zza6);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 16:
                    if (zza(obj, i18)) {
                        zzb4 = zzhn.zzb(obj, j4);
                        zza7 = zzea.zzf(i21, zzb4);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 17:
                    if (!zza(obj, i18)) {
                        break;
                    }
                    zza7 = zzea.zzc(i21, (zzfz) zzhn.zzf(obj, j4), zza(i18));
                    i19 += zza7;
                    break;
                case 18:
                case 23:
                case 32:
                    zza7 = zzgr.zzi(i21, zza(obj, j4), false);
                    i19 += zza7;
                    break;
                case 19:
                case 24:
                case 31:
                    zza7 = zzgr.zzh(i21, zza(obj, j4), false);
                    i19 += zza7;
                    break;
                case 20:
                    zza7 = zzgr.zza(i21, zza(obj, j4), false);
                    i19 += zza7;
                    break;
                case 21:
                    zza7 = zzgr.zzb(i21, zza(obj, j4), false);
                    i19 += zza7;
                    break;
                case 22:
                    zza7 = zzgr.zze(i21, zza(obj, j4), false);
                    i19 += zza7;
                    break;
                case 25:
                    zza7 = zzgr.zzj(i21, zza(obj, j4), false);
                    i19 += zza7;
                    break;
                case 26:
                    zza7 = zzgr.zza(i21, zza(obj, j4));
                    i19 += zza7;
                    break;
                case 27:
                    zza7 = zzgr.zza(i21, zza(obj, j4), zza(i18));
                    i19 += zza7;
                    break;
                case 28:
                    zza7 = zzgr.zzb(i21, zza(obj, j4));
                    i19 += zza7;
                    break;
                case 29:
                    zza7 = zzgr.zzf(i21, zza(obj, j4), false);
                    i19 += zza7;
                    break;
                case 30:
                    zza7 = zzgr.zzd(i21, zza(obj, j4), false);
                    i19 += zza7;
                    break;
                case 33:
                    zza7 = zzgr.zzg(i21, zza(obj, j4), false);
                    i19 += zza7;
                    break;
                case 34:
                    zza7 = zzgr.zzc(i21, zza(obj, j4), false);
                    i19 += zza7;
                    break;
                case 35:
                    zzi3 = zzgr.zzi((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzea.zze(i21) + zzea.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 36:
                    zzi3 = zzgr.zzh((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzea.zze(i21) + zzea.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 37:
                    zzi3 = zzgr.zza((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzea.zze(i21) + zzea.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 38:
                    zzi3 = zzgr.zzb((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzea.zze(i21) + zzea.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 39:
                    zzi3 = zzgr.zze((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzea.zze(i21) + zzea.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 40:
                    zzi3 = zzgr.zzi((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzea.zze(i21) + zzea.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 41:
                    zzi3 = zzgr.zzh((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzea.zze(i21) + zzea.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 42:
                    zzi3 = zzgr.zzj((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzea.zze(i21) + zzea.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 43:
                    zzi3 = zzgr.zzf((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzea.zze(i21) + zzea.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 44:
                    zzi3 = zzgr.zzd((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzea.zze(i21) + zzea.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 45:
                    zzi3 = zzgr.zzh((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzea.zze(i21) + zzea.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 46:
                    zzi3 = zzgr.zzi((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzea.zze(i21) + zzea.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 47:
                    zzi3 = zzgr.zzg((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzea.zze(i21) + zzea.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 48:
                    zzi3 = zzgr.zzc((List) unsafe2.getObject(obj, j4));
                    if (zzi3 <= 0) {
                        break;
                    }
                    zza7 = zzea.zze(i21) + zzea.zzg(zzi3) + zzi3;
                    i19 += zza7;
                    break;
                case 49:
                    zza7 = zzgr.zzb(i21, zza(obj, j4), zza(i18));
                    i19 += zza7;
                    break;
                case 50:
                    zza7 = this.zzs.zza(i21, zzhn.zzf(obj, j4), zzb(i18));
                    i19 += zza7;
                    break;
                case 51:
                    if (!zza(obj, i21, i18)) {
                        break;
                    }
                    zza7 = zzea.zzb(i21, 0.0d);
                    i19 += zza7;
                    break;
                case 52:
                    if (!zza(obj, i21, i18)) {
                        break;
                    }
                    zza7 = zzea.zzb(i21, 0.0f);
                    i19 += zza7;
                    break;
                case 53:
                    if (zza(obj, i21, i18)) {
                        zzb2 = zze(obj, j4);
                        zza7 = zzea.zzd(i21, zzb2);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 54:
                    if (zza(obj, i21, i18)) {
                        zzb3 = zze(obj, j4);
                        zza7 = zzea.zze(i21, zzb3);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 55:
                    if (zza(obj, i21, i18)) {
                        zza3 = zzd(obj, j4);
                        zza7 = zzea.zzf(i21, zza3);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 56:
                    if (!zza(obj, i21, i18)) {
                        break;
                    }
                    zza7 = zzea.zzg(i21, 0L);
                    i19 += zza7;
                    break;
                case 57:
                    if (!zza(obj, i21, i18)) {
                        break;
                    }
                    zza7 = zzea.zzi(i21, 0);
                    i19 += zza7;
                    break;
                case 58:
                    if (!zza(obj, i21, i18)) {
                        break;
                    }
                    zza7 = zzea.zzb(i21, true);
                    i19 += zza7;
                    break;
                case 59:
                    if (zza(obj, i21, i18)) {
                        zzf = zzhn.zzf(obj, j4);
                        break;
                    } else {
                        break;
                    }
                case 60:
                    if (!zza(obj, i21, i18)) {
                        break;
                    }
                    zza7 = zzgr.zza(i21, zzhn.zzf(obj, j4), zza(i18));
                    i19 += zza7;
                    break;
                case 61:
                    if (!zza(obj, i21, i18)) {
                        break;
                    }
                    zzf = zzhn.zzf(obj, j4);
                    zza7 = zzea.zzc(i21, (zzdn) zzf);
                    i19 += zza7;
                    break;
                case 62:
                    if (zza(obj, i21, i18)) {
                        zza4 = zzd(obj, j4);
                        zza7 = zzea.zzg(i21, zza4);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 63:
                    if (zza(obj, i21, i18)) {
                        zza5 = zzd(obj, j4);
                        zza7 = zzea.zzk(i21, zza5);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 64:
                    if (!zza(obj, i21, i18)) {
                        break;
                    }
                    zza7 = zzea.zzj(i21, 0);
                    i19 += zza7;
                    break;
                case 65:
                    if (!zza(obj, i21, i18)) {
                        break;
                    }
                    zza7 = zzea.zzh(i21, 0L);
                    i19 += zza7;
                    break;
                case 66:
                    if (zza(obj, i21, i18)) {
                        zza6 = zzd(obj, j4);
                        zza7 = zzea.zzh(i21, zza6);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 67:
                    if (zza(obj, i21, i18)) {
                        zzb4 = zze(obj, j4);
                        zza7 = zzea.zzf(i21, zzb4);
                        i19 += zza7;
                        break;
                    } else {
                        break;
                    }
                case 68:
                    if (!zza(obj, i21, i18)) {
                        break;
                    }
                    zza7 = zzea.zzc(i21, (zzfz) zzhn.zzf(obj, j4), zza(i18));
                    i19 += zza7;
                    break;
            }
            i18 += 3;
            i5 = 267386880;
        }
        return i19 + zza(this.zzq, obj);
    }
}
