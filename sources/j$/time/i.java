package j$.time;

import j$.time.temporal.n;
import j$.time.temporal.o;
import j$.time.temporal.t;
import j$.time.temporal.u;
import j$.time.temporal.w;
import j$.time.temporal.x;
/* loaded from: classes2.dex */
public enum i implements j$.time.temporal.k {
    JANUARY,
    FEBRUARY,
    MARCH,
    APRIL,
    MAY,
    JUNE,
    JULY,
    AUGUST,
    SEPTEMBER,
    OCTOBER,
    NOVEMBER,
    DECEMBER;
    
    private static final i[] a = values();

    public static i i(int i) {
        if (i < 1 || i > 12) {
            throw new c("Invalid value for MonthOfYear: " + i);
        }
        return a[i - 1];
    }

    @Override // j$.time.temporal.k
    public int a(j$.time.temporal.l lVar) {
        return lVar == j$.time.temporal.a.MONTH_OF_YEAR ? g() : j$.time.temporal.j.a(this, lVar);
    }

    @Override // j$.time.temporal.k
    public x b(j$.time.temporal.l lVar) {
        return lVar == j$.time.temporal.a.MONTH_OF_YEAR ? lVar.a() : j$.time.temporal.j.c(this, lVar);
    }

    @Override // j$.time.temporal.k
    public long c(j$.time.temporal.l lVar) {
        if (lVar == j$.time.temporal.a.MONTH_OF_YEAR) {
            return g();
        }
        if (lVar instanceof j$.time.temporal.a) {
            throw new w("Unsupported field: " + lVar);
        }
        return lVar.c(this);
    }

    @Override // j$.time.temporal.k
    public Object d(u uVar) {
        int i = t.a;
        return uVar == n.a ? j$.time.chrono.h.a : uVar == o.a ? j$.time.temporal.b.MONTHS : j$.time.temporal.j.b(this, uVar);
    }

    @Override // j$.time.temporal.k
    public boolean e(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? lVar == j$.time.temporal.a.MONTH_OF_YEAR : lVar != null && lVar.d(this);
    }

    public int f(boolean z) {
        int i;
        switch (h.a[ordinal()]) {
            case 1:
                return 32;
            case 2:
                i = 91;
                break;
            case 3:
                i = 152;
                break;
            case 4:
                i = 244;
                break;
            case 5:
                i = 305;
                break;
            case 6:
                return 1;
            case 7:
                i = 60;
                break;
            case 8:
                i = 121;
                break;
            case 9:
                i = 182;
                break;
            case 10:
                i = 213;
                break;
            case 11:
                i = 274;
                break;
            default:
                i = 335;
                break;
        }
        return (z ? 1 : 0) + i;
    }

    public int g() {
        return ordinal() + 1;
    }

    public int h(boolean z) {
        int i = h.a[ordinal()];
        return i != 1 ? (i == 2 || i == 3 || i == 4 || i == 5) ? 30 : 31 : z ? 29 : 28;
    }

    public i j(long j) {
        return a[((((int) (j % 12)) + 12) + ordinal()) % 12];
    }
}
