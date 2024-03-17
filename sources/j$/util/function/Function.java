package j$.util.function;

import java.util.Objects;
/* loaded from: classes2.dex */
public interface Function<T, R> {

    /* loaded from: classes2.dex */
    public final /* synthetic */ class -CC {
        public static Function $default$andThen(Function function, Function function2) {
            Objects.requireNonNull(function2);
            return new i(function, function2, 0);
        }

        public static Function $default$compose(Function function, Function function2) {
            Objects.requireNonNull(function2);
            return new i(function, function2, 1);
        }
    }

    <V> Function<T, V> andThen(Function<? super R, ? extends V> function);

    R apply(T t);

    <V> Function<V, R> compose(Function<? super V, ? extends T> function);
}
