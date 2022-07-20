package com.google.mlkit.common.sdkinternal;

import com.google.mlkit.common.sdkinternal.Cleaner;
import java.lang.ref.ReferenceQueue;
import java.util.Set;
/* compiled from: com.google.mlkit:common@@17.0.0 */
/* loaded from: classes.dex */
public final /* synthetic */ class zzb implements Runnable {
    private final ReferenceQueue zza;
    private final Set zzb;

    public zzb(ReferenceQueue referenceQueue, Set set) {
        this.zza = referenceQueue;
        this.zzb = set;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ReferenceQueue referenceQueue = this.zza;
        Set set = this.zzb;
        while (!set.isEmpty()) {
            try {
                ((Cleaner.zza) referenceQueue.remove()).clean();
            } catch (InterruptedException unused) {
            }
        }
    }
}
