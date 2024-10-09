package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.LongFunction;
import j$.util.function.Supplier;
import j$.util.stream.IntStream;
import j$.util.stream.Stream;
import java.util.Iterator;

/* loaded from: classes2.dex */
public final /* synthetic */ class i0 implements LongStream {
    public final /* synthetic */ java.util.stream.LongStream a;

    private /* synthetic */ i0(java.util.stream.LongStream longStream) {
        this.a = longStream;
    }

    public static /* synthetic */ LongStream i0(java.util.stream.LongStream longStream) {
        if (longStream == null) {
            return null;
        }
        return longStream instanceof j0 ? ((j0) longStream).a : new i0(longStream);
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ IntStream A(j$.util.function.b0 b0Var) {
        return IntStream.VivifiedWrapper.convert(this.a.mapToInt(b0Var == null ? null : b0Var.a));
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ boolean E(j$.util.function.Z z) {
        return this.a.anyMatch(z == null ? null : z.a);
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ boolean G(j$.util.function.Z z) {
        return this.a.noneMatch(z == null ? null : z.a);
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ LongStream M(j$.util.function.Z z) {
        return i0(this.a.filter(z == null ? null : z.a));
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ void V(j$.util.function.W w) {
        this.a.forEachOrdered(j$.util.function.V.a(w));
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ Object Z(Supplier supplier, j$.util.function.o0 o0Var, BiConsumer biConsumer) {
        return this.a.collect(j$.util.function.u0.a(supplier), j$.util.function.n0.a(o0Var), j$.util.function.a.a(biConsumer));
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ D asDoubleStream() {
        return B.i0(this.a.asDoubleStream());
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ j$.util.l average() {
        return j$.util.a.t(this.a.average());
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ Stream boxed() {
        return Stream.VivifiedWrapper.convert(this.a.boxed());
    }

    @Override // j$.util.stream.BaseStream, java.lang.AutoCloseable
    public final /* synthetic */ void close() {
        this.a.close();
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ long count() {
        return this.a.count();
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ void d(j$.util.function.W w) {
        this.a.forEach(j$.util.function.V.a(w));
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ LongStream distinct() {
        return i0(this.a.distinct());
    }

    public final /* synthetic */ boolean equals(Object obj) {
        java.util.stream.LongStream longStream = this.a;
        if (obj instanceof i0) {
            obj = ((i0) obj).a;
        }
        return longStream.equals(obj);
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ j$.util.n findAny() {
        return j$.util.a.v(this.a.findAny());
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ j$.util.n findFirst() {
        return j$.util.a.v(this.a.findFirst());
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ j$.util.n h(j$.util.function.S s) {
        return j$.util.a.v(this.a.reduce(j$.util.function.Q.a(s)));
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }

    @Override // j$.util.stream.BaseStream
    public final /* synthetic */ boolean isParallel() {
        return this.a.isParallel();
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.util.PrimitiveIterator$OfLong] */
    @Override // j$.util.stream.LongStream, j$.util.stream.BaseStream, j$.util.stream.D
    public final /* synthetic */ j$.util.z iterator() {
        return j$.util.x.b(this.a.iterator());
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.D
    public final /* synthetic */ Iterator iterator() {
        return this.a.iterator();
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ LongStream limit(long j) {
        return i0(this.a.limit(j));
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ Stream mapToObj(LongFunction longFunction) {
        return Stream.VivifiedWrapper.convert(this.a.mapToObj(j$.util.function.Y.a(longFunction)));
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ j$.util.n max() {
        return j$.util.a.v(this.a.max());
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ j$.util.n min() {
        return j$.util.a.v(this.a.min());
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ LongStream o(j$.util.function.W w) {
        return i0(this.a.peek(j$.util.function.V.a(w)));
    }

    @Override // j$.util.stream.BaseStream
    public final /* synthetic */ BaseStream onClose(Runnable runnable) {
        return f.i0(this.a.onClose(runnable));
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ LongStream p(LongFunction longFunction) {
        return i0(this.a.flatMap(j$.util.function.Y.a(longFunction)));
    }

    @Override // j$.util.stream.BaseStream
    public final /* synthetic */ BaseStream parallel() {
        return f.i0(this.a.parallel());
    }

    @Override // j$.util.stream.LongStream, j$.util.stream.BaseStream
    public final /* synthetic */ LongStream parallel() {
        return i0(this.a.parallel());
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ D r(j$.util.function.a0 a0Var) {
        return B.i0(this.a.mapToDouble(a0Var == null ? null : a0Var.a));
    }

    @Override // j$.util.stream.BaseStream
    public final /* synthetic */ BaseStream sequential() {
        return f.i0(this.a.sequential());
    }

    @Override // j$.util.stream.LongStream, j$.util.stream.BaseStream
    public final /* synthetic */ LongStream sequential() {
        return i0(this.a.sequential());
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ LongStream skip(long j) {
        return i0(this.a.skip(j));
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ LongStream sorted() {
        return i0(this.a.sorted());
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.util.Spliterator$OfLong] */
    @Override // j$.util.stream.LongStream, j$.util.stream.BaseStream, j$.util.stream.D
    public final /* synthetic */ j$.util.K spliterator() {
        return j$.util.I.b(this.a.spliterator());
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.D
    public final /* synthetic */ j$.util.Q spliterator() {
        return j$.util.O.b(this.a.spliterator());
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ long sum() {
        return this.a.sum();
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.k summaryStatistics() {
        this.a.summaryStatistics();
        throw new Error("Java 8+ API desugaring (library desugaring) cannot convert from java.util.LongSummaryStatistics");
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ long[] toArray() {
        return this.a.toArray();
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ boolean u(j$.util.function.Z z) {
        return this.a.allMatch(z == null ? null : z.a);
    }

    @Override // j$.util.stream.BaseStream
    public final /* synthetic */ BaseStream unordered() {
        return f.i0(this.a.unordered());
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ LongStream v(j$.util.function.f0 f0Var) {
        return i0(this.a.map(j$.util.function.e0.a(f0Var)));
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ long x(long j, j$.util.function.S s) {
        return this.a.reduce(j, j$.util.function.Q.a(s));
    }
}
