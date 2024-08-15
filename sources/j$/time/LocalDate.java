package j$.time;

import j$.time.temporal.p;
import j$.time.temporal.q;
import java.io.Serializable;
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
        if (kVar != null) {
            LocalDate localDate = (LocalDate) kVar.d(j$.time.temporal.j.e());
            if (localDate != null) {
                return localDate;
            }
            throw new d("Unable to obtain LocalDate from TemporalAccessor: " + kVar + " of type " + kVar.getClass().getName());
        }
        throw new NullPointerException("temporal");
    }

    private int i(j$.time.temporal.l lVar) {
        int i;
        int k;
        int i2 = g.a[((j$.time.temporal.a) lVar).ordinal()];
        int i3 = this.a;
        short s = this.c;
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
                throw new p("Invalid field 'EpochDay' for get() method, use getLong() instead");
            case 9:
                k = (k() - 1) / 7;
                return k + 1;
            case 10:
                return this.b;
            case 11:
                throw new p("Invalid field 'ProlepticMonth' for get() method, use getLong() instead");
            case 12:
                return i3;
            case 13:
                return i3 >= 1 ? 1 : 0;
            default:
                throw new p("Unsupported field: " + lVar);
        }
    }

    public static LocalDate now() {
        b bVar = new b(ZoneId.systemDefault());
        Instant j = Instant.j(System.currentTimeMillis());
        return o(a.f(j.i() + bVar.b().getRules().getOffset(j).getTotalSeconds(), 86400L));
    }

    public static LocalDate o(long j) {
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
        return new LocalDate(j$.time.temporal.a.YEAR.e(j5 + j2 + (i2 / 10)), ((i2 + 2) % 12) + 1, (i - (((i2 * 306) + 5) / 10)) + 1);
    }

    public static LocalDate of(int i, int i2, int i3) {
        long j = i;
        j$.time.temporal.a.YEAR.f(j);
        j$.time.temporal.a.MONTH_OF_YEAR.f(i2);
        j$.time.temporal.a.DAY_OF_MONTH.f(i3);
        int i4 = 28;
        if (i3 > 28) {
            if (i2 != 2) {
                i4 = (i2 == 4 || i2 == 6 || i2 == 9 || i2 == 11) ? 30 : 31;
            } else {
                j$.time.chrono.e.a.getClass();
                if (j$.time.chrono.e.a(j)) {
                    i4 = 29;
                }
            }
            if (i3 > i4) {
                if (i3 == 29) {
                    throw new d("Invalid date 'February 29' as '" + i + "' is not a leap year");
                }
                throw new d("Invalid date '" + l.h(i2).name() + " " + i3 + "'");
            }
        }
        return new LocalDate(i, i2, i3);
    }

    private static LocalDate q(int i, int i2, int i3) {
        int i4;
        if (i2 != 2) {
            if (i2 == 4 || i2 == 6 || i2 == 9 || i2 == 11) {
                i4 = 30;
            }
            return new LocalDate(i, i2, i3);
        }
        j$.time.chrono.e.a.getClass();
        i4 = j$.time.chrono.e.a((long) i) ? 29 : 28;
        i3 = Math.min(i3, i4);
        return new LocalDate(i, i2, i3);
    }

    @Override // j$.time.temporal.k
    public final q a(j$.time.temporal.l lVar) {
        int n;
        if (lVar instanceof j$.time.temporal.a) {
            j$.time.temporal.a aVar = (j$.time.temporal.a) lVar;
            if (!aVar.isDateBased()) {
                throw new p("Unsupported field: " + lVar);
            }
            int i = g.a[aVar.ordinal()];
            if (i == 1) {
                n = n();
            } else if (i != 2) {
                if (i == 3) {
                    return q.i(1L, (l.h(this.b) != l.FEBRUARY || m()) ? 5L : 4L);
                } else if (i != 4) {
                    return lVar.c();
                } else {
                    return q.i(1L, this.a <= 0 ? 1000000000L : 999999999L);
                }
            } else {
                n = m() ? 366 : 365;
            }
            return q.i(1L, n);
        }
        return lVar.b(this);
    }

    @Override // j$.time.temporal.k
    public final boolean b(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? lVar.isDateBased() : lVar != null && lVar.a(this);
    }

    @Override // j$.time.temporal.k
    public final long c(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? lVar == j$.time.temporal.a.EPOCH_DAY ? r() : lVar == j$.time.temporal.a.PROLEPTIC_MONTH ? ((this.a * 12) + this.b) - 1 : i(lVar) : lVar.d(this);
    }

    @Override // j$.time.temporal.k
    public final Object d(j$.time.temporal.n nVar) {
        if (nVar == j$.time.temporal.j.e()) {
            return this;
        }
        if (nVar == j$.time.temporal.j.j() || nVar == j$.time.temporal.j.i() || nVar == j$.time.temporal.j.g() || nVar == j$.time.temporal.j.f()) {
            return null;
        }
        return nVar == j$.time.temporal.j.d() ? j$.time.chrono.e.a : nVar == j$.time.temporal.j.h() ? j$.time.temporal.b.DAYS : nVar.a(this);
    }

    @Override // j$.time.temporal.k
    public final int e(j$.time.temporal.a aVar) {
        return aVar instanceof j$.time.temporal.a ? i(aVar) : j$.time.temporal.j.a(this, aVar);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof LocalDate) && g((LocalDate) obj) == 0;
    }

    @Override // java.lang.Comparable
    /* renamed from: f */
    public final int compareTo(j$.time.chrono.b bVar) {
        if (bVar instanceof LocalDate) {
            return g((LocalDate) bVar);
        }
        int compare = Long.compare(r(), ((LocalDate) bVar).r());
        if (compare == 0) {
            j$.time.chrono.e.a.getClass();
            return 0;
        }
        return compare;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int g(LocalDate localDate) {
        int i = this.a - localDate.a;
        if (i == 0) {
            int i2 = this.b - localDate.b;
            return i2 == 0 ? this.c - localDate.c : i2;
        }
        return i;
    }

    public final int hashCode() {
        int i = this.a;
        return (((i << 11) + (this.b << 6)) + this.c) ^ (i & (-2048));
    }

    public final DayOfWeek j() {
        return DayOfWeek.f(((int) a.d(r() + 3, 7L)) + 1);
    }

    public final int k() {
        return (l.h(this.b).f(m()) + this.c) - 1;
    }

    public final int l() {
        return this.a;
    }

    public final boolean m() {
        j$.time.chrono.e.a.getClass();
        return j$.time.chrono.e.a(this.a);
    }

    public final int n() {
        short s = this.b;
        return s != 2 ? (s == 4 || s == 6 || s == 9 || s == 11) ? 30 : 31 : m() ? 29 : 28;
    }

    public final LocalDate p(long j) {
        return j == 0 ? this : q(j$.time.temporal.a.YEAR.e(this.a + j), this.b, this.c);
    }

    public final long r() {
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

    public final Period s(j$.time.chrono.b bVar) {
        LocalDate q;
        LocalDate h = h(bVar);
        int i = this.a;
        short s = this.b;
        long j = (((h.a * 12) + h.b) - 1) - (((i * 12) + s) - 1);
        short s2 = h.c;
        short s3 = this.c;
        int i2 = s2 - s3;
        if (j > 0 && i2 < 0) {
            j--;
            if (j == 0) {
                q = this;
            } else {
                long j2 = (i * 12) + (s - 1) + j;
                q = q(j$.time.temporal.a.YEAR.e(a.f(j2, 12L)), ((int) a.d(j2, 12L)) + 1, s3);
            }
            i2 = (int) (h.r() - q.r());
        } else if (j < 0 && i2 > 0) {
            j++;
            i2 -= h.n();
        }
        long j3 = j / 12;
        int i3 = (int) (j % 12);
        int i4 = (int) j3;
        if (j3 == i4) {
            return Period.a(i4, i3, i2);
        }
        throw new ArithmeticException();
    }

    public final LocalDate t() {
        if (k() == 180) {
            return this;
        }
        j$.time.temporal.a aVar = j$.time.temporal.a.YEAR;
        int i = this.a;
        long j = i;
        aVar.f(j);
        j$.time.temporal.a.DAY_OF_YEAR.f(180);
        j$.time.chrono.e.a.getClass();
        boolean a = j$.time.chrono.e.a(j);
        l h = l.h(6);
        if (180 > (h.g(a) + h.f(a)) - 1) {
            h = h.i();
        }
        return new LocalDate(i, h.ordinal() + 1, (180 - h.f(a)) + 1);
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
        sb.append(s2 >= 10 ? "-" : "-0");
        sb.append((int) s2);
        return sb.toString();
    }
}
