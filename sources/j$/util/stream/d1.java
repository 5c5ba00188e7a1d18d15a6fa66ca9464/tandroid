package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.ToLongFunction;
import j$.util.t;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes2.dex */
public abstract class d1 extends c implements e1 {
    public d1(c cVar, int i) {
        super(cVar, i);
    }

    public d1(j$.util.t tVar, int i, boolean z) {
        super(tVar, i, z);
    }

    public static /* synthetic */ t.c L0(j$.util.t tVar) {
        return M0(tVar);
    }

    public static t.c M0(j$.util.t tVar) {
        if (tVar instanceof t.c) {
            return (t.c) tVar;
        }
        if (Q4.a) {
            Q4.a(c.class, "using LongStream.adapt(Spliterator<Long> s)");
            throw null;
        }
        throw new UnsupportedOperationException("LongStream.adapt(Spliterator<Long> s)");
    }

    @Override // j$.util.stream.c
    final void A0(j$.util.t tVar, m3 m3Var) {
        j$.util.function.q w0;
        t.c M0 = M0(tVar);
        if (m3Var instanceof j$.util.function.q) {
            w0 = (j$.util.function.q) m3Var;
        } else if (Q4.a) {
            Q4.a(c.class, "using LongStream.adapt(Sink<Long> s)");
            throw null;
        } else {
            w0 = new W0(m3Var);
        }
        while (!m3Var.o() && M0.i(w0)) {
        }
    }

    @Override // j$.util.stream.c
    public final e4 B0() {
        return e4.LONG_VALUE;
    }

    @Override // j$.util.stream.e1
    public final long D(long j, j$.util.function.o oVar) {
        Objects.requireNonNull(oVar);
        return ((Long) x0(new P2(e4.LONG_VALUE, oVar, j))).longValue();
    }

    @Override // j$.util.stream.c
    final j$.util.t K0(y2 y2Var, j$.util.function.y yVar, boolean z) {
        return new s4(y2Var, yVar, z);
    }

    @Override // j$.util.stream.e1
    public final boolean L(j$.wrappers.i0 i0Var) {
        return ((Boolean) x0(o1.w(i0Var, k1.ALL))).booleanValue();
    }

    @Override // j$.util.stream.e1
    public final U O(j$.wrappers.k0 k0Var) {
        Objects.requireNonNull(k0Var);
        return new K(this, this, e4.LONG_VALUE, d4.p | d4.n, k0Var);
    }

    @Override // j$.util.stream.e1
    public final Stream Q(j$.util.function.r rVar) {
        Objects.requireNonNull(rVar);
        return new L(this, this, e4.LONG_VALUE, d4.p | d4.n, rVar);
    }

    @Override // j$.util.stream.e1
    public final boolean S(j$.wrappers.i0 i0Var) {
        return ((Boolean) x0(o1.w(i0Var, k1.NONE))).booleanValue();
    }

    public void Z(j$.util.function.q qVar) {
        Objects.requireNonNull(qVar);
        x0(new m0(qVar, true));
    }

    @Override // j$.util.stream.e1
    public final U asDoubleStream() {
        return new O(this, this, e4.LONG_VALUE, d4.p | d4.n);
    }

