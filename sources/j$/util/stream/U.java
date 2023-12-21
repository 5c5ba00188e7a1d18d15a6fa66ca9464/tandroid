package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.t;
/* loaded from: classes2.dex */
public interface U extends g {
    j$.util.j G(j$.util.function.d dVar);

    Object H(j$.util.function.y yVar, j$.util.function.u uVar, BiConsumer biConsumer);

    double K(double d, j$.util.function.d dVar);

    Stream M(j$.util.function.g gVar);

    IntStream R(j$.wrappers.F f);

    boolean Y(j$.wrappers.D d);

    j$.util.j average();

    U b(j$.util.function.f fVar);

    Stream boxed();

    long count();

    U distinct();

    j$.util.j findAny();

    j$.util.j findFirst();

    boolean h0(j$.wrappers.D d);

    boolean i0(j$.wrappers.D d);

    @Override // j$.util.stream.g
    j$.util.n iterator();

    void j(j$.util.function.f fVar);

    void l0(j$.util.function.f fVar);

    U limit(long j);

    j$.util.j max();

    j$.util.j min();

    @Override // j$.util.stream.g, j$.util.stream.IntStream
    U parallel();

    U r(j$.wrappers.D d);

    @Override // j$.util.stream.g, j$.util.stream.IntStream
    U sequential();

    U skip(long j);

    U sorted();

    @Override // j$.util.stream.g
    t.a spliterator();

    double sum();

    j$.util.g summaryStatistics();

    double[] toArray();

    U w(j$.util.function.g gVar);

    e1 x(j$.util.function.h hVar);

    U y(j$.wrappers.J j);
}
