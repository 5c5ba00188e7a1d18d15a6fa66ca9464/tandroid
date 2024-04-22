package j$.util;

import j$.util.p;
import j$.util.s;
import java.util.Objects;
/* loaded from: classes2.dex */
public abstract class I {
    private static final s a = new D();
    private static final s.b b = new B();
    private static final s.c c = new C();
    private static final s.a d = new A();

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

    public static s.a b() {
        return d;
    }

    public static s.b c() {
        return b;
    }

    public static s.c d() {
        return c;
    }

    public static s e() {
        return a;
    }

    public static n f(s.a aVar) {
        Objects.requireNonNull(aVar);
        return new x(aVar);
    }

    public static p.a g(s.b bVar) {
        Objects.requireNonNull(bVar);
        return new v(bVar);
    }

    public static p.b h(s.c cVar) {
        Objects.requireNonNull(cVar);
        return new w(cVar);
    }

    public static java.util.Iterator i(s sVar) {
        Objects.requireNonNull(sVar);
        return new u(sVar);
    }

    public static s.a j(double[] dArr, int i, int i2, int i3) {
        Objects.requireNonNull(dArr);
        a(dArr.length, i, i2);
        return new z(dArr, i, i2, i3);
    }

    public static s.b k(int[] iArr, int i, int i2, int i3) {
        Objects.requireNonNull(iArr);
        a(iArr.length, i, i2);
        return new F(iArr, i, i2, i3);
    }

    public static s.c l(long[] jArr, int i, int i2, int i3) {
        Objects.requireNonNull(jArr);
        a(jArr.length, i, i2);
        return new H(jArr, i, i2, i3);
    }

    public static s m(Object[] objArr, int i, int i2, int i3) {
        Objects.requireNonNull(objArr);
        a(objArr.length, i, i2);
        return new y(objArr, i, i2, i3);
    }
}
