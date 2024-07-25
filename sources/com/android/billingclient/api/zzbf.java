package com.android.billingclient.api;

import org.json.JSONException;
import org.json.JSONObject;
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
public final class zzbf {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzbf(JSONObject jSONObject) throws JSONException {
        jSONObject.getInt("commitmentPaymentsCount");
        jSONObject.optInt("subsequentCommitmentPaymentsCount");
    }
}
