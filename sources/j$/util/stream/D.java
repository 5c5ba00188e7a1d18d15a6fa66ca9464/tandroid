package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Supplier;
import j$.util.stream.IntStream;
import j$.util.stream.Stream;
import java.util.Iterator;
import java.util.stream.DoubleStream;
/* loaded from: classes2.dex */
public final /* synthetic */ class D implements F {
    public final /* synthetic */ DoubleStream a;

    private /* synthetic */ D(DoubleStream doubleStream) {
        this.a = doubleStream;
    }

    public static /* synthetic */ F i0(DoubleStream doubleStream) {
        if (doubleStream == null) {
            return null;
        }
        return doubleStream instanceof E ? ((E) doubleStream).a : new D(doubleStream);
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ IntStream B(j$.util.function.v vVar) {
        return IntStream.VivifiedWrapper.convert(this.a.mapToInt(j$.util.function.u.a(vVar)));
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ void H(j$.util.function.m mVar) {
        this.a.forEach(j$.util.function.l.a(mVar));
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ j$.util.l N(j$.util.function.i iVar) {
        return j$.util.k.b(this.a.reduce(j$.util.function.h.a(iVar)));
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ double Q(double d, j$.util.function.i iVar) {
        return this.a.reduce(d, j$.util.function.h.a(iVar));
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ boolean R(j$.util.function.s sVar) {
        return this.a.noneMatch(j$.util.function.r.a(sVar));
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ boolean U(j$.util.function.s sVar) {
        return this.a.allMatch(j$.util.function.r.a(sVar));
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ j$.util.l average() {
        return j$.util.k.b(this.a.average());
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ F b(j$.util.function.m mVar) {
        return i0(this.a.peek(j$.util.function.l.a(mVar)));
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ Stream boxed() {
        return Stream.VivifiedWrapper.convert(this.a.boxed());
    }

    @Override // j$.util.stream.BaseStream, java.lang.AutoCloseable
    public final /* synthetic */ void close() {
        this.a.close();
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ long count() {
        return this.a.count();
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ F distinct() {
        return i0(this.a.distinct());
    }

    public final /* synthetic */ boolean equals(Object obj) {
        if (obj instanceof D) {
            obj = ((D) obj).a;
        }
        return this.a.equals(obj);
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ j$.util.l findAny() {
        return j$.util.k.b(this.a.findAny());
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ j$.util.l findFirst() {
        return j$.util.k.b(this.a.findFirst());
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ void g0(j$.util.function.m mVar) {
        this.a.forEachOrdered(j$.util.function.l.a(mVar));
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ F i(j$.util.function.s sVar) {
        return i0(this.a.filter(j$.util.function.r.a(sVar)));
    }

    @Override // j$.util.stream.BaseStream
    public final /* synthetic */ boolean isParallel() {
        return this.a.isParallel();
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ j$.util.r iterator() {
        return j$.util.p.a(this.a.iterator());
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    public final /* synthetic */ Iterator iterator() {
        return this.a.iterator();
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ F j(j$.util.function.p pVar) {
        return i0(this.a.flatMap(j$.util.function.o.a(pVar)));
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ LongStream k(j$.util.function.y yVar) {
        return k0.i0(this.a.mapToLong(j$.util.function.x.a(yVar)));
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ F limit(long j) {
        return i0(this.a.limit(j));
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ j$.util.l max() {
        return j$.util.k.b(this.a.max());
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ j$.util.l min() {
        return j$.util.k.b(this.a.min());
    }

    @Override // j$.util.stream.BaseStream
    public final /* synthetic */ BaseStream onClose(Runnable runnable) {
        return g.i0(this.a.onClose(runnable));
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ Object p(Supplier supplier, j$.util.function.z0 z0Var, BiConsumer biConsumer) {
        return this.a.collect(j$.util.function.L0.a(supplier), j$.util.function.y0.a(z0Var), j$.util.function.a.a(biConsumer));
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    public final /* synthetic */ BaseStream parallel() {
        return g.i0(this.a.parallel());
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ F parallel() {
        return i0(this.a.parallel());
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ F q(j$.util.function.B b) {
        return i0(this.a.map(j$.util.function.A.a(b)));
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ Stream r(j$.util.function.p pVar) {
        return Stream.VivifiedWrapper.convert(this.a.mapToObj(j$.util.function.o.a(pVar)));
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    public final /* synthetic */ BaseStream sequential() {
        return g.i0(this.a.sequential());
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ F sequential() {
        return i0(this.a.sequential());
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ F skip(long j) {
        return i0(this.a.skip(j));
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ F sorted() {
        return i0(this.a.sorted());
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ j$.util.E spliterator() {
        return j$.util.C.f(this.a.spliterator());
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    public final /* synthetic */ j$.util.Q spliterator() {
        return j$.util.O.f(this.a.spliterator());
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ double sum() {
        return this.a.sum();
    }

    @Override // j$.util.stream.F
    public final j$.util.h summaryStatistics() {
        this.a.summaryStatistics();
        throw new Error("Java 8+ API desugaring (library desugaring) cannot convert from java.util.DoubleSummaryStatistics");
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ double[] toArray() {
        return this.a.toArray();
    }

    @Override // j$.util.stream.BaseStream
    public final /* synthetic */ BaseStream unordered() {
        return g.i0(this.a.unordered());
    }

    @Override // j$.util.stream.F
    public final /* synthetic */ boolean w(j$.util.function.s sVar) {
        return this.a.anyMatch(j$.util.function.r.a(sVar));
    }
}
