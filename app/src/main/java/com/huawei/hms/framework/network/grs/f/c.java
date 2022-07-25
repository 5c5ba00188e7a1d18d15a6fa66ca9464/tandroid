package com.huawei.hms.framework.network.grs.f;

import android.content.Context;
import android.text.TextUtils;
import com.huawei.hms.framework.common.Logger;
import com.huawei.hms.framework.common.StringUtils;
import j$.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class c extends a {
    public c(Context context, boolean z) {
        this.e = z;
        if (a("grs_sdk_global_route_config.json", context) == 0) {
            this.d = true;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x006e A[Catch: JSONException -> 0x0088, LOOP:1: B:15:0x0068->B:17:0x006e, LOOP_END, TryCatch #0 {JSONException -> 0x0088, blocks: (B:3:0x0007, B:4:0x0012, B:6:0x0018, B:8:0x0043, B:10:0x0059, B:12:0x0060, B:15:0x0068, B:17:0x006e, B:19:0x007a, B:22:0x0081, B:27:0x0049, B:29:0x004f, B:30:0x0054), top: B:2:0x0007 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private List<com.huawei.hms.framework.network.grs.local.model.b> a(JSONObject jSONObject) {
        JSONArray jSONArray;
        HashSet hashSet;
        int i;
        try {
            ArrayList arrayList = new ArrayList(16);
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                com.huawei.hms.framework.network.grs.local.model.b bVar = new com.huawei.hms.framework.network.grs.local.model.b();
                bVar.b(next);
                JSONObject jSONObject2 = jSONObject.getJSONObject(next);
                bVar.c(jSONObject2.getString("name"));
                bVar.a(jSONObject2.getString("description"));
                JSONArray jSONArray2 = null;
                if (jSONObject2.has("countriesOrAreas")) {
                    jSONArray = jSONObject2.getJSONArray("countriesOrAreas");
                } else if (!jSONObject2.has("countries")) {
                    Logger.w("LocalManagerV1", "current country or area group has not config countries or areas.");
                    hashSet = new HashSet(16);
                    if (jSONArray2 != null && jSONArray2.length() != 0) {
                        for (i = 0; i < jSONArray2.length(); i++) {
                            hashSet.add((String) jSONArray2.get(i));
                        }
                        bVar.a(hashSet);
                        arrayList.add(bVar);
                    }
                    return new ArrayList();
                } else {
                    jSONArray = jSONObject2.getJSONArray("countries");
                }
                jSONArray2 = jSONArray;
                hashSet = new HashSet(16);
                if (jSONArray2 != null) {
                    while (i < jSONArray2.length()) {
                    }
                    bVar.a(hashSet);
                    arrayList.add(bVar);
                }
                return new ArrayList();
            }
            return arrayList;
        } catch (JSONException e) {
            Logger.w("LocalManagerV1", "parse countryGroups failed maybe json style is wrong. %s", StringUtils.anonymizeMessage(e.getMessage()));
            return new ArrayList();
        }
    }

    @Override // com.huawei.hms.framework.network.grs.f.a
    public int a(String str) {
        this.a = new com.huawei.hms.framework.network.grs.local.model.a();
        try {
            JSONObject jSONObject = new JSONObject(str).getJSONObject("application");
            String string = jSONObject.getString("name");
            long j = jSONObject.getLong("cacheControl");
            JSONArray jSONArray = jSONObject.getJSONArray("services");
            this.a.b(string);
            this.a.a(j);
            if (jSONArray != null) {
                if (jSONArray.length() != 0) {
                    return 0;
                }
            }
            return -1;
        } catch (JSONException e) {
            Logger.w("LocalManagerV1", "parse appbean failed maybe json style is wrong. %s", StringUtils.anonymizeMessage(e.getMessage()));
            return -1;
        }
    }

    public List<com.huawei.hms.framework.network.grs.local.model.b> a(JSONArray jSONArray, JSONObject jSONObject) {
        return (jSONObject == null || jSONObject.length() == 0) ? new ArrayList() : a(jSONObject);
    }

    @Override // com.huawei.hms.framework.network.grs.f.a
    public int b(String str) {
        this.b = new ArrayList(16);
        try {
            JSONObject jSONObject = new JSONObject(str);
            JSONObject jSONObject2 = null;
            if (jSONObject.has("countryOrAreaGroups")) {
                jSONObject2 = jSONObject.getJSONObject("countryOrAreaGroups");
            } else if (jSONObject.has("countryGroups")) {
                jSONObject2 = jSONObject.getJSONObject("countryGroups");
            } else {
                Logger.e("LocalManagerV1", "maybe local config json is wrong because the default countryOrAreaGroups isn't config.");
            }
            if (jSONObject2 == null) {
                return -1;
            }
            if (jSONObject2.length() != 0) {
                this.b.addAll(a(jSONObject2));
            }
            return 0;
        } catch (JSONException e) {
            Logger.w("LocalManagerV1", "parse countrygroup failed maybe json style is wrong. %s", StringUtils.anonymizeMessage(e.getMessage()));
            return -1;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x00a2 A[Catch: JSONException -> 0x0120, TryCatch #0 {JSONException -> 0x0120, blocks: (B:3:0x000c, B:4:0x001d, B:6:0x0023, B:9:0x0039, B:12:0x0042, B:13:0x0056, B:15:0x005c, B:17:0x006d, B:19:0x0086, B:20:0x009c, B:22:0x00a2, B:24:0x00b6, B:31:0x00bc, B:35:0x00cd, B:36:0x0072, B:38:0x0078, B:39:0x007f, B:42:0x00e1, B:44:0x00ec, B:45:0x00fb, B:46:0x0105, B:48:0x010c, B:49:0x0113, B:53:0x00f1, B:55:0x00f7, B:56:0x0100), top: B:2:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x010c A[Catch: JSONException -> 0x0120, TryCatch #0 {JSONException -> 0x0120, blocks: (B:3:0x000c, B:4:0x001d, B:6:0x0023, B:9:0x0039, B:12:0x0042, B:13:0x0056, B:15:0x005c, B:17:0x006d, B:19:0x0086, B:20:0x009c, B:22:0x00a2, B:24:0x00b6, B:31:0x00bc, B:35:0x00cd, B:36:0x0072, B:38:0x0078, B:39:0x007f, B:42:0x00e1, B:44:0x00ec, B:45:0x00fb, B:46:0x0105, B:48:0x010c, B:49:0x0113, B:53:0x00f1, B:55:0x00f7, B:56:0x0100), top: B:2:0x000c }] */
    @Override // com.huawei.hms.framework.network.grs.f.a
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int e(String str) {
        JSONObject jSONObject;
        String str2;
        String string;
        Iterator<String> keys;
        String str3 = "countryGroup";
        String str4 = "countryOrAreaGroup";
        try {
            JSONObject jSONObject2 = new JSONObject(str).getJSONObject("services");
            Iterator<String> keys2 = jSONObject2.keys();
            while (keys2.hasNext()) {
                String next = keys2.next();
                com.huawei.hms.framework.network.grs.local.model.c cVar = new com.huawei.hms.framework.network.grs.local.model.c();
                cVar.b(next);
                if (!this.g.contains(next)) {
                    this.g.add(next);
                    if (this.e) {
                        JSONObject jSONObject3 = jSONObject2.getJSONObject(next);
                        cVar.c(jSONObject3.getString("routeBy"));
                        JSONArray jSONArray = jSONObject3.getJSONArray("servings");
                        int i = 0;
                        while (i < jSONArray.length()) {
                            JSONObject jSONObject4 = (JSONObject) jSONArray.get(i);
                            com.huawei.hms.framework.network.grs.local.model.d dVar = new com.huawei.hms.framework.network.grs.local.model.d();
                            if (jSONObject4.has(str4)) {
                                string = jSONObject4.getString(str4);
                            } else if (jSONObject4.has(str3)) {
                                string = jSONObject4.getString(str3);
                            } else {
                                Logger.v("LocalManagerV1", "maybe this service routeBy is unconditional.");
                                str2 = "no-country";
                                dVar.a(str2);
                                JSONObject jSONObject5 = jSONObject4.getJSONObject("addresses");
                                String str5 = str3;
                                ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap(16);
                                keys = jSONObject5.keys();
                                while (keys.hasNext()) {
                                    Iterator<String> it = keys;
                                    String next2 = keys.next();
                                    String string2 = jSONObject5.getString(next2);
                                    if (TextUtils.isEmpty(next2) || TextUtils.isEmpty(string2)) {
                                        keys = it;
                                    } else {
                                        concurrentHashMap.put(next2, jSONObject5.getString(next2));
                                        keys = it;
                                        str4 = str4;
                                    }
                                }
                                dVar.a(concurrentHashMap);
                                cVar.a(dVar.b(), dVar);
                                i++;
                                str3 = str5;
                                str4 = str4;
                            }
                            str2 = string;
                            dVar.a(str2);
                            JSONObject jSONObject52 = jSONObject4.getJSONObject("addresses");
                            String str52 = str3;
                            ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap(16);
                            keys = jSONObject52.keys();
                            while (keys.hasNext()) {
                            }
                            dVar.a(concurrentHashMap2);
                            cVar.a(dVar.b(), dVar);
                            i++;
                            str3 = str52;
                            str4 = str4;
                        }
                        String str6 = str3;
                        String str7 = str4;
                        List<com.huawei.hms.framework.network.grs.local.model.b> list = null;
                        if (jSONObject3.has("countryOrAreaGroups")) {
                            jSONObject = jSONObject3.getJSONObject("countryOrAreaGroups");
                        } else if (jSONObject3.has("countryGroups")) {
                            jSONObject = jSONObject3.getJSONObject("countryGroups");
                        } else {
                            Logger.v("LocalManagerV1", "service use default countryOrAreaGroup");
                            cVar.a(list);
                            if (this.a == null) {
                                this.a = new com.huawei.hms.framework.network.grs.local.model.a();
                            }
                            this.a.a(next, cVar);
                            str3 = str6;
                            str4 = str7;
                        }
                        list = a((JSONArray) null, jSONObject);
                        cVar.a(list);
                        if (this.a == null) {
                        }
                        this.a.a(next, cVar);
                        str3 = str6;
                        str4 = str7;
                    }
                }
            }
            return 0;
        } catch (JSONException e) {
            Logger.w("LocalManagerV1", "parse 1.0 services failed maybe because of json style.please check! %s", StringUtils.anonymizeMessage(e.getMessage()));
            return -1;
        }
    }
}
