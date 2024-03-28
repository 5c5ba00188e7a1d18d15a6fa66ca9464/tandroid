package j$.util.stream;

import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class Q extends U {
    /* JADX INFO: Access modifiers changed from: package-private */
    public Q(j$.util.t tVar, int i, boolean z) {
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

    @Override // j$.util.stream.U, j$.util.stream.V
    public void j(j$.util.function.f fVar) {
        if (isParallel()) {
            super.j(fVar);
        } else {
            U.J0(H0()).e(fVar);
        }
    }

    @Override // j$.util.stream.U, j$.util.stream.V
    public void j0(j$.util.function.f fVar) {
        if (!isParallel()) {
            U.J0(H0()).e(fVar);
            return;
        }
        Objects.requireNonNull(fVar);
        v0(new l0(fVar, true));
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
