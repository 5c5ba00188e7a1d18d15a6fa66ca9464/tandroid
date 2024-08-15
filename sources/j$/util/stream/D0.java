package j$.util.stream;

import j$.util.function.Consumer;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public interface D0 {
    D0 a(int i);

    long count();

    void e(Object[] objArr, int i);

    void forEach(Consumer consumer);

    int j();

    Object[] o(j$.util.function.N n);

    D0 q(long j, long j2, j$.util.function.N n);

    j$.util.Q spliterator();
}
