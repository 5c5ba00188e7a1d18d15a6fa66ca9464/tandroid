package com.google.android.gms.internal.play_billing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
final class zzcp extends zzct {
    private static final Class zza = Collections.unmodifiableList(Collections.emptyList()).getClass();

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzcp(zzco zzcoVar) {
        super(null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.play_billing.zzct
    public final void zza(Object obj, long j) {
        Object unmodifiableList;
        List list = (List) zzeq.zzf(obj, j);
        if (list instanceof zzcn) {
            unmodifiableList = ((zzcn) list).zze();
        } else if (zza.isAssignableFrom(list.getClass())) {
            return;
        } else {
            if ((list instanceof zzdm) && (list instanceof zzcf)) {
                zzcf zzcfVar = (zzcf) list;
                if (zzcfVar.zzc()) {
                    zzcfVar.zzb();
                    return;
                }
                return;
            }
            unmodifiableList = Collections.unmodifiableList(list);
        }
        zzeq.zzs(obj, j, unmodifiableList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0094 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x009c  */
    @Override // com.google.android.gms.internal.play_billing.zzct
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void zzb(Object obj, Object obj2, long j) {
        zzcm zzcmVar;
        int size;
        List list = (List) zzeq.zzf(obj2, j);
        int size2 = list.size();
        List list2 = (List) zzeq.zzf(obj, j);
        if (!list2.isEmpty()) {
            if (zza.isAssignableFrom(list2.getClass())) {
                ArrayList arrayList = new ArrayList(list2.size() + size2);
                arrayList.addAll(list2);
                zzcmVar = arrayList;
            } else if (!(list2 instanceof zzel)) {
                if ((list2 instanceof zzdm) && (list2 instanceof zzcf)) {
                    zzcf zzcfVar = (zzcf) list2;
                    if (!zzcfVar.zzc()) {
                        list2 = zzcfVar.zzd(list2.size() + size2);
                    }
                }
                size = list2.size();
                int size3 = list.size();
                if (size > 0 && size3 > 0) {
                    list2.addAll(list);
                }
                if (size > 0) {
                    list = list2;
                }
                zzeq.zzs(obj, j, list);
            } else {
                zzcm zzcmVar2 = new zzcm(list2.size() + size2);
                zzcmVar2.addAll(zzcmVar2.size(), (zzel) list2);
                zzcmVar = zzcmVar2;
            }
            zzeq.zzs(obj, j, zzcmVar);
            list2 = zzcmVar;
            size = list2.size();
            int size32 = list.size();
            if (size > 0) {
                list2.addAll(list);
            }
            if (size > 0) {
            }
            zzeq.zzs(obj, j, list);
        }
        list2 = list2 instanceof zzcn ? new zzcm(size2) : ((list2 instanceof zzdm) && (list2 instanceof zzcf)) ? ((zzcf) list2).zzd(size2) : new ArrayList(size2);
        zzeq.zzs(obj, j, list2);
        size = list2.size();
        int size322 = list.size();
        if (size > 0) {
        }
        if (size > 0) {
        }
        zzeq.zzs(obj, j, list);
    }
}
