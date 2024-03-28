package j$.time;

import j$.time.temporal.n;
import j$.time.temporal.o;
import j$.time.temporal.p;
import j$.time.temporal.q;
import j$.time.temporal.r;
import j$.time.temporal.s;
import j$.time.temporal.t;
import j$.time.temporal.u;
import j$.time.temporal.x;
import java.io.Serializable;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class e implements j$.time.temporal.k, j$.time.chrono.c, Serializable {
    private final LocalDate a;
    private final g b;

    static {
        LocalDate localDate = LocalDate.d;
        g gVar = g.e;
        Objects.requireNonNull(localDate, "date");
        Objects.requireNonNull(gVar, "time");
        LocalDate localDate2 = LocalDate.e;
        g gVar2 = g.f;
        Objects.requireNonNull(localDate2, "date");
        Objects.requireNonNull(gVar2, "time");
    }

    private e(LocalDate localDate, g gVar) {
        this.a = localDate;
        this.b = gVar;
    }

    public static e j(int i, int i2, int i3, int i4, int i5) {
        return new e(LocalDate.of(i, i2, i3), g.j(i4, i5));
    }

    public static e k(long j, int i, ZoneOffset zoneOffset) {
        long totalSeconds;
        Objects.requireNonNull(zoneOffset, "offset");
        long j2 = i;
        j$.time.temporal.a.NANO_OF_SECOND.h(j2);
        return new e(LocalDate.p(j$.lang.d.d(j + zoneOffset.getTotalSeconds(), 86400L)), g.k((((int) j$.lang.d.c(totalSeconds, 86400L)) * 1000000000) + j2));
    }

    @Override // j$.time.temporal.k
    public int a(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? ((j$.time.temporal.a) lVar).b() ? this.b.a(lVar) : this.a.a(lVar) : j$.time.temporal.j.a(this, lVar);
    }

    @Override // j$.time.temporal.k
    public x b(j$.time.temporal.l lVar) {
        if (lVar instanceof j$.time.temporal.a) {
            if (((j$.time.temporal.a) lVar).b()) {
                g gVar = this.b;
                Objects.requireNonNull(gVar);
                return j$.time.temporal.j.c(gVar, lVar);
            }
            return this.a.b(lVar);
        }
        return lVar.e(this);
    }

    @Override // j$.time.temporal.k
    public long c(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? ((j$.time.temporal.a) lVar).b() ? this.b.c(lVar) : this.a.c(lVar) : lVar.c(this);
    }

    @Override // j$.time.temporal.k
    public Object d(u uVar) {
        int i = t.a;
        if (uVar == r.a) {
            return this.a;
        }
        if (uVar == j$.time.temporal.m.a || uVar == q.a || uVar == p.a) {
            return null;
        }
        if (uVar == s.a) {
            return o();
        }
        if (uVar != n.a) {
            return uVar == o.a ? j$.time.temporal.b.NANOS : uVar.a(this);
        }
        g();
        return j$.time.chrono.h.a;
    }

    @Override // j$.time.temporal.k
    public boolean e(j$.time.temporal.l lVar) {
        if (!(lVar instanceof j$.time.temporal.a)) {
            return lVar != null && lVar.d(this);
        }
        j$.time.temporal.a aVar = (j$.time.temporal.a) lVar;
        return aVar.f() || aVar.b();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof e) {
            e eVar = (e) obj;
            return this.a.equals(eVar.a) && this.b.equals(eVar.b);
        }
        return false;
    }

    @Override // java.lang.Comparable
    /* renamed from: f */
    public int compareTo(j$.time.chrono.c cVar) {
        if (cVar instanceof e) {
            e eVar = (e) cVar;
            int g = this.a.g(eVar.a);
            return g == 0 ? this.b.compareTo(eVar.b) : g;
        }
        e eVar2 = (e) cVar;
        int compareTo = ((LocalDate) n()).compareTo(eVar2.n());
        if (compareTo == 0) {
            int compareTo2 = o().compareTo(eVar2.o());
            if (compareTo2 == 0) {
                g();
                j$.time.chrono.h hVar = j$.time.chrono.h.a;
                eVar2.g();
                return 0;
            }
            return compareTo2;
        }
        return compareTo;
    }

    public j$.time.chrono.g g() {
        Objects.requireNonNull((LocalDate) n());
        return j$.time.chrono.h.a;
    }

    public int h() {
        return this.b.i();
    }

    public int hashCode() {
        return this.a.hashCode() ^ this.b.hashCode();
    }

    public int i() {
        return this.a.m();
    }

    public long l(ZoneOffset zoneOffset) {
        Objects.requireNonNull(zoneOffset, "offset");
        return ((((LocalDate) n()).s() * 86400) + o().m()) - zoneOffset.getTotalSeconds();
    }

    public LocalDate m() {
        return this.a;
    }

    public j$.time.chrono.b n() {
        return this.a;
    }

    public g o() {
        return this.b;
    }

    public String toString() {
        return this.a.toString() + 'T' + this.b.toString();
    }
}
