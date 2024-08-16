package j$.util.function;

import java.util.function.ObjDoubleConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class g0 implements i0 {
    public final /* synthetic */ ObjDoubleConsumer a;

    private /* synthetic */ g0(ObjDoubleConsumer objDoubleConsumer) {
        this.a = objDoubleConsumer;
    }

    public static /* synthetic */ i0 a(ObjDoubleConsumer objDoubleConsumer) {
        if (objDoubleConsumer == null) {
            return null;
        }
        return objDoubleConsumer instanceof h0 ? ((h0) objDoubleConsumer).a : new g0(objDoubleConsumer);
    }

    @Override // j$.util.function.i0
    public final /* synthetic */ void accept(Object obj, double d) {
        this.a.accept(obj, d);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        ObjDoubleConsumer objDoubleConsumer = this.a;
        if (obj instanceof g0) {
            obj = ((g0) obj).a;
        }
        return objDoubleConsumer.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
