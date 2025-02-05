package com.microsoft.appcenter.crashes;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import com.microsoft.appcenter.AbstractAppCenterService;
import com.microsoft.appcenter.channel.Channel;
import com.microsoft.appcenter.crashes.ingestion.models.ErrorAttachmentLog;
import com.microsoft.appcenter.crashes.ingestion.models.Exception;
import com.microsoft.appcenter.crashes.ingestion.models.HandledErrorLog;
import com.microsoft.appcenter.crashes.ingestion.models.ManagedErrorLog;
import com.microsoft.appcenter.crashes.ingestion.models.json.ErrorAttachmentLogFactory;
import com.microsoft.appcenter.crashes.ingestion.models.json.HandledErrorLogFactory;
import com.microsoft.appcenter.crashes.ingestion.models.json.ManagedErrorLogFactory;
import com.microsoft.appcenter.crashes.model.ErrorReport;
import com.microsoft.appcenter.crashes.model.NativeException;
import com.microsoft.appcenter.crashes.utils.ErrorLogHelper;
import com.microsoft.appcenter.ingestion.models.Device;
import com.microsoft.appcenter.ingestion.models.json.DefaultLogSerializer;
import com.microsoft.appcenter.ingestion.models.json.LogSerializer;
import com.microsoft.appcenter.utils.AppCenterLog;
import com.microsoft.appcenter.utils.DeviceInfoHelper;
import com.microsoft.appcenter.utils.HandlerUtils;
import com.microsoft.appcenter.utils.async.AppCenterFuture;
import com.microsoft.appcenter.utils.async.DefaultAppCenterFuture;
import com.microsoft.appcenter.utils.context.SessionContext;
import com.microsoft.appcenter.utils.context.UserIdContext;
import com.microsoft.appcenter.utils.storage.FileManager;
import com.microsoft.appcenter.utils.storage.SharedPreferencesManager;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import org.json.JSONException;

/* loaded from: classes.dex */
public class Crashes extends AbstractAppCenterService {
    private static final CrashesListener DEFAULT_ERROR_REPORTING_LISTENER = new DefaultCrashesListener();
    private static Crashes sInstance = null;
    private boolean mAutomaticProcessing = true;
    private Context mContext;
    private CrashesListener mCrashesListener;
    private Device mDevice;
    private final Map mErrorReportCache;
    private final Map mFactories;
    private boolean mHasReceivedMemoryWarningInLastSession;
    private long mInitializeTimestamp;
    private ErrorReport mLastSessionErrorReport;
    private LogSerializer mLogSerializer;
    private ComponentCallbacks2 mMemoryWarningListener;
    private boolean mSavedUncaughtException;
    private UncaughtExceptionHandler mUncaughtExceptionHandler;
    private final Map mUnprocessedErrorReports;

    private interface CallbackProcessor {
        void onCallBack(ErrorReport errorReport);

        boolean shouldDeleteThrowable();
    }

    private static class DefaultCrashesListener extends AbstractCrashesListener {
        private DefaultCrashesListener() {
        }
    }

    private static class ErrorLogReport {
        private final ManagedErrorLog log;
        private final ErrorReport report;

        private ErrorLogReport(ManagedErrorLog managedErrorLog, ErrorReport errorReport) {
            this.log = managedErrorLog;
            this.report = errorReport;
        }
    }

    private interface ExceptionModelBuilder {
        Exception buildExceptionModel();
    }

    private Crashes() {
        HashMap hashMap = new HashMap();
        this.mFactories = hashMap;
        hashMap.put("managedError", ManagedErrorLogFactory.getInstance());
        hashMap.put("handledError", HandledErrorLogFactory.getInstance());
        hashMap.put("errorAttachment", ErrorAttachmentLogFactory.getInstance());
        DefaultLogSerializer defaultLogSerializer = new DefaultLogSerializer();
        this.mLogSerializer = defaultLogSerializer;
        defaultLogSerializer.addLogFactory("managedError", ManagedErrorLogFactory.getInstance());
        this.mLogSerializer.addLogFactory("errorAttachment", ErrorAttachmentLogFactory.getInstance());
        this.mCrashesListener = DEFAULT_ERROR_REPORTING_LISTENER;
        this.mUnprocessedErrorReports = new LinkedHashMap();
        this.mErrorReportCache = new LinkedHashMap();
    }

