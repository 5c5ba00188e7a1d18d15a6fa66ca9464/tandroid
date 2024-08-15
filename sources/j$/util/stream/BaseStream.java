package j$.util.stream;

import j$.util.stream.BaseStream;
import java.util.Iterator;
/* loaded from: classes2.dex */
public interface BaseStream<T, S extends BaseStream<T, S>> extends AutoCloseable {
    @Override // java.lang.AutoCloseable
    void close();

    boolean isParallel();

    Iterator iterator();

    BaseStream onClose(Runnable runnable);

    BaseStream parallel();

    BaseStream sequential();

    j$.util.Q spliterator();

    BaseStream unordered();
}
