package j$.util;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
public interface z extends A {
    void b(j$.util.function.h0 h0Var);

    void forEachRemaining(Consumer consumer);

    @Override // java.util.Iterator, j$.util.Iterator
    Long next();

    long nextLong();
}
