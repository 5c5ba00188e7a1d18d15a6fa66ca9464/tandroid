package com.google.android.recaptcha.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
final class zzjo extends zzjs {
    private static final Class zza = Collections.unmodifiableList(Collections.emptyList()).getClass();

    private zzjo() {
        super(null);
    }

    /* synthetic */ zzjo(zzjn zzjnVar) {
        super(null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static List zzf(Object obj, long j, int i) {
        zzjl zzjlVar;
        List list = (List) zzlv.zzf(obj, j);
        if (list.isEmpty()) {
            List zzjlVar2 = list instanceof zzjm ? new zzjl(i) : ((list instanceof zzkm) && (list instanceof zzjb)) ? ((zzjb) list).zzd(i) : new ArrayList(i);
            zzlv.zzs(obj, j, zzjlVar2);
            return zzjlVar2;
        }
        if (zza.isAssignableFrom(list.getClass())) {
            ArrayList arrayList = new ArrayList(list.size() + i);
            arrayList.addAll(list);
            zzjlVar = arrayList;
        } else {
            if (!(list instanceof zzlq)) {
                if (!(list instanceof zzkm) || !(list instanceof zzjb)) {
                    return list;
                }
                zzjb zzjbVar = (zzjb) list;
                if (zzjbVar.zzc()) {
                    return list;
                }
                zzjb zzd = zzjbVar.zzd(list.size() + i);
                zzlv.zzs(obj, j, zzd);
                return zzd;
            }
            zzjl zzjlVar3 = new zzjl(list.size() + i);
            zzjlVar3.addAll(zzjlVar3.size(), (zzlq) list);
            zzjlVar = zzjlVar3;
        }
        zzlv.zzs(obj, j, zzjlVar);
        return zzjlVar;
    }

    @Override // com.google.android.recaptcha.internal.zzjs
    final List zza(Object obj, long j) {
        return zzf(obj, j, 10);
    }

    @Override // com.google.android.recaptcha.internal.zzjs
    final void zzb(Object obj, long j) {
        Object unmodifiableList;
        List list = (List) zzlv.zzf(obj, j);
        if (list instanceof zzjm) {
            unmodifiableList = ((zzjm) list).zze();
        } else {
            if (zza.isAssignableFrom(list.getClass())) {
                return;
            }
            if ((list instanceof zzkm) && (list instanceof zzjb)) {
                zzjb zzjbVar = (zzjb) list;
                if (zzjbVar.zzc()) {
                    zzjbVar.zzb();
                    return;
                }
                return;
            }
            unmodifiableList = Collections.unmodifiableList(list);
        }
        zzlv.zzs(obj, j, unmodifiableList);
    }

    @Override // com.google.android.recaptcha.internal.zzjs
    final void zzc(Object obj, Object obj2, long j) {
        List list = (List) zzlv.zzf(obj2, j);
        List zzf = zzf(obj, j, list.size());
        int size = zzf.size();
        int size2 = list.size();
        if (size > 0 && size2 > 0) {
            zzf.addAll(list);
        }
        if (size > 0) {
            list = zzf;
        }
        zzlv.zzs(obj, j, list);
    }
}
