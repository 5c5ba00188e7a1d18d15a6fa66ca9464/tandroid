package com.google.firebase.components;

import com.google.firebase.inject.Deferred$DeferredHandler;
import com.google.firebase.inject.Provider;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class OptionalProvider<T> implements Provider<T> {
    private volatile Provider<T> delegate;
    private Deferred$DeferredHandler<T> handler;
    private static final Deferred$DeferredHandler<Object> NOOP_HANDLER = new Deferred$DeferredHandler() { // from class: com.google.firebase.components.OptionalProvider$$ExternalSyntheticLambda0
        @Override // com.google.firebase.inject.Deferred$DeferredHandler
        public final void handle(Provider provider) {
            OptionalProvider.lambda$static$0(provider);
        }
    };
    private static final Provider<Object> EMPTY_PROVIDER = new Provider() { // from class: com.google.firebase.components.OptionalProvider$$ExternalSyntheticLambda1
        @Override // com.google.firebase.inject.Provider
        public final Object get() {
            Object lambda$static$1;
            lambda$static$1 = OptionalProvider.lambda$static$1();
            return lambda$static$1;
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$static$0(Provider provider) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Object lambda$static$1() {
        return null;
    }

    private OptionalProvider(Deferred$DeferredHandler<T> deferred$DeferredHandler, Provider<T> provider) {
        this.handler = deferred$DeferredHandler;
        this.delegate = provider;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> OptionalProvider<T> empty() {
        return new OptionalProvider<>(NOOP_HANDLER, EMPTY_PROVIDER);
    }

    @Override // com.google.firebase.inject.Provider
    public T get() {
        return this.delegate.get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void set(Provider<T> provider) {
        Deferred$DeferredHandler<T> deferred$DeferredHandler;
        if (this.delegate != EMPTY_PROVIDER) {
            throw new IllegalStateException("provide() can be called only once.");
        }
        synchronized (this) {
            deferred$DeferredHandler = this.handler;
            this.handler = null;
            this.delegate = provider;
        }
        deferred$DeferredHandler.handle(provider);
    }
}
