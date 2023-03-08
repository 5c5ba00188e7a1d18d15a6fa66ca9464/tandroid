package org.telegram.messenger;

import android.app.Activity;
import android.os.SystemClock;
import android.text.TextUtils;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.crashes.Crashes;
import com.microsoft.appcenter.distribute.Distribute;
import org.telegram.tgnet.TLRPC$User;
/* loaded from: classes.dex */
public class ApplicationLoaderImpl extends ApplicationLoader {
    private static long lastUpdateCheckTime;

    @Override // org.telegram.messenger.ApplicationLoader
    protected String onGetApplicationId() {
        return "org.telegram.messenger.beta";
    }

    @Override // org.telegram.messenger.ApplicationLoader
    protected void startAppCenterInternal(Activity activity) {
        try {
            if (BuildVars.DEBUG_VERSION) {
                Distribute.setEnabledForDebuggableBuild(true);
                if (TextUtils.isEmpty(BuildConfig.APP_CENTER_HASH)) {
                    throw new RuntimeException("App Center hash is empty. add to local.properties field APP_CENTER_HASH_PRIVATE and APP_CENTER_HASH_PUBLIC");
                }
                AppCenter.start(activity.getApplication(), BuildConfig.APP_CENTER_HASH, Distribute.class, Crashes.class);
                Crashes.getMinidumpDirectory().thenAccept(ApplicationLoaderImpl$$ExternalSyntheticLambda0.INSTANCE);
                String str = "uid=" + UserConfig.getInstance(UserConfig.selectedAccount).clientUserId;
                TLRPC$User currentUser = UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser();
                if (currentUser != null && !TextUtils.isEmpty(currentUser.username)) {
                    str = str + " uname=" + currentUser.username;
                }
                AppCenter.setUserId(str);
            }
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$startAppCenterInternal$0(String str) {
        if (str != null) {
            Utilities.setupNativeCrashesListener(str);
        }
    }

    @Override // org.telegram.messenger.ApplicationLoader
    protected void checkForUpdatesInternal() {
        try {
            if (!BuildVars.DEBUG_VERSION || SystemClock.elapsedRealtime() - lastUpdateCheckTime < 3600000) {
                return;
            }
            lastUpdateCheckTime = SystemClock.elapsedRealtime();
            Distribute.checkForUpdate();
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    @Override // org.telegram.messenger.ApplicationLoader
    protected void appCenterLogInternal(Throwable th) {
        try {
            Crashes.trackError(th);
        } catch (Throwable unused) {
        }
    }
}
