package j$.time.temporal;

import j$.time.LocalDate;
/* loaded from: classes2.dex */
public final /* synthetic */ class r implements u {
    public static final /* synthetic */ r a = new r();

    private /* synthetic */ r() {
    }

    @Override // j$.time.temporal.u
    public final Object a(k kVar) {
        int i = t.a;
        a aVar = a.EPOCH_DAY;
        if (kVar.e(aVar)) {
            return LocalDate.p(kVar.c(aVar));
        }
        return null;
    }
}
