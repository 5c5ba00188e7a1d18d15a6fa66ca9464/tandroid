package j$.wrappers;

import j$.util.function.BiConsumer;
import j$.util.stream.IntStream;
import j$.util.stream.Stream;
import j$.util.stream.e1;
import java.util.Iterator;
import java.util.stream.LongStream;
/* loaded from: classes2.dex */
public final /* synthetic */ class M0 implements e1 {
    final /* synthetic */ LongStream a;

    private /* synthetic */ M0(LongStream longStream) {
        this.a = longStream;
    }

    public static /* synthetic */ e1 n0(LongStream longStream) {
        if (longStream == null) {
            return null;
        }
        return longStream instanceof N0 ? ((N0) longStream).a : new M0(longStream);
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ long D(long j, j$.util.function.o oVar) {
        return this.a.reduce(j, d0.a(oVar));
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ boolean L(i0 i0Var) {
        return this.a.allMatch(j0.a(i0Var));
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ j$.util.stream.U O(k0 k0Var) {
        return K0.n0(this.a.mapToDouble(k0Var == null ? null : k0Var.a));
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ Stream Q(j$.util.function.r rVar) {
        return $r8$wrapper$java$util$stream$Stream$-V-WRP.convert(this.a.mapToObj(h0.a(rVar)));
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ boolean S(i0 i0Var) {
        return this.a.noneMatch(j0.a(i0Var));
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ void Z(j$.util.function.q qVar) {
        this.a.forEachOrdered(f0.a(qVar));
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ j$.util.stream.U asDoubleStream() {
        return K0.n0(this.a.asDoubleStream());
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ j$.util.j average() {
        return j$.util.a.q(this.a.average());
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ Stream boxed() {
        return $r8$wrapper$java$util$stream$Stream$-V-WRP.convert(this.a.boxed());
    }

    @Override // j$.util.stream.g, java.lang.AutoCloseable
    public /* synthetic */ void close() {
        this.a.close();
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ long count() {
        return this.a.count();
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ void d(j$.util.function.q qVar) {
        this.a.forEach(f0.a(qVar));
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ e1 distinct() {
        return n0(this.a.distinct());
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ IntStream e0(m0 m0Var) {
        return $r8$wrapper$java$util$stream$IntStream$-V-WRP.convert(this.a.mapToInt(m0Var == null ? null : m0Var.a));
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ Object f0(j$.util.function.y yVar, j$.util.function.w wVar, BiConsumer biConsumer) {
        return this.a.collect(z0.a(yVar), v0.a(wVar), r.a(biConsumer));
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ j$.util.l findAny() {
        return j$.util.a.s(this.a.findAny());
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ j$.util.l findFirst() {
        return j$.util.a.s(this.a.findFirst());
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ j$.util.l g(j$.util.function.o oVar) {
        return j$.util.a.s(this.a.reduce(d0.a(oVar)));
    }

    @Override // j$.util.stream.g
    public /* synthetic */ boolean isParallel() {
        return this.a.isParallel();
    }

    @Override // j$.util.stream.e1, j$.util.stream.g
    public /* synthetic */ j$.util.r iterator() {
        return e.a(this.a.iterator());
    }

    @Override // j$.util.stream.e1, j$.util.stream.g
    public /* synthetic */ Iterator iterator() {
        return this.a.iterator();
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ boolean k(i0 i0Var) {
        return this.a.anyMatch(j0.a(i0Var));
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ e1 limit(long j) {
        return n0(this.a.limit(j));
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ j$.util.l max() {
        return j$.util.a.s(this.a.max());
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ j$.util.l min() {
        return j$.util.a.s(this.a.min());
    }

    @Override // j$.util.stream.g
    public /* synthetic */ j$.util.stream.g onClose(Runnable runnable) {
        return G0.n0(this.a.onClose(runnable));
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ e1 p(j$.util.function.q qVar) {
        return n0(this.a.peek(f0.a(qVar)));
    }

    @Override // j$.util.stream.e1, j$.util.stream.g, j$.util.stream.IntStream
    public /* synthetic */ e1 parallel() {
        return n0(this.a.parallel());
    }

    @Override // j$.util.stream.e1, j$.util.stream.g, j$.util.stream.IntStream
    public /* synthetic */ j$.util.stream.g parallel() {
        return G0.n0(this.a.parallel());
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ e1 s(j$.util.function.r rVar) {
        return n0(this.a.flatMap(h0.a(rVar)));
    }

    @Override // j$.util.stream.e1, j$.util.stream.g, j$.util.stream.IntStream
    public /* synthetic */ e1 sequential() {
        return n0(this.a.sequential());
    }

    @Override // j$.util.stream.e1, j$.util.stream.g, j$.util.stream.IntStream
    public /* synthetic */ j$.util.stream.g sequential() {
        return G0.n0(this.a.sequential());
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ e1 skip(long j) {
        return n0(this.a.skip(j));
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ e1 sorted() {
        return n0(this.a.sorted());
    }

    @Override // j$.util.stream.e1, j$.util.stream.g
    public /* synthetic */ j$.util.u spliterator() {
        return g.a(this.a.spliterator());
    }

    @Override // j$.util.stream.e1, j$.util.stream.g
    public /* synthetic */ j$.util.v spliterator() {
        return m.a(this.a.spliterator());
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ long sum() {
        return this.a.sum();
    }

    @Override // j$.util.stream.e1
    public j$.util.i summaryStatistics() {
        this.a.summaryStatistics();
        throw new Error("Java 8+ API desugaring (library desugaring) cannot convert from java.util.LongSummaryStatistics");
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ long[] toArray() {
        return this.a.toArray();
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ e1 u(i0 i0Var) {
        return n0(this.a.filter(j0.a(i0Var)));
    }

    @Override // j$.util.stream.g
    public /* synthetic */ j$.util.stream.g unordered() {
        return G0.n0(this.a.unordered());
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ e1 z(j$.util.function.t tVar) {
        return n0(this.a.map(p0.a(tVar)));
    }
}
