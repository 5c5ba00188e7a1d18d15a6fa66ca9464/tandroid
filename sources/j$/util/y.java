package j$.util;

import j$.util.Iterator;
import j$.util.function.Consumer;
import j$.util.t;
import java.util.NoSuchElementException;
import java.util.Objects;
/* loaded from: classes2.dex */
class y implements n, j$.util.function.f, Iterator {
    boolean a = false;
    double b;
    final /* synthetic */ t.a c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public y(t.a aVar) {
        this.c = aVar;
    }

    @Override // j$.util.function.f
    public void accept(double d) {
        this.a = true;
        this.b = d;
    }

    @Override // j$.util.p
    /* renamed from: e */
    public void forEachRemaining(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
        while (hasNext()) {
            fVar.accept(nextDouble());
        }
    }

    @Override // j$.util.n, j$.util.Iterator
    public void forEachRemaining(Consumer consumer) {
        if (consumer instanceof j$.util.function.f) {
            forEachRemaining((j$.util.function.f) consumer);
            return;
        }
        Objects.requireNonNull(consumer);
        if (!L.a) {
            while (hasNext()) {
                consumer.accept(Double.valueOf(nextDouble()));
            }
            return;
        }
        L.a(y.class, "{0} calling PrimitiveIterator.OfDouble.forEachRemainingDouble(action::accept)");
        throw null;
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public boolean hasNext() {
        if (!this.a) {
            this.c.k(this);
        }
        return this.a;
    }

    @Override // j$.util.function.f
    public j$.util.function.f j(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
        return new j$.util.function.e(this, fVar);
    }

    @Override // j$.util.n, java.util.Iterator
    public Double next() {
        if (L.a) {
            L.a(y.class, "{0} calling PrimitiveIterator.OfDouble.nextLong()");
            throw null;
        }
        return Double.valueOf(nextDouble());
    }

    @Override // j$.util.n
    public double nextDouble() {
        if (this.a || hasNext()) {
            this.a = false;
            return this.b;
        }
        throw new NoSuchElementException();
    }

    @Override // java.util.Iterator, j$.util.Iterator
    public /* synthetic */ void remove() {
        Iterator.-CC.a(this);
        throw null;
    }
}
