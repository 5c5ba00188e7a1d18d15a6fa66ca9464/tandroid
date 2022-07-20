package com.huawei.hms.push;

import android.text.TextUtils;
import com.huawei.hms.support.log.HMSLog;
import java.nio.charset.Charset;
import org.json.JSONException;
import org.json.JSONObject;
/* compiled from: PushSelfShowMessage.java */
/* loaded from: classes.dex */
public class k {
    public int B;
    public String D;
    public int b;
    public String c;
    public String d;
    public String l;
    public String m;
    public String n;
    public String o;
    public String p;
    public String r;
    public String s;
    public String z;
    public String a = "";
    public String e = "";
    public String f = "";
    public String g = "";
    public String h = "";
    public String i = "";
    public String j = "";
    public String k = "";
    public String q = "";
    public int t = n.STYLE_DEFAULT.ordinal();
    public String u = "";
    public String v = "";
    public String w = "";
    public int x = 0;
    public int y = 0;
    public String A = "";
    public String C = "";
    public String E = "";
    public String F = "";

    public k(byte[] bArr, byte[] bArr2) {
        Charset charset = x.a;
        this.r = new String(bArr, charset);
        this.s = new String(bArr2, charset);
    }

    public final JSONObject a(JSONObject jSONObject) throws JSONException {
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put("msgContent", jSONObject);
        jSONObject2.put("group", this.a);
        jSONObject2.put("tag", this.A);
        jSONObject2.put("autoCancel", this.x);
        jSONObject2.put("visibility", this.y);
        jSONObject2.put("when", this.z);
        return jSONObject2;
    }

    public final JSONObject b(JSONObject jSONObject) throws JSONException {
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put("cmd", this.g);
        jSONObject2.put("content", this.h);
        jSONObject2.put("notifyIcon", this.i);
        jSONObject2.put("notifyTitle", this.j);
        jSONObject2.put("notifySummary", this.k);
        jSONObject2.put("param", jSONObject);
        return jSONObject2;
    }

    public final void c(JSONObject jSONObject) throws JSONException {
        if (jSONObject.has("ap")) {
            String string = jSONObject.getString("ap");
            StringBuilder sb = new StringBuilder();
            if (!TextUtils.isEmpty(string) && string.length() < 48) {
                int length = 48 - string.length();
                for (int i = 0; i < length; i++) {
                    sb.append("0");
                }
                sb.append(string);
                this.d = sb.toString();
                return;
            }
            this.d = string.substring(0, 48);
        }
    }

    public final boolean d(JSONObject jSONObject) throws JSONException {
        if (jSONObject == null) {
            return false;
        }
        if (jSONObject.has("acn")) {
            this.m = jSONObject.getString("acn");
        }
        if (jSONObject.has("intentUri")) {
            this.c = jSONObject.getString("intentUri");
        }
        if (jSONObject.has("appPackageName")) {
            this.l = jSONObject.getString("appPackageName");
            return true;
        }
        HMSLog.d("PushSelfShowLog", "appPackageName is null");
        return false;
    }

    public final boolean e(JSONObject jSONObject) throws JSONException {
        if (jSONObject.has("msgId")) {
            Object obj = jSONObject.get("msgId");
            if (obj instanceof String) {
                this.e = (String) obj;
                return true;
            } else if (!(obj instanceof Integer)) {
                return true;
            } else {
                this.e = String.valueOf(((Integer) obj).intValue());
                return true;
            }
        }
        HMSLog.i("PushSelfShowLog", "msgId == null");
        return false;
    }

    public final boolean f(JSONObject jSONObject) {
        HMSLog.d("PushSelfShowLog", "enter parseNotifyParam");
        try {
            JSONObject jSONObject2 = jSONObject.getJSONObject("notifyDetail");
            if (jSONObject2.has("style")) {
                this.t = jSONObject2.getInt("style");
            }
            this.u = jSONObject2.optString("bigTitle");
            this.v = jSONObject2.optString("bigContent");
            this.E = jSONObject2.optString("icon");
            return true;
        } catch (JSONException e) {
            HMSLog.i("PushSelfShowLog", e.toString());
            return false;
        }
    }

    public final void g(JSONObject jSONObject) {
        this.a = jSONObject.optString("group");
        HMSLog.d("PushSelfShowLog", "NOTIFY_GROUP:" + this.a);
        this.x = jSONObject.optInt("autoCancel", 1);
        HMSLog.d("PushSelfShowLog", "autoCancel: " + this.x);
        this.y = jSONObject.optInt("visibility", 0);
        this.z = jSONObject.optString("when");
        this.A = jSONObject.optString("tag");
    }

    public final boolean h(JSONObject jSONObject) {
        try {
            JSONObject jSONObject2 = jSONObject.getJSONObject("param");
            if (jSONObject2.has("autoClear")) {
                this.b = jSONObject2.getInt("autoClear");
            } else {
                this.b = 0;
            }
            if (!"app".equals(this.g) && !"cosa".equals(this.g)) {
                if ("url".equals(this.g)) {
                    k(jSONObject2);
                    return true;
                } else if (!"rp".equals(this.g)) {
                    return true;
                } else {
                    j(jSONObject2);
                    return true;
                }
            }
            d(jSONObject2);
            return true;
        } catch (Exception e) {
            HMSLog.e("PushSelfShowLog", "ParseParam error ", e);
            return false;
        }
    }

