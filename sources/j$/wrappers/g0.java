package j$.wrappers;

import j$.util.function.LongFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class g0 implements LongFunction {
    final /* synthetic */ java.util.function.LongFunction a;

    private /* synthetic */ g0(java.util.function.LongFunction longFunction) {
        this.a = longFunction;
    }

    public static /* synthetic */ LongFunction a(java.util.function.LongFunction longFunction) {
        if (longFunction == null) {
            return null;
        }
        return longFunction instanceof h0 ? ((h0) longFunction).a : new g0(longFunction);
    }

    @Override // j$.util.function.LongFunction
    public /* synthetic */ Object apply(long j) {
        return this.a.apply(j);
    }
}
