package j$.util.stream;

import java.util.Arrays;
/* loaded from: classes2.dex */
final class C2 extends q2 {
    private double[] c;
    private int d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public C2(f2 f2Var) {
        super(f2Var);
    }

    @Override // j$.util.stream.c2, j$.util.function.m
    public final void accept(double d) {
        double[] dArr = this.c;
        int i = this.d;
        this.d = i + 1;
        dArr[i] = d;
    }

    @Override // j$.util.stream.Y1, j$.util.stream.f2
    public final void end() {
        int i = 0;
        Arrays.sort(this.c, 0, this.d);
        f2 f2Var = this.a;
        f2Var.f(this.d);
        if (this.b) {
            while (i < this.d && !f2Var.h()) {
                f2Var.accept(this.c[i]);
                i++;
            }
        } else {
            while (i < this.d) {
                f2Var.accept(this.c[i]);
                i++;
            }
        }
        f2Var.end();
        this.c = null;
    }

    @Override // j$.util.stream.f2
    public final void f(long j) {
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        this.c = new double[(int) j];
    }
}
