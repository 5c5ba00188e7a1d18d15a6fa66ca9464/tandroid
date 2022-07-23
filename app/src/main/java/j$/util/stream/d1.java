package j$.util.stream;

import j$.util.function.BiConsumer;
import java.util.Iterator;
/* loaded from: classes2.dex */
public abstract class d1 extends c implements e1 {
    public d1(c cVar, int i) {
        super(cVar, i);
    }

    public d1(j$.util.u uVar, int i, boolean z) {
        super(uVar, i, z);
    }

    public static /* synthetic */ j$.util.v L0(j$.util.u uVar) {
        return M0(uVar);
    }

    public static j$.util.v M0(j$.util.u uVar) {
        if (uVar instanceof j$.util.v) {
            return (j$.util.v) uVar;
        }
        if (!Q4.a) {
            throw new UnsupportedOperationException("LongStream.adapt(Spliterator<Long> s)");
        }
        Q4.a(c.class, "using LongStream.adapt(Spliterator<Long> s)");
        throw null;
    }

    @Override // j$.util.stream.c
    final void A0(j$.util.u uVar, m3 m3Var) {
        j$.util.function.q qVar;
        j$.util.v M0 = M0(uVar);
        if (m3Var instanceof j$.util.function.q) {
            qVar = (j$.util.function.q) m3Var;
        } else if (Q4.a) {
            Q4.a(c.class, "using LongStream.adapt(Sink<Long> s)");
            throw null;
        } else {
            qVar = new W0(m3Var);
        }
        while (!m3Var.o() && M0.i(qVar)) {
        }
    }

    @Override // j$.util.stream.c
    public final e4 B0() {
        return e4.LONG_VALUE;
    }

    @Override // j$.util.stream.e1
    public final long D(long j, j$.util.function.o oVar) {
        oVar.getClass();
        return ((Long) x0(new P2(e4.LONG_VALUE, oVar, j))).longValue();
    }

    @Override // j$.util.stream.c
    final j$.util.u K0(y2 y2Var, j$.util.function.y yVar, boolean z) {
        return new s4(y2Var, yVar, z);
    }

    @Override // j$.util.stream.e1
    public final boolean L(j$.wrappers.j0 j0Var) {
        return ((Boolean) x0(o1.w(j0Var, k1.ALL))).booleanValue();
    }

    @Override // j$.util.stream.e1
    public final U O(j$.wrappers.l0 l0Var) {
        l0Var.getClass();
        return new K(this, this, e4.LONG_VALUE, d4.p | d4.n, l0Var);
    }

    @Override // j$.util.stream.e1
    public final Stream Q(j$.util.function.r rVar) {
        rVar.getClass();
        return new L(this, this, e4.LONG_VALUE, d4.p | d4.n, rVar);
    }

    @Override // j$.util.stream.e1
    public final boolean S(j$.wrappers.j0 j0Var) {
        return ((Boolean) x0(o1.w(j0Var, k1.NONE))).booleanValue();
    }

    public void Z(j$.util.function.q qVar) {
        qVar.getClass();
        x0(new m0(qVar, true));
    }

    @Override // j$.util.stream.e1
    public final U asDoubleStream() {
        return new O(this, this, e4.LONG_VALUE, d4.p | d4.n);
    }

    @Override // j$.util.stream.e1
    public final j$.util.j average() {
        long[] jArr = (long[]) f0(P0.a, O0.a, R0.a);
        if (jArr[0] > 0) {
            double d = jArr[1];
            double d2 = jArr[0];
            Double.isNaN(d);
            Double.isNaN(d2);
            return j$.util.j.d(d / d2);
        }
        return j$.util.j.a();
    }

    @Override // j$.util.stream.e1
    public final Stream boxed() {
        return Q(X0.a);
    }

    @Override // j$.util.stream.e1
    public final long count() {
        return ((d1) z(Y0.a)).sum();
    }

    public void d(j$.util.function.q qVar) {
        qVar.getClass();
        x0(new m0(qVar, false));
    }

    @Override // j$.util.stream.e1
    public final e1 distinct() {
        return ((e3) Q(X0.a)).distinct().g0(Q0.a);
    }

    @Override // j$.util.stream.e1
    public final IntStream e0(j$.wrappers.n0 n0Var) {
        n0Var.getClass();
        return new M(this, this, e4.LONG_VALUE, d4.p | d4.n, n0Var);
    }

