package com.microsoft.appcenter;

import com.microsoft.appcenter.utils.AppCenterLog;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class CustomProperties {
    private static final Pattern KEY_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9]*$");
    private final Map mProperties = new HashMap();

    private void addProperty(String str, Object obj) {
        if (this.mProperties.containsKey(str) || this.mProperties.size() < 60) {
            this.mProperties.put(str, obj);
        } else {
            AppCenterLog.error("AppCenter", "Custom properties cannot contain more than 60 items");
        }
    }

    private boolean isValidKey(String str) {
        StringBuilder sb;
        String str2;
        if (str == null || !KEY_PATTERN.matcher(str).matches()) {
            sb = new StringBuilder();
            sb.append("Custom property \"");
            sb.append(str);
            sb.append("\" must match \"");
            sb.append(KEY_PATTERN);
            str2 = "\"";
        } else {
            if (str.length() <= 128) {
                if (!this.mProperties.containsKey(str)) {
                    return true;
                }
                AppCenterLog.warn("AppCenter", "Custom property \"" + str + "\" is already set or cleared and will be overridden.");
                return true;
            }
            sb = new StringBuilder();
            sb.append("Custom property \"");
            sb.append(str);
            sb.append("\" length cannot be longer than ");
            sb.append(128);
            str2 = " characters.";
        }
        sb.append(str2);
        AppCenterLog.error("AppCenter", sb.toString());
        return false;
    }

    private boolean isValidStringValue(String str, String str2) {
        String str3;
        if (str2 == null) {
            str3 = "Custom property value cannot be null, did you mean to call clear?";
        } else {
            if (str2.length() <= 128) {
                return true;
            }
            str3 = "Custom property \"" + str + "\" value length cannot be longer than 128 characters.";
        }
        AppCenterLog.error("AppCenter", str3);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Map getProperties() {
        return new HashMap(this.mProperties);
    }

    public synchronized CustomProperties set(String str, String str2) {
        if (isValidKey(str) && isValidStringValue(str, str2)) {
            addProperty(str, str2);
        }
        return this;
    }
}
