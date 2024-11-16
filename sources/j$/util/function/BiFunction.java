package j$.util.function;

import java.util.function.BinaryOperator;

/* loaded from: classes2.dex */
public interface BiFunction<T, U, R> {

    public final /* synthetic */ class VivifiedWrapper implements BiFunction {
        public final /* synthetic */ java.util.function.BiFunction a;

        private /* synthetic */ VivifiedWrapper(java.util.function.BiFunction biFunction) {
            this.a = biFunction;
        }

        public static /* synthetic */ BiFunction convert(java.util.function.BiFunction biFunction) {
            if (biFunction == null) {
                return null;
            }
            return biFunction instanceof b ? ((b) biFunction).a : biFunction instanceof BinaryOperator ? d.a((BinaryOperator) biFunction) : new VivifiedWrapper(biFunction);
        }

        @Override // j$.util.function.BiFunction
        public final /* synthetic */ BiFunction andThen(Function function) {
            return convert(this.a.andThen(y.a(function)));
        }

        @Override // j$.util.function.BiFunction
        public final /* synthetic */ Object apply(Object obj, Object obj2) {
            return this.a.apply(obj, obj2);
        }

        public final /* synthetic */ boolean equals(Object obj) {
            java.util.function.BiFunction biFunction = this.a;
            if (obj instanceof VivifiedWrapper) {
                obj = ((VivifiedWrapper) obj).a;
            }
            return biFunction.equals(obj);
        }

        public final /* synthetic */ int hashCode() {
            return this.a.hashCode();
        }
    }

    BiFunction andThen(Function function);

    Object apply(Object obj, Object obj2);
}
