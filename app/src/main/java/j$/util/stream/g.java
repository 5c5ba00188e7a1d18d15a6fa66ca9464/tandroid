package j$.util.stream;

import java.util.Iterator;
/* loaded from: classes2.dex */
public interface g extends AutoCloseable {
    @Override // java.lang.AutoCloseable
    void close();

    boolean isParallel();

    /* renamed from: iterator */
    Iterator mo331iterator();

    g onClose(Runnable runnable);

    /* renamed from: parallel */
    g mo332parallel();

    /* renamed from: sequential */
    g mo333sequential();

    /* renamed from: spliterator */
    j$.util.u mo334spliterator();

    g unordered();
}
