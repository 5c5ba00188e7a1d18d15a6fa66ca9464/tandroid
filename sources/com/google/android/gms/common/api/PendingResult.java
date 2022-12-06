package com.google.android.gms.common.api;

import com.google.android.gms.common.api.Result;
import java.util.concurrent.TimeUnit;
/* compiled from: com.google.android.gms:play-services-base@@18.1.0 */
/* loaded from: classes.dex */
public abstract class PendingResult<R extends Result> {

    /* compiled from: com.google.android.gms:play-services-base@@18.1.0 */
    /* loaded from: classes.dex */
    public interface StatusListener {
        void onComplete(Status status);
    }

    public abstract void addStatusListener(StatusListener statusListener);

    public abstract R await(long j, TimeUnit timeUnit);
}
