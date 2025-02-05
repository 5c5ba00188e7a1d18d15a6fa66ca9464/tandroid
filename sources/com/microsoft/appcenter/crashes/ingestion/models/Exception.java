package com.microsoft.appcenter.crashes.ingestion.models;

import com.microsoft.appcenter.crashes.ingestion.models.json.ExceptionFactory;
import com.microsoft.appcenter.crashes.ingestion.models.json.StackFrameFactory;
import com.microsoft.appcenter.ingestion.models.Model;
import com.microsoft.appcenter.ingestion.models.json.JSONUtils;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONStringer;

/* loaded from: classes.dex */
public class Exception implements Model {
    private List frames;
    private List innerExceptions;
    private String message;
    private String minidumpFilePath;
    private String stackTrace;
    private String type;
    private String wrapperSdkName;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Exception exception = (Exception) obj;
        String str = this.type;
        if (str == null ? exception.type != null : !str.equals(exception.type)) {
            return false;
        }
        String str2 = this.message;
        if (str2 == null ? exception.message != null : !str2.equals(exception.message)) {
            return false;
        }
        String str3 = this.stackTrace;
        if (str3 == null ? exception.stackTrace != null : !str3.equals(exception.stackTrace)) {
            return false;
        }
        List list = this.frames;
        if (list == null ? exception.frames != null : !list.equals(exception.frames)) {
            return false;
        }
        List list2 = this.innerExceptions;
        if (list2 == null ? exception.innerExceptions != null : !list2.equals(exception.innerExceptions)) {
            return false;
        }
        String str4 = this.wrapperSdkName;
        if (str4 == null ? exception.wrapperSdkName != null : !str4.equals(exception.wrapperSdkName)) {
            return false;
        }
        String str5 = this.minidumpFilePath;
        String str6 = exception.minidumpFilePath;
        return str5 != null ? str5.equals(str6) : str6 == null;
    }

    public List getFrames() {
        return this.frames;
    }

    public List getInnerExceptions() {
        return this.innerExceptions;
    }

    public String getMessage() {
        return this.message;
    }

    public String getMinidumpFilePath() {
        return this.minidumpFilePath;
    }

    public String getStackTrace() {
        return this.stackTrace;
    }

    public String getType() {
        return this.type;
    }

    public String getWrapperSdkName() {
        return this.wrapperSdkName;
    }

    public int hashCode() {
        String str = this.type;
        int hashCode = (str != null ? str.hashCode() : 0) * 31;
        String str2 = this.message;
        int hashCode2 = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
        String str3 = this.stackTrace;
        int hashCode3 = (hashCode2 + (str3 != null ? str3.hashCode() : 0)) * 31;
        List list = this.frames;
        int hashCode4 = (hashCode3 + (list != null ? list.hashCode() : 0)) * 31;
        List list2 = this.innerExceptions;
        int hashCode5 = (hashCode4 + (list2 != null ? list2.hashCode() : 0)) * 31;
        String str4 = this.wrapperSdkName;
        int hashCode6 = (hashCode5 + (str4 != null ? str4.hashCode() : 0)) * 31;
        String str5 = this.minidumpFilePath;
        return hashCode6 + (str5 != null ? str5.hashCode() : 0);
    }

    @Override // com.microsoft.appcenter.ingestion.models.Model
    public void read(JSONObject jSONObject) {
        setType(jSONObject.optString("type", null));
        setMessage(jSONObject.optString("message", null));
        setStackTrace(jSONObject.optString("stackTrace", null));
        setFrames(JSONUtils.readArray(jSONObject, "frames", StackFrameFactory.getInstance()));
        setInnerExceptions(JSONUtils.readArray(jSONObject, "innerExceptions", ExceptionFactory.getInstance()));
        setWrapperSdkName(jSONObject.optString("wrapperSdkName", null));
        setMinidumpFilePath(jSONObject.optString("minidumpFilePath", null));
    }

    public void setFrames(List list) {
        this.frames = list;
    }

    public void setInnerExceptions(List list) {
        this.innerExceptions = list;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public void setMinidumpFilePath(String str) {
        this.minidumpFilePath = str;
    }

    public void setStackTrace(String str) {
        this.stackTrace = str;
    }

    public void setType(String str) {
        this.type = str;
    }

    public void setWrapperSdkName(String str) {
        this.wrapperSdkName = str;
    }

    @Override // com.microsoft.appcenter.ingestion.models.Model
    public void write(JSONStringer jSONStringer) {
        JSONUtils.write(jSONStringer, "type", getType());
        JSONUtils.write(jSONStringer, "message", getMessage());
        JSONUtils.write(jSONStringer, "stackTrace", getStackTrace());
        JSONUtils.writeArray(jSONStringer, "frames", getFrames());
        JSONUtils.writeArray(jSONStringer, "innerExceptions", getInnerExceptions());
        JSONUtils.write(jSONStringer, "wrapperSdkName", getWrapperSdkName());
        JSONUtils.write(jSONStringer, "minidumpFilePath", getMinidumpFilePath());
    }
}
