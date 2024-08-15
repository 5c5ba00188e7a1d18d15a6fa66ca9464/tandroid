package j$.util.function;

import j$.util.function.BiFunction;
import j$.util.function.Function;
/* loaded from: classes2.dex */
public final /* synthetic */ class b implements java.util.function.BiFunction {
    public final /* synthetic */ BiFunction a;

    private /* synthetic */ b(BiFunction biFunction) {
        this.a = biFunction;
    }

    public static /* synthetic */ java.util.function.BiFunction a(BiFunction biFunction) {
        if (biFunction == null) {
            return null;
        }
        return biFunction instanceof BiFunction.VivifiedWrapper ? ((BiFunction.VivifiedWrapper) biFunction).a : biFunction instanceof f ? e.a((f) biFunction) : new b(biFunction);
    }

    @Override // java.util.function.BiFunction
    public final /* synthetic */ java.util.function.BiFunction andThen(java.util.function.Function function) {
        return a(this.a.andThen(Function.VivifiedWrapper.convert(function)));
    }

    @Override // java.util.function.BiFunction
    public final /* synthetic */ Object apply(Object obj, Object obj2) {
        return this.a.apply(obj, obj2);
    }

    public final /* synthetic */ boolean equals(Object obj) {
        BiFunction biFunction = this.a;
        if (obj instanceof b) {
            obj = ((b) obj).a;
        }
        return biFunction.equals(obj);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }
}
