package j$.util.function;

import java.util.function.LongToDoubleFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class o0 implements LongToDoubleFunction {
    public final /* synthetic */ p0 a;

    private /* synthetic */ o0(p0 p0Var) {
        this.a = p0Var;
    }

    public static /* synthetic */ LongToDoubleFunction a(p0 p0Var) {
        if (p0Var == null) {
            return null;
        }
        return p0Var instanceof n0 ? ((n0) p0Var).a : new o0(p0Var);
    }

    @Override // java.util.function.LongToDoubleFunction
    public final /* synthetic */ double applyAsDouble(long j) {
        return ((n0) this.a).a(j);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        p0 p0Var = this.a;
        if (obj instanceof o0) {
            obj = ((o0) obj).a;
        }
        return p0Var.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
