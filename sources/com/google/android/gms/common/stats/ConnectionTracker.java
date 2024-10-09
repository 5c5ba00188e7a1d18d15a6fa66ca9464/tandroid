package com.google.android.gms.common.stats;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.zzs;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.android.gms.common.wrappers.Wrappers;
import j$.util.concurrent.ConcurrentHashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public class ConnectionTracker {
    private static final Object zzb = new Object();
    private static volatile ConnectionTracker zzc;
    public ConcurrentHashMap zza = new ConcurrentHashMap();

    private ConnectionTracker() {
    }

    public static ConnectionTracker getInstance() {
        if (zzc == null) {
            synchronized (zzb) {
                try {
                    if (zzc == null) {
                        zzc = new ConnectionTracker();
                    }
                } finally {
                }
            }
        }
        ConnectionTracker connectionTracker = zzc;
        Preconditions.checkNotNull(connectionTracker);
        return connectionTracker;
    }

    private static void zzb(Context context, ServiceConnection serviceConnection) {
        try {
            context.unbindService(serviceConnection);
        } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException unused) {
        }
    }

    private final boolean zzc(Context context, String str, Intent intent, ServiceConnection serviceConnection, int i, boolean z, Executor executor) {
        ComponentName component = intent.getComponent();
        if (component != null) {
            String packageName = component.getPackageName();
            "com.google.android.gms".equals(packageName);
            try {
                if ((Wrappers.packageManager(context).getApplicationInfo(packageName, 0).flags & 2097152) != 0) {
                    Log.w("ConnectionTracker", "Attempted to bind to a service in a STOPPED package.");
                    return false;
                }
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }
        if (!zzd(serviceConnection)) {
            return zze(context, intent, serviceConnection, i, executor);
        }
        ServiceConnection serviceConnection2 = (ServiceConnection) this.zza.putIfAbsent(serviceConnection, serviceConnection);
        if (serviceConnection2 != null && serviceConnection != serviceConnection2) {
            Log.w("ConnectionTracker", String.format("Duplicate binding with the same ServiceConnection: %s, %s, %s.", serviceConnection, str, intent.getAction()));
        }
        try {
            boolean zze = zze(context, intent, serviceConnection, i, executor);
            if (zze) {
                return zze;
            }
            return false;
        } finally {
            this.zza.remove(serviceConnection, serviceConnection);
        }
    }

    private static boolean zzd(ServiceConnection serviceConnection) {
        return !(serviceConnection instanceof zzs);
    }

    private static final boolean zze(Context context, Intent intent, ServiceConnection serviceConnection, int i, Executor executor) {
        boolean bindService;
        if (!PlatformVersion.isAtLeastQ() || executor == null) {
            return context.bindService(intent, serviceConnection, i);
        }
        bindService = context.bindService(intent, i, executor, serviceConnection);
        return bindService;
    }

    public boolean bindService(Context context, Intent intent, ServiceConnection serviceConnection, int i) {
        return zzc(context, context.getClass().getName(), intent, serviceConnection, i, true, null);
    }

    public void unbindService(Context context, ServiceConnection serviceConnection) {
        if (!zzd(serviceConnection) || !this.zza.containsKey(serviceConnection)) {
            zzb(context, serviceConnection);
            return;
        }
        try {
            zzb(context, (ServiceConnection) this.zza.get(serviceConnection));
        } finally {
            this.zza.remove(serviceConnection);
        }
    }

    public final boolean zza(Context context, String str, Intent intent, ServiceConnection serviceConnection, int i, Executor executor) {
        return zzc(context, str, intent, serviceConnection, i, true, executor);
    }
}
