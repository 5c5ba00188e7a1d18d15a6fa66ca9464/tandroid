package j$.util;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
public interface E extends N {
    @Override // j$.util.Q
    boolean a(Consumer consumer);

    void d(j$.util.function.m mVar);

    @Override // j$.util.Q
    void forEachRemaining(Consumer consumer);

    boolean o(j$.util.function.m mVar);

    @Override // j$.util.N, j$.util.Q
    E trySplit();
}
