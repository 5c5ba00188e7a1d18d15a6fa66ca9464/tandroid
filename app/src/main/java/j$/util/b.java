package j$.util;

import j$.util.function.Consumer;
import j$.util.function.Predicate;
import j$.util.stream.Stream;
/* loaded from: classes2.dex */
public interface b extends j$.lang.e {
    @Override // j$.lang.e
    void forEach(Consumer consumer);

    boolean k(Predicate predicate);

    @Override // j$.lang.e
    /* renamed from: spliterator */
    u mo317spliterator();

    /* renamed from: stream */
    Stream mo266stream();
}
