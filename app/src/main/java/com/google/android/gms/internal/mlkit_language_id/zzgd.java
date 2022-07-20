package com.google.android.gms.internal.mlkit_language_id;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import sun.misc.Unsafe;
/* compiled from: com.google.mlkit:language-id@@16.1.1 */
/* loaded from: classes.dex */
final class zzgd<T> implements zzgp<T> {
    private static final int[] zza = new int[0];
    private static final Unsafe zzb = zzhn.zzc();
    private final int[] zzc;
    private final Object[] zzd;
    private final zzfz zzg;
    private final boolean zzh;
    private final boolean zzj;
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
        boolean z3 = zzfzVar instanceof zzeo;
        this.zzj = z;
        this.zzh = zzeeVar != null && zzeeVar.zza(zzfzVar);
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

    /* JADX WARN: Removed duplicated region for block: B:159:0x033a  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x039c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static <T> zzgd<T> zza(Class<T> cls, zzfx zzfxVar, zzge zzgeVar, zzfj zzfjVar, zzhh<?, ?> zzhhVar, zzee<?> zzeeVar, zzfs zzfsVar) {
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
        String str;
        Object[] objArr;
        int i12;
        int i13;
        int i14;
        int i15;
        boolean z;
        int i16;
        Field field;
        int i17;
        char charAt;
        int i18;
        int i19;
        Field field2;
        Field field3;
        int i20;
        char charAt2;
        int i21;
        char charAt3;
        int i22;
        char charAt4;
        int i23;
        char charAt5;
        int i24;
        char charAt6;
        int i25;
        char charAt7;
        int i26;
        char charAt8;
        int i27;
        char charAt9;
        int i28;
        char charAt10;
        int i29;
        char charAt11;
        int i30;
        char charAt12;
        int i31;
        char charAt13;
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
            int charAt14 = zzd.charAt(i);
            if (charAt14 >= 55296) {
                int i35 = charAt14 & 8191;
                int i36 = 13;
                while (true) {
                    i31 = i34 + 1;
                    charAt13 = zzd.charAt(i34);
                    if (charAt13 < 55296) {
                        break;
                    }
                    i35 |= (charAt13 & 8191) << i36;
                    i36 += 13;
                    i34 = i31;
                }
                charAt14 = i35 | (charAt13 << i36);
                i34 = i31;
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
                int i37 = i34 + 1;
                int charAt15 = zzd.charAt(i34);
                if (charAt15 >= 55296) {
                    int i38 = charAt15 & 8191;
                    int i39 = 13;
                    while (true) {
                        i30 = i37 + 1;
                        charAt12 = zzd.charAt(i37);
                        if (charAt12 < 55296) {
                            break;
                        }
                        i38 |= (charAt12 & 8191) << i39;
                        i39 += 13;
                        i37 = i30;
                    }
                    charAt15 = i38 | (charAt12 << i39);
                    i37 = i30;
                }
                int i40 = i37 + 1;
                int charAt16 = zzd.charAt(i37);
                if (charAt16 >= 55296) {
                    int i41 = charAt16 & 8191;
                    int i42 = 13;
                    while (true) {
                        i29 = i40 + 1;
                        charAt11 = zzd.charAt(i40);
                        if (charAt11 < 55296) {
                            break;
                        }
                        i41 |= (charAt11 & 8191) << i42;
                        i42 += 13;
                        i40 = i29;
                    }
                    charAt16 = i41 | (charAt11 << i42);
                    i40 = i29;
                }
                int i43 = i40 + 1;
                i6 = zzd.charAt(i40);
                if (i6 >= 55296) {
                    int i44 = i6 & 8191;
                    int i45 = 13;
                    while (true) {
                        i28 = i43 + 1;
                        charAt10 = zzd.charAt(i43);
                        if (charAt10 < 55296) {
                            break;
                        }
                        i44 |= (charAt10 & 8191) << i45;
                        i45 += 13;
                        i43 = i28;
                    }
                    i6 = i44 | (charAt10 << i45);
                    i43 = i28;
                }
                int i46 = i43 + 1;
                i5 = zzd.charAt(i43);
                if (i5 >= 55296) {
                    int i47 = i5 & 8191;
                    int i48 = 13;
                    while (true) {
                        i27 = i46 + 1;
                        charAt9 = zzd.charAt(i46);
                        if (charAt9 < 55296) {
                            break;
                        }
                        i47 |= (charAt9 & 8191) << i48;
                        i48 += 13;
                        i46 = i27;
                    }
                    i5 = i47 | (charAt9 << i48);
                    i46 = i27;
                }
                int i49 = i46 + 1;
                i4 = zzd.charAt(i46);
                if (i4 >= 55296) {
                    int i50 = i4 & 8191;
                    int i51 = 13;
                    while (true) {
                        i26 = i49 + 1;
                        charAt8 = zzd.charAt(i49);
                        if (charAt8 < 55296) {
                            break;
                        }
                        i50 |= (charAt8 & 8191) << i51;
                        i51 += 13;
                        i49 = i26;
                    }
                    i4 = i50 | (charAt8 << i51);
                    i49 = i26;
                }
                int i52 = i49 + 1;
                i3 = zzd.charAt(i49);
                if (i3 >= 55296) {
                    int i53 = i3 & 8191;
                    int i54 = 13;
                    while (true) {
                        i25 = i52 + 1;
                        charAt7 = zzd.charAt(i52);
                        if (charAt7 < 55296) {
                            break;
                        }
                        i53 |= (charAt7 & 8191) << i54;
                        i54 += 13;
                        i52 = i25;
                    }
                    i3 = i53 | (charAt7 << i54);
                    i52 = i25;
                }
                int i55 = i52 + 1;
                int charAt17 = zzd.charAt(i52);
                if (charAt17 >= 55296) {
                    int i56 = charAt17 & 8191;
                    int i57 = 13;
                    while (true) {
                        i24 = i55 + 1;
                        charAt6 = zzd.charAt(i55);
                        if (charAt6 < 55296) {
                            break;
                        }
                        i56 |= (charAt6 & 8191) << i57;
                        i57 += 13;
                        i55 = i24;
                    }
                    charAt17 = i56 | (charAt6 << i57);
                    i55 = i24;
                }
                int i58 = i55 + 1;
                i2 = zzd.charAt(i55);
                if (i2 >= 55296) {
                    int i59 = i2 & 8191;
                    int i60 = i58;
                    int i61 = 13;
                    while (true) {
                        i23 = i60 + 1;
                        charAt5 = zzd.charAt(i60);
                        if (charAt5 < 55296) {
                            break;
                        }
                        i59 |= (charAt5 & 8191) << i61;
                        i61 += 13;
                        i60 = i23;
                    }
                    i2 = i59 | (charAt5 << i61);
                    i58 = i23;
                }
                i7 = (charAt15 << 1) + charAt16;
                iArr = new int[i2 + i3 + charAt17];
                i32 = charAt15;
                i34 = i58;
            }
            Unsafe unsafe = zzb;
            Object[] zze = zzgmVar.zze();
            Class<?> cls2 = zzgmVar.zzc().getClass();
            int i62 = i34;
            int[] iArr2 = new int[i4 * 3];
            Object[] objArr2 = new Object[i4 << 1];
            int i63 = i2 + i3;
            int i64 = i7;
            int i65 = i2;
            int i66 = i62;
            int i67 = i63;
            int i68 = 0;
            int i69 = 0;
            while (i66 < length) {
                int i70 = i66 + 1;
                int charAt18 = zzd.charAt(i66);
                if (charAt18 >= 55296) {
                    int i71 = charAt18 & 8191;
                    int i72 = i70;
                    int i73 = 13;
                    while (true) {
                        i22 = i72 + 1;
                        charAt4 = zzd.charAt(i72);
                        i8 = length;
                        if (charAt4 < 55296) {
                            break;
                        }
                        i71 |= (charAt4 & 8191) << i73;
                        i73 += 13;
                        i72 = i22;
                        length = i8;
                    }
                    charAt18 = i71 | (charAt4 << i73);
                    i9 = i22;
                } else {
                    i8 = length;
                    i9 = i70;
                }
                int i74 = i9 + 1;
                int charAt19 = zzd.charAt(i9);
                if (charAt19 >= 55296) {
                    int i75 = charAt19 & 8191;
                    int i76 = i74;
                    int i77 = 13;
                    while (true) {
                        i21 = i76 + 1;
                        charAt3 = zzd.charAt(i76);
                        i10 = i2;
                        if (charAt3 < 55296) {
                            break;
                        }
                        i75 |= (charAt3 & 8191) << i77;
                        i77 += 13;
                        i76 = i21;
                        i2 = i10;
                    }
                    charAt19 = i75 | (charAt3 << i77);
                    i11 = i21;
                } else {
                    i10 = i2;
                    i11 = i74;
                }
                int i78 = charAt19 & 255;
                int i79 = i5;
                if ((charAt19 & 1024) != 0) {
                    iArr[i68] = i69;
                    i68++;
                }
                int i80 = i6;
                if (i78 >= 51) {
                    int i81 = i11 + 1;
                    int charAt20 = zzd.charAt(i11);
                    char c = 55296;
                    if (charAt20 >= 55296) {
                        int i82 = charAt20 & 8191;
                        int i83 = 13;
                        while (true) {
                            i20 = i81 + 1;
                            charAt2 = zzd.charAt(i81);
                            if (charAt2 < c) {
                                break;
                            }
                            i82 |= (charAt2 & 8191) << i83;
                            i83 += 13;
                            i81 = i20;
                            c = 55296;
                        }
                        charAt20 = i82 | (charAt2 << i83);
                        i81 = i20;
                    }
                    int i84 = i78 - 51;
                    int i85 = i81;
                    if (i84 == 9 || i84 == 17) {
                        i19 = 1;
                        objArr2[((i69 / 3) << 1) + 1] = zze[i64];
                        i64++;
                    } else {
                        if (i84 == 12 && !z2) {
                            objArr2[((i69 / 3) << 1) + 1] = zze[i64];
                            i64++;
                        }
                        i19 = 1;
                    }
                    int i86 = charAt20 << i19;
                    Object obj = zze[i86];
                    if (obj instanceof Field) {
                        field2 = (Field) obj;
                    } else {
                        field2 = zza(cls2, (String) obj);
                        zze[i86] = field2;
                    }
                    int objectFieldOffset = (int) unsafe.objectFieldOffset(field2);
                    int i87 = i86 + 1;
                    Object obj2 = zze[i87];
                    if (obj2 instanceof Field) {
                        field3 = (Field) obj2;
                    } else {
                        field3 = zza(cls2, (String) obj2);
                        zze[i87] = field3;
                    }
                    str = zzd;
                    i14 = (int) unsafe.objectFieldOffset(field3);
                    z = z2;
                    objArr = objArr2;
                    i13 = objectFieldOffset;
                    i12 = i85;
                    i15 = 0;
                } else {
                    int i88 = i64 + 1;
                    Field zza2 = zza(cls2, (String) zze[i64]);
                    if (i78 == 9 || i78 == 17) {
                        objArr2[((i69 / 3) << 1) + 1] = zza2.getType();
                    } else {
                        if (i78 == 27 || i78 == 49) {
                            i18 = i88 + 1;
                            objArr2[((i69 / 3) << 1) + 1] = zze[i88];
                        } else {
                            if (i78 == 12 || i78 == 30 || i78 == 44) {
                                if (!z2) {
                                    i18 = i88 + 1;
                                    objArr2[((i69 / 3) << 1) + 1] = zze[i88];
                                }
                            } else if (i78 == 50) {
                                int i89 = i65 + 1;
                                iArr[i65] = i69;
                                int i90 = (i69 / 3) << 1;
                                i18 = i88 + 1;
                                objArr2[i90] = zze[i88];
                                if ((charAt19 & 2048) != 0) {
                                    i88 = i18 + 1;
                                    objArr2[i90 + 1] = zze[i18];
                                    i65 = i89;
                                } else {
                                    i65 = i89;
                                }
                            }
                            i13 = (int) unsafe.objectFieldOffset(zza2);
                            int i91 = i16;
                            if ((charAt19 & 4096) == 4096 || i78 > 17) {
                                str = zzd;
                                z = z2;
                                objArr = objArr2;
                                i14 = 1048575;
                                i12 = i11;
                                i15 = 0;
                            } else {
                                int i92 = i11 + 1;
                                int charAt21 = zzd.charAt(i11);
                                if (charAt21 >= 55296) {
                                    int i93 = charAt21 & 8191;
                                    int i94 = 13;
                                    while (true) {
                                        i17 = i92 + 1;
                                        charAt = zzd.charAt(i92);
                                        if (charAt < 55296) {
                                            break;
                                        }
                                        i93 |= (charAt & 8191) << i94;
                                        i94 += 13;
                                        i92 = i17;
                                    }
                                    charAt21 = i93 | (charAt << i94);
                                    i92 = i17;
                                }
                                int i95 = (i32 << 1) + (charAt21 / 32);
                                Object obj3 = zze[i95];
                                str = zzd;
                                if (obj3 instanceof Field) {
                                    field = (Field) obj3;
                                } else {
                                    field = zza(cls2, (String) obj3);
                                    zze[i95] = field;
                                }
                                z = z2;
                                objArr = objArr2;
                                i15 = charAt21 % 32;
                                i12 = i92;
                                i14 = (int) unsafe.objectFieldOffset(field);
                            }
                            if (i78 >= 18 && i78 <= 49) {
                                iArr[i67] = i13;
                                i67++;
                            }
                            i64 = i91;
                        }
                        i16 = i18;
                        i13 = (int) unsafe.objectFieldOffset(zza2);
                        int i912 = i16;
                        if ((charAt19 & 4096) == 4096) {
                        }
                        str = zzd;
                        z = z2;
                        objArr = objArr2;
                        i14 = 1048575;
                        i12 = i11;
                        i15 = 0;
                        if (i78 >= 18) {
                            iArr[i67] = i13;
                            i67++;
                        }
                        i64 = i912;
                    }
                    i16 = i88;
                    i13 = (int) unsafe.objectFieldOffset(zza2);
                    int i9122 = i16;
                    if ((charAt19 & 4096) == 4096) {
                    }
                    str = zzd;
                    z = z2;
                    objArr = objArr2;
                    i14 = 1048575;
                    i12 = i11;
                    i15 = 0;
                    if (i78 >= 18) {
                    }
                    i64 = i9122;
                }
                int i96 = i69 + 1;
                iArr2[i69] = charAt18;
                int i97 = i96 + 1;
                int i98 = i32;
                iArr2[i96] = ((charAt19 & 256) != 0 ? 268435456 : 0) | ((charAt19 & 512) != 0 ? 536870912 : 0) | (i78 << 20) | i13;
                int i99 = i97 + 1;
                iArr2[i97] = (i15 << 20) | i14;
                i66 = i12;
                i32 = i98;
                i5 = i79;
                objArr2 = objArr;
                i2 = i10;
                i6 = i80;
                z2 = z;
                i69 = i99;
                length = i8;
                zzd = str;
            }
            return new zzgd<>(iArr2, objArr2, i6, i5, zzgmVar.zzc(), z2, false, iArr, i2, i63, zzgeVar, zzfjVar, zzhhVar, zzeeVar, zzfsVar);
        }
        ((zzha) zzfxVar).zza();
        int i100 = zzgl.zzb;
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

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0038, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzgr.zza(com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r10, r6), com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r11, r6)) != false) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x006a, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzgr.zza(com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r10, r6), com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r11, r6)) != false) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x007e, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r11, r6)) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0090, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r11, r6)) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00a4, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r11, r6)) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00b6, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r11, r6)) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00c8, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r11, r6)) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00da, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r11, r6)) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00f0, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzgr.zza(com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r10, r6), com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r11, r6)) != false) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0106, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzgr.zza(com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r10, r6), com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r11, r6)) != false) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x011c, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzgr.zza(com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r10, r6), com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r11, r6)) != false) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x012e, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zzc(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zzc(r11, r6)) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0140, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r11, r6)) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0154, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r11, r6)) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0165, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r11, r6)) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x0178, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r11, r6)) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x018b, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r11, r6)) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x01a4, code lost:
        if (java.lang.Float.floatToIntBits(com.google.android.gms.internal.mlkit_language_id.zzhn.zzd(r10, r6)) == java.lang.Float.floatToIntBits(com.google.android.gms.internal.mlkit_language_id.zzhn.zzd(r11, r6))) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x01bf, code lost:
        if (java.lang.Double.doubleToLongBits(com.google.android.gms.internal.mlkit_language_id.zzhn.zze(r10, r6)) == java.lang.Double.doubleToLongBits(com.google.android.gms.internal.mlkit_language_id.zzhn.zze(r11, r6))) goto L86;
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

    @Override // com.google.android.gms.internal.mlkit_language_id.zzgp
    public final int zza(T t) {
        int i;
        int i2;
        int length = this.zzc.length;
        int i3 = 0;
        for (int i4 = 0; i4 < length; i4 += 3) {
            int zzc = zzc(i4);
            int i5 = this.zzc[i4];
            long j = 1048575 & zzc;
            int i6 = 37;
            switch ((zzc & 267386880) >>> 20) {
                case 0:
                    i2 = i3 * 53;
                    i = zzeq.zza(Double.doubleToLongBits(zzhn.zze(t, j)));
                    i3 = i2 + i;
                    break;
                case 1:
                    i2 = i3 * 53;
                    i = Float.floatToIntBits(zzhn.zzd(t, j));
                    i3 = i2 + i;
                    break;
                case 2:
                    i2 = i3 * 53;
                    i = zzeq.zza(zzhn.zzb(t, j));
                    i3 = i2 + i;
                    break;
                case 3:
                    i2 = i3 * 53;
                    i = zzeq.zza(zzhn.zzb(t, j));
                    i3 = i2 + i;
                    break;
                case 4:
                    i2 = i3 * 53;
                    i = zzhn.zza(t, j);
                    i3 = i2 + i;
                    break;
                case 5:
                    i2 = i3 * 53;
                    i = zzeq.zza(zzhn.zzb(t, j));
                    i3 = i2 + i;
                    break;
                case 6:
                    i2 = i3 * 53;
                    i = zzhn.zza(t, j);
                    i3 = i2 + i;
                    break;
                case 7:
                    i2 = i3 * 53;
                    i = zzeq.zza(zzhn.zzc(t, j));
                    i3 = i2 + i;
                    break;
                case 8:
                    i2 = i3 * 53;
                    i = ((String) zzhn.zzf(t, j)).hashCode();
                    i3 = i2 + i;
                    break;
                case 9:
                    Object zzf = zzhn.zzf(t, j);
                    if (zzf != null) {
                        i6 = zzf.hashCode();
                    }
                    i3 = (i3 * 53) + i6;
                    break;
                case 10:
                    i2 = i3 * 53;
                    i = zzhn.zzf(t, j).hashCode();
                    i3 = i2 + i;
                    break;
                case 11:
                    i2 = i3 * 53;
                    i = zzhn.zza(t, j);
                    i3 = i2 + i;
                    break;
                case 12:
                    i2 = i3 * 53;
                    i = zzhn.zza(t, j);
                    i3 = i2 + i;
                    break;
                case 13:
                    i2 = i3 * 53;
                    i = zzhn.zza(t, j);
                    i3 = i2 + i;
                    break;
                case 14:
                    i2 = i3 * 53;
                    i = zzeq.zza(zzhn.zzb(t, j));
                    i3 = i2 + i;
                    break;
                case 15:
                    i2 = i3 * 53;
                    i = zzhn.zza(t, j);
                    i3 = i2 + i;
                    break;
                case 16:
                    i2 = i3 * 53;
                    i = zzeq.zza(zzhn.zzb(t, j));
                    i3 = i2 + i;
                    break;
                case 17:
                    Object zzf2 = zzhn.zzf(t, j);
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
                    i2 = i3 * 53;
                    i = zzhn.zzf(t, j).hashCode();
                    i3 = i2 + i;
                    break;
                case 50:
                    i2 = i3 * 53;
                    i = zzhn.zzf(t, j).hashCode();
                    i3 = i2 + i;
                    break;
                case 51:
                    if (zza((zzgd<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzeq.zza(Double.doubleToLongBits(zzb(t, j)));
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 52:
                    if (zza((zzgd<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = Float.floatToIntBits(zzc(t, j));
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 53:
                    if (zza((zzgd<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzeq.zza(zze(t, j));
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 54:
                    if (zza((zzgd<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzeq.zza(zze(t, j));
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 55:
                    if (zza((zzgd<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzd(t, j);
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 56:
                    if (zza((zzgd<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzeq.zza(zze(t, j));
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 57:
                    if (zza((zzgd<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzd(t, j);
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 58:
                    if (zza((zzgd<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzeq.zza(zzf(t, j));
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 59:
                    if (zza((zzgd<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = ((String) zzhn.zzf(t, j)).hashCode();
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 60:
                    if (zza((zzgd<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzhn.zzf(t, j).hashCode();
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 61:
                    if (zza((zzgd<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzhn.zzf(t, j).hashCode();
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 62:
                    if (zza((zzgd<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzd(t, j);
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 63:
                    if (zza((zzgd<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzd(t, j);
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 64:
                    if (zza((zzgd<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzd(t, j);
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 65:
                    if (zza((zzgd<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzeq.zza(zze(t, j));
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 66:
                    if (zza((zzgd<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzd(t, j);
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 67:
                    if (zza((zzgd<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzeq.zza(zze(t, j));
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 68:
                    if (zza((zzgd<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzhn.zzf(t, j).hashCode();
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
            }
        }
        int hashCode = (i3 * 53) + this.zzq.zza(t).hashCode();
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
        if (!zza((zzgd<T>) t2, i)) {
            return;
        }
        Object zzf = zzhn.zzf(t, zzc);
        Object zzf2 = zzhn.zzf(t2, zzc);
        if (zzf != null && zzf2 != null) {
            zzhn.zza(t, zzc, zzeq.zza(zzf, zzf2));
            zzb((zzgd<T>) t, i);
        } else if (zzf2 == null) {
        } else {
            zzhn.zza(t, zzc, zzf2);
            zzb((zzgd<T>) t, i);
        }
    }

    private final void zzb(T t, T t2, int i) {
        int zzc = zzc(i);
        int i2 = this.zzc[i];
        long j = zzc & 1048575;
        if (!zza((zzgd<T>) t2, i2, i)) {
            return;
        }
        Object zzf = zzhn.zzf(t, j);
        Object zzf2 = zzhn.zzf(t2, j);
        if (zzf != null && zzf2 != null) {
            zzhn.zza(t, j, zzeq.zza(zzf, zzf2));
            zzb((zzgd<T>) t, i2, i);
        } else if (zzf2 == null) {
        } else {
            zzhn.zza(t, j, zzf2);
            zzb((zzgd<T>) t, i2, i);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzgp
    public final int zzd(T t) {
        int i;
        long j;
        int i2;
        int zzb2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int zzb3;
        int i8;
        int i9;
        int i10;
        int i11 = 267386880;
        int i12 = 1048575;
        int i13 = 1;
        if (this.zzj) {
            Unsafe unsafe = zzb;
            int i14 = 0;
            int i15 = 0;
            while (i14 < this.zzc.length) {
                int zzc = zzc(i14);
                int i16 = (zzc & i11) >>> 20;
                int i17 = this.zzc[i14];
                long j2 = zzc & 1048575;
                if (i16 >= zzek.DOUBLE_LIST_PACKED.zza() && i16 <= zzek.SINT64_LIST_PACKED.zza()) {
                    int i18 = this.zzc[i14 + 2];
                }
                switch (i16) {
                    case 0:
                        if (zza((zzgd<T>) t, i14)) {
                            zzb3 = zzea.zzb(i17, 0.0d);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 1:
                        if (zza((zzgd<T>) t, i14)) {
                            zzb3 = zzea.zzb(i17, 0.0f);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 2:
                        if (zza((zzgd<T>) t, i14)) {
                            zzb3 = zzea.zzd(i17, zzhn.zzb(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 3:
                        if (zza((zzgd<T>) t, i14)) {
                            zzb3 = zzea.zze(i17, zzhn.zzb(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 4:
                        if (zza((zzgd<T>) t, i14)) {
                            zzb3 = zzea.zzf(i17, zzhn.zza(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 5:
                        if (zza((zzgd<T>) t, i14)) {
                            zzb3 = zzea.zzg(i17, 0L);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 6:
                        if (zza((zzgd<T>) t, i14)) {
                            zzb3 = zzea.zzi(i17, 0);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 7:
                        if (zza((zzgd<T>) t, i14)) {
                            zzb3 = zzea.zzb(i17, true);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 8:
                        if (zza((zzgd<T>) t, i14)) {
                            Object zzf = zzhn.zzf(t, j2);
                            if (zzf instanceof zzdn) {
                                zzb3 = zzea.zzc(i17, (zzdn) zzf);
                                break;
                            } else {
                                zzb3 = zzea.zzb(i17, (String) zzf);
                                break;
                            }
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 9:
                        if (zza((zzgd<T>) t, i14)) {
                            zzb3 = zzgr.zza(i17, zzhn.zzf(t, j2), zza(i14));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 10:
                        if (zza((zzgd<T>) t, i14)) {
                            zzb3 = zzea.zzc(i17, (zzdn) zzhn.zzf(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 11:
                        if (zza((zzgd<T>) t, i14)) {
                            zzb3 = zzea.zzg(i17, zzhn.zza(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 12:
                        if (zza((zzgd<T>) t, i14)) {
                            zzb3 = zzea.zzk(i17, zzhn.zza(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 13:
                        if (zza((zzgd<T>) t, i14)) {
                            zzb3 = zzea.zzj(i17, 0);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 14:
                        if (zza((zzgd<T>) t, i14)) {
                            zzb3 = zzea.zzh(i17, 0L);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 15:
                        if (zza((zzgd<T>) t, i14)) {
                            zzb3 = zzea.zzh(i17, zzhn.zza(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 16:
                        if (zza((zzgd<T>) t, i14)) {
                            zzb3 = zzea.zzf(i17, zzhn.zzb(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 17:
                        if (zza((zzgd<T>) t, i14)) {
                            zzb3 = zzea.zzc(i17, (zzfz) zzhn.zzf(t, j2), zza(i14));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 18:
                        zzb3 = zzgr.zzi(i17, zza(t, j2), false);
                        break;
                    case 19:
                        zzb3 = zzgr.zzh(i17, zza(t, j2), false);
                        break;
                    case 20:
                        zzb3 = zzgr.zza(i17, (List<Long>) zza(t, j2), false);
                        break;
                    case 21:
                        zzb3 = zzgr.zzb(i17, (List<Long>) zza(t, j2), false);
                        break;
                    case 22:
                        zzb3 = zzgr.zze(i17, zza(t, j2), false);
                        break;
                    case 23:
                        zzb3 = zzgr.zzi(i17, zza(t, j2), false);
                        break;
                    case 24:
                        zzb3 = zzgr.zzh(i17, zza(t, j2), false);
                        break;
                    case 25:
                        zzb3 = zzgr.zzj(i17, zza(t, j2), false);
                        break;
                    case 26:
                        zzb3 = zzgr.zza(i17, zza(t, j2));
                        break;
                    case 27:
                        zzb3 = zzgr.zza(i17, zza(t, j2), zza(i14));
                        break;
                    case 28:
                        zzb3 = zzgr.zzb(i17, zza(t, j2));
                        break;
                    case 29:
                        zzb3 = zzgr.zzf(i17, zza(t, j2), false);
                        break;
                    case 30:
                        zzb3 = zzgr.zzd(i17, zza(t, j2), false);
                        break;
                    case 31:
                        zzb3 = zzgr.zzh(i17, zza(t, j2), false);
                        break;
                    case 32:
                        zzb3 = zzgr.zzi(i17, zza(t, j2), false);
                        break;
                    case 33:
                        zzb3 = zzgr.zzg(i17, zza(t, j2), false);
                        break;
                    case 34:
                        zzb3 = zzgr.zzc(i17, zza(t, j2), false);
                        break;
                    case 35:
                        i9 = zzgr.zzi((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzea.zze(i17);
                            i8 = zzea.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 36:
                        i9 = zzgr.zzh((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzea.zze(i17);
                            i8 = zzea.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 37:
                        i9 = zzgr.zza((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzea.zze(i17);
                            i8 = zzea.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 38:
                        i9 = zzgr.zzb((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzea.zze(i17);
                            i8 = zzea.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 39:
                        i9 = zzgr.zze((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzea.zze(i17);
                            i8 = zzea.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 40:
                        i9 = zzgr.zzi((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzea.zze(i17);
                            i8 = zzea.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 41:
                        i9 = zzgr.zzh((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzea.zze(i17);
                            i8 = zzea.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 42:
                        i9 = zzgr.zzj((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzea.zze(i17);
                            i8 = zzea.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 43:
                        i9 = zzgr.zzf((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzea.zze(i17);
                            i8 = zzea.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 44:
                        i9 = zzgr.zzd((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzea.zze(i17);
                            i8 = zzea.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 45:
                        i9 = zzgr.zzh((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzea.zze(i17);
                            i8 = zzea.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 46:
                        i9 = zzgr.zzi((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzea.zze(i17);
                            i8 = zzea.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 47:
                        i9 = zzgr.zzg((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzea.zze(i17);
                            i8 = zzea.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 48:
                        i9 = zzgr.zzc((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzea.zze(i17);
                            i8 = zzea.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 49:
                        zzb3 = zzgr.zzb(i17, (List<zzfz>) zza(t, j2), zza(i14));
                        break;
                    case 50:
                        zzb3 = this.zzs.zza(i17, zzhn.zzf(t, j2), zzb(i14));
                        break;
                    case 51:
                        if (zza((zzgd<T>) t, i17, i14)) {
                            zzb3 = zzea.zzb(i17, 0.0d);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 52:
                        if (zza((zzgd<T>) t, i17, i14)) {
                            zzb3 = zzea.zzb(i17, 0.0f);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 53:
                        if (zza((zzgd<T>) t, i17, i14)) {
                            zzb3 = zzea.zzd(i17, zze(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 54:
                        if (zza((zzgd<T>) t, i17, i14)) {
                            zzb3 = zzea.zze(i17, zze(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 55:
                        if (zza((zzgd<T>) t, i17, i14)) {
                            zzb3 = zzea.zzf(i17, zzd(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 56:
                        if (zza((zzgd<T>) t, i17, i14)) {
                            zzb3 = zzea.zzg(i17, 0L);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 57:
                        if (zza((zzgd<T>) t, i17, i14)) {
                            zzb3 = zzea.zzi(i17, 0);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 58:
                        if (zza((zzgd<T>) t, i17, i14)) {
                            zzb3 = zzea.zzb(i17, true);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 59:
                        if (zza((zzgd<T>) t, i17, i14)) {
                            Object zzf2 = zzhn.zzf(t, j2);
                            if (zzf2 instanceof zzdn) {
                                zzb3 = zzea.zzc(i17, (zzdn) zzf2);
                                break;
                            } else {
                                zzb3 = zzea.zzb(i17, (String) zzf2);
                                break;
                            }
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 60:
                        if (zza((zzgd<T>) t, i17, i14)) {
                            zzb3 = zzgr.zza(i17, zzhn.zzf(t, j2), zza(i14));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 61:
                        if (zza((zzgd<T>) t, i17, i14)) {
                            zzb3 = zzea.zzc(i17, (zzdn) zzhn.zzf(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 62:
                        if (zza((zzgd<T>) t, i17, i14)) {
                            zzb3 = zzea.zzg(i17, zzd(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 63:
                        if (zza((zzgd<T>) t, i17, i14)) {
                            zzb3 = zzea.zzk(i17, zzd(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 64:
                        if (zza((zzgd<T>) t, i17, i14)) {
                            zzb3 = zzea.zzj(i17, 0);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 65:
                        if (zza((zzgd<T>) t, i17, i14)) {
                            zzb3 = zzea.zzh(i17, 0L);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 66:
                        if (zza((zzgd<T>) t, i17, i14)) {
                            zzb3 = zzea.zzh(i17, zzd(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 67:
                        if (zza((zzgd<T>) t, i17, i14)) {
                            zzb3 = zzea.zzf(i17, zze(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 68:
                        if (zza((zzgd<T>) t, i17, i14)) {
                            zzb3 = zzea.zzc(i17, (zzfz) zzhn.zzf(t, j2), zza(i14));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    default:
                        i14 += 3;
                        i11 = 267386880;
                }
                i15 += zzb3;
                i14 += 3;
                i11 = 267386880;
            }
            return i15 + zza((zzhh) this.zzq, (Object) t);
        }
        Unsafe unsafe2 = zzb;
        int i19 = 0;
        int i20 = 0;
        int i21 = 1048575;
        int i22 = 0;
        while (i19 < this.zzc.length) {
            int zzc2 = zzc(i19);
            int[] iArr = this.zzc;
            int i23 = iArr[i19];
            int i24 = (zzc2 & 267386880) >>> 20;
            if (i24 <= 17) {
                int i25 = iArr[i19 + 2];
                int i26 = i25 & i12;
                i = i13 << (i25 >>> 20);
                if (i26 != i21) {
                    i22 = unsafe2.getInt(t, i26);
                    i21 = i26;
                }
            } else {
                i = 0;
            }
            long j3 = zzc2 & i12;
            switch (i24) {
                case 0:
                    j = 0;
                    if ((i22 & i) != 0) {
                        i20 += zzea.zzb(i23, 0.0d);
                        continue;
                        i19 += 3;
                        i12 = 1048575;
                        i13 = 1;
                    }
                    break;
                case 1:
                    j = 0;
                    if ((i22 & i) != 0) {
                        i20 += zzea.zzb(i23, 0.0f);
                        break;
                    }
                    break;
                case 2:
                    j = 0;
                    if ((i & i22) != 0) {
                        i2 = zzea.zzd(i23, unsafe2.getLong(t, j3));
                        i20 += i2;
                        break;
                    }
                    break;
                case 3:
                    j = 0;
                    if ((i & i22) != 0) {
                        i2 = zzea.zze(i23, unsafe2.getLong(t, j3));
                        i20 += i2;
                        break;
                    }
                    break;
                case 4:
                    j = 0;
                    if ((i & i22) != 0) {
                        i2 = zzea.zzf(i23, unsafe2.getInt(t, j3));
                        i20 += i2;
                        break;
                    }
                    break;
                case 5:
                    j = 0;
                    if ((i22 & i) != 0) {
                        i2 = zzea.zzg(i23, 0L);
                        i20 += i2;
                        break;
                    }
                    break;
                case 6:
                    if ((i22 & i) != 0) {
                        i20 += zzea.zzi(i23, 0);
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 7:
                    if ((i22 & i) != 0) {
                        i20 += zzea.zzb(i23, true);
                        j = 0;
                        i19 += 3;
                        i12 = 1048575;
                        i13 = 1;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 8:
                    if ((i22 & i) != 0) {
                        Object object = unsafe2.getObject(t, j3);
                        if (object instanceof zzdn) {
                            zzb2 = zzea.zzc(i23, (zzdn) object);
                        } else {
                            zzb2 = zzea.zzb(i23, (String) object);
                        }
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 9:
                    if ((i22 & i) != 0) {
                        zzb2 = zzgr.zza(i23, unsafe2.getObject(t, j3), zza(i19));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 10:
                    if ((i22 & i) != 0) {
                        zzb2 = zzea.zzc(i23, (zzdn) unsafe2.getObject(t, j3));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 11:
                    if ((i22 & i) != 0) {
                        zzb2 = zzea.zzg(i23, unsafe2.getInt(t, j3));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 12:
                    if ((i22 & i) != 0) {
                        zzb2 = zzea.zzk(i23, unsafe2.getInt(t, j3));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 13:
                    if ((i22 & i) != 0) {
                        i3 = zzea.zzj(i23, 0);
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 14:
                    if ((i22 & i) != 0) {
                        zzb2 = zzea.zzh(i23, 0L);
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 15:
                    if ((i22 & i) != 0) {
                        zzb2 = zzea.zzh(i23, unsafe2.getInt(t, j3));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 16:
                    if ((i22 & i) != 0) {
                        zzb2 = zzea.zzf(i23, unsafe2.getLong(t, j3));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 17:
                    if ((i22 & i) != 0) {
                        zzb2 = zzea.zzc(i23, (zzfz) unsafe2.getObject(t, j3), zza(i19));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 18:
                    zzb2 = zzgr.zzi(i23, (List) unsafe2.getObject(t, j3), false);
                    i20 += zzb2;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 19:
                    i4 = zzgr.zzh(i23, (List) unsafe2.getObject(t, j3), false);
                    i20 += i4;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 20:
                    i4 = zzgr.zza(i23, (List<Long>) unsafe2.getObject(t, j3), false);
                    i20 += i4;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 21:
                    i4 = zzgr.zzb(i23, (List<Long>) unsafe2.getObject(t, j3), false);
                    i20 += i4;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 22:
                    i4 = zzgr.zze(i23, (List) unsafe2.getObject(t, j3), false);
                    i20 += i4;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 23:
                    i4 = zzgr.zzi(i23, (List) unsafe2.getObject(t, j3), false);
                    i20 += i4;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 24:
                    i4 = zzgr.zzh(i23, (List) unsafe2.getObject(t, j3), false);
                    i20 += i4;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 25:
                    i4 = zzgr.zzj(i23, (List) unsafe2.getObject(t, j3), false);
                    i20 += i4;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 26:
                    zzb2 = zzgr.zza(i23, (List) unsafe2.getObject(t, j3));
                    i20 += zzb2;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 27:
                    zzb2 = zzgr.zza(i23, (List<?>) unsafe2.getObject(t, j3), zza(i19));
                    i20 += zzb2;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 28:
                    zzb2 = zzgr.zzb(i23, (List) unsafe2.getObject(t, j3));
                    i20 += zzb2;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 29:
                    zzb2 = zzgr.zzf(i23, (List) unsafe2.getObject(t, j3), false);
                    i20 += zzb2;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 30:
                    i4 = zzgr.zzd(i23, (List) unsafe2.getObject(t, j3), false);
                    i20 += i4;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 31:
                    i4 = zzgr.zzh(i23, (List) unsafe2.getObject(t, j3), false);
                    i20 += i4;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 32:
                    i4 = zzgr.zzi(i23, (List) unsafe2.getObject(t, j3), false);
                    i20 += i4;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 33:
                    i4 = zzgr.zzg(i23, (List) unsafe2.getObject(t, j3), false);
                    i20 += i4;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 34:
                    i4 = zzgr.zzc(i23, (List) unsafe2.getObject(t, j3), false);
                    i20 += i4;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 35:
                    i7 = zzgr.zzi((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzea.zze(i23);
                        i5 = zzea.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 36:
                    i7 = zzgr.zzh((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzea.zze(i23);
                        i5 = zzea.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 37:
                    i7 = zzgr.zza((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzea.zze(i23);
                        i5 = zzea.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 38:
                    i7 = zzgr.zzb((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzea.zze(i23);
                        i5 = zzea.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 39:
                    i7 = zzgr.zze((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzea.zze(i23);
                        i5 = zzea.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 40:
                    i7 = zzgr.zzi((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzea.zze(i23);
                        i5 = zzea.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 41:
                    i7 = zzgr.zzh((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzea.zze(i23);
                        i5 = zzea.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 42:
                    i7 = zzgr.zzj((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzea.zze(i23);
                        i5 = zzea.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 43:
                    i7 = zzgr.zzf((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzea.zze(i23);
                        i5 = zzea.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 44:
                    i7 = zzgr.zzd((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzea.zze(i23);
                        i5 = zzea.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 45:
                    i7 = zzgr.zzh((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzea.zze(i23);
                        i5 = zzea.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 46:
                    i7 = zzgr.zzi((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzea.zze(i23);
                        i5 = zzea.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 47:
                    i7 = zzgr.zzg((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzea.zze(i23);
                        i5 = zzea.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 48:
                    i7 = zzgr.zzc((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzea.zze(i23);
                        i5 = zzea.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 49:
                    zzb2 = zzgr.zzb(i23, (List) unsafe2.getObject(t, j3), zza(i19));
                    i20 += zzb2;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 50:
                    zzb2 = this.zzs.zza(i23, unsafe2.getObject(t, j3), zzb(i19));
                    i20 += zzb2;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 51:
                    if (zza((zzgd<T>) t, i23, i19)) {
                        zzb2 = zzea.zzb(i23, 0.0d);
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 52:
                    if (zza((zzgd<T>) t, i23, i19)) {
                        i3 = zzea.zzb(i23, 0.0f);
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 53:
                    if (zza((zzgd<T>) t, i23, i19)) {
                        zzb2 = zzea.zzd(i23, zze(t, j3));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 54:
                    if (zza((zzgd<T>) t, i23, i19)) {
                        zzb2 = zzea.zze(i23, zze(t, j3));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 55:
                    if (zza((zzgd<T>) t, i23, i19)) {
                        zzb2 = zzea.zzf(i23, zzd(t, j3));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 56:
                    if (zza((zzgd<T>) t, i23, i19)) {
                        zzb2 = zzea.zzg(i23, 0L);
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 57:
                    if (zza((zzgd<T>) t, i23, i19)) {
                        i3 = zzea.zzi(i23, 0);
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 58:
                    if (zza((zzgd<T>) t, i23, i19)) {
                        i3 = zzea.zzb(i23, true);
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 59:
                    if (zza((zzgd<T>) t, i23, i19)) {
                        Object object2 = unsafe2.getObject(t, j3);
                        if (object2 instanceof zzdn) {
                            zzb2 = zzea.zzc(i23, (zzdn) object2);
                        } else {
                            zzb2 = zzea.zzb(i23, (String) object2);
                        }
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 60:
                    if (zza((zzgd<T>) t, i23, i19)) {
                        zzb2 = zzgr.zza(i23, unsafe2.getObject(t, j3), zza(i19));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 61:
                    if (zza((zzgd<T>) t, i23, i19)) {
                        zzb2 = zzea.zzc(i23, (zzdn) unsafe2.getObject(t, j3));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 62:
                    if (zza((zzgd<T>) t, i23, i19)) {
                        zzb2 = zzea.zzg(i23, zzd(t, j3));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 63:
                    if (zza((zzgd<T>) t, i23, i19)) {
                        zzb2 = zzea.zzk(i23, zzd(t, j3));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 64:
                    if (zza((zzgd<T>) t, i23, i19)) {
                        i3 = zzea.zzj(i23, 0);
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 65:
                    if (zza((zzgd<T>) t, i23, i19)) {
                        zzb2 = zzea.zzh(i23, 0L);
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 66:
                    if (zza((zzgd<T>) t, i23, i19)) {
                        zzb2 = zzea.zzh(i23, zzd(t, j3));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 67:
                    if (zza((zzgd<T>) t, i23, i19)) {
                        zzb2 = zzea.zzf(i23, zze(t, j3));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 68:
                    if (zza((zzgd<T>) t, i23, i19)) {
                        zzb2 = zzea.zzc(i23, (zzfz) unsafe2.getObject(t, j3), zza(i19));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                default:
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
            }
            i19 += 3;
            i12 = 1048575;
            i13 = 1;
        }
        int i27 = 0;
        int zza2 = i20 + zza((zzhh) this.zzq, (Object) t);
        if (!this.zzh) {
            return zza2;
        }
        zzej<?> zza3 = this.zzr.zza(t);
        for (int i28 = 0; i28 < zza3.zza.zzc(); i28++) {
            Map.Entry<?, Object> zzb4 = zza3.zza.zzb(i28);
            i27 += zzej.zza((zzel) zzb4.getKey(), zzb4.getValue());
        }
        for (Map.Entry<?, Object> entry : zza3.zza.zzd()) {
            i27 += zzej.zza((zzel) entry.getKey(), entry.getValue());
        }
        return zza2 + i27;
    }

    private static <UT, UB> int zza(zzhh<UT, UB> zzhhVar, T t) {
        return zzhhVar.zzd(zzhhVar.zza(t));
    }

    private static List<?> zza(Object obj, long j) {
        return (List) zzhn.zzf(obj, j);
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x003b  */
    /* JADX WARN: Removed duplicated region for block: B:165:0x0513  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x0552  */
    /* JADX WARN: Removed duplicated region for block: B:333:0x0a2a  */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzgp
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void zza(T t, zzib zzibVar) throws IOException {
        Map.Entry<?, Object> entry;
        Iterator<Map.Entry<?, Object>> it;
        int length;
        int i;
        Map.Entry<?, Object> entry2;
        Iterator<Map.Entry<?, Object>> it2;
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
        Map.Entry<?, Object> entry;
        Iterator<Map.Entry<?, Object>> it;
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
            zzibVar.zza(i, this.zzs.zzc(zzb(i2)), this.zzs.zza(obj));
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
            if (i9 != i3) {
                if (i9 != 1048575) {
                    i4 = zzb.getInt(t, i9);
                }
                i = i4;
                i2 = i9;
            } else {
                i2 = i3;
                i = i4;
            }
            if (((268435456 & zzc) != 0) && !zza(t, i6, i2, i, i10)) {
                return false;
            }
            int i11 = (267386880 & zzc) >>> 20;
            if (i11 == 9 || i11 == 17) {
                if (zza(t, i6, i2, i, i10) && !zza(t, zzc, zza(i6))) {
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
                if (!list.isEmpty()) {
                    zzgp zza2 = zza(i6);
                    int i12 = 0;
                    while (true) {
                        if (i12 >= list.size()) {
                            break;
                        } else if (!zza2.zzc(list.get(i12))) {
                            z = false;
                            break;
                        } else {
                            i12++;
                        }
                    }
                }
                if (!z) {
                    return false;
                }
            }
            i5++;
            i3 = i2;
            i4 = i;
        }
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
                } else if (!(zzf instanceof zzdn)) {
                    throw new IllegalArgumentException();
                } else {
                    return !zzdn.zza.equals(zzf);
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
