package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.stream.IntStream;
import java.util.Iterator;
import java.util.LongSummaryStatistics;
import java.util.OptionalDouble;
import java.util.OptionalLong;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;
import java.util.function.ObjLongConsumer;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;

/* loaded from: classes2.dex */
public final /* synthetic */ class j0 implements java.util.stream.LongStream {
    public final /* synthetic */ LongStream a;

    private /* synthetic */ j0(LongStream longStream) {
        this.a = longStream;
    }

    public static /* synthetic */ java.util.stream.LongStream i0(LongStream longStream) {
        if (longStream == null) {
            return null;
        }
        return longStream instanceof i0 ? ((i0) longStream).a : new j0(longStream);
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ boolean allMatch(LongPredicate longPredicate) {
        return this.a.u(j$.util.function.Z.a(longPredicate));
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ boolean anyMatch(LongPredicate longPredicate) {
        return this.a.E(j$.util.function.Z.a(longPredicate));
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ DoubleStream asDoubleStream() {
        return C.i0(this.a.asDoubleStream());
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ OptionalDouble average() {
        return j$.util.a.x(this.a.average());
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ java.util.stream.Stream boxed() {
        return P2.i0(this.a.boxed());
    }

    @Override // java.util.stream.BaseStream, java.lang.AutoCloseable
    public final /* synthetic */ void close() {
        this.a.close();
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ Object collect(Supplier supplier, ObjLongConsumer objLongConsumer, BiConsumer biConsumer) {
        return this.a.Z(j$.util.function.t0.a(supplier), j$.util.function.m0.a(objLongConsumer), BiConsumer.VivifiedWrapper.convert(biConsumer));
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ long count() {
        return this.a.count();
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ java.util.stream.LongStream distinct() {
        return i0(this.a.distinct());
    }

    public final /* synthetic */ boolean equals(Object obj) {
        LongStream longStream = this.a;
        if (obj instanceof j0) {
            obj = ((j0) obj).a;
        }
        return longStream.equals(obj);
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ java.util.stream.LongStream filter(LongPredicate longPredicate) {
        return i0(this.a.M(j$.util.function.Z.a(longPredicate)));
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ OptionalLong findAny() {
        return j$.util.a.z(this.a.findAny());
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ OptionalLong findFirst() {
        return j$.util.a.z(this.a.findFirst());
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ java.util.stream.LongStream flatMap(LongFunction longFunction) {
        return i0(this.a.p(j$.util.function.X.a(longFunction)));
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ void forEach(LongConsumer longConsumer) {
        this.a.d(j$.util.function.U.a(longConsumer));
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ void forEachOrdered(LongConsumer longConsumer) {
        this.a.V(j$.util.function.U.a(longConsumer));
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }

    @Override // java.util.stream.BaseStream
    public final /* synthetic */ boolean isParallel() {
        return this.a.isParallel();
    }

    @Override // java.util.stream.LongStream, java.util.stream.BaseStream
    public final /* synthetic */ Iterator<Long> iterator() {
        return this.a.iterator();
    }

    @Override // java.util.stream.LongStream, java.util.stream.BaseStream
    public final /* synthetic */ Iterator<Long> iterator() {
        return j$.util.y.b(this.a.iterator());
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ java.util.stream.LongStream limit(long j) {
        return i0(this.a.limit(j));
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ java.util.stream.LongStream map(LongUnaryOperator longUnaryOperator) {
        return i0(this.a.v(j$.util.function.d0.b(longUnaryOperator)));
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ DoubleStream mapToDouble(LongToDoubleFunction longToDoubleFunction) {
        return C.i0(this.a.r(j$.util.function.a0.a(longToDoubleFunction)));
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ java.util.stream.IntStream mapToInt(LongToIntFunction longToIntFunction) {
        return IntStream.Wrapper.convert(this.a.A(j$.util.function.b0.a(longToIntFunction)));
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ java.util.stream.Stream mapToObj(LongFunction longFunction) {
        return P2.i0(this.a.mapToObj(j$.util.function.X.a(longFunction)));
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ OptionalLong max() {
        return j$.util.a.z(this.a.max());
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ OptionalLong min() {
        return j$.util.a.z(this.a.min());
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ boolean noneMatch(LongPredicate longPredicate) {
        return this.a.G(j$.util.function.Z.a(longPredicate));
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [java.util.stream.LongStream, java.util.stream.BaseStream] */
    @Override // java.util.stream.BaseStream
    public final /* synthetic */ java.util.stream.LongStream onClose(Runnable runnable) {
        return g.i0(this.a.onClose(runnable));
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [java.util.stream.LongStream, java.util.stream.BaseStream] */
    @Override // java.util.stream.LongStream, java.util.stream.BaseStream
    public final /* synthetic */ java.util.stream.LongStream parallel() {
        return g.i0(this.a.parallel());
    }

    @Override // java.util.stream.LongStream, java.util.stream.BaseStream
    public final /* synthetic */ java.util.stream.LongStream parallel() {
        return i0(this.a.parallel());
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ java.util.stream.LongStream peek(LongConsumer longConsumer) {
        return i0(this.a.o(j$.util.function.U.a(longConsumer)));
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ long reduce(long j, LongBinaryOperator longBinaryOperator) {
        return this.a.x(j, j$.util.function.P.a(longBinaryOperator));
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ OptionalLong reduce(LongBinaryOperator longBinaryOperator) {
        return j$.util.a.z(this.a.h(j$.util.function.P.a(longBinaryOperator)));
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [java.util.stream.LongStream, java.util.stream.BaseStream] */
    @Override // java.util.stream.LongStream, java.util.stream.BaseStream
    public final /* synthetic */ java.util.stream.LongStream sequential() {
        return g.i0(this.a.sequential());
    }

    @Override // java.util.stream.LongStream, java.util.stream.BaseStream
    public final /* synthetic */ java.util.stream.LongStream sequential() {
        return i0(this.a.sequential());
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ java.util.stream.LongStream skip(long j) {
        return i0(this.a.skip(j));
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ java.util.stream.LongStream sorted() {
        return i0(this.a.sorted());
    }

    @Override // java.util.stream.LongStream, java.util.stream.BaseStream
    public final /* synthetic */ Spliterator<Long> spliterator() {
        return j$.util.J.a(this.a.spliterator());
    }

    @Override // java.util.stream.LongStream, java.util.stream.BaseStream
    public final /* synthetic */ Spliterator<Long> spliterator() {
        return j$.util.P.a(this.a.spliterator());
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ long sum() {
        return this.a.sum();
    }

    @Override // java.util.stream.LongStream
    public final LongSummaryStatistics summaryStatistics() {
        this.a.summaryStatistics();
        throw new Error("Java 8+ API desugaring (library desugaring) cannot convert to java.util.LongSummaryStatistics");
    }

    @Override // java.util.stream.LongStream
    public final /* synthetic */ long[] toArray() {
        return this.a.toArray();
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [java.util.stream.LongStream, java.util.stream.BaseStream] */
    @Override // java.util.stream.BaseStream
    public final /* synthetic */ java.util.stream.LongStream unordered() {
        return g.i0(this.a.unordered());
    }
}
