package com.google.android.gms.internal.location;

import com.google.android.gms.common.api.internal.RemoteCall;
import com.google.android.gms.location.LastLocationRequest;
import com.google.android.gms.tasks.TaskCompletionSource;
/* compiled from: com.google.android.gms:play-services-location@@21.0.1 */
/* loaded from: classes.dex */
public final /* synthetic */ class zzbe implements RemoteCall {
    public static final /* synthetic */ zzbe zza = new zzbe();

    private /* synthetic */ zzbe() {
    }

    @Override // com.google.android.gms.common.api.internal.RemoteCall
    public final void accept(Object obj, Object obj2) {
        ((zzda) obj).zzt(new LastLocationRequest.Builder().build(), (TaskCompletionSource) obj2);
    }
}
