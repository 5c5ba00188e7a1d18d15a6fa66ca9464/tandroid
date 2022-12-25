package j$.wrappers;

import java.util.function.LongFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class h0 implements LongFunction {
    final /* synthetic */ j$.util.function.r a;

    private /* synthetic */ h0(j$.util.function.r rVar) {
        this.a = rVar;
    }

    public static /* synthetic */ LongFunction a(j$.util.function.r rVar) {
        if (rVar == null) {
            return null;
        }
        return rVar instanceof g0 ? ((g0) rVar).a : new h0(rVar);
    }

    @Override // java.util.function.LongFunction
    public /* synthetic */ Object apply(long j) {
        return this.a.apply(j);
    }
}
