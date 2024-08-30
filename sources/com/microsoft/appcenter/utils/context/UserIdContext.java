package com.microsoft.appcenter.utils.context;

import android.text.TextUtils;
import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import com.microsoft.appcenter.utils.AppCenterLog;
import j$.util.concurrent.ConcurrentHashMap;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
/* loaded from: classes.dex */
public class UserIdContext {
    private static UserIdContext sInstance;
    private final Set mListeners = Collections.newSetFromMap(new ConcurrentHashMap());
    private String mUserId;

    public static boolean checkUserIdValidForAppCenter(String str) {
        if (str == null || str.length() <= 256) {
            return true;
        }
        AppCenterLog.error("AppCenter", "userId is limited to 256 characters.");
        return false;
    }

    public static boolean checkUserIdValidForOneCollector(String str) {
        if (str == null) {
            return true;
        }
        if (str.isEmpty()) {
            AppCenterLog.error("AppCenter", "userId must not be empty.");
            return false;
        }
        int indexOf = str.indexOf(":");
        if (indexOf >= 0) {
            String substring = str.substring(0, indexOf);
            if (!substring.equals("c")) {
                AppCenterLog.error("AppCenter", String.format("userId prefix must be '%s%s', '%s%s' is not supported.", "c", ":", substring, ":"));
                return false;
            } else if (indexOf == str.length() - 1) {
                AppCenterLog.error("AppCenter", "userId must not be empty.");
                return false;
            }
        }
        return true;
    }

    public static synchronized UserIdContext getInstance() {
        UserIdContext userIdContext;
        synchronized (UserIdContext.class) {
            try {
                if (sInstance == null) {
                    sInstance = new UserIdContext();
                }
                userIdContext = sInstance;
            } catch (Throwable th) {
                throw th;
            }
        }
        return userIdContext;
    }

    public static String getPrefixedUserId(String str) {
        if (str == null || str.contains(":")) {
            return str;
        }
        return "c:" + str;
    }

    private synchronized boolean updateUserId(String str) {
        if (TextUtils.equals(this.mUserId, str)) {
            return false;
        }
        this.mUserId = str;
        return true;
    }

    public synchronized String getUserId() {
        return this.mUserId;
    }

    public void setUserId(String str) {
        if (updateUserId(str)) {
            Iterator it = this.mListeners.iterator();
            if (it.hasNext()) {
                ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(it.next());
                throw null;
            }
        }
    }
}
