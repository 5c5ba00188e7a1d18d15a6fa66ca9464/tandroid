package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Supplier;
import j$.util.function.ToLongFunction;
import j$.util.t;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes2.dex */
public abstract class e1 extends c implements f1 {
    public e1(c cVar, int i) {
        super(cVar, i);
    }

    public e1(j$.util.t tVar, int i, boolean z) {
        super(tVar, i, z);
    }

    public static /* synthetic */ t.c J0(j$.util.t tVar) {
        return K0(tVar);
    }

    public static t.c K0(j$.util.t tVar) {
        if (tVar instanceof t.c) {
            return (t.c) tVar;
        }
        if (R4.a) {
            R4.a(c.class, "using LongStream.adapt(Spliterator<Long> s)");
            throw null;
        }
        throw new UnsupportedOperationException("LongStream.adapt(Spliterator<Long> s)");
    }

    @Override // j$.util.stream.f1
    public final long D(long j, j$.util.function.o oVar) {
        Objects.requireNonNull(oVar);
        return ((Long) v0(new Q2(f4.LONG_VALUE, oVar, j))).longValue();
    }

    @Override // j$.util.stream.c
    final j$.util.t I0(z2 z2Var, Supplier supplier, boolean z) {
        return new t4(z2Var, supplier, z);
    }

    @Override // j$.util.stream.f1
    public final boolean L(j$.wrappers.i0 i0Var) {
        return ((Boolean) v0(p1.w(i0Var, l1.ALL))).booleanValue();
    }

    @Override // j$.util.stream.f1
    public final V O(j$.wrappers.k0 k0Var) {
        Objects.requireNonNull(k0Var);
        return new L(this, this, f4.LONG_VALUE, e4.p | e4.n, k0Var);
    }

    @Override // j$.util.stream.f1
    public final Stream Q(j$.util.function.r rVar) {
        Objects.requireNonNull(rVar);
        return new M(this, this, f4.LONG_VALUE, e4.p | e4.n, rVar);
    }

    @Override // j$.util.stream.f1
    public final boolean S(j$.wrappers.i0 i0Var) {
        return ((Boolean) v0(p1.w(i0Var, l1.NONE))).booleanValue();
    }

    public void Y(j$.util.function.q qVar) {
        Objects.requireNonNull(qVar);
        v0(new n0(qVar, true));
    }

    @Override // j$.util.stream.f1
    public final V asDoubleStream() {
        return new P(this, this, f4.LONG_VALUE, e4.p | e4.n);
    }

