package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.ToIntFunction;
import j$.util.p;
import j$.util.t;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes2.dex */
public abstract class L0 extends c implements IntStream {
    public L0(c cVar, int i) {
        super(cVar, i);
    }

    public L0(j$.util.t tVar, int i, boolean z) {
        super(tVar, i, z);
    }

    public static /* synthetic */ t.b L0(j$.util.t tVar) {
        return M0(tVar);
    }

    public static t.b M0(j$.util.t tVar) {
        if (tVar instanceof t.b) {
            return (t.b) tVar;
        }
        if (Q4.a) {
            Q4.a(c.class, "using IntStream.adapt(Spliterator<Integer> s)");
            throw null;
        }
        throw new UnsupportedOperationException("IntStream.adapt(Spliterator<Integer> s)");
    }

    @Override // j$.util.stream.IntStream
    public final U A(j$.wrappers.W w) {
        Objects.requireNonNull(w);
        return new K(this, this, e4.INT_VALUE, d4.p | d4.n, w);
    }

    @Override // j$.util.stream.c
    final void A0(j$.util.t tVar, m3 m3Var) {
        j$.util.function.l b0;
        t.b M0 = M0(tVar);
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
    public final boolean C(j$.wrappers.U u) {
        return ((Boolean) x0(o1.v(u, k1.ALL))).booleanValue();
    }

    @Override // j$.util.stream.IntStream
    public final boolean F(j$.wrappers.U u) {
        return ((Boolean) x0(o1.v(u, k1.ANY))).booleanValue();
    }

    public void I(j$.util.function.l lVar) {
        Objects.requireNonNull(lVar);
        x0(new l0(lVar, true));
    }

    @Override // j$.util.stream.IntStream
    public final Stream J(j$.util.function.m mVar) {
        Objects.requireNonNull(mVar);
        return new L(this, this, e4.INT_VALUE, d4.p | d4.n, mVar);
    }

    @Override // j$.util.stream.c
    final j$.util.t K0(y2 y2Var, j$.util.function.y yVar, boolean z) {
        return new q4(y2Var, yVar, z);
    }

    @Override // j$.util.stream.IntStream
    public final int N(int i, j$.util.function.j jVar) {
        Objects.requireNonNull(jVar);
        return ((Integer) x0(new L2(e4.INT_VALUE, jVar, i))).intValue();
    }

    @Override // j$.util.stream.IntStream
    public final IntStream P(j$.util.function.m mVar) {
        return new M(this, this, e4.INT_VALUE, d4.p | d4.n | d4.t, mVar);
    }

    public void U(j$.util.function.l lVar) {
        Objects.requireNonNull(lVar);
        x0(new l0(lVar, false));
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.k a0(j$.util.function.j jVar) {
        Objects.requireNonNull(jVar);
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
        long[] jArr = (long[]) k0(new j$.util.function.y() { // from class: j$.util.stream.v0
            @Override // j$.util.function.y
            public final Object get() {
                return new long[2];
            }
        }, new j$.util.function.v() { // from class: j$.util.stream.u0
            @Override // j$.util.function.v
            public final void accept(Object obj, int i) {
                long[] jArr2 = (long[]) obj;
                jArr2[0] = jArr2[0] + 1;
                jArr2[1] = jArr2[1] + i;
            }
        }, new BiConsumer() { // from class: j$.util.stream.x0
            @Override // j$.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                long[] jArr2 = (long[]) obj;
                long[] jArr3 = (long[]) obj2;
                jArr2[0] = jArr2[0] + jArr3[0];
                jArr2[1] = jArr2[1] + jArr3[1];
            }

            @Override // j$.util.function.BiConsumer
            public /* synthetic */ BiConsumer andThen(BiConsumer biConsumer) {
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
            }
        });
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
        Objects.requireNonNull(lVar);
        return new M(this, this, e4.INT_VALUE, 0, lVar);
    }

    @Override // j$.util.stream.IntStream
    public final long count() {
        return ((d1) f(new j$.util.function.n() { // from class: j$.util.stream.E0
            @Override // j$.util.function.n
            public final long applyAsLong(int i) {
                return 1L;
            }
        })).sum();
    }

    @Override // j$.util.stream.IntStream
    public final IntStream distinct() {
        return ((e3) J(C0.a)).distinct().m(new ToIntFunction() { // from class: j$.util.stream.w0
            @Override // j$.util.function.ToIntFunction
            public final int applyAsInt(Object obj) {
                return ((Integer) obj).intValue();
            }
        });
    }

    @Override // j$.util.stream.IntStream
    public final e1 f(j$.util.function.n nVar) {
        Objects.requireNonNull(nVar);
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
    public final IntStream h(j$.wrappers.U u) {
        Objects.requireNonNull(u);
        return new M(this, this, e4.INT_VALUE, d4.t, u);
    }

    @Override // j$.util.stream.g
    public final p.a iterator() {
        return j$.util.J.g(spliterator());
    }

    @Override // j$.util.stream.g
    public Iterator iterator() {
        return j$.util.J.g(spliterator());
    }

    @Override // j$.util.stream.IntStream
    public final Object k0(j$.util.function.y yVar, j$.util.function.v vVar, BiConsumer biConsumer) {
        C c = new C(biConsumer, 1);
        Objects.requireNonNull(yVar);
        Objects.requireNonNull(vVar);
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
        return a0(new j$.util.function.j() { // from class: j$.util.stream.z0
            @Override // j$.util.function.j
            public final int applyAsInt(int i, int i2) {
                return Math.max(i, i2);
            }
        });
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.k min() {
        return a0(new j$.util.function.j() { // from class: j$.util.stream.A0
            @Override // j$.util.function.j
            public final int applyAsInt(int i, int i2) {
                return Math.min(i, i2);
            }
        });
    }

    @Override // j$.util.stream.IntStream
    public final IntStream q(j$.wrappers.a0 a0Var) {
        Objects.requireNonNull(a0Var);
        return new M(this, this, e4.INT_VALUE, d4.p | d4.n, a0Var);
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
    public final t.b spliterator() {
        return M0(super.spliterator());
    }

    @Override // j$.util.stream.IntStream
    public final int sum() {
        return ((Integer) x0(new L2(e4.INT_VALUE, new j$.util.function.j() { // from class: j$.util.stream.y0
            @Override // j$.util.function.j
            public final int applyAsInt(int i, int i2) {
                return i + i2;
            }
        }, 0))).intValue();
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.h summaryStatistics() {
        return (j$.util.h) k0(new j$.util.function.y() { // from class: j$.util.stream.j
            @Override // j$.util.function.y
            public final Object get() {
                return new j$.util.h();
            }
        }, new j$.util.function.v() { // from class: j$.util.stream.t0
            @Override // j$.util.function.v
            public final void accept(Object obj, int i) {
                ((j$.util.h) obj).accept(i);
            }
        }, new BiConsumer() { // from class: j$.util.stream.s0
            @Override // j$.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((j$.util.h) obj).b((j$.util.h) obj2);
            }

            @Override // j$.util.function.BiConsumer
            public /* synthetic */ BiConsumer andThen(BiConsumer biConsumer) {
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
            }
        });
    }

    @Override // j$.util.stream.y2
    public final s1 t0(long j, j$.util.function.m mVar) {
        return x2.p(j);
    }

    @Override // j$.util.stream.IntStream
    public final int[] toArray() {
        return (int[]) x2.n((w1) y0(new j$.util.function.m() { // from class: j$.util.stream.D0
            @Override // j$.util.function.m
            public final Object apply(int i) {
                return new Integer[i];
            }
        })).e();
    }

    @Override // j$.util.stream.g
    public g unordered() {
        return !C0() ? this : new H0(this, this, e4.INT_VALUE, d4.r);
    }

    @Override // j$.util.stream.IntStream
    public final boolean v(j$.wrappers.U u) {
        return ((Boolean) x0(o1.v(u, k1.NONE))).booleanValue();
    }

    @Override // j$.util.stream.c
    final A1 z0(y2 y2Var, j$.util.t tVar, boolean z, j$.util.function.m mVar) {
        return x2.g(y2Var, tVar, z);
    }
}
