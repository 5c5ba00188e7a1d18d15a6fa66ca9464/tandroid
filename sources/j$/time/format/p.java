package j$.time.format;

import j$.time.ZoneId;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class p implements h {
    private final j$.time.temporal.u a;
    private final String b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public p(j$.time.temporal.u uVar, String str) {
        this.a = uVar;
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

    public String toString() {
        return this.b;
    }
}
