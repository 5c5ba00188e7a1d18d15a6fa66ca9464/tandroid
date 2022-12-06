package com.android.billingclient.api;

import org.json.JSONException;
import org.json.JSONObject;
/* compiled from: com.android.billingclient:billing@@5.1.0 */
/* loaded from: classes.dex */
public final class zzbh {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzbh(JSONObject jSONObject) throws JSONException {
        jSONObject.getInt("commitmentPaymentsCount");
        jSONObject.optInt("subsequentCommitmentPaymentsCount");
    }
}
