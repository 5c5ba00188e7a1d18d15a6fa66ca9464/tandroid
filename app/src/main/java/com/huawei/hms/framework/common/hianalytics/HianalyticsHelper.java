package com.huawei.hms.framework.common.hianalytics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import com.huawei.hianalytics.process.HiAnalyticsInstance;
import com.huawei.hianalytics.process.HiAnalyticsManager;
import com.huawei.hms.framework.common.ContextHolder;
import com.huawei.hms.framework.common.ExecutorsUtils;
import com.huawei.hms.framework.common.Logger;
import com.huawei.hms.framework.common.StringUtils;
import com.huawei.hms.support.hianalytics.HiAnalyticsUtils;
import com.huawei.hms.utils.HMSBIInitializer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
/* loaded from: classes.dex */
public class HianalyticsHelper {
    private static final String DEAULT_HA_SERVICE_TAG = "_default_config_tag";
    private static final String HWID_HA_SERVICE_TAG = "hms_hwid";
    private static final String TAG = "HianalyticsHelper";
    private static final int TYPE_MAINTF = 1;
    private static final String USER_EXPERIENCE_INVOLVED = "user_experience_involved";
    private static final int USER_EXPERIENCE_ON = 1;
    @SuppressLint({"StaticFieldLeak"})
    private static volatile HianalyticsHelper instance;
    private boolean hasHMSBI;
    private boolean hasHianalytics;
    private String haTag = "hms_hwid";
    private HiAnalyticsInstance hInstance = null;
    private HiAnalyticsInstance defaultInstance = null;
    private boolean isEnablePrivacyPolicy = false;
    private ExecutorService reportExecutor = ExecutorsUtils.newSingleThreadExecutor("report_ha");

    private HianalyticsHelper() {
        try {
            HiAnalyticsManager.getInitFlag("_default_config_tag");
            this.hasHianalytics = true;
        } catch (Throwable unused) {
            Logger.i("HianalyticsHelper", "Hianalytics sdk not found");
            this.hasHianalytics = false;
        }
        if (!this.hasHianalytics) {
            tryHMSBIInit(ContextHolder.getAppContext());
        }
        Logger.v("HianalyticsHelper", "this time the ha %s, mini %s", Boolean.valueOf(this.hasHianalytics), Boolean.valueOf(this.hasHMSBI));
    }

    private void tryHMSBIInit(Context context) {
        if (context == null) {
            Logger.i("HianalyticsHelper", "the appContext hasn't init");
            return;
        }
        try {
            HMSBIInitializer.getInstance(context).initBI();
            this.hasHMSBI = true;
        } catch (NoClassDefFoundError unused) {
            Logger.w("HianalyticsHelper", "maybe you need add base sdk!");
        } catch (Throwable unused2) {
            Logger.w("HianalyticsHelper", "the hms base has other error!");
        }
    }

    public void setHaTag(String str) {
        this.haTag = str;
    }

    public void enablePrivacyPolicy(boolean z) {
        this.isEnablePrivacyPolicy = z;
    }

    public static HianalyticsHelper getInstance() {
        if (instance == null) {
            synchronized (HianalyticsHelper.class) {
                if (instance == null) {
                    instance = new HianalyticsHelper();
                }
            }
        }
        return instance;
    }

    public boolean isEnableReport(Context context) {
        if (this.hasHMSBI) {
            return true;
        }
        if (this.hasHianalytics) {
            return isEnableReportNoSeed(context);
        }
        return false;
    }

    public boolean isEnableReportNoSeed(Context context) {
        if (this.hasHMSBI) {
            return true;
        }
        if (!this.hasHianalytics) {
            Logger.i("HianalyticsHelper", "Hianalytics sdk need to be initialized");
            return false;
        } else if (context == null) {
            Logger.i("HianalyticsHelper", "HianalyticsHelper context can't be null");
            return false;
        } else if (this.isEnablePrivacyPolicy) {
            return isHianalyticsOk();
        } else {
            try {
                if (Settings.Secure.getInt(context.getContentResolver(), "user_experience_involved", -1) == 1) {
                    return isHianalyticsOk();
                }
            } catch (IllegalStateException unused) {
                Logger.w("HianalyticsHelper", "the setting has illegalStateException");
            } catch (Throwable unused2) {
                Logger.w("HianalyticsHelper", "the setting has other error");
            }
            Logger.i("HianalyticsHelper", "user experience involved needs to be opened");
            return false;
        }
    }

    private boolean isHianalyticsOk() {
        if (HiAnalyticsManager.getInitFlag("_default_config_tag")) {
            if (this.defaultInstance == null) {
                this.defaultInstance = HiAnalyticsManager.getInstanceByTag("_default_config_tag");
            }
            return this.defaultInstance != null;
        }
        if (this.hInstance == null) {
            this.hInstance = HiAnalyticsManager.getInstanceByTag(this.haTag);
        }
        return this.hInstance != null;
    }

