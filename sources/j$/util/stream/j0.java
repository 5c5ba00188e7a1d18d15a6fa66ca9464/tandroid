package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.LongFunction;
import j$.util.function.Supplier;
/* loaded from: classes2.dex */
abstract class j0 extends c implements LongStream {
    /* JADX INFO: Access modifiers changed from: package-private */
    public j0(j$.util.Q q, int i) {
        super(q, i, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public j0(c cVar, int i) {
        super(cVar, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static j$.util.K r1(j$.util.Q q) {
        if (q instanceof j$.util.K) {
            return (j$.util.K) q;
        }
        if (F3.a) {
            F3.a(c.class, "using LongStream.adapt(Spliterator<Long> s)");
            throw null;
        }
        throw new UnsupportedOperationException("LongStream.adapt(Spliterator<Long> s)");
    }

    public void E(j$.util.function.h0 h0Var) {
        h0Var.getClass();
        a1(new P(h0Var, false));
    }

    @Override // j$.util.stream.LongStream
    public final F J(j$.util.function.p0 p0Var) {
        p0Var.getClass();
        return new u(this, T2.p | T2.n, p0Var, 5);
    }

    @Override // j$.util.stream.LongStream
    public final LongStream M(j$.util.function.w0 w0Var) {
        w0Var.getClass();
        return new x(this, T2.p | T2.n, w0Var, 2);
    }

    @Override // j$.util.stream.LongStream
    public final IntStream T(j$.util.function.s0 s0Var) {
        s0Var.getClass();
        return new w(this, T2.p | T2.n, s0Var, 5);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.u0
    public final y0 T0(long j, j$.util.function.N n) {
        return u1.t(j);
    }

    @Override // j$.util.stream.LongStream
    public final boolean a(j$.util.function.m0 m0Var) {
        return ((Boolean) a1(u0.S0(m0Var, r0.NONE))).booleanValue();
    }

    @Override // j$.util.stream.LongStream
    public final F asDoubleStream() {
        return new y(this, T2.p | T2.n, 2);
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.l average() {
        long[] jArr = (long[]) y(new b(25), new b(26), new b(27));
        long j = jArr[0];
        if (j > 0) {
            double d = jArr[1];
            double d2 = j;
            Double.isNaN(d);
            Double.isNaN(d2);
            Double.isNaN(d);
            Double.isNaN(d2);
            return j$.util.l.d(d / d2);
        }
        return j$.util.l.a();
    }

    @Override // j$.util.stream.LongStream
    public final Stream boxed() {
        return mapToObj(new W(3));
    }

    @Override // j$.util.stream.LongStream
    public final boolean c0(j$.util.function.m0 m0Var) {
        return ((Boolean) a1(u0.S0(m0Var, r0.ANY))).booleanValue();
    }

    @Override // j$.util.stream.c
    final D0 c1(u0 u0Var, j$.util.Q q, boolean z, j$.util.function.N n) {
        return u1.k(u0Var, q, z);
    }

    @Override // j$.util.stream.LongStream
    public final long count() {
        return ((j0) M(new b(22))).sum();
    }

    @Override // j$.util.stream.c
    final void d1(j$.util.Q q, f2 f2Var) {
        j$.util.function.h0 e0Var;
        j$.util.K r1 = r1(q);
        if (f2Var instanceof j$.util.function.h0) {
            e0Var = (j$.util.function.h0) f2Var;
        } else if (F3.a) {
            F3.a(c.class, "using LongStream.adapt(Sink<Long> s)");
            throw null;
        } else {
            f2Var.getClass();
            e0Var = new e0(0, f2Var);
        }
        while (!f2Var.h() && r1.e(e0Var)) {
        }
    }

    @Override // j$.util.stream.LongStream
    public final LongStream distinct() {
        return ((X1) boxed()).distinct().mapToLong(new b(23));
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.n e(j$.util.function.d0 d0Var) {
        d0Var.getClass();
        return (j$.util.n) a1(new z1(U2.LONG_VALUE, d0Var, 3));
    }

    @Override // j$.util.stream.LongStream
    public final LongStream e0(j$.util.function.m0 m0Var) {
        m0Var.getClass();
        return new x(this, T2.t, m0Var, 4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public final U2 e1() {
        return U2.LONG_VALUE;
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.n findAny() {
        return (j$.util.n) a1(new G(false, U2.LONG_VALUE, j$.util.n.a(), new J0(22), new b(12)));
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.n findFirst() {
        return (j$.util.n) a1(new G(true, U2.LONG_VALUE, j$.util.n.a(), new J0(22), new b(12)));
    }

    @Override // j$.util.stream.LongStream
    public final LongStream g(j$.util.function.h0 h0Var) {
        h0Var.getClass();
        return new x(this, 0, h0Var, 5);
    }

    @Override // j$.util.stream.LongStream
    public final LongStream h(LongFunction longFunction) {
        return new x(this, T2.p | T2.n | T2.t, longFunction, 3);
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    public final j$.util.z iterator() {
        return j$.util.f0.h(spliterator());
    }

    @Override // j$.util.stream.LongStream
    public final LongStream limit(long j) {
        if (j >= 0) {
            return u0.R0(this, 0L, j);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.LongStream
    public final Stream mapToObj(LongFunction longFunction) {
        longFunction.getClass();
        return new v(this, T2.p | T2.n, longFunction, 2);
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.n max() {
        return e(new W(2));
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.n min() {
        return e(new W(1));
    }

    @Override // j$.util.stream.LongStream
    public final long n(long j, j$.util.function.d0 d0Var) {
        d0Var.getClass();
        return ((Long) a1(new L1(U2.LONG_VALUE, d0Var, j))).longValue();
    }

    @Override // j$.util.stream.c
    final j$.util.Q o1(u0 u0Var, a aVar, boolean z) {
        return new i3(u0Var, aVar, z);
    }

    @Override // j$.util.stream.LongStream
    public final LongStream skip(long j) {
        if (j >= 0) {
            return j == 0 ? this : u0.R0(this, j, -1L);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.LongStream
    public final LongStream sorted() {
        return new z2(this);
    }

    @Override // j$.util.stream.c, j$.util.stream.BaseStream, j$.util.stream.F
    public final j$.util.K spliterator() {
        return r1(super.spliterator());
    }

    @Override // j$.util.stream.LongStream
    public final long sum() {
        return n(0L, new W(4));
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.j summaryStatistics() {
        return (j$.util.j) y(new J0(12), new W(5), new W(6));
    }

    @Override // j$.util.stream.LongStream
    public final long[] toArray() {
        return (long[]) u1.r((B0) b1(new b(24))).b();
    }

    @Override // j$.util.stream.BaseStream
    public final BaseStream unordered() {
        return !g1() ? this : new Y(this, T2.r, 1);
    }

    public void x(j$.util.function.h0 h0Var) {
        h0Var.getClass();
        a1(new P(h0Var, true));
    }

    @Override // j$.util.stream.LongStream
    public final Object y(Supplier supplier, j$.util.function.F0 f0, BiConsumer biConsumer) {
        r rVar = new r(biConsumer, 2);
        supplier.getClass();
        f0.getClass();
        return a1(new v1(U2.LONG_VALUE, rVar, f0, supplier, 0));
    }

    @Override // j$.util.stream.LongStream
    public final boolean z(j$.util.function.m0 m0Var) {
        return ((Boolean) a1(u0.S0(m0Var, r0.ALL))).booleanValue();
    }
}
