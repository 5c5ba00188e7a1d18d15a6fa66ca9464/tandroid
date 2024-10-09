package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Supplier;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class b0 extends b implements IntStream {
    /* JADX INFO: Access modifiers changed from: package-private */
    public b0(j$.util.Q q, int i) {
        super(q, i, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public b0(b bVar, int i) {
        super(bVar, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static j$.util.H H0(j$.util.Q q) {
        if (q instanceof j$.util.H) {
            return (j$.util.H) q;
        }
        if (!A3.a) {
            throw new UnsupportedOperationException("IntStream.adapt(Spliterator<Integer> s)");
        }
        A3.a(b.class, "using IntStream.adapt(Spliterator<Integer> s)");
        throw null;
    }

    public void C(j$.util.function.F f) {
        f.getClass();
        l0(new N(f, true));
    }

    @Override // j$.util.stream.b
    final j$.util.Q C0(b bVar, Supplier supplier, boolean z) {
        return new f3(bVar, supplier, z);
    }

    @Override // j$.util.stream.IntStream
    public final Stream D(j$.util.function.I i) {
        i.getClass();
        return new u(this, S2.p | S2.n, i, 1);
    }

    @Override // j$.util.stream.IntStream
    public final IntStream H(j$.util.function.O o) {
        o.getClass();
        return new v(this, S2.p | S2.n, o, 2);
    }

    @Override // j$.util.stream.IntStream
    public final int J(int i, j$.util.function.B b) {
        b.getClass();
        return ((Integer) l0(new G1(T2.INT_VALUE, b, i))).intValue();
    }

    @Override // j$.util.stream.IntStream
    public final IntStream K(j$.util.function.I i) {
        return new v(this, S2.p | S2.n | S2.t, i, 3);
    }

    public void L(j$.util.function.F f) {
        f.getClass();
        l0(new N(f, false));
    }

    @Override // j$.util.stream.IntStream
    public final IntStream Q(j$.util.function.J j) {
        j.getClass();
        return new v(this, S2.t, j, 4);
    }

    @Override // j$.util.stream.IntStream
    public final boolean T(j$.util.function.J j) {
        return ((Boolean) l0(t0.Y(j, q0.ALL))).booleanValue();
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.m W(j$.util.function.B b) {
        b.getClass();
        return (j$.util.m) l0(new y1(T2.INT_VALUE, b, 2));
    }

    @Override // j$.util.stream.IntStream
    public final IntStream X(j$.util.function.F f) {
        f.getClass();
        return new v(this, f);
    }

    @Override // j$.util.stream.IntStream
    public final boolean a0(j$.util.function.J j) {
        return ((Boolean) l0(t0.Y(j, q0.ANY))).booleanValue();
    }

    @Override // j$.util.stream.IntStream
    public final D asDoubleStream() {
        return new x(this, S2.p | S2.n, 1);
    }

    @Override // j$.util.stream.IntStream
    public final LongStream asLongStream() {
        return new X(this, S2.p | S2.n, 0);
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.l average() {
        long[] jArr = (long[]) e0(new E(14), new E(15), new E(16));
        long j = jArr[0];
        if (j <= 0) {
            return j$.util.l.a();
        }
        double d = jArr[1];
        double d2 = j;
        Double.isNaN(d);
        Double.isNaN(d2);
        return j$.util.l.d(d / d2);
    }

    @Override // j$.util.stream.IntStream
    public final boolean b0(j$.util.function.J j) {
        return ((Boolean) l0(t0.Y(j, q0.NONE))).booleanValue();
    }

    @Override // j$.util.stream.IntStream
    public final Stream boxed() {
        return D(new E(8));
    }

    @Override // j$.util.stream.IntStream
    public final long count() {
        return ((h0) g(new E(7))).sum();
    }

    @Override // j$.util.stream.IntStream
    public final IntStream distinct() {
        return ((W1) ((W1) boxed()).distinct()).m(new E(6));
    }

    @Override // j$.util.stream.IntStream
    public final D e(j$.util.function.K k) {
        k.getClass();
        return new t(this, S2.p | S2.n, k, 4);
    }

    @Override // j$.util.stream.IntStream
    public final Object e0(Supplier supplier, j$.util.function.l0 l0Var, BiConsumer biConsumer) {
        r rVar = new r(biConsumer, 1);
        supplier.getClass();
        l0Var.getClass();
        return l0(new u1(T2.INT_VALUE, rVar, l0Var, supplier, 4));
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.m findAny() {
        return (j$.util.m) l0(new F(false, T2.INT_VALUE, j$.util.m.a(), new E(3), new l(8)));
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.m findFirst() {
        return (j$.util.m) l0(new F(true, T2.INT_VALUE, j$.util.m.a(), new E(3), new l(8)));
    }

    @Override // j$.util.stream.IntStream
    public final LongStream g(j$.util.function.N n) {
        n.getClass();
        return new w(this, S2.p | S2.n, n, 1);
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.D
    public final j$.util.v iterator() {
        return j$.util.f0.g(spliterator());
    }

    @Override // j$.util.stream.IntStream
    public final IntStream limit(long j) {
        if (j >= 0) {
            return t0.X(this, 0L, j);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.m max() {
        return W(new E(13));
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.m min() {
        return W(new E(9));
    }

    @Override // j$.util.stream.b
    final F0 n0(b bVar, j$.util.Q q, boolean z, j$.util.function.I i) {
        return t0.G(bVar, q, z);
    }

    @Override // j$.util.stream.b
    final void p0(j$.util.Q q, e2 e2Var) {
        j$.util.function.F u;
        j$.util.H H0 = H0(q);
        if (e2Var instanceof j$.util.function.F) {
            u = (j$.util.function.F) e2Var;
        } else {
            if (A3.a) {
                A3.a(b.class, "using IntStream.adapt(Sink<Integer> s)");
                throw null;
            }
            e2Var.getClass();
            u = new U(0, e2Var);
        }
        while (!e2Var.q() && H0.p(u)) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.b
    public final T2 q0() {
        return T2.INT_VALUE;
    }

    @Override // j$.util.stream.IntStream
    public final IntStream skip(long j) {
        if (j >= 0) {
            return j == 0 ? this : t0.X(this, j, -1L);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.IntStream
    public final IntStream sorted() {
        return new x2(this);
    }

    @Override // j$.util.stream.b, j$.util.stream.BaseStream, j$.util.stream.D
    public final j$.util.H spliterator() {
        return H0(super.spliterator());
    }

    @Override // j$.util.stream.IntStream
    public final int sum() {
        return J(0, new E(12));
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.i summaryStatistics() {
        return (j$.util.i) e0(new l(15), new E(10), new E(11));
    }

    @Override // j$.util.stream.IntStream
    public final int[] toArray() {
        return (int[]) t0.P((B0) m0(new E(5))).e();
    }

    @Override // j$.util.stream.BaseStream
    public final BaseStream unordered() {
        return !t0() ? this : new Y(this, S2.r);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.b
    public final x0 v0(long j, j$.util.function.I i) {
        return t0.R(j);
    }
}
