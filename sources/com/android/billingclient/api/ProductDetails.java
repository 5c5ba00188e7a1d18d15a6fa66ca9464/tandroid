package com.android.billingclient.api;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* compiled from: com.android.billingclient:billing@@5.1.0 */
/* loaded from: classes.dex */
public final class ProductDetails {
    private final String zza;
    private final JSONObject zzb;
    private final String zzc;
    private final String zzd;
    private final String zze;
    private final String zzh;
    private final String zzi;
    private final List zzj;
    private final List zzk;

    /* compiled from: com.android.billingclient:billing@@5.1.0 */
    /* loaded from: classes.dex */
    public static final class OneTimePurchaseOfferDetails {
        private final long zzb;
        private final String zzc;
        private final String zzd;

        OneTimePurchaseOfferDetails(JSONObject jSONObject) throws JSONException {
            jSONObject.optString("formattedPrice");
            this.zzb = jSONObject.optLong("priceAmountMicros");
            this.zzc = jSONObject.optString("priceCurrencyCode");
            this.zzd = jSONObject.optString("offerIdToken");
            jSONObject.optString("offerId");
            jSONObject.optInt("offerType");
            JSONArray optJSONArray = jSONObject.optJSONArray("offerTags");
            ArrayList arrayList = new ArrayList();
            if (optJSONArray != null) {
                for (int i = 0; i < optJSONArray.length(); i++) {
                    arrayList.add(optJSONArray.getString(i));
                }
            }
            com.google.android.gms.internal.play_billing.zzu.zzk(arrayList);
        }

        public long getPriceAmountMicros() {
            return this.zzb;
        }

        public String getPriceCurrencyCode() {
            return this.zzc;
        }

        public final String zza() {
            return this.zzd;
        }
    }

    /* compiled from: com.android.billingclient:billing@@5.1.0 */
    /* loaded from: classes.dex */
    public static final class PricingPhase {
        private final String billingPeriod;
        private final String formattedPrice;
        private final long priceAmountMicros;
        private final String priceCurrencyCode;

        PricingPhase(JSONObject jSONObject) {
            this.billingPeriod = jSONObject.optString("billingPeriod");
            this.priceCurrencyCode = jSONObject.optString("priceCurrencyCode");
            this.formattedPrice = jSONObject.optString("formattedPrice");
            this.priceAmountMicros = jSONObject.optLong("priceAmountMicros");
            jSONObject.optInt("recurrenceMode");
            jSONObject.optInt("billingCycleCount");
        }

        public String getBillingPeriod() {
            return this.billingPeriod;
        }

        public String getFormattedPrice() {
            return this.formattedPrice;
        }

        public long getPriceAmountMicros() {
            return this.priceAmountMicros;
        }

        public String getPriceCurrencyCode() {
            return this.priceCurrencyCode;
        }
    }

    /* compiled from: com.android.billingclient:billing@@5.1.0 */
    /* loaded from: classes.dex */
    public static class PricingPhases {
        private final List<PricingPhase> pricingPhaseList;

        PricingPhases(JSONArray jSONArray) {
            ArrayList arrayList = new ArrayList();
            if (jSONArray != null) {
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject optJSONObject = jSONArray.optJSONObject(i);
                    if (optJSONObject != null) {
                        arrayList.add(new PricingPhase(optJSONObject));
                    }
                }
            }
            this.pricingPhaseList = arrayList;
        }

