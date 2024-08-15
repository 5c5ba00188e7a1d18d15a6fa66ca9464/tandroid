package j$.util;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
public interface v extends A {
    void c(j$.util.function.K k);

    void forEachRemaining(Consumer consumer);

    @Override // java.util.Iterator, j$.util.Iterator
    Integer next();

    int nextInt();
}
