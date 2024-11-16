package com.google.common.collect;

import java.util.Iterator;

/* loaded from: classes.dex */
public abstract class UnmodifiableIterator implements Iterator {
    protected UnmodifiableIterator() {
    }

    @Override // java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
