package j$.time.format;

import j$.time.ZoneId;
import j$.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
/* loaded from: classes2.dex */
public final class q {
    private static final b f = new j$.time.temporal.n() { // from class: j$.time.format.b
        @Override // j$.time.temporal.n
        public final Object a(j$.time.temporal.k kVar) {
            ZoneId zoneId = (ZoneId) kVar.c(j$.time.temporal.j.j());
            if (zoneId == null || (zoneId instanceof ZoneOffset)) {
                return null;
            }
            return zoneId;
        }
    };
    private q a;
    private final q b;
    private final ArrayList c;
    private final boolean d;
    private int e;

    /* JADX WARN: Type inference failed for: r0v0, types: [j$.time.format.b] */
    static {
        HashMap hashMap = new HashMap();
        hashMap.put('G', j$.time.temporal.a.ERA);
        hashMap.put('y', j$.time.temporal.a.YEAR_OF_ERA);
        hashMap.put('u', j$.time.temporal.a.YEAR);
        j$.time.temporal.l lVar = j$.time.temporal.i.a;
        hashMap.put('Q', lVar);
        hashMap.put('q', lVar);
        j$.time.temporal.a aVar = j$.time.temporal.a.MONTH_OF_YEAR;
        hashMap.put('M', aVar);
        hashMap.put('L', aVar);
        hashMap.put('D', j$.time.temporal.a.DAY_OF_YEAR);
        hashMap.put('d', j$.time.temporal.a.DAY_OF_MONTH);
        hashMap.put('F', j$.time.temporal.a.ALIGNED_DAY_OF_WEEK_IN_MONTH);
        j$.time.temporal.a aVar2 = j$.time.temporal.a.DAY_OF_WEEK;
        hashMap.put('E', aVar2);
        hashMap.put('c', aVar2);
        hashMap.put('e', aVar2);
        hashMap.put('a', j$.time.temporal.a.AMPM_OF_DAY);
        hashMap.put('H', j$.time.temporal.a.HOUR_OF_DAY);
        hashMap.put('k', j$.time.temporal.a.CLOCK_HOUR_OF_DAY);
        hashMap.put('K', j$.time.temporal.a.HOUR_OF_AMPM);
        hashMap.put('h', j$.time.temporal.a.CLOCK_HOUR_OF_AMPM);
        hashMap.put('m', j$.time.temporal.a.MINUTE_OF_HOUR);
        hashMap.put('s', j$.time.temporal.a.SECOND_OF_MINUTE);
        j$.time.temporal.a aVar3 = j$.time.temporal.a.NANO_OF_SECOND;
        hashMap.put('S', aVar3);
        hashMap.put('A', j$.time.temporal.a.MILLI_OF_DAY);
        hashMap.put('n', aVar3);
        hashMap.put('N', j$.time.temporal.a.NANO_OF_DAY);
    }

    public q() {
        this.a = this;
        this.c = new ArrayList();
        this.e = -1;
        this.b = null;
        this.d = false;
    }

    private q(q qVar) {
        this.a = this;
        this.c = new ArrayList();
        this.e = -1;
        this.b = qVar;
        this.d = true;
    }

    private int d(g gVar) {
        j$.util.a.B(gVar, "pp");
        q qVar = this.a;
        qVar.getClass();
        qVar.c.add(gVar);
        q qVar2 = this.a;
        qVar2.e = -1;
        return qVar2.c.size() - 1;
    }

    private void k(j jVar) {
        j c;
        q qVar = this.a;
        int i = qVar.e;
        if (i < 0) {
            qVar.e = d(jVar);
            return;
        }
        j jVar2 = (j) qVar.c.get(i);
        int i2 = jVar.b;
        int i3 = jVar.c;
        if (i2 == i3 && j.b(jVar) == y.NOT_NEGATIVE) {
            c = jVar2.d(i3);
            d(jVar.c());
            this.a.e = i;
        } else {
            c = jVar2.c();
            this.a.e = d(jVar);
        }
        this.a.c.set(i, c);
    }

