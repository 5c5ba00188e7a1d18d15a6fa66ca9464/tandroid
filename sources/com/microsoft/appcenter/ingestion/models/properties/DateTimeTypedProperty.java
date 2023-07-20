package com.microsoft.appcenter.ingestion.models.properties;

import com.microsoft.appcenter.ingestion.models.json.JSONDateUtils;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
/* loaded from: classes.dex */
public class DateTimeTypedProperty extends TypedProperty {
    private Date value;

    @Override // com.microsoft.appcenter.ingestion.models.properties.TypedProperty
    public String getType() {
        return "dateTime";
    }

    public Date getValue() {
        return this.value;
    }

    public void setValue(Date date) {
        this.value = date;
    }

    @Override // com.microsoft.appcenter.ingestion.models.properties.TypedProperty, com.microsoft.appcenter.ingestion.models.Model
    public void read(JSONObject jSONObject) throws JSONException {
        super.read(jSONObject);
        setValue(JSONDateUtils.toDate(jSONObject.getString("value")));
    }

    @Override // com.microsoft.appcenter.ingestion.models.properties.TypedProperty, com.microsoft.appcenter.ingestion.models.Model
    public void write(JSONStringer jSONStringer) throws JSONException {
        super.write(jSONStringer);
        jSONStringer.key("value").value(JSONDateUtils.toString(getValue()));
    }

    @Override // com.microsoft.appcenter.ingestion.models.properties.TypedProperty
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && DateTimeTypedProperty.class == obj.getClass() && super.equals(obj)) {
            Date date = this.value;
            Date date2 = ((DateTimeTypedProperty) obj).value;
            return date != null ? date.equals(date2) : date2 == null;
        }
        return false;
    }

    @Override // com.microsoft.appcenter.ingestion.models.properties.TypedProperty
    public int hashCode() {
        int hashCode = super.hashCode() * 31;
        Date date = this.value;
        return hashCode + (date != null ? date.hashCode() : 0);
    }
}
