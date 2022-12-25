package j$.wrappers;

import java.util.function.ObjIntConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class s0 implements j$.util.function.v {
    final /* synthetic */ ObjIntConsumer a;

    private /* synthetic */ s0(ObjIntConsumer objIntConsumer) {
        this.a = objIntConsumer;
    }

    public static /* synthetic */ j$.util.function.v a(ObjIntConsumer objIntConsumer) {
        if (objIntConsumer == null) {
            return null;
        }
        return objIntConsumer instanceof t0 ? ((t0) objIntConsumer).a : new s0(objIntConsumer);
    }

    @Override // j$.util.function.v
    public /* synthetic */ void accept(Object obj, int i) {
        this.a.accept(obj, i);
    }
}
