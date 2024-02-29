package j$.time;

import j$.time.temporal.p;
import j$.time.temporal.q;
import j$.time.temporal.r;
import j$.time.temporal.s;
import j$.time.temporal.t;
import j$.time.temporal.u;
import j$.time.temporal.w;
import j$.time.temporal.x;
import java.io.Serializable;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class o implements j$.time.temporal.k, j$.time.chrono.f, Serializable {
    private final g a;
    private final ZoneOffset b;
    private final ZoneId c;

    private o(g gVar, ZoneOffset zoneOffset, ZoneId zoneId) {
        this.a = gVar;
        this.b = zoneOffset;
        this.c = zoneId;
    }

    public static o i(Instant instant, ZoneId zoneId) {
        Objects.requireNonNull(instant, "instant");
        Objects.requireNonNull(zoneId, "zone");
        long i = instant.i();
        int j = instant.j();
        ZoneOffset offset = zoneId.getRules().getOffset(Instant.l(i, j));
        return new o(g.k(i, j, offset), offset, zoneId);
    }

    @Override // j$.time.temporal.k
    public int a(j$.time.temporal.l lVar) {
        if (lVar instanceof j$.time.temporal.a) {
            int i = n.a[((j$.time.temporal.a) lVar).ordinal()];
            if (i != 1) {
                return i != 2 ? this.a.a(lVar) : this.b.getTotalSeconds();
            }
            throw new w("Invalid field 'InstantSeconds' for get() method, use getLong() instead");
        }
        return j$.time.chrono.d.a(this, lVar);
    }

    @Override // j$.time.temporal.k
    public x b(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? (lVar == j$.time.temporal.a.INSTANT_SECONDS || lVar == j$.time.temporal.a.OFFSET_SECONDS) ? lVar.a() : this.a.b(lVar) : lVar.e(this);
    }

    @Override // j$.time.temporal.k
    public long c(j$.time.temporal.l lVar) {
        if (lVar instanceof j$.time.temporal.a) {
            int i = n.a[((j$.time.temporal.a) lVar).ordinal()];
            return i != 1 ? i != 2 ? this.a.c(lVar) : this.b.getTotalSeconds() : j();
        }
        return lVar.c(this);
    }

    @Override // java.lang.Comparable
    public int compareTo(Object obj) {
        o oVar = (o) ((j$.time.chrono.f) obj);
        int compare = Long.compare(j(), oVar.j());
        if (compare == 0) {
            int h = m().h() - oVar.m().h();
            if (h == 0) {
                int compareTo = ((g) l()).compareTo(oVar.l());
                if (compareTo == 0) {
                    int compareTo2 = h().getId().compareTo(oVar.h().getId());
                    if (compareTo2 == 0) {
                        f();
                        j$.time.chrono.h hVar = j$.time.chrono.h.a;
                        oVar.f();
                        return 0;
                    }
                    return compareTo2;
                }
                return compareTo;
            }
            return h;
        }
        return compare;
    }

    @Override // j$.time.temporal.k
    public Object d(u uVar) {
        int i = t.a;
        if (uVar == r.a) {
            return this.a.m();
        }
        if (uVar == q.a || uVar == j$.time.temporal.m.a) {
            return this.c;
        }
        if (uVar == p.a) {
            return this.b;
        }
        if (uVar == s.a) {
            return m();
        }
        if (uVar != j$.time.temporal.n.a) {
            return uVar == j$.time.temporal.o.a ? j$.time.temporal.b.NANOS : uVar.a(this);
        }
        f();
        return j$.time.chrono.h.a;
    }

    @Override // j$.time.temporal.k
    public boolean e(j$.time.temporal.l lVar) {
        return (lVar instanceof j$.time.temporal.a) || (lVar != null && lVar.d(this));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof o) {
            o oVar = (o) obj;
            return this.a.equals(oVar.a) && this.b.equals(oVar.b) && this.c.equals(oVar.c);
        }
        return false;
    }

    public j$.time.chrono.g f() {
        Objects.requireNonNull((f) k());
        return j$.time.chrono.h.a;
    }

    public ZoneOffset g() {
        return this.b;
    }

    public ZoneId h() {
        return this.c;
    }

    public int hashCode() {
        return (this.a.hashCode() ^ this.b.hashCode()) ^ Integer.rotateLeft(this.c.hashCode(), 3);
    }

    public long j() {
        return ((((f) k()).q() * 86400) + m().m()) - g().getTotalSeconds();
    }

    public j$.time.chrono.b k() {
        return this.a.m();
    }

    public j$.time.chrono.c l() {
        return this.a;
    }

    public i m() {
        return this.a.o();
    }

    public String toString() {
        String str = this.a.toString() + this.b.toString();
        if (this.b != this.c) {
            return str + '[' + this.c.toString() + ']';
        }
        return str;
    }
}
