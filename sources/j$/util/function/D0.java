package j$.util.function;

import java.util.function.ObjLongConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class D0 implements F0 {
    public final /* synthetic */ ObjLongConsumer a;

    private /* synthetic */ D0(ObjLongConsumer objLongConsumer) {
        this.a = objLongConsumer;
    }

    public static /* synthetic */ F0 a(ObjLongConsumer objLongConsumer) {
        if (objLongConsumer == null) {
            return null;
        }
        return objLongConsumer instanceof E0 ? ((E0) objLongConsumer).a : new D0(objLongConsumer);
    }

    @Override // j$.util.function.F0
    public final /* synthetic */ void accept(Object obj, long j) {
        this.a.accept(obj, j);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof D0) {
            obj = ((D0) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
