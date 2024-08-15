package j$.time;

import j$.time.temporal.q;
import java.io.Serializable;
/* loaded from: classes2.dex */
public final class h implements j$.time.temporal.k, j$.time.chrono.c, Serializable {
    private final LocalDate a;
    private final j b;

    static {
        LocalDate localDate = LocalDate.d;
        j jVar = j.e;
        if (localDate == null) {
            throw new NullPointerException("date");
        }
        if (jVar == null) {
            throw new NullPointerException("time");
        }
        LocalDate localDate2 = LocalDate.e;
        j jVar2 = j.f;
        if (localDate2 == null) {
            throw new NullPointerException("date");
        }
        if (jVar2 == null) {
            throw new NullPointerException("time");
        }
    }

    private h(LocalDate localDate, j jVar) {
        this.a = localDate;
        this.b = jVar;
    }

    public static h i(int i) {
        return new h(LocalDate.of(i, 12, 31), j.j());
    }

    public static h j(long j, int i, ZoneOffset zoneOffset) {
        long totalSeconds;
        if (zoneOffset != null) {
            long j2 = i;
            j$.time.temporal.a.NANO_OF_SECOND.f(j2);
            return new h(LocalDate.o(a.f(j + zoneOffset.getTotalSeconds(), 86400L)), j.k((((int) a.d(totalSeconds, 86400L)) * 1000000000) + j2));
        }
        throw new NullPointerException("offset");
    }

    @Override // j$.time.temporal.k
    public final q a(j$.time.temporal.l lVar) {
        if (lVar instanceof j$.time.temporal.a) {
            if (((j$.time.temporal.a) lVar).isTimeBased()) {
                j jVar = this.b;
                jVar.getClass();
                return j$.time.temporal.j.c(jVar, lVar);
            }
            return this.a.a(lVar);
        }
        return lVar.b(this);
    }

    @Override // j$.time.temporal.k
    public final boolean b(j$.time.temporal.l lVar) {
        if (!(lVar instanceof j$.time.temporal.a)) {
            return lVar != null && lVar.a(this);
        }
        j$.time.temporal.a aVar = (j$.time.temporal.a) lVar;
        return aVar.isDateBased() || aVar.isTimeBased();
    }

    @Override // j$.time.temporal.k
    public final long c(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? ((j$.time.temporal.a) lVar).isTimeBased() ? this.b.c(lVar) : this.a.c(lVar) : lVar.d(this);
    }

    @Override // j$.time.temporal.k
    public final Object d(j$.time.temporal.n nVar) {
        if (nVar == j$.time.temporal.j.e()) {
            return this.a;
        }
        if (nVar == j$.time.temporal.j.j() || nVar == j$.time.temporal.j.i() || nVar == j$.time.temporal.j.g()) {
            return null;
        }
        if (nVar == j$.time.temporal.j.f()) {
            return this.b;
        }
        if (nVar != j$.time.temporal.j.d()) {
            return nVar == j$.time.temporal.j.h() ? j$.time.temporal.b.NANOS : nVar.a(this);
        }
        ((LocalDate) l()).getClass();
        return j$.time.chrono.e.a;
    }

    @Override // j$.time.temporal.k
    public final int e(j$.time.temporal.a aVar) {
        return aVar instanceof j$.time.temporal.a ? aVar.isTimeBased() ? this.b.e(aVar) : this.a.e(aVar) : j$.time.temporal.j.a(this, aVar);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof h) {
            h hVar = (h) obj;
            return this.a.equals(hVar.a) && this.b.equals(hVar.b);
        }
        return false;
    }

    @Override // java.lang.Comparable
    /* renamed from: f */
    public final int compareTo(j$.time.chrono.c cVar) {
        boolean z = cVar instanceof h;
        j jVar = this.b;
        LocalDate localDate = this.a;
        if (z) {
            h hVar = (h) cVar;
            int g = localDate.g(hVar.a);
            return g == 0 ? jVar.compareTo(hVar.b) : g;
        }
        h hVar2 = (h) cVar;
        int compareTo = localDate.compareTo(hVar2.a);
        if (compareTo == 0) {
            int compareTo2 = jVar.compareTo(hVar2.b);
            if (compareTo2 == 0) {
                ((LocalDate) l()).getClass();
                j$.time.chrono.e eVar = j$.time.chrono.e.a;
                ((LocalDate) hVar2.l()).getClass();
                eVar.getClass();
                eVar.getClass();
                return 0;
            }
            return compareTo2;
        }
        return compareTo;
    }

    public final int g() {
        return this.b.i();
    }

    public final int h() {
        return this.a.l();
    }

    public final int hashCode() {
        return this.a.hashCode() ^ this.b.hashCode();
    }

    public final long k(ZoneOffset zoneOffset) {
        if (zoneOffset != null) {
            return ((((LocalDate) l()).r() * 86400) + m().m()) - zoneOffset.getTotalSeconds();
        }
        throw new NullPointerException("offset");
    }

    public final j$.time.chrono.b l() {
        return this.a;
    }

    public final j m() {
        return this.b;
    }

    public final String toString() {
        return this.a.toString() + 'T' + this.b.toString();
    }
}
