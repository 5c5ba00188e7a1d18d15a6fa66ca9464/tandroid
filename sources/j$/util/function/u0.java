package j$.util.function;

import java.util.function.LongUnaryOperator;
/* loaded from: classes2.dex */
public final /* synthetic */ class u0 implements w0 {
    public final /* synthetic */ LongUnaryOperator a;

    private /* synthetic */ u0(LongUnaryOperator longUnaryOperator) {
        this.a = longUnaryOperator;
    }

    public static /* synthetic */ w0 c(LongUnaryOperator longUnaryOperator) {
        if (longUnaryOperator == null) {
            return null;
        }
        return longUnaryOperator instanceof v0 ? ((v0) longUnaryOperator).a : new u0(longUnaryOperator);
    }

    @Override // j$.util.function.w0
    public final /* synthetic */ w0 a(w0 w0Var) {
        return c(this.a.compose(v0.a(w0Var)));
    }

    @Override // j$.util.function.w0
    public final /* synthetic */ long applyAsLong(long j) {
        return this.a.applyAsLong(j);
    }

    @Override // j$.util.function.w0
    public final /* synthetic */ w0 b(w0 w0Var) {
        return c(this.a.andThen(v0.a(w0Var)));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof u0) {
            obj = ((u0) obj).a;
        }
        return this.a.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
