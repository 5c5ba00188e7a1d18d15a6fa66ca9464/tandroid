package com.google.android.gms.common.internal;

import android.os.Bundle;
import com.google.android.gms.common.api.Api;

/* loaded from: classes.dex */
public class TelemetryLoggingOptions implements Api.ApiOptions {
    public static final TelemetryLoggingOptions zaa = builder().build();
    private final String zab;

    /* loaded from: classes.dex */
    public static class Builder {
        private String zaa;

        /* synthetic */ Builder(zaac zaacVar) {
        }

        public TelemetryLoggingOptions build() {
            return new TelemetryLoggingOptions(this.zaa, null);
        }

        public Builder setApi(String str) {
            this.zaa = str;
            return this;
        }
    }

    /* synthetic */ TelemetryLoggingOptions(String str, zaad zaadVar) {
        this.zab = str;
    }

    public static Builder builder() {
        return new Builder(null);
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof TelemetryLoggingOptions) {
            return Objects.equal(this.zab, ((TelemetryLoggingOptions) obj).zab);
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hashCode(this.zab);
    }

    public final Bundle zaa() {
        Bundle bundle = new Bundle();
        String str = this.zab;
        if (str != null) {
            bundle.putString("api", str);
        }
        return bundle;
    }
}
