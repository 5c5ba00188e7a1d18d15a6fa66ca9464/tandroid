package com.google.firebase.appindexing.internal;

import android.content.Context;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.internal.icing.zze;
import com.google.firebase.FirebaseExceptionMapper;

/* loaded from: classes.dex */
final class zzr extends GoogleApi {
    zzr(Context context) {
        super(context, zze.zzb, Api.ApiOptions.NO_OPTIONS, new FirebaseExceptionMapper());
    }
}
