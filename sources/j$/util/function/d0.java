package j$.util.function;

import java.util.function.LongUnaryOperator;

/* loaded from: classes2.dex */
public final /* synthetic */ class d0 implements f0 {
    public final /* synthetic */ LongUnaryOperator a;

    private /* synthetic */ d0(LongUnaryOperator longUnaryOperator) {
        this.a = longUnaryOperator;
    }

    public static /* synthetic */ f0 b(LongUnaryOperator longUnaryOperator) {
        if (longUnaryOperator == null) {
            return null;
        }
        return longUnaryOperator instanceof e0 ? ((e0) longUnaryOperator).a : new d0(longUnaryOperator);
    }

    @Override // j$.util.function.f0
    public final /* synthetic */ f0 a(f0 f0Var) {
        return b(this.a.andThen(e0.a(f0Var)));
    }

    @Override // j$.util.function.f0
    public final /* synthetic */ long applyAsLong(long j) {
        return this.a.applyAsLong(j);
    }

    @Override // j$.util.function.f0
    public final /* synthetic */ f0 c(f0 f0Var) {
        return b(this.a.compose(e0.a(f0Var)));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        LongUnaryOperator longUnaryOperator = this.a;
        if (obj instanceof d0) {
            obj = ((d0) obj).a;
        }
        return longUnaryOperator.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
