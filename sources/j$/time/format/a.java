package j$.time.format;

import j$.time.ZoneId;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes2.dex */
public final class a {
    public static final a f;
    private final g a;
    private final Locale b;
    private final w c;
    private final j$.time.chrono.g d;
    private final ZoneId e;

    static {
        r rVar = new r();
        j$.time.temporal.a aVar = j$.time.temporal.a.YEAR;
        y yVar = y.EXCEEDS_PAD;
        r m = rVar.m(aVar, 4, 10, yVar);
        m.e('-');
        j$.time.temporal.a aVar2 = j$.time.temporal.a.MONTH_OF_YEAR;
        m.l(aVar2, 2);
        m.e('-');
        j$.time.temporal.a aVar3 = j$.time.temporal.a.DAY_OF_MONTH;
        m.l(aVar3, 2);
        j$.time.chrono.h hVar = j$.time.chrono.h.a;
        a w = m.w(1, hVar);
        r rVar2 = new r();
        rVar2.r();
        rVar2.a(w);
        rVar2.h();
        rVar2.w(1, hVar);
        r rVar3 = new r();
        rVar3.r();
        rVar3.a(w);
        rVar3.q();
        rVar3.h();
        rVar3.w(1, hVar);
        r rVar4 = new r();
        j$.time.temporal.a aVar4 = j$.time.temporal.a.HOUR_OF_DAY;
        rVar4.l(aVar4, 2);
        rVar4.e(':');
        j$.time.temporal.a aVar5 = j$.time.temporal.a.MINUTE_OF_HOUR;
        rVar4.l(aVar5, 2);
        rVar4.q();
        rVar4.e(':');
        j$.time.temporal.a aVar6 = j$.time.temporal.a.SECOND_OF_MINUTE;
        rVar4.l(aVar6, 2);
        rVar4.q();
        rVar4.b(j$.time.temporal.a.NANO_OF_SECOND, 0, 9, true);
        a w2 = rVar4.w(1, null);
        r rVar5 = new r();
        rVar5.r();
        rVar5.a(w2);
        rVar5.h();
        rVar5.w(1, null);
        r rVar6 = new r();
        rVar6.r();
        rVar6.a(w2);
        rVar6.q();
        rVar6.h();
        rVar6.w(1, null);
        r rVar7 = new r();
        rVar7.r();
        rVar7.a(w);
        rVar7.e('T');
        rVar7.a(w2);
        a w3 = rVar7.w(1, hVar);
        r rVar8 = new r();
        rVar8.r();
        rVar8.a(w3);
        rVar8.h();
        a w4 = rVar8.w(1, hVar);
        r rVar9 = new r();
        rVar9.a(w4);
        rVar9.q();
        rVar9.e('[');
        rVar9.s();
        rVar9.n();
        rVar9.e(']');
        rVar9.w(1, hVar);
        r rVar10 = new r();
        rVar10.a(w3);
        rVar10.q();
        rVar10.h();
        rVar10.q();
        rVar10.e('[');
        rVar10.s();
        rVar10.n();
        rVar10.e(']');
        rVar10.w(1, hVar);
        r rVar11 = new r();
        rVar11.r();
        r m2 = rVar11.m(aVar, 4, 10, yVar);
        m2.e('-');
        m2.l(j$.time.temporal.a.DAY_OF_YEAR, 3);
        m2.q();
        m2.h();
        m2.w(1, hVar);
        r rVar12 = new r();
        rVar12.r();
        r m3 = rVar12.m(j$.time.temporal.i.c, 4, 10, yVar);
        m3.f("-W");
        m3.l(j$.time.temporal.i.b, 2);
        m3.e('-');
        j$.time.temporal.a aVar7 = j$.time.temporal.a.DAY_OF_WEEK;
        m3.l(aVar7, 1);
        m3.q();
        m3.h();
        m3.w(1, hVar);
        r rVar13 = new r();
        rVar13.r();
        rVar13.c();
        f = rVar13.w(1, null);
        r rVar14 = new r();
        rVar14.r();
        rVar14.l(aVar, 4);
        rVar14.l(aVar2, 2);
        rVar14.l(aVar3, 2);
        rVar14.q();
        rVar14.g("+HHMMss", "Z");
        rVar14.w(1, hVar);
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
        r m4 = rVar15.m(aVar3, 1, 2, y.NOT_NEGATIVE);
        m4.e(' ');
        m4.j(aVar2, hashMap2);
        m4.e(' ');
        m4.l(aVar, 4);
        m4.e(' ');
        m4.l(aVar4, 2);
        m4.e(':');
        m4.l(aVar5, 2);
        m4.q();
        m4.e(':');
        m4.l(aVar6, 2);
        m4.p();
        m4.e(' ');
        m4.g("+HHMM", "GMT");
        m4.w(2, hVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(g gVar, Locale locale, w wVar, int i, Set set, j$.time.chrono.g gVar2, ZoneId zoneId) {
        Objects.requireNonNull(gVar, "printerParser");
        this.a = gVar;
        Objects.requireNonNull(locale, "locale");
        this.b = locale;
        Objects.requireNonNull(wVar, "decimalStyle");
        this.c = wVar;
        x.a(i, "resolverStyle");
        this.d = gVar2;
        this.e = zoneId;
    }

    public String a(j$.time.temporal.k kVar) {
        StringBuilder sb = new StringBuilder(32);
        try {
            this.a.a(new t(kVar, this), sb);
            return sb.toString();
        } catch (IOException e) {
            throw new j$.time.c(e.getMessage(), e);
        }
    }

    public j$.time.chrono.g b() {
        return this.d;
    }

    public w c() {
        return this.c;
    }

    public Locale d() {
        return this.b;
    }

    public ZoneId e() {
        return this.e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public g f(boolean z) {
        return this.a.b(z);
    }

    public String toString() {
        String gVar = this.a.toString();
        return gVar.startsWith("[") ? gVar : gVar.substring(1, gVar.length() - 1);
    }
}
