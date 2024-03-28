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
public abstract class f3 extends c implements Stream {
    /* JADX INFO: Access modifiers changed from: package-private */
    public f3(c cVar, int i) {
        super(cVar, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public f3(j$.util.t tVar, int i, boolean z) {
        super(tVar, i, z);
    }

    @Override // j$.util.stream.Stream
    public final Object B(Object obj, BiFunction biFunction, j$.util.function.b bVar) {
        Objects.requireNonNull(biFunction);
        Objects.requireNonNull(bVar);
        return v0(new A2(f4.REFERENCE, bVar, biFunction, obj));
    }

    @Override // j$.util.stream.Stream
    public final V E(Function function) {
        Objects.requireNonNull(function);
        return new L(this, this, f4.REFERENCE, e4.p | e4.n | e4.t, function);
    }

    @Override // j$.util.stream.c
    final j$.util.t I0(z2 z2Var, Supplier supplier, boolean z) {
        return new M4(z2Var, supplier, z);
    }

    @Override // j$.util.stream.Stream
    public final Stream U(Consumer consumer) {
        Objects.requireNonNull(consumer);
        return new M(this, this, f4.REFERENCE, 0, consumer);
    }

    @Override // j$.util.stream.Stream
    public final boolean V(Predicate predicate) {
        return ((Boolean) v0(p1.x(predicate, l1.ALL))).booleanValue();
    }

    @Override // j$.util.stream.Stream
    public final f1 W(Function function) {
        Objects.requireNonNull(function);
        return new O(this, this, f4.REFERENCE, e4.p | e4.n | e4.t, function);
    }

    @Override // j$.util.stream.Stream
    public final boolean a(Predicate predicate) {
        return ((Boolean) v0(p1.x(predicate, l1.ANY))).booleanValue();
    }

    @Override // j$.util.stream.Stream
    public final boolean b0(Predicate predicate) {
        return ((Boolean) v0(p1.x(predicate, l1.NONE))).booleanValue();
    }

    @Override // j$.util.stream.Stream
    public final IntStream c(Function function) {
        Objects.requireNonNull(function);
        return new N(this, this, f4.REFERENCE, e4.p | e4.n | e4.t, function);
    }

    @Override // j$.util.stream.Stream
    public final Object collect(Collector collector) {
        Object v0;
        if (isParallel() && collector.characteristics().contains(Collector.a.CONCURRENT) && (!A0() || collector.characteristics().contains(Collector.a.UNORDERED))) {
            v0 = collector.supplier().get();
            forEach(new p(collector.accumulator(), v0));
        } else {
            Objects.requireNonNull(collector);
            Supplier supplier = collector.supplier();
            v0 = v0(new J2(f4.REFERENCE, collector.combiner(), collector.accumulator(), supplier, collector));
        }
        return collector.characteristics().contains(Collector.a.IDENTITY_FINISH) ? v0 : collector.finisher().apply(v0);
    }

    @Override // j$.util.stream.Stream
    public final long count() {
        return ((e1) e0(new ToLongFunction() { // from class: j$.util.stream.Y2
            @Override // j$.util.function.ToLongFunction
            public final long applyAsLong(Object obj) {
                return 1L;
            }
        })).sum();
    }

    @Override // j$.util.stream.Stream
    public final Stream distinct() {
        return new t(this, f4.REFERENCE, e4.m | e4.t);
    }

    public void e(Consumer consumer) {
        Objects.requireNonNull(consumer);
        v0(new o0(consumer, true));
    }

    @Override // j$.util.stream.Stream
    public final f1 e0(ToLongFunction toLongFunction) {
        Objects.requireNonNull(toLongFunction);
        return new O(this, this, f4.REFERENCE, e4.p | e4.n, toLongFunction);
    }

    @Override // j$.util.stream.Stream
    public final Stream filter(Predicate predicate) {
        Objects.requireNonNull(predicate);
        return new M(this, this, f4.REFERENCE, e4.t, predicate);
    }

    @Override // j$.util.stream.Stream
    public final Optional findAny() {
        return (Optional) v0(new e0(false, f4.REFERENCE, Optional.empty(), W.a, d0.a));
    }

    @Override // j$.util.stream.Stream
    public final Optional findFirst() {
        return (Optional) v0(new e0(true, f4.REFERENCE, Optional.empty(), W.a, d0.a));
    }

    public void forEach(Consumer consumer) {
        Objects.requireNonNull(consumer);
        v0(new o0(consumer, false));
    }

    @Override // j$.util.stream.Stream
    public final V h0(ToDoubleFunction toDoubleFunction) {
        Objects.requireNonNull(toDoubleFunction);
        return new L(this, this, f4.REFERENCE, e4.p | e4.n, toDoubleFunction);
    }

    @Override // j$.util.stream.Stream
    public final Object i(Supplier supplier, BiConsumer biConsumer, BiConsumer biConsumer2) {
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(biConsumer);
        Objects.requireNonNull(biConsumer2);
        return v0(new A2(f4.REFERENCE, biConsumer2, biConsumer, supplier));
    }

    @Override // j$.util.stream.g
    public final Iterator iterator() {
        return j$.util.J.i(spliterator());
    }

    @Override // j$.util.stream.Stream
    public final Object k0(Object obj, j$.util.function.b bVar) {
        Objects.requireNonNull(bVar);
        return v0(new A2(f4.REFERENCE, bVar, bVar, obj));
    }

    @Override // j$.util.stream.Stream
    public final Object[] l(j$.util.function.m mVar) {
        return y2.l(w0(mVar), mVar).q(mVar);
    }

    @Override // j$.util.stream.Stream
    public final Stream limit(long j) {
        if (j >= 0) {
            return C3.i(this, 0L, j);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.Stream
    public final IntStream m(ToIntFunction toIntFunction) {
        Objects.requireNonNull(toIntFunction);
        return new N(this, this, f4.REFERENCE, e4.p | e4.n, toIntFunction);
    }

    @Override // j$.util.stream.Stream
    public final Optional max(Comparator comparator) {
        Objects.requireNonNull(comparator);
        return t(new j$.util.function.a(comparator, 0));
    }

    @Override // j$.util.stream.Stream
    public final Optional min(Comparator comparator) {
        Objects.requireNonNull(comparator);
        return t(new j$.util.function.a(comparator, 1));
    }

    @Override // j$.util.stream.Stream
    public final Stream n(Function function) {
        Objects.requireNonNull(function);
        return new b3(this, this, f4.REFERENCE, e4.p | e4.n, function, 0);
    }

    @Override // j$.util.stream.Stream
    public final Stream o(Function function) {
        Objects.requireNonNull(function);
        return new b3(this, this, f4.REFERENCE, e4.p | e4.n | e4.t, function, 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.z2
    public final t1 r0(long j, j$.util.function.m mVar) {
        return y2.d(j, mVar);
    }

    @Override // j$.util.stream.Stream
    public final Stream skip(long j) {
        int i = (j > 0L ? 1 : (j == 0L ? 0 : -1));
        if (i >= 0) {
            return i == 0 ? this : C3.i(this, j, -1L);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.Stream
    public final Stream sorted() {
        return new N3(this);
    }

    @Override // j$.util.stream.Stream
    public final Optional t(j$.util.function.b bVar) {
        Objects.requireNonNull(bVar);
        return (Optional) v0(new E2(f4.REFERENCE, bVar));
    }

    @Override // j$.util.stream.Stream
    public final Object[] toArray() {
        X2 x2 = new j$.util.function.m() { // from class: j$.util.stream.X2
            @Override // j$.util.function.m
            public final Object apply(int i) {
                return new Object[i];
            }
        };
        return y2.l(w0(x2), x2).q(x2);
    }

    @Override // j$.util.stream.g
    public g unordered() {
        return !A0() ? this : new a3(this, this, f4.REFERENCE, e4.r);
    }

    @Override // j$.util.stream.c
    final B1 x0(z2 z2Var, j$.util.t tVar, boolean z, j$.util.function.m mVar) {
        return y2.e(z2Var, tVar, z, mVar);
    }

    @Override // j$.util.stream.c
    final void y0(j$.util.t tVar, n3 n3Var) {
        while (!n3Var.o() && tVar.b(n3Var)) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public final f4 z0() {
        return f4.REFERENCE;
    }

    @Override // j$.util.stream.Stream
    public final Stream sorted(Comparator comparator) {
        return new N3(this, comparator);
    }
}
