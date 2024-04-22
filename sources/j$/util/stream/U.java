package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Supplier;
import j$.util.function.ToDoubleFunction;
import j$.util.s;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes2.dex */
public abstract class U extends c implements V {
    public U(j$.util.s sVar, int i, boolean z) {
        super(sVar, i, z);
    }

    public U(c cVar, int i) {
        super(cVar, i);
    }

    public static /* synthetic */ s.a G0(j$.util.s sVar) {
        return H0(sVar);
    }

    public static s.a H0(j$.util.s sVar) {
        if (sVar instanceof s.a) {
            return (s.a) sVar;
        }
        if (Q4.a) {
            Q4.a(c.class, "using DoubleStream.adapt(Spliterator<Double> s)");
            throw null;
        }
        throw new UnsupportedOperationException("DoubleStream.adapt(Spliterator<Double> s)");
    }

    @Override // j$.util.stream.V
    public final j$.util.j F(j$.util.function.d dVar) {
        Objects.requireNonNull(dVar);
        return (j$.util.j) s0(new D2(e4.DOUBLE_VALUE, dVar));
    }

    @Override // j$.util.stream.c
    final j$.util.s F0(y2 y2Var, Supplier supplier, boolean z) {
        return new o4(y2Var, supplier, z);
    }

    @Override // j$.util.stream.V
    public final Object G(Supplier supplier, j$.util.function.t tVar, BiConsumer biConsumer) {
        D d = new D(biConsumer, 0);
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(tVar);
        return s0(new z2(e4.DOUBLE_VALUE, d, tVar, supplier));
    }

    @Override // j$.util.stream.V
    public final double J(double d, j$.util.function.d dVar) {
        Objects.requireNonNull(dVar);
        return ((Double) s0(new B2(e4.DOUBLE_VALUE, dVar, d))).doubleValue();
    }

    @Override // j$.util.stream.V
    public final Stream L(j$.util.function.g gVar) {
        Objects.requireNonNull(gVar);
        return new M(this, this, e4.DOUBLE_VALUE, d4.p | d4.n, gVar);
    }

    @Override // j$.util.stream.V
    public final IntStream P(j$.wrappers.F f) {
        Objects.requireNonNull(f);
        return new N(this, this, e4.DOUBLE_VALUE, d4.p | d4.n, f);
    }

    @Override // j$.util.stream.V
    public final boolean V(j$.wrappers.D d) {
        return ((Boolean) s0(o1.u(d, k1.ALL))).booleanValue();
    }

