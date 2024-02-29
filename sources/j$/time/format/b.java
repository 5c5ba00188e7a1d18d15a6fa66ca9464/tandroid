package j$.time.format;

import j$.time.ZoneId;
import j$.time.ZoneOffset;
/* loaded from: classes2.dex */
public final /* synthetic */ class b implements j$.time.temporal.u {
    public static final /* synthetic */ b a = new b();

    private /* synthetic */ b() {
    }

    @Override // j$.time.temporal.u
    public final Object a(j$.time.temporal.k kVar) {
        int i = r.f;
        int i2 = j$.time.temporal.t.a;
        ZoneId zoneId = (ZoneId) kVar.d(j$.time.temporal.m.a);
        if (zoneId == null || (zoneId instanceof ZoneOffset)) {
            return null;
        }
        return zoneId;
    }
}
