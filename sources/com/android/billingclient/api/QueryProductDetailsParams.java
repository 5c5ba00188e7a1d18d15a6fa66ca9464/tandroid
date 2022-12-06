package com.android.billingclient.api;

import java.util.HashSet;
import java.util.List;
/* compiled from: com.android.billingclient:billing@@5.1.0 */
/* loaded from: classes.dex */
public final class QueryProductDetailsParams {
    private final com.google.android.gms.internal.play_billing.zzu zza;

    /* compiled from: com.android.billingclient:billing@@5.1.0 */
    /* loaded from: classes.dex */
    public static class Builder {
        private com.google.android.gms.internal.play_billing.zzu zza;

        /* synthetic */ Builder(zzbl zzblVar) {
        }

        public QueryProductDetailsParams build() {
            return new QueryProductDetailsParams(this, null);
        }

        public Builder setProductList(List<Product> list) {
            if (list == null || list.isEmpty()) {
                throw new IllegalArgumentException("Product list cannot be empty.");
            }
            HashSet hashSet = new HashSet();
            for (Product product : list) {
                if (!"play_pass_subs".equals(product.zzb())) {
                    hashSet.add(product.zzb());
                }
            }
            if (hashSet.size() > 1) {
                throw new IllegalArgumentException("All products should be of the same product type.");
            }
            this.zza = com.google.android.gms.internal.play_billing.zzu.zzk(list);
            return this;
        }
    }

    /* compiled from: com.android.billingclient:billing@@5.1.0 */
    /* loaded from: classes.dex */
    public static class Product {
        private final String zza;
        private final String zzb;

        /* compiled from: com.android.billingclient:billing@@5.1.0 */
        /* loaded from: classes.dex */
        public static class Builder {
            private String zza;
            private String zzb;

            /* synthetic */ Builder(zzbm zzbmVar) {
            }

            public Product build() {
                if (!"first_party".equals(this.zzb)) {
                    if (this.zza != null) {
                        if (this.zzb != null) {
                            return new Product(this, null);
                        }
                        throw new IllegalArgumentException("Product type must be provided.");
                    }
                    throw new IllegalArgumentException("Product id must be provided.");
                }
                throw new IllegalArgumentException("Serialized doc id must be provided for first party products.");
            }

            public Builder setProductId(String str) {
                this.zza = str;
                return this;
            }

            public Builder setProductType(String str) {
                this.zzb = str;
                return this;
            }
        }

        /* synthetic */ Product(Builder builder, zzbn zzbnVar) {
            this.zza = builder.zza;
            this.zzb = builder.zzb;
        }

        public static Builder newBuilder() {
            return new Builder(null);
        }

        public final String zza() {
            return this.zza;
        }

        public final String zzb() {
            return this.zzb;
        }
    }

    /* synthetic */ QueryProductDetailsParams(Builder builder, zzbo zzboVar) {
        this.zza = builder.zza;
    }

    public static Builder newBuilder() {
        return new Builder(null);
    }

    public final com.google.android.gms.internal.play_billing.zzu zza() {
        return this.zza;
    }

    public final String zzb() {
        return ((Product) this.zza.get(0)).zzb();
    }
}
