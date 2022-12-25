package j$.wrappers;

import j$.util.function.BiConsumer;
import j$.util.stream.IntStream;
import j$.util.stream.Stream;
import j$.util.stream.e1;
import java.util.Iterator;
import java.util.stream.DoubleStream;
/* loaded from: classes2.dex */
public final /* synthetic */ class K0 implements j$.util.stream.U {
    final /* synthetic */ DoubleStream a;

    private /* synthetic */ K0(DoubleStream doubleStream) {
        this.a = doubleStream;
    }

    public static /* synthetic */ j$.util.stream.U n0(DoubleStream doubleStream) {
        if (doubleStream == null) {
            return null;
        }
        return doubleStream instanceof L0 ? ((L0) doubleStream).a : new K0(doubleStream);
    }

    @Override // j$.util.stream.U
    public /* synthetic */ j$.util.j G(j$.util.function.d dVar) {
        return j$.util.a.q(this.a.reduce(y.a(dVar)));
    }

    @Override // j$.util.stream.U
    public /* synthetic */ Object H(j$.util.function.y yVar, j$.util.function.u uVar, BiConsumer biConsumer) {
        return this.a.collect(z0.a(yVar), r0.a(uVar), r.a(biConsumer));
    }

    @Override // j$.util.stream.U
    public /* synthetic */ double K(double d, j$.util.function.d dVar) {
        return this.a.reduce(d, y.a(dVar));
    }

    @Override // j$.util.stream.U
    public /* synthetic */ Stream M(j$.util.function.g gVar) {
        return $r8$wrapper$java$util$stream$Stream$-V-WRP.convert(this.a.mapToObj(C.a(gVar)));
    }

    @Override // j$.util.stream.U
    public /* synthetic */ IntStream R(F f) {
        return $r8$wrapper$java$util$stream$IntStream$-V-WRP.convert(this.a.mapToInt(f == null ? null : f.a));
    }

    @Override // j$.util.stream.U
    public /* synthetic */ boolean Y(D d) {
        return this.a.allMatch(E.a(d));
    }

    @Override // j$.util.stream.U
    public /* synthetic */ j$.util.j average() {
        return j$.util.a.q(this.a.average());
    }

    @Override // j$.util.stream.U
    public /* synthetic */ j$.util.stream.U b(j$.util.function.f fVar) {
        return n0(this.a.peek(A.a(fVar)));
    }

    @Override // j$.util.stream.U
    public /* synthetic */ Stream boxed() {
        return $r8$wrapper$java$util$stream$Stream$-V-WRP.convert(this.a.boxed());
    }

    @Override // j$.util.stream.g, java.lang.AutoCloseable
    public /* synthetic */ void close() {
        this.a.close();
    }

    @Override // j$.util.stream.U
    public /* synthetic */ long count() {
        return this.a.count();
    }

    @Override // j$.util.stream.U
    public /* synthetic */ j$.util.stream.U distinct() {
        return n0(this.a.distinct());
    }

    @Override // j$.util.stream.U
    public /* synthetic */ j$.util.j findAny() {
        return j$.util.a.q(this.a.findAny());
    }

    @Override // j$.util.stream.U
    public /* synthetic */ j$.util.j findFirst() {
        return j$.util.a.q(this.a.findFirst());
    }

    @Override // j$.util.stream.U
    public /* synthetic */ boolean h0(D d) {
        return this.a.anyMatch(E.a(d));
    }

    @Override // j$.util.stream.U
    public /* synthetic */ boolean i0(D d) {
        return this.a.noneMatch(E.a(d));
    }

    @Override // j$.util.stream.g
    public /* synthetic */ boolean isParallel() {
        return this.a.isParallel();
    }

    @Override // j$.util.stream.U, j$.util.stream.g
    public /* synthetic */ j$.util.n iterator() {
        return a.a(this.a.iterator());
    }

    @Override // j$.util.stream.U, j$.util.stream.g
    public /* synthetic */ Iterator iterator() {
        return this.a.iterator();
    }

    @Override // j$.util.stream.U
    public /* synthetic */ void j(j$.util.function.f fVar) {
        this.a.forEach(A.a(fVar));
    }

    @Override // j$.util.stream.U
    public /* synthetic */ void l0(j$.util.function.f fVar) {
        this.a.forEachOrdered(A.a(fVar));
    }

    @Override // j$.util.stream.U
    public /* synthetic */ j$.util.stream.U limit(long j) {
        return n0(this.a.limit(j));
    }

    @Override // j$.util.stream.U
    public /* synthetic */ j$.util.j max() {
        return j$.util.a.q(this.a.max());
    }

    @Override // j$.util.stream.U
    public /* synthetic */ j$.util.j min() {
        return j$.util.a.q(this.a.min());
    }

    @Override // j$.util.stream.g
    public /* synthetic */ j$.util.stream.g onClose(Runnable runnable) {
        return G0.n0(this.a.onClose(runnable));
    }

    @Override // j$.util.stream.U, j$.util.stream.g, j$.util.stream.IntStream
    public /* synthetic */ j$.util.stream.U parallel() {
        return n0(this.a.parallel());
    }

    @Override // j$.util.stream.U, j$.util.stream.g, j$.util.stream.IntStream
    public /* synthetic */ j$.util.stream.g parallel() {
        return G0.n0(this.a.parallel());
    }

    @Override // j$.util.stream.U
    public /* synthetic */ j$.util.stream.U r(D d) {
        return n0(this.a.filter(E.a(d)));
    }

    @Override // j$.util.stream.U, j$.util.stream.g, j$.util.stream.IntStream
    public /* synthetic */ j$.util.stream.U sequential() {
        return n0(this.a.sequential());
    }

    @Override // j$.util.stream.U, j$.util.stream.g, j$.util.stream.IntStream
    public /* synthetic */ j$.util.stream.g sequential() {
        return G0.n0(this.a.sequential());
    }

    @Override // j$.util.stream.U
    public /* synthetic */ j$.util.stream.U skip(long j) {
        return n0(this.a.skip(j));
    }

    @Override // j$.util.stream.U
    public /* synthetic */ j$.util.stream.U sorted() {
        return n0(this.a.sorted());
    }

    @Override // j$.util.stream.U, j$.util.stream.g
    public /* synthetic */ j$.util.t spliterator() {
        return i.a(this.a.spliterator());
    }

    @Override // j$.util.stream.U, j$.util.stream.g
    public /* synthetic */ j$.util.u spliterator() {
        return g.a(this.a.spliterator());
    }

    @Override // j$.util.stream.U
    public /* synthetic */ double sum() {
        return this.a.sum();
    }

    @Override // j$.util.stream.U
    public j$.util.g summaryStatistics() {
        this.a.summaryStatistics();
        throw new Error("Java 8+ API desugaring (library desugaring) cannot convert from java.util.DoubleSummaryStatistics");
    }

    @Override // j$.util.stream.U
    public /* synthetic */ double[] toArray() {
        return this.a.toArray();
    }

    @Override // j$.util.stream.g
    public /* synthetic */ j$.util.stream.g unordered() {
        return G0.n0(this.a.unordered());
    }

    @Override // j$.util.stream.U
    public /* synthetic */ j$.util.stream.U w(j$.util.function.g gVar) {
        return n0(this.a.flatMap(C.a(gVar)));
    }

    @Override // j$.util.stream.U
    public /* synthetic */ e1 x(j$.util.function.h hVar) {
        return M0.n0(this.a.mapToLong(I.a(hVar)));
    }

    @Override // j$.util.stream.U
    public /* synthetic */ j$.util.stream.U y(J j) {
        return n0(this.a.map(K.a(j)));
    }
}
