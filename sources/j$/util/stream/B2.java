package j$.util.stream;

import java.util.Arrays;

/* loaded from: classes2.dex */
final class B2 extends p2 {
    private double[] c;
    private int d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public B2(e2 e2Var) {
        super(e2Var);
    }

    @Override // j$.util.stream.e2, j$.util.function.n
    public final void accept(double d) {
        double[] dArr = this.c;
        int i = this.d;
        this.d = i + 1;
        dArr[i] = d;
    }

    @Override // j$.util.stream.X1, j$.util.stream.e2
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

    @Override // j$.util.stream.X1, j$.util.stream.e2
    public final void n(long j) {
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        this.c = new double[(int) j];
    }
}
