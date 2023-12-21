package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.p;
import j$.util.t;
/* loaded from: classes2.dex */
public interface IntStream extends g {
    U A(j$.wrappers.W w);

    boolean C(j$.wrappers.U u);

    boolean F(j$.wrappers.U u);

    void I(j$.util.function.l lVar);

    Stream J(j$.util.function.m mVar);

    int N(int i, j$.util.function.j jVar);

    IntStream P(j$.util.function.m mVar);

    void U(j$.util.function.l lVar);

    j$.util.k a0(j$.util.function.j jVar);

    U asDoubleStream();

    e1 asLongStream();

    j$.util.j average();

    Stream boxed();

    IntStream c0(j$.util.function.l lVar);

    long count();

    IntStream distinct();

    e1 f(j$.util.function.n nVar);

    j$.util.k findAny();

    j$.util.k findFirst();

    IntStream h(j$.wrappers.U u);

    @Override // j$.util.stream.g
    p.a iterator();

    Object k0(j$.util.function.y yVar, j$.util.function.v vVar, BiConsumer biConsumer);

    IntStream limit(long j);

    j$.util.k max();

    j$.util.k min();

    @Override // 
    IntStream parallel();

    IntStream q(j$.wrappers.a0 a0Var);

    @Override // 
    IntStream sequential();

    IntStream skip(long j);

    IntStream sorted();

    @Override // j$.util.stream.g
    t.b spliterator();

    int sum();

    j$.util.h summaryStatistics();

    int[] toArray();

    boolean v(j$.wrappers.U u);
}
