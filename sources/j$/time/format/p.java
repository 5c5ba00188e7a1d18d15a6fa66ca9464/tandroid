package j$.time.format;

import j$.time.ZoneId;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class p implements h {
    private final j$.time.temporal.n a;
    private final String b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public p(j$.time.temporal.n nVar, String str) {
        this.a = nVar;
        this.b = str;
    }

    @Override // j$.time.format.h
    public boolean a(t tVar, StringBuilder sb) {
        ZoneId zoneId = (ZoneId) tVar.f(this.a);
        if (zoneId == null) {
            return false;
        }
        sb.append(zoneId.getId());
        return true;
    }

    public final String toString() {
        return this.b;
    }
}
