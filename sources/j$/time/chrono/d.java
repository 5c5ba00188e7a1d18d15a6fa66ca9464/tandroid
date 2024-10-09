package j$.time.chrono;

import j$.time.p;
import j$.time.temporal.j;

/* loaded from: classes2.dex */
public abstract /* synthetic */ class d {
    public static int a(p pVar, j$.time.temporal.a aVar) {
        if (!(aVar instanceof j$.time.temporal.a)) {
            return j.a(pVar, aVar);
        }
        int i = e.a[aVar.ordinal()];
        if (i != 1) {
            return i != 2 ? pVar.i().d(aVar) : pVar.f().getTotalSeconds();
        }
        throw new j$.time.temporal.p("Invalid field 'InstantSeconds' for get() method, use getLong() instead");
    }
}
