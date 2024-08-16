package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.LongFunction;
import j$.util.function.Supplier;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class h0 extends b implements LongStream {
    /* JADX INFO: Access modifiers changed from: package-private */
    public h0(j$.util.Q q, int i) {
        super(q, i, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public h0(b bVar, int i) {
        super(bVar, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static j$.util.K H0(j$.util.Q q) {
        if (q instanceof j$.util.K) {
            return (j$.util.K) q;
        }
        if (A3.a) {
            A3.a(b.class, "using LongStream.adapt(Spliterator<Long> s)");
            throw null;
        }
        throw new UnsupportedOperationException("LongStream.adapt(Spliterator<Long> s)");
    }

    @Override // j$.util.stream.LongStream
    public final IntStream A(j$.util.function.b0 b0Var) {
        b0Var.getClass();
        return new v(this, S2.p | S2.n, b0Var, 5);
    }

    @Override // j$.util.stream.b
    final j$.util.Q C0(b bVar, Supplier supplier, boolean z) {
        return new h3(bVar, supplier, z);
    }

    @Override // j$.util.stream.LongStream
    public final boolean E(j$.util.function.Z z) {
        return ((Boolean) l0(t0.a0(z, q0.ANY))).booleanValue();
    }

    @Override // j$.util.stream.LongStream
    public final boolean G(j$.util.function.Z z) {
        return ((Boolean) l0(t0.a0(z, q0.NONE))).booleanValue();
    }

    @Override // j$.util.stream.LongStream
    public final LongStream M(j$.util.function.Z z) {
        z.getClass();
        return new w(this, S2.t, z, 4);
    }

    public void V(j$.util.function.W w) {
        w.getClass();
        l0(new O(w, true));
    }

    @Override // j$.util.stream.LongStream
    public final Object Z(Supplier supplier, j$.util.function.o0 o0Var, BiConsumer biConsumer) {
        r rVar = new r(biConsumer, 2);
        supplier.getClass();
        o0Var.getClass();
        return l0(new u1(T2.LONG_VALUE, rVar, o0Var, supplier, 0));
    }

    @Override // j$.util.stream.LongStream
    public final D asDoubleStream() {
        return new x(this, S2.p | S2.n, 2);
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.l average() {
        long[] jArr = (long[]) Z(new E(25), new E(26), new E(27));
        long j = jArr[0];
        if (j > 0) {
            double d = jArr[1];
            double d2 = j;
            Double.isNaN(d);
            Double.isNaN(d2);
            return j$.util.l.d(d / d2);
        }
        return j$.util.l.a();
    }

    @Override // j$.util.stream.LongStream
    public final Stream boxed() {
        return mapToObj(new E(23));
    }

    @Override // j$.util.stream.LongStream
    public final long count() {
        return ((h0) v(new E(24))).sum();
    }

    public void d(j$.util.function.W w) {
        w.getClass();
        l0(new O(w, false));
    }

    @Override // j$.util.stream.LongStream
    public final LongStream distinct() {
        return ((W1) boxed()).distinct().mapToLong(new E(20));
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.n findAny() {
        return (j$.util.n) l0(new F(false, T2.LONG_VALUE, j$.util.n.a(), new E(2), new l(7)));
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.n findFirst() {
        return (j$.util.n) l0(new F(true, T2.LONG_VALUE, j$.util.n.a(), new E(2), new l(7)));
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.n h(j$.util.function.S s) {
        s.getClass();
        return (j$.util.n) l0(new y1(T2.LONG_VALUE, s, 3));
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.D
    public final j$.util.z iterator() {
        return j$.util.f0.h(spliterator());
    }

    @Override // j$.util.stream.LongStream
    public final LongStream limit(long j) {
        if (j >= 0) {
            return t0.Z(this, 0L, j);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.LongStream
    public final Stream mapToObj(LongFunction longFunction) {
        longFunction.getClass();
        return new u(this, S2.p | S2.n, longFunction, 2);
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.n max() {
        return h(new E(28));
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.n min() {
        return h(new E(19));
    }

    @Override // j$.util.stream.b
    final F0 n0(b bVar, j$.util.Q q, boolean z, j$.util.function.I i) {
        return t0.H(bVar, q, z);
    }

    @Override // j$.util.stream.LongStream
    public final LongStream o(j$.util.function.W w) {
        w.getClass();
        return new w(this, w);
    }

    @Override // j$.util.stream.LongStream
    public final LongStream p(LongFunction longFunction) {
        return new w(this, S2.p | S2.n | S2.t, longFunction, 3);
    }

    @Override // j$.util.stream.b
    final void p0(j$.util.Q q, e2 e2Var) {
        j$.util.function.W c0Var;
        j$.util.K H0 = H0(q);
        if (e2Var instanceof j$.util.function.W) {
            c0Var = (j$.util.function.W) e2Var;
        } else if (A3.a) {
            A3.a(b.class, "using LongStream.adapt(Sink<Long> s)");
            throw null;
        } else {
            e2Var.getClass();
            c0Var = new c0(0, e2Var);
        }
        while (!e2Var.q() && H0.i(c0Var)) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.b
    public final T2 q0() {
        return T2.LONG_VALUE;
    }

    @Override // j$.util.stream.LongStream
    public final D r(j$.util.function.a0 a0Var) {
        a0Var.getClass();
        return new t(this, S2.p | S2.n, a0Var, 5);
    }

    @Override // j$.util.stream.LongStream
    public final LongStream skip(long j) {
        if (j >= 0) {
            return j == 0 ? this : t0.Z(this, j, -1L);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.LongStream
    public final LongStream sorted() {
        return new y2(this);
    }

    @Override // j$.util.stream.b, j$.util.stream.BaseStream, j$.util.stream.D
    public final j$.util.K spliterator() {
        return H0(super.spliterator());
    }

    @Override // j$.util.stream.LongStream
    public final long sum() {
        return x(0L, new E(18));
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.k summaryStatistics() {
        return (j$.util.k) Z(new l(16), new E(17), new E(21));
    }

    @Override // j$.util.stream.LongStream
    public final long[] toArray() {
        return (long[]) t0.Q((D0) m0(new E(22))).e();
    }

    @Override // j$.util.stream.LongStream
    public final boolean u(j$.util.function.Z z) {
        return ((Boolean) l0(t0.a0(z, q0.ALL))).booleanValue();
    }

    @Override // j$.util.stream.BaseStream
    public final BaseStream unordered() {
        return !t0() ? this : new X(this, S2.r, 1);
    }

    @Override // j$.util.stream.LongStream
    public final LongStream v(j$.util.function.f0 f0Var) {
        f0Var.getClass();
        return new w(this, S2.p | S2.n, f0Var, 2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.b
    public final x0 v0(long j, j$.util.function.I i) {
        return t0.T(j);
    }

    @Override // j$.util.stream.LongStream
    public final long x(long j, j$.util.function.S s) {
        s.getClass();
        return ((Long) l0(new K1(T2.LONG_VALUE, s, j))).longValue();
    }
}
