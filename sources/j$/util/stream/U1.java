package j$.util.stream;

import j$.util.function.Consumer;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class U1 extends X1 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public U1(j$.util.Q q, int i, boolean z) {
        super(q, i, z);
    }

    @Override // j$.util.stream.X1, j$.util.stream.Stream
    public final void F(Consumer consumer) {
        if (isParallel()) {
            super.F(consumer);
        } else {
            n1().forEachRemaining(consumer);
        }
    }

    @Override // j$.util.stream.X1, j$.util.stream.Stream
    public final void forEach(Consumer consumer) {
        if (isParallel()) {
            super.forEach(consumer);
        } else {
            n1().forEachRemaining(consumer);
        }
    }

    @Override // j$.util.stream.c
    final boolean k1() {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public final f2 l1(int i, f2 f2Var) {
        throw new UnsupportedOperationException();
    }
}
