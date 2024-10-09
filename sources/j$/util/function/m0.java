package j$.util.function;

import java.util.function.ObjLongConsumer;

/* loaded from: classes2.dex */
public final /* synthetic */ class m0 implements o0 {
    public final /* synthetic */ ObjLongConsumer a;

    private /* synthetic */ m0(ObjLongConsumer objLongConsumer) {
        this.a = objLongConsumer;
    }

    public static /* synthetic */ o0 a(ObjLongConsumer objLongConsumer) {
        if (objLongConsumer == null) {
            return null;
        }
        return objLongConsumer instanceof n0 ? ((n0) objLongConsumer).a : new m0(objLongConsumer);
    }

    @Override // j$.util.function.o0
    public final /* synthetic */ void accept(Object obj, long j) {
        this.a.accept(obj, j);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        ObjLongConsumer objLongConsumer = this.a;
        if (obj instanceof m0) {
            obj = ((m0) obj).a;
        }
        return objLongConsumer.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
