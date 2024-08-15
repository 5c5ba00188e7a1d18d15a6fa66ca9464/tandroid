package j$.util;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
public interface K extends N {
    @Override // j$.util.Q
    boolean a(Consumer consumer);

    void b(j$.util.function.h0 h0Var);

    boolean e(j$.util.function.h0 h0Var);

    @Override // j$.util.Q
    void forEachRemaining(Consumer consumer);

    @Override // j$.util.N, j$.util.Q
    K trySplit();
}
