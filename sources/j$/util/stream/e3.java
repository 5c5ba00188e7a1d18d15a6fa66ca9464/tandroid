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
import j$.util.stream.Collector;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class e3 extends c implements Stream {
    /* JADX INFO: Access modifiers changed from: package-private */
    public e3(j$.util.s sVar, int i, boolean z) {
        super(sVar, i, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public e3(c cVar, int i) {
        super(cVar, i);
    }

    @Override // j$.util.stream.Stream
    public final Object A(Object obj, BiFunction biFunction, j$.util.function.b bVar) {
        Objects.requireNonNull(biFunction);
        Objects.requireNonNull(bVar);
        return s0(new z2(e4.REFERENCE, bVar, biFunction, obj));
    }

    @Override // j$.util.stream.Stream
    public final V D(Function function) {
        Objects.requireNonNull(function);
        return new L(this, this, e4.REFERENCE, d4.p | d4.n | d4.t, function);
    }

    @Override // j$.util.stream.c
    final j$.util.s F0(y2 y2Var, Supplier supplier, boolean z) {
        return new L4(y2Var, supplier, z);
    }

    @Override // j$.util.stream.Stream
    public final Stream S(Consumer consumer) {
        Objects.requireNonNull(consumer);
        return new M(this, this, e4.REFERENCE, 0, consumer);
    }

    @Override // j$.util.stream.Stream
    public final boolean T(Predicate predicate) {
        return ((Boolean) s0(o1.x(predicate, k1.ALL))).booleanValue();
    }

    @Override // j$.util.stream.Stream
    public final LongStream U(Function function) {
        Objects.requireNonNull(function);
        return new O(this, this, e4.REFERENCE, d4.p | d4.n | d4.t, function);
    }

    @Override // j$.util.stream.Stream
    public final boolean Z(Predicate predicate) {
        return ((Boolean) s0(o1.x(predicate, k1.NONE))).booleanValue();
    }

    @Override // j$.util.stream.Stream
    public final boolean a(Predicate predicate) {
        return ((Boolean) s0(o1.x(predicate, k1.ANY))).booleanValue();
    }

    @Override // j$.util.stream.Stream
    public final IntStream c(Function function) {
        Objects.requireNonNull(function);
        return new N(this, this, e4.REFERENCE, d4.p | d4.n | d4.t, function);
    }

    @Override // j$.util.stream.Stream
    public final Object collect(Collector collector) {
        Object s0;
        if (isParallel() && collector.characteristics().contains(Collector.a.CONCURRENT) && (!x0() || collector.characteristics().contains(Collector.a.UNORDERED))) {
            s0 = collector.supplier().get();
            forEach(new p(collector.accumulator(), s0));
        } else {
            Objects.requireNonNull(collector);
            Supplier supplier = collector.supplier();
            s0 = s0(new I2(e4.REFERENCE, collector.combiner(), collector.accumulator(), supplier, collector));
        }
        return collector.characteristics().contains(Collector.a.IDENTITY_FINISH) ? s0 : collector.finisher().apply(s0);
    }

    @Override // j$.util.stream.Stream
    public final long count() {
        return ((e1) mapToLong(new ToLongFunction() { // from class: j$.util.stream.X2
            @Override // j$.util.function.ToLongFunction
            public final long applyAsLong(Object obj) {
                return 1L;
            }
        })).sum();
    }

    @Override // j$.util.stream.Stream
    public final Stream distinct() {
        return new t(this, e4.REFERENCE, d4.m | d4.t);
    }

    public void e(Consumer consumer) {
        Objects.requireNonNull(consumer);
        s0(new o0(consumer, true));
    }

    @Override // j$.util.stream.Stream
    public final V e0(ToDoubleFunction toDoubleFunction) {
        Objects.requireNonNull(toDoubleFunction);
        return new L(this, this, e4.REFERENCE, d4.p | d4.n, toDoubleFunction);
    }

    @Override // j$.util.stream.Stream
    public final Stream filter(Predicate predicate) {
        Objects.requireNonNull(predicate);
        return new M(this, this, e4.REFERENCE, d4.t, predicate);
    }

    @Override // j$.util.stream.Stream
    public final Optional findAny() {
        return (Optional) s0(new e0(false, e4.REFERENCE, Optional.empty(), W.a, d0.a));
    }

    @Override // j$.util.stream.Stream
    public final Optional findFirst() {
        return (Optional) s0(new e0(true, e4.REFERENCE, Optional.empty(), W.a, d0.a));
    }

    public void forEach(Consumer consumer) {
        Objects.requireNonNull(consumer);
        s0(new o0(consumer, false));
    }

    @Override // j$.util.stream.Stream
    public final Object h0(Object obj, j$.util.function.b bVar) {
        Objects.requireNonNull(bVar);
        return s0(new z2(e4.REFERENCE, bVar, bVar, obj));
    }

    @Override // j$.util.stream.Stream
    public final Object i(Supplier supplier, BiConsumer biConsumer, BiConsumer biConsumer2) {
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(biConsumer);
        Objects.requireNonNull(biConsumer2);
        return s0(new z2(e4.REFERENCE, biConsumer2, biConsumer, supplier));
    }

    @Override // j$.util.stream.g
    public final Iterator iterator() {
        return j$.util.I.i(spliterator());
    }

    @Override // j$.util.stream.Stream
    public final Object[] l(j$.util.function.m mVar) {
        return x2.l(t0(mVar), mVar).q(mVar);
    }

    @Override // j$.util.stream.Stream
    public final Stream limit(long j) {
        if (j >= 0) {
            return B3.i(this, 0L, j);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.Stream
    public final IntStream m(ToIntFunction toIntFunction) {
        Objects.requireNonNull(toIntFunction);
        return new N(this, this, e4.REFERENCE, d4.p | d4.n, toIntFunction);
    }

    @Override // j$.util.stream.Stream
    public final Stream map(Function function) {
        Objects.requireNonNull(function);
        return new a3(this, this, e4.REFERENCE, d4.p | d4.n, function, 0);
    }

    @Override // j$.util.stream.Stream
    public final LongStream mapToLong(ToLongFunction toLongFunction) {
        Objects.requireNonNull(toLongFunction);
        return new O(this, this, e4.REFERENCE, d4.p | d4.n, toLongFunction);
    }

    @Override // j$.util.stream.Stream
    public final Optional max(Comparator comparator) {
        Objects.requireNonNull(comparator);
        return s(new j$.util.function.a(comparator, 0));
    }

    @Override // j$.util.stream.Stream
    public final Optional min(Comparator comparator) {
        Objects.requireNonNull(comparator);
        return s(new j$.util.function.a(comparator, 1));
    }

    @Override // j$.util.stream.Stream
    public final Stream n(Function function) {
        Objects.requireNonNull(function);
        return new a3(this, this, e4.REFERENCE, d4.p | d4.n | d4.t, function, 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.y2
    public final s1 o0(long j, j$.util.function.m mVar) {
        return x2.d(j, mVar);
    }

    @Override // j$.util.stream.Stream
    public final Optional s(j$.util.function.b bVar) {
        Objects.requireNonNull(bVar);
        return (Optional) s0(new D2(e4.REFERENCE, bVar));
    }

    @Override // j$.util.stream.Stream
    public final Stream skip(long j) {
        int i = (j > 0L ? 1 : (j == 0L ? 0 : -1));
        if (i >= 0) {
            return i == 0 ? this : B3.i(this, j, -1L);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.Stream
    public final Stream sorted() {
        return new M3(this);
    }

    @Override // j$.util.stream.Stream
    public final Object[] toArray() {
        W2 w2 = new j$.util.function.m() { // from class: j$.util.stream.W2
            @Override // j$.util.function.m
            public final Object apply(int i) {
                return new Object[i];
            }
        };
        return x2.l(t0(w2), w2).q(w2);
    }

    @Override // j$.util.stream.c
    final A1 u0(y2 y2Var, j$.util.s sVar, boolean z, j$.util.function.m mVar) {
        return x2.e(y2Var, sVar, z, mVar);
    }

    @Override // j$.util.stream.g
    public g unordered() {
        return !x0() ? this : new Z2(this, this, e4.REFERENCE, d4.r);
    }

    @Override // j$.util.stream.c
    final void v0(j$.util.s sVar, m3 m3Var) {
        while (!m3Var.o() && sVar.b(m3Var)) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public final e4 w0() {
        return e4.REFERENCE;
    }

    @Override // j$.util.stream.Stream
    public final Stream sorted(Comparator comparator) {
        return new M3(this, comparator);
    }
}
