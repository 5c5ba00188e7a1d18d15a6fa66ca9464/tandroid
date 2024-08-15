package j$.util.function;

import java.util.function.LongUnaryOperator;
/* loaded from: classes2.dex */
public final /* synthetic */ class v0 implements LongUnaryOperator {
    public final /* synthetic */ w0 a;

    private /* synthetic */ v0(w0 w0Var) {
        this.a = w0Var;
    }

    public static /* synthetic */ LongUnaryOperator a(w0 w0Var) {
        if (w0Var == null) {
            return null;
        }
        return w0Var instanceof u0 ? ((u0) w0Var).a : new v0(w0Var);
    }

    @Override // java.util.function.LongUnaryOperator
    public final /* synthetic */ LongUnaryOperator andThen(LongUnaryOperator longUnaryOperator) {
        return a(this.a.b(u0.c(longUnaryOperator)));
    }

    @Override // java.util.function.LongUnaryOperator
    public final /* synthetic */ long applyAsLong(long j) {
        return this.a.applyAsLong(j);
    }

    @Override // java.util.function.LongUnaryOperator
    public final /* synthetic */ LongUnaryOperator compose(LongUnaryOperator longUnaryOperator) {
        return a(this.a.a(u0.c(longUnaryOperator)));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        w0 w0Var = this.a;
        if (obj instanceof v0) {
            obj = ((v0) obj).a;
        }
        return w0Var.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
