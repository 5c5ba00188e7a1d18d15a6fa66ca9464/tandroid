package j$.util;

import j$.util.function.Function;
import j$.util.function.ToIntFunction;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
/* loaded from: classes2.dex */
public final /* synthetic */ class Comparator$-CC {
    public static Comparator a() {
        return f.INSTANCE;
    }

    public static <T, U extends Comparable<? super U>> Comparator<T> comparing(Function<? super T, ? extends U> function) {
        Objects.requireNonNull(function);
        return new d(function);
    }

    public static <T> Comparator<T> comparingInt(ToIntFunction<? super T> toIntFunction) {
        Objects.requireNonNull(toIntFunction);
        return new d(toIntFunction);
    }

    public static <T extends Comparable<? super T>> Comparator<T> reverseOrder() {
        return Collections.reverseOrder();
    }
}
