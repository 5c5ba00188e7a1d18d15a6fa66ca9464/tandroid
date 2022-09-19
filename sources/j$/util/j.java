package j$.util;

import java.util.NoSuchElementException;
/* loaded from: classes2.dex */
public final class j {
    private static final j c = new j();
    private final boolean a;
    private final double b;

    private j() {
        this.a = false;
        this.b = Double.NaN;
    }

    private j(double d) {
        this.a = true;
        this.b = d;
    }

    public static j a() {
        return c;
    }

    public static j d(double d) {
        return new j(d);
    }

    public double b() {
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
        if (!(obj instanceof j)) {
            return false;
        }
        j jVar = (j) obj;
        boolean z = this.a;
        if (!z || !jVar.a) {
            if (z == jVar.a) {
                return true;
            }
        } else if (Double.compare(this.b, jVar.b) == 0) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (this.a) {
            long doubleToLongBits = Double.doubleToLongBits(this.b);
            return (int) (doubleToLongBits ^ (doubleToLongBits >>> 32));
        }
        return 0;
    }

    public String toString() {
        return this.a ? String.format("OptionalDouble[%s]", Double.valueOf(this.b)) : "OptionalDouble.empty";
    }
}
