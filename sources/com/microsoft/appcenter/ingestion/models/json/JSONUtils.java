package com.microsoft.appcenter.ingestion.models.json;

import com.microsoft.appcenter.ingestion.models.Model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;
/* loaded from: classes.dex */
public abstract class JSONUtils {
    public static List readArray(JSONObject jSONObject, String str, ModelFactory modelFactory) {
        JSONArray optJSONArray = jSONObject.optJSONArray(str);
        if (optJSONArray == null) {
            return null;
        }
        List createList = modelFactory.createList(optJSONArray.length());
        for (int i = 0; i < optJSONArray.length(); i++) {
            JSONObject jSONObject2 = optJSONArray.getJSONObject(i);
            Model create = modelFactory.create();
            create.read(jSONObject2);
            createList.add(create);
        }
        return createList;
    }

    public static Boolean readBoolean(JSONObject jSONObject, String str) {
        if (jSONObject.has(str)) {
            return Boolean.valueOf(jSONObject.getBoolean(str));
        }
        return null;
    }

    public static Integer readInteger(JSONObject jSONObject, String str) {
        if (jSONObject.has(str)) {
            return Integer.valueOf(jSONObject.getInt(str));
        }
        return null;
    }

    public static Long readLong(JSONObject jSONObject, String str) {
        if (jSONObject.has(str)) {
            return Long.valueOf(jSONObject.getLong(str));
        }
        return null;
    }

    public static Map readMap(JSONObject jSONObject, String str) {
        JSONObject optJSONObject = jSONObject.optJSONObject(str);
        if (optJSONObject == null) {
            return null;
        }
        HashMap hashMap = new HashMap(optJSONObject.length());
        Iterator<String> keys = optJSONObject.keys();
        while (keys.hasNext()) {
            String next = keys.next();
            hashMap.put(next, optJSONObject.getString(next));
        }
        return hashMap;
    }

    public static List readStringArray(JSONObject jSONObject, String str) {
        JSONArray optJSONArray = jSONObject.optJSONArray(str);
        if (optJSONArray == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(optJSONArray.length());
        for (int i = 0; i < optJSONArray.length(); i++) {
            arrayList.add(optJSONArray.getString(i));
        }
        return arrayList;
    }

    public static void write(JSONStringer jSONStringer, String str, Object obj) {
        if (obj != null) {
            jSONStringer.key(str).value(obj);
        }
    }

    public static void writeArray(JSONStringer jSONStringer, String str, List list) {
        if (list != null) {
            jSONStringer.key(str).array();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                jSONStringer.object();
                ((Model) it.next()).write(jSONStringer);
                jSONStringer.endObject();
            }
            jSONStringer.endArray();
        }
    }

    public static void writeMap(JSONStringer jSONStringer, String str, Map map) {
        if (map != null) {
            jSONStringer.key(str).object();
            for (Map.Entry entry : map.entrySet()) {
                jSONStringer.key((String) entry.getKey()).value(entry.getValue());
            }
            jSONStringer.endObject();
        }
    }

    public static void writeStringArray(JSONStringer jSONStringer, String str, List list) {
        if (list != null) {
            jSONStringer.key(str).array();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                jSONStringer.value((String) it.next());
            }
            jSONStringer.endArray();
        }
    }
}
