package j$.util.function;

import java.util.function.LongToIntFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class r0 implements LongToIntFunction {
    public final /* synthetic */ s0 a;

    private /* synthetic */ r0(s0 s0Var) {
        this.a = s0Var;
    }

    public static /* synthetic */ LongToIntFunction a(s0 s0Var) {
        if (s0Var == null) {
            return null;
        }
        return s0Var instanceof q0 ? ((q0) s0Var).a : new r0(s0Var);
    }

    @Override // java.util.function.LongToIntFunction
    public final /* synthetic */ int applyAsInt(long j) {
        return ((q0) this.a).a(j);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        s0 s0Var = this.a;
        if (obj instanceof r0) {
            obj = ((r0) obj).a;
        }
        return s0Var.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
