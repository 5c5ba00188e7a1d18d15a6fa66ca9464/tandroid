package com.google.firebase.messaging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import com.google.android.gms.common.util.concurrent.NamedThreadFactory;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class SyncTask implements Runnable {
    private final FirebaseMessaging firebaseMessaging;
    private final long nextDelaySeconds;
    ExecutorService processorExecutor = new ThreadPoolExecutor(0, 1, 30, TimeUnit.SECONDS, new LinkedBlockingQueue(), new NamedThreadFactory("firebase-iid-executor"));
    private final PowerManager.WakeLock syncWakeLock;

    /* loaded from: classes.dex */
    static class ConnectivityChangeReceiver extends BroadcastReceiver {
        private SyncTask task;

        public ConnectivityChangeReceiver(SyncTask syncTask) {
            this.task = syncTask;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            SyncTask syncTask = this.task;
            if (syncTask != null && syncTask.isDeviceConnected()) {
                if (SyncTask.isDebugLogEnabled()) {
                    Log.d("FirebaseMessaging", "Connectivity changed. Starting background sync.");
                }
                this.task.firebaseMessaging.enqueueTaskWithDelaySeconds(this.task, 0L);
                this.task.getContext().unregisterReceiver(this);
                this.task = null;
            }
        }

        public void registerReceiver() {
            if (SyncTask.isDebugLogEnabled()) {
                Log.d("FirebaseMessaging", "Connectivity change received registered");
            }
            this.task.getContext().registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        }
    }

    public SyncTask(FirebaseMessaging firebaseMessaging, long j) {
        this.firebaseMessaging = firebaseMessaging;
        this.nextDelaySeconds = j;
        PowerManager.WakeLock newWakeLock = ((PowerManager) getContext().getSystemService("power")).newWakeLock(1, "fiid-sync");
        this.syncWakeLock = newWakeLock;
        newWakeLock.setReferenceCounted(false);
    }

    static boolean isDebugLogEnabled() {
        if (Log.isLoggable("FirebaseMessaging", 3)) {
            return true;
        }
        return Build.VERSION.SDK_INT == 23 && Log.isLoggable("FirebaseMessaging", 3);
    }

    Context getContext() {
        return this.firebaseMessaging.getApplicationContext();
    }

    boolean isDeviceConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    boolean maybeRefreshToken() {
        String str;
        try {
            if (this.firebaseMessaging.blockingGetToken() == null) {
                Log.e("FirebaseMessaging", "Token retrieval failed: null");
                return false;
            }
            if (!Log.isLoggable("FirebaseMessaging", 3)) {
                return true;
            }
            Log.d("FirebaseMessaging", "Token successfully retrieved");
            return true;
        } catch (IOException e) {
            if (GmsRpc.isErrorMessageForRetryableError(e.getMessage())) {
                String message = e.getMessage();
                StringBuilder sb = new StringBuilder(String.valueOf(message).length() + 52);
                sb.append("Token retrieval failed: ");
                sb.append(message);
                sb.append(". Will retry token retrieval");
                str = sb.toString();
            } else {
                if (e.getMessage() != null) {
                    throw e;
                }
                str = "Token retrieval failed without exception message. Will retry token retrieval";
            }
            Log.w("FirebaseMessaging", str);
            return false;
        } catch (SecurityException unused) {
            str = "Token retrieval failed with SecurityException. Will retry token retrieval";
            Log.w("FirebaseMessaging", str);
            return false;
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        if (ServiceStarter.getInstance().hasWakeLockPermission(getContext())) {
            this.syncWakeLock.acquire();
        }
        try {
            try {
                this.firebaseMessaging.setSyncScheduledOrRunning(true);
                if (!this.firebaseMessaging.isGmsCorePresent()) {
                    this.firebaseMessaging.setSyncScheduledOrRunning(false);
                    if (!ServiceStarter.getInstance().hasWakeLockPermission(getContext())) {
                        return;
                    }
                } else if (!ServiceStarter.getInstance().hasAccessNetworkStatePermission(getContext()) || isDeviceConnected()) {
                    if (maybeRefreshToken()) {
                        this.firebaseMessaging.setSyncScheduledOrRunning(false);
                    } else {
                        this.firebaseMessaging.syncWithDelaySecondsInternal(this.nextDelaySeconds);
                    }
                    if (!ServiceStarter.getInstance().hasWakeLockPermission(getContext())) {
                        return;
                    }
                } else {
                    new ConnectivityChangeReceiver(this).registerReceiver();
                    if (!ServiceStarter.getInstance().hasWakeLockPermission(getContext())) {
                        return;
                    }
                }
            } catch (IOException e) {
                String message = e.getMessage();
                StringBuilder sb = new StringBuilder(String.valueOf(message).length() + 93);
                sb.append("Topic sync or token retrieval failed on hard failure exceptions: ");
                sb.append(message);
                sb.append(". Won't retry the operation.");
                Log.e("FirebaseMessaging", sb.toString());
                this.firebaseMessaging.setSyncScheduledOrRunning(false);
                if (!ServiceStarter.getInstance().hasWakeLockPermission(getContext())) {
                    return;
                }
            }
            this.syncWakeLock.release();
        } catch (Throwable th) {
            if (ServiceStarter.getInstance().hasWakeLockPermission(getContext())) {
                this.syncWakeLock.release();
            }
            throw th;
        }
    }
}
