package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.LongFunction;
import j$.util.function.Supplier;
import j$.util.function.ToLongFunction;
import j$.util.p;
import j$.util.s;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes2.dex */
public abstract class e1 extends c implements LongStream {
    public e1(j$.util.s sVar, int i, boolean z) {
        super(sVar, i, z);
    }

    public e1(c cVar, int i) {
        super(cVar, i);
    }

    public static /* synthetic */ s.c G0(j$.util.s sVar) {
        return H0(sVar);
    }

    public static s.c H0(j$.util.s sVar) {
        if (sVar instanceof s.c) {
            return (s.c) sVar;
        }
        if (Q4.a) {
            Q4.a(c.class, "using LongStream.adapt(Spliterator<Long> s)");
            throw null;
        }
        throw new UnsupportedOperationException("LongStream.adapt(Spliterator<Long> s)");
    }

    @Override // j$.util.stream.LongStream
    public final long C(long j, j$.util.function.o oVar) {
        Objects.requireNonNull(oVar);
        return ((Long) s0(new P2(e4.LONG_VALUE, oVar, j))).longValue();
    }

    @Override // j$.util.stream.c
    final j$.util.s F0(y2 y2Var, Supplier supplier, boolean z) {
        return new s4(y2Var, supplier, z);
    }

    @Override // j$.util.stream.LongStream
    public final boolean K(j$.wrappers.i0 i0Var) {
        return ((Boolean) s0(o1.w(i0Var, k1.ALL))).booleanValue();
    }

    @Override // j$.util.stream.LongStream
    public final V N(j$.wrappers.k0 k0Var) {
        Objects.requireNonNull(k0Var);
        return new L(this, this, e4.LONG_VALUE, d4.p | d4.n, k0Var);
    }

    @Override // j$.util.stream.LongStream
    public final boolean Q(j$.wrappers.i0 i0Var) {
        return ((Boolean) s0(o1.w(i0Var, k1.NONE))).booleanValue();
    }

    public void W(j$.util.function.q qVar) {
        Objects.requireNonNull(qVar);
        s0(new n0(qVar, true));
    }

    @Override // j$.util.stream.LongStream
    public final IntStream a0(j$.wrappers.m0 m0Var) {
        Objects.requireNonNull(m0Var);
        return new N(this, this, e4.LONG_VALUE, d4.p | d4.n, m0Var);
    }

