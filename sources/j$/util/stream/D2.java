package j$.util.stream;

import java.util.Arrays;

/* loaded from: classes2.dex */
final class D2 extends r2 {
    private long[] c;
    private int d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public D2(e2 e2Var) {
        super(e2Var);
    }

    @Override // j$.util.stream.e2
    public final void accept(long j) {
        long[] jArr = this.c;
        int i = this.d;
        this.d = i + 1;
        jArr[i] = j;
    }

    @Override // j$.util.stream.Z1, j$.util.stream.e2
    public final void m() {
        int i = 0;
        Arrays.sort(this.c, 0, this.d);
        long j = this.d;
        e2 e2Var = this.a;
        e2Var.n(j);
        if (this.b) {
            while (i < this.d && !e2Var.q()) {
                e2Var.accept(this.c[i]);
                i++;
            }
        } else {
            while (i < this.d) {
                e2Var.accept(this.c[i]);
                i++;
            }
        }
        e2Var.m();
        this.c = null;
    }

    @Override // j$.util.stream.Z1, j$.util.stream.e2
    public final void n(long j) {
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        this.c = new long[(int) j];
    }
}
