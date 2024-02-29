package j$.time.temporal;

import j$.time.ZoneId;
/* loaded from: classes2.dex */
public final /* synthetic */ class q implements u {
    public static final /* synthetic */ q a = new q();

    private /* synthetic */ q() {
    }

    @Override // j$.time.temporal.u
    public final Object a(k kVar) {
        int i = t.a;
        ZoneId zoneId = (ZoneId) kVar.d(m.a);
        return zoneId != null ? zoneId : (ZoneId) kVar.d(p.a);
    }
}
