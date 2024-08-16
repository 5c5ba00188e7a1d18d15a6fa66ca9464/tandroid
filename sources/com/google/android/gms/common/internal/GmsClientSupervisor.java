package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.ServiceConnection;
import android.os.HandlerThread;
import android.os.Looper;
import java.util.concurrent.Executor;
/* compiled from: com.google.android.gms:play-services-basement@@18.1.0 */
/* loaded from: classes.dex */
public abstract class GmsClientSupervisor {
    static HandlerThread zza = null;
    private static int zzb = 4225;
    private static final Object zzc = new Object();
    private static zzr zzd = null;
    private static boolean zze = false;

    public static int getDefaultBindFlags() {
        return zzb;
    }

    public static GmsClientSupervisor getInstance(Context context) {
        Looper mainLooper;
        synchronized (zzc) {
            try {
                if (zzd == null) {
                    Context applicationContext = context.getApplicationContext();
                    if (zze) {
                        mainLooper = getOrStartHandlerThread().getLooper();
                    } else {
                        mainLooper = context.getMainLooper();
                    }
                    zzd = new zzr(applicationContext, mainLooper);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return zzd;
    }

    public static HandlerThread getOrStartHandlerThread() {
        synchronized (zzc) {
            try {
                HandlerThread handlerThread = zza;
                if (handlerThread != null) {
                    return handlerThread;
                }
                HandlerThread handlerThread2 = new HandlerThread("GoogleApiHandler", 9);
                zza = handlerThread2;
                handlerThread2.start();
                return zza;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    protected abstract void zza(zzn zznVar, ServiceConnection serviceConnection, String str);

    public final void zzb(String str, String str2, int i, ServiceConnection serviceConnection, String str3, boolean z) {
        zza(new zzn(str, str2, i, z), serviceConnection, str3);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract boolean zzc(zzn zznVar, ServiceConnection serviceConnection, String str, Executor executor);
}
