package j$.time.format;

import j$.time.LocalDate;
import j$.time.ZoneId;
/* loaded from: classes2.dex */
final class r implements j$.time.temporal.k {
    final /* synthetic */ j$.time.chrono.b a;
    final /* synthetic */ j$.time.temporal.k b;
    final /* synthetic */ j$.time.chrono.f c;
    final /* synthetic */ ZoneId d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public r(LocalDate localDate, j$.time.temporal.k kVar, j$.time.chrono.f fVar, ZoneId zoneId) {
        this.a = localDate;
        this.b = kVar;
        this.c = fVar;
        this.d = zoneId;
    }

    @Override // j$.time.temporal.k
    public final j$.time.temporal.q a(j$.time.temporal.l lVar) {
        j$.time.chrono.b bVar = this.a;
        return (bVar == null || !lVar.e()) ? this.b.a(lVar) : ((LocalDate) bVar).a(lVar);
    }

    @Override // j$.time.temporal.k
    public final long b(j$.time.temporal.l lVar) {
        j$.time.chrono.b bVar = this.a;
        return (bVar == null || !lVar.e()) ? this.b.b(lVar) : ((LocalDate) bVar).b(lVar);
    }

    @Override // j$.time.temporal.k
    public final Object c(j$.time.temporal.n nVar) {
        return nVar == j$.time.temporal.j.d() ? this.c : nVar == j$.time.temporal.j.j() ? this.d : nVar == j$.time.temporal.j.h() ? this.b.c(nVar) : nVar.a(this);
    }

    @Override // j$.time.temporal.k
    public final /* synthetic */ int d(j$.time.temporal.a aVar) {
        return j$.time.temporal.j.a(this, aVar);
    }

    @Override // j$.time.temporal.k
    public final boolean e(j$.time.temporal.l lVar) {
        j$.time.chrono.b bVar = this.a;
        return (bVar == null || !lVar.e()) ? this.b.e(lVar) : ((LocalDate) bVar).e(lVar);
    }
}