    public void onEvent(LinkedHashMap<String, String> linkedHashMap, String str) {
        onEvent(linkedHashMap, str, 1);
    }

    public void onEvent(LinkedHashMap<String, String> linkedHashMap, String str, int i) {
        HiAnalyticsInstance hiAnalyticsInstance;
        if (this.hasHMSBI) {
            onNewEvent(ContextHolder.getAppContext(), str, linkedHashMap, i);
        } else if (i == 0) {
            Logger.v("HianalyticsHelper", "the base sdk isn't exsit, and reportType is %s", Integer.valueOf(i));
            return;
        }
        if (this.hasHianalytics && linkedHashMap != null) {
            Logger.v("HianalyticsHelper", "data = %s", linkedHashMap);
            if (HiAnalyticsManager.getInitFlag("_default_config_tag") && (hiAnalyticsInstance = this.defaultInstance) != null) {
                hiAnalyticsInstance.onEvent(1, str, linkedHashMap);
                return;
            }
            HiAnalyticsInstance hiAnalyticsInstance2 = this.hInstance;
            if (hiAnalyticsInstance2 != null) {
                hiAnalyticsInstance2.onEvent(1, str, linkedHashMap);
            } else {
                Logger.e("HianalyticsHelper", "the ha has error,has init but is null!");
            }
        }
    }

    private void onNewEvent(Context context, String str, Map map, int i) {
        if (context == null || map == null) {
            return;
        }
        Logger.v("HianalyticsHelper", "data = %s", map);
        try {
            HiAnalyticsUtils.getInstance().onNewEvent(context, str, map, i);
        } catch (NoSuchMethodError unused) {
            Logger.w("HianalyticsHelper", "may be you need upgrade stats sdk");
        } catch (Throwable unused2) {
            Logger.i("HianalyticsHelper", "the stats has other error,pls check it");
        }
    }

    public void onEvent(LinkedHashMap<String, String> linkedHashMap) {
        onEvent(linkedHashMap, "url_request");
    }

    public ExecutorService getReportExecutor() {
        return this.reportExecutor;
    }

    public void executeReportHa(HianalyticsBaseData hianalyticsBaseData, String str) {
        getReportExecutor().execute(new HianalyticsRunnable(hianalyticsBaseData, str));
    }

    public void reportException(Throwable th, String str) {
        if (!getInstance().isEnableReport(ContextHolder.getAppContext())) {
            return;
        }
        try {
            this.reportExecutor.submit(new AnonymousClass1(Thread.currentThread().getName(), th, str));
        } catch (RejectedExecutionException unused) {
            Logger.i("HianalyticsHelper", "reportException error RejectedExecutionException");
        } catch (Exception unused2) {
            Logger.i("HianalyticsHelper", "reportException error!", th);
        }
    }

    /* renamed from: com.huawei.hms.framework.common.hianalytics.HianalyticsHelper$1 */
    /* loaded from: classes.dex */
    class AnonymousClass1 implements Runnable {
        final /* synthetic */ Throwable val$e;
        final /* synthetic */ String val$eventId;
        final /* synthetic */ String val$threadName;

        AnonymousClass1(String str, Throwable th, String str2) {
            HianalyticsHelper.this = r1;
            this.val$threadName = str;
            this.val$e = th;
            this.val$eventId = str2;
        }

        @Override // java.lang.Runnable
        public void run() {
            CrashHianalyticsData crashHianalyticsData = new CrashHianalyticsData();
            crashHianalyticsData.put("sdk_version", "5.0.10.302");
            crashHianalyticsData.put("crash_type", "exception");
            crashHianalyticsData.put("thread_name", this.val$threadName);
            crashHianalyticsData.put("exception_name", this.val$e.getClass().getName());
            crashHianalyticsData.put("message", StringUtils.anonymizeMessage(this.val$e.getMessage()));
            crashHianalyticsData.put("stack_trace", StringUtils.getTraceInfo(this.val$e));
            HianalyticsHelper.getInstance().onEvent(crashHianalyticsData.get(), this.val$eventId);
        }
    }

    public void reportData(Context context, LinkedHashMap<String, String> linkedHashMap, String str, int i) {
        if (!isEnableReportNoSeed(context)) {
            return;
        }
        onEvent(linkedHashMap, str, i);
    }

    /* loaded from: classes.dex */
    private static class HianalyticsRunnable implements Runnable {
        private final HianalyticsBaseData data;
        private final String event;

        HianalyticsRunnable(HianalyticsBaseData hianalyticsBaseData, String str) {
            this.data = hianalyticsBaseData;
            this.event = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            HianalyticsHelper.getInstance().onEvent(this.data.get(), this.event);
        }
    }
}
