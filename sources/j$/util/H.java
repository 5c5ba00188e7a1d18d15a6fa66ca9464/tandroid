package j$.util;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
public interface H extends N {
    @Override // j$.util.Q
    boolean a(Consumer consumer);

    void c(j$.util.function.K k);

    @Override // j$.util.Q
    void forEachRemaining(Consumer consumer);

    boolean j(j$.util.function.K k);

    @Override // j$.util.N, j$.util.Q
    H trySplit();
}
