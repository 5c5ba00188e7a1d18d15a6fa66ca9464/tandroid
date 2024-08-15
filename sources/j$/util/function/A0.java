package j$.util.function;

import java.util.function.ObjIntConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class A0 implements C0 {
    public final /* synthetic */ ObjIntConsumer a;

    private /* synthetic */ A0(ObjIntConsumer objIntConsumer) {
        this.a = objIntConsumer;
    }

    public static /* synthetic */ C0 a(ObjIntConsumer objIntConsumer) {
        if (objIntConsumer == null) {
            return null;
        }
        return objIntConsumer instanceof B0 ? ((B0) objIntConsumer).a : new A0(objIntConsumer);
    }

    @Override // j$.util.function.C0
    public final /* synthetic */ void accept(Object obj, int i) {
        this.a.accept(obj, i);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof A0) {
            obj = ((A0) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
