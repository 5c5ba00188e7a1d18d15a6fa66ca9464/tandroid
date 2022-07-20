package com.google.firebase.remoteconfig.internal;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
/* loaded from: classes.dex */
public final /* synthetic */ class ConfigFetchHandler$$ExternalSyntheticLambda0 implements Continuation {
    public final /* synthetic */ ConfigFetchHandler f$0;
    public final /* synthetic */ long f$1;

    public /* synthetic */ ConfigFetchHandler$$ExternalSyntheticLambda0(ConfigFetchHandler configFetchHandler, long j) {
        this.f$0 = configFetchHandler;
        this.f$1 = j;
    }

    @Override // com.google.android.gms.tasks.Continuation
    public final Object then(Task task) {
        Task lambda$fetch$0;
        lambda$fetch$0 = this.f$0.lambda$fetch$0(this.f$1, task);
        return lambda$fetch$0;
    }
}
