package com.android.billingclient.api;

import java.util.ArrayList;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
public final class AlternativeChoiceDetails {
    private final String zza;
    private final JSONObject zzb;

    /* compiled from: com.android.billingclient:billing@@6.0.1 */
    /* loaded from: classes.dex */
    public static class Product {
        private final String zza;
        private final String zzb;
        private final String zzc;

        /* synthetic */ Product(JSONObject jSONObject, zzc zzcVar) {
            this.zza = jSONObject.optString("productId");
            this.zzb = jSONObject.optString("productType");
            String optString = jSONObject.optString("offerToken");
            this.zzc = true == optString.isEmpty() ? null : optString;
        }

        public final boolean equals(Object obj) {
            String str;
            String offerToken;
            if (this == obj) {
                return true;
            }
            if (obj instanceof Product) {
                Product product = (Product) obj;
                return this.zza.equals(product.getId()) && this.zzb.equals(product.getType()) && ((str = this.zzc) == (offerToken = product.getOfferToken()) || (str != null && str.equals(offerToken)));
            }
            return false;
        }

        public String getId() {
            return this.zza;
        }

        public String getOfferToken() {
            return this.zzc;
        }

        public String getType() {
            return this.zzb;
        }

        public final int hashCode() {
            return Arrays.hashCode(new Object[]{this.zza, this.zzb, this.zzc});
        }

        public final String toString() {
            return String.format("{id: %s, type: %s, offer token: %s}", this.zza, this.zzb, this.zzc);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AlternativeChoiceDetails(String str) throws JSONException {
        this.zza = str;
        JSONObject jSONObject = new JSONObject(str);
        this.zzb = jSONObject;
        JSONArray optJSONArray = jSONObject.optJSONArray("products");
        ArrayList arrayList = new ArrayList();
        if (optJSONArray != null) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                if (optJSONObject != null) {
                    arrayList.add(new Product(optJSONObject, null));
                }
            }
        }
    }
}
