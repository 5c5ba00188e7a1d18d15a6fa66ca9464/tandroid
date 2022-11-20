package j$.util;

import j$.util.p;
import j$.util.u;
import java.util.Objects;
/* loaded from: classes2.dex */
public abstract class L {
    private static final u a = new G();
    private static final u.a b = new E();
    private static final v c = new F();
    private static final t d = new D();

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

    public static t b() {
        return d;
    }

    public static u.a c() {
        return b;
    }

    public static v d() {
        return c;
    }

    public static u e() {
        return a;
    }

    public static n f(t tVar) {
        Objects.requireNonNull(tVar);
        return new A(tVar);
    }

    public static p.a g(u.a aVar) {
        Objects.requireNonNull(aVar);
        return new y(aVar);
    }

    public static r h(v vVar) {
        Objects.requireNonNull(vVar);
        return new z(vVar);
    }

    public static java.util.Iterator i(u uVar) {
        Objects.requireNonNull(uVar);
        return new x(uVar);
    }

    public static t j(double[] dArr, int i, int i2, int i3) {
        Objects.requireNonNull(dArr);
        a(dArr.length, i, i2);
        return new C(dArr, i, i2, i3);
    }

    public static u.a k(int[] iArr, int i, int i2, int i3) {
        Objects.requireNonNull(iArr);
        a(iArr.length, i, i2);
        return new I(iArr, i, i2, i3);
    }

    public static v l(long[] jArr, int i, int i2, int i3) {
        Objects.requireNonNull(jArr);
        a(jArr.length, i, i2);
        return new K(jArr, i, i2, i3);
    }

    public static u m(Object[] objArr, int i, int i2, int i3) {
        Objects.requireNonNull(objArr);
        a(objArr.length, i, i2);
        return new B(objArr, i, i2, i3);
    }
}
