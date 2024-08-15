package j$.time.format;

import j$.time.ZoneId;
import j$.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
/* loaded from: classes2.dex */
public final class r {
    private static final a f = new j$.time.temporal.n() { // from class: j$.time.format.a
        @Override // j$.time.temporal.n
        public final Object a(j$.time.temporal.k kVar) {
            int i = r.g;
            ZoneId zoneId = (ZoneId) kVar.d(j$.time.temporal.j.j());
            if (zoneId == null || (zoneId instanceof ZoneOffset)) {
                return null;
            }
            return zoneId;
        }
    };
    public static final /* synthetic */ int g = 0;
    private r a;
    private final r b;
    private final ArrayList c;
    private final boolean d;
    private int e;

    /* JADX WARN: Type inference failed for: r0v0, types: [j$.time.format.a] */
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

    public r() {
        this.a = this;
        this.c = new ArrayList();
        this.e = -1;
        this.b = null;
        this.d = false;
    }

    private r(r rVar) {
        this.a = this;
        this.c = new ArrayList();
        this.e = -1;
        this.b = rVar;
        this.d = true;
    }

    private int d(h hVar) {
        if (hVar != null) {
            r rVar = this.a;
            rVar.getClass();
            rVar.c.add(hVar);
            r rVar2 = this.a;
            rVar2.e = -1;
            return rVar2.c.size() - 1;
        }
        throw new NullPointerException("pp");
    }

    private void l(k kVar) {
        k c;
        r rVar = this.a;
        int i = rVar.e;
        if (i < 0) {
            rVar.e = d(kVar);
            return;
        }
        k kVar2 = (k) rVar.c.get(i);
        int i2 = kVar.b;
        int i3 = kVar.c;
        if (i2 == i3 && k.b(kVar) == y.NOT_NEGATIVE) {
            c = kVar2.d(i3);
            d(kVar.c());
            this.a.e = i;
        } else {
            c = kVar2.c();
            this.a.e = d(kVar);
        }
        this.a.c.set(i, c);
    }

    private b w(Locale locale, x xVar, j$.time.chrono.e eVar) {
        if (locale != null) {
            while (this.a.b != null) {
                p();
            }
            g gVar = new g(this.c, false);
            w wVar = w.a;
            return new b(gVar, locale, xVar, eVar);
        }
        throw new NullPointerException("locale");
    }

    public final void a(b bVar) {
        d(bVar.e());
    }

    public final void b(j$.time.temporal.a aVar) {
        d(new i(aVar));
    }

    public final void c() {
        d(new j());
    }

    public final void e(char c) {
        d(new f(c));
    }

    public final void f(String str) {
        if (str.length() > 0) {
            d(str.length() == 1 ? new f(str.charAt(0)) : new n(str));
        }
    }

    public final void g(String str, String str2) {
        d(new l(str, str2));
    }

    public final void h() {
        d(l.d);
    }

    public final void i(j$.time.temporal.a aVar, TextStyle textStyle) {
        if (aVar == null) {
            throw new NullPointerException("field");
        }
        if (textStyle == null) {
            throw new NullPointerException("textStyle");
        }
        d(new o(aVar, textStyle, new v()));
    }

    public final void j(j$.time.temporal.a aVar, HashMap hashMap) {
        if (aVar == null) {
            throw new NullPointerException("field");
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap(hashMap);
        TextStyle textStyle = TextStyle.FULL;
        d(new o(aVar, textStyle, new c(new u(Collections.singletonMap(textStyle, linkedHashMap)))));
    }

    public final r k(j$.time.temporal.l lVar, int i, int i2, y yVar) {
        if (i == i2 && yVar == y.NOT_NEGATIVE) {
            m(lVar, i2);
            return this;
        } else if (lVar != null) {
            if (yVar != null) {
                if (i < 1 || i > 19) {
                    throw new IllegalArgumentException("The minimum width must be from 1 to 19 inclusive but was " + i);
                } else if (i2 < 1 || i2 > 19) {
                    throw new IllegalArgumentException("The maximum width must be from 1 to 19 inclusive but was " + i2);
                } else if (i2 >= i) {
                    l(new k(lVar, i, i2, yVar));
                    return this;
                } else {
                    throw new IllegalArgumentException("The maximum width must exceed or equal the minimum width but " + i2 + " < " + i);
                }
            }
            throw new NullPointerException("signStyle");
        } else {
            throw new NullPointerException("field");
        }
    }

    public final void m(j$.time.temporal.l lVar, int i) {
        if (lVar == null) {
            throw new NullPointerException("field");
        }
        if (i >= 1 && i <= 19) {
            l(new k(lVar, i, i, y.NOT_NEGATIVE));
            return;
        }
        throw new IllegalArgumentException("The width must be from 1 to 19 inclusive but was " + i);
    }

    public final void n() {
        d(new p(f, "ZoneRegionId()"));
    }

    public final void o(TextStyle textStyle) {
        d(new q(textStyle));
    }

    public final void p() {
        r rVar = this.a;
        if (rVar.b == null) {
            throw new IllegalStateException("Cannot call optionalEnd() as there was no previous call to optionalStart()");
        }
        if (rVar.c.size() <= 0) {
            this.a = this.a.b;
            return;
        }
        r rVar2 = this.a;
        g gVar = new g(rVar2.c, rVar2.d);
        this.a = this.a.b;
        d(gVar);
    }

    public final void q() {
        r rVar = this.a;
        rVar.e = -1;
        this.a = new r(rVar);
    }

    public final void r() {
        d(m.INSENSITIVE);
    }

    public final void s() {
        d(m.SENSITIVE);
    }

    public final void t() {
        d(m.LENIENT);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final b u(x xVar, j$.time.chrono.e eVar) {
        return w(Locale.getDefault(), xVar, eVar);
    }

    public final b v(Locale locale) {
        return w(locale, x.SMART, null);
    }
}
