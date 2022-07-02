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
/* compiled from: com.google.firebase:firebase-messaging@@22.0.0 */
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
    private final Map<String, ArrayDeque<TaskCompletionSource<Void>>> pendingOperations = new ArrayMap();
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

    private static <T> T awaitTask(Task<T> task) throws IOException {
        Throwable e;
        try {
            return (T) Tasks.await(task, 30L, TimeUnit.SECONDS);
        } catch (InterruptedException e2) {
            e = e2;
            throw new IOException("SERVICE_NOT_AVAILABLE", e);
        } catch (ExecutionException e3) {
            Throwable cause = e3.getCause();
            if (cause instanceof IOException) {
                throw ((IOException) cause);
            }
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            throw new IOException(e3);
        } catch (TimeoutException e4) {
            e = e4;
            throw new IOException("SERVICE_NOT_AVAILABLE", e);
        }
    }

    private void blockingSubscribeToTopic(String str) throws IOException {
        awaitTask(this.rpc.subscribeToTopic((String) awaitTask(this.firebaseInstallationsApi.getId()), this.firebaseMessaging.blockingGetToken(), str));
    }

    private void blockingUnsubscribeFromTopic(String str) throws IOException {
        awaitTask(this.rpc.unsubscribeFromTopic((String) awaitTask(this.firebaseInstallationsApi.getId()), this.firebaseMessaging.blockingGetToken(), str));
    }

    public static Task<TopicsSubscriber> createInstance(FirebaseMessaging firebaseMessaging, FirebaseInstallationsApi firebaseInstallationsApi, Metadata metadata, GmsRpc gmsRpc, Context context, ScheduledExecutorService scheduledExecutorService) {
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
        return Log.isLoggable("FirebaseMessaging", 3) || (Build.VERSION.SDK_INT == 23 && Log.isLoggable("FirebaseMessaging", 3));
    }

    public static final /* synthetic */ TopicsSubscriber lambda$createInstance$0$TopicsSubscriber(Context context, ScheduledExecutorService scheduledExecutorService, FirebaseMessaging firebaseMessaging, FirebaseInstallationsApi firebaseInstallationsApi, Metadata metadata, GmsRpc gmsRpc) throws Exception {
        return new TopicsSubscriber(firebaseMessaging, firebaseInstallationsApi, metadata, TopicsStore.getInstance(context, scheduledExecutorService), gmsRpc, context, scheduledExecutorService);
    }

    private void markCompletePendingOperation(TopicOperation topicOperation) {
        synchronized (this.pendingOperations) {
            String serialize = topicOperation.serialize();
            if (!this.pendingOperations.containsKey(serialize)) {
                return;
            }
            ArrayDeque<TaskCompletionSource<Void>> arrayDeque = this.pendingOperations.get(serialize);
            TaskCompletionSource<Void> poll = arrayDeque.poll();
            if (poll != null) {
                poll.setResult(null);
            }
            if (arrayDeque.isEmpty()) {
                this.pendingOperations.remove(serialize);
            }
        }
    }

    private void startSync() {
        if (!isSyncScheduledOrRunning()) {
            syncWithDelaySecondsInternal(0L);
        }
    }

    boolean hasPendingOperation() {
        return this.store.getNextTopicOperation() != null;
    }

    synchronized boolean isSyncScheduledOrRunning() {
        return this.syncScheduledOrRunning;
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x002e  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x008d A[Catch: IOException -> 0x00c0, TryCatch #0 {IOException -> 0x00c0, blocks: (B:3:0x0003, B:18:0x0030, B:20:0x0036, B:21:0x005a, B:23:0x0067, B:24:0x008d, B:26:0x009a), top: B:40:0x0003 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    boolean performTopicOperation(TopicOperation topicOperation) throws IOException {
        char c;
        try {
            String operation = topicOperation.getOperation();
            int hashCode = operation.hashCode();
            if (hashCode == 83) {
                if (operation.equals("S")) {
                    c = 0;
                    if (c != 0) {
                    }
                    return true;
                }
                c = 65535;
                if (c != 0) {
                }
                return true;
            }
            if (hashCode == 85 && operation.equals("U")) {
                c = 1;
                if (c != 0) {
                    blockingSubscribeToTopic(topicOperation.getTopic());
                    if (isDebugLogEnabled()) {
                        String topic = topicOperation.getTopic();
                        StringBuilder sb = new StringBuilder(String.valueOf(topic).length() + 31);
                        sb.append("Subscribe to topic: ");
                        sb.append(topic);
                        sb.append(" succeeded.");
                        Log.d("FirebaseMessaging", sb.toString());
                    }
                } else if (c == 1) {
                    blockingUnsubscribeFromTopic(topicOperation.getTopic());
                    if (isDebugLogEnabled()) {
                        String topic2 = topicOperation.getTopic();
                        StringBuilder sb2 = new StringBuilder(String.valueOf(topic2).length() + 35);
                        sb2.append("Unsubscribe from topic: ");
                        sb2.append(topic2);
                        sb2.append(" succeeded.");
                        Log.d("FirebaseMessaging", sb2.toString());
                    }
                } else if (isDebugLogEnabled()) {
                    String valueOf = String.valueOf(topicOperation);
                    StringBuilder sb3 = new StringBuilder(valueOf.length() + 24);
                    sb3.append("Unknown topic operation");
                    sb3.append(valueOf);
                    sb3.append(".");
                    Log.d("FirebaseMessaging", sb3.toString());
                }
                return true;
            }
            c = 65535;
            if (c != 0) {
            }
            return true;
        } catch (IOException e) {
            if ("SERVICE_NOT_AVAILABLE".equals(e.getMessage()) || "INTERNAL_SERVER_ERROR".equals(e.getMessage())) {
                String message = e.getMessage();
                StringBuilder sb4 = new StringBuilder(String.valueOf(message).length() + 53);
                sb4.append("Topic operation failed: ");
                sb4.append(message);
                sb4.append(". Will retry Topic operation.");
                Log.e("FirebaseMessaging", sb4.toString());
                return false;
            } else if (e.getMessage() == null) {
                Log.e("FirebaseMessaging", "Topic operation failed without exception message. Will retry Topic operation.");
                return false;
            } else {
                throw e;
            }
        }
    }

    public void scheduleSyncTaskWithDelaySeconds(Runnable runnable, long j) {
        this.syncExecutor.schedule(runnable, j, TimeUnit.SECONDS);
    }

    public synchronized void setSyncScheduledOrRunning(boolean z) {
        this.syncScheduledOrRunning = z;
    }

    public void startTopicsSyncIfNecessary() {
        if (hasPendingOperation()) {
            startSync();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:6:0x000d, code lost:
        if (isDebugLogEnabled() == false) goto L8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x000f, code lost:
        android.util.Log.d("FirebaseMessaging", "topic sync succeeded");
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0017, code lost:
        return true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean syncTopics() throws IOException {
        while (true) {
            synchronized (this) {
                TopicOperation nextTopicOperation = this.store.getNextTopicOperation();
                if (nextTopicOperation == null) {
                    break;
                } else if (!performTopicOperation(nextTopicOperation)) {
                    return false;
                } else {
                    this.store.removeTopicOperation(nextTopicOperation);
                    markCompletePendingOperation(nextTopicOperation);
                }
            }
        }
    }

    public void syncWithDelaySecondsInternal(long j) {
        scheduleSyncTaskWithDelaySeconds(new TopicsSyncTask(this, this.context, this.metadata, Math.min(Math.max(30L, j + j), MAX_DELAY_SEC)), j);
        setSyncScheduledOrRunning(true);
    }
}
