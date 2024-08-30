package com.microsoft.appcenter.analytics;

import com.microsoft.appcenter.ingestion.models.properties.BooleanTypedProperty;
import com.microsoft.appcenter.ingestion.models.properties.StringTypedProperty;
import com.microsoft.appcenter.utils.AppCenterLog;
import j$.util.concurrent.ConcurrentHashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class EventProperties {
    private final Map mProperties = new ConcurrentHashMap();

    private boolean isValidKey(String str) {
        if (str == null) {
            AppCenterLog.error("AppCenterAnalytics", "Property key must not be null");
            return false;
        } else if (this.mProperties.containsKey(str)) {
            AppCenterLog.warn("AppCenterAnalytics", "Property \"" + str + "\" is already set and will be overridden.");
            return true;
        } else {
            return true;
        }
    }

    private boolean isValidValue(Object obj) {
        if (obj == null) {
            AppCenterLog.error("AppCenterAnalytics", "Property value cannot be null");
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Map getProperties() {
        return this.mProperties;
    }

    public EventProperties set(String str, String str2) {
        if (isValidKey(str) && isValidValue(str2)) {
            StringTypedProperty stringTypedProperty = new StringTypedProperty();
            stringTypedProperty.setName(str);
            stringTypedProperty.setValue(str2);
            this.mProperties.put(str, stringTypedProperty);
        }
        return this;
    }

    public EventProperties set(String str, boolean z) {
        if (isValidKey(str)) {
            BooleanTypedProperty booleanTypedProperty = new BooleanTypedProperty();
            booleanTypedProperty.setName(str);
            booleanTypedProperty.setValue(z);
            this.mProperties.put(str, booleanTypedProperty);
        }
        return this;
    }
}
