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

/* loaded from: classes.dex */
public abstract class MapsInitializer {
    private static final String zza = "MapsInitializer";
    private static boolean zzb = false;
    private static Renderer zzc = Renderer.LEGACY;

    /* loaded from: classes.dex */
    public enum Renderer {
        LEGACY,
        LATEST
    }

    public static synchronized int initialize(Context context) {
        int initialize;
        synchronized (MapsInitializer.class) {
            initialize = initialize(context, null, null);
        }
        return initialize;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(16:11|12|13|14|15|16|(10:18|(1:(1:21))|22|23|(1:25)|26|27|(1:29)|30|31)|36|22|23|(0)|26|27|(0)|30|31) */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0056, code lost:
    
        r5 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0060, code lost:
    
        android.util.Log.e(com.google.android.gms.maps.MapsInitializer.zza, "Failed to retrieve renderer type or log initialization.", r5);
     */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0051 A[Catch: all -> 0x0024, RemoteException -> 0x0056, TryCatch #3 {RemoteException -> 0x0056, blocks: (B:23:0x004b, B:25:0x0051, B:26:0x0058), top: B:22:0x004b, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x007a A[Catch: all -> 0x0024, TRY_LEAVE, TryCatch #1 {, blocks: (B:4:0x0003, B:7:0x001e, B:12:0x0028, B:14:0x002c, B:16:0x003b, B:18:0x0040, B:23:0x004b, B:25:0x0051, B:26:0x0058, B:27:0x0067, B:29:0x007a, B:35:0x0060, B:39:0x0082, B:40:0x0087, B:43:0x0089), top: B:3:0x0003, inners: #0, #2, #3 }] */
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
