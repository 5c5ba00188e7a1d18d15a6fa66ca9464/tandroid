package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Supplier;
/* loaded from: classes2.dex */
public interface F extends BaseStream {
    IntStream B(j$.util.function.v vVar);

    void H(j$.util.function.m mVar);

    j$.util.l N(j$.util.function.i iVar);

    double Q(double d, j$.util.function.i iVar);

    boolean R(j$.util.function.s sVar);

    boolean U(j$.util.function.s sVar);

    j$.util.l average();

    F b(j$.util.function.m mVar);

    Stream boxed();

    long count();

    F distinct();

    j$.util.l findAny();

    j$.util.l findFirst();

    void g0(j$.util.function.m mVar);

    F i(j$.util.function.s sVar);

    @Override // 
    j$.util.r iterator();

    F j(j$.util.function.p pVar);

    LongStream k(j$.util.function.y yVar);

    F limit(long j);

    j$.util.l max();

    j$.util.l min();

    Object p(Supplier supplier, j$.util.function.z0 z0Var, BiConsumer biConsumer);

    @Override // 
    F parallel();

    F q(j$.util.function.B b);

    Stream r(j$.util.function.p pVar);

    @Override // 
    F sequential();

    F skip(long j);

    F sorted();

    @Override // 
    j$.util.E spliterator();

    double sum();

    j$.util.h summaryStatistics();

    double[] toArray();

    boolean w(j$.util.function.s sVar);
}
