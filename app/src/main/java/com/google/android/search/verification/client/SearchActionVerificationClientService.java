package com.google.android.search.verification.client;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.google.android.search.verification.api.ISearchActionVerificationService;
/* loaded from: classes.dex */
public abstract class SearchActionVerificationClientService extends IntentService {
    private static final int CONNECTION_TIMEOUT_IN_MS = 1000;
    public static final String EXTRA_INTENT = "SearchActionVerificationClientExtraIntent";
    private static final long MS_TO_NS = 1000000;
    private static final String NOTIFICATION_CHANNEL_ID = "Assistant_verifier";
    private static final int NOTIFICATION_ID = 10000;
    private static final String REMOTE_ASSISTANT_GO_SERVICE_ACTION = "com.google.android.apps.assistant.go.verification.VERIFICATION_SERVICE";
    private static final String REMOTE_GSA_SERVICE_ACTION = "com.google.android.googlequicksearchbox.SEARCH_ACTION_VERIFICATION_SERVICE";
    private static final String SEND_MESSAGE_ERROR_MESSAGE = "com.google.android.voicesearch.extra.ERROR_MESSAGE";
    private static final String SEND_MESSAGE_RESULT_RECEIVER = "com.google.android.voicesearch.extra.SEND_MESSAGE_RESULT_RECEIVER";
    private static final String TAG = "SAVerificationClientS";
    private static final int TIME_TO_SLEEP_IN_MS = 50;
    private final Intent assistantGoServiceIntent;
    private SearchActionVerificationServiceConnection assistantGoVerificationServiceConnection;
    private final long connectionTimeout;
    private final boolean dbg = isDebugMode();
    private final Intent gsaServiceIntent;
    private SearchActionVerificationServiceConnection searchActionVerificationServiceConnection;

    public long getConnectionTimeout() {
        return 1000L;
    }

    public boolean isTestingMode() {
        return false;
    }

    public abstract void performAction(Intent intent, boolean isVerified, Bundle options) throws Exception;

    private boolean isDebugMode() {
        return isTestingMode() || !"user".equals(Build.TYPE);
    }

    public SearchActionVerificationClientService() {
        super("SearchActionVerificationClientService");
        Intent intent = new Intent("com.google.android.googlequicksearchbox.SEARCH_ACTION_VERIFICATION_SERVICE").setPackage("com.google.android.googlequicksearchbox");
        this.gsaServiceIntent = intent;
        Intent intent2 = new Intent("com.google.android.apps.assistant.go.verification.VERIFICATION_SERVICE").setPackage("com.google.android.apps.assistant");
        this.assistantGoServiceIntent = intent2;
        if (isTestingMode()) {
            intent.setPackage("com.google.verificationdemo.fakeverification");
            intent2.setPackage("com.google.verificationdemo.fakeverification");
        }
        this.connectionTimeout = getConnectionTimeout();
    }

    /* loaded from: classes.dex */
    public class SearchActionVerificationServiceConnection implements ServiceConnection {
        private ISearchActionVerificationService iRemoteService;

        SearchActionVerificationServiceConnection() {
            SearchActionVerificationClientService.this = this$0;
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            if (SearchActionVerificationClientService.this.dbg) {
                Log.d("SAVerificationClientS", "onServiceConnected");
            }
            this.iRemoteService = ISearchActionVerificationService.Stub.asInterface(binder);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            this.iRemoteService = null;
            if (SearchActionVerificationClientService.this.dbg) {
                Log.d("SAVerificationClientS", "onServiceDisconnected");
            }
        }

        public ISearchActionVerificationService getRemoteService() {
            return this.iRemoteService;
        }

        public boolean isVerified(Intent intent, Bundle options) throws RemoteException {
            ISearchActionVerificationService iSearchActionVerificationService = this.iRemoteService;
            return iSearchActionVerificationService != null && iSearchActionVerificationService.isSearchAction(intent, options);
        }

        public boolean isConnected() {
            return this.iRemoteService != null;
        }
    }

    private boolean isPackageSafe(String packageName) {
        return isPackageInstalled(packageName) && (isDebugMode() || SearchActionVerificationClientUtil.isPackageGoogleSigned(this, packageName));
    }