    @Override // j$.util.stream.e1
    public final j$.util.j average() {
        long[] jArr = (long[]) f0(new j$.util.function.y() { // from class: j$.util.stream.P0
            @Override // j$.util.function.y
            public final Object get() {
                return new long[2];
            }
        }, new j$.util.function.w() { // from class: j$.util.stream.O0
            @Override // j$.util.function.w
            public final void accept(Object obj, long j) {
                long[] jArr2 = (long[]) obj;
                jArr2[0] = jArr2[0] + 1;
                jArr2[1] = jArr2[1] + j;
            }
        }, new BiConsumer() { // from class: j$.util.stream.R0
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

    @Override // j$.util.stream.e1
    public final Stream boxed() {
        return Q(X0.a);
    }

    @Override // j$.util.stream.e1
    public final long count() {
        return ((d1) z(new j$.util.function.t() { // from class: j$.util.stream.Y0
            @Override // j$.util.function.t
            public j$.util.function.t a(j$.util.function.t tVar) {
                Objects.requireNonNull(tVar);
                return new j$.util.function.s(this, tVar, 0);
            }

            @Override // j$.util.function.t
            public final long applyAsLong(long j) {
                return 1L;
            }

            @Override // j$.util.function.t
            public j$.util.function.t b(j$.util.function.t tVar) {
                Objects.requireNonNull(tVar);
                return new j$.util.function.s(this, tVar, 1);
            }
        })).sum();
    }

    public void d(j$.util.function.q qVar) {
        Objects.requireNonNull(qVar);
        x0(new m0(qVar, false));
    }

    @Override // j$.util.stream.e1
    public final e1 distinct() {
        return ((e3) Q(X0.a)).distinct().g0(new ToLongFunction() { // from class: j$.util.stream.Q0
            @Override // j$.util.function.ToLongFunction
            public final long applyAsLong(Object obj) {
                return ((Long) obj).longValue();
            }
        });
    }

    @Override // j$.util.stream.e1
    public final IntStream e0(j$.wrappers.m0 m0Var) {
        Objects.requireNonNull(m0Var);
        return new M(this, this, e4.LONG_VALUE, d4.p | d4.n, m0Var);
    }

    @Override // j$.util.stream.e1
    public final Object f0(j$.util.function.y yVar, j$.util.function.w wVar, BiConsumer biConsumer) {
        C c = new C(biConsumer, 2);
        Objects.requireNonNull(yVar);
        Objects.requireNonNull(wVar);
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
        Objects.requireNonNull(oVar);
        return (j$.util.l) x0(new D2(e4.LONG_VALUE, oVar));
    }

    @Override // j$.util.stream.g
    public final j$.util.r iterator() {
        return j$.util.J.h(spliterator());
    }

    @Override // j$.util.stream.g
    public Iterator iterator() {
        return j$.util.J.h(spliterator());
    }

    @Override // j$.util.stream.e1
    public final boolean k(j$.wrappers.i0 i0Var) {
        return ((Boolean) x0(o1.w(i0Var, k1.ANY))).booleanValue();
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
        return g(new j$.util.function.o() { // from class: j$.util.stream.U0
            @Override // j$.util.function.o
            public final long applyAsLong(long j, long j2) {
                return Math.max(j, j2);
            }
        });
    }

    @Override // j$.util.stream.e1
    public final j$.util.l min() {
        return g(new j$.util.function.o() { // from class: j$.util.stream.V0
            @Override // j$.util.function.o
            public final long applyAsLong(long j, long j2) {
                return Math.min(j, j2);
            }
        });
    }

    @Override // j$.util.stream.e1
    public final e1 p(j$.util.function.q qVar) {
        Objects.requireNonNull(qVar);
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
    public final t.c spliterator() {
        return M0(super.spliterator());
    }

    @Override // j$.util.stream.e1
    public final long sum() {
        return ((Long) x0(new P2(e4.LONG_VALUE, new j$.util.function.o() { // from class: j$.util.stream.T0
            @Override // j$.util.function.o
            public final long applyAsLong(long j, long j2) {
                return j + j2;
            }
        }, 0L))).longValue();
    }

    @Override // j$.util.stream.e1
    public final j$.util.i summaryStatistics() {
        return (j$.util.i) f0(new j$.util.function.y() { // from class: j$.util.stream.k
            @Override // j$.util.function.y
            public final Object get() {
                return new j$.util.i();
            }
        }, new j$.util.function.w() { // from class: j$.util.stream.N0
            @Override // j$.util.function.w
            public final void accept(Object obj, long j) {
                ((j$.util.i) obj).accept(j);
            }
        }, new BiConsumer() { // from class: j$.util.stream.M0
            @Override // j$.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((j$.util.i) obj).b((j$.util.i) obj2);
            }

            @Override // j$.util.function.BiConsumer
            public /* synthetic */ BiConsumer andThen(BiConsumer biConsumer) {
                return BiConsumer.-CC.$default$andThen(this, biConsumer);
            }
        });
    }

    @Override // j$.util.stream.y2
    public final s1 t0(long j, j$.util.function.m mVar) {
        return x2.q(j);
    }

    @Override // j$.util.stream.e1
    public final long[] toArray() {
        return (long[]) x2.o((y1) y0(new j$.util.function.m() { // from class: j$.util.stream.S0
            @Override // j$.util.function.m
            public final Object apply(int i) {
                return new Long[i];
            }
        })).e();
    }

    @Override // j$.util.stream.e1
    public final e1 u(j$.wrappers.i0 i0Var) {
        Objects.requireNonNull(i0Var);
        return new N(this, this, e4.LONG_VALUE, d4.t, i0Var);
    }

    @Override // j$.util.stream.g
    public g unordered() {
        return !C0() ? this : new G0(this, this, e4.LONG_VALUE, d4.r);
    }

    @Override // j$.util.stream.e1
    public final e1 z(j$.util.function.t tVar) {
        Objects.requireNonNull(tVar);
        return new N(this, this, e4.LONG_VALUE, d4.p | d4.n, tVar);
    }

    @Override // j$.util.stream.c
    final A1 z0(y2 y2Var, j$.util.t tVar, boolean z, j$.util.function.m mVar) {
        return x2.h(y2Var, tVar, z);
    }
}
