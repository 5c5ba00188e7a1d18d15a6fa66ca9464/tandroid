package com.microsoft.appcenter.http;

import android.net.TrafficStats;
import android.os.AsyncTask;
import com.microsoft.appcenter.http.HttpClient;
import com.microsoft.appcenter.utils.AppCenterLog;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class DefaultHttpClientCallTask extends AsyncTask {
    private final HttpClient.CallTemplate mCallTemplate;
    private final boolean mCompressionEnabled;
    private final Map mHeaders;
    private final String mMethod;
    private final ServiceCallback mServiceCallback;
    private final Tracker mTracker;
    private final String mUrl;
    private static final Pattern TOKEN_REGEX_URL_ENCODED = Pattern.compile("token=[^&]+");
    private static final Pattern TOKEN_REGEX_JSON = Pattern.compile("token\":\"[^\"]+\"");
    private static final Pattern REDIRECT_URI_REGEX_JSON = Pattern.compile("redirect_uri\":\"[^\"]+\"");

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface Tracker {
        void onFinish(DefaultHttpClientCallTask defaultHttpClientCallTask);

        void onStart(DefaultHttpClientCallTask defaultHttpClientCallTask);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DefaultHttpClientCallTask(String str, String str2, Map map, HttpClient.CallTemplate callTemplate, ServiceCallback serviceCallback, Tracker tracker, boolean z) {
        this.mUrl = str;
        this.mMethod = str2;
        this.mHeaders = map;
        this.mCallTemplate = callTemplate;
        this.mServiceCallback = serviceCallback;
        this.mTracker = tracker;
        this.mCompressionEnabled = z;
    }

    private HttpResponse doHttpCall() {
        String str;
        byte[] bArr;
        String replaceAll;
        HttpClient.CallTemplate callTemplate;
        URL url = new URL(this.mUrl);
        HttpsURLConnection createHttpsConnection = HttpUtils.createHttpsConnection(url);
        try {
            createHttpsConnection.setRequestMethod(this.mMethod);
            boolean z = false;
            if (!this.mMethod.equals("POST") || (callTemplate = this.mCallTemplate) == null) {
                str = null;
                bArr = null;
            } else {
                str = callTemplate.buildRequestBody();
                bArr = str.getBytes("UTF-8");
                if (this.mCompressionEnabled && bArr.length >= 1400) {
                    z = true;
                }
                if (!this.mHeaders.containsKey("Content-Type")) {
                    this.mHeaders.put("Content-Type", "application/json");
                }
            }
            if (z) {
                this.mHeaders.put("Content-Encoding", "gzip");
            }
            for (Map.Entry entry : this.mHeaders.entrySet()) {
                createHttpsConnection.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
            }
            if (isCancelled()) {
                createHttpsConnection.disconnect();
                return null;
            }
            HttpClient.CallTemplate callTemplate2 = this.mCallTemplate;
            if (callTemplate2 != null) {
                callTemplate2.onBeforeCalling(url, this.mHeaders);
            }
            if (bArr != null) {
                if (AppCenterLog.getLogLevel() <= 2) {
                    if (str.length() < 4096) {
                        str = TOKEN_REGEX_URL_ENCODED.matcher(str).replaceAll("token=***");
                        if ("application/json".equals(this.mHeaders.get("Content-Type"))) {
                            str = new JSONObject(str).toString(2);
                        }
                    }
                    AppCenterLog.verbose("AppCenter", str);
                }
                if (z) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bArr.length);
                    GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
                    gZIPOutputStream.write(bArr);
                    gZIPOutputStream.close();
                    bArr = byteArrayOutputStream.toByteArray();
                }
                createHttpsConnection.setDoOutput(true);
                createHttpsConnection.setFixedLengthStreamingMode(bArr.length);
                OutputStream outputStream = createHttpsConnection.getOutputStream();
                try {
                    writePayload(outputStream, bArr);
                    outputStream.close();
                } catch (Throwable th) {
                    outputStream.close();
                    throw th;
                }
            }
            if (isCancelled()) {
                createHttpsConnection.disconnect();
                return null;
            }
            int responseCode = createHttpsConnection.getResponseCode();
            String readResponse = readResponse(createHttpsConnection);
            if (AppCenterLog.getLogLevel() <= 2) {
                String headerField = createHttpsConnection.getHeaderField("Content-Type");
                if (headerField != null && !headerField.startsWith("text/") && !headerField.startsWith("application/")) {
                    replaceAll = "<binary>";
                    AppCenterLog.verbose("AppCenter", "HTTP response status=" + responseCode + " payload=" + replaceAll);
                }
                replaceAll = REDIRECT_URI_REGEX_JSON.matcher(TOKEN_REGEX_JSON.matcher(readResponse).replaceAll("token\":\"***\"")).replaceAll("redirect_uri\":\"***\"");
                AppCenterLog.verbose("AppCenter", "HTTP response status=" + responseCode + " payload=" + replaceAll);
            }
            HashMap hashMap = new HashMap();
            for (Map.Entry<String, List<String>> entry2 : createHttpsConnection.getHeaderFields().entrySet()) {
                hashMap.put(entry2.getKey(), entry2.getValue().iterator().next());
            }
            HttpResponse httpResponse = new HttpResponse(responseCode, readResponse, hashMap);
            if (responseCode < 200 || responseCode >= 300) {
                throw new HttpException(httpResponse);
            }
            createHttpsConnection.disconnect();
            return httpResponse;
        } catch (Throwable th2) {
            createHttpsConnection.disconnect();
            throw th2;
        }
    }

    private static InputStream getInputStream(HttpsURLConnection httpsURLConnection) {
        int responseCode = httpsURLConnection.getResponseCode();
        return (responseCode < 200 || responseCode >= 400) ? httpsURLConnection.getErrorStream() : httpsURLConnection.getInputStream();
    }

    private String readResponse(HttpsURLConnection httpsURLConnection) {
        StringBuilder sb = new StringBuilder(Math.max(httpsURLConnection.getContentLength(), 16));
        InputStream inputStream = getInputStream(httpsURLConnection);
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            char[] cArr = new char[1024];
            do {
                int read = inputStreamReader.read(cArr);
                if (read <= 0) {
                    break;
                }
                sb.append(cArr, 0, read);
            } while (!isCancelled());
            String sb2 = sb.toString();
            inputStream.close();
            return sb2;
        } catch (Throwable th) {
            inputStream.close();
            throw th;
        }
    }

    private void writePayload(OutputStream outputStream, byte[] bArr) {
        for (int i = 0; i < bArr.length; i += 1024) {
            outputStream.write(bArr, i, Math.min(bArr.length - i, 1024));
            if (isCancelled()) {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Object doInBackground(Void... voidArr) {
        TrafficStats.setThreadStatsTag(-667034599);
        try {
            return doHttpCall();
        } catch (Exception e) {
            return e;
        } finally {
            TrafficStats.clearThreadStatsTag();
        }
    }

    @Override // android.os.AsyncTask
    protected void onCancelled(Object obj) {
        if ((obj instanceof HttpResponse) || (obj instanceof HttpException)) {
            onPostExecute(obj);
        } else {
            this.mTracker.onFinish(this);
        }
    }

    @Override // android.os.AsyncTask
    protected void onPostExecute(Object obj) {
        this.mTracker.onFinish(this);
        if (obj instanceof Exception) {
            this.mServiceCallback.onCallFailed((Exception) obj);
        } else {
            this.mServiceCallback.onCallSucceeded((HttpResponse) obj);
        }
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        this.mTracker.onStart(this);
    }
}
