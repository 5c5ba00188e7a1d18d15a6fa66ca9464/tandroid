package j$.util;

import j$.util.function.Consumer;
import j$.util.function.Predicate;
import j$.util.stream.Stream;
/* loaded from: classes2.dex */
public interface b {
    boolean a(Predicate predicate);

    void forEach(Consumer consumer);

    Q spliterator();

    Stream stream();
}
