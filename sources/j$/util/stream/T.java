package j$.util.stream;

import j$.util.function.BiConsumer;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes2.dex */
public abstract class T extends c implements U {
    public T(c cVar, int i) {
        super(cVar, i);
    }

    public T(j$.util.u uVar, int i, boolean z) {
        super(uVar, i, z);
    }

    public static /* synthetic */ j$.util.t L0(j$.util.u uVar) {
        return M0(uVar);
    }

    public static j$.util.t M0(j$.util.u uVar) {
        if (uVar instanceof j$.util.t) {
            return (j$.util.t) uVar;
        }
        if (!Q4.a) {
            throw new UnsupportedOperationException("DoubleStream.adapt(Spliterator<Double> s)");
        }
        Q4.a(c.class, "using DoubleStream.adapt(Spliterator<Double> s)");
        throw null;
    }

    @Override // j$.util.stream.c
    final void A0(j$.util.u uVar, m3 m3Var) {
        j$.util.function.f f;
        j$.util.t M0 = M0(uVar);
        if (m3Var instanceof j$.util.function.f) {
            f = (j$.util.function.f) m3Var;
        } else if (Q4.a) {
            Q4.a(c.class, "using DoubleStream.adapt(Sink<Double> s)");
            throw null;
        } else {
            f = new F(m3Var);
        }
        while (!m3Var.o() && M0.k(f)) {
        }
    }

    @Override // j$.util.stream.c
    public final e4 B0() {
        return e4.DOUBLE_VALUE;
    }

    @Override // j$.util.stream.U
    public final j$.util.j G(j$.util.function.d dVar) {
        Objects.requireNonNull(dVar);
        return (j$.util.j) x0(new D2(e4.DOUBLE_VALUE, dVar));
    }

    @Override // j$.util.stream.U
    public final Object H(j$.util.function.y yVar, j$.util.function.u uVar, BiConsumer biConsumer) {
        C c = new C(biConsumer, 0);
        Objects.requireNonNull(yVar);
        Objects.requireNonNull(uVar);
        return x0(new z2(e4.DOUBLE_VALUE, c, uVar, yVar));
    }

    @Override // j$.util.stream.U
    public final double K(double d, j$.util.function.d dVar) {
        Objects.requireNonNull(dVar);
        return ((Double) x0(new B2(e4.DOUBLE_VALUE, dVar, d))).doubleValue();
    }

    @Override // j$.util.stream.c
    final j$.util.u K0(y2 y2Var, j$.util.function.y yVar, boolean z) {
        return new o4(y2Var, yVar, z);
    }

    @Override // j$.util.stream.U
    public final Stream M(j$.util.function.g gVar) {
        Objects.requireNonNull(gVar);
        return new L(this, this, e4.DOUBLE_VALUE, d4.p | d4.n, gVar);
    }

    @Override // j$.util.stream.U
    public final IntStream R(j$.wrappers.F f) {
        Objects.requireNonNull(f);
        return new M(this, this, e4.DOUBLE_VALUE, d4.p | d4.n, f);
    }

    @Override // j$.util.stream.U
    public final boolean Y(j$.wrappers.D d) {
        return ((Boolean) x0(o1.u(d, k1.ALL))).booleanValue();
    }

    @Override // j$.util.stream.U
    public final j$.util.j average() {
        double[] dArr = (double[]) H(x.a, v.a, A.a);
        return dArr[2] > 0.0d ? j$.util.j.d(l.a(dArr) / dArr[2]) : j$.util.j.a();
    }

    @Override // j$.util.stream.U
    public final U b(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
        return new K(this, this, e4.DOUBLE_VALUE, 0, fVar);
    }

    @Override // j$.util.stream.U
    public final Stream boxed() {
        return M(G.a);
    }

    @Override // j$.util.stream.U
    public final long count() {
        return ((d1) x(H.a)).sum();
    }

