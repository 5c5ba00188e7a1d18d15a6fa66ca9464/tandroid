package com.google.common.collect;

import java.util.ListIterator;
/* loaded from: classes.dex */
abstract class TransformedListIterator extends TransformedIterator implements ListIterator {
    /* JADX INFO: Access modifiers changed from: package-private */
    public TransformedListIterator(ListIterator listIterator) {
        super(listIterator);
    }

    private ListIterator backingIterator() {
        return Iterators.cast(this.backingIterator);
    }

    @Override // java.util.ListIterator
    public void add(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.ListIterator
    public final boolean hasPrevious() {
        return backingIterator().hasPrevious();
    }

    @Override // java.util.ListIterator
    public final int nextIndex() {
        return backingIterator().nextIndex();
    }

    @Override // java.util.ListIterator
    public final Object previous() {
        return transform(backingIterator().previous());
    }

    @Override // java.util.ListIterator
    public final int previousIndex() {
        return backingIterator().previousIndex();
    }

    @Override // java.util.ListIterator
    public void set(Object obj) {
        throw new UnsupportedOperationException();
    }
}
