package com.microsoft.appcenter.crashes.ingestion.models;

import com.microsoft.appcenter.ingestion.models.LogWithProperties;
import java.util.UUID;
import org.json.JSONObject;
import org.json.JSONStringer;
/* loaded from: classes.dex */
public class HandledErrorLog extends LogWithProperties {
    private Exception exception;
    private UUID id;

    @Override // com.microsoft.appcenter.ingestion.models.LogWithProperties, com.microsoft.appcenter.ingestion.models.AbstractLog
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass() && super.equals(obj)) {
            HandledErrorLog handledErrorLog = (HandledErrorLog) obj;
            UUID uuid = this.id;
            if (uuid == null ? handledErrorLog.id == null : uuid.equals(handledErrorLog.id)) {
                Exception exception = this.exception;
                Exception exception2 = handledErrorLog.exception;
                return exception != null ? exception.equals(exception2) : exception2 == null;
            }
            return false;
        }
        return false;
    }

    public Exception getException() {
        return this.exception;
    }

    public UUID getId() {
        return this.id;
    }

    @Override // com.microsoft.appcenter.ingestion.models.Log
    public String getType() {
        return "handledError";
    }

    @Override // com.microsoft.appcenter.ingestion.models.LogWithProperties, com.microsoft.appcenter.ingestion.models.AbstractLog
    public int hashCode() {
        int hashCode = super.hashCode() * 31;
        UUID uuid = this.id;
        int hashCode2 = (hashCode + (uuid != null ? uuid.hashCode() : 0)) * 31;
        Exception exception = this.exception;
        return hashCode2 + (exception != null ? exception.hashCode() : 0);
    }

    @Override // com.microsoft.appcenter.ingestion.models.LogWithProperties, com.microsoft.appcenter.ingestion.models.AbstractLog, com.microsoft.appcenter.ingestion.models.Model
    public void read(JSONObject jSONObject) {
        super.read(jSONObject);
        setId(UUID.fromString(jSONObject.getString("id")));
        if (jSONObject.has("exception")) {
            JSONObject jSONObject2 = jSONObject.getJSONObject("exception");
            Exception exception = new Exception();
            exception.read(jSONObject2);
            setException(exception);
        }
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public void setId(UUID uuid) {
        this.id = uuid;
    }

    @Override // com.microsoft.appcenter.ingestion.models.LogWithProperties, com.microsoft.appcenter.ingestion.models.AbstractLog, com.microsoft.appcenter.ingestion.models.Model
    public void write(JSONStringer jSONStringer) {
        super.write(jSONStringer);
        jSONStringer.key("id").value(getId());
        if (getException() != null) {
            jSONStringer.key("exception").object();
            this.exception.write(jSONStringer);
            jSONStringer.endObject();
        }
    }
}
