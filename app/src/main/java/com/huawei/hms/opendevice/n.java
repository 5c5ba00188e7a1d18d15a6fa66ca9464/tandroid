package com.huawei.hms.opendevice;

import android.content.Context;
import android.text.TextUtils;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.android.HwBuildEx;
import com.huawei.hms.android.SystemUtils;
import com.huawei.hms.push.constant.RemoteMessageConst;
import com.huawei.hms.support.hianalytics.HiAnalyticsConstant;
import com.huawei.hms.support.log.HMSLog;
import com.huawei.hms.utils.PackageManagerHelper;
import com.huawei.hms.utils.Util;
import java.util.TimeZone;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;
/* compiled from: ReportAaidToken.java */
/* loaded from: classes.dex */
public class n {

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ReportAaidToken.java */
    /* loaded from: classes.dex */
    public enum a {
        MOBILE("1"),
        PC("2"),
        TABLET("3"),
        TV("4"),
        SOUNDBOX("5"),
        GLASS("6"),
        WATCH("7"),
        VEHICLE("8"),
        OFFICE_DEVICE("9"),
        IOT_DEVICES("10"),
        HEALTHY("11"),
        ENTERTAINMENT("12"),
        TRANSPORT_DEVICES("13");
        
        public String o;

        a(String str) {
            this.o = str;
        }

        public String a() {
            return this.o;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ReportAaidToken.java */
    /* loaded from: classes.dex */
    public enum b {
        IOS("ios"),
        ANDROID("android"),
        HARMONY("harmony"),
        WINDOWS("windows"),
        EMBED("embed"),
        OTHERS("others");
        
        public String h;

        b(String str) {
            this.h = str;
        }

        public String a() {
            return this.h;
        }
    }

    public static String c(Context context, String str, String str2) {
        try {
            JSONObject jSONObject = new JSONObject();
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("timezone", TimeZone.getDefault().getID());
            jSONObject2.put("country", SystemUtils.getLocalCountry());
            JSONObject jSONObject3 = new JSONObject();
            jSONObject3.put("agent_version", new PackageManagerHelper(context).getPackageVersionName("com.huawei.android.pushagent"));
            jSONObject3.put("hms_version", String.valueOf(Util.getHmsVersion(context)));
            JSONObject jSONObject4 = new JSONObject();
            jSONObject4.put("dev_type", a.MOBILE.a());
            jSONObject4.put("dev_sub_type", "phone");
            jSONObject4.put("os_type", b.ANDROID.a());
            jSONObject4.put("os_version", String.valueOf(HwBuildEx.VERSION.EMUI_SDK_INT));
            jSONObject.put("id", UUID.randomUUID().toString());
            jSONObject.put("global", jSONObject2);
            jSONObject.put("push_agent", jSONObject3);
            jSONObject.put("hardware", jSONObject4);
            jSONObject.put("aaid", str);
            jSONObject.put("token", str2);
            jSONObject.put(HiAnalyticsConstant.BI_KEY_APP_ID, AGConnectServicesConfig.fromContext(context).getString("client/app_id"));
            jSONObject.put("region", AGConnectServicesConfig.fromContext(context).getString("region"));
            return jSONObject.toString();
        } catch (JSONException unused) {
            HMSLog.e("ReportAaidToken", "Catch JSONException.");
            return null;
        }
    }

    public static boolean d(Context context, String str, String str2) {
        i a2 = i.a(context);
        if (!a2.containsKey("reportAaidAndToken")) {
            HMSLog.d("ReportAaidToken", "It hasn't been reported, this time needs report.");
            return true;
        }
        String string = a2.getString("reportAaidAndToken");
        if (TextUtils.isEmpty(string)) {
            HMSLog.e("ReportAaidToken", "It has been reported, but sp file is empty, this time needs report.");
            return true;
        }
        return !string.equals(r.a(str2 + str, "SHA-256"));
    }

    public static boolean b(Context context) {
        int packageVersionCode = new PackageManagerHelper(context).getPackageVersionCode("com.huawei.android.pushagent");
        HMSLog.d("ReportAaidToken", "NC version code: " + packageVersionCode);
        return (90101400 <= packageVersionCode && packageVersionCode < 100000000) || packageVersionCode >= 100001301;
    }

    public static void a(Context context, String str) {
        new m(context, str).start();
    }

    public static void b(Context context, String str, String str2, String str3) {
        if (TextUtils.isEmpty(str)) {
            HMSLog.e("ReportAaidToken", "Https response is empty.");
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            int optInt = jSONObject.optInt("ret", 256);
            if (optInt == 0) {
                boolean saveString = i.a(context).saveString("reportAaidAndToken", r.a(str3 + str2, "SHA-256"));
                StringBuilder sb = new StringBuilder();
                sb.append("Report success ");
                sb.append(saveString ? "and save success." : "but save failure.");
                HMSLog.d("ReportAaidToken", sb.toString());
                return;
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Https response body's ret code: ");
            sb2.append(optInt);
            sb2.append(", error message: ");
            sb2.append(jSONObject.optString(RemoteMessageConst.MessageBody.MSG));
            HMSLog.e("ReportAaidToken", sb2.toString());
        } catch (JSONException unused) {
            HMSLog.e("ReportAaidToken", "Has JSONException.");
        } catch (Exception unused2) {
            HMSLog.e("ReportAaidToken", "Exception occur.");
        }
    }
}
