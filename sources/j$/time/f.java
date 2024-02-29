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
import java.util.Objects;
/* loaded from: classes2.dex */
public final class f implements j$.time.temporal.k, j$.time.chrono.b, Serializable {
    public static final f d = n(-999999999, 1, 1);
    public static final f e = n(999999999, 12, 31);
    private final int a;
    private final short b;
    private final short c;

    private f(int i, int i2, int i3) {
        this.a = i;
        this.b = (short) i2;
        this.c = (short) i3;
    }

    public static f h(j$.time.temporal.k kVar) {
        Objects.requireNonNull(kVar, "temporal");
        int i = t.a;
        f fVar = (f) kVar.d(r.a);
        if (fVar != null) {
            return fVar;
        }
        throw new c("Unable to obtain LocalDate from TemporalAccessor: " + kVar + " of type " + kVar.getClass().getName());
    }

    private int i(j$.time.temporal.l lVar) {
        switch (e.a[((j$.time.temporal.a) lVar).ordinal()]) {
            case 1:
                return this.c;
            case 2:
                return k();
            case 3:
                return ((this.c - 1) / 7) + 1;
            case 4:
                int i = this.a;
                return i >= 1 ? i : 1 - i;
            case 5:
                return j().f();
            case 6:
                return ((this.c - 1) % 7) + 1;
            case 7:
                return ((k() - 1) % 7) + 1;
            case 8:
                throw new w("Invalid field 'EpochDay' for get() method, use getLong() instead");
            case 9:
                return ((k() - 1) / 7) + 1;
            case 10:
                return this.b;
            case 11:
                throw new w("Invalid field 'ProlepticMonth' for get() method, use getLong() instead");
            case 12:
                return this.a;
            case 13:
                return this.a >= 1 ? 1 : 0;
            default:
                throw new w("Unsupported field: " + lVar);
        }
    }

    public static f n(int i, int i2, int i3) {
        long j = i;
        j$.time.temporal.a.YEAR.h(j);
        j$.time.temporal.a.MONTH_OF_YEAR.h(i2);
        j$.time.temporal.a.DAY_OF_MONTH.h(i3);
        int i4 = 28;
        if (i3 > 28) {
            if (i2 != 2) {
                i4 = (i2 == 4 || i2 == 6 || i2 == 9 || i2 == 11) ? 30 : 31;
            } else if (j$.time.chrono.h.a.a(j)) {
                i4 = 29;
            }
            if (i3 > i4) {
                if (i3 == 29) {
                    throw new c("Invalid date 'February 29' as '" + i + "' is not a leap year");
                }
                throw new c("Invalid date '" + k.i(i2).name() + " " + i3 + "'");
            }
        }
        return new f(i, i2, i3);
    }

    public static f o(long j) {
        long j2;
        long j3 = (j + 719528) - 60;
        if (j3 < 0) {
            long j4 = ((j3 + 1) / 146097) - 1;
            j2 = j4 * 400;
            j3 += (-j4) * 146097;
        } else {
            j2 = 0;
        }
        long j5 = ((j3 * 400) + 591) / 146097;
        long j6 = j3 - ((j5 / 400) + (((j5 / 4) + (j5 * 365)) - (j5 / 100)));
        if (j6 < 0) {
            j5--;
            j6 = j3 - ((j5 / 400) + (((j5 / 4) + (365 * j5)) - (j5 / 100)));
        }
        int i = (int) j6;
        int i2 = ((i * 5) + 2) / 153;
        return new f(j$.time.temporal.a.YEAR.g(j5 + j2 + (i2 / 10)), ((i2 + 2) % 12) + 1, (i - (((i2 * 306) + 5) / 10)) + 1);
    }

    @Override // j$.time.temporal.k
    public int a(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? i(lVar) : j$.time.temporal.j.a(this, lVar);
    }

    @Override // j$.time.temporal.k
    public x b(j$.time.temporal.l lVar) {
        int i;
        if (lVar instanceof j$.time.temporal.a) {
            j$.time.temporal.a aVar = (j$.time.temporal.a) lVar;
            if (!aVar.f()) {
                throw new w("Unsupported field: " + lVar);
            }
            int i2 = e.a[aVar.ordinal()];
            if (i2 == 1) {
                short s = this.b;
                i = s != 2 ? (s == 4 || s == 6 || s == 9 || s == 11) ? 30 : 31 : m() ? 29 : 28;
            } else if (i2 != 2) {
                if (i2 == 3) {
                    return x.i(1L, (k.i(this.b) != k.FEBRUARY || m()) ? 5L : 4L);
                } else if (i2 != 4) {
                    return lVar.a();
                } else {
                    return x.i(1L, this.a <= 0 ? 1000000000L : 999999999L);
                }
            } else {
                i = m() ? 366 : 365;
            }
            return x.i(1L, i);
        }
        return lVar.e(this);
    }

