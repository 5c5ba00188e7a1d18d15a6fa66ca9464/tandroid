package j$.wrappers;

import java.util.function.BiFunction;
import java.util.function.Function;
/* loaded from: classes2.dex */
public final /* synthetic */ class t implements BiFunction {
    final /* synthetic */ j$.util.function.BiFunction a;

    private /* synthetic */ t(j$.util.function.BiFunction biFunction) {
        this.a = biFunction;
    }

    public static /* synthetic */ BiFunction a(j$.util.function.BiFunction biFunction) {
        if (biFunction == null) {
            return null;
        }
        return biFunction instanceof s ? ((s) biFunction).a : new t(biFunction);
    }

    @Override // java.util.function.BiFunction
    public /* synthetic */ BiFunction andThen(Function function) {
        return a(this.a.andThen(L.a(function)));
    }

    @Override // java.util.function.BiFunction
    public /* synthetic */ Object apply(Object obj, Object obj2) {
        return this.a.apply(obj, obj2);
    }
}
