package com.microsoft.appcenter.http;

import java.util.Map;

/* loaded from: classes.dex */
public class HttpResponse {
    private final Map headers;
    private final String payload;
    private final int statusCode;

    public HttpResponse(int i, String str, Map map) {
        this.payload = str;
        this.statusCode = i;
        this.headers = map;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        HttpResponse httpResponse = (HttpResponse) obj;
        return this.statusCode == httpResponse.statusCode && this.payload.equals(httpResponse.payload) && this.headers.equals(httpResponse.headers);
    }

    public Map getHeaders() {
        return this.headers;
    }

    public String getPayload() {
        return this.payload;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public int hashCode() {
        return (((this.statusCode * 31) + this.payload.hashCode()) * 31) + this.headers.hashCode();
    }
}
