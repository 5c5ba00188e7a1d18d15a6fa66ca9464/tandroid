package com.huawei.hms.framework.network.grs.g;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import com.huawei.hms.framework.common.ContainerUtils;
import com.huawei.hms.framework.common.IoUtils;
import com.huawei.hms.framework.common.Logger;
import com.huawei.hms.framework.network.grs.GrsBaseInfo;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.Callable;
import javax.net.ssl.HttpsURLConnection;
/* loaded from: classes.dex */
public class g extends a implements Callable<d> {
    private static final String i = "g";

    public g(String str, int i2, c cVar, Context context, String str2, GrsBaseInfo grsBaseInfo, com.huawei.hms.framework.network.grs.e.c cVar2) {
        super(str, i2, cVar, context, str2, grsBaseInfo, cVar2);
    }

    /* JADX WARN: Not initialized variable reg: 9, insn: 0x0130: MOVE  (r6 I:??[OBJECT, ARRAY]) = (r9 I:??[OBJECT, ARRAY]), block:B:56:0x0130 */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0123  */
    @Override // java.util.concurrent.Callable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public d call() {
        long j;
        HttpsURLConnection httpsURLConnection;
        long j2;
        IOException e;
        long elapsedRealtime;
        HttpsURLConnection httpsURLConnection2;
        String str = i;
        Logger.i(str, "Post call execute");
        long j3 = 0;
        HttpsURLConnection httpsURLConnection3 = null;
        InputStream inputStream = null;
        byte[] bArr = null;
        try {
            try {
                try {
                    elapsedRealtime = SystemClock.elapsedRealtime();
                    try {
                        j3 = System.currentTimeMillis();
                        httpsURLConnection = com.huawei.hms.framework.network.grs.h.f.a.a(c(), a(), e());
                    } catch (IOException e2) {
                        e = e2;
                        httpsURLConnection = null;
                    }
                } catch (IOException e3) {
                    e = e3;
                    httpsURLConnection = null;
                    j2 = 0;
                }
                try {
                } catch (IOException e4) {
                    e = e4;
                    long j4 = j3;
                    j3 = elapsedRealtime;
                    j2 = j4;
                    long elapsedRealtime2 = SystemClock.elapsedRealtime();
                    j = System.currentTimeMillis();
                    Logger.w(i, "RequestCallableV2 run task catch IOException", e);
                    this.a = new d(e, elapsedRealtime2 - j3);
                    if (httpsURLConnection != null) {
                        try {
                            httpsURLConnection.disconnect();
                        } catch (RuntimeException unused) {
                            j3 = j2;
                            Logger.w(i, "RequestCallableV2 disconnect HttpsURLConnection catch RuntimeException");
                            this.a.b(c());
                            this.a.a(d());
                            this.a.b(j3);
                            this.a.a(j);
                            if (b() != null) {
                            }
                            return this.a;
                        } catch (Throwable unused2) {
                            j3 = j2;
                            Logger.w(i, "RequestCallableV2 disconnect HttpsURLConnection catch Throwable");
                            this.a.b(c());
                            this.a.a(d());
                            this.a.b(j3);
                            this.a.a(j);
                            if (b() != null) {
                            }
                            return this.a;
                        }
                    }
                    j3 = j2;
                    this.a.b(c());
                    this.a.a(d());
                    this.a.b(j3);
                    this.a.a(j);
                    if (b() != null) {
                    }
                    return this.a;
                }
                if (httpsURLConnection == null) {
                    Logger.w(str, "create HttpsURLConnection instance by url return null.");
                    if (httpsURLConnection != null) {
                        try {
                            httpsURLConnection.disconnect();
                        } catch (RuntimeException unused3) {
                            Logger.w(i, "RequestCallableV2 disconnect HttpsURLConnection catch RuntimeException");
                        } catch (Throwable unused4) {
                            Logger.w(i, "RequestCallableV2 disconnect HttpsURLConnection catch Throwable");
                        }
                    }
                    return null;
                }
                httpsURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.setDoInput(true);
                String a = b() != null ? b().a() : "";
                if (TextUtils.isEmpty(a)) {
                    a = ContainerUtils.FIELD_DELIMITER;
                }
                httpsURLConnection.setRequestProperty("If-None-Match", a);
                httpsURLConnection.connect();
                com.huawei.hms.framework.network.grs.h.f.a.a(httpsURLConnection, f().a("services", ""));
                int responseCode = httpsURLConnection.getResponseCode();
                if (responseCode == 200) {
                    try {
                        inputStream = httpsURLConnection.getInputStream();
                        byte[] byteArray = IoUtils.toByteArray(inputStream);
                        IoUtils.closeSecure(inputStream);
                        bArr = byteArray;
                    } catch (Throwable th) {
                        IoUtils.closeSecure(inputStream);
                        throw th;
                    }
                }
                Map headerFields = httpsURLConnection.getHeaderFields();
                httpsURLConnection.disconnect();
                long elapsedRealtime3 = SystemClock.elapsedRealtime();
                j = System.currentTimeMillis();
                this.a = new d(responseCode, headerFields, bArr == null ? new byte[0] : bArr, elapsedRealtime3 - elapsedRealtime);
                try {
                    httpsURLConnection.disconnect();
                } catch (RuntimeException unused5) {
                    Logger.w(i, "RequestCallableV2 disconnect HttpsURLConnection catch RuntimeException");
                    this.a.b(c());
                    this.a.a(d());
                    this.a.b(j3);
                    this.a.a(j);
                    if (b() != null) {
                    }
                    return this.a;
                } catch (Throwable unused6) {
                    Logger.w(i, "RequestCallableV2 disconnect HttpsURLConnection catch Throwable");
                    this.a.b(c());
                    this.a.a(d());
                    this.a.b(j3);
                    this.a.a(j);
                    if (b() != null) {
                    }
                    return this.a;
                }
                this.a.b(c());
                this.a.a(d());
                this.a.b(j3);
                this.a.a(j);
                if (b() != null) {
                    b().a(this.a);
                }
                return this.a;
            } catch (Throwable th2) {
                th = th2;
                if (httpsURLConnection3 != null) {
                    try {
                        httpsURLConnection3.disconnect();
                    } catch (RuntimeException unused7) {
                        Logger.w(i, "RequestCallableV2 disconnect HttpsURLConnection catch RuntimeException");
                    } catch (Throwable unused8) {
                        Logger.w(i, "RequestCallableV2 disconnect HttpsURLConnection catch Throwable");
                    }
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            httpsURLConnection3 = httpsURLConnection2;
        }
    }
}
