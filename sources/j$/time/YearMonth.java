package j$.time;

import j$.time.format.r;
import j$.time.format.y;
import j$.time.temporal.t;
import j$.time.temporal.u;
import j$.time.temporal.w;
import j$.time.temporal.x;
import java.io.Serializable;
import java.util.Locale;
/* loaded from: classes2.dex */
public final class YearMonth implements Comparable<YearMonth>, Serializable, Serializable {
    private final int a;
    private final int b;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class a {
        static final /* synthetic */ int[] a;
        static final /* synthetic */ int[] b;

        static {
            int[] iArr = new int[j$.time.temporal.b.values().length];
            b = iArr;
            try {
                iArr[j$.time.temporal.b.MONTHS.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                b[j$.time.temporal.b.YEARS.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                b[j$.time.temporal.b.DECADES.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                b[j$.time.temporal.b.CENTURIES.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                b[j$.time.temporal.b.MILLENNIA.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                b[j$.time.temporal.b.ERAS.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            int[] iArr2 = new int[j$.time.temporal.a.values().length];
            a = iArr2;
            try {
                iArr2[j$.time.temporal.a.MONTH_OF_YEAR.ordinal()] = 1;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                a[j$.time.temporal.a.PROLEPTIC_MONTH.ordinal()] = 2;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                a[j$.time.temporal.a.YEAR_OF_ERA.ordinal()] = 3;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                a[j$.time.temporal.a.YEAR.ordinal()] = 4;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                a[j$.time.temporal.a.ERA.ordinal()] = 5;
            } catch (NoSuchFieldError unused11) {
            }
        }
    }

    static {
        r m = new r().m(j$.time.temporal.a.YEAR, 4, 10, y.EXCEEDS_PAD);
        m.e('-');
        m.l(j$.time.temporal.a.MONTH_OF_YEAR, 2);
        m.u(Locale.getDefault());
    }

    private YearMonth(int i, int i2) {
        this.a = i;
        this.b = i2;
    }

    public static YearMonth of(int i, int i2) {
        j$.time.temporal.a.YEAR.h(i);
        j$.time.temporal.a.MONTH_OF_YEAR.h(i2);
        return new YearMonth(i, i2);
    }

    public int a(j$.time.temporal.l lVar) {
        return b(lVar).a(c(lVar), lVar);
    }

    public x b(j$.time.temporal.l lVar) {
        if (lVar == j$.time.temporal.a.YEAR_OF_ERA) {
            return x.i(1L, this.a <= 0 ? 1000000000L : 999999999L);
        }
        return j$.time.temporal.j.c(this, lVar);
    }

    public long c(j$.time.temporal.l lVar) {
        int i;
        if (lVar instanceof j$.time.temporal.a) {
            int i2 = a.a[((j$.time.temporal.a) lVar).ordinal()];
            if (i2 == 1) {
                i = this.b;
            } else if (i2 == 2) {
                return ((this.a * 12) + this.b) - 1;
            } else {
                if (i2 == 3) {
                    int i3 = this.a;
                    if (i3 < 1) {
                        i3 = 1 - i3;
                    }
                    return i3;
                } else if (i2 != 4) {
                    if (i2 == 5) {
                        return this.a < 1 ? 0 : 1;
                    }
                    throw new w("Unsupported field: " + lVar);
                } else {
                    i = this.a;
                }
            }
            return i;
        }
        return lVar.c(this);
    }

    @Override // java.lang.Comparable
    public int compareTo(YearMonth yearMonth) {
        YearMonth yearMonth2 = yearMonth;
        int i = this.a - yearMonth2.a;
        return i == 0 ? this.b - yearMonth2.b : i;
    }

    public Object d(u uVar) {
        int i = t.a;
        return uVar == j$.time.temporal.n.a ? j$.time.chrono.h.a : uVar == j$.time.temporal.o.a ? j$.time.temporal.b.MONTHS : j$.time.temporal.j.b(this, uVar);
    }

    public boolean e(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? lVar == j$.time.temporal.a.YEAR || lVar == j$.time.temporal.a.MONTH_OF_YEAR || lVar == j$.time.temporal.a.PROLEPTIC_MONTH || lVar == j$.time.temporal.a.YEAR_OF_ERA || lVar == j$.time.temporal.a.ERA : lVar != null && lVar.d(this);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof YearMonth) {
            YearMonth yearMonth = (YearMonth) obj;
            return this.a == yearMonth.a && this.b == yearMonth.b;
        }
        return false;
    }

    public int hashCode() {
        return this.a ^ (this.b << 27);
    }

    public int lengthOfMonth() {
        return k.i(this.b).h(j$.time.chrono.h.a.a(this.a));
    }

    public String toString() {
        int i;
        int abs = Math.abs(this.a);
        StringBuilder sb = new StringBuilder(9);
        if (abs < 1000) {
            int i2 = this.a;
            if (i2 < 0) {
                sb.append(i2 - 10000);
                i = 1;
            } else {
                sb.append(i2 + 10000);
                i = 0;
            }
            sb.deleteCharAt(i);
        } else {
            sb.append(this.a);
        }
        sb.append(this.b < 10 ? "-0" : "-");
        sb.append(this.b);
        return sb.toString();
    }
}
