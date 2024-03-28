package j$.time;

import j$.time.temporal.t;
import j$.time.temporal.u;
import j$.time.temporal.w;
import j$.time.temporal.x;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class j implements j$.time.temporal.k {
    final /* synthetic */ ZoneId a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public j(ZoneId zoneId) {
        this.a = zoneId;
    }

    @Override // j$.time.temporal.k
    public /* synthetic */ int a(j$.time.temporal.l lVar) {
        return j$.time.temporal.j.a(this, lVar);
    }

    @Override // j$.time.temporal.k
    public /* synthetic */ x b(j$.time.temporal.l lVar) {
        return j$.time.temporal.j.c(this, lVar);
    }

    @Override // j$.time.temporal.k
    public long c(j$.time.temporal.l lVar) {
        throw new w("Unsupported field: " + lVar);
    }

    @Override // j$.time.temporal.k
    public Object d(u uVar) {
        int i = t.a;
        return uVar == j$.time.temporal.m.a ? this.a : j$.time.temporal.j.b(this, uVar);
    }

    @Override // j$.time.temporal.k
    public boolean e(j$.time.temporal.l lVar) {
        return false;
    }
}
