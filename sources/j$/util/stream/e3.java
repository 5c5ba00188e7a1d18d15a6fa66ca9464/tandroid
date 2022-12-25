package j$.util.stream;

import j$.util.Optional;
import j$.util.function.BiConsumer;
import j$.util.function.BiFunction;
import j$.util.function.Consumer;
import j$.util.function.Function;
import j$.util.function.Predicate;
import j$.util.function.ToIntFunction;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class e3 extends c implements Stream {
    /* JADX INFO: Access modifiers changed from: package-private */
    public e3(c cVar, int i) {
        super(cVar, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public e3(j$.util.u uVar, int i, boolean z) {
        super(uVar, i, z);
    }

    @Override // j$.util.stream.c
    final void A0(j$.util.u uVar, m3 m3Var) {
        while (!m3Var.o() && uVar.b(m3Var)) {
        }
    }

    @Override // j$.util.stream.Stream
    public final Object B(Object obj, BiFunction biFunction, j$.util.function.b bVar) {
        Objects.requireNonNull(biFunction);
        Objects.requireNonNull(bVar);
        return x0(new z2(e4.REFERENCE, bVar, biFunction, obj));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public final e4 B0() {
        return e4.REFERENCE;
    }

    @Override // j$.util.stream.Stream
    public final U E(Function function) {
        Objects.requireNonNull(function);
        return new K(this, this, e4.REFERENCE, d4.p | d4.n | d4.t, function);
    }

    @Override // j$.util.stream.c
    final j$.util.u K0(y2 y2Var, j$.util.function.y yVar, boolean z) {
        return new L4(y2Var, yVar, z);
    }

    @Override // j$.util.stream.Stream
    public final Stream T(Predicate predicate) {
        Objects.requireNonNull(predicate);
        return new L(this, this, e4.REFERENCE, d4.t, predicate);
    }

    @Override // j$.util.stream.Stream
    public final Stream V(Consumer consumer) {
        Objects.requireNonNull(consumer);
        return new L(this, this, e4.REFERENCE, 0, consumer);
    }

    @Override // j$.util.stream.Stream
    public final boolean W(Predicate predicate) {
        return ((Boolean) x0(o1.x(predicate, k1.ALL))).booleanValue();
    }

    @Override // j$.util.stream.Stream
    public final e1 X(Function function) {
        Objects.requireNonNull(function);
        return new N(this, this, e4.REFERENCE, d4.p | d4.n | d4.t, function);
    }

    @Override // j$.util.stream.Stream
    public final boolean a(Predicate predicate) {
        return ((Boolean) x0(o1.x(predicate, k1.ANY))).booleanValue();
    }

    @Override // j$.util.stream.Stream
    public final Object b0(j$.wrappers.I0 i0) {
        Object x0;
        if (!isParallel() || !i0.b().contains(h.CONCURRENT) || (C0() && !i0.b().contains(h.UNORDERED))) {
            Objects.requireNonNull(i0);
            j$.util.function.y f = i0.f();
            x0 = x0(new I2(e4.REFERENCE, i0.c(), i0.a(), f, i0));
        } else {
            x0 = i0.f().get();
            forEach(new o(i0.a(), x0));
        }
        return i0.b().contains(h.IDENTITY_FINISH) ? x0 : i0.e().apply(x0);
    }

    @Override // j$.util.stream.Stream
    public final IntStream c(Function function) {
        Objects.requireNonNull(function);
        return new M(this, this, e4.REFERENCE, d4.p | d4.n | d4.t, function);
    }

    @Override // j$.util.stream.Stream
    public final long count() {
        return ((d1) g0(X2.a)).sum();
    }

    @Override // j$.util.stream.Stream
    public final boolean d0(Predicate predicate) {
        return ((Boolean) x0(o1.x(predicate, k1.NONE))).booleanValue();
    }

    @Override // j$.util.stream.Stream
    public final Stream distinct() {
        return new s(this, e4.REFERENCE, d4.m | d4.t);
    }

    public void e(Consumer consumer) {
        Objects.requireNonNull(consumer);
        x0(new n0(consumer, true));
    }

    @Override // j$.util.stream.Stream
    public final Optional findAny() {
        return (Optional) x0(new d0(false, e4.REFERENCE, Optional.empty(), V.a, c0.a));
    }

    @Override // j$.util.stream.Stream
    public final Optional findFirst() {
        return (Optional) x0(new d0(true, e4.REFERENCE, Optional.empty(), V.a, c0.a));
    }

    public void forEach(Consumer consumer) {
        Objects.requireNonNull(consumer);
        x0(new n0(consumer, false));
    }

    @Override // j$.util.stream.Stream
    public final e1 g0(j$.util.function.A a) {
        Objects.requireNonNull(a);
        return new N(this, this, e4.REFERENCE, d4.p | d4.n, a);
    }

    @Override // j$.util.stream.Stream
    public final Object i(j$.util.function.y yVar, BiConsumer biConsumer, BiConsumer biConsumer2) {
        Objects.requireNonNull(yVar);
        Objects.requireNonNull(biConsumer);
        Objects.requireNonNull(biConsumer2);
        return x0(new z2(e4.REFERENCE, biConsumer2, biConsumer, yVar));
    }

    @Override // j$.util.stream.g
    public final Iterator iterator() {
        return j$.util.L.i(spliterator());
    }

    @Override // j$.util.stream.Stream
    public final U j0(j$.util.function.z zVar) {
        Objects.requireNonNull(zVar);
        return new K(this, this, e4.REFERENCE, d4.p | d4.n, zVar);
    }

    @Override // j$.util.stream.Stream
    public final Object[] l(j$.util.function.m mVar) {
        return x2.l(y0(mVar), mVar).q(mVar);
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
        return new M(this, this, e4.REFERENCE, d4.p | d4.n, toIntFunction);
    }

    @Override // j$.util.stream.Stream
    public final Object m0(Object obj, j$.util.function.b bVar) {
        Objects.requireNonNull(bVar);
        return x0(new z2(e4.REFERENCE, bVar, bVar, obj));
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
        return new a3(this, this, e4.REFERENCE, d4.p | d4.n, function, 0);
    }

    @Override // j$.util.stream.Stream
    public final Stream o(Function function) {
        Objects.requireNonNull(function);
        return new a3(this, this, e4.REFERENCE, d4.p | d4.n | d4.t, function, 1);
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
    public final Optional t(j$.util.function.b bVar) {
        Objects.requireNonNull(bVar);
        return (Optional) x0(new D2(e4.REFERENCE, bVar));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.y2
    public final s1 t0(long j, j$.util.function.m mVar) {
        return x2.d(j, mVar);
    }

    @Override // j$.util.stream.Stream
    public final Object[] toArray() {
        W2 w2 = W2.a;
        return x2.l(y0(w2), w2).q(w2);
    }

    @Override // j$.util.stream.g
    public g unordered() {
        return !C0() ? this : new Z2(this, this, e4.REFERENCE, d4.r);
    }

    @Override // j$.util.stream.c
    final A1 z0(y2 y2Var, j$.util.u uVar, boolean z, j$.util.function.m mVar) {
        return x2.e(y2Var, uVar, z, mVar);
    }

    @Override // j$.util.stream.Stream
    public final Stream sorted(Comparator comparator) {
        return new M3(this, comparator);
    }
}
