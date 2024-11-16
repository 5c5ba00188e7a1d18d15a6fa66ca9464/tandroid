package com.google.android.gms.internal.mlkit_language_id;

import j$.util.concurrent.ConcurrentHashMap;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.List;
import java.util.Vector;

/* loaded from: classes.dex */
final class zzs {
    private final ConcurrentHashMap zza = new ConcurrentHashMap(16, 0.75f, 10);
    private final ReferenceQueue zzb = new ReferenceQueue();

    zzs() {
    }

    public final List zza(Throwable th, boolean z) {
        while (true) {
            Reference poll = this.zzb.poll();
            if (poll == null) {
                break;
            }
            this.zza.remove(poll);
        }
        List list = (List) this.zza.get(new zzv(th, null));
        if (list != null) {
            return list;
        }
        Vector vector = new Vector(2);
        List list2 = (List) this.zza.putIfAbsent(new zzv(th, this.zzb), vector);
        return list2 == null ? vector : list2;
    }
}
