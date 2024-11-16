package com.google.android.play.core.integrity;

import android.os.Build;

/* loaded from: classes.dex */
final class ao extends IntegrityTokenRequest {
    private final String a;
    private final Long b;
    private final Object c = null;

    /* synthetic */ ao(String str, Long l, Object obj, an anVar) {
        this.a = str;
        this.b = l;
    }

    private static boolean a() {
        return Build.VERSION.SDK_INT >= 23;
    }

    @Override // com.google.android.play.core.integrity.IntegrityTokenRequest
    public final Long cloudProjectNumber() {
        return this.b;
    }

    public final boolean equals(Object obj) {
        boolean z;
        if (obj == this) {
            return true;
        }
        if (obj instanceof IntegrityTokenRequest) {
            IntegrityTokenRequest integrityTokenRequest = (IntegrityTokenRequest) obj;
            if (this.a.equals(integrityTokenRequest.nonce())) {
                Long l = this.b;
                Long cloudProjectNumber = integrityTokenRequest.cloudProjectNumber();
                if (l != null ? l.equals(cloudProjectNumber) : cloudProjectNumber == null) {
                    z = true;
                    if ((obj instanceof ao) || !a()) {
                        return z;
                    }
                    ao aoVar = (ao) obj;
                    if (!z) {
                        return false;
                    }
                    Object obj2 = aoVar.c;
                    return true;
                }
            }
        }
        z = false;
        if (obj instanceof ao) {
        }
        return z;
    }

    public final int hashCode() {
        int hashCode = this.a.hashCode() ^ 1000003;
        Long l = this.b;
        int hashCode2 = (hashCode * 1000003) ^ (l == null ? 0 : l.hashCode());
        return a() ? hashCode2 * 1000003 : hashCode2;
    }

    @Override // com.google.android.play.core.integrity.IntegrityTokenRequest
    public final String nonce() {
        return this.a;
    }

    public final String toString() {
        String str = "IntegrityTokenRequest{nonce=" + this.a + ", cloudProjectNumber=" + this.b;
        if (a()) {
            str = str.concat(", network=null");
        }
        return str.concat("}");
    }
}
