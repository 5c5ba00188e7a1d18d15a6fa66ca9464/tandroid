package j$.util.stream;

import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class J0 extends M0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public J0(j$.util.t tVar, int i, boolean z) {
        super(tVar, i, z);
    }

    @Override // j$.util.stream.c
    final boolean E0() {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public final n3 F0(int i, n3 n3Var) {
        throw new UnsupportedOperationException();
    }

    @Override // j$.util.stream.M0, j$.util.stream.IntStream
    public void I(j$.util.function.l lVar) {
        if (!isParallel()) {
            M0.J0(H0()).c(lVar);
            return;
        }
        Objects.requireNonNull(lVar);
        v0(new m0(lVar, true));
    }

    @Override // j$.util.stream.M0, j$.util.stream.IntStream
    public void T(j$.util.function.l lVar) {
        if (isParallel()) {
            super.T(lVar);
        } else {
            M0.J0(H0()).c(lVar);
        }
    }

    @Override // j$.util.stream.c, j$.util.stream.g
    public /* bridge */ /* synthetic */ IntStream parallel() {
        parallel();
        return this;
    }

    @Override // j$.util.stream.c, j$.util.stream.g
    public /* bridge */ /* synthetic */ IntStream sequential() {
        sequential();
        return this;
    }
}
