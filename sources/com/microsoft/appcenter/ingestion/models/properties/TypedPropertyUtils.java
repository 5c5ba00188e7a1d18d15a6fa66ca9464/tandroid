package com.microsoft.appcenter.ingestion.models.properties;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public abstract class TypedPropertyUtils {
    public static TypedProperty create(String str) {
        if ("boolean".equals(str)) {
            return new BooleanTypedProperty();
        }
        if ("dateTime".equals(str)) {
            return new DateTimeTypedProperty();
        }
        if ("double".equals(str)) {
            return new DoubleTypedProperty();
        }
        if ("long".equals(str)) {
            return new LongTypedProperty();
        }
        if ("string".equals(str)) {
            return new StringTypedProperty();
        }
        throw new JSONException("Unsupported type: " + str);
    }

    public static List read(JSONObject jSONObject) {
        JSONArray optJSONArray = jSONObject.optJSONArray("typedProperties");
        if (optJSONArray != null) {
            ArrayList arrayList = new ArrayList(optJSONArray.length());
            for (int i = 0; i < optJSONArray.length(); i++) {
                JSONObject jSONObject2 = optJSONArray.getJSONObject(i);
                TypedProperty create = create(jSONObject2.getString("type"));
                create.read(jSONObject2);
                arrayList.add(create);
            }
            return arrayList;
        }
        return null;
    }
}
