package com.microsoft.appcenter.analytics;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import com.microsoft.appcenter.AbstractAppCenterService;
import com.microsoft.appcenter.Flags;
import com.microsoft.appcenter.analytics.channel.AnalyticsListener;
import com.microsoft.appcenter.analytics.channel.AnalyticsValidator;
import com.microsoft.appcenter.analytics.channel.SessionTracker;
import com.microsoft.appcenter.analytics.ingestion.models.EventLog;
import com.microsoft.appcenter.analytics.ingestion.models.PageLog;
import com.microsoft.appcenter.analytics.ingestion.models.json.EventLogFactory;
import com.microsoft.appcenter.analytics.ingestion.models.json.PageLogFactory;
import com.microsoft.appcenter.analytics.ingestion.models.json.StartSessionLogFactory;
import com.microsoft.appcenter.analytics.ingestion.models.one.json.CommonSchemaEventLogFactory;
import com.microsoft.appcenter.channel.Channel;
import com.microsoft.appcenter.ingestion.models.Log;
import com.microsoft.appcenter.ingestion.models.json.LogFactory;
import com.microsoft.appcenter.ingestion.models.properties.TypedProperty;
import com.microsoft.appcenter.utils.AppCenterLog;
import com.microsoft.appcenter.utils.context.UserIdContext;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public class Analytics extends AbstractAppCenterService {
    @SuppressLint({"StaticFieldLeak"})
    private static Analytics sInstance;
    private Channel.Listener mAnalyticsTransmissionTargetListener;
    private AnalyticsValidator mAnalyticsValidator;
    private boolean mAutoPageTrackingEnabled = false;
    private Context mContext;
    private WeakReference<Activity> mCurrentActivity;
    AnalyticsTransmissionTarget mDefaultTransmissionTarget;
    private final Map<String, LogFactory> mFactories;
    private SessionTracker mSessionTracker;
    private boolean mStartedFromApp;
    private long mTransmissionInterval;
    private final Map<String, AnalyticsTransmissionTarget> mTransmissionTargets;

    @Override // com.microsoft.appcenter.AbstractAppCenterService
    protected String getGroupName() {
        return "group_analytics";
    }

    @Override // com.microsoft.appcenter.AbstractAppCenterService
    protected String getLoggerTag() {
        return "AppCenterAnalytics";
    }

    @Override // com.microsoft.appcenter.AppCenterService
    public String getServiceName() {
        return "Analytics";
    }

    @Override // com.microsoft.appcenter.AbstractAppCenterService, com.microsoft.appcenter.AppCenterService
    public boolean isAppSecretRequired() {
        return false;
    }

    static /* synthetic */ AnalyticsListener access$500(Analytics analytics) {
        analytics.getClass();
        return null;
    }

    private Analytics() {
        HashMap hashMap = new HashMap();
        this.mFactories = hashMap;
        hashMap.put("startSession", new StartSessionLogFactory());
        hashMap.put("page", new PageLogFactory());
        hashMap.put("event", new EventLogFactory());
        hashMap.put("commonSchemaEvent", new CommonSchemaEventLogFactory());
        this.mTransmissionTargets = new HashMap();
        this.mTransmissionInterval = TimeUnit.SECONDS.toMillis(3L);
    }

    public static synchronized Analytics getInstance() {
        Analytics analytics;
        synchronized (Analytics.class) {
            if (sInstance == null) {
                sInstance = new Analytics();
            }
            analytics = sInstance;
        }
        return analytics;
    }

    public static void trackEvent(String str, EventProperties eventProperties) {
        trackEvent(str, eventProperties, 1);
    }

    public static void trackEvent(String str, EventProperties eventProperties, int i) {
        trackEvent(str, eventProperties, null, i);
    }

    static void trackEvent(String str, EventProperties eventProperties, AnalyticsTransmissionTarget analyticsTransmissionTarget, int i) {
        getInstance().trackEventAsync(str, convertProperties(eventProperties), analyticsTransmissionTarget, i);
    }

    private static List<TypedProperty> convertProperties(EventProperties eventProperties) {
        if (eventProperties == null) {
            return null;
        }
        return new ArrayList(eventProperties.getProperties().values());
    }

    private static String generatePageName(Class<?> cls) {
        String simpleName = cls.getSimpleName();
        return (!simpleName.endsWith("Activity") || simpleName.length() <= 8) ? simpleName : simpleName.substring(0, simpleName.length() - 8);
    }

    private AnalyticsTransmissionTarget createAnalyticsTransmissionTarget(String str) {
        final AnalyticsTransmissionTarget analyticsTransmissionTarget = new AnalyticsTransmissionTarget(str, null);
        AppCenterLog.debug("AppCenterAnalytics", "Created transmission target with token " + str);
        postCommandEvenIfDisabled(new Runnable() { // from class: com.microsoft.appcenter.analytics.Analytics.1
            @Override // java.lang.Runnable
            public void run() {
                analyticsTransmissionTarget.initInBackground(Analytics.this.mContext, ((AbstractAppCenterService) Analytics.this).mChannel);
            }
        });
        return analyticsTransmissionTarget;
    }

    @Override // com.microsoft.appcenter.AppCenterService
    public Map<String, LogFactory> getLogFactories() {
        return this.mFactories;
    }

    @Override // com.microsoft.appcenter.AbstractAppCenterService, android.app.Application.ActivityLifecycleCallbacks
    public synchronized void onActivityResumed(final Activity activity) {
        final Runnable runnable = new Runnable() { // from class: com.microsoft.appcenter.analytics.Analytics.2
            @Override // java.lang.Runnable
            public void run() {
                Analytics.this.mCurrentActivity = new WeakReference(activity);
            }
        };
        post(new Runnable() { // from class: com.microsoft.appcenter.analytics.Analytics.3
            @Override // java.lang.Runnable
            public void run() {
                runnable.run();
                Analytics.this.processOnResume(activity);
            }
        }, runnable, runnable);
    }

    @Override // com.microsoft.appcenter.AbstractAppCenterService
    protected long getTriggerInterval() {
        return this.mTransmissionInterval;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processOnResume(Activity activity) {
        SessionTracker sessionTracker = this.mSessionTracker;
        if (sessionTracker != null) {
            sessionTracker.onActivityResumed();
            if (this.mAutoPageTrackingEnabled) {
                queuePage(generatePageName(activity.getClass()), null);
            }
        }
    }

    @Override // com.microsoft.appcenter.AbstractAppCenterService, android.app.Application.ActivityLifecycleCallbacks
    public synchronized void onActivityPaused(Activity activity) {
        final Runnable runnable = new Runnable() { // from class: com.microsoft.appcenter.analytics.Analytics.4
            @Override // java.lang.Runnable
            public void run() {
                Analytics.this.mCurrentActivity = null;
            }
        };
        post(new Runnable() { // from class: com.microsoft.appcenter.analytics.Analytics.5
            @Override // java.lang.Runnable
            public void run() {
                runnable.run();
                if (Analytics.this.mSessionTracker != null) {
                    Analytics.this.mSessionTracker.onActivityPaused();
                }
            }
        }, runnable, runnable);
    }

    @Override // com.microsoft.appcenter.AbstractAppCenterService
    protected Channel.GroupListener getChannelListener() {
        return new Channel.GroupListener() { // from class: com.microsoft.appcenter.analytics.Analytics.6
            @Override // com.microsoft.appcenter.channel.Channel.GroupListener
            public void onBeforeSending(Log log) {
                Analytics.access$500(Analytics.this);
            }

            @Override // com.microsoft.appcenter.channel.Channel.GroupListener
            public void onSuccess(Log log) {
                Analytics.access$500(Analytics.this);
            }

            @Override // com.microsoft.appcenter.channel.Channel.GroupListener
            public void onFailure(Log log, Exception exc) {
                Analytics.access$500(Analytics.this);
            }
        };
    }

    @Override // com.microsoft.appcenter.AbstractAppCenterService
    protected synchronized void applyEnabledState(boolean z) {
        if (z) {
            this.mChannel.addGroup("group_analytics_critical", getTriggerCount(), 3000L, getTriggerMaxParallelRequests(), null, getChannelListener());
            startAppLevelFeatures();
        } else {
            this.mChannel.removeGroup("group_analytics_critical");
            AnalyticsValidator analyticsValidator = this.mAnalyticsValidator;
            if (analyticsValidator != null) {
                this.mChannel.removeListener(analyticsValidator);
                this.mAnalyticsValidator = null;
            }
            SessionTracker sessionTracker = this.mSessionTracker;
            if (sessionTracker != null) {
                this.mChannel.removeListener(sessionTracker);
                this.mSessionTracker.clearSessions();
                this.mSessionTracker = null;
            }
            Channel.Listener listener = this.mAnalyticsTransmissionTargetListener;
            if (listener != null) {
                this.mChannel.removeListener(listener);
                this.mAnalyticsTransmissionTargetListener = null;
            }
        }
    }

    private void startAppLevelFeatures() {
        Activity activity;
        if (this.mStartedFromApp) {
            AnalyticsValidator analyticsValidator = new AnalyticsValidator();
            this.mAnalyticsValidator = analyticsValidator;
            this.mChannel.addListener(analyticsValidator);
            SessionTracker sessionTracker = new SessionTracker(this.mChannel, "group_analytics");
            this.mSessionTracker = sessionTracker;
            this.mChannel.addListener(sessionTracker);
            WeakReference<Activity> weakReference = this.mCurrentActivity;
            if (weakReference != null && (activity = weakReference.get()) != null) {
                processOnResume(activity);
            }
            Channel.Listener channelListener = AnalyticsTransmissionTarget.getChannelListener();
            this.mAnalyticsTransmissionTargetListener = channelListener;
            this.mChannel.addListener(channelListener);
        }
    }

    private void queuePage(String str, Map<String, String> map) {
        PageLog pageLog = new PageLog();
        pageLog.setName(str);
        pageLog.setProperties(map);
        this.mChannel.enqueue(pageLog, "group_analytics", 1);
    }

    private synchronized void trackEventAsync(final String str, final List<TypedProperty> list, final AnalyticsTransmissionTarget analyticsTransmissionTarget, final int i) {
        final String userId = UserIdContext.getInstance().getUserId();
        post(new Runnable() { // from class: com.microsoft.appcenter.analytics.Analytics.8
            @Override // java.lang.Runnable
            public void run() {
                AnalyticsTransmissionTarget analyticsTransmissionTarget2 = analyticsTransmissionTarget;
                if (analyticsTransmissionTarget2 == null) {
                    analyticsTransmissionTarget2 = Analytics.this.mDefaultTransmissionTarget;
                }
                EventLog eventLog = new EventLog();
                if (analyticsTransmissionTarget2 == null) {
                    if (!Analytics.this.mStartedFromApp) {
                        AppCenterLog.error("AppCenterAnalytics", "Cannot track event using Analytics.trackEvent if not started from app, please start from the application or use Analytics.getTransmissionTarget.");
                        return;
                    }
                } else if (analyticsTransmissionTarget2.isEnabled()) {
                    eventLog.addTransmissionTarget(analyticsTransmissionTarget2.getTransmissionTargetToken());
                    eventLog.setTag(analyticsTransmissionTarget2);
                    if (analyticsTransmissionTarget2 == Analytics.this.mDefaultTransmissionTarget) {
                        eventLog.setUserId(userId);
                    }
                } else {
                    AppCenterLog.error("AppCenterAnalytics", "This transmission target is disabled.");
                    return;
                }
                eventLog.setId(UUID.randomUUID());
                eventLog.setName(str);
                eventLog.setTypedProperties(list);
                int persistenceFlag = Flags.getPersistenceFlag(i, true);
                ((AbstractAppCenterService) Analytics.this).mChannel.enqueue(eventLog, persistenceFlag == 2 ? "group_analytics_critical" : "group_analytics", persistenceFlag);
            }
        });
    }

    @Override // com.microsoft.appcenter.AbstractAppCenterService, com.microsoft.appcenter.AppCenterService
    public synchronized void onStarted(Context context, Channel channel, String str, String str2, boolean z) {
        this.mContext = context;
        this.mStartedFromApp = z;
        super.onStarted(context, channel, str, str2, z);
        setDefaultTransmissionTarget(str2);
    }

    @Override // com.microsoft.appcenter.AbstractAppCenterService, com.microsoft.appcenter.AppCenterService
    public void onConfigurationUpdated(String str, String str2) {
        this.mStartedFromApp = true;
        startAppLevelFeatures();
        setDefaultTransmissionTarget(str2);
    }

    private void setDefaultTransmissionTarget(String str) {
        if (str != null) {
            this.mDefaultTransmissionTarget = createAnalyticsTransmissionTarget(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.appcenter.AbstractAppCenterService
    public synchronized void post(Runnable runnable) {
        super.post(runnable);
    }

    void postCommandEvenIfDisabled(Runnable runnable) {
        post(runnable, runnable, runnable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getEnabledPreferenceKeyPrefix() {
        return getEnabledPreferenceKey() + "/";
    }
}
