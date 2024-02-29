package j$.time.temporal;

import java.io.Serializable;
/* loaded from: classes2.dex */
public final class x implements Serializable {
    private final long a;
    private final long b;
    private final long c;
    private final long d;

    private x(long j, long j2, long j3, long j4) {
        this.a = j;
        this.b = j2;
        this.c = j3;
        this.d = j4;
    }

    private String c(l lVar, long j) {
        if (lVar == null) {
            return "Invalid value (valid values " + this + "): " + j;
        }
        return "Invalid value for " + lVar + " (valid values " + this + "): " + j;
    }

    public static x i(long j, long j2) {
        if (j <= j2) {
            return new x(j, j, j2, j2);
        }
        throw new IllegalArgumentException("Minimum value must be less than maximum value");
    }

    public static x j(long j, long j2, long j3) {
        if (j <= j) {
            if (j2 <= j3) {
                if (j <= j3) {
                    return new x(j, j, j2, j3);
                }
                throw new IllegalArgumentException("Minimum value must be less than maximum value");
            }
            throw new IllegalArgumentException("Smallest maximum value must be less than largest maximum value");
        }
        throw new IllegalArgumentException("Smallest minimum value must be less than largest minimum value");
    }

    public int a(long j, l lVar) {
        if (g() && h(j)) {
            return (int) j;
        }
        throw new j$.time.c(c(lVar, j));
    }

    public long b(long j, l lVar) {
        if (h(j)) {
            return j;
        }
        throw new j$.time.c(c(lVar, j));
    }

    public long d() {
        return this.d;
    }

    public long e() {
        return this.a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof x) {
            x xVar = (x) obj;
            return this.a == xVar.a && this.b == xVar.b && this.c == xVar.c && this.d == xVar.d;
        }
        return false;
    }

    public boolean f() {
        return this.a == this.b && this.c == this.d;
    }

    public boolean g() {
        return this.a >= -2147483648L && this.d <= 2147483647L;
    }

    public boolean h(long j) {
        return j >= this.a && j <= this.d;
    }

    public int hashCode() {
        long j = this.a;
        long j2 = this.b;
        long j3 = j + (j2 << 16) + (j2 >> 48);
        long j4 = this.c;
        long j5 = j3 + (j4 << 32) + (j4 >> 32);
        long j6 = this.d;
        long j7 = j5 + (j6 << 48) + (j6 >> 16);
        return (int) (j7 ^ (j7 >>> 32));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.a);
        if (this.a != this.b) {
            sb.append('/');
            sb.append(this.b);
        }
        sb.append(" - ");
        sb.append(this.c);
        if (this.c != this.d) {
            sb.append('/');
            sb.append(this.d);
        }
        return sb.toString();
    }
}
