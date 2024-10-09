package j$.util;

import java.util.NoSuchElementException;

/* loaded from: classes2.dex */
public final class l {
    private static final l c = new l();
    private final boolean a;
    private final double b;

    private l() {
        this.a = false;
        this.b = Double.NaN;
    }

    private l(double d) {
        this.a = true;
        this.b = d;
    }

    public static l a() {
        return c;
    }

    public static l d(double d) {
        return new l(d);
    }

    public final double b() {
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
        if (!(obj instanceof l)) {
            return false;
        }
        l lVar = (l) obj;
        boolean z = this.a;
        if (z && lVar.a) {
            if (Double.compare(this.b, lVar.b) == 0) {
                return true;
            }
        } else if (z == lVar.a) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        if (!this.a) {
            return 0;
        }
        long doubleToLongBits = Double.doubleToLongBits(this.b);
        return (int) (doubleToLongBits ^ (doubleToLongBits >>> 32));
    }

    public final String toString() {
        if (!this.a) {
            return "OptionalDouble.empty";
        }
        return "OptionalDouble[" + this.b + "]";
    }
}
