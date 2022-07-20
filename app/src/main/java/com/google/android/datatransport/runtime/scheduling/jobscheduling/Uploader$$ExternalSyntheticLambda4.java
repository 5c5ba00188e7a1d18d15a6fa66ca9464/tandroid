package com.google.android.datatransport.runtime.scheduling.jobscheduling;

import com.google.android.datatransport.runtime.synchronization.SynchronizationGuard;
/* loaded from: classes.dex */
public final /* synthetic */ class Uploader$$ExternalSyntheticLambda4 implements SynchronizationGuard.CriticalSection {
    public final /* synthetic */ Uploader f$0;
    public final /* synthetic */ Iterable f$1;

    public /* synthetic */ Uploader$$ExternalSyntheticLambda4(Uploader uploader, Iterable iterable) {
        this.f$0 = uploader;
        this.f$1 = iterable;
    }

    @Override // com.google.android.datatransport.runtime.synchronization.SynchronizationGuard.CriticalSection
    public final Object execute() {
        Object lambda$logAndUpdateState$5;
        lambda$logAndUpdateState$5 = this.f$0.lambda$logAndUpdateState$5(this.f$1);
        return lambda$logAndUpdateState$5;
    }
}
