package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.LongFunction;
import j$.util.function.Supplier;
import j$.util.p;
import j$.util.s;
/* loaded from: classes2.dex */
public interface LongStream extends g {
    long C(long j, j$.util.function.o oVar);

    boolean K(j$.wrappers.i0 i0Var);

    V N(j$.wrappers.k0 k0Var);

    boolean Q(j$.wrappers.i0 i0Var);

    void W(j$.util.function.q qVar);

    IntStream a0(j$.wrappers.m0 m0Var);

    V asDoubleStream();

    j$.util.j average();

    Object b0(Supplier supplier, j$.util.function.v vVar, BiConsumer biConsumer);

    Stream boxed();

    long count();

    void d(j$.util.function.q qVar);

    LongStream distinct();

    j$.util.l findAny();

    j$.util.l findFirst();

    j$.util.l g(j$.util.function.o oVar);

    @Override // j$.util.stream.g
    p.b iterator();

    boolean k(j$.wrappers.i0 i0Var);

    LongStream limit(long j);

    <U> Stream<U> mapToObj(LongFunction<? extends U> longFunction);

    j$.util.l max();

    j$.util.l min();

    LongStream o(j$.util.function.q qVar);

    @Override // j$.util.stream.g
    LongStream parallel();

    LongStream r(LongFunction longFunction);

    @Override // j$.util.stream.g
    LongStream sequential();

    LongStream skip(long j);

    LongStream sorted();

    @Override // j$.util.stream.g
    s.c spliterator();

    long sum();

    j$.util.i summaryStatistics();

    LongStream t(j$.wrappers.i0 i0Var);

    long[] toArray();

    LongStream y(j$.util.function.s sVar);
}
