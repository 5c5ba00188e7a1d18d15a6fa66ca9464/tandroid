package j$.wrappers;

import j$.util.function.BiConsumer;
import j$.util.stream.IntStream;
import j$.util.stream.Stream;
import j$.util.stream.e1;
import java.util.Iterator;
import java.util.stream.LongStream;
/* loaded from: classes2.dex */
public final /* synthetic */ class N0 implements e1 {
    final /* synthetic */ LongStream a;

    private /* synthetic */ N0(LongStream longStream) {
        this.a = longStream;
    }

    public static /* synthetic */ e1 n0(LongStream longStream) {
        if (longStream == null) {
            return null;
        }
        return longStream instanceof O0 ? ((O0) longStream).a : new N0(longStream);
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ long D(long j, j$.util.function.o oVar) {
        return this.a.reduce(j, e0.a(oVar));
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ boolean L(j0 j0Var) {
        return this.a.allMatch(k0.a(j0Var));
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ j$.util.stream.U O(l0 l0Var) {
        return L0.n0(this.a.mapToDouble(l0Var == null ? null : l0Var.a));
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ Stream Q(j$.util.function.r rVar) {
        return C$r8$wrapper$java$util$stream$Stream$VWRP.convert(this.a.mapToObj(i0.a(rVar)));
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ boolean S(j0 j0Var) {
        return this.a.noneMatch(k0.a(j0Var));
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ void Z(j$.util.function.q qVar) {
        this.a.forEachOrdered(g0.a(qVar));
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ j$.util.stream.U asDoubleStream() {
        return L0.n0(this.a.asDoubleStream());
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ j$.util.j average() {
        return j$.util.a.q(this.a.average());
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ Stream boxed() {
        return C$r8$wrapper$java$util$stream$Stream$VWRP.convert(this.a.boxed());
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
        this.a.forEach(g0.a(qVar));
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ e1 distinct() {
        return n0(this.a.distinct());
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ IntStream e0(n0 n0Var) {
        return C$r8$wrapper$java$util$stream$IntStream$VWRP.convert(this.a.mapToInt(n0Var == null ? null : n0Var.a));
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ Object f0(j$.util.function.y yVar, j$.util.function.w wVar, BiConsumer biConsumer) {
        return this.a.collect(A0.a(yVar), w0.a(wVar), r.a(biConsumer));
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
        return j$.util.a.s(this.a.reduce(e0.a(oVar)));
    }

    @Override // j$.util.stream.g
    public /* synthetic */ boolean isParallel() {
        return this.a.isParallel();
    }

    @Override // j$.util.stream.e1, j$.util.stream.g
    /* renamed from: iterator */
    public /* synthetic */ j$.util.r mo331iterator() {
        return e.a(this.a.iterator());
    }

    @Override // j$.util.stream.e1, j$.util.stream.g
    /* renamed from: iterator */
    public /* synthetic */ Iterator mo331iterator() {
        return this.a.iterator();
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ boolean k(j0 j0Var) {
        return this.a.anyMatch(k0.a(j0Var));
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
        return H0.n0(this.a.onClose(runnable));
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ e1 p(j$.util.function.q qVar) {
        return n0(this.a.peek(g0.a(qVar)));
    }

    @Override // j$.util.stream.e1, j$.util.stream.g, j$.util.stream.IntStream
    /* renamed from: parallel */
    public /* synthetic */ e1 mo332parallel() {
        return n0(this.a.parallel());
    }

    @Override // j$.util.stream.e1, j$.util.stream.g, j$.util.stream.IntStream
    /* renamed from: parallel */
    public /* synthetic */ j$.util.stream.g mo332parallel() {
        return H0.n0(this.a.parallel());
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ e1 s(j$.util.function.r rVar) {
        return n0(this.a.flatMap(i0.a(rVar)));
    }

    @Override // j$.util.stream.e1, j$.util.stream.g, j$.util.stream.IntStream
    /* renamed from: sequential */
    public /* synthetic */ e1 mo333sequential() {
        return n0(this.a.sequential());
    }

    @Override // j$.util.stream.e1, j$.util.stream.g, j$.util.stream.IntStream
    /* renamed from: sequential */
    public /* synthetic */ j$.util.stream.g mo333sequential() {
        return H0.n0(this.a.sequential());
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
    /* renamed from: spliterator */
    public /* synthetic */ j$.util.u mo334spliterator() {
        return g.a(this.a.spliterator());
    }

    @Override // j$.util.stream.e1, j$.util.stream.g
    /* renamed from: spliterator */
    public /* synthetic */ j$.util.v mo334spliterator() {
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
    public /* synthetic */ e1 u(j0 j0Var) {
        return n0(this.a.filter(k0.a(j0Var)));
    }

    @Override // j$.util.stream.g
    public /* synthetic */ j$.util.stream.g unordered() {
        return H0.n0(this.a.unordered());
    }

    @Override // j$.util.stream.e1
    public /* synthetic */ e1 z(j$.util.function.t tVar) {
        return n0(this.a.map(q0.a(tVar)));
    }
}
