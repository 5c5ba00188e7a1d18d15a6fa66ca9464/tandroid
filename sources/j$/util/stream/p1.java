package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Predicate;
import j$.util.t;
import java.util.Objects;
/* loaded from: classes2.dex */
public abstract /* synthetic */ class p1 {
    public static void a(k3 k3Var, Double d) {
        if (R4.a) {
            R4.a(k3Var.getClass(), "{0} calling Sink.OfDouble.accept(Double)");
            throw null;
        } else {
            k3Var.accept(d.doubleValue());
        }
    }

    public static void b(l3 l3Var, Integer num) {
        if (R4.a) {
            R4.a(l3Var.getClass(), "{0} calling Sink.OfInt.accept(Integer)");
            throw null;
        } else {
            l3Var.accept(num.intValue());
        }
    }

    public static void c(m3 m3Var, Long l) {
        if (R4.a) {
            R4.a(m3Var.getClass(), "{0} calling Sink.OfLong.accept(Long)");
            throw null;
        } else {
            m3Var.accept(l.longValue());
        }
    }

    public static void d(n3 n3Var) {
        throw new IllegalStateException("called wrong accept method");
    }

    public static void e(n3 n3Var) {
        throw new IllegalStateException("called wrong accept method");
    }

    public static void f(n3 n3Var) {
        throw new IllegalStateException("called wrong accept method");
    }

    public static Object[] g(A1 a1, j$.util.function.m mVar) {
        if (R4.a) {
            R4.a(a1.getClass(), "{0} calling Node.OfPrimitive.asArray");
            throw null;
        } else if (a1.count() < 2147483639) {
            Object[] objArr = (Object[]) mVar.apply((int) a1.count());
            a1.i(objArr, 0);
            return objArr;
        } else {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
    }

    public static void h(v1 v1Var, Double[] dArr, int i) {
        if (R4.a) {
            R4.a(v1Var.getClass(), "{0} calling Node.OfDouble.copyInto(Double[], int)");
            throw null;
        }
        double[] dArr2 = (double[]) v1Var.e();
        for (int i2 = 0; i2 < dArr2.length; i2++) {
            dArr[i + i2] = Double.valueOf(dArr2[i2]);
        }
    }

    public static void i(x1 x1Var, Integer[] numArr, int i) {
        if (R4.a) {
            R4.a(x1Var.getClass(), "{0} calling Node.OfInt.copyInto(Integer[], int)");
            throw null;
        }
        int[] iArr = (int[]) x1Var.e();
        for (int i2 = 0; i2 < iArr.length; i2++) {
            numArr[i + i2] = Integer.valueOf(iArr[i2]);
        }
    }

    public static void j(z1 z1Var, Long[] lArr, int i) {
        if (R4.a) {
            R4.a(z1Var.getClass(), "{0} calling Node.OfInt.copyInto(Long[], int)");
            throw null;
        }
        long[] jArr = (long[]) z1Var.e();
        for (int i2 = 0; i2 < jArr.length; i2++) {
            lArr[i + i2] = Long.valueOf(jArr[i2]);
        }
    }

    public static void k(v1 v1Var, Consumer consumer) {
        if (consumer instanceof j$.util.function.f) {
            v1Var.g((j$.util.function.f) consumer);
        } else if (R4.a) {
            R4.a(v1Var.getClass(), "{0} calling Node.OfLong.forEachRemaining(Consumer)");
            throw null;
        } else {
            ((t.a) v1Var.spliterator()).forEachRemaining(consumer);
        }
    }

    public static void l(x1 x1Var, Consumer consumer) {
        if (consumer instanceof j$.util.function.l) {
            x1Var.g((j$.util.function.l) consumer);
        } else if (R4.a) {
            R4.a(x1Var.getClass(), "{0} calling Node.OfInt.forEachRemaining(Consumer)");
            throw null;
        } else {
            ((t.b) x1Var.spliterator()).forEachRemaining(consumer);
        }
    }

    public static void m(z1 z1Var, Consumer consumer) {
        if (consumer instanceof j$.util.function.q) {
            z1Var.g((j$.util.function.q) consumer);
        } else if (R4.a) {
            R4.a(z1Var.getClass(), "{0} calling Node.OfLong.forEachRemaining(Consumer)");
            throw null;
        } else {
            ((t.c) z1Var.spliterator()).forEachRemaining(consumer);
        }
    }

    public static v1 n(v1 v1Var, long j, long j2, j$.util.function.m mVar) {
        if (j == 0 && j2 == v1Var.count()) {
            return v1Var;
        }
        long j3 = j2 - j;
        t.a aVar = (t.a) v1Var.spliterator();
        q1 j4 = y2.j(j3);
        j4.n(j3);
        for (int i = 0; i < j && aVar.k(new j$.util.function.f() { // from class: j$.util.stream.u1
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

    public static x1 o(x1 x1Var, long j, long j2, j$.util.function.m mVar) {
        if (j == 0 && j2 == x1Var.count()) {
            return x1Var;
        }
        long j3 = j2 - j;
        t.b bVar = (t.b) x1Var.spliterator();
        r1 p = y2.p(j3);
        p.n(j3);
        for (int i = 0; i < j && bVar.g(new j$.util.function.l() { // from class: j$.util.stream.w1
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

    public static z1 p(z1 z1Var, long j, long j2, j$.util.function.m mVar) {
        if (j == 0 && j2 == z1Var.count()) {
            return z1Var;
        }
        long j3 = j2 - j;
        t.c cVar = (t.c) z1Var.spliterator();
        s1 q = y2.q(j3);
        q.n(j3);
        for (int i = 0; i < j && cVar.i(new j$.util.function.q() { // from class: j$.util.stream.y1
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

    public static B1 q(B1 b1, long j, long j2, j$.util.function.m mVar) {
        if (j == 0 && j2 == b1.count()) {
            return b1;
        }
        j$.util.t spliterator = b1.spliterator();
        long j3 = j2 - j;
        t1 d = y2.d(j3, mVar);
        d.n(j3);
        for (int i = 0; i < j && spliterator.b(new Consumer() { // from class: j$.util.stream.o1
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

    public static V r(t.a aVar, boolean z) {
        return new Q(aVar, e4.c(aVar), z);
    }

    public static IntStream s(t.b bVar, boolean z) {
        return new J0(bVar, e4.c(bVar), z);
    }

    public static f1 t(t.c cVar, boolean z) {
        return new b1(cVar, e4.c(cVar), z);
    }

    public static O4 u(j$.wrappers.D d, l1 l1Var) {
        Objects.requireNonNull(d);
        Objects.requireNonNull(l1Var);
        return new m1(f4.DOUBLE_VALUE, l1Var, new p(l1Var, d));
    }

    public static O4 v(j$.wrappers.U u, l1 l1Var) {
        Objects.requireNonNull(u);
        Objects.requireNonNull(l1Var);
        return new m1(f4.INT_VALUE, l1Var, new p(l1Var, u));
    }

    public static O4 w(j$.wrappers.i0 i0Var, l1 l1Var) {
        Objects.requireNonNull(i0Var);
        Objects.requireNonNull(l1Var);
        return new m1(f4.LONG_VALUE, l1Var, new p(l1Var, i0Var));
    }

    public static O4 x(Predicate predicate, l1 l1Var) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(l1Var);
        return new m1(f4.REFERENCE, l1Var, new p(l1Var, predicate));
    }

    public static Stream y(j$.util.t tVar, boolean z) {
        Objects.requireNonNull(tVar);
        return new c3(tVar, e4.c(tVar), z);
    }
}
