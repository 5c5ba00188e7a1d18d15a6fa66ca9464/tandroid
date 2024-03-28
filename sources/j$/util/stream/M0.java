package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Supplier;
import j$.util.function.ToIntFunction;
import j$.util.p;
import j$.util.t;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes2.dex */
public abstract class M0 extends c implements IntStream {
    public M0(c cVar, int i) {
        super(cVar, i);
    }

    public M0(j$.util.t tVar, int i, boolean z) {
        super(tVar, i, z);
    }

    public static /* synthetic */ t.b J0(j$.util.t tVar) {
        return K0(tVar);
    }

    public static t.b K0(j$.util.t tVar) {
        if (tVar instanceof t.b) {
            return (t.b) tVar;
        }
        if (R4.a) {
            R4.a(c.class, "using IntStream.adapt(Spliterator<Integer> s)");
            throw null;
        }
        throw new UnsupportedOperationException("IntStream.adapt(Spliterator<Integer> s)");
    }

    @Override // j$.util.stream.IntStream
    public final V A(j$.wrappers.W w) {
        Objects.requireNonNull(w);
        return new L(this, this, f4.INT_VALUE, e4.p | e4.n, w);
    }

    @Override // j$.util.stream.IntStream
    public final boolean C(j$.wrappers.U u) {
        return ((Boolean) v0(p1.v(u, l1.ALL))).booleanValue();
    }

    @Override // j$.util.stream.IntStream
    public final boolean F(j$.wrappers.U u) {
        return ((Boolean) v0(p1.v(u, l1.ANY))).booleanValue();
    }

    public void I(j$.util.function.l lVar) {
        Objects.requireNonNull(lVar);
        v0(new m0(lVar, true));
    }

    @Override // j$.util.stream.c
    final j$.util.t I0(z2 z2Var, Supplier supplier, boolean z) {
        return new r4(z2Var, supplier, z);
    }

    @Override // j$.util.stream.IntStream
    public final Stream J(j$.util.function.m mVar) {
        Objects.requireNonNull(mVar);
        return new M(this, this, f4.INT_VALUE, e4.p | e4.n, mVar);
    }

    @Override // j$.util.stream.IntStream
    public final int N(int i, j$.util.function.j jVar) {
        Objects.requireNonNull(jVar);
        return ((Integer) v0(new M2(f4.INT_VALUE, jVar, i))).intValue();
    }

    @Override // j$.util.stream.IntStream
    public final IntStream P(j$.util.function.m mVar) {
        return new N(this, this, f4.INT_VALUE, e4.p | e4.n | e4.t, mVar);
    }

