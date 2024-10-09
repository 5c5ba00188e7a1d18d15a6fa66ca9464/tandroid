package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.BiFunction;
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
public final /* synthetic */ class P2 implements java.util.stream.Stream {
    public final /* synthetic */ Stream a;

    private /* synthetic */ P2(Stream stream) {
        this.a = stream;
    }

    public static /* synthetic */ java.util.stream.Stream i0(Stream stream) {
        if (stream == null) {
            return null;
        }
        return stream instanceof Stream.VivifiedWrapper ? ((Stream.VivifiedWrapper) stream).a : new P2(stream);
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ boolean allMatch(Predicate predicate) {
        return this.a.P(j$.util.function.r0.a(predicate));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ boolean anyMatch(Predicate predicate) {
        return this.a.a(j$.util.function.r0.a(predicate));
    }

    @Override // java.util.stream.BaseStream, java.lang.AutoCloseable
    public final /* synthetic */ void close() {
        this.a.close();
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ Object collect(Supplier supplier, BiConsumer biConsumer, BiConsumer biConsumer2) {
        return this.a.i(j$.util.function.t0.a(supplier), BiConsumer.VivifiedWrapper.convert(biConsumer), BiConsumer.VivifiedWrapper.convert(biConsumer2));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ Object collect(java.util.stream.Collector collector) {
        return this.a.collect(i.a(collector));
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
        if (obj instanceof P2) {
            obj = ((P2) obj).a;
        }
        return stream.equals(obj);
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ java.util.stream.Stream filter(Predicate predicate) {
        return i0(this.a.filter(j$.util.function.r0.a(predicate)));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ Optional findAny() {
        return j$.util.a.w(this.a.findAny());
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ Optional findFirst() {
        return j$.util.a.w(this.a.findFirst());
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ java.util.stream.Stream flatMap(Function function) {
        return i0(this.a.n(Function.VivifiedWrapper.convert(function)));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ DoubleStream flatMapToDouble(java.util.function.Function function) {
        return C.i0(this.a.y(Function.VivifiedWrapper.convert(function)));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ java.util.stream.IntStream flatMapToInt(java.util.function.Function function) {
        return IntStream.Wrapper.convert(this.a.c(Function.VivifiedWrapper.convert(function)));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ java.util.stream.LongStream flatMapToLong(java.util.function.Function function) {
        return j0.i0(this.a.R(Function.VivifiedWrapper.convert(function)));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ void forEach(Consumer consumer) {
        this.a.forEach(j$.util.function.g.a(consumer));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ void forEachOrdered(Consumer consumer) {
        this.a.f(j$.util.function.g.a(consumer));
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
        return C.i0(this.a.c0(j$.util.function.v0.a(toDoubleFunction)));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ java.util.stream.IntStream mapToInt(ToIntFunction toIntFunction) {
        return IntStream.Wrapper.convert(this.a.m(j$.util.function.x0.a(toIntFunction)));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ java.util.stream.LongStream mapToLong(ToLongFunction toLongFunction) {
        return j0.i0(this.a.mapToLong(j$.util.function.z0.a(toLongFunction)));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ Optional max(Comparator comparator) {
        return j$.util.a.w(this.a.max(comparator));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ Optional min(Comparator comparator) {
        return j$.util.a.w(this.a.min(comparator));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ boolean noneMatch(Predicate predicate) {
        return this.a.Y(j$.util.function.r0.a(predicate));
    }

    @Override // java.util.stream.BaseStream
    public final /* synthetic */ java.util.stream.BaseStream onClose(Runnable runnable) {
        return g.i0(this.a.onClose(runnable));
    }

    @Override // java.util.stream.BaseStream
    public final /* synthetic */ java.util.stream.BaseStream parallel() {
        return g.i0(this.a.parallel());
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ java.util.stream.Stream peek(Consumer consumer) {
        return i0(this.a.O(j$.util.function.g.a(consumer)));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ Object reduce(Object obj, BiFunction biFunction, BinaryOperator binaryOperator) {
        return this.a.w(obj, BiFunction.VivifiedWrapper.convert(biFunction), j$.util.function.d.a(binaryOperator));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ Object reduce(Object obj, BinaryOperator binaryOperator) {
        return this.a.h0(obj, j$.util.function.d.a(binaryOperator));
    }

    @Override // java.util.stream.Stream
    public final /* synthetic */ Optional reduce(BinaryOperator binaryOperator) {
        return j$.util.a.w(this.a.q(j$.util.function.d.a(binaryOperator)));
    }

    @Override // java.util.stream.BaseStream
    public final /* synthetic */ java.util.stream.BaseStream sequential() {
        return g.i0(this.a.sequential());
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
        return this.a.l(j$.util.function.G.a(intFunction));
    }

    @Override // java.util.stream.BaseStream
    public final /* synthetic */ java.util.stream.BaseStream unordered() {
        return g.i0(this.a.unordered());
    }
}
