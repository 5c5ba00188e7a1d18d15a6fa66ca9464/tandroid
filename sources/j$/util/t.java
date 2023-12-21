package j$.util;

import j$.util.function.Consumer;
import java.util.Comparator;
/* loaded from: classes2.dex */
public interface t {

    /* loaded from: classes2.dex */
    public interface a extends u {
        @Override // j$.util.t
        boolean b(Consumer consumer);

        void e(j$.util.function.f fVar);

        @Override // j$.util.t
        void forEachRemaining(Consumer consumer);

        boolean k(j$.util.function.f fVar);

        @Override // j$.util.u, j$.util.t
        a trySplit();
    }

    /* loaded from: classes2.dex */
    public interface b extends u {
        @Override // j$.util.t
        boolean b(Consumer consumer);

        void c(j$.util.function.l lVar);

        @Override // j$.util.t
        void forEachRemaining(Consumer consumer);

        boolean g(j$.util.function.l lVar);

        @Override // j$.util.u, j$.util.t
        b trySplit();
    }

    /* loaded from: classes2.dex */
    public interface c extends u {
        @Override // j$.util.t
        boolean b(Consumer consumer);

        void d(j$.util.function.q qVar);

        @Override // j$.util.t
        void forEachRemaining(Consumer consumer);

        boolean i(j$.util.function.q qVar);

        @Override // j$.util.u, j$.util.t
        c trySplit();
    }

    boolean b(Consumer consumer);

    int characteristics();

    long estimateSize();

    void forEachRemaining(Consumer consumer);

    Comparator getComparator();

    long getExactSizeIfKnown();

    boolean hasCharacteristics(int i);

    t trySplit();
}
