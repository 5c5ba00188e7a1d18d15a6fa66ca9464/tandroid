package com.google.android.datatransport.runtime.scheduling.jobscheduling;

import android.content.Context;
import com.google.android.datatransport.runtime.backends.BackendRegistry;
import com.google.android.datatransport.runtime.dagger.internal.Factory;
import com.google.android.datatransport.runtime.scheduling.persistence.ClientHealthMetricsStore;
import com.google.android.datatransport.runtime.scheduling.persistence.EventStore;
import com.google.android.datatransport.runtime.synchronization.SynchronizationGuard;
import com.google.android.datatransport.runtime.time.Clock;
import java.util.concurrent.Executor;
import javax.inject.Provider;

/* loaded from: classes.dex */
public final class Uploader_Factory implements Factory {
    private final Provider backendRegistryProvider;
    private final Provider clientHealthMetricsStoreProvider;
    private final Provider clockProvider;
    private final Provider contextProvider;
    private final Provider eventStoreProvider;
    private final Provider executorProvider;
    private final Provider guardProvider;
    private final Provider uptimeClockProvider;
    private final Provider workSchedulerProvider;

    public Uploader_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9) {
        this.contextProvider = provider;
        this.backendRegistryProvider = provider2;
        this.eventStoreProvider = provider3;
        this.workSchedulerProvider = provider4;
        this.executorProvider = provider5;
        this.guardProvider = provider6;
        this.clockProvider = provider7;
        this.uptimeClockProvider = provider8;
        this.clientHealthMetricsStoreProvider = provider9;
    }

    public static Uploader_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9) {
        return new Uploader_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static Uploader newInstance(Context context, BackendRegistry backendRegistry, EventStore eventStore, WorkScheduler workScheduler, Executor executor, SynchronizationGuard synchronizationGuard, Clock clock, Clock clock2, ClientHealthMetricsStore clientHealthMetricsStore) {
        return new Uploader(context, backendRegistry, eventStore, workScheduler, executor, synchronizationGuard, clock, clock2, clientHealthMetricsStore);
    }

    @Override // javax.inject.Provider
    public Uploader get() {
        return newInstance((Context) this.contextProvider.get(), (BackendRegistry) this.backendRegistryProvider.get(), (EventStore) this.eventStoreProvider.get(), (WorkScheduler) this.workSchedulerProvider.get(), (Executor) this.executorProvider.get(), (SynchronizationGuard) this.guardProvider.get(), (Clock) this.clockProvider.get(), (Clock) this.uptimeClockProvider.get(), (ClientHealthMetricsStore) this.clientHealthMetricsStoreProvider.get());
    }
}
