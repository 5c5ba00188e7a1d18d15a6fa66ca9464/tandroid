package j$.time;

import j$.time.temporal.n;
import j$.time.temporal.o;
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
public final class LocalDate implements j$.time.temporal.k, j$.time.chrono.b, Serializable {
    public static final LocalDate d = of(-999999999, 1, 1);
    public static final LocalDate e = of(999999999, 12, 31);
    private final int a;
    private final short b;
    private final short c;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class a {
        static final /* synthetic */ int[] a;
        static final /* synthetic */ int[] b;

        static {
            int[] iArr = new int[j$.time.temporal.b.values().length];
            b = iArr;
            try {
                iArr[j$.time.temporal.b.DAYS.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                b[j$.time.temporal.b.WEEKS.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                b[j$.time.temporal.b.MONTHS.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                b[j$.time.temporal.b.YEARS.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                b[j$.time.temporal.b.DECADES.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                b[j$.time.temporal.b.CENTURIES.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                b[j$.time.temporal.b.MILLENNIA.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                b[j$.time.temporal.b.ERAS.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            int[] iArr2 = new int[j$.time.temporal.a.values().length];
            a = iArr2;
            try {
                iArr2[j$.time.temporal.a.DAY_OF_MONTH.ordinal()] = 1;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                a[j$.time.temporal.a.DAY_OF_YEAR.ordinal()] = 2;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                a[j$.time.temporal.a.ALIGNED_WEEK_OF_MONTH.ordinal()] = 3;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                a[j$.time.temporal.a.YEAR_OF_ERA.ordinal()] = 4;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                a[j$.time.temporal.a.DAY_OF_WEEK.ordinal()] = 5;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                a[j$.time.temporal.a.ALIGNED_DAY_OF_WEEK_IN_MONTH.ordinal()] = 6;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                a[j$.time.temporal.a.ALIGNED_DAY_OF_WEEK_IN_YEAR.ordinal()] = 7;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                a[j$.time.temporal.a.EPOCH_DAY.ordinal()] = 8;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                a[j$.time.temporal.a.ALIGNED_WEEK_OF_YEAR.ordinal()] = 9;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                a[j$.time.temporal.a.MONTH_OF_YEAR.ordinal()] = 10;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                a[j$.time.temporal.a.PROLEPTIC_MONTH.ordinal()] = 11;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                a[j$.time.temporal.a.YEAR.ordinal()] = 12;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                a[j$.time.temporal.a.ERA.ordinal()] = 13;
            } catch (NoSuchFieldError unused21) {
            }
        }
    }

    private LocalDate(int i, int i2, int i3) {
        this.a = i;
        this.b = (short) i2;
        this.c = (short) i3;
    }

    public static LocalDate h(j$.time.temporal.k kVar) {
        Objects.requireNonNull(kVar, "temporal");
        int i = t.a;
        LocalDate localDate = (LocalDate) kVar.d(r.a);
        if (localDate != null) {
            return localDate;
        }
        throw new c("Unable to obtain LocalDate from TemporalAccessor: " + kVar + " of type " + kVar.getClass().getName());
    }

    private int i(j$.time.temporal.l lVar) {
        switch (a.a[((j$.time.temporal.a) lVar).ordinal()]) {
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

    private long l() {
        return ((this.a * 12) + this.b) - 1;
    }

    public static LocalDate now() {
        j$.time.a aVar = new j$.time.a(ZoneId.systemDefault());
        Instant k = Instant.k(System.currentTimeMillis());
        return p(j$.lang.d.d(k.i() + aVar.c().getRules().getOffset(k).getTotalSeconds(), 86400L));
    }

    public static LocalDate of(int i, int i2, int i3) {
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
                throw new c("Invalid date '" + i.i(i2).name() + " " + i3 + "'");
            }
        }
        return new LocalDate(i, i2, i3);
    }

    public static LocalDate p(long j) {
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
        return new LocalDate(j$.time.temporal.a.YEAR.g(j5 + j2 + (i2 / 10)), ((i2 + 2) % 12) + 1, (i - (((i2 * 306) + 5) / 10)) + 1);
    }

    private static LocalDate r(int i, int i2, int i3) {
        int i4;
        if (i2 != 2) {
            if (i2 == 4 || i2 == 6 || i2 == 9 || i2 == 11) {
                i4 = 30;
            }
            return new LocalDate(i, i2, i3);
        }
        i4 = j$.time.chrono.h.a.a((long) i) ? 29 : 28;
        i3 = Math.min(i3, i4);
        return new LocalDate(i, i2, i3);
    }

    @Override // j$.time.temporal.k
    public int a(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? i(lVar) : j$.time.temporal.j.a(this, lVar);
    }

    @Override // j$.time.temporal.k
    public x b(j$.time.temporal.l lVar) {
        int o;
        if (lVar instanceof j$.time.temporal.a) {
            j$.time.temporal.a aVar = (j$.time.temporal.a) lVar;
            if (!aVar.f()) {
                throw new w("Unsupported field: " + lVar);
            }
            int i = a.a[aVar.ordinal()];
            if (i == 1) {
                o = o();
            } else if (i != 2) {
                if (i == 3) {
                    return x.i(1L, (i.i(this.b) != i.FEBRUARY || n()) ? 5L : 4L);
                } else if (i != 4) {
                    return lVar.a();
                } else {
                    return x.i(1L, this.a <= 0 ? 1000000000L : 999999999L);
                }
            } else {
                o = n() ? 366 : 365;
            }
            return x.i(1L, o);
        }
        return lVar.e(this);
    }

    @Override // j$.time.temporal.k
    public long c(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? lVar == j$.time.temporal.a.EPOCH_DAY ? s() : lVar == j$.time.temporal.a.PROLEPTIC_MONTH ? l() : i(lVar) : lVar.c(this);
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
        if (uVar == n.a) {
            return j$.time.chrono.h.a;
        }
        return uVar == o.a ? j$.time.temporal.b.DAYS : uVar.a(this);
    }

    @Override // j$.time.temporal.k
    public boolean e(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? lVar.f() : lVar != null && lVar.d(this);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof LocalDate) && g((LocalDate) obj) == 0;
    }

    @Override // java.lang.Comparable
    /* renamed from: f */
    public int compareTo(j$.time.chrono.b bVar) {
        if (bVar instanceof LocalDate) {
            return g((LocalDate) bVar);
        }
        int compare = Long.compare(s(), ((LocalDate) bVar).s());
        if (compare == 0) {
            j$.time.chrono.h hVar = j$.time.chrono.h.a;
            return 0;
        }
        return compare;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int g(LocalDate localDate) {
        int i = this.a - localDate.a;
        if (i == 0) {
            int i2 = this.b - localDate.b;
            return i2 == 0 ? this.c - localDate.c : i2;
        }
        return i;
    }

    public int hashCode() {
        int i = this.a;
        return (((i << 11) + (this.b << 6)) + this.c) ^ (i & (-2048));
    }

    public DayOfWeek j() {
        return DayOfWeek.g(((int) j$.lang.d.c(s() + 3, 7L)) + 1);
    }

    public int k() {
        return (i.i(this.b).f(n()) + this.c) - 1;
    }

    public int m() {
        return this.a;
    }

    public boolean n() {
        return j$.time.chrono.h.a.a(this.a);
    }

    public int o() {
        short s = this.b;
        return s != 2 ? (s == 4 || s == 6 || s == 9 || s == 11) ? 30 : 31 : n() ? 29 : 28;
    }

    public LocalDate q(long j) {
        return j == 0 ? this : r(j$.time.temporal.a.YEAR.g(this.a + j), this.b, this.c);
    }

    public long s() {
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
            if (!n()) {
                j5--;
            }
        }
        return j5 - 719528;
    }

    public Period t(j$.time.chrono.b bVar) {
        LocalDate r;
        LocalDate h = h(bVar);
        long l = h.l() - l();
        int i = h.c - this.c;
        int i2 = (l > 0L ? 1 : (l == 0L ? 0 : -1));
        if (i2 > 0 && i < 0) {
            l--;
            if (l == 0) {
                r = this;
            } else {
                long j = (this.a * 12) + (this.b - 1) + l;
                r = r(j$.time.temporal.a.YEAR.g(j$.lang.d.d(j, 12L)), ((int) j$.lang.d.c(j, 12L)) + 1, this.c);
            }
            i = (int) (h.s() - r.s());
        } else if (i2 < 0 && i > 0) {
            l++;
            i -= h.o();
        }
        long j2 = l / 12;
        int i3 = (int) (l % 12);
        int i4 = (int) j2;
        if (j2 == i4) {
            return Period.a(i4, i3, i);
        }
        throw new ArithmeticException();
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

    public LocalDate u(int i) {
        if (k() == i) {
            return this;
        }
        int i2 = this.a;
        long j = i2;
        j$.time.temporal.a.YEAR.h(j);
        j$.time.temporal.a.DAY_OF_YEAR.h(i);
        boolean a2 = j$.time.chrono.h.a.a(j);
        if (i == 366 && !a2) {
            throw new c("Invalid date 'DayOfYear 366' as '" + i2 + "' is not a leap year");
        }
        i i3 = i.i(((i - 1) / 31) + 1);
        if (i > (i3.h(a2) + i3.f(a2)) - 1) {
            i3 = i3.j(1L);
        }
        return new LocalDate(i2, i3.g(), (i - i3.f(a2)) + 1);
    }
}
