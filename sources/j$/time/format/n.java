package j$.time.format;

import java.util.Locale;

/* loaded from: classes2.dex */
final class n implements g {
    private final j$.time.temporal.l a;
    private final TextStyle b;
    private final v c;
    private volatile j d;

    n(j$.time.temporal.a aVar, TextStyle textStyle, v vVar) {
        this.a = aVar;
        this.b = textStyle;
        this.c = vVar;
    }

    @Override // j$.time.format.g
    public final boolean a(s sVar, StringBuilder sb) {
        String c;
        j$.time.chrono.g gVar;
        Long e = sVar.e(this.a);
        if (e == null) {
            return false;
        }
        j$.time.chrono.f fVar = (j$.time.chrono.f) sVar.d().c(j$.time.temporal.j.d());
        if (fVar == null || fVar == (gVar = j$.time.chrono.g.a)) {
            c = this.c.c(this.a, e.longValue(), this.b, sVar.c());
        } else {
            v vVar = this.c;
            j$.time.temporal.l lVar = this.a;
            long longValue = e.longValue();
            TextStyle textStyle = this.b;
            Locale c2 = sVar.c();
            vVar.getClass();
            c = (fVar == gVar || !(lVar instanceof j$.time.temporal.a)) ? vVar.c(lVar, longValue, textStyle, c2) : null;
        }
        if (c != null) {
            sb.append(c);
            return true;
        }
        if (this.d == null) {
            this.d = new j(this.a, 1, 19, y.NORMAL);
        }
        return this.d.a(sVar, sb);
    }

    public final String toString() {
        StringBuilder sb;
        TextStyle textStyle = TextStyle.FULL;
        j$.time.temporal.l lVar = this.a;
        TextStyle textStyle2 = this.b;
        if (textStyle2 == textStyle) {
            sb = new StringBuilder("Text(");
            sb.append(lVar);
        } else {
            sb = new StringBuilder("Text(");
            sb.append(lVar);
            sb.append(",");
            sb.append(textStyle2);
        }
        sb.append(")");
        return sb.toString();
    }
}
