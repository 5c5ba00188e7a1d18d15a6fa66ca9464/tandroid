package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.p;
import j$.util.u;
import java.util.Iterator;
/* loaded from: classes2.dex */
public abstract class L0 extends c implements IntStream {
    public L0(c cVar, int i) {
        super(cVar, i);
    }

    public L0(j$.util.u uVar, int i, boolean z) {
        super(uVar, i, z);
    }

    public static /* synthetic */ u.a L0(j$.util.u uVar) {
        return M0(uVar);
    }

    public static u.a M0(j$.util.u uVar) {
        if (uVar instanceof u.a) {
            return (u.a) uVar;
        }
        if (!Q4.a) {
            throw new UnsupportedOperationException("IntStream.adapt(Spliterator<Integer> s)");
        }
        Q4.a(c.class, "using IntStream.adapt(Spliterator<Integer> s)");
        throw null;
    }

    @Override // j$.util.stream.IntStream
    public final U A(j$.wrappers.X x) {
        x.getClass();
        return new K(this, this, e4.INT_VALUE, d4.p | d4.n, x);
    }

    @Override // j$.util.stream.c
    final void A0(j$.util.u uVar, m3 m3Var) {
        j$.util.function.l b0;
        u.a M0 = M0(uVar);
        if (m3Var instanceof j$.util.function.l) {
            b0 = (j$.util.function.l) m3Var;
        } else if (Q4.a) {
            Q4.a(c.class, "using IntStream.adapt(Sink<Integer> s)");
            throw null;
        } else {
            b0 = new B0(m3Var);
        }
        while (!m3Var.o() && M0.g(b0)) {
        }
    }

    @Override // j$.util.stream.c
    public final e4 B0() {
        return e4.INT_VALUE;
    }

    @Override // j$.util.stream.IntStream
    public final boolean C(j$.wrappers.V v) {
        return ((Boolean) x0(o1.v(v, k1.ALL))).booleanValue();
    }

    @Override // j$.util.stream.IntStream
    public final boolean F(j$.wrappers.V v) {
        return ((Boolean) x0(o1.v(v, k1.ANY))).booleanValue();
    }

    public void I(j$.util.function.l lVar) {
        lVar.getClass();
        x0(new l0(lVar, true));
    }

    @Override // j$.util.stream.IntStream
    public final Stream J(j$.util.function.m mVar) {
        mVar.getClass();
        return new L(this, this, e4.INT_VALUE, d4.p | d4.n, mVar);
    }

    @Override // j$.util.stream.c
    final j$.util.u K0(y2 y2Var, j$.util.function.y yVar, boolean z) {
        return new q4(y2Var, yVar, z);
    }

    @Override // j$.util.stream.IntStream
    public final int N(int i, j$.util.function.j jVar) {
        jVar.getClass();
        return ((Integer) x0(new L2(e4.INT_VALUE, jVar, i))).intValue();
    }

    @Override // j$.util.stream.IntStream
    public final IntStream P(j$.util.function.m mVar) {
        return new M(this, this, e4.INT_VALUE, d4.p | d4.n | d4.t, mVar);
    }

