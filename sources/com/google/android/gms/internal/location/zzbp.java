package com.google.android.gms.internal.location;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.common.api.internal.ListenerHolders;
import com.google.android.gms.common.api.internal.RegistrationMethods;
import com.google.android.gms.common.api.internal.RemoteCall;
import com.google.android.gms.common.api.internal.TaskApiCall;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LastLocationRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public final class zzbp extends GoogleApi implements FusedLocationProviderClient {
    static final Api.ClientKey zza;
    public static final Api zzb;

    static {
        Api.ClientKey clientKey = new Api.ClientKey();
        zza = clientKey;
        zzb = new Api("LocationServices.API", new zzbm(), clientKey);
    }

    public zzbp(Context context) {
        super(context, zzb, Api.ApiOptions.NO_OPTIONS, GoogleApi.Settings.DEFAULT_SETTINGS);
    }

    private final Task zza(final LocationRequest locationRequest, ListenerHolder listenerHolder) {
        final zzbo zzboVar = new zzbo(this, listenerHolder, new zzbn() { // from class: com.google.android.gms.internal.location.zzax
            @Override // com.google.android.gms.internal.location.zzbn
            public final void zza(zzda zzdaVar, ListenerHolder.ListenerKey listenerKey, boolean z, TaskCompletionSource taskCompletionSource) {
                zzdaVar.zzB(listenerKey, z, taskCompletionSource);
            }
        });
        return doRegisterEventListener(RegistrationMethods.builder().register(new RemoteCall() { // from class: com.google.android.gms.internal.location.zzay
            @Override // com.google.android.gms.common.api.internal.RemoteCall
            public final void accept(Object obj, Object obj2) {
                Api api = zzbp.zzb;
                ((zzda) obj).zzu(zzbo.this, locationRequest, (TaskCompletionSource) obj2);
            }
        }).unregister(zzboVar).withHolder(listenerHolder).setMethodKey(2436).build());
    }

    @Override // com.google.android.gms.location.FusedLocationProviderClient
    public final Task getLastLocation() {
        return doRead(TaskApiCall.builder().run(new RemoteCall() { // from class: com.google.android.gms.internal.location.zzbe
            @Override // com.google.android.gms.common.api.internal.RemoteCall
            public final void accept(Object obj, Object obj2) {
                ((zzda) obj).zzt(new LastLocationRequest.Builder().build(), (TaskCompletionSource) obj2);
            }
        }).setMethodKey(2414).build());
    }

    @Override // com.google.android.gms.location.FusedLocationProviderClient
    public final Task removeLocationUpdates(LocationCallback locationCallback) {
        return doUnregisterEventListener(ListenerHolders.createListenerKey(locationCallback, LocationCallback.class.getSimpleName()), 2418).continueWith(new Executor() { // from class: com.google.android.gms.internal.location.zzbk
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                runnable.run();
            }
        }, new Continuation() { // from class: com.google.android.gms.internal.location.zzbc
            @Override // com.google.android.gms.tasks.Continuation
            public final Object then(Task task) {
                Api api = zzbp.zzb;
                return null;
            }
        });
    }

    @Override // com.google.android.gms.location.FusedLocationProviderClient
    public final Task requestLocationUpdates(LocationRequest locationRequest, LocationCallback locationCallback, Looper looper) {
        if (looper == null) {
            looper = Looper.myLooper();
            Preconditions.checkNotNull(looper, "invalid null looper");
        }
        return zza(locationRequest, ListenerHolders.createListenerHolder(locationCallback, looper, LocationCallback.class.getSimpleName()));
    }
}
