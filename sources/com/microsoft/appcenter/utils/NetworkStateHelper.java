package com.microsoft.appcenter.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import java.io.Closeable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes.dex */
public class NetworkStateHelper implements Closeable {
    private static NetworkStateHelper sSharedInstance;
    private final ConnectivityManager mConnectivityManager;
    private ConnectivityReceiver mConnectivityReceiver;
    private final Context mContext;
    private ConnectivityManager.NetworkCallback mNetworkCallback;
    private final Set mListeners = new CopyOnWriteArraySet();
    private final AtomicBoolean mConnected = new AtomicBoolean();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ConnectivityReceiver extends BroadcastReceiver {
        private ConnectivityReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            NetworkStateHelper.this.handleNetworkStateUpdate();
        }
    }

    /* loaded from: classes.dex */
    public interface Listener {
        void onNetworkStateUpdated(boolean z);
    }

    public NetworkStateHelper(Context context) {
        this.mContext = context.getApplicationContext();
        this.mConnectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        reopen();
    }

    private IntentFilter getOldIntentFilter() {
        return new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
    }

    public static synchronized NetworkStateHelper getSharedInstance(Context context) {
        NetworkStateHelper networkStateHelper;
        synchronized (NetworkStateHelper.class) {
            try {
                if (sSharedInstance == null) {
                    sSharedInstance = new NetworkStateHelper(context);
                }
                networkStateHelper = sSharedInstance;
            } catch (Throwable th) {
                throw th;
            }
        }
        return networkStateHelper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNetworkStateUpdate() {
        boolean isAnyNetworkConnected = isAnyNetworkConnected();
        if (this.mConnected.compareAndSet(!isAnyNetworkConnected, isAnyNetworkConnected)) {
            notifyNetworkStateUpdated(isAnyNetworkConnected);
        }
    }

    private boolean isAnyNetworkConnected() {
        Network[] allNetworks;
        NetworkInfo networkInfo;
        if (Build.VERSION.SDK_INT >= 21) {
            allNetworks = this.mConnectivityManager.getAllNetworks();
            if (allNetworks == null) {
                return false;
            }
            for (Network network : allNetworks) {
                networkInfo = this.mConnectivityManager.getNetworkInfo(network);
                if (networkInfo != null && networkInfo.isConnected()) {
                    return true;
                }
            }
        } else {
            NetworkInfo[] allNetworkInfo = this.mConnectivityManager.getAllNetworkInfo();
            if (allNetworkInfo == null) {
                return false;
            }
            for (NetworkInfo networkInfo2 : allNetworkInfo) {
                if (networkInfo2 != null && networkInfo2.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void notifyNetworkStateUpdated(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("Network has been ");
        sb.append(z ? "connected." : "disconnected.");
        AppCenterLog.debug("AppCenter", sb.toString());
        Iterator it = this.mListeners.iterator();
        while (it.hasNext()) {
            ((Listener) it.next()).onNetworkStateUpdated(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onNetworkAvailable(Network network) {
        AppCenterLog.debug("AppCenter", "Network " + network + " is available.");
        if (this.mConnected.compareAndSet(false, true)) {
            notifyNetworkStateUpdated(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onNetworkLost(Network network) {
        Network[] allNetworks;
        AppCenterLog.debug("AppCenter", "Network " + network + " is lost.");
        allNetworks = this.mConnectivityManager.getAllNetworks();
        if ((allNetworks == null || allNetworks.length == 0 || Arrays.equals(allNetworks, new Network[]{network})) && this.mConnected.compareAndSet(true, false)) {
            notifyNetworkStateUpdated(false);
        }
    }

    public void addListener(Listener listener) {
        this.mListeners.add(listener);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.mConnected.set(false);
        if (Build.VERSION.SDK_INT >= 21) {
            this.mConnectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
        } else {
            this.mContext.unregisterReceiver(this.mConnectivityReceiver);
        }
    }

    public boolean isNetworkConnected() {
        return this.mConnected.get() || isAnyNetworkConnected();
    }

    public void removeListener(Listener listener) {
        this.mListeners.remove(listener);
    }

    public void reopen() {
        NetworkRequest build;
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                NetworkRequest.Builder builder = new NetworkRequest.Builder();
                builder.addCapability(12);
                this.mNetworkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.microsoft.appcenter.utils.NetworkStateHelper.1
                    @Override // android.net.ConnectivityManager.NetworkCallback
                    public void onAvailable(Network network) {
                        NetworkStateHelper.this.onNetworkAvailable(network);
                    }

                    @Override // android.net.ConnectivityManager.NetworkCallback
                    public void onLost(Network network) {
                        NetworkStateHelper.this.onNetworkLost(network);
                    }
                };
                ConnectivityManager connectivityManager = this.mConnectivityManager;
                build = builder.build();
                connectivityManager.registerNetworkCallback(build, this.mNetworkCallback);
            } else {
                ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();
                this.mConnectivityReceiver = connectivityReceiver;
                this.mContext.registerReceiver(connectivityReceiver, getOldIntentFilter());
                handleNetworkStateUpdate();
            }
        } catch (RuntimeException e) {
            AppCenterLog.error("AppCenter", "Cannot access network state information.", e);
            this.mConnected.set(true);
        }
    }
}
