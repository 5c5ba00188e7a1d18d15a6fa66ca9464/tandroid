package com.microsoft.appcenter.analytics;

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
    private static Analytics sInstance;
    private Channel.Listener mAnalyticsTransmissionTargetListener;
    private AnalyticsValidator mAnalyticsValidator;
    private boolean mAutoPageTrackingEnabled = false;
    private Context mContext;
    private WeakReference mCurrentActivity;
    AnalyticsTransmissionTarget mDefaultTransmissionTarget;
    private final Map mFactories;
    private SessionTracker mSessionTracker;
    private boolean mStartedFromApp;
    private long mTransmissionInterval;
    private final Map mTransmissionTargets;

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

    static /* synthetic */ AnalyticsListener access$500(Analytics analytics) {
        analytics.getClass();
        return null;
    }

    private static List convertProperties(EventProperties eventProperties) {
        if (eventProperties == null) {
            return null;
        }
        return new ArrayList(eventProperties.getProperties().values());
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

    private static String generatePageName(Class cls) {
        String simpleName = cls.getSimpleName();
        return (!simpleName.endsWith("Activity") || simpleName.length() <= 8) ? simpleName : simpleName.substring(0, simpleName.length() - 8);
    }

    public static synchronized Analytics getInstance() {
        Analytics analytics;
        synchronized (Analytics.class) {
            try {
                if (sInstance == null) {
                    sInstance = new Analytics();
                }
                analytics = sInstance;
            } catch (Throwable th) {
                throw th;
            }
        }
        return analytics;
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

    private void queuePage(String str, Map map) {
        PageLog pageLog = new PageLog();
        pageLog.setName(str);
        pageLog.setProperties(map);
        this.mChannel.enqueue(pageLog, "group_analytics", 1);
    }

    private void setDefaultTransmissionTarget(String str) {
        if (str != null) {
            this.mDefaultTransmissionTarget = createAnalyticsTransmissionTarget(str);
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
            WeakReference weakReference = this.mCurrentActivity;
            if (weakReference != null && (activity = (Activity) weakReference.get()) != null) {
                processOnResume(activity);
            }
            Channel.Listener channelListener = AnalyticsTransmissionTarget.getChannelListener();
            this.mAnalyticsTransmissionTargetListener = channelListener;
            this.mChannel.addListener(channelListener);
        }
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

    private synchronized void trackEventAsync(final String str, final List list, final AnalyticsTransmissionTarget analyticsTransmissionTarget, final int i) {
        final String userId = UserIdContext.getInstance().getUserId();
        post(new Runnable() { // from class: com.microsoft.appcenter.analytics.Analytics.8
            /* JADX WARN: Removed duplicated region for block: B:13:0x005f  */
            /* JADX WARN: Removed duplicated region for block: B:17:0x0062  */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void run() {
                String str2;
                AnalyticsTransmissionTarget analyticsTransmissionTarget2 = analyticsTransmissionTarget;
                if (analyticsTransmissionTarget2 == null) {
                    analyticsTransmissionTarget2 = Analytics.this.mDefaultTransmissionTarget;
                }
                EventLog eventLog = new EventLog();
                if (analyticsTransmissionTarget2 == null) {
                    if (!Analytics.this.mStartedFromApp) {
                        str2 = "Cannot track event using Analytics.trackEvent if not started from app, please start from the application or use Analytics.getTransmissionTarget.";
                        AppCenterLog.error("AppCenterAnalytics", str2);
                        return;
                    }
                    eventLog.setId(UUID.randomUUID());
                    eventLog.setName(str);
                    eventLog.setTypedProperties(list);
                    int persistenceFlag = Flags.getPersistenceFlag(i, true);
                    ((AbstractAppCenterService) Analytics.this).mChannel.enqueue(eventLog, persistenceFlag != 2 ? "group_analytics_critical" : "group_analytics", persistenceFlag);
                }
                if (!analyticsTransmissionTarget2.isEnabled()) {
                    str2 = "This transmission target is disabled.";
                    AppCenterLog.error("AppCenterAnalytics", str2);
                    return;
                }
                eventLog.addTransmissionTarget(analyticsTransmissionTarget2.getTransmissionTargetToken());
                eventLog.setTag(analyticsTransmissionTarget2);
                if (analyticsTransmissionTarget2 == Analytics.this.mDefaultTransmissionTarget) {
                    eventLog.setUserId(userId);
                }
                eventLog.setId(UUID.randomUUID());
                eventLog.setName(str);
                eventLog.setTypedProperties(list);
                int persistenceFlag2 = Flags.getPersistenceFlag(i, true);
                ((AbstractAppCenterService) Analytics.this).mChannel.enqueue(eventLog, persistenceFlag2 != 2 ? "group_analytics_critical" : "group_analytics", persistenceFlag2);
            }
        });
    }

    @Override // com.microsoft.appcenter.AbstractAppCenterService
    protected synchronized void applyEnabledState(boolean z) {
        try {
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
        } catch (Throwable th) {
            throw th;
        }
    }

    @Override // com.microsoft.appcenter.AbstractAppCenterService
    protected Channel.GroupListener getChannelListener() {
        return new Channel.GroupListener() { // from class: com.microsoft.appcenter.analytics.Analytics.6
            @Override // com.microsoft.appcenter.channel.Channel.GroupListener
            public void onBeforeSending(Log log) {
                Analytics.access$500(Analytics.this);
            }

            @Override // com.microsoft.appcenter.channel.Channel.GroupListener
            public void onFailure(Log log, Exception exc) {
                Analytics.access$500(Analytics.this);
            }

            @Override // com.microsoft.appcenter.channel.Channel.GroupListener
            public void onSuccess(Log log) {
                Analytics.access$500(Analytics.this);
            }
        };
    }

    String getEnabledPreferenceKeyPrefix() {
        return getEnabledPreferenceKey() + "/";
    }

    @Override // com.microsoft.appcenter.AbstractAppCenterService
    protected String getGroupName() {
        return "group_analytics";
    }

    @Override // com.microsoft.appcenter.AppCenterService
    public Map getLogFactories() {
        return this.mFactories;
    }

    @Override // com.microsoft.appcenter.AbstractAppCenterService
    protected String getLoggerTag() {
        return "AppCenterAnalytics";
    }

    @Override // com.microsoft.appcenter.AppCenterService
    public String getServiceName() {
        return "Analytics";
    }

    @Override // com.microsoft.appcenter.AbstractAppCenterService
    protected long getTriggerInterval() {
        return this.mTransmissionInterval;
    }

    @Override // com.microsoft.appcenter.AbstractAppCenterService, com.microsoft.appcenter.AppCenterService
    public boolean isAppSecretRequired() {
        return false;
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

    @Override // com.microsoft.appcenter.AbstractAppCenterService, com.microsoft.appcenter.AppCenterService
    public void onConfigurationUpdated(String str, String str2) {
        this.mStartedFromApp = true;
        startAppLevelFeatures();
        setDefaultTransmissionTarget(str2);
    }

    @Override // com.microsoft.appcenter.AbstractAppCenterService, com.microsoft.appcenter.AppCenterService
    public synchronized void onStarted(Context context, Channel channel, String str, String str2, boolean z) {
        this.mContext = context;
        this.mStartedFromApp = z;
        super.onStarted(context, channel, str, str2, z);
        setDefaultTransmissionTarget(str2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.appcenter.AbstractAppCenterService
    public synchronized void post(Runnable runnable) {
        super.post(runnable);
    }

    void postCommandEvenIfDisabled(Runnable runnable) {
        post(runnable, runnable, runnable);
    }
}
