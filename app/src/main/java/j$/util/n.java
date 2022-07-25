package j$.util;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
public interface n extends p {
    void e(j$.util.function.f fVar);

    void forEachRemaining(Consumer consumer);

    @Override // java.util.Iterator, j$.util.Iterator
    /* renamed from: next */
    Double mo335next();

    double nextDouble();
}
