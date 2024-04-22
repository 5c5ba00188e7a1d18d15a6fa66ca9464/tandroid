package j$.util.stream;

import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class J0 extends M0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public J0(j$.util.s sVar, int i, boolean z) {
        super(sVar, i, z);
    }

    @Override // j$.util.stream.c
    final boolean B0() {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public final m3 C0(int i, m3 m3Var) {
        throw new UnsupportedOperationException();
    }

    @Override // j$.util.stream.M0, j$.util.stream.IntStream
    public void H(j$.util.function.l lVar) {
        if (!isParallel()) {
            M0.G0(E0()).c(lVar);
            return;
        }
        Objects.requireNonNull(lVar);
        s0(new m0(lVar, true));
    }

    @Override // j$.util.stream.M0, j$.util.stream.IntStream
    public void R(j$.util.function.l lVar) {
        if (isParallel()) {
            super.R(lVar);
        } else {
            M0.G0(E0()).c(lVar);
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
