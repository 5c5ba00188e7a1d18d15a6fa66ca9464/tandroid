package j$.wrappers;

import java.util.function.ObjDoubleConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class r0 implements ObjDoubleConsumer {
    final /* synthetic */ j$.util.function.t a;

    private /* synthetic */ r0(j$.util.function.t tVar) {
        this.a = tVar;
    }

    public static /* synthetic */ ObjDoubleConsumer a(j$.util.function.t tVar) {
        if (tVar == null) {
            return null;
        }
        return tVar instanceof q0 ? ((q0) tVar).a : new r0(tVar);
    }

    @Override // java.util.function.ObjDoubleConsumer
    public /* synthetic */ void accept(Object obj, double d) {
        this.a.accept(obj, d);
    }
}
