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

    public final /* synthetic */ class VivifiedWrapper implements Stream {
        public final /* synthetic */ java.util.stream.Stream a;

        private /* synthetic */ VivifiedWrapper(java.util.stream.Stream stream) {
            this.a = stream;
        }

        public static /* synthetic */ Stream convert(java.util.stream.Stream stream) {
            if (stream == null) {
                return null;
            }
            return stream instanceof P2 ? ((P2) stream).a : new VivifiedWrapper(stream);
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Stream O(Consumer consumer) {
            return convert(this.a.peek(Consumer.Wrapper.convert(consumer)));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ boolean P(Predicate predicate) {
            return this.a.allMatch(j$.util.function.s0.a(predicate));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ LongStream R(Function function) {
            return i0.i0(this.a.flatMapToLong(j$.util.function.y.a(function)));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ boolean Y(Predicate predicate) {
            return this.a.noneMatch(j$.util.function.s0.a(predicate));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ boolean a(Predicate predicate) {
            return this.a.anyMatch(j$.util.function.s0.a(predicate));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ IntStream c(Function function) {
            return IntStream.VivifiedWrapper.convert(this.a.flatMapToInt(j$.util.function.y.a(function)));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ D c0(ToDoubleFunction toDoubleFunction) {
            return B.i0(this.a.mapToDouble(j$.util.function.w0.a(toDoubleFunction)));
        }

        @Override // j$.util.stream.BaseStream, java.lang.AutoCloseable
        public final /* synthetic */ void close() {
            this.a.close();
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Object collect(Collector collector) {
            return this.a.collect(j.a(collector));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ long count() {
            return this.a.count();
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Stream distinct() {
            return convert(this.a.distinct());
        }

        public final /* synthetic */ boolean equals(Object obj) {
            java.util.stream.Stream stream = this.a;
            if (obj instanceof VivifiedWrapper) {
                obj = ((VivifiedWrapper) obj).a;
            }
            return stream.equals(obj);
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ void f(Consumer consumer) {
            this.a.forEachOrdered(Consumer.Wrapper.convert(consumer));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Stream filter(Predicate predicate) {
            return convert(this.a.filter(j$.util.function.s0.a(predicate)));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Optional findAny() {
            return j$.util.a.r(this.a.findAny());
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Optional findFirst() {
            return j$.util.a.r(this.a.findFirst());
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

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Object i(Supplier supplier, BiConsumer biConsumer, BiConsumer biConsumer2) {
            return this.a.collect(j$.util.function.u0.a(supplier), j$.util.function.a.a(biConsumer), j$.util.function.a.a(biConsumer2));
        }

        @Override // j$.util.stream.BaseStream
        public final /* synthetic */ boolean isParallel() {
            return this.a.isParallel();
        }

        @Override // j$.util.stream.BaseStream, j$.util.stream.D
        public final /* synthetic */ Iterator iterator() {
            return this.a.iterator();
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Object[] l(j$.util.function.I i) {
            return this.a.toArray(j$.util.function.H.a(i));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Stream limit(long j) {
            return convert(this.a.limit(j));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ IntStream m(ToIntFunction toIntFunction) {
            return IntStream.VivifiedWrapper.convert(this.a.mapToInt(j$.util.function.y0.a(toIntFunction)));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Stream map(Function function) {
            return convert(this.a.map(j$.util.function.y.a(function)));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ LongStream mapToLong(ToLongFunction toLongFunction) {
            return i0.i0(this.a.mapToLong(j$.util.function.A0.a(toLongFunction)));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Optional max(Comparator comparator) {
            return j$.util.a.r(this.a.max(comparator));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Optional min(Comparator comparator) {
            return j$.util.a.r(this.a.min(comparator));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Stream n(Function function) {
            return convert(this.a.flatMap(j$.util.function.y.a(function)));
        }

        @Override // j$.util.stream.BaseStream
        public final /* synthetic */ BaseStream onClose(Runnable runnable) {
            return f.i0(this.a.onClose(runnable));
        }

        @Override // j$.util.stream.BaseStream
        public final /* synthetic */ BaseStream parallel() {
            return f.i0(this.a.parallel());
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Optional q(j$.util.function.f fVar) {
            return j$.util.a.r(this.a.reduce(j$.util.function.e.a(fVar)));
        }

        @Override // j$.util.stream.BaseStream
        public final /* synthetic */ BaseStream sequential() {
            return f.i0(this.a.sequential());
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

        @Override // j$.util.stream.BaseStream, j$.util.stream.D
        public final /* synthetic */ j$.util.Q spliterator() {
            return j$.util.O.b(this.a.spliterator());
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Object[] toArray() {
            return this.a.toArray();
        }

        @Override // j$.util.stream.BaseStream
        public final /* synthetic */ BaseStream unordered() {
            return f.i0(this.a.unordered());
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ Object w(Object obj, BiFunction biFunction, j$.util.function.f fVar) {
            return this.a.reduce(obj, j$.util.function.b.a(biFunction), j$.util.function.e.a(fVar));
        }

        @Override // j$.util.stream.Stream
        public final /* synthetic */ D y(Function function) {
            return B.i0(this.a.flatMapToDouble(j$.util.function.y.a(function)));
        }
    }

    Stream O(Consumer consumer);

    boolean P(Predicate predicate);

    LongStream R(Function function);

    boolean Y(Predicate predicate);

    boolean a(Predicate predicate);

    IntStream c(Function function);

    D c0(ToDoubleFunction toDoubleFunction);

    <R, A> R collect(Collector<? super T, A, R> collector);

    long count();

    Stream<T> distinct();

    void f(Consumer consumer);

    Stream<T> filter(Predicate<? super T> predicate);

    Optional findAny();

    Optional findFirst();

    void forEach(Consumer<? super T> consumer);

    Object h0(Object obj, j$.util.function.f fVar);

    Object i(Supplier supplier, BiConsumer biConsumer, BiConsumer biConsumer2);

    Object[] l(j$.util.function.I i);

    Stream<T> limit(long j);

    IntStream m(ToIntFunction toIntFunction);

    <R> Stream<R> map(Function<? super T, ? extends R> function);

    LongStream mapToLong(ToLongFunction<? super T> toLongFunction);

    Optional max(Comparator comparator);

    Optional min(Comparator comparator);

    Stream n(Function function);

    Optional q(j$.util.function.f fVar);

    Stream skip(long j);

    Stream sorted();

    Stream sorted(Comparator comparator);

    Object[] toArray();

    Object w(Object obj, BiFunction biFunction, j$.util.function.f fVar);

    D y(Function function);
}
