package j$.util;

import java.util.NoSuchElementException;
/* loaded from: classes2.dex */
public final class k {
    private static final k c = new k();
    private final boolean a;
    private final int b;

    private k() {
        this.a = false;
        this.b = 0;
    }

    private k(int i) {
        this.a = true;
        this.b = i;
    }

    public static k a() {
        return c;
    }

    public static k d(int i) {
        return new k(i);
    }

    public int b() {
        if (this.a) {
            return this.b;
        }
        throw new NoSuchElementException("No value present");
    }

    public boolean c() {
        return this.a;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof k)) {
            return false;
        }
        k kVar = (k) obj;
        boolean z = this.a;
        if (!z || !kVar.a) {
            if (z == kVar.a) {
                return true;
            }
        } else if (this.b == kVar.b) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (this.a) {
            return this.b;
        }
        return 0;
    }

    public String toString() {
        return this.a ? String.format("OptionalInt[%s]", Integer.valueOf(this.b)) : "OptionalInt.empty";
    }
}
