package com.microsoft.appcenter.crashes.ingestion.models;

import com.microsoft.appcenter.ingestion.models.Model;
import com.microsoft.appcenter.ingestion.models.json.JSONUtils;
import org.json.JSONObject;
import org.json.JSONStringer;

/* loaded from: classes.dex */
public class StackFrame implements Model {
    private String className;
    private String fileName;
    private Integer lineNumber;
    private String methodName;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        StackFrame stackFrame = (StackFrame) obj;
        String str = this.className;
        if (str == null ? stackFrame.className != null : !str.equals(stackFrame.className)) {
            return false;
        }
        String str2 = this.methodName;
        if (str2 == null ? stackFrame.methodName != null : !str2.equals(stackFrame.methodName)) {
            return false;
        }
        Integer num = this.lineNumber;
        if (num == null ? stackFrame.lineNumber != null : !num.equals(stackFrame.lineNumber)) {
            return false;
        }
        String str3 = this.fileName;
        String str4 = stackFrame.fileName;
        return str3 != null ? str3.equals(str4) : str4 == null;
    }

    public String getClassName() {
        return this.className;
    }

    public String getFileName() {
        return this.fileName;
    }

    public Integer getLineNumber() {
        return this.lineNumber;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public int hashCode() {
        String str = this.className;
        int hashCode = (str != null ? str.hashCode() : 0) * 31;
        String str2 = this.methodName;
        int hashCode2 = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
        Integer num = this.lineNumber;
        int hashCode3 = (hashCode2 + (num != null ? num.hashCode() : 0)) * 31;
        String str3 = this.fileName;
        return hashCode3 + (str3 != null ? str3.hashCode() : 0);
    }

    @Override // com.microsoft.appcenter.ingestion.models.Model
    public void read(JSONObject jSONObject) {
        setClassName(jSONObject.optString("className", null));
        setMethodName(jSONObject.optString("methodName", null));
        setLineNumber(JSONUtils.readInteger(jSONObject, "lineNumber"));
        setFileName(jSONObject.optString("fileName", null));
    }

    public void setClassName(String str) {
        this.className = str;
    }

    public void setFileName(String str) {
        this.fileName = str;
    }

    public void setLineNumber(Integer num) {
        this.lineNumber = num;
    }

    public void setMethodName(String str) {
        this.methodName = str;
    }

    @Override // com.microsoft.appcenter.ingestion.models.Model
    public void write(JSONStringer jSONStringer) {
        JSONUtils.write(jSONStringer, "className", getClassName());
        JSONUtils.write(jSONStringer, "methodName", getMethodName());
        JSONUtils.write(jSONStringer, "lineNumber", getLineNumber());
        JSONUtils.write(jSONStringer, "fileName", getFileName());
    }
}
