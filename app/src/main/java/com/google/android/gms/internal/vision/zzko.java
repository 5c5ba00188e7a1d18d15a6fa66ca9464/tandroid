package com.google.android.gms.internal.vision;

import com.google.android.gms.internal.vision.zzjb;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.telegram.messenger.CharacterCompat;
import org.telegram.messenger.R;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.ConnectionsManager;
import sun.misc.Unsafe;
/* compiled from: com.google.android.gms:play-services-vision-common@@19.1.3 */
/* loaded from: classes.dex */
public final class zzko<T> implements zzlc<T> {
    private static final int[] zza = new int[0];
    private static final Unsafe zzb = zzma.zzc();
    private final int[] zzc;
    private final Object[] zzd;
    private final int zze;
    private final int zzf;
    private final zzkk zzg;
    private final boolean zzh;
    private final boolean zzj;
    private final int[] zzl;
    private final int zzm;
    private final int zzn;
    private final zzks zzo;
    private final zzju zzp;
    private final zzlu<?, ?> zzq;
    private final zziq<?> zzr;
    private final zzkh zzs;

    private zzko(int[] iArr, Object[] objArr, int i, int i2, zzkk zzkkVar, boolean z, boolean z2, int[] iArr2, int i3, int i4, zzks zzksVar, zzju zzjuVar, zzlu<?, ?> zzluVar, zziq<?> zziqVar, zzkh zzkhVar) {
        this.zzc = iArr;
        this.zzd = objArr;
        this.zze = i;
        this.zzf = i2;
        boolean z3 = zzkkVar instanceof zzjb;
        this.zzj = z;
        this.zzh = zziqVar != null && zziqVar.zza(zzkkVar);
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

    /* JADX WARN: Removed duplicated region for block: B:159:0x033a  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x039c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static <T> zzko<T> zza(Class<T> cls, zzki zzkiVar, zzks zzksVar, zzju zzjuVar, zzlu<?, ?> zzluVar, zziq<?> zziqVar, zzkh zzkhVar) {
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
        if (zzkiVar instanceof zzla) {
            zzla zzlaVar = (zzla) zzkiVar;
            int i32 = 0;
            boolean z2 = zzlaVar.zza() == zzkz.zzb;
            String zzd = zzlaVar.zzd();
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
            Object[] zze = zzlaVar.zze();
            Class<?> cls2 = zzlaVar.zzc().getClass();
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
                    char c = CharacterCompat.MIN_HIGH_SURROGATE;
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
                            c = CharacterCompat.MIN_HIGH_SURROGATE;
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
            return new zzko<>(iArr2, objArr2, i6, i5, zzlaVar.zzc(), z2, false, iArr, i2, i63, zzksVar, zzjuVar, zzluVar, zziqVar, zzkhVar);
        }
        ((zzlr) zzkiVar).zza();
        int i100 = zzkz.zzb;
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

    @Override // com.google.android.gms.internal.vision.zzlc
    public final T zza() {
        return (T) this.zzo.zza(this.zzg);
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0038, code lost:
        if (com.google.android.gms.internal.vision.zzle.zza(com.google.android.gms.internal.vision.zzma.zzf(r10, r6), com.google.android.gms.internal.vision.zzma.zzf(r11, r6)) != false) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x006a, code lost:
        if (com.google.android.gms.internal.vision.zzle.zza(com.google.android.gms.internal.vision.zzma.zzf(r10, r6), com.google.android.gms.internal.vision.zzma.zzf(r11, r6)) != false) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x007e, code lost:
        if (com.google.android.gms.internal.vision.zzma.zzb(r10, r6) == com.google.android.gms.internal.vision.zzma.zzb(r11, r6)) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0090, code lost:
        if (com.google.android.gms.internal.vision.zzma.zza(r10, r6) == com.google.android.gms.internal.vision.zzma.zza(r11, r6)) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00a4, code lost:
        if (com.google.android.gms.internal.vision.zzma.zzb(r10, r6) == com.google.android.gms.internal.vision.zzma.zzb(r11, r6)) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00b6, code lost:
        if (com.google.android.gms.internal.vision.zzma.zza(r10, r6) == com.google.android.gms.internal.vision.zzma.zza(r11, r6)) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00c8, code lost:
        if (com.google.android.gms.internal.vision.zzma.zza(r10, r6) == com.google.android.gms.internal.vision.zzma.zza(r11, r6)) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00da, code lost:
        if (com.google.android.gms.internal.vision.zzma.zza(r10, r6) == com.google.android.gms.internal.vision.zzma.zza(r11, r6)) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00f0, code lost:
        if (com.google.android.gms.internal.vision.zzle.zza(com.google.android.gms.internal.vision.zzma.zzf(r10, r6), com.google.android.gms.internal.vision.zzma.zzf(r11, r6)) != false) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0106, code lost:
        if (com.google.android.gms.internal.vision.zzle.zza(com.google.android.gms.internal.vision.zzma.zzf(r10, r6), com.google.android.gms.internal.vision.zzma.zzf(r11, r6)) != false) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x011c, code lost:
        if (com.google.android.gms.internal.vision.zzle.zza(com.google.android.gms.internal.vision.zzma.zzf(r10, r6), com.google.android.gms.internal.vision.zzma.zzf(r11, r6)) != false) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x012e, code lost:
        if (com.google.android.gms.internal.vision.zzma.zzc(r10, r6) == com.google.android.gms.internal.vision.zzma.zzc(r11, r6)) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0140, code lost:
        if (com.google.android.gms.internal.vision.zzma.zza(r10, r6) == com.google.android.gms.internal.vision.zzma.zza(r11, r6)) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0154, code lost:
        if (com.google.android.gms.internal.vision.zzma.zzb(r10, r6) == com.google.android.gms.internal.vision.zzma.zzb(r11, r6)) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0165, code lost:
        if (com.google.android.gms.internal.vision.zzma.zza(r10, r6) == com.google.android.gms.internal.vision.zzma.zza(r11, r6)) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x0178, code lost:
        if (com.google.android.gms.internal.vision.zzma.zzb(r10, r6) == com.google.android.gms.internal.vision.zzma.zzb(r11, r6)) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x018b, code lost:
        if (com.google.android.gms.internal.vision.zzma.zzb(r10, r6) == com.google.android.gms.internal.vision.zzma.zzb(r11, r6)) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x01a4, code lost:
        if (java.lang.Float.floatToIntBits(com.google.android.gms.internal.vision.zzma.zzd(r10, r6)) == java.lang.Float.floatToIntBits(com.google.android.gms.internal.vision.zzma.zzd(r11, r6))) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x01bf, code lost:
        if (java.lang.Double.doubleToLongBits(com.google.android.gms.internal.vision.zzma.zze(r10, r6)) == java.lang.Double.doubleToLongBits(com.google.android.gms.internal.vision.zzma.zze(r11, r6))) goto L86;
     */
    @Override // com.google.android.gms.internal.vision.zzlc
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final boolean zza(T t, T t2) {
        int length = this.zzc.length;
        int i = 0;
        while (true) {
            boolean z = true;
            if (i < length) {
                int zzd = zzd(i);
                long j = zzd & 1048575;
                switch ((zzd & 267386880) >>> 20) {
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
                    case R.styleable.MapAttrs_uiScrollGesturesDuringRotateOrZoom /* 18 */:
                    case R.styleable.MapAttrs_uiTiltGestures /* 19 */:
                    case R.styleable.MapAttrs_uiZoomControls /* 20 */:
                    case R.styleable.MapAttrs_uiZoomGestures /* 21 */:
                    case R.styleable.MapAttrs_useViewLifecycle /* 22 */:
                    case R.styleable.MapAttrs_zOrderOnTop /* 23 */:
                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 28:
                    case 29:
                    case 30:
                    case 31:
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
                        z = zzle.zza(zzma.zzf(t, j), zzma.zzf(t2, j));
                        break;
                    case 50:
                        z = zzle.zza(zzma.zzf(t, j), zzma.zzf(t2, j));
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
                        long zze = zze(i) & 1048575;
                        if (zzma.zza(t, zze) == zzma.zza(t2, zze)) {
                            break;
                        }
                        z = false;
                        break;
                }
                if (!z) {
                    return false;
                }
                i += 3;
            } else if (!this.zzq.zzb(t).equals(this.zzq.zzb(t2))) {
                return false;
            } else {
                if (!this.zzh) {
                    return true;
                }
                return this.zzr.zza(t).equals(this.zzr.zza(t2));
            }
        }
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final int zza(T t) {
        int i;
        int i2;
        int length = this.zzc.length;
        int i3 = 0;
        for (int i4 = 0; i4 < length; i4 += 3) {
            int zzd = zzd(i4);
            int i5 = this.zzc[i4];
            long j = 1048575 & zzd;
            int i6 = 37;
            switch ((zzd & 267386880) >>> 20) {
                case 0:
                    i2 = i3 * 53;
                    i = zzjf.zza(Double.doubleToLongBits(zzma.zze(t, j)));
                    i3 = i2 + i;
                    break;
                case 1:
                    i2 = i3 * 53;
                    i = Float.floatToIntBits(zzma.zzd(t, j));
                    i3 = i2 + i;
                    break;
                case 2:
                    i2 = i3 * 53;
                    i = zzjf.zza(zzma.zzb(t, j));
                    i3 = i2 + i;
                    break;
                case 3:
                    i2 = i3 * 53;
                    i = zzjf.zza(zzma.zzb(t, j));
                    i3 = i2 + i;
                    break;
                case 4:
                    i2 = i3 * 53;
                    i = zzma.zza(t, j);
                    i3 = i2 + i;
                    break;
                case 5:
                    i2 = i3 * 53;
                    i = zzjf.zza(zzma.zzb(t, j));
                    i3 = i2 + i;
                    break;
                case 6:
                    i2 = i3 * 53;
                    i = zzma.zza(t, j);
                    i3 = i2 + i;
                    break;
                case 7:
                    i2 = i3 * 53;
                    i = zzjf.zza(zzma.zzc(t, j));
                    i3 = i2 + i;
                    break;
                case 8:
                    i2 = i3 * 53;
                    i = ((String) zzma.zzf(t, j)).hashCode();
                    i3 = i2 + i;
                    break;
                case 9:
                    Object zzf = zzma.zzf(t, j);
                    if (zzf != null) {
                        i6 = zzf.hashCode();
                    }
                    i3 = (i3 * 53) + i6;
                    break;
                case 10:
                    i2 = i3 * 53;
                    i = zzma.zzf(t, j).hashCode();
                    i3 = i2 + i;
                    break;
                case 11:
                    i2 = i3 * 53;
                    i = zzma.zza(t, j);
                    i3 = i2 + i;
                    break;
                case 12:
                    i2 = i3 * 53;
                    i = zzma.zza(t, j);
                    i3 = i2 + i;
                    break;
                case 13:
                    i2 = i3 * 53;
                    i = zzma.zza(t, j);
                    i3 = i2 + i;
                    break;
                case 14:
                    i2 = i3 * 53;
                    i = zzjf.zza(zzma.zzb(t, j));
                    i3 = i2 + i;
                    break;
                case 15:
                    i2 = i3 * 53;
                    i = zzma.zza(t, j);
                    i3 = i2 + i;
                    break;
                case 16:
                    i2 = i3 * 53;
                    i = zzjf.zza(zzma.zzb(t, j));
                    i3 = i2 + i;
                    break;
                case 17:
                    Object zzf2 = zzma.zzf(t, j);
                    if (zzf2 != null) {
                        i6 = zzf2.hashCode();
                    }
                    i3 = (i3 * 53) + i6;
                    break;
                case R.styleable.MapAttrs_uiScrollGesturesDuringRotateOrZoom /* 18 */:
                case R.styleable.MapAttrs_uiTiltGestures /* 19 */:
                case R.styleable.MapAttrs_uiZoomControls /* 20 */:
                case R.styleable.MapAttrs_uiZoomGestures /* 21 */:
                case R.styleable.MapAttrs_useViewLifecycle /* 22 */:
                case R.styleable.MapAttrs_zOrderOnTop /* 23 */:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
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
                    i2 = i3 * 53;
                    i = zzma.zzf(t, j).hashCode();
                    i3 = i2 + i;
                    break;
                case 50:
                    i2 = i3 * 53;
                    i = zzma.zzf(t, j).hashCode();
                    i3 = i2 + i;
                    break;
                case 51:
                    if (zza((zzko<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzjf.zza(Double.doubleToLongBits(zzb(t, j)));
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 52:
                    if (zza((zzko<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = Float.floatToIntBits(zzc(t, j));
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 53:
                    if (zza((zzko<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzjf.zza(zze(t, j));
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 54:
                    if (zza((zzko<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzjf.zza(zze(t, j));
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 55:
                    if (zza((zzko<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzd(t, j);
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 56:
                    if (zza((zzko<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzjf.zza(zze(t, j));
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 57:
                    if (zza((zzko<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzd(t, j);
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 58:
                    if (zza((zzko<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzjf.zza(zzf(t, j));
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 59:
                    if (zza((zzko<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = ((String) zzma.zzf(t, j)).hashCode();
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 60:
                    if (zza((zzko<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzma.zzf(t, j).hashCode();
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 61:
                    if (zza((zzko<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzma.zzf(t, j).hashCode();
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 62:
                    if (zza((zzko<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzd(t, j);
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 63:
                    if (zza((zzko<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzd(t, j);
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 64:
                    if (zza((zzko<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzd(t, j);
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case VoIPService.CALL_MIN_LAYER /* 65 */:
                    if (zza((zzko<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzjf.zza(zze(t, j));
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 66:
                    if (zza((zzko<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzd(t, j);
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 67:
                    if (zza((zzko<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzjf.zza(zze(t, j));
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
                case 68:
                    if (zza((zzko<T>) t, i5, i4)) {
                        i2 = i3 * 53;
                        i = zzma.zzf(t, j).hashCode();
                        i3 = i2 + i;
                        break;
                    } else {
                        break;
                    }
            }
        }
        int hashCode = (i3 * 53) + this.zzq.zzb(t).hashCode();
        return this.zzh ? (hashCode * 53) + this.zzr.zza(t).hashCode() : hashCode;
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final void zzb(T t, T t2) {
        t2.getClass();
        for (int i = 0; i < this.zzc.length; i += 3) {
            int zzd = zzd(i);
            long j = 1048575 & zzd;
            int i2 = this.zzc[i];
            switch ((zzd & 267386880) >>> 20) {
                case 0:
                    if (zza((zzko<T>) t2, i)) {
                        zzma.zza(t, j, zzma.zze(t2, j));
                        zzb((zzko<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 1:
                    if (zza((zzko<T>) t2, i)) {
                        zzma.zza((Object) t, j, zzma.zzd(t2, j));
                        zzb((zzko<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 2:
                    if (zza((zzko<T>) t2, i)) {
                        zzma.zza((Object) t, j, zzma.zzb(t2, j));
                        zzb((zzko<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 3:
                    if (zza((zzko<T>) t2, i)) {
                        zzma.zza((Object) t, j, zzma.zzb(t2, j));
                        zzb((zzko<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 4:
                    if (zza((zzko<T>) t2, i)) {
                        zzma.zza((Object) t, j, zzma.zza(t2, j));
                        zzb((zzko<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 5:
                    if (zza((zzko<T>) t2, i)) {
                        zzma.zza((Object) t, j, zzma.zzb(t2, j));
                        zzb((zzko<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 6:
                    if (zza((zzko<T>) t2, i)) {
                        zzma.zza((Object) t, j, zzma.zza(t2, j));
                        zzb((zzko<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 7:
                    if (zza((zzko<T>) t2, i)) {
                        zzma.zza(t, j, zzma.zzc(t2, j));
                        zzb((zzko<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 8:
                    if (zza((zzko<T>) t2, i)) {
                        zzma.zza(t, j, zzma.zzf(t2, j));
                        zzb((zzko<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 9:
                    zza(t, t2, i);
                    break;
                case 10:
                    if (zza((zzko<T>) t2, i)) {
                        zzma.zza(t, j, zzma.zzf(t2, j));
                        zzb((zzko<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 11:
                    if (zza((zzko<T>) t2, i)) {
                        zzma.zza((Object) t, j, zzma.zza(t2, j));
                        zzb((zzko<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 12:
                    if (zza((zzko<T>) t2, i)) {
                        zzma.zza((Object) t, j, zzma.zza(t2, j));
                        zzb((zzko<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 13:
                    if (zza((zzko<T>) t2, i)) {
                        zzma.zza((Object) t, j, zzma.zza(t2, j));
                        zzb((zzko<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 14:
                    if (zza((zzko<T>) t2, i)) {
                        zzma.zza((Object) t, j, zzma.zzb(t2, j));
                        zzb((zzko<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 15:
                    if (zza((zzko<T>) t2, i)) {
                        zzma.zza((Object) t, j, zzma.zza(t2, j));
                        zzb((zzko<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 16:
                    if (zza((zzko<T>) t2, i)) {
                        zzma.zza((Object) t, j, zzma.zzb(t2, j));
                        zzb((zzko<T>) t, i);
                        break;
                    } else {
                        break;
                    }
                case 17:
                    zza(t, t2, i);
                    break;
                case R.styleable.MapAttrs_uiScrollGesturesDuringRotateOrZoom /* 18 */:
                case R.styleable.MapAttrs_uiTiltGestures /* 19 */:
                case R.styleable.MapAttrs_uiZoomControls /* 20 */:
                case R.styleable.MapAttrs_uiZoomGestures /* 21 */:
                case R.styleable.MapAttrs_useViewLifecycle /* 22 */:
                case R.styleable.MapAttrs_zOrderOnTop /* 23 */:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
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
                    this.zzp.zza(t, t2, j);
                    break;
                case 50:
                    zzle.zza(this.zzs, t, t2, j);
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
                    if (zza((zzko<T>) t2, i2, i)) {
                        zzma.zza(t, j, zzma.zzf(t2, j));
                        zzb((zzko<T>) t, i2, i);
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
                case VoIPService.CALL_MIN_LAYER /* 65 */:
                case 66:
                case 67:
                    if (zza((zzko<T>) t2, i2, i)) {
                        zzma.zza(t, j, zzma.zzf(t2, j));
                        zzb((zzko<T>) t, i2, i);
                        break;
                    } else {
                        break;
                    }
                case 68:
                    zzb(t, t2, i);
                    break;
            }
        }
        zzle.zza(this.zzq, t, t2);
        if (this.zzh) {
            zzle.zza(this.zzr, t, t2);
        }
    }

    private final void zza(T t, T t2, int i) {
        long zzd = zzd(i) & 1048575;
        if (!zza((zzko<T>) t2, i)) {
            return;
        }
        Object zzf = zzma.zzf(t, zzd);
        Object zzf2 = zzma.zzf(t2, zzd);
        if (zzf != null && zzf2 != null) {
            zzma.zza(t, zzd, zzjf.zza(zzf, zzf2));
            zzb((zzko<T>) t, i);
        } else if (zzf2 == null) {
        } else {
            zzma.zza(t, zzd, zzf2);
            zzb((zzko<T>) t, i);
        }
    }

    private final void zzb(T t, T t2, int i) {
        int zzd = zzd(i);
        int i2 = this.zzc[i];
        long j = zzd & 1048575;
        if (!zza((zzko<T>) t2, i2, i)) {
            return;
        }
        Object obj = null;
        if (zza((zzko<T>) t, i2, i)) {
            obj = zzma.zzf(t, j);
        }
        Object zzf = zzma.zzf(t2, j);
        if (obj != null && zzf != null) {
            zzma.zza(t, j, zzjf.zza(obj, zzf));
            zzb((zzko<T>) t, i2, i);
        } else if (zzf == null) {
        } else {
            zzma.zza(t, j, zzf);
            zzb((zzko<T>) t, i2, i);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.google.android.gms.internal.vision.zzlc
    public final int zzb(T t) {
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
                int zzd = zzd(i14);
                int i16 = (zzd & i11) >>> 20;
                int i17 = this.zzc[i14];
                long j2 = zzd & 1048575;
                if (i16 >= zziv.DOUBLE_LIST_PACKED.zza() && i16 <= zziv.SINT64_LIST_PACKED.zza()) {
                    int i18 = this.zzc[i14 + 2];
                }
                switch (i16) {
                    case 0:
                        if (zza((zzko<T>) t, i14)) {
                            zzb3 = zzii.zzb(i17, 0.0d);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 1:
                        if (zza((zzko<T>) t, i14)) {
                            zzb3 = zzii.zzb(i17, 0.0f);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 2:
                        if (zza((zzko<T>) t, i14)) {
                            zzb3 = zzii.zzd(i17, zzma.zzb(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 3:
                        if (zza((zzko<T>) t, i14)) {
                            zzb3 = zzii.zze(i17, zzma.zzb(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 4:
                        if (zza((zzko<T>) t, i14)) {
                            zzb3 = zzii.zzf(i17, zzma.zza(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 5:
                        if (zza((zzko<T>) t, i14)) {
                            zzb3 = zzii.zzg(i17, 0L);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 6:
                        if (zza((zzko<T>) t, i14)) {
                            zzb3 = zzii.zzi(i17, 0);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 7:
                        if (zza((zzko<T>) t, i14)) {
                            zzb3 = zzii.zzb(i17, true);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 8:
                        if (zza((zzko<T>) t, i14)) {
                            Object zzf = zzma.zzf(t, j2);
                            if (zzf instanceof zzht) {
                                zzb3 = zzii.zzc(i17, (zzht) zzf);
                                break;
                            } else {
                                zzb3 = zzii.zzb(i17, (String) zzf);
                                break;
                            }
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 9:
                        if (zza((zzko<T>) t, i14)) {
                            zzb3 = zzle.zza(i17, zzma.zzf(t, j2), zza(i14));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 10:
                        if (zza((zzko<T>) t, i14)) {
                            zzb3 = zzii.zzc(i17, (zzht) zzma.zzf(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 11:
                        if (zza((zzko<T>) t, i14)) {
                            zzb3 = zzii.zzg(i17, zzma.zza(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 12:
                        if (zza((zzko<T>) t, i14)) {
                            zzb3 = zzii.zzk(i17, zzma.zza(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 13:
                        if (zza((zzko<T>) t, i14)) {
                            zzb3 = zzii.zzj(i17, 0);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 14:
                        if (zza((zzko<T>) t, i14)) {
                            zzb3 = zzii.zzh(i17, 0L);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 15:
                        if (zza((zzko<T>) t, i14)) {
                            zzb3 = zzii.zzh(i17, zzma.zza(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 16:
                        if (zza((zzko<T>) t, i14)) {
                            zzb3 = zzii.zzf(i17, zzma.zzb(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 17:
                        if (zza((zzko<T>) t, i14)) {
                            zzb3 = zzii.zzc(i17, (zzkk) zzma.zzf(t, j2), zza(i14));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case R.styleable.MapAttrs_uiScrollGesturesDuringRotateOrZoom /* 18 */:
                        zzb3 = zzle.zzi(i17, zza(t, j2), false);
                        break;
                    case R.styleable.MapAttrs_uiTiltGestures /* 19 */:
                        zzb3 = zzle.zzh(i17, zza(t, j2), false);
                        break;
                    case R.styleable.MapAttrs_uiZoomControls /* 20 */:
                        zzb3 = zzle.zza(i17, (List<Long>) zza(t, j2), false);
                        break;
                    case R.styleable.MapAttrs_uiZoomGestures /* 21 */:
                        zzb3 = zzle.zzb(i17, (List<Long>) zza(t, j2), false);
                        break;
                    case R.styleable.MapAttrs_useViewLifecycle /* 22 */:
                        zzb3 = zzle.zze(i17, zza(t, j2), false);
                        break;
                    case R.styleable.MapAttrs_zOrderOnTop /* 23 */:
                        zzb3 = zzle.zzi(i17, zza(t, j2), false);
                        break;
                    case 24:
                        zzb3 = zzle.zzh(i17, zza(t, j2), false);
                        break;
                    case 25:
                        zzb3 = zzle.zzj(i17, zza(t, j2), false);
                        break;
                    case 26:
                        zzb3 = zzle.zza(i17, zza(t, j2));
                        break;
                    case 27:
                        zzb3 = zzle.zza(i17, zza(t, j2), zza(i14));
                        break;
                    case 28:
                        zzb3 = zzle.zzb(i17, zza(t, j2));
                        break;
                    case 29:
                        zzb3 = zzle.zzf(i17, zza(t, j2), false);
                        break;
                    case 30:
                        zzb3 = zzle.zzd(i17, zza(t, j2), false);
                        break;
                    case 31:
                        zzb3 = zzle.zzh(i17, zza(t, j2), false);
                        break;
                    case ConnectionsManager.RequestFlagForceDownload /* 32 */:
                        zzb3 = zzle.zzi(i17, zza(t, j2), false);
                        break;
                    case 33:
                        zzb3 = zzle.zzg(i17, zza(t, j2), false);
                        break;
                    case 34:
                        zzb3 = zzle.zzc(i17, zza(t, j2), false);
                        break;
                    case 35:
                        i9 = zzle.zzi((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzii.zze(i17);
                            i8 = zzii.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 36:
                        i9 = zzle.zzh((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzii.zze(i17);
                            i8 = zzii.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 37:
                        i9 = zzle.zza((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzii.zze(i17);
                            i8 = zzii.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 38:
                        i9 = zzle.zzb((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzii.zze(i17);
                            i8 = zzii.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 39:
                        i9 = zzle.zze((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzii.zze(i17);
                            i8 = zzii.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 40:
                        i9 = zzle.zzi((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzii.zze(i17);
                            i8 = zzii.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 41:
                        i9 = zzle.zzh((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzii.zze(i17);
                            i8 = zzii.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 42:
                        i9 = zzle.zzj((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzii.zze(i17);
                            i8 = zzii.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 43:
                        i9 = zzle.zzf((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzii.zze(i17);
                            i8 = zzii.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 44:
                        i9 = zzle.zzd((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzii.zze(i17);
                            i8 = zzii.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 45:
                        i9 = zzle.zzh((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzii.zze(i17);
                            i8 = zzii.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 46:
                        i9 = zzle.zzi((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzii.zze(i17);
                            i8 = zzii.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 47:
                        i9 = zzle.zzg((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzii.zze(i17);
                            i8 = zzii.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 48:
                        i9 = zzle.zzc((List) unsafe.getObject(t, j2));
                        if (i9 > 0) {
                            i10 = zzii.zze(i17);
                            i8 = zzii.zzg(i9);
                            zzb3 = i10 + i8 + i9;
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 49:
                        zzb3 = zzle.zzb(i17, (List<zzkk>) zza(t, j2), zza(i14));
                        break;
                    case 50:
                        zzb3 = this.zzs.zza(i17, zzma.zzf(t, j2), zzb(i14));
                        break;
                    case 51:
                        if (zza((zzko<T>) t, i17, i14)) {
                            zzb3 = zzii.zzb(i17, 0.0d);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 52:
                        if (zza((zzko<T>) t, i17, i14)) {
                            zzb3 = zzii.zzb(i17, 0.0f);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 53:
                        if (zza((zzko<T>) t, i17, i14)) {
                            zzb3 = zzii.zzd(i17, zze(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 54:
                        if (zza((zzko<T>) t, i17, i14)) {
                            zzb3 = zzii.zze(i17, zze(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 55:
                        if (zza((zzko<T>) t, i17, i14)) {
                            zzb3 = zzii.zzf(i17, zzd(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 56:
                        if (zza((zzko<T>) t, i17, i14)) {
                            zzb3 = zzii.zzg(i17, 0L);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 57:
                        if (zza((zzko<T>) t, i17, i14)) {
                            zzb3 = zzii.zzi(i17, 0);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 58:
                        if (zza((zzko<T>) t, i17, i14)) {
                            zzb3 = zzii.zzb(i17, true);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 59:
                        if (zza((zzko<T>) t, i17, i14)) {
                            Object zzf2 = zzma.zzf(t, j2);
                            if (zzf2 instanceof zzht) {
                                zzb3 = zzii.zzc(i17, (zzht) zzf2);
                                break;
                            } else {
                                zzb3 = zzii.zzb(i17, (String) zzf2);
                                break;
                            }
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 60:
                        if (zza((zzko<T>) t, i17, i14)) {
                            zzb3 = zzle.zza(i17, zzma.zzf(t, j2), zza(i14));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 61:
                        if (zza((zzko<T>) t, i17, i14)) {
                            zzb3 = zzii.zzc(i17, (zzht) zzma.zzf(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 62:
                        if (zza((zzko<T>) t, i17, i14)) {
                            zzb3 = zzii.zzg(i17, zzd(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 63:
                        if (zza((zzko<T>) t, i17, i14)) {
                            zzb3 = zzii.zzk(i17, zzd(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 64:
                        if (zza((zzko<T>) t, i17, i14)) {
                            zzb3 = zzii.zzj(i17, 0);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case VoIPService.CALL_MIN_LAYER /* 65 */:
                        if (zza((zzko<T>) t, i17, i14)) {
                            zzb3 = zzii.zzh(i17, 0L);
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 66:
                        if (zza((zzko<T>) t, i17, i14)) {
                            zzb3 = zzii.zzh(i17, zzd(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 67:
                        if (zza((zzko<T>) t, i17, i14)) {
                            zzb3 = zzii.zzf(i17, zze(t, j2));
                            break;
                        } else {
                            continue;
                            i14 += 3;
                            i11 = 267386880;
                        }
                    case 68:
                        if (zza((zzko<T>) t, i17, i14)) {
                            zzb3 = zzii.zzc(i17, (zzkk) zzma.zzf(t, j2), zza(i14));
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
            return i15 + zza((zzlu) this.zzq, (Object) t);
        }
        Unsafe unsafe2 = zzb;
        int i19 = 0;
        int i20 = 0;
        int i21 = 1048575;
        int i22 = 0;
        while (i19 < this.zzc.length) {
            int zzd2 = zzd(i19);
            int[] iArr = this.zzc;
            int i23 = iArr[i19];
            int i24 = (zzd2 & 267386880) >>> 20;
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
            long j3 = zzd2 & i12;
            switch (i24) {
                case 0:
                    j = 0;
                    if ((i22 & i) != 0) {
                        i20 += zzii.zzb(i23, 0.0d);
                        continue;
                        i19 += 3;
                        i12 = 1048575;
                        i13 = 1;
                    }
                    break;
                case 1:
                    j = 0;
                    if ((i22 & i) != 0) {
                        i20 += zzii.zzb(i23, 0.0f);
                        break;
                    }
                    break;
                case 2:
                    j = 0;
                    if ((i & i22) != 0) {
                        i2 = zzii.zzd(i23, unsafe2.getLong(t, j3));
                        i20 += i2;
                        break;
                    }
                    break;
                case 3:
                    j = 0;
                    if ((i & i22) != 0) {
                        i2 = zzii.zze(i23, unsafe2.getLong(t, j3));
                        i20 += i2;
                        break;
                    }
                    break;
                case 4:
                    j = 0;
                    if ((i & i22) != 0) {
                        i2 = zzii.zzf(i23, unsafe2.getInt(t, j3));
                        i20 += i2;
                        break;
                    }
                    break;
                case 5:
                    j = 0;
                    if ((i22 & i) != 0) {
                        i2 = zzii.zzg(i23, 0L);
                        i20 += i2;
                        break;
                    }
                    break;
                case 6:
                    if ((i22 & i) != 0) {
                        i20 += zzii.zzi(i23, 0);
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 7:
                    if ((i22 & i) != 0) {
                        i20 += zzii.zzb(i23, true);
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
                        if (object instanceof zzht) {
                            zzb2 = zzii.zzc(i23, (zzht) object);
                        } else {
                            zzb2 = zzii.zzb(i23, (String) object);
                        }
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 9:
                    if ((i22 & i) != 0) {
                        zzb2 = zzle.zza(i23, unsafe2.getObject(t, j3), zza(i19));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 10:
                    if ((i22 & i) != 0) {
                        zzb2 = zzii.zzc(i23, (zzht) unsafe2.getObject(t, j3));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 11:
                    if ((i22 & i) != 0) {
                        zzb2 = zzii.zzg(i23, unsafe2.getInt(t, j3));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 12:
                    if ((i22 & i) != 0) {
                        zzb2 = zzii.zzk(i23, unsafe2.getInt(t, j3));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 13:
                    if ((i22 & i) != 0) {
                        i3 = zzii.zzj(i23, 0);
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 14:
                    if ((i22 & i) != 0) {
                        zzb2 = zzii.zzh(i23, 0L);
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 15:
                    if ((i22 & i) != 0) {
                        zzb2 = zzii.zzh(i23, unsafe2.getInt(t, j3));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 16:
                    if ((i22 & i) != 0) {
                        zzb2 = zzii.zzf(i23, unsafe2.getLong(t, j3));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 17:
                    if ((i22 & i) != 0) {
                        zzb2 = zzii.zzc(i23, (zzkk) unsafe2.getObject(t, j3), zza(i19));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case R.styleable.MapAttrs_uiScrollGesturesDuringRotateOrZoom /* 18 */:
                    zzb2 = zzle.zzi(i23, (List) unsafe2.getObject(t, j3), false);
                    i20 += zzb2;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case R.styleable.MapAttrs_uiTiltGestures /* 19 */:
                    i4 = zzle.zzh(i23, (List) unsafe2.getObject(t, j3), false);
                    i20 += i4;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case R.styleable.MapAttrs_uiZoomControls /* 20 */:
                    i4 = zzle.zza(i23, (List<Long>) unsafe2.getObject(t, j3), false);
                    i20 += i4;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case R.styleable.MapAttrs_uiZoomGestures /* 21 */:
                    i4 = zzle.zzb(i23, (List<Long>) unsafe2.getObject(t, j3), false);
                    i20 += i4;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case R.styleable.MapAttrs_useViewLifecycle /* 22 */:
                    i4 = zzle.zze(i23, (List) unsafe2.getObject(t, j3), false);
                    i20 += i4;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case R.styleable.MapAttrs_zOrderOnTop /* 23 */:
                    i4 = zzle.zzi(i23, (List) unsafe2.getObject(t, j3), false);
                    i20 += i4;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 24:
                    i4 = zzle.zzh(i23, (List) unsafe2.getObject(t, j3), false);
                    i20 += i4;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 25:
                    i4 = zzle.zzj(i23, (List) unsafe2.getObject(t, j3), false);
                    i20 += i4;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 26:
                    zzb2 = zzle.zza(i23, (List) unsafe2.getObject(t, j3));
                    i20 += zzb2;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 27:
                    zzb2 = zzle.zza(i23, (List<?>) unsafe2.getObject(t, j3), zza(i19));
                    i20 += zzb2;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 28:
                    zzb2 = zzle.zzb(i23, (List) unsafe2.getObject(t, j3));
                    i20 += zzb2;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 29:
                    zzb2 = zzle.zzf(i23, (List) unsafe2.getObject(t, j3), false);
                    i20 += zzb2;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 30:
                    i4 = zzle.zzd(i23, (List) unsafe2.getObject(t, j3), false);
                    i20 += i4;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 31:
                    i4 = zzle.zzh(i23, (List) unsafe2.getObject(t, j3), false);
                    i20 += i4;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case ConnectionsManager.RequestFlagForceDownload /* 32 */:
                    i4 = zzle.zzi(i23, (List) unsafe2.getObject(t, j3), false);
                    i20 += i4;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 33:
                    i4 = zzle.zzg(i23, (List) unsafe2.getObject(t, j3), false);
                    i20 += i4;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 34:
                    i4 = zzle.zzc(i23, (List) unsafe2.getObject(t, j3), false);
                    i20 += i4;
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 35:
                    i7 = zzle.zzi((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzii.zze(i23);
                        i5 = zzii.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 36:
                    i7 = zzle.zzh((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzii.zze(i23);
                        i5 = zzii.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 37:
                    i7 = zzle.zza((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzii.zze(i23);
                        i5 = zzii.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 38:
                    i7 = zzle.zzb((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzii.zze(i23);
                        i5 = zzii.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 39:
                    i7 = zzle.zze((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzii.zze(i23);
                        i5 = zzii.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 40:
                    i7 = zzle.zzi((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzii.zze(i23);
                        i5 = zzii.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 41:
                    i7 = zzle.zzh((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzii.zze(i23);
                        i5 = zzii.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 42:
                    i7 = zzle.zzj((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzii.zze(i23);
                        i5 = zzii.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 43:
                    i7 = zzle.zzf((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzii.zze(i23);
                        i5 = zzii.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 44:
                    i7 = zzle.zzd((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzii.zze(i23);
                        i5 = zzii.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 45:
                    i7 = zzle.zzh((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzii.zze(i23);
                        i5 = zzii.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 46:
                    i7 = zzle.zzi((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzii.zze(i23);
                        i5 = zzii.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 47:
                    i7 = zzle.zzg((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzii.zze(i23);
                        i5 = zzii.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 48:
                    i7 = zzle.zzc((List) unsafe2.getObject(t, j3));
                    if (i7 > 0) {
                        i6 = zzii.zze(i23);
                        i5 = zzii.zzg(i7);
                        i3 = i6 + i5 + i7;
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 49:
                    zzb2 = zzle.zzb(i23, (List) unsafe2.getObject(t, j3), zza(i19));
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
                    if (zza((zzko<T>) t, i23, i19)) {
                        zzb2 = zzii.zzb(i23, 0.0d);
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 52:
                    if (zza((zzko<T>) t, i23, i19)) {
                        i3 = zzii.zzb(i23, 0.0f);
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 53:
                    if (zza((zzko<T>) t, i23, i19)) {
                        zzb2 = zzii.zzd(i23, zze(t, j3));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 54:
                    if (zza((zzko<T>) t, i23, i19)) {
                        zzb2 = zzii.zze(i23, zze(t, j3));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 55:
                    if (zza((zzko<T>) t, i23, i19)) {
                        zzb2 = zzii.zzf(i23, zzd(t, j3));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 56:
                    if (zza((zzko<T>) t, i23, i19)) {
                        zzb2 = zzii.zzg(i23, 0L);
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 57:
                    if (zza((zzko<T>) t, i23, i19)) {
                        i3 = zzii.zzi(i23, 0);
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 58:
                    if (zza((zzko<T>) t, i23, i19)) {
                        i3 = zzii.zzb(i23, true);
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 59:
                    if (zza((zzko<T>) t, i23, i19)) {
                        Object object2 = unsafe2.getObject(t, j3);
                        if (object2 instanceof zzht) {
                            zzb2 = zzii.zzc(i23, (zzht) object2);
                        } else {
                            zzb2 = zzii.zzb(i23, (String) object2);
                        }
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 60:
                    if (zza((zzko<T>) t, i23, i19)) {
                        zzb2 = zzle.zza(i23, unsafe2.getObject(t, j3), zza(i19));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 61:
                    if (zza((zzko<T>) t, i23, i19)) {
                        zzb2 = zzii.zzc(i23, (zzht) unsafe2.getObject(t, j3));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 62:
                    if (zza((zzko<T>) t, i23, i19)) {
                        zzb2 = zzii.zzg(i23, zzd(t, j3));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 63:
                    if (zza((zzko<T>) t, i23, i19)) {
                        zzb2 = zzii.zzk(i23, zzd(t, j3));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 64:
                    if (zza((zzko<T>) t, i23, i19)) {
                        i3 = zzii.zzj(i23, 0);
                        i20 += i3;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case VoIPService.CALL_MIN_LAYER /* 65 */:
                    if (zza((zzko<T>) t, i23, i19)) {
                        zzb2 = zzii.zzh(i23, 0L);
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 66:
                    if (zza((zzko<T>) t, i23, i19)) {
                        zzb2 = zzii.zzh(i23, zzd(t, j3));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 67:
                    if (zza((zzko<T>) t, i23, i19)) {
                        zzb2 = zzii.zzf(i23, zze(t, j3));
                        i20 += zzb2;
                    }
                    j = 0;
                    i19 += 3;
                    i12 = 1048575;
                    i13 = 1;
                case 68:
                    if (zza((zzko<T>) t, i23, i19)) {
                        zzb2 = zzii.zzc(i23, (zzkk) unsafe2.getObject(t, j3), zza(i19));
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
        int zza2 = i20 + zza((zzlu) this.zzq, (Object) t);
        if (!this.zzh) {
            return zza2;
        }
        zziu<?> zza3 = this.zzr.zza(t);
        for (int i28 = 0; i28 < zza3.zza.zzc(); i28++) {
            Map.Entry<?, Object> zzb4 = zza3.zza.zzb(i28);
            i27 += zziu.zzc((zziw) zzb4.getKey(), zzb4.getValue());
        }
        for (Map.Entry<?, Object> entry : zza3.zza.zzd()) {
            i27 += zziu.zzc((zziw) entry.getKey(), entry.getValue());
        }
        return zza2 + i27;
    }

    private static <UT, UB> int zza(zzlu<UT, UB> zzluVar, T t) {
        return zzluVar.zzf(zzluVar.zzb(t));
    }

    private static List<?> zza(Object obj, long j) {
        return (List) zzma.zzf(obj, j);
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x003b  */
    /* JADX WARN: Removed duplicated region for block: B:165:0x0513  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x0552  */
    /* JADX WARN: Removed duplicated region for block: B:333:0x0a2a  */
    @Override // com.google.android.gms.internal.vision.zzlc
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void zza(T t, zzmr zzmrVar) throws IOException {
        Map.Entry<?, Object> entry;
        Iterator<Map.Entry<?, Object>> it;
        int length;
        int i;
        Map.Entry<?, Object> entry2;
        Iterator<Map.Entry<?, Object>> it2;
        int length2;
        if (zzmrVar.zza() == zzmq.zzb) {
            zza(this.zzq, t, zzmrVar);
            if (this.zzh) {
                zziu<?> zza2 = this.zzr.zza(t);
                if (!zza2.zza.isEmpty()) {
                    it2 = zza2.zze();
                    entry2 = it2.next();
                    for (length2 = this.zzc.length - 3; length2 >= 0; length2 -= 3) {
                        int zzd = zzd(length2);
                        int i2 = this.zzc[length2];
                        while (entry2 != null && this.zzr.zza(entry2) > i2) {
                            this.zzr.zza(zzmrVar, entry2);
                            entry2 = it2.hasNext() ? it2.next() : null;
                        }
                        switch ((zzd & 267386880) >>> 20) {
                            case 0:
                                if (zza((zzko<T>) t, length2)) {
                                    zzmrVar.zza(i2, zzma.zze(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 1:
                                if (zza((zzko<T>) t, length2)) {
                                    zzmrVar.zza(i2, zzma.zzd(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 2:
                                if (zza((zzko<T>) t, length2)) {
                                    zzmrVar.zza(i2, zzma.zzb(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 3:
                                if (zza((zzko<T>) t, length2)) {
                                    zzmrVar.zzc(i2, zzma.zzb(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 4:
                                if (zza((zzko<T>) t, length2)) {
                                    zzmrVar.zzc(i2, zzma.zza(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 5:
                                if (zza((zzko<T>) t, length2)) {
                                    zzmrVar.zzd(i2, zzma.zzb(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 6:
                                if (zza((zzko<T>) t, length2)) {
                                    zzmrVar.zzd(i2, zzma.zza(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 7:
                                if (zza((zzko<T>) t, length2)) {
                                    zzmrVar.zza(i2, zzma.zzc(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 8:
                                if (zza((zzko<T>) t, length2)) {
                                    zza(i2, zzma.zzf(t, zzd & 1048575), zzmrVar);
                                    break;
                                } else {
                                    break;
                                }
                            case 9:
                                if (zza((zzko<T>) t, length2)) {
                                    zzmrVar.zza(i2, zzma.zzf(t, zzd & 1048575), zza(length2));
                                    break;
                                } else {
                                    break;
                                }
                            case 10:
                                if (zza((zzko<T>) t, length2)) {
                                    zzmrVar.zza(i2, (zzht) zzma.zzf(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 11:
                                if (zza((zzko<T>) t, length2)) {
                                    zzmrVar.zze(i2, zzma.zza(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 12:
                                if (zza((zzko<T>) t, length2)) {
                                    zzmrVar.zzb(i2, zzma.zza(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 13:
                                if (zza((zzko<T>) t, length2)) {
                                    zzmrVar.zza(i2, zzma.zza(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 14:
                                if (zza((zzko<T>) t, length2)) {
                                    zzmrVar.zzb(i2, zzma.zzb(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 15:
                                if (zza((zzko<T>) t, length2)) {
                                    zzmrVar.zzf(i2, zzma.zza(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 16:
                                if (zza((zzko<T>) t, length2)) {
                                    zzmrVar.zze(i2, zzma.zzb(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 17:
                                if (zza((zzko<T>) t, length2)) {
                                    zzmrVar.zzb(i2, zzma.zzf(t, zzd & 1048575), zza(length2));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.MapAttrs_uiScrollGesturesDuringRotateOrZoom /* 18 */:
                                zzle.zza(this.zzc[length2], (List<Double>) zzma.zzf(t, zzd & 1048575), zzmrVar, false);
                                break;
                            case R.styleable.MapAttrs_uiTiltGestures /* 19 */:
                                zzle.zzb(this.zzc[length2], (List<Float>) zzma.zzf(t, zzd & 1048575), zzmrVar, false);
                                break;
                            case R.styleable.MapAttrs_uiZoomControls /* 20 */:
                                zzle.zzc(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, false);
                                break;
                            case R.styleable.MapAttrs_uiZoomGestures /* 21 */:
                                zzle.zzd(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, false);
                                break;
                            case R.styleable.MapAttrs_useViewLifecycle /* 22 */:
                                zzle.zzh(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, false);
                                break;
                            case R.styleable.MapAttrs_zOrderOnTop /* 23 */:
                                zzle.zzf(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, false);
                                break;
                            case 24:
                                zzle.zzk(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, false);
                                break;
                            case 25:
                                zzle.zzn(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, false);
                                break;
                            case 26:
                                zzle.zza(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar);
                                break;
                            case 27:
                                zzle.zza(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, zza(length2));
                                break;
                            case 28:
                                zzle.zzb(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar);
                                break;
                            case 29:
                                zzle.zzi(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, false);
                                break;
                            case 30:
                                zzle.zzm(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, false);
                                break;
                            case 31:
                                zzle.zzl(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, false);
                                break;
                            case ConnectionsManager.RequestFlagForceDownload /* 32 */:
                                zzle.zzg(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, false);
                                break;
                            case 33:
                                zzle.zzj(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, false);
                                break;
                            case 34:
                                zzle.zze(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, false);
                                break;
                            case 35:
                                zzle.zza(this.zzc[length2], (List<Double>) zzma.zzf(t, zzd & 1048575), zzmrVar, true);
                                break;
                            case 36:
                                zzle.zzb(this.zzc[length2], (List<Float>) zzma.zzf(t, zzd & 1048575), zzmrVar, true);
                                break;
                            case 37:
                                zzle.zzc(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, true);
                                break;
                            case 38:
                                zzle.zzd(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, true);
                                break;
                            case 39:
                                zzle.zzh(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, true);
                                break;
                            case 40:
                                zzle.zzf(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, true);
                                break;
                            case 41:
                                zzle.zzk(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, true);
                                break;
                            case 42:
                                zzle.zzn(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, true);
                                break;
                            case 43:
                                zzle.zzi(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, true);
                                break;
                            case 44:
                                zzle.zzm(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, true);
                                break;
                            case 45:
                                zzle.zzl(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, true);
                                break;
                            case 46:
                                zzle.zzg(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, true);
                                break;
                            case 47:
                                zzle.zzj(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, true);
                                break;
                            case 48:
                                zzle.zze(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, true);
                                break;
                            case 49:
                                zzle.zzb(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, zza(length2));
                                break;
                            case 50:
                                zza(zzmrVar, i2, zzma.zzf(t, zzd & 1048575), length2);
                                break;
                            case 51:
                                if (zza((zzko<T>) t, i2, length2)) {
                                    zzmrVar.zza(i2, zzb(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 52:
                                if (zza((zzko<T>) t, i2, length2)) {
                                    zzmrVar.zza(i2, zzc(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 53:
                                if (zza((zzko<T>) t, i2, length2)) {
                                    zzmrVar.zza(i2, zze(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 54:
                                if (zza((zzko<T>) t, i2, length2)) {
                                    zzmrVar.zzc(i2, zze(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 55:
                                if (zza((zzko<T>) t, i2, length2)) {
                                    zzmrVar.zzc(i2, zzd(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 56:
                                if (zza((zzko<T>) t, i2, length2)) {
                                    zzmrVar.zzd(i2, zze(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 57:
                                if (zza((zzko<T>) t, i2, length2)) {
                                    zzmrVar.zzd(i2, zzd(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 58:
                                if (zza((zzko<T>) t, i2, length2)) {
                                    zzmrVar.zza(i2, zzf(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 59:
                                if (zza((zzko<T>) t, i2, length2)) {
                                    zza(i2, zzma.zzf(t, zzd & 1048575), zzmrVar);
                                    break;
                                } else {
                                    break;
                                }
                            case 60:
                                if (zza((zzko<T>) t, i2, length2)) {
                                    zzmrVar.zza(i2, zzma.zzf(t, zzd & 1048575), zza(length2));
                                    break;
                                } else {
                                    break;
                                }
                            case 61:
                                if (zza((zzko<T>) t, i2, length2)) {
                                    zzmrVar.zza(i2, (zzht) zzma.zzf(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 62:
                                if (zza((zzko<T>) t, i2, length2)) {
                                    zzmrVar.zze(i2, zzd(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 63:
                                if (zza((zzko<T>) t, i2, length2)) {
                                    zzmrVar.zzb(i2, zzd(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 64:
                                if (zza((zzko<T>) t, i2, length2)) {
                                    zzmrVar.zza(i2, zzd(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case VoIPService.CALL_MIN_LAYER /* 65 */:
                                if (zza((zzko<T>) t, i2, length2)) {
                                    zzmrVar.zzb(i2, zze(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 66:
                                if (zza((zzko<T>) t, i2, length2)) {
                                    zzmrVar.zzf(i2, zzd(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 67:
                                if (zza((zzko<T>) t, i2, length2)) {
                                    zzmrVar.zze(i2, zze(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 68:
                                if (zza((zzko<T>) t, i2, length2)) {
                                    zzmrVar.zzb(i2, zzma.zzf(t, zzd & 1048575), zza(length2));
                                    break;
                                } else {
                                    break;
                                }
                        }
                    }
                    while (entry2 != null) {
                        this.zzr.zza(zzmrVar, entry2);
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
                zziu<?> zza3 = this.zzr.zza(t);
                if (!zza3.zza.isEmpty()) {
                    it = zza3.zzd();
                    entry = it.next();
                    length = this.zzc.length;
                    for (i = 0; i < length; i += 3) {
                        int zzd2 = zzd(i);
                        int i3 = this.zzc[i];
                        while (entry != null && this.zzr.zza(entry) <= i3) {
                            this.zzr.zza(zzmrVar, entry);
                            entry = it.hasNext() ? it.next() : null;
                        }
                        switch ((zzd2 & 267386880) >>> 20) {
                            case 0:
                                if (zza((zzko<T>) t, i)) {
                                    zzmrVar.zza(i3, zzma.zze(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 1:
                                if (zza((zzko<T>) t, i)) {
                                    zzmrVar.zza(i3, zzma.zzd(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 2:
                                if (zza((zzko<T>) t, i)) {
                                    zzmrVar.zza(i3, zzma.zzb(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 3:
                                if (zza((zzko<T>) t, i)) {
                                    zzmrVar.zzc(i3, zzma.zzb(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 4:
                                if (zza((zzko<T>) t, i)) {
                                    zzmrVar.zzc(i3, zzma.zza(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 5:
                                if (zza((zzko<T>) t, i)) {
                                    zzmrVar.zzd(i3, zzma.zzb(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 6:
                                if (zza((zzko<T>) t, i)) {
                                    zzmrVar.zzd(i3, zzma.zza(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 7:
                                if (zza((zzko<T>) t, i)) {
                                    zzmrVar.zza(i3, zzma.zzc(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 8:
                                if (zza((zzko<T>) t, i)) {
                                    zza(i3, zzma.zzf(t, zzd2 & 1048575), zzmrVar);
                                    break;
                                } else {
                                    break;
                                }
                            case 9:
                                if (zza((zzko<T>) t, i)) {
                                    zzmrVar.zza(i3, zzma.zzf(t, zzd2 & 1048575), zza(i));
                                    break;
                                } else {
                                    break;
                                }
                            case 10:
                                if (zza((zzko<T>) t, i)) {
                                    zzmrVar.zza(i3, (zzht) zzma.zzf(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 11:
                                if (zza((zzko<T>) t, i)) {
                                    zzmrVar.zze(i3, zzma.zza(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 12:
                                if (zza((zzko<T>) t, i)) {
                                    zzmrVar.zzb(i3, zzma.zza(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 13:
                                if (zza((zzko<T>) t, i)) {
                                    zzmrVar.zza(i3, zzma.zza(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 14:
                                if (zza((zzko<T>) t, i)) {
                                    zzmrVar.zzb(i3, zzma.zzb(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 15:
                                if (zza((zzko<T>) t, i)) {
                                    zzmrVar.zzf(i3, zzma.zza(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 16:
                                if (zza((zzko<T>) t, i)) {
                                    zzmrVar.zze(i3, zzma.zzb(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 17:
                                if (zza((zzko<T>) t, i)) {
                                    zzmrVar.zzb(i3, zzma.zzf(t, zzd2 & 1048575), zza(i));
                                    break;
                                } else {
                                    break;
                                }
                            case R.styleable.MapAttrs_uiScrollGesturesDuringRotateOrZoom /* 18 */:
                                zzle.zza(this.zzc[i], (List<Double>) zzma.zzf(t, zzd2 & 1048575), zzmrVar, false);
                                break;
                            case R.styleable.MapAttrs_uiTiltGestures /* 19 */:
                                zzle.zzb(this.zzc[i], (List<Float>) zzma.zzf(t, zzd2 & 1048575), zzmrVar, false);
                                break;
                            case R.styleable.MapAttrs_uiZoomControls /* 20 */:
                                zzle.zzc(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, false);
                                break;
                            case R.styleable.MapAttrs_uiZoomGestures /* 21 */:
                                zzle.zzd(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, false);
                                break;
                            case R.styleable.MapAttrs_useViewLifecycle /* 22 */:
                                zzle.zzh(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, false);
                                break;
                            case R.styleable.MapAttrs_zOrderOnTop /* 23 */:
                                zzle.zzf(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, false);
                                break;
                            case 24:
                                zzle.zzk(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, false);
                                break;
                            case 25:
                                zzle.zzn(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, false);
                                break;
                            case 26:
                                zzle.zza(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar);
                                break;
                            case 27:
                                zzle.zza(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, zza(i));
                                break;
                            case 28:
                                zzle.zzb(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar);
                                break;
                            case 29:
                                zzle.zzi(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, false);
                                break;
                            case 30:
                                zzle.zzm(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, false);
                                break;
                            case 31:
                                zzle.zzl(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, false);
                                break;
                            case ConnectionsManager.RequestFlagForceDownload /* 32 */:
                                zzle.zzg(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, false);
                                break;
                            case 33:
                                zzle.zzj(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, false);
                                break;
                            case 34:
                                zzle.zze(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, false);
                                break;
                            case 35:
                                zzle.zza(this.zzc[i], (List<Double>) zzma.zzf(t, zzd2 & 1048575), zzmrVar, true);
                                break;
                            case 36:
                                zzle.zzb(this.zzc[i], (List<Float>) zzma.zzf(t, zzd2 & 1048575), zzmrVar, true);
                                break;
                            case 37:
                                zzle.zzc(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, true);
                                break;
                            case 38:
                                zzle.zzd(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, true);
                                break;
                            case 39:
                                zzle.zzh(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, true);
                                break;
                            case 40:
                                zzle.zzf(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, true);
                                break;
                            case 41:
                                zzle.zzk(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, true);
                                break;
                            case 42:
                                zzle.zzn(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, true);
                                break;
                            case 43:
                                zzle.zzi(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, true);
                                break;
                            case 44:
                                zzle.zzm(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, true);
                                break;
                            case 45:
                                zzle.zzl(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, true);
                                break;
                            case 46:
                                zzle.zzg(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, true);
                                break;
                            case 47:
                                zzle.zzj(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, true);
                                break;
                            case 48:
                                zzle.zze(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, true);
                                break;
                            case 49:
                                zzle.zzb(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, zza(i));
                                break;
                            case 50:
                                zza(zzmrVar, i3, zzma.zzf(t, zzd2 & 1048575), i);
                                break;
                            case 51:
                                if (zza((zzko<T>) t, i3, i)) {
                                    zzmrVar.zza(i3, zzb(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 52:
                                if (zza((zzko<T>) t, i3, i)) {
                                    zzmrVar.zza(i3, zzc(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 53:
                                if (zza((zzko<T>) t, i3, i)) {
                                    zzmrVar.zza(i3, zze(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 54:
                                if (zza((zzko<T>) t, i3, i)) {
                                    zzmrVar.zzc(i3, zze(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 55:
                                if (zza((zzko<T>) t, i3, i)) {
                                    zzmrVar.zzc(i3, zzd(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 56:
                                if (zza((zzko<T>) t, i3, i)) {
                                    zzmrVar.zzd(i3, zze(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 57:
                                if (zza((zzko<T>) t, i3, i)) {
                                    zzmrVar.zzd(i3, zzd(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 58:
                                if (zza((zzko<T>) t, i3, i)) {
                                    zzmrVar.zza(i3, zzf(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 59:
                                if (zza((zzko<T>) t, i3, i)) {
                                    zza(i3, zzma.zzf(t, zzd2 & 1048575), zzmrVar);
                                    break;
                                } else {
                                    break;
                                }
                            case 60:
                                if (zza((zzko<T>) t, i3, i)) {
                                    zzmrVar.zza(i3, zzma.zzf(t, zzd2 & 1048575), zza(i));
                                    break;
                                } else {
                                    break;
                                }
                            case 61:
                                if (zza((zzko<T>) t, i3, i)) {
                                    zzmrVar.zza(i3, (zzht) zzma.zzf(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 62:
                                if (zza((zzko<T>) t, i3, i)) {
                                    zzmrVar.zze(i3, zzd(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 63:
                                if (zza((zzko<T>) t, i3, i)) {
                                    zzmrVar.zzb(i3, zzd(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 64:
                                if (zza((zzko<T>) t, i3, i)) {
                                    zzmrVar.zza(i3, zzd(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case VoIPService.CALL_MIN_LAYER /* 65 */:
                                if (zza((zzko<T>) t, i3, i)) {
                                    zzmrVar.zzb(i3, zze(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 66:
                                if (zza((zzko<T>) t, i3, i)) {
                                    zzmrVar.zzf(i3, zzd(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 67:
                                if (zza((zzko<T>) t, i3, i)) {
                                    zzmrVar.zze(i3, zze(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case 68:
                                if (zza((zzko<T>) t, i3, i)) {
                                    zzmrVar.zzb(i3, zzma.zzf(t, zzd2 & 1048575), zza(i));
                                    break;
                                } else {
                                    break;
                                }
                        }
                    }
                    while (entry != null) {
                        this.zzr.zza(zzmrVar, entry);
                        entry = it.hasNext() ? it.next() : null;
                    }
                    zza(this.zzq, t, zzmrVar);
                }
            }
            it = null;
            entry = null;
            length = this.zzc.length;
            while (i < length) {
            }
            while (entry != null) {
            }
            zza(this.zzq, t, zzmrVar);
        } else {
            zzb((zzko<T>) t, zzmrVar);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0031  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x0491  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void zzb(T t, zzmr zzmrVar) throws IOException {
        Map.Entry<?, Object> entry;
        Iterator<Map.Entry<?, Object>> it;
        int length;
        int i;
        int i2;
        if (this.zzh) {
            zziu<?> zza2 = this.zzr.zza(t);
            if (!zza2.zza.isEmpty()) {
                it = zza2.zzd();
                entry = it.next();
                length = this.zzc.length;
                Unsafe unsafe = zzb;
                int i3 = 1048575;
                int i4 = 0;
                for (i = 0; i < length; i += 3) {
                    int zzd = zzd(i);
                    int[] iArr = this.zzc;
                    int i5 = iArr[i];
                    int i6 = (zzd & 267386880) >>> 20;
                    if (i6 <= 17) {
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
                        this.zzr.zza(zzmrVar, entry);
                        entry = it.hasNext() ? it.next() : null;
                    }
                    long j = zzd & 1048575;
                    switch (i6) {
                        case 0:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zza(i5, zzma.zze(t, j));
                                continue;
                            }
                        case 1:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zza(i5, zzma.zzd(t, j));
                            } else {
                                continue;
                            }
                        case 2:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zza(i5, unsafe.getLong(t, j));
                            } else {
                                continue;
                            }
                        case 3:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zzc(i5, unsafe.getLong(t, j));
                            } else {
                                continue;
                            }
                        case 4:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zzc(i5, unsafe.getInt(t, j));
                            } else {
                                continue;
                            }
                        case 5:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zzd(i5, unsafe.getLong(t, j));
                            } else {
                                continue;
                            }
                        case 6:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zzd(i5, unsafe.getInt(t, j));
                            } else {
                                continue;
                            }
                        case 7:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zza(i5, zzma.zzc(t, j));
                            } else {
                                continue;
                            }
                        case 8:
                            if ((i2 & i4) != 0) {
                                zza(i5, unsafe.getObject(t, j), zzmrVar);
                            } else {
                                continue;
                            }
                        case 9:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zza(i5, unsafe.getObject(t, j), zza(i));
                            } else {
                                continue;
                            }
                        case 10:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zza(i5, (zzht) unsafe.getObject(t, j));
                            } else {
                                continue;
                            }
                        case 11:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zze(i5, unsafe.getInt(t, j));
                            } else {
                                continue;
                            }
                        case 12:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zzb(i5, unsafe.getInt(t, j));
                            } else {
                                continue;
                            }
                        case 13:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zza(i5, unsafe.getInt(t, j));
                            } else {
                                continue;
                            }
                        case 14:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zzb(i5, unsafe.getLong(t, j));
                            } else {
                                continue;
                            }
                        case 15:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zzf(i5, unsafe.getInt(t, j));
                            } else {
                                continue;
                            }
                        case 16:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zze(i5, unsafe.getLong(t, j));
                            } else {
                                continue;
                            }
                        case 17:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zzb(i5, unsafe.getObject(t, j), zza(i));
                            } else {
                                continue;
                            }
                        case R.styleable.MapAttrs_uiScrollGesturesDuringRotateOrZoom /* 18 */:
                            zzle.zza(this.zzc[i], (List<Double>) unsafe.getObject(t, j), zzmrVar, false);
                            break;
                        case R.styleable.MapAttrs_uiTiltGestures /* 19 */:
                            zzle.zzb(this.zzc[i], (List<Float>) unsafe.getObject(t, j), zzmrVar, false);
                            break;
                        case R.styleable.MapAttrs_uiZoomControls /* 20 */:
                            zzle.zzc(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, false);
                            break;
                        case R.styleable.MapAttrs_uiZoomGestures /* 21 */:
                            zzle.zzd(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, false);
                            break;
                        case R.styleable.MapAttrs_useViewLifecycle /* 22 */:
                            zzle.zzh(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, false);
                            break;
                        case R.styleable.MapAttrs_zOrderOnTop /* 23 */:
                            zzle.zzf(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, false);
                            break;
                        case 24:
                            zzle.zzk(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, false);
                            break;
                        case 25:
                            zzle.zzn(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, false);
                            break;
                        case 26:
                            zzle.zza(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar);
                            break;
                        case 27:
                            zzle.zza(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, zza(i));
                            break;
                        case 28:
                            zzle.zzb(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar);
                            break;
                        case 29:
                            zzle.zzi(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, false);
                            break;
                        case 30:
                            zzle.zzm(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, false);
                            break;
                        case 31:
                            zzle.zzl(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, false);
                            break;
                        case ConnectionsManager.RequestFlagForceDownload /* 32 */:
                            zzle.zzg(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, false);
                            break;
                        case 33:
                            zzle.zzj(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, false);
                            break;
                        case 34:
                            zzle.zze(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, false);
                            break;
                        case 35:
                            zzle.zza(this.zzc[i], (List<Double>) unsafe.getObject(t, j), zzmrVar, true);
                            break;
                        case 36:
                            zzle.zzb(this.zzc[i], (List<Float>) unsafe.getObject(t, j), zzmrVar, true);
                            break;
                        case 37:
                            zzle.zzc(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, true);
                            break;
                        case 38:
                            zzle.zzd(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, true);
                            break;
                        case 39:
                            zzle.zzh(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, true);
                            break;
                        case 40:
                            zzle.zzf(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, true);
                            break;
                        case 41:
                            zzle.zzk(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, true);
                            break;
                        case 42:
                            zzle.zzn(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, true);
                            break;
                        case 43:
                            zzle.zzi(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, true);
                            break;
                        case 44:
                            zzle.zzm(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, true);
                            break;
                        case 45:
                            zzle.zzl(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, true);
                            break;
                        case 46:
                            zzle.zzg(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, true);
                            break;
                        case 47:
                            zzle.zzj(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, true);
                            break;
                        case 48:
                            zzle.zze(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, true);
                            break;
                        case 49:
                            zzle.zzb(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, zza(i));
                            break;
                        case 50:
                            zza(zzmrVar, i5, unsafe.getObject(t, j), i);
                            break;
                        case 51:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zza(i5, zzb(t, j));
                                break;
                            }
                            break;
                        case 52:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zza(i5, zzc(t, j));
                                break;
                            }
                            break;
                        case 53:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zza(i5, zze(t, j));
                                break;
                            }
                            break;
                        case 54:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zzc(i5, zze(t, j));
                                break;
                            }
                            break;
                        case 55:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zzc(i5, zzd(t, j));
                                break;
                            }
                            break;
                        case 56:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zzd(i5, zze(t, j));
                                break;
                            }
                            break;
                        case 57:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zzd(i5, zzd(t, j));
                                break;
                            }
                            break;
                        case 58:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zza(i5, zzf(t, j));
                                break;
                            }
                            break;
                        case 59:
                            if (zza((zzko<T>) t, i5, i)) {
                                zza(i5, unsafe.getObject(t, j), zzmrVar);
                                break;
                            }
                            break;
                        case 60:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zza(i5, unsafe.getObject(t, j), zza(i));
                                break;
                            }
                            break;
                        case 61:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zza(i5, (zzht) unsafe.getObject(t, j));
                                break;
                            }
                            break;
                        case 62:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zze(i5, zzd(t, j));
                                break;
                            }
                            break;
                        case 63:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zzb(i5, zzd(t, j));
                                break;
                            }
                            break;
                        case 64:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zza(i5, zzd(t, j));
                                break;
                            }
                            break;
                        case VoIPService.CALL_MIN_LAYER /* 65 */:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zzb(i5, zze(t, j));
                                break;
                            }
                            break;
                        case 66:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zzf(i5, zzd(t, j));
                                break;
                            }
                            break;
                        case 67:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zze(i5, zze(t, j));
                                break;
                            }
                            break;
                        case 68:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zzb(i5, unsafe.getObject(t, j), zza(i));
                                break;
                            }
                            break;
                    }
                }
                while (entry != null) {
                    this.zzr.zza(zzmrVar, entry);
                    entry = it.hasNext() ? it.next() : null;
                }
                zza(this.zzq, t, zzmrVar);
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
        zza(this.zzq, t, zzmrVar);
    }

    private final <K, V> void zza(zzmr zzmrVar, int i, Object obj, int i2) throws IOException {
        if (obj != null) {
            zzmrVar.zza(i, this.zzs.zzb(zzb(i2)), this.zzs.zzc(obj));
        }
    }

    private static <UT, UB> void zza(zzlu<UT, UB> zzluVar, T t, zzmr zzmrVar) throws IOException {
        zzluVar.zza((zzlu<UT, UB>) zzluVar.zzb(t), zzmrVar);
    }

    private static zzlx zze(Object obj) {
        zzjb zzjbVar = (zzjb) obj;
        zzlx zzlxVar = zzjbVar.zzb;
        if (zzlxVar == zzlx.zza()) {
            zzlx zzb2 = zzlx.zzb();
            zzjbVar.zzb = zzb2;
            return zzb2;
        }
        return zzlxVar;
    }

    /* JADX WARN: Removed duplicated region for block: B:114:0x0236  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x016e  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x01e8  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:112:0x0233 -> B:113:0x0234). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:63:0x016b -> B:64:0x016c). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:92:0x01e5 -> B:93:0x01e6). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final int zza(T t, byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6, long j, int i7, long j2, zzhn zzhnVar) throws IOException {
        int zza2;
        int i8;
        int i9 = i;
        Unsafe unsafe = zzb;
        zzjl zzjlVar = (zzjl) unsafe.getObject(t, j2);
        if (!zzjlVar.zza()) {
            int size = zzjlVar.size();
            zzjlVar = zzjlVar.zza(size == 0 ? 10 : size << 1);
            unsafe.putObject(t, j2, zzjlVar);
        }
        switch (i7) {
            case R.styleable.MapAttrs_uiScrollGesturesDuringRotateOrZoom /* 18 */:
            case 35:
                if (i5 == 2) {
                    zzin zzinVar = (zzin) zzjlVar;
                    int zza3 = zzhl.zza(bArr, i9, zzhnVar);
                    int i10 = zzhnVar.zza + zza3;
                    while (zza3 < i10) {
                        zzinVar.zza(zzhl.zzc(bArr, zza3));
                        zza3 += 8;
                    }
                    if (zza3 != i10) {
                        throw zzjk.zza();
                    }
                    return zza3;
                }
                if (i5 == 1) {
                    zzin zzinVar2 = (zzin) zzjlVar;
                    zzinVar2.zza(zzhl.zzc(bArr, i));
                    while (true) {
                        int i11 = i9 + 8;
                        if (i11 >= i2) {
                            return i11;
                        }
                        i9 = zzhl.zza(bArr, i11, zzhnVar);
                        if (i3 != zzhnVar.zza) {
                            return i11;
                        }
                        zzinVar2.zza(zzhl.zzc(bArr, i9));
                    }
                }
                return i9;
            case R.styleable.MapAttrs_uiTiltGestures /* 19 */:
            case 36:
                if (i5 == 2) {
                    zzja zzjaVar = (zzja) zzjlVar;
                    int zza4 = zzhl.zza(bArr, i9, zzhnVar);
                    int i12 = zzhnVar.zza + zza4;
                    while (zza4 < i12) {
                        zzjaVar.zza(zzhl.zzd(bArr, zza4));
                        zza4 += 4;
                    }
                    if (zza4 != i12) {
                        throw zzjk.zza();
                    }
                    return zza4;
                }
                if (i5 == 5) {
                    zzja zzjaVar2 = (zzja) zzjlVar;
                    zzjaVar2.zza(zzhl.zzd(bArr, i));
                    while (true) {
                        int i13 = i9 + 4;
                        if (i13 >= i2) {
                            return i13;
                        }
                        i9 = zzhl.zza(bArr, i13, zzhnVar);
                        if (i3 != zzhnVar.zza) {
                            return i13;
                        }
                        zzjaVar2.zza(zzhl.zzd(bArr, i9));
                    }
                }
                return i9;
            case R.styleable.MapAttrs_uiZoomControls /* 20 */:
            case R.styleable.MapAttrs_uiZoomGestures /* 21 */:
            case 37:
            case 38:
                if (i5 == 2) {
                    zzjy zzjyVar = (zzjy) zzjlVar;
                    int zza5 = zzhl.zza(bArr, i9, zzhnVar);
                    int i14 = zzhnVar.zza + zza5;
                    while (zza5 < i14) {
                        zza5 = zzhl.zzb(bArr, zza5, zzhnVar);
                        zzjyVar.zza(zzhnVar.zzb);
                    }
                    if (zza5 != i14) {
                        throw zzjk.zza();
                    }
                    return zza5;
                }
                if (i5 == 0) {
                    zzjy zzjyVar2 = (zzjy) zzjlVar;
                    int zzb2 = zzhl.zzb(bArr, i9, zzhnVar);
                    zzjyVar2.zza(zzhnVar.zzb);
                    while (zzb2 < i2) {
                        int zza6 = zzhl.zza(bArr, zzb2, zzhnVar);
                        if (i3 != zzhnVar.zza) {
                            return zzb2;
                        }
                        zzb2 = zzhl.zzb(bArr, zza6, zzhnVar);
                        zzjyVar2.zza(zzhnVar.zzb);
                    }
                    return zzb2;
                }
                return i9;
            case R.styleable.MapAttrs_useViewLifecycle /* 22 */:
            case 29:
            case 39:
            case 43:
                if (i5 == 2) {
                    return zzhl.zza(bArr, i9, zzjlVar, zzhnVar);
                }
                if (i5 == 0) {
                    return zzhl.zza(i3, bArr, i, i2, zzjlVar, zzhnVar);
                }
                return i9;
            case R.styleable.MapAttrs_zOrderOnTop /* 23 */:
            case ConnectionsManager.RequestFlagForceDownload /* 32 */:
            case 40:
            case 46:
                if (i5 == 2) {
                    zzjy zzjyVar3 = (zzjy) zzjlVar;
                    int zza7 = zzhl.zza(bArr, i9, zzhnVar);
                    int i15 = zzhnVar.zza + zza7;
                    while (zza7 < i15) {
                        zzjyVar3.zza(zzhl.zzb(bArr, zza7));
                        zza7 += 8;
                    }
                    if (zza7 != i15) {
                        throw zzjk.zza();
                    }
                    return zza7;
                }
                if (i5 == 1) {
                    zzjy zzjyVar4 = (zzjy) zzjlVar;
                    zzjyVar4.zza(zzhl.zzb(bArr, i));
                    while (true) {
                        int i16 = i9 + 8;
                        if (i16 >= i2) {
                            return i16;
                        }
                        i9 = zzhl.zza(bArr, i16, zzhnVar);
                        if (i3 != zzhnVar.zza) {
                            return i16;
                        }
                        zzjyVar4.zza(zzhl.zzb(bArr, i9));
                    }
                }
                return i9;
            case 24:
            case 31:
            case 41:
            case 45:
                if (i5 == 2) {
                    zzjd zzjdVar = (zzjd) zzjlVar;
                    int zza8 = zzhl.zza(bArr, i9, zzhnVar);
                    int i17 = zzhnVar.zza + zza8;
                    while (zza8 < i17) {
                        zzjdVar.zzc(zzhl.zza(bArr, zza8));
                        zza8 += 4;
                    }
                    if (zza8 != i17) {
                        throw zzjk.zza();
                    }
                    return zza8;
                }
                if (i5 == 5) {
                    zzjd zzjdVar2 = (zzjd) zzjlVar;
                    zzjdVar2.zzc(zzhl.zza(bArr, i));
                    while (true) {
                        int i18 = i9 + 4;
                        if (i18 >= i2) {
                            return i18;
                        }
                        i9 = zzhl.zza(bArr, i18, zzhnVar);
                        if (i3 != zzhnVar.zza) {
                            return i18;
                        }
                        zzjdVar2.zzc(zzhl.zza(bArr, i9));
                    }
                }
                return i9;
            case 25:
            case 42:
                if (i5 == 2) {
                    zzhr zzhrVar = (zzhr) zzjlVar;
                    zza2 = zzhl.zza(bArr, i9, zzhnVar);
                    int i19 = zzhnVar.zza + zza2;
                    while (zza2 < i19) {
                        zza2 = zzhl.zzb(bArr, zza2, zzhnVar);
                        zzhrVar.zza(zzhnVar.zzb != 0);
                    }
                    if (zza2 != i19) {
                        throw zzjk.zza();
                    }
                    return zza2;
                }
                if (i5 == 0) {
                    zzhr zzhrVar2 = (zzhr) zzjlVar;
                    i9 = zzhl.zzb(bArr, i9, zzhnVar);
                    zzhrVar2.zza(zzhnVar.zzb != 0);
                    while (i9 < i2) {
                        int zza9 = zzhl.zza(bArr, i9, zzhnVar);
                        if (i3 == zzhnVar.zza) {
                            i9 = zzhl.zzb(bArr, zza9, zzhnVar);
                            zzhrVar2.zza(zzhnVar.zzb != 0);
                        }
                    }
                }
                return i9;
            case 26:
                if (i5 == 2) {
                    if ((j & 536870912) == 0) {
                        i9 = zzhl.zza(bArr, i9, zzhnVar);
                        int i20 = zzhnVar.zza;
                        if (i20 < 0) {
                            throw zzjk.zzb();
                        }
                        if (i20 == 0) {
                            zzjlVar.add("");
                            while (i9 < i2) {
                                int zza10 = zzhl.zza(bArr, i9, zzhnVar);
                                if (i3 == zzhnVar.zza) {
                                    i9 = zzhl.zza(bArr, zza10, zzhnVar);
                                    i20 = zzhnVar.zza;
                                    if (i20 < 0) {
                                        throw zzjk.zzb();
                                    }
                                    if (i20 == 0) {
                                        zzjlVar.add("");
                                    } else {
                                        zzjlVar.add(new String(bArr, i9, i20, zzjf.zza));
                                        i9 += i20;
                                        while (i9 < i2) {
                                        }
                                    }
                                }
                            }
                        } else {
                            zzjlVar.add(new String(bArr, i9, i20, zzjf.zza));
                            i9 += i20;
                            while (i9 < i2) {
                            }
                        }
                    } else {
                        i9 = zzhl.zza(bArr, i9, zzhnVar);
                        int i21 = zzhnVar.zza;
                        if (i21 < 0) {
                            throw zzjk.zzb();
                        }
                        if (i21 == 0) {
                            zzjlVar.add("");
                            while (i9 < i2) {
                                int zza11 = zzhl.zza(bArr, i9, zzhnVar);
                                if (i3 == zzhnVar.zza) {
                                    i9 = zzhl.zza(bArr, zza11, zzhnVar);
                                    int i22 = zzhnVar.zza;
                                    if (i22 < 0) {
                                        throw zzjk.zzb();
                                    }
                                    if (i22 == 0) {
                                        zzjlVar.add("");
                                    } else {
                                        i8 = i9 + i22;
                                        if (!zzmd.zza(bArr, i9, i8)) {
                                            throw zzjk.zzh();
                                        }
                                        zzjlVar.add(new String(bArr, i9, i22, zzjf.zza));
                                        i9 = i8;
                                        while (i9 < i2) {
                                        }
                                    }
                                }
                            }
                        } else {
                            i8 = i9 + i21;
                            if (!zzmd.zza(bArr, i9, i8)) {
                                throw zzjk.zzh();
                            }
                            zzjlVar.add(new String(bArr, i9, i21, zzjf.zza));
                            i9 = i8;
                            while (i9 < i2) {
                            }
                        }
                    }
                }
                return i9;
            case 27:
                if (i5 == 2) {
                    return zzhl.zza(zza(i6), i3, bArr, i, i2, zzjlVar, zzhnVar);
                }
                return i9;
            case 28:
                if (i5 == 2) {
                    int zza12 = zzhl.zza(bArr, i9, zzhnVar);
                    int i23 = zzhnVar.zza;
                    if (i23 < 0) {
                        throw zzjk.zzb();
                    }
                    if (i23 > bArr.length - zza12) {
                        throw zzjk.zza();
                    }
                    if (i23 == 0) {
                        zzjlVar.add(zzht.zza);
                        while (zza12 < i2) {
                            int zza13 = zzhl.zza(bArr, zza12, zzhnVar);
                            if (i3 != zzhnVar.zza) {
                                return zza12;
                            }
                            zza12 = zzhl.zza(bArr, zza13, zzhnVar);
                            i23 = zzhnVar.zza;
                            if (i23 < 0) {
                                throw zzjk.zzb();
                            }
                            if (i23 > bArr.length - zza12) {
                                throw zzjk.zza();
                            }
                            if (i23 == 0) {
                                zzjlVar.add(zzht.zza);
                            } else {
                                zzjlVar.add(zzht.zza(bArr, zza12, i23));
                                zza12 += i23;
                                while (zza12 < i2) {
                                }
                            }
                        }
                        return zza12;
                    }
                    zzjlVar.add(zzht.zza(bArr, zza12, i23));
                    zza12 += i23;
                    while (zza12 < i2) {
                    }
                    return zza12;
                }
                return i9;
            case 30:
            case 44:
                if (i5 != 2) {
                    if (i5 == 0) {
                        zza2 = zzhl.zza(i3, bArr, i, i2, zzjlVar, zzhnVar);
                    }
                    return i9;
                }
                zza2 = zzhl.zza(bArr, i9, zzjlVar, zzhnVar);
                zzjb zzjbVar = (zzjb) t;
                zzlx zzlxVar = zzjbVar.zzb;
                if (zzlxVar == zzlx.zza()) {
                    zzlxVar = null;
                }
                zzlx zzlxVar2 = (zzlx) zzle.zza(i4, zzjlVar, zzc(i6), zzlxVar, this.zzq);
                if (zzlxVar2 != null) {
                    zzjbVar.zzb = zzlxVar2;
                }
                return zza2;
            case 33:
            case 47:
                if (i5 == 2) {
                    zzjd zzjdVar3 = (zzjd) zzjlVar;
                    int zza14 = zzhl.zza(bArr, i9, zzhnVar);
                    int i24 = zzhnVar.zza + zza14;
                    while (zza14 < i24) {
                        zza14 = zzhl.zza(bArr, zza14, zzhnVar);
                        zzjdVar3.zzc(zzif.zze(zzhnVar.zza));
                    }
                    if (zza14 != i24) {
                        throw zzjk.zza();
                    }
                    return zza14;
                }
                if (i5 == 0) {
                    zzjd zzjdVar4 = (zzjd) zzjlVar;
                    int zza15 = zzhl.zza(bArr, i9, zzhnVar);
                    zzjdVar4.zzc(zzif.zze(zzhnVar.zza));
                    while (zza15 < i2) {
                        int zza16 = zzhl.zza(bArr, zza15, zzhnVar);
                        if (i3 != zzhnVar.zza) {
                            return zza15;
                        }
                        zza15 = zzhl.zza(bArr, zza16, zzhnVar);
                        zzjdVar4.zzc(zzif.zze(zzhnVar.zza));
                    }
                    return zza15;
                }
                return i9;
            case 34:
            case 48:
                if (i5 == 2) {
                    zzjy zzjyVar5 = (zzjy) zzjlVar;
                    int zza17 = zzhl.zza(bArr, i9, zzhnVar);
                    int i25 = zzhnVar.zza + zza17;
                    while (zza17 < i25) {
                        zza17 = zzhl.zzb(bArr, zza17, zzhnVar);
                        zzjyVar5.zza(zzif.zza(zzhnVar.zzb));
                    }
                    if (zza17 != i25) {
                        throw zzjk.zza();
                    }
                    return zza17;
                }
                if (i5 == 0) {
                    zzjy zzjyVar6 = (zzjy) zzjlVar;
                    int zzb3 = zzhl.zzb(bArr, i9, zzhnVar);
                    zzjyVar6.zza(zzif.zza(zzhnVar.zzb));
                    while (zzb3 < i2) {
                        int zza18 = zzhl.zza(bArr, zzb3, zzhnVar);
                        if (i3 != zzhnVar.zza) {
                            return zzb3;
                        }
                        zzb3 = zzhl.zzb(bArr, zza18, zzhnVar);
                        zzjyVar6.zza(zzif.zza(zzhnVar.zzb));
                    }
                    return zzb3;
                }
                return i9;
            case 49:
                if (i5 == 3) {
                    zzlc zza19 = zza(i6);
                    int i26 = (i3 & (-8)) | 4;
                    i9 = zzhl.zza(zza19, bArr, i, i2, i26, zzhnVar);
                    zzjlVar.add(zzhnVar.zzc);
                    while (i9 < i2) {
                        int zza20 = zzhl.zza(bArr, i9, zzhnVar);
                        if (i3 == zzhnVar.zza) {
                            i9 = zzhl.zza(zza19, bArr, zza20, i2, i26, zzhnVar);
                            zzjlVar.add(zzhnVar.zzc);
                        }
                    }
                }
                return i9;
            default:
                return i9;
        }
    }

    private final <K, V> int zza(T t, byte[] bArr, int i, int i2, int i3, long j, zzhn zzhnVar) throws IOException {
        Unsafe unsafe = zzb;
        Object zzb2 = zzb(i3);
        Object object = unsafe.getObject(t, j);
        if (this.zzs.zzd(object)) {
            Object zzf = this.zzs.zzf(zzb2);
            this.zzs.zza(zzf, object);
            unsafe.putObject(t, j, zzf);
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

    private final int zza(T t, byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6, int i7, long j, int i8, zzhn zzhnVar) throws IOException {
        int i9;
        Unsafe unsafe = zzb;
        long j2 = this.zzc[i8 + 2] & 1048575;
        switch (i7) {
            case 51:
                if (i5 == 1) {
                    unsafe.putObject(t, j, Double.valueOf(zzhl.zzc(bArr, i)));
                    i9 = i + 8;
                    unsafe.putInt(t, j2, i4);
                    return i9;
                }
                return i;
            case 52:
                if (i5 == 5) {
                    unsafe.putObject(t, j, Float.valueOf(zzhl.zzd(bArr, i)));
                    i9 = i + 4;
                    unsafe.putInt(t, j2, i4);
                    return i9;
                }
                return i;
            case 53:
            case 54:
                if (i5 == 0) {
                    i9 = zzhl.zzb(bArr, i, zzhnVar);
                    unsafe.putObject(t, j, Long.valueOf(zzhnVar.zzb));
                    unsafe.putInt(t, j2, i4);
                    return i9;
                }
                return i;
            case 55:
            case 62:
                if (i5 == 0) {
                    i9 = zzhl.zza(bArr, i, zzhnVar);
                    unsafe.putObject(t, j, Integer.valueOf(zzhnVar.zza));
                    unsafe.putInt(t, j2, i4);
                    return i9;
                }
                return i;
            case 56:
            case VoIPService.CALL_MIN_LAYER /* 65 */:
                if (i5 == 1) {
                    unsafe.putObject(t, j, Long.valueOf(zzhl.zzb(bArr, i)));
                    i9 = i + 8;
                    unsafe.putInt(t, j2, i4);
                    return i9;
                }
                return i;
            case 57:
            case 64:
                if (i5 == 5) {
                    unsafe.putObject(t, j, Integer.valueOf(zzhl.zza(bArr, i)));
                    i9 = i + 4;
                    unsafe.putInt(t, j2, i4);
                    return i9;
                }
                return i;
            case 58:
                if (i5 == 0) {
                    i9 = zzhl.zzb(bArr, i, zzhnVar);
                    unsafe.putObject(t, j, Boolean.valueOf(zzhnVar.zzb != 0));
                    unsafe.putInt(t, j2, i4);
                    return i9;
                }
                return i;
            case 59:
                if (i5 == 2) {
                    int zza2 = zzhl.zza(bArr, i, zzhnVar);
                    int i10 = zzhnVar.zza;
                    if (i10 == 0) {
                        unsafe.putObject(t, j, "");
                    } else if ((i6 & 536870912) != 0 && !zzmd.zza(bArr, zza2, zza2 + i10)) {
                        throw zzjk.zzh();
                    } else {
                        unsafe.putObject(t, j, new String(bArr, zza2, i10, zzjf.zza));
                        zza2 += i10;
                    }
                    unsafe.putInt(t, j2, i4);
                    return zza2;
                }
                return i;
            case 60:
                if (i5 == 2) {
                    int zza3 = zzhl.zza(zza(i8), bArr, i, i2, zzhnVar);
                    Object object = unsafe.getInt(t, j2) == i4 ? unsafe.getObject(t, j) : null;
                    if (object == null) {
                        unsafe.putObject(t, j, zzhnVar.zzc);
                    } else {
                        unsafe.putObject(t, j, zzjf.zza(object, zzhnVar.zzc));
                    }
                    unsafe.putInt(t, j2, i4);
                    return zza3;
                }
                return i;
            case 61:
                if (i5 == 2) {
                    i9 = zzhl.zze(bArr, i, zzhnVar);
                    unsafe.putObject(t, j, zzhnVar.zzc);
                    unsafe.putInt(t, j2, i4);
                    return i9;
                }
                return i;
            case 63:
                if (i5 == 0) {
                    int zza4 = zzhl.zza(bArr, i, zzhnVar);
                    int i11 = zzhnVar.zza;
                    zzjg zzc = zzc(i8);
                    if (zzc == null || zzc.zza(i11)) {
                        unsafe.putObject(t, j, Integer.valueOf(i11));
                        i9 = zza4;
                        unsafe.putInt(t, j2, i4);
                        return i9;
                    }
                    zze(t).zza(i3, Long.valueOf(i11));
                    return zza4;
                }
                return i;
            case 66:
                if (i5 == 0) {
                    i9 = zzhl.zza(bArr, i, zzhnVar);
                    unsafe.putObject(t, j, Integer.valueOf(zzif.zze(zzhnVar.zza)));
                    unsafe.putInt(t, j2, i4);
                    return i9;
                }
                return i;
            case 67:
                if (i5 == 0) {
                    i9 = zzhl.zzb(bArr, i, zzhnVar);
                    unsafe.putObject(t, j, Long.valueOf(zzif.zza(zzhnVar.zzb)));
                    unsafe.putInt(t, j2, i4);
                    return i9;
                }
                return i;
            case 68:
                if (i5 == 3) {
                    i9 = zzhl.zza(zza(i8), bArr, i, i2, (i3 & (-8)) | 4, zzhnVar);
                    Object object2 = unsafe.getInt(t, j2) == i4 ? unsafe.getObject(t, j) : null;
                    if (object2 == null) {
                        unsafe.putObject(t, j, zzhnVar.zzc);
                    } else {
                        unsafe.putObject(t, j, zzjf.zza(object2, zzhnVar.zzc));
                    }
                    unsafe.putInt(t, j2, i4);
                    return i9;
                }
                return i;
            default:
                return i;
        }
    }

    private final zzlc zza(int i) {
        int i2 = (i / 3) << 1;
        zzlc zzlcVar = (zzlc) this.zzd[i2];
        if (zzlcVar != null) {
            return zzlcVar;
        }
        zzlc<T> zza2 = zzky.zza().zza((Class) ((Class) this.zzd[i2 + 1]));
        this.zzd[i2] = zza2;
        return zza2;
    }

    private final Object zzb(int i) {
        return this.zzd[(i / 3) << 1];
    }

    private final zzjg zzc(int i) {
        return (zzjg) this.zzd[((i / 3) << 1) + 1];
    }

    public final int zza(T t, byte[] bArr, int i, int i2, int i3, zzhn zzhnVar) throws IOException {
        Unsafe unsafe;
        int i4;
        int i5;
        T t2;
        zzko<T> zzkoVar;
        int i6;
        int i7;
        byte b;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        boolean z;
        int i14;
        int i15;
        T t3;
        byte[] bArr2;
        zzhn zzhnVar2;
        int i16;
        Object obj;
        Object zza2;
        long j;
        int i17;
        int i18;
        int i19;
        int i20;
        int i21;
        int i22;
        T t4;
        int i23;
        int i24;
        zzko<T> zzkoVar2 = this;
        T t5 = t;
        byte[] bArr3 = bArr;
        int i25 = i2;
        int i26 = i3;
        zzhn zzhnVar3 = zzhnVar;
        Unsafe unsafe2 = zzb;
        int i27 = i;
        int i28 = -1;
        int i29 = 0;
        int i30 = 0;
        int i31 = 0;
        int i32 = 1048575;
        while (true) {
            Object obj2 = null;
            if (i27 < i25) {
                int i33 = i27 + 1;
                byte b2 = bArr3[i27];
                if (b2 < 0) {
                    int zza3 = zzhl.zza(b2, bArr3, i33, zzhnVar3);
                    b = zzhnVar3.zza;
                    i33 = zza3;
                } else {
                    b = b2;
                }
                int i34 = b >>> 3;
                int i35 = b & 7;
                if (i34 > i28) {
                    i8 = zzkoVar2.zza(i34, i29 / 3);
                } else {
                    i8 = zzkoVar2.zzg(i34);
                }
                int i36 = i8;
                if (i36 == -1) {
                    i9 = i34;
                    i10 = i33;
                    i11 = b;
                    i12 = i31;
                    unsafe = unsafe2;
                    i13 = i26;
                    z = true;
                    i14 = 0;
                } else {
                    int[] iArr = zzkoVar2.zzc;
                    int i37 = iArr[i36 + 1];
                    int i38 = (i37 & 267386880) >>> 20;
                    int i39 = b;
                    long j2 = i37 & 1048575;
                    if (i38 <= 17) {
                        int i40 = iArr[i36 + 2];
                        int i41 = 1 << (i40 >>> 20);
                        int i42 = i40 & 1048575;
                        if (i42 != i32) {
                            if (i32 != 1048575) {
                                long j3 = i32;
                                t4 = t;
                                j = j2;
                                unsafe2.putInt(t4, j3, i31);
                            } else {
                                t4 = t;
                                j = j2;
                            }
                            i31 = unsafe2.getInt(t4, i42);
                            t5 = t4;
                        } else {
                            t5 = t;
                            j = j2;
                            i42 = i32;
                        }
                        int i43 = i31;
                        switch (i38) {
                            case 0:
                                i18 = i34;
                                i19 = i36;
                                i12 = i43;
                                i17 = i42;
                                i20 = i39;
                                long j4 = j;
                                if (i35 == 1) {
                                    zzma.zza(t5, j4, zzhl.zzc(bArr3, i33));
                                    i27 = i33 + 8;
                                    i31 = i12 | i41;
                                    i32 = i17;
                                    i30 = i20;
                                    i29 = i19;
                                    i28 = i18;
                                    i25 = i2;
                                    i26 = i3;
                                    break;
                                } else {
                                    i32 = i17;
                                    i13 = i3;
                                    i10 = i33;
                                    i11 = i20;
                                    unsafe = unsafe2;
                                    i14 = i19;
                                    i9 = i18;
                                    z = true;
                                    break;
                                }
                            case 1:
                                i18 = i34;
                                i19 = i36;
                                i12 = i43;
                                i17 = i42;
                                i20 = i39;
                                long j5 = j;
                                if (i35 == 5) {
                                    zzma.zza((Object) t5, j5, zzhl.zzd(bArr3, i33));
                                    i27 = i33 + 4;
                                    i31 = i12 | i41;
                                    i32 = i17;
                                    i30 = i20;
                                    i29 = i19;
                                    i28 = i18;
                                    i25 = i2;
                                    i26 = i3;
                                    break;
                                } else {
                                    i32 = i17;
                                    i13 = i3;
                                    i10 = i33;
                                    i11 = i20;
                                    unsafe = unsafe2;
                                    i14 = i19;
                                    i9 = i18;
                                    z = true;
                                    break;
                                }
                            case 2:
                            case 3:
                                i18 = i34;
                                i19 = i36;
                                i12 = i43;
                                i17 = i42;
                                i20 = i39;
                                long j6 = j;
                                if (i35 == 0) {
                                    i21 = zzhl.zzb(bArr3, i33, zzhnVar3);
                                    unsafe2.putLong(t, j6, zzhnVar3.zzb);
                                    i31 = i12 | i41;
                                    i32 = i17;
                                    i27 = i21;
                                    i30 = i20;
                                    i29 = i19;
                                    i28 = i18;
                                    i25 = i2;
                                    i26 = i3;
                                    break;
                                } else {
                                    i32 = i17;
                                    i13 = i3;
                                    i10 = i33;
                                    i11 = i20;
                                    unsafe = unsafe2;
                                    i14 = i19;
                                    i9 = i18;
                                    z = true;
                                    break;
                                }
                            case 4:
                            case 11:
                                i18 = i34;
                                i19 = i36;
                                i12 = i43;
                                i17 = i42;
                                i20 = i39;
                                long j7 = j;
                                if (i35 == 0) {
                                    i27 = zzhl.zza(bArr3, i33, zzhnVar3);
                                    unsafe2.putInt(t5, j7, zzhnVar3.zza);
                                    i31 = i12 | i41;
                                    i32 = i17;
                                    i30 = i20;
                                    i29 = i19;
                                    i28 = i18;
                                    i25 = i2;
                                    i26 = i3;
                                    break;
                                } else {
                                    i32 = i17;
                                    i13 = i3;
                                    i10 = i33;
                                    i11 = i20;
                                    unsafe = unsafe2;
                                    i14 = i19;
                                    i9 = i18;
                                    z = true;
                                    break;
                                }
                            case 5:
                            case 14:
                                i18 = i34;
                                i19 = i36;
                                i12 = i43;
                                i17 = i42;
                                i20 = i39;
                                long j8 = j;
                                if (i35 == 1) {
                                    unsafe2.putLong(t, j8, zzhl.zzb(bArr3, i33));
                                    i27 = i33 + 8;
                                    i31 = i12 | i41;
                                    i32 = i17;
                                    i30 = i20;
                                    i29 = i19;
                                    i28 = i18;
                                    i25 = i2;
                                    i26 = i3;
                                    break;
                                } else {
                                    i32 = i17;
                                    i13 = i3;
                                    i10 = i33;
                                    i11 = i20;
                                    unsafe = unsafe2;
                                    i14 = i19;
                                    i9 = i18;
                                    z = true;
                                    break;
                                }
                            case 6:
                            case 13:
                                i18 = i34;
                                i19 = i36;
                                i12 = i43;
                                i17 = i42;
                                i20 = i39;
                                long j9 = j;
                                if (i35 == 5) {
                                    unsafe2.putInt(t5, j9, zzhl.zza(bArr3, i33));
                                    i27 = i33 + 4;
                                    i31 = i12 | i41;
                                    i32 = i17;
                                    i30 = i20;
                                    i29 = i19;
                                    i28 = i18;
                                    i25 = i2;
                                    i26 = i3;
                                    break;
                                } else {
                                    i32 = i17;
                                    i13 = i3;
                                    i10 = i33;
                                    i11 = i20;
                                    unsafe = unsafe2;
                                    i14 = i19;
                                    i9 = i18;
                                    z = true;
                                    break;
                                }
                            case 7:
                                i18 = i34;
                                i19 = i36;
                                i12 = i43;
                                i17 = i42;
                                i20 = i39;
                                long j10 = j;
                                if (i35 == 0) {
                                    i27 = zzhl.zzb(bArr3, i33, zzhnVar3);
                                    zzma.zza(t5, j10, zzhnVar3.zzb != 0);
                                    i31 = i12 | i41;
                                    i32 = i17;
                                    i30 = i20;
                                    i29 = i19;
                                    i28 = i18;
                                    i25 = i2;
                                    i26 = i3;
                                    break;
                                } else {
                                    i32 = i17;
                                    i13 = i3;
                                    i10 = i33;
                                    i11 = i20;
                                    unsafe = unsafe2;
                                    i14 = i19;
                                    i9 = i18;
                                    z = true;
                                    break;
                                }
                            case 8:
                                i18 = i34;
                                i19 = i36;
                                i12 = i43;
                                i17 = i42;
                                i20 = i39;
                                long j11 = j;
                                if (i35 == 2) {
                                    if ((536870912 & i37) == 0) {
                                        i27 = zzhl.zzc(bArr3, i33, zzhnVar3);
                                    } else {
                                        i27 = zzhl.zzd(bArr3, i33, zzhnVar3);
                                    }
                                    unsafe2.putObject(t5, j11, zzhnVar3.zzc);
                                    i31 = i12 | i41;
                                    i32 = i17;
                                    i30 = i20;
                                    i29 = i19;
                                    i28 = i18;
                                    i25 = i2;
                                    i26 = i3;
                                    break;
                                } else {
                                    i32 = i17;
                                    i13 = i3;
                                    i10 = i33;
                                    i11 = i20;
                                    unsafe = unsafe2;
                                    i14 = i19;
                                    i9 = i18;
                                    z = true;
                                    break;
                                }
                            case 9:
                                i18 = i34;
                                i19 = i36;
                                i17 = i42;
                                i20 = i39;
                                long j12 = j;
                                if (i35 == 2) {
                                    int zza4 = zzhl.zza(zzkoVar2.zza(i19), bArr3, i33, i2, zzhnVar3);
                                    if ((i43 & i41) == 0) {
                                        unsafe2.putObject(t5, j12, zzhnVar3.zzc);
                                    } else {
                                        unsafe2.putObject(t5, j12, zzjf.zza(unsafe2.getObject(t5, j12), zzhnVar3.zzc));
                                    }
                                    int i44 = i43 | i41;
                                    i32 = i17;
                                    i30 = i20;
                                    i28 = i18;
                                    i25 = i2;
                                    i31 = i44;
                                    i27 = zza4;
                                    i29 = i19;
                                    i26 = i3;
                                    break;
                                } else {
                                    i12 = i43;
                                    i32 = i17;
                                    i13 = i3;
                                    i10 = i33;
                                    i11 = i20;
                                    unsafe = unsafe2;
                                    i14 = i19;
                                    i9 = i18;
                                    z = true;
                                    break;
                                }
                            case 10:
                                i18 = i34;
                                i19 = i36;
                                i17 = i42;
                                i20 = i39;
                                long j13 = j;
                                if (i35 == 2) {
                                    i22 = zzhl.zze(bArr3, i33, zzhnVar3);
                                    unsafe2.putObject(t5, j13, zzhnVar3.zzc);
                                    i31 = i43 | i41;
                                    i32 = i17;
                                    i27 = i22;
                                    i30 = i20;
                                    i29 = i19;
                                    i28 = i18;
                                    i25 = i2;
                                    i26 = i3;
                                    break;
                                } else {
                                    i12 = i43;
                                    i32 = i17;
                                    i13 = i3;
                                    i10 = i33;
                                    i11 = i20;
                                    unsafe = unsafe2;
                                    i14 = i19;
                                    i9 = i18;
                                    z = true;
                                    break;
                                }
                            case 12:
                                i18 = i34;
                                i19 = i36;
                                i17 = i42;
                                i20 = i39;
                                long j14 = j;
                                if (i35 != 0) {
                                    i12 = i43;
                                    i32 = i17;
                                    i13 = i3;
                                    i10 = i33;
                                    i11 = i20;
                                    unsafe = unsafe2;
                                    i14 = i19;
                                    i9 = i18;
                                    z = true;
                                    break;
                                } else {
                                    i22 = zzhl.zza(bArr3, i33, zzhnVar3);
                                    int i45 = zzhnVar3.zza;
                                    zzjg zzc = zzkoVar2.zzc(i19);
                                    if (zzc == null || zzc.zza(i45)) {
                                        unsafe2.putInt(t5, j14, i45);
                                        i31 = i43 | i41;
                                        i32 = i17;
                                        i27 = i22;
                                        i30 = i20;
                                        i29 = i19;
                                        i28 = i18;
                                        i25 = i2;
                                        i26 = i3;
                                        break;
                                    } else {
                                        zze(t).zza(i20, Long.valueOf(i45));
                                        i27 = i22;
                                        i31 = i43;
                                        i30 = i20;
                                        i29 = i19;
                                        i28 = i18;
                                        i32 = i17;
                                        i25 = i2;
                                        i26 = i3;
                                    }
                                }
                                break;
                            case 15:
                                i18 = i34;
                                i19 = i36;
                                i17 = i42;
                                i20 = i39;
                                long j15 = j;
                                if (i35 == 0) {
                                    i22 = zzhl.zza(bArr3, i33, zzhnVar3);
                                    unsafe2.putInt(t5, j15, zzif.zze(zzhnVar3.zza));
                                    i31 = i43 | i41;
                                    i32 = i17;
                                    i27 = i22;
                                    i30 = i20;
                                    i29 = i19;
                                    i28 = i18;
                                    i25 = i2;
                                    i26 = i3;
                                    break;
                                } else {
                                    i12 = i43;
                                    i32 = i17;
                                    i13 = i3;
                                    i10 = i33;
                                    i11 = i20;
                                    unsafe = unsafe2;
                                    i14 = i19;
                                    i9 = i18;
                                    z = true;
                                    break;
                                }
                            case 16:
                                i18 = i34;
                                i19 = i36;
                                long j16 = j;
                                if (i35 == 0) {
                                    i21 = zzhl.zzb(bArr3, i33, zzhnVar3);
                                    i17 = i42;
                                    i20 = i39;
                                    unsafe2.putLong(t, j16, zzif.zza(zzhnVar3.zzb));
                                    i31 = i43 | i41;
                                    i32 = i17;
                                    i27 = i21;
                                    i30 = i20;
                                    i29 = i19;
                                    i28 = i18;
                                    i25 = i2;
                                    i26 = i3;
                                    break;
                                } else {
                                    i17 = i42;
                                    i20 = i39;
                                    i12 = i43;
                                    i32 = i17;
                                    i13 = i3;
                                    i10 = i33;
                                    i11 = i20;
                                    unsafe = unsafe2;
                                    i14 = i19;
                                    i9 = i18;
                                    z = true;
                                    break;
                                }
                            case 17:
                                if (i35 == 3) {
                                    i18 = i34;
                                    i19 = i36;
                                    i27 = zzhl.zza(zzkoVar2.zza(i36), bArr, i33, i2, (i34 << 3) | 4, zzhnVar);
                                    if ((i43 & i41) == 0) {
                                        unsafe2.putObject(t5, j, zzhnVar3.zzc);
                                    } else {
                                        long j17 = j;
                                        unsafe2.putObject(t5, j17, zzjf.zza(unsafe2.getObject(t5, j17), zzhnVar3.zzc));
                                    }
                                    i31 = i43 | i41;
                                    i30 = i39;
                                    i32 = i42;
                                    i29 = i19;
                                    i28 = i18;
                                    i25 = i2;
                                    i26 = i3;
                                    break;
                                } else {
                                    i18 = i34;
                                    i19 = i36;
                                    i12 = i43;
                                    i17 = i42;
                                    i20 = i39;
                                    i32 = i17;
                                    i13 = i3;
                                    i10 = i33;
                                    i11 = i20;
                                    unsafe = unsafe2;
                                    i14 = i19;
                                    i9 = i18;
                                    z = true;
                                    break;
                                }
                            default:
                                i18 = i34;
                                i19 = i36;
                                i12 = i43;
                                i17 = i42;
                                i20 = i39;
                                i32 = i17;
                                i13 = i3;
                                i10 = i33;
                                i11 = i20;
                                unsafe = unsafe2;
                                i14 = i19;
                                i9 = i18;
                                z = true;
                                break;
                        }
                    } else {
                        i12 = i31;
                        int i46 = i32;
                        t5 = t;
                        if (i38 != 27) {
                            i14 = i36;
                            if (i38 <= 49) {
                                int i47 = i33;
                                i24 = i39;
                                z = true;
                                unsafe = unsafe2;
                                i13 = i3;
                                i9 = i34;
                                i27 = zza((zzko<T>) t, bArr, i33, i2, i39, i34, i35, i14, i37, i38, j2, zzhnVar);
                                if (i27 == i47) {
                                    i10 = i27;
                                } else {
                                    t5 = t;
                                    bArr3 = bArr;
                                    i25 = i2;
                                    zzhnVar3 = zzhnVar;
                                    i26 = i13;
                                    i30 = i24;
                                    i32 = i46;
                                    i31 = i12;
                                    i29 = i14;
                                    i28 = i9;
                                    unsafe2 = unsafe;
                                    zzkoVar2 = this;
                                }
                            } else {
                                i13 = i3;
                                i23 = i33;
                                i24 = i39;
                                unsafe = unsafe2;
                                i9 = i34;
                                z = true;
                                if (i38 != 50) {
                                    i27 = zza((zzko<T>) t, bArr, i23, i2, i24, i9, i35, i37, i38, j2, i14, zzhnVar);
                                    if (i27 != i23) {
                                        t5 = t;
                                        bArr3 = bArr;
                                        i25 = i2;
                                        zzhnVar3 = zzhnVar;
                                        i30 = i24;
                                        i26 = i13;
                                        i32 = i46;
                                        i31 = i12;
                                        i29 = i14;
                                        i28 = i9;
                                        unsafe2 = unsafe;
                                        zzkoVar2 = this;
                                    }
                                } else if (i35 == 2) {
                                    i27 = zza((zzko<T>) t, bArr, i23, i2, i14, j2, zzhnVar);
                                    if (i27 != i23) {
                                        t5 = t;
                                        bArr3 = bArr;
                                        i25 = i2;
                                        zzhnVar3 = zzhnVar;
                                        i26 = i13;
                                        i30 = i24;
                                        i32 = i46;
                                        i31 = i12;
                                        i29 = i14;
                                        i28 = i9;
                                        unsafe2 = unsafe;
                                        zzkoVar2 = this;
                                    }
                                } else {
                                    i10 = i23;
                                }
                                i10 = i27;
                            }
                        } else if (i35 == 2) {
                            zzjl zzjlVar = (zzjl) unsafe2.getObject(t5, j2);
                            if (!zzjlVar.zza()) {
                                int size = zzjlVar.size();
                                zzjlVar = zzjlVar.zza(size == 0 ? 10 : size << 1);
                                unsafe2.putObject(t5, j2, zzjlVar);
                            }
                            i27 = zzhl.zza(zzkoVar2.zza(i36), i39, bArr, i33, i2, zzjlVar, zzhnVar);
                            i26 = i3;
                            i30 = i39;
                            i28 = i34;
                            i32 = i46;
                            i31 = i12;
                            i29 = i36;
                            i25 = i2;
                        } else {
                            i14 = i36;
                            i13 = i3;
                            i23 = i33;
                            i24 = i39;
                            unsafe = unsafe2;
                            i9 = i34;
                            z = true;
                            i10 = i23;
                        }
                        i11 = i24;
                        i32 = i46;
                    }
                }
                if (i11 != i13 || i13 == 0) {
                    int i48 = i13;
                    if (this.zzh) {
                        zzhnVar2 = zzhnVar;
                        if (zzhnVar2.zzd != zzio.zzb()) {
                            int i49 = i9;
                            zzjb.zze zza5 = zzhnVar2.zzd.zza(this.zzg, i49);
                            if (zza5 == null) {
                                i27 = zzhl.zza(i11, bArr, i10, i2, zze(t), zzhnVar);
                                t3 = t;
                                i15 = i32;
                                i9 = i49;
                                bArr2 = bArr;
                                i16 = i2;
                            } else {
                                t3 = t;
                                zzjb.zzc zzcVar = (zzjb.zzc) t3;
                                zzcVar.zza();
                                zziu<zzjb.zzf> zziuVar = zzcVar.zzc;
                                zzjb.zzf zzfVar = zza5.zzd;
                                boolean z2 = zzfVar.zzd;
                                zzml zzmlVar = zzfVar.zzc;
                                if (zzmlVar == zzml.ENUM) {
                                    zzhl.zza(bArr, i10, zzhnVar2);
                                    throw null;
                                }
                                int[] iArr2 = zzhk.zza;
                                switch (iArr2[zzmlVar.ordinal()]) {
                                    case 1:
                                        i15 = i32;
                                        i9 = i49;
                                        bArr2 = bArr;
                                        i16 = i2;
                                        obj2 = Double.valueOf(zzhl.zzc(bArr2, i10));
                                        i10 += 8;
                                        obj = obj2;
                                        break;
                                    case 2:
                                        i15 = i32;
                                        i9 = i49;
                                        bArr2 = bArr;
                                        i16 = i2;
                                        obj2 = Float.valueOf(zzhl.zzd(bArr2, i10));
                                        i10 += 4;
                                        obj = obj2;
                                        break;
                                    case 3:
                                    case 4:
                                        i15 = i32;
                                        i9 = i49;
                                        bArr2 = bArr;
                                        i16 = i2;
                                        i10 = zzhl.zzb(bArr2, i10, zzhnVar2);
                                        obj2 = Long.valueOf(zzhnVar2.zzb);
                                        obj = obj2;
                                        break;
                                    case 5:
                                    case 6:
                                        i15 = i32;
                                        i9 = i49;
                                        bArr2 = bArr;
                                        i16 = i2;
                                        i10 = zzhl.zza(bArr2, i10, zzhnVar2);
                                        obj2 = Integer.valueOf(zzhnVar2.zza);
                                        obj = obj2;
                                        break;
                                    case 7:
                                    case 8:
                                        i15 = i32;
                                        i9 = i49;
                                        bArr2 = bArr;
                                        i16 = i2;
                                        obj2 = Long.valueOf(zzhl.zzb(bArr2, i10));
                                        i10 += 8;
                                        obj = obj2;
                                        break;
                                    case 9:
                                    case 10:
                                        i15 = i32;
                                        i9 = i49;
                                        bArr2 = bArr;
                                        i16 = i2;
                                        obj2 = Integer.valueOf(zzhl.zza(bArr2, i10));
                                        i10 += 4;
                                        obj = obj2;
                                        break;
                                    case 11:
                                        i15 = i32;
                                        i9 = i49;
                                        bArr2 = bArr;
                                        i16 = i2;
                                        i10 = zzhl.zzb(bArr2, i10, zzhnVar2);
                                        if (zzhnVar2.zzb == 0) {
                                            z = false;
                                        }
                                        obj2 = Boolean.valueOf(z);
                                        obj = obj2;
                                        break;
                                    case 12:
                                        i15 = i32;
                                        i9 = i49;
                                        bArr2 = bArr;
                                        i16 = i2;
                                        i10 = zzhl.zza(bArr2, i10, zzhnVar2);
                                        obj2 = Integer.valueOf(zzif.zze(zzhnVar2.zza));
                                        obj = obj2;
                                        break;
                                    case 13:
                                        i15 = i32;
                                        i9 = i49;
                                        bArr2 = bArr;
                                        i16 = i2;
                                        i10 = zzhl.zzb(bArr2, i10, zzhnVar2);
                                        obj2 = Long.valueOf(zzif.zza(zzhnVar2.zzb));
                                        obj = obj2;
                                        break;
                                    case 14:
                                        throw new IllegalStateException("Shouldn't reach here.");
                                    case 15:
                                        i15 = i32;
                                        i9 = i49;
                                        bArr2 = bArr;
                                        i16 = i2;
                                        i10 = zzhl.zze(bArr2, i10, zzhnVar2);
                                        obj = zzhnVar2.zzc;
                                        break;
                                    case 16:
                                        i15 = i32;
                                        i9 = i49;
                                        bArr2 = bArr;
                                        i16 = i2;
                                        i10 = zzhl.zzc(bArr2, i10, zzhnVar2);
                                        obj = zzhnVar2.zzc;
                                        break;
                                    case 17:
                                        int i50 = (i49 << 3) | 4;
                                        i15 = i32;
                                        i16 = i2;
                                        i9 = i49;
                                        bArr2 = bArr;
                                        i10 = zzhl.zza(zzky.zza().zza((Class) zza5.zzc.getClass()), bArr, i10, i2, i50, zzhnVar);
                                        obj = zzhnVar2.zzc;
                                        break;
                                    case R.styleable.MapAttrs_uiScrollGesturesDuringRotateOrZoom /* 18 */:
                                        i10 = zzhl.zza(zzky.zza().zza((Class) zza5.zzc.getClass()), bArr, i10, i2, zzhnVar2);
                                        obj = zzhnVar2.zzc;
                                        i15 = i32;
                                        i9 = i49;
                                        i16 = i2;
                                        bArr2 = bArr;
                                        break;
                                    default:
                                        i15 = i32;
                                        i9 = i49;
                                        bArr2 = bArr;
                                        i16 = i2;
                                        obj = obj2;
                                        break;
                                }
                                zzjb.zzf zzfVar2 = zza5.zzd;
                                if (zzfVar2.zzd) {
                                    zziuVar.zzb(zzfVar2, obj);
                                } else {
                                    int i51 = iArr2[zzfVar2.zzc.ordinal()];
                                    if ((i51 == 17 || i51 == 18) && (zza2 = zziuVar.zza((zziu<zzjb.zzf>) zza5.zzd)) != null) {
                                        obj = zzjf.zza(zza2, obj);
                                    }
                                    zziuVar.zza((zziu<zzjb.zzf>) zza5.zzd, obj);
                                }
                                i27 = i10;
                            }
                            i30 = i11;
                            zzkoVar2 = this;
                            bArr3 = bArr2;
                            t5 = t3;
                            i31 = i12;
                            i29 = i14;
                            i28 = i9;
                            i25 = i16;
                            i26 = i48;
                            zzhnVar3 = zzhnVar2;
                            unsafe2 = unsafe;
                            i32 = i15;
                        } else {
                            t3 = t;
                            bArr2 = bArr;
                        }
                    } else {
                        t3 = t;
                        bArr2 = bArr;
                        zzhnVar2 = zzhnVar;
                    }
                    i15 = i32;
                    i16 = i2;
                    i27 = zzhl.zza(i11, bArr, i10, i2, zze(t), zzhnVar);
                    i30 = i11;
                    zzkoVar2 = this;
                    bArr3 = bArr2;
                    t5 = t3;
                    i31 = i12;
                    i29 = i14;
                    i28 = i9;
                    i25 = i16;
                    i26 = i48;
                    zzhnVar3 = zzhnVar2;
                    unsafe2 = unsafe;
                    i32 = i15;
                } else {
                    zzkoVar = this;
                    t2 = t;
                    i27 = i10;
                    i6 = i32;
                    i30 = i11;
                    i4 = i13;
                    i31 = i12;
                    i7 = 1048575;
                    i5 = i2;
                }
            } else {
                int i52 = i32;
                unsafe = unsafe2;
                i4 = i26;
                i5 = i25;
                t2 = t5;
                zzkoVar = zzkoVar2;
                i6 = i52;
                i7 = 1048575;
            }
        }
        if (i6 != i7) {
            unsafe.putInt(t2, i6, i31);
        }
        zzlx zzlxVar = null;
        for (int i53 = zzkoVar.zzm; i53 < zzkoVar.zzn; i53++) {
            zzlxVar = (zzlx) zzkoVar.zza((Object) t2, zzkoVar.zzl[i53], (int) zzlxVar, (zzlu<UT, int>) zzkoVar.zzq);
        }
        if (zzlxVar != null) {
            zzkoVar.zzq.zzb((Object) t2, (T) zzlxVar);
        }
        if (i4 == 0) {
            if (i27 != i5) {
                throw zzjk.zzg();
            }
        } else if (i27 > i5 || i30 != i4) {
            throw zzjk.zzg();
        }
        return i27;
    }

    /* JADX WARN: Code restructure failed: missing block: B:102:0x02dc, code lost:
        if (r0 == r5) goto L114;
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x02e0, code lost:
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
    /* JADX WARN: Code restructure failed: missing block: B:109:0x0323, code lost:
        if (r0 == r15) goto L114;
     */
    /* JADX WARN: Code restructure failed: missing block: B:113:0x0346, code lost:
        if (r0 == r15) goto L114;
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x0348, code lost:
        r2 = r0;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v11, types: [int] */
    @Override // com.google.android.gms.internal.vision.zzlc
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void zza(T t, byte[] bArr, int i, int i2, zzhn zzhnVar) throws IOException {
        byte b;
        int i3;
        int i4;
        Unsafe unsafe;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        Unsafe unsafe2;
        int i12;
        Unsafe unsafe3;
        int i13;
        Unsafe unsafe4;
        zzko<T> zzkoVar = this;
        T t2 = t;
        byte[] bArr2 = bArr;
        int i14 = i2;
        zzhn zzhnVar2 = zzhnVar;
        if (zzkoVar.zzj) {
            Unsafe unsafe5 = zzb;
            int i15 = -1;
            int i16 = 1048575;
            int i17 = i;
            int i18 = -1;
            int i19 = 0;
            int i20 = 0;
            int i21 = 1048575;
            while (i17 < i14) {
                int i22 = i17 + 1;
                byte b2 = bArr2[i17];
                if (b2 < 0) {
                    i3 = zzhl.zza(b2, bArr2, i22, zzhnVar2);
                    b = zzhnVar2.zza;
                } else {
                    b = b2;
                    i3 = i22;
                }
                int i23 = b >>> 3;
                int i24 = b & 7;
                if (i23 > i18) {
                    i4 = zzkoVar.zza(i23, i19 / 3);
                } else {
                    i4 = zzkoVar.zzg(i23);
                }
                int i25 = i4;
                if (i25 == i15) {
                    i7 = i3;
                    i5 = i23;
                    unsafe = unsafe5;
                    i6 = 0;
                } else {
                    int[] iArr = zzkoVar.zzc;
                    int i26 = iArr[i25 + 1];
                    int i27 = (i26 & 267386880) >>> 20;
                    Unsafe unsafe6 = unsafe5;
                    long j = i26 & i16;
                    if (i27 <= 17) {
                        int i28 = iArr[i25 + 2];
                        int i29 = 1 << (i28 >>> 20);
                        int i30 = i28 & 1048575;
                        if (i30 != i21) {
                            if (i21 != 1048575) {
                                long j2 = i21;
                                unsafe4 = unsafe6;
                                unsafe4.putInt(t2, j2, i20);
                            } else {
                                unsafe4 = unsafe6;
                            }
                            if (i30 != 1048575) {
                                i20 = unsafe4.getInt(t2, i30);
                            }
                            unsafe2 = unsafe4;
                            i21 = i30;
                        } else {
                            unsafe2 = unsafe6;
                        }
                        switch (i27) {
                            case 0:
                                i5 = i23;
                                i13 = i3;
                                i12 = i25;
                                i8 = i21;
                                unsafe3 = unsafe2;
                                if (i24 == 1) {
                                    zzma.zza(t2, j, zzhl.zzc(bArr2, i13));
                                    i17 = i13 + 8;
                                    i20 |= i29;
                                    unsafe5 = unsafe3;
                                    i19 = i12;
                                    break;
                                }
                                i7 = i13;
                                unsafe = unsafe3;
                                i6 = i12;
                                i21 = i8;
                                break;
                            case 1:
                                i5 = i23;
                                i13 = i3;
                                i12 = i25;
                                i8 = i21;
                                unsafe3 = unsafe2;
                                if (i24 == 5) {
                                    zzma.zza((Object) t2, j, zzhl.zzd(bArr2, i13));
                                    i17 = i13 + 4;
                                    i20 |= i29;
                                    unsafe5 = unsafe3;
                                    i19 = i12;
                                    break;
                                }
                                i7 = i13;
                                unsafe = unsafe3;
                                i6 = i12;
                                i21 = i8;
                                break;
                            case 2:
                            case 3:
                                i5 = i23;
                                i13 = i3;
                                i12 = i25;
                                i8 = i21;
                                unsafe3 = unsafe2;
                                if (i24 == 0) {
                                    int zzb2 = zzhl.zzb(bArr2, i13, zzhnVar2);
                                    unsafe3.putLong(t, j, zzhnVar2.zzb);
                                    i20 |= i29;
                                    unsafe5 = unsafe3;
                                    i19 = i12;
                                    i17 = zzb2;
                                    break;
                                }
                                i7 = i13;
                                unsafe = unsafe3;
                                i6 = i12;
                                i21 = i8;
                                break;
                            case 4:
                            case 11:
                                i5 = i23;
                                i13 = i3;
                                i12 = i25;
                                i8 = i21;
                                unsafe3 = unsafe2;
                                if (i24 == 0) {
                                    i17 = zzhl.zza(bArr2, i13, zzhnVar2);
                                    unsafe3.putInt(t2, j, zzhnVar2.zza);
                                    i20 |= i29;
                                    unsafe5 = unsafe3;
                                    i19 = i12;
                                    break;
                                }
                                i7 = i13;
                                unsafe = unsafe3;
                                i6 = i12;
                                i21 = i8;
                                break;
                            case 5:
                            case 14:
                                i5 = i23;
                                i12 = i25;
                                i8 = i21;
                                unsafe3 = unsafe2;
                                if (i24 == 1) {
                                    unsafe3.putLong(t, j, zzhl.zzb(bArr2, i3));
                                    i17 = i3 + 8;
                                    i20 |= i29;
                                    unsafe5 = unsafe3;
                                    i19 = i12;
                                    break;
                                }
                                i13 = i3;
                                i7 = i13;
                                unsafe = unsafe3;
                                i6 = i12;
                                i21 = i8;
                                break;
                            case 6:
                            case 13:
                                i5 = i23;
                                i12 = i25;
                                i8 = i21;
                                unsafe3 = unsafe2;
                                if (i24 == 5) {
                                    unsafe3.putInt(t2, j, zzhl.zza(bArr2, i3));
                                    i17 = i3 + 4;
                                    i20 |= i29;
                                    unsafe5 = unsafe3;
                                    i19 = i12;
                                    break;
                                }
                                i13 = i3;
                                i7 = i13;
                                unsafe = unsafe3;
                                i6 = i12;
                                i21 = i8;
                                break;
                            case 7:
                                i5 = i23;
                                i12 = i25;
                                i8 = i21;
                                unsafe3 = unsafe2;
                                if (i24 == 0) {
                                    i17 = zzhl.zzb(bArr2, i3, zzhnVar2);
                                    zzma.zza(t2, j, zzhnVar2.zzb != 0);
                                    i20 |= i29;
                                    unsafe5 = unsafe3;
                                    i19 = i12;
                                    break;
                                }
                                i13 = i3;
                                i7 = i13;
                                unsafe = unsafe3;
                                i6 = i12;
                                i21 = i8;
                                break;
                            case 8:
                                i5 = i23;
                                i12 = i25;
                                i8 = i21;
                                unsafe3 = unsafe2;
                                if (i24 == 2) {
                                    if ((i26 & 536870912) == 0) {
                                        i17 = zzhl.zzc(bArr2, i3, zzhnVar2);
                                    } else {
                                        i17 = zzhl.zzd(bArr2, i3, zzhnVar2);
                                    }
                                    unsafe3.putObject(t2, j, zzhnVar2.zzc);
                                    i20 |= i29;
                                    unsafe5 = unsafe3;
                                    i19 = i12;
                                    break;
                                }
                                i13 = i3;
                                i7 = i13;
                                unsafe = unsafe3;
                                i6 = i12;
                                i21 = i8;
                                break;
                            case 9:
                                i5 = i23;
                                i12 = i25;
                                i8 = i21;
                                unsafe3 = unsafe2;
                                if (i24 == 2) {
                                    i17 = zzhl.zza(zzkoVar.zza(i12), bArr2, i3, i14, zzhnVar2);
                                    Object object = unsafe3.getObject(t2, j);
                                    if (object == null) {
                                        unsafe3.putObject(t2, j, zzhnVar2.zzc);
                                    } else {
                                        unsafe3.putObject(t2, j, zzjf.zza(object, zzhnVar2.zzc));
                                    }
                                    i20 |= i29;
                                    unsafe5 = unsafe3;
                                    i19 = i12;
                                    break;
                                }
                                i13 = i3;
                                i7 = i13;
                                unsafe = unsafe3;
                                i6 = i12;
                                i21 = i8;
                                break;
                            case 10:
                                i5 = i23;
                                i12 = i25;
                                i8 = i21;
                                unsafe3 = unsafe2;
                                if (i24 == 2) {
                                    i17 = zzhl.zze(bArr2, i3, zzhnVar2);
                                    unsafe3.putObject(t2, j, zzhnVar2.zzc);
                                    i20 |= i29;
                                    unsafe5 = unsafe3;
                                    i19 = i12;
                                    break;
                                }
                                i13 = i3;
                                i7 = i13;
                                unsafe = unsafe3;
                                i6 = i12;
                                i21 = i8;
                                break;
                            case 12:
                                i5 = i23;
                                i12 = i25;
                                i8 = i21;
                                unsafe3 = unsafe2;
                                if (i24 == 0) {
                                    i17 = zzhl.zza(bArr2, i3, zzhnVar2);
                                    unsafe3.putInt(t2, j, zzhnVar2.zza);
                                    i20 |= i29;
                                    unsafe5 = unsafe3;
                                    i19 = i12;
                                    break;
                                }
                                i13 = i3;
                                i7 = i13;
                                unsafe = unsafe3;
                                i6 = i12;
                                i21 = i8;
                                break;
                            case 15:
                                i5 = i23;
                                i12 = i25;
                                i8 = i21;
                                unsafe3 = unsafe2;
                                if (i24 == 0) {
                                    i17 = zzhl.zza(bArr2, i3, zzhnVar2);
                                    unsafe3.putInt(t2, j, zzif.zze(zzhnVar2.zza));
                                    i20 |= i29;
                                    unsafe5 = unsafe3;
                                    i19 = i12;
                                    break;
                                }
                                i13 = i3;
                                i7 = i13;
                                unsafe = unsafe3;
                                i6 = i12;
                                i21 = i8;
                                break;
                            case 16:
                                if (i24 != 0) {
                                    i5 = i23;
                                    i8 = i21;
                                    unsafe3 = unsafe2;
                                    i13 = i3;
                                    i12 = i25;
                                    i7 = i13;
                                    unsafe = unsafe3;
                                    i6 = i12;
                                    i21 = i8;
                                    break;
                                } else {
                                    int zzb3 = zzhl.zzb(bArr2, i3, zzhnVar2);
                                    i8 = i21;
                                    i5 = i23;
                                    unsafe2.putLong(t, j, zzif.zza(zzhnVar2.zzb));
                                    i20 |= i29;
                                    unsafe5 = unsafe2;
                                    i19 = i25;
                                    i17 = zzb3;
                                    break;
                                }
                            default:
                                i5 = i23;
                                i13 = i3;
                                i12 = i25;
                                i8 = i21;
                                unsafe3 = unsafe2;
                                i7 = i13;
                                unsafe = unsafe3;
                                i6 = i12;
                                i21 = i8;
                                break;
                        }
                    } else {
                        i5 = i23;
                        int i31 = i3;
                        i8 = i21;
                        if (i27 != 27) {
                            i6 = i25;
                            if (i27 <= 49) {
                                i10 = i20;
                                i9 = i8;
                                unsafe = unsafe6;
                                i17 = zza((zzko<T>) t, bArr, i31, i2, b, i5, i24, i6, i26, i27, j, zzhnVar);
                            } else {
                                i11 = i31;
                                i10 = i20;
                                unsafe = unsafe6;
                                i9 = i8;
                                if (i27 != 50) {
                                    i17 = zza((zzko<T>) t, bArr, i11, i2, b, i5, i24, i26, i27, j, i6, zzhnVar);
                                } else if (i24 == 2) {
                                    i17 = zza((zzko<T>) t, bArr, i11, i2, i6, j, zzhnVar);
                                }
                            }
                            unsafe5 = unsafe;
                            i16 = 1048575;
                            i15 = -1;
                        } else if (i24 == 2) {
                            zzjl zzjlVar = (zzjl) unsafe6.getObject(t2, j);
                            if (!zzjlVar.zza()) {
                                int size = zzjlVar.size();
                                zzjlVar = zzjlVar.zza(size == 0 ? 10 : size << 1);
                                unsafe6.putObject(t2, j, zzjlVar);
                            }
                            i17 = zzhl.zza(zzkoVar.zza(i25), b, bArr, i31, i2, zzjlVar, zzhnVar);
                            unsafe5 = unsafe6;
                            i20 = i20;
                            i19 = i25;
                        } else {
                            i6 = i25;
                            i11 = i31;
                            i10 = i20;
                            unsafe = unsafe6;
                            i9 = i8;
                        }
                        i7 = i11;
                        i20 = i10;
                        i21 = i9;
                        i17 = zzhl.zza(b, bArr, i7, i2, zze(t), zzhnVar);
                        zzkoVar = this;
                        t2 = t;
                        bArr2 = bArr;
                        i14 = i2;
                        zzhnVar2 = zzhnVar;
                        i19 = i6;
                        i18 = i5;
                        unsafe5 = unsafe;
                        i16 = 1048575;
                        i15 = -1;
                    }
                    i21 = i8;
                    i18 = i5;
                    i16 = 1048575;
                    i15 = -1;
                }
                i17 = zzhl.zza(b, bArr, i7, i2, zze(t), zzhnVar);
                zzkoVar = this;
                t2 = t;
                bArr2 = bArr;
                i14 = i2;
                zzhnVar2 = zzhnVar;
                i19 = i6;
                i18 = i5;
                unsafe5 = unsafe;
                i16 = 1048575;
                i15 = -1;
            }
            int i32 = i20;
            Unsafe unsafe7 = unsafe5;
            if (i21 != 1048575) {
                unsafe7.putInt(t, i21, i32);
            }
            if (i17 != i2) {
                throw zzjk.zzg();
            }
            return;
        }
        zza((zzko<T>) t, bArr, i, i2, 0, zzhnVar);
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final void zzc(T t) {
        int i;
        int i2 = this.zzm;
        while (true) {
            i = this.zzn;
            if (i2 >= i) {
                break;
            }
            long zzd = zzd(this.zzl[i2]) & 1048575;
            Object zzf = zzma.zzf(t, zzd);
            if (zzf != null) {
                zzma.zza(t, zzd, this.zzs.zze(zzf));
            }
            i2++;
        }
        int length = this.zzl.length;
        while (i < length) {
            this.zzp.zzb(t, this.zzl[i]);
            i++;
        }
        this.zzq.zzd(t);
        if (this.zzh) {
            this.zzr.zzc(t);
        }
    }

    private final <UT, UB> UB zza(Object obj, int i, UB ub, zzlu<UT, UB> zzluVar) {
        zzjg zzc;
        int i2 = this.zzc[i];
        Object zzf = zzma.zzf(obj, zzd(i) & 1048575);
        return (zzf == null || (zzc = zzc(i)) == null) ? ub : (UB) zza(i, i2, this.zzs.zza(zzf), zzc, (zzjg) ub, (zzlu<UT, zzjg>) zzluVar);
    }

    private final <K, V, UT, UB> UB zza(int i, int i2, Map<K, V> map, zzjg zzjgVar, UB ub, zzlu<UT, UB> zzluVar) {
        zzkf<?, ?> zzb2 = this.zzs.zzb(zzb(i));
        Iterator<Map.Entry<K, V>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<K, V> next = it.next();
            if (!zzjgVar.zza(((Integer) next.getValue()).intValue())) {
                if (ub == null) {
                    ub = zzluVar.zza();
                }
                zzib zzc = zzht.zzc(zzkc.zza(zzb2, next.getKey(), next.getValue()));
                try {
                    zzkc.zza(zzc.zzb(), zzb2, next.getKey(), next.getValue());
                    zzluVar.zza((zzlu<UT, UB>) ub, i2, zzc.zza());
                    it.remove();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return ub;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.android.gms.internal.vision.zzlc
    public final boolean zzd(T t) {
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
            int zzd = zzd(i6);
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
            if (((268435456 & zzd) != 0) && !zza((zzko<T>) t, i6, i2, i, i10)) {
                return false;
            }
            int i11 = (267386880 & zzd) >>> 20;
            if (i11 == 9 || i11 == 17) {
                if (zza((zzko<T>) t, i6, i2, i, i10) && !zza(t, zzd, zza(i6))) {
                    return false;
                }
            } else {
                if (i11 != 27) {
                    if (i11 == 60 || i11 == 68) {
                        if (zza((zzko<T>) t, i7, i6) && !zza(t, zzd, zza(i6))) {
                            return false;
                        }
                    } else if (i11 != 49) {
                        if (i11 == 50 && !this.zzs.zzc(zzma.zzf(t, zzd & 1048575)).isEmpty()) {
                            this.zzs.zzb(zzb(i6));
                            throw null;
                        }
                    }
                }
                List list = (List) zzma.zzf(t, zzd & 1048575);
                if (!list.isEmpty()) {
                    zzlc zza2 = zza(i6);
                    int i12 = 0;
                    while (true) {
                        if (i12 >= list.size()) {
                            break;
                        } else if (!zza2.zzd(list.get(i12))) {
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
    private static boolean zza(Object obj, int i, zzlc zzlcVar) {
        return zzlcVar.zzd(zzma.zzf(obj, i & 1048575));
    }

    private static void zza(int i, Object obj, zzmr zzmrVar) throws IOException {
        if (obj instanceof String) {
            zzmrVar.zza(i, (String) obj);
        } else {
            zzmrVar.zza(i, (zzht) obj);
        }
    }

    private final int zzd(int i) {
        return this.zzc[i + 1];
    }

    private final int zze(int i) {
        return this.zzc[i + 2];
    }

    private static <T> double zzb(T t, long j) {
        return ((Double) zzma.zzf(t, j)).doubleValue();
    }

    private static <T> float zzc(T t, long j) {
        return ((Float) zzma.zzf(t, j)).floatValue();
    }

    private static <T> int zzd(T t, long j) {
        return ((Integer) zzma.zzf(t, j)).intValue();
    }

    private static <T> long zze(T t, long j) {
        return ((Long) zzma.zzf(t, j)).longValue();
    }

    private static <T> boolean zzf(T t, long j) {
        return ((Boolean) zzma.zzf(t, j)).booleanValue();
    }

    private final boolean zzc(T t, T t2, int i) {
        return zza((zzko<T>) t, i) == zza((zzko<T>) t2, i);
    }

    private final boolean zza(T t, int i, int i2, int i3, int i4) {
        if (i2 == 1048575) {
            return zza((zzko<T>) t, i);
        }
        return (i3 & i4) != 0;
    }

    private final boolean zza(T t, int i) {
        int zze = zze(i);
        long j = zze & 1048575;
        if (j != 1048575) {
            return (zzma.zza(t, j) & (1 << (zze >>> 20))) != 0;
        }
        int zzd = zzd(i);
        long j2 = zzd & 1048575;
        switch ((zzd & 267386880) >>> 20) {
            case 0:
                return zzma.zze(t, j2) != 0.0d;
            case 1:
                return zzma.zzd(t, j2) != 0.0f;
            case 2:
                return zzma.zzb(t, j2) != 0;
            case 3:
                return zzma.zzb(t, j2) != 0;
            case 4:
                return zzma.zza(t, j2) != 0;
            case 5:
                return zzma.zzb(t, j2) != 0;
            case 6:
                return zzma.zza(t, j2) != 0;
            case 7:
                return zzma.zzc(t, j2);
            case 8:
                Object zzf = zzma.zzf(t, j2);
                if (zzf instanceof String) {
                    return !((String) zzf).isEmpty();
                } else if (!(zzf instanceof zzht)) {
                    throw new IllegalArgumentException();
                } else {
                    return !zzht.zza.equals(zzf);
                }
            case 9:
                return zzma.zzf(t, j2) != null;
            case 10:
                return !zzht.zza.equals(zzma.zzf(t, j2));
            case 11:
                return zzma.zza(t, j2) != 0;
            case 12:
                return zzma.zza(t, j2) != 0;
            case 13:
                return zzma.zza(t, j2) != 0;
            case 14:
                return zzma.zzb(t, j2) != 0;
            case 15:
                return zzma.zza(t, j2) != 0;
            case 16:
                return zzma.zzb(t, j2) != 0;
            case 17:
                return zzma.zzf(t, j2) != null;
            default:
                throw new IllegalArgumentException();
        }
    }

    private final void zzb(T t, int i) {
        int zze = zze(i);
        long j = 1048575 & zze;
        if (j == 1048575) {
            return;
        }
        zzma.zza((Object) t, j, (1 << (zze >>> 20)) | zzma.zza(t, j));
    }

    private final boolean zza(T t, int i, int i2) {
        return zzma.zza(t, (long) (zze(i2) & 1048575)) == i;
    }

    private final void zzb(T t, int i, int i2) {
        zzma.zza((Object) t, zze(i2) & 1048575, i);
    }

    private final int zzg(int i) {
        if (i < this.zze || i > this.zzf) {
            return -1;
        }
        return zzb(i, 0);
    }

    private final int zza(int i, int i2) {
        if (i < this.zze || i > this.zzf) {
            return -1;
        }
        return zzb(i, i2);
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
}
