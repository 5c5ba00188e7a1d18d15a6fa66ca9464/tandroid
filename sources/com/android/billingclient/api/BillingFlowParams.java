package com.android.billingclient.api;

import android.text.TextUtils;
import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class BillingFlowParams {
    private boolean zza;
    private String zzb;
    private String zzc;
    private SubscriptionUpdateParams zzd;
    private com.google.android.gms.internal.play_billing.zzu zze;
    private ArrayList zzf;
    private boolean zzg;

    /* loaded from: classes.dex */
    public static class Builder {
        private String zza;
        private String zzb;
        private List zzc;
        private ArrayList zzd;
        private boolean zze;
        private SubscriptionUpdateParams.Builder zzf;

        /* synthetic */ Builder(zzak zzakVar) {
            SubscriptionUpdateParams.Builder newBuilder = SubscriptionUpdateParams.newBuilder();
            SubscriptionUpdateParams.Builder.zza(newBuilder);
            this.zzf = newBuilder;
        }

        public BillingFlowParams build() {
            ArrayList arrayList = this.zzd;
            boolean z = (arrayList == null || arrayList.isEmpty()) ? false : true;
            List list = this.zzc;
            boolean z2 = (list == null || list.isEmpty()) ? false : true;
            if (!z && !z2) {
                throw new IllegalArgumentException("Details of the products must be provided.");
            }
            if (z && z2) {
                throw new IllegalArgumentException("Set SkuDetails or ProductDetailsParams, not both.");
            }
            zzap zzapVar = null;
            if (!z) {
                ProductDetailsParams productDetailsParams = (ProductDetailsParams) this.zzc.get(0);
                for (int i = 0; i < this.zzc.size(); i++) {
                    ProductDetailsParams productDetailsParams2 = (ProductDetailsParams) this.zzc.get(i);
                    if (productDetailsParams2 == null) {
                        throw new IllegalArgumentException("ProductDetailsParams cannot be null.");
                    }
                    if (i != 0 && !productDetailsParams2.zza().getProductType().equals(productDetailsParams.zza().getProductType()) && !productDetailsParams2.zza().getProductType().equals("play_pass_subs")) {
                        throw new IllegalArgumentException("All products should have same ProductType.");
                    }
                }
                String zza = productDetailsParams.zza().zza();
                for (ProductDetailsParams productDetailsParams3 : this.zzc) {
                    if (!productDetailsParams.zza().getProductType().equals("play_pass_subs") && !productDetailsParams3.zza().getProductType().equals("play_pass_subs") && !zza.equals(productDetailsParams3.zza().zza())) {
                        throw new IllegalArgumentException("All products must have the same package name.");
                    }
                }
            } else {
                if (this.zzd.contains(null)) {
                    throw new IllegalArgumentException("SKU cannot be null.");
                }
                if (this.zzd.size() > 1) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(this.zzd.get(0));
                    throw null;
                }
            }
            BillingFlowParams billingFlowParams = new BillingFlowParams(zzapVar);
            if (z) {
                ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(this.zzd.get(0));
                throw null;
            }
            billingFlowParams.zza = z2 && !((ProductDetailsParams) this.zzc.get(0)).zza().zza().isEmpty();
            billingFlowParams.zzb = this.zza;
            billingFlowParams.zzc = this.zzb;
            billingFlowParams.zzd = this.zzf.build();
            ArrayList arrayList2 = this.zzd;
            billingFlowParams.zzf = arrayList2 != null ? new ArrayList(arrayList2) : new ArrayList();
            billingFlowParams.zzg = this.zze;
            List list2 = this.zzc;
            billingFlowParams.zze = list2 != null ? com.google.android.gms.internal.play_billing.zzu.zzj(list2) : com.google.android.gms.internal.play_billing.zzu.zzk();
            return billingFlowParams;
        }

        public Builder setObfuscatedAccountId(String str) {
            this.zza = str;
            return this;
        }

        public Builder setObfuscatedProfileId(String str) {
            this.zzb = str;
            return this;
        }

        public Builder setProductDetailsParamsList(List list) {
            this.zzc = new ArrayList(list);
            return this;
        }

        public Builder setSubscriptionUpdateParams(SubscriptionUpdateParams subscriptionUpdateParams) {
            this.zzf = SubscriptionUpdateParams.zzc(subscriptionUpdateParams);
            return this;
        }
    }

    /* loaded from: classes.dex */
    public static final class ProductDetailsParams {
        private final ProductDetails zza;
        private final String zzb;

        /* loaded from: classes.dex */
        public static class Builder {
            private ProductDetails zza;
            private String zzb;

            /* synthetic */ Builder(zzal zzalVar) {
            }

            public ProductDetailsParams build() {
                com.google.android.gms.internal.play_billing.zzm.zzc(this.zza, "ProductDetails is required for constructing ProductDetailsParams.");
                com.google.android.gms.internal.play_billing.zzm.zzc(this.zzb, "offerToken is required for constructing ProductDetailsParams.");
                return new ProductDetailsParams(this, null);
            }

            public Builder setOfferToken(String str) {
                this.zzb = str;
                return this;
            }

            public Builder setProductDetails(ProductDetails productDetails) {
                this.zza = productDetails;
                if (productDetails.getOneTimePurchaseOfferDetails() != null) {
                    productDetails.getOneTimePurchaseOfferDetails().getClass();
                    this.zzb = productDetails.getOneTimePurchaseOfferDetails().zza();
                }
                return this;
            }
        }

        /* synthetic */ ProductDetailsParams(Builder builder, zzam zzamVar) {
            this.zza = builder.zza;
            this.zzb = builder.zzb;
        }

        public static Builder newBuilder() {
            return new Builder(null);
        }

        public final ProductDetails zza() {
            return this.zza;
        }

        public final String zzb() {
            return this.zzb;
        }
    }

    /* loaded from: classes.dex */
    public static class SubscriptionUpdateParams {
        private String zza;
        private String zzb;
        private int zzc = 0;
        private int zzd = 0;

        /* loaded from: classes.dex */
        public static class Builder {
            private String zza;
            private String zzb;
            private boolean zzc;
            private int zzd = 0;
            private int zze = 0;

            /* synthetic */ Builder(zzan zzanVar) {
            }

            static /* synthetic */ Builder zza(Builder builder) {
                builder.zzc = true;
                return builder;
            }

            public SubscriptionUpdateParams build() {
                zzao zzaoVar = null;
                boolean z = (TextUtils.isEmpty(this.zza) && TextUtils.isEmpty(null)) ? false : true;
                boolean isEmpty = true ^ TextUtils.isEmpty(this.zzb);
                if (z && isEmpty) {
                    throw new IllegalArgumentException("Please provide Old SKU purchase information(token/id) or original external transaction id, not both.");
                }
                if (!this.zzc && !z && !isEmpty) {
                    throw new IllegalArgumentException("Old SKU purchase information(token/id) or original external transaction id must be provided.");
                }
                SubscriptionUpdateParams subscriptionUpdateParams = new SubscriptionUpdateParams(zzaoVar);
                subscriptionUpdateParams.zza = this.zza;
                subscriptionUpdateParams.zzc = this.zzd;
                subscriptionUpdateParams.zzd = this.zze;
                subscriptionUpdateParams.zzb = this.zzb;
                return subscriptionUpdateParams;
            }

            public Builder setOldPurchaseToken(String str) {
                this.zza = str;
                return this;
            }

            public Builder setOldSkuPurchaseToken(String str) {
                this.zza = str;
                return this;
            }

            public Builder setOriginalExternalTransactionId(String str) {
                this.zzb = str;
                return this;
            }

            public Builder setReplaceSkusProrationMode(int i) {
                this.zzd = i;
                return this;
            }

            public Builder setSubscriptionReplacementMode(int i) {
                this.zze = i;
                return this;
            }
        }

        /* synthetic */ SubscriptionUpdateParams(zzao zzaoVar) {
        }

        public static Builder newBuilder() {
            return new Builder(null);
        }

        static /* bridge */ /* synthetic */ Builder zzc(SubscriptionUpdateParams subscriptionUpdateParams) {
            Builder newBuilder = newBuilder();
            newBuilder.setOldSkuPurchaseToken(subscriptionUpdateParams.zza);
            newBuilder.setReplaceSkusProrationMode(subscriptionUpdateParams.zzc);
            newBuilder.setSubscriptionReplacementMode(subscriptionUpdateParams.zzd);
            newBuilder.setOriginalExternalTransactionId(subscriptionUpdateParams.zzb);
            return newBuilder;
        }

        final int zza() {
            return this.zzc;
        }

        final int zzb() {
            return this.zzd;
        }

        final String zzd() {
            return this.zza;
        }

        final String zze() {
            return this.zzb;
        }
    }

    /* synthetic */ BillingFlowParams(zzap zzapVar) {
    }

    public static Builder newBuilder() {
        return new Builder(null);
    }

    public final int zza() {
        return this.zzd.zza();
    }

    public final int zzb() {
        return this.zzd.zzb();
    }

    public final String zzc() {
        return this.zzb;
    }

    public final String zzd() {
        return this.zzc;
    }

    public final String zze() {
        return this.zzd.zzd();
    }

    public final String zzf() {
        return this.zzd.zze();
    }

    public final ArrayList zzg() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.zzf);
        return arrayList;
    }

    public final List zzh() {
        return this.zze;
    }

    public final boolean zzp() {
        return this.zzg;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean zzq() {
        return (this.zzb == null && this.zzc == null && this.zzd.zze() == null && this.zzd.zza() == 0 && this.zzd.zzb() == 0 && !this.zza && !this.zzg) ? false : true;
    }
}
