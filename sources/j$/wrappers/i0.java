package j$.wrappers;

import java.util.function.LongFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class i0 implements LongFunction {
    final /* synthetic */ j$.util.function.r a;

    private /* synthetic */ i0(j$.util.function.r rVar) {
        this.a = rVar;
    }

    public static /* synthetic */ LongFunction a(j$.util.function.r rVar) {
        if (rVar == null) {
            return null;
        }
        return rVar instanceof h0 ? ((h0) rVar).a : new i0(rVar);
    }

    @Override // java.util.function.LongFunction
    public /* synthetic */ Object apply(long j) {
        return this.a.apply(j);
    }
}
