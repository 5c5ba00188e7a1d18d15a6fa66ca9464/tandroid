package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.p;
import j$.util.u;
/* loaded from: classes2.dex */
public interface IntStream extends g {
    U A(j$.wrappers.X x);

    boolean C(j$.wrappers.V v);

    boolean F(j$.wrappers.V v);

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

    IntStream h(j$.wrappers.V v);

    @Override // j$.util.stream.g
    /* renamed from: iterator */
    p.a mo331iterator();

    Object k0(j$.util.function.y yVar, j$.util.function.v vVar, BiConsumer biConsumer);

    IntStream limit(long j);

    j$.util.k max();

    j$.util.k min();

    @Override // 
    /* renamed from: parallel */
    IntStream mo332parallel();

    IntStream q(j$.wrappers.b0 b0Var);

    @Override // 
    /* renamed from: sequential */
    IntStream mo333sequential();

    IntStream skip(long j);

    IntStream sorted();

    @Override // j$.util.stream.g
    /* renamed from: spliterator */
    u.a mo334spliterator();

    int sum();

    j$.util.h summaryStatistics();

    int[] toArray();

    boolean v(j$.wrappers.V v);
}