    public final boolean i(JSONObject jSONObject) throws JSONException {
        if (jSONObject.has("psContent")) {
            JSONObject jSONObject2 = jSONObject.getJSONObject("psContent");
            this.g = jSONObject2.getString("cmd");
            this.h = jSONObject2.optString("content");
            this.i = jSONObject2.optString("notifyIcon");
            this.j = jSONObject2.optString("notifyTitle");
            this.k = jSONObject2.optString("notifySummary");
            this.D = jSONObject2.optString("ticker");
            if ((!jSONObject2.has("notifyDetail") || f(jSONObject2)) && jSONObject2.has("param")) {
                return h(jSONObject2);
            }
        }
        return false;
    }

    public final boolean j(JSONObject jSONObject) throws JSONException {
        if (jSONObject == null) {
            return false;
        }
        if (jSONObject.has("appPackageName")) {
            this.l = jSONObject.getString("appPackageName");
        }
        if (jSONObject.has("rpt") && jSONObject.has("rpl")) {
            this.o = jSONObject.getString("rpl");
            this.p = jSONObject.getString("rpt");
            if (!jSONObject.has("rpct")) {
                return true;
            }
            this.q = jSONObject.getString("rpct");
            return true;
        }
        HMSLog.d("PushSelfShowLog", "rpl or rpt is null");
        return false;
    }

    public final boolean k(JSONObject jSONObject) throws JSONException {
        if (jSONObject == null) {
            return false;
        }
        if (jSONObject.has("url")) {
            this.n = jSONObject.getString("url");
            if (jSONObject.has("appPackageName")) {
                this.l = jSONObject.getString("appPackageName");
            }
            if (!jSONObject.has("rpt") || !jSONObject.has("rpl")) {
                return true;
            }
            this.o = jSONObject.getString("rpl");
            this.p = jSONObject.getString("rpt");
            if (!jSONObject.has("rpct")) {
                return true;
            }
            this.q = jSONObject.getString("rpct");
            return true;
        }
        HMSLog.d("PushSelfShowLog", "url is null");
        return false;
    }

    public String l() {
        return this.a;
    }

    public String m() {
        return this.E;
    }

    public String n() {
        return this.c;
    }

    public byte[] o() {
        try {
            return a(a(b(v()), r())).toString().getBytes(x.a);
        } catch (JSONException e) {
            HMSLog.e("PushSelfShowLog", "getMsgData failed JSONException:", e);
            return new byte[0];
        }
    }

    public String p() {
        HMSLog.d("PushSelfShowLog", "msgId =" + this.e);
        return this.e;
    }

    public String q() {
        return this.A;
    }

    public final JSONObject r() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("style", this.t);
        jSONObject.put("bigTitle", this.u);
        jSONObject.put("bigContent", this.v);
        jSONObject.put("bigPic", this.w);
        return jSONObject;
    }

    public int s() {
        return this.B;
    }

    public String t() {
        return this.k;
    }

    public String u() {
        return this.j;
    }

    public final JSONObject v() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("autoClear", this.b);
        jSONObject.put("url", this.n);
        jSONObject.put("rpl", this.o);
        jSONObject.put("rpt", this.p);
        jSONObject.put("rpct", this.q);
        jSONObject.put("appPackageName", this.l);
        jSONObject.put("acn", this.m);
        jSONObject.put("intentUri", this.c);
        return jSONObject;
    }

    public int w() {
        return this.t;
    }

    public String x() {
        return this.D;
    }

    public byte[] y() {
        return this.s.getBytes(x.a);
    }

    public boolean z() {
        try {
            if (TextUtils.isEmpty(this.r)) {
                HMSLog.d("PushSelfShowLog", "msg is null");
                return false;
            }
            JSONObject jSONObject = new JSONObject(this.r);
            g(jSONObject);
            JSONObject jSONObject2 = jSONObject.getJSONObject("msgContent");
            if (!e(jSONObject2)) {
                return false;
            }
            this.f = jSONObject2.optString("dispPkgName");
            c(jSONObject2);
            this.B = jSONObject2.optInt("notifyId", -1);
            this.C = jSONObject2.optString("data");
            this.F = jSONObject2.optString("analyticInfo");
            return i(jSONObject2);
        } catch (JSONException unused) {
            HMSLog.d("PushSelfShowLog", "parse message exception.");
            return false;
        } catch (Exception e) {
            HMSLog.d("PushSelfShowLog", e.toString());
            return false;
        }
    }

    public final JSONObject a(JSONObject jSONObject, JSONObject jSONObject2) throws JSONException {
        JSONObject jSONObject3 = new JSONObject();
        jSONObject3.put("dispPkgName", this.f);
        jSONObject3.put("msgId", this.e);
        jSONObject3.put("ap", this.d);
        jSONObject3.put("notifyId", this.B);
        jSONObject3.put("psContent", jSONObject);
        jSONObject3.put("notifyDetail", jSONObject2);
        jSONObject3.put("ticker", this.D);
        jSONObject3.put("data", this.C);
        return jSONObject3;
    }

    public String b() {
        return this.F;
    }

    public String d() {
        return this.l;
    }

    public String g() {
        return this.v;
    }

    public int e() {
        return this.x;
    }

    public int f() {
        return this.b;
    }

    public String j() {
        return this.h;
    }

    public String c() {
        return this.d;
    }

    public String k() {
        return this.f;
    }

    public String h() {
        return this.u;
    }

    public String i() {
        return this.g;
    }

    public String a() {
        return this.m;
    }

    public void a(int i) {
        this.B = i;
    }
}
