package j$.time;

import j$.time.temporal.q;

/* loaded from: classes2.dex */
final class m implements j$.time.temporal.k {
    final /* synthetic */ ZoneId a;

    m(ZoneId zoneId) {
        this.a = zoneId;
    }

    @Override // j$.time.temporal.k
    public final /* synthetic */ q a(j$.time.temporal.l lVar) {
        return j$.time.temporal.j.c(this, lVar);
    }

    @Override // j$.time.temporal.k
    public final long b(j$.time.temporal.l lVar) {
        throw new j$.time.temporal.p("Unsupported field: " + lVar);
    }

    @Override // j$.time.temporal.k
    public final Object c(j$.time.temporal.n nVar) {
        return nVar == j$.time.temporal.j.j() ? this.a : j$.time.temporal.j.b(this, nVar);
    }

    @Override // j$.time.temporal.k
    public final /* synthetic */ int d(j$.time.temporal.a aVar) {
        return j$.time.temporal.j.a(this, aVar);
    }

    @Override // j$.time.temporal.k
    public final boolean e(j$.time.temporal.l lVar) {
        return false;
    }
}
