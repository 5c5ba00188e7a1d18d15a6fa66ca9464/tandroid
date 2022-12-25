package com.google.android.gms.internal.vision;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* compiled from: com.google.android.gms:play-services-vision-common@@19.1.3 */
/* loaded from: classes.dex */
final class zzjw extends zzju {
    private static final Class<?> zza = Collections.unmodifiableList(Collections.emptyList()).getClass();

    private zzjw() {
        super();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.vision.zzju
    public final void zzb(Object obj, long j) {
        Object unmodifiableList;
        List list = (List) zzma.zzf(obj, j);
        if (list instanceof zzjv) {
            unmodifiableList = ((zzjv) list).zze();
        } else if (zza.isAssignableFrom(list.getClass())) {
            return;
        } else {
            if ((list instanceof zzkw) && (list instanceof zzjl)) {
                zzjl zzjlVar = (zzjl) list;
                if (zzjlVar.zza()) {
                    zzjlVar.zzb();
                    return;
                }
                return;
            }
            unmodifiableList = Collections.unmodifiableList(list);
        }
        zzma.zza(obj, j, unmodifiableList);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <L> List<L> zza(Object obj, long j, int i) {
        zzjs zzjsVar;
        List<L> arrayList;
        List<L> zzc = zzc(obj, j);
        if (zzc.isEmpty()) {
            if (zzc instanceof zzjv) {
                arrayList = new zzjs(i);
            } else if ((zzc instanceof zzkw) && (zzc instanceof zzjl)) {
                arrayList = ((zzjl) zzc).zza(i);
            } else {
                arrayList = new ArrayList<>(i);
            }
            zzma.zza(obj, j, arrayList);
            return arrayList;
        }
        if (zza.isAssignableFrom(zzc.getClass())) {
            ArrayList arrayList2 = new ArrayList(zzc.size() + i);
            arrayList2.addAll(zzc);
            zzma.zza(obj, j, arrayList2);
            zzjsVar = arrayList2;
        } else if (zzc instanceof zzlz) {
            zzjs zzjsVar2 = new zzjs(zzc.size() + i);
            zzjsVar2.addAll((zzlz) zzc);
            zzma.zza(obj, j, zzjsVar2);
            zzjsVar = zzjsVar2;
        } else if ((zzc instanceof zzkw) && (zzc instanceof zzjl)) {
            zzjl zzjlVar = (zzjl) zzc;
            if (zzjlVar.zza()) {
                return zzc;
            }
            zzjl zza2 = zzjlVar.zza(zzc.size() + i);
            zzma.zza(obj, j, zza2);
            return zza2;
        } else {
            return zzc;
        }
        return zzjsVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.vision.zzju
    public final <E> void zza(Object obj, Object obj2, long j) {
        List zzc = zzc(obj2, j);
        List zza2 = zza(obj, j, zzc.size());
        int size = zza2.size();
        int size2 = zzc.size();
        if (size > 0 && size2 > 0) {
            zza2.addAll(zzc);
        }
        if (size > 0) {
            zzc = zza2;
        }
        zzma.zza(obj, j, zzc);
    }

    private static <E> List<E> zzc(Object obj, long j) {
        return (List) zzma.zzf(obj, j);
    }
}
