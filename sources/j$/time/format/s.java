package j$.time.format;

import j$.time.ZoneId;
/* loaded from: classes2.dex */
class s implements j$.time.temporal.k {
    final /* synthetic */ j$.time.chrono.b a;
    final /* synthetic */ j$.time.temporal.k b;
    final /* synthetic */ j$.time.chrono.g c;
    final /* synthetic */ ZoneId d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public s(j$.time.chrono.b bVar, j$.time.temporal.k kVar, j$.time.chrono.g gVar, ZoneId zoneId) {
        this.a = bVar;
        this.b = kVar;
        this.c = gVar;
        this.d = zoneId;
    }

    @Override // j$.time.temporal.k
    public /* synthetic */ int a(j$.time.temporal.l lVar) {
        return j$.time.temporal.j.a(this, lVar);
    }

    @Override // j$.time.temporal.k
    public j$.time.temporal.x b(j$.time.temporal.l lVar) {
        return (this.a == null || !lVar.f()) ? this.b.b(lVar) : ((j$.time.f) this.a).b(lVar);
    }

    @Override // j$.time.temporal.k
    public long c(j$.time.temporal.l lVar) {
        return (this.a == null || !lVar.f()) ? this.b.c(lVar) : ((j$.time.f) this.a).c(lVar);
    }

    @Override // j$.time.temporal.k
    public Object d(j$.time.temporal.u uVar) {
        int i = j$.time.temporal.t.a;
        return uVar == j$.time.temporal.n.a ? this.c : uVar == j$.time.temporal.m.a ? this.d : uVar == j$.time.temporal.o.a ? this.b.d(uVar) : uVar.a(this);
    }

    @Override // j$.time.temporal.k
    public boolean e(j$.time.temporal.l lVar) {
        return (this.a == null || !lVar.f()) ? this.b.e(lVar) : ((j$.time.f) this.a).e(lVar);
    }
}
