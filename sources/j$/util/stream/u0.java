package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Predicate;
/* loaded from: classes2.dex */
public abstract /* synthetic */ class u0 {
    public static B0 A0(B0 b0, long j, long j2) {
        if (j == 0 && j2 == b0.count()) {
            return b0;
        }
        long j3 = j2 - j;
        j$.util.K k = (j$.util.K) b0.spliterator();
        x0 t = u1.t(j3);
        t.f(j3);
        for (int i = 0; i < j && k.e(new n3(1)); i++) {
        }
        for (int i2 = 0; i2 < j3 && k.e(t); i2++) {
        }
        t.end();
        return t.build();
    }

    public static D0 B0(D0 d0, long j, long j2, j$.util.function.N n) {
        if (j == 0 && j2 == d0.count()) {
            return d0;
        }
        j$.util.Q spliterator = d0.spliterator();
        long j3 = j2 - j;
        y0 g = u1.g(j3, n);
        g.f(j3);
        for (int i = 0; i < j && spliterator.a(new W(7)); i++) {
        }
        for (int i2 = 0; i2 < j3 && spliterator.a(g); i2++) {
        }
        g.end();
        return g.build();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long C0(long j, long j2) {
        long j3 = j2 >= 0 ? j + j2 : Long.MAX_VALUE;
        if (j3 >= 0) {
            return j3;
        }
        return Long.MAX_VALUE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static j$.util.Q D0(U2 u2, j$.util.Q q, long j, long j2) {
        long j3 = j2 >= 0 ? j + j2 : Long.MAX_VALUE;
        long j4 = j3 >= 0 ? j3 : Long.MAX_VALUE;
        int i = o2.a[u2.ordinal()];
        if (i != 1) {
            if (i != 2) {
                if (i != 3) {
                    if (i == 4) {
                        return new k3((j$.util.E) q, j, j4);
                    }
                    throw new IllegalStateException("Unknown shape " + u2);
                }
                return new o3((j$.util.K) q, j, j4);
            }
            return new m3((j$.util.H) q, j, j4);
        }
        return new q3(q, j, j4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long E0(long j, long j2, long j3) {
        if (j >= 0) {
            return Math.max(-1L, Math.min(j - j2, j3));
        }
        return -1L;
    }

    public static F H0(j$.util.E e) {
        return new z(e, T2.c(e));
    }

    private static int J0(long j) {
        return (j != -1 ? T2.u : 0) | T2.t;
    }

    public static IntStream L0(j$.util.H h) {
        return new a0(h, T2.c(h));
    }

    public static LongStream M0(j$.util.K k) {
        return new g0(k, T2.c(k));
    }

    public static F N0(c cVar, long j, long j2) {
        if (j >= 0) {
            return new n2(cVar, J0(j2), j, j2);
        }
        throw new IllegalArgumentException("Skip must be non-negative: " + j);
    }

    public static s0 O0(j$.util.function.s sVar, r0 r0Var) {
        sVar.getClass();
        r0Var.getClass();
        return new s0(U2.DOUBLE_VALUE, r0Var, new n(3, r0Var, sVar));
    }

    public static IntStream P0(c cVar, long j, long j2) {
        if (j >= 0) {
            return new j2(cVar, J0(j2), j, j2);
        }
        throw new IllegalArgumentException("Skip must be non-negative: " + j);
    }

    public static s0 Q0(j$.util.function.Q q, r0 r0Var) {
        q.getClass();
        r0Var.getClass();
        return new s0(U2.INT_VALUE, r0Var, new n(1, r0Var, q));
    }

    public static LongStream R0(c cVar, long j, long j2) {
        if (j >= 0) {
            return new l2(cVar, J0(j2), j, j2);
        }
        throw new IllegalArgumentException("Skip must be non-negative: " + j);
    }

    public static s0 S0(j$.util.function.m0 m0Var, r0 r0Var) {
        m0Var.getClass();
        r0Var.getClass();
        return new s0(U2.LONG_VALUE, r0Var, new n(4, r0Var, m0Var));
    }

    public static s0 U0(Predicate predicate, r0 r0Var) {
        predicate.getClass();
        r0Var.getClass();
        return new s0(U2.REFERENCE, r0Var, new n(2, r0Var, predicate));
    }

    public static Stream V0(c cVar, long j, long j2) {
        if (j >= 0) {
            return new h2(cVar, J0(j2), j, j2);
        }
        throw new IllegalArgumentException("Skip must be non-negative: " + j);
    }

    public static Stream W0(j$.util.Q q, boolean z) {
        q.getClass();
        return new U1(q, T2.c(q), z);
    }

    public static void i0() {
        throw new IllegalStateException("called wrong accept method");
    }

    public static void j0(c2 c2Var, Double d) {
        if (F3.a) {
            F3.a(c2Var.getClass(), "{0} calling Sink.OfDouble.accept(Double)");
            throw null;
        } else {
            c2Var.accept(d.doubleValue());
        }
    }

    public static void l0(d2 d2Var, Integer num) {
        if (F3.a) {
            F3.a(d2Var.getClass(), "{0} calling Sink.OfInt.accept(Integer)");
            throw null;
        } else {
            d2Var.accept(num.intValue());
        }
    }

    public static void n0(e2 e2Var, Long l) {
        if (F3.a) {
            F3.a(e2Var.getClass(), "{0} calling Sink.OfLong.accept(Long)");
            throw null;
        } else {
            e2Var.accept(l.longValue());
        }
    }

    public static void p0() {
        throw new IllegalStateException("called wrong accept method");
    }

    public static void q0() {
        throw new IllegalStateException("called wrong accept method");
    }

    public static Object[] r0(C0 c0, j$.util.function.N n) {
        if (F3.a) {
            F3.a(c0.getClass(), "{0} calling Node.OfPrimitive.asArray");
            throw null;
        } else if (c0.count() < 2147483639) {
            Object[] objArr = (Object[]) n.apply((int) c0.count());
            c0.e(objArr, 0);
            return objArr;
        } else {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
    }

    public static void s0(z0 z0Var, Double[] dArr, int i) {
        if (F3.a) {
            F3.a(z0Var.getClass(), "{0} calling Node.OfDouble.copyInto(Double[], int)");
            throw null;
        }
        double[] dArr2 = (double[]) z0Var.b();
        for (int i2 = 0; i2 < dArr2.length; i2++) {
            dArr[i + i2] = Double.valueOf(dArr2[i2]);
        }
    }

    public static void t0(A0 a0, Integer[] numArr, int i) {
        if (F3.a) {
            F3.a(a0.getClass(), "{0} calling Node.OfInt.copyInto(Integer[], int)");
            throw null;
        }
        int[] iArr = (int[]) a0.b();
        for (int i2 = 0; i2 < iArr.length; i2++) {
            numArr[i + i2] = Integer.valueOf(iArr[i2]);
        }
    }

    public static void u0(B0 b0, Long[] lArr, int i) {
        if (F3.a) {
            F3.a(b0.getClass(), "{0} calling Node.OfInt.copyInto(Long[], int)");
            throw null;
        }
        long[] jArr = (long[]) b0.b();
        for (int i2 = 0; i2 < jArr.length; i2++) {
            lArr[i + i2] = Long.valueOf(jArr[i2]);
        }
    }

    public static void v0(z0 z0Var, Consumer consumer) {
        if (consumer instanceof j$.util.function.m) {
            z0Var.d((j$.util.function.m) consumer);
        } else if (F3.a) {
            F3.a(z0Var.getClass(), "{0} calling Node.OfLong.forEachRemaining(Consumer)");
            throw null;
        } else {
            ((j$.util.E) z0Var.spliterator()).forEachRemaining(consumer);
        }
    }

    public static void w0(A0 a0, Consumer consumer) {
        if (consumer instanceof j$.util.function.K) {
            a0.d((j$.util.function.K) consumer);
        } else if (F3.a) {
            F3.a(a0.getClass(), "{0} calling Node.OfInt.forEachRemaining(Consumer)");
            throw null;
        } else {
            ((j$.util.H) a0.spliterator()).forEachRemaining(consumer);
        }
    }

    public static void x0(B0 b0, Consumer consumer) {
        if (consumer instanceof j$.util.function.h0) {
            b0.d((j$.util.function.h0) consumer);
        } else if (F3.a) {
            F3.a(b0.getClass(), "{0} calling Node.OfLong.forEachRemaining(Consumer)");
            throw null;
        } else {
            ((j$.util.K) b0.spliterator()).forEachRemaining(consumer);
        }
    }

    public static z0 y0(z0 z0Var, long j, long j2) {
        if (j == 0 && j2 == z0Var.count()) {
            return z0Var;
        }
        long j3 = j2 - j;
        j$.util.E e = (j$.util.E) z0Var.spliterator();
        v0 m = u1.m(j3);
        m.f(j3);
        for (int i = 0; i < j && e.o(new j3(1)); i++) {
        }
        for (int i2 = 0; i2 < j3 && e.o(m); i2++) {
        }
        m.end();
        return m.build();
    }

    public static A0 z0(A0 a0, long j, long j2) {
        if (j == 0 && j2 == a0.count()) {
            return a0;
        }
        long j3 = j2 - j;
        j$.util.H h = (j$.util.H) a0.spliterator();
        w0 s = u1.s(j3);
        s.f(j3);
        for (int i = 0; i < j && h.j(new l3(1)); i++) {
        }
        for (int i2 = 0; i2 < j3 && h.j(s); i2++) {
        }
        s.end();
        return s.build();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void F0(j$.util.Q q, f2 f2Var);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void G0(j$.util.Q q, f2 f2Var);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract long I0(j$.util.Q q);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int K0();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract y0 T0(long j, j$.util.function.N n);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract f2 X0(j$.util.Q q, f2 f2Var);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract f2 Y0(f2 f2Var);
}
