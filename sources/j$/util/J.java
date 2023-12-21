package j$.util;

import j$.util.p;
import j$.util.t;
import java.util.Objects;
/* loaded from: classes2.dex */
public abstract class J {
    private static final t a = new E();
    private static final t.b b = new C();
    private static final t.c c = new D();
    private static final t.a d = new B();

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

    public static t.a b() {
        return d;
    }

    public static t.b c() {
        return b;
    }

    public static t.c d() {
        return c;
    }

    public static t e() {
        return a;
    }

    public static n f(t.a aVar) {
        Objects.requireNonNull(aVar);
        return new y(aVar);
    }

    public static p.a g(t.b bVar) {
        Objects.requireNonNull(bVar);
        return new w(bVar);
    }

    public static r h(t.c cVar) {
        Objects.requireNonNull(cVar);
        return new x(cVar);
    }

    public static java.util.Iterator i(t tVar) {
        Objects.requireNonNull(tVar);
        return new v(tVar);
    }

    public static t.a j(double[] dArr, int i, int i2, int i3) {
        Objects.requireNonNull(dArr);
        a(dArr.length, i, i2);
        return new A(dArr, i, i2, i3);
    }

    public static t.b k(int[] iArr, int i, int i2, int i3) {
        Objects.requireNonNull(iArr);
        a(iArr.length, i, i2);
        return new G(iArr, i, i2, i3);
    }

    public static t.c l(long[] jArr, int i, int i2, int i3) {
        Objects.requireNonNull(jArr);
        a(jArr.length, i, i2);
        return new I(jArr, i, i2, i3);
    }

    public static t m(Object[] objArr, int i, int i2, int i3) {
        Objects.requireNonNull(objArr);
        a(objArr.length, i, i2);
        return new z(objArr, i, i2, i3);
    }
}
