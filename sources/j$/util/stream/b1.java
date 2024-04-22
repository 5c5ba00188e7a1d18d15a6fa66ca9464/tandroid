package j$.util.stream;

import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class b1 extends e1 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public b1(j$.util.s sVar, int i, boolean z) {
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

    @Override // j$.util.stream.e1, j$.util.stream.LongStream
    public void W(j$.util.function.q qVar) {
        if (!isParallel()) {
            e1.G0(E0()).d(qVar);
            return;
        }
        Objects.requireNonNull(qVar);
        s0(new n0(qVar, true));
    }

    @Override // j$.util.stream.e1, j$.util.stream.LongStream
    public void d(j$.util.function.q qVar) {
        if (isParallel()) {
            super.d(qVar);
        } else {
            e1.G0(E0()).d(qVar);
        }
    }

    @Override // j$.util.stream.c, j$.util.stream.g
    public /* bridge */ /* synthetic */ LongStream parallel() {
        parallel();
        return this;
    }

    @Override // j$.util.stream.c, j$.util.stream.g
    public /* bridge */ /* synthetic */ LongStream sequential() {
        sequential();
        return this;
    }
}
