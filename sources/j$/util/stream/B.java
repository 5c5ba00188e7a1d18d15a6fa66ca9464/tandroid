package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Supplier;
import j$.util.stream.IntStream;
import j$.util.stream.Stream;
import java.util.Iterator;
import java.util.stream.DoubleStream;
/* loaded from: classes2.dex */
public final /* synthetic */ class B implements D {
    public final /* synthetic */ DoubleStream a;

    private /* synthetic */ B(DoubleStream doubleStream) {
        this.a = doubleStream;
    }

    public static /* synthetic */ D i0(DoubleStream doubleStream) {
        if (doubleStream == null) {
            return null;
        }
        return doubleStream instanceof C ? ((C) doubleStream).a : new B(doubleStream);
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ Object B(Supplier supplier, j$.util.function.i0 i0Var, BiConsumer biConsumer) {
        return this.a.collect(j$.util.function.u0.a(supplier), j$.util.function.h0.a(i0Var), j$.util.function.a.a(biConsumer));
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ double F(double d, j$.util.function.j jVar) {
        return this.a.reduce(d, j$.util.function.i.a(jVar));
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ Stream I(j$.util.function.q qVar) {
        return Stream.VivifiedWrapper.convert(this.a.mapToObj(j$.util.function.p.a(qVar)));
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ D N(j$.util.function.w wVar) {
        return i0(this.a.map(wVar == null ? null : wVar.a));
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ IntStream S(j$.util.function.s sVar) {
        return IntStream.VivifiedWrapper.convert(this.a.mapToInt(sVar == null ? null : sVar.a));
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ D U(j$.util.function.r rVar) {
        return i0(this.a.filter(rVar == null ? null : rVar.a));
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ j$.util.l average() {
        return j$.util.a.t(this.a.average());
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ D b(j$.util.function.n nVar) {
        return i0(this.a.peek(j$.util.function.m.a(nVar)));
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ Stream boxed() {
        return Stream.VivifiedWrapper.convert(this.a.boxed());
    }

    @Override // j$.util.stream.BaseStream, java.lang.AutoCloseable
    public final /* synthetic */ void close() {
        this.a.close();
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ long count() {
        return this.a.count();
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ boolean d0(j$.util.function.r rVar) {
        return this.a.anyMatch(rVar == null ? null : rVar.a);
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ D distinct() {
        return i0(this.a.distinct());
    }

    public final /* synthetic */ boolean equals(Object obj) {
        DoubleStream doubleStream = this.a;
        if (obj instanceof B) {
            obj = ((B) obj).a;
        }
        return doubleStream.equals(obj);
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ void f0(j$.util.function.n nVar) {
        this.a.forEachOrdered(j$.util.function.m.a(nVar));
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ j$.util.l findAny() {
        return j$.util.a.t(this.a.findAny());
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ j$.util.l findFirst() {
        return j$.util.a.t(this.a.findFirst());
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ boolean g0(j$.util.function.r rVar) {
        return this.a.allMatch(rVar == null ? null : rVar.a);
    }

    public final /* synthetic */ int hashCode() {
        return this.a.hashCode();
    }

    @Override // j$.util.stream.BaseStream
    public final /* synthetic */ boolean isParallel() {
        return this.a.isParallel();
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ j$.util.r iterator() {
        return j$.util.p.b(this.a.iterator());
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.D
    public final /* synthetic */ Iterator iterator() {
        return this.a.iterator();
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ void j(j$.util.function.n nVar) {
        this.a.forEach(j$.util.function.m.a(nVar));
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ boolean k(j$.util.function.r rVar) {
        return this.a.noneMatch(rVar == null ? null : rVar.a);
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ D limit(long j) {
        return i0(this.a.limit(j));
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ j$.util.l max() {
        return j$.util.a.t(this.a.max());
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ j$.util.l min() {
        return j$.util.a.t(this.a.min());
    }

    @Override // j$.util.stream.BaseStream
    public final /* synthetic */ BaseStream onClose(Runnable runnable) {
        return f.i0(this.a.onClose(runnable));
    }

    @Override // j$.util.stream.BaseStream
    public final /* synthetic */ BaseStream parallel() {
        return f.i0(this.a.parallel());
    }

    @Override // j$.util.stream.D, j$.util.stream.BaseStream
    public final /* synthetic */ D parallel() {
        return i0(this.a.parallel());
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ D s(j$.util.function.q qVar) {
        return i0(this.a.flatMap(j$.util.function.p.a(qVar)));
    }

    @Override // j$.util.stream.BaseStream
    public final /* synthetic */ BaseStream sequential() {
        return f.i0(this.a.sequential());
    }

    @Override // j$.util.stream.D, j$.util.stream.BaseStream
    public final /* synthetic */ D sequential() {
        return i0(this.a.sequential());
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ D skip(long j) {
        return i0(this.a.skip(j));
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ D sorted() {
        return i0(this.a.sorted());
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ j$.util.E spliterator() {
        return j$.util.C.b(this.a.spliterator());
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.D
    public final /* synthetic */ j$.util.Q spliterator() {
        return j$.util.O.b(this.a.spliterator());
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ double sum() {
        return this.a.sum();
    }

    @Override // j$.util.stream.D
    public final j$.util.h summaryStatistics() {
        this.a.summaryStatistics();
        throw new Error("Java 8+ API desugaring (library desugaring) cannot convert from java.util.DoubleSummaryStatistics");
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ LongStream t(j$.util.function.v vVar) {
        return i0.i0(this.a.mapToLong(j$.util.function.u.a(vVar)));
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ double[] toArray() {
        return this.a.toArray();
    }

    @Override // j$.util.stream.BaseStream
    public final /* synthetic */ BaseStream unordered() {
        return f.i0(this.a.unordered());
    }

    @Override // j$.util.stream.D
    public final /* synthetic */ j$.util.l z(j$.util.function.j jVar) {
        return j$.util.a.t(this.a.reduce(j$.util.function.i.a(jVar)));
    }
}
