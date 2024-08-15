package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.BiFunction;
import j$.util.function.Consumer;
import j$.util.function.Function;
import j$.util.stream.IntStream;
import j$.util.stream.Stream;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.DoubleStream;
/* loaded from: classes2.dex */
public final /* synthetic */ class Q2 implements java.util.stream.Stream {
    public final /* synthetic */ Stream a;

    private /* synthetic */ Q2(Stream stream) {
        this.a = stream;
    }

    public static /* synthetic */ java.util.stream.Stream i0(Stream stream) {
        if (stream == null) {
            return null;
        }
        return stream instanceof Stream.VivifiedWrapper ? ((Stream.VivifiedWrapper) stream).a : new Q2(stream);
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ boolean allMatch(Predicate predicate) {
        return this.a.X(j$.util.function.I0.a(predicate));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ boolean anyMatch(Predicate predicate) {
        return this.a.C(j$.util.function.I0.a(predicate));
    }

    @Override // java.util.stream.BaseStream, java.lang.AutoCloseable
    public final /* synthetic */ void close() {
        this.a.close();
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ Object collect(Supplier supplier, BiConsumer biConsumer, BiConsumer biConsumer2) {
        return this.a.G(j$.util.function.K0.a(supplier), BiConsumer.VivifiedWrapper.convert(biConsumer), BiConsumer.VivifiedWrapper.convert(biConsumer2));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ Object collect(java.util.stream.Collector collector) {
        return this.a.collect(j.a(collector));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ long count() {
        return this.a.count();
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ java.util.stream.Stream distinct() {
        return i0(this.a.distinct());
    }

    public final /* synthetic */ boolean equals(Object obj) {
        Stream stream = this.a;
        if (obj instanceof Q2) {
            obj = ((Q2) obj).a;
        }
        return stream.equals(obj);
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ java.util.stream.Stream filter(Predicate predicate) {
        return i0(this.a.filter(j$.util.function.I0.a(predicate)));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ Optional findAny() {
        return j$.util.k.e(this.a.findAny());
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ Optional findFirst() {
        return j$.util.k.e(this.a.findFirst());
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ java.util.stream.Stream flatMap(Function function) {
        return i0(this.a.K(Function.VivifiedWrapper.convert(function)));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ DoubleStream flatMapToDouble(java.util.function.Function function) {
        return E.i0(this.a.o(Function.VivifiedWrapper.convert(function)));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ java.util.stream.IntStream flatMapToInt(java.util.function.Function function) {
        return IntStream.Wrapper.convert(this.a.c(Function.VivifiedWrapper.convert(function)));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ java.util.stream.LongStream flatMapToLong(java.util.function.Function function) {
        return l0.i0(this.a.Y(Function.VivifiedWrapper.convert(function)));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ void forEach(Consumer consumer) {
        this.a.forEach(Consumer.VivifiedWrapper.convert(consumer));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ void forEachOrdered(java.util.function.Consumer consumer) {
        this.a.F(Consumer.VivifiedWrapper.convert(consumer));
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }

    @Override // java.util.stream.BaseStream
    public final /* synthetic */ boolean isParallel() {
        return this.a.isParallel();
    }

    @Override // java.util.stream.BaseStream
    public final /* synthetic */ Iterator iterator() {
        return this.a.iterator();
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ java.util.stream.Stream limit(long j) {
        return i0(this.a.limit(j));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ java.util.stream.Stream map(java.util.function.Function function) {
        return i0(this.a.map(Function.VivifiedWrapper.convert(function)));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ DoubleStream mapToDouble(ToDoubleFunction toDoubleFunction) {
        return E.i0(this.a.f0(j$.util.function.M0.a(toDoubleFunction)));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ java.util.stream.IntStream mapToInt(ToIntFunction toIntFunction) {
        return IntStream.Wrapper.convert(this.a.I(j$.util.function.O0.a(toIntFunction)));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ java.util.stream.LongStream mapToLong(ToLongFunction toLongFunction) {
        return l0.i0(this.a.mapToLong(j$.util.function.Q0.a(toLongFunction)));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ Optional max(Comparator comparator) {
        return j$.util.k.e(this.a.max(comparator));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ Optional min(Comparator comparator) {
        return j$.util.k.e(this.a.min(comparator));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ boolean noneMatch(Predicate predicate) {
        return this.a.d0(j$.util.function.I0.a(predicate));
    }

    @Override // java.util.stream.BaseStream
    public final /* synthetic */ java.util.stream.BaseStream onClose(Runnable runnable) {
        return h.i0(this.a.onClose(runnable));
    }

    @Override // java.util.stream.BaseStream
    public final /* synthetic */ java.util.stream.BaseStream parallel() {
        return h.i0(this.a.parallel());
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ java.util.stream.Stream peek(java.util.function.Consumer consumer) {
        return i0(this.a.v(Consumer.VivifiedWrapper.convert(consumer)));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ Object reduce(Object obj, BiFunction biFunction, BinaryOperator binaryOperator) {
        return this.a.m(obj, BiFunction.VivifiedWrapper.convert(biFunction), j$.util.function.d.a(binaryOperator));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ Object reduce(Object obj, BinaryOperator binaryOperator) {
        return this.a.h0(obj, j$.util.function.d.a(binaryOperator));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ Optional reduce(BinaryOperator binaryOperator) {
        return j$.util.k.e(this.a.L(j$.util.function.d.a(binaryOperator)));
    }

    @Override // java.util.stream.BaseStream
    public final /* synthetic */ java.util.stream.BaseStream sequential() {
        return h.i0(this.a.sequential());
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ java.util.stream.Stream skip(long j) {
        return i0(this.a.skip(j));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ java.util.stream.Stream sorted() {
        return i0(this.a.sorted());
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ java.util.stream.Stream sorted(Comparator comparator) {
        return i0(this.a.sorted(comparator));
    }

    @Override // java.util.stream.BaseStream
    public final /* synthetic */ Spliterator spliterator() {
        return j$.util.P.a(this.a.spliterator());
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ Object[] toArray() {
        return this.a.toArray();
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ Object[] toArray(IntFunction intFunction) {
        return this.a.f(j$.util.function.L.a(intFunction));
    }

    @Override // java.util.stream.BaseStream
    public final /* synthetic */ java.util.stream.BaseStream unordered() {
        return h.i0(this.a.unordered());
    }
}
