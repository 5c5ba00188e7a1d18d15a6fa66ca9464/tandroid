package j$.wrappers;

import java.util.function.LongFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class h0 implements j$.util.function.r {
    final /* synthetic */ LongFunction a;

    private /* synthetic */ h0(LongFunction longFunction) {
        this.a = longFunction;
    }

    public static /* synthetic */ j$.util.function.r a(LongFunction longFunction) {
        if (longFunction == null) {
            return null;
        }
        return longFunction instanceof i0 ? ((i0) longFunction).a : new h0(longFunction);
    }

    @Override // j$.util.function.r
    public /* synthetic */ Object apply(long j) {
        return this.a.apply(j);
    }
}
