package com.google.android.gms.tasks;

import java.util.concurrent.Callable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzz implements Runnable {
    final /* synthetic */ zzw zza;
    final /* synthetic */ Callable zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzz(zzw zzwVar, Callable callable) {
        this.zza = zzwVar;
        this.zzb = callable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            this.zza.zzb(this.zzb.call());
        } catch (Exception e) {
            this.zza.zza(e);
        } catch (Throwable th) {
            this.zza.zza(new RuntimeException(th));
        }
    }
}
