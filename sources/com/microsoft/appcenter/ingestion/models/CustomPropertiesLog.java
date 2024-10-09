package com.microsoft.appcenter.ingestion.models;

import com.microsoft.appcenter.ingestion.models.json.JSONDateUtils;
import com.microsoft.appcenter.ingestion.models.json.JSONUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

/* loaded from: classes.dex */
public class CustomPropertiesLog extends AbstractLog {
    private Map properties;

    private static Map readProperties(JSONObject jSONObject) {
        JSONArray jSONArray = jSONObject.getJSONArray("properties");
        HashMap hashMap = new HashMap();
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONObject jSONObject2 = jSONArray.getJSONObject(i);
            hashMap.put(jSONObject2.getString("name"), readPropertyValue(jSONObject2));
        }
        return hashMap;
    }

    private static Object readPropertyValue(JSONObject jSONObject) {
        String string = jSONObject.getString("type");
        if (string.equals("clear")) {
            return null;
        }
        if (string.equals("boolean")) {
            return Boolean.valueOf(jSONObject.getBoolean("value"));
        }
        if (string.equals("number")) {
            Object obj = jSONObject.get("value");
            if (obj instanceof Number) {
                return obj;
            }
            throw new JSONException("Invalid value type");
        }
        if (string.equals("dateTime")) {
            return JSONDateUtils.toDate(jSONObject.getString("value"));
        }
        if (string.equals("string")) {
            return jSONObject.getString("value");
        }
        throw new JSONException("Invalid value type");
    }

    private static void writeProperties(JSONStringer jSONStringer, Map map) {
        if (map == null) {
            throw new JSONException("Properties cannot be null");
        }
        jSONStringer.key("properties").array();
        for (Map.Entry entry : map.entrySet()) {
            jSONStringer.object();
            JSONUtils.write(jSONStringer, "name", entry.getKey());
            writePropertyValue(jSONStringer, entry.getValue());
            jSONStringer.endObject();
        }
        jSONStringer.endArray();
    }

    private static void writePropertyValue(JSONStringer jSONStringer, Object obj) {
        String str;
        if (obj == null) {
            JSONUtils.write(jSONStringer, "type", "clear");
            return;
        }
        if (obj instanceof Boolean) {
            str = "boolean";
        } else if (obj instanceof Number) {
            str = "number";
        } else if (obj instanceof Date) {
            JSONUtils.write(jSONStringer, "type", "dateTime");
            obj = JSONDateUtils.toString((Date) obj);
            JSONUtils.write(jSONStringer, "value", obj);
        } else {
            if (!(obj instanceof String)) {
                throw new JSONException("Invalid value type");
            }
            str = "string";
        }
        JSONUtils.write(jSONStringer, "type", str);
        JSONUtils.write(jSONStringer, "value", obj);
    }

    @Override // com.microsoft.appcenter.ingestion.models.AbstractLog
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass() || !super.equals(obj)) {
            return false;
        }
        Map map = this.properties;
        Map map2 = ((CustomPropertiesLog) obj).properties;
        return map != null ? map.equals(map2) : map2 == null;
    }

    public Map getProperties() {
        return this.properties;
    }

    @Override // com.microsoft.appcenter.ingestion.models.Log
    public String getType() {
        return "customProperties";
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
        setProperties(readProperties(jSONObject));
    }

    public void setProperties(Map map) {
        this.properties = map;
    }

    @Override // com.microsoft.appcenter.ingestion.models.AbstractLog, com.microsoft.appcenter.ingestion.models.Model
    public void write(JSONStringer jSONStringer) {
        super.write(jSONStringer);
        writeProperties(jSONStringer, getProperties());
    }
}