    @Override // j$.util.stream.V
    public final j$.util.j average() {
        double[] dArr = (double[]) G(new Supplier() { // from class: j$.util.stream.y
            @Override // j$.util.function.Supplier
            public final Object get() {
                return new double[4];
            }
        }, new j$.util.function.t() { // from class: j$.util.stream.w
            @Override // j$.util.function.t
            public final void accept(Object obj, double d) {
                double[] dArr2 = (double[]) obj;
                dArr2[2] = dArr2[2] + 1.0d;
                Collectors.b(dArr2, d);
                dArr2[3] = dArr2[3] + d;
            }
        }, new BiConsumer() { // from class: j$.util.stream.B
            @Override // j$.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                double[] dArr2 = (double[]) obj;
                double[] dArr3 = (double[]) obj2;
                Collectors.b(dArr2, dArr3[0]);
                Collectors.b(dArr2, dArr3[1]);
                dArr2[2] = dArr2[2] + dArr3[2];
                dArr2[3] = dArr2[3] + dArr3[3];
            }

            @Override // j$.util.function.BiConsumer
            public /* synthetic */ BiConsumer andThen(BiConsumer biConsumer) {
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
            }
        });
        return dArr[2] > 0.0d ? j$.util.j.d(Collectors.a(dArr) / dArr[2]) : j$.util.j.a();
    }

    @Override // j$.util.stream.V
    public final V b(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
        return new L(this, this, e4.DOUBLE_VALUE, 0, fVar);
    }

    @Override // j$.util.stream.V
    public final Stream boxed() {
        return L(H.a);
    }

    @Override // j$.util.stream.V
    public final boolean c0(j$.wrappers.D d) {
        return ((Boolean) s0(o1.u(d, k1.ANY))).booleanValue();
    }

    @Override // j$.util.stream.V
    public final long count() {
        return ((e1) w(new j$.util.function.h() { // from class: j$.util.stream.I
            @Override // j$.util.function.h
            public final long applyAsLong(double d) {
                return 1L;
            }
        })).sum();
    }

    @Override // j$.util.stream.V
    public final boolean d0(j$.wrappers.D d) {
        return ((Boolean) s0(o1.u(d, k1.NONE))).booleanValue();
    }

    @Override // j$.util.stream.V
    public final V distinct() {
        return ((e3) L(H.a)).distinct().e0(new ToDoubleFunction() { // from class: j$.util.stream.A
            @Override // j$.util.function.ToDoubleFunction
            public final double applyAsDouble(Object obj) {
                return ((Double) obj).doubleValue();
            }
        });
    }

    @Override // j$.util.stream.V
    public final j$.util.j findAny() {
        return (j$.util.j) s0(new e0(false, e4.DOUBLE_VALUE, j$.util.j.a(), X.a, a0.a));
    }

    @Override // j$.util.stream.V
    public final j$.util.j findFirst() {
        return (j$.util.j) s0(new e0(true, e4.DOUBLE_VALUE, j$.util.j.a(), X.a, a0.a));
    }

    public void g0(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
        s0(new l0(fVar, true));
    }

    @Override // j$.util.stream.g
    public final j$.util.n iterator() {
        return j$.util.I.f(spliterator());
    }

    @Override // j$.util.stream.g
    public Iterator iterator() {
        return j$.util.I.f(spliterator());
    }

    public void j(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
        s0(new l0(fVar, false));
    }

    @Override // j$.util.stream.V
    public final V limit(long j) {
        if (j >= 0) {
            return B3.f(this, 0L, j);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.V
    public final j$.util.j max() {
        return F(new j$.util.function.d() { // from class: j$.util.stream.E
            @Override // j$.util.function.d
            public final double applyAsDouble(double d, double d2) {
                return Math.max(d, d2);
            }
        });
    }

    @Override // j$.util.stream.V
    public final j$.util.j min() {
        return F(new j$.util.function.d() { // from class: j$.util.stream.F
            @Override // j$.util.function.d
            public final double applyAsDouble(double d, double d2) {
                return Math.min(d, d2);
            }
        });
    }

    @Override // j$.util.stream.y2
    public final s1 o0(long j, j$.util.function.m mVar) {
        return x2.j(j);
    }

    @Override // j$.util.stream.V
    public final V q(j$.wrappers.D d) {
        Objects.requireNonNull(d);
        return new L(this, this, e4.DOUBLE_VALUE, d4.t, d);
    }

    @Override // j$.util.stream.V
    public final V skip(long j) {
        int i = (j > 0L ? 1 : (j == 0L ? 0 : -1));
        if (i >= 0) {
            return i == 0 ? this : B3.f(this, j, -1L);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.V
    public final V sorted() {
        return new J3(this);
    }

    @Override // j$.util.stream.c, j$.util.stream.g
    public final s.a spliterator() {
        return H0(super.spliterator());
    }

    @Override // j$.util.stream.V
    public final double sum() {
        return Collectors.a((double[]) G(new Supplier() { // from class: j$.util.stream.z
            @Override // j$.util.function.Supplier
            public final Object get() {
                return new double[3];
            }
        }, new j$.util.function.t() { // from class: j$.util.stream.x
            @Override // j$.util.function.t
            public final void accept(Object obj, double d) {
                double[] dArr = (double[]) obj;
                Collectors.b(dArr, d);
                dArr[2] = dArr[2] + d;
            }
        }, new BiConsumer() { // from class: j$.util.stream.C
            @Override // j$.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                double[] dArr = (double[]) obj;
                double[] dArr2 = (double[]) obj2;
                Collectors.b(dArr, dArr2[0]);
                Collectors.b(dArr, dArr2[1]);
                dArr[2] = dArr[2] + dArr2[2];
            }

            @Override // j$.util.function.BiConsumer
            public /* synthetic */ BiConsumer andThen(BiConsumer biConsumer) {
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
            }
        }));
    }

    @Override // j$.util.stream.V
    public final j$.util.g summaryStatistics() {
        return (j$.util.g) G(new Supplier() { // from class: j$.util.stream.k
            @Override // j$.util.function.Supplier
            public final Object get() {
                return new j$.util.g();
            }
        }, new j$.util.function.t() { // from class: j$.util.stream.v
            @Override // j$.util.function.t
            public final void accept(Object obj, double d) {
                ((j$.util.g) obj).accept(d);
            }
        }, new BiConsumer() { // from class: j$.util.stream.u
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

    @Override // j$.util.stream.V
    public final double[] toArray() {
        return (double[]) x2.m((u1) t0(new j$.util.function.m() { // from class: j$.util.stream.J
            @Override // j$.util.function.m
            public final Object apply(int i) {
                return new Double[i];
            }
        })).e();
    }

    @Override // j$.util.stream.c
    final A1 u0(y2 y2Var, j$.util.s sVar, boolean z, j$.util.function.m mVar) {
        return x2.f(y2Var, sVar, z);
    }

    @Override // j$.util.stream.g
    public g unordered() {
        return !x0() ? this : new P(this, this, e4.DOUBLE_VALUE, d4.r);
    }

    @Override // j$.util.stream.V
    public final V v(j$.util.function.g gVar) {
        return new L(this, this, e4.DOUBLE_VALUE, d4.p | d4.n | d4.t, gVar);
    }

    @Override // j$.util.stream.c
    final void v0(j$.util.s sVar, m3 m3Var) {
        j$.util.function.f g;
        s.a H0 = H0(sVar);
        if (m3Var instanceof j$.util.function.f) {
            g = (j$.util.function.f) m3Var;
        } else if (Q4.a) {
            Q4.a(c.class, "using DoubleStream.adapt(Sink<Double> s)");
            throw null;
        } else {
            g = new G(m3Var);
        }
        while (!m3Var.o() && H0.k(g)) {
        }
    }

    @Override // j$.util.stream.V
    public final LongStream w(j$.util.function.h hVar) {
        Objects.requireNonNull(hVar);
        return new O(this, this, e4.DOUBLE_VALUE, d4.p | d4.n, hVar);
    }

    @Override // j$.util.stream.c
    public final e4 w0() {
        return e4.DOUBLE_VALUE;
    }

    @Override // j$.util.stream.V
    public final V x(j$.wrappers.J j) {
        Objects.requireNonNull(j);
        return new L(this, this, e4.DOUBLE_VALUE, d4.p | d4.n, j);
    }
}
