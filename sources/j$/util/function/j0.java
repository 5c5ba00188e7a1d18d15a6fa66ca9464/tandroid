package j$.util.function;

import java.util.function.ObjIntConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class j0 implements l0 {
    public final /* synthetic */ ObjIntConsumer a;

    private /* synthetic */ j0(ObjIntConsumer objIntConsumer) {
        this.a = objIntConsumer;
    }

    public static /* synthetic */ l0 a(ObjIntConsumer objIntConsumer) {
        if (objIntConsumer == null) {
            return null;
        }
        return objIntConsumer instanceof k0 ? ((k0) objIntConsumer).a : new j0(objIntConsumer);
    }

    @Override // j$.util.function.l0
    public final /* synthetic */ void accept(Object obj, int i) {
        this.a.accept(obj, i);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        ObjIntConsumer objIntConsumer = this.a;
        if (obj instanceof j0) {
            obj = ((j0) obj).a;
        }
        return objIntConsumer.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
