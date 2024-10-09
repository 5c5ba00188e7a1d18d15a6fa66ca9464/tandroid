package com.google.firebase.messaging;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import androidx.collection.ArrayMap;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.installations.FirebaseInstallationsApi;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class TopicsSubscriber {
    private static final long MAX_DELAY_SEC = TimeUnit.HOURS.toSeconds(8);
    private final Context context;
    private final FirebaseInstallationsApi firebaseInstallationsApi;
    private final FirebaseMessaging firebaseMessaging;
    private final Metadata metadata;
    private final GmsRpc rpc;
    private final TopicsStore store;
    private final ScheduledExecutorService syncExecutor;
    private final Map pendingOperations = new ArrayMap();
    private boolean syncScheduledOrRunning = false;

    private TopicsSubscriber(FirebaseMessaging firebaseMessaging, FirebaseInstallationsApi firebaseInstallationsApi, Metadata metadata, TopicsStore topicsStore, GmsRpc gmsRpc, Context context, ScheduledExecutorService scheduledExecutorService) {
        this.firebaseMessaging = firebaseMessaging;
        this.firebaseInstallationsApi = firebaseInstallationsApi;
        this.metadata = metadata;
        this.store = topicsStore;
        this.rpc = gmsRpc;
        this.context = context;
        this.syncExecutor = scheduledExecutorService;
    }

    private static Object awaitTask(Task task) {
        try {
            return Tasks.await(task, 30L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e = e;
            throw new IOException("SERVICE_NOT_AVAILABLE", e);
        } catch (ExecutionException e2) {
            Throwable cause = e2.getCause();
            if (cause instanceof IOException) {
                throw ((IOException) cause);
            }
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            throw new IOException(e2);
        } catch (TimeoutException e3) {
            e = e3;
            throw new IOException("SERVICE_NOT_AVAILABLE", e);
        }
    }

    private void blockingSubscribeToTopic(String str) {
        awaitTask(this.rpc.subscribeToTopic((String) awaitTask(this.firebaseInstallationsApi.getId()), this.firebaseMessaging.blockingGetToken(), str));
    }

    private void blockingUnsubscribeFromTopic(String str) {
        awaitTask(this.rpc.unsubscribeFromTopic((String) awaitTask(this.firebaseInstallationsApi.getId()), this.firebaseMessaging.blockingGetToken(), str));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Task createInstance(final FirebaseMessaging firebaseMessaging, final FirebaseInstallationsApi firebaseInstallationsApi, final Metadata metadata, final GmsRpc gmsRpc, final Context context, final ScheduledExecutorService scheduledExecutorService) {
        return Tasks.call(scheduledExecutorService, new Callable(context, scheduledExecutorService, firebaseMessaging, firebaseInstallationsApi, metadata, gmsRpc) { // from class: com.google.firebase.messaging.TopicsSubscriber$$Lambda$0
            private final Context arg$1;
            private final ScheduledExecutorService arg$2;
            private final FirebaseMessaging arg$3;
            private final FirebaseInstallationsApi arg$4;
            private final Metadata arg$5;
            private final GmsRpc arg$6;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.arg$1 = context;
                this.arg$2 = scheduledExecutorService;
                this.arg$3 = firebaseMessaging;
                this.arg$4 = firebaseInstallationsApi;
                this.arg$5 = metadata;
                this.arg$6 = gmsRpc;
            }

            @Override // java.util.concurrent.Callable
            public Object call() {
                return TopicsSubscriber.lambda$createInstance$0$TopicsSubscriber(this.arg$1, this.arg$2, this.arg$3, this.arg$4, this.arg$5, this.arg$6);
            }
        });
    }

    static boolean isDebugLogEnabled() {
        if (Log.isLoggable("FirebaseMessaging", 3)) {
            return true;
        }
        return Build.VERSION.SDK_INT == 23 && Log.isLoggable("FirebaseMessaging", 3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final /* synthetic */ TopicsSubscriber lambda$createInstance$0$TopicsSubscriber(Context context, ScheduledExecutorService scheduledExecutorService, FirebaseMessaging firebaseMessaging, FirebaseInstallationsApi firebaseInstallationsApi, Metadata metadata, GmsRpc gmsRpc) {
        return new TopicsSubscriber(firebaseMessaging, firebaseInstallationsApi, metadata, TopicsStore.getInstance(context, scheduledExecutorService), gmsRpc, context, scheduledExecutorService);
    }

    private void markCompletePendingOperation(TopicOperation topicOperation) {
        synchronized (this.pendingOperations) {
            try {
                String serialize = topicOperation.serialize();
                if (this.pendingOperations.containsKey(serialize)) {
                    ArrayDeque arrayDeque = (ArrayDeque) this.pendingOperations.get(serialize);
                    TaskCompletionSource taskCompletionSource = (TaskCompletionSource) arrayDeque.poll();
                    if (taskCompletionSource != null) {
                        taskCompletionSource.setResult(null);
                    }
                    if (arrayDeque.isEmpty()) {
                        this.pendingOperations.remove(serialize);
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void startSync() {
        if (isSyncScheduledOrRunning()) {
            return;
        }
        syncWithDelaySecondsInternal(0L);
    }

    boolean hasPendingOperation() {
        return this.store.getNextTopicOperation() != null;
    }

    synchronized boolean isSyncScheduledOrRunning() {
        return this.syncScheduledOrRunning;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x002e  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x008c A[Catch: IOException -> 0x005a, TryCatch #0 {IOException -> 0x005a, blocks: (B:3:0x0003, B:14:0x0030, B:16:0x0036, B:17:0x0056, B:21:0x005c, B:23:0x0069, B:24:0x008c, B:26:0x0099), top: B:2:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00c9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    boolean performTopicOperation(TopicOperation topicOperation) {
        String sb;
        String operation;
        int hashCode;
        char c;
        String sb2;
        try {
            operation = topicOperation.getOperation();
            hashCode = operation.hashCode();
        } catch (IOException e) {
            if ("SERVICE_NOT_AVAILABLE".equals(e.getMessage())) {
            }
            String message = e.getMessage();
            StringBuilder sb3 = new StringBuilder(String.valueOf(message).length() + 53);
            sb3.append("Topic operation failed: ");
            sb3.append(message);
            sb3.append(". Will retry Topic operation.");
            sb = sb3.toString();
            Log.e("FirebaseMessaging", sb);
            return false;
        }
        if (hashCode == 83) {
            if (operation.equals("S")) {
                c = 0;
                if (c != 0) {
                }
                if ("SERVICE_NOT_AVAILABLE".equals(e.getMessage())) {
                }
                String message2 = e.getMessage();
                StringBuilder sb32 = new StringBuilder(String.valueOf(message2).length() + 53);
                sb32.append("Topic operation failed: ");
                sb32.append(message2);
                sb32.append(". Will retry Topic operation.");
                sb = sb32.toString();
                Log.e("FirebaseMessaging", sb);
                return false;
            }
            c = 65535;
            if (c != 0) {
            }
            if ("SERVICE_NOT_AVAILABLE".equals(e.getMessage())) {
            }
            String message22 = e.getMessage();
            StringBuilder sb322 = new StringBuilder(String.valueOf(message22).length() + 53);
            sb322.append("Topic operation failed: ");
            sb322.append(message22);
            sb322.append(". Will retry Topic operation.");
            sb = sb322.toString();
            Log.e("FirebaseMessaging", sb);
            return false;
        }
        if (hashCode == 85 && operation.equals("U")) {
            c = 1;
            if (c != 0) {
                blockingSubscribeToTopic(topicOperation.getTopic());
                if (isDebugLogEnabled()) {
                    String topic = topicOperation.getTopic();
                    StringBuilder sb4 = new StringBuilder(String.valueOf(topic).length() + 31);
                    sb4.append("Subscribe to topic: ");
                    sb4.append(topic);
                    sb4.append(" succeeded.");
                    sb2 = sb4.toString();
                    Log.d("FirebaseMessaging", sb2);
                }
                return true;
            }
            if (c != 1) {
                if (isDebugLogEnabled()) {
                    String valueOf = String.valueOf(topicOperation);
                    StringBuilder sb5 = new StringBuilder(valueOf.length() + 24);
                    sb5.append("Unknown topic operation");
                    sb5.append(valueOf);
                    sb5.append(".");
                    sb2 = sb5.toString();
                    Log.d("FirebaseMessaging", sb2);
                }
                return true;
            }
            blockingUnsubscribeFromTopic(topicOperation.getTopic());
            if (isDebugLogEnabled()) {
                String topic2 = topicOperation.getTopic();
                StringBuilder sb6 = new StringBuilder(String.valueOf(topic2).length() + 35);
                sb6.append("Unsubscribe from topic: ");
                sb6.append(topic2);
                sb6.append(" succeeded.");
                sb2 = sb6.toString();
                Log.d("FirebaseMessaging", sb2);
            }
            return true;
            if (!"SERVICE_NOT_AVAILABLE".equals(e.getMessage()) || "INTERNAL_SERVER_ERROR".equals(e.getMessage())) {
                String message222 = e.getMessage();
                StringBuilder sb3222 = new StringBuilder(String.valueOf(message222).length() + 53);
                sb3222.append("Topic operation failed: ");
                sb3222.append(message222);
                sb3222.append(". Will retry Topic operation.");
                sb = sb3222.toString();
            } else {
                if (e.getMessage() != null) {
                    throw e;
                }
                sb = "Topic operation failed without exception message. Will retry Topic operation.";
            }
            Log.e("FirebaseMessaging", sb);
            return false;
        }
        c = 65535;
        if (c != 0) {
        }
        if ("SERVICE_NOT_AVAILABLE".equals(e.getMessage())) {
        }
        String message2222 = e.getMessage();
        StringBuilder sb32222 = new StringBuilder(String.valueOf(message2222).length() + 53);
        sb32222.append("Topic operation failed: ");
        sb32222.append(message2222);
        sb32222.append(". Will retry Topic operation.");
        sb = sb32222.toString();
        Log.e("FirebaseMessaging", sb);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleSyncTaskWithDelaySeconds(Runnable runnable, long j) {
        this.syncExecutor.schedule(runnable, j, TimeUnit.SECONDS);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setSyncScheduledOrRunning(boolean z) {
        this.syncScheduledOrRunning = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startTopicsSyncIfNecessary() {
        if (hasPendingOperation()) {
            startSync();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x000d, code lost:
    
        if (isDebugLogEnabled() == false) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x000f, code lost:
    
        android.util.Log.d("FirebaseMessaging", "topic sync succeeded");
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x001a, code lost:
    
        return true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean syncTopics() {
        while (true) {
            synchronized (this) {
                try {
                    TopicOperation nextTopicOperation = this.store.getNextTopicOperation();
                    if (nextTopicOperation == null) {
                        break;
                    }
                    if (!performTopicOperation(nextTopicOperation)) {
                        return false;
                    }
                    this.store.removeTopicOperation(nextTopicOperation);
                    markCompletePendingOperation(nextTopicOperation);
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void syncWithDelaySecondsInternal(long j) {
        scheduleSyncTaskWithDelaySeconds(new TopicsSyncTask(this, this.context, this.metadata, Math.min(Math.max(30L, j + j), MAX_DELAY_SEC)), j);
        setSyncScheduledOrRunning(true);
    }
}
