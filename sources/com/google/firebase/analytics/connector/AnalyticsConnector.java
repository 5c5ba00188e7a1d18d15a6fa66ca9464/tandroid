package com.google.firebase.analytics.connector;

/* loaded from: classes.dex */
public interface AnalyticsConnector {

    public static class ConditionalUserProperty {
        public long creationTimestamp;
        public String name;
        public String origin;
        public long timeToLive;
        public String triggerEventName;
        public long triggerTimeout;
        public Object value;
    }
}
