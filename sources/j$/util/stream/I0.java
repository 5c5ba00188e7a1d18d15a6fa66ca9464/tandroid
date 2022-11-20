package j$.util.stream;

import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class I0 extends L0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public I0(j$.util.u uVar, int i, boolean z) {
        super(uVar, i, z);
    }

    @Override // j$.util.stream.c
    final boolean G0() {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public final m3 H0(int i, m3 m3Var) {
        throw new UnsupportedOperationException();
    }

    @Override // j$.util.stream.L0, j$.util.stream.IntStream
    public void I(j$.util.function.l lVar) {
        if (!isParallel()) {
            L0.L0(J0()).c(lVar);
            return;
        }
        Objects.requireNonNull(lVar);
        x0(new l0(lVar, true));
    }

    @Override // j$.util.stream.L0, j$.util.stream.IntStream
    public void U(j$.util.function.l lVar) {
        if (!isParallel()) {
            L0.L0(J0()).c(lVar);
        } else {
            super.U(lVar);
        }
    }

    @Override // j$.util.stream.c, j$.util.stream.g, j$.util.stream.IntStream
    public /* bridge */ /* synthetic */ IntStream parallel() {
        parallel();
        return this;
    }

    @Override // j$.util.stream.c, j$.util.stream.g, j$.util.stream.IntStream
    public /* bridge */ /* synthetic */ IntStream sequential() {
        sequential();
        return this;
    }
}