    private boolean installedServicesConnected() {
        boolean isPackageInstalled = isPackageInstalled("com.google.android.googlequicksearchbox");
        boolean z = !isPackageInstalled || this.searchActionVerificationServiceConnection.isConnected();
        if (this.dbg) {
            Log.d("SAVerificationClientS", String.format("GSA app %s installed: %s connected %s", "com.google.android.googlequicksearchbox", Boolean.valueOf(isPackageInstalled), Boolean.valueOf(this.searchActionVerificationServiceConnection.isConnected())));
        }
        boolean isPackageInstalled2 = isPackageInstalled("com.google.android.apps.assistant");
        boolean z2 = !isPackageInstalled2 || this.assistantGoVerificationServiceConnection.isConnected();
        if (this.dbg) {
            Log.d("SAVerificationClientS", String.format("AssistantGo app %s installed: %s connected %s", "com.google.android.apps.assistant", Boolean.valueOf(isPackageInstalled2), Boolean.valueOf(this.assistantGoVerificationServiceConnection.isConnected())));
        }
        return z && z2;
    }

    private boolean isPackageInstalled(String packageName) {
        ApplicationInfo applicationInfo;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(packageName, 0);
            if (packageInfo != null && (applicationInfo = packageInfo.applicationInfo) != null) {
                if (applicationInfo.enabled) {
                    return true;
                }
            }
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            Log.w("SAVerificationClientS", String.format("Couldn't find package name %s", packageName), e);
            return false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:50:0x0113  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean maybePerformActionIfVerified(String packageName, Intent intent, SearchActionVerificationServiceConnection searchActionVerificationServiceConnection) {
        boolean isVerified;
        int i = 0;
        if (!packageName.equals("com.google.android.googlequicksearchbox") && !packageName.equals("com.google.android.apps.assistant")) {
            if (this.dbg) {
                Log.d("SAVerificationClientS", String.format("Unsupported package %s for verification.", packageName));
            }
            return false;
        }
        if (!(isDebugMode() || SearchActionVerificationClientUtil.isPackageGoogleSigned(this, packageName))) {
            if (this.dbg) {
                Log.d("SAVerificationClientS", String.format("Cannot verify the intent with package %s in unsafe mode.", packageName));
            }
            return false;
        } else if (!intent.hasExtra("SearchActionVerificationClientExtraIntent")) {
            if (this.dbg) {
                String valueOf = String.valueOf(intent);
                StringBuilder sb = new StringBuilder(valueOf.length() + 28);
                sb.append("No extra, nothing to check: ");
                sb.append(valueOf);
                Log.d("SAVerificationClientS", sb.toString());
            }
            return false;
        } else {
            Intent intent2 = (Intent) intent.getParcelableExtra("SearchActionVerificationClientExtraIntent");
            if (this.dbg) {
                SearchActionVerificationClientUtil.logIntentWithExtras(intent2);
            }
            String str = "VerificationService is not connected to %s, unable to check intent: %s";
            if (searchActionVerificationServiceConnection.isConnected()) {
                try {
                    Log.i("SAVerificationClientS", String.format("%s Service API version: %s", packageName, Integer.valueOf(searchActionVerificationServiceConnection.getRemoteService().getVersion())));
                    Bundle bundle = new Bundle();
                    isVerified = searchActionVerificationServiceConnection.isVerified(intent2, bundle);
                    performAction(intent2, isVerified, bundle);
                    str = "";
                } catch (RemoteException e) {
                    String valueOf2 = String.valueOf(e.getMessage());
                    Log.e("SAVerificationClientS", valueOf2.length() != 0 ? "Remote exception: ".concat(valueOf2) : new String("Remote exception: "));
                    str = e.getMessage();
                } catch (Exception e2) {
                    String valueOf3 = String.valueOf(e2.getMessage());
                    Log.e("SAVerificationClientS", valueOf3.length() != 0 ? "Exception: ".concat(valueOf3) : new String("Exception: "));
                    str = e2.getMessage();
                }
                if (intent2.hasExtra("com.google.android.voicesearch.extra.SEND_MESSAGE_RESULT_RECEIVER")) {
                    ResultReceiver resultReceiver = (ResultReceiver) intent2.getExtras().getParcelable("com.google.android.voicesearch.extra.SEND_MESSAGE_RESULT_RECEIVER");
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("com.google.android.voicesearch.extra.ERROR_MESSAGE", str);
                    if (!isVerified) {
                        i = -1;
                    }
                    resultReceiver.send(i, bundle2);
                }
                return isVerified;
            }
            Log.e("SAVerificationClientS", String.format(str, packageName, intent));
            isVerified = false;
            if (intent2.hasExtra("com.google.android.voicesearch.extra.SEND_MESSAGE_RESULT_RECEIVER")) {
            }
            return isVerified;
        }
    }

    @Override // android.app.IntentService
    protected final void onHandleIntent(Intent intent) {
        if (intent == null) {
            if (!this.dbg) {
                return;
            }
            Log.d("SAVerificationClientS", "Unable to verify null intent");
            return;
        }
        long nanoTime = System.nanoTime();
        while (!installedServicesConnected() && System.nanoTime() - nanoTime < this.connectionTimeout * 1000000) {
            try {
                Thread.sleep(50L);
            } catch (InterruptedException e) {
                if (this.dbg) {
                    String valueOf = String.valueOf(e);
                    StringBuilder sb = new StringBuilder(valueOf.length() + 33);
                    sb.append("Unexpected InterruptedException: ");
                    sb.append(valueOf);
                    Log.d("SAVerificationClientS", sb.toString());
                }
            }
        }
        if (maybePerformActionIfVerified("com.google.android.googlequicksearchbox", intent, this.searchActionVerificationServiceConnection)) {
            Log.i("SAVerificationClientS", "Verified the intent with GSA.");
            return;
        }
        Log.i("SAVerificationClientS", "Unable to verify the intent with GSA.");
        if (maybePerformActionIfVerified("com.google.android.apps.assistant", intent, this.assistantGoVerificationServiceConnection)) {
            Log.i("SAVerificationClientS", "Verified the intent with Assistant Go.");
        } else {
            Log.i("SAVerificationClientS", "Unable to verify the intent with Assistant Go.");
        }
    }

    @Override // android.app.IntentService, android.app.Service
    public final void onCreate() {
        if (this.dbg) {
            Log.d("SAVerificationClientS", "onCreate");
        }
        super.onCreate();
        this.searchActionVerificationServiceConnection = new SearchActionVerificationServiceConnection();
        if (isPackageSafe("com.google.android.googlequicksearchbox")) {
            bindService(this.gsaServiceIntent, this.searchActionVerificationServiceConnection, 1);
        }
        this.assistantGoVerificationServiceConnection = new SearchActionVerificationServiceConnection();
        if (isPackageSafe("com.google.android.apps.assistant")) {
            bindService(this.assistantGoServiceIntent, this.assistantGoVerificationServiceConnection, 1);
        }
        if (Build.VERSION.SDK_INT >= 26) {
            postForegroundNotification();
        }
    }

    @Override // android.app.IntentService, android.app.Service
    public final void onDestroy() {
        if (this.dbg) {
            Log.d("SAVerificationClientS", "onDestroy");
        }
        super.onDestroy();
        if (this.searchActionVerificationServiceConnection.isConnected()) {
            unbindService(this.searchActionVerificationServiceConnection);
        }
        if (this.assistantGoVerificationServiceConnection.isConnected()) {
            unbindService(this.assistantGoVerificationServiceConnection);
        }
        if (Build.VERSION.SDK_INT >= 26) {
            stopForeground(true);
        }
    }

    protected void postForegroundNotification() {
        createChannel();
        startForeground(10000, new NotificationCompat.Builder(getApplicationContext(), "Assistant_verifier").setGroup("Assistant_verifier").setContentTitle(getApplicationContext().getResources().getString(R$string.google_assistant_verification_notification_title)).setSmallIcon(17301545).setPriority(-2).setVisibility(1).build());
    }

    private void createChannel() {
        NotificationChannel notificationChannel = new NotificationChannel("Assistant_verifier", getApplicationContext().getResources().getString(R$string.google_assistant_verification_channel_name), 2);
        notificationChannel.enableVibration(false);
        notificationChannel.enableLights(false);
        notificationChannel.setShowBadge(false);
        ((NotificationManager) getApplicationContext().getSystemService(NotificationManager.class)).createNotificationChannel(notificationChannel);
    }
}
