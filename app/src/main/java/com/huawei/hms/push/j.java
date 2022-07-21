package com.huawei.hms.push;

import android.content.Context;
import android.content.Intent;
import com.huawei.hms.push.constant.RemoteMessageConst;
import com.huawei.hms.support.log.HMSLog;
/* compiled from: SelfShowType.java */
/* loaded from: classes.dex */
public class j {
    public static final String[] a = {RemoteMessageConst.Notification.URL, "app", "cosa", "rp"};
    public Context b;
    public k c;

    public j(Context context, k kVar) {
        this.b = context;
        this.c = kVar;
    }

    public static boolean a(String str) {
        for (String str2 : a) {
            if (str2.equals(str)) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x007a, code lost:
        if (r3 != false) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x00ba, code lost:
        if (com.huawei.hms.push.q.a(r6.b, r6.c.d(), r2).booleanValue() != false) goto L22;
     */
    /* JADX WARN: Removed duplicated region for block: B:24:0x00bf  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00c5 A[Catch: Exception -> 0x00e1, TryCatch #1 {Exception -> 0x00e1, blocks: (B:3:0x0007, B:5:0x000e, B:7:0x001c, B:15:0x007e, B:17:0x0085, B:18:0x0097, B:20:0x009f, B:25:0x00c1, B:27:0x00c5, B:29:0x00d0, B:30:0x00d6, B:31:0x00db, B:10:0x0044, B:12:0x0059), top: B:37:0x0007, inners: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void b() {
        Intent parseUri;
        HMSLog.i("PushSelfShowLog", "run into launchCosaApp");
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("enter launchExistApp cosa, appPackageName =");
            sb.append(this.c.d());
            sb.append(",and msg.intentUri is ");
            sb.append(this.c.n());
            HMSLog.i("PushSelfShowLog", sb.toString());
            Intent b = q.b(this.b, this.c.d());
            boolean z = false;
            if (this.c.n() != null) {
                try {
                    parseUri = Intent.parseUri(this.c.n(), 0);
                    parseUri.setSelector(null);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Intent.parseUri(msg.intentUri, 0), action:");
                    sb2.append(parseUri.getAction());
                    HMSLog.i("PushSelfShowLog", sb2.toString());
                    z = q.a(this.b, this.c.d(), parseUri).booleanValue();
                } catch (Exception e) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("intentUri error.");
                    sb3.append(e.toString());
                    HMSLog.w("PushSelfShowLog", sb3.toString());
                }
            } else {
                if (this.c.a() != null) {
                    parseUri = new Intent(this.c.a());
                }
                if (b != null) {
                    HMSLog.i("PushSelfShowLog", "launchCosaApp,intent == null");
                    return;
                }
                b.setPackage(this.c.d());
                if (z) {
                    b.addFlags(268435456);
                } else {
                    b.setFlags(805437440);
                }
                this.b.startActivity(b);
                return;
            }
            b = parseUri;
            if (b != null) {
            }
        } catch (Exception e2) {
            HMSLog.e("PushSelfShowLog", "launch Cosa App exception." + e2.toString());
        }
    }

    public void c() {
        k kVar;
        HMSLog.d("PushSelfShowLog", "enter launchNotify()");
        if (this.b != null && (kVar = this.c) != null) {
            if ("app".equals(kVar.i())) {
                a();
                return;
            } else if ("cosa".equals(this.c.i())) {
                b();
                return;
            } else if ("rp".equals(this.c.i())) {
                HMSLog.w("PushSelfShowLog", this.c.i() + " not support rich message.");
                return;
            } else if (RemoteMessageConst.Notification.URL.equals(this.c.i())) {
                HMSLog.w("PushSelfShowLog", this.c.i() + " not support URL.");
                return;
            } else {
                HMSLog.d("PushSelfShowLog", this.c.i() + " is not exist in hShowType");
                return;
            }
        }
        HMSLog.d("PushSelfShowLog", "launchNotify  context or msg is null");
    }

    public final void a() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("enter launchApp, appPackageName =");
            sb.append(this.c.d());
            HMSLog.i("PushSelfShowLog", sb.toString());
            if (!q.c(this.b, this.c.d())) {
                return;
            }
            b();
        } catch (Exception e) {
            HMSLog.e("PushSelfShowLog", "launchApp error:" + e.toString());
        }
    }
}
