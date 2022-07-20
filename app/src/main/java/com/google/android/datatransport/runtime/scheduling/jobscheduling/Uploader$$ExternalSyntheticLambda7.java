package com.google.android.datatransport.runtime.scheduling.jobscheduling;

import com.google.android.datatransport.runtime.scheduling.persistence.ClientHealthMetricsStore;
import com.google.android.datatransport.runtime.synchronization.SynchronizationGuard;
/* loaded from: classes.dex */
public final /* synthetic */ class Uploader$$ExternalSyntheticLambda7 implements SynchronizationGuard.CriticalSection {
    public final /* synthetic */ ClientHealthMetricsStore f$0;

    public /* synthetic */ Uploader$$ExternalSyntheticLambda7(ClientHealthMetricsStore clientHealthMetricsStore) {
        this.f$0 = clientHealthMetricsStore;
    }

    @Override // com.google.android.datatransport.runtime.synchronization.SynchronizationGuard.CriticalSection
    public final Object execute() {
        return this.f$0.loadClientMetrics();
    }
}
