package j$.time;

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
public final class g implements j$.time.temporal.k, j$.time.chrono.c, Serializable {
    private final f a;
    private final i b;

    static {
        f fVar = f.d;
        i iVar = i.e;
        Objects.requireNonNull(fVar, "date");
        Objects.requireNonNull(iVar, "time");
        f fVar2 = f.e;
        i iVar2 = i.f;
        Objects.requireNonNull(fVar2, "date");
        Objects.requireNonNull(iVar2, "time");
    }

    private g(f fVar, i iVar) {
        this.a = fVar;
        this.b = iVar;
    }

    public static g j(int i, int i2, int i3, int i4, int i5) {
        return new g(f.n(i, i2, i3), i.j(i4, i5));
    }

    public static g k(long j, int i, ZoneOffset zoneOffset) {
        long totalSeconds;
        Objects.requireNonNull(zoneOffset, "offset");
        long j2 = i;
        j$.time.temporal.a.NANO_OF_SECOND.h(j2);
        return new g(f.o(j$.lang.d.d(j + zoneOffset.getTotalSeconds(), 86400L)), i.k((((int) j$.lang.d.c(totalSeconds, 86400L)) * 1000000000) + j2));
    }

    @Override // j$.time.temporal.k
    public int a(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? ((j$.time.temporal.a) lVar).b() ? this.b.a(lVar) : this.a.a(lVar) : j$.time.temporal.j.a(this, lVar);
    }

    @Override // j$.time.temporal.k
    public x b(j$.time.temporal.l lVar) {
        if (lVar instanceof j$.time.temporal.a) {
            if (((j$.time.temporal.a) lVar).b()) {
                i iVar = this.b;
                Objects.requireNonNull(iVar);
                return j$.time.temporal.j.c(iVar, lVar);
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
        if (uVar != j$.time.temporal.n.a) {
            return uVar == j$.time.temporal.o.a ? j$.time.temporal.b.NANOS : uVar.a(this);
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
        if (obj instanceof g) {
            g gVar = (g) obj;
            return this.a.equals(gVar.a) && this.b.equals(gVar.b);
        }
        return false;
    }

    @Override // java.lang.Comparable
    /* renamed from: f */
    public int compareTo(j$.time.chrono.c cVar) {
        if (cVar instanceof g) {
            g gVar = (g) cVar;
            int g = this.a.g(gVar.a);
            return g == 0 ? this.b.compareTo(gVar.b) : g;
        }
        g gVar2 = (g) cVar;
        int compareTo = ((f) n()).compareTo(gVar2.n());
        if (compareTo == 0) {
            int compareTo2 = o().compareTo(gVar2.o());
            if (compareTo2 == 0) {
                g();
                j$.time.chrono.h hVar = j$.time.chrono.h.a;
                gVar2.g();
                return 0;
            }
            return compareTo2;
        }
        return compareTo;
    }

    public j$.time.chrono.g g() {
        Objects.requireNonNull((f) n());
        return j$.time.chrono.h.a;
    }

    public int h() {
        return this.b.i();
    }

    public int hashCode() {
        return this.a.hashCode() ^ this.b.hashCode();
    }

    public int i() {
        return this.a.l();
    }

    public long l(ZoneOffset zoneOffset) {
        Objects.requireNonNull(zoneOffset, "offset");
        return ((((f) n()).q() * 86400) + o().m()) - zoneOffset.getTotalSeconds();
    }

    public f m() {
        return this.a;
    }

    public j$.time.chrono.b n() {
        return this.a;
    }

    public i o() {
        return this.b;
    }

    public String toString() {
        return this.a.toString() + 'T' + this.b.toString();
    }
}
