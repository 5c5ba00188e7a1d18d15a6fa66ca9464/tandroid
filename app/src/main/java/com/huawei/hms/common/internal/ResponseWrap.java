package com.huawei.hms.common.internal;

import android.text.TextUtils;
import com.huawei.hms.support.log.HMSLog;
import com.huawei.hms.utils.JsonUtil;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class ResponseWrap {
    private static final String TAG = "ResponseWrap";
    private String body;
    private ResponseHeader responseHeader;

    public ResponseWrap(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public boolean fromJson(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            this.responseHeader.setStatusCode(JsonUtil.getIntValue(jSONObject, "status_code"));
            this.responseHeader.setErrorCode(JsonUtil.getIntValue(jSONObject, "error_code"));
            this.responseHeader.setErrorReason(JsonUtil.getStringValue(jSONObject, "error_reason"));
            this.responseHeader.setSrvName(JsonUtil.getStringValue(jSONObject, "srv_name"));
            this.responseHeader.setApiName(JsonUtil.getStringValue(jSONObject, "api_name"));
            this.responseHeader.setAppID(JsonUtil.getStringValue(jSONObject, "app_id"));
            this.responseHeader.setPkgName(JsonUtil.getStringValue(jSONObject, "pkg_name"));
            this.responseHeader.setSessionId(JsonUtil.getStringValue(jSONObject, "session_id"));
            this.responseHeader.setTransactionId(JsonUtil.getStringValue(jSONObject, "transaction_id"));
            this.responseHeader.setResolution(JsonUtil.getStringValue(jSONObject, "resolution"));
            this.body = JsonUtil.getStringValue(jSONObject, "body");
            return true;
        } catch (JSONException e) {
            HMSLog.e("ResponseWrap", "fromJson failed: " + e.getMessage());
            return false;
        }
    }

    public String getBody() {
        if (TextUtils.isEmpty(this.body)) {
            this.body = new JSONObject().toString();
        }
        return this.body;
    }

    public ResponseHeader getResponseHeader() {
        return this.responseHeader;
    }

    public void setBody(String str) {
        this.body = str;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public String toJson() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("status_code", this.responseHeader.getStatusCode());
            jSONObject.put("error_code", this.responseHeader.getErrorCode());
            jSONObject.put("error_reason", this.responseHeader.getErrorReason());
            jSONObject.put("srv_name", this.responseHeader.getSrvName());
            jSONObject.put("api_name", this.responseHeader.getApiName());
            jSONObject.put("app_id", this.responseHeader.getAppID());
            jSONObject.put("pkg_name", this.responseHeader.getPkgName());
            jSONObject.put("transaction_id", this.responseHeader.getTransactionId());
            jSONObject.put("resolution", this.responseHeader.getResolution());
            String sessionId = this.responseHeader.getSessionId();
            if (!TextUtils.isEmpty(sessionId)) {
                jSONObject.put("session_id", sessionId);
            }
            if (!TextUtils.isEmpty(this.body)) {
                jSONObject.put("body", this.body);
            }
        } catch (JSONException e) {
            HMSLog.e("ResponseWrap", "toJson failed: " + e.getMessage());
        }
        return jSONObject.toString();
    }

    public String toString() {
        return "ResponseWrap{body='" + this.body + "', responseHeader=" + this.responseHeader + '}';
    }
}
