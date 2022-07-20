package com.huawei.hms.push;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.huawei.hms.push.utils.DateUtil;
import com.huawei.hms.push.utils.JsonUtil;
import com.huawei.hms.support.api.push.PushException;
import com.huawei.hms.support.log.HMSLog;
import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class RemoteMessage implements Parcelable {
    public static final Parcelable.Creator<RemoteMessage> CREATOR = new b();
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_NORMAL = 2;
    public static final int PRIORITY_UNKNOWN = 0;
    public static final String[] a;
    public static final int[] b;
    public static final long[] c;
    public static final HashMap<String, Object> d;
    public static final HashMap<String, Object> e;
    public static final HashMap<String, Object> f;
    public static final HashMap<String, Object> g;
    public static final HashMap<String, Object> h;
    public Bundle i;
    public Notification j;

    /* loaded from: classes.dex */
    public static class Builder {
        public final Bundle a;
        public final Map<String, String> b = new HashMap();

        public Builder(String str) {
            Bundle bundle = new Bundle();
            this.a = bundle;
            bundle.putString("to", str);
        }

        public Builder addData(String str, String str2) {
            if (str != null) {
                this.b.put(str, str2);
                return this;
            }
            throw new IllegalArgumentException("add data failed, key is null.");
        }

        public RemoteMessage build() {
            Bundle bundle = new Bundle();
            JSONObject jSONObject = new JSONObject();
            try {
                for (Map.Entry<String, String> entry : this.b.entrySet()) {
                    jSONObject.put(entry.getKey(), entry.getValue());
                }
                try {
                    String jSONObject2 = jSONObject.toString();
                    JSONObject jSONObject3 = new JSONObject();
                    jSONObject3.put("collapseKey", this.a.getString("collapseKey"));
                    jSONObject3.put("ttl", this.a.getInt("ttl"));
                    jSONObject3.put("sendMode", this.a.getInt("sendMode"));
                    jSONObject3.put("receiptMode", this.a.getInt("receiptMode"));
                    JSONObject jSONObject4 = new JSONObject();
                    if (jSONObject.length() != 0) {
                        jSONObject4.put("data", jSONObject2);
                    }
                    jSONObject4.put("msgId", this.a.getString("msgId"));
                    jSONObject3.put("msgContent", jSONObject4);
                    bundle.putByteArray("message_body", jSONObject3.toString().getBytes(x.a));
                    bundle.putString("to", this.a.getString("to"));
                    bundle.putString("message_type", this.a.getString("message_type"));
                    return new RemoteMessage(bundle);
                } catch (JSONException unused) {
                    HMSLog.w("RemoteMessage", "JSONException: parse message body failed.");
                    throw new PushException("send message failed");
                }
            } catch (JSONException unused2) {
                HMSLog.w("RemoteMessage", "JSONException: parse data to json failed.");
                throw new PushException("send message failed");
            }
        }

        public Builder clearData() {
            this.b.clear();
            return this;
        }

        public Builder setCollapseKey(String str) {
            this.a.putString("collapseKey", str);
            return this;
        }

        public Builder setData(Map<String, String> map) {
            this.b.clear();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                this.b.put(entry.getKey(), entry.getValue());
            }
            return this;
        }

        public Builder setMessageId(String str) {
            this.a.putString("msgId", str);
            return this;
        }

        public Builder setMessageType(String str) {
            this.a.putString("message_type", str);
            return this;
        }

        public Builder setReceiptMode(int i) {
            if (i != 1 && i != 0) {
                throw new IllegalArgumentException("receipt mode can only be 0 or 1.");
            }
            this.a.putInt("receiptMode", i);
            return this;
        }

        public Builder setSendMode(int i) {
            if (i != 0 && i != 1) {
                throw new IllegalArgumentException("send mode can only be 0 or 1.");
            }
            this.a.putInt("sendMode", i);
            return this;
        }

        public Builder setTtl(int i) {
            if (i >= 1 && i <= 1296000) {
                this.a.putInt("ttl", i);
                return this;
            }
            throw new IllegalArgumentException("ttl must be greater than or equal to 1 and less than or equal to 1296000");
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface MessagePriority {
    }

    /* loaded from: classes.dex */
    public static class Notification implements Serializable {
        public final long[] A;
        public final String B;
        public final String a;
        public final String b;
        public final String[] c;
        public final String d;
        public final String e;
        public final String[] f;
        public final String g;
        public final String h;
        public final String i;
        public final String j;
        public final String k;
        public final String l;
        public final String m;
        public final Uri n;
        public final int o;
        public final String p;
        public final int q;
        public final int r;
        public final int s;
        public final int[] t;
        public final String u;
        public final int v;
        public final String w;
        public final int x;
        public final String y;
        public final String z;

        public /* synthetic */ Notification(Bundle bundle, b bVar) {
            this(bundle);
        }

        public final Integer a(String str) {
            if (str != null) {
                try {
                    return Integer.valueOf(str);
                } catch (NumberFormatException unused) {
                    HMSLog.w("RemoteMessage", "NumberFormatException: get " + str + " failed.");
                }
            }
            return null;
        }

        public Integer getBadgeNumber() {
            return a(this.w);
        }

        public String getBody() {
            return this.d;
        }

        public String[] getBodyLocalizationArgs() {
            String[] strArr = this.f;
            return strArr == null ? new String[0] : (String[]) strArr.clone();
        }

        public String getBodyLocalizationKey() {
            return this.e;
        }

        public String getChannelId() {
            return this.m;
        }

        public String getClickAction() {
            return this.k;
        }

        public String getColor() {
            return this.j;
        }

        public String getIcon() {
            return this.g;
        }

        public Uri getImageUrl() {
            String str = this.p;
            if (str == null) {
                return null;
            }
            return Uri.parse(str);
        }

        public Integer getImportance() {
            return a(this.y);
        }

        public String getIntentUri() {
            return this.l;
        }

        public int[] getLightSettings() {
            int[] iArr = this.t;
            return iArr == null ? new int[0] : (int[]) iArr.clone();
        }

        public Uri getLink() {
            return this.n;
        }

        public int getNotifyId() {
            return this.o;
        }

        public String getSound() {
            return this.h;
        }

        public String getTag() {
            return this.i;
        }

        public String getTicker() {
            return this.z;
        }

        public String getTitle() {
            return this.a;
        }

        public String[] getTitleLocalizationArgs() {
            String[] strArr = this.c;
            return strArr == null ? new String[0] : (String[]) strArr.clone();
        }

        public String getTitleLocalizationKey() {
            return this.b;
        }

        public long[] getVibrateConfig() {
            long[] jArr = this.A;
            return jArr == null ? new long[0] : (long[]) jArr.clone();
        }

        public Integer getVisibility() {
            return a(this.B);
        }

        public Long getWhen() {
            if (!TextUtils.isEmpty(this.u)) {
                try {
                    return Long.valueOf(DateUtil.parseUtcToMillisecond(this.u));
                } catch (StringIndexOutOfBoundsException unused) {
                    HMSLog.w("RemoteMessage", "StringIndexOutOfBoundsException: parse when failed.");
                } catch (ParseException unused2) {
                    HMSLog.w("RemoteMessage", "ParseException: parse when failed.");
                }
            }
            return null;
        }

        public boolean isAutoCancel() {
            return this.x == 1;
        }

        public boolean isDefaultLight() {
            return this.q == 1;
        }

        public boolean isDefaultSound() {
            return this.r == 1;
        }

        public boolean isDefaultVibrate() {
            return this.s == 1;
        }

        public boolean isLocalOnly() {
            return this.v == 1;
        }

        public Notification(Bundle bundle) {
            this.a = bundle.getString("notifyTitle");
            this.d = bundle.getString("content");
            this.b = bundle.getString("title_loc_key");
            this.e = bundle.getString("body_loc_key");
            this.c = bundle.getStringArray("title_loc_args");
            this.f = bundle.getStringArray("body_loc_args");
            this.g = bundle.getString("icon");
            this.j = bundle.getString("color");
            this.h = bundle.getString("sound");
            this.i = bundle.getString("tag");
            this.m = bundle.getString("channelId");
            this.k = bundle.getString("acn");
            this.l = bundle.getString("intentUri");
            this.o = bundle.getInt("notifyId");
            String string = bundle.getString("url");
            this.n = !TextUtils.isEmpty(string) ? Uri.parse(string) : null;
            this.p = bundle.getString("notifyIcon");
            this.q = bundle.getInt("defaultLightSettings");
            this.r = bundle.getInt("defaultSound");
            this.s = bundle.getInt("defaultVibrateTimings");
            this.t = bundle.getIntArray("lightSettings");
            this.u = bundle.getString("when");
            this.v = bundle.getInt("localOnly");
            this.w = bundle.getString("badgeSetNum", null);
            this.x = bundle.getInt("autoCancel");
            this.y = bundle.getString("priority", null);
            this.z = bundle.getString("ticker");
            this.A = bundle.getLongArray("vibrateTimings");
            this.B = bundle.getString("visibility", null);
        }
    }

    public RemoteMessage(Bundle bundle) {
        this.i = a(bundle);
    }

    public static JSONObject b(Bundle bundle) {
        try {
            return new JSONObject(w.a(bundle.getByteArray("message_body")));
        } catch (JSONException unused) {
            HMSLog.w("RemoteMessage", "JSONException:parse message body failed.");
            return null;
        }
    }

    public static JSONObject c(JSONObject jSONObject) {
        if (jSONObject != null) {
            return jSONObject.optJSONObject("param");
        }
        return null;
    }

    public static JSONObject d(JSONObject jSONObject) {
        if (jSONObject != null) {
            return jSONObject.optJSONObject("psContent");
        }
        return null;
    }

    public final Bundle a(Bundle bundle) {
        Bundle bundle2 = new Bundle();
        JSONObject b2 = b(bundle);
        JSONObject a2 = a(b2);
        String string = JsonUtil.getString(a2, "data", null);
        bundle2.putString("analyticInfo", JsonUtil.getString(a2, "analyticInfo", null));
        bundle2.putString("device_token", bundle.getString("device_token"));
        JSONObject d2 = d(a2);
        JSONObject b3 = b(d2);
        JSONObject c2 = c(d2);
        if (bundle.getInt("inputType") == 1 && s.a(a2, d2, string)) {
            bundle2.putString("data", w.a(bundle.getByteArray("message_body")));
            return bundle2;
        }
        String string2 = bundle.getString("to");
        String string3 = bundle.getString("message_type");
        String string4 = JsonUtil.getString(a2, "msgId", null);
        bundle2.putString("to", string2);
        bundle2.putString("data", string);
        bundle2.putString("msgId", string4);
        bundle2.putString("message_type", string3);
        JsonUtil.transferJsonObjectToBundle(b2, bundle2, d);
        bundle2.putBundle("notification", a(b2, a2, d2, b3, c2));
        return bundle2;
    }

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    public String getAnalyticInfo() {
        return this.i.getString("analyticInfo");
    }

    public Map<String, String> getAnalyticInfoMap() {
        HashMap hashMap = new HashMap();
        String string = this.i.getString("analyticInfo");
        if (string != null && !string.trim().isEmpty()) {
            try {
                JSONObject jSONObject = new JSONObject(string);
                Iterator<String> keys = jSONObject.keys();
                while (keys.hasNext()) {
                    String valueOf = String.valueOf(keys.next());
                    hashMap.put(valueOf, String.valueOf(jSONObject.get(valueOf)));
                }
            } catch (JSONException unused) {
                HMSLog.w("RemoteMessage", "JSONException: get analyticInfo from map failed.");
            }
        }
        return hashMap;
    }

    public String getCollapseKey() {
        return this.i.getString("collapseKey");
    }

    public String getData() {
        return this.i.getString("data");
    }

    public Map<String, String> getDataOfMap() {
        HashMap hashMap = new HashMap();
        String string = this.i.getString("data");
        if (string != null && !string.trim().isEmpty()) {
            try {
                JSONObject jSONObject = new JSONObject(string);
                Iterator<String> keys = jSONObject.keys();
                while (keys.hasNext()) {
                    String valueOf = String.valueOf(keys.next());
                    hashMap.put(valueOf, String.valueOf(jSONObject.get(valueOf)));
                }
            } catch (JSONException unused) {
                HMSLog.w("RemoteMessage", "JSONException: get data from map failed");
            }
        }
        return hashMap;
    }

    public String getFrom() {
        return this.i.getString("from");
    }

    public String getMessageId() {
        return this.i.getString("msgId");
    }

    public String getMessageType() {
        return this.i.getString("message_type");
    }

    public Notification getNotification() {
        Bundle bundle = this.i.getBundle("notification");
        if (this.j == null && bundle != null) {
            this.j = new Notification(bundle, null);
        }
        if (this.j == null) {
            this.j = new Notification(new Bundle(), null);
        }
        return this.j;
    }

    public int getOriginalUrgency() {
        int i = this.i.getInt("oriUrgency");
        if (i == 1 || i == 2) {
            return i;
        }
        return 0;
    }

    public int getReceiptMode() {
        return this.i.getInt("receiptMode");
    }

    public int getSendMode() {
        return this.i.getInt("sendMode");
    }

    public long getSentTime() {
        try {
            String string = this.i.getString("sendTime");
            if (TextUtils.isEmpty(string)) {
                return 0L;
            }
            return Long.parseLong(string);
        } catch (NumberFormatException unused) {
            HMSLog.w("RemoteMessage", "NumberFormatException: get sendTime error.");
            return 0L;
        }
    }

    public String getTo() {
        return this.i.getString("to");
    }

    public String getToken() {
        return this.i.getString("device_token");
    }

    public int getTtl() {
        return this.i.getInt("ttl");
    }

    public int getUrgency() {
        int i = this.i.getInt("urgency");
        if (i == 1 || i == 2) {
            return i;
        }
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.i);
        parcel.writeSerializable(this.j);
    }

    public RemoteMessage(Parcel parcel) {
        this.i = parcel.readBundle();
        this.j = (Notification) parcel.readSerializable();
    }

    public static JSONObject b(JSONObject jSONObject) {
        if (jSONObject != null) {
            return jSONObject.optJSONObject("notifyDetail");
        }
        return null;
    }

    static {
        String[] strArr = new String[0];
        a = strArr;
        int[] iArr = new int[0];
        b = iArr;
        long[] jArr = new long[0];
        c = jArr;
        HashMap<String, Object> hashMap = new HashMap<>(8);
        d = hashMap;
        hashMap.put("from", "");
        hashMap.put("collapseKey", "");
        hashMap.put("sendTime", "");
        hashMap.put("ttl", 86400);
        hashMap.put("urgency", 2);
        hashMap.put("oriUrgency", 2);
        hashMap.put("sendMode", 0);
        hashMap.put("receiptMode", 0);
        HashMap<String, Object> hashMap2 = new HashMap<>(8);
        e = hashMap2;
        hashMap2.put("title_loc_key", "");
        hashMap2.put("body_loc_key", "");
        hashMap2.put("notifyIcon", "");
        hashMap2.put("title_loc_args", strArr);
        hashMap2.put("body_loc_args", strArr);
        hashMap2.put("ticker", "");
        hashMap2.put("notifyTitle", "");
        hashMap2.put("content", "");
        HashMap<String, Object> hashMap3 = new HashMap<>(8);
        f = hashMap3;
        hashMap3.put("icon", "");
        hashMap3.put("color", "");
        hashMap3.put("sound", "");
        hashMap3.put("defaultLightSettings", 1);
        hashMap3.put("lightSettings", iArr);
        hashMap3.put("defaultSound", 1);
        hashMap3.put("defaultVibrateTimings", 1);
        hashMap3.put("vibrateTimings", jArr);
        HashMap<String, Object> hashMap4 = new HashMap<>(8);
        g = hashMap4;
        hashMap4.put("tag", "");
        hashMap4.put("when", "");
        hashMap4.put("localOnly", 1);
        hashMap4.put("badgeSetNum", "");
        hashMap4.put("priority", "");
        hashMap4.put("autoCancel", 1);
        hashMap4.put("visibility", "");
        hashMap4.put("channelId", "");
        HashMap<String, Object> hashMap5 = new HashMap<>(3);
        h = hashMap5;
        hashMap5.put("acn", "");
        hashMap5.put("intentUri", "");
        hashMap5.put("url", "");
    }

    public final Bundle a(JSONObject jSONObject, JSONObject jSONObject2, JSONObject jSONObject3, JSONObject jSONObject4, JSONObject jSONObject5) {
        Bundle bundle = new Bundle();
        JsonUtil.transferJsonObjectToBundle(jSONObject3, bundle, e);
        JsonUtil.transferJsonObjectToBundle(jSONObject4, bundle, f);
        JsonUtil.transferJsonObjectToBundle(jSONObject, bundle, g);
        JsonUtil.transferJsonObjectToBundle(jSONObject5, bundle, h);
        bundle.putInt("notifyId", JsonUtil.getInt(jSONObject2, "notifyId", 0));
        return bundle;
    }

    public static JSONObject a(JSONObject jSONObject) {
        if (jSONObject != null) {
            return jSONObject.optJSONObject("msgContent");
        }
        return null;
    }
}
