package com.stripe.android.net;

import com.stripe.android.exception.APIConnectionException;
import com.stripe.android.exception.APIException;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.exception.CardException;
import com.stripe.android.exception.InvalidRequestException;
import com.stripe.android.exception.PermissionException;
import com.stripe.android.exception.RateLimitException;
import com.stripe.android.model.Token;
import com.stripe.android.net.ErrorParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Security;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public abstract class StripeApiHandler {
    private static final SSLSocketFactory SSL_SOCKET_FACTORY = new StripeSSLSocketFactory();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class Parameter {
        public final String key;
        public final String value;

        public Parameter(String str, String str2) {
            this.key = str;
            this.value = str2;
        }
    }

    private static HttpURLConnection createGetConnection(String str, String str2, RequestOptions requestOptions) {
        HttpURLConnection createStripeConnection = createStripeConnection(formatURL(str, str2), requestOptions);
        createStripeConnection.setRequestMethod("GET");
        return createStripeConnection;
    }

    private static HttpURLConnection createPostConnection(String str, String str2, RequestOptions requestOptions) {
        OutputStream outputStream;
        HttpURLConnection createStripeConnection = createStripeConnection(str, requestOptions);
        createStripeConnection.setDoOutput(true);
        createStripeConnection.setRequestMethod("POST");
        createStripeConnection.setRequestProperty("Content-Type", String.format("application/x-www-form-urlencoded;charset=%s", "UTF-8"));
        try {
            outputStream = createStripeConnection.getOutputStream();
        } catch (Throwable th) {
            th = th;
            outputStream = null;
        }
        try {
            outputStream.write(str2.getBytes("UTF-8"));
            outputStream.close();
            return createStripeConnection;
        } catch (Throwable th2) {
            th = th2;
            if (outputStream != null) {
                outputStream.close();
            }
            throw th;
        }
    }

    static String createQuery(Map map) {
        StringBuilder sb = new StringBuilder();
        for (Parameter parameter : flattenParams(map)) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(urlEncodePair(parameter.key, parameter.value));
        }
        return sb.toString();
    }

    private static HttpURLConnection createStripeConnection(String str, RequestOptions requestOptions) {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        httpURLConnection.setConnectTimeout(30000);
        httpURLConnection.setReadTimeout(80000);
        httpURLConnection.setUseCaches(false);
        for (Map.Entry entry : getHeaders(requestOptions).entrySet()) {
            httpURLConnection.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
        }
        if (httpURLConnection instanceof HttpsURLConnection) {
            ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(SSL_SOCKET_FACTORY);
        }
        return httpURLConnection;
    }

    public static Token createToken(Map map, RequestOptions requestOptions) {
        return requestToken("POST", getApiUrl(), map, requestOptions);
    }

    private static List flattenParams(Map map) {
        return flattenParamsMap(map, null);
    }

    private static List flattenParamsList(List list, String str) {
        LinkedList linkedList = new LinkedList();
        Iterator it = list.iterator();
        String format = String.format("%s[]", str);
        if (list.isEmpty()) {
            linkedList.add(new Parameter(str, ""));
        } else {
            while (it.hasNext()) {
                linkedList.addAll(flattenParamsValue(it.next(), format));
            }
        }
        return linkedList;
    }

    private static List flattenParamsMap(Map map, String str) {
        LinkedList linkedList = new LinkedList();
        if (map == null) {
            return linkedList;
        }
        for (Map.Entry entry : map.entrySet()) {
            String str2 = (String) entry.getKey();
            Object value = entry.getValue();
            if (str != null) {
                str2 = String.format("%s[%s]", str, str2);
            }
            linkedList.addAll(flattenParamsValue(value, str2));
        }
        return linkedList;
    }

    private static List flattenParamsValue(Object obj, String str) {
        if (obj instanceof Map) {
            return flattenParamsMap((Map) obj, str);
        }
        if (obj instanceof List) {
            return flattenParamsList((List) obj, str);
        }
        if ("".equals(obj)) {
            throw new InvalidRequestException("You cannot set '" + str + "' to an empty string. We interpret empty strings as null in requests. You may set '" + str + "' to null to delete the property.", str, null, 0, null);
        }
        if (obj == null) {
            LinkedList linkedList = new LinkedList();
            linkedList.add(new Parameter(str, ""));
            return linkedList;
        }
        LinkedList linkedList2 = new LinkedList();
        linkedList2.add(new Parameter(str, obj.toString()));
        return linkedList2;
    }

    private static String formatURL(String str, String str2) {
        if (str2 == null || str2.isEmpty()) {
            return str;
        }
        return String.format("%s%s%s", str, str.contains("?") ? "&" : "?", str2);
    }

    static String getApiUrl() {
        return String.format("%s/v1/%s", "https://api.stripe.com", "tokens");
    }

    static Map getHeaders(RequestOptions requestOptions) {
        HashMap hashMap = new HashMap();
        String apiVersion = requestOptions.getApiVersion();
        hashMap.put("Accept-Charset", "UTF-8");
        hashMap.put("Accept", "application/json");
        hashMap.put("User-Agent", String.format("Stripe/v1 JavaBindings/%s", "3.5.0"));
        hashMap.put("Authorization", String.format("Bearer %s", requestOptions.getPublishableApiKey()));
        String[] strArr = {"os.name", "os.version", "os.arch", "java.version", "java.vendor", "java.vm.version", "java.vm.vendor"};
        HashMap hashMap2 = new HashMap();
        for (int i = 0; i < 7; i++) {
            String str = strArr[i];
            hashMap2.put(str, System.getProperty(str));
        }
        hashMap2.put("bindings.version", "3.5.0");
        hashMap2.put("lang", "Java");
        hashMap2.put("publisher", "Stripe");
        hashMap.put("X-Stripe-Client-User-Agent", new JSONObject(hashMap2).toString());
        if (apiVersion != null) {
            hashMap.put("Stripe-Version", apiVersion);
        }
        if (requestOptions.getIdempotencyKey() != null) {
            hashMap.put("Idempotency-Key", requestOptions.getIdempotencyKey());
        }
        return hashMap;
    }

    private static String getResponseBody(InputStream inputStream) {
        String next = new Scanner(inputStream, "UTF-8").useDelimiter("\\A").next();
        inputStream.close();
        return next;
    }

    private static StripeResponse getStripeResponse(String str, String str2, Map map, RequestOptions requestOptions) {
        try {
            return makeURLConnectionRequest(str, str2, createQuery(map), requestOptions);
        } catch (UnsupportedEncodingException e) {
            throw new InvalidRequestException("Unable to encode parameters to UTF-8. Please contact support@stripe.com for assistance.", null, null, 0, e);
        }
    }

    private static void handleAPIError(String str, int i, String str2) {
        ErrorParser.StripeError parseError = ErrorParser.parseError(str);
        if (i == 429) {
            throw new RateLimitException(parseError.message, parseError.param, str2, Integer.valueOf(i), null);
        }
        switch (i) {
            case 400:
                throw new InvalidRequestException(parseError.message, parseError.param, str2, Integer.valueOf(i), null);
            case 401:
                throw new AuthenticationException(parseError.message, str2, Integer.valueOf(i));
            case 402:
                throw new CardException(parseError.message, str2, parseError.code, parseError.param, parseError.decline_code, parseError.charge, Integer.valueOf(i), null);
            case 403:
                throw new PermissionException(parseError.message, str2, Integer.valueOf(i));
            case 404:
                throw new InvalidRequestException(parseError.message, parseError.param, str2, Integer.valueOf(i), null);
            default:
                throw new APIException(parseError.message, str2, Integer.valueOf(i), null);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x002e  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0053  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0046 A[Catch: all -> 0x001c, IOException -> 0x001f, TryCatch #0 {IOException -> 0x001f, blocks: (B:4:0x0003, B:12:0x0030, B:14:0x004b, B:18:0x0057, B:19:0x005b, B:20:0x0065, B:24:0x0060, B:25:0x0036, B:26:0x0045, B:27:0x0046, B:28:0x0012, B:31:0x0021), top: B:3:0x0003, outer: #1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static StripeResponse makeURLConnectionRequest(String str, String str2, String str3, RequestOptions requestOptions) {
        char c;
        HttpURLConnection createGetConnection;
        HttpURLConnection httpURLConnection = null;
        try {
            try {
                int hashCode = str.hashCode();
                if (hashCode == 70454) {
                    if (str.equals("GET")) {
                        c = 0;
                        if (c != 0) {
                        }
                        HttpURLConnection httpURLConnection2 = createGetConnection;
                        int responseCode = httpURLConnection2.getResponseCode();
                        StripeResponse stripeResponse = new StripeResponse(responseCode, getResponseBody((responseCode >= 200 || responseCode >= 300) ? httpURLConnection2.getErrorStream() : httpURLConnection2.getInputStream()), httpURLConnection2.getHeaderFields());
                        httpURLConnection2.disconnect();
                        return stripeResponse;
                    }
                    c = 65535;
                    if (c != 0) {
                    }
                    HttpURLConnection httpURLConnection22 = createGetConnection;
                    int responseCode2 = httpURLConnection22.getResponseCode();
                    StripeResponse stripeResponse2 = new StripeResponse(responseCode2, getResponseBody((responseCode2 >= 200 || responseCode2 >= 300) ? httpURLConnection22.getErrorStream() : httpURLConnection22.getInputStream()), httpURLConnection22.getHeaderFields());
                    httpURLConnection22.disconnect();
                    return stripeResponse2;
                }
                if (hashCode == 2461856 && str.equals("POST")) {
                    c = 1;
                    if (c != 0) {
                        createGetConnection = createGetConnection(str2, str3, requestOptions);
                    } else {
                        if (c != 1) {
                            throw new APIConnectionException(String.format("Unrecognized HTTP method %s. This indicates a bug in the Stripe bindings. Please contact support@stripe.com for assistance.", str));
                        }
                        createGetConnection = createPostConnection(str2, str3, requestOptions);
                    }
                    HttpURLConnection httpURLConnection222 = createGetConnection;
                    int responseCode22 = httpURLConnection222.getResponseCode();
                    StripeResponse stripeResponse22 = new StripeResponse(responseCode22, getResponseBody((responseCode22 >= 200 || responseCode22 >= 300) ? httpURLConnection222.getErrorStream() : httpURLConnection222.getInputStream()), httpURLConnection222.getHeaderFields());
                    httpURLConnection222.disconnect();
                    return stripeResponse22;
                }
                c = 65535;
                if (c != 0) {
                }
                HttpURLConnection httpURLConnection2222 = createGetConnection;
                int responseCode222 = httpURLConnection2222.getResponseCode();
                StripeResponse stripeResponse222 = new StripeResponse(responseCode222, getResponseBody((responseCode222 >= 200 || responseCode222 >= 300) ? httpURLConnection2222.getErrorStream() : httpURLConnection2222.getInputStream()), httpURLConnection2222.getHeaderFields());
                httpURLConnection2222.disconnect();
                return stripeResponse222;
            } catch (IOException e) {
                throw new APIConnectionException(String.format("IOException during API request to Stripe (%s): %s Please check your internet connection and try again. If this problem persists, you should check Stripe's service status at https://twitter.com/stripestatus, or let us know at support@stripe.com.", getApiUrl(), e.getMessage()), e);
            }
        } catch (Throwable th) {
            if (0 != 0) {
                httpURLConnection.disconnect();
            }
            throw th;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0095  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0026 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static Token requestToken(String str, String str2, Map map, RequestOptions requestOptions) {
        String str3;
        if (requestOptions == null) {
            return null;
        }
        Boolean bool = Boolean.TRUE;
        try {
            str3 = Security.getProperty("networkaddress.cache.ttl");
        } catch (SecurityException unused) {
            str3 = null;
        }
        try {
            Security.setProperty("networkaddress.cache.ttl", "0");
        } catch (SecurityException unused2) {
            bool = Boolean.FALSE;
            if (!requestOptions.getPublishableApiKey().trim().isEmpty()) {
            }
        }
        if (!requestOptions.getPublishableApiKey().trim().isEmpty()) {
            throw new AuthenticationException("No API key provided. (HINT: set your API key using 'Stripe.apiKey = <API-KEY>'. You can generate API keys from the Stripe web interface. See https://stripe.com/api for details or email support@stripe.com if you have questions.", null, 0);
        }
        try {
            StripeResponse stripeResponse = getStripeResponse(str, str2, map, requestOptions);
            int responseCode = stripeResponse.getResponseCode();
            String responseBody = stripeResponse.getResponseBody();
            Map responseHeaders = stripeResponse.getResponseHeaders();
            List list = responseHeaders == null ? null : (List) responseHeaders.get("Request-Id");
            String str4 = (list == null || list.size() <= 0) ? null : (String) list.get(0);
            if (responseCode < 200 || responseCode >= 300) {
                handleAPIError(responseBody, responseCode, str4);
            }
            Token parseToken = TokenParser.parseToken(responseBody);
            if (bool.booleanValue()) {
                if (str3 == null) {
                    Security.setProperty("networkaddress.cache.ttl", "-1");
                } else {
                    Security.setProperty("networkaddress.cache.ttl", str3);
                }
            }
            return parseToken;
        } catch (JSONException unused3) {
            if (bool.booleanValue()) {
                if (str3 == null) {
                    Security.setProperty("networkaddress.cache.ttl", "-1");
                } else {
                    Security.setProperty("networkaddress.cache.ttl", str3);
                }
            }
            return null;
        } catch (Throwable th) {
            if (bool.booleanValue()) {
                if (str3 == null) {
                    Security.setProperty("networkaddress.cache.ttl", "-1");
                } else {
                    Security.setProperty("networkaddress.cache.ttl", str3);
                }
            }
            throw th;
        }
    }

    private static String urlEncode(String str) {
        if (str == null) {
            return null;
        }
        return URLEncoder.encode(str, "UTF-8");
    }

    private static String urlEncodePair(String str, String str2) {
        return String.format("%s=%s", urlEncode(str), urlEncode(str2));
    }
}
