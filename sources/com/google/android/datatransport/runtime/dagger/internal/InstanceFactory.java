package com.google.android.datatransport.runtime.dagger.internal;
/* loaded from: classes.dex */
public final class InstanceFactory<T> implements Factory<T> {
    private final T instance;

    public static <T> Factory<T> create(T t) {
        return new InstanceFactory(Preconditions.checkNotNull(t, "instance cannot be null"));
    }

    static {
        new InstanceFactory(null);
    }

    private InstanceFactory(T t) {
        this.instance = t;
    }

    @Override // javax.inject.Provider
    public T get() {
        return this.instance;
    }
}
