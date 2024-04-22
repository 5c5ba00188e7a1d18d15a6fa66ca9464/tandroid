package j$.wrappers;

import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
/* loaded from: classes2.dex */
public final /* synthetic */ class $r8$wrapper$java$util$stream$IntStream$-WRP implements IntStream {
    final /* synthetic */ j$.util.stream.IntStream a;

    private /* synthetic */ $r8$wrapper$java$util$stream$IntStream$-WRP(j$.util.stream.IntStream intStream) {
        this.a = intStream;
    }

    public static /* synthetic */ IntStream convert(j$.util.stream.IntStream intStream) {
        if (intStream == null) {
            return null;
        }
        return intStream instanceof $r8$wrapper$java$util$stream$IntStream$-V-WRP ? (($r8$wrapper$java$util$stream$IntStream$-V-WRP) intStream).a : new $r8$wrapper$java$util$stream$IntStream$-WRP(intStream);
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ boolean allMatch(IntPredicate intPredicate) {
        return this.a.B(U.a(intPredicate));
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ boolean anyMatch(IntPredicate intPredicate) {
        return this.a.E(U.a(intPredicate));
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ DoubleStream asDoubleStream() {
        return L0.i0(this.a.asDoubleStream());
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ LongStream asLongStream() {
        return N0.i0(this.a.asLongStream());
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ OptionalDouble average() {
        return j$.util.a.r(this.a.average());
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ Stream boxed() {
        return O0.i0(this.a.boxed());
    }

    @Override // java.util.stream.BaseStream, java.lang.AutoCloseable
    public /* synthetic */ void close() {
        this.a.close();
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ Object collect(Supplier supplier, ObjIntConsumer objIntConsumer, BiConsumer biConsumer) {
        return this.a.f0(y0.a(supplier), s0.a(objIntConsumer), q.a(biConsumer));
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ long count() {
        return this.a.count();
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ IntStream distinct() {
        return convert(this.a.distinct());
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ IntStream filter(IntPredicate intPredicate) {
        return convert(this.a.h(U.a(intPredicate)));
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ OptionalInt findAny() {
        return j$.util.a.s(this.a.findAny());
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ OptionalInt findFirst() {
        return j$.util.a.s(this.a.findFirst());
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ IntStream flatMap(IntFunction intFunction) {
        return convert(this.a.O(S.a(intFunction)));
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ void forEach(IntConsumer intConsumer) {
        this.a.R(P.b(intConsumer));
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ void forEachOrdered(IntConsumer intConsumer) {
        this.a.H(P.b(intConsumer));
    }

    @Override // java.util.stream.BaseStream
    public /* synthetic */ boolean isParallel() {
        return this.a.isParallel();
    }

    @Override // java.util.stream.IntStream, java.util.stream.BaseStream
    public /* synthetic */ Iterator<Integer> iterator() {
        return this.a.iterator();
    }

    @Override // java.util.stream.IntStream, java.util.stream.BaseStream
    public /* synthetic */ Iterator<Integer> iterator() {
        return d.a(this.a.iterator());
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ IntStream limit(long j) {
        return convert(this.a.limit(j));
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ IntStream map(IntUnaryOperator intUnaryOperator) {
        return convert(this.a.p(a0.b(intUnaryOperator)));
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ DoubleStream mapToDouble(IntToDoubleFunction intToDoubleFunction) {
        return L0.i0(this.a.z(W.b(intToDoubleFunction)));
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ LongStream mapToLong(IntToLongFunction intToLongFunction) {
        return N0.i0(this.a.f(Y.a(intToLongFunction)));
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ Stream mapToObj(IntFunction intFunction) {
        return O0.i0(this.a.I(S.a(intFunction)));
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ OptionalInt max() {
        return j$.util.a.s(this.a.max());
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ OptionalInt min() {
        return j$.util.a.s(this.a.min());
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ boolean noneMatch(IntPredicate intPredicate) {
        return this.a.u(U.a(intPredicate));
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [java.util.stream.IntStream, java.util.stream.BaseStream] */
    @Override // java.util.stream.BaseStream
    public /* synthetic */ IntStream onClose(Runnable runnable) {
        return H0.i0(this.a.onClose(runnable));
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [java.util.stream.IntStream, java.util.stream.BaseStream] */
    @Override // java.util.stream.IntStream, java.util.stream.BaseStream
    public /* synthetic */ IntStream parallel() {
        return H0.i0(this.a.parallel());
    }

    @Override // java.util.stream.IntStream, java.util.stream.BaseStream
    public /* synthetic */ IntStream parallel() {
        return convert(this.a.parallel());
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ IntStream peek(IntConsumer intConsumer) {
        return convert(this.a.Y(P.b(intConsumer)));
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ int reduce(int i, IntBinaryOperator intBinaryOperator) {
        return this.a.M(i, N.a(intBinaryOperator));
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ OptionalInt reduce(IntBinaryOperator intBinaryOperator) {
        return j$.util.a.s(this.a.X(N.a(intBinaryOperator)));
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [java.util.stream.IntStream, java.util.stream.BaseStream] */
    @Override // java.util.stream.IntStream, java.util.stream.BaseStream
    public /* synthetic */ IntStream sequential() {
        return H0.i0(this.a.sequential());
    }

    @Override // java.util.stream.IntStream, java.util.stream.BaseStream
    public /* synthetic */ IntStream sequential() {
        return convert(this.a.sequential());
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ IntStream skip(long j) {
        return convert(this.a.skip(j));
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ IntStream sorted() {
        return convert(this.a.sorted());
    }

    @Override // java.util.stream.IntStream, java.util.stream.BaseStream
    public /* synthetic */ Spliterator<Integer> spliterator() {
        return l.a(this.a.spliterator());
    }

    @Override // java.util.stream.IntStream, java.util.stream.BaseStream
    public /* synthetic */ Spliterator<Integer> spliterator() {
        return h.a(this.a.spliterator());
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ int sum() {
        return this.a.sum();
    }

    @Override // java.util.stream.IntStream
    public IntSummaryStatistics summaryStatistics() {
        this.a.summaryStatistics();
        throw new Error("Java 8+ API desugaring (library desugaring) cannot convert to java.util.IntSummaryStatistics");
    }

    @Override // java.util.stream.IntStream
    public /* synthetic */ int[] toArray() {
        return this.a.toArray();
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [java.util.stream.IntStream, java.util.stream.BaseStream] */
    @Override // java.util.stream.BaseStream
    public /* synthetic */ IntStream unordered() {
        return H0.i0(this.a.unordered());
    }
}
