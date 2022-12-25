package j$.util.stream;

import j$.util.function.BiConsumer;
/* loaded from: classes2.dex */
public interface e1 extends g {
    long D(long j, j$.util.function.o oVar);

    boolean L(j$.wrappers.i0 i0Var);

    U O(j$.wrappers.k0 k0Var);

    Stream Q(j$.util.function.r rVar);

    boolean S(j$.wrappers.i0 i0Var);

    void Z(j$.util.function.q qVar);

    U asDoubleStream();

    j$.util.j average();

    Stream boxed();

    long count();

    void d(j$.util.function.q qVar);

    e1 distinct();

    IntStream e0(j$.wrappers.m0 m0Var);

    Object f0(j$.util.function.y yVar, j$.util.function.w wVar, BiConsumer biConsumer);

    j$.util.l findAny();

    j$.util.l findFirst();

    j$.util.l g(j$.util.function.o oVar);

    @Override // j$.util.stream.g
    j$.util.r iterator();

    boolean k(j$.wrappers.i0 i0Var);

    e1 limit(long j);

    j$.util.l max();

    j$.util.l min();

    e1 p(j$.util.function.q qVar);

    @Override // j$.util.stream.g, j$.util.stream.IntStream
    e1 parallel();

    e1 s(j$.util.function.r rVar);

    @Override // j$.util.stream.g, j$.util.stream.IntStream
    e1 sequential();

    e1 skip(long j);

    e1 sorted();

    @Override // j$.util.stream.g
    j$.util.v spliterator();

    long sum();

    j$.util.i summaryStatistics();

    long[] toArray();

    e1 u(j$.wrappers.i0 i0Var);

    e1 z(j$.util.function.t tVar);
}
