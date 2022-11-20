package j$.util.stream;

import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class P extends T {
    /* JADX INFO: Access modifiers changed from: package-private */
    public P(j$.util.u uVar, int i, boolean z) {
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

    @Override // j$.util.stream.T, j$.util.stream.U
    public void j(j$.util.function.f fVar) {
        if (!isParallel()) {
            T.L0(J0()).e(fVar);
        } else {
            super.j(fVar);
        }
    }

    @Override // j$.util.stream.T, j$.util.stream.U
    public void l0(j$.util.function.f fVar) {
        if (!isParallel()) {
            T.L0(J0()).e(fVar);
            return;
        }
        Objects.requireNonNull(fVar);
        x0(new k0(fVar, true));
    }

    @Override // j$.util.stream.c, j$.util.stream.g, j$.util.stream.IntStream
    public /* bridge */ /* synthetic */ U parallel() {
        parallel();
        return this;
    }

    @Override // j$.util.stream.c, j$.util.stream.g, j$.util.stream.IntStream
    public /* bridge */ /* synthetic */ U sequential() {
        sequential();
        return this;
    }
}
