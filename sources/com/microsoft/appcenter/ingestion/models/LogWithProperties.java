package com.microsoft.appcenter.ingestion.models;

import com.microsoft.appcenter.ingestion.models.json.JSONUtils;
import java.util.Map;
import org.json.JSONObject;
import org.json.JSONStringer;
/* loaded from: classes.dex */
public abstract class LogWithProperties extends AbstractLog {
    private Map properties;

    @Override // com.microsoft.appcenter.ingestion.models.AbstractLog
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass() && super.equals(obj)) {
            Map map = this.properties;
            Map map2 = ((LogWithProperties) obj).properties;
            return map != null ? map.equals(map2) : map2 == null;
        }
        return false;
    }

    public Map getProperties() {
        return this.properties;
    }

    @Override // com.microsoft.appcenter.ingestion.models.AbstractLog
    public int hashCode() {
        int hashCode = super.hashCode() * 31;
        Map map = this.properties;
        return hashCode + (map != null ? map.hashCode() : 0);
    }

    @Override // com.microsoft.appcenter.ingestion.models.AbstractLog, com.microsoft.appcenter.ingestion.models.Model
    public void read(JSONObject jSONObject) {
        super.read(jSONObject);
        setProperties(JSONUtils.readMap(jSONObject, "properties"));
    }

    public void setProperties(Map map) {
        this.properties = map;
    }

    @Override // com.microsoft.appcenter.ingestion.models.AbstractLog, com.microsoft.appcenter.ingestion.models.Model
    public void write(JSONStringer jSONStringer) {
        super.write(jSONStringer);
        JSONUtils.writeMap(jSONStringer, "properties", getProperties());
    }
}
