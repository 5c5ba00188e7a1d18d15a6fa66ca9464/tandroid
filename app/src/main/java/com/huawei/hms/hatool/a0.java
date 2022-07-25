package com.huawei.hms.hatool;

import android.text.TextUtils;
import com.huawei.secure.android.common.ssl.SecureSSLSocketFactory;
import com.huawei.secure.android.common.ssl.hostname.StrictHostnameVerifier;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
/* loaded from: classes.dex */
public abstract class a0 {

    /* loaded from: classes.dex */
    public static class a extends Exception {
        public a(String str) {
            super(str);
        }
    }

    public static b0 a(String str, byte[] bArr, Map<String, String> map) {
        return a(str, bArr, map, "POST");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r6v0, types: [java.lang.CharSequence, java.lang.String] */
    /* JADX WARN: Type inference failed for: r6v1 */
    /* JADX WARN: Type inference failed for: r6v10, types: [java.net.HttpURLConnection] */
    /* JADX WARN: Type inference failed for: r6v11, types: [java.net.HttpURLConnection] */
    /* JADX WARN: Type inference failed for: r6v12, types: [java.net.HttpURLConnection] */
    /* JADX WARN: Type inference failed for: r6v13, types: [java.net.HttpURLConnection] */
    /* JADX WARN: Type inference failed for: r6v14, types: [java.net.HttpURLConnection] */
    /* JADX WARN: Type inference failed for: r6v15, types: [java.net.HttpURLConnection] */
    /* JADX WARN: Type inference failed for: r6v16, types: [java.net.HttpURLConnection] */
    /* JADX WARN: Type inference failed for: r6v17, types: [java.net.HttpURLConnection] */
    /* JADX WARN: Type inference failed for: r6v2 */
    /* JADX WARN: Type inference failed for: r6v3 */
    /* JADX WARN: Type inference failed for: r6v4 */
    /* JADX WARN: Type inference failed for: r6v5 */
    /* JADX WARN: Type inference failed for: r6v6 */
    /* JADX WARN: Type inference failed for: r6v7 */
    /* JADX WARN: Type inference failed for: r6v8 */
    /* JADX WARN: Type inference failed for: r6v9, types: [java.net.HttpURLConnection] */
    /* JADX WARN: Type inference failed for: r8v0, types: [java.util.Map<java.lang.String, java.lang.String>, java.util.Map] */
    /* JADX WARN: Type inference failed for: r8v1 */
    /* JADX WARN: Type inference failed for: r8v10, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r8v11, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r8v12, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r8v13, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r8v14, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r8v15, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r8v16, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r8v17 */
    /* JADX WARN: Type inference failed for: r8v18 */
    /* JADX WARN: Type inference failed for: r8v19 */
    /* JADX WARN: Type inference failed for: r8v2 */
    /* JADX WARN: Type inference failed for: r8v20 */
    /* JADX WARN: Type inference failed for: r8v21 */
    /* JADX WARN: Type inference failed for: r8v22 */
    /* JADX WARN: Type inference failed for: r8v23 */
    /* JADX WARN: Type inference failed for: r8v24 */
    /* JADX WARN: Type inference failed for: r8v25, types: [java.io.OutputStream, java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r8v3 */
    /* JADX WARN: Type inference failed for: r8v4 */
    /* JADX WARN: Type inference failed for: r8v5 */
    /* JADX WARN: Type inference failed for: r8v6 */
    /* JADX WARN: Type inference failed for: r8v7 */
    /* JADX WARN: Type inference failed for: r8v8 */
    /* JADX WARN: Type inference failed for: r8v9, types: [java.io.Closeable] */
    public static b0 a(String str, byte[] bArr, Map<String, String> map, String str2) {
        if (TextUtils.isEmpty(str)) {
            return new b0(-100, "");
        }
        int i = -102;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            try {
                str = a((String) str, bArr.length, (Map<String, String>) map, str2);
                try {
                    if (str == 0) {
                        b0 b0Var = new b0(-101, "");
                        t0.a((Closeable) null);
                        t0.a((Closeable) null);
                        if (str != 0) {
                            t0.a((HttpURLConnection) str);
                        }
                        return b0Var;
                    }
                    map = str.getOutputStream();
                    try {
                        BufferedOutputStream bufferedOutputStream2 = new BufferedOutputStream(map);
                        try {
                            try {
                                bufferedOutputStream2.write(bArr);
                                bufferedOutputStream2.flush();
                                int responseCode = str.getResponseCode();
                                try {
                                    b0 b0Var2 = new b0(responseCode, b(str));
                                    t0.a((Closeable) bufferedOutputStream2);
                                    t0.a((Closeable) map);
                                    t0.a((HttpURLConnection) str);
                                    return b0Var2;
                                } catch (SecurityException unused) {
                                    i = responseCode;
                                    bufferedOutputStream = bufferedOutputStream2;
                                    y.f("hmsSdk", "SecurityException with HttpClient. Please check INTERNET permission.");
                                    b0 b0Var3 = new b0(i, "");
                                    t0.a((Closeable) bufferedOutputStream);
                                    t0.a((Closeable) map);
                                    if (str != 0) {
                                        t0.a((HttpURLConnection) str);
                                    }
                                    return b0Var3;
                                } catch (ConnectException unused2) {
                                    i = responseCode;
                                    bufferedOutputStream = bufferedOutputStream2;
                                    y.f("hmsSdk", "Network is unreachable or Connection refused");
                                    b0 b0Var4 = new b0(i, "");
                                    t0.a((Closeable) bufferedOutputStream);
                                    t0.a((Closeable) map);
                                    if (str != 0) {
                                        t0.a((HttpURLConnection) str);
                                    }
                                    return b0Var4;
                                } catch (UnknownHostException unused3) {
                                    i = responseCode;
                                    bufferedOutputStream = bufferedOutputStream2;
                                    y.f("hmsSdk", "No address associated with hostname or No network");
                                    b0 b0Var5 = new b0(i, "");
                                    t0.a((Closeable) bufferedOutputStream);
                                    t0.a((Closeable) map);
                                    if (str != 0) {
                                        t0.a((HttpURLConnection) str);
                                    }
                                    return b0Var5;
                                } catch (SSLHandshakeException unused4) {
                                    i = responseCode;
                                    bufferedOutputStream = bufferedOutputStream2;
                                    y.f("hmsSdk", "Chain validation failed,Certificate expired");
                                    b0 b0Var6 = new b0(i, "");
                                    t0.a((Closeable) bufferedOutputStream);
                                    t0.a((Closeable) map);
                                    if (str != 0) {
                                        t0.a((HttpURLConnection) str);
                                    }
                                    return b0Var6;
                                } catch (SSLPeerUnverifiedException unused5) {
                                    i = responseCode;
                                    bufferedOutputStream = bufferedOutputStream2;
                                    y.f("hmsSdk", "Certificate has not been verified,Request is restricted!");
                                    b0 b0Var7 = new b0(i, "");
                                    t0.a((Closeable) bufferedOutputStream);
                                    t0.a((Closeable) map);
                                    if (str != 0) {
                                        t0.a((HttpURLConnection) str);
                                    }
                                    return b0Var7;
                                } catch (IOException unused6) {
                                    i = responseCode;
                                    bufferedOutputStream = bufferedOutputStream2;
                                    y.f("hmsSdk", "events PostRequest(byte[]): IOException occurred.");
                                    b0 b0Var8 = new b0(i, "");
                                    t0.a((Closeable) bufferedOutputStream);
                                    t0.a((Closeable) map);
                                    if (str != 0) {
                                        t0.a((HttpURLConnection) str);
                                    }
                                    return b0Var8;
                                }
                            } catch (a unused7) {
                                bufferedOutputStream = bufferedOutputStream2;
                                y.f("hmsSdk", "PostRequest(byte[]): No ssl socket factory set!");
                                b0 b0Var9 = new b0(-101, "");
                                t0.a((Closeable) bufferedOutputStream);
                                t0.a((Closeable) map);
                                if (str != 0) {
                                    t0.a((HttpURLConnection) str);
                                }
                                return b0Var9;
                            } catch (Throwable th) {
                                th = th;
                                bufferedOutputStream = bufferedOutputStream2;
                                t0.a((Closeable) bufferedOutputStream);
                                t0.a((Closeable) map);
                                if (str != 0) {
                                    t0.a((HttpURLConnection) str);
                                }
                                throw th;
                            }
                        } catch (SecurityException unused8) {
                        } catch (ConnectException unused9) {
                        } catch (UnknownHostException unused10) {
                        } catch (SSLHandshakeException unused11) {
                        } catch (SSLPeerUnverifiedException unused12) {
                        } catch (IOException unused13) {
                        }
                    } catch (a unused14) {
                    } catch (SecurityException unused15) {
                    } catch (ConnectException unused16) {
                    } catch (UnknownHostException unused17) {
                    } catch (SSLHandshakeException unused18) {
                    } catch (SSLPeerUnverifiedException unused19) {
                    } catch (IOException unused20) {
                    }
                } catch (a unused21) {
                    map = 0;
                } catch (SecurityException unused22) {
                    map = 0;
                } catch (ConnectException unused23) {
                    map = 0;
                } catch (UnknownHostException unused24) {
                    map = 0;
                } catch (SSLHandshakeException unused25) {
                    map = 0;
                } catch (SSLPeerUnverifiedException unused26) {
                    map = 0;
                } catch (IOException unused27) {
                    map = 0;
                } catch (Throwable th2) {
                    th = th2;
                    map = 0;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        } catch (a unused28) {
            str = 0;
            map = 0;
        } catch (SecurityException unused29) {
            str = 0;
            map = 0;
        } catch (ConnectException unused30) {
            str = 0;
            map = 0;
        } catch (UnknownHostException unused31) {
            str = 0;
            map = 0;
        } catch (SSLHandshakeException unused32) {
            str = 0;
            map = 0;
        } catch (SSLPeerUnverifiedException unused33) {
            str = 0;
            map = 0;
        } catch (IOException unused34) {
            str = 0;
            map = 0;
        } catch (Throwable th4) {
            th = th4;
            str = 0;
            map = 0;
        }
    }

    public static HttpURLConnection a(String str, int i, Map<String, String> map, String str2) {
        if (TextUtils.isEmpty(str)) {
            y.b("hmsSdk", "CreateConnection: invalid urlPath.");
            return null;
        }
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        a(httpURLConnection);
        httpURLConnection.setRequestMethod(str2);
        httpURLConnection.setConnectTimeout(15000);
        httpURLConnection.setReadTimeout(15000);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(i));
        httpURLConnection.setRequestProperty("Connection", "close");
        if (map != null && map.size() >= 1) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                if (key != null && !TextUtils.isEmpty(key)) {
                    httpURLConnection.setRequestProperty(key, entry.getValue());
                }
            }
        }
        return httpURLConnection;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0031  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0025  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void a(HttpURLConnection httpURLConnection) {
        String str;
        if (httpURLConnection instanceof HttpsURLConnection) {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) httpURLConnection;
            SecureSSLSocketFactory secureSSLSocketFactory = null;
            try {
                secureSSLSocketFactory = SecureSSLSocketFactory.getInstance(b.i());
            } catch (IOException unused) {
                str = "getSocketFactory(): IO Exception!";
                y.f("hmsSdk", str);
                if (secureSSLSocketFactory != null) {
                }
            } catch (IllegalAccessException unused2) {
                str = "getSocketFactory(): Illegal Access Exception ";
                y.f("hmsSdk", str);
                if (secureSSLSocketFactory != null) {
                }
            } catch (KeyStoreException unused3) {
                str = "getSocketFactory(): Key Store exception";
                y.f("hmsSdk", str);
                if (secureSSLSocketFactory != null) {
                }
            } catch (NoSuchAlgorithmException unused4) {
                str = "getSocketFactory(): Algorithm Exception!";
                y.f("hmsSdk", str);
                if (secureSSLSocketFactory != null) {
                }
            } catch (GeneralSecurityException unused5) {
                str = "getSocketFactory(): General Security Exception";
                y.f("hmsSdk", str);
                if (secureSSLSocketFactory != null) {
                }
            }
            if (secureSSLSocketFactory != null) {
                throw new a("No ssl socket factory set");
            }
            httpsURLConnection.setSSLSocketFactory(secureSSLSocketFactory);
            httpsURLConnection.setHostnameVerifier(new StrictHostnameVerifier());
        }
    }

    public static String b(HttpURLConnection httpURLConnection) {
        InputStream inputStream = null;
        try {
            try {
                inputStream = httpURLConnection.getInputStream();
                return t0.a(inputStream);
            } catch (IOException unused) {
                int responseCode = httpURLConnection.getResponseCode();
                y.f("hmsSdk", "When Response Content From Connection inputStream operation exception! " + responseCode);
                t0.a((Closeable) inputStream);
                return "";
            }
        } finally {
            t0.a((Closeable) inputStream);
        }
    }
}
