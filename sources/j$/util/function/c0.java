package j$.util.function;

import java.util.function.LongBinaryOperator;
/* loaded from: classes2.dex */
public final /* synthetic */ class c0 implements LongBinaryOperator {
    public final /* synthetic */ d0 a;

    private /* synthetic */ c0(d0 d0Var) {
        this.a = d0Var;
    }

    public static /* synthetic */ LongBinaryOperator a(d0 d0Var) {
        if (d0Var == null) {
            return null;
        }
        return d0Var instanceof b0 ? ((b0) d0Var).a : new c0(d0Var);
    }

    @Override // java.util.function.LongBinaryOperator
    public final /* synthetic */ long applyAsLong(long j, long j2) {
        return this.a.applyAsLong(j, j2);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        d0 d0Var = this.a;
        if (obj instanceof c0) {
            obj = ((c0) obj).a;
        }
        return d0Var.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
