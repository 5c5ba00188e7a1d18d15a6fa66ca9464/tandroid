package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Predicate;
import j$.util.s;
import java.util.Objects;
/* loaded from: classes2.dex */
public abstract /* synthetic */ class o1 {
    public static void a(j3 j3Var, Double d) {
        if (Q4.a) {
            Q4.a(j3Var.getClass(), "{0} calling Sink.OfDouble.accept(Double)");
            throw null;
        } else {
            j3Var.accept(d.doubleValue());
        }
    }

    public static void b(k3 k3Var, Integer num) {
        if (Q4.a) {
            Q4.a(k3Var.getClass(), "{0} calling Sink.OfInt.accept(Integer)");
            throw null;
        } else {
            k3Var.accept(num.intValue());
        }
    }

    public static void c(l3 l3Var, Long l) {
        if (Q4.a) {
            Q4.a(l3Var.getClass(), "{0} calling Sink.OfLong.accept(Long)");
            throw null;
        } else {
            l3Var.accept(l.longValue());
        }
    }

    public static void d(m3 m3Var) {
        throw new IllegalStateException("called wrong accept method");
    }

    public static void e(m3 m3Var) {
        throw new IllegalStateException("called wrong accept method");
    }

    public static void f(m3 m3Var) {
        throw new IllegalStateException("called wrong accept method");
    }

    public static Object[] g(z1 z1Var, j$.util.function.m mVar) {
        if (Q4.a) {
            Q4.a(z1Var.getClass(), "{0} calling Node.OfPrimitive.asArray");
            throw null;
        } else if (z1Var.count() < 2147483639) {
            Object[] objArr = (Object[]) mVar.apply((int) z1Var.count());
            z1Var.i(objArr, 0);
            return objArr;
        } else {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
    }

    public static void h(u1 u1Var, Double[] dArr, int i) {
        if (Q4.a) {
            Q4.a(u1Var.getClass(), "{0} calling Node.OfDouble.copyInto(Double[], int)");
            throw null;
        }
        double[] dArr2 = (double[]) u1Var.e();
        for (int i2 = 0; i2 < dArr2.length; i2++) {
            dArr[i + i2] = Double.valueOf(dArr2[i2]);
        }
    }

    public static void i(w1 w1Var, Integer[] numArr, int i) {
        if (Q4.a) {
            Q4.a(w1Var.getClass(), "{0} calling Node.OfInt.copyInto(Integer[], int)");
            throw null;
        }
        int[] iArr = (int[]) w1Var.e();
        for (int i2 = 0; i2 < iArr.length; i2++) {
            numArr[i + i2] = Integer.valueOf(iArr[i2]);
        }
    }

    public static void j(y1 y1Var, Long[] lArr, int i) {
        if (Q4.a) {
            Q4.a(y1Var.getClass(), "{0} calling Node.OfInt.copyInto(Long[], int)");
            throw null;
        }
        long[] jArr = (long[]) y1Var.e();
        for (int i2 = 0; i2 < jArr.length; i2++) {
            lArr[i + i2] = Long.valueOf(jArr[i2]);
        }
    }

    public static void k(u1 u1Var, Consumer consumer) {
        if (consumer instanceof j$.util.function.f) {
            u1Var.g((j$.util.function.f) consumer);
        } else if (Q4.a) {
            Q4.a(u1Var.getClass(), "{0} calling Node.OfLong.forEachRemaining(Consumer)");
            throw null;
        } else {
            ((s.a) u1Var.spliterator()).forEachRemaining(consumer);
        }
    }

    public static void l(w1 w1Var, Consumer consumer) {
        if (consumer instanceof j$.util.function.l) {
            w1Var.g((j$.util.function.l) consumer);
        } else if (Q4.a) {
            Q4.a(w1Var.getClass(), "{0} calling Node.OfInt.forEachRemaining(Consumer)");
            throw null;
        } else {
            ((s.b) w1Var.spliterator()).forEachRemaining(consumer);
        }
    }

    public static void m(y1 y1Var, Consumer consumer) {
        if (consumer instanceof j$.util.function.q) {
            y1Var.g((j$.util.function.q) consumer);
        } else if (Q4.a) {
            Q4.a(y1Var.getClass(), "{0} calling Node.OfLong.forEachRemaining(Consumer)");
            throw null;
        } else {
            ((s.c) y1Var.spliterator()).forEachRemaining(consumer);
        }
    }

    public static u1 n(u1 u1Var, long j, long j2, j$.util.function.m mVar) {
        if (j == 0 && j2 == u1Var.count()) {
            return u1Var;
        }
        long j3 = j2 - j;
        s.a aVar = (s.a) u1Var.spliterator();
        p1 j4 = x2.j(j3);
        j4.n(j3);
        for (int i = 0; i < j && aVar.k(new j$.util.function.f() { // from class: j$.util.stream.t1
            @Override // j$.util.function.f
            public final void accept(double d) {
            }

            @Override // j$.util.function.f
            public j$.util.function.f j(j$.util.function.f fVar) {
                Objects.requireNonNull(fVar);
                return new j$.util.function.e(this, fVar);
            }
        }); i++) {
        }
        for (int i2 = 0; i2 < j3 && aVar.k(j4); i2++) {
        }
        j4.m();
        return j4.a();
    }

    public static w1 o(w1 w1Var, long j, long j2, j$.util.function.m mVar) {
        if (j == 0 && j2 == w1Var.count()) {
            return w1Var;
        }
        long j3 = j2 - j;
        s.b bVar = (s.b) w1Var.spliterator();
        q1 p = x2.p(j3);
        p.n(j3);
        for (int i = 0; i < j && bVar.g(new j$.util.function.l() { // from class: j$.util.stream.v1
            @Override // j$.util.function.l
            public final void accept(int i2) {
            }

            @Override // j$.util.function.l
            public j$.util.function.l l(j$.util.function.l lVar) {
                Objects.requireNonNull(lVar);
                return new j$.util.function.k(this, lVar);
            }
        }); i++) {
        }
        for (int i2 = 0; i2 < j3 && bVar.g(p); i2++) {
        }
        p.m();
        return p.a();
    }

    public static y1 p(y1 y1Var, long j, long j2, j$.util.function.m mVar) {
        if (j == 0 && j2 == y1Var.count()) {
            return y1Var;
        }
        long j3 = j2 - j;
        s.c cVar = (s.c) y1Var.spliterator();
        r1 q = x2.q(j3);
        q.n(j3);
        for (int i = 0; i < j && cVar.i(new j$.util.function.q() { // from class: j$.util.stream.x1
            @Override // j$.util.function.q
            public final void accept(long j4) {
            }

            @Override // j$.util.function.q
            public j$.util.function.q f(j$.util.function.q qVar) {
                Objects.requireNonNull(qVar);
                return new j$.util.function.p(this, qVar);
            }
        }); i++) {
        }
        for (int i2 = 0; i2 < j3 && cVar.i(q); i2++) {
        }
        q.m();
        return q.a();
    }

    public static A1 q(A1 a1, long j, long j2, j$.util.function.m mVar) {
        if (j == 0 && j2 == a1.count()) {
            return a1;
        }
        j$.util.s spliterator = a1.spliterator();
        long j3 = j2 - j;
        s1 d = x2.d(j3, mVar);
        d.n(j3);
        for (int i = 0; i < j && spliterator.b(new Consumer() { // from class: j$.util.stream.n1
            @Override // j$.util.function.Consumer
            public final void accept(Object obj) {
            }

            @Override // j$.util.function.Consumer
            public /* synthetic */ Consumer andThen(Consumer consumer) {
                return Consumer.-CC.$default$andThen(this, consumer);
            }
        }); i++) {
        }
        for (int i2 = 0; i2 < j3 && spliterator.b(d); i2++) {
        }
        d.m();
        return d.a();
    }

    public static V r(s.a aVar, boolean z) {
        return new Q(aVar, d4.c(aVar), z);
    }

    public static IntStream s(s.b bVar, boolean z) {
        return new J0(bVar, d4.c(bVar), z);
    }

    public static LongStream t(s.c cVar, boolean z) {
        return new b1(cVar, d4.c(cVar), z);
    }

    public static N4 u(j$.wrappers.D d, k1 k1Var) {
        Objects.requireNonNull(d);
        Objects.requireNonNull(k1Var);
        return new l1(e4.DOUBLE_VALUE, k1Var, new p(k1Var, d));
    }

    public static N4 v(j$.wrappers.U u, k1 k1Var) {
        Objects.requireNonNull(u);
        Objects.requireNonNull(k1Var);
        return new l1(e4.INT_VALUE, k1Var, new p(k1Var, u));
    }

    public static N4 w(j$.wrappers.i0 i0Var, k1 k1Var) {
        Objects.requireNonNull(i0Var);
        Objects.requireNonNull(k1Var);
        return new l1(e4.LONG_VALUE, k1Var, new p(k1Var, i0Var));
    }

    public static N4 x(Predicate predicate, k1 k1Var) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(k1Var);
        return new l1(e4.REFERENCE, k1Var, new p(k1Var, predicate));
    }

    public static Stream y(j$.util.s sVar, boolean z) {
        Objects.requireNonNull(sVar);
        return new b3(sVar, d4.c(sVar), z);
    }
}
