package j$.util.stream;

import j$.util.Optional;
import j$.util.function.BiConsumer;
import j$.util.function.BiFunction;
import j$.util.function.Consumer;
import j$.util.function.Function;
import j$.util.function.Predicate;
import j$.util.function.Supplier;
import j$.util.function.ToDoubleFunction;
import j$.util.function.ToIntFunction;
import j$.util.function.ToLongFunction;
import java.util.Comparator;
/* loaded from: classes2.dex */
public interface Stream<T> extends g {
    Object A(Object obj, BiFunction biFunction, j$.util.function.b bVar);

    V D(Function function);

    Stream S(Consumer consumer);

    boolean T(Predicate predicate);

    LongStream U(Function function);

    boolean Z(Predicate predicate);

    boolean a(Predicate predicate);

    IntStream c(Function function);

    @Override // j$.util.stream.g, java.lang.AutoCloseable
    /* synthetic */ void close();

    <R, A> R collect(Collector<? super T, A, R> collector);

    long count();

    Stream<T> distinct();

    void e(Consumer consumer);

    V e0(ToDoubleFunction toDoubleFunction);

    Stream<T> filter(Predicate<? super T> predicate);

    Optional findAny();

    Optional findFirst();

    void forEach(Consumer<? super T> consumer);

    Object h0(Object obj, j$.util.function.b bVar);

    Object i(Supplier supplier, BiConsumer biConsumer, BiConsumer biConsumer2);

    Object[] l(j$.util.function.m mVar);

    Stream<T> limit(long j);

    IntStream m(ToIntFunction toIntFunction);

    <R> Stream<R> map(Function<? super T, ? extends R> function);

    LongStream mapToLong(ToLongFunction<? super T> toLongFunction);

    Optional max(Comparator comparator);

    Optional min(Comparator comparator);

    Stream n(Function function);

    Optional s(j$.util.function.b bVar);

    Stream skip(long j);

    Stream sorted();

    Stream sorted(Comparator comparator);

    Object[] toArray();
}
