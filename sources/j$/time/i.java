package j$.time;

import j$.time.temporal.p;
import j$.time.temporal.q;
import j$.time.temporal.r;
import j$.time.temporal.s;
import j$.time.temporal.t;
import j$.time.temporal.u;
import j$.time.temporal.w;
import j$.time.temporal.x;
import java.io.Serializable;
import org.telegram.messenger.MediaController;
/* loaded from: classes2.dex */
public final class i implements j$.time.temporal.k, Comparable, Serializable {
    public static final i e;
    public static final i f;
    private static final i[] g = new i[24];
    private final byte a;
    private final byte b;
    private final byte c;
    private final int d;

    static {
        int i = 0;
        while (true) {
            i[] iVarArr = g;
            if (i >= iVarArr.length) {
                i iVar = iVarArr[0];
                i iVar2 = iVarArr[12];
                e = iVarArr[0];
                f = new i(23, 59, 59, 999999999);
                return;
            }
            iVarArr[i] = new i(i, 0, 0, 0);
            i++;
        }
    }

    private i(int i, int i2, int i3, int i4) {
        this.a = (byte) i;
        this.b = (byte) i2;
        this.c = (byte) i3;
        this.d = i4;
    }

    private int g(j$.time.temporal.l lVar) {
        switch (h.a[((j$.time.temporal.a) lVar).ordinal()]) {
            case 1:
                return this.d;
            case 2:
                throw new w("Invalid field 'NanoOfDay' for get() method, use getLong() instead");
            case 3:
                return this.d / 1000;
            case 4:
                throw new w("Invalid field 'MicroOfDay' for get() method, use getLong() instead");
            case 5:
                return this.d / MediaController.VIDEO_BITRATE_480;
            case 6:
                return (int) (l() / 1000000);
            case 7:
                return this.c;
            case 8:
                return m();
            case 9:
                return this.b;
            case 10:
                return (this.a * 60) + this.b;
            case 11:
                return this.a % 12;
            case 12:
                int i = this.a % 12;
                if (i % 12 == 0) {
                    return 12;
                }
                return i;
            case 13:
                return this.a;
            case 14:
                byte b = this.a;
                if (b == 0) {
                    return 24;
                }
                return b;
            case 15:
                return this.a / 12;
            default:
                throw new w("Unsupported field: " + lVar);
        }
    }

    public static i j(int i, int i2) {
        j$.time.temporal.a.HOUR_OF_DAY.h(i);
        if (i2 == 0) {
            return g[i];
        }
        j$.time.temporal.a.MINUTE_OF_HOUR.h(i2);
        return new i(i, i2, 0, 0);
    }

    public static i k(long j) {
        j$.time.temporal.a.NANO_OF_DAY.h(j);
        int i = (int) (j / 3600000000000L);
        long j2 = j - (i * 3600000000000L);
        int i2 = (int) (j2 / 60000000000L);
        long j3 = j2 - (i2 * 60000000000L);
        int i3 = (int) (j3 / 1000000000);
        int i4 = (int) (j3 - (i3 * 1000000000));
        return ((i2 | i3) | i4) == 0 ? g[i] : new i(i, i2, i3, i4);
    }

    @Override // j$.time.temporal.k
    public int a(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? g(lVar) : j$.time.temporal.j.a(this, lVar);
    }

    @Override // j$.time.temporal.k
    public x b(j$.time.temporal.l lVar) {
        return j$.time.temporal.j.c(this, lVar);
    }

    @Override // j$.time.temporal.k
    public long c(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? lVar == j$.time.temporal.a.NANO_OF_DAY ? l() : lVar == j$.time.temporal.a.MICRO_OF_DAY ? l() / 1000 : g(lVar) : lVar.c(this);
    }

    @Override // j$.time.temporal.k
    public Object d(u uVar) {
        int i = t.a;
        if (uVar == j$.time.temporal.n.a || uVar == j$.time.temporal.m.a || uVar == q.a || uVar == p.a) {
            return null;
        }
        if (uVar == s.a) {
            return this;
        }
        if (uVar == r.a) {
            return null;
        }
        return uVar == j$.time.temporal.o.a ? j$.time.temporal.b.NANOS : uVar.a(this);
    }

    @Override // j$.time.temporal.k
    public boolean e(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? lVar.b() : lVar != null && lVar.d(this);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof i) {
            i iVar = (i) obj;
            return this.a == iVar.a && this.b == iVar.b && this.c == iVar.c && this.d == iVar.d;
        }
        return false;
    }

    @Override // java.lang.Comparable
    /* renamed from: f */
    public int compareTo(i iVar) {
        int compare = Integer.compare(this.a, iVar.a);
        if (compare == 0) {
            int compare2 = Integer.compare(this.b, iVar.b);
            if (compare2 == 0) {
                int compare3 = Integer.compare(this.c, iVar.c);
                return compare3 == 0 ? Integer.compare(this.d, iVar.d) : compare3;
            }
            return compare2;
        }
        return compare;
    }

    public int h() {
        return this.d;
    }

    public int hashCode() {
        long l = l();
        return (int) (l ^ (l >>> 32));
    }

    public int i() {
        return this.c;
    }

    public long l() {
        return (this.c * 1000000000) + (this.b * 60000000000L) + (this.a * 3600000000000L) + this.d;
    }

    public int m() {
        return (this.b * 60) + (this.a * 3600) + this.c;
    }

    public String toString() {
        int i;
        StringBuilder sb = new StringBuilder(18);
        byte b = this.a;
        byte b2 = this.b;
        byte b3 = this.c;
        int i2 = this.d;
        sb.append(b < 10 ? "0" : "");
        sb.append((int) b);
        sb.append(b2 < 10 ? ":0" : ":");
        sb.append((int) b2);
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
