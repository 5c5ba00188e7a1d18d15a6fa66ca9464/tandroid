package kotlin.collections;

import java.util.Iterator;
/* compiled from: PrimitiveIterators.kt */
/* loaded from: classes.dex */
public abstract class IntIterator implements Iterator<Integer> {
    public abstract int nextInt();

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Iterator
    public /* bridge */ /* synthetic */ Integer next() {
        return Integer.valueOf(nextInt());
    }
}
