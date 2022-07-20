package com.google.firebase.remoteconfig.internal;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import java.util.Date;
/* loaded from: classes.dex */
public final /* synthetic */ class ConfigFetchHandler$$ExternalSyntheticLambda2 implements Continuation {
    public final /* synthetic */ ConfigFetchHandler f$0;
    public final /* synthetic */ Date f$1;

    public /* synthetic */ ConfigFetchHandler$$ExternalSyntheticLambda2(ConfigFetchHandler configFetchHandler, Date date) {
        this.f$0 = configFetchHandler;
        this.f$1 = date;
    }

    @Override // com.google.android.gms.tasks.Continuation
    public final Object then(Task task) {
        Task lambda$fetchIfCacheExpiredAndNotThrottled$2;
        lambda$fetchIfCacheExpiredAndNotThrottled$2 = this.f$0.lambda$fetchIfCacheExpiredAndNotThrottled$2(this.f$1, task);
        return lambda$fetchIfCacheExpiredAndNotThrottled$2;
    }
}
