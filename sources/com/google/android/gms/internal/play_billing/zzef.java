package com.google.android.gms.internal.play_billing;

/* loaded from: classes.dex */
public final class zzef extends RuntimeException {
    public zzef(zzdf zzdfVar) {
        super("Message was missing required fields.  (Lite runtime could not determine which fields were missing).");
    }

    public final zzci zza() {
        return new zzci(getMessage());
    }
}