    public void T(j$.util.function.l lVar) {
        Objects.requireNonNull(lVar);
        v0(new m0(lVar, false));
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.k Z(j$.util.function.j jVar) {
        Objects.requireNonNull(jVar);
        return (j$.util.k) v0(new E2(f4.INT_VALUE, jVar));
    }

    @Override // j$.util.stream.IntStream
    public final IntStream a0(j$.util.function.l lVar) {
        Objects.requireNonNull(lVar);
        return new N(this, this, f4.INT_VALUE, 0, lVar);
    }

    @Override // j$.util.stream.IntStream
    public final V asDoubleStream() {
        return new P(this, this, f4.INT_VALUE, e4.p | e4.n);
    }

    @Override // j$.util.stream.IntStream
    public final f1 asLongStream() {
        return new H0(this, this, f4.INT_VALUE, e4.p | e4.n);
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.j average() {
        long[] jArr = (long[]) i0(new Supplier() { // from class: j$.util.stream.w0
            @Override // j$.util.function.Supplier
            public final Object get() {
                return new long[2];
            }
        }, new j$.util.function.v() { // from class: j$.util.stream.v0
            @Override // j$.util.function.v
            public final void accept(Object obj, int i) {
                long[] jArr2 = (long[]) obj;
                jArr2[0] = jArr2[0] + 1;
                jArr2[1] = jArr2[1] + i;
            }
        }, new BiConsumer() { // from class: j$.util.stream.y0
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
        return J(D0.a);
    }

    @Override // j$.util.stream.IntStream
    public final long count() {
        return ((e1) f(new j$.util.function.n() { // from class: j$.util.stream.F0
            @Override // j$.util.function.n
            public final long applyAsLong(int i) {
                return 1L;
            }
        })).sum();
    }

    @Override // j$.util.stream.IntStream
    public final IntStream distinct() {
        return ((f3) J(D0.a)).distinct().m(new ToIntFunction() { // from class: j$.util.stream.x0
            @Override // j$.util.function.ToIntFunction
            public final int applyAsInt(Object obj) {
                return ((Integer) obj).intValue();
            }
        });
    }

    @Override // j$.util.stream.IntStream
    public final f1 f(j$.util.function.n nVar) {
        Objects.requireNonNull(nVar);
        return new O(this, this, f4.INT_VALUE, e4.p | e4.n, nVar);
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.k findAny() {
        return (j$.util.k) v0(new e0(false, f4.INT_VALUE, j$.util.k.a(), Y.a, b0.a));
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.k findFirst() {
        return (j$.util.k) v0(new e0(true, f4.INT_VALUE, j$.util.k.a(), Y.a, b0.a));
    }

    @Override // j$.util.stream.IntStream
    public final IntStream h(j$.wrappers.U u) {
        Objects.requireNonNull(u);
        return new N(this, this, f4.INT_VALUE, e4.t, u);
    }

    @Override // j$.util.stream.IntStream
    public final Object i0(Supplier supplier, j$.util.function.v vVar, BiConsumer biConsumer) {
        D d = new D(biConsumer, 1);
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(vVar);
        return v0(new A2(f4.INT_VALUE, d, vVar, supplier));
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
    public final IntStream limit(long j) {
        if (j >= 0) {
            return C3.g(this, 0L, j);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.k max() {
        return Z(new j$.util.function.j() { // from class: j$.util.stream.A0
            @Override // j$.util.function.j
            public final int applyAsInt(int i, int i2) {
                return Math.max(i, i2);
            }
        });
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.k min() {
        return Z(new j$.util.function.j() { // from class: j$.util.stream.B0
            @Override // j$.util.function.j
            public final int applyAsInt(int i, int i2) {
                return Math.min(i, i2);
            }
        });
    }

    @Override // j$.util.stream.IntStream
    public final IntStream q(j$.wrappers.a0 a0Var) {
        Objects.requireNonNull(a0Var);
        return new N(this, this, f4.INT_VALUE, e4.p | e4.n, a0Var);
    }

    @Override // j$.util.stream.z2
    public final t1 r0(long j, j$.util.function.m mVar) {
        return y2.p(j);
    }

    @Override // j$.util.stream.IntStream
    public final IntStream skip(long j) {
        int i = (j > 0L ? 1 : (j == 0L ? 0 : -1));
        if (i >= 0) {
            return i == 0 ? this : C3.g(this, j, -1L);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.IntStream
    public final IntStream sorted() {
        return new L3(this);
    }

    @Override // j$.util.stream.c, j$.util.stream.g
    public final t.b spliterator() {
        return K0(super.spliterator());
    }

    @Override // j$.util.stream.IntStream
    public final int sum() {
        return ((Integer) v0(new M2(f4.INT_VALUE, new j$.util.function.j() { // from class: j$.util.stream.z0
            @Override // j$.util.function.j
            public final int applyAsInt(int i, int i2) {
                return i + i2;
            }
        }, 0))).intValue();
    }

    @Override // j$.util.stream.IntStream
    public final j$.util.h summaryStatistics() {
        return (j$.util.h) i0(new Supplier() { // from class: j$.util.stream.l
            @Override // j$.util.function.Supplier
            public final Object get() {
                return new j$.util.h();
            }
        }, new j$.util.function.v() { // from class: j$.util.stream.u0
            @Override // j$.util.function.v
            public final void accept(Object obj, int i) {
                ((j$.util.h) obj).accept(i);
            }
        }, new BiConsumer() { // from class: j$.util.stream.t0
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

    @Override // j$.util.stream.IntStream
    public final int[] toArray() {
        return (int[]) y2.n((x1) w0(new j$.util.function.m() { // from class: j$.util.stream.E0
            @Override // j$.util.function.m
            public final Object apply(int i) {
                return new Integer[i];
            }
        })).e();
    }

    @Override // j$.util.stream.g
    public g unordered() {
        return !A0() ? this : new I0(this, this, f4.INT_VALUE, e4.r);
    }

    @Override // j$.util.stream.IntStream
    public final boolean v(j$.wrappers.U u) {
        return ((Boolean) v0(p1.v(u, l1.NONE))).booleanValue();
    }

    @Override // j$.util.stream.c
    final B1 x0(z2 z2Var, j$.util.t tVar, boolean z, j$.util.function.m mVar) {
        return y2.g(z2Var, tVar, z);
    }

    @Override // j$.util.stream.c
    final void y0(j$.util.t tVar, n3 n3Var) {
        j$.util.function.l c0;
        t.b K0 = K0(tVar);
        if (n3Var instanceof j$.util.function.l) {
            c0 = (j$.util.function.l) n3Var;
        } else if (R4.a) {
            R4.a(c.class, "using IntStream.adapt(Sink<Integer> s)");
            throw null;
        } else {
            c0 = new C0(n3Var);
        }
        while (!n3Var.o() && K0.g(c0)) {
        }
    }

    @Override // j$.util.stream.c
    public final f4 z0() {
        return f4.INT_VALUE;
    }
}
