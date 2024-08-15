package j$.util.function;

import java.util.function.ObjDoubleConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class x0 implements z0 {
    public final /* synthetic */ ObjDoubleConsumer a;

    private /* synthetic */ x0(ObjDoubleConsumer objDoubleConsumer) {
        this.a = objDoubleConsumer;
    }

    public static /* synthetic */ z0 a(ObjDoubleConsumer objDoubleConsumer) {
        if (objDoubleConsumer == null) {
            return null;
        }
        return objDoubleConsumer instanceof y0 ? ((y0) objDoubleConsumer).a : new x0(objDoubleConsumer);
    }

    @Override // j$.util.function.z0
    public final /* synthetic */ void accept(Object obj, double d) {
        this.a.accept(obj, d);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof x0) {
            obj = ((x0) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
