package com.microsoft.appcenter.ingestion.models.properties;

import org.json.JSONObject;
import org.json.JSONStringer;

/* loaded from: classes.dex */
public class LongTypedProperty extends TypedProperty {
    private long value;

    @Override // com.microsoft.appcenter.ingestion.models.properties.TypedProperty
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && super.equals(obj) && this.value == ((LongTypedProperty) obj).value;
    }

    @Override // com.microsoft.appcenter.ingestion.models.properties.TypedProperty
    public String getType() {
        return "long";
    }

    public long getValue() {
        return this.value;
    }

    @Override // com.microsoft.appcenter.ingestion.models.properties.TypedProperty
    public int hashCode() {
        int hashCode = super.hashCode() * 31;
        long j = this.value;
        return hashCode + ((int) (j ^ (j >>> 32)));
    }

    @Override // com.microsoft.appcenter.ingestion.models.properties.TypedProperty, com.microsoft.appcenter.ingestion.models.Model
    public void read(JSONObject jSONObject) {
        super.read(jSONObject);
        setValue(jSONObject.getLong("value"));
    }

    public void setValue(long j) {
        this.value = j;
    }

    @Override // com.microsoft.appcenter.ingestion.models.properties.TypedProperty, com.microsoft.appcenter.ingestion.models.Model
    public void write(JSONStringer jSONStringer) {
        super.write(jSONStringer);
        jSONStringer.key("value").value(getValue());
    }
}