    @Override // j$.util.stream.e1
    public final Object f0(j$.util.function.y yVar, j$.util.function.w wVar, BiConsumer biConsumer) {
        C c = new C(biConsumer, 2);
        yVar.getClass();
        wVar.getClass();
        return x0(new z2(e4.LONG_VALUE, c, wVar, yVar));
    }

    @Override // j$.util.stream.e1
    public final j$.util.l findAny() {
        return (j$.util.l) x0(new d0(false, e4.LONG_VALUE, j$.util.l.a(), Y.a, b0.a));
    }

    @Override // j$.util.stream.e1
    public final j$.util.l findFirst() {
        return (j$.util.l) x0(new d0(true, e4.LONG_VALUE, j$.util.l.a(), Y.a, b0.a));
    }

    @Override // j$.util.stream.e1
    public final j$.util.l g(j$.util.function.o oVar) {
        oVar.getClass();
        return (j$.util.l) x0(new D2(e4.LONG_VALUE, oVar));
    }

    @Override // j$.util.stream.g
    /* renamed from: iterator */
    public final j$.util.r mo66iterator() {
        return j$.util.L.h(spliterator());
    }

    @Override // j$.util.stream.g
    /* renamed from: iterator */
    public Iterator mo66iterator() {
        return j$.util.L.h(spliterator());
    }

    @Override // j$.util.stream.e1
    public final boolean k(j$.wrappers.j0 j0Var) {
        return ((Boolean) x0(o1.w(j0Var, k1.ANY))).booleanValue();
    }

    @Override // j$.util.stream.e1
    public final e1 limit(long j) {
        if (j >= 0) {
            return B3.h(this, 0L, j);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.e1
    public final j$.util.l max() {
        return g(U0.a);
    }

    @Override // j$.util.stream.e1
    public final j$.util.l min() {
        return g(V0.a);
    }

    @Override // j$.util.stream.e1
    public final e1 p(j$.util.function.q qVar) {
        qVar.getClass();
        return new N(this, this, e4.LONG_VALUE, 0, qVar);
    }

    @Override // j$.util.stream.e1
    public final e1 s(j$.util.function.r rVar) {
        return new N(this, this, e4.LONG_VALUE, d4.p | d4.n | d4.t, rVar);
    }

    @Override // j$.util.stream.e1
    public final e1 skip(long j) {
        int i = (j > 0L ? 1 : (j == 0L ? 0 : -1));
        if (i >= 0) {
            return i == 0 ? this : B3.h(this, j, -1L);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.e1
    public final e1 sorted() {
        return new L3(this);
    }

    @Override // j$.util.stream.c, j$.util.stream.g
    public final j$.util.v spliterator() {
        return M0(super.spliterator());
    }

    @Override // j$.util.stream.e1
    public final long sum() {
        return ((Long) x0(new P2(e4.LONG_VALUE, T0.a, 0L))).longValue();
    }

    @Override // j$.util.stream.e1
    public final j$.util.i summaryStatistics() {
        return (j$.util.i) f0(k.a, N0.a, M0.a);
    }

    @Override // j$.util.stream.y2
    public final s1 t0(long j, j$.util.function.m mVar) {
        return x2.q(j);
    }

    @Override // j$.util.stream.e1
    public final long[] toArray() {
        return (long[]) x2.o((y1) y0(S0.a)).e();
    }

    @Override // j$.util.stream.e1
    public final e1 u(j$.wrappers.j0 j0Var) {
        j0Var.getClass();
        return new N(this, this, e4.LONG_VALUE, d4.t, j0Var);
    }

    @Override // j$.util.stream.g
    public g unordered() {
        return !C0() ? this : new G0(this, this, e4.LONG_VALUE, d4.r);
    }

    @Override // j$.util.stream.e1
    public final e1 z(j$.util.function.t tVar) {
        tVar.getClass();
        return new N(this, this, e4.LONG_VALUE, d4.p | d4.n, tVar);
    }

    @Override // j$.util.stream.c
    final A1 z0(y2 y2Var, j$.util.u uVar, boolean z, j$.util.function.m mVar) {
        return x2.h(y2Var, uVar, z);
    }
}
