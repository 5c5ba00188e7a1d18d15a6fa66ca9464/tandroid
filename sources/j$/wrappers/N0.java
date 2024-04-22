package j$.wrappers;

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
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
/* loaded from: classes2.dex */
public final /* synthetic */ class N0 implements LongStream {
    final /* synthetic */ j$.util.stream.LongStream a;

    private /* synthetic */ N0(j$.util.stream.LongStream longStream) {
        this.a = longStream;
    }

    public static /* synthetic */ LongStream i0(j$.util.stream.LongStream longStream) {
        if (longStream == null) {
            return null;
        }
        return longStream instanceof M0 ? ((M0) longStream).a : new N0(longStream);
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ boolean allMatch(LongPredicate longPredicate) {
        return this.a.K(i0.a(longPredicate));
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ boolean anyMatch(LongPredicate longPredicate) {
        return this.a.k(i0.a(longPredicate));
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ DoubleStream asDoubleStream() {
        return L0.i0(this.a.asDoubleStream());
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ OptionalDouble average() {
        return j$.util.a.r(this.a.average());
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ Stream boxed() {
        return O0.i0(this.a.boxed());
    }

    @Override // java.util.stream.BaseStream, java.lang.AutoCloseable
    public /* synthetic */ void close() {
        this.a.close();
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ Object collect(Supplier supplier, ObjLongConsumer objLongConsumer, BiConsumer biConsumer) {
        return this.a.b0(y0.a(supplier), u0.a(objLongConsumer), q.a(biConsumer));
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ long count() {
        return this.a.count();
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ LongStream distinct() {
        return i0(this.a.distinct());
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ LongStream filter(LongPredicate longPredicate) {
        return i0(this.a.t(i0.a(longPredicate)));
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ OptionalLong findAny() {
        return j$.util.a.t(this.a.findAny());
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ OptionalLong findFirst() {
        return j$.util.a.t(this.a.findFirst());
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ LongStream flatMap(LongFunction longFunction) {
        return i0(this.a.r(g0.a(longFunction)));
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ void forEach(LongConsumer longConsumer) {
        this.a.d(e0.b(longConsumer));
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ void forEachOrdered(LongConsumer longConsumer) {
        this.a.W(e0.b(longConsumer));
    }

    @Override // java.util.stream.BaseStream
    public /* synthetic */ boolean isParallel() {
        return this.a.isParallel();
    }

    @Override // java.util.stream.LongStream, java.util.stream.BaseStream
    public /* synthetic */ Iterator<Long> iterator() {
        return this.a.iterator();
    }

    @Override // java.util.stream.LongStream, java.util.stream.BaseStream
    public /* synthetic */ Iterator<Long> iterator() {
        return f.a(this.a.iterator());
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ LongStream limit(long j) {
        return i0(this.a.limit(j));
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ LongStream map(LongUnaryOperator longUnaryOperator) {
        return i0(this.a.y(o0.c(longUnaryOperator)));
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ DoubleStream mapToDouble(LongToDoubleFunction longToDoubleFunction) {
        return L0.i0(this.a.N(k0.b(longToDoubleFunction)));
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ IntStream mapToInt(LongToIntFunction longToIntFunction) {
        return $r8$wrapper$java$util$stream$IntStream$-WRP.convert(this.a.a0(m0.b(longToIntFunction)));
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ Stream mapToObj(LongFunction longFunction) {
        return O0.i0(this.a.mapToObj(g0.a(longFunction)));
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ OptionalLong max() {
        return j$.util.a.t(this.a.max());
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ OptionalLong min() {
        return j$.util.a.t(this.a.min());
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ boolean noneMatch(LongPredicate longPredicate) {
        return this.a.Q(i0.a(longPredicate));
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [java.util.stream.LongStream, java.util.stream.BaseStream] */
    @Override // java.util.stream.BaseStream
    public /* synthetic */ LongStream onClose(Runnable runnable) {
        return H0.i0(this.a.onClose(runnable));
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [java.util.stream.LongStream, java.util.stream.BaseStream] */
    @Override // java.util.stream.LongStream, java.util.stream.BaseStream
    public /* synthetic */ LongStream parallel() {
        return H0.i0(this.a.parallel());
    }

    @Override // java.util.stream.LongStream, java.util.stream.BaseStream
    public /* synthetic */ LongStream parallel() {
        return i0(this.a.parallel());
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ LongStream peek(LongConsumer longConsumer) {
        return i0(this.a.o(e0.b(longConsumer)));
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ long reduce(long j, LongBinaryOperator longBinaryOperator) {
        return this.a.C(j, c0.a(longBinaryOperator));
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ OptionalLong reduce(LongBinaryOperator longBinaryOperator) {
        return j$.util.a.t(this.a.g(c0.a(longBinaryOperator)));
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [java.util.stream.LongStream, java.util.stream.BaseStream] */
    @Override // java.util.stream.LongStream, java.util.stream.BaseStream
    public /* synthetic */ LongStream sequential() {
        return H0.i0(this.a.sequential());
    }

    @Override // java.util.stream.LongStream, java.util.stream.BaseStream
    public /* synthetic */ LongStream sequential() {
        return i0(this.a.sequential());
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ LongStream skip(long j) {
        return i0(this.a.skip(j));
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ LongStream sorted() {
        return i0(this.a.sorted());
    }

    @Override // java.util.stream.LongStream, java.util.stream.BaseStream
    public /* synthetic */ Spliterator<Long> spliterator() {
        return n.a(this.a.spliterator());
    }

    @Override // java.util.stream.LongStream, java.util.stream.BaseStream
    public /* synthetic */ Spliterator<Long> spliterator() {
        return h.a(this.a.spliterator());
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ long sum() {
        return this.a.sum();
    }

    @Override // java.util.stream.LongStream
    public LongSummaryStatistics summaryStatistics() {
        this.a.summaryStatistics();
        throw new Error("Java 8+ API desugaring (library desugaring) cannot convert to java.util.LongSummaryStatistics");
    }

    @Override // java.util.stream.LongStream
    public /* synthetic */ long[] toArray() {
        return this.a.toArray();
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [java.util.stream.LongStream, java.util.stream.BaseStream] */
    @Override // java.util.stream.BaseStream
    public /* synthetic */ LongStream unordered() {
        return H0.i0(this.a.unordered());
    }
}
