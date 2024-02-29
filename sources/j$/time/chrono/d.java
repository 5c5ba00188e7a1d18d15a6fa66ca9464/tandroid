package j$.time.chrono;

import j$.time.o;
import j$.time.temporal.j;
import j$.time.temporal.l;
import j$.time.temporal.w;
/* loaded from: classes2.dex */
public abstract /* synthetic */ class d {
    public static int a(f fVar, l lVar) {
        if (lVar instanceof j$.time.temporal.a) {
            int i = e.a[((j$.time.temporal.a) lVar).ordinal()];
            if (i != 1) {
                o oVar = (o) fVar;
                return i != 2 ? ((j$.time.g) oVar.l()).a(lVar) : oVar.g().getTotalSeconds();
            }
            throw new w("Invalid field 'InstantSeconds' for get() method, use getLong() instead");
        }
        return j.a(fVar, lVar);
    }
}
