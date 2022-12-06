package com.google.android.gms.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.internal.base.zau;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-base@@18.1.0 */
@SuppressLint({"HandlerLeak"})
/* loaded from: classes.dex */
public final class zac extends zau {
    final /* synthetic */ GoogleApiAvailability zaa;
    private final Context zab;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zac(GoogleApiAvailability googleApiAvailability, Context context) {
        super(Looper.myLooper() == null ? Looper.getMainLooper() : Looper.myLooper());
        this.zaa = googleApiAvailability;
        this.zab = context.getApplicationContext();
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        int i = message.what;
        if (i == 1) {
            int isGooglePlayServicesAvailable = this.zaa.isGooglePlayServicesAvailable(this.zab);
            if (!this.zaa.isUserResolvableError(isGooglePlayServicesAvailable)) {
                return;
            }
            this.zaa.showErrorNotification(this.zab, isGooglePlayServicesAvailable);
            return;
        }
        Log.w("GoogleApiAvailability", "Don't know how to handle this message: " + i);
    }
}
