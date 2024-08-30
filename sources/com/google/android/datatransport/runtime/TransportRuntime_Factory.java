package com.google.android.datatransport.runtime;

import com.google.android.datatransport.runtime.dagger.internal.Factory;
import com.google.android.datatransport.runtime.scheduling.Scheduler;
import com.google.android.datatransport.runtime.scheduling.jobscheduling.Uploader;
import com.google.android.datatransport.runtime.scheduling.jobscheduling.WorkInitializer;
import com.google.android.datatransport.runtime.time.Clock;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TransportRuntime_Factory implements Factory {
    private final Provider eventClockProvider;
    private final Provider initializerProvider;
    private final Provider schedulerProvider;
    private final Provider uploaderProvider;
    private final Provider uptimeClockProvider;

    public TransportRuntime_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5) {
        this.eventClockProvider = provider;
        this.uptimeClockProvider = provider2;
        this.schedulerProvider = provider3;
        this.uploaderProvider = provider4;
        this.initializerProvider = provider5;
    }

    public static TransportRuntime_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5) {
        return new TransportRuntime_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static TransportRuntime newInstance(Clock clock, Clock clock2, Scheduler scheduler, Uploader uploader, WorkInitializer workInitializer) {
        return new TransportRuntime(clock, clock2, scheduler, uploader, workInitializer);
    }

    @Override // javax.inject.Provider
    public TransportRuntime get() {
        return newInstance((Clock) this.eventClockProvider.get(), (Clock) this.uptimeClockProvider.get(), (Scheduler) this.schedulerProvider.get(), (Uploader) this.uploaderProvider.get(), (WorkInitializer) this.initializerProvider.get());
    }
}
