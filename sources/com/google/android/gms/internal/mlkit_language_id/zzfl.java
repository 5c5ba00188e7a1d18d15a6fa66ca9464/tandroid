package com.google.android.gms.internal.mlkit_language_id;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
final class zzfl extends zzfj {
    private static final Class zza = Collections.unmodifiableList(Collections.emptyList()).getClass();

    private zzfl() {
        super();
    }

    private static List zzb(Object obj, long j) {
        return (List) zzhn.zzf(obj, j);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzfj
    final void zza(Object obj, long j) {
        Object unmodifiableList;
        List list = (List) zzhn.zzf(obj, j);
        if (list instanceof zzfg) {
            unmodifiableList = ((zzfg) list).a_();
        } else {
            if (zza.isAssignableFrom(list.getClass())) {
                return;
            }
            if ((list instanceof zzgi) && (list instanceof zzew)) {
                zzew zzewVar = (zzew) list;
                if (zzewVar.zza()) {
                    zzewVar.b_();
                    return;
                }
                return;
            }
            unmodifiableList = Collections.unmodifiableList(list);
        }
        zzhn.zza(obj, j, unmodifiableList);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0093  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x008c A[ADDED_TO_REGION] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzfj
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    final void zza(Object obj, Object obj2, long j) {
        zzfh zzfhVar;
        int size;
        List zzb = zzb(obj2, j);
        int size2 = zzb.size();
        List zzb2 = zzb(obj, j);
        if (!zzb2.isEmpty()) {
            if (zza.isAssignableFrom(zzb2.getClass())) {
                ArrayList arrayList = new ArrayList(zzb2.size() + size2);
                arrayList.addAll(zzb2);
                zzfhVar = arrayList;
            } else {
                if (!(zzb2 instanceof zzhi)) {
                    if ((zzb2 instanceof zzgi) && (zzb2 instanceof zzew)) {
                        zzew zzewVar = (zzew) zzb2;
                        if (!zzewVar.zza()) {
                            zzb2 = zzewVar.zzb(zzb2.size() + size2);
                        }
                    }
                    size = zzb2.size();
                    int size3 = zzb.size();
                    if (size > 0 && size3 > 0) {
                        zzb2.addAll(zzb);
                    }
                    if (size > 0) {
                        zzb = zzb2;
                    }
                    zzhn.zza(obj, j, zzb);
                }
                zzfh zzfhVar2 = new zzfh(zzb2.size() + size2);
                zzfhVar2.addAll((zzhi) zzb2);
                zzfhVar = zzfhVar2;
            }
            zzhn.zza(obj, j, zzfhVar);
            zzb2 = zzfhVar;
            size = zzb2.size();
            int size32 = zzb.size();
            if (size > 0) {
                zzb2.addAll(zzb);
            }
            if (size > 0) {
            }
            zzhn.zza(obj, j, zzb);
        }
        zzb2 = zzb2 instanceof zzfg ? new zzfh(size2) : ((zzb2 instanceof zzgi) && (zzb2 instanceof zzew)) ? ((zzew) zzb2).zzb(size2) : new ArrayList(size2);
        zzhn.zza(obj, j, zzb2);
        size = zzb2.size();
        int size322 = zzb.size();
        if (size > 0) {
        }
        if (size > 0) {
        }
        zzhn.zza(obj, j, zzb);
    }
}
