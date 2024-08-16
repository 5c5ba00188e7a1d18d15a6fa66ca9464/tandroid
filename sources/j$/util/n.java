package j$.util;

import java.util.NoSuchElementException;
/* loaded from: classes2.dex */
public final class n {
    private static final n c = new n();
    private final boolean a;
    private final long b;

    private n() {
        this.a = false;
        this.b = 0L;
    }

    private n(long j) {
        this.a = true;
        this.b = j;
    }

    public static n a() {
        return c;
    }

    public static n d(long j) {
        return new n(j);
    }

    public final long b() {
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
        if (obj instanceof n) {
            n nVar = (n) obj;
            boolean z = this.a;
            if (z && nVar.a) {
                if (this.b == nVar.b) {
                    return true;
                }
            } else if (z == nVar.a) {
                return true;
            }
            return false;
        }
        return false;
    }

    public final int hashCode() {
        if (this.a) {
            long j = this.b;
            return (int) (j ^ (j >>> 32));
        }
        return 0;
    }

    public final String toString() {
        if (this.a) {
            return "OptionalLong[" + this.b + "]";
        }
        return "OptionalLong.empty";
    }
}
