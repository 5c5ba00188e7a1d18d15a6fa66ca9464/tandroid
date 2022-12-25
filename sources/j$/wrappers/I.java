package j$.wrappers;

import java.util.function.DoubleToLongFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class I implements DoubleToLongFunction {
    final /* synthetic */ j$.util.function.h a;

    private /* synthetic */ I(j$.util.function.h hVar) {
        this.a = hVar;
    }

    public static /* synthetic */ DoubleToLongFunction a(j$.util.function.h hVar) {
        if (hVar == null) {
            return null;
        }
        return hVar instanceof H ? ((H) hVar).a : new I(hVar);
    }

    @Override // java.util.function.DoubleToLongFunction
    public /* synthetic */ long applyAsLong(double d) {
        return this.a.applyAsLong(d);
    }
}
