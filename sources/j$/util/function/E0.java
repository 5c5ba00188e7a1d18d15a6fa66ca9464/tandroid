package j$.util.function;

import java.util.function.ObjLongConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class E0 implements ObjLongConsumer {
    public final /* synthetic */ F0 a;

    private /* synthetic */ E0(F0 f0) {
        this.a = f0;
    }

    public static /* synthetic */ ObjLongConsumer a(F0 f0) {
        if (f0 == null) {
            return null;
        }
        return f0 instanceof D0 ? ((D0) f0).a : new E0(f0);
    }

    @Override // java.util.function.ObjLongConsumer
    public final /* synthetic */ void accept(Object obj, long j) {
        this.a.accept(obj, j);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        F0 f0 = this.a;
        if (obj instanceof E0) {
            obj = ((E0) obj).a;
        }
        return f0.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
