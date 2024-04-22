package j$.wrappers;

import j$.util.function.BiConsumer;
import j$.util.function.LongFunction;
import j$.util.function.Supplier;
import j$.util.p;
import j$.util.s;
import j$.util.stream.IntStream;
import j$.util.stream.LongStream;
import j$.util.stream.Stream;
import java.util.Iterator;
/* loaded from: classes2.dex */
public final /* synthetic */ class M0 implements LongStream {
    final /* synthetic */ java.util.stream.LongStream a;

    private /* synthetic */ M0(java.util.stream.LongStream longStream) {
        this.a = longStream;
    }

    public static /* synthetic */ LongStream i0(java.util.stream.LongStream longStream) {
        if (longStream == null) {
            return null;
        }
        return longStream instanceof N0 ? ((N0) longStream).a : new M0(longStream);
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ long C(long j, j$.util.function.o oVar) {
        return this.a.reduce(j, d0.a(oVar));
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ boolean K(i0 i0Var) {
        return this.a.allMatch(j0.a(i0Var));
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ j$.util.stream.V N(k0 k0Var) {
        return K0.i0(this.a.mapToDouble(k0Var == null ? null : k0Var.a));
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ boolean Q(i0 i0Var) {
        return this.a.noneMatch(j0.a(i0Var));
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ void W(j$.util.function.q qVar) {
        this.a.forEachOrdered(f0.a(qVar));
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ IntStream a0(m0 m0Var) {
        return $r8$wrapper$java$util$stream$IntStream$-V-WRP.convert(this.a.mapToInt(m0Var == null ? null : m0Var.a));
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ j$.util.stream.V asDoubleStream() {
        return K0.i0(this.a.asDoubleStream());
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ j$.util.j average() {
        return j$.util.a.n(this.a.average());
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ Object b0(Supplier supplier, j$.util.function.v vVar, BiConsumer biConsumer) {
        return this.a.collect(z0.a(supplier), v0.a(vVar), r.a(biConsumer));
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ Stream boxed() {
        return $r8$wrapper$java$util$stream$Stream$-V-WRP.convert(this.a.boxed());
    }

    @Override // j$.util.stream.g, java.lang.AutoCloseable
    public /* synthetic */ void close() {
        this.a.close();
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ long count() {
        return this.a.count();
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ void d(j$.util.function.q qVar) {
        this.a.forEach(f0.a(qVar));
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ LongStream distinct() {
        return i0(this.a.distinct());
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ j$.util.l findAny() {
        return j$.util.a.p(this.a.findAny());
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ j$.util.l findFirst() {
        return j$.util.a.p(this.a.findFirst());
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ j$.util.l g(j$.util.function.o oVar) {
        return j$.util.a.p(this.a.reduce(d0.a(oVar)));
    }

    @Override // j$.util.stream.g
    public /* synthetic */ boolean isParallel() {
        return this.a.isParallel();
    }

    @Override // j$.util.stream.LongStream, j$.util.stream.g
    public /* synthetic */ p.b iterator() {
        return e.a(this.a.iterator());
    }

    @Override // j$.util.stream.g
    public /* synthetic */ Iterator iterator() {
        return this.a.iterator();
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ boolean k(i0 i0Var) {
        return this.a.anyMatch(j0.a(i0Var));
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ LongStream limit(long j) {
        return i0(this.a.limit(j));
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ Stream mapToObj(LongFunction longFunction) {
        return $r8$wrapper$java$util$stream$Stream$-V-WRP.convert(this.a.mapToObj(h0.a(longFunction)));
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ j$.util.l max() {
        return j$.util.a.p(this.a.max());
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ j$.util.l min() {
        return j$.util.a.p(this.a.min());
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ LongStream o(j$.util.function.q qVar) {
        return i0(this.a.peek(f0.a(qVar)));
    }

    @Override // j$.util.stream.g
    public /* synthetic */ j$.util.stream.g onClose(Runnable runnable) {
        return G0.i0(this.a.onClose(runnable));
    }

    @Override // j$.util.stream.LongStream, j$.util.stream.g
    public /* synthetic */ LongStream parallel() {
        return i0(this.a.parallel());
    }

    @Override // j$.util.stream.g
    public /* synthetic */ j$.util.stream.g parallel() {
        return G0.i0(this.a.parallel());
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ LongStream r(LongFunction longFunction) {
        return i0(this.a.flatMap(h0.a(longFunction)));
    }

    @Override // j$.util.stream.LongStream, j$.util.stream.g
    public /* synthetic */ LongStream sequential() {
        return i0(this.a.sequential());
    }

    @Override // j$.util.stream.g
    public /* synthetic */ j$.util.stream.g sequential() {
        return G0.i0(this.a.sequential());
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ LongStream skip(long j) {
        return i0(this.a.skip(j));
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ LongStream sorted() {
        return i0(this.a.sorted());
    }

    @Override // j$.util.stream.LongStream, j$.util.stream.g
    public /* synthetic */ s.c spliterator() {
        return m.a(this.a.spliterator());
    }

    @Override // j$.util.stream.g
    public /* synthetic */ j$.util.s spliterator() {
        return g.a(this.a.spliterator());
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ long sum() {
        return this.a.sum();
    }

    @Override // j$.util.stream.LongStream
    public j$.util.i summaryStatistics() {
        this.a.summaryStatistics();
        throw new Error("Java 8+ API desugaring (library desugaring) cannot convert from java.util.LongSummaryStatistics");
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ LongStream t(i0 i0Var) {
        return i0(this.a.filter(j0.a(i0Var)));
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ long[] toArray() {
        return this.a.toArray();
    }

    @Override // j$.util.stream.g
    public /* synthetic */ j$.util.stream.g unordered() {
        return G0.i0(this.a.unordered());
    }

    @Override // j$.util.stream.LongStream
    public /* synthetic */ LongStream y(j$.util.function.s sVar) {
        return i0(this.a.map(p0.a(sVar)));
    }
}
