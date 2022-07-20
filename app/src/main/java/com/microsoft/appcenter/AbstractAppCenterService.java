package com.microsoft.appcenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.microsoft.appcenter.channel.Channel;
import com.microsoft.appcenter.utils.AppCenterLog;
import com.microsoft.appcenter.utils.async.AppCenterFuture;
import com.microsoft.appcenter.utils.async.DefaultAppCenterFuture;
import com.microsoft.appcenter.utils.storage.SharedPreferencesManager;
/* loaded from: classes.dex */
public abstract class AbstractAppCenterService implements AppCenterService {
    protected Channel mChannel;
    private AppCenterHandler mHandler;

    protected synchronized void applyEnabledState(boolean z) {
        throw null;
    }

    protected Channel.GroupListener getChannelListener() {
        return null;
    }

    protected abstract String getGroupName();

    protected abstract String getLoggerTag();

    protected abstract int getTriggerCount();

    protected long getTriggerInterval() {
        return 3000L;
    }

    protected int getTriggerMaxParallelRequests() {
        return 3;
    }

    @Override // com.microsoft.appcenter.AppCenterService
    public boolean isAppSecretRequired() {
        return true;
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityDestroyed(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPaused(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityResumed(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStarted(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStopped(Activity activity) {
    }

    @Override // com.microsoft.appcenter.utils.ApplicationLifecycleListener.ApplicationLifecycleCallbacks
    public void onApplicationEnterBackground() {
    }

    @Override // com.microsoft.appcenter.utils.ApplicationLifecycleListener.ApplicationLifecycleCallbacks
    public void onApplicationEnterForeground() {
    }

    @Override // com.microsoft.appcenter.AppCenterService
    public void onConfigurationUpdated(String str, String str2) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.microsoft.appcenter.AbstractAppCenterService$1 */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements Runnable {
        final /* synthetic */ DefaultAppCenterFuture val$future;

        AnonymousClass1(AbstractAppCenterService abstractAppCenterService, DefaultAppCenterFuture defaultAppCenterFuture) {
            this.val$future = defaultAppCenterFuture;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.val$future.complete(Boolean.TRUE);
        }
    }

    public synchronized AppCenterFuture<Boolean> isInstanceEnabledAsync() {
        DefaultAppCenterFuture defaultAppCenterFuture;
        defaultAppCenterFuture = new DefaultAppCenterFuture();
        postAsyncGetter(new AnonymousClass1(this, defaultAppCenterFuture), defaultAppCenterFuture, Boolean.FALSE);
        return defaultAppCenterFuture;
    }

    @Override // com.microsoft.appcenter.AppCenterService
    public synchronized boolean isInstanceEnabled() {
        return SharedPreferencesManager.getBoolean(getEnabledPreferenceKey(), true);
    }

    @Override // com.microsoft.appcenter.AppCenterService
    public synchronized void setInstanceEnabled(boolean z) {
        if (z == isInstanceEnabled()) {
            String loggerTag = getLoggerTag();
            Object[] objArr = new Object[2];
            objArr[0] = getServiceName();
            objArr[1] = z ? "enabled" : "disabled";
            AppCenterLog.info(loggerTag, String.format("%s service has already been %s.", objArr));
            return;
        }
        String groupName = getGroupName();
        Channel channel = this.mChannel;
        if (channel != null && groupName != null) {
            if (z) {
                channel.addGroup(groupName, getTriggerCount(), getTriggerInterval(), getTriggerMaxParallelRequests(), null, getChannelListener());
            } else {
                channel.clear(groupName);
                this.mChannel.removeGroup(groupName);
            }
        }
        SharedPreferencesManager.putBoolean(getEnabledPreferenceKey(), z);
        String loggerTag2 = getLoggerTag();
        Object[] objArr2 = new Object[2];
        objArr2[0] = getServiceName();
        objArr2[1] = z ? "enabled" : "disabled";
        AppCenterLog.info(loggerTag2, String.format("%s service has been %s.", objArr2));
        if (this.mChannel != null) {
            applyEnabledState(z);
        }
    }

    @Override // com.microsoft.appcenter.AppCenterService
    public final synchronized void onStarting(AppCenterHandler appCenterHandler) {
        this.mHandler = appCenterHandler;
    }

    @Override // com.microsoft.appcenter.AppCenterService
    public synchronized void onStarted(Context context, Channel channel, String str, String str2, boolean z) {
        String groupName = getGroupName();
        boolean isInstanceEnabled = isInstanceEnabled();
        if (groupName != null) {
            channel.removeGroup(groupName);
            if (isInstanceEnabled) {
                channel.addGroup(groupName, getTriggerCount(), getTriggerInterval(), getTriggerMaxParallelRequests(), null, getChannelListener());
            } else {
                channel.clear(groupName);
            }
        }
        this.mChannel = channel;
        applyEnabledState(isInstanceEnabled);
    }

    protected String getEnabledPreferenceKey() {
        return "enabled_" + getServiceName();
    }

    public synchronized void post(Runnable runnable) {
        post(runnable, null, null);
    }

    protected synchronized boolean post(Runnable runnable, Runnable runnable2, Runnable runnable3) {
        AppCenterHandler appCenterHandler = this.mHandler;
        if (appCenterHandler == null) {
            AppCenterLog.error("AppCenter", getServiceName() + " needs to be started before it can be used.");
            return false;
        }
        appCenterHandler.post(new AnonymousClass4(runnable, runnable3), runnable2);
        return true;
    }

    /* renamed from: com.microsoft.appcenter.AbstractAppCenterService$4 */
    /* loaded from: classes.dex */
    public class AnonymousClass4 implements Runnable {
        final /* synthetic */ Runnable val$runnable;
        final /* synthetic */ Runnable val$serviceDisabledRunnable;

        AnonymousClass4(Runnable runnable, Runnable runnable2) {
            AbstractAppCenterService.this = r1;
            this.val$runnable = runnable;
            this.val$serviceDisabledRunnable = runnable2;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (AbstractAppCenterService.this.isInstanceEnabled()) {
                this.val$runnable.run();
                return;
            }
            Runnable runnable = this.val$serviceDisabledRunnable;
            if (runnable != null) {
                runnable.run();
                return;
            }
            AppCenterLog.info("AppCenter", AbstractAppCenterService.this.getServiceName() + " service disabled, discarding calls.");
        }
    }

    /* renamed from: com.microsoft.appcenter.AbstractAppCenterService$5 */
    /* loaded from: classes.dex */
    public class AnonymousClass5 implements Runnable {
        final /* synthetic */ DefaultAppCenterFuture val$future;
        final /* synthetic */ Object val$valueIfDisabledOrNotStarted;

        AnonymousClass5(AbstractAppCenterService abstractAppCenterService, DefaultAppCenterFuture defaultAppCenterFuture, Object obj) {
            this.val$future = defaultAppCenterFuture;
            this.val$valueIfDisabledOrNotStarted = obj;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.val$future.complete(this.val$valueIfDisabledOrNotStarted);
        }
    }

    protected synchronized <T> void postAsyncGetter(Runnable runnable, DefaultAppCenterFuture<T> defaultAppCenterFuture, T t) {
        AnonymousClass5 anonymousClass5 = new AnonymousClass5(this, defaultAppCenterFuture, t);
        if (!post(new AnonymousClass6(this, runnable), anonymousClass5, anonymousClass5)) {
            anonymousClass5.run();
        }
    }

    /* renamed from: com.microsoft.appcenter.AbstractAppCenterService$6 */
    /* loaded from: classes.dex */
    public class AnonymousClass6 implements Runnable {
        final /* synthetic */ Runnable val$runnable;

        AnonymousClass6(AbstractAppCenterService abstractAppCenterService, Runnable runnable) {
            this.val$runnable = runnable;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.val$runnable.run();
        }
    }
}
