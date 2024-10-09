package j$.util.function;

import java.util.function.ObjLongConsumer;

/* loaded from: classes2.dex */
public final /* synthetic */ class n0 implements ObjLongConsumer {
    public final /* synthetic */ o0 a;

    private /* synthetic */ n0(o0 o0Var) {
        this.a = o0Var;
    }

    public static /* synthetic */ ObjLongConsumer a(o0 o0Var) {
        if (o0Var == null) {
            return null;
        }
        return o0Var instanceof m0 ? ((m0) o0Var).a : new n0(o0Var);
    }

    @Override // java.util.function.ObjLongConsumer
    public final /* synthetic */ void accept(Object obj, long j) {
        this.a.accept(obj, j);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        o0 o0Var = this.a;
        if (obj instanceof n0) {
            obj = ((n0) obj).a;
        }
        return o0Var.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
