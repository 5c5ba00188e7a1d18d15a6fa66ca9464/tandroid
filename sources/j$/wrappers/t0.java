package j$.wrappers;

import java.util.function.ObjIntConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class t0 implements ObjIntConsumer {
    final /* synthetic */ j$.util.function.u a;

    private /* synthetic */ t0(j$.util.function.u uVar) {
        this.a = uVar;
    }

    public static /* synthetic */ ObjIntConsumer a(j$.util.function.u uVar) {
        if (uVar == null) {
            return null;
        }
        return uVar instanceof s0 ? ((s0) uVar).a : new t0(uVar);
    }

    @Override // java.util.function.ObjIntConsumer
    public /* synthetic */ void accept(Object obj, int i) {
        this.a.accept(obj, i);
    }
}
