package j$.time.format;

import java.util.Locale;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class o implements h {
    private final j$.time.temporal.l a;
    private final TextStyle b;
    private final v c;
    private volatile k d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public o(j$.time.temporal.l lVar, TextStyle textStyle, v vVar) {
        this.a = lVar;
        this.b = textStyle;
        this.c = vVar;
    }

    @Override // j$.time.format.h
    public boolean a(t tVar, StringBuilder sb) {
        String c;
        j$.time.chrono.h hVar;
        Long e = tVar.e(this.a);
        if (e == null) {
            return false;
        }
        j$.time.temporal.k d = tVar.d();
        int i = j$.time.temporal.t.a;
        j$.time.chrono.g gVar = (j$.time.chrono.g) d.d(j$.time.temporal.n.a);
        if (gVar != null && gVar != (hVar = j$.time.chrono.h.a)) {
            v vVar = this.c;
            j$.time.temporal.l lVar = this.a;
            long longValue = e.longValue();
            TextStyle textStyle = this.b;
            Locale c2 = tVar.c();
            Objects.requireNonNull(vVar);
            c = (gVar == hVar || !(lVar instanceof j$.time.temporal.a)) ? vVar.c(lVar, longValue, textStyle, c2) : null;
        } else {
            c = this.c.c(this.a, e.longValue(), this.b, tVar.c());
        }
        if (c != null) {
            sb.append(c);
            return true;
        }
        if (this.d == null) {
            this.d = new k(this.a, 1, 19, y.NORMAL);
        }
        return this.d.a(tVar, sb);
    }

    public String toString() {
        StringBuilder sb;
        Object obj;
        if (this.b == TextStyle.FULL) {
            sb = new StringBuilder();
            sb.append("Text(");
            obj = this.a;
        } else {
            sb = new StringBuilder();
            sb.append("Text(");
            sb.append(this.a);
            sb.append(",");
            obj = this.b;
        }
        sb.append(obj);
        sb.append(")");
        return sb.toString();
    }
}
