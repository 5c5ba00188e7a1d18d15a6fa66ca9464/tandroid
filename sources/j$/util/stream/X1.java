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
/* loaded from: classes2.dex */
abstract class X1 extends c implements Stream {
    /* JADX INFO: Access modifiers changed from: package-private */
    public X1(j$.util.Q q, int i, boolean z) {
        super(q, i, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public X1(c cVar, int i) {
        super(cVar, i);
    }

    @Override // j$.util.stream.Stream
    public final boolean C(Predicate predicate) {
        return ((Boolean) a1(u0.U0(predicate, r0.ANY))).booleanValue();
    }

    public void F(Consumer consumer) {
        consumer.getClass();
        a1(new Q(consumer, true));
    }

    @Override // j$.util.stream.Stream
    public final Object G(Supplier supplier, BiConsumer biConsumer, BiConsumer biConsumer2) {
        supplier.getClass();
        biConsumer.getClass();
        biConsumer2.getClass();
        return a1(new v1(U2.REFERENCE, biConsumer2, biConsumer, supplier, 3));
    }

    @Override // j$.util.stream.Stream
    public final IntStream I(ToIntFunction toIntFunction) {
        toIntFunction.getClass();
        return new w(this, T2.p | T2.n, toIntFunction, 6);
    }

    @Override // j$.util.stream.Stream
    public final Stream K(Function function) {
        function.getClass();
        return new T1(this, T2.p | T2.n | T2.t, function, 1);
    }

    @Override // j$.util.stream.Stream
    public final Optional L(j$.util.function.f fVar) {
        fVar.getClass();
        return (Optional) a1(new z1(U2.REFERENCE, fVar, 1));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.u0
    public final y0 T0(long j, j$.util.function.N n) {
        return u1.g(j, n);
    }

    @Override // j$.util.stream.Stream
    public final boolean X(Predicate predicate) {
        return ((Boolean) a1(u0.U0(predicate, r0.ALL))).booleanValue();
    }

    @Override // j$.util.stream.Stream
    public final LongStream Y(Function function) {
        function.getClass();
        return new x(this, T2.p | T2.n | T2.t, function, 6);
    }

    @Override // j$.util.stream.Stream
    public final IntStream c(Function function) {
        function.getClass();
        return new w(this, T2.p | T2.n | T2.t, function, 7);
    }

    @Override // j$.util.stream.c
    final D0 c1(u0 u0Var, j$.util.Q q, boolean z, j$.util.function.N n) {
        return u1.h(u0Var, q, z, n);
    }

    @Override // j$.util.stream.Stream
    public final Object collect(Collector collector) {
        Object a1;
        if (isParallel() && collector.characteristics().contains(i.CONCURRENT) && (!g1() || collector.characteristics().contains(i.UNORDERED))) {
            a1 = collector.supplier().get();
            forEach(new n(5, collector.accumulator(), a1));
        } else {
            collector.getClass();
            Supplier supplier = collector.supplier();
            a1 = a1(new E1(U2.REFERENCE, collector.combiner(), collector.accumulator(), supplier, collector));
        }
        return collector.characteristics().contains(i.IDENTITY_FINISH) ? a1 : collector.finisher().apply(a1);
    }

    @Override // j$.util.stream.Stream
    public final long count() {
        return ((j0) mapToLong(new J0(2))).sum();
    }

    @Override // j$.util.stream.Stream
    public final boolean d0(Predicate predicate) {
        return ((Boolean) a1(u0.U0(predicate, r0.NONE))).booleanValue();
    }

    @Override // j$.util.stream.c
    final void d1(j$.util.Q q, f2 f2Var) {
        while (!f2Var.h() && q.a(f2Var)) {
        }
    }

    @Override // j$.util.stream.Stream
    public final Stream distinct() {
        return new q(this, T2.m | T2.t);
    }

    @Override // j$.util.stream.c
    final U2 e1() {
        return U2.REFERENCE;
    }

    @Override // j$.util.stream.Stream
    public final Object[] f(j$.util.function.N n) {
        return u1.o(b1(n), n).o(n);
    }

    @Override // j$.util.stream.Stream
    public final F f0(ToDoubleFunction toDoubleFunction) {
        toDoubleFunction.getClass();
        return new u(this, T2.p | T2.n, toDoubleFunction, 6);
    }

    @Override // j$.util.stream.Stream
    public final Stream filter(Predicate predicate) {
        predicate.getClass();
        return new v(this, T2.t, predicate, 4);
    }

    @Override // j$.util.stream.Stream
    public final Optional findAny() {
        return (Optional) a1(new G(false, U2.REFERENCE, Optional.empty(), new J0(23), new b(13)));
    }

    @Override // j$.util.stream.Stream
    public final Optional findFirst() {
        return (Optional) a1(new G(true, U2.REFERENCE, Optional.empty(), new J0(23), new b(13)));
    }

    public void forEach(Consumer consumer) {
        consumer.getClass();
        a1(new Q(consumer, false));
    }

    @Override // j$.util.stream.Stream
    public final Object h0(Object obj, j$.util.function.f fVar) {
        fVar.getClass();
        return a1(new v1(U2.REFERENCE, fVar, fVar, obj, 2));
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    public final Iterator iterator() {
        return j$.util.f0.i(spliterator());
    }

    @Override // j$.util.stream.Stream
    public final Stream limit(long j) {
        if (j >= 0) {
            return u0.V0(this, 0L, j);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.Stream
    public final Object m(Object obj, BiFunction biFunction, j$.util.function.f fVar) {
        biFunction.getClass();
        fVar.getClass();
        return a1(new v1(U2.REFERENCE, fVar, biFunction, obj, 2));
    }

    @Override // j$.util.stream.Stream
    public final Stream map(Function function) {
        function.getClass();
        return new T1(this, T2.p | T2.n, function, 0);
    }

    @Override // j$.util.stream.Stream
    public final LongStream mapToLong(ToLongFunction toLongFunction) {
        toLongFunction.getClass();
        return new x(this, T2.p | T2.n, toLongFunction, 7);
    }

    @Override // j$.util.stream.Stream
    public final Optional max(Comparator comparator) {
        comparator.getClass();
        return L(new j$.util.function.c(comparator, 0));
    }

    @Override // j$.util.stream.Stream
    public final Optional min(Comparator comparator) {
        comparator.getClass();
        return L(new j$.util.function.c(comparator, 1));
    }

    @Override // j$.util.stream.Stream
    public final F o(Function function) {
        function.getClass();
        return new u(this, T2.p | T2.n | T2.t, function, 7);
    }

    @Override // j$.util.stream.c
    final j$.util.Q o1(u0 u0Var, a aVar, boolean z) {
        return new A3(u0Var, aVar, z);
    }

    @Override // j$.util.stream.Stream
    public final Stream skip(long j) {
        if (j >= 0) {
            return j == 0 ? this : u0.V0(this, j, -1L);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.Stream
    public final Stream sorted() {
        return new A2(this);
    }

    @Override // j$.util.stream.Stream
    public final Stream sorted(Comparator comparator) {
        return new A2(this, comparator);
    }

    @Override // j$.util.stream.Stream
    public final Object[] toArray() {
        return f(new J0(1));
    }

    @Override // j$.util.stream.BaseStream
    public final BaseStream unordered() {
        return !g1() ? this : new S1(this, T2.r);
    }

    @Override // j$.util.stream.Stream
    public final Stream v(Consumer consumer) {
        consumer.getClass();
        return new v(this, 0, consumer, 3);
    }
}
