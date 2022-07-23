package j$.util.stream;

import java.util.Iterator;
/* loaded from: classes2.dex */
public interface g extends AutoCloseable {
    @Override // java.lang.AutoCloseable
    void close();

    boolean isParallel();

    /* renamed from: iterator */
    Iterator mo66iterator();

    g onClose(Runnable runnable);

    g parallel();

    g sequential();

    j$.util.u spliterator();

    g unordered();
}
