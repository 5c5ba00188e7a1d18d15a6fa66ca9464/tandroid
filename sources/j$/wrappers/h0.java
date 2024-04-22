package j$.wrappers;

import java.util.function.LongFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class h0 implements LongFunction {
    final /* synthetic */ j$.util.function.LongFunction a;

    private /* synthetic */ h0(j$.util.function.LongFunction longFunction) {
        this.a = longFunction;
    }

    public static /* synthetic */ LongFunction a(j$.util.function.LongFunction longFunction) {
        if (longFunction == null) {
            return null;
        }
        return longFunction instanceof g0 ? ((g0) longFunction).a : new h0(longFunction);
    }

    @Override // java.util.function.LongFunction
    public /* synthetic */ Object apply(long j) {
        return this.a.apply(j);
    }
}
