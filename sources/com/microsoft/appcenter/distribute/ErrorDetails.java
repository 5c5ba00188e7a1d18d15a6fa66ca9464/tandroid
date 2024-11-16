package com.microsoft.appcenter.distribute;

import org.json.JSONObject;

/* loaded from: classes.dex */
class ErrorDetails {
    private String code;

    ErrorDetails() {
    }

    static ErrorDetails parse(String str) {
        JSONObject jSONObject = new JSONObject(str);
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.code = jSONObject.getString("code");
        return errorDetails;
    }

    String getCode() {
        return this.code;
    }
}
