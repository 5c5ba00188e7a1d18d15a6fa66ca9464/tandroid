package j$.wrappers;

import java.util.function.IntToLongFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class Z implements IntToLongFunction {
    final /* synthetic */ j$.util.function.n a;

    private /* synthetic */ Z(j$.util.function.n nVar) {
        this.a = nVar;
    }

    public static /* synthetic */ IntToLongFunction a(j$.util.function.n nVar) {
        if (nVar == null) {
            return null;
        }
        return nVar instanceof Y ? ((Y) nVar).a : new Z(nVar);
    }

    @Override // java.util.function.IntToLongFunction
    public /* synthetic */ long applyAsLong(int i) {
        return this.a.applyAsLong(i);
    }
}
