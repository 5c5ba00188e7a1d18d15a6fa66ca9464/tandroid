package com.google.android.gms.internal.mlkit_common;

import java.util.Arrays;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.mlkit:common@@18.10.0 */
/* loaded from: classes.dex */
public final class zzbb extends zzat {
    static final zzat zza = new zzbb(null, new Object[0], 0);
    final transient Object[] zzb;
    private final transient Object zzc;
    private final transient int zzd;

    private zzbb(Object obj, Object[] objArr, int i) {
        this.zzc = obj;
        this.zzb = objArr;
        this.zzd = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:80:0x01ba  */
    /* JADX WARN: Type inference failed for: r15v0 */
    /* JADX WARN: Type inference failed for: r5v10, types: [java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r5v2, types: [int[]] */
    /* JADX WARN: Type inference failed for: r5v8 */
    /* JADX WARN: Type inference failed for: r6v5, types: [java.lang.Object[]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static zzbb zzg(int i, Object[] objArr, zzas zzasVar) {
        int i2;
        short[] sArr;
        char c;
        char c2;
        Object obj;
        Object[] objArr2;
        boolean z;
        double d;
        int i3 = i;
        Object[] objArr3 = objArr;
        if (i3 == 0) {
            return (zzbb) zza;
        }
        Object obj2 = null;
        int i4 = 1;
        if (i3 == 1) {
            Object obj3 = objArr3[0];
            obj3.getClass();
            Object obj4 = objArr3[1];
            obj4.getClass();
            zzah.zza(obj3, obj4);
            return new zzbb(null, objArr3, 1);
        }
        zzae.zzb(i3, objArr3.length >> 1, "index");
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
            Object obj5 = objArr3[0];
            obj5.getClass();
            Object obj6 = objArr3[1];
            obj6.getClass();
            zzah.zza(obj5, obj6);
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
                    Object obj7 = objArr3[i9];
                    obj7.getClass();
                    Object obj8 = objArr3[i9 ^ i4];
                    obj8.getClass();
                    zzah.zza(obj7, obj8);
                    int zza2 = zzaj.zza(obj7.hashCode());
                    while (true) {
                        int i10 = zza2 & i5;
                        int i11 = bArr[i10] & 255;
                        if (i11 != 255) {
                            if (obj7.equals(objArr3[i11])) {
                                int i12 = i11 ^ 1;
                                Object obj9 = objArr3[i12];
                                obj9.getClass();
                                zzar zzarVar = new zzar(obj7, obj8, obj9);
                                objArr3[i12] = obj8;
                                obj2 = zzarVar;
                                break;
                            }
                            zza2 = i10 + 1;
                        } else {
                            bArr[i10] = (byte) i8;
                            if (i7 < i6) {
                                objArr3[i8] = obj7;
                                objArr3[i8 ^ 1] = obj8;
                            }
                            i7++;
                        }
                    }
                    i6++;
                    i4 = 1;
                }
                if (i7 == i3) {
                    obj2 = bArr;
                } else {
                    sArr = new Object[]{bArr, Integer.valueOf(i7), obj2};
                }
            } else if (i2 <= 32768) {
                sArr = new short[i2];
                Arrays.fill(sArr, (short) -1);
                int i13 = 0;
                for (int i14 = 0; i14 < i3; i14++) {
                    int i15 = i13 + i13;
                    int i16 = i14 + i14;
                    Object obj10 = objArr3[i16];
                    obj10.getClass();
                    Object obj11 = objArr3[i16 ^ 1];
                    obj11.getClass();
                    zzah.zza(obj10, obj11);
                    int zza3 = zzaj.zza(obj10.hashCode());
                    while (true) {
                        int i17 = zza3 & i5;
                        char c4 = (char) sArr[i17];
                        if (c4 != 65535) {
                            if (obj10.equals(objArr3[c4])) {
                                int i18 = c4 ^ 1;
                                Object obj12 = objArr3[i18];
                                obj12.getClass();
                                zzar zzarVar2 = new zzar(obj10, obj11, obj12);
                                objArr3[i18] = obj11;
                                obj2 = zzarVar2;
                                break;
                            }
                            zza3 = i17 + 1;
                        } else {
                            sArr[i17] = (short) i15;
                            if (i13 < i14) {
                                objArr3[i15] = obj10;
                                objArr3[i15 ^ 1] = obj11;
                            }
                            i13++;
                        }
                    }
                }
                if (i13 != i3) {
                    c2 = 2;
                    objArr2 = new Object[]{sArr, Integer.valueOf(i13), obj2};
                    c = 1;
                    obj = objArr2;
                    z = obj instanceof Object[];
                    Object obj13 = obj;
                    if (z) {
                        Object[] objArr4 = (Object[]) obj;
                        zzar zzarVar3 = (zzar) objArr4[c2];
                        if (zzasVar != null) {
                            zzasVar.zzc = zzarVar3;
                            Object obj14 = objArr4[0];
                            int intValue = ((Integer) objArr4[c]).intValue();
                            objArr3 = Arrays.copyOf(objArr3, intValue + intValue);
                            obj13 = obj14;
                            i3 = intValue;
                        } else {
                            throw zzarVar3.zza();
                        }
                    }
                    return new zzbb(obj13, objArr3, i3);
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
                    Object obj15 = objArr3[i23];
                    obj15.getClass();
                    Object obj16 = objArr3[i23 ^ i19];
                    obj16.getClass();
                    zzah.zza(obj15, obj16);
                    int zza4 = zzaj.zza(obj15.hashCode());
                    while (true) {
                        int i24 = zza4 & i5;
                        ?? r15 = sArr[i24];
                        if (r15 != c3) {
                            if (obj15.equals(objArr3[r15])) {
                                int i25 = r15 ^ 1;
                                Object obj17 = objArr3[i25];
                                obj17.getClass();
                                zzar zzarVar4 = new zzar(obj15, obj16, obj17);
                                objArr3[i25] = obj16;
                                obj2 = zzarVar4;
                                break;
                            }
                            zza4 = i24 + 1;
                            c3 = 65535;
                        } else {
                            sArr[i24] = i22;
                            if (i21 < i20) {
                                objArr3[i22] = obj15;
                                objArr3[i22 ^ 1] = obj16;
                            }
                            i21++;
                        }
                    }
                    i20++;
                    i19 = 1;
                    c3 = 65535;
                }
                if (i21 != i3) {
                    c = 1;
                    c2 = 2;
                    obj = new Object[]{sArr, Integer.valueOf(i21), obj2};
                    z = obj instanceof Object[];
                    Object obj132 = obj;
                    if (z) {
                    }
                    return new zzbb(obj132, objArr3, i3);
                }
            }
            obj2 = sArr;
        }
        c2 = 2;
        objArr2 = obj2;
        c = 1;
        obj = objArr2;
        z = obj instanceof Object[];
        Object obj1322 = obj;
        if (z) {
        }
        return new zzbb(obj1322, objArr3, i3);
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x009e A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x009f A[RETURN] */
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
                            } else if (obj.equals(objArr[i3])) {
                                obj2 = objArr[i3 ^ 1];
                                break;
                            } else {
                                zza2 = i2 + 1;
                            }
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
                            } else if (obj.equals(objArr[c])) {
                                obj2 = objArr[c ^ 1];
                                break;
                            } else {
                                zza3 = i4 + 1;
                            }
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
                            } else if (obj.equals(objArr[i6])) {
                                obj2 = objArr[i6 ^ 1];
                                break;
                            } else {
                                zza4 = i5 + 1;
                            }
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
