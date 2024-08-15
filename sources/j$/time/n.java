package j$.time;

import j$.time.temporal.p;
import j$.time.temporal.q;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class n implements j$.time.temporal.k {
    final /* synthetic */ ZoneId a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public n(ZoneId zoneId) {
        this.a = zoneId;
    }

    @Override // j$.time.temporal.k
    public final /* synthetic */ q a(j$.time.temporal.l lVar) {
        return j$.time.temporal.j.c(this, lVar);
    }

    @Override // j$.time.temporal.k
    public final boolean b(j$.time.temporal.l lVar) {
        return false;
    }

    @Override // j$.time.temporal.k
    public final long c(j$.time.temporal.l lVar) {
        throw new p("Unsupported field: " + lVar);
    }

    @Override // j$.time.temporal.k
    public final Object d(j$.time.temporal.n nVar) {
        return nVar == j$.time.temporal.j.j() ? this.a : j$.time.temporal.j.b(this, nVar);
    }

    @Override // j$.time.temporal.k
    public final /* synthetic */ int e(j$.time.temporal.a aVar) {
        return j$.time.temporal.j.a(this, aVar);
    }
}
