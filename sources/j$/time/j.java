package j$.time;

import j$.time.temporal.p;
import j$.time.temporal.q;
import java.io.Serializable;
import org.telegram.messenger.MediaController;
/* loaded from: classes2.dex */
public final class j implements j$.time.temporal.k, Comparable, Serializable {
    public static final j e;
    public static final j f;
    private static final j[] g = new j[24];
    private final byte a;
    private final byte b;
    private final byte c;
    private final int d;

    static {
        int i = 0;
        while (true) {
            j[] jVarArr = g;
            if (i >= jVarArr.length) {
                j jVar = jVarArr[0];
                j jVar2 = jVarArr[12];
                e = jVar;
                f = new j(23, 59, 59, 999999999);
                return;
            }
            jVarArr[i] = new j(i, 0, 0, 0);
            i++;
        }
    }

    private j(int i, int i2, int i3, int i4) {
        this.a = (byte) i;
        this.b = (byte) i2;
        this.c = (byte) i3;
        this.d = i4;
    }

    private int g(j$.time.temporal.l lVar) {
        int i = i.a[((j$.time.temporal.a) lVar).ordinal()];
        byte b = this.b;
        int i2 = this.d;
        byte b2 = this.a;
        switch (i) {
            case 1:
                return i2;
            case 2:
                throw new p("Invalid field 'NanoOfDay' for get() method, use getLong() instead");
            case 3:
                return i2 / 1000;
            case 4:
                throw new p("Invalid field 'MicroOfDay' for get() method, use getLong() instead");
            case 5:
                return i2 / MediaController.VIDEO_BITRATE_480;
            case 6:
                return (int) (l() / 1000000);
            case 7:
                return this.c;
            case 8:
                return m();
            case 9:
                return b;
            case 10:
                return (b2 * 60) + b;
            case 11:
                return b2 % 12;
            case 12:
                int i3 = b2 % 12;
                if (i3 % 12 == 0) {
                    return 12;
                }
                return i3;
            case 13:
                return b2;
            case 14:
                if (b2 == 0) {
                    return 24;
                }
                return b2;
            case 15:
                return b2 / 12;
            default:
                throw new p("Unsupported field: " + lVar);
        }
    }

    public static j j() {
        j$.time.temporal.a.HOUR_OF_DAY.f(0);
        return g[0];
    }

    public static j k(long j) {
        j$.time.temporal.a.NANO_OF_DAY.f(j);
        int i = (int) (j / 3600000000000L);
        long j2 = j - (i * 3600000000000L);
        int i2 = (int) (j2 / 60000000000L);
        long j3 = j2 - (i2 * 60000000000L);
        int i3 = (int) (j3 / 1000000000);
        int i4 = (int) (j3 - (i3 * 1000000000));
        return ((i2 | i3) | i4) == 0 ? g[i] : new j(i, i2, i3, i4);
    }

    @Override // j$.time.temporal.k
    public final q a(j$.time.temporal.l lVar) {
        return j$.time.temporal.j.c(this, lVar);
    }

    @Override // j$.time.temporal.k
    public final boolean b(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? lVar.isTimeBased() : lVar != null && lVar.a(this);
    }

    @Override // j$.time.temporal.k
    public final long c(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? lVar == j$.time.temporal.a.NANO_OF_DAY ? l() : lVar == j$.time.temporal.a.MICRO_OF_DAY ? l() / 1000 : g(lVar) : lVar.d(this);
    }

    @Override // j$.time.temporal.k
    public final Object d(j$.time.temporal.n nVar) {
        if (nVar == j$.time.temporal.j.d() || nVar == j$.time.temporal.j.j() || nVar == j$.time.temporal.j.i() || nVar == j$.time.temporal.j.g()) {
            return null;
        }
        if (nVar == j$.time.temporal.j.f()) {
            return this;
        }
        if (nVar == j$.time.temporal.j.e()) {
            return null;
        }
        return nVar == j$.time.temporal.j.h() ? j$.time.temporal.b.NANOS : nVar.a(this);
    }

    @Override // j$.time.temporal.k
    public final int e(j$.time.temporal.a aVar) {
        return aVar instanceof j$.time.temporal.a ? g(aVar) : j$.time.temporal.j.a(this, aVar);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof j) {
            j jVar = (j) obj;
            return this.a == jVar.a && this.b == jVar.b && this.c == jVar.c && this.d == jVar.d;
        }
        return false;
    }

    @Override // java.lang.Comparable
    /* renamed from: f */
    public final int compareTo(j jVar) {
        int compare = Integer.compare(this.a, jVar.a);
        if (compare == 0) {
            int compare2 = Integer.compare(this.b, jVar.b);
            if (compare2 == 0) {
                int compare3 = Integer.compare(this.c, jVar.c);
                return compare3 == 0 ? Integer.compare(this.d, jVar.d) : compare3;
            }
            return compare2;
        }
        return compare;
    }

    public final int h() {
        return this.d;
    }

    public final int hashCode() {
        long l = l();
        return (int) (l ^ (l >>> 32));
    }

    public final int i() {
        return this.c;
    }

    public final long l() {
        return (this.c * 1000000000) + (this.b * 60000000000L) + (this.a * 3600000000000L) + this.d;
    }

    public final int m() {
        return (this.b * 60) + (this.a * 3600) + this.c;
    }

    public final String toString() {
        int i;
        StringBuilder sb = new StringBuilder(18);
        byte b = this.a;
        sb.append(b < 10 ? "0" : "");
        sb.append((int) b);
        byte b2 = this.b;
        sb.append(b2 < 10 ? ":0" : ":");
        sb.append((int) b2);
        byte b3 = this.c;
        int i2 = this.d;
        if (b3 > 0 || i2 > 0) {
            sb.append(b3 >= 10 ? ":" : ":0");
            sb.append((int) b3);
            if (i2 > 0) {
                sb.append('.');
                int i3 = MediaController.VIDEO_BITRATE_480;
                if (i2 % MediaController.VIDEO_BITRATE_480 == 0) {
                    i = (i2 / MediaController.VIDEO_BITRATE_480) + 1000;
                } else {
                    if (i2 % 1000 == 0) {
                        i2 /= 1000;
                    } else {
                        i3 = 1000000000;
                    }
                    i = i2 + i3;
                }
                sb.append(Integer.toString(i).substring(1));
            }
        }
        return sb.toString();
    }
}
