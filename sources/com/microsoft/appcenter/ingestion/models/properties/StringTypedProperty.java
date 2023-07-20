package com.microsoft.appcenter.ingestion.models.properties;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
/* loaded from: classes.dex */
public class StringTypedProperty extends TypedProperty {
    private String value;

    @Override // com.microsoft.appcenter.ingestion.models.properties.TypedProperty
    public String getType() {
        return "string";
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        this.value = str;
    }

    @Override // com.microsoft.appcenter.ingestion.models.properties.TypedProperty, com.microsoft.appcenter.ingestion.models.Model
    public void read(JSONObject jSONObject) throws JSONException {
        super.read(jSONObject);
        setValue(jSONObject.getString("value"));
    }

    @Override // com.microsoft.appcenter.ingestion.models.properties.TypedProperty, com.microsoft.appcenter.ingestion.models.Model
    public void write(JSONStringer jSONStringer) throws JSONException {
        super.write(jSONStringer);
        jSONStringer.key("value").value(getValue());
    }

    @Override // com.microsoft.appcenter.ingestion.models.properties.TypedProperty
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && StringTypedProperty.class == obj.getClass() && super.equals(obj)) {
            String str = this.value;
            String str2 = ((StringTypedProperty) obj).value;
            return str != null ? str.equals(str2) : str2 == null;
        }
        return false;
    }

    @Override // com.microsoft.appcenter.ingestion.models.properties.TypedProperty
    public int hashCode() {
        int hashCode = super.hashCode() * 31;
        String str = this.value;
        return hashCode + (str != null ? str.hashCode() : 0);
    }
}
