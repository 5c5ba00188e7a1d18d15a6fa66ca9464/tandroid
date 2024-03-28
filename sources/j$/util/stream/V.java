package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Supplier;
import j$.util.t;
/* loaded from: classes2.dex */
public interface V extends g {
    j$.util.j G(j$.util.function.d dVar);

    Object H(Supplier supplier, j$.util.function.u uVar, BiConsumer biConsumer);

    double K(double d, j$.util.function.d dVar);

    Stream M(j$.util.function.g gVar);

    IntStream R(j$.wrappers.F f);

    boolean X(j$.wrappers.D d);

    j$.util.j average();

    V b(j$.util.function.f fVar);

    Stream boxed();

    long count();

    V distinct();

    boolean f0(j$.wrappers.D d);

    j$.util.j findAny();

    j$.util.j findFirst();

    boolean g0(j$.wrappers.D d);

    @Override // j$.util.stream.g
    j$.util.n iterator();

    void j(j$.util.function.f fVar);

    void j0(j$.util.function.f fVar);

    V limit(long j);

    j$.util.j max();

    j$.util.j min();

    @Override // j$.util.stream.g
    V parallel();

    V r(j$.wrappers.D d);

    @Override // j$.util.stream.g
    V sequential();

    V skip(long j);

    V sorted();

    @Override // j$.util.stream.g
    t.a spliterator();

    double sum();

    j$.util.g summaryStatistics();

    double[] toArray();

    V w(j$.util.function.g gVar);

    f1 x(j$.util.function.h hVar);

    V y(j$.wrappers.J j);
}
