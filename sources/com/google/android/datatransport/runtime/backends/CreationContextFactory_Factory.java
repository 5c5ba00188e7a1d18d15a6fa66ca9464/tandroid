package com.google.android.datatransport.runtime.backends;

import android.content.Context;
import com.google.android.datatransport.runtime.dagger.internal.Factory;
import com.google.android.datatransport.runtime.time.Clock;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class CreationContextFactory_Factory implements Factory {
    private final Provider applicationContextProvider;
    private final Provider monotonicClockProvider;
    private final Provider wallClockProvider;

    public CreationContextFactory_Factory(Provider provider, Provider provider2, Provider provider3) {
        this.applicationContextProvider = provider;
        this.wallClockProvider = provider2;
        this.monotonicClockProvider = provider3;
    }

    public static CreationContextFactory_Factory create(Provider provider, Provider provider2, Provider provider3) {
        return new CreationContextFactory_Factory(provider, provider2, provider3);
    }

    public static CreationContextFactory newInstance(Context context, Clock clock, Clock clock2) {
        return new CreationContextFactory(context, clock, clock2);
    }

    @Override // javax.inject.Provider
    public CreationContextFactory get() {
        return newInstance((Context) this.applicationContextProvider.get(), (Clock) this.wallClockProvider.get(), (Clock) this.monotonicClockProvider.get());
    }
}
