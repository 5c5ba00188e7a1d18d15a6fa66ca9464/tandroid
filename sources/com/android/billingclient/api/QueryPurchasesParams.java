package com.android.billingclient.api;

/* loaded from: classes.dex */
public final class QueryPurchasesParams {
    private final String zza;

    /* loaded from: classes.dex */
    public static class Builder {
        private String zza;

        /* synthetic */ Builder(zzbs zzbsVar) {
        }

        public QueryPurchasesParams build() {
            if (this.zza != null) {
                return new QueryPurchasesParams(this, null);
            }
            throw new IllegalArgumentException("Product type must be set");
        }

        public Builder setProductType(String str) {
            this.zza = str;
            return this;
        }
    }

    /* synthetic */ QueryPurchasesParams(Builder builder, zzbt zzbtVar) {
        this.zza = builder.zza;
    }

    public static Builder newBuilder() {
        return new Builder(null);
    }

    public final String zza() {
        return this.zza;
    }
}
