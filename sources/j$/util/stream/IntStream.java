package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Supplier;
import j$.util.p;
import j$.util.t;
/* loaded from: classes2.dex */
public interface IntStream extends g {
    V A(j$.wrappers.W w);

    boolean C(j$.wrappers.U u);

    boolean F(j$.wrappers.U u);

    void I(j$.util.function.l lVar);

    Stream J(j$.util.function.m mVar);

    int N(int i, j$.util.function.j jVar);

    IntStream P(j$.util.function.m mVar);

    void T(j$.util.function.l lVar);

    j$.util.k Z(j$.util.function.j jVar);

    IntStream a0(j$.util.function.l lVar);

    V asDoubleStream();

    f1 asLongStream();

    j$.util.j average();

    Stream boxed();

    long count();

    IntStream distinct();

    f1 f(j$.util.function.n nVar);

    j$.util.k findAny();

    j$.util.k findFirst();

    IntStream h(j$.wrappers.U u);

    Object i0(Supplier supplier, j$.util.function.v vVar, BiConsumer biConsumer);

    @Override // j$.util.stream.g
    p.a iterator();

    IntStream limit(long j);

    j$.util.k max();

    j$.util.k min();

    @Override // j$.util.stream.g
    IntStream parallel();

    IntStream q(j$.wrappers.a0 a0Var);

    @Override // j$.util.stream.g
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
