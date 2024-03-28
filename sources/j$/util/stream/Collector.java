package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Function;
import j$.util.function.Supplier;
import java.util.Set;
/* loaded from: classes2.dex */
public interface Collector<T, A, R> {

    /* loaded from: classes2.dex */
    public enum a {
        CONCURRENT,
        UNORDERED,
        IDENTITY_FINISH
    }

    BiConsumer accumulator();

    Set characteristics();

    j$.util.function.b combiner();

    Function finisher();

    Supplier supplier();
}
