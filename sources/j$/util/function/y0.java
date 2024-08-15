package j$.util.function;

import java.util.function.ObjDoubleConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class y0 implements ObjDoubleConsumer {
    public final /* synthetic */ z0 a;

    private /* synthetic */ y0(z0 z0Var) {
        this.a = z0Var;
    }

    public static /* synthetic */ ObjDoubleConsumer a(z0 z0Var) {
        if (z0Var == null) {
            return null;
        }
        return z0Var instanceof x0 ? ((x0) z0Var).a : new y0(z0Var);
    }

    @Override // java.util.function.ObjDoubleConsumer
    public final /* synthetic */ void accept(Object obj, double d) {
        this.a.accept(obj, d);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        z0 z0Var = this.a;
        if (obj instanceof y0) {
            obj = ((y0) obj).a;
        }
        return z0Var.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