    public void U(j$.util.function.l lVar) {
        lVar.getClass();
        x0(new l0(lVar, false));
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.k a0(j$.util.function.j jVar) {
        jVar.getClass();
        return (j$.util.k) x0(new D2(e4.INT_VALUE, jVar));
    }

    @Override // j$.util.stream.IntStream
    public final U asDoubleStream() {
        return new O(this, this, e4.INT_VALUE, d4.p | d4.n);
    }

    @Override // j$.util.stream.IntStream
    public final e1 asLongStream() {
        return new G0(this, this, e4.INT_VALUE, d4.p | d4.n);
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.j average() {
        long[] jArr = (long[]) k0(v0.a, u0.a, x0.a);
        if (jArr[0] > 0) {
            double d = jArr[1];
            double d2 = jArr[0];
            Double.isNaN(d);
            Double.isNaN(d2);
            return j$.util.j.d(d / d2);
        }
        return j$.util.j.a();
    }

    @Override // j$.util.stream.IntStream
    public final Stream boxed() {
        return J(C0.a);
    }

    @Override // j$.util.stream.IntStream
    public final IntStream c0(j$.util.function.l lVar) {
        lVar.getClass();
        return new M(this, this, e4.INT_VALUE, 0, lVar);
    }

    @Override // j$.util.stream.IntStream
    public final long count() {
        return ((d1) f(E0.a)).sum();
    }

    @Override // j$.util.stream.IntStream
    public final IntStream distinct() {
        return ((e3) J(C0.a)).distinct().m(w0.a);
    }

    @Override // j$.util.stream.IntStream
    public final e1 f(j$.util.function.n nVar) {
        nVar.getClass();
        return new N(this, this, e4.INT_VALUE, d4.p | d4.n, nVar);
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.k findAny() {
        return (j$.util.k) x0(new d0(false, e4.INT_VALUE, j$.util.k.a(), X.a, a0.a));
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.k findFirst() {
        return (j$.util.k) x0(new d0(true, e4.INT_VALUE, j$.util.k.a(), X.a, a0.a));
    }

    @Override // j$.util.stream.IntStream
    public final IntStream h(j$.wrappers.V v) {
        v.getClass();
        return new M(this, this, e4.INT_VALUE, d4.t, v);
    }

    @Override // j$.util.stream.g
    public final p.a iterator() {
        return j$.util.L.g(spliterator());
    }

    @Override // j$.util.stream.g
    public Iterator iterator() {
        return j$.util.L.g(spliterator());
    }

    @Override // j$.util.stream.IntStream
    public final Object k0(j$.util.function.y yVar, j$.util.function.v vVar, BiConsumer biConsumer) {
        C c = new C(biConsumer, 1);
        yVar.getClass();
        vVar.getClass();
        return x0(new z2(e4.INT_VALUE, c, vVar, yVar));
    }

    @Override // j$.util.stream.IntStream
    public final IntStream limit(long j) {
        if (j >= 0) {
            return B3.g(this, 0L, j);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.k max() {
        return a0(z0.a);
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.k min() {
        return a0(A0.a);
    }

    @Override // j$.util.stream.IntStream
    public final IntStream q(j$.wrappers.b0 b0Var) {
        b0Var.getClass();
        return new M(this, this, e4.INT_VALUE, d4.p | d4.n, b0Var);
    }

    @Override // j$.util.stream.IntStream
    public final IntStream skip(long j) {
        int i = (j > 0L ? 1 : (j == 0L ? 0 : -1));
        if (i >= 0) {
            return i == 0 ? this : B3.g(this, j, -1L);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.IntStream
    public final IntStream sorted() {
        return new K3(this);
    }

    @Override // j$.util.stream.c, j$.util.stream.g
    public final u.a spliterator() {
        return M0(super.spliterator());
    }

    @Override // j$.util.stream.IntStream
    public final int sum() {
        return ((Integer) x0(new L2(e4.INT_VALUE, y0.a, 0))).intValue();
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.h summaryStatistics() {
        return (j$.util.h) k0(j.a, t0.a, s0.a);
    }

    @Override // j$.util.stream.y2
    public final s1 t0(long j, j$.util.function.m mVar) {
        return x2.p(j);
    }

    @Override // j$.util.stream.IntStream
    public final int[] toArray() {
        return (int[]) x2.n((w1) y0(D0.a)).e();
    }

    @Override // j$.util.stream.g
    public g unordered() {
        return !C0() ? this : new H0(this, this, e4.INT_VALUE, d4.r);
    }

    @Override // j$.util.stream.IntStream
    public final boolean v(j$.wrappers.V v) {
        return ((Boolean) x0(o1.v(v, k1.NONE))).booleanValue();
    }

    @Override // j$.util.stream.c
    final A1 z0(y2 y2Var, j$.util.u uVar, boolean z, j$.util.function.m mVar) {
        return x2.g(y2Var, uVar, z);
    }
}
