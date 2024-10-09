package j$.util.function;

import java.util.function.LongUnaryOperator;

/* loaded from: classes2.dex */
public final /* synthetic */ class e0 implements LongUnaryOperator {
    public final /* synthetic */ f0 a;

    private /* synthetic */ e0(f0 f0Var) {
        this.a = f0Var;
    }

    public static /* synthetic */ LongUnaryOperator a(f0 f0Var) {
        if (f0Var == null) {
            return null;
        }
        return f0Var instanceof d0 ? ((d0) f0Var).a : new e0(f0Var);
    }

    @Override // java.util.function.LongUnaryOperator
    public final /* synthetic */ LongUnaryOperator andThen(LongUnaryOperator longUnaryOperator) {
        return a(this.a.a(d0.b(longUnaryOperator)));
    }

    @Override // java.util.function.LongUnaryOperator
    public final /* synthetic */ long applyAsLong(long j) {
        return this.a.applyAsLong(j);
    }

    @Override // java.util.function.LongUnaryOperator
    public final /* synthetic */ LongUnaryOperator compose(LongUnaryOperator longUnaryOperator) {
        return a(this.a.c(d0.b(longUnaryOperator)));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        f0 f0Var = this.a;
        if (obj instanceof e0) {
            obj = ((e0) obj).a;
        }
        return f0Var.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
