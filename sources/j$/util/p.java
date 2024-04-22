package j$.util;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
public interface p extends java.util.Iterator {

    /* loaded from: classes2.dex */
    public interface a extends p {
        void c(j$.util.function.l lVar);

        void forEachRemaining(Consumer consumer);

        @Override // java.util.Iterator
        Integer next();

        int nextInt();
    }

    /* loaded from: classes2.dex */
    public interface b extends p {
        void d(j$.util.function.q qVar);

        void forEachRemaining(Consumer consumer);

        @Override // java.util.Iterator
        Long next();

        long nextLong();
    }

    void forEachRemaining(Object obj);
}
