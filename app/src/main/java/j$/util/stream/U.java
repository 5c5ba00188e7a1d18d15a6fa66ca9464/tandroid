package j$.util.stream;

import j$.util.function.BiConsumer;
/* loaded from: classes2.dex */
public interface U extends g {
    j$.util.j G(j$.util.function.d dVar);

    Object H(j$.util.function.y yVar, j$.util.function.u uVar, BiConsumer biConsumer);

    double K(double d, j$.util.function.d dVar);

    Stream M(j$.util.function.g gVar);

    IntStream R(j$.wrappers.G g);

    boolean Y(j$.wrappers.E e);

    j$.util.j average();

    U b(j$.util.function.f fVar);

    Stream boxed();

    long count();

    U distinct();

    j$.util.j findAny();

    j$.util.j findFirst();

    boolean h0(j$.wrappers.E e);

    boolean i0(j$.wrappers.E e);

    @Override // j$.util.stream.g
    /* renamed from: iterator */
    j$.util.n mo331iterator();

    void j(j$.util.function.f fVar);

    void l0(j$.util.function.f fVar);

    U limit(long j);

    j$.util.j max();

    j$.util.j min();

    @Override // j$.util.stream.g, j$.util.stream.IntStream
    /* renamed from: parallel */
    U mo332parallel();

    U r(j$.wrappers.E e);

    @Override // j$.util.stream.g, j$.util.stream.IntStream
    /* renamed from: sequential */
    U mo333sequential();

    U skip(long j);

    U sorted();

    @Override // j$.util.stream.g
    /* renamed from: spliterator */
    j$.util.t mo334spliterator();

    double sum();

    j$.util.g summaryStatistics();

    double[] toArray();

    U w(j$.util.function.g gVar);

    e1 x(j$.util.function.h hVar);

    U y(j$.wrappers.K k);
}
