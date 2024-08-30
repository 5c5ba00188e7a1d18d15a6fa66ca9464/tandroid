package com.stripe.android.util;

import org.json.JSONObject;
/* loaded from: classes.dex */
public abstract class StripeJsonUtils {
    public static String getString(JSONObject jSONObject, String str) {
        return nullIfNullOrEmpty(jSONObject.getString(str));
    }

    static String nullIfNullOrEmpty(String str) {
        if ("null".equals(str) || "".equals(str)) {
            return null;
        }
        return str;
    }

    public static String optString(JSONObject jSONObject, String str) {
        return nullIfNullOrEmpty(jSONObject.optString(str));
    }
}
