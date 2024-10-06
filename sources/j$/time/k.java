package j$.time;

import j$.time.temporal.q;
import org.telegram.messenger.NotificationCenter;
/* loaded from: classes2.dex */
public enum k implements j$.time.temporal.k {
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
    
    private static final k[] a = values();

    public static k h(int i) {
        if (i < 1 || i > 12) {
            throw new c("Invalid value for MonthOfYear: " + i);
        }
        return a[i - 1];
    }

    @Override // j$.time.temporal.k
    public final q a(j$.time.temporal.l lVar) {
        return lVar == j$.time.temporal.a.MONTH_OF_YEAR ? lVar.a() : j$.time.temporal.j.c(this, lVar);
    }

    @Override // j$.time.temporal.k
    public final long b(j$.time.temporal.l lVar) {
        if (lVar == j$.time.temporal.a.MONTH_OF_YEAR) {
            return ordinal() + 1;
        }
        if (lVar instanceof j$.time.temporal.a) {
            throw new j$.time.temporal.p("Unsupported field: " + lVar);
        }
        return lVar.b(this);
    }

    @Override // j$.time.temporal.k
    public final Object c(j$.time.temporal.n nVar) {
        return nVar == j$.time.temporal.j.d() ? j$.time.chrono.g.a : nVar == j$.time.temporal.j.h() ? j$.time.temporal.b.MONTHS : j$.time.temporal.j.b(this, nVar);
    }

    @Override // j$.time.temporal.k
    public final int d(j$.time.temporal.a aVar) {
        return aVar == j$.time.temporal.a.MONTH_OF_YEAR ? ordinal() + 1 : j$.time.temporal.j.a(this, aVar);
    }

    @Override // j$.time.temporal.k
    public final boolean e(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? lVar == j$.time.temporal.a.MONTH_OF_YEAR : lVar != null && lVar.c(this);
    }

    public final int f(boolean z) {
        switch (j.a[ordinal()]) {
            case 1:
                return 32;
            case 2:
                return (z ? 1 : 0) + 91;
            case 3:
                return (z ? 1 : 0) + NotificationCenter.recordStartError;
            case 4:
                return (z ? 1 : 0) + NotificationCenter.reloadInterface;
            case 5:
                return (z ? 1 : 0) + 305;
            case 6:
                return 1;
            case 7:
                return (z ? 1 : 0) + 60;
            case 8:
                return (z ? 1 : 0) + 121;
            case 9:
                return (z ? 1 : 0) + NotificationCenter.didStartedMultiGiftsSelector;
            case 10:
                return (z ? 1 : 0) + NotificationCenter.starTransactionsLoaded;
            case 11:
                return (z ? 1 : 0) + NotificationCenter.onRequestPermissionResultReceived;
            default:
                return (z ? 1 : 0) + 335;
        }
    }

    public final int g(boolean z) {
        int i = j.a[ordinal()];
        return i != 1 ? (i == 2 || i == 3 || i == 4 || i == 5) ? 30 : 31 : z ? 29 : 28;
    }

    public final k i() {
        int i = ((int) 1) + 12;
        return a[(i + ordinal()) % 12];
    }
}
