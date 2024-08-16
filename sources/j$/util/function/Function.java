package j$.util.function;

import java.util.function.UnaryOperator;
/* loaded from: classes2.dex */
public interface Function<T, R> {

    /* loaded from: classes2.dex */
    public final /* synthetic */ class -CC {
        public static Function $default$andThen(Function function, Function function2) {
            function2.getClass();
            return new x(function, function2, 0);
        }

        public static Function $default$compose(Function function, Function function2) {
            function2.getClass();
            return new x(function, function2, 1);
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
            return function instanceof y ? ((y) function).a : function instanceof UnaryOperator ? B0.a((UnaryOperator) function) : new VivifiedWrapper(function);
        }

        @Override // j$.util.function.Function
        public final /* synthetic */ Function andThen(Function function) {
            return convert(this.a.andThen(y.a(function)));
        }

        @Override // j$.util.function.Function
        public final /* synthetic */ Object apply(Object obj) {
            return this.a.apply(obj);
        }

        @Override // j$.util.function.Function
        public final /* synthetic */ Function compose(Function function) {
            return convert(this.a.compose(y.a(function)));
        }

        public final /* synthetic */ boolean equals(Object obj) {
            java.util.function.Function function = this.a;
            if (obj instanceof VivifiedWrapper) {
                obj = ((VivifiedWrapper) obj).a;
            }
            return function.equals(obj);
        }

        public final /* synthetic */ int hashCode() {
            return this.a.hashCode();
        }
    }

    <V> Function<T, V> andThen(Function<? super R, ? extends V> function);

    R apply(T t);

    <V> Function<V, R> compose(Function<? super V, ? extends T> function);
}
