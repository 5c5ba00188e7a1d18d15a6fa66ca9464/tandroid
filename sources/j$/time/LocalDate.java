package j$.time;

import j$.time.temporal.q;
import java.io.Serializable;
import org.telegram.messenger.NotificationCenter;

/* loaded from: classes2.dex */
public final class LocalDate implements j$.time.temporal.k, j$.time.chrono.b, Serializable {
    public static final LocalDate d = of(-999999999, 1, 1);
    public static final LocalDate e = of(999999999, 12, 31);
    private final int a;
    private final short b;
    private final short c;

    private LocalDate(int i, int i2, int i3) {
        this.a = i;
        this.b = (short) i2;
        this.c = (short) i3;
    }

    public static LocalDate h(j$.time.temporal.k kVar) {
        j$.util.a.B(kVar, "temporal");
        LocalDate localDate = (LocalDate) kVar.c(j$.time.temporal.j.e());
        if (localDate != null) {
            return localDate;
        }
        throw new c("Unable to obtain LocalDate from TemporalAccessor: " + kVar + " of type " + kVar.getClass().getName());
    }

    private int i(j$.time.temporal.l lVar) {
        int i;
        int k;
        int i2 = f.a[((j$.time.temporal.a) lVar).ordinal()];
        short s = this.c;
        int i3 = this.a;
        switch (i2) {
            case 1:
                return s;
            case 2:
                return k();
            case 3:
                i = (s - 1) / 7;
                return i + 1;
            case 4:
                return i3 >= 1 ? i3 : 1 - i3;
            case 5:
                return j().ordinal() + 1;
            case 6:
                i = (s - 1) % 7;
                return i + 1;
            case 7:
                k = (k() - 1) % 7;
                return k + 1;
            case 8:
                throw new j$.time.temporal.p("Invalid field 'EpochDay' for get() method, use getLong() instead");
            case 9:
                k = (k() - 1) / 7;
                return k + 1;
            case 10:
                return this.b;
            case 11:
                throw new j$.time.temporal.p("Invalid field 'ProlepticMonth' for get() method, use getLong() instead");
            case 12:
                return i3;
            case 13:
                return i3 >= 1 ? 1 : 0;
            default:
                throw new j$.time.temporal.p("Unsupported field: " + lVar);
        }
    }

    private long l() {
        return ((this.a * 12) + this.b) - 1;
    }

    public static LocalDate now() {
        a aVar = new a(ZoneId.systemDefault());
        return p(j$.com.android.tools.r8.a.j(Instant.k(System.currentTimeMillis()).i() + aVar.c().getRules().getOffset(r1).getTotalSeconds(), 86400L));
    }

    public static LocalDate of(int i, int i2, int i3) {
        long j = i;
        j$.time.temporal.a.YEAR.g(j);
        j$.time.temporal.a.MONTH_OF_YEAR.g(i2);
        j$.time.temporal.a.DAY_OF_MONTH.g(i3);
        int i4 = 28;
        if (i3 > 28) {
            if (i2 != 2) {
                i4 = (i2 == 4 || i2 == 6 || i2 == 9 || i2 == 11) ? 30 : 31;
            } else {
                j$.time.chrono.g.a.getClass();
                if (j$.time.chrono.g.a(j)) {
                    i4 = 29;
                }
            }
            if (i3 > i4) {
                if (i3 == 29) {
                    throw new c("Invalid date 'February 29' as '" + i + "' is not a leap year");
                }
                throw new c("Invalid date '" + k.h(i2).name() + " " + i3 + "'");
            }
        }
        return new LocalDate(i, i2, i3);
    }

    public static LocalDate p(long j) {
        long j2;
        long j3 = j + 719468;
        if (j3 < 0) {
            long j4 = ((j + 719469) / 146097) - 1;
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
        int i2 = ((i * 5) + 2) / NotificationCenter.recordStopped;
        return new LocalDate(j$.time.temporal.a.YEAR.f(j5 + j2 + (i2 / 10)), ((i2 + 2) % 12) + 1, (i - (((i2 * NotificationCenter.nearEarEvent) + 5) / 10)) + 1);
    }

    private static LocalDate r(int i, int i2, int i3) {
        int i4;
        if (i2 != 2) {
            if (i2 == 4 || i2 == 6 || i2 == 9 || i2 == 11) {
                i4 = 30;
            }
            return new LocalDate(i, i2, i3);
        }
        j$.time.chrono.g.a.getClass();
        i4 = j$.time.chrono.g.a((long) i) ? 29 : 28;
        i3 = Math.min(i3, i4);
        return new LocalDate(i, i2, i3);
    }

    @Override // j$.time.temporal.k
    public final q a(j$.time.temporal.l lVar) {
        if (!(lVar instanceof j$.time.temporal.a)) {
            return lVar.d(this);
        }
        j$.time.temporal.a aVar = (j$.time.temporal.a) lVar;
        if (!aVar.e()) {
            throw new j$.time.temporal.p("Unsupported field: " + lVar);
        }
        int i = f.a[aVar.ordinal()];
        if (i == 1) {
            return q.i(1L, o());
        }
        if (i == 2) {
            return q.i(1L, n() ? 366 : 365);
        }
        if (i == 3) {
            return q.i(1L, (k.h(this.b) != k.FEBRUARY || n()) ? 5L : 4L);
        }
        if (i != 4) {
            return ((j$.time.temporal.a) lVar).a();
        }
        return q.i(1L, this.a <= 0 ? 1000000000L : 999999999L);
    }

    @Override // j$.time.temporal.k
    public final long b(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? lVar == j$.time.temporal.a.EPOCH_DAY ? s() : lVar == j$.time.temporal.a.PROLEPTIC_MONTH ? l() : i(lVar) : lVar.b(this);
    }

    @Override // j$.time.temporal.k
    public final Object c(j$.time.temporal.n nVar) {
        if (nVar == j$.time.temporal.j.e()) {
            return this;
        }
        if (nVar == j$.time.temporal.j.j() || nVar == j$.time.temporal.j.i() || nVar == j$.time.temporal.j.g() || nVar == j$.time.temporal.j.f()) {
            return null;
        }
        return nVar == j$.time.temporal.j.d() ? j$.time.chrono.g.a : nVar == j$.time.temporal.j.h() ? j$.time.temporal.b.DAYS : nVar.a(this);
    }

    @Override // j$.time.temporal.k
    public final int d(j$.time.temporal.a aVar) {
        return aVar instanceof j$.time.temporal.a ? i(aVar) : j$.time.temporal.j.a(this, aVar);
    }

    @Override // j$.time.temporal.k
    public final boolean e(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? ((j$.time.temporal.a) lVar).e() : lVar != null && lVar.c(this);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof LocalDate) && g((LocalDate) obj) == 0;
    }

