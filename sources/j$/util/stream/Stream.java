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
    Object B(Object obj, BiFunction biFunction, j$.util.function.b bVar);

    V E(Function function);

    Stream U(Consumer consumer);

    boolean V(Predicate predicate);

    f1 W(Function function);

    boolean a(Predicate predicate);

    boolean b0(Predicate predicate);

    IntStream c(Function function);

    @Override // j$.util.stream.g, java.lang.AutoCloseable
    /* synthetic */ void close();

    <R, A> R collect(Collector<? super T, A, R> collector);

    long count();

    Stream distinct();

    void e(Consumer consumer);

    f1 e0(ToLongFunction toLongFunction);

    Stream<T> filter(Predicate<? super T> predicate);

    Optional findAny();

    Optional findFirst();

    void forEach(Consumer<? super T> consumer);

    V h0(ToDoubleFunction toDoubleFunction);

    Object i(Supplier supplier, BiConsumer biConsumer, BiConsumer biConsumer2);

    Object k0(Object obj, j$.util.function.b bVar);

    Object[] l(j$.util.function.m mVar);

    Stream limit(long j);

    IntStream m(ToIntFunction toIntFunction);

    Optional max(Comparator comparator);

    Optional min(Comparator comparator);

    Stream n(Function function);

    Stream o(Function function);

    Stream skip(long j);

    Stream sorted();

    Stream sorted(Comparator comparator);

    Optional t(j$.util.function.b bVar);

    Object[] toArray();
}
