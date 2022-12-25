package j$.util.stream;

import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class a1 extends d1 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public a1(j$.util.u uVar, int i, boolean z) {
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

    @Override // j$.util.stream.d1, j$.util.stream.e1
    public void Z(j$.util.function.q qVar) {
        if (!isParallel()) {
            d1.L0(J0()).d(qVar);
            return;
        }
        Objects.requireNonNull(qVar);
        x0(new m0(qVar, true));
    }

    @Override // j$.util.stream.d1, j$.util.stream.e1
    public void d(j$.util.function.q qVar) {
        if (isParallel()) {
            super.d(qVar);
        } else {
            d1.L0(J0()).d(qVar);
        }
    }

    @Override // j$.util.stream.c, j$.util.stream.g, j$.util.stream.IntStream
    public /* bridge */ /* synthetic */ e1 parallel() {
        parallel();
        return this;
    }

    @Override // j$.util.stream.c, j$.util.stream.g, j$.util.stream.IntStream
    public /* bridge */ /* synthetic */ e1 sequential() {
        sequential();
        return this;
    }
}
