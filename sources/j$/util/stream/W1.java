package j$.util.stream;

import j$.util.Optional;
import j$.util.function.BiConsumer;
import j$.util.function.BiFunction;
import j$.util.function.Consumer;
import j$.util.function.Function;
import j$.util.function.Predicate;
import j$.util.function.Supplier;
import j$.util.function.ToDoubleFunction;
import j$.util.function.ToIntFunction;
import j$.util.function.ToLongFunction;
import java.util.Comparator;
import java.util.Iterator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class W1 extends b implements Stream {
    /* JADX INFO: Access modifiers changed from: package-private */
    public W1(j$.util.Q q, int i, boolean z) {
        super(q, i, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public W1(b bVar, int i) {
        super(bVar, i);
    }

    @Override // j$.util.stream.b
    final j$.util.Q C0(b bVar, Supplier supplier, boolean z) {
        return new v3(bVar, supplier, z);
    }

    @Override // j$.util.stream.Stream
    public final Stream O(Consumer consumer) {
        consumer.getClass();
        return new u(this, consumer);
    }

    @Override // j$.util.stream.Stream
    public final boolean P(Predicate predicate) {
        return ((Boolean) l0(t0.b0(predicate, q0.ALL))).booleanValue();
    }

    @Override // j$.util.stream.Stream
    public final LongStream R(Function function) {
        function.getClass();
        return new w(this, S2.p | S2.n | S2.t, function, 6);
    }

    @Override // j$.util.stream.Stream
    public final boolean Y(Predicate predicate) {
        return ((Boolean) l0(t0.b0(predicate, q0.NONE))).booleanValue();
    }

    @Override // j$.util.stream.Stream
    public final boolean a(Predicate predicate) {
        return ((Boolean) l0(t0.b0(predicate, q0.ANY))).booleanValue();
    }

    @Override // j$.util.stream.Stream
    public final IntStream c(Function function) {
        function.getClass();
        return new v(this, S2.p | S2.n | S2.t, function, 7);
    }

    @Override // j$.util.stream.Stream
    public final D c0(ToDoubleFunction toDoubleFunction) {
        toDoubleFunction.getClass();
        return new t(this, S2.p | S2.n, toDoubleFunction, 6);
    }

    @Override // j$.util.stream.Stream
    public final Object collect(Collector collector) {
        Object l0;
        if (isParallel() && collector.characteristics().contains(h.CONCURRENT) && (!t0() || collector.characteristics().contains(h.UNORDERED))) {
            l0 = collector.supplier().get();
            forEach(new k0(6, collector.accumulator(), l0));
        } else {
            collector.getClass();
            Supplier supplier = collector.supplier();
            l0 = l0(new D1(T2.REFERENCE, collector.combiner(), collector.accumulator(), supplier, collector));
        }
        return collector.characteristics().contains(h.IDENTITY_FINISH) ? l0 : collector.finisher().apply(l0);
    }

    @Override // j$.util.stream.Stream
    public final long count() {
        return ((h0) mapToLong(new Q1(1))).sum();
    }

    @Override // j$.util.stream.Stream
    public final Stream distinct() {
        return new p(this, S2.m | S2.t);
    }

    public void f(Consumer consumer) {
        consumer.getClass();
        l0(new P(consumer, true));
    }

    @Override // j$.util.stream.Stream
    public final Stream filter(Predicate predicate) {
        predicate.getClass();
        return new u(this, S2.t, predicate, 4);
    }

    @Override // j$.util.stream.Stream
    public final Optional findAny() {
        return (Optional) l0(new F(false, T2.REFERENCE, Optional.empty(), new E(0), new l(5)));
    }

    @Override // j$.util.stream.Stream
    public final Optional findFirst() {
        return (Optional) l0(new F(true, T2.REFERENCE, Optional.empty(), new E(0), new l(5)));
    }

    public void forEach(Consumer consumer) {
        consumer.getClass();
        l0(new P(consumer, false));
    }

    @Override // j$.util.stream.Stream
    public final Object h0(Object obj, j$.util.function.f fVar) {
        fVar.getClass();
        return l0(new u1(T2.REFERENCE, fVar, fVar, obj, 2));
    }

    @Override // j$.util.stream.Stream
    public final Object i(Supplier supplier, BiConsumer biConsumer, BiConsumer biConsumer2) {
        supplier.getClass();
        biConsumer.getClass();
        biConsumer2.getClass();
        return l0(new u1(T2.REFERENCE, biConsumer2, biConsumer, supplier, 3));
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.D
    public final Iterator iterator() {
        return j$.util.f0.i(spliterator());
    }

    @Override // j$.util.stream.Stream
    public final Object[] l(j$.util.function.I i) {
        return t0.N(m0(i), i).s(i);
    }

    @Override // j$.util.stream.Stream
    public final Stream limit(long j) {
        if (j >= 0) {
            return t0.c0(this, 0L, j);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.Stream
    public final IntStream m(ToIntFunction toIntFunction) {
        toIntFunction.getClass();
        return new v(this, S2.p | S2.n, toIntFunction, 6);
    }

    @Override // j$.util.stream.Stream
    public final Stream map(Function function) {
        function.getClass();
        return new T1(this, S2.p | S2.n, function, 0);
    }

    @Override // j$.util.stream.Stream
    public final LongStream mapToLong(ToLongFunction toLongFunction) {
        toLongFunction.getClass();
        return new w(this, S2.p | S2.n, toLongFunction, 7);
    }

    @Override // j$.util.stream.Stream
    public final Optional max(Comparator comparator) {
        comparator.getClass();
        return q(new j$.util.function.c(comparator, 0));
    }

    @Override // j$.util.stream.Stream
    public final Optional min(Comparator comparator) {
        comparator.getClass();
        return q(new j$.util.function.c(comparator, 1));
    }

    @Override // j$.util.stream.Stream
    public final Stream n(Function function) {
        function.getClass();
        return new T1(this, S2.p | S2.n | S2.t, function, 1);
    }

    @Override // j$.util.stream.b
    final F0 n0(b bVar, j$.util.Q q, boolean z, j$.util.function.I i) {
        return t0.E(bVar, q, z, i);
    }

    @Override // j$.util.stream.b
    final void p0(j$.util.Q q, e2 e2Var) {
        while (!e2Var.q() && q.s(e2Var)) {
        }
    }

    @Override // j$.util.stream.Stream
    public final Optional q(j$.util.function.f fVar) {
        fVar.getClass();
        return (Optional) l0(new y1(T2.REFERENCE, fVar, 1));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.b
    public final T2 q0() {
        return T2.REFERENCE;
    }

    @Override // j$.util.stream.Stream
    public final Stream skip(long j) {
        if (j >= 0) {
            return j == 0 ? this : t0.c0(this, j, -1L);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.Stream
    public final Stream sorted() {
        return new z2(this);
    }

    @Override // j$.util.stream.Stream
    public final Stream sorted(Comparator comparator) {
        return new z2(this, comparator);
    }

    @Override // j$.util.stream.Stream
    public final Object[] toArray() {
        return l(new Q1(0));
    }

    @Override // j$.util.stream.BaseStream
    public final BaseStream unordered() {
        return !t0() ? this : new S1(this, S2.r);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.b
    public final x0 v0(long j, j$.util.function.I i) {
        return t0.D(j, i);
    }

    @Override // j$.util.stream.Stream
    public final Object w(Object obj, BiFunction biFunction, j$.util.function.f fVar) {
        biFunction.getClass();
        fVar.getClass();
        return l0(new u1(T2.REFERENCE, fVar, biFunction, obj, 2));
    }

    @Override // j$.util.stream.Stream
    public final D y(Function function) {
        function.getClass();
        return new t(this, S2.p | S2.n | S2.t, function, 7);
    }
}
