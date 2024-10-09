package com.microsoft.appcenter.analytics.channel;

import com.microsoft.appcenter.analytics.ingestion.models.EventLog;
import com.microsoft.appcenter.analytics.ingestion.models.LogWithNameAndProperties;
import com.microsoft.appcenter.analytics.ingestion.models.PageLog;
import com.microsoft.appcenter.channel.AbstractChannelListener;
import com.microsoft.appcenter.ingestion.models.Log;
import com.microsoft.appcenter.ingestion.models.properties.BooleanTypedProperty;
import com.microsoft.appcenter.ingestion.models.properties.DateTimeTypedProperty;
import com.microsoft.appcenter.ingestion.models.properties.DoubleTypedProperty;
import com.microsoft.appcenter.ingestion.models.properties.LongTypedProperty;
import com.microsoft.appcenter.ingestion.models.properties.StringTypedProperty;
import com.microsoft.appcenter.ingestion.models.properties.TypedProperty;
import com.microsoft.appcenter.utils.AppCenterLog;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/* loaded from: classes.dex */
public class AnalyticsValidator extends AbstractChannelListener {
    /* JADX WARN: Multi-variable type inference failed */
    private static TypedProperty copyProperty(TypedProperty typedProperty, String str) {
        StringTypedProperty stringTypedProperty;
        String type = typedProperty.getType();
        if ("boolean".equals(type)) {
            BooleanTypedProperty booleanTypedProperty = new BooleanTypedProperty();
            booleanTypedProperty.setValue(((BooleanTypedProperty) typedProperty).getValue());
            stringTypedProperty = booleanTypedProperty;
        } else if ("dateTime".equals(type)) {
            DateTimeTypedProperty dateTimeTypedProperty = new DateTimeTypedProperty();
            dateTimeTypedProperty.setValue(((DateTimeTypedProperty) typedProperty).getValue());
            stringTypedProperty = dateTimeTypedProperty;
        } else if ("double".equals(type)) {
            DoubleTypedProperty doubleTypedProperty = new DoubleTypedProperty();
            doubleTypedProperty.setValue(((DoubleTypedProperty) typedProperty).getValue());
            stringTypedProperty = doubleTypedProperty;
        } else if ("long".equals(type)) {
            LongTypedProperty longTypedProperty = new LongTypedProperty();
            longTypedProperty.setValue(((LongTypedProperty) typedProperty).getValue());
            stringTypedProperty = longTypedProperty;
        } else {
            StringTypedProperty stringTypedProperty2 = new StringTypedProperty();
            stringTypedProperty2.setValue(((StringTypedProperty) typedProperty).getValue());
            stringTypedProperty = stringTypedProperty2;
        }
        stringTypedProperty.setName(str);
        return stringTypedProperty;
    }

    private boolean validateLog(EventLog eventLog) {
        String validateName = validateName(eventLog.getName(), eventLog.getType());
        if (validateName == null) {
            return false;
        }
        validateProperties(eventLog.getTypedProperties());
        eventLog.setName(validateName);
        return true;
    }

    private boolean validateLog(LogWithNameAndProperties logWithNameAndProperties) {
        String validateName = validateName(logWithNameAndProperties.getName(), logWithNameAndProperties.getType());
        if (validateName == null) {
            return false;
        }
        Map validateProperties = validateProperties(logWithNameAndProperties.getProperties(), validateName, logWithNameAndProperties.getType());
        logWithNameAndProperties.setName(validateName);
        logWithNameAndProperties.setProperties(validateProperties);
        return true;
    }

    private static String validateName(String str, String str2) {
        if (str != null && !str.isEmpty()) {
            if (str.length() <= 256) {
                return str;
            }
            AppCenterLog.warn("AppCenterAnalytics", String.format("%s '%s' : name length cannot be longer than %s characters. Name will be truncated.", str2, str, 256));
            return str.substring(0, 256);
        }
        AppCenterLog.error("AppCenterAnalytics", str2 + " name cannot be null or empty.");
        return null;
    }