    @Override // j$.util.stream.LongStream
    public final V asDoubleStream() {
        return new P(this, this, e4.LONG_VALUE, d4.p | d4.n);
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.j average() {
        long[] jArr = (long[]) b0(new Supplier() { // from class: j$.util.stream.Q0
            @Override // j$.util.function.Supplier
            public final Object get() {
                return new long[2];
            }
        }, new j$.util.function.v() { // from class: j$.util.stream.P0
            @Override // j$.util.function.v
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

    @Override // j$.util.stream.LongStream
    public final Object b0(Supplier supplier, j$.util.function.v vVar, BiConsumer biConsumer) {
        D d = new D(biConsumer, 2);
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(vVar);
        return s0(new z2(e4.LONG_VALUE, d, vVar, supplier));
    }

    @Override // j$.util.stream.LongStream
    public final Stream boxed() {
        return mapToObj(Y0.a);
    }

    @Override // j$.util.stream.LongStream
    public final long count() {
        return ((e1) y(new j$.util.function.s() { // from class: j$.util.stream.Z0
            @Override // j$.util.function.s
            public j$.util.function.s a(j$.util.function.s sVar) {
                Objects.requireNonNull(sVar);
                return new j$.util.function.r(this, sVar, 0);
            }

            @Override // j$.util.function.s
            public final long applyAsLong(long j) {
                return 1L;
            }

            @Override // j$.util.function.s
            public j$.util.function.s b(j$.util.function.s sVar) {
                Objects.requireNonNull(sVar);
                return new j$.util.function.r(this, sVar, 1);
            }
        })).sum();
    }

    public void d(j$.util.function.q qVar) {
        Objects.requireNonNull(qVar);
        s0(new n0(qVar, false));
    }

    @Override // j$.util.stream.LongStream
    public final LongStream distinct() {
        return ((e3) mapToObj(Y0.a)).distinct().mapToLong(new ToLongFunction() { // from class: j$.util.stream.R0
            @Override // j$.util.function.ToLongFunction
            public final long applyAsLong(Object obj) {
                return ((Long) obj).longValue();
            }
        });
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.l findAny() {
        return (j$.util.l) s0(new e0(false, e4.LONG_VALUE, j$.util.l.a(), Z.a, c0.a));
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.l findFirst() {
        return (j$.util.l) s0(new e0(true, e4.LONG_VALUE, j$.util.l.a(), Z.a, c0.a));
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.l g(j$.util.function.o oVar) {
        Objects.requireNonNull(oVar);
        return (j$.util.l) s0(new D2(e4.LONG_VALUE, oVar));
    }

    @Override // j$.util.stream.g
    public final p.b iterator() {
        return j$.util.I.h(spliterator());
    }

    @Override // j$.util.stream.g
    public Iterator iterator() {
        return j$.util.I.h(spliterator());
    }

    @Override // j$.util.stream.LongStream
    public final boolean k(j$.wrappers.i0 i0Var) {
        return ((Boolean) s0(o1.w(i0Var, k1.ANY))).booleanValue();
    }

    @Override // j$.util.stream.LongStream
    public final LongStream limit(long j) {
        if (j >= 0) {
            return B3.h(this, 0L, j);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.LongStream
    public final Stream mapToObj(LongFunction longFunction) {
        Objects.requireNonNull(longFunction);
        return new M(this, this, e4.LONG_VALUE, d4.p | d4.n, longFunction);
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.l max() {
        return g(new j$.util.function.o() { // from class: j$.util.stream.V0
            @Override // j$.util.function.o
            public final long applyAsLong(long j, long j2) {
                return Math.max(j, j2);
            }
        });
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.l min() {
        return g(new j$.util.function.o() { // from class: j$.util.stream.W0
            @Override // j$.util.function.o
            public final long applyAsLong(long j, long j2) {
                return Math.min(j, j2);
            }
        });
    }

    @Override // j$.util.stream.LongStream
    public final LongStream o(j$.util.function.q qVar) {
        Objects.requireNonNull(qVar);
        return new O(this, this, e4.LONG_VALUE, 0, qVar);
    }

    @Override // j$.util.stream.y2
    public final s1 o0(long j, j$.util.function.m mVar) {
        return x2.q(j);
    }

    @Override // j$.util.stream.LongStream
    public final LongStream r(LongFunction longFunction) {
        return new O(this, this, e4.LONG_VALUE, d4.p | d4.n | d4.t, longFunction);
    }

    @Override // j$.util.stream.LongStream
    public final LongStream skip(long j) {
        int i = (j > 0L ? 1 : (j == 0L ? 0 : -1));
        if (i >= 0) {
            return i == 0 ? this : B3.h(this, j, -1L);
        }
        throw new IllegalArgumentException(Long.toString(j));
    }

    @Override // j$.util.stream.LongStream
    public final LongStream sorted() {
        return new L3(this);
    }

    @Override // j$.util.stream.c, j$.util.stream.g
    public final s.c spliterator() {
        return H0(super.spliterator());
    }

    @Override // j$.util.stream.LongStream
    public final long sum() {
        return ((Long) s0(new P2(e4.LONG_VALUE, new j$.util.function.o() { // from class: j$.util.stream.U0
            @Override // j$.util.function.o
            public final long applyAsLong(long j, long j2) {
                return j + j2;
            }
        }, 0L))).longValue();
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.i summaryStatistics() {
        return (j$.util.i) b0(new Supplier() { // from class: j$.util.stream.m
            @Override // j$.util.function.Supplier
            public final Object get() {
                return new j$.util.i();
            }
        }, new j$.util.function.v() { // from class: j$.util.stream.O0
            @Override // j$.util.function.v
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

    @Override // j$.util.stream.LongStream
    public final LongStream t(j$.wrappers.i0 i0Var) {
        Objects.requireNonNull(i0Var);
        return new O(this, this, e4.LONG_VALUE, d4.t, i0Var);
    }

    @Override // j$.util.stream.LongStream
    public final long[] toArray() {
        return (long[]) x2.o((y1) t0(new j$.util.function.m() { // from class: j$.util.stream.T0
            @Override // j$.util.function.m
            public final Object apply(int i) {
                return new Long[i];
            }
        })).e();
    }

    @Override // j$.util.stream.c
    final A1 u0(y2 y2Var, j$.util.s sVar, boolean z, j$.util.function.m mVar) {
        return x2.h(y2Var, sVar, z);
    }

    @Override // j$.util.stream.g
    public g unordered() {
        return !x0() ? this : new H0(this, this, e4.LONG_VALUE, d4.r);
    }

    @Override // j$.util.stream.c
    final void v0(j$.util.s sVar, m3 m3Var) {
        j$.util.function.q x0;
        s.c H0 = H0(sVar);
        if (m3Var instanceof j$.util.function.q) {
            x0 = (j$.util.function.q) m3Var;
        } else if (Q4.a) {
            Q4.a(c.class, "using LongStream.adapt(Sink<Long> s)");
            throw null;
        } else {
            x0 = new X0(m3Var);
        }
        while (!m3Var.o() && H0.i(x0)) {
        }
    }

    @Override // j$.util.stream.c
    public final e4 w0() {
        return e4.LONG_VALUE;
    }

    @Override // j$.util.stream.LongStream
    public final LongStream y(j$.util.function.s sVar) {
        Objects.requireNonNull(sVar);
        return new O(this, this, e4.LONG_VALUE, d4.p | d4.n, sVar);
    }
}
