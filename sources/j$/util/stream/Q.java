package j$.util.stream;

import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class Q extends U {
    /* JADX INFO: Access modifiers changed from: package-private */
    public Q(j$.util.s sVar, int i, boolean z) {
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

    @Override // j$.util.stream.U, j$.util.stream.V
    public void g0(j$.util.function.f fVar) {
        if (!isParallel()) {
            U.G0(E0()).e(fVar);
            return;
        }
        Objects.requireNonNull(fVar);
        s0(new l0(fVar, true));
    }

    @Override // j$.util.stream.U, j$.util.stream.V
    public void j(j$.util.function.f fVar) {
        if (isParallel()) {
            super.j(fVar);
        } else {
            U.G0(E0()).e(fVar);
        }
    }

    @Override // j$.util.stream.c, j$.util.stream.g
    public /* bridge */ /* synthetic */ V parallel() {
        parallel();
        return this;
    }

    @Override // j$.util.stream.c, j$.util.stream.g
    public /* bridge */ /* synthetic */ V sequential() {
        sequential();
        return this;
    }
}
