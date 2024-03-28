package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Supplier;
import j$.util.t;
/* loaded from: classes2.dex */
public interface f1 extends g {
    long D(long j, j$.util.function.o oVar);

    boolean L(j$.wrappers.i0 i0Var);

    V O(j$.wrappers.k0 k0Var);

    Stream Q(j$.util.function.r rVar);

    boolean S(j$.wrappers.i0 i0Var);

    void Y(j$.util.function.q qVar);

    V asDoubleStream();

    j$.util.j average();

    Stream boxed();

    IntStream c0(j$.wrappers.m0 m0Var);

    long count();

    void d(j$.util.function.q qVar);

    Object d0(Supplier supplier, j$.util.function.w wVar, BiConsumer biConsumer);

    f1 distinct();

    j$.util.l findAny();

    j$.util.l findFirst();

    j$.util.l g(j$.util.function.o oVar);

    @Override // j$.util.stream.g
    j$.util.r iterator();

    boolean k(j$.wrappers.i0 i0Var);

    f1 limit(long j);

    j$.util.l max();

    j$.util.l min();

    f1 p(j$.util.function.q qVar);

    @Override // j$.util.stream.g
    f1 parallel();

    f1 s(j$.util.function.r rVar);

    @Override // j$.util.stream.g
    f1 sequential();

    f1 skip(long j);

    f1 sorted();

    @Override // j$.util.stream.g
    t.c spliterator();

    long sum();

    j$.util.i summaryStatistics();

    long[] toArray();

    f1 u(j$.wrappers.i0 i0Var);

    f1 z(j$.util.function.t tVar);
}
