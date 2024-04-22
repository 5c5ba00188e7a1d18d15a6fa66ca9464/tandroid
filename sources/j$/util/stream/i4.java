package j$.util.stream;

import java.util.Objects;
/* loaded from: classes2.dex */
final class i4 extends j4 implements j$.util.function.q {
    final long[] c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public i4(int i) {
        this.c = new long[i];
    }

    @Override // j$.util.function.q
    public void accept(long j) {
        long[] jArr = this.c;
        int i = this.b;
        this.b = i + 1;
        jArr[i] = j;
    }

    @Override // j$.util.stream.j4
    public void b(Object obj, long j) {
        j$.util.function.q qVar = (j$.util.function.q) obj;
        for (int i = 0; i < j; i++) {
            qVar.accept(this.c[i]);
        }
    }

    @Override // j$.util.function.q
    public j$.util.function.q f(j$.util.function.q qVar) {
        Objects.requireNonNull(qVar);
        return new j$.util.function.p(this, qVar);
    }
}
