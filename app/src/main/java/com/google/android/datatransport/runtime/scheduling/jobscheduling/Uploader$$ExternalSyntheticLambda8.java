package com.google.android.datatransport.runtime.scheduling.jobscheduling;

import com.google.android.datatransport.runtime.scheduling.persistence.EventStore;
import com.google.android.datatransport.runtime.synchronization.SynchronizationGuard;
/* loaded from: classes.dex */
public final /* synthetic */ class Uploader$$ExternalSyntheticLambda8 implements SynchronizationGuard.CriticalSection {
    public final /* synthetic */ EventStore f$0;

    public /* synthetic */ Uploader$$ExternalSyntheticLambda8(EventStore eventStore) {
        this.f$0 = eventStore;
    }

    @Override // com.google.android.datatransport.runtime.synchronization.SynchronizationGuard.CriticalSection
    public final Object execute() {
        return Integer.valueOf(this.f$0.cleanUp());
    }
}
