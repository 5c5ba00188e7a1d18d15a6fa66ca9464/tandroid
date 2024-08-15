package com.google.firebase.analytics.connector;

import android.os.Bundle;
import java.util.List;
import java.util.Map;
/* compiled from: com.google.firebase:firebase-measurement-connector@@19.0.0 */
/* loaded from: classes.dex */
public interface AnalyticsConnector {

    /* compiled from: com.google.firebase:firebase-measurement-connector@@19.0.0 */
    /* loaded from: classes.dex */
    public static class ConditionalUserProperty {
        public long creationTimestamp;
        public String name;
        public String origin;
        public long timeToLive;
        public String triggerEventName;
        public long triggerTimeout;
        public Object value;
    }

    void clearConditionalUserProperty(String str, String str2, Bundle bundle);

    List<ConditionalUserProperty> getConditionalUserProperties(String str, String str2);

    int getMaxUserProperties(String str);

    Map<String, Object> getUserProperties(boolean z);

    void logEvent(String str, String str2, Bundle bundle);

    void setConditionalUserProperty(ConditionalUserProperty conditionalUserProperty);

    void setUserProperty(String str, String str2, Object obj);
}
