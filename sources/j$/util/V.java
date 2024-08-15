package j$.util;

import j$.util.function.Consumer;
import java.util.NoSuchElementException;
/* loaded from: classes2.dex */
final class V implements r, j$.util.function.m, Iterator {
    boolean a = false;
    double b;
    final /* synthetic */ E c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public V(E e) {
        this.c = e;
    }

    @Override // j$.util.function.m
    public final void accept(double d) {
        this.a = true;
        this.b = d;
    }

    @Override // j$.util.A
    /* renamed from: d */
    public final void forEachRemaining(j$.util.function.m mVar) {
        mVar.getClass();
        while (hasNext()) {
            mVar.accept(nextDouble());
        }
    }

    @Override // j$.util.r, j$.util.Iterator
    public final void forEachRemaining(Consumer consumer) {
        if (consumer instanceof j$.util.function.m) {
            forEachRemaining((j$.util.function.m) consumer);
            return;
        }
        consumer.getClass();
        if (h0.a) {
            h0.a(V.class, "{0} calling PrimitiveIterator.OfDouble.forEachRemainingDouble(action::accept)");
            throw null;
        } else {
            forEachRemaining(new o(consumer));
        }
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public final boolean hasNext() {
        if (!this.a) {
            this.c.o(this);
        }
        return this.a;
    }

    @Override // j$.util.function.m
    public final j$.util.function.m m(j$.util.function.m mVar) {
        mVar.getClass();
        return new j$.util.function.j(this, mVar);
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public final Double next() {
        if (h0.a) {
            h0.a(V.class, "{0} calling PrimitiveIterator.OfDouble.nextLong()");
            throw null;
        }
        return Double.valueOf(nextDouble());
    }

    @Override // j$.util.r
    public final double nextDouble() {
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
