package com.google.android.gms.internal.mlkit_common;

import java.util.Arrays;

/* loaded from: classes.dex */
final class zzbb extends zzat {
    static final zzat zza = new zzbb(null, new Object[0], 0);
    final transient Object[] zzb;
    private final transient Object zzc;
    private final transient int zzd;

    private zzbb(Object obj, Object[] objArr, int i) {
        this.zzc = obj;
        this.zzb = objArr;
        this.zzd = i;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:22:0x01ba  */
    /* JADX WARN: Type inference failed for: r15v0 */
    /* JADX WARN: Type inference failed for: r5v10, types: [java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r5v2, types: [int[]] */
    /* JADX WARN: Type inference failed for: r5v8 */
    /* JADX WARN: Type inference failed for: r6v5, types: [java.lang.Object[]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    static zzbb zzg(int i, Object[] objArr, zzas zzasVar) {
        int i2;
        short[] sArr;
        char c;
        char c2;
        boolean z;
        double d;
        int i3 = i;
        Object[] objArr2 = objArr;
        if (i3 == 0) {
            return (zzbb) zza;
        }
        Object obj = null;
        int i4 = 1;
        if (i3 == 1) {
            Object obj2 = objArr2[0];
            obj2.getClass();
            Object obj3 = objArr2[1];
            obj3.getClass();
            zzah.zza(obj2, obj3);
            return new zzbb(null, objArr2, 1);
        }
        zzae.zzb(i3, objArr2.length >> 1, "index");
        int max = Math.max(i3, 2);
        if (max < 751619276) {
            i2 = Integer.highestOneBit(max - 1);
            do {
                i2 += i2;
                d = i2;
                Double.isNaN(d);
            } while (d * 0.7d < max);
        } else {
            i2 = 1073741824;
            if (max >= 1073741824) {
                throw new IllegalArgumentException("collection too large");
            }
        }
        if (i3 == 1) {
            Object obj4 = objArr2[0];
            obj4.getClass();
            Object obj5 = objArr2[1];
            obj5.getClass();
            zzah.zza(obj4, obj5);
            i3 = 1;
        } else {
            int i5 = i2 - 1;
            char c3 = 65535;
            if (i2 <= 128) {
                byte[] bArr = new byte[i2];
                Arrays.fill(bArr, (byte) -1);
                int i6 = 0;
                int i7 = 0;
                while (i6 < i3) {
                    int i8 = i7 + i7;
                    int i9 = i6 + i6;
                    Object obj6 = objArr2[i9];
                    obj6.getClass();
                    Object obj7 = objArr2[i9 ^ i4];
                    obj7.getClass();
                    zzah.zza(obj6, obj7);
                    int zza2 = zzaj.zza(obj6.hashCode());
                    while (true) {
                        int i10 = zza2 & i5;
                        int i11 = bArr[i10] & 255;
                        if (i11 == 255) {
                            bArr[i10] = (byte) i8;
                            if (i7 < i6) {
                                objArr2[i8] = obj6;
                                objArr2[i8 ^ 1] = obj7;
                            }
                            i7++;
                        } else {
                            if (obj6.equals(objArr2[i11])) {
                                int i12 = i11 ^ 1;
                                Object obj8 = objArr2[i12];
                                obj8.getClass();
                                zzar zzarVar = new zzar(obj6, obj7, obj8);
                                objArr2[i12] = obj7;
                                obj = zzarVar;
                                break;
                            }
                            zza2 = i10 + 1;
                        }
                    }
                    i6++;
                    i4 = 1;
                }
                if (i7 == i3) {
                    obj = bArr;
                } else {
                    sArr = new Object[]{bArr, Integer.valueOf(i7), obj};
                }
            } else if (i2 <= 32768) {
                sArr = new short[i2];
                Arrays.fill(sArr, (short) -1);
                int i13 = 0;
                for (int i14 = 0; i14 < i3; i14++) {
                    int i15 = i13 + i13;
                    int i16 = i14 + i14;
                    Object obj9 = objArr2[i16];
                    obj9.getClass();
                    Object obj10 = objArr2[i16 ^ 1];
                    obj10.getClass();
                    zzah.zza(obj9, obj10);
                    int zza3 = zzaj.zza(obj9.hashCode());
                    while (true) {
                        int i17 = zza3 & i5;
                        char c4 = (char) sArr[i17];
                        if (c4 == 65535) {
                            sArr[i17] = (short) i15;
                            if (i13 < i14) {
                                objArr2[i15] = obj9;
                                objArr2[i15 ^ 1] = obj10;
                            }
                            i13++;
                        } else {
                            if (obj9.equals(objArr2[c4])) {
                                int i18 = c4 ^ 1;
                                Object obj11 = objArr2[i18];
                                obj11.getClass();
                                zzar zzarVar2 = new zzar(obj9, obj10, obj11);
                                objArr2[i18] = obj10;
                                obj = zzarVar2;
                                break;
                            }
                            zza3 = i17 + 1;
                        }
                    }
                }
                if (i13 != i3) {
                    c2 = 2;
                    obj = new Object[]{sArr, Integer.valueOf(i13), obj};
                    c = 1;
                    z = obj instanceof Object[];
                    Object obj12 = obj;
                    if (z) {
                        Object[] objArr3 = (Object[]) obj;
                        zzar zzarVar3 = (zzar) objArr3[c2];
                        if (zzasVar == null) {
                            throw zzarVar3.zza();
                        }
                        zzasVar.zzc = zzarVar3;
                        Object obj13 = objArr3[0];
                        int intValue = ((Integer) objArr3[c]).intValue();
                        objArr2 = Arrays.copyOf(objArr2, intValue + intValue);
                        obj12 = obj13;
                        i3 = intValue;
                    }
                    return new zzbb(obj12, objArr2, i3);
                }
            } else {
                int i19 = 1;
                sArr = new int[i2];
                Arrays.fill((int[]) sArr, -1);
                int i20 = 0;
                int i21 = 0;
                while (i20 < i3) {
                    int i22 = i21 + i21;
                    int i23 = i20 + i20;
                    Object obj14 = objArr2[i23];
                    obj14.getClass();
                    Object obj15 = objArr2[i23 ^ i19];
                    obj15.getClass();
                    zzah.zza(obj14, obj15);
                    int zza4 = zzaj.zza(obj14.hashCode());
                    while (true) {
                        int i24 = zza4 & i5;
                        ?? r15 = sArr[i24];
                        if (r15 == c3) {
                            sArr[i24] = i22;
                            if (i21 < i20) {
                                objArr2[i22] = obj14;
                                objArr2[i22 ^ 1] = obj15;
                            }
                            i21++;
                        } else {
                            if (obj14.equals(objArr2[r15])) {
                                int i25 = r15 ^ 1;
                                Object obj16 = objArr2[i25];
                                obj16.getClass();
                                zzar zzarVar4 = new zzar(obj14, obj15, obj16);
                                objArr2[i25] = obj15;
                                obj = zzarVar4;
                                break;
                            }
                            zza4 = i24 + 1;
                            c3 = 65535;
                        }
                    }
                    i20++;
                    i19 = 1;
                    c3 = 65535;
                }
                if (i21 != i3) {
                    c = 1;
                    c2 = 2;
                    obj = new Object[]{sArr, Integer.valueOf(i21), obj};
                    z = obj instanceof Object[];
                    Object obj122 = obj;
                    if (z) {
                    }
                    return new zzbb(obj122, objArr2, i3);
                }
            }
            obj = sArr;
        }
        c2 = 2;
        c = 1;
        z = obj instanceof Object[];
        Object obj1222 = obj;
        if (z) {
        }
        return new zzbb(obj1222, objArr2, i3);
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x009e A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x009f A[RETURN] */
    @Override // com.google.android.gms.internal.mlkit_common.zzat, java.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object get(Object obj) {
        Object obj2;
        if (obj != null) {
            int i = this.zzd;
            Object[] objArr = this.zzb;
            if (i == 1) {
                Object obj3 = objArr[0];
                obj3.getClass();
                if (obj3.equals(obj)) {
                    obj2 = objArr[1];
                    obj2.getClass();
                }
            } else {
                Object obj4 = this.zzc;
                if (obj4 != null) {
                    if (obj4 instanceof byte[]) {
                        byte[] bArr = (byte[]) obj4;
                        int length = bArr.length - 1;
                        int zza2 = zzaj.zza(obj.hashCode());
                        while (true) {
                            int i2 = zza2 & length;
                            int i3 = bArr[i2] & 255;
                            if (i3 == 255) {
                                break;
                            }
                            if (obj.equals(objArr[i3])) {
                                obj2 = objArr[i3 ^ 1];
                                break;
                            }
                            zza2 = i2 + 1;
                        }
                    } else if (obj4 instanceof short[]) {
                        short[] sArr = (short[]) obj4;
                        int length2 = sArr.length - 1;
                        int zza3 = zzaj.zza(obj.hashCode());
                        while (true) {
                            int i4 = zza3 & length2;
                            char c = (char) sArr[i4];
                            if (c == 65535) {
                                break;
                            }
                            if (obj.equals(objArr[c])) {
                                obj2 = objArr[c ^ 1];
                                break;
                            }
                            zza3 = i4 + 1;
                        }
                    } else {
                        int[] iArr = (int[]) obj4;
                        int length3 = iArr.length - 1;
                        int zza4 = zzaj.zza(obj.hashCode());
                        while (true) {
                            int i5 = zza4 & length3;
                            int i6 = iArr[i5];
                            if (i6 == -1) {
                                break;
                            }
                            if (obj.equals(objArr[i6])) {
                                obj2 = objArr[i6 ^ 1];
                                break;
                            }
                            zza4 = i5 + 1;
                        }
                    }
                }
            }
            if (obj2 != null) {
                return null;
            }
            return obj2;
        }
        obj2 = null;
        if (obj2 != null) {
        }
    }

    @Override // java.util.Map
    public final int size() {
        return this.zzd;
    }

    @Override // com.google.android.gms.internal.mlkit_common.zzat
    final zzam zza() {
        return new zzba(this.zzb, 1, this.zzd);
    }

    @Override // com.google.android.gms.internal.mlkit_common.zzat
    final zzau zzd() {
        return new zzay(this, this.zzb, 0, this.zzd);
    }

    @Override // com.google.android.gms.internal.mlkit_common.zzat
    final zzau zze() {
        return new zzaz(this, new zzba(this.zzb, 0, this.zzd));
    }
}
