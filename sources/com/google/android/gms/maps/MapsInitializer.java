package com.google.android.gms.maps;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.maps.internal.zzcb;
import com.google.android.gms.maps.internal.zzf;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.RuntimeRemoteException;
/* compiled from: com.google.android.gms:play-services-maps@@18.1.0 */
/* loaded from: classes.dex */
public final class MapsInitializer {
    private static final String zza = "MapsInitializer";
    private static boolean zzb = false;
    private static Renderer zzc = Renderer.LEGACY;

    /* compiled from: com.google.android.gms:play-services-maps@@18.1.0 */
    /* loaded from: classes.dex */
    public enum Renderer {
        LEGACY,
        LATEST
    }

    private MapsInitializer() {
    }

    public static synchronized int initialize(Context context) {
        int initialize;
        synchronized (MapsInitializer.class) {
            initialize = initialize(context, null, null);
        }
        return initialize;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(14:14|15|16|17|(10:19|(1:(1:22))|23|24|(1:26)|27|28|(1:30)|31|32)|36|23|24|(0)|27|28|(0)|31|32) */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x005c, code lost:
        r5 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x005d, code lost:
        android.util.Log.e(com.google.android.gms.maps.MapsInitializer.zza, "Failed to retrieve renderer type or log initialization.", r5);
     */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0050 A[Catch: RemoteException -> 0x005c, all -> 0x008a, TryCatch #0 {RemoteException -> 0x005c, blocks: (B:21:0x004a, B:23:0x0050, B:24:0x0054), top: B:43:0x004a, outer: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0077 A[Catch: all -> 0x008a, TRY_LEAVE, TryCatch #3 {, blocks: (B:4:0x0003, B:7:0x001f, B:10:0x0026, B:11:0x002a, B:13:0x0039, B:15:0x003e, B:21:0x004a, B:23:0x0050, B:24:0x0054, B:28:0x0064, B:30:0x0077, B:27:0x005d, B:34:0x007f, B:35:0x0084, B:37:0x0086), top: B:49:0x0003, inners: #0, #1, #2 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static synchronized int initialize(Context context, Renderer renderer, OnMapsSdkInitializedCallback onMapsSdkInitializedCallback) {
        synchronized (MapsInitializer.class) {
            Preconditions.checkNotNull(context, "Context is null");
            Log.d(zza, "preferredRenderer: ".concat(String.valueOf(renderer)));
            if (zzb) {
                if (onMapsSdkInitializedCallback != null) {
                    onMapsSdkInitializedCallback.onMapsSdkInitialized(zzc);
                }
                return 0;
            }
            try {
                zzf zza2 = zzcb.zza(context, renderer);
                try {
                    CameraUpdateFactory.zza(zza2.zze());
                    BitmapDescriptorFactory.zza(zza2.zzj());
                    int i = 1;
                    zzb = true;
                    if (renderer != null) {
                        int ordinal = renderer.ordinal();
                        if (ordinal != 0) {
                            if (ordinal == 1) {
                                i = 2;
                            }
                        }
                        if (zza2.zzd() == 2) {
                            zzc = Renderer.LATEST;
                        }
                        zza2.zzl(ObjectWrapper.wrap(context), i);
                        Log.d(zza, "loadedRenderer: ".concat(String.valueOf(zzc)));
                        if (onMapsSdkInitializedCallback != null) {
                            onMapsSdkInitializedCallback.onMapsSdkInitialized(zzc);
                        }
                        return 0;
                    }
                    i = 0;
                    if (zza2.zzd() == 2) {
                    }
                    zza2.zzl(ObjectWrapper.wrap(context), i);
                    Log.d(zza, "loadedRenderer: ".concat(String.valueOf(zzc)));
                    if (onMapsSdkInitializedCallback != null) {
                    }
                    return 0;
                } catch (RemoteException e) {
                    throw new RuntimeRemoteException(e);
                }
            } catch (GooglePlayServicesNotAvailableException e2) {
                return e2.errorCode;
            }
        }
    }
}
