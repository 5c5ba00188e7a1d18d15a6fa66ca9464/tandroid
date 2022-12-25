package j$.wrappers;

import java.util.function.ObjLongConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class v0 implements ObjLongConsumer {
    final /* synthetic */ j$.util.function.w a;

    private /* synthetic */ v0(j$.util.function.w wVar) {
        this.a = wVar;
    }

    public static /* synthetic */ ObjLongConsumer a(j$.util.function.w wVar) {
        if (wVar == null) {
            return null;
        }
        return wVar instanceof u0 ? ((u0) wVar).a : new v0(wVar);
    }

    @Override // java.util.function.ObjLongConsumer
    public /* synthetic */ void accept(Object obj, long j) {
        this.a.accept(obj, j);
    }
}
