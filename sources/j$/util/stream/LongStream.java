package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.LongFunction;
import j$.util.function.Supplier;

/* loaded from: classes2.dex */
public interface LongStream extends BaseStream<Long, LongStream> {
    IntStream A(j$.util.function.b0 b0Var);

    boolean E(j$.util.function.Z z);

    boolean G(j$.util.function.Z z);

    LongStream M(j$.util.function.Z z);

    void V(j$.util.function.W w);

    Object Z(Supplier supplier, j$.util.function.o0 o0Var, BiConsumer biConsumer);

    D asDoubleStream();

    j$.util.l average();

    Stream boxed();

    long count();

    void d(j$.util.function.W w);

    LongStream distinct();

    j$.util.n findAny();

    j$.util.n findFirst();

    j$.util.n h(j$.util.function.S s);

    @Override // j$.util.stream.BaseStream, j$.util.stream.D
    j$.util.z iterator();

    LongStream limit(long j);

    <U> Stream<U> mapToObj(LongFunction<? extends U> longFunction);

    j$.util.n max();

    j$.util.n min();

    LongStream o(j$.util.function.W w);

    LongStream p(LongFunction longFunction);

    @Override // j$.util.stream.BaseStream
    LongStream parallel();

    D r(j$.util.function.a0 a0Var);

    @Override // j$.util.stream.BaseStream
    LongStream sequential();

    LongStream skip(long j);

    LongStream sorted();

    @Override // j$.util.stream.BaseStream, j$.util.stream.D
    j$.util.K spliterator();

    long sum();

    j$.util.k summaryStatistics();

    long[] toArray();

    boolean u(j$.util.function.Z z);

    LongStream v(j$.util.function.f0 f0Var);

    long x(long j, j$.util.function.S s);
}