    @Override // java.lang.Comparable
    /* renamed from: f, reason: merged with bridge method [inline-methods] */
    public final int compareTo(j$.time.chrono.b bVar) {
        if (bVar instanceof LocalDate) {
            return g((LocalDate) bVar);
        }
        int compare = Long.compare(s(), ((LocalDate) bVar).s());
        if (compare != 0) {
            return compare;
        }
        j$.time.chrono.g.a.getClass();
        return 0;
    }

    final int g(LocalDate localDate) {
        int i = this.a - localDate.a;
        if (i != 0) {
            return i;
        }
        int i2 = this.b - localDate.b;
        return i2 == 0 ? this.c - localDate.c : i2;
    }

    public final int hashCode() {
        int i = this.a;
        return (((i << 11) + (this.b << 6)) + this.c) ^ (i & (-2048));
    }

    public final DayOfWeek j() {
        return DayOfWeek.f(((int) j$.com.android.tools.r8.a.i(s() + 3, 7L)) + 1);
    }

    public final int k() {
        return (k.h(this.b).f(n()) + this.c) - 1;
    }

    public final int m() {
        return this.a;
    }

    public final boolean n() {
        j$.time.chrono.g gVar = j$.time.chrono.g.a;
        long j = this.a;
        gVar.getClass();
        return j$.time.chrono.g.a(j);
    }

    public final int o() {
        short s = this.b;
        return s != 2 ? (s == 4 || s == 6 || s == 9 || s == 11) ? 30 : 31 : n() ? 29 : 28;
    }

    public final LocalDate q(long j) {
        return j == 0 ? this : r(j$.time.temporal.a.YEAR.f(this.a + j), this.b, this.c);
    }

    public final long s() {
        long j = this.a;
        long j2 = this.b;
        long j3 = 365 * j;
        long j4 = (((367 * j2) - 362) / 12) + (j >= 0 ? ((j + 399) / 400) + (((3 + j) / 4) - ((99 + j) / 100)) + j3 : j3 - ((j / (-400)) + ((j / (-4)) - (j / (-100))))) + (this.c - 1);
        if (j2 > 2) {
            j4 = !n() ? j4 - 2 : j4 - 1;
        }
        return j4 - 719528;
    }

    public final Period t(j$.time.chrono.b bVar) {
        LocalDate r;
        LocalDate h = h(bVar);
        long l = h.l() - l();
        short s = h.c;
        short s2 = this.c;
        int i = s - s2;
        if (l > 0 && i < 0) {
            l--;
            if (l == 0) {
                r = this;
            } else {
                long j = (this.a * 12) + (this.b - 1) + l;
                r = r(j$.time.temporal.a.YEAR.f(j$.com.android.tools.r8.a.j(j, 12L)), ((int) j$.com.android.tools.r8.a.i(j, 12L)) + 1, s2);
            }
            i = (int) (h.s() - r.s());
        } else if (l < 0 && i > 0) {
            l++;
            i -= h.o();
        }
        long j2 = l / 12;
        int i2 = (int) (l % 12);
        int i3 = (int) j2;
        if (j2 == i3) {
            return Period.a(i3, i2, i);
        }
        throw new ArithmeticException();
    }

    public final String toString() {
        int i;
        int i2 = this.a;
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
        short s = this.b;
        sb.append(s < 10 ? "-0" : "-");
        sb.append((int) s);
        short s2 = this.c;
        sb.append(s2 < 10 ? "-0" : "-");
        sb.append((int) s2);
        return sb.toString();
    }

    public final LocalDate u() {
        if (k() == 180) {
            return this;
        }
        j$.time.temporal.a aVar = j$.time.temporal.a.YEAR;
        int i = this.a;
        long j = i;
        aVar.g(j);
        j$.time.temporal.a.DAY_OF_YEAR.g(NotificationCenter.updateBotMenuButton);
        j$.time.chrono.g.a.getClass();
        boolean a = j$.time.chrono.g.a(j);
        k h = k.h(6);
        if (180 > (h.g(a) + h.f(a)) - 1) {
            h = h.i();
        }
        return new LocalDate(i, h.ordinal() + 1, 181 - h.f(a));
    }
}
