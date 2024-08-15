package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.LongFunction;
import j$.util.function.Supplier;
/* loaded from: classes2.dex */
public interface LongStream extends BaseStream<Long, LongStream> {
    void E(j$.util.function.h0 h0Var);

    F J(j$.util.function.p0 p0Var);

    LongStream M(j$.util.function.w0 w0Var);

    IntStream T(j$.util.function.s0 s0Var);

    boolean a(j$.util.function.m0 m0Var);

    F asDoubleStream();

    j$.util.l average();

    Stream boxed();

    boolean c0(j$.util.function.m0 m0Var);

    long count();

    LongStream distinct();

    j$.util.n e(j$.util.function.d0 d0Var);

    LongStream e0(j$.util.function.m0 m0Var);

    j$.util.n findAny();

    j$.util.n findFirst();

    LongStream g(j$.util.function.h0 h0Var);

    LongStream h(LongFunction longFunction);

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    j$.util.z iterator();

    LongStream limit(long j);

    <U> Stream<U> mapToObj(LongFunction<? extends U> longFunction);

    j$.util.n max();

    j$.util.n min();

    long n(long j, j$.util.function.d0 d0Var);

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    LongStream parallel();

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    LongStream sequential();

    LongStream skip(long j);

    LongStream sorted();

    @Override // j$.util.stream.BaseStream, j$.util.stream.F
    j$.util.K spliterator();

    long sum();

    j$.util.j summaryStatistics();

    long[] toArray();

    void x(j$.util.function.h0 h0Var);

    Object y(Supplier supplier, j$.util.function.F0 f0, BiConsumer biConsumer);

    boolean z(j$.util.function.m0 m0Var);
}