    private a w(Locale locale, x xVar, j$.time.chrono.g gVar) {
        j$.util.a.B(locale, "locale");
        while (this.a.b != null) {
            p();
        }
        f fVar = new f(this.c, false);
        w wVar = w.a;
        return new a(fVar, locale, xVar, gVar);
    }

    public final void a(a aVar) {
        d(aVar.f());
    }

    public final void b(j$.time.temporal.a aVar) {
        d(new h(aVar));
    }

    public final void c() {
        d(new i());
    }

    public final void e(char c) {
        d(new e(c));
    }

    public final void f(String str) {
        if (str.length() > 0) {
            d(str.length() == 1 ? new e(str.charAt(0)) : new m(str));
        }
    }

    public final void g(String str, String str2) {
        d(new k(str, str2));
    }

    public final void h() {
        d(k.d);
    }

    public final void i(j$.time.temporal.a aVar, TextStyle textStyle) {
        j$.util.a.B(aVar, "field");
        j$.util.a.B(textStyle, "textStyle");
        d(new n(aVar, textStyle, new v()));
    }

    public final void j(j$.time.temporal.a aVar, HashMap hashMap) {
        j$.util.a.B(aVar, "field");
        LinkedHashMap linkedHashMap = new LinkedHashMap(hashMap);
        TextStyle textStyle = TextStyle.FULL;
        d(new n(aVar, textStyle, new c(new u(Collections.singletonMap(textStyle, linkedHashMap)))));
    }

    public final void l(j$.time.temporal.l lVar, int i) {
        j$.util.a.B(lVar, "field");
        if (i >= 1 && i <= 19) {
            k(new j(lVar, i, i, y.NOT_NEGATIVE));
            return;
        }
        throw new IllegalArgumentException("The width must be from 1 to 19 inclusive but was " + i);
    }

    public final void m(j$.time.temporal.l lVar, int i, int i2, y yVar) {
        if (i == i2 && yVar == y.NOT_NEGATIVE) {
            l(lVar, i2);
            return;
        }
        j$.util.a.B(lVar, "field");
        j$.util.a.B(yVar, "signStyle");
        if (i < 1 || i > 19) {
            throw new IllegalArgumentException("The minimum width must be from 1 to 19 inclusive but was " + i);
        } else if (i2 < 1 || i2 > 19) {
            throw new IllegalArgumentException("The maximum width must be from 1 to 19 inclusive but was " + i2);
        } else if (i2 >= i) {
            k(new j(lVar, i, i2, yVar));
        } else {
            throw new IllegalArgumentException("The maximum width must exceed or equal the minimum width but " + i2 + " < " + i);
        }
    }

    public final void n() {
        d(new o(f, "ZoneRegionId()"));
    }

    public final void o(TextStyle textStyle) {
        d(new p(textStyle));
    }

    public final void p() {
        q qVar = this.a;
        if (qVar.b == null) {
            throw new IllegalStateException("Cannot call optionalEnd() as there was no previous call to optionalStart()");
        }
        if (qVar.c.size() <= 0) {
            this.a = this.a.b;
            return;
        }
        q qVar2 = this.a;
        f fVar = new f(qVar2.c, qVar2.d);
        this.a = this.a.b;
        d(fVar);
    }

    public final void q() {
        q qVar = this.a;
        qVar.e = -1;
        this.a = new q(qVar);
    }

    public final void r() {
        d(l.INSENSITIVE);
    }

    public final void s() {
        d(l.SENSITIVE);
    }

    public final void t() {
        d(l.LENIENT);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final a u(x xVar, j$.time.chrono.g gVar) {
        return w(Locale.getDefault(), xVar, gVar);
    }

    public final a v(Locale locale) {
        return w(locale, x.SMART, null);
    }
}
