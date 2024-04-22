package j$.wrappers;

import java.util.function.ObjLongConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class v0 implements ObjLongConsumer {
    final /* synthetic */ j$.util.function.v a;

    private /* synthetic */ v0(j$.util.function.v vVar) {
        this.a = vVar;
    }

    public static /* synthetic */ ObjLongConsumer a(j$.util.function.v vVar) {
        if (vVar == null) {
            return null;
        }
        return vVar instanceof u0 ? ((u0) vVar).a : new v0(vVar);
    }

    @Override // java.util.function.ObjLongConsumer
    public /* synthetic */ void accept(Object obj, long j) {
        this.a.accept(obj, j);
    }
}
