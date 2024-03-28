package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Supplier;
import j$.util.function.ToDoubleFunction;
import j$.util.t;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes2.dex */
public abstract class U extends c implements V {
    public U(c cVar, int i) {
        super(cVar, i);
    }

    public U(j$.util.t tVar, int i, boolean z) {
        super(tVar, i, z);
    }

    public static /* synthetic */ t.a J0(j$.util.t tVar) {
        return K0(tVar);
    }

    public static t.a K0(j$.util.t tVar) {
        if (tVar instanceof t.a) {
            return (t.a) tVar;
        }
        if (R4.a) {
            R4.a(c.class, "using DoubleStream.adapt(Spliterator<Double> s)");
            throw null;
        }
        throw new UnsupportedOperationException("DoubleStream.adapt(Spliterator<Double> s)");
    }

    @Override // j$.util.stream.V
    public final j$.util.j G(j$.util.function.d dVar) {
        Objects.requireNonNull(dVar);
        return (j$.util.j) v0(new E2(f4.DOUBLE_VALUE, dVar));
    }

    @Override // j$.util.stream.V
    public final Object H(Supplier supplier, j$.util.function.u uVar, BiConsumer biConsumer) {
        D d = new D(biConsumer, 0);
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(uVar);
        return v0(new A2(f4.DOUBLE_VALUE, d, uVar, supplier));
    }

    @Override // j$.util.stream.c
    final j$.util.t I0(z2 z2Var, Supplier supplier, boolean z) {
        return new p4(z2Var, supplier, z);
    }

    @Override // j$.util.stream.V
    public final double K(double d, j$.util.function.d dVar) {
        Objects.requireNonNull(dVar);
        return ((Double) v0(new C2(f4.DOUBLE_VALUE, dVar, d))).doubleValue();
    }

    @Override // j$.util.stream.V
    public final Stream M(j$.util.function.g gVar) {
        Objects.requireNonNull(gVar);
        return new M(this, this, f4.DOUBLE_VALUE, e4.p | e4.n, gVar);
    }

    @Override // j$.util.stream.V
    public final IntStream R(j$.wrappers.F f) {
        Objects.requireNonNull(f);
        return new N(this, this, f4.DOUBLE_VALUE, e4.p | e4.n, f);
    }

    @Override // j$.util.stream.V
    public final boolean X(j$.wrappers.D d) {
        return ((Boolean) v0(p1.u(d, l1.ALL))).booleanValue();
    }