    @Override // j$.util.stream.U
    public final U distinct() {
        return ((e3) M(G.a)).distinct().j0(z.a);
    }

    @Override // j$.util.stream.U
    public final j$.util.j findAny() {
        return (j$.util.j) x0(new d0(false, e4.DOUBLE_VALUE, j$.util.j.a(), W.a, Z.a));
    }

    @Override // j$.util.stream.U
    public final j$.util.j findFirst() {
        return (j$.util.j) x0(new d0(true, e4.DOUBLE_VALUE, j$.util.j.a(), W.a, Z.a));
    }

    @Override // j$.util.stream.U
    public final boolean h0(j$.wrappers.D d) {
        return ((Boolean) x0(o1.u(d, k1.ANY))).booleanValue();
    }

    @Override // j$.util.stream.U
    public final boolean i0(j$.wrappers.D d) {
        return ((Boolean) x0(o1.u(d, k1.NONE))).booleanValue();
    }

    @Override // j$.util.stream.g
    public final j$.util.n iterator() {
        return j$.util.L.f(spliterator());
    }

    @Override // j$.util.stream.g
    public Iterator iterator() {
        return j$.util.L.f(spliterator());
    }

    public void j(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
        x0(new k0(fVar, false));
    }

    public void l0(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
        x0(new k0(fVar, true));
    }

    @Override // j$.util.stream.U
    public final U limit(long j) {
        if (j >= 0) {
            return B3.f(this, 0L, j);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.U
    public final j$.util.j max() {
        return G(D.a);
    }

    @Override // j$.util.stream.U
    public final j$.util.j min() {
        return G(E.a);
    }

    @Override // j$.util.stream.U
    public final U r(j$.wrappers.D d) {
        Objects.requireNonNull(d);
        return new K(this, this, e4.DOUBLE_VALUE, d4.t, d);
    }

    @Override // j$.util.stream.U
    public final U skip(long j) {
        int i = (j > 0L ? 1 : (j == 0L ? 0 : -1));
        if (i >= 0) {
            return i == 0 ? this : B3.f(this, j, -1L);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.U
    public final U sorted() {
        return new J3(this);
    }

    @Override // j$.util.stream.c, j$.util.stream.g
    public final j$.util.t spliterator() {
        return M0(super.spliterator());
    }

    @Override // j$.util.stream.U
    public final double sum() {
        return l.a((double[]) H(y.a, w.a, B.a));
    }

    @Override // j$.util.stream.U
    public final j$.util.g summaryStatistics() {
        return (j$.util.g) H(i.a, u.a, t.a);
    }

    @Override // j$.util.stream.y2
    public final s1 t0(long j, j$.util.function.m mVar) {
        return x2.j(j);
    }

    @Override // j$.util.stream.U
    public final double[] toArray() {
        return (double[]) x2.m((u1) y0(I.a)).e();
    }

    @Override // j$.util.stream.g
    public g unordered() {
        return !C0() ? this : new O(this, this, e4.DOUBLE_VALUE, d4.r);
    }

    @Override // j$.util.stream.U
    public final U w(j$.util.function.g gVar) {
        return new K(this, this, e4.DOUBLE_VALUE, d4.p | d4.n | d4.t, gVar);
    }

    @Override // j$.util.stream.U
    public final e1 x(j$.util.function.h hVar) {
        Objects.requireNonNull(hVar);
        return new N(this, this, e4.DOUBLE_VALUE, d4.p | d4.n, hVar);
    }

    @Override // j$.util.stream.U
    public final U y(j$.wrappers.J j) {
        Objects.requireNonNull(j);
        return new K(this, this, e4.DOUBLE_VALUE, d4.p | d4.n, j);
    }

    @Override // j$.util.stream.c
    final A1 z0(y2 y2Var, j$.util.u uVar, boolean z, j$.util.function.m mVar) {
        return x2.f(y2Var, uVar, z);
    }
}
