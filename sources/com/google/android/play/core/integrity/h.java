package com.google.android.play.core.integrity;

import com.google.android.play.core.integrity.StandardIntegrityManager;

/* loaded from: classes.dex */
final class h extends StandardIntegrityManager.StandardIntegrityTokenRequest {
    private final String a;

    /* synthetic */ h(String str, g gVar) {
        this.a = str;
    }

    @Override // com.google.android.play.core.integrity.StandardIntegrityManager.StandardIntegrityTokenRequest
    public final String a() {
        return this.a;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StandardIntegrityManager.StandardIntegrityTokenRequest)) {
            return false;
        }
        String str = this.a;
        String a = ((StandardIntegrityManager.StandardIntegrityTokenRequest) obj).a();
        return str == null ? a == null : str.equals(a);
    }

    public final int hashCode() {
        String str = this.a;
        return (str == null ? 0 : str.hashCode()) ^ 1000003;
    }

    public final String toString() {
        return "StandardIntegrityTokenRequest{requestHash=" + this.a + "}";
    }
}
