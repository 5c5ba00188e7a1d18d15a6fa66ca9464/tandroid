package com.google.android.datatransport.runtime.scheduling.jobscheduling;

import com.google.android.datatransport.runtime.synchronization.SynchronizationGuard;
/* loaded from: classes.dex */
public final /* synthetic */ class WorkInitializer$$ExternalSyntheticLambda0 implements SynchronizationGuard.CriticalSection {
    public final /* synthetic */ WorkInitializer f$0;

    public /* synthetic */ WorkInitializer$$ExternalSyntheticLambda0(WorkInitializer workInitializer) {
        this.f$0 = workInitializer;
    }

    @Override // com.google.android.datatransport.runtime.synchronization.SynchronizationGuard.CriticalSection
    public final Object execute() {
        Object lambda$ensureContextsScheduled$0;
        lambda$ensureContextsScheduled$0 = this.f$0.lambda$ensureContextsScheduled$0();
        return lambda$ensureContextsScheduled$0;
    }
}
