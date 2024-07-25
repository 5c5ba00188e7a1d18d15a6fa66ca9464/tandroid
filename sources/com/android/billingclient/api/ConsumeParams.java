package com.android.billingclient.api;
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
public final class ConsumeParams {
    private String zza;

    /* compiled from: com.android.billingclient:billing@@6.0.1 */
    /* loaded from: classes.dex */
    public static final class Builder {
        private String zza;

        /* synthetic */ Builder(zzau zzauVar) {
        }

        public ConsumeParams build() {
            String str = this.zza;
            if (str == null) {
                throw new IllegalArgumentException("Purchase token must be set");
            }
            ConsumeParams consumeParams = new ConsumeParams(null);
            consumeParams.zza = str;
            return consumeParams;
        }

        public Builder setPurchaseToken(String str) {
            this.zza = str;
            return this;
        }
    }

    /* synthetic */ ConsumeParams(zzav zzavVar) {
    }

    public static Builder newBuilder() {
        return new Builder(null);
    }

    public String getPurchaseToken() {
        return this.zza;
    }
}
