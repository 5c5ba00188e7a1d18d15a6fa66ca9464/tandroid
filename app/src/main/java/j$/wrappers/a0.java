package j$.wrappers;

import java.util.function.IntToLongFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class a0 implements IntToLongFunction {
    final /* synthetic */ j$.util.function.n a;

    private /* synthetic */ a0(j$.util.function.n nVar) {
        this.a = nVar;
    }

    public static /* synthetic */ IntToLongFunction a(j$.util.function.n nVar) {
        if (nVar == null) {
            return null;
        }
        return nVar instanceof Z ? ((Z) nVar).a : new a0(nVar);
    }

    @Override // java.util.function.IntToLongFunction
    public /* synthetic */ long applyAsLong(int i) {
        return this.a.applyAsLong(i);
    }
}
