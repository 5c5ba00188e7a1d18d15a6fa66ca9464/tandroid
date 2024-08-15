package j$.time;

import j$.time.temporal.p;
import j$.time.temporal.q;
/* loaded from: classes2.dex */
public enum l implements j$.time.temporal.k {
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
    
    private static final l[] a = values();

    public static l h(int i) {
        if (i < 1 || i > 12) {
            throw new d("Invalid value for MonthOfYear: " + i);
        }
        return a[i - 1];
    }

    @Override // j$.time.temporal.k
    public final q a(j$.time.temporal.l lVar) {
        return lVar == j$.time.temporal.a.MONTH_OF_YEAR ? lVar.c() : j$.time.temporal.j.c(this, lVar);
    }

    @Override // j$.time.temporal.k
    public final boolean b(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? lVar == j$.time.temporal.a.MONTH_OF_YEAR : lVar != null && lVar.a(this);
    }

    @Override // j$.time.temporal.k
    public final long c(j$.time.temporal.l lVar) {
        if (lVar == j$.time.temporal.a.MONTH_OF_YEAR) {
            return ordinal() + 1;
        }
        if (lVar instanceof j$.time.temporal.a) {
            throw new p("Unsupported field: " + lVar);
        }
        return lVar.d(this);
    }

    @Override // j$.time.temporal.k
    public final Object d(j$.time.temporal.n nVar) {
        return nVar == j$.time.temporal.j.d() ? j$.time.chrono.e.a : nVar == j$.time.temporal.j.h() ? j$.time.temporal.b.MONTHS : j$.time.temporal.j.b(this, nVar);
    }

    @Override // j$.time.temporal.k
    public final int e(j$.time.temporal.a aVar) {
        return aVar == j$.time.temporal.a.MONTH_OF_YEAR ? ordinal() + 1 : j$.time.temporal.j.a(this, aVar);
    }

    public final int f(boolean z) {
        switch (k.a[ordinal()]) {
            case 1:
                return 32;
            case 2:
                return (z ? 1 : 0) + 91;
            case 3:
                return (z ? 1 : 0) + 152;
            case 4:
                return (z ? 1 : 0) + 244;
            case 5:
                return (z ? 1 : 0) + 305;
            case 6:
                return 1;
            case 7:
                return (z ? 1 : 0) + 60;
            case 8:
                return (z ? 1 : 0) + 121;
            case 9:
                return (z ? 1 : 0) + 182;
            case 10:
                return (z ? 1 : 0) + 213;
            case 11:
                return (z ? 1 : 0) + 274;
            default:
                return (z ? 1 : 0) + 335;
        }
    }

    public final int g(boolean z) {
        int i = k.a[ordinal()];
        return i != 1 ? (i == 2 || i == 3 || i == 4 || i == 5) ? 30 : 31 : z ? 29 : 28;
    }

    public final l i() {
        int i = ((int) 1) + 12;
        return a[(i + ordinal()) % 12];
    }
}