    @Override // j$.util.stream.f1
    public final j$.util.j average() {
        long[] jArr = (long[]) d0(new Supplier() { // from class: j$.util.stream.Q0
            @Override // j$.util.function.Supplier
            public final Object get() {
                return new long[2];
            }
        }, new j$.util.function.w() { // from class: j$.util.stream.P0
            @Override // j$.util.function.w
            public final void accept(Object obj, long j) {
                long[] jArr2 = (long[]) obj;
                jArr2[0] = jArr2[0] + 1;
                jArr2[1] = jArr2[1] + j;
            }
        }, new BiConsumer() { // from class: j$.util.stream.S0
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

    @Override // j$.util.stream.f1
    public final Stream boxed() {
        return Q(Y0.a);
    }

    @Override // j$.util.stream.f1
    public final IntStream c0(j$.wrappers.m0 m0Var) {
        Objects.requireNonNull(m0Var);
        return new N(this, this, f4.LONG_VALUE, e4.p | e4.n, m0Var);
    }

    @Override // j$.util.stream.f1
    public final long count() {
        return ((e1) z(new j$.util.function.t() { // from class: j$.util.stream.Z0
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
        v0(new n0(qVar, false));
    }

    @Override // j$.util.stream.f1
    public final Object d0(Supplier supplier, j$.util.function.w wVar, BiConsumer biConsumer) {
        D d = new D(biConsumer, 2);
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(wVar);
        return v0(new A2(f4.LONG_VALUE, d, wVar, supplier));
    }

    @Override // j$.util.stream.f1
    public final f1 distinct() {
        return ((f3) Q(Y0.a)).distinct().e0(new ToLongFunction() { // from class: j$.util.stream.R0
            @Override // j$.util.function.ToLongFunction
            public final long applyAsLong(Object obj) {
                return ((Long) obj).longValue();
            }
        });
    }

    @Override // j$.util.stream.f1
    public final j$.util.l findAny() {
        return (j$.util.l) v0(new e0(false, f4.LONG_VALUE, j$.util.l.a(), Z.a, c0.a));
    }

    @Override // j$.util.stream.f1
    public final j$.util.l findFirst() {
        return (j$.util.l) v0(new e0(true, f4.LONG_VALUE, j$.util.l.a(), Z.a, c0.a));
    }

    @Override // j$.util.stream.f1
    public final j$.util.l g(j$.util.function.o oVar) {
        Objects.requireNonNull(oVar);
        return (j$.util.l) v0(new E2(f4.LONG_VALUE, oVar));
    }

    @Override // j$.util.stream.g
    public final j$.util.r iterator() {
        return j$.util.J.h(spliterator());
    }

    @Override // j$.util.stream.g
    public Iterator iterator() {
        return j$.util.J.h(spliterator());
    }

    @Override // j$.util.stream.f1
    public final boolean k(j$.wrappers.i0 i0Var) {
        return ((Boolean) v0(p1.w(i0Var, l1.ANY))).booleanValue();
    }

    @Override // j$.util.stream.f1
    public final f1 limit(long j) {
        if (j >= 0) {
            return C3.h(this, 0L, j);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.f1
    public final j$.util.l max() {
        return g(new j$.util.function.o() { // from class: j$.util.stream.V0
            @Override // j$.util.function.o
            public final long applyAsLong(long j, long j2) {
                return Math.max(j, j2);
            }
        });
    }

    @Override // j$.util.stream.f1
    public final j$.util.l min() {
        return g(new j$.util.function.o() { // from class: j$.util.stream.W0
            @Override // j$.util.function.o
            public final long applyAsLong(long j, long j2) {
                return Math.min(j, j2);
            }
        });
    }

    @Override // j$.util.stream.f1
    public final f1 p(j$.util.function.q qVar) {
        Objects.requireNonNull(qVar);
        return new O(this, this, f4.LONG_VALUE, 0, qVar);
    }

    @Override // j$.util.stream.z2
    public final t1 r0(long j, j$.util.function.m mVar) {
        return y2.q(j);
    }

    @Override // j$.util.stream.f1
    public final f1 s(j$.util.function.r rVar) {
        return new O(this, this, f4.LONG_VALUE, e4.p | e4.n | e4.t, rVar);
    }

    @Override // j$.util.stream.f1
    public final f1 skip(long j) {
        int i = (j > 0L ? 1 : (j == 0L ? 0 : -1));
        if (i >= 0) {
            return i == 0 ? this : C3.h(this, j, -1L);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.f1
    public final f1 sorted() {
        return new M3(this);
    }

    @Override // j$.util.stream.c, j$.util.stream.g
    public final t.c spliterator() {
        return K0(super.spliterator());
    }

    @Override // j$.util.stream.f1
    public final long sum() {
        return ((Long) v0(new Q2(f4.LONG_VALUE, new j$.util.function.o() { // from class: j$.util.stream.U0
            @Override // j$.util.function.o
            public final long applyAsLong(long j, long j2) {
                return j + j2;
            }
        }, 0L))).longValue();
    }

    @Override // j$.util.stream.f1
    public final j$.util.i summaryStatistics() {
        return (j$.util.i) d0(new Supplier() { // from class: j$.util.stream.m
            @Override // j$.util.function.Supplier
            public final Object get() {
                return new j$.util.i();
            }
        }, new j$.util.function.w() { // from class: j$.util.stream.O0
            @Override // j$.util.function.w
            public final void accept(Object obj, long j) {
                ((j$.util.i) obj).accept(j);
            }
        }, new BiConsumer() { // from class: j$.util.stream.N0
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

    @Override // j$.util.stream.f1
    public final long[] toArray() {
        return (long[]) y2.o((z1) w0(new j$.util.function.m() { // from class: j$.util.stream.T0
            @Override // j$.util.function.m
            public final Object apply(int i) {
                return new Long[i];
            }
        })).e();
    }

    @Override // j$.util.stream.f1
    public final f1 u(j$.wrappers.i0 i0Var) {
        Objects.requireNonNull(i0Var);
        return new O(this, this, f4.LONG_VALUE, e4.t, i0Var);
    }

    @Override // j$.util.stream.g
    public g unordered() {
        return !A0() ? this : new H0(this, this, f4.LONG_VALUE, e4.r);
    }

    @Override // j$.util.stream.c
    final B1 x0(z2 z2Var, j$.util.t tVar, boolean z, j$.util.function.m mVar) {
        return y2.h(z2Var, tVar, z);
    }

    @Override // j$.util.stream.c
    final void y0(j$.util.t tVar, n3 n3Var) {
        j$.util.function.q x0;
        t.c K0 = K0(tVar);
        if (n3Var instanceof j$.util.function.q) {
            x0 = (j$.util.function.q) n3Var;
        } else if (R4.a) {
            R4.a(c.class, "using LongStream.adapt(Sink<Long> s)");
            throw null;
        } else {
            x0 = new X0(n3Var);
        }
        while (!n3Var.o() && K0.i(x0)) {
        }
    }

    @Override // j$.util.stream.f1
    public final f1 z(j$.util.function.t tVar) {
        Objects.requireNonNull(tVar);
        return new O(this, this, f4.LONG_VALUE, e4.p | e4.n, tVar);
    }

    @Override // j$.util.stream.c
    public final f4 z0() {
        return f4.LONG_VALUE;
    }
}
