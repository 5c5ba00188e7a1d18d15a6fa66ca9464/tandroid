package j$.util;

import j$.util.function.Consumer;
import java.util.NoSuchElementException;
/* loaded from: classes2.dex */
final class T implements v, j$.util.function.K, Iterator {
    boolean a = false;
    int b;
    final /* synthetic */ H c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public T(H h) {
        this.c = h;
    }

    @Override // j$.util.function.K
    public final void accept(int i) {
        this.a = true;
        this.b = i;
    }

    @Override // j$.util.A
    /* renamed from: c */
    public final void forEachRemaining(j$.util.function.K k) {
        k.getClass();
        while (hasNext()) {
            k.accept(nextInt());
        }
    }

    @Override // j$.util.v, j$.util.Iterator
    public final void forEachRemaining(Consumer consumer) {
        if (consumer instanceof j$.util.function.K) {
            forEachRemaining((j$.util.function.K) consumer);
            return;
        }
        consumer.getClass();
        if (h0.a) {
            h0.a(T.class, "{0} calling PrimitiveIterator.OfInt.forEachRemainingInt(action::accept)");
            throw null;
        } else {
            forEachRemaining(new s(consumer));
        }
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public final boolean hasNext() {
        if (!this.a) {
            this.c.j(this);
        }
        return this.a;
    }

    @Override // j$.util.function.K
    public final j$.util.function.K n(j$.util.function.K k) {
        k.getClass();
        return new j$.util.function.H(this, k);
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public final Integer next() {
        if (h0.a) {
            h0.a(T.class, "{0} calling PrimitiveIterator.OfInt.nextInt()");
            throw null;
        }
        return Integer.valueOf(nextInt());
    }

    @Override // j$.util.v
    public final int nextInt() {
        if (this.a || hasNext()) {
            this.a = false;
            return this.b;
        }
        throw new NoSuchElementException();
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException("remove");
    }
}
