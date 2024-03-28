package j$.util.stream;

import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class b1 extends e1 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public b1(j$.util.t tVar, int i, boolean z) {
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

    @Override // j$.util.stream.e1, j$.util.stream.f1
    public void Y(j$.util.function.q qVar) {
        if (!isParallel()) {
            e1.J0(H0()).d(qVar);
            return;
        }
        Objects.requireNonNull(qVar);
        v0(new n0(qVar, true));
    }

    @Override // j$.util.stream.e1, j$.util.stream.f1
    public void d(j$.util.function.q qVar) {
        if (isParallel()) {
            super.d(qVar);
        } else {
            e1.J0(H0()).d(qVar);
        }
    }

    @Override // j$.util.stream.c, j$.util.stream.g
    public /* bridge */ /* synthetic */ f1 parallel() {
        parallel();
        return this;
    }

    @Override // j$.util.stream.c, j$.util.stream.g
    public /* bridge */ /* synthetic */ f1 sequential() {
        sequential();
        return this;
    }
}
