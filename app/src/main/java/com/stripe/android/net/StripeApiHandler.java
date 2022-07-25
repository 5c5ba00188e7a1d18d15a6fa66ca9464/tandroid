package com.stripe.android.net;

import com.huawei.hms.framework.common.ContainerUtils;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class StripeApiHandler {
    private static final SSLSocketFactory SSL_SOCKET_FACTORY = new StripeSSLSocketFactory();

    public static Token createToken(Map<String, Object> map, RequestOptions requestOptions) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
        return requestToken("POST", getApiUrl(), map, requestOptions);
    }

    static String createQuery(Map<String, Object> map) throws UnsupportedEncodingException, InvalidRequestException {
        StringBuilder sb = new StringBuilder();
        for (Parameter parameter : flattenParams(map)) {
            if (sb.length() > 0) {
                sb.append(ContainerUtils.FIELD_DELIMITER);
            }
            sb.append(urlEncodePair(parameter.key, parameter.value));
        }
        return sb.toString();
    }

    static Map<String, String> getHeaders(RequestOptions requestOptions) {
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

    static String getApiUrl() {
        return String.format("%s/v1/%s", "https://api.stripe.com", "tokens");
    }

    private static String formatURL(String str, String str2) {
        if (str2 == null || str2.isEmpty()) {
            return str;
        }
        String str3 = "?";
        if (str.contains(str3)) {
            str3 = ContainerUtils.FIELD_DELIMITER;
        }
        return String.format("%s%s%s", str, str3, str2);
    }

    private static HttpURLConnection createGetConnection(String str, String str2, RequestOptions requestOptions) throws IOException {
        HttpURLConnection createStripeConnection = createStripeConnection(formatURL(str, str2), requestOptions);
        createStripeConnection.setRequestMethod("GET");
        return createStripeConnection;
    }

    private static HttpURLConnection createPostConnection(String str, String str2, RequestOptions requestOptions) throws IOException {
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

    private static HttpURLConnection createStripeConnection(String str, RequestOptions requestOptions) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        httpURLConnection.setConnectTimeout(30000);
        httpURLConnection.setReadTimeout(80000);
        httpURLConnection.setUseCaches(false);
        for (Map.Entry<String, String> entry : getHeaders(requestOptions).entrySet()) {
            httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
        }
        if (httpURLConnection instanceof HttpsURLConnection) {
            ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(SSL_SOCKET_FACTORY);
        }
        return httpURLConnection;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0093  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0026 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static Token requestToken(String str, String str2, Map<String, Object> map, RequestOptions requestOptions) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
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
            Map<String, List<String>> responseHeaders = stripeResponse.getResponseHeaders();
            List<String> list = responseHeaders == null ? null : responseHeaders.get("Request-Id");
            String str4 = (list == null || list.size() <= 0) ? null : list.get(0);
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

    private static StripeResponse getStripeResponse(String str, String str2, Map<String, Object> map, RequestOptions requestOptions) throws InvalidRequestException, APIConnectionException, APIException {
        try {
            return makeURLConnectionRequest(str, str2, createQuery(map), requestOptions);
        } catch (UnsupportedEncodingException e) {
            throw new InvalidRequestException("Unable to encode parameters to UTF-8. Please contact support@stripe.com for assistance.", null, null, 0, e);
        }
    }

    private static List<Parameter> flattenParams(Map<String, Object> map) throws InvalidRequestException {
        return flattenParamsMap(map, null);
    }

    private static List<Parameter> flattenParamsList(List<Object> list, String str) throws InvalidRequestException {
        LinkedList linkedList = new LinkedList();
        String format = String.format("%s[]", str);
        if (list.isEmpty()) {
            linkedList.add(new Parameter(str, ""));
        } else {
            for (Object obj : list) {
                linkedList.addAll(flattenParamsValue(obj, format));
            }
        }
        return linkedList;
    }

    private static List<Parameter> flattenParamsMap(Map<String, Object> map, String str) throws InvalidRequestException {
        LinkedList linkedList = new LinkedList();
        if (map == null) {
            return linkedList;
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (str != null) {
                key = String.format("%s[%s]", str, key);
            }
            linkedList.addAll(flattenParamsValue(value, key));
        }
        return linkedList;
    }

    private static List<Parameter> flattenParamsValue(Object obj, String str) throws InvalidRequestException {
        if (obj instanceof Map) {
            return flattenParamsMap((Map) obj, str);
        }
        if (obj instanceof List) {
            return flattenParamsList((List) obj, str);
        }
        if (!"".equals(obj)) {
            if (obj == null) {
                LinkedList linkedList = new LinkedList();
                linkedList.add(new Parameter(str, ""));
                return linkedList;
            }
            LinkedList linkedList2 = new LinkedList();
            linkedList2.add(new Parameter(str, obj.toString()));
            return linkedList2;
        }
        throw new InvalidRequestException("You cannot set '" + str + "' to an empty string. We interpret empty strings as null in requests. You may set '" + str + "' to null to delete the property.", str, null, 0, null);
    }

    private static void handleAPIError(String str, int i, String str2) throws InvalidRequestException, AuthenticationException, CardException, APIException {
        ErrorParser.StripeError parseError = ErrorParser.parseError(str);
        if (i != 429) {
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
        throw new RateLimitException(parseError.message, parseError.param, str2, Integer.valueOf(i), null);
    }

    private static String urlEncodePair(String str, String str2) throws UnsupportedEncodingException {
        return String.format("%s=%s", urlEncode(str), urlEncode(str2));
    }

    private static String urlEncode(String str) throws UnsupportedEncodingException {
        if (str == null) {
            return null;
        }
        return URLEncoder.encode(str, "UTF-8");
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0028, code lost:
        if (r0 != 1) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x002a, code lost:
        r6 = createPostConnection(r7, r8, r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x003e, code lost:
        throw new com.stripe.android.exception.APIConnectionException(java.lang.String.format("Unrecognized HTTP method %s. This indicates a bug in the Stripe bindings. Please contact support@stripe.com for assistance.", r6));
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static StripeResponse makeURLConnectionRequest(String str, String str2, String str3, RequestOptions requestOptions) throws APIConnectionException {
        String responseBody;
        char c = 65535;
        HttpURLConnection httpURLConnection = null;
        try {
            try {
                int hashCode = str.hashCode();
                if (hashCode != 70454) {
                    if (hashCode == 2461856 && str.equals("POST")) {
                        c = 1;
                    }
                } else if (str.equals("GET")) {
                    c = 0;
                }
                HttpURLConnection createGetConnection = createGetConnection(str2, str3, requestOptions);
                HttpURLConnection httpURLConnection2 = createGetConnection;
                int responseCode = httpURLConnection2.getResponseCode();
                if (responseCode >= 200 && responseCode < 300) {
                    responseBody = getResponseBody(httpURLConnection2.getInputStream());
                } else {
                    responseBody = getResponseBody(httpURLConnection2.getErrorStream());
                }
                StripeResponse stripeResponse = new StripeResponse(responseCode, responseBody, httpURLConnection2.getHeaderFields());
                httpURLConnection2.disconnect();
                return stripeResponse;
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

    private static String getResponseBody(InputStream inputStream) throws IOException {
        String next = new Scanner(inputStream, "UTF-8").useDelimiter("\\A").next();
        inputStream.close();
        return next;
    }

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
}
