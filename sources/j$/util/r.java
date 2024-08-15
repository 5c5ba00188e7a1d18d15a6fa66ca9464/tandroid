package j$.util;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
public interface r extends A {
    void d(j$.util.function.m mVar);

    void forEachRemaining(Consumer consumer);

    @Override // java.util.Iterator, j$.util.Iterator
    Double next();

    double nextDouble();
}
