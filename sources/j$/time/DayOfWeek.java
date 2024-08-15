package j$.time;

import j$.time.format.TextStyle;
import j$.time.format.r;
import j$.time.temporal.p;
import j$.time.temporal.q;
import java.util.Locale;
/* loaded from: classes2.dex */
public enum DayOfWeek implements j$.time.temporal.k {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;
    
    private static final DayOfWeek[] a = values();

    public static DayOfWeek f(int i) {
        if (i < 1 || i > 7) {
            throw new d("Invalid value for DayOfWeek: " + i);
        }
        return a[i - 1];
    }

    @Override // j$.time.temporal.k
    public final q a(j$.time.temporal.l lVar) {
        return lVar == j$.time.temporal.a.DAY_OF_WEEK ? lVar.c() : j$.time.temporal.j.c(this, lVar);
    }

    @Override // j$.time.temporal.k
    public final boolean b(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? lVar == j$.time.temporal.a.DAY_OF_WEEK : lVar != null && lVar.a(this);
    }

    @Override // j$.time.temporal.k
    public final long c(j$.time.temporal.l lVar) {
        if (lVar == j$.time.temporal.a.DAY_OF_WEEK) {
            return ordinal() + 1;
        }
        if (lVar instanceof j$.time.temporal.a) {
            throw new p("Unsupported field: " + lVar);
        }
        return lVar.d(this);
    }

    @Override // j$.time.temporal.k
    public final Object d(j$.time.temporal.n nVar) {
        return nVar == j$.time.temporal.j.h() ? j$.time.temporal.b.DAYS : j$.time.temporal.j.b(this, nVar);
    }

    @Override // j$.time.temporal.k
    public final int e(j$.time.temporal.a aVar) {
        return aVar == j$.time.temporal.a.DAY_OF_WEEK ? ordinal() + 1 : j$.time.temporal.j.a(this, aVar);
    }

    public String getDisplayName(TextStyle textStyle, Locale locale) {
        r rVar = new r();
        rVar.i(j$.time.temporal.a.DAY_OF_WEEK, textStyle);
        return rVar.v(locale).a(this);
    }
}