    private static Map validateProperties(Map map, String str, String str2) {
        String format;
        if (map == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        Iterator it = map.entrySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Map.Entry entry = (Map.Entry) it.next();
            String str3 = (String) entry.getKey();
            String str4 = (String) entry.getValue();
            if (hashMap.size() >= 20) {
                AppCenterLog.warn("AppCenterAnalytics", String.format("%s '%s' : properties cannot contain more than %s items. Skipping other properties.", str2, str, 20));
                break;
            }
            if (str3 == null || str3.isEmpty()) {
                format = String.format("%s '%s' : a property key cannot be null or empty. Property will be skipped.", str2, str);
            } else if (str4 == null) {
                format = String.format("%s '%s' : property '%s' : property value cannot be null. Property '%s' will be skipped.", str2, str, str3, str3);
            } else {
                if (str3.length() > 125) {
                    AppCenterLog.warn("AppCenterAnalytics", String.format("%s '%s' : property '%s' : property key length cannot be longer than %s characters. Property key will be truncated.", str2, str, str3, 125));
                    str3 = str3.substring(0, 125);
                }
                if (str4.length() > 125) {
                    AppCenterLog.warn("AppCenterAnalytics", String.format("%s '%s' : property '%s' : property value cannot be longer than %s characters. Property value will be truncated.", str2, str, str3, 125));
                    str4 = str4.substring(0, 125);
                }
                hashMap.put(str3, str4);
            }
            AppCenterLog.warn("AppCenterAnalytics", format);
        }
        return hashMap;
    }

    private static void validateProperties(List list) {
        String str;
        boolean z;
        if (list == null) {
            return;
        }
        ListIterator listIterator = list.listIterator();
        int i = 0;
        boolean z2 = false;
        while (listIterator.hasNext()) {
            TypedProperty typedProperty = (TypedProperty) listIterator.next();
            String name = typedProperty.getName();
            if (i < 20) {
                if (name == null || name.isEmpty()) {
                    str = "A typed property key cannot be null or empty. Property will be skipped.";
                } else {
                    if (name.length() > 125) {
                        AppCenterLog.warn("AppCenterAnalytics", String.format("Typed property '%s' : property key length cannot be longer than %s characters. Property key will be truncated.", name, 125));
                        name = name.substring(0, 125);
                        typedProperty = copyProperty(typedProperty, name);
                        listIterator.set(typedProperty);
                        z = false;
                    } else {
                        z = true;
                    }
                    if (typedProperty instanceof StringTypedProperty) {
                        StringTypedProperty stringTypedProperty = (StringTypedProperty) typedProperty;
                        String value = stringTypedProperty.getValue();
                        if (value == null) {
                            str = String.format("Typed property '%s' : property value cannot be null. Property '%s' will be skipped.", name, name);
                        } else if (value.length() > 125) {
                            AppCenterLog.warn("AppCenterAnalytics", String.format("A String property '%s' : property value cannot be longer than %s characters. Property value will be truncated.", name, 125));
                            String substring = value.substring(0, 125);
                            if (z) {
                                StringTypedProperty stringTypedProperty2 = new StringTypedProperty();
                                stringTypedProperty2.setName(name);
                                stringTypedProperty2.setValue(substring);
                                listIterator.set(stringTypedProperty2);
                            } else {
                                stringTypedProperty.setValue(substring);
                            }
                        }
                    }
                    i++;
                }
                AppCenterLog.warn("AppCenterAnalytics", str);
            } else if (!z2) {
                AppCenterLog.warn("AppCenterAnalytics", String.format("Typed properties cannot contain more than %s items. Skipping other properties.", 20));
                z2 = true;
            }
            listIterator.remove();
        }
    }

    @Override // com.microsoft.appcenter.channel.AbstractChannelListener, com.microsoft.appcenter.channel.Channel.Listener
    public boolean shouldFilter(Log log) {
        if (log instanceof PageLog) {
            return !validateLog((LogWithNameAndProperties) log);
        }
        if (log instanceof EventLog) {
            return !validateLog((EventLog) log);
        }
        return false;
    }
}
