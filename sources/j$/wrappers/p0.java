package j$.wrappers;

import java.util.function.LongUnaryOperator;
/* loaded from: classes2.dex */
public final /* synthetic */ class p0 implements LongUnaryOperator {
    final /* synthetic */ j$.util.function.s a;

    private /* synthetic */ p0(j$.util.function.s sVar) {
        this.a = sVar;
    }

    public static /* synthetic */ LongUnaryOperator a(j$.util.function.s sVar) {
        if (sVar == null) {
            return null;
        }
        return sVar instanceof o0 ? ((o0) sVar).a : new p0(sVar);
    }

    @Override // java.util.function.LongUnaryOperator
    public /* synthetic */ LongUnaryOperator andThen(LongUnaryOperator longUnaryOperator) {
        return a(this.a.a(o0.c(longUnaryOperator)));
    }

    @Override // java.util.function.LongUnaryOperator
    public /* synthetic */ long applyAsLong(long j) {
        return this.a.applyAsLong(j);
    }

    @Override // java.util.function.LongUnaryOperator
    public /* synthetic */ LongUnaryOperator compose(LongUnaryOperator longUnaryOperator) {
        return a(this.a.b(o0.c(longUnaryOperator)));
    }
}
