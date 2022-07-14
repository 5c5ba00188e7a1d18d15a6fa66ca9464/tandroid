package com.google.android.gms.internal.wearable;

import java.io.IOException;
/* compiled from: com.google.android.gms:play-services-wearable@@17.1.0 */
/* loaded from: classes3.dex */
public final class zzba extends IOException {
    zzba() {
        super("CodedOutputStream was writing to a flat byte array and ran out of space.");
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public zzba(String str, Throwable th) {
        super(r3.length() != 0 ? "CodedOutputStream was writing to a flat byte array and ran out of space.: ".concat(r3) : new String("CodedOutputStream was writing to a flat byte array and ran out of space.: "), th);
        String valueOf = String.valueOf(str);
    }

    public zzba(Throwable th) {
        super("CodedOutputStream was writing to a flat byte array and ran out of space.", th);
    }
}
