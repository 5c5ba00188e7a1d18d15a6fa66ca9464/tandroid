package com.google.android.gms.internal.firebase_messaging;

import j$.util.concurrent.ConcurrentHashMap;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.List;
import java.util.Vector;
/* loaded from: classes.dex */
final class zzp {
    private final ConcurrentHashMap zza = new ConcurrentHashMap(16, 0.75f, 10);
    private final ReferenceQueue zzb = new ReferenceQueue();

    public final List zza(Throwable th, boolean z) {
        while (true) {
            Reference poll = this.zzb.poll();
            if (poll == null) {
                break;
            }
            this.zza.remove(poll);
        }
        List list = (List) this.zza.get(new zzo(th, null));
        if (list != null) {
            return list;
        }
        Vector vector = new Vector(2);
        List list2 = (List) this.zza.putIfAbsent(new zzo(th, this.zzb), vector);
        return list2 == null ? vector : list2;
    }
}
