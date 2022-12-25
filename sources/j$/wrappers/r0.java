package j$.wrappers;

import java.util.function.ObjDoubleConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class r0 implements ObjDoubleConsumer {
    final /* synthetic */ j$.util.function.u a;

    private /* synthetic */ r0(j$.util.function.u uVar) {
        this.a = uVar;
    }

    public static /* synthetic */ ObjDoubleConsumer a(j$.util.function.u uVar) {
        if (uVar == null) {
            return null;
        }
        return uVar instanceof q0 ? ((q0) uVar).a : new r0(uVar);
    }

    @Override // java.util.function.ObjDoubleConsumer
    public /* synthetic */ void accept(Object obj, double d) {
        this.a.accept(obj, d);
    }
}
