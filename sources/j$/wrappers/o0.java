package j$.wrappers;

import java.util.function.LongUnaryOperator;
/* loaded from: classes2.dex */
public final /* synthetic */ class o0 implements j$.util.function.s {
    final /* synthetic */ LongUnaryOperator a;

    private /* synthetic */ o0(LongUnaryOperator longUnaryOperator) {
        this.a = longUnaryOperator;
    }

    public static /* synthetic */ j$.util.function.s c(LongUnaryOperator longUnaryOperator) {
        if (longUnaryOperator == null) {
            return null;
        }
        return longUnaryOperator instanceof p0 ? ((p0) longUnaryOperator).a : new o0(longUnaryOperator);
    }

    @Override // j$.util.function.s
    public /* synthetic */ j$.util.function.s a(j$.util.function.s sVar) {
        return c(this.a.andThen(p0.a(sVar)));
    }

    @Override // j$.util.function.s
    public /* synthetic */ long applyAsLong(long j) {
        return this.a.applyAsLong(j);
    }

    @Override // j$.util.function.s
    public /* synthetic */ j$.util.function.s b(j$.util.function.s sVar) {
        return c(this.a.compose(p0.a(sVar)));
    }
}
