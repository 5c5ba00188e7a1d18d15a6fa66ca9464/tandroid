package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Supplier;
import j$.util.s;
/* loaded from: classes2.dex */
public interface V extends g {
    j$.util.j F(j$.util.function.d dVar);

    Object G(Supplier supplier, j$.util.function.t tVar, BiConsumer biConsumer);

    double J(double d, j$.util.function.d dVar);

    Stream L(j$.util.function.g gVar);

    IntStream P(j$.wrappers.F f);

    boolean V(j$.wrappers.D d);

    j$.util.j average();

    V b(j$.util.function.f fVar);

    Stream boxed();

    boolean c0(j$.wrappers.D d);

    long count();

    boolean d0(j$.wrappers.D d);

    V distinct();

    j$.util.j findAny();

    j$.util.j findFirst();

    void g0(j$.util.function.f fVar);

    @Override // j$.util.stream.g
    j$.util.n iterator();

    void j(j$.util.function.f fVar);

    V limit(long j);

    j$.util.j max();

    j$.util.j min();

    @Override // j$.util.stream.g
    V parallel();

    V q(j$.wrappers.D d);

    @Override // j$.util.stream.g
    V sequential();

    V skip(long j);

    V sorted();

    @Override // j$.util.stream.g
    s.a spliterator();

    double sum();

    j$.util.g summaryStatistics();

    double[] toArray();

    V v(j$.util.function.g gVar);

    LongStream w(j$.util.function.h hVar);

    V x(j$.wrappers.J j);
}
