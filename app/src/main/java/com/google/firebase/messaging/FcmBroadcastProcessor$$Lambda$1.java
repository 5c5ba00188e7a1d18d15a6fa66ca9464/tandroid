package com.google.firebase.messaging;

import android.content.Context;
import android.content.Intent;
import java.util.concurrent.Callable;
/* compiled from: com.google.firebase:firebase-messaging@@22.0.0 */
/* loaded from: classes.dex */
public final /* synthetic */ class FcmBroadcastProcessor$$Lambda$1 implements Callable {
    private final Context arg$1;
    private final Intent arg$2;

    public FcmBroadcastProcessor$$Lambda$1(Context context, Intent intent) {
        this.arg$1 = context;
        this.arg$2 = intent;
    }

    @Override // java.util.concurrent.Callable
    public Object call() {
        Integer valueOf;
        valueOf = Integer.valueOf(ServiceStarter.getInstance().startMessagingService(this.arg$1, this.arg$2));
        return valueOf;
    }
}
