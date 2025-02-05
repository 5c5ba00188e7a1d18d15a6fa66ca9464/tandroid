package com.google.android.gms.internal.mlkit_language_id;

/* loaded from: classes.dex */
final class zzu extends zzt {
    private final zzs zza = new zzs();

    zzu() {
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzt
    public final void zza(Throwable th, Throwable th2) {
        if (th2 == th) {
            throw new IllegalArgumentException("Self suppression is not allowed.", th2);
        }
        if (th2 == null) {
            throw new NullPointerException("The suppressed exception cannot be null.");
        }
        this.zza.zza(th, true).add(th2);
    }
}
