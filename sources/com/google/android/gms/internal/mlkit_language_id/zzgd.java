package com.google.android.gms.internal.mlkit_language_id;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.telegram.messenger.CharacterCompat;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.R;
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

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:163:0x033a  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x039c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static <T> zzgd<T> zza(Class<T> cls, zzfx zzfxVar, zzge zzgeVar, zzfj zzfjVar, zzhh<?, ?> zzhhVar, zzee<?> zzeeVar, zzfs zzfsVar) {
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
        int i15;
        int objectFieldOffset;
        String str;
        boolean z;
        Object[] objArr;
        int i16;
        int i17;
        int i18;
        Field zza2;
        int i19;
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
        if (zzfxVar instanceof zzgm) {
            zzgm zzgmVar = (zzgm) zzfxVar;
            int i26 = 0;
            boolean z2 = zzgmVar.zza() == zzgl.zzb;
            String zzd = zzgmVar.zzd();
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
            Object[] zze = zzgmVar.zze();
            Class<?> cls2 = zzgmVar.zzc().getClass();
            int i56 = i28;
            int[] iArr2 = new int[charAt3 * 3];
            Object[] objArr2 = new Object[charAt3 << 1];
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
                int i72 = charAt24 & 255;
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
                        objArr2[((i63 / 3) << 1) + 1] = zze[i58];
                        i58++;
                    } else {
                        if (i78 == 12 && !z2) {
                            objArr2[((i63 / 3) << 1) + 1] = zze[i58];
                            i58++;
                        }
                        i21 = 1;
                    }
                    int i80 = charAt25 << i21;
                    Object obj = zze[i80];
                    if (obj instanceof Field) {
                        zza3 = (Field) obj;
                    } else {
                        zza3 = zza(cls2, (String) obj);
                        zze[i80] = zza3;
                    }
                    int objectFieldOffset2 = (int) unsafe.objectFieldOffset(zza3);
                    int i81 = i80 + 1;
                    Object obj2 = zze[i81];
                    if (obj2 instanceof Field) {
                        zza4 = (Field) obj2;
                    } else {
                        zza4 = zza(cls2, (String) obj2);
                        zze[i81] = zza4;
                    }
                    str = zzd;
                    i16 = (int) unsafe.objectFieldOffset(zza4);
                    z = z2;
                    objArr = objArr2;
                    objectFieldOffset = objectFieldOffset2;
                    i17 = i79;
                    i18 = 0;
                } else {
                    int i82 = i58 + 1;
                    Field zza5 = zza(cls2, (String) zze[i58]);
                    if (i72 == 9 || i72 == 17) {
                        objArr2[((i63 / 3) << 1) + 1] = zza5.getType();
                    } else {
                        if (i72 == 27 || i72 == 49) {
                            i20 = i82 + 1;
                            objArr2[((i63 / 3) << 1) + 1] = zze[i82];
                        } else {
                            if (i72 == 12 || i72 == 30 || i72 == 44) {
                                if (!z2) {
                                    i20 = i82 + 1;
                                    objArr2[((i63 / 3) << 1) + 1] = zze[i82];
                                }
                            } else if (i72 == 50) {
                                int i83 = i59 + 1;
                                iArr[i59] = i63;
                                int i84 = (i63 / 3) << 1;
                                i20 = i82 + 1;
                                objArr2[i84] = zze[i82];
                                if ((charAt24 & 2048) != 0) {
                                    i82 = i20 + 1;
                                    objArr2[i84 + 1] = zze[i20];
                                    i59 = i83;
                                } else {
                                    i59 = i83;
                                }
                            }
                            objectFieldOffset = (int) unsafe.objectFieldOffset(zza5);
                            int i85 = i15;
                            if ((charAt24 & LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM) == 4096 || i72 > 17) {
                                str = zzd;
                                z = z2;
                                objArr = objArr2;
                                i16 = 1048575;
                                i17 = i14;
                                i18 = 0;
                            } else {
                                int i86 = i14 + 1;
                                int charAt26 = zzd.charAt(i14);
                                if (charAt26 >= 55296) {
                                    int i87 = charAt26 & 8191;
                                    int i88 = 13;
                                    while (true) {
                                        i19 = i86 + 1;
                                        charAt14 = zzd.charAt(i86);
                                        if (charAt14 < 55296) {
                                            break;
                                        }
                                        i87 |= (charAt14 & 8191) << i88;
                                        i88 += 13;
                                        i86 = i19;
                                    }
                                    charAt26 = i87 | (charAt14 << i88);
                                    i86 = i19;
                                }
                                int i89 = (i26 << 1) + (charAt26 / 32);
                                Object obj3 = zze[i89];
                                str = zzd;
                                if (obj3 instanceof Field) {
                                    zza2 = (Field) obj3;
                                } else {
                                    zza2 = zza(cls2, (String) obj3);
                                    zze[i89] = zza2;
                                }
                                z = z2;
                                objArr = objArr2;
                                i18 = charAt26 % 32;
                                i17 = i86;
                                i16 = (int) unsafe.objectFieldOffset(zza2);
                            }
                            if (i72 >= 18 && i72 <= 49) {
                                iArr[i61] = objectFieldOffset;
                                i61++;
                            }
                            i58 = i85;
                        }
                        i15 = i20;
                        objectFieldOffset = (int) unsafe.objectFieldOffset(zza5);
                        int i852 = i15;
                        if ((charAt24 & LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM) == 4096) {
                        }
                        str = zzd;
                        z = z2;
                        objArr = objArr2;
                        i16 = 1048575;
                        i17 = i14;
                        i18 = 0;
                        if (i72 >= 18) {
                            iArr[i61] = objectFieldOffset;
                            i61++;
                        }
                        i58 = i852;
                    }
                    i15 = i82;
                    objectFieldOffset = (int) unsafe.objectFieldOffset(zza5);
                    int i8522 = i15;
                    if ((charAt24 & LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM) == 4096) {
                    }
                    str = zzd;
                    z = z2;
                    objArr = objArr2;
                    i16 = 1048575;
                    i17 = i14;
                    i18 = 0;
                    if (i72 >= 18) {
                    }
                    i58 = i8522;
                }
                int i90 = i63 + 1;
                iArr2[i63] = charAt23;
                int i91 = i90 + 1;
                int i92 = i26;
                iArr2[i90] = ((charAt24 & LiteMode.FLAG_CHAT_BLUR) != 0 ? 268435456 : 0) | ((charAt24 & LiteMode.FLAG_CALLS_ANIMATIONS) != 0 ? 536870912 : 0) | (i72 << 20) | objectFieldOffset;
                int i93 = i91 + 1;
                iArr2[i91] = (i18 << 20) | i16;
                i60 = i17;
                i26 = i92;
                charAt2 = i73;
                objArr2 = objArr;
                charAt5 = i13;
                charAt = i74;
                z2 = z;
                i63 = i93;
                length = i11;
                zzd = str;
            }
            return new zzgd<>(iArr2, objArr2, charAt, charAt2, zzgmVar.zzc(), z2, false, iArr, charAt5, i57, zzgeVar, zzfjVar, zzhhVar, zzeeVar, zzfsVar);
        }
        ((zzha) zzfxVar).zza();
        int i94 = zzgl.zzb;
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

    /* JADX WARN: Code restructure failed: missing block: B:103:0x01bf, code lost:
        if (java.lang.Double.doubleToLongBits(com.google.android.gms.internal.mlkit_language_id.zzhn.zze(r10, r6)) == java.lang.Double.doubleToLongBits(com.google.android.gms.internal.mlkit_language_id.zzhn.zze(r11, r6))) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0038, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzgr.zza(com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r10, r6), com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r11, r6)) != false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x006a, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzgr.zza(com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r10, r6), com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r11, r6)) != false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x007e, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0090, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00a4, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00b6, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00c8, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00da, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00f0, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzgr.zza(com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r10, r6), com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r11, r6)) != false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0106, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzgr.zza(com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r10, r6), com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r11, r6)) != false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x011c, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzgr.zza(com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r10, r6), com.google.android.gms.internal.mlkit_language_id.zzhn.zzf(r11, r6)) != false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x012e, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zzc(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zzc(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0140, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x0154, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x0165, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zza(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x0178, code lost:
        if (com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r10, r6) == com.google.android.gms.internal.mlkit_language_id.zzhn.zzb(r11, r6)) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x018b, code lost:
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
                        z = zzgr.zza(zzhn.zzf(t, j), zzhn.zzf(t2, j));
                        break;
                    case R.styleable.AppCompatTheme_colorAccent /* 50 */:
                        z = zzgr.zza(zzhn.zzf(t, j), zzhn.zzf(t2, j));
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
                    i = i2 * 53;
                    zza2 = zzhn.zzf(t, j).hashCode();
                    i2 = i + zza2;
                    break;
                case R.styleable.AppCompatTheme_colorAccent /* 50 */:
                    i = i2 * 53;
                    zza2 = zzhn.zzf(t, j).hashCode();
                    i2 = i + zza2;
                    break;
                case R.styleable.AppCompatTheme_colorBackgroundFloating /* 51 */:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzeq.zza(Double.doubleToLongBits(zzb(t, j)));
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_colorButtonNormal /* 52 */:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = Float.floatToIntBits(zzc(t, j));
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_colorControlActivated /* 53 */:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzeq.zza(zze(t, j));
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_colorControlHighlight /* 54 */:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzeq.zza(zze(t, j));
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_colorControlNormal /* 55 */:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzd(t, j);
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_colorError /* 56 */:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzeq.zza(zze(t, j));
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_colorPrimary /* 57 */:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzd(t, j);
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_colorPrimaryDark /* 58 */:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzeq.zza(zzf(t, j));
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_colorSwitchThumbNormal /* 59 */:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = ((String) zzhn.zzf(t, j)).hashCode();
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_controlBackground /* 60 */:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzhn.zzf(t, j).hashCode();
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_dialogCornerRadius /* 61 */:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzhn.zzf(t, j).hashCode();
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_dialogPreferredPadding /* 62 */:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzd(t, j);
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_dialogTheme /* 63 */:
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
                case R.styleable.AppCompatTheme_dropDownListViewStyle /* 66 */:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzd(t, j);
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_dropdownListPreferredItemHeight /* 67 */:
                    if (zza((zzgd<T>) t, i4, i3)) {
                        i = i2 * 53;
                        zza2 = zzeq.zza(zze(t, j));
                        i2 = i + zza2;
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_editTextBackground /* 68 */:
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
        Objects.requireNonNull(t2);
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
                    this.zzp.zza(t, t2, j);
                    break;
                case R.styleable.AppCompatTheme_colorAccent /* 50 */:
                    zzgr.zza(this.zzs, t, t2, j);
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
                    if (zza((zzgd<T>) t2, i2, i)) {
                        zzhn.zza(t, j, zzhn.zzf(t2, j));
                        zzb((zzgd<T>) t, i2, i);
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_controlBackground /* 60 */:
                    zzb(t, t2, i);
                    break;
                case R.styleable.AppCompatTheme_dialogCornerRadius /* 61 */:
                case R.styleable.AppCompatTheme_dialogPreferredPadding /* 62 */:
                case R.styleable.AppCompatTheme_dialogTheme /* 63 */:
                case 64:
                case 65:
                case R.styleable.AppCompatTheme_dropDownListViewStyle /* 66 */:
                case R.styleable.AppCompatTheme_dropdownListPreferredItemHeight /* 67 */:
                    if (zza((zzgd<T>) t2, i2, i)) {
                        zzhn.zza(t, j, zzhn.zzf(t2, j));
                        zzb((zzgd<T>) t, i2, i);
                        break;
                    } else {
                        break;
                    }
                case R.styleable.AppCompatTheme_editTextBackground /* 68 */:
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
        int zzj;
        int zzh;
        int zzi;
        int zze;
        int zzg;
        int zzb3;
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
                            zzb3 = zzea.zzb(i8, 0.0d);
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 1:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb3 = zzea.zzb(i8, 0.0f);
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 2:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb3 = zzea.zzd(i8, zzhn.zzb(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 3:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb3 = zzea.zze(i8, zzhn.zzb(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 4:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb3 = zzea.zzf(i8, zzhn.zza(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 5:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb3 = zzea.zzg(i8, 0L);
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 6:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb3 = zzea.zzi(i8, 0);
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 7:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb3 = zzea.zzb(i8, true);
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
                                zzb3 = zzea.zzc(i8, (zzdn) zzf);
                                break;
                            } else {
                                zzb3 = zzea.zzb(i8, (String) zzf);
                                break;
                            }
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 9:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb3 = zzgr.zza(i8, zzhn.zzf(t, j2), zza(i5));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 10:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb3 = zzea.zzc(i8, (zzdn) zzhn.zzf(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 11:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb3 = zzea.zzg(i8, zzhn.zza(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 12:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb3 = zzea.zzk(i8, zzhn.zza(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 13:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb3 = zzea.zzj(i8, 0);
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 14:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb3 = zzea.zzh(i8, 0L);
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 15:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb3 = zzea.zzh(i8, zzhn.zza(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 16:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb3 = zzea.zzf(i8, zzhn.zzb(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 17:
                        if (zza((zzgd<T>) t, i5)) {
                            zzb3 = zzea.zzc(i8, (zzfz) zzhn.zzf(t, j2), zza(i5));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 18:
                        zzb3 = zzgr.zzi(i8, zza(t, j2), false);
                        break;
                    case 19:
                        zzb3 = zzgr.zzh(i8, zza(t, j2), false);
                        break;
                    case 20:
                        zzb3 = zzgr.zza(i8, (List<Long>) zza(t, j2), false);
                        break;
                    case 21:
                        zzb3 = zzgr.zzb(i8, (List<Long>) zza(t, j2), false);
                        break;
                    case 22:
                        zzb3 = zzgr.zze(i8, zza(t, j2), false);
                        break;
                    case 23:
                        zzb3 = zzgr.zzi(i8, zza(t, j2), false);
                        break;
                    case 24:
                        zzb3 = zzgr.zzh(i8, zza(t, j2), false);
                        break;
                    case 25:
                        zzb3 = zzgr.zzj(i8, zza(t, j2), false);
                        break;
                    case 26:
                        zzb3 = zzgr.zza(i8, zza(t, j2));
                        break;
                    case 27:
                        zzb3 = zzgr.zza(i8, zza(t, j2), zza(i5));
                        break;
                    case 28:
                        zzb3 = zzgr.zzb(i8, zza(t, j2));
                        break;
                    case 29:
                        zzb3 = zzgr.zzf(i8, zza(t, j2), false);
                        break;
                    case 30:
                        zzb3 = zzgr.zzd(i8, zza(t, j2), false);
                        break;
                    case R.styleable.AppCompatTheme_actionModeWebSearchDrawable /* 31 */:
                        zzb3 = zzgr.zzh(i8, zza(t, j2), false);
                        break;
                    case 32:
                        zzb3 = zzgr.zzi(i8, zza(t, j2), false);
                        break;
                    case R.styleable.AppCompatTheme_actionOverflowMenuStyle /* 33 */:
                        zzb3 = zzgr.zzg(i8, zza(t, j2), false);
                        break;
                    case R.styleable.AppCompatTheme_activityChooserViewStyle /* 34 */:
                        zzb3 = zzgr.zzc(i8, zza(t, j2), false);
                        break;
                    case R.styleable.AppCompatTheme_alertDialogButtonGroupStyle /* 35 */:
                        zzi2 = zzgr.zzi((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb3 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_alertDialogCenterButtons /* 36 */:
                        zzi2 = zzgr.zzh((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb3 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_alertDialogStyle /* 37 */:
                        zzi2 = zzgr.zza((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb3 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_alertDialogTheme /* 38 */:
                        zzi2 = zzgr.zzb((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb3 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_autoCompleteTextViewStyle /* 39 */:
                        zzi2 = zzgr.zze((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb3 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_borderlessButtonStyle /* 40 */:
                        zzi2 = zzgr.zzi((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb3 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_buttonBarButtonStyle /* 41 */:
                        zzi2 = zzgr.zzh((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb3 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_buttonBarNegativeButtonStyle /* 42 */:
                        zzi2 = zzgr.zzj((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb3 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_buttonBarNeutralButtonStyle /* 43 */:
                        zzi2 = zzgr.zzf((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb3 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_buttonBarPositiveButtonStyle /* 44 */:
                        zzi2 = zzgr.zzd((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb3 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_buttonBarStyle /* 45 */:
                        zzi2 = zzgr.zzh((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb3 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_buttonStyle /* 46 */:
                        zzi2 = zzgr.zzi((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb3 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_buttonStyleSmall /* 47 */:
                        zzi2 = zzgr.zzg((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb3 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_checkboxStyle /* 48 */:
                        zzi2 = zzgr.zzc((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            zze2 = zzea.zze(i8);
                            zzg2 = zzea.zzg(zzi2);
                            zzb3 = zze2 + zzg2 + zzi2;
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_checkedTextViewStyle /* 49 */:
                        zzb3 = zzgr.zzb(i8, (List<zzfz>) zza(t, j2), zza(i5));
                        break;
                    case R.styleable.AppCompatTheme_colorAccent /* 50 */:
                        zzb3 = this.zzs.zza(i8, zzhn.zzf(t, j2), zzb(i5));
                        break;
                    case R.styleable.AppCompatTheme_colorBackgroundFloating /* 51 */:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb3 = zzea.zzb(i8, 0.0d);
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_colorButtonNormal /* 52 */:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb3 = zzea.zzb(i8, 0.0f);
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_colorControlActivated /* 53 */:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb3 = zzea.zzd(i8, zze(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_colorControlHighlight /* 54 */:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb3 = zzea.zze(i8, zze(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_colorControlNormal /* 55 */:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb3 = zzea.zzf(i8, zzd(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_colorError /* 56 */:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb3 = zzea.zzg(i8, 0L);
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_colorPrimary /* 57 */:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb3 = zzea.zzi(i8, 0);
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_colorPrimaryDark /* 58 */:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb3 = zzea.zzb(i8, true);
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_colorSwitchThumbNormal /* 59 */:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            Object zzf2 = zzhn.zzf(t, j2);
                            if (zzf2 instanceof zzdn) {
                                zzb3 = zzea.zzc(i8, (zzdn) zzf2);
                                break;
                            } else {
                                zzb3 = zzea.zzb(i8, (String) zzf2);
                                break;
                            }
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_controlBackground /* 60 */:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb3 = zzgr.zza(i8, zzhn.zzf(t, j2), zza(i5));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_dialogCornerRadius /* 61 */:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb3 = zzea.zzc(i8, (zzdn) zzhn.zzf(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_dialogPreferredPadding /* 62 */:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb3 = zzea.zzg(i8, zzd(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_dialogTheme /* 63 */:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb3 = zzea.zzk(i8, zzd(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 64:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb3 = zzea.zzj(i8, 0);
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case 65:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb3 = zzea.zzh(i8, 0L);
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_dropDownListViewStyle /* 66 */:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb3 = zzea.zzh(i8, zzd(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_dropdownListPreferredItemHeight /* 67 */:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb3 = zzea.zzf(i8, zze(t, j2));
                            break;
                        } else {
                            continue;
                            i5 += 3;
                            i2 = 267386880;
                        }
                    case R.styleable.AppCompatTheme_editTextBackground /* 68 */:
                        if (zza((zzgd<T>) t, i8, i5)) {
                            zzb3 = zzea.zzc(i8, (zzfz) zzhn.zzf(t, j2), zza(i5));
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
                i6 += zzb3;
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
                        continue;
                        i10 += 3;
                        i3 = 1048575;
                        i4 = 1;
                    }
                    break;
                case 1:
                    j = 0;
                    if ((i13 & i) != 0) {
                        i11 += zzea.zzb(i14, 0.0f);
                        break;
                    }
                    break;
                case 2:
                    j = 0;
                    if ((i & i13) != 0) {
                        zzd = zzea.zzd(i14, unsafe2.getLong(t, j3));
                        i11 += zzd;
                        break;
                    }
                    break;
                case 3:
                    j = 0;
                    if ((i & i13) != 0) {
                        zzd = zzea.zze(i14, unsafe2.getLong(t, j3));
                        i11 += zzd;
                        break;
                    }
                    break;
                case 4:
                    j = 0;
                    if ((i & i13) != 0) {
                        zzd = zzea.zzf(i14, unsafe2.getInt(t, j3));
                        i11 += zzd;
                        break;
                    }
                    break;
                case 5:
                    j = 0;
                    if ((i13 & i) != 0) {
                        zzd = zzea.zzg(i14, 0L);
                        i11 += zzd;
                        break;
                    }
                    break;
                case 6:
                    if ((i13 & i) != 0) {
                        i11 += zzea.zzi(i14, 0);
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case 7:
                    if ((i13 & i) != 0) {
                        i11 += zzea.zzb(i14, true);
                        j = 0;
                        i10 += 3;
                        i3 = 1048575;
                        i4 = 1;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case 8:
                    if ((i13 & i) != 0) {
                        Object object = unsafe2.getObject(t, j3);
                        if (object instanceof zzdn) {
                            zzb2 = zzea.zzc(i14, (zzdn) object);
                        } else {
                            zzb2 = zzea.zzb(i14, (String) object);
                        }
                        i11 += zzb2;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case 9:
                    if ((i13 & i) != 0) {
                        zzb2 = zzgr.zza(i14, unsafe2.getObject(t, j3), zza(i10));
                        i11 += zzb2;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case 10:
                    if ((i13 & i) != 0) {
                        zzb2 = zzea.zzc(i14, (zzdn) unsafe2.getObject(t, j3));
                        i11 += zzb2;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case 11:
                    if ((i13 & i) != 0) {
                        zzb2 = zzea.zzg(i14, unsafe2.getInt(t, j3));
                        i11 += zzb2;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case 12:
                    if ((i13 & i) != 0) {
                        zzb2 = zzea.zzk(i14, unsafe2.getInt(t, j3));
                        i11 += zzb2;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case 13:
                    if ((i13 & i) != 0) {
                        zzj = zzea.zzj(i14, 0);
                        i11 += zzj;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case 14:
                    if ((i13 & i) != 0) {
                        zzb2 = zzea.zzh(i14, 0L);
                        i11 += zzb2;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case 15:
                    if ((i13 & i) != 0) {
                        zzb2 = zzea.zzh(i14, unsafe2.getInt(t, j3));
                        i11 += zzb2;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case 16:
                    if ((i13 & i) != 0) {
                        zzb2 = zzea.zzf(i14, unsafe2.getLong(t, j3));
                        i11 += zzb2;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case 17:
                    if ((i13 & i) != 0) {
                        zzb2 = zzea.zzc(i14, (zzfz) unsafe2.getObject(t, j3), zza(i10));
                        i11 += zzb2;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case 18:
                    zzb2 = zzgr.zzi(i14, (List) unsafe2.getObject(t, j3), false);
                    i11 += zzb2;
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case 19:
                    zzh = zzgr.zzh(i14, (List) unsafe2.getObject(t, j3), false);
                    i11 += zzh;
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case 20:
                    zzh = zzgr.zza(i14, (List<Long>) unsafe2.getObject(t, j3), false);
                    i11 += zzh;
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case 21:
                    zzh = zzgr.zzb(i14, (List<Long>) unsafe2.getObject(t, j3), false);
                    i11 += zzh;
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case 22:
                    zzh = zzgr.zze(i14, (List) unsafe2.getObject(t, j3), false);
                    i11 += zzh;
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case 23:
                    zzh = zzgr.zzi(i14, (List) unsafe2.getObject(t, j3), false);
                    i11 += zzh;
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case 24:
                    zzh = zzgr.zzh(i14, (List) unsafe2.getObject(t, j3), false);
                    i11 += zzh;
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case 25:
                    zzh = zzgr.zzj(i14, (List) unsafe2.getObject(t, j3), false);
                    i11 += zzh;
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case 26:
                    zzb2 = zzgr.zza(i14, (List) unsafe2.getObject(t, j3));
                    i11 += zzb2;
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case 27:
                    zzb2 = zzgr.zza(i14, (List<?>) unsafe2.getObject(t, j3), zza(i10));
                    i11 += zzb2;
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case 28:
                    zzb2 = zzgr.zzb(i14, (List) unsafe2.getObject(t, j3));
                    i11 += zzb2;
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case 29:
                    zzb2 = zzgr.zzf(i14, (List) unsafe2.getObject(t, j3), false);
                    i11 += zzb2;
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case 30:
                    zzh = zzgr.zzd(i14, (List) unsafe2.getObject(t, j3), false);
                    i11 += zzh;
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_actionModeWebSearchDrawable /* 31 */:
                    zzh = zzgr.zzh(i14, (List) unsafe2.getObject(t, j3), false);
                    i11 += zzh;
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case 32:
                    zzh = zzgr.zzi(i14, (List) unsafe2.getObject(t, j3), false);
                    i11 += zzh;
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_actionOverflowMenuStyle /* 33 */:
                    zzh = zzgr.zzg(i14, (List) unsafe2.getObject(t, j3), false);
                    i11 += zzh;
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_activityChooserViewStyle /* 34 */:
                    zzh = zzgr.zzc(i14, (List) unsafe2.getObject(t, j3), false);
                    i11 += zzh;
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_alertDialogButtonGroupStyle /* 35 */:
                    zzi = zzgr.zzi((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzj = zze + zzg + zzi;
                        i11 += zzj;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_alertDialogCenterButtons /* 36 */:
                    zzi = zzgr.zzh((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzj = zze + zzg + zzi;
                        i11 += zzj;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_alertDialogStyle /* 37 */:
                    zzi = zzgr.zza((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzj = zze + zzg + zzi;
                        i11 += zzj;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_alertDialogTheme /* 38 */:
                    zzi = zzgr.zzb((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzj = zze + zzg + zzi;
                        i11 += zzj;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_autoCompleteTextViewStyle /* 39 */:
                    zzi = zzgr.zze((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzj = zze + zzg + zzi;
                        i11 += zzj;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_borderlessButtonStyle /* 40 */:
                    zzi = zzgr.zzi((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzj = zze + zzg + zzi;
                        i11 += zzj;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_buttonBarButtonStyle /* 41 */:
                    zzi = zzgr.zzh((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzj = zze + zzg + zzi;
                        i11 += zzj;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_buttonBarNegativeButtonStyle /* 42 */:
                    zzi = zzgr.zzj((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzj = zze + zzg + zzi;
                        i11 += zzj;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_buttonBarNeutralButtonStyle /* 43 */:
                    zzi = zzgr.zzf((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzj = zze + zzg + zzi;
                        i11 += zzj;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_buttonBarPositiveButtonStyle /* 44 */:
                    zzi = zzgr.zzd((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzj = zze + zzg + zzi;
                        i11 += zzj;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_buttonBarStyle /* 45 */:
                    zzi = zzgr.zzh((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzj = zze + zzg + zzi;
                        i11 += zzj;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_buttonStyle /* 46 */:
                    zzi = zzgr.zzi((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzj = zze + zzg + zzi;
                        i11 += zzj;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_buttonStyleSmall /* 47 */:
                    zzi = zzgr.zzg((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzj = zze + zzg + zzi;
                        i11 += zzj;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_checkboxStyle /* 48 */:
                    zzi = zzgr.zzc((List) unsafe2.getObject(t, j3));
                    if (zzi > 0) {
                        zze = zzea.zze(i14);
                        zzg = zzea.zzg(zzi);
                        zzj = zze + zzg + zzi;
                        i11 += zzj;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_checkedTextViewStyle /* 49 */:
                    zzb2 = zzgr.zzb(i14, (List) unsafe2.getObject(t, j3), zza(i10));
                    i11 += zzb2;
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_colorAccent /* 50 */:
                    zzb2 = this.zzs.zza(i14, unsafe2.getObject(t, j3), zzb(i10));
                    i11 += zzb2;
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_colorBackgroundFloating /* 51 */:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb2 = zzea.zzb(i14, 0.0d);
                        i11 += zzb2;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_colorButtonNormal /* 52 */:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzj = zzea.zzb(i14, 0.0f);
                        i11 += zzj;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_colorControlActivated /* 53 */:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb2 = zzea.zzd(i14, zze(t, j3));
                        i11 += zzb2;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_colorControlHighlight /* 54 */:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb2 = zzea.zze(i14, zze(t, j3));
                        i11 += zzb2;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_colorControlNormal /* 55 */:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb2 = zzea.zzf(i14, zzd(t, j3));
                        i11 += zzb2;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_colorError /* 56 */:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb2 = zzea.zzg(i14, 0L);
                        i11 += zzb2;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_colorPrimary /* 57 */:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzj = zzea.zzi(i14, 0);
                        i11 += zzj;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_colorPrimaryDark /* 58 */:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzj = zzea.zzb(i14, true);
                        i11 += zzj;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_colorSwitchThumbNormal /* 59 */:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        Object object2 = unsafe2.getObject(t, j3);
                        if (object2 instanceof zzdn) {
                            zzb2 = zzea.zzc(i14, (zzdn) object2);
                        } else {
                            zzb2 = zzea.zzb(i14, (String) object2);
                        }
                        i11 += zzb2;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_controlBackground /* 60 */:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb2 = zzgr.zza(i14, unsafe2.getObject(t, j3), zza(i10));
                        i11 += zzb2;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_dialogCornerRadius /* 61 */:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb2 = zzea.zzc(i14, (zzdn) unsafe2.getObject(t, j3));
                        i11 += zzb2;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_dialogPreferredPadding /* 62 */:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb2 = zzea.zzg(i14, zzd(t, j3));
                        i11 += zzb2;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_dialogTheme /* 63 */:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb2 = zzea.zzk(i14, zzd(t, j3));
                        i11 += zzb2;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case 64:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzj = zzea.zzj(i14, 0);
                        i11 += zzj;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case 65:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb2 = zzea.zzh(i14, 0L);
                        i11 += zzb2;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_dropDownListViewStyle /* 66 */:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb2 = zzea.zzh(i14, zzd(t, j3));
                        i11 += zzb2;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_dropdownListPreferredItemHeight /* 67 */:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb2 = zzea.zzf(i14, zze(t, j3));
                        i11 += zzb2;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                case R.styleable.AppCompatTheme_editTextBackground /* 68 */:
                    if (zza((zzgd<T>) t, i14, i10)) {
                        zzb2 = zzea.zzc(i14, (zzfz) unsafe2.getObject(t, j3), zza(i10));
                        i11 += zzb2;
                    }
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
                default:
                    j = 0;
                    i10 += 3;
                    i3 = 1048575;
                    i4 = 1;
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
                Map.Entry<?, Object> zzb4 = zza3.zza.zzb(i19);
                i18 += zzej.zza((zzel) zzb4.getKey(), zzb4.getValue());
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
                            case R.styleable.AppCompatTheme_actionModeWebSearchDrawable /* 31 */:
                                zzgr.zzl(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, false);
                                break;
                            case 32:
                                zzgr.zzg(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, false);
                                break;
                            case R.styleable.AppCompatTheme_actionOverflowMenuStyle /* 33 */:
                                zzgr.zzj(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, false);
                                break;
                            case R.styleable.AppCompatTheme_activityChooserViewStyle /* 34 */:
                                zzgr.zze(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, false);
                                break;
                            case R.styleable.AppCompatTheme_alertDialogButtonGroupStyle /* 35 */:
                                zzgr.zza(this.zzc[length2], (List<Double>) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_alertDialogCenterButtons /* 36 */:
                                zzgr.zzb(this.zzc[length2], (List<Float>) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_alertDialogStyle /* 37 */:
                                zzgr.zzc(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_alertDialogTheme /* 38 */:
                                zzgr.zzd(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_autoCompleteTextViewStyle /* 39 */:
                                zzgr.zzh(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_borderlessButtonStyle /* 40 */:
                                zzgr.zzf(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_buttonBarButtonStyle /* 41 */:
                                zzgr.zzk(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_buttonBarNegativeButtonStyle /* 42 */:
                                zzgr.zzn(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_buttonBarNeutralButtonStyle /* 43 */:
                                zzgr.zzi(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_buttonBarPositiveButtonStyle /* 44 */:
                                zzgr.zzm(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_buttonBarStyle /* 45 */:
                                zzgr.zzl(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_buttonStyle /* 46 */:
                                zzgr.zzg(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_buttonStyleSmall /* 47 */:
                                zzgr.zzj(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_checkboxStyle /* 48 */:
                                zzgr.zze(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_checkedTextViewStyle /* 49 */:
                                zzgr.zzb(this.zzc[length2], (List) zzhn.zzf(t, zzc & 1048575), zzibVar, zza(length2));
                                break;
                            case R.styleable.AppCompatTheme_colorAccent /* 50 */:
                                zza(zzibVar, i2, zzhn.zzf(t, zzc & 1048575), length2);
                                break;
                            case R.styleable.AppCompatTheme_colorBackgroundFloating /* 51 */:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zza(i2, zzb(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_colorButtonNormal /* 52 */:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zza(i2, zzc(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_colorControlActivated /* 53 */:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zza(i2, zze(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_colorControlHighlight /* 54 */:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zzc(i2, zze(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_colorControlNormal /* 55 */:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zzc(i2, zzd(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_colorError /* 56 */:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zzd(i2, zze(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_colorPrimary /* 57 */:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zzd(i2, zzd(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_colorPrimaryDark /* 58 */:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zza(i2, zzf(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_colorSwitchThumbNormal /* 59 */:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zza(i2, zzhn.zzf(t, zzc & 1048575), zzibVar);
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_controlBackground /* 60 */:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zza(i2, zzhn.zzf(t, zzc & 1048575), zza(length2));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_dialogCornerRadius /* 61 */:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zza(i2, (zzdn) zzhn.zzf(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_dialogPreferredPadding /* 62 */:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zze(i2, zzd(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_dialogTheme /* 63 */:
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
                            case R.styleable.AppCompatTheme_dropDownListViewStyle /* 66 */:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zzf(i2, zzd(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_dropdownListPreferredItemHeight /* 67 */:
                                if (zza((zzgd<T>) t, i2, length2)) {
                                    zzibVar.zze(i2, zze(t, zzc & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_editTextBackground /* 68 */:
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
                            case R.styleable.AppCompatTheme_actionModeWebSearchDrawable /* 31 */:
                                zzgr.zzl(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, false);
                                break;
                            case 32:
                                zzgr.zzg(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, false);
                                break;
                            case R.styleable.AppCompatTheme_actionOverflowMenuStyle /* 33 */:
                                zzgr.zzj(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, false);
                                break;
                            case R.styleable.AppCompatTheme_activityChooserViewStyle /* 34 */:
                                zzgr.zze(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, false);
                                break;
                            case R.styleable.AppCompatTheme_alertDialogButtonGroupStyle /* 35 */:
                                zzgr.zza(this.zzc[i], (List<Double>) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_alertDialogCenterButtons /* 36 */:
                                zzgr.zzb(this.zzc[i], (List<Float>) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_alertDialogStyle /* 37 */:
                                zzgr.zzc(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_alertDialogTheme /* 38 */:
                                zzgr.zzd(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_autoCompleteTextViewStyle /* 39 */:
                                zzgr.zzh(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_borderlessButtonStyle /* 40 */:
                                zzgr.zzf(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_buttonBarButtonStyle /* 41 */:
                                zzgr.zzk(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_buttonBarNegativeButtonStyle /* 42 */:
                                zzgr.zzn(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_buttonBarNeutralButtonStyle /* 43 */:
                                zzgr.zzi(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_buttonBarPositiveButtonStyle /* 44 */:
                                zzgr.zzm(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_buttonBarStyle /* 45 */:
                                zzgr.zzl(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_buttonStyle /* 46 */:
                                zzgr.zzg(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_buttonStyleSmall /* 47 */:
                                zzgr.zzj(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_checkboxStyle /* 48 */:
                                zzgr.zze(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, true);
                                break;
                            case R.styleable.AppCompatTheme_checkedTextViewStyle /* 49 */:
                                zzgr.zzb(this.zzc[i], (List) zzhn.zzf(t, zzc2 & 1048575), zzibVar, zza(i));
                                break;
                            case R.styleable.AppCompatTheme_colorAccent /* 50 */:
                                zza(zzibVar, i3, zzhn.zzf(t, zzc2 & 1048575), i);
                                break;
                            case R.styleable.AppCompatTheme_colorBackgroundFloating /* 51 */:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zza(i3, zzb(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_colorButtonNormal /* 52 */:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zza(i3, zzc(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_colorControlActivated /* 53 */:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zza(i3, zze(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_colorControlHighlight /* 54 */:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zzc(i3, zze(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_colorControlNormal /* 55 */:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zzc(i3, zzd(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_colorError /* 56 */:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zzd(i3, zze(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_colorPrimary /* 57 */:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zzd(i3, zzd(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_colorPrimaryDark /* 58 */:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zza(i3, zzf(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_colorSwitchThumbNormal /* 59 */:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zza(i3, zzhn.zzf(t, zzc2 & 1048575), zzibVar);
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_controlBackground /* 60 */:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zza(i3, zzhn.zzf(t, zzc2 & 1048575), zza(i));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_dialogCornerRadius /* 61 */:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zza(i3, (zzdn) zzhn.zzf(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_dialogPreferredPadding /* 62 */:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zze(i3, zzd(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_dialogTheme /* 63 */:
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
                            case R.styleable.AppCompatTheme_dropDownListViewStyle /* 66 */:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zzf(i3, zzd(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_dropdownListPreferredItemHeight /* 67 */:
                                if (zza((zzgd<T>) t, i3, i)) {
                                    zzibVar.zze(i3, zze(t, zzc2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.AppCompatTheme_editTextBackground /* 68 */:
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
                        case R.styleable.AppCompatTheme_actionModeWebSearchDrawable /* 31 */:
                            zzgr.zzl(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, false);
                            break;
                        case 32:
                            zzgr.zzg(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, false);
                            break;
                        case R.styleable.AppCompatTheme_actionOverflowMenuStyle /* 33 */:
                            zzgr.zzj(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, false);
                            break;
                        case R.styleable.AppCompatTheme_activityChooserViewStyle /* 34 */:
                            zzgr.zze(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, false);
                            break;
                        case R.styleable.AppCompatTheme_alertDialogButtonGroupStyle /* 35 */:
                            zzgr.zza(this.zzc[i], (List<Double>) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case R.styleable.AppCompatTheme_alertDialogCenterButtons /* 36 */:
                            zzgr.zzb(this.zzc[i], (List<Float>) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case R.styleable.AppCompatTheme_alertDialogStyle /* 37 */:
                            zzgr.zzc(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case R.styleable.AppCompatTheme_alertDialogTheme /* 38 */:
                            zzgr.zzd(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case R.styleable.AppCompatTheme_autoCompleteTextViewStyle /* 39 */:
                            zzgr.zzh(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case R.styleable.AppCompatTheme_borderlessButtonStyle /* 40 */:
                            zzgr.zzf(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case R.styleable.AppCompatTheme_buttonBarButtonStyle /* 41 */:
                            zzgr.zzk(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case R.styleable.AppCompatTheme_buttonBarNegativeButtonStyle /* 42 */:
                            zzgr.zzn(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case R.styleable.AppCompatTheme_buttonBarNeutralButtonStyle /* 43 */:
                            zzgr.zzi(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case R.styleable.AppCompatTheme_buttonBarPositiveButtonStyle /* 44 */:
                            zzgr.zzm(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case R.styleable.AppCompatTheme_buttonBarStyle /* 45 */:
                            zzgr.zzl(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case R.styleable.AppCompatTheme_buttonStyle /* 46 */:
                            zzgr.zzg(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case R.styleable.AppCompatTheme_buttonStyleSmall /* 47 */:
                            zzgr.zzj(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case R.styleable.AppCompatTheme_checkboxStyle /* 48 */:
                            zzgr.zze(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, true);
                            break;
                        case R.styleable.AppCompatTheme_checkedTextViewStyle /* 49 */:
                            zzgr.zzb(this.zzc[i], (List) unsafe.getObject(t, j), zzibVar, zza(i));
                            break;
                        case R.styleable.AppCompatTheme_colorAccent /* 50 */:
                            zza(zzibVar, i5, unsafe.getObject(t, j), i);
                            break;
                        case R.styleable.AppCompatTheme_colorBackgroundFloating /* 51 */:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zza(i5, zzb(t, j));
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_colorButtonNormal /* 52 */:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zza(i5, zzc(t, j));
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_colorControlActivated /* 53 */:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zza(i5, zze(t, j));
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_colorControlHighlight /* 54 */:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zzc(i5, zze(t, j));
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_colorControlNormal /* 55 */:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zzc(i5, zzd(t, j));
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_colorError /* 56 */:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zzd(i5, zze(t, j));
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_colorPrimary /* 57 */:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zzd(i5, zzd(t, j));
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_colorPrimaryDark /* 58 */:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zza(i5, zzf(t, j));
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_colorSwitchThumbNormal /* 59 */:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zza(i5, unsafe.getObject(t, j), zzibVar);
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_controlBackground /* 60 */:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zza(i5, unsafe.getObject(t, j), zza(i));
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_dialogCornerRadius /* 61 */:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zza(i5, (zzdn) unsafe.getObject(t, j));
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_dialogPreferredPadding /* 62 */:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zze(i5, zzd(t, j));
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_dialogTheme /* 63 */:
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
                        case R.styleable.AppCompatTheme_dropDownListViewStyle /* 66 */:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zzf(i5, zzd(t, j));
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_dropdownListPreferredItemHeight /* 67 */:
                            if (zza((zzgd<T>) t, i5, i)) {
                                zzibVar.zze(i5, zze(t, j));
                                break;
                            }
                            break;
                        case R.styleable.AppCompatTheme_editTextBackground /* 68 */:
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
                i2 = i4;
                i = i9;
            } else {
                i = i3;
                i2 = i4;
            }
            if (((268435456 & zzc) != 0) && !zza(t, i6, i, i2, i10)) {
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
            i3 = i;
            i4 = i2;
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
