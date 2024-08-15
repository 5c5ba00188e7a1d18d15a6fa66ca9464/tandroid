package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Supplier;
import java.util.Set;
/* loaded from: classes2.dex */
abstract class C extends c implements F {
    /* JADX INFO: Access modifiers changed from: package-private */
    public C(j$.util.Q q, int i) {
        super(q, i, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public C(c cVar, int i) {
        super(cVar, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static j$.util.E r1(j$.util.Q q) {
        if (q instanceof j$.util.E) {
            return (j$.util.E) q;
        }
        if (F3.a) {
            F3.a(c.class, "using DoubleStream.adapt(Spliterator<Double> s)");
            throw null;
        }
        throw new UnsupportedOperationException("DoubleStream.adapt(Spliterator<Double> s)");
    }

    @Override // j$.util.stream.F
    public final IntStream B(j$.util.function.v vVar) {
        vVar.getClass();
        return new w(this, T2.p | T2.n, vVar, 0);
    }

    @Override // j$.util.stream.F
    public void H(j$.util.function.m mVar) {
        mVar.getClass();
        a1(new N(mVar, false));
    }

    @Override // j$.util.stream.F
    public final j$.util.l N(j$.util.function.i iVar) {
        iVar.getClass();
        return (j$.util.l) a1(new z1(U2.DOUBLE_VALUE, iVar, 0));
    }

    @Override // j$.util.stream.F
    public final double Q(double d, j$.util.function.i iVar) {
        iVar.getClass();
        return ((Double) a1(new x1(U2.DOUBLE_VALUE, iVar, d))).doubleValue();
    }

    @Override // j$.util.stream.F
    public final boolean R(j$.util.function.s sVar) {
        return ((Boolean) a1(u0.O0(sVar, r0.NONE))).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.u0
    public final y0 T0(long j, j$.util.function.N n) {
        return u1.m(j);
    }

    @Override // j$.util.stream.F
    public final boolean U(j$.util.function.s sVar) {
        return ((Boolean) a1(u0.O0(sVar, r0.ALL))).booleanValue();
    }

    @Override // j$.util.stream.F
    public final j$.util.l average() {
        double[] dArr = (double[]) p(new b(6), new b(7), new b(8));
        if (dArr[2] > 0.0d) {
            Set set = Collectors.a;
            double d = dArr[0] + dArr[1];
            double d2 = dArr[dArr.length - 1];
            if (Double.isNaN(d) && Double.isInfinite(d2)) {
                d = d2;
            }
            return j$.util.l.d(d / dArr[2]);
        }
        return j$.util.l.a();
    }

    @Override // j$.util.stream.F
    public final F b(j$.util.function.m mVar) {
        mVar.getClass();
        return new u(this, 0, mVar, 3);
    }

    @Override // j$.util.stream.F
    public final Stream boxed() {
        return r(new J0(18));
    }

    @Override // j$.util.stream.c
    final D0 c1(u0 u0Var, j$.util.Q q, boolean z, j$.util.function.N n) {
        return u1.i(u0Var, q, z);
    }

    @Override // j$.util.stream.F
    public final long count() {
        return ((j0) k(new b(5))).sum();
    }

    @Override // j$.util.stream.c
    final void d1(j$.util.Q q, f2 f2Var) {
        j$.util.function.m sVar;
        j$.util.E r1 = r1(q);
        if (f2Var instanceof j$.util.function.m) {
            sVar = (j$.util.function.m) f2Var;
        } else if (F3.a) {
            F3.a(c.class, "using DoubleStream.adapt(Sink<Double> s)");
            throw null;
        } else {
            f2Var.getClass();
            sVar = new s(0, f2Var);
        }
        while (!f2Var.h() && r1.o(sVar)) {
        }
    }

    @Override // j$.util.stream.F
    public final F distinct() {
        return ((X1) ((X1) boxed()).distinct()).f0(new b(9));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public final U2 e1() {
        return U2.DOUBLE_VALUE;
    }

    @Override // j$.util.stream.F
    public final j$.util.l findAny() {
        return (j$.util.l) a1(new G(false, U2.DOUBLE_VALUE, j$.util.l.a(), new J0(21), new b(11)));
    }

    @Override // j$.util.stream.F
    public final j$.util.l findFirst() {
        return (j$.util.l) a1(new G(true, U2.DOUBLE_VALUE, j$.util.l.a(), new J0(21), new b(11)));
    }

    @Override // j$.util.stream.F
    public void g0(j$.util.function.m mVar) {
        mVar.getClass();
        a1(new N(mVar, true));
    }

    @Override // j$.util.stream.F
    public final F i(j$.util.function.s sVar) {
        sVar.getClass();
        return new u(this, T2.t, sVar, 2);
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    public final j$.util.r iterator() {
        return j$.util.f0.f(spliterator());
    }

    @Override // j$.util.stream.F
    public final F j(j$.util.function.p pVar) {
        return new u(this, T2.p | T2.n | T2.t, pVar, 1);
    }

    @Override // j$.util.stream.F
    public final LongStream k(j$.util.function.y yVar) {
        yVar.getClass();
        return new x(this, T2.p | T2.n, yVar, 0);
    }

    @Override // j$.util.stream.F
    public final F limit(long j) {
        if (j >= 0) {
            return u0.N0(this, 0L, j);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.F
    public final j$.util.l max() {
        return N(new J0(17));
    }

    @Override // j$.util.stream.F
    public final j$.util.l min() {
        return N(new J0(16));
    }

    @Override // j$.util.stream.c
    final j$.util.Q o1(u0 u0Var, a aVar, boolean z) {
        return new e3(u0Var, aVar, z);
    }

    @Override // j$.util.stream.F
    public final Object p(Supplier supplier, j$.util.function.z0 z0Var, BiConsumer biConsumer) {
        r rVar = new r(biConsumer, 0);
        supplier.getClass();
        z0Var.getClass();
        return a1(new v1(U2.DOUBLE_VALUE, rVar, z0Var, supplier, 1));
    }

    @Override // j$.util.stream.F
    public final F q(j$.util.function.B b) {
        b.getClass();
        return new u(this, T2.p | T2.n, b, 0);
    }

    @Override // j$.util.stream.F
    public final Stream r(j$.util.function.p pVar) {
        pVar.getClass();
        return new v(this, T2.p | T2.n, pVar, 0);
    }

    @Override // j$.util.stream.F
    public final F skip(long j) {
        if (j >= 0) {
            return j == 0 ? this : u0.N0(this, j, -1L);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.F
    public final F sorted() {
        return new x2(this);
    }

    @Override // j$.util.stream.c, j$.util.stream.BaseStream, j$.util.stream.F
    public final j$.util.E spliterator() {
        return r1(super.spliterator());
    }

    @Override // j$.util.stream.F
    public final double sum() {
        double[] dArr = (double[]) p(new b(10), new b(3), new b(4));
        Set set = Collectors.a;
        double d = dArr[0] + dArr[1];
        double d2 = dArr[dArr.length - 1];
        return (Double.isNaN(d) && Double.isInfinite(d2)) ? d2 : d;
    }

    @Override // j$.util.stream.F
    public final j$.util.h summaryStatistics() {
        return (j$.util.h) p(new J0(10), new J0(19), new J0(20));
    }

    @Override // j$.util.stream.F
    public final double[] toArray() {
        return (double[]) u1.p((z0) b1(new b(2))).b();
    }

    @Override // j$.util.stream.BaseStream
    public final BaseStream unordered() {
        return !g1() ? this : new y(this, T2.r, 0);
    }

    @Override // j$.util.stream.F
    public final boolean w(j$.util.function.s sVar) {
        return ((Boolean) a1(u0.O0(sVar, r0.ANY))).booleanValue();
    }
}