    @Override // j$.time.temporal.k
    public long c(j$.time.temporal.l lVar) {
        if (lVar instanceof j$.time.temporal.a) {
            if (lVar == j$.time.temporal.a.EPOCH_DAY) {
                return q();
            }
            if (lVar == j$.time.temporal.a.PROLEPTIC_MONTH) {
                return ((this.a * 12) + this.b) - 1;
            }
            return i(lVar);
        }
        return lVar.c(this);
    }

    @Override // j$.time.temporal.k
    public Object d(u uVar) {
        int i = t.a;
        if (uVar == r.a) {
            return this;
        }
        if (uVar == j$.time.temporal.m.a || uVar == q.a || uVar == p.a || uVar == s.a) {
            return null;
        }
        if (uVar == j$.time.temporal.n.a) {
            return j$.time.chrono.h.a;
        }
        return uVar == j$.time.temporal.o.a ? j$.time.temporal.b.DAYS : uVar.a(this);
    }

    @Override // j$.time.temporal.k
    public boolean e(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? lVar.f() : lVar != null && lVar.d(this);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof f) && g((f) obj) == 0;
    }

    @Override // java.lang.Comparable
    /* renamed from: f */
    public int compareTo(j$.time.chrono.b bVar) {
        if (bVar instanceof f) {
            return g((f) bVar);
        }
        int compare = Long.compare(q(), ((f) bVar).q());
        if (compare == 0) {
            j$.time.chrono.h hVar = j$.time.chrono.h.a;
            return 0;
        }
        return compare;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int g(f fVar) {
        int i = this.a - fVar.a;
        if (i == 0) {
            int i2 = this.b - fVar.b;
            return i2 == 0 ? this.c - fVar.c : i2;
        }
        return i;
    }

    public int hashCode() {
        int i = this.a;
        return (((i << 11) + (this.b << 6)) + this.c) ^ (i & (-2048));
    }

    public DayOfWeek j() {
        return DayOfWeek.g(((int) j$.lang.d.c(q() + 3, 7L)) + 1);
    }

    public int k() {
        return (k.i(this.b).f(m()) + this.c) - 1;
    }

    public int l() {
        return this.a;
    }

    public boolean m() {
        return j$.time.chrono.h.a.a(this.a);
    }

    public f p(long j) {
        int i;
        if (j == 0) {
            return this;
        }
        int g = j$.time.temporal.a.YEAR.g(this.a + j);
        short s = this.b;
        int i2 = this.c;
        if (s != 2) {
            if (s == 4 || s == 6 || s == 9 || s == 11) {
                i = 30;
            }
            return new f(g, s, i2);
        }
        i = j$.time.chrono.h.a.a((long) g) ? 29 : 28;
        i2 = Math.min(i2, i);
        return new f(g, s, i2);
    }

    public long q() {
        long j;
        long j2 = this.a;
        long j3 = this.b;
        long j4 = (365 * j2) + 0;
        if (j2 >= 0) {
            j = ((j2 + 399) / 400) + (((3 + j2) / 4) - ((99 + j2) / 100)) + j4;
        } else {
            j = j4 - ((j2 / (-400)) + ((j2 / (-4)) - (j2 / (-100))));
        }
        long j5 = (((367 * j3) - 362) / 12) + j + (this.c - 1);
        if (j3 > 2) {
            j5--;
            if (!m()) {
                j5--;
            }
        }
        return j5 - 719528;
    }

    public f r(int i) {
        if (k() == i) {
            return this;
        }
        int i2 = this.a;
        long j = i2;
        j$.time.temporal.a.YEAR.h(j);
        j$.time.temporal.a.DAY_OF_YEAR.h(i);
        boolean a = j$.time.chrono.h.a.a(j);
        if (i == 366 && !a) {
            throw new c("Invalid date 'DayOfYear 366' as '" + i2 + "' is not a leap year");
        }
        k i3 = k.i(((i - 1) / 31) + 1);
        if (i > (i3.h(a) + i3.f(a)) - 1) {
            i3 = i3.j(1L);
        }
        return new f(i2, i3.g(), (i - i3.f(a)) + 1);
    }

    public String toString() {
        int i;
        int i2 = this.a;
        short s = this.b;
        short s2 = this.c;
        int abs = Math.abs(i2);
        StringBuilder sb = new StringBuilder(10);
        if (abs < 1000) {
            if (i2 < 0) {
                sb.append(i2 - 10000);
                i = 1;
            } else {
                sb.append(i2 + 10000);
                i = 0;
            }
            sb.deleteCharAt(i);
        } else {
            if (i2 > 9999) {
                sb.append('+');
            }
            sb.append(i2);
        }
        sb.append(s < 10 ? "-0" : "-");
        sb.append((int) s);
        sb.append(s2 >= 10 ? "-" : "-0");
        sb.append((int) s2);
        return sb.toString();
    }
}
