package com.google.android.exoplayer2.drm;

import android.net.Uri;
import android.text.TextUtils;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.drm.ExoMediaDrm;
import com.google.android.exoplayer2.upstream.DataSourceInputStream;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import com.microsoft.appcenter.http.DefaultHttpClient;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/* loaded from: classes3.dex */
public final class HttpMediaDrmCallback implements MediaDrmCallback {
    private static final int MAX_MANUAL_REDIRECTS = 5;
    private final HttpDataSource.Factory dataSourceFactory;
    private final String defaultLicenseUrl;
    private final boolean forceDefaultLicenseUrl;
    private final Map<String, String> keyRequestProperties;

    public HttpMediaDrmCallback(String defaultLicenseUrl, HttpDataSource.Factory dataSourceFactory) {
        this(defaultLicenseUrl, false, dataSourceFactory);
    }

    public HttpMediaDrmCallback(String defaultLicenseUrl, boolean forceDefaultLicenseUrl, HttpDataSource.Factory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
        this.defaultLicenseUrl = defaultLicenseUrl;
        this.forceDefaultLicenseUrl = forceDefaultLicenseUrl;
        this.keyRequestProperties = new HashMap();
    }

    public void setKeyRequestProperty(String name, String value) {
        Assertions.checkNotNull(name);
        Assertions.checkNotNull(value);
        synchronized (this.keyRequestProperties) {
            this.keyRequestProperties.put(name, value);
        }
    }

    public void clearKeyRequestProperty(String name) {
        Assertions.checkNotNull(name);
        synchronized (this.keyRequestProperties) {
            this.keyRequestProperties.remove(name);
        }
    }

    public void clearAllKeyRequestProperties() {
        synchronized (this.keyRequestProperties) {
            this.keyRequestProperties.clear();
        }
    }

    @Override // com.google.android.exoplayer2.drm.MediaDrmCallback
    public byte[] executeProvisionRequest(UUID uuid, ExoMediaDrm.ProvisionRequest request) throws IOException {
        String url = request.getDefaultUrl() + "&signedRequest=" + Util.fromUtf8Bytes(request.getData());
        return executePost(this.dataSourceFactory, url, null, null);
    }

    @Override // com.google.android.exoplayer2.drm.MediaDrmCallback
    public byte[] executeKeyRequest(UUID uuid, ExoMediaDrm.KeyRequest request) throws Exception {
        String contentType;
        String url = request.getLicenseServerUrl();
        if (this.forceDefaultLicenseUrl || TextUtils.isEmpty(url)) {
            url = this.defaultLicenseUrl;
        }
        Map<String, String> requestProperties = new HashMap<>();
        if (C.PLAYREADY_UUID.equals(uuid)) {
            contentType = "text/xml";
        } else {
            contentType = C.CLEARKEY_UUID.equals(uuid) ? "application/json" : "application/octet-stream";
        }
        requestProperties.put(DefaultHttpClient.CONTENT_TYPE_KEY, contentType);
        if (C.PLAYREADY_UUID.equals(uuid)) {
            requestProperties.put("SOAPAction", "http://schemas.microsoft.com/DRM/2007/03/protocols/AcquireLicense");
        }
        synchronized (this.keyRequestProperties) {
            requestProperties.putAll(this.keyRequestProperties);
        }
        return executePost(this.dataSourceFactory, url, request.getData(), requestProperties);
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x0077  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x007a A[LOOP:1: B:9:0x002e->B:32:0x007a, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x006f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0082 A[EDGE_INSN: B:43:0x0082->B:33:0x0082 ?: BREAK  , SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static byte[] executePost(HttpDataSource.Factory dataSourceFactory, String url, byte[] httpBody, Map<String, String> requestProperties) throws IOException {
        HttpDataSource.InvalidResponseCodeException e;
        int manualRedirectCount;
        int manualRedirectCount2;
        String redirectUrl;
        HttpDataSource dataSource = dataSourceFactory.createDataSource();
        if (requestProperties != null) {
            for (Map.Entry<String, String> requestProperty : requestProperties.entrySet()) {
                dataSource.setRequestProperty(requestProperty.getKey(), requestProperty.getValue());
            }
        }
        String url2 = url;
        int manualRedirectCount3 = 0;
        while (true) {
            DataSpec dataSpec = new DataSpec(Uri.parse(url2), 2, httpBody, 0L, 0L, -1L, null, 1);
            DataSourceInputStream inputStream = new DataSourceInputStream(dataSource, dataSpec);
            try {
                byte[] byteArray = Util.toByteArray(inputStream);
                Util.closeQuietly(inputStream);
                return byteArray;
            } catch (HttpDataSource.InvalidResponseCodeException e2) {
                try {
                    if (e2.responseCode == 307 || e2.responseCode == 308) {
                        manualRedirectCount = manualRedirectCount3 + 1;
                        if (manualRedirectCount3 < 5) {
                            manualRedirectCount2 = 1;
                            if (manualRedirectCount2 == 0) {
                                try {
                                    redirectUrl = getRedirectUrl(e2);
                                } catch (Throwable th) {
                                    e = th;
                                    Util.closeQuietly(inputStream);
                                    throw e;
                                }
                            } else {
                                redirectUrl = null;
                            }
                            if (redirectUrl != null) {
                                throw e2;
                            }
                            url2 = redirectUrl;
                            Util.closeQuietly(inputStream);
                            manualRedirectCount3 = manualRedirectCount;
                        } else {
                            manualRedirectCount3 = manualRedirectCount;
                        }
                    }
                    manualRedirectCount = manualRedirectCount3;
                    manualRedirectCount2 = 0;
                    if (manualRedirectCount2 == 0) {
                    }
                    if (redirectUrl != null) {
                    }
                } catch (Throwable th2) {
                    e = th2;
                }
            }
        }
    }

    private static String getRedirectUrl(HttpDataSource.InvalidResponseCodeException exception) {
        List<String> locationHeaders;
        Map<String, List<String>> headerFields = exception.headerFields;
        if (headerFields != null && (locationHeaders = headerFields.get("Location")) != null && !locationHeaders.isEmpty()) {
            return locationHeaders.get(0);
        }
        return null;
    }
}