        public List<PricingPhase> getPricingPhaseList() {
            return this.pricingPhaseList;
        }
    }

    /* compiled from: com.android.billingclient:billing@@5.1.0 */
    /* loaded from: classes.dex */
    public static final class SubscriptionOfferDetails {
        private final String zzc;
        private final PricingPhases zzd;

        SubscriptionOfferDetails(JSONObject jSONObject) throws JSONException {
            jSONObject.optString("basePlanId");
            jSONObject.optString("offerId").isEmpty();
            this.zzc = jSONObject.getString("offerIdToken");
            this.zzd = new PricingPhases(jSONObject.getJSONArray("pricingPhases"));
            JSONObject optJSONObject = jSONObject.optJSONObject("installmentPlanDetails");
            if (optJSONObject != null) {
                new zzbh(optJSONObject);
            }
            ArrayList arrayList = new ArrayList();
            JSONArray optJSONArray = jSONObject.optJSONArray("offerTags");
            if (optJSONArray != null) {
                for (int i = 0; i < optJSONArray.length(); i++) {
                    arrayList.add(optJSONArray.getString(i));
                }
            }
        }

        public String getOfferToken() {
            return this.zzc;
        }

        public PricingPhases getPricingPhases() {
            return this.zzd;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ProductDetails(String str) throws JSONException {
        this.zza = str;
        JSONObject jSONObject = new JSONObject(str);
        this.zzb = jSONObject;
        String optString = jSONObject.optString("productId");
        this.zzc = optString;
        String optString2 = jSONObject.optString("type");
        this.zzd = optString2;
        if (TextUtils.isEmpty(optString)) {
            throw new IllegalArgumentException("Product id cannot be empty.");
        }
        if (TextUtils.isEmpty(optString2)) {
            throw new IllegalArgumentException("Product type cannot be empty.");
        }
        this.zze = jSONObject.optString("title");
        jSONObject.optString("name");
        jSONObject.optString("description");
        this.zzh = jSONObject.optString("skuDetailsToken");
        this.zzi = jSONObject.optString("serializedDocid");
        JSONArray optJSONArray = jSONObject.optJSONArray("subscriptionOfferDetails");
        if (optJSONArray != null) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < optJSONArray.length(); i++) {
                arrayList.add(new SubscriptionOfferDetails(optJSONArray.getJSONObject(i)));
            }
            this.zzj = arrayList;
        } else {
            this.zzj = (optString2.equals("subs") || optString2.equals("play_pass_subs")) ? new ArrayList() : null;
        }
        JSONObject optJSONObject = this.zzb.optJSONObject("oneTimePurchaseOfferDetails");
        JSONArray optJSONArray2 = this.zzb.optJSONArray("oneTimePurchaseOfferDetailsList");
        ArrayList arrayList2 = new ArrayList();
        if (optJSONArray2 != null) {
            for (int i2 = 0; i2 < optJSONArray2.length(); i2++) {
                arrayList2.add(new OneTimePurchaseOfferDetails(optJSONArray2.getJSONObject(i2)));
            }
            this.zzk = arrayList2;
        } else if (optJSONObject != null) {
            arrayList2.add(new OneTimePurchaseOfferDetails(optJSONObject));
            this.zzk = arrayList2;
        } else {
            this.zzk = null;
        }
        JSONObject optJSONObject2 = this.zzb.optJSONObject("limitedQuantityInfo");
        if (optJSONObject2 != null) {
            new zzbi(optJSONObject2);
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ProductDetails) {
            return TextUtils.equals(this.zza, ((ProductDetails) obj).zza);
        }
        return false;
    }

    public OneTimePurchaseOfferDetails getOneTimePurchaseOfferDetails() {
        List list = this.zzk;
        if (list == null || list.isEmpty()) {
            return null;
        }
        return (OneTimePurchaseOfferDetails) this.zzk.get(0);
    }

    public String getProductId() {
        return this.zzc;
    }

    public String getProductType() {
        return this.zzd;
    }

    public List<SubscriptionOfferDetails> getSubscriptionOfferDetails() {
        return this.zzj;
    }

    public int hashCode() {
        return this.zza.hashCode();
    }

    public String toString() {
        String str = this.zza;
        String obj = this.zzb.toString();
        String str2 = this.zzc;
        String str3 = this.zzd;
        String str4 = this.zze;
        String str5 = this.zzh;
        String valueOf = String.valueOf(this.zzj);
        return "ProductDetails{jsonString='" + str + "', parsedJson=" + obj + ", productId='" + str2 + "', productType='" + str3 + "', title='" + str4 + "', productDetailsToken='" + str5 + "', subscriptionOfferDetails=" + valueOf + "}";
    }

    public final String zza() {
        return this.zzb.optString("packageName");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String zzb() {
        return this.zzh;
    }

    public String zzc() {
        return this.zzi;
    }
}
