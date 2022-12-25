package j$.wrappers;

import java.util.function.LongFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class g0 implements j$.util.function.r {
    final /* synthetic */ LongFunction a;

    private /* synthetic */ g0(LongFunction longFunction) {
        this.a = longFunction;
    }

    public static /* synthetic */ j$.util.function.r a(LongFunction longFunction) {
        if (longFunction == null) {
            return null;
        }
        return longFunction instanceof h0 ? ((h0) longFunction).a : new g0(longFunction);
    }

    @Override // j$.util.function.r
    public /* synthetic */ Object apply(long j) {
        return this.a.apply(j);
    }
}
