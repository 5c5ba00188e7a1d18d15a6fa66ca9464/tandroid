package com.google.android.gms.internal.mlkit_language_id;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.NotificationCenter;
import sun.misc.Unsafe;
/* compiled from: com.google.mlkit:language-id@@16.1.1 */
/* loaded from: classes.dex */
final class zzgd<T> implements zzgp<T> {
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
    private final zzhh<?, ?> zzq;
    private final zzee<?> zzr;
    private final zzfs zzs;

    private zzgd(int[] iArr, Object[] objArr, int i, int i2, zzfz zzfzVar, boolean z, boolean z2, int[] iArr2, int i3, int i4, zzge zzgeVar, zzfj zzfjVar, zzhh<?, ?> zzhhVar, zzee<?> zzeeVar, zzfs zzfsVar) {
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

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:130:0x0297  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x029b  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x02b3  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x02b7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static <T> zzgd<T> zza(Class<T> cls, zzfx zzfxVar, zzge zzgeVar, zzfj zzfjVar, zzhh<?, ?> zzhhVar, zzee<?> zzeeVar, zzfs zzfsVar) {
        int i;
        int charAt;
        int charAt2;
        int charAt3;
        int charAt4;
        int i2;
        int i3;
        int i4;
        int i5;
        int[] iArr;
        int i6;
        char charAt5;
        int i7;
        char charAt6;
        int i8;
        char charAt7;
        int i9;
        char charAt8;
        int i10;
        char charAt9;
        int i11;
        char charAt10;
        int i12;
        char charAt11;
        int i13;
        char charAt12;
        int i14;
        int i15;
        int i16;
        int i17;
        int i18;
        int i19;
        int i20;
        int objectFieldOffset;
        boolean z;
        int i21;
        int i22;
        int i23;
        int i24;
        int i25;
        Field zza2;
        char charAt13;
        int i26;
        int i27;
        Object obj;
        Field zza3;
        Object obj2;
        Field zza4;
        int i28;
        char charAt14;
        int i29;
        char charAt15;
        int i30;
        char charAt16;
        int i31;
        char charAt17;
        if (zzfxVar instanceof zzgm) {
            zzgm zzgmVar = (zzgm) zzfxVar;
            int i32 = 0;
            boolean z2 = zzgmVar.zza() == zzgl.zzb;
            String zzd = zzgmVar.zzd();
            int length = zzd.length();
            if (zzd.charAt(0) >= 55296) {
                int i33 = 1;
                while (true) {
                    i = i33 + 1;
                    if (zzd.charAt(i33) < 55296) {
                        break;
                    }
                    i33 = i;
                }
            } else {
                i = 1;
            }
            int i34 = i + 1;
            int charAt18 = zzd.charAt(i);
            if (charAt18 >= 55296) {
                int i35 = charAt18 & 8191;
                int i36 = 13;
                while (true) {
                    i31 = i34 + 1;
                    charAt17 = zzd.charAt(i34);
                    if (charAt17 < 55296) {
                        break;
                    }
                    i35 |= (charAt17 & 8191) << i36;
                    i36 += 13;
                    i34 = i31;
                }
                charAt18 = i35 | (charAt17 << i36);
                i34 = i31;
            }
            if (charAt18 == 0) {
                iArr = zza;
                i5 = 0;
                i4 = 0;
                charAt = 0;
                charAt2 = 0;
                charAt3 = 0;
                charAt4 = 0;
            } else {
                int i37 = i34 + 1;
                int charAt19 = zzd.charAt(i34);
                if (charAt19 >= 55296) {
                    int i38 = charAt19 & 8191;
                    int i39 = 13;
                    while (true) {
                        i13 = i37 + 1;
                        charAt12 = zzd.charAt(i37);
                        if (charAt12 < 55296) {
                            break;
                        }
                        i38 |= (charAt12 & 8191) << i39;
                        i39 += 13;
                        i37 = i13;
                    }
                    charAt19 = i38 | (charAt12 << i39);
                    i37 = i13;
                }
                int i40 = i37 + 1;
                int charAt20 = zzd.charAt(i37);
                if (charAt20 >= 55296) {
                    int i41 = charAt20 & 8191;
                    int i42 = 13;
                    while (true) {
                        i12 = i40 + 1;
                        charAt11 = zzd.charAt(i40);
                        if (charAt11 < 55296) {
                            break;
                        }
                        i41 |= (charAt11 & 8191) << i42;
                        i42 += 13;
                        i40 = i12;
                    }
                    charAt20 = i41 | (charAt11 << i42);
                    i40 = i12;
                }
                int i43 = i40 + 1;
                charAt = zzd.charAt(i40);
                if (charAt >= 55296) {
                    int i44 = charAt & 8191;
                    int i45 = 13;
                    while (true) {
                        i11 = i43 + 1;
                        charAt10 = zzd.charAt(i43);
                        if (charAt10 < 55296) {
                            break;
                        }
                        i44 |= (charAt10 & 8191) << i45;
                        i45 += 13;
                        i43 = i11;
                    }
                    charAt = i44 | (charAt10 << i45);
                    i43 = i11;
                }
                int i46 = i43 + 1;
                charAt2 = zzd.charAt(i43);
                if (charAt2 >= 55296) {
                    int i47 = charAt2 & 8191;
                    int i48 = 13;
                    while (true) {
                        i10 = i46 + 1;
                        charAt9 = zzd.charAt(i46);
                        if (charAt9 < 55296) {
                            break;
                        }
                        i47 |= (charAt9 & 8191) << i48;
                        i48 += 13;
                        i46 = i10;
                    }
                    charAt2 = i47 | (charAt9 << i48);
                    i46 = i10;
                }
                int i49 = i46 + 1;
                charAt3 = zzd.charAt(i46);
                if (charAt3 >= 55296) {
                    int i50 = charAt3 & 8191;
                    int i51 = 13;
                    while (true) {
                        i9 = i49 + 1;
                        charAt8 = zzd.charAt(i49);
                        if (charAt8 < 55296) {
                            break;
                        }
                        i50 |= (charAt8 & 8191) << i51;
                        i51 += 13;
                        i49 = i9;
                    }
                    charAt3 = i50 | (charAt8 << i51);
                    i49 = i9;
                }
                int i52 = i49 + 1;
                charAt4 = zzd.charAt(i49);
                if (charAt4 >= 55296) {
                    int i53 = charAt4 & 8191;
                    int i54 = i52;
                    int i55 = 13;
                    while (true) {
                        i8 = i54 + 1;
                        charAt7 = zzd.charAt(i54);
                        if (charAt7 < 55296) {
                            break;
                        }
                        i53 |= (charAt7 & 8191) << i55;
                        i55 += 13;
                        i54 = i8;
                    }
                    charAt4 = i53 | (charAt7 << i55);
                    i2 = i8;
                } else {
                    i2 = i52;
                }
                int i56 = i2 + 1;
                int charAt21 = zzd.charAt(i2);
                if (charAt21 >= 55296) {
                    int i57 = charAt21 & 8191;
                    int i58 = i56;
                    int i59 = 13;
                    while (true) {
                        i7 = i58 + 1;
                        charAt6 = zzd.charAt(i58);
                        if (charAt6 < 55296) {
                            break;
                        }
                        i57 |= (charAt6 & 8191) << i59;
                        i59 += 13;
                        i58 = i7;
                    }
                    charAt21 = i57 | (charAt6 << i59);
                    i3 = i7;
                } else {
                    i3 = i56;
                }
                int i60 = i3 + 1;
                int charAt22 = zzd.charAt(i3);
                if (charAt22 >= 55296) {
                    int i61 = charAt22 & 8191;
                    int i62 = i60;
                    int i63 = 13;
                    while (true) {
                        i6 = i62 + 1;
                        charAt5 = zzd.charAt(i62);
                        if (charAt5 < 55296) {
                            break;
                        }
                        i61 |= (charAt5 & 8191) << i63;
                        i63 += 13;
                        i62 = i6;
                    }
                    charAt22 = i61 | (charAt5 << i63);
                    i60 = i6;
                }
                int[] iArr2 = new int[charAt22 + charAt4 + charAt21];
                i32 = (charAt19 << 1) + charAt20;
                i4 = charAt22;
                i5 = charAt19;
                i34 = i60;
                iArr = iArr2;
            }
            Unsafe unsafe = zzb;
            Object[] zze = zzgmVar.zze();
            Class<?> cls2 = zzgmVar.zzc().getClass();
            int[] iArr3 = new int[charAt3 * 3];
            Object[] objArr = new Object[charAt3 << 1];
            int i64 = i4 + charAt4;
            int i65 = i4;
            int i66 = i64;
            int i67 = 0;
            int i68 = 0;
            while (i34 < length) {
                int i69 = i34 + 1;
                int charAt23 = zzd.charAt(i34);
                if (charAt23 >= 55296) {
                    int i70 = charAt23 & 8191;
                    int i71 = i69;
                    int i72 = 13;
                    while (true) {
                        i30 = i71 + 1;
                        charAt16 = zzd.charAt(i71);
                        i14 = length;
                        if (charAt16 < 55296) {
                            break;
                        }
                        i70 |= (charAt16 & 8191) << i72;
                        i72 += 13;
                        i71 = i30;
                        length = i14;
                    }
                    charAt23 = i70 | (charAt16 << i72);
                    i15 = i30;
                } else {
                    i14 = length;
                    i15 = i69;
                }
                int i73 = i15 + 1;
                int charAt24 = zzd.charAt(i15);
                if (charAt24 >= 55296) {
                    int i74 = charAt24 & 8191;
                    int i75 = i73;
                    int i76 = 13;
                    while (true) {
                        i29 = i75 + 1;
                        charAt15 = zzd.charAt(i75);
                        i16 = i4;
                        if (charAt15 < 55296) {
                            break;
                        }
                        i74 |= (charAt15 & 8191) << i76;
                        i76 += 13;
                        i75 = i29;
                        i4 = i16;
                    }
                    charAt24 = i74 | (charAt15 << i76);
                    i17 = i29;
                } else {
                    i16 = i4;
                    i17 = i73;
                }
                int i77 = charAt24 & NotificationCenter.voipServiceCreated;
                int i78 = charAt2;
                if ((charAt24 & 1024) != 0) {
                    iArr[i67] = i68;
                    i67++;
                }
                if (i77 >= 51) {
                    int i79 = i17 + 1;
                    int charAt25 = zzd.charAt(i17);
                    if (charAt25 >= 55296) {
                        int i80 = charAt25 & 8191;
                        int i81 = i79;
                        int i82 = 13;
                        while (true) {
                            i28 = i81 + 1;
                            charAt14 = zzd.charAt(i81);
                            i18 = i67;
                            if (charAt14 < 55296) {
                                break;
                            }
                            i80 |= (charAt14 & 8191) << i82;
                            i82 += 13;
                            i81 = i28;
                            i67 = i18;
                        }
                        charAt25 = i80 | (charAt14 << i82);
                        i79 = i28;
                    } else {
                        i18 = i67;
                    }
                    int i83 = i77 - 51;
                    if (i83 == 9 || i83 == 17) {
                        i26 = 1;
                        i27 = i32 + 1;
                        objArr[((i68 / 3) << 1) + 1] = zze[i32];
                    } else if (i83 == 12 && !z2) {
                        i26 = 1;
                        i27 = i32 + 1;
                        objArr[((i68 / 3) << 1) + 1] = zze[i32];
                    } else {
                        i26 = 1;
                        int i84 = charAt25 << i26;
                        obj = zze[i84];
                        if (!(obj instanceof Field)) {
                            zza3 = (Field) obj;
                        } else {
                            zza3 = zza(cls2, (String) obj);
                            zze[i84] = zza3;
                        }
                        int i85 = charAt;
                        int objectFieldOffset2 = (int) unsafe.objectFieldOffset(zza3);
                        int i86 = i84 + 1;
                        obj2 = zze[i86];
                        int i87 = i32;
                        if (!(obj2 instanceof Field)) {
                            zza4 = (Field) obj2;
                        } else {
                            zza4 = zza(cls2, (String) obj2);
                            zze[i86] = zza4;
                        }
                        i25 = (int) unsafe.objectFieldOffset(zza4);
                        objectFieldOffset = objectFieldOffset2;
                        z = z2;
                        i24 = i87;
                        i22 = i79;
                        i23 = 0;
                        i19 = i85;
                    }
                    i32 = i27;
                    int i842 = charAt25 << i26;
                    obj = zze[i842];
                    if (!(obj instanceof Field)) {
                    }
                    int i852 = charAt;
                    int objectFieldOffset22 = (int) unsafe.objectFieldOffset(zza3);
                    int i862 = i842 + 1;
                    obj2 = zze[i862];
                    int i872 = i32;
                    if (!(obj2 instanceof Field)) {
                    }
                    i25 = (int) unsafe.objectFieldOffset(zza4);
                    objectFieldOffset = objectFieldOffset22;
                    z = z2;
                    i24 = i872;
                    i22 = i79;
                    i23 = 0;
                    i19 = i852;
                } else {
                    i18 = i67;
                    int i88 = charAt;
                    int i89 = i32 + 1;
                    Field zza5 = zza(cls2, (String) zze[i32]);
                    i19 = i88;
                    if (i77 == 9 || i77 == 17) {
                        objArr[((i68 / 3) << 1) + 1] = zza5.getType();
                    } else {
                        if (i77 == 27 || i77 == 49) {
                            i20 = i32 + 2;
                            objArr[((i68 / 3) << 1) + 1] = zze[i89];
                        } else if (i77 == 12 || i77 == 30 || i77 == 44) {
                            if (!z2) {
                                i20 = i32 + 2;
                                objArr[((i68 / 3) << 1) + 1] = zze[i89];
                            }
                        } else if (i77 == 50) {
                            int i90 = i65 + 1;
                            iArr[i65] = i68;
                            int i91 = (i68 / 3) << 1;
                            int i92 = i32 + 2;
                            objArr[i91] = zze[i89];
                            if ((charAt24 & 2048) != 0) {
                                objArr[i91 + 1] = zze[i92];
                                i20 = i32 + 3;
                                i65 = i90;
                            } else {
                                i65 = i90;
                                i20 = i92;
                            }
                        }
                        objectFieldOffset = (int) unsafe.objectFieldOffset(zza5);
                        if ((charAt24 & LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM) == 4096 || i77 > 17) {
                            z = z2;
                            i21 = 1048575;
                            i22 = i17;
                            i23 = 0;
                        } else {
                            int i93 = i17 + 1;
                            int charAt26 = zzd.charAt(i17);
                            if (charAt26 >= 55296) {
                                int i94 = charAt26 & 8191;
                                int i95 = i93;
                                int i96 = 13;
                                while (true) {
                                    i22 = i95 + 1;
                                    charAt13 = zzd.charAt(i95);
                                    if (charAt13 < 55296) {
                                        break;
                                    }
                                    i94 |= (charAt13 & 8191) << i96;
                                    i96 += 13;
                                    i95 = i22;
                                }
                                charAt26 = i94 | (charAt13 << i96);
                            } else {
                                i22 = i93;
                            }
                            int i97 = (i5 << 1) + (charAt26 / 32);
                            Object obj3 = zze[i97];
                            if (obj3 instanceof Field) {
                                zza2 = (Field) obj3;
                            } else {
                                zza2 = zza(cls2, (String) obj3);
                                zze[i97] = zza2;
                            }
                            z = z2;
                            i21 = (int) unsafe.objectFieldOffset(zza2);
                            i23 = charAt26 % 32;
                        }
                        if (i77 < 18 && i77 <= 49) {
                            iArr[i66] = objectFieldOffset;
                            i66++;
                        }
                        int i98 = i21;
                        i24 = i20;
                        i25 = i98;
                    }
                    i20 = i89;
                    objectFieldOffset = (int) unsafe.objectFieldOffset(zza5);
                    if ((charAt24 & LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM) == 4096) {
                    }
                    z = z2;
                    i21 = 1048575;
                    i22 = i17;
                    i23 = 0;
                    if (i77 < 18) {
                    }
                    int i982 = i21;
                    i24 = i20;
                    i25 = i982;
                }
                int i99 = i68 + 1;
                iArr3[i68] = charAt23;
                int i100 = i68 + 2;
                iArr3[i99] = ((charAt24 & 256) != 0 ? 268435456 : 0) | ((charAt24 & LiteMode.FLAG_CALLS_ANIMATIONS) != 0 ? 536870912 : 0) | (i77 << 20) | objectFieldOffset;
                i68 += 3;
                iArr3[i100] = (i23 << 20) | i25;
                i32 = i24;
                z2 = z;
                charAt2 = i78;
                length = i14;
                i4 = i16;
                i34 = i22;
                charAt = i19;
                i67 = i18;
            }
            return new zzgd<>(iArr3, objArr, charAt, charAt2, zzgmVar.zzc(), z2, false, iArr, i4, i64, zzgeVar, zzfjVar, zzhhVar, zzeeVar, zzfsVar);
        }
        ((zzha) zzfxVar).zza();
        int i101 = zzgl.zza;
        throw new NoSuchMethodError();
    }

    private static Field zza(Class<?> cls, String str) {
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

    /* JADX WARN: Code restructure failed: missing block: B:103:0x01c0, code lost:
        if (java.lang.Double.doubleToLongBits(com.google.android.gms.internal.mlkit_language_id.zzhn.zze(r10, r6)) == java.lang.Double.doubleToLongBits(com.google.android.gms.internal.mlkit_language_id.zzhn.zze(r11, r6))) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0038, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzgr.zza(com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r10, r6), com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r11, r6)) != false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x006b, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzgr.zza(com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r10, r6), com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r11, r6)) != false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x007e, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x008f, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00a2, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00b3, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00c4, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00d6, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00ec, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzgr.zza(com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r10, r6), com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r11, r6)) != false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0102, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzgr.zza(com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r10, r6), com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r11, r6)) != false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0118, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzgr.zza(com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r10, r6), com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r11, r6)) != false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x012a, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zzc(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zzc(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x013c, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x0150, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x0162, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x0176, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x018a, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x01a4, code lost:
        if (java.lang.Float.floatToIntBits(com.google.android.gms.internal.mlkit_language_id.zzhn.zzd(r10, r6)) == java.lang.Float.floatToIntBits(com.google.android.gms.internal.mlkit_language_id.zzhn.zzd(r11, r6))) goto L85;
     */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzgp
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
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
                        z = zzgr.zza(zzhn.zzf(t, j), zzhn.zzf(t2, j));
                        break;
                    case 50:
                        z = zzgr.zza(zzhn.zzf(t, j), zzhn.zzf(t2, j));
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
                        if (zzhn.zza(t, zzd) == zzhn.zza(t2, zzd)) {
                            break;
                        }
                        z = false;
                        break;
                }
                if (!z) {
                    return false;
                }
                i += 3;
            } else if (this.zzq.zza(t).equals(this.zzq.zza(t2))) {
                if (this.zzh) {
                    return this.zzr.zza(t).equals(this.zzr.zza(t2));
                }
                return true;
            } else {
                return false;
            }
        }
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzgp
    public final int zza(T t) {
        int i;
        int zza2;
        int length = this.zzc.length;
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3 += 3) {
            int zzc = zzc(i3);
            int i4 = this.zzc[i3];
            long j = 1048575 & zzc;
            int i5 = 37;
            switch ((zzc & 267386880) >>> 20) {
                case 0:
                    i = i2 * 53;
                    zza2 = zzeq.zza(Double.doubleToLongBits(zzhn.zze(t, j)));
                    i2 = i + zza2;
                    break;
                case 1:
                    i = i2 * 53;
                    zza2 = Float.floatToIntBits(zzhn.zzd(t, j));
                    i2 = i + zza2;
                    break;
                case 2:
                    i = i2 * 53;
                    zza2 = zzeq.zza(zzhn.zzb(t, j));
                    i2 = i + zza2;
                    break;
                case 3:
                    i = i2 * 53;
                    zza2 = zzeq.zza(zzhn.zzb(t, j));
                    i2 = i + zza2;
                    break;
                case 4:
                    i = i2 * 53;
                    zza2 = zzhn.zza(t, j);
                    i2 = i + zza2;
                    break;
                case 5:
                    i = i2 * 53;
                    zza2 = zzeq.zza(zzhn.zzb(t, j));
                    i2 = i + zza2;
                    break;
                case 6:
                    i = i2 * 53;
                    zza2 = zzhn.zza(t, j);
                    i2 = i + zza2;
                    break;
                case 7:
                    i = i2 * 53;
                    zza2 = zzeq.zza(zzhn.zzc(t, j));
                    i2 = i + zza2;
                    break;
                case 8:
                    i = i2 * 53;
                    zza2 = ((String) zzhn.zzf(t, j)).hashCode();
                    i2 = i + zza2;
                    break;
                case 9:
                    Object zzf = zzhn.zzf(t, j);
                    if (zzf != null) {
                        i5 = zzf.hashCode();
                    }
                    i2 = (i2 * 53) + i5;
                    break;
                case 10:
                    i = i2 * 53;
                    zza2 = zzhn.zzf(t, j).hashCode();
                    i2 = i + zza2;
                    break;
                case 11:
                    i = i2 * 53;
                    zza2 = zzhn.zza(t, j);
                    i2 = i + zza2;
                    break;
                case 12:
                    i = i2 * 53;
                    zza2 = zzhn.zza(t, j);
                    i2 = i + zza2;
                    break;
                case 13:
                    i = i2 * 53;
                    zza2 = zzhn.zza(t, j);
                    i2 = i + zza2;
                    break;
                case 14:
                    i = i2 * 53;
                    zza2 = zzeq.zza(zzhn.zzb(t, j));
                    i2 = i + zza2;
                    break;
                case 15:
                    i = i2 * 53;
                    zza2 = zzhn.zza(t, j);
                    i2 = i + zza2;
                    break;
                case 16:
                    i = i2 * 53;
                    zza2 = zzeq.zza(zzhn.zzb(t, j));
                    i2 = i + zza2;
                    break;
                case 17:
                    Object zzf2 = zzhn.zzf(t, j);
                    if (zzf2 != null) {
                        i5 = zzf2.hashCode();
                    }
                    i2 = (i2 * 53) + i5;
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
                    i = i2 * 53;
                    zza2 = zzhn.zzf(t, j).hashCode();
                    i2 = i + zza2;
                    break;
                case 50:
                    i = i2 * 53;
                    zza2 = zzhn.zzf(t, j).hashCode();
                    i2 = i + zza2;
                    break;
                case 51:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzeq.zza(Double.doubleToLongBits(zzb(t, j)));
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case 52:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = Float.floatToIntBits(zzc(t, j));
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case 53:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzeq.zza(zze(t, j));
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case 54:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzeq.zza(zze(t, j));
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case 55:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzd(t, j);
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case 56:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzeq.zza(zze(t, j));
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case 57:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzd(t, j);
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case 58:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzeq.zza(zzf(t, j));
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case 59:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = ((String) zzhn.zzf(t, j)).hashCode();
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case 60:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzhn.zzf(t, j).hashCode();
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case 61:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzhn.zzf(t, j).hashCode();
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case 62:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzd(t, j);
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case 63:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzd(t, j);
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case 64:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzd(t, j);
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case 65:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzeq.zza(zze(t, j));
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case 66:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzd(t, j);
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case 67:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzeq.zza(zze(t, j));
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case 68:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzhn.zzf(t, j).hashCode();
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
            }
        }
        int hashCode = (i2 * 53) + this.zzq.zza(t).hashCode();
        return this.zzh ? (hashCode * 53) + this.zzr.zza(t).hashCode() : hashCode;
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzgp
    public final void zzb(T t, T t2) {
        t2.getClass();
        for (int i = 0; i < this.zzc.length; i += 3) {
            int zzc = zzc(i);
            long j = 1048575 & zzc;
            int i2 = this.zzc[i];
            switch ((zzc & 267386880) >>> 20) {
                case 0:
                    if (zza((zzgd<T>) t2, i)) {
                        zzhn.zza(t, j, zzhn.zze(t2, j));
                        zzb((zzgd<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 1:
                    if (zza((zzgd<T>) t2, i)) {
                        zzhn.zza((Object) t, j, zzhn.zzd(t2, j));
                        zzb((zzgd<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 2:
                    if (zza((zzgd<T>) t2, i)) {
                        zzhn.zza((Object) t, j, zzhn.zzb(t2, j));
                        zzb((zzgd<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 3:
                    if (zza((zzgd<T>) t2, i)) {
                        zzhn.zza((Object) t, j, zzhn.zzb(t2, j));
                        zzb((zzgd<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 4:
                    if (zza((zzgd<T>) t2, i)) {
                        zzhn.zza((Object) t, j, zzhn.zza(t2, j));
                        zzb((zzgd<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 5:
                    if (zza((zzgd<T>) t2, i)) {
                        zzhn.zza((Object) t, j, zzhn.zzb(t2, j));
                        zzb((zzgd<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 6:
                    if (zza((zzgd<T>) t2, i)) {
                        zzhn.zza((Object) t, j, zzhn.zza(t2, j));
                        zzb((zzgd<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 7:
                    if (zza((zzgd<T>) t2, i)) {
                        zzhn.zza(t, j, zzhn.zzc(t2, j));
                        zzb((zzgd<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 8:
                    if (zza((zzgd<T>) t2, i)) {
                        zzhn.zza(t, j, zzhn.zzf(t2, j));
                        zzb((zzgd<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 9:
                    zza(t, t2, i);
                    break;
                case 10:
                    if (zza((zzgd<T>) t2, i)) {
                        zzhn.zza(t, j, zzhn.zzf(t2, j));
                        zzb((zzgd<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 11:
                    if (zza((zzgd<T>) t2, i)) {
                        zzhn.zza((Object) t, j, zzhn.zza(t2, j));
                        zzb((zzgd<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 12:
                    if (zza((zzgd<T>) t2, i)) {
                        zzhn.zza((Object) t, j, zzhn.zza(t2, j));
                        zzb((zzgd<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 13:
                    if (zza((zzgd<T>) t2, i)) {
                        zzhn.zza((Object) t, j, zzhn.zza(t2, j));
                        zzb((zzgd<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 14:
                    if (zza((zzgd<T>) t2, i)) {
                        zzhn.zza((Object) t, j, zzhn.zzb(t2, j));
                        zzb((zzgd<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 15:
                    if (zza((zzgd<T>) t2, i)) {
                        zzhn.zza((Object) t, j, zzhn.zza(t2, j));
                        zzb((zzgd<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 16:
                    if (zza((zzgd<T>) t2, i)) {
                        zzhn.zza((Object) t, j, zzhn.zzb(t2, j));
                        zzb((zzgd<T>) t, i);
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
                    this.zzp.zza(t, t2, j);
                    break;
                case 50:
                    zzgr.zza(this.zzs, t, t2, j);
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
                    if (zza((zzgd<T>) t2, i2, i)) {
                        zzhn.zza(t, j, zzhn.zzf(t2, j));
                        zzb((zzgd<T>) t, i2, i);
                        break;
                    } else {
                        break;
                    }
                case 60:
                    zzb(t, t2, i);
                    break;
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                    if (zza((zzgd<T>) t2, i2, i)) {
                        zzhn.zza(t, j, zzhn.zzf(t2, j));
                        zzb((zzgd<T>) t, i2, i);
                        break;
                    } else {
                        break;
                    }
                case 68:
                    zzb(t, t2, i);
                    break;
            }
        }
        zzgr.zza(this.zzq, t, t2);
        if (this.zzh) {
            zzgr.zza(this.zzr, t, t2);
        }
    }

    private final void zza(T t, T t2, int i) {
        long zzc = zzc(i) & 1048575;
        if (zza((zzgd<T>) t2, i)) {
            Object zzf = zzhn.zzf(t, zzc);
            Object zzf2 = zzhn.zzf(t2, zzc);
            if (zzf != null && zzf2 != null) {
                zzhn.zza(t, zzc, zzeq.zza(zzf, zzf2));
                zzb((zzgd<T>) t, i);
            } else if (zzf2 != null) {
                zzhn.zza(t, zzc, zzf2);
                zzb((zzgd<T>) t, i);
            }
        }
    }

    private final void zzb(T t, T t2, int i) {
        int zzc = zzc(i);
        int i2 = this.zzc[i];
        long j = zzc & 1048575;
        if (zza((zzgd<T>) t2, i2, i)) {
            Object zzf = zzhn.zzf(t, j);
            Object zzf2 = zzhn.zzf(t2, j);
            if (zzf != null && zzf2 != null) {
                zzhn.zza(t, j, zzeq.zza(zzf, zzf2));
                zzb((zzgd<T>) t, i2, i);
            } else if (zzf2 != null) {
                zzhn.zza(t, j, zzf2);
                zzb((zzgd<T>) t, i2, i);
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzgp
    public final int zzd(T t) {
        int i;
        long j;
        int zzd;
        int zzb2;
        int zzb3;
        int zzh;
        int zzi;
        int zze;
        int zzg;
        int zzb4;
        int zzi2;
        int zze2;
        int zzg2;
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
                if (i7 >= zzek.zza.zza() && i7 <= zzek.zzb.zza()) {
                    int i9 = this.zzc[i5 + 2];
                }
                switch (i7) {
                    case 0:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb4 = zzea.zzb(i8, 0.0d);
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 1:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb4 = zzea.zzb(i8, 0.0f);
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 2:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb4 = zzea.zzd(i8, zzhn.zzb(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 3:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb4 = zzea.zze(i8, zzhn.zzb(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 4:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb4 = zzea.zzf(i8, zzhn.zza(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 5:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb4 = zzea.zzg(i8, 0L);
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 6:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb4 = zzea.zzi(i8, 0);
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 7:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb4 = zzea.zzb(i8, true);
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 8:
                        if (zza((zzgd<T>) t, i5)) {
                            Object zzf = zzhn.zzf(t, j2);
                            if (zzf instanceof zzdn) {
                                zzb4 = zzea.zzc(i8, (zzdn) zzf);
                                break;
                            } else {
                                zzb4 = zzea.zzb(i8, (String) zzf);
                                break;
                            }
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 9:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb4 = zzgr.zza(i8, zzhn.zzf(t, j2), zza(i5));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 10:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb4 = zzea.zzc(i8, (zzdn) zzhn.zzf(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 11:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb4 = zzea.zzg(i8, zzhn.zza(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 12:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb4 = zzea.zzk(i8, zzhn.zza(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 13:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb4 = zzea.zzj(i8, 0);
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 14:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb4 = zzea.zzh(i8, 0L);
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 15:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb4 = zzea.zzh(i8, zzhn.zza(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 16:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb4 = zzea.zzf(i8, zzhn.zzb(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 17:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb4 = zzea.zzc(i8, (zzfz) zzhn.zzf(t, j2), zza(i5));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 18:
                        zzb4 = zzgr.zzi(i8, zza(t, j2), false);
                        break;
                    case 19:
                        zzb4 = zzgr.zzh(i8, zza(t, j2), false);
                        break;
                    case 20:
                        zzb4 = zzgr.zza(i8, (List<Long>) zza(t, j2), false);
                        break;
                    case 21:
                        zzb4 = zzgr.zzb(i8, (List<Long>) zza(t, j2), false);
                        break;
                    case 22:
                        zzb4 = zzgr.zze(i8, zza(t, j2), false);
                        break;
                    case 23:
                        zzb4 = zzgr.zzi(i8, zza(t, j2), false);
                        break;
                    case 24:
                        zzb4 = zzgr.zzh(i8, zza(t, j2), false);
                        break;
                    case 25:
                        zzb4 = zzgr.zzj(i8, zza(t, j2), false);
                        break;
                    case 26:
                        zzb4 = zzgr.zza(i8, zza(t, j2));
                        break;
                    case 27:
                        zzb4 = zzgr.zza(i8, zza(t, j2), zza(i5));
                        break;
                    case 28:
                        zzb4 = zzgr.zzb(i8, zza(t, j2));
                        break;
                    case 29:
                        zzb4 = zzgr.zzf(i8, zza(t, j2), false);
                        break;
                    case 30:
                        zzb4 = zzgr.zzd(i8, zza(t, j2), false);
                        break;
                    case 31:
                        zzb4 = zzgr.zzh(i8, zza(t, j2), false);
                        break;
                    case 32:
                        zzb4 = zzgr.zzi(i8, zza(t, j2), false);
                        break;
                    case 33:
                        zzb4 = zzgr.zzg(i8, zza(t, j2), false);
                        break;
                    case 34:
                        zzb4 = zzgr.zzc(i8, zza(t, j2), false);
                        break;
                    case 35:
                        zzi2 = zzgr.zzi((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb4 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 36:
                        zzi2 = zzgr.zzh((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb4 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 37:
                        zzi2 = zzgr.zza((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb4 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 38:
                        zzi2 = zzgr.zzb((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb4 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 39:
                        zzi2 = zzgr.zze((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb4 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 40:
                        zzi2 = zzgr.zzi((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb4 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 41:
                        zzi2 = zzgr.zzh((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb4 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 42:
                        zzi2 = zzgr.zzj((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb4 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 43:
                        zzi2 = zzgr.zzf((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb4 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 44:
                        zzi2 = zzgr.zzd((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb4 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 45:
                        zzi2 = zzgr.zzh((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb4 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 46:
                        zzi2 = zzgr.zzi((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb4 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 47:
                        zzi2 = zzgr.zzg((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb4 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 48:
                        zzi2 = zzgr.zzc((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb4 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 49:
                        zzb4 = zzgr.zzb(i8, (List<zzfz>) zza(t, j2), zza(i5));
                        break;
                    case 50:
                        zzb4 = this.zzs.zza(i8, zzhn.zzf(t, j2), zzb(i5));
                        break;
                    case 51:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb4 = zzea.zzb(i8, 0.0d);
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 52:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb4 = zzea.zzb(i8, 0.0f);
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 53:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb4 = zzea.zzd(i8, zze(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 54:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb4 = zzea.zze(i8, zze(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 55:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb4 = zzea.zzf(i8, zzd(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 56:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb4 = zzea.zzg(i8, 0L);
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 57:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb4 = zzea.zzi(i8, 0);
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 58:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb4 = zzea.zzb(i8, true);
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 59:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            Object zzf2 = zzhn.zzf(t, j2);
                            if (zzf2 instanceof zzdn) {
                                zzb4 = zzea.zzc(i8, (zzdn) zzf2);
                                break;
                            } else {
                                zzb4 = zzea.zzb(i8, (String) zzf2);
                                break;
                            }
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 60:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb4 = zzgr.zza(i8, zzhn.zzf(t, j2), zza(i5));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 61:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb4 = zzea.zzc(i8, (zzdn) zzhn.zzf(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 62:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb4 = zzea.zzg(i8, zzd(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 63:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb4 = zzea.zzk(i8, zzd(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 64:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb4 = zzea.zzj(i8, 0);
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 65:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb4 = zzea.zzh(i8, 0L);
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 66:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb4 = zzea.zzh(i8, zzd(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 67:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb4 = zzea.zzf(i8, zze(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 68:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb4 = zzea.zzc(i8, (zzfz) zzhn.zzf(t, j2), zza(i5));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    default:
                        i5 += 3;
                        i2 = 267386880;
                }
                i6 += zzb4;
                i5 += 3;
                i2 = 267386880;
            }
            return i6 + zza((zzhh) this.zzq, (Object) t);
        }
        Unsafe unsafe2 = zzb;
        int i10 = 0;
        int i11 = 0;
        int i12 = 1048575;
        int i13 = 0;
        while (i10 < this.zzc.length) {
            int zzc2 = zzc(i10);
            int[] iArr = this.zzc;
            int i14 = iArr[i10];
            int i15 = (zzc2 & 267386880) >>> 20;
            if (i15 <= 17) {
                int i16 = iArr[i10 + 2];
                int i17 = i16 & i3;
                i = i4 << (i16 >>> 20);
                if (i17 != i12) {
                    i13 = unsafe2.getInt(t, i17);
                    i12 = i17;
                }
            } else {
                i = 0;
            }
            long j3 = zzc2 & i3;
            switch (i15) {
                case 0:
                    j = 0;
                    if ((i13 & i) != 0) {
                        i11 += zzea.zzb(i14, 0.0d);
                        break;
                    } else {
                        break;
                    }
                case 1:
                    j = 0;
                    if ((i13 & i) != 0) {
                        i11 += zzea.zzb(i14, 0.0f);
                    }
                    break;
                case 2:
                    j = 0;
                    if ((i & i13) != 0) {
                        zzd = zzea.zzd(i14, unsafe2.getLong(t, j3));
                        i11 += zzd;
                    }
                    break;
                case 3:
                    j = 0;
                    if ((i & i13) != 0) {
                        zzd = zzea.zze(i14, unsafe2.getLong(t, j3));
                        i11 += zzd;
                    }
                    break;
                case 4:
                    j = 0;
                    if ((i & i13) != 0) {
                        zzd = zzea.zzf(i14, unsafe2.getInt(t, j3));
                        i11 += zzd;
                    }
                    break;
                case 5:
                    j = 0;
                    if ((i13 & i) != 0) {
                        zzd = zzea.zzg(i14, 0L);
                        i11 += zzd;
                    }
                    break;
                case 6:
                    if ((i13 & i) != 0) {
                        i11 += zzea.zzi(i14, 0);
                    }
                    j = 0;
                    break;
                case 7:
                    if ((i13 & i) != 0) {
                        zzb2 = zzea.zzb(i14, true);
                        i11 += zzb2;
                    }
                    j = 0;
                    break;
                case 8:
                    if ((i13 & i) != 0) {
                        Object object = unsafe2.getObject(t, j3);
                        if (object instanceof zzdn) {
                            zzb3 = zzea.zzc(i14, (zzdn) object);
                        } else {
                            zzb3 = zzea.zzb(i14, (String) object);
                        }
                        i11 += zzb3;
                    }
                    j = 0;
                    break;
                case 9:
                    if ((i13 & i) != 0) {
                        zzb3 = zzgr.zza(i14, unsafe2.getObject(t, j3), zza(i10));
                        i11 += zzb3;
                    }
                    j = 0;
                    break;
                case 10:
                    if ((i13 & i) != 0) {
                        zzb3 = zzea.zzc(i14, (zzdn) unsafe2.getObject(t, j3));
                        i11 += zzb3;
                    }
                    j = 0;
                    break;
                case 11:
                    if ((i13 & i) != 0) {
                        zzb3 = zzea.zzg(i14, unsafe2.getInt(t, j3));
                        i11 += zzb3;
                    }
                    j = 0;
                    break;
                case 12:
                    if ((i13 & i) != 0) {
                        zzb3 = zzea.zzk(i14, unsafe2.getInt(t, j3));
                        i11 += zzb3;
                    }
                    j = 0;
                    break;
                case 13:
                    if ((i13 & i) != 0) {
                        zzb2 = zzea.zzj(i14, 0);
                        i11 += zzb2;
                    }
                    j = 0;
                    break;
                case 14:
                    if ((i13 & i) != 0) {
                        zzb3 = zzea.zzh(i14, 0L);
                        i11 += zzb3;
                    }
                    j = 0;
                    break;
                case 15:
                    if ((i13 & i) != 0) {
                        zzb3 = zzea.zzh(i14, unsafe2.getInt(t, j3));
                        i11 += zzb3;
                    }
                    j = 0;
                    break;
                case 16:
                    if ((i13 & i) != 0) {
                        zzb3 = zzea.zzf(i14, unsafe2.getLong(t, j3));
                        i11 += zzb3;
                    }
                    j = 0;
                    break;
                case 17:
                    if ((i13 & i) != 0) {
                        zzb3 = zzea.zzc(i14, (zzfz) unsafe2.getObject(t, j3), zza(i10));
                        i11 += zzb3;
                    }
                    j = 0;
                    break;
                case 18:
                    zzb3 = zzgr.zzi(i14, (List) unsafe2.getObject(t, j3), false);
                    i11 += zzb3;
                    j = 0;
                    break;
                case 19:
                    zzh = zzgr.zzh(i14, (List) unsafe2.getObject(t, j3), false);
                    i11 += zzh;
                    j = 0;
                    break;
                case 20:
                    zzh = zzgr.zza(i14, (List<Long>) unsafe2.getObject(t, j3), false);
                    i11 += zzh;
                    j = 0;
                    break;
                case 21:
                    zzh = zzgr.zzb(i14, (List<Long>) unsafe2.getObject(t, j3), false);
                    i11 += zzh;
                    j = 0;
                    break;
                case 22:
                    zzh = zzgr.zze(i14, (List) unsafe2.getObject(t, j3), false);
                    i11 += zzh;
                    j = 0;
                    break;
                case 23:
                    zzh = zzgr.zzi(i14, (List) unsafe2.getObject(t, j3), false);
                    i11 += zzh;
                    j = 0;
                    break;
                case 24:
                    zzh = zzgr.zzh(i14, (List) unsafe2.getObject(t, j3), false);
                    i11 += zzh;
                    j = 0;
                    break;
                case 25:
                    zzh = zzgr.zzj(i14, (List) unsafe2.getObject(t, j3), false);
                    i11 += zzh;
                    j = 0;
                    break;
                case 26:
                    zzb3 = zzgr.zza(i14, (List) unsafe2.getObject(t, j3));
                    i11 += zzb3;
                    j = 0;
                    break;
                case 27:
                    zzb3 = zzgr.zza(i14, (List<?>) unsafe2.getObject(t, j3), zza(i10));
                    i11 += zzb3;
                    j = 0;
                    break;
                case 28:
                    zzb3 = zzgr.zzb(i14, (List) unsafe2.getObject(t, j3));
                    i11 += zzb3;
                    j = 0;
                    break;
                case 29:
                    zzb3 = zzgr.zzf(i14, (List) unsafe2.getObject(t, j3), false);
                    i11 += zzb3;
                    j = 0;
                    break;
                case 30:
                    zzh = zzgr.zzd(i14, (List) unsafe2.getObject(t, j3), false);
                    i11 += zzh;
                    j = 0;
                    break;
                case 31:
                    zzh = zzgr.zzh(i14, (List) unsafe2.getObject(t, j3), false);
                    i11 += zzh;
                    j = 0;
                    break;
                case 32:
                    zzh = zzgr.zzi(i14, (List) unsafe2.getObject(t, j3), false);
                    i11 += zzh;
                    j = 0;
                    break;
                case 33:
                    zzh = zzgr.zzg(i14, (List) unsafe2.getObject(t, j3), false);
                    i11 += zzh;
                    j = 0;
                    break;
                case 34:
                    zzh = zzgr.zzc(i14, (List) unsafe2.getObject(t, j3), false);
                    i11 += zzh;
                    j = 0;
                    break;
                case 35:
                    zzi = zzgr.zzi((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzb2 = zze + zzg + zzi;
                        i11 += zzb2;
                    }
                    j = 0;
                    break;
                case 36:
                    zzi = zzgr.zzh((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzb2 = zze + zzg + zzi;
                        i11 += zzb2;
                    }
                    j = 0;
                    break;
                case 37:
                    zzi = zzgr.zza((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzb2 = zze + zzg + zzi;
                        i11 += zzb2;
                    }
                    j = 0;
                    break;
                case 38:
                    zzi = zzgr.zzb((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzb2 = zze + zzg + zzi;
                        i11 += zzb2;
                    }
                    j = 0;
                    break;
                case 39:
                    zzi = zzgr.zze((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzb2 = zze + zzg + zzi;
                        i11 += zzb2;
                    }
                    j = 0;
                    break;
                case 40:
                    zzi = zzgr.zzi((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzb2 = zze + zzg + zzi;
                        i11 += zzb2;
                    }
                    j = 0;
                    break;
                case 41:
                    zzi = zzgr.zzh((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzb2 = zze + zzg + zzi;
                        i11 += zzb2;
                    }
                    j = 0;
                    break;
                case 42:
                    zzi = zzgr.zzj((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzb2 = zze + zzg + zzi;
                        i11 += zzb2;
                    }
                    j = 0;
                    break;
                case 43:
                    zzi = zzgr.zzf((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzb2 = zze + zzg + zzi;
                        i11 += zzb2;
                    }
                    j = 0;
                    break;
                case 44:
                    zzi = zzgr.zzd((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzb2 = zze + zzg + zzi;
                        i11 += zzb2;
                    }
                    j = 0;
                    break;
                case 45:
                    zzi = zzgr.zzh((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzb2 = zze + zzg + zzi;
                        i11 += zzb2;
                    }
                    j = 0;
                    break;
                case 46:
                    zzi = zzgr.zzi((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzb2 = zze + zzg + zzi;
                        i11 += zzb2;
                    }
                    j = 0;
                    break;
                case 47:
                    zzi = zzgr.zzg((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzb2 = zze + zzg + zzi;
                        i11 += zzb2;
                    }
                    j = 0;
                    break;
                case 48:
                    zzi = zzgr.zzc((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzb2 = zze + zzg + zzi;
                        i11 += zzb2;
                    }
                    j = 0;
                    break;
                case 49:
                    zzb3 = zzgr.zzb(i14, (List) unsafe2.getObject(t, j3), zza(i10));
                    i11 += zzb3;
                    j = 0;
                    break;
                case 50:
                    zzb3 = this.zzs.zza(i14, unsafe2.getObject(t, j3), zzb(i10));
                    i11 += zzb3;
                    j = 0;
                    break;
                case 51:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb3 = zzea.zzb(i14, 0.0d);
                        i11 += zzb3;
                    }
                    j = 0;
                    break;
                case 52:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb2 = zzea.zzb(i14, 0.0f);
                        i11 += zzb2;
                    }
                    j = 0;
                    break;
                case 53:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb3 = zzea.zzd(i14, zze(t, j3));
                        i11 += zzb3;
                    }
                    j = 0;
                    break;
                case 54:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb3 = zzea.zze(i14, zze(t, j3));
                        i11 += zzb3;
                    }
                    j = 0;
                    break;
                case 55:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb3 = zzea.zzf(i14, zzd(t, j3));
                        i11 += zzb3;
                    }
                    j = 0;
                    break;
                case 56:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb3 = zzea.zzg(i14, 0L);
                        i11 += zzb3;
                    }
                    j = 0;
                    break;
                case 57:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb2 = zzea.zzi(i14, 0);
                        i11 += zzb2;
                    }
                    j = 0;
                    break;
                case 58:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb2 = zzea.zzb(i14, true);
                        i11 += zzb2;
                    }
                    j = 0;
                    break;
                case 59:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        Object object2 = unsafe2.getObject(t, j3);
                        if (object2 instanceof zzdn) {
                            zzb3 = zzea.zzc(i14, (zzdn) object2);
                        } else {
                            zzb3 = zzea.zzb(i14, (String) object2);
                        }
                        i11 += zzb3;
                    }
                    j = 0;
                    break;
                case 60:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb3 = zzgr.zza(i14, unsafe2.getObject(t, j3), zza(i10));
                        i11 += zzb3;
                    }
                    j = 0;
                    break;
                case 61:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb3 = zzea.zzc(i14, (zzdn) unsafe2.getObject(t, j3));
                        i11 += zzb3;
                    }
                    j = 0;
                    break;
                case 62:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb3 = zzea.zzg(i14, zzd(t, j3));
                        i11 += zzb3;
                    }
                    j = 0;
                    break;
                case 63:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb3 = zzea.zzk(i14, zzd(t, j3));
                        i11 += zzb3;
                    }
                    j = 0;
                    break;
                case 64:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb2 = zzea.zzj(i14, 0);
                        i11 += zzb2;
                    }
                    j = 0;
                    break;
                case 65:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb3 = zzea.zzh(i14, 0L);
                        i11 += zzb3;
                    }
                    j = 0;
                    break;
                case 66:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb3 = zzea.zzh(i14, zzd(t, j3));
                        i11 += zzb3;
                    }
                    j = 0;
                    break;
                case 67:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb3 = zzea.zzf(i14, zze(t, j3));
                        i11 += zzb3;
                    }
                    j = 0;
                    break;
                case 68:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb3 = zzea.zzc(i14, (zzfz) unsafe2.getObject(t, j3), zza(i10));
                        i11 += zzb3;
                    }
                    j = 0;
                    break;
                default:
                    j = 0;
                    break;
            }
            i10 += 3;
            i3 = 1048575;
            i4 = 1;
        }
        int i18 = 0;
        int zza2 = i11 + zza((zzhh) this.zzq, (Object) t);
        if (this.zzh) {
            zzej<?> zza3 = this.zzr.zza(t);
            for (int i19 = 0; i19 < zza3.zza.zzc(); i19++) {
                Map.Entry<?, Object> zzb5 = zza3.zza.zzb(i19);
                i18 += zzej.zza((zzel) zzb5.getKey(), zzb5.getValue());
            }
            for (Map.Entry<?, Object> entry : zza3.zza.zzd()) {
                i18 += zzej.zza((zzel) entry.getKey(), entry.getValue());
            }
            return zza2 + i18;
        }
        return zza2;
    }

    private static <UT, UB> int zza(zzhh<UT, UB> zzhhVar, T t) {
        return zzhhVar.zzd(zzhhVar.zza(t));
    }

    private static List<?> zza(Object obj, long j) {
        return (List) zzhn.zzf(obj, j);
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x003b  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x0513  */
    /* JADX WARN: Removed duplicated region for block: B:181:0x0552  */
    /* JADX WARN: Removed duplicated region for block: B:335:0x0a2a  */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzgp
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void zza(T t, zzib zzibVar) throws IOException {
        Iterator<Map.Entry<?, Object>> it;
        Map.Entry<?, Object> entry;
        int length;
        int i;
        Iterator<Map.Entry<?, Object>> it2;
        Map.Entry<?, Object> entry2;
        int length2;
        if (zzibVar.zza() == zzia.zzb) {
            zza(this.zzq, t, zzibVar);
            if (this.zzh) {
                zzej<?> zza2 = this.zzr.zza(t);
                if (!zza2.zza.isEmpty()) {
                    it2 = zza2.zze();
                    entry2 = it2.next();
                    for (length2 = this.zzc.length - 3; length2 >= 0; length2 -= 3) {
                        int zzc = zzc(length2);
                        int i2 = this.zzc[length2];
                        while (entry2 != null && this.zzr.zza(entry2) > i2) {
                            this.zzr.zza(zzibVar, entry2);
                            entry2 = it2.hasNext() ? it2.next() : null;
                        }
                        switch ((zzc & 267386880) >>> 20) {
                            case 0:
                                if (zza((zzgd<T>) t, length2)) {
                                    zzibVar.zza(i2, zzhn.zze(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 1:
                                if (zza((zzgd<T>) t, length2)) {
                                    zzibVar.zza(i2, zzhn.zzd(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 2:
                                if (zza((zzgd<T>) t, length2)) {
                                    zzibVar.zza(i2, zzhn.zzb(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 3:
                                if (zza((zzgd<T>) t, length2)) {
                                    zzibVar.zzc(i2, zzhn.zzb(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 4:
                                if (zza((zzgd<T>) t, length2)) {
                                    zzibVar.zzc(i2, zzhn.zza(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 5:
                                if (zza((zzgd<T>) t, length2)) {
                                    zzibVar.zzd(i2, zzhn.zzb(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 6:
                                if (zza((zzgd<T>) t, length2)) {
                                    zzibVar.zzd(i2, zzhn.zza(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 7:
                                if (zza((zzgd<T>) t, length2)) {
                                    zzibVar.zza(i2, zzhn.zzc(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 8:
                                if (zza((zzgd<T>) t, length2)) {
                                    zza(i2, zzhn.zzf(t, zzc & 1048575), zzibVar);
                                    break;
                                } else {
                                    break;
                                }
                            case 9:
                                if (zza((zzgd<T>) t, length2)) {
                                    zzibVar.zza(i2, zzhn.zzf(t, zzc & 1048575), zza(length2));
                                    break;
                                } else {
                                    break;
                                }
                            case 10:
                                if (zza((zzgd<T>) t, length2)) {
                                    zzibVar.zza(i2, (zzdn) zzhn.zzf(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 11:
                                if (zza((zzgd<T>) t, length2)) {
                                    zzibVar.zze(i2, zzhn.zza(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 12:
                                if (zza((zzgd<T>) t, length2)) {
                                    zzibVar.zzb(i2, zzhn.zza(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 13:
                                if (zza((zzgd<T>) t, length2)) {
                                    zzibVar.zza(i2, zzhn.zza(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 14:
                                if (zza((zzgd<T>) t, length2)) {
                                    zzibVar.zzb(i2, zzhn.zzb(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 15:
                                if (zza((zzgd<T>) t, length2)) {
                                    zzibVar.zzf(i2, zzhn.zza(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 16:
                                if (zza((zzgd<T>) t, length2)) {
                                    zzibVar.zze(i2, zzhn.zzb(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 17:
                                if (zza((zzgd<T>) t, length2)) {
                                    zzibVar.zzb(i2, zzhn.zzf(t, zzc & 1048575), zza(length2));
                                    break;
                                } else {
                                    break;
                                }
                            case 18:
                                zzgr.zza(this.zzc[length2], (List<Double>) zzhn.zzf(t, zzc & 1048575), zzibVar, false);
                                break;
                            case 19:
                                zzgr.zzb(this.zzc[length2], (List<Float>) zzhn.zzf(t, zzc & 1048575), zzibVar, false);
                                break;
                            case 20:
                                zzgr.zzc(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, false);
                                break;
                            case 21:
                                zzgr.zzd(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, false);
                                break;
                            case 22:
                                zzgr.zzh(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, false);
                                break;
                            case 23:
                                zzgr.zzf(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, false);
                                break;
                            case 24:
                                zzgr.zzk(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, false);
                                break;
                            case 25:
                                zzgr.zzn(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, false);
                                break;
                            case 26:
                                zzgr.zza(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar);
                                break;
                            case 27:
                                zzgr.zza(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, zza(length2));
                                break;
                            case 28:
                                zzgr.zzb(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar);
                                break;
                            case 29:
                                zzgr.zzi(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, false);
                                break;
                            case 30:
                                zzgr.zzm(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, false);
                                break;
                            case 31:
                                zzgr.zzl(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, false);
                                break;
                            case 32:
                                zzgr.zzg(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, false);
                                break;
                            case 33:
                                zzgr.zzj(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, false);
                                break;
                            case 34:
                                zzgr.zze(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, false);
                                break;
                            case 35:
                                zzgr.zza(this.zzc[length2], (List<Double>) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case 36:
                                zzgr.zzb(this.zzc[length2], (List<Float>) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case 37:
                                zzgr.zzc(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case 38:
                                zzgr.zzd(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case 39:
                                zzgr.zzh(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case 40:
                                zzgr.zzf(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case 41:
                                zzgr.zzk(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case 42:
                                zzgr.zzn(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case 43:
                                zzgr.zzi(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case 44:
                                zzgr.zzm(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case 45:
                                zzgr.zzl(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case 46:
                                zzgr.zzg(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case 47:
                                zzgr.zzj(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case 48:
                                zzgr.zze(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case 49:
                                zzgr.zzb(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, zza(length2));
                                break;
                            case 50:
                                zza(zzibVar, i2, zzhn.zzf(t, zzc & 1048575), length2);
                                break;
                            case 51:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zza(i2, zzb(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 52:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zza(i2, zzc(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 53:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zza(i2, zze(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 54:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zzc(i2, zze(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 55:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zzc(i2, zzd(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 56:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zzd(i2, zze(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 57:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zzd(i2, zzd(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 58:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zza(i2, zzf(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 59:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zza(i2, zzhn.zzf(t, zzc & 1048575), zzibVar);
                                    break;
                                } else {
                                    break;
                                }
                            case 60:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zza(i2, zzhn.zzf(t, zzc & 1048575), zza(length2));
                                    break;
                                } else {
                                    break;
                                }
                            case 61:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zza(i2, (zzdn) zzhn.zzf(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 62:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zze(i2, zzd(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 63:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zzb(i2, zzd(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 64:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zza(i2, zzd(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 65:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zzb(i2, zze(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 66:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zzf(i2, zzd(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 67:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zze(i2, zze(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 68:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zzb(i2, zzhn.zzf(t, zzc & 1048575), zza(length2));
                                    break;
                                } else {
                                    break;
                                }
                        }
                    }
                    while (entry2 != null) {
                        this.zzr.zza(zzibVar, entry2);
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
                zzej<?> zza3 = this.zzr.zza(t);
                if (!zza3.zza.isEmpty()) {
                    it = zza3.zzd();
                    entry = it.next();
                    length = this.zzc.length;
                    for (i = 0; i < length; i += 3) {
                        int zzc2 = zzc(i);
                        int i3 = this.zzc[i];
                        while (entry != null && this.zzr.zza(entry) <= i3) {
                            this.zzr.zza(zzibVar, entry);
                            entry = it.hasNext() ? it.next() : null;
                        }
                        switch ((zzc2 & 267386880) >>> 20) {
                            case 0:
                                if (zza((zzgd<T>) t, i)) {
                                    zzibVar.zza(i3, zzhn.zze(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 1:
                                if (zza((zzgd<T>) t, i)) {
                                    zzibVar.zza(i3, zzhn.zzd(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 2:
                                if (zza((zzgd<T>) t, i)) {
                                    zzibVar.zza(i3, zzhn.zzb(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 3:
                                if (zza((zzgd<T>) t, i)) {
                                    zzibVar.zzc(i3, zzhn.zzb(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 4:
                                if (zza((zzgd<T>) t, i)) {
                                    zzibVar.zzc(i3, zzhn.zza(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 5:
                                if (zza((zzgd<T>) t, i)) {
                                    zzibVar.zzd(i3, zzhn.zzb(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 6:
                                if (zza((zzgd<T>) t, i)) {
                                    zzibVar.zzd(i3, zzhn.zza(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 7:
                                if (zza((zzgd<T>) t, i)) {
                                    zzibVar.zza(i3, zzhn.zzc(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 8:
                                if (zza((zzgd<T>) t, i)) {
                                    zza(i3, zzhn.zzf(t, zzc2 & 1048575), zzibVar);
                                    break;
                                } else {
                                    break;
                                }
                            case 9:
                                if (zza((zzgd<T>) t, i)) {
                                    zzibVar.zza(i3, zzhn.zzf(t, zzc2 & 1048575), zza(i));
                                    break;
                                } else {
                                    break;
                                }
                            case 10:
                                if (zza((zzgd<T>) t, i)) {
                                    zzibVar.zza(i3, (zzdn) zzhn.zzf(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 11:
                                if (zza((zzgd<T>) t, i)) {
                                    zzibVar.zze(i3, zzhn.zza(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 12:
                                if (zza((zzgd<T>) t, i)) {
                                    zzibVar.zzb(i3, zzhn.zza(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 13:
                                if (zza((zzgd<T>) t, i)) {
                                    zzibVar.zza(i3, zzhn.zza(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 14:
                                if (zza((zzgd<T>) t, i)) {
                                    zzibVar.zzb(i3, zzhn.zzb(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 15:
                                if (zza((zzgd<T>) t, i)) {
                                    zzibVar.zzf(i3, zzhn.zza(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 16:
                                if (zza((zzgd<T>) t, i)) {
                                    zzibVar.zze(i3, zzhn.zzb(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 17:
                                if (zza((zzgd<T>) t, i)) {
                                    zzibVar.zzb(i3, zzhn.zzf(t, zzc2 & 1048575), zza(i));
                                    break;
                                } else {
                                    break;
                                }
                            case 18:
                                zzgr.zza(this.zzc[i], (List<Double>) zzhn.zzf(t, zzc2 & 1048575), zzibVar, false);
                                break;
                            case 19:
                                zzgr.zzb(this.zzc[i], (List<Float>) zzhn.zzf(t, zzc2 & 1048575), zzibVar, false);
                                break;
                            case 20:
                                zzgr.zzc(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, false);
                                break;
                            case 21:
                                zzgr.zzd(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, false);
                                break;
                            case 22:
                                zzgr.zzh(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, false);
                                break;
                            case 23:
                                zzgr.zzf(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, false);
                                break;
                            case 24:
                                zzgr.zzk(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, false);
                                break;
                            case 25:
                                zzgr.zzn(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, false);
                                break;
                            case 26:
                                zzgr.zza(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar);
                                break;
                            case 27:
                                zzgr.zza(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, zza(i));
                                break;
                            case 28:
                                zzgr.zzb(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar);
                                break;
                            case 29:
                                zzgr.zzi(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, false);
                                break;
                            case 30:
                                zzgr.zzm(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, false);
                                break;
                            case 31:
                                zzgr.zzl(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, false);
                                break;
                            case 32:
                                zzgr.zzg(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, false);
                                break;
                            case 33:
                                zzgr.zzj(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, false);
                                break;
                            case 34:
                                zzgr.zze(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, false);
                                break;
                            case 35:
                                zzgr.zza(this.zzc[i], (List<Double>) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case 36:
                                zzgr.zzb(this.zzc[i], (List<Float>) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case 37:
                                zzgr.zzc(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case 38:
                                zzgr.zzd(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case 39:
                                zzgr.zzh(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case 40:
                                zzgr.zzf(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case 41:
                                zzgr.zzk(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case 42:
                                zzgr.zzn(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case 43:
                                zzgr.zzi(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case 44:
                                zzgr.zzm(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case 45:
                                zzgr.zzl(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case 46:
                                zzgr.zzg(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case 47:
                                zzgr.zzj(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case 48:
                                zzgr.zze(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case 49:
                                zzgr.zzb(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, zza(i));
                                break;
                            case 50:
                                zza(zzibVar, i3, zzhn.zzf(t, zzc2 & 1048575), i);
                                break;
                            case 51:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zza(i3, zzb(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 52:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zza(i3, zzc(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 53:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zza(i3, zze(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 54:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zzc(i3, zze(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 55:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zzc(i3, zzd(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 56:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zzd(i3, zze(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 57:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zzd(i3, zzd(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 58:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zza(i3, zzf(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 59:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zza(i3, zzhn.zzf(t, zzc2 & 1048575), zzibVar);
                                    break;
                                } else {
                                    break;
                                }
                            case 60:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zza(i3, zzhn.zzf(t, zzc2 & 1048575), zza(i));
                                    break;
                                } else {
                                    break;
                                }
                            case 61:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zza(i3, (zzdn) zzhn.zzf(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 62:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zze(i3, zzd(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 63:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zzb(i3, zzd(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 64:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zza(i3, zzd(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 65:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zzb(i3, zze(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 66:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zzf(i3, zzd(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 67:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zze(i3, zze(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 68:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zzb(i3, zzhn.zzf(t, zzc2 & 1048575), zza(i));
                                    break;
                                } else {
                                    break;
                                }
                        }
                    }
                    while (entry != null) {
                        this.zzr.zza(zzibVar, entry);
                        entry = it.hasNext() ? it.next() : null;
                    }
                    zza(this.zzq, t, zzibVar);
                }
            }
            it = null;
            entry = null;
            length = this.zzc.length;
            while (i < length) {
            }
            while (entry != null) {
            }
            zza(this.zzq, t, zzibVar);
        } else {
            zzb((zzgd<T>) t, zzibVar);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0031  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x0495  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void zzb(T t, zzib zzibVar) throws IOException {
        Iterator<Map.Entry<?, Object>> it;
        Map.Entry<?, Object> entry;
        int length;
        int i;
        int i2;
        if (this.zzh) {
            zzej<?> zza2 = this.zzr.zza(t);
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
                    if (this.zzj || i6 > 17) {
                        i2 = 0;
                    } else {
                        int i7 = iArr[i + 2];
                        int i8 = i7 & 1048575;
                        if (i8 != i3) {
                            i4 = unsafe.getInt(t, i8);
                            i3 = i8;
                        }
                        i2 = 1 << (i7 >>> 20);
                    }
                    while (entry != null && this.zzr.zza(entry) <= i5) {
                        this.zzr.zza(zzibVar, entry);
                        entry = it.hasNext() ? it.next() : null;
                    }
                    long j = zzc & 1048575;
                    switch (i6) {
                        case 0:
                            if ((i2 & i4) != 0) {
                                zzibVar.zza(i5, zzhn.zze(t, j));
                                continue;
                            }
                        case 1:
                            if ((i2 & i4) != 0) {
                                zzibVar.zza(i5, zzhn.zzd(t, j));
                            } else {
                                continue;
                            }
                        case 2:
                            if ((i2 & i4) != 0) {
                                zzibVar.zza(i5, unsafe.getLong(t, j));
                            } else {
                                continue;
                            }
                        case 3:
                            if ((i2 & i4) != 0) {
                                zzibVar.zzc(i5, unsafe.getLong(t, j));
                            } else {
                                continue;
                            }
                        case 4:
                            if ((i2 & i4) != 0) {
                                zzibVar.zzc(i5, unsafe.getInt(t, j));
                            } else {
                                continue;
                            }
                        case 5:
                            if ((i2 & i4) != 0) {
                                zzibVar.zzd(i5, unsafe.getLong(t, j));
                            } else {
                                continue;
                            }
                        case 6:
                            if ((i2 & i4) != 0) {
                                zzibVar.zzd(i5, unsafe.getInt(t, j));
                            } else {
                                continue;
                            }
                        case 7:
                            if ((i2 & i4) != 0) {
                                zzibVar.zza(i5, zzhn.zzc(t, j));
                            } else {
                                continue;
                            }
                        case 8:
                            if ((i2 & i4) != 0) {
                                zza(i5, unsafe.getObject(t, j), zzibVar);
                            } else {
                                continue;
                            }
                        case 9:
                            if ((i2 & i4) != 0) {
                                zzibVar.zza(i5, unsafe.getObject(t, j), zza(i));
                            } else {
                                continue;
                            }
                        case 10:
                            if ((i2 & i4) != 0) {
                                zzibVar.zza(i5, (zzdn) unsafe.getObject(t, j));
                            } else {
                                continue;
                            }
                        case 11:
                            if ((i2 & i4) != 0) {
                                zzibVar.zze(i5, unsafe.getInt(t, j));
                            } else {
                                continue;
                            }
                        case 12:
                            if ((i2 & i4) != 0) {
                                zzibVar.zzb(i5, unsafe.getInt(t, j));
                            } else {
                                continue;
                            }
                        case 13:
                            if ((i2 & i4) != 0) {
                                zzibVar.zza(i5, unsafe.getInt(t, j));
                            } else {
                                continue;
                            }
                        case 14:
                            if ((i2 & i4) != 0) {
                                zzibVar.zzb(i5, unsafe.getLong(t, j));
                            } else {
                                continue;
                            }
                        case 15:
                            if ((i2 & i4) != 0) {
                                zzibVar.zzf(i5, unsafe.getInt(t, j));
                            } else {
                                continue;
                            }
                        case 16:
                            if ((i2 & i4) != 0) {
                                zzibVar.zze(i5, unsafe.getLong(t, j));
                            } else {
                                continue;
                            }
                        case 17:
                            if ((i2 & i4) != 0) {
                                zzibVar.zzb(i5, unsafe.getObject(t, j), zza(i));
                            } else {
                                continue;
                            }
                        case 18:
                            zzgr.zza(this.zzc[i], (List<Double>) unsafe.getObject(t, j), zzibVar, false);
                            break;
                        case 19:
                            zzgr.zzb(this.zzc[i], (List<Float>) unsafe.getObject(t, j), zzibVar, false);
                            break;
                        case 20:
                            zzgr.zzc(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, false);
                            break;
                        case 21:
                            zzgr.zzd(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, false);
                            break;
                        case 22:
                            zzgr.zzh(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, false);
                            break;
                        case 23:
                            zzgr.zzf(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, false);
                            break;
                        case 24:
                            zzgr.zzk(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, false);
                            break;
                        case 25:
                            zzgr.zzn(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, false);
                            break;
                        case 26:
                            zzgr.zza(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar);
                            break;
                        case 27:
                            zzgr.zza(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, zza(i));
                            break;
                        case 28:
                            zzgr.zzb(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar);
                            break;
                        case 29:
                            zzgr.zzi(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, false);
                            break;
                        case 30:
                            zzgr.zzm(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, false);
                            break;
                        case 31:
                            zzgr.zzl(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, false);
                            break;
                        case 32:
                            zzgr.zzg(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, false);
                            break;
                        case 33:
                            zzgr.zzj(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, false);
                            break;
                        case 34:
                            zzgr.zze(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, false);
                            break;
                        case 35:
                            zzgr.zza(this.zzc[i], (List<Double>) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case 36:
                            zzgr.zzb(this.zzc[i], (List<Float>) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case 37:
                            zzgr.zzc(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case 38:
                            zzgr.zzd(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case 39:
                            zzgr.zzh(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case 40:
                            zzgr.zzf(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case 41:
                            zzgr.zzk(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case 42:
                            zzgr.zzn(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case 43:
                            zzgr.zzi(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case 44:
                            zzgr.zzm(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case 45:
                            zzgr.zzl(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case 46:
                            zzgr.zzg(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case 47:
                            zzgr.zzj(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case 48:
                            zzgr.zze(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case 49:
                            zzgr.zzb(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, zza(i));
                            break;
                        case 50:
                            zza(zzibVar, i5, unsafe.getObject(t, j), i);
                            break;
                        case 51:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zza(i5, zzb(t, j));
                                break;
                            }
                            break;
                        case 52:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zza(i5, zzc(t, j));
                                break;
                            }
                            break;
                        case 53:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zza(i5, zze(t, j));
                                break;
                            }
                            break;
                        case 54:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zzc(i5, zze(t, j));
                                break;
                            }
                            break;
                        case 55:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zzc(i5, zzd(t, j));
                                break;
                            }
                            break;
                        case 56:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zzd(i5, zze(t, j));
                                break;
                            }
                            break;
                        case 57:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zzd(i5, zzd(t, j));
                                break;
                            }
                            break;
                        case 58:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zza(i5, zzf(t, j));
                                break;
                            }
                            break;
                        case 59:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zza(i5, unsafe.getObject(t, j), zzibVar);
                                break;
                            }
                            break;
                        case 60:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zza(i5, unsafe.getObject(t, j), zza(i));
                                break;
                            }
                            break;
                        case 61:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zza(i5, (zzdn) unsafe.getObject(t, j));
                                break;
                            }
                            break;
                        case 62:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zze(i5, zzd(t, j));
                                break;
                            }
                            break;
                        case 63:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zzb(i5, zzd(t, j));
                                break;
                            }
                            break;
                        case 64:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zza(i5, zzd(t, j));
                                break;
                            }
                            break;
                        case 65:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zzb(i5, zze(t, j));
                                break;
                            }
                            break;
                        case 66:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zzf(i5, zzd(t, j));
                                break;
                            }
                            break;
                        case 67:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zze(i5, zze(t, j));
                                break;
                            }
                            break;
                        case 68:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zzb(i5, unsafe.getObject(t, j), zza(i));
                                break;
                            }
                            break;
                    }
                }
                while (entry != null) {
                    this.zzr.zza(zzibVar, entry);
                    entry = it.hasNext() ? it.next() : null;
                }
                zza(this.zzq, t, zzibVar);
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
        zza(this.zzq, t, zzibVar);
    }

    private final <K, V> void zza(zzib zzibVar, int i, Object obj, int i2) throws IOException {
        if (obj != null) {
            this.zzs.zzc(zzb(i2));
            zzibVar.zza(i, (zzfq) null, this.zzs.zza(obj));
        }
    }

    private static <UT, UB> void zza(zzhh<UT, UB> zzhhVar, T t, zzib zzibVar) throws IOException {
        zzhhVar.zza((zzhh<UT, UB>) zzhhVar.zza(t), zzibVar);
    }

    private final zzgp zza(int i) {
        int i2 = (i / 3) << 1;
        zzgp zzgpVar = (zzgp) this.zzd[i2];
        if (zzgpVar != null) {
            return zzgpVar;
        }
        zzgp<T> zza2 = zzgk.zza().zza((Class) ((Class) this.zzd[i2 + 1]));
        this.zzd[i2] = zza2;
        return zza2;
    }

    private final Object zzb(int i) {
        return this.zzd[(i / 3) << 1];
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzgp
    public final void zzb(T t) {
        int i;
        int i2 = this.zzm;
        while (true) {
            i = this.zzn;
            if (i2 >= i) {
                break;
            }
            long zzc = zzc(this.zzl[i2]) & 1048575;
            Object zzf = zzhn.zzf(t, zzc);
            if (zzf != null) {
                zzhn.zza(t, zzc, this.zzs.zzb(zzf));
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
    @Override // com.google.android.gms.internal.mlkit_language_id.zzgp
    public final boolean zzc(T t) {
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
                    i4 = zzb.getInt(t, i9);
                }
                i2 = i4;
                i = i9;
            } else {
                i = i3;
                i2 = i4;
            }
            if ((268435456 & zzc) != 0 && !zza(t, i6, i, i2, i10)) {
                return false;
            }
            int i11 = (267386880 & zzc) >>> 20;
            if (i11 == 9 || i11 == 17) {
                if (zza(t, i6, i, i2, i10) && !zza(t, zzc, zza(i6))) {
                    return false;
                }
            } else {
                if (i11 != 27) {
                    if (i11 == 60 || i11 == 68) {
                        if (zza((zzgd<T>) t, i7, i6) && !zza(t, zzc, zza(i6))) {
                            return false;
                        }
                    } else if (i11 != 49) {
                        if (i11 == 50 && !this.zzs.zza(zzhn.zzf(t, zzc & 1048575)).isEmpty()) {
                            this.zzs.zzc(zzb(i6));
                            throw null;
                        }
                    }
                }
                List list = (List) zzhn.zzf(t, zzc & 1048575);
                if (list.isEmpty()) {
                    continue;
                } else {
                    zzgp zza2 = zza(i6);
                    for (int i12 = 0; i12 < list.size(); i12++) {
                        if (!zza2.zzc(list.get(i12))) {
                            return false;
                        }
                    }
                    continue;
                }
            }
            i5++;
            i3 = i;
            i4 = i2;
        }
        return !this.zzh || this.zzr.zza(t).zzf();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static boolean zza(Object obj, int i, zzgp zzgpVar) {
        return zzgpVar.zzc(zzhn.zzf(obj, i & 1048575));
    }

    private static void zza(int i, Object obj, zzib zzibVar) throws IOException {
        if (obj instanceof String) {
            zzibVar.zza(i, (String) obj);
        } else {
            zzibVar.zza(i, (zzdn) obj);
        }
    }

    private final int zzc(int i) {
        return this.zzc[i + 1];
    }

    private final int zzd(int i) {
        return this.zzc[i + 2];
    }

    private static <T> double zzb(T t, long j) {
        return ((Double) zzhn.zzf(t, j)).doubleValue();
    }

    private static <T> float zzc(T t, long j) {
        return ((Float) zzhn.zzf(t, j)).floatValue();
    }

    private static <T> int zzd(T t, long j) {
        return ((Integer) zzhn.zzf(t, j)).intValue();
    }

    private static <T> long zze(T t, long j) {
        return ((Long) zzhn.zzf(t, j)).longValue();
    }

    private static <T> boolean zzf(T t, long j) {
        return ((Boolean) zzhn.zzf(t, j)).booleanValue();
    }

    private final boolean zzc(T t, T t2, int i) {
        return zza((zzgd<T>) t, i) == zza((zzgd<T>) t2, i);
    }

    private final boolean zza(T t, int i, int i2, int i3, int i4) {
        if (i2 == 1048575) {
            return zza((zzgd<T>) t, i);
        }
        return (i3 & i4) != 0;
    }

    private final boolean zza(T t, int i) {
        int zzd = zzd(i);
        long j = zzd & 1048575;
        if (j != 1048575) {
            return (zzhn.zza(t, j) & (1 << (zzd >>> 20))) != 0;
        }
        int zzc = zzc(i);
        long j2 = zzc & 1048575;
        switch ((zzc & 267386880) >>> 20) {
            case 0:
                return zzhn.zze(t, j2) != 0.0d;
            case 1:
                return zzhn.zzd(t, j2) != 0.0f;
            case 2:
                return zzhn.zzb(t, j2) != 0;
            case 3:
                return zzhn.zzb(t, j2) != 0;
            case 4:
                return zzhn.zza(t, j2) != 0;
            case 5:
                return zzhn.zzb(t, j2) != 0;
            case 6:
                return zzhn.zza(t, j2) != 0;
            case 7:
                return zzhn.zzc(t, j2);
            case 8:
                Object zzf = zzhn.zzf(t, j2);
                if (zzf instanceof String) {
                    return !((String) zzf).isEmpty();
                } else if (zzf instanceof zzdn) {
                    return !zzdn.zza.equals(zzf);
                } else {
                    throw new IllegalArgumentException();
                }
            case 9:
                return zzhn.zzf(t, j2) != null;
            case 10:
                return !zzdn.zza.equals(zzhn.zzf(t, j2));
            case 11:
                return zzhn.zza(t, j2) != 0;
            case 12:
                return zzhn.zza(t, j2) != 0;
            case 13:
                return zzhn.zza(t, j2) != 0;
            case 14:
                return zzhn.zzb(t, j2) != 0;
            case 15:
                return zzhn.zza(t, j2) != 0;
            case 16:
                return zzhn.zzb(t, j2) != 0;
            case 17:
                return zzhn.zzf(t, j2) != null;
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
        zzhn.zza((Object) t, j, (1 << (zzd >>> 20)) | zzhn.zza(t, j));
    }

    private final boolean zza(T t, int i, int i2) {
        return zzhn.zza(t, (long) (zzd(i2) & 1048575)) == i;
    }

    private final void zzb(T t, int i, int i2) {
        zzhn.zza((Object) t, zzd(i2) & 1048575, i);
    }
}
