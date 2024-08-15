package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Function;
import j$.util.function.Supplier;
import java.util.Set;
/* loaded from: classes2.dex */
public interface Collector<T, A, R> {
    BiConsumer accumulator();

    Set characteristics();

    j$.util.function.f combiner();

    Function finisher();

    Supplier supplier();
}
