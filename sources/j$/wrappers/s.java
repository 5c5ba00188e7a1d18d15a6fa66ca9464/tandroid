package j$.wrappers;

import j$.util.function.BiFunction;
import j$.util.function.Function;
/* loaded from: classes2.dex */
public final /* synthetic */ class s implements BiFunction {
    final /* synthetic */ java.util.function.BiFunction a;

    private /* synthetic */ s(java.util.function.BiFunction biFunction) {
        this.a = biFunction;
    }

    public static /* synthetic */ BiFunction a(java.util.function.BiFunction biFunction) {
        if (biFunction == null) {
            return null;
        }
        return biFunction instanceof t ? ((t) biFunction).a : new s(biFunction);
    }

    @Override // j$.util.function.BiFunction
    public /* synthetic */ BiFunction andThen(Function function) {
        return a(this.a.andThen(M.a(function)));
    }

    @Override // j$.util.function.BiFunction
    public /* synthetic */ Object apply(Object obj, Object obj2) {
        return this.a.apply(obj, obj2);
    }
}
