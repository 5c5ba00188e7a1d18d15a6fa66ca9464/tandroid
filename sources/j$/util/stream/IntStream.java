package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Supplier;
import j$.util.p;
import j$.util.s;
/* loaded from: classes2.dex */
public interface IntStream extends g {
    boolean B(j$.wrappers.U u);

    boolean E(j$.wrappers.U u);

    void H(j$.util.function.l lVar);

    Stream I(j$.util.function.m mVar);

    int M(int i, j$.util.function.j jVar);

    IntStream O(j$.util.function.m mVar);

    void R(j$.util.function.l lVar);

    j$.util.k X(j$.util.function.j jVar);

    IntStream Y(j$.util.function.l lVar);

    V asDoubleStream();

    LongStream asLongStream();

    j$.util.j average();

    Stream boxed();

    long count();

    IntStream distinct();

    LongStream f(j$.util.function.n nVar);

    Object f0(Supplier supplier, j$.util.function.u uVar, BiConsumer biConsumer);

    j$.util.k findAny();

    j$.util.k findFirst();

    IntStream h(j$.wrappers.U u);

    @Override // j$.util.stream.g
    p.a iterator();

    IntStream limit(long j);

    j$.util.k max();

    j$.util.k min();

    IntStream p(j$.wrappers.a0 a0Var);

    @Override // j$.util.stream.g
    IntStream parallel();

    @Override // j$.util.stream.g
    IntStream sequential();

    IntStream skip(long j);

    IntStream sorted();

    @Override // j$.util.stream.g
    s.b spliterator();

    int sum();

    j$.util.h summaryStatistics();

    int[] toArray();

    boolean u(j$.wrappers.U u);

    V z(j$.wrappers.W w);
}
