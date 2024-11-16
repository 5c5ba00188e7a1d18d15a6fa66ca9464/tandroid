package com.google.firebase.components;

import com.google.firebase.inject.Provider;
import j$.util.concurrent.ConcurrentHashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

/* loaded from: classes.dex */
class LazySet implements Provider {
    private volatile Set actualSet = null;
    private volatile Set providers = Collections.newSetFromMap(new ConcurrentHashMap());

    LazySet(Collection collection) {
        this.providers.addAll(collection);
    }

    static LazySet fromCollection(Collection collection) {
        return new LazySet((Set) collection);
    }

    private synchronized void updateSet() {
        try {
            Iterator it = this.providers.iterator();
            while (it.hasNext()) {
                this.actualSet.add(((Provider) it.next()).get());
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
