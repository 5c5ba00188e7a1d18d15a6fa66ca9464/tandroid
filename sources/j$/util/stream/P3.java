package j$.util.stream;

import java.util.Arrays;
/* loaded from: classes2.dex */
final class P3 extends D3 {
    private double[] c;
    private int d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public P3(n3 n3Var) {
        super(n3Var);
    }

    @Override // j$.util.stream.k3, j$.util.stream.n3
    public void accept(double d) {
        double[] dArr = this.c;
        int i = this.d;
        this.d = i + 1;
        dArr[i] = d;
    }

    @Override // j$.util.stream.g3, j$.util.stream.n3
    public void m() {
        int i = 0;
        Arrays.sort(this.c, 0, this.d);
        this.a.n(this.d);
        if (this.b) {
            while (i < this.d && !this.a.o()) {
                this.a.accept(this.c[i]);
                i++;
            }
        } else {
            while (i < this.d) {
                this.a.accept(this.c[i]);
                i++;
            }
        }
        this.a.m();
        this.c = null;
    }

    @Override // j$.util.stream.n3
    public void n(long j) {
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        this.c = new double[(int) j];
    }
}
