package j$.time;

import j$.time.temporal.q;
import java.io.Serializable;
/* loaded from: classes2.dex */
public final class p implements j$.time.temporal.k, Serializable, Comparable {
    private final g a;
    private final ZoneOffset b;
    private final ZoneId c;

    private p(g gVar, ZoneOffset zoneOffset, ZoneId zoneId) {
        this.a = gVar;
        this.b = zoneOffset;
        this.c = zoneId;
    }

    public static p g(Instant instant, ZoneId zoneId) {
        j$.util.a.B(instant, "instant");
        long i = instant.i();
        int j = instant.j();
        ZoneOffset offset = zoneId.getRules().getOffset(Instant.l(i, j));
        return new p(g.j(i, j, offset), offset, zoneId);
    }

    @Override // j$.time.temporal.k
    public final q a(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? (lVar == j$.time.temporal.a.INSTANT_SECONDS || lVar == j$.time.temporal.a.OFFSET_SECONDS) ? ((j$.time.temporal.a) lVar).a() : this.a.a(lVar) : lVar.d(this);
    }

    @Override // j$.time.temporal.k
    public final long b(j$.time.temporal.l lVar) {
        if (lVar instanceof j$.time.temporal.a) {
            int i = o.a[((j$.time.temporal.a) lVar).ordinal()];
            return i != 1 ? i != 2 ? this.a.b(lVar) : this.b.getTotalSeconds() : h();
        }
        return lVar.b(this);
    }

    @Override // j$.time.temporal.k
    public final Object c(j$.time.temporal.n nVar) {
        j$.time.temporal.m e = j$.time.temporal.j.e();
        g gVar = this.a;
        if (nVar == e) {
            return gVar.l();
        }
        if (nVar == j$.time.temporal.j.i() || nVar == j$.time.temporal.j.j()) {
            return this.c;
        }
        if (nVar == j$.time.temporal.j.g()) {
            return this.b;
        }
        if (nVar == j$.time.temporal.j.f()) {
            return gVar.m();
        }
        if (nVar != j$.time.temporal.j.d()) {
            return nVar == j$.time.temporal.j.h() ? j$.time.temporal.b.NANOS : nVar.a(this);
        }
        gVar.l().getClass();
        return j$.time.chrono.g.a;
    }

    @Override // java.lang.Comparable
    public final int compareTo(Object obj) {
        p pVar = (p) obj;
        int compare = Long.compare(h(), pVar.h());
        if (compare == 0) {
            g gVar = this.a;
            int h = gVar.m().h();
            g gVar2 = pVar.a;
            int h2 = h - gVar2.m().h();
            if (h2 == 0 && (h2 = gVar.compareTo(gVar2)) == 0) {
                int compareTo = this.c.getId().compareTo(pVar.c.getId());
                if (compareTo == 0) {
                    gVar.l().getClass();
                    j$.time.chrono.g gVar3 = j$.time.chrono.g.a;
                    gVar2.l().getClass();
                    gVar3.getClass();
                    gVar3.getClass();
                    return 0;
                }
                return compareTo;
            }
            return h2;
        }
        return compare;
    }

    @Override // j$.time.temporal.k
    public final int d(j$.time.temporal.a aVar) {
        if (aVar instanceof j$.time.temporal.a) {
            int i = o.a[aVar.ordinal()];
            if (i != 1) {
                return i != 2 ? this.a.d(aVar) : this.b.getTotalSeconds();
            }
            throw new j$.time.temporal.p("Invalid field 'InstantSeconds' for get() method, use getLong() instead");
        }
        return j$.time.chrono.d.a(this, aVar);
    }

    @Override // j$.time.temporal.k
    public final boolean e(j$.time.temporal.l lVar) {
        return (lVar instanceof j$.time.temporal.a) || (lVar != null && lVar.c(this));
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof p) {
            p pVar = (p) obj;
            return this.a.equals(pVar.a) && this.b.equals(pVar.b) && this.c.equals(pVar.c);
        }
        return false;
    }

    public final ZoneOffset f() {
        return this.b;
    }

    public final long h() {
        g gVar = this.a;
        return ((gVar.l().s() * 86400) + gVar.m().m()) - this.b.getTotalSeconds();
    }

    public final int hashCode() {
        return (this.a.hashCode() ^ this.b.hashCode()) ^ Integer.rotateLeft(this.c.hashCode(), 3);
    }

    public final g i() {
        return this.a;
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.a.toString());
        ZoneOffset zoneOffset = this.b;
        sb.append(zoneOffset.toString());
        String sb2 = sb.toString();
        ZoneId zoneId = this.c;
        if (zoneOffset != zoneId) {
            return sb2 + '[' + zoneId.toString() + ']';
        }
        return sb2;
    }
}
