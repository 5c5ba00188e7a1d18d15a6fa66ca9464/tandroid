package kotlin.collections;

import java.util.Iterator;

/* loaded from: classes.dex */
public abstract class CharIterator implements Iterator {
    @Override // java.util.Iterator
    public /* bridge */ /* synthetic */ Object next() {
        return Character.valueOf(nextChar());
    }

    public abstract char nextChar();

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
}
