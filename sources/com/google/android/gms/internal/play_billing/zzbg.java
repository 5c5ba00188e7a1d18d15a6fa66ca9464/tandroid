package com.google.android.gms.internal.play_billing;

import java.io.IOException;
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
public final class zzbg extends IOException {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzbg(String str, Throwable th) {
        super("CodedOutputStream was writing to a flat byte array and ran out of space.: ".concat(String.valueOf(str)), th);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzbg(Throwable th) {
        super("CodedOutputStream was writing to a flat byte array and ran out of space.", th);
    }
}
