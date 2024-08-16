package j$.util;

import java.util.NoSuchElementException;
/* loaded from: classes2.dex */
public final class m {
    private static final m c = new m();
    private final boolean a;
    private final int b;

    private m() {
        this.a = false;
        this.b = 0;
    }

    private m(int i) {
        this.a = true;
        this.b = i;
    }

    public static m a() {
        return c;
    }

    public static m d(int i) {
        return new m(i);
    }

    public final int b() {
        if (this.a) {
            return this.b;
        }
        throw new NoSuchElementException("No value present");
    }

    public final boolean c() {
        return this.a;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof m) {
            m mVar = (m) obj;
            boolean z = this.a;
            if (z && mVar.a) {
                if (this.b == mVar.b) {
                    return true;
                }
            } else if (z == mVar.a) {
                return true;
            }
            return false;
        }
        return false;
    }

    public final int hashCode() {
        if (this.a) {
            return this.b;
        }
        return 0;
    }

    public final String toString() {
        if (this.a) {
            return "OptionalInt[" + this.b + "]";
        }
        return "OptionalInt.empty";
    }
}
