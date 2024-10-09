package com.google.android.datatransport.runtime.dagger.internal;

import javax.inject.Provider;

/* loaded from: classes.dex */
public final class DoubleCheck implements Provider {
    private static final Object UNINITIALIZED = new Object();
    private volatile Object instance = UNINITIALIZED;
    private volatile Provider provider;

    private DoubleCheck(Provider provider) {
        this.provider = provider;
    }

    public static Provider provider(Provider provider) {
        Preconditions.checkNotNull(provider);
        return provider instanceof DoubleCheck ? provider : new DoubleCheck(provider);
    }

    public static Object reentrantCheck(Object obj, Object obj2) {
        if (obj == UNINITIALIZED || obj == obj2) {
            return obj2;
        }
        throw new IllegalStateException("Scoped provider was invoked recursively returning different results: " + obj + " & " + obj2 + ". This is likely due to a circular dependency.");
    }

    @Override // javax.inject.Provider
    public Object get() {
        Object obj = this.instance;
        Object obj2 = UNINITIALIZED;
        if (obj == obj2) {
            synchronized (this) {
                try {
                    obj = this.instance;
                    if (obj == obj2) {
                        obj = this.provider.get();
                        this.instance = reentrantCheck(this.instance, obj);
                        this.provider = null;
                    }
                } finally {
                }
            }
        }
        return obj;
    }
}
