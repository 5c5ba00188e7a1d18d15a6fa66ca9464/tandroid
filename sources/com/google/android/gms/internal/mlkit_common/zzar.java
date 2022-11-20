package com.google.android.gms.internal.mlkit_common;

import java.util.Objects;
/* compiled from: com.google.mlkit:common@@17.0.0 */
/* loaded from: classes.dex */
final class zzar extends zzaq {
    private final zzap zza = new zzap();

    @Override // com.google.android.gms.internal.mlkit_common.zzaq
    public final void zza(Throwable th, Throwable th2) {
        if (th2 == th) {
            throw new IllegalArgumentException("Self suppression is not allowed.", th2);
        }
        Objects.requireNonNull(th2, "The suppressed exception cannot be null.");
        this.zza.zza(th, true).add(th2);
    }
}
