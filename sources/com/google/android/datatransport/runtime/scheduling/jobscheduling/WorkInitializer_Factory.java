package com.google.android.datatransport.runtime.scheduling.jobscheduling;

import com.google.android.datatransport.runtime.dagger.internal.Factory;
import com.google.android.datatransport.runtime.scheduling.persistence.EventStore;
import com.google.android.datatransport.runtime.synchronization.SynchronizationGuard;
import java.util.concurrent.Executor;
import javax.inject.Provider;

/* loaded from: classes.dex */
public final class WorkInitializer_Factory implements Factory {
    private final Provider executorProvider;
    private final Provider guardProvider;
    private final Provider schedulerProvider;
    private final Provider storeProvider;

    public WorkInitializer_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4) {
        this.executorProvider = provider;
        this.storeProvider = provider2;
        this.schedulerProvider = provider3;
        this.guardProvider = provider4;
    }

    public static WorkInitializer_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4) {
        return new WorkInitializer_Factory(provider, provider2, provider3, provider4);
    }

    public static WorkInitializer newInstance(Executor executor, EventStore eventStore, WorkScheduler workScheduler, SynchronizationGuard synchronizationGuard) {
        return new WorkInitializer(executor, eventStore, workScheduler, synchronizationGuard);
    }

    @Override // javax.inject.Provider
    public WorkInitializer get() {
        return newInstance((Executor) this.executorProvider.get(), (EventStore) this.storeProvider.get(), (WorkScheduler) this.schedulerProvider.get(), (SynchronizationGuard) this.guardProvider.get());
    }
}
