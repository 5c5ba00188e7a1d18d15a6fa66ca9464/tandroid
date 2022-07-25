package com.google.android.datatransport.runtime.backends;

import android.content.Context;
import com.google.android.datatransport.runtime.dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MetadataBackendRegistry_Factory implements Factory<MetadataBackendRegistry> {
    private final Provider<Context> applicationContextProvider;
    private final Provider<CreationContextFactory> creationContextFactoryProvider;

    public MetadataBackendRegistry_Factory(Provider<Context> provider, Provider<CreationContextFactory> provider2) {
        this.applicationContextProvider = provider;
        this.creationContextFactoryProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public MetadataBackendRegistry mo70get() {
        return newInstance(this.applicationContextProvider.mo70get(), this.creationContextFactoryProvider.mo70get());
    }

    public static MetadataBackendRegistry_Factory create(Provider<Context> provider, Provider<CreationContextFactory> provider2) {
        return new MetadataBackendRegistry_Factory(provider, provider2);
    }

    public static MetadataBackendRegistry newInstance(Context context, Object obj) {
        return new MetadataBackendRegistry(context, (CreationContextFactory) obj);
    }
}
