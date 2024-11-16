package j$.util.stream;

import j$.util.function.Consumer;

/* loaded from: classes2.dex */
final class U1 extends W1 {
    U1(j$.util.Q q, int i, boolean z) {
        super(q, i, z);
    }

    @Override // j$.util.stream.W1, j$.util.stream.Stream
    public final void f(Consumer consumer) {
        if (isParallel()) {
            super.f(consumer);
        } else {
            B0().a(consumer);
        }
    }

    @Override // j$.util.stream.W1, j$.util.stream.Stream
    public final void forEach(Consumer consumer) {
        if (isParallel()) {
            super.forEach(consumer);
        } else {
            B0().a(consumer);
        }
    }

    @Override // j$.util.stream.b
    final boolean y0() {
        throw new UnsupportedOperationException();
    }

    @Override // j$.util.stream.b
    final e2 z0(int i, e2 e2Var) {
        throw new UnsupportedOperationException();
    }
}
