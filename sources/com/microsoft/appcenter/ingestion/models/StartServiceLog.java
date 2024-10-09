package com.microsoft.appcenter.ingestion.models;

import com.microsoft.appcenter.ingestion.models.json.JSONUtils;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONStringer;

/* loaded from: classes.dex */
public class StartServiceLog extends AbstractLog {
    private List services;

    @Override // com.microsoft.appcenter.ingestion.models.AbstractLog
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass() || !super.equals(obj)) {
            return false;
        }
        List list = this.services;
        List list2 = ((StartServiceLog) obj).services;
        return list != null ? list.equals(list2) : list2 == null;
    }

    public List getServices() {
        return this.services;
    }

    @Override // com.microsoft.appcenter.ingestion.models.Log
    public String getType() {
        return "startService";
    }

    @Override // com.microsoft.appcenter.ingestion.models.AbstractLog
    public int hashCode() {
        int hashCode = super.hashCode() * 31;
        List list = this.services;
        return hashCode + (list != null ? list.hashCode() : 0);
    }

    @Override // com.microsoft.appcenter.ingestion.models.AbstractLog, com.microsoft.appcenter.ingestion.models.Model
    public void read(JSONObject jSONObject) {
        super.read(jSONObject);
        setServices(JSONUtils.readStringArray(jSONObject, "services"));
    }

    public void setServices(List list) {
        this.services = list;
    }

    @Override // com.microsoft.appcenter.ingestion.models.AbstractLog, com.microsoft.appcenter.ingestion.models.Model
    public void write(JSONStringer jSONStringer) {
        super.write(jSONStringer);
        JSONUtils.writeStringArray(jSONStringer, "services", getServices());
    }
}
