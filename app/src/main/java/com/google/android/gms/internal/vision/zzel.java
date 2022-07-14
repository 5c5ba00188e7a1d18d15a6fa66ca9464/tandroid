package com.google.android.gms.internal.vision;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
/* compiled from: com.google.android.gms:play-services-vision-common@@19.1.3 */
/* loaded from: classes3.dex */
public final class zzel<K, V> extends zzek<K, V> {
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0049, code lost:
        if (r9.zzf() == false) goto L39;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final zzem<K, V> zza() {
        zzej zzejVar;
        Set<Map.Entry<K, Collection<V>>> entrySet = this.zza.entrySet();
        if (entrySet.isEmpty()) {
            return zzdz.zza;
        }
        zzei zzeiVar = new zzei(entrySet.size());
        char c = 0;
        int i = 0;
        for (Map.Entry<K, Collection<V>> entry : entrySet) {
            K key = entry.getKey();
            Collection<V> value = entry.getValue();
            if ((value instanceof zzej) && !(value instanceof SortedSet)) {
                zzejVar = (zzej) value;
            }
            Object[] array = value.toArray();
            int length = array.length;
            while (true) {
                switch (length) {
                    case 0:
                        zzejVar = zzev.zza;
                        break;
                    case 1:
                        zzejVar = new zzex(array[c]);
                        break;
                    default:
                        int zza = zzej.zza(length);
                        Object[] objArr = new Object[zza];
                        int i2 = zza - 1;
                        int i3 = 0;
                        int i4 = 0;
                        for (int i5 = 0; i5 < length; i5++) {
                            Object zza2 = zzeq.zza(array[i5], i5);
                            int hashCode = zza2.hashCode();
                            int zza3 = zzec.zza(hashCode);
                            while (true) {
                                int i6 = zza3 & i2;
                                Object obj = objArr[i6];
                                if (obj == null) {
                                    array[i3] = zza2;
                                    objArr[i6] = zza2;
                                    i4 += hashCode;
                                    i3++;
                                } else if (!obj.equals(zza2)) {
                                    zza3++;
                                }
                            }
                        }
                        Arrays.fill(array, i3, length, (Object) null);
                        if (i3 == 1) {
                            zzejVar = new zzex(array[0], i4);
                            break;
                        } else if (zzej.zza(i3) < zza / 2) {
                            length = i3;
                            c = 0;
                        } else {
                            int length2 = array.length;
                            if (i3 < (length2 >> 1) + (length2 >> 2)) {
                                array = Arrays.copyOf(array, i3);
                            }
                            zzejVar = new zzev(array, i4, objArr, i2, i3);
                            break;
                        }
                        break;
                }
            }
            if (zzejVar.isEmpty()) {
                c = 0;
            } else {
                int i7 = (zzeiVar.zzb + 1) << 1;
                if (i7 <= zzeiVar.zza.length) {
                    c = 0;
                } else {
                    Object[] objArr2 = zzeiVar.zza;
                    int length3 = zzeiVar.zza.length;
                    if (i7 < 0) {
                        throw new AssertionError("cannot store more than MAX_VALUE elements");
                    }
                    int i8 = length3 + (length3 >> 1) + 1;
                    if (i8 < i7) {
                        i8 = Integer.highestOneBit(i7 - 1) << 1;
                    }
                    if (i8 < 0) {
                        i8 = Integer.MAX_VALUE;
                    }
                    zzeiVar.zza = Arrays.copyOf(objArr2, i8);
                    c = 0;
                    zzeiVar.zzc = false;
                }
                zzdq.zza(key, zzejVar);
                zzeiVar.zza[zzeiVar.zzb * 2] = key;
                zzeiVar.zza[(zzeiVar.zzb * 2) + 1] = zzejVar;
                zzeiVar.zzb++;
                i += zzejVar.size();
            }
        }
        zzeiVar.zzc = true;
        return new zzem<>(zzes.zza(zzeiVar.zzb, zzeiVar.zza), i, null);
    }
}