    public static synchronized Crashes getInstance() {
        Crashes crashes;
        synchronized (Crashes.class) {
            try {
                if (sInstance == null) {
                    sInstance = new Crashes();
                }
                crashes = sInstance;
            } catch (Throwable th) {
                throw th;
            }
        }
        return crashes;
    }

    public static AppCenterFuture getMinidumpDirectory() {
        return getInstance().getNewMinidumpDirectoryAsync();
    }

    private synchronized AppCenterFuture getNewMinidumpDirectoryAsync() {
        final DefaultAppCenterFuture defaultAppCenterFuture;
        defaultAppCenterFuture = new DefaultAppCenterFuture();
        postAsyncGetter(new Runnable() { // from class: com.microsoft.appcenter.crashes.Crashes.1
            @Override // java.lang.Runnable
            public void run() {
                defaultAppCenterFuture.complete(ErrorLogHelper.getNewMinidumpSubfolderWithContextData(Crashes.this.mContext).getAbsolutePath());
            }
        }, defaultAppCenterFuture, null);
        return defaultAppCenterFuture;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handleUserConfirmation(final int i) {
        post(new Runnable() { // from class: com.microsoft.appcenter.crashes.Crashes.12
            /* JADX WARN: Removed duplicated region for block: B:28:0x00bc  */
            /* JADX WARN: Removed duplicated region for block: B:31:0x00d8  */
            /* JADX WARN: Removed duplicated region for block: B:34:0x00f3 A[SYNTHETIC] */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void run() {
                File file;
                int i2 = i;
                if (i2 == 1) {
                    Iterator it = Crashes.this.mUnprocessedErrorReports.keySet().iterator();
                    while (it.hasNext()) {
                        UUID uuid = (UUID) it.next();
                        it.remove();
                        Crashes.this.removeAllStoredErrorLogFiles(uuid);
                    }
                    ErrorLogHelper.cleanPendingMinidumps();
                    return;
                }
                if (i2 == 2) {
                    SharedPreferencesManager.putBoolean("com.microsoft.appcenter.crashes.always.send", true);
                }
                Iterator it2 = Crashes.this.mUnprocessedErrorReports.entrySet().iterator();
                while (it2.hasNext()) {
                    Map.Entry entry = (Map.Entry) it2.next();
                    ErrorLogReport errorLogReport = (ErrorLogReport) entry.getValue();
                    ErrorAttachmentLog errorAttachmentLog = null;
                    if (errorLogReport.report.getDevice() != null && "appcenter.ndk".equals(errorLogReport.report.getDevice().getWrapperSdkName())) {
                        Exception exception = errorLogReport.log.getException();
                        String minidumpFilePath = exception.getMinidumpFilePath();
                        exception.setMinidumpFilePath(null);
                        if (minidumpFilePath == null) {
                            minidumpFilePath = exception.getStackTrace();
                            exception.setStackTrace(null);
                        }
                        if (minidumpFilePath != null) {
                            File file2 = new File(minidumpFilePath);
                            errorAttachmentLog = ErrorAttachmentLog.attachmentWithBinary(FileManager.readBytes(file2), "minidump.dmp", "application/octet-stream");
                            file = file2;
                            ((AbstractAppCenterService) Crashes.this).mChannel.enqueue(errorLogReport.log, "groupErrors", 2);
                            if (errorAttachmentLog != null) {
                                Crashes.this.sendErrorAttachment(errorLogReport.log.getId(), Collections.singleton(errorAttachmentLog));
                                file.delete();
                            }
                            if (!Crashes.this.mAutomaticProcessing) {
                                Crashes.this.sendErrorAttachment(errorLogReport.log.getId(), Crashes.this.mCrashesListener.getErrorAttachments(errorLogReport.report));
                            }
                            it2.remove();
                            ErrorLogHelper.removeStoredErrorLogFile((UUID) entry.getKey());
                        } else {
                            AppCenterLog.warn("AppCenterCrashes", "NativeException found without minidump.");
                        }
                    }
                    file = null;
                    ((AbstractAppCenterService) Crashes.this).mChannel.enqueue(errorLogReport.log, "groupErrors", 2);
                    if (errorAttachmentLog != null) {
                    }
                    if (!Crashes.this.mAutomaticProcessing) {
                    }
                    it2.remove();
                    ErrorLogHelper.removeStoredErrorLogFile((UUID) entry.getKey());
                }
            }
        });
    }

    private void initialize() {
        boolean isInstanceEnabled = isInstanceEnabled();
        this.mInitializeTimestamp = isInstanceEnabled ? System.currentTimeMillis() : -1L;
        if (isInstanceEnabled) {
            UncaughtExceptionHandler uncaughtExceptionHandler = new UncaughtExceptionHandler();
            this.mUncaughtExceptionHandler = uncaughtExceptionHandler;
            uncaughtExceptionHandler.register();
            processMinidumpFiles();
            return;
        }
        UncaughtExceptionHandler uncaughtExceptionHandler2 = this.mUncaughtExceptionHandler;
        if (uncaughtExceptionHandler2 != null) {
            uncaughtExceptionHandler2.unregister();
            this.mUncaughtExceptionHandler = null;
        }
    }

    public static AppCenterFuture isEnabled() {
        return getInstance().isInstanceEnabledAsync();
    }

    private static boolean isMemoryRunningLevelWasReceived(int i) {
        return i == 5 || i == 10 || i == 15 || i == 80;
    }

    private void processMinidumpFiles() {
        File lastErrorLogFile;
        for (File file : ErrorLogHelper.getNewMinidumpFiles()) {
            if (file.isDirectory()) {
                File[] listFiles = file.listFiles(new FilenameFilter() { // from class: com.microsoft.appcenter.crashes.Crashes.10
                    @Override // java.io.FilenameFilter
                    public boolean accept(File file2, String str) {
                        return str.endsWith(".dmp");
                    }
                });
                if (listFiles != null && listFiles.length != 0) {
                    for (File file2 : listFiles) {
                        processSingleMinidump(file2, file);
                    }
                }
            } else {
                AppCenterLog.debug("AppCenterCrashes", "Found a minidump from a previous SDK version.");
                processSingleMinidump(file, file);
            }
        }
        while (true) {
            lastErrorLogFile = ErrorLogHelper.getLastErrorLogFile();
            if (lastErrorLogFile == null || lastErrorLogFile.length() != 0) {
                break;
            }
            AppCenterLog.warn("AppCenterCrashes", "Deleting empty error file: " + lastErrorLogFile);
            lastErrorLogFile.delete();
        }
        if (lastErrorLogFile != null) {
            AppCenterLog.debug("AppCenterCrashes", "Processing crash report for the last session.");
            String read = FileManager.read(lastErrorLogFile);
            if (read == null) {
                AppCenterLog.error("AppCenterCrashes", "Error reading last session error log.");
            } else {
                try {
                    this.mLastSessionErrorReport = buildErrorReport((ManagedErrorLog) this.mLogSerializer.deserializeLog(read, null));
                    AppCenterLog.debug("AppCenterCrashes", "Processed crash report for the last session.");
                } catch (JSONException e) {
                    AppCenterLog.error("AppCenterCrashes", "Error parsing last session error log.", e);
                }
            }
        }
        ErrorLogHelper.removeStaleMinidumpSubfolders();
    }

    private void processPendingErrors() {
        for (File file : ErrorLogHelper.getStoredErrorLogFiles()) {
            AppCenterLog.debug("AppCenterCrashes", "Process pending error file: " + file);
            String read = FileManager.read(file);
            if (read != null) {
                try {
                    ManagedErrorLog managedErrorLog = (ManagedErrorLog) this.mLogSerializer.deserializeLog(read, null);
                    UUID id = managedErrorLog.getId();
                    ErrorReport buildErrorReport = buildErrorReport(managedErrorLog);
                    if (buildErrorReport != null) {
                        if (this.mAutomaticProcessing && !this.mCrashesListener.shouldProcess(buildErrorReport)) {
                            AppCenterLog.debug("AppCenterCrashes", "CrashesListener.shouldProcess returned false, clean up and ignore log: " + id.toString());
                        }
                        if (!this.mAutomaticProcessing) {
                            AppCenterLog.debug("AppCenterCrashes", "CrashesListener.shouldProcess returned true, continue processing log: " + id.toString());
                        }
                        this.mUnprocessedErrorReports.put(id, this.mErrorReportCache.get(id));
                    }
                    removeAllStoredErrorLogFiles(id);
                } catch (JSONException e) {
                    AppCenterLog.error("AppCenterCrashes", "Error parsing error log. Deleting invalid file: " + file, e);
                    file.delete();
                }
            }
        }
        boolean isMemoryRunningLevelWasReceived = isMemoryRunningLevelWasReceived(SharedPreferencesManager.getInt("com.microsoft.appcenter.crashes.memory", -1));
        this.mHasReceivedMemoryWarningInLastSession = isMemoryRunningLevelWasReceived;
        if (isMemoryRunningLevelWasReceived) {
            AppCenterLog.debug("AppCenterCrashes", "The application received a low memory warning in the last session.");
        }
        SharedPreferencesManager.remove("com.microsoft.appcenter.crashes.memory");
        if (this.mAutomaticProcessing) {
            sendCrashReportsOrAwaitUserConfirmation();
        }
    }

    private void processSingleMinidump(File file, File file2) {
        AppCenterLog.debug("AppCenterCrashes", "Process pending minidump file: " + file);
        long lastModified = file.lastModified();
        File file3 = new File(ErrorLogHelper.getPendingMinidumpDirectory(), file.getName());
        Exception exception = new Exception();
        exception.setType("minidump");
        exception.setWrapperSdkName("appcenter.ndk");
        exception.setMinidumpFilePath(file3.getPath());
        ManagedErrorLog managedErrorLog = new ManagedErrorLog();
        managedErrorLog.setException(exception);
        managedErrorLog.setTimestamp(new Date(lastModified));
        managedErrorLog.setFatal(Boolean.TRUE);
        managedErrorLog.setId(ErrorLogHelper.parseLogFolderUuid(file2));
        SessionContext.SessionInfo sessionAt = SessionContext.getInstance().getSessionAt(lastModified);
        managedErrorLog.setAppLaunchTimestamp((sessionAt == null || sessionAt.getAppLaunchTimestamp() > lastModified) ? managedErrorLog.getTimestamp() : new Date(sessionAt.getAppLaunchTimestamp()));
        managedErrorLog.setProcessId(0);
        managedErrorLog.setProcessName("");
        managedErrorLog.setUserId(UserIdContext.getInstance().getUserId());
        try {
            Device storedDeviceInfo = ErrorLogHelper.getStoredDeviceInfo(file2);
            if (storedDeviceInfo == null) {
                storedDeviceInfo = getDeviceInfo(this.mContext);
                storedDeviceInfo.setWrapperSdkName("appcenter.ndk");
            }
            managedErrorLog.setDevice(storedDeviceInfo);
            saveErrorLogFiles(new NativeException(), managedErrorLog);
            if (file.renameTo(file3)) {
            } else {
                throw new IOException("Failed to move file");
            }
        } catch (Exception e) {
            file.delete();
            removeAllStoredErrorLogFiles(managedErrorLog.getId());
            AppCenterLog.error("AppCenterCrashes", "Failed to process new minidump file: " + file, e);
        }
    }

    private synchronized UUID queueException(final ExceptionModelBuilder exceptionModelBuilder, Map map, final Iterable iterable) {
        final UUID randomUUID;
        final String userId = UserIdContext.getInstance().getUserId();
        randomUUID = UUID.randomUUID();
        final Map validateProperties = ErrorLogHelper.validateProperties(map, "HandledError");
        post(new Runnable() { // from class: com.microsoft.appcenter.crashes.Crashes.9
            @Override // java.lang.Runnable
            public void run() {
                HandledErrorLog handledErrorLog = new HandledErrorLog();
                handledErrorLog.setId(randomUUID);
                handledErrorLog.setUserId(userId);
                handledErrorLog.setException(exceptionModelBuilder.buildExceptionModel());
                handledErrorLog.setProperties(validateProperties);
                ((AbstractAppCenterService) Crashes.this).mChannel.enqueue(handledErrorLog, "groupErrors", 1);
                Crashes.this.sendErrorAttachment(randomUUID, iterable);
            }
        });
        return randomUUID;
    }

    private synchronized void queueException(final Throwable th, Map map, Iterable iterable) {
        queueException(new ExceptionModelBuilder() { // from class: com.microsoft.appcenter.crashes.Crashes.7
            @Override // com.microsoft.appcenter.crashes.Crashes.ExceptionModelBuilder
            public Exception buildExceptionModel() {
                return ErrorLogHelper.getModelExceptionFromThrowable(th);
            }
        }, map, iterable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeAllStoredErrorLogFiles(UUID uuid) {
        ErrorLogHelper.removeStoredErrorLogFile(uuid);
        removeStoredThrowable(uuid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeStoredThrowable(UUID uuid) {
        this.mErrorReportCache.remove(uuid);
        WrapperSdkExceptionManager.deleteWrapperExceptionData(uuid);
        ErrorLogHelper.removeStoredThrowableFile(uuid);
    }

    private UUID saveErrorLogFiles(Throwable th, ManagedErrorLog managedErrorLog) {
        File errorStorageDirectory = ErrorLogHelper.getErrorStorageDirectory();
        UUID id = managedErrorLog.getId();
        String uuid = id.toString();
        AppCenterLog.debug("AppCenterCrashes", "Saving uncaught exception.");
        File file = new File(errorStorageDirectory, uuid + ".json");
        FileManager.write(file, this.mLogSerializer.serializeLog(managedErrorLog));
        AppCenterLog.debug("AppCenterCrashes", "Saved JSON content for ingestion into " + file);
        File file2 = new File(errorStorageDirectory, uuid + ".throwable");
        if (th != null) {
            try {
                String stackTraceString = Log.getStackTraceString(th);
                FileManager.write(file2, stackTraceString);
                AppCenterLog.debug("AppCenterCrashes", "Saved stack trace as is for client side inspection in " + file2 + " stack trace:" + stackTraceString);
            } catch (StackOverflowError e) {
                AppCenterLog.error("AppCenterCrashes", "Failed to store stack trace.", e);
                file2.delete();
                th = null;
            }
        }
        if (th == null) {
            if (!file2.createNewFile()) {
                throw new IOException(file2.getName());
            }
            AppCenterLog.debug("AppCenterCrashes", "Saved empty Throwable file in " + file2);
        }
        return id;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void saveMemoryRunningLevel(int i) {
        SharedPreferencesManager.putInt("com.microsoft.appcenter.crashes.memory", i);
        AppCenterLog.debug("AppCenterCrashes", String.format("The memory running level (%s) was saved.", Integer.valueOf(i)));
    }

    private boolean sendCrashReportsOrAwaitUserConfirmation() {
        final boolean z = SharedPreferencesManager.getBoolean("com.microsoft.appcenter.crashes.always.send", false);
        HandlerUtils.runOnUiThread(new Runnable() { // from class: com.microsoft.appcenter.crashes.Crashes.11
            @Override // java.lang.Runnable
            public void run() {
                if (Crashes.this.mUnprocessedErrorReports.size() > 0) {
                    if (z) {
                        AppCenterLog.debug("AppCenterCrashes", "The flag for user confirmation is set to ALWAYS_SEND, will send logs.");
                        Crashes.this.handleUserConfirmation(0);
                    } else if (!Crashes.this.mAutomaticProcessing) {
                        AppCenterLog.debug("AppCenterCrashes", "Automatic processing disabled, will wait for explicit user confirmation.");
                    } else if (Crashes.this.mCrashesListener.shouldAwaitUserConfirmation()) {
                        AppCenterLog.debug("AppCenterCrashes", "CrashesListener.shouldAwaitUserConfirmation returned true, wait sending logs.");
                    } else {
                        AppCenterLog.debug("AppCenterCrashes", "CrashesListener.shouldAwaitUserConfirmation returned false, will send logs.");
                        Crashes.this.handleUserConfirmation(0);
                    }
                }
            }
        });
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendErrorAttachment(UUID uuid, Iterable iterable) {
        String str;
        if (iterable == null) {
            AppCenterLog.debug("AppCenterCrashes", "Error report: " + uuid.toString() + " does not have any attachment.");
            return;
        }
        Iterator it = iterable.iterator();
        while (it.hasNext()) {
            ErrorAttachmentLog errorAttachmentLog = (ErrorAttachmentLog) it.next();
            if (errorAttachmentLog != null) {
                errorAttachmentLog.setId(UUID.randomUUID());
                errorAttachmentLog.setErrorId(uuid);
                if (!errorAttachmentLog.isValid()) {
                    str = "Not all required fields are present in ErrorAttachmentLog.";
                } else if (errorAttachmentLog.getData().length > 7340032) {
                    str = String.format(Locale.ENGLISH, "Discarding attachment with size above %d bytes: size=%d, fileName=%s.", 7340032, Integer.valueOf(errorAttachmentLog.getData().length), errorAttachmentLog.getFileName());
                } else {
                    this.mChannel.enqueue(errorAttachmentLog, "groupErrors", 1);
                }
                AppCenterLog.error("AppCenterCrashes", str);
            } else {
                AppCenterLog.warn("AppCenterCrashes", "Skipping null ErrorAttachmentLog.");
            }
        }
    }

    public static void trackError(Throwable th) {
        trackError(th, null, null);
    }

    public static void trackError(Throwable th, Map map, Iterable iterable) {
        getInstance().queueException(th, map, iterable);
    }

    @Override // com.microsoft.appcenter.AbstractAppCenterService
    protected synchronized void applyEnabledState(boolean z) {
        try {
            initialize();
            if (z) {
                ComponentCallbacks2 componentCallbacks2 = new ComponentCallbacks2() { // from class: com.microsoft.appcenter.crashes.Crashes.5
                    @Override // android.content.ComponentCallbacks
                    public void onConfigurationChanged(Configuration configuration) {
                    }

                    @Override // android.content.ComponentCallbacks
                    public void onLowMemory() {
                        Crashes.saveMemoryRunningLevel(80);
                    }

                    @Override // android.content.ComponentCallbacks2
                    public void onTrimMemory(int i) {
                        Crashes.saveMemoryRunningLevel(i);
                    }
                };
                this.mMemoryWarningListener = componentCallbacks2;
                this.mContext.registerComponentCallbacks(componentCallbacks2);
            } else {
                File[] listFiles = ErrorLogHelper.getErrorStorageDirectory().listFiles();
                if (listFiles != null) {
                    for (File file : listFiles) {
                        AppCenterLog.debug("AppCenterCrashes", "Deleting file " + file);
                        if (!file.delete()) {
                            AppCenterLog.warn("AppCenterCrashes", "Failed to delete file " + file);
                        }
                    }
                }
                AppCenterLog.info("AppCenterCrashes", "Deleted crashes local files");
                this.mErrorReportCache.clear();
                this.mLastSessionErrorReport = null;
                this.mContext.unregisterComponentCallbacks(this.mMemoryWarningListener);
                this.mMemoryWarningListener = null;
                SharedPreferencesManager.remove("com.microsoft.appcenter.crashes.memory");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    ErrorReport buildErrorReport(ManagedErrorLog managedErrorLog) {
        UUID id = managedErrorLog.getId();
        if (this.mErrorReportCache.containsKey(id)) {
            ErrorReport errorReport = ((ErrorLogReport) this.mErrorReportCache.get(id)).report;
            errorReport.setDevice(managedErrorLog.getDevice());
            return errorReport;
        }
        File storedThrowableFile = ErrorLogHelper.getStoredThrowableFile(id);
        if (storedThrowableFile == null) {
            return null;
        }
        ErrorReport errorReportFromErrorLog = ErrorLogHelper.getErrorReportFromErrorLog(managedErrorLog, storedThrowableFile.length() > 0 ? FileManager.read(storedThrowableFile) : null);
        this.mErrorReportCache.put(id, new ErrorLogReport(managedErrorLog, errorReportFromErrorLog));
        return errorReportFromErrorLog;
    }

    @Override // com.microsoft.appcenter.AbstractAppCenterService
    protected Channel.GroupListener getChannelListener() {
        return new Channel.GroupListener() { // from class: com.microsoft.appcenter.crashes.Crashes.6
            private void processCallback(final com.microsoft.appcenter.ingestion.models.Log log, final CallbackProcessor callbackProcessor) {
                Crashes.this.post(new Runnable() { // from class: com.microsoft.appcenter.crashes.Crashes.6.1
                    @Override // java.lang.Runnable
                    public void run() {
                        String str;
                        com.microsoft.appcenter.ingestion.models.Log log2 = log;
                        if (log2 instanceof ManagedErrorLog) {
                            ManagedErrorLog managedErrorLog = (ManagedErrorLog) log2;
                            final ErrorReport buildErrorReport = Crashes.this.buildErrorReport(managedErrorLog);
                            UUID id = managedErrorLog.getId();
                            if (buildErrorReport != null) {
                                if (callbackProcessor.shouldDeleteThrowable()) {
                                    Crashes.this.removeStoredThrowable(id);
                                }
                                HandlerUtils.runOnUiThread(new Runnable() { // from class: com.microsoft.appcenter.crashes.Crashes.6.1.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        callbackProcessor.onCallBack(buildErrorReport);
                                    }
                                });
                                return;
                            } else {
                                str = "Cannot find crash report for the error log: " + id;
                            }
                        } else {
                            if ((log2 instanceof ErrorAttachmentLog) || (log2 instanceof HandledErrorLog)) {
                                return;
                            }
                            str = "A different type of log comes to crashes: " + log.getClass().getName();
                        }
                        AppCenterLog.warn("AppCenterCrashes", str);
                    }
                });
            }

            @Override // com.microsoft.appcenter.channel.Channel.GroupListener
            public void onBeforeSending(com.microsoft.appcenter.ingestion.models.Log log) {
                processCallback(log, new CallbackProcessor() { // from class: com.microsoft.appcenter.crashes.Crashes.6.2
                    @Override // com.microsoft.appcenter.crashes.Crashes.CallbackProcessor
                    public void onCallBack(ErrorReport errorReport) {
                        Crashes.this.mCrashesListener.onBeforeSending(errorReport);
                    }

                    @Override // com.microsoft.appcenter.crashes.Crashes.CallbackProcessor
                    public boolean shouldDeleteThrowable() {
                        return false;
                    }
                });
            }

            @Override // com.microsoft.appcenter.channel.Channel.GroupListener
            public void onFailure(com.microsoft.appcenter.ingestion.models.Log log, final Exception exc) {
                processCallback(log, new CallbackProcessor() { // from class: com.microsoft.appcenter.crashes.Crashes.6.4
                    @Override // com.microsoft.appcenter.crashes.Crashes.CallbackProcessor
                    public void onCallBack(ErrorReport errorReport) {
                        Crashes.this.mCrashesListener.onSendingFailed(errorReport, exc);
                    }

                    @Override // com.microsoft.appcenter.crashes.Crashes.CallbackProcessor
                    public boolean shouldDeleteThrowable() {
                        return true;
                    }
                });
            }

            @Override // com.microsoft.appcenter.channel.Channel.GroupListener
            public void onSuccess(com.microsoft.appcenter.ingestion.models.Log log) {
                processCallback(log, new CallbackProcessor() { // from class: com.microsoft.appcenter.crashes.Crashes.6.3
                    @Override // com.microsoft.appcenter.crashes.Crashes.CallbackProcessor
                    public void onCallBack(ErrorReport errorReport) {
                        Crashes.this.mCrashesListener.onSendingSucceeded(errorReport);
                    }

                    @Override // com.microsoft.appcenter.crashes.Crashes.CallbackProcessor
                    public boolean shouldDeleteThrowable() {
                        return true;
                    }
                });
            }
        };
    }

    synchronized Device getDeviceInfo(Context context) {
        try {
            if (this.mDevice == null) {
                this.mDevice = DeviceInfoHelper.getDeviceInfo(context);
            }
        } catch (Throwable th) {
            throw th;
        }
        return this.mDevice;
    }

    @Override // com.microsoft.appcenter.AbstractAppCenterService
    protected String getGroupName() {
        return "groupErrors";
    }

    @Override // com.microsoft.appcenter.AppCenterService
    public Map getLogFactories() {
        return this.mFactories;
    }

    @Override // com.microsoft.appcenter.AbstractAppCenterService
    protected String getLoggerTag() {
        return "AppCenterCrashes";
    }

    @Override // com.microsoft.appcenter.AppCenterService
    public String getServiceName() {
        return "Crashes";
    }

    @Override // com.microsoft.appcenter.AbstractAppCenterService
    protected int getTriggerCount() {
        return 1;
    }

    @Override // com.microsoft.appcenter.AbstractAppCenterService, com.microsoft.appcenter.AppCenterService
    public synchronized void onStarted(Context context, Channel channel, String str, String str2, boolean z) {
        try {
            this.mContext = context;
            if (!isInstanceEnabled()) {
                ErrorLogHelper.removeMinidumpFolder();
                AppCenterLog.debug("AppCenterCrashes", "Clean up minidump folder.");
            }
            super.onStarted(context, channel, str, str2, z);
            if (isInstanceEnabled()) {
                processPendingErrors();
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    UUID saveUncaughtException(Thread thread, Throwable th, Exception exception) {
        if (!((Boolean) isEnabled().get()).booleanValue() || this.mSavedUncaughtException) {
            return null;
        }
        this.mSavedUncaughtException = true;
        return saveErrorLogFiles(th, ErrorLogHelper.createErrorLog(this.mContext, thread, exception, Thread.getAllStackTraces(), this.mInitializeTimestamp, true));
    }

    void saveUncaughtException(Thread thread, Throwable th) {
        String str;
        try {
            saveUncaughtException(thread, th, ErrorLogHelper.getModelExceptionFromThrowable(th));
        } catch (IOException e) {
            e = e;
            str = "Error writing error log to file";
            AppCenterLog.error("AppCenterCrashes", str, e);
        } catch (JSONException e2) {
            e = e2;
            str = "Error serializing error log to JSON";
            AppCenterLog.error("AppCenterCrashes", str, e);
        }
    }
}
