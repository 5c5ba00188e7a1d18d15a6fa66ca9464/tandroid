package j$.time;

import j$.time.format.TextStyle;
import j$.time.format.r;
import j$.time.temporal.t;
import j$.time.temporal.u;
import j$.time.temporal.w;
import j$.time.temporal.x;
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

    public static DayOfWeek g(int i) {
        if (i < 1 || i > 7) {
            throw new c("Invalid value for DayOfWeek: " + i);
        }
        return a[i - 1];
    }

    @Override // j$.time.temporal.k
    public int a(j$.time.temporal.l lVar) {
        return lVar == j$.time.temporal.a.DAY_OF_WEEK ? f() : j$.time.temporal.j.a(this, lVar);
    }

    @Override // j$.time.temporal.k
    public x b(j$.time.temporal.l lVar) {
        return lVar == j$.time.temporal.a.DAY_OF_WEEK ? lVar.a() : j$.time.temporal.j.c(this, lVar);
    }

    @Override // j$.time.temporal.k
    public long c(j$.time.temporal.l lVar) {
        if (lVar == j$.time.temporal.a.DAY_OF_WEEK) {
            return f();
        }
        if (lVar instanceof j$.time.temporal.a) {
            throw new w("Unsupported field: " + lVar);
        }
        return lVar.c(this);
    }

    @Override // j$.time.temporal.k
    public Object d(u uVar) {
        int i = t.a;
        return uVar == j$.time.temporal.o.a ? j$.time.temporal.b.DAYS : j$.time.temporal.j.b(this, uVar);
    }

    @Override // j$.time.temporal.k
    public boolean e(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? lVar == j$.time.temporal.a.DAY_OF_WEEK : lVar != null && lVar.d(this);
    }

    public int f() {
        return ordinal() + 1;
    }

    public String getDisplayName(TextStyle textStyle, Locale locale) {
        r rVar = new r();
        rVar.i(j$.time.temporal.a.DAY_OF_WEEK, textStyle);
        return rVar.u(locale).a(this);
    }
}
