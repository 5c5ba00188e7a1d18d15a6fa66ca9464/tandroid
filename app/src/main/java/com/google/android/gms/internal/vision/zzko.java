package com.google.android.gms.internal.vision;

import androidx.core.text.HtmlCompat;
import com.google.android.gms.internal.vision.zzjb;
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
/* compiled from: com.google.android.gms:play-services-vision-common@@19.1.3 */
/* loaded from: classes3.dex */
public final class zzko<T> implements zzlc<T> {
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
    private final zzlu<?, ?> zzq;
    private final zziq<?> zzr;
    private final zzkh zzs;

    private zzko(int[] iArr, Object[] objArr, int i, int i2, zzkk zzkkVar, boolean z, boolean z2, int[] iArr2, int i3, int i4, zzks zzksVar, zzju zzjuVar, zzlu<?, ?> zzluVar, zziq<?> zziqVar, zzkh zzkhVar) {
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

    /* JADX WARN: Removed duplicated region for block: B:157:0x0349  */
    /* JADX WARN: Removed duplicated region for block: B:173:0x03a2  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x03af A[ADDED_TO_REGION] */
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
        if (zzkiVar instanceof zzla) {
            zzla zzlaVar = (zzla) zzkiVar;
            int i31 = 0;
            boolean z = zzlaVar.zza() == zzkz.zzb;
            String zzd = zzlaVar.zzd();
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
            Object[] zze = zzlaVar.zze();
            Class<?> cls2 = zzlaVar.zzc().getClass();
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
            return new zzko<>(iArr3, objArr, i6, i5, zzlaVar.zzc(), z, false, iArr, i2, i61, zzksVar, zzjuVar, zzluVar, zziqVar, zzkhVar);
        }
        ((zzlr) zzkiVar).zza();
        int i97 = zzkz.zzb;
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

    @Override // com.google.android.gms.internal.vision.zzlc
    public final T zza() {
        return (T) this.zzo.zza(this.zzg);
    }

    @Override // com.google.android.gms.internal.vision.zzlc
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
                        if (!zzc(t, t2, i) || Double.doubleToLongBits(zzma.zze(t, j)) != Double.doubleToLongBits(zzma.zze(t2, j))) {
                            z = false;
                            break;
                        }
                        break;
                    case 1:
                        if (!zzc(t, t2, i) || Float.floatToIntBits(zzma.zzd(t, j)) != Float.floatToIntBits(zzma.zzd(t2, j))) {
                            z = false;
                            break;
                        }
                        break;
                    case 2:
                        if (!zzc(t, t2, i) || zzma.zzb(t, j) != zzma.zzb(t2, j)) {
                            z = false;
                            break;
                        }
                        break;
                    case 3:
                        if (!zzc(t, t2, i) || zzma.zzb(t, j) != zzma.zzb(t2, j)) {
                            z = false;
                            break;
                        }
                        break;
                    case 4:
                        if (!zzc(t, t2, i) || zzma.zza(t, j) != zzma.zza(t2, j)) {
                            z = false;
                            break;
                        }
                        break;
                    case 5:
                        if (!zzc(t, t2, i) || zzma.zzb(t, j) != zzma.zzb(t2, j)) {
                            z = false;
                            break;
                        }
                        break;
                    case 6:
                        if (!zzc(t, t2, i) || zzma.zza(t, j) != zzma.zza(t2, j)) {
                            z = false;
                            break;
                        }
                        break;
                    case 7:
                        if (!zzc(t, t2, i) || zzma.zzc(t, j) != zzma.zzc(t2, j)) {
                            z = false;
                            break;
                        }
                        break;
                    case 8:
                        if (!zzc(t, t2, i) || !zzle.zza(zzma.zzf(t, j), zzma.zzf(t2, j))) {
                            z = false;
                            break;
                        }
                        break;
                    case 9:
                        if (!zzc(t, t2, i) || !zzle.zza(zzma.zzf(t, j), zzma.zzf(t2, j))) {
                            z = false;
                            break;
                        }
                        break;
                    case 10:
                        if (!zzc(t, t2, i) || !zzle.zza(zzma.zzf(t, j), zzma.zzf(t2, j))) {
                            z = false;
                            break;
                        }
                        break;
                    case 11:
                        if (!zzc(t, t2, i) || zzma.zza(t, j) != zzma.zza(t2, j)) {
                            z = false;
                            break;
                        }
                        break;
                    case 12:
                        if (!zzc(t, t2, i) || zzma.zza(t, j) != zzma.zza(t2, j)) {
                            z = false;
                            break;
                        }
                        break;
                    case 13:
                        if (!zzc(t, t2, i) || zzma.zza(t, j) != zzma.zza(t2, j)) {
                            z = false;
                            break;
                        }
                        break;
                    case 14:
                        if (!zzc(t, t2, i) || zzma.zzb(t, j) != zzma.zzb(t2, j)) {
                            z = false;
                            break;
                        }
                        break;
                    case 15:
                        if (!zzc(t, t2, i) || zzma.zza(t, j) != zzma.zza(t2, j)) {
                            z = false;
                            break;
                        }
                        break;
                    case 16:
                        if (!zzc(t, t2, i) || zzma.zzb(t, j) != zzma.zzb(t2, j)) {
                            z = false;
                            break;
                        }
                        break;
                    case 17:
                        if (!zzc(t, t2, i) || !zzle.zza(zzma.zzf(t, j), zzma.zzf(t2, j))) {
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
                        long zze = zze(i) & 1048575;
                        if (zzma.zza(t, zze) != zzma.zza(t2, zze) || !zzle.zza(zzma.zzf(t, j), zzma.zzf(t2, j))) {
                            z = false;
                            break;
                        }
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
        int length = this.zzc.length;
        int i = 0;
        for (int i2 = 0; i2 < length; i2 += 3) {
            int zzd = zzd(i2);
            int i3 = this.zzc[i2];
            long j = 1048575 & zzd;
            int i4 = 37;
            switch ((zzd & 267386880) >>> 20) {
                case 0:
                    i = (i * 53) + zzjf.zza(Double.doubleToLongBits(zzma.zze(t, j)));
                    break;
                case 1:
                    i = (i * 53) + Float.floatToIntBits(zzma.zzd(t, j));
                    break;
                case 2:
                    i = (i * 53) + zzjf.zza(zzma.zzb(t, j));
                    break;
                case 3:
                    i = (i * 53) + zzjf.zza(zzma.zzb(t, j));
                    break;
                case 4:
                    i = (i * 53) + zzma.zza(t, j);
                    break;
                case 5:
                    i = (i * 53) + zzjf.zza(zzma.zzb(t, j));
                    break;
                case 6:
                    i = (i * 53) + zzma.zza(t, j);
                    break;
                case 7:
                    i = (i * 53) + zzjf.zza(zzma.zzc(t, j));
                    break;
                case 8:
                    i = (i * 53) + ((String) zzma.zzf(t, j)).hashCode();
                    break;
                case 9:
                    Object zzf = zzma.zzf(t, j);
                    if (zzf != null) {
                        i4 = zzf.hashCode();
                    }
                    i = (i * 53) + i4;
                    break;
                case 10:
                    i = (i * 53) + zzma.zzf(t, j).hashCode();
                    break;
                case 11:
                    i = (i * 53) + zzma.zza(t, j);
                    break;
                case 12:
                    i = (i * 53) + zzma.zza(t, j);
                    break;
                case 13:
                    i = (i * 53) + zzma.zza(t, j);
                    break;
                case 14:
                    i = (i * 53) + zzjf.zza(zzma.zzb(t, j));
                    break;
                case 15:
                    i = (i * 53) + zzma.zza(t, j);
                    break;
                case 16:
                    i = (i * 53) + zzjf.zza(zzma.zzb(t, j));
                    break;
                case 17:
                    Object zzf2 = zzma.zzf(t, j);
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
                    i = (i * 53) + zzma.zzf(t, j).hashCode();
                    break;
                case 50:
                    i = (i * 53) + zzma.zzf(t, j).hashCode();
                    break;
                case 51:
                    if (zza((zzko<T>) t, i3, i2)) {
                        i = (i * 53) + zzjf.zza(Double.doubleToLongBits(zzb(t, j)));
                        break;
                    } else {
                        break;
                    }
                case 52:
                    if (zza((zzko<T>) t, i3, i2)) {
                        i = (i * 53) + Float.floatToIntBits(zzc(t, j));
                        break;
                    } else {
                        break;
                    }
                case 53:
                    if (zza((zzko<T>) t, i3, i2)) {
                        i = (i * 53) + zzjf.zza(zze(t, j));
                        break;
                    } else {
                        break;
                    }
                case 54:
                    if (zza((zzko<T>) t, i3, i2)) {
                        i = (i * 53) + zzjf.zza(zze(t, j));
                        break;
                    } else {
                        break;
                    }
                case 55:
                    if (zza((zzko<T>) t, i3, i2)) {
                        i = (i * 53) + zzd(t, j);
                        break;
                    } else {
                        break;
                    }
                case UndoView.ACTION_USERNAME_COPIED /* 56 */:
                    if (zza((zzko<T>) t, i3, i2)) {
                        i = (i * 53) + zzjf.zza(zze(t, j));
                        break;
                    } else {
                        break;
                    }
                case UndoView.ACTION_HASHTAG_COPIED /* 57 */:
                    if (zza((zzko<T>) t, i3, i2)) {
                        i = (i * 53) + zzd(t, j);
                        break;
                    } else {
                        break;
                    }
                case UndoView.ACTION_TEXT_COPIED /* 58 */:
                    if (zza((zzko<T>) t, i3, i2)) {
                        i = (i * 53) + zzjf.zza(zzf(t, j));
                        break;
                    } else {
                        break;
                    }
                case 59:
                    if (zza((zzko<T>) t, i3, i2)) {
                        i = (i * 53) + ((String) zzma.zzf(t, j)).hashCode();
                        break;
                    } else {
                        break;
                    }
                case UndoView.ACTION_PHONE_COPIED /* 60 */:
                    if (zza((zzko<T>) t, i3, i2)) {
                        i = (i * 53) + zzma.zzf(t, j).hashCode();
                        break;
                    } else {
                        break;
                    }
                case UndoView.ACTION_SHARE_BACKGROUND /* 61 */:
                    if (zza((zzko<T>) t, i3, i2)) {
                        i = (i * 53) + zzma.zzf(t, j).hashCode();
                        break;
                    } else {
                        break;
                    }
                case 62:
                    if (zza((zzko<T>) t, i3, i2)) {
                        i = (i * 53) + zzd(t, j);
                        break;
                    } else {
                        break;
                    }
                case HtmlCompat.FROM_HTML_MODE_COMPACT /* 63 */:
                    if (zza((zzko<T>) t, i3, i2)) {
                        i = (i * 53) + zzd(t, j);
                        break;
                    } else {
                        break;
                    }
                case 64:
                    if (zza((zzko<T>) t, i3, i2)) {
                        i = (i * 53) + zzd(t, j);
                        break;
                    } else {
                        break;
                    }
                case VoIPService.CALL_MIN_LAYER /* 65 */:
                    if (zza((zzko<T>) t, i3, i2)) {
                        i = (i * 53) + zzjf.zza(zze(t, j));
                        break;
                    } else {
                        break;
                    }
                case 66:
                    if (zza((zzko<T>) t, i3, i2)) {
                        i = (i * 53) + zzd(t, j);
                        break;
                    } else {
                        break;
                    }
                case 67:
                    if (zza((zzko<T>) t, i3, i2)) {
                        i = (i * 53) + zzjf.zza(zze(t, j));
                        break;
                    } else {
                        break;
                    }
                case 68:
                    if (zza((zzko<T>) t, i3, i2)) {
                        i = (i * 53) + zzma.zzf(t, j).hashCode();
                        break;
                    } else {
                        break;
                    }
            }
        }
        int hashCode = (i * 53) + this.zzq.zzb(t).hashCode();
        if (this.zzh) {
            return (hashCode * 53) + this.zzr.zza(t).hashCode();
        }
        return hashCode;
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final void zzb(T t, T t2) {
        if (t2 == null) {
            throw new NullPointerException();
        }
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
                    zzle.zza(this.zzs, t, t2, j);
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
                    if (zza((zzko<T>) t2, i2, i)) {
                        zzma.zza(t, j, zzma.zzf(t2, j));
                        zzb((zzko<T>) t, i2, i);
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
        } else if (zzf2 != null) {
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
        } else if (zzf != null) {
            zzma.zza(t, j, zzf);
            zzb((zzko<T>) t, i2, i);
        }
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final int zzb(T t) {
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
                int zzd = zzd(i5);
                int i7 = (zzd & i2) >>> 20;
                int i8 = this.zzc[i5];
                long j2 = zzd & 1048575;
                if (i7 >= zziv.DOUBLE_LIST_PACKED.zza() && i7 <= zziv.SINT64_LIST_PACKED.zza()) {
                    int i9 = this.zzc[i5 + 2];
                }
                switch (i7) {
                    case 0:
                        if (zza((zzko<T>) t, i5)) {
                            i6 += zzii.zzb(i8, (double) FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE);
                            break;
                        } else {
                            break;
                        }
                    case 1:
                        if (zza((zzko<T>) t, i5)) {
                            i6 += zzii.zzb(i8, 0.0f);
                            break;
                        } else {
                            break;
                        }
                    case 2:
                        if (zza((zzko<T>) t, i5)) {
                            i6 += zzii.zzd(i8, zzma.zzb(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 3:
                        if (zza((zzko<T>) t, i5)) {
                            i6 += zzii.zze(i8, zzma.zzb(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 4:
                        if (zza((zzko<T>) t, i5)) {
                            i6 += zzii.zzf(i8, zzma.zza(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 5:
                        if (zza((zzko<T>) t, i5)) {
                            i6 += zzii.zzg(i8, 0L);
                            break;
                        } else {
                            break;
                        }
                    case 6:
                        if (zza((zzko<T>) t, i5)) {
                            i6 += zzii.zzi(i8, 0);
                            break;
                        } else {
                            break;
                        }
                    case 7:
                        if (zza((zzko<T>) t, i5)) {
                            i6 += zzii.zzb(i8, true);
                            break;
                        } else {
                            break;
                        }
                    case 8:
                        if (zza((zzko<T>) t, i5)) {
                            Object zzf = zzma.zzf(t, j2);
                            if (zzf instanceof zzht) {
                                i6 += zzii.zzc(i8, (zzht) zzf);
                                break;
                            } else {
                                i6 += zzii.zzb(i8, (String) zzf);
                                break;
                            }
                        } else {
                            break;
                        }
                    case 9:
                        if (zza((zzko<T>) t, i5)) {
                            i6 += zzle.zza(i8, zzma.zzf(t, j2), zza(i5));
                            break;
                        } else {
                            break;
                        }
                    case 10:
                        if (zza((zzko<T>) t, i5)) {
                            i6 += zzii.zzc(i8, (zzht) zzma.zzf(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 11:
                        if (zza((zzko<T>) t, i5)) {
                            i6 += zzii.zzg(i8, zzma.zza(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 12:
                        if (zza((zzko<T>) t, i5)) {
                            i6 += zzii.zzk(i8, zzma.zza(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 13:
                        if (zza((zzko<T>) t, i5)) {
                            i6 += zzii.zzj(i8, 0);
                            break;
                        } else {
                            break;
                        }
                    case 14:
                        if (zza((zzko<T>) t, i5)) {
                            i6 += zzii.zzh(i8, 0L);
                            break;
                        } else {
                            break;
                        }
                    case 15:
                        if (zza((zzko<T>) t, i5)) {
                            i6 += zzii.zzh(i8, zzma.zza(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 16:
                        if (zza((zzko<T>) t, i5)) {
                            i6 += zzii.zzf(i8, zzma.zzb(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 17:
                        if (zza((zzko<T>) t, i5)) {
                            i6 += zzii.zzc(i8, (zzkk) zzma.zzf(t, j2), zza(i5));
                            break;
                        } else {
                            break;
                        }
                    case 18:
                        i6 += zzle.zzi(i8, zza(t, j2), false);
                        break;
                    case 19:
                        i6 += zzle.zzh(i8, zza(t, j2), false);
                        break;
                    case 20:
                        i6 += zzle.zza(i8, (List<Long>) zza(t, j2), false);
                        break;
                    case 21:
                        i6 += zzle.zzb(i8, (List<Long>) zza(t, j2), false);
                        break;
                    case 22:
                        i6 += zzle.zze(i8, zza(t, j2), false);
                        break;
                    case 23:
                        i6 += zzle.zzi(i8, zza(t, j2), false);
                        break;
                    case 24:
                        i6 += zzle.zzh(i8, zza(t, j2), false);
                        break;
                    case 25:
                        i6 += zzle.zzj(i8, zza(t, j2), false);
                        break;
                    case 26:
                        i6 += zzle.zza(i8, zza(t, j2));
                        break;
                    case 27:
                        i6 += zzle.zza(i8, zza(t, j2), zza(i5));
                        break;
                    case 28:
                        i6 += zzle.zzb(i8, zza(t, j2));
                        break;
                    case NalUnitTypes.NAL_TYPE_RSV_VCL29 /* 29 */:
                        i6 += zzle.zzf(i8, zza(t, j2), false);
                        break;
                    case 30:
                        i6 += zzle.zzd(i8, zza(t, j2), false);
                        break;
                    case 31:
                        i6 += zzle.zzh(i8, zza(t, j2), false);
                        break;
                    case 32:
                        i6 += zzle.zzi(i8, zza(t, j2), false);
                        break;
                    case 33:
                        i6 += zzle.zzg(i8, zza(t, j2), false);
                        break;
                    case 34:
                        i6 += zzle.zzc(i8, zza(t, j2), false);
                        break;
                    case 35:
                        int zzi = zzle.zzi((List) unsafe.getObject(t, j2));
                        if (zzi > 0) {
                            i6 += zzii.zze(i8) + zzii.zzg(zzi) + zzi;
                            break;
                        } else {
                            break;
                        }
                    case 36:
                        int zzh = zzle.zzh((List) unsafe.getObject(t, j2));
                        if (zzh > 0) {
                            i6 += zzii.zze(i8) + zzii.zzg(zzh) + zzh;
                            break;
                        } else {
                            break;
                        }
                    case 37:
                        int zza2 = zzle.zza((List) unsafe.getObject(t, j2));
                        if (zza2 > 0) {
                            i6 += zzii.zze(i8) + zzii.zzg(zza2) + zza2;
                            break;
                        } else {
                            break;
                        }
                    case 38:
                        int zzb2 = zzle.zzb((List) unsafe.getObject(t, j2));
                        if (zzb2 > 0) {
                            i6 += zzii.zze(i8) + zzii.zzg(zzb2) + zzb2;
                            break;
                        } else {
                            break;
                        }
                    case 39:
                        int zze = zzle.zze((List) unsafe.getObject(t, j2));
                        if (zze > 0) {
                            i6 += zzii.zze(i8) + zzii.zzg(zze) + zze;
                            break;
                        } else {
                            break;
                        }
                    case 40:
                        int zzi2 = zzle.zzi((List) unsafe.getObject(t, j2));
                        if (zzi2 > 0) {
                            i6 += zzii.zze(i8) + zzii.zzg(zzi2) + zzi2;
                            break;
                        } else {
                            break;
                        }
                    case 41:
                        int zzh2 = zzle.zzh((List) unsafe.getObject(t, j2));
                        if (zzh2 > 0) {
                            i6 += zzii.zze(i8) + zzii.zzg(zzh2) + zzh2;
                            break;
                        } else {
                            break;
                        }
                    case 42:
                        int zzj = zzle.zzj((List) unsafe.getObject(t, j2));
                        if (zzj > 0) {
                            i6 += zzii.zze(i8) + zzii.zzg(zzj) + zzj;
                            break;
                        } else {
                            break;
                        }
                    case 43:
                        int zzf2 = zzle.zzf((List) unsafe.getObject(t, j2));
                        if (zzf2 > 0) {
                            i6 += zzii.zze(i8) + zzii.zzg(zzf2) + zzf2;
                            break;
                        } else {
                            break;
                        }
                    case 44:
                        int zzd2 = zzle.zzd((List) unsafe.getObject(t, j2));
                        if (zzd2 > 0) {
                            i6 += zzii.zze(i8) + zzii.zzg(zzd2) + zzd2;
                            break;
                        } else {
                            break;
                        }
                    case 45:
                        int zzh3 = zzle.zzh((List) unsafe.getObject(t, j2));
                        if (zzh3 > 0) {
                            i6 += zzii.zze(i8) + zzii.zzg(zzh3) + zzh3;
                            break;
                        } else {
                            break;
                        }
                    case 46:
                        int zzi3 = zzle.zzi((List) unsafe.getObject(t, j2));
                        if (zzi3 > 0) {
                            i6 += zzii.zze(i8) + zzii.zzg(zzi3) + zzi3;
                            break;
                        } else {
                            break;
                        }
                    case 47:
                        int zzg = zzle.zzg((List) unsafe.getObject(t, j2));
                        if (zzg > 0) {
                            i6 += zzii.zze(i8) + zzii.zzg(zzg) + zzg;
                            break;
                        } else {
                            break;
                        }
                    case 48:
                        int zzc = zzle.zzc((List) unsafe.getObject(t, j2));
                        if (zzc > 0) {
                            i6 += zzii.zze(i8) + zzii.zzg(zzc) + zzc;
                            break;
                        } else {
                            break;
                        }
                    case 49:
                        i6 += zzle.zzb(i8, (List<zzkk>) zza(t, j2), zza(i5));
                        break;
                    case 50:
                        i6 += this.zzs.zza(i8, zzma.zzf(t, j2), zzb(i5));
                        break;
                    case 51:
                        if (zza((zzko<T>) t, i8, i5)) {
                            i6 += zzii.zzb(i8, (double) FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE);
                            break;
                        } else {
                            break;
                        }
                    case 52:
                        if (zza((zzko<T>) t, i8, i5)) {
                            i6 += zzii.zzb(i8, 0.0f);
                            break;
                        } else {
                            break;
                        }
                    case 53:
                        if (zza((zzko<T>) t, i8, i5)) {
                            i6 += zzii.zzd(i8, zze(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 54:
                        if (zza((zzko<T>) t, i8, i5)) {
                            i6 += zzii.zze(i8, zze(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 55:
                        if (zza((zzko<T>) t, i8, i5)) {
                            i6 += zzii.zzf(i8, zzd(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case UndoView.ACTION_USERNAME_COPIED /* 56 */:
                        if (zza((zzko<T>) t, i8, i5)) {
                            i6 += zzii.zzg(i8, 0L);
                            break;
                        } else {
                            break;
                        }
                    case UndoView.ACTION_HASHTAG_COPIED /* 57 */:
                        if (zza((zzko<T>) t, i8, i5)) {
                            i6 += zzii.zzi(i8, 0);
                            break;
                        } else {
                            break;
                        }
                    case UndoView.ACTION_TEXT_COPIED /* 58 */:
                        if (zza((zzko<T>) t, i8, i5)) {
                            i6 += zzii.zzb(i8, true);
                            break;
                        } else {
                            break;
                        }
                    case 59:
                        if (zza((zzko<T>) t, i8, i5)) {
                            Object zzf3 = zzma.zzf(t, j2);
                            if (zzf3 instanceof zzht) {
                                i6 += zzii.zzc(i8, (zzht) zzf3);
                                break;
                            } else {
                                i6 += zzii.zzb(i8, (String) zzf3);
                                break;
                            }
                        } else {
                            break;
                        }
                    case UndoView.ACTION_PHONE_COPIED /* 60 */:
                        if (zza((zzko<T>) t, i8, i5)) {
                            i6 += zzle.zza(i8, zzma.zzf(t, j2), zza(i5));
                            break;
                        } else {
                            break;
                        }
                    case UndoView.ACTION_SHARE_BACKGROUND /* 61 */:
                        if (zza((zzko<T>) t, i8, i5)) {
                            i6 += zzii.zzc(i8, (zzht) zzma.zzf(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 62:
                        if (zza((zzko<T>) t, i8, i5)) {
                            i6 += zzii.zzg(i8, zzd(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case HtmlCompat.FROM_HTML_MODE_COMPACT /* 63 */:
                        if (zza((zzko<T>) t, i8, i5)) {
                            i6 += zzii.zzk(i8, zzd(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 64:
                        if (zza((zzko<T>) t, i8, i5)) {
                            i6 += zzii.zzj(i8, 0);
                            break;
                        } else {
                            break;
                        }
                    case VoIPService.CALL_MIN_LAYER /* 65 */:
                        if (zza((zzko<T>) t, i8, i5)) {
                            i6 += zzii.zzh(i8, 0L);
                            break;
                        } else {
                            break;
                        }
                    case 66:
                        if (zza((zzko<T>) t, i8, i5)) {
                            i6 += zzii.zzh(i8, zzd(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 67:
                        if (zza((zzko<T>) t, i8, i5)) {
                            i6 += zzii.zzf(i8, zze(t, j2));
                            break;
                        } else {
                            break;
                        }
                    case 68:
                        if (zza((zzko<T>) t, i8, i5)) {
                            i6 += zzii.zzc(i8, (zzkk) zzma.zzf(t, j2), zza(i5));
                            break;
                        } else {
                            break;
                        }
                }
                i5 += 3;
                i2 = 267386880;
            }
            return i6 + zza((zzlu) this.zzq, (Object) t);
        }
        Unsafe unsafe2 = zzb;
        int i10 = 0;
        int i11 = 0;
        int i12 = 1048575;
        int i13 = 0;
        while (i10 < this.zzc.length) {
            int zzd3 = zzd(i10);
            int[] iArr = this.zzc;
            int i14 = iArr[i10];
            int i15 = (zzd3 & 267386880) >>> 20;
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
            long j3 = zzd3 & i3;
            switch (i15) {
                case 0:
                    j = 0;
                    if ((i13 & i) != 0) {
                        i11 += zzii.zzb(i14, (double) FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE);
                        break;
                    } else {
                        break;
                    }
                case 1:
                    j = 0;
                    if ((i13 & i) != 0) {
                        i11 += zzii.zzb(i14, 0.0f);
                        break;
                    } else {
                        break;
                    }
                case 2:
                    j = 0;
                    if ((i & i13) != 0) {
                        i11 += zzii.zzd(i14, unsafe2.getLong(t, j3));
                        break;
                    } else {
                        break;
                    }
                case 3:
                    j = 0;
                    if ((i & i13) != 0) {
                        i11 += zzii.zze(i14, unsafe2.getLong(t, j3));
                        break;
                    } else {
                        break;
                    }
                case 4:
                    j = 0;
                    if ((i & i13) != 0) {
                        i11 += zzii.zzf(i14, unsafe2.getInt(t, j3));
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
                        i11 += zzii.zzg(i14, 0L);
                        break;
                    }
                case 6:
                    if ((i13 & i) != 0) {
                        i11 += zzii.zzi(i14, 0);
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 7:
                    if ((i13 & i) != 0) {
                        i11 += zzii.zzb(i14, true);
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 8:
                    if ((i13 & i) != 0) {
                        Object object = unsafe2.getObject(t, j3);
                        if (object instanceof zzht) {
                            i11 += zzii.zzc(i14, (zzht) object);
                            j = 0;
                            break;
                        } else {
                            i11 += zzii.zzb(i14, (String) object);
                            j = 0;
                            break;
                        }
                    } else {
                        j = 0;
                        break;
                    }
                case 9:
                    if ((i13 & i) != 0) {
                        i11 += zzle.zza(i14, unsafe2.getObject(t, j3), zza(i10));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 10:
                    if ((i13 & i) != 0) {
                        i11 += zzii.zzc(i14, (zzht) unsafe2.getObject(t, j3));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 11:
                    if ((i13 & i) != 0) {
                        i11 += zzii.zzg(i14, unsafe2.getInt(t, j3));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 12:
                    if ((i13 & i) != 0) {
                        i11 += zzii.zzk(i14, unsafe2.getInt(t, j3));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 13:
                    if ((i13 & i) != 0) {
                        i11 += zzii.zzj(i14, 0);
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 14:
                    if ((i13 & i) != 0) {
                        i11 += zzii.zzh(i14, 0L);
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 15:
                    if ((i13 & i) != 0) {
                        i11 += zzii.zzh(i14, unsafe2.getInt(t, j3));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 16:
                    if ((i13 & i) != 0) {
                        i11 += zzii.zzf(i14, unsafe2.getLong(t, j3));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 17:
                    if ((i13 & i) != 0) {
                        i11 += zzii.zzc(i14, (zzkk) unsafe2.getObject(t, j3), zza(i10));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 18:
                    i11 += zzle.zzi(i14, (List) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 19:
                    i11 += zzle.zzh(i14, (List) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 20:
                    i11 += zzle.zza(i14, (List<Long>) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 21:
                    i11 += zzle.zzb(i14, (List<Long>) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 22:
                    i11 += zzle.zze(i14, (List) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 23:
                    i11 += zzle.zzi(i14, (List) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 24:
                    i11 += zzle.zzh(i14, (List) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 25:
                    i11 += zzle.zzj(i14, (List) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 26:
                    i11 += zzle.zza(i14, (List) unsafe2.getObject(t, j3));
                    j = 0;
                    break;
                case 27:
                    i11 += zzle.zza(i14, (List<?>) unsafe2.getObject(t, j3), zza(i10));
                    j = 0;
                    break;
                case 28:
                    i11 += zzle.zzb(i14, (List) unsafe2.getObject(t, j3));
                    j = 0;
                    break;
                case NalUnitTypes.NAL_TYPE_RSV_VCL29 /* 29 */:
                    i11 += zzle.zzf(i14, (List) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 30:
                    i11 += zzle.zzd(i14, (List) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 31:
                    i11 += zzle.zzh(i14, (List) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 32:
                    i11 += zzle.zzi(i14, (List) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 33:
                    i11 += zzle.zzg(i14, (List) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 34:
                    i11 += zzle.zzc(i14, (List) unsafe2.getObject(t, j3), false);
                    j = 0;
                    break;
                case 35:
                    int zzi4 = zzle.zzi((List) unsafe2.getObject(t, j3));
                    if (zzi4 > 0) {
                        i11 += zzii.zze(i14) + zzii.zzg(zzi4) + zzi4;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 36:
                    int zzh4 = zzle.zzh((List) unsafe2.getObject(t, j3));
                    if (zzh4 > 0) {
                        i11 += zzii.zze(i14) + zzii.zzg(zzh4) + zzh4;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 37:
                    int zza3 = zzle.zza((List) unsafe2.getObject(t, j3));
                    if (zza3 > 0) {
                        i11 += zzii.zze(i14) + zzii.zzg(zza3) + zza3;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 38:
                    int zzb3 = zzle.zzb((List) unsafe2.getObject(t, j3));
                    if (zzb3 > 0) {
                        i11 += zzii.zze(i14) + zzii.zzg(zzb3) + zzb3;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 39:
                    int zze2 = zzle.zze((List) unsafe2.getObject(t, j3));
                    if (zze2 > 0) {
                        i11 += zzii.zze(i14) + zzii.zzg(zze2) + zze2;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 40:
                    int zzi5 = zzle.zzi((List) unsafe2.getObject(t, j3));
                    if (zzi5 > 0) {
                        i11 += zzii.zze(i14) + zzii.zzg(zzi5) + zzi5;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 41:
                    int zzh5 = zzle.zzh((List) unsafe2.getObject(t, j3));
                    if (zzh5 > 0) {
                        i11 += zzii.zze(i14) + zzii.zzg(zzh5) + zzh5;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 42:
                    int zzj2 = zzle.zzj((List) unsafe2.getObject(t, j3));
                    if (zzj2 > 0) {
                        i11 += zzii.zze(i14) + zzii.zzg(zzj2) + zzj2;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 43:
                    int zzf4 = zzle.zzf((List) unsafe2.getObject(t, j3));
                    if (zzf4 > 0) {
                        i11 += zzii.zze(i14) + zzii.zzg(zzf4) + zzf4;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 44:
                    int zzd4 = zzle.zzd((List) unsafe2.getObject(t, j3));
                    if (zzd4 > 0) {
                        i11 += zzii.zze(i14) + zzii.zzg(zzd4) + zzd4;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 45:
                    int zzh6 = zzle.zzh((List) unsafe2.getObject(t, j3));
                    if (zzh6 > 0) {
                        i11 += zzii.zze(i14) + zzii.zzg(zzh6) + zzh6;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 46:
                    int zzi6 = zzle.zzi((List) unsafe2.getObject(t, j3));
                    if (zzi6 > 0) {
                        i11 += zzii.zze(i14) + zzii.zzg(zzi6) + zzi6;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 47:
                    int zzg2 = zzle.zzg((List) unsafe2.getObject(t, j3));
                    if (zzg2 > 0) {
                        i11 += zzii.zze(i14) + zzii.zzg(zzg2) + zzg2;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 48:
                    int zzc2 = zzle.zzc((List) unsafe2.getObject(t, j3));
                    if (zzc2 > 0) {
                        i11 += zzii.zze(i14) + zzii.zzg(zzc2) + zzc2;
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 49:
                    i11 += zzle.zzb(i14, (List) unsafe2.getObject(t, j3), zza(i10));
                    j = 0;
                    break;
                case 50:
                    i11 += this.zzs.zza(i14, unsafe2.getObject(t, j3), zzb(i10));
                    j = 0;
                    break;
                case 51:
                    if (zza((zzko<T>) t, i14, i10)) {
                        i11 += zzii.zzb(i14, (double) FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE);
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 52:
                    if (zza((zzko<T>) t, i14, i10)) {
                        i11 += zzii.zzb(i14, 0.0f);
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 53:
                    if (zza((zzko<T>) t, i14, i10)) {
                        i11 += zzii.zzd(i14, zze(t, j3));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 54:
                    if (zza((zzko<T>) t, i14, i10)) {
                        i11 += zzii.zze(i14, zze(t, j3));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 55:
                    if (zza((zzko<T>) t, i14, i10)) {
                        i11 += zzii.zzf(i14, zzd(t, j3));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case UndoView.ACTION_USERNAME_COPIED /* 56 */:
                    if (zza((zzko<T>) t, i14, i10)) {
                        i11 += zzii.zzg(i14, 0L);
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case UndoView.ACTION_HASHTAG_COPIED /* 57 */:
                    if (zza((zzko<T>) t, i14, i10)) {
                        i11 += zzii.zzi(i14, 0);
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case UndoView.ACTION_TEXT_COPIED /* 58 */:
                    if (zza((zzko<T>) t, i14, i10)) {
                        i11 += zzii.zzb(i14, true);
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 59:
                    if (zza((zzko<T>) t, i14, i10)) {
                        Object object2 = unsafe2.getObject(t, j3);
                        if (object2 instanceof zzht) {
                            i11 += zzii.zzc(i14, (zzht) object2);
                            j = 0;
                            break;
                        } else {
                            i11 += zzii.zzb(i14, (String) object2);
                            j = 0;
                            break;
                        }
                    } else {
                        j = 0;
                        break;
                    }
                case UndoView.ACTION_PHONE_COPIED /* 60 */:
                    if (zza((zzko<T>) t, i14, i10)) {
                        i11 += zzle.zza(i14, unsafe2.getObject(t, j3), zza(i10));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case UndoView.ACTION_SHARE_BACKGROUND /* 61 */:
                    if (zza((zzko<T>) t, i14, i10)) {
                        i11 += zzii.zzc(i14, (zzht) unsafe2.getObject(t, j3));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 62:
                    if (zza((zzko<T>) t, i14, i10)) {
                        i11 += zzii.zzg(i14, zzd(t, j3));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case HtmlCompat.FROM_HTML_MODE_COMPACT /* 63 */:
                    if (zza((zzko<T>) t, i14, i10)) {
                        i11 += zzii.zzk(i14, zzd(t, j3));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 64:
                    if (zza((zzko<T>) t, i14, i10)) {
                        i11 += zzii.zzj(i14, 0);
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case VoIPService.CALL_MIN_LAYER /* 65 */:
                    if (zza((zzko<T>) t, i14, i10)) {
                        i11 += zzii.zzh(i14, 0L);
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 66:
                    if (zza((zzko<T>) t, i14, i10)) {
                        i11 += zzii.zzh(i14, zzd(t, j3));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 67:
                    if (zza((zzko<T>) t, i14, i10)) {
                        i11 += zzii.zzf(i14, zze(t, j3));
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        break;
                    }
                case 68:
                    if (zza((zzko<T>) t, i14, i10)) {
                        i11 += zzii.zzc(i14, (zzkk) unsafe2.getObject(t, j3), zza(i10));
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
        int zza4 = i11 + zza((zzlu) this.zzq, (Object) t);
        if (this.zzh) {
            zziu<?> zza5 = this.zzr.zza(t);
            for (int i19 = 0; i19 < zza5.zza.zzc(); i19++) {
                Map.Entry<?, Object> zzb4 = zza5.zza.zzb(i19);
                i18 += zziu.zzc((zziw) zzb4.getKey(), zzb4.getValue());
            }
            for (Map.Entry<?, Object> entry : zza5.zza.zzd()) {
                i18 += zziu.zzc((zziw) entry.getKey(), entry.getValue());
            }
            return zza4 + i18;
        }
        return zza4;
    }

    private static <UT, UB> int zza(zzlu<UT, UB> zzluVar, T t) {
        return zzluVar.zzf(zzluVar.zzb(t));
    }

    private static List<?> zza(Object obj, long j) {
        return (List) zzma.zzf(obj, j);
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x003f  */
    /* JADX WARN: Removed duplicated region for block: B:165:0x059f  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x05e2  */
    /* JADX WARN: Removed duplicated region for block: B:333:0x0b42  */
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
                            case 18:
                                zzle.zza(this.zzc[length2], (List<Double>) zzma.zzf(t, zzd & 1048575), zzmrVar, false);
                                break;
                            case 19:
                                zzle.zzb(this.zzc[length2], (List<Float>) zzma.zzf(t, zzd & 1048575), zzmrVar, false);
                                break;
                            case 20:
                                zzle.zzc(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, false);
                                break;
                            case 21:
                                zzle.zzd(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, false);
                                break;
                            case 22:
                                zzle.zzh(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, false);
                                break;
                            case 23:
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
                            case NalUnitTypes.NAL_TYPE_RSV_VCL29 /* 29 */:
                                zzle.zzi(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, false);
                                break;
                            case 30:
                                zzle.zzm(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, false);
                                break;
                            case 31:
                                zzle.zzl(this.zzc[length2], (List) zzma.zzf(t, zzd & 1048575), zzmrVar, false);
                                break;
                            case 32:
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
                            case UndoView.ACTION_USERNAME_COPIED /* 56 */:
                                if (zza((zzko<T>) t, i2, length2)) {
                                    zzmrVar.zzd(i2, zze(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case UndoView.ACTION_HASHTAG_COPIED /* 57 */:
                                if (zza((zzko<T>) t, i2, length2)) {
                                    zzmrVar.zzd(i2, zzd(t, zzd & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case UndoView.ACTION_TEXT_COPIED /* 58 */:
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
                            case UndoView.ACTION_PHONE_COPIED /* 60 */:
                                if (zza((zzko<T>) t, i2, length2)) {
                                    zzmrVar.zza(i2, zzma.zzf(t, zzd & 1048575), zza(length2));
                                    break;
                                } else {
                                    break;
                                }
                            case UndoView.ACTION_SHARE_BACKGROUND /* 61 */:
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
                            case HtmlCompat.FROM_HTML_MODE_COMPACT /* 63 */:
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
                            case 18:
                                zzle.zza(this.zzc[i], (List<Double>) zzma.zzf(t, zzd2 & 1048575), zzmrVar, false);
                                break;
                            case 19:
                                zzle.zzb(this.zzc[i], (List<Float>) zzma.zzf(t, zzd2 & 1048575), zzmrVar, false);
                                break;
                            case 20:
                                zzle.zzc(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, false);
                                break;
                            case 21:
                                zzle.zzd(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, false);
                                break;
                            case 22:
                                zzle.zzh(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, false);
                                break;
                            case 23:
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
                            case NalUnitTypes.NAL_TYPE_RSV_VCL29 /* 29 */:
                                zzle.zzi(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, false);
                                break;
                            case 30:
                                zzle.zzm(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, false);
                                break;
                            case 31:
                                zzle.zzl(this.zzc[i], (List) zzma.zzf(t, zzd2 & 1048575), zzmrVar, false);
                                break;
                            case 32:
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
                            case UndoView.ACTION_USERNAME_COPIED /* 56 */:
                                if (zza((zzko<T>) t, i3, i)) {
                                    zzmrVar.zzd(i3, zze(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case UndoView.ACTION_HASHTAG_COPIED /* 57 */:
                                if (zza((zzko<T>) t, i3, i)) {
                                    zzmrVar.zzd(i3, zzd(t, zzd2 & 1048575));
                                    break;
                                } else {
                                    break;
                                }
                            case UndoView.ACTION_TEXT_COPIED /* 58 */:
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
                            case UndoView.ACTION_PHONE_COPIED /* 60 */:
                                if (zza((zzko<T>) t, i3, i)) {
                                    zzmrVar.zza(i3, zzma.zzf(t, zzd2 & 1048575), zza(i));
                                    break;
                                } else {
                                    break;
                                }
                            case UndoView.ACTION_SHARE_BACKGROUND /* 61 */:
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
                            case HtmlCompat.FROM_HTML_MODE_COMPACT /* 63 */:
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

    /* JADX WARN: Removed duplicated region for block: B:10:0x0035  */
    /* JADX WARN: Removed duplicated region for block: B:188:0x0555  */
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
                    if (i6 > 17) {
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
                        this.zzr.zza(zzmrVar, entry);
                        entry = it.hasNext() ? it.next() : null;
                    }
                    long j = zzd & 1048575;
                    switch (i6) {
                        case 0:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zza(i5, zzma.zze(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 1:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zza(i5, zzma.zzd(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 2:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zza(i5, unsafe.getLong(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 3:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zzc(i5, unsafe.getLong(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 4:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zzc(i5, unsafe.getInt(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 5:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zzd(i5, unsafe.getLong(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 6:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zzd(i5, unsafe.getInt(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 7:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zza(i5, zzma.zzc(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 8:
                            if ((i2 & i4) != 0) {
                                zza(i5, unsafe.getObject(t, j), zzmrVar);
                                break;
                            } else {
                                break;
                            }
                        case 9:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zza(i5, unsafe.getObject(t, j), zza(i));
                                break;
                            } else {
                                break;
                            }
                        case 10:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zza(i5, (zzht) unsafe.getObject(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 11:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zze(i5, unsafe.getInt(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 12:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zzb(i5, unsafe.getInt(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 13:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zza(i5, unsafe.getInt(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 14:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zzb(i5, unsafe.getLong(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 15:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zzf(i5, unsafe.getInt(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 16:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zze(i5, unsafe.getLong(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 17:
                            if ((i2 & i4) != 0) {
                                zzmrVar.zzb(i5, unsafe.getObject(t, j), zza(i));
                                break;
                            } else {
                                break;
                            }
                        case 18:
                            zzle.zza(this.zzc[i], (List<Double>) unsafe.getObject(t, j), zzmrVar, false);
                            break;
                        case 19:
                            zzle.zzb(this.zzc[i], (List<Float>) unsafe.getObject(t, j), zzmrVar, false);
                            break;
                        case 20:
                            zzle.zzc(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, false);
                            break;
                        case 21:
                            zzle.zzd(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, false);
                            break;
                        case 22:
                            zzle.zzh(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, false);
                            break;
                        case 23:
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
                        case NalUnitTypes.NAL_TYPE_RSV_VCL29 /* 29 */:
                            zzle.zzi(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, false);
                            break;
                        case 30:
                            zzle.zzm(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, false);
                            break;
                        case 31:
                            zzle.zzl(this.zzc[i], (List) unsafe.getObject(t, j), zzmrVar, false);
                            break;
                        case 32:
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
                            } else {
                                break;
                            }
                        case 52:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zza(i5, zzc(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 53:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zza(i5, zze(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 54:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zzc(i5, zze(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 55:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zzc(i5, zzd(t, j));
                                break;
                            } else {
                                break;
                            }
                        case UndoView.ACTION_USERNAME_COPIED /* 56 */:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zzd(i5, zze(t, j));
                                break;
                            } else {
                                break;
                            }
                        case UndoView.ACTION_HASHTAG_COPIED /* 57 */:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zzd(i5, zzd(t, j));
                                break;
                            } else {
                                break;
                            }
                        case UndoView.ACTION_TEXT_COPIED /* 58 */:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zza(i5, zzf(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 59:
                            if (zza((zzko<T>) t, i5, i)) {
                                zza(i5, unsafe.getObject(t, j), zzmrVar);
                                break;
                            } else {
                                break;
                            }
                        case UndoView.ACTION_PHONE_COPIED /* 60 */:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zza(i5, unsafe.getObject(t, j), zza(i));
                                break;
                            } else {
                                break;
                            }
                        case UndoView.ACTION_SHARE_BACKGROUND /* 61 */:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zza(i5, (zzht) unsafe.getObject(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 62:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zze(i5, zzd(t, j));
                                break;
                            } else {
                                break;
                            }
                        case HtmlCompat.FROM_HTML_MODE_COMPACT /* 63 */:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zzb(i5, zzd(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 64:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zza(i5, zzd(t, j));
                                break;
                            } else {
                                break;
                            }
                        case VoIPService.CALL_MIN_LAYER /* 65 */:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zzb(i5, zze(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 66:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zzf(i5, zzd(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 67:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zze(i5, zze(t, j));
                                break;
                            } else {
                                break;
                            }
                        case 68:
                            if (zza((zzko<T>) t, i5, i)) {
                                zzmrVar.zzb(i5, unsafe.getObject(t, j), zza(i));
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

    @Override // com.google.android.gms.internal.vision.zzlc
    public final void zza(T t, zzld zzldVar, zzio zzioVar) throws IOException {
        Object obj;
        zziu<?> zziuVar;
        if (zzioVar == null) {
            throw new NullPointerException();
        }
        zzlu zzluVar = this.zzq;
        zziq<?> zziqVar = this.zzr;
        zziu<?> zziuVar2 = null;
        Object obj2 = null;
        while (true) {
            try {
                int zza2 = zzldVar.zza();
                int zzg = zzg(zza2);
                if (zzg < 0) {
                    if (zza2 == Integer.MAX_VALUE) {
                        for (int i = this.zzm; i < this.zzn; i++) {
                            obj2 = zza((Object) t, this.zzl[i], (int) obj2, (zzlu<UT, int>) zzluVar);
                        }
                        if (obj2 != null) {
                            zzluVar.zzb((Object) t, (T) obj2);
                            return;
                        }
                        return;
                    }
                    if (!this.zzh) {
                        obj = null;
                    } else {
                        obj = zziqVar.zza(zzioVar, this.zzg, zza2);
                    }
                    if (obj != null) {
                        if (zziuVar2 != null) {
                            zziuVar = zziuVar2;
                        } else {
                            zziuVar = zziqVar.zzb(t);
                        }
                        obj2 = zziqVar.zza(zzldVar, obj, zzioVar, zziuVar, obj2, zzluVar);
                        zziuVar2 = zziuVar;
                    } else {
                        zzluVar.zza(zzldVar);
                        if (obj2 == null) {
                            obj2 = zzluVar.zzc(t);
                        }
                        if (!zzluVar.zza((zzlu) obj2, zzldVar)) {
                            for (int i2 = this.zzm; i2 < this.zzn; i2++) {
                                obj2 = zza((Object) t, this.zzl[i2], (int) obj2, (zzlu<UT, int>) zzluVar);
                            }
                            if (obj2 != null) {
                                zzluVar.zzb((Object) t, (T) obj2);
                                return;
                            }
                            return;
                        }
                    }
                } else {
                    int zzd = zzd(zzg);
                    switch ((267386880 & zzd) >>> 20) {
                        case 0:
                            zzma.zza(t, zzd & 1048575, zzldVar.zzd());
                            zzb((zzko<T>) t, zzg);
                            break;
                        case 1:
                            zzma.zza((Object) t, zzd & 1048575, zzldVar.zze());
                            zzb((zzko<T>) t, zzg);
                            break;
                        case 2:
                            zzma.zza((Object) t, zzd & 1048575, zzldVar.zzg());
                            zzb((zzko<T>) t, zzg);
                            break;
                        case 3:
                            zzma.zza((Object) t, zzd & 1048575, zzldVar.zzf());
                            zzb((zzko<T>) t, zzg);
                            break;
                        case 4:
                            zzma.zza((Object) t, zzd & 1048575, zzldVar.zzh());
                            zzb((zzko<T>) t, zzg);
                            break;
                        case 5:
                            zzma.zza((Object) t, zzd & 1048575, zzldVar.zzi());
                            zzb((zzko<T>) t, zzg);
                            break;
                        case 6:
                            zzma.zza((Object) t, zzd & 1048575, zzldVar.zzj());
                            zzb((zzko<T>) t, zzg);
                            break;
                        case 7:
                            zzma.zza(t, zzd & 1048575, zzldVar.zzk());
                            zzb((zzko<T>) t, zzg);
                            break;
                        case 8:
                            zza(t, zzd, zzldVar);
                            zzb((zzko<T>) t, zzg);
                            break;
                        case 9:
                            if (zza((zzko<T>) t, zzg)) {
                                long j = zzd & 1048575;
                                zzma.zza(t, j, zzjf.zza(zzma.zzf(t, j), zzldVar.zza(zza(zzg), zzioVar)));
                                break;
                            } else {
                                zzma.zza(t, zzd & 1048575, zzldVar.zza(zza(zzg), zzioVar));
                                zzb((zzko<T>) t, zzg);
                                break;
                            }
                        case 10:
                            zzma.zza(t, zzd & 1048575, zzldVar.zzn());
                            zzb((zzko<T>) t, zzg);
                            break;
                        case 11:
                            zzma.zza((Object) t, zzd & 1048575, zzldVar.zzo());
                            zzb((zzko<T>) t, zzg);
                            break;
                        case 12:
                            int zzp = zzldVar.zzp();
                            zzjg zzc = zzc(zzg);
                            if (zzc != null && !zzc.zza(zzp)) {
                                obj2 = zzle.zza(zza2, zzp, obj2, zzluVar);
                                break;
                            }
                            zzma.zza((Object) t, zzd & 1048575, zzp);
                            zzb((zzko<T>) t, zzg);
                            break;
                        case 13:
                            zzma.zza((Object) t, zzd & 1048575, zzldVar.zzq());
                            zzb((zzko<T>) t, zzg);
                            break;
                        case 14:
                            zzma.zza((Object) t, zzd & 1048575, zzldVar.zzr());
                            zzb((zzko<T>) t, zzg);
                            break;
                        case 15:
                            zzma.zza((Object) t, zzd & 1048575, zzldVar.zzs());
                            zzb((zzko<T>) t, zzg);
                            break;
                        case 16:
                            zzma.zza((Object) t, zzd & 1048575, zzldVar.zzt());
                            zzb((zzko<T>) t, zzg);
                            break;
                        case 17:
                            if (zza((zzko<T>) t, zzg)) {
                                long j2 = zzd & 1048575;
                                zzma.zza(t, j2, zzjf.zza(zzma.zzf(t, j2), zzldVar.zzb(zza(zzg), zzioVar)));
                                break;
                            } else {
                                zzma.zza(t, zzd & 1048575, zzldVar.zzb(zza(zzg), zzioVar));
                                zzb((zzko<T>) t, zzg);
                                break;
                            }
                        case 18:
                            zzldVar.zza(this.zzp.zza(t, zzd & 1048575));
                            break;
                        case 19:
                            zzldVar.zzb(this.zzp.zza(t, zzd & 1048575));
                            break;
                        case 20:
                            zzldVar.zzd(this.zzp.zza(t, zzd & 1048575));
                            break;
                        case 21:
                            zzldVar.zzc(this.zzp.zza(t, zzd & 1048575));
                            break;
                        case 22:
                            zzldVar.zze(this.zzp.zza(t, zzd & 1048575));
                            break;
                        case 23:
                            zzldVar.zzf(this.zzp.zza(t, zzd & 1048575));
                            break;
                        case 24:
                            zzldVar.zzg(this.zzp.zza(t, zzd & 1048575));
                            break;
                        case 25:
                            zzldVar.zzh(this.zzp.zza(t, zzd & 1048575));
                            break;
                        case 26:
                            if (zzf(zzd)) {
                                zzldVar.zzj(this.zzp.zza(t, zzd & 1048575));
                                break;
                            } else {
                                zzldVar.zzi(this.zzp.zza(t, zzd & 1048575));
                                break;
                            }
                        case 27:
                            zzldVar.zza(this.zzp.zza(t, zzd & 1048575), zza(zzg), zzioVar);
                            break;
                        case 28:
                            zzldVar.zzk(this.zzp.zza(t, zzd & 1048575));
                            break;
                        case NalUnitTypes.NAL_TYPE_RSV_VCL29 /* 29 */:
                            zzldVar.zzl(this.zzp.zza(t, zzd & 1048575));
                            break;
                        case 30:
                            List<Integer> zza3 = this.zzp.zza(t, zzd & 1048575);
                            zzldVar.zzm(zza3);
                            obj2 = zzle.zza(zza2, zza3, zzc(zzg), obj2, zzluVar);
                            break;
                        case 31:
                            zzldVar.zzn(this.zzp.zza(t, zzd & 1048575));
                            break;
                        case 32:
                            zzldVar.zzo(this.zzp.zza(t, zzd & 1048575));
                            break;
                        case 33:
                            zzldVar.zzp(this.zzp.zza(t, zzd & 1048575));
                            break;
                        case 34:
                            zzldVar.zzq(this.zzp.zza(t, zzd & 1048575));
                            break;
                        case 35:
                            zzldVar.zza(this.zzp.zza(t, zzd & 1048575));
                            break;
                        case 36:
                            zzldVar.zzb(this.zzp.zza(t, zzd & 1048575));
                            break;
                        case 37:
                            zzldVar.zzd(this.zzp.zza(t, zzd & 1048575));
                            break;
                        case 38:
                            zzldVar.zzc(this.zzp.zza(t, zzd & 1048575));
                            break;
                        case 39:
                            zzldVar.zze(this.zzp.zza(t, zzd & 1048575));
                            break;
                        case 40:
                            zzldVar.zzf(this.zzp.zza(t, zzd & 1048575));
                            break;
                        case 41:
                            zzldVar.zzg(this.zzp.zza(t, zzd & 1048575));
                            break;
                        case 42:
                            zzldVar.zzh(this.zzp.zza(t, zzd & 1048575));
                            break;
                        case 43:
                            zzldVar.zzl(this.zzp.zza(t, zzd & 1048575));
                            break;
                        case 44:
                            List<Integer> zza4 = this.zzp.zza(t, zzd & 1048575);
                            zzldVar.zzm(zza4);
                            obj2 = zzle.zza(zza2, zza4, zzc(zzg), obj2, zzluVar);
                            break;
                        case 45:
                            zzldVar.zzn(this.zzp.zza(t, zzd & 1048575));
                            break;
                        case 46:
                            zzldVar.zzo(this.zzp.zza(t, zzd & 1048575));
                            break;
                        case 47:
                            zzldVar.zzp(this.zzp.zza(t, zzd & 1048575));
                            break;
                        case 48:
                            zzldVar.zzq(this.zzp.zza(t, zzd & 1048575));
                            break;
                        case 49:
                            zzldVar.zzb(this.zzp.zza(t, zzd & 1048575), zza(zzg), zzioVar);
                            break;
                        case 50:
                            Object zzb2 = zzb(zzg);
                            long zzd2 = zzd(zzg) & 1048575;
                            Object zzf = zzma.zzf(t, zzd2);
                            if (zzf == null) {
                                zzf = this.zzs.zzf(zzb2);
                                zzma.zza(t, zzd2, zzf);
                            } else if (this.zzs.zzd(zzf)) {
                                Object zzf2 = this.zzs.zzf(zzb2);
                                this.zzs.zza(zzf2, zzf);
                                zzma.zza(t, zzd2, zzf2);
                                zzf = zzf2;
                            }
                            zzldVar.zza(this.zzs.zza(zzf), this.zzs.zzb(zzb2), zzioVar);
                            break;
                        case 51:
                            zzma.zza(t, zzd & 1048575, Double.valueOf(zzldVar.zzd()));
                            zzb((zzko<T>) t, zza2, zzg);
                            break;
                        case 52:
                            zzma.zza(t, zzd & 1048575, Float.valueOf(zzldVar.zze()));
                            zzb((zzko<T>) t, zza2, zzg);
                            break;
                        case 53:
                            zzma.zza(t, zzd & 1048575, Long.valueOf(zzldVar.zzg()));
                            zzb((zzko<T>) t, zza2, zzg);
                            break;
                        case 54:
                            zzma.zza(t, zzd & 1048575, Long.valueOf(zzldVar.zzf()));
                            zzb((zzko<T>) t, zza2, zzg);
                            break;
                        case 55:
                            zzma.zza(t, zzd & 1048575, Integer.valueOf(zzldVar.zzh()));
                            zzb((zzko<T>) t, zza2, zzg);
                            break;
                        case UndoView.ACTION_USERNAME_COPIED /* 56 */:
                            zzma.zza(t, zzd & 1048575, Long.valueOf(zzldVar.zzi()));
                            zzb((zzko<T>) t, zza2, zzg);
                            break;
                        case UndoView.ACTION_HASHTAG_COPIED /* 57 */:
                            zzma.zza(t, zzd & 1048575, Integer.valueOf(zzldVar.zzj()));
                            zzb((zzko<T>) t, zza2, zzg);
                            break;
                        case UndoView.ACTION_TEXT_COPIED /* 58 */:
                            zzma.zza(t, zzd & 1048575, Boolean.valueOf(zzldVar.zzk()));
                            zzb((zzko<T>) t, zza2, zzg);
                            break;
                        case 59:
                            zza(t, zzd, zzldVar);
                            zzb((zzko<T>) t, zza2, zzg);
                            break;
                        case UndoView.ACTION_PHONE_COPIED /* 60 */:
                            if (zza((zzko<T>) t, zza2, zzg)) {
                                long j3 = zzd & 1048575;
                                zzma.zza(t, j3, zzjf.zza(zzma.zzf(t, j3), zzldVar.zza(zza(zzg), zzioVar)));
                            } else {
                                zzma.zza(t, zzd & 1048575, zzldVar.zza(zza(zzg), zzioVar));
                                zzb((zzko<T>) t, zzg);
                            }
                            zzb((zzko<T>) t, zza2, zzg);
                            break;
                        case UndoView.ACTION_SHARE_BACKGROUND /* 61 */:
                            zzma.zza(t, zzd & 1048575, zzldVar.zzn());
                            zzb((zzko<T>) t, zza2, zzg);
                            break;
                        case 62:
                            zzma.zza(t, zzd & 1048575, Integer.valueOf(zzldVar.zzo()));
                            zzb((zzko<T>) t, zza2, zzg);
                            break;
                        case HtmlCompat.FROM_HTML_MODE_COMPACT /* 63 */:
                            int zzp2 = zzldVar.zzp();
                            zzjg zzc2 = zzc(zzg);
                            if (zzc2 != null && !zzc2.zza(zzp2)) {
                                obj2 = zzle.zza(zza2, zzp2, obj2, zzluVar);
                                break;
                            }
                            zzma.zza(t, zzd & 1048575, Integer.valueOf(zzp2));
                            zzb((zzko<T>) t, zza2, zzg);
                            break;
                        case 64:
                            zzma.zza(t, zzd & 1048575, Integer.valueOf(zzldVar.zzq()));
                            zzb((zzko<T>) t, zza2, zzg);
                            break;
                        case VoIPService.CALL_MIN_LAYER /* 65 */:
                            zzma.zza(t, zzd & 1048575, Long.valueOf(zzldVar.zzr()));
                            zzb((zzko<T>) t, zza2, zzg);
                            break;
                        case 66:
                            zzma.zza(t, zzd & 1048575, Integer.valueOf(zzldVar.zzs()));
                            zzb((zzko<T>) t, zza2, zzg);
                            break;
                        case 67:
                            zzma.zza(t, zzd & 1048575, Long.valueOf(zzldVar.zzt()));
                            zzb((zzko<T>) t, zza2, zzg);
                            break;
                        case 68:
                            zzma.zza(t, zzd & 1048575, zzldVar.zzb(zza(zzg), zzioVar));
                            zzb((zzko<T>) t, zza2, zzg);
                            break;
                        default:
                            if (obj2 == null) {
                                try {
                                    obj2 = zzluVar.zza();
                                } catch (zzjn e) {
                                    zzluVar.zza(zzldVar);
                                    if (obj2 == null) {
                                        obj2 = zzluVar.zzc(t);
                                    }
                                    if (!zzluVar.zza((zzlu) obj2, zzldVar)) {
                                        for (int i3 = this.zzm; i3 < this.zzn; i3++) {
                                            obj2 = zza((Object) t, this.zzl[i3], (int) obj2, (zzlu<UT, int>) zzluVar);
                                        }
                                        if (obj2 != null) {
                                            zzluVar.zzb((Object) t, (T) obj2);
                                            return;
                                        }
                                        return;
                                    }
                                    break;
                                }
                            }
                            if (!zzluVar.zza((zzlu) obj2, zzldVar)) {
                                for (int i4 = this.zzm; i4 < this.zzn; i4++) {
                                    obj2 = zza((Object) t, this.zzl[i4], (int) obj2, (zzlu<UT, int>) zzluVar);
                                }
                                if (obj2 != null) {
                                    zzluVar.zzb((Object) t, (T) obj2);
                                    return;
                                }
                                return;
                            }
                            break;
                    }
                }
            } catch (Throwable th) {
                for (int i5 = this.zzm; i5 < this.zzn; i5++) {
                    obj2 = zza((Object) t, this.zzl[i5], (int) obj2, (zzlu<UT, int>) zzluVar);
                }
                if (obj2 != null) {
                    zzluVar.zzb((Object) t, (T) obj2);
                }
                throw th;
            }
        }
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

    private static int zza(byte[] bArr, int i, int i2, zzml zzmlVar, Class<?> cls, zzhn zzhnVar) throws IOException {
        switch (zzkr.zza[zzmlVar.ordinal()]) {
            case 1:
                int zzb2 = zzhl.zzb(bArr, i, zzhnVar);
                zzhnVar.zzc = Boolean.valueOf(zzhnVar.zzb != 0);
                return zzb2;
            case 2:
                return zzhl.zze(bArr, i, zzhnVar);
            case 3:
                zzhnVar.zzc = Double.valueOf(zzhl.zzc(bArr, i));
                return i + 8;
            case 4:
            case 5:
                zzhnVar.zzc = Integer.valueOf(zzhl.zza(bArr, i));
                return i + 4;
            case 6:
            case 7:
                zzhnVar.zzc = Long.valueOf(zzhl.zzb(bArr, i));
                return i + 8;
            case 8:
                zzhnVar.zzc = Float.valueOf(zzhl.zzd(bArr, i));
                return i + 4;
            case 9:
            case 10:
            case 11:
                int zza2 = zzhl.zza(bArr, i, zzhnVar);
                zzhnVar.zzc = Integer.valueOf(zzhnVar.zza);
                return zza2;
            case 12:
            case 13:
                int zzb3 = zzhl.zzb(bArr, i, zzhnVar);
                zzhnVar.zzc = Long.valueOf(zzhnVar.zzb);
                return zzb3;
            case 14:
                return zzhl.zza(zzky.zza().zza((Class) cls), bArr, i, i2, zzhnVar);
            case 15:
                int zza3 = zzhl.zza(bArr, i, zzhnVar);
                zzhnVar.zzc = Integer.valueOf(zzif.zze(zzhnVar.zza));
                return zza3;
            case 16:
                int zzb4 = zzhl.zzb(bArr, i, zzhnVar);
                zzhnVar.zzc = Long.valueOf(zzif.zza(zzhnVar.zzb));
                return zzb4;
            case 17:
                return zzhl.zzd(bArr, i, zzhnVar);
            default:
                throw new RuntimeException("unsupported field type.");
        }
    }

    private final int zza(T t, byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6, long j, int i7, long j2, zzhn zzhnVar) throws IOException {
        int i8;
        Unsafe unsafe = zzb;
        zzjl zzjlVar = (zzjl) unsafe.getObject(t, j2);
        if (!zzjlVar.zza()) {
            int size = zzjlVar.size();
            zzjlVar = zzjlVar.zza(size == 0 ? 10 : size << 1);
            unsafe.putObject(t, j2, zzjlVar);
        }
        switch (i7) {
            case 18:
            case 35:
                if (i5 == 2) {
                    zzin zzinVar = (zzin) zzjlVar;
                    int zza2 = zzhl.zza(bArr, i, zzhnVar);
                    int i9 = zzhnVar.zza + zza2;
                    while (zza2 < i9) {
                        zzinVar.zza(zzhl.zzc(bArr, zza2));
                        zza2 += 8;
                    }
                    if (zza2 != i9) {
                        throw zzjk.zza();
                    }
                    return zza2;
                } else if (i5 == 1) {
                    zzin zzinVar2 = (zzin) zzjlVar;
                    zzinVar2.zza(zzhl.zzc(bArr, i));
                    int i10 = i + 8;
                    while (i10 < i2) {
                        int zza3 = zzhl.zza(bArr, i10, zzhnVar);
                        if (i3 != zzhnVar.zza) {
                            return i10;
                        }
                        zzinVar2.zza(zzhl.zzc(bArr, zza3));
                        i10 = zza3 + 8;
                    }
                    return i10;
                }
                break;
            case 19:
            case 36:
                if (i5 == 2) {
                    zzja zzjaVar = (zzja) zzjlVar;
                    int zza4 = zzhl.zza(bArr, i, zzhnVar);
                    int i11 = zzhnVar.zza + zza4;
                    while (zza4 < i11) {
                        zzjaVar.zza(zzhl.zzd(bArr, zza4));
                        zza4 += 4;
                    }
                    if (zza4 != i11) {
                        throw zzjk.zza();
                    }
                    return zza4;
                } else if (i5 == 5) {
                    zzja zzjaVar2 = (zzja) zzjlVar;
                    zzjaVar2.zza(zzhl.zzd(bArr, i));
                    int i12 = i + 4;
                    while (i12 < i2) {
                        int zza5 = zzhl.zza(bArr, i12, zzhnVar);
                        if (i3 != zzhnVar.zza) {
                            return i12;
                        }
                        zzjaVar2.zza(zzhl.zzd(bArr, zza5));
                        i12 = zza5 + 4;
                    }
                    return i12;
                }
                break;
            case 20:
            case 21:
            case 37:
            case 38:
                if (i5 == 2) {
                    zzjy zzjyVar = (zzjy) zzjlVar;
                    int zza6 = zzhl.zza(bArr, i, zzhnVar);
                    int i13 = zzhnVar.zza + zza6;
                    while (zza6 < i13) {
                        zza6 = zzhl.zzb(bArr, zza6, zzhnVar);
                        zzjyVar.zza(zzhnVar.zzb);
                    }
                    if (zza6 != i13) {
                        throw zzjk.zza();
                    }
                    return zza6;
                } else if (i5 == 0) {
                    zzjy zzjyVar2 = (zzjy) zzjlVar;
                    int zzb2 = zzhl.zzb(bArr, i, zzhnVar);
                    zzjyVar2.zza(zzhnVar.zzb);
                    while (zzb2 < i2) {
                        int zza7 = zzhl.zza(bArr, zzb2, zzhnVar);
                        if (i3 != zzhnVar.zza) {
                            return zzb2;
                        }
                        zzb2 = zzhl.zzb(bArr, zza7, zzhnVar);
                        zzjyVar2.zza(zzhnVar.zzb);
                    }
                    return zzb2;
                }
                break;
            case 22:
            case NalUnitTypes.NAL_TYPE_RSV_VCL29 /* 29 */:
            case 39:
            case 43:
                if (i5 == 2) {
                    return zzhl.zza(bArr, i, zzjlVar, zzhnVar);
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
                    zzjy zzjyVar3 = (zzjy) zzjlVar;
                    int zza8 = zzhl.zza(bArr, i, zzhnVar);
                    int i14 = zzhnVar.zza + zza8;
                    while (zza8 < i14) {
                        zzjyVar3.zza(zzhl.zzb(bArr, zza8));
                        zza8 += 8;
                    }
                    if (zza8 != i14) {
                        throw zzjk.zza();
                    }
                    return zza8;
                } else if (i5 == 1) {
                    zzjy zzjyVar4 = (zzjy) zzjlVar;
                    zzjyVar4.zza(zzhl.zzb(bArr, i));
                    int i15 = i + 8;
                    while (i15 < i2) {
                        int zza9 = zzhl.zza(bArr, i15, zzhnVar);
                        if (i3 != zzhnVar.zza) {
                            return i15;
                        }
                        zzjyVar4.zza(zzhl.zzb(bArr, zza9));
                        i15 = zza9 + 8;
                    }
                    return i15;
                }
                break;
            case 24:
            case 31:
            case 41:
            case 45:
                if (i5 == 2) {
                    zzjd zzjdVar = (zzjd) zzjlVar;
                    int zza10 = zzhl.zza(bArr, i, zzhnVar);
                    int i16 = zzhnVar.zza + zza10;
                    while (zza10 < i16) {
                        zzjdVar.zzc(zzhl.zza(bArr, zza10));
                        zza10 += 4;
                    }
                    if (zza10 != i16) {
                        throw zzjk.zza();
                    }
                    return zza10;
                } else if (i5 == 5) {
                    zzjd zzjdVar2 = (zzjd) zzjlVar;
                    zzjdVar2.zzc(zzhl.zza(bArr, i));
                    int i17 = i + 4;
                    while (i17 < i2) {
                        int zza11 = zzhl.zza(bArr, i17, zzhnVar);
                        if (i3 != zzhnVar.zza) {
                            return i17;
                        }
                        zzjdVar2.zzc(zzhl.zza(bArr, zza11));
                        i17 = zza11 + 4;
                    }
                    return i17;
                }
                break;
            case 25:
            case 42:
                if (i5 == 2) {
                    zzhr zzhrVar = (zzhr) zzjlVar;
                    int zza12 = zzhl.zza(bArr, i, zzhnVar);
                    int i18 = zzhnVar.zza + zza12;
                    while (zza12 < i18) {
                        zza12 = zzhl.zzb(bArr, zza12, zzhnVar);
                        zzhrVar.zza(zzhnVar.zzb != 0);
                    }
                    if (zza12 != i18) {
                        throw zzjk.zza();
                    }
                    return zza12;
                } else if (i5 == 0) {
                    zzhr zzhrVar2 = (zzhr) zzjlVar;
                    int zzb3 = zzhl.zzb(bArr, i, zzhnVar);
                    zzhrVar2.zza(zzhnVar.zzb != 0);
                    while (zzb3 < i2) {
                        int zza13 = zzhl.zza(bArr, zzb3, zzhnVar);
                        if (i3 == zzhnVar.zza) {
                            zzb3 = zzhl.zzb(bArr, zza13, zzhnVar);
                            zzhrVar2.zza(zzhnVar.zzb != 0);
                        } else {
                            return zzb3;
                        }
                    }
                    return zzb3;
                }
                break;
            case 26:
                if (i5 == 2) {
                    if ((j & 536870912) == 0) {
                        int zza14 = zzhl.zza(bArr, i, zzhnVar);
                        int i19 = zzhnVar.zza;
                        if (i19 < 0) {
                            throw zzjk.zzb();
                        }
                        if (i19 == 0) {
                            zzjlVar.add("");
                        } else {
                            zzjlVar.add(new String(bArr, zza14, i19, zzjf.zza));
                            zza14 += i19;
                        }
                        while (zza14 < i2) {
                            int zza15 = zzhl.zza(bArr, zza14, zzhnVar);
                            if (i3 == zzhnVar.zza) {
                                zza14 = zzhl.zza(bArr, zza15, zzhnVar);
                                int i20 = zzhnVar.zza;
                                if (i20 < 0) {
                                    throw zzjk.zzb();
                                }
                                if (i20 == 0) {
                                    zzjlVar.add("");
                                } else {
                                    zzjlVar.add(new String(bArr, zza14, i20, zzjf.zza));
                                    zza14 += i20;
                                }
                            } else {
                                return zza14;
                            }
                        }
                        return zza14;
                    }
                    int zza16 = zzhl.zza(bArr, i, zzhnVar);
                    int i21 = zzhnVar.zza;
                    if (i21 < 0) {
                        throw zzjk.zzb();
                    }
                    if (i21 == 0) {
                        zzjlVar.add("");
                    } else {
                        int i22 = zza16 + i21;
                        if (!zzmd.zza(bArr, zza16, i22)) {
                            throw zzjk.zzh();
                        }
                        zzjlVar.add(new String(bArr, zza16, i21, zzjf.zza));
                        zza16 = i22;
                    }
                    while (zza16 < i2) {
                        int zza17 = zzhl.zza(bArr, zza16, zzhnVar);
                        if (i3 == zzhnVar.zza) {
                            zza16 = zzhl.zza(bArr, zza17, zzhnVar);
                            int i23 = zzhnVar.zza;
                            if (i23 < 0) {
                                throw zzjk.zzb();
                            }
                            if (i23 == 0) {
                                zzjlVar.add("");
                            } else {
                                int i24 = zza16 + i23;
                                if (!zzmd.zza(bArr, zza16, i24)) {
                                    throw zzjk.zzh();
                                }
                                zzjlVar.add(new String(bArr, zza16, i23, zzjf.zza));
                                zza16 = i24;
                            }
                        } else {
                            return zza16;
                        }
                    }
                    return zza16;
                }
                break;
            case 27:
                if (i5 == 2) {
                    return zzhl.zza(zza(i6), i3, bArr, i, i2, zzjlVar, zzhnVar);
                }
                break;
            case 28:
                if (i5 == 2) {
                    int zza18 = zzhl.zza(bArr, i, zzhnVar);
                    int i25 = zzhnVar.zza;
                    if (i25 < 0) {
                        throw zzjk.zzb();
                    }
                    if (i25 > bArr.length - zza18) {
                        throw zzjk.zza();
                    }
                    if (i25 == 0) {
                        zzjlVar.add(zzht.zza);
                    } else {
                        zzjlVar.add(zzht.zza(bArr, zza18, i25));
                        zza18 += i25;
                    }
                    while (zza18 < i2) {
                        int zza19 = zzhl.zza(bArr, zza18, zzhnVar);
                        if (i3 != zzhnVar.zza) {
                            return zza18;
                        }
                        zza18 = zzhl.zza(bArr, zza19, zzhnVar);
                        int i26 = zzhnVar.zza;
                        if (i26 < 0) {
                            throw zzjk.zzb();
                        }
                        if (i26 > bArr.length - zza18) {
                            throw zzjk.zza();
                        }
                        if (i26 == 0) {
                            zzjlVar.add(zzht.zza);
                        } else {
                            zzjlVar.add(zzht.zza(bArr, zza18, i26));
                            zza18 += i26;
                        }
                    }
                    return zza18;
                }
                break;
            case 30:
            case 44:
                if (i5 == 2) {
                    i8 = zzhl.zza(bArr, i, zzjlVar, zzhnVar);
                } else if (i5 == 0) {
                    i8 = zzhl.zza(i3, bArr, i, i2, zzjlVar, zzhnVar);
                }
                zzjb zzjbVar = (zzjb) t;
                zzlx zzlxVar = zzjbVar.zzb;
                if (zzlxVar == zzlx.zza()) {
                    zzlxVar = null;
                }
                zzlx zzlxVar2 = (zzlx) zzle.zza(i4, zzjlVar, zzc(i6), zzlxVar, this.zzq);
                if (zzlxVar2 != null) {
                    zzjbVar.zzb = zzlxVar2;
                }
                return i8;
            case 33:
            case 47:
                if (i5 == 2) {
                    zzjd zzjdVar3 = (zzjd) zzjlVar;
                    int zza20 = zzhl.zza(bArr, i, zzhnVar);
                    int i27 = zzhnVar.zza + zza20;
                    while (zza20 < i27) {
                        zza20 = zzhl.zza(bArr, zza20, zzhnVar);
                        zzjdVar3.zzc(zzif.zze(zzhnVar.zza));
                    }
                    if (zza20 != i27) {
                        throw zzjk.zza();
                    }
                    return zza20;
                } else if (i5 == 0) {
                    zzjd zzjdVar4 = (zzjd) zzjlVar;
                    int zza21 = zzhl.zza(bArr, i, zzhnVar);
                    zzjdVar4.zzc(zzif.zze(zzhnVar.zza));
                    while (zza21 < i2) {
                        int zza22 = zzhl.zza(bArr, zza21, zzhnVar);
                        if (i3 != zzhnVar.zza) {
                            return zza21;
                        }
                        zza21 = zzhl.zza(bArr, zza22, zzhnVar);
                        zzjdVar4.zzc(zzif.zze(zzhnVar.zza));
                    }
                    return zza21;
                }
                break;
            case 34:
            case 48:
                if (i5 == 2) {
                    zzjy zzjyVar5 = (zzjy) zzjlVar;
                    int zza23 = zzhl.zza(bArr, i, zzhnVar);
                    int i28 = zzhnVar.zza + zza23;
                    while (zza23 < i28) {
                        zza23 = zzhl.zzb(bArr, zza23, zzhnVar);
                        zzjyVar5.zza(zzif.zza(zzhnVar.zzb));
                    }
                    if (zza23 != i28) {
                        throw zzjk.zza();
                    }
                    return zza23;
                } else if (i5 == 0) {
                    zzjy zzjyVar6 = (zzjy) zzjlVar;
                    int zzb4 = zzhl.zzb(bArr, i, zzhnVar);
                    zzjyVar6.zza(zzif.zza(zzhnVar.zzb));
                    while (zzb4 < i2) {
                        int zza24 = zzhl.zza(bArr, zzb4, zzhnVar);
                        if (i3 != zzhnVar.zza) {
                            return zzb4;
                        }
                        zzb4 = zzhl.zzb(bArr, zza24, zzhnVar);
                        zzjyVar6.zza(zzif.zza(zzhnVar.zzb));
                    }
                    return zzb4;
                }
                break;
            case 49:
                if (i5 == 3) {
                    zzlc zza25 = zza(i6);
                    int i29 = (i3 & (-8)) | 4;
                    int zza26 = zzhl.zza(zza25, bArr, i, i2, i29, zzhnVar);
                    zzjlVar.add(zzhnVar.zzc);
                    while (zza26 < i2) {
                        int zza27 = zzhl.zza(bArr, zza26, zzhnVar);
                        if (i3 == zzhnVar.zza) {
                            zza26 = zzhl.zza(zza25, bArr, zza27, i2, i29, zzhnVar);
                            zzjlVar.add(zzhnVar.zzc);
                        } else {
                            return zza26;
                        }
                    }
                    return zza26;
                }
                break;
        }
        return i;
    }

    private final <K, V> int zza(T t, byte[] bArr, int i, int i2, int i3, long j, zzhn zzhnVar) throws IOException {
        int i4;
        Unsafe unsafe = zzb;
        Object zzb2 = zzb(i3);
        Object object = unsafe.getObject(t, j);
        if (this.zzs.zzd(object)) {
            Object zzf = this.zzs.zzf(zzb2);
            this.zzs.zza(zzf, object);
            unsafe.putObject(t, j, zzf);
            object = zzf;
        }
        zzkf<?, ?> zzb3 = this.zzs.zzb(zzb2);
        Map<?, ?> zza2 = this.zzs.zza(object);
        int zza3 = zzhl.zza(bArr, i, zzhnVar);
        int i5 = zzhnVar.zza;
        if (i5 < 0 || i5 > i2 - zza3) {
            throw zzjk.zza();
        }
        int i6 = i5 + zza3;
        Object obj = (K) zzb3.zzb;
        Object obj2 = (V) zzb3.zzd;
        while (zza3 < i6) {
            int i7 = zza3 + 1;
            int i8 = bArr[zza3];
            if (i8 >= 0) {
                i4 = i7;
            } else {
                int zza4 = zzhl.zza(i8, bArr, i7, zzhnVar);
                i8 = zzhnVar.zza;
                i4 = zza4;
            }
            int i9 = i8 & 7;
            switch (i8 >>> 3) {
                case 1:
                    if (i9 != zzb3.zza.zzb()) {
                        break;
                    } else {
                        zza3 = zza(bArr, i4, i2, zzb3.zza, (Class<?>) null, zzhnVar);
                        obj = (K) zzhnVar.zzc;
                        continue;
                    }
                case 2:
                    if (i9 != zzb3.zzc.zzb()) {
                        break;
                    } else {
                        zza3 = zza(bArr, i4, i2, zzb3.zzc, zzb3.zzd.getClass(), zzhnVar);
                        obj2 = (V) zzhnVar.zzc;
                        continue;
                    }
            }
            zza3 = zzhl.zza(i8, bArr, i4, i2, zzhnVar);
        }
        if (zza3 != i6) {
            throw zzjk.zzg();
        }
        zza2.put(obj, obj2);
        return i6;
    }

    private final int zza(T t, byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6, int i7, long j, int i8, zzhn zzhnVar) throws IOException {
        int i9;
        Object obj;
        Object obj2;
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
            case UndoView.ACTION_USERNAME_COPIED /* 56 */:
            case VoIPService.CALL_MIN_LAYER /* 65 */:
                if (i5 == 1) {
                    unsafe.putObject(t, j, Long.valueOf(zzhl.zzb(bArr, i)));
                    i9 = i + 8;
                    unsafe.putInt(t, j2, i4);
                    return i9;
                }
                return i;
            case UndoView.ACTION_HASHTAG_COPIED /* 57 */:
            case 64:
                if (i5 == 5) {
                    unsafe.putObject(t, j, Integer.valueOf(zzhl.zza(bArr, i)));
                    i9 = i + 4;
                    unsafe.putInt(t, j2, i4);
                    return i9;
                }
                return i;
            case UndoView.ACTION_TEXT_COPIED /* 58 */:
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
            case UndoView.ACTION_PHONE_COPIED /* 60 */:
                if (i5 == 2) {
                    int zza3 = zzhl.zza(zza(i8), bArr, i, i2, zzhnVar);
                    if (unsafe.getInt(t, j2) == i4) {
                        obj = unsafe.getObject(t, j);
                    } else {
                        obj = null;
                    }
                    if (obj == null) {
                        unsafe.putObject(t, j, zzhnVar.zzc);
                    } else {
                        unsafe.putObject(t, j, zzjf.zza(obj, zzhnVar.zzc));
                    }
                    unsafe.putInt(t, j2, i4);
                    return zza3;
                }
                return i;
            case UndoView.ACTION_SHARE_BACKGROUND /* 61 */:
                if (i5 == 2) {
                    i9 = zzhl.zze(bArr, i, zzhnVar);
                    unsafe.putObject(t, j, zzhnVar.zzc);
                    unsafe.putInt(t, j2, i4);
                    return i9;
                }
                return i;
            case HtmlCompat.FROM_HTML_MODE_COMPACT /* 63 */:
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
                    if (unsafe.getInt(t, j2) == i4) {
                        obj2 = unsafe.getObject(t, j);
                    } else {
                        obj2 = null;
                    }
                    if (obj2 == null) {
                        unsafe.putObject(t, j, zzhnVar.zzc);
                    } else {
                        unsafe.putObject(t, j, zzjf.zza(obj2, zzhnVar.zzc));
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

    /* JADX WARN: Code restructure failed: missing block: B:193:0x073f, code lost:
        if (r1 == 1048575) goto L195;
     */
    /* JADX WARN: Code restructure failed: missing block: B:194:0x0741, code lost:
        r31.putInt(r13, r1, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:195:0x0747, code lost:
        r1 = r8.zzm;
     */
    /* JADX WARN: Code restructure failed: missing block: B:197:0x074c, code lost:
        if (r1 >= r8.zzn) goto L302;
     */
    /* JADX WARN: Code restructure failed: missing block: B:198:0x074e, code lost:
        r4 = (com.google.android.gms.internal.vision.zzlx) r8.zza((java.lang.Object) r13, r8.zzl[r1], (int) r4, (com.google.android.gms.internal.vision.zzlu<UT, int>) r8.zzq);
        r1 = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:199:0x075e, code lost:
        if (r4 == null) goto L201;
     */
    /* JADX WARN: Code restructure failed: missing block: B:200:0x0760, code lost:
        r8.zzq.zzb((java.lang.Object) r13, (T) r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:201:0x0765, code lost:
        if (r9 != 0) goto L205;
     */
    /* JADX WARN: Code restructure failed: missing block: B:202:0x0767, code lost:
        if (r0 != r6) goto L203;
     */
    /* JADX WARN: Code restructure failed: missing block: B:204:0x076e, code lost:
        throw com.google.android.gms.internal.vision.zzjk.zzg();
     */
    /* JADX WARN: Code restructure failed: missing block: B:205:0x076f, code lost:
        if (r0 > r6) goto L208;
     */
    /* JADX WARN: Code restructure failed: missing block: B:206:0x0771, code lost:
        if (r3 != r9) goto L208;
     */
    /* JADX WARN: Code restructure failed: missing block: B:207:0x0773, code lost:
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:209:0x0779, code lost:
        throw com.google.android.gms.internal.vision.zzjk.zzg();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final int zza(T t, byte[] bArr, int i, int i2, int i3, zzhn zzhnVar) throws IOException {
        Unsafe unsafe;
        int i4;
        int i5;
        T t2;
        zzko<T> zzkoVar;
        zzlx zzlxVar;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        T t3;
        byte[] bArr2;
        int i16;
        zzhn zzhnVar2;
        int i17;
        byte[] bArr3;
        T t4;
        int i18;
        Object obj;
        int i19;
        int i20;
        int i21;
        int i22;
        int i23;
        int i24;
        int i25;
        zzko<T> zzkoVar2 = this;
        T t5 = t;
        byte[] bArr4 = bArr;
        int i26 = i2;
        int i27 = i3;
        zzhn zzhnVar3 = zzhnVar;
        Unsafe unsafe2 = zzb;
        int i28 = i;
        int i29 = -1;
        int i30 = 0;
        int i31 = 0;
        int i32 = 0;
        int i33 = 1048575;
        while (true) {
            if (i28 < i26) {
                int i34 = i28 + 1;
                byte b = bArr4[i28];
                if (b >= 0) {
                    i7 = b;
                    i8 = i34;
                } else {
                    i8 = zzhl.zza(b, bArr4, i34, zzhnVar3);
                    i7 = zzhnVar3.zza;
                }
                int i35 = i7 >>> 3;
                int i36 = i7 & 7;
                if (i35 > i29) {
                    i9 = zzkoVar2.zza(i35, i30 / 3);
                } else {
                    i9 = zzkoVar2.zzg(i35);
                }
                if (i9 == -1) {
                    i10 = i8;
                    i11 = i7;
                    i12 = i32;
                    i13 = i35;
                    unsafe = unsafe2;
                    i14 = i27;
                    i15 = 0;
                } else {
                    int[] iArr = zzkoVar2.zzc;
                    int i37 = iArr[i9 + 1];
                    int i38 = (i37 & 267386880) >>> 20;
                    int i39 = i8;
                    long j = i37 & 1048575;
                    int i40 = i7;
                    if (i38 <= 17) {
                        int i41 = iArr[i9 + 2];
                        int i42 = 1 << (i41 >>> 20);
                        int i43 = i41 & 1048575;
                        if (i43 == i33) {
                            i19 = i33;
                            i20 = i32;
                        } else {
                            if (i33 != 1048575) {
                                unsafe2.putInt(t5, i33, i32);
                            }
                            i19 = i43;
                            i20 = unsafe2.getInt(t5, i43);
                        }
                        switch (i38) {
                            case 0:
                                i23 = i9;
                                i21 = i39;
                                i22 = i40;
                                bArr4 = bArr;
                                if (i36 == 1) {
                                    zzma.zza(t5, j, zzhl.zzc(bArr4, i21));
                                    i28 = i21 + 8;
                                    i32 = i20 | i42;
                                    i27 = i3;
                                    i30 = i23;
                                    i29 = i35;
                                    i31 = i22;
                                    i33 = i19;
                                    i26 = i2;
                                    break;
                                } else {
                                    i14 = i3;
                                    i15 = i23;
                                    i12 = i20;
                                    i13 = i35;
                                    unsafe = unsafe2;
                                    i10 = i21;
                                    i11 = i22;
                                    i33 = i19;
                                    break;
                                }
                            case 1:
                                i23 = i9;
                                i21 = i39;
                                i22 = i40;
                                bArr4 = bArr;
                                if (i36 == 5) {
                                    zzma.zza((Object) t5, j, zzhl.zzd(bArr4, i21));
                                    i28 = i21 + 4;
                                    i32 = i20 | i42;
                                    i27 = i3;
                                    i30 = i23;
                                    i29 = i35;
                                    i31 = i22;
                                    i33 = i19;
                                    i26 = i2;
                                    break;
                                } else {
                                    i14 = i3;
                                    i15 = i23;
                                    i12 = i20;
                                    i13 = i35;
                                    unsafe = unsafe2;
                                    i10 = i21;
                                    i11 = i22;
                                    i33 = i19;
                                    break;
                                }
                            case 2:
                            case 3:
                                i23 = i9;
                                i21 = i39;
                                i22 = i40;
                                bArr4 = bArr;
                                if (i36 == 0) {
                                    int zzb2 = zzhl.zzb(bArr4, i21, zzhnVar3);
                                    unsafe2.putLong(t, j, zzhnVar3.zzb);
                                    i32 = i20 | i42;
                                    i29 = i35;
                                    i28 = zzb2;
                                    i30 = i23;
                                    i31 = i22;
                                    i33 = i19;
                                    i26 = i2;
                                    i27 = i3;
                                    break;
                                } else {
                                    i14 = i3;
                                    i15 = i23;
                                    i12 = i20;
                                    i13 = i35;
                                    unsafe = unsafe2;
                                    i10 = i21;
                                    i11 = i22;
                                    i33 = i19;
                                    break;
                                }
                            case 4:
                            case 11:
                                i23 = i9;
                                i21 = i39;
                                i22 = i40;
                                bArr4 = bArr;
                                if (i36 == 0) {
                                    i28 = zzhl.zza(bArr4, i21, zzhnVar3);
                                    unsafe2.putInt(t5, j, zzhnVar3.zza);
                                    i32 = i20 | i42;
                                    i27 = i3;
                                    i30 = i23;
                                    i29 = i35;
                                    i31 = i22;
                                    i33 = i19;
                                    i26 = i2;
                                    break;
                                } else {
                                    i14 = i3;
                                    i15 = i23;
                                    i12 = i20;
                                    i13 = i35;
                                    unsafe = unsafe2;
                                    i10 = i21;
                                    i11 = i22;
                                    i33 = i19;
                                    break;
                                }
                            case 5:
                            case 14:
                                i23 = i9;
                                i21 = i39;
                                i22 = i40;
                                bArr4 = bArr;
                                if (i36 == 1) {
                                    unsafe2.putLong(t, j, zzhl.zzb(bArr4, i21));
                                    i28 = i21 + 8;
                                    i32 = i20 | i42;
                                    i27 = i3;
                                    i29 = i35;
                                    i30 = i23;
                                    i31 = i22;
                                    i33 = i19;
                                    i26 = i2;
                                    break;
                                } else {
                                    i14 = i3;
                                    i15 = i23;
                                    i12 = i20;
                                    i13 = i35;
                                    unsafe = unsafe2;
                                    i10 = i21;
                                    i11 = i22;
                                    i33 = i19;
                                    break;
                                }
                            case 6:
                            case 13:
                                i23 = i9;
                                i21 = i39;
                                i22 = i40;
                                bArr4 = bArr;
                                if (i36 == 5) {
                                    unsafe2.putInt(t5, j, zzhl.zza(bArr4, i21));
                                    i28 = i21 + 4;
                                    int i44 = i20 | i42;
                                    i27 = i3;
                                    i30 = i23;
                                    i31 = i22;
                                    i33 = i19;
                                    i26 = i2;
                                    i32 = i44;
                                    i29 = i35;
                                    break;
                                } else {
                                    i14 = i3;
                                    i15 = i23;
                                    i12 = i20;
                                    i13 = i35;
                                    unsafe = unsafe2;
                                    i10 = i21;
                                    i11 = i22;
                                    i33 = i19;
                                    break;
                                }
                            case 7:
                                i23 = i9;
                                i21 = i39;
                                i22 = i40;
                                bArr4 = bArr;
                                if (i36 == 0) {
                                    int zzb3 = zzhl.zzb(bArr4, i21, zzhnVar3);
                                    zzma.zza(t5, j, zzhnVar3.zzb != 0);
                                    int i45 = i20 | i42;
                                    i27 = i3;
                                    i30 = i23;
                                    i29 = i35;
                                    i31 = i22;
                                    i33 = i19;
                                    i26 = i2;
                                    i32 = i45;
                                    i28 = zzb3;
                                    break;
                                } else {
                                    i14 = i3;
                                    i15 = i23;
                                    i12 = i20;
                                    i13 = i35;
                                    unsafe = unsafe2;
                                    i10 = i21;
                                    i11 = i22;
                                    i33 = i19;
                                    break;
                                }
                            case 8:
                                i23 = i9;
                                i21 = i39;
                                i22 = i40;
                                bArr4 = bArr;
                                if (i36 == 2) {
                                    if ((536870912 & i37) == 0) {
                                        i28 = zzhl.zzc(bArr4, i21, zzhnVar3);
                                    } else {
                                        i28 = zzhl.zzd(bArr4, i21, zzhnVar3);
                                    }
                                    unsafe2.putObject(t5, j, zzhnVar3.zzc);
                                    int i46 = i20 | i42;
                                    i27 = i3;
                                    i30 = i23;
                                    i31 = i22;
                                    i33 = i19;
                                    i26 = i2;
                                    i32 = i46;
                                    i29 = i35;
                                    break;
                                } else {
                                    i14 = i3;
                                    i15 = i23;
                                    i12 = i20;
                                    i13 = i35;
                                    unsafe = unsafe2;
                                    i10 = i21;
                                    i11 = i22;
                                    i33 = i19;
                                    break;
                                }
                            case 9:
                                i23 = i9;
                                i21 = i39;
                                i22 = i40;
                                bArr4 = bArr;
                                if (i36 == 2) {
                                    i28 = zzhl.zza(zzkoVar2.zza(i23), bArr4, i21, i2, zzhnVar3);
                                    if ((i20 & i42) == 0) {
                                        unsafe2.putObject(t5, j, zzhnVar3.zzc);
                                    } else {
                                        unsafe2.putObject(t5, j, zzjf.zza(unsafe2.getObject(t5, j), zzhnVar3.zzc));
                                    }
                                    int i47 = i20 | i42;
                                    i27 = i3;
                                    i30 = i23;
                                    i31 = i22;
                                    i33 = i19;
                                    i26 = i2;
                                    i32 = i47;
                                    i29 = i35;
                                    break;
                                } else {
                                    i14 = i3;
                                    i15 = i23;
                                    i12 = i20;
                                    i13 = i35;
                                    unsafe = unsafe2;
                                    i10 = i21;
                                    i11 = i22;
                                    i33 = i19;
                                    break;
                                }
                            case 10:
                                i23 = i9;
                                i21 = i39;
                                i22 = i40;
                                bArr4 = bArr;
                                if (i36 == 2) {
                                    i28 = zzhl.zze(bArr4, i21, zzhnVar3);
                                    unsafe2.putObject(t5, j, zzhnVar3.zzc);
                                    i32 = i20 | i42;
                                    i27 = i3;
                                    i30 = i23;
                                    i29 = i35;
                                    i31 = i22;
                                    i33 = i19;
                                    i26 = i2;
                                    break;
                                } else {
                                    i14 = i3;
                                    i15 = i23;
                                    i12 = i20;
                                    i13 = i35;
                                    unsafe = unsafe2;
                                    i10 = i21;
                                    i11 = i22;
                                    i33 = i19;
                                    break;
                                }
                            case 12:
                                i23 = i9;
                                i21 = i39;
                                i22 = i40;
                                bArr4 = bArr;
                                if (i36 == 0) {
                                    int zza2 = zzhl.zza(bArr4, i21, zzhnVar3);
                                    int i48 = zzhnVar3.zza;
                                    zzjg zzc = zzkoVar2.zzc(i23);
                                    if (zzc == null || zzc.zza(i48)) {
                                        unsafe2.putInt(t5, j, i48);
                                        i32 = i20 | i42;
                                        i28 = zza2;
                                        i27 = i3;
                                        i30 = i23;
                                        i29 = i35;
                                        i31 = i22;
                                        i33 = i19;
                                        i26 = i2;
                                        break;
                                    } else {
                                        zze(t).zza(i22, Long.valueOf(i48));
                                        i28 = zza2;
                                        i27 = i3;
                                        i30 = i23;
                                        i32 = i20;
                                        i29 = i35;
                                        i31 = i22;
                                        i33 = i19;
                                        i26 = i2;
                                        break;
                                    }
                                } else {
                                    i14 = i3;
                                    i15 = i23;
                                    i12 = i20;
                                    i13 = i35;
                                    unsafe = unsafe2;
                                    i10 = i21;
                                    i11 = i22;
                                    i33 = i19;
                                    break;
                                }
                                break;
                            case 15:
                                i23 = i9;
                                i21 = i39;
                                i22 = i40;
                                bArr4 = bArr;
                                if (i36 == 0) {
                                    i28 = zzhl.zza(bArr4, i21, zzhnVar3);
                                    unsafe2.putInt(t5, j, zzif.zze(zzhnVar3.zza));
                                    i32 = i20 | i42;
                                    i27 = i3;
                                    i30 = i23;
                                    i29 = i35;
                                    i31 = i22;
                                    i33 = i19;
                                    i26 = i2;
                                    break;
                                } else {
                                    i14 = i3;
                                    i15 = i23;
                                    i12 = i20;
                                    i13 = i35;
                                    unsafe = unsafe2;
                                    i10 = i21;
                                    i11 = i22;
                                    i33 = i19;
                                    break;
                                }
                            case 16:
                                int i49 = i9;
                                i21 = i39;
                                if (i36 == 0) {
                                    bArr4 = bArr;
                                    int zzb4 = zzhl.zzb(bArr4, i21, zzhnVar3);
                                    unsafe2.putLong(t, j, zzif.zza(zzhnVar3.zzb));
                                    i32 = i20 | i42;
                                    i29 = i35;
                                    i28 = zzb4;
                                    i30 = i49;
                                    i31 = i40;
                                    i33 = i19;
                                    i26 = i2;
                                    i27 = i3;
                                    break;
                                } else {
                                    i22 = i40;
                                    i23 = i49;
                                    i14 = i3;
                                    i15 = i23;
                                    i12 = i20;
                                    i13 = i35;
                                    unsafe = unsafe2;
                                    i10 = i21;
                                    i11 = i22;
                                    i33 = i19;
                                    break;
                                }
                            case 17:
                                if (i36 != 3) {
                                    i21 = i39;
                                    i22 = i40;
                                    i23 = i9;
                                    i14 = i3;
                                    i15 = i23;
                                    i12 = i20;
                                    i13 = i35;
                                    unsafe = unsafe2;
                                    i10 = i21;
                                    i11 = i22;
                                    i33 = i19;
                                    break;
                                } else {
                                    int i50 = i9;
                                    i28 = zzhl.zza(zzkoVar2.zza(i9), bArr, i39, i2, (i35 << 3) | 4, zzhnVar);
                                    if ((i20 & i42) == 0) {
                                        unsafe2.putObject(t5, j, zzhnVar3.zzc);
                                    } else {
                                        unsafe2.putObject(t5, j, zzjf.zza(unsafe2.getObject(t5, j), zzhnVar3.zzc));
                                    }
                                    i32 = i20 | i42;
                                    bArr4 = bArr;
                                    i26 = i2;
                                    i29 = i35;
                                    i30 = i50;
                                    i31 = i40;
                                    i33 = i19;
                                    i27 = i3;
                                    break;
                                }
                            default:
                                i23 = i9;
                                i21 = i39;
                                i22 = i40;
                                i14 = i3;
                                i15 = i23;
                                i12 = i20;
                                i13 = i35;
                                unsafe = unsafe2;
                                i10 = i21;
                                i11 = i22;
                                i33 = i19;
                                break;
                        }
                    } else {
                        int i51 = i9;
                        bArr4 = bArr;
                        if (i38 == 27) {
                            if (i36 != 2) {
                                i15 = i51;
                                i12 = i32;
                                i24 = i33;
                                i14 = i3;
                                i13 = i35;
                                unsafe = unsafe2;
                                i11 = i40;
                                i25 = i39;
                                i10 = i25;
                                i33 = i24;
                            } else {
                                zzjl zzjlVar = (zzjl) unsafe2.getObject(t5, j);
                                if (!zzjlVar.zza()) {
                                    int size = zzjlVar.size();
                                    zzjlVar = zzjlVar.zza(size == 0 ? 10 : size << 1);
                                    unsafe2.putObject(t5, j, zzjlVar);
                                }
                                i28 = zzhl.zza(zzkoVar2.zza(i51), i40, bArr, i39, i2, zzjlVar, zzhnVar);
                                i27 = i3;
                                i29 = i35;
                                i31 = i40;
                                i30 = i51;
                                i32 = i32;
                                i33 = i33;
                                i26 = i2;
                            }
                        } else {
                            i15 = i51;
                            i12 = i32;
                            i24 = i33;
                            if (i38 <= 49) {
                                i13 = i35;
                                unsafe = unsafe2;
                                i14 = i3;
                                i11 = i40;
                                i28 = zza((zzko<T>) t, bArr, i39, i2, i40, i35, i36, i15, i37, i38, j, zzhnVar);
                                if (i28 != i39) {
                                    t5 = t;
                                    bArr4 = bArr;
                                    i29 = i13;
                                    i26 = i2;
                                    zzhnVar3 = zzhnVar;
                                    i27 = i14;
                                    i31 = i11;
                                    i30 = i15;
                                    i32 = i12;
                                    i33 = i24;
                                    unsafe2 = unsafe;
                                    zzkoVar2 = this;
                                } else {
                                    i10 = i28;
                                    i33 = i24;
                                }
                            } else {
                                i14 = i3;
                                i13 = i35;
                                unsafe = unsafe2;
                                i11 = i40;
                                i25 = i39;
                                if (i38 == 50) {
                                    if (i36 == 2) {
                                        i28 = zza((zzko<T>) t, bArr, i25, i2, i15, j, zzhnVar);
                                        if (i28 != i25) {
                                            t5 = t;
                                            bArr4 = bArr;
                                            i29 = i13;
                                            i26 = i2;
                                            zzhnVar3 = zzhnVar;
                                            i27 = i14;
                                            i31 = i11;
                                            i30 = i15;
                                            i32 = i12;
                                            i33 = i24;
                                            unsafe2 = unsafe;
                                            zzkoVar2 = this;
                                        } else {
                                            i10 = i28;
                                            i33 = i24;
                                        }
                                    } else {
                                        i10 = i25;
                                        i33 = i24;
                                    }
                                } else {
                                    i28 = zza((zzko<T>) t, bArr, i25, i2, i11, i13, i36, i37, i38, j, i15, zzhnVar);
                                    if (i28 == i25) {
                                        i10 = i28;
                                        i33 = i24;
                                    } else {
                                        i31 = i11;
                                        i29 = i13;
                                        t5 = t;
                                        bArr4 = bArr;
                                        i30 = i15;
                                        i32 = i12;
                                        i26 = i2;
                                        zzkoVar2 = this;
                                        i27 = i14;
                                        zzhnVar3 = zzhnVar;
                                        i33 = i24;
                                        unsafe2 = unsafe;
                                    }
                                }
                            }
                        }
                    }
                }
                int i52 = i11;
                if (i52 != i14 || i14 == 0) {
                    int i53 = i14;
                    if (!this.zzh) {
                        t3 = t;
                        bArr2 = bArr;
                        i16 = i13;
                        zzhnVar2 = zzhnVar;
                    } else {
                        zzhnVar2 = zzhnVar;
                        if (zzhnVar2.zzd == zzio.zzb()) {
                            t3 = t;
                            bArr2 = bArr;
                            i16 = i13;
                        } else {
                            zzkk zzkkVar = this.zzg;
                            zzlu<?, ?> zzluVar = this.zzq;
                            int i54 = i13;
                            zzjb.zze zza3 = zzhnVar2.zzd.zza(zzkkVar, i54);
                            if (zza3 == null) {
                                i28 = zzhl.zza(i52, bArr, i10, i2, zze(t), zzhnVar);
                                t4 = t;
                                bArr3 = bArr;
                                i17 = i33;
                                i18 = i2;
                            } else {
                                t4 = t;
                                zzjb.zzc zzcVar = (zzjb.zzc) t4;
                                zzcVar.zza();
                                zziu<zzjb.zzf> zziuVar = zzcVar.zzc;
                                boolean z = zza3.zzd.zzd;
                                if (zza3.zzd.zzc == zzml.ENUM) {
                                    bArr3 = bArr;
                                    i10 = zzhl.zza(bArr3, i10, zzhnVar2);
                                    zzjh zzjhVar = null;
                                    if (zzjhVar.zza(zzhnVar2.zza) == null) {
                                        zzlx zzlxVar2 = zzcVar.zzb;
                                        if (zzlxVar2 == zzlx.zza()) {
                                            zzlxVar2 = zzlx.zzb();
                                            zzcVar.zzb = zzlxVar2;
                                        }
                                        zzle.zza(i54, zzhnVar2.zza, zzlxVar2, (zzlu<UT, zzlx>) zzluVar);
                                        i17 = i33;
                                        i18 = i2;
                                        i28 = i10;
                                    } else {
                                        obj = Integer.valueOf(zzhnVar2.zza);
                                        i17 = i33;
                                        i18 = i2;
                                    }
                                } else {
                                    bArr3 = bArr;
                                    obj = null;
                                    switch (zzhk.zza[zza3.zzd.zzc.ordinal()]) {
                                        case 1:
                                            i17 = i33;
                                            i18 = i2;
                                            obj = Double.valueOf(zzhl.zzc(bArr3, i10));
                                            i10 += 8;
                                            break;
                                        case 2:
                                            i17 = i33;
                                            i18 = i2;
                                            obj = Float.valueOf(zzhl.zzd(bArr3, i10));
                                            i10 += 4;
                                            break;
                                        case 3:
                                        case 4:
                                            i17 = i33;
                                            i18 = i2;
                                            i10 = zzhl.zzb(bArr3, i10, zzhnVar2);
                                            obj = Long.valueOf(zzhnVar2.zzb);
                                            break;
                                        case 5:
                                        case 6:
                                            i17 = i33;
                                            i18 = i2;
                                            i10 = zzhl.zza(bArr3, i10, zzhnVar2);
                                            obj = Integer.valueOf(zzhnVar2.zza);
                                            break;
                                        case 7:
                                        case 8:
                                            i17 = i33;
                                            i18 = i2;
                                            obj = Long.valueOf(zzhl.zzb(bArr3, i10));
                                            i10 += 8;
                                            break;
                                        case 9:
                                        case 10:
                                            i17 = i33;
                                            i18 = i2;
                                            obj = Integer.valueOf(zzhl.zza(bArr3, i10));
                                            i10 += 4;
                                            break;
                                        case 11:
                                            i17 = i33;
                                            i18 = i2;
                                            i10 = zzhl.zzb(bArr3, i10, zzhnVar2);
                                            obj = Boolean.valueOf(zzhnVar2.zzb != 0);
                                            break;
                                        case 12:
                                            i17 = i33;
                                            i18 = i2;
                                            i10 = zzhl.zza(bArr3, i10, zzhnVar2);
                                            obj = Integer.valueOf(zzif.zze(zzhnVar2.zza));
                                            break;
                                        case 13:
                                            i17 = i33;
                                            i18 = i2;
                                            i10 = zzhl.zzb(bArr3, i10, zzhnVar2);
                                            obj = Long.valueOf(zzif.zza(zzhnVar2.zzb));
                                            break;
                                        case 14:
                                            throw new IllegalStateException("Shouldn't reach here.");
                                        case 15:
                                            i17 = i33;
                                            i18 = i2;
                                            i10 = zzhl.zze(bArr3, i10, zzhnVar2);
                                            obj = zzhnVar2.zzc;
                                            break;
                                        case 16:
                                            i17 = i33;
                                            i18 = i2;
                                            i10 = zzhl.zzc(bArr3, i10, zzhnVar2);
                                            obj = zzhnVar2.zzc;
                                            break;
                                        case 17:
                                            i17 = i33;
                                            i18 = i2;
                                            i10 = zzhl.zza(zzky.zza().zza((Class) zza3.zzc.getClass()), bArr, i10, i2, (i54 << 3) | 4, zzhnVar);
                                            obj = zzhnVar2.zzc;
                                            break;
                                        case 18:
                                            i10 = zzhl.zza(zzky.zza().zza((Class) zza3.zzc.getClass()), bArr3, i10, i2, zzhnVar2);
                                            obj = zzhnVar2.zzc;
                                            i17 = i33;
                                            i18 = i2;
                                            break;
                                        default:
                                            i17 = i33;
                                            i18 = i2;
                                            break;
                                    }
                                }
                                if (zza3.zzd.zzd) {
                                    zziuVar.zzb(zza3.zzd, obj);
                                } else {
                                    switch (zza3.zzd.zzc) {
                                        case GROUP:
                                        case MESSAGE:
                                            Object zza4 = zziuVar.zza((zziu<zzjb.zzf>) zza3.zzd);
                                            if (zza4 != null) {
                                                obj = zzjf.zza(zza4, obj);
                                                break;
                                            }
                                            break;
                                    }
                                    zziuVar.zza((zziu<zzjb.zzf>) zza3.zzd, obj);
                                }
                                i28 = i10;
                            }
                            i31 = i52;
                            i29 = i54;
                            t5 = t4;
                            bArr4 = bArr3;
                            i30 = i15;
                            i32 = i12;
                            i26 = i18;
                            zzkoVar2 = this;
                            i27 = i53;
                            zzhnVar3 = zzhnVar2;
                            unsafe2 = unsafe;
                            i33 = i17;
                        }
                    }
                    i28 = zzhl.zza(i52, bArr, i10, i2, zze(t), zzhnVar);
                    i31 = i52;
                    i29 = i16;
                    t5 = t3;
                    bArr4 = bArr2;
                    i30 = i15;
                    i32 = i12;
                    i26 = i2;
                    zzkoVar2 = this;
                    i27 = i53;
                    zzhnVar3 = zzhnVar2;
                    unsafe2 = unsafe;
                    i33 = i33;
                } else {
                    zzkoVar = this;
                    t2 = t;
                    i28 = i10;
                    i6 = i33;
                    i31 = i52;
                    i4 = i14;
                    i32 = i12;
                    zzlxVar = null;
                    i5 = i2;
                }
            } else {
                int i55 = i33;
                unsafe = unsafe2;
                i4 = i27;
                i5 = i26;
                t2 = t5;
                zzkoVar = zzkoVar2;
                zzlxVar = null;
                i6 = i55;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v12, types: [int] */
    @Override // com.google.android.gms.internal.vision.zzlc
    public final void zza(T t, byte[] bArr, int i, int i2, zzhn zzhnVar) throws IOException {
        byte b;
        int i3;
        int i4;
        int i5;
        int i6;
        Unsafe unsafe;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        zzko<T> zzkoVar = this;
        T t2 = t;
        byte[] bArr2 = bArr;
        int i14 = i2;
        zzhn zzhnVar2 = zzhnVar;
        if (zzkoVar.zzj) {
            Unsafe unsafe2 = zzb;
            int i15 = -1;
            int i16 = i;
            int i17 = -1;
            int i18 = 0;
            int i19 = 0;
            int i20 = 1048575;
            while (i16 < i14) {
                int i21 = i16 + 1;
                byte b2 = bArr2[i16];
                if (b2 >= 0) {
                    b = b2;
                    i3 = i21;
                } else {
                    i3 = zzhl.zza(b2, bArr2, i21, zzhnVar2);
                    b = zzhnVar2.zza;
                }
                int i22 = b >>> 3;
                int i23 = b & 7;
                if (i22 > i17) {
                    i4 = zzkoVar.zza(i22, i18 / 3);
                } else {
                    i4 = zzkoVar.zzg(i22);
                }
                if (i4 == i15) {
                    i5 = i3;
                    i6 = i22;
                    unsafe = unsafe2;
                    i7 = 0;
                } else {
                    int[] iArr = zzkoVar.zzc;
                    int i24 = iArr[i4 + 1];
                    int i25 = (i24 & 267386880) >>> 20;
                    int i26 = i3;
                    long j = i24 & 1048575;
                    if (i25 <= 17) {
                        int i27 = iArr[i4 + 2];
                        int i28 = 1 << (i27 >>> 20);
                        int i29 = i27 & 1048575;
                        if (i29 == i20) {
                            i8 = i4;
                        } else {
                            if (i20 == 1048575) {
                                i8 = i4;
                            } else {
                                i8 = i4;
                                unsafe2.putInt(t2, i20, i19);
                            }
                            if (i29 != 1048575) {
                                i19 = unsafe2.getInt(t2, i29);
                            }
                            i20 = i29;
                        }
                        switch (i25) {
                            case 0:
                                i9 = i26;
                                i10 = i8;
                                i6 = i22;
                                if (i23 == 1) {
                                    zzma.zza(t2, j, zzhl.zzc(bArr2, i9));
                                    i16 = i9 + 8;
                                    i19 |= i28;
                                    i18 = i10;
                                    i17 = i6;
                                    i15 = -1;
                                    i14 = i2;
                                    break;
                                } else {
                                    i5 = i9;
                                    unsafe = unsafe2;
                                    i7 = i10;
                                    break;
                                }
                            case 1:
                                i9 = i26;
                                i10 = i8;
                                i6 = i22;
                                if (i23 == 5) {
                                    zzma.zza((Object) t2, j, zzhl.zzd(bArr2, i9));
                                    i16 = i9 + 4;
                                    i19 |= i28;
                                    i18 = i10;
                                    i17 = i6;
                                    i15 = -1;
                                    i14 = i2;
                                    break;
                                } else {
                                    i5 = i9;
                                    unsafe = unsafe2;
                                    i7 = i10;
                                    break;
                                }
                            case 2:
                            case 3:
                                i9 = i26;
                                i10 = i8;
                                i6 = i22;
                                if (i23 == 0) {
                                    int zzb2 = zzhl.zzb(bArr2, i9, zzhnVar2);
                                    unsafe2.putLong(t, j, zzhnVar2.zzb);
                                    i19 |= i28;
                                    i16 = zzb2;
                                    i18 = i10;
                                    i17 = i6;
                                    i15 = -1;
                                    i14 = i2;
                                    break;
                                } else {
                                    i5 = i9;
                                    unsafe = unsafe2;
                                    i7 = i10;
                                    break;
                                }
                            case 4:
                            case 11:
                                i9 = i26;
                                i10 = i8;
                                i6 = i22;
                                if (i23 == 0) {
                                    i16 = zzhl.zza(bArr2, i9, zzhnVar2);
                                    unsafe2.putInt(t2, j, zzhnVar2.zza);
                                    i19 |= i28;
                                    i18 = i10;
                                    i17 = i6;
                                    i15 = -1;
                                    i14 = i2;
                                    break;
                                } else {
                                    i5 = i9;
                                    unsafe = unsafe2;
                                    i7 = i10;
                                    break;
                                }
                            case 5:
                            case 14:
                                i9 = i26;
                                i10 = i8;
                                i6 = i22;
                                if (i23 == 1) {
                                    unsafe2.putLong(t, j, zzhl.zzb(bArr2, i9));
                                    i16 = i9 + 8;
                                    i19 |= i28;
                                    i18 = i10;
                                    i17 = i6;
                                    i15 = -1;
                                    i14 = i2;
                                    break;
                                } else {
                                    i5 = i9;
                                    unsafe = unsafe2;
                                    i7 = i10;
                                    break;
                                }
                            case 6:
                            case 13:
                                i9 = i26;
                                i10 = i8;
                                i6 = i22;
                                if (i23 == 5) {
                                    unsafe2.putInt(t2, j, zzhl.zza(bArr2, i9));
                                    i16 = i9 + 4;
                                    i19 |= i28;
                                    i17 = i6;
                                    i15 = -1;
                                    i14 = i2;
                                    i18 = i10;
                                    break;
                                } else {
                                    i5 = i9;
                                    unsafe = unsafe2;
                                    i7 = i10;
                                    break;
                                }
                            case 7:
                                i9 = i26;
                                i10 = i8;
                                i6 = i22;
                                if (i23 == 0) {
                                    int zzb3 = zzhl.zzb(bArr2, i9, zzhnVar2);
                                    zzma.zza(t2, j, zzhnVar2.zzb != 0);
                                    i19 |= i28;
                                    i16 = zzb3;
                                    i17 = i6;
                                    i15 = -1;
                                    i14 = i2;
                                    i18 = i10;
                                    break;
                                } else {
                                    i5 = i9;
                                    unsafe = unsafe2;
                                    i7 = i10;
                                    break;
                                }
                            case 8:
                                i9 = i26;
                                i10 = i8;
                                i6 = i22;
                                if (i23 == 2) {
                                    if ((i24 & 536870912) == 0) {
                                        i16 = zzhl.zzc(bArr2, i9, zzhnVar2);
                                    } else {
                                        i16 = zzhl.zzd(bArr2, i9, zzhnVar2);
                                    }
                                    unsafe2.putObject(t2, j, zzhnVar2.zzc);
                                    i19 |= i28;
                                    i17 = i6;
                                    i15 = -1;
                                    i14 = i2;
                                    i18 = i10;
                                    break;
                                } else {
                                    i5 = i9;
                                    unsafe = unsafe2;
                                    i7 = i10;
                                    break;
                                }
                            case 9:
                                i9 = i26;
                                i10 = i8;
                                i6 = i22;
                                if (i23 == 2) {
                                    i16 = zzhl.zza(zzkoVar.zza(i10), bArr2, i9, i2, zzhnVar2);
                                    Object object = unsafe2.getObject(t2, j);
                                    if (object == null) {
                                        unsafe2.putObject(t2, j, zzhnVar2.zzc);
                                    } else {
                                        unsafe2.putObject(t2, j, zzjf.zza(object, zzhnVar2.zzc));
                                    }
                                    i19 |= i28;
                                    i17 = i6;
                                    i15 = -1;
                                    i14 = i2;
                                    i18 = i10;
                                    break;
                                } else {
                                    i5 = i9;
                                    unsafe = unsafe2;
                                    i7 = i10;
                                    break;
                                }
                            case 10:
                                i9 = i26;
                                i10 = i8;
                                i6 = i22;
                                if (i23 == 2) {
                                    i16 = zzhl.zze(bArr2, i9, zzhnVar2);
                                    unsafe2.putObject(t2, j, zzhnVar2.zzc);
                                    i19 |= i28;
                                    i18 = i10;
                                    i17 = i6;
                                    i15 = -1;
                                    i14 = i2;
                                    break;
                                } else {
                                    i5 = i9;
                                    unsafe = unsafe2;
                                    i7 = i10;
                                    break;
                                }
                            case 12:
                                i9 = i26;
                                i10 = i8;
                                i6 = i22;
                                if (i23 == 0) {
                                    i16 = zzhl.zza(bArr2, i9, zzhnVar2);
                                    unsafe2.putInt(t2, j, zzhnVar2.zza);
                                    i19 |= i28;
                                    i18 = i10;
                                    i17 = i6;
                                    i15 = -1;
                                    i14 = i2;
                                    break;
                                } else {
                                    i5 = i9;
                                    unsafe = unsafe2;
                                    i7 = i10;
                                    break;
                                }
                            case 15:
                                i9 = i26;
                                i10 = i8;
                                i6 = i22;
                                if (i23 == 0) {
                                    i16 = zzhl.zza(bArr2, i9, zzhnVar2);
                                    unsafe2.putInt(t2, j, zzif.zze(zzhnVar2.zza));
                                    i19 |= i28;
                                    i18 = i10;
                                    i17 = i6;
                                    i15 = -1;
                                    i14 = i2;
                                    break;
                                } else {
                                    i5 = i9;
                                    unsafe = unsafe2;
                                    i7 = i10;
                                    break;
                                }
                            case 16:
                                if (i23 != 0) {
                                    i9 = i26;
                                    i10 = i8;
                                    i6 = i22;
                                    i5 = i9;
                                    unsafe = unsafe2;
                                    i7 = i10;
                                    break;
                                } else {
                                    int zzb4 = zzhl.zzb(bArr2, i26, zzhnVar2);
                                    unsafe2.putLong(t, j, zzif.zza(zzhnVar2.zzb));
                                    i19 |= i28;
                                    i16 = zzb4;
                                    i18 = i8;
                                    i17 = i22;
                                    i15 = -1;
                                    i14 = i2;
                                    break;
                                }
                            default:
                                i9 = i26;
                                i10 = i8;
                                i6 = i22;
                                i5 = i9;
                                unsafe = unsafe2;
                                i7 = i10;
                                break;
                        }
                    } else {
                        i6 = i22;
                        int i30 = i4;
                        if (i25 == 27) {
                            if (i23 != 2) {
                                i11 = i20;
                                i12 = i19;
                                unsafe = unsafe2;
                                i13 = i26;
                                i7 = i30;
                                i5 = i13;
                                i20 = i11;
                                i19 = i12;
                            } else {
                                zzjl zzjlVar = (zzjl) unsafe2.getObject(t2, j);
                                if (!zzjlVar.zza()) {
                                    int size = zzjlVar.size();
                                    zzjlVar = zzjlVar.zza(size == 0 ? 10 : size << 1);
                                    unsafe2.putObject(t2, j, zzjlVar);
                                }
                                i16 = zzhl.zza(zzkoVar.zza(i30), b, bArr, i26, i2, zzjlVar, zzhnVar);
                                i19 = i19;
                                i18 = i30;
                                i17 = i6;
                                i15 = -1;
                                i14 = i2;
                            }
                        } else if (i25 <= 49) {
                            int i31 = i19;
                            int i32 = i20;
                            unsafe = unsafe2;
                            i7 = i30;
                            i16 = zza((zzko<T>) t, bArr, i26, i2, b, i6, i23, i30, i24, i25, j, zzhnVar);
                            if (i16 != i26) {
                                zzkoVar = this;
                                t2 = t;
                                bArr2 = bArr;
                                i14 = i2;
                                zzhnVar2 = zzhnVar;
                                i20 = i32;
                                i17 = i6;
                                i18 = i7;
                                i19 = i31;
                                unsafe2 = unsafe;
                                i15 = -1;
                            } else {
                                i5 = i16;
                                i20 = i32;
                                i19 = i31;
                            }
                        } else {
                            i12 = i19;
                            i11 = i20;
                            unsafe = unsafe2;
                            i13 = i26;
                            i7 = i30;
                            if (i25 == 50) {
                                if (i23 == 2) {
                                    i16 = zza((zzko<T>) t, bArr, i13, i2, i7, j, zzhnVar);
                                    if (i16 != i13) {
                                        zzkoVar = this;
                                        t2 = t;
                                        bArr2 = bArr;
                                        i14 = i2;
                                        zzhnVar2 = zzhnVar;
                                        i20 = i11;
                                        i17 = i6;
                                        i18 = i7;
                                        i19 = i12;
                                        unsafe2 = unsafe;
                                        i15 = -1;
                                    } else {
                                        i5 = i16;
                                        i20 = i11;
                                        i19 = i12;
                                    }
                                } else {
                                    i5 = i13;
                                    i20 = i11;
                                    i19 = i12;
                                }
                            } else {
                                i16 = zza((zzko<T>) t, bArr, i13, i2, b, i6, i23, i24, i25, j, i7, zzhnVar);
                                if (i16 == i13) {
                                    i5 = i16;
                                    i20 = i11;
                                    i19 = i12;
                                } else {
                                    zzkoVar = this;
                                    t2 = t;
                                    bArr2 = bArr;
                                    i14 = i2;
                                    zzhnVar2 = zzhnVar;
                                    i20 = i11;
                                    i17 = i6;
                                    i18 = i7;
                                    i19 = i12;
                                    unsafe2 = unsafe;
                                    i15 = -1;
                                }
                            }
                        }
                    }
                }
                i16 = zzhl.zza(b, bArr, i5, i2, zze(t), zzhnVar);
                zzkoVar = this;
                t2 = t;
                bArr2 = bArr;
                i14 = i2;
                zzhnVar2 = zzhnVar;
                i17 = i6;
                i18 = i7;
                unsafe2 = unsafe;
                i15 = -1;
            }
            int i33 = i19;
            Unsafe unsafe3 = unsafe2;
            if (i20 != 1048575) {
                unsafe3.putInt(t, i20, i33);
            }
            if (i16 != i2) {
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
        int i2 = this.zzc[i];
        Object zzf = zzma.zzf(obj, zzd(i) & 1048575);
        if (zzf == null) {
            return ub;
        }
        zzjg zzc = zzc(i);
        if (zzc == null) {
            return ub;
        }
        return (UB) zza(i, i2, this.zzs.zza(zzf), zzc, (zzjg) ub, (zzlu<UT, zzjg>) zzluVar);
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
    /* JADX WARN: Type inference failed for: r1v14 */
    /* JADX WARN: Type inference failed for: r1v16, types: [com.google.android.gms.internal.vision.zzlc] */
    /* JADX WARN: Type inference failed for: r1v22 */
    /* JADX WARN: Type inference failed for: r1v5, types: [com.google.android.gms.internal.vision.zzlc] */
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
            if (((268435456 & zzd) != 0) && !zza((zzko<T>) t, i6, i2, i, i10)) {
                return false;
            }
            switch ((267386880 & zzd) >>> 20) {
                case 9:
                case 17:
                    if (zza((zzko<T>) t, i6, i2, i, i10) && !zza(t, zzd, zza(i6))) {
                        return false;
                    }
                    break;
                case 27:
                case 49:
                    List list = (List) zzma.zzf(t, zzd & 1048575);
                    if (!list.isEmpty()) {
                        ?? zza2 = zza(i6);
                        int i11 = 0;
                        while (true) {
                            if (i11 < list.size()) {
                                if (!zza2.zzd(list.get(i11))) {
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
                    Map<?, ?> zzc = this.zzs.zzc(zzma.zzf(t, zzd & 1048575));
                    if (!zzc.isEmpty()) {
                        if (this.zzs.zzb(zzb(i6)).zzc.zza() == zzmo.MESSAGE) {
                            zzlc<T> zzlcVar = 0;
                            Iterator<?> it = zzc.values().iterator();
                            while (true) {
                                if (it.hasNext()) {
                                    Object next = it.next();
                                    if (zzlcVar == null) {
                                        zzlcVar = zzky.zza().zza((Class) next.getClass());
                                    }
                                    boolean zzd2 = zzlcVar.zzd(next);
                                    zzlcVar = zzlcVar;
                                    if (!zzd2) {
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
                    if (zza((zzko<T>) t, i7, i6) && !zza(t, zzd, zza(i6))) {
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

    private final void zza(Object obj, int i, zzld zzldVar) throws IOException {
        if (zzf(i)) {
            zzma.zza(obj, i & 1048575, zzldVar.zzm());
        } else if (this.zzi) {
            zzma.zza(obj, i & 1048575, zzldVar.zzl());
        } else {
            zzma.zza(obj, i & 1048575, zzldVar.zzn());
        }
    }

    private final int zzd(int i) {
        return this.zzc[i + 1];
    }

    private final int zze(int i) {
        return this.zzc[i + 2];
    }

    private static boolean zzf(int i) {
        return (i & 536870912) != 0;
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
                return zzma.zze(t, j2) != FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE;
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
        if (i >= this.zze && i <= this.zzf) {
            return zzb(i, 0);
        }
        return -1;
    }

    private final int zza(int i, int i2) {
        if (i >= this.zze && i <= this.zzf) {
            return zzb(i, i2);
        }
        return -1;
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
