package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.ToDoubleFunction;
import j$.util.t;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes2.dex */
public abstract class T extends c implements U {
    public T(c cVar, int i) {
        super(cVar, i);
    }

    public T(j$.util.t tVar, int i, boolean z) {
        super(tVar, i, z);
    }

    public static /* synthetic */ t.a L0(j$.util.t tVar) {
        return M0(tVar);
    }

    public static t.a M0(j$.util.t tVar) {
        if (tVar instanceof t.a) {
            return (t.a) tVar;
        }
        if (Q4.a) {
            Q4.a(c.class, "using DoubleStream.adapt(Spliterator<Double> s)");
            throw null;
        }
        throw new UnsupportedOperationException("DoubleStream.adapt(Spliterator<Double> s)");
    }

    @Override // j$.util.stream.c
    final void A0(j$.util.t tVar, m3 m3Var) {
        j$.util.function.f f;
        t.a M0 = M0(tVar);
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
    final j$.util.t K0(y2 y2Var, j$.util.function.y yVar, boolean z) {
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
        double[] dArr = (double[]) H(new j$.util.function.y() { // from class: j$.util.stream.x
            @Override // j$.util.function.y
            public final Object get() {
                return new double[4];
            }
        }, new j$.util.function.u() { // from class: j$.util.stream.v
            @Override // j$.util.function.u
            public final void accept(Object obj, double d) {
                double[] dArr2 = (double[]) obj;
                dArr2[2] = dArr2[2] + 1.0d;
                l.b(dArr2, d);
                dArr2[3] = dArr2[3] + d;
            }
        }, new BiConsumer() { // from class: j$.util.stream.A
            @Override // j$.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                double[] dArr2 = (double[]) obj;
                double[] dArr3 = (double[]) obj2;
                l.b(dArr2, dArr3[0]);
                l.b(dArr2, dArr3[1]);
                dArr2[2] = dArr2[2] + dArr3[2];
                dArr2[3] = dArr2[3] + dArr3[3];
            }

            @Override // j$.util.function.BiConsumer
            public /* synthetic */ BiConsumer andThen(BiConsumer biConsumer) {
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
            }
        });
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
        return ((d1) x(new j$.util.function.h() { // from class: j$.util.stream.H
            @Override // j$.util.function.h
            public final long applyAsLong(double d) {
                return 1L;
            }
        })).sum();
    }

    @Override // j$.util.stream.U
    public final U distinct() {
        return ((e3) M(G.a)).distinct().j0(new ToDoubleFunction() { // from class: j$.util.stream.z
            @Override // j$.util.function.ToDoubleFunction
            public final double applyAsDouble(Object obj) {
                return ((Double) obj).doubleValue();
            }
        });
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
        return j$.util.J.f(spliterator());
    }

    @Override // j$.util.stream.g
    public Iterator iterator() {
        return j$.util.J.f(spliterator());
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
        return G(new j$.util.function.d() { // from class: j$.util.stream.D
            @Override // j$.util.function.d
            public final double applyAsDouble(double d, double d2) {
                return Math.max(d, d2);
            }
        });
    }

    @Override // j$.util.stream.U
    public final j$.util.j min() {
        return G(new j$.util.function.d() { // from class: j$.util.stream.E
            @Override // j$.util.function.d
            public final double applyAsDouble(double d, double d2) {
                return Math.min(d, d2);
            }
        });
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
    public final t.a spliterator() {
        return M0(super.spliterator());
    }

    @Override // j$.util.stream.U
    public final double sum() {
        return l.a((double[]) H(new j$.util.function.y() { // from class: j$.util.stream.y
            @Override // j$.util.function.y
            public final Object get() {
                return new double[3];
            }
        }, new j$.util.function.u() { // from class: j$.util.stream.w
            @Override // j$.util.function.u
            public final void accept(Object obj, double d) {
                double[] dArr = (double[]) obj;
                l.b(dArr, d);
                dArr[2] = dArr[2] + d;
            }
        }, new BiConsumer() { // from class: j$.util.stream.B
            @Override // j$.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                double[] dArr = (double[]) obj;
                double[] dArr2 = (double[]) obj2;
                l.b(dArr, dArr2[0]);
                l.b(dArr, dArr2[1]);
                dArr[2] = dArr[2] + dArr2[2];
            }

            @Override // j$.util.function.BiConsumer
            public /* synthetic */ BiConsumer andThen(BiConsumer biConsumer) {
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
            }
        }));
    }

    @Override // j$.util.stream.U
    public final j$.util.g summaryStatistics() {
        return (j$.util.g) H(new j$.util.function.y() { // from class: j$.util.stream.i
            @Override // j$.util.function.y
            public final Object get() {
                return new j$.util.g();
            }
        }, new j$.util.function.u() { // from class: j$.util.stream.u
            @Override // j$.util.function.u
            public final void accept(Object obj, double d) {
                ((j$.util.g) obj).accept(d);
            }
        }, new BiConsumer() { // from class: j$.util.stream.t
            @Override // j$.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((j$.util.g) obj).b((j$.util.g) obj2);
            }

            @Override // j$.util.function.BiConsumer
            public /* synthetic */ BiConsumer andThen(BiConsumer biConsumer) {
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
            }
        });
    }

    @Override // j$.util.stream.y2
    public final s1 t0(long j, j$.util.function.m mVar) {
        return x2.j(j);
    }

    @Override // j$.util.stream.U
    public final double[] toArray() {
        return (double[]) x2.m((u1) y0(new j$.util.function.m() { // from class: j$.util.stream.I
            @Override // j$.util.function.m
            public final Object apply(int i) {
                return new Double[i];
            }
        })).e();
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
    final A1 z0(y2 y2Var, j$.util.t tVar, boolean z, j$.util.function.m mVar) {
        return x2.f(y2Var, tVar, z);
    }
}
