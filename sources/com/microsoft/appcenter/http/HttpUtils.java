package com.microsoft.appcenter.http;

import android.content.Context;
import android.os.Build;
import com.microsoft.appcenter.utils.NetworkStateHelper;
import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.RejectedExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;

/* loaded from: classes.dex */
public abstract class HttpUtils {
    private static final Class[] RECOVERABLE_EXCEPTIONS = {EOFException.class, InterruptedIOException.class, SocketException.class, UnknownHostException.class, RejectedExecutionException.class};
    private static final Pattern CONNECTION_ISSUE_PATTERN = Pattern.compile("connection (time|reset|abort)|failure in ssl library, usually a protocol error|anchor for certification path not found");
    private static final Pattern TOKEN_VALUE_PATTERN = Pattern.compile(":[^\"]+");
    private static final Pattern API_KEY_PATTERN = Pattern.compile("-[^,]+(,|$)");

    public static HttpClient createHttpClient(Context context) {
        return createHttpClient(context, true);
    }

    public static HttpClient createHttpClient(Context context, boolean z) {
        return new HttpClientRetryer(createHttpClientWithoutRetryer(context, z));
    }

    public static HttpClient createHttpClientWithoutRetryer(Context context, boolean z) {
        return new HttpClientNetworkStateHandler(new DefaultHttpClient(z), NetworkStateHelper.getSharedInstance(context));
    }

    public static HttpsURLConnection createHttpsConnection(URL url) {
        if (!"https".equals(url.getProtocol())) {
            throw new IOException("App Center support only HTTPS connection.");
        }
        URLConnection openConnection = url.openConnection();
        if (!(openConnection instanceof HttpsURLConnection)) {
            throw new IOException("App Center supports only HTTPS connection.");
        }
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) openConnection;
        if (Build.VERSION.SDK_INT <= 21) {
            httpsURLConnection.setSSLSocketFactory(new TLS1_2SocketFactory());
        }
        httpsURLConnection.setConnectTimeout(10000);
        httpsURLConnection.setReadTimeout(10000);
        return httpsURLConnection;
    }

    public static String hideApiKeys(String str) {
        StringBuilder sb = new StringBuilder();
        Matcher matcher = API_KEY_PATTERN.matcher(str);
        int i = 0;
        while (matcher.find()) {
            sb.append(str.substring(i, matcher.start()));
            sb.append("-***");
            sb.append(matcher.group(1));
            i = matcher.end();
        }
        if (i < str.length()) {
            sb.append(str.substring(i));
        }
        return sb.toString();
    }

    public static String hideSecret(String str) {
        int length = str.length() - (str.length() < 8 ? 0 : 8);
        char[] cArr = new char[length];
        Arrays.fill(cArr, '*');
        return new String(cArr) + str.substring(length);
    }

    public static String hideTickets(String str) {
        return TOKEN_VALUE_PATTERN.matcher(str).replaceAll(":***");
    }

    public static boolean isRecoverableError(Throwable th) {
        String message;
        if (th instanceof HttpException) {
            int statusCode = ((HttpException) th).getHttpResponse().getStatusCode();
            return statusCode >= 500 || statusCode == 408 || statusCode == 429;
        }
        for (Class cls : RECOVERABLE_EXCEPTIONS) {
            if (cls.isAssignableFrom(th.getClass())) {
                return true;
            }
        }
        Throwable cause = th.getCause();
        if (cause != null) {
            for (Class cls2 : RECOVERABLE_EXCEPTIONS) {
                if (cls2.isAssignableFrom(cause.getClass())) {
                    return true;
                }
            }
        }
        return (th instanceof SSLException) && (message = th.getMessage()) != null && CONNECTION_ISSUE_PATTERN.matcher(message.toLowerCase(Locale.US)).find();
    }
}
