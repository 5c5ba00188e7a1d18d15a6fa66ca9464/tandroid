package j$.time;

import j$.time.format.r;
import j$.time.format.y;
import j$.time.temporal.p;
import j$.time.temporal.q;
import java.io.Serializable;
import java.util.Locale;
/* loaded from: classes2.dex */
public final class YearMonth implements j$.time.temporal.k, Comparable<YearMonth>, Serializable {
    private final int a;
    private final int b;

    static {
        r rVar = new r();
        rVar.k(j$.time.temporal.a.YEAR, 4, 10, y.EXCEEDS_PAD);
        rVar.e('-');
        rVar.m(j$.time.temporal.a.MONTH_OF_YEAR, 2);
        rVar.v(Locale.getDefault());
    }

    private YearMonth(int i, int i2) {
        this.a = i;
        this.b = i2;
    }

    public static YearMonth of(int i, int i2) {
        j$.time.temporal.a.YEAR.f(i);
        j$.time.temporal.a.MONTH_OF_YEAR.f(i2);
        return new YearMonth(i, i2);
    }

    @Override // j$.time.temporal.k
    public final q a(j$.time.temporal.l lVar) {
        if (lVar == j$.time.temporal.a.YEAR_OF_ERA) {
            return q.i(1L, this.a <= 0 ? 1000000000L : 999999999L);
        }
        return j$.time.temporal.j.c(this, lVar);
    }

    @Override // j$.time.temporal.k
    public final boolean b(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? lVar == j$.time.temporal.a.YEAR || lVar == j$.time.temporal.a.MONTH_OF_YEAR || lVar == j$.time.temporal.a.PROLEPTIC_MONTH || lVar == j$.time.temporal.a.YEAR_OF_ERA || lVar == j$.time.temporal.a.ERA : lVar != null && lVar.a(this);
    }

    @Override // j$.time.temporal.k
    public final long c(j$.time.temporal.l lVar) {
        if (lVar instanceof j$.time.temporal.a) {
            int i = m.a[((j$.time.temporal.a) lVar).ordinal()];
            int i2 = this.b;
            if (i != 1) {
                int i3 = this.a;
                if (i != 2) {
                    if (i == 3) {
                        if (i3 < 1) {
                            i3 = 1 - i3;
                        }
                        return i3;
                    } else if (i != 4) {
                        if (i == 5) {
                            return i3 < 1 ? 0 : 1;
                        }
                        throw new p("Unsupported field: " + lVar);
                    } else {
                        return i3;
                    }
                }
                return ((i3 * 12) + i2) - 1;
            }
            return i2;
        }
        return lVar.d(this);
    }

    @Override // java.lang.Comparable
    public final int compareTo(YearMonth yearMonth) {
        YearMonth yearMonth2 = yearMonth;
        int i = this.a - yearMonth2.a;
        return i == 0 ? this.b - yearMonth2.b : i;
    }

    @Override // j$.time.temporal.k
    public final Object d(j$.time.temporal.n nVar) {
        return nVar == j$.time.temporal.j.d() ? j$.time.chrono.e.a : nVar == j$.time.temporal.j.h() ? j$.time.temporal.b.MONTHS : j$.time.temporal.j.b(this, nVar);
    }

    @Override // j$.time.temporal.k
    public final int e(j$.time.temporal.a aVar) {
        return a(aVar).a(c(aVar), aVar);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof YearMonth) {
            YearMonth yearMonth = (YearMonth) obj;
            return this.a == yearMonth.a && this.b == yearMonth.b;
        }
        return false;
    }

    public final int hashCode() {
        return (this.b << 27) ^ this.a;
    }

    public int lengthOfMonth() {
        l h = l.h(this.b);
        j$.time.chrono.e.a.getClass();
        return h.g(j$.time.chrono.e.a(this.a));
    }

    public final String toString() {
        int i;
        int i2 = this.a;
        int abs = Math.abs(i2);
        StringBuilder sb = new StringBuilder(9);
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
            sb.append(i2);
        }
        int i3 = this.b;
        sb.append(i3 < 10 ? "-0" : "-");
        sb.append(i3);
        return sb.toString();
    }
}
