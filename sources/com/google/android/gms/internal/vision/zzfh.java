package com.google.android.gms.internal.vision;

import java.util.List;

/* loaded from: classes.dex */
final class zzfh extends zzfd {
    private final zzfg zza = new zzfg();

    zzfh() {
    }

    @Override // com.google.android.gms.internal.vision.zzfd
    public final void zza(Throwable th) {
        th.printStackTrace();
        List<Throwable> zza = this.zza.zza(th, false);
        if (zza == null) {
            return;
        }
        synchronized (zza) {
            try {
                for (Throwable th2 : zza) {
                    System.err.print("Suppressed: ");
                    th2.printStackTrace();
                }
            } catch (Throwable th3) {
                throw th3;
            }
        }
    }
}
