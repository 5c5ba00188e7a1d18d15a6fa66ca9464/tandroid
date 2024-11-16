package j$.time.format;

import j$.time.ZoneId;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

/* loaded from: classes2.dex */
public final class a {
    public static final a f;
    private final f a;
    private final Locale b;
    private final w c;
    private final j$.time.chrono.f d;
    private final ZoneId e;

    static {
        q qVar = new q();
        j$.time.temporal.a aVar = j$.time.temporal.a.YEAR;
        y yVar = y.EXCEEDS_PAD;
        qVar.m(aVar, 4, 10, yVar);
        qVar.e('-');
        j$.time.temporal.a aVar2 = j$.time.temporal.a.MONTH_OF_YEAR;
        qVar.l(aVar2, 2);
        qVar.e('-');
        j$.time.temporal.a aVar3 = j$.time.temporal.a.DAY_OF_MONTH;
        qVar.l(aVar3, 2);
        x xVar = x.STRICT;
        j$.time.chrono.g gVar = j$.time.chrono.g.a;
        a u = qVar.u(xVar, gVar);
        q qVar2 = new q();
        qVar2.r();
        qVar2.a(u);
        qVar2.h();
        qVar2.u(xVar, gVar);
        q qVar3 = new q();
        qVar3.r();
        qVar3.a(u);
        qVar3.q();
        qVar3.h();
        qVar3.u(xVar, gVar);
        q qVar4 = new q();
        j$.time.temporal.a aVar4 = j$.time.temporal.a.HOUR_OF_DAY;
        qVar4.l(aVar4, 2);
        qVar4.e(':');
        j$.time.temporal.a aVar5 = j$.time.temporal.a.MINUTE_OF_HOUR;
        qVar4.l(aVar5, 2);
        qVar4.q();
        qVar4.e(':');
        j$.time.temporal.a aVar6 = j$.time.temporal.a.SECOND_OF_MINUTE;
        qVar4.l(aVar6, 2);
        qVar4.q();
        qVar4.b(j$.time.temporal.a.NANO_OF_SECOND);
        a u2 = qVar4.u(xVar, null);
        q qVar5 = new q();
        qVar5.r();
        qVar5.a(u2);
        qVar5.h();
        qVar5.u(xVar, null);
        q qVar6 = new q();
        qVar6.r();
        qVar6.a(u2);
        qVar6.q();
        qVar6.h();
        qVar6.u(xVar, null);
        q qVar7 = new q();
        qVar7.r();
        qVar7.a(u);
        qVar7.e('T');
        qVar7.a(u2);
        a u3 = qVar7.u(xVar, gVar);
        q qVar8 = new q();
        qVar8.r();
        qVar8.a(u3);
        qVar8.h();
        a u4 = qVar8.u(xVar, gVar);
        q qVar9 = new q();
        qVar9.a(u4);
        qVar9.q();
        qVar9.e('[');
        qVar9.s();
        qVar9.n();
        qVar9.e(']');
        qVar9.u(xVar, gVar);
        q qVar10 = new q();
        qVar10.a(u3);
        qVar10.q();
        qVar10.h();
        qVar10.q();
        qVar10.e('[');
        qVar10.s();
        qVar10.n();
        qVar10.e(']');
        qVar10.u(xVar, gVar);
        q qVar11 = new q();
        qVar11.r();
        qVar11.m(aVar, 4, 10, yVar);
        qVar11.e('-');
        qVar11.l(j$.time.temporal.a.DAY_OF_YEAR, 3);
        qVar11.q();
        qVar11.h();
        qVar11.u(xVar, gVar);
        q qVar12 = new q();
        qVar12.r();
        qVar12.m(j$.time.temporal.i.c, 4, 10, yVar);
        qVar12.f("-W");
        qVar12.l(j$.time.temporal.i.b, 2);
        qVar12.e('-');
        j$.time.temporal.a aVar7 = j$.time.temporal.a.DAY_OF_WEEK;
        qVar12.l(aVar7, 1);
        qVar12.q();
        qVar12.h();
        qVar12.u(xVar, gVar);
        q qVar13 = new q();
        qVar13.r();
        qVar13.c();
        f = qVar13.u(xVar, null);
        q qVar14 = new q();
        qVar14.r();
        qVar14.l(aVar, 4);
        qVar14.l(aVar2, 2);
        qVar14.l(aVar3, 2);
        qVar14.q();
        qVar14.g("+HHMMss", "Z");
        qVar14.u(xVar, gVar);
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
        q qVar15 = new q();
        qVar15.r();
        qVar15.t();
        qVar15.q();
        qVar15.j(aVar7, hashMap);
        qVar15.f(", ");
        qVar15.p();
        qVar15.m(aVar3, 1, 2, y.NOT_NEGATIVE);
        qVar15.e(' ');
        qVar15.j(aVar2, hashMap2);
        qVar15.e(' ');
        qVar15.l(aVar, 4);
        qVar15.e(' ');
        qVar15.l(aVar4, 2);
        qVar15.e(':');
        qVar15.l(aVar5, 2);
        qVar15.q();
        qVar15.e(':');
        qVar15.l(aVar6, 2);
        qVar15.p();
        qVar15.e(' ');
        qVar15.g("+HHMM", "GMT");
        qVar15.u(x.SMART, gVar);
    }

    a(f fVar, Locale locale, x xVar, j$.time.chrono.g gVar) {
        w wVar = w.a;
        this.a = fVar;
        j$.util.a.B(locale, "locale");
        this.b = locale;
        this.c = wVar;
        j$.util.a.B(xVar, "resolverStyle");
        this.d = gVar;
        this.e = null;
    }

    public final String a(j$.time.temporal.k kVar) {
        StringBuilder sb = new StringBuilder(32);
        j$.util.a.B(kVar, "temporal");
        try {
            this.a.a(new s(kVar, this), sb);
            return sb.toString();
        } catch (IOException e) {
            throw new j$.time.c(e.getMessage(), e);
        }
    }

    public final j$.time.chrono.f b() {
        return this.d;
    }

    public final w c() {
        return this.c;
    }

    public final Locale d() {
        return this.b;
    }

    public final ZoneId e() {
        return this.e;
    }

    final f f() {
        return this.a.b();
    }

    public final String toString() {
        String fVar = this.a.toString();
        return fVar.startsWith("[") ? fVar : fVar.substring(1, fVar.length() - 1);
    }
}
