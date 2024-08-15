package j$.util.function;

import java.util.function.ObjIntConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class B0 implements ObjIntConsumer {
    public final /* synthetic */ C0 a;

    private /* synthetic */ B0(C0 c0) {
        this.a = c0;
    }

    public static /* synthetic */ ObjIntConsumer a(C0 c0) {
        if (c0 == null) {
            return null;
        }
        return c0 instanceof A0 ? ((A0) c0).a : new B0(c0);
    }

    @Override // java.util.function.ObjIntConsumer
    public final /* synthetic */ void accept(Object obj, int i) {
        this.a.accept(obj, i);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        C0 c0 = this.a;
        if (obj instanceof B0) {
            obj = ((B0) obj).a;
        }
        return c0.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
