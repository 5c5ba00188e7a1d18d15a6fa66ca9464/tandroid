package com.microsoft.appcenter;

import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;
import com.microsoft.appcenter.channel.Channel;
import com.microsoft.appcenter.channel.DefaultChannel;
import com.microsoft.appcenter.channel.OneCollectorChannelListener;
import com.microsoft.appcenter.http.HttpClient;
import com.microsoft.appcenter.http.HttpUtils;
import com.microsoft.appcenter.ingestion.models.CustomPropertiesLog;
import com.microsoft.appcenter.ingestion.models.StartServiceLog;
import com.microsoft.appcenter.ingestion.models.json.CustomPropertiesLogFactory;
import com.microsoft.appcenter.ingestion.models.json.DefaultLogSerializer;
import com.microsoft.appcenter.ingestion.models.json.LogFactory;
import com.microsoft.appcenter.ingestion.models.json.LogSerializer;
import com.microsoft.appcenter.ingestion.models.json.StartServiceLogFactory;
import com.microsoft.appcenter.utils.AppCenterLog;
import com.microsoft.appcenter.utils.ApplicationLifecycleListener;
import com.microsoft.appcenter.utils.IdHelper;
import com.microsoft.appcenter.utils.NetworkStateHelper;
import com.microsoft.appcenter.utils.async.DefaultAppCenterFuture;
import com.microsoft.appcenter.utils.context.SessionContext;
import com.microsoft.appcenter.utils.context.UserIdContext;
import com.microsoft.appcenter.utils.storage.FileManager;
import com.microsoft.appcenter.utils.storage.SharedPreferencesManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
/* loaded from: classes.dex */
public class AppCenter {
    private static AppCenter sInstance;
    private AppCenterHandler mAppCenterHandler;
    private String mAppSecret;
    private Application mApplication;
    private ApplicationLifecycleListener mApplicationLifecycleListener;
    private Channel mChannel;
    private boolean mConfiguredFromApp;
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    private boolean mLogLevelConfigured;
    private LogSerializer mLogSerializer;
    private String mLogUrl;
    private OneCollectorChannelListener mOneCollectorChannelListener;
    private Set mServices;
    private Set mServicesStartedFromLibrary;
    private DefaultAppCenterFuture mSetMaxStorageSizeFuture;
    private String mTransmissionTargetToken;
    private UncaughtExceptionHandler mUncaughtExceptionHandler;
    private final List mStartedServicesNamesToLog = new ArrayList();
    private long mMaxStorageSizeInBytes = 10485760;

    /* JADX INFO: Access modifiers changed from: private */
    public void applyStorageMaxSize() {
        boolean maxStorageSize = this.mChannel.setMaxStorageSize(this.mMaxStorageSizeInBytes);
        DefaultAppCenterFuture defaultAppCenterFuture = this.mSetMaxStorageSizeFuture;
        if (defaultAppCenterFuture != null) {
            defaultAppCenterFuture.complete(Boolean.valueOf(maxStorageSize));
        }
    }

    private synchronized boolean checkPrecondition() {
        if (isInstanceConfigured()) {
            return true;
        }
        AppCenterLog.error("AppCenter", "App Center hasn't been configured. You need to call AppCenter.start with appSecret or AppCenter.configure first.");
        return false;
    }

    private void configureAndStartServices(Application application, String str, boolean z, Class[] clsArr) {
        if (configureInstance(application, str, z)) {
            startServices(z, clsArr);
        }
    }

