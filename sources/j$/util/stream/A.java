package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Supplier;
import java.util.Set;

/* loaded from: classes2.dex */
abstract class A extends b implements D {
    A(j$.util.Q q, int i) {
        super(q, i, false);
    }

    A(b bVar, int i) {
        super(bVar, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static j$.util.E H0(j$.util.Q q) {
        if (q instanceof j$.util.E) {
            return (j$.util.E) q;
        }
        if (!A3.a) {
            throw new UnsupportedOperationException("DoubleStream.adapt(Spliterator<Double> s)");
        }
        A3.a(b.class, "using DoubleStream.adapt(Spliterator<Double> s)");
        throw null;
    }

    @Override // j$.util.stream.D
    public final Object B(Supplier supplier, j$.util.function.i0 i0Var, BiConsumer biConsumer) {
        r rVar = new r(biConsumer, 0);
        supplier.getClass();
        i0Var.getClass();
        return l0(new u1(T2.DOUBLE_VALUE, rVar, i0Var, supplier, 1));
    }

    @Override // j$.util.stream.b
    final j$.util.Q C0(b bVar, Supplier supplier, boolean z) {
        return new d3(bVar, supplier, z);
    }

    @Override // j$.util.stream.D
    public final double F(double d, j$.util.function.j jVar) {
        jVar.getClass();
        return ((Double) l0(new w1(T2.DOUBLE_VALUE, jVar, d))).doubleValue();
    }

    @Override // j$.util.stream.D
    public final Stream I(j$.util.function.q qVar) {
        qVar.getClass();
        return new u(this, S2.p | S2.n, qVar, 0);
    }

    @Override // j$.util.stream.D
    public final D N(j$.util.function.w wVar) {
        wVar.getClass();
        return new t(this, S2.p | S2.n, wVar, 0);
    }

    @Override // j$.util.stream.D
    public final IntStream S(j$.util.function.s sVar) {
        sVar.getClass();
        return new v(this, S2.p | S2.n, sVar, 0);
    }

    @Override // j$.util.stream.D
    public final D U(j$.util.function.r rVar) {
        rVar.getClass();
        return new t(this, S2.t, rVar, 2);
    }

    @Override // j$.util.stream.D
    public final j$.util.l average() {
        double[] dArr = (double[]) B(new l(21), new l(3), new l(4));
        if (dArr[2] <= 0.0d) {
            return j$.util.l.a();
        }
        Set set = Collectors.a;
        double d = dArr[0] + dArr[1];
        double d2 = dArr[dArr.length - 1];
        if (Double.isNaN(d) && Double.isInfinite(d2)) {
            d = d2;
        }
        return j$.util.l.d(d / dArr[2]);
    }

    @Override // j$.util.stream.D
    public final D b(j$.util.function.n nVar) {
        nVar.getClass();
        return new t(this, nVar);
    }

    @Override // j$.util.stream.D
    public final Stream boxed() {
        return I(new l(24));
    }

    @Override // j$.util.stream.D
    public final long count() {
        return ((h0) t(new l(25))).sum();
    }

    @Override // j$.util.stream.D
    public final boolean d0(j$.util.function.r rVar) {
        return ((Boolean) l0(t0.W(rVar, q0.ANY))).booleanValue();
    }

    @Override // j$.util.stream.D
    public final D distinct() {
        return ((W1) ((W1) boxed()).distinct()).c0(new l(26));
    }

    @Override // j$.util.stream.D
    public void f0(j$.util.function.n nVar) {
        nVar.getClass();
        l0(new M(nVar, true));
    }

    @Override // j$.util.stream.D
    public final j$.util.l findAny() {
        return (j$.util.l) l0(new F(false, T2.DOUBLE_VALUE, j$.util.l.a(), new E(1), new l(6)));
    }

    @Override // j$.util.stream.D
    public final j$.util.l findFirst() {
        return (j$.util.l) l0(new F(true, T2.DOUBLE_VALUE, j$.util.l.a(), new E(1), new l(6)));
    }

    @Override // j$.util.stream.D
    public final boolean g0(j$.util.function.r rVar) {
        return ((Boolean) l0(t0.W(rVar, q0.ALL))).booleanValue();
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.D
    public final j$.util.r iterator() {
        return j$.util.f0.f(spliterator());
    }

    @Override // j$.util.stream.D
    public void j(j$.util.function.n nVar) {
        nVar.getClass();
        l0(new M(nVar, false));
    }

    @Override // j$.util.stream.D
    public final boolean k(j$.util.function.r rVar) {
        return ((Boolean) l0(t0.W(rVar, q0.NONE))).booleanValue();
    }

    @Override // j$.util.stream.D
    public final D limit(long j) {
        if (j >= 0) {
            return t0.V(this, 0L, j);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.D
    public final j$.util.l max() {
        return z(new l(28));
    }

    @Override // j$.util.stream.D
    public final j$.util.l min() {
        return z(new l(20));
    }

    @Override // j$.util.stream.b
    final F0 n0(b bVar, j$.util.Q q, boolean z, j$.util.function.I i) {
        return t0.F(bVar, q, z);
    }

    @Override // j$.util.stream.b
    final void p0(j$.util.Q q, e2 e2Var) {
        j$.util.function.n qVar;
        j$.util.E H0 = H0(q);
        if (e2Var instanceof j$.util.function.n) {
            qVar = (j$.util.function.n) e2Var;
        } else {
            if (A3.a) {
                A3.a(b.class, "using DoubleStream.adapt(Sink<Double> s)");
                throw null;
            }
            e2Var.getClass();
            qVar = new q(0, e2Var);
        }
        while (!e2Var.q() && H0.p(qVar)) {
        }
    }

    @Override // j$.util.stream.b
    final T2 q0() {
        return T2.DOUBLE_VALUE;
    }

    @Override // j$.util.stream.D
    public final D s(j$.util.function.q qVar) {
        return new t(this, S2.p | S2.n | S2.t, qVar, 1);
    }

    @Override // j$.util.stream.D
    public final D skip(long j) {
        if (j >= 0) {
            return j == 0 ? this : t0.V(this, j, -1L);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.D
    public final D sorted() {
        return new w2(this);
    }

    @Override // j$.util.stream.b, j$.util.stream.BaseStream, j$.util.stream.D
    public final j$.util.E spliterator() {
        return H0(super.spliterator());
    }

    @Override // j$.util.stream.D
    public final double sum() {
        double[] dArr = (double[]) B(new l(29), new l(1), new l(2));
        Set set = Collectors.a;
        double d = dArr[0] + dArr[1];
        double d2 = dArr[dArr.length - 1];
        return (Double.isNaN(d) && Double.isInfinite(d2)) ? d2 : d;
    }

    @Override // j$.util.stream.D
    public final j$.util.h summaryStatistics() {
        return (j$.util.h) B(new l(13), new l(22), new l(23));
    }

    @Override // j$.util.stream.D
    public final LongStream t(j$.util.function.v vVar) {
        vVar.getClass();
        return new w(this, S2.p | S2.n, vVar, 0);
    }

    @Override // j$.util.stream.D
    public final double[] toArray() {
        return (double[]) t0.O((z0) m0(new l(27))).e();
    }

    @Override // j$.util.stream.BaseStream
    public final BaseStream unordered() {
        return !t0() ? this : new x(this, S2.r, 0);
    }

    @Override // j$.util.stream.b
    final x0 v0(long j, j$.util.function.I i) {
        return t0.J(j);
    }

    @Override // j$.util.stream.D
    public final j$.util.l z(j$.util.function.j jVar) {
        jVar.getClass();
        return (j$.util.l) l0(new y1(T2.DOUBLE_VALUE, jVar, 0));
    }
}
