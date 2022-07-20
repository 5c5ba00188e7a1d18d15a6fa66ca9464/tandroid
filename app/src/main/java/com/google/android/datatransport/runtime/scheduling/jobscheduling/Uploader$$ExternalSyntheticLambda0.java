package com.google.android.datatransport.runtime.scheduling.jobscheduling;

import com.google.android.datatransport.runtime.TransportContext;
import com.google.android.datatransport.runtime.synchronization.SynchronizationGuard;
/* loaded from: classes.dex */
public final /* synthetic */ class Uploader$$ExternalSyntheticLambda0 implements SynchronizationGuard.CriticalSection {
    public final /* synthetic */ Uploader f$0;
    public final /* synthetic */ TransportContext f$1;

    public /* synthetic */ Uploader$$ExternalSyntheticLambda0(Uploader uploader, TransportContext transportContext) {
        this.f$0 = uploader;
        this.f$1 = transportContext;
    }

    @Override // com.google.android.datatransport.runtime.synchronization.SynchronizationGuard.CriticalSection
    public final Object execute() {
        Boolean lambda$logAndUpdateState$2;
        lambda$logAndUpdateState$2 = this.f$0.lambda$logAndUpdateState$2(this.f$1);
        return lambda$logAndUpdateState$2;
    }
}
