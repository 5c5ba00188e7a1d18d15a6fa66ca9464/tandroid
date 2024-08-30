package com.google.firebase.components;

import com.google.firebase.inject.Provider;
import j$.util.concurrent.ConcurrentHashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class LazySet implements Provider {
    private volatile Set actualSet = null;
    private volatile Set providers = Collections.newSetFromMap(new ConcurrentHashMap());

    LazySet(Collection collection) {
        this.providers.addAll(collection);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static LazySet fromCollection(Collection collection) {
        return new LazySet((Set) collection);
    }

    private synchronized void updateSet() {
        try {
            for (Provider provider : this.providers) {
                this.actualSet.add(provider.get());
            }
            this.providers = null;
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void add(Provider provider) {
        Set set;
        Object obj;
        try {
            if (this.actualSet == null) {
                set = this.providers;
                obj = provider;
            } else {
                set = this.actualSet;
                obj = provider.get();
            }
            set.add(obj);
        } catch (Throwable th) {
            throw th;
        }
    }

    @Override // com.google.firebase.inject.Provider
    public Set get() {
        if (this.actualSet == null) {
            synchronized (this) {
                try {
                    if (this.actualSet == null) {
                        this.actualSet = Collections.newSetFromMap(new ConcurrentHashMap());
                        updateSet();
                    }
                } finally {
                }
            }
        }
        return Collections.unmodifiableSet(this.actualSet);
    }
}
