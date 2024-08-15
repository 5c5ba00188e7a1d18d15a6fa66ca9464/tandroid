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
import j$.util.stream.IntStream;
import java.util.Comparator;
import java.util.Iterator;
/* loaded from: classes2.dex */
public interface Stream<T> extends BaseStream<T, Stream<T>> {

    /* loaded from: classes2.dex */
    public final /* synthetic */ class VivifiedWrapper implements Stream {
        public final /* synthetic */ java.util.stream.Stream a;

        private /* synthetic */ VivifiedWrapper(java.util.stream.Stream stream) {
            this.a = stream;
        }

        public static /* synthetic */ Stream convert(java.util.stream.Stream stream) {
            if (stream == null) {
                return null;
            }
            return stream instanceof Q2 ? ((Q2) stream).a : new VivifiedWrapper(stream);
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ boolean C(Predicate predicate) {
            return this.a.anyMatch(j$.util.function.J0.a(predicate));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ void F(Consumer consumer) {
            this.a.forEachOrdered(Consumer.Wrapper.convert(consumer));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Object G(Supplier supplier, BiConsumer biConsumer, BiConsumer biConsumer2) {
            return this.a.collect(j$.util.function.L0.a(supplier), j$.util.function.a.a(biConsumer), j$.util.function.a.a(biConsumer2));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ IntStream I(ToIntFunction toIntFunction) {
            return IntStream.VivifiedWrapper.convert(this.a.mapToInt(j$.util.function.P0.a(toIntFunction)));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Stream K(Function function) {
            return convert(this.a.flatMap(j$.util.function.D.a(function)));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Optional L(j$.util.function.f fVar) {
            return j$.util.k.a(this.a.reduce(j$.util.function.e.a(fVar)));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ boolean X(Predicate predicate) {
            return this.a.allMatch(j$.util.function.J0.a(predicate));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ LongStream Y(Function function) {
            return k0.i0(this.a.flatMapToLong(j$.util.function.D.a(function)));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ IntStream c(Function function) {
            return IntStream.VivifiedWrapper.convert(this.a.flatMapToInt(j$.util.function.D.a(function)));
        }

        @Override // j$.util.stream.BaseStream, java.lang.AutoCloseable
        public final /* synthetic */ void close() {
            this.a.close();
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Object collect(Collector collector) {
            return this.a.collect(k.a(collector));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ long count() {
            return this.a.count();
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ boolean d0(Predicate predicate) {
            return this.a.noneMatch(j$.util.function.J0.a(predicate));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Stream distinct() {
            return convert(this.a.distinct());
        }

        public final /* synthetic */ boolean equals(Object obj) {
            if (obj instanceof VivifiedWrapper) {
                obj = ((VivifiedWrapper) obj).a;
            }
            return this.a.equals(obj);
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Object[] f(j$.util.function.N n) {
            return this.a.toArray(j$.util.function.M.a(n));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ F f0(ToDoubleFunction toDoubleFunction) {
            return D.i0(this.a.mapToDouble(j$.util.function.N0.a(toDoubleFunction)));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Stream filter(Predicate predicate) {
            return convert(this.a.filter(j$.util.function.J0.a(predicate)));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Optional findAny() {
            return j$.util.k.a(this.a.findAny());
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Optional findFirst() {
            return j$.util.k.a(this.a.findFirst());
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ void forEach(Consumer consumer) {
            this.a.forEach(Consumer.Wrapper.convert(consumer));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Object h0(Object obj, j$.util.function.f fVar) {
            return this.a.reduce(obj, j$.util.function.e.a(fVar));
        }

        public final /* synthetic */ int hashCode() {
            return this.a.hashCode();
        }

        @Override // j$.util.stream.BaseStream
        public final /* synthetic */ boolean isParallel() {
            return this.a.isParallel();
        }

        @Override // j$.util.stream.BaseStream, j$.util.stream.F
        public final /* synthetic */ Iterator iterator() {
            return this.a.iterator();
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Stream limit(long j) {
            return convert(this.a.limit(j));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Object m(Object obj, BiFunction biFunction, j$.util.function.f fVar) {
            return this.a.reduce(obj, j$.util.function.b.a(biFunction), j$.util.function.e.a(fVar));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Stream map(Function function) {
            return convert(this.a.map(j$.util.function.D.a(function)));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ LongStream mapToLong(ToLongFunction toLongFunction) {
            return k0.i0(this.a.mapToLong(j$.util.function.R0.a(toLongFunction)));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Optional max(Comparator comparator) {
            return j$.util.k.a(this.a.max(comparator));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Optional min(Comparator comparator) {
            return j$.util.k.a(this.a.min(comparator));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ F o(Function function) {
            return D.i0(this.a.flatMapToDouble(j$.util.function.D.a(function)));
        }

        @Override // j$.util.stream.BaseStream
        public final /* synthetic */ BaseStream onClose(Runnable runnable) {
            return g.i0(this.a.onClose(runnable));
        }

        @Override // j$.util.stream.BaseStream, j$.util.stream.F
        public final /* synthetic */ BaseStream parallel() {
            return g.i0(this.a.parallel());
        }

        @Override // j$.util.stream.BaseStream, j$.util.stream.F
        public final /* synthetic */ BaseStream sequential() {
            return g.i0(this.a.sequential());
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Stream skip(long j) {
            return convert(this.a.skip(j));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Stream sorted() {
            return convert(this.a.sorted());
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Stream sorted(Comparator comparator) {
            return convert(this.a.sorted(comparator));
        }

        @Override // j$.util.stream.BaseStream, j$.util.stream.F
        public final /* synthetic */ j$.util.Q spliterator() {
            return j$.util.O.f(this.a.spliterator());
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Object[] toArray() {
            return this.a.toArray();
        }

        @Override // j$.util.stream.BaseStream
        public final /* synthetic */ BaseStream unordered() {
            return g.i0(this.a.unordered());
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Stream v(Consumer consumer) {
            return convert(this.a.peek(Consumer.Wrapper.convert(consumer)));
        }
    }

    boolean C(Predicate predicate);

    void F(Consumer consumer);

    Object G(Supplier supplier, BiConsumer biConsumer, BiConsumer biConsumer2);

    IntStream I(ToIntFunction toIntFunction);

    Stream K(Function function);

    Optional L(j$.util.function.f fVar);

    boolean X(Predicate predicate);

    LongStream Y(Function function);

    IntStream c(Function function);

    <R, A> R collect(Collector<? super T, A, R> collector);

    long count();

    boolean d0(Predicate predicate);

    Stream<T> distinct();

    Object[] f(j$.util.function.N n);

    F f0(ToDoubleFunction toDoubleFunction);

    Stream<T> filter(Predicate<? super T> predicate);

    Optional findAny();

    Optional findFirst();

    void forEach(Consumer<? super T> consumer);

    Object h0(Object obj, j$.util.function.f fVar);

    Stream<T> limit(long j);

    Object m(Object obj, BiFunction biFunction, j$.util.function.f fVar);

    <R> Stream<R> map(Function<? super T, ? extends R> function);

    LongStream mapToLong(ToLongFunction<? super T> toLongFunction);

    Optional max(Comparator comparator);

    Optional min(Comparator comparator);

    F o(Function function);

    Stream skip(long j);

    Stream sorted();

    Stream sorted(Comparator comparator);

    Object[] toArray();

    Stream v(Consumer consumer);
}
