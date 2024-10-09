package j$.time;

import j$.time.temporal.q;
import java.io.Serializable;

/* loaded from: classes2.dex */
public final class g implements j$.time.temporal.k, j$.time.chrono.c, Serializable {
    private final LocalDate a;
    private final i b;

    static {
        LocalDate localDate = LocalDate.d;
        i iVar = i.e;
        j$.util.a.B(localDate, "date");
        j$.util.a.B(iVar, "time");
        LocalDate localDate2 = LocalDate.e;
        i iVar2 = i.f;
        j$.util.a.B(localDate2, "date");
        j$.util.a.B(iVar2, "time");
    }

    private g(LocalDate localDate, i iVar) {
        this.a = localDate;
        this.b = iVar;
    }

    public static g i(int i) {
        return new g(LocalDate.of(i, 12, 31), i.j());
    }

    public static g j(long j, int i, ZoneOffset zoneOffset) {
        j$.util.a.B(zoneOffset, "offset");
        long j2 = i;
        j$.time.temporal.a.NANO_OF_SECOND.g(j2);
        return new g(LocalDate.p(j$.com.android.tools.r8.a.j(j + zoneOffset.getTotalSeconds(), 86400L)), i.k((((int) j$.com.android.tools.r8.a.i(r5, 86400L)) * 1000000000) + j2));
    }

    @Override // j$.time.temporal.k
    public final q a(j$.time.temporal.l lVar) {
        if (!(lVar instanceof j$.time.temporal.a)) {
            return lVar.d(this);
        }
        if (!((j$.time.temporal.a) lVar).h()) {
            return this.a.a(lVar);
        }
        i iVar = this.b;
        iVar.getClass();
        return j$.time.temporal.j.c(iVar, lVar);
    }

    @Override // j$.time.temporal.k
    public final long b(j$.time.temporal.l lVar) {
        return lVar instanceof j$.time.temporal.a ? ((j$.time.temporal.a) lVar).h() ? this.b.b(lVar) : this.a.b(lVar) : lVar.b(this);
    }

    @Override // j$.time.temporal.k
    public final Object c(j$.time.temporal.n nVar) {
        j$.time.temporal.m e = j$.time.temporal.j.e();
        LocalDate localDate = this.a;
        if (nVar == e) {
            return localDate;
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
        localDate.getClass();
        return j$.time.chrono.g.a;
    }

    @Override // j$.time.temporal.k
    public final int d(j$.time.temporal.a aVar) {
        return aVar instanceof j$.time.temporal.a ? aVar.h() ? this.b.d(aVar) : this.a.d(aVar) : j$.time.temporal.j.a(this, aVar);
    }

    @Override // j$.time.temporal.k
    public final boolean e(j$.time.temporal.l lVar) {
        if (!(lVar instanceof j$.time.temporal.a)) {
            return lVar != null && lVar.c(this);
        }
        j$.time.temporal.a aVar = (j$.time.temporal.a) lVar;
        return aVar.e() || aVar.h();
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof g)) {
            return false;
        }
        g gVar = (g) obj;
        return this.a.equals(gVar.a) && this.b.equals(gVar.b);
    }

    @Override // java.lang.Comparable
    /* renamed from: f, reason: merged with bridge method [inline-methods] */
    public final int compareTo(j$.time.chrono.c cVar) {
        boolean z = cVar instanceof g;
        i iVar = this.b;
        LocalDate localDate = this.a;
        if (z) {
            g gVar = (g) cVar;
            int g = localDate.g(gVar.a);
            return g == 0 ? iVar.compareTo(gVar.b) : g;
        }
        g gVar2 = (g) cVar;
        int compareTo = localDate.compareTo(gVar2.a);
        if (compareTo != 0) {
            return compareTo;
        }
        int compareTo2 = iVar.compareTo(gVar2.b);
        if (compareTo2 != 0) {
            return compareTo2;
        }
        localDate.getClass();
        j$.time.chrono.g gVar3 = j$.time.chrono.g.a;
        gVar2.a.getClass();
        gVar3.getClass();
        gVar3.getClass();
        return 0;
    }

    public final int g() {
        return this.b.i();
    }

    public final int h() {
        return this.a.m();
    }

    public final int hashCode() {
        return this.a.hashCode() ^ this.b.hashCode();
    }

    public final long k(ZoneOffset zoneOffset) {
        j$.util.a.B(zoneOffset, "offset");
        return ((this.a.s() * 86400) + this.b.m()) - zoneOffset.getTotalSeconds();
    }

    public final LocalDate l() {
        return this.a;
    }

    public final i m() {
        return this.b;
    }

    public final String toString() {
        return this.a.toString() + 'T' + this.b.toString();
    }
}
