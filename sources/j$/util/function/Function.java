package j$.util.function;

import java.util.function.UnaryOperator;
/* loaded from: classes2.dex */
public interface Function<T, R> {

    /* loaded from: classes2.dex */
    public final /* synthetic */ class -CC<T, R> {
        public static Function $default$andThen(Function function, Function function2) {
            function2.getClass();
            return new C(function, function2, 0);
        }

        public static Function $default$compose(Function function, Function function2) {
            function2.getClass();
            return new C(function, function2, 1);
        }
    }

    /* loaded from: classes2.dex */
    public final /* synthetic */ class VivifiedWrapper implements Function {
        public final /* synthetic */ java.util.function.Function a;

        private /* synthetic */ VivifiedWrapper(java.util.function.Function function) {
            this.a = function;
        }

        public static /* synthetic */ Function convert(java.util.function.Function function) {
            if (function == null) {
                return null;
            }
            return function instanceof D ? ((D) function).a : function instanceof UnaryOperator ? S0.a((UnaryOperator) function) : new VivifiedWrapper(function);
        }

        @Override // j$.util.function.Function
        public final /* synthetic */ Function andThen(Function function) {
            return convert(this.a.andThen(D.a(function)));
        }

        @Override // j$.util.function.Function
        public final /* synthetic */ Object apply(Object obj) {
            return this.a.apply(obj);
        }

        @Override // j$.util.function.Function
        public final /* synthetic */ Function compose(Function function) {
            return convert(this.a.compose(D.a(function)));
        }

        public final /* synthetic */ boolean equals(Object obj) {
            if (obj instanceof VivifiedWrapper) {
                obj = ((VivifiedWrapper) obj).a;
            }
            return this.a.equals(obj);
        }

        public final /* synthetic */ int hashCode() {
            return this.a.hashCode();
        }
    }

    <V> Function<T, V> andThen(Function<? super R, ? extends V> function);

    R apply(T t);

    <V> Function<V, R> compose(Function<? super V, ? extends T> function);
}
