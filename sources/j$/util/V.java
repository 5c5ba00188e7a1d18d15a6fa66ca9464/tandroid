package j$.util;

import j$.util.function.Consumer;
import java.util.NoSuchElementException;

/* loaded from: classes2.dex */
final class V implements r, j$.util.function.n, j {
    boolean a = false;
    double b;
    final /* synthetic */ E c;

    V(E e) {
        this.c = e;
    }

    @Override // j$.util.r, j$.util.j
    public final void a(Consumer consumer) {
        if (consumer instanceof j$.util.function.n) {
            forEachRemaining((j$.util.function.n) consumer);
            return;
        }
        consumer.getClass();
        if (h0.a) {
            h0.a(V.class, "{0} calling PrimitiveIterator.OfDouble.forEachRemainingDouble(action::accept)");
            throw null;
        }
        forEachRemaining(new o(consumer));
    }

    @Override // j$.util.function.n
    public final void accept(double d) {
        this.a = true;
        this.b = d;
    }

    @Override // j$.util.A
    /* renamed from: e, reason: merged with bridge method [inline-methods] */
    public final void forEachRemaining(j$.util.function.n nVar) {
        nVar.getClass();
        while (hasNext()) {
            nVar.accept(nextDouble());
        }
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        if (!this.a) {
            this.c.p(this);
        }
        return this.a;
    }

    @Override // j$.util.function.n
    public final /* synthetic */ j$.util.function.n k(j$.util.function.n nVar) {
        return j$.com.android.tools.r8.a.b(this, nVar);
    }

    @Override // java.util.Iterator
    public final Double next() {
        if (!h0.a) {
            return Double.valueOf(nextDouble());
        }
        h0.a(V.class, "{0} calling PrimitiveIterator.OfDouble.nextLong()");
        throw null;
    }

    @Override // j$.util.r
    public final double nextDouble() {
        if (!this.a && !hasNext()) {
            throw new NoSuchElementException();
        }
        this.a = false;
        return this.b;
    }
}
