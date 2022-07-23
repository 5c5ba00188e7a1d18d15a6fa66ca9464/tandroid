package j$.util;

import java.util.NoSuchElementException;
/* loaded from: classes2.dex */
public final class l {
    private static final l c = new l();
    private final boolean a;
    private final long b;

    private l() {
        this.a = false;
        this.b = 0L;
    }

    private l(long j) {
        this.a = true;
        this.b = j;
    }

    public static l a() {
        return c;
    }

    public static l d(long j) {
        return new l(j);
    }

    public long b() {
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
        if (!(obj instanceof l)) {
            return false;
        }
        l lVar = (l) obj;
        boolean z = this.a;
        if (!z || !lVar.a) {
            if (z == lVar.a) {
                return true;
            }
        } else if (this.b == lVar.b) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (this.a) {
            long j = this.b;
            return (int) (j ^ (j >>> 32));
        }
        return 0;
    }

    public String toString() {
        return this.a ? String.format("OptionalLong[%s]", Long.valueOf(this.b)) : "OptionalLong.empty";
    }
}
