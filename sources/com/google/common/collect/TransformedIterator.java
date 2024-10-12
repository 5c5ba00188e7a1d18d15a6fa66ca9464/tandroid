package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Iterator;

/* loaded from: classes.dex */
abstract class TransformedIterator implements Iterator {
    final Iterator backingIterator;

    /* JADX INFO: Access modifiers changed from: package-private */
    public TransformedIterator(Iterator it) {
        this.backingIterator = (Iterator) Preconditions.checkNotNull(it);
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.backingIterator.hasNext();
    }

    @Override // java.util.Iterator
    public final Object next() {
        return transform(this.backingIterator.next());
    }

    @Override // java.util.Iterator
    public final void remove() {
        this.backingIterator.remove();
    }

    abstract Object transform(Object obj);
}
