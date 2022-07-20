package com.google.mlkit.common.sdkinternal;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
/* compiled from: com.google.mlkit:common@@17.0.0 */
/* loaded from: classes.dex */
public class Cleaner {
    private final ReferenceQueue<Object> zza = new ReferenceQueue<>();
    private final Set<zza> zzb = Collections.synchronizedSet(new HashSet());

    /* compiled from: com.google.mlkit:common@@17.0.0 */
    /* loaded from: classes.dex */
    public interface Cleanable {
        void clean();
    }

    private Cleaner() {
    }

    /* compiled from: com.google.mlkit:common@@17.0.0 */
    /* loaded from: classes.dex */
    public static class zza extends PhantomReference<Object> implements Cleanable {
        private final Set<zza> zza;
        private final Runnable zzb;

        private zza(Object obj, ReferenceQueue<? super Object> referenceQueue, Set<zza> set, Runnable runnable) {
            super(obj, referenceQueue);
            this.zza = set;
            this.zzb = runnable;
        }

        @Override // com.google.mlkit.common.sdkinternal.Cleaner.Cleanable
        public final void clean() {
            if (!this.zza.remove(this)) {
                return;
            }
            clear();
            this.zzb.run();
        }
    }

    public static Cleaner create() {
        ThreadFactory threadFactory = com.google.mlkit.common.sdkinternal.zza.zza;
        Cleaner cleaner = new Cleaner();
        cleaner.register(cleaner, zzc.zza);
        threadFactory.newThread(new zzb(cleaner.zza, cleaner.zzb)).start();
        return cleaner;
    }

    public Cleanable register(Object obj, Runnable runnable) {
        zza zzaVar = new zza(obj, this.zza, this.zzb, runnable);
        this.zzb.add(zzaVar);
        return zzaVar;
    }
}
