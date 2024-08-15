package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Supplier;
/* loaded from: classes2.dex */
abstract class d0 extends c implements IntStream {
    /* JADX INFO: Access modifiers changed from: package-private */
    public d0(j$.util.Q q, int i) {
        super(q, i, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public d0(c cVar, int i) {
        super(cVar, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static j$.util.H r1(j$.util.Q q) {
        if (q instanceof j$.util.H) {
            return (j$.util.H) q;
        }
        if (F3.a) {
            F3.a(c.class, "using IntStream.adapt(Spliterator<Integer> s)");
            throw null;
        }
        throw new UnsupportedOperationException("IntStream.adapt(Spliterator<Integer> s)");
    }

    @Override // j$.util.stream.IntStream
    public final Object A(Supplier supplier, j$.util.function.C0 c0, BiConsumer biConsumer) {
        r rVar = new r(biConsumer, 1);
        supplier.getClass();
        c0.getClass();
        return a1(new v1(U2.INT_VALUE, rVar, c0, supplier, 4));
    }

    @Override // j$.util.stream.IntStream
    public final boolean D(j$.util.function.Q q) {
        return ((Boolean) a1(u0.Q0(q, r0.ANY))).booleanValue();
    }

    public void O(j$.util.function.K k) {
        k.getClass();
        a1(new O(k, true));
    }

    @Override // j$.util.stream.IntStream
    public final Stream P(j$.util.function.N n) {
        n.getClass();
        return new v(this, T2.p | T2.n, n, 1);
    }

    @Override // j$.util.stream.IntStream
    public final IntStream S(j$.util.function.N n) {
        return new w(this, T2.p | T2.n | T2.t, n, 3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.u0
    public final y0 T0(long j, j$.util.function.N n) {
        return u1.s(j);
    }

    public void V(j$.util.function.K k) {
        k.getClass();
        a1(new O(k, false));
    }

    @Override // j$.util.stream.IntStream
    public final F W(j$.util.function.U u) {
        u.getClass();
        return new u(this, T2.p | T2.n, u, 4);
    }

    @Override // j$.util.stream.IntStream
    public final IntStream Z(j$.util.function.Q q) {
        q.getClass();
        return new w(this, T2.t, q, 4);
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.m a0(j$.util.function.G g) {
        g.getClass();
        return (j$.util.m) a1(new z1(U2.INT_VALUE, g, 2));
    }

    @Override // j$.util.stream.IntStream
    public final F asDoubleStream() {
        return new y(this, T2.p | T2.n, 1);
    }

    @Override // j$.util.stream.IntStream
    public final LongStream asLongStream() {
        return new Y(this, T2.p | T2.n, 0);
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.l average() {
        long[] jArr = (long[]) A(new b(18), new b(19), new b(20));
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

    @Override // j$.util.stream.IntStream
    public final IntStream b0(j$.util.function.K k) {
        k.getClass();
        return new w(this, 0, k, 1);
    }

    @Override // j$.util.stream.IntStream
    public final Stream boxed() {
        return P(new J0(29));
    }

    @Override // j$.util.stream.c
    final D0 c1(u0 u0Var, j$.util.Q q, boolean z, j$.util.function.N n) {
        return u1.j(u0Var, q, z);
    }

    @Override // j$.util.stream.IntStream
    public final long count() {
        return ((j0) d(new b(17))).sum();
    }

    @Override // j$.util.stream.IntStream
    public final LongStream d(j$.util.function.X x) {
        x.getClass();
        return new x(this, T2.p | T2.n, x, 1);
    }

    @Override // j$.util.stream.c
    final void d1(j$.util.Q q, f2 f2Var) {
        j$.util.function.K v;
        j$.util.H r1 = r1(q);
        if (f2Var instanceof j$.util.function.K) {
            v = (j$.util.function.K) f2Var;
        } else if (F3.a) {
            F3.a(c.class, "using IntStream.adapt(Sink<Integer> s)");
            throw null;
        } else {
            f2Var.getClass();
            v = new V(0, f2Var);
        }
        while (!f2Var.h() && r1.j(v)) {
        }
    }

    @Override // j$.util.stream.IntStream
    public final IntStream distinct() {
        return ((X1) ((X1) boxed()).distinct()).I(new b(16));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public final U2 e1() {
        return U2.INT_VALUE;
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.m findAny() {
        return (j$.util.m) a1(new G(false, U2.INT_VALUE, j$.util.m.a(), new J0(24), new b(14)));
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.m findFirst() {
        return (j$.util.m) a1(new G(true, U2.INT_VALUE, j$.util.m.a(), new J0(24), new b(14)));
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    public final j$.util.v iterator() {
        return j$.util.f0.g(spliterator());
    }

    @Override // j$.util.stream.IntStream
    public final IntStream l(j$.util.function.a0 a0Var) {
        a0Var.getClass();
        return new w(this, T2.p | T2.n, a0Var, 2);
    }

    @Override // j$.util.stream.IntStream
    public final IntStream limit(long j) {
        if (j >= 0) {
            return u0.P0(this, 0L, j);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.m max() {
        return a0(new W(0));
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.m min() {
        return a0(new J0(25));
    }

    @Override // j$.util.stream.c
    final j$.util.Q o1(u0 u0Var, a aVar, boolean z) {
        return new g3(u0Var, aVar, z);
    }

    @Override // j$.util.stream.IntStream
    public final int s(int i, j$.util.function.G g) {
        g.getClass();
        return ((Integer) a1(new H1(U2.INT_VALUE, g, i))).intValue();
    }

    @Override // j$.util.stream.IntStream
    public final IntStream skip(long j) {
        if (j >= 0) {
            return j == 0 ? this : u0.P0(this, j, -1L);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.IntStream
    public final IntStream sorted() {
        return new y2(this);
    }

    @Override // j$.util.stream.c, j$.util.stream.BaseStream, j$.util.stream.F
    public final j$.util.H spliterator() {
        return r1(super.spliterator());
    }

    @Override // j$.util.stream.IntStream
    public final int sum() {
        return s(0, new J0(26));
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.i summaryStatistics() {
        return (j$.util.i) A(new J0(11), new J0(27), new J0(28));
    }

    @Override // j$.util.stream.IntStream
    public final boolean t(j$.util.function.Q q) {
        return ((Boolean) a1(u0.Q0(q, r0.ALL))).booleanValue();
    }

    @Override // j$.util.stream.IntStream
    public final int[] toArray() {
        return (int[]) u1.q((A0) b1(new b(21))).b();
    }

    @Override // j$.util.stream.IntStream
    public final boolean u(j$.util.function.Q q) {
        return ((Boolean) a1(u0.Q0(q, r0.NONE))).booleanValue();
    }

    @Override // j$.util.stream.BaseStream
    public final BaseStream unordered() {
        return !g1() ? this : new Z(this, T2.r);
    }
}