    private synchronized void configureAndStartServices(Application application, String str, Class[] clsArr) {
        if (str != null) {
            try {
                if (!str.isEmpty()) {
                    configureAndStartServices(application, str, true, clsArr);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        AppCenterLog.error("AppCenter", "appSecret may not be null or empty.");
    }

    private synchronized boolean configureInstance(Application application, String str, final boolean z) {
        if (application == null) {
            AppCenterLog.error("AppCenter", "Application context may not be null.");
            return false;
        }
        if (!this.mLogLevelConfigured && (application.getApplicationInfo().flags & 2) == 2) {
            AppCenterLog.setLogLevel(5);
        }
        String str2 = this.mAppSecret;
        if (!z || configureSecretString(str)) {
            if (this.mHandler != null) {
                String str3 = this.mAppSecret;
                if (str3 != null && !str3.equals(str2)) {
                    this.mHandler.post(new Runnable() { // from class: com.microsoft.appcenter.AppCenter.4
                        @Override // java.lang.Runnable
                        public void run() {
                            AppCenter.this.mChannel.setAppSecret(AppCenter.this.mAppSecret);
                            AppCenter.this.applyStorageMaxSize();
                        }
                    });
                }
                return true;
            }
            this.mApplication = application;
            HandlerThread handlerThread = new HandlerThread("AppCenter.Looper");
            this.mHandlerThread = handlerThread;
            handlerThread.start();
            this.mHandler = new Handler(this.mHandlerThread.getLooper());
            this.mAppCenterHandler = new AppCenterHandler() { // from class: com.microsoft.appcenter.AppCenter.5
                @Override // com.microsoft.appcenter.AppCenterHandler
                public void post(Runnable runnable, Runnable runnable2) {
                    AppCenter.this.handlerAppCenterOperation(runnable, runnable2);
                }
            };
            ApplicationLifecycleListener applicationLifecycleListener = new ApplicationLifecycleListener(this.mHandler);
            this.mApplicationLifecycleListener = applicationLifecycleListener;
            this.mApplication.registerActivityLifecycleCallbacks(applicationLifecycleListener);
            this.mServices = new HashSet();
            this.mServicesStartedFromLibrary = new HashSet();
            this.mHandler.post(new Runnable() { // from class: com.microsoft.appcenter.AppCenter.6
                @Override // java.lang.Runnable
                public void run() {
                    AppCenter.this.finishConfiguration(z);
                }
            });
            AppCenterLog.info("AppCenter", "App Center SDK configured successfully.");
            return true;
        }
        return false;
    }

    private boolean configureSecretString(String str) {
        if (this.mConfiguredFromApp) {
            AppCenterLog.warn("AppCenter", "App Center may only be configured once.");
            return false;
        }
        this.mConfiguredFromApp = true;
        if (str != null) {
            for (String str2 : str.split(";")) {
                String[] split = str2.split("=", -1);
                String str3 = split[0];
                if (split.length == 1) {
                    if (!str3.isEmpty()) {
                        this.mAppSecret = str3;
                    }
                } else if (!split[1].isEmpty()) {
                    String str4 = split[1];
                    if ("appsecret".equals(str3)) {
                        this.mAppSecret = str4;
                    } else if ("target".equals(str3)) {
                        this.mTransmissionTargetToken = str4;
                    }
                }
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishConfiguration(boolean z) {
        Constants.loadFromContext(this.mApplication);
        FileManager.initialize(this.mApplication);
        SharedPreferencesManager.initialize(this.mApplication);
        SessionContext.getInstance();
        boolean isInstanceEnabled = isInstanceEnabled();
        HttpClient httpClient = DependencyConfiguration.getHttpClient();
        if (httpClient == null) {
            httpClient = HttpUtils.createHttpClient(this.mApplication);
        }
        DefaultLogSerializer defaultLogSerializer = new DefaultLogSerializer();
        this.mLogSerializer = defaultLogSerializer;
        defaultLogSerializer.addLogFactory("startService", new StartServiceLogFactory());
        this.mLogSerializer.addLogFactory("customProperties", new CustomPropertiesLogFactory());
        DefaultChannel defaultChannel = new DefaultChannel(this.mApplication, this.mAppSecret, this.mLogSerializer, httpClient, this.mHandler);
        this.mChannel = defaultChannel;
        if (z) {
            applyStorageMaxSize();
        } else {
            defaultChannel.setMaxStorageSize(10485760L);
        }
        this.mChannel.setEnabled(isInstanceEnabled);
        this.mChannel.addGroup("group_core", 50, 3000L, 3, null, null);
        this.mOneCollectorChannelListener = new OneCollectorChannelListener(this.mChannel, this.mLogSerializer, httpClient, IdHelper.getInstallId());
        if (this.mLogUrl != null) {
            if (this.mAppSecret != null) {
                AppCenterLog.info("AppCenter", "The log url of App Center endpoint has been changed to " + this.mLogUrl);
                this.mChannel.setLogUrl(this.mLogUrl);
            } else {
                AppCenterLog.info("AppCenter", "The log url of One Collector endpoint has been changed to " + this.mLogUrl);
                this.mOneCollectorChannelListener.setLogUrl(this.mLogUrl);
            }
        }
        this.mChannel.addListener(this.mOneCollectorChannelListener);
        if (!isInstanceEnabled) {
            NetworkStateHelper.getSharedInstance(this.mApplication).close();
        }
        UncaughtExceptionHandler uncaughtExceptionHandler = new UncaughtExceptionHandler(this.mHandler, this.mChannel);
        this.mUncaughtExceptionHandler = uncaughtExceptionHandler;
        if (isInstanceEnabled) {
            uncaughtExceptionHandler.register();
        }
        AppCenterLog.debug("AppCenter", "App Center initialized.");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishStartServices(Iterable iterable, Iterable iterable2, boolean z) {
        StringBuilder sb;
        String str;
        Iterator it = iterable.iterator();
        while (it.hasNext()) {
            AppCenterService appCenterService = (AppCenterService) it.next();
            appCenterService.onConfigurationUpdated(this.mAppSecret, this.mTransmissionTargetToken);
            AppCenterLog.info("AppCenter", appCenterService.getClass().getSimpleName() + " service configuration updated.");
        }
        boolean isInstanceEnabled = isInstanceEnabled();
        Iterator it2 = iterable2.iterator();
        while (it2.hasNext()) {
            AppCenterService appCenterService2 = (AppCenterService) it2.next();
            Map logFactories = appCenterService2.getLogFactories();
            if (logFactories != null) {
                for (Map.Entry entry : logFactories.entrySet()) {
                    this.mLogSerializer.addLogFactory((String) entry.getKey(), (LogFactory) entry.getValue());
                }
            }
            if (!isInstanceEnabled && appCenterService2.isInstanceEnabled()) {
                appCenterService2.setInstanceEnabled(false);
            }
            Application application = this.mApplication;
            Channel channel = this.mChannel;
            if (z) {
                appCenterService2.onStarted(application, channel, this.mAppSecret, this.mTransmissionTargetToken, true);
                sb = new StringBuilder();
                sb.append(appCenterService2.getClass().getSimpleName());
                str = " service started from application.";
            } else {
                appCenterService2.onStarted(application, channel, null, null, false);
                sb = new StringBuilder();
                sb.append(appCenterService2.getClass().getSimpleName());
                str = " service started from library.";
            }
            sb.append(str);
            AppCenterLog.info("AppCenter", sb.toString());
        }
        if (z) {
            Iterator it3 = iterable.iterator();
            while (it3.hasNext()) {
                this.mStartedServicesNamesToLog.add(((AppCenterService) it3.next()).getServiceName());
            }
            Iterator it4 = iterable2.iterator();
            while (it4.hasNext()) {
                this.mStartedServicesNamesToLog.add(((AppCenterService) it4.next()).getServiceName());
            }
            sendStartServiceLog();
        }
    }

    public static synchronized AppCenter getInstance() {
        AppCenter appCenter;
        synchronized (AppCenter.class) {
            try {
                if (sInstance == null) {
                    sInstance = new AppCenter();
                }
                appCenter = sInstance;
            } catch (Throwable th) {
                throw th;
            }
        }
        return appCenter;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handlerAppCenterOperation(final Runnable runnable, final Runnable runnable2) {
        try {
            if (checkPrecondition()) {
                Runnable runnable3 = new Runnable() { // from class: com.microsoft.appcenter.AppCenter.7
                    @Override // java.lang.Runnable
                    public void run() {
                        Runnable runnable4;
                        if (AppCenter.this.isInstanceEnabled()) {
                            runnable4 = runnable;
                        } else {
                            runnable4 = runnable2;
                            if (runnable4 == null) {
                                AppCenterLog.error("AppCenter", "App Center SDK is disabled.");
                                return;
                            }
                        }
                        runnable4.run();
                    }
                };
                if (Thread.currentThread() == this.mHandlerThread) {
                    runnable.run();
                } else {
                    this.mHandler.post(runnable3);
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private synchronized boolean isInstanceConfigured() {
        return this.mApplication != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void queueCustomProperties(Map map) {
        CustomPropertiesLog customPropertiesLog = new CustomPropertiesLog();
        customPropertiesLog.setProperties(map);
        this.mChannel.enqueue(customPropertiesLog, "group_core", 1);
    }

    private void sendStartServiceLog() {
        if (this.mStartedServicesNamesToLog.isEmpty() || !isInstanceEnabled()) {
            return;
        }
        ArrayList arrayList = new ArrayList(this.mStartedServicesNamesToLog);
        this.mStartedServicesNamesToLog.clear();
        StartServiceLog startServiceLog = new StartServiceLog();
        startServiceLog.setServices(arrayList);
        this.mChannel.enqueue(startServiceLog, "group_core", 1);
    }

    public static void setCustomProperties(CustomProperties customProperties) {
        getInstance().setInstanceCustomProperties(customProperties);
    }

    private synchronized void setInstanceCustomProperties(CustomProperties customProperties) {
        if (customProperties == null) {
            AppCenterLog.error("AppCenter", "Custom properties may not be null.");
            return;
        }
        final Map properties = customProperties.getProperties();
        if (properties.size() == 0) {
            AppCenterLog.error("AppCenter", "Custom properties may not be empty.");
        } else {
            handlerAppCenterOperation(new Runnable() { // from class: com.microsoft.appcenter.AppCenter.3
                @Override // java.lang.Runnable
                public void run() {
                    AppCenter.this.queueCustomProperties(properties);
                }
            }, null);
        }
    }

    private synchronized void setInstanceUserId(String str) {
        if (!this.mConfiguredFromApp) {
            AppCenterLog.error("AppCenter", "AppCenter must be configured from application, libraries cannot use call setUserId.");
            return;
        }
        String str2 = this.mAppSecret;
        if (str2 == null && this.mTransmissionTargetToken == null) {
            AppCenterLog.error("AppCenter", "AppCenter must be configured with a secret from application to call setUserId.");
            return;
        }
        if (str != null) {
            if (str2 != null && !UserIdContext.checkUserIdValidForAppCenter(str)) {
                return;
            }
            if (this.mTransmissionTargetToken != null && !UserIdContext.checkUserIdValidForOneCollector(str)) {
                return;
            }
        }
        UserIdContext.getInstance().setUserId(str);
    }

    public static void setUserId(String str) {
        getInstance().setInstanceUserId(str);
    }

    public static void start(Application application, String str, Class... clsArr) {
        getInstance().configureAndStartServices(application, str, clsArr);
    }

    private void startOrUpdateService(AppCenterService appCenterService, Collection collection, Collection collection2, boolean z) {
        if (z) {
            startOrUpdateServiceFromApp(appCenterService, collection, collection2);
        } else if (this.mServices.contains(appCenterService)) {
        } else {
            startServiceFromLibrary(appCenterService, collection);
        }
    }

    private void startOrUpdateServiceFromApp(AppCenterService appCenterService, Collection collection, Collection collection2) {
        String serviceName = appCenterService.getServiceName();
        if (this.mServices.contains(appCenterService)) {
            if (this.mServicesStartedFromLibrary.remove(appCenterService)) {
                collection2.add(appCenterService);
                return;
            }
            AppCenterLog.warn("AppCenter", "App Center has already started the service with class name: " + appCenterService.getServiceName());
        } else if (this.mAppSecret != null || !appCenterService.isAppSecretRequired()) {
            startService(appCenterService, collection);
        } else {
            AppCenterLog.error("AppCenter", "App Center was started without app secret, but the service requires it; not starting service " + serviceName + ".");
        }
    }

    private boolean startService(AppCenterService appCenterService, Collection collection) {
        String serviceName = appCenterService.getServiceName();
        if (ServiceInstrumentationUtils.isServiceDisabledByInstrumentation(serviceName)) {
            AppCenterLog.debug("AppCenter", "Instrumentation variable to disable service has been set; not starting service " + serviceName + ".");
            return false;
        }
        appCenterService.onStarting(this.mAppCenterHandler);
        this.mApplicationLifecycleListener.registerApplicationLifecycleCallbacks(appCenterService);
        this.mApplication.registerActivityLifecycleCallbacks(appCenterService);
        this.mServices.add(appCenterService);
        collection.add(appCenterService);
        return true;
    }

    private void startServiceFromLibrary(AppCenterService appCenterService, Collection collection) {
        String serviceName = appCenterService.getServiceName();
        if (!appCenterService.isAppSecretRequired()) {
            if (startService(appCenterService, collection)) {
                this.mServicesStartedFromLibrary.add(appCenterService);
                return;
            }
            return;
        }
        AppCenterLog.error("AppCenter", "This service cannot be started from a library: " + serviceName + ".");
    }

    private final synchronized void startServices(final boolean z, Class... clsArr) {
        if (clsArr == null) {
            AppCenterLog.error("AppCenter", "Cannot start services, services array is null. Failed to start services.");
            return;
        }
        int i = 0;
        if (this.mApplication == null) {
            StringBuilder sb = new StringBuilder();
            int length = clsArr.length;
            while (i < length) {
                Class cls = clsArr[i];
                sb.append("\t");
                sb.append(cls.getName());
                sb.append("\n");
                i++;
            }
            AppCenterLog.error("AppCenter", "Cannot start services, App Center has not been configured. Failed to start the following services:\n" + ((Object) sb));
            return;
        }
        final ArrayList arrayList = new ArrayList();
        final ArrayList arrayList2 = new ArrayList();
        int length2 = clsArr.length;
        while (i < length2) {
            Class cls2 = clsArr[i];
            if (cls2 == null) {
                AppCenterLog.warn("AppCenter", "Skipping null service, please check your varargs/array does not contain any null reference.");
            } else {
                try {
                    startOrUpdateService((AppCenterService) cls2.getMethod("getInstance", null).invoke(null, null), arrayList, arrayList2, z);
                } catch (Exception e) {
                    AppCenterLog.error("AppCenter", "Failed to get service instance '" + cls2.getName() + "', skipping it.", e);
                }
            }
            i++;
        }
        this.mHandler.post(new Runnable() { // from class: com.microsoft.appcenter.AppCenter.8
            @Override // java.lang.Runnable
            public void run() {
                AppCenter.this.finishStartServices(arrayList2, arrayList, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isInstanceEnabled() {
        return SharedPreferencesManager.getBoolean("enabled", true);
    }
}
