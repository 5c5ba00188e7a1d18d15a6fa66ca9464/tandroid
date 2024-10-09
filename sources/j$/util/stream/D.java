package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Supplier;

/* loaded from: classes2.dex */
public interface D extends BaseStream {
    Object B(Supplier supplier, j$.util.function.i0 i0Var, BiConsumer biConsumer);

    double F(double d, j$.util.function.j jVar);

    Stream I(j$.util.function.q qVar);

    D N(j$.util.function.w wVar);

    IntStream S(j$.util.function.s sVar);

    D U(j$.util.function.r rVar);

    j$.util.l average();

    D b(j$.util.function.n nVar);

    Stream boxed();

    long count();

    boolean d0(j$.util.function.r rVar);

    D distinct();

    void f0(j$.util.function.n nVar);

    j$.util.l findAny();

    j$.util.l findFirst();

    boolean g0(j$.util.function.r rVar);

    j$.util.r iterator();

    void j(j$.util.function.n nVar);

    boolean k(j$.util.function.r rVar);

    D limit(long j);

    j$.util.l max();

    j$.util.l min();

    @Override // j$.util.stream.BaseStream
    D parallel();

    D s(j$.util.function.q qVar);

    @Override // j$.util.stream.BaseStream
    D sequential();

    D skip(long j);

    D sorted();

    j$.util.E spliterator();

    double sum();

    j$.util.h summaryStatistics();

    LongStream t(j$.util.function.v vVar);

    double[] toArray();

    j$.util.l z(j$.util.function.j jVar);
}
