package j$.time.temporal;

import j$.time.ZoneOffset;
/* loaded from: classes2.dex */
public final /* synthetic */ class p implements u {
    public static final /* synthetic */ p a = new p();

    private /* synthetic */ p() {
    }

    @Override // j$.time.temporal.u
    public final Object a(k kVar) {
        int i = t.a;
        a aVar = a.OFFSET_SECONDS;
        if (kVar.e(aVar)) {
            return ZoneOffset.j(kVar.a(aVar));
        }
        return null;
    }
}
