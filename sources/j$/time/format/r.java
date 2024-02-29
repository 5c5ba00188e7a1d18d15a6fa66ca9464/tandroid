package j$.time.format;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class r {
    public static final /* synthetic */ int f = 0;
    private r a;
    private final r b;
    private final List c;
    private final boolean d;
    private int e;

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

    private r(r rVar, boolean z) {
        this.a = this;
        this.c = new ArrayList();
        this.e = -1;
        this.b = rVar;
        this.d = z;
    }

    private int d(h hVar) {
        Objects.requireNonNull(hVar, "pp");
        r rVar = this.a;
        Objects.requireNonNull(rVar);
        rVar.c.add(hVar);
        r rVar2 = this.a;
        rVar2.e = -1;
        return rVar2.c.size() - 1;
    }

    private r k(k kVar) {
        k c;
        r rVar = this.a;
        int i = rVar.e;
        if (i >= 0) {
            k kVar2 = (k) rVar.c.get(i);
            if (kVar.b == kVar.c && k.b(kVar) == y.NOT_NEGATIVE) {
                c = kVar2.d(kVar.c);
                d(kVar.c());
                this.a.e = i;
            } else {
                c = kVar2.c();
                this.a.e = d(kVar);
            }
            this.a.c.set(i, c);
        } else {
            rVar.e = d(kVar);
        }
        return this;
    }

    private a v(Locale locale, int i, j$.time.chrono.g gVar) {
        Objects.requireNonNull(locale, "locale");
        while (this.a.b != null) {
            p();
        }
        return new a(new g(this.c, false), locale, w.a, i, null, gVar, null);
    }

    public r a(a aVar) {
        d(aVar.f(false));
        return this;
    }

    public r b(j$.time.temporal.l lVar, int i, int i2, boolean z) {
        d(new i(lVar, i, i2, z));
        return this;
    }

    public r c() {
        d(new j(-2));
        return this;
    }

    public r e(char c) {
        d(new f(c));
        return this;
    }

    public r f(String str) {
        if (str.length() > 0) {
            d(str.length() == 1 ? new f(str.charAt(0)) : new n(str));
        }
        return this;
    }

    public r g(String str, String str2) {
        d(new l(str, str2));
        return this;
    }

    public r h() {
        d(l.d);
        return this;
    }

    public r i(j$.time.temporal.l lVar, TextStyle textStyle) {
        Objects.requireNonNull(lVar, "field");
        Objects.requireNonNull(textStyle, "textStyle");
        d(new o(lVar, textStyle, new v()));
        return this;
    }

    public r j(j$.time.temporal.l lVar, Map map) {
        Objects.requireNonNull(lVar, "field");
        LinkedHashMap linkedHashMap = new LinkedHashMap(map);
        TextStyle textStyle = TextStyle.FULL;
        d(new o(lVar, textStyle, new c(this, new u(Collections.singletonMap(textStyle, linkedHashMap)))));
        return this;
    }

    public r l(j$.time.temporal.l lVar, int i) {
        Objects.requireNonNull(lVar, "field");
        if (i >= 1 && i <= 19) {
            k(new k(lVar, i, i, y.NOT_NEGATIVE));
            return this;
        }
        throw new IllegalArgumentException("The width must be from 1 to 19 inclusive but was " + i);
    }

    public r m(j$.time.temporal.l lVar, int i, int i2, y yVar) {
        if (i == i2 && yVar == y.NOT_NEGATIVE) {
            l(lVar, i2);
            return this;
        }
        Objects.requireNonNull(lVar, "field");
        Objects.requireNonNull(yVar, "signStyle");
        if (i < 1 || i > 19) {
            throw new IllegalArgumentException("The minimum width must be from 1 to 19 inclusive but was " + i);
        } else if (i2 < 1 || i2 > 19) {
            throw new IllegalArgumentException("The maximum width must be from 1 to 19 inclusive but was " + i2);
        } else if (i2 >= i) {
            k(new k(lVar, i, i2, yVar));
            return this;
        } else {
            throw new IllegalArgumentException("The maximum width must exceed or equal the minimum width but " + i2 + " < " + i);
        }
    }

    public r n() {
        d(new p(b.a, "ZoneRegionId()"));
        return this;
    }

    public r o(TextStyle textStyle) {
        d(new q(textStyle, null));
        return this;
    }

    public r p() {
        r rVar = this.a;
        if (rVar.b != null) {
            if (rVar.c.size() > 0) {
                r rVar2 = this.a;
                g gVar = new g(rVar2.c, rVar2.d);
                this.a = this.a.b;
                d(gVar);
            } else {
                this.a = this.a.b;
            }
            return this;
        }
        throw new IllegalStateException("Cannot call optionalEnd() as there was no previous call to optionalStart()");
    }

    public r q() {
        r rVar = this.a;
        rVar.e = -1;
        this.a = new r(rVar, true);
        return this;
    }

    public r r() {
        d(m.INSENSITIVE);
        return this;
    }

    public r s() {
        d(m.SENSITIVE);
        return this;
    }

    public r t() {
        d(m.LENIENT);
        return this;
    }

    public a u(Locale locale) {
        return v(locale, 2, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public a w(int i, j$.time.chrono.g gVar) {
        return v(Locale.getDefault(), i, gVar);
    }
}
