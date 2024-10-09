package j$.time;

import j$.time.format.q;
import j$.time.format.y;
import java.io.Serializable;
import java.util.Locale;

/* loaded from: classes2.dex */
public final class YearMonth implements j$.time.temporal.k, Comparable<YearMonth>, Serializable {
    private final int a;
    private final int b;

    static {
        q qVar = new q();
        qVar.m(j$.time.temporal.a.YEAR, 4, 10, y.EXCEEDS_PAD);
        qVar.e('-');
        qVar.l(j$.time.temporal.a.MONTH_OF_YEAR, 2);
        qVar.v(Locale.getDefault());
    }

    private YearMonth(int i, int i2) {
        this.a = i;
        this.b = i2;
    }

    public static YearMonth of(int i, int i2) {
        j$.time.temporal.a.YEAR.g(i);
        j$.time.temporal.a.MONTH_OF_YEAR.g(i2);
        return new YearMonth(i, i2);
    }

    @Override // j$.time.temporal.k
    public final j$.time.temporal.q a(j$.time.temporal.l lVar) {
        if (lVar == j$.time.temporal.a.YEAR_OF_ERA) {
            return j$.time.temporal.q.i(1L, this.a <= 0 ? 1000000000L : 999999999L);
        }
        return j$.time.temporal.j.c(this, lVar);
    }

    @Override // j$.time.temporal.k
    public final long b(j$.time.temporal.l lVar) {
        if (!(lVar instanceof j$.time.temporal.a)) {
            return lVar.b(this);
        }
        int i = l.a[((j$.time.temporal.a) lVar).ordinal()];
        int i2 = this.b;
        if (i == 1) {
            return i2;
        }
        int i3 = this.a;
        if (i == 2) {
            return ((i3 * 12) + i2) - 1;
        }
        if (i == 3) {
            if (i3 < 1) {
                i3 = 1 - i3;
            }
            return i3;
        }
        if (i == 4) {
            return i3;
        }
        if (i == 5) {
            return i3 < 1 ? 0 : 1;
        }
        throw new j$.time.temporal.p("Unsupported field: " + lVar);
    }

    @Override // j$.time.temporal.k
    public final Object c(j$.time.temporal.n nVar) {
        return nVar == j$.time.temporal.j.d() ? j$.time.chrono.g.a : nVar == j$.time.temporal.j.h() ? j$.time.temporal.b.MONTHS : j$.time.temporal.j.b(this, nVar);
    }

    @Override // java.lang.Comparable
    public final int compareTo(YearMonth yearMonth) {
        YearMonth yearMonth2 = yearMonth;
        int i = this.a - yearMonth2.a;
        return i == 0 ? this.b - yearMonth2.b : i;
    }

    @Override // j$.time.temporal.k
    public final int d(j$.time.temporal.a aVar) {
        return a(aVar).a(b(aVar), aVar);
    }

    @Override // j$.time.temporal.k
    public final boolean e(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? lVar == j$.time.temporal.a.YEAR || lVar == j$.time.temporal.a.MONTH_OF_YEAR || lVar == j$.time.temporal.a.PROLEPTIC_MONTH || lVar == j$.time.temporal.a.YEAR_OF_ERA || lVar == j$.time.temporal.a.ERA : lVar != null && lVar.c(this);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof YearMonth)) {
            return false;
        }
        YearMonth yearMonth = (YearMonth) obj;
        return this.a == yearMonth.a && this.b == yearMonth.b;
    }

    public final int hashCode() {
        return (this.b << 27) ^ this.a;
    }

    public int lengthOfMonth() {
        k h = k.h(this.b);
        j$.time.chrono.g gVar = j$.time.chrono.g.a;
        long j = this.a;
        gVar.getClass();
        return h.g(j$.time.chrono.g.a(j));
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
