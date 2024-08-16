package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Predicate;
import org.telegram.messenger.LiteMode;
/* loaded from: classes2.dex */
public abstract /* synthetic */ class t0 implements x3 {
    private static final X0 a = new X0();
    private static final B0 b = new V0();
    private static final D0 c = new W0();
    private static final z0 d = new U0();
    private static final int[] e = new int[0];
    private static final long[] f = new long[0];
    private static final double[] g = new double[0];

    /* JADX INFO: Access modifiers changed from: package-private */
    public t0(T2 t2) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static x0 D(long j, j$.util.function.I i) {
        return (j < 0 || j >= 2147483639) ? new r1() : new Z0(j, i);
    }

    public static F0 E(b bVar, j$.util.Q q, boolean z, j$.util.function.I i) {
        long o0 = bVar.o0(q);
        if (o0 < 0 || !q.hasCharacteristics(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM)) {
            F0 f0 = (F0) new K0(q, i, bVar).invoke();
            return z ? N(f0, i) : f0;
        } else if (o0 < 2147483639) {
            Object[] objArr = (Object[]) i.apply((int) o0);
            new p1(q, bVar, objArr).invoke();
            return new I0(objArr);
        } else {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
    }

    public static z0 F(b bVar, j$.util.Q q, boolean z) {
        long o0 = bVar.o0(q);
        if (o0 < 0 || !q.hasCharacteristics(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM)) {
            z0 z0Var = (z0) new K0(0, q, bVar).invoke();
            return z ? O(z0Var) : z0Var;
        } else if (o0 < 2147483639) {
            double[] dArr = new double[(int) o0];
            new m1(q, bVar, dArr).invoke();
            return new R0(dArr);
        } else {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
    }

    public static B0 G(b bVar, j$.util.Q q, boolean z) {
        long o0 = bVar.o0(q);
        if (o0 < 0 || !q.hasCharacteristics(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM)) {
            B0 b0 = (B0) new K0(1, q, bVar).invoke();
            return z ? P(b0) : b0;
        } else if (o0 < 2147483639) {
            int[] iArr = new int[(int) o0];
            new n1(q, bVar, iArr).invoke();
            return new a1(iArr);
        } else {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
    }

    public static D0 H(b bVar, j$.util.Q q, boolean z) {
        long o0 = bVar.o0(q);
        if (o0 < 0 || !q.hasCharacteristics(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM)) {
            D0 d0 = (D0) new K0(2, q, bVar).invoke();
            return z ? Q(d0) : d0;
        } else if (o0 < 2147483639) {
            long[] jArr = new long[(int) o0];
            new o1(q, bVar, jArr).invoke();
            return new j1(jArr);
        } else {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static H0 I(T2 t2, F0 f0, F0 f02) {
        int i = G0.a[t2.ordinal()];
        if (i != 1) {
            if (i != 2) {
                if (i != 3) {
                    if (i == 4) {
                        return new M0((z0) f0, (z0) f02);
                    }
                    throw new IllegalStateException("Unknown shape " + t2);
                }
                return new O0((D0) f0, (D0) f02);
            }
            return new N0((B0) f0, (B0) f02);
        }
        return new Q0(f0, f02);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static u0 J(long j) {
        return (j < 0 || j >= 2147483639) ? new T0() : new S0(j);
    }

    public static D K(j$.util.E e2) {
        return new y(e2, S2.c(e2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Y0 L(T2 t2) {
        Object obj;
        int i = G0.a[t2.ordinal()];
        if (i != 1) {
            if (i == 2) {
                obj = b;
            } else if (i == 3) {
                obj = c;
            } else if (i != 4) {
                throw new IllegalStateException("Unknown shape " + t2);
            } else {
                obj = d;
            }
            return (Y0) obj;
        }
        return a;
    }

    private static int M(long j) {
        return (j != -1 ? S2.u : 0) | S2.t;
    }

    public static F0 N(F0 f0, j$.util.function.I i) {
        if (f0.p() > 0) {
            long count = f0.count();
            if (count < 2147483639) {
                Object[] objArr = (Object[]) i.apply((int) count);
                new t1(f0, objArr, 1).invoke();
                return new I0(objArr);
            }
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        return f0;
    }

    public static z0 O(z0 z0Var) {
        if (z0Var.p() > 0) {
            long count = z0Var.count();
            if (count < 2147483639) {
                double[] dArr = new double[(int) count];
                new s1(z0Var, dArr).invoke();
                return new R0(dArr);
            }
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        return z0Var;
    }

    public static B0 P(B0 b0) {
        if (b0.p() > 0) {
            long count = b0.count();
            if (count < 2147483639) {
                int[] iArr = new int[(int) count];
                new s1(b0, iArr).invoke();
                return new a1(iArr);
            }
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        return b0;
    }

    public static D0 Q(D0 d0) {
        if (d0.p() > 0) {
            long count = d0.count();
            if (count < 2147483639) {
                long[] jArr = new long[(int) count];
                new s1(d0, jArr).invoke();
                return new j1(jArr);
            }
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        return d0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static v0 R(long j) {
        return (j < 0 || j >= 2147483639) ? new c1() : new b1(j);
    }

    public static IntStream S(j$.util.H h) {
        return new Z(h, S2.c(h));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static w0 T(long j) {
        return (j < 0 || j >= 2147483639) ? new l1() : new k1(j);
    }

    public static LongStream U(j$.util.K k) {
        return new f0(k, S2.c(k));
    }

    public static D V(b bVar, long j, long j2) {
        if (j >= 0) {
            return new m2(bVar, M(j2), j, j2);
        }
        throw new IllegalArgumentException("Skip must be non-negative: " + j);
    }

    public static r0 W(j$.util.function.r rVar, q0 q0Var) {
        rVar.getClass();
        q0Var.getClass();
        return new r0(T2.DOUBLE_VALUE, q0Var, new k0(2, q0Var, rVar));
    }

    public static IntStream X(b bVar, long j, long j2) {
        if (j >= 0) {
            return new i2(bVar, M(j2), j, j2);
        }
        throw new IllegalArgumentException("Skip must be non-negative: " + j);
    }

    public static r0 Y(j$.util.function.J j, q0 q0Var) {
        j.getClass();
        q0Var.getClass();
        return new r0(T2.INT_VALUE, q0Var, new k0(1, q0Var, j));
    }

    public static LongStream Z(b bVar, long j, long j2) {
        if (j >= 0) {
            return new k2(bVar, M(j2), j, j2);
        }
        throw new IllegalArgumentException("Skip must be non-negative: " + j);
    }

    public static r0 a0(j$.util.function.Z z, q0 q0Var) {
        z.getClass();
        q0Var.getClass();
        return new r0(T2.LONG_VALUE, q0Var, new k0(0, q0Var, z));
    }

    public static void b() {
        throw new IllegalStateException("called wrong accept method");
    }

    public static r0 b0(Predicate predicate, q0 q0Var) {
        predicate.getClass();
        q0Var.getClass();
        return new r0(T2.REFERENCE, q0Var, new k0(3, q0Var, predicate));
    }

    public static Stream c0(b bVar, long j, long j2) {
        if (j >= 0) {
            return new g2(bVar, M(j2), j, j2);
        }
        throw new IllegalArgumentException("Skip must be non-negative: " + j);
    }

    public static void e(b2 b2Var, Double d2) {
        if (A3.a) {
            A3.a(b2Var.getClass(), "{0} calling Sink.OfDouble.accept(Double)");
            throw null;
        } else {
            b2Var.accept(d2.doubleValue());
        }
    }

    public static Stream e0(j$.util.Q q, boolean z) {
        q.getClass();
        return new U1(q, S2.c(q), z);
    }

    public static void g(c2 c2Var, Integer num) {
        if (A3.a) {
            A3.a(c2Var.getClass(), "{0} calling Sink.OfInt.accept(Integer)");
            throw null;
        } else {
            c2Var.accept(num.intValue());
        }
    }

    public static void i(d2 d2Var, Long l) {
        if (A3.a) {
            A3.a(d2Var.getClass(), "{0} calling Sink.OfLong.accept(Long)");
            throw null;
        } else {
            d2Var.accept(l.longValue());
        }
    }

    public static void k() {
        throw new IllegalStateException("called wrong accept method");
    }

    public static void l() {
        throw new IllegalStateException("called wrong accept method");
    }

    public static Object[] m(E0 e0, j$.util.function.I i) {
        if (A3.a) {
            A3.a(e0.getClass(), "{0} calling Node.OfPrimitive.asArray");
            throw null;
        } else if (e0.count() < 2147483639) {
            Object[] objArr = (Object[]) i.apply((int) e0.count());
            e0.i(objArr, 0);
            return objArr;
        } else {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
    }

    public static void n(z0 z0Var, Double[] dArr, int i) {
        if (A3.a) {
            A3.a(z0Var.getClass(), "{0} calling Node.OfDouble.copyInto(Double[], int)");
            throw null;
        }
        double[] dArr2 = (double[]) z0Var.e();
        for (int i2 = 0; i2 < dArr2.length; i2++) {
            dArr[i + i2] = Double.valueOf(dArr2[i2]);
        }
    }

    public static void o(B0 b0, Integer[] numArr, int i) {
        if (A3.a) {
            A3.a(b0.getClass(), "{0} calling Node.OfInt.copyInto(Integer[], int)");
            throw null;
        }
        int[] iArr = (int[]) b0.e();
        for (int i2 = 0; i2 < iArr.length; i2++) {
            numArr[i + i2] = Integer.valueOf(iArr[i2]);
        }
    }

    public static void p(D0 d0, Long[] lArr, int i) {
        if (A3.a) {
            A3.a(d0.getClass(), "{0} calling Node.OfInt.copyInto(Long[], int)");
            throw null;
        }
        long[] jArr = (long[]) d0.e();
        for (int i2 = 0; i2 < jArr.length; i2++) {
            lArr[i + i2] = Long.valueOf(jArr[i2]);
        }
    }

    public static void q(z0 z0Var, Consumer consumer) {
        if (consumer instanceof j$.util.function.n) {
            z0Var.g((j$.util.function.n) consumer);
        } else if (A3.a) {
            A3.a(z0Var.getClass(), "{0} calling Node.OfLong.forEachRemaining(Consumer)");
            throw null;
        } else {
            ((j$.util.E) z0Var.spliterator()).a(consumer);
        }
    }

    public static void r(B0 b0, Consumer consumer) {
        if (consumer instanceof j$.util.function.F) {
            b0.g((j$.util.function.F) consumer);
        } else if (A3.a) {
            A3.a(b0.getClass(), "{0} calling Node.OfInt.forEachRemaining(Consumer)");
            throw null;
        } else {
            ((j$.util.H) b0.spliterator()).a(consumer);
        }
    }

    public static void s(D0 d0, Consumer consumer) {
        if (consumer instanceof j$.util.function.W) {
            d0.g((j$.util.function.W) consumer);
        } else if (A3.a) {
            A3.a(d0.getClass(), "{0} calling Node.OfLong.forEachRemaining(Consumer)");
            throw null;
        } else {
            ((j$.util.K) d0.spliterator()).a(consumer);
        }
    }

    public static z0 t(z0 z0Var, long j, long j2) {
        if (j == 0 && j2 == z0Var.count()) {
            return z0Var;
        }
        long j3 = j2 - j;
        j$.util.E e2 = (j$.util.E) z0Var.spliterator();
        u0 J = J(j3);
        J.n(j3);
        for (int i = 0; i < j && e2.p(new y0(0)); i++) {
        }
        for (int i2 = 0; i2 < j3 && e2.p(J); i2++) {
        }
        J.m();
        return J.b();
    }

    public static B0 u(B0 b0, long j, long j2) {
        if (j == 0 && j2 == b0.count()) {
            return b0;
        }
        long j3 = j2 - j;
        j$.util.H h = (j$.util.H) b0.spliterator();
        v0 R = R(j3);
        R.n(j3);
        for (int i = 0; i < j && h.g(new A0(0)); i++) {
        }
        for (int i2 = 0; i2 < j3 && h.g(R); i2++) {
        }
        R.m();
        return R.b();
    }

    public static D0 v(D0 d0, long j, long j2) {
        if (j == 0 && j2 == d0.count()) {
            return d0;
        }
        long j3 = j2 - j;
        j$.util.K k = (j$.util.K) d0.spliterator();
        w0 T = T(j3);
        T.n(j3);
        for (int i = 0; i < j && k.i(new C0(0)); i++) {
        }
        for (int i2 = 0; i2 < j3 && k.i(T); i2++) {
        }
        T.m();
        return T.b();
    }

    public static F0 w(F0 f0, long j, long j2, j$.util.function.I i) {
        if (j == 0 && j2 == f0.count()) {
            return f0;
        }
        j$.util.Q spliterator = f0.spliterator();
        long j3 = j2 - j;
        x0 D = D(j3, i);
        D.n(j3);
        for (int i2 = 0; i2 < j && spliterator.s(new E(29)); i2++) {
        }
        for (int i3 = 0; i3 < j3 && spliterator.s(D); i3++) {
        }
        D.m();
        return D.b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long x(long j, long j2) {
        long j3 = j2 >= 0 ? j + j2 : Long.MAX_VALUE;
        if (j3 >= 0) {
            return j3;
        }
        return Long.MAX_VALUE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static j$.util.Q y(T2 t2, j$.util.Q q, long j, long j2) {
        long j3 = j2 >= 0 ? j + j2 : Long.MAX_VALUE;
        long j4 = j3 >= 0 ? j3 : Long.MAX_VALUE;
        int i = n2.a[t2.ordinal()];
        if (i != 1) {
            if (i != 2) {
                if (i != 3) {
                    if (i == 4) {
                        return new i3((j$.util.E) q, j, j4);
                    }
                    throw new IllegalStateException("Unknown shape " + t2);
                }
                return new k3((j$.util.K) q, j, j4);
            }
            return new j3((j$.util.H) q, j, j4);
        }
        return new m3(q, j, j4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long z(long j, long j2, long j3) {
        if (j >= 0) {
            return Math.max(-1L, Math.min(j - j2, j3));
        }
        return -1L;
    }

    @Override // j$.util.stream.x3
    public Object a(b bVar, j$.util.Q q) {
        N1 d0 = d0();
        bVar.getClass();
        bVar.i0(q, bVar.E0(d0));
        return d0.get();
    }

    @Override // j$.util.stream.x3
    public Object c(b bVar, j$.util.Q q) {
        return ((N1) new P1(this, bVar, q).invoke()).get();
    }

    @Override // j$.util.stream.x3
    public /* synthetic */ int d() {
        return 0;
    }

    public abstract N1 d0();
}
