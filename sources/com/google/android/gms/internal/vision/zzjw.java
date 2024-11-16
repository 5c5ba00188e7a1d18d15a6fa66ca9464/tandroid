package com.google.android.gms.internal.vision;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
final class zzjw extends zzju {
    private static final Class zza = Collections.unmodifiableList(Collections.emptyList()).getClass();

    private zzjw() {
        super();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static List zza(Object obj, long j, int i) {
        List zza2;
        zzjs zzjsVar;
        List zzc = zzc(obj, j);
        if (!zzc.isEmpty()) {
            if (zza.isAssignableFrom(zzc.getClass())) {
                ArrayList arrayList = new ArrayList(zzc.size() + i);
                arrayList.addAll(zzc);
                zzjsVar = arrayList;
            } else if (zzc instanceof zzlz) {
                zzjs zzjsVar2 = new zzjs(zzc.size() + i);
                zzjsVar2.addAll((zzlz) zzc);
                zzjsVar = zzjsVar2;
            } else {
                if (!(zzc instanceof zzkw) || !(zzc instanceof zzjl)) {
                    return zzc;
                }
                zzjl zzjlVar = (zzjl) zzc;
                if (zzjlVar.zza()) {
                    return zzc;
                }
                zza2 = zzjlVar.zza(zzc.size() + i);
            }
            zzma.zza(obj, j, zzjsVar);
            return zzjsVar;
        }
        zza2 = zzc instanceof zzjv ? new zzjs(i) : ((zzc instanceof zzkw) && (zzc instanceof zzjl)) ? ((zzjl) zzc).zza(i) : new ArrayList(i);
        zzma.zza(obj, j, zza2);
        return zza2;
    }

    private static List zzc(Object obj, long j) {
        return (List) zzma.zzf(obj, j);
    }

    @Override // com.google.android.gms.internal.vision.zzju
    final void zza(Object obj, Object obj2, long j) {
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

    @Override // com.google.android.gms.internal.vision.zzju
    final void zzb(Object obj, long j) {
        Object unmodifiableList;
        List list = (List) zzma.zzf(obj, j);
        if (list instanceof zzjv) {
            unmodifiableList = ((zzjv) list).zze();
        } else {
            if (zza.isAssignableFrom(list.getClass())) {
                return;
            }
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
}
