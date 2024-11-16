package com.microsoft.appcenter.http;

import java.io.Closeable;
import java.net.URL;
import java.util.Map;

/* loaded from: classes.dex */
public interface HttpClient extends Closeable {

    public interface CallTemplate {
        String buildRequestBody();

        void onBeforeCalling(URL url, Map map);
    }

    ServiceCall callAsync(String str, String str2, Map map, CallTemplate callTemplate, ServiceCallback serviceCallback);

    void reopen();
}
