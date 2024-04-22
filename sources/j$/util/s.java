package j$.util;

import j$.util.function.Consumer;
import java.util.Comparator;
/* loaded from: classes2.dex */
public interface s {

    /* loaded from: classes2.dex */
    public interface a extends t {
        @Override // j$.util.s
        boolean b(Consumer consumer);

        void e(j$.util.function.f fVar);

        @Override // j$.util.s
        void forEachRemaining(Consumer consumer);

        boolean k(j$.util.function.f fVar);

        @Override // j$.util.t, j$.util.s
        a trySplit();
    }

    /* loaded from: classes2.dex */
    public interface b extends t {
        @Override // j$.util.s
        boolean b(Consumer consumer);

        void c(j$.util.function.l lVar);

        @Override // j$.util.s
        void forEachRemaining(Consumer consumer);

        boolean g(j$.util.function.l lVar);

        @Override // j$.util.t, j$.util.s
        b trySplit();
    }

    /* loaded from: classes2.dex */
    public interface c extends t {
        @Override // j$.util.s
        boolean b(Consumer consumer);

        void d(j$.util.function.q qVar);

        @Override // j$.util.s
        void forEachRemaining(Consumer consumer);

        boolean i(j$.util.function.q qVar);

        @Override // j$.util.t, j$.util.s
        c trySplit();
    }

    boolean b(Consumer consumer);

    int characteristics();

    long estimateSize();

    void forEachRemaining(Consumer consumer);

    Comparator getComparator();

    long getExactSizeIfKnown();

    boolean hasCharacteristics(int i);

    s trySplit();
}
