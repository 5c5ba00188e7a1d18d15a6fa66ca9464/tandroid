package com.google.android.datatransport.runtime.backends;

import android.content.Context;
import com.google.android.datatransport.runtime.dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MetadataBackendRegistry_Factory implements Factory {
    private final Provider applicationContextProvider;
    private final Provider creationContextFactoryProvider;

    public MetadataBackendRegistry_Factory(Provider provider, Provider provider2) {
        this.applicationContextProvider = provider;
        this.creationContextFactoryProvider = provider2;
    }

    public static MetadataBackendRegistry_Factory create(Provider provider, Provider provider2) {
        return new MetadataBackendRegistry_Factory(provider, provider2);
    }

    public static MetadataBackendRegistry newInstance(Context context, Object obj) {
        return new MetadataBackendRegistry(context, (CreationContextFactory) obj);
    }

    @Override // javax.inject.Provider
    public MetadataBackendRegistry get() {
        return newInstance((Context) this.applicationContextProvider.get(), this.creationContextFactoryProvider.get());
    }
}
