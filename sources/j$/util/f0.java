package j$.util;

import java.util.Iterator;

/* loaded from: classes2.dex */
public abstract class f0 {
    private static final Q a = new b0();
    private static final H b = new Z();
    private static final K c = new a0();
    private static final E d = new Y();

    private static void a(int i, int i2, int i3) {
        if (i2 <= i3) {
            if (i2 < 0) {
                throw new ArrayIndexOutOfBoundsException(i2);
            }
            if (i3 > i) {
                throw new ArrayIndexOutOfBoundsException(i3);
            }
            return;
        }
        throw new ArrayIndexOutOfBoundsException("origin(" + i2 + ") > fence(" + i3 + ")");
    }

    public static E b() {
        return d;
    }

    public static H c() {
        return b;
    }

    public static K d() {
        return c;
    }

    public static Q e() {
        return a;
    }

    public static r f(E e) {
        e.getClass();
        return new V(e);
    }

    public static v g(H h) {
        h.getClass();
        return new T(h);
    }

    public static z h(K k) {
        k.getClass();
        return new U(k);
    }

    public static Iterator i(Q q) {
        q.getClass();
        return new S(q);
    }

    public static E j(double[] dArr, int i, int i2) {
        dArr.getClass();
        a(dArr.length, i, i2);
        return new X(dArr, i, i2, 1040);
    }

    public static H k(int[] iArr, int i, int i2) {
        iArr.getClass();
        a(iArr.length, i, i2);
        return new c0(iArr, i, i2, 1040);
    }

    public static K l(long[] jArr, int i, int i2) {
        jArr.getClass();
        a(jArr.length, i, i2);
        return new e0(jArr, i, i2, 1040);
    }

    public static Q m(Object[] objArr, int i, int i2) {
        objArr.getClass();
        a(objArr.length, i, i2);
        return new W(objArr, i, i2, 1040);
    }
}
