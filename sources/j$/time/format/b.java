package j$.time.format;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
/* loaded from: classes2.dex */
public final class b {
    public static final b d;
    private final g a;
    private final Locale b;
    private final j$.time.chrono.d c;

    static {
        r rVar = new r();
        j$.time.temporal.a aVar = j$.time.temporal.a.YEAR;
        y yVar = y.EXCEEDS_PAD;
        rVar.k(aVar, 4, 10, yVar);
        rVar.e('-');
        j$.time.temporal.a aVar2 = j$.time.temporal.a.MONTH_OF_YEAR;
        rVar.m(aVar2, 2);
        rVar.e('-');
        j$.time.temporal.a aVar3 = j$.time.temporal.a.DAY_OF_MONTH;
        rVar.m(aVar3, 2);
        x xVar = x.STRICT;
        j$.time.chrono.e eVar = j$.time.chrono.e.a;
        b u = rVar.u(xVar, eVar);
        r rVar2 = new r();
        rVar2.r();
        rVar2.a(u);
        rVar2.h();
        rVar2.u(xVar, eVar);
        r rVar3 = new r();
        rVar3.r();
        rVar3.a(u);
        rVar3.q();
        rVar3.h();
        rVar3.u(xVar, eVar);
        r rVar4 = new r();
        j$.time.temporal.a aVar4 = j$.time.temporal.a.HOUR_OF_DAY;
        rVar4.m(aVar4, 2);
        rVar4.e(':');
        j$.time.temporal.a aVar5 = j$.time.temporal.a.MINUTE_OF_HOUR;
        rVar4.m(aVar5, 2);
        rVar4.q();
        rVar4.e(':');
        j$.time.temporal.a aVar6 = j$.time.temporal.a.SECOND_OF_MINUTE;
        rVar4.m(aVar6, 2);
        rVar4.q();
        rVar4.b(j$.time.temporal.a.NANO_OF_SECOND);
        b u2 = rVar4.u(xVar, null);
        r rVar5 = new r();
        rVar5.r();
        rVar5.a(u2);
        rVar5.h();
        rVar5.u(xVar, null);
        r rVar6 = new r();
        rVar6.r();
        rVar6.a(u2);
        rVar6.q();
        rVar6.h();
        rVar6.u(xVar, null);
        r rVar7 = new r();
        rVar7.r();
        rVar7.a(u);
        rVar7.e('T');
        rVar7.a(u2);
        b u3 = rVar7.u(xVar, eVar);
        r rVar8 = new r();
        rVar8.r();
        rVar8.a(u3);
        rVar8.h();
        b u4 = rVar8.u(xVar, eVar);
        r rVar9 = new r();
        rVar9.a(u4);
        rVar9.q();
        rVar9.e('[');
        rVar9.s();
        rVar9.n();
        rVar9.e(']');
        rVar9.u(xVar, eVar);
        r rVar10 = new r();
        rVar10.a(u3);
        rVar10.q();
        rVar10.h();
        rVar10.q();
        rVar10.e('[');
        rVar10.s();
        rVar10.n();
        rVar10.e(']');
        rVar10.u(xVar, eVar);
        r rVar11 = new r();
        rVar11.r();
        rVar11.k(aVar, 4, 10, yVar);
        rVar11.e('-');
        rVar11.m(j$.time.temporal.a.DAY_OF_YEAR, 3);
        rVar11.q();
        rVar11.h();
        rVar11.u(xVar, eVar);
        r rVar12 = new r();
        rVar12.r();
        rVar12.k(j$.time.temporal.i.c, 4, 10, yVar);
        rVar12.f("-W");
        rVar12.m(j$.time.temporal.i.b, 2);
        rVar12.e('-');
        j$.time.temporal.a aVar7 = j$.time.temporal.a.DAY_OF_WEEK;
        rVar12.m(aVar7, 1);
        rVar12.q();
        rVar12.h();
        rVar12.u(xVar, eVar);
        r rVar13 = new r();
        rVar13.r();
        rVar13.c();
        d = rVar13.u(xVar, null);
        r rVar14 = new r();
        rVar14.r();
        rVar14.m(aVar, 4);
        rVar14.m(aVar2, 2);
        rVar14.m(aVar3, 2);
        rVar14.q();
        rVar14.g("+HHMMss", "Z");
        rVar14.u(xVar, eVar);
        HashMap hashMap = new HashMap();
        hashMap.put(1L, "Mon");
        hashMap.put(2L, "Tue");
        hashMap.put(3L, "Wed");
        hashMap.put(4L, "Thu");
        hashMap.put(5L, "Fri");
        hashMap.put(6L, "Sat");
        hashMap.put(7L, "Sun");
        HashMap hashMap2 = new HashMap();
        hashMap2.put(1L, "Jan");
        hashMap2.put(2L, "Feb");
        hashMap2.put(3L, "Mar");
        hashMap2.put(4L, "Apr");
        hashMap2.put(5L, "May");
        hashMap2.put(6L, "Jun");
        hashMap2.put(7L, "Jul");
        hashMap2.put(8L, "Aug");
        hashMap2.put(9L, "Sep");
        hashMap2.put(10L, "Oct");
        hashMap2.put(11L, "Nov");
        hashMap2.put(12L, "Dec");
        r rVar15 = new r();
        rVar15.r();
        rVar15.t();
        rVar15.q();
        rVar15.j(aVar7, hashMap);
        rVar15.f(", ");
        rVar15.p();
        rVar15.k(aVar3, 1, 2, y.NOT_NEGATIVE);
        rVar15.e(' ');
        rVar15.j(aVar2, hashMap2);
        rVar15.e(' ');
        rVar15.m(aVar, 4);
        rVar15.e(' ');
        rVar15.m(aVar4, 2);
        rVar15.e(':');
        rVar15.m(aVar5, 2);
        rVar15.q();
        rVar15.e(':');
        rVar15.m(aVar6, 2);
        rVar15.p();
        rVar15.e(' ');
        rVar15.g("+HHMM", "GMT");
        rVar15.u(x.SMART, eVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(g gVar, Locale locale, x xVar, j$.time.chrono.e eVar) {
        w wVar = w.a;
        this.a = gVar;
        if (locale == null) {
            throw new NullPointerException("locale");
        }
        this.b = locale;
        if (xVar == null) {
            throw new NullPointerException("resolverStyle");
        }
        this.c = eVar;
    }

    public final String a(j$.time.temporal.k kVar) {
        StringBuilder sb = new StringBuilder(32);
        if (kVar != null) {
            try {
                this.a.a(new t(kVar, this), sb);
                return sb.toString();
            } catch (IOException e) {
                throw new j$.time.d(e.getMessage(), e);
            }
        }
        throw new NullPointerException("temporal");
    }

    public final j$.time.chrono.d b() {
        return this.c;
    }

    public final w c() {
        return w.a;
    }

    public final Locale d() {
        return this.b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final g e() {
        return this.a.b();
    }

    public final String toString() {
        String gVar = this.a.toString();
        return gVar.startsWith("[") ? gVar : gVar.substring(1, gVar.length() - 1);
    }
}