    @Override // j$.util.stream.V
    public final j$.util.j average() {
        double[] dArr = (double[]) H(new Supplier() { // from class: j$.util.stream.y
            @Override // j$.util.function.Supplier
            public final Object get() {
                return new double[4];
            }
        }, new j$.util.function.u() { // from class: j$.util.stream.w
            @Override // j$.util.function.u
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
        return new L(this, this, f4.DOUBLE_VALUE, 0, fVar);
    }

    @Override // j$.util.stream.V
    public final Stream boxed() {
        return M(H.a);
    }

    @Override // j$.util.stream.V
    public final long count() {
        return ((e1) x(new j$.util.function.h() { // from class: j$.util.stream.I
            @Override // j$.util.function.h
            public final long applyAsLong(double d) {
                return 1L;
            }
        })).sum();
    }

    @Override // j$.util.stream.V
    public final V distinct() {
        return ((f3) M(H.a)).distinct().h0(new ToDoubleFunction() { // from class: j$.util.stream.A
            @Override // j$.util.function.ToDoubleFunction
            public final double applyAsDouble(Object obj) {
                return ((Double) obj).doubleValue();
            }
        });
    }

    @Override // j$.util.stream.V
    public final boolean f0(j$.wrappers.D d) {
        return ((Boolean) v0(p1.u(d, l1.ANY))).booleanValue();
    }

    @Override // j$.util.stream.V
    public final j$.util.j findAny() {
        return (j$.util.j) v0(new e0(false, f4.DOUBLE_VALUE, j$.util.j.a(), X.a, a0.a));
    }

    @Override // j$.util.stream.V
    public final j$.util.j findFirst() {
        return (j$.util.j) v0(new e0(true, f4.DOUBLE_VALUE, j$.util.j.a(), X.a, a0.a));
    }

    @Override // j$.util.stream.V
    public final boolean g0(j$.wrappers.D d) {
        return ((Boolean) v0(p1.u(d, l1.NONE))).booleanValue();
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
        v0(new l0(fVar, false));
    }

    public void j0(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
        v0(new l0(fVar, true));
    }

    @Override // j$.util.stream.V
    public final V limit(long j) {
        if (j >= 0) {
            return C3.f(this, 0L, j);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.V
    public final j$.util.j max() {
        return G(new j$.util.function.d() { // from class: j$.util.stream.E
            @Override // j$.util.function.d
            public final double applyAsDouble(double d, double d2) {
                return Math.max(d, d2);
            }
        });
    }

    @Override // j$.util.stream.V
    public final j$.util.j min() {
        return G(new j$.util.function.d() { // from class: j$.util.stream.F
            @Override // j$.util.function.d
            public final double applyAsDouble(double d, double d2) {
                return Math.min(d, d2);
            }
        });
    }

    @Override // j$.util.stream.V
    public final V r(j$.wrappers.D d) {
        Objects.requireNonNull(d);
        return new L(this, this, f4.DOUBLE_VALUE, e4.t, d);
    }

    @Override // j$.util.stream.z2
    public final t1 r0(long j, j$.util.function.m mVar) {
        return y2.j(j);
    }

    @Override // j$.util.stream.V
    public final V skip(long j) {
        int i = (j > 0L ? 1 : (j == 0L ? 0 : -1));
        if (i >= 0) {
            return i == 0 ? this : C3.f(this, j, -1L);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.V
    public final V sorted() {
        return new K3(this);
    }

    @Override // j$.util.stream.c, j$.util.stream.g
    public final t.a spliterator() {
        return K0(super.spliterator());
    }

    @Override // j$.util.stream.V
    public final double sum() {
        return Collectors.a((double[]) H(new Supplier() { // from class: j$.util.stream.z
            @Override // j$.util.function.Supplier
            public final Object get() {
                return new double[3];
            }
        }, new j$.util.function.u() { // from class: j$.util.stream.x
            @Override // j$.util.function.u
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
        return (j$.util.g) H(new Supplier() { // from class: j$.util.stream.k
            @Override // j$.util.function.Supplier
            public final Object get() {
                return new j$.util.g();
            }
        }, new j$.util.function.u() { // from class: j$.util.stream.v
            @Override // j$.util.function.u
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
        return (double[]) y2.m((v1) w0(new j$.util.function.m() { // from class: j$.util.stream.J
            @Override // j$.util.function.m
            public final Object apply(int i) {
                return new Double[i];
            }
        })).e();
    }

    @Override // j$.util.stream.g
    public g unordered() {
        return !A0() ? this : new P(this, this, f4.DOUBLE_VALUE, e4.r);
    }

    @Override // j$.util.stream.V
    public final V w(j$.util.function.g gVar) {
        return new L(this, this, f4.DOUBLE_VALUE, e4.p | e4.n | e4.t, gVar);
    }

    @Override // j$.util.stream.V
    public final f1 x(j$.util.function.h hVar) {
        Objects.requireNonNull(hVar);
        return new O(this, this, f4.DOUBLE_VALUE, e4.p | e4.n, hVar);
    }

    @Override // j$.util.stream.c
    final B1 x0(z2 z2Var, j$.util.t tVar, boolean z, j$.util.function.m mVar) {
        return y2.f(z2Var, tVar, z);
    }

    @Override // j$.util.stream.V
    public final V y(j$.wrappers.J j) {
        Objects.requireNonNull(j);
        return new L(this, this, f4.DOUBLE_VALUE, e4.p | e4.n, j);
    }

    @Override // j$.util.stream.c
    final void y0(j$.util.t tVar, n3 n3Var) {
        j$.util.function.f g;
        t.a K0 = K0(tVar);
        if (n3Var instanceof j$.util.function.f) {
            g = (j$.util.function.f) n3Var;
        } else if (R4.a) {
            R4.a(c.class, "using DoubleStream.adapt(Sink<Double> s)");
            throw null;
        } else {
            g = new G(n3Var);
        }
        while (!n3Var.o() && K0.k(g)) {
        }
    }

    @Override // j$.util.stream.c
    public final f4 z0() {
        return f4.DOUBLE_VALUE;
    }
}
