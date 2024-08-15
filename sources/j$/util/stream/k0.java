package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.LongFunction;
import j$.util.function.Supplier;
import j$.util.stream.IntStream;
import j$.util.stream.Stream;
import java.util.Iterator;
/* loaded from: classes2.dex */
public final /* synthetic */ class k0 implements LongStream {
    public final /* synthetic */ java.util.stream.LongStream a;

    private /* synthetic */ k0(java.util.stream.LongStream longStream) {
        this.a = longStream;
    }

    public static /* synthetic */ LongStream i0(java.util.stream.LongStream longStream) {
        if (longStream == null) {
            return null;
        }
        return longStream instanceof l0 ? ((l0) longStream).a : new k0(longStream);
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ void E(j$.util.function.h0 h0Var) {
        this.a.forEach(j$.util.function.g0.a(h0Var));
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ F J(j$.util.function.p0 p0Var) {
        return D.i0(this.a.mapToDouble(j$.util.function.o0.a(p0Var)));
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ LongStream M(j$.util.function.w0 w0Var) {
        return i0(this.a.map(j$.util.function.v0.a(w0Var)));
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ IntStream T(j$.util.function.s0 s0Var) {
        return IntStream.VivifiedWrapper.convert(this.a.mapToInt(j$.util.function.r0.a(s0Var)));
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ boolean a(j$.util.function.m0 m0Var) {
        return this.a.noneMatch(j$.util.function.l0.a(m0Var));
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ F asDoubleStream() {
        return D.i0(this.a.asDoubleStream());
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ j$.util.l average() {
        return j$.util.k.b(this.a.average());
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ Stream boxed() {
        return Stream.VivifiedWrapper.convert(this.a.boxed());
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ boolean c0(j$.util.function.m0 m0Var) {
        return this.a.anyMatch(j$.util.function.l0.a(m0Var));
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
    public final /* synthetic */ LongStream distinct() {
        return i0(this.a.distinct());
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ j$.util.n e(j$.util.function.d0 d0Var) {
        return j$.util.k.d(this.a.reduce(j$.util.function.c0.a(d0Var)));
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ LongStream e0(j$.util.function.m0 m0Var) {
        return i0(this.a.filter(j$.util.function.l0.a(m0Var)));
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof k0) {
            obj = ((k0) obj).a;
        }
        return this.a.equals(obj);
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ j$.util.n findAny() {
        return j$.util.k.d(this.a.findAny());
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ j$.util.n findFirst() {
        return j$.util.k.d(this.a.findFirst());
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ LongStream g(j$.util.function.h0 h0Var) {
        return i0(this.a.peek(j$.util.function.g0.a(h0Var)));
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ LongStream h(LongFunction longFunction) {
        return i0(this.a.flatMap(j$.util.function.j0.a(longFunction)));
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }

    @Override // j$.util.stream.BaseStream
    public final /* synthetic */ boolean isParallel() {
        return this.a.isParallel();
    }

    @Override // j$.util.stream.LongStream, j$.util.stream.BaseStream, j$.util.stream.F
    public final /* synthetic */ j$.util.z iterator() {
        return j$.util.x.a(this.a.iterator());
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    public final /* synthetic */ Iterator iterator() {
        return this.a.iterator();
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ LongStream limit(long j) {
        return i0(this.a.limit(j));
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ Stream mapToObj(LongFunction longFunction) {
        return Stream.VivifiedWrapper.convert(this.a.mapToObj(j$.util.function.j0.a(longFunction)));
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ j$.util.n max() {
        return j$.util.k.d(this.a.max());
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ j$.util.n min() {
        return j$.util.k.d(this.a.min());
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ long n(long j, j$.util.function.d0 d0Var) {
        return this.a.reduce(j, j$.util.function.c0.a(d0Var));
    }

    @Override // j$.util.stream.BaseStream
    public final /* synthetic */ BaseStream onClose(Runnable runnable) {
        return g.i0(this.a.onClose(runnable));
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    public final /* synthetic */ BaseStream parallel() {
        return g.i0(this.a.parallel());
    }

    @Override // j$.util.stream.LongStream, j$.util.stream.BaseStream, j$.util.stream.F
    public final /* synthetic */ LongStream parallel() {
        return i0(this.a.parallel());
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    public final /* synthetic */ BaseStream sequential() {
        return g.i0(this.a.sequential());
    }

    @Override // j$.util.stream.LongStream, j$.util.stream.BaseStream, j$.util.stream.F
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

    @Override // j$.util.stream.LongStream, j$.util.stream.BaseStream, j$.util.stream.F
    public final /* synthetic */ j$.util.K spliterator() {
        return j$.util.I.f(this.a.spliterator());
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    public final /* synthetic */ j$.util.Q spliterator() {
        return j$.util.O.f(this.a.spliterator());
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ long sum() {
        return this.a.sum();
    }

    @Override // j$.util.stream.LongStream
    public final j$.util.j summaryStatistics() {
        this.a.summaryStatistics();
        throw new Error("Java 8+ API desugaring (library desugaring) cannot convert from java.util.LongSummaryStatistics");
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ long[] toArray() {
        return this.a.toArray();
    }

    @Override // j$.util.stream.BaseStream
    public final /* synthetic */ BaseStream unordered() {
        return g.i0(this.a.unordered());
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ void x(j$.util.function.h0 h0Var) {
        this.a.forEachOrdered(j$.util.function.g0.a(h0Var));
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ Object y(Supplier supplier, j$.util.function.F0 f0, BiConsumer biConsumer) {
        return this.a.collect(j$.util.function.L0.a(supplier), j$.util.function.E0.a(f0), j$.util.function.a.a(biConsumer));
    }

    @Override // j$.util.stream.LongStream
    public final /* synthetic */ boolean z(j$.util.function.m0 m0Var) {
        return this.a.allMatch(j$.util.function.l0.a(m0Var));
    }
}
