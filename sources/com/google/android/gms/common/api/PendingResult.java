package com.google.android.gms.common.api;

import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public abstract class PendingResult {

    /* loaded from: classes.dex */
    public interface StatusListener {
        void onComplete(Status status);
    }

    public abstract void addStatusListener(StatusListener statusListener);

    public abstract Result await(long j, TimeUnit timeUnit);
}
